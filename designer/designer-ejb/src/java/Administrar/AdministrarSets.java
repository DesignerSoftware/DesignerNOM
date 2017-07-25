
package Administrar;

import Entidades.Empleados;
import Entidades.Sets;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarSetsInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaSetsInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;


/**
 *
 * @author AndresPineda
 */

@Stateful
public class AdministrarSets implements AdministrarSetsInterface {

   private static Logger log = Logger.getLogger(AdministrarSets.class);
    
    @EJB
    PersistenciaSetsInterface persistenciaSets;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    	/**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;    
    
    List<Sets> setsLista;
    Sets sets;
    Empleados empleado;
    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public List<Sets> SetsEmpleado(BigInteger secEmpleado) {
        try {
            setsLista = persistenciaSets.buscarSetsEmpleado(em, secEmpleado);
        } catch (Exception e) {
            log.warn("Error en Administrar Vigencias Contratos (VigenciasContratosEmpleado)");
            setsLista = null;
        }
        return setsLista;
    }


    @Override
    public void modificarSets(List<Sets> listSetsModificadas) {
        for (int i = 0; i < listSetsModificadas.size(); i++) {
            log.warn("Modificando...");
            sets = listSetsModificadas.get(i);
            persistenciaSets.editar(em, sets);
        }
    }


    @Override
    public void borrarSets(Sets sets) {
        persistenciaSets.borrar(em, sets);
    }


    @Override
    public void crearSets(Sets sets) {
        persistenciaSets.crear(em, sets);
    }


    @Override
    public Empleados buscarEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleados.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            empleado = null;
            return empleado;
        }
    }
    
    @Remove
    @Override
    public void salir() {
        sets = null;
    }
}
