/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.ErroresLiquidacion;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarErroresLiquidacionesInterface;
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
public class ControlErroresLiquidacion implements Serializable {

   private static Logger log = Logger.getLogger(ControlErroresLiquidacion.class);

   @EJB
   AdministrarErroresLiquidacionesInterface administrarErroresLiquidacion;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<ErroresLiquidacion> listErroresLiquidacion;
   private List<ErroresLiquidacion> filtrarErroresLiquidacion;
   private List<ErroresLiquidacion> borrarErroresLiquidacion;
   private ErroresLiquidacion editarErroresLiquidacion;
   private ErroresLiquidacion errorLiquSeleccionado;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //RASTRO
   private Column fechaInicial, fechaFinal, empleado, tipoCentroCosto, concepto, nombreLargo, fecha, error, paquete;
   //borrado
   //filtrado table
   private int tamano, tamanoReg;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   private ErroresLiquidacion backupErroresLiq;

   private String infoRegistro;

   public ControlErroresLiquidacion() {
      listErroresLiquidacion = null;
      errorLiquSeleccionado = null;
      borrarErroresLiquidacion = new ArrayList<ErroresLiquidacion>();
      editarErroresLiquidacion = new ErroresLiquidacion();
      guardado = true;
      aceptar = true;
      tamano = 296;
      tamanoReg = 13;
      backupErroresLiq = new ErroresLiquidacion();
      log.info("controlErroresLiquidacion Constructor");
      mapParametros.put("paginaAnterior", paginaAnterior);
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

   public void limpiarListasValor() {
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }
   
   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      try {
         log.info("ControlErroresLiquidacion PostConstruct ");
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarErroresLiquidacion.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "errorliquidacion";
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

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      errorLiquSeleccionado = null;
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void cambiarIndice(ErroresLiquidacion erroresLiq, int celda) {
      errorLiquSeleccionado = erroresLiq;
      cualCelda = celda;
      backupErroresLiq = new ErroresLiquidacion();
      backupErroresLiq.setConcepto(erroresLiq.getConcepto());
      backupErroresLiq.setEmpleado(erroresLiq.getEmpleado());
      backupErroresLiq.setError(erroresLiq.getError());
      backupErroresLiq.setFecha(erroresLiq.getFecha());
      backupErroresLiq.setFechadesde(erroresLiq.getFechadesde());
      backupErroresLiq.setFechahasta(erroresLiq.getFechahasta());
      backupErroresLiq.setFormula(erroresLiq.getFormula());
      backupErroresLiq.setPaquete(erroresLiq.getPaquete());
      backupErroresLiq.setVigenciaLocalizacion(erroresLiq.getVigenciaLocalizacion());
   }

   public void modificarRegistro(ErroresLiquidacion erroresLiq, int celda) {
      errorLiquSeleccionado = erroresLiq;
      cualCelda = celda;

      if (cualCelda == 0) {
         errorLiquSeleccionado.setFechadesde(backupErroresLiq.getFechadesde());
      } else if (cualCelda == 1) {
         errorLiquSeleccionado.setFechahasta(backupErroresLiq.getFechahasta());
      } else if (cualCelda == 2) {
         errorLiquSeleccionado.setEmpleado(backupErroresLiq.getEmpleado());
      } else if (cualCelda == 3) {
         errorLiquSeleccionado.setVigenciaLocalizacion(backupErroresLiq.getVigenciaLocalizacion());
      } else if (cualCelda == 4) {
         errorLiquSeleccionado.setConcepto(backupErroresLiq.getConcepto());
      } else if (cualCelda == 5) {
         errorLiquSeleccionado.setFormula(backupErroresLiq.getFormula());
      } else if (cualCelda == 6) {
         errorLiquSeleccionado.setFecha(backupErroresLiq.getFecha());
      } else if (cualCelda == 7) {
         errorLiquSeleccionado.setError(backupErroresLiq.getError());
      } else if (cualCelda == 8) {
         errorLiquSeleccionado.setPaquete(backupErroresLiq.getPaquete());
      }
      RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
      RequestContext.getCurrentInstance().execute("PF('noModificar').show()");
   }

//
//   public void modificarLiquidacionesLogSinGuardar(int indice, String confirmarCambio, String valorConfirmar) {
//      index = indice;
//      int coincidencias = 0;
//      int indiceUnicoElemento = 0, pass = 0;
//      RequestContext context = RequestContext.getCurrentInstance();
//      if (confirmarCambio.equalsIgnoreCase("N")) {
//         log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar");
//         if (!crearErroresLiquidacion.contains(errorLiquSeleccionado)) {
//
//            if (errorLiquSeleccionado.getEmpleado().getPersona().getNombreCompleto() == null) {
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpEmpleado : " + backUpEmpleado);
//               errorLiquSeleccionado.getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
//            } else if (!errorLiquSeleccionado.getEmpleado().getPersona().getNombreCompleto().equals(backUpEmpleado) && backUpEmpleado != null) {
//               errorLiquSeleccionado.getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpEmpleado : " + backUpEmpleado);
//            }
//            if (errorLiquSeleccionado.getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().getNombre() == null) {
//               errorLiquSeleccionado.getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().setNombre(backUpTipoCentroCosto);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpTipoCentroCosto : " + backUpTipoCentroCosto);
//            } else if (!errorLiquSeleccionado.getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().getNombre().equals(backUpTipoCentroCosto) && backUpTipoCentroCosto != null) {
//               errorLiquSeleccionado.getVigenciaLocalizacion().getLocalizacion().getCentrocosto().getTipocentrocosto().setNombre(backUpTipoCentroCosto);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpTipoCentroCosto : " + backUpTipoCentroCosto);
//            }
//            if (errorLiquSeleccionado.getConcepto().getDescripcion() == null) {
//               errorLiquSeleccionado.getConcepto().setDescripcion(backUpConcepto);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpConcepto : " + backUpConcepto);
//            } else if (!errorLiquSeleccionado.getConcepto().getDescripcion().equals(backUpConcepto) && backUpConcepto != null) {
//               errorLiquSeleccionado.getConcepto().setDescripcion(backUpConcepto);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpConcepto : " + backUpConcepto);
//            }
//            if (errorLiquSeleccionado.getFormula().getNombrelargo() == null) {
//               errorLiquSeleccionado.getFormula().setNombrelargo(backUpFormula);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpValor : " + backUpFormula);
//            } else if (!errorLiquSeleccionado.getFormula().getNombrelargo().equals(backUpFormula) && backUpFormula != null) {
//               errorLiquSeleccionado.getFormula().setNombrelargo(backUpFormula);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpFormula : " + backUpFormula);
//            }
//            if (errorLiquSeleccionado.getError() == null) {
//               errorLiquSeleccionado.setError(backUpError);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpError : " + backUpError);
//            } else if (!errorLiquSeleccionado.getError().equals(backUpError) && backUpError != null) {
//               errorLiquSeleccionado.setError(backUpError);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar backUpError : " + backUpError);
//            }
//            if (errorLiquSeleccionado.getPaquete() == null) {
//               errorLiquSeleccionado.setPaquete(backUpPaquete);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar getPaquete : " + backUpPaquete);
//            } else if (!errorLiquSeleccionado.getPaquete().equals(backUpPaquete) && backUpPaquete != null) {
//               errorLiquSeleccionado.setPaquete(backUpPaquete);
//               log.error("CONTROLERRORESLIQUIDACION modificarLiquidacionesLogSinGuardar getPaquete : " + backUpPaquete);
//            }
//            
//            errorLiquSeleccionado = null;
//         }
//      }
//      RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
//   }
   public void asignarIndex(ErroresLiquidacion erroresLiq, int LND, int dig) {
      log.info("\n ENTRE A ControlErroresLiquidacion.asignarIndex \n");
      errorLiquSeleccionado = erroresLiq;
      if (LND == 0) {
         tipoActualizacion = 0;
      } else if (LND == 1) {
         tipoActualizacion = 1;
         log.info("Tipo Actualizacion: " + tipoActualizacion);
      } else if (LND == 2) {
         tipoActualizacion = 2;
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTabla();
      }
      borrarErroresLiquidacion.clear();

      errorLiquSeleccionado = null;
      k = 0;
      listErroresLiquidacion = null;
      guardado = true;
      getListErroresLiquidacion();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      context.update("form:datosErroresLiquidacion");
      context.update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         restaurarTabla();
      }
      borrarErroresLiquidacion.clear();
      errorLiquSeleccionado = null;
      k = 0;
      listErroresLiquidacion = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void borrandoErroresLiquidaciones() {
      if (errorLiquSeleccionado != null) {
         log.info("Entro a borrandoErroresLiquidaciones");
         if (!borrarErroresLiquidacion.contains(errorLiquSeleccionado)) {
            borrarErroresLiquidacion.add(errorLiquSeleccionado);
         }
         listErroresLiquidacion.remove(errorLiquSeleccionado);
         if (tipoLista == 1) {
            filtrarErroresLiquidacion.remove(errorLiquSeleccionado);
         }
         RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
         contarRegistros();
         errorLiquSeleccionado = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         FacesContext c = FacesContext.getCurrentInstance();
         tamano = 276;
         tamanoReg = 12;
         fechaInicial = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaInicial");
         fechaInicial.setFilterStyle("width: 85% !important;");
         fechaFinal = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaFinal");
         fechaFinal.setFilterStyle("width: 85% !important;");
         empleado = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:empleado");
         empleado.setFilterStyle("width: 85% !important;");
         tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:tipoCentroCosto");
         tipoCentroCosto.setFilterStyle("width: 85% !important;");
         concepto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:concepto");
         concepto.setFilterStyle("width: 85% !important;");
         nombreLargo = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:nombreLargo");
         nombreLargo.setFilterStyle("width: 85% !important;");
         fecha = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fecha");
         fecha.setFilterStyle("width: 85% !important;");
         error = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:error");
         error.setFilterStyle("width: 85% !important;");
         paquete = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:paquete");
         paquete.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void restaurarTabla() {
      FacesContext c = FacesContext.getCurrentInstance();
      tamano = 296;
      tamanoReg = 13;
      fechaInicial = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaInicial");
      fechaInicial.setFilterStyle("display: none; visibility: hidden;");
      fechaFinal = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fechaFinal");
      fechaFinal.setFilterStyle("display: none; visibility: hidden;");
      empleado = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:empleado");
      empleado.setFilterStyle("display: none; visibility: hidden;");
      tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:tipoCentroCosto");
      tipoCentroCosto.setFilterStyle("display: none; visibility: hidden;");
      concepto = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:concepto");
      concepto.setFilterStyle("display: none; visibility: hidden;");
      nombreLargo = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:nombreLargo");
      nombreLargo.setFilterStyle("display: none; visibility: hidden;");
      fecha = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:fecha");
      fecha.setFilterStyle("display: none; visibility: hidden;");
      error = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:error");
      error.setFilterStyle("display: none; visibility: hidden;");
      paquete = (Column) c.getViewRoot().findComponent("form:datosErroresLiquidacion:paquete");
      paquete.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");

      RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
      bandera = 0;
      filtrarErroresLiquidacion = null;
      tipoLista = 0;
   }

   public void editarCelda() {
      if (errorLiquSeleccionado != null) {
         editarErroresLiquidacion = errorLiquSeleccionado;
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicial");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicial').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
            RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleado");
            RequestContext.getCurrentInstance().execute("PF('editarEmpleado').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('editarTipoCentroCosto').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConcepto");
            RequestContext.getCurrentInstance().execute("PF('editarConcepto').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormula");
            RequestContext.getCurrentInstance().execute("PF('editarFormula').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaE");
            RequestContext.getCurrentInstance().execute("PF('editarFechaE').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarErrorE");
            RequestContext.getCurrentInstance().execute("PF('editarErrorE').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPaqueteE");
            RequestContext.getCurrentInstance().execute("PF('editarPaqueteE').show()");
            cualCelda = -1;
         }
      }
      errorLiquSeleccionado = null;
   }
//
//   public void limpiarNuevoErroresLiquidacion() {
//      nuevoErroresLiquidacion = new ErroresLiquidacion();
//      nuevoErroresLiquidacion.setEmpleado(new Empleados());
//      nuevoErroresLiquidacion.setConcepto(new Conceptos());
//      nuevoErroresLiquidacion.setFormula(new Formulas());
//      nuevoErroresLiquidacion.setVigenciaLocalizacion(new VigenciasLocalizaciones());
//   }

   public void revisarDialogoGuardar() {
      RequestContext.getCurrentInstance().update("form:confirmarGuardar");
      RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
   }

   public void revisarDialogoBorrarTodo() {
      if (listErroresLiquidacion != null) {
         RequestContext.getCurrentInstance().update("form:confirmarBorrarTodo");
         RequestContext.getCurrentInstance().execute("PF('confirmarBorrarTodo').show()");
      }
   }

   public void borrarTodosErroresLiquidacion() {
      int n = administrarErroresLiquidacion.borrarTodosErroresLiquidacion();
      errorLiquSeleccionado = null;
      guardado = true;
      listErroresLiquidacion = null;
      getListErroresLiquidacion();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:BORRARTODO");
      RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
      contarRegistros();
      if (n > 0) {
         FacesMessage msg = new FacesMessage("Información", "Se borraron " + n + " registros con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } else {
         FacesMessage msg = new FacesMessage("ERROR", "No se pudo borrar ningun registro.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarErroresLiquidacion() {
      if (guardado == false) {
         log.info("Realizando guardarClasesPensiones");
         if (!borrarErroresLiquidacion.isEmpty()) {
            administrarErroresLiquidacion.borrarErroresLiquidaciones(borrarErroresLiquidacion);
            borrarErroresLiquidacion.clear();
         }
         log.info("Se guardaron los datos con exito");
         listErroresLiquidacion = null;
         RequestContext.getCurrentInstance().update("form:datosErroresLiquidacion");
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      errorLiquSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void guardarYSalir() {
      guardarErroresLiquidacion();
      salir();
   }

   public void cancelarYSalir() {
      cancelarModificacion();
      salir();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosErroresLiquidacionExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ERRORESLIQUIDACION", false, false, "UTF-8", null, null);
      context.responseComplete();

      errorLiquSeleccionado = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosErroresLiquidacionExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ERRORESLIQUIDACION", false, false, "UTF-8", null, null);
      context.responseComplete();

      errorLiquSeleccionado = null;
   }

   public void verificarRastro() {
      log.info("lol");
      if (errorLiquSeleccionado != null) {
         log.info("lol 2");
         int resultado = administrarRastros.obtenerTabla(errorLiquSeleccionado.getSecuencia(), "ERRORESLIQUIDACION"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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

      } else if (administrarRastros.verificarHistoricosTabla("ERRORESLIQUIDACION")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      errorLiquSeleccionado = null;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<ErroresLiquidacion> getListErroresLiquidacion() {
      if (listErroresLiquidacion == null) {
         log.info("ControlErroresLiquidacion getListErroresLiquidacion");
         listErroresLiquidacion = administrarErroresLiquidacion.consultarErroresLiquidacion();
      }
      return listErroresLiquidacion;
   }

   public void setListErroresLiquidacion(List<ErroresLiquidacion> listErroresLiquidacion) {
      this.listErroresLiquidacion = listErroresLiquidacion;
   }

   public List<ErroresLiquidacion> getFiltrarErroresLiquidacion() {
      return filtrarErroresLiquidacion;
   }

   public void setFiltrarErroresLiquidacion(List<ErroresLiquidacion> filtrarErroresLiquidacion) {
      this.filtrarErroresLiquidacion = filtrarErroresLiquidacion;
   }

   public ErroresLiquidacion getEditarErroresLiquidacion() {
      return editarErroresLiquidacion;
   }

   public void setEditarErroresLiquidacion(ErroresLiquidacion editarErroresLiquidacion) {
      this.editarErroresLiquidacion = editarErroresLiquidacion;
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

   public int getTamanoReg() {
      return tamanoReg;
   }

   public void setTamanoReg(int tamanoReg) {
      this.tamanoReg = tamanoReg;
   }

   public ErroresLiquidacion getErroresLiquidacionSeleccionado() {
      return errorLiquSeleccionado;
   }

   public void setErroresLiquidacionSeleccionado(ErroresLiquidacion clasesPensionesSeleccionado) {
      this.errorLiquSeleccionado = clasesPensionesSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosErroresLiquidacion");
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

}
