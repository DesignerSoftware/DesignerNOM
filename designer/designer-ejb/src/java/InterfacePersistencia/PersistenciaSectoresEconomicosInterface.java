/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.SectoresEconomicos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Clase Stateless 
 * Clase encargada de realizar operaciones sobre la tabla 'SectoresEconomicos'
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaSectoresEconomicosInterface {
    /**
     * Método encargado de insertar un SectorEconomico en la base de datos.
     * @param sectoresEconomicos SectorEconomico que se quiere crear.
     */
    public void crear(EntityManager em, SectoresEconomicos sectoresEconomicos);
    /**
     * Método encargado de modificar un SectorEconomico de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param sectoresEconomicos SectorEconomico con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, SectoresEconomicos sectoresEconomicos);
    /**
     * Método encargado de eliminar de la base de datos el SectorEconomico que entra por parámetro.
     * @param sectoresEconomicos SectorEconomico que se quiere eliminar.
     */
    public void borrar(EntityManager em, SectoresEconomicos sectoresEconomicos);
    /**
     * Método encargado de buscar todos los SectoresEconomicos existentes en la base de datos.
     * @return Retorna una lista de SectoresEconomicos.
     */
    public List<SectoresEconomicos> buscarSectoresEconomicos(EntityManager em);
    /**
     * Método encargado de buscar el SectorEconomico con la secuencia dada por parámetro.
     * @param secuencia Secuencia del SectorEconomico que se quiere encontrar.
     * @return Retorna el SectorEconomico identificado con la secuencia dada por parámetro.
     */
    public SectoresEconomicos buscarSectoresEconomicosSecuencia(EntityManager em, BigInteger secuencia);
}
