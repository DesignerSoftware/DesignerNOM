/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ClasesPensiones;
import InterfaceAdministrar.AdministrarClasesPensionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaClasesPensionesInterface;
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
public class AdministrarClasesPensiones implements AdministrarClasesPensionesInterface {

    private static Logger log = Logger.getLogger(AdministrarClasesPensiones.class);

    @EJB
    PersistenciaClasesPensionesInterface persistenciaClasesPensiones;
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

    @Override
    public String modificarClasesPensiones(ClasesPensiones claseP) {
        try {
            return persistenciaClasesPensiones.editar(getEm(), claseP);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarClasesPensiones() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarClasesPensiones(ClasesPensiones claseP) {
        try {
            return persistenciaClasesPensiones.borrar(getEm(), claseP);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarClasesPensiones() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearClasesPensiones(ClasesPensiones claseP) {
        try {
            return persistenciaClasesPensiones.crear(getEm(), claseP);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearClasesPensiones() ERROR: " + e);
            return e.getMessage();
        }
    }

    public List<ClasesPensiones> consultarClasesPensiones() {
        try {
            List<ClasesPensiones> listMotivosCambiosCargos;
            listMotivosCambiosCargos = persistenciaClasesPensiones.consultarClasesPensiones(getEm());
            return listMotivosCambiosCargos;
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarClasesPensiones() ERROR: " + e);
            return null;
        }
    }

    @Override
    public ClasesPensiones consultarClasePension(BigInteger secClasesPensiones) {
        try {
            ClasesPensiones subCategoria;
            subCategoria = persistenciaClasesPensiones.consultarClasePension(getEm(), secClasesPensiones);
            return subCategoria;
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarClasePension() ERROR: " + e);
            return null;
        }
    }

    @Override
    public BigInteger contarRetiradosClasePension(BigInteger secClasesPensiones) {
        try {
            return persistenciaClasesPensiones.contarRetiradosClasePension(getEm(), secClasesPensiones);
        } catch (Exception e) {
            log.error("ERROR AdministrarClasesPensiones contarEscalafones ERROR : " + e);
            return null;
        }
    }
}
