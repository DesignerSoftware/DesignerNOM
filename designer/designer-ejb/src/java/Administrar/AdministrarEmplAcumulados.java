/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Empleados;
import Entidades.VWAcumulados;
import InterfaceAdministrar.AdministrarEmplAcumuladosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaVWAcumuladosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'EmplAcumulados'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarEmplAcumulados implements AdministrarEmplAcumuladosInterface {

   private static Logger log = Logger.getLogger(AdministrarEmplAcumulados.class);

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWAcumulados'.
     */
    @EJB
    PersistenciaVWAcumuladosInterface persistenciaVWAcumulados;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEmpleados'.
     */
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

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<VWAcumulados> consultarVWAcumuladosEmpleado(BigInteger secEmpleado) {
        try {
            return persistenciaVWAcumulados.buscarAcumuladosPorEmpleado(em,secEmpleado);
        } catch (Exception e) {
            log.error("ERROR EN ADMINISTRAR EMPLACUMULADOS ERROR " + e);
            return null;
        }
    }

    @Override
    public Empleados consultarEmpleado(BigInteger secEmplado) {
        try {
            return persistenciaEmpleados.buscarEmpleadoSecuencia(em,secEmplado);
        } catch (Exception e) {
            log.error("ERROR Administrar emplAcumulados ERROR : " + e);
            return null;
        }
    }
}
