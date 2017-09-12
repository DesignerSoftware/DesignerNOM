/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.NovedadesSistema;
import Entidades.Vacaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesSistemaInterface;
import InterfaceAdministrar.AdministrarNovedadesVacacionesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import InterfaceAdministrar.AdministrarEmplVigenciasFormasPagosInterface;
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
public class ControlNovedadesVacaciones implements Serializable {

   private static Logger log = Logger.getLogger(ControlNovedadesVacaciones.class);

   @EJB
   AdministrarNovedadesVacacionesInterface administrarNovedadesVacaciones;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarNovedadesSistemaInterface administrarNovedadesSistema;
   @EJB
   AdministrarEmplVigenciasFormasPagosInterface administrarVigFormasPagos;
   //SECUENCIA DE LA PERSONA
   private BigInteger secuenciaEmpleado;
   //Lista Empleados Novedad Vacaciones
   private List<Empleados> listaEmpleadosNovedad;
   private List<Empleados> filtradosListaEmpleadosNovedad;
   private Empleados empleadoSeleccionado;
   //LISTA NOVEDADES
   private List<NovedadesSistema> listaNovedades;
   private List<NovedadesSistema> filtradosListaNovedades;
   private NovedadesSistema novedadSeleccionada;
   //Duplicar
   private NovedadesSistema duplicarNovedad;
   //editar celda
   private NovedadesSistema editarNovedades;
   private int cualCelda, tipoLista, tipoListaNov;
   //Crear Novedades
   private List<NovedadesSistema> listaNovedadesCrear;
   public NovedadesSistema nuevaNovedad;
   private int paraNuevaNovedad;
   private BigInteger nuevaNovedadSec;
   private String mensajeValidacion;
   //Modificar Novedades
   private List<NovedadesSistema> listaNovedadesModificar;
   //Borrar Novedades
   private List<NovedadesSistema> listaNovedadesBorrar;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   private boolean permitirIndex;
   //RASTROS
   private boolean guardado;
   //guardarOk;
   //LOV EMPLEADOS
   private List<Empleados> lovEmpleados;
   private List<Empleados> lovEmpleadosFiltrar;
   private Empleados empleadoSeleccionadoLOV;
   //LOV PERIODOS
   private List<Vacaciones> lovPeriodos;
   private List<Vacaciones> lovPeriodosFiltrar;
   private Vacaciones periodoSeleccionado;
   //Columnas Tabla NOVEDADES
   private Column nEFechasIngreso, nEEmpleadosNombres;
   private Column nEFechaInicialDisfrute, nEPeriodo, nEDias, nEFechaSiguiente,
           nESubTipo, nEAdelantoHasta, nEFechaPago, nEDiasAplazados;
   private Date suma364;
   private Date finalsuma364;
   private BigInteger diasTotales;
   private BigInteger diasAplazadosTotal;
   private final String cero;
   //ALTO SCROLL TABLA
   private String altoTablaEmp, altoTablaRegEmp, altoTablaAux;// altoTablaEmp, altoTablaRegEmp;
   // activar mostrar todos:
   private boolean activarMTodos, activarLOV;
   // fecha contratacion empleado
   private Date fechaContratacionE;
   private String infoRegistroEmpleados, infoRegistroNovedades, infoRegistroEmpleadosLOV, infoRegistroPeriodo;
   private BigInteger periodicidadEmpleado;
   private BigInteger periodicidadCodigoDos;
   private Date fechaUltimoCorte;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private ListasRecurrentes listasRecurrentes;

   public ControlNovedadesVacaciones() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();

