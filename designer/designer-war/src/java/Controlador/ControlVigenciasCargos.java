package Controlador;

//import Convertidores.MotivosCambiosCargosConverter;
import Entidades.*;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarEstructurasInterface;
import InterfaceAdministrar.AdministrarMotivosCambiosCargosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasArpsInterface;
import InterfaceAdministrar.AdministrarVigenciasCargosInterface;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import java.io.FileInputStream;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultStreamedContent;

@ManagedBean
@SessionScoped
public class ControlVigenciasCargos implements Serializable {

   //------------------------------------------------------------------------------------------
   //EJB
   //------------------------------------------------------------------------------------------
   @EJB
   AdministrarVigenciasCargosInterface administrarVigenciasCargos;
   @EJB
   AdministrarMotivosCambiosCargosInterface administrarMotivosCambiosCargos;
   @EJB
   AdministrarEstructurasInterface administrarEstructuras;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarVigenciasArpsInterface administrarVigArp;
   @EJB
   AdministarReportesInterface administarReportes;
//   @EJB
//   AdministrarClasesRiesgosInterface administrarClaseRiesgo;

   //------------------------------------------------------------------------------------------
   //ATRIBUTOS
   //------------------------------------------------------------------------------------------
   //Vigencias Cargos
   private List<VigenciasCargos> vigenciasCargosEmpleado;
   private List<VigenciasCargos> filterVC;
   private VigenciasCargos vigenciaSeleccionada;
   private DataTable tablaC;
   //private List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadoresesLista;
   private List<VwTiposEmpleados> actualesTiposTrabajadores;
   private List<VwTiposEmpleados> filtradoActualesTiposTrabajadores;
   private VwTiposEmpleados tiposTrabajadorJefeSeleccionado;
   private Date fechaVigencia;
//    private BigInteger secuencia;
   private Empleados empleado;
   private int tipoActualizacion;//Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla VC
   private Column vcFecha, vcEstructura, vcMotivo, vcNombreCargo, vcCentrosC, vcNombreJefe, vcClaseRiesgo, vcPapel;
   //Estructuras
   private List<Estructuras> lovEstructuras;
   private List<Estructuras> filterEstructuras;
   private Estructuras estructuraSeleccionada;
   //Motivos
   private List<MotivosCambiosCargos> motivosCambiosCargos;
   private List<MotivosCambiosCargos> filterMotivos;
   private MotivosCambiosCargos motivoSeleccionado;
//   private MotivosCambiosCargosConverter motivoConverter;
   //Cargos
   private List<Cargos> lovCargos;
   private List<Cargos> filterCargos;
   private Cargos cargoSeleccionado;
   //Clase Riesgo
   private List<ClasesRiesgos> lovClasesRiesgos;
   private List<ClasesRiesgos> lovClasesRiesgosFiltrar;
   private ClasesRiesgos claseRiesgoSeleccionada;
   //Papel
   private List<Papeles> lovPapeles;
   private List<Papeles> lovPapelesFiltrar;
   private Papeles papelSeleccionado;

   //Otros
   private boolean aceptar;
   private SimpleDateFormat formatoFecha;
   //Pruebas para modificar
   private List<VigenciasCargos> listVCModificar;
   private boolean guardado;
   //crear VC
   public VigenciasCargos nuevaVigencia;
   private List<VigenciasCargos> listVCCrear;
   private BigInteger l;
   private int k;
   private String mensajeValidacion;
   //borrar VC
   private List<VigenciasCargos> listVCBorrar;
   //editar celda
   private VigenciasCargos editarVC;
   private int cualCelda, tipoLista;
   private boolean cambioEditor, aceptarEditar;
   //duplicar
   private VigenciasCargos duplicarVC;
   //AUTOCOMPLETAR
   private String nombreEstructura, motivoCambioC, nombreCargo, nombreCompleto;
   private Date fechaVigenciaBck;
   private boolean permitirIndex;
   //ACTIVAR  - DESACTIVAR BOTONES ULTIMO Y PRIMER REGISTRO
   private boolean botonPrimero;
   private boolean botonAnterior;
   private boolean botonSiguiente;
   private boolean botonUltimo;
   //REGISTRO QUE TENDRA EL FOCO
   private String registroFoco;
   //INFORMACION DEL REGISTRO QUE TIENE EL FOCO
   private String altoTabla, infoRegistro, infoRegistroJefe, infoRegistroEstructuras, infoRegistroMotivos, infoRegistroCargos, infoRegistroClaseR, infoRegistroPapel;
   //variables del dialogo vigencias arp
   private String porcentaje;
   private VigenciasArps nuevaVigArp;
   //
   private boolean activarLOV;
   //
   private StreamedContent reporte;
   private String pathReporteGenerado = null;
   private String nombreReporte, tipoReporte;
   private Inforeportes funcionesCargo;
   private String cabezeraVisor;
   private boolean estadoReporte;
   private String resultadoReporte;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   //------------------------------------------------------------------------------------------
   //CONSTRUCTOR(ES)
   //------------------------------------------------------------------------------------------
   public ControlVigenciasCargos() {
      actualesTiposTrabajadores = null;
      motivosCambiosCargos = null;
      lovEstructuras = null;
      lovCargos = null;
      empleado = new Empleados();
      bandera = 0;
      vigenciasCargosEmpleado = null;
      fechaVigencia = new Date();
      motivoSeleccionado = null;
      formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
      aceptar = true;
      listVCBorrar = new ArrayList();
      listVCCrear = new ArrayList();
      k = 0;
      listVCModificar = new ArrayList();
      editarVC = new VigenciasCargos();
      cambioEditor = false;
      aceptarEditar = true;
      cualCelda = -1;
      tipoLista = 0;
      guardado = true;
      nuevaVigencia = new VigenciasCargos();
      nuevaVigencia.setEstructura(new Estructuras());
      nuevaVigencia.setMotivocambiocargo(new MotivosCambiosCargos());
      nuevaVigencia.setCargo(new Cargos());
      nuevaVigencia.setClaseRiesgo(new ClasesRiesgos());
      duplicarVC = new VigenciasCargos();
      duplicarVC.setEstructura(new Estructuras());
      duplicarVC.setMotivocambiocargo(new MotivosCambiosCargos());
      duplicarVC.setCargo(new Cargos());
      duplicarVC.setClaseRiesgo(new ClasesRiesgos());
      permitirIndex = true;
      registroFoco = "form:datosVCEmpleado:editFecha";
      altoTabla = "292";
      vigenciaSeleccionada = null;
      activarLOV = true;
      lovClasesRiesgos = null;
      nuevaVigArp = new VigenciasArps();
      nuevaVigArp.setFechainicial(new Date());
      nuevaVigArp.setFechafinal(new Date());
      nombreReporte = "funciones_cargo";
      tipoReporte = "PDF";
      estadoReporte = false;
      lovPapeles = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovClasesRiesgos = null;
      lovPapeles = null;
      motivosCambiosCargos = null;
      lovEstructuras = null;
      lovCargos = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarVigenciasCargos.obtenerConexion(ses.getId());
         administrarMotivosCambiosCargos.obtenerConexion(ses.getId());
         administrarEstructuras.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         administrarVigArp.obtenerConexion(ses.getId());
         administarReportes.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
         System.out.println("Causa: " + e.getCause());
      }
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
      String pagActual = "emplvigenciascargos";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
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

   //------------------------------------------------------------------------------------------
   //METODOS DE MANEJO DE INFORMACION
   //------------------------------------------------------------------------------------------
   //VIGENCIASCARGOS----------------------------------------------------
   public void recibirEmpleado(Empleados emp) {
      empleado = emp;
      getVigenciasCargosEmpleado();
      if (vigenciasCargosEmpleado != null) {
         vigenciaSeleccionada = vigenciasCargosEmpleado.get(0);
         if (vigenciaSeleccionada.getClaseRiesgo() == null) {
            vigenciaSeleccionada.setClaseRiesgo(null);
         }
      }
   }

   public void recibirPaginaEntrante(String pagina, Empleados emp) {
      paginaAnterior = pagina;
      recibirEmpleado(emp);
   }

