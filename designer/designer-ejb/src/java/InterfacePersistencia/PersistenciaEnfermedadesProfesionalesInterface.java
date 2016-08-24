/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.EnfermeadadesProfesionales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'EnfermedadesProfesionales' 
 * de la base de datos.
 * @author Viktor
 */
public interface PersistenciaEnfermedadesProfesionalesInterface {
    /**
     * Método encargado de insertar una EnfermedadProfesional en la base de datos.
     * @param enfermedadesProfesionales EnfermedadProfesional que se quiere crear.
     */
    public void crear(EntityManager em,EnfermeadadesProfesionales enfermedadesProfesionales);
    /**
     * Método encargado de modificar una EnfermedadProfesional de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param enfermedadesProfesionales EnfermedadProfesional con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,EnfermeadadesProfesionales enfermedadesProfesionales);
    /**
     * Método encargado de eliminar de la base de datos la EnfermedadProfesional que entra por parámetro.
     * @param enfermedadesProfesionales EnfermedadProfesional que se quiere eliminar.
     */
    public void borrar(EntityManager em,EnfermeadadesProfesionales enfermedadesProfesionales);
    /**
     * Método encargado de buscar la EnfermedadProfesional con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la EnfermedadProfesional que se quiere encontrar.
     * @return Retorna la EnfermedadProfesional identificada con la secuencia dada por parámetro.
     */
    public EnfermeadadesProfesionales buscarEnfermedadesProfesionales(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar las EnfermedadesProfesionales asociadas a un empleado
     * @param secEmpleado Secuencia del empleado
     * @return Retorna una lista de EnfermedadesProfesionales
     */
    public List<EnfermeadadesProfesionales> buscarEPPorEmpleado(EntityManager em,BigInteger secEmpleado);
}
