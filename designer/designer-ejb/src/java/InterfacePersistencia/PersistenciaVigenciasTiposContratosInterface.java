/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Empleados;
import Entidades.VigenciasTiposContratos;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasTiposContratos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasTiposContratosInterface {
    /**
     * Método encargado de insertar una VigenciaTipoContrato en la base de datos.
     * @param vigenciasTiposContratos VigenciaTipoContrato que se quiere crear.
     */
    public boolean crear(EntityManager em, VigenciasTiposContratos vigenciasTiposContratos);
    /**
     * Método encargado de modificar una VigenciaTipoContrato de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasTiposContratos VigenciaTipoContrato con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasTiposContratos vigenciasTiposContratos);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaTipoContrato que entra por parámetro.
     * @param vigenciasTiposContratos VigenciaTipoContrato que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasTiposContratos vigenciasTiposContratos);
    /**
     * Método encargado de buscar la VigenciaTipoContrato con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaTipoContrato que se quiere encontrar.
     * @return Retorna la VigenciaTipoContrato identificada con la secuencia dada por parámetro.
     */
    public VigenciasTiposContratos buscarVigenciaTipoContrato(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todas las VigenciasTiposContratos existentes en la base de datos.
     * @return Retorna una lista de VigenciasTiposContratos.
     */
    public List<VigenciasTiposContratos> buscarVigenciasTiposContratos(EntityManager em );
    /**
     * Método encargado de buscar las VigenciasTiposContratos de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasTiposContratos, odenadas descendentemente por la fechaInicial, del Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasTiposContratos> buscarVigenciaTipoContratoEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar la última fecha de contratación de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna la mayor fecha registrada para el contrato del empleado cuya secuencia
     * coincide con la secuencia dada por parámetro.
     */
    public Date fechaMaxContratacion(EntityManager em, Empleados secuencia);
    
    
    public Date fechaFinalContratacionVacaciones(EntityManager em, BigInteger secuencia);
    
    public Date fechaMaxContrato(EntityManager em, BigInteger secuencia);
    
}
