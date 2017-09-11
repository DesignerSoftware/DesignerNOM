/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import InterfaceAdministrar.AdministrarSalidasUTLInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author user
 */
@Named(value = "controlSalidasUTL")
@SessionScoped
public class ControlSalidasUTL implements Serializable {

    private static Logger log = Logger.getLogger(ControlSalidasUTL.class);

    @EJB
    AdministrarSalidasUTLInterface administrarSalidasUTL;

    private List<File> listArchivosError;
    private List<File> listArchivosErrorFiltrar;
    private File archivoErrorSeleccionado;
    private String altoTabla, paginaAnterior;
    private String pathError, rutaArchivo, infoRegistro;
    private String msgError;

    public ControlSalidasUTL() {
        listArchivosError = null;
        altoTabla = "340";
        archivoErrorSeleccionado = null;

    }

    public void limpiarListasValor() {

    }

    @PreDestroy
    public void destruyendoce() {
        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSalidasUTL.obtenerConexion(ses.getId());
            recibirPaginaEntrante("nominaf");
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listArchivosError = null;
        getListArchivosError();
        if (listArchivosError != null) {
            archivoErrorSeleccionado = listArchivosError.get(0);
        }
        pathError = administrarSalidasUTL.pathError();
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "salidasutl";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
        }
        limpiarListasValor();
    }

    public void salir() {
        navegar("atras");
    }

    public StreamedContent descargarArchivo(String nombre) {
        StreamedContent archivoDescarga = null;
        if (pathError != null && !pathError.isEmpty()) {
            rutaArchivo = pathError + nombre;
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.setResponseHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            ec.setResponseHeader("Pragma", "no-cache");
            ec.setResponseHeader("Expires", "0");
            ec.setResponseHeader("Expires", "Mon, 8 Aug 1980 10:00:00 GMT");
            try {
                File archivo = new File(rutaArchivo);
                FileInputStream fis = new FileInputStream(archivo);
                archivoDescarga = new DefaultStreamedContent(fis, "text/xml", nombre);
            } catch (Exception e) {
                archivoDescarga = null;
                msgError = e.getMessage();
                RequestContext.getCurrentInstance().execute("PF('errorDescargarArchivo').show()");
            }
        }
        return archivoDescarga;
    }

    //////////gets y sets///////
    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public List<File> getListArchivosError() {
        if (listArchivosError == null) {
            listArchivosError = administrarSalidasUTL.consultarArchivosError();
        }
        return listArchivosError;
    }

    public void setListArchivosError(List<File> listArchivosError) {
        this.listArchivosError = listArchivosError;
    }

    public List<File> getListArchivosErrorFiltrar() {
        return listArchivosErrorFiltrar;
    }

    public void setListArchivosErrorFiltrar(List<File> listArchivosErrorFiltrar) {
        this.listArchivosErrorFiltrar = listArchivosErrorFiltrar;
    }

    public File getArchivoErrorSeleccionado() {
        return archivoErrorSeleccionado;
    }

    public void setArchivoErrorSeleccionado(File archivoErrorSeleccionado) {
        this.archivoErrorSeleccionado = archivoErrorSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:ArchivosError");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getPathError() {
        return pathError;
    }

    public void setPathError(String pathError) {
        this.pathError = pathError;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }
}
