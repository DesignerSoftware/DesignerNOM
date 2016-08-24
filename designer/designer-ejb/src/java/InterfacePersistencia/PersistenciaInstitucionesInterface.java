/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Instituciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Instituciones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaInstitucionesInterface {
    
    /**
     * Método encargado de insertar una Institución en la base de datos.
     * @param instituciones Institución que se quiere crear.
     */
    public void crear(EntityManager em, Instituciones instituciones);
    /**
     * Método encargado de modificar una Institución de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param instituciones Institución con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Instituciones instituciones);
    /**
     * Método encargado de eliminar de la base de datos la Institución que entra por parámetro.
     * @param instituciones Institución que se quiere eliminar.
     */
    public void borrar(EntityManager em, Instituciones instituciones);
    /**
     * Método encargado de buscar la Institución con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la Institución que se quiere encontrar.
     * @return Retorna la Institución identificado con la secuencia dada por parámetro.
     */
    public Instituciones buscarInstitucion(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todas las Instituciones existentes en la base de datos.
     * @return Retorna una lista de Instituciones.
     */
    public List<Instituciones> instituciones(EntityManager em);

}
