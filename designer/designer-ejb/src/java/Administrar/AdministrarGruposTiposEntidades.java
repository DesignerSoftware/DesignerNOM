/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Grupostiposentidades;
import InterfaceAdministrar.AdministrarGruposTiposEntidadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposTiposEntidadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarGruposTiposEntidades implements AdministrarGruposTiposEntidadesInterface {

    @EJB
    PersistenciaGruposTiposEntidadesInterface persistenciaGruposTiposEntidades;

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
    public void modificarGruposTiposEntidades(List<Grupostiposentidades> listaGruposTiposEntidades) {
        for (int i = 0; i < listaGruposTiposEntidades.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaGruposTiposEntidades.editar(em, listaGruposTiposEntidades.get(i));
        }
    }

    @Override
    public void borrarGruposTiposEntidades(List<Grupostiposentidades> listaGruposTiposEntidades) {
        for (int i = 0; i < listaGruposTiposEntidades.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaGruposTiposEntidades.borrar(em, listaGruposTiposEntidades.get(i));
        }
    }

    @Override
    public void crearGruposTiposEntidades(List<Grupostiposentidades> listaGruposTiposEntidades) {
        for (int i = 0; i < listaGruposTiposEntidades.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaGruposTiposEntidades.crear(em, listaGruposTiposEntidades.get(i));
        }
    }

    @Override
    public List<Grupostiposentidades> consultarGruposTiposEntidades() {
        List<Grupostiposentidades> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaGruposTiposEntidades.consultarGruposTiposEntidades(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public Grupostiposentidades consultarGrupoTipoEntidad(BigInteger secGruposTiposEntidades) {
        Grupostiposentidades subCategoria;
        subCategoria = persistenciaGruposTiposEntidades.consultarGrupoTipoEntidad(em, secGruposTiposEntidades);
        return subCategoria;
    }

    @Override
    public BigInteger contarTSgruposTiposEntidadesTipoEntidad(BigInteger secGruposTiposEntidades) {
        BigInteger contarTSgruposTiposEntidadesTipoEntidad = null;

        try {
            return contarTSgruposTiposEntidadesTipoEntidad = persistenciaGruposTiposEntidades.contarTSgruposTiposEntidadesTipoEntidad(em, secGruposTiposEntidades);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarGruposTiposEntidades contarTSgruposTiposEntidadesTipoEntidad ERROR : " + e);
            return null;
        }
    }

    @Override
    public BigInteger contarTiposEntidadesGrupoTipoEntidad(BigInteger secGruposTiposEntidades) {
        BigInteger contarTiposEntidadesGrupoTipoEntidad = null;

        try {
            return contarTiposEntidadesGrupoTipoEntidad = persistenciaGruposTiposEntidades.contarTiposEntidadesGrupoTipoEntidad(em, secGruposTiposEntidades);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarGruposTiposEntidades contarTiposEntidadesGrupoTipoEntidad ERROR : " + e);
            return null;
        }
    }
}
