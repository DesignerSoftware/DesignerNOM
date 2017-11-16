/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposAnexos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarTiposAnexosInterface;
import InterfacePersistencia.PersistenciaTiposAnexosInterface;
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
public class AdministrarTiposAnexos implements AdministrarTiposAnexosInterface {

    private static Logger log = Logger.getLogger(AdministrarEnfermedades.class);
    @EJB
    PersistenciaTiposAnexosInterface persistenciaTiposAnexos;
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
    public String modificarTiposAnexos(TiposAnexos tiposAnexos) {
        try {
            return persistenciaTiposAnexos.editar(getEm(), tiposAnexos);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarTiposAnexos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarTiposAnexos(TiposAnexos tiposAnexos) {
        try {
            return persistenciaTiposAnexos.borrar(getEm(), tiposAnexos);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarTiposAnexos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearTiposAnexos(TiposAnexos tiposAnexos) {
        try {
            return persistenciaTiposAnexos.crear(getEm(), tiposAnexos);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearTiposAnexos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public TiposAnexos consultarTiposAnexo(BigInteger secTiposAnexos) {
        try {
            return persistenciaTiposAnexos.buscarTiposAnexos(getEm(), secTiposAnexos);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTiposAnexo() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<TiposAnexos> consultarTiposAnexos() {
        try {
            return persistenciaTiposAnexos.buscarTiposAnexos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTiposAnexos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public BigInteger verificarTiposAnexos(BigInteger secTiposAnexos) {
        try {
            return persistenciaTiposAnexos.contadorAnexos(getEm(), secTiposAnexos);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARTIPOSANEXOS verificarTiposAnexos ERROR :" + e);
            return null;
        }
    }
}
