/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposPensionados;
import InterfaceAdministrar.AdministrarTiposPensionadosInterface;
import InterfacePersistencia.PersistenciaTiposPensionadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarTiposPensionados implements AdministrarTiposPensionadosInterface {

    @EJB
    PersistenciaTiposPensionadosInterface persistenciaTiposPensionados;
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
    public void modificarTiposPensionados(List<TiposPensionados> listaTiposPensionados) {
        for (int i = 0; i < listaTiposPensionados.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaTiposPensionados.editar(em, listaTiposPensionados.get(i));
        }
    }

    @Override
    public void borrarTiposPensionados(List<TiposPensionados> listaTiposPensionados) {
        for (int i = 0; i < listaTiposPensionados.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaTiposPensionados.borrar(em, listaTiposPensionados.get(i));
        }
    }

    @Override
    public void crearTiposPensionados(List<TiposPensionados> listaTiposPensionados) {
        for (int i = 0; i < listaTiposPensionados.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaTiposPensionados.crear(em, listaTiposPensionados.get(i));
        }
    }

    public List<TiposPensionados> consultarTiposPensionados() {
        List<TiposPensionados> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaTiposPensionados.consultarTiposPensionados(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public TiposPensionados consultarTipoPensionado(BigInteger secTiposPensionados) {
        TiposPensionados subCategoria;
        subCategoria = persistenciaTiposPensionados.consultarTipoPensionado(em, secTiposPensionados);
        return subCategoria;
    }

    @Override
    public BigInteger contarRetiradosTipoPensionado(BigInteger secTiposPensionados) {
        BigInteger contarRetiradosTipoPensionado = null;

        try {
            return contarRetiradosTipoPensionado = persistenciaTiposPensionados.contarPensionadosTipoPension(em, secTiposPensionados);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarTiposPensionados contarEscalafones ERROR : " + e);
            return null;
        }
    }
}
