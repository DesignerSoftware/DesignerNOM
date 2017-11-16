/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Errores;
import InterfaceAdministrar.AdministrarErroresInterfaz;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaErroresInterfaz;
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
public class AdministrarErrores implements AdministrarErroresInterfaz {

    private static Logger log = Logger.getLogger(AdministrarEnfermedades.class);
    @EJB
    PersistenciaErroresInterfaz persistenciaErrores;
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
    public String modificarErrores(Errores errores) {
         try {
            return persistenciaErrores.editar(getEm(), errores);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarErrores() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarErrores(Errores errores) {
         try {
            return persistenciaErrores.borrar(getEm(), errores);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarErrores() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearErrores(Errores errores) {
        try {
            return persistenciaErrores.crear(getEm(), errores);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearErrores() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public Errores consultarErrores(BigInteger secErrores) {
          try {
            return persistenciaErrores.buscarErrores(getEm(), secErrores);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTiposAnexo() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Errores> consultarErrores() {
           try {
            return persistenciaErrores.buscarErrores(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarErrores() ERROR: " + e);
            return null;
        }
    }
}
