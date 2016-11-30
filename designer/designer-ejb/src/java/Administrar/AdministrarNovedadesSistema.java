/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.MotivosRetiros;
import Entidades.MotivosDefinitivas;
import Entidades.NovedadesSistema;
import Entidades.Vacaciones;
import InterfaceAdministrar.AdministrarNovedadesSistemaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosDefinitivasInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import InterfacePersistencia.PersistenciaNovedadesSistemaInterface;
import InterfacePersistencia.PersistenciaVacacionesInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarNovedadesSistema implements AdministrarNovedadesSistemaInterface{
    
    @EJB
    PersistenciaNovedadesSistemaInterface persistenciaNovedades;
    @EJB
    PersistenciaMotivosDefinitivasInterface persistenciaMotivos;
    @EJB
    PersistenciaMotivosRetirosInterface persistenciaRetiros;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaVacacionesInterface persistenciaVacaciones;
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
    
    //Trae las novedades del empleado cuya secuencia se envía como parametro//
    @Override
    public List<NovedadesSistema> novedadesEmpleado(BigInteger secuenciaEmpleado) {
        try {
            return persistenciaNovedades.novedadesEmpleado(em, secuenciaEmpleado);
        } catch (Exception e) {
            System.err.println("Error AdministrarNovedadesEmpleados.novedadesEmpleado" + e);
            return null;
        }
    }
    
    @Override
    public void borrarNovedades(NovedadesSistema novedades) {
        persistenciaNovedades.borrar(em, novedades);
    }

    @Override
    public void crearNovedades(NovedadesSistema novedades) {
        persistenciaNovedades.crear(em, novedades);
    }
    
    

    @Override
    public void modificarNovedades(NovedadesSistema novedades) {
            persistenciaNovedades.editar(em, novedades);
        
    }
    
    @Override
    public List<Empleados> buscarEmpleados(){
        return persistenciaEmpleados.empleadosDefinitiva(em);
    }
    
    public List<Empleados> lovEmpleados(){
        return persistenciaEmpleados.empleadosDefinitiva(em);
    }
    
    @Override
    public List<MotivosDefinitivas> lovMotivos(){
        return persistenciaMotivos.buscarMotivosDefinitivas(em);
    }
    
    @Override
    public List<MotivosRetiros> lovRetiros(){
        return persistenciaRetiros.consultarMotivosRetiros(em);
    }
    
    @Override
    public List<NovedadesSistema> vacacionesEmpleado(BigInteger secuenciaEmpleado){
        return persistenciaNovedades.novedadesEmpleadoVacaciones(em, secuenciaEmpleado);
    }
     
   
    @Override
    public List<NovedadesSistema> cesantiasEmpleado(BigInteger secuenciaEmpleado){
        return persistenciaNovedades.novedadesEmpleadoCesantias(em, secuenciaEmpleado);
    }
     
    
    @Override
    public List<Vacaciones> periodosEmpleado(BigInteger secuenciaEmpleado){
        return persistenciaVacaciones.periodoVacaciones(em, secuenciaEmpleado);
    }

    @Override
    public BigDecimal valorCesantias(BigInteger secuenciaEmpleado) {
        return persistenciaNovedades.valorCesantias(em, secuenciaEmpleado);
    }

    @Override
        public BigDecimal valorIntCesantias(BigInteger secuenciaEmpleado) {
        return persistenciaNovedades.valorIntCesantias(em, secuenciaEmpleado);
    }

}
