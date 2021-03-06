package Controlador;

import Entidades.CentrosCostos;
import Entidades.Conceptos;
import Entidades.Cuentas;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import Entidades.GruposConceptos;
import Entidades.Procesos;
import Entidades.ReformasLaborales;
import Entidades.TiposCentrosCostos;
import Entidades.TiposContratos;
import Entidades.TiposTrabajadores;
import Entidades.VigenciasConceptosRL;
import Entidades.VigenciasConceptosTC;
import Entidades.VigenciasConceptosTT;
import Entidades.VigenciasCuentas;
import Entidades.VigenciasGruposConceptos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDetalleConceptoInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
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
public class ControlDetalleConcepto implements Serializable {

   private static Logger log = Logger.getLogger(ControlDetalleConcepto.class);

   @EJB
   AdministrarDetalleConceptoInterface administrarDetalleConcepto;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //////////////Conceptos////////////////
   private Conceptos conceptoActual;
   ///////////VigenciasCuentas////////////
   private List<VigenciasCuentas> listVigenciasCuentasConcepto;
   private List<VigenciasCuentas> filtrarListVigenciasCuentasConcepto;
   private VigenciasCuentas vigenciaCuentaSeleccionada, vigCuentaCrearSeleccionada;
   private String altoTablaVigenciaCuenta;
   ///////////////////VigenciasGruposConceptos///////////////////////
   private List<VigenciasGruposConceptos> listVigenciasGruposConceptos;
   private List<VigenciasGruposConceptos> filtrarListVigenciasGruposConceptos;
   private VigenciasGruposConceptos vigenciaGrupoCoSeleccionada;
   private String altoTablaVigenciaGrupoC;
   ///////////////////VigenciasConceptosTT////////////////////////////
   private List<VigenciasConceptosTT> listVigenciasConceptosTTConcepto;
   private List<VigenciasConceptosTT> filtrarListVigenciasConceptosTT;
   private VigenciasConceptosTT vigenciaConceptoTTSeleccionada;
   private String altoTablaVigenciaConceptoTT;
   ///////////////////VigenciasConceptosTC///////////////////////////////
   private List<VigenciasConceptosTC> listVigenciasConceptosTCConcepto;
   private List<VigenciasConceptosTC> filtrarListVigenciasConceptosTC;
   private VigenciasConceptosTC vigenciaConceptoTCSeleccionada;
   private String altoTablaVigenciaConceptoTC;
   ///////////////////VigenciasConceptosRL///////////////////////////////
   private List<VigenciasConceptosRL> listVigenciasConceptosRLConcepto;
   private List<VigenciasConceptosRL> filtrarListVigenciasConceptosRL;
   private VigenciasConceptosRL vigenciaConceptoRLSeleccionada;
   private String altoTablaVigenciaConceptoRL;
   ///////////////////FormulasConceptos///////////////////////////////
   private List<FormulasConceptos> listFormulasConceptos;
   private List<FormulasConceptos> filtrarListFormulasConceptosConcepto;
   private FormulasConceptos vigFormulaConceptoSeleccionada;
   private String altoTablaFormulaConcepto;
   ///////////Conteo de registros para las tablas principales
   private String infoRegistroCuenta, infoRegistroGrupoC, infoRegistroConceptoTT, infoRegistroConceptoTC, infoRegistroConceptoRL, infoRegistroFormulaConcepto;
   ///////////VigenciasCuentas////////////
   private int banderaVigenciaCuenta;
   private List<VigenciasCuentas> listVigenciasCuentasModificar;
//   private VigenciasCuentas nuevaVigenciaCuenta;
   private List<VigenciasCuentas> listVigenciasCuentasCrear;
   private List<VigenciasCuentas> listVigCParaCrear;
   private List<VigenciasCuentas> listVigenciasCuentasBorrar;
   private VigenciasCuentas editarVigenciaCuenta;
   private int cualCeldaVigenciaCuenta, tipoListaVigenciaCuenta;
//   private VigenciasCuentas duplicarVigenciaCuenta;
   private boolean cambiosVigenciaCuenta;
   private String auxVC_TipoCC, auxVC_Debito, auxVC_DescDeb, auxVC_Credito, auxVC_DescCre, auxVC_ConsDeb, auxVC_ConsCre, auxVC_Proceso;
   private Date auxVC_FechaIni, auxVC_FechaFin;
   private Column vigenciaCuentaFechaInicial, vigenciaCuentaFechaFinal, vigenciaCuentaTipoCC, vigenciaCuentaDebitoCod, vigenciaCuentaDebitoDes,
           vigenciaCuentaCCConsolidadorD, vigenciaCuentaCreditoCod, vigenciaCuentaCreditoDes, vigenciaCuentaCCProceso, vigenciaCuentaCCConsolidadorC;
   private boolean permitirIndexVigenciaCuenta;
   ///////////////////VigenciasGruposConceptos///////////////////////
   private int banderaVigenciaGrupoConcepto;
   private List<VigenciasGruposConceptos> listVigenciasGruposConceptosModificar;
   private VigenciasGruposConceptos nuevaVigenciaGrupoConcepto;
   private List<VigenciasGruposConceptos> listVigenciasGruposConceptosCrear;
   private List<VigenciasGruposConceptos> listVigenciasGruposConceptosBorrar;
   private VigenciasGruposConceptos editarVigenciaGrupoConcepto;
   private int cualCeldaVigenciaGrupoConcepto, tipoListaVigenciaGrupoConcepto;
   private VigenciasGruposConceptos duplicarVigenciaGrupoConcepto;
   private boolean cambiosVigenciaGrupoConcepto;
   private String auxVGC_Descripcion, auxVGC_Codigo;
   private Date auxVGC_FechaIni, auxVGC_FechaFin;
   private Column vigenciaGCFechaInicial, vigenciaGCFechaFinal, vigenciaGCCodigo, vigenciaGCDescripcion;
   private boolean permitirIndexVigenciaGrupoConcepto;
   ///////////////////VigenciasConceptosTT///////////////////////
   private int banderaVigenciaConceptoTT;
   private List<VigenciasConceptosTT> listVigenciasConceptosTTModificar;
   private VigenciasConceptosTT nuevaVigenciaConceptoTT;
   private List<VigenciasConceptosTT> listVigenciasConceptosTTCrear;
   private List<VigenciasConceptosTT> listVigenciasConceptosTTBorrar;
   private VigenciasConceptosTT editarVigenciaConceptoTT;
   private int cualCeldaVigenciaConceptoTT, tipoListaVigenciaConceptoTT;
   private VigenciasConceptosTT duplicarVigenciaConceptoTT;
   private boolean cambiosVigenciaConceptoTT;
   private String auxVCTT_Descripcion;
   private Date auxVCTT_FechaIni, auxVCTT_FechaFin;
   private Column vigenciaCTTFechaInicial, vigenciaCTTFechaFinal, vigenciaCTTDescripcion;
   private boolean permitirIndexVigenciaConceptoTT;
   ///////////////////VigenciasConceptosTC///////////////////////
   private int banderaVigenciaConceptoTC;
   private List<VigenciasConceptosTC> listVigenciasConceptosTCModificar;
   private VigenciasConceptosTC nuevaVigenciaConceptoTC;
   private List<VigenciasConceptosTC> listVigenciasConceptosTCCrear;
   private List<VigenciasConceptosTC> listVigenciasConceptosTCBorrar;
   private VigenciasConceptosTC editarVigenciaConceptoTC;
   private int cualCeldaVigenciaConceptoTC, tipoListaVigenciaConceptoTC;
   private VigenciasConceptosTC duplicarVigenciaConceptoTC;
   private boolean cambiosVigenciaConceptoTC;
   private String auxVCTC_Descripcion;
   private Date auxVCTC_FechaIni, auxVCTC_FechaFin;
   private Column vigenciaCTCFechaInicial, vigenciaCTCFechaFinal, vigenciaCTCDescripcion;
   private boolean permitirIndexVigenciaConceptoTC;
   ///////////////////VigenciasConceptosRL///////////////////////
   private int banderaVigenciaConceptoRL;
   private List<VigenciasConceptosRL> listVigenciasConceptosRLModificar;
   private VigenciasConceptosRL nuevaVigenciaConceptoRL;
   private List<VigenciasConceptosRL> listVigenciasConceptosRLCrear;
   private List<VigenciasConceptosRL> listVigenciasConceptosRLBorrar;
   private VigenciasConceptosRL editarVigenciaConceptoRL;
   private int cualCeldaVigenciaConceptoRL, tipoListaVigenciaConceptoRL;
   private VigenciasConceptosRL duplicarVigenciaConceptoRL;
   private boolean cambiosVigenciaConceptoRL;
   private String auxVCRL_Descripcion;
   private Date auxVCRL_FechaIni, auxVCRL_FechaFin;
   private Column vigenciaCRLFechaInicial, vigenciaCRLFechaFinal, vigenciaCRLDescripcion;
   private boolean permitirIndexVigenciaConceptoRL;
   ///////////////////FormulasConceptos///////////////////////
   private int banderaFormulasConceptos;
   private List<FormulasConceptos> listFormulasConceptosModificar;
   private FormulasConceptos nuevaFormulasConceptos;
   private List<FormulasConceptos> listFormulasConceptosCrear;
   private List<FormulasConceptos> listFormulasConceptosBorrar;
   private FormulasConceptos editarFormulasConceptos;
   private int cualCeldaFormulasConceptos, tipoListaFormulasConceptos;
   private FormulasConceptos duplicarFormulasConceptos;
   private boolean cambiosFormulasConceptos;
   private String auxFC_Descripcion, auxFC_Orden;
   private Date auxFC_FechaIni, auxFC_FechaFin;
   private Column formulaCFechaInicial, formulaCFechaFinal, formulaCNombre, formulaCOrden;
   private boolean permitirIndexFormulasConceptos;
   private FormulasConceptos actualFormulaConcepto;

   ////////////Listas Valores VigenciasCuentas/////////////
   private List<TiposCentrosCostos> lovTiposCentrosCostos;
   private List<TiposCentrosCostos> filtrarListTiposCentrosCostos;
   private TiposCentrosCostos tipoCentroCostoSeleccionadoLOV;

   private List<Cuentas> lovCuentas;
   private List<Cuentas> lovCuentas2505;
   private List<Cuentas> filtrarListCuentas;
   private Cuentas cuentaSeleccionadaLOV;

   private List<CentrosCostos> lovCentrosCostos;
   private List<CentrosCostos> filtrarListCentrosCostos;
   private CentrosCostos centroCostoSeleccionadoLOV;

   private List<Procesos> lovProcesos;
   private List<Procesos> filtrarlovProcesos;
   private Procesos procesoSeleccionadoLOV;
   /////////////Lista Valores VigenciaGrupoConcepto///////////////////////
   private List<GruposConceptos> lovGruposConceptos;
   private List<GruposConceptos> filtrarListGruposConceptos;
   private GruposConceptos grupoConceptoSeleccionadoLOV;
   /////////////Lista Valores VigenciasConceptosTT///////////////////////
   private List<TiposTrabajadores> lovTiposTrabajadores;
   private List<TiposTrabajadores> filtrarListTiposTrabajadores;
   private TiposTrabajadores tipoTrabajadorSeleccionadoLOV;
   /////////////Lista Valores VigenciasConceptosTC///////////////////////
   private List<TiposContratos> lovTiposContratos;
   private List<TiposContratos> filtrarListTiposContratos;
   private TiposContratos tipoContratoSeleccionadoLOV;
   /////////////Lista Valores VigenciasConceptosRL///////////////////////
   private List<ReformasLaborales> lovReformasLaborales;
   private List<ReformasLaborales> filtrarListReformasLaborales;
   private ReformasLaborales reformaLaboralSeleccionadoLOV;
   /////////////Lista Valores FormulasConceptos///////////////////////
   private List<Formulas> lovFormulas;
   private List<Formulas> filtrarListFormulas;
   private Formulas formulaSeleccionadoLOV;

   private List<FormulasConceptos> lovFormulasConceptos;
   private List<FormulasConceptos> filtrarListFormulasConceptos;
   private FormulasConceptos formulaConceptoSeleccionadoLOV;
   //////////////Otros////////////////
   private boolean aceptar;
   private boolean guardado;
   private BigInteger l;
   private int k;
   private String nombreXML, nombreExportar;
   private String nombreTabla;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private int tipoActualizacion;
   private Date fechaParametro;
   private String comportamientoConcepto;
   private boolean formulaSeleccionada;
   private String conceptoEliminar;
   private String mensajeError = "";
   private int num;
   ////////////////////////////////
   private String infoRegistroLovTipoCentroCosto, infoRegistroLovCuentaDebito, infoRegistroLovCuentas, infoRegistroLovCuentaCredito, infoRegistroLovCentroCostoDebito, infoRegistroLovCentroCostoCredito, infoRegistroLovProcesos,
           infoRegistroLovGrupoConcepto, infoRegistroLovTipoTrabajador, infoRegistroLovTipoContrato, infoRegistroLovReformaLaboral, infoRegistroLovFormula, infoRegistroLovFormulasConceptos;
   ////
   public boolean activarLOV;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private ListasRecurrentes listasRecurrentes;

   public ControlDetalleConcepto() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
      log.info("ControlDetalleConcepto() : 1");
      altoTablaVigenciaCuenta = "105";
      altoTablaVigenciaGrupoC = "120";
      altoTablaVigenciaConceptoTT = "105";
      altoTablaVigenciaConceptoTC = "105";
      altoTablaVigenciaConceptoRL = "105";
      altoTablaFormulaConcepto = "122";
      //
      formulaSeleccionada = true;
      conceptoActual = new Conceptos();

      listFormulasConceptos = null;
      listVigenciasConceptosRLConcepto = null;
      listVigenciasConceptosTCConcepto = null;
      listVigenciasConceptosTTConcepto = null;
      listVigenciasGruposConceptos = null;
      listVigenciasCuentasConcepto = null;

      comportamientoConcepto = "";
      fechaParametro = new Date(1, 1, 0);

      permitirIndexVigenciaCuenta = true;
      permitirIndexVigenciaGrupoConcepto = true;
      permitirIndexVigenciaConceptoTT = true;
      permitirIndexVigenciaConceptoTC = true;
      permitirIndexVigenciaConceptoRL = true;
      permitirIndexFormulasConceptos = true;

      lovTiposCentrosCostos = null;
      lovTiposContratos = null;
      lovCuentas = null;
      lovCentrosCostos = null;
      lovReformasLaborales = null;
      lovFormulas = null;
      lovFormulasConceptos = null;
      lovProcesos = null;
      lovGruposConceptos = null;
      lovTiposTrabajadores = null;

      tipoCentroCostoSeleccionadoLOV = null;
      tipoTrabajadorSeleccionadoLOV = null;
      tipoContratoSeleccionadoLOV = null;
      cuentaSeleccionadaLOV = null;
      centroCostoSeleccionadoLOV = null;
      reformaLaboralSeleccionadoLOV = null;
      formulaSeleccionadoLOV = null;
      formulaConceptoSeleccionadoLOV = null;

      nombreExportar = "";
      nombreTablaRastro = "";
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      backUp = null;

      aceptar = true;
      k = 0;

      listVigenciasCuentasBorrar = new ArrayList<VigenciasCuentas>();
      listVigenciasCuentasCrear = new ArrayList<VigenciasCuentas>();
      listVigCParaCrear = new ArrayList<VigenciasCuentas>();
      listVigenciasCuentasModificar = new ArrayList<VigenciasCuentas>();

      listVigenciasGruposConceptosBorrar = new ArrayList<VigenciasGruposConceptos>();
      listVigenciasGruposConceptosCrear = new ArrayList<VigenciasGruposConceptos>();
      listVigenciasGruposConceptosModificar = new ArrayList<VigenciasGruposConceptos>();

      listVigenciasConceptosTTBorrar = new ArrayList<VigenciasConceptosTT>();
      listVigenciasConceptosTTCrear = new ArrayList<VigenciasConceptosTT>();
      listVigenciasConceptosTTModificar = new ArrayList<VigenciasConceptosTT>();

      listVigenciasConceptosTCBorrar = new ArrayList<VigenciasConceptosTC>();
      listVigenciasConceptosTCCrear = new ArrayList<VigenciasConceptosTC>();
      listVigenciasConceptosTCModificar = new ArrayList<VigenciasConceptosTC>();

      listVigenciasConceptosRLBorrar = new ArrayList<VigenciasConceptosRL>();
      listVigenciasConceptosRLCrear = new ArrayList<VigenciasConceptosRL>();
      listVigenciasConceptosRLModificar = new ArrayList<VigenciasConceptosRL>();

      listFormulasConceptosBorrar = new ArrayList<FormulasConceptos>();
      listFormulasConceptosCrear = new ArrayList<FormulasConceptos>();
      listFormulasConceptosModificar = new ArrayList<FormulasConceptos>();

      editarVigenciaCuenta = new VigenciasCuentas();
      editarVigenciaGrupoConcepto = new VigenciasGruposConceptos();
      editarVigenciaConceptoTT = new VigenciasConceptosTT();
      editarVigenciaConceptoTC = new VigenciasConceptosTC();
      editarVigenciaConceptoRL = new VigenciasConceptosRL();
      editarFormulasConceptos = new FormulasConceptos();

      cualCeldaVigenciaCuenta = -1;
      cualCeldaVigenciaGrupoConcepto = -1;
      cualCeldaVigenciaConceptoTT = -1;
      cualCeldaVigenciaConceptoTC = -1;
      cualCeldaVigenciaConceptoRL = -1;
      cualCeldaFormulasConceptos = -1;

      tipoListaVigenciaCuenta = 0;
      tipoListaVigenciaGrupoConcepto = 0;
      tipoListaVigenciaConceptoTT = 0;
      tipoListaVigenciaConceptoTC = 0;
      tipoListaVigenciaConceptoRL = 0;
      tipoListaFormulasConceptos = 0;

      guardado = true;
//
//      nuevaVigenciaCuenta = new VigenciasCuentas();
//      nuevaVigenciaCuenta.setFechainicial(new Date(100, 0, 1));
//      nuevaVigenciaCuenta.setFechafinal(new Date(9999 - 1900, 11, 31));
//      nuevaVigenciaCuenta.setConsolidadorc(new CentrosCostos());
//      nuevaVigenciaCuenta.setConsolidadord(new CentrosCostos());
//      nuevaVigenciaCuenta.setCuentac(new Cuentas());
//      nuevaVigenciaCuenta.setCuentad(new Cuentas());
//      nuevaVigenciaCuenta.setTipocc(new TiposCentrosCostos());
//      nuevaVigenciaCuenta.setProceso(null);
//      nuevaVigenciaCuenta.setNombreProceso("");

      nuevaVigenciaGrupoConcepto = new VigenciasGruposConceptos();
      nuevaVigenciaGrupoConcepto.setGrupoconcepto(new GruposConceptos());
      nuevaVigenciaConceptoTT = new VigenciasConceptosTT();
      nuevaVigenciaConceptoRL = new VigenciasConceptosRL();
      nuevaVigenciaConceptoRL.setTiposalario(new ReformasLaborales());
      nuevaFormulasConceptos = new FormulasConceptos();
//        nuevaFormulasConceptos.setFormula(new Formulas());

      banderaVigenciaCuenta = 0;
      banderaVigenciaGrupoConcepto = 0;
      banderaVigenciaConceptoTT = 0;
      banderaVigenciaConceptoTC = 0;
      banderaVigenciaConceptoRL = 0;
      banderaFormulasConceptos = 0;

      nombreTabla = ":formExportarVigenciasCuentas:datosVigenciaCuentasExportar";
      nombreXML = "ConceptosXML";

//      duplicarVigenciaCuenta = new VigenciasCuentas();
//      duplicarVigenciaCuenta.setConsolidadorc(new CentrosCostos());
//      duplicarVigenciaCuenta.setConsolidadord(new CentrosCostos());
//      duplicarVigenciaCuenta.setCuentac(new Cuentas());
//      duplicarVigenciaCuenta.setCuentad(new Cuentas());
//      duplicarVigenciaCuenta.setTipocc(new TiposCentrosCostos());
      duplicarVigenciaGrupoConcepto = new VigenciasGruposConceptos();
      duplicarVigenciaConceptoTT = new VigenciasConceptosTT();
      duplicarVigenciaConceptoTC = new VigenciasConceptosTC();
      duplicarVigenciaConceptoRL = new VigenciasConceptosRL();
      duplicarFormulasConceptos = new FormulasConceptos();

      cambiosVigenciaCuenta = false;
      cambiosVigenciaGrupoConcepto = false;
      cambiosVigenciaConceptoTT = false;
      cambiosVigenciaConceptoTC = false;
      cambiosVigenciaConceptoRL = false;
      cambiosFormulasConceptos = false;

      vigenciaCuentaSeleccionada = null;
      vigenciaGrupoCoSeleccionada = null;
      vigenciaConceptoRLSeleccionada = null;
      vigenciaConceptoTCSeleccionada = null;
      vigenciaConceptoTTSeleccionada = null;
      vigFormulaConceptoSeleccionada = null;

      infoRegistroLovTipoCentroCosto = "0";
      infoRegistroLovCuentaDebito = "0";
      infoRegistroLovCuentaCredito = "0";
      infoRegistroLovCentroCostoDebito = "0";
      infoRegistroLovCentroCostoCredito = "0";
      infoRegistroLovGrupoConcepto = "0";
      infoRegistroLovTipoTrabajador = "0";
      infoRegistroLovTipoContrato = "0";
      infoRegistroLovReformaLaboral = "0";
      infoRegistroLovFormula = "0";
      infoRegistroLovFormulasConceptos = "0";

      infoRegistroCuenta = "0";
      infoRegistroGrupoC = "0";
      infoRegistroConceptoTT = "0";
      infoRegistroConceptoTC = "0";
      infoRegistroConceptoRL = "0";
      infoRegistroFormulaConcepto = "0";

