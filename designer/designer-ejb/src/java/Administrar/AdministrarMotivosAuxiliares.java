/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosAuxiliares;
import InterfaceAdministrar.AdministrarMotivosAuxiliaresInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosAuxiliaresInterface;
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
public class AdministrarMotivosAuxiliares implements AdministrarMotivosAuxiliaresInterface {

    private static Logger log = Logger.getLogger(AdministrarMotivosAuxiliares.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaMotivosAuxiliaresInterface persistenciaMotivosAuxiliares;

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
    public String modificarMotivosAuxiliares(MotivosAuxiliares motivo) {
        try {
            return persistenciaMotivosAuxiliares.editar(getEm(), motivo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarMotivosAuxiliares() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarMotivosAuxiliares(MotivosAuxiliares motivo) {
         try {
            return persistenciaMotivosAuxiliares.borrar(getEm(), motivo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarMotivosAuxiliares() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearMotivosAuxiliares(MotivosAuxiliares motivo) {
         try {
            return persistenciaMotivosAuxiliares.crear(getEm(), motivo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearMotivosAuxiliares() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public List<MotivosAuxiliares> buscarMotivosAuxiliares() {
         try {
            return persistenciaMotivosAuxiliares.buscarMotivosAuxiliares(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".buscarMotivosAuxiliares() ERROR: " + e);
            return null;
        }
    }

}
