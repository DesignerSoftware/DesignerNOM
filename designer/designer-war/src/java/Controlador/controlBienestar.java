/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class controlBienestar implements Serializable {

    private String paginaAnterior;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    /**
     * Creates a new instance of controlBienestar
     */
    public controlBienestar() {
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirAtras(String atras) {
        paginaAnterior = atras;
        System.out.println("ControlBienestar pagina anterior : " + paginaAnterior);
    }

    public String redireccionarAtras() {
        return paginaAnterior;
    }
}
