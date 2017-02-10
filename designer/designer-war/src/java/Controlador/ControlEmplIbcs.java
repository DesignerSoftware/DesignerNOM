/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.Ibcs;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarIBCSInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
public class ControlEmplIbcs implements Serializable {

   @EJB
   AdministrarIBCSInterface administrarIBCS;
   private List<Ibcs> listIbcsPorEmpleado;
   private List<Ibcs> filtrarIbcsPorEmpleado;
   private Ibcs editarIbcsPorEmpleado;
   private Ibcs nuevaIbcsPorEmpleado;
   private Ibcs ibcSeleccionado;
   private Ibcs dialogoIbcsPorEmpleado;
   private BigInteger secuenciaEmpleado;
   private Empleados empleadoSeleccionado;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   private Column fechaFinal, fechaInicial, valor;
   private String altoTabla;
   public String infoRegistro;
   private DataTable tablaC;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
//*****************************************************************
   //prueba
   private Ibcs selecionIbcs;

   public Ibcs getSelecionIbcs() {
      return selecionIbcs;
   }

   public void setSelecionIbcs(Ibcs selecionIbcs) {
      this.selecionIbcs = selecionIbcs;
   }

//*****************************************************************
   public ControlEmplIbcs() {
      listIbcsPorEmpleado = null;
      //secuenciaEmpleado = BigInteger.valueOf(10664356);
      editarIbcsPorEmpleado = new Ibcs();
      empleadoSeleccionado = null;
      selecionIbcs = new Ibcs();
      altoTabla = "288";
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

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "emplibc";
         //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParametros.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
         controlListaNavegacion.adicionarPagina(pagActual);
      }
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarIBCS.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger sec) {
      empleadoSeleccionado = null;
      secuenciaEmpleado = sec;
      listIbcsPorEmpleado = null;
      getListIbcsPorEmpleado();
      //INICIALIZAR BOTONES NAVEGACION
      if (listIbcsPorEmpleado != null && !listIbcsPorEmpleado.isEmpty()) {
         if (listIbcsPorEmpleado.size() == 1) {
            //INFORMACION REGISTRO
            ibcSeleccionado = listIbcsPorEmpleado.get(0);
         } else if (listIbcsPorEmpleado.size() > 1) {
            //INFORMACION REGISTRO
            ibcSeleccionado = listIbcsPorEmpleado.get(0);
         }
      }
   }

   public void cancelarModificacion() {
      cerrarFiltrado();
      ibcSeleccionado = null;
      listIbcsPorEmpleado = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosIbcs");
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
   }

   public void revisarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('repeticiones').show()");
      listIbcsPorEmpleado = null;
      context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosEmplAcumulados");
   }

   public void posicionIBCS() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      //int indice = Integer.parseInt(type);
      Ibcs ibc = new Ibcs();
      int columna = Integer.parseInt(name);
      cambiarIndice(ibc, columna);
   }

   public void cambiarIndice(Ibcs ibc, int celda) {
      ibcSeleccionado = ibc;
      cualCelda = celda;

   }

   public void limpiarNuevaIbcs() {
      nuevaIbcsPorEmpleado = new Ibcs();
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         FacesContext c = FacesContext.getCurrentInstance();
         fechaInicial = (Column) c.getViewRoot().findComponent("form:datosIbcs:fechaInicial");
         fechaInicial.setFilterStyle("width: 85% !important");
         fechaFinal = (Column) c.getViewRoot().findComponent("form:datosIbcs:fechaFinal");
         fechaFinal.setFilterStyle("width: 85% !important");
         valor = (Column) c.getViewRoot().findComponent("form:datosIbcs:valor");
         valor.setFilterStyle("width: 85% !important");
         altoTabla = "268";
         RequestContext.getCurrentInstance().update("form:datosIbcs");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
      }
   }

   private void cerrarFiltrado() {
      FacesContext c = FacesContext.getCurrentInstance();
      fechaInicial = (Column) c.getViewRoot().findComponent("form:datosIbcs:fechaInicial");
      fechaInicial.setFilterStyle("display: none; visibility: hidden;");
      fechaFinal = (Column) c.getViewRoot().findComponent("form:datosIbcs:fechaFinal");
      fechaFinal.setFilterStyle("display: none; visibility: hidden;");
      valor = (Column) c.getViewRoot().findComponent("form:datosIbcs:valor");
      valor.setFilterStyle("display: none; visibility: hidden;");
      altoTabla = "288";
      RequestContext.getCurrentInstance().update("form:datosIbcs");
      bandera = 0;
      filtrarIbcsPorEmpleado = null;
      tipoLista = 0;
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (ibcSeleccionado != null) {
         editarIbcsPorEmpleado = ibcSeleccionado;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaInicial");
            RequestContext.getCurrentInstance().execute("PF('editFechaInicial').show()");
            cualCelda = -1;
         }
         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaFinal");
            RequestContext.getCurrentInstance().execute("PF('editFechaFinal').show()");
            cualCelda = -1;
         }
         if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editValor");
            RequestContext.getCurrentInstance().execute("PF('editValor').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void dialogo() {

      if (ibcSeleccionado != null) {
         dialogoIbcsPorEmpleado = ibcSeleccionado;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:dialogoIbcs");
         RequestContext.getCurrentInstance().execute("PF('dialogoIbcs').show()");

      }
      ibcSeleccionado = null;
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIbcsExportar");
      //DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:aficiones");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "EmplIbcsPDF", false, false, "UTF-8", null, null);
      //exporter.export(context, tabla, "AficionesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIbcsExportar");
      //DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:aficiones");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "EmplIbcsXLS", false, false, "UTF-8", null, null);
      //exporter.export(context, tabla, "AficionesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   private void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void recordarSeleccion() {
      if (ibcSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosIbcs");
         tablaC.setSelection(ibcSeleccionado);
      } else {
         ibcSeleccionado = null;
      }
      System.out.println("ibcSeleccionado: " + ibcSeleccionado);
   }

   //datosIbcsExportar
   //--------------------------------------------------------------------------
   public List<Ibcs> getListIbcsPorEmpleado() {
      if (listIbcsPorEmpleado == null) {
         listIbcsPorEmpleado = administrarIBCS.ibcsPorEmplelado(secuenciaEmpleado);
      }
      return listIbcsPorEmpleado;
   }

   public void setListIbcsPorEmpleado(List<Ibcs> listIbcsPorEmpleado) {
      this.listIbcsPorEmpleado = listIbcsPorEmpleado;
   }

   public Ibcs getEditarIbcsPorEmpleado() {
      return editarIbcsPorEmpleado;
   }

   public void setEditarIbcsPorEmpleado(Ibcs editarIbcsPorEmpleado) {
      this.editarIbcsPorEmpleado = editarIbcsPorEmpleado;
   }

   public Empleados getEmpleadoSeleccionado() {
      if (empleadoSeleccionado == null) {
         empleadoSeleccionado = administrarIBCS.buscarEmpleado(secuenciaEmpleado);
      }
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<Ibcs> getFiltrarIbcsPorEmpleado() {
      return filtrarIbcsPorEmpleado;
   }

   public void setFiltrarIbcsPorEmpleado(List<Ibcs> filtrarIbcsPorEmpleado) {
      this.filtrarIbcsPorEmpleado = filtrarIbcsPorEmpleado;
   }

   public Ibcs getDialogoIbcsPorEmpleado() {
      return dialogoIbcsPorEmpleado;
   }

   public void setDialogoIbcsPorEmpleado(Ibcs dialogoIbcsPorEmpleado) {
      this.dialogoIbcsPorEmpleado = dialogoIbcsPorEmpleado;
   }

   public Ibcs getIbcSeleccionado() {
      return ibcSeleccionado;
   }

   public void setIbcSeleccionado(Ibcs ibcSeleccionado) {
      this.ibcSeleccionado = ibcSeleccionado;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosIbcs");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }
}
