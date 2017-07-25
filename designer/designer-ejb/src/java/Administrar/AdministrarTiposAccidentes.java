/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposAccidentes;
import InterfaceAdministrar.AdministrarTiposAccidentesInterface;
import InterfacePersistencia.PersistenciaTiposAccidentesInterface;
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
public class AdministrarTiposAccidentes implements AdministrarTiposAccidentesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposAccidentes.class);

    @EJB
    PersistenciaTiposAccidentesInterface persistenciaTiposAccidentes;
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
    public void modificarTiposAccidentes(List<TiposAccidentes> listaTiposAccidentes) {
        for (int i = 0; i < listaTiposAccidentes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposAccidentes.editar(em, listaTiposAccidentes.get(i));
        }
    }
    @Override
    public void borrarTiposAccidentes(List<TiposAccidentes> listaTiposAccidentes) {
        for (int i = 0; i < listaTiposAccidentes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposAccidentes.borrar(em, listaTiposAccidentes.get(i));
        }
    }
    @Override
    public void crearTiposAccidentes(List<TiposAccidentes> listaTiposAccidentes) {
        for (int i = 0; i < listaTiposAccidentes.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposAccidentes.crear(em, listaTiposAccidentes.get(i));
        }
    }
    @Override
    public List<TiposAccidentes> consultarTiposAccidentes() {
        List<TiposAccidentes> listTiposAccidentes;
        listTiposAccidentes = persistenciaTiposAccidentes.buscarTiposAccidentes(em);
        return listTiposAccidentes;
    }
    @Override
    public TiposAccidentes consultarTiposAccidentes(BigInteger secTiposAccidentes) {
        TiposAccidentes tiposAccidentes;
        tiposAccidentes = persistenciaTiposAccidentes.buscarTipoAccidente(em, secTiposAccidentes);
        return tiposAccidentes;
    }
    @Override
    public BigInteger contarSoAccidentesMedicosTipoAccidente(BigInteger secuenciaTiposAccidentes) {
        BigInteger verificarSoAccidentesMedicos;

        try {
            return verificarSoAccidentesMedicos = persistenciaTiposAccidentes.contadorSoAccidentesMedicos(em, secuenciaTiposAccidentes);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARTIPOSACCIDENTES verificarSoAccidentesMedicos ERROR :" + e);
            return null;
        } finally {
        }
    }
    @Override
    public BigInteger contarAccidentesTipoAccidente(BigInteger secuenciaTiposAccidentes) {
        BigInteger verificarBorradoAccidentes = null;
        try {
            verificarBorradoAccidentes = persistenciaTiposAccidentes.contadorAccidentes(em, secuenciaTiposAccidentes);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARTIPOSACCIDENTES verificarBorradoAccidentes ERROR :" + e);
            verificarBorradoAccidentes = null;
        } finally {
            return verificarBorradoAccidentes;
        }
    }
}
