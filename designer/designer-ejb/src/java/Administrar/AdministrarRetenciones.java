/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Retenciones;
import Entidades.VigenciasRetenciones;
import InterfaceAdministrar.AdministrarRetencionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRetencionesInterface;
import InterfacePersistencia.PersistenciaVigenciasRetencionesInterface;
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
public class AdministrarRetenciones implements AdministrarRetencionesInterface {

    private static Logger log = Logger.getLogger(AdministrarRetenciones.class);

    @EJB
    PersistenciaRetencionesInterface persistenciaRetenciones;
    @EJB
    PersistenciaVigenciasRetencionesInterface persistenciaVigenciasRetenciones;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
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

//VIGENCIAS RETENCIONES
    @Override
    public String borrarVigenciaRetencion(VigenciasRetenciones vretenciones) {
        try {
            return persistenciaVigenciasRetenciones.borrar(getEm(), vretenciones);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarVigenciaRetencion() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearVigenciaRetencion(VigenciasRetenciones vretenciones) {
        try {
            return persistenciaVigenciasRetenciones.crear(getEm(), vretenciones);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearVigenciaRetencion() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String modificarVigenciaRetencion(VigenciasRetenciones vretenciones) {
        try {
            return persistenciaVigenciasRetenciones.editar(getEm(), vretenciones);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarVigenciaRetencion() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public List<VigenciasRetenciones> consultarVigenciasRetenciones() {
        try {
            return persistenciaVigenciasRetenciones.buscarVigenciasRetenciones(getEm());
        } catch (Exception e) {
            log.warn("Error consultarVigenciasRetenciones: " + e.toString());
            return null;
        }
    }

//RETENCIONES
    @Override
    public String borrarRetencion(Retenciones retenciones) {
        try {
            return persistenciaRetenciones.borrar(getEm(), retenciones);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarRetencion() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearRetencion(Retenciones retenciones) {
        try {
            return persistenciaRetenciones.crear(getEm(), retenciones);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearRetencion() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String modificarRetencion(Retenciones retenciones) {
        try {
            return persistenciaRetenciones.editar(getEm(), retenciones);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarRetencion() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public List<Retenciones> consultarRetenciones(BigInteger secRetencion) {
        try {
            return persistenciaRetenciones.buscarRetencionesVig(getEm(), secRetencion);
        } catch (Exception e) {
            log.warn("Error conceptoActual Admi : " + e.toString());
            return null;
        }
    }

}
