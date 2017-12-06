/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ObjetosBloques;
import Entidades.PermisosPantallas;
import InterfaceAdministrar.AdministrarPermisosPantallasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaObjetosBloquesInterface;
import InterfacePersistencia.PersistenciaPermisosPantallasInterface;
import java.math.BigInteger;
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
public class AdministrarPermisosPantallas implements AdministrarPermisosPantallasInterface {

    private static Logger log = Logger.getLogger(AdministrarPermisosPantallas.class);

    @EJB
    PersistenciaObjetosBloquesInterface persistenciaObjetosBloques;
    @EJB
    PersistenciaPermisosPantallasInterface persistenciaPermisos;
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
    public String modificarPermiso(PermisosPantallas permiso) {
        try {
            return persistenciaPermisos.editar(getEm(), permiso);
        } catch (Exception e) {
            log.error("Error AdministrarPermisosPantallas.modificarPermiso : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String crearPermiso(PermisosPantallas permiso) {
        try {
            return persistenciaPermisos.crear(getEm(), permiso);
        } catch (Exception e) {
            log.error("Error AdministrarPermisosPantallas.crearPermiso : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarPermiso(PermisosPantallas permiso) {
        try {
            return persistenciaPermisos.borrar(getEm(), permiso);
        } catch (Exception e) {
            log.error("Error AdministrarPermisosPantallas.borrarPermiso : ", e);
            return e.getMessage();
        }
    }

    @Override
    public List<ObjetosBloques> lovObjetosBloques() {
        try {
            return persistenciaObjetosBloques.consultarObjetosBloques(em);
        } catch (Exception e) {
            log.error("Error AdministrarPermisosPantallas.borrarPermiso : ", e);
            return null;
        }
    }

    @Override
    public List<PermisosPantallas> consultarPermisosPantallas(EntityManager em, BigInteger secPerfil) {
        try {
            return persistenciaPermisos.consultarPermisosPorPerfil(em, secPerfil);
        } catch (Exception e) {
            log.error("Error AdministrarPermisosPantallas.consultarPermisosPantallas : ", e);
            return null;
        }
    }

    @Override
    public List<PermisosPantallas> consultarPermisosPantallas() {
        try {
            return persistenciaPermisos.consultarPermisosPorPerfil(getEm());
        } catch (Exception e) {
            log.error("Error AdministrarPermisosPantallas.consultarPermisosPantallas : ", e);
            return null;
        }
    }

    @Override
    public Integer conteoPantallas(BigInteger secPerfil, BigInteger secObjetos) {
       try {
            return persistenciaPermisos.conteo(getEm(), secPerfil, secObjetos);
        } catch (Exception e) {
            log.error("Error AdministrarPermisosPantallas.consultarPermisosPantallas : ", e);
            return null;
        }
    }

}
