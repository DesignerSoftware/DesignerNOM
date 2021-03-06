package Controlador;

import Banner.BannerInicioRed;
import ClasesAyuda.CadenasDesignerInn;
import ClasesAyuda.LeerArchivoXML;
import Entidades.Conexiones;
import Entidades.Recordatorios;
import InterfaceAdministrar.AdministrarInicioRedInterface;
import java.io.IOException;
//import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
//import org.primefaces.model.DefaultStreamedContent;
//import org.primefaces.model.StreamedContent;

@Named("controlInicioRed")
//@ManagedBean
@SessionScoped
public class ControlInicioRed implements Serializable {

   private static Logger log = Logger.getLogger(ControlInicioRed.class);

   @EJB
   AdministrarInicioRedInterface administrarInicioRed;
   private String usuario, contraseña, baseDatos;
   private boolean estadoInicio, cambioClave, modulosDesigner;
   private String txtBoton;
   private List<String> actualizaciones;
   private List<BannerInicioRed> banner;
   private boolean inicioSession;
   //FECHA ACTUAL
   private Date fechaActual;
   private SimpleDateFormat formatoFechaActual, formatoAño;
   private String mostrarFecha, añoActual;
   //RECORDATORIOS
   private Recordatorios recordatorio;
   private List<String> listaRecordatorios;
   private List<Recordatorios> listaConsultas;
   private boolean acceso;
   //NOMBRE EMPRESA
   private String nombreEmpresa;
   //CAMBIO CLAVE
   private String NClave, Rclave;
   private String msgSesion;
   private String candadoLogin;

   private static List<CadenasDesignerInn> listaCadenasDesignerInn;
   private static CadenasDesignerInn cadena;

   public ControlInicioRed() {
      cadena = new CadenasDesignerInn("1", "N", "N", "N");
      try {
         log.info(this.getClass().getName() + ".constructor()");
         inicializarCampos();
         asignarImagenCandado(false);
         inicializarListaActualizaciones();
         llenarBannerSinEntrar();
         listaCadenasDesignerInn = (new LeerArchivoXML()).leerArchivoDesignerInn();
         if (listaCadenasDesignerInn != null) {
            if (!listaCadenasDesignerInn.isEmpty()) {
               for (CadenasDesignerInn recCadena : listaCadenasDesignerInn) {
                  log.warn("recCadena.getId(): " + recCadena.getId() + ", getNombrepoolJPA: " + recCadena.getNombrepoolJPA() + ", getBd: " + recCadena.getBd() + ", getUsapool: " + recCadena.getUsapool());
               }
            }
         }
         log.info("estadoinicio constructor: " + estadoInicio);
      } catch (Exception e) {
         log.fatal("ControlInicioRed.<init>() ", e);
      }
   }

   private void inicializarCampos() {
      cambioClave = true;
      estadoInicio = false;
      modulosDesigner = true;
      txtBoton = "Conectar";
      listaConsultas = new ArrayList<>();
      //FECHA ACTUAL
      formatoFechaActual = new SimpleDateFormat("EEEEE dd 'de' MMMMM 'de' yyyy");
      formatoAño = new SimpleDateFormat("yyyy");
      //INICIO SESSION DEFAULT
      inicioSession = true;
      acceso = false;
      msgSesion = "Iniciando sesión, por favor espere...";
   }

   private void inicializarListaActualizaciones() {
      actualizaciones = new ArrayList<>();
      actualizaciones.add("form:btnLogin");
      actualizaciones.add("form:btnCambioClave");
      actualizaciones.add("form:btnPersonal");
      actualizaciones.add("form:btnNomina");
      actualizaciones.add("form:btnIntegracion");
      actualizaciones.add("form:btnGerencial");
      actualizaciones.add("form:btnDesigner");
      actualizaciones.add("form:nombreEmpresa");
      actualizaciones.add("form:usuario");
      actualizaciones.add("form:contrasenha");
      actualizaciones.add("form:baseDatos");
      actualizaciones.add("form:bannerConsultas");
      actualizaciones.add("form:btnProverbio");
   }

   public CadenasDesignerInn validarCadenas() {
      cadena.setBd(baseDatos);
      cadena.setNombrepoolJPA(baseDatos);
      try {
         for (CadenasDesignerInn recCadena : listaCadenasDesignerInn) {
            if (recCadena.getBd().equals(baseDatos) && recCadena.getUsapool().equals("S")) {
               cadena.setBd(recCadena.getBd());
               cadena.setId(recCadena.getId());
               cadena.setNombrepoolJPA(recCadena.getNombrepoolJPA());
               cadena.setUsapool(recCadena.getUsapool());
               break;
            }
         }
         return cadena;
      } catch (Exception e) {
         log.fatal("ControlInicioRed.obtenerCadenas() ", e);
         return null;
      }
   }

