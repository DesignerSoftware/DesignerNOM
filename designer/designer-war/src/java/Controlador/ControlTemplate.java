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

    public ControlTemplate() {
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
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
        } else {
            String pagActual = "template";
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParametros.put("paginaAnterior", pagActual);
            //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
            controlListaNavegacion.adicionarPagina(pagActual);
        }
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
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
            String rutaFoto = administrarTemplate.rutaFotoUsuario();
            if (rutaFoto != null) {
                String bckRuta = rutaFoto;
                try {
                    rutaFoto = rutaFoto + actualUsuario.getAlias() + ".png";
                    fis = new FileInputStream(new File(rutaFoto));
                    fotoUsuario = new DefaultStreamedContent(fis, "image/jpg");
                } catch (FileNotFoundException e) {
                    try {
                        System.out.println("El usuario no tiene una foto asignada: " + rutaFoto);
                        rutaFoto = bckRuta + "sinFoto.jpg";
                        fis = new FileInputStream(new File(rutaFoto));
                        fotoUsuario = new DefaultStreamedContent(fis, "image/jpg");
                    } catch (FileNotFoundException ex) {
                        System.out.println("No se encontro el siguiente archivo, verificar. \n" + rutaFoto);
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
        obtenerEmpresaLogo();
        return logoEmpresa;
    }

    public void obtenerEmpresaLogo() {
        String rutaFoto = administrarTemplate.logoEmpresa();
        if (rutaFoto != null) {
            try {
                fis = new FileInputStream(new File(rutaFoto));
                logoEmpresa = new DefaultStreamedContent(fis, "image/png");
            } catch (FileNotFoundException fnfe) {
                try {
                    System.out.println("Logo de la empresa no encontrado para el template. \n" + fnfe);
                    logoEmpresa = null;
                    fis = null;
                    rutaFoto = administrarTemplate.rutaFotoUsuario() + "sinLogo.png";
                    //rutaFoto = "Imagenes/Iconos/" + "sinLogo.png";
                    System.out.println("ruta sin logo: " + rutaFoto);
                    fis = new FileInputStream(new File(rutaFoto));
                    logoEmpresa = new DefaultStreamedContent(fis, "image/png");
                } catch (FileNotFoundException fnfei) {
                    System.out.println("Logo de empresa por defecto no encontrado. \n" + fnfei);
                    logoEmpresa = null;
                }

            }
        }
    }

    public String getWebSite() {
        return webSite;
    }

    public String getLinkSoporte() {
        return linkSoporte;
    }

    public DetallesEmpresas getDetalleEmpresa() {
        //System.out.println("ControlTemplate.getDetalleEmpresa");
        detalleEmpresa = administrarTemplate.consultarDetalleEmpresaUsuario();
        return detalleEmpresa;
    }

    public String getFechaDesde() {
        parametrosEstructuras = administrarTemplate.consultarParametrosUsuario();
        fechaDesde = formato.format(parametrosEstructuras.getFechadesdecausado());
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        fechaHasta = formato.format(parametrosEstructuras.getFechahastacausado());
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getFechaCorte() {
        fechaCorte = formato.format(parametrosEstructuras.getFechasistema());
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

}
