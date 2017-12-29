/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Roles;
import Entidades.Tablas;
import InterfaceAdministrar.AdministrarRolInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRolInterface;
import InterfacePersistencia.PersistenciaTablasInterface;
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
public class AdministrarRol implements AdministrarRolInterface {

    private static Logger log = Logger.getLogger(AdministrarPantallas.class);

    @EJB
    PersistenciaTablasInterface persistenciaTablas;
    @EJB
    PersistenciaRolInterface persistenciaRol;
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
    public String modificarRol(Roles rol) {
        try {
            return persistenciaRol.editar(getEm(), rol);
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.modificarRol : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String crearRol(Roles rol) {
        try {
            return persistenciaRol.crear(getEm(), rol);
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.crearRol : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarRol(Roles rol) {
        try {
            return persistenciaRol.borrar(getEm(), rol);
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.borrarRol : ", e);
            return e.getMessage();
        }
    }

    @Override
    public List<Tablas> consultarTablas() {
        try {
            return persistenciaTablas.consultarTablas(getEm());
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.consultarTablas : ", e);
            return null;
        }
    }

    @Override
    public List<Roles> consultarRol() {
        try {
            return persistenciaRol.consultarRol(getEm());
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.consultarModulos : ", e);
            return null;
        }
    }

}
