/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Modulos;
import Entidades.ObjetosDB;
import InterfaceAdministrar.AdministrarObjetosDBInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaModulosInterface;
import InterfacePersistencia.PersistenciaObjetosDBInterface;
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
public class AdministrarObjetosDB implements AdministrarObjetosDBInterface {

    private static Logger log = Logger.getLogger(AdministrarObjetosDB.class);

    @EJB
    PersistenciaObjetosDBInterface persistenciaObjetosDB;
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
    public String modificarObjetosDB(ObjetosDB objeto) {
        try {
            return persistenciaObjetosDB.editar(getEm(), objeto);
        } catch (Exception e) {
            log.error("Error PersistenciaObjetosDB.modificarObjetosDB : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String crearObjetosDB(ObjetosDB objeto) {
        try {
            return persistenciaObjetosDB.crear(getEm(), objeto);
        } catch (Exception e) {
            log.error("Error PersistenciaObjetosDB.crearObjetosDB : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarObjetosDB(ObjetosDB objeto) {
        try {
            return persistenciaObjetosDB.borrar(getEm(), objeto);
        } catch (Exception e) {
            log.error("Error PersistenciaObjetosDB.borrarObjetosDB : ", e);
            return e.getMessage();
        }
    }

    @Override
    public List<ObjetosDB> consultarObjetosDB() {
        try {
            return persistenciaObjetosDB.consultarObjetoDB(getEm());
        } catch (Exception e) {
            log.error("Error PersistenciaObjetosDB.consultarObjetosDB : ", e);
            return null;
        }
    }

    @Override
    public List<Modulos> consultarModulos() {
        try {
            return persistenciaModulos.listaModulos(getEm());
        } catch (Exception e) {
            log.error("Error PersistenciaObjetosDB.consultarModulos : ", e);
            return null;
        }
    }

}
