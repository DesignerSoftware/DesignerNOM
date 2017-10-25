/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Modulos;
import InterfaceAdministrar.AdministrarModulosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaModulosInterface;
import static java.lang.StrictMath.log;
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
public class AdministrarModulos implements AdministrarModulosInterface {

    private static Logger log = Logger.getLogger(AdministrarModulos.class);

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
    public String modificarModulos(Modulos modulo) {
        try {
            return persistenciaModulos.editar(getEm(), modulo);
        } catch (Exception e) {
            log.error("Error AdministrarModulos.modificarModulos : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String crearModulos(Modulos modulo) {
        try {
            return persistenciaModulos.crear(getEm(), modulo);
        } catch (Exception e) {
            log.error("Error AdministrarModulos.crearModulos : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarModulos(Modulos modulo) {
        try {
            return persistenciaModulos.borrar(getEm(), modulo);
        } catch (Exception e) {
            log.error("Error AdministrarModulos.borrarModulos : ", e);
            return e.getMessage();
        }
    }

    @Override
    public List<Modulos> consultarModulos() {
        try {
            return persistenciaModulos.buscarModulos(getEm());
        } catch (Exception e) {
            log.error("Error AdministrarModulos.consultarModulos : ", e);
            return null;
        }
    }

}
