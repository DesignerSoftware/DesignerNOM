/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.Empleados;
import Entidades.Estructuras;
import InterfaceAdministrar.AdministrarReingresarEmpleadoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarReingresarEmpleado implements AdministrarReingresarEmpleadoInterface{

    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;
    private List<Empleados> lovEmpleados;
    private List<Estructuras> lovEstructuras;
    private Date fechaDeRetiro;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    public Date obtenerFechaRetiro(BigInteger secuenciaEmpleado){
        fechaDeRetiro = persistenciaEmpleado.verificarFecha(em, secuenciaEmpleado);
        return fechaDeRetiro;
    }
    
    public void reintegrarEmpleado(BigInteger codigoEmpleado, BigInteger centroCosto,Date fechaReingreso, BigInteger empresa, Date fechaFinal) {
        persistenciaEmpleado.reingresarEmpleado(em, codigoEmpleado,centroCosto,fechaReingreso,empresa,fechaFinal);
    }
    
    public List<Empleados> listaEmpleados(){
        return persistenciaEmpleado.consultarEmpleadosReingreso(em);
    }
    
     public List<Estructuras> listaEstructuras(){
        return persistenciaEstructuras.consultarEstructurasReingreso(em);
    }
    
    
}