   public void ingresar() throws IOException {
      try {
//         RequestContext context = RequestContext.getCurrentInstance();
         FacesContext contextoF = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) contextoF.getExternalContext().getSession(false);
         log.info("Ses= " + ses);
         log.info("estado sesion ingreso = " + estadoInicio);
         if (!estadoInicio) {
            // Iniciar sesión.
            if (!baseDatos.equals("") && !usuario.equals("") && !contraseña.equals("")) {
               // Utiliza los datos ingresados // Crea una sesion con entrada // Si no usa pool usa el nombre de la BD por default
               if (administrarInicioRed.conexionInicial((validarCadenas()).getNombrepoolJPA())) {
                  // La base de datos fue verificada // Da RollEntrada -> compruba el usuario
                  if (administrarInicioRed.validarUsuario(usuario, cadena.getUsapool())) {
                     // El usuario fue verificado // Consulta perfil del usuario -> crea la conexion con el usuario y password
                     if (administrarInicioRed.conexionUsuario(baseDatos, usuario, contraseña, cadena.getUsapool(), ses.getId())) {
                        // La contraseña fue validada // Valida la conexion -> setea el roll -> crea el SessionEntityManager -> lo agraga a AdministrarSesiones
                        if (administrarInicioRed.validarConexionUsuario(ses.getId())) {
                           // La sesión fue otorgada
                           cambioClave = false;
                           estadoInicio = true;
                           modulosDesigner = false;
                           txtBoton = "Desconectar";
                           asignarImagenCandado(true);
                           acceso = true;
                           getNombreEmpresa();
                           obtenerRecordatorios();
                           obtenerConsultas();
                           HttpServletRequest request = (HttpServletRequest) (contextoF.getExternalContext().getRequest());
                           imprimirRequest(request);
                           String Ip, nombreEquipo;
                           java.net.InetAddress localMachine;
                           if (request.getRemoteAddr().startsWith("127.0.0.1")) {
                              localMachine = java.net.InetAddress.getLocalHost();
                              Ip = localMachine.getHostAddress();
                           } else {
                              Ip = request.getRemoteAddr();
                           }
                           localMachine = java.net.InetAddress.getByName(Ip);
                           nombreEquipo = localMachine.getHostName();
                           Conexiones conexion = new Conexiones();
                           conexion.setDireccionip(Ip);
                           conexion.setEstacion(nombreEquipo);
                           conexion.setSecuencia(BigInteger.valueOf(1));
                           conexion.setUltimaentrada(new Date());
                           conexion.setUsuarioso(System.getProperty("os.name") + " / " + System.getProperty("user.name"));
                           conexion.setUsuariobd(administrarInicioRed.usuarioBD());
                           administrarInicioRed.guardarDatosConexion(conexion);
                           RequestContext.getCurrentInstance().update(actualizaciones);
                        } else {
                           inicioSession = true;
                           sessionEntradaDefault();
                           contextoF.addMessage(null, new FacesMessage("", "La contraseña puede ser incorrecta, ha expirado ó esta bloqueada."));
                           RequestContext.getCurrentInstance().update("form:informacionAcceso");
                        }
                     } else {
                        inicioSession = true;
                        sessionEntradaDefault();
                        contextoF.addMessage(null, new FacesMessage("", "No se pudo crear el EntityManager, comuniquese con soporte.."));
                        RequestContext.getCurrentInstance().update("form:informacionAcceso");
                     }
                  } else {
                     inicioSession = true;
                     sessionEntradaDefault();
                     contextoF.addMessage(null, new FacesMessage("", "El usuario no existe o esta inactivo"));
                     RequestContext.getCurrentInstance().update("form:informacionAcceso");
                  }
               } else {
                  inicioSession = true;
                  sessionEntradaDefault();
                  contextoF.addMessage(null, new FacesMessage("", "Base de datos incorrecta."));
                  RequestContext.getCurrentInstance().update("form:informacionAcceso");
               }
            } else {
               contextoF.addMessage(null, new FacesMessage("", "Existen campos vacios."));
               RequestContext.getCurrentInstance().update("form:informacionAcceso");
            }
         } else {
            ControlTemplate controlTemplate = (ControlTemplate) contextoF.getApplication().evaluateExpressionGet(contextoF, "#{controlTemplate}", ControlTemplate.class);
            controlTemplate.cerrarSession();
            cambioClave = true;
            modulosDesigner = true;
            txtBoton = "Conectar";
            asignarImagenCandado(false);
            log.info("estadoinicio ingresar else: " + estadoInicio);
            estadoInicio = false;
            getNombreEmpresa();
            llenarBannerSinEntrar();
            RequestContext.getCurrentInstance().update(actualizaciones);
            RequestContext.getCurrentInstance().update("form:growl");
            inicioSession = true;
            acceso = false;
            sessionEntradaDefault();
         }
         RequestContext.getCurrentInstance().update("form:btnCandadoLogin");
         log.info("estadoinicio ingresar fin: " + estadoInicio);
      } catch (UnknownHostException e) {
         log.info("estadoinicio ingresar exception: " + estadoInicio);
         log.info(e);
      }
   }

   private void imprimirRequest(HttpServletRequest request) {
      log.info(this.getClass().getName() + "imprimirRequest()");
      try {
         log.info("auth: " + request.getAuthType());
         log.info("ContextPath: " + request.getContextPath());
         log.info("LocalAddr: " + request.getLocalAddr());
         log.info("LocalName: " + request.getLocalName());
         log.info("LocalPort: " + request.getLocalPort());
         log.info("PathInfo: " + request.getPathInfo());
         log.info("PathTranslated: " + request.getPathTranslated());
         log.info("Protocol: " + request.getProtocol());
         log.info("QueryString: " + request.getQueryString());
         log.info("RemoteAddr: " + request.getRemoteAddr());
         log.info("RemoteHost: " + request.getRemoteHost());
         log.info("RemotePort: " + request.getRemotePort());
         log.info("RemoteUser: " + request.getRemoteUser());
         log.info("RequestURI: " + request.getRequestURI());
         log.info("RequestURL: " + request.getRequestURL());
         log.info("RequestedSessionId: " + request.getRequestedSessionId());
         log.info("ServerName: " + request.getServerName());
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public void validarDialogoSesion() {
//        RequestContext context = RequestContext.getCurrentInstance();
      if (!estadoInicio) {
         msgSesion = "Iniciando sesión, por favor espere...";
         RequestContext.getCurrentInstance().update("formularioDialogos:estadoSesion");
         RequestContext.getCurrentInstance().execute("PF('estadoSesion').show()");
      } else {
         msgSesion = "Cerrando sesión, por favor espere...";
//         RequestContext.getCurrentInstance().update("formularioDialogos:estadoSesion");
         RequestContext.getCurrentInstance().update("formularioDialogos:estadoSesion");
//         RequestContext.getCurrentInstance().execute("PF('estadoSesion').show()");
         RequestContext.getCurrentInstance().execute("PF('estadoSesion').show()");
      }
      log.info("ControlInicioRed.validaDialogoSesion");
      RequestContext.getCurrentInstance().update("form:btnCandadoLogin");
   }

   private void llenarBannerSinEntrar() {
      String ubicaPublicidad = "Iconos/";
      banner = new ArrayList<>();
      banner.clear();
      banner.add(new BannerInicioRed(ubicaPublicidad + "publicidad01.png", "http://www.nomina.com.co/"));
      banner.add(new BannerInicioRed(ubicaPublicidad + "publicidad02.png", "http://www.nomina.com.co/"));
      banner.add(new BannerInicioRed(ubicaPublicidad + "publicidad03.png", "http://www.nomina.com.co/"));
      banner.add(new BannerInicioRed(ubicaPublicidad + "publicidad04.png", "http://www.nomina.com.co/"));
   }

   private void llenarBannerListaVacia() {
      String ubicaPublicidad = "Iconos/";
      banner.clear();
      banner.add(new BannerInicioRed(ubicaPublicidad + "SinImagen.png", ""));
   }

   private void obtenerRecordatorios() {
      listaRecordatorios = administrarInicioRed.recordatoriosInicio();
      if (listaRecordatorios != null) {
         for (int i = 0; i < listaRecordatorios.size(); i++) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", listaRecordatorios.get(i)));
         }
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   private void obtenerConsultas() {
      String ubicaPublicidad = "Iconos/";
      String paginaGeneraListado = "generaconsulta.xhtml";
      String parametro = "?secuencia=";
      listaConsultas = administrarInicioRed.consultasInicio();
      if (listaConsultas != null && !listaConsultas.isEmpty()) {
         banner.clear();
         for (Recordatorios recor : listaConsultas) {
            if (recor.getNombreimagen() != null) {
               banner.add(new BannerInicioRed(ubicaPublicidad + recor.getNombreimagen(),
                       paginaGeneraListado + parametro + recor.getSecuencia().toString()));
            }
         }
         if (banner.isEmpty()) {
            llenarBannerListaVacia();
         }
      } else {
         llenarBannerListaVacia();
      }
   }

   public void sessionEntradaDefault() {
      if (inicioSession) {
         acceso = administrarInicioRed.conexionDefault();
         inicioSession = false;
      }
   }

   public void nuevoRecordatorio() {
      getRecordatorio();
      RequestContext.getCurrentInstance().update("form:recordatorio");
   }

   public void inicioComponentes() throws IOException {
      sessionEntradaDefault();
      getNombreEmpresa();
      getRecordatorio();
      RequestContext.getCurrentInstance().update("form:nombreEmpresa");
      RequestContext.getCurrentInstance().update("form:recordatorio");
   }

   public void cambiarClave() {
      log.info("Nclave: " + NClave + " Rclave: " + Rclave);
      if (!NClave.equals("") && !Rclave.equals("")) {
         if (NClave.equals(Rclave)) {
            int transaccion = administrarInicioRed.cambioClave(usuario, NClave);
            if (transaccion == 0) {
               NClave = null;
               Rclave = null;
               RequestContext.getCurrentInstance().execute("PF('cambiarClave').hide()");
               RequestContext.getCurrentInstance().execute("PF('exitoCambioClave').show()");
            } else if (transaccion == 28007) {
               RequestContext.getCurrentInstance().execute("PF('errorCambioClaveReusar').show()");
            } else if (transaccion == -1) {
               RequestContext.getCurrentInstance().execute("PF('cambiarClave').hide()");
               log.info("El entity manager Factory no se ha creado, revisar.");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorCambioClave').show();");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorCambioClaveCamposVacios').show();");
      }
   }

   public void cancelarCambioClave() {
      NClave = null;
      Rclave = null;
   }

   private void asignarImagenCandado(boolean inicioSesion) {
      log.info("ControlInicioRed.asignarImagenCandado");
      log.info("parametro: " + inicioSesion);
      this.candadoLogin = (inicioSesion) ? "loginCandadoAbierto.png" : "loginCandadoCerrado.png";
   }

   //GETTER AND SETTER
   public String getUsuario() {
      if (usuario == null) {
         usuario = "";
      }
      return usuario;
   }

   public void setUsuario(String usuario) {
      this.usuario = usuario.toUpperCase();
   }

   public String getContraseña() {
      if (contraseña == null) {
         contraseña = "";
      }
      return contraseña;
   }

   public void setContraseña(String contraseña) {
      this.contraseña = contraseña;
   }

   public String getBaseDatos() {
      if (baseDatos == null) {
         baseDatos = "";
      }
      return baseDatos;
   }

   public void setBaseDatos(String baseDatos) {
      this.baseDatos = baseDatos.toUpperCase();
   }

   public boolean isCambioClave() {
      return cambioClave;
   }

   public String getTxtBoton() {
      return txtBoton;
   }

   public boolean isModulosDesigner() {
      return modulosDesigner;
   }

   public List<BannerInicioRed> getBanner() {
      return banner;
   }

   public String getMostrarFecha() {
      fechaActual = new Date();
      mostrarFecha = formatoFechaActual.format(fechaActual).toUpperCase();
      añoActual = formatoAño.format(fechaActual);
      return mostrarFecha;
   }

   public String getAñoActual() {
      return añoActual;
   }

   public Recordatorios getRecordatorio() {
      if (acceso) {
         recordatorio = administrarInicioRed.recordatorioAleatorio();
      }
      return recordatorio;
   }

   public String getNombreEmpresa() {
      if (acceso) {
         nombreEmpresa = administrarInicioRed.nombreEmpresaPrincipal();
      }
      return nombreEmpresa;
   }

   public boolean isEstadoInicio() {
      return estadoInicio;
   }

   public List<Recordatorios> getListaConsultas() {
      return listaConsultas;
   }

   public void setListaConsultas(List<Recordatorios> listaConsultas) {
      this.listaConsultas = listaConsultas;
   }

   public String getNClave() {
      return NClave;
   }

   public void setNClave(String NClave) {
      this.NClave = NClave.toUpperCase();
   }

   public String getRclave() {
      return Rclave;
   }

   public void setRclave(String Rclave) {
      this.Rclave = Rclave.toUpperCase();
   }

   public String getMsgSesion() {
      return msgSesion;
   }

   public String getCandadoLogin() {
      log.info("ControlInicioRed.getCandadoLogin");
      log.info("inicio sesion: " + !modulosDesigner);
      asignarImagenCandado(!modulosDesigner);
      return candadoLogin;
   }

}
