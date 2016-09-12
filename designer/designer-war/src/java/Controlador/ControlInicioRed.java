package Controlador;

import Banner.BannerInicioRed;
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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import utilidadesUI.PrimefacesContextUI;
import utilidadesUI.PrimefacesContextUI;
//import org.primefaces.model.DefaultStreamedContent;
//import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ControlInicioRed implements Serializable {

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

   public ControlInicioRed() {
      cambioClave = true;
      System.out.println("estadoinicio constructor: " + estadoInicio);
      estadoInicio = false;
      modulosDesigner = true;
      txtBoton = "Conectar";
      asignarImagenCandado(false);
      listaConsultas = new ArrayList<Recordatorios>();
      actualizaciones = new ArrayList<String>();
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

      banner = new ArrayList<BannerInicioRed>();
      this.llenarBannerSinEntrar();
      //FECHA ACTUAL
      formatoFechaActual = new SimpleDateFormat("EEEEE dd 'de' MMMMM 'de' yyyy");
      formatoAño = new SimpleDateFormat("yyyy");
      //INICIO SESSION DEFAULT
      inicioSession = true;
      acceso = false;
      msgSesion = "Iniciando sesión, por favor espere...";
   }

   public void ingresar() {
      try {
//         RequestContext context = RequestContext.getCurrentInstance();
         FacesContext contexto = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) contexto.getExternalContext().getSession(false);
         System.out.println("Ses= " + ses);
         System.out.println("estado sesion ingreso = " + estadoInicio);
         if (estadoInicio == false) {
            if (!baseDatos.equals("") && !usuario.equals("") && !contraseña.equals("")) {
               if (administrarInicioRed.conexionInicial(baseDatos)) {
                  if (administrarInicioRed.validarUsuario(usuario)) {
                     if (administrarInicioRed.conexionUsuario(baseDatos, usuario, contraseña)) {
                        if (administrarInicioRed.validarConexionUsuario(ses.getId())) {
                           cambioClave = false;
                           estadoInicio = true;
                           modulosDesigner = false;
                           txtBoton = "Desconectar";
                           asignarImagenCandado(true);
                           acceso = true;
                           getNombreEmpresa();
                           obtenerRecordatorios();
                           /*listaRecordatorios = administrarInicioRed.recordatoriosInicio();
                                     if (listaRecordatorios != null) {
                                     for (int i = 0; i < listaRecordatorios.size(); i++) {
                                     contexto.addMessage(null, new FacesMessage("", listaRecordatorios.get(i)));
                                     }
                                     PrimefacesContextUI.actualizar("form:growl");
                                     }*/
                           obtenerConsultas();
                           /*listaConsultas = administrarInicioRed.consultasInicio();
                                     if (listaConsultas != null && !listaConsultas.isEmpty()) {
                                     banner.clear();
                                     if (listaConsultas.size() > 0) {
                                     for (int j = 0; j < listaConsultas.size(); j++) {
                                     if (listaConsultas.get(j).getNombreimagen() != null) {
                                     banner.add(new BannerInicioRed("Iconos/" + listaConsultas.get(j).getNombreimagen(), ""));
                                     }
                                     }
                                     } else {
                                     llenarBannerListaVacia();
                                     }
                                     } else {
                                     llenarBannerSinEntrar();
                                     }*/
                           HttpServletRequest request = (HttpServletRequest) (contexto.getExternalContext().getRequest());
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
//                           PrimefacesContextUI.actualizar(actualizaciones);
                           PrimefacesContextUI.actualizarLista(actualizaciones);
                        } else {
                           inicioSession = true;
                           sessionEntradaDefault();
                           contexto.addMessage(null, new FacesMessage("", "La contraseña puede ser incorrecta, ha expirado ó esta bloqueada."));
//                           PrimefacesContextUI.actualizar("form:informacionAcceso");
                           PrimefacesContextUI.actualizar("form:informacionAcceso");
                        }
                     } else {
                        inicioSession = true;
                        sessionEntradaDefault();
                        contexto.addMessage(null, new FacesMessage("", "No se pudo crear el EntityManager, comuniquese con soporte.."));
                        PrimefacesContextUI.actualizar("form:informacionAcceso");
                     }
                  } else {
                     inicioSession = true;
                     sessionEntradaDefault();
                     contexto.addMessage(null, new FacesMessage("", "El usuario no existe o esta inactivo"));
                     PrimefacesContextUI.actualizar("form:informacionAcceso");
                  }
               } else {
                  inicioSession = true;
                  sessionEntradaDefault();
                  contexto.addMessage(null, new FacesMessage("", "Base de datos incorrecta."));
                  PrimefacesContextUI.actualizar("form:informacionAcceso");
               }
            } else {
               contexto.addMessage(null, new FacesMessage("", "Existen campos vacios."));
               PrimefacesContextUI.actualizar("form:informacionAcceso");
            }
         } else {
            administrarInicioRed.cerrarSession(ses.getId());
            cambioClave = true;
            modulosDesigner = true;
            txtBoton = "Conectar";
            asignarImagenCandado(false);
            System.out.println("estadoinicio ingresar else: " + estadoInicio);
            estadoInicio = false;
            getNombreEmpresa();
            llenarBannerSinEntrar();
            PrimefacesContextUI.actualizarLista(actualizaciones);
            PrimefacesContextUI.actualizar("form:growl");
            inicioSession = true;
            acceso = false;
            sessionEntradaDefault();
         }
         PrimefacesContextUI.actualizar("form:btnCandadoLogin");
         System.out.println("estadoinicio ingresar fin: " + estadoInicio);
      } catch (UnknownHostException e) {
         System.out.println("estadoinicio ingresar exception: " + estadoInicio);
         System.out.println(e);
      }
   }

   public void validarDialogoSesion() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!estadoInicio) {
         msgSesion = "Iniciando sesión, por favor espere...";
         PrimefacesContextUI.actualizar("formularioDialogos:estadoSesion");
         PrimefacesContextUI.ejecutar("PF('PF('estadoSesion')').show()");
      } else {
         msgSesion = "Cerrando sesión, por favor espere...";
//         PrimefacesContextUI.actualizar("formularioDialogos:estadoSesion");
         PrimefacesContextUI.actualizar("formularioDialogos:estadoSesion");
//         PrimefacesContextUI.ejecutar("PF('PF('estadoSesion')').show()");
         PrimefacesContextUI.ejecutar("PF('estadoSesion')').show()");
      }
      System.out.println("ControlInicioRed.validaDialogoSesion");
      PrimefacesContextUI.actualizar("form:btnCandadoLogin");
   }

   private void llenarBannerSinEntrar() {
      String ubicaPublicidad = "Iconos/";
      banner.clear();
      banner.add(new BannerInicioRed(ubicaPublicidad + "publicidad01.png", "http://www.nomina.com.co/"));
      banner.add(new BannerInicioRed(ubicaPublicidad + "publicidad02.png", "http://www.nomina.com.co/"));
      banner.add(new BannerInicioRed(ubicaPublicidad + "publicidad03.png", "http://www.nomina.com.co/"));
      banner.add(new BannerInicioRed(ubicaPublicidad + "publicidad04.png", "http://www.nomina.com.co/"));
      /*banner.add(new BannerInicioRed(ubicaPublicidad+"publicidad05.png", "https://glassfish.java.net/"));
         banner.add(new BannerInicioRed(ubicaPublicidad+"publicidad06.png", "https://www.java.com/"));
         banner.add(new BannerInicioRed(ubicaPublicidad+"publicidad07.png", "https://javaserverfaces.java.net/"));
       */
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
//         request.update("form:growl");
         PrimefacesContextUI.actualizar("form:growl");
      }
   }

   private void obtenerConsultas() {
      String ubicaPublicidad = "Iconos/";
      String paginaGeneraListado = "generaconsulta.xhtml";
      String parametro = "?secuencia=";
      listaConsultas = administrarInicioRed.consultasInicio();
      if (listaConsultas != null && !listaConsultas.isEmpty()) {
         banner.clear();
         /*for (int j = 0; j < listaConsultas.size(); j++) {
            
                if (listaConsultas.get(j).getNombreimagen() != null) {
                    banner.add(new BannerInicioRed("Iconos/" + listaConsultas.get(j).getNombreimagen(), ""));
                }
            }*/
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
      RequestContext context = RequestContext.getCurrentInstance();
      PrimefacesContextUI.actualizar("form:recordatorio");
   }

   public void inicioComponentes() throws IOException {
      sessionEntradaDefault();
      getNombreEmpresa();
      getRecordatorio();
      RequestContext context = RequestContext.getCurrentInstance();
      PrimefacesContextUI.actualizar("form:nombreEmpresa");
      PrimefacesContextUI.actualizar("form:recordatorio");
   }

   public void cambiarClave() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("Nclave: " + NClave + " Rclave: " + Rclave);
      if (!NClave.equals("") && !Rclave.equals("")) {
         if (NClave.equals(Rclave)) {
            int transaccion = administrarInicioRed.cambioClave(usuario, NClave);
            if (transaccion == 0) {
               NClave = null;
               Rclave = null;
               PrimefacesContextUI.ejecutar("PF('cambiarClave.hide();");
               PrimefacesContextUI.ejecutar("PF('exitoCambioClave.show();");
            } else if (transaccion == 28007) {
               PrimefacesContextUI.ejecutar("PF('errorCambioClaveReusar.show();");
            } else if (transaccion == -1) {
               PrimefacesContextUI.ejecutar("PF('cambiarClave.hide();");
               System.out.println("El entity manager Factory no se ha creado, revisar.");
            }
         } else {
            PrimefacesContextUI.ejecutar("PF('errorCambioClave.show();");
         }
      } else {
         PrimefacesContextUI.ejecutar("PF('errorCambioClaveCamposVacios.show();");
      }
   }

   public void cancelarCambioClave() {
      NClave = null;
      Rclave = null;
   }

   private void asignarImagenCandado(boolean inicioSesion) {
      System.out.println("ControlInicioRed.asignarImagenCandado");
      System.out.println("parametro: " + inicioSesion);
//        if (inicioSesion) {
//            this.candadoLogin = "loginCandadoAbierto.png";
//        } else {
//            this.candadoLogin = "loginCandadoCerrado.png";
//        }
      this.candadoLogin = (inicioSesion) ? "loginCandadoAbierto.png" : "loginCandadoCerrado.p ng";
   }

   //GETTER AND SETTER
   public String getUsuario() {
      return usuario;
   }

   public void setUsuario(String usuario) {
      this.usuario = usuario.toUpperCase();
   }

   public String getContraseña() {
      return contraseña;
   }

   public void setContraseña(String contraseña) {
      this.contraseña = contraseña;
   }

   public String getBaseDatos() {
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
      System.out.println("ControlInicioRed.getCandadoLogin");
      System.out.println("inicio sesion: " + !modulosDesigner);
      asignarImagenCandado(!modulosDesigner);
      return candadoLogin;
   }

}
