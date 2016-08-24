/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EstadosCiviles;
import InterfaceAdministrar.AdministrarEstadosCivilesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEstadosCivilesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarEstadosCiviles implements AdministrarEstadosCivilesInterface {

    @EJB
    PersistenciaEstadosCivilesInterface persistenciaEstadosCiviles;
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
    public void modificarEstadosCiviles(List<EstadosCiviles> listaEstadosCiviles) {
        for (int i = 0; i < listaEstadosCiviles.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaEstadosCiviles.editar(em,listaEstadosCiviles.get(i));
        }
    }

    @Override
    public void borrarEstadosCiviles(List<EstadosCiviles> listaEstadosCiviles) {
        for (int i = 0; i < listaEstadosCiviles.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaEstadosCiviles.borrar(em,listaEstadosCiviles.get(i));
        }
    }

    @Override
    public void crearEstadosCiviles(List<EstadosCiviles> listaEstadosCiviles) {
        for (int i = 0; i < listaEstadosCiviles.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaEstadosCiviles.crear(em,listaEstadosCiviles.get(i));
        }
    }

    @Override
    public List<EstadosCiviles> consultarEstadosCiviles() {
        List<EstadosCiviles> listEstadosCiviles;
        listEstadosCiviles = persistenciaEstadosCiviles.consultarEstadosCiviles(em);
        return listEstadosCiviles;
    }

    @Override
    public BigInteger verificarVigenciasEstadosCiviles(BigInteger secuenciaEstadosCiviles) {
        BigInteger verificadorVigenciasEstadosCiviles = null;
        try {
            System.err.println("Secuencia verificarBorradoVigenciasEstadoCiviles  " + secuenciaEstadosCiviles);
            verificadorVigenciasEstadosCiviles = persistenciaEstadosCiviles.contadorVigenciasEstadosCiviles(em,secuenciaEstadosCiviles);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarEstadosCiviles verificarBorradoVigenciasEstadoCiviles ERROR :" + e);
        } finally {
            return verificadorVigenciasEstadosCiviles;
        }
    }
}
