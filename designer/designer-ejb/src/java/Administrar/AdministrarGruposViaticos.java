/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposViaticos;
import InterfaceAdministrar.AdministrarGruposViaticosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposViaticosInterface;
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
public class AdministrarGruposViaticos implements AdministrarGruposViaticosInterface {

    @EJB
    PersistenciaGruposViaticosInterface persistenciaGruposViaticos;
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
    public void modificarGruposViaticos(List<GruposViaticos> listGruposViaticos) {
        for (int i = 0; i < listGruposViaticos.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaGruposViaticos.editar(em, listGruposViaticos.get(i));
        }
    }

    @Override
    public void borrarGruposViaticos(List<GruposViaticos> listGruposViaticos) {
        for (int i = 0; i < listGruposViaticos.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaGruposViaticos.borrar(em, listGruposViaticos.get(i));
        }
    }

    @Override
    public void crearGruposViaticos(List<GruposViaticos> listGruposViaticos) {
        for (int i = 0; i < listGruposViaticos.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaGruposViaticos.crear(em, listGruposViaticos.get(i));
        }
    }

    @Override
    public List<GruposViaticos> consultarGruposViaticos() {
        List<GruposViaticos> listGruposViaticos;
        listGruposViaticos = persistenciaGruposViaticos.buscarGruposViaticos(em);
        return listGruposViaticos;
    }

    @Override
    public GruposViaticos consultarGrupoViatico(BigInteger secGruposViaticos) {
        GruposViaticos evalGruposViaticos;
        evalGruposViaticos = persistenciaGruposViaticos.buscarGrupoViatico(em, secGruposViaticos);
        return evalGruposViaticos;
    }

    @Override
    public BigInteger verificarCargos(BigInteger secuenciaCargos) {
        BigInteger verificadorCargos = null;
        try {
            System.err.println("Secuencia Borrado  Cargos" + secuenciaCargos);
            verificadorCargos = persistenciaGruposViaticos.contadorCargos(em, secuenciaCargos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarGruposViativos verificarBorradoCargos ERROR :" + e);
        } finally {
            return verificadorCargos;
        }
    }

    @Override
    public BigInteger verificarPlantas(BigInteger secuenciaCargos) {
        BigInteger verificadorPlantas = null;
        try {
            System.err.println("Secuencia Borrado  Plantas" + secuenciaCargos);
            verificadorPlantas = persistenciaGruposViaticos.contadorPlantas(em, secuenciaCargos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarGruposViativos verificarBorradoPlantas ERROR :" + e);
        } finally {
            return verificadorPlantas;
        }
    }

    @Override
    public BigInteger verificarTablasViaticos(BigInteger secuenciaCargos) {
        BigInteger verificadorTablasViaticos = null;
        try {
            System.err.println("Secuencia Borrado  Tablas Viaticos" + secuenciaCargos);
            verificadorTablasViaticos = persistenciaGruposViaticos.contadorTablasViaticos(em, secuenciaCargos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarGruposViativos verificarTablasViaticos ERROR :" + e);
        } finally {
            return verificadorTablasViaticos;
        }
    }

    @Override
    public BigInteger verificarEersViaticos(BigInteger secuenciaCargos) {
        BigInteger verificadorErsViaticos = null;
        try {
            System.err.println("Secuencia Borrado  Tablas ErsViaticos" + secuenciaCargos);
            verificadorErsViaticos = persistenciaGruposViaticos.contadorEersViaticos(em, secuenciaCargos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarGruposViativos verificarEersViaticos ERROR :" + e);
        } finally {
            return verificadorErsViaticos;
        }
    }
}
