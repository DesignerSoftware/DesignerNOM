/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Tiposviajeros;
import Entidades.VigenciasViajeros;
import InterfaceAdministrar.AdministrarVigenciasViajerosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaTiposViajerosInterface;
import InterfacePersistencia.PersistenciaVigenciasViajerosInterface;
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
public class AdministrarVigenciasViajeros implements AdministrarVigenciasViajerosInterface {

    @EJB
    PersistenciaVigenciasViajerosInterface persistenciaVigenciasViajeros;
    @EJB
    PersistenciaTiposViajerosInterface persistenciaTiposViajeros;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
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
    public Empleados consultarEmpleado(BigInteger secuencia) {
        Empleados empleado;
        try {
            empleado = persistenciaEmpleados.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            empleado = null;
            return empleado;
        }
    }

    public void modificarVigenciasViajeros(List<VigenciasViajeros> listaVigenciasViajeros) {
        for (int i = 0; i < listaVigenciasViajeros.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaVigenciasViajeros.editar(em, listaVigenciasViajeros.get(i));
        }
    }

    public void borrarVigenciasViajeros(List<VigenciasViajeros> listaVigenciasViajeros) {
        for (int i = 0; i < listaVigenciasViajeros.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaVigenciasViajeros.borrar(em, listaVigenciasViajeros.get(i));
        }
    }

    public void crearVigenciasViajeros(List<VigenciasViajeros> listaVigenciasViajeros) {
        for (int i = 0; i < listaVigenciasViajeros.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaVigenciasViajeros.crear(em, listaVigenciasViajeros.get(i));
        }
    }

    @Override
    public List<VigenciasViajeros> consultarVigenciasViajeros() {
        List<VigenciasViajeros> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaVigenciasViajeros.consultarVigenciasViajeros(em);
        return listMotivosCambiosCargos;
    }

    public VigenciasViajeros consultarTipoViajero(BigInteger secVigenciasViajeros) {
        VigenciasViajeros vigennciaViajero;
        vigennciaViajero = persistenciaVigenciasViajeros.consultarTipoExamen(em, secVigenciasViajeros);
        return vigennciaViajero;
    }

    public List<VigenciasViajeros> consultarVigenciasViajerosPorEmpleado(BigInteger secVigenciasViajeros) {
        List<VigenciasViajeros> listVigenciasViajerosPorEmpleado;
        listVigenciasViajerosPorEmpleado = persistenciaVigenciasViajeros.consultarVigenciasViajerosPorEmpleado(em, secVigenciasViajeros);
        return listVigenciasViajerosPorEmpleado;
    }

    public List<Tiposviajeros> consultarLOVTiposViajeros() {
        List<Tiposviajeros> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaTiposViajeros.consultarTiposViajeros(em);
        return listMotivosCambiosCargos;
    }

}
