/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposCentrosCostos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TiposCentrosCostos' 
 * de la base de datos.
 * @author betelgeuse
 */
@Local
public interface PersistenciaTiposCentrosCostosInterface {
    /**
     * Método encargado de insertar un TipoCentroCosto en la base de datos.
     * @param tiposCentrosCostos TipoCentroCosto que se quiere crear.
     */
    public void crear(EntityManager em, TiposCentrosCostos tiposCentrosCostos);
    /**
     * Método encargado de modificar un TipoCentroCosto de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param tiposCentrosCostos TipoCentroCosto con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposCentrosCostos tiposCentrosCostos);
    /**
     * Método encargado de eliminar de la base de datos el TipoCentroCosto que entra por parámetro.
     * @param tiposCentrosCostos TipoCentroCosto que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposCentrosCostos tiposCentrosCostos);
    /**
     * Método encargado de buscar el TipoCentroCosto con la secTiposCentrosCostos dada por parámetro.
     * @param secTiposCentrosCostos Secuencia del TipoCentroCosto que se quiere encontrar.
     * @return Retorna el TipoCentroCosto identificado con la secTiposCentrosCostos dada por parámetro.
     */
    public TiposCentrosCostos buscarTipoCentrosCostos(EntityManager em, BigInteger secTiposCentrosCostos);
    /**
     * Método encargado de buscar todos los TiposCentrosCostos existentes en la base de datos.
     * @return Retorna una lista de TiposCentrosCostos.
     */
    public List<TiposCentrosCostos> buscarTiposCentrosCostos(EntityManager em);
    /**
     * Método encargado de contar los CentrosCostos asociados a el TipoCentroCosto cuya secTiposCentrosCostos coincide con la del 
     * parámetro.
     * @param secTiposCentrosCostos Secuencia del TipoCentroCosto
     * @return Retorna un Número con la cantidad de CentrosCostos asociados a un TipoCentroCosto.
     */
    public BigInteger verificarBorradoCentrosCostos(EntityManager em, BigInteger secTiposCentrosCostos);
    /**
     * Método encargado de contar las VigenciasCuentas asociadas a el TipoCentroCosto cuya secTiposCentrosCostos coincide con la del 
     * parámetro.
     * @param secTiposCentrosCostos Secuencia del TipoCentroCosto
     * @return Retorna un Número con la cantidad de VigenciasCuentas asociados a un TipoCentroCosto.
     */
    public BigInteger verificarBorradoVigenciasCuentas(EntityManager em, BigInteger secTiposCentrosCostos);
    /**
     * Método encargado de contar los RiesgosProfesionales asociados a el TipoCentroCosto cuya secTiposCentrosCostos coincide con la del 
     * parámetro.
     * @param secTiposCentrosCostos Secuencia del TipoCentroCosto
     * @return Retorna un Número con la cantidad de RiesgosProfesionales asociados a un TipoCentroCosto.
     */
    public BigInteger verificarBorradoRiesgosProfesionales(EntityManager em, BigInteger secTiposCentrosCostos);
}
