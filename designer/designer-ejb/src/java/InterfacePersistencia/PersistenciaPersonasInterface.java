/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Personas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Personas' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaPersonasInterface {

    /**
     * Método encargado de insertar una Persona en la base de datos.
     *
     * @param em
     * @param personas Persona que se quiere crear.
     */
    public void crear(EntityManager em, Personas personas);

    /**
     * Método encargado de modificar una Persona de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param em
     * @param personas Persona con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Personas personas);

    /**
     * Método encargado de eliminar de la base de datos la Persona que entra por
     * parámetro.
     *
     * @param em
     * @param personas Persona que se quiere eliminar.
     */
    public void borrar(EntityManager em, Personas personas);

    /**
     * Método encargado de buscar la Persona con la secuencia dada por
     * parámetro.
     *
     * @param em
     * @param secuencia Secuencia de la Persona que se quiere encontrar.
     * @return Retorna la Persona identificada con la secuencia dada por
     * parámetro.
     */
    public Personas buscarPersona(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de buscar todas las Personas existentes en la base de
     * datos.
     *
     * @param em
     * @return Retorna una lista de Personas.
     */
    public List<Personas> consultarPersonas(EntityManager em);

    /**
     * Metodo encargado de actualizar en la base de datos si una persona tiene
     * foto o no.
     *
     * @param em
     * @param identificacion Número de documento de la persona a la que se le
     * pone una foto.
     */
    public void actualizarFotoPersona(EntityManager em, BigInteger identificacion);

    /**
     * Método encargado de buscar una persona dada su identificación, para saber
     * cúal es el pathFoto de un empleado.
     *
     * @param em
     * @param identificacion Número del documento de la persona de la que se
     * quiere la información.
     * @return Retorna la persona con el NumeroDocumento igual al dado por
     * parametro.
     */
    public Personas buscarFotoPersona(EntityManager em, BigInteger identificacion);

    /**
     * Método encargado de buscar la Persona con la secuencia dada por
     * parámetro.
     *
     * @param em
     * @param secuencia Secuencia de la Persona que se quiere encontrar.
     * @return Retorna la Persona identificada con la secuencia dada por
     * parámetro.
     */
    public Personas buscarPersonaSecuencia(EntityManager em, BigInteger secuencia);
    
    public Personas buscarPersonaPorEmpleado(EntityManager em, BigInteger secEmpleado);

    public Personas buscarPersonaPorNumeroDocumento(EntityManager em, BigInteger numeroDocumento);

    public Personas obtenerUltimaPersonaAlmacenada(EntityManager em,BigInteger documento);

}
