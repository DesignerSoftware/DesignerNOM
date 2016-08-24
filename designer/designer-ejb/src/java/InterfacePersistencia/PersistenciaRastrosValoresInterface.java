/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.RastrosValores;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'RastrosValores' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaRastrosValoresInterface {
    /**
     * Método encargado de buscar los RastrosValores de un Rastro específico.
     * @param secRastro Secuencia del Rastro al que se le quieren encontrar los RastrosValores.
     * @return Retorna una lista de RastrosValores.
     */
    public List<RastrosValores> rastroValores(EntityManager em, BigInteger secRastro);
}
