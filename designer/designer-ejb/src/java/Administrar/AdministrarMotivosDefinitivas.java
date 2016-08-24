/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosDefinitivas;
import InterfaceAdministrar.AdministrarMotivosDefinitivasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosDefinitivasInterface;
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
public class AdministrarMotivosDefinitivas implements AdministrarMotivosDefinitivasInterface {

    @EJB
    PersistenciaMotivosDefinitivasInterface persistenciaMotivosDefinitivas;
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
    
    public void modificarMotivosDefinitivas(List<MotivosDefinitivas> listaMotivosDefinitivas) {
        for (int i = 0; i < listaMotivosDefinitivas.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaMotivosDefinitivas.editar(em, listaMotivosDefinitivas.get(i));
        }
    }

    public void borrarMotivosDefinitivas(List<MotivosDefinitivas> listaMotivosDefinitivas) {
        for (int i = 0; i < listaMotivosDefinitivas.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaMotivosDefinitivas.borrar(em, listaMotivosDefinitivas.get(i));
        }
    }

    public void crearMotivosDefinitivas(List<MotivosDefinitivas> listaMotivosDefinitivas) {
        for (int i = 0; i < listaMotivosDefinitivas.size(); i++) {
            System.out.println("Administrar Crenando...");
            persistenciaMotivosDefinitivas.crear(em, listaMotivosDefinitivas.get(i));
        }
    }

    public List<MotivosDefinitivas> mostrarMotivosDefinitivas() {
        List<MotivosDefinitivas> listMotivosDefinitivas;
        listMotivosDefinitivas = persistenciaMotivosDefinitivas.buscarMotivosDefinitivas(em);
        return listMotivosDefinitivas;
    }

    public MotivosDefinitivas mostrarMotivoDefinitiva(BigInteger secMotivoPrestamo) {
        MotivosDefinitivas motivosDefinitivas;
        motivosDefinitivas = persistenciaMotivosDefinitivas.buscarMotivoDefinitiva(em, secMotivoPrestamo);
        return motivosDefinitivas;
    }

    public BigInteger contarNovedadesSistemasMotivoDefinitiva(BigInteger secuenciaMotivosCesantias) {
        BigInteger verificarNovedadesSistema = null;
        try {
            verificarNovedadesSistema = persistenciaMotivosDefinitivas.contadorNovedadesSistema(em, secuenciaMotivosCesantias);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARMOTIVOSDEFINITIVAS VERIFICARNOVEDADESSISTEMA ERROR :" + e);
        } finally {
            return verificarNovedadesSistema;
        }
    }

    public BigInteger contarParametrosCambiosMasivosMotivoDefinitiva(BigInteger secuenciaMotivosCesantias) {
        BigInteger verificarParametrosCambiosMasivos = null;
        try {
            verificarParametrosCambiosMasivos = persistenciaMotivosDefinitivas.contadorParametrosCambiosMasivos(em, secuenciaMotivosCesantias);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARMOTIVOSDEFINITIVAS VERIFICARPARAMETROSCAMBIOSMASIVOS ERROR :" + e);
        } finally {
            return verificarParametrosCambiosMasivos;
        }
    }

}
