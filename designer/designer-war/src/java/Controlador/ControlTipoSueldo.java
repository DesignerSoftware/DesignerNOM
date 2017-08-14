package Controlador;

import Entidades.Conceptos;
import Entidades.Empresas;
import Entidades.Formulas;
import Entidades.Grupostiposentidades;
import Entidades.TEFormulasConceptos;
import Entidades.TSFormulasConceptos;
import Entidades.TSGruposTiposEntidades;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposSueldosInterface;
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

@ManagedBean
@SessionScoped
public class ControlTipoSueldo implements Serializable {

   private static Logger log = Logger.getLogger(ControlTipoSueldo.class);

   @EJB
   AdministrarTiposSueldosInterface administrarTipoSueldo;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //
   private List<TiposSueldos> listaTiposSueldos;
   private List<TiposSueldos> filtrarListaTiposSueldos;
   private TiposSueldos tipoSueldoTablaSeleccionado;
   private List<TSFormulasConceptos> listaTSFormulasConceptos;
   private List<TSFormulasConceptos> filtrarListaTSFormulasConceptos;
   private TSFormulasConceptos tsFormulaTablaSeleccionado;
   private List<TSGruposTiposEntidades> listaTSGruposTiposEntidades;
   private List<TSGruposTiposEntidades> filtrarListaTSGruposTiposEntidades;
   private TSGruposTiposEntidades tsGrupoTablaSeleccionado;
   private List<TEFormulasConceptos> listaTEFormulasConceptos;
   private List<TEFormulasConceptos> filtrarListaTEFormulasConceptos;
   private TEFormulasConceptos teFormulaTablaSeleccionado;
   private int bandera;
   private Column tipoSueldoCodigo, tipoSueldoDescripcion, tipoSueldoCap, tipoSueldoBas, tipoSueldoAdi;
   private Column tsFormulaFormula, tsFormulaConcepto, tsFormulaEmpresa, tsFormulaOrigen;
   private Column teFormulaFormula, teFormulaConcepto, teFormulaEmpresa, teFormulaTipoEntidad;
   private Column tsGrupoGrupo;
   private boolean aceptar;
   private List<TiposSueldos> listTiposSueldosModificar;
   private List<TSFormulasConceptos> listTSFormulasConceptosModificar;
   private List<TSGruposTiposEntidades> listTSGruposTiposEntidadesModificar;
   private List<TEFormulasConceptos> listTEFormulasConceptosModificar;
   private boolean guardado, guardadoTSFormulas, guardadoTSGrupos, guardadoTEFormulas;
   private TiposSueldos nuevoTipoSueldo;
   private TSFormulasConceptos nuevoTSFormulaConcepto;
   private TSGruposTiposEntidades nuevoTSGrupoTipoEntidad;
   private TEFormulasConceptos nuevoTEFormulaConcepto;
   private List<TiposSueldos> listTiposSueldosCrear;
   private List<TSFormulasConceptos> listTSFormulasConceptosCrear;
   private List<TSGruposTiposEntidades> listTSGruposTiposEntidadesCrear;
   private List<TEFormulasConceptos> listTEFormulasConceptosCrear;
   private BigInteger l;
   private int k;
   private List<TiposSueldos> listTiposSueldosBorrar;
   private List<TSFormulasConceptos> listTSFormulasConceptosBorrar;
   private List<TSGruposTiposEntidades> listTSGruposTiposEntidadesBorrar;
   private List<TEFormulasConceptos> listTEFormulasConceptosBorrar;
   private TiposSueldos editarTipoSueldo;
   private TSFormulasConceptos editarTSFormulaConcepto;
   private TSGruposTiposEntidades editarTSGrupoTipoEntidad;
   private TEFormulasConceptos editarTEFormulaConcepto;
   private int cualCelda, tipoLista, cualCeldaTSFormulas, tipoListaTSFormulas, cualCeldaTSGrupos, tipoListaTSGrupos, cualCeldaTEFormulas, tipoListaTEFormulas;
   private TiposSueldos duplicarTipoSueldo;
   private TSFormulasConceptos duplicarTSFormulaConcepto;
   private TSGruposTiposEntidades duplicarTSGrupoTipoEntidad;
   private TEFormulasConceptos duplicarTEFormulaConcepto;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private String nombreTablaRastro;
   private String nombreXML, nombreTabla;

   ///////////LOV///////////
   private List<Formulas> lovFormulas;
   private List<Formulas> filtrarLovFormulas;
   private Formulas formulaSeleccionado;

   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtrarLovConceptos;
   private Conceptos conceptoSeleccionado;

   private List<Grupostiposentidades> lovGruposTiposEntidades;
   private List<Grupostiposentidades> filtrarLovGruposTiposEntidades;
   private Grupostiposentidades grupoTipoEntidadSeleccionado;

   private List<TiposEntidades> lovTiposEntidades;
   private List<TiposEntidades> filtrarLovTiposEntidades;
   private TiposEntidades tipoEntidadSeleccionado;

   private boolean permitirIndexTSFormulas, permitirIndexTSGrupos, permitirIndexTEFormulas;
   private int tipoActualizacion;
   private boolean cambiosPagina;
   private String altoTablaTiposSueldos, altoTablaTSFormulas, altoTablaTSGrupos, altoTablaTEFormulas;
   private boolean activoFormulaConcepto, activoGrupoDistribucion, activoTipoEntidad;
   private String infoRegistroTS, infoRegistroTSFormulas, infoRegistroTSGrupos, infoRegistroTE;
   private String infoRegistroFormula, infoRegistroConcepto, infoRegistroGrupo, infoRegistroTipoEntidad, infoRegistroFormulaTE, infoRegistroConceptoTE;
   private int cualtabla;
   private BigInteger backup;
   private boolean activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlTipoSueldo() {
      activoFormulaConcepto = true;
      activoGrupoDistribucion = true;
      activoTipoEntidad = true;
      paginaAnterior = "nominaf";
      altoTablaTiposSueldos = "70";
      altoTablaTSFormulas = "210";
      altoTablaTSGrupos = "55";
      altoTablaTEFormulas = "80";
      permitirIndexTSFormulas = true;
      permitirIndexTEFormulas = true;
      permitirIndexTSGrupos = true;
      lovFormulas = null;
      formulaSeleccionado = new Formulas();
      lovConceptos = null;
      conceptoSeleccionado = new Conceptos();
      lovGruposTiposEntidades = null;
      grupoTipoEntidadSeleccionado = new Grupostiposentidades();
      lovTiposEntidades = null;
      tipoEntidadSeleccionado = new TiposEntidades();
      listaTiposSueldos = null;
      listaTSFormulasConceptos = null;
      listaTSGruposTiposEntidades = null;
      listaTEFormulasConceptos = null;
      aceptar = true;
      cambiosPagina = true;
      tipoActualizacion = -1;
      k = 0;
      listTiposSueldosBorrar = new ArrayList<TiposSueldos>();
      listTSFormulasConceptosBorrar = new ArrayList<TSFormulasConceptos>();
      listTSGruposTiposEntidadesBorrar = new ArrayList<TSGruposTiposEntidades>();
      listTEFormulasConceptosBorrar = new ArrayList<TEFormulasConceptos>();
      listTiposSueldosCrear = new ArrayList<TiposSueldos>();
      listTSFormulasConceptosCrear = new ArrayList<TSFormulasConceptos>();
      listTSGruposTiposEntidadesCrear = new ArrayList<TSGruposTiposEntidades>();
      listTEFormulasConceptosCrear = new ArrayList<TEFormulasConceptos>();
      listTSFormulasConceptosModificar = new ArrayList<TSFormulasConceptos>();
      listTiposSueldosModificar = new ArrayList<TiposSueldos>();
      listTSGruposTiposEntidadesModificar = new ArrayList<TSGruposTiposEntidades>();
      listTEFormulasConceptosModificar = new ArrayList<TEFormulasConceptos>();
      editarTipoSueldo = new TiposSueldos();
      editarTSFormulaConcepto = new TSFormulasConceptos();
      editarTSGrupoTipoEntidad = new TSGruposTiposEntidades();
      editarTEFormulaConcepto = new TEFormulasConceptos();
      cualCelda = -1;
      cualCeldaTSFormulas = -1;
      cualCeldaTEFormulas = -1;
      cualCeldaTSGrupos = -1;
      tipoListaTSFormulas = 0;
      tipoListaTEFormulas = 0;
      tipoLista = 0;
      tipoListaTSGrupos = 0;
      guardado = true;
      guardadoTSFormulas = true;
      guardadoTEFormulas = true;
      guardadoTSGrupos = true;
      nuevoTipoSueldo = new TiposSueldos();
      nuevoTSFormulaConcepto = new TSFormulasConceptos();
      nuevoTSFormulaConcepto.setFormula(new Formulas());
      nuevoTSFormulaConcepto.setConcepto(new Conceptos());
      nuevoTSGrupoTipoEntidad = new TSGruposTiposEntidades();
      nuevoTSGrupoTipoEntidad.setGrupotipoentidad(new Grupostiposentidades());
      nuevoTEFormulaConcepto = new TEFormulasConceptos();
      nuevoTEFormulaConcepto.setFormula(new Formulas());
      nuevoTEFormulaConcepto.setTipoentidad(new TiposEntidades());
      nuevoTEFormulaConcepto.setConcepto(new Conceptos());
      duplicarTipoSueldo = new TiposSueldos();
      duplicarTSFormulaConcepto = new TSFormulasConceptos();
      duplicarTEFormulaConcepto = new TEFormulasConceptos();
      duplicarTSGrupoTipoEntidad = new TSGruposTiposEntidades();
      tipoSueldoTablaSeleccionado = null;
      tsFormulaTablaSeleccionado = null;
      teFormulaTablaSeleccionado = null;
      bandera = 0;
      activarLov = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovConceptos = null;
      lovFormulas = null;
      lovGruposTiposEntidades = null;
      lovTiposEntidades = null;
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
         administrarTipoSueldo.obtenerConexion(ses.getId());
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
      String pagActual = "tiposueldo";
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

   public String valorPaginaAnterior() {
      return paginaAnterior;
   }

   public void inicializarPagina(String paginaLlamado) {
      paginaAnterior = paginaLlamado;
      listaTSFormulasConceptos = null;
      listaTiposSueldos = null;
      getListaTiposSueldos();
      if (listaTiposSueldos != null) {
         if (!listaTiposSueldos.isEmpty()) {
            tipoSueldoTablaSeleccionado = listaTiposSueldos.get(0);
            cargarDatos();
         }
      }
      getListaTSGruposTiposEntidades();
      getListaTSFormulasConceptos();
      getListaTEFormulasConceptos();
   }

   public boolean validarCamposNulosTipoSueldo(int i) {
      boolean retorno = true;
      if (i == 0) {
         TiposSueldos aux = new TiposSueldos();
         aux = tipoSueldoTablaSeleccionado;
         if (aux.getCodigo() == null || aux.getDescripcion().isEmpty()) {
            retorno = false;
         } else {
            retorno = true;
         }
         if (aux.getCodigo() != null) {
            if (aux.getCodigo() <= 0) {
               retorno = false;
            } else {
               retorno = true;
            }
         }
      }
      if (i == 1) {
         if (nuevoTipoSueldo.getCodigo() == null || nuevoTipoSueldo.getDescripcion().isEmpty()) {
            retorno = false;
         } else {
            retorno = true;
         }
         if (nuevoTipoSueldo.getCodigo() != null) {
            if (nuevoTipoSueldo.getCodigo() <= 0) {
               retorno = false;
            } else {
               retorno = true;
            }
         }
      }
      if (i == 2) {
         if (duplicarTipoSueldo.getCodigo() == null || duplicarTipoSueldo.getDescripcion().isEmpty()) {
            retorno = false;
         } else {
            retorno = true;
         }
         if (duplicarTipoSueldo.getCodigo() != null) {
            if (duplicarTipoSueldo.getCodigo() <= 0) {
               retorno = false;
            } else {
               retorno = true;
            }
         }
      }
      return retorno;
   }

   public boolean validarCamposNulosTSFormula(int i) {
      boolean retorno = true;
      if (i == 1) {
         if (nuevoTSFormulaConcepto.getConcepto().getSecuencia() == null || nuevoTSFormulaConcepto.getFormula().getSecuencia() == null) {
            retorno = false;
         } else {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarTSFormulaConcepto.getConcepto().getSecuencia() == null || duplicarTSFormulaConcepto.getFormula().getSecuencia() == null) {
            retorno = false;
         } else {
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarCamposNulosTEFormula(int i) {
      boolean retorno = true;
      if (i == 1) {
         if (nuevoTEFormulaConcepto.getConcepto().getSecuencia() == null || nuevoTEFormulaConcepto.getFormula().getSecuencia() == null || nuevoTEFormulaConcepto.getTipoentidad().getSecuencia() == null) {
            retorno = false;
         } else {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarTEFormulaConcepto.getConcepto().getSecuencia() == null || duplicarTEFormulaConcepto.getFormula().getSecuencia() == null || duplicarTEFormulaConcepto.getTipoentidad().getSecuencia() == null) {
            retorno = false;
         } else {
            retorno = true;
         }
      }
      return retorno;
   }

   public boolean validarTipoEntidadYEmpresaNuevoRegistro(int tipoNuevo) {

      boolean retorno = true;
      if (tipoNuevo == 0) {
         int conteo = 0;
         int tam = listaTEFormulasConceptos.size();
         if (tam > 0) {
            List<TEFormulasConceptos> lista = administrarTipoSueldo.listaTEFormulasConceptos();
            if (tipoListaTEFormulas == 0) {
               for (int i = 0; i < lista.size(); i++) {
                  TEFormulasConceptos aux = lista.get(i);
                  if (aux.getConcepto().getEmpresa().equals(teFormulaTablaSeleccionado.getConcepto().getEmpresa())
                          && (aux.getTipoentidad().getSecuencia().equals(teFormulaTablaSeleccionado.getTipoentidad().getSecuencia()))) {
                     conteo++;
                  }
               }
            }
            if (tipoListaTEFormulas == 1) {
               for (int i = 0; i < lista.size(); i++) {
                  TEFormulasConceptos aux = lista.get(i);
                  if ((aux.getConcepto().getEmpresa().equals(teFormulaTablaSeleccionado.getConcepto().getEmpresa()))
                          && (aux.getTipoentidad().getSecuencia().equals(teFormulaTablaSeleccionado.getTipoentidad().getSecuencia()))) {
                     conteo++;
                  }
               }
            }
         }
         if (conteo > 0) {
            retorno = false;
         }
      }
      if (tipoNuevo == 1) {
         int conteo = 0;
         List<TEFormulasConceptos> lista = administrarTipoSueldo.listaTEFormulasConceptos();
         for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getConcepto().getEmpresa().equals(nuevoTEFormulaConcepto.getConcepto().getEmpresa())
                    && lista.get(i).getTipoentidad().getSecuencia().equals(nuevoTEFormulaConcepto.getTipoentidad().getSecuencia())) {
               conteo++;
               break;
            }
         }
         if (conteo > 0) {
            retorno = false;
         }
      }
      if (tipoNuevo == 2) {
         int conteo = 0;
         List<TEFormulasConceptos> lista = administrarTipoSueldo.listaTEFormulasConceptos();
         for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getConcepto().getEmpresa().equals(duplicarTEFormulaConcepto.getConcepto().getEmpresa())
                    && lista.get(i).getTipoentidad().getSecuencia().equals(duplicarTEFormulaConcepto.getTipoentidad().getSecuencia())) {
               conteo++;
               break;
            }
         }
         if (conteo > 0) {
            retorno = false;
         }
      }

      return retorno;
   }

