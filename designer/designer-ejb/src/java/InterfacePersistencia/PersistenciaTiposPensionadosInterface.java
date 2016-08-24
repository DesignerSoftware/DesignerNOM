/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposPensionados;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TiposPensionados' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaTiposPensionadosInterface {

    /**
     * Método encargado de insertar un TipoPensionado en la base de datos.
     *
     * @param tiposPensionados TipoPensionado que se quiere crear.
     */
    public void crear(EntityManager em, TiposPensionados tiposPensionados);

    /**
     * Método encargado de modificar un TipoPensionado de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposPensionados TipoPensionado con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, TiposPensionados tiposPensionados);

    /**
     * Método encargado de eliminar de la base de datos el TipoPensionado que
     * entra por parámetro.
     *
     * @param tiposPensionados TipoPensionado que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposPensionados tiposPensionados);

    /**
     * Método encargado de buscar todos los TiposPensionados existentes en la
     * base de datos.
     *
     * @return Retorna una lista de TiposPensionados.
     */
    public List<TiposPensionados> consultarTiposPensionados(EntityManager em );

    /**
     * Método encargado de buscar el TipoPensionado con la secTiposPensionados
     * dada por parámetro.
     *
     * @param secTiposPensionados Secuencia del TipoPensionado que se quiere
     * encontrar.
     * @return Retorna el TipoPensionado identificado con la secTiposPensionados
     * dada por parámetro.
     */
    public TiposPensionados consultarTipoPensionado(EntityManager em, BigInteger secTiposPensionados);

    /**
     * Metodo encargado de contar cuantas Persionados estan relacionadas con la
     * secClasesPensiones de TipoPension recivida
     *
     * @param secTiposPensionados Secuencia de la TipoPension
     * @return Cuantos Persionados tienen la secClasesPensiones recivida
     */
    public BigInteger contarPensionadosTipoPension(EntityManager em, BigInteger secTiposPensionados);
}
