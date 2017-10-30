/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Modulos;
import Entidades.Tablas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarTablasInterface;
import InterfacePersistencia.PersistenciaModulosInterface;
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
public class AdministrarTablas  implements AdministrarTablasInterface{

      private static Logger log = Logger.getLogger(AdministrarPantallas.class);
      
    @EJB
    PersistenciaTablasInterface persistenciaTablas;
    @EJB
    PersistenciaModulosInterface persistenciaModulos;
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
    public String modificarTablas(Tablas tabla) {
        try {
            return persistenciaTablas.editar(getEm(), tabla);
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.modificarPantallas : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String crearTablas(Tablas tabla) {
       try {
            return persistenciaTablas.crear(getEm(), tabla);
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.crearTablas : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarTablas(Tablas tabla) {
       try {
            return persistenciaTablas.borrar(getEm(), tabla);
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.borrarTablas : ", e);
            return e.getMessage();
        }
    }

    @Override
    public List<Modulos> consultarModulos() {
       try {
            return persistenciaModulos.buscarModulos(getEm());
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.consultarModulos : ", e);
            return null;
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

}
