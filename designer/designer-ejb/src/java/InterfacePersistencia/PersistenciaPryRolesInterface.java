/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.PryRoles;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'PryRoles' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaPryRolesInterface {
    /**
     * Método encargado de buscar todos los PryRoles existentes en la base de datos, ordenados por descripción.
     * @param em
     * @return Retorna una lista de PryRoles ordenados por descripción.
     */
    public List<PryRoles> pryroles(EntityManager em);
    
    public void crear(EntityManager em, PryRoles pryrol);

    public void editar(EntityManager em, PryRoles pryrol);

    public void borrar(EntityManager em, PryRoles pryrol);
}
