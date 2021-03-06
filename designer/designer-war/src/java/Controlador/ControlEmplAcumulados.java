/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ClasesAyuda.LazyEmplAcumuladosDataModel;
import Entidades.Empleados;
import Entidades.VWAcumulados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplAcumuladosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlEmplAcumulados implements Serializable {

   private static Logger log = Logger.getLogger(ControlEmplAcumulados.class);

   @EJB
   AdministrarEmplAcumuladosInterface administrarEmplAcumulados;
   private BigInteger secuenciaEmpleado;
   private Empleados empleadoSeleccionado;
//valores tablas
   private List<VWAcumulados> listVWAcumuladosPorEmpleado;
   private List<VWAcumulados> filtrarVWAcumuladosPorEmpleado;
   private VWAcumulados nuevaEmplAcumulados;
   private VWAcumulados editarVWAcumuladosPorEmpleado;
   private VWAcumulados acumuladosPorEmpleadoSeleccionado;
//otros
   private int tipoLista, bandera, cualCelda;
   //revisarcambios
   private BigInteger revisarConceptoCodigo;
   //
   private Column conceptoCodigo, conceptoDescripcion, fechaDesde, fechaPago, unidades,
           valor, saldo, tipo, corteProceso, tercero, formula, debito, centroCostoDebito,
           credito, centroCostoCredito, ultimaModificacion, observaciones, motivoNovedad;
   private int tamano;
   public String altoTabla, altoTablaReg;
   public String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private LazyDataModel<VWAcumulados> model;

   public ControlEmplAcumulados() {
      secuenciaEmpleado = null;
      empleadoSeleccionado = null;
      listVWAcumuladosPorEmpleado = null;
      editarVWAcumuladosPorEmpleado = new VWAcumulados();
      altoTabla = "284";
      mapParametros.put("paginaAnterior", paginaAnterior);
      model = null;
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
      String pagActual = "emplacumulados";
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
         administrarEmplAcumulados.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void cargarModel() {
      try {
         if (secuenciaEmpleado != null) {
            model = new LazyEmplAcumuladosDataModel(administrarEmplAcumulados.consultarVWAcumuladosEmpleado(secuenciaEmpleado));
         }
      } catch (Exception e) {
         log.error("error en cargarModel() : " + e.getMessage());
      }
   }

   public void recibirEmpleado(BigInteger sec) {
      empleadoSeleccionado = null;
      secuenciaEmpleado = sec;
      cargarModel();
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      modificarInfoRegistro();
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "264";
         conceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:conceptoCodigo");
         conceptoCodigo.setFilterStyle("width: 85% !important");
         conceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:conceptoDescripcion");
         conceptoDescripcion.setFilterStyle("width: 85% !important");
         fechaDesde = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:fechaDesde");
         fechaDesde.setFilterStyle("width: 85% !important");
         fechaPago = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:fechaPago");
         fechaPago.setFilterStyle("width: 85% !important");
         unidades = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:unidades");
         unidades.setFilterStyle("width: 85% !important");
         valor = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:valor");
         valor.setFilterStyle("width: 85% !important");
         saldo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:saldo");
         saldo.setFilterStyle("width: 85% !important");
         tipo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:tipo");
         tipo.setFilterStyle("width: 85% !important");
         corteProceso = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:corteProceso");
         corteProceso.setFilterStyle("width: 85% !important");
         tercero = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:tercero");
         tercero.setFilterStyle("width: 85% !important");
         formula = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:formula");
         formula.setFilterStyle("width: 85% !important");
         debito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:debito");
         debito.setFilterStyle("width: 85% !important");
         centroCostoDebito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:centroCostoDebito");
         centroCostoDebito.setFilterStyle("width: 85% !important");
         credito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:credito");
         credito.setFilterStyle("width: 85% !important");
         centroCostoCredito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:centroCostoCredito");
         centroCostoCredito.setFilterStyle("width: 85% !important");
         ultimaModificacion = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:ultimaModificacion");
         ultimaModificacion.setFilterStyle("width: 85% !important");
         observaciones = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:observaciones");
         observaciones.setFilterStyle("width: 85% !important");
         motivoNovedad = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:motivoNovedad");
         motivoNovedad.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosEmplAcumulados");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVWAEmpleadoExportar");
      //DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:aficiones");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "EmplAcumuladosPDF", false, false, "UTF-8", null, null);
      //exporter.export(context, tabla, "AficionesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVWAEmpleadoExportar");
      //DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:aficiones");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "EmplAcumuladosXLS", false, false, "UTF-8", null, null);
      //exporter.export(context, tabla, "AficionesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void limpiarNuevaVigenciaFormpasPagos() {
      nuevaEmplAcumulados = new VWAcumulados();
   }

   public void editarCelda() {
      if (acumuladosPorEmpleadoSeleccionado != null) {
         editarVWAcumuladosPorEmpleado = acumuladosPorEmpleadoSeleccionado;

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editConceptoCodigo");
            RequestContext.getCurrentInstance().execute("PF('editConceptoCodigo').show()");
            cualCelda = -1;
         }
         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editConceptoDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editConceptoDescripcion').show()");
            cualCelda = -1;
         }
         if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaDesde");
            RequestContext.getCurrentInstance().execute("PF('editFechaDesde').show()");
            cualCelda = -1;
         }
         if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaPago");
            RequestContext.getCurrentInstance().execute("PF('editFechaPago').show()");
            cualCelda = -1;
         }
         if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editUnidad");
            RequestContext.getCurrentInstance().execute("PF('editUnidad').show()");
            cualCelda = -1;
         }
         if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editValor");
            RequestContext.getCurrentInstance().execute("PF('editValor').show()");
            cualCelda = -1;
         }
         if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editSaldo");
            RequestContext.getCurrentInstance().execute("PF('editSaldo').show()");
            cualCelda = -1;
         }
         if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editTipo");
            RequestContext.getCurrentInstance().execute("PF('editTipo').show()");
            cualCelda = -1;
         }
         if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editProceso");
            RequestContext.getCurrentInstance().execute("PF('editProceso').show()");
            cualCelda = -1;
         }
         if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editNitNombre");
            RequestContext.getCurrentInstance().execute("PF('editNitNombre').show()");
            cualCelda = -1;
         }
         if (cualCelda == 10) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFormula");
            RequestContext.getCurrentInstance().execute("PF('editFormula').show()");
            cualCelda = -1;
         }
         if (cualCelda == 11) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDebito");
            RequestContext.getCurrentInstance().execute("PF('editDebito').show()");
            cualCelda = -1;
         }
         if (cualCelda == 12) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCentroCostoDebito");
            RequestContext.getCurrentInstance().execute("PF('editCentroCostoDebito').show()");
            cualCelda = -1;
         }
         if (cualCelda == 13) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCredito");
            RequestContext.getCurrentInstance().execute("PF('editCredito').show()");
            cualCelda = -1;
         }
         if (cualCelda == 13) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCredito");
            RequestContext.getCurrentInstance().execute("PF('editCredito').show()");
            cualCelda = -1;
         }
         if (cualCelda == 14) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCentroCostoCredito");
            RequestContext.getCurrentInstance().execute("PF('editCentroCostoCredito').show()");
            cualCelda = -1;
         }
         if (cualCelda == 15) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaModificacion");
            RequestContext.getCurrentInstance().execute("PF('editFechaModificacion').show()");
            cualCelda = -1;
         }
         if (cualCelda == 16) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editObservaciones");
            RequestContext.getCurrentInstance().execute("PF('editObservaciones').show()");
            cualCelda = -1;
         }
         if (cualCelda == 17) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editMotivoNovedad");
            RequestContext.getCurrentInstance().execute("PF('editMotivoNovedad').show()");
            cualCelda = -1;
         }
         if (cualCelda == 18) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDetallesConcepto");
            RequestContext.getCurrentInstance().execute("PF('editDetallesConcepto').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void cambiarIndice(VWAcumulados aux, int celda) {

      acumuladosPorEmpleadoSeleccionado = aux;
      cualCelda = celda;
      acumuladosPorEmpleadoSeleccionado.getSecuencia();
      if (cualCelda == 0) {
         acumuladosPorEmpleadoSeleccionado.getConcepto_Codigo();
      } else if (cualCelda == 1) {
         acumuladosPorEmpleadoSeleccionado.getConcepto_Descripcion();
      } else if (cualCelda == 2) {
         acumuladosPorEmpleadoSeleccionado.getFechaDesde();
      } else if (cualCelda == 3) {
         acumuladosPorEmpleadoSeleccionado.getFechaPago();
      } else if (cualCelda == 4) {
         acumuladosPorEmpleadoSeleccionado.getUnidades();
      } else if (cualCelda == 5) {
         acumuladosPorEmpleadoSeleccionado.getValor();
      } else if (cualCelda == 6) {
         acumuladosPorEmpleadoSeleccionado.getSaldo();
      } else if (cualCelda == 7) {
         acumuladosPorEmpleadoSeleccionado.getTipo();
      } else if (cualCelda == 8) {
         acumuladosPorEmpleadoSeleccionado.getCorteProceso().getProceso().getDescripcion();
      } else if (cualCelda == 9) {
         acumuladosPorEmpleadoSeleccionado.getNit_nombre();
      } else if (cualCelda == 10) {
         acumuladosPorEmpleadoSeleccionado.getFormula().getNombrelargo();
      } else if (cualCelda == 11) {
         acumuladosPorEmpleadoSeleccionado.getCuentaD().getCodigo();
      } else if (cualCelda == 12) {
         acumuladosPorEmpleadoSeleccionado.getCentroCostoD().getNombre();
      } else if (cualCelda == 13) {
         acumuladosPorEmpleadoSeleccionado.getCuentaC().getCodigo();
      } else if (cualCelda == 14) {
         acumuladosPorEmpleadoSeleccionado.getCentroCostoC().getNombre();
      } else if (cualCelda == 15) {
         acumuladosPorEmpleadoSeleccionado.getUltimaModificacion();
      } else if (cualCelda == 16) {
         acumuladosPorEmpleadoSeleccionado.getObservacionesMovedad();
      } else if (cualCelda == 17) {
         acumuladosPorEmpleadoSeleccionado.getMotivoNovedad();
      }

   }

   public void revisarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('repeticiones').show()");
      listVWAcumuladosPorEmpleado = null;
      context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosEmplAcumulados");
   }
