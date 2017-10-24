/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosEvaluaciones;
import InterfaceAdministrar.AdministrarMotivosEvaluacionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosEvaluacionesInterface;
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
public class AdministrarMotivosEvaluaciones implements AdministrarMotivosEvaluacionesInterface {

    private static Logger log = Logger.getLogger(AdministrarMotivosEvaluaciones.class);

    @EJB
    PersistenciaMotivosEvaluacionesInterface persistenciaMotivosEvaluaciones;
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
    public String modificarMotivosEvaluaciones(MotivosEvaluaciones motivo) {
        try {
            return persistenciaMotivosEvaluaciones.editar(getEm(), motivo);
        } catch (Exception e) {
            log.error("Error persistenciaMotivosEvaluaciones.modificarMotivosEvaluaciones : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String crearMotivosEvaluaciones(MotivosEvaluaciones motivo) {
        try {
            return persistenciaMotivosEvaluaciones.crear(getEm(), motivo);
        } catch (Exception e) {
            log.error("Error persistenciaMotivosEvaluaciones.crearMotivosEvaluaciones : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarMotivosEvaluaciones(MotivosEvaluaciones motivo) {
        try {
            return persistenciaMotivosEvaluaciones.borrar(getEm(), motivo);
        } catch (Exception e) {
            log.error("Error persistenciaMotivosEvaluaciones.borrarMotivosEvaluaciones : ", e);
            return e.getMessage();
        }
    }

    @Override
    public List<MotivosEvaluaciones> consultarMotivosEvaluaciones() {
        try {
            return persistenciaMotivosEvaluaciones.buscarMotivosEvaluaciones(getEm());
        } catch (Exception e) {
            log.error("Error persistenciaMotivosEvaluaciones.ConsultarMotivosEvaluaciones : ", e);
            return null;
        }
    }

}
