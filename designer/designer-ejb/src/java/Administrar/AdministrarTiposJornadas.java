/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposJornadas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaTiposJornadasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;
import InterfaceAdministrar.AdministrarTiposJornadasInterface;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposJornadas implements AdministrarTiposJornadasInterface {

    private static Logger log = Logger.getLogger(AdministrarEnfermedades.class);
    @EJB
    PersistenciaTiposJornadasInterface persistenciaTiposJornadas;
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
    public String modificarTipoJornada(TiposJornadas tiposJornadas) {
        try {
            return persistenciaTiposJornadas.editar(getEm(), tiposJornadas);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarTiposAnexos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarTipoJornada(TiposJornadas tiposJornadas) {
        try {
            return persistenciaTiposJornadas.borrar(getEm(), tiposJornadas);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarTiposAnexos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearTipoJornada(TiposJornadas tiposJornadas) {
        try {
            return persistenciaTiposJornadas.crear(getEm(), tiposJornadas);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearTiposAnexos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public TiposJornadas consultarTipoJornada(BigInteger secTiposJornadas) {
        try {
            return persistenciaTiposJornadas.buscarTiposJornadas(getEm(), secTiposJornadas);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTiposAnexo() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<TiposJornadas> consultarTipoJornada() {
        try {
            return persistenciaTiposJornadas.buscarTiposJornadas(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTiposAnexos() ERROR: " + e);
            return null;
        }
    }

}
