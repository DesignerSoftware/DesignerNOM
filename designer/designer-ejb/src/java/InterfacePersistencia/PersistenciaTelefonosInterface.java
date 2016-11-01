/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Telefonos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Teléfonos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaTelefonosInterface {
    /**
     * Método encargado de insertar un Teléfono en la base de datos.
     * @param telefonos Teléfono que se quiere crear.
     */
    public boolean crear(EntityManager em, Telefonos telefonos);
    /**
     * Método encargado de modificar un Teléfono de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param telefonos Teléfono con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Telefonos telefonos);
    /**
     * Método encargado de eliminar de la base de datos el Teléfono que entra por parámetro.
     * @param telefonos Teléfono que se quiere eliminar.
     */
    public void borrar(EntityManager em, Telefonos telefonos);
    /**
     * Método encargado de buscar el Teléfono con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Teléfono que se quiere encontrar.
     * @return Retorna el Teléfono identificado con la secuencia dada por parámetro.
     */
    public Telefonos buscarTelefono(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los Teléfonos existentes en la base de datos.
     * @return Retorna una lista de Teléfonos.
     */
    public List<Telefonos> buscarTelefonos(EntityManager em);
    /**
     * Método encargado de buscar los Teléfonos de una persona.
     * @param secuenciaPersona Secuencia de la persona.
     * @return Retorna una lista de Teléfonos asociados a una persona específica.
     */
    public List<Telefonos> telefonosPersona(EntityManager em, BigInteger secuenciaPersona);
    
    public Telefonos telefonoActual(EntityManager em, BigInteger secuenciaPersona);

    public String consultarUltimoTelefono(EntityManager em, BigInteger secuenciaPersona);
    
}
