/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposEducaciones;
import java.util.List;
import javax.persistence.EntityManager;
/**
 * Clase Stateless 
 * Clase encargada de realizar operaciones sobre la tabla 'TiposEducaciones'
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaTiposEducacionesInterface {
    /**
     * Método encargado de buscar todos los TiposEducaciones existentes en la base de datos, ordenados por nombre.
     * @return Retorna una lista de TiposEducaciones ordenados por nombre.
     */
    public List<TiposEducaciones> tiposEducaciones(EntityManager em);
    public void crear(EntityManager em,TiposEducaciones tipoEducacion);
    public void borrar(EntityManager em,TiposEducaciones tipoEducacion);
    public void editar(EntityManager em,TiposEducaciones tipoEducacion);
    
}
