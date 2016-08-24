/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ClavesSap;
import InterfaceAdministrar.AdministrarClavesSapInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaClavesSapInterface;
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
public class AdministrarClavesSap implements AdministrarClavesSapInterface {

    @EJB
    PersistenciaClavesSapInterface persistenciaClavesSap;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void modificarClavesSap(List<ClavesSap> listaClavesSap) {
        for (int i = 0; i < listaClavesSap.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaClavesSap.editar(em, listaClavesSap.get(i));
        }
    }

    public void borrarClavesSap(List<ClavesSap> listaClavesSap) {
        for (int i = 0; i < listaClavesSap.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaClavesSap.borrar(em, listaClavesSap.get(i));
        }
    }

    public void crearClavesSap(List<ClavesSap> listaClavesSap) {
        for (int i = 0; i < listaClavesSap.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaClavesSap.crear(em, listaClavesSap.get(i));
        }
    }

    public List<ClavesSap> consultarClavesSap() {
        List<ClavesSap> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaClavesSap.consultarClavesSap(em);
        return listMotivosCambiosCargos;
    }

    public List<ClavesSap> consultarLOVClavesSap() {
        List<ClavesSap> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaClavesSap.consultarClavesSap(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public BigInteger contarClavesContablesCreditoClaveSap(BigInteger secuencia) {
        BigInteger retorno = persistenciaClavesSap.contarClavesContablesCreditoClaveSap(em, secuencia);
        return retorno;
    }

    @Override
    public BigInteger contarClavesContablesDebitoClaveSap(BigInteger secuencia) {
        BigInteger retorno = persistenciaClavesSap.contarClavesContablesDebitoClaveSap(em, secuencia);
        return retorno;
    }
}
