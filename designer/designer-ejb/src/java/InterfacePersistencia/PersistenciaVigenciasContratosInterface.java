/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasContratos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasContratos' 
 * de la base de datos.
 * @author Andres Pineda.
 */
public interface PersistenciaVigenciasContratosInterface {    
    /**
     * Método encargado de insertar una VigenciaContrato en la base de datos.
     * @param vigenciasContratos VigenciaContrato que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasContratos vigenciasContratos);
    /**
     * Método encargado de modificar una VigenciaContrato de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasContratos VigenciaContrato con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasContratos vigenciasContratos);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaContrato que entra por parámetro.
     * @param vigenciasContratos VigenciaContrato que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasContratos vigenciasContratos);
    /**
     * Método encargado de buscar todas las VigenciasContratos existentes en la base de datos.
     * @return Retorna una lista de VigenciasContratos.
     */
    public List<VigenciasContratos> buscarVigenciasContratos(EntityManager em);
    /**
     * Método encargado de buscar las VigenciasContratos asociadas a un empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna una lista de VigenciasContratos asociados al empleado cuya secuencia coincida con la del parámetro.
     */
    public List<VigenciasContratos> buscarVigenciaContratoEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar la VigenciaContrato con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaContrato que se quiere encontrar.
     * @return Retorna la VigenciaContrato identificada con la secuencia dada por parámetro.
     */
    public VigenciasContratos buscarVigenciaContratoSecuencia(EntityManager em, BigInteger secuencia);
    
}
