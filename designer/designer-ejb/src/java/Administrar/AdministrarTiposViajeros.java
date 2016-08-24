/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Tiposviajeros;
import InterfaceAdministrar.AdministrarTiposViajerosInterface;
import InterfacePersistencia.PersistenciaTiposViajerosInterface;
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
public class AdministrarTiposViajeros implements AdministrarTiposViajerosInterface {

    @EJB
    PersistenciaTiposViajerosInterface persistenciaTiposViajeros;
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
    public void modificarTiposViajeros(List<Tiposviajeros> listaTiposViajeros) {
        for (int i = 0; i < listaTiposViajeros.size(); i++) {
            System.out.println("Administrar Modificando...");
            System.out.println("Nombre " + listaTiposViajeros.get(i).getNombre() + " Codigo " + listaTiposViajeros.get(i).getCodigo());
            persistenciaTiposViajeros.editar(em, listaTiposViajeros.get(i));
        }
    }

    @Override
    public void borrarTiposViajeros(List<Tiposviajeros> listaTiposViajeros) {
        for (int i = 0; i < listaTiposViajeros.size(); i++) {
            System.out.println("Administrar Borrando...");
            System.out.println("Nombre " + listaTiposViajeros.get(i).getNombre() + " Codigo " + listaTiposViajeros.get(i).getCodigo());
            persistenciaTiposViajeros.borrar(em, listaTiposViajeros.get(i));
        }
    }

    @Override
    public void crearTiposViajeros(List<Tiposviajeros> listaTiposViajeros) {
        for (int i = 0; i < listaTiposViajeros.size(); i++) {
            System.out.println("Administrar Creando...");
            System.out.println("Nombre " + listaTiposViajeros.get(i).getNombre() + " Codigo " + listaTiposViajeros.get(i).getCodigo());
            persistenciaTiposViajeros.crear(em, listaTiposViajeros.get(i));
        }
    }

    @Override
    public List<Tiposviajeros> consultarTiposViajeros() {
        List<Tiposviajeros> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaTiposViajeros.consultarTiposViajeros(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public Tiposviajeros consultarTipoViajero(BigInteger secTiposViajeros) {
        Tiposviajeros subCategoria;
        subCategoria = persistenciaTiposViajeros.consultarSubCategoria(em, secTiposViajeros);
        return subCategoria;
    }

    @Override
    public BigInteger contarTiposLegalizaciones(BigInteger secTiposViajeros) {
        BigInteger contarTiposLegalizaciones = null;

        try {
            return contarTiposLegalizaciones = persistenciaTiposViajeros.contarTiposLegalizacionesTipoViajero(em, secTiposViajeros);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarTiposViajeros contarEscalafones ERROR : " + e);
            return null;
        }
    }

    @Override
    public BigInteger contarVigenciasViajeros(BigInteger secTiposViajeros) {
        BigInteger contarVigenciasViajeros = null;

        try {
            return contarVigenciasViajeros = persistenciaTiposViajeros.contarVigenciasViajerosTipoViajero(em, secTiposViajeros);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarTiposViajeros contarEscalafones ERROR : " + e);
            return null;
        }
    }

}
