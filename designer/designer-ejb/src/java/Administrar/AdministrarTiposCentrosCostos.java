/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposCentrosCostosInterface;
import Entidades.GruposTiposCC;
import Entidades.TiposCentrosCostos;
import InterfacePersistencia.PersistenciaGruposTiposCCInterface;
import InterfacePersistencia.PersistenciaTiposCentrosCostosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposCentrosCostos implements AdministrarTiposCentrosCostosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposCentrosCostos.class);

    @EJB
    PersistenciaTiposCentrosCostosInterface persistenciaTiposCentrosCostos;
    @EJB
    PersistenciaGruposTiposCCInterface persistenciaGruposTiposCC;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    
    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarTipoCentrosCostos(List<TiposCentrosCostos> listaTiposCentrosCostos) {
        for (int i = 0; i < listaTiposCentrosCostos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposCentrosCostos.editar(em, listaTiposCentrosCostos.get(i));
        }
    }

    @Override
    public void borrarTiposCentrosCostos(List<TiposCentrosCostos> listaTiposCentrosCostos) {
        for (int i = 0; i < listaTiposCentrosCostos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposCentrosCostos.borrar(em, listaTiposCentrosCostos.get(i));
        }
    }

    @Override
    public void crearTiposCentrosCostos(List<TiposCentrosCostos> listaTiposCentrosCostos) {
        for (int i = 0; i < listaTiposCentrosCostos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposCentrosCostos.crear(em, listaTiposCentrosCostos.get(i));
        }
    }

    @Override
    public List<TiposCentrosCostos> consultarTiposCentrosCostos() {
        List<TiposCentrosCostos> listTiposEntidades;
        listTiposEntidades = persistenciaTiposCentrosCostos.buscarTiposCentrosCostos(em);
        return listTiposEntidades;
    }

    @Override
    public TiposCentrosCostos consultarTipoCentroCosto(BigInteger secTipoCentrosCostos) {
        TiposCentrosCostos tipoCentrosCostos;
        tipoCentrosCostos = persistenciaTiposCentrosCostos.buscarTipoCentrosCostos(em, secTipoCentrosCostos);
        return tipoCentrosCostos;
    }

    @Override
    public List<GruposTiposCC> consultarLOVGruposTiposCentrosCostos() {
        List<GruposTiposCC> listGruposTiposEntidades;
        listGruposTiposEntidades = persistenciaGruposTiposCC.buscarGruposTiposCC(em);
        return listGruposTiposEntidades;
    }

    @Override
    public BigInteger contarCentrosCostosTipoCentroCosto(BigInteger secuenciaTipoEntidad) {
        BigInteger verificadorCC;
        try {
            return verificadorCC = persistenciaTiposCentrosCostos.verificarBorradoCentrosCostos(em, secuenciaTipoEntidad);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposCentrosCostos verificarBorradoCC ERROR :" + e);
            return null;
        }
    }

    @Override
    public BigInteger contarVigenciasCuentasTipoCentroCosto(BigInteger secuenciaTipoEntidad) {
        BigInteger verificadorVC;
        try {
            return verificadorVC = persistenciaTiposCentrosCostos.verificarBorradoVigenciasCuentas(em, secuenciaTipoEntidad);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposCentrosCostos verificarBorradoVC ERROR :" + e);
            return null;
        }
    }

    @Override
    public BigInteger contarRiesgosProfesionalesTipoCentroCosto(BigInteger secuenciaTipoEntidad) {
        BigInteger verificadorRP;
        try {
            return verificadorRP = persistenciaTiposCentrosCostos.verificarBorradoRiesgosProfesionales(em, secuenciaTipoEntidad);
        } catch (Exception e) {
            log.error("ERROR AdministrarTipoEntidad verificarBorrado ERROR :" + e);
            return null;
        }
    }
}
