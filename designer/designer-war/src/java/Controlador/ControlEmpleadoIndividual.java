package Controlador;

import ClasesAyuda.EmpleadoIndividualExportar;
import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Demandas;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.EvalResultadosConv;
//import Entidades.Familiares;
import Entidades.Generales;
import Entidades.HVHojasDeVida;
import Entidades.HvEntrevistas;
import Entidades.HvExperienciasLaborales;
import Entidades.HvReferencias;
import Entidades.IdiomasPersonas;
import Entidades.Inforeportes;
import Entidades.InformacionesAdicionales;
import Entidades.Personas;
import Entidades.Telefonos;
import Entidades.TiposDocumentos;
import Entidades.VigenciasAficiones;
import Entidades.VigenciasDeportes;
import Entidades.VigenciasDomiciliarias;
import Entidades.VigenciasEstadosCiviles;
import Entidades.VigenciasEventos;
import Entidades.VigenciasFormales;
import Entidades.VigenciasIndicadores;
import Entidades.VigenciasProyectos;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministarReportesInterface;
import InterfaceAdministrar.AdministrarEmpleadoIndividualInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;

@ManagedBean
@SessionScoped
public class ControlEmpleadoIndividual implements Serializable {

   private static Logger log = Logger.getLogger(ControlEmpleadoIndividual.class);

   @EJB
   AdministrarEmpleadoIndividualInterface administrarEmpleadoIndividual;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministarReportesInterface administarReportes;

   private Empleados empleado;
   private Personas persona;
   private BigInteger secuencia;
   private HVHojasDeVida hojaDeVidaPersona;
   private Telefonos telefono;
   private Direcciones direccion;
   private VigenciasEstadosCiviles estadoCivil;
   private InformacionesAdicionales informacionAdicional;
   private Encargaturas encargatura;
   private VigenciasFormales vigenciaFormal;
   private IdiomasPersonas idiomasPersona;
   private VigenciasProyectos vigenciaProyecto;
   private HvReferencias hvReferenciasPersonales;
   private HvReferencias hvReferenciasFamiliares;
   private HvExperienciasLaborales experienciaLaboral;
   private VigenciasEventos vigenciaEvento;
   private VigenciasDeportes vigenciaDeporte;
   private VigenciasAficiones vigenciaAficion;
//    private Familiares familiares;
   private HvEntrevistas entrevistas;
   private VigenciasIndicadores vigenciaIndicador;
   private Demandas demandas;
   private VigenciasDomiciliarias vigenciaDomiciliaria;
   private EvalResultadosConv pruebasAplicadas;
   //CAMPOS A MOSTRAR EN LA PAGINA
   private String telefonoP, direccionP, estadoCivilP,
           informacionAdicionalP, reemplazoP, educacionP,
           idiomasP, proyectosP, referenciasPersonalesP,
           referenciasFamiliaresP, experienciaLaboralP,
           eventosP, deportesP, aficionesP, familiaresP,
           entrevistasP, indicadoresP, demandasP,
           visitasDomiciliariasP, pruebasAplicadasP;
   //CONVERTIR FECHA
   private SimpleDateFormat formatoFecha;
   //LISTAS DE VALORES
   private List<TiposDocumentos> lovTiposDocumentos;
   private List<TiposDocumentos> filtradoLovTiposDocumentos;
   private TiposDocumentos seleccionLovTipoDocumento;
   private List<Ciudades> lovCiudades, lovCiudadesDocumento;
   private List<Ciudades> filtradoListaCiudades, filtradoCiudadesDocumento;
   private Ciudades seleccionLovCiudad, seleccionLovCiudadDocumento;
   private List<Cargos> lovCargos;
   private List<Cargos> filtradoListaCargos;
   private Cargos seleccionLovCargo;
   private boolean aceptar;
   private int modificacionCiudad;
   private String cabezeraDialogoCiudad;
   private int dialogo;
   //AUTOCOMPLETAR
   private String tipoDocumento, ciudad, ciudaddocumento, cargoPostulado;
   //EXPORTAR DATOS 
   private List<EmpleadoIndividualExportar> empleadoIndividualExportar;
   //RASTRO
   private String nombreTabla;
   private BigInteger secRastro;
   //MODIFICACIÓN
   private boolean modificacionPersona, modificacionEmpleado, modificacionHV;
   //GUARDAR
   private boolean guardado;
   //FOTO EMPLEADO
   //private String fotoEmpleado;
   //private String destino = "C:\\glassfish3\\glassfish\\domains\\domain1\\applications\\DesignerRHN\\DesignerRHN-war_war\\resources\\ArchivosCargados\\";
   private String destino;
   //private String directorioDespliegue = "C:\\\\glassfish3\\\\glassfish\\\\domains\\\\domain1\\\\applications\\\\DesignerRHN\\\\DesignerRHN-war_war";
   //private String destino = directorioDespliegue + "\\resources\\ArchivosCargados\\";
   private BigInteger identificacionEmpleado;
//    private String nombreArchivoFoto;
   //VEHICULO PROPIO
   private boolean estadoVP;
   private String vehiculoPropio;
   private StreamedContent fotoEmpleado;
   private FileInputStream fis;
   private boolean existenHV;
   private boolean paraRecargar;
   //
   private String infoRegistroTipoDocumento, infoRegistroCiudad, infoRegistroCargo, infoRegistroCiudadDocumento;
   //reporte
   private StreamedContent reporte;
   private String pathReporteGenerado = null;
   private String nombreReporte, tipoReporte;
   private Inforeportes hojaVidaR;
   private String cabezeraVisor;
   private boolean estadoReporte;
   private String resultadoReporte;
   private boolean deshabilitarBotones;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private String userAgent;
   private ExternalContext externalContext;

