/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposExamenesInterface;
import Entidades.TiposExamenes;
import InterfacePersistencia.PersistenciaTiposExamenesInterface;
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
public class AdministrarTiposExamenes implements AdministrarTiposExamenesInterface {

    private static Logger log = Logger.getLogger(AdministrarTiposExamenes.class);

    @EJB
    PersistenciaTiposExamenesInterface persistenciaTiposExamenes;
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
    public String modificarTiposExamenes(TiposExamenes tipoExamen) {
        try {
            return persistenciaTiposExamenes.editar(getEm(), tipoExamen);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarTiposExamenes() ERROR: " + e);
            return e.getMessage();
        }
    }

    public String borrarTiposExamenes(TiposExamenes tipoExamen) {
        try {
            return persistenciaTiposExamenes.borrar(getEm(), tipoExamen);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarTiposExamenes() ERROR: " + e);
            return e.getMessage();
        }
    }

    public String crearTiposExamenes(TiposExamenes tipoExamen) {
        try {
            return persistenciaTiposExamenes.crear(getEm(), tipoExamen);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearTiposExamenes() ERROR: " + e);
            return e.getMessage();
        }
    }

    public List<TiposExamenes> consultarTiposExamenes() {
        try {
            return persistenciaTiposExamenes.buscarTiposExamenes(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTiposExamenes() ERROR: " + e);
            return null;
        }
    }

    public TiposExamenes consultarTipoExamen(BigInteger secTipoEmpresa) {
        try {
            return persistenciaTiposExamenes.buscarTipoExamen(getEm(), secTipoEmpresa);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTipoExamen() ERROR: " + e);
            return null;
        }
    }

    public BigInteger contarTiposExamenesCargosTipoExamen(BigInteger secuenciaTiposExamenesCargos) {
        try {
            log.error("Secuencia Borrado Elementos" + secuenciaTiposExamenesCargos);
            return persistenciaTiposExamenes.contadorTiposExamenesCargos(getEm(), secuenciaTiposExamenesCargos);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposExamenes verificarBorradoTiposExamenesCargos ERROR :" + e);
            return null;
        }
    }

    public BigInteger contarVigenciasExamenesMedicosTipoExamen(BigInteger secuenciaVigenciasExamenesMedicos) {
        try {
            log.error("Secuencia Borrado Vigencias Tallas" + secuenciaVigenciasExamenesMedicos);
            return persistenciaTiposExamenes.contadorVigenciasExamenesMedicos(getEm(), secuenciaVigenciasExamenesMedicos);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposExamenes verificarBorradoVigenciasExamenesMedicos ERROR :" + e);
            return null;
        }
    }
}
