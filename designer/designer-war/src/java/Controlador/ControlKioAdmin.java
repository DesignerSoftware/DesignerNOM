/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.ConexionesKioskos;
import Entidades.Empleados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarKioAdminInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlKioAdmin")
@SessionScoped
public class ControlKioAdmin implements Serializable {

   private static Logger log = Logger.getLogger(ControlKioAdmin.class);

   @EJB
   AdministrarKioAdminInterface administrarkioadmin;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //tabla
   private List<Empleados> listEmpleadosCK;
   private List<Empleados> listEmpleadosCKFiltrar;
   private Empleados empleadoCKSeleccionado;
   //lov
   private List<Empleados> lovEmpleadosCK;
   private List<Empleados> lovEmpleadosCKFiltrar;
   private Empleados lovCKSeleccionado;
   //
   private Empleados editarEmpleadoCK;
   private ConexionesKioskos ckSeleccionada;
   private ConexionesKioskos ckAux;
   private ConexionesKioskos editarCK;
   private List<ConexionesKioskos> listaCKModificar;
   private int bandera;
   private Column cedula, nombreempleado, empresa;
   private boolean aceptar;
   private boolean guardado;
   private int cualCelda, cualCeldaCk, tipoLista;
   private String altoTabla;
   private String infoRegistro, infoRegistroEmpleadosLov;
   private String respuesta1, respuesta2;
   private DataTable tablaC;
   private boolean activarLOV;
   private boolean activarMTodos;
   private boolean activarR1, activarR2;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlKioAdmin() {
      altoTabla = "140";
      listEmpleadosCK = null;
      lovEmpleadosCK = null;
      lovCKSeleccionado = null;
      aceptar = true;
      editarCK = new ConexionesKioskos();
      ckAux = new ConexionesKioskos();
      editarEmpleadoCK = new Empleados();
      listaCKModificar = new ArrayList<ConexionesKioskos>();
      cualCelda = -1;
      tipoLista = 0;
      guardado = true;
      activarLOV = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
      activarR1 = true;
      activarR2 = true;
      respuesta1 = "";
      respuesta2 = "";
   }

