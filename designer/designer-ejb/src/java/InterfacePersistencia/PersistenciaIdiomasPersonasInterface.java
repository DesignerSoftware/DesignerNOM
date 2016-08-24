/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.IdiomasPersonas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'IdiomasPersonas' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaIdiomasPersonasInterface {
    /**
     * Método encargado de insertar un IdiomaPersona en la base de datos.
     * @param idiomasPersonas IdiomaPersona que se quiere crear.
     */
    public void crear(EntityManager em, IdiomasPersonas idiomasPersonas);
    /**
     * Método encargado de modificar un IdiomaPersona de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param idiomasPersonas IdiomaPersona con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, IdiomasPersonas idiomasPersonas);
    /**
     * Método encargado de eliminar de la base de datos el IdiomaPersona que entra por parámetro.
     * @param idiomasPersonas IdiomaPersona que se quiere eliminar.
     */
    public void borrar(EntityManager em, IdiomasPersonas idiomasPersonas);
    /**
     * Método encargado de buscar los IdiomasPersonas de una persona.
     * @param secuenciaPersona Secuencia de la persona a la cual se le van a averiguar los idiomas.
     * @return Retorna una lista de IdiomasPersonas asociadas a una persona.
     */
    public List<IdiomasPersonas> idiomasPersona(EntityManager em, BigInteger secuenciaPersona);
    /**
     * Método encargado de buscar todos los IdiomasPersonas existentes en la base de datos.
     * @return Retorna una lista de IdiomasPersonas.
     */
    public List<IdiomasPersonas> totalIdiomasPersonas(EntityManager em);
}
