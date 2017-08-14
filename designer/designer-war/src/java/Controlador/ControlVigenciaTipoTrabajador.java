package Controlador;

import Entidades.ClasesPensiones;
import Entidades.Empleados;
import Entidades.MotivosRetiros;
import Entidades.Pensionados;
import Entidades.Personas;
import Entidades.Retirados;
import Entidades.TiposPensionados;
import Entidades.TiposTrabajadores;
import Entidades.TiposCotizantes;
import Entidades.VigenciasTiposTrabajadores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasTiposTrabajadoresInterface;
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
 * @author AndresPineda
 */
@ManagedBean
@SessionScoped
public class ControlVigenciaTipoTrabajador implements Serializable {

   private static Logger log = Logger.getLogger(ControlVigenciaTipoTrabajador.class);

   @EJB
   AdministrarVigenciasTiposTrabajadoresInterface administrarVigenciasTiposTrabajadores;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //Vigencias Tipos Trabajadores
   private List<VigenciasTiposTrabajadores> listaVigenciasTT;
   private List<VigenciasTiposTrabajadores> filtrarVTT;
   private VigenciasTiposTrabajadores vigenciaTTSeleccionada;
   //Tipos Trabajadores
   private List<TiposTrabajadores> lovTiposTrabajadores;
   private TiposTrabajadores tipoTrabajadorSeleccionado;
   private List<TiposTrabajadores> filtradoTiposTrabajadores;
   private Empleados empleado;
   private int tipoActualizacion;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla VC
   private Column vttFecha, vttTipoTrabajador, vttTipoCotizante;
   //Paneles Retirados - Pensionados
//   private Panel panelRetiradosMensaje, panelRetiradosInput, panelPensionadosMensaje, panelPensionadosInput;
   private String estiloRetiradosMensaje, estiloRetiradosInput, estiloPensionadosMensaje, estiloPensionadosInput;
   //Otros
   private boolean aceptar;
   //modificar
   private List<VigenciasTiposTrabajadores> listVTTModificar;
   private boolean guardado;
   //crear VC
   public VigenciasTiposTrabajadores nuevaVigencia;
   private List<VigenciasTiposTrabajadores> listVTTCrear;
   private BigInteger l;
   private int k;
   //borrar VC
   private List<VigenciasTiposTrabajadores> listVTTBorrar;
   //editar celda
   private VigenciasTiposTrabajadores editarVTT;
   private int cualCelda, tipoLista;
   private boolean cambioEditor, aceptarEditar;
   //duplicar
   private VigenciasTiposTrabajadores duplicarVTT;
   //elementos retirados
   private Retirados retiroVigencia;
   private List<MotivosRetiros> lovMotivosRetiros;
   private List<MotivosRetiros> filtradoMotivosRetiros;
   private MotivosRetiros motivoRetiroSeleccionado;
   private boolean indexRetiro;
   private boolean indexPension;
   //Boolean cambio registro retirados
   private boolean cambioRetiros;
   //Editar o Crear Retiro
   private boolean operacionRetiro;
   //Bandera almacenar extrabajador
   private boolean almacenarRetirado;
   private Retirados retiroCopia;
   private boolean banderaLimpiarRetiro;
   private boolean banderaEliminarRetiro;
   //elementos pensionados
   private List<Pensionados> lovPensionados;
   private Pensionados pensionVigencia;
   private Pensionados pensionadoSeleccionado;
   private List<Personas> lovPersonas;
   private Personas personaSeleccionada;
   private List<ClasesPensiones> lovclasesPensiones;
   private ClasesPensiones clasesPensionesSeleccionada;
   private List<TiposPensionados> lovTiposPensionados;
   private TiposPensionados tiposPensionadosSeleccionada;
   private Pensionados pensionCopia;
   //bandera Editar o crear pension
   private boolean operacionPension;
   //bandera encontro o no el registro pension en la base de datos
   private boolean almacenarPensionado;
   //bandera que encuentra si el registro pension se limpio
   private boolean banderaLimpiarPension;
   private boolean banderaEliminarPension;
   private boolean cambioPension;
   //filtrados listas pensionados
   private List<ClasesPensiones> clasesPensionesFiltrado;
   private List<TiposPensionados> tiposPensionadosFiltrado;
   private List<Pensionados> pensionadosFiltrado;
   private List<Personas> personasFiltrado;
   private boolean permitirIndex;
   private String tipoTrabajador;
   private Date fechaVigenciaVTT;
   private Date fechaParametro;
   private String altoTabla;
   //
   private String infoRegistro;
   private String infoRegistroTipoTrabajador;
   private String infoRegistroMotivoRetiros;
   private String infoRegistroClasePension;
   private String infoRegistroTipoPension;
   private String infoRegistroEmpleado;
   private String infoRegistroPersona;
   //
   private boolean cambiosPagina;
   //
   private String mensajeValidacion;
   //
   private DataTable tablaC;
   //
   private boolean activarLOV;

   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   /**
    * Constructo del Controlador
    */
   public ControlVigenciaTipoTrabajador() {
//        lovPensionados = new ArrayList<Pensionados>();
      lovPensionados = null;
      pensionadoSeleccionado = new Pensionados();
      //
      pensionVigencia = new Pensionados();
      pensionVigencia.setClase(new ClasesPensiones());
      pensionVigencia.setCausabiente(new Empleados());
      pensionVigencia.getCausabiente().setPersona(new Personas());
      pensionVigencia.setTipopensionado(new TiposPensionados());
      pensionVigencia.setTutor(new Personas());
      //
      //listaPersonas = new ArrayList<Personas>();
      lovPersonas = null;
      personaSeleccionada = new Personas();
      //
      //clasesPensiones = new ArrayList<ClasesPensiones>();
      lovclasesPensiones = null;
      clasesPensionesSeleccionada = new ClasesPensiones();
      //
      //tiposPensionados = new ArrayList<TiposPensionados>();
      lovTiposPensionados = null;
      tiposPensionadosSeleccionada = new TiposPensionados();

      operacionPension = false;
      almacenarPensionado = false;
      banderaLimpiarPension = false;
      cambioPension = false;
      banderaEliminarPension = false;
      //
      banderaEliminarRetiro = false;
      banderaLimpiarRetiro = false;
      operacionRetiro = false;
      almacenarRetirado = false;
      retiroVigencia = new Retirados();
      retiroVigencia.setMotivoretiro(new MotivosRetiros());
      //
      //motivosRetiros = new ArrayList<MotivosRetiros>();
      lovMotivosRetiros = null;
      motivoRetiroSeleccionado = new MotivosRetiros();
      //
      listaVigenciasTT = null;
      //
      //listaTiposTrabajadores = new ArrayList<TiposTrabajadores>();
      lovTiposTrabajadores = null;
      empleado = new Empleados();
      //Otros
      aceptar = true;
      //borrar aficiones
      listVTTBorrar = new ArrayList<VigenciasTiposTrabajadores>();
      //crear aficiones
      listVTTCrear = new ArrayList<VigenciasTiposTrabajadores>();
      k = 0;
      //modificar aficiones
      listVTTModificar = new ArrayList<VigenciasTiposTrabajadores>();
      //editar
      editarVTT = new VigenciasTiposTrabajadores();
      cambioEditor = false;
      aceptarEditar = true;
      cualCelda = -1;
      tipoLista = 0;
      //guardar
      guardado = true;
      //Crear VC
      nuevaVigencia = new VigenciasTiposTrabajadores();
      nuevaVigencia.setTipotrabajador(new TiposTrabajadores());
      nuevaVigencia.getTipotrabajador().setTipocotizante(new TiposCotizantes());
      vigenciaTTSeleccionada = null;
      indexPension = false;
      indexRetiro = false;

      cambioRetiros = false;
      retiroCopia = new Retirados();
      pensionCopia = new Pensionados();

      permitirIndex = true;
      altoTabla = "116";
      cambiosPagina = true;
      mensajeValidacion = " ";
      activarLOV = true;
      paginaAnterior = "nominaf";

      estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
      estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
      estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
      estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovPensionados = null;
      lovPersonas = null;
      lovTiposTrabajadores = null;
      lovclasesPensiones = null;
      lovMotivosRetiros = null;
      lovTiposPensionados = null;
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
         administrarVigenciasTiposTrabajadores.obtenerConexion(ses.getId());
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
      String pagActual = "emplvigenciatipotrabajador";
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

   public String retornarPagina() {
      return paginaAnterior;
   }

   //EMPLEADO DE LA VIGENCIA
   /**
    * Metodo que recibe la secuencia empleado desde la pagina anterior y obtiene
    * el empleado referenciado
    *
    * @param sec Secuencia del Empleado
    */
   public void recibirEmpleado(Empleados empl, String pagina) {
      paginaAnterior = pagina;
      listaVigenciasTT = null;
      empleado = empl;
      getListaVigenciasTT();
      if (listaVigenciasTT != null) {
         if (!listaVigenciasTT.isEmpty()) {
            vigenciaTTSeleccionada = listaVigenciasTT.get(0);
            cargarPrimerosDatos();
         }
      }
   }

   public void cargarPrimerosDatos() {
      if (vigenciaTTSeleccionada != null) {
         VigenciasTiposTrabajadores vigenciaTemporal = vigenciaTTSeleccionada;
         short n1 = 1;
         short n2 = 2;
         TiposTrabajadores tipoTrabajadorRetirado = administrarVigenciasTiposTrabajadores.tipoTrabajadorCodigo(n1);
//         panelRetiradosInput = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelRetiradosInput");
         if (tipoTrabajadorRetirado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            indexRetiro = true;
            cargarRetiro();
            estiloRetiradosInput = "position: absolute; left: 440px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: visible;";
         } else {
            estiloRetiradosInput = "position: absolute; left: 440px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
         }

//         panelRetiradosMensaje = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelRetiradosMensaje");
         if (tipoTrabajadorRetirado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            estiloRetiradosMensaje = "position: absolute; left: 440px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
         } else {
            estiloRetiradosMensaje = "position: absolute; left: 440px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: visible;";
         }

         TiposTrabajadores tipoTrabajadorPensionado = administrarVigenciasTiposTrabajadores.tipoTrabajadorCodigo(n2);
//         panelPensionadosInput = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelPensionadosInput");
         if (tipoTrabajadorPensionado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            indexPension = true;
            cargarPension();
            estiloPensionadosInput = "position: absolute; left: 12px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left;  visibility: visible;";
         } else {
            estiloPensionadosInput = "position: absolute; left: 12px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
         }

//         panelPensionadosMensaje = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelPensionadosMensaje");
         if (tipoTrabajadorPensionado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            estiloPensionadosMensaje = "position: absolute; left: 12px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; display: none; visibility: hidden;";
         } else {
            estiloPensionadosMensaje = "position: absolute; left: 12px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: visible;";
         }
      }
   }

   public void modificarVTT(VigenciasTiposTrabajadores vtt) {
      if (!listVTTCrear.contains(vigenciaTTSeleccionada)) {
         if (listVTTModificar.isEmpty()) {
            listVTTModificar.add(vigenciaTTSeleccionada);
         } else if (!listVTTModificar.contains(vigenciaTTSeleccionada)) {
            listVTTModificar.add(vigenciaTTSeleccionada);
         }
         if (cambiosPagina) {
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      guardado = false;
      RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
   }

   public boolean validarFechasRegistroVTT(int i) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      boolean retorno = true;
      if (i == 0) {
         VigenciasTiposTrabajadores auxiliar = null;
         auxiliar = vigenciaTTSeleccionada;
         if (auxiliar.getFechavigencia().after(fechaParametro)) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevaVigencia.getFechavigencia().after(fechaParametro)) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarVTT.getFechavigencia().after(fechaParametro)) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      return retorno;
   }

   public void modificarFechasVTT(VigenciasTiposTrabajadores vtt, int c) {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      vigenciaTTSeleccionada = vtt;
      if (vigenciaTTSeleccionada.getFechavigencia() != null) {
         boolean retorno = false;
         retorno = validarFechasRegistroVTT(0);
         if (retorno == true) {
            cambiarIndice(vtt, c);
            modificarVTT(vtt);
         } else {
            vigenciaTTSeleccionada.setFechavigencia(fechaVigenciaVTT);

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
            RequestContext.getCurrentInstance().execute("PF('errorFechaVTT').show()");
         }
      } else {
         vigenciaTTSeleccionada.setFechavigencia(fechaVigenciaVTT);

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
         RequestContext.getCurrentInstance().execute("PF('errorRegNewVTT').show()");
      }
   }

   /**
    * Metodo que modifica los cambios efectuados en la tabla
    * VigenciaTiposTrabajador de la pagina
    *
    * @param indice Fila en la cual se realizo el cambio
    */
   public void modificarVTT(VigenciasTiposTrabajadores vtt, String confirmarCambio, String valorConfirmar) {
      vigenciaTTSeleccionada = vtt;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TIPOTRABAJADOR")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         vigenciaTTSeleccionada.getTipotrabajador().setNombre(tipoTrabajador);

         for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
            if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaTTSeleccionada.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));

