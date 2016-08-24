/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposInfAdicionales;
import InterfaceAdministrar.AdministrarGruposInfAdicionalesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposInfAdicionalesInterface;
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
public class AdministrarGruposInfAdicionales implements AdministrarGruposInfAdicionalesInterface {

    @EJB
    PersistenciaGruposInfAdicionalesInterface persistenciaGruposInfAdicionales;
    private GruposInfAdicionales grupoInfAdicionalSeleccionado;
    private GruposInfAdicionales gruposInfAdicionales;
    private List<GruposInfAdicionales> listGruposInfAdicionales;
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
    public void modificarGruposInfAdicionales(List<GruposInfAdicionales> listGruposInfAdicionales) {
        for (int i = 0; i < listGruposInfAdicionales.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaGruposInfAdicionales.editar(em, listGruposInfAdicionales.get(i));
        }
    }

    @Override
    public void borrarGruposInfAdicionales(List<GruposInfAdicionales> listGruposInfAdicionales) {
        for (int i = 0; i < listGruposInfAdicionales.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaGruposInfAdicionales.borrar(em, listGruposInfAdicionales.get(i));
        }
    }

    @Override
    public void crearGruposInfAdicionales(List<GruposInfAdicionales> listGruposInfAdicionales) {
        for (int i = 0; i < listGruposInfAdicionales.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaGruposInfAdicionales.crear(em, listGruposInfAdicionales.get(i));
        }
    }

    @Override
    public List<GruposInfAdicionales> consultarGruposInfAdicionales() {
        listGruposInfAdicionales = persistenciaGruposInfAdicionales.buscarGruposInfAdicionales(em);
        return listGruposInfAdicionales;
    }

    @Override
    public GruposInfAdicionales consultarGrupoInfAdicional(BigInteger secDeportes) {
        gruposInfAdicionales = persistenciaGruposInfAdicionales.buscarGrupoInfAdicional(em, secDeportes);
        return gruposInfAdicionales;
    }

    @Override
    public BigInteger verificarInformacionesAdicionales(BigInteger secuenciaGruposInfAdicionales) {
        BigInteger verificadorInformacionesAdicionales = null;
        try {
            System.err.println("Secuencia Grupo Inf Adicional : " + secuenciaGruposInfAdicionales);
            verificadorInformacionesAdicionales = persistenciaGruposInfAdicionales.contadorInformacionesAdicionales(em, secuenciaGruposInfAdicionales);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarEstadosCiviles VigenciasEstadoCiviles ERROR :" + e);
        } finally {
            return verificadorInformacionesAdicionales;
        }
    }
}
