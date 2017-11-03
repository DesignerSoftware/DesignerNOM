/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.BloquesPantallas;
import Entidades.ObjetosBloques;
import InterfaceAdministrar.AdministrarObjetosBloquesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaObjetosBloquesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarObjetosBloques implements AdministrarObjetosBloquesInterface {

    private static Logger log = Logger.getLogger(AdministrarObjetosDB.class);

    @EJB
    PersistenciaObjetosBloquesInterface persistenciaObjetosBloques;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManagerFactory emf;
    private EntityManager em;
    private String idSesionBck;

    private EntityManager getEm() {
        try {
            if (this.emf != null) {
                if (this.em != null) {
                    if (this.em.isOpen()) {
                        this.em.close();
                    }
                }
            } else {
                this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
            }
            this.em = emf.createEntityManager();
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
        }
        return this.em;
    }

    @Override
    public void obtenerConexion(String idSesion) {
        idSesionBck = idSesion;
        try {
            emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
        }
    }

    @Override
    public String modificar(ObjetosBloques objeto) {
        try {
            return persistenciaObjetosBloques.editar(getEm(), objeto);
        } catch (Exception e) {
            log.error("Error PersistenciaObjetosDB.modificar : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String crear(ObjetosBloques objeto) {
        try {
            return persistenciaObjetosBloques.crear(getEm(), objeto);
        } catch (Exception e) {
            log.error("Error PersistenciaObjetosDB.crear : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String borrar(ObjetosBloques objeto) {
        try {
            return persistenciaObjetosBloques.borrar(getEm(), objeto);
        } catch (Exception e) {
            log.error("Error PersistenciaObjetosDB.borrar : ", e);
            return e.getMessage();
        }
    }

    @Override
    public List<ObjetosBloques> consultarObjetosBloques() {
        try {
            return persistenciaObjetosBloques.consultarObjetosBloques(getEm());
        } catch (Exception e) {
            log.error("Error PersistenciaObjetosDB.consultarObjetosBloques : ", e);
            return null;
        }
    }

    @Override
    public List<BloquesPantallas> consultarBloquesPantallas() {
        try {
            return persistenciaObjetosBloques.consultarBloquesPantallas(getEm());
        } catch (Exception e) {
            log.error("Error PersistenciaObjetosDB.consultarBloquesPantallas : ", e);
            return null;
        }
    }

}
