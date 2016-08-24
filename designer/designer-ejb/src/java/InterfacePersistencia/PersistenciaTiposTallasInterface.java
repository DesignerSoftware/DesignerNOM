/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposTallas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TiposTallas' de la base de datos.
 *
 * @author betelgeuse
 */
@Local
public interface PersistenciaTiposTallasInterface {

    /**
     * Método encargado de insertar un TipoTalla en la base de datos.
     *
     * @param tiposTallas TipoTalla que se quiere crear.
     */
    public void crear(EntityManager em, TiposTallas tiposTallas);

    /**
     * Método encargado de modificar un TipoTalla de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposTallas TipoTalla con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposTallas tiposTallas);

    /**
     * Método encargado de eliminar de la base de datos el TipoTalla que entra
     * por parámetro.
     *
     * @param tiposTallas TipoTalla con los cambios que se van a realizar.
     */
    public void borrar(EntityManager em, TiposTallas tiposTallas);

    /**
     * Método encargado de buscar el TipoTalla con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del TipoTalla que se quiere encontrar.
     * @return Retorna el TipoTalla identificada con la secuencia dada por
     * parámetro.
     */
    public TiposTallas buscarTipoTalla(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de buscar todos los TiposTallas existentes en la base de
     * datos, ordenadas por código.
     *
     * @return Retorna una lista de TiposTallas.
     */
    public List<TiposTallas> buscarTiposTallas(EntityManager em );

    /**
     * Método encargado de recuperar cuantos Elementos están asociados a un
     * TipoTalla específico.
     *
     * @param secuencia Secuencia de TipoTalla.
     * @return Retorna el número de Elementos cuyo atributo 'TipoTalla' tiene
     * como secuencia el valor dado por parámetro.
     */
    public BigInteger contadorElementos(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de recuperar cuantos VigenciasTallas están asociados a
     * un TipoTalla específico.
     *
     * @param secuencia Secuencia de TipoTalla.
     * @return Retorna el número de VigenciasTallas cuyo atributo 'TipoTalla'
     * tiene como secuencia el valor dado por parámetro.
     */
    public BigInteger contadorVigenciasTallas(EntityManager em, BigInteger secuencia);
}
