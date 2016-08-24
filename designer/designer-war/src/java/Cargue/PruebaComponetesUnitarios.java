package Cargue;

import Entidades.Estructuras;
import InterfaceAdministrar.AdministrarEstructurasInterface;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class PruebaComponetesUnitarios implements Serializable {

    private String inputFoco;
    private List<String> listaPrueb;
    private int num;

    public PruebaComponetesUnitarios() {
        listaPrueb = null;
        inputFoco = "form:tablaEjemplo:0:input3";
        num = 0;

    }

    public List<String> getListaPrueb() {
        if (listaPrueb == null) {
            listaPrueb = new ArrayList<String>();
            listaPrueb.add("HOLA1");
            listaPrueb.add("HOLA2");
            listaPrueb.add("HOLA3");
            listaPrueb.add("HOLA4");
        }
        return listaPrueb;
    }

    public void probarFocusDinamico() {
        if (num < 4) {
            num++;
            inputFoco = "form:tablaEjemplo:" + num + ":input3";
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:superBoton");
        }
    }

    public void setListaPrueb(List<String> listaPrueb) {
        this.listaPrueb = listaPrueb;
    }

    public String getInputFoco() {
        return inputFoco;
    }

    public void setInputFoco(String inputFoco) {
        this.inputFoco = inputFoco;
    }

    public void cambiarFoco() {
    }
}
