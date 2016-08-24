/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Rubrospresupuestales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'RubrosPresupuestales' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaRubrosPresupuestalesInterface {
    /**
     * Método encargado de insertar un RubroPresupuestal en la base de datos.
     * @param rubrospresupuestales RubroPresupuestal que se quiere crear.
     */
    public void crear(EntityManager em, Rubrospresupuestales rubrospresupuestales);
    /**
     * Método encargado de modificar un RubroPresupuestal de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param rubrospresupuestales RubroPresupuestal con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Rubrospresupuestales rubrospresupuestales);
    /**
     * Método encargado de eliminar de la base de datos el RubroPresupuestal que entra por parámetro.
     * @param rubrospresupuestales RubroPresupuestal que se quiere eliminar.
     */
    public void borrar(EntityManager em, Rubrospresupuestales rubrospresupuestales);    
    /**
     * Método encargado de buscar todos los RubrosPresupuestales existentes en la base de datos.
     * @return Retorna una lista de RubrosPresupuestales.
     */
    public List<Rubrospresupuestales> buscarRubros(EntityManager em);
    /**
     * Método encargado de buscar el RubroPresupuestal con la secuencia dada por parámetro.
     * @param secuencia Secuencia del RubroPresupuestal que se quiere encontrar.
     * @return Retorna el RubroPresupuestal identificado con la secuencia dada por parámetro.
     */
    public Rubrospresupuestales buscarRubrosSecuencia(EntityManager em, BigInteger secuencia);
}
