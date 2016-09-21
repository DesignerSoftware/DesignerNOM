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
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.component.panel.Panel;
import org.primefaces.context.RequestContext;

/**
 *
 * @author AndresPineda
 */
@ManagedBean
@SessionScoped
public class ControlVigenciaTipoTrabajador implements Serializable {

   @EJB
   AdministrarVigenciasTiposTrabajadoresInterface administrarVigenciasTiposTrabajadores;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //Vigencias Tipos Trabajadores
   private List<VigenciasTiposTrabajadores> vigenciasTiposTrabajadores;
   private List<VigenciasTiposTrabajadores> filtrarVTT;
   private VigenciasTiposTrabajadores vigenciaSeleccionada;
   //Tipos Trabajadores
   private List<TiposTrabajadores> listaTiposTrabajadores;
   private TiposTrabajadores tipoTrabajadorSeleccionado;
   private List<TiposTrabajadores> filtradoTiposTrabajadores;
   private Empleados empleado;
   private int tipoActualizacion;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla VC
   private Column vttFecha, vttTipoTrabajador, vttTipoCotizante;
   //Paneles Retirados - Pensionados
   private Panel panelRetiradosMensaje, panelRetiradosInput, panelPensionadosMensaje, panelPensionadosInput;
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
   private List<MotivosRetiros> motivosRetiros;
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
   private List<Pensionados> listaPensionados;
   private Pensionados pensionVigencia;
   private Pensionados pensionadoSeleccionado;
   private List<Personas> listaPersonas;
   private Personas personaSeleccionada;
   private List<ClasesPensiones> clasesPensiones;
   private ClasesPensiones clasesPensionesSeleccionada;
   private List<TiposPensionados> tiposPensionados;
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
   public String paginaAnterior;
   //
   private boolean cambiosPagina;
   //
   private String mensajeValidacion;
   //
   private DataTable tablaC;
   //
   private boolean activarLOV;

   /**
    * Constructo del Controlador
    */
   public ControlVigenciaTipoTrabajador() {

//        listaPensionados = new ArrayList<Pensionados>();
      listaPensionados = null;
      pensionadoSeleccionado = new Pensionados();
      //
      pensionVigencia = new Pensionados();
      pensionVigencia.setClase(new ClasesPensiones());
      pensionVigencia.setCausabiente(new Empleados());
      pensionVigencia.setTipopensionado(new TiposPensionados());
      pensionVigencia.setTutor(new Personas());
      //
      //listaPersonas = new ArrayList<Personas>();
      listaPersonas = null;
      personaSeleccionada = new Personas();
      //
      //clasesPensiones = new ArrayList<ClasesPensiones>();
      clasesPensiones = null;
      clasesPensionesSeleccionada = new ClasesPensiones();
      //
      //tiposPensionados = new ArrayList<TiposPensionados>();
      tiposPensionados = null;
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
      motivosRetiros = null;
      motivoRetiroSeleccionado = new MotivosRetiros();
      //
      vigenciasTiposTrabajadores = null;
      //
      //listaTiposTrabajadores = new ArrayList<TiposTrabajadores>();
      listaTiposTrabajadores = null;
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
      vigenciaSeleccionada = null;
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
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarVigenciasTiposTrabajadores.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
   }

   public String redirigir() {
      return paginaAnterior;
   }

   //EMPLEADO DE LA VIGENCIA
   /**
    * Metodo que recibe la secuencia empleado desde la pagina anterior y obtiene
    * el empleado referenciado
    *
    * @param sec Secuencia del Empleado
    */
   public void recibirEmpleado(Empleados empl) {
      vigenciasTiposTrabajadores = null;
      empleado = empl;
      getVigenciasTiposTrabajadores();
      if (vigenciasTiposTrabajadores != null) {
         vigenciaSeleccionada = vigenciasTiposTrabajadores.get(0);
      }
   }

