package Controlador;

import Entidades.Empleados;
import Entidades.Novedades;
import Entidades.SolucionesFormulas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSolucionesFormulasInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.ArrayList;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlSolucionFormula implements Serializable {

   private static Logger log = Logger.getLogger(ControlSolucionFormula.class);

   @EJB
   AdministrarSolucionesFormulasInterface administrarSolucionesFormulas;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<SolucionesFormulas> listaSolucionesFormulas;
   private List<SolucionesFormulas> filtrarListaSolucionesFormulas;
   private List<SolucionesFormulas> listaSolucionesFormulasBorrar;
   private SolucionesFormulas solucionTablaSeleccionada;
   private Empleados empleado;
   private Novedades novedad;
   private int bandera;
   private Column fechaHasta, concepto, valor, saldo, fechaPago, proceso, formula;
   private boolean aceptar;
   private SolucionesFormulas editarSolucionFormula;
   private int cualCelda, tipoLista;
   private BigInteger backUpSecRegistro;
   private String informacionEmpleadoNovedad;
   private String algoTabla;
   private boolean guardado;
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlSolucionFormula() {
      algoTabla = "300";
      empleado = new Empleados();
      novedad = new Novedades();
      backUpSecRegistro = null;
      listaSolucionesFormulas = null;
      listaSolucionesFormulasBorrar = new ArrayList<SolucionesFormulas>();
      aceptar = true;
      editarSolucionFormula = new SolucionesFormulas();
      cualCelda = -1;
      tipoLista = 0;
      guardado = true;
      solucionTablaSeleccionada = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
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
         administrarSolucionesFormulas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
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
      String pagActual = "solucionformula";
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

   public void recibirParametros(BigInteger codEmpleado, BigInteger secNovedad, String pagina) {
      paginaAnterior = pagina;
      empleado = administrarSolucionesFormulas.empleadoActual(codEmpleado);
      listaSolucionesFormulas = administrarSolucionesFormulas.listaSolucionesFormulaParaEmpleadoYNovedad(empleado.getSecuencia(), secNovedad);
      novedad = administrarSolucionesFormulas.novedadActual(secNovedad);
      informacionEmpleadoNovedad = empleado.getNombreCompleto().toUpperCase() + " NOVEDAD POR VALOR DE : $ " + novedad.getValortotal().toString();
   }

   public void posicionTabla() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String type = map.get("t"); // type attribute of node
      String cass = map.get("n"); // type attribute of node
      int ind = Integer.parseInt(type);
      int cassi = Integer.parseInt(cass);
      solucionTablaSeleccionada = listaSolucionesFormulas.get(ind);
      cambiarIndice(solucionTablaSeleccionada, cassi);
   }

   public void cambiarIndice(SolucionesFormulas solucionf, int celda) {
      solucionTablaSeleccionada = solucionf;
      cualCelda = celda;
      solucionTablaSeleccionada.getSecuencia();
      if (cualCelda == 0) {
         solucionTablaSeleccionada.getNovedad().getFechafinal();
      } else if (cualCelda == 1) {
         solucionTablaSeleccionada.getNovedad().getConcepto().getDescripcion();
      } else if (cualCelda == 2) {
         solucionTablaSeleccionada.getNovedad().getValortotal();
      } else if (cualCelda == 3) {
         solucionTablaSeleccionada.getNovedad().getSaldo();
      } else if (cualCelda == 4) {
         solucionTablaSeleccionada.getNovedad().getFechareporte();
      } else if (cualCelda == 5) {
         solucionTablaSeleccionada.getSolucionnodo().getNombreproceso();
      } else if (cualCelda == 6) {
         solucionTablaSeleccionada.getSolucionnodo().getNombreformula();
      }
   }

   public void editarCelda() {
      if (solucionTablaSeleccionada != null) {
         editarSolucionFormula = solucionTablaSeleccionada;
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHastaD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaHastaD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoD");
            RequestContext.getCurrentInstance().execute("PF('editarConceptoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValorD");
            RequestContext.getCurrentInstance().execute("PF('editarValorD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldoD");
            RequestContext.getCurrentInstance().execute("PF('editarSaldoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaPagoD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaPagoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoD");
            RequestContext.getCurrentInstance().execute("PF('editarProcesoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaD");
            RequestContext.getCurrentInstance().execute("PF('editarFormulaD').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         algoTabla = "280";
         fechaHasta = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaHasta");
         fechaHasta.setFilterStyle("width: 85% !important;");
         concepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:concepto");
         concepto.setFilterStyle("width: 85% !important;");
         fechaPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaPago");
         fechaPago.setFilterStyle("width: 85% !important;");
         valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:valor");
         valor.setFilterStyle("width: 85% !important;");
         saldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:saldo");
         saldo.setFilterStyle("width: 85% !important;");
         proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:proceso");
         proceso.setFilterStyle("width: 85% !important;");
         formula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:formula");
         formula.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosSolucionFormula");
         bandera = 1;
      } else if (bandera == 1) {
         algoTabla = "300";
         fechaHasta = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaHasta");
         fechaHasta.setFilterStyle("display: none; visibility: hidden;");
         concepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:concepto");
         concepto.setFilterStyle("display: none; visibility: hidden;");
         fechaPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaPago");
         fechaPago.setFilterStyle("display: none; visibility: hidden;");
         valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:valor");
         valor.setFilterStyle("display: none; visibility: hidden;");
         saldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:saldo");
         saldo.setFilterStyle("display: none; visibility: hidden;");
         proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:proceso");
         proceso.setFilterStyle("display: none; visibility: hidden;");
         formula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:formula");
         formula.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosSolucionFormula");
         bandera = 0;
         filtrarListaSolucionesFormulas = null;
         tipoLista = 0;
      }
      solucionTablaSeleccionada = null;
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         algoTabla = "300";
         fechaHasta = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaHasta");
         fechaHasta.setFilterStyle("display: none; visibility: hidden;");
         concepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:concepto");
         concepto.setFilterStyle("display: none; visibility: hidden;");
         fechaPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaPago");
         fechaPago.setFilterStyle("display: none; visibility: hidden;");
         valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:valor");
         valor.setFilterStyle("display: none; visibility: hidden;");
         saldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:saldo");
         saldo.setFilterStyle("display: none; visibility: hidden;");
         proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:proceso");
         proceso.setFilterStyle("display: none; visibility: hidden;");
         formula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:formula");
         formula.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosSolucionFormula");
         bandera = 0;
         filtrarListaSolucionesFormulas = null;
         tipoLista = 0;
      }
      solucionTablaSeleccionada = null;
      listaSolucionesFormulas = null;
      navegar("atras");
   }

   public void borrarSolucionFormula() {

      if (solucionTablaSeleccionada != null) {
         listaSolucionesFormulasBorrar.add(solucionTablaSeleccionada);
         listaSolucionesFormulas.remove(solucionTablaSeleccionada);
         if (tipoLista == 1) {
            filtrarListaSolucionesFormulas.remove(solucionTablaSeleccionada);
         }
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosSolucionFormula");
         solucionTablaSeleccionada = null;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void guardarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listaSolucionesFormulasBorrar.isEmpty()) {
               administrarSolucionesFormulas.borrarSolucionesFormulas(listaSolucionesFormulasBorrar);
               listaSolucionesFormulasBorrar.clear();
            }
            listaSolucionesFormulas = null;
            getListaSolucionesFormulas();
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            solucionTablaSeleccionada = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosSolucionFormula");
      } catch (Exception e) {
         log.warn("Error guardarCambios : " + e.getMessage());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarSalir() {
      guardarCambios();
      salir();
   }

   public void cancelarSalir() {
      refrescar();
      salir();
   }

   public void refrescar() {
      if (bandera == 1) {
         algoTabla = "300";
         fechaHasta = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaHasta");
         fechaHasta.setFilterStyle("display: none; visibility: hidden;");
         concepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:concepto");
         concepto.setFilterStyle("display: none; visibility: hidden;");
         fechaPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaPago");
         fechaPago.setFilterStyle("display: none; visibility: hidden;");
         valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:valor");
         valor.setFilterStyle("display: none; visibility: hidden;");
         saldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:saldo");
         saldo.setFilterStyle("display: none; visibility: hidden;");
         proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:proceso");
         proceso.setFilterStyle("display: none; visibility: hidden;");
         formula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:formula");
         formula.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosSolucionFormula");
         bandera = 0;
         filtrarListaSolucionesFormulas = null;
         tipoLista = 0;
      }
      listaSolucionesFormulas = null;
      solucionTablaSeleccionada = null;
      guardado = true;
      getListaSolucionesFormulas();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosSolucionFormula");
   }

   /**
    * Metodo que exporta datos a PDF
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSolucionFormulaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "SolucionesFormulas_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSolucionFormulaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "SolucionesFormulas_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }
   //EVENTO FILTRAR

   /**
    * Evento que cambia la lista reala a la filtrada
    */
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }
   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (solucionTablaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(solucionTablaSeleccionada.getSecuencia(), "SOLUCIONESFORMULAS");
         backUpSecRegistro = solucionTablaSeleccionada.getSecuencia();
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
      } else if (administrarRastros.verificarHistoricosTabla("SOLUCIONESFORMULAS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public List<SolucionesFormulas> getListaSolucionesFormulas() {
      if (listaSolucionesFormulas == null) {
         listaSolucionesFormulas = administrarSolucionesFormulas.listaSolucionesFormulaParaEmpleadoYNovedad(empleado.getSecuencia(), novedad.getSecuencia());
      }
      return listaSolucionesFormulas;
   }

   public void setListaSolucionesFormulas(List<SolucionesFormulas> setListaSolucionesFormulas) {
      this.listaSolucionesFormulas = setListaSolucionesFormulas;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public List<SolucionesFormulas> getFiltrarListaSolucionesFormulas() {
      return filtrarListaSolucionesFormulas;
   }

   public void setFiltrarListaSolucionesFormulas(List<SolucionesFormulas> setFiltrarListaSolucionesFormulas) {
      this.filtrarListaSolucionesFormulas = setFiltrarListaSolucionesFormulas;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public SolucionesFormulas getEditarSolucionFormula() {
      return editarSolucionFormula;
   }

   public void setEditarSolucionFormula(SolucionesFormulas setEditarSolucionFormula) {
      this.editarSolucionFormula = setEditarSolucionFormula;
   }

   public BigInteger getBackUpSecRegistro() {
      return backUpSecRegistro;
   }

   public void setBackUpSecRegistro(BigInteger BackUpSecRegistro) {
      this.backUpSecRegistro = BackUpSecRegistro;
   }

   public Novedades getNovedad() {
      return novedad;
   }

   public void setNovedad(Novedades novedad) {
      this.novedad = novedad;
   }

   public String getInformacionEmpleadoNovedad() {
      return informacionEmpleadoNovedad;
   }

   public void setInformacionEmpleadoNovedad(String informacionEmpleadoNovedad) {
      this.informacionEmpleadoNovedad = informacionEmpleadoNovedad;
   }

   public String getAlgoTabla() {
      return algoTabla;
   }

   public void setAlgoTabla(String algoTabla) {
      this.algoTabla = algoTabla;
   }

   public SolucionesFormulas getSolucionTablaSeleccionada() {
      getListaSolucionesFormulas();
      if (listaSolucionesFormulas != null) {
         int tam = listaSolucionesFormulas.size();
         if (tam > 0) {
            solucionTablaSeleccionada = listaSolucionesFormulas.get(0);
         }
      }
      return solucionTablaSeleccionada;
   }

   public void setSolucionTablaSeleccionada(SolucionesFormulas solucionTablaSeleccionada) {
      this.solucionTablaSeleccionada = solucionTablaSeleccionada;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSolucionFormula");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }
}
