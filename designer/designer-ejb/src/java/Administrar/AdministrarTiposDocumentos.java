/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposDocumentosInterface;
import Entidades.TiposDocumentos;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposDocumentos implements AdministrarTiposDocumentosInterface {

    private static Logger log = Logger.getLogger(AdministrarTiposDocumentos.class);

    @EJB
    PersistenciaTiposDocumentosInterface persistenciaTiposDocumentos;
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
    public String modificarTiposDocumentos(TiposDocumentos td) {
        try {
            return persistenciaTiposDocumentos.editar(getEm(), td);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarTiposDocumentos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarTiposDocumentos(TiposDocumentos td) {
        try {
            return persistenciaTiposDocumentos.borrar(getEm(), td);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarTiposDocumentos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearTiposDocumentos(TiposDocumentos td) {
        try {
            return persistenciaTiposDocumentos.crear(getEm(), td);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearTiposDocumentos() ERROR: " + e);
            return e.getMessage();
        }
    }

    public List<TiposDocumentos> consultarTiposDocumentos() {
        try {
            return persistenciaTiposDocumentos.consultarTiposDocumentos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTiposDocumentos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public TiposDocumentos consultarTipoDocumento(BigInteger secTiposDocumentos) {
        try {
            return persistenciaTiposDocumentos.consultarTipoDocumento(getEm(), secTiposDocumentos);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTipoDocumento() ERROR: " + e);
            return null;
        }
    }

    @Override
    public BigInteger contarCodeudoresTipoDocumento(BigInteger secTiposDocumentos) {
        try {
            return persistenciaTiposDocumentos.contarCodeudoresTipoDocumento(getEm(), secTiposDocumentos);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposDocumentos contarCodeudoresTipoDocumento ERROR : " + e);
            return null;
        }
    }

    @Override
    public BigInteger contarPersonasTipoDocumento(BigInteger secTiposDocumentos) {
        try {
            return persistenciaTiposDocumentos.contarPersonasTipoDocumento(getEm(), secTiposDocumentos);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposDocumentos contarPersonasTipoDocumento ERROR : " + e);
            return null;
        }
    }
}