   public void modificarVTT(VigenciasTiposTrabajadores vtt) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listVTTCrear.contains(vigenciaSeleccionada)) {
         if (listVTTModificar.isEmpty()) {
            listVTTModificar.add(vigenciaSeleccionada);
         } else if (!listVTTModificar.contains(vigenciaSeleccionada)) {
            listVTTModificar.add(vigenciaSeleccionada);
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
         auxiliar = vigenciaSeleccionada;
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
      vigenciaSeleccionada = vtt;
      if (vigenciaSeleccionada.getFechavigencia() != null) {
         boolean retorno = false;
         retorno = validarFechasRegistroVTT(0);
         if (retorno == true) {
            cambiarIndice(vtt, c);
            modificarVTT(vtt);
         } else {
            vigenciaSeleccionada.setFechavigencia(fechaVigenciaVTT);

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
            RequestContext.getCurrentInstance().execute("PF('errorFechaVTT').show()");
         }
      } else {
         vigenciaSeleccionada.setFechavigencia(fechaVigenciaVTT);

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
      vigenciaSeleccionada = vtt;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TIPOTRABAJADOR")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         vigenciaSeleccionada.getTipotrabajador().setNombre(tipoTrabajador);

         for (int i = 0; i < listaTiposTrabajadores.size(); i++) {
            if (listaTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaSeleccionada.setTipotrabajador(listaTiposTrabajadores.get(indiceUnicoElemento));

            listaTiposTrabajadores.clear();
            getListaTiposTrabajadores();
         } else {
            permitirIndex = false;
            contarRegistros();
            // modificarInfoRegistroTipoTrabajador(listaTiposTrabajadores.size());
            RequestContext.getCurrentInstance().update("formLovs:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoTrabajadorDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listVTTCrear.contains(vigenciaSeleccionada)) {

            if (listVTTModificar.isEmpty()) {
               listVTTModificar.add(vigenciaSeleccionada);
            } else if (!listVTTModificar.contains(vigenciaSeleccionada)) {
               listVTTModificar.add(vigenciaSeleccionada);
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
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TIPOTRABAJADOR")) {
         activarLOV = false;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (tipoNuevo == 1) {
            nuevaVigencia.getTipotrabajador().setNombre(tipoTrabajador);
         } else if (tipoNuevo == 2) {
            duplicarVTT.getTipotrabajador().setNombre(tipoTrabajador);
         }
         for (int i = 0; i < listaTiposTrabajadores.size(); i++) {
            if (listaTiposTrabajadores.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigencia.setTipotrabajador(listaTiposTrabajadores.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoTrabajador");
            } else if (tipoNuevo == 2) {
               duplicarVTT.setTipotrabajador(listaTiposTrabajadores.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoTrabajador");
            }
            listaTiposTrabajadores.clear();
            getListaTiposTrabajadores();
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
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void posicionTabla() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      vigenciaSeleccionada = vigenciasTiposTrabajadores.get(indice);
      cambiarIndice(vigenciaSeleccionada, columna);
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
      if (permitirIndex) {
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         if (cambioRetiros == false && cambioPension == false) {
            vigenciaSeleccionada = vtt;
            cualCelda = celda;
            fechaVigenciaVTT = vigenciaSeleccionada.getFechavigencia();
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");

            cambioVisibleRetiradosInput();
            cambioVisibleRetiradosMensaje();
            cambioVisiblePensionadoInput();
            cambioVisiblePensionadoMensaje();

         } else {
            if (cambioRetiros == true) {
               RequestContext context = RequestContext.getCurrentInstance();
               activarLOV = false;
               RequestContext.getCurrentInstance().update("form:listaValores");
               RequestContext.getCurrentInstance().update("formularioDialogos:guardarCambiosRetiros");
               RequestContext.getCurrentInstance().execute("PF('guardarCambiosRetiros').show()");
            }
            if (cambioPension == true) {
               RequestContext context = RequestContext.getCurrentInstance();
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
         pensionVigencia.setTipopensionado(new TiposPensionados());
         pensionVigencia.setTutor(new Personas());
         RequestContext context = RequestContext.getCurrentInstance();
         FacesContext c = FacesContext.getCurrentInstance();
         System.out.println("panelRetiradosInput");
         panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
         panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

         panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
         panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

         panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
         panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");
         System.out.println("panelRetiradosMensaje");
         panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
         panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

         RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
         RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
         RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
         RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");
         RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");

         guardado = true;
         k = 0;
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         vigenciasTiposTrabajadores = null;
         getVigenciasTiposTrabajadores();
         if (vigenciasTiposTrabajadores != null) {
            vigenciaSeleccionada = vigenciasTiposTrabajadores.get(0);
         }
         contarRegistros();
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

         FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
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
      k = 0;
      vigenciasTiposTrabajadores = null;
      vigenciaSeleccionada = null;
      guardado = true;
      permitirIndex = true;
      getVigenciasTiposTrabajadores();
      contarRegistros();
      cambiosPagina = true;
      almacenarRetirado = false;
      almacenarPensionado = false;
      banderaEliminarRetiro = false;

      FacesContext c = FacesContext.getCurrentInstance();
      panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
      panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

      panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
      panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

      panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
      panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

      panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
      panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

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
      if (vigenciaSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (vigenciaSeleccionada != null) {
         editarVTT = vigenciaSeleccionada;

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
      System.out.println("ControlVigenciaTipoTrabajador");
      System.out.println("Secuencia: " + nuevaVigencia.getSecuencia());
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
            System.out.println("Secuencia VTT: " + nuevaVigencia.getSecuencia());
            vigenciasTiposTrabajadores.add(nuevaVigencia);
            vigenciaSeleccionada = vigenciasTiposTrabajadores.get(vigenciasTiposTrabajadores.indexOf(nuevaVigencia));
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            nuevaVigencia = new VigenciasTiposTrabajadores();
            nuevaVigencia.setTipotrabajador(new TiposTrabajadores());
            nuevaVigencia.getTipotrabajador().setTipocotizante(new TiposCotizantes());
            contarRegistros();
            panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
            panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

            panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
            panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

            panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
            panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

            panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
            panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
            RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
            RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
            RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");

            RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVTT.hide();");
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
      if (vigenciaSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (vigenciaSeleccionada != null) {
         duplicarVTT = new VigenciasTiposTrabajadores();
         k++;
         l = BigInteger.valueOf(k);

         duplicarVTT.setEmpleado(vigenciaSeleccionada.getEmpleado());
         duplicarVTT.setFechavigencia(vigenciaSeleccionada.getFechavigencia());
         duplicarVTT.setTipotrabajador(vigenciaSeleccionada.getTipotrabajador());

         FacesContext c = FacesContext.getCurrentInstance();
         panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
         panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

         panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
         panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

         panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
         panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

         panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
         panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

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

         for (int j = 0; j < vigenciasTiposTrabajadores.size(); j++) {
            if (duplicarVTT.getFechavigencia().equals(vigenciasTiposTrabajadores.get(j).getFechavigencia())) {
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
               vigenciasTiposTrabajadores.add(duplicarVTT);
               listVTTCrear.add(duplicarVTT);
               contarRegistros();
               vigenciaSeleccionada = vigenciasTiposTrabajadores.get(vigenciasTiposTrabajadores.indexOf(duplicarVTT));

               FacesContext c = FacesContext.getCurrentInstance();
               panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
               panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

               panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
               panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

               panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
               panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

               panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
               panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

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
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVTT.hide();");
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
      if (vigenciaSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (vigenciaSeleccionada != null) {
            if ((indexRetiro == false) && (indexPension == false)) {
               if (!listVTTModificar.isEmpty() && listVTTModificar.contains(vigenciaSeleccionada)) {
                  int modIndex = listVTTModificar.indexOf(vigenciaSeleccionada);
                  listVTTModificar.remove(modIndex);
                  listVTTBorrar.add(vigenciaSeleccionada);
               } else if (!listVTTCrear.isEmpty() && listVTTCrear.contains(vigenciaSeleccionada)) {
                  int crearIndex = listVTTCrear.indexOf(vigenciaSeleccionada);
                  listVTTCrear.remove(crearIndex);
               } else {
                  listVTTBorrar.add(vigenciaSeleccionada);
               }
               vigenciasTiposTrabajadores.remove(vigenciaSeleccionada);
               if (tipoLista == 1) {
                  filtrarVTT.remove(vigenciaSeleccionada);
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
                     if (!listVTTModificar.isEmpty() && listVTTModificar.contains(vigenciaSeleccionada)) {
                        int modIndex = listVTTModificar.indexOf(vigenciaSeleccionada);
                        listVTTModificar.remove(modIndex);
                        listVTTBorrar.add(vigenciaSeleccionada);
                     } else if (!listVTTCrear.isEmpty() && listVTTCrear.contains(vigenciaSeleccionada)) {
                        int crearIndex = listVTTCrear.indexOf(vigenciaSeleccionada);
                        listVTTCrear.remove(crearIndex);
                     } else {
                        listVTTBorrar.add(vigenciaSeleccionada);
                     }
                     vigenciasTiposTrabajadores.remove(vigenciaSeleccionada);
                     if (tipoLista == 1) {
                        filtrarVTT.remove(vigenciaSeleccionada);
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
                     if (!listVTTModificar.isEmpty() && listVTTModificar.contains(vigenciaSeleccionada)) {
                        int modIndex = listVTTModificar.indexOf(vigenciaSeleccionada);
                        listVTTModificar.remove(modIndex);
                        listVTTBorrar.add(vigenciaSeleccionada);
                     } else if (!listVTTCrear.isEmpty() && listVTTCrear.contains(vigenciaSeleccionada)) {
                        int crearIndex = listVTTCrear.indexOf(vigenciaSeleccionada);
                        listVTTCrear.remove(crearIndex);
                     } else {

                        listVTTBorrar.add(vigenciaSeleccionada);
                     }
                     vigenciasTiposTrabajadores.remove(vigenciaSeleccionada);
                     if (tipoLista == 1) {
                        filtrarVTT.remove(vigenciaSeleccionada);
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
            panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
            panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

            panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
            panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

            panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
            panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

            panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
            panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

            RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
            RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
            RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
            RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");
            RequestContext.getCurrentInstance().update("form:datosVTTEmpleado");
         }
         vigenciaSeleccionada = null;
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
      cerrarFiltrado();
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
      listVTTBorrar.clear();
      listVTTCrear.clear();
      listVTTModificar.clear();
      vigenciaSeleccionada = null;
      k = 0;
      vigenciasTiposTrabajadores = null;
      guardado = false;
      cambiosPagina = true;
      cambioPension = false;
      cambioRetiros = false;
      //tiposPensionados = null;        
      clasesPensiones = null;
      listaPersonas = null;
      listaPensionados = null;
      motivosRetiros = null;
      listaTiposTrabajadores = null;
      //RequestContext.getCurrentInstance().update("form:ACEPTAR");
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
    * @param indice Fila de la tabla
    * @param dlg Dialogo
    * @param LND Tipo actualizacion = LISTA - NUEVO - DUPLICADO
    * @param tt Tipo Trabajador : Pensionado / Retirado / VigenciaTipoTrabajador
    */
   public void asignarIndex(VigenciasTiposTrabajadores vtt, int dlg, int LND, int tt) {
      RequestContext context = RequestContext.getCurrentInstance();
      activarLOV = true;
      vigenciaSeleccionada = vtt;
      RequestContext.getCurrentInstance().update("form:listaValores");
      System.out.println("VigenciasTiposTrabajadores: " + vtt.toString());
      if (tt == 0) {
         if (LND == 0) {
            // vigenciaSeleccionada = vtt;
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dlg == 0) {
            //TiposTrabajadoresDialogo
            tipoTrabajadorSeleccionado = null;
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            contarRegistrosTipoTrabajador();
            RequestContext.getCurrentInstance().update("formLovs:TipoTrabajadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('formLovs:TipoTrabajadorDialogo').show()");
         }
      }
      if (tt == 1) {
         if (LND == 0) {
            indexPension = true;
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dlg == 0) {
            //ClasesPensionDialogo
            clasesPensionesSeleccionada = null;
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            contarRegistrosClasePension();
            RequestContext.getCurrentInstance().update("formLovs:clasePensionDialogo");
            RequestContext.getCurrentInstance().execute("PF('clasePensionDialogo').show()");
         } else if (dlg == 1) {
            //tipoPensionadoDialogo
            tiposPensionadosSeleccionada = null;
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            contarRegistrosTipoPension();
            RequestContext.getCurrentInstance().update("formLovs:tipoPensionadoDialogo");
            RequestContext.getCurrentInstance().execute("PF('tipoPensionadoDialogo').show()");
         } else if (dlg == 2) {
            //causaBientesDialogo
            pensionadoSeleccionado = null;
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            contarRegistrosEmpleado();
            RequestContext.getCurrentInstance().update("formLovs:causaBientesDialogo");
            RequestContext.getCurrentInstance().execute("PF('causaBientesDialogo').show()");
         } else if (dlg == 3) {
            //tipoPensionadoDialogo
            personaSeleccionada = null;
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            contarRegistrosPersona();
            RequestContext.getCurrentInstance().update("formLovs:tutorDialogo");
            RequestContext.getCurrentInstance().execute("PF('tutorDialogo').show()");
         }
      }
      if (tt == 2) {
         if (LND == 0) {
            indexRetiro = true;
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dlg == 0) {
            //RetirosDialogo
            motivoRetiroSeleccionado = null;
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            contarRegistrosMotivosRetiros();
            //    dialogoRetiros();
            RequestContext.getCurrentInstance().update("formLovs:RetirosDialogo");
            RequestContext.getCurrentInstance().execute("PF('RetirosDialogo').show()");
         }
      }
   }

   public void asignarIndex(Retirados retirados, int dlg, int LND, int tt) {
//        RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("Retirados: " + retirados.toString());
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
//                modificarInfoRegistroMotivoRetiros(motivosRetiros.size());
//                //    dialogoRetiros();
//                RequestContext.getCurrentInstance().update("formLovs:RetirosDialogo");
//                RequestContext.getCurrentInstance().execute("PF('RetirosDialogo').show()");
//            }
//        }
   }

   //LOVS
   /**
    * Actualiza la informacion de l tipo de trabajador con respecto a la tabla -
    * nuevo registro o duplicar registro
    */
   public void actualizarTipoTrabajador() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaSeleccionada.setTipotrabajador(tipoTrabajadorSeleccionado);
         if (!listVTTCrear.contains(vigenciaSeleccionada)) {
            if (listVTTModificar.isEmpty()) {
               listVTTModificar.add(vigenciaSeleccionada);
            } else if (!listVTTModificar.contains(vigenciaSeleccionada)) {
               listVTTModificar.add(vigenciaSeleccionada);
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
      RequestContext.getCurrentInstance().update("form:TipoTrabajadorDialogo");
      RequestContext.getCurrentInstance().update("form:lovTipoTrabajador");
      RequestContext.getCurrentInstance().update("form:aceptarTT");

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
      //if (vigenciaSeleccionada == null && indexPension < 0 && indexRetiro < 0) {
      if (vigenciaSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (vigenciaSeleccionada != null) {
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
      vigenciaSeleccionada = null;
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
      vigenciaSeleccionada = null;
   }

   //VISIBILIDAD PANELES RETIRADOS - PENSIONADOS
   /**
    * Cambia la visibilidad del panelRetiradosMensaje con respecto a que el
    * registro sea Extrabajador
    */
   public void cambioVisibleRetiradosMensaje() {
      TiposTrabajadores tipoTrabajadorRetirado;
      if (vigenciaSeleccionada != null) {
         short n1 = 1;
         tipoTrabajadorRetirado = administrarVigenciasTiposTrabajadores.tipoTrabajadorCodigo(n1);
         VigenciasTiposTrabajadores vigenciaTemporal = vigenciaSeleccionada;
         panelRetiradosMensaje = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelRetiradosMensaje");
         if (tipoTrabajadorRetirado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; display: none; visibility: hidden;");
         } else {
            panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left;visibility: visible;");
         }
         RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
      }
   }

   /**
    * Cambia la visibilidad del panelRetiradosInput con respecto a que el
    * registro sea Extrabajador, en caso verdadero carga los datos del retirado
    */
   public void cambioVisibleRetiradosInput() {
      TiposTrabajadores tipoTrabajadorRetirado;
      if (vigenciaSeleccionada != null) {
         short n1 = 1;
         tipoTrabajadorRetirado = administrarVigenciasTiposTrabajadores.tipoTrabajadorCodigo(n1);
         VigenciasTiposTrabajadores vigenciaTemporal = vigenciaSeleccionada;
         panelRetiradosInput = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelRetiradosInput");
         if (tipoTrabajadorRetirado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            indexRetiro = true;
            cargarRetiro();
            panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible;");
         } else {
            panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");
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
      TiposTrabajadores tipoTrabajadorPensionado;
      if (vigenciaSeleccionada != null) {
         short n2 = 2;
         tipoTrabajadorPensionado = administrarVigenciasTiposTrabajadores.tipoTrabajadorCodigo(n2);
         VigenciasTiposTrabajadores vigenciaTemporal = vigenciaSeleccionada;
         panelPensionadosMensaje = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelPensionadosMensaje");
         if (tipoTrabajadorPensionado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; display: none; visibility: hidden;");
         } else {
            panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible;");
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
      TiposTrabajadores tipoTrabajadorPensionado;
      if (vigenciaSeleccionada != null) {
         short n2 = 2;
         tipoTrabajadorPensionado = administrarVigenciasTiposTrabajadores.tipoTrabajadorCodigo(n2);
         VigenciasTiposTrabajadores vigenciaTemporal = vigenciaSeleccionada;
         panelPensionadosInput = (Panel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelPensionadosInput");
         if (tipoTrabajadorPensionado.getCodigo() == vigenciaTemporal.getTipotrabajador().getCodigo()) {
            indexPension = true;
            cargarPension();
            panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left;  visibility: visible;");
         } else {
            panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");
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
      getMotivosRetiros();
      retiroVigencia = administrarVigenciasTiposTrabajadores.retiroPorSecuenciaVigencia(vigenciaSeleccionada.getSecuencia());
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
      pensionVigencia = administrarVigenciasTiposTrabajadores.pensionPorSecuenciaVigencia(vigenciaSeleccionada.getSecuencia());
      if (pensionVigencia.getSecuencia() == null) {
         operacionPension = true;
         pensionVigencia = new Pensionados();
         pensionVigencia.setClase(new ClasesPensiones());
         pensionVigencia.setCausabiente(new Empleados());
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
      System.out.println("ENTRO guardarDatosRetiros");
      if (retiroVigencia.getFecharetiro() != null) {
         System.out.println("ControlVigenciaTipoTrabajador.guardarDatosPensiones:");
         System.out.println("Secuencia: " + retiroVigencia.getSecuencia());
         retiroVigencia.setVigenciatipotrabajador(vigenciaSeleccionada);

         k++;
         k = retiroVigencia.getSecuencia().intValue();
         System.out.println("K: " + k);
         l = BigInteger.valueOf(k);
         System.out.println("L: " + l);
         retiroVigencia.setSecuencia(l);
         System.out.println("pensionVigencia.Secuencia: " + retiroVigencia.getSecuencia());
         if (operacionRetiro == false) {
            if (banderaLimpiarRetiro == true) {
               administrarVigenciasTiposTrabajadores.borrarRetirado(retiroCopia);
            } else {
               System.out.println("editar");
               System.out.println("Secuencia: " + retiroVigencia.getSecuencia());
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

            panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
            panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 10px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

            panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
            panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 10px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible;");

            panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
            panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 10px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

            panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
            panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 10px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");
         } else {
            panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
            panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

            panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
            panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

            panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
            panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

            panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
            panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

         }
         System.err.println("---------------------DATOS GUARDADOS---------------------------------");
         System.out.println("Fecha Retiro: " + retiroVigencia.getFecharetiro());
         System.out.println("Motivo de retiro: " + retiroVigencia.getMotivoretiro().getNombre());
         System.out.println("Descripcion: " + retiroVigencia.getDescripcion());
         System.err.println("---------------------------------------------------------------------");

         cambioRetiros = false;
         retiroVigencia = new Retirados();
         retiroVigencia.setMotivoretiro(new MotivosRetiros());
         banderaLimpiarRetiro = false;

         operacionRetiro = false;

         //  cargarRetiro();
         RequestContext context = RequestContext.getCurrentInstance();

         FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos de Retirados con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");

         RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
         RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
         RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
         RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");

         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         System.out.println("SALIO GUARDARDATOSRETIROS");

      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegRetiro').show()");
         System.out.println("SALIO CON ERROR");
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
      panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
      panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

      panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
      panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

      panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
      panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

      panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
      panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
      RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
      RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
      RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");
   }

   /**
    * Efectua el guardado de los datos cambiados en el panel pensiones
    */
   public void guardarDatosPensiones() {
      System.out.println("ENTRO guardarDatosPensiones");
      if (pensionVigencia.getFechainiciopension() != null && pensionVigencia.getClase().getSecuencia() != null) {
         System.out.println("ControlVigenciaTipoTrabajador.guardarDatosPensiones:");
         System.out.println("Secuencia: " + pensionVigencia.getSecuencia());
         pensionVigencia.setVigenciatipotrabajador(vigenciaSeleccionada);

         k++;
         k = pensionVigencia.getSecuencia().intValue();
         System.out.println("K: " + k);
         l = BigInteger.valueOf(k);
         System.out.println("L: " + l);
         pensionVigencia.setSecuencia(l);
         System.out.println("pensionVigencia.Secuencia: " + pensionVigencia.getSecuencia());
         if (operacionPension == false) {
            if (banderaLimpiarPension == true) {
               administrarVigenciasTiposTrabajadores.borrarPensionado(pensionCopia);
            } else {
               System.out.println("editar");
               System.out.println("Secuencia: " + pensionVigencia.getSecuencia());
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

            panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
            panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 10px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

            panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
            panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 10px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible;");

            panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
            panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 10px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

            panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
            panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 10px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");
         } else {
            panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
            panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

            panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
            panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

            panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
            panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

            panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
            panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");
         }
         cambioPension = false;
         pensionVigencia = new Pensionados();
         pensionVigencia.setClase(new ClasesPensiones());
         pensionVigencia.setCausabiente(new Empleados());
         pensionVigencia.setTipopensionado(new TiposPensionados());
         pensionVigencia.setTutor(new Personas());
         banderaLimpiarPension = false;

         operacionPension = false;

         RequestContext context = RequestContext.getCurrentInstance();

         FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos de Pensionados con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");

         RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
         RequestContext.getCurrentInstance().update("form:panelRetiradosMensaje");
         RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
         RequestContext.getCurrentInstance().update("form:panelPensionadosMensaje");

         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         System.out.println("SALIO");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegPensionado').show()");
         System.out.println("SALIO CON ERROR");
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
      pensionVigencia.setTipopensionado(new TiposPensionados());
      pensionVigencia.setTutor(new Personas());
      FacesContext c = FacesContext.getCurrentInstance();
      panelRetiradosInput = (Panel) c.getViewRoot().findComponent("form:panelRetiradosInput");
      panelRetiradosInput.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

      panelPensionadosMensaje = (Panel) c.getViewRoot().findComponent("form:panelPensionadosMensaje");
      panelPensionadosMensaje.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

      panelPensionadosInput = (Panel) c.getViewRoot().findComponent("form:panelPensionadosInput");
      panelPensionadosInput.setStyle("position: absolute; left: 12px; top: 310px; font-size: 12px; width: 410px; height: 185px; border-radius: 10px; text-align: left; visibility: hidden; display: none;");

      panelRetiradosMensaje = (Panel) c.getViewRoot().findComponent("form:panelRetiradosMensaje");
      panelRetiradosMensaje.setStyle("position: absolute; left: 440px; top: 310px; font-size: 12px; width: 415px; height: 185px; border-radius: 10px; text-align: left; visibility: visible");

      RequestContext context = RequestContext.getCurrentInstance();
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
      System.out.println("dialogoRetiros.Motivosretiros: " + motivosRetiros);
      context.reset("formLovs:RetirosDialogo");
      RequestContext.getCurrentInstance().execute("PF('RetirosDialogo').show()");
   }

   /**
    * Actualiza la selecciion del motivo retiro
    */
   public void actualizarMotivoRetiro() {
      banderaCambiosRetirados();
      System.err.println("Estoy en actualizarMotivoRetiro");
      System.out.println("retiroVigencia: " + retiroVigencia);
      retiroVigencia.setMotivoretiro(motivoRetiroSeleccionado);
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelRetiradosInput");
      aceptar = true;
      filtradoMotivosRetiros = null;
      motivoRetiroSeleccionado = null;
      vigenciaSeleccionada = null;
      /*
         *
       */
      getMotivosRetiros();

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
      System.out.println("cancelarMotivoRetiro.Motivosretiros: " + motivosRetiros);
      motivoRetiroSeleccionado = null;
      filtradoMotivosRetiros = null;
      aceptar = true;
      vigenciaSeleccionada = null;
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
      getClasesPensiones();

      RequestContext.getCurrentInstance().update("form:clasePensionDialogo");
      RequestContext.getCurrentInstance().update("form:lovClasePension");
      RequestContext.getCurrentInstance().update("form:aceptarCP");

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
      getTiposPensionados();
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
      getListaPensionados();
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
      getListaPersonas();
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
      retiroVigencia.setVigenciatipotrabajador(vigenciaSeleccionada);
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
      pensionVigencia.setClase(new ClasesPensiones());
      pensionVigencia.setTipopensionado(new TiposPensionados());
      pensionVigencia.setTutor(new Personas());
      pensionVigencia.setSecuencia(pensionCopia.getSecuencia());
      pensionVigencia.setVigenciatipotrabajador(vigenciaSeleccionada);
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
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:panelPensionadosInput");
   }

   /**
    * Metodo que activa una bandera que se activa si se realiza un cambio en el
    * panel retiros
    */
   public void banderaCambiosRetirados() {
      if (cambioRetiros == false) {
         System.out.println("ControlVigenciaTipoTrabajador.banderaCambiosRetirados()");
         System.out.println("Secuencia: " + retiroVigencia.getSecuencia());
         cambioRetiros = true;
         banderaEliminarRetiro = false;
      }
      cambiosPagina = false;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      System.out.println("paso por banderaCambiosRetirados() y salio Bien");
   }

   /**
    * Metodo que activa una bandera que se activa si se realiza un cambio en el
    * panel pensionado
    */
   public void banderaCambiosPensionados() {
      if (cambioPension == false) {
         System.out.println("ControlVigenciaTipoTrabajador.banderaCambiosPensionados()");
         System.out.println("Secuencia: " + pensionVigencia.getSecuencia());
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
      List<VigenciasTiposTrabajadores> listaVigenciaTiposTrabajadoresReal;
      listaVigenciaTiposTrabajadoresReal = administrarVigenciasTiposTrabajadores.vigenciasTiposTrabajadoresEmpleado(empleado.getSecuencia());
      VigenciasTiposTrabajadores vigenciaSeleccionadaRetirados = vigenciaSeleccionada;
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
      System.out.println("ENTRO validarRegistroSeleccionadoPensionado");
      System.out.println("Secuencia: " + pensionVigencia.getSecuencia());
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
      VigenciasTiposTrabajadores vigenciaSeleccionadaPension = vigenciaSeleccionada;
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
      System.out.println("SALIO validarRegistroSeleccionadoPensionado");
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
      System.out.println("ENTRO guardarNuevoRegistroPension()");
      guardarCambiosVTT();
      almacenarPensionado = true;
   }

   /**
    * Guardado general de la pagina
    */
   public void guardarGeneral() {
      System.out.println("ENTRO GUARDADO GENERAL");
      if (cambiosPagina == false) {
         if ((almacenarRetirado == true) && (banderaEliminarRetiro == false)) {
            System.out.println("ENTRO IF ALMACENAR RETIRADOS");
            guardarDatosRetiros();
         }

         if ((almacenarPensionado == true) && (banderaEliminarPension == false)) {
            System.out.println("ENTRO IF ALMACENAR PENSIONADO");
            guardarDatosPensiones();
         }

         if (guardado == false) {
            guardarCambiosVTT();
         }
      }
      System.out.println("SALIO guardarGeneral");
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (vigenciaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASTIPOSTRABAJADORES");
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
      RequestContext.getCurrentInstance().update("form:infoRegistroTipoEntidad");
   }

   public void recordarSeleccion() {
      if (vigenciaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVTTEmpleado");
         tablaC.setSelection(vigenciaSeleccionada);
      } else {
         vigenciaSeleccionada = null;
      }
      //System.out.println("vigenciaSeleccionada: " + vigenciaSeleccionada);
   }

   public void anularLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //**************************GETTER & SETTER**********************************************************************************************************************************************//
   public List<VigenciasTiposTrabajadores> getVigenciasTiposTrabajadores() {
      try {
         if (vigenciasTiposTrabajadores == null) {
            vigenciasTiposTrabajadores = administrarVigenciasTiposTrabajadores.vigenciasTiposTrabajadoresEmpleado(empleado.getSecuencia());
         }
         return vigenciasTiposTrabajadores;
      } catch (Exception e) {
         //System.out.println("Error....................!!!!!!!!!!!! getVigenciasTiposTrabajadores ");
         return null;
      }
   }

   public void setVigenciasTiposTrabajadores(List<VigenciasTiposTrabajadores> vigenciasTiposTrabajadores) {
      this.vigenciasTiposTrabajadores = vigenciasTiposTrabajadores;
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

   public List<TiposTrabajadores> getListaTiposTrabajadores() {
      if (listaTiposTrabajadores == null) {
         listaTiposTrabajadores = administrarVigenciasTiposTrabajadores.tiposTrabajadores();
      }
      return listaTiposTrabajadores;
   }

   public void setListaTiposTrabajadores(List<TiposTrabajadores> listaTiposTrabajadores) {
      this.listaTiposTrabajadores = listaTiposTrabajadores;
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

   public List<MotivosRetiros> getMotivosRetiros() {
      if (motivosRetiros == null) {
         motivosRetiros = administrarVigenciasTiposTrabajadores.motivosRetiros();
      }
      return motivosRetiros;
   }

   public void setMotivosRetiros(List<MotivosRetiros> motivosRetiros) {
      this.motivosRetiros = motivosRetiros;
   }

   public MotivosRetiros getMotivoRetiroSeleccionado() {
      return motivoRetiroSeleccionado;
   }

   public void setMotivoRetiroSeleccionado(MotivosRetiros motivoRetiroSeleccionado) {
      this.motivoRetiroSeleccionado = motivoRetiroSeleccionado;
   }

   public List<Pensionados> getListaPensionados() {
      if (listaPensionados == null) {
         listaPensionados = administrarVigenciasTiposTrabajadores.listaPensionados();
      }
      return listaPensionados;
   }

   public void setListaPensionados(List<Pensionados> listaPensionados) {
      this.listaPensionados = listaPensionados;
   }

   public Pensionados getPensionVigencia() {
      return pensionVigencia;
   }

   public void setPensionVigencia(Pensionados pensionVigencia) {
      this.pensionVigencia = pensionVigencia;
   }

   public List<Personas> getListaPersonas() {
      if (listaPersonas == null) {
         listaPersonas = administrarVigenciasTiposTrabajadores.listaPersonas();
      }
      return listaPersonas;
   }

   public void setListaPersonas(List<Personas> listaPersonas) {
      this.listaPersonas = listaPersonas;
   }

   public Personas getPersonaSeleccionada() {
      return personaSeleccionada;
   }

   public void setPersonaSeleccionada(Personas personaSeleccionada) {
      this.personaSeleccionada = personaSeleccionada;
   }

   public List<ClasesPensiones> getClasesPensiones() {
      if (clasesPensiones == null) {
         clasesPensiones = administrarVigenciasTiposTrabajadores.clasesPensiones();
      }
      return clasesPensiones;
   }

   public void setClasesPensiones(List<ClasesPensiones> clasesPensiones) {
      this.clasesPensiones = clasesPensiones;
   }

   public ClasesPensiones getClasesPensionesSeleccionada() {
      return clasesPensionesSeleccionada;
   }

   public void setClasesPensionesSeleccionada(ClasesPensiones clasesPensionesSeleccionada) {
      this.clasesPensionesSeleccionada = clasesPensionesSeleccionada;
   }

   public List<TiposPensionados> getTiposPensionados() {
      if (tiposPensionados == null) {
         tiposPensionados = administrarVigenciasTiposTrabajadores.tiposPensionados();
      }
      return tiposPensionados;
   }

   public void setTiposPensionados(List<TiposPensionados> tiposPensionados) {
      this.tiposPensionados = tiposPensionados;
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

   public VigenciasTiposTrabajadores getVigenciaSeleccionada() {
      return vigenciaSeleccionada;
   }

   public void setVigenciaSeleccionada(VigenciasTiposTrabajadores vigenciaSeleccionada) {
      this.vigenciaSeleccionada = vigenciaSeleccionada;
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
}