      activarLOV = true;
      log.info("ControlDetalleConcepto() : 2");
      num = 1;
      paginaAnterior = "nominaf";
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
      String pagActual = "detalleconcepto";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
      } else if (pag.equals("formula")) {
          System.out.println("Controlador.ControlDetalleConcepto.navegar(): Entre a formula" );
         Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         mapParaEnviar.put("paginaAnterior", pagActual);
         mapParaEnviar.put("secFormula", vigFormulaConceptoSeleccionada.getFormula());
         mapParaEnviar.put("cargarFormula", (String) "SI");
         ControlFormula controlFormula = (ControlFormula) fc.getApplication().evaluateExpressionGet(fc, "#{controlFormula}", ControlFormula.class);
         controlFormula.recibirParametros(mapParaEnviar);
      } else if (pag.equals(pagActual)) {
         controlListaNavegacion.guardarNavegacion("concepto", pagActual);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pagActual);
      } else {
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
      }
      limpiarListasValor();
   }

   public void limpiarListasValor() {
      lovCentrosCostos = null;
      lovCuentas = null;
      lovFormulas = null;
      lovFormulasConceptos = null;
      lovGruposConceptos = null;
      lovProcesos = null;
      lovReformasLaborales = null;
      lovTiposCentrosCostos = null;
      lovTiposContratos = null;
      lovTiposTrabajadores = null;
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }

   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      log.info("Entro en inicializarAdministrador()");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarDetalleConcepto.obtenerConexion(ses.getId());
         log.info("FacesContext x : " + x.toString());
         administrarRastros.obtenerConexion(ses.getId());
         log.info("HttpSession ses : " + ses.toString());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void obtenerConcepto(BigInteger secuencia, String paginaEntr) {
      if (num == 1) {
         paginaAnterior = paginaEntr;
         conceptoActual = administrarDetalleConcepto.consultarConceptoActual(secuencia);
         if (conceptoActual != null) {
            Long auto = administrarDetalleConcepto.contarFormulasConceptosConcepto(conceptoActual.getSecuencia());
            Long semi = administrarDetalleConcepto.contarFormulasNovedadesConcepto(conceptoActual.getSecuencia());
            log.info("obtenerConcepto() auto : " + auto);
            log.info("obtenerConcepto() semi : " + semi);
            if ((auto == 0 && semi == 0) || auto == null || semi == null) {
               if (conceptoActual.getDescripcion().length() > 60) {
//                        comportamientoConcepto = conceptoActual.getInfoDetalleConcepto().substring(0, 30) + "/ MANUAL";
                  comportamientoConcepto = conceptoActual.getCodigo() + " - " + conceptoActual.getDescripcion().substring(0, 59) + " - " + conceptoActual.getNaturalezaConcepto() + " / MANUAL";
               } else {
//                        comportamientoConcepto = conceptoActual.getInfoDetalleConcepto() + "MANUAL";
                  comportamientoConcepto = conceptoActual.getCodigo() + " - " + conceptoActual.getDescripcion() + " - " + conceptoActual.getNaturalezaConcepto() + " / MANUAL";
               }
            } else if ((auto > 0 && semi > 0) || (auto > 0 && semi == 0)) {
               if (conceptoActual.getDescripcion().length() > 60) {
                  comportamientoConcepto = conceptoActual.getCodigo() + " - " + conceptoActual.getDescripcion().substring(0, 59) + " - " + conceptoActual.getNaturalezaConcepto() + " / AUTOMATICO";
//                            comportamientoConcepto = conceptoActual.getInfoDetalleConcepto().substring(0, 30) + "/ AUTOMATICO";
               } else {
                  comportamientoConcepto = conceptoActual.getCodigo() + " - " + conceptoActual.getDescripcion() + " - " + conceptoActual.getNaturalezaConcepto() + " / AUTOMATICO";
//                            comportamientoConcepto = conceptoActual.getInfoDetalleConcepto() + "AUTOMATICO";
               }
            } else if (auto == 0 && semi > 0) {
               if (conceptoActual.getDescripcion().length() > 60) {
                  comportamientoConcepto = conceptoActual.getCodigo() + " - " + conceptoActual.getDescripcion().substring(0, 59) + " - " + conceptoActual.getNaturalezaConcepto() + " / SEMI-AUTOMATICO";
//                            comportamientoConcepto = conceptoActual.getInfoDetalleConcepto().substring(0, 30) + "/ SEMI-AUTOMATICO";
               } else {
                  comportamientoConcepto = conceptoActual.getCodigo() + " - " + conceptoActual.getDescripcion() + " - " + conceptoActual.getNaturalezaConcepto() + " / SEMI-AUTOMATICO";
//                        comportamientoConcepto = conceptoActual.getInfoDetalleConcepto() + "SEMI-AUTOMATICO";
               }
            }
         }
         for (int i = 0; i < 7; i++) {
            k++;
            listVigCParaCrear.add(new VigenciasCuentas((new BigInteger("" + k)), conceptoActual));
         }
         getListFormulasConceptos();
         getListVigenciasConceptosRLConcepto();
         getListVigenciasConceptosTTConcepto();
         getListVigenciasConceptosTCConcepto();
         getListVigenciasGruposConceptos();
         getListVigenciasCuentasConcepto();
         listVigenciasCuentasConcepto = null;
         listVigenciasGruposConceptos = null;
         listVigenciasConceptosTTConcepto = null;
         listVigenciasConceptosTCConcepto = null;
         listVigenciasConceptosRLConcepto = null;
         listFormulasConceptos = null;
         formulaSeleccionada = true;
         try {
            navegar("detalleconcepto");
         } catch (Exception e) {
            log.info("obtenerConcepto() Entro al Catch, Error : " + e.toString());
         }
         num++;
      }
   }

   public void modificarVigenciaCuenta() {
      if (validarActualizarVigenciaCuenta()) {
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
            cambiosVigenciaCuenta = true;
         }
      }
   }

   public void modificarVigenciaCuenta(VigenciasCuentas cuenta, String columnCambio, String valor) {
      cargarLOVsVCuentas();
      int coincidencias = 0;
      tipoActualizacion = 0;
      int indiceUnicoElemento = 0;
      vigenciaCuentaSeleccionada = cuenta;

      if (columnCambio.equalsIgnoreCase("TIPOCC")) {
         vigenciaCuentaSeleccionada.setNombreTipocc(auxVC_TipoCC);

         for (int i = 0; i < lovTiposCentrosCostos.size(); i++) {
            if (lovTiposCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setTipocc(lovTiposCentrosCostos.get(indiceUnicoElemento));
            autocompletarTipoCC(vigenciaCuentaSeleccionada);
         } else {
            contarRegistrosLovTipoCentroCosto();
            RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (columnCambio.equalsIgnoreCase("CODDEBITO")) {
         vigenciaCuentaSeleccionada.setCodCuentad(auxVC_Debito);

         for (int i = 0; i < lovCuentas.size(); i++) {
            if (lovCuentas.get(i).getCodigo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setCuentad(lovCuentas.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCuentaDebito();
            RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (columnCambio.equalsIgnoreCase("DESDEBITO")) {
         vigenciaCuentaSeleccionada.setDescripcionCuentad(auxVC_DescDeb);

         for (int i = 0; i < lovCuentas.size(); i++) {
            if (lovCuentas.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setCuentad(lovCuentas.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCuentaDebito();
            RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (columnCambio.equalsIgnoreCase("CONSOLIDADORDEBITO")) {
         vigenciaCuentaSeleccionada.setNombreConsolidadord(auxVC_ConsDeb);

         for (int i = 0; i < lovCentrosCostos.size(); i++) {
            if (lovCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setConsolidadord(lovCentrosCostos.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCentroCostoDebito();
            RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
            RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (columnCambio.equalsIgnoreCase("CODCREDITO")) {
         vigenciaCuentaSeleccionada.setCodCuentac(auxVC_Credito);

         for (int i = 0; i < lovCuentas.size(); i++) {
            if (lovCuentas.get(i).getCodigo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setCuentac(lovCuentas.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCuentaCredito();
            RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
            RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (columnCambio.equalsIgnoreCase("DESCREDITO")) {
         vigenciaCuentaSeleccionada.setDescripcionCuentac(auxVC_DescCre);

         for (int i = 0; i < lovCuentas.size(); i++) {
            if (lovCuentas.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setCuentac(lovCuentas.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCuentaCredito();
            RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (columnCambio.equalsIgnoreCase("PROCESO")) {
         if (valor.equals("") || valor.isEmpty()) {
            vigenciaCuentaSeleccionada.setNombreProceso("");
            vigenciaCuentaSeleccionada.setProceso(null);
            coincidencias = 1;
         } else {
            vigenciaCuentaSeleccionada.setNombreProceso(auxVC_Proceso);

            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigenciaCuentaSeleccionada.setProceso(lovProcesos.get(indiceUnicoElemento).getSecuencia());
               vigenciaCuentaSeleccionada.setNombreProceso(lovProcesos.get(indiceUnicoElemento).getDescripcion());
            } else {
               contarRegistrosLovCuentaCredito();
               RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
               tipoActualizacion = 0;
            }
         }
      }
      if (columnCambio.equalsIgnoreCase("CONSOLIDADORCREDITO")) {
         vigenciaCuentaSeleccionada.setNombreConsolidadorc(auxVC_ConsCre);

         for (int i = 0; i < lovCentrosCostos.size(); i++) {
            if (lovCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setConsolidadorc(lovCentrosCostos.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCentroCostoCredito();
            RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
            RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         modificarVigenciaCuenta();
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
   }

   //////////////VigenciaGrupoConcepto////////////////
   public void modificarVigenciaGrupoConcepto() {
      boolean retorno = validarNuevosDatosVigenciaGrupoConcepto(0);
      if (retorno == true) {
         if (!listVigenciasGruposConceptosCrear.contains(vigenciaGrupoCoSeleccionada)) {
            if (listVigenciasGruposConceptosModificar.isEmpty()) {
               listVigenciasGruposConceptosModificar.add(vigenciaGrupoCoSeleccionada);
            } else if (!listVigenciasGruposConceptosModificar.contains(vigenciaGrupoCoSeleccionada)) {
               listVigenciasGruposConceptosModificar.add(vigenciaGrupoCoSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }

         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
         cambiosVigenciaGrupoConcepto = true;
      } else {
         vigenciaGrupoCoSeleccionada.getGrupoconcepto().setStrCodigo(auxVGC_Codigo);
         vigenciaGrupoCoSeleccionada.getGrupoconcepto().setDescripcion(auxVGC_Descripcion);

         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void modificarVigenciaGrupoConcepto(VigenciasGruposConceptos grupoC, String columnCambio, String valor) {
      cargarLOVsVGrupoConcepto();
      RequestContext context = RequestContext.getCurrentInstance();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      vigenciaGrupoCoSeleccionada = grupoC;
      if (columnCambio.equalsIgnoreCase("CODIGO")) {
         vigenciaGrupoCoSeleccionada.getGrupoconcepto().setStrCodigo(auxVGC_Codigo);

         for (int i = 0; i < lovGruposConceptos.size(); i++) {
            if (lovGruposConceptos.get(i).getStrCodigo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaGrupoCoSeleccionada.setGrupoconcepto(lovGruposConceptos.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovGrupoConcepto();
            permitirIndexVigenciaGrupoConcepto = false;
            RequestContext.getCurrentInstance().update("formlovGrupoConcepto:GruposConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (columnCambio.equalsIgnoreCase("DESCRIPCION")) {
         vigenciaGrupoCoSeleccionada.getGrupoconcepto().setDescripcion(auxVGC_Descripcion);

         for (int i = 0; i < lovGruposConceptos.size(); i++) {
            if (lovGruposConceptos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaGrupoCoSeleccionada.setGrupoconcepto(lovGruposConceptos.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovGrupoConcepto();
            permitirIndexVigenciaGrupoConcepto = false;
            RequestContext.getCurrentInstance().update("formlovGrupoConcepto:GruposConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listVigenciasGruposConceptosCrear.contains(vigenciaGrupoCoSeleccionada)) {
            if (listVigenciasGruposConceptosModificar.isEmpty()) {
               listVigenciasGruposConceptosModificar.add(vigenciaGrupoCoSeleccionada);
            } else if (!listVigenciasGruposConceptosModificar.contains(vigenciaGrupoCoSeleccionada)) {
               listVigenciasGruposConceptosModificar.add(vigenciaGrupoCoSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         cambiosVigenciaGrupoConcepto = true;
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
   }

   //////////////VigenciaConceptoTT////////////////
   public void modificarVigenciaConceptoTT() {
      cargarLOVsVConceptosTT();
      boolean retorno = validarNuevosDatosVigenciaConceptoTT(0);
      if (retorno == true) {
         if (!listVigenciasConceptosTTCrear.contains(vigenciaConceptoTTSeleccionada)) {
            if (listVigenciasConceptosTTModificar.isEmpty()) {
               listVigenciasConceptosTTModificar.add(vigenciaConceptoTTSeleccionada);
            } else if (!listVigenciasConceptosTTModificar.contains(vigenciaConceptoTTSeleccionada)) {
               listVigenciasConceptosTTModificar.add(vigenciaConceptoTTSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
         cambiosVigenciaConceptoTT = true;
      } else {
         vigenciaConceptoTTSeleccionada.getTipotrabajador().setNombre(auxVCTT_Descripcion);

         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   //////////////VigenciaConceptoTT////////////////
   public void modificarVigenciaConceptoTT(VigenciasConceptosTT conceptoTT, String columnCambio, String valor) {
      cargarLOVsVConceptosTT();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      vigenciaConceptoTTSeleccionada = conceptoTT;
      RequestContext context = RequestContext.getCurrentInstance();
      if (columnCambio.equalsIgnoreCase("TRABAJADOR")) {
         vigenciaConceptoTTSeleccionada.getTipotrabajador().setNombre(auxVCTT_Descripcion);

         for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
            if (lovTiposTrabajadores.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaConceptoTTSeleccionada.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovTipoTrabajador();
            permitirIndexVigenciaConceptoTT = false;
            RequestContext.getCurrentInstance().update("formlovTipoTrabajador:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listVigenciasConceptosTTCrear.contains(vigenciaConceptoTTSeleccionada)) {
            if (listVigenciasConceptosTTModificar.isEmpty()) {
               listVigenciasConceptosTTModificar.add(vigenciaConceptoTTSeleccionada);
            } else if (!listVigenciasConceptosTTModificar.contains(vigenciaConceptoTTSeleccionada)) {
               listVigenciasConceptosTTModificar.add(vigenciaConceptoTTSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         cambiosVigenciaConceptoTT = true;
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
   }

   //////////////VigenciaConceptoTC////////////////
   public void modificarVigenciaConceptoTC() {
      boolean retorno = validarNuevosDatosVigenciaConceptoTC(0);
      if (retorno == true) {
         if (!listVigenciasConceptosTCCrear.contains(vigenciaConceptoTCSeleccionada)) {
            if (listVigenciasConceptosTCModificar.isEmpty()) {
               listVigenciasConceptosTCModificar.add(vigenciaConceptoTCSeleccionada);
            } else if (!listVigenciasConceptosTCModificar.contains(vigenciaConceptoTCSeleccionada)) {
               listVigenciasConceptosTCModificar.add(vigenciaConceptoTCSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
         cambiosVigenciaConceptoTC = true;
      } else {
         vigenciaConceptoTCSeleccionada.getTipocontrato().setNombre(auxVCTC_Descripcion);
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   //////////////VigenciaConceptoTC////////////////
   public void modificarVigenciaConceptoTC(VigenciasConceptosTC conceptoTC, String columnCambio, String valor) {
      cargarLOVsVConceptosTC();
      boolean retorno = validarNuevosDatosVigenciaConceptoTC(0);
      if (retorno == true) {
         int coincidencias = 0;
         int indiceUnicoElemento = 0;
         vigenciaConceptoTCSeleccionada = conceptoTC;
         if (columnCambio.equalsIgnoreCase("CONTRATO")) {
            vigenciaConceptoTCSeleccionada.getTipocontrato().setNombre(auxVCTC_Descripcion);

            for (int i = 0; i < lovTiposContratos.size(); i++) {
               if (lovTiposContratos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigenciaConceptoTCSeleccionada.setTipocontrato(lovTiposContratos.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovTipoContrato();
               permitirIndexVigenciaConceptoTC = false;
               RequestContext.getCurrentInstance().update("formlovTipoContrato:TipoContratosDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoContratosDialogo').show()");
               tipoActualizacion = 0;
            }
         }
         if (coincidencias == 1) {
            if (!listVigenciasConceptosTCCrear.contains(vigenciaConceptoTCSeleccionada)) {
               if (listVigenciasConceptosTCModificar.isEmpty()) {
                  listVigenciasConceptosTCModificar.add(vigenciaConceptoTCSeleccionada);
               } else if (!listVigenciasConceptosTCModificar.contains(vigenciaConceptoTCSeleccionada)) {
                  listVigenciasConceptosTCModificar.add(vigenciaConceptoTCSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
            cambiosVigenciaConceptoTC = true;
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
      } else {
         vigenciaConceptoTCSeleccionada.getTipocontrato().setNombre(auxVCTC_Descripcion);

         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   //////////////VigenciaConceptoRL////////////////
   public void modificarVigenciaConceptoRL() {
      boolean retorno = validarNuevosDatosVigenciaConceptoRL(0);
      if (retorno == true) {
         if (!listVigenciasConceptosRLCrear.contains(vigenciaConceptoRLSeleccionada)) {
            if (listVigenciasConceptosRLModificar.isEmpty()) {
               listVigenciasConceptosRLModificar.add(vigenciaConceptoRLSeleccionada);
            } else if (!listVigenciasConceptosRLModificar.contains(vigenciaConceptoRLSeleccionada)) {
               listVigenciasConceptosRLModificar.add(vigenciaConceptoRLSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
         cambiosVigenciaConceptoRL = true;
      } else {
         vigenciaConceptoRLSeleccionada.getTiposalario().setNombre(auxVCRL_Descripcion);
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   //////////////VigenciaConceptoRL////////////////
   public void modificarVigenciaConceptoRL(VigenciasConceptosRL conceptoRL, String columnCambio, String valor) {
      cargarLOVsVConceptosRL();
      boolean retorno = validarNuevosDatosVigenciaConceptoRL(0);
      if (retorno == true) {
         int coincidencias = 0;
         int indiceUnicoElemento = 0;
         vigenciaConceptoRLSeleccionada = conceptoRL;
         if (columnCambio.equalsIgnoreCase("REFORMA")) {
            vigenciaConceptoRLSeleccionada.getTiposalario().setNombre(auxVCRL_Descripcion);

            for (int i = 0; i < lovReformasLaborales.size(); i++) {
               if (lovReformasLaborales.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigenciaConceptoRLSeleccionada.setTiposalario(lovReformasLaborales.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovReformaLaboral();
               permitirIndexVigenciaConceptoRL = false;
               RequestContext.getCurrentInstance().update("formlovReformaLaboral:ReformaLaboralDialogo");
               RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').show()");
               tipoActualizacion = 0;
            }
         }
         if (coincidencias == 1) {
            if (!listVigenciasConceptosRLCrear.contains(vigenciaConceptoRLSeleccionada)) {
               if (listVigenciasConceptosRLModificar.isEmpty()) {
                  listVigenciasConceptosRLModificar.add(vigenciaConceptoRLSeleccionada);
               } else if (!listVigenciasConceptosRLModificar.contains(vigenciaConceptoRLSeleccionada)) {
                  listVigenciasConceptosRLModificar.add(vigenciaConceptoRLSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }

            cambiosVigenciaConceptoTC = true;
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
      } else {
         vigenciaConceptoRLSeleccionada.getTiposalario().setNombre(auxVCRL_Descripcion);
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   //////////////FormulasConceptos////////////////
   public void modificarFormulasConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean retorno = validarNuevosDatosFormulasConceptos(0);
      if (retorno == true) {
         if (!listFormulasConceptosCrear.contains(vigFormulaConceptoSeleccionada)) {
            if (listFormulasConceptosModificar.isEmpty()) {
               listFormulasConceptosModificar.add(vigFormulaConceptoSeleccionada);
            } else if (!listFormulasConceptosModificar.contains(vigFormulaConceptoSeleccionada)) {
               listFormulasConceptosModificar.add(vigFormulaConceptoSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }

         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         cambiosFormulasConceptos = true;
      } else {
         vigFormulaConceptoSeleccionada.setNombreFormula(auxFC_Descripcion);
         vigFormulaConceptoSeleccionada.setStrOrden(auxFC_Orden);

         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   //////////////FormulasConceptos////////////////
   public void modificarFormulasConceptos(FormulasConceptos formulaC, String columnCambio, String valor) {
      cargarLOVsFormulasConceptos();
      boolean retorno = validarNuevosDatosFormulasConceptos(0);
      if (retorno == true) {
         int coincidencias = 0;
         int indiceUnicoElemento = 0;
         vigFormulaConceptoSeleccionada = formulaC;
         if (columnCambio.equalsIgnoreCase("FORMULA")) {
            vigFormulaConceptoSeleccionada.setNombreFormula(auxFC_Descripcion);

            for (int i = 0; i < lovFormulas.size(); i++) {
               if (lovFormulas.get(i).getNombrelargo().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigFormulaConceptoSeleccionada.setFormula(lovFormulas.get(indiceUnicoElemento).getSecuencia());
               vigFormulaConceptoSeleccionada.setNombreFormula(lovFormulas.get(indiceUnicoElemento).getNombrelargo());
            } else {
               contarRegistrosLovFormula();
               permitirIndexFormulasConceptos = false;
               RequestContext.getCurrentInstance().update("formlovFormula:FormulasDialogo");
               RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').show()");
               tipoActualizacion = 0;
            }
         }
         if (columnCambio.equalsIgnoreCase("ORDEN")) {
            vigFormulaConceptoSeleccionada.setStrOrden(auxFC_Orden);

            for (int i = 0; i < lovFormulasConceptos.size(); i++) {
               if (lovFormulasConceptos.get(i).getStrOrden().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigFormulaConceptoSeleccionada.setStrOrden(lovFormulasConceptos.get(indiceUnicoElemento).getStrOrden());
            } else {
               contarRegistrosLovFormCon();
               permitirIndexFormulasConceptos = false;
               RequestContext.getCurrentInstance().update("formlovFormulaC:FormulaConceptoDialogo");
               RequestContext.getCurrentInstance().execute("PF('FormulaConceptoDialogo').show()");
               tipoActualizacion = 0;
            }
         }
         if (coincidencias == 1) {
            if (!listFormulasConceptosCrear.contains(vigFormulaConceptoSeleccionada)) {
               if (listFormulasConceptosModificar.isEmpty()) {
                  listFormulasConceptosModificar.add(vigFormulaConceptoSeleccionada);
               } else if (!listFormulasConceptosModificar.contains(vigFormulaConceptoSeleccionada)) {
                  listFormulasConceptosModificar.add(vigFormulaConceptoSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
            cambiosVigenciaConceptoTC = true;
         }
         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      } else {
         vigFormulaConceptoSeleccionada.setNombreFormula(auxFC_Descripcion);
         vigFormulaConceptoSeleccionada.setStrOrden(auxFC_Orden);
         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   ///////////////////////////////////////////////////////////////////////////
   public void valoresBackupAutocompletarVCuenta(VigenciasCuentas vigencia, String campo) {
      vigCuentaCrearSeleccionada = vigencia;
//      tipoActualizacion = tipoNuevo;
      switch (campo) {
         case "TIPOCC":
            auxVC_TipoCC = vigCuentaCrearSeleccionada.getNombreTipocc();
            break;
         case "CODDEBITO":
            auxVC_Debito = vigCuentaCrearSeleccionada.getCodCuentad();
            break;
         case "DESDEBITO":
            auxVC_DescDeb = vigCuentaCrearSeleccionada.getDescripcionCuentad();
            break;
         case "CONSOLIDADORDEBITO":
            auxVC_ConsDeb = vigCuentaCrearSeleccionada.getNombreConsolidadord();
            break;
         case "CODCREDITO":
            auxVC_Credito = vigCuentaCrearSeleccionada.getCodCuentac();
            break;
         case "DESCREDITO":
            auxVC_DescCre = vigCuentaCrearSeleccionada.getDescripcionCuentac();
            break;
         case "CONSOLIDADORCREDITO":
            auxVC_ConsCre = vigCuentaCrearSeleccionada.getNombreConsolidadorc();
            break;
         case "PROCESO":
            log.info("valoresBackupAutocompletarGeneral campo = 'PROCESO'");
            auxVC_Proceso = vigCuentaCrearSeleccionada.getNombreProceso();
            break;
         default:
            break;
      }
   }

   public void valoresBackupAutocompletarGeneral(int tipoNuevo, String campo, int tabla) {
      tipoActualizacion = tipoNuevo;
      if (tabla == 1) {
         if (campo.equals("CODIGO")) {
            if (tipoNuevo == 1) {
               auxVGC_Codigo = nuevaVigenciaGrupoConcepto.getGrupoconcepto().getStrCodigo();
            } else if (tipoNuevo == 2) {
               auxVGC_Codigo = duplicarVigenciaGrupoConcepto.getGrupoconcepto().getStrCodigo();
            }
         } else if (campo.equals("DESCRIPCION")) {
            if (tipoNuevo == 1) {
               auxVGC_Descripcion = nuevaVigenciaGrupoConcepto.getGrupoconcepto().getDescripcion();
            } else if (tipoNuevo == 2) {
               auxVGC_Descripcion = duplicarVigenciaGrupoConcepto.getGrupoconcepto().getDescripcion();
            }
         }
      }
      if (tabla == 2) {
         if (campo.equals("TRABAJADOR")) {
            if (tipoNuevo == 1) {
               auxVCTT_Descripcion = nuevaVigenciaConceptoTT.getTipotrabajador().getNombre();
            } else if (tipoNuevo == 2) {
               auxVCTT_Descripcion = duplicarVigenciaConceptoTT.getTipotrabajador().getNombre();
            }
         }
      }
      if (tabla == 3) {
         if (campo.equals("CONTRATO")) {
            if (tipoNuevo == 1) {
               auxVCTC_Descripcion = nuevaVigenciaConceptoTC.getTipocontrato().getNombre();
            } else if (tipoNuevo == 2) {
               auxVCTC_Descripcion = duplicarVigenciaConceptoTC.getTipocontrato().getNombre();
            }
         }
      }
      if (tabla == 4) {
         if (campo.equals("REFORMA")) {
            if (tipoNuevo == 1) {
               auxVCRL_Descripcion = nuevaVigenciaConceptoRL.getTiposalario().getNombre();
            } else if (tipoNuevo == 2) {
               auxVCRL_Descripcion = duplicarVigenciaConceptoRL.getTiposalario().getNombre();
            }
         }
      }
      if (tabla == 5) {
         if (campo.equals("FORMULA")) {
            if (tipoNuevo == 1) {
               auxFC_Descripcion = nuevaFormulasConceptos.getNombreFormula();
            } else if (tipoNuevo == 2) {
               auxFC_Descripcion = duplicarFormulasConceptos.getNombreFormula();
            }
         }
         if (campo.equals("ORDEN")) {
            if (tipoNuevo == 1) {
               auxFC_Orden = nuevaFormulasConceptos.getStrOrden();
            } else if (tipoNuevo == 2) {
               auxFC_Orden = duplicarFormulasConceptos.getStrOrden();
            }
         }
      }
   }

   public boolean autocompletarTipoCC(VigenciasCuentas vigCuenta) {
      int error = 0;
      CentrosCostos ccLocalizacionTrabajador = null;
      CentrosCostos ccContabilidad = null;
      List<Cuentas> cuenta2505 = null;
      log.info("ControlParametrizacionContable.autocompletarTipoCC() conceptoActual : " + conceptoActual);
      log.info("ControlParametrizacionContable.autocompletarTipoCC() vigCuenta : " + vigCuenta);
      log.info("ControlParametrizacionContable.autocompletarTipoCC() vigCuenta.getConcepto() : " + vigCuenta.getConcepto());
      if (vigCuenta.getConcepto() == null) {
         vigCuenta.setConcepto(conceptoActual.getSecuencia());
      }
      if (vigCuenta.getConcepto() != null) {
         log.info("ControlParametrizacionContable.autocompletarTipoCC() 1");
         int conteoVCuentas = administrarDetalleConcepto.contarVigCuentasPorTipoccConceptoYCuentac(vigCuenta.getTipocc(), vigCuenta.getCuentac(), vigCuenta.getConcepto(), vigCuenta.getFechainicial());
         if (conteoVCuentas > 1) {
            mensajeError = "El Tipo de Centro Costo que Ingresó ya está asignado a otra VigenciaCuenta";
            log.info("El Tipo de Centro Costo que Ingresó ya está asignado a otra VigenciaCuenta 1");
            error++;
         } else {
            conteoVCuentas = administrarDetalleConcepto.contarVigCuentasPorTipoccConceptoYCuentad(vigCuenta.getTipocc(), vigCuenta.getCuentad(), vigCuenta.getConcepto(), vigCuenta.getFechainicial());
            if (conteoVCuentas > 1) {
               mensajeError = "El Tipo de Centro Costo que Ingresó ya está asignado a otro VigenciaCuenta";
               log.info("El Tipo de Centro Costo que Ingresó ya está asignado a otra VigenciaCuenta 2");
               error++;
            }
         }
         log.info("ControlParametrizacionContable.autocompletarTipoCC() error: " + error);
         if (error == 0) {
            ccLocalizacionTrabajador = administrarDetalleConcepto.centroCostoLocalizacionTrabajador(vigCuenta.getEmpresa());
            ccContabilidad = administrarDetalleConcepto.centroCostoContabilidad(vigCuenta.getEmpresa());
            cuenta2505 = administrarDetalleConcepto.cuenta2505(vigCuenta.getEmpresa());
            log.info("ccLocalizacionTrabajador: " + ccLocalizacionTrabajador);
            log.info("ccContabilidad: " + ccContabilidad);
            log.info("cuenta2505: " + cuenta2505);
            if (cuenta2505 != null) {
               if (cuenta2505.size() != 1) {
                  lovCuentas2505.clear();
                  for (Cuentas recCuenta : lovCuentas) {
                     if (recCuenta.getCodigo().startsWith("2505")) {
                        lovCuentas2505.add(recCuenta);
                     }
                  }
                  error++;
                  mensajeError = "cuentas con [2505...] retornan mas de un registro o ninguno.";
                  log.info(mensajeError);
                  RequestContext.getCurrentInstance().update("formlovCuentas:CuentasDialogo");
                  RequestContext.getCurrentInstance().update("formlovCuentas:lovCuentas");
                  RequestContext.getCurrentInstance().execute("PF('CuentasDialogo').show()");
                  contarRegistrosCuentas();
               }
            } else {
               mensajeError = "Se genero un conflicto consultando cuentas con [2505...]";
               log.info(mensajeError);
               error++;
            }
            log.warn("Error: " + error);
            if (error == 0) {
               log.info("vigCuenta.getConcepto().getNaturaleza(): " + vigCuenta.getNaturaleza());
               log.info("vigCuenta.getNombreTipocc(): " + vigCuenta.getNombreTipocc());
               log.info("vigCuenta.getConsolidadord(): " + vigCuenta.getConsolidadord());
               log.info("vigCuenta.getConsolidadorc(): " + vigCuenta.getConsolidadorc());
               log.info("vigCuenta.getTipocc(): " + vigCuenta.getTipocc());
               if (vigCuenta.getTipocc() != null) {
                  log.info("vigCuenta.getNombreTipocc(): " + vigCuenta.getNombreTipocc());
               }
               if (vigCuenta.getNaturaleza().equals("P")) {
                  log.info("vigCuenta.getNombreTipocc().contains(\"APLICA\")  :: ->" + (vigCuenta.getNombreTipocc().contains("APLICA")));
                  if (!vigCuenta.getNombreTipocc().contains("APLICA")) {
                     if (vigCuenta.getConsolidadord() != null) {
                        if (vigCuenta.getNombreConsolidadord() != null) {
                           log.info("vigCuenta.getNombreConsolidadord(): " + vigCuenta.getNombreConsolidadord());
                           mensajeError = "Atención: Se va a cambiar el centro de costo consolidador débito por la localizacion del trabajador. Revise por favor esta configuración.";
                           log.info(mensajeError);
                           error++;
                        }
                     }
                     vigCuenta.setConsolidadord(ccLocalizacionTrabajador);
                     vigCuenta.setConsolidadorc(ccContabilidad);
                  }
                  if (vigCuenta.getCuentac() == null) {
                     vigCuenta.setCuentac(cuenta2505.get(0));
                  } else if (vigCuenta.getCuentac() == null) {
                     vigCuenta.setCuentac(cuenta2505.get(0));
                  }
               }
               if (vigCuenta.getNaturaleza().equals("D")) {
                  if (!vigCuenta.getNombreTipocc().contains("APLICA")) {
                     if (vigCuenta.getConsolidadorc() != null) {
                        if (vigCuenta.getNombreConsolidadorc() != null) {
                           log.info("vigCuenta.getNombreConsolidadord(): " + vigCuenta.getNombreConsolidadord());
                           mensajeError = "Atención: Se va a cambiar el centro de costo consolidador crédito por la localizacion del trabajador. Revise por favor esta configuración.";
                           log.info(mensajeError);
                           error++;
                        }
                     }
                     vigCuenta.setConsolidadord(ccLocalizacionTrabajador);
                     vigCuenta.setConsolidadorc(ccContabilidad);

                  } else if (tipoActualizacion != 0) {
                     vigCuenta.setConsolidadorc(ccContabilidad);
                     vigCuenta.setConsolidadord(ccContabilidad);
                  }
                  if (vigCuenta.getCuentad() == null) {
                     vigCuenta.setCuentad(cuenta2505.get(0));
                  } else if (vigCuenta.getCuentad() == null) {
                     vigCuenta.setCuentad(cuenta2505.get(0));
                  }
               }
               if (vigCuenta.getNaturaleza().equals("N")) {
                  if (!vigCuenta.getNombreTipocc().contains("APLICA")) {
                     if (vigCuenta.getConsolidadorc() != null) {
                        if (vigCuenta.getNombreConsolidadorc() != null) {
                           log.info("vigCuenta.getNombreConsolidadord(): " + vigCuenta.getNombreConsolidadord());
                           mensajeError = "Atención: Se va a cambiar el centro de costo consolidador crédito por la localizacion del trabajador. Revise por favor esta configuración.";
                           log.info(mensajeError);
                           error++;
                        }
                     }
                     vigCuenta.setConsolidadorc(ccContabilidad);
                     vigCuenta.setConsolidadord(ccContabilidad);
                  }
                  if (vigCuenta.getCuentad() == null) {
                     vigCuenta.setCuentad(cuenta2505.get(0));
                     vigCuenta.setCuentac(cuenta2505.get(0));
                  } else if (vigCuenta.getCuentad() == null) {
                     vigCuenta.setCuentad(cuenta2505.get(0));
                     vigCuenta.setCuentac(cuenta2505.get(0));
                  }
               }
               log.warn("Error: " + error);
               log.info("vigCuenta.getConsolidadord(): " + vigCuenta.getConsolidadord());
               log.info("vigCuenta.getConsolidadorc(): " + vigCuenta.getConsolidadorc());
               log.info("vigCuenta.getTipocc(): " + vigCuenta.getTipocc());
               if (error > 0) {
                  RequestContext.getCurrentInstance().update("form:alertaTCC");
                  RequestContext.getCurrentInstance().execute("PF('alertaTCC').show()");
               }
               return true;
            } else {
               RequestContext.getCurrentInstance().update("form:errorTCC");
               RequestContext.getCurrentInstance().execute("PF('errorTCC').show()");
               return false;
            }
         } else {
            RequestContext.getCurrentInstance().update("form:errorTCC");
            RequestContext.getCurrentInstance().execute("PF('errorTCC').show()");
            return false;
         }
      } else {
         log.info("ControlParametrizacionContable.autocompletarTipoCC() vigCuenta.getConcepto() : " + vigCuenta.getConcepto());
         return false;
      }
   }

   public void autocompletarNuevoyDuplicadoVigenciaCuenta(VigenciasCuentas vigencia, String campo, String valor) {
//      tipoActualizacion = tipoNuevo;
      vigCuentaCrearSeleccionada = vigencia;
      cargarLOVsVCuentas();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      switch (campo) {
         case "TIPOCC":
            vigCuentaCrearSeleccionada.setNombreTipocc(auxVC_TipoCC);
            for (int i = 0; i < lovTiposCentrosCostos.size(); i++) {
               if (lovTiposCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setTipocc(lovTiposCentrosCostos.get(indiceUnicoElemento));
               autocompletarTipoCC(vigCuentaCrearSeleccionada);
            } else {
               contarRegistrosLovTipoCentroCosto();
               RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').show()");
            }
            break;
         case "CODDEBITO":
            vigCuentaCrearSeleccionada.setCodCuentad(auxVC_Debito);
            for (int i = 0; i < lovCuentas.size(); i++) {
               if (lovCuentas.get(i).getCodigo().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setCuentad(lovCuentas.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCuentaDebito();
               RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
               RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta:::nuevaDebitoVC");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta:::nuevaDesDebitoVC");
               }
            }
            break;
         case "DESDEBITO":
            vigCuentaCrearSeleccionada.setDescripcionCuentad(auxVC_DescDeb);
            for (int i = 0; i < lovCuentas.size(); i++) {
               if (lovCuentas.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setCuentad(lovCuentas.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCuentaDebito();
               RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
               RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDebitoVC");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta:::nuevaDesDebitoVC");
               }
            }
            break;
         case "CONSOLIDADORDEBITO":
            vigCuentaCrearSeleccionada.setNombreConsolidadord(auxVC_ConsDeb);
            for (int i = 0; i < lovCentrosCostos.size(); i++) {
               if (lovCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setConsolidadord(lovCentrosCostos.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCentroCostoDebito();
               RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
               RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaConsoliDebVC");
               }
            }
            break;
         case "CODCREDITO":
            vigCuentaCrearSeleccionada.setCodCuentac(auxVC_Credito);
            for (int i = 0; i < lovCuentas.size(); i++) {
               if (lovCuentas.get(i).getCodigo().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setCuentac(lovCuentas.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCuentaCredito();
               RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
               RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaCreditoVC");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDesCreditoVC");
               }
            }
            break;
         case "DESCREDITO":
            vigCuentaCrearSeleccionada.setDescripcionCuentac(auxVC_DescCre);
            for (int i = 0; i < lovCuentas.size(); i++) {
               if (lovCuentas.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setCuentac(lovCuentas.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCuentaCredito();
               RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
               RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaCreditoVC");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDesCreditoVC");
               }
            }
            break;
         case "CONSOLIDADORCREDITO":
            vigCuentaCrearSeleccionada.setNombreConsolidadorc(auxVC_ConsCre);
            for (int i = 0; i < lovCentrosCostos.size(); i++) {
               if (lovCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setConsolidadorc(lovCentrosCostos.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCentroCostoCredito();
               RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
               RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaConsoliCreVC");
               }
            }
            break;
         case "PROCESO":
            log.info("autocompletarNuevoyDuplicadoVigenciaCuenta campo = 'PROCESO'");
            vigCuentaCrearSeleccionada.setNombreProceso(auxVC_Proceso);
            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setProceso(lovProcesos.get(indiceUnicoElemento).getSecuencia());
               vigCuentaCrearSeleccionada.setNombreProceso(lovProcesos.get(indiceUnicoElemento).getDescripcion());
            } else {
               contarRegistrosLovCentroCostoCredito();
               RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevoProceso");
               }
            }
            break;
         default:
            break;
      }
   }

   public void autocompletarNuevoyDuplicadoVigenciaGrupoConcepto(String campo, String valor, int tipoNuevo) {
      cargarLOVsVGrupoConcepto();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (campo.equalsIgnoreCase("CODIGO")) {
         if (tipoNuevo == 1) {
            nuevaVigenciaGrupoConcepto.getGrupoconcepto().setStrCodigo(auxVGC_Codigo);
         } else if (tipoNuevo == 2) {
            duplicarVigenciaGrupoConcepto.getGrupoconcepto().setStrCodigo(auxVGC_Codigo);
         }
         for (int i = 0; i < lovGruposConceptos.size(); i++) {
            if (lovGruposConceptos.get(i).getStrCodigo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigenciaGrupoConcepto.setGrupoconcepto(lovGruposConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoVGC");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcionVGC");
            } else if (tipoNuevo == 2) {
               duplicarVigenciaGrupoConcepto.setGrupoconcepto(lovGruposConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoVGC");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionVGC");
            }
         } else {
            contarRegistrosLovGrupoConcepto();
            RequestContext.getCurrentInstance().update("formlovGrupoConcepto:GruposConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoVGC");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcionVGC");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoVGC");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionVGC");
            }
         }
      } else if (campo.equalsIgnoreCase("DESCRIPCION")) {
         if (tipoNuevo == 1) {
            nuevaVigenciaGrupoConcepto.getGrupoconcepto().setDescripcion(auxVGC_Descripcion);
         } else if (tipoNuevo == 2) {
            duplicarVigenciaGrupoConcepto.getGrupoconcepto().setDescripcion(auxVGC_Descripcion);
         }
         for (int i = 0; i < lovGruposConceptos.size(); i++) {
            if (lovGruposConceptos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigenciaGrupoConcepto.setGrupoconcepto(lovGruposConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoVGC");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcionVGC");
            } else if (tipoNuevo == 2) {
               duplicarVigenciaGrupoConcepto.setGrupoconcepto(lovGruposConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoVGC");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionVGC");
            }
         } else {
            contarRegistrosLovGrupoConcepto();
            RequestContext.getCurrentInstance().update("formlovGrupoConcepto:GruposConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoVGC");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcionVGC");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoVGC");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionVGC");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoVigenciaConceptoTT(String campo, String valor, int tipoNuevo) {
      cargarLOVsVConceptosTT();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (campo.equalsIgnoreCase("TRABAJADOR")) {
         if (tipoNuevo == 1) {
            nuevaVigenciaConceptoTT.getTipotrabajador().setNombre(auxVCTT_Descripcion);
         } else if (tipoNuevo == 2) {
            duplicarVigenciaConceptoTT.getTipotrabajador().setNombre(auxVCTT_Descripcion);
         }
         for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
            if (lovTiposTrabajadores.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigenciaConceptoTT.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTrabajadorVCTT");
            } else if (tipoNuevo == 2) {
               duplicarVigenciaConceptoTT.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTrabajadorVCTT");
            }
         } else {
            contarRegistrosLovTipoTrabajador();
            RequestContext.getCurrentInstance().update("formlovTipoTrabajador:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTrabajadorVCTT");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTrabajadorVCTT");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoVigenciaConceptoTC(String campo, String valor, int tipoNuevo) {
      cargarLOVsVConceptosTC();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (campo.equalsIgnoreCase("CONTRATO")) {
         if (tipoNuevo == 1) {
            nuevaVigenciaConceptoTC.getTipocontrato().setNombre(auxVCTC_Descripcion);
         } else if (tipoNuevo == 2) {
            duplicarVigenciaConceptoTC.getTipocontrato().setNombre(auxVCTC_Descripcion);
         }
         for (int i = 0; i < lovTiposContratos.size(); i++) {
            if (lovTiposContratos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigenciaConceptoTC.setTipocontrato(lovTiposContratos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaContratoVCTC");
            } else if (tipoNuevo == 2) {
               duplicarVigenciaConceptoTC.setTipocontrato(lovTiposContratos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarContratoVCTC");
            }
         } else {
            contarRegistrosLovTipoContrato();
            RequestContext.getCurrentInstance().update("formlovTipoContrato:TipoContratosDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoContratosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaContratoVCTC");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarContratoVCTC");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoVigenciaConceptoRL(String campo, String valor, int tipoNuevo) {
      cargarLOVsVConceptosRL();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (campo.equalsIgnoreCase("REFORMA")) {
         if (tipoNuevo == 1) {
            nuevaVigenciaConceptoRL.getTiposalario().setNombre(auxVCRL_Descripcion);
         } else if (tipoNuevo == 2) {
            duplicarVigenciaConceptoRL.getTiposalario().setNombre(auxVCRL_Descripcion);
         }
         for (int i = 0; i < lovReformasLaborales.size(); i++) {
            if (lovReformasLaborales.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigenciaConceptoRL.setTiposalario(lovReformasLaborales.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaReformaVCRL");
            } else if (tipoNuevo == 2) {
               duplicarVigenciaConceptoRL.setTiposalario(lovReformasLaborales.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarReformaVCRL");
            }
         } else {
            contarRegistrosLovReformaLaboral();
            RequestContext.getCurrentInstance().update("formlovReformaLaboral:ReformaLaboralDialogo");
            RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaReformaVCRL");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarReformaVCRL");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoFormulasConceptos(String campo, String valor, int tipoNuevo) {
      cargarLOVsFormulasConceptos();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (campo.equalsIgnoreCase("FORMULA")) {
         if (tipoNuevo == 1) {
            nuevaFormulasConceptos.setNombreFormula(auxFC_Descripcion);
         } else if (tipoNuevo == 2) {
            duplicarFormulasConceptos.setNombreFormula(auxFC_Descripcion);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulasConceptos.setFormula(lovFormulas.get(indiceUnicoElemento).getSecuencia());
               nuevaFormulasConceptos.setNombreFormula(lovFormulas.get(indiceUnicoElemento).getNombrelargo());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaFC");
            } else if (tipoNuevo == 2) {
               duplicarFormulasConceptos.setFormula(lovFormulas.get(indiceUnicoElemento).getSecuencia());
               duplicarFormulasConceptos.setNombreFormula(lovFormulas.get(indiceUnicoElemento).getNombrelargo());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaFC");
            }
         } else {
            contarRegistrosLovFormula();
            RequestContext.getCurrentInstance().update("formlovFormula:FormulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaFC");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaFC");
            }
         }
      }

      if (campo.equalsIgnoreCase("ORDEN")) {
         if (tipoNuevo == 1) {
            nuevaFormulasConceptos.setStrOrden(auxFC_Orden);
         } else if (tipoNuevo == 2) {
            duplicarFormulasConceptos.setStrOrden(auxFC_Orden);
         }
         for (int i = 0; i < lovFormulasConceptos.size(); i++) {
            if (lovFormulasConceptos.get(i).getStrOrden().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulasConceptos.setStrOrden(lovFormulasConceptos.get(indiceUnicoElemento).getStrOrden());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaOrdenFC");
            } else if (tipoNuevo == 2) {
               duplicarFormulasConceptos.setStrOrden(lovFormulasConceptos.get(indiceUnicoElemento).getStrOrden());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOrdenFC");
            }
         } else {
            contarRegistrosLovFormCon();
            RequestContext.getCurrentInstance().update("formlovFormulaC:FormulaConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaConceptoDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaOrdenFC");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOrdenFC");
            }
         }
      }
   }

   public void cambiarIndiceVigenciaCuenta(VigenciasCuentas cuenta, int celda) {
      if (permitirIndexVigenciaCuenta == true) {
         cualCeldaVigenciaCuenta = celda;
         if (celda > 1) {
            activarBotonLOV();
         } else {
            anularBotonLOV();
         }
         vigenciaCuentaSeleccionada = cuenta;
         ///////// Captura Objetos Para campos NotNull ///////////
         auxVC_FechaIni = vigenciaCuentaSeleccionada.getFechainicial();
         auxVC_FechaFin = vigenciaCuentaSeleccionada.getFechafinal();
         auxVC_TipoCC = vigenciaCuentaSeleccionada.getNombreTipocc();
         auxVC_DescDeb = vigenciaCuentaSeleccionada.getDescripcionCuentad();
         auxVC_Debito = vigenciaCuentaSeleccionada.getCodCuentad();
         auxVC_ConsDeb = vigenciaCuentaSeleccionada.getNombreConsolidadord();
         auxVC_DescCre = vigenciaCuentaSeleccionada.getDescripcionCuentac();
         auxVC_Credito = vigenciaCuentaSeleccionada.getCodCuentac();
         auxVC_ConsCre = vigenciaCuentaSeleccionada.getNombreConsolidadorc();
         auxVC_Proceso = vigenciaCuentaSeleccionada.getNombreProceso();

         vigenciaConceptoTCSeleccionada = null;
         vigenciaConceptoTTSeleccionada = null;
         vigenciaGrupoCoSeleccionada = null;
         vigenciaConceptoRLSeleccionada = null;
         vigFormulaConceptoSeleccionada = null;
         formulaSeleccionada = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
//            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      }
   }

   public void cambiarIndiceVigenciaGrupoConcepto(VigenciasGruposConceptos grupoConcepto, int celda) {
      if (permitirIndexVigenciaGrupoConcepto == true) {
         cualCeldaVigenciaGrupoConcepto = celda;
         if (celda == 2 || celda == 3) {
            activarBotonLOV();
         } else {
            anularBotonLOV();
         }
         vigenciaGrupoCoSeleccionada = grupoConcepto;
         ///////// Captura Objetos Para campos NotNull ///////////
         auxVGC_FechaIni = vigenciaGrupoCoSeleccionada.getFechainicial();
         auxVGC_FechaFin = vigenciaGrupoCoSeleccionada.getFechafinal();
         auxVGC_Codigo = vigenciaGrupoCoSeleccionada.getGrupoconcepto().getStrCodigo();
         auxVGC_Descripcion = vigenciaGrupoCoSeleccionada.getGrupoconcepto().getDescripcion();

         vigenciaConceptoTCSeleccionada = null;
         vigenciaConceptoTTSeleccionada = null;
         vigenciaCuentaSeleccionada = null;
         vigenciaConceptoRLSeleccionada = null;
         vigFormulaConceptoSeleccionada = null;
         formulaSeleccionada = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
//            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      }
   }

   public void cambiarIndiceVigenciaConceptoTT(VigenciasConceptosTT conceptoTT, int celda) {
      log.info("Ya entro en cambiarIndiceVigenciaConceptoTT() permitirIndexVigenciaConceptoTT : " + permitirIndexVigenciaConceptoTT);
      if (permitirIndexVigenciaConceptoTT == true) {
         cualCeldaVigenciaConceptoTT = celda;
         if (celda == 2) {
            activarBotonLOV();
         } else {
            anularBotonLOV();
         }
         vigenciaConceptoTTSeleccionada = conceptoTT;
         ///////// Captura Objetos Para campos NotNull ///////////
         auxVCTT_FechaIni = vigenciaConceptoTTSeleccionada.getFechainicial();
         auxVCTT_FechaFin = vigenciaConceptoTTSeleccionada.getFechafinal();
         auxVCTT_Descripcion = vigenciaConceptoTTSeleccionada.getTipotrabajador().getNombre();

         vigenciaConceptoTCSeleccionada = null;
         vigenciaGrupoCoSeleccionada = null;
         vigenciaCuentaSeleccionada = null;
         vigenciaConceptoRLSeleccionada = null;
         vigFormulaConceptoSeleccionada = null;
         formulaSeleccionada = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
//            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      }
   }

   public void cambiarIndiceVigenciaConceptoTC(VigenciasConceptosTC conceptoTC, int celda) {
      if (permitirIndexVigenciaConceptoTC == true) {
         cualCeldaVigenciaConceptoTC = celda;
         if (celda == 2) {
            activarBotonLOV();
         } else {
            anularBotonLOV();
         }
         vigenciaConceptoTCSeleccionada = conceptoTC;
         ///////// Captura Objetos Para campos NotNull ///////////
         auxVCTC_FechaIni = vigenciaConceptoTCSeleccionada.getFechainicial();
         auxVCTC_FechaFin = vigenciaConceptoTCSeleccionada.getFechafinal();
         auxVCTC_Descripcion = vigenciaConceptoTCSeleccionada.getTipocontrato().getNombre();

         vigenciaConceptoTTSeleccionada = null;
         vigenciaGrupoCoSeleccionada = null;
         vigenciaCuentaSeleccionada = null;
         vigenciaConceptoRLSeleccionada = null;
         vigFormulaConceptoSeleccionada = null;
         formulaSeleccionada = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
//            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      }
   }

   public void cambiarIndiceVigenciaConceptoRL(VigenciasConceptosRL conceptoRL, int celda) {
      if (permitirIndexVigenciaConceptoRL == true) {
         cualCeldaVigenciaConceptoRL = celda;
         if (celda == 2) {
            activarBotonLOV();
         } else {
            anularBotonLOV();
         }
         vigenciaConceptoRLSeleccionada = conceptoRL;
         ///////// Captura Objetos Para campos NotNull ///////////
         auxVCRL_FechaIni = vigenciaConceptoRLSeleccionada.getFechainicial();
         auxVCRL_FechaFin = vigenciaConceptoRLSeleccionada.getFechafinal();
         auxVCRL_Descripcion = vigenciaConceptoRLSeleccionada.getTiposalario().getNombre();

         vigenciaConceptoTTSeleccionada = null;
         vigenciaGrupoCoSeleccionada = null;
         vigenciaCuentaSeleccionada = null;
         vigenciaConceptoTCSeleccionada = null;
         vigFormulaConceptoSeleccionada = null;
         formulaSeleccionada = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
//            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      }
   }

   public void cambiarIndiceFormulasConceptos(FormulasConceptos formulaC, int celda) {
      if (permitirIndexFormulasConceptos == true) {
         cualCeldaFormulasConceptos = celda;
         if (celda == 2 || celda == 3) {
            activarBotonLOV();
         } else {
            anularBotonLOV();
         }
         vigFormulaConceptoSeleccionada = formulaC;
         actualFormulaConcepto = vigFormulaConceptoSeleccionada;
         ///////// Captura Objetos Para campos NotNull ///////////
         auxFC_FechaIni = vigFormulaConceptoSeleccionada.getFechainicial();
         auxFC_FechaFin = vigFormulaConceptoSeleccionada.getFechafinal();
         auxFC_Descripcion = vigFormulaConceptoSeleccionada.getNombreFormula();
         auxFC_Orden = vigFormulaConceptoSeleccionada.getStrOrden();

         vigenciaConceptoTTSeleccionada = null;
         vigenciaConceptoRLSeleccionada = null;
         vigenciaGrupoCoSeleccionada = null;
         vigenciaCuentaSeleccionada = null;
         vigenciaConceptoTCSeleccionada = null;
         formulaSeleccionada = false;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
//            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
      }
   }

   public boolean validarFechasRegistroVigCuenta() {
      boolean retorno = false;
      VigenciasCuentas auxiliar = vigenciaCuentaSeleccionada;
      if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
         retorno = true;
      }
      return retorno;
   }

   public boolean validarFechasNuevoVigCuenta() {
      log.info("ControlDetalleConcepto.validarFechasNuevoVigCuenta()");
      boolean retorno = true;
      for (VigenciasCuentas nuevaVigenciaCuenta : listVigCParaCrear) {
         if (nuevaVigenciaCuenta.getAcrear()
                 && (!nuevaVigenciaCuenta.getFechainicial().after(fechaParametro)
                 || (!nuevaVigenciaCuenta.getFechainicial().before(nuevaVigenciaCuenta.getFechafinal())))) {
            retorno = false;
            break;
         }
      }
      log.info("validarFechasNuevoVigCuenta() Va a retornar: " + retorno);
      return retorno;
   }

   public boolean validarFechasRegistroVigenciaGrupoConcepto(int i) {
      boolean retorno = false;
      if (i == 0) {
         VigenciasGruposConceptos auxiliar = vigenciaGrupoCoSeleccionada;
         if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 1) {
         if (nuevaVigenciaGrupoConcepto.getFechainicial().after(fechaParametro) && (nuevaVigenciaGrupoConcepto.getFechainicial().before(nuevaVigenciaGrupoConcepto.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarVigenciaGrupoConcepto.getFechainicial().after(fechaParametro) && (duplicarVigenciaGrupoConcepto.getFechainicial().before(duplicarVigenciaGrupoConcepto.getFechafinal()))) {
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarFechasRegistroVigenciaConceptoTT(int i) {
      boolean retorno = false;
      if (i == 0) {
         VigenciasConceptosTT auxiliar = vigenciaConceptoTTSeleccionada;
         if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 1) {
         if (nuevaVigenciaConceptoTT.getFechainicial().after(fechaParametro) && (nuevaVigenciaConceptoTT.getFechainicial().before(nuevaVigenciaConceptoTT.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarVigenciaConceptoTT.getFechainicial().after(fechaParametro) && (duplicarVigenciaConceptoTT.getFechainicial().before(duplicarVigenciaConceptoTT.getFechafinal()))) {
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarFechasRegistroVigenciaConceptoTC(int i) {
      boolean retorno = false;
      if (i == 0) {
         VigenciasConceptosTC auxiliar = vigenciaConceptoTCSeleccionada;
         if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 1) {
         if (nuevaVigenciaConceptoTC.getFechainicial().after(fechaParametro) && (nuevaVigenciaConceptoTC.getFechainicial().before(nuevaVigenciaConceptoTC.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarVigenciaConceptoTC.getFechainicial().after(fechaParametro) && (duplicarVigenciaConceptoTC.getFechainicial().before(duplicarVigenciaConceptoTC.getFechafinal()))) {
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarFechasRegistroVigenciaConceptoRL(int i) {
      boolean retorno = false;
      if (i == 0) {
         VigenciasConceptosRL auxiliar = vigenciaConceptoRLSeleccionada;
         if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 1) {
         if (nuevaVigenciaConceptoRL.getFechainicial().after(fechaParametro) && (nuevaVigenciaConceptoRL.getFechainicial().before(nuevaVigenciaConceptoRL.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarVigenciaConceptoRL.getFechainicial().after(fechaParametro) && (duplicarVigenciaConceptoRL.getFechainicial().before(duplicarVigenciaConceptoRL.getFechafinal()))) {
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarFechasRegistroFormulasConceptos(int i) {
      boolean retorno = false;
      if (i == 0) {
         FormulasConceptos auxiliar = vigFormulaConceptoSeleccionada;
         if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 1) {
         if (nuevaFormulasConceptos.getFechainicial().after(fechaParametro) && (nuevaFormulasConceptos.getFechainicial().before(nuevaFormulasConceptos.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarFormulasConceptos.getFechainicial().after(fechaParametro) && (duplicarFormulasConceptos.getFechainicial().before(duplicarFormulasConceptos.getFechafinal()))) {
            retorno = true;
         }
      }
      return retorno;
   }

   public void modificacionesFechaVigenciaCuenta(VigenciasCuentas cuenta, int c) {
      VigenciasCuentas auxiliar = null;
      auxiliar = cuenta;
      vigenciaCuentaSeleccionada = cuenta;

      if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
         boolean validacion = validarFechasRegistroVigCuenta();
         if (validacion == true) {
            cambiarIndiceVigenciaCuenta(cuenta, c);
            modificarVigenciaCuenta();
            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
         } else {
            vigenciaCuentaSeleccionada.setFechainicial(auxVC_FechaIni);
            vigenciaCuentaSeleccionada.setFechafinal(auxVC_FechaFin);
            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
//                vigenciaCuentaSeleccionada = null;
         }
      } else {
         vigenciaCuentaSeleccionada.setFechainicial(auxVC_FechaIni);
         vigenciaCuentaSeleccionada.setFechafinal(auxVC_FechaFin);
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
//            vigenciaCuentaSeleccionada = null;
      }
   }

   public void modificacionesFechaVigenciaGrupoConcepto(VigenciasGruposConceptos grupoC, int c) {
      VigenciasGruposConceptos auxiliar = null;
      auxiliar = vigenciaGrupoCoSeleccionada;
      vigenciaGrupoCoSeleccionada = grupoC;
      if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
         boolean validacion = validarFechasRegistroVigenciaGrupoConcepto(0);
         if (validacion == true) {
            cambiarIndiceVigenciaGrupoConcepto(grupoC, c);
            modificarVigenciaGrupoConcepto();
         } else {
            vigenciaGrupoCoSeleccionada.setFechainicial(auxVGC_FechaIni);
            vigenciaGrupoCoSeleccionada.setFechafinal(auxVGC_FechaFin);
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
//                vigenciaGrupoCoSeleccionada = null;
         }
      } else {
         vigenciaGrupoCoSeleccionada.setFechainicial(auxVGC_FechaIni);
         vigenciaGrupoCoSeleccionada.setFechafinal(auxVGC_FechaFin);

         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
//            vigenciaGrupoCoSeleccionada = null;
      }
   }

   public void modificacionesFechaVigenciaConceptoTT(VigenciasConceptosTT conceptoTT, int c) {
      RequestContext context = RequestContext.getCurrentInstance();
      VigenciasConceptosTT auxiliar = null;
      auxiliar = conceptoTT;
      vigenciaConceptoTTSeleccionada = conceptoTT;

      if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
         boolean validacion = validarFechasRegistroVigenciaConceptoTT(0);
         if (validacion == true) {
            cambiarIndiceVigenciaConceptoTT(conceptoTT, c);
            modificarVigenciaConceptoTT();
         } else {
            vigenciaConceptoTTSeleccionada.setFechainicial(auxVCTT_FechaIni);
            vigenciaConceptoTTSeleccionada.setFechafinal(auxVCTT_FechaFin);
            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
//                vigenciaConceptoTTSeleccionada = null;
         }
      } else {
         vigenciaConceptoTTSeleccionada.setFechainicial(auxVCTT_FechaIni);
         vigenciaConceptoTTSeleccionada.setFechafinal(auxVCTT_FechaFin);
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
//            vigenciaConceptoTTSeleccionada = null;
      }
   }

   public void modificacionesFechaVigenciaConceptoTC(VigenciasConceptosTC conceptoTC, int c) {
      VigenciasConceptosTC auxiliar = null;
      auxiliar = conceptoTC;
      vigenciaConceptoTCSeleccionada = conceptoTC;

      if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
         RequestContext context = RequestContext.getCurrentInstance();
         boolean validacion = validarFechasRegistroVigenciaConceptoTC(0);
         if (validacion == true) {
            cambiarIndiceVigenciaConceptoTC(conceptoTC, c);
            modificarVigenciaConceptoTC();
         } else {
            vigenciaConceptoTCSeleccionada.setFechainicial(auxVCTC_FechaIni);
            vigenciaConceptoTCSeleccionada.setFechafinal(auxVCTC_FechaFin);
            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
//                vigenciaConceptoTCSeleccionada = null;
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         vigenciaConceptoTCSeleccionada.setFechainicial(auxVCTC_FechaIni);
         vigenciaConceptoTCSeleccionada.setFechafinal(auxVCTC_FechaFin);
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
//            vigenciaConceptoTCSeleccionada = null;
      }
   }

   public void modificacionesFechaVigenciaConceptoRL(VigenciasConceptosRL conceptoRL, int c) {
      RequestContext context = RequestContext.getCurrentInstance();
      VigenciasConceptosRL auxiliar = null;
      auxiliar = conceptoRL;
      vigenciaConceptoRLSeleccionada = conceptoRL;

      if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
         boolean validacion = validarFechasRegistroVigenciaConceptoRL(0);
         if (validacion == true) {
            cambiarIndiceVigenciaConceptoRL(conceptoRL, c);
            modificarVigenciaConceptoRL();
         } else {
            vigenciaConceptoRLSeleccionada.setFechainicial(auxVCRL_FechaIni);
            vigenciaConceptoRLSeleccionada.setFechafinal(auxVCRL_FechaFin);
            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
//                vigenciaConceptoRLSeleccionada = null;
         }
      } else {
         vigenciaConceptoRLSeleccionada.setFechainicial(auxVCRL_FechaIni);
         vigenciaConceptoRLSeleccionada.setFechafinal(auxVCRL_FechaFin);
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
//            vigenciaConceptoRLSeleccionada = null;
      }
   }

   public void modificacionesFechaFormulasConceptos(FormulasConceptos formulaC, int c) {
      RequestContext context = RequestContext.getCurrentInstance();
      FormulasConceptos auxiliar = null;
      auxiliar = formulaC;
      vigFormulaConceptoSeleccionada = formulaC;

      if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
         boolean validacion = validarFechasRegistroFormulasConceptos(0);
         if (validacion == true) {
            cambiarIndiceFormulasConceptos(formulaC, c);
            modificarFormulasConceptos();
         } else {
            vigFormulaConceptoSeleccionada.setFechainicial(auxFC_FechaIni);
            vigFormulaConceptoSeleccionada.setFechafinal(auxFC_FechaFin);
            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
//                formulaConceptoSeleccionada = null;
         }
      } else {
         vigFormulaConceptoSeleccionada.setFechainicial(auxFC_FechaIni);
         vigFormulaConceptoSeleccionada.setFechafinal(auxFC_FechaFin);
         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
//            formulaConceptoSeleccionada = null;
      }
   }

   public void guardarYSalir() {
      guardadoGeneral();
      salir();
   }

   //GUARDAR
   /**
    */
   public void guardadoGeneral() {
      if (cambiosVigenciaCuenta == true) {
         guardarCambiosVigenciaCuenta();
      }
      if (cambiosVigenciaGrupoConcepto == true) {
         guardarCambiosVigenciaGrupoConcepto();
      }
      if (cambiosVigenciaConceptoTT == true) {
         guardarCambiosVigenciaConceptoTT();
      }
      if (cambiosVigenciaConceptoTC == true) {
         guardarCambiosVigenciaConceptoTC();
      }
      if (cambiosVigenciaConceptoRL == true) {
         guardarCambiosVigenciaConceptoRL();
      }
      if (cambiosFormulasConceptos == true) {
         guardarCambiosFormulasConceptos();
      }
   }

   public void guardarCambiosVigenciaCuenta() {
      try {
         if (!listVigenciasCuentasBorrar.isEmpty()) {
            administrarDetalleConcepto.borrarVigenciasCuentas(listVigenciasCuentasBorrar);
            listVigenciasCuentasBorrar.clear();
         }
         if (!listVigenciasCuentasCrear.isEmpty()) {
            administrarDetalleConcepto.crearVigenciasCuentas(listVigenciasCuentasCrear);
            listVigenciasCuentasCrear.clear();
         }
         if (!listVigenciasCuentasModificar.isEmpty()) {
            administrarDetalleConcepto.modificarVigenciasCuentas(listVigenciasCuentasModificar);
            listVigenciasCuentasModificar.clear();
         }
         listVigenciasCuentasConcepto = null;
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
         k = 0;
//         vigenciaCuentaSeleccionada = null;
         cambiosVigenciaCuenta = false;
         FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron de Cuentas y Tipos CC con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         log.warn("Error guardarCambiosVigenciaCuenta : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Cuentas y Tipos CC, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosVigenciaGrupoConcepto() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (!listVigenciasGruposConceptosBorrar.isEmpty()) {
            administrarDetalleConcepto.borrarVigenciasGruposConceptos(listVigenciasGruposConceptosBorrar);
            listVigenciasGruposConceptosBorrar.clear();
         }
         if (!listVigenciasGruposConceptosCrear.isEmpty()) {
            administrarDetalleConcepto.crearVigenciasGruposConceptos(listVigenciasGruposConceptosCrear);
            listVigenciasGruposConceptosCrear.clear();
         }
         if (!listVigenciasGruposConceptosModificar.isEmpty()) {
            administrarDetalleConcepto.modificarVigenciasGruposConceptos(listVigenciasGruposConceptosModificar);
            listVigenciasGruposConceptosModificar.clear();
         }
         listVigenciasCuentasConcepto = null;
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
         k = 0;
//         vigenciaGrupoCoSeleccionada = null;
         cambiosVigenciaGrupoConcepto = false;
         FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron de Grupos C/N/G con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         log.warn("Error guardarCambiosVigenciaGrupoConcepto : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Grupos C/N/G, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosVigenciaConceptoTT() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (!listVigenciasConceptosTTBorrar.isEmpty()) {
            administrarDetalleConcepto.borrarVigenciasConceptosTT(listVigenciasConceptosTTBorrar);
            listVigenciasConceptosTTBorrar.clear();
         }
         if (!listVigenciasConceptosTTCrear.isEmpty()) {
            administrarDetalleConcepto.crearVigenciasConceptosTT(listVigenciasConceptosTTCrear);
            listVigenciasConceptosTTCrear.clear();
         }
         if (!listVigenciasConceptosTTModificar.isEmpty()) {
            administrarDetalleConcepto.modificarVigenciasConceptosTT(listVigenciasConceptosTTModificar);
            listVigenciasConceptosTTModificar.clear();
         }
         listVigenciasConceptosTTConcepto = null;
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
         k = 0;
//         vigenciaConceptoTTSeleccionada = null;
         cambiosVigenciaConceptoTT = false;
         FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron de Tipo Trabajador con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         log.warn("Error guardarCambiosVigenciaConceptoTT : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Tipo Trabajador, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosVigenciaConceptoTC() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (!listVigenciasConceptosTCBorrar.isEmpty()) {
            administrarDetalleConcepto.borrarVigenciasConceptosTC(listVigenciasConceptosTCBorrar);
            listVigenciasConceptosTCBorrar.clear();
         }
         if (!listVigenciasConceptosTCCrear.isEmpty()) {
            administrarDetalleConcepto.crearVigenciasConceptosTC(listVigenciasConceptosTCCrear);
            listVigenciasConceptosTCCrear.clear();
         }
         if (!listVigenciasConceptosTCModificar.isEmpty()) {
            administrarDetalleConcepto.modificarVigenciasConceptosTC(listVigenciasConceptosTCModificar);
            listVigenciasConceptosTCModificar.clear();
         }
         listVigenciasConceptosTCConcepto = null;
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
         k = 0;
//         vigenciaConceptoTCSeleccionada = null;
         cambiosVigenciaConceptoTC = false;
         FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron de Tipo Contrato con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         log.warn("Error guardarCambiosVigenciaConceptoTC : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Tipo Contrato, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosVigenciaConceptoRL() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (!listVigenciasConceptosRLBorrar.isEmpty()) {
            administrarDetalleConcepto.borrarVigenciasConceptosRL(listVigenciasConceptosRLBorrar);
            listVigenciasConceptosRLBorrar.clear();
         }
         if (!listVigenciasConceptosRLCrear.isEmpty()) {
            administrarDetalleConcepto.crearVigenciasConceptosRL(listVigenciasConceptosRLCrear);
            listVigenciasConceptosRLCrear.clear();
         }
         if (!listVigenciasConceptosRLModificar.isEmpty()) {
            administrarDetalleConcepto.modificarVigenciasConceptosRL(listVigenciasConceptosRLModificar);
            listVigenciasConceptosRLModificar.clear();
         }
         listVigenciasConceptosRLConcepto = null;
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
         k = 0;
//         vigenciaConceptoRLSeleccionada = null;
         cambiosVigenciaConceptoRL = false;
         FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron de Tipo Salario con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         log.warn("Error guardarCambiosVigenciaConceptoRL : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Tipo Salario, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosFormulasConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (!listFormulasConceptosBorrar.isEmpty()) {
            administrarDetalleConcepto.borrarFormulasConceptos(listFormulasConceptosBorrar);
            listFormulasConceptosBorrar.clear();
         }
         if (!listFormulasConceptosCrear.isEmpty()) {
            administrarDetalleConcepto.crearFormulasConceptos(listFormulasConceptosCrear);
            listFormulasConceptosCrear.clear();
         }
         if (!listFormulasConceptosModificar.isEmpty()) {
            administrarDetalleConcepto.modificarFormulasConceptos(listFormulasConceptosModificar);
            listFormulasConceptosModificar.clear();
         }
         listFormulasConceptos = null;
         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         k = 0;
//         vigFormulaConceptoSeleccionada = null;
         cambiosFormulasConceptos = false;
         FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron de Formula con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         log.warn("Error guardarCambiosFormulasConceptos : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Formula, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }
   //CANCELAR MODIFICACIONES

   public void cancelarYSalir() {
//      cancelarModificacion();
      salir();
   }

   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacion() {
      vigCuentaCrearSeleccionada = null;
      vigenciaCuentaSeleccionada = null;
      vigFormulaConceptoSeleccionada = null;
      vigenciaGrupoCoSeleccionada = null;
      vigenciaConceptoRLSeleccionada = null;
      vigenciaConceptoTTSeleccionada = null;
      vigenciaConceptoTCSeleccionada = null;
      if (banderaVigenciaCuenta == 1) {
         recargarVigenciaCuentaDefault();
      }
      if (banderaVigenciaGrupoConcepto == 1) {
         recargarVigenciaGrupoCDefault();
      }
      if (banderaVigenciaConceptoTT == 1) {
         recargarVigenciaConceptoTT();
      }
      if (banderaVigenciaConceptoTC == 1) {
         recargarVigenciaConceptoTC();
      }
      if (banderaVigenciaConceptoRL == 1) {
         recargarVigenciaConceptoRT();
      }
      if (banderaFormulasConceptos == 1) {
         recargarFormulaConcepto();
      }
      listVigenciasCuentasBorrar.clear();
      listVigenciasCuentasCrear.clear();
      listVigenciasCuentasModificar.clear();

      listVigenciasGruposConceptosBorrar.clear();
      listVigenciasGruposConceptosCrear.clear();
      listVigenciasGruposConceptosModificar.clear();

      listVigenciasConceptosTTBorrar.clear();
      listVigenciasConceptosTTCrear.clear();
      listVigenciasConceptosTTModificar.clear();

      listVigenciasConceptosTCBorrar.clear();
      listVigenciasConceptosTCCrear.clear();
      listVigenciasConceptosTCModificar.clear();

      listVigenciasConceptosRLBorrar.clear();
      listVigenciasConceptosRLCrear.clear();
      listVigenciasConceptosRLModificar.clear();

      listFormulasConceptosBorrar.clear();
      listFormulasConceptosCrear.clear();
      listFormulasConceptosModificar.clear();

      vigenciaCuentaSeleccionada = null;
      vigenciaConceptoTTSeleccionada = null;
      vigenciaConceptoRLSeleccionada = null;
      vigenciaConceptoTCSeleccionada = null;
      vigenciaGrupoCoSeleccionada = null;
      vigFormulaConceptoSeleccionada = null;
      k = 0;
      listVigenciasCuentasConcepto = null;
      listVigenciasGruposConceptos = null;
      listVigenciasConceptosTTConcepto = null;
      listVigenciasConceptosTCConcepto = null;
      listVigenciasConceptosRLConcepto = null;
      listFormulasConceptos = null;

      guardado = true;
      formulaSeleccionada = true;

      cambiosVigenciaCuenta = false;
      cambiosVigenciaConceptoTT = false;
      cambiosVigenciaConceptoRL = false;
      cambiosVigenciaConceptoTC = false;
      cambiosVigenciaGrupoConcepto = false;
      cambiosFormulasConceptos = false;

      permitirIndexVigenciaCuenta = true;
      permitirIndexVigenciaConceptoTT = true;
      permitirIndexVigenciaConceptoTC = true;
      permitirIndexVigenciaConceptoRL = true;
      permitirIndexVigenciaGrupoConcepto = true;
      permitirIndexFormulasConceptos = true;

      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:datosVigenciaCuenta");
      context.update("form:datosVigenciaGrupoConcepto");
      context.update("form:datosVigenciaConceptoTT");
      context.update("form:datosVigenciaConceptoTC");
      context.update("form:datosVigenciaConceptoRL");
      context.update("form:datosFormulaConcepto");
      context.update("form:detalleFormula");
   }

   public void listaValoresBoton() {
      if (vigenciaCuentaSeleccionada != null) {
         cargarLOVsVCuentas();
         if (cualCeldaVigenciaCuenta == 2) {
            contarRegistrosLovTipoCentroCosto();
            RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaVigenciaCuenta == 3) {
            contarRegistrosLovCuentaDebito();
            RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaVigenciaCuenta == 4) {
            contarRegistrosLovCuentaDebito();
            RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaVigenciaCuenta == 5) {
            contarRegistrosLovCentroCostoDebito();
            RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
            RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaVigenciaCuenta == 6) {
            contarRegistrosLovCuentaCredito();
            RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
            RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaVigenciaCuenta == 7) {
            contarRegistrosLovCuentaCredito();
            RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
            RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaVigenciaCuenta == 8) {
            contarRegistrosLovCentroCostoCredito();
            RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
            RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaVigenciaCuenta == 9) {
            contarRegistrosLovProcesos();
            RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (vigenciaGrupoCoSeleccionada != null) {
         cargarLOVsVGrupoConcepto();
         if (cualCeldaVigenciaGrupoConcepto == 2) {
            contarRegistrosLovGrupoConcepto();
            RequestContext.getCurrentInstance().update("formlovGrupoConcepto:GruposConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaVigenciaGrupoConcepto == 3) {
            contarRegistrosLovGrupoConcepto();
            RequestContext.getCurrentInstance().update("formlovGrupoConcepto:GruposConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (vigenciaConceptoTTSeleccionada != null) {
         cargarLOVsVConceptosTT();
         if (cualCeldaVigenciaConceptoTT == 2) {
            contarRegistrosLovTipoTrabajador();
            RequestContext.getCurrentInstance().update("formlovTipoTrabajador:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (vigenciaConceptoTCSeleccionada != null) {
         cargarLOVsVConceptosTC();
         if (cualCeldaVigenciaConceptoTC == 2) {
            contarRegistrosLovTipoContrato();
            RequestContext.getCurrentInstance().update("formlovTipoContrato:TipoContratosDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoContratosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (vigenciaConceptoRLSeleccionada != null) {
         cargarLOVsVConceptosRL();
         if (cualCeldaVigenciaConceptoRL == 2) {
            contarRegistrosLovReformaLaboral();
            RequestContext.getCurrentInstance().update("formlovReformaLaboral:ReformaLaboralDialogo");
            RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (vigFormulaConceptoSeleccionada != null) {
         cargarLOVsFormulasConceptos();
         if (cualCeldaFormulasConceptos == 2) {
            contarRegistrosLovFormula();
            RequestContext.getCurrentInstance().update("formlovFormula:FormulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaFormulasConceptos == 3) {
            contarRegistrosLovFormCon();
            RequestContext.getCurrentInstance().update("formlovFormulaC:FormulaConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndex(int campo, int tipoAct, int tabla) {
      tipoActualizacion = tipoAct;
      if (tabla == 0) {
         cargarLOVsVCuentas();
         if (campo == 0) {
            contarRegistrosLovTipoCentroCosto();
            RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').show()");
         } else if (campo == 1) {
            contarRegistrosLovCuentaDebito();
            RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
         } else if (campo == 2) {
            contarRegistrosLovCuentaDebito();
            RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
         } else if (campo == 3) {
            contarRegistrosLovCentroCostoDebito();
            RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
            RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
         } else if (campo == 4) {
            contarRegistrosLovCuentaCredito();
            RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
            RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
         } else if (campo == 5) {
            contarRegistrosLovCuentaCredito();
            RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
            RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
         } else if (campo == 6) {
            contarRegistrosLovCentroCostoCredito();
            RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
            RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').show()");
         } else if (campo == 9) {
            log.info("asignarIndex (campo == 9)");
            contarRegistrosLovProcesos();
            RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
            RequestContext.getCurrentInstance().update("formlovProceso:lovProceso");
            RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
         }
      }
      if (tabla == 1) {
         cargarLOVsVGrupoConcepto();
         if (campo == 0) {
            contarRegistrosLovGrupoConcepto();
            RequestContext.getCurrentInstance().update("formlovGrupoConcepto:GruposConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
         } else if (campo == 1) {
            contarRegistrosLovGrupoConcepto();
            RequestContext.getCurrentInstance().update("formlovGrupoConcepto:GruposConceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').show()");
         }
      }
      if (tabla == 2) {
         cargarLOVsVConceptosTT();
         if (campo == 0) {
            contarRegistrosLovTipoTrabajador();
            RequestContext.getCurrentInstance().update("formlovTipoTrabajador:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
         }
      }
      if (tabla == 3) {
         cargarLOVsVConceptosTC();
         if (campo == 0) {
            contarRegistrosLovTipoContrato();
            RequestContext.getCurrentInstance().update("formlovTipoContrato:TipoContratosDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoContratosDialogo').show()");
         }
      }
      if (tabla == 4) {
         cargarLOVsVConceptosRL();
         if (campo == 0) {
            contarRegistrosLovReformaLaboral();
            RequestContext.getCurrentInstance().update("formlovReformaLaboral:ReformaLaboralDialogo");
            RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').show()");
         }
      }
      if (tabla == 5) {
         cargarLOVsFormulasConceptos();
         formulaSeleccionada = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
         if (campo == 0) {
            contarRegistrosLovFormula();
            RequestContext.getCurrentInstance().update("formlovFormula:FormulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').show()");
         }
         if (campo == 1) {
            contarRegistrosLovFormCon();
            RequestContext.getCurrentInstance().update("formlovFormulaC:FormulaConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaConceptoDialogo').show()");
         }
      }
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      ///////////VigenciaCuenta/////////////
      if (vigenciaCuentaSeleccionada != null || vigenciaConceptoRLSeleccionada != null || vigenciaConceptoTCSeleccionada != null
              || vigenciaConceptoTTSeleccionada != null || vigFormulaConceptoSeleccionada != null || vigenciaGrupoCoSeleccionada != null) {

         if (vigenciaCuentaSeleccionada != null) {
            editarVigenciaCuenta = vigenciaCuentaSeleccionada;

            if (cualCeldaVigenciaCuenta == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialVCD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialVCD').show()");
               cualCeldaVigenciaCuenta = -1;
            } else if (cualCeldaVigenciaCuenta == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalVCD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalVCD').show()");
               cualCeldaVigenciaCuenta = -1;
            } else if (cualCeldaVigenciaCuenta == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoCCVCD");
               RequestContext.getCurrentInstance().execute("PF('editarTipoCCVCD').show()");
               cualCeldaVigenciaCuenta = -1;
            } else if (cualCeldaVigenciaCuenta == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaCodDebitoVCD");
               RequestContext.getCurrentInstance().execute("PF('editaCodDebitoVCD').show()");
               cualCeldaVigenciaCuenta = -1;
            } else if (cualCeldaVigenciaCuenta == 4) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaDesDebitoVCD");
               RequestContext.getCurrentInstance().execute("PF('editaDesDebitoVCD').show()");
               cualCeldaVigenciaCuenta = -1;
            } else if (cualCeldaVigenciaCuenta == 5) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaCCConsolidadorDVCD");
               RequestContext.getCurrentInstance().execute("PF('editaCCConsolidadorDVCD').show()");
               cualCeldaVigenciaCuenta = -1;
            } else if (cualCeldaVigenciaCuenta == 6) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaCodCreditoVCD");
               RequestContext.getCurrentInstance().execute("PF('editaCodCreditoVCD').show()");
               cualCeldaVigenciaCuenta = -1;
            } else if (cualCeldaVigenciaCuenta == 7) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaDesCreditoVCD");
               RequestContext.getCurrentInstance().execute("PF('editaDesCreditoVCD').show()");
               cualCeldaVigenciaCuenta = -1;
            } else if (cualCeldaVigenciaCuenta == 8) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaCCConsolidadorCVCD");
               RequestContext.getCurrentInstance().execute("PF('editaCCConsolidadorCVCD').show()");
               cualCeldaVigenciaCuenta = -1;
            }
            vigenciaCuentaSeleccionada = null;
         }
         ///////////VigenciaCuenta/////////////
         ///////////VigenciaGrupoConcepto/////////////
         if (vigenciaGrupoCoSeleccionada != null) {
            editarVigenciaGrupoConcepto = vigenciaGrupoCoSeleccionada;

            if (cualCeldaVigenciaGrupoConcepto == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialVGCD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialVGCD').show()");
               cualCeldaVigenciaGrupoConcepto = -1;
            } else if (cualCeldaVigenciaGrupoConcepto == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalVGCD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalVGCD').show()");
               cualCeldaVigenciaGrupoConcepto = -1;
            } else if (cualCeldaVigenciaGrupoConcepto == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoVGCD");
               RequestContext.getCurrentInstance().execute("PF('editarCodigoVGCD').show()");
               cualCeldaVigenciaGrupoConcepto = -1;
            } else if (cualCeldaVigenciaGrupoConcepto == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaDescripcionVGCD");
               RequestContext.getCurrentInstance().execute("PF('editaDescripcionVGCD').show()");
               cualCeldaVigenciaGrupoConcepto = -1;
            }
            vigenciaGrupoCoSeleccionada = null;
         }
         ///////////VigenciaGrupoConcepto/////////////
         ///////////VigenciaConceptoTT/////////////
         if (vigenciaConceptoTTSeleccionada != null) {
            editarVigenciaConceptoTT = vigenciaConceptoTTSeleccionada;

            if (cualCeldaVigenciaConceptoTT == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialVCTCD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialVCTCD').show()");
               cualCeldaVigenciaConceptoTT = -1;
            } else if (cualCeldaVigenciaConceptoTT == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalVCTCD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalVCTCD').show()");
               cualCeldaVigenciaConceptoTT = -1;
            } else if (cualCeldaVigenciaConceptoTT == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaDescripcionVCTCD");
               RequestContext.getCurrentInstance().execute("PF('editaDescripcionVCTCD').show()");
               cualCeldaVigenciaConceptoTT = -1;
            }
            vigenciaConceptoTTSeleccionada = null;
         }
         ///////////VigenciaConceptoTT/////////////
         ///////////VigenciaConceptoTT/////////////
         if (vigenciaConceptoTCSeleccionada != null) {
            editarVigenciaConceptoTC = vigenciaConceptoTCSeleccionada;

            if (cualCeldaVigenciaConceptoTC == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialVCTTD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialVCTTD').show()");
               cualCeldaVigenciaConceptoTC = -1;
            } else if (cualCeldaVigenciaConceptoTC == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalVCTTD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalVCTTD').show()");
               cualCeldaVigenciaConceptoTC = -1;
            } else if (cualCeldaVigenciaConceptoTC == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaDescripcionVCTTD");
               RequestContext.getCurrentInstance().execute("PF('editaDescripcionVCTTD').show()");
               cualCeldaVigenciaConceptoTC = -1;
            }
            vigenciaConceptoTCSeleccionada = null;
            vigenciaConceptoTCSeleccionada = null;
         }
         ///////////VigenciaConceptoTC/////////////
         ///////////VigenciaConceptoRL/////////////
         if (vigenciaConceptoRLSeleccionada != null) {
            editarVigenciaConceptoRL = vigenciaConceptoRLSeleccionada;

            if (cualCeldaVigenciaConceptoRL == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialVCRLD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialVCRLD').show()");
               cualCeldaVigenciaConceptoRL = -1;
            } else if (cualCeldaVigenciaConceptoRL == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalVCRLD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalVCRLD').show()");
               cualCeldaVigenciaConceptoRL = -1;
            } else if (cualCeldaVigenciaConceptoRL == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaDescripcionVCRLD");
               RequestContext.getCurrentInstance().execute("PF('editaDescripcionVCRLD').show()");
               cualCeldaVigenciaConceptoRL = -1;
            }
         }
         ///////////VigenciaConceptoRL/////////////
         ///////////FormulasConceptos/////////////
         if (vigFormulaConceptoSeleccionada != null) {
            editarFormulasConceptos = vigFormulaConceptoSeleccionada;

            if (cualCeldaFormulasConceptos == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialFCD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaInicialFCD').show()");
               cualCeldaFormulasConceptos = -1;
            } else if (cualCeldaFormulasConceptos == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalFCD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaFinalFCD').show()");
               cualCeldaFormulasConceptos = -1;
            } else if (cualCeldaFormulasConceptos == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaFormulaFCD");
               RequestContext.getCurrentInstance().execute("PF('editaFormulaFCD').show()");
               cualCeldaFormulasConceptos = -1;
            } else if (cualCeldaFormulasConceptos == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editaOrdenFCD");
               RequestContext.getCurrentInstance().execute("PF('editaOrdenFCD').show()");
               cualCeldaFormulasConceptos = -1;
            }
            formulaSeleccionada = true;
            RequestContext.getCurrentInstance().update("form:detalleFormula");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void ingresoNuevoRegistro() {
      formulaSeleccionada = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      RequestContext.getCurrentInstance().execute("PF('seleccionarTablaNewReg').show()");
   }

   public void validarIngresoNuevaVigenciaCuenta() {
      tipoActualizacion = 1;
      limpiarNuevoVigenciaCuenta();
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
      RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaCuenta");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaCuenta').show()");
   }

   public void validarIngresoNuevaVigenciaGrupoConcepto() {
      tipoActualizacion = 1;
      limpiarNuevoVigenciaGrupoConcepto();
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVGC");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroGrupoConcepto");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroGrupoConcepto').show()");

   }

   public void validarIngresoNuevaVigenciaConceptoTT() {
      tipoActualizacion = 1;
      limpiarNuevoVigenciaConceptoTT();
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVCTT");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaConceptoTT");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaConceptoTT').show()");

   }

   public void validarIngresoNuevaVigenciaConceptoTC() {
      tipoActualizacion = 1;
      limpiarNuevoVigenciaConceptoTC();
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVCTC");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaConceptoTC");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaConceptoTC').show()");

   }

   public void validarIngresoNuevaVigenciaConceptoRL() {
      tipoActualizacion = 1;
      limpiarNuevoVigenciaConceptoRL();
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVCRL");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaConceptoRL");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaConceptoRL').show()");

   }

   public void validarIngresoNuevaFormulasConceptos() {
      tipoActualizacion = 1;
      formulaSeleccionada = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      limpiarNuevoFormulasConceptos();
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFC");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFormulaConcepto");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormulaConcepto').show()");

   }

   public void validarDuplicadoRegistro() {
      ///////////////VigenciaCuenta///////////////
      if (vigenciaCuentaSeleccionada != null) {
         tipoActualizacion = 2;
         duplicarVigenciaCuentaM();
      } ///////////////VigenciaGrupoConcepto///////////////
      else if (vigenciaGrupoCoSeleccionada != null) {
         tipoActualizacion = 2;
         duplicarVigenciaGrupoConceptoM();
      } ///////////////VigenciaConceptoTT///////////////
      else if (vigenciaConceptoTTSeleccionada != null) {
         tipoActualizacion = 2;
         duplicarVigenciaConceptoTTM();
      } ////////////VigenciaConceptoTC /////////////////////
      else if (vigenciaConceptoTCSeleccionada != null) {
         tipoActualizacion = 2;
         duplicarVigenciaConceptoTCM();
      } ////////////VigenciaConceptoRL /////////////////////
      else if (vigenciaConceptoRLSeleccionada != null) {
         tipoActualizacion = 2;
         duplicarVigenciaConceptoRLM();
      } ////////////FormulasConceptos /////////////////////
      else if (vigFormulaConceptoSeleccionada != null) {
         tipoActualizacion = 2;
         duplicarFormulasConceptosM();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //////////Validaciones de los campos de las tablas de la pagina///////////////
   public boolean validarActualizarVigenciaCuenta() {
      boolean retorno = false;
      if ((vigenciaCuentaSeleccionada.getFechafinal() != null) && (vigenciaCuentaSeleccionada.getFechainicial() != null)
              && (vigenciaCuentaSeleccionada.getConsolidadorc() != null)
              && (vigenciaCuentaSeleccionada.getConsolidadord() != null)
              && (vigenciaCuentaSeleccionada.getCuentac() != null)
              && (vigenciaCuentaSeleccionada.getCuentad() != null)
              && (vigenciaCuentaSeleccionada.getTipocc() != null)) {
         retorno = true;
      }
      return retorno;
   }

   public boolean validarNuevosDatosVigenciaCuenta() {
      log.info("ControlDetalleConcepto.validarNuevosDatosVigenciaCuenta()");
      boolean retorno = true;
      for (VigenciasCuentas nuevaVigenciaCuenta : listVigCParaCrear) {
         if (nuevaVigenciaCuenta.getAcrear() && (nuevaVigenciaCuenta.getFechafinal() == null
                 || nuevaVigenciaCuenta.getFechainicial() == null
                 || nuevaVigenciaCuenta.getConsolidadorc() == null
                 || nuevaVigenciaCuenta.getConsolidadord() == null
                 || nuevaVigenciaCuenta.getCuentac() == null
                 || nuevaVigenciaCuenta.getCuentad() == null
                 || nuevaVigenciaCuenta.getTipocc() == null)) {
            retorno = false;
            break;
         }
      }
      log.info("validarNuevosDatosVigenciaCuenta() Va a retornar: " + retorno);
      return retorno;
   }

   public boolean validarNuevosDatosVigenciaGrupoConcepto(int i) {
      boolean retorno = false;
      if (i == 0) {
         VigenciasGruposConceptos aux = null;
         aux = vigenciaGrupoCoSeleccionada;

         if ((aux.getFechafinal() != null) && (aux.getFechainicial() != null)
                 && (aux.getGrupoconcepto().getSecuencia() != null)) {
            /////
            retorno = true;
         }
      }
      if (i == 1) {
         if ((nuevaVigenciaGrupoConcepto.getFechafinal() != null) && (nuevaVigenciaGrupoConcepto.getFechainicial() != null)
                 && (nuevaVigenciaGrupoConcepto.getGrupoconcepto().getSecuencia() != null)) {
            /////
            retorno = true;
         }
      }
      if (i == 2) {
         if ((duplicarVigenciaGrupoConcepto.getFechafinal() != null) && (duplicarVigenciaGrupoConcepto.getFechainicial() != null)
                 && (duplicarVigenciaGrupoConcepto.getGrupoconcepto().getSecuencia() != null)) {
            /////
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarNuevosDatosVigenciaConceptoTT(int i) {
      boolean retorno = false;
      if (i == 0) {
         VigenciasConceptosTT aux = null;
         aux = vigenciaConceptoTTSeleccionada;

         if ((aux.getFechafinal() != null) && (aux.getFechainicial() != null)
                 && (aux.getTipotrabajador().getSecuencia() != null)) {
            /////
            retorno = true;
         }
      }
      if (i == 1) {
         if ((nuevaVigenciaConceptoTT.getTipotrabajador().getSecuencia() != null)
                 && (nuevaVigenciaConceptoTT.getFechafinal() != null) && (nuevaVigenciaConceptoTT.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      if (i == 2) {
         if ((duplicarVigenciaConceptoTT.getTipotrabajador().getSecuencia() != null)
                 && (duplicarVigenciaConceptoTT.getFechafinal() != null) && (duplicarVigenciaConceptoTT.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarNuevosDatosVigenciaConceptoTC(int i) {
      boolean retorno = false;
      if (i == 0) {
         VigenciasConceptosTC aux = null;
         aux = vigenciaConceptoTCSeleccionada;

         if ((aux.getTipocontrato().getSecuencia() != null)
                 && (aux.getFechafinal() != null) && (aux.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      if (i == 1) {
         if ((nuevaVigenciaConceptoTC.getTipocontrato().getSecuencia() != null)
                 && (nuevaVigenciaConceptoTC.getFechafinal() != null) && (nuevaVigenciaConceptoTC.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      if (i == 2) {
         if ((duplicarVigenciaConceptoTC.getTipocontrato().getSecuencia() != null)
                 && (duplicarVigenciaConceptoTC.getFechafinal() != null) && (duplicarVigenciaConceptoTC.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarNuevosDatosVigenciaConceptoRL(int i) {
      boolean retorno = false;
      if (i == 0) {
         VigenciasConceptosRL aux = null;
         aux = vigenciaConceptoRLSeleccionada;

         if ((aux.getTiposalario().getSecuencia() != null)
                 && (aux.getFechafinal() != null) && (aux.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      if (i == 1) {
         if ((nuevaVigenciaConceptoRL.getTiposalario().getSecuencia() != null)
                 && (nuevaVigenciaConceptoRL.getFechafinal() != null) && (nuevaVigenciaConceptoRL.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      if (i == 2) {
         if ((duplicarVigenciaConceptoRL.getTiposalario().getSecuencia() != null)
                 && (duplicarVigenciaConceptoRL.getFechafinal() != null) && (duplicarVigenciaConceptoRL.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarNuevosDatosFormulasConceptos(int i) {
      boolean retorno = false;
      if (i == 0) {
         FormulasConceptos aux = null;
         aux = vigFormulaConceptoSeleccionada;

         if ((aux.getFormula() != null)
                 && (!aux.getStrOrden().isEmpty())
                 && (aux.getFechafinal() != null) && (aux.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      if (i == 1) {
         if ((nuevaFormulasConceptos.getFormula() != null)
                 && (!nuevaFormulasConceptos.getStrOrden().isEmpty())
                 && (nuevaFormulasConceptos.getFechafinal() != null) && (nuevaFormulasConceptos.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      if (i == 2) {
         if ((duplicarFormulasConceptos.getFormula() != null)
                 && (!duplicarFormulasConceptos.getStrOrden().isEmpty())
                 && (duplicarFormulasConceptos.getFechafinal() != null) && (duplicarFormulasConceptos.getFechainicial() != null)) {
            /////
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarHayNuevos() {
      int cont = 0;
      for (VigenciasCuentas nuevaVigenciaCuenta : listVigCParaCrear) {
         if (nuevaVigenciaCuenta.getAcrear()) {
            log.info("validarHayNuevos() nuevaVigenciaCuenta: " + nuevaVigenciaCuenta);
            log.info("validarHayNuevos() nuevaVigenciaCuenta CodCuentac: " + nuevaVigenciaCuenta.getCodCuentac());
            log.info("validarHayNuevos() nuevaVigenciaCuenta CodConsolidadorc: " + nuevaVigenciaCuenta.getCodConsolidadorc());
            log.info("validarHayNuevos() nuevaVigenciaCuenta NombreTipocc: " + nuevaVigenciaCuenta.getNombreTipocc());
            cont++;
            break;
         }
      }
      return (cont > 0);
   }

   public void agregarNuevoVigenciaCuenta() {
      log.info("ControlDetalleConcepto.agregarNuevoVigenciaCuenta()");
      if (validarHayNuevos()) {
         if (validarNuevosDatosVigenciaCuenta()) {
            if (validarFechasNuevoVigCuenta()) {
               if (banderaVigenciaCuenta == 1) {
                  recargarVigenciaCuentaDefault();
               }
               int cont = 0;
               for (VigenciasCuentas nuevaVigenciaCuenta : listVigCParaCrear) {
                  if (nuevaVigenciaCuenta.getAcrear()) {
                     log.info("agregarNuevoVigenciaCuenta() nuevaVigenciaCuenta: " + nuevaVigenciaCuenta);
                     log.info("agregarNuevoVigenciaCuenta() nuevaVigenciaCuenta CodCuentac: " + nuevaVigenciaCuenta.getCodCuentac());
                     log.info("agregarNuevoVigenciaCuenta() nuevaVigenciaCuenta CodConsolidadorc: " + nuevaVigenciaCuenta.getCodConsolidadorc());
                     log.info("agregarNuevoVigenciaCuenta() nuevaVigenciaCuenta NombreTipocc: " + nuevaVigenciaCuenta.getNombreTipocc());
                     cont++;
                     nuevaVigenciaCuenta.setConcepto(conceptoActual);
                     listVigenciasCuentasCrear.add(nuevaVigenciaCuenta);
                     VigenciasCuentas vigCuenta = new VigenciasCuentas();
                     vigCuenta.clonar(nuevaVigenciaCuenta);
                     vigCuenta.setSecuencia(nuevaVigenciaCuenta.getSecuencia());
                     listVigenciasCuentasConcepto.add(vigCuenta);
                     vigenciaCuentaSeleccionada = listVigenciasCuentasConcepto.get(listVigenciasCuentasConcepto.indexOf(vigCuenta));
                  }
               }
               log.info("agregarNuevoVigenciaCuenta() cont: " + cont);
               ////------////
//            limpiarNuevoVigenciaCuenta();
               ////-----////
               RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaCuenta').hide()");
               RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
               RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaCuenta");
               contarRegistrosCuentas();
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               cambiosVigenciaCuenta = true;
               limpiarNuevoVigenciaCuenta();
               tipoActualizacion = 0;
               log.info("agregarNuevoVigenciaCuenta() nuevaVigenciaCuenta vigenciaCuentaSeleccionada: " + vigenciaCuentaSeleccionada);
               log.info("agregarNuevoVigenciaCuenta() nuevaVigenciaCuenta vigenciaCuentaSeleccionada.getCodCuentac(): " + vigenciaCuentaSeleccionada.getCodCuentac());
               log.info("agregarNuevoVigenciaCuenta() nuevaVigenciaCuenta vigenciaCuentaSeleccionada.getNombreTipocc(): " + vigenciaCuentaSeleccionada.getNombreTipocc());
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void limpiarNuevoVigenciaCuenta() {
      for (VigenciasCuentas recVig : listVigCParaCrear) {
//         if (tipoActualizacion == 1) {
//            if ((recVig.getAcrear())) {
//               k++;
//               recVig.clear();
//               recVig.setSecuencia(new BigInteger("" + k));
//               recVig.setConcepto(conceptoActual);
//            }
//         } else {
         k++;
         recVig.clear();
         recVig.setSecuencia(new BigInteger("" + k));
         recVig.setConcepto(conceptoActual);
//         }
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
   }

   public void modificarACrear(VigenciasCuentas vc, boolean valor) {
      log.info("ControlDetalleConcepto.modificarACrear() valor: " + valor);
      vc.setAcrear(valor);
   }

   public void modificarACrear(VigenciasCuentas vc) {
      log.info("ControlDetalleConcepto.modificarACrear()");
      if (vc.getAcrear()) {
         vc.setAcrear(false);
      } else {
         vc.setAcrear(true);
      }
   }

//
//   public void limpiarNuevoVigenciaCuenta() {
//      nuevaVigenciaCuenta = new VigenciasCuentas();
//      nuevaVigenciaCuenta.setFechainicial(new Date(100, 0, 1));
//      nuevaVigenciaCuenta.setFechafinal(new Date(9999 - 1900, 11, 31));
//      nuevaVigenciaCuenta.setConsolidadorc(new CentrosCostos());
//      nuevaVigenciaCuenta.setConsolidadord(new CentrosCostos());
//      nuevaVigenciaCuenta.setCuentac(new Cuentas());
//      nuevaVigenciaCuenta.setCuentad(new Cuentas());
//      nuevaVigenciaCuenta.setTipocc(new TiposCentrosCostos());
//      nuevaVigenciaCuenta.setProceso(null);
//      nuevaVigenciaCuenta.setNombreProceso("");
//   }
   public void agregarNuevoVigenciaGrupoConcepto() {
      if (validarNuevosDatosVigenciaGrupoConcepto(1)) {
         if (validarFechasRegistroVigenciaGrupoConcepto(1)) {
            if (banderaVigenciaGrupoConcepto == 1) {
               recargarVigenciaGrupoCDefault();
            }
            k++;

            BigInteger var = BigInteger.valueOf(k);
            nuevaVigenciaGrupoConcepto.setSecuencia(var);
            nuevaVigenciaGrupoConcepto.setConcepto(conceptoActual);
            listVigenciasGruposConceptosCrear.add(nuevaVigenciaGrupoConcepto);
            listVigenciasGruposConceptos.add(nuevaVigenciaGrupoConcepto);
            vigenciaGrupoCoSeleccionada = listVigenciasGruposConceptos.get(listVigenciasGruposConceptos.indexOf(nuevaVigenciaGrupoConcepto));
            contarRegistrosGrupoC();
            ////------////
            nuevaVigenciaGrupoConcepto = new VigenciasGruposConceptos();
            nuevaVigenciaGrupoConcepto.setGrupoconcepto(new GruposConceptos());
            ////-----////
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroGrupoConcepto').hide()");
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroGrupoConcepto");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosVigenciaGrupoConcepto = true;
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarNuevoVigenciaGrupoConcepto() {
      nuevaVigenciaGrupoConcepto = new VigenciasGruposConceptos();
      nuevaVigenciaGrupoConcepto.setGrupoconcepto(new GruposConceptos());
      vigenciaGrupoCoSeleccionada = null;
      vigenciaGrupoCoSeleccionada = null;
   }

   public void agregarNuevoVigenciaConceptoTT() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean resp = validarNuevosDatosVigenciaConceptoTT(1);
      if (resp == true) {
         boolean validacion = validarFechasRegistroVigenciaConceptoTT(1);
         if (validacion == true) {
            if (banderaVigenciaConceptoTT == 1) {
               recargarVigenciaConceptoTT();
            }
            k++;

            BigInteger var = BigInteger.valueOf(k);
            nuevaVigenciaConceptoTT.setSecuencia(var);
            nuevaVigenciaConceptoTT.setConcepto(conceptoActual);
            listVigenciasConceptosTTCrear.add(nuevaVigenciaConceptoTT);
            listVigenciasConceptosTTConcepto.add(nuevaVigenciaConceptoTT);
            vigenciaConceptoTTSeleccionada = listVigenciasConceptosTTConcepto.get(listVigenciasConceptosTTConcepto.indexOf(nuevaVigenciaConceptoTT));
            contarRegistrosConceptoTT();
            ////------////
            nuevaVigenciaConceptoTT = new VigenciasConceptosTT();
            nuevaVigenciaConceptoTT.setTipotrabajador(new TiposTrabajadores());
            ////-----////
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaConceptoTT').hide()");
            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaConceptoTT");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosVigenciaConceptoTT = true;
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarNuevoVigenciaConceptoTT() {
      nuevaVigenciaConceptoTT = new VigenciasConceptosTT();
      nuevaVigenciaConceptoTT.setTipotrabajador(new TiposTrabajadores());
      vigenciaConceptoTTSeleccionada = null;
      vigenciaConceptoTTSeleccionada = null;
   }

   public void agregarNuevoVigenciaConceptoTC() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean resp = validarNuevosDatosVigenciaConceptoTC(1);
      if (resp == true) {
         boolean validacion = validarFechasRegistroVigenciaConceptoTC(1);
         if (validacion == true) {
            if (banderaVigenciaConceptoTC == 1) {
               recargarVigenciaConceptoTC();
            }
            k++;

            BigInteger var = BigInteger.valueOf(k);
            nuevaVigenciaConceptoTC.setSecuencia(var);
            nuevaVigenciaConceptoTC.setConcepto(conceptoActual);
            listVigenciasConceptosTCCrear.add(nuevaVigenciaConceptoTC);
            listVigenciasConceptosTCConcepto.add(nuevaVigenciaConceptoTC);
            vigenciaConceptoTCSeleccionada = listVigenciasConceptosTCConcepto.get(listVigenciasConceptosTCConcepto.indexOf(nuevaVigenciaConceptoTC));
            contarRegistrosConceptoTC();
            ////------////
            nuevaVigenciaConceptoTC = new VigenciasConceptosTC();
            nuevaVigenciaConceptoTC.setTipocontrato(new TiposContratos());
            ////-----////
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaConceptoTC').hide()");
            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaConceptoTC");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosVigenciaConceptoTC = true;
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarNuevoVigenciaConceptoTC() {
      nuevaVigenciaConceptoTC = new VigenciasConceptosTC();
      nuevaVigenciaConceptoTC.setTipocontrato(new TiposContratos());
      vigenciaConceptoTCSeleccionada = null;
      vigenciaConceptoTCSeleccionada = null;
   }

   public void agregarNuevoVigenciaConceptoRL() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean resp = validarNuevosDatosVigenciaConceptoRL(1);
      if (resp == true) {
         boolean validacion = validarFechasRegistroVigenciaConceptoRL(1);
         if (validacion == true) {
            if (banderaVigenciaConceptoRL == 1) {
               recargarVigenciaConceptoRT();
            }
            k++;

            BigInteger var = BigInteger.valueOf(k);
            nuevaVigenciaConceptoRL.setSecuencia(var);
            nuevaVigenciaConceptoRL.setConcepto(conceptoActual);
            listVigenciasConceptosRLCrear.add(nuevaVigenciaConceptoRL);
            listVigenciasConceptosRLConcepto.add(nuevaVigenciaConceptoRL);
            vigenciaConceptoRLSeleccionada = listVigenciasConceptosRLConcepto.get(listVigenciasConceptosRLConcepto.indexOf(nuevaVigenciaConceptoRL));
            contarRegistrosConceptoRL();
            ////------////
            nuevaVigenciaConceptoRL = new VigenciasConceptosRL();
            nuevaVigenciaConceptoRL.setTiposalario(new ReformasLaborales());
            ////-----////
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaConceptoRL').hide()");
            RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaConceptoRL");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosVigenciaConceptoRL = true;
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarNuevoVigenciaConceptoRL() {
      nuevaVigenciaConceptoRL = new VigenciasConceptosRL();
      nuevaVigenciaConceptoRL.setTiposalario(new ReformasLaborales());
      vigenciaConceptoRLSeleccionada = null;
   }

   public void agregarNuevoFormulasConceptos() {
      boolean resp = validarNuevosDatosFormulasConceptos(1);
      if (resp == true) {
         boolean validacion = validarFechasRegistroFormulasConceptos(1);
         if (validacion == true) {
            if (banderaFormulasConceptos == 1) {
               recargarFormulaConcepto();
            }
            k++;

            BigInteger var = BigInteger.valueOf(k);
            nuevaFormulasConceptos.setSecuencia(var);
            nuevaFormulasConceptos.setConcepto(conceptoActual.getSecuencia());
            nuevaFormulasConceptos.setNombreConcepto(conceptoActual.getDescripcion());
            nuevaFormulasConceptos.setCodigoConcepto(conceptoActual.getCodigo());
            nuevaFormulasConceptos.setNombreEmpresa(conceptoActual.getNombreEmpresa());
            nuevaFormulasConceptos.setNitEmpresa(conceptoActual.getNitEmpresa());
            listFormulasConceptosCrear.add(nuevaFormulasConceptos);
            listFormulasConceptos.add(nuevaFormulasConceptos);
            vigFormulaConceptoSeleccionada = listFormulasConceptos.get(listFormulasConceptos.indexOf(nuevaFormulasConceptos));
            contarRegistrosFormulaConcepto();
            ////------////
            nuevaFormulasConceptos = new FormulasConceptos();
            ////-----////
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormulaConcepto').hide()");
            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFormulaConcepto");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosFormulasConceptos = true;
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarNuevoFormulasConceptos() {
      nuevaFormulasConceptos = new FormulasConceptos();
      vigFormulaConceptoSeleccionada = null;
   }

   public void duplicarVigenciaCuentaM() {
      if (vigenciaCuentaSeleccionada != null) {
         for (VigenciasCuentas duplicarVigenciaCuenta : listVigCParaCrear) {
            duplicarVigenciaCuenta.clonar(vigenciaCuentaSeleccionada);
//            duplicarVigenciaCuenta.clear();
//            duplicarVigenciaCuenta.setConcepto(vigenciaCuentaSeleccionada.getConcepto());
//            duplicarVigenciaCuenta.setConsolidadorc(vigenciaCuentaSeleccionada.getConsolidadorc());
//            duplicarVigenciaCuenta.setConsolidadord(vigenciaCuentaSeleccionada.getConsolidadord());
//            duplicarVigenciaCuenta.setCuentac(vigenciaCuentaSeleccionada.getCuentac());
//            duplicarVigenciaCuenta.setCuentad(vigenciaCuentaSeleccionada.getCuentad());
//            duplicarVigenciaCuenta.setFechafinal(vigenciaCuentaSeleccionada.getFechafinal());
//            duplicarVigenciaCuenta.setFechainicial(vigenciaCuentaSeleccionada.getFechainicial());
//            duplicarVigenciaCuenta.setTipocc(vigenciaCuentaSeleccionada.getTipocc());
//            duplicarVigenciaCuenta.setProceso(vigenciaCuentaSeleccionada.getProceso());
//            duplicarVigenciaCuenta.setNombreProceso(vigenciaCuentaSeleccionada.getNombreProceso());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaCuenta");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaCuenta').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarVigenciaGrupoConceptoM() {
      if (vigenciaGrupoCoSeleccionada != null) {
         duplicarVigenciaGrupoConcepto = new VigenciasGruposConceptos();
         duplicarVigenciaGrupoConcepto.setConcepto(vigenciaGrupoCoSeleccionada.getConcepto());
         duplicarVigenciaGrupoConcepto.setGrupoconcepto(vigenciaGrupoCoSeleccionada.getGrupoconcepto());
         duplicarVigenciaGrupoConcepto.setFechafinal(vigenciaGrupoCoSeleccionada.getFechafinal());
         duplicarVigenciaGrupoConcepto.setFechainicial(vigenciaGrupoCoSeleccionada.getFechainicial());

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroVigenciaGrupoConcepto");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaGrupoConcepto').show()");
      }
   }

   public void duplicarVigenciaConceptoTTM() {
      if (vigenciaConceptoTTSeleccionada != null) {
         duplicarVigenciaConceptoTT = new VigenciasConceptosTT();
         duplicarVigenciaConceptoTT.setConcepto(vigenciaConceptoTTSeleccionada.getConcepto());
         duplicarVigenciaConceptoTT.setTipotrabajador(vigenciaConceptoTTSeleccionada.getTipotrabajador());
         duplicarVigenciaConceptoTT.setFechafinal(vigenciaConceptoTTSeleccionada.getFechafinal());
         duplicarVigenciaConceptoTT.setFechainicial(vigenciaConceptoTTSeleccionada.getFechainicial());

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroVigenciaConceptoTT");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaConceptoTT').show()");
      }
   }

   public void duplicarVigenciaConceptoTCM() {
      if (vigenciaConceptoTCSeleccionada != null) {
         duplicarVigenciaConceptoTC = new VigenciasConceptosTC();
         duplicarVigenciaConceptoTC.setConcepto(vigenciaConceptoTCSeleccionada.getConcepto());
         duplicarVigenciaConceptoTC.setTipocontrato(vigenciaConceptoTCSeleccionada.getTipocontrato());
         duplicarVigenciaConceptoTC.setFechafinal(vigenciaConceptoTCSeleccionada.getFechafinal());
         duplicarVigenciaConceptoTC.setFechainicial(vigenciaConceptoTCSeleccionada.getFechainicial());

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroVigenciaConceptoTC");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaConceptoTC').show()");
      }
   }

   public void duplicarVigenciaConceptoRLM() {
      if (vigenciaConceptoRLSeleccionada != null) {
         duplicarVigenciaConceptoRL = new VigenciasConceptosRL();
         duplicarVigenciaConceptoRL.setConcepto(vigenciaConceptoRLSeleccionada.getConcepto());
         duplicarVigenciaConceptoRL.setTiposalario(vigenciaConceptoRLSeleccionada.getTiposalario());
         duplicarVigenciaConceptoRL.setFechafinal(vigenciaConceptoRLSeleccionada.getFechafinal());
         duplicarVigenciaConceptoRL.setFechainicial(vigenciaConceptoRLSeleccionada.getFechainicial());

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroVigenciaConceptoRL");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaConceptoRL').show()");
      }
   }

   public void duplicarFormulasConceptosM() {
      if (vigFormulaConceptoSeleccionada != null) {
         formulaSeleccionada = true;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:detalleFormula");
         duplicarFormulasConceptos = new FormulasConceptos();
         duplicarFormulasConceptos.setConcepto(vigFormulaConceptoSeleccionada.getConcepto());
         duplicarFormulasConceptos.setFormula(vigFormulaConceptoSeleccionada.getFormula());
         duplicarFormulasConceptos.setFechafinal(vigFormulaConceptoSeleccionada.getFechafinal());
         duplicarFormulasConceptos.setFechainicial(vigFormulaConceptoSeleccionada.getFechainicial());
         duplicarFormulasConceptos.setStrOrden(vigFormulaConceptoSeleccionada.getStrOrden());

         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroFormulaConcepto");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormulaConcepto').show()");
      }
   }

//   public void confirmarDuplicarVigenciaCuenta() {
//      if (validarNuevosDatosVigenciaCuenta()) {
////         if (validarFechasRegistroVigenciaCuenta(2)) {
////            if (vigenciaCuentaSeleccionada != null) {
////               if (banderaVigenciaCuenta == 1) {
////                  recargarVigenciaCuentaDefault();
////               }
////               k++;
////               BigInteger var = BigInteger.valueOf(k);
////               duplicarVigenciaCuenta.setSecuencia(var);
////               duplicarVigenciaCuenta.setConcepto(conceptoActual);
////               listVigenciasCuentasCrear.add(duplicarVigenciaCuenta);
////               listVigenciasCuentasConcepto.add(duplicarVigenciaCuenta);
////               vigenciaCuentaSeleccionada = listVigenciasCuentasConcepto.get(listVigenciasCuentasConcepto.indexOf(duplicarVigenciaCuenta));
////               contarRegistrosCuentas();
////
////               limpiarDuplicarVigenciaCuenta();
////               RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
////               if (guardado == true) {
////                  guardado = false;
////                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
////               }
////               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaCuenta').hide()");
////               cambiosVigenciaCuenta = true;
////            }
////         } else {
////            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
////         }
//      } else {
//         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
//      }
//   }
//   public void limpiarDuplicarVigenciaCuenta() {
//      duplicarVigenciaCuenta = new VigenciasCuentas();
//      duplicarVigenciaCuenta.setConsolidadorc(new CentrosCostos());
//      duplicarVigenciaCuenta.setConsolidadord(new CentrosCostos());
//      duplicarVigenciaCuenta.setCuentac(new Cuentas());
//      duplicarVigenciaCuenta.setCuentad(new Cuentas());
//      duplicarVigenciaCuenta.setProceso(new BigInteger("0"));
//      duplicarVigenciaCuenta.setNombreProceso("");
//   }
   public void confirmarDuplicarVigenciaGrupoConcepto() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean resp = validarNuevosDatosVigenciaGrupoConcepto(2);
      if (resp == true) {
         boolean validacion = validarFechasRegistroVigenciaGrupoConcepto(2);
         if (validacion == true) {
            if (vigenciaGrupoCoSeleccionada != null) {
               if (banderaVigenciaGrupoConcepto == 1) {
                  recargarVigenciaGrupoCDefault();
               }
               k++;
               BigInteger var = BigInteger.valueOf(k);
               duplicarVigenciaGrupoConcepto.setSecuencia(var);
               duplicarVigenciaGrupoConcepto.setConcepto(conceptoActual);
               listVigenciasGruposConceptosCrear.add(duplicarVigenciaGrupoConcepto);
               listVigenciasGruposConceptos.add(duplicarVigenciaGrupoConcepto);
               vigenciaGrupoCoSeleccionada = listVigenciasGruposConceptos.get(listVigenciasGruposConceptos.indexOf(duplicarVigenciaGrupoConcepto));
               contarRegistrosGrupoC();

               duplicarVigenciaGrupoConcepto = new VigenciasGruposConceptos();
               duplicarVigenciaGrupoConcepto.setGrupoconcepto(new GruposConceptos());
               RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaGrupoConcepto').hide()");
               cambiosVigenciaGrupoConcepto = true;
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarDuplicarVigenciaGrupoConcepto() {
      duplicarVigenciaGrupoConcepto = new VigenciasGruposConceptos();
      duplicarVigenciaGrupoConcepto.setGrupoconcepto(new GruposConceptos());
   }

   public void confirmarDuplicarVigenciaConceptoTT() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean resp = validarNuevosDatosVigenciaConceptoTT(2);
      if (resp == true) {
         boolean validacion = validarFechasRegistroVigenciaConceptoTT(2);
         if (validacion == true) {
            if (vigenciaConceptoTTSeleccionada != null) {
               if (banderaVigenciaConceptoTT == 1) {
                  recargarVigenciaConceptoTT();
               }
               k++;
               BigInteger var = BigInteger.valueOf(k);
               duplicarVigenciaConceptoTT.setSecuencia(var);
               duplicarVigenciaConceptoTT.setConcepto(conceptoActual);
               listVigenciasConceptosTTCrear.add(duplicarVigenciaConceptoTT);
               listVigenciasConceptosTTConcepto.add(duplicarVigenciaConceptoTT);
               vigenciaConceptoTTSeleccionada = listVigenciasConceptosTTConcepto.get(listVigenciasConceptosTTConcepto.indexOf(duplicarVigenciaConceptoTT));
               contarRegistrosConceptoTT();

               duplicarVigenciaConceptoTT = new VigenciasConceptosTT();
               duplicarVigenciaConceptoTT.setTipotrabajador(new TiposTrabajadores());
               RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaConceptoTT').hide()");
               cambiosVigenciaConceptoTT = true;
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarDuplicarVigenciaConceptoTT() {
      duplicarVigenciaConceptoTT = new VigenciasConceptosTT();
      duplicarVigenciaConceptoTT.setTipotrabajador(new TiposTrabajadores());
   }

   public void confirmarDuplicarVigenciaConceptoTC() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean resp = validarNuevosDatosVigenciaConceptoTC(2);
      if (resp == true) {
         boolean validacion = validarFechasRegistroVigenciaConceptoTC(2);
         if (validacion == true) {
            if (vigenciaConceptoTCSeleccionada != null) {
               if (banderaVigenciaConceptoTC == 1) {
                  recargarVigenciaConceptoTC();
               }
               k++;
               BigInteger var = BigInteger.valueOf(k);
               duplicarVigenciaConceptoTC.setSecuencia(var);
               duplicarVigenciaConceptoTC.setConcepto(conceptoActual);
               listVigenciasConceptosTCCrear.add(duplicarVigenciaConceptoTC);
               listVigenciasConceptosTCConcepto.add(duplicarVigenciaConceptoTC);
               vigenciaConceptoTCSeleccionada = listVigenciasConceptosTCConcepto.get(listVigenciasConceptosTCConcepto.indexOf(duplicarVigenciaConceptoTC));
               contarRegistrosConceptoTC();

               duplicarVigenciaConceptoTC = new VigenciasConceptosTC();
               duplicarVigenciaConceptoTC.setTipocontrato(new TiposContratos());
               RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaConceptoTC').hide()");
               cambiosVigenciaConceptoTC = true;
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarDuplicarVigenciaConceptoTC() {
      duplicarVigenciaConceptoTC = new VigenciasConceptosTC();
      duplicarVigenciaConceptoTC.setTipocontrato(new TiposContratos());
   }

   public void confirmarDuplicarVigenciaConceptoRL() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean resp = validarNuevosDatosVigenciaConceptoRL(2);
      if (resp == true) {
         boolean validacion = validarFechasRegistroVigenciaConceptoRL(2);
         if (validacion == true) {
            if (vigenciaConceptoRLSeleccionada != null) {
               if (banderaVigenciaConceptoRL == 1) {
                  recargarVigenciaConceptoRT();
               }
               k++;
               BigInteger var = BigInteger.valueOf(k);
               duplicarVigenciaConceptoRL.setSecuencia(var);
               duplicarVigenciaConceptoRL.setConcepto(conceptoActual);
               listVigenciasConceptosRLCrear.add(duplicarVigenciaConceptoRL);
               listVigenciasConceptosRLConcepto.add(duplicarVigenciaConceptoRL);
               vigenciaConceptoRLSeleccionada = listVigenciasConceptosRLConcepto.get(listVigenciasConceptosRLConcepto.indexOf(duplicarVigenciaConceptoRL));
               contarRegistrosConceptoRL();

               duplicarVigenciaConceptoRL = new VigenciasConceptosRL();
               duplicarVigenciaConceptoRL.setTiposalario(new ReformasLaborales());
               RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaConceptoRL').hide()");
               cambiosVigenciaConceptoRL = true;
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarDuplicarVigenciaConceptoRL() {
      duplicarVigenciaConceptoRL = new VigenciasConceptosRL();
      duplicarVigenciaConceptoRL.setTiposalario(new ReformasLaborales());
   }

   public void confirmarDuplicarFormulasConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean resp = validarNuevosDatosFormulasConceptos(2);
      if (resp == true) {
         boolean validacion = validarFechasRegistroFormulasConceptos(2);
         if (validacion == true) {
            if (vigFormulaConceptoSeleccionada != null) {
               if (banderaFormulasConceptos == 1) {
                  recargarFormulaConcepto();
               }
               k++;
               BigInteger var = BigInteger.valueOf(k);
               duplicarFormulasConceptos.setSecuencia(var);
               duplicarFormulasConceptos.setConcepto(conceptoActual.getSecuencia());
               duplicarFormulasConceptos.setNombreConcepto(conceptoActual.getDescripcion());
               duplicarFormulasConceptos.setCodigoConcepto(conceptoActual.getCodigo());
               duplicarFormulasConceptos.setNombreEmpresa(conceptoActual.getNombreEmpresa());
               duplicarFormulasConceptos.setNitEmpresa(conceptoActual.getNitEmpresa());
               listFormulasConceptosCrear.add(duplicarFormulasConceptos);
               listFormulasConceptos.add(duplicarFormulasConceptos);
               vigFormulaConceptoSeleccionada = listFormulasConceptos.get(listFormulasConceptos.indexOf(duplicarFormulasConceptos));
               contarRegistrosFormulaConcepto();

               duplicarFormulasConceptos = new FormulasConceptos();
               RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               formulaSeleccionada = true;
               RequestContext.getCurrentInstance().update("form:detalleFormula");
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormulaConcepto').hide()");
               cambiosFormulasConceptos = true;
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarDuplicarFormulasConceptos() {
      duplicarFormulasConceptos = new FormulasConceptos();
   }
   ///////////////////////////////////////////////////////////////

   public void validarBorradoRegistro() {
      if (vigenciaCuentaSeleccionada != null) {
         borrarVigenciaCuenta();
      } else if (vigenciaGrupoCoSeleccionada != null) {
         borrarVigenciaGrupoConcepto();
      } else if (vigenciaConceptoTTSeleccionada != null) {
         borrarVigenciaConceptoTT();
      } else if (vigenciaConceptoTCSeleccionada != null) {
         borrarVigenciaConceptoTC();
      } else if (vigenciaConceptoRLSeleccionada != null) {
         borrarVigenciaConceptoRL();
      } else if (vigFormulaConceptoSeleccionada != null) {
         borrarFormulasConceptos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   /**
    * Valida que registro se elimina de que tabla con respecto a la posicion en
    * la pagina
    */
   public void borrarVigenciaCuenta() {
      if (vigenciaCuentaSeleccionada != null) {
         if (!listVigenciasCuentasModificar.isEmpty() && listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
            listVigenciasCuentasModificar.remove(vigenciaCuentaSeleccionada);
            listVigenciasCuentasBorrar.add(vigenciaCuentaSeleccionada);
         } else if (!listVigenciasCuentasCrear.isEmpty() && listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            listVigenciasCuentasCrear.remove(vigenciaCuentaSeleccionada);
         } else {
            listVigenciasCuentasBorrar.add(vigenciaCuentaSeleccionada);
         }
         listVigenciasCuentasConcepto.remove(vigenciaCuentaSeleccionada);
         if (tipoListaVigenciaCuenta == 1) {
            filtrarListVigenciasCuentasConcepto.remove(vigenciaCuentaSeleccionada);
         }
         contarRegistrosCuentas();
         vigenciaCuentaSeleccionada = null;
         cambiosVigenciaCuenta = true;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void borrarVigenciaGrupoConcepto() {
      if (vigenciaGrupoCoSeleccionada != null) {
         if (!listVigenciasGruposConceptosModificar.isEmpty() && listVigenciasGruposConceptosModificar.contains(vigenciaGrupoCoSeleccionada)) {
            listVigenciasGruposConceptosModificar.remove(vigenciaGrupoCoSeleccionada);
            listVigenciasGruposConceptosBorrar.add(vigenciaGrupoCoSeleccionada);
         } else if (!listVigenciasGruposConceptosCrear.isEmpty() && listVigenciasGruposConceptosCrear.contains(vigenciaGrupoCoSeleccionada)) {
            listVigenciasGruposConceptosCrear.remove(vigenciaGrupoCoSeleccionada);
         } else {
            listVigenciasGruposConceptosBorrar.add(vigenciaGrupoCoSeleccionada);
         }
         listVigenciasGruposConceptos.remove(vigenciaGrupoCoSeleccionada);
         if (tipoListaVigenciaGrupoConcepto == 1) {
            filtrarListVigenciasGruposConceptos.remove(vigenciaGrupoCoSeleccionada);
         }
         contarRegistrosGrupoC();
         vigenciaGrupoCoSeleccionada = null;

         RequestContext context = RequestContext.getCurrentInstance();
         cambiosVigenciaGrupoConcepto = true;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
      }
   }

   public void borrarVigenciaConceptoTT() {
      if (vigenciaConceptoTTSeleccionada != null) {
         if (!listVigenciasConceptosTTModificar.isEmpty() && listVigenciasConceptosTTModificar.contains(vigenciaConceptoTTSeleccionada)) {
            listVigenciasConceptosTTModificar.remove(vigenciaConceptoTTSeleccionada);
            listVigenciasConceptosTTBorrar.add(vigenciaConceptoTTSeleccionada);
         } else if (!listVigenciasConceptosTTCrear.isEmpty() && listVigenciasConceptosTTCrear.contains(vigenciaConceptoTTSeleccionada)) {
            listVigenciasConceptosTTCrear.remove(vigenciaConceptoTTSeleccionada);
         } else {
            listVigenciasConceptosTTBorrar.add(vigenciaConceptoTTSeleccionada);
         }
         listVigenciasConceptosTTConcepto.remove(vigenciaConceptoTTSeleccionada);
         if (tipoListaVigenciaConceptoTT == 1) {
            filtrarListVigenciasConceptosTT.remove(vigenciaConceptoTTSeleccionada);
         }
         contarRegistrosConceptoTT();
         vigenciaConceptoTTSeleccionada = null;

         RequestContext context = RequestContext.getCurrentInstance();
         cambiosVigenciaConceptoTT = true;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
      }
   }

   public void borrarVigenciaConceptoTC() {
      if (vigenciaConceptoTCSeleccionada != null) {
         if (!listVigenciasConceptosTCModificar.isEmpty() && listVigenciasConceptosTCModificar.contains(vigenciaConceptoTCSeleccionada)) {
            listVigenciasConceptosTCModificar.remove(vigenciaConceptoTCSeleccionada);
            listVigenciasConceptosTCBorrar.add(vigenciaConceptoTCSeleccionada);
         } else if (!listVigenciasConceptosTCCrear.isEmpty() && listVigenciasConceptosTCCrear.contains(vigenciaConceptoTCSeleccionada)) {
            listVigenciasConceptosTCCrear.remove(vigenciaConceptoTCSeleccionada);
         } else {
            listVigenciasConceptosTCBorrar.add(vigenciaConceptoTCSeleccionada);
         }
         listVigenciasConceptosTCConcepto.remove(vigenciaConceptoTCSeleccionada);
         if (tipoListaVigenciaConceptoTC == 1) {
            filtrarListVigenciasConceptosTC.remove(vigenciaConceptoTCSeleccionada);
         }
         contarRegistrosConceptoTC();
         vigenciaConceptoTCSeleccionada = null;

         RequestContext context = RequestContext.getCurrentInstance();
         cambiosVigenciaConceptoTC = true;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
      }
   }

   public void borrarVigenciaConceptoRL() {
      if (vigenciaConceptoRLSeleccionada != null) {
         if (!listVigenciasConceptosRLModificar.isEmpty() && listVigenciasConceptosRLModificar.contains(vigenciaConceptoRLSeleccionada)) {
            listVigenciasConceptosRLModificar.remove(vigenciaConceptoRLSeleccionada);
            listVigenciasConceptosRLBorrar.add(vigenciaConceptoRLSeleccionada);
         } else if (!listVigenciasConceptosRLCrear.isEmpty() && listVigenciasConceptosRLCrear.contains(vigenciaConceptoRLSeleccionada)) {
            listVigenciasConceptosRLCrear.remove(vigenciaConceptoRLSeleccionada);
         } else {
            listVigenciasConceptosRLBorrar.add(vigenciaConceptoRLSeleccionada);
         }
         listVigenciasConceptosRLConcepto.remove(vigenciaConceptoRLSeleccionada);
         if (tipoListaVigenciaConceptoRL == 1) {
            filtrarListVigenciasConceptosRL.remove(vigenciaConceptoRLSeleccionada);
         }
         contarRegistrosConceptoRL();
         vigenciaConceptoRLSeleccionada = null;

         cambiosVigenciaConceptoRL = true;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
      }
   }

   public void borrarFormulasConceptos() {
      if (vigFormulaConceptoSeleccionada != null) {
         if (!listFormulasConceptosModificar.isEmpty() && listFormulasConceptosModificar.contains(vigFormulaConceptoSeleccionada)) {
            listFormulasConceptosModificar.remove(vigFormulaConceptoSeleccionada);
            listFormulasConceptosBorrar.add(vigFormulaConceptoSeleccionada);
         } else if (!listFormulasConceptosCrear.isEmpty() && listFormulasConceptosCrear.contains(vigFormulaConceptoSeleccionada)) {
            listFormulasConceptosCrear.remove(vigFormulaConceptoSeleccionada);
         } else {
            listFormulasConceptosBorrar.add(vigFormulaConceptoSeleccionada);
         }
         listFormulasConceptos.remove(vigFormulaConceptoSeleccionada);
         if (tipoListaFormulasConceptos == 1) {
            filtrarListFormulasConceptos.remove(vigFormulaConceptoSeleccionada);
         }
         contarRegistrosFormulaConcepto();
         vigFormulaConceptoSeleccionada = null;

         formulaSeleccionada = true;
         cambiosFormulasConceptos = true;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:detalleFormula");
         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      }
   }
   //CTRL + F11 ACTIVAR/DESACTIVAR

   /**
    * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      vigenciaConceptoRLSeleccionada = null;
      vigenciaConceptoTTSeleccionada = null;
      vigenciaConceptoTCSeleccionada = null;
      vigenciaCuentaSeleccionada = null;
      vigenciaGrupoCoSeleccionada = null;
      vigFormulaConceptoSeleccionada = null;
      log.info("Entro en activarCtrlF11()");
      if (vigenciaCuentaSeleccionada != null) {
         filtradoVigenciaCuenta();
      }
      if (vigenciaGrupoCoSeleccionada != null) {
         filtradoVigenciaGrupoConcepto();
      }
      if (vigenciaConceptoTTSeleccionada != null) {
         filtradoVigenciaConceptoTT();
      }
      if (vigenciaConceptoTCSeleccionada != null) {
         filtradoVigenciaConceptoTC();
      }
      if (vigenciaConceptoRLSeleccionada != null) {
         filtradoVigenciaConceptoRL();
      }
      if (vigFormulaConceptoSeleccionada != null) {
         filtradoFormulasConceptos();
         formulaSeleccionada = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
      }
   }

   public void filtradoVigenciaCuenta() {
      log.info("banderaVigenciaCuenta : " + banderaVigenciaCuenta);
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaVigenciaCuenta == 0) {
         altoTablaVigenciaCuenta = "85";
         vigenciaCuentaFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaFechaInicial");
         vigenciaCuentaFechaInicial.setFilterStyle("width: 85% !important;");
         vigenciaCuentaFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaFechaFinal");
         vigenciaCuentaFechaFinal.setFilterStyle("width: 85% !important;");
         vigenciaCuentaTipoCC = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaTipoCC");
         vigenciaCuentaTipoCC.setFilterStyle("width: 85% !important;");
         vigenciaCuentaDebitoCod = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaDebitoCod");
         vigenciaCuentaDebitoCod.setFilterStyle("width: 85% !important;");
         vigenciaCuentaDebitoDes = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaDebitoDes");
         vigenciaCuentaDebitoDes.setFilterStyle("width: 85% !important;");
         vigenciaCuentaCCConsolidadorD = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCConsolidadorD");
         vigenciaCuentaCCConsolidadorD.setFilterStyle("width: 85% !important;");
         vigenciaCuentaCreditoCod = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCreditoCod");
         vigenciaCuentaCreditoCod.setFilterStyle("width: 85% !important;");
         vigenciaCuentaCreditoDes = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCreditoDes");
         vigenciaCuentaCreditoDes.setFilterStyle("width: 85% !important;");
         vigenciaCuentaCCConsolidadorC = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCConsolidadorC");
         vigenciaCuentaCCConsolidadorC.setFilterStyle("width: 85% !important;");
         vigenciaCuentaCCProceso = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCProceso");
         vigenciaCuentaCCProceso.setFilterStyle("width: 85% !important;");
         banderaVigenciaCuenta = 1;
      } else if (banderaVigenciaCuenta == 1) {
         recargarVigenciaCuentaDefault();
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
   }

   public void filtradoVigenciaGrupoConcepto() {
      log.info("banderaVigenciaGrupoConcepto : " + banderaVigenciaGrupoConcepto);
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaVigenciaGrupoConcepto == 0) {
         altoTablaVigenciaGrupoC = "100";
         vigenciaGCFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoConcepto:vigenciaGCFechaInicial");
         vigenciaGCFechaInicial.setFilterStyle("width: 85% !important;");
         vigenciaGCFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoConcepto:vigenciaGCFechaFinal");
         vigenciaGCFechaFinal.setFilterStyle("width: 85% !important;");
         vigenciaGCCodigo = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoConcepto:vigenciaGCCodigo");
         vigenciaGCCodigo.setFilterStyle("width: 85% !important;");
         vigenciaGCDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoConcepto:vigenciaGCDescripcion");
         vigenciaGCDescripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
         banderaVigenciaGrupoConcepto = 1;
      } else if (banderaVigenciaGrupoConcepto == 1) {
         recargarVigenciaGrupoCDefault();
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
   }

   public void filtradoVigenciaConceptoTT() {
      log.info("banderaVigenciaConceptoTT : " + banderaVigenciaConceptoTT);
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaVigenciaConceptoTT == 0) {
         altoTablaVigenciaConceptoTT = "85";
         vigenciaCTTFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTT:vigenciaCTTFechaFinal");
         vigenciaCTTFechaFinal.setFilterStyle("width: 85% !important;");
         vigenciaCTTFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTT:vigenciaCTTFechaInicial");
         vigenciaCTTFechaInicial.setFilterStyle("width: 85% !important;");
         vigenciaCTTDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTT:vigenciaCTTDescripcion");
         vigenciaCTTDescripcion.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
         banderaVigenciaConceptoTT = 1;
      } else if (banderaVigenciaConceptoTT == 1) {
         recargarVigenciaConceptoTT();
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
   }

   public void filtradoVigenciaConceptoTC() {
      log.info("banderaVigenciaConceptoTC : " + banderaVigenciaConceptoTC);
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaVigenciaConceptoTC == 0) {
         altoTablaVigenciaConceptoTC = "85";
         vigenciaCTCFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTC:vigenciaCTCFechaFinal");
         vigenciaCTCFechaFinal.setFilterStyle("width: 85% !important;");
         vigenciaCTCFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTC:vigenciaCTCFechaInicial");
         vigenciaCTCFechaInicial.setFilterStyle("width: 85% !important;");
         vigenciaCTCDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTC:vigenciaCTCDescripcion");
         vigenciaCTCDescripcion.setFilterStyle("width: 85% !important;");
         banderaVigenciaConceptoTC = 1;
      } else if (banderaVigenciaConceptoTC == 1) {
         recargarVigenciaConceptoTC();
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
   }

   public void filtradoVigenciaConceptoRL() {
      log.info("banderaVigenciaConceptoRL : " + banderaVigenciaConceptoRL);
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaVigenciaConceptoRL == 0) {
         altoTablaVigenciaConceptoRL = "85";
         vigenciaCRLFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoRL:vigenciaCRLFechaFinal");
         vigenciaCRLFechaFinal.setFilterStyle("width: 85% !important;");
         vigenciaCRLFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoRL:vigenciaCRLFechaInicial");
         vigenciaCRLFechaInicial.setFilterStyle("width: 85% !important;");
         vigenciaCRLDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoRL:vigenciaCRLDescripcion");
         vigenciaCRLDescripcion.setFilterStyle("width: 85% !important;");
         banderaVigenciaConceptoRL = 1;
      } else if (banderaVigenciaConceptoRL == 1) {
         recargarVigenciaConceptoRT();
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
   }

   public void filtradoFormulasConceptos() {
      log.info("banderaFormulasConceptos : " + banderaFormulasConceptos);
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaFormulasConceptos == 0) {
         altoTablaFormulaConcepto = "102";
         formulaCFechaInicial = (Column) c.getViewRoot().findComponent("form:datosFormulaConcepto:formulaCFechaInicial");
         formulaCFechaInicial.setFilterStyle("width: 85% !important;");
         formulaCFechaFinal = (Column) c.getViewRoot().findComponent("form:datosFormulaConcepto:formulaCFechaFinal");
         formulaCFechaFinal.setFilterStyle("width: 85% !important;");
         formulaCNombre = (Column) c.getViewRoot().findComponent("form:datosFormulaConcepto:formulaCNombre");
         formulaCNombre.setFilterStyle("width: 85% !important;");
         formulaCOrden = (Column) c.getViewRoot().findComponent("form:datosFormulaConcepto:formulaCOrden");
         formulaCOrden.setFilterStyle("width: 85% !important;");
         banderaFormulasConceptos = 1;
      } else if (banderaFormulasConceptos == 1) {
         recargarFormulaConcepto();
      }
      RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
   }

   //Activar y desactivar el boton de listas de valores : 
   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      if (banderaVigenciaCuenta == 1) {
         recargarVigenciaCuentaDefault();
      }
      if (banderaVigenciaGrupoConcepto == 1) {
         recargarVigenciaGrupoCDefault();
      }
      if (banderaVigenciaConceptoTT == 1) {
         recargarVigenciaConceptoTT();
      }
      if (banderaVigenciaConceptoTC == 1) {
         recargarVigenciaConceptoTC();
      }
      if (banderaVigenciaConceptoRL == 1) {
         recargarVigenciaConceptoRT();
      }

      if (banderaFormulasConceptos == 1) {
         recargarFormulaConcepto();
      }

      listVigenciasCuentasBorrar.clear();
      listVigenciasCuentasCrear.clear();
      listVigenciasCuentasModificar.clear();

      listVigenciasGruposConceptosBorrar.clear();
      listVigenciasGruposConceptosCrear.clear();
      listVigenciasGruposConceptosModificar.clear();

      listVigenciasConceptosTTBorrar.clear();
      listVigenciasConceptosTTCrear.clear();
      listVigenciasConceptosTTModificar.clear();

      listVigenciasConceptosTCBorrar.clear();
      listVigenciasConceptosTCCrear.clear();
      listVigenciasConceptosTCModificar.clear();

      listVigenciasConceptosRLBorrar.clear();
      listVigenciasConceptosRLCrear.clear();
      listVigenciasConceptosRLModificar.clear();

      listFormulasConceptosBorrar.clear();
      listFormulasConceptosCrear.clear();
      listFormulasConceptosModificar.clear();

      vigenciaGrupoCoSeleccionada = null;
      vigenciaCuentaSeleccionada = null;
      vigenciaConceptoRLSeleccionada = null;
      vigenciaConceptoTTSeleccionada = null;
      vigenciaConceptoTCSeleccionada = null;
      vigFormulaConceptoSeleccionada = null;

      conceptoActual = null;
      k = 0;

      listVigenciasCuentasConcepto = null;
      listVigenciasGruposConceptos = null;
      listVigenciasConceptosTTConcepto = null;
      listVigenciasConceptosTCConcepto = null;
      listVigenciasConceptosRLConcepto = null;
      listFormulasConceptos = null;

      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

      cambiosVigenciaCuenta = false;
      cambiosVigenciaGrupoConcepto = false;
      cambiosVigenciaConceptoTT = false;
      cambiosVigenciaConceptoTC = false;
      cambiosVigenciaConceptoRL = false;
      cambiosFormulasConceptos = false;

//      nuevaVigenciaCuenta = new VigenciasCuentas();
//      nuevaVigenciaCuenta.setFechainicial(new Date(100, 0, 1));
//      nuevaVigenciaCuenta.setFechafinal(new Date(9999 - 1900, 11, 31));
//      nuevaVigenciaCuenta.setConsolidadorc(new CentrosCostos());
//      nuevaVigenciaCuenta.setConsolidadord(new CentrosCostos());
//      nuevaVigenciaCuenta.setCuentac(new Cuentas());
//      nuevaVigenciaCuenta.setCuentad(new Cuentas());
//      nuevaVigenciaCuenta.setTipocc(new TiposCentrosCostos());
//      nuevaVigenciaCuenta.setProceso(null);
//      nuevaVigenciaCuenta.setNombreProceso("");
      nuevaVigenciaConceptoTT = new VigenciasConceptosTT();
      nuevaVigenciaConceptoTC = new VigenciasConceptosTC();
      nuevaVigenciaConceptoRL = new VigenciasConceptosRL();
      nuevaVigenciaGrupoConcepto = new VigenciasGruposConceptos();
      nuevaFormulasConceptos = new FormulasConceptos();

//      duplicarVigenciaCuenta = new VigenciasCuentas();
      duplicarVigenciaGrupoConcepto = new VigenciasGruposConceptos();
      duplicarVigenciaConceptoTT = new VigenciasConceptosTT();
      duplicarVigenciaConceptoRL = new VigenciasConceptosRL();
      duplicarVigenciaConceptoTC = new VigenciasConceptosTC();
      duplicarFormulasConceptos = new FormulasConceptos();

      lovTiposContratos = null;
      lovTiposTrabajadores = null;
      lovGruposConceptos = null;
      lovCentrosCostos = null;
      lovCuentas = null;
      lovTiposCentrosCostos = null;
      lovReformasLaborales = null;
      lovFormulas = null;
      lovFormulasConceptos = null;
      lovProcesos = null;
      formulaSeleccionada = true;
      num = 1;
      navegar("atras");
   }

   public void actualizarTipoCentroCosto() {
      log.info("actualizarTipoCentroCosto() tipoActualizacion : " + tipoActualizacion);
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setTipocc(tipoCentroCostoSeleccionadoLOV);
         if (autocompletarTipoCC(vigenciaCuentaSeleccionada)) {
            if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
               if (listVigenciasCuentasModificar.isEmpty()) {
                  listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
               } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
                  listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
               }
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         permitirIndexVigenciaCuenta = true;
         cambiosVigenciaCuenta = true;
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setTipocc(tipoCentroCostoSeleccionadoLOV);
         autocompletarTipoCC(vigCuentaCrearSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaTipoCCVC");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
      }
      tipoCentroCostoSeleccionadoLOV = null;
      aceptar = true;
//      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
      RequestContext.getCurrentInstance().update("formlovTiposCC:lovTiposCC");
      RequestContext.getCurrentInstance().update("formlovTiposCC:aceptarTCC");
      context.reset("formlovTiposCC:lovTiposCC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposCC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').hide()");
   }

   public void siguienteListaIngreso() {
      if (tipoActualizacion == 1) {
//         if (nuevaVigenciaCuenta.getCodCuentad() == null) {
//            asignarIndex(1, 1, 0);
//         } else if (nuevaVigenciaCuenta.getNombreConsolidadord() == null) {
//            asignarIndex(3, 1, 0);
//         } else if (nuevaVigenciaCuenta.getCodCuentac() == null) {
//            asignarIndex(4, 1, 0);
//         } else if (nuevaVigenciaCuenta.getNombreConsolidadorc() == null) {
//            asignarIndex(6, 1, 0);
//         }
      } else if (tipoActualizacion == 2) {
//         if (nuevaVigenciaCuenta.getCodCuentad() == null) {
//            asignarIndex(1, 2, 0);
//         } else if (nuevaVigenciaCuenta.getNombreConsolidadord() == null) {
//            asignarIndex(3, 2, 0);
//         } else if (nuevaVigenciaCuenta.getCodCuentac() == null) {
//            asignarIndex(4, 2, 0);
//         } else if (nuevaVigenciaCuenta.getNombreConsolidadorc() == null) {
//            asignarIndex(6, 2, 0);
//         }
      }
   }

   public void cancelarCambioTipoCentroCosto() {
      tipoCentroCostoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaCuenta = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovTiposCC:lovTiposCC");
      RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
      RequestContext.getCurrentInstance().update("formlovTiposCC:aceptarTCC");
      context.reset("formlovTiposCC:lovTiposCC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposCC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').hide()");
   }

   public void actualizarCuentaDebito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setCuentad(cuentaSeleccionadaLOV);
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexVigenciaCuenta = true;
         cambiosVigenciaCuenta = true;
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setCuentad(cuentaSeleccionadaLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDebitoVC");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDesDebitoVC");
      }
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovDebito:lovDebito");
      RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
      RequestContext.getCurrentInstance().update("formlovDebito:aceptarCD");
      context.reset("formlovDebito:lovDebito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovDebito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').hide()");
   }

   public void actualizarCuentas2505() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setCuentad(cuentaSeleccionadaLOV);
         vigenciaCuentaSeleccionada.setCuentac(cuentaSeleccionadaLOV);
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexVigenciaCuenta = true;
         cambiosVigenciaCuenta = true;
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setCuentad(cuentaSeleccionadaLOV);
         vigCuentaCrearSeleccionada.setCuentac(cuentaSeleccionadaLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDebitoVC");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDesDebitoVC");
      }
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovCuentas:lovCuentas");
      RequestContext.getCurrentInstance().update("formlovCuentas:CuentasDialogo");
      RequestContext.getCurrentInstance().update("formlovCuentas:aceptarCuentas");
      context.reset("formlovCuentas:lovCuentas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCuentas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CuentasDialogo').hide()");
   }

   public void cancelarCambioCuentaDebito() {
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaCuenta = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovDebito:lovDebito");
      RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
      RequestContext.getCurrentInstance().update("formlovDebito:aceptarCD");
      context.reset("formlovDebito:lovDebito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovDebito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').hide()");
   }

   public void cancelarCambioCuentas() {
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaCuenta = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("formlovCuentas:lovCuentas");
      context.update("formlovCuentas:CuentasDialogo");
      context.update("formlovCuentas:aceptarCuentas");
      context.reset("formlovCuentas:lovCuentas:globalFilter");
      context.execute("PF('lovCuentas').clearFilters()");
      context.execute("PF('CuentasDialogo').hide()");
   }

   public void actualizarCuentaCredito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setCuentac(cuentaSeleccionadaLOV);
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexVigenciaCuenta = true;
         cambiosVigenciaCuenta = true;
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setCuentac(cuentaSeleccionadaLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaCreditoVC");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDesCreditoVC");
      }
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovCredito:lovCredito");
      RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
      RequestContext.getCurrentInstance().update("formlovCredito:aceptarCC");
      context.reset("formlovCredito:lovCredito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCredito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').hide()");
   }

   public void cancelarCambioCuentaCredito() {
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaCuenta = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovCredito:lovCredito");
      RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
      RequestContext.getCurrentInstance().update("formlovCredito:aceptarCC");
      context.reset("formlovCredito:lovCredito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCredito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').hide()");
   }

   public void actualizarCentroCostoDebito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setConsolidadord(centroCostoSeleccionadoLOV);
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexVigenciaCuenta = true;
         cambiosVigenciaCuenta = true;
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setConsolidadord(centroCostoSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaConsoliDebVC");
      }
      filtrarListCentrosCostos = null;
      centroCostoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:lovCentroCostoD");
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:aceptarCCD");
      context.reset("formlovCentroCostoD:lovCentroCostoD:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoD').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').hide()");
   }

   public void cancelarCambioCentroCostoDebito() {
      filtrarListCentrosCostos = null;
      centroCostoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaCuenta = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:lovCentroCostoD");
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:aceptarCCD");
      context.reset("formlovCentroCostoD:lovCentroCostoD:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoD').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').hide()");
   }

   public void actualizarCentroCostoCredito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setConsolidadorc(centroCostoSeleccionadoLOV);
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexVigenciaCuenta = true;
         cambiosVigenciaCuenta = true;

         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setConsolidadorc(centroCostoSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaConsoliCreVC");
      }
      filtrarListCentrosCostos = null;
      centroCostoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:lovCentroCostoC");
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:aceptarCCC");
      context.reset("formlovCentroCostoC:lovCentroCostoC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').hide()");
   }

   public void cancelarCambioCentroCostoCredito() {
      filtrarListCentrosCostos = null;
      centroCostoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaCuenta = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:lovCentroCostoC");
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:aceptarCCC");
      context.reset("formlovCentroCostoC:lovCentroCostoC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').hide()");
   }

   public void actualizarProceso() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setProceso(procesoSeleccionadoLOV.getSecuencia());
         vigenciaCuentaSeleccionada.setNombreProceso(procesoSeleccionadoLOV.getDescripcion());

         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambiosVigenciaCuenta = true;

         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setProceso(procesoSeleccionadoLOV.getSecuencia());
         vigCuentaCrearSeleccionada.setNombreProceso(procesoSeleccionadoLOV.getDescripcion());
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevoProceso");
      }
      cancelarCambioProceso();
   }

   public void cancelarCambioProceso() {
      filtrarlovProcesos = null;
      procesoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaCuenta = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovProceso:lovProceso");
      RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
      RequestContext.getCurrentInstance().update("formlovProceso:aceptarPro");
      context.reset("formlovProceso:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').hide()");
   }

   public void actualizarGrupoConcepto() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaGrupoCoSeleccionada.setGrupoconcepto(grupoConceptoSeleccionadoLOV);
         if (!listVigenciasGruposConceptosCrear.contains(vigenciaGrupoCoSeleccionada)) {
            if (listVigenciasGruposConceptosModificar.isEmpty()) {
               listVigenciasGruposConceptosModificar.add(vigenciaGrupoCoSeleccionada);
            } else if (!listVigenciasGruposConceptosModificar.contains(vigenciaGrupoCoSeleccionada)) {
               listVigenciasGruposConceptosModificar.add(vigenciaGrupoCoSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexVigenciaGrupoConcepto = true;
         cambiosVigenciaGrupoConcepto = true;
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoConcepto");
      } else if (tipoActualizacion == 1) {
         nuevaVigenciaGrupoConcepto.setGrupoconcepto(grupoConceptoSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoVGC");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcionVGC");
      } else if (tipoActualizacion == 2) {
         duplicarVigenciaGrupoConcepto.setGrupoconcepto(grupoConceptoSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoVGC");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionVGC");
      }
      filtrarListGruposConceptos = null;
      grupoConceptoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovGrupoConcepto:formlovGrupoConcepto:lovGrupoConcepto");
      RequestContext.getCurrentInstance().update("formlovGrupoConcepto:GruposConceptosDialogo");
      RequestContext.getCurrentInstance().update("formlovGrupoConcepto:aceptarGC");
      context.reset("formlovGrupoConcepto:formlovGrupoConcepto:lovGrupoConcepto:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGrupoConcepto').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').hide()");
   }

   public void cancelarCambioGrupoConcepto() {
      filtrarListGruposConceptos = null;
      grupoConceptoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaGrupoConcepto = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovGrupoConcepto:lovGrupoConcepto");
      RequestContext.getCurrentInstance().update("formlovGrupoConcepto:GruposConceptosDialogo");
      RequestContext.getCurrentInstance().update("formlovGrupoConcepto:aceptarGC");
      context.reset("formlovGrupoConcepto:lovGrupoConcepto:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGrupoConcepto').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GruposConceptosDialogo').hide()");
   }

   public void actualizarTipoTrabajador() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaConceptoTTSeleccionada.setTipotrabajador(tipoTrabajadorSeleccionadoLOV);
         if (!listVigenciasConceptosTTCrear.contains(vigenciaConceptoTTSeleccionada)) {
            if (listVigenciasConceptosTTModificar.isEmpty()) {
               listVigenciasConceptosTTModificar.add(vigenciaConceptoTTSeleccionada);
            } else if (!listVigenciasConceptosTTModificar.contains(vigenciaConceptoTTSeleccionada)) {
               listVigenciasConceptosTTModificar.add(vigenciaConceptoTTSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexVigenciaConceptoTT = true;
         cambiosVigenciaConceptoTT = true;
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTT");
      } else if (tipoActualizacion == 1) {
         nuevaVigenciaConceptoTT.setTipotrabajador(tipoTrabajadorSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTrabajadorVCTT");
      } else if (tipoActualizacion == 2) {
         duplicarVigenciaConceptoTT.setTipotrabajador(tipoTrabajadorSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTrabajadorVCTT");
      }
      filtrarListTiposTrabajadores = null;
      tipoTrabajadorSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovTipoTrabajador:lovTipoTrabajador");
      RequestContext.getCurrentInstance().update("formlovTipoTrabajador:TipoTrabajadorDialogo");
      RequestContext.getCurrentInstance().update("formlovTipoTrabajador:aceptarTTr");
      context.reset("formlovTipoTrabajador:lovTipoTrabajador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
   }

   public void cancelarCambioTipoTrabajador() {
      filtrarListTiposTrabajadores = null;
      tipoTrabajadorSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaConceptoTT = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovTipoTrabajador:lovTipoTrabajador");
      RequestContext.getCurrentInstance().update("formlovTipoTrabajador:TipoTrabajadorDialogo");
      RequestContext.getCurrentInstance().update("formlovTipoTrabajador:aceptarTTr");
      context.reset("formlovTipoTrabajador:lovTipoTrabajador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
   }

   public void actualizarTipoContrato() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaConceptoTCSeleccionada.setTipocontrato(tipoContratoSeleccionadoLOV);
         if (!listVigenciasConceptosTCCrear.contains(vigenciaConceptoTCSeleccionada)) {
            if (listVigenciasConceptosTCModificar.isEmpty()) {
               listVigenciasConceptosTCModificar.add(vigenciaConceptoTCSeleccionada);
            } else if (!listVigenciasConceptosTCModificar.contains(vigenciaConceptoTCSeleccionada)) {
               listVigenciasConceptosTCModificar.add(vigenciaConceptoTCSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexVigenciaConceptoTC = true;
         cambiosVigenciaConceptoTC = true;

         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoTC");
      } else if (tipoActualizacion == 1) {
         nuevaVigenciaConceptoTC.setTipocontrato(tipoContratoSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaContratoVCTC");
      } else if (tipoActualizacion == 2) {
         duplicarVigenciaConceptoTC.setTipocontrato(tipoContratoSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarContratoVCTC");
      }
      filtrarListTiposContratos = null;
      tipoContratoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovTipoContrato:lovTipoContrato");
      RequestContext.getCurrentInstance().update("formlovTipoContrato:TipoContratosDialogo");
      RequestContext.getCurrentInstance().update("formlovTipoContrato:aceptarTCt");
      context.reset("formlovTipoContrato:lovTipoContrato:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoContrato').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoContratosDialogo').hide()");
   }

   public void cancelarCambioTipoContrato() {
      filtrarListTiposContratos = null;
      tipoContratoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaConceptoTC = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovTipoContrato:lovTipoContrato");
      RequestContext.getCurrentInstance().update("formlovTipoContrato:TipoContratosDialogo");
      RequestContext.getCurrentInstance().update("formlovTipoContrato:aceptarTCt");
      context.reset("formlovTipoContrato:lovTipoContrato:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoContrato').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoContratosDialogo').hide()");
   }

   public void actualizarReformaLaboral() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaConceptoRLSeleccionada.setTiposalario(reformaLaboralSeleccionadoLOV);
         if (!listVigenciasConceptosRLCrear.contains(vigenciaConceptoRLSeleccionada)) {
            if (listVigenciasConceptosRLModificar.isEmpty()) {
               listVigenciasConceptosRLModificar.add(vigenciaConceptoRLSeleccionada);
            } else if (!listVigenciasConceptosRLModificar.contains(vigenciaConceptoRLSeleccionada)) {
               listVigenciasConceptosRLModificar.add(vigenciaConceptoRLSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexVigenciaConceptoRL = true;
         cambiosVigenciaConceptoRL = true;
         RequestContext.getCurrentInstance().update("form:datosVigenciaConceptoRL");
      } else if (tipoActualizacion == 1) {
         nuevaVigenciaConceptoRL.setTiposalario(reformaLaboralSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaReformaVCRL");
      } else if (tipoActualizacion == 2) {
         duplicarVigenciaConceptoRL.setTiposalario(reformaLaboralSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarReformaVCRL");
      }
      filtrarListReformasLaborales = null;
      reformaLaboralSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovReformaLaboral:lovReformaLaboral");
      RequestContext.getCurrentInstance().update("formlovReformaLaboral:ReformaLaboralDialogo");
      RequestContext.getCurrentInstance().update("formlovReformaLaboral:aceptarRL");
      context.reset("formlovReformaLaboral:lovReformaLaboral:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReformaLaboral').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').hide()");
   }

   public void cancelarCambioReformaLaboral() {
      filtrarListReformasLaborales = null;
      reformaLaboralSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexVigenciaConceptoRL = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovReformaLaboral:lovReformaLaboral");
      RequestContext.getCurrentInstance().update("formlovReformaLaboral:ReformaLaboralDialogo");
      RequestContext.getCurrentInstance().update("formlovReformaLaboral:aceptarRL");
      context.reset("formlovReformaLaboral:lovReformaLaboral:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReformaLaboral').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').hide()");
   }

   public void actualizarFormula() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigFormulaConceptoSeleccionada.setFormula(formulaSeleccionadoLOV.getSecuencia());
         vigFormulaConceptoSeleccionada.setNombreFormula(formulaSeleccionadoLOV.getNombrelargo());
         if (!listFormulasConceptosCrear.contains(vigFormulaConceptoSeleccionada)) {
            if (listFormulasConceptosModificar.isEmpty()) {
               listFormulasConceptosModificar.add(vigFormulaConceptoSeleccionada);
            } else if (!listFormulasConceptosModificar.contains(vigFormulaConceptoSeleccionada)) {
               listFormulasConceptosModificar.add(vigFormulaConceptoSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexFormulasConceptos = true;
         cambiosFormulasConceptos = true;
         RequestContext.getCurrentInstance().update("form:editarNombreFC");
      } else if (tipoActualizacion == 1) {
         nuevaFormulasConceptos.setFormula(formulaSeleccionadoLOV.getSecuencia());
         nuevaFormulasConceptos.setNombreFormula(formulaSeleccionadoLOV.getNombrelargo());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormulaFC");
      } else if (tipoActualizacion == 2) {
         duplicarFormulasConceptos.setFormula(formulaSeleccionadoLOV.getSecuencia());
         duplicarFormulasConceptos.setNombreFormula(formulaSeleccionadoLOV.getNombrelargo());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaFC");
      }
      filtrarListFormulas = null;
      formulaSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("formlovFormula:lovFormula");
      RequestContext.getCurrentInstance().update("formlovFormula:FormulasDialogo");
      RequestContext.getCurrentInstance().update("formlovFormula:aceptarF");
      context.reset("formlovFormula:lovFormula:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').hide()");
   }

   public void cancelarCambioFormula() {
      filtrarListFormulas = null;
      formulaSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexFormulasConceptos = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovFormula:lovFormula");
      RequestContext.getCurrentInstance().update("formlovFormula:FormulasDialogo");
      RequestContext.getCurrentInstance().update("formlovFormula:aceptarF");
      context.reset("formlovFormula:lovFormula:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').hide()");
   }

   public void actualizarOrden() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoListaFormulasConceptos == 0) {
         vigFormulaConceptoSeleccionada.setStrOrden(formulaConceptoSeleccionadoLOV.getStrOrden());
         if (!listFormulasConceptosCrear.contains(vigFormulaConceptoSeleccionada)) {
            if (listFormulasConceptosModificar.isEmpty()) {
               listFormulasConceptosModificar.add(vigFormulaConceptoSeleccionada);
            } else if (!listFormulasConceptosModificar.contains(vigFormulaConceptoSeleccionada)) {
               listFormulasConceptosModificar.add(vigFormulaConceptoSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexFormulasConceptos = true;
         cambiosFormulasConceptos = true;

         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      } else if (tipoActualizacion == 1) {
         nuevaFormulasConceptos.setStrOrden(formulaConceptoSeleccionadoLOV.getStrOrden());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaOrdenFC");
      } else if (tipoActualizacion == 2) {
         duplicarFormulasConceptos.setStrOrden(formulaConceptoSeleccionadoLOV.getStrOrden());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOrdenFC");
      }
      filtrarListFormulasConceptos = null;
      formulaConceptoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      context.reset("formlovFormulaC:lovFormulaC");
      context.reset("formlovFormulaC:FormulaConceptoDialogo");
      context.reset("formlovFormulaC:aceptarFC");
      context.reset("formlovFormulaC:lovFormulaC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulaC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulaConceptoDialogo').hide()");
   }

   public void cancelarCambioOrden() {
      filtrarListFormulasConceptos = null;
      formulaConceptoSeleccionadoLOV = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexFormulasConceptos = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formlovFormulaC:lovFormulaC");
      context.reset("formlovFormulaC:FormulaConceptoDialogo");
      context.reset("formlovFormulaC:aceptarFC");
      context.reset("formlovFormulaC:lovFormulaC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulaC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulaConceptoDialogo').hide()");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   /**
    * Selecciona la tabla a exportar XML con respecto al index activo
    *
    * @return Nombre del dialogo a exportar en XML
    */
   public String exportXML() {
      if (vigenciaCuentaSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasCuentas:datosVigenciaCuentasExportar";
         nombreXML = "VigenciasCuentas_XML";
      } else if (vigenciaGrupoCoSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasGruposConceptos:datosVigenciaGrupoConceptoExportar";
         nombreXML = "VigenciasGruposConceptos_XML";
      } else if (vigenciaConceptoTTSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasConceptosTT:datosVigenciaConceptoTTExportar";
         nombreXML = "VigenciasConceptosTT_XML";
      } else if (vigenciaConceptoTCSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasConceptosTC:datosVigenciaConceptoTCExportar";
         nombreXML = "VigenciasConceptosTC_XML";
      } else if (vigenciaConceptoRLSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasConceptosRL:datosVigenciaConceptoRLExportar";
         nombreXML = "VigenciasConceptosRL_XML";
      } else if (vigFormulaConceptoSeleccionada != null) {
         nombreTabla = ":formExportarFormulasConceptos:datosFormulasConceptosExportar";
         nombreXML = "FormulasConceptos_XML";
         formulaSeleccionada = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
      return nombreTabla;
   }

   /**
    * Valida la tabla a exportar en PDF con respecto al index activo
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      if (vigenciaCuentaSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasCuentas:datosVigenciaCuentasExportar";
         nombreExportar = "VigenciasCuentas_PDF";
         exportPDF_Tabla();
      } else if (vigenciaGrupoCoSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasGruposConceptos:datosVigenciaGrupoConceptoExportar";
         nombreExportar = "VigenciasGruposConceptos_PDF";
         exportPDF_Tabla();
      } else if (vigenciaConceptoTTSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasConceptosTT:datosVigenciaConceptoTTExportar";
         nombreExportar = "VigenciasConceptosTT_PDF";
         exportPDF_Tabla();
      } else if (vigenciaConceptoTCSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasConceptosTC:datosVigenciaConceptoTCExportar";
         nombreExportar = "VigenciasConceptosTC_PDF";
         exportPDF_Tabla();
      } else if (vigenciaConceptoRLSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasConceptosRL:datosVigenciaConceptoRLExportar";
         nombreExportar = "VigenciasConceptosRL_PDF";
         exportPDF_Tabla();
      } else if (vigFormulaConceptoSeleccionada != null) {
         nombreTabla = ":formExportarFormulasConceptos:datosFormulasConceptosExportar";
         nombreExportar = "FormulasConceptos_PDF";
         exportPDF_Tabla();
         formulaSeleccionada = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   /**
    * Metodo que exporta datos a PDF Vigencia Localizacion
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDF_Tabla() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(nombreTabla);
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, nombreExportar, false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Verifica que tabla exportar XLS con respecto al index activo
    *
    * @throws IOException
    */
   public void verificarExportXLS() throws IOException {
      if (vigenciaCuentaSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasCuentas:datosVigenciaCuentasExportar";
         nombreExportar = "VigenciasCuentas_XLS";
         exportXLS_Tabla();
      } else if (vigenciaGrupoCoSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasGruposConceptos:datosVigenciaGrupoConceptoExportar";
         nombreExportar = "VigenciasGruposConceptos_XLS";
         exportXLS_Tabla();
      } else if (vigenciaConceptoTTSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasConceptosTT:datosVigenciaConceptoTTExportar";
         nombreExportar = "VigenciasConceptosTT_XLS";
         exportXLS_Tabla();
      } else if (vigenciaConceptoTCSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasConceptosTC:datosVigenciaConceptoTCExportar";
         nombreExportar = "VigenciasConceptosTC_XLS";
         exportXLS_Tabla();
      } else if (vigenciaConceptoRLSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasConceptosRL:datosVigenciaConceptoRLExportar";
         nombreExportar = "VigenciasConceptosRL_XLS";
         exportXLS_Tabla();
      } else if (vigFormulaConceptoSeleccionada != null) {
         nombreTabla = ":formExportarFormulasConceptos:datosFormulasConceptosExportar";
         nombreExportar = "FormulasConceptos_XLS";
         exportXLS_Tabla();
         formulaSeleccionada = true;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:detalleFormula");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Sueldos
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS_Tabla() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(nombreTabla);
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, nombreExportar, false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista real a la filtrada
    */
   public void eventoFiltrarCuenta() {
      if (tipoListaVigenciaCuenta == 0) {
         tipoListaVigenciaCuenta = 1;
      }
      vigenciaCuentaSeleccionada = null;
      contarRegistrosCuentas();
   }

   public void eventoFiltrarGrupo() {
      if (tipoListaVigenciaGrupoConcepto == 0) {
         tipoListaVigenciaGrupoConcepto = 1;
      }
      vigenciaGrupoCoSeleccionada = null;
      contarRegistrosGrupoC();
   }

   public void eventoFiltrarConceptoTT() {
      if (tipoListaVigenciaConceptoTT == 0) {
         tipoListaVigenciaConceptoTT = 1;
      }
      vigenciaConceptoTTSeleccionada = null;
      contarRegistrosConceptoTT();
   }

   public void eventoFiltrarConceptoTC() {
      if (tipoListaVigenciaConceptoTC == 0) {
         tipoListaVigenciaConceptoTC = 1;
      }
      vigenciaConceptoTCSeleccionada = null;
      contarRegistrosConceptoTC();
   }

   public void eventoFiltrarConceptoRL() {
      if (tipoListaVigenciaConceptoRL == 0) {
         tipoListaVigenciaConceptoRL = 1;
      }
      vigenciaConceptoRLSeleccionada = null;
      contarRegistrosConceptoRL();
   }

   public void eventoFiltrarFormulas() {
      formulaSeleccionada = true;
      if (tipoListaFormulasConceptos == 0) {
         tipoListaFormulasConceptos = 1;
      }
      vigFormulaConceptoSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      contarRegistrosFormulaConcepto();
   }

   public void verificarRastroTabla() {
      if (vigenciaCuentaSeleccionada != null) {
         verificarRastroVigenciaCuenta();
      } else if (vigenciaGrupoCoSeleccionada != null) {
         verificarRastroVigenciaGrupoConcepto();
      } else if (vigenciaConceptoTTSeleccionada != null) {
         verificarRastroVigenciaConceptoTT();
      } else if (vigenciaConceptoTCSeleccionada != null) {
         verificarRastroVigenciaConceptoTC();
      } else if (vigenciaConceptoRLSeleccionada != null) {
         verificarRastroVigenciaConceptoRL();
      } else if (vigFormulaConceptoSeleccionada != null) {
         verificarRastroFormulasConceptos();
         formulaSeleccionada = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
         //Si ninguno esta seleccionado pregunta por historicos
      } else {
         RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
      }
   }

   public void verificarRastroVigenciaCuenta() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(vigenciaCuentaSeleccionada.getSecuencia(), "VIGENCIASCUENTAS");
      backUp = vigenciaCuentaSeleccionada.getSecuencia();
      vigenciaCuentaSeleccionada = null;
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "VigenciasCuentas";
         msnConfirmarRastro = "La tabla VIGENCIASCUENTAS tiene rastros para el registro seleccionado, ¿desea continuar?";
         RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
      } else if (resultado == 3) {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
      } else if (resultado == 4) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
      } else if (resultado == 5) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      }
   }

   public void verificarRastroVigenciaCuentaHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("VIGENCIASCUENTAS")) {
         nombreTablaRastro = "VigenciasCuentas";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASCUENTAS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroVigenciaGrupoConcepto() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(vigenciaGrupoCoSeleccionada.getSecuencia(), "VIGENCIASGRUPOSCONCEPTOS");
      backUp = vigenciaGrupoCoSeleccionada.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "VigenciasGruposConceptos";
         msnConfirmarRastro = "La tabla VIGENCIASGRUPOSCONCEPTOS tiene rastros para el registro seleccionado, ¿desea continuar?";
         RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
      } else if (resultado == 3) {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
      } else if (resultado == 4) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
      } else if (resultado == 5) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      }
   }

   public void verificarRastroVigenciaGrupoConceptoHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("VIGENCIASGRUPOSCONCEPTOS")) {
         nombreTablaRastro = "VigenciasGruposConceptos";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASGRUPOSCONCEPTOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroVigenciaConceptoTT() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(vigenciaConceptoTTSeleccionada.getSecuencia(), "VIGENCIASCONCEPTOSTT");
      backUp = vigenciaConceptoTTSeleccionada.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "VigenciasConceptosTT";
         msnConfirmarRastro = "La tabla VIGENCIASCONCEPTOSTT tiene rastros para el registro seleccionado, ¿desea continuar?";
         RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
      } else if (resultado == 3) {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
      } else if (resultado == 4) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
      } else if (resultado == 5) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      }
   }

   public void verificarRastroVigenciaConceptoTTHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("VIGENCIASCONCEPTOSTT")) {
         nombreTablaRastro = "VigenciasConceptosTT";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASCONCEPTOSTT tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroVigenciaConceptoTC() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(vigenciaConceptoTCSeleccionada.getSecuencia(), "VIGENCIASCONCEPTOSTC");
      backUp = vigenciaConceptoTCSeleccionada.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "VigenciasConceptosTC";
         msnConfirmarRastro = "La tabla VIGENCIASCONCEPTOSTC tiene rastros para el registro seleccionado, ¿desea continuar?";
         RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
      } else if (resultado == 3) {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
      } else if (resultado == 4) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
      } else if (resultado == 5) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      }
   }

   public void verificarRastroVigenciaConceptoTCHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("VIGENCIASCONCEPTOSTC")) {
         nombreTablaRastro = "VigenciasConceptosTC";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASCONCEPTOSTC tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroVigenciaConceptoRL() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(vigenciaConceptoRLSeleccionada.getSecuencia(), "VIGENCIASCONCEPTOSRL");
      backUp = vigenciaConceptoRLSeleccionada.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "VigenciasConceptosRL";
         msnConfirmarRastro = "La tabla VIGENCIASCONCEPTOSRL tiene rastros para el registro seleccionado, ¿desea continuar?";
         RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
      } else if (resultado == 3) {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
      } else if (resultado == 4) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
      } else if (resultado == 5) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      }
   }

   public void verificarRastroVigenciaConceptoRLHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("VIGENCIASCONCEPTOSRL")) {
         nombreTablaRastro = "VigenciasConceptosRL";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASCONCEPTOSRL tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroFormulasConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(vigFormulaConceptoSeleccionada.getSecuencia(), "FORMULASCONCEPTOS");
      backUp = vigFormulaConceptoSeleccionada.getSecuencia();
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "FormulasConceptos";
         msnConfirmarRastro = "La tabla FORMULASCONCEPTOS tiene rastros para el registro seleccionado, ¿desea continuar?";
         RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
      } else if (resultado == 3) {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
      } else if (resultado == 4) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
      } else if (resultado == 5) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      }
   }

   public void verificarRastroFormulasConceptosHist() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("FORMULASCONCEPTOS")) {
         nombreTablaRastro = "FormulasConceptos";
         msnConfirmarRastroHistorico = "La tabla FORMULASCONCEPTOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   public void validarEliminacionConcepto() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean retorno = administrarDetalleConcepto.verificarSolucionesNodosConcepto(conceptoActual.getSecuencia());
      if (retorno == true) {
         RequestContext.getCurrentInstance().execute("PF('errorEliminacionConcepto').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('paso1Eliminacion').show()");
      }
   }

   public void eliminarConcepto() {
      boolean rep = administrarDetalleConcepto.eliminarConceptoTotal(conceptoActual.getSecuencia());
      if (rep == true) {
         salir();
      } else {
         FacesMessage msg = new FacesMessage("Información", "El reporte no pudo ser eliminado.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
      }
   }

   public String conceptoAEliminar() {
      if (vigFormulaConceptoSeleccionada != null) {
         conceptoEliminar = "Va a eliminar el concepto el cual tiene el código : " + vigFormulaConceptoSeleccionada.getCodigoConcepto().toString() + ".¿Esta seguro?";
      }
      return conceptoEliminar;
   }

   public void recargarVigenciaCuentaDefault() {
      altoTablaVigenciaCuenta = "105";
      FacesContext c = FacesContext.getCurrentInstance();
      vigenciaCuentaFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaFechaInicial");
      vigenciaCuentaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaFechaFinal");
      vigenciaCuentaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaTipoCC = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaTipoCC");
      vigenciaCuentaTipoCC.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaDebitoCod = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaDebitoCod");
      vigenciaCuentaDebitoCod.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaDebitoDes = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaDebitoDes");
      vigenciaCuentaDebitoDes.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaCCConsolidadorD = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCConsolidadorD");
      vigenciaCuentaCCConsolidadorD.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaCreditoCod = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCreditoCod");
      vigenciaCuentaCreditoCod.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaCreditoDes = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCreditoDes");
      vigenciaCuentaCreditoDes.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaCCConsolidadorC = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCConsolidadorC");
      vigenciaCuentaCCConsolidadorC.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaCCProceso = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCProceso");
      vigenciaCuentaCCProceso.setFilterStyle("display: none; visibility: hidden;");
      banderaVigenciaCuenta = 0;
      filtrarListVigenciasCuentasConcepto = null;
      tipoListaVigenciaCuenta = 0;
   }

   public void recargarVigenciaGrupoCDefault() {
      altoTablaVigenciaGrupoC = "120";
      FacesContext c = FacesContext.getCurrentInstance();
      vigenciaGCFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoConcepto:vigenciaGCFechaInicial");
      vigenciaGCFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      vigenciaGCFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoConcepto:vigenciaGCFechaFinal");
      vigenciaGCFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      vigenciaGCCodigo = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoConcepto:vigenciaGCCodigo");
      vigenciaGCCodigo.setFilterStyle("display: none; visibility: hidden;");
      vigenciaGCDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoConcepto:vigenciaGCDescripcion");
      vigenciaGCDescripcion.setFilterStyle("display: none; visibility: hidden;");
      banderaVigenciaGrupoConcepto = 0;
      filtrarListVigenciasGruposConceptos = null;
      tipoListaVigenciaGrupoConcepto = 0;
   }

   public void recargarVigenciaConceptoTT() {
      altoTablaVigenciaConceptoTT = "105";
      FacesContext c = FacesContext.getCurrentInstance();
      vigenciaCTTFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTT:vigenciaCTTFechaFinal");
      vigenciaCTTFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCTTFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTT:vigenciaCTTFechaInicial");
      vigenciaCTTFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCTTDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTT:vigenciaCTTDescripcion");
      vigenciaCTTDescripcion.setFilterStyle("display: none; visibility: hidden;");
      banderaVigenciaConceptoTT = 0;
      filtrarListVigenciasConceptosTT = null;
      tipoListaVigenciaConceptoTT = 0;
   }

   public void recargarVigenciaConceptoRT() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTablaVigenciaConceptoRL = "105";
      vigenciaCRLFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoRL:vigenciaCRLFechaFinal");
      vigenciaCRLFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCRLFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoRL:vigenciaCRLFechaInicial");
      vigenciaCRLFechaInicial.setFilterStyle("display: none; visibililty: hidden;");
      vigenciaCRLDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoRL:vigenciaCRLDescripcion");
      vigenciaCRLDescripcion.setFilterStyle("display: none; visibility: hidden;");
      banderaVigenciaConceptoRL = 0;
      filtrarListVigenciasConceptosRL = null;
      tipoListaVigenciaConceptoRL = 0;
   }

   public void recargarFormulaConcepto() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTablaFormulaConcepto = "122";
      formulaCFechaInicial = (Column) c.getViewRoot().findComponent("form:datosFormulaConcepto:formulaCFechaInicial");
      formulaCFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      formulaCFechaFinal = (Column) c.getViewRoot().findComponent("form:datosFormulaConcepto:formulaCFechaFinal");
      formulaCFechaFinal.setFilterStyle("display: none; visibililty: hidden;");
      formulaCNombre = (Column) c.getViewRoot().findComponent("form:datosFormulaConcepto:formulaCNombre");
      formulaCNombre.setFilterStyle("display: none; visibility: hidden;");
      formulaCOrden = (Column) c.getViewRoot().findComponent("form:datosFormulaConcepto:formulaCOrden");
      formulaCOrden.setFilterStyle("display: none; visibility: hidden;");
      banderaFormulasConceptos = 0;
      filtrarListFormulasConceptosConcepto = null;
      tipoListaFormulasConceptos = 0;
   }

   public void recargarVigenciaConceptoTC() {
      altoTablaVigenciaConceptoTC = "105";
      FacesContext c = FacesContext.getCurrentInstance();
      vigenciaCTCFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTC:vigenciaCTCFechaFinal");
      vigenciaCTCFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCTCFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTC:vigenciaCTCFechaInicial");
      vigenciaCTCFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCTCDescripcion = (Column) c.getViewRoot().findComponent("form:datosVigenciaConceptoTC:vigenciaCTCDescripcion");
      vigenciaCTCDescripcion.setFilterStyle("display: none; visibility: hidden;");
      banderaVigenciaConceptoTC = 0;
      filtrarListVigenciasConceptosTC = null;
      tipoListaVigenciaConceptoTC = 0;
   }

   public void recordarSeleccionesC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaCuenta");
      if (vigenciaCuentaSeleccionada != null) {
         tabla.setSelection(vigenciaCuentaSeleccionada);
      }
   }

   public void recordarSeleccionesGC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaGrupoConcepto");
      if (vigenciaGrupoCoSeleccionada != null) {
         tabla.setSelection(vigenciaGrupoCoSeleccionada);
      }
   }

   public void recordarSeleccionesRL() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaConceptoRL");
      if (vigenciaConceptoRLSeleccionada != null) {
         tabla.setSelection(vigenciaConceptoRLSeleccionada);
      }
   }

   public void recordarSeleccionesTT() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaConceptoTT");
      if (vigenciaConceptoTTSeleccionada != null) {
         tabla.setSelection(vigenciaConceptoTTSeleccionada);
      }
   }

   public void recordarSeleccionesTC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaConceptoTC");
      if (vigenciaConceptoTCSeleccionada != null) {
         tabla.setSelection(vigenciaConceptoTCSeleccionada);
      }
   }

   public void recordarSeleccionesFC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFormulaConcepto");
      if (vigFormulaConceptoSeleccionada != null) {
         tabla.setSelection(vigFormulaConceptoSeleccionada);
      }
   }

   ////////////Listas Valores VigenciasCuentas/////////////
   public void cargarLOVsVCuentas() {
      log.info("Entro en cargarLOVs()");
      if (lovTiposCentrosCostos == null) {
         lovTiposCentrosCostos = administrarDetalleConcepto.consultarLOVTiposCentrosCostos();
         log.info("cargo lovTiposCentrosCostos");
      }
      if (lovCuentas == null) {
         if (listasRecurrentes.getLovCuentas().isEmpty()) {
            lovCuentas = administrarDetalleConcepto.consultarLOVCuentas();
            if (lovCuentas != null) {
               listasRecurrentes.setLovCuentas(lovCuentas);
            }
         } else {
            lovCuentas = new ArrayList<Cuentas>(listasRecurrentes.getLovCuentas());
         }
         log.info("cargo lovCuentas");
      }
      if (lovCentrosCostos == null) {
         if (listasRecurrentes.getLovCentrosCostos().isEmpty()) {
            lovCentrosCostos = administrarDetalleConcepto.consultarLOVCentrosCostos();
            if (lovCentrosCostos != null) {
               listasRecurrentes.setLovCentrosCostos(lovCentrosCostos);
            }
         } else {
            lovCentrosCostos = new ArrayList<CentrosCostos>(listasRecurrentes.getLovCentrosCostos());
         }
         log.info("cargo lovCentrosCostos");
      }
      if (lovProcesos == null) {
         if (listasRecurrentes.getLovProcesos().isEmpty()) {
            lovProcesos = administrarDetalleConcepto.consultarLOVProcesos();
            if (lovProcesos != null) {
               listasRecurrentes.setLovProcesos(lovProcesos);
            }
         } else {
            lovProcesos = new ArrayList<Procesos>(listasRecurrentes.getLovProcesos());
         }
         log.info("cargo lovProcesos");
      }
   }

   /////////////Lista Valores VigenciaGrupoConcepto///////////////////////
   public void cargarLOVsVGrupoConcepto() {
      if (lovGruposConceptos == null) {
         if (listasRecurrentes.getLovGruposConceptos().isEmpty()) {
            lovGruposConceptos = administrarDetalleConcepto.consultarLOVGruposConceptos();
            if (lovGruposConceptos != null) {
               listasRecurrentes.setLovGruposConceptos(lovGruposConceptos);
            }
         } else {
            lovGruposConceptos = new ArrayList<GruposConceptos>(listasRecurrentes.getLovGruposConceptos());
         }
         log.info("cargo lovGruposConceptos");
      }
   }

   /////////////Lista Valores VigenciasConceptosTT///////////////////////
   public void cargarLOVsVConceptosTT() {
      if (lovTiposTrabajadores == null) {
         if (listasRecurrentes.getLovTiposTrabajadores().isEmpty()) {
            lovTiposTrabajadores = administrarDetalleConcepto.consultarLOVTiposTrabajadores();
            if (lovTiposTrabajadores != null) {
               listasRecurrentes.setLovTiposTrabajadores(lovTiposTrabajadores);
            }
         } else {
            lovTiposTrabajadores = new ArrayList<TiposTrabajadores>(listasRecurrentes.getLovTiposTrabajadores());
         }
         log.info("cargo lovTiposTrabajadores");
      }
   }

   public void cargarLOVsVConceptosTC() {
      /////////////Lista Valores VigenciasConceptosTC///////////////////////
      if (lovTiposContratos == null) {
         lovTiposContratos = administrarDetalleConcepto.consultarLOVTiposContratos();
         log.info("cargo lovTiposContratos");
      }
   }

   /////////////Lista Valores VigenciasConceptosRL///////////////////////
   public void cargarLOVsVConceptosRL() {
      if (lovReformasLaborales == null) {
         lovReformasLaborales = administrarDetalleConcepto.consultarLOVReformasLaborales();
         log.info("cargo lovReformasLaborales");
      }
   }

   /////////////Lista Valores FormulasConceptos///////////////////////
   public void cargarLOVsFormulasConceptos() {
      if (lovFormulas == null) {
         if (listasRecurrentes.getLovFormulas().isEmpty()) {
            lovFormulas = administrarDetalleConcepto.consultarLOVFormulas();
            if (lovFormulas != null) {
               listasRecurrentes.setLovFormulas(lovFormulas);
            }
            log.warn("SE AGREGA A listasRecurrentes lovFormulas");
         } else {
            lovFormulas = new ArrayList<Formulas>(listasRecurrentes.getLovFormulas());
            log.warn("SE CONSULTA DE listasRecurrentes lovFormulas");
         }
      }
      if (lovFormulasConceptos == null) {
         if (listasRecurrentes.getLovFormulasConceptos().isEmpty()) {
            lovFormulasConceptos = administrarDetalleConcepto.consultarLOVFormulasConceptos();
            if (lovFormulasConceptos != null) {
               listasRecurrentes.setLovFormulasConceptos(lovFormulasConceptos);
            }
            log.warn("SE AGREGA A listasRecurrentes lovFormulasConceptos");
         } else {
            lovFormulasConceptos = new ArrayList<FormulasConceptos>(listasRecurrentes.getLovFormulasConceptos());
            log.warn("SE CONSULTA DE listasRecurrentes lovFormulasConceptos");
         }
         log.info("cargo lovFormulasConceptos");
      }
   }

//Conteo de registros: 
   public void contarRegistrosCuentas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCuenta");
   }

   public void contarRegistrosGrupoC() {
      RequestContext.getCurrentInstance().update("form:infoRegistroGrupoC");
   }

   public void contarRegistrosConceptoTT() {
      RequestContext.getCurrentInstance().update("form:infoRegistroConceptoTT");
   }

   public void contarRegistrosConceptoTC() {
      RequestContext.getCurrentInstance().update("form:infoRegistroConceptoTC");
   }

   public void contarRegistrosConceptoRL() {
      RequestContext.getCurrentInstance().update("form:infoRegistroConceptoRL");
   }

   public void contarRegistrosFormulaConcepto() {
      RequestContext.getCurrentInstance().update("form:infoRegistroFormulaConcepto");
   }

   //Conteo de Registros Listas de Valor:
   public void contarRegistrosLovTipoCentroCosto() {
      RequestContext.getCurrentInstance().update("formlovTiposCC:infoRegistroLovTipoCentroCosto");
   }

   public void contarRegistrosLovCuentaDebito() {
      RequestContext.getCurrentInstance().update("formlovDebito:infoRegistroLovCuentaDebito");
   }

   public void contarRegistrosLovCuentas() {
      RequestContext.getCurrentInstance().update("formlovCuentas:infoRegistroLovCuentas");
   }

   public void contarRegistrosLovCuentaCredito() {
      RequestContext.getCurrentInstance().update("formlovCredito:infoRegistroLovCuentaCredito");
   }

   public void contarRegistrosLovProcesos() {
      RequestContext.getCurrentInstance().update("formlovProceso:infoRegistroLovProcesos");
   }

   public void contarRegistrosLovCentroCostoDebito() {
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:infoRegistroLovCentroCostoDebito");
   }

   public void contarRegistrosLovCentroCostoCredito() {
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:infoRegistroLovCentroCostoCredito");
   }

   public void contarRegistrosLovGrupoConcepto() {
      RequestContext.getCurrentInstance().update("formlovGrupoConcepto:infoRegistroLovGrupoConcepto");
   }

   public void contarRegistrosLovTipoTrabajador() {
      RequestContext.getCurrentInstance().update("formlovTipoTrabajador:infoRegistroLovTipoTrabajador");
   }

   public void contarRegistrosLovTipoContrato() {
      RequestContext.getCurrentInstance().update("formlovTipoContrato:infoRegistroLovTipoContrato");
   }

   public void contarRegistrosLovReformaLaboral() {
      RequestContext.getCurrentInstance().update("formlovReformaLaboral:infoRegistroLovReformaLaboral");
   }

   public void contarRegistrosLovFormula() {
      RequestContext.getCurrentInstance().update("formlovFormula:infoRegistroLovFormula");
   }

   public void contarRegistrosLovFormCon() {
      RequestContext.getCurrentInstance().update("formlovFormulaC:infoRegistroLovFormulasConceptos");
   }

   //GET - SET 
   public boolean isFormulaSeleccionada() {
      return formulaSeleccionada;
   }

   public void setFormulaSeleccionada(boolean formulaSeleccionada) {
      this.formulaSeleccionada = formulaSeleccionada;
   }

   public VigenciasCuentas getVigenciaCuentaSeleccionada() {
      return vigenciaCuentaSeleccionada;
   }

   public void setVigenciaCuentaSeleccionada(VigenciasCuentas vigenciaCuentaTablaSeleccionada) {
      this.vigenciaCuentaSeleccionada = vigenciaCuentaTablaSeleccionada;
   }

   public VigenciasGruposConceptos getVigenciaGrupoCoSeleccionada() {
      return vigenciaGrupoCoSeleccionada;
   }

   public void setVigenciaGrupoCoSeleccionada(VigenciasGruposConceptos vigenciaGrupoConceptoTablaSeleccionada) {
      this.vigenciaGrupoCoSeleccionada = vigenciaGrupoConceptoTablaSeleccionada;
   }

   public VigenciasConceptosTT getVigenciaConceptoTTSeleccionada() {
      return vigenciaConceptoTTSeleccionada;
   }

   public void setVigenciaConceptoTTSeleccionada(VigenciasConceptosTT vigenciaConceptoTTSeleccionada) {
      this.vigenciaConceptoTTSeleccionada = vigenciaConceptoTTSeleccionada;
   }

   public VigenciasConceptosTC getVigenciaConceptoTCSeleccionada() {
      return vigenciaConceptoTCSeleccionada;
   }

   public void setVigenciaConceptoTCSeleccionada(VigenciasConceptosTC vigenciaConceptoTCSeleccionada) {
      this.vigenciaConceptoTCSeleccionada = vigenciaConceptoTCSeleccionada;
   }

   public VigenciasConceptosRL getVigenciaConceptoRLSeleccionada() {
      return vigenciaConceptoRLSeleccionada;
   }

   public void setVigenciaConceptoRLSeleccionada(VigenciasConceptosRL vigenciaConceptoRLTablaSeleccionada) {
      this.vigenciaConceptoRLSeleccionada = vigenciaConceptoRLTablaSeleccionada;
   }

   public FormulasConceptos getVigFormulaConceptoSeleccionada() {
      return vigFormulaConceptoSeleccionada;
   }

   public void setVigFormulaConceptoSeleccionada(FormulasConceptos formulaConceptoTablaSeleccionada) {
      this.vigFormulaConceptoSeleccionada = formulaConceptoTablaSeleccionada;
   }

   public FormulasConceptos getFormulaConceptoSeleccionadoLOV() {
      return formulaConceptoSeleccionadoLOV;
   }

   public void setFormulaConceptoSeleccionadoLOV(FormulasConceptos formulaConceptoSeleccionado) {
      this.formulaConceptoSeleccionadoLOV = formulaConceptoSeleccionado;
   }

   public Formulas getFormulaSeleccionadoLOV() {
      return formulaSeleccionadoLOV;
   }

   public void setFormulaSeleccionadoLOV(Formulas formulaSeleccionado) {
      this.formulaSeleccionadoLOV = formulaSeleccionado;
   }

   public ReformasLaborales getReformaLaboralSeleccionadoLOV() {
      return reformaLaboralSeleccionadoLOV;
   }

   public void setReformaLaboralSeleccionadoLOV(ReformasLaborales reformaLaboralSeleccionado) {
      this.reformaLaboralSeleccionadoLOV = reformaLaboralSeleccionado;
   }

   public TiposTrabajadores getTipoTrabajadorSeleccionadoLOV() {
      return tipoTrabajadorSeleccionadoLOV;
   }

   public void setTipoTrabajadorSeleccionadoLOV(TiposTrabajadores tipoTrabajadorSeleccionado) {
      this.tipoTrabajadorSeleccionadoLOV = tipoTrabajadorSeleccionado;
   }

   public TiposContratos getTipoContratoSeleccionadoLOV() {
      return tipoContratoSeleccionadoLOV;
   }

   public void setTipoContratoSeleccionadoLOV(TiposContratos tipoContratoSeleccionado) {
      this.tipoContratoSeleccionadoLOV = tipoContratoSeleccionado;
   }

   public GruposConceptos getGrupoConceptoSeleccionadoLOV() {
      return grupoConceptoSeleccionadoLOV;
   }

   public void setGrupoConceptoSeleccionadoLOV(GruposConceptos grupoConceptoSeleccionado) {
      this.grupoConceptoSeleccionadoLOV = grupoConceptoSeleccionado;
   }

   public CentrosCostos getCentroCostoSeleccionadoLOV() {
      return centroCostoSeleccionadoLOV;
   }

   public void setCentroCostoSeleccionadoLOV(CentrosCostos centroCostoSeleccionado) {
      this.centroCostoSeleccionadoLOV = centroCostoSeleccionado;
   }

   public TiposCentrosCostos getTipoCentroCostoSeleccionadoLOV() {
      return tipoCentroCostoSeleccionadoLOV;
   }

   public void setTipoCentroCostoSeleccionadoLOV(TiposCentrosCostos tipoCentroCostoSeleccionado) {
      this.tipoCentroCostoSeleccionadoLOV = tipoCentroCostoSeleccionado;
   }

   public Cuentas getCuentaSeleccionadaLOV() {
      return cuentaSeleccionadaLOV;
   }

   public void setCuentaSeleccionadaLOV(Cuentas cuentaSeleccionada) {
      this.cuentaSeleccionadaLOV = cuentaSeleccionada;
   }

   public void cargarNombresProcesos() {
      if (listVigenciasCuentasConcepto != null) {
         if (!listVigenciasCuentasConcepto.isEmpty()) {
            if (lovProcesos == null) {
               lovProcesos = administrarDetalleConcepto.consultarLOVProcesos();
            }
            if (lovProcesos != null) {
               if (!lovProcesos.isEmpty()) {
                  for (int i = 0; i < listVigenciasCuentasConcepto.size(); i++) {
                     if (listVigenciasCuentasConcepto.get(i).getProceso() != null) {
                        for (int j = 0; j < lovProcesos.size(); j++) {
                           if (listVigenciasCuentasConcepto.get(i).getProceso().equals(lovProcesos.get(j).getSecuencia())) {
                              listVigenciasCuentasConcepto.get(i).setNombreProceso(lovProcesos.get(j).getDescripcion());
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public List<VigenciasCuentas> getListVigenciasCuentasConcepto() {
      try {
         if (listVigenciasCuentasConcepto == null) {
            if (conceptoActual.getSecuencia() != null) {
               listVigenciasCuentasConcepto = administrarDetalleConcepto.consultarListaVigenciasCuentasConcepto(conceptoActual.getSecuencia());
               cargarNombresProcesos();
            }
         }
         return listVigenciasCuentasConcepto;
      } catch (Exception e) {
         log.warn("Error getListConceptosJuridicos " + e.toString());
         return null;
      }
   }

   public void setListVigenciasCuentasConcepto(List<VigenciasCuentas> t) {
      this.listVigenciasCuentasConcepto = t;
   }

   public List<VigenciasCuentas> getFiltrarListVigenciasCuentasConcepto() {
      return filtrarListVigenciasCuentasConcepto;
   }

   public void setFiltrarListVigenciasCuentasConcepto(List<VigenciasCuentas> t) {
      this.filtrarListVigenciasCuentasConcepto = t;
   }

   public String getNombreXML() {
      return nombreXML;
   }

   public void setNombreXML(String nombreXML) {
      this.nombreXML = nombreXML;
   }

   public String getNombreTabla() {
      return nombreTabla;
   }

   public void setNombreTabla(String nombreTabla) {
      this.nombreTabla = nombreTabla;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public List<VigenciasCuentas> getListVigenciasCuentasModificar() {
      return listVigenciasCuentasModificar;
   }

   public void setListVigenciasCuentasModificar(List<VigenciasCuentas> setListVigenciasCuentasModificar) {
      this.listVigenciasCuentasModificar = setListVigenciasCuentasModificar;
   }

//   public VigenciasCuentas getNuevaVigenciaCuenta() {
//      return nuevaVigenciaCuenta;
//   }
//
//   public void setNuevaVigenciaCuenta(VigenciasCuentas setNuevaVigenciaCuenta) {
//      this.nuevaVigenciaCuenta = setNuevaVigenciaCuenta;
//   }
   public List<VigenciasCuentas> getListConceptosJuridicosCrear() {
      return listVigenciasCuentasCrear;
   }

   public void setListConceptosJuridicosCrear(List<VigenciasCuentas> setListConceptosJuridicosCrear) {
      this.listVigenciasCuentasCrear = setListConceptosJuridicosCrear;
   }

   public List<VigenciasCuentas> getListVigenciasCuentasBorrar() {
      return listVigenciasCuentasBorrar;
   }

   public void setListVigenciasCuentasBorrar(List<VigenciasCuentas> setListVigenciasCuentasBorrar) {
      this.listVigenciasCuentasBorrar = setListVigenciasCuentasBorrar;
   }

   public VigenciasCuentas getEditarVigenciaCuenta() {
      return editarVigenciaCuenta;
   }

   public void setEditarVigenciaCuenta(VigenciasCuentas setEditarVigenciaCuenta) {
      this.editarVigenciaCuenta = setEditarVigenciaCuenta;
   }

//   public VigenciasCuentas getDuplicarVigenciaCuenta() {
//      return duplicarVigenciaCuenta;
//   }
//
//   public void setDuplicarVigenciaCuenta(VigenciasCuentas setDuplicarVigenciaCuenta) {
//      this.duplicarVigenciaCuenta = setDuplicarVigenciaCuenta;
//   }
   public String getMsnConfirmarRastro() {
      return msnConfirmarRastro;
   }

   public void setMsnConfirmarRastro(String msnConfirmarRastro) {
      this.msnConfirmarRastro = msnConfirmarRastro;
   }

   public String getMsnConfirmarRastroHistorico() {
      return msnConfirmarRastroHistorico;
   }

   public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
      this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
   }

   public BigInteger getBackUp() {
      return backUp;
   }

   public void setBackUp(BigInteger backUp) {
      this.backUp = backUp;
   }

   public String getNombreTablaRastro() {
      return nombreTablaRastro;
   }

   public void setNombreTablaRastro(String nombreTablaRastro) {
      this.nombreTablaRastro = nombreTablaRastro;
   }

   public Conceptos getConceptoActual() {
      return conceptoActual;
   }

   public void setConceptoActual(Conceptos setConceptoActual) {
      this.conceptoActual = setConceptoActual;
   }

   public List<VigenciasCuentas> getListVigenciasCuentasCrear() {
      return listVigenciasCuentasCrear;
   }

   public void setListVigenciasCuentasCrear(List<VigenciasCuentas> listVigenciasCuentasCrear) {
      this.listVigenciasCuentasCrear = listVigenciasCuentasCrear;
   }

   public List<TiposCentrosCostos> getFiltrarListTiposCentrosCostos() {
      return filtrarListTiposCentrosCostos;
   }

   public void setFiltrarListTiposCentrosCostos(List<TiposCentrosCostos> filtrarListTiposCentrosCostos) {
      this.filtrarListTiposCentrosCostos = filtrarListTiposCentrosCostos;
   }

   public List<Cuentas> getFiltrarListCuentas() {
      return filtrarListCuentas;
   }

   public void setFiltrarListCuentas(List<Cuentas> filtrarListCuentas) {
      this.filtrarListCuentas = filtrarListCuentas;
   }

   public List<CentrosCostos> getFiltrarListCentrosCostos() {
      return filtrarListCentrosCostos;
   }

   public void setFiltrarListCentrosCostos(List<CentrosCostos> filtrarListCentrosCostos) {
      this.filtrarListCentrosCostos = filtrarListCentrosCostos;
   }

   public String getComportamientoConcepto() {
      return comportamientoConcepto;
   }

   public void setComportamientoConcepto(String comportamientoConcepto) {
      this.comportamientoConcepto = comportamientoConcepto;
   }

   public List<VigenciasGruposConceptos> getListVigenciasGruposConceptos() {
      try {
         if (listVigenciasGruposConceptos == null) {
            if (conceptoActual.getSecuencia() != null) {
               listVigenciasGruposConceptos = administrarDetalleConcepto.consultarListaVigenciasGruposConceptosConcepto(conceptoActual.getSecuencia());
            }
         }
         return listVigenciasGruposConceptos;
      } catch (Exception e) {
         log.warn("Error getListVigenciasGruposConceptosConcepto : " + e.toString());
         return null;
      }

   }

   public void setListVigenciasGruposConceptos(List<VigenciasGruposConceptos> listVigenciasGruposConceptosConcepto) {
      this.listVigenciasGruposConceptos = listVigenciasGruposConceptosConcepto;
   }

   public List<VigenciasGruposConceptos> getFiltrarListVigenciasGruposConceptos() {
      return filtrarListVigenciasGruposConceptos;
   }

   public void setFiltrarListVigenciasGruposConceptos(List<VigenciasGruposConceptos> filtrarVigenciasGruposConceptosConcepto) {
      this.filtrarListVigenciasGruposConceptos = filtrarVigenciasGruposConceptosConcepto;
   }

   public List<VigenciasGruposConceptos> getListVigenciasGruposConceptosModificar() {
      return listVigenciasGruposConceptosModificar;
   }

   public void setListVigenciasGruposConceptosModificar(List<VigenciasGruposConceptos> listVigenciasGruposConceptosModificar) {
      this.listVigenciasGruposConceptosModificar = listVigenciasGruposConceptosModificar;
   }

   public VigenciasGruposConceptos getNuevaVigenciaGrupoConcepto() {
      return nuevaVigenciaGrupoConcepto;
   }

   public void setNuevaVigenciaGrupoConcepto(VigenciasGruposConceptos nuevaVigenciaGrupoConcepto) {
      this.nuevaVigenciaGrupoConcepto = nuevaVigenciaGrupoConcepto;
   }

   public List<VigenciasGruposConceptos> getListVigenciasGruposConceptosCrear() {
      return listVigenciasGruposConceptosCrear;
   }

   public void setListVigenciasGruposConceptosCrear(List<VigenciasGruposConceptos> listVigenciasGruposConceptosCrear) {
      this.listVigenciasGruposConceptosCrear = listVigenciasGruposConceptosCrear;
   }

   public List<VigenciasGruposConceptos> getListVigenciasGruposConceptosBorrar() {
      return listVigenciasGruposConceptosBorrar;
   }

   public void setListVigenciasGruposConceptosBorrar(List<VigenciasGruposConceptos> listVigenciasGruposConceptosBorrar) {
      this.listVigenciasGruposConceptosBorrar = listVigenciasGruposConceptosBorrar;
   }

   public VigenciasGruposConceptos getEditarVigenciaGrupoConcepto() {
      return editarVigenciaGrupoConcepto;
   }

   public void setEditarVigenciaGrupoConcepto(VigenciasGruposConceptos editarVigenciaGrupoConcepto) {
      this.editarVigenciaGrupoConcepto = editarVigenciaGrupoConcepto;
   }

   public VigenciasGruposConceptos getDuplicarVigenciaGrupoConcepto() {
      return duplicarVigenciaGrupoConcepto;
   }

   public void setDuplicarVigenciaGrupoConcepto(VigenciasGruposConceptos duplicarVigenciaGrupoConcepto) {
      this.duplicarVigenciaGrupoConcepto = duplicarVigenciaGrupoConcepto;
   }

   public List<GruposConceptos> getFiltrarListGruposConceptos() {
      return filtrarListGruposConceptos;
   }

   public void setFiltrarListGruposConceptos(List<GruposConceptos> filtrarListGruposConceptos) {
      this.filtrarListGruposConceptos = filtrarListGruposConceptos;
   }

   public List<VigenciasConceptosTT> getListVigenciasConceptosTTConcepto() {
      try {
         if (listVigenciasConceptosTTConcepto == null) {
            if (conceptoActual.getSecuencia() != null) {
               listVigenciasConceptosTTConcepto = administrarDetalleConcepto.consultarListaVigenciasConceptosTTConcepto(conceptoActual.getSecuencia());
            }
         }
         return listVigenciasConceptosTTConcepto;
      } catch (Exception e) {
         log.warn("Error getListVigenciasConceptosTTConcepto : " + e.toString());
         return null;
      }
   }

   public void setListVigenciasConceptosTTConcepto(List<VigenciasConceptosTT> listVigenciasConceptosTTConcepto) {
      this.listVigenciasConceptosTTConcepto = listVigenciasConceptosTTConcepto;
   }

   public List<VigenciasConceptosTT> getFiltrarListVigenciasConceptosTT() {
      return filtrarListVigenciasConceptosTT;
   }

   public void setFiltrarListVigenciasConceptosTT(List<VigenciasConceptosTT> filtrarListVigenciasConceptosTT) {
      this.filtrarListVigenciasConceptosTT = filtrarListVigenciasConceptosTT;
   }

   public List<VigenciasConceptosTC> getListVigenciasConceptosTCConcepto() {
      try {
         if (listVigenciasConceptosTCConcepto == null) {
            if (conceptoActual.getSecuencia() != null) {
               listVigenciasConceptosTCConcepto = administrarDetalleConcepto.consultarListaVigenciasConceptosTCConcepto(conceptoActual.getSecuencia());
            }
         }
         return listVigenciasConceptosTCConcepto;
      } catch (Exception e) {
         log.warn("Error getListVigenciasConceptosTCConcepto : " + e.toString());
         return null;
      }
   }

   public void setListVigenciasConceptosTCConcepto(List<VigenciasConceptosTC> listVigenciasConceptosTCConcepto) {
      this.listVigenciasConceptosTCConcepto = listVigenciasConceptosTCConcepto;
   }

   public List<VigenciasConceptosTC> getFiltrarListVigenciasConceptosTC() {
      return filtrarListVigenciasConceptosTC;
   }

   public void setFiltrarListVigenciasConceptosTC(List<VigenciasConceptosTC> filtrarListVigenciasConceptosTC) {
      this.filtrarListVigenciasConceptosTC = filtrarListVigenciasConceptosTC;
   }

   public List<VigenciasConceptosRL> getListVigenciasConceptosRLConcepto() {
      try {

         if (listVigenciasConceptosRLConcepto == null) {
            if (conceptoActual.getSecuencia() != null) {
               listVigenciasConceptosRLConcepto = administrarDetalleConcepto.consultarListaVigenciasConceptosRLCConcepto(conceptoActual.getSecuencia());
            }
         }
         return listVigenciasConceptosRLConcepto;
      } catch (Exception e) {
         return null;
      }
   }

   public void setListVigenciasConceptosRLConcepto(List<VigenciasConceptosRL> listVigenciasConceptosRLConcepto) {
      this.listVigenciasConceptosRLConcepto = listVigenciasConceptosRLConcepto;
   }

   public List<VigenciasConceptosRL> getFiltrarListVigenciasConceptosRL() {
      return filtrarListVigenciasConceptosRL;
   }

   public void setFiltrarListVigenciasConceptosRL(List<VigenciasConceptosRL> filtrarListVigenciasConceptosRL) {
      this.filtrarListVigenciasConceptosRL = filtrarListVigenciasConceptosRL;
   }

   public List<FormulasConceptos> getListFormulasConceptos() {
      try {
         if (listFormulasConceptos == null) {
            if (conceptoActual.getSecuencia() != null) {
               listFormulasConceptos = administrarDetalleConcepto.consultarListaFormulasConceptosConcepto(conceptoActual.getSecuencia());
            }
         }
         return listFormulasConceptos;
      } catch (Exception e) {
         return null;
      }
   }

   public void setListFormulasConceptos(List<FormulasConceptos> listFormulasConceptosConcepto) {
      this.listFormulasConceptos = listFormulasConceptosConcepto;
   }

   public List<FormulasConceptos> getFiltrarListFormulasConceptosConcepto() {
      return filtrarListFormulasConceptosConcepto;
   }

   public void setFiltrarListFormulasConceptosConcepto(List<FormulasConceptos> filtrarListFormulasConceptos) {
      this.filtrarListFormulasConceptosConcepto = filtrarListFormulasConceptos;
   }

   public List<VigenciasConceptosTT> getListVigenciasConceptosTTModificar() {
      return listVigenciasConceptosTTModificar;
   }

   public void setListVigenciasConceptosTTModificar(List<VigenciasConceptosTT> listVigenciasConceptosTTModificar) {
      this.listVigenciasConceptosTTModificar = listVigenciasConceptosTTModificar;
   }

   public VigenciasConceptosTT getNuevaVigenciaConceptoTT() {
      return nuevaVigenciaConceptoTT;
   }

   public void setNuevaVigenciaConceptoTT(VigenciasConceptosTT nuevaVigenciaConceptoTT) {
      this.nuevaVigenciaConceptoTT = nuevaVigenciaConceptoTT;
   }

   public List<VigenciasConceptosTT> getListVigenciasConceptosTTCrear() {
      return listVigenciasConceptosTTCrear;
   }

   public void setListVigenciasConceptosTTCrear(List<VigenciasConceptosTT> listVigenciasConceptosTTCrear) {
      this.listVigenciasConceptosTTCrear = listVigenciasConceptosTTCrear;
   }

   public List<VigenciasConceptosTT> getListVigenciasConceptosTTBorrar() {
      return listVigenciasConceptosTTBorrar;
   }

   public void setListVigenciasConceptosTTBorrar(List<VigenciasConceptosTT> listVigenciasConceptosTTBorrar) {
      this.listVigenciasConceptosTTBorrar = listVigenciasConceptosTTBorrar;
   }

   public VigenciasConceptosTT getEditarVigenciaConceptoTT() {
      return editarVigenciaConceptoTT;
   }

   public void setEditarVigenciaConceptoTT(VigenciasConceptosTT editarVigenciaConceptoTT) {
      this.editarVigenciaConceptoTT = editarVigenciaConceptoTT;
   }

   public VigenciasConceptosTT getDuplicarVigenciaConceptoTT() {
      return duplicarVigenciaConceptoTT;
   }

   public void setDuplicarVigenciaConceptoTT(VigenciasConceptosTT duplicarVigenciaConceptoTT) {
      this.duplicarVigenciaConceptoTT = duplicarVigenciaConceptoTT;
   }

   public List<TiposTrabajadores> getFiltrarListTiposTrabajadores() {
      return filtrarListTiposTrabajadores;
   }

   public void setFiltrarListTiposTrabajadores(List<TiposTrabajadores> filtrarListTiposTrabajadores) {
      this.filtrarListTiposTrabajadores = filtrarListTiposTrabajadores;
   }

   public List<VigenciasConceptosTC> getListVigenciasConceptosTCModificar() {
      return listVigenciasConceptosTCModificar;
   }

   public void setListVigenciasConceptosTCModificar(List<VigenciasConceptosTC> listVigenciasConceptosTCModificar) {
      this.listVigenciasConceptosTCModificar = listVigenciasConceptosTCModificar;
   }

   public VigenciasConceptosTC getNuevaVigenciaConceptoTC() {
      return nuevaVigenciaConceptoTC;
   }

   public void setNuevaVigenciaConceptoTC(VigenciasConceptosTC nuevaVigenciaConceptoTC) {
      this.nuevaVigenciaConceptoTC = nuevaVigenciaConceptoTC;
   }

   public List<VigenciasConceptosTC> getListVigenciasConceptosTCCrear() {
      return listVigenciasConceptosTCCrear;
   }

   public void setListVigenciasConceptosTCCrear(List<VigenciasConceptosTC> listVigenciasConceptosTCCrear) {
      this.listVigenciasConceptosTCCrear = listVigenciasConceptosTCCrear;
   }

   public List<VigenciasConceptosTC> getListVigenciasConceptosTCBorrar() {
      return listVigenciasConceptosTCBorrar;
   }

   public void setListVigenciasConceptosTCBorrar(List<VigenciasConceptosTC> listVigenciasConceptosTCBorrar) {
      this.listVigenciasConceptosTCBorrar = listVigenciasConceptosTCBorrar;
   }

   public VigenciasConceptosTC getEditarVigenciaConceptoTC() {
      return editarVigenciaConceptoTC;
   }

   public void setEditarVigenciaConceptoTC(VigenciasConceptosTC editarVigenciaConceptoTC) {
      this.editarVigenciaConceptoTC = editarVigenciaConceptoTC;
   }

   public VigenciasConceptosTC getDuplicarVigenciaConceptoTC() {
      return duplicarVigenciaConceptoTC;
   }

   public void setDuplicarVigenciaConceptoTC(VigenciasConceptosTC duplicarVigenciaConceptoTC) {
      this.duplicarVigenciaConceptoTC = duplicarVigenciaConceptoTC;
   }

   public List<TiposContratos> getFiltrarListTiposContratos() {
      return filtrarListTiposContratos;
   }

   public void setFiltrarListTiposContratos(List<TiposContratos> filtrarListTiposContratos) {
      this.filtrarListTiposContratos = filtrarListTiposContratos;
   }

   public List<VigenciasConceptosRL> getListVigenciasConceptosRLModificar() {
      return listVigenciasConceptosRLModificar;
   }

   public void setListVigenciasConceptosRLModificar(List<VigenciasConceptosRL> listVigenciasConceptosRLModificar) {
      this.listVigenciasConceptosRLModificar = listVigenciasConceptosRLModificar;
   }

   public VigenciasConceptosRL getNuevaVigenciaConceptoRL() {
      return nuevaVigenciaConceptoRL;
   }

   public void setNuevaVigenciaConceptoRL(VigenciasConceptosRL nuevaVigenciaConceptoRL) {
      this.nuevaVigenciaConceptoRL = nuevaVigenciaConceptoRL;
   }

   public List<VigenciasConceptosRL> getListVigenciasConceptosRLCrear() {
      return listVigenciasConceptosRLCrear;
   }

   public void setListVigenciasConceptosRLCrear(List<VigenciasConceptosRL> listVigenciasConceptosRLCrear) {
      this.listVigenciasConceptosRLCrear = listVigenciasConceptosRLCrear;
   }

   public List<VigenciasConceptosRL> getListVigenciasConceptosRLBorrar() {
      return listVigenciasConceptosRLBorrar;
   }

   public void setListVigenciasConceptosRLBorrar(List<VigenciasConceptosRL> listVigenciasConceptosRLBorrar) {
      this.listVigenciasConceptosRLBorrar = listVigenciasConceptosRLBorrar;
   }

   public VigenciasConceptosRL getEditarVigenciaConceptoRL() {
      return editarVigenciaConceptoRL;
   }

   public void setEditarVigenciaConceptoRL(VigenciasConceptosRL editarVigenciaConceptoRL) {
      this.editarVigenciaConceptoRL = editarVigenciaConceptoRL;
   }

   public VigenciasConceptosRL getDuplicarVigenciaConceptoRL() {
      return duplicarVigenciaConceptoRL;
   }

   public void setDuplicarVigenciaConceptoRL(VigenciasConceptosRL duplicarVigenciaConceptoRL) {
      this.duplicarVigenciaConceptoRL = duplicarVigenciaConceptoRL;
   }

   public List<ReformasLaborales> getFiltrarListReformasLaborales() {
      return filtrarListReformasLaborales;
   }

   public void setFiltrarListReformasLaborales(List<ReformasLaborales> filtrarListReformasLaborales) {
      this.filtrarListReformasLaborales = filtrarListReformasLaborales;
   }

   public List<FormulasConceptos> getListFormulasConceptosModificar() {
      return listFormulasConceptosModificar;
   }

   public void setListFormulasConceptosModificar(List<FormulasConceptos> listFormulasConceptosModificar) {
      this.listFormulasConceptosModificar = listFormulasConceptosModificar;
   }

   public FormulasConceptos getNuevaFormulasConceptos() {
      return nuevaFormulasConceptos;
   }

   public void setNuevaFormulasConceptos(FormulasConceptos nuevaFormulasConceptos) {
      this.nuevaFormulasConceptos = nuevaFormulasConceptos;
   }

   public List<FormulasConceptos> getListFormulasConceptosCrear() {
      return listFormulasConceptosCrear;
   }

   public void setListFormulasConceptosCrear(List<FormulasConceptos> listFormulasConceptosCrear) {
      this.listFormulasConceptosCrear = listFormulasConceptosCrear;
   }

   public List<FormulasConceptos> getListFormulasConceptosBorrar() {
      return listFormulasConceptosBorrar;
   }

   public void setListFormulasConceptosBorrar(List<FormulasConceptos> listFormulasConceptosBorrar) {
      this.listFormulasConceptosBorrar = listFormulasConceptosBorrar;
   }

   public FormulasConceptos getEditarFormulasConceptos() {
      return editarFormulasConceptos;
   }

   public void setEditarFormulasConceptos(FormulasConceptos editarFormulasConceptos) {
      this.editarFormulasConceptos = editarFormulasConceptos;
   }

   public FormulasConceptos getDuplicarFormulasConceptos() {
      return duplicarFormulasConceptos;
   }

   public void setDuplicarFormulasConceptos(FormulasConceptos duplicarFormulasConceptos) {
      this.duplicarFormulasConceptos = duplicarFormulasConceptos;
   }

   public List<Formulas> getFiltrarListFormulas() {
      return filtrarListFormulas;
   }

   public void setFiltrarListFormulas(List<Formulas> filtrarListFormulas) {
      this.filtrarListFormulas = filtrarListFormulas;
   }

   public List<FormulasConceptos> getFiltrarListFormulasConceptos() {
      return filtrarListFormulasConceptos;
   }

   public void setFiltrarListFormulasConceptos(List<FormulasConceptos> filtrarListFormulasConceptos) {
      this.filtrarListFormulasConceptos = filtrarListFormulasConceptos;
   }

   public FormulasConceptos getActualFormulaConcepto() {
      return actualFormulaConcepto;
   }

   public void setActualFormulaConcepto(FormulasConceptos setActualFormulaConcepto) {
      this.actualFormulaConcepto = setActualFormulaConcepto;
   }

   public String getAltoTablaVigenciaCuenta() {
      return altoTablaVigenciaCuenta;
   }

   public void setAltoTablaVigenciaCuenta(String altoTablaVigenciaCuenta) {
      this.altoTablaVigenciaCuenta = altoTablaVigenciaCuenta;
   }

   public String getAltoTablaVigenciaGrupoC() {
      return altoTablaVigenciaGrupoC;
   }

   public void setAltoTablaVigenciaGrupoC(String altoTablaVigenciaGrupoC) {
      this.altoTablaVigenciaGrupoC = altoTablaVigenciaGrupoC;
   }

   public String getAltoTablaVigenciaConceptoTT() {
      return altoTablaVigenciaConceptoTT;
   }

   public void setAltoTablaVigenciaConceptoTT(String altoTablaVigenciaConceptoTT) {
      this.altoTablaVigenciaConceptoTT = altoTablaVigenciaConceptoTT;
   }

   public String getAltoTablaVigenciaConceptoTC() {
      return altoTablaVigenciaConceptoTC;
   }

   public void setAltoTablaVigenciaConceptoTC(String altoTablaVigenciaConceptoTC) {
      this.altoTablaVigenciaConceptoTC = altoTablaVigenciaConceptoTC;
   }

   public String getAltoTablaVigenciaConceptoRL() {
      return altoTablaVigenciaConceptoRL;
   }

   public void setAltoTablaVigenciaConceptoRL(String altoTablaVigenciaConceptoRL) {
      this.altoTablaVigenciaConceptoRL = altoTablaVigenciaConceptoRL;
   }

   public String getAltoTablaFormulaConcepto() {
      return altoTablaFormulaConcepto;
   }

   public void setAltoTablaFormulaConcepto(String altoTablaFormulaConcepto) {
      this.altoTablaFormulaConcepto = altoTablaFormulaConcepto;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public List<TiposCentrosCostos> getLovTiposCentrosCostos() {
      return lovTiposCentrosCostos;
   }

   public void setLovTiposCentrosCostos(List<TiposCentrosCostos> lovTiposCentrosCostos) {
      this.lovTiposCentrosCostos = lovTiposCentrosCostos;
   }

   public List<Cuentas> getLovCuentas() {
      return lovCuentas;
   }

   public void setLovCuentas(List<Cuentas> lovCuentas) {
      this.lovCuentas = lovCuentas;
   }

   public List<Cuentas> getLovCuentas2505() {
      if (lovCuentas2505 == null) {
         lovCuentas2505 = new ArrayList<Cuentas>();
      }
      return lovCuentas2505;
   }

   public void setLovCuentas2505(List<Cuentas> lovCuentas2505) {
      this.lovCuentas2505 = lovCuentas2505;
   }

   public List<CentrosCostos> getLovCentrosCostos() {
      return lovCentrosCostos;
   }

   public void setLovCentrosCostos(List<CentrosCostos> lovCentrosCostos) {
      this.lovCentrosCostos = lovCentrosCostos;
   }

   public List<GruposConceptos> getLovGruposConceptos() {
      return lovGruposConceptos;
   }

   public void setLovGruposConceptos(List<GruposConceptos> lovGruposConceptos) {
      this.lovGruposConceptos = lovGruposConceptos;
   }

   public List<TiposTrabajadores> getLovTiposTrabajadores() {
      return lovTiposTrabajadores;
   }

   public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadores) {
      this.lovTiposTrabajadores = lovTiposTrabajadores;
   }

   public List<TiposContratos> getLovTiposContratos() {
      return lovTiposContratos;
   }

   public void setLovTiposContratos(List<TiposContratos> lovTiposContratos) {
      this.lovTiposContratos = lovTiposContratos;
   }

   public List<ReformasLaborales> getLovReformasLaborales() {
      return lovReformasLaborales;
   }

   public void setLovReformasLaborales(List<ReformasLaborales> lovReformasLaborales) {
      this.lovReformasLaborales = lovReformasLaborales;
   }

   public List<Formulas> getLovFormulas() {
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> lovFormulas) {
      this.lovFormulas = lovFormulas;
   }

   public List<FormulasConceptos> getLovFormulasConceptos() {
      return lovFormulasConceptos;
   }

   public void setLovFormulasConceptos(List<FormulasConceptos> lovFormulasConceptos) {
      this.lovFormulasConceptos = lovFormulasConceptos;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

   public List<Procesos> getLovProcesos() {
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> lovProcesos) {
      this.lovProcesos = lovProcesos;
   }

   public List<Procesos> getFiltrarlovProcesos() {
      return filtrarlovProcesos;
   }

   public void setFiltrarlovProcesos(List<Procesos> filtrarlovProcesos) {
      this.filtrarlovProcesos = filtrarlovProcesos;
   }

   public Procesos getProcesoSeleccionadoLOV() {
      return procesoSeleccionadoLOV;
   }

   public void setProcesoSeleccionadoLOV(Procesos procesoSeleccionadoLOV) {
      this.procesoSeleccionadoLOV = procesoSeleccionadoLOV;
   }

   public void setInfoRegistroLovTipoCentroCosto(String infoRegistroLovTipoCentroCosto) {
      this.infoRegistroLovTipoCentroCosto = infoRegistroLovTipoCentroCosto;
   }

   public void setInfoRegistroLovCuentaDebito(String infoRegistroLovCuentaDebito) {
      this.infoRegistroLovCuentaDebito = infoRegistroLovCuentaDebito;
   }

   public void setInfoRegistroLovCuentaCredito(String infoRegistroLovCuentaCredito) {
      this.infoRegistroLovCuentaCredito = infoRegistroLovCuentaCredito;
   }

   public void setInfoRegistroLovCentroCostoDebito(String infoRegistroLovCentroCostoDebito) {
      this.infoRegistroLovCentroCostoDebito = infoRegistroLovCentroCostoDebito;
   }

   public void setInfoRegistroLovCentroCostoCredito(String infoRegistroLovCentroCostoCredito) {
      this.infoRegistroLovCentroCostoCredito = infoRegistroLovCentroCostoCredito;
   }

   public void setInfoRegistroLovGrupoConcepto(String infoRegistroLovGrupoConcepto) {
      this.infoRegistroLovGrupoConcepto = infoRegistroLovGrupoConcepto;
   }

   public void setInfoRegistroLovTipoTrabajador(String infoRegistroLovTipoTrabajador) {
      this.infoRegistroLovTipoTrabajador = infoRegistroLovTipoTrabajador;
   }

   public void setInfoRegistroLovTipoContrato(String infoRegistroLovTipoContrato) {
      this.infoRegistroLovTipoContrato = infoRegistroLovTipoContrato;
   }

   public void setInfoRegistroLovReformaLaboral(String infoRegistroLovReformaLaboral) {
      this.infoRegistroLovReformaLaboral = infoRegistroLovReformaLaboral;
   }

   public void setInfoRegistroLovFormula(String infoRegistroLovFormula) {
      this.infoRegistroLovFormula = infoRegistroLovFormula;
   }

   public void setInfoRegistroLovFormulasConceptos(String infoRegistroLovOrden) {
      this.infoRegistroLovFormulasConceptos = infoRegistroLovOrden;
   }

   public void setInfoRegistroCuenta(String infoRegistroCuenta) {
      this.infoRegistroCuenta = infoRegistroCuenta;
   }

   public void setInfoRegistroGrupoC(String infoRegistroGrupoC) {
      this.infoRegistroGrupoC = infoRegistroGrupoC;
   }

   public void setInfoRegistroConceptoTT(String infoRegistroConceptoTT) {
      this.infoRegistroConceptoTT = infoRegistroConceptoTT;
   }

   public void setInfoRegistroConceptoTC(String infoRegistroConceptoTC) {
      this.infoRegistroConceptoTC = infoRegistroConceptoTC;
   }

   public void setInfoRegistroConceptoRL(String infoRegistroConceptoRL) {
      this.infoRegistroConceptoRL = infoRegistroConceptoRL;
   }

   public void setInfoRegistroFormulaConcepto(String infoRegistroFormulaConcepto) {
      this.infoRegistroFormulaConcepto = infoRegistroFormulaConcepto;
   }

   public void setInfoRegistroLovProcesos(String infoRegistroLovProcesos) {
      this.infoRegistroLovProcesos = infoRegistroLovProcesos;
   }

   public String getInfoRegistroLovTipoCentroCosto() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovTiposCC:lovTiposCC");
      infoRegistroLovTipoCentroCosto = String.valueOf(tabla.getRowCount());
      return infoRegistroLovTipoCentroCosto;
   }

   public String getInfoRegistroLovCuentaDebito() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovDebito:lovDebito");
      infoRegistroLovCuentaDebito = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCuentaDebito;
   }

   public String getInfoRegistroLovCuentas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovCuentas:lovCuentas");
      infoRegistroLovCuentas = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCuentas;
   }

   public String getInfoRegistroLovCuentaCredito() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovCredito:lovCredito");
      infoRegistroLovCuentaCredito = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCuentaCredito;
   }

   public String getInfoRegistroLovCentroCostoDebito() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovCentroCostoD:lovCentroCostoD");
      infoRegistroLovCentroCostoDebito = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCentroCostoDebito;
   }

   public String getInfoRegistroLovCentroCostoCredito() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovCentroCostoC:lovCentroCostoC");
      infoRegistroLovCentroCostoCredito = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCentroCostoCredito;
   }

   public String getInfoRegistroLovGrupoConcepto() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovGrupoConcepto:lovGrupoConcepto");
      infoRegistroLovGrupoConcepto = String.valueOf(tabla.getRowCount());
      return infoRegistroLovGrupoConcepto;
   }

   public String getInfoRegistroLovTipoTrabajador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovTipoTrabajador:lovTipoTrabajador");
      infoRegistroLovTipoTrabajador = String.valueOf(tabla.getRowCount());
      return infoRegistroLovTipoTrabajador;
   }

   public String getInfoRegistroLovTipoContrato() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovTipoContrato:lovTipoContrato");
      infoRegistroLovTipoContrato = String.valueOf(tabla.getRowCount());
      return infoRegistroLovTipoContrato;
   }

   public String getInfoRegistroLovReformaLaboral() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovReformaLaboral:lovReformaLaboral");
      infoRegistroLovReformaLaboral = String.valueOf(tabla.getRowCount());
      return infoRegistroLovReformaLaboral;
   }

   public String getInfoRegistroLovFormula() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovFormula:lovFormula");
      infoRegistroLovFormula = String.valueOf(tabla.getRowCount());
      return infoRegistroLovFormula;
   }

   public String getInfoRegistroLovFormulasConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovFormulaC:lovFormulaC");
      infoRegistroLovFormulasConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroLovFormulasConceptos;
   }

   public String getInfoRegistroCuenta() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaCuenta");
      infoRegistroCuenta = String.valueOf(tabla.getRowCount());
      return infoRegistroCuenta;
   }

   public String getInfoRegistroGrupoC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaGrupoConcepto");
      infoRegistroGrupoC = String.valueOf(tabla.getRowCount());
      return infoRegistroGrupoC;
   }

   public String getInfoRegistroConceptoTT() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaConceptoTT");
      infoRegistroConceptoTT = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptoTT;
   }

   public String getInfoRegistroConceptoTC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaConceptoTC");
      infoRegistroConceptoTC = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptoTC;
   }

   public String getInfoRegistroConceptoRL() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaConceptoRL");
      infoRegistroConceptoRL = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptoRL;
   }

   public String getInfoRegistroFormulaConcepto() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFormulaConcepto");
      infoRegistroFormulaConcepto = String.valueOf(tabla.getRowCount());
      return infoRegistroFormulaConcepto;
   }

   public String getInfoRegistroLovProcesos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovProceso:lovProceso");
      infoRegistroLovProcesos = String.valueOf(tabla.getRowCount());
      return infoRegistroLovProcesos;
   }

   public String getMensajeError() {
      return mensajeError;
   }

   public List<VigenciasCuentas> getListVigCParaCrear() {
      return listVigCParaCrear;
   }

   public void setListVigCParaCrear(List<VigenciasCuentas> listVigCParaCrear) {
      this.listVigCParaCrear = listVigCParaCrear;
   }

   public VigenciasCuentas getVigCuentaCrearSeleccionada() {
      return vigCuentaCrearSeleccionada;
   }

   public void setVigCuentaCrearSeleccionada(VigenciasCuentas vigCuentaCrearSeleccionada) {
      this.vigCuentaCrearSeleccionada = vigCuentaCrearSeleccionada;
   }

}
