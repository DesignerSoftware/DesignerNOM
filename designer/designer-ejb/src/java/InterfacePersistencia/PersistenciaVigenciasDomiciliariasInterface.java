/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasDomiciliarias;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasDomiciliarias' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasDomiciliariasInterface {
    /**
     * Método encargado de buscar las últimas VigenciasDomiciliarias registradas de una Persona específica.
     * @param secuencia Secuencia de la Persona.
     * @return Retorna una lista de las VigenciasDomiciliarias asociadas a una Persona.
     */
    public List<VigenciasDomiciliarias> visitasDomiciliariasPersona(EntityManager em, BigInteger secuencia) ;
}
