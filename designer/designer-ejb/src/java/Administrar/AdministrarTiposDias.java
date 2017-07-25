/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposDiasInterface;
import Entidades.TiposDias;
import InterfacePersistencia.PersistenciaTiposDiasInterface;
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
public class AdministrarTiposDias implements AdministrarTiposDiasInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposDias.class);

    @EJB
    PersistenciaTiposDiasInterface persistenciaTiposDias;
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
    public void modificarTiposDias(List<TiposDias> listaTiposDias) {
        for (int i = 0; i < listaTiposDias.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposDias.editar(em, listaTiposDias.get(i));
        }
    }

    @Override
    public void borrarTiposDias(List<TiposDias> listaTiposDias) {
        for (int i = 0; i < listaTiposDias.size(); i++) {
            log.warn("Administrar Borrar...");
            persistenciaTiposDias.borrar(em, listaTiposDias.get(i));
        }
    }

    @Override
    public void crearTiposDias(List<TiposDias> listaTiposDias) {
        for (int i = 0; i < listaTiposDias.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposDias.crear(em, listaTiposDias.get(i));
        }
    }

    @Override
    public List<TiposDias> mostrarTiposDias() {
        List<TiposDias> listTiposDias;
        listTiposDias = persistenciaTiposDias.buscarTiposDias(em);
        return listTiposDias;
    }

    @Override
    public TiposDias mostrarTipoDia(BigInteger secTipoDia) {
        TiposDias tiposDias;
        tiposDias = persistenciaTiposDias.buscarTipoDia(em, secTipoDia);
        return tiposDias;
    }

    @Override
    public BigInteger verificarDiasLaborales(BigInteger secuenciaTiposDias) {
        BigInteger verificarBorradoDiasLaborales;
        try {
            return verificarBorradoDiasLaborales = persistenciaTiposDias.contadorDiasLaborales(em, secuenciaTiposDias);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARTIPOSDIAS VERIFICARDIASLABORALES ERROR :" + e);
            return null;
        }
    }

    @Override
    public BigInteger verificarExtrasRecargos(BigInteger secuenciaTiposDias) {
        BigInteger verificarBorradoExtrasRecargos = null;
        try {
            verificarBorradoExtrasRecargos = persistenciaTiposDias.contadorExtrasRecargos(em, secuenciaTiposDias);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARTIPOSDIAS VERIFICAREXTRASRECARGOS ERROR :" + e);
            verificarBorradoExtrasRecargos = null;
        }
        finally{
            return verificarBorradoExtrasRecargos;
        }
    }
}
