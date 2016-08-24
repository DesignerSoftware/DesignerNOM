/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.AdiestramientosF;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'AdiestramientosF' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaAdiestramientosFInterface {
    /**
     * Método encargado de consultar de la base de datos toda la información de los AdiestramientosF.
     * @return Retorna una lista con todos los AdiestramientosF recuperados.
     */
    public List<AdiestramientosF> adiestramientosF(EntityManager em); 
}
