/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposDescansos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TiposDescansos' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaTiposDescansosInterface {

    /**
     * Método encargado de insertar un TipoDescanso en la base de datos.
     *
     * @param tiposDescansos TipoDescanso que se quiere crear.
     */
    public void crear(EntityManager em, TiposDescansos tiposDescansos);

    /**
     * Método encargado de modificar un TipoDescanso de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposDescansos TipoDescanso con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposDescansos tiposDescansos);

    /**
     * Método encargado de eliminar de la base de datos el TipoDescanso que
     * entra por parámetro.
     *
     * @param tiposDescansos TipoDescanso que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposDescansos tiposDescansos);

    /**
     * Método encargado de buscar todos los TiposDescansos existentes en la base
     * de datos.
     *
     * @return Retorna una lista de TiposDescansos.
     */
    public List<TiposDescansos> consultarTiposDescansos(EntityManager em);

    /**
     * Método encargado de buscar el TipoDescanso con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del TipoDescanso que se quiere encontrar.
     * @return Retorna el TipoDescanso identificado con la secuencia dada por
     * parámetro.
     */
    public TiposDescansos consultarTipoDescanso(EntityManager em, BigInteger secuencia);

    /**
     * *
     * Metodo encargado de contar cuantas VigenciasJornadas estan relacionadas
     * con la secuencia del tipodescanso
     *
     * @param secTiposDescansos Secuencia del TipoDescanso
     * @return Cuantas VigenciasJornadas tienen relacion con la secuencia
     * recivida
     */
    public BigInteger contarVigenciasJornadasTipoDescanso(EntityManager em, BigInteger secTiposDescansos);
}
