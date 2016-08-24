
package InterfacePersistencia;

import Entidades.EersCabeceras;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaEersCabecerasInterface {

    public void crear(EntityManager em, EersCabeceras eersCabeceras);

    public void editar(EntityManager em, EersCabeceras eersCabeceras);

    public void borrar(EntityManager em, EersCabeceras eersCabeceras);

    public List<EersCabeceras> buscarEersCabecerasTotales(EntityManager em);
    
    public List<EersCabeceras> buscarEersCabecerasTotalesPorEmpleado(EntityManager em, BigInteger secuencia);

}
