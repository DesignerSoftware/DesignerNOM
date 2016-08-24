package Controlador;

import Exportar.ExportarPDF;
import InterfaceAdministrar.AdministrarGeneraConsultaInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.*;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;

/**
 *
 * @author Edwin Hastamorir
 */
@ManagedBean
@SessionScoped
public class ControlGeneraConsulta implements Serializable {

    @EJB
    AdministrarGeneraConsultaInterface administrarGeneraConsulta;

    private String secuencia;
    private List<String> listaConsultas;

    public ControlGeneraConsulta() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        System.out.println("ControlGeneraConsulta.inicializarAdministrador");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarGeneraConsulta.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e + " " + "Causa: " + e.getCause());
        }
    }

    public void obtieneConsulta() {
        System.out.println("ControlGeneraConsulta.obtieneConsulta");
        System.out.println("SECUENCIA: " + this.secuencia);
        listaConsultas = administrarGeneraConsulta.ejecutarConsulta(new BigInteger(this.secuencia));
        /*try {
            exportPDF();
        } catch (IOException ex) {
            System.out.println("obtieneConsulta en "+this.getClass().getName());
            ex.printStackTrace();
        }*/
    }

    public void exportPDF() throws IOException {
        DataTable tablaEx = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("principal:datos");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tablaEx, "consultasRecordatoriosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }
    
    @PreDestroy
    public void salir(){
        administrarGeneraConsulta.salir();
    }

    public String getSecuencia() {
        System.out.println("ControlGeneraConsulta.getSecuencia");
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        System.out.println("ControlGeneraConsulta.setSecuencia");
        this.secuencia = secuencia;
    }

    public List<String> getListaConsultas() {
        return listaConsultas;
    }

    public void setListaConsultas(List<String> listaConsultas) {
        this.listaConsultas = listaConsultas;
    }

}
