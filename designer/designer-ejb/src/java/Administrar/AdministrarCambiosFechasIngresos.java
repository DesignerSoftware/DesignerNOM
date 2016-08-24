/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import InterfaceAdministrar.AdministrarCambiosFechasIngresosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigInteger;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarCambiosFechasIngresos implements AdministrarCambiosFechasIngresosInterface {

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    
    @EJB
    PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
    
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private Empleados empleado;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public Empleados buscarEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            empleado = null;
            return empleado;
        }
    }
    
    @Override
    public void cambiarFechaIngreso(BigInteger secuenciaEmpleado, Date fechaAntigua, Date fechaNueva){
        persistenciaEmpleado.cambiarFechaIngreso(em, secuenciaEmpleado, fechaAntigua, fechaNueva);
    }
}
