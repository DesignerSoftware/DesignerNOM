/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosAusentismos;
import InterfaceAdministrar.AdministrarMotivosAusentismosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosAusentismosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarMotivosAusentismos implements AdministrarMotivosAusentismosInterface {

    private static Logger log = Logger.getLogger(AdministrarMotivosAusentismos.class);

    @EJB
    PersistenciaMotivosAusentismosInterface persistenciaMotivosAusentismos;
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
    public String modificarMotivosAusentismos(MotivosAusentismos motivoAusentismo) {
        try {
            return persistenciaMotivosAusentismos.editar(getEm(), motivoAusentismo);
        } catch (Exception e) {
            log.error("Error persistenciaMotivosAusentismos.modificarMotivosAusentismos : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String crearMotivosAusentismos(MotivosAusentismos motivoAusentismo) {
        try {
            return persistenciaMotivosAusentismos.crear(getEm(), motivoAusentismo);
        } catch (Exception e) {
            log.error("Error persistenciaMotivosAusentismos.crearMotivosAusentismos : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarMotivosAusentismos(MotivosAusentismos motivoAusentismo) {
        try {
            return persistenciaMotivosAusentismos.borrar(getEm(), motivoAusentismo);
        } catch (Exception e) {
            log.error("Error persistenciaMotivosAusentismos.borrarMotivosAusentismos : ", e);
            return e.getMessage();
        }
    }

    @Override
    public List<MotivosAusentismos> consultarMotivosAusentismos() {
        try {
            return persistenciaMotivosAusentismos.buscarMotivosAusentismo(getEm());
        } catch (Exception e) {
            log.error("Error persistenciaMotivosAusentismos.ConsultarMotivosAusentismos : ", e);
            return null;
        }
    }
}
