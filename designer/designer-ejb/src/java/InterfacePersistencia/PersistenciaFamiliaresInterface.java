/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Familiares;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Familiares' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaFamiliaresInterface {
    /**
     * Método encargado de buscar los familiares de una persona.
     * @param secuenciaPersona Secuencia de la persona de la que se necesita saber la familia.
     * @return Retorna una lista de Familiares pertenecientes a una persona especifica.
     */
    public void crear(EntityManager em, Familiares familiar);
    public void editar(EntityManager em, Familiares familiar);
    public void borrar(EntityManager em, Familiares familiar);
    public List<Familiares> familiaresPersona(EntityManager em,BigInteger secuenciaPersona);
}
