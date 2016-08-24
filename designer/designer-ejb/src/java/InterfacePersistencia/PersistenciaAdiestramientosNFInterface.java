/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.AdiestramientosNF;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'AdiestramientosNF' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaAdiestramientosNFInterface {
    /**
     * Método encargado de consultar de la base de datos toda la información de los AdiestramientosNF.
     * @return Retorna una lista con todos los AdiestramientosNF recuperados.
     */
    public List<AdiestramientosNF> adiestramientosNF(EntityManager em); 
}
