/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasFormasPagos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasFormasPagos' 
 * de la base de datos.
 * @author betelgeuse
 */
@Local
public interface PersistenciaVigenciasFormasPagosInterface {
    /**
     * Método encargado de insertar una VigenciaFormaPago en la base de datos.
     * @param vigenciasFormasPagos VigenciaFormaPago que se quiere crear.
     */
    public boolean crear(EntityManager em, VigenciasFormasPagos vigenciasFormasPagos);
    /**
     * Método encargado de modificar una VigenciaFormaPago de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasFormasPagos VigenciaFormaPago con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasFormasPagos vigenciasFormasPagos);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaFormaPago que entra por parámetro.
     * @param vigenciasFormasPagos VigenciaFormaPago que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasFormasPagos vigenciasFormasPagos);
    /**
     * Método encargado de buscar la VigenciaFormaPago con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaFormaPago que se quiere encontrar.
     * @return Retorna la VigenciaFormaPago identificada con la secuencia dada por parámetro.
     */
    public VigenciasFormasPagos buscarVigenciaFormaPago(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciasFormasPagos de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasFormasPagos, odenadas descendentemente por la fechaVigencia, del Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasFormasPagos> buscarVigenciasFormasPagosPorEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todas las VigenciasFormasPagos existentes en la base de datos.
     * @return Retorna una lista de VigenciasFormasPagos.
     */
    public List<VigenciasFormasPagos> buscarVigenciasFormasPagos(EntityManager em );
    
     public BigDecimal buscarPeriodicidadPorEmpl(EntityManager em, BigInteger secEmpleado);
    
}
