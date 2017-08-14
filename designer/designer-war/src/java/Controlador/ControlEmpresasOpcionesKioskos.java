/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Empresas;
import Entidades.EmpresasOpcionesKioskos;
import Entidades.OpcionesKioskos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmpresasInterface;
import InterfaceAdministrar.AdministrarEmpresasOpcionesKioskosInterface;
import InterfaceAdministrar.AdministrarOpcionesKioskosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
@Named(value = "controlEmpresasOpcionesKioskos")
@SessionScoped
public class ControlEmpresasOpcionesKioskos implements Serializable {

   private static Logger log = Logger.getLogger(ControlEmpresasOpcionesKioskos.class);

   @EJB
   AdministrarEmpresasOpcionesKioskosInterface administrarEmpresasOK;
   @EJB
   AdministrarEmpresasInterface administrarEmpresas;
   @EJB
   AdministrarOpcionesKioskosInterface administrarOK;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<EmpresasOpcionesKioskos> listEmpresasOk;
   private List<EmpresasOpcionesKioskos> listEmpresasOkFiltrar;
   private List<EmpresasOpcionesKioskos> listEmpresasOkCrear;
   private List<EmpresasOpcionesKioskos> listEmpresasOkModificar;
   private List<EmpresasOpcionesKioskos> listEmpresasOkBorrar;
   private EmpresasOpcionesKioskos nuevaEmpresaOk;
   private EmpresasOpcionesKioskos duplicarEmpresaOk;
   private EmpresasOpcionesKioskos editarEmpresaOK;
   private EmpresasOpcionesKioskos empresaOkSeleccionada;
   private List<Empresas> lovEmpresas;
   private List<Empresas> lovEmpresasFiltrar;
   private Empresas empresaLovSeleccionda;
   private List<OpcionesKioskos> lovOpcionesKioskos;
   private List<OpcionesKioskos> lovOpcionesKioskosFiltrar;
   private OpcionesKioskos opcionKioskoLovSeleccionda;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   private Column empresa, opcionkiosko;
   private int registrosBorrados;
   private String mensajeValidacion;
   private int tamano;
   private boolean activarLov;
   private String infoRegistro, infoRegistroEmpresa, infoRegistroOK;
   private DataTable tablaC;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmpresasOpcionesKioskos() {
      listEmpresasOk = null;
      listEmpresasOkCrear = new ArrayList<EmpresasOpcionesKioskos>();
      listEmpresasOkModificar = new ArrayList<EmpresasOpcionesKioskos>();
      listEmpresasOkBorrar = new ArrayList<EmpresasOpcionesKioskos>();
      editarEmpresaOK = new EmpresasOpcionesKioskos();
      nuevaEmpresaOk = new EmpresasOpcionesKioskos();
      duplicarEmpresaOk = new EmpresasOpcionesKioskos();
      guardado = true;
      tamano = 330;
      cualCelda = -1;
      empresaOkSeleccionada = null;
      lovEmpresas = null;
      empresaLovSeleccionda = null;
      lovOpcionesKioskos = null;
      opcionKioskoLovSeleccionda = null;
      activarLov = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovEmpresas = null;
      lovOpcionesKioskos = null;
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }
   
   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEmpresasOK.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         administrarEmpresas.obtenerConexion(ses.getId());
         administrarOK.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listEmpresasOk = null;
      getListEmpresasOk();
      if (listEmpresasOk != null) {
         if (!listEmpresasOk.isEmpty()) {
            empresaOkSeleccionada = listEmpresasOk.get(0);
         }
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listEmpresasOk = null;
      getListEmpresasOk();
      if (listEmpresasOk != null) {
         empresaOkSeleccionada = listEmpresasOk.get(0);
      }
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "kioempresas";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
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

   public String redirigir() {
      return paginaAnterior;
   }

   public void cambiarIndice(EmpresasOpcionesKioskos empresa, int celda) {
      empresaOkSeleccionada = empresa;
      cualCelda = celda;
      empresaOkSeleccionada.getSecuencia();
      if (cualCelda == 0) {
         empresaOkSeleccionada.getEmpresa().getNombre();
      } else if (cualCelda == 1) {
         empresaOkSeleccionada.getOpcionkiosko().getDescripcion();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         empresa = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:empresa");
         empresa.setFilterStyle("display: none; visibility: hidden;");
         opcionkiosko = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:opcionkiosko");
         opcionkiosko.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listEmpresasOkFiltrar = null;
         tamano = 330;
         RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
         tipoLista = 0;
      }

      listEmpresasOkBorrar.clear();
      listEmpresasOkCrear.clear();
      listEmpresasOkModificar.clear();
      empresaOkSeleccionada = null;
      contarRegistros();
      k = 0;
      listEmpresasOk = null;
      guardado = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         empresa = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:empresa");
         empresa.setFilterStyle("display: none; visibility: hidden;");
         opcionkiosko = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:opcionkiosko");
         opcionkiosko.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
         bandera = 0;
         listEmpresasOkFiltrar = null;
         tipoLista = 0;
         tamano = 330;
      }
      listEmpresasOkBorrar.clear();
      listEmpresasOkCrear.clear();
      listEmpresasOkModificar.clear();
      empresaOkSeleccionada = null;
      k = 0;
      listEmpresasOk = null;
      guardado = true;
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 310;
         empresa = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:empresa");
         empresa.setFilterStyle("width: 85% !important;");
         opcionkiosko = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:opcionkiosko");
         opcionkiosko.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 330;
         empresa = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:empresa");
         empresa.setFilterStyle("display: none; visibility: hidden;");
         opcionkiosko = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:opcionkiosko");
         opcionkiosko.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
         bandera = 0;
         listEmpresasOkFiltrar = null;
         tipoLista = 0;
      }
   }

   public void modificarEmpresaOk(EmpresasOpcionesKioskos empok) {
      empresaOkSeleccionada = empok;
      if (!listEmpresasOkCrear.contains(empresaOkSeleccionada)) {
         if (listEmpresasOkModificar.isEmpty()) {
            listEmpresasOkModificar.add(empresaOkSeleccionada);
         } else if (!listEmpresasOkModificar.contains(empresaOkSeleccionada)) {
            listEmpresasOkModificar.add(empresaOkSeleccionada);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
   }

   public void borrarEmpresaOk() {
      if (empresaOkSeleccionada != null) {
         if (!listEmpresasOkModificar.isEmpty() && listEmpresasOkModificar.contains(empresaOkSeleccionada)) {
            listEmpresasOkModificar.remove(listEmpresasOkModificar.indexOf(empresaOkSeleccionada));
            listEmpresasOkBorrar.add(empresaOkSeleccionada);
         } else if (!listEmpresasOkCrear.isEmpty() && listEmpresasOkCrear.contains(empresaOkSeleccionada)) {
            listEmpresasOkCrear.remove(listEmpresasOkCrear.indexOf(empresaOkSeleccionada));
         } else {
            listEmpresasOkBorrar.add(empresaOkSeleccionada);
         }
         listEmpresasOk.remove(empresaOkSeleccionada);
         if (tipoLista == 1) {
            listEmpresasOkFiltrar.remove(empresaOkSeleccionada);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
         contarRegistros();
         empresaOkSeleccionada = null;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void revisarDialogoGuardar() {
      if (!listEmpresasOkBorrar.isEmpty() || !listEmpresasOkCrear.isEmpty() || !listEmpresasOkModificar.isEmpty()) {
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listEmpresasOkBorrar.isEmpty()) {
               administrarEmpresasOK.borrarEmpresasOpcionesKioskos(listEmpresasOkBorrar);
               registrosBorrados = listEmpresasOkBorrar.size();
               RequestContext.getCurrentInstance().update("form:mostrarBorrados");
               RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
               listEmpresasOkBorrar.clear();
            }
            if (!listEmpresasOkModificar.isEmpty()) {
               administrarEmpresasOK.modificarEmpresasOpcionesKioskos(listEmpresasOkModificar);
               listEmpresasOkModificar.clear();
            }
            if (!listEmpresasOkCrear.isEmpty()) {
               administrarEmpresasOK.crearEmpresasOpcionesKioskos(listEmpresasOkCrear);
               listEmpresasOkCrear.clear();
            }
            listEmpresasOk = null;
            getListEmpresasOk();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            empresaOkSeleccionada = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
      } catch (Exception e) {
         log.warn("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }

   }

   public void editarCelda() {
      if (empresaOkSeleccionada != null) {
         editarEmpresaOK = empresaOkSeleccionada;

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editEmpresa");
            RequestContext.getCurrentInstance().execute("PF('editEmpresa').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editOpcionKiosko");
            RequestContext.getCurrentInstance().execute("PF('editOpcionKiosko').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevaEmpresaOK() {
      int contador = 0;
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      mensajeValidacion = " ";

      if (nuevaEmpresaOk.getEmpresa().getSecuencia() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }
      if (nuevaEmpresaOk.getOpcionkiosko().getSecuencia() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         contador++;
      }

      for (int i = 0; i < listEmpresasOk.size(); i++) {
         if (nuevaEmpresaOk.getOpcionkiosko().getSecuencia() == listEmpresasOk.get(i).getOpcionkiosko().getSecuencia()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            duplicados++;
         }
      }

      if (contador != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaPregunta");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaPregunta').show()");
      }

      if (contador == 0 && duplicados == 0) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            empresa = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            opcionkiosko = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:opcionkiosko");
            opcionkiosko.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listEmpresasOkFiltrar = null;
            tipoLista = 0;
            tamano = 330;
            RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevaEmpresaOk.setSecuencia(l);
         listEmpresasOkCrear.add(nuevaEmpresaOk);
         listEmpresasOk.add(nuevaEmpresaOk);
         contarRegistros();
         empresaOkSeleccionada = nuevaEmpresaOk;
         nuevaEmpresaOk = new EmpresasOpcionesKioskos();
         RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEmpresaOK').hide()");
      }
   }

   public void limpiarNuevaEmpresaOK() {
      nuevaEmpresaOk = new EmpresasOpcionesKioskos();
   }

   public void duplicandoEmpresaOk() {
      if (empresaOkSeleccionada != null) {
         duplicarEmpresaOk = new EmpresasOpcionesKioskos();
         k++;
         l = BigInteger.valueOf(k);

         duplicarEmpresaOk.setSecuencia(l);
         duplicarEmpresaOk.setEmpresa(empresaOkSeleccionada.getEmpresa());
         duplicarEmpresaOk.setOpcionkiosko(empresaOkSeleccionada.getOpcionkiosko());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEmpresaOK').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      RequestContext context = RequestContext.getCurrentInstance();
      int contador = 0;
      int duplicados = 0;

      for (int i = 0; i < listEmpresasOk.size(); i++) {
         if (duplicarEmpresaOk.getOpcionkiosko().getSecuencia() == listEmpresasOk.get(i).getOpcionkiosko().getSecuencia()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
            RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
            duplicados++;
         }
      }

      if (contador == 0 && duplicados == 0) {
         listEmpresasOk.add(duplicarEmpresaOk);
         listEmpresasOkCrear.add(duplicarEmpresaOk);
         empresaOkSeleccionada = duplicarEmpresaOk;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            empresa = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            opcionkiosko = (Column) c.getViewRoot().findComponent("form:datosEmpresasOk:opcionkiosko");
            opcionkiosko.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listEmpresasOkFiltrar = null;
            RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
            tipoLista = 0;
         }
         duplicarEmpresaOk = new EmpresasOpcionesKioskos();
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroEmpresaOK");
      RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEmpresaOK').hide()");
   }

   public void limpiparDuplicarEmpresaOk() {
      duplicarEmpresaOk = new EmpresasOpcionesKioskos();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresasOkExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "EmpresasOpcionesKioskos", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresasOkExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "EmpresasOpcionesKioskos", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (empresaOkSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(empresaOkSeleccionada.getSecuencia(), "EMPRESASOPCIONESKIOSKOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("EMPRESASOPCIONESKIOSKOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosEmpresas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpresas");
   }

   public void contarRegistrosOpcionesKioskos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroOK");
   }

   public void asignarIndex(EmpresasOpcionesKioskos empresasok, int LND, int dialogo) {
      empresaOkSeleccionada = empresasok;
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = LND;
      if (dialogo == 1) {
         contarRegistrosEmpresas();
         RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");

      } else if (dialogo == 2) {
         contarRegistrosOpcionesKioskos();
         RequestContext.getCurrentInstance().update("form:OpcionesKioskosDialogo");
         RequestContext.getCurrentInstance().execute("PF('OpcionesKioskosDialogo').show()");
      }
   }

   public void actualizarEmpresa() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         empresaOkSeleccionada.setEmpresa(empresaLovSeleccionda);
         if (!listEmpresasOkCrear.contains(empresaOkSeleccionada)) {
            if (listEmpresasOkModificar.isEmpty()) {
               listEmpresasOkModificar.add(empresaOkSeleccionada);
            } else if (!listEmpresasOkModificar.contains(empresaOkSeleccionada)) {
               listEmpresasOkModificar.add(empresaOkSeleccionada);
            }
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         deshabilitarBotonLov();
         RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
      } else if (tipoActualizacion == 1) {
         nuevaEmpresaOk.setEmpresa(empresaLovSeleccionda);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresaOK");
      } else if (tipoActualizacion == 2) {
         duplicarEmpresaOk.setEmpresa(empresaLovSeleccionda);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
      }
      lovEmpresasFiltrar = null;
      empresaLovSeleccionda = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
      RequestContext.getCurrentInstance().update("form:lovEmpresas");
      RequestContext.getCurrentInstance().update("form:aceptarD");
      context.reset("form:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
   }

   public void cancelarCambioEmpresa() {
      lovEmpresasFiltrar = null;
      empresaLovSeleccionda = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
      RequestContext.getCurrentInstance().update("form:lovEmpresas");
      RequestContext.getCurrentInstance().update("form:aceptarD");
      RequestContext.getCurrentInstance().reset("form:lovEmpresas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
   }

   public void actualizarOpcionKiosko() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         empresaOkSeleccionada.setOpcionkiosko(opcionKioskoLovSeleccionda);
         if (!listEmpresasOkCrear.contains(empresaOkSeleccionada)) {
            if (listEmpresasOkModificar.isEmpty()) {
               listEmpresasOkModificar.add(empresaOkSeleccionada);
            } else if (!listEmpresasOkModificar.contains(empresaOkSeleccionada)) {
               listEmpresasOkModificar.add(empresaOkSeleccionada);
            }
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         deshabilitarBotonLov();
         RequestContext.getCurrentInstance().update("form:datosEmpresasOk");
      } else if (tipoActualizacion == 1) {
         nuevaEmpresaOk.setOpcionkiosko(opcionKioskoLovSeleccionda);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresaOK");
      } else if (tipoActualizacion == 2) {
         duplicarEmpresaOk.setOpcionkiosko(opcionKioskoLovSeleccionda);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
      }
      lovOpcionesKioskosFiltrar = null;
      opcionKioskoLovSeleccionda = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:OpcionesKioskosDialogo");
      RequestContext.getCurrentInstance().update("form:lovOK");
      RequestContext.getCurrentInstance().update("form:aceptarD");

      context.reset("form:lovOK:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOK').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('OpcionesKioskosDialogo').hide()");
   }

   public void cancelarCambioOpcionKiosko() {
      lovOpcionesKioskosFiltrar = null;
      aceptar = true;
      tipoActualizacion = -1;
      opcionKioskoLovSeleccionda = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovOK:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOK').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('OpcionesKioskosDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:OpcionesKioskosDialogo");
      RequestContext.getCurrentInstance().update("form:lovOK");
      RequestContext.getCurrentInstance().update("form:aceptarOK");
   }

   public void listaValoresBoton() {
      if (empresaOkSeleccionada != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            contarRegistrosEmpresas();
            RequestContext.getCurrentInstance().update("form:EmpresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 1) {
            contarRegistrosOpcionesKioskos();
            RequestContext.getCurrentInstance().update("form:OpcionesKioskosDialogo");
            RequestContext.getCurrentInstance().execute("PF('OpcionesKioskosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //////SETS Y GETS///
   public List<EmpresasOpcionesKioskos> getListEmpresasOk() {
      if (listEmpresasOk == null) {
         listEmpresasOk = administrarEmpresasOK.consultarEmpresasOpcionesKioskos();
      }
      return listEmpresasOk;
   }

   public void setListEmpresasOk(List<EmpresasOpcionesKioskos> listEmpresasOk) {
      this.listEmpresasOk = listEmpresasOk;
   }

   public EmpresasOpcionesKioskos getNuevaEmpresaOk() {
      return nuevaEmpresaOk;
   }

   public void setNuevaEmpresaOk(EmpresasOpcionesKioskos nuevaEmpresaOk) {
      this.nuevaEmpresaOk = nuevaEmpresaOk;
   }

   public EmpresasOpcionesKioskos getDuplicarEmpresaOk() {
      return duplicarEmpresaOk;
   }

   public void setDuplicarEmpresaOk(EmpresasOpcionesKioskos duplicarEmpresaOk) {
      this.duplicarEmpresaOk = duplicarEmpresaOk;
   }

   public EmpresasOpcionesKioskos getEditarEmpresaOK() {
      return editarEmpresaOK;
   }

   public void setEditarEmpresaOK(EmpresasOpcionesKioskos editarEmpresaOK) {
      this.editarEmpresaOK = editarEmpresaOK;
   }

   public EmpresasOpcionesKioskos getEmpresaOkSeleccionada() {
      return empresaOkSeleccionada;
   }

   public void setEmpresaOkSeleccionada(EmpresasOpcionesKioskos empresaOkSeleccionada) {
      this.empresaOkSeleccionada = empresaOkSeleccionada;
   }

   public List<Empresas> getLovEmpresas() {
      if (lovEmpresas == null) {
         lovEmpresas = administrarEmpresas.listaEmpresas();
      }
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> lovEmpresas) {
      this.lovEmpresas = lovEmpresas;
   }

   public List<Empresas> getLovEmpresasFiltrar() {
      return lovEmpresasFiltrar;
   }

   public void setLovEmpresasFiltrar(List<Empresas> lovEmpresasFiltrar) {
      this.lovEmpresasFiltrar = lovEmpresasFiltrar;
   }

   public Empresas getEmpresaLovSeleccionda() {
      return empresaLovSeleccionda;
   }

   public void setEmpresaLovSeleccionda(Empresas empresaLovSeleccionda) {
      this.empresaLovSeleccionda = empresaLovSeleccionda;
   }

   public List<OpcionesKioskos> getLovOpcionesKioskos() {
      if (lovOpcionesKioskos == null) {
         lovOpcionesKioskos = administrarOK.consultarOpcionesKioskos();
      }
      return lovOpcionesKioskos;
   }

   public void setLovOpcionesKioskos(List<OpcionesKioskos> lovOpcionesKioskos) {
      this.lovOpcionesKioskos = lovOpcionesKioskos;
   }

   public List<OpcionesKioskos> getLovOpcionesKioskosFiltrar() {
      return lovOpcionesKioskosFiltrar;
   }

   public void setLovOpcionesKioskosFiltrar(List<OpcionesKioskos> lovOpcionesKioskosFiltrar) {
      this.lovOpcionesKioskosFiltrar = lovOpcionesKioskosFiltrar;
   }

   public OpcionesKioskos getOpcionKioskoLovSeleccionda() {
      return opcionKioskoLovSeleccionda;
   }

   public void setOpcionKioskoLovSeleccionda(OpcionesKioskos opcionKioskoLovSeleccionda) {
      this.opcionKioskoLovSeleccionda = opcionKioskoLovSeleccionda;
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

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpresasOk");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresas");
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
      this.infoRegistroEmpresa = infoRegistroEmpresa;
   }

   public String getInfoRegistroOK() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOK");
      infoRegistroOK = String.valueOf(tabla.getRowCount());
      return infoRegistroOK;
   }

   public void setInfoRegistroOK(String infoRegistroOK) {
      this.infoRegistroOK = infoRegistroOK;
   }

   public List<EmpresasOpcionesKioskos> getListEmpresasOkFiltrar() {
      return listEmpresasOkFiltrar;
   }

   public void setListEmpresasOkFiltrar(List<EmpresasOpcionesKioskos> listEmpresasOkFiltrar) {
      this.listEmpresasOkFiltrar = listEmpresasOkFiltrar;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

}
