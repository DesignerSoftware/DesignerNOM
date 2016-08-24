/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Nodos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Nodos' 
 * de la base de datos.
 * @author Andres Pineda.
 */
public interface PersistenciaNodosInterface {
    /**
     * Método encargado de insertar un Nodo en la base de datos.
     * @param nodos Nodo que se quiere crear.
     */
    public void crear(EntityManager em, Nodos nodos);
    /**
     * Método encargado de modificar un Nodo de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param nodos Nodo con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Nodos nodos);
    /**
     * Método encargado de eliminar de la base de datos el Nodo que entra por parámetro.
     * @param nodos Nodo que se quiere eliminar.
     */
    public void borrar(EntityManager em, Nodos nodos);
    /**
     * Método encargado de buscar el Nodo con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Nodo que se quiere encontrar.
     * @return Retorna el Nodo identificado con la secuencia dada por parámetro.
     */
    public Nodos buscarNodoSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los Nodos existentes en la base de datos.
     * @return Retorna una lista de Nodos.
     */
    public List<Nodos> listNodos(EntityManager em);
    /**
     * Método encargado de buscar todos los nodos asociados a una historiaformula específica.
     * @param secuencia Secuencia de la historiaformula.
     * @return Retorna una lista de Nodos, ordenados por posición, cuya historiaformula tiene como secuencia 
     * el valor dado por parámetro.
     */
    public List<Nodos> buscarNodosPorSecuenciaHistoriaFormula(EntityManager em, BigInteger secuencia);

}
