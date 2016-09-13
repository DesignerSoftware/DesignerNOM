package Controlador;

import utilidadesUI.PrimefacesContextUI;
import ClasesAyuda.ColumnasBusquedaAvanzada;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.scrollpanel.ScrollPanel;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class ControlPruebasUnitarias implements Serializable {

   

    private Date fechaPrueba;

    public ControlPruebasUnitarias() {
        
    }
    
    public void modificarParametroInforme(){
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:fechaDesdeParametroL");
        RequestContext.getCurrentInstance().update("form:prueba");
    }

    public Date getFechaPrueba() {
        return fechaPrueba;
    }

    public void setFechaPrueba(Date fechaPrueba) {
        this.fechaPrueba = fechaPrueba;
    }

}
