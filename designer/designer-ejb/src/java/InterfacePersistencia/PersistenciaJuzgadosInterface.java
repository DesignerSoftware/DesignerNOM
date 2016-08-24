/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Juzgados;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Juzgados' 
 * de la base de datos.
 * @author betelgeuse
 */

public interface PersistenciaJuzgadosInterface {
    /**
     * Método encargado de insertar un Juzgado en la base de datos.
     * @param juzgados Juzgado que se quiere crear.
     */
    public void crear(EntityManager em, Juzgados juzgados);
    /**
     * Método encargado de modificar un Juzgado de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param juzgados Juzgado con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Juzgados juzgados) ;
    /**
     * Método encargado de eliminar de la base de datos el Juzgado que entra por parámetro.
     * @param juzgados Juzgado que se quiere eliminar.
     */
    public void borrar(EntityManager em, Juzgados juzgados) ;
    /**
     * Método encargado de buscar el Juzgado con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Juzgado que se quiere encontrar.
     * @return Retorna el Juzgado identificado con la secuencia dada por parámetro.
     */
    public Juzgados buscarJuzgado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los Juzgados existentes en la base de datos.
     * @return Retorna una lista de Juzgados.
     */
    public List<Juzgados> buscarJuzgados(EntityManager em) ;
    /**
     * Metodo encargado de buscar los juzgados de una ciudad específica.
     * @param secuencia Secuencia de la ciudad.
     * @return Retorna una lista de Juzgados, ordenados por código, los cuales tienen relación con la ciudad cuya secuencia
     * coincide con el valor dado por parámetro.
     */
    public List<Juzgados> buscarJuzgadosPorCiudad(EntityManager em, BigInteger secuencia) ;
    /**
     * Método encargado de contar la cantidad de Eersprestamos asociados a un Juzgado específico.
     * @param secuencia Secuencia del Juzgado.
     * @return Retorna la cantidad de Eersprestamos cuyo Juzgado tiene como secuencia el valor dado por parámetro.
     */
    public BigInteger contadorEerPrestamos(EntityManager em, BigInteger secuencia) ;
}
