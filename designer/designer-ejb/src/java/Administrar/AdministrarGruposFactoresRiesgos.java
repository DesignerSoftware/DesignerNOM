/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.GruposFactoresRiesgos;
import InterfaceAdministrar.AdministrarGruposFactoresRiesgosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposFactoresRiesgosInterface;
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
public class AdministrarGruposFactoresRiesgos implements AdministrarGruposFactoresRiesgosInterface {

     @EJB
    PersistenciaGruposFactoresRiesgosInterface persistenciaGruposFactoresRiesgos;
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
    public void modificarGruposFactoresRiesgos(List<GruposFactoresRiesgos> listaGruposFactoresRiesgos) {
        for (int i = 0; i < listaGruposFactoresRiesgos.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaGruposFactoresRiesgos.editar(em, listaGruposFactoresRiesgos.get(i));
        }
    }

    public void borrarGruposFactoresRiesgos(List<GruposFactoresRiesgos> listaGruposFactoresRiesgos) {
        for (int i = 0; i < listaGruposFactoresRiesgos.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaGruposFactoresRiesgos.borrar(em, listaGruposFactoresRiesgos.get(i));
        }
    }

    public void crearGruposFactoresRiesgos(List<GruposFactoresRiesgos> listaGruposFactoresRiesgos) {
        for (int i = 0; i < listaGruposFactoresRiesgos.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaGruposFactoresRiesgos.crear(em, listaGruposFactoresRiesgos.get(i));
        }
    }

     @Override
    public List<GruposFactoresRiesgos> consultarGruposFactoresRiesgos() {
        List<GruposFactoresRiesgos> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaGruposFactoresRiesgos.consultarGruposFactoresRiesgos(em);
        return listMotivosCambiosCargos;
    }

    public GruposFactoresRiesgos consultarGrupoFactorRiesgo(BigInteger secGruposFactoresRiesgos) {
        GruposFactoresRiesgos subCategoria;
        subCategoria = persistenciaGruposFactoresRiesgos.consultarGrupoFactorRiesgo(em, secGruposFactoresRiesgos);
        return subCategoria;
    }

    public BigInteger contarFactoresRiesgoGrupoFactorRiesgo(BigInteger secGruposFactoresRiesgos) {
        BigInteger contarFactoresRiesgoGrupoFactorRiesgo = null;

        try {
            return contarFactoresRiesgoGrupoFactorRiesgo = persistenciaGruposFactoresRiesgos.contarFactoresRiesgoGrupoFactorRiesgo(em, secGruposFactoresRiesgos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarGruposFactoresRiesgos contarFactoresRiesgoGrupoFactorRiesgo ERROR : " + e);
            return null;
        }
    }

    public BigInteger contarSoIndicadoresGrupoFactorRiesgo(BigInteger secGruposFactoresRiesgos) {
        BigInteger contarSoIndicadoresGrupoFactorRiesgo = null;

        try {
            return contarSoIndicadoresGrupoFactorRiesgo = persistenciaGruposFactoresRiesgos.contarSoIndicadoresGrupoFactorRiesgo(em, secGruposFactoresRiesgos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarGruposFactoresRiesgos contarSoIndicadoresGrupoFactorRiesgo ERROR : " + e);
            return null;
        }
    }

    public BigInteger contarSoProActividadesGrupoFactorRiesgo(BigInteger secGruposFactoresRiesgos) {
        BigInteger contarSoProActividadesGrupoFactorRiesgo = null;

        try {
            return contarSoProActividadesGrupoFactorRiesgo = persistenciaGruposFactoresRiesgos.contarSoProActividadesGrupoFactorRiesgo(em, secGruposFactoresRiesgos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarGruposFactoresRiesgos contarSoProActividadesGrupoFactorRiesgo ERROR : " + e);
            return null;
        }
    }
}
