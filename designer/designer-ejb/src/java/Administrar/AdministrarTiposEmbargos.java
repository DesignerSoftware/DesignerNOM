/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposEmbargosInterface;
import Entidades.TiposEmbargos;
import InterfacePersistencia.PersistenciaTiposEmbargosInterface;
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
public class AdministrarTiposEmbargos implements AdministrarTiposEmbargosInterface {

    @EJB
    PersistenciaTiposEmbargosInterface persistenciaTiposEmbargos;
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
    public void modificarTiposPrestamos(List<TiposEmbargos> listaTiposEmbargos) {
        for (int i = 0; i < listaTiposEmbargos.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposEmbargos.editar(em, listaTiposEmbargos.get(i));
        }
    }

    @Override
    public void borrarTiposPrestamos(List<TiposEmbargos> listaTiposEmbargos) {
        for (int i = 0; i < listaTiposEmbargos.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaTiposEmbargos.borrar(em, listaTiposEmbargos.get(i));
        }
    }

    @Override
    public void crearTiposPrestamos(List<TiposEmbargos> listaTiposEmbargos) {
        for (int i = 0; i < listaTiposEmbargos.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaTiposEmbargos.crear(em, listaTiposEmbargos.get(i));
        }
    }

    @Override
    public List<TiposEmbargos> consultarTiposPrestamos() {
        List<TiposEmbargos> listTiposEmbargos;
        listTiposEmbargos = persistenciaTiposEmbargos.buscarTiposEmbargos(em);
        return listTiposEmbargos;
    }

    @Override
    public TiposEmbargos consultarTipoPrestamo(BigInteger secMotivoPrestamo) {
        TiposEmbargos tiposEmbargos;
        tiposEmbargos = persistenciaTiposEmbargos.buscarTipoEmbargo(em, secMotivoPrestamo);
        return tiposEmbargos;
    }

    @Override
    public BigInteger contarDiasLaboralesTipoEmbargo(BigInteger secuenciaTiposDias) {
        BigInteger verificarBorradoEerPrestamos = null;
        try {
            verificarBorradoEerPrestamos = persistenciaTiposEmbargos.contadorEerPrestamos(em, secuenciaTiposDias);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARTIPOSEMBARGOS VERIFICARDIASLABORALES ERROR :" + e);
        } finally {
            return verificarBorradoEerPrestamos;
        }
    }

    @Override
    public BigInteger contarExtrasRecargosTipoEmbargo(BigInteger secuenciaTiposDias) {
        BigInteger verificarBorradoFormasDtos = null;
        try {
            verificarBorradoFormasDtos = persistenciaTiposEmbargos.contadorFormasDtos(em, secuenciaTiposDias);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARTIPOSEMBARGOS VERIFICAREXTRASRECARGOS ERROR :" + e);
        } finally {
            return verificarBorradoFormasDtos;
        }
    }
}