   public String redirigir() {
      System.out.println(this.getClass().getName() + ".redirigir()");
      if (paginaAnterior == null) {
         System.out.println("la pagina anterior es nula.");
      }
      return paginaAnterior;
   }

   /*
     * Metodo encargado de actualizar una VigenciaCargo en la base de datos
    */
   public void actualizarVigencias() {
      for (int i = 0; i < vigenciasCargosEmpleado.size(); i++) {
         administrarVigenciasCargos.editarVigenciaCargo(vigenciasCargosEmpleado.get(i));
      }
      vigenciasCargosEmpleado = null;
   }
   //ESTRUCTURAS--------------------------------------------------------

   /*
     * Metodo encargado de cargar las estructuras que van a componer el dialogo
    */
   public void cargarEstructuras(VigenciasCargos vCargo) {
      activarLOV = false;
      String forFecha = formatoFecha.format(vCargo.getFechavigencia());
      lovEstructuras = administrarEstructuras.consultarNativeQueryEstructuras(forFecha);
      vigenciaSeleccionada = vCargo;
      RequestContext.getCurrentInstance().update("form:dlgEstructuras");
      RequestContext.getCurrentInstance().execute("PF('dlgEstructuras').show()");
      contarRegistrosEstructuras();
      RequestContext.getCurrentInstance().update("form:listaValores");
      tipoActualizacion = 0;
   }

