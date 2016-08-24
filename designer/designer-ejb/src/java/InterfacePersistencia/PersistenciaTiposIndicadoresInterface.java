/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposIndicadores;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TiposIndicadores' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaTiposIndicadoresInterface {

    /**
     * Método encargado de insertar un TipoIndicador en la base de datos.
     *
     * @param tiposIndicadores TipoIndicador que se quiere crear.
     */
    public void crear(EntityManager em, TiposIndicadores tiposIndicadores);

    /**
     * Método encargado de modificar un TipoIndicador de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposIndicadores TipoIndicador con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, TiposIndicadores tiposIndicadores);

    /**
     * Método encargado de eliminar de la base de datos el TipoIndicador que
     * entra por parámetro.
     *
     * @param tiposIndicadores TipoIndicador que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposIndicadores tiposIndicadores);

    /**
     * Método encargado de buscar todos los TiposIndicadores existentes en la
     * base de datos.
     *
     * @return Retorna una lista de TiposIndicadores.
     */
    public List<TiposIndicadores> buscarTiposIndicadores(EntityManager em );

    /**
     * Método encargado de buscar el TipoIndicador con la secTiposIndicadores dada por
     * parámetro.
     *
     * @param secTiposIndicadores Secuencia del TipoIndicador que se quiere encontrar.
     * @return Retorna el TipoIndicador identificado con la secTiposIndicadores dada por
     * parámetro.
     */
    public TiposIndicadores buscarTiposIndicadoresSecuencia(EntityManager em, BigInteger secTiposIndicadores);

    /**
     * Método encargado de revisar si existe una relacion entre un TipoIndicador
     * específica y algúna VigenciasIndicadores. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secTiposIndicadores Secuencia del TipoIndicadores.
     * @return Retorna el número de proyectos relacionados con el TipoIndicador
     * cuya secTiposIndicadoresTR coincide con el parámetro.
     */
    public BigInteger contadorVigenciasIndicadores(EntityManager em, BigInteger secTiposIndicadores);
}
