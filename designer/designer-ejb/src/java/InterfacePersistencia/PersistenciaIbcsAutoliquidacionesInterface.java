/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.IbcsAutoliquidaciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'IbcsAutoliquidaciones' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaIbcsAutoliquidacionesInterface {
    /**
     * Método encargado de insertar un contrato en la base de datos.
     * @param autoliquidaciones IbcsAutoliquidacion que se quiere crear.
     */
    public void crear(EntityManager em,IbcsAutoliquidaciones autoliquidaciones);
    /**
     * Método encargado de modificar un IbcsAutoliquidacion de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param autoliquidaciones IbcsAutoliquidacion con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,IbcsAutoliquidaciones autoliquidaciones);
    /**
     * Método encargado de eliminar de la base de datos el IbcsAutoliquidacion que entra por parámetro.
     * @param autoliquidaciones IbcsAutoliquidacion que se quiere eliminar.
     */
    public void borrar(EntityManager em,IbcsAutoliquidaciones autoliquidaciones);
    /**
     * Método encargado de buscar todos los IbcsAutoliquidaciones existentes en la base de datos.
     * @return Retorna una lista de IbcsAutoliquidaciones.
     */
    public List<IbcsAutoliquidaciones> buscarIbcsAutoliquidaciones(EntityManager em);
    /**
     * Método encargado de buscar el IbcsAutoliquidacion con la secuencia dada por parámetro.
     * @param secuencia Secuencia del IbcsAutoliquidacion que se quiere encontrar.
     * @return Retorna el IbcsAutoliquidacion identificado con la secuencia dada por parámetro.
     */
    public IbcsAutoliquidaciones buscarIbcAutoliquidacionSecuencia(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar los IbcsAutoliquidaciones de un TipoEntidad y empleado especificos.
     * @param secuenciaTE Secuencia del TipoEntidad a la que pertenecen los IbcsAutoliquidaciones. 
     * @param secuenciaEmpl Secuencia del Empleado asociado a los IbcsAutoliquidaciones que se quieren encontrar.
     * @return Retorna una lista de IbcsAutoliquidaciones pertenecientes a un TipoEntidad y a un Empleado.
     */
    public List<IbcsAutoliquidaciones> buscarIbcsAutoliquidacionesTipoEntidadEmpleado(EntityManager em,BigInteger secuenciaTE, BigInteger secuenciaEmpl);
}
