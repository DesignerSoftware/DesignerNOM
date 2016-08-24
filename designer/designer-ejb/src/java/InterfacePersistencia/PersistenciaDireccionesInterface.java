/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Direcciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Direcciones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaDireccionesInterface {
    /**
     * Método encargado de insertar una Dirección en la base de datos.
     * @param telefonos Dirección que se quiere crear.
     */
    public void crear(EntityManager em,Direcciones telefonos);
    /**
     * Método encargado de modificar una Dirección de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param telefonos Dirección con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Direcciones telefonos);
    /**
     * Método encargado de eliminar de la base de datos la Dirección que entra por parámetro.
     * @param telefonos Dirección que se quiere eliminar.
     */
    public void borrar(EntityManager em,Direcciones telefonos);
    /**
     * Método encargado de buscar la Dirección con la secuencia dada por parámetro.
     * @param secuencia Identificador único de la Dirección que se quiere encontrar.
     * @return Retorna la Dirección identificada con la secuencia dada por parámetro.
     */
    public Direcciones buscarDireccion(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar todas las Direcciones existentes en la base de datos.
     * @return Retorna una lista de Direcciones.
     */
    public List<Direcciones> buscarDirecciones(EntityManager em);
    /**
     * Método encargado de buscar las Direcciones con ultima fecha vigente, existentes en la base de datos y asociadas a una persona.
     * @param secuenciaPersona Secuencia de la persona de la que se quiere saber la(s) dirección(es)
     * @return Retorna una lista de Direcciones
     */
    public List<Direcciones> direccionPersona(EntityManager em,BigInteger secuenciaPersona);
    /**
     * Método encargado de buscar las Direcciones asociadas a una persona, existentes en la base de datos.
     * @param secuenciaPersona Secuencia de la persona de la que se quiere saber la(s) dirección(es)
     * @return Retorna una lista de Direcciones
     */
    public List<Direcciones> direccionesPersona(EntityManager em,BigInteger secuenciaPersona);
}