   public ControlEmpleadoIndividual() {
      empleadoIndividualExportar = new ArrayList<EmpleadoIndividualExportar>();
      formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
      aceptar = true;
      empleado = null;
      modificacionPersona = false;
      modificacionEmpleado = false;
      modificacionHV = false;
      guardado = true;
      existenHV = true;
      deshabilitarBotones = true;
      persona = new Personas();
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
      String pagActual = "empleadoindividual";
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
      lovCargos = null;
      lovCiudades = null;
      lovCiudadesDocumento = null;
      lovTiposDocumentos = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEmpleadoIndividual.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         administarReportes.obtenerConexion(ses.getId());
         externalContext = x.getExternalContext();
         userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger sec) {
      try {
         secuencia = sec;
         empleado = null;
//        persona = administrarEmpleadoIndividual.encontrarPersona(sec);
         persona = administrarEmpleadoIndividual.obtenerPersonaPorEmpleado(sec);
         getEmpleado();
         datosEmpleado();
         getFotoEmpleado();
         lovTiposDocumentos = null;
         lovCiudades = null;
         lovCiudadesDocumento = null;
         lovCargos = null;
         guardado = true;
      } catch (Exception e) {
         log.warn("Error en recibir empleado" + e.getMessage());
      }
   }

   public void datosEmpleado() {
      try {
         BigInteger secPersona = null;
         if (persona != null) {
            secPersona = persona.getSecuencia();
         }
         BigInteger secEmpleado = empleado.getSecuencia();
         hojaDeVidaPersona = administrarEmpleadoIndividual.hvHojaDeVidaPersona(secPersona);
         if (hojaDeVidaPersona == null) {
            hojaDeVidaPersona = new HVHojasDeVida();
            hojaDeVidaPersona.setCargo(new Cargos());
         }

         if (hojaDeVidaPersona != null && hojaDeVidaPersona.getSecuencia() != null) {
            BigInteger secHv = hojaDeVidaPersona.getSecuencia();

            referenciasPersonalesP = administrarEmpleadoIndividual.consultarPrimeraReferenciaP(secHv);
            referenciasFamiliaresP = administrarEmpleadoIndividual.consultarPrimeraReferenciaF(secHv);

            experienciaLaboralP = administrarEmpleadoIndividual.consultarPrimeraExpLaboral(secHv);
            entrevistasP = administrarEmpleadoIndividual.consultarPrimeraEntrevista(secHv);
            existenHV = false;
         } else {
            referenciasPersonalesP = "";
            referenciasFamiliaresP = "";
            experienciaLaboralP = "";
            entrevistasP = "";
         }

         telefonoP = administrarEmpleadoIndividual.consultarPrimerTelefonoPersona(secPersona);
         direccionP = administrarEmpleadoIndividual.consultarPrimeraDireccionPersona(secPersona);
         estadoCivilP = administrarEmpleadoIndividual.consultarPrimerEstadoCivilPersona(secPersona);
         informacionAdicionalP = administrarEmpleadoIndividual.consultarPrimeraInformacionAd(secPersona);
         reemplazoP = administrarEmpleadoIndividual.consultarPrimerReemplazo(secPersona);
         if (reemplazoP == null || reemplazoP.isEmpty()) {
            reemplazoP = " ";
         }
         educacionP = administrarEmpleadoIndividual.consultarPrimeraVigenciaFormal(secPersona);
         idiomasP = administrarEmpleadoIndividual.consultarPimerIdioma(secPersona);
         proyectosP = administrarEmpleadoIndividual.consultarPrimerProyecto(secPersona);
         deportesP = administrarEmpleadoIndividual.consultarPrimerDeporte(secPersona);
         eventosP = administrarEmpleadoIndividual.consultarPrimerEvento(secPersona);
         aficionesP = administrarEmpleadoIndividual.consultarPrimeraAficion(secPersona);
         familiaresP = administrarEmpleadoIndividual.consultarPrimerFamiliar(secPersona);
         indicadoresP = administrarEmpleadoIndividual.consultarPrimerIndicador(secPersona);
         demandasP = administrarEmpleadoIndividual.consultarPrimeraDemanda(secPersona);
         visitasDomiciliariasP = administrarEmpleadoIndividual.consultarPrimeraVisita(secPersona);
         pruebasAplicadasP = administrarEmpleadoIndividual.consultarPrimeraPrueba(secPersona);

         if (persona.getPlacavehiculo() != null) {
            estadoVP = false;
            vehiculoPropio = "S";
         } else {
            estadoVP = true;
            vehiculoPropio = "N";
         }
      } catch (Exception e) {
         log.warn("Error en consultar Datos Empleado" + e.getCause() + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   //METODOS LOVS
   public void seleccionarTipoDocumento() {
      if (seleccionLovTipoDocumento != null && persona != null) {
         persona.setTipodocumento(seleccionLovTipoDocumento);
         seleccionLovTipoDocumento = null;
         filtradoLovTiposDocumentos = null;
         aceptar = true;
         dialogo = -1;
         RequestContext context = RequestContext.getCurrentInstance();
         if (!modificacionPersona) {
            modificacionPersona = true;
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tipo");
         context.reset("formularioDialogos:lovTiposDocumentos:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovTiposDocumentos').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('TiposDocumentosDialogo').hide()");
         //RequestContext.getCurrentInstance().update("formularioDialogos:lovTiposDocumentos");
      }
   }

   public void cancelarSeleccionTipoDocumento() {
      filtradoLovTiposDocumentos = null;
      seleccionLovTipoDocumento = null;
      aceptar = true;
      dialogo = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:lovTiposDocumentos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposDocumentos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TiposDocumentosDialogo').hide()");
   }

   public void dialogoCiudad(int modificacion) {
      RequestContext context = RequestContext.getCurrentInstance();
      modificacionCiudad = modificacion;
      if (modificacionCiudad == 0) {
         cabezeraDialogoCiudad = "Ciudad documento";
         RequestContext.getCurrentInstance().update("formularioDialogos:CiudadesDocumentoDialogo");
         RequestContext.getCurrentInstance().execute("PF('CiudadesDocumentoDialogo').show()");
      } else if (modificacionCiudad == 1) {
         cabezeraDialogoCiudad = "Ciudad nacimiento";
         RequestContext.getCurrentInstance().update("formularioDialogos:CiudadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('CiudadesDialogo').show()");
      }
   }

   public void seleccionarCiudad() {
      if (seleccionLovCiudad != null && persona != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (modificacionCiudad == 1) {
            persona.setCiudadnacimiento(seleccionLovCiudad);
            RequestContext.getCurrentInstance().update("form:lugarNacimiento");
         }
         if (!modificacionPersona) {
            modificacionPersona = true;
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         filtradoListaCiudades = null;
         aceptar = true;
         context.reset("formularioDialogos:lovCiudades:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('CiudadesDialogo').hide()");
         //RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudades");
         modificacionCiudad = -1;
         dialogo = -1;
      }
   }

   public void cancelarSeleccionCiudad() {
      filtradoListaCiudades = null;
      aceptar = true;
      modificacionCiudad = -1;
      dialogo = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:lovCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadesDialogo').hide()");
   }

   public void seleccionarCiudadDocumento() {
      if (persona != null) {
         if (persona.getCiudaddocumento() != null && seleccionLovCiudadDocumento != null) {
//                log.info("entra al if 2");
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionCiudad == 0) {
               persona.setCiudaddocumento(seleccionLovCiudadDocumento);
//                    log.info("nueva ciudad : " + seleccionLovCiudadDocumento);
//                    log.info("nueva ciudad : " + persona.getCiudaddocumento());
               RequestContext.getCurrentInstance().update("form:lugarExpedicion");
            }
            if (!modificacionPersona) {
               modificacionPersona = true;
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            filtradoCiudadesDocumento = null;
            aceptar = true;
            context.reset("formularioDialogos:lovCiudades:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('lovCiudadesDocumento').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('CiudadesDocumentoDialogo').hide()");
            //RequestContext.getCurrentInstance().update("formularioDialogos:lovCiudades");
            modificacionCiudad = -1;
            dialogo = -1;
         } else {
//                log.info("entra al else");
//                log.info("ciudadnacimiento" + persona.getCiudadnacimiento().getNombre());
            persona.setCiudaddocumento(persona.getCiudadnacimiento());
//                log.info("ciudadnacimiento" + persona.getCiudaddocumento().getNombre());
         }
      }
   }

   public void cancelarSeleccionCiudadDocumento() {
      filtradoCiudadesDocumento = null;
      aceptar = true;
      modificacionCiudad = -1;
      dialogo = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:lovCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCiudadesDocumento').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CiudadesDocumentoDialogo').hide()");
   }

   public void seleccionarCargo() {
      if (seleccionLovCargo != null && hojaDeVidaPersona != null && hojaDeVidaPersona.getSecuencia() != null) {
         hojaDeVidaPersona.setCargo(seleccionLovCargo);
         seleccionLovCargo = null;
         filtradoListaCargos = null;
         aceptar = true;
         dialogo = -1;
         RequestContext context = RequestContext.getCurrentInstance();
         if (!modificacionHV) {
            modificacionHV = true;
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:cargoPostulado");
         context.reset("formularioDialogos:lovCargos:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovCargos').clearFilters()");
         RequestContext.getCurrentInstance().execute("PF('CargosDialogo').hide()");
         //RequestContext.getCurrentInstance().update("formularioDialogos:lovCargos");
      }
   }

   public void cancelarSeleccionCargo() {
      filtradoListaCargos = null;
      seleccionLovCargo = null;
      aceptar = true;
      dialogo = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:lovCargos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCargos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CargosDialogo').hide()");
   }

   //AUTOCOMPLETAR
   public void valoresBackupAutocompletar(String Campo) {
      if (Campo.equals("TIPODOCUMENTO")) {
         tipoDocumento = persona.getTipodocumento().getNombrecorto();
         dialogo = 0;
         nombreTabla = "PERSONAS";
      } else if (Campo.equals("CIUDADDOCUMENTO")) {
         modificacionCiudad = 0;
         ciudaddocumento = persona.getCiudaddocumento().getNombre();
         dialogo = 1;
         nombreTabla = "PERSONAS";
      } else if (Campo.equals("CIUDADNACIMIENTO")) {
         modificacionCiudad = 1;
         ciudad = persona.getCiudadnacimiento().getNombre();
         dialogo = 1;
         nombreTabla = "PERSONAS";
      } else if (Campo.equals("CARGOPOSTULADO")) {
         cargoPostulado = hojaDeVidaPersona.getCargo().getNombre();
         dialogo = 2;
         nombreTabla = "PERSONAS";
      }
   }

   public void autocompletar(String confirmarCambio, String valorConfirmar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TIPODOCUMENTO")) {
         persona.getTipodocumento().setNombrecorto(tipoDocumento);
         for (int i = 0; i < lovTiposDocumentos.size(); i++) {
            if (lovTiposDocumentos.get(i).getNombrecorto().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            persona.setTipodocumento(lovTiposDocumentos.get(indiceUnicoElemento));
            RequestContext.getCurrentInstance().update("form:tipo");
            lovTiposDocumentos = null;
            getLovTiposDocumentos();
            if (!modificacionPersona) {
               modificacionPersona = true;
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:TiposDocumentosDialogo");
            RequestContext.getCurrentInstance().execute("PF('TiposDocumentosDialogo').show()");
            RequestContext.getCurrentInstance().update("form:tipo");
         }
      } else if (confirmarCambio.equalsIgnoreCase("CIUDAD")) {
         if (modificacionCiudad == 0) {
            persona.getCiudaddocumento().setNombre(ciudaddocumento);
         } else if (modificacionCiudad == 1) {
            persona.getCiudadnacimiento().setNombre(ciudad);
         }
         for (int i = 0; i < lovCiudades.size(); i++) {
            if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (modificacionCiudad == 1) {
               persona.setCiudadnacimiento(lovCiudades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("form:lugarNacimiento");
            }
            lovCiudades = null;
            getLovCiudades();

            if (!modificacionPersona) {
               modificacionPersona = true;
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            if (modificacionCiudad == 1) {
               cabezeraDialogoCiudad = "Ciudad nacimiento";
               RequestContext.getCurrentInstance().update("formularioDialogos:lugarNacimiento");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:CiudadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('CiudadesDialogo').show()");
         }
      } /*
        else if (confirmarCambio.equalsIgnoreCase("CIUDADDOCUMENTO")) {
            if (modificacionCiudad == 0) {
                persona.getCiudaddocumento().setNombre(ciudaddocumento);
            }
            for (int i = 0; i < lovCiudadesDocumento.size(); i++) {
                if (lovCiudadesDocumento.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (modificacionCiudad == 0) {
                    persona.setCiudaddocumento(lovCiudadesDocumento.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("form:lugarExpedicion");
                }
                lovCiudadesDocumento = null;
                getListaCiudadesDocumento();

                if (!modificacionPersona) {
                    modificacionPersona = true;
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            } else {
                if (modificacionCiudad == 0) {
                    cabezeraDialogoCiudad = "Ciudad documento";
                    RequestContext.getCurrentInstance().update("form:lugarExpedicion");
                }
                RequestContext.getCurrentInstance().update("formularioDialogos:CiudadesDocumentoDialogo");
                RequestContext.getCurrentInstance().execute("PF('CiudadesDocumentoDialogo').show()");
            }
        }
        
       */ else if (confirmarCambio.equalsIgnoreCase("CARGOPOSTULADO")) {
         hojaDeVidaPersona.getCargo().setNombre(cargoPostulado);
         for (int i = 0; i < lovCargos.size(); i++) {
            if (lovCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            hojaDeVidaPersona.setCargo(lovCargos.get(indiceUnicoElemento));
            RequestContext.getCurrentInstance().update("form:cargoPostulado");
            lovCargos = null;
            getLovCargos();
            if (!modificacionHV) {
               modificacionHV = true;
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:CargosDialogo");
            RequestContext.getCurrentInstance().execute("PF('CargosDialogo').show()");
            RequestContext.getCurrentInstance().update("form:cargoPostulado");
         }
      }
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
//        RequestContext context = RequestContext.getCurrentInstance();
      if (dialogo == 0) {
         RequestContext.getCurrentInstance().execute("PF('TiposDocumentosDialogo').show()");
      } else if (dialogo == 1) {
         if (modificacionCiudad == 0) {
            cabezeraDialogoCiudad = "Ciudad documento";
         } else if (modificacionCiudad == 1) {
            cabezeraDialogoCiudad = "Ciudad nacimiento";
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:CiudadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('CiudadesDialogo').show()");
      } else if (dialogo == 2) {
         RequestContext.getCurrentInstance().execute("PF('CargosDialogo').show()");
      }
   }

   public void salir() {
      refrescar();
      navegar("atras");
   }

   public void refrescar() {
      getEmpleado();
      datosEmpleado();
      RequestContext context = RequestContext.getCurrentInstance();
      guardado = true;
//        persona = null;
      empleado = null;
      RequestContext.getCurrentInstance().update("form");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDFTablasAnchas();
      exporter.export(context, tabla, "HojaVidaEmpleadoPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "HojaVidaEmpleadoXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void cambiarTablaRastro(String tabla) {
      nombreTabla = tabla;
   }

   public void verificarRastro() {
//        RequestContext context = RequestContext.getCurrentInstance();
//        int resultado = -1;
//        if (nombreTabla != null) {
//            if (nombreTabla.equals("Personas")) {
//                secRastro = persona.getSecuencia();
//                resultado = administrarRastros.obtenerTabla(secRastro, nombreTabla.toUpperCase());
//            } else if (nombreTabla.equals("Empleados")) {
//                secRastro = empleado.getSecuencia();
//                resultado = administrarRastros.obtenerTabla(secRastro, nombreTabla.toUpperCase());
//            } else if (nombreTabla.equals("HVHojasDeVida")) {
//                secRastro = hojaDeVidaPersona.getSecuencia();
//                resultado = administrarRastros.obtenerTabla(secRastro, nombreTabla.toUpperCase());
//            }
//            if (resultado == 1) {
//                RequestContext.getCurrentInstance().update("formularioDialogos:errorObjetosDB");
//                RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
//            } else if (resultado == 2) {
      RequestContext.getCurrentInstance().update("formularioDialogos:confirmarRastro");
      RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
//            } else if (resultado == 3) {
//                RequestContext.getCurrentInstance().update("formularioDialogos:errorRegistroRastro");
//                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
//            } else if (resultado == 4) {
//                RequestContext.getCurrentInstance().update("formularioDialogos:errorTablaConRastro");
//                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
//            } else if (resultado == 5) {
//                RequestContext.getCurrentInstance().update("formularioDialogos:errorTablaSinRastro");
//                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
//            }
//        } else {
//            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//        }
   }

   //MODIFICACION
   public void eventoDataSelectFechaNacimiento(String tipoCampo) {
//        log.info(this.getClass().getName() + ".eventoDataSelectFechaNacimiento");
      if (persona.getFechanacimiento() != null) {
         if (tipoCampo.equals("P")) {
            if (modificacionPersona == false) {
               modificacionPersona = true;
            }
         } else if (tipoCampo.equals("E")) {
            if (modificacionEmpleado == false) {
               modificacionEmpleado = true;
            }
         } else if (tipoCampo.equals("HV")) {
            if (!modificacionHV) {
               modificacionHV = true;
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void eventoDataSelectFechaVencimiento(String tipoCampo) {
      if (persona.getFechavencimientocertificado() != null) {
         if (tipoCampo.equals("P")) {
            if (modificacionPersona == false) {
               modificacionPersona = true;
            }
         } else if (tipoCampo.equals("E")) {
            if (modificacionEmpleado == false) {
               modificacionEmpleado = true;
            }
         } else if (tipoCampo.equals("HV")) {
            if (!modificacionHV) {
               modificacionHV = true;
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void eventoDataSelectFechaFallecimiento(String tipoCampo) {
      if (persona.getFechafallecimiento() != null) {
         if (tipoCampo.equals("P")) {
            if (modificacionPersona == false) {
               modificacionPersona = true;
            }
         } else if (tipoCampo.equals("E")) {
            if (modificacionEmpleado == false) {
               modificacionEmpleado = true;
            }
         } else if (tipoCampo.equals("HV")) {
            if (!modificacionHV) {
               modificacionHV = true;
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void modificarCampo(String tipoCampo) {
      if (tipoCampo.equals("P")) {
         if (!modificacionPersona) {
            modificacionPersona = true;
         }
      } else if (tipoCampo.equals("E")) {
         if (!modificacionEmpleado) {
            modificacionEmpleado = true;
         }
      } else if (tipoCampo.equals("HV")) {
         if (!modificacionHV) {
            modificacionHV = true;
         }
      }
      if (guardado) {
         guardado = false;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void guardarYSalir() {
      guardarCambios();
      salir();
   }

   //GUARDAR CAMBIOS
   public void guardarCambios() {
//        RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (!guardado) {
            if (modificacionPersona) {
               administrarEmpleadoIndividual.modificarPersona(persona);
               modificacionPersona = false;
            }
            if (modificacionEmpleado) {
               administrarEmpleadoIndividual.modificarEmpleado(empleado);
               modificacionEmpleado = false;
            }
            if (modificacionHV) {
               if (hojaDeVidaPersona.getCargo().getSecuencia() == null) {
                  hojaDeVidaPersona.setCargo(null);
               }
               if (hojaDeVidaPersona.getSecuencia() == null) {
                  hojaDeVidaPersona.setSecuencia(BigInteger.valueOf(0));
                  hojaDeVidaPersona.setPersona(persona);
                  administrarEmpleadoIndividual.modificarHojaDeVida(hojaDeVidaPersona);
               } else {
                  administrarEmpleadoIndividual.modificarHojaDeVida(hojaDeVidaPersona);
               }
               modificacionHV = false;
            }
            empleado = null;
            persona = null;
            getEmpleado();
            datosEmpleado();
            guardado = true;
            RequestContext.getCurrentInstance().update("form");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //SUBIR FOTO EMPLEADO
   public void subirFotoEmpleado(FileUploadEvent event) throws IOException {
      RequestContext context = RequestContext.getCurrentInstance();
      String extension = event.getFile().getFileName().split("[.]")[1];
      Long tamanho = event.getFile().getSize();
      if (extension.equals("jpg") || extension.equals("JPG")) {
         if (tamanho <= 307200) {
            Generales general = administrarEmpleadoIndividual.obtenerRutaFoto();
            if (general != null && persona != null) {
               destino = general.getPathfoto();
               identificacionEmpleado = persona.getNumerodocumento();
               transformarArchivo(tamanho, event.getFile().getInputstream());
               RequestContext.getCurrentInstance().execute("PF('subirFoto').hide()");
               RequestContext.getCurrentInstance().update("form:btnFoto");
               FacesMessage msg = new FacesMessage("Información", "Archivo cargado con éxito.");
               FacesContext fc = FacesContext.getCurrentInstance();
               fc.addMessage(null, msg);
               RequestContext.getCurrentInstance().update("form:growl");
            } else {
               FacesMessage msg = new FacesMessage("Información", "Ruta generales ó empleado, nulo.");
               FacesContext fc = FacesContext.getCurrentInstance();
               fc.addMessage(null, msg);
               RequestContext.getCurrentInstance().update("form:growl");
            }
         } else {
            FacesMessage msg = new FacesMessage("Información", "El tamaño maximo permitido es de 300 KB.");
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } else {
         FacesMessage msg = new FacesMessage("Información", "Solo se admiten imagenes con formato (.JPG).");
         FacesContext fc = FacesContext.getCurrentInstance();
         fc.addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }

      /*HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

         response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
         response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
         response.setDateHeader("Expires", 0); // Proxies. */
      //RequestContext.getCurrentInstance().execute("PF('espera').hide()");
   }

   public void transformarArchivo(long size, InputStream in) {
      try {
         //extension = fileName.split("[.]")[1];
         //log.info(extension); 
         OutputStream out = new FileOutputStream(new File(destino + identificacionEmpleado + ".jpg"));
         int reader = 0;
         byte[] bytes = new byte[(int) size];
         while ((reader = in.read(bytes)) != -1) {
            out.write(bytes, 0, reader);
         }
         in.close();
         out.flush();
         out.close();
         administrarEmpleadoIndividual.actualizarFotoPersona(persona);
      } catch (IOException e) {
         log.warn("Error: ControlEmpleadoIndividual.transformarArchivo: " + e);
      }
   }

   //VEHICULO PROPIO DINAMICO
   public void estadoVehiculoPropio() {
//        RequestContext context = RequestContext.getCurrentInstance();
      if (vehiculoPropio.equals("S")) {
         estadoVP = false;
         RequestContext.getCurrentInstance().update("form:placa");
      } else {
         estadoVP = true;
         persona.setPlacavehiculo(null);
         modificarCampo("P");
         RequestContext.getCurrentInstance().update("form:placa");
      }
   }

   public void validarRedirigirReferencias(String pagina) {
      if (hojaDeVidaPersona != null) {
         if (hojaDeVidaPersona.getPerfilprofesional() == null || hojaDeVidaPersona.getPerfilprofesional().isEmpty()) {
            RequestContext.getCurrentInstance().execute("PF('validarPerfilProfesional').show()");
         } else {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pagina);
            ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
            controlListaNavegacion.guardarNavegacion("empleadoindividual", pagina);

         }
      }
   }

   public AsynchronousFilllListener listener() {
      log.info(this.getClass().getName() + ".listener()");
      return new AsynchronousFilllListener() {
         //RequestContext context = c;

         @Override
         public void reportFinished(JasperPrint jp) {
            log.info(this.getClass().getName() + ".listener().reportFinished()");
            try {
               estadoReporte = true;
               resultadoReporte = "Exito";
            } catch (Exception e) {
               log.info("ControlNReporteNomina reportFinished ERROR: " + e.toString());
            }
         }

         @Override
         public void reportCancelled() {
            log.info(this.getClass().getName() + ".listener().reportCancelled()");
            estadoReporte = true;
            resultadoReporte = "Cancelación";
         }

         @Override
         public void reportFillError(Throwable e) {
            log.info(this.getClass().getName() + ".listener().reportFillError()");
            if (e.getCause() != null) {
               pathReporteGenerado = "ControlEmpleadoIndividual reportFillError Error: " + e.toString() + "\n" + e.getCause().toString();
            } else {
               pathReporteGenerado = "ControlEmpleadoIndividual reportFillError Error: " + e.toString();
            }
            estadoReporte = true;
            resultadoReporte = "Se estallo";
         }
      };
   }

   public void validarDescargaReporte() {
      try {
         log.info(this.getClass().getName() + ".validarDescargaReporte()");
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').show()");
         RequestContext context = RequestContext.getCurrentInstance();
         nombreReporte = "HojaVidaPorEmpleado";
         tipoReporte = "PDF";
         log.info("nombre reporte : " + nombreReporte);
         log.info("tipo reporte: " + tipoReporte);
         BigDecimal secPersona = new BigDecimal(persona.getSecuencia());

         Map param = new HashMap();
         param.put("secEmpleado", secPersona);
         pathReporteGenerado = null;
         RequestContext.getCurrentInstance().execute("PF('generandoReporte').hide()");
         pathReporteGenerado = administarReportes.generarReporteHojaVida(nombreReporte, tipoReporte, param);
         if (pathReporteGenerado != null && !pathReporteGenerado.startsWith("Error:")) {
            log.info("validar descarga reporte - ingreso al if 1");
            if (tipoReporte.equals("PDF")) {
               log.info("validar descarga reporte - ingreso al if 2 else");
               FileInputStream fis;
               try {
                  log.info("pathReporteGenerado : " + pathReporteGenerado);
                  fis = new FileInputStream(new File(pathReporteGenerado));
                  log.info("fis : " + fis);
                  reporte = new DefaultStreamedContent(fis, "application/pdf");
                  log.info("reporte despues de esto : " + reporte);
                  if (reporte != null) {
                     log.info("userAgent: " + userAgent);
                     log.info("validar descarga reporte - ingreso al if 4");
                     if (userAgent.toUpperCase().contains("Mobile".toUpperCase()) || userAgent.toUpperCase().contains("Tablet".toUpperCase()) || userAgent.toUpperCase().contains("Android".toUpperCase())) {
                        context.update("formularioDialogos:descargarReporte");
                        context.execute("PF('descargarReporte').show();");
                     } else {
                        cabezeraVisor = "Reporte - " + nombreReporte;
                        RequestContext.getCurrentInstance().update("formularioDialogos:verReportePDF");
                        RequestContext.getCurrentInstance().execute("PF('verReportePDF').show()");
                     }
                  }
               } catch (FileNotFoundException ex) {
                  log.warn("Error en validar descargar reporte : " + ex.getMessage());
                  RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
                  RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
                  reporte = null;
               }
            }
         } else {
            log.info("validar descarga reporte - ingreso al if 1 else");
            RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         log.warn("Error en validar descargar Reporte" + e.toString());
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void reiniciarStreamedContent() {
      reporte = null;
   }

   public void cancelarReporte() {
      log.info(this.getClass().getName() + ".cancelarReporte()");
      administarReportes.cancelarReporte();
   }

   public void exportarReporte() throws IOException {
      try {
         log.info("Controlador.ControlInterfaseContableTotal.exportarReporte()   path generado : " + pathReporteGenerado);
         if (pathReporteGenerado != null || !pathReporteGenerado.startsWith("Error:")) {
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
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
            RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
         }
      } catch (Exception e) {
         log.warn("Error en exportarReporte :" + e.getMessage());
         RequestContext.getCurrentInstance().update("formularioDialogos:errorGenerandoReporte");
         RequestContext.getCurrentInstance().execute("PF('errorGenerandoReporte').show()");
      }
   }

   public void contarRegistrosCiudades() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudad");
   }

   public void contarRegistrosCiudadesDocumentos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudadDocumento");
   }

   public void contarRegistrosTiposDocumentos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTipoDocumento");
   }

   public void contarRegistrosCargos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCargo");
   }

//GETTER AND SETTER
   public Empleados getEmpleado() {
      if (empleado == null) {
         empleado = administrarEmpleadoIndividual.buscarEmpleado(secuencia);
         if (empleado != null) {
            persona = empleado.getPersona();
         }
      }
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public HVHojasDeVida getHojaDeVidaPersona() {
      return hojaDeVidaPersona;
   }

   public void setHojaDeVidaPersona(HVHojasDeVida hojaDeVidaPersona) {
      this.hojaDeVidaPersona = hojaDeVidaPersona;
   }

   public void setTelefono(Telefonos telefono) {
      this.telefono = telefono;
   }

   public void setDireccion(Direcciones direccion) {
      this.direccion = direccion;
   }

   public void setEstadoCivil(VigenciasEstadosCiviles estadoCivil) {
      this.estadoCivil = estadoCivil;
   }

   public void setInformacionAdicional(InformacionesAdicionales informacionAdicional) {
      this.informacionAdicional = informacionAdicional;
   }

   public void setEncargatura(Encargaturas encargatura) {
      this.encargatura = encargatura;
   }

   public void setVigenciaFormal(VigenciasFormales vigenciaFormal) {
      this.vigenciaFormal = vigenciaFormal;
   }

   public void setIdiomasPersona(IdiomasPersonas idiomasPersona) {
      this.idiomasPersona = idiomasPersona;
   }

   public void setVigenciaProyecto(VigenciasProyectos vigenciaProyecto) {
      this.vigenciaProyecto = vigenciaProyecto;
   }

   public HvReferencias getHvReferenciasPersonales() {
      return hvReferenciasPersonales;
   }

   public void setHvReferenciasPersonales(HvReferencias hvReferenciasPersonales) {
      this.hvReferenciasPersonales = hvReferenciasPersonales;
   }

   public void setHvReferenciasFamiliares(HvReferencias hvReferenciasFamiliares) {
      this.hvReferenciasFamiliares = hvReferenciasFamiliares;
   }

   public void setExperienciaLaboral(HvExperienciasLaborales experienciaLaboral) {
      this.experienciaLaboral = experienciaLaboral;
   }

   public void setVigenciaEvento(VigenciasEventos vigenciaEvento) {
      this.vigenciaEvento = vigenciaEvento;
   }

   public void setVigenciaDeporte(VigenciasDeportes vigenciaDeporte) {
      this.vigenciaDeporte = vigenciaDeporte;
   }

   public void setVigenciaAficion(VigenciasAficiones vigenciaAficion) {
      this.vigenciaAficion = vigenciaAficion;
   }

   /*public void setFamiliares(Familiares familiares) {
        this.familiares = familiares;
    }*/
   public void setEntrevistas(HvEntrevistas entrevistas) {
      this.entrevistas = entrevistas;
   }

   public void setVigenciaIndicador(VigenciasIndicadores vigenciaIndicador) {
      this.vigenciaIndicador = vigenciaIndicador;
   }

   public void setDemandas(Demandas demandas) {
      this.demandas = demandas;
   }

   public void setVigenciaDomiciliaria(VigenciasDomiciliarias vigenciaDomiciliaria) {
      this.vigenciaDomiciliaria = vigenciaDomiciliaria;
   }

   public void setPruebasAplicadas(EvalResultadosConv pruebasAplicadas) {
      this.pruebasAplicadas = pruebasAplicadas;
   }

   public String getTelefonoP() {
      return telefonoP;
   }

   public String getDireccionP() {
      return direccionP;
   }

   public String getEstadoCivilP() {
      return estadoCivilP;
   }

   public String getInformacionAdicionalP() {
      return informacionAdicionalP;
   }

   public String getReemplazoP() {
      return reemplazoP;
   }

   public String getEducacionP() {
      return educacionP;
   }

   public String getIdiomasP() {
      return idiomasP;
   }

   public String getProyectosP() {
      return proyectosP;
   }

   public String getReferenciasPersonalesP() {
      return referenciasPersonalesP;
   }

   public String getReferenciasFamiliaresP() {
      return referenciasFamiliaresP;
   }

   public String getExperienciaLaboralP() {
      return experienciaLaboralP;
   }

   public String getEventosP() {
      return eventosP;
   }

   public String getDeportesP() {
      return deportesP;
   }

   public String getAficionesP() {
      return aficionesP;
   }

   public String getFamiliaresP() {
      return familiaresP;
   }

   public String getEntrevistasP() {
      return entrevistasP;
   }

   public String getIndicadoresP() {
      return indicadoresP;
   }

   public String getDemandasP() {
      return demandasP;
   }

   public String getVisitasDomiciliariasP() {
      return visitasDomiciliariasP;
   }

   public String getPruebasAplicadasP() {
      return pruebasAplicadasP;
   }

   //LISTAS DE VALORES
   public List<TiposDocumentos> getLovTiposDocumentos() {
      if (lovTiposDocumentos == null) {
         lovTiposDocumentos = administrarEmpleadoIndividual.tiposDocumentos();
      }
      return lovTiposDocumentos;
   }

   public void setLovTiposDocumentos(List<TiposDocumentos> lovTiposDocumentos) {
      this.lovTiposDocumentos = lovTiposDocumentos;
   }

   public List<TiposDocumentos> getFiltradoLovTiposDocumentos() {
      return filtradoLovTiposDocumentos;
   }

   public void setFiltradoLovTiposDocumentos(List<TiposDocumentos> filtradoLovTiposDocumentos) {
      this.filtradoLovTiposDocumentos = filtradoLovTiposDocumentos;
   }

   public TiposDocumentos getSeleccionLovTipoDocumento() {
      return seleccionLovTipoDocumento;
   }

   public void setSeleccionLovTipoDocumento(TiposDocumentos seleccionLovTipoDocumento) {
      this.seleccionLovTipoDocumento = seleccionLovTipoDocumento;
   }

   public List<Ciudades> getLovCiudades() {
      if (lovCiudades == null) {
         lovCiudades = administrarEmpleadoIndividual.ciudades();
      }
      return lovCiudades;
   }

   public void setLovCiudades(List<Ciudades> lovCiudades) {
      this.lovCiudades = lovCiudades;
   }

   public List<Ciudades> getFiltradoListaCiudades() {
      return filtradoListaCiudades;
   }

   public void setFiltradoListaCiudades(List<Ciudades> filtradoListaCiudades) {
      this.filtradoListaCiudades = filtradoListaCiudades;
   }

   public Ciudades getSeleccionCiudades() {
      return seleccionLovCiudad;
   }

   public void setSeleccionCiudades(Ciudades seleccionCiudades) {
      this.seleccionLovCiudad = seleccionCiudades;
   }

   public Ciudades getSeleccionLovCiudadDocumento() {
      return seleccionLovCiudadDocumento;
   }

   public void setSeleccionLovCiudadDocumento(Ciudades seleccionLovCiudadDocumento) {
      this.seleccionLovCiudadDocumento = seleccionLovCiudadDocumento;
   }

   public List<Cargos> getLovCargos() {
      if (lovCargos == null) {
         lovCargos = administrarEmpleadoIndividual.cargos();
      }
      return lovCargos;
   }

   public void setLovCargos(List<Cargos> lovCargos) {
      this.lovCargos = lovCargos;
   }

   public List<Cargos> getFiltradoListaCargos() {
      return filtradoListaCargos;
   }

   public void setFiltradoListaCargos(List<Cargos> filtradoListaCargos) {
      this.filtradoListaCargos = filtradoListaCargos;
   }

   public Cargos getSeleccionLovCargo() {
      return seleccionLovCargo;
   }

   public void setSeleccionLovCargo(Cargos seleccionLovCargo) {
      this.seleccionLovCargo = seleccionLovCargo;
   }

   public String getCabezeraDialogoCiudad() {
      return cabezeraDialogoCiudad;
   }

   public List<EmpleadoIndividualExportar> getEmpleadoIndividualExportar() {
      if (!empleadoIndividualExportar.equals(null)) {
         if (!empleadoIndividualExportar.isEmpty()) {
            empleadoIndividualExportar.clear();
         }
      }
      empleadoIndividualExportar.add(new EmpleadoIndividualExportar(empleado, persona, hojaDeVidaPersona));
      return empleadoIndividualExportar;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setPruebasAplicadasP(String pruebasAplicadasP) {
      this.pruebasAplicadasP = pruebasAplicadasP;
   }

   public String getNombreTabla() {
      return nombreTabla;
   }

   public BigInteger getSecRastro() {
      return secRastro;
   }

   public void setSecRastro(BigInteger secRastro) {
      this.secRastro = secRastro;
   }

   public boolean isGuardado() {
//        refrescar();
      return guardado;
   }

   public boolean isEstadoVP() {
      return estadoVP;
   }

   public String getVehiculoPropio() {
      return vehiculoPropio;
   }

   public void setVehiculoPropio(String vehiculoPropio) {
      this.vehiculoPropio = vehiculoPropio;
   }

   public StreamedContent getFotoEmpleado() {
      obtenerFotoEmpleado();
//        refrescarDatos();
      return fotoEmpleado;
   }

   public boolean isExistenHV() {
      return existenHV;
   }

   public Personas getPersona() {
      return persona;
   }

   public void setPersona(Personas persona) {
      this.persona = persona;
   }

   public void obtenerFotoEmpleado() {
      empleado = administrarEmpleadoIndividual.buscarEmpleado(secuencia);
      String rutaFoto = administrarEmpleadoIndividual.fotoEmpleado(empleado);
      if (rutaFoto != null) {
         try {
            fis = new FileInputStream(new File(rutaFoto));
            fotoEmpleado = new DefaultStreamedContent(fis, "image/jpg");
         } catch (FileNotFoundException e) {
            fotoEmpleado = null;
            log.info("Foto del empleado no encontrada. \n" + e);
         }
      } else {
         log.info("la ruta de la foto del empleado es nula");
      }
   }

   public void refrescarDatos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (context != null) {
         datosEmpleado();
         RequestContext.getCurrentInstance().update("form:panelInferior");
      }
   }

   public String getInfoRegistroTipoDocumento() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTiposDocumentos");
      infoRegistroTipoDocumento = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoDocumento;
   }

   public void setInfoRegistroTipoDocumento(String infoRegistroTipoDocumento) {
      this.infoRegistroTipoDocumento = infoRegistroTipoDocumento;
   }

   public String getInfoRegistroCiudad() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCiudades");
      infoRegistroCiudad = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudad;
   }

   public void setInfoRegistroCiudad(String infoRegistroCiudad) {
      this.infoRegistroCiudad = infoRegistroCiudad;
   }

   public String getInfoRegistroCargo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCargos");
      infoRegistroCargo = String.valueOf(tabla.getRowCount());
      return infoRegistroCargo;
   }

   public void setInfoRegistroCargo(String infoRegistroCargo) {
      this.infoRegistroCargo = infoRegistroCargo;
   }

   public List<Ciudades> getLovCiudadesDocumento() {
      if (lovCiudadesDocumento == null) {
         lovCiudadesDocumento = administrarEmpleadoIndividual.ciudades();
      }
      return lovCiudadesDocumento;
   }

   public void setLovCiudadesDocumento(List<Ciudades> lovCiudadesDocumento) {
      this.lovCiudadesDocumento = lovCiudadesDocumento;
   }

   public List<Ciudades> getFiltradoCiudadesDocumento() {
      return filtradoCiudadesDocumento;
   }

   public void setFiltradoCiudadesDocumento(List<Ciudades> filtradoCiudadesDocumento) {
      this.filtradoCiudadesDocumento = filtradoCiudadesDocumento;
   }

   public String getInfoRegistroCiudadDocumento() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCiudadesDocumento");
      infoRegistroCiudadDocumento = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudadDocumento;
   }

   public void setInfoRegistroCiudadDocumento(String infoRegistroCiudadDocumento) {
      this.infoRegistroCiudadDocumento = infoRegistroCiudadDocumento;
   }

   public String getCiudad() {
      return ciudad;
   }

   public void setCiudad(String ciudad) {
      this.ciudad = ciudad;
   }

   public String getCiudaddocumento() {
//        if(ciudaddocumento == null){
//            ciudaddocumento = persona.getCiudadnacimiento().getNombre();
//        }
      return ciudaddocumento;
   }

   public void setCiudaddocumento(String ciudaddocumento) {
      this.ciudaddocumento = ciudaddocumento;
   }

   public boolean isParaRecargar() {
      recibirEmpleado(secuencia);
      return paraRecargar;
   }

   public void setParaRecargar(boolean paraRecargar) {
      this.paraRecargar = paraRecargar;
   }

   private void imprimir(String etiqueta, String texto) {
      if (false) {
         log.info(etiqueta + " " + texto);
      }
   }

   public FileInputStream getFis() {
      return fis;
   }

   public void setFis(FileInputStream fis) {
      this.fis = fis;
   }

   public StreamedContent getReporte() {
      return reporte;
   }

   public void setReporte(StreamedContent reporte) {
      this.reporte = reporte;
   }

   public String getPathReporteGenerado() {
      return pathReporteGenerado;
   }

   public void setPathReporteGenerado(String pathReporteGenerado) {
      this.pathReporteGenerado = pathReporteGenerado;
   }

   public String getNombreReporte() {
      return nombreReporte;
   }

   public void setNombreReporte(String nombreReporte) {
      this.nombreReporte = nombreReporte;
   }

   public String getTipoReporte() {
      return tipoReporte;
   }

   public void setTipoReporte(String tipoReporte) {
      this.tipoReporte = tipoReporte;
   }

   public Inforeportes getHojaVidaR() {
      return hojaVidaR;
   }

   public void setHojaVidaR(Inforeportes hojaVidaR) {
      this.hojaVidaR = hojaVidaR;
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

   public String getResultadoReporte() {
      return resultadoReporte;
   }

   public void setResultadoReporte(String resultadoReporte) {
      this.resultadoReporte = resultadoReporte;
   }

   public boolean isDeshabilitarBotones() {
      getEmpleado();
      datosEmpleado();
      return deshabilitarBotones;
   }

   public void setDeshabilitarBotones(boolean deshabilitarBotones) {
      this.deshabilitarBotones = deshabilitarBotones;
   }

}