      cero = "0";
      listaEmpleadosNovedad = null;
      listaNovedadesBorrar = new ArrayList<NovedadesSistema>();
      listaNovedadesCrear = new ArrayList<NovedadesSistema>();
      listaNovedadesModificar = new ArrayList<NovedadesSistema>();
      aceptar = true;
      guardado = true;
      tipoLista = 0;
      tipoListaNov = 0;
      lovEmpleados = null;
      permitirIndex = true;
      nuevaNovedad = new NovedadesSistema();
      nuevaNovedad.setSubtipo("TIEMPO");
      nuevaNovedad.setTipo("VACACION");
      nuevaNovedad.setVacacion(new Vacaciones());
      nuevaNovedad.setDias(BigInteger.valueOf(0));
      nuevaNovedad.setPagado("N");
      nuevaNovedad.setEstado("ABIERTO");
      duplicarNovedad = new NovedadesSistema();
      duplicarNovedad.setSubtipo("TIEMPO");
      duplicarNovedad.setTipo("VACACION");
      duplicarNovedad.setVacacion(new Vacaciones());
      duplicarNovedad.setDias(BigInteger.valueOf(0));
      diasTotales = BigInteger.valueOf(0);
      diasAplazadosTotal = BigInteger.ZERO;
      altoTablaEmp = "110";
      altoTablaAux = "100";
      altoTablaRegEmp = "6";
      paginaAnterior = "nominaf";
      activarMTodos = true;
      novedadSeleccionada = null;
      periodicidadCodigoDos = BigInteger.valueOf(19847);
      activarLOV = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarNovedadesVacaciones.obtenerConexion(ses.getId());
         administrarNovedadesSistema.obtenerConexion(ses.getId());
         administrarVigFormasPagos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         getListaEmpleadosNovedad();
         if (listaEmpleadosNovedad != null) {
            if (!listaEmpleadosNovedad.isEmpty()) {
               empleadoSeleccionado = listaEmpleadosNovedad.get(0);
            }
         }
         listaNovedades = null;
         getListaNovedades();
         if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
            secuenciaEmpleado = empleadoSeleccionado.getSecuencia();
            listaNovedades = administrarNovedadesSistema.vacacionesEmpleado(secuenciaEmpleado);
            if (listaNovedades != null) {
               if (!listaNovedades.isEmpty()) {
                  for (int i = 0; i < listaNovedades.size(); i++) {
                     if (listaNovedades.get(i).getDias() != null) {
                        diasTotales = diasTotales.add(listaNovedades.get(i).getDias());
                     }
                     if (listaNovedades.get(i).getVacadiasaplazados() != null) {
                        diasAplazadosTotal = diasAplazadosTotal.add(listaNovedades.get(i).getVacadiasaplazados());
                     }
                  }
               }
            }
         }
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
      String pagActual = "novedadvacacionesarmor";
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
      lovEmpleados = null;
      lovPeriodos = null;
   }

   public void recibirPag(String pag) {
      paginaAnterior = pag;
   }

   public String volverPagAnterior() {
      return paginaAnterior;
   }

   public void editarCelda() {
      if (novedadSeleccionada != null) {
         editarNovedades = novedadSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaInicial");
            RequestContext.getCurrentInstance().execute("PF('editFechaInicial').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPeriodo");
            RequestContext.getCurrentInstance().execute("PF('editPeriodo').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDias");
            RequestContext.getCurrentInstance().execute("PF('editDias').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaSiguiente");
            RequestContext.getCurrentInstance().execute("PF('editFechaSiguiente').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editSubtipo");
            RequestContext.getCurrentInstance().execute("PF('editSubtipo').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editAdelantoHasta");
            RequestContext.getCurrentInstance().execute("PF('editAdelantoHasta').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFechaPago");
            RequestContext.getCurrentInstance().execute("PF('editFechaPago').show()");
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDA");
            RequestContext.getCurrentInstance().execute("PF('editarDA').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //LIMPIAR NUEVO REGISTRO NOVEDAD
   public void limpiarNuevaNovedad() {
      String cero = "0";
      nuevaNovedad = new NovedadesSistema();
      nuevaNovedad.setSubtipo("TIEMPO");
      nuevaNovedad.setTipo("VACACION");
      nuevaNovedad.setVacacion(new Vacaciones());
      nuevaNovedad.setDias(BigInteger.valueOf(0));
   }

   public void limpiarduplicarNovedades() {
      String cero = "0";
      duplicarNovedad = new NovedadesSistema();
      duplicarNovedad.setSubtipo("TIEMPO");
      duplicarNovedad.setTipo("VACACION");
      duplicarNovedad.setVacacion(new Vacaciones());
      duplicarNovedad.setDias(BigInteger.valueOf(0));
   }

   //CREAR NOVEDADES
   public void agregarNuevaNovedadVacaciones() {
      int pasa = 0;
//        Empleados emp = new Empleados();
      mensajeValidacion = new String();
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevaNovedad.getFechainicialdisfrute() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial Disfrute\n";
         pasa++;
      }
      if (nuevaNovedad.getDias() == null) {
         mensajeValidacion = mensajeValidacion + " * Dias\n";
         pasa++;
      }

      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
      }

      if (pasa == 0) {
         if (bandera == 1) {
            cargarTablaDefault();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         paraNuevaNovedad++;
         nuevaNovedadSec = BigInteger.valueOf(paraNuevaNovedad);
         nuevaNovedad.setSecuencia(nuevaNovedadSec);
         nuevaNovedad.setEmpleado(empleadoSeleccionado);
         nuevaNovedad.setFechasistema(new Date());
         nuevaNovedad.setEstado("ABIERTO");
         nuevaNovedad.setAdelantapago("N");
         nuevaNovedad.setPagado("N");
         nuevaNovedad.setPagarporfuera("N");
         listaNovedadesCrear.add(nuevaNovedad);
         if (listaNovedades == null) {
            listaNovedades = new ArrayList<NovedadesSistema>();
         }
         listaNovedades.add(nuevaNovedad);
         novedadSeleccionada = nuevaNovedad;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
      RequestContext.getCurrentInstance().execute("PF('nuevanovedadVacaciones').hide()");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:diasTotales");
      RequestContext.getCurrentInstance().update("form:diasAplazados");
      String cero = "0";
      nuevaNovedad = new NovedadesSistema();
      nuevaNovedad.setSubtipo("TIEMPO");
      nuevaNovedad.setTipo("VACACION");
      nuevaNovedad.setVacacion(new Vacaciones());
      nuevaNovedad.setDias(BigInteger.valueOf(0));
   }

   public void duplicarNV() {
      if (novedadSeleccionada != null) {
         duplicarNovedad = new NovedadesSistema();
         paraNuevaNovedad++;
         nuevaNovedadSec = BigInteger.valueOf(paraNuevaNovedad);
         Empleados emple = new Empleados();
         cargarLovEmpleados();
         for (int i = 0; i < lovEmpleados.size(); i++) {
            if (empleadoSeleccionado.getSecuencia().compareTo(lovEmpleados.get(i).getSecuencia()) == 0) {
               emple = lovEmpleados.get(i);
            }
         }
         duplicarNovedad.setSecuencia(nuevaNovedadSec);
         duplicarNovedad.setEmpleado(emple);
         duplicarNovedad.setVacacion(nuevaNovedad.getVacacion());
         duplicarNovedad.setFechainicialdisfrute(novedadSeleccionada.getFechainicialdisfrute());
         duplicarNovedad.getVacacion().setPeriodo(novedadSeleccionada.getVacacion().getPeriodo());
         duplicarNovedad.setEstado(novedadSeleccionada.getEstado());
         duplicarNovedad.setDias(novedadSeleccionada.getDias());
         duplicarNovedad.setFechasiguientefinvaca(novedadSeleccionada.getFechasiguientefinvaca());
         duplicarNovedad.setSubtipo(novedadSeleccionada.getSubtipo());
         duplicarNovedad.setAdelantapagohasta(novedadSeleccionada.getAdelantapagohasta());
         duplicarNovedad.setFechapago(novedadSeleccionada.getFechapago());
         duplicarNovedad.setVacadiasaplazados(novedadSeleccionada.getVacadiasaplazados());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
         RequestContext.getCurrentInstance().execute("PF('duplicarregistroNovedad').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void entrarNuevoRegistro() {
      if (empleadoSeleccionado != null) {
         fechaContratacionE = administrarNovedadesVacaciones.obtenerFechaContratacionEmpleado(empleadoSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
         RequestContext.getCurrentInstance().execute("PF('nuevanovedadVacaciones').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int pasa = 0;
      if (duplicarNovedad.getFechainicialdisfrute() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (duplicarNovedad.getEmpleado() == null) {
         mensajeValidacion = mensajeValidacion + " * Empleado\n";
         pasa++;
      }
      if (duplicarNovedad.getDias() == null) {
         mensajeValidacion = mensajeValidacion + " * Formula\n";
         pasa++;
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
      }
      if (pasa == 0) {
         listaNovedades.add(duplicarNovedad);
         listaNovedadesCrear.add(duplicarNovedad);
         novedadSeleccionada = duplicarNovedad;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            cargarTablaDefault();
         }
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         duplicarNovedad = new NovedadesSistema();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarregistroNovedad");
         RequestContext.getCurrentInstance().execute("PF('duplicarregistroNovedad').hide()");
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "NovedadVacacionesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "NovedadVacacionesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTablaEmp = "92";
         altoTablaAux = "80";
         altoTablaRegEmp = "5";
         nEFechaInicialDisfrute = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaInicialDisfrute");
         nEFechaInicialDisfrute.setFilterStyle("width: 85% !important");
         nEPeriodo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEPeriodo");
         nEPeriodo.setFilterStyle("width: 85% !important");
         nEDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDias");
         nEDias.setFilterStyle("width: 75% !important");
         nEFechaSiguiente = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaSiguiente");
         nEFechaSiguiente.setFilterStyle("width: 85% !important");
         nESubTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nESubTipo");
         nESubTipo.setFilterStyle("width: 85% !important");
         nEAdelantoHasta = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEAdelantoHasta");
         nEAdelantoHasta.setFilterStyle("width: 85% !important");
         nEFechaPago = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaPago");
         nEFechaPago.setFilterStyle("width: 85% !important");
         nEDiasAplazados = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDiasAplazados");
         nEDiasAplazados.setFilterStyle("width: 75% !important");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         bandera = 1;
         nEEmpleadosNombres = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosNombres");
         nEEmpleadosNombres.setFilterStyle("width: 85% !important");
         nEFechasIngreso = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEFechasIngreso");
         nEFechasIngreso.setFilterStyle("width: 80% !important");
         RequestContext.getCurrentInstance().update("form:datosEmpleados");
      } else if (bandera == 1) {
         cargarTablaDefault();
      }
      contarRegistroEmpleados();
      contarRegistrosNovedades();
   }

   public void cargarTablaDefault() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTablaEmp = "110";
      altoTablaAux = "100";
      altoTablaRegEmp = "6";
      nEFechaInicialDisfrute = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaInicialDisfrute");
      nEFechaInicialDisfrute.setFilterStyle("display: none; visibility: hidden;");
      nEPeriodo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEPeriodo");
      nEPeriodo.setFilterStyle("display: none; visibility: hidden;");
      nEDias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDias");
      nEDias.setFilterStyle("display: none; visibility: hidden;");
      nEFechaSiguiente = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaSiguiente");
      nEFechaSiguiente.setFilterStyle("display: none; visibility: hidden;");
      nESubTipo = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nESubTipo");
      nESubTipo.setFilterStyle("display: none; visibility: hidden;");
      nEAdelantoHasta = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEAdelantoHasta");
      nEAdelantoHasta.setFilterStyle("display: none; visibility: hidden;");
      nEFechaPago = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEFechaPago");
      nEFechaPago.setFilterStyle("display: none; visibility: hidden;");
      nEDiasAplazados = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:nEDiasAplazados");
      nEDiasAplazados.setFilterStyle("display: none; visibility: hidden;");
      bandera = 0;
      nEEmpleadosNombres = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosNombres");
      nEEmpleadosNombres.setFilterStyle("display: none; visibility: hidden;");
      nEFechasIngreso = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEFechasIngreso");
      nEFechasIngreso.setFilterStyle("display: none; visibility: hidden;");
      filtradosListaNovedades = null;
      filtradosListaEmpleadosNovedad = null;
      RequestContext.getCurrentInstance().execute("PF('datosNovedadesEmpleado').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('datosEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      tipoLista = 0;
      tipoListaNov = 0;
   }

   public void mostrarTodos() {
      if (!listaEmpleadosNovedad.isEmpty()) {
         listaEmpleadosNovedad.clear();
      }
      if (lovEmpleados != null) {
         for (int i = 0; i < lovEmpleados.size(); i++) {
            listaEmpleadosNovedad.add(lovEmpleados.get(i));
         }
      }
      empleadoSeleccionado = listaEmpleadosNovedad.get(0);
      listaNovedades = administrarNovedadesSistema.vacacionesEmpleado(empleadoSeleccionado.getSecuencia());
      getDiasTotales();
      filtradosListaEmpleadosNovedad = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      activarMTodos = true;
      contarRegistroEmpleados();
      contarRegistrosNovedades();
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:diasTotales");
      RequestContext.getCurrentInstance().update("form:diasAplazados");
      RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
   }

   //Ubicacion Celda Indice Abajo. //Van los que no son NOT NULL.
   public void cambiarIndice(NovedadesSistema novedadS, int celda) {
      if (permitirIndex == true) {
         novedadSeleccionada = novedadS;
         cualCelda = celda;
         novedadSeleccionada.getSecuencia();
         if (cualCelda == 0) {
            novedadSeleccionada.getFechainicialdisfrute();
         } else if (cualCelda == 1) {
            novedadSeleccionada.getVacacion().getPeriodo();
         } else if (cualCelda == 2) {
            novedadSeleccionada.getDias();
         } else if (cualCelda == 3) {
            novedadSeleccionada.getFechasiguientefinvaca();
//                RequestContext.getCurrentInstance().execute("PF('proceso').show()");
         } else if (cualCelda == 4) {
            novedadSeleccionada.getSubtipo();
         } else if (cualCelda == 5) {
            novedadSeleccionada.getAdelantapagohasta();
         } else if (cualCelda == 6) {
            novedadSeleccionada.getFechapago();
         } else if (cualCelda == 7) {
            novedadSeleccionada.getVacadiasaplazados();
         }
      }
   }

   //Ubicacion Celda Arriba 
   public void cambiarEmpleado() {
      listaNovedades = null;
      if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
         novedadSeleccionada = null;
         getListaNovedades();
         if (tipoListaNov == 1) {
            RequestContext.getCurrentInstance().execute("PF('datosNovedadesEmpleado').clearFilters()");
         }
         contarRegistrosNovedades();
         getDiasTotales();
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         RequestContext.getCurrentInstance().update("form:diasTotales");
         RequestContext.getCurrentInstance().update("form:diasAplazados");
      }
   }

   public void abrirLista(int listaV) {

      RequestContext context = RequestContext.getCurrentInstance();
      if (listaV == 0) {
         cargarLovEmpleados();
         contarRegistroEmpleadosLOV();
         RequestContext.getCurrentInstance().update("formLovEmpleado:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      }
   }

   public void actualizarEmpleadosNovedad() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listaEmpleadosNovedad.isEmpty()) {
         listaEmpleadosNovedad.clear();
         listaEmpleadosNovedad.add(empleadoSeleccionadoLOV);
         empleadoSeleccionado = listaEmpleadosNovedad.get(0);
      }
      secuenciaEmpleado = empleadoSeleccionadoLOV.getSecuencia();
      listaNovedades = administrarNovedadesSistema.vacacionesEmpleado(empleadoSeleccionado.getSecuencia());
      // Se recargan las novedades:
      getDiasTotales();
      aceptar = true;
      filtradosListaEmpleadosNovedad = null;
      //empleadoSeleccionadoLOV = null;
      novedadSeleccionada = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      activarMTodos = false;
      contarRegistroEmpleados();
      contarRegistrosNovedades();
      context.reset("formLovEmpleado:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovEmpleado:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formLovEmpleado:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formLovEmpleado:aceptarE");
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");

      RequestContext.getCurrentInstance().update("form:diasTotales");
      RequestContext.getCurrentInstance().update("form:diasAplazados");
      RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
   }

   //BORRAR NOVEDADES
   public void borrarNovedades() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (novedadSeleccionada != null) {
         if (!listaNovedadesModificar.isEmpty() && listaNovedadesModificar.contains(novedadSeleccionada)) {
            int modIndex = listaNovedadesModificar.indexOf(novedadSeleccionada);
            listaNovedadesModificar.remove(modIndex);
            listaNovedadesBorrar.add(novedadSeleccionada);
         } else if (!listaNovedadesCrear.isEmpty() && listaNovedadesCrear.contains(novedadSeleccionada)) {
            int crearIndex = listaNovedadesCrear.indexOf(novedadSeleccionada);
            listaNovedadesCrear.remove(crearIndex);
         } else {
            listaNovedadesBorrar.add(novedadSeleccionada);
         }
         listaNovedades.remove(novedadSeleccionada);

         if (tipoListaNov == 1) {
            filtradosListaNovedades.remove(novedadSeleccionada);
         }
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         novedadSeleccionada = null;

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //GUARDAR
   public void guardarCambiosNovedades() {
      Empleados emp = new Empleados();
      if (guardado == false) {

         if (!listaNovedadesBorrar.isEmpty()) {
            for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
               if (listaNovedadesBorrar.get(i).getVacacion() == null) {
                  listaNovedadesBorrar.get(i).setVacacion(new Vacaciones());
               }

               if (listaNovedadesBorrar.get(i).getFechasiguientefinvaca() == null) {
                  listaNovedadesBorrar.get(i).setFechasiguientefinvaca(null);
               }
               if (listaNovedadesBorrar.get(i).getFechapago() == null) {
                  listaNovedadesBorrar.get(i).setFechapago(null);
               }
               if (listaNovedadesBorrar.get(i).getAdelantapagohasta() == null) {
                  listaNovedadesBorrar.get(i).setAdelantapagohasta(null);
               }
               administrarNovedadesSistema.borrarNovedades(listaNovedadesBorrar.get(i));
            }
            listaNovedadesBorrar.clear();
         }

         if (!listaNovedadesCrear.isEmpty()) {
            for (int i = 0; i < listaNovedadesCrear.size(); i++) {
               if (listaNovedadesCrear.get(i).getVacacion() == null) {
                  listaNovedadesCrear.get(i).setVacacion(new Vacaciones());
               }

               if (listaNovedadesCrear.get(i).getFechasiguientefinvaca() == null) {
                  listaNovedadesCrear.get(i).setFechasiguientefinvaca(null);
               }
               if (listaNovedadesCrear.get(i).getFechapago() == null) {
                  listaNovedadesCrear.get(i).setFechapago(null);
               }
               if (listaNovedadesCrear.get(i).getAdelantapagohasta() == null) {
                  listaNovedadesCrear.get(i).setAdelantapagohasta(null);
               }
               administrarNovedadesSistema.crearNovedades(listaNovedadesCrear.get(i));
            }
            listaNovedadesCrear.clear();
         }
         limpiarNuevaNovedad();
         limpiarduplicarNovedades();
         listaNovedades = null;
         getListaNovedades();
         contarRegistrosNovedades();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         guardado = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         //  k = 0;
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void mostrarDialogoProceso() {
      RequestContext.getCurrentInstance().execute("PF('proceso').show()");
   }

   public void cancelarCambioEmpleados() {
      lovEmpleadosFiltrar = null;
      empleadoSeleccionadoLOV = null;
      aceptar = true;
      novedadSeleccionada = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovEmpleado:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovEmpleado:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formLovEmpleado:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formLovEmpleado:aceptarE");
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada != null) {
         int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADESSISTEMA");
         if (result == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (result == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (result == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (result == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (result == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("NOVEDADESSISTEMA")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //CANCELAR MODIFICACIONES
   public void cancelarModificacion() {
      if (bandera == 1) {
         cargarTablaDefault();
      }
      aceptar = true;
      guardado = true;
      tipoLista = 0;
      tipoListaNov = 0;
      permitirIndex = true;
      limpiarNuevaNovedad();
      limpiarduplicarNovedades();
      diasTotales = BigInteger.valueOf(0);
      diasAplazadosTotal = BigInteger.ZERO;
      altoTablaEmp = "110";
      altoTablaAux = "100";
      altoTablaRegEmp = "6";
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      listaNovedades = null;
      lovEmpleados = null;
      activarMTodos = true;
      empleadoSeleccionado = null;
      novedadSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         cargarTablaDefault();
      }
      listaEmpleadosNovedad = null;
      aceptar = true;
      guardado = true;
      tipoLista = 0;
      tipoListaNov = 0;
      lovEmpleados = null;
      permitirIndex = true;
      limpiarNuevaNovedad();
      limpiarduplicarNovedades();
      diasTotales = BigInteger.valueOf(0);
      diasAplazadosTotal = BigInteger.ZERO;
      altoTablaEmp = "110";
      altoTablaAux = "100";
      altoTablaRegEmp = "6";
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      novedadSeleccionada = null;
      listaNovedades = null;
      activarMTodos = true;
      empleadoSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      navegar("atras");
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      if (tipoListaNov == 0) {
         tipoListaNov = 1;
      }
      novedadSeleccionada = null;
      contarRegistrosNovedades();
   }

   public void eventoFiltrarEmpleados() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      empleadoSeleccionado = null;
      novedadSeleccionada = null;
      listaNovedades = null;
      filtradosListaNovedades = null;
      getDiasTotales();
      if (tipoListaNov == 1) {
         RequestContext.getCurrentInstance().execute("PF('datosNovedadesEmpleado').clearFilters()");
      }
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      RequestContext.getCurrentInstance().update("form:diasTotales");
      RequestContext.getCurrentInstance().update("form:diasAplazados");
      contarRegistrosNovedades();
      contarRegistroEmpleados();
   }

   public void contarRegistroEmpleados() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
   }

   public void contarRegistrosNovedades() {
      RequestContext.getCurrentInstance().update("form:infoRegistroNovedades");
   }

   public void contarRegistroEmpleadosLOV() {
      RequestContext.getCurrentInstance().update("formLovEmpleado:infoRegistroEmpleadosLOV");
   }

   public void contarRegistroPeriodos() {
      RequestContext.getCurrentInstance().update("formLovPeriodo:infoRegistroPeriodosLOV");

   }

   public void posicionOtro() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String celda = map.get("celda"); // name attribute of node 
      String registro = map.get("registro"); // type attribute of node 
      int indice = Integer.parseInt(registro);
      int columna = Integer.parseInt(celda);
      novedadSeleccionada = listaNovedades.get(indice);
      cambiarIndice(novedadSeleccionada, columna);
   }

   public void actualizarPeriodos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
            novedadSeleccionada.getVacacion().setPeriodo(periodoSeleccionado.getPeriodo());
            if (listaNovedadesModificar.isEmpty()) {
               listaNovedadesModificar.add(novedadSeleccionada);
            } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
               listaNovedadesModificar.add(novedadSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      }
      if (tipoActualizacion == 1) {
         nuevaNovedad.setVacacion(periodoSeleccionado);
         nuevaNovedad.getVacacion().setPeriodo(periodoSeleccionado.getPeriodo());
         nuevaNovedad.setDias(periodoSeleccionado.getDiaspendientes().toBigInteger());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
         fechaSiguiente(1);
         fechaAdelantoHasta(nuevaNovedad.getFechasiguientefinvaca(), 1);
         RequestContext.getCurrentInstance().execute("PF('proceso').show()");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAdelantoHasta");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaPago");

      } else if (tipoActualizacion == 2) {
         duplicarNovedad.setVacacion(periodoSeleccionado);
         duplicarNovedad.getVacacion().setPeriodo(periodoSeleccionado.getPeriodo());
         duplicarNovedad.setDias(periodoSeleccionado.getDiaspendientes().toBigInteger());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
         fechaSiguiente(2);
         RequestContext.getCurrentInstance().execute("PF('proceso').show()");
         fechaAdelantoHasta(duplicarNovedad.getFechasiguientefinvaca(), 2);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAdelantoHasta");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFechaPago");
      }
      lovPeriodosFiltrar = null;
      periodoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formLovPeriodo:LOVPeriodos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovPeriodo:LOVPeriodos");
      RequestContext.getCurrentInstance().update("formLovPeriodo:periodosDialogo");
      RequestContext.getCurrentInstance().update("formLovPeriodo:aceptarP");
   }

   public void cancelarCambioPeriodos() {
      lovPeriodosFiltrar = null;
      periodoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formLovPeriodo:LOVPeriodos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVPeriodos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formLovPeriodo:LOVPeriodos");
      RequestContext.getCurrentInstance().update("formLovPeriodo:periodosDialogo");
      RequestContext.getCurrentInstance().update("formLovPeriodo:aceptarP");

   }

   public Date fechaAdelantoHasta(Date fechaRegreso, int tipo) {
      Calendar cd = Calendar.getInstance();
      cd.setTime(fechaRegreso);
      cd.add(Calendar.DAY_OF_MONTH, -1);
      Date fechaAdelantoH = cd.getTime();
      if (tipo == 1) {
         nuevaNovedad.setAdelantapagohasta(fechaAdelantoH);
      } else if (tipo == 2) {
         duplicarNovedad.setAdelantapagohasta(fechaAdelantoH);
      }
      fechaslimite(fechaRegreso);
      return fechaAdelantoH;
   }

   public void fechaslimite(Date fecharegreso) {
      Date fechalimite = administrarNovedadesVacaciones.anteriorFechaLimite(fecharegreso, periodicidadCodigoDos);
      Date siguientelimite = administrarNovedadesVacaciones.despuesFechaLimite(fecharegreso, periodicidadCodigoDos);
   }

   public Date fechaSiguiente(int tipo) {
      this.tipoActualizacion = tipo;
      Date fechaSiguiente = new Date();
      BigDecimal jornada = administrarNovedadesVacaciones.validarJornadaVacaciones(secuenciaEmpleado, nuevaNovedad.getFechainicialdisfrute());
      if (tipoActualizacion == 1) {
         fechaSiguiente = administrarNovedadesVacaciones.fechaSiguiente(nuevaNovedad.getFechainicialdisfrute(), nuevaNovedad.getDias(), jornada);
         nuevaNovedad.setFechasiguientefinvaca(fechaSiguiente);
      } else if (tipoActualizacion == 2) {
         fechaSiguiente = administrarNovedadesVacaciones.fechaSiguiente(duplicarNovedad.getFechainicialdisfrute(), duplicarNovedad.getDias(), jornada);
         duplicarNovedad.setFechasiguientefinvaca(fechaSiguiente);
      }
      return fechaSiguiente;
   }

   public void validacionProcesoVacaciones(String vacdifnomina) {
      Date siguientefechalimite;
      Date siguientefechalimite2;
      Date fechaUltimoCorteNomina = administrarNovedadesVacaciones.fechaUltimoCorte(secuenciaEmpleado, 1);
      Date fechaUltimoCorteUnidades = administrarNovedadesVacaciones.fechaUltimoCorte(secuenciaEmpleado, 80);
      fechaUltimoCorte = administrarNovedadesVacaciones.fechaUltimoCorte(secuenciaEmpleado, 1);

      if (fechaUltimoCorteNomina != null && fechaUltimoCorteUnidades != null) {
         if (fechaUltimoCorteNomina.compareTo(fechaUltimoCorteUnidades) > 0) {
            fechaUltimoCorte = fechaUltimoCorteNomina;
         } else {
            fechaUltimoCorte = fechaUltimoCorteUnidades;
         }
      } else {
         fechaUltimoCorte = fechaUltimoCorteNomina;
      }

      Calendar media = Calendar.getInstance();
      media.setTime(fechaUltimoCorte);
      media.add(Calendar.DAY_OF_MONTH, 1);
      Date aux = media.getTime();

      siguientefechalimite = administrarNovedadesVacaciones.despuesFechaLimite(aux, periodicidadCodigoDos);

      Calendar pnomina = Calendar.getInstance();
      pnomina.setTime(siguientefechalimite);
      pnomina.add(Calendar.DAY_OF_MONTH, 1);
      Date aux2 = pnomina.getTime();

      siguientefechalimite2 = administrarNovedadesVacaciones.despuesFechaLimite(aux2, periodicidadCodigoDos);

      if (vacdifnomina.equals("S")) {
         nuevaNovedad.setFechapago(siguientefechalimite);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaPago");
      } else if (vacdifnomina.equals("N")) {
         BigDecimal periodicidadaux = administrarVigFormasPagos.consultarPeriodicidadPorEmpl(secuenciaEmpleado);
         if (periodicidadaux.toBigInteger().equals(periodicidadCodigoDos)) {
            nuevaNovedad.setFechapago(siguientefechalimite);
         } else if (periodicidadaux.toBigInteger().equals(BigInteger.valueOf(19845))) {
            nuevaNovedad.setFechapago(siguientefechalimite2);
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaPago");
      }
   }

   public void validaciondias() {
      periodicidadEmpleado = administrarNovedadesVacaciones.periodicidadEmpleado(secuenciaEmpleado);
   }

   public void agregarPeriodo(BigInteger secuenciaEmpleado) {
      administrarNovedadesVacaciones.adelantarPeriodo(secuenciaEmpleado);
      lovPeriodos = administrarNovedadesSistema.periodosEmpleado(secuenciaEmpleado);
      getLovPeriodos();
      RequestContext.getCurrentInstance().update("formLovPeriodo:periodosDialogo");
      RequestContext.getCurrentInstance().execute("PF('periodosDialogo').show()");
   }

   public void lovPeriodo(BigInteger secuenciaEmpleado, int tipoAct) {
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = tipoAct;
      lovPeriodos = null;
      cargarLovPeriodos();
      if (nuevaNovedad.getDias() == null || nuevaNovedad.getDias() == BigInteger.ZERO) {
         if (lovPeriodos.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:dias");
            RequestContext.getCurrentInstance().execute("PF('dias').show()");
         } else {
            FacesContext c = FacesContext.getCurrentInstance();
            periodoSeleccionado = null;
            contarRegistroPeriodos();
            RequestContext.getCurrentInstance().update("formLovPeriodo:periodosDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodosDialogo').show()");
         }
      } else {
         contarRegistroPeriodos();
         periodoSeleccionado = null;
         RequestContext.getCurrentInstance().update("formLovPeriodo:periodosDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodosDialogo').show()");
      }
   }

   public void diasMayor(BigInteger dias, int tipoact) {
      RequestContext context = RequestContext.getCurrentInstance();
      BigInteger backup = dias;

      if (tipoact == 1) {
         if (nuevaNovedad.getDias().compareTo(BigInteger.valueOf(15)) == 1) { //// si el resultado es uno e sporque nueva novedad . get dias es mayor a 15
            RequestContext.getCurrentInstance().update("formularioDialogos:diasMayor");
            nuevaNovedad.setDias(BigInteger.ZERO);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
            RequestContext.getCurrentInstance().execute("PF('diasMayor').show()");
         } else {
            nuevaNovedad.setDias(dias);
            fechaSiguiente(1);
            fechaAdelantoHasta(nuevaNovedad.getFechasiguientefinvaca(), 1);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAdelantoHasta");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaSiguiente");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaPago");
         }
      } else if (tipoact == 2) {
         if (duplicarNovedad.getDias().compareTo(BigInteger.valueOf(15)) == 1) { //// si el resultado es uno e sporque nueva novedad . get dias es mayor a 15
            RequestContext.getCurrentInstance().update("formularioDialogos:diasMayor");
            duplicarNovedad.setDias(BigInteger.ZERO);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
            RequestContext.getCurrentInstance().execute("PF('diasMayor').show()");
         } else {
            duplicarNovedad.setDias(dias);

            fechaSiguiente(2);
            fechaAdelantoHasta(duplicarNovedad.getFechasiguientefinvaca(), 2);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFechaSiguiente");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAdelantoHasta");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFechaPago");
         }
      }
   }

   public void validaciones(Integer tipoAct) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoAct == 1) {
         if (nuevaNovedad.getFechainicialdisfrute().before(fechaContratacionE)) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacion1");
            nuevaNovedad.setFechainicialdisfrute(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
            RequestContext.getCurrentInstance().execute("PF('validacion1').show()");
         }
      } else if (tipoAct == 2) {
         fechaContratacionE = administrarNovedadesVacaciones.obtenerFechaContratacionEmpleado(empleadoSeleccionado.getSecuencia());
         if (duplicarNovedad.getFechainicialdisfrute().before(fechaContratacionE)) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacion1");
            duplicarNovedad.setFechainicialdisfrute(null);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
            RequestContext.getCurrentInstance().execute("PF('validacion1').show()");
         }
      }
      BigDecimal jornada = administrarNovedadesVacaciones.validarJornadaVacaciones(secuenciaEmpleado, nuevaNovedad.getFechainicialdisfrute());
      if (administrarNovedadesVacaciones.validarFestivoVacaciones(nuevaNovedad.getFechainicialdisfrute(), jornada)) {
         RequestContext.getCurrentInstance().execute("PF('diaFestivo').show()");
      }
      int diaSemana1;
      String diaSemanaStr = "";
      GregorianCalendar c = new GregorianCalendar();
      c.setTime(nuevaNovedad.getFechainicialdisfrute());
      diaSemana1 = c.get(Calendar.DAY_OF_WEEK);
      switch (diaSemana1) {
         case 1:
            diaSemanaStr = "DOM";
            break;
         case 2:
            diaSemanaStr = "LUN";
            break;
         case 3:
            diaSemanaStr = "MAR";
            break;
         case 4:
            diaSemanaStr = "MIE";
            break;
         case 5:
            diaSemanaStr = "JUE";
            break;
         case 6:
            diaSemanaStr = "VIE";
            break;
         case 7:
            diaSemanaStr = "SAB";
            break;
      }

      if (administrarNovedadesVacaciones.validarDiaLaboralVacaciones(jornada, diaSemanaStr) == true) {
         RequestContext.getCurrentInstance().execute("PF('diaNoLaboral').show()");
      }
   }

   public void seleccionarSubtipo(String subtipo, int tipo) {
      if (subtipo.equalsIgnoreCase("DINERO")) {
         if (tipo == 1) {
            Date vacDinero;
            Calendar tipoD = Calendar.getInstance();
            tipoD.setTime(nuevaNovedad.getFechainicialdisfrute());
            tipoD.add(Calendar.DAY_OF_MONTH, 1);
            vacDinero = tipoD.getTime();
            nuevaNovedad.setFechasiguientefinvaca(vacDinero);
            Date vacD;
            Calendar aux = Calendar.getInstance();
            aux.setTime(nuevaNovedad.getFechasiguientefinvaca());
            aux.add(Calendar.DAY_OF_MONTH, -1);
            vacD = aux.getTime();
            nuevaNovedad.setAdelantapagohasta(vacD);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaSiguiente");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAdelantoHasta");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaPago");
         } else if (tipo == 2) {
            Date vacDineroD;
            Calendar tipoD = Calendar.getInstance();
            tipoD.setTime(duplicarNovedad.getFechainicialdisfrute());
            tipoD.add(Calendar.DAY_OF_MONTH, 1);
            vacDineroD = tipoD.getTime();
            duplicarNovedad.setFechasiguientefinvaca(vacDineroD);
            Date vacDD;
            Calendar aux = Calendar.getInstance();
            aux.setTime(nuevaNovedad.getFechasiguientefinvaca());
            aux.add(Calendar.DAY_OF_MONTH, -1);
            vacDD = aux.getTime();
            duplicarNovedad.setAdelantapagohasta(vacDD);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFechaSiguiente");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAdelantoHasta");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFechaPago");
         }
      }
   }

   public void cargarLovEmpleados() {
      if (lovEmpleados == null) {
         if (listasRecurrentes.getLovEmpleadosActivos().isEmpty()) {
            lovEmpleados = administrarNovedadesVacaciones.empleadosVacaciones();
            if (lovEmpleados != null) {
               log.warn("GUARDANDO lovEmpleadosActivos en Listas recurrentes");
               listasRecurrentes.setLovEmpleadosActivos(lovEmpleados);
            }
         } else {
            lovEmpleados = new ArrayList<Empleados>(listasRecurrentes.getLovEmpleadosActivos());
            log.warn("CONSULTANDO lovEmpleadosActivos de Listas recurrentes");
         }
      }
   }

   public void cargarLovPeriodos() {
      if (lovPeriodos == null) {
         lovPeriodos = administrarNovedadesSistema.periodosEmpleado(secuenciaEmpleado);
      }
   }

//GETTER & SETTER
   public List<Empleados> getListaEmpleadosNovedad() {
      if (listaEmpleadosNovedad == null) {
         if (listasRecurrentes.getLovEmpleadosActivos().isEmpty()) {
            listaEmpleadosNovedad = administrarNovedadesVacaciones.empleadosVacaciones();
            if (listaEmpleadosNovedad != null) {
               log.warn("GUARDANDO lovEmpleadosActivos en Listas recurrentes");
               listasRecurrentes.setLovEmpleadosActivos(listaEmpleadosNovedad);
            }
         } else {
            listaEmpleadosNovedad = new ArrayList<Empleados>(listasRecurrentes.getLovEmpleadosActivos());
            log.warn("CONSULTANDO lovEmpleadosActivos de Listas recurrentes");
         }
      }
      return listaEmpleadosNovedad;
   }

   public void setListaEmpleadosNovedad(List<Empleados> listaEmpleadosNovedad) {
      this.listaEmpleadosNovedad = listaEmpleadosNovedad;
   }

   public List<Empleados> getFiltradosListaEmpleadosNovedad() {
      return filtradosListaEmpleadosNovedad;
   }

   public void setFiltradosListaEmpleadosNovedad(List<Empleados> filtradosListaEmpleadosNovedad) {
      this.filtradosListaEmpleadosNovedad = filtradosListaEmpleadosNovedad;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public List<Empleados> getLovEmpleados() {
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> listaEmpleados) {
      this.lovEmpleados = listaEmpleados;
   }

   public List<Empleados> getLovEmpleadosFiltrar() {
      return lovEmpleadosFiltrar;
   }

   public void setLovEmpleadosFiltrar(List<Empleados> lovEmpleadosFiltrar) {
      this.lovEmpleadosFiltrar = lovEmpleadosFiltrar;
   }

   public Empleados getSeleccionEmpleados() {
      return empleadoSeleccionadoLOV;
   }

   public void setSeleccionEmpleados(Empleados seleccionEmpleados) {
      this.empleadoSeleccionadoLOV = seleccionEmpleados;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public List<NovedadesSistema> getListaNovedades() {
      if (listaNovedades == null) {
         if (empleadoSeleccionado != null) {
            listaNovedades = administrarNovedadesSistema.vacacionesEmpleado(empleadoSeleccionado.getSecuencia());
         }
      }
      return listaNovedades;
   }

   public void setListaNovedades(List<NovedadesSistema> listaNovedades) {
      this.listaNovedades = listaNovedades;
   }

   public List<NovedadesSistema> getListaNovedadesCrear() {
      return listaNovedadesCrear;
   }

   public void setListaNovedadesCrear(List<NovedadesSistema> listaNovedadesCrear) {
      this.listaNovedadesCrear = listaNovedadesCrear;
   }

   public List<NovedadesSistema> getListaNovedadesModificar() {
      return listaNovedadesModificar;
   }

   public void setListaNovedadesModificar(List<NovedadesSistema> listaNovedadesModificar) {
      this.listaNovedadesModificar = listaNovedadesModificar;
   }

   public List<NovedadesSistema> getListaNovedadesBorrar() {
      return listaNovedadesBorrar;
   }

   public void setListaNovedadesBorrar(List<NovedadesSistema> listaNovedadesBorrar) {
      this.listaNovedadesBorrar = listaNovedadesBorrar;
   }

   public NovedadesSistema getEditarNovedades() {
      return editarNovedades;
   }

   public void setEditarNovedades(NovedadesSistema editarNovedades) {
      this.editarNovedades = editarNovedades;
   }

   public List<NovedadesSistema> getFiltradosListaNovedades() {
      return filtradosListaNovedades;
   }

   public void setFiltradosListaNovedades(List<NovedadesSistema> filtradosListaNovedades) {
      this.filtradosListaNovedades = filtradosListaNovedades;
   }

   public NovedadesSistema getNuevaNovedad() {
      return nuevaNovedad;
   }

   public void setNuevaNovedad(NovedadesSistema nuevaNovedad) {
      this.nuevaNovedad = nuevaNovedad;
   }

   public List<Vacaciones> getLovPeriodos() {
      return lovPeriodos;
   }

   public void setLovPeriodos(List<Vacaciones> lovPeriodos) {
      this.lovPeriodos = lovPeriodos;
   }

   public List<Vacaciones> getLovPeriodosFiltrar() {
      return lovPeriodosFiltrar;
   }

   public void setLovPeriodosFiltrar(List<Vacaciones> lovPeriodosFiltrar) {
      this.lovPeriodosFiltrar = lovPeriodosFiltrar;
   }

   public Vacaciones getPeriodoSeleccionado() {
      return periodoSeleccionado;
   }

   public void setPeriodoSeleccionado(Vacaciones seleccionPeriodo) {
      this.periodoSeleccionado = seleccionPeriodo;
   }

   public NovedadesSistema getDuplicarNovedad() {
      return duplicarNovedad;
   }

   public void setDuplicarNovedad(NovedadesSistema duplicarNovedad) {
      this.duplicarNovedad = duplicarNovedad;
   }

   public BigInteger getDiasTotales() {
      diasTotales = BigInteger.valueOf(0);
      diasAplazadosTotal = BigInteger.valueOf(0);
      if (listaNovedades != null) {
         if (!listaNovedades.isEmpty()) {
            for (int i = 0; i < listaNovedades.size(); i++) {
               if (listaNovedades.get(i).getDias() != null) {
                  diasTotales = diasTotales.add(listaNovedades.get(i).getDias());
                  if (listaNovedades.get(i).getVacadiasaplazados() != null) {
                     diasAplazadosTotal = diasAplazadosTotal.add(listaNovedades.get(i).getVacadiasaplazados());
                  }
               }
            }
         }
      } else if (listaNovedades == null) {
         contarRegistrosNovedades();
      }
      return diasTotales;
   }

   public void setDiasTotales(BigInteger diasTotales) {
      this.diasTotales = diasTotales;
   }

   public BigInteger getDiasAplazadosTotal() {
      return diasAplazadosTotal;
   }

   public void setDiasAplazadosTotal(BigInteger diasAplazadosTotal) {
      this.diasAplazadosTotal = diasAplazadosTotal;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public String getAltoTablaEmp() {
      return altoTablaEmp;
   }

   public String getAltoTablaRegEmp() {
      return altoTablaRegEmp;
   }

   public void setAltoTablaRegEmp(String altoTablaRegEmp) {
      this.altoTablaRegEmp = altoTablaRegEmp;
   }

   public NovedadesSistema getNovedadSeleccionada() {
      return novedadSeleccionada;
   }

   public void setNovedadSeleccionada(NovedadesSistema novedadSeleccionada) {
      this.novedadSeleccionada = novedadSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public boolean isActivarMTodos() {
      return activarMTodos;
   }

   public void setActivarMTodos(boolean activarMTodos) {
      this.activarMTodos = activarMTodos;
   }

   public String getInfoRegistroEmpleados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
      infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleados;
   }

   public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
      this.infoRegistroEmpleados = infoRegistroEmpleados;
   }

   public String getInfoRegistroNovedades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesEmpleado");
      infoRegistroNovedades = String.valueOf(tabla.getRowCount());
      return infoRegistroNovedades;
   }

   public void setInfoRegistroNovedades(String infoRegistroNovedades) {
      this.infoRegistroNovedades = infoRegistroNovedades;
   }

   public String getInfoRegistroEmpleadosLOV() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovEmpleado:LOVEmpleados");
      if (lovEmpleadosFiltrar != null) {
         if (lovEmpleadosFiltrar.size() == 1) {
            empleadoSeleccionadoLOV = lovEmpleadosFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();PF('LOVEmpleados').selectRow(0);");
         } else {
            empleadoSeleccionadoLOV = null;
            RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').unselectAllRows();");
         }
      } else {
         empleadoSeleccionadoLOV = null;
         aceptar = true;
      }
      infoRegistroEmpleadosLOV = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadosLOV;
   }

   public void setInfoRegistroEmpleadosLOV(String infoRegistroEmpleadosLOV) {
      this.infoRegistroEmpleadosLOV = infoRegistroEmpleadosLOV;
   }

   public String getInfoRegistroPeriodo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovPeriodo:LOVPeriodos");
      if (lovPeriodosFiltrar != null) {
         if (lovPeriodosFiltrar.size() == 1) {
            periodoSeleccionado = lovPeriodosFiltrar.get(0);
            aceptar = false;
            RequestContext.getCurrentInstance().execute("PF('LOVPeriodos').unselectAllRows();PF('LOVPeriodos').selectRow(0);");
         } else {
            periodoSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('LOVPeriodos').unselectAllRows();");
         }
      } else {
         periodoSeleccionado = null;
         aceptar = true;
      }
      infoRegistroPeriodo = String.valueOf(tabla.getRowCount());
      return infoRegistroPeriodo;
   }

   public void setInfoRegistroPeriodo(String infoRegistroPeriodo) {
      this.infoRegistroPeriodo = infoRegistroPeriodo;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

   public String getAltoTablaAux() {
      return altoTablaAux;
   }

   public void setAltoTablaAux(String altoTablaAux) {
      this.altoTablaAux = altoTablaAux;
   }

}
