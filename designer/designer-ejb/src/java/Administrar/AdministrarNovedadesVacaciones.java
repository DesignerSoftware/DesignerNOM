/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import InterfaceAdministrar.AdministrarNovedadesVacacionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarNovedadesVacaciones implements AdministrarNovedadesVacacionesInterface {

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
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
    public List<Empleados> empleadosVacaciones() {
        return persistenciaEmpleados.empleadosVacaciones(em);
    }
    
    @Override
    public Date obtenerFechaContratacionEmpleado(BigInteger secEmpleado) {
        try {
            Date ultimaFecha = persistenciaVigenciasTiposContratos.fechaFinalContratacionVacaciones(em, secEmpleado);
            return ultimaFecha;
        } catch (Exception e) {
            System.out.println("Error obtenerFechaContratacionEmpleado Admi : " + e.toString());
            return null;
        }
    }
}
