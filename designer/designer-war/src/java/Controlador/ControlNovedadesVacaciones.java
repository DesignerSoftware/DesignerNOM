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
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import InterfaceAdministrar.AdministrarEmplVigenciasFormasPagosInterface;
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

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlNovedadesVacaciones implements Serializable {

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
   private List<Empleados> listaValEmpleados;
   private List<Empleados> filtradoslistaValEmpleados;
   private Empleados empleadoSeleccionadoLOV;
   //LOV PERIODOS
   private List<Vacaciones> listaPeriodos;
   private List<Vacaciones> filtradoslistaPeriodos;
   private Vacaciones periodoSeleccionado;
   //Columnas Tabla NOVEDADES
   private Column nEFechasIngreso, nEEmpleadosNombres;
   private Column nEFechaInicialDisfrute, nEPeriodo, nEDias, nEFechaSiguiente,
           nESubTipo, nEAdelantoHasta, nEFechaPago, nEDiasAplazados;
   private Date suma364;
   private Date finalsuma364;
   private BigInteger diasTotales;
   private short diasAplazadosTotal;
   private final String cero;
   //ALTO SCROLL TABLA
   private String altoTablaEmp, altoTablaRegEmp;// altoTablaEmp, altoTablaRegEmp;
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

   public ControlNovedadesVacaciones() {
      cero = "0";
      listaEmpleadosNovedad = null;
      listaNovedadesBorrar = new ArrayList<NovedadesSistema>();
      listaNovedadesCrear = new ArrayList<NovedadesSistema>();
      listaNovedadesModificar = new ArrayList<NovedadesSistema>();
      aceptar = true;
      guardado = true;
      tipoLista = 0;
      tipoListaNov = 0;
      listaValEmpleados = null;
      permitirIndex = true;
      nuevaNovedad = new NovedadesSistema();
      nuevaNovedad.setSubtipo("TIEMPO");
      nuevaNovedad.setTipo("VACACION");
      nuevaNovedad.setVacacion(new Vacaciones());
      nuevaNovedad.setVacadiasaplazados(Short.valueOf(cero));
      nuevaNovedad.setDias(BigInteger.valueOf(0));
      duplicarNovedad = new NovedadesSistema();
      duplicarNovedad.setSubtipo("TIEMPO");
      duplicarNovedad.setTipo("VACACION");
      duplicarNovedad.setVacacion(new Vacaciones());
      duplicarNovedad.setVacadiasaplazados(Short.valueOf(cero));
      duplicarNovedad.setDias(BigInteger.valueOf(0));
      diasTotales = BigInteger.valueOf(0);
      diasAplazadosTotal = Short.parseShort(cero);
      altoTablaEmp = "110";
      paginaAnterior = "nominaf";
      activarMTodos = true;
      novedadSeleccionada = null;
      periodicidadCodigoDos = BigInteger.valueOf(19847);
      activarLOV = true;
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
      String pagActual = "novedadvacaciones";
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

   public void limpiarListasValor() {

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
                     diasTotales = diasTotales.add(listaNovedades.get(i).getDias());
                     diasAplazadosTotal = (short) (diasAplazadosTotal + listaNovedades.get(i).getVacadiasaplazados());
                  }
               }
            }
         }
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPag(String pag) {
      paginaAnterior = pag;
   }

   public String volverPagAnterior() {
      return paginaAnterior;
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
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
      nuevaNovedad.setVacadiasaplazados(Short.valueOf(cero));
      nuevaNovedad.setDias(BigInteger.valueOf(0));
   }

   public void limpiarduplicarNovedades() {
      String cero = "0";
      duplicarNovedad = new NovedadesSistema();
      duplicarNovedad.setSubtipo("TIEMPO");
      duplicarNovedad.setTipo("VACACION");
      duplicarNovedad.setVacacion(new Vacaciones());
      duplicarNovedad.setVacadiasaplazados(Short.valueOf(cero));
      duplicarNovedad.setDias(BigInteger.valueOf(0));
   }

   //CREAR NOVEDADES
   public void agregarNuevaNovedadVacaciones() {
      int pasa = 0;
//        Empleados emp = new Empleados();
      mensajeValidacion = new String();
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevaNovedad.getFechainicialdisfrute() == null) {
         System.out.println("Entro a Fecha Inicial Disfrute");
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial Disfrute\n";
         pasa++;
      }

      if (nuevaNovedad.getDias() == null) {
         System.out.println("Entro a Dias");
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
         System.out.println("fecha inicial disfrute" + nuevaNovedad.getFechainicialdisfrute());
         System.out.println("periodo " + nuevaNovedad.getVacacion().getPeriodo());
         System.out.println("dias" + nuevaNovedad.getDias());
         System.out.println("fecha sigueinte vacacion " + nuevaNovedad.getFechasiguientefinvaca());
         System.out.println("subtipo " + nuevaNovedad.getSubtipo());
         System.out.println("adelanto hasta  :" + nuevaNovedad.getAdelantapagohasta());
         System.out.println("fecha pago : " + nuevaNovedad.getFechapago());
         System.out.println("dias aplazados" + nuevaNovedad.getVacadiasaplazados());

         listaNovedadesCrear.add(nuevaNovedad);
         if (listaNovedades == null) {
            listaNovedades = new ArrayList<NovedadesSistema>();
         }
         listaNovedades.add(nuevaNovedad);
         System.out.println("periodo : " + nuevaNovedad.getVacacion().getPeriodo());
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
      nuevaNovedad.setVacadiasaplazados(Short.valueOf(cero));
      nuevaNovedad.setDias(BigInteger.valueOf(0));
   }

   public void duplicarNV() {
      if (novedadSeleccionada != null) {
         duplicarNovedad = new NovedadesSistema();
         paraNuevaNovedad++;
         nuevaNovedadSec = BigInteger.valueOf(paraNuevaNovedad);
         Empleados emple = new Empleados();
         cargarLovEmpleados();
         for (int i = 0; i < listaValEmpleados.size(); i++) {
            if (empleadoSeleccionado.getSecuencia().compareTo(listaValEmpleados.get(i).getSecuencia()) == 0) {
               emple = listaValEmpleados.get(i);
            }
         }
         duplicarNovedad.setSecuencia(nuevaNovedadSec);
         duplicarNovedad.setEmpleado(emple);
         duplicarNovedad.setVacacion(nuevaNovedad.getVacacion());
         duplicarNovedad.setFechainicialdisfrute(novedadSeleccionada.getFechainicialdisfrute());
         duplicarNovedad.getVacacion().setPeriodo(novedadSeleccionada.getVacacion().getPeriodo());
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
         System.out.println("Entro a Fecha Inicial");
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (duplicarNovedad.getEmpleado() == null) {
         System.out.println("Entro a Empleado");
         mensajeValidacion = mensajeValidacion + " * Empleado\n";
         pasa++;
      }
      if (duplicarNovedad.getDias() == null) {
         System.out.println("Entro a Dias");
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
      if (listaValEmpleados != null) {
         for (int i = 0; i < listaValEmpleados.size(); i++) {
            listaEmpleadosNovedad.add(listaValEmpleados.get(i));
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
      System.out.println("entra a guardar");
      Empleados emp = new Empleados();
      if (guardado == false) {
         System.out.println("Realizando Operaciones Novedades");

         if (!listaNovedadesBorrar.isEmpty()) {
            for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
               System.out.println("Borrando..." + listaNovedadesBorrar.size());

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
               if (listaNovedadesBorrar.get(i).getVacadiasaplazados() == null) {
                  listaNovedadesBorrar.get(i).setVacadiasaplazados(null);
               }
               administrarNovedadesSistema.borrarNovedades(listaNovedadesBorrar.get(i));
            }
            System.out.println("Entra");
            listaNovedadesBorrar.clear();
         }

         if (!listaNovedadesCrear.isEmpty()) {
            for (int i = 0; i < listaNovedadesCrear.size(); i++) {
               System.out.println("Creando...");

               System.out.println("1");
               if (listaNovedadesCrear.get(i).getVacacion() == null) {
                  listaNovedadesCrear.get(i).setVacacion(new Vacaciones());
               }
               System.out.println("2");

               if (listaNovedadesCrear.get(i).getFechasiguientefinvaca() == null) {
                  listaNovedadesCrear.get(i).setFechasiguientefinvaca(null);
               }
               System.out.println("3");
               if (listaNovedadesCrear.get(i).getFechapago() == null) {
                  listaNovedadesCrear.get(i).setFechapago(null);
               }
               System.out.println("4");
               if (listaNovedadesCrear.get(i).getAdelantapagohasta() == null) {
                  listaNovedadesCrear.get(i).setAdelantapagohasta(null);
               }
               if (listaNovedadesCrear.get(i).getVacadiasaplazados() == null) {
                  listaNovedadesCrear.get(i).setVacadiasaplazados(null);
               }
               System.out.println("6");
               System.out.println(listaNovedadesCrear.get(i).getTipo());
               System.out.println("7");
               System.out.println("7 y medio : " + listaNovedadesCrear.get(i));

               administrarNovedadesSistema.crearNovedades(listaNovedadesCrear.get(i));
               System.out.println("8");
            }
            System.out.println("LimpiaLista");
            listaNovedadesCrear.clear();
         }
         System.out.println("Se guardaron los datos con exito");
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
      filtradoslistaValEmpleados = null;
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
      diasAplazadosTotal = Short.parseShort(cero);
      altoTablaEmp = "110";
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      listaNovedades = null;
      listaValEmpleados = null;
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
      listaValEmpleados = null;
      permitirIndex = true;
      limpiarNuevaNovedad();
      limpiarduplicarNovedades();
      diasTotales = BigInteger.valueOf(0);
      diasAplazadosTotal = Short.parseShort(cero);
      altoTablaEmp = "110";
      listaNovedadesBorrar.clear();
      listaNovedadesCrear.clear();
      listaNovedadesModificar.clear();
      novedadSeleccionada = null;
      listaNovedades = null;
      activarMTodos = true;
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
         System.out.println("actualizarPeriodos fecha adelanto hasta" + nuevaNovedad.getAdelantapagohasta());
         RequestContext.getCurrentInstance().execute("PF('proceso').show()");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoAdelantoHasta");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaPago");

      } else if (tipoActualizacion == 2) {
         System.out.println("entró a actualizar periodo 3");
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
      filtradoslistaPeriodos = null;
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
      filtradoslistaPeriodos = null;
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
      System.out.println("fecha adelanto h : " + fechaAdelantoH);
      if (tipo == 1) {
         nuevaNovedad.setAdelantapagohasta(fechaAdelantoH);
      } else if (tipo == 2) {
         duplicarNovedad.setAdelantapagohasta(fechaAdelantoH);
      }
      fechaslimite(fechaRegreso);
      return fechaAdelantoH;
   }

   public void fechaslimite(Date fecharegreso) {
      System.out.println("fechas limite fecha regreso : " + fecharegreso);
      Date fechalimite = administrarNovedadesVacaciones.anteriorFechaLimite(fecharegreso, periodicidadCodigoDos);
      Date siguientelimite = administrarNovedadesVacaciones.despuesFechaLimite(fecharegreso, periodicidadCodigoDos);
      System.out.println("fecha limite : " + fechalimite);
      System.out.println("fecha siguiente limite : " + siguientelimite);
   }

   public Date fechaSiguiente(int tipo) {
      System.out.println("entró a fechaSiguiente");
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
      System.out.println("fecha Siguiente : " + fechaSiguiente);
      return fechaSiguiente;
   }

   public void validacionProcesoVacaciones(String vacdifnomina) {
      Date siguientefechalimite;
      Date siguientefechalimite2;
      Date fechaUltimoCorteNomina = administrarNovedadesVacaciones.fechaUltimoCorte(secuenciaEmpleado, 1);
      Date fechaUltimoCorteUnidades = administrarNovedadesVacaciones.fechaUltimoCorte(secuenciaEmpleado, 80);
      fechaUltimoCorte = administrarNovedadesVacaciones.fechaUltimoCorte(secuenciaEmpleado, 1);
      System.out.println("fechaUltimoCorteNomina : " + fechaUltimoCorteNomina);
      System.out.println("fechaUltimoCorteUnidades : " + fechaUltimoCorteUnidades);

      if (fechaUltimoCorteNomina != null && fechaUltimoCorteUnidades != null) {
         System.out.println("compare : " + fechaUltimoCorteNomina.compareTo(fechaUltimoCorteUnidades));
         if (fechaUltimoCorteNomina.compareTo(fechaUltimoCorteUnidades) > 0) {
            fechaUltimoCorte = fechaUltimoCorteNomina;
         } else {
            fechaUltimoCorte = fechaUltimoCorteUnidades;
         }
      } else {
         fechaUltimoCorte = fechaUltimoCorteNomina;
      }

      System.out.println("fechaUltimoCorte : " + fechaUltimoCorte);
      Calendar media = Calendar.getInstance();
      media.setTime(fechaUltimoCorte);
      media.add(Calendar.DAY_OF_MONTH, 1);
      Date aux = media.getTime();
      System.out.println("aux :" + aux);

      siguientefechalimite = administrarNovedadesVacaciones.despuesFechaLimite(aux, periodicidadCodigoDos);

      Calendar pnomina = Calendar.getInstance();
      pnomina.setTime(siguientefechalimite);
      pnomina.add(Calendar.DAY_OF_MONTH, 1);
      Date aux2 = pnomina.getTime();
      System.out.println("aux2 :" + aux2);

      siguientefechalimite2 = administrarNovedadesVacaciones.despuesFechaLimite(aux2, periodicidadCodigoDos);

      System.out.println("siguientefechalimite" + siguientefechalimite);
      System.out.println("siguientefechalimite2" + siguientefechalimite2);

      if (vacdifnomina.equals("S")) {
         nuevaNovedad.setFechapago(siguientefechalimite);
         System.out.println("validacionProcesoVacaciones igual S fecha pago nueva novedad : " + nuevaNovedad.getFechapago());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaPago");
      } else if (vacdifnomina.equals("N")) {
         System.out.println("secuenciaEmpleado en validacionProcesoVacaciones : " + secuenciaEmpleado);
         BigDecimal periodicidadaux = administrarVigFormasPagos.consultarPeriodicidadPorEmpl(secuenciaEmpleado);
         System.out.println("periodicidad aux " + periodicidadaux);
         if (periodicidadaux.toBigInteger().equals(periodicidadCodigoDos)) {
            nuevaNovedad.setFechapago(siguientefechalimite);
         } else if (periodicidadaux.toBigInteger().equals(BigInteger.valueOf(19845))) {
            nuevaNovedad.setFechapago(siguientefechalimite2);
         }
         System.out.println("validacionProcesoVacaciones igual N fecha pago nueva novedad : " + nuevaNovedad.getFechapago());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaPago");
      }
   }

   public void validaciondias() {
      periodicidadEmpleado = administrarNovedadesVacaciones.periodicidadEmpleado(secuenciaEmpleado);
      System.out.println("periodicidad empleado : " + periodicidadEmpleado);
   }

   public void agregarPeriodo(BigInteger secuenciaEmpleado) {
      System.out.println("agregar Periodo para empleado : " + secuenciaEmpleado);
      administrarNovedadesVacaciones.adelantarPeriodo(secuenciaEmpleado);
      listaPeriodos = administrarNovedadesSistema.periodosEmpleado(secuenciaEmpleado);
      getListaPeriodos();
      System.out.println("lista periodos en agregar periodo = " + listaPeriodos);
      RequestContext.getCurrentInstance().update("formLovPeriodo:periodosDialogo");
      RequestContext.getCurrentInstance().execute("PF('periodosDialogo').show()");
   }

   public void lovPeriodo(BigInteger secuenciaEmpleado, int tipoAct) {
      System.out.println("entró a lov periodo");
      System.out.println("lov periodo : " + secuenciaEmpleado);
      System.out.println("dias empleado : " + nuevaNovedad.getDias());
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = tipoAct;
      listaPeriodos = null;
      cargarLovPeriodos();
      if (nuevaNovedad.getDias() == null || nuevaNovedad.getDias() == BigInteger.ZERO) {
         System.out.println("Entró");
         System.out.println("empleado seleccionado = " + secuenciaEmpleado);
         if (listaPeriodos.isEmpty()) {
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
      System.out.println(" entró a Dias Mayor");
      BigInteger backup = dias;
      System.out.println("días solicitados : " + dias);

      if (tipoact == 1) {
         if (nuevaNovedad.getDias().compareTo(BigInteger.valueOf(15)) == 1) { //// si el resultado es uno e sporque nueva novedad . get dias es mayor a 15
            RequestContext.getCurrentInstance().update("formularioDialogos:diasMayor");
            nuevaNovedad.setDias(BigInteger.ZERO);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
            RequestContext.getCurrentInstance().execute("PF('diasMayor').show()");
         } else {
            System.out.println("entró al else");
            nuevaNovedad.setDias(dias);
            fechaSiguiente(1);
            fechaAdelantoHasta(nuevaNovedad.getFechasiguientefinvaca(), 1);
            System.out.println("actualizarPeriodos fecha adelanto hasta" + nuevaNovedad.getAdelantapagohasta());
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
            System.out.println("entró al else");
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
      System.out.println("tipo jornada : " + jornada);

      if (administrarNovedadesVacaciones.validarFestivoVacaciones(nuevaNovedad.getFechainicialdisfrute(), jornada)) {
         RequestContext.getCurrentInstance().execute("PF('diaFestivo').show()");
      }
//
      int diaSemana1;
      String diaSemanaStr = "";
      GregorianCalendar c = new GregorianCalendar();
      c.setTime(nuevaNovedad.getFechainicialdisfrute());
      diaSemana1 = c.get(Calendar.DAY_OF_WEEK);
      System.out.println("dia de la semana gregoriano  : " + diaSemana1);

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
      System.out.println("diaSemanaStr al salir del switch : " + diaSemanaStr);

      if (administrarNovedadesVacaciones.validarDiaLaboralVacaciones(jornada, diaSemanaStr) == true) {
         RequestContext.getCurrentInstance().execute("PF('diaNoLaboral').show()");
      }
//        System.out.println("aux : " + aux);
//        if (aux == true) {
//        }
//        if (administrarNovedadesVacaciones.validarDiaLaboralVacaciones(jornada, diaSemanaStr)) {
//            System.out.println("diaSemanaStr : " + diaSemanaStr);
//        }
   }

   public void seleccionarSubtipo(String subtipo, int tipo) {
      if (subtipo.equalsIgnoreCase("DINERO")) {
         if (tipo == 1) {
            Date vacDinero;
            Calendar tipoD = Calendar.getInstance();
            tipoD.setTime(nuevaNovedad.getFechainicialdisfrute());
            tipoD.add(Calendar.DAY_OF_MONTH, 1);
            vacDinero = tipoD.getTime();
            System.out.println("vacDinero :" + vacDinero);
            nuevaNovedad.setFechasiguientefinvaca(vacDinero);
            Date vacD;
            Calendar aux = Calendar.getInstance();
            aux.setTime(nuevaNovedad.getFechasiguientefinvaca());
            aux.add(Calendar.DAY_OF_MONTH, -1);
            vacD = aux.getTime();
            System.out.println("vacD : " + vacD);
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
            System.out.println("vacDineroD :" + vacDineroD);
            duplicarNovedad.setFechasiguientefinvaca(vacDineroD);
            Date vacDD;
            Calendar aux = Calendar.getInstance();
            aux.setTime(nuevaNovedad.getFechasiguientefinvaca());
            aux.add(Calendar.DAY_OF_MONTH, -1);
            vacDD = aux.getTime();
            System.out.println("vacD : " + vacDD);
            duplicarNovedad.setAdelantapagohasta(vacDD);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFechaSiguiente");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAdelantoHasta");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFechaPago");
         }
      }
   }

   public void cargarLovEmpleados() {
      if (listaValEmpleados == null) {
         listaValEmpleados = administrarNovedadesVacaciones.empleadosVacaciones();
      }
   }

   public void cargarLovPeriodos() {
      if (listaPeriodos == null) {
         listaPeriodos = administrarNovedadesSistema.periodosEmpleado(secuenciaEmpleado);
      }
   }

//GETTER & SETTER
   public List<Empleados> getListaEmpleadosNovedad() {
      if (listaEmpleadosNovedad == null) {
         listaEmpleadosNovedad = administrarNovedadesVacaciones.empleadosVacaciones();
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

   public List<Empleados> getListaValEmpleados() {
      return listaValEmpleados;
   }

   public void setListaValEmpleados(List<Empleados> listaEmpleados) {
      this.listaValEmpleados = listaEmpleados;
   }

   public List<Empleados> getFiltradoslistaValEmpleados() {
      return filtradoslistaValEmpleados;
   }

   public void setFiltradoslistaValEmpleados(List<Empleados> filtradoslistaValEmpleados) {
      this.filtradoslistaValEmpleados = filtradoslistaValEmpleados;
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

   public List<Vacaciones> getListaPeriodos() {
      return listaPeriodos;
   }

   public void setListaPeriodos(List<Vacaciones> listaPeriodos) {
      this.listaPeriodos = listaPeriodos;
   }

   public List<Vacaciones> getFiltradoslistaPeriodos() {
      return filtradoslistaPeriodos;
   }

   public void setFiltradoslistaPeriodos(List<Vacaciones> filtradoslistaPeriodos) {
      this.filtradoslistaPeriodos = filtradoslistaPeriodos;
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
      diasAplazadosTotal = 0;
      if (listaNovedades != null) {
         if (!listaNovedades.isEmpty()) {
            for (int i = 0; i < listaNovedades.size(); i++) {
               diasTotales = diasTotales.add(listaNovedades.get(i).getDias());
               diasAplazadosTotal = (short) (diasAplazadosTotal + listaNovedades.get(i).getVacadiasaplazados());
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

   public short getDiasAplazadosTotal() {
      return diasAplazadosTotal;
   }

   public void setDiasAplazadosTotal(short diasAplazadosTotal) {
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
      if (altoTablaEmp.equals("92")) {
         altoTablaRegEmp = "5";
      } else {
         altoTablaRegEmp = "6";
      }
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
      if (filtradoslistaValEmpleados != null) {
         if (filtradoslistaValEmpleados.size() == 1) {
            empleadoSeleccionadoLOV = filtradoslistaValEmpleados.get(0);
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
      if (filtradoslistaPeriodos != null) {
         if (filtradoslistaPeriodos.size() == 1) {
            periodoSeleccionado = filtradoslistaPeriodos.get(0);
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

}