            lovTiposTrabajadores.clear();
            getLovTiposTrabajadores();
         } else {
            permitirIndex = false;
            contarRegistros();
            // modificarInfoRegistroTipoTrabajador(lovTiposTrabajadores.size());
            RequestContext.getCurrentInstance().update("formLovs:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listVTTCrear.contains(vigenciaTTSeleccionada)) {

            if (listVTTModificar.isEmpty()) {
               listVTTModificar.add(vigenciaTTSeleccionada);
            } else if (!listVTTModificar.contains(vigenciaTTSeleccionada)) {
               listVTTModificar.add(vigenciaTTSeleccionada);
            }
            if (cambiosPagina) {
               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         guardado = false;

      }
      RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("TIPOTRABAJADOR")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (tipoNuevo == 1) {
            tipoTrabajador = nuevaVigencia.getTipotrabajador().getNombre();
         } else if (tipoNuevo == 2) {
            tipoTrabajador = duplicarVTT.getTipotrabajador().getNombre();
         }
      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("TIPOTRABAJADOR")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (tipoNuevo == 1) {
            nuevaVigencia.getTipotrabajador().setNombre(tipoTrabajador);
         } else if (tipoNuevo == 2) {
            duplicarVTT.getTipotrabajador().setNombre(tipoTrabajador);
         }
         for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
            if (lovTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigencia.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoTrabajador");
            } else if (tipoNuevo == 2) {
               duplicarVTT.setTipotrabajador(lovTiposTrabajadores.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoTrabajador");
            }
            lovTiposTrabajadores.clear();
            getLovTiposTrabajadores();
         } else {
            RequestContext.getCurrentInstance().update("formLovs:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoTrabajador");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoTrabajador");
            }
         }
      }
   }

   public void cambiosPension() {
      if (cambioPension == false) {
         cambioPension = true;
      }
      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void posicionTabla() {
      FacesContext context = FacesContext.getCurrentInstance();
      log.info("Controlador.ControlVigenciaTipoTrabajador.posicionTabla()");
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      vigenciaTTSeleccionada = listaVigenciasTT.get(indice);
      cualCelda = columna;
      cambioVisibleRetiradosInput();
      cambioVisibleRetiradosMensaje();
      cambioVisiblePensionadoInput();
      cambioVisiblePensionadoMensaje();
      RequestContext.getCurrentInstance().execute("PF('datosVTTEmpleado').unselectAllRows(); PF('datosVTTEmpleado').selectRow(" + indice + ");");
//      cambiarIndice(vigenciaTTSeleccionada, columna);
   }

   //Ubicacion Celda.
   /**
    * Metodo que obtiene la posicion dentro de la tabla VigenciasTiposTrabajador
    * y lanza los cambios en los paneles Retirados y Pensionados en caso de
    * encontrar concordancia del registro con alguno de ellos
    *
    * @param indice Fila de la tabla
    * @param celda Columna de la tabla
    */
   public void cambiarIndice(VigenciasTiposTrabajadores vtt, int celda) {
      log.info("Controlador.ControlVigenciaTipoTrabajador.cambiarIndice()");
      if (permitirIndex) {
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (cambioRetiros == false && cambioPension == false) {
            vigenciaTTSeleccionada = vtt;
            cualCelda = celda;
            fechaVigenciaVTT = vigenciaTTSeleccionada.getFechavigencia();
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            cambioVisibleRetiradosInput();
            cambioVisibleRetiradosMensaje();
            cambioVisiblePensionadoInput();
            cambioVisiblePensionadoMensaje();
         } else {
            if (cambioRetiros == true) {
               activarLOV = false;
               RequestContext.getCurrentInstance().update("form:listaValores");
               RequestContext.getCurrentInstance().update("formularioDialogos:guardarCambiosRetiros");
               RequestContext.getCurrentInstance().execute("PF('guardarCambiosRetiros').show()");
            }
            if (cambioPension == true) {
               activarLOV = false;
               RequestContext.getCurrentInstance().update("form:listaValores");
               RequestContext.getCurrentInstance().update("formularioDialogos:guardarCambiosPensionados");
               RequestContext.getCurrentInstance().execute("PF('guardarCambiosPensionados').show()");
            }
         }
      }
   }

   //GUARDAR
   /**
    * Guarda los cambios efectuador en la VigenciaTiposTrabajador y actualiza la
    * pagina en su totalidad
    */
   public void guardarCambiosVTT() {
      if (guardado == false) {
         if (!listVTTBorrar.isEmpty()) {
            for (int i = 0; i < listVTTBorrar.size(); i++) {
               administrarVigenciasTiposTrabajadores.borrarVTT(listVTTBorrar.get(i));
            }
            listVTTBorrar.clear();
         }
         if (!listVTTCrear.isEmpty()) {
            for (int i = 0; i < listVTTCrear.size(); i++) {
               administrarVigenciasTiposTrabajadores.crearVTT(listVTTCrear.get(i));

            }
            listVTTCrear.clear();
         }
         if (!listVTTModificar.isEmpty()) {
            administrarVigenciasTiposTrabajadores.modificarVTT(listVTTModificar);
            listVTTModificar.clear();
         }

         retiroVigencia = new Retirados();
         retiroVigencia.setMotivoretiro(new MotivosRetiros());
         pensionVigencia = new Pensionados();
         pensionVigencia.setClase(new ClasesPensiones());
         pensionVigencia.setCausabiente(new Empleados());
         pensionVigencia.getCausabiente().setPersona(new Personas());
         pensionVigencia.setTipopensionado(new TiposPensionados());
         pensionVigencia.setTutor(new Personas());
         FacesContext c = FacesContext.getCurrentInstance();
//         panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
         estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
//         panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
         estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
//         panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
         estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
//         panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
         estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";

         RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
         RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
         RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
         RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");
         RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");

         guardado = true;
         k = 0;
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         listaVigenciasTT = null;
         getListaVigenciasTT();
         if (listaVigenciasTT != null) {
            vigenciaTTSeleccionada = listaVigenciasTT.get(0);
         }
         contarRegistros();
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }
   //CANCELAR MODIFICACIONES

   /**
    * Cancela las modificaciones efectuadas en la pagina
    */
   public void cancelarModificacion() {
      cerrarFiltrado();
      activarLOV = true;
      listVTTBorrar.clear();
      listVTTCrear.clear();
      listVTTModificar.clear();
      cambioPension = false;
      cambioRetiros = false;
      k = 0;
      listaVigenciasTT = null;
      vigenciaTTSeleccionada = null;
      guardado = true;
      permitirIndex = true;
      getListaVigenciasTT();
      contarRegistros();
      cambiosPagina = true;
      almacenarRetirado = false;
      almacenarPensionado = false;
      banderaEliminarRetiro = false;

      FacesContext c = FacesContext.getCurrentInstance();
      estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
      estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
      estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
      estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";

      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
      RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
      RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
      RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");

      RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
      context.reset("form:motivoRetiro");
      context.reset("form:fechaRetiro");
      context.reset("form:observacion");

      context.reset("form:fechaPensionInicio");
      context.reset("form:fechaPensionFinal");
      context.reset("form:porcentajePension");
      context.reset("form:clasePension");
      context.reset("form:tipoPensionado");
      context.reset("form:resolucionPension");
      context.reset("form:causaBiente");
      context.reset("form:tutorPension");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:listaValores");

   }

   //MOSTRAR DATOS CELDA
   /**
    * Metodo que muestra los dialogos de editar con respecto a la lista real o
    * la lista filtrada y a la columna
    */
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaTTSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (vigenciaTTSeleccionada != null) {
         editarVTT = vigenciaTTSeleccionada;

         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaDialogo");
            RequestContext.getCurrentInstance().execute("PF('editarFechaDialogo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('editarTipoTrabajadorDialogo').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoCotizanterDialogo");
            RequestContext.getCurrentInstance().execute("PF('editarTipoCotizanterDialogo').show()");
            cualCelda = -1;
         }
      }
   }
   //CREAR VU

   /**
    * Metodo que se encarga de agregar una nueva VigenciasTiposTrabajador
    */
   public void agregarNuevaVTT() {
      log.info("ControlVigenciaTipoTrabajador");
      log.info("Secuencia: " + nuevaVigencia.getSecuencia());
      if (nuevaVigencia.getFechavigencia() != null && nuevaVigencia.getTipotrabajador().getSecuencia() != null) {//Evalua que los datos no sean nulos ni esten vacios
         if (validarFechasRegistroVTT(1) == true) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
               cerrarFiltrado();
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigencia.setSecuencia(l);
            nuevaVigencia.setEmpleado(empleado);
            listVTTCrear.add(nuevaVigencia);
            log.info("Secuencia VTT: " + nuevaVigencia.getSecuencia());
            listaVigenciasTT.add(nuevaVigencia);
            vigenciaTTSeleccionada = listaVigenciasTT.get(listaVigenciasTT.indexOf(nuevaVigencia));
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            nuevaVigencia = new VigenciasTiposTrabajadores();
            nuevaVigencia.setTipotrabajador(new TiposTrabajadores());
            nuevaVigencia.getTipotrabajador().setTipocotizante(new TiposCotizantes());
            contarRegistros();
//            panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
            estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
//            panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
            estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
//            panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
            estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
//            panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
            estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";

            RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
            RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
            RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
            RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");

            RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVTT').hide();");
            if (cambiosPagina) {
               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            guardado = false;
            banderaEliminarRetiro = false;
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechaVTT').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNewVTT').show()");
      }
   }
   //LIMPIAR NUEVO REGISTRO

   /**
    * Limpia las casillas del nuevo registro
    */
   public void limpiarNuevaVTT() {
      nuevaVigencia = new VigenciasTiposTrabajadores();
      nuevaVigencia.setTipotrabajador(new TiposTrabajadores());
      nuevaVigencia.getTipotrabajador().setTipocotizante(new TiposCotizantes());
   }
   //DUPLICAR VC

   /**
    * Duplica una VigenciasTiposTrabajador
    */
   public void duplicarVigenciaTT() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaTTSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (vigenciaTTSeleccionada != null) {
         duplicarVTT = new VigenciasTiposTrabajadores();
         k++;
         l = BigInteger.valueOf(k);

         duplicarVTT.setEmpleado(vigenciaTTSeleccionada.getEmpleado());
         duplicarVTT.setFechavigencia(vigenciaTTSeleccionada.getFechavigencia());
         duplicarVTT.setTipotrabajador(vigenciaTTSeleccionada.getTipotrabajador());

         FacesContext c = FacesContext.getCurrentInstance();
//         panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
         estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
//         panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
         estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
//         panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
         estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
//         panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
         estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";

         RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
         RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
         RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
         RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVTT");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVTT').show()");
      }
   }

   /**
    * Guarda los datos del duplicado de la vigencia
    */
   public void confirmarDuplicar() {

      int contador = 0;
      mensajeValidacion = " ";
      int pasa = 0;
      int fechas = 0;

      if (duplicarVTT.getFechavigencia() == null) {
         mensajeValidacion = mensajeValidacion + "   * Fecha \n";
      } else {

         for (int j = 0; j < listaVigenciasTT.size(); j++) {
            if (duplicarVTT.getFechavigencia().equals(listaVigenciasTT.get(j).getFechavigencia())) {
               fechas++;
            }
         }
         if (fechas > 0) {
            RequestContext.getCurrentInstance().update("form:validacionFechas");
            RequestContext.getCurrentInstance().execute("PF('validacionFechas').show()");
            pasa++;

         } else if (duplicarVTT.getFechavigencia() != null && duplicarVTT.getTipotrabajador().getSecuencia() != null) {
            if (validarFechasRegistroVTT(2) == true) {
               k++;
               l = BigInteger.valueOf(k);
               duplicarVTT.setSecuencia(l);
               listaVigenciasTT.add(duplicarVTT);
               listVTTCrear.add(duplicarVTT);
               contarRegistros();
               vigenciaTTSeleccionada = listaVigenciasTT.get(listaVigenciasTT.indexOf(duplicarVTT));

               FacesContext c = FacesContext.getCurrentInstance();
               estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
               estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
               estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
               estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";

               RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
               RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
               RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
               RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");
               RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");

               activarLOV = true;
               RequestContext.getCurrentInstance().update("form:listaValores");

               if (cambiosPagina) {
                  cambiosPagina = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               guardado = false;
               if (bandera == 1) {
                  cerrarFiltrado();
               }
               duplicarVTT = new VigenciasTiposTrabajadores();
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVTT').hide();");
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechaVTT').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechaVTT').show()");
         }
      }
   }
   //LIMPIAR DUPLICAR

   /**
    * Limpia las casillas de un duplicar registro
    */
   public void limpiarduplicarVTT() {
      duplicarVTT = new VigenciasTiposTrabajadores();
      duplicarVTT.setTipotrabajador(new TiposTrabajadores());
      duplicarVTT.getTipotrabajador().setTipocotizante(new TiposCotizantes());
   }

   //BORRAR VTT
   /**
    * Borra una VigenciaTiposTrabajador, en caso de que sea pensionado o
    * extrabajador realiza el borrado de una manera independiente. En caso de
    * que alguna casilla este llena muestra un mensaje de advertencia en los dos
    * casos
    */
   public void borrarVTT() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaTTSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (vigenciaTTSeleccionada != null) {
            if ((indexRetiro == false) && (indexPension == false)) {
               if (!listVTTModificar.isEmpty() && listVTTModificar.contains(vigenciaTTSeleccionada)) {
                  int modIndex = listVTTModificar.indexOf(vigenciaTTSeleccionada);
                  listVTTModificar.remove(modIndex);
                  listVTTBorrar.add(vigenciaTTSeleccionada);
               } else if (!listVTTCrear.isEmpty() && listVTTCrear.contains(vigenciaTTSeleccionada)) {
                  int crearIndex = listVTTCrear.indexOf(vigenciaTTSeleccionada);
                  listVTTCrear.remove(crearIndex);
               } else {
                  listVTTBorrar.add(vigenciaTTSeleccionada);
               }
               listaVigenciasTT.remove(vigenciaTTSeleccionada);
               if (tipoLista == 1) {
                  filtrarVTT.remove(vigenciaTTSeleccionada);
               }

               RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");

               if (cambiosPagina) {
                  cambiosPagina = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               guardado = false;
            } else {
               if (indexRetiro == true) {
                  banderaEliminarRetiro = true;
                  if ((retiroVigencia.getFecharetiro() == null) && (retiroVigencia.getMotivoretiro().getSecuencia() == null)
                          && (retiroVigencia.getDescripcion() == null)) {
                     if (retiroVigencia.getMotivoretiro().getSecuencia() == null) {
                        retiroVigencia.setMotivoretiro(null);
                     }
                     administrarVigenciasTiposTrabajadores.borrarRetirado(retiroVigencia);
                     if (!listVTTModificar.isEmpty() && listVTTModificar.contains(vigenciaTTSeleccionada)) {
                        int modIndex = listVTTModificar.indexOf(vigenciaTTSeleccionada);
                        listVTTModificar.remove(modIndex);
                        listVTTBorrar.add(vigenciaTTSeleccionada);
                     } else if (!listVTTCrear.isEmpty() && listVTTCrear.contains(vigenciaTTSeleccionada)) {
                        int crearIndex = listVTTCrear.indexOf(vigenciaTTSeleccionada);
                        listVTTCrear.remove(crearIndex);
                     } else {
                        listVTTBorrar.add(vigenciaTTSeleccionada);
                     }
                     listaVigenciasTT.remove(vigenciaTTSeleccionada);
                     if (tipoLista == 1) {
                        filtrarVTT.remove(vigenciaTTSeleccionada);
                     }

                     RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");

                     if (cambiosPagina) {
                        cambiosPagina = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                     }
                     guardado = false;
                  } else {
                     RequestContext.getCurrentInstance().execute("PF('informacionEliminarExtrabajador').show()");
                  }
                  banderaLimpiarRetiro = false;
               }
               if (indexPension == true) {
                  banderaEliminarPension = true;
                  if ((pensionVigencia.getFechainiciopension() == null) && (pensionVigencia.getFechafinalpension() == null)
                          && (pensionVigencia.getPorcentaje() == null) && (pensionVigencia.getClase().getSecuencia() == null)
                          && (pensionVigencia.getTipopensionado().getSecuencia() == null) && (pensionVigencia.getResolucionpension() == null)
                          && (pensionVigencia.getTutor().getSecuencia() == null) && (pensionVigencia.getCausabiente().getSecuencia() == null)) {
                     if (pensionVigencia.getCausabiente().getSecuencia() == null) {
                        pensionVigencia.setCausabiente(null);
                     }
                     if (pensionVigencia.getClase().getSecuencia() == null) {
                        pensionVigencia.setClase(null);
                     }
                     if (pensionVigencia.getTipopensionado().getSecuencia() == null) {
                        pensionVigencia.setTipopensionado(null);
                     }
                     if (pensionVigencia.getTutor().getSecuencia() == null) {
                        pensionVigencia.setTutor(null);
                     }
                     administrarVigenciasTiposTrabajadores.borrarPensionado(pensionVigencia);
                     if (!listVTTModificar.isEmpty() && listVTTModificar.contains(vigenciaTTSeleccionada)) {
                        int modIndex = listVTTModificar.indexOf(vigenciaTTSeleccionada);
                        listVTTModificar.remove(modIndex);
                        listVTTBorrar.add(vigenciaTTSeleccionada);
                     } else if (!listVTTCrear.isEmpty() && listVTTCrear.contains(vigenciaTTSeleccionada)) {
                        int crearIndex = listVTTCrear.indexOf(vigenciaTTSeleccionada);
                        listVTTCrear.remove(crearIndex);
                     } else {

                        listVTTBorrar.add(vigenciaTTSeleccionada);
                     }
                     listaVigenciasTT.remove(vigenciaTTSeleccionada);
                     if (tipoLista == 1) {
                        filtrarVTT.remove(vigenciaTTSeleccionada);
                     }
                     contarRegistros();
                     RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");

                     if (cambiosPagina) {
                        cambiosPagina = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                     }
                     guardado = false;
                  } else {
                     //Dialogo de aviso de limpiar registro adicional pensionados
                     RequestContext.getCurrentInstance().execute("PF('informacionEliminarPensionado').show()");
                  }
                  banderaLimpiarPension = false;
               }
            }
            FacesContext c = FacesContext.getCurrentInstance();
            estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
            estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
            estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
            estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";

            RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
            RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
            RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
            RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");
            RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
         }
         vigenciaTTSeleccionada = null;
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         indexPension = false;
         indexRetiro = false;
         banderaLimpiarRetiro = false;
         banderaLimpiarPension = false;
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   /**
    * Metodo que activa el filtrado de la tabla VigenciasTiposTrabajadores por
    * medio del boton filtrado o la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         vttFecha = (Column) c.getViewRoot().findComponent("form:datosVTTEmpleado:vttFecha");
         vttFecha.setFilterStyle("width: 85% !important;");
         vttTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosVTTEmpleado:vttTipoTrabajador");
         vttTipoTrabajador.setFilterStyle("width: 85% !important;");
         vttTipoCotizante = (Column) c.getViewRoot().findComponent("form:datosVTTEmpleado:vttTipoCotizante");
         vttTipoCotizante.setFilterStyle("width: 85% !important;");
         altoTabla = "96";
         RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
      }
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      cerrarFiltrado();
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      listVTTBorrar.clear();
      listVTTCrear.clear();
      listVTTModificar.clear();
      vigenciaTTSeleccionada = null;
      k = 0;
      listaVigenciasTT = null;
      guardado = false;
      cambiosPagina = true;
      cambioPension = false;
      cambioRetiros = false;
      //tiposPensionados = null;        
      lovclasesPensiones = null;
      lovPersonas = null;
      lovPensionados = null;
      lovMotivosRetiros = null;
      lovTiposTrabajadores = null;
      limpiarListasValor();
      navegar("atras");
   }

   public void cerrarFiltrado() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         vttFecha = (Column) c.getViewRoot().findComponent("form:datosVTTEmpleado:vttFecha");
         vttFecha.setFilterStyle("display: none; visibility: hidden;");
         vttTipoTrabajador = (Column) c.getViewRoot().findComponent("form:datosVTTEmpleado:vttTipoTrabajador");
         vttTipoTrabajador.setFilterStyle("display: none; visibility: hidden;");
         vttTipoCotizante = (Column) c.getViewRoot().findComponent("form:datosVTTEmpleado:vttTipoCotizante");
         vttTipoCotizante.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "116";
         RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
         bandera = 0;
         filtrarVTT = null;
         tipoLista = 0;
      }
   }

   //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO)
   /**
    * Metodo que muestra los dialogos de las listas dentro de la pagina
    *
    * @param vtt
    * @param campo Dialogo
    * @param tipoAct Tipo actualizacion = LISTA - NUEVO - DUPLICADO
    * @param tt Tipo Trabajador : Pensionado / Retirado / VigenciaTipoTrabajador
    */
   public void asignarIndex(VigenciasTiposTrabajadores vtt, int campo, int tipoAct, int tt) {
      RequestContext context = RequestContext.getCurrentInstance();
      activarLOV = true;
      vigenciaTTSeleccionada = vtt;
      context.update("form:listaValores");
      log.info("VigenciasTiposTrabajadores: " + vtt.toString());
      if (tt == 0) {
         tipoActualizacion = tipoAct;
         if (campo == 0) {
            tipoTrabajadorSeleccionado = null;
            activarLOV = false;
            context.update("form:listaValores");
            contarRegistrosTipoTrabajador();
            context.update("formLovs:TipoTrabajadorDialogo");
            context.execute("PF('TipoTrabajadorDialogo').show()");
         }
      }
      if (tt == 1) {
         indexPension = true;
         tipoActualizacion = tipoAct;
         if (campo == 0) {
            clasesPensionesSeleccionada = null;
            activarLOV = true;
            context.update("form:listaValores");
            contarRegistrosClasePension();
            context.update("formLovs:clasePensionDialogo");
            context.execute("PF('clasePensionDialogo').show()");
         } else if (campo == 1) {
            tiposPensionadosSeleccionada = null;
            activarLOV = true;
            context.update("form:listaValores");
            contarRegistrosTipoPension();
            context.update("formLovs:tipoPensionadoDialogo");
            context.execute("PF('tipoPensionadoDialogo').show()");
         } else if (campo == 2) {
            pensionadoSeleccionado = null;
            activarLOV = true;
            context.update("form:listaValores");
            contarRegistrosEmpleado();
            context.update("formLovs:causaBientesDialogo");
            context.execute("PF('causaBientesDialogo').show()");
         } else if (campo == 3) {
            personaSeleccionada = null;
            activarLOV = true;
            context.update("form:listaValores");
            contarRegistrosPersona();
            context.update("formLovs:tutorDialogo");
            context.execute("PF('tutorDialogo').show()");
         }
      }
      if (tt == 2) {
         indexRetiro = true;
         tipoActualizacion = tipoAct;
         if (campo == 0) {
            motivoRetiroSeleccionado = null;
            activarLOV = true;
            context.update("form:listaValores");
            contarRegistrosMotivosRetiros();
            context.update("formLovs:RetirosDialogo");
            context.execute("PF('RetirosDialogo').show()");
         }
      }
   }

   public void asignarIndex(int campo, int tipoAct, int tt) {
      RequestContext context = RequestContext.getCurrentInstance();
      activarLOV = true;
      context.update("form:listaValores");
      if (tt == 0) {
         tipoActualizacion = tipoAct;
         if (campo == 0) {
            tipoTrabajadorSeleccionado = null;
            activarLOV = false;
            context.update("form:listaValores");
            contarRegistrosTipoTrabajador();
            context.update("formLovs:TipoTrabajadorDialogo");
            context.execute("PF('TipoTrabajadorDialogo').show()");
         }
      }
      if (tt == 1) {
         indexPension = true;
         tipoActualizacion = tipoAct;
         if (campo == 0) {
            clasesPensionesSeleccionada = null;
            activarLOV = true;
            context.update("form:listaValores");
            contarRegistrosClasePension();
            context.update("formLovs:clasePensionDialogo");
            context.execute("PF('clasePensionDialogo').show()");
         } else if (campo == 1) {
            tiposPensionadosSeleccionada = null;
            activarLOV = true;
            context.update("form:listaValores");
            contarRegistrosTipoPension();
            context.update("formLovs:tipoPensionadoDialogo");
            context.execute("PF('tipoPensionadoDialogo').show()");
         } else if (campo == 2) {
            pensionadoSeleccionado = null;
            activarLOV = true;
            context.update("form:listaValores");
            contarRegistrosEmpleado();
            context.update("formLovs:causaBientesDialogo");
            context.execute("PF('causaBientesDialogo').show()");
         } else if (campo == 3) {
            personaSeleccionada = null;
            activarLOV = true;
            context.update("form:listaValores");
            contarRegistrosPersona();
            context.update("formLovs:tutorDialogo");
            context.execute("PF('tutorDialogo').show()");
         }
      }
      if (tt == 2) {
         indexRetiro = true;
         tipoActualizacion = tipoAct;
         if (campo == 0) {
            motivoRetiroSeleccionado = null;
            activarLOV = true;
            context.update("form:listaValores");
            contarRegistrosMotivosRetiros();
            context.update("formLovs:RetirosDialogo");
            context.execute("PF('RetirosDialogo').show()");
         }
      }
   }

//   public void asignarIndex(Retirados retirados, int dlg, int LND, int tt) {
//        RequestContext context = RequestContext.getCurrentInstance();
//      log.info("Retirados: " + retirados.toString());
//        if (tt == 2) {
//            if (LND == 0) {
//                indexRetiro = true;
//                tipoActualizacion = 0;
//            } else if (LND == 1) {
//                tipoActualizacion = 1;
//            } else if (LND == 2) {
//                tipoActualizacion = 2;
//            }
//            if (dlg == 0) {
//                //RetirosDialogo
//                motivoRetiroSeleccionado = null;
//                activarLOV = true;
//                RequestContext.getCurrentInstance().update("form:listaValores");
//                modificarInfoRegistroMotivoRetiros(lovMotivosRetiros.size());
//                //    dialogoRetiros();
//                RequestContext.getCurrentInstance().update("formLovs:RetirosDialogo");
//                RequestContext.getCurrentInstance().execute("PF('RetirosDialogo').show()");
//            }
//        }
//   }
   //LOVS
   /**
    * Actualiza la informacion de l tipo de trabajador con respecto a la tabla -
    * nuevo registro o duplicar registro
    */
   public void actualizarTipoTrabajador() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaTTSeleccionada.setTipotrabajador(tipoTrabajadorSeleccionado);
         if (!listVTTCrear.contains(vigenciaTTSeleccionada)) {
            if (listVTTModificar.isEmpty()) {
               listVTTModificar.add(vigenciaTTSeleccionada);
            } else if (!listVTTModificar.contains(vigenciaTTSeleccionada)) {
               listVTTModificar.add(vigenciaTTSeleccionada);
            }
         }

         if (cambiosPagina) {
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         guardado = false;
      } else if (tipoActualizacion == 1) {
         nuevaVigencia.setTipotrabajador(tipoTrabajadorSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVTT");
      } else if (tipoActualizacion == 2) {
         duplicarVTT.setTipotrabajador(tipoTrabajadorSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVTT");
      }
      filtradoTiposTrabajadores = null;
      tipoTrabajadorSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
      RequestContext.getCurrentInstance().update("formLovs:TipoTrabajadorDialogo");
      RequestContext.getCurrentInstance().update("formLovs:lovTipoTrabajador");
      RequestContext.getCurrentInstance().update("formLovs:aceptarTT");

      context.reset("formLovs:lovTipoTrabajador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
   }

   /**
    * Cancela los cambios en el dialogo TipoTrabajador
    */
   public void cancelarCambioTipoTrabajador() {
      filtradoTiposTrabajadores = null;
      tipoTrabajadorSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovs:lovTipoTrabajador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoTrabajador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').hide()");
   }

   //LISTA DE VALORES DINAMICA
   /**
    * Metodo que activa la lista de valores de todas las tablas con respecto al
    * index activo y la columna activa
    */
   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      //Si no hay registro seleccionado
      //if (vigenciaTTSeleccionada == null && indexPension < 0 && indexRetiro < 0) {
      if (vigenciaTTSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (vigenciaTTSeleccionada != null) {
         if (cualCelda == 1) {
            //TiposTrabajadoresDialogo
            tipoTrabajadorSeleccionado = null;
            contarRegistrosTipoTrabajador();
            RequestContext.getCurrentInstance().update("formLovs:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   /**
    * Activa el boton aceptar en la pantalla inicial y en los dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   /**
    * Metodo que exporta datos a PDF
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVTTEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "VigenciasTiposTrabajadoresPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      vigenciaTTSeleccionada = null;
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVTTEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "VigenciasTiposTrabajadoresXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      vigenciaTTSeleccionada = null;
   }

   //VISIBILIDAD PANELES RETIRADOS - PENSIONADOS
   /**
    * Cambia la visibilidad del panelRetiradosMensaje con respecto a que el
    * registro sea Extrabajador
    */
   public void cambioVisibleRetiradosMensaje() {
      TiposTrabajadores tipoTrabajadorRetirado;
      if (vigenciaTTSeleccionada != null) {
         short n1 = 1;
         tipoTrabajadorRetirado = administrarVigenciasTiposTrabajadores.tipoTrabajadorCodigo(n1);
         VigenciasTiposTrabajadores vigenciaTemporal = vigenciaTTSeleccionada;
         if (tipoTrabajadorRetirado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            estiloRetiradosMensaje = "position: absolute; left: 440px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
         } else {
            estiloRetiradosMensaje = "position: absolute; left: 440px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: visible;";
         }
         RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
      }
   }

   /**
    * Cambia la visibilidad del panelRetiradosInput con respecto a que el
    * registro sea Extrabajador, en caso verdadero carga los datos del retirado
    */
   public void cambioVisibleRetiradosInput() {
      if (vigenciaTTSeleccionada != null) {
         short n1 = 1;
         TiposTrabajadores tipoTrabajadorRetirado = administrarVigenciasTiposTrabajadores.tipoTrabajadorCodigo(n1);
         VigenciasTiposTrabajadores vigenciaTemporal = vigenciaTTSeleccionada;
//         panelRetiradosInput = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelRetiradosInput");
         if (tipoTrabajadorRetirado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            indexRetiro = true;
            cargarRetiro();
            estiloRetiradosInput = "position: absolute; left: 440px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: visible;";
         } else {
            estiloRetiradosInput = "position: absolute; left: 440px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
         }
         RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
         RequestContext context = RequestContext.getCurrentInstance();
         context.reset("form:motivoRetiro");
         context.reset("form:fechaRetiro");
         context.reset("form:observacion");
      }
   }

   /**
    * Cambia la visibilidad del panelPensionadosMensaje con respecto a que el
    * registro sea Extrabajador
    */
   public void cambioVisiblePensionadoMensaje() {
      if (vigenciaTTSeleccionada != null) {
         short n2 = 2;
         TiposTrabajadores tipoTrabajadorPensionado = administrarVigenciasTiposTrabajadores.tipoTrabajadorCodigo(n2);
         VigenciasTiposTrabajadores vigenciaTemporal = vigenciaTTSeleccionada;
         if (tipoTrabajadorPensionado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            estiloPensionadosMensaje = "position: absolute; left: 12px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; display: none; visibility: hidden;";
         } else {
            estiloPensionadosMensaje = "position: absolute; left: 12px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: visible;";
         }
         RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");
      }
   }

   /**
    * Cambia la visibilidad del panelPensionadosMensaje con respecto a que el
    * registro sea Extrabajador, en caso verdadero carga los datos del
    * pensionado
    */
   public void cambioVisiblePensionadoInput() {
      if (vigenciaTTSeleccionada != null) {
         short n2 = 2;
         TiposTrabajadores tipoTrabajadorPensionado = administrarVigenciasTiposTrabajadores.tipoTrabajadorCodigo(n2);
         VigenciasTiposTrabajadores vigenciaTemporal = vigenciaTTSeleccionada;
         if (tipoTrabajadorPensionado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            indexPension = true;
            cargarPension();
            estiloPensionadosInput = "position: absolute; left: 12px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left;  visibility: visible;";
         } else {
            estiloPensionadosInput = "position: absolute; left: 12px; top: 304px; width: 415px; height: 187px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
         }
         RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
      }
      indexPension = false;
   }

   //CARGAR RETIRO CON INDEX DEFINIDO
   /**
    * Carga los datos del retirado con respecto a la secuencia de la vigencia
    * seleccionada
    */
   public void cargarRetiro() {
      k++;
      l = BigInteger.valueOf(k);
      getLovMotivosRetiros();
      retiroVigencia = administrarVigenciasTiposTrabajadores.retiroPorSecuenciaVigencia(vigenciaTTSeleccionada.getSecuencia());
      if (retiroVigencia.getSecuencia() == null) {
         operacionRetiro = true;
         retiroVigencia = new Retirados();
         retiroVigencia.setSecuencia(l);
         retiroVigencia.setMotivoretiro(new MotivosRetiros());
      }
   }

   //CARGAR PENSION CON INDEX DEFINIDO
   /**
    * Carga los datos del pensionado con respecto a la secuencia de la vigencia
    * seleccionada
    */
   public void cargarPension() {
      k++;
      l = BigInteger.valueOf(k);
      pensionVigencia = administrarVigenciasTiposTrabajadores.pensionPorSecuenciaVigencia(vigenciaTTSeleccionada.getSecuencia());
      if (pensionVigencia.getSecuencia() == null) {
         operacionPension = true;
         pensionVigencia = new Pensionados();
         pensionVigencia.setClase(new ClasesPensiones());
         pensionVigencia.setCausabiente(new Empleados());
         pensionVigencia.getCausabiente().setPersona(new Personas());
         pensionVigencia.setTipopensionado(new TiposPensionados());
         pensionVigencia.setTutor(new Personas());
         pensionVigencia.setSecuencia(l);

      }
   }

   //GUARDADO RETIROS
   /**
    * Guarda los datos efectuados en el panel retirados
    */
   public void guardarDatosRetiros() {
      log.info("ENTRO guardarDatosRetiros");
      if (retiroVigencia.getFecharetiro() != null) {
         log.info("ControlVigenciaTipoTrabajador.guardarDatosPensiones:");
         log.info("Secuencia: " + retiroVigencia.getSecuencia());
         retiroVigencia.setVigenciatipotrabajador(vigenciaTTSeleccionada);

         k++;
         k = retiroVigencia.getSecuencia().intValue();
         log.info("K: " + k);
         l = BigInteger.valueOf(k);
         log.info("L: " + l);
         retiroVigencia.setSecuencia(l);
         log.info("pensionVigencia.Secuencia: " + retiroVigencia.getSecuencia());
         if (operacionRetiro == false) {
            if (banderaLimpiarRetiro == true) {
               administrarVigenciasTiposTrabajadores.borrarRetirado(retiroCopia);
            } else {
               log.info("editar");
               log.info("Secuencia: " + retiroVigencia.getSecuencia());
               administrarVigenciasTiposTrabajadores.editarRetirado(retiroVigencia);
            }
         } else if (banderaLimpiarRetiro == false) {
            if (retiroVigencia.getMotivoretiro().getSecuencia() == null) {
               retiroVigencia.setMotivoretiro(null);
            }
            administrarVigenciasTiposTrabajadores.crearRetirado(retiroVigencia);
         }
         FacesContext c = FacesContext.getCurrentInstance();
         if (banderaLimpiarRetiro == true) {
            estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; font-size: 10px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
            estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; font-size: 10px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible;";
            estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; font-size: 10px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
            estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; font-size: 10px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
         } else {
            estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
            estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
            estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
            estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
         }
         log.error("---------------------DATOS GUARDADOS---------------------------------");
         log.info("Fecha Retiro: " + retiroVigencia.getFecharetiro());
         log.info("Motivo de retiro: " + retiroVigencia.getMotivoretiro().getNombre());
         log.info("Descripcion: " + retiroVigencia.getDescripcion());
         log.error("---------------------------------------------------------------------");

         cambioRetiros = false;
         retiroVigencia = new Retirados();
         retiroVigencia.setMotivoretiro(new MotivosRetiros());
         banderaLimpiarRetiro = false;

         operacionRetiro = false;

         //  cargarRetiro();
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Retirados con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");

         RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
         RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
         RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
         RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");

         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         log.info("SALIO GUARDARDATOSRETIROS");

      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegRetiro').show()");
         log.info("SALIO CON ERROR");
      }
   }

   /**
    * Cancela los cambios efectuados en el panel de retirados
    */
   public void cancelarDatosRetiros() {
      cambioRetiros = false;
      indexRetiro = false;
      // limpiarRetiro();
      retiroVigencia = new Retirados();
      retiroVigencia.setMotivoretiro(new MotivosRetiros());
      FacesContext c = FacesContext.getCurrentInstance();
      estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
      estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
      estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
      estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";

      RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
      RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
      RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
      RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");
   }

   /**
    * Efectua el guardado de los datos cambiados en el panel pensiones
    */
   public void guardarDatosPensiones() {
      log.info("ENTRO guardarDatosPensiones");
      if (pensionVigencia.getFechainiciopension() != null && pensionVigencia.getClase().getSecuencia() != null) {
         log.info("ControlVigenciaTipoTrabajador.guardarDatosPensiones:");
         log.info("Secuencia: " + pensionVigencia.getSecuencia());
         pensionVigencia.setVigenciatipotrabajador(vigenciaTTSeleccionada);

         k++;
         k = pensionVigencia.getSecuencia().intValue();
         log.info("K: " + k);
         l = BigInteger.valueOf(k);
         log.info("L: " + l);
         pensionVigencia.setSecuencia(l);
         log.info("pensionVigencia.Secuencia: " + pensionVigencia.getSecuencia());
         if (operacionPension == false) {
            if (banderaLimpiarPension == true) {
               administrarVigenciasTiposTrabajadores.borrarPensionado(pensionCopia);
            } else {
               log.info("editar");
               log.info("Secuencia: " + pensionVigencia.getSecuencia());
               administrarVigenciasTiposTrabajadores.editarPensionado(pensionVigencia);
            }
         } else if (banderaLimpiarPension == false) {
            if (pensionVigencia.getCausabiente().getSecuencia() == null) {
               pensionVigencia.setCausabiente(null);
            }
            if (pensionVigencia.getTipopensionado().getSecuencia() == null) {
               pensionVigencia.setTipopensionado(null);
            }
            if (pensionVigencia.getTutor().getSecuencia() == null) {
               pensionVigencia.setTutor(null);
            }
            if (pensionVigencia.getClase().getSecuencia() == null) {
               pensionVigencia.setClase(null);
            }
            administrarVigenciasTiposTrabajadores.crearPensionado(pensionVigencia);
         }
         FacesContext c = FacesContext.getCurrentInstance();
         if (banderaLimpiarPension == true) {
            estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; font-size: 10px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
            estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; font-size: 10px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible;";
            estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; font-size: 10px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
            estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; font-size: 10px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
         } else {
            estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
            estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
            estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
            estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
         }
         cambioPension = false;
         pensionVigencia = new Pensionados();
         pensionVigencia.setClase(new ClasesPensiones());
         pensionVigencia.setCausabiente(new Empleados());
         pensionVigencia.getCausabiente().setPersona(new Personas());
         pensionVigencia.setTipopensionado(new TiposPensionados());
         pensionVigencia.setTutor(new Personas());
         banderaLimpiarPension = false;

         operacionPension = false;

         RequestContext context = RequestContext.getCurrentInstance();

         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Pensionados con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");

         RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
         RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
         RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
         RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");

         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         log.info("SALIO");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegPensionado').show()");
         log.info("SALIO CON ERROR");
      }
   }

   /**
    * Cancela los datos efectuados en el panel pension
    */
   public void cancelarDatosPension() {
      cambioPension = false;
      indexPension = false;
      //LimpiarPension();
      pensionVigencia = new Pensionados();
      pensionVigencia.setClase(new ClasesPensiones());
      pensionVigencia.setCausabiente(new Empleados());
      pensionVigencia.getCausabiente().setPersona(new Personas());
      pensionVigencia.setTipopensionado(new TiposPensionados());
      pensionVigencia.setTutor(new Personas());
      FacesContext c = FacesContext.getCurrentInstance();
      estiloRetiradosInput = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
      estiloPensionadosMensaje = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
      estiloPensionadosInput = "position: absolute; left: 12px; top: 310px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;";
      estiloRetiradosMensaje = "position: absolute; left: 440px; top: 310px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible";
      RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
      RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
      RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
      RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");
   }

   /**
    * Dispara el dialogo de MotivosRetiros
    */
   public void dialogoRetiros() {
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistrosMotivosRetiros();
      log.info("dialogoRetiros.Motivosretiros: " + lovMotivosRetiros);
      context.reset("formLovs:RetirosDialogo");
      RequestContext.getCurrentInstance().execute("PF('RetirosDialogo').show()");
   }

   /**
    * Actualiza la selecciion del motivo retiro
    */
   public void actualizarMotivoRetiro() {
      banderaCambiosRetirados();
      log.error("Estoy en actualizarMotivoRetiro");
      log.info("retiroVigencia: " + retiroVigencia);
      retiroVigencia.setMotivoretiro(motivoRetiroSeleccionado);
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
      aceptar = true;
      filtradoMotivosRetiros = null;
      motivoRetiroSeleccionado = null;
      vigenciaTTSeleccionada = null;
      /*
         *
       */
      getLovMotivosRetiros();

//        RequestContext.getCurrentInstance().update("form:RetirosDialogo");
//        RequestContext.getCurrentInstance().update("form:lovMotivosRetiros");
//        RequestContext.getCurrentInstance().update("form:aceptarMR");
      context.reset("formLovs:lovMotivosRetiros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivosRetiros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('RetirosDialogo').hide()");
   }

   /**
    * Cancela la seleccion del motivo retiro
    */
   public void cancelarMotivoRetiro() {
      log.info("cancelarMotivoRetiro.Motivosretiros: " + lovMotivosRetiros);
      motivoRetiroSeleccionado = null;
      filtradoMotivosRetiros = null;
      aceptar = true;
      vigenciaTTSeleccionada = null;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovs:lovMotivosRetiros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovMotivosRetiros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('RetirosDialogo').hide()");
   }

   //////////////////////////////////////
   //Pendiente
   /**
    * Dispara el dialogo de ClasePension
    */
   public void dialogoClasePension() {
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistrosClasePension();
      context.reset("formLovs:clasePension");
      RequestContext.getCurrentInstance().execute("PF('clasePensionDialogo').show()");
   }

   /**
    * Actualiza la selecciion del Clase Pension
    */
   public void actualizarClasePension() {
      banderaCambiosPensionados();
      pensionVigencia.setClase(clasesPensionesSeleccionada);
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
      aceptar = true;
      clasesPensionesFiltrado = null;
      clasesPensionesSeleccionada = null;
      getLovclasesPensiones();

      RequestContext.getCurrentInstance().update("formLovs:clasePensionDialogo");
      RequestContext.getCurrentInstance().update("formLovs:lovClasePension");
      RequestContext.getCurrentInstance().update("formLovs:aceptarCP");

      context.reset("formLovs:lovClasePension:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovClasePension').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('clasePensionDialogo').hide()");
   }

   /**
    * Cancela la seleccion del Clase Pension
    */
   public void cancelarClasePension() {
      clasesPensionesSeleccionada = null;
      clasesPensionesFiltrado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovs:lovClasePension:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovClasePension').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('clasePensionDialogo').hide()");
   }
   //////////////////////////////////////

   //////////////////////////////////////
   //Pendiente
   /**
    * Dispara el dialogo de TipoPensionado
    */
   public void dialogoTipoPensionado() {
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistrosTipoPension();
      context.reset("formLovs:tipoPensionado");
      RequestContext.getCurrentInstance().execute("PF('tipoPensionadoDialogo').show()");
   }

   /**
    * Actualiza la selecciion del TipoPensionado
    */
   public void actualizarTipoPensionado() {
      banderaCambiosPensionados();
      pensionVigencia.setTipopensionado(tiposPensionadosSeleccionada);
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
      aceptar = true;
      tiposPensionadosSeleccionada = null;
      tiposPensionadosFiltrado = null;
      getLovTiposPensionados();
      /*
         * RequestContext.getCurrentInstance().update("form:tipoPensionadoDialogo");
         * RequestContext.getCurrentInstance().update("form:lovTipoPensionado");
         * RequestContext.getCurrentInstance().update("form:aceptarTP");
       */
      context.reset("formLovs:lovTipoPensionado:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoPensionado').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tipoPensionadoDialogo').hide()");
   }

   /**
    * Cancela la seleccion del TipoPensionado
    */
   public void cancelarTipoPensionado() {
      tiposPensionadosSeleccionada = null;
      tiposPensionadosFiltrado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovs:lovTipoPensionado:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTipoPensionado').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tipoPensionadoDialogo').hide()");
   }
   //////////////////////////////////////
   //////////////////////////////////////
   //Pendiente

   /**
    * Dispara el dialogo de CausaBiente
    */
   public void dialogoCausaBiente() {
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistrosEmpleado();
      context.reset("formLovs:causaBiente");
      RequestContext.getCurrentInstance().execute("PF('causaBientesDialogo').show()");
   }

   /**
    * Actualiza la selecciion del CausaBiente
    */
   public void actualizarCausaBiente() {
      banderaCambiosPensionados();
      pensionVigencia.setCausabiente(pensionadoSeleccionado.getCausabiente());
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
      aceptar = true;
      pensionadoSeleccionado = null;
      pensionadosFiltrado = null;
      getLovPensionados();
      /*
         * RequestContext.getCurrentInstance().update("form:causaBientesDialogo");
         * RequestContext.getCurrentInstance().update("form:lovCausaBientes");
         * RequestContext.getCurrentInstance().update("form:aceptarCB");
       */
      context.reset("formLovs:lovCausaBientes:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCausaBientes').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('causaBientesDialogo').hide()");
   }

   /**
    * Cancela la seleccion del CausaBiente
    */
   public void cancelarCausaBiente() {
      pensionadoSeleccionado = null;
      pensionadosFiltrado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovs:lovCausaBientes:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCausaBientes').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('causaBientesDialogo').hide()");
   }
   //////////////////////////////////////
   //////////////////////////////////////
   //Pendiente

   /**
    * Dispara el dialogo de Tutor
    */
   public void dialogoTutor() {
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistrosPersona();
      context.reset("formLovs:tutorPension");
      RequestContext.getCurrentInstance().execute("PF('tutorDialogo').show()");
   }

   /**
    * Actualiza la selecciion del Tutor
    */
   public void actualizarTutor() {
      banderaCambiosPensionados();
      pensionVigencia.setTutor(personaSeleccionada);
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
      aceptar = true;
      personasFiltrado = null;
      personaSeleccionada = null;
      getLovPersonas();
      /*
         * RequestContext.getCurrentInstance().update("form:tutorDialogo"); RequestContext.getCurrentInstance().update("form:lovTutor");
         * RequestContext.getCurrentInstance().update("form:aceptarT");
       */
      context.reset("formLovs:lovTutor:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTutor').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tutorDialogo').hide()");
   }

   /**
    * Cancela la seleccion del Tutor
    */
   public void cancelarTutor() {
      personaSeleccionada = null;
      personasFiltrado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovs:lovTutor:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTutor').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tutorDialogo').hide()");
   }
   //////////////////////////////////////

   /**
    * Limpia la informacion del panel retirado
    */
   public void limpiarRetiro() {
      banderaLimpiarRetiro = true;
      //se efecto un cambio en --> se adiciono retiroVigencia.getMotivoretiro() !=null, retiroVigencia.getDescripcion() !=null
      // en caso de fallas eliminar nuevo cambio
      if (retiroVigencia.getFecharetiro() != null || retiroVigencia.getMotivoretiro() != null || retiroVigencia.getDescripcion() != null) {
         banderaCambiosRetirados();
      }
      retiroCopia = retiroVigencia;
      retiroVigencia = new Retirados();
      retiroVigencia.setMotivoretiro(new MotivosRetiros());
      retiroVigencia.setSecuencia(retiroCopia.getSecuencia());
      retiroVigencia.setVigenciatipotrabajador(vigenciaTTSeleccionada);
      if (operacionRetiro == false) {
         if (banderaLimpiarRetiro == true) {
            administrarVigenciasTiposTrabajadores.borrarRetirado(retiroCopia);
         } else {
            administrarVigenciasTiposTrabajadores.editarRetirado(retiroVigencia);
         }
      }
      cargarRetiro();
      banderaLimpiarRetiro = false;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:observacion");
      RequestContext.getCurrentInstance().update("form:motivoRetiro");
      RequestContext.getCurrentInstance().update("form:fechaRetiro");
   }

   /**
    * Limpia la informacion del panel pensionado
    */
   public void limpiarPension() {
      banderaLimpiarPension = true;
      if (pensionVigencia.getFechainiciopension() != null || pensionVigencia.getFechafinalpension() != null
              || pensionVigencia.getPorcentaje() != null || pensionVigencia.getClase() != null
              || pensionVigencia.getTipopensionado() != null || pensionVigencia.getResolucionpension() != null
              || pensionVigencia.getTutor() != null || pensionVigencia.getCausabiente() != null) {
         banderaCambiosPensionados();
      }
      pensionCopia = pensionVigencia;
      pensionVigencia = new Pensionados();
      pensionVigencia.setCausabiente(new Empleados());
      pensionVigencia.getCausabiente().setPersona(new Personas());
      pensionVigencia.setClase(new ClasesPensiones());
      pensionVigencia.setTipopensionado(new TiposPensionados());
      pensionVigencia.setTutor(new Personas());
      pensionVigencia.setSecuencia(pensionCopia.getSecuencia());
      pensionVigencia.setVigenciatipotrabajador(vigenciaTTSeleccionada);
      if (operacionPension == false) {
         if (banderaLimpiarPension == true) {
            administrarVigenciasTiposTrabajadores.borrarPensionado(pensionCopia);
         } else {
            administrarVigenciasTiposTrabajadores.editarPensionado(pensionVigencia);
         }
      } else if (banderaLimpiarPension == false) {
         administrarVigenciasTiposTrabajadores.crearPensionado(pensionVigencia);
      }
      cargarPension();
      banderaLimpiarPension = false;
      RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
   }

   /**
    * Metodo que activa una bandera que se activa si se realiza un cambio en el
    * panel retiros
    */
   public void banderaCambiosRetirados() {
      if (cambioRetiros == false) {
         log.info("ControlVigenciaTipoTrabajador.banderaCambiosRetirados()");
         log.info("Secuencia: " + retiroVigencia.getSecuencia());
         cambioRetiros = true;
         banderaEliminarRetiro = false;
      }
      cambiosPagina = false;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      log.info("paso por banderaCambiosRetirados() y salio Bien");
   }

   /**
    * Metodo que activa una bandera que se activa si se realiza un cambio en el
    * panel pensionado
    */
   public void banderaCambiosPensionados() {
      if (cambioPension == false) {
         log.info("ControlVigenciaTipoTrabajador.banderaCambiosPensionados()");
         log.info("Secuencia: " + pensionVigencia.getSecuencia());
         cambioPension = true;
         banderaEliminarPension = false;
      }
      cambiosPagina = false;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   /**
    * Metodo que valida que el registro extrabajador se encuentra almacenado en
    * la base de datos, en caso contrario muestra un dialogo para que lo
    * almacene o no
    */
   public void validarRegistroSeleccionadoRetirados() {
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:motivoRetiro");
      context.reset("form:fechaRetiro");
      context.reset("form:observacion");
      boolean banderaValidacion = true;
      List<VigenciasTiposTrabajadores> listaVigenciaTiposTrabajadoresReal = administrarVigenciasTiposTrabajadores.vigenciasTiposTrabajadoresEmpleado(empleado.getSecuencia());
      VigenciasTiposTrabajadores vigenciaSeleccionadaRetirados = vigenciaTTSeleccionada;
      int tamanoTabla = listaVigenciaTiposTrabajadoresReal.size();
      int i = 0;
      while (banderaValidacion && (i < tamanoTabla)) {
         if (listaVigenciaTiposTrabajadoresReal.get(i).getSecuencia() == vigenciaSeleccionadaRetirados.getSecuencia()) {
            if (listaVigenciaTiposTrabajadoresReal.get(i).getTipotrabajador().getNombre().equalsIgnoreCase(
                    vigenciaSeleccionadaRetirados.getTipotrabajador().getNombre())) {
               banderaValidacion = false;
               almacenarRetirado = true;
            }
         }
         i++;
      }
      if (banderaValidacion == true && guardado == false) {
         //Dialogo de almacenar el nuevo registro de retiro antes de realizar operaciones
         RequestContext.getCurrentInstance().update("formularioDialogos:guardarNuevoRegistroRetiro");
         RequestContext.getCurrentInstance().execute("PF('guardarNuevoRegistroRetiro').show()");
      }
   }

   /**
    * Metodo que valida que el registro pensionado se encuentra almacenado en la
    * base de datos, en caso contrario muestra un dialogo para que lo almacene o
    * no
    */
   public void validarRegistroSeleccionadoPensionado() {
      log.info("ENTRO validarRegistroSeleccionadoPensionado");
      log.info("Secuencia: " + pensionVigencia.getSecuencia());
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:fechaPensionInicio");
      context.reset("form:fechaPensionFinal");
      context.reset("form:porcentajePension");
      context.reset("form:clasePension");
      context.reset("form:tipoPensionado");
      context.reset("form:resolucionPension");
      context.reset("form:causaBiente");
      context.reset("form:tutorPension");
      boolean banderaValidacion = true;
      List<VigenciasTiposTrabajadores> listaVigenciaTiposTrabajadoresReal;
      listaVigenciaTiposTrabajadoresReal = administrarVigenciasTiposTrabajadores.vigenciasTiposTrabajadoresEmpleado(empleado.getSecuencia());
      VigenciasTiposTrabajadores vigenciaSeleccionadaPension = vigenciaTTSeleccionada;
      int tamanoTabla = listaVigenciaTiposTrabajadoresReal.size();
      int i = 0;
      while (banderaValidacion && (i < tamanoTabla)) {
         if (listaVigenciaTiposTrabajadoresReal.get(i).getSecuencia() == vigenciaSeleccionadaPension.getSecuencia()) {
            if (listaVigenciaTiposTrabajadoresReal.get(i).getTipotrabajador().getNombre().equalsIgnoreCase(vigenciaSeleccionadaPension.getTipotrabajador().getNombre())) {
               banderaValidacion = false;
               almacenarPensionado = true;
            }
         }
         i++;
      }
      if (banderaValidacion == true && guardado == false) {
         //Dialogo de almacenar el nuevo registro de retiro antes de realizar operaciones
         /*
             * RequestContext.getCurrentInstance().update("formularioDialogos:guardarNuevoRegistroPensionado");
             * RequestContext.getCurrentInstance().execute("PF('guardarNuevoRegistroPensionado').show()");
          */
      }
      log.info("SALIO validarRegistroSeleccionadoPensionado");
   }

   /**
    * Guardar el nuevo registro de extrabajador
    */
   public void guardarNuevoRegistroRetiro() {
      guardarCambiosVTT();
      almacenarRetirado = true;
   }

   /**
    * Guardar el nuevo registro de pensionado
    */
   public void guardarNuevoRegistroPension() {
      log.info("ENTRO guardarNuevoRegistroPension()");
      guardarCambiosVTT();
      almacenarPensionado = true;
   }

   public void guardarYSalir() {
      guardarGeneral();
      salir();
   }

   /**
    * Guardado general de la pagina
    */
   public void guardarGeneral() {
      log.info("ENTRO GUARDADO GENERAL");
      if (cambiosPagina == false) {
         if ((almacenarRetirado == true) && (banderaEliminarRetiro == false)) {
            log.info("ENTRO IF ALMACENAR RETIRADOS");
            guardarDatosRetiros();
         }

         if ((almacenarPensionado == true) && (banderaEliminarPension == false)) {
            log.info("ENTRO IF ALMACENAR PENSIONADO");
            guardarDatosPensiones();
         }

         if (guardado == false) {
            guardarCambiosVTT();
         }
      }
      log.info("SALIO guardarGeneral");
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (vigenciaTTSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(vigenciaTTSeleccionada.getSecuencia(), "VIGENCIASTIPOSTRABAJADORES");
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
      } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASTIPOSTRABAJADORES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista reala a la filtrada
    */
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosTipoTrabajador() {
      RequestContext.getCurrentInstance().update("formLovs:infoRegistroTipoTrabajador");
   }

   public void contarRegistrosClasePension() {
      RequestContext.getCurrentInstance().update("formLovs:infoRegistroClasePension");
   }

   public void contarRegistrosTipoPension() {
      RequestContext.getCurrentInstance().update("formLovs:infoRegistroClasePension");
   }

   public void contarRegistrosEmpleado() {
      RequestContext.getCurrentInstance().update("formLovs:infoRegistroEmpleado");
   }

   public void contarRegistrosPersona() {
      RequestContext.getCurrentInstance().update("formLovs:infoRegistroPersona");
   }

   public void contarRegistrosMotivosRetiros() {
      RequestContext.getCurrentInstance().update("formLovs:infoRegistroMotivoRetiros");
   }

   public void recordarSeleccion() {
      if (vigenciaTTSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVTTEmpleado");
         tablaC.setSelection(vigenciaTTSeleccionada);
      }
      //log.info("vigenciaTTSeleccionada: " + vigenciaTTSeleccionada);
   }

   public void anularLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //**************************GETTER & SETTER**********************************************************************************************************************************************//
   public List<VigenciasTiposTrabajadores> getListaVigenciasTT() {
      if (listaVigenciasTT == null) {
         listaVigenciasTT = administrarVigenciasTiposTrabajadores.vigenciasTiposTrabajadoresEmpleado(empleado.getSecuencia());
      }
      return listaVigenciasTT;
   }

   public void setListaVigenciasTT(List<VigenciasTiposTrabajadores> listaVigenciasTT) {
      this.listaVigenciasTT = listaVigenciasTT;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public List<VigenciasTiposTrabajadores> getFiltrarVTT() {
      return filtrarVTT;
   }

   public void setFiltrarVTT(List<VigenciasTiposTrabajadores> filtrarVTT) {
      this.filtrarVTT = filtrarVTT;
   }

   public VigenciasTiposTrabajadores getNuevaVigencia() {
      return nuevaVigencia;
   }

   public void setNuevaVigencia(VigenciasTiposTrabajadores nuevaVigencia) {
      this.nuevaVigencia = nuevaVigencia;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public List<TiposTrabajadores> getLovTiposTrabajadores() {
      if (lovTiposTrabajadores == null) {
         lovTiposTrabajadores = administrarVigenciasTiposTrabajadores.tiposTrabajadores();
      }
      return lovTiposTrabajadores;
   }

   public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadores) {
      this.lovTiposTrabajadores = lovTiposTrabajadores;
   }

   public List<TiposTrabajadores> getFiltradoTiposTrabajadores() {
      return filtradoTiposTrabajadores;
   }

   public void setFiltradoTiposTrabajadores(List<TiposTrabajadores> filtradoTiposTrabajadores) {
      this.filtradoTiposTrabajadores = filtradoTiposTrabajadores;
   }

   public VigenciasTiposTrabajadores getEditarVTT() {
      return editarVTT;
   }

   public void setEditarVTT(VigenciasTiposTrabajadores editarVTT) {
      this.editarVTT = editarVTT;
   }

   public VigenciasTiposTrabajadores getDuplicarVTT() {
      return duplicarVTT;
   }

   public void setDuplicarVTT(VigenciasTiposTrabajadores duplicarVTT) {
      this.duplicarVTT = duplicarVTT;
   }

   public TiposTrabajadores getTipoTrabajadorSeleccionado() {
      return tipoTrabajadorSeleccionado;
   }

   public void setTipoTrabajadorSeleccionado(TiposTrabajadores tipoTrabajadorSelecionado) {
      this.tipoTrabajadorSeleccionado = tipoTrabajadorSelecionado;
   }

   public Retirados getRetiroVigencia() {
      return retiroVigencia;
   }

   public void setRetiroVigencia(Retirados retiroVigencia) {
      this.retiroVigencia = retiroVigencia;
   }

   public List<MotivosRetiros> getLovMotivosRetiros() {
      if (lovMotivosRetiros == null) {
         lovMotivosRetiros = administrarVigenciasTiposTrabajadores.motivosRetiros();
      }
      return lovMotivosRetiros;
   }

   public void setLovMotivosRetiros(List<MotivosRetiros> lovMotivosRetiros) {
      this.lovMotivosRetiros = lovMotivosRetiros;
   }

   public MotivosRetiros getMotivoRetiroSeleccionado() {
      return motivoRetiroSeleccionado;
   }

   public void setMotivoRetiroSeleccionado(MotivosRetiros motivoRetiroSeleccionado) {
      this.motivoRetiroSeleccionado = motivoRetiroSeleccionado;
   }

   public List<Pensionados> getLovPensionados() {
      if (lovPensionados == null) {
         lovPensionados = administrarVigenciasTiposTrabajadores.listaPensionados();
      }
      return lovPensionados;
   }

   public void setLovPensionados(List<Pensionados> lovPensionados) {
      this.lovPensionados = lovPensionados;
   }

   public Pensionados getPensionVigencia() {
      return pensionVigencia;
   }

   public void setPensionVigencia(Pensionados pensionVigencia) {
      this.pensionVigencia = pensionVigencia;
   }

   public List<Personas> getLovPersonas() {
      if (lovPersonas == null) {
         lovPersonas = administrarVigenciasTiposTrabajadores.listaPersonas();
      }
      return lovPersonas;
   }

   public void setLovPersonas(List<Personas> lovPersonas) {
      this.lovPersonas = lovPersonas;
   }

   public Personas getPersonaSeleccionada() {
      return personaSeleccionada;
   }

   public void setPersonaSeleccionada(Personas personaSeleccionada) {
      this.personaSeleccionada = personaSeleccionada;
   }

   public List<ClasesPensiones> getLovclasesPensiones() {
      if (lovclasesPensiones == null) {
         lovclasesPensiones = administrarVigenciasTiposTrabajadores.clasesPensiones();
      }
      return lovclasesPensiones;
   }

   public void setLovclasesPensiones(List<ClasesPensiones> lovclasesPensiones) {
      this.lovclasesPensiones = lovclasesPensiones;
   }

   public ClasesPensiones getClasesPensionesSeleccionada() {
      return clasesPensionesSeleccionada;
   }

   public void setClasesPensionesSeleccionada(ClasesPensiones clasesPensionesSeleccionada) {
      this.clasesPensionesSeleccionada = clasesPensionesSeleccionada;
   }

   public List<TiposPensionados> getLovTiposPensionados() {
      if (lovTiposPensionados == null) {
         lovTiposPensionados = administrarVigenciasTiposTrabajadores.tiposPensionados();
      }
      return lovTiposPensionados;
   }

   public void setLovTiposPensionados(List<TiposPensionados> lovTiposPensionados) {
      this.lovTiposPensionados = lovTiposPensionados;
   }

   public TiposPensionados getTiposPensionadosSeleccionada() {
      return tiposPensionadosSeleccionada;
   }

   public void setTiposPensionadosSeleccionada(TiposPensionados tiposPensionadosSeleccionada) {
      this.tiposPensionadosSeleccionada = tiposPensionadosSeleccionada;
   }

   public Pensionados getPensionadoSeleccionado() {
      return pensionadoSeleccionado;
   }

   public void setPensionadoSeleccionado(Pensionados pensionadoSeleccionado) {
      this.pensionadoSeleccionado = pensionadoSeleccionado;
   }

   public List<ClasesPensiones> getClasesPensionesFiltrado() {
      return clasesPensionesFiltrado;
   }

   public void setClasesPensionesFiltrado(List<ClasesPensiones> clasesPensionesFiltrado) {
      this.clasesPensionesFiltrado = clasesPensionesFiltrado;
   }

   public List<TiposPensionados> getTiposPensionadosFiltrado() {
      return tiposPensionadosFiltrado;
   }

   public void setTiposPensionadosFiltrado(List<TiposPensionados> tiposPensionesFiltrado) {
      this.tiposPensionadosFiltrado = tiposPensionesFiltrado;
   }

   public List<Pensionados> getPensionadosFiltrado() {
      return pensionadosFiltrado;
   }

   public void setPensionadosFiltrado(List<Pensionados> pensionadosFiltrado) {
      this.pensionadosFiltrado = pensionadosFiltrado;
   }

   public List<Personas> getPersonasFiltrado() {
      return personasFiltrado;
   }

   public void setPersonasFiltrado(List<Personas> personasFiltrado) {
      this.personasFiltrado = personasFiltrado;
   }

   public VigenciasTiposTrabajadores getVigenciaTTSeleccionada() {
      return vigenciaTTSeleccionada;
   }

   public void setVigenciaTTSeleccionada(VigenciasTiposTrabajadores vigenciaTTSeleccionada) {
      this.vigenciaTTSeleccionada = vigenciaTTSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public List<MotivosRetiros> getFiltradoMotivosRetiros() {
      return filtradoMotivosRetiros;
   }

   public void setFiltradoMotivosRetiros(List<MotivosRetiros> filtradoMotivosRetiros) {
      this.filtradoMotivosRetiros = filtradoMotivosRetiros;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVTTEmpleado");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public String getInfoRegistroTipoTrabajador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovs:lovTipoTrabajador");
      infoRegistroTipoTrabajador = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoTrabajador;
   }

   public String getInfoRegistroMotivoRetiros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovs:lovMotivosRetiros");
      infoRegistroMotivoRetiros = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivoRetiros;
   }

   public String getInfoRegistroClasePension() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovs:lovClasePension");
      infoRegistroClasePension = String.valueOf(tabla.getRowCount());
      return infoRegistroClasePension;
   }

   public String getInfoRegistroTipoPension() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovs:lovTipoPensionado");
      infoRegistroTipoPension = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoPension;
   }

   public String getInfoRegistroEmpleado() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovs:lovCausaBientes");
      infoRegistroEmpleado = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleado;
   }

   public String getInfoRegistroPersona() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovs:lovTutor");
      infoRegistroPersona = String.valueOf(tabla.getRowCount());
      return infoRegistroPersona;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

   public String getEstiloRetiradosMensaje() {
      return estiloRetiradosMensaje;
   }

   public void setEstiloRetiradosMensaje(String estiloRetiradosMensaje) {
      this.estiloRetiradosMensaje = estiloRetiradosMensaje;
   }

   public String getEstiloRetiradosInput() {
      return estiloRetiradosInput;
   }

   public void setEstiloRetiradosInput(String estiloRetiradosInput) {
      this.estiloRetiradosInput = estiloRetiradosInput;
   }

   public String getEstiloPensionadosMensaje() {
      return estiloPensionadosMensaje;
   }

   public void setEstiloPensionadosMensaje(String estiloPensionadosMensaje) {
      this.estiloPensionadosMensaje = estiloPensionadosMensaje;
   }

   public String getEstiloPensionadosInput() {
      return estiloPensionadosInput;
   }

   public void setEstiloPensionadosInput(String estiloPensionadosInput) {
      this.estiloPensionadosInput = estiloPensionadosInput;
   }

}
