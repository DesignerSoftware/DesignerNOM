package Controlador;

import Administrar.AdministrarCarpetaPersonal;
import Banner.BannerInicioRed;
import ControlNavegacion.ControlListaNavegacion;
import Entidades.*;
import InterfaceAdministrar.*;
import javax.ejb.EJB;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.Tab;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class ControlRemoto implements Serializable {

   @EJB
   AdministrarCarpetaPersonalInterface administrarCarpetaPersonal;
   @EJB
   AdministrarCarpetaDesignerInterface administrarCarpetaDesigner;
   @EJB
   AdministrarPermisosObjetosJsfInterface administrarPermisosObjetosJsf;
   private Empleados empleado;
   private Personas persona;
   private DetallesEmpresas detallesEmpresas;
   private VWActualesCargos vwActualesCargos;
   private Usuarios usuarios;
   private ParametrosEstructuras parametrosEstructuras;
   private VWActualesTiposContratos vwActualesTiposContratos;
   private VWActualesNormasEmpleados vwActualesNormasEmpleados;
   private VWActualesAfiliacionesSalud vwActualesAfiliacionesSalud;
   private VWActualesAfiliacionesPension vwActualesAfiliacionesPension;
   private VWActualesLocalizaciones vwActualesLocalizaciones;
   private VWActualesTiposTrabajadores vwActualesTiposTrabajadores;
//   private VWActualesTiposTrabajadores trabajador;
   private VWActualesContratos vwActualesContratos;
   private VWActualesJornadas vwActualesJornadas;
   private VWActualesReformasLaborales vwActualesReformasLaborales;
   private VWActualesUbicaciones vwActualesUbicaciones;
   private VWActualesFormasPagos vwActualesFormasPagos;
   private VWActualesVigenciasViajeros vwActualesVigenciasViajeros;
   private String estadoVacaciones, actualIBC, actualSet, actualComprobante;
   private BigDecimal actualMVR;
   private VWActualesTiposTrabajadores vwActualesTiposTrabajadoresPosicion;
   private VWActualesTiposTrabajadores backup;
   private List<VwTiposEmpleados> lovBusquedaRapida;
   private List<VwTiposEmpleados> filterBusquedaRapida;
   private List<VwTiposEmpleados> filterBuscarEmpleado;
   private List<VwTiposEmpleados> lovBuscarEmplTipo;
   private List<VigenciasCargos> vigenciasCargosEmpleados;
   private VigenciasCargos vigenciaSeleccionada;
   private String fechaActualesTiposContratos;
   private String FechaDesde, FechaHasta, FechaSistema;
   private String Sueldo;
   private String tipo, tipoBk;
   private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
   private final Locale locale = new Locale("es", "CO");
   private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
   private String Mensaje;
   private String Imagen;
   private String fotoEmpleado;
   private BigInteger secuencia, identificacion;
   private boolean acumulado, novedad, evaluacion, activo, pensionado, aspirante, hv1, hv2, bandera;
   private VwTiposEmpleados emplSeleccionado;
   private VwTiposEmpleados emplSeleccionadoBE;
   private boolean buscar, buscarEmp, mostrarT, mostrarT2;
   private UploadedFile file;
   private String nombreTabla;
   private Integer estadoEmpleado;
   //datos tablas ctrl+f11
   private Column tablasNombre, tablasDescripcion, moduloCodigo, moduloNombre, moduloObs;
   private boolean filtrosActivos;
   private List<Modulos> listModulos;
   private List<Modulos> filtradolistModulos;
   private List<Tablas> listaTablas;
   private List<Tablas> filterListTablas;
   private Modulos moduloSeleccionado;
   private Tablas tablaSeleccionada;
   private Pantallas pantalla;
   private String tablaExportar, nombreArchivo;
//   private BigInteger secuenciaMod;
   //PESTAÑA ACTUAL
   private int numPestanha;
   //SELECT ONE RADIO
   private String mensajePagos, tituloPago;
   private String pago;
   //ALTO TABLAS (PESTAÑA DESIGNER)
   private String altoModulos, altoTablas;
   //Buscar Tablas LOV
   private List<Tablas> lovTablas, lovTodasTablas;
   private List<Tablas> filtradoListTablasLOV;
   private Tablas tablaSeleccionadaLOV;
   private boolean buscarTablasLOV;
   //Visualizar seleccion de tipos trabajadores (StyleClass)
   private String styleActivos, stylePensionados, styleRetirados, styleAspirantes;
   private String actualCargo;
   private String tipoPersonal;
   private String infoRegistroBuscarEmpleados;
   private String infoRegistroBuscarTablas;
   private String infoRegistroBuscarTablasT;
   private String infoRegistroBusquedaRapida;
   private String infoRegistroTablas;
   private String infoRegistroMod;
   private int posicion;
   private int totalRegistros;
   private String informacionTiposTrabajadores;
   private final String extension;
   private List<BannerInicioRed> banner;
   //Seleccion de empresa para ingreso de personal
   private List<Empresas> filtradoLOVEmpresas;
   private List<Empresas> lovEmpresas;
   private Empresas empresaSeleccionada;
   private String infoRegistroEmpresas;
   private boolean activarAceptarEmpresas;
   private Empresas unicaEmpresa;

   private boolean resultadoBusquedaAv = false;
   private List<VWActualesTiposTrabajadores> listaBusquedaAvanzada;
   private List<ObjetosJsf> ListObjetosJSF;
   private List<String> listObjetosSinEmpleados;
   private ControlListaNavegacion controlListaNavegacion;
   private String nombrepantalla, mensajeinterface;

   public ControlRemoto() {
      lovEmpresas = null;
      extension = ".png";
      tipoPersonal = "activos";
      tipo = "ACTIVO";
      vwActualesCargos = new VWActualesCargos();
      vwActualesTiposContratos = new VWActualesTiposContratos();
      vwActualesNormasEmpleados = new VWActualesNormasEmpleados();
      vwActualesAfiliacionesSalud = new VWActualesAfiliacionesSalud();
      vwActualesAfiliacionesPension = new VWActualesAfiliacionesPension();
      vwActualesLocalizaciones = new VWActualesLocalizaciones();
      vwActualesTiposTrabajadores = new VWActualesTiposTrabajadores();
      vwActualesContratos = new VWActualesContratos();
      vwActualesJornadas = new VWActualesJornadas();
      vwActualesReformasLaborales = new VWActualesReformasLaborales();
      vwActualesUbicaciones = new VWActualesUbicaciones();
      vwActualesFormasPagos = new VWActualesFormasPagos();
      vwActualesVigenciasViajeros = new VWActualesVigenciasViajeros();
      vwActualesTiposTrabajadoresPosicion = new VWActualesTiposTrabajadores();
      administrarCarpetaPersonal = new AdministrarCarpetaPersonal();
      lovBusquedaRapida = null;
      Imagen = "personal1" + extension;
      styleActivos = "ui-state-highlight";
      acumulado = false;
      novedad = false;
      evaluacion = false;
      activo = false;
      pensionado = true;
      aspirante = false;
      hv1 = true;
      hv2 = false;
      buscar = true;
      buscarEmp = false;
      mostrarT = true;
      mostrarT2 = true;
      bandera = false;
      //Carpeta Designer //
      listModulos = null;
      moduloSeleccionado = null;
      tablaExportar = "data1";
      nombreArchivo = "Modulos";
      //Inicializar pestanha en 0
      numPestanha = 0;
      pago = "AUTOMATICO";
      tituloPago = "PAGOS AUTOMATICOS";
      mensajePagos = "Realice liquidaciones automáticas quincenales, mensuales, entre otras, por estructuras o por tipo de empleado. Primero ingrese los parametros a liquidar, después genere la liquidación para luego poder observar los comprobantes de pago. Usted puede deshacer todas las liquidaciones que desee siempre y cuando no se hayan cerrado. Al cerrar una liquidación se generan acumulados, por eso es importante estar seguro que la liquidación es correcta antes de cerrarla.";
      altoModulos = "73";
      altoTablas = "202";
      buscarTablasLOV = true;
      filtrosActivos = false;
      posicion = 0;
      totalRegistros = -1;
      banner = new ArrayList<BannerInicioRed>();
      filtradoLOVEmpresas = new ArrayList<Empresas>();
      infoRegistroEmpresas = "0";
      activarAceptarEmpresas = true;
      unicaEmpresa = null;
      listaBusquedaAvanzada = new ArrayList<VWActualesTiposTrabajadores>();
      listaTablas = null;
      lovTodasTablas = null;
      mensajeinterface = " ";
      nombrepantalla = " ";
      if (lovTablas == null) {
         lovTablas = new ArrayList<Tablas>();
      }
      lovBuscarEmplTipo = new ArrayList<VwTiposEmpleados>();
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         controlListaNavegacion = (ControlListaNavegacion) x.getApplication().evaluateExpressionGet(x, "#{controlListaNavegacion}", ControlListaNavegacion.class);
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarCarpetaPersonal.obtenerConexion(ses.getId());
         administrarCarpetaDesigner.obtenerConexion(ses.getId());
         administrarPermisosObjetosJsf.obtenerConexion(ses.getId());
         posicion = 0;
         requerirTipoTrabajador(posicion);
         try {
            valorInputText();
         } catch (ParseException ex) {
            System.out.println("error en primerTipoTrabajador");
            System.out.println("ControlRemoto.inicializarAdministrador() 7 CATCH");
         }
         llenarListaDeshavilitados();
         lovEmpresas = administrarCarpetaPersonal.consultarEmpresas();
         totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
         actualizarInformacionTipoTrabajador();
         llenarBannerDefault();
         getListModulos();
         if (listModulos != null) {
            if (!listModulos.isEmpty()) {
               moduloSeleccionado = listModulos.get(0);
               listaTablas = administrarCarpetaDesigner.consultarTablas(moduloSeleccionado.getSecuencia());
            }
         }
         ListObjetosJSF = null;
         getListObjetosJSF();
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void datosIniciales(int pestana) {
      numPestanha = pestana;
   }

   public void valorInputText() throws ParseException {
      if (vwActualesTiposTrabajadoresPosicion != null) {
         if (vwActualesTiposTrabajadoresPosicion.getEmpleado() != null) {
            secuencia = vwActualesTiposTrabajadoresPosicion.getEmpleado().getSecuencia();
            identificacion = vwActualesTiposTrabajadoresPosicion.getEmpleado().getPersona().getNumerodocumento();
            try {
               vwActualesCargos = administrarCarpetaPersonal.consultarActualCargoEmpleado(secuencia);
               Date actualFechaHasta = administrarCarpetaPersonal.consultarActualesFechas();
               String actualARP = administrarCarpetaPersonal.consultarActualARP(vwActualesCargos.getEstructura().getSecuencia(), vwActualesCargos.getCargo().getSecuencia(), actualFechaHasta);
               actualCargo = "%ARP: " + actualARP + " > " + vwActualesCargos.getCargo().getNombre() + " - " + vwActualesCargos.getEstructura().getOrganigrama().getEmpresa().getNombre();
            } catch (Exception e) {
               actualCargo = null;
            }
            try {
               vwActualesTiposContratos = administrarCarpetaPersonal.consultarActualTipoContratoEmpleado(secuencia);
               if (vwActualesTiposContratos != null) {
                  fechaActualesTiposContratos = formato.format(vwActualesTiposContratos.getFechaVigencia());
               } else {
                  fechaActualesTiposContratos = null;
               }
            } catch (ParseException pe) {
               vwActualesTiposContratos = null;
               fechaActualesTiposContratos = null;
            }
            try {
               vwActualesNormasEmpleados = administrarCarpetaPersonal.consultarActualNormaLaboralEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesNormasEmpleados = null;
            }
            try {
               vwActualesAfiliacionesSalud = administrarCarpetaPersonal.consultarActualAfiliacionSaludEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesAfiliacionesSalud = null;
            }
            try {
               vwActualesAfiliacionesPension = administrarCarpetaPersonal.consultarActualAfiliacionPensionEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesAfiliacionesPension = null;
            }
            try {
               vwActualesLocalizaciones = administrarCarpetaPersonal.consultarActualLocalizacionEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesLocalizaciones = null;
            }
            try {
               vwActualesTiposTrabajadores = administrarCarpetaPersonal.consultarActualTipoTrabajadorEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesTiposTrabajadores = null;
            }
            try {
               vwActualesContratos = administrarCarpetaPersonal.consultarActualContratoEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesContratos = null;
            }
            try {
               vwActualesJornadas = administrarCarpetaPersonal.consultarActualJornadaEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesJornadas = null;
            }
            try {
               Sueldo = "TOTAL: " + nf.format(administrarCarpetaPersonal.consultarActualSueldoEmpleado(secuencia));
            } catch (Exception e) {
               Sueldo = null;
            }
            try {
               vwActualesReformasLaborales = administrarCarpetaPersonal.consultarActualReformaLaboralEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesReformasLaborales = null;
            }
            try {
               vwActualesUbicaciones = administrarCarpetaPersonal.consultarActualUbicacionEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesUbicaciones = null;
            }
            try {
               vwActualesFormasPagos = administrarCarpetaPersonal.consultarActualFormaPagoEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesFormasPagos = null;
            }
            try {
               vwActualesVigenciasViajeros = administrarCarpetaPersonal.consultarActualTipoViajeroEmpleado(secuencia);
            } catch (Exception e) {
               vwActualesVigenciasViajeros = null;
            }
            try {
               estadoVacaciones = administrarCarpetaPersonal.consultarActualEstadoVacaciones(secuencia);
            } catch (Exception e) {
               estadoVacaciones = null;
            }
            try {
               actualMVR = administrarCarpetaPersonal.consultarActualMVR(secuencia);
            } catch (Exception e) {
               actualMVR = null;
            }
            try {
               actualIBC = administrarCarpetaPersonal.actualIBC(secuencia, vwActualesTiposTrabajadoresPosicion.getEmpleado().getEmpresa().getRetencionysegsocxpersona());
            } catch (Exception e) {
               actualIBC = null;
            }
            try {
               actualSet = administrarCarpetaPersonal.consultarActualSet(secuencia);
            } catch (Exception e) {
               actualSet = null;
            }
            try {
               actualComprobante = administrarCarpetaPersonal.consultarActualComprobante(secuencia);
            } catch (Exception e) {
               actualComprobante = null;
            }
         }
      }
   }

   public void recibirBusquedaAvansada(List<BigInteger> listaSecEmpleados) {
      this.resultadoBusquedaAv = true;
      if (listaSecEmpleados != null) {
         if (!listaSecEmpleados.isEmpty()) {
            listaBusquedaAvanzada.clear();
            lovBuscarEmplTipo.clear();
            for (int i = 0; i < listaSecEmpleados.size(); i++) {
               try {
                  listaBusquedaAvanzada.add(administrarCarpetaPersonal.consultarActualTipoTrabajadorEmpleado(listaSecEmpleados.get(i)));
                  System.out.println("Controlador.ControlRemoto.recibirBusquedaAvansada()");
               } catch (Exception e) {
                  System.out.println("recibirBusquedaAvansada() Error consultando vwactualtipotrabajador Pos : " + i + ",  ERROR e : " + e);
               }
            }
            if (!listaBusquedaAvanzada.isEmpty()) {
               if (lovBusquedaRapida == null) {
                  lovBusquedaRapida = administrarCarpetaPersonal.consultarRapidaEmpleados();
               }
               for (VWActualesTiposTrabajadores recBAvanzada : listaBusquedaAvanzada) {
                  for (VwTiposEmpleados recBRapida : lovBusquedaRapida) {
                     if (recBAvanzada.getEmpleado().getCodigoempleado().toBigInteger().equals(recBRapida.getCodigoEmpleado())) {
                        lovBuscarEmplTipo.add(recBRapida);
                     }
                  }
               }
               System.out.println("ControlRemoto.recibirBusquedaAvansada() lovBuscarEmplTipo.size(): " + lovBuscarEmplTipo.size());
            }
            totalRegistros = listaBusquedaAvanzada.size();
            tipoPersonal = "activos";
            tipoBk = tipo;
            tipo = "ACTIVO";
            posicion = 0;
            vwActualesTiposTrabajadoresPosicion = listaBusquedaAvanzada.get(0);
            System.out.println("vwActualesTiposTrabajadoresPosicion : " + vwActualesTiposTrabajadoresPosicion);
            if (vwActualesTiposTrabajadoresPosicion == null) {
               vwActualesTiposTrabajadoresPosicion = backup;
               Mensaje = "Activo";
               RequestContext.getCurrentInstance().execute("PF('alerta').show()");
               tipo = tipoBk;
            } else {
               backup = null;
               Imagen = "personal1" + extension;
               styleActivos = "ui-state-highlight";
               stylePensionados = "";
               styleRetirados = "";
               styleAspirantes = "";
               buscarEmp = false;
               tipoBk = null;
               acumulado = false;
               novedad = false;
               evaluacion = false;
               activo = false;
               pensionado = true;
               aspirante = false;
               hv1 = true;
               hv2 = false;
               try {
                  valorInputText();
               } catch (ParseException ex) {
                  System.out.println(this.getClass().getName() + "activos() error ");
                  ex.printStackTrace();
               }
               mostrarT2 = false;
               mostrarT = true;
               System.out.println("Ya termino de entrar  :)");
               actualizarInformacionTipoTrabajador();
               controlListaNavegacion.quitarPagina("busquedaavanzada");
            }
         }
      }
   }

   public void activos() {
      resultadoBusquedaAv = false;
      tipoPersonal = "activos";
      backup = vwActualesTiposTrabajadoresPosicion;
      tipoBk = tipo;
      tipo = "ACTIVO";
      posicion = 0;
      requerirTipoTrabajador(posicion);
      totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
      if (vwActualesTiposTrabajadoresPosicion == null) {
         vwActualesTiposTrabajadoresPosicion = backup;
         Mensaje = "Activo";
         RequestContext.getCurrentInstance().execute("PF('alerta').show()");
         tipo = tipoBk;
      } else {
         backup = null;
         Imagen = "personal1" + extension;
         styleActivos = "ui-state-highlight";
         stylePensionados = "";
         styleRetirados = "";
         styleAspirantes = "";
         buscarEmp = false;
         tipoBk = null;
         acumulado = false;
         novedad = false;
         evaluacion = false;
         activo = false;
         pensionado = true;
         aspirante = false;
         hv1 = true;
         hv2 = false;
         mostrarT = true;
         mostrarT2 = true;
         try {
            valorInputText();
         } catch (ParseException ex) {
            System.out.println(this.getClass().getName() + "activos() error ");
            ex.printStackTrace();
         }
         lovBuscarEmplTipo.clear();
         actualizarInformacionTipoTrabajador();
         RequestContext.getCurrentInstance().update("form:tabmenu");
         RequestContext.getCurrentInstance().update("form:tabmenu:activos");
         RequestContext.getCurrentInstance().update("form:tabmenu:pensionados");
         RequestContext.getCurrentInstance().update("form:tabmenu:retirados");
         RequestContext.getCurrentInstance().update("form:tabmenu:aspirantes");
      }
   }

   public void pensionados() {
      resultadoBusquedaAv = false;
      tipoPersonal = "pensionados";
      backup = vwActualesTiposTrabajadoresPosicion;
      tipoBk = tipo;
      tipo = "PENSIONADO";
      posicion = 0;
      requerirTipoTrabajador(posicion);
      totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
      if (vwActualesTiposTrabajadoresPosicion == null) {
         vwActualesTiposTrabajadoresPosicion = backup;
         Mensaje = "Pensionado";
         RequestContext.getCurrentInstance().execute("PF('alerta').show()");
         tipo = tipoBk;
      } else {
         backup = null;
         Imagen = "personal2" + extension;
         stylePensionados = "ui-state-highlight";
         styleActivos = "";
         styleRetirados = "";
         styleAspirantes = "";
         buscarEmp = false;
         tipoBk = null;
         acumulado = false;
         novedad = false;
         evaluacion = false;
         activo = true;
         pensionado = false;
         aspirante = true;
         hv1 = true;
         hv2 = true;
         mostrarT = true;
         mostrarT2 = true;
         try {
            valorInputText();
         } catch (ParseException ex) {
            System.out.println(ControlRemoto.class.getName() + " error en la entrada");
         }
         lovBuscarEmplTipo.clear();
         actualizarInformacionTipoTrabajador();
         RequestContext.getCurrentInstance().update("form:tabmenu");
         RequestContext.getCurrentInstance().update("form:tabmenu:activos");
         RequestContext.getCurrentInstance().update("form:tabmenu:pensionados");
         RequestContext.getCurrentInstance().update("form:tabmenu:retirados");
         RequestContext.getCurrentInstance().update("form:tabmenu:aspirantes");
      }
   }

   public void retirados() {
      resultadoBusquedaAv = false;
      tipoPersonal = "retirados";
      backup = vwActualesTiposTrabajadoresPosicion;
      tipoBk = tipo;
      tipo = "RETIRADO";
      posicion = 0;
      requerirTipoTrabajador(posicion);
      totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
      if (vwActualesTiposTrabajadoresPosicion == null) {
         vwActualesTiposTrabajadoresPosicion = backup;
         Mensaje = "Retirado";
         RequestContext.getCurrentInstance().execute("PF('alerta').show()");
         tipo = tipoBk;
      } else {
         backup = null;
         Imagen = "personal3" + extension;
         styleRetirados = "ui-state-highlight";
         stylePensionados = "";
         styleActivos = "";
         styleAspirantes = "";
         buscarEmp = false;
         tipoBk = null;
         acumulado = false;
         novedad = false;
         evaluacion = true;
         activo = true;
         pensionado = true;
         aspirante = true;
         hv1 = true;
         hv2 = true;
         mostrarT = true;
         mostrarT2 = true;
         try {
            valorInputText();
         } catch (ParseException ex) {
            System.out.println(ControlRemoto.class.getName() + " error en la entrada");
         }
         lovBuscarEmplTipo.clear();
         actualizarInformacionTipoTrabajador();
         RequestContext.getCurrentInstance().update("form:tabmenu");
         RequestContext.getCurrentInstance().update("form:tabmenu:activos");
         RequestContext.getCurrentInstance().update("form:tabmenu:pensionados");
         RequestContext.getCurrentInstance().update("form:tabmenu:retirados");
         RequestContext.getCurrentInstance().update("form:tabmenu:aspirantes");
      }
   }

   public void aspirantes() {
      resultadoBusquedaAv = false;
      tipoPersonal = "aspirantes";
      backup = vwActualesTiposTrabajadoresPosicion;
      tipoBk = tipo;
      tipo = "DISPONIBLE";
      posicion = 0;
      requerirTipoTrabajador(posicion);
      totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
      if (vwActualesTiposTrabajadoresPosicion == null) {
         vwActualesTiposTrabajadoresPosicion = backup;
         Mensaje = "Aspirante";
         RequestContext.getCurrentInstance().execute("PF('alerta').show()");
         tipo = tipoBk;
      } else {
         backup = null;
         Imagen = "personal4" + extension;
         styleAspirantes = "ui-state-highlight";
         stylePensionados = "";
         styleActivos = "";
         styleRetirados = "";
         buscarEmp = false;
         tipoBk = null;
         acumulado = true;
         novedad = true;
         evaluacion = false;
         activo = true;
         pensionado = true;
         aspirante = false;
         hv1 = false;
         hv2 = true;
         mostrarT = true;
         mostrarT2 = true;
         try {
            valorInputText();
         } catch (ParseException ex) {
            System.out.println(ControlRemoto.class.getName() + " error en la entrada");
         }
         lovBuscarEmplTipo.clear();
         actualizarInformacionTipoTrabajador();
         RequestContext.getCurrentInstance().update("form:tabmenu");
         RequestContext.getCurrentInstance().update("form:tabmenu:activos");
         RequestContext.getCurrentInstance().update("form:tabmenu:pensionados");
         RequestContext.getCurrentInstance().update("form:tabmenu:retirados");
         RequestContext.getCurrentInstance().update("form:tabmenu:aspirantes");
      }
   }

   public void ejecutarProcedimientoPaquete() {
      BigDecimal result = BigDecimal.valueOf(-1);
      BigDecimal x = BigDecimal.valueOf(0);
      if (tipoPersonal.equals("activos")) {
         result = administrarCarpetaPersonal.borrarActivo(secuencia);
         System.out.println("result : " + result);
         if (result == null) {
            result = BigDecimal.valueOf(0);
         }
         if (result.equals(x)) {
            RequestContext.getCurrentInstance().update("formulariodialogos:activoeliminarpaso1");
            RequestContext.getCurrentInstance().execute("PF('activoeliminarpaso1').show()");
         } else {
            RequestContext.getCurrentInstance().update("formulariodialogos:activonoeliminar");
            RequestContext.getCurrentInstance().execute("PF('activonoeliminar').show()");
         }
      } else if (tipoPersonal.equals("retirados")) {
         FacesContext fc = FacesContext.getCurrentInstance();
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, controlListaNavegacion.guardarNavegacion("nominaf", "reingresarempleado"));
      }
      lovBuscarEmplTipo.clear();
      lovBusquedaRapida = null;
   }

   public void paso2() {
      RequestContext.getCurrentInstance().update("formulariodialogos:activoeliminarpaso2");
      RequestContext.getCurrentInstance().execute("PF('activoeliminarpaso2').show()");
   }

   public void paso3() {
      RequestContext.getCurrentInstance().update("formulariodialogos:activoeliminarpaso3");
      RequestContext.getCurrentInstance().execute("PF('activoeliminarpaso3').show()");
   }

   public void paso4() {
      try {
         boolean b = administrarCarpetaPersonal.borrarEmpleadoActivo(vwActualesTiposTrabajadoresPosicion.getEmpleado().getSecuencia(), vwActualesTiposTrabajadoresPosicion.getEmpleado().getPersona().getSecuencia());
         if (b) {
            RequestContext.getCurrentInstance().update("formulariodialogos:activoeliminarpaso4");
            RequestContext.getCurrentInstance().execute("PF('activoeliminarpaso4').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorEmpleadoNoEliminado').show()");
         }

         if (resultadoBusquedaAv) {
            totalRegistros = listaBusquedaAvanzada.size();
            tipoPersonal = "activos";
            tipoBk = tipo;
            tipo = "ACTIVO";
            posicion = 0;
            listaBusquedaAvanzada.remove(vwActualesTiposTrabajadoresPosicion);
            requerirTipoTrabajador(posicion);
            if (vwActualesTiposTrabajadoresPosicion != null) {
               backup = null;
               Imagen = "personal1" + extension;
               styleActivos = "ui-state-highlight";
               stylePensionados = "";
               styleRetirados = "";
               styleAspirantes = "";
               buscarEmp = false;
               tipoBk = null;
               acumulado = false;
               novedad = false;
               evaluacion = false;
               activo = false;
               pensionado = true;
               aspirante = false;
               hv1 = true;
               hv2 = false;
               mostrarT = true;
               mostrarT2 = true;
               try {
                  valorInputText();
               } catch (ParseException ex) {
                  System.out.println(this.getClass().getName() + "activos() 'resultadoBusquedaAv' error ");
                  ex.printStackTrace();
               }
               lovBuscarEmplTipo.clear();
               actualizarInformacionTipoTrabajador();
               RequestContext.getCurrentInstance().update("form:tabmenu");
               RequestContext.getCurrentInstance().update("form:tabmenu:activos");
               RequestContext.getCurrentInstance().update("form:tabmenu:pensionados");
               RequestContext.getCurrentInstance().update("form:tabmenu:retirados");
               RequestContext.getCurrentInstance().update("form:tabmenu:aspirantes");
            }
         }
      } catch (Exception e) {
         System.out.println("Error en borrar al empleado");
      }
   }

   public void busquedaRapida() {
      System.out.println("Entro en busqueda Rapida()");
      filterBusquedaRapida = null;
      VWActualesTiposTrabajadores empleadoSeleccionado = administrarCarpetaPersonal.consultarActualTipoTrabajadorEmpleado(emplSeleccionado.getRfEmpleado());
      resultadoBusquedaAv = false;
      if (empleadoSeleccionado.getTipoTrabajador().getTipo().equalsIgnoreCase("activo")) {
         Imagen = "personal1" + extension;
         styleActivos = "ui-state-highlight";
         stylePensionados = "";
         styleRetirados = "";
         styleAspirantes = "";
         acumulado = false;
         novedad = false;
         evaluacion = false;
         activo = false;
         pensionado = true;
         aspirante = false;
         hv1 = true;
         hv2 = false;
         tipo = "ACTIVO";
         tipoPersonal = "activos";
      }
      if (empleadoSeleccionado.getTipoTrabajador().getTipo().equalsIgnoreCase("pensionado")) {
         Imagen = "personal2" + extension;
         stylePensionados = "ui-state-highlight";
         styleActivos = "";
         styleRetirados = "";
         styleAspirantes = "";
         acumulado = false;
         novedad = false;
         evaluacion = false;
         activo = true;
         pensionado = false;
         aspirante = true;
         hv1 = true;
         hv2 = true;
         tipo = "PENSIONADO";
         tipoPersonal = "pensionados";
      }
      if (empleadoSeleccionado.getTipoTrabajador().getTipo().equalsIgnoreCase("retirado")) {
         Imagen = "personal3" + extension;
         styleRetirados = "ui-state-highlight";
         stylePensionados = "";
         styleActivos = "";
         styleAspirantes = "";
         acumulado = false;
         novedad = false;
         evaluacion = true;
         activo = true;
         pensionado = true;
         aspirante = true;
         hv1 = true;
         hv2 = true;
         tipo = "RETIRADO";
         tipoPersonal = "retirados";
      }
      if (empleadoSeleccionado.getTipoTrabajador().getTipo().equalsIgnoreCase("disponible")) {
         Imagen = "personal4" + extension;
         styleAspirantes = "ui-state-highlight";
         styleRetirados = "";
         stylePensionados = "";
         styleActivos = "";
         acumulado = true;
         novedad = true;
         evaluacion = false;
         activo = true;
         pensionado = true;
         aspirante = false;
         hv1 = false;
         hv2 = true;
         tipo = "DISPONIBLE";
         tipoPersonal = "aspirantes";
      }
      RequestContext.getCurrentInstance().reset("form:formlovempleadosR:lvbusquedarapida:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lvbusquedarapida').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('lvbr').hide()");
      RequestContext.getCurrentInstance().update("form:formlovempleadosR:lvbusquedarapida");
      RequestContext.getCurrentInstance().update("form:formlovempleadosR:lvbr");
      RequestContext.getCurrentInstance().update("form:formlovempleadosR:buscarrap");

      vwActualesTiposTrabajadoresPosicion = empleadoSeleccionado;
      emplSeleccionado = null;
      buscar = true;
      buscarEmp = true;
      mostrarT = false;
      mostrarT2 = false;
      totalRegistros = 1;
      posicion = 0;
      RequestContext.getCurrentInstance().update("form:tabmenu:activos");
      RequestContext.getCurrentInstance().update("form:tabmenu:pensionados");
      RequestContext.getCurrentInstance().update("form:tabmenu:retirados");
      RequestContext.getCurrentInstance().update("form:tabmenu:aspirantes");
      RequestContext.getCurrentInstance().update("form:tabmenu");
      RequestContext.getCurrentInstance().update("form:tabmenu:tipopersonal");
      RequestContext.getCurrentInstance().update("form:tabmenu:panelinf");
      RequestContext.getCurrentInstance().update("form:tabmenu:mostrartodos");
      try {
         valorInputText();

      } catch (ParseException ex) {
         System.out.println(ControlRemoto.class.getName() + " error en la entrada");
      }
      actualizarInformacionTipoTrabajador();
      actualizarNavegacion();
   }

   public void busquedaRapidaAtras() {
      buscar = true;
      filterBusquedaRapida = null;
      emplSeleccionado = null;
      RequestContext.getCurrentInstance().reset("form:formlovempleadosR:lvbusquedarapida:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lvbusquedarapida').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('lvbr').hide()");
      RequestContext.getCurrentInstance().update("form:formlovempleadosR:lvbusquedarapida");
      RequestContext.getCurrentInstance().update("form:formlovempleadosR:lvbr");
      RequestContext.getCurrentInstance().update("form:formlovempleadosR:buscarrap");
   }

   public void activarBuscar() {
      buscar = false;
   }

   public void buscarEmpleadoDefault() {
      if (emplSeleccionadoBE != null) {
         buscarEmpleado();
      } else if (emplSeleccionado != null) {
         busquedaRapida();
      }
   }

   public void buscarEmpleado() {
      System.out.println("Entro en buscarEmpleado();");
      System.out.println("emplSeleccionadoBE : " + emplSeleccionadoBE);
      filterBuscarEmpleado = null;
      resultadoBusquedaAv = false;
      VWActualesTiposTrabajadores empleadoSeleccionado = administrarCarpetaPersonal.consultarActualTipoTrabajadorEmpleado(emplSeleccionadoBE.getRfEmpleado());
      tipo = empleadoSeleccionado.getTipoTrabajador().getTipo();
      vwActualesTiposTrabajadoresPosicion = empleadoSeleccionado;
      emplSeleccionadoBE = null;
      buscar = true;
      mostrarT = false;
      mostrarT2 = false;
      totalRegistros = 1;
      posicion = 0;

      RequestContext.getCurrentInstance().update("form:tabmenu");
      RequestContext.getCurrentInstance().update("form:tabmenu:mostrartodos");
      RequestContext.getCurrentInstance().reset("form:formlovempleados:lvbuscarempleado:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lvbuscarempleado').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('lvbe').hide()");
      RequestContext.getCurrentInstance().update("form:formlovempleados:lvbuscarempleado");
      RequestContext.getCurrentInstance().update("form:formlovempleados:lvbe");
      RequestContext.getCurrentInstance().update("form:formlovempleados:buscarempl");
      try {
         valorInputText();

      } catch (ParseException ex) {
         System.out.println(ControlRemoto.class.getName() + " error en la entrada");
      }
      actualizarInformacionTipoTrabajador();
      actualizarNavegacion();
   }

   public void busquedaEmpleadoAtras() {
      buscar = true;
      filterBuscarEmpleado = null;
      emplSeleccionadoBE = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:formlovempleados:lvbuscarempleado:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lvbuscarempleado').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('lvbe').hide()");
      RequestContext.getCurrentInstance().update("form:formlovempleados:lvbuscarempleado");
      RequestContext.getCurrentInstance().update("form:formlovempleados:lvbe");
      RequestContext.getCurrentInstance().update("form:formlovempleados:buscarempl");
   }

   public void mostrarTodos() {
      totalRegistros = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
      resultadoBusquedaAv = false;
      mostrarT = true;
      mostrarT2 = true;
      buscarEmp = false;
      posicion = 0;
      requerirTipoTrabajador(0);
      try {
         valorInputText();
      } catch (ParseException ex) {
         System.out.println(ControlRemoto.class.getName() + " error en la entrada");
      }
      actualizarInformacionTipoTrabajador();
      actualizarNavegacion();
   }

   public void probar(String nombreTab) {
      setNombreTabla(nombreTab);
   }

   public void lab() {
      if (tablaExportar.equals("tablas")) {
         tablasNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasnombre");
         tablasNombre.setFilterStyle("");
         tablasDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasdescripcion");
         tablasDescripcion.setFilterStyle("");
         altoTablas = "176";
         RequestContext.getCurrentInstance().update("form:tabmenu:tablas");
         filtrosActivos = true;
      } else if (tablaExportar.equals("data1")) {
         moduloCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:modulocodigo");
         moduloCodigo.setFilterStyle("width: 40px;");
         moduloNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:modulonombre");
         moduloNombre.setFilterStyle("");
         moduloObs = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:moduloobs");
         moduloObs.setFilterStyle("");
         altoModulos = "50";
         RequestContext.getCurrentInstance().update("form:tabmenu:data1");
         filtrosActivos = true;
      }
   }

   public void lab2() {
      tablasNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasnombre");
      tablasNombre.setFilterStyle("display: none; visibility: hidden;");
      tablasDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasdescripcion");
      tablasDescripcion.setFilterStyle("display: none; visibility: hidden;");
      altoTablas = "202";
      filtrosActivos = false;
      RequestContext.getCurrentInstance().update("form:tabmenu:tablas");
   }
/////////////////////////  
   private Integer progress;

   public Integer getProgress() {
      if (progress == null) {
         progress = 0;
      } else {
         progress = progress + 1;
         RequestContext.getCurrentInstance().update("formu:progreso");
         if (progress > 100) {
            progress = 100;
         }
      }
      return progress;
   }

   public void cancel() {
      progress = null;
   }

// Carpeta Designer //
   public List<Modulos> getListModulos() {
      if (listModulos == null) {
         listModulos = administrarCarpetaDesigner.consultarModulos();
      }
      return listModulos;
   }

   public void cambiarTablas() {
      //System.out.println("ControlRemoto.cambiarTablas() moduloSeleccionado : " + moduloSeleccionado);
      if (moduloSeleccionado != null) {
         listaTablas = administrarCarpetaDesigner.consultarTablas(moduloSeleccionado.getSecuencia());
         if (listaTablas != null) {
            if (!listaTablas.isEmpty()) {
               buscarTablasLOV = true;
               lovTablas.clear();
               lovTablas.addAll(listaTablas);
               RequestContext.getCurrentInstance().update("form:formlovtablas:buscartablasdialogo");
               RequestContext.getCurrentInstance().update("form:formlovtablas:lovtablas");
            }
         }
//         if (tablaExportar.equals("tablas")) {
//            tablasNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasnombre");
//            tablasNombre.setFilterStyle("display: none; visibility: hidden;");
//            tablasDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:tablas:tablasdescripcion");
//            tablasDescripcion.setFilterStyle("display: none; visibility: hidden;");
//            RequestContext.getCurrentInstance().execute("PF('tablas').clearFilters()");
//            altoTablas = "202";
//            RequestContext.getCurrentInstance().update("form:tabmenu:tablas");
//            filtrosActivos = false;
//         }
         RequestContext.getCurrentInstance().update("form:tabmenu:buscartablas");
         tablaExportar = "data1";
         nombreArchivo = "modulos";
         filterListTablas = null;
         RequestContext.getCurrentInstance().update("form:tabmenu:tablas");
         RequestContext.getCurrentInstance().update("form:tabmenu:infoRegistroTablas");
      }
   }

   public void exportarTabla() {
      if (tablaExportar.equals("data1")) {
         moduloCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:modulocodigo");
         moduloCodigo.setFilterStyle("display: none; visibility: hidden;");
         moduloNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:modulonombre");
         moduloNombre.setFilterStyle("display: none; visibility: hidden;");
         moduloObs = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tabmenu:data1:moduloobs");
         moduloObs.setFilterStyle("display: none; visibility: hidden;");
         filtradolistModulos = null;
         RequestContext.getCurrentInstance().execute("PF('data1').clearFilters()");
         altoModulos = "73";
         RequestContext.getCurrentInstance().update("form:tabmenu:data1");
         filtrosActivos = false;
      }
      tablaExportar = "tablas";
      nombreArchivo = "tablas";
   }

   public void redireccion(Tablas tabla) {
      tablaSeleccionada = tabla;
      if (tablaSeleccionada != null) {
         String pag = administrarCarpetaDesigner.consultarNombrePantalla(tablaSeleccionada.getSecuencia());
         System.out.println("ControlRemoto.redireccion() pag : " + pag);
         if (pag != null) {
            pag = pag.toLowerCase();
            FacesContext fc = FacesContext.getCurrentInstance();
            System.out.println("ControlRemoto.redireccion() pag : " + pag);
            pag = controlListaNavegacion.guardarNavegacion("nominaf", pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
         }
      }
      infoTablas(tablaSeleccionada);
   }

   public void infoTablas(Tablas tab) {
      tablaSeleccionada = tab;
      pantalla = administrarCarpetaDesigner.consultarPantalla(tablaSeleccionada.getSecuencia());
      tablaExportar = "tablas";
      RequestContext.getCurrentInstance().execute("PF('ventanatab').show()");
   }

   public void requerirBusquedaRapida() {
      if (lovBusquedaRapida == null) {
         filterBusquedaRapida = null;
         lovBusquedaRapida = administrarCarpetaPersonal.consultarRapidaEmpleados();
         contarRegistrosBR();
         RequestContext.getCurrentInstance().update("form:formlovempleadosR:lvbusquedarapida");
         RequestContext.getCurrentInstance().update("form:formlovempleadosR:lvbr");
      }
      RequestContext.getCurrentInstance().execute("PF('lvbr').show();");
   }

   public void requerirBuscarEmpleadoTipo() {
      if (lovBuscarEmplTipo == null) {
         lovBuscarEmplTipo = administrarCarpetaPersonal.consultarEmpleadosTipoTrabajador(tipo);
      } else if (lovBuscarEmplTipo.isEmpty()) {
         lovBuscarEmplTipo = administrarCarpetaPersonal.consultarEmpleadosTipoTrabajador(tipo);
      }
      filterBuscarEmpleado = null;
      RequestContext.getCurrentInstance().update("form:formlovempleados:lvbe");
      RequestContext.getCurrentInstance().update("form:formlovempleados:lvbuscarempleado");
      contarRegistrosBE();
      RequestContext.getCurrentInstance().execute("PF('lvbe').show()");
   }

   public void primerTipoTrabajador() {
      posicion = 0;
      System.out.println("Esta en primerTipoTrabajador()");
      System.out.println("resultadoBusquedaAv : " + resultadoBusquedaAv);
      requerirTipoTrabajador(posicion);
      try {
         valorInputText();
      } catch (ParseException ex) {
         System.out.println("error en primerTipoTrabajador");
      }
      actualizarInformacionTipoTrabajador();
      RequestContext.getCurrentInstance().update("form:tabmenu");
   }

   public void atrasTipoTrabajador() {
      if (posicion > 0) {
         posicion--;
         requerirTipoTrabajador(posicion);
         try {
            valorInputText();
         } catch (ParseException ex) {
            System.out.println(ControlRemoto.class.getName() + " error en la entrada.");
         }
         actualizarInformacionTipoTrabajador();
         RequestContext.getCurrentInstance().update("form:tabmenu");
      }
   }

   public void siguienteTipoTrabajador() {
      if (posicion < (totalRegistros - 1)) {
         posicion++;
         requerirTipoTrabajador(posicion);
         try {
            valorInputText();
         } catch (ParseException ex) {
            System.out.println(ControlRemoto.class.getName() + " error en la entrada");
         }
         actualizarInformacionTipoTrabajador();
         RequestContext.getCurrentInstance().update("form:tabmenu");
      }
   }

   public void ultimoTipoTrabajador() {
      posicion = totalRegistros - 1;
      requerirTipoTrabajador(posicion);
      try {
         valorInputText();

      } catch (ParseException ex) {
         System.out.println(ControlRemoto.class.getName() + " error en la entrada");
      }
      actualizarInformacionTipoTrabajador();
      RequestContext.getCurrentInstance().update("form:tabmenu");
   }

   public void validarEstadoNominaF() {
      int totalActual = administrarCarpetaPersonal.obtenerTotalRegistrosTipoTrabajador(tipo);
      if (totalActual != totalRegistros) {
         posicion = 0;
         totalRegistros = totalActual;
//         primerTipoTrabajador();
         lovBuscarEmplTipo.clear();
         lovBusquedaRapida = null;
         actualizarInformacionTipoTrabajador();
      }
   }

   public void actualizarInformacionTipoTrabajador() {
      if (totalRegistros == 0) {
         informacionTiposTrabajadores = "Reg. 0 de 0";
      } else {
         informacionTiposTrabajadores = "Reg. " + (posicion + 1) + " de " + totalRegistros;
      }
   }

   public void actualizarNavegacion() {
      RequestContext.getCurrentInstance().update("form:tabmenu:btnprimero");
      RequestContext.getCurrentInstance().update("form:tabmenu:btnatras");
      RequestContext.getCurrentInstance().update("form:tabmenu:btnsiguiente");
      RequestContext.getCurrentInstance().update("form:tabmenu:btnultimo");
   }

   public VWActualesTiposTrabajadores requerirTipoTrabajador(int posicion) {
      if (resultadoBusquedaAv) {
         vwActualesTiposTrabajadoresPosicion = listaBusquedaAvanzada.get(posicion);
      } else {
         vwActualesTiposTrabajadoresPosicion = administrarCarpetaPersonal.consultarEmpleadosTipoTrabajadorPosicion(tipo, posicion);
      }
      return vwActualesTiposTrabajadoresPosicion;
   }

   public void llenarBannerDefault() {
      banner.clear();
      banner.add(new BannerInicioRed("http://www.nomina.com.co/images/publicidadInn/pInn01.png", "www.nomina.com.co"));
      banner.add(new BannerInicioRed("http://www.nomina.com.co/images/publicidadInn/pInn02.png", "www.nomina.com.co"));
      banner.add(new BannerInicioRed("https://www.nomina.com.co/images/publicidadInn/pInn03.png", "www.nomina.com.co"));
      banner.add(new BannerInicioRed("https://www.nomina.com.co/images/publicidadInn/pInn04.png", "www.nomina.com.co"));
   }

   public void llenarListaDeshavilitados() {
      listObjetosSinEmpleados = new ArrayList<String>();
      listObjetosSinEmpleados.add("hojadevida");
      listObjetosSinEmpleados.add("txtcargodesempenhado");
      listObjetosSinEmpleados.add("txtfechacontratacion");
      listObjetosSinEmpleados.add("txtnormalaboral");
      listObjetosSinEmpleados.add("txtafiliaciones");
      listObjetosSinEmpleados.add("txtibc");
      listObjetosSinEmpleados.add("txttipoviajero");
      listObjetosSinEmpleados.add("txtcentrodecosto");
      listObjetosSinEmpleados.add("txttipotrabajador");
      listObjetosSinEmpleados.add("txtlegislacionlaboral");
      listObjetosSinEmpleados.add("txtjornada");
      listObjetosSinEmpleados.add("txtmenorvalorretencion");
      listObjetosSinEmpleados.add("txtvacaciones");
      listObjetosSinEmpleados.add("txtsueldo");
      listObjetosSinEmpleados.add("txttiposalario");
      listObjetosSinEmpleados.add("txtubicaciongeografica");
      listObjetosSinEmpleados.add("txtformadepago");
      listObjetosSinEmpleados.add("txtset");
      listObjetosSinEmpleados.add("txtcomprobantesdepago");
      listObjetosSinEmpleados.add("cargodesempenhado");
      listObjetosSinEmpleados.add("fechacontratacion");
      listObjetosSinEmpleados.add("normalaboral");
      listObjetosSinEmpleados.add("afiliaciones");
      listObjetosSinEmpleados.add("ibc");
      listObjetosSinEmpleados.add("tipoviajero");
      listObjetosSinEmpleados.add("centrodecosto");
      listObjetosSinEmpleados.add("tipotrabajador");
      listObjetosSinEmpleados.add("legislacionlaboral");
      listObjetosSinEmpleados.add("jornada");
      listObjetosSinEmpleados.add("menorvalorretencio");
      listObjetosSinEmpleados.add("vacaciones");
      listObjetosSinEmpleados.add("sueldo");
      listObjetosSinEmpleados.add("tiposalario");
      listObjetosSinEmpleados.add("ubicacion");
      listObjetosSinEmpleados.add("formadepago");
      listObjetosSinEmpleados.add("set");
      listObjetosSinEmpleados.add("comprobantes");
      listObjetosSinEmpleados.add("acumuladosnomina");
      listObjetosSinEmpleados.add("novedadesempleado");
      listObjetosSinEmpleados.add("evaluarpersona");
      listObjetosSinEmpleados.add("resultadosevaluaciones");
      listObjetosSinEmpleados.add("informacionadicional");
//      listObjetosSinEmpleados.add("reporteslaborales");
   }

   //   GET'S Y SET'S
   public List<Tablas> getListaTablas() {
//      if (moduloSeleccionado != null && listaTablas == null) {
//         listaTablas = administrarCarpetaDesigner.consultarTablas(moduloSeleccionado.getSecuencia());
//         if (listaTablas != null && !listaTablas.isEmpty()) {
//            buscarTablasLOV = false;
//         }
//      }
      return listaTablas;
   }

   public Modulos getModuloSeleccionado() {
      return moduloSeleccionado;
   }

   public void setModuloSeleccionado(Modulos moduloSeleccionado) {
      this.moduloSeleccionado = moduloSeleccionado;
   }

   public Tablas getTablaSeleccionada() {
      return tablaSeleccionada;
   }

   public void setTablaSeleccionada(Tablas tablaSeleccionada) {
      this.tablaSeleccionada = tablaSeleccionada;
   }

   public Pantallas getPantalla() {
      return pantalla;
   }

   public void setPantalla(Pantallas pantalla) {
      this.pantalla = pantalla;
   }

   public String getTablaExportar() {
      return tablaExportar;
   }

   public List<Tablas> getFilterListTablas() {
      return filterListTablas;
   }

   public void setFilterListTablas(List<Tablas> filterListTablas) {
      this.filterListTablas = filterListTablas;
   }

   public List<Modulos> getFiltradolistModulos() {
      return filtradolistModulos;
   }

   public void setFiltradolistModulos(List<Modulos> filtradolistModulos) {
      this.filtradolistModulos = filtradolistModulos;
   }

   // Getter and Setter //
   public Empleados getEmpleado() {
      return empleado;
   }

   public VWActualesCargos getVwActualesCargos() {
      return vwActualesCargos;
   }

   public VWActualesTiposContratos getVwActualesTiposContratos() {
      return vwActualesTiposContratos;
   }

   public VWActualesNormasEmpleados getVwActualesNormasEmpleados() {
      return vwActualesNormasEmpleados;
   }

   public String getFechaActualesTiposContratos() {
      return fechaActualesTiposContratos;
   }

   public VWActualesAfiliacionesSalud getVwActualesAfiliacionesSalud() {
      return vwActualesAfiliacionesSalud;
   }

   public VWActualesAfiliacionesPension getVwActualesAfiliacionesPension() {
      return vwActualesAfiliacionesPension;
   }

   public VWActualesLocalizaciones getVwActualesLocalizaciones() {
      return vwActualesLocalizaciones;
   }

   public VWActualesTiposTrabajadores getVwActualesTiposTrabajadores() {
      return vwActualesTiposTrabajadores;
   }

   public VWActualesContratos getVwActualesContratos() {
      return vwActualesContratos;
   }

   public VWActualesJornadas getVwActualesJornadas() {
      return vwActualesJornadas;
   }

   public String getSueldo() {
      return Sueldo;
   }

   public VWActualesReformasLaborales getVwActualesReformasLaborales() {
      return vwActualesReformasLaborales;
   }

   public VWActualesUbicaciones getVwActualesUbicaciones() {
      return vwActualesUbicaciones;
   }

   public VWActualesFormasPagos getVwActualesFormasPagos() {
      return vwActualesFormasPagos;
   }

   public VWActualesVigenciasViajeros getVwActualesVigenciasViajeros() {
      return vwActualesVigenciasViajeros;
   }

   public String getNombreTabla() {
      nombreTabla = "data1";
      return nombreTabla;
   }

   public void setNombreTabla(String nombreTabla) {
      this.nombreTabla = nombreTabla;
   }

   public VWActualesTiposTrabajadores getVwActualesTiposTrabajadoresPosicion() {
      return vwActualesTiposTrabajadoresPosicion;
   }

   public String getMensaje() {
      return Mensaje;
   }

   public String getImagen() {
      return Imagen;
   }

   public DetallesEmpresas getDetallesEmpresas() {
      detallesEmpresas = administrarCarpetaPersonal.consultarDetalleEmpresaUsuario();
      return detallesEmpresas;
   }

   public Usuarios getUsuarios() {
      String alias = administrarCarpetaPersonal.consultarAliasActualUsuario();
      usuarios = administrarCarpetaPersonal.consultarUsuario(alias);
      return usuarios;
   }

   public ParametrosEstructuras getParametrosEstructuras() {
      return parametrosEstructuras;
   }

   public String getFechaDesde() {
      parametrosEstructuras = administrarCarpetaPersonal.consultarParametrosUsuario();
      FechaDesde = formato.format(parametrosEstructuras.getFechadesdecausado());
      return FechaDesde;
   }

   public String getFechaHasta() {
      FechaHasta = formato.format(parametrosEstructuras.getFechahastacausado());
      return FechaHasta;
   }

   public String getFechaSistema() {
      FechaSistema = formato.format(parametrosEstructuras.getFechasistema());
      return FechaSistema;
   }

   public List<VigenciasCargos> getVigenciasCargosEmpleados() {
      //BigInteger s = BigInteger.valueOf(10661039);
      vigenciasCargosEmpleados = administrarCarpetaPersonal.consultarVigenciasCargosEmpleado(secuencia);
      return vigenciasCargosEmpleados;
   }

   public VigenciasCargos getVigenciaSeleccionada() {
      return vigenciaSeleccionada;
   }

   public void setVigenciaSeleccionada(VigenciasCargos vigenciaSeleccionada) {
      this.vigenciaSeleccionada = vigenciaSeleccionada;
   }
//
//   public VWActualesTiposTrabajadores getTrabajador() {
//      return trabajador;
//   }

   public boolean isAcumulado() {
      return acumulado;
   }

   public boolean isNovedad() {
      return novedad;
   }

   public boolean isEvaluacion() {
      return evaluacion;
   }

   public boolean isActivo() {
      return activo;
   }

   public boolean isPensionado() {
      return pensionado;
   }

   public boolean isAspirante() {
      return aspirante;
   }

   public boolean isHv1() {
      return hv1;
   }

   public boolean isHv2() {
      return hv2;
   }

   public VwTiposEmpleados getEmplSeleccionado() {
      return emplSeleccionado;
   }

   public void setEmplSeleccionado(VwTiposEmpleados emplSeleccionado) {
      this.emplSeleccionado = emplSeleccionado;
   }

   public String getMensajePagos() {
      return mensajePagos;
   }

   public String getTituloPago() {
      return tituloPago;
   }

   public String getPago() {
      if (pago == null) {
         pago = "AUTOMATICO";
      }
      return pago;
   }

   public void setPago(String pago) {
      this.pago = pago;
   }

   public List<VwTiposEmpleados> getLovBusquedaRapida() {
      return lovBusquedaRapida;
   }

   public List<VwTiposEmpleados> getFilterBusquedaRapida() {
      return filterBusquedaRapida;
   }

   public void setFilterBusquedaRapida(List<VwTiposEmpleados> filterBusquedaRapida) {
      this.filterBusquedaRapida = filterBusquedaRapida;
   }

   public List<VwTiposEmpleados> getFilterBuscarEmpleado() {
      return filterBuscarEmpleado;
   }

   public void setFilterBuscarEmpleado(List<VwTiposEmpleados> filterBuscarEmpleado) {
      this.filterBuscarEmpleado = filterBuscarEmpleado;
   }

   public VwTiposEmpleados getEmplSeleccionadoBE() {
      return emplSeleccionadoBE;
   }

   public void setEmplSeleccionadoBE(VwTiposEmpleados emplSeleccionadoBE) {
      this.emplSeleccionadoBE = emplSeleccionadoBE;
   }

   public boolean isBuscar() {
      return buscar;
   }

   public boolean isBuscarEmp() {
      if (totalRegistros == 0) {
         buscarEmp = true;
      }
      return buscarEmp;
   }

   public boolean isMostrarT() {
      return mostrarT;
   }

   public List<VwTiposEmpleados> getLovBuscarEmplTipo() {
      return lovBuscarEmplTipo;
   }

   public UploadedFile getFile() {
      return file;
   }

   public void setFile(UploadedFile file) {
      this.file = file;
   }

   public BigInteger getIdentificacion() {
      return identificacion;
   }

   public Integer getEstadoEmpleado() {
      return estadoEmpleado;
   }

   public int getNumPestanha() {
      return numPestanha;
   }

   public void setNumPestanha(int numPestanha) {
      this.numPestanha = numPestanha;
   }

   public String getAltoModulos() {
      return altoModulos;
   }

   public String getAltoTablas() {
      return altoTablas;
   }

   public String getNombreArchivo() {
      return nombreArchivo;
   }

   public List<Tablas> getLovTablas() {
      if (lovTablas == null) {
         lovTablas = new ArrayList<Tablas>();
      }
      return lovTablas;
   }

   public void setLovTablas(List<Tablas> lovTablas) {
      this.lovTablas = lovTablas;
   }

   public List<Tablas> getLovTodasTablas() {
      return lovTodasTablas;
   }

   public void setLovTodasTablas(List<Tablas> lovTodasTablas) {
      this.lovTodasTablas = lovTodasTablas;
   }

   public List<Tablas> getFiltradoListTablasLOV() {
      return filtradoListTablasLOV;
   }

   public void setFiltradoListTablasLOV(List<Tablas> filtradoListTablasLOV) {
      this.filtradoListTablasLOV = filtradoListTablasLOV;
   }

   public Tablas getTablaSeleccionadaLOV() {
      return tablaSeleccionadaLOV;
   }

   public void setTablaSeleccionadaLOV(Tablas tablaSeleccionadaLOV) {
      this.tablaSeleccionadaLOV = tablaSeleccionadaLOV;
   }

   public String getEstadoVacaciones() {
      return estadoVacaciones;
   }

   public BigDecimal getActualMVR() {
      return actualMVR;
   }

   public void setActualMVR(BigDecimal actualMVR) {
      this.actualMVR = actualMVR;
   }

   public void setEstadoVacaciones(String estadoVacaciones) {
      this.estadoVacaciones = estadoVacaciones;
   }

   public String getActualIBC() {
      return actualIBC;
   }

   public void setActualIBC(String actualIBC) {
      this.actualIBC = actualIBC;
   }

   public String getActualSet() {
      return actualSet;
   }

   public void setActualSet(String actualSet) {
      this.actualSet = actualSet;
   }

   public String getActualComprobante() {
      return actualComprobante;
   }

   public void setActualComprobante(String actualComprobante) {
      this.actualComprobante = actualComprobante;
   }

   public String getStyleActivos() {
      return styleActivos;
   }

   public String getStylePensionados() {
      return stylePensionados;
   }

   public String getStyleRetirados() {
      return styleRetirados;
   }

   public String getStyleAspirantes() {
      return styleAspirantes;
   }

   public String getActualCargo() {
      return actualCargo;
   }

   public String getFotoEmpleado() {
      persona = administrarCarpetaPersonal.consultarFotoPersona(identificacion);
      if (persona.getPathfoto() == null || persona.getPathfoto().equalsIgnoreCase("N")) {
         fotoEmpleado = "default.png";
         return fotoEmpleado;
      } else {
         fotoEmpleado = identificacion.toString() + ".png";
         return fotoEmpleado;
      }
   }

   public void pestanhaActual(TabChangeEvent event) {
      Tab pestanha = event.getTab();
      if (pestanha.getId().equals("personal")) {
         numPestanha = 0;
      } else if (pestanha.getId().equals("nomina")) {
         numPestanha = 1;
      } else if (pestanha.getId().equals("integracion")) {
         numPestanha = 2;
      } else if (pestanha.getId().equals("gerencial")) {
         numPestanha = 3;
      } else if (pestanha.getId().equals("designer")) {
         numPestanha = 4;
      }
   }

   public void recargar() {
      RequestContext.getCurrentInstance().update("form:tablainferiorderecha");
      RequestContext.getCurrentInstance().update("form:tablainferiorizquierda");
   }

   public void activarFiltro() {
      Column columna;
      Column columna2;
      Column columna3;
      DataTable tabla;
      DataTable tabla2;
      tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tablasuperiorderecha");
      tabla2 = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tablasuperiorizquierda");
      columna = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tablasuperiorderecha:modulonombre2");
      columna2 = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tablasuperiorderecha:moduloobs2");
      columna3 = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:tablasuperiorizquierda:modulocodigo1");
      tabla.setStyle("font-size: 11px; width: 600px; position: relative;");
      tabla2.setStyle("font-size: 11px; width: 300px; position: relative; top: -6px;");
      columna.setFilterStyle("");
      columna2.setFilterStyle("");
      columna3.setFilterStyle("");
      RequestContext.getCurrentInstance().update("form:tablasuperiorderecha");
      RequestContext.getCurrentInstance().update("form:tablasuperiorizquierda");
   }

   public void navegarAParametro() {
      String automatico = "";
      if (getPago().equalsIgnoreCase("NO AUTOMATICO")) {
         automatico = "N";
      } else {
         automatico = "S";
      }
      Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
      mapParametros.put("paginaAnterior", "nominaf");
      mapParametros.put("automatico", automatico);
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlParametro controlParametro = (ControlParametro) fc.getApplication().evaluateExpressionGet(fc, "#{controlParametro}", ControlParametro.class);
      controlParametro.recibirParametros(mapParametros);
//      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      controlListaNavegacion.guardarNavegacion("nominaf", "parametro");
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "parametro");
   }

   public void cambiarFormaPago() {
      if (pago.equalsIgnoreCase("AUTOMATICO")) {
         tituloPago = "PAGOS AUTOMATICOS";
         mensajePagos = "Realice liquidaciones automáticas quincenales, mensuales, entre otras, por estructuras o por tipo de empleado. Primero ingrese los parametros a liquidar, después genere la liquidación para luego poder observar los comprobantes de pago. Usted puede deshacer todas las liquidaciones que desee siempre y cuando no se hayan cerrado. Al cerrar una liquidación se generan acumulados, por eso es importante estar seguro que la liquidación es correcta antes de cerrarla.";
         RequestContext.getCurrentInstance().update("form:tabmenu:tipopago");
         RequestContext.getCurrentInstance().update("form:tabmenu:mensajepago");
      } else if (pago.equalsIgnoreCase("NO AUTOMATICO")) {
         tituloPago = "PAGOS POR FUERA DE NÓMINA";
         mensajePagos = "Genere pagos por fuera de nómina cuando necesite liquidar vacaciones por anticipado, viaticos, entre otros. esta liquidaciones se pueden efectuar por estructura o por empleado. Primero ingrese los parametros a liquidar, después genere la liquidación para luego poder observar los comprobantes de pago. Usted puede deshacer todas las liquidaciones que desee siempre y cuando no se hayan cerrado. Al cerrar una liquidación se generan acumulados, por eso es importante estar seguro que la liquidación es correcta antes de cerrarla.";
         RequestContext.getCurrentInstance().update("form:tabmenu:tipopago");
         RequestContext.getCurrentInstance().update("form:tabmenu:mensajepago");
      }
   }

   public void buscarTablas() {
      if (moduloSeleccionado != null && listaTablas != null) {
         filtradoListTablasLOV = null;
         lovTablas.clear();
         lovTablas.addAll(listaTablas);
         RequestContext.getCurrentInstance().update("form:formlovtablas:buscartablasdialogo");
         RequestContext.getCurrentInstance().update("form:formlovtablas:lovtablas");
         RequestContext.getCurrentInstance().execute("PF('buscartablasdialogo').show()");
         contarRegistrosBT();
      }
   }

   public void seleccionTablaT() {
      tablaExportar = "data1";
      nombreArchivo = "modulos";
      listaTablas.clear();
      listaTablas.add(tablaSeleccionadaLOV);
      filtradoListTablasLOV = null;
      tablaSeleccionadaLOV = null;
      buscar = true;
      RequestContext.getCurrentInstance().update("form:formlovtablasT:lovtablasT");
      RequestContext.getCurrentInstance().reset("form:formlovtablasT:lovtablasT:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovtablasT').clearFilters()");
      RequestContext.getCurrentInstance().update("form:formlovtablasT:buscartablasdialogoT");
//      RequestContext.getCurrentInstance().execute("PF('buscartablasdialogoT').hide()");
      RequestContext.getCurrentInstance().update("form:tabmenu:tablas");
      RequestContext.getCurrentInstance().update("form:tabmenu:infoRegistroTablas");
   }

   public void seleccionTabla() {
      tablaExportar = "data1";
      nombreArchivo = "modulos";
      listaTablas.clear();
      listaTablas.add(tablaSeleccionadaLOV);
      filtradoListTablasLOV = null;
      tablaSeleccionadaLOV = null;
      buscar = true;
      RequestContext.getCurrentInstance().update("form:formlovtablas:lovtablas");
      RequestContext.getCurrentInstance().reset("form:formlovtablas:lovtablas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovtablas').clearFilters()");
      RequestContext.getCurrentInstance().update("form:formlovtablas:buscartablasdialogo");
      RequestContext.getCurrentInstance().execute("PF('buscartablasdialogo').hide()");
      RequestContext.getCurrentInstance().update("form:tabmenu:tablas");
      RequestContext.getCurrentInstance().update("form:tabmenu:infoRegistroTablas");
   }

   public void cancelarSeleccionTabla() {
      filtradoListTablasLOV = null;
      tablaSeleccionadaLOV = null;
      buscar = true;
      RequestContext.getCurrentInstance().update("form:formlovtablas:lovtablas");
      RequestContext.getCurrentInstance().reset("form:formlovtablas:lovtablas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovtablas').clearFilters()");
      RequestContext.getCurrentInstance().update("form:formlovtablas:buscartablasdialogo");
      RequestContext.getCurrentInstance().execute("PF('buscartablasdialogo').hide()");
   }

   public void cancelarSeleccionTablaT() {
      filtradoListTablasLOV = null;
      tablaSeleccionadaLOV = null;
      buscar = true;
      moduloSeleccionado = listModulos.get(0);
      cambiarTablas();
      RequestContext.getCurrentInstance().update("form:formlovtablasT:lovtablasT");
      RequestContext.getCurrentInstance().reset("form:formlovtablasT:lovtablasT:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovtablasT').clearFilters()");
      RequestContext.getCurrentInstance().update("form:formlovtablasT:buscartablasdialogoT");
      RequestContext.getCurrentInstance().execute("PF('buscartablasdialogoT').hide()");
      RequestContext.getCurrentInstance().update("form:tabmenu:data1");
   }

   public void mostrarTodo_Tablas() {
      if (lovTodasTablas == null) {
         lovTodasTablas = administrarCarpetaDesigner.consultarTablas();
      }
      moduloSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:formlovtablasT:buscartablasdialogoT");
      RequestContext.getCurrentInstance().update("form:formlovtablasT:lovtablasT");
      RequestContext.getCurrentInstance().execute("PF('buscartablasdialogoT').show()");
      RequestContext.getCurrentInstance().update("form:tabmenu:tablas");
      RequestContext.getCurrentInstance().update("form:tabmenu:data1");
      contarRegistrosBTT();
   }

   public void validarBorradoLiquidacion() {
      if (pago.equals("AUTOMATICO")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarborrarliquidacion').show()");
      } else if (pago.equals("NO AUTOMATICO")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarborrarliquidacionporfuera').show()");
      }
   }

   public void borrarLiquidacion() {
      administrarCarpetaPersonal.borrarLiquidacionAutomatico();
      mensajeResultadoBorrarLiquidacion();
   }

   public void borrarLiquidacionPorFuera() {
      administrarCarpetaPersonal.borrarLiquidacionNoAutomatico();
      mensajeResultadoBorrarLiquidacion();
   }

   public void mensajeResultadoBorrarLiquidacion() {
      FacesMessage msg = new FacesMessage("Información", "Liquidación borrada con éxito.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:growl");
   }

   public void redireccionPersonaIndividual() {
      System.out.println("Va a Redireccionar la pagina");
      FacesContext fc = FacesContext.getCurrentInstance();
//      anularBotonEmpresas();
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "personaindividual");
   }

   public void anularBotonEmpresas() {
      activarAceptarEmpresas = true;
      RequestContext.getCurrentInstance().update("formulariodialogos:aceptaremp");
   }

   public void contarRegistrosEmpresas() {
      anularBotonEmpresas();
      RequestContext.getCurrentInstance().update("formulariodialogos:infoRegistroEmpresas");
   }

   public void contarRegistrosBR() {
      RequestContext.getCurrentInstance().update("form:formlovempleadosR:inforegistrobusquedarapida");
   }

   public void contarRegistrosBT() {
      RequestContext.getCurrentInstance().update("form:formlovtablas:inforegistrobuscartablas");
   }

   public void contarRegistrosBTT() {
      RequestContext.getCurrentInstance().update("form:formlovtablasT:inforegistrobuscartablasT");
   }

   public void contarRegistrosBE() {
      RequestContext.getCurrentInstance().update("form:formlovempleados:inforegistrobuscarempleados");
   }

   public void activarAceptarEmp() {
      System.out.println("Entro en activarAceptarEmp()");
      activarAceptarEmpresas = false;
      RequestContext.getCurrentInstance().update("formulariodialogos:aceptaremp");
   }

   public void asignarUnicaEmpresa() {
      System.out.println("Entro en asignarUnicaEmpresa() lovEmpresas : " + lovEmpresas);
      if (lovEmpresas == null) {
         lovEmpresas = administrarCarpetaPersonal.consultarEmpresas();
      }
      RequestContext.getCurrentInstance().update("formulariodialogos:LovEmpresasDialogo");
      RequestContext.getCurrentInstance().update("formulariodialogos:LovEmpresasTabla");
      System.out.println("Despues de getLovEmpresas lovEmpresas : " + lovEmpresas);
      if (lovEmpresas != null) {
         if (lovEmpresas.size() >= 1) {
            unicaEmpresa = lovEmpresas.get(0);
         }
//         } else {
//            System.out.println("Va a abrir la lista de empresas");
////            RequestContext.getCurrentInstance().reset("formulariodialogos:LovEmpresasDialogo");
////            RequestContext.getCurrentInstance().reset("formulariodialogos:LovEmpresasTabla");
//            RequestContext.getCurrentInstance().execute("PF('LovEmpresasDialogo').show()");
//            contarRegistrosEmpresas();
//            FacesContext c = FacesContext.getCurrentInstance();
//            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formulariodialogos:LovEmpresasTabla");
//            tabla.updateValue(lovEmpresas);
//         }
      }
      redireccionPersonaIndividual();
      System.out.println("Unica Empresa : " + unicaEmpresa);
   }

   public void actualizarLOVEmpresas() {
      System.out.println("Entro en actualizarLOVEmpresas()");
      if (lovEmpresas == null) {
         lovEmpresas = administrarCarpetaPersonal.consultarEmpresas();
      }
      if (lovEmpresas != null) {
         if (lovEmpresas.size() == 1) {
            redireccionPersonaIndividual();
         } else {
            RequestContext.getCurrentInstance().reset("formulariodialogos:LovEmpresasDialogo");
            RequestContext.getCurrentInstance().reset("formulariodialogos:LovEmpresasTabla");
            RequestContext.getCurrentInstance().update("formulariodialogos:LovEmpresasDialogo");
            RequestContext.getCurrentInstance().update("formulariodialogos:LovEmpresasTabla");
            contarRegistrosEmpresas();
         }
      }
   }

   public boolean consultarPermisosPorId(String id) {
      int n = 0;
      if (totalRegistros == 0) {
         for (int i = 0; i < listObjetosSinEmpleados.size(); i++) {
            if (id.equals(listObjetosSinEmpleados.get(i))) {
               n++;
               break;
            }
         }
      }
      if (n > 0) {
         return true;
      } else {
         boolean enable = false;
         if (ListObjetosJSF != null) {
            if (!ListObjetosJSF.isEmpty()) {
               for (int i = 0; i < ListObjetosJSF.size(); i++) {
                  if (id.equalsIgnoreCase(ListObjetosJSF.get(i).getIdentificador())) {
                     enable = ListObjetosJSF.get(i).getEnable().equals("N");
                     i = ListObjetosJSF.size();
                  }
               }
            }
         }
         return enable;
      }
   }

   public boolean consultarRenderPorId(String id) {
      boolean enable = true;
      if (ListObjetosJSF != null) {
         if (!ListObjetosJSF.isEmpty()) {
            for (int i = 0; i < ListObjetosJSF.size(); i++) {
               if (id.equalsIgnoreCase(ListObjetosJSF.get(i).getIdentificador())) {
                  enable = ListObjetosJSF.get(i).getEnable().equals("S");
                  i = ListObjetosJSF.size();
               }
            }
         }
      }
      return enable;
   }

   public void validarIntercon() {
      mensajeinterface = " ";
      try {
         nombrepantalla = administrarCarpetaDesigner.consultarNombrePantallaPorEmpresa(Short.valueOf("902"));
      } catch (Exception e) {
         mensajeinterface = e.getMessage();
         RequestContext.getCurrentInstance().execute("PF('errorInterfaz').show()");
         nombrepantalla = " ";
      }
      System.out.println("nombre pantalla : " + nombrepantalla.toLowerCase());
      if (!nombrepantalla.equalsIgnoreCase(" ")) {
         FacesContext fc = FacesContext.getCurrentInstance();
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, nombrepantalla.toLowerCase());
         ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class
         );
         controlListaNavegacion.guardarNavegacion("nominaf", nombrepantalla.toLowerCase());
      }
   }

   /////get y set/////
   public boolean isBuscarTablasLOV() {
      if (moduloSeleccionado == null) {
         buscarTablasLOV = true;
      } else {
         buscarTablasLOV = false;
      }
      return buscarTablasLOV;
   }

   public boolean isBandera() {
      return bandera;
   }

   public String getInfoRegistroBuscarEmpleados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:formlovempleados:lvbuscarempleado");
      if (filterBuscarEmpleado != null) {
         if (filterBuscarEmpleado.size() == 1) {
            emplSeleccionadoBE = filterBuscarEmpleado.get(0);
            buscar = false;
            RequestContext.getCurrentInstance().execute("PF('lvbuscarempleado').unselectAllRows(); PF('lvbuscarempleado').selectRow(0);");
         } else {
            emplSeleccionadoBE = null;
            RequestContext.getCurrentInstance().execute("PF('lvbuscarempleado').unselectAllRows();");
         }
      } else {
         emplSeleccionadoBE = null;
         buscar = true;
      }
      infoRegistroBuscarEmpleados = String.valueOf(tabla.getRowCount());
      return infoRegistroBuscarEmpleados;
   }

   public String getInfoRegistroBusquedaRapida() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:formlovempleadosR:lvbusquedarapida");
      if (filterBusquedaRapida != null) {
         if (filterBusquedaRapida.size() == 1) {
            emplSeleccionado = filterBusquedaRapida.get(0);
            buscar = false;
            RequestContext.getCurrentInstance().execute("PF('lvbusquedarapida').unselectAllRows(); PF('lvbusquedarapida').selectRow(0);");
         } else {
            emplSeleccionado = null;
            RequestContext.getCurrentInstance().execute("PF('lvbusquedarapida').unselectAllRows();");
         }
      } else {
         emplSeleccionado = null;
         buscar = true;
      }
      infoRegistroBusquedaRapida = String.valueOf(tabla.getRowCount());
      return infoRegistroBusquedaRapida;
   }

   public String getInfoRegistroBuscarTablas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:formlovtablas:lovtablas");
      infoRegistroBuscarTablas = String.valueOf(tabla.getRowCount());
      return infoRegistroBuscarTablas;
   }

   public void setInfoRegistroBuscarTablas(String infoRegistroBuscarTablas) {
      this.infoRegistroBuscarTablas = infoRegistroBuscarTablas;
   }

   public String getInfoRegistroBuscarTablasT() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:formlovtablasT:lovtablasT");
      infoRegistroBuscarTablasT = String.valueOf(tabla.getRowCount());
      return infoRegistroBuscarTablasT;
   }

   public void setInfoRegistroBuscarTablasT(String infoRegistroBuscarTablasT) {
      this.infoRegistroBuscarTablasT = infoRegistroBuscarTablasT;
   }

   public String getInfoRegistroEmpresas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formulariodialogos:LovEmpresasTabla");
      infoRegistroEmpresas = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresas;
   }

   public String getInformacionTiposTrabajadores() {
      try {
         valorInputText();
//         RequestContext.getCurrentInstance().update("formulario:info:VCargoDesempeñado");
      } catch (Exception e) {
         System.out.println("ERROR getInformacionTiposTrabajadores() : " + e.getCause());
      }
      return informacionTiposTrabajadores;
   }

   public void setInfoRegistroBuscarEmpleados(String infoRegistroBuscarEmpleados) {
      this.infoRegistroBuscarEmpleados = infoRegistroBuscarEmpleados;
   }

   public List<BannerInicioRed> getBanner() {
      return banner;
   }

   public void setBanner(List<BannerInicioRed> banner) {
      this.banner = banner;
   }

   public List<Empresas> getFiltradoLOVEmpresas() {
      return filtradoLOVEmpresas;
   }

   public void setFiltradoLOVEmpresas(List<Empresas> filtradoLOVEmpresas) {
      this.filtradoLOVEmpresas = filtradoLOVEmpresas;
   }

   public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
      this.infoRegistroEmpresas = infoRegistroEmpresas;
   }

   public List<Empresas> getLovEmpresas() {
      if (lovEmpresas == null) {
         lovEmpresas = administrarCarpetaPersonal.consultarEmpresas();
      }
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> lovEmpresas) {
      this.lovEmpresas = lovEmpresas;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public boolean isActivarAceptarEmpresas() {
      return activarAceptarEmpresas;
   }

   public void setActivarAceptarEmpresas(boolean activarAceptarEmpresas) {
      this.activarAceptarEmpresas = activarAceptarEmpresas;
   }

   public Empresas getUnicaEmpresa() {
      if (unicaEmpresa == null) {
         getLovEmpresas();
         if (lovEmpresas != null) {
            unicaEmpresa = lovEmpresas.get(0);
         }
      }
      return unicaEmpresa;
   }

   public void setUnicaEmpresa(Empresas unicaEmpresa) {
      this.unicaEmpresa = unicaEmpresa;
   }

   public boolean isMostrarT2() {
      return mostrarT2;
   }

   public void setMostrarT2(boolean mostrarT2) {
      this.mostrarT2 = mostrarT2;
   }

   public String getInfoRegistroTablas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:tabmenu:data1");
      infoRegistroTablas = String.valueOf(tabla.getRowCount());
      return infoRegistroTablas;
   }

   public void setInfoRegistroTablas(String infoRegistroTablas) {
      this.infoRegistroTablas = infoRegistroTablas;
   }

   public String getInfoRegistroMod() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:tabmenu:tablas");
      infoRegistroMod = String.valueOf(tabla.getRowCount());
      return infoRegistroMod;
   }

   public void setInfoRegistroMod(String infoRegistroMod) {
      this.infoRegistroMod = infoRegistroMod;
   }

   public ControlListaNavegacion getControlListaNavegacion() {
      return controlListaNavegacion;
   }

   public void setControlListaNavegacion(ControlListaNavegacion controlListaNavegacion) {
      this.controlListaNavegacion = controlListaNavegacion;
   }

   public List<ObjetosJsf> getListObjetosJSF() {
      if (ListObjetosJSF == null) {
         ListObjetosJSF = administrarPermisosObjetosJsf.consultarEnable("NOMINAF");
      }
      return ListObjetosJSF;
   }

   public void setListObjetosJSF(List<ObjetosJsf> ListObjetosJSF) {
      this.ListObjetosJSF = ListObjetosJSF;
   }

   public String getMensajeinterface() {
      return mensajeinterface;
   }

   public void setMensajeinterface(String mensajeinterface) {
      this.mensajeinterface = mensajeinterface;
   }
}
