/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Pensionados;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Pensionados' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaPensionadosInterface {
    /**
     * Método encargado de insertar un Pensionado en la base de datos.
     * @param pensionados Pensionado que se quiere crear.
     */
    public void crear(EntityManager em, Pensionados pensionados);
    /**
     * Método encargado de modificar un Pensionado de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param pensionados Pensionado con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Pensionados pensionados);
    /**
     * Método encargado de eliminar de la base de datos el Pensionado que entra por parámetro.
     * @param pensionados Pensionado que se quiere eliminar.
     */
    public void borrar(EntityManager em, Pensionados pensionados);
    /**
     * Método encargado de buscar el Pensionado con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Pensionado que se quiere encontrar.
     * @return Retorna el Pensionado identificado con la secuencia dada por parámetro.
     */
    public Pensionados buscarPensionado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los Pensionados existentes en la base de datos.
     * @return Retorna una lista de Pensionados.
     */
    public List<Pensionados> buscarPensionados(EntityManager em);
    /**
     * Método encargado de buscar los Pensionados por empleado y ordenados por fechainiciopension.
     * @param secEmpleado Secuencia del empleado por el cual se quieren buscar los Pensionados.
     * @return Retorna una lista de Pensionados cuya vigenciatipotrabajador esta asociada al empleado 
     * con secuencia igual a la del parametro.
     */
    public List<Pensionados> buscarPensionadosEmpleado(EntityManager em, BigInteger secEmpleado);
    /**
     * Método encargado de buscar un Pensionado por su vigenciatipotrabajador.
     * @param secVigencia Secuencia de la vigenciatipotrabajador por la que se quiere buscar.
     * @return Retorna el pensionado asociado con la vigenciatipotrabajador.
     */
    public Pensionados buscarPensionVigenciaSecuencia(EntityManager em, BigInteger secVigencia);
    
}
