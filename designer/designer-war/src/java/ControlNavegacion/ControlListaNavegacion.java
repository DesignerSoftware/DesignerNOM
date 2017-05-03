/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControlNavegacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author user
 */
//@Named(value = "controlListaNavegacion")
//@Dependent
@ManagedBean
@SessionScoped
public class ControlListaNavegacion implements Serializable {

    private List<String> listaPaginasAnteriores = new ArrayList<String>();
    private String paginaActual = "1";

    /**
     * Creates a new instance of ControlListaNavegacion
     */
    public ControlListaNavegacion() {
        listaPaginasAnteriores.add("iniciored");
    }

    public String guardarNavegacion(String pagActual, String pagDestino) {
        try {
            if (pagActual.equals("nominaf")) {
                listaPaginasAnteriores.clear();
                listaPaginasAnteriores.add("iniciored");
            } else if (!pagActual.equals(paginaActual) && !paginaActual.equals("1")) {
                adicionarPagina("nominaf");
            }
            paginaActual = pagDestino;
            adicionarPagina(pagActual);
        } catch (Exception e) {
            System.out.println("ControlListaNavegacion.guardarNavegacion() ERROR : " + e);
            pagDestino = "nominaf";
        }
        return pagDestino;
    }

    public void adicionarPagina(String pag) {
        listaPaginasAnteriores.add(pag);
        System.out.println("ListaPaginasAnteriores : " + listaPaginasAnteriores + "::[" + paginaActual + "]");
    }

    public void quitarPagina(String pagParametro) {
        FacesContext fc = FacesContext.getCurrentInstance();
        System.out.println("quitarPagina(pag) : " + pagParametro + ", paginaActual : " + paginaActual);
        if (paginaActual.equals(pagParametro)) {
            if (listaPaginasAnteriores.size() > 1) {
                paginaActual = listaPaginasAnteriores.get((listaPaginasAnteriores.size() - 1));
                listaPaginasAnteriores.remove((listaPaginasAnteriores.size() - 1));
            }
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, paginaActual);
        } else {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "nominaf");
        }
        System.out.println("ListaPaginasAnteriores : " + listaPaginasAnteriores + "::[" + paginaActual + "]");
    }

    public List<String> getListaPaginasAnteriores() {
        return listaPaginasAnteriores;
    }

    public void setListaPaginasAnteriores(List<String> listaPaginasAnteriores) {
        this.listaPaginasAnteriores = listaPaginasAnteriores;
    }

}
