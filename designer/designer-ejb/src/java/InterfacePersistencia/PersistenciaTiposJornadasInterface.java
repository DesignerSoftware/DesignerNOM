/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposJornadas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TiposJornadas' 
 * de la base de datos.
 * @author Andres Pineda.
 */
public interface PersistenciaTiposJornadasInterface {
     public String crear(EntityManager em,TiposJornadas tiposJornadas);

    public String editar(EntityManager em,TiposJornadas tiposJornadas);

    public String borrar(EntityManager em,TiposJornadas tiposJornadas);

    public TiposJornadas buscarTiposJornadas(EntityManager em,BigInteger sectiposJornadas);

    public List<TiposJornadas> buscarTiposJornadas(EntityManager em);

}