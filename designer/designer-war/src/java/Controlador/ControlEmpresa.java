package Controlador;

import Entidades.CentrosCostos;
import Entidades.Circulares;
import Entidades.Empresas;
import Entidades.Monedas;
import Entidades.VigenciasMonedasBases;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmpresasInterface;
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
public class ControlEmpresa implements Serializable {

   private static Logger log = Logger.getLogger(ControlEmpresa.class);

   @EJB
   AdministrarEmpresasInterface administrarEmpresa;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //
   private List<Empresas> listaEmpresas;
   private List<Empresas> filtrarListaEmpresas;
   private Empresas empresaSeleccionada;
   //
   private List<VigenciasMonedasBases> listaVigenciasMB;
   private List<VigenciasMonedasBases> filtrarListaVigenciasMonedasBases;
   private VigenciasMonedasBases vigenciaMBSeleccionada;
   //
   private List<Circulares> listaCirculares;
   private List<Circulares> filtrarListaCirculares;
   private Circulares circularSeleccionada;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla 
   private Column empresaCodigo, empresaNIT, empresaNombre, empresaReglamento, empresaManual, empresaLogo, empresaCentroCosto, empresaCodigoAlternativo;
   private Column vigenciaFecha, vigenciaCodigo, vigenciaMoneda;
   private Column circularFecha, circularExpedido, circularContenido;
   //Otros
   private boolean aceptar;
   //modificar
   private List<Empresas> listEmpresasModificar;
   private List<VigenciasMonedasBases> listVigenciasMonedasBasesModificar;
   private List<Circulares> listCircularesModificar;
   private boolean guardado, guardadoVigencia, guardadoCircular;
   //crear 
   private Empresas nuevoEmpresa;
   private VigenciasMonedasBases nuevoVigenciaMonedaBase;
   private Circulares nuevoCircular;
   private List<Empresas> listEmpresasCrear;
   private List<VigenciasMonedasBases> listVigenciasMBCrear;
   private List<Circulares> listCircularesCrear;
   private BigInteger l;
   private int k;
   //borrar 
   private List<Empresas> listEmpresasBorrar;
   private List<VigenciasMonedasBases> listVigenciasMonedasBasesBorrar;
   private List<Circulares> listCircularesBorrar;
   //editar celda
   private Empresas editarEmpresa;
   private VigenciasMonedasBases editarVigenciaMonedaBase;
   private Circulares editarCircular;
   private int cualCelda, tipoLista, cualCeldaVigencia, tipoListaVigencia, cualCeldaCircular, tipoListaCircular;
   //duplicar
   private Empresas duplicarEmpresa;
   private VigenciasMonedasBases duplicarVigenciaMB;
   private Circulares duplicarCircular;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private String nombreXML, nombreTabla;
   private String moneda, centroCosto, codigoMoneda;

   ///////////LOV///////////
   private List<CentrosCostos> lovCentrosCostos;
   private List<CentrosCostos> filtrarLovCentrosCostos;
   private CentrosCostos centroCostoLovSeleccionado;

   private List<Monedas> lovMonedas;
   private List<Monedas> filtrarLovMonedas;
   private Monedas monedaLovSeleccionada;

   private List<Empresas> lovEmpresas;
   private List<Empresas> filtrarLovEmpresas;
   private Empresas empresaLovSeleccionada;
   private Empresas empresaOrigenClonado, empresaDestinoClonado;

   private int tipoActualizacion;
   private short auxCodigoEmpresa;
   private String auxNombreEmpresa;
   private long auxNitEmpresa;
   private Date auxFechaVigencia;
   private Date auxFechaCircular;
   private Date fechaParametro;
   //
   private boolean cambiosPagina;
   private String altoTablaEmpresa, altoTablasSecundarias;
   //
   //
   private boolean activoCasillaClonado;
   //
   private String variableClonado;
   //
   private String auxClonadoNombreOrigen, auxClonadoNombreDestino;
   private short auxClonadoCodigoOrigen, auxClonadoCodigoDestino;
   //
   private int indexClonadoOrigen, indexClonadoDestino;
   private int tablaActiva;
   //
   private boolean activoBtnesAdd;
   //
   private String infoRegistroLovEmpresa, infoRegistroLovMoneda, infoRegistroLovCentro, infoRegistroEmpresas, infoRegistroCJuridicos, infoRegistroCirculares;
   private String paginaAnterior = "nominaf";
   private String errorClonado = "";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmpresa() {
      activoBtnesAdd = true;
      activoCasillaClonado = true;
      paginaAnterior = "nominaf";
      //altos tablas
      altoTablaEmpresa = "100";
      altoTablasSecundarias = "90";
      //lovs
      lovCentrosCostos = null;
      centroCostoLovSeleccionado = new CentrosCostos();
      lovMonedas = null;
      monedaLovSeleccionada = new Monedas();
      lovEmpresas = null;
      empresaLovSeleccionada = new Empresas();
      empresaOrigenClonado = new Empresas();
      empresaDestinoClonado = new Empresas();
      //index tablas
      empresaSeleccionada = null;
      vigenciaMBSeleccionada = null;
      circularSeleccionada = null;
      indexClonadoOrigen = -1;
      indexClonadoDestino = -1;
      //listas de tablas
      listaEmpresas = null;
      listaVigenciasMB = null;
      listaCirculares = null;
      //Otros
      aceptar = true;
      cambiosPagina = true;
      tipoActualizacion = -1;
      variableClonado = "origen";
      k = 0;
      //borrar 
      listEmpresasBorrar = new ArrayList<Empresas>();
      listVigenciasMonedasBasesBorrar = new ArrayList<VigenciasMonedasBases>();
      listCircularesBorrar = new ArrayList<Circulares>();
      //crear 
      listEmpresasCrear = new ArrayList<Empresas>();
      listVigenciasMBCrear = new ArrayList<VigenciasMonedasBases>();
      listCircularesCrear = new ArrayList<Circulares>();
      //modificar 
      listEmpresasModificar = new ArrayList<Empresas>();
      listVigenciasMonedasBasesModificar = new ArrayList<VigenciasMonedasBases>();
      listCircularesModificar = new ArrayList<Circulares>();
      //editar
      editarEmpresa = new Empresas();
      editarVigenciaMonedaBase = new VigenciasMonedasBases();
      editarCircular = new Circulares();
      //Cual Celda
      cualCelda = -1;
      cualCeldaVigencia = -1;
      cualCeldaCircular = -1;
      //Tipo Lista
      tipoListaVigencia = 0;
      tipoLista = 0;
      tipoListaCircular = 0;
      //guardar
      guardado = true;
      guardadoVigencia = true;
      guardadoCircular = true;
      //Crear 
      nuevoEmpresa = new Empresas();
      nuevoEmpresa.setCentrocosto(new CentrosCostos());
      nuevoVigenciaMonedaBase = new VigenciasMonedasBases();
      nuevoVigenciaMonedaBase.setMoneda(new Monedas());
      nuevoCircular = new Circulares();

      //Duplicar
      duplicarEmpresa = new Empresas();
      duplicarVigenciaMB = new VigenciasMonedasBases();
      duplicarCircular = new Circulares();
      //Banderas
      bandera = 0;
      mapParametros.put("paginaAnterior", paginaAnterior);
      tablaActiva = 0;
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
      limpiarListasValor();
      String pagActual = "empresa";
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
      lovCentrosCostos = null;
      lovEmpresas = null;
      lovMonedas = null;
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
         administrarEmpresa.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public String valorPaginaAnterior() {
      return paginaAnterior;
   }

   public void inicializarPagina(String paginaLlamado) {
      paginaAnterior = paginaLlamado;
      listaVigenciasMB = null;
      listaCirculares = null;
      listaEmpresas = null;
      getListaEmpresas();
   }

   public boolean validarFechaVigenciaMonedaBase(int i) {
      boolean retorno = true;
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      if (i == 0) {
         VigenciasMonedasBases auxiliar = new VigenciasMonedasBases();
         auxiliar = vigenciaMBSeleccionada;
         if (auxiliar.getFecha() != null) {
            if (auxiliar.getFecha().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = true;
         }
      }
      if (i == 1) {
         if (nuevoVigenciaMonedaBase.getFecha() != null) {
            if (nuevoVigenciaMonedaBase.getFecha().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarVigenciaMB.getFecha() != null) {
            if (duplicarVigenciaMB.getFecha().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = true;
         }
      }
      return retorno;
   }

   public void modificacionesFechaVigenciaMonedaBase(VigenciasMonedasBases vigMoneda, int c) {
      vigenciaMBSeleccionada = vigMoneda;
      if (validarFechaVigenciaMonedaBase(0) == true) {
         cambiarIndiceVigencia(vigenciaMBSeleccionada, c);
         modificarVigenciaMonedaBase(vigenciaMBSeleccionada);
      } else {
         vigenciaMBSeleccionada.setFecha(auxFechaVigencia);
         RequestContext.getCurrentInstance().update("form:datosDetalleEmpresa");
         RequestContext.getCurrentInstance().execute("PF('errorFechasVigencia').show()");
      }
   }

   public boolean validarFechaCircular(int i) {
      boolean retorno = true;
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      if (i == 0) {
         Circulares auxiliar = new Circulares();
         auxiliar = circularSeleccionada;
         if (auxiliar.getFecha() != null) {
            if (auxiliar.getFecha().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = true;
         }
      }
      if (i == 1) {
         if (nuevoCircular.getFecha() != null) {
            if (nuevoCircular.getFecha().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarCircular.getFecha() != null) {
            if (duplicarCircular.getFecha().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = true;
         }
      }
      return retorno;
   }

   public void modificacionesFechaCircular(Circulares circular, int c) {
      circularSeleccionada = circular;
      if (validarFechaCircular(0) == true) {
         cambiarIndiceCircular(circularSeleccionada, c);
         modificarCirculares(circularSeleccionada);
      } else {
         circularSeleccionada.setFecha(auxFechaCircular);
         RequestContext.getCurrentInstance().update("form:datosCircular");
         RequestContext.getCurrentInstance().execute("PF('errorFechasCircular').show()");
      }
   }

   public boolean validarCamposNulosEmpresa(int i) {
      boolean retorno = true;
      if (i == 0) {
         if (empresaSeleccionada.getNit() <= 0) {
            retorno = false;
         }
         if (empresaSeleccionada.getNombre() == null) {
            retorno = false;
         } else if (empresaSeleccionada.getNombre().isEmpty()) {
            retorno = false;
         }
         if (empresaSeleccionada.getCodigo() <= 0) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoEmpresa.getNit() <= 0) {
            retorno = false;
         }
         if (nuevoEmpresa.getNombre() == null) {
            retorno = false;
         } else if (nuevoEmpresa.getNombre().isEmpty()) {
            retorno = false;
         }
         if (nuevoEmpresa.getCodigo() <= 0) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarEmpresa.getNit() <= 0) {
            retorno = false;
         }
         if (duplicarEmpresa.getNombre() == null) {
            retorno = false;
         } else if (duplicarEmpresa.getNombre().isEmpty()) {
            retorno = false;
         }
         if (duplicarEmpresa.getCodigo() <= 0) {
            retorno = false;
         }
      }
      log.info("ControlEmpresa.validarCamposNulosEmpresa() retorno: " + retorno);
      return retorno;
   }

   public boolean validarCamposNulosVigenciaMonedaBase(int i) {
      boolean retorno = true;
      if (i == 0) {
         if (vigenciaMBSeleccionada.getMoneda().getSecuencia() == null || vigenciaMBSeleccionada.getFecha() == null) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoVigenciaMonedaBase.getMoneda().getSecuencia() == null || nuevoVigenciaMonedaBase.getFecha() == null) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarVigenciaMB.getMoneda().getSecuencia() == null || duplicarVigenciaMB.getFecha() == null) {
            retorno = false;
         }
      }
      return retorno;
   }

   public void procesoModificacionEmpresa(Empresas empresa) {
      empresaSeleccionada = empresa;
      if (validarCamposNulosEmpresa(0)) {
         modificarEmpresa(empresaSeleccionada);
      } else {
         empresaSeleccionada.setCodigo(auxCodigoEmpresa);
         empresaSeleccionada.setNombre(auxNombreEmpresa);
         empresaSeleccionada.setNit(auxNitEmpresa);
         RequestContext.getCurrentInstance().update("form:datosEmpresa");
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullEmpresa').show()");
      }
   }

   public void modificarEmpresa(Empresas empresa) {
      log.info("ControlEmpresa.modificarEmpresa()");
      tablaActiva = 0;
      empresaSeleccionada = empresa;
      if (empresaSeleccionada.getNombre().length() >= 1 && empresaSeleccionada.getNombre().length() <= 50) {
         if (empresaSeleccionada.getNombre() != null) {
            String textM = empresaSeleccionada.getNombre().toUpperCase();
            empresaSeleccionada.setNombre(textM);
         }
         if (empresaSeleccionada.getReglamento() != null) {
            String textM1 = empresaSeleccionada.getReglamento().toUpperCase();
            empresaSeleccionada.setReglamento(textM1);
         }
         if (empresaSeleccionada.getManualadministrativo() != null) {
            String textM2 = empresaSeleccionada.getManualadministrativo().toUpperCase();
            empresaSeleccionada.setManualadministrativo(textM2);
         }
         if (!listEmpresasCrear.contains(empresaSeleccionada)) {
            if (listEmpresasModificar.isEmpty()) {
               listEmpresasModificar.add(empresaSeleccionada);
            } else if (!listEmpresasModificar.contains(empresaSeleccionada)) {
               listEmpresasModificar.add(empresaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
            }
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosEmpresa");
      } else {
         empresaSeleccionada.setNombre(auxNombreEmpresa);
         RequestContext.getCurrentInstance().update("form:datosEmpresa");
         RequestContext.getCurrentInstance().execute("PF('errorNombreEmpresa').show()");
      }
      if (!listEmpresasModificar.isEmpty()) {
         log.info("listEmpresasModificar.get(0): " + listEmpresasModificar.get(0) + ", Nom: " + listEmpresasModificar.get(0).getNombre());
      }
   }

   public void modificarEmpresa(Empresas empresa, String confirmarCambio, String valorConfirmar) {
      tablaActiva = 0;
      empresaSeleccionada = empresa;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("CENTROCOSTO")) {
         if (!valorConfirmar.isEmpty()) {
            empresaSeleccionada.getCentrocosto().setNombre(centroCosto);
            for (int i = 0; i < lovCentrosCostos.size(); i++) {
               if (lovCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               empresaSeleccionada.setCentrocosto(lovCentrosCostos.get(indiceUnicoElemento));
               lovCentrosCostos.clear();
               getLovCentrosCostos();
               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            } else {
               RequestContext.getCurrentInstance().update("form:CentroCostoDialogo");
               RequestContext.getCurrentInstance().execute("PF('CentroCostoDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            lovCentrosCostos.clear();
            getLovCentrosCostos();
            empresaSeleccionada.setCentrocosto(new CentrosCostos());
         }
      }
      if (coincidencias == 1) {
         if (!listEmpresasCrear.contains(empresaSeleccionada)) {
            if (listEmpresasModificar.isEmpty()) {
               listEmpresasModificar.add(empresaSeleccionada);
            } else if (!listEmpresasModificar.contains(empresaSeleccionada)) {
               listEmpresasModificar.add(empresaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosEmpresa");
   }

   public void modificarVigenciaMonedaBase(VigenciasMonedasBases vigMoneda) {
      tablaActiva = 1;
      vigenciaMBSeleccionada = vigMoneda;
      if (!listVigenciasMBCrear.contains(vigenciaMBSeleccionada)) {
         if (listVigenciasMonedasBasesModificar.isEmpty()) {
            listVigenciasMonedasBasesModificar.add(vigenciaMBSeleccionada);
         } else if (!listVigenciasMonedasBasesModificar.contains(vigenciaMBSeleccionada)) {
            listVigenciasMonedasBasesModificar.add(vigenciaMBSeleccionada);
         }
         if (guardadoVigencia == true) {
            guardadoVigencia = false;
         }
      }
      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
   }

   public void modificarVigenciaMonedaBase(VigenciasMonedasBases vigenciaMB, String confirmarCambio, String valorConfirmar) {
      tablaActiva = 1;
      vigenciaMBSeleccionada = vigenciaMB;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("MONEDA")) {
         vigenciaMBSeleccionada.getMoneda().setNombre(moneda);
         for (int i = 0; i < lovMonedas.size(); i++) {
            if (lovMonedas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaMBSeleccionada.setMoneda(lovMonedas.get(indiceUnicoElemento));
            lovMonedas.clear();
            getLovMonedas();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            RequestContext.getCurrentInstance().update("form:MonedaDialogo");
            RequestContext.getCurrentInstance().execute("PF('MonedaDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("CODIGOMONEDA")) {
         vigenciaMBSeleccionada.getMoneda().setCodigo(codigoMoneda);
         for (int i = 0; i < lovMonedas.size(); i++) {
            if (lovMonedas.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaMBSeleccionada.setMoneda(lovMonedas.get(indiceUnicoElemento));
            lovMonedas.clear();
            getLovMonedas();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            RequestContext.getCurrentInstance().update("form:MonedaDialogo");
            RequestContext.getCurrentInstance().execute("PF('MonedaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listVigenciasMBCrear.contains(vigenciaMBSeleccionada)) {
            if (listVigenciasMonedasBasesModificar.isEmpty()) {
               listVigenciasMonedasBasesModificar.add(vigenciaMBSeleccionada);
            } else if (!listVigenciasMonedasBasesModificar.contains(vigenciaMBSeleccionada)) {
               listVigenciasMonedasBasesModificar.add(vigenciaMBSeleccionada);
            }
            if (guardadoVigencia == true) {
               guardadoVigencia = false;
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
   }

   public boolean validarCamposNulosCircular(int i) {
      boolean retorno = true;
      if (i == 0) {
         if (circularSeleccionada.getFecha() == null) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoCircular.getFecha() == null) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarCircular.getFecha() == null) {
            retorno = false;
         }
      }
      return retorno;
   }

   public void modificarCirculares(Circulares circular) {
      tablaActiva = 2;
      circularSeleccionada = circular;
      if (circularSeleccionada.getExpedidopor() != null) {
         String textM = circularSeleccionada.getExpedidopor().toUpperCase();
         circularSeleccionada.setExpedidopor(textM);
      }
      if (circularSeleccionada.getExpedidopor() != null) {
         String textM1 = circularSeleccionada.getTexto().toUpperCase();
         circularSeleccionada.setTexto(textM1);
      }
      if (!listCircularesCrear.contains(circularSeleccionada)) {
         if (listCircularesModificar.isEmpty()) {
            listCircularesModificar.add(circularSeleccionada);
         } else if (!listCircularesModificar.contains(circularSeleccionada)) {
            listCircularesModificar.add(circularSeleccionada);
         }
         if (guardadoCircular == true) {
            guardadoCircular = false;
         }
      }
      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosCircular");
   }

   public void cambiarIndice(Empresas empresa, int celda) {
      tablaActiva = 0;
      log.info("Controlador.ControlEmpresa.cambiarIndice()");
      empresaSeleccionada = empresa;
      if (guardadoVigencia == true && guardadoCircular == true) {
         log.info("Controlador.ControlEmpresa.cambiarIndice() empresaSeleccionada: " + empresaSeleccionada);
         cualCelda = celda;
         vigenciaMBSeleccionada = null;
         circularSeleccionada = null;
         auxCodigoEmpresa = empresaSeleccionada.getCodigo();
         auxNombreEmpresa = empresaSeleccionada.getNombre();
         auxNitEmpresa = empresaSeleccionada.getNit();
         if (empresaSeleccionada.getCentrocosto() == null) {
            log.info("Centro costo nulo");
         } else {
            centroCosto = empresaSeleccionada.getCentrocosto().getNombre();
         }
         activoBtnesAdd = false;
         RequestContext.getCurrentInstance().update("form:DETALLES");
         RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
         RequestContext.getCurrentInstance().update("form:DIRECCIONES");
         listaVigenciasMB = null;
         getListaVigenciasMB();
         listaCirculares = null;
         getListaCirculares();
         RequestContext.getCurrentInstance().update("form:datosCircular");
         RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
         contarRegistrosCirculares();
         contarRegistrosVMB();
         if (bandera == 1) {
            restaurarFiltroTablas();
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
      log.info("Controlador.ControlEmpresa.cambiarIndice() Saliendo de la selección");
   }

   public void cambiarIndiceVigencia(VigenciasMonedasBases vigMoneda, int celda) {
      tablaActiva = 1;
      vigenciaMBSeleccionada = vigMoneda;
      circularSeleccionada = null;
      cualCeldaVigencia = celda;
      moneda = vigenciaMBSeleccionada.getMoneda().getNombre();
      codigoMoneda = vigenciaMBSeleccionada.getMoneda().getCodigo();
      auxFechaVigencia = vigenciaMBSeleccionada.getFecha();
      activoBtnesAdd = true;
      RequestContext.getCurrentInstance().update("form:DETALLES");
      RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
      RequestContext.getCurrentInstance().update("form:DIRECCIONES");
      RequestContext.getCurrentInstance().update("form:datosCircular");
   }

   public void cambiarIndiceCircular(Circulares circular, int celda) {
      tablaActiva = 2;
      circularSeleccionada = circular;
      vigenciaMBSeleccionada = null;
      cualCeldaCircular = celda;
      auxFechaCircular = circularSeleccionada.getFecha();
      activoBtnesAdd = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:DETALLES");
      context.update("form:EMPRESABANCO");
      context.update("form:DIRECCIONES");
      RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
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
      if (guardado == false || guardadoVigencia == false || guardadoCircular == false) {
         if (guardado == false) {
            guardarCambiosEmpresa();
         }
         if (guardadoVigencia == false) {
            guardarCambiosVigencia();
         }
         if (guardadoCircular == false) {
            guardarCambiosCircular();
         }
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      activoBtnesAdd = true;
      RequestContext.getCurrentInstance().update("form:DETALLES");
      RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
      RequestContext.getCurrentInstance().update("form:DIRECCIONES");
      contarRegistrosEmpresas();
      contarRegistrosCirculares();
      contarRegistrosVMB();
   }

   public void guardarCambiosEmpresa() {
      try {
         if (!listEmpresasBorrar.isEmpty()) {
            for (int i = 0; i < listEmpresasBorrar.size(); i++) {
               administrarEmpresa.borrarEmpresas(listEmpresasBorrar);
            }
            listEmpresasBorrar.clear();
         }
         if (!listEmpresasCrear.isEmpty()) {
            for (int i = 0; i < listEmpresasCrear.size(); i++) {
               administrarEmpresa.crearEmpresas(listEmpresasCrear);
            }
            listEmpresasCrear.clear();
         }
         if (!listEmpresasModificar.isEmpty()) {
            administrarEmpresa.editarEmpresas(listEmpresasModificar);
            listEmpresasModificar.clear();
         }
         listaEmpresas = null;
         activoBtnesAdd = true;
         RequestContext context = RequestContext.getCurrentInstance();
         context.update("form:DETALLES");
         context.update("form:EMPRESABANCO");
         context.update("form:DIRECCIONES");
         context.update("form:datosEmpresa");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Empresa se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         log.warn("Error guardarCambiosEmpresa : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Empresa, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosVigencia() {
      try {
         if (!listVigenciasMonedasBasesBorrar.isEmpty()) {
            administrarEmpresa.borrarVigenciasMonedasBases(listVigenciasMonedasBasesBorrar);
            listVigenciasMonedasBasesBorrar.clear();
         }
         if (!listVigenciasMBCrear.isEmpty()) {
            administrarEmpresa.crearVigenciasMonedasBases(listVigenciasMBCrear);
            listVigenciasMBCrear.clear();
         }
         if (!listVigenciasMonedasBasesModificar.isEmpty()) {
            administrarEmpresa.editarVigenciasMonedasBases(listVigenciasMonedasBasesModificar);
            listVigenciasMonedasBasesModificar.clear();
         }
         listaVigenciasMB = null;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
         guardadoVigencia = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Concepto Juridico se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         log.warn("Error guardarCambiosVigencia : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Concepto Juridico, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosCircular() {
      try {
         if (!listCircularesBorrar.isEmpty()) {
            administrarEmpresa.borrarCirculares(listCircularesBorrar);
            listCircularesBorrar.clear();
         }
         if (!listCircularesCrear.isEmpty()) {
            administrarEmpresa.crearCirculares(listCircularesCrear);
            listCircularesCrear.clear();
         }
         if (!listCircularesModificar.isEmpty()) {
            administrarEmpresa.editarCirculares(listCircularesModificar);
            listCircularesModificar.clear();
         }
         listaCirculares = null;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosCircular");
         guardadoCircular = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Circular se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         log.warn("Error guardarCambiosCircular : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Circular, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificacionGeneral() {
      cancelarModificacionEmpresa();
      RequestContext.getCurrentInstance().update("form:datosEmpresa");
      cancelarModificacionVigencia();
      RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
      cancelarModificacionCircular();
      RequestContext.getCurrentInstance().update("form:datosCircular");
      cancelarModificacionClonado();
      cambiosPagina = true;
      activoBtnesAdd = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:DETALLES");
      context.update("form:EMPRESABANCO");
      context.update("form:DIRECCIONES");
      context.update("form:ACEPTAR");
      contarRegistrosCirculares();
      contarRegistrosEmpresas();
      contarRegistrosVMB();
   }

   public void cancelarModificacionEmpresa() {
      if (bandera == 1) {
         restaurarFiltroTablas();
      }
      listEmpresasBorrar.clear();
      listEmpresasCrear.clear();
      listEmpresasModificar.clear();
      empresaSeleccionada = null;
      k = 0;
      listaEmpresas = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:datosEmpresa");
   }

   public void cancelarModificacionVigencia() {
      if (bandera == 1) {
         restaurarFiltroTablas();
      }
      listVigenciasMonedasBasesBorrar.clear();
      listVigenciasMBCrear.clear();
      listVigenciasMonedasBasesModificar.clear();
      vigenciaMBSeleccionada = null;
      k = 0;
      listaVigenciasMB = null;
      guardadoVigencia = true;
      RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
   }

   public void cancelarModificacionCircular() {
      if (bandera == 1) {
         restaurarFiltroTablas();
      }
      listCircularesBorrar.clear();
      listCircularesCrear.clear();
      listCircularesModificar.clear();
      circularSeleccionada = null;
      k = 0;
      listaCirculares = null;
      guardadoCircular = true;
      RequestContext.getCurrentInstance().update("form:datosCircular");
   }

   public void editarCelda() {
      if (tablaActiva == 1) {
         editarVigenciaMonedaBase = vigenciaMBSeleccionada;
         if (cualCeldaVigencia == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVigenciaD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaVigenciaD').show()");
            cualCeldaVigencia = -1;
         } else if (cualCeldaVigencia == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoVigenciaD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoVigenciaD').show()");
            cualCeldaVigencia = -1;
         } else if (cualCeldaVigencia == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarMonedaVigenciaD");
            RequestContext.getCurrentInstance().execute("PF('editarMonedaVigenciaD').show()");
            cualCeldaVigencia = -1;
         }
      } else if (tablaActiva == 2) {
         editarCircular = circularSeleccionada;
         if (cualCeldaCircular == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaCircularD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaCircularD').show()");
            cualCeldaCircular = -1;
         } else if (cualCeldaCircular == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarExpedidoCircularD");
            RequestContext.getCurrentInstance().execute("PF('editarExpedidoCircularD').show()");
            cualCeldaCircular = -1;
         } else if (cualCeldaCircular == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarContenidoCircularD");
            RequestContext.getCurrentInstance().execute("PF('editarContenidoCircularD').show()");
            cualCeldaCircular = -1;
         }
      } else if (indexClonadoOrigen == 1) {
         RequestContext.getCurrentInstance().update("formularioDialogos:editarOrigenClonadoD");
         RequestContext.getCurrentInstance().execute("PF('editarOrigenClonadoD').show()");
      } else if (indexClonadoDestino == 1) {
         RequestContext.getCurrentInstance().update("formularioDialogos:editarDestinoClonadoD");
         RequestContext.getCurrentInstance().execute("PF('editarDestinoClonadoD').show()");
      } else if (tablaActiva == 0) {
         if (empresaSeleccionada != null) {
            editarEmpresa = empresaSeleccionada;
            if (cualCelda == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoEmpresaD");
               RequestContext.getCurrentInstance().execute("PF('editarCodigoEmpresaD').show()");
               cualCelda = -1;
            } else if (cualCelda == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarNITEmpresaD");
               RequestContext.getCurrentInstance().execute("PF('editarNITEmpresaD').show()");
               cualCelda = -1;
            } else if (cualCelda == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreEmpresaD");
               RequestContext.getCurrentInstance().execute("PF('editarNombreEmpresaD').show()");
               cualCelda = -1;
            } else if (cualCelda == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarReglamentoEmpresaD");
               RequestContext.getCurrentInstance().execute("PF('editarReglamentoEmpresaD').show()");
               cualCelda = -1;
            } else if (cualCelda == 4) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarManualEmpresaD");
               RequestContext.getCurrentInstance().execute("PF('editarManualEmpresaD').show()");
               cualCelda = -1;
            } else if (cualCelda == 5) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarLogoD");
               RequestContext.getCurrentInstance().execute("PF('editarLogoD').show()");
               cualCelda = -1;
            } else if (cualCelda == 6) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCentroCostoD");
               RequestContext.getCurrentInstance().execute("PF('editarCentroCostoD').show()");
               cualCelda = -1;
            } else if (cualCelda == 7) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoAlternativoD");
               RequestContext.getCurrentInstance().execute("PF('editarCodigoAlternativoD').show()");
               cualCelda = -1;
            }
            activoBtnesAdd = true;
            RequestContext.getCurrentInstance().update("form:DETALLES");
            RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
            RequestContext.getCurrentInstance().update("form:DIRECCIONES");
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      }
   }

   public void dialogoTablaNuevo() {
      if (guardado == false || guardadoVigencia == false || guardadoCircular == false) {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
         RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()");
      }
   }

   public void dialogoNuevoRegistro(int n) {
      if (n == 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroEmpresa");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroEmpresa').show()");
      } else if (empresaSeleccionada != null) {
         if (n == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigencia");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigencia').show()");
         } else if (n == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroCircular");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCircular').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorSeleccionParaInsertar').show()");
      }
   }
   //CREAR 

   public void agregarNuevoEmpresa() {
      boolean respueta = validarCamposNulosEmpresa(1);
      if (respueta == true) {
         int tamDes = 0;
         tamDes = nuevoEmpresa.getNombre().length();
         if (tamDes >= 1 && tamDes <= 50) {
            if (bandera == 1) {
               restaurarFiltroTablas();
            }
            k++;
            l = BigInteger.valueOf(k);
            String text = nuevoEmpresa.getNombre().toUpperCase();
            nuevoEmpresa.setNombre(text);
            nuevoEmpresa.setSecuencia(l);
            if (listaEmpresas == null) {
               listaEmpresas = new ArrayList<Empresas>();
            }
            listEmpresasCrear.add(nuevoEmpresa);
            listaEmpresas.add(nuevoEmpresa);
            empresaSeleccionada = listaEmpresas.get(listaEmpresas.indexOf(nuevoEmpresa));
            nuevoEmpresa = new Empresas();
            cambiosPagina = false;
            activoBtnesAdd = true;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:DETALLES");
            context.update("form:EMPRESABANCO");
            context.update("form:DIRECCIONES");
            context.update("form:ACEPTAR");
            context.update("form:datosEmpresa");
            context.execute("PF('NuevoRegistroEmpresa').hide()");
            contarRegistrosEmpresas();
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorNombreEmpresa').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullEmpresa').show()");
      }
   }

   public void agregarNuevoVigenciaMonedaBase() {
      boolean respueta = validarCamposNulosVigenciaMonedaBase(1);
      if (respueta == true) {
         if (validarFechaVigenciaMonedaBase(1) == true) {
            if (bandera == 1) {
               restaurarFiltroTablas();
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoVigenciaMonedaBase.setSecuencia(l);
            nuevoVigenciaMonedaBase.setEmpresa(empresaSeleccionada);
            if (listaVigenciasMB.size() == 0) {
               listaVigenciasMB = new ArrayList<VigenciasMonedasBases>();
            }
            listVigenciasMBCrear.add(nuevoVigenciaMonedaBase);
            listaVigenciasMB.add(nuevoVigenciaMonedaBase);
            vigenciaMBSeleccionada = listaVigenciasMB.get(listaVigenciasMB.indexOf(nuevoVigenciaMonedaBase));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigencia').hide()");
            contarRegistrosVMB();
            nuevoVigenciaMonedaBase = new VigenciasMonedasBases();
            nuevoVigenciaMonedaBase.setMoneda(new Monedas());
            if (guardadoVigencia == true) {
               guardadoVigencia = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVigencia').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullVigencia').show()");
      }
   }

   public void agregarNuevoCircular() {
      boolean pte = validarCamposNulosCircular(1);
      if (pte == true) {
         if (validarFechaCircular(1) == true) {
            if (bandera == 1) {
               restaurarFiltroTablas();
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoCircular.setSecuencia(l);
            nuevoCircular.setEmpresa(empresaSeleccionada);
            if (listaCirculares.size() == 0) {
               listaCirculares = new ArrayList<Circulares>();
            }
            listCircularesCrear.add(nuevoCircular);
            listaCirculares.add(nuevoCircular);
            circularSeleccionada = listaCirculares.get(listaCirculares.indexOf(nuevoCircular));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCircular");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCircular').hide()");
            contarRegistrosCirculares();
            nuevoCircular = new Circulares();
            if (guardadoCircular == true) {
               guardadoCircular = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasCircular').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullCircular').show()");
      }
   }

   public void limpiarNuevaEmpresa() {
      nuevoEmpresa = new Empresas();
      nuevoEmpresa.setCentrocosto(new CentrosCostos());
      activoBtnesAdd = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:DETALLES");
      RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
      RequestContext.getCurrentInstance().update("form:DIRECCIONES");
   }

   public void limpiarNuevaVigenciaMonedaBase() {
      nuevoVigenciaMonedaBase = new VigenciasMonedasBases();
      nuevoVigenciaMonedaBase.setMoneda(new Monedas());
   }

   public void limpiarNuevaCircular() {
      nuevoCircular = new Circulares();
   }

   public void verificarRegistroDuplicar() {
      if (tablaActiva == 0) {
         duplicarEmpresaM();
      }
      if (tablaActiva == 1) {
         duplicarVigenciaMonedaBaseM();
      }
      if (tablaActiva == 2) {
         duplicarCircularM();
      }
   }

   public void duplicarEmpresaM() {
      if (empresaSeleccionada != null) {
         duplicarEmpresa = new Empresas();
         duplicarEmpresa.setCodigo(empresaSeleccionada.getCodigo());
         duplicarEmpresa.setNit(empresaSeleccionada.getNit());
         duplicarEmpresa.setNombre(empresaSeleccionada.getNombre());
         duplicarEmpresa.setReglamento(empresaSeleccionada.getReglamento());
         duplicarEmpresa.setManualadministrativo(empresaSeleccionada.getManualadministrativo());
         duplicarEmpresa.setLogo(empresaSeleccionada.getLogo());
         duplicarEmpresa.setCodigoalternativo(empresaSeleccionada.getCodigoalternativo());
         duplicarEmpresa.setCentrocosto(empresaSeleccionada.getCentrocosto());

         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroEmpresa");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroEmpresa').show()");
         activoBtnesAdd = true;
         RequestContext.getCurrentInstance().update("form:DETALLES");
         RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
         RequestContext.getCurrentInstance().update("form:DIRECCIONES");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarVigenciaMonedaBaseM() {
      if (vigenciaMBSeleccionada != null) {
         duplicarVigenciaMB = new VigenciasMonedasBases();
         duplicarVigenciaMB.setFecha(vigenciaMBSeleccionada.getFecha());
         duplicarVigenciaMB.setMoneda(vigenciaMBSeleccionada.getMoneda());
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroVigenciaMonedaBase");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaMonedaBase').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarCircularM() {
      if (circularSeleccionada != null) {
         duplicarCircular = new Circulares();
         duplicarCircular.setFecha(circularSeleccionada.getFecha());
         duplicarCircular.setExpedidopor(circularSeleccionada.getExpedidopor());
         duplicarCircular.setTexto(circularSeleccionada.getTexto());
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroCircular");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCircular').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicarEmpresa() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean respueta = validarCamposNulosEmpresa(2);
      if (respueta == true) {
         if (duplicarEmpresa.getNombre().length() >= 1 && duplicarEmpresa.getNombre().length() <= 50) {
            if (bandera == 1) {
               restaurarFiltroTablas();
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarEmpresa.setSecuencia(l);
            if (duplicarEmpresa.getNombre() != null) {
               String text = duplicarEmpresa.getNombre().toUpperCase();
               duplicarEmpresa.setNombre(text);
            }
            listaEmpresas.add(duplicarEmpresa);
            listEmpresasCrear.add(duplicarEmpresa);
            empresaSeleccionada = listaEmpresas.get(listaEmpresas.indexOf(duplicarEmpresa));
            cambiosPagina = false;
            if (guardado == true) {
               guardado = false;
            }
            duplicarEmpresa = new Empresas();
            context.update("form:ACEPTAR");
            context.update("form:datosEmpresa");
            context.execute("PF('DuplicarRegistroEmpresa').hide()");
            contarRegistrosEmpresas();
         } else {
            context.execute("PF('errorNombreEmpresa').show()");
         }
      } else {
         context.execute("PF('errorDatosNullEmpresa').show()");
      }
   }

   public void confirmarDuplicarVigenciaMonedaBase() {
      boolean respueta = validarCamposNulosVigenciaMonedaBase(2);
      if (respueta == true) {
         if (validarFechaVigenciaMonedaBase(2) == true) {
            if (bandera == 1) {
               restaurarFiltroTablas();
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarVigenciaMB.setSecuencia(l);
            duplicarVigenciaMB.setEmpresa(empresaSeleccionada);
            listaVigenciasMB.add(duplicarVigenciaMB);
            listVigenciasMBCrear.add(duplicarVigenciaMB);
            vigenciaMBSeleccionada = listaVigenciasMB.get(listaVigenciasMB.indexOf(duplicarVigenciaMB));
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:ACEPTAR");
            context.update("form:datosVigenciaMonedaBase");
            context.execute("PF('DuplicarRegistroVigenciaMonedaBase').hide()");
            contarRegistrosVMB();
            if (guardadoVigencia == true) {
               guardadoVigencia = false;
               //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            duplicarVigenciaMB = new VigenciasMonedasBases();
            duplicarVigenciaMB.setMoneda(new Monedas());
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasVigencia').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullVigencia').show()");
      }
   }

   public void confirmarDuplicarCircular() {
      boolean respueta = validarCamposNulosCircular(2);
      if (respueta == true) {
         if (validarFechaCircular(2) == true) {
            if (bandera == 1) {
               restaurarFiltroTablas();
            }
            duplicarCircular.setEmpresa(empresaSeleccionada);
            k++;
            l = BigInteger.valueOf(k);
            duplicarCircular.setSecuencia(l);
            listaCirculares.add(duplicarCircular);
            listCircularesCrear.add(duplicarCircular);
            circularSeleccionada = listaCirculares.get(listaCirculares.indexOf(duplicarCircular));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosCircular");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCircular').hide()");
            contarRegistrosCirculares();
            if (guardadoCircular == true) {
               guardadoCircular = false;
            }
            duplicarCircular = new Circulares();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasCircular').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullCircular').show()");
      }
   }

   public void limpiarDuplicarEmpresa() {
      duplicarEmpresa = new Empresas();
      duplicarEmpresa.setCentrocosto(new CentrosCostos());
      activoBtnesAdd = true;
      RequestContext.getCurrentInstance().update("form:DETALLES");
      RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
      RequestContext.getCurrentInstance().update("form:DIRECCIONES");
   }

   public void limpiarDuplicarVigenciaMonedaBase() {
      duplicarVigenciaMB = new VigenciasMonedasBases();
      duplicarVigenciaMB.setMoneda(new Monedas());
   }

   public void limpiarDuplicarCircular() {
      duplicarCircular = new Circulares();
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   public void verificarRegistroBorrar() {
      if (tablaActiva == 0) {
         if (empresaSeleccionada != null) {
            int tam = 0;
            int tam2 = 0;
            if (listaVigenciasMB != null) {
               tam = listaVigenciasMB.size();
            }
            if (listaCirculares != null) {
               tam2 = listaCirculares.size();
            }
            if (tam == 0 && tam2 == 0) {
               borrarEmpresa();
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      } else if (tablaActiva == 1) {
         borrarVigencia();
      } else if (tablaActiva == 2) {
         borrarCircular();
      }
   }

   public void borrarEmpresa() {
      if (empresaSeleccionada != null) {
         if (!listEmpresasModificar.isEmpty() && listEmpresasModificar.contains(empresaSeleccionada)) {
            int modIndex = listEmpresasModificar.indexOf(empresaSeleccionada);
            listEmpresasModificar.remove(modIndex);
            listEmpresasBorrar.add(empresaSeleccionada);
         } else if (!listEmpresasCrear.isEmpty() && listEmpresasCrear.contains(empresaSeleccionada)) {
            int crearIndex = listEmpresasCrear.indexOf(empresaSeleccionada);
            listEmpresasCrear.remove(crearIndex);
         } else {
            listEmpresasBorrar.add(empresaSeleccionada);
         }
         listaEmpresas.remove(empresaSeleccionada);
         if (tipoLista == 1) {
            filtrarListaEmpresas.remove(empresaSeleccionada);
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:datosEmpresa");
         empresaSeleccionada = null;
         activoBtnesAdd = true;
         RequestContext.getCurrentInstance().update("form:DETALLES");
         RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
         RequestContext.getCurrentInstance().update("form:DIRECCIONES");
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      contarRegistrosEmpresas();
   }

   public void borrarVigencia() {
      if (vigenciaMBSeleccionada != null) {
         if (!listVigenciasMonedasBasesModificar.isEmpty() && listVigenciasMonedasBasesModificar.contains(vigenciaMBSeleccionada)) {
            int modIndex = listVigenciasMonedasBasesModificar.indexOf(vigenciaMBSeleccionada);
            listVigenciasMonedasBasesModificar.remove(modIndex);
            listVigenciasMonedasBasesBorrar.add(vigenciaMBSeleccionada);
         } else if (!listVigenciasMBCrear.isEmpty() && listVigenciasMBCrear.contains(vigenciaMBSeleccionada)) {
            int crearIndex = listVigenciasMBCrear.indexOf(vigenciaMBSeleccionada);
            listVigenciasMBCrear.remove(crearIndex);
         } else {
            listVigenciasMonedasBasesBorrar.add(vigenciaMBSeleccionada);
         }
         listaVigenciasMB.remove(vigenciaMBSeleccionada);
         if (tipoListaVigencia == 1) {
            filtrarListaVigenciasMonedasBases.remove(vigenciaMBSeleccionada);
         }
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
         vigenciaMBSeleccionada = null;
         if (guardadoVigencia == true) {
            guardadoVigencia = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      contarRegistrosVMB();
   }

   public void borrarCircular() {
      if (circularSeleccionada != null) {
         if (!listCircularesModificar.isEmpty() && listCircularesModificar.contains(circularSeleccionada)) {
            int modIndex = listCircularesModificar.indexOf(circularSeleccionada);
            listCircularesModificar.remove(modIndex);
            listCircularesBorrar.add(circularSeleccionada);
         } else if (!listCircularesCrear.isEmpty() && listCircularesCrear.contains(circularSeleccionada)) {
            int crearIndex = listCircularesCrear.indexOf(circularSeleccionada);
            listCircularesCrear.remove(crearIndex);
         } else {
            listCircularesBorrar.add(circularSeleccionada);
         }
         listaCirculares.remove(circularSeleccionada);
         if (tipoListaCircular == 1) {
            filtrarListaCirculares.remove(circularSeleccionada);
         }
         cambiosPagina = false;
         circularSeleccionada = null;
         if (guardadoCircular == true) {
            guardadoCircular = false;
         }
         RequestContext.getCurrentInstance().update("form:datosCircular");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      contarRegistrosCirculares();
   }

   public void activarCtrlF11() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (bandera == 0) {
         altoTablaEmpresa = "80";
         empresaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaCodigo");
         empresaCodigo.setFilterStyle("width: 85% !important");
         empresaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaNIT");
         empresaNIT.setFilterStyle("width: 85% !important");
         empresaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaNombre");
         empresaNombre.setFilterStyle("width: 85% !important");
         empresaReglamento = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaReglamento");
         empresaReglamento.setFilterStyle("width: 85% !important");
         empresaManual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaManual");
         empresaManual.setFilterStyle("width: 85% !important");
         empresaLogo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaLogo");
         empresaLogo.setFilterStyle("width: 85% !important");
         empresaCentroCosto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaCentroCosto");
         empresaCentroCosto.setFilterStyle("width: 85% !important");
         empresaCodigoAlternativo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaCodigoAlternativo");
         empresaCodigoAlternativo.setFilterStyle("width: 85% !important");

         vigenciaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciaMonedaBase:vigenciaCodigo");
         vigenciaCodigo.setFilterStyle("width: 85% !important;");
         vigenciaFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciaMonedaBase:vigenciaFecha");
         vigenciaFecha.setFilterStyle("width: 85% !important;");
         vigenciaMoneda = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciaMonedaBase:vigenciaMoneda");
         vigenciaMoneda.setFilterStyle("width: 85% !important;");

         altoTablasSecundarias = "70";
         circularFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCircular:circularFecha");
         circularFecha.setFilterStyle("width: 85% !important;");
         circularExpedido = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCircular:circularExpedido");
         circularExpedido.setFilterStyle("width: 85% !important;");
         circularContenido = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCircular:circularContenido");
         circularContenido.setFilterStyle("width: 85% !important;");

         bandera = 1;
         context.update("form:datosEmpresa");
         context.update("form:datosVigenciaMonedaBase");
         context.update("form:datosCircular");
         contarRegistrosEmpresas();
         contarRegistrosCirculares();
         contarRegistrosVMB();
      } else if (bandera == 1) {
         restaurarFiltroTablas();
      }
      activoBtnesAdd = true;
      context.update("form:DETALLES");
      context.update("form:EMPRESABANCO");
      context.update("form:DIRECCIONES");
   }

   public void restaurarFiltroTablas() {
      RequestContext context = RequestContext.getCurrentInstance();

      altoTablaEmpresa = "100";
      empresaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaCodigo");
      empresaCodigo.setFilterStyle("display: none; visibility: hidden;");
      empresaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaNIT");
      empresaNIT.setFilterStyle("display: none; visibility: hidden;");
      empresaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaNombre");
      empresaNombre.setFilterStyle("display: none; visibility: hidden;");
      empresaReglamento = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaReglamento");
      empresaReglamento.setFilterStyle("display: none; visibility: hidden;");
      empresaManual = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaManual");
      empresaManual.setFilterStyle("display: none; visibility: hidden;");
      empresaLogo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaLogo");
      empresaLogo.setFilterStyle("display: none; visibility: hidden;");
      empresaCentroCosto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaCentroCosto");
      empresaCentroCosto.setFilterStyle("display: none; visibility: hidden;");
      empresaCodigoAlternativo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEmpresa:empresaCodigoAlternativo");
      empresaCodigoAlternativo.setFilterStyle("display: none; visibility: hidden;");
      filtrarListaEmpresas = null;
      tipoLista = 0;

      vigenciaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciaMonedaBase:vigenciaCodigo");
      vigenciaCodigo.setFilterStyle("display: none; visibility: hidden;");
      vigenciaFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciaMonedaBase:vigenciaFecha");
      vigenciaFecha.setFilterStyle("display: none; visibility: hidden;");
      vigenciaMoneda = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciaMonedaBase:vigenciaMoneda");
      vigenciaMoneda.setFilterStyle("display: none; visibility: hidden;");
      filtrarListaVigenciasMonedasBases = null;
      tipoListaVigencia = 0;

      altoTablasSecundarias = "90";
      circularFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCircular:circularFecha");
      circularFecha.setFilterStyle("display: none; visibility: hidden;");
      circularExpedido = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCircular:circularExpedido");
      circularExpedido.setFilterStyle("display: none; visibility: hidden;");
      circularContenido = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCircular:circularContenido");
      circularContenido.setFilterStyle("display: none; visibility: hidden;");
      filtrarListaCirculares = null;
      tipoListaCircular = 0;

      bandera = 0;
      context.update("form:datosEmpresa");
      context.update("form:datosVigenciaMonedaBase");
      context.update("form:datosCircular");
      contarRegistrosEmpresas();
      contarRegistrosCirculares();
      contarRegistrosVMB();
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         restaurarFiltroTablas();
      }
      listEmpresasBorrar.clear();
      listEmpresasCrear.clear();
      listEmpresasModificar.clear();
      listVigenciasMonedasBasesBorrar.clear();
      listVigenciasMBCrear.clear();
      listVigenciasMonedasBasesModificar.clear();
      listCircularesBorrar.clear();
      listCircularesCrear.clear();
      listCircularesModificar.clear();
      empresaSeleccionada = null;
      vigenciaMBSeleccionada = null;
      circularSeleccionada = null;
      k = 0;
      listaEmpresas = null;
      listaVigenciasMB = null;
      listaCirculares = null;
      guardado = true;
      guardadoVigencia = true;
      guardadoCircular = true;
      cambiosPagina = true;
      lovMonedas = null;
      lovCentrosCostos = null;
      activoBtnesAdd = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:DETALLES");
      context.update("form:EMPRESABANCO");
      context.update("form:DIRECCIONES");
      context.update("form:ACEPTAR");
      navegar("atras");
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tablaActiva == 0) {
         if (empresaSeleccionada != null) {
            if (cualCelda == 6) {
               RequestContext.getCurrentInstance().update("form:CentroCostoDialogo");
               RequestContext.getCurrentInstance().execute("PF('CentroCostoDialogo').show()");
               tipoActualizacion = 0;
            }
         }
         if (tablaActiva == 1) {
            RequestContext.getCurrentInstance().update("form:MonedaDialogo");
            RequestContext.getCurrentInstance().execute("PF('MonedaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndex1(int campo, int tipoAct) {
      tipoActualizacion = tipoAct;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("form:CentroCostoDialogo");
         RequestContext.getCurrentInstance().execute("PF('CentroCostoDialogo').show()");
      }
   }

   public void asignarIndex1(Empresas empresa, int campo, int tipoAct) {
      empresaSeleccionada = empresa;
      tipoActualizacion = tipoAct;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("form:CentroCostoDialogo");
         RequestContext.getCurrentInstance().execute("PF('CentroCostoDialogo').show()");
      }
   }

   public void asignarIndex2(int campo, int tipoAct) {
      tipoActualizacion = tipoAct;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("form:MonedaDialogo");
         RequestContext.getCurrentInstance().execute("PF('MonedaDialogo').show()");
      }
   }

   public void asignarIndex2(VigenciasMonedasBases vigenciamB, int campo, int tipoAct) {
      vigenciaMBSeleccionada = vigenciamB;
      tipoActualizacion = tipoAct;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("form:MonedaDialogo");
         RequestContext.getCurrentInstance().execute("PF('MonedaDialogo').show()");
      }
   }

   public void valoresBackupAutocompletarGeneral(int tipoNuevo, String Campo) {
      if (Campo.equals("MONEDA")) {
         if (tipoNuevo == 1) {
            moneda = nuevoVigenciaMonedaBase.getMoneda().getNombre();
         } else if (tipoNuevo == 2) {
            moneda = duplicarVigenciaMB.getMoneda().getNombre();
         }
      }
      if (Campo.equals("CODIGOMONEDA")) {
         if (tipoNuevo == 1) {
            codigoMoneda = nuevoVigenciaMonedaBase.getMoneda().getCodigo();
         } else if (tipoNuevo == 2) {
            codigoMoneda = duplicarVigenciaMB.getMoneda().getCodigo();
         }
      }
      if (Campo.equals("CENTROCOSTO")) {
         if (tipoNuevo == 1) {
            centroCosto = nuevoEmpresa.getCentrocosto().getNombre();
         } else if (tipoNuevo == 2) {
            centroCosto = duplicarEmpresa.getCentrocosto().getNombre();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoVigenciaMonedaBase(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("MONEDA")) {
         if (tipoNuevo == 1) {
            nuevoVigenciaMonedaBase.getMoneda().setNombre(moneda);
         } else if (tipoNuevo == 2) {
            duplicarVigenciaMB.getMoneda().setNombre(moneda);
         }
         for (int i = 0; i < lovMonedas.size(); i++) {
            if (lovMonedas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoVigenciaMonedaBase.setMoneda(lovMonedas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaMonedaBaseCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaMonedaBaseMoneda");
            } else if (tipoNuevo == 2) {
               duplicarVigenciaMB.setMoneda(lovMonedas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaMonedaBaseCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaMonedaBaseMoneda");
            }
            lovMonedas.clear();
            getLovMonedas();
         } else {
            RequestContext.getCurrentInstance().update("form:MonedaDialogo");
            RequestContext.getCurrentInstance().execute("PF('MonedaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaMonedaBaseCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaMonedaBaseMoneda");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaMonedaBaseCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaMonedaBaseMoneda");
            }
         }
      }
      if (confirmarCambio.equalsIgnoreCase("CODIGOMONEDA")) {
         if (tipoNuevo == 1) {
            nuevoVigenciaMonedaBase.getMoneda().setCodigo(codigoMoneda);
         } else if (tipoNuevo == 2) {
            duplicarVigenciaMB.getMoneda().setCodigo(codigoMoneda);
         }
         for (int i = 0; i < lovMonedas.size(); i++) {
            if (lovMonedas.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoVigenciaMonedaBase.setMoneda(lovMonedas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaMonedaBaseCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaMonedaBaseMoneda");
            } else if (tipoNuevo == 2) {
               duplicarVigenciaMB.setMoneda(lovMonedas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaMonedaBaseCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaMonedaBaseMoneda");
            }
            lovMonedas.clear();
            getLovMonedas();
         } else {
            RequestContext.getCurrentInstance().update("form:MonedaDialogo");
            RequestContext.getCurrentInstance().execute("PF('MonedaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaMonedaBaseCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaMonedaBaseMoneda");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaMonedaBaseCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaMonedaBaseMoneda");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoEmpresa(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("CENTROCOSTO")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevoEmpresa.getCentrocosto().setNombre(centroCosto);
            } else if (tipoNuevo == 2) {
               duplicarEmpresa.getCentrocosto().setNombre(centroCosto);
            }
            for (int i = 0; i < lovCentrosCostos.size(); i++) {
               if (lovCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevoEmpresa.setCentrocosto(lovCentrosCostos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpresaCentroCosto");
               } else if (tipoNuevo == 2) {
                  duplicarEmpresa.setCentrocosto(lovCentrosCostos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresaCentroCosto");
               }
               lovCentrosCostos.clear();
               getLovCentrosCostos();
            } else {
               RequestContext.getCurrentInstance().update("form:CentroCostoDialogo");
               RequestContext.getCurrentInstance().execute("PF('CentroCostoDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpresaCentroCosto");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresaCentroCosto");
               }
            }
         } else {
            lovCentrosCostos.clear();
            getLovCentrosCostos();
            if (tipoNuevo == 1) {
               nuevoEmpresa.setCentrocosto(new CentrosCostos());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpresaCentroCosto");
            } else if (tipoNuevo == 2) {
               duplicarEmpresa.setCentrocosto(new CentrosCostos());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresaCentroCosto");
            }
         }
      }

   }

   public void actualizarMoneda() {
      if (tipoActualizacion == 0) {
         vigenciaMBSeleccionada.setMoneda(monedaLovSeleccionada);
         if (!listVigenciasMBCrear.contains(vigenciaMBSeleccionada)) {
            if (listVigenciasMonedasBasesModificar.isEmpty()) {
               listVigenciasMonedasBasesModificar.add(vigenciaMBSeleccionada);
            } else if (!listVigenciasMonedasBasesModificar.contains(vigenciaMBSeleccionada)) {
               listVigenciasMonedasBasesModificar.add(vigenciaMBSeleccionada);
            }
         }
         if (guardadoVigencia == true) {
            guardadoVigencia = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
      } else if (tipoActualizacion == 1) {
         nuevoVigenciaMonedaBase.setMoneda(monedaLovSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaMonedaBaseCodigo");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaMonedaBaseMoneda");
      } else if (tipoActualizacion == 2) {
         duplicarVigenciaMB.setMoneda(monedaLovSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaMonedaBaseCodigo");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaMonedaBaseMoneda");
      }
      filtrarLovMonedas = null;
      monedaLovSeleccionada = null;
      aceptar = true;
      vigenciaMBSeleccionada = null;
      vigenciaMBSeleccionada = null;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovMoneda:globalFilter");
      context.update("form:MonedaDialogo");
      context.update("form:lovMoneda");
      context.update("form:aceptarM");
      context.execute("PF('lovMoneda').clearFilters()");
      context.execute("PF('MonedaDialogo').hide()");
   }

   public void cancelarCambioMoneda() {
      filtrarLovMonedas = null;
      monedaLovSeleccionada = null;
      aceptar = true;
      vigenciaMBSeleccionada = null;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovMoneda:globalFilter");
      context.update("form:MonedaDialogo");
      context.update("form:lovMoneda");
      context.update("form:aceptarM");
      context.execute("PF('lovMoneda').clearFilters()");
      context.execute("PF('MonedaDialogo').hide()");
   }

   public void actualizarCentroCosto() {
      if (tipoActualizacion == 0) {
         empresaSeleccionada.setCentrocosto(centroCostoLovSeleccionado);
         if (!listEmpresasCrear.contains(empresaSeleccionada)) {
            if (listEmpresasModificar.isEmpty()) {
               listEmpresasModificar.add(empresaSeleccionada);
            } else if (!listEmpresasModificar.contains(empresaSeleccionada)) {
               listEmpresasModificar.add(empresaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosEmpresa");
      } else if (tipoActualizacion == 1) {
         nuevoEmpresa.setCentrocosto(centroCostoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEmpresaCentroCosto");
      } else if (tipoActualizacion == 2) {
         duplicarEmpresa.setCentrocosto(centroCostoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresaCentroCosto");
      }
      filtrarLovCentrosCostos = null;
      centroCostoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovCentroCosto:globalFilter");
      context.execute("PF('lovCentroCosto').clearFilters()");
      context.execute("PF('CentroCostoDialogo').hide()");
      context.update("form:CentroCostoDialogo");
      context.update("form:lovCentroCosto");
      context.update("form:aceptarCC");
   }

   public void cancelarCambioCentroCosto() {
      filtrarLovCentrosCostos = null;
      centroCostoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovCentroCosto:globalFilter");
      context.execute("PF('lovCentroCosto').clearFilters()");
      context.execute("PF('CentroCostoDialogo').hide()");
      context.update("form:CentroCostoDialogo");
      context.update("form:lovCentroCosto");
      context.update("form:aceptarCC");
   }

   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   public String exportXML() {
      if (tablaActiva == 0) {
         nombreTabla = ":formExportarEMP:datosEmpresaExportar";
         nombreXML = "Empresas_XML";
      }
      if (tablaActiva == 1) {
         nombreTabla = ":formExportarVMB:datosVigenciaMonedaBaseExportar";
         nombreXML = "ConceptosJuridicos_XML";
      }
      if (tablaActiva == 2) {
         nombreTabla = ":formExportarC:datosCircularExportar";
         nombreXML = "Circulares_XML";
      }
      return nombreTabla;
   }

   public void validarExportPDF() throws IOException {
      if (tablaActiva == 0) {
         exportPDF_E();
         activoBtnesAdd = true;
         RequestContext.getCurrentInstance().update("form:DETALLES");
         RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
         RequestContext.getCurrentInstance().update("form:DIRECCIONES");
      }
      if (tablaActiva == 1) {
         exportPDF_VMB();
      }
      if (tablaActiva == 2) {
         exportPDF_C();
      }
   }

   public void exportPDF_E() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarEMP:datosEmpresaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Empresas_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_VMB() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVMB:datosVigenciaMonedaBaseExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ConceptosJuridicos_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_C() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarC:datosCircularExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Circulares_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void validarExportXLS() throws IOException {
      if (tablaActiva == 0) {
         exportXLS_E();
         activoBtnesAdd = true;
         RequestContext.getCurrentInstance().update("form:DETALLES");
         RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
         RequestContext.getCurrentInstance().update("form:DIRECCIONES");
      }
      if (tablaActiva == 1) {
         exportXLS_VMB();
      }
      if (tablaActiva == 2) {
         exportXLS_C();
      }
   }

   public void exportXLS_E() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarEMP:datosEmpresaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Empresas_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_VMB() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVMB:datosVigenciaMonedaBaseExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ConceptosJuridicos_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_C() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarC:datosCircularExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Circulares_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void eventoFiltrarEmpresa() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      empresaSeleccionada = null;
      contarRegistrosEmpresas();
      listaCirculares = null;
      listaVigenciasMB = null;
      RequestContext.getCurrentInstance().update("form:datosVigenciaMonedaBase");
      RequestContext.getCurrentInstance().update("form:datosCircular");
      contarRegistrosCirculares();
      contarRegistrosVMB();
   }

   public void eventoFiltrarVMB() {
      if (tipoListaVigencia == 0) {
         tipoListaVigencia = 1;
      }
      vigenciaMBSeleccionada = null;
      contarRegistrosVMB();
   }

   public void eventoFiltrarC() {
      if (tipoListaCircular == 0) {
         tipoListaCircular = 1;
      }
      circularSeleccionada = null;
      contarRegistrosCirculares();
   }

   public void contarRegistrosEmpresas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpresas");
   }

   public void contarRegistrosVMB() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCJuridicos");
   }

   public void contarRegistrosCirculares() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCircular");
   }

   public void contarRegistrosLovEmpresas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroLovEmpresa");
   }

   public void contarRegistrosLovMonedas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroMoneda");
   }

   public void contarRegistrosLovCentrosC() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCentro");
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {

      if (tablaActiva == 0) {
         if (empresaSeleccionada != null) {
            verificarRastroEmpresa();
         } else {
            RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
         }
      } else if (tablaActiva == 1) {
         verificarRastroVigencia();
      }
      if (tablaActiva == 2) {
         verificarRastroCircular();
      }
   }

   public void verificarRastroEmpresa() {
      if (empresaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(empresaSeleccionada.getSecuencia(), "EMPRESAS");
         backUp = empresaSeleccionada.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "Empresas";
            msnConfirmarRastro = "La tabla EMPRESAS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
   }

   public void verificarRastroEmpresaH() {
      if (administrarRastros.verificarHistoricosTabla("EMPRESAS")) {
         nombreTablaRastro = "Empresas";
         msnConfirmarRastroHistorico = "La tabla EMPRESAS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroVigencia() {
      if (vigenciaMBSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(vigenciaMBSeleccionada.getSecuencia(), "VIGENCIASMONEDASBASES");
         backUp = vigenciaMBSeleccionada.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "VigenciasMonedasBases";
            msnConfirmarRastro = "La tabla VIGENCIASMONEDASBASES tiene rastros para el registro seleccionado, ¿desea continuar?";
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
   }

   public void verificarRastroVigenciaH() {
      if (administrarRastros.verificarHistoricosTabla("VIGENCIASMONEDASBASES")) {
         nombreTablaRastro = "VigenciasMonedasBases";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASMONEDASBASES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroCircular() {
      if (circularSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(circularSeleccionada.getSecuencia(), "CIRCULARES");
         backUp = circularSeleccionada.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "Circulares";
            msnConfirmarRastro = "La tabla CIRCULARES tiene rastros para el registro seleccionado, ¿desea continuar?";
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
   }

   public void verificarRastroCircularH() {
      if (administrarRastros.verificarHistoricosTabla("CIRCULARES")) {
         nombreTablaRastro = "Circulares";
         msnConfirmarRastroHistorico = "La tabla CIRCULARES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void cancelarModificacionClonado() {
      empresaOrigenClonado = new Empresas();
      empresaDestinoClonado = new Empresas();
      activoCasillaClonado = true;
      RequestContext.getCurrentInstance().update("form:CodigoNuevoClonado");
      RequestContext.getCurrentInstance().update("form:DescripcionNuevoClonado");
      RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
      RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");

   }

   public void dispararDialogoEmpresaOrigenClonado() {
      variableClonado = "origen";
      lovEmpresas = null;
      getLovEmpresas();
      RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");

   }

   public void dispararDialogoEmpresaDestinoClonado() {
      variableClonado = "destino";
      lovEmpresas = null;
      getLovEmpresas();
      int indice = lovEmpresas.indexOf(empresaOrigenClonado);
      lovEmpresas.remove(indice);
      RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
   }

   public void actualizarEmpresasClonado() {
      if (variableClonado.equalsIgnoreCase("origen")) {
         log.info("Origen");
         actualizarEmpresaOrigenClonado();
      } else if (variableClonado.equalsIgnoreCase("destino")) {
         log.info("Destino");
         actualizarEmpresaDestinoClonado();
      }
      aceptar = true;
   }

   public void actualizarEmpresaOrigenClonado() {
      empresaOrigenClonado = empresaLovSeleccionada;
      filtrarLovEmpresas = null;
      empresaLovSeleccionada = null;
      activoCasillaClonado = false;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:CodigoNuevoClonado");
      RequestContext.getCurrentInstance().update("form:DescripcionNuevoClonado");
      RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
      RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");

      RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
      RequestContext.getCurrentInstance().update("form:aceptarEmp");
      RequestContext.getCurrentInstance().update("form:lovEmpresa");
      context.reset("form:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void actualizarEmpresaDestinoClonado() {
      empresaDestinoClonado = empresaLovSeleccionada;
      filtrarLovEmpresas = null;
      empresaLovSeleccionada = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
      RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
      RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
      RequestContext.getCurrentInstance().update("form:aceptarEmp");
      RequestContext.getCurrentInstance().update("form:lovEmpresa");
      RequestContext.getCurrentInstance().update("form:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void cancelarSeleccionEmpresa() {
      filtrarLovEmpresas = null;
      empresaLovSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
      RequestContext.getCurrentInstance().update("form:aceptarEmp");
      RequestContext.getCurrentInstance().update("form:lovEmpresa");
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void validarProcesoClonado() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (empresaDestinoClonado.getSecuencia() != null && empresaOrigenClonado.getSecuencia() != null) {
         empresaOrigenClonado = new Empresas();
         empresaDestinoClonado = new Empresas();
         activoCasillaClonado = true;
         RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
         RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
         log.info("Proceso Clonado en Proceso");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorProcesoClonado').show()");
      }
   }

   public void posicionProcesoClonadoOrigen() {
      if (guardado == true) {
         indexClonadoOrigen = 1;
         indexClonadoDestino = -1;
         auxClonadoCodigoOrigen = empresaOrigenClonado.getCodigo();
         auxClonadoNombreOrigen = empresaOrigenClonado.getNombre();
         activoBtnesAdd = true;
         RequestContext.getCurrentInstance().update("form:DETALLES");
         RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
         RequestContext.getCurrentInstance().update("form:DIRECCIONES");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void posicionProcesoClonadoDestino() {
      if (guardado == true) {
         indexClonadoOrigen = -1;
         indexClonadoDestino = 1;
         auxClonadoCodigoDestino = empresaDestinoClonado.getCodigo();
         auxClonadoNombreDestino = empresaDestinoClonado.getNombre();
         activoBtnesAdd = true;
         RequestContext.getCurrentInstance().update("form:DETALLES");
         RequestContext.getCurrentInstance().update("form:EMPRESABANCO");
         RequestContext.getCurrentInstance().update("form:DIRECCIONES");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void autoCompletarEmpresaOrigenClonado(String valorConfirmar, int tipoAutoCompletar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (tipoAutoCompletar == 0) {
         if (!valorConfirmar.isEmpty()) {
            short num = Short.parseShort(valorConfirmar);
            for (int i = 0; i < lovEmpresas.size(); i++) {
               if (lovEmpresas.get(i).getCodigo() == num) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               empresaOrigenClonado = lovEmpresas.get(indiceUnicoElemento);
               lovEmpresas.clear();
               getLovEmpresas();
            } else {
               empresaOrigenClonado.setCodigo(auxClonadoCodigoOrigen);
               empresaOrigenClonado.setNombre(auxClonadoNombreOrigen);
               RequestContext.getCurrentInstance().update("form:CodigoNuevoClonado");
               RequestContext.getCurrentInstance().update("form:DescripcionNuevoClonado");
               dispararDialogoEmpresaOrigenClonado();
            }
         } else {
            empresaOrigenClonado = new Empresas();
            RequestContext.getCurrentInstance().update("form:CodigoNuevoClonado");
            RequestContext.getCurrentInstance().update("form:DescripcionNuevoClonado");
         }
      }
      if (tipoAutoCompletar == 1) {
         if (!valorConfirmar.isEmpty()) {
            for (int i = 0; i < lovEmpresas.size(); i++) {
               if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               empresaOrigenClonado = lovEmpresas.get(indiceUnicoElemento);
               lovEmpresas.clear();
               getLovEmpresas();
            } else {
               empresaOrigenClonado.setCodigo(auxClonadoCodigoOrigen);
               empresaOrigenClonado.setNombre(auxClonadoNombreOrigen);
               RequestContext.getCurrentInstance().update("form:CodigoNuevoClonado");
               RequestContext.getCurrentInstance().update("form:DescripcionNuevoClonado");
               dispararDialogoEmpresaOrigenClonado();
            }
         } else {
            empresaOrigenClonado = new Empresas();
            RequestContext.getCurrentInstance().update("form:CodigoNuevoClonado");
            RequestContext.getCurrentInstance().update("form:DescripcionNuevoClonado");
         }
      }
   }

   public void autoCompletarEmpresaDestinoClonado(String valorConfirmar, int tipoAutoCompletar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoAutoCompletar == 0) {
         if (!valorConfirmar.isEmpty()) {
            short num = Short.parseShort(valorConfirmar);
            for (int i = 0; i < lovEmpresas.size(); i++) {
               if (lovEmpresas.get(i).getCodigo() == num) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               empresaDestinoClonado = lovEmpresas.get(indiceUnicoElemento);
               lovEmpresas.clear();
               getLovEmpresas();
            } else {
               empresaDestinoClonado.setCodigo(auxClonadoCodigoDestino);
               empresaDestinoClonado.setNombre(auxClonadoNombreDestino);
               RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
               RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
               dispararDialogoEmpresaDestinoClonado();
            }
         } else {
            empresaDestinoClonado = new Empresas();
            RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
            RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
         }
      }
      if (tipoAutoCompletar == 1) {
         if (!valorConfirmar.isEmpty()) {
            for (int i = 0; i < lovEmpresas.size(); i++) {
               if (lovEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               empresaDestinoClonado = lovEmpresas.get(indiceUnicoElemento);
               lovEmpresas.clear();
               getLovEmpresas();
            } else {
               empresaDestinoClonado.setCodigo(auxClonadoCodigoDestino);
               empresaDestinoClonado.setNombre(auxClonadoNombreDestino);
               RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
               RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
               dispararDialogoEmpresaDestinoClonado();
            }
         } else {
            empresaDestinoClonado = new Empresas();
            RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
            RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
         }
      }
   }

   public boolean validarNuevaEmpresaClon() {
      int conteo = 0;
      for (int i = 0; i < lovEmpresas.size(); i++) {
         if (lovEmpresas.get(i).getCodigo() == empresaDestinoClonado.getCodigo() || lovEmpresas.get(i).getNombre().equals(empresaDestinoClonado.getNombre())) {
            conteo++;
         }
      }
      if (conteo > 0) {
         return false;
      } else {
         return true;
      }
   }

   public void clonarEmpresa() {
      log.info("ControlEmpresa.clonarEmpresa() 1");
      if (!empresaDestinoClonado.getNombre().isEmpty() && empresaDestinoClonado.getCodigo() >= 1 && empresaOrigenClonado.getCodigo() >= 1) {
         log.info("ControlEmpresa.clonarEmpresa() 2");
//         if (validarNuevaEmpresaClon() == true) {
         log.info("ControlEmpresa.clonarEmpresa() 3");
         errorClonado = administrarEmpresa.clonarEmpresa(empresaOrigenClonado.getCodigo(), empresaDestinoClonado.getCodigo());
         log.info("ControlEmpresa.clonarEmpresa() 4");
         if (errorClonado.equals("SI")) {
            FacesMessage msg = new FacesMessage("Información", "El registro fue clonado con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         } else {
            RequestContext.getCurrentInstance().update("form:errorClonado");
            RequestContext.getCurrentInstance().execute("PF('errorClonado').show()");
         }
//         } else {
//            RequestContext.getCurrentInstance().execute("PF('errorRepetidosClon').show()");
//         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosClonado').show()");
      }
   }

   //GETTERS AND SETTERS
   public List<Empresas> getListaEmpresas() {
      if (listaEmpresas == null) {
         listaEmpresas = administrarEmpresa.listaEmpresas();
         if (listaEmpresas != null) {
            for (int i = 0; i < listaEmpresas.size(); i++) {
               if (listaEmpresas.get(i).getCentrocosto() == null) {
                  listaEmpresas.get(i).setCentrocosto(new CentrosCostos());
               }
            }
         }
      }
      return listaEmpresas;
   }

   public void setListaEmpresas(List<Empresas> setListaEmpresas) {
      this.listaEmpresas = setListaEmpresas;
   }

   public List<Empresas> getFiltrarListaEmpresas() {
      return filtrarListaEmpresas;
   }

   public void setFiltrarListaEmpresas(List<Empresas> setFiltrarListaEmpresas) {
      this.filtrarListaEmpresas = setFiltrarListaEmpresas;
   }

   public Empresas getNuevoEmpresa() {
      return nuevoEmpresa;
   }

   public void setNuevoEmpresa(Empresas setNuevoEmpresa) {
      this.nuevoEmpresa = setNuevoEmpresa;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public Empresas getEditarEmpresa() {
      return editarEmpresa;
   }

   public void setEditarEmpresa(Empresas setEditarEmpresa) {
      this.editarEmpresa = setEditarEmpresa;
   }

   public Empresas getDuplicarEmpresa() {
      return duplicarEmpresa;
   }

   public void setDuplicarEmpresa(Empresas setDuplicarEmpresa) {
      this.duplicarEmpresa = setDuplicarEmpresa;
   }

   public List<VigenciasMonedasBases> getListaVigenciasMB() {
      if (listaVigenciasMB == null) {
         if (empresaSeleccionada != null) {
            listaVigenciasMB = administrarEmpresa.listaVigenciasMonedasBasesParaEmpresa(empresaSeleccionada.getSecuencia());
            for (int i = 0; i < listaVigenciasMB.size(); i++) {
               if (listaVigenciasMB.get(i).getMoneda() == null) {
                  listaVigenciasMB.get(i).setMoneda(new Monedas());
               }
            }
         }
      }
      return listaVigenciasMB;
   }

   public void setListaVigenciasMB(List<VigenciasMonedasBases> setListaVigenciasMonedasBases) {
      this.listaVigenciasMB = setListaVigenciasMonedasBases;
   }

   public List<VigenciasMonedasBases> getFiltrarListaVigenciasMonedasBases() {
      return filtrarListaVigenciasMonedasBases;
   }

   public void setFiltrarListaVigenciasMonedasBases(List<VigenciasMonedasBases> setFiltrarListaVigenciasMonedasBases) {
      this.filtrarListaVigenciasMonedasBases = setFiltrarListaVigenciasMonedasBases;
   }

   public List<Empresas> getListEmpresasModificar() {
      return listEmpresasModificar;
   }

   public void setListEmpresasModificar(List<Empresas> setListEmpresasModificar) {
      this.listEmpresasModificar = setListEmpresasModificar;
   }

   public List<Empresas> getListEmpresasCrear() {
      return listEmpresasCrear;
   }

   public void setListEmpresasCrear(List<Empresas> setListEmpresasCrear) {
      this.listEmpresasCrear = setListEmpresasCrear;
   }

   public List<Empresas> getListEmpresasBorrar() {
      return listEmpresasBorrar;
   }

   public void setListEmpresasBorrar(List<Empresas> setListEmpresasBorrar) {
      this.listEmpresasBorrar = setListEmpresasBorrar;
   }

   public List<VigenciasMonedasBases> getListVigenciasMonedasBasesModificar() {
      return listVigenciasMonedasBasesModificar;
   }

   public void setListVigenciasMonedasBasesModificar(List<VigenciasMonedasBases> setListVigenciasMonedasBasesModificar) {
      this.listVigenciasMonedasBasesModificar = setListVigenciasMonedasBasesModificar;
   }

   public VigenciasMonedasBases getNuevoVigenciaMonedaBase() {
      return nuevoVigenciaMonedaBase;
   }

   public void setNuevoVigenciaMonedaBase(VigenciasMonedasBases setNuevoVigenciaMonedaBase) {
      this.nuevoVigenciaMonedaBase = setNuevoVigenciaMonedaBase;
   }

   public List<VigenciasMonedasBases> getListVigenciasMBCrear() {
      return listVigenciasMBCrear;
   }

   public void setListVigenciasMBCrear(List<VigenciasMonedasBases> setListVigenciasMonedasBasesCrear) {
      this.listVigenciasMBCrear = setListVigenciasMonedasBasesCrear;
   }

   public List<VigenciasMonedasBases> getLisVigenciasMonedasBasesBorrar() {
      return listVigenciasMonedasBasesBorrar;
   }

   public void setListVigenciasMonedasBasesBorrar(List<VigenciasMonedasBases> setListVigenciasMonedasBasesBorrar) {
      this.listVigenciasMonedasBasesBorrar = setListVigenciasMonedasBasesBorrar;
   }

   public VigenciasMonedasBases getEditarVigenciaMonedaBase() {
      return editarVigenciaMonedaBase;
   }

   public void setEditarVigenciaMonedaBase(VigenciasMonedasBases setEditarVigenciaMonedaBase) {
      this.editarVigenciaMonedaBase = setEditarVigenciaMonedaBase;
   }

   public VigenciasMonedasBases getDuplicarVigenciaMB() {
      return duplicarVigenciaMB;
   }

   public void setDuplicarVigenciaMB(VigenciasMonedasBases setDuplicarVigenciaMonedaBase) {
      this.duplicarVigenciaMB = setDuplicarVigenciaMonedaBase;
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

   public List<CentrosCostos> getLovCentrosCostos() {
      if (lovCentrosCostos == null) {
         lovCentrosCostos = administrarEmpresa.lovCentrosCostos();
      }
      return lovCentrosCostos;
   }

   public void setLovCentrosCostos(List<CentrosCostos> setLovCentrosCostos) {
      this.lovCentrosCostos = setLovCentrosCostos;
   }

   public List<CentrosCostos> getFiltrarLovCentrosCostos() {
      return filtrarLovCentrosCostos;
   }

   public void setFiltrarLovCentrosCostos(List<CentrosCostos> setFiltrarLovCentrosCostos) {
      this.filtrarLovCentrosCostos = setFiltrarLovCentrosCostos;
   }

   public CentrosCostos getCentroCostoLovSeleccionado() {
      return centroCostoLovSeleccionado;
   }

   public void setCentroCostoLovSeleccionado(CentrosCostos setCentroCostoSeleccionado) {
      this.centroCostoLovSeleccionado = setCentroCostoSeleccionado;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean setCambiosPagina) {
      this.cambiosPagina = setCambiosPagina;
   }

   public String getAltoTablaEmpresa() {
      return altoTablaEmpresa;
   }

   public void setAltoTablaEmpresa(String setAltoTablaEmpresa) {
      this.altoTablaEmpresa = setAltoTablaEmpresa;
   }

   public List<Monedas> getLovMonedas() {
      if (lovMonedas == null) {
         lovMonedas = administrarEmpresa.lovMonedas();
      }
      return lovMonedas;
   }

   public void setLovMonedas(List<Monedas> setLovMonedas) {
      this.lovMonedas = setLovMonedas;
   }

   public List<Monedas> getFiltrarLovMonedas() {
      return filtrarLovMonedas;
   }

   public void setFiltrarLovMonedas(List<Monedas> setFiltrarLovMonedas) {
      this.filtrarLovMonedas = setFiltrarLovMonedas;
   }

   public Monedas getMonedaLovSeleccionada() {
      return monedaLovSeleccionada;
   }

   public void setMonedaLovSeleccionada(Monedas setMonedaSeleccionado) {
      this.monedaLovSeleccionada = setMonedaSeleccionado;
   }

   public List<Circulares> getListaCirculares() {
      if (listaCirculares == null) {
         if (empresaSeleccionada != null) {
            listaCirculares = administrarEmpresa.listaCircularesParaEmpresa(empresaSeleccionada.getSecuencia());
            log.info("Controlador.ControlEmpresa.getListaCirculares(): " + listaCirculares);
         }
      }
      return listaCirculares;
   }

   public void setListaCirculares(List<Circulares> setListaCirculares) {
      this.listaCirculares = setListaCirculares;
   }

   public List<Circulares> getFiltrarListaCirculares() {
      return filtrarListaCirculares;
   }

   public void setFiltrarListaCirculares(List<Circulares> setFiltrarListaCirculares) {
      this.filtrarListaCirculares = setFiltrarListaCirculares;
   }

   public List<Circulares> getListCircularesModificar() {
      return listCircularesModificar;
   }

   public void setListCircularesModificar(List<Circulares> setListCircularesModificar) {
      this.listCircularesModificar = setListCircularesModificar;
   }

   public Circulares getNuevoCircular() {
      return nuevoCircular;
   }

   public void setNuevoCircular(Circulares setNuevoCircular) {
      this.nuevoCircular = setNuevoCircular;
   }

   public List<Circulares> getListCircularesCrear() {
      return listCircularesCrear;
   }

   public void setListCircularesCrear(List<Circulares> setListCircularesCrear) {
      this.listCircularesCrear = setListCircularesCrear;
   }

   public List<Circulares> getListCircularesBorrar() {
      return listCircularesBorrar;
   }

   public void setListCircularesBorrar(List<Circulares> setListCircularesBorrar) {
      this.listCircularesBorrar = setListCircularesBorrar;
   }

   public Circulares getEditarCircular() {
      return editarCircular;
   }

   public void setEditarCircular(Circulares setEditarCircular) {
      this.editarCircular = setEditarCircular;
   }

   public Circulares getDuplicarCircular() {
      return duplicarCircular;
   }

   public void setDuplicarCircular(Circulares setDuplicarCircular) {
      this.duplicarCircular = setDuplicarCircular;
   }

   public String getAltoTablasSecundarias() {
      return altoTablasSecundarias;
   }

   public void setAltoTablasSecundarias(String setAltoTablaCircular) {
      this.altoTablasSecundarias = setAltoTablaCircular;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String setPaginaAnterior) {
      this.paginaAnterior = setPaginaAnterior;
   }

   public List<Empresas> getLovEmpresas() {
      if (lovEmpresas == null) {
         lovEmpresas = administrarEmpresa.listaEmpresas();
      }
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> lovEmpresas) {
      this.lovEmpresas = lovEmpresas;
   }

   public List<Empresas> getFiltrarLovEmpresas() {
      return filtrarLovEmpresas;
   }

   public void setFiltrarLovEmpresas(List<Empresas> filtrarLovEmpresas) {
      this.filtrarLovEmpresas = filtrarLovEmpresas;
   }

   public Empresas getEmpresaLovSeleccionada() {
      return empresaLovSeleccionada;
   }

   public void setEmpresaLovSeleccionada(Empresas empresaLovSeleccionada) {
      this.empresaLovSeleccionada = empresaLovSeleccionada;
   }

   public Empresas getEmpresaOrigenClonado() {
      return empresaOrigenClonado;
   }

   public void setEmpresaOrigenClonado(Empresas empresaOrigenClonado) {
      this.empresaOrigenClonado = empresaOrigenClonado;
   }

   public Empresas getEmpresaDestinoClonado() {
      return empresaDestinoClonado;
   }

   public void setEmpresaDestinoClonado(Empresas empresaDestinoClonado) {
      this.empresaDestinoClonado = empresaDestinoClonado;
   }

   public boolean isActivoCasillaClonado() {
      return activoCasillaClonado;
   }

   public void setActivoCasillaClonado(boolean activoCasillaClonado) {
      this.activoCasillaClonado = activoCasillaClonado;
   }

   public boolean isActivoBtnesAdd() {
      return activoBtnesAdd;
   }

   public void setActivoBtnesAdd(boolean activoBtnesAdd) {
      this.activoBtnesAdd = activoBtnesAdd;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public VigenciasMonedasBases getVigenciaMBSeleccionada() {
      return vigenciaMBSeleccionada;
   }

   public void setVigenciaMBSeleccionada(VigenciasMonedasBases vigenciaMBSeleccionada) {
      this.vigenciaMBSeleccionada = vigenciaMBSeleccionada;
   }

   public Circulares getCircularSeleccionada() {
      return circularSeleccionada;
   }

   public void setCircularSeleccionada(Circulares circularSeleccionada) {
      this.circularSeleccionada = circularSeleccionada;
   }

   public String getInfoRegistroLovEmpresa() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresa");
      infoRegistroLovEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroLovEmpresa;
   }

   public void setInfoRegistroLovEmpresa(String infoRegistroLovEmpresa) {
      this.infoRegistroLovEmpresa = infoRegistroLovEmpresa;
   }

   public String getInfoRegistroLovMoneda() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovMoneda");
      infoRegistroLovMoneda = String.valueOf(tabla.getRowCount());
      return infoRegistroLovMoneda;
   }

   public void setInfoRegistroLovMoneda(String infoRegistroLovMoneda) {
      this.infoRegistroLovMoneda = infoRegistroLovMoneda;
   }

   public String getInfoRegistroLovCentro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovCentroCosto");
      infoRegistroLovCentro = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCentro;
   }

   public void setInfoRegistroLovCentro(String infoRegistroLovCentro) {
      this.infoRegistroLovCentro = infoRegistroLovCentro;
   }

   public String getInfoRegistroEmpresas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpresa");
      infoRegistroEmpresas = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresas;
   }

   public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
      this.infoRegistroEmpresas = infoRegistroEmpresas;
   }

   public String getInfoRegistroCJuridicos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaMonedaBase");
      infoRegistroCJuridicos = String.valueOf(tabla.getRowCount());
      return infoRegistroCJuridicos;
   }

   public void setInfoRegistroCJuridicos(String infoRegistroCJuridicos) {
      this.infoRegistroCJuridicos = infoRegistroCJuridicos;
   }

   public String getInfoRegistroCirculares() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCircular");
      infoRegistroCirculares = String.valueOf(tabla.getRowCount());
      return infoRegistroCirculares;
   }

   public void setInfoRegistroCirculares(String infoRegistroCirculares) {
      this.infoRegistroCirculares = infoRegistroCirculares;
   }

   public String getErrorClonado() {
      return errorClonado;
   }

   public void setErrorClonado(String errorClonado) {
      this.errorClonado = errorClonado;
   }

}
