/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Departamentos;
import Entidades.Paises;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDepartamentosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
@ManagedBean
@SessionScoped
public class ControlDepartamentos implements Serializable {

   private static Logger log = Logger.getLogger(ControlDepartamentos.class);

   @EJB
   AdministrarDepartamentosInterface administrarDepartamentos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<Departamentos> listDepartamentos;
   private List<Departamentos> filtrarDepartamentos;
   private List<Departamentos> crearDepartamentos;
   private List<Departamentos> modificarDepartamentos;
   private List<Departamentos> borrarDepartamentos;
   private Departamentos departamentoSeleccionado;
   private Departamentos nuevoDepartamentos;
   private Departamentos duplicarDepartamentos;
   private Departamentos editarDepartamentos;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   private boolean permitirIndex;
   private Column codigo, descripcion, pais;
   private int registrosBorrados;
   private String mensajeValidacion;
   private int tamano;
   private String paises;
   private List<Paises> lovPaises;
   private List<Paises> filtrarLovPaises;
   private Paises paislovSeleccionado;
   private String nuevoYduplicarCompletarPais;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private boolean activarLov;

   public ControlDepartamentos() {
      listDepartamentos = null;
      crearDepartamentos = new ArrayList<Departamentos>();
      modificarDepartamentos = new ArrayList<Departamentos>();
      borrarDepartamentos = new ArrayList<Departamentos>();
      permitirIndex = true;
      editarDepartamentos = new Departamentos();
      nuevoDepartamentos = new Departamentos();
      nuevoDepartamentos.setPais(new Paises());
      duplicarDepartamentos = new Departamentos();
      duplicarDepartamentos.setPais(new Paises());
      lovPaises = null;
      filtrarLovPaises = null;
      guardado = true;
      tamano = 320;
      aceptar = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
      activarLov = true;
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
      String pagActual = "departamento";
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

   public void limpiarListasValor() {
      lovPaises = null;
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
         administrarDepartamentos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         listDepartamentos = null;
         getListDepartamentos();
         if (listDepartamentos != null) {
            if (!listDepartamentos.isEmpty()) {
               departamentoSeleccionado = listDepartamentos.get(0);
            }
         }
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void cambiarIndice(Departamentos departamento, int celda) {
      if (permitirIndex == true) {
         departamentoSeleccionado = departamento;
         cualCelda = celda;
         departamentoSeleccionado.getSecuencia();
         if (cualCelda == 0) {
            deshabilitarBotonLov();
            departamentoSeleccionado.getCodigo();
         } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            departamentoSeleccionado.getNombre();
         }
         if (cualCelda == 2) {
            habilitarBotonLov();
            departamentoSeleccionado.getPais().getNombre();
         }
      }
   }

   public void asignarIndex(Departamentos departamento, int LND, int dig) {
      departamentoSeleccionado = departamento;
      tipoActualizacion = LND;
      if (dig == 2) {
         lovPaises = null;
         getLovPaises();
         RequestContext.getCurrentInstance().update("form:paisesDialogo");
         RequestContext.getCurrentInstance().execute("PF('paisesDialogo').show()");
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (departamentoSeleccionado != null) {
         if (cualCelda == 2) {
            lovPaises = null;
            getLovPaises();
            RequestContext.getCurrentInstance().update("form:paisesDialogo");
            RequestContext.getCurrentInstance().execute("PF('paisesDialogo').show()");
         }
      }
   }

   public void cancelarYSalir() {
//      cancelarModificacion();
      salir();
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDepartamentos");
         bandera = 0;
         filtrarDepartamentos = null;
         tipoLista = 0;
      }
      borrarDepartamentos.clear();
      crearDepartamentos.clear();
      modificarDepartamentos.clear();
      departamentoSeleccionado = null;
      k = 0;
      listDepartamentos = null;
      guardado = true;
      permitirIndex = true;
      getListDepartamentos();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosDepartamentos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDepartamentos");
         bandera = 0;
         filtrarDepartamentos = null;
         tipoLista = 0;
      }
      borrarDepartamentos.clear();
      crearDepartamentos.clear();
      modificarDepartamentos.clear();
      departamentoSeleccionado = null;
      k = 0;
      listDepartamentos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosDepartamentos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 300;
         codigo = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         pais = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:pais");
         pais.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosDepartamentos");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 320;
         codigo = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         pais = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:pais");
         pais.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDepartamentos");
         bandera = 0;
         filtrarDepartamentos = null;
         tipoLista = 0;
      }
   }

   public void actualizarPais() {
      if (tipoActualizacion == 0) {
         departamentoSeleccionado.setPais(paislovSeleccionado);
         if (!crearDepartamentos.contains(departamentoSeleccionado)) {
            if (modificarDepartamentos.isEmpty()) {
               modificarDepartamentos.add(departamentoSeleccionado);
            } else if (!modificarDepartamentos.contains(departamentoSeleccionado)) {
               modificarDepartamentos.add(departamentoSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosDepartamentos");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         nuevoDepartamentos.setPais(paislovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPais");
      } else if (tipoActualizacion == 2) {
         duplicarDepartamentos.setPais(paislovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPais");
      }
      filtrarLovPaises = null;
      paislovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;

      RequestContext.getCurrentInstance().update("form:paisesDialogo");
      RequestContext.getCurrentInstance().update("form:lovPaises");
      RequestContext.getCurrentInstance().update("form:aceptarS");
      RequestContext.getCurrentInstance().reset("form:lovPaises:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPaises').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('paisesDialogo').hide()");
   }

   public void cancelarCambioPais() {
      filtrarLovPaises = null;
      paislovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:paisesDialogo");
      RequestContext.getCurrentInstance().update("form:lovPaises");
      RequestContext.getCurrentInstance().update("form:aceptarS");
      RequestContext.getCurrentInstance().reset("form:lovPaises:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPaises').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('paisesDialogo').hide()");
   }

   public void modificarDepartamentos(Departamentos departamento) {
      departamentoSeleccionado = departamento;
      if (!crearDepartamentos.contains(departamentoSeleccionado)) {
         if (modificarDepartamentos.isEmpty()) {
            modificarDepartamentos.add(departamentoSeleccionado);
         } else if (!modificarDepartamentos.contains(departamentoSeleccionado)) {
            modificarDepartamentos.add(departamentoSeleccionado);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosDepartamentos");
   }

   public void borrandoDepartamentos() {
      if (departamentoSeleccionado != null) {
         if (!modificarDepartamentos.isEmpty() && modificarDepartamentos.contains(departamentoSeleccionado)) {
            int modIndex = modificarDepartamentos.indexOf(departamentoSeleccionado);
            modificarDepartamentos.remove(modIndex);
            borrarDepartamentos.add(departamentoSeleccionado);
         } else if (!crearDepartamentos.isEmpty() && crearDepartamentos.contains(departamentoSeleccionado)) {
            int crearIndex = crearDepartamentos.indexOf(departamentoSeleccionado);
            crearDepartamentos.remove(crearIndex);
         } else {
            borrarDepartamentos.add(departamentoSeleccionado);
         }
         listDepartamentos.remove(departamentoSeleccionado);
         if (tipoLista == 1) {
            filtrarDepartamentos.remove(departamentoSeleccionado);
         }
         contarRegistros();
         departamentoSeleccionado = null;
         RequestContext.getCurrentInstance().update("form:datosDepartamentos");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void verificarBorrado() {
      BigInteger contarBienProgramacionesDepartamento;
      BigInteger contarCapModulosDepartamento;
      BigInteger contarCiudadesDepartamento;
      BigInteger contarSoAccidentesMedicosDepartamento;
      try {
         contarBienProgramacionesDepartamento = administrarDepartamentos.contarBienProgramacionesDepartamento(departamentoSeleccionado.getSecuencia());
         contarCapModulosDepartamento = administrarDepartamentos.contarCapModulosDepartamento(departamentoSeleccionado.getSecuencia());
         contarCiudadesDepartamento = administrarDepartamentos.contarCiudadesDepartamento(departamentoSeleccionado.getSecuencia());
         contarSoAccidentesMedicosDepartamento = administrarDepartamentos.contarSoAccidentesMedicosDepartamento(departamentoSeleccionado.getSecuencia());
         if (contarBienProgramacionesDepartamento.equals(new BigInteger("0"))
                 && contarCapModulosDepartamento.equals(new BigInteger("0"))
                 && contarCiudadesDepartamento.equals(new BigInteger("0"))
                 && contarSoAccidentesMedicosDepartamento.equals(new BigInteger("0"))) {
            borrandoDepartamentos();
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            contarBienProgramacionesDepartamento = new BigInteger("-1");
            contarCapModulosDepartamento = new BigInteger("-1");
            contarCiudadesDepartamento = new BigInteger("-1");
            contarSoAccidentesMedicosDepartamento = new BigInteger("-1");
         }
      } catch (Exception e) {
         log.error("ERROR ControlDepartamentos verificarBorrado ERROR  ", e);
      }
   }

   public void revisarDialogoGuardar() {
      if (!borrarDepartamentos.isEmpty() || !crearDepartamentos.isEmpty() || !modificarDepartamentos.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarYSalir() {
      guardarDepartamentos();
      salir();
   }

   public void guardarDepartamentos() {
      try {
         if (guardado == false) {
            if (!borrarDepartamentos.isEmpty()) {
               administrarDepartamentos.borrarDepartamentos(borrarDepartamentos);
               registrosBorrados = borrarDepartamentos.size();
               RequestContext.getCurrentInstance().update("form:mostrarBorrados");
               RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
               borrarDepartamentos.clear();
            }
            if (!modificarDepartamentos.isEmpty()) {
               administrarDepartamentos.modificarDepartamentos(modificarDepartamentos);
               modificarDepartamentos.clear();
            }
            if (!crearDepartamentos.isEmpty()) {
               administrarDepartamentos.crearDepartamentos(crearDepartamentos);
               crearDepartamentos.clear();
            }
            listDepartamentos = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosDepartamentos");
            k = 0;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } catch (Exception e) {
         log.info("ControlDepartamentos.guardarDepartamentos()" + e.getMessage());
         FacesMessage msg = new FacesMessage("Información", "Hubo un error en el guardado. Intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void editarCelda() {
      if (departamentoSeleccionado != null) {
         editarDepartamentos = departamentoSeleccionado;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
            RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
            cualCelda = -1;

         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
            RequestContext.getCurrentInstance().execute("PF('editPais').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoDepartamentos() {
      int contador = 0;
      int duplicados = 0;
      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoDepartamentos.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listDepartamentos.size(); x++) {
            if (listDepartamentos.get(x).getCodigo() == nuevoDepartamentos.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado ya está en uno. Por favor ingrese un código válido";
         } else {
            log.info("bandera");
            contador++;
         }
      }
      if (nuevoDepartamentos.getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         log.info("bandera");
         contador++;
      }
      if (nuevoDepartamentos.getPais().getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }

      if (contador == 3) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDepartamentos");
            bandera = 0;
            filtrarDepartamentos = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoDepartamentos.setSecuencia(l);
         crearDepartamentos.add(nuevoDepartamentos);
         listDepartamentos.add(nuevoDepartamentos);
         departamentoSeleccionado = nuevoDepartamentos;
         nuevoDepartamentos = new Departamentos();
         nuevoDepartamentos.setPais(new Paises());
         RequestContext.getCurrentInstance().update("form:datosDepartamentos");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroDepartamentos').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoDepartamento");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoDepartamento').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoDepartamentos() {
      nuevoDepartamentos = new Departamentos();
      nuevoDepartamentos.setPais(new Paises());
   }

   public void duplicandoDepartamentos() {
      if (departamentoSeleccionado != null) {
         duplicarDepartamentos = new Departamentos();
         duplicarDepartamentos.setPais(new Paises());
         k++;
         l = BigInteger.valueOf(k);
         duplicarDepartamentos.setSecuencia(l);
         duplicarDepartamentos.setCodigo(departamentoSeleccionado.getCodigo());
         duplicarDepartamentos.setNombre(departamentoSeleccionado.getNombre());
         duplicarDepartamentos.getPais().setNombre(departamentoSeleccionado.getPais().getNombre());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroDepartamentos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      Integer a = 0;
      a = null;
      if (duplicarDepartamentos.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listDepartamentos.size(); x++) {
            if (listDepartamentos.get(x).getCodigo() == duplicarDepartamentos.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado ya está en uno. Por favor ingrese un código válido";
         } else {
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarDepartamentos.getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }
      if (duplicarDepartamentos.getPais().getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }

      if (contador == 3) {
         listDepartamentos.add(duplicarDepartamentos);
         crearDepartamentos.add(duplicarDepartamentos);
         departamentoSeleccionado = duplicarDepartamentos;
         RequestContext.getCurrentInstance().update("form:datosDepartamentos");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         contarRegistros();
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosDepartamentos:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDepartamentos");
            bandera = 0;
            filtrarDepartamentos = null;
            tipoLista = 0;
         }
         duplicarDepartamentos = new Departamentos();
         duplicarDepartamentos.setPais(new Paises());
         RequestContext.getCurrentInstance().execute("duplicarRegistroDepartamentos').hide()");
      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarDepartamentos() {
      duplicarDepartamentos = new Departamentos();
      duplicarDepartamentos.setPais(new Paises());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDepartamentosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Departamentos", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDepartamentosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Departamentos", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (departamentoSeleccionado != null) {
         log.info("lol 2");
         int resultado = administrarRastros.obtenerTabla(departamentoSeleccionado.getSecuencia(), "DEPARTAMENTOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
         log.info("resultado: " + resultado);
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
      } else if (administrarRastros.verificarHistoricosTabla("DEPARTAMENTOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosPaises() {
      RequestContext.getCurrentInstance().update("form:infoRegistroPaises");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<Departamentos> getListDepartamentos() {
      if (listDepartamentos == null) {
         listDepartamentos = administrarDepartamentos.consultarDepartamentos();
      }
      return listDepartamentos;
   }

   public void setListDepartamentos(List<Departamentos> listDepartamentos) {
      this.listDepartamentos = listDepartamentos;
   }

   public List<Departamentos> getFiltrarDepartamentos() {
      return filtrarDepartamentos;
   }

   public void setFiltrarDepartamentos(List<Departamentos> filtrarDepartamentos) {
      this.filtrarDepartamentos = filtrarDepartamentos;
   }

   public Departamentos getNuevoDepartamentos() {
      return nuevoDepartamentos;
   }

   public void setNuevoDepartamentos(Departamentos nuevoDepartamentos) {
      this.nuevoDepartamentos = nuevoDepartamentos;
   }

   public Departamentos getDuplicarDepartamentos() {
      return duplicarDepartamentos;
   }

   public void setDuplicarDepartamentos(Departamentos duplicarDepartamentos) {
      this.duplicarDepartamentos = duplicarDepartamentos;
   }

   public Departamentos getEditarDepartamentos() {
      return editarDepartamentos;
   }

   public void setEditarDepartamentos(Departamentos editarDepartamentos) {
      this.editarDepartamentos = editarDepartamentos;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }
   private String infoRegistroPaises;

   public List<Paises> getLovPaises() {
      if (lovPaises == null) {
         lovPaises = administrarDepartamentos.consultarLOVPaises();
      }
      return lovPaises;
   }

   public void setLovPaises(List<Paises> lovPaises) {
      this.lovPaises = lovPaises;
   }

   public List<Paises> getFiltrarLovPaises() {
      return filtrarLovPaises;
   }

   public void setFiltrarLovPaises(List<Paises> filtrarLovPaises) {
      this.filtrarLovPaises = filtrarLovPaises;
   }

   public Paises getPaislovSeleccionado() {
      return paislovSeleccionado;
   }

   public void setPaislovSeleccionado(Paises paislovSeleccionado) {
      this.paislovSeleccionado = paislovSeleccionado;
   }

   public Departamentos getDepartamentoSeleccionado() {
      return departamentoSeleccionado;
   }

   public void setDepartamentoSeleccionado(Departamentos departamentoSeleccionado) {
      this.departamentoSeleccionado = departamentoSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDepartamentos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getInfoRegistroPaises() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovPaises");
      infoRegistroPaises = String.valueOf(tabla.getRowCount());
      return infoRegistroPaises;
   }

   public void setInfoRegistroPaises(String infoRegistroPaises) {
      this.infoRegistroPaises = infoRegistroPaises;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