   public void procesoModificacionTipoSueldo(TiposSueldos tipoS) {
      tipoSueldoTablaSeleccionado = tipoS;
      boolean respuesta = validarCamposNulosTipoSueldo(0);
      if (respuesta == true) {
         modificarTipoSueldo(tipoSueldoTablaSeleccionado);
      } else {
         tipoSueldoTablaSeleccionado.setCodigo(tipoSueldoTablaSeleccionado.getCodigo());
         tipoSueldoTablaSeleccionado.setDescripcion(tipoSueldoTablaSeleccionado.getDescripcion());
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoSueldo').show()");
      }
   }

   public void modificarTipoSueldo(TiposSueldos tipoS) {
      int tamDes = 0;
      if (tipoLista == 0) {
         tamDes = tipoSueldoTablaSeleccionado.getDescripcion().length();
      }
      if (tipoLista == 1) {
         tamDes = tipoSueldoTablaSeleccionado.getDescripcion().length();
      }
      if (tamDes >= 1 && tamDes <= 30) {
         if (tipoLista == 0) {
            String textM = tipoSueldoTablaSeleccionado.getDescripcion().toUpperCase();
            tipoSueldoTablaSeleccionado.setDescripcion(textM);
            if (!listTiposSueldosCrear.contains(tipoSueldoTablaSeleccionado)) {
               if (listTiposSueldosModificar.isEmpty()) {
                  listTiposSueldosModificar.add(tipoSueldoTablaSeleccionado);
               } else if (!listTiposSueldosModificar.contains(tipoSueldoTablaSeleccionado)) {
                  listTiposSueldosModificar.add(tipoSueldoTablaSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
         }
         if (tipoLista == 1) {
            String textM = tipoSueldoTablaSeleccionado.getDescripcion().toUpperCase();
            tipoSueldoTablaSeleccionado.setDescripcion(textM);
            if (!listTiposSueldosCrear.contains(tipoSueldoTablaSeleccionado)) {
               if (listTiposSueldosModificar.isEmpty()) {
                  listTiposSueldosModificar.add(tipoSueldoTablaSeleccionado);
               } else if (!listTiposSueldosModificar.contains(tipoSueldoTablaSeleccionado)) {
                  listTiposSueldosModificar.add(tipoSueldoTablaSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
                  //RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
      } else {
         if (tipoLista == 0) {
            tipoSueldoTablaSeleccionado.setDescripcion(tipoSueldoTablaSeleccionado.getDescripcion());
         }
         if (tipoLista == 1) {
            tipoSueldoTablaSeleccionado.setDescripcion(tipoSueldoTablaSeleccionado.getDescripcion());
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
         RequestContext.getCurrentInstance().execute("PF('errorDescripcionTipoSueldo').show()");
      }

   }

   public void modificarTSFormula(TSFormulasConceptos tsformcon) {
      log.info("ControlTipoSueldo.modificarTSFormula()");
      tsFormulaTablaSeleccionado = tsformcon;
      if (!listTSFormulasConceptosCrear.contains(tsFormulaTablaSeleccionado)) {
         if (listTSFormulasConceptosModificar.isEmpty()) {
            listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
         } else if (!listTSFormulasConceptosModificar.contains(tsFormulaTablaSeleccionado)) {
            listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
         }
         if (guardadoTSFormulas == true) {
            guardadoTSFormulas = false;
         }
      }
      cambiosPagina = false;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosTSFormula");
   }

   public void modificarTSFormula(TSFormulasConceptos tsformulac, String confirmarCambio, String valorConfirmar) {
      tsFormulaTablaSeleccionado = tsformulac;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         tsFormulaTablaSeleccionado.getFormula().setNombrelargo(tsFormulaTablaSeleccionado.getFormula().getNombrelargo());
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            tsFormulaTablaSeleccionado.setFormula(lovFormulas.get(indiceUnicoElemento));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexTSFormulas = false;
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = 0;
         }
      }

      if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         tsFormulaTablaSeleccionado.getConcepto().setDescripcion(tsFormulaTablaSeleccionado.getConcepto().getDescripcion());
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            tsFormulaTablaSeleccionado.setConcepto(lovConceptos.get(indiceUnicoElemento));
            lovConceptos.clear();
            getLovConceptos();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexTSFormulas = false;
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
      }

      if (coincidencias == 1) {
         if (!listTSFormulasConceptosCrear.contains(tsFormulaTablaSeleccionado)) {
            if (listTSFormulasConceptosModificar.isEmpty()) {
               listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
            } else if (!listTSFormulasConceptosModificar.contains(tsFormulaTablaSeleccionado)) {
               listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
            }
            if (guardadoTSFormulas == true) {
               guardadoTSFormulas = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosTSFormula");
   }

   public void modificarTSGrupo(TSGruposTiposEntidades tsgrupo) {
      tsGrupoTablaSeleccionado = tsgrupo;
      if (!listTSGruposTiposEntidadesCrear.contains(tsGrupoTablaSeleccionado)) {
         if (listTSGruposTiposEntidadesModificar.isEmpty()) {
            listTSGruposTiposEntidadesModificar.add(tsGrupoTablaSeleccionado);
         } else if (!listTSGruposTiposEntidadesModificar.contains(tsGrupoTablaSeleccionado)) {
            listTSGruposTiposEntidadesModificar.add(tsGrupoTablaSeleccionado);
         }
         if (guardadoTSGrupos == true) {
            guardadoTSGrupos = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      cambiosPagina = false;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosTSGrupo");
   }

   public void modificarTSGrupo(TSGruposTiposEntidades tsgrupo, String confirmarCambio, String valorConfirmar) {
      tsGrupoTablaSeleccionado = tsgrupo;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("GRUPO")) {
         tsGrupoTablaSeleccionado.getGrupotipoentidad().setNombre(tsGrupoTablaSeleccionado.getGrupotipoentidad().getNombre());
         for (int i = 0; i < lovGruposTiposEntidades.size(); i++) {
            if (lovGruposTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            tsGrupoTablaSeleccionado.setGrupotipoentidad(lovGruposTiposEntidades.get(indiceUnicoElemento));
            lovGruposTiposEntidades.clear();
            getLovGruposTiposEntidades();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexTSGrupos = false;
            RequestContext.getCurrentInstance().update("form:GrupoDialogo");
            RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (tipoListaTSGrupos == 0) {
            if (!listTSGruposTiposEntidadesCrear.contains(tsGrupoTablaSeleccionado)) {
               if (listTSGruposTiposEntidadesModificar.isEmpty()) {
                  listTSGruposTiposEntidadesModificar.add(tsGrupoTablaSeleccionado);
               } else if (!listTSGruposTiposEntidadesModificar.contains(tsGrupoTablaSeleccionado)) {
                  listTSGruposTiposEntidadesModificar.add(tsGrupoTablaSeleccionado);
               }
               if (guardadoTSGrupos == true) {
                  guardadoTSGrupos = false;
               }
            }
         }
         if (tipoListaTSGrupos == 1) {
            if (!listTSGruposTiposEntidadesCrear.contains(tsGrupoTablaSeleccionado)) {
               if (listTSGruposTiposEntidadesModificar.isEmpty()) {
                  listTSGruposTiposEntidadesModificar.add(tsGrupoTablaSeleccionado);
               } else if (!listTSGruposTiposEntidadesModificar.contains(tsGrupoTablaSeleccionado)) {
                  listTSGruposTiposEntidadesModificar.add(tsGrupoTablaSeleccionado);
               }
               if (guardadoTSGrupos == true) {
                  guardadoTSGrupos = false;
                  //RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosTSGrupo");
   }

   public void modificarTEFormula(TEFormulasConceptos teformcon) {
      teFormulaTablaSeleccionado = teformcon;
      if (validarTipoEntidadYEmpresaNuevoRegistro(0) == true) {
         if (!listTEFormulasConceptosCrear.contains(teFormulaTablaSeleccionado)) {
            if (listTEFormulasConceptosModificar.isEmpty()) {
               listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
            } else if (!listTEFormulasConceptosModificar.contains(teFormulaTablaSeleccionado)) {
               listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
            }
            if (guardadoTEFormulas == true) {
               guardadoTEFormulas = false;
            }
         }
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTEFormula");
      } else {
         teFormulaTablaSeleccionado.setConcepto(teFormulaTablaSeleccionado.getConcepto());
         teFormulaTablaSeleccionado.setTipoentidad(teFormulaTablaSeleccionado.getTipoentidad());
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorNuevoRegistroTEFormula').show()");
         RequestContext.getCurrentInstance().update("form:datosTEFormula");
      }
   }

   public void modificarTEFormula(TEFormulasConceptos teformcon, String confirmarCambio, String valorConfirmar) {
      teFormulaTablaSeleccionado = teformcon;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("FORMULATE")) {
         teFormulaTablaSeleccionado.getFormula().setNombrelargo(tsFormulaTablaSeleccionado.getFormula().getNombrelargo());
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            teFormulaTablaSeleccionado.setFormula(lovFormulas.get(indiceUnicoElemento));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexTEFormulas = false;
            RequestContext.getCurrentInstance().update("form:FormulaTEDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').show()");
            tipoActualizacion = 0;
         }
      }

      if (confirmarCambio.equalsIgnoreCase("CONCEPTOTE")) {
         if (tipoListaTEFormulas == 0) {
            teFormulaTablaSeleccionado.getConcepto().setDescripcion(tsFormulaTablaSeleccionado.getConcepto().getDescripcion());
         } else {
            teFormulaTablaSeleccionado.getConcepto().setDescripcion(tsFormulaTablaSeleccionado.getConcepto().getDescripcion());
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoListaTEFormulas == 0) {
               teFormulaTablaSeleccionado.setConcepto(lovConceptos.get(indiceUnicoElemento));
            } else {
               teFormulaTablaSeleccionado.setConcepto(lovConceptos.get(indiceUnicoElemento));
            }
            lovConceptos.clear();
            getLovConceptos();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexTEFormulas = false;
            RequestContext.getCurrentInstance().update("form:ConceptoTEDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').show()");
            tipoActualizacion = 0;
         }
      }

      if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
         if (tipoListaTEFormulas == 0) {
            teFormulaTablaSeleccionado.getTipoentidad().setNombre(teFormulaTablaSeleccionado.getTipoentidad().getNombre());
         } else {
            teFormulaTablaSeleccionado.getTipoentidad().setNombre(tsFormulaTablaSeleccionado.getConcepto().getDescripcion());
         }
         for (int i = 0; i < lovTiposEntidades.size(); i++) {
            if (lovTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            teFormulaTablaSeleccionado.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
            lovTiposEntidades.clear();
            getLovTiposEntidades();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexTEFormulas = false;
            RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
            tipoActualizacion = 0;
         }
      }

      if (coincidencias == 1) {
         if (!listTEFormulasConceptosCrear.contains(teFormulaTablaSeleccionado)) {
            if (listTEFormulasConceptosModificar.isEmpty()) {
               listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
            } else if (!listTEFormulasConceptosModificar.contains(teFormulaTablaSeleccionado)) {
               listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
            }
            if (guardadoTEFormulas == true) {
               guardadoTEFormulas = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosTEFormula");
   }

   public void posicionTSFormula() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      tsFormulaTablaSeleccionado = listaTSFormulasConceptos.get(indice);
      cambiarIndiceTSFormula(tsFormulaTablaSeleccionado, columna);
   }

   public void posicionTEFormula() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      teFormulaTablaSeleccionado = listaTEFormulasConceptos.get(indice);
      cambiarIndiceTEFormula(teFormulaTablaSeleccionado, columna);
   }

   public void cambiarIndice(TiposSueldos tipoS, int celda) {
      log.info("ControlTipoSueldo.cambiarIndice()");
      deshabiltiarBotonLov();
      cualtabla = 1;
      cualCelda = celda;
      tipoSueldoTablaSeleccionado = tipoS;
      cargarDatos();
      contarRegistrosFormulas();
      contarRegistrosGrupos();
      contarRegistrosTipoEntidades();
      RequestContext.getCurrentInstance().update("form:datosTSFormula");
      log.info("ControlTipoSueldo.cambiarIndice() 1");
      RequestContext.getCurrentInstance().update("form:datosTSGrupo");
      log.info("ControlTipoSueldo.cambiarIndice() 2");
      RequestContext.getCurrentInstance().update("form:datosTEFormula");
      log.info("ControlTipoSueldo.cambiarIndice() 3");
   }

   public void cargarDatos() {
      log.info("ControlTipoSueldo.cargarDatos()");
      listaTSGruposTiposEntidades = administrarTipoSueldo.listaTSGruposTiposEntidadesParaTipoSueldoSecuencia(tipoSueldoTablaSeleccionado.getSecuencia());
      log.info("ControlTipoSueldo.cargarDatos()2");
      listaTSFormulasConceptos = administrarTipoSueldo.listaTSFormulasConceptosParaTipoSueldoSecuencia(tipoSueldoTablaSeleccionado.getSecuencia());
      log.info("ControlTipoSueldo.cargarDatos()3");
      teFormulaTablaSeleccionado = null;
      if (listaTSGruposTiposEntidades != null) {
         if (!listaTSGruposTiposEntidades.isEmpty()) {
            listaTEFormulasConceptos = administrarTipoSueldo.listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia(listaTSGruposTiposEntidades.get(0).getSecuencia());
            log.info("ControlTipoSueldo.cargarDatos()4");
            tsGrupoTablaSeleccionado = listaTSGruposTiposEntidades.get(0);
         } else {
            listaTEFormulasConceptos = null;
         }
      } else {
         listaTEFormulasConceptos = null;
      }
      log.info("ControlTipoSueldo.cargarDatos() tsGrupoTablaSeleccionado: " + tsGrupoTablaSeleccionado);
   }

   public void cambiarIndiceTSFormula(TSFormulasConceptos tsformcon, int celda) {
      cualCeldaTSFormulas = celda;
      log.info("cambiarIndiceTSFormula celda " + cualCeldaTSFormulas);
      cualtabla = 2;
      if (permitirIndexTSFormulas == true) {
         tsFormulaTablaSeleccionado = tsformcon;
         if (cualCeldaTSFormulas == 0) {
            tsFormulaTablaSeleccionado.getFormula().getNombrelargo();
            habilitarBotonLov();
         } else if (cualCeldaTSFormulas == 1) {
            tsFormulaTablaSeleccionado.getConcepto().getDescripcion();
            habilitarBotonLov();
         } else if (cualCeldaTSFormulas == 2) {
            tsFormulaTablaSeleccionado.getConcepto().getNombreEmpresa();
            deshabiltiarBotonLov();
         }
      }
   }

   public void cambiarIndiceTSGrupo(TSGruposTiposEntidades tsgrupo, int celda) {
      cualtabla = 3;
      if (permitirIndexTSGrupos == true) {
         tsGrupoTablaSeleccionado = tsgrupo;
         cualCeldaTSGrupos = celda;
         if (listaTSGruposTiposEntidades != null) {
            if (!listaTSGruposTiposEntidades.isEmpty()) {
               listaTEFormulasConceptos = administrarTipoSueldo.listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia(tsGrupoTablaSeleccionado.getSecuencia());
            } else {
               listaTEFormulasConceptos = null;
            }
         } else {
            listaTEFormulasConceptos = null;
         }
         habilitarBotonLov();
      }
   }

   public void cambiarIndiceTEFormula(TEFormulasConceptos teformcon, int celda) {
      if (permitirIndexTEFormulas == true) {
         teFormulaTablaSeleccionado = teformcon;
         cualCeldaTEFormulas = celda;
         cualtabla = 4;
         teFormulaTablaSeleccionado.getSecuencia();
         if (cualCeldaTEFormulas == 0) {
            teFormulaTablaSeleccionado.getTipoentidad().getNombre();
            habilitarBotonLov();
         } else if (cualCeldaTEFormulas == 1) {
            teFormulaTablaSeleccionado.getFormula().getNombrelargo();
            habilitarBotonLov();
         } else if (cualCeldaTEFormulas == 2) {
            teFormulaTablaSeleccionado.getConcepto().getDescripcion();
            habilitarBotonLov();
         } else if (cualCeldaTEFormulas == 3) {
            teFormulaTablaSeleccionado.getConcepto().getNombreEmpresa();
            deshabiltiarBotonLov();
         }

      }
   }
   //GUARDAR

   public void guardarYSalir() {
      guardarGeneral();
      salir();
   }

   public void cancelarYSalir() {
      cancelarModificacionGeneral();
      salir();
   }

   public void guardarGeneral() {
      guardarCambiosTipoSueldo();
      guardarCambiosTSFormula();
      guardarCambiosTSGrupo();
      guardarCambiosTEFormula();
      FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:growl");
      cambiosPagina = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void guardarCambiosTipoSueldo() {

      if (!listTiposSueldosBorrar.isEmpty()) {
         for (int i = 0; i < listTiposSueldosBorrar.size(); i++) {
            administrarTipoSueldo.borrarTiposSueldos(listTiposSueldosBorrar);
         }
         listTiposSueldosBorrar.clear();
      }
      if (!listTiposSueldosCrear.isEmpty()) {
         for (int i = 0; i < listTiposSueldosCrear.size(); i++) {
            administrarTipoSueldo.crearTiposSueldos(listTiposSueldosCrear);
         }
         listTiposSueldosCrear.clear();
      }
      if (!listTiposSueldosModificar.isEmpty()) {
         administrarTipoSueldo.editarTiposSueldos(listTiposSueldosModificar);
         listTiposSueldosModificar.clear();
      }
      listaTiposSueldos = null;
      getListaTiposSueldos();
      RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      k = 0;
      contarRegistrosTipoSueldos();

   }

   public void guardarCambiosTSFormula() {
      if (!listTSFormulasConceptosBorrar.isEmpty()) {
         administrarTipoSueldo.borrarTSFormulasConceptos(listTSFormulasConceptosBorrar);
         listTSFormulasConceptosBorrar.clear();
      }
      if (!listTSFormulasConceptosCrear.isEmpty()) {
         administrarTipoSueldo.crearTSFormulasConceptos(listTSFormulasConceptosCrear);
         listTSFormulasConceptosCrear.clear();
      }
      if (!listTSFormulasConceptosModificar.isEmpty()) {
         administrarTipoSueldo.editarTSFormulasConceptos(listTSFormulasConceptosModificar);
         listTSFormulasConceptosModificar.clear();
      }
      listaTSFormulasConceptos = null;
      getListaTSFormulasConceptos();
      RequestContext.getCurrentInstance().update("form:datosTSFormula");
      guardadoTSFormulas = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      k = 0;
      contarRegistrosFormulas();
   }

   public void guardarCambiosTSGrupo() {
      if (!listTSGruposTiposEntidadesBorrar.isEmpty()) {
         administrarTipoSueldo.borrarTSGruposTiposEntidades(listTSGruposTiposEntidadesBorrar);
         listTSGruposTiposEntidadesBorrar.clear();
      }
      if (!listTSGruposTiposEntidadesCrear.isEmpty()) {
         administrarTipoSueldo.crearTSGruposTiposEntidades(listTSGruposTiposEntidadesCrear);
         listTSGruposTiposEntidadesCrear.clear();
      }
      if (!listTSGruposTiposEntidadesModificar.isEmpty()) {
         administrarTipoSueldo.editarTSGruposTiposEntidades(listTSGruposTiposEntidadesModificar);
         listTSGruposTiposEntidadesModificar.clear();
      }
      listaTSGruposTiposEntidades = null;
      getListaTSGruposTiposEntidades();
      RequestContext.getCurrentInstance().update("form:datosTSGrupo");
      guardadoTSGrupos = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      k = 0;
      contarRegistrosGrupos();
   }

   public void guardarCambiosTEFormula() {
      if (!listTEFormulasConceptosBorrar.isEmpty()) {
         administrarTipoSueldo.borrarTEFormulasConceptos(listTEFormulasConceptosBorrar);
         listTEFormulasConceptosBorrar.clear();
      }
      if (!listTEFormulasConceptosCrear.isEmpty()) {
         administrarTipoSueldo.crearTEFormulasConceptos(listTEFormulasConceptosCrear);
         listTEFormulasConceptosCrear.clear();
      }
      if (!listTEFormulasConceptosModificar.isEmpty()) {
         administrarTipoSueldo.editarTEFormulasConceptos(listTEFormulasConceptosModificar);
         listTEFormulasConceptosModificar.clear();
      }
      RequestContext.getCurrentInstance().update("form:datosTEFormula");
      guardadoTEFormulas = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      k = 0;
      contarRegistrosTipoEntidades();
   }
   //CANCELAR MODIFICACIONES

   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacionGeneral() {
      cancelarModificacionTipoSueldo();
      cancelarModificacionTSFormula();
      cancelarModificacionTSGrupo();
      cancelarModificacionTEFormula();
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void cancelarModificacionTipoSueldo() {
      if (bandera == 1) {
         restaurarTablas();
      }
      listTiposSueldosBorrar.clear();
      listTiposSueldosCrear.clear();
      listTiposSueldosModificar.clear();
      tipoSueldoTablaSeleccionado = null;
      k = 0;
      listaTiposSueldos = null;
      guardado = true;
      contarRegistrosTipoSueldos();
      RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
   }

   public void restaurarTablas() {
      altoTablaTiposSueldos = "70";
      tipoSueldoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCodigo");
      tipoSueldoCodigo.setFilterStyle("display: none; visibility: hidden;");
      tipoSueldoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoDescripcion");
      tipoSueldoDescripcion.setFilterStyle("display: none; visibility: hidden;");
      tipoSueldoCap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCap");
      tipoSueldoCap.setFilterStyle("display: none; visibility: hidden;");
      tipoSueldoBas = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoBas");
      tipoSueldoBas.setFilterStyle("display: none; visibility: hidden;");
      tipoSueldoAdi = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoAdi");
      tipoSueldoAdi.setFilterStyle("display: none; visibility: hidden;");

      altoTablaTSFormulas = "210";
      tsFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaConcepto");
      tsFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
      tsFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaFormula");
      tsFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
      tsFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaEmpresa");
      tsFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
      tsFormulaOrigen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaOrigen");
      tsFormulaOrigen.setFilterStyle("display: none; visibility: hidden;");

      altoTablaTSGrupos = "55";
      tsGrupoGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSGrupo:tsGrupoGrupo");
      tsGrupoGrupo.setFilterStyle("display: none; visibility: hidden;");

      altoTablaTEFormulas = "80";
      teFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaConcepto");
      teFormulaConcepto.setFilterStyle("display: none; visibility: hidden;");
      teFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaFormula");
      teFormulaFormula.setFilterStyle("display: none; visibility: hidden;");
      teFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaEmpresa");
      teFormulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
      teFormulaTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaTipoEntidad");
      teFormulaTipoEntidad.setFilterStyle("display: none; visibility: hidden;");

      RequestContext.getCurrentInstance().execute("PF('datosTEFormula').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('datosTSGrupo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('datosTipoSueldo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('datosTSFormula').clearFilters()");
      RequestContext.getCurrentInstance().update("form:datosTEFormula");
      RequestContext.getCurrentInstance().update("form:datosTSGrupo");
      RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
      RequestContext.getCurrentInstance().update("form:datosTSFormula");

      bandera = 0;
      filtrarListaTEFormulasConceptos = null;
      filtrarListaTSGruposTiposEntidades = null;
      filtrarListaTSFormulasConceptos = null;
      filtrarListaTiposSueldos = null;
      tipoListaTEFormulas = 0;
      tipoListaTSGrupos = 0;
      tipoListaTSFormulas = 0;
      tipoLista = 0;
   }

   public void cancelarModificacionTSFormula() {
      if (bandera == 1) {
         restaurarTablas();
      }
      listTSFormulasConceptosBorrar.clear();
      listTSFormulasConceptosCrear.clear();
      listTSFormulasConceptosModificar.clear();
      tsFormulaTablaSeleccionado = null;
      k = 0;
      listaTSFormulasConceptos = null;
      guardadoTSFormulas = true;
      permitirIndexTSFormulas = true;
      contarRegistrosFormulas();
      RequestContext.getCurrentInstance().update("form:datosTSFormula");
   }

   public void cancelarModificacionTSGrupo() {
      if (bandera == 1) {
         restaurarTablas();
      }
      listTSGruposTiposEntidadesBorrar.clear();
      listTSGruposTiposEntidadesCrear.clear();
      listTSGruposTiposEntidadesModificar.clear();
      tsGrupoTablaSeleccionado = null;
      k = 0;
      listaTSGruposTiposEntidades = null;
      guardadoTSGrupos = true;
      permitirIndexTSGrupos = true;
      contarRegistrosGrupos();
      RequestContext.getCurrentInstance().update("form:datosTSGrupo");
   }

   public void cancelarModificacionTEFormula() {
      if (bandera == 1) {
         restaurarTablas();
      }
      listTEFormulasConceptosBorrar.clear();
      listTEFormulasConceptosCrear.clear();
      listTEFormulasConceptosModificar.clear();
      teFormulaTablaSeleccionado = null;
      k = 0;
      listaTEFormulasConceptos = null;
      guardadoTEFormulas = true;
      permitirIndexTEFormulas = true;
      contarRegistrosTipoEntidades();
      RequestContext.getCurrentInstance().update("form:datosTEFormula");
   }

   public void editarCelda() {
      if (cualtabla == 1) {
         editarTipoSueldo = tipoSueldoTablaSeleccionado;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoTipoSueldoD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoTipoSueldoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionTipoSueldoD");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionTipoSueldoD').show()");
            cualCelda = -1;
         }
      }
      if (cualtabla == 2) {
         editarTSFormulaConcepto = tsFormulaTablaSeleccionado;
         if (cualCeldaTSFormulas == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaTSFormulaD");
            RequestContext.getCurrentInstance().execute("PF('editarFormulaTSFormulaD').show()");
            cualCeldaTSFormulas = -1;
         } else if (cualCeldaTSFormulas == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoTSFormulaD");
            RequestContext.getCurrentInstance().execute("PF('editarConceptoTSFormulaD').show()");
            cualCeldaTSFormulas = -1;
         } else if (cualCeldaTSFormulas == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaTSFormulaD");
            RequestContext.getCurrentInstance().execute("PF('editarEmpresaTSFormulaD').show()");
            cualCeldaTSFormulas = -1;
         }
      }
      if (cualtabla == 3) {
         editarTSGrupoTipoEntidad = tsGrupoTablaSeleccionado;
         if (cualCeldaTSGrupos == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarGrupoTSGrupoD");
            RequestContext.getCurrentInstance().execute("PF('editarGrupoTSGrupoD').show()");
            cualCeldaTSGrupos = -1;
         }
      }
      if (cualtabla == 4) {
         editarTEFormulaConcepto = teFormulaTablaSeleccionado;
         if (cualCeldaTEFormulas == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaTETipoEntidadD");
            RequestContext.getCurrentInstance().execute("PF('editarFormulaTETipoEntidadD').show()");
            cualCeldaTEFormulas = -1;
         } else if (cualCeldaTEFormulas == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaTEFormulaD");
            RequestContext.getCurrentInstance().execute("PF('editarFormulaTEFormulaD').show()");
            cualCeldaTEFormulas = -1;
         } else if (cualCeldaTEFormulas == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoTEFormulaD");
            RequestContext.getCurrentInstance().execute("PF('editarConceptoTEFormulaD').show()");
            cualCeldaTEFormulas = -1;
         } else if (cualCeldaTEFormulas == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaTEFormulaD");
            RequestContext.getCurrentInstance().execute("PF('editarEmpresaTEFormulaD').show()");
            cualCeldaTEFormulas = -1;
         }
      }
   }

   public void dialogoNuevoRegistro() {
      if (tipoSueldoTablaSeleccionado != null) {
         activoFormulaConcepto = false;
         activoGrupoDistribucion = false;
         activoTipoEntidad = true;
         if (listaTSGruposTiposEntidades != null) {
            activoTipoEntidad = false;
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
         RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()");
      } else {
         activoFormulaConcepto = true;
         activoGrupoDistribucion = true;
         activoTipoEntidad = true;
         RequestContext.getCurrentInstance().update("formularioDialogos:nrFormula");
         RequestContext.getCurrentInstance().update("formularioDialogos:nrGrupo");
         RequestContext.getCurrentInstance().update("formularioDialogos:nrteFormula");
         RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
         RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()");
      }
   }

   //CREAR 
   public void agregarNuevoTipoSueldo() {
      boolean respueta = validarCamposNulosTipoSueldo(1);
      if (respueta == true) {
         int tamDes = 0;
         tamDes = nuevoTipoSueldo.getDescripcion().length();
         if (tamDes >= 1 && tamDes <= 30) {
            if (bandera == 1) {
               restaurarTablas();
            }
            k++;
            l = BigInteger.valueOf(k);
            String text = nuevoTipoSueldo.getDescripcion().toUpperCase();
            nuevoTipoSueldo.setDescripcion(text);
            nuevoTipoSueldo.setSecuencia(l);
            listTiposSueldosCrear.add(nuevoTipoSueldo);
            listaTiposSueldos.add(nuevoTipoSueldo);
            tipoSueldoTablaSeleccionado = nuevoTipoSueldo;
            nuevoTipoSueldo = new TiposSueldos();
            cambiosPagina = false;
            contarRegistrosTipoSueldos();
            RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTipoSueldo').hide()");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorDescripcionTipoSueldo').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoSueldo').show()");
      }
   }

   public void agregarNuevoTSFormula() {
      boolean respueta = validarCamposNulosTSFormula(1);
      if (respueta == true) {
         if (bandera == 1) {
            restaurarTablas();
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoTSFormulaConcepto.setSecuencia(l);
         nuevoTSFormulaConcepto.setTiposueldo(tipoSueldoTablaSeleccionado);
         if (listaTSFormulasConceptos.size() == 0) {
            listaTSFormulasConceptos = new ArrayList<TSFormulasConceptos>();
         }
         listTSFormulasConceptosCrear.add(nuevoTSFormulaConcepto);
         listaTSFormulasConceptos.add(nuevoTSFormulaConcepto);
         tsFormulaTablaSeleccionado = nuevoTSFormulaConcepto;
         contarRegistrosFormulas();
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:datosTSFormula");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTSFormula').hide()");
         guardadoTSFormulas = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         nuevoTSFormulaConcepto = new TSFormulasConceptos();
         nuevoTSFormulaConcepto.setFormula(new Formulas());
         nuevoTSFormulaConcepto.setConcepto(new Conceptos());
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTSFormula').show()");
      }
   }

   public void agregarNuevoTSGrupo() {
      if (nuevoTSGrupoTipoEntidad.getGrupotipoentidad().getSecuencia() != null) {
         if (bandera == 1) {
            restaurarTablas();
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoTSGrupoTipoEntidad.setSecuencia(l);
         nuevoTSGrupoTipoEntidad.setTiposueldo(tipoSueldoTablaSeleccionado);
         if (listaTSGruposTiposEntidades.size() == 0) {
            listaTSGruposTiposEntidades = new ArrayList<TSGruposTiposEntidades>();
         }
         listTSGruposTiposEntidadesCrear.add(nuevoTSGrupoTipoEntidad);
         listaTSGruposTiposEntidades.add(nuevoTSGrupoTipoEntidad);
         tsGrupoTablaSeleccionado = nuevoTSGrupoTipoEntidad;
         contarRegistrosGrupos();
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTSGrupo");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTSGrupo').hide()");
         nuevoTSGrupoTipoEntidad = new TSGruposTiposEntidades();
         nuevoTSGrupoTipoEntidad.setGrupotipoentidad(new Grupostiposentidades());
         guardadoTSGrupos = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTSGrupo').show()");
      }
   }

   public void agregarNuevoTEFormula() {
      boolean respueta = validarCamposNulosTEFormula(1);
      if (respueta == true) {
         if (validarTipoEntidadYEmpresaNuevoRegistro(1) == true) {
            if (bandera == 1) {
               restaurarTablas();
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTEFormulaConcepto.setSecuencia(l);
            nuevoTEFormulaConcepto.setTsgrupotipoentidad(tsGrupoTablaSeleccionado);
            if (listaTEFormulasConceptos.size() == 0) {
               listaTEFormulasConceptos = new ArrayList<TEFormulasConceptos>();
            }
            listTEFormulasConceptosCrear.add(nuevoTEFormulaConcepto);
            listaTEFormulasConceptos.add(nuevoTEFormulaConcepto);
            teFormulaTablaSeleccionado = nuevoTEFormulaConcepto;
            contarRegistrosTipoEntidades();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTEFormula').hide()");
            nuevoTEFormulaConcepto = new TEFormulasConceptos();
            nuevoTEFormulaConcepto.setFormula(new Formulas());
            nuevoTEFormulaConcepto.setTipoentidad(new TiposEntidades());
            nuevoTEFormulaConcepto.setConcepto(new Conceptos());
            guardadoTEFormulas = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorNuevoRegistroTEFormula').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTEFormula').show()");
      }
   }
   //LIMPIAR NUEVO REGISTRO

   /**
    */
   public void limpiarNuevaTipoSueldo() {
      nuevoTipoSueldo = new TiposSueldos();
   }

   public void limpiarNuevaTSFormula() {
      nuevoTSFormulaConcepto = new TSFormulasConceptos();
      nuevoTSFormulaConcepto.setFormula(new Formulas());
      nuevoTSFormulaConcepto.setConcepto(new Conceptos());
   }

   public void limpiarNuevaTSGrupo() {
      nuevoTSGrupoTipoEntidad = new TSGruposTiposEntidades();
      nuevoTSGrupoTipoEntidad.setGrupotipoentidad(new Grupostiposentidades());
   }

   public void limpiarNuevaTEFormula() {
      nuevoTEFormulaConcepto = new TEFormulasConceptos();
      nuevoTEFormulaConcepto.setFormula(new Formulas());
      nuevoTEFormulaConcepto.setTipoentidad(new TiposEntidades());
      nuevoTEFormulaConcepto.setConcepto(new Conceptos());
   }

   //DUPLICAR VC
   /**
    */
   public void verificarRegistroDuplicar() {
      if (cualtabla == 1) {
         duplicarTipoSueldoM();
      }
      if (cualtabla == 2) {
         duplicarTSFormulaM();
      }
      if (cualtabla == 3) {
         duplicarTSGrupoM();
      }
      if (cualtabla == 4) {
         duplicarTEFormulaM();
      }
   }

   public void duplicarTipoSueldoM() {
      duplicarTipoSueldo = new TiposSueldos();
      k++;
      l = BigInteger.valueOf(k);
      duplicarTipoSueldo.setCodigo(tipoSueldoTablaSeleccionado.getCodigo());
      duplicarTipoSueldo.setDescripcion(tipoSueldoTablaSeleccionado.getDescripcion());
      duplicarTipoSueldo.setBasico(tipoSueldoTablaSeleccionado.getBasico());
      duplicarTipoSueldo.setCapacidadendeudamiento(tipoSueldoTablaSeleccionado.getCapacidadendeudamiento());
      duplicarTipoSueldo.setAdicionalbasico(tipoSueldoTablaSeleccionado.getAdicionalbasico());
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTipoSueldo");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoSueldo').show()");

   }

   public void duplicarTSFormulaM() {
      duplicarTSFormulaConcepto = new TSFormulasConceptos();
      k++;
      l = BigInteger.valueOf(k);
      duplicarTSFormulaConcepto.setOrigen(tsFormulaTablaSeleccionado.getOrigen());
      duplicarTSFormulaConcepto.setFormula(tsFormulaTablaSeleccionado.getFormula());
      duplicarTSFormulaConcepto.setConcepto(tsFormulaTablaSeleccionado.getConcepto());
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTSFormula");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTSFormula').show()");

   }

   public void duplicarTSGrupoM() {
      duplicarTSGrupoTipoEntidad = new TSGruposTiposEntidades();
      k++;
      l = BigInteger.valueOf(k);
      duplicarTSGrupoTipoEntidad.setGrupotipoentidad(tsGrupoTablaSeleccionado.getGrupotipoentidad());
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTSGrupo");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTSGrupo').show()");

   }

   public void duplicarTEFormulaM() {
      duplicarTEFormulaConcepto = new TEFormulasConceptos();

      duplicarTEFormulaConcepto.setTipoentidad(teFormulaTablaSeleccionado.getTipoentidad());
      duplicarTEFormulaConcepto.setFormula(teFormulaTablaSeleccionado.getFormula());
      duplicarTEFormulaConcepto.setConcepto(teFormulaTablaSeleccionado.getConcepto());
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTEFormula");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTEFormula').show()");

   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
    */
   public void confirmarDuplicarTipoSueldo() {
      boolean respueta = validarCamposNulosTipoSueldo(2);
      if (respueta == true) {
         if (bandera == 1) {
            restaurarTablas();
         }
         k++;
         l = BigInteger.valueOf(k);
         duplicarTipoSueldo.setSecuencia(l);
         String text = duplicarTipoSueldo.getDescripcion().toUpperCase();
         duplicarTipoSueldo.setDescripcion(text);
         listaTiposSueldos.add(duplicarTipoSueldo);
         listTiposSueldosCrear.add(duplicarTipoSueldo);
         tipoSueldoTablaSeleccionado = duplicarTipoSueldo;
         cambiosPagina = false;
         contarRegistrosTipoSueldos();
         RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTipoSueldo').hide()");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         duplicarTipoSueldo = new TiposSueldos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTipoSueldo').show()");
      }
   }

   public void confirmarDuplicarTSFormula() {
      boolean respueta = validarCamposNulosTSFormula(2);
      if (respueta == true) {
         if (bandera == 1) {
            restaurarTablas();
         }
         k++;
         l = BigInteger.valueOf(k);
         duplicarTSFormulaConcepto.setSecuencia(l);
         duplicarTSFormulaConcepto.setTiposueldo(tipoSueldoTablaSeleccionado);
         listaTSFormulasConceptos.add(duplicarTSFormulaConcepto);
         listTSFormulasConceptosCrear.add(duplicarTSFormulaConcepto);
         tsFormulaTablaSeleccionado = duplicarTSFormulaConcepto;
         contarRegistrosFormulas();
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:datosTSFormula");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTSFormula').hide()");
         if (guardadoTSFormulas == true) {
            guardadoTSFormulas = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         duplicarTSFormulaConcepto = new TSFormulasConceptos();
         duplicarTSFormulaConcepto.setFormula(new Formulas());
         duplicarTSFormulaConcepto.setConcepto(new Conceptos());
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTSFormula').show()");
      }
   }

   public void confirmarDuplicarTSGrupo() {
      log.info("ControlTipoSueldo.confirmarDuplicarTSGrupo() duplicarTSGrupoTipoEntidad.getGrupotipoentidad() : " + duplicarTSGrupoTipoEntidad.getGrupotipoentidad());
      if (duplicarTSGrupoTipoEntidad.getGrupotipoentidad() != null) {
         if (duplicarTSGrupoTipoEntidad.getGrupotipoentidad().getSecuencia() != null) {
            int duplicados = 0;
            int contador = 0;
            for (int i = 0; i < listaTSGruposTiposEntidades.size(); i++) {
               if (duplicarTSGrupoTipoEntidad.getGrupotipoentidad() == listaTSGruposTiposEntidades.get(i).getGrupotipoentidad()) {
                  duplicados++;
               }
            }
            if (duplicados == 0) {
               contador++;
            }
            if (contador == 1) {
               if (bandera == 1) {
                  restaurarTablas();
               }
               duplicarTSGrupoTipoEntidad.setTiposueldo(tipoSueldoTablaSeleccionado);
               k++;
               l = BigInteger.valueOf(k);
               duplicarTSGrupoTipoEntidad.setSecuencia(l);
               listaTSGruposTiposEntidades.add(duplicarTSGrupoTipoEntidad);
               listTSGruposTiposEntidadesCrear.add(duplicarTSGrupoTipoEntidad);
               tsGrupoTablaSeleccionado = duplicarTSGrupoTipoEntidad;
               contarRegistrosGrupos();
               cambiosPagina = false;
               RequestContext context = RequestContext.getCurrentInstance();
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
               RequestContext.getCurrentInstance().update("form:datosTSGrupo");
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTSGrupo').hide()");
               if (guardadoTSGrupos == true) {
                  guardadoTSGrupos = false;
               }
               duplicarTSGrupoTipoEntidad = new TSGruposTiposEntidades();
               duplicarTSGrupoTipoEntidad.setGrupotipoentidad(new Grupostiposentidades());
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorGrupoRepetido').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullTSGrupo').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTSGrupo').show()");
      }
   }

   public void confirmarDuplicarTEFormula() {
      boolean respueta = validarCamposNulosTEFormula(2);
      if (respueta == true) {
         if (validarTipoEntidadYEmpresaNuevoRegistro(2) == true) {
            if (bandera == 1) {
               restaurarTablas();
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarTEFormulaConcepto.setSecuencia(l);
            duplicarTEFormulaConcepto.setTsgrupotipoentidad(tsGrupoTablaSeleccionado);
            listaTEFormulasConceptos.add(duplicarTEFormulaConcepto);
            listTEFormulasConceptosCrear.add(duplicarTEFormulaConcepto);
            teFormulaTablaSeleccionado = duplicarTEFormulaConcepto;
            contarRegistrosTipoEntidades();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTEFormula");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroTEFormula').hide()");
            if (guardadoTEFormulas == true) {
               guardadoTEFormulas = false;
               //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            duplicarTEFormulaConcepto = new TEFormulasConceptos();
            duplicarTEFormulaConcepto.setFormula(new Formulas());
            duplicarTEFormulaConcepto.setTipoentidad(new TiposEntidades());
            duplicarTEFormulaConcepto.setConcepto(new Conceptos());
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorNuevoRegistroTEFormula').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullTEFormula').show()");
      }
   }

   //LIMPIAR DUPLICAR
   /**
    * Metodo que limpia los datos de un duplicar Set
    */
   public void limpiarDuplicarTipoSueldo() {
      duplicarTipoSueldo = new TiposSueldos();
   }

   public void limpiarDuplicarTSFormula() {
      duplicarTSFormulaConcepto = new TSFormulasConceptos();
      duplicarTSFormulaConcepto.setFormula(new Formulas());
      duplicarTSFormulaConcepto.setConcepto(new Conceptos());
   }

   public void limpiarDuplicarTSGrupo() {
      duplicarTSGrupoTipoEntidad = new TSGruposTiposEntidades();
      duplicarTSGrupoTipoEntidad.setGrupotipoentidad(new Grupostiposentidades());
   }

   public void limpiarDuplicarTEFormula() {
      duplicarTEFormulaConcepto = new TEFormulasConceptos();
      duplicarTEFormulaConcepto.setFormula(new Formulas());
      duplicarTEFormulaConcepto.setTipoentidad(new TiposEntidades());
      duplicarTEFormulaConcepto.setConcepto(new Conceptos());
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   //BORRAR VC
   /**
    */
   public void verificarRegistroBorrar() {
      if (cualtabla == 1) {
         if (listaTSFormulasConceptos.size() == 0) {
            borrarTipoSueldo();
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
         }
      }
      if (cualtabla == 2) {
         borrarTSFormula();
      }
      if (cualtabla == 3) {
         int tam = listaTEFormulasConceptos.size();
         if (tam == 0) {
            borrarTSGrupo();
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistroTSGrupo').show()");
         }
      }
      if (cualtabla == 4) {
         borrarTEFormula();
      }
   }

   public void borrarTipoSueldo() {
      if (tipoSueldoTablaSeleccionado != null) {
         if (!listTiposSueldosModificar.isEmpty() && listTiposSueldosModificar.contains(tipoSueldoTablaSeleccionado)) {
            int modIndex = listTiposSueldosModificar.indexOf(tipoSueldoTablaSeleccionado);
            listTiposSueldosModificar.remove(modIndex);
            listTiposSueldosBorrar.add(tipoSueldoTablaSeleccionado);
         } else if (!listTiposSueldosCrear.isEmpty() && listTiposSueldosCrear.contains(tipoSueldoTablaSeleccionado)) {
            int crearIndex = listTiposSueldosCrear.indexOf(tipoSueldoTablaSeleccionado);
            listTiposSueldosCrear.remove(crearIndex);
         } else {
            listTiposSueldosBorrar.add(tipoSueldoTablaSeleccionado);
         }
         listaTiposSueldos.remove(tipoSueldoTablaSeleccionado);
         if (tipoLista == 1) {
            filtrarListaTiposSueldos.remove(tipoSueldoTablaSeleccionado);
         }
         tipoSueldoTablaSeleccionado = null;
         contarRegistrosTipoSueldos();
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTipoSueldo");

         if (guardado == true) {
            guardado = false;
         }
      }
   }

   public void borrarTSFormula() {
      if (tsFormulaTablaSeleccionado != null) {
         if (!listTSFormulasConceptosModificar.isEmpty() && listTSFormulasConceptosModificar.contains(tsFormulaTablaSeleccionado)) {
            int modIndex = listTSFormulasConceptosModificar.indexOf(tsFormulaTablaSeleccionado);
            listTSFormulasConceptosModificar.remove(modIndex);
            listTSFormulasConceptosBorrar.add(tsFormulaTablaSeleccionado);
         } else if (!listTSFormulasConceptosCrear.isEmpty() && listTSFormulasConceptosCrear.contains(tsFormulaTablaSeleccionado)) {
            int crearIndex = listTSFormulasConceptosCrear.indexOf(tsFormulaTablaSeleccionado);
            listTSFormulasConceptosCrear.remove(crearIndex);
         } else {
            listTSFormulasConceptosBorrar.add(tsFormulaTablaSeleccionado);
         }
         listaTSFormulasConceptos.remove(tsFormulaTablaSeleccionado);
         if (tipoListaTSFormulas == 1) {
            filtrarListaTSFormulasConceptos.remove(tsFormulaTablaSeleccionado);
         }
         tsFormulaTablaSeleccionado = null;
         contarRegistrosFormulas();
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTSFormula");

         if (guardadoTSFormulas == true) {
            guardadoTSFormulas = false;
            //RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void borrarTSGrupo() {
      if (tsGrupoTablaSeleccionado != null) {
         if (!listTSGruposTiposEntidadesModificar.isEmpty() && listTSGruposTiposEntidadesModificar.contains(tsGrupoTablaSeleccionado)) {
            int modIndex = listTSGruposTiposEntidadesModificar.indexOf(tsGrupoTablaSeleccionado);
            listTSGruposTiposEntidadesModificar.remove(modIndex);
            listTSGruposTiposEntidadesBorrar.add(tsGrupoTablaSeleccionado);
         } else if (!listTSGruposTiposEntidadesCrear.isEmpty() && listTSGruposTiposEntidadesCrear.contains(tsGrupoTablaSeleccionado)) {
            int crearIndex = listTSGruposTiposEntidadesCrear.indexOf(tsGrupoTablaSeleccionado);
            listTSGruposTiposEntidadesCrear.remove(crearIndex);
         } else {
            listTSGruposTiposEntidadesBorrar.add(tsGrupoTablaSeleccionado);
         }
         listaTSGruposTiposEntidades.remove(tsGrupoTablaSeleccionado);
         if (tipoListaTSGrupos == 1) {
            filtrarListaTSGruposTiposEntidades.remove(tsGrupoTablaSeleccionado);
         }
         tsGrupoTablaSeleccionado = null;
         contarRegistrosGrupos();
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTSGrupo");

         if (guardadoTSGrupos == true) {
            guardadoTSGrupos = false;
            //RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void borrarTEFormula() {
      if (teFormulaTablaSeleccionado != null) {
         if (!listTEFormulasConceptosModificar.isEmpty() && listTEFormulasConceptosModificar.contains(teFormulaTablaSeleccionado)) {
            int modIndex = listTEFormulasConceptosModificar.indexOf(teFormulaTablaSeleccionado);
            listTEFormulasConceptosModificar.remove(modIndex);
            listTEFormulasConceptosBorrar.add(teFormulaTablaSeleccionado);
         } else if (!listTEFormulasConceptosCrear.isEmpty() && listTEFormulasConceptosCrear.contains(teFormulaTablaSeleccionado)) {
            int crearIndex = listTEFormulasConceptosCrear.indexOf(teFormulaTablaSeleccionado);
            listTEFormulasConceptosCrear.remove(crearIndex);
         } else {
            listTEFormulasConceptosBorrar.add(teFormulaTablaSeleccionado);
         }
         listaTEFormulasConceptos.remove(teFormulaTablaSeleccionado);
         if (tipoListaTEFormulas == 1) {
            filtrarListaTEFormulasConceptos.remove(teFormulaTablaSeleccionado);
         }
         teFormulaTablaSeleccionado = null;
         contarRegistrosTipoEntidades();
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTEFormula");

         if (guardadoTEFormulas == true) {
            guardadoTEFormulas = false;
            //RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }
   //CTRL + F11 ACTIVAR/DESACTIVAR

   /**
    * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      if (bandera == 0) {
         altoTablaTiposSueldos = "50";
         tipoSueldoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCodigo");
         tipoSueldoCodigo.setFilterStyle("width: 85% !important;");
         tipoSueldoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoDescripcion");
         tipoSueldoDescripcion.setFilterStyle("width: 85% !important;");
         tipoSueldoCap = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoCap");
         tipoSueldoCap.setFilterStyle("width: 85% !important;");
         tipoSueldoBas = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoBas");
         tipoSueldoBas.setFilterStyle("width: 85% !important;");
         tipoSueldoAdi = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoSueldo:tipoSueldoAdi");
         tipoSueldoAdi.setFilterStyle("width: 85% !important;");

         altoTablaTSFormulas = "190";
         tsFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaConcepto");
         tsFormulaConcepto.setFilterStyle("width: 85% !important;");
         tsFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaFormula");
         tsFormulaFormula.setFilterStyle("width: 85% !important;");
         tsFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaEmpresa");
         tsFormulaEmpresa.setFilterStyle("width: 85% !important;");
         tsFormulaOrigen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSFormula:tsFormulaOrigen");
         tsFormulaOrigen.setFilterStyle("width: 85% !important;");

         altoTablaTSGrupos = "35";
         tsGrupoGrupo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTSGrupo:tsGrupoGrupo");
         tsGrupoGrupo.setFilterStyle("width: 85% !important;");

         altoTablaTEFormulas = "60";
         teFormulaConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaConcepto");
         teFormulaConcepto.setFilterStyle("width: 85% !important;");
         teFormulaFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaFormula");
         teFormulaFormula.setFilterStyle("width: 85% !important;");
         teFormulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaEmpresa");
         teFormulaEmpresa.setFilterStyle("width: 85% !important;");
         teFormulaTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTEFormula:teFormulaTipoEntidad");
         teFormulaTipoEntidad.setFilterStyle("width: 85% !important;");

         RequestContext.getCurrentInstance().update("form:datosTEFormula");
         RequestContext.getCurrentInstance().update("form:datosTSGrupo");
         RequestContext.getCurrentInstance().update("form:datosTSFormula");
         RequestContext.getCurrentInstance().update("form:datosTipoSueldo");
         bandera = 1;
      } else {
         restaurarTablas();
      }
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         restaurarTablas();
      }
      if (bandera == 1) {
         restaurarTablas();
      }
      if (bandera == 1) {
         restaurarTablas();
      }
      if (bandera == 1) {
         restaurarTablas();
      }
      listTiposSueldosBorrar.clear();
      listTiposSueldosCrear.clear();
      listTiposSueldosModificar.clear();
      listTSFormulasConceptosBorrar.clear();
      listTSFormulasConceptosCrear.clear();
      listTSFormulasConceptosModificar.clear();
      listTSGruposTiposEntidadesBorrar.clear();
      listTSGruposTiposEntidadesCrear.clear();
      listTSGruposTiposEntidadesModificar.clear();
      listTEFormulasConceptosBorrar.clear();
      listTEFormulasConceptosCrear.clear();
      listTEFormulasConceptosModificar.clear();
      tipoSueldoTablaSeleccionado = null;
      tsFormulaTablaSeleccionado = null;
      teFormulaTablaSeleccionado = null;
      tsGrupoTablaSeleccionado = null;
      k = 0;
      listaTiposSueldos = null;
      listaTSFormulasConceptos = null;
      listaTEFormulasConceptos = null;
      listaTSGruposTiposEntidades = null;
      guardado = true;
      guardadoTSFormulas = true;
      guardadoTEFormulas = true;
      guardadoTSGrupos = true;
      cambiosPagina = true;
      lovConceptos = null;
      lovFormulas = null;
      lovGruposTiposEntidades = null;
      lovTiposEntidades = null;
      contarRegistrosTipoSueldos();
      contarRegistrosFormulas();
      contarRegistrosGrupos();
      contarRegistrosTipoEntidades();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cualtabla == 2) {
         if (cualCeldaTSFormulas == 0) {
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaTSFormulas == 1) {
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (cualtabla == 3) {
         if (cualCeldaTSGrupos == 0) {
            RequestContext.getCurrentInstance().update("form:GrupoDialogo");
            RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (cualtabla == 4) {
         if (cualCeldaTEFormulas == 0) {
            RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaTEFormulas == 1) {
            RequestContext.getCurrentInstance().update("form:FormulaTEDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCeldaTEFormulas == 2) {
            RequestContext.getCurrentInstance().update("form:ConceptoTEDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndexTsGrupos(TSGruposTiposEntidades tsgrupo, int dlg, int LND) {
      tsGrupoTablaSeleccionado = tsgrupo;
      tipoActualizacion = LND;
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:GrupoDialogo");
         RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').show()");
      }

   }

   public void asignarIndexTeFormulas(TEFormulasConceptos teformcon, int dlg, int LND) {
      teFormulaTablaSeleccionado = teformcon;
      tipoActualizacion = LND;
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
      }
      if (dlg == 1) {
         RequestContext.getCurrentInstance().update("form:FormulaTEDialogo");
         RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').show()");
      }
      if (dlg == 2) {
         RequestContext.getCurrentInstance().update("form:ConceptoTEDialogo");
         RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').show()");
      }
   }

   public void asignarIndexTsFormulas(TSFormulasConceptos tsformulacon, int dlg, int LND) {
      tsFormulaTablaSeleccionado = tsformulacon;
      tipoActualizacion = LND;
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:FormulaDialogo");
         RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
      }
      if (dlg == 1) {
         RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
      }
   }

   public void asignarIndexTsFormulas(int dlg, int LND) {
      tipoActualizacion = LND;
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:FormulaDialogo");
         RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
      }
      if (dlg == 1) {
         RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
      }
   }

   public void autocompletarNuevoyDuplicadoTSFormula(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         if (tipoNuevo == 1) {
            nuevoTSFormulaConcepto.getFormula().setNombrelargo(tsFormulaTablaSeleccionado.getFormula().getNombrelargo());
         } else if (tipoNuevo == 2) {
            duplicarTSFormulaConcepto.getFormula().setNombrelargo(tsFormulaTablaSeleccionado.getFormula().getNombrelargo());
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoTSFormulaConcepto.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaFormula");
            } else if (tipoNuevo == 2) {
               duplicarTSFormulaConcepto.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaFormula");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaFormula");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaFormula");
            }
         }
      }
      if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         if (tipoNuevo == 1) {
            nuevoTSFormulaConcepto.getConcepto().setDescripcion(tsFormulaTablaSeleccionado.getConcepto().getDescripcion());
         } else if (tipoNuevo == 2) {
            duplicarTSFormulaConcepto.getConcepto().setDescripcion(tsFormulaTablaSeleccionado.getConcepto().getDescripcion());
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoTSFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaCConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaEmpresa");
            } else if (tipoNuevo == 2) {
               duplicarTSFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaCConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaEmpresa");
            }
            lovConceptos.clear();
            getLovConceptos();
         } else {
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaCConcepto");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaCConcepto");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoTSGrupo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("GRUPO")) {
         if (tipoNuevo == 1) {
            nuevoTSGrupoTipoEntidad.getGrupotipoentidad().setNombre(tsGrupoTablaSeleccionado.getGrupotipoentidad().getNombre());
         } else if (tipoNuevo == 2) {
            duplicarTSGrupoTipoEntidad.getGrupotipoentidad().setNombre(tsGrupoTablaSeleccionado.getGrupotipoentidad().getNombre());
         }
         for (int i = 0; i < lovGruposTiposEntidades.size(); i++) {
            if (lovGruposTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoTSGrupoTipoEntidad.setGrupotipoentidad(lovGruposTiposEntidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSGrupoGrupo");
            } else if (tipoNuevo == 2) {
               duplicarTSGrupoTipoEntidad.setGrupotipoentidad(lovGruposTiposEntidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSGrupoGrupo");
            }
            lovGruposTiposEntidades.clear();
            getLovGruposTiposEntidades();
         } else {
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSGrupoGrupo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSGrupoGrupo");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoTEFormula(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
         if (tipoNuevo == 1) {
            nuevoTEFormulaConcepto.getTipoentidad().setNombre(tsFormulaTablaSeleccionado.getFormula().getNombrelargo());
         } else if (tipoNuevo == 2) {
            duplicarTEFormulaConcepto.getTipoentidad().setNombre(tsFormulaTablaSeleccionado.getFormula().getNombrelargo());
         }
         for (int i = 0; i < lovTiposEntidades.size(); i++) {
            if (lovTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoTEFormulaConcepto.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaTipoEntidad");
            } else if (tipoNuevo == 2) {
               duplicarTEFormulaConcepto.setTipoentidad(lovTiposEntidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaTipoEntidad");
            }
            lovTiposEntidades.clear();
            getLovTiposEntidades();
         } else {
            RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaTipoEntidad");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaTipoEntidad");
            }
         }
      }
      if (confirmarCambio.equalsIgnoreCase("FORMULATE")) {
         if (tipoNuevo == 1) {
            nuevoTEFormulaConcepto.getFormula().setNombrelargo(tsFormulaTablaSeleccionado.getFormula().getNombrelargo());
         } else if (tipoNuevo == 2) {
            duplicarTEFormulaConcepto.getFormula().setNombrelargo(tsFormulaTablaSeleccionado.getFormula().getNombrelargo());
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoTEFormulaConcepto.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaFormula");
            } else if (tipoNuevo == 2) {
               duplicarTEFormulaConcepto.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaFormula");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:FormulaTEDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaFormula");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaFormula");
            }
         }
      }
      if (confirmarCambio.equalsIgnoreCase("CONCEPTOTE")) {
         if (tipoNuevo == 1) {
            nuevoTEFormulaConcepto.getConcepto().setDescripcion(tsFormulaTablaSeleccionado.getConcepto().getDescripcion());
         } else if (tipoNuevo == 2) {
            duplicarTEFormulaConcepto.getConcepto().setDescripcion(tsFormulaTablaSeleccionado.getConcepto().getDescripcion());
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoTEFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaCConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaEmpresa");
            } else if (tipoNuevo == 2) {
               duplicarTEFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaCConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaEmpresa");
            }
            lovConceptos.clear();
            getLovConceptos();
         } else {
            RequestContext.getCurrentInstance().update("form:ConceptoTEDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaCConcepto");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaCConcepto");
            }
         }
      }
   }

   public void actualizarFormula() {
      if (tipoActualizacion == 0) {
         if (tipoListaTSFormulas == 0) {
            tsFormulaTablaSeleccionado.setFormula(formulaSeleccionado);
            if (!listTSFormulasConceptosCrear.contains(tsFormulaTablaSeleccionado)) {
               if (listTSFormulasConceptosModificar.isEmpty()) {
                  listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
               } else if (!listTSFormulasConceptosModificar.contains(tsFormulaTablaSeleccionado)) {
                  listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
               }
            }
         } else {
            tsFormulaTablaSeleccionado.setFormula(formulaSeleccionado);
            if (!listTSFormulasConceptosCrear.contains(tsFormulaTablaSeleccionado)) {
               if (listTSFormulasConceptosModificar.isEmpty()) {
                  listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
               } else if (!listTSFormulasConceptosModificar.contains(tsFormulaTablaSeleccionado)) {
                  listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
               }
            }
         }
         if (guardadoTSFormulas == true) {
            guardadoTSFormulas = false;
         }
         permitirIndexTSFormulas = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTSFormula");
      } else if (tipoActualizacion == 1) {
         nuevoTSFormulaConcepto.setFormula(formulaSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaFormula");
      } else if (tipoActualizacion == 2) {
         duplicarTSFormulaConcepto.setFormula(formulaSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaFormula");
      }
      filtrarLovFormulas = null;
      formulaSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:FormulaDialogo");
      RequestContext.getCurrentInstance().update("form:lovFormula");
      RequestContext.getCurrentInstance().update("form:aceptarF");
      context.reset("form:lovFormula:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').hide()");
   }

   public void cancelarCambioFormula() {
      filtrarLovFormulas = null;
      formulaSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexTSFormulas = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovFormula:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').hide()");
   }

   public void actualizarConcepto() {
      if (tipoActualizacion == 0) {
         if (tipoListaTSFormulas == 0) {
            tsFormulaTablaSeleccionado.setConcepto(conceptoSeleccionado);
            if (!listTSFormulasConceptosCrear.contains(tsFormulaTablaSeleccionado)) {
               if (listTSFormulasConceptosModificar.isEmpty()) {
                  listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
               } else if (!listTSFormulasConceptosModificar.contains(tsFormulaTablaSeleccionado)) {
                  listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
               }
            }
         } else {
            tsFormulaTablaSeleccionado.setConcepto(conceptoSeleccionado);
            if (!listTSFormulasConceptosCrear.contains(tsFormulaTablaSeleccionado)) {
               if (listTSFormulasConceptosModificar.isEmpty()) {
                  listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
               } else if (!listTSFormulasConceptosModificar.contains(tsFormulaTablaSeleccionado)) {
                  listTSFormulasConceptosModificar.add(tsFormulaTablaSeleccionado);
               }
            }
         }
         if (guardadoTSFormulas == true) {
            guardadoTSFormulas = false;
         }
         permitirIndexTSFormulas = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTSFormula");
      } else if (tipoActualizacion == 1) {
         nuevoTSFormulaConcepto.setConcepto(conceptoSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaCConcepto");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSFormulaEmpresa");
      } else if (tipoActualizacion == 2) {
         duplicarTSFormulaConcepto.setConcepto(conceptoSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaCConcepto");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSFormulaEmpresa");
      }
      filtrarLovConceptos = null;
      conceptoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
      RequestContext.getCurrentInstance().update("form:lovConcepto");
      RequestContext.getCurrentInstance().update("form:aceptarC");
      context.reset("form:lovConcepto:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConcepto').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').hide()");
   }

   public void cancelarCambioConcepto() {
      filtrarLovConceptos = null;
      conceptoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexTSFormulas = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovConcepto:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConcepto').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').hide()");
   }

   public void actualizarGrupo() {
      if (tipoActualizacion == 0) {
         if (tipoListaTSGrupos == 0) {
            tsGrupoTablaSeleccionado.setGrupotipoentidad(grupoTipoEntidadSeleccionado);
            if (!listTSGruposTiposEntidadesCrear.contains(tsGrupoTablaSeleccionado)) {
               if (listTSGruposTiposEntidadesModificar.isEmpty()) {
                  listTSGruposTiposEntidadesModificar.add(tsGrupoTablaSeleccionado);
               } else if (!listTSGruposTiposEntidadesModificar.contains(tsGrupoTablaSeleccionado)) {
                  listTSGruposTiposEntidadesModificar.add(tsGrupoTablaSeleccionado);
               }
            }
         } else {
            tsGrupoTablaSeleccionado.setGrupotipoentidad(grupoTipoEntidadSeleccionado);
            if (!listTSGruposTiposEntidadesCrear.contains(tsGrupoTablaSeleccionado)) {
               if (listTSGruposTiposEntidadesModificar.isEmpty()) {
                  listTSGruposTiposEntidadesModificar.add(tsGrupoTablaSeleccionado);
               } else if (!listTSGruposTiposEntidadesModificar.contains(tsGrupoTablaSeleccionado)) {
                  listTSGruposTiposEntidadesModificar.add(tsGrupoTablaSeleccionado);
               }
            }
         }
         if (guardadoTSGrupos == true) {
            guardadoTSGrupos = false;
         }
         permitirIndexTSGrupos = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTSGrupo");
      } else if (tipoActualizacion == 1) {
         nuevoTSGrupoTipoEntidad.setGrupotipoentidad(grupoTipoEntidadSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTSGrupoGrupo");
      } else if (tipoActualizacion == 2) {
         duplicarTSGrupoTipoEntidad.setGrupotipoentidad(grupoTipoEntidadSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTSGrupoGrupo");
      }
      filtrarLovGruposTiposEntidades = null;
      grupoTipoEntidadSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:GrupoDialogo");
      RequestContext.getCurrentInstance().update("form:lovGrupo");
      RequestContext.getCurrentInstance().update("form:aceptarG");
      context.reset("form:lovGrupo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGrupo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').hide()");
   }

   public void cancelarCambioGrupo() {
      filtrarLovGruposTiposEntidades = null;
      grupoTipoEntidadSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexTSGrupos = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovGrupo:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGrupo').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('GrupoDialogo').hide()");
   }

   public void actualizarTipoEntidad() {
      if (tipoActualizacion == 0) {
         if (tipoListaTEFormulas == 0) {
            teFormulaTablaSeleccionado.setTipoentidad(tipoEntidadSeleccionado);
            if (!listTEFormulasConceptosCrear.contains(teFormulaTablaSeleccionado)) {
               if (listTEFormulasConceptosModificar.isEmpty()) {
                  listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
               } else if (!listTEFormulasConceptosModificar.contains(teFormulaTablaSeleccionado)) {
                  listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
               }
            }
         } else {
            teFormulaTablaSeleccionado.setTipoentidad(tipoEntidadSeleccionado);
            if (!listTEFormulasConceptosCrear.contains(teFormulaTablaSeleccionado)) {
               if (listTEFormulasConceptosModificar.isEmpty()) {
                  listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
               } else if (!listTEFormulasConceptosModificar.contains(teFormulaTablaSeleccionado)) {
                  listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
               }
            }
         }
         if (guardadoTEFormulas == true) {
            guardadoTEFormulas = false;
         }
         permitirIndexTEFormulas = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTEFormula");
      } else if (tipoActualizacion == 1) {
         nuevoTEFormulaConcepto.setTipoentidad(tipoEntidadSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaTipoEntidad");
      } else if (tipoActualizacion == 2) {
         duplicarTEFormulaConcepto.setTipoentidad(tipoEntidadSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaTipoEntidad");
      }
      lovTiposEntidades = null;
      tipoEntidadSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:TipoEntidadDialogo");
      RequestContext.getCurrentInstance().update("form:lovTipoEntidad");
      RequestContext.getCurrentInstance().update("form:aceptarTE");
      context.reset("form:lovTipoEntidad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoEntidad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').hide()");
   }

   public void cancelarCambioTipoEntidad() {
      lovTiposEntidades = null;
      tipoEntidadSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexTEFormulas = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipoEntidad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoEntidad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoEntidadDialogo').hide()");
   }

   public void actualizarFormulaTE() {
      if (tipoActualizacion == 0) {
         if (tipoListaTEFormulas == 0) {
            teFormulaTablaSeleccionado.setFormula(formulaSeleccionado);
            if (!listTEFormulasConceptosCrear.contains(teFormulaTablaSeleccionado)) {
               if (listTEFormulasConceptosModificar.isEmpty()) {
                  listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
               } else if (!listTEFormulasConceptosModificar.contains(teFormulaTablaSeleccionado)) {
                  listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
               }
            }
         } else {
            teFormulaTablaSeleccionado.setFormula(formulaSeleccionado);
            if (!listTEFormulasConceptosCrear.contains(teFormulaTablaSeleccionado)) {
               if (listTEFormulasConceptosModificar.isEmpty()) {
                  listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
               } else if (!listTEFormulasConceptosModificar.contains(teFormulaTablaSeleccionado)) {
                  listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
               }
            }
         }
         if (guardadoTEFormulas == true) {
            guardadoTEFormulas = false;
         }
         permitirIndexTEFormulas = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTEFormula");
      } else if (tipoActualizacion == 1) {
         nuevoTEFormulaConcepto.setFormula(formulaSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaFormula");
      } else if (tipoActualizacion == 2) {
         duplicarTEFormulaConcepto.setFormula(formulaSeleccionado);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaFormula");
      }
      filtrarLovFormulas = null;
      formulaSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:FormulaTEDialogo");
      RequestContext.getCurrentInstance().update("form:lovFormulaTE");
      RequestContext.getCurrentInstance().update("form:aceptarFTE");
      context.reset("form:lovFormulaTE:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulaTE').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').hide()");
   }

   public void cancelarCambioFormulaTE() {
      filtrarLovFormulas = null;
      formulaSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexTEFormulas = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovFormulaTE:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulaTE').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulaTEDialogo').hide()");
   }

   public void actualizarConceptoTE() {
      if (tipoActualizacion == 0) {
         teFormulaTablaSeleccionado.setConcepto(conceptoSeleccionado);
         teFormulaTablaSeleccionado.getConcepto().setEmpresa(conceptoSeleccionado.getEmpresa());
         teFormulaTablaSeleccionado.getConcepto().setNombreEmpresa(conceptoSeleccionado.getNombreEmpresa());
         if (!listTEFormulasConceptosCrear.contains(teFormulaTablaSeleccionado)) {
            if (listTEFormulasConceptosModificar.isEmpty()) {
               listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
            } else if (!listTEFormulasConceptosModificar.contains(teFormulaTablaSeleccionado)) {
               listTEFormulasConceptosModificar.add(teFormulaTablaSeleccionado);
            }
         }
         if (guardadoTEFormulas == true) {
            guardadoTEFormulas = false;
         }
         permitirIndexTEFormulas = true;
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTEFormula");
      } else if (tipoActualizacion == 1) {
         nuevoTEFormulaConcepto.setConcepto(conceptoSeleccionado);
         nuevoTEFormulaConcepto.getConcepto().setNombreEmpresa(conceptoSeleccionado.getNombreEmpresa());
         nuevoTEFormulaConcepto.getConcepto().setEmpresa(conceptoSeleccionado.getEmpresa());
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaCConcepto");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTEFormulaEmpresa");
      } else if (tipoActualizacion == 2) {
         duplicarTEFormulaConcepto.setConcepto(conceptoSeleccionado);
         duplicarTEFormulaConcepto.getConcepto().setNombreEmpresa(conceptoSeleccionado.getNombreEmpresa());
         duplicarTEFormulaConcepto.getConcepto().setEmpresa(conceptoSeleccionado.getEmpresa());
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaCConcepto");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTEFormulaEmpresa");
      }
      filtrarLovConceptos = null;
      conceptoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();

      RequestContext.getCurrentInstance().update("form:ConceptoTEDialogo");
      RequestContext.getCurrentInstance().update("form:lovConceptoTE");
      RequestContext.getCurrentInstance().update("form:aceptarCTE");
      context.reset("form:lovConceptoTE:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptoTE').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').hide()");
   }

   public void cancelarCambioConceptoTE() {
      filtrarLovConceptos = null;
      conceptoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexTEFormulas = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovConceptoTE:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptoTE').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ConceptoTEDialogo').hide()");
   }

   /**
    * Metodo que activa el boton aceptar de la pantalla y dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   public String exportXML() {
      if (cualtabla == 1) {
         nombreTabla = ":formExportarTS:datosTipoSueldoExportar";
         nombreXML = "TiposSueldos_XML";
      }
      if (cualtabla == 2) {
         nombreTabla = ":formExportarTSF:datosTSFormulaExportar";
         nombreXML = "FormulasConceptos_XML";
      }
      if (cualtabla == 3) {
         nombreTabla = ":formExportarTSG:datosTSGrupoExportar";
         nombreXML = "GruposDistribucion_XML";
      }
      if (cualtabla == 4) {
         nombreTabla = ":formExportarTEF:datosTEFormulaExportar";
         nombreXML = "TiposEntidadesDistribucion_XML";
      }
      return nombreTabla;
   }

   /**
    * Metodo que exporta datos a PDF
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      if (cualtabla == 1) {
         exportPDF_TS();
      }
      if (cualtabla == 2) {
         exportPDF_TSF();
      }
      if (cualtabla == 3) {
         exportPDF_TSG();
      }
      if (cualtabla == 4) {
         exportPDF_TEF();
      }
   }

   public void exportPDF_TS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTS:datosTipoSueldoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TiposSueldos_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_TSF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTSF:datosTSFormulaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "FormulasConceptos_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_TSG() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTSG:datosTSGrupoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "GruposDistribucion_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_TEF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTEF:datosTEFormulaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TiposEntidadesDistribucion_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportXLS() throws IOException {
      if (cualtabla == 1) {
         exportXLS_TS();
      }
      if (cualtabla == 2) {
         exportXLS_TSF();
      }
      if (cualtabla == 3) {
         exportXLS_TSG();
      }
      if (cualtabla == 4) {
         exportXLS_TEF();
      }
   }

   public void exportXLS_TS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTS:datosTipoSueldoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TiposSueldos_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_TSF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTSF:datosTSFormulaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "FormulasConceptos_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_TSG() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTSG:datosTSGrupoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "GruposDistribucion_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_TEF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTEF:datosTEFormulaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TiposEntidadesDistribucion_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }
   //EVENTO FILTRAR

   /**
    * Evento que cambia la lista reala a la filtrada
    */
   public void contarRegistrosTipoSueldos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTS");
   }

   public void contarRegistrosFormulas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTSFormulas");
   }

   public void contarRegistrosGrupos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTSGrupos");
   }

   public void contarRegistrosTipoEntidades() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTE");
   }

   public void contarRegistrosLovFormulas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroFormula");
   }

   public void contarRegistrosLovFormulasConceptos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroConcepto");
   }

   public void contarRegistrosLovGrupos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroGrupo");
   }

   public void contarRegistrosLovTipoEntidad() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTipoEntidad");
   }

   public void contarRegistrosLovFormulasTE() {
      RequestContext.getCurrentInstance().update("form:infoRegistroFormulaTE");
   }

   public void contarRegistrosLovTEConceptos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroConceptoTE");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabiltiarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void verificarRastro() {
      if (cualtabla == 1) {
         verificarRastroTipoSueldo();
      }
      if (cualtabla == 2) {
         verificarRastroTSFormula();
      }
      if (cualtabla == 3) {
         verificarRastroTSGrupo();
      }
      if (cualtabla == 4) {
         verificarRastroTEFormula();
      }
   }

   public void verificarRastroTipoSueldo() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaTiposSueldos != null) {
         if (tipoSueldoTablaSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tipoSueldoTablaSeleccionado.getSecuencia(), "TIPOSSUELDOS");
            backup = tipoSueldoTablaSeleccionado.getSecuencia();
            if (resultado == 1) {
               RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
               nombreTablaRastro = "TiposSueldos";
               msnConfirmarRastro = "La tabla TIPOSSUELDOS tiene rastros para el registro seleccionado, ¿desea continuar?";
               RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
               RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (resultado == 3) {
               RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (resultado == 4) {
               RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (resultado == 5) {
               RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSSUELDOS")) {
         nombreTablaRastro = "TiposSueldos";
         msnConfirmarRastroHistorico = "La tabla TIPOSSUELDOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroTSFormula() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tsFormulaTablaSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(tsFormulaTablaSeleccionado.getSecuencia(), "TSFORMULASCONCEPTOS");
         backup = tsFormulaTablaSeleccionado.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "TSFormulasConceptos";
            msnConfirmarRastro = "La tabla TSFORMULASCONCEPTOS tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("TSFORMULASCONCEPTOS")) {
         nombreTablaRastro = "TSFormulasConceptos";
         msnConfirmarRastroHistorico = "La tabla TSFORMULASCONCEPTOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroTSGrupo() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaTSGruposTiposEntidades != null) {
         int resultado = administrarRastros.obtenerTabla(tsGrupoTablaSeleccionado.getSecuencia(), "TSGRUPOSTIPOSENTIDADES");
         backup = tsGrupoTablaSeleccionado.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "TSGruposTiposEntidades";
            msnConfirmarRastro = "La tabla TSGRUPOSTIPOSENTIDADES tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("TSGRUPOSTIPOSENTIDADES")) {
         nombreTablaRastro = "TSGruposTiposEntidades";
         msnConfirmarRastroHistorico = "La tabla TSGRUPOSTIPOSENTIDADES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroTEFormula() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (teFormulaTablaSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(teFormulaTablaSeleccionado.getSecuencia(), "TEFORMULASCONCEPTOS");
         backup = teFormulaTablaSeleccionado.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "TEFormulasConceptos";
            msnConfirmarRastro = "La tabla TEFORMULASCONCEPTOS tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("TEFORMULASCONCEPTOS")) {
         nombreTablaRastro = "TEFormulasConceptos";
         msnConfirmarRastroHistorico = "La tabla TEFORMULASCONCEPTOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //GETTERS AND SETTERS
   public List<TiposSueldos> getListaTiposSueldos() {
      try {
         if (listaTiposSueldos == null) {
            listaTiposSueldos = administrarTipoSueldo.listaTiposSueldos();
            return listaTiposSueldos;
         } else {
            return listaTiposSueldos;
         }
      } catch (Exception e) {
         log.warn("Error...!! getListaTiposSueldos " + e.toString());
         return null;
      }
   }

   public void setListaTiposSueldos(List<TiposSueldos> setListaTiposSueldos) {
      this.listaTiposSueldos = setListaTiposSueldos;
   }

   public List<TiposSueldos> getFiltrarListaTiposSueldos() {
      return filtrarListaTiposSueldos;
   }

   public void setFiltrarListaTiposSueldos(List<TiposSueldos> setFiltrarListaTiposSueldos) {
      this.filtrarListaTiposSueldos = setFiltrarListaTiposSueldos;
   }

   public TiposSueldos getNuevoTipoSueldo() {
      return nuevoTipoSueldo;
   }

   public void setNuevoTipoSueldo(TiposSueldos setNuevoTipoSueldo) {
      this.nuevoTipoSueldo = setNuevoTipoSueldo;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public TiposSueldos getEditarTipoSueldo() {
      return editarTipoSueldo;
   }

   public void setEditarTipoSueldo(TiposSueldos setEditarTipoSueldo) {
      this.editarTipoSueldo = setEditarTipoSueldo;
   }

   public TiposSueldos getDuplicarTipoSueldo() {
      return duplicarTipoSueldo;
   }

   public void setDuplicarTipoSueldo(TiposSueldos setDuplicarTipoSueldo) {
      this.duplicarTipoSueldo = setDuplicarTipoSueldo;
   }

   public List<TSFormulasConceptos> getListaTSFormulasConceptos() {
      if (listaTSFormulasConceptos == null) {
         if (tipoSueldoTablaSeleccionado != null) {
            listaTSFormulasConceptos = administrarTipoSueldo.listaTSFormulasConceptosParaTipoSueldoSecuencia(tipoSueldoTablaSeleccionado.getSecuencia());
            if (listaTSFormulasConceptos != null) {
               if (!listaTSFormulasConceptos.isEmpty()) {
                  for (TSFormulasConceptos recTSFC : listaTSFormulasConceptos) {
                     if (recTSFC.getConcepto() == null) {
                        recTSFC.setConcepto(new Conceptos());
                     }
                     if (recTSFC.getEmpresa() == null) {
                        recTSFC.setEmpresa(new Empresas());
                     }
                     if (recTSFC.getFormula() == null) {
                        recTSFC.setFormula(new Formulas());
                     }
                     if (recTSFC.getTiposueldo() == null) {
                        recTSFC.setTiposueldo(new TiposSueldos());
                     }
                  }
               }
            }
         }
      }
      return listaTSFormulasConceptos;
   }

   public void setListaTSFormulasConceptos(List<TSFormulasConceptos> setListaTSFormulasConceptos) {
      this.listaTSFormulasConceptos = setListaTSFormulasConceptos;
   }

   public List<TSFormulasConceptos> getFiltrarListaTSFormulasConceptos() {
      return filtrarListaTSFormulasConceptos;
   }

   public void setFiltrarListaTSFormulasConceptos(List<TSFormulasConceptos> setFiltrarListaTSFormulasConceptos) {
      this.filtrarListaTSFormulasConceptos = setFiltrarListaTSFormulasConceptos;
   }

   public List<TiposSueldos> getListTiposSueldosModificar() {
      return listTiposSueldosModificar;
   }

   public void setListTiposSueldosModificar(List<TiposSueldos> setListTiposSueldosModificar) {
      this.listTiposSueldosModificar = setListTiposSueldosModificar;
   }

   public List<TiposSueldos> getListTiposSueldosCrear() {
      return listTiposSueldosCrear;
   }

   public void setListTiposSueldosCrear(List<TiposSueldos> setListTiposSueldosCrear) {
      this.listTiposSueldosCrear = setListTiposSueldosCrear;
   }

   public List<TiposSueldos> getListTiposSueldosBorrar() {
      return listTiposSueldosBorrar;
   }

   public void setListTiposSueldosBorrar(List<TiposSueldos> setListTiposSueldosBorrar) {
      this.listTiposSueldosBorrar = setListTiposSueldosBorrar;
   }

   public List<TSFormulasConceptos> getListTSFormulasConceptosModificar() {
      return listTSFormulasConceptosModificar;
   }

   public void setListTSFormulasConceptosModificar(List<TSFormulasConceptos> setListTSFormulasConceptosModificar) {
      this.listTSFormulasConceptosModificar = setListTSFormulasConceptosModificar;
   }

   public TSFormulasConceptos getNuevoTSFormulaConcepto() {
      return nuevoTSFormulaConcepto;
   }

   public void setNuevoTSFormulaConcepto(TSFormulasConceptos setNuevoTSFormulaConcepto) {
      this.nuevoTSFormulaConcepto = setNuevoTSFormulaConcepto;
   }

   public List<TSFormulasConceptos> getListTSFormulasConceptosCrear() {
      return listTSFormulasConceptosCrear;
   }

   public void setListTSFormulasConceptosCrear(List<TSFormulasConceptos> setListTSFormulasConceptosCrear) {
      this.listTSFormulasConceptosCrear = setListTSFormulasConceptosCrear;
   }

   public List<TSFormulasConceptos> getLisTSFormulasConceptosBorrar() {
      return listTSFormulasConceptosBorrar;
   }

   public void setListTSFormulasConceptosBorrar(List<TSFormulasConceptos> setListTSFormulasConceptosBorrar) {
      this.listTSFormulasConceptosBorrar = setListTSFormulasConceptosBorrar;
   }

   public TSFormulasConceptos getEditarTSFormulaConcepto() {
      return editarTSFormulaConcepto;
   }

   public void setEditarTSFormulaConcepto(TSFormulasConceptos setEditarTSFormulaConcepto) {
      this.editarTSFormulaConcepto = setEditarTSFormulaConcepto;
   }

   public TSFormulasConceptos getDuplicarTSFormulaConcepto() {
      return duplicarTSFormulaConcepto;
   }

   public void setDuplicarTSFormulaConcepto(TSFormulasConceptos setDuplicarTSFormulaConcepto) {
      this.duplicarTSFormulaConcepto = setDuplicarTSFormulaConcepto;
   }

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

   public String getNombreTablaRastro() {
      return nombreTablaRastro;
   }

   public void setNombreTablaRastro(String nombreTablaRastro) {
      this.nombreTablaRastro = nombreTablaRastro;
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

   public void setNombreTabla(String setNombreTabla) {
      this.nombreTabla = setNombreTabla;
   }

   public List<Formulas> getLovFormulas() {
      if (lovFormulas == null) {
         lovFormulas = administrarTipoSueldo.lovFormulas();
      }
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> setLovFormulas) {
      this.lovFormulas = setLovFormulas;
   }

   public List<Formulas> getFiltrarLovFormulas() {
      return filtrarLovFormulas;
   }

   public void setFiltrarLovFormulas(List<Formulas> setFiltrarLovFormulas) {
      this.filtrarLovFormulas = setFiltrarLovFormulas;
   }

   public Formulas getFormulaSeleccionado() {
      return formulaSeleccionado;
   }

   public void setFormulaSeleccionado(Formulas setFormulaSeleccionado) {
      this.formulaSeleccionado = setFormulaSeleccionado;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public String getAltoTablaTiposSueldos() {
      return altoTablaTiposSueldos;
   }

   public void setAltoTablaTiposSueldos(String setAltoTablaTiposSueldos) {
      this.altoTablaTiposSueldos = setAltoTablaTiposSueldos;
   }

   public String getAltoTablaTSFormulas() {
      return altoTablaTSFormulas;
   }

   public void setAltoTablaTSFormulas(String setAltoTablaTSFormulas) {
      this.altoTablaTSFormulas = setAltoTablaTSFormulas;
   }

   public List<Conceptos> getLovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = administrarTipoSueldo.lovConceptos();
      }

      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptos) {
      this.lovConceptos = lovConceptos;
   }

   public List<Conceptos> getFiltrarLovConceptos() {
      return filtrarLovConceptos;
   }

   public void setFiltrarLovConceptos(List<Conceptos> filtrarLovConceptos) {
      this.filtrarLovConceptos = filtrarLovConceptos;
   }

   public Conceptos getConceptoSeleccionado() {
      return conceptoSeleccionado;
   }

   public void setConceptoSeleccionado(Conceptos conceptoSeleccionado) {
      this.conceptoSeleccionado = conceptoSeleccionado;
   }

   public List<TSGruposTiposEntidades> getListaTSGruposTiposEntidades() {
      if (listaTSGruposTiposEntidades == null) {
         if (tipoSueldoTablaSeleccionado != null) {
            listaTSGruposTiposEntidades = administrarTipoSueldo.listaTSGruposTiposEntidadesParaTipoSueldoSecuencia(tipoSueldoTablaSeleccionado.getSecuencia());
         }

      }
      return listaTSGruposTiposEntidades;
   }

   public void setListaTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTSGruposTiposEntidades) {
      this.listaTSGruposTiposEntidades = listaTSGruposTiposEntidades;
   }

   public List<TSGruposTiposEntidades> getFiltrarListaTSGruposTiposEntidades() {
      return filtrarListaTSGruposTiposEntidades;
   }

   public void setFiltrarListaTSGruposTiposEntidades(List<TSGruposTiposEntidades> filtrarListaTSGruposTiposEntidades) {
      this.filtrarListaTSGruposTiposEntidades = filtrarListaTSGruposTiposEntidades;
   }

   public List<TSGruposTiposEntidades> getListTSGruposTiposEntidadesModificar() {
      return listTSGruposTiposEntidadesModificar;
   }

   public void setListTSGruposTiposEntidadesModificar(List<TSGruposTiposEntidades> listTSGruposTiposEntidadesModificar) {
      this.listTSGruposTiposEntidadesModificar = listTSGruposTiposEntidadesModificar;
   }

   public TSGruposTiposEntidades getNuevoTSGrupoTipoEntidad() {
      return nuevoTSGrupoTipoEntidad;
   }

   public void setNuevoTSGrupoTipoEntidad(TSGruposTiposEntidades nuevoTSGrupoTipoEntidad) {
      this.nuevoTSGrupoTipoEntidad = nuevoTSGrupoTipoEntidad;
   }

   public List<TSGruposTiposEntidades> getListTSGruposTiposEntidadesCrear() {
      return listTSGruposTiposEntidadesCrear;
   }

   public void setListTSGruposTiposEntidadesCrear(List<TSGruposTiposEntidades> listTSGruposTiposEntidadessCrear) {
      this.listTSGruposTiposEntidadesCrear = listTSGruposTiposEntidadessCrear;
   }

   public List<TSGruposTiposEntidades> getListTSGruposTiposEntidadesBorrar() {
      return listTSGruposTiposEntidadesBorrar;
   }

   public void setListTSGruposTiposEntidadesBorrar(List<TSGruposTiposEntidades> listTSGruposTiposEntidadesBorrar) {
      this.listTSGruposTiposEntidadesBorrar = listTSGruposTiposEntidadesBorrar;
   }

   public TSGruposTiposEntidades getEditarTSGrupoTipoEntidad() {
      return editarTSGrupoTipoEntidad;
   }

   public void setEditarTSGrupoTipoEntidad(TSGruposTiposEntidades editarTSGrupoTipoEntidad) {
      this.editarTSGrupoTipoEntidad = editarTSGrupoTipoEntidad;
   }

   public TSFormulasConceptos getDuplicarTSFormulaCConcepto() {
      return duplicarTSFormulaConcepto;
   }

   public void setDuplicarTSFormulaCConcepto(TSFormulasConceptos duplicarTSFormulaConcepto) {
      this.duplicarTSFormulaConcepto = duplicarTSFormulaConcepto;
   }

   public TSGruposTiposEntidades getDuplicarTSGrupoTipoEntidad() {
      return duplicarTSGrupoTipoEntidad;
   }

   public void setDuplicarTSGrupoTipoEntidad(TSGruposTiposEntidades duplicarTSGrupoTipoEntidad) {
      this.duplicarTSGrupoTipoEntidad = duplicarTSGrupoTipoEntidad;
   }

   public List<Grupostiposentidades> getLovGruposTiposEntidades() {
      lovGruposTiposEntidades = administrarTipoSueldo.lovGruposTiposEntidades();

      return lovGruposTiposEntidades;
   }

   public void setLovGruposTiposEntidades(List<Grupostiposentidades> lovGruposTiposEntidades) {
      this.lovGruposTiposEntidades = lovGruposTiposEntidades;
   }

   public List<Grupostiposentidades> getFiltrarLovGruposTiposEntidades() {
      return filtrarLovGruposTiposEntidades;
   }

   public void setFiltrarLovGruposTiposEntidades(List<Grupostiposentidades> filtrarLovGruposTiposEntidades) {
      this.filtrarLovGruposTiposEntidades = filtrarLovGruposTiposEntidades;
   }

   public Grupostiposentidades getGrupoTipoEntidadSeleccionado() {
      return grupoTipoEntidadSeleccionado;
   }

   public void setGrupoTipoEntidadSeleccionado(Grupostiposentidades grupoTipoEntidadSeleccionado) {
      this.grupoTipoEntidadSeleccionado = grupoTipoEntidadSeleccionado;
   }

   public String getAltoTablaTSGrupos() {
      return altoTablaTSGrupos;
   }

   public void setAltoTablaTSGrupos(String altoTablaTSGrupos) {
      this.altoTablaTSGrupos = altoTablaTSGrupos;
   }

   public List<TEFormulasConceptos> getListaTEFormulasConceptos() {
      if (listaTEFormulasConceptos == null) {
         if (tsGrupoTablaSeleccionado != null) {
            listaTEFormulasConceptos = administrarTipoSueldo.listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia(tsGrupoTablaSeleccionado.getSecuencia());
         }
      }
      return listaTEFormulasConceptos;
   }

   public void setListaTEFormulasConceptos(List<TEFormulasConceptos> listaTEFormulasConceptos) {
      this.listaTEFormulasConceptos = listaTEFormulasConceptos;
   }

   public List<TEFormulasConceptos> getFiltrarListaTEFormulasConceptos() {
      return filtrarListaTEFormulasConceptos;
   }

   public void setFiltrarListaTEFormulasConceptos(List<TEFormulasConceptos> filtrarListaTEFormulasConceptos) {
      this.filtrarListaTEFormulasConceptos = filtrarListaTEFormulasConceptos;
   }

   public List<TEFormulasConceptos> getListTEFormulasConceptosModificar() {
      return listTEFormulasConceptosModificar;
   }

   public void setListTEFormulasConceptosModificar(List<TEFormulasConceptos> listTEFormulasConceptosModificar) {
      this.listTEFormulasConceptosModificar = listTEFormulasConceptosModificar;
   }

   public TEFormulasConceptos getNuevoTEFormulaConcepto() {
      return nuevoTEFormulaConcepto;
   }

   public void setNuevoTEFormulaConcepto(TEFormulasConceptos nuevoTEFormulaConcepto) {
      this.nuevoTEFormulaConcepto = nuevoTEFormulaConcepto;
   }

   public List<TEFormulasConceptos> getListTEFormulasConceptosCrear() {
      return listTEFormulasConceptosCrear;
   }

   public void setListTEFormulasConceptosCrear(List<TEFormulasConceptos> listTEFormulasConceptosCrear) {
      this.listTEFormulasConceptosCrear = listTEFormulasConceptosCrear;
   }

   public List<TEFormulasConceptos> getListTEFormulasConceptosBorrar() {
      return listTEFormulasConceptosBorrar;
   }

   public void setListTEFormulasConceptosBorrar(List<TEFormulasConceptos> listTEFormulasConceptosBorrar) {
      this.listTEFormulasConceptosBorrar = listTEFormulasConceptosBorrar;
   }

   public TEFormulasConceptos getEditarTEFormulaConcepto() {
      return editarTEFormulaConcepto;
   }

   public void setEditarTEFormulaConcepto(TEFormulasConceptos editarTEFormulaConcepto) {
      this.editarTEFormulaConcepto = editarTEFormulaConcepto;
   }

   public TEFormulasConceptos getDuplicarTEFormulaConcepto() {
      return duplicarTEFormulaConcepto;
   }

   public void setDuplicarTEFormulaConcepto(TEFormulasConceptos duplicarTEFormulaConcepto) {
      this.duplicarTEFormulaConcepto = duplicarTEFormulaConcepto;
   }

   public List<TiposEntidades> getLovTiposEntidades() {
      if (lovTiposEntidades == null) {
         if (tsGrupoTablaSeleccionado != null) {
            lovTiposEntidades = administrarTipoSueldo.lovTiposEntidades(tsGrupoTablaSeleccionado.getGrupotipoentidad().getSecuencia());
         }
      }
      return lovTiposEntidades;
   }

   public void setLovTiposEntidades(List<TiposEntidades> lovTiposEntidades) {
      this.lovTiposEntidades = lovTiposEntidades;
   }

   public List<TiposEntidades> getFiltrarLovTiposEntidades() {
      return filtrarLovTiposEntidades;
   }

   public void setFiltrarLovTiposEntidades(List<TiposEntidades> filtrarLovTiposEntidades) {
      this.filtrarLovTiposEntidades = filtrarLovTiposEntidades;
   }

   public TiposEntidades getTipoEntidadSeleccionado() {
      return tipoEntidadSeleccionado;
   }

   public void setTipoEntidadSeleccionado(TiposEntidades tipoEntidadSeleccionado) {
      this.tipoEntidadSeleccionado = tipoEntidadSeleccionado;
   }

   public String getAltoTablaTEFormulas() {
      return altoTablaTEFormulas;
   }

   public void setAltoTablaTEFormulas(String altoTablaTEFormulas) {
      this.altoTablaTEFormulas = altoTablaTEFormulas;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public boolean isActivoFormulaConcepto() {
      return activoFormulaConcepto;
   }

   public void setActivoFormulaConcepto(boolean activoFormulaConcepto) {
      this.activoFormulaConcepto = activoFormulaConcepto;
   }

   public boolean isActivoGrupoDistribucion() {
      return activoGrupoDistribucion;
   }

   public void setActivoGrupoDistribucion(boolean activoGrupoDistribucion) {
      this.activoGrupoDistribucion = activoGrupoDistribucion;
   }

   public boolean isActivoTipoEntidad() {
      return activoTipoEntidad;
   }

   public void setActivoTipoEntidad(boolean activoTipoEntidad) {
      this.activoTipoEntidad = activoTipoEntidad;
   }

   public TiposSueldos getTipoSueldoTablaSeleccionado() {
      return tipoSueldoTablaSeleccionado;
   }

   public void setTipoSueldoTablaSeleccionado(TiposSueldos tipoSueldoTablaSeleccionado) {
      this.tipoSueldoTablaSeleccionado = tipoSueldoTablaSeleccionado;
   }

   public TSFormulasConceptos getTsFormulaTablaSeleccionado() {
      return tsFormulaTablaSeleccionado;
   }

   public void setTsFormulaTablaSeleccionado(TSFormulasConceptos tsFormulaTablaSeleccionado) {
      this.tsFormulaTablaSeleccionado = tsFormulaTablaSeleccionado;
   }

   public TSGruposTiposEntidades getTsGrupoTablaSeleccionado() {
      return tsGrupoTablaSeleccionado;
   }

   public void setTsGrupoTablaSeleccionado(TSGruposTiposEntidades tsGrupoTablaSeleccionado) {
      this.tsGrupoTablaSeleccionado = tsGrupoTablaSeleccionado;
   }

   public TEFormulasConceptos getTeFormulaTablaSeleccionado() {
      return teFormulaTablaSeleccionado;
   }

   public void setTeFormulaTablaSeleccionado(TEFormulasConceptos teFormulaTablaSeleccionado) {
      this.teFormulaTablaSeleccionado = teFormulaTablaSeleccionado;
   }

   public String getInfoRegistroFormula() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovFormula");
      infoRegistroFormula = String.valueOf(tabla.getRowCount());
      return infoRegistroFormula;
   }

   public void setInfoRegistroFormula(String infoRegistroFormula) {
      this.infoRegistroFormula = infoRegistroFormula;
   }

   public String getInfoRegistroConcepto() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovConcepto");
      infoRegistroConcepto = String.valueOf(tabla.getRowCount());
      return infoRegistroConcepto;
   }

   public void setInfoRegistroConcepto(String infoRegistroConcepto) {
      this.infoRegistroConcepto = infoRegistroConcepto;
   }

   public String getInfoRegistroGrupo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovGrupo");
      infoRegistroGrupo = String.valueOf(tabla.getRowCount());
      return infoRegistroGrupo;
   }

   public void setInfoRegistroGrupo(String infoRegistroGrupo) {
      this.infoRegistroGrupo = infoRegistroGrupo;
   }

   public String getInfoRegistroTipoEntidad() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoEntidad");
      infoRegistroTipoEntidad = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoEntidad;
   }

   public void setInfoRegistroTipoEntidad(String infoRegistroTipoEntidad) {
      this.infoRegistroTipoEntidad = infoRegistroTipoEntidad;
   }

   public String getInfoRegistroFormulaTE() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovFormulaTE");
      infoRegistroFormulaTE = String.valueOf(tabla.getRowCount());
      return infoRegistroFormulaTE;
   }

   public void setInfoRegistroFormulaTE(String infoRegistroFormulaTE) {
      this.infoRegistroFormulaTE = infoRegistroFormulaTE;
   }

   public String getInfoRegistroConceptoTE() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovConceptoTE");
      infoRegistroConceptoTE = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptoTE;
   }

   public void setInfoRegistroConceptoTE(String infoRegistroConceptoTe) {
      this.infoRegistroConceptoTE = infoRegistroConceptoTe;
   }

   public BigInteger getBackup() {
      return backup;
   }

   public void setBackup(BigInteger backup) {
      this.backup = backup;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public String getInfoRegistroTS() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTipoSueldo");
      infoRegistroTS = String.valueOf(tabla.getRowCount());
      return infoRegistroTS;
   }

   public void setInfoRegistroTS(String infoRegistroTS) {
      this.infoRegistroTS = infoRegistroTS;
   }

   public String getInfoRegistroTSFormulas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTSFormula");
      infoRegistroTSFormulas = String.valueOf(tabla.getRowCount());
      return infoRegistroTSFormulas;
   }

   public void setInfoRegistroTSFormulas(String infoRegistroTSFormulas) {
      this.infoRegistroTSFormulas = infoRegistroTSFormulas;
   }

   public String getInfoRegistroTSGrupos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTSGrupo");
      infoRegistroTSGrupos = String.valueOf(tabla.getRowCount());
      return infoRegistroTSGrupos;
   }

   public void setInfoRegistroTSGrupos(String infoRegistroTSGrupos) {
      this.infoRegistroTSGrupos = infoRegistroTSGrupos;
   }

   public String getInfoRegistroTE() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTEFormula");
      infoRegistroTE = String.valueOf(tabla.getRowCount());
      return infoRegistroTE;
   }

   public void setInfoRegistroTE(String infoRegistroTE) {
      this.infoRegistroTE = infoRegistroTE;
   }

}
