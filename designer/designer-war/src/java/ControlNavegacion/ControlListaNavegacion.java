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

/**
 *
 * @author user
 */
//@Named(value = "controlListaNavegacion")
//@Dependent
@ManagedBean
@SessionScoped
public class ControlListaNavegacion implements Serializable{

    private List<String> listaPaginasAnteriores;

    /**
     * Creates a new instance of ControlListaNavegacion
     */
    public ControlListaNavegacion() {
        listaPaginasAnteriores = new ArrayList<String>();
        listaPaginasAnteriores.add("iniciored");
    }

    public void adicionarPagina(String pag) {
        listaPaginasAnteriores.add(pag);
        System.out.println("ControlListaNavegacion.adicionarPagina() listaPaginasAnteriores : " + listaPaginasAnteriores);
    }

    public void quitarPagina() {
        if (listaPaginasAnteriores.size() > 0) {
            listaPaginasAnteriores.remove((listaPaginasAnteriores.size() - 1));
        }
        System.out.println("ControlListaNavegacion.quitarPagina() listaPaginasAnteriores : " + listaPaginasAnteriores);
    }

    public String retornarPaginaAnterior() {
        String s = listaPaginasAnteriores.get((listaPaginasAnteriores.size() - 1));
        listaPaginasAnteriores.remove((listaPaginasAnteriores.size() - 1));
        System.out.println("ControlListaNavegacion.retornarPaginaAnterior() listaPaginasAnteriores : " + listaPaginasAnteriores);
        return s;
    }

    public List<String> getListaPaginasAnteriores() {
        return listaPaginasAnteriores;
    }

    public void setListaPaginasAnteriores(List<String> listaPaginasAnteriores) {
        this.listaPaginasAnteriores = listaPaginasAnteriores;
    }

}
