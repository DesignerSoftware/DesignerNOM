/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TercerosSucursales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TercerosSucursales' 
 * de la base de datos.
 * @author user
 */
public interface PersistenciaTercerosSucursalesInterface {
    /**
     * Método encargado de insertar un TerceroSucursal en la base de datos.
    * @param em
     * @param tercerosSucursales TerceroSucursal que se quiere crear.
     */
    public void crear(EntityManager em, TercerosSucursales tercerosSucursales);
    /**
     * Método encargado de modificar un TerceroSucursal de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param tercerosSucursales TerceroSucursal con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TercerosSucursales tercerosSucursales);
    /**
     * Método encargado de eliminar de la base de datos el TerceroSucursal que entra por parámetro.
     * @param tercerosSucursales TerceroSucursal que se quiere eliminar.
     */
    public void borrar(EntityManager em, TercerosSucursales tercerosSucursales);
    /**
     * Método encargado de buscar todos los TercerosSucursales existentes en la base de datos.
     * @return Retorna una lista de TercerosSucursales.
     */
    public List<TercerosSucursales> buscarTercerosSucursales(EntityManager em);
    /**
     * Método encargado de buscar el TerceroSucursal con la secuencia dada por parámetro.
     * @param secuencia Secuencia del TerceroSucursal que se quiere encontrar.
     * @return Retorna el TerceroSucursal identificado con la secuencia dada por parámetro.
     */
    public TercerosSucursales buscarTercerosSucursalesSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar los TercerosSucursales relacionados con un tercero específico.
    * @param em
     * @param secuencia Secuencia del Tercero.
     * @return Retorna una lista de TercerosSucursales, 
     * los cuales están asociados con el tercero de la secuencia pasada por parámetro.
     */
    public List<TercerosSucursales> buscarTercerosSucursalesPorTerceroSecuencia(EntityManager em, BigInteger secuencia);
    
   /**
    *
    * @param em
    * @param secuencia
    * @return
    */
   public List<TercerosSucursales> buscarTercerosSucursalesPorEmpresa(EntityManager em, BigInteger secuencia);
    
}
