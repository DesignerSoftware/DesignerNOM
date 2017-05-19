package Controlador;

import Entidades.ActualUsuario;
import Entidades.DetallesEmpresas;
import Entidades.ParametrosEstructuras;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTemplateInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
//import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.math.BigDecimal;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ControlTemplate implements Serializable {

   @EJB
   AdministrarTemplateInterface administrarTemplate;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private ActualUsuario actualUsuario;
   private String nombreUsuario;
   private StreamedContent logoEmpresa;
   private StreamedContent fotoUsuario;
   private String rutaFotoLogo, rutaFotoUs;
   private FileInputStream fis;
   private final String webSite;
   private final String linkSoporte;
   private DetallesEmpresas detalleEmpresa;
   private String fechaDesde, fechaHasta, fechaCorte;
   private ParametrosEstructuras parametrosEstructuras;
   private SimpleDateFormat formato;
   private String nombrePerfil;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private BigDecimal smlv, auxtrans, uvt, minibc, segsocial;

   public ControlTemplate() {
      rutaFotoLogo = null;
      rutaFotoUs = null;
      webSite = "www.nomina.com.co";
      linkSoporte = "Teamviewer";
      formato = new SimpleDateFormat("dd/MM/yyyy");
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
      /*if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
         
        } else {
       */
      String pagActual = "template";

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
      System.out.println("Inicializando Template.");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTemplate.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         actualUsuario = administrarTemplate.consultarActualUsuario();
         detalleEmpresa = administrarTemplate.consultarDetalleEmpresaUsuario();
         nombrePerfil = administrarTemplate.consultarNombrePerfil();
      } catch (Exception e) {
         System.out.println("Error postconstruct ControlTemplate: " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void informacionUsuario() {
      if (actualUsuario != null) {
         int n = 0;
         if (rutaFotoUs == null) {
            rutaFotoUs = administrarTemplate.rutaFotoUsuario();
            n = 1;
         }
         if (rutaFotoUs != null) {
            String bckRuta = rutaFotoUs;
            try {
               rutaFotoUs = rutaFotoUs + actualUsuario.getAlias() + ".png";
               fis = new FileInputStream(new File(rutaFotoUs));
               fotoUsuario = new DefaultStreamedContent(fis, "image/jpg");
            } catch (FileNotFoundException e) {
               try {
                  if (n == 1) {
                     rutaFotoUs = bckRuta + "sinFoto.jpg";
                  } else {
                     rutaFotoUs = bckRuta;
                  }
                  fis = new FileInputStream(new File(rutaFotoUs));
                  fotoUsuario = new DefaultStreamedContent(fis, "image/jpg");
               } catch (FileNotFoundException ex) {
                  System.out.println("No se encontro el siguiente archivo, verificar. \n" + rutaFotoUs);
               }
            }
         }
      }
   }

   public void cerrarSession() throws IOException {
      FacesContext x = FacesContext.getCurrentInstance();
      HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
      administrarTemplate.cerrarSession(ses.getId());
      ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ses.setAttribute(ses.getId(), null); 
      ec.invalidateSession();
      ec.redirect(ec.getRequestContextPath() + "/iniciored.xhtml");
   }

   public void validarSession() throws IOException {
      FacesContext x = FacesContext.getCurrentInstance();
      HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
      boolean resultado = administrarTemplate.obtenerConexion(ses.getId());
      if (resultado == false) {
         ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
         //ec.invalidateSession();
         ec.redirect(ec.getRequestContextPath() + "/iniciored.xhtml");
      }
   }

   public String getNombreUsuario() {
      if (nombreUsuario == null && actualUsuario != null) {
         nombreUsuario = actualUsuario.getPersona().getNombreCompletoOrden2();
      }
      return nombreUsuario;
   }

   public StreamedContent getFotoUsuario() {
      informacionUsuario();
      return fotoUsuario;
   }

   public StreamedContent getLogoEmpresa() {
      if (rutaFotoLogo == null) {
         rutaFotoLogo = administrarTemplate.logoEmpresa();
      }
      if (rutaFotoLogo != null) {
         try {
            fis = new FileInputStream(new File(rutaFotoLogo));
            logoEmpresa = new DefaultStreamedContent(fis, "image/png");
         } catch (FileNotFoundException fnfe) {
            try {
               logoEmpresa = null;
               fis = null;
               rutaFotoLogo = administrarTemplate.rutaFotoUsuario() + "sinLogo.png";
//               System.out.println("ruta sin logo: " + rutaFotoLogo);
               fis = new FileInputStream(new File(rutaFotoLogo));
               logoEmpresa = new DefaultStreamedContent(fis, "image/png");
            } catch (FileNotFoundException fnfei) {
               System.out.println("Logo de empresa por defecto no encontrado. \n" + fnfei);
               logoEmpresa = null;
            }
         }
      }
      return logoEmpresa;
   }

   public String getWebSite() {
      return webSite;
   }

   public String getLinkSoporte() {
      return linkSoporte;
   }

   public DetallesEmpresas getDetalleEmpresa() {
      if (detalleEmpresa == null) {
         detalleEmpresa = administrarTemplate.consultarDetalleEmpresaUsuario();
      }
      return detalleEmpresa;
   }

   public String getFechaDesde() {
      parametrosEstructuras = administrarTemplate.consultarParametrosUsuario();
      if (parametrosEstructuras != null) {
         fechaDesde = formato.format(parametrosEstructuras.getFechadesdecausado());
      } else {
         fechaDesde = " ";
      }
      return fechaDesde;
   }

   public void setFechaDesde(String fechaDesde) {
      this.fechaDesde = fechaDesde;
   }

   public String getFechaHasta() {
      if (parametrosEstructuras != null) {
         fechaHasta = formato.format(parametrosEstructuras.getFechahastacausado());
      } else {
         fechaHasta = " ";
      }
      return fechaHasta;
   }

   public void setFechaHasta(String fechaHasta) {
      this.fechaHasta = fechaHasta;
   }

   public String getFechaCorte() {
      if (parametrosEstructuras != null) {
         fechaCorte = formato.format(parametrosEstructuras.getFechasistema());
      } else {
         fechaCorte = "";
      }

      return fechaCorte;
   }

   public void setFechaCorte(String fechaCorte) {
      this.fechaCorte = fechaCorte;
   }

   public String getNombrePerfil() {
      if (nombrePerfil == null) {
         nombrePerfil = administrarTemplate.consultarNombrePerfil();
      }
      return nombrePerfil;
   }

   public BigDecimal getSmlv() {
      smlv = administrarTemplate.consultarSMLV();
      return smlv;
   }

   public void setSmlv(BigDecimal smlv) {
      this.smlv = smlv;
   }

   public BigDecimal getAuxtrans() {
      auxtrans = administrarTemplate.consultarAuxTrans();
      return auxtrans;
   }

   public void setAuxtrans(BigDecimal auxtrans) {
      this.auxtrans = auxtrans;
   }

   public BigDecimal getUvt() {
      uvt = administrarTemplate.consultarUVT();
      return uvt;
   }

   public void setUvt(BigDecimal uvt) {
      this.uvt = uvt;
   }

   public BigDecimal getMinibc() {
      minibc = administrarTemplate.consultarMinIBC();
      return minibc;
   }

   public void setMinibc(BigDecimal minibc) {
      this.minibc = minibc;
   }

   public BigDecimal getSegsocial() {
      segsocial = administrarTemplate.consultarTopeSegSocial();
      return segsocial;
   }

   public void setSegsocial(BigDecimal segsocial) {
      this.segsocial = segsocial;
   }

}
