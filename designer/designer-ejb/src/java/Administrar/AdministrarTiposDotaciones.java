/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposDotaciones;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarTiposDotacionesInterface;
import InterfacePersistencia.PersistenciaTiposDotacionesInterface;
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
public class AdministrarTiposDotaciones implements AdministrarTiposDotacionesInterface {

    private static Logger log = Logger.getLogger(AdministrarTiposDotaciones.class);

    @EJB
    PersistenciaTiposDotacionesInterface persistenciaTiposDotaciones;
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
    public String modificarTiposDotaciones(TiposDotaciones tipoDotacion) {
        try {
            return persistenciaTiposDotaciones.editar(getEm(), tipoDotacion);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarTiposDotaciones() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarTiposDotaciones(TiposDotaciones tipoDotacion) {
        try {
            return persistenciaTiposDotaciones.borrar(getEm(), tipoDotacion);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarTiposDotaciones() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearTiposDotaciones(TiposDotaciones tipoDotacion) {
         try {
            return persistenciaTiposDotaciones.crear(getEm(), tipoDotacion);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearTiposDotaciones() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public List<TiposDotaciones> consultarTiposDotaciones() {
       try {
         return persistenciaTiposDotaciones.consultarTiposDotaciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTiposDotaciones() ERROR: " + e);
         return null;
      }
    }

}