   public void cargarEstructurasNuevoRegistro(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
         if (nuevaVigencia.getFechavigencia() == null) {
            RequestContext.getCurrentInstance().execute("PF('necesitaFecha').show()");
         } else {
            String forFecha = formatoFecha.format(nuevaVigencia.getFechavigencia());
            lovEstructuras = administrarEstructuras.consultarNativeQueryEstructuras(forFecha);
            contarRegistrosEstructuras();
            RequestContext.getCurrentInstance().update("form:dlgEstructuras");
            RequestContext.getCurrentInstance().execute("PF('dlgEstructuras').show()");
         }
      } else if (tipoNuevo == 1) {
         tipoActualizacion = 2;
         if (duplicarVC.getFechavigencia() == null) {
            RequestContext.getCurrentInstance().execute("PF('necesitaFecha').show()");
         } else {
            String forFecha = formatoFecha.format(duplicarVC.getFechavigencia());
            lovEstructuras = administrarEstructuras.consultarNativeQueryEstructuras(forFecha);
            contarRegistrosEstructuras();
            RequestContext.getCurrentInstance().update("form:dlgEstructuras");
            RequestContext.getCurrentInstance().execute("PF('dlgEstructuras').show()");
         }
      }
   }

   /*
     * Metodo encargado de actualizar la Estructura de una VigenciaCargo
    */
   public void actualizarEstructura() {
      if (tipoActualizacion == 0) {
         vigenciaSeleccionada.setEstructura(estructuraSeleccionada);

         if (!listVCCrear.contains(vigenciaSeleccionada)) {
            if (listVCModificar.isEmpty()) {
               listVCModificar.add(vigenciaSeleccionada);
            } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
               listVCModificar.add(vigenciaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaVigencia.setEstructura(estructuraSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
      } else if (tipoActualizacion == 2) {
         duplicarVC.setEstructura(estructuraSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVC");
      }
      filterEstructuras = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      estructuraSeleccionada = null;
   }

   public void cancelarCambioEstructura() {
      //filterEstructuras = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
   }
   //MOTIVOS----------------------------------------------------------

   /*
     * Metodo encargado de llenar la lista utilizada por el autocompletar
    */
   public List<MotivosCambiosCargos> autocompletarMotivo(String in) {
      List<MotivosCambiosCargos> mot = getMotivosCambiosCargos();
      List<MotivosCambiosCargos> rta = new ArrayList<MotivosCambiosCargos>();
      for (MotivosCambiosCargos m : mot) {
         if (m.getNombre().startsWith(in.toUpperCase())) {
            rta.add(m);
         }
      }
      return rta;
   }

   /*
     * Metodo encargado de actualizar el MotivoCambioCargo de una VigenciaCargo
    */
   public void actualizarMotivo() {
      if (tipoActualizacion == 0) {
         vigenciaSeleccionada.setMotivocambiocargo(motivoSeleccionado);
         if (!listVCCrear.contains(vigenciaSeleccionada)) {
            if (listVCModificar.isEmpty()) {
               listVCModificar.add(vigenciaSeleccionada);
            } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
               listVCModificar.add(vigenciaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaVigencia.setMotivocambiocargo(motivoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
      } else if (tipoActualizacion == 2) {
         duplicarVC.setMotivocambiocargo(motivoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVC");
      }
      filterMotivos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   public void cancelarCambioMotivo() {
      filterMotivos = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
   }

   //CARGOS-----------------------------------------------------------
   /*
     * Metodo encargado de actualizar el MotivoCambioCargo de una VigenciaCargo
    */
   public void actualizarCargo() {
      if (tipoActualizacion == 0) {
         vigenciaSeleccionada.setCargo(cargoSeleccionado);
         if (!listVCCrear.contains(vigenciaSeleccionada)) {
            if (listVCModificar.isEmpty()) {
               listVCModificar.add(vigenciaSeleccionada);
            } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
               listVCModificar.add(vigenciaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaVigencia.setCargo(cargoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
      } else if (tipoActualizacion == 2) {
         duplicarVC.setCargo(cargoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVC");
      }
      filterCargos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   public void cancelarCambioCargo() {
      filterCargos = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
   }

   public void actualizarEmpleadoJefe() {
      if (tipoActualizacion == 0) {
         vigenciaSeleccionada.setEmpleadojefe(administrarVigenciasCargos.buscarEmpleado(tiposTrabajadorJefeSeleccionado.getRfEmpleado()));
         if (!listVCCrear.contains(vigenciaSeleccionada)) {
            if (listVCModificar.isEmpty()) {
               listVCModificar.add(vigenciaSeleccionada);
            } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
               listVCModificar.add(vigenciaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaVigencia.setEmpleadojefe(administrarVigenciasCargos.buscarEmpleado(tiposTrabajadorJefeSeleccionado.getRfEmpleado()));
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
      } else if (tipoActualizacion == 2) {
         duplicarVC.setEmpleadojefe(administrarVigenciasCargos.buscarEmpleado(tiposTrabajadorJefeSeleccionado.getRfEmpleado()));
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVC");
      }
      //filtradoActualesTiposTrabajadores = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;

   }

   public void cancelarEmpleadoJefe() {
      //filtradoActualesTiposTrabajadores = null;
      //seleccionVWActualesTiposTrabajadoreses = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
   }
   //FECHAVIGENCIA-------------------------------------------------------------

   /*
     * Metodo encargado de actualizar la fecha de la Vigencia seleccionada
    */
   public void actualizarFecha(Date fecha) {
      System.out.println(fecha);
      fechaVigencia = fecha;
   }

   /*
     * Metodo encargado de cambiar el formato de la fecha de una vigencia cargo
    */
   public String cambioFormatoFecha(VigenciasCargos vigencia) {
      Date fecha = vigencia.getFechavigencia();
      String forFecha = formatoFecha.format(fecha);
      return forFecha;
   }
   //OTROS---------------------------------------------------------------------
   /*
     * Metodo encargado de cambiar el valor booleano para habilitar un boton
    */
   public void activarAceptar() {
      aceptar = false;
   }

   /*
     * Metodo encargado de cambiar el valor booleano para deshabilitar un boton
    */
   public void desactivarAceptar() {
      aceptar = true;
   }

   public void asignarIndex(VigenciasCargos vCargos, int dlg, int tipo) {
      vigenciaSeleccionada = vCargos;
      tipoActualizacion = tipo;
      RequestContext context = RequestContext.getCurrentInstance();
      activarLOV = false;
      if (dlg == 0) {
         motivosCambiosCargos = null;
         getMotivosCambiosCargos();
         contarRegistrosMotivos();
         RequestContext.getCurrentInstance().update("form:dlgMotivos");
         RequestContext.getCurrentInstance().execute("PF('dlgMotivos').show()");
      } else if (dlg == 1) {
         lovCargos = null;
         getLovCargos();
         contarRegistrosCargos();
         RequestContext.getCurrentInstance().update("form:dlgCargos");
         RequestContext.getCurrentInstance().execute("PF('dlgCargos').show()");
      } else if (dlg == 2) {
         contarRegistrosJefe();
         RequestContext.getCurrentInstance().update("form:dialogoEmpleadoJefe");
         RequestContext.getCurrentInstance().execute("PF('dialogoEmpleadoJefe').show()");
      } else if (dlg == 3) {
         lovClasesRiesgos = null;
         getLovClasesRiesgos();
         contarRegistrosClasesRiesgos();
         RequestContext.getCurrentInstance().update("form:dialogoClasesRiesgos");
         RequestContext.getCurrentInstance().execute("PF('dialogoClasesRiesgos').show()");
      } else if (dlg == 4) {
         lovPapeles = null;
         getLovPapeles();
         contarRegistrosPapel();
         RequestContext.getCurrentInstance().update("form:dialogoPapel");
         RequestContext.getCurrentInstance().execute("PF('dialogoPapel').show()");

      }
   }

   public void cancelarYSalir() {
      cancelarModificacion();
      salir();
   }

   public void cancelarModificacion() {
      restaurarTabla();
      activarLOV = true;
      listVCBorrar.clear();
      listVCCrear.clear();
      listVCModificar.clear();
      k = 0;
      vigenciasCargosEmpleado = null;
      vigenciaSeleccionada = null;
      guardado = true;
      permitirIndex = true;
      getVigenciasCargosEmpleado();
      contarRegistros();
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:datosVCEmpleado");
      context.update("form:ACEPTAR");
      context.update("form:listaValores");
   }

   public void salir() {
      limpiarListasValor();
      restaurarTabla();
      activarLOV = true;
      listVCBorrar.clear();
      listVCCrear.clear();
      listVCModificar.clear();
      vigenciaSeleccionada = null;
      k = 0;
      vigenciasCargosEmpleado = null;
      guardado = true;
      navegar("atras");
   }

   private void restaurarTabla() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         vcFecha = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFecha");
         vcFecha.setFilterStyle("display: none; visibility: hidden;");
         vcEstructura = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcEstructura");
         vcEstructura.setFilterStyle("display: none; visibility: hidden;");
         vcMotivo = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcMotivo");
         vcMotivo.setFilterStyle("display: none; visibility: hidden;");
         vcNombreCargo = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcNombreCargo");
         vcNombreCargo.setFilterStyle("display: none; visibility: hidden;");
         vcCentrosC = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcCentrosC");
         vcCentrosC.setFilterStyle("display: none; visibility: hidden;");
         vcNombreJefe = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcNombreJefe");
         vcNombreJefe.setFilterStyle("display: none; visibility: hidden;");
         vcClaseRiesgo = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcClaseRiesgo");
         vcClaseRiesgo.setFilterStyle("display: none; visibility: hidden;");
         vcPapel = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcPapel");
         vcPapel.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "292";
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
         bandera = 0;
         filterVC = null;
         tipoLista = 0;
      }
   }

   /*
     * Metodo encargado de accionar un dialogo especifico
    */
   public void infoDialog() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('datosDialog').show()");
   }

   /*
     * metodo encargado de cambiar de pagina xhtml
    */

 /*
     * public String navega() { //Pruebas //vigenciasCargos //tablaPrueba
     * //pruebaRichFaces //Integracion //Gerencial return "Pruebas.xhtml"; }
    */
   public void fechaSeleccionada(SelectEvent event) {
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      Date nuevaFecha = (Date) event.getObject();
   }

   public void estadoLista() {
      //System.out.println("Lista Mostrada: ");
      //System.out.println("___________________________________________________________________________________________________________");
      for (int i = 0; i < vigenciasCargosEmpleado.size(); i++) {
         Date fecha = vigenciasCargosEmpleado.get(i).getFechavigencia();
         String forFecha = formatoFecha.format(fecha);
         System.out.println(forFecha + " | "
                 + vigenciasCargosEmpleado.get(i).getEstructura().getNombre() + " | "
                 + vigenciasCargosEmpleado.get(i).getMotivocambiocargo().getNombre() + " | "
                 + vigenciasCargosEmpleado.get(i).getCargo().getNombre() + " | "
                 + vigenciasCargosEmpleado.get(i).getClaseRiesgo().getPorcentaje() + " - " + vigenciasCargosEmpleado.get(i).getClaseRiesgo().getDescripcion());
         System.out.println("--------------------------------------------------------------------------------------------------------");
      }
   }

   /*
     * public void unclick() { System.out.println("Un solo Click"); }
     *
     * public void dobleclick() { System.out.println("Doble Click"); }
    */
   // METODOS DEL TOOLBAR
   public void modificarVC(VigenciasCargos vCargos, String confirmarCambio, String valor) {
      vigenciaSeleccionada = vCargos;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("N")) {
         activarLOV = true;
         int control = 0;
         for (int i = 0; i < vigenciasCargosEmpleado.size(); i++) {
            if ((vigenciasCargosEmpleado.get(i).getFechavigencia().compareTo(vigenciaSeleccionada.getFechavigencia()) == 0)
                    && (!vigenciasCargosEmpleado.get(i).getSecuencia().equals(vigenciaSeleccionada.getSecuencia()))) {
               control++;
               vigenciaSeleccionada.setFechavigencia(fechaVigenciaBck);
            }
         }
         if (control == 0) {
            if (!listVCCrear.contains(vigenciaSeleccionada)) {

               if (listVCModificar.isEmpty()) {
                  listVCModificar.add(vigenciaSeleccionada);
               } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
                  listVCModificar.add(vigenciaSeleccionada);
               }
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('validacionFechaDuplicada').show();");
            RequestContext.getCurrentInstance().execute("PF('form:datosVCEmpleado");
         }
      } else if (confirmarCambio.equalsIgnoreCase("ESTRUCTURA")) {
         activarLOV = false;
         vigenciaSeleccionada.getEstructura().setNombre(nombreEstructura);
         for (int i = 0; i < lovEstructuras.size(); i++) {
            if (lovEstructuras.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaSeleccionada.setEstructura(lovEstructuras.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:dlgEstructuras");
            RequestContext.getCurrentInstance().execute("PF('dlgEstructuras').show()");
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("MOTIVOC")) {
         activarLOV = false;
         vigenciaSeleccionada.getMotivocambiocargo().setNombre(motivoCambioC);
         for (int i = 0; i < motivosCambiosCargos.size(); i++) {
            if (motivosCambiosCargos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaSeleccionada.setMotivocambiocargo(motivosCambiosCargos.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:dlgMotivos");
            RequestContext.getCurrentInstance().execute("PF('dlgMotivos').show()");
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("NOMBREC")) {
         activarLOV = false;
         vigenciaSeleccionada.getCargo().setNombre(nombreCargo);

         for (int i = 0; i < lovCargos.size(); i++) {
            if (lovCargos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaSeleccionada.setCargo(lovCargos.get(indiceUnicoElemento));

            lovCargos.clear();
            getLovCargos();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:dlgCargos");
            RequestContext.getCurrentInstance().execute("PF('dlgCargos').show()");
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listVCCrear.contains(vigenciaSeleccionada)) {

            if (listVCModificar.isEmpty()) {
               listVCModificar.add(vigenciaSeleccionada);
            } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
               listVCModificar.add(vigenciaSeleccionada);
            }
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
      }
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void modificarVC(VigenciasCargos vigenciaC) {
      vigenciaSeleccionada = vigenciaC;
      if (!listVCCrear.contains(vigenciaSeleccionada)) {
         if (listVCModificar.isEmpty()) {
            listVCModificar.add(vigenciaSeleccionada);
         } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
            listVCModificar.add(vigenciaSeleccionada);
         }
      }
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("ESTRUCTURA")) {
         boolean fechaValida = false;
         String forFecha;
         if (tipoNuevo == 1) {
            nuevaVigencia.getEstructura().setNombre(nombreEstructura);
            if (nuevaVigencia.getFechavigencia() == null) {
               fechaValida = false;
            } else {
               forFecha = formatoFecha.format(nuevaVigencia.getFechavigencia());
               lovEstructuras = administrarEstructuras.consultarNativeQueryEstructuras(forFecha);
               fechaValida = true;
            }
         } else if (tipoNuevo == 2) {
            duplicarVC.getEstructura().setNombre(nombreEstructura);
            if (duplicarVC.getFechavigencia() == null) {
               fechaValida = false;
            } else {
               forFecha = formatoFecha.format(duplicarVC.getFechavigencia());
               lovEstructuras = administrarEstructuras.consultarNativeQueryEstructuras(forFecha);
               fechaValida = true;
            }
         }
         if (fechaValida == true) {
            for (int i = 0; i < lovEstructuras.size(); i++) {
               if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaVigencia.setEstructura(lovEstructuras.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstructura");
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCentroC");
               } else if (tipoNuevo == 2) {
                  duplicarVC.setEstructura(lovEstructuras.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructura");
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCentroC");
               }
               lovEstructuras.clear();
               getLovEstructuras();
            } else {
               RequestContext.getCurrentInstance().update("form:dlgEstructuras");
               RequestContext.getCurrentInstance().execute("PF('dlgEstructuras').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstructura");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructura");
               }
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('necesitaFecha').show()");
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstructura");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructura");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("MOTIVOC")) {
         if (tipoNuevo == 1) {
            nuevaVigencia.getMotivocambiocargo().setNombre(motivoCambioC);
         } else if (tipoNuevo == 2) {
            duplicarVC.getMotivocambiocargo().setNombre(motivoCambioC);
         }
         for (int i = 0; i < motivosCambiosCargos.size(); i++) {
            if (motivosCambiosCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigencia.setMotivocambiocargo(motivosCambiosCargos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoMotivo");
            } else if (tipoNuevo == 2) {
               duplicarVC.setMotivocambiocargo(motivosCambiosCargos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivo");
            }
            motivosCambiosCargos.clear();
            getMotivosCambiosCargos();
         } else {
            RequestContext.getCurrentInstance().update("form:dlgMotivos");
            RequestContext.getCurrentInstance().execute("PF('dlgMotivos').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoMotivo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivo");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("NOMBREC")) {
         if (tipoNuevo == 1) {
            nuevaVigencia.getCargo().setNombre(nombreCargo);
         } else if (tipoNuevo == 2) {
            duplicarVC.getCargo().setNombre(nombreCargo);
         }
         for (int i = 0; i < lovCargos.size(); i++) {
            if (lovCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaVigencia.setCargo(lovCargos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreCargo");
            } else if (tipoNuevo == 2) {
               duplicarVC.setCargo(lovCargos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreCargo");
            }
            lovCargos.clear();
            getLovCargos();
         } else {
            RequestContext.getCurrentInstance().update("form:dlgCargos");
            RequestContext.getCurrentInstance().execute("PF('dlgCargos').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreCargo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreCargo");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("EMPLEADOJ")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaVigencia.getEmpleadojefe().getPersona().setNombreCompleto(nombreCompleto);
            } else if (tipoNuevo == 2) {
               duplicarVC.getEmpleadojefe().getPersona().setNombreCompleto(nombreCompleto);
            }
            for (int i = 0; i < actualesTiposTrabajadores.size(); i++) {
               if (actualesTiposTrabajadores.get(indiceUnicoElemento).getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaVigencia.setEmpleadojefe(administrarVigenciasCargos.buscarEmpleado(actualesTiposTrabajadores.get(indiceUnicoElemento).getRfEmpleado()));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoJefe");
               } else if (tipoNuevo == 2) {
                  duplicarVC.setEmpleadojefe(administrarVigenciasCargos.buscarEmpleado(actualesTiposTrabajadores.get(indiceUnicoElemento).getRfEmpleado()));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarJefe");
               }
               actualesTiposTrabajadores.clear();
               getActualesTiposTrabajadores();
            } else {
               RequestContext.getCurrentInstance().update("form:dialogoEmpleadoJefe");
               RequestContext.getCurrentInstance().execute("PF('dialogoEmpleadoJefe').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevoJefe");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarJefe");
               }
            }
         } else if (tipoNuevo == 1) {
            nuevaVigencia.setEmpleadojefe(new Empleados());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoJefe");
         } else if (tipoNuevo == 2) {
            duplicarVC.setEmpleadojefe(new Empleados());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarJefe");
         }
      }
   }

   public void guardarYSalir() {
      guardarCambiosVC();
      salir();
   }

   public void guardarCambiosVC() {
      if (guardado == false) {
         if (!listVCBorrar.isEmpty()) {
            for (int i = 0; i < listVCBorrar.size(); i++) {
               if (listVCBorrar.get(i).getEmpleadojefe() != null && listVCBorrar.get(i).getEmpleadojefe().getSecuencia() == null) {
                  listVCBorrar.get(i).setEmpleadojefe(null);
                  administrarVigenciasCargos.borrarVC(listVCBorrar.get(i));
               } else {
                  administrarVigenciasCargos.borrarVC(listVCBorrar.get(i));
               }
            }
            listVCBorrar.clear();
         }
         if (!listVCCrear.isEmpty()) {
            for (int i = 0; i < listVCCrear.size(); i++) {
               if (listVCCrear.get(i).getEmpleadojefe() != null && listVCCrear.get(i).getEmpleadojefe().getSecuencia() == null) {
                  listVCCrear.get(i).setEmpleadojefe(null);
                  administrarVigenciasCargos.crearVC(listVCCrear.get(i));
               } else {
                  administrarVigenciasCargos.crearVC(listVCCrear.get(i));
               }
            }
            listVCCrear.clear();
         }
         if (!listVCModificar.isEmpty()) {
            administrarVigenciasCargos.modificarVC(listVCModificar);
            listVCModificar.clear();
         }
         //System.out.println("Se guardaron los datos con exito");
         vigenciasCargosEmpleado = null;
         getVigenciasCargosEmpleado();
         contarRegistros();
         activarLOV = true;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
         RequestContext.getCurrentInstance().update("form:listaValores");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;
         permitirIndex = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cambiarIndice(VigenciasCargos vCargos, int celda) {
      if (permitirIndex == true) {
         vigenciaSeleccionada = vCargos;
         cualCelda = celda;
         if (cualCelda == 0) {
            activarLOV = true;
            fechaVigenciaBck = vigenciaSeleccionada.getFechavigencia();

         } else if (cualCelda == 1) {
            activarLOV = false;
            nombreEstructura = vigenciaSeleccionada.getEstructura().getNombre();

         } else if (cualCelda == 2) {
            activarLOV = false;
            motivoCambioC = vigenciaSeleccionada.getMotivocambiocargo().getNombre();

         } else if (cualCelda == 3) {
            activarLOV = false;
            nombreCargo = vigenciaSeleccionada.getCargo().getNombre();

         } else if (cualCelda == 4) {
            activarLOV = true;

         } else if (cualCelda == 5) {
            activarLOV = false;
            nombreCompleto = "";
            vigenciaSeleccionada.getNombreEmplJefe();
            if (vigenciaSeleccionada.getNombreEmplJefe() != null) {
               nombreCompleto = vigenciaSeleccionada.getNombreEmplJefe();
            } else {
               nombreCompleto = "";
            }

         } else if (cualCelda == 6) {
            vigenciaSeleccionada.getDescClase();
         } else if (cualCelda == 7) {
            vigenciaSeleccionada.getNombrePapel();
         }
      }
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //CREAR VC
   public void agregarNuevaVC() {
      int pasa = 0;
      mensajeValidacion = "";
      RequestContext context = RequestContext.getCurrentInstance();

      vigenciaSeleccionada = null;
      if (nuevaVigencia.getFechavigencia() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha \n";
         pasa++;
      }
      if (nuevaVigencia.getEstructura().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + " * Estructura \n";
         pasa++;
      }
      if (nuevaVigencia.getMotivocambiocargo().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + "   * Motivo del cambio del cargo \n";
         pasa++;
      }
      if (nuevaVigencia.getCargo().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + " *Cargo";
         pasa++;
      }
      if (pasa == 0) {
         int control = 0;

         for (VigenciasCargos curVigenciasCargosEmpleado : vigenciasCargosEmpleado) {
            if (curVigenciasCargosEmpleado.getFechavigencia().compareTo(nuevaVigencia.getFechavigencia()) == 0) {
               control++;
            }
         }

         if (control == 0) {
            restaurarTabla();
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevaVigencia.setSecuencia(l);
            nuevaVigencia.setEmpleado(empleado);
            if (nuevaVigencia.getEmpleadojefe() != null && nuevaVigencia.getEmpleadojefe().getSecuencia() == null) {
               nuevaVigencia.setEmpleadojefe(null);
            }
            listVCCrear.add(nuevaVigencia);
            vigenciasCargosEmpleado.add(nuevaVigencia);
            vigenciaSeleccionada = nuevaVigencia;
            activarLOV = true;
            nuevaVigencia = new VigenciasCargos();
            nuevaVigencia.setEstructura(new Estructuras());
            nuevaVigencia.setMotivocambiocargo(new MotivosCambiosCargos());
            nuevaVigencia.setCargo(new Cargos());
            nuevaVigencia.setClaseRiesgo(new ClasesRiesgos());
            nuevaVigencia.setPapel(new Papeles());
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            RequestContext.getCurrentInstance().update("form:listaValores");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVC').hide()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('validacionFechaDuplicada').show();");
         }
      } else {
         RequestContext.getCurrentInstance().update("form:validacioNuevaVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevaVigencia').show()");
      }
   }
   //EDITAR DIALOGOS

   public void cambioEditable() {
      cambioEditor = true;
      //System.out.println("Estado del cambio : " + cambioEditor);
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaSeleccionada != null) {
         editarVC = vigenciaSeleccionada;

         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
            RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEstructura");
            RequestContext.getCurrentInstance().execute("PF('editarEstructura').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivo");
            RequestContext.getCurrentInstance().execute("PF('editarMotivo').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreCargo");
            RequestContext.getCurrentInstance().execute("PF('editarNombreCargo').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('editarCentroCosto').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadoJefe");
            RequestContext.getCurrentInstance().execute("PF('editarEmpleadoJefe').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarClaseRiesgo");
            RequestContext.getCurrentInstance().execute("PF('editarClaseRiesgo').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPapelCargo");
            RequestContext.getCurrentInstance().execute("PF('editarPapelCargo').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void reiniciarEditar() {
      editarVC = new VigenciasCargos();
      cambioEditor = false;
      aceptarEditar = true;
      cualCelda = -1;
   }

   //BORRAR VC
   public void borrarVC() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaSeleccionada != null) {
         if (!listVCModificar.isEmpty() && listVCModificar.contains(vigenciaSeleccionada)) {
            int modIndex = listVCModificar.indexOf(vigenciaSeleccionada);
            listVCModificar.remove(modIndex);
            listVCBorrar.add(vigenciaSeleccionada);
         } else if (!listVCCrear.isEmpty() && listVCCrear.contains(vigenciaSeleccionada)) {
            int crearIndex = listVCCrear.indexOf(vigenciaSeleccionada);
            listVCCrear.remove(crearIndex);
         } else {
            listVCBorrar.add(vigenciaSeleccionada);
         }
         vigenciasCargosEmpleado.remove(vigenciaSeleccionada);
         if (tipoLista == 1) {
            restaurarTabla();
         }

         contarRegistros();
         vigenciaSeleccionada = null;
         activarLOV = true;
         RequestContext.getCurrentInstance().update("form:listaValores");
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

//DUPLICAR VC
   public void duplicarVigenciaC() {
      if (vigenciaSeleccionada != null) {
         duplicarVC = new VigenciasCargos();
         k++;
         l = BigInteger.valueOf(k);

         duplicarVC.setSecuencia(l);
         duplicarVC.setFechavigencia(vigenciaSeleccionada.getFechavigencia());
         duplicarVC.setEstructura(vigenciaSeleccionada.getEstructura());
         duplicarVC.setMotivocambiocargo(vigenciaSeleccionada.getMotivocambiocargo());
         duplicarVC.setCargo(vigenciaSeleccionada.getCargo());
         duplicarVC.setEmpleadojefe(vigenciaSeleccionada.getEmpleadojefe());
         duplicarVC.setCalificacion(vigenciaSeleccionada.getCalificacion());
         duplicarVC.setEmpleado(vigenciaSeleccionada.getEmpleado());
         duplicarVC.setEscalafon(vigenciaSeleccionada.getEscalafon());
         duplicarVC.setLiquidahe(vigenciaSeleccionada.getLiquidahe());
         duplicarVC.setTurnorotativo(vigenciaSeleccionada.getTurnorotativo());
         duplicarVC.setClaseRiesgo(vigenciaSeleccionada.getClaseRiesgo());
         duplicarVC.setPapel(vigenciaSeleccionada.getPapel());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVC");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroVC').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int pasa = 0;
      mensajeValidacion = "";
      RequestContext context = RequestContext.getCurrentInstance();

      if (duplicarVC.getFechavigencia() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha \n";
         pasa++;
      }
      if (duplicarVC.getEstructura().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + " * Estructura \n";
         pasa++;
      }
      if (duplicarVC.getMotivocambiocargo().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + "   * Motivo del cambio del cargo \n";
         pasa++;
      }
      if (duplicarVC.getCargo().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + " *Cargo";
         pasa++;
      }
      if (pasa == 0) {
         int control = 0;
         for (VigenciasCargos curVigenciasCargosEmpleado : vigenciasCargosEmpleado) {
            if (curVigenciasCargosEmpleado.getFechavigencia().compareTo(duplicarVC.getFechavigencia()) == 0) {
               control++;
            }
         }
         if (control == 0) {
            vigenciasCargosEmpleado.add(duplicarVC);
            listVCCrear.add(duplicarVC);
            vigenciaSeleccionada = vigenciasCargosEmpleado.get(vigenciasCargosEmpleado.indexOf(duplicarVC));

            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            restaurarTabla();
            contarRegistros();
            System.out.println("vigenciaSeleccionada : " + vigenciaSeleccionada);
            duplicarVC = new VigenciasCargos();
            duplicarVC.setEstructura(new Estructuras());
            duplicarVC.setMotivocambiocargo(new MotivosCambiosCargos());
            duplicarVC.setCargo(new Cargos());
            duplicarVC.setClaseRiesgo(new ClasesRiesgos());
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroVC').hide()");
            RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
            System.out.println("Ya paso por la actualizacion de tabla");
         } else {
            RequestContext.getCurrentInstance().execute("PF('validacionFechaDuplicada').show();");
         }
      } else {
         RequestContext.getCurrentInstance().update("form:validacioNuevaVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevaVigencia').show()");
      }
   }

   public void anularLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //LIMPIAR NUEVO REGISTRO
   public void limpiarNuevaVC() {
      nuevaVigencia = new VigenciasCargos();
      nuevaVigencia.setEstructura(new Estructuras());
      nuevaVigencia.setMotivocambiocargo(new MotivosCambiosCargos());
      nuevaVigencia.setCargo(new Cargos());
      nuevaVigencia.setClaseRiesgo(new ClasesRiesgos());
      nuevaVigencia.setPapel(new Papeles());
   }
   
   public void limpiarduplicarVC() {
      duplicarVC = new VigenciasCargos();
      duplicarVC.setEstructura(new Estructuras());
      duplicarVC.setMotivocambiocargo(new MotivosCambiosCargos());
      duplicarVC.setCargo(new Cargos());
      duplicarVC.setClaseRiesgo(new ClasesRiesgos());
      duplicarVC.setPapel(new Papeles());
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaSeleccionada != null) {
         if (cualCelda == 1) {
            String forFecha = formatoFecha.format(vigenciaSeleccionada.getFechavigencia());
            lovEstructuras = administrarEstructuras.consultarNativeQueryEstructuras(forFecha);
            estructuraSeleccionada = null;
            contarRegistrosEstructuras();
            RequestContext.getCurrentInstance().update("form:dlgEstructuras");
            RequestContext.getCurrentInstance().execute("PF('dlgEstructuras').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 2) {
            tipoActualizacion = 0;
            motivoSeleccionado = null;
            contarRegistrosMotivos();
            RequestContext.getCurrentInstance().update("form:dlgMotivos");
            RequestContext.getCurrentInstance().execute("PF('dlgMotivos').show()");
         } else if (cualCelda == 3) {
            tipoActualizacion = 0;
            cargoSeleccionado = null;
            contarRegistrosCargos();
            RequestContext.getCurrentInstance().update("form:dlgCargos");
            RequestContext.getCurrentInstance().execute("PF('dlgCargos').show()");
         } else if (cualCelda == 5) {
            tipoActualizacion = 0;
            tiposTrabajadorJefeSeleccionado = null;
            contarRegistrosJefe();
            RequestContext.getCurrentInstance().update("form:dialogoEmpleadoJefe");
            RequestContext.getCurrentInstance().execute("PF('dialogoEmpleadoJefe').show()");
         } else if (cualCelda == 6) {
            tipoActualizacion = 0;
            contarRegistrosClasesRiesgos();
            RequestContext.getCurrentInstance().update("form:dialogoClasesRiesgos");
            RequestContext.getCurrentInstance().execute("PF('dialogoClasesRiesgos').show()");
         } else if (cualCelda == 7) {
            tipoActualizacion = 0;
            contarRegistrosPapel();
            RequestContext.getCurrentInstance().update("form:dialogoPapel");
            RequestContext.getCurrentInstance().execute("PF('dialogoPapel').show()");
         }
      }
   }
   //EXPORTAR PDF

   public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
      Document pdf = (Document) document;
      pdf.open();
      pdf.addAuthor("Designer Software Ltda");
      pdf.setPageSize(PageSize.LETTER);
   }

   public void bien() {
      RequestContext.getCurrentInstance().update("formularioDialogos:editarCentroCosto");
      RequestContext.getCurrentInstance().execute("PF('editarCentroCosto').show()");
   }

   public void exportPDF() throws IOException {
      FacesContext context = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) context.getViewRoot().findComponent("formExportar:datosVCEmpleadoExportar");
      Exporter exporter = new ExportarPDF();
      System.out.println("exportPDF() tabla: " + tabla.getColumns());
      exporter.export(context, tabla, "VigenciasCargosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      FacesContext context = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) context.getViewRoot().findComponent("formExportar:datosVCEmpleadoExportar");
      Exporter exporter = new ExportarXLS();
      System.out.println("exportXLS() tabla: " + tabla.getColumns());
      exporter.export(context, tabla, "VigenciasCargosXLS", false, false, "UTF-8", null, null);
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         vcFecha = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcFecha");
         vcFecha.setFilterStyle("width: 85% !important;");
         vcEstructura = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcEstructura");
         vcEstructura.setFilterStyle("width: 85% !important;");
         vcMotivo = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcMotivo");
         vcMotivo.setFilterStyle("width: 85% !important;");
         vcNombreCargo = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcNombreCargo");
         vcNombreCargo.setFilterStyle("width: 85% !important;");
         vcCentrosC = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcCentrosC");
         vcCentrosC.setFilterStyle("width: 85% !important;");
         vcNombreJefe = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcNombreJefe");
         vcNombreJefe.setFilterStyle("width: 85% !important;");
         vcClaseRiesgo = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcClaseRiesgo");
         vcClaseRiesgo.setFilterStyle("width: 85% !important;");
         vcPapel = (Column) c.getViewRoot().findComponent("form:datosVCEmpleado:vcPapel");
         vcPapel.setFilterStyle("width: 85% !important;");
         altoTabla = "272";
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
         bandera = 1;
      } else {
         restaurarTabla();
      }
      cualCelda = -1;
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {
      if (vigenciaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASCARGOS");
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
      } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASCARGOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      activarLOV = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosCargos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCargos");
   }

   public void contarRegistrosEstructuras() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEstructuras");
   }

   public void contarRegistrosJefe() {
      RequestContext.getCurrentInstance().update("form:informacionLOVEJ");
   }

   public void contarRegistrosMotivos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroMotivos");
   }

   public void contarRegistrosClasesRiesgos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroClaseR");
   }

   public void contarRegistrosPapel() {
      RequestContext.getCurrentInstance().update("form:infoRegistroPapel");
   }

   public void recordarSeleccion() {
      if (vigenciaSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVCEmpleado");
         tablaC.setSelection(vigenciaSeleccionada);
      }
   }

   public void deshabilitarBotonLov() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void habilitarBotonLov() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void actualizarClaseRiesgo() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaSeleccionada.setClaseRiesgo(claseRiesgoSeleccionada);
         if (!listVCCrear.contains(vigenciaSeleccionada)) {
            if (listVCModificar.isEmpty()) {
               listVCModificar.add(vigenciaSeleccionada);
            } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
               listVCModificar.add(vigenciaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         deshabilitarBotonLov();
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaVigencia.setClaseRiesgo(claseRiesgoSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
      } else if (tipoActualizacion == 2) {
         duplicarVC.setClaseRiesgo(claseRiesgoSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVC");
      }
      lovClasesRiesgosFiltrar = null;
      claseRiesgoSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:dialogoClasesRiesgos");
      RequestContext.getCurrentInstance().update("form:lovClasesRiesgos");
      RequestContext.getCurrentInstance().update("form:aceptarCR");

      context.reset("form:lovClasesRiesgos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovClasesRiesgos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('dialogoClasesRiesgos').hide()");
   }

   public void cancelarCambioClaseRiesgo() {
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      lovClasesRiesgosFiltrar = null;
      claseRiesgoSeleccionada = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:dialogoClasesRiesgos");
      RequestContext.getCurrentInstance().update("form:lovClasesRiesgos");
      RequestContext.getCurrentInstance().update("form:aceptarCR");

      context.reset("form:lovClasesRiesgos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovClasesRiesgos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('dialogoClasesRiesgos').hide()");
   }

   public void actualizarPapel() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaSeleccionada.setPapel(papelSeleccionado);
         if (!listVCCrear.contains(vigenciaSeleccionada)) {
            if (listVCModificar.isEmpty()) {
               listVCModificar.add(vigenciaSeleccionada);
            } else if (!listVCModificar.contains(vigenciaSeleccionada)) {
               listVCModificar.add(vigenciaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         deshabilitarBotonLov();
         RequestContext.getCurrentInstance().update("form:datosVCEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaVigencia.setPapel(papelSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
      } else if (tipoActualizacion == 2) {
         duplicarVC.setPapel(papelSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVC");
      }
      lovPapelesFiltrar = null;
      papelSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:dialogoPapel");
      RequestContext.getCurrentInstance().update("form:lovPapeles");
      RequestContext.getCurrentInstance().update("form:aceptarPapel");

      context.reset("form:lovPapeles:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPapeles').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('dialogoPapel').hide()");
   }

   public void cancelarCambioPapel() {
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      lovPapelesFiltrar = null;
      papelSeleccionado = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:dialogoPapel");
      RequestContext.getCurrentInstance().update("form:lovPapeles");
      RequestContext.getCurrentInstance().update("form:aceptarPapel");

      context.reset("form:lovPapeles:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPapeles').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('dialogoPapel').hide()");
   }

   public void mostrarDialogoAdicionarPorcentaje() {
      nuevaVigArp.setFechainicial(new Date());
      nuevaVigArp.setFechafinal(new Date());
      nuevaVigArp.setEstructura(vigenciaSeleccionada.getEstructura().getSecuencia());
      nuevaVigArp.setCargo(vigenciaSeleccionada.getCargo().getSecuencia());
      nuevaVigArp.setPorcentaje(BigDecimal.ZERO);
      RequestContext.getCurrentInstance().update("formularioDialogos:adicionarPorcentaje");
      RequestContext.getCurrentInstance().execute("PF('adicionarPorcentaje').show()");
   }

   public void agregarPorcentajeARP() {
      int contador = 0;
      if (nuevaVigArp.getFechainicial() == null || nuevaVigArp.getFechafinal() == null || nuevaVigArp.getPorcentaje() == null) {
         contador++;
      }
      if (contador == 0) {
         porcentaje = administrarVigArp.buscarPorcentaje(nuevaVigArp.getEstructura(), nuevaVigArp.getCargo(), new Date());
         if (porcentaje != null) {
            RequestContext.getCurrentInstance().update("formularioDialogos:existePorcentaje");
            RequestContext.getCurrentInstance().execute("PF('existePorcentaje').show()");
         } else {
            nuevaVigArp.setPorcentaje(nuevaVigArp.getPorcentaje());
            RequestContext.getCurrentInstance().update("formularioDialogos:paso1");
            RequestContext.getCurrentInstance().execute("PF('paso1').show()");
            administrarVigArp.crearVArp(nuevaVigArp);
            RequestContext.getCurrentInstance().update("formularioDialogos:exito");
            RequestContext.getCurrentInstance().execute("PF('exito').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('camposVacios').show()");
      }
   }

   public AsynchronousFilllListener listener() {
      System.out.println(this.getClass().getName() + ".listener()");
      return new AsynchronousFilllListener() {
         //RequestContext context = c;

         @Override
         public void reportFinished(JasperPrint jp) {
            System.out.println(this.getClass().getName() + ".listener().reportFinished()");
            try {
               estadoReporte = true;
               resultadoReporte = "Exito";
            } catch (Exception e) {
               System.out.println("ControlNReporteNomina reportFinished ERROR: " + e.toString());
            }
         }

         @Override
         public void reportCancelled() {
            System.out.println(this.getClass().getName() + ".listener().reportCancelled()");
            estadoReporte = true;
            resultadoReporte = "Cancelación";
         }

         @Override
         public void reportFillError(Throwable e) {
            System.out.println(this.getClass().getName() + ".listener().reportFillError()");
            if (e.getCause() != null) {
               pathReporteGenerado = "ControlInterfaseContableTotal reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
               pathReporteGenerado = "ControlInterfaseContableTotal reportFillError Error: " + e.toString();
            }
            estadoReporte = true;
            resultadoReporte = "Se estallo";
         }
      };
   }

   public void validarDescargaReporte() {
      try {
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
         nombreReporte = "funciones_cargo";
         tipoReporte = "PDF";
         SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
         String estructura = vigenciaSeleccionada.getEstructura().getSecuencia().toString();
         String cargo = vigenciaSeleccionada.getCargo().getSecuencia().toString();

         Map param = new HashMap();
         param.put("estructura", estructura);
         param.put("cargo", cargo);

         pathReporteGenerado = administarReportes.generarReporteFuncionesCargo(nombreReporte, tipoReporte, param);
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            if (tipoReporte.equals("PDF")) {
               FileInputStream fis;
               try {
                  fis = new FileInputStream(new File(pathReporteGenerado));
                  reporte = new DefaultStreamedContent(fis, "application/pdf");
                  cabezeraVisor = "Reporte - " + nombreReporte;
                  RequestContext.getCurrentInstance().update("formularioDialogos:verReportePDF");
                  RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
                  pathReporteGenerado = null;
               } catch (FileNotFoundException ex) {
                  System.out.println("validar descarga reporte - ingreso al catch 1");
                  System.out.println(ex);
                  reporte = null;
               }
            }
         } else {
            System.out.println("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         System.out.println("Error en validar descargar Reporte");
         RequestContext.getCurrentInstance().execute("PF('errorCifraControl').show()");
      }
   }

   public void reiniciarStreamedContent() {
      System.out.println(this.getClass().getName() + ".reiniciarStreamedContent()");
      reporte = null;
   }

   public void cancelarReporte() {
      System.out.println(this.getClass().getName() + ".cancelarReporte()");
      administarReportes.cancelarReporte();
   }

   public void exportarReporte() throws IOException {
      System.out.println(this.getClass().getName() + ".exportarReporte()");
      if (pathReporteGenerado != null) {
         File reporteF = new File(pathReporteGenerado);
         FacesContext ctx = FacesContext.getCurrentInstance();
         FileInputStream fis = new FileInputStream(reporteF);
         byte[] bytes = new byte[1024];
         int read;
         if (!ctx.getResponseComplete()) {
            String fileName = reporteF.getName();
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            ServletOutputStream out = response.getOutputStream();
            while ((read = fis.read(bytes)) != -1) {
               out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            ctx.responseComplete();
         }
      }
   }

   //------------------------------------------------------------------------------------------
   //METODOS GETTER'S AND SETTER'S
   //------------------------------------------------------------------------------------------
   //VIGENCIAS CARGOS
   //VigenciasCargosEmpleado---------------------------
   public List<VigenciasCargos> getVigenciasCargosEmpleado() {
      try {
         if (vigenciasCargosEmpleado == null) {
            vigenciasCargosEmpleado = administrarVigenciasCargos.vigenciasEmpleado(empleado.getSecuencia());
         }
         return vigenciasCargosEmpleado;
      } catch (Exception e) {
         return null;
      }
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public List<VigenciasCargos> getFilterVC() {
      return filterVC;
   }

   public void setFilterVC(List<VigenciasCargos> filterVC) {
      this.filterVC = filterVC;
   }

   public void setVigenciasCargosEmpleado(List<VigenciasCargos> vigenciasCargosEmpleado) {
      this.vigenciasCargosEmpleado = vigenciasCargosEmpleado;
   }
   //FechaVigencia--------------------------------------

   public Date getFechaVigencia() {
      return fechaVigencia;
   }

   public void setFechaVigencia(Date fechaVigencia) {
      this.fechaVigencia = fechaVigencia;
   }
   //VigenciaSeleccionada--------------------------------

   public VigenciasCargos getVigenciaSeleccionada() {
      return vigenciaSeleccionada;
   }

   public void setVigenciaSeleccionada(VigenciasCargos vigenciaSeleccionada) {
      this.vigenciaSeleccionada = vigenciaSeleccionada;
   }

   //dlgEstructuras-----------------------------------------
   public List<Estructuras> getLovEstructuras() {
      if (lovEstructuras == null) {
         lovEstructuras = administrarEstructuras.consultarTodoEstructuras();
      }
      return lovEstructuras;
   }

   public void setLovEstructuras(List<Estructuras> lovEstructuras) {
      this.lovEstructuras = lovEstructuras;
   }
   //Estructurasfilter--------------------------------------

   public List<Estructuras> getFilterEstructuras() {
      return filterEstructuras;
   }

   public void setFilterEstructuras(List<Estructuras> filterEstructuras) {
      this.filterEstructuras = filterEstructuras;
   }

   //EstructuraSeleccionada---------------------------------
   public Estructuras getEstructuraSeleccionada() {
      return estructuraSeleccionada;
   }

   public void setEstructuraSeleccionada(Estructuras estructuraSeleccionada) {
      this.estructuraSeleccionada = estructuraSeleccionada;
   }
   //MOTIVOS
   //MotivosCambiosCargos---------------------------------

   public List<MotivosCambiosCargos> getMotivosCambiosCargos() {
      if (motivosCambiosCargos == null) {
         motivosCambiosCargos = administrarMotivosCambiosCargos.consultarMotivosCambiosCargos();
      }
      return motivosCambiosCargos;
   }

   public void setMotivosCambiosCargos(List<MotivosCambiosCargos> motivosCambiosCargos) {
      this.motivosCambiosCargos = motivosCambiosCargos;
   }

   public String getInfoRegistroMotivos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:motivosCambCargo");
      infoRegistroMotivos = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivos;
   }

   //FilterMotivos---------------------------------------
   public List<MotivosCambiosCargos> getFilterMotivos() {
      return filterMotivos;
   }

   public void setFilterMotivos(List<MotivosCambiosCargos> filterMotivos) {
      this.filterMotivos = filterMotivos;
   }
   //MotivoSeleccionado-----------------------------------

   public MotivosCambiosCargos getMotivoSeleccionado() {
      return motivoSeleccionado;
   }

   public void setMotivoSeleccionado(MotivosCambiosCargos motivoSeleccionado) {
      this.motivoSeleccionado = motivoSeleccionado;
   }

   //Cargos------------------------------------------------
   public List<Cargos> getLovCargos() {
      if (lovCargos == null) {
         lovCargos = administrarEstructuras.consultarTodoCargos();
      }
      return lovCargos;
   }

   public void setLovCargos(List<Cargos> lovCargos) {
      this.lovCargos = lovCargos;
   }

   public String getInfoRegistroCargos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lOVCargos");
      infoRegistroCargos = String.valueOf(tabla.getRowCount());
      return infoRegistroCargos;
   }

   //CargosFilter------------------------------------------
   public List<Cargos> getFilterCargos() {
      return filterCargos;
   }

   public void setFilterCargos(List<Cargos> cargosFilter) {
      this.filterCargos = cargosFilter;
   }
   //CargoSeleccionado-------------------------------------

   public Cargos getCargoSeleccionado() {
      return cargoSeleccionado;
   }

   public void setCargoSeleccionado(Cargos cargoSeleccionado) {
      this.cargoSeleccionado = cargoSeleccionado;
   }
   //OTROS
   //Aceptar---------------------------------------------

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   //Nueva Vigencia Cargo
   public VigenciasCargos getNuevaVigencia() {
      return nuevaVigencia;
   }

   public void setNuevaVigencia(VigenciasCargos nuevaVigencia) {
      this.nuevaVigencia = nuevaVigencia;
   }

   //editar VC celda
   public VigenciasCargos getEditarVC() {
      return editarVC;
   }

   public void setEditarVC(VigenciasCargos editarVC) {
      this.editarVC = editarVC;
   }

   // guardado VC
   public boolean isGuardado() {
      return guardado;
   }

   //DUPLICAR
   public VigenciasCargos getDuplicarVC() {
      return duplicarVC;
   }

   public void setDuplicarVC(VigenciasCargos duplicarVC) {
      this.duplicarVC = duplicarVC;
   }

   public List<VwTiposEmpleados> getActualesTiposTrabajadores() {
      if (actualesTiposTrabajadores == null) {
         actualesTiposTrabajadores = administrarVigenciasCargos.FiltrarTipoTrabajador();
      }
      return actualesTiposTrabajadores;
   }

   public List<VwTiposEmpleados> getFiltradoActualesTiposTrabajadores() {
      return filtradoActualesTiposTrabajadores;
   }

   public void setFiltradoActualesTiposTrabajadores(List<VwTiposEmpleados> filtradoVWActualesTiposTrabajadoresesLista) {
      this.filtradoActualesTiposTrabajadores = filtradoVWActualesTiposTrabajadoresesLista;
   }

   public VwTiposEmpleados getTiposTrabajadorJefeSeleccionado() {
      return tiposTrabajadorJefeSeleccionado;
   }

   public void setTiposTrabajadorJefeSeleccionado(VwTiposEmpleados tiposTrabajadorJefeSeleccionado) {
      this.tiposTrabajadorJefeSeleccionado = tiposTrabajadorJefeSeleccionado;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public boolean isBotonPrimero() {
      return botonPrimero;
   }

   public boolean isBotonAnterior() {
      return botonAnterior;
   }

   public boolean isBotonSiguiente() {
      return botonSiguiente;
   }

   public boolean isBotonUltimo() {
      return botonUltimo;
   }

   public String getRegistroFoco() {
      return registroFoco;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVCEmpleado");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public String getInfoRegistroJefe() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lvEmpleadoJefe");
      infoRegistroJefe = String.valueOf(tabla.getRowCount());
      return infoRegistroJefe;
   }

   public String getInfoRegistroEstructuras() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:estructurasLOV");
      infoRegistroEstructuras = String.valueOf(tabla.getRowCount());
      return infoRegistroEstructuras;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

   public List<ClasesRiesgos> getLovClasesRiesgos() {
      if (lovClasesRiesgos == null) {
         lovClasesRiesgos = administrarVigenciasCargos.lovClasesRiesgos();
      }
      return lovClasesRiesgos;
   }

   public void setLovClasesRiesgos(List<ClasesRiesgos> lovClasesRiesgos) {
      this.lovClasesRiesgos = lovClasesRiesgos;
   }

   public List<ClasesRiesgos> getLovClasesRiesgosFiltrar() {
      return lovClasesRiesgosFiltrar;
   }

   public void setLovClasesRiesgosFiltrar(List<ClasesRiesgos> lovClasesRiesgosFiltrar) {
      this.lovClasesRiesgosFiltrar = lovClasesRiesgosFiltrar;
   }

   public ClasesRiesgos getClaseRiesgoSeleccionada() {
      return claseRiesgoSeleccionada;
   }

   public void setClaseRiesgoSeleccionada(ClasesRiesgos claseRiesgoSeleccionada) {
      this.claseRiesgoSeleccionada = claseRiesgoSeleccionada;
   }

   public String getInfoRegistroClaseR() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovClasesRiesgos");
      infoRegistroClaseR = String.valueOf(tabla.getRowCount());
      return infoRegistroClaseR;
   }

   public void setInfoRegistroClaseR(String infoRegistroClaseR) {
      this.infoRegistroClaseR = infoRegistroClaseR;
   }

   public VigenciasArps getNuevaVigArp() {
      return nuevaVigArp;
   }

   public void setNuevaVigArp(VigenciasArps nuevaVigArp) {
      this.nuevaVigArp = nuevaVigArp;
   }

   public StreamedContent getReporte() {
      return reporte;
   }

   public void setReporte(StreamedContent reporte) {
      this.reporte = reporte;
   }

   public Inforeportes getFuncionesCargo() {
      return funcionesCargo;
   }

   public void setFuncionesCargo(Inforeportes funcionesCargo) {
      this.funcionesCargo = funcionesCargo;
   }

   public String getCabezeraVisor() {
      return cabezeraVisor;
   }

   public void setCabezeraVisor(String cabezeraVisor) {
      this.cabezeraVisor = cabezeraVisor;
   }

   public boolean isEstadoReporte() {
      return estadoReporte;
   }

   public void setEstadoReporte(boolean estadoReporte) {
      this.estadoReporte = estadoReporte;
   }

   public String getPathReporteGenerado() {
      return pathReporteGenerado;
   }

   public void setPathReporteGenerado(String pathReporteGenerado) {
      this.pathReporteGenerado = pathReporteGenerado;
   }

   public List<Papeles> getLovPapeles() {
      if (lovPapeles == null) {
         BigDecimal secEmpresa = administrarVigenciasCargos.consultarEmpresaPorEmpl(empleado.getSecuencia());
         System.out.println("sec empresa : " + secEmpresa);
         if (secEmpresa != null) {
            lovPapeles = administrarVigenciasCargos.lovPapeles(secEmpresa.toBigInteger());
         }
      }
      return lovPapeles;
   }

   public void setLovPapeles(List<Papeles> lovPapeles) {
      this.lovPapeles = lovPapeles;
   }

   public List<Papeles> getLovPapelesFiltrar() {
      return lovPapelesFiltrar;
   }

   public void setLovPapelesFiltrar(List<Papeles> lovPapelesFiltrar) {
      this.lovPapelesFiltrar = lovPapelesFiltrar;
   }

   public Papeles getPapelSeleccionado() {
      return papelSeleccionado;
   }

   public void setPapelSeleccionado(Papeles papelSeleccionado) {
      this.papelSeleccionado = papelSeleccionado;
   }

   public String getInfoRegistroPapel() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovPapeles");
      infoRegistroPapel = String.valueOf(tabla.getRowCount());
      return infoRegistroPapel;
   }

   public void setInfoRegistroPapel(String infoRegistroPapel) {
      this.infoRegistroPapel = infoRegistroPapel;
   }

}
