/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasNormasEmpleados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasNormasEmpleados' 
 * de la base de datos.
 * @author betelgeuse
 */
@Local
public interface PersistenciaVigenciasNormasEmpleadosInterface {
    /**
     * Método encargado de insertar una VigenciaNormaEmpleado en la base de datos.
     * @param vigenciasNormasEmpleados VigenciaNormaEmpleado que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasNormasEmpleados vigenciasNormasEmpleados);
    /**
     * Método encargado de modificar una VigenciaNormaEmpleado de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasNormasEmpleados VigenciaNormaEmpleado con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasNormasEmpleados vigenciasNormasEmpleados);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaNormaEmpleado que entra por parámetro.
     * @param vigenciasNormasEmpleados VigenciaNormaEmpleado que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasNormasEmpleados vigenciasNormasEmpleados);
    /**
     * Método encargado de buscar la VigenciaNormaEmpleado con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaNormaEmpleado que se quiere encontrar.
     * @return Retorna la VigenciaNormaEmpleado identificada con la secuencia dada por parámetro.
     */
    public VigenciasNormasEmpleados buscarVigenciasNormasEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciasNormasEmpleados de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna las VigenciasNormasEmpleados, odenadas descendentemente por la fechaVigencia, del Empleado cuya secuencia coincide 
     * con la secuencia dada por parámetro.
     */
    public List<VigenciasNormasEmpleados> buscarVigenciasNormasEmpleadosEmpl(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todas las VigenciasNormasEmpleados existentes en la base de datos.
     * @return Retorna una lista de VigenciasNormasEmpleados.
     */
    public List<VigenciasNormasEmpleados> buscarVigenciasNormasEmpleados(EntityManager em );
}
