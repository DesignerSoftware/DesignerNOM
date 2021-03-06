/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposReemplazos;
import InterfaceAdministrar.AdministrarTiposReemplazosInterface;
import InterfacePersistencia.PersistenciaTiposReemplazosInterface;
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
public class AdministrarTiposReemplazos implements AdministrarTiposReemplazosInterface {

    private static Logger log = Logger.getLogger(AdministrarTiposReemplazos.class);

    @EJB
    PersistenciaTiposReemplazosInterface persistenciaTiposReemplazos;
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
    public String modificarTiposReemplazos(TiposReemplazos tipoReemplazo) {
        try {
            return persistenciaTiposReemplazos.editar(getEm(), tipoReemplazo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarTiposReemplazos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarTiposReemplazos(TiposReemplazos tipoReemplazo) {
        try {
            return persistenciaTiposReemplazos.borrar(getEm(), tipoReemplazo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarTiposReemplazos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearTiposReemplazos(TiposReemplazos tipoReemplazo) {
        try {
            return persistenciaTiposReemplazos.crear(getEm(), tipoReemplazo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearTiposReemplazos() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public List<TiposReemplazos> consultarTiposReemplazos() {
        try {
            return persistenciaTiposReemplazos.buscarTiposReemplazos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTiposReemplazos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public TiposReemplazos consultarTipoReemplazo(BigInteger secMotivoDemanda) {
        try {
            return persistenciaTiposReemplazos.buscarTipoReemplazo(getEm(), secMotivoDemanda);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTipoReemplazo() ERROR: " + e);
            return null;
        }
    }

    @Override
    public BigInteger contarEncargaturasTipoReemplazo(BigInteger secuenciaTiposReemplazos) {
        try {
            log.warn("Secuencia Vigencias Indicadores " + secuenciaTiposReemplazos);
            return persistenciaTiposReemplazos.contadorEncargaturas(getEm(), secuenciaTiposReemplazos);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARTIPOSREEMPLAZOS VERIFICARBORRADOENCARGATURAS ERROR :" + e);
            return null;
        }
    }

    @Override
    public BigInteger contarProgramacionesTiemposTipoReemplazo(BigInteger secuenciaTiposReemplazos) {
        try {
            log.warn("Secuencia Vigencias Indicadores " + secuenciaTiposReemplazos);
            return persistenciaTiposReemplazos.contadorProgramacionesTiempos(getEm(), secuenciaTiposReemplazos);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARTIPOSREEMPLAZOS VERIFICARBORRADOPROGRAMACIONESTIEMPOS ERROR :" + e);
            return null;
        }
    }

    @Override
    public BigInteger contarReemplazosTipoReemplazo(BigInteger secuenciaTiposReemplazos) {
        try {
            log.warn("Secuencia Vigencias Indicadores " + secuenciaTiposReemplazos);
            return persistenciaTiposReemplazos.contadorReemplazos(getEm(), secuenciaTiposReemplazos);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARTIPOSREEMPLAZOS VERIFICARBORRADOREEMPLAZOS ERROR :" + e);
            return null;
        }
    }

    @Override
    public List<TiposReemplazos> consultarLOVTiposReemplazos() {
        try {
            return persistenciaTiposReemplazos.buscarTiposReemplazos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarLOVTiposReemplazos() ERROR: " + e);
            return null;
        }
    }
}
