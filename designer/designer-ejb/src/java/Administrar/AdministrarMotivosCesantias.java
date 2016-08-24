/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosCesantias;
import InterfaceAdministrar.AdministrarMotivosCesantiasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosCesantiasInterface;
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
public class AdministrarMotivosCesantias implements AdministrarMotivosCesantiasInterface {

    @EJB
    PersistenciaMotivosCesantiasInterface persistenciaMotivosCensantias;
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
    public void modificarMotivosCesantias(List<MotivosCesantias> listaMotivosCesantias) {
        for (int i = 0; i < listaMotivosCesantias.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaMotivosCensantias.editar(em, listaMotivosCesantias.get(i));
        }
    }

    @Override
    public void borrarMotivosCesantias(List<MotivosCesantias> listaMotivosCesantias) {
        for (int i = 0; i < listaMotivosCesantias.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaMotivosCensantias.borrar(em, listaMotivosCesantias.get(i));
        }
    }

    @Override
    public void crearMotivosCesantias(List<MotivosCesantias> listaMotivosCesantias) {
        for (int i = 0; i < listaMotivosCesantias.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaMotivosCensantias.crear(em, listaMotivosCesantias.get(i));
        }
    }

    @Override
    public List<MotivosCesantias> consultarMotivosCesantias() {
        List<MotivosCesantias> listMotivosCensantias;
        listMotivosCensantias = persistenciaMotivosCensantias.buscarMotivosCesantias(em);
        return listMotivosCensantias;
    }

    @Override
    public MotivosCesantias consultarMotivoCesantia(BigInteger secMotivoPrestamo) {
        MotivosCesantias motivosCensantias;
        motivosCensantias = persistenciaMotivosCensantias.buscarMotivoCensantia(em, secMotivoPrestamo);
        return motivosCensantias;
    }

    @Override
    public BigInteger contarNovedadesSistemasMotivoCesantia(BigInteger secuenciaMotivosCesantias) {
        BigInteger verificarNovedadesSistema = null;
        try {
            verificarNovedadesSistema = persistenciaMotivosCensantias.contadorNovedadesSistema(em, secuenciaMotivosCesantias);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARMOTIVOSCESANTIAS verificarNovedadesSistema ERROR :" + e);
        } finally {
            return verificarNovedadesSistema;
        }
    }
}
