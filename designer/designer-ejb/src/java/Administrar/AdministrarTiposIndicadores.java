/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposIndicadoresInterface;
import Entidades.TiposIndicadores;
import InterfacePersistencia.PersistenciaTiposIndicadoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposIndicadores implements AdministrarTiposIndicadoresInterface {

    @EJB
    PersistenciaTiposIndicadoresInterface persistenciaTiposIndicadores;
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
    public void modificarTiposIndicadores(List<TiposIndicadores> listTiposIndicadores) {
        for (int i = 0; i < listTiposIndicadores.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposIndicadores.editar(em, listTiposIndicadores.get(i));
        }
    }

    @Override
    public void borrarTiposIndicadores(List<TiposIndicadores> listTiposIndicadores) {
        for (int i = 0; i < listTiposIndicadores.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaTiposIndicadores.borrar(em, listTiposIndicadores.get(i));
        }
    }

    @Override
    public void crearTiposIndicadores(List<TiposIndicadores> listTiposIndicadores) {
        for (int i = 0; i < listTiposIndicadores.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaTiposIndicadores.crear(em, listTiposIndicadores.get(i));
        }
    }

    @Override
    public List<TiposIndicadores> consultarTiposIndicadores() {
        List<TiposIndicadores> listTiposIndicadores;
        listTiposIndicadores = persistenciaTiposIndicadores.buscarTiposIndicadores(em);
        return listTiposIndicadores;
    }

    @Override
    public TiposIndicadores consultarTipoIndicador(BigInteger secMotivoDemanda) {
        TiposIndicadores tiposIndicadores;
        tiposIndicadores = persistenciaTiposIndicadores.buscarTiposIndicadoresSecuencia(em, secMotivoDemanda);
        return tiposIndicadores;
    }

    @Override
    public BigInteger contarVigenciasIndicadoresTipoIndicador(BigInteger secuenciaVigenciasIndicadores) {
        BigInteger verificadorVigenciasIndicadores = null;

        try {
            System.err.println("Secuencia Vigencias Indicadores " + secuenciaVigenciasIndicadores);
            verificadorVigenciasIndicadores = persistenciaTiposIndicadores.contadorVigenciasIndicadores(em, secuenciaVigenciasIndicadores);
        } catch (Exception e) {
            System.err.println("ERROR AdmnistrarTiposIndicadores verificarBorradoVigenciasIndicadores ERROR :" + e);
        } finally {
            return verificadorVigenciasIndicadores;
        }
    }
}
