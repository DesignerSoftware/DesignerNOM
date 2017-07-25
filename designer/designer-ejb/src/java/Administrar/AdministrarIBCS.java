/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Ibcs;
import InterfaceAdministrar.AdministrarIBCSInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaIBCSInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarIBCS implements AdministrarIBCSInterface {

   private static Logger log = Logger.getLogger(AdministrarIBCS.class);

    @EJB
    PersistenciaIBCSInterface persistenciaIBCS;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    Empleados empleado;
    List<Ibcs> listIbcsPorEmpleado;
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
    public List<Ibcs> ibcsPorEmplelado(BigInteger secEmpleado) {
        try {
            listIbcsPorEmpleado = persistenciaIBCS.buscarIbcsPorEmpleado(em, secEmpleado);
        } catch (Exception e) {
            log.warn("Error en Administrar Ibcs ERROR " + e);
            listIbcsPorEmpleado = null;
        }
        return listIbcsPorEmpleado;
    }

    @Override
    public Empleados buscarEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleados.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            log.warn("AdministrarVigenciasFormasPagos buscarEmpleado error" + e);
            empleado = null;
            return empleado;
        }
    }
}