//------------------------------------------------------------------------------

   public void salir() {
      cancelarModificacion();
      navegar("atras");
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         cerrarFiltrado();
      }
      listVWAcumuladosPorEmpleado = null;
      acumuladosPorEmpleadoSeleccionado = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosEmplAcumulados");
   }

   public void cerrarFiltrado() {
      FacesContext c = FacesContext.getCurrentInstance();
      conceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:conceptoCodigo");
      conceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
      conceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:conceptoDescripcion");
      conceptoDescripcion.setFilterStyle("display: none; visibility: hidden;");
      fechaDesde = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:fechaDesde");
      fechaDesde.setFilterStyle("display: none; visibility: hidden;");
      fechaPago = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:fechaPago");
      fechaPago.setFilterStyle("display: none; visibility: hidden;");
      unidades = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:unidades");
      unidades.setFilterStyle("display: none; visibility: hidden;");
      valor = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:valor");
      valor.setFilterStyle("display: none; visibility: hidden;");
      saldo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:saldo");
      saldo.setFilterStyle("display: none; visibility: hidden;");
      tipo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:tipo");
      tipo.setFilterStyle("display: none; visibility: hidden;");
      corteProceso = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:corteProceso");
      corteProceso.setFilterStyle("display: none; visibility: hidden;");
      tercero = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:tercero");
      tercero.setFilterStyle("display: none; visibility: hidden;");
      formula = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:formula");
      formula.setFilterStyle("display: none; visibility: hidden;");
      debito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:debito");
      debito.setFilterStyle("display: none; visibility: hidden;");
      centroCostoDebito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:centroCostoDebito");
      centroCostoDebito.setFilterStyle("display: none; visibility: hidden;");
      credito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:credito");
      credito.setFilterStyle("display: none; visibility: hidden;");
      centroCostoCredito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:centroCostoCredito");
      centroCostoCredito.setFilterStyle("display: none; visibility: hidden;");
      ultimaModificacion = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:ultimaModificacion");
      ultimaModificacion.setFilterStyle("display: none; visibility: hidden;");
      observaciones = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:observaciones");
      observaciones.setFilterStyle("display: none; visibility: hidden;");
      motivoNovedad = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:motivoNovedad");
      motivoNovedad.setFilterStyle("display: none; visibility: hidden;");
      altoTabla = "284";
      RequestContext.getCurrentInstance().update("form:datosEmplAcumulados");
      bandera = 0;
      filtrarVWAcumuladosPorEmpleado = null;
      tipoLista = 0;
   }

   private void modificarInfoRegistro() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   //-----------------------------------------------------------------------------
   public Empleados getEmpleadoSeleccionado() {
      if (empleadoSeleccionado == null) {
         empleadoSeleccionado = administrarEmplAcumulados.consultarEmpleado(secuenciaEmpleado);
      }
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<VWAcumulados> getListVWAcumuladosPorEmpleado() {
//        if (listVWAcumuladosPorEmpleado == null) {
//            listVWAcumuladosPorEmpleado = administrarEmplAcumulados.consultarVWAcumuladosEmpleado(secuenciaEmpleado);
//        }
      return listVWAcumuladosPorEmpleado;

   }

   public void setListVWAcumuladosPorEmpleado(List<VWAcumulados> listVWAcumuladosPorEmpleado) {
      this.listVWAcumuladosPorEmpleado = listVWAcumuladosPorEmpleado;
   }

   public List<VWAcumulados> getFiltrarVWAcumuladosPorEmpleado() {
      return filtrarVWAcumuladosPorEmpleado;
   }

   public void setFiltrarVWAcumuladosPorEmpleado(List<VWAcumulados> filtrarVWAcumuladosPorEmpleado) {
      this.filtrarVWAcumuladosPorEmpleado = filtrarVWAcumuladosPorEmpleado;
   }

   public VWAcumulados getEditarVWAcumuladosPorEmpleado() {
      return editarVWAcumuladosPorEmpleado;
   }

   public void setEditarVWAcumuladosPorEmpleado(VWAcumulados editarVWAcumuladosPorEmpleado) {
      this.editarVWAcumuladosPorEmpleado = editarVWAcumuladosPorEmpleado;
   }

   public VWAcumulados getAcumuladosPorEmpleadoSeleccionado() {
      return acumuladosPorEmpleadoSeleccionado;
   }

   public void setAcumuladosPorEmpleadoSeleccionado(VWAcumulados acumuladosPorEmpleadoSeleccionado) {
      this.acumuladosPorEmpleadoSeleccionado = acumuladosPorEmpleadoSeleccionado;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getAltoTablaReg() {
      if ("264".equals(altoTabla)) {
         altoTablaReg = "11";
      } else {
         altoTablaReg = "12";
      }
      return altoTablaReg;
   }

   public void setAltoTablaReg(String altoTablaReg) {
      this.altoTablaReg = altoTablaReg;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmplAcumulados");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public LazyDataModel<VWAcumulados> getModel() {
      return model;
   }

   public void setModel(LazyDataModel<VWAcumulados> model) {
      this.model = model;
   }

}