   public void limpiarListasValor() {
      lovEmpleadosCK = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarkioadmin.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         listEmpleadosCK = null;
         getListEmpleadosCK();
         if (listEmpleadosCK != null) {
            if (!listEmpleadosCK.isEmpty()) {
               empleadoCKSeleccionado = listEmpleadosCK.get(0);
            }
         }
         ckSeleccionada = null;
         getCkSeleccionada();
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "kioadmin";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
      } else {
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
//Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParaEnviar.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
      }
      limpiarListasValor();
   }

   public void modificarKioAdminRespuesta1(String Respuesta1) {
      if (empleadoCKSeleccionado != null) {
         if (ckSeleccionada.getActivo().equalsIgnoreCase("N")) {
            RequestContext.getCurrentInstance().execute("PF('usuarioInactivo').show()");
         } else if (Respuesta1 == null || Respuesta1.equals("")) {
            RequestContext.getCurrentInstance().execute("PF('respuestaVacia').show()");
         } else {
            ckSeleccionada.setRespuesta1(Respuesta1);
            if (listaCKModificar.isEmpty()) {
               listaCKModificar.add(ckSeleccionada);
            } else if (!listaCKModificar.contains(ckSeleccionada)) {
               listaCKModificar.add(ckSeleccionada);
            }
            guardado = false;
            activarR1 = false;
            respuesta1 = "";
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//        RequestContext.getCurrentInstance().update("form:respuesta1");
            RequestContext.getCurrentInstance().update("form:ModR1");
//            RequestContext.getCurrentInstance().execute("PF('respuestaGuardada').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void modificarKioAdminRespuesta2(String Respuesta2) {
      if (empleadoCKSeleccionado != null) {
         if (ckSeleccionada.getActivo().equalsIgnoreCase("N")) {
            RequestContext.getCurrentInstance().execute("PF('usuarioInactivo').show()");
         } else if (Respuesta2 == null || Respuesta2.equals("")) {
            RequestContext.getCurrentInstance().execute("PF('respuestaVacia').show()");
         } else {
            ckSeleccionada.setRespuesta2(Respuesta2);
            if (listaCKModificar.isEmpty()) {
               listaCKModificar.add(ckSeleccionada);
            } else if (!listaCKModificar.contains(ckSeleccionada)) {
               listaCKModificar.add(ckSeleccionada);
            }
            guardado = false;
            activarR2 = false;
            respuesta2 = "";
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//        RequestContext.getCurrentInstance().update("form:respuesta2");
            RequestContext.getCurrentInstance().update("form:ModR2");
//            RequestContext.getCurrentInstance().execute("PF('respuestaGuardada').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void cambiarIndice(Empleados empl, int celda) {
      empleadoCKSeleccionado = empl;
      cualCelda = celda;
//        fechaFin = vigenciaTablaSeleccionada.getFechafinal();
//        fechaIni = vigenciaTablaSeleccionada.getFechainicial();
      empleadoCKSeleccionado.getSecuencia();
      if (cualCelda == 0) {
         empleadoCKSeleccionado.getCodigoempleado();
      } else if (cualCelda == 1) {
         empleadoCKSeleccionado.getPersona().getNombreCompleto();
      } else if (cualCelda == 2) {
         empleadoCKSeleccionado.getEmpresa().getNombre();
      }

      ckSeleccionada = administrarkioadmin.listCK(empleadoCKSeleccionado.getSecuencia());
      RequestContext.getCurrentInstance().update("form:activo");
      RequestContext.getCurrentInstance().update("form:pregunta1");
      RequestContext.getCurrentInstance().update("form:respuesta1");
      RequestContext.getCurrentInstance().update("form:pregunta2");
      RequestContext.getCurrentInstance().update("form:respuesta2");
   }

   public void guardarSalir() {
      guardarCambios();
      salir();
   }

   public void cancelarSalir() {
      cancelarModificacion();
      salir();
   }

   public void guardarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listaCKModificar.isEmpty()) {
               administrarkioadmin.editarCK(listaCKModificar);
               listaCKModificar.clear();
            }
//                listVigenciasDeportes = null;
//                getListVigenciasDeportes();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistrosVD();
         }
         guardado = true;
         activarR1 = true;
         activarR2 = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosKioAdmin");
         RequestContext.getCurrentInstance().update("form:activo");
         RequestContext.getCurrentInstance().update("form:pregunta1");
         RequestContext.getCurrentInstance().update("form:pregunta2");
         RequestContext.getCurrentInstance().update("form:respuesta1");
         RequestContext.getCurrentInstance().update("form:respuesta2");
         RequestContext.getCurrentInstance().update("form:ModR1");
         RequestContext.getCurrentInstance().update("form:ModR2");
      } catch (Exception e) {
         log.warn("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {

         cedula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:veFechaInicial");
         cedula.setFilterStyle("display: none; visibility: hidden;");
         nombreempleado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:veFechaFinal");
         nombreempleado.setFilterStyle("display: none; visibility: hidden;");
         empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:veDescripcion");
         empresa.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosKioAdmin");
         bandera = 0;
         listEmpleadosCKFiltrar = null;
         tipoLista = 0;
         altoTabla = "140";
      }
      listaCKModificar.clear();
      listEmpleadosCK = null;
      empleadoCKSeleccionado = null;
      guardado = true;
      ckSeleccionada = null;
      activarR1 = true;
      activarR2 = true;
      getListEmpleadosCK();
      contarRegistrosVD();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosKioAdmin");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {

      if (empleadoCKSeleccionado != null) {
         editarEmpleadoCK = empleadoCKSeleccionado;
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCedula");
            RequestContext.getCurrentInstance().execute("PF('editarCedula').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreEmpleado");
            RequestContext.getCurrentInstance().execute("PF('editarNombreEmpleado').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreEmpresa");
            RequestContext.getCurrentInstance().execute("PF('editarNombreEmpresa').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         altoTabla = "120";
         cedula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:cedula");
         cedula.setFilterStyle("width: 85% !important");
         nombreempleado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:nombreempleado");
         nombreempleado.setFilterStyle("width: 85% !important");
         empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:empresa");
         empresa.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosKioAdmin");
         bandera = 1;
      } else if (bandera == 1) {
         altoTabla = "140";
         cedula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:cedula");
         cedula.setFilterStyle("display: none; visibility: hidden;");
         nombreempleado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:nombreempleado");
         nombreempleado.setFilterStyle("display: none; visibility: hidden;");
         empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:empresa");
         empresa.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosKioAdmin");
         bandera = 0;
         listEmpleadosCKFiltrar = null;
         tipoLista = 0;
      }
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         altoTabla = "140";
         cedula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:cedula");
         cedula.setFilterStyle("display: none; visibility: hidden;");
         nombreempleado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:nombreempleado");
         nombreempleado.setFilterStyle("display: none; visibility: hidden;");
         empresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosKioAdmin:empresa");
         empresa.setFilterStyle("display: none; visibility: hidden;");
         contarRegistrosVD();
         RequestContext.getCurrentInstance().update("form:datosKioAdmin");
         bandera = 0;
         listEmpleadosCKFiltrar = null;
         tipoLista = 0;
      }
      limpiarListasValor();
      listaCKModificar.clear();
      empleadoCKSeleccionado = null;
      listEmpleadosCK = null;
      guardado = true;
      activarR1 = true;
      activarR2 = true;
      navegar("atras");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosKioAdminExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "KioAdminPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosKioAdminExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "KioAdminXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }
   //EVENTO FILTRAR

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistrosVD();
   }

   public void contarRegistrosVD() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");

   }

   public void limpiarCk() {
      ckSeleccionada = new ConexionesKioskos();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (ckSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(ckSeleccionada.getSecuencia(), "KIOADMIN");
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("KIOADMIN")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void abrirLista(int listaV) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaV == 0) {
         cargarLovEmpleados();
         contarRegistroEmpleadosLov();
         RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      }
   }

   public void actualizarEmpleadosNovedad() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listEmpleadosCK.isEmpty()) {
         listEmpleadosCK.clear();
         listEmpleadosCK.add(lovCKSeleccionado);
         empleadoCKSeleccionado = listEmpleadosCK.get(0);
      }
      ckSeleccionada = null;
      ckSeleccionada = administrarkioadmin.listCK(empleadoCKSeleccionado.getSecuencia());
      aceptar = true;
      cualCelda = -1;
      activarMTodos = false;
      contarRegistrosVD();
      RequestContext.getCurrentInstance().update("form:activo");
      RequestContext.getCurrentInstance().update("form:pregunta1");
      RequestContext.getCurrentInstance().update("form:pregunta2");
      RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");
      context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:datosKioAdmin");
      RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
   }

   public void cancelarCambioEmpleados() {
      lovEmpleadosCKFiltrar = null;
      lovCKSeleccionado = null;
      aceptar = true;
      cualCelda = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formLovEmpleados:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formLovEmpleados:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formLovEmpleados:aceptarE");
      context.reset("formLovEmpleados:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
   }

   public void mostrarTodos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listEmpleadosCK.isEmpty()) {
         listEmpleadosCK.clear();
      }
      if (lovEmpleadosCK != null) {
         for (int i = 0; i < lovEmpleadosCK.size(); i++) {
            listEmpleadosCK.add(lovEmpleadosCK.get(i));
         }
      }
      empleadoCKSeleccionado = listEmpleadosCK.get(0);
      ckSeleccionada = null;
      ckSeleccionada = administrarkioadmin.listCK(empleadoCKSeleccionado.getSecuencia());
      contarRegistrosVD();
      aceptar = true;
      cualCelda = -1;
      activarMTodos = true;
      RequestContext.getCurrentInstance().update("form:activo");
      RequestContext.getCurrentInstance().update("form:pregunta1");
      RequestContext.getCurrentInstance().update("form:pregunta2");
      RequestContext.getCurrentInstance().update("form:datosKioAdmin");
      RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
   }

   public void contarRegistroEmpleadosLov() {
      RequestContext.getCurrentInstance().update("formLovEmpleados:infoRegistroEmpleadosLOV");
   }

   public void cargarLovEmpleados() {
      if (lovEmpleadosCK == null) {
         lovEmpleadosCK = administrarkioadmin.listEmpleadosCK();
      }
   }

   public void unlockUsuario() {
      if (ckSeleccionada != null) {
         if (ckSeleccionada.getActivo().equalsIgnoreCase("N")) {
            administrarkioadmin.unlockUsuario(ckSeleccionada.getEmpleado().getSecuencia());
            RequestContext.getCurrentInstance().execute("PF('usuarioUnlock').show()");
            guardado = false;
            activarR1 = true;
            activarR2 = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosKioAdmin");
            RequestContext.getCurrentInstance().update("form:activo");
            RequestContext.getCurrentInstance().update("form:pregunta1");
            RequestContext.getCurrentInstance().update("form:pregunta2");
            RequestContext.getCurrentInstance().update("form:respuesta1");
            RequestContext.getCurrentInstance().update("form:respuesta2");
            RequestContext.getCurrentInstance().update("form:ModR1");
            RequestContext.getCurrentInstance().update("form:ModR2");
         } else {
            RequestContext.getCurrentInstance().execute("PF('usuarioActivo').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void resetUsuario() {
      if (ckSeleccionada != null) {
         administrarkioadmin.resetUsuario(ckSeleccionada.getEmpleado().getSecuencia());
         RequestContext.getCurrentInstance().execute("PF('usuarioReset').show()");
         guardado = false;
         activarR1 = true;
         activarR2 = true;
         listEmpleadosCK = null;
         getListEmpleadosCK();
         if (listEmpleadosCK == null || listEmpleadosCK.isEmpty()) {
            listEmpleadosCK = new ArrayList<>();
         } else {
            empleadoCKSeleccionado = listEmpleadosCK.get(0);
         }
         ckSeleccionada = null;
         contarRegistrosVD();
         getCkSeleccionada();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosKioAdmin");
         RequestContext.getCurrentInstance().update("form:activo");
         RequestContext.getCurrentInstance().update("form:pregunta1");
         RequestContext.getCurrentInstance().update("form:pregunta2");
         RequestContext.getCurrentInstance().update("form:respuesta1");
         RequestContext.getCurrentInstance().update("form:respuesta2");
         RequestContext.getCurrentInstance().update("form:ModR1");
         RequestContext.getCurrentInstance().update("form:ModR2");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //////////SETS Y GETS///////////////
   public List<Empleados> getListEmpleadosCK() {
      if (listEmpleadosCK == null) {
         listEmpleadosCK = administrarkioadmin.listEmpleadosCK();
      }
      return listEmpleadosCK;
   }

   public void setListEmpleadosCK(List<Empleados> listEmpleadosCK) {
      this.listEmpleadosCK = listEmpleadosCK;
   }

   public List<Empleados> getListEmpleadosCKFiltrar() {
      return listEmpleadosCKFiltrar;
   }

   public void setListEmpleadosCKFiltrar(List<Empleados> listEmpleadosCKFiltrar) {
      this.listEmpleadosCKFiltrar = listEmpleadosCKFiltrar;
   }

   public Empleados getEmpleadoCKSeleccionado() {
      if (empleadoCKSeleccionado != null) {
         ckSeleccionada = administrarkioadmin.listCK(empleadoCKSeleccionado.getSecuencia());
      }

      return empleadoCKSeleccionado;
   }

   public void setEmpleadoCKSeleccionado(Empleados empleadoCKSeleccionado) {
      this.empleadoCKSeleccionado = empleadoCKSeleccionado;
   }

   public Empleados getEditarEmpleadoCK() {
      return editarEmpleadoCK;
   }

   public void setEditarEmpleadoCK(Empleados editarEmpleadoCK) {
      this.editarEmpleadoCK = editarEmpleadoCK;
   }

   public ConexionesKioskos getCkSeleccionada() {
      if (empleadoCKSeleccionado != null) {
         ckSeleccionada = administrarkioadmin.listCK(empleadoCKSeleccionado.getSecuencia());
      } else {
         ckSeleccionada = new ConexionesKioskos();
      }
      return ckSeleccionada;
   }

   public void setCkSeleccionada(ConexionesKioskos ckSeleccionada) {
      this.ckSeleccionada = ckSeleccionada;
   }

   public ConexionesKioskos getEditarCK() {
      return editarCK;
   }

   public void setEditarCK(ConexionesKioskos editarCK) {
      this.editarCK = editarCK;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosKioAdmin");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

   public List<Empleados> getLovEmpleadosCK() {
      return lovEmpleadosCK;
   }

   public void setLovEmpleadosCK(List<Empleados> lovEmpleadosCK) {
      this.lovEmpleadosCK = lovEmpleadosCK;
   }

   public List<Empleados> getLovEmpleadosCKFiltrar() {
      return lovEmpleadosCKFiltrar;
   }

   public void setLovEmpleadosCKFiltrar(List<Empleados> lovEmpleadosCKFiltrar) {
      this.lovEmpleadosCKFiltrar = lovEmpleadosCKFiltrar;
   }

   public Empleados getLovCKSeleccionado() {
      return lovCKSeleccionado;
   }

   public void setLovCKSeleccionado(Empleados lovCKSeleccionado) {
      this.lovCKSeleccionado = lovCKSeleccionado;
   }

   public String getInfoRegistroEmpleadosLov() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovEmpleados:LOVEmpleados");
      if (lovEmpleadosCKFiltrar != null) {
         if (lovEmpleadosCKFiltrar.size() == 1) {
            lovCKSeleccionado = lovEmpleadosCKFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();PF('LOVEmpleados').selectRow(0);");
         } else {
            lovCKSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();");
         }
      } else {
         lovCKSeleccionado = null;
         aceptar = true;
      }
      infoRegistroEmpleadosLov = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadosLov;
   }

   public void setInfoRegistroEmpleadosLov(String infoRegistroEmpleadosLov) {
      this.infoRegistroEmpleadosLov = infoRegistroEmpleadosLov;
   }

   public boolean isActivarMTodos() {
      return activarMTodos;
   }

   public void setActivarMTodos(boolean activarMTodos) {
      this.activarMTodos = activarMTodos;
   }

   public boolean isActivarR1() {
      return activarR1;
   }

   public void setActivarR1(boolean activarR1) {
      this.activarR1 = activarR1;
   }

   public boolean isActivarR2() {
      return activarR2;
   }

   public void setActivarR2(boolean activarR2) {
      this.activarR2 = activarR2;
   }

   public ConexionesKioskos getCkAux() {
      return ckAux;
   }

   public void setCkAux(ConexionesKioskos ckAux) {
      this.ckAux = ckAux;
   }

   public String getRespuesta1() {
      return respuesta1;
   }

   public void setRespuesta1(String respuesta1) {
      this.respuesta1 = respuesta1;
   }

   public String getRespuesta2() {
      return respuesta2;
   }

   public void setRespuesta2(String respuesta2) {
      this.respuesta2 = respuesta2;
   }

}
