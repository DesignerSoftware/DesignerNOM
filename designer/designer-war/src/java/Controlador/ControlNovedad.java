/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.ObjetosJsf;
import InterfaceAdministrar.AdministrarPermisosObjetosJsfInterface;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Named(value = "controlNovedad")
@SessionScoped
public class ControlNovedad implements Serializable {

   private static Logger log = Logger.getLogger(ControlNovedad.class);

    @EJB
    AdministrarPermisosObjetosJsfInterface administrarPermisosObjetosJsf;
    private List<ObjetosJsf> ListObjetosJSF;
    ControlListaNavegacion controlListaNavegacion;

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
            administrarPermisosObjetosJsf.obtenerConexion(ses.getId());
            controlListaNavegacion = (ControlListaNavegacion) x.getApplication().evaluateExpressionGet(x, "#{controlListaNavegacion}", ControlListaNavegacion.class);
            ListObjetosJSF = null;
            getListObjetosJSF();
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }
    
    
    public ControlNovedad() {
    }

    public boolean consultarPermisosPorId(String id) {
        boolean enable = false;
        if (ListObjetosJSF != null) {
            if (!ListObjetosJSF.isEmpty()) {
                for (int i = 0; i < ListObjetosJSF.size(); i++) {
                    if (id.equalsIgnoreCase(ListObjetosJSF.get(i).getIdentificador())) {
                        enable = ListObjetosJSF.get(i).getEnable().equals("N");
                        i = ListObjetosJSF.size();
                    }
                }
            }
        }
        return enable;
    }
    
    ////GETS Y SETS
    
   public List<ObjetosJsf> getListObjetosJSF() {
        if (ListObjetosJSF == null) {
            ListObjetosJSF = administrarPermisosObjetosJsf.consultarEnable("NOVEDAD");
        }
        return ListObjetosJSF;
    }

    public void setListObjetosJSF(List<ObjetosJsf> ListObjetosJSF) {
        this.ListObjetosJSF = ListObjetosJSF;
    }
    
}
