/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.HVHojasDeVida;
import Entidades.HvEntrevistas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'HvEntrevistas' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaHvEntrevistasInterface {

    /**
     * Método encargado de insertar una Moneda en la base de datos.
     *
     * @param hvEntrevistas HvEntrevistas que se quiere crear.
     */
    public void crear(EntityManager em,HvEntrevistas hvEntrevistas);

    /**
     * Método encargado de modificar una Moneda de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param hvEntrevistas HvEntrevistas con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,HvEntrevistas hvEntrevistas);

    /**
     * Método encargado de eliminar de la base de datos una Moneda que entra por
     * parámetro.
     *
     * @param hvEntrevistas HvEntrevistas que se quiere eliminar.
     */
    public void borrar(EntityManager em,HvEntrevistas hvEntrevistas);

    /**
     * Método encargado de buscar un HvEntrevista con la secuencia dada por
     * parámetro.
     *
     * @param secHvEntrevista Secuencia de la HvEntrevista que se quiere
     * encontrar.
     * @return Retorna la HvEntrevista identificado con la secuencia dada por
     * parámetro.
     */
    public HvEntrevistas buscarHvEntrevista(EntityManager em,BigInteger secHvEntrevista);

    /**
     * Método encargado de buscar todas las HvEntrevistas existentes en la base
     * de datos.
     *
     * @return Retorna una lista de Monedas.
     */
    public List<HvEntrevistas> buscarHvEntrevistas(EntityManager em);

    /**
     * *
     * Metodo encargado de compara HVHojasDeVida y HvEntrevistas por medio de la
     * secuencia del empleado
     *
     * @param secEmpleado Secuecia Empleado
     * @return Lista de HvEntrevistas
     */
    public List<HvEntrevistas> buscarHvEntrevistasPorEmpleado(EntityManager em,BigInteger secEmpleado);

    /**
     * Metodo encargado de buscar una Lista de Hojas de vida relacionadas con la
     * secuencia
     *
     * @param secEmpleado Secuencia del empleado.
     * @return Una lista de hojas de vida.
     */
    public List<HVHojasDeVida> buscarHvHojaDeVidaPorPersona(EntityManager em,BigInteger secEmpleado);

    /**
     * Método encargado de recuperar las ultimas HvEntrevistas realizadas para
     * una hoja de vida.
     *
     * @param secuenciaHV Secuencia de la hoja de vida.
     * @return Retorna una lista de HvEntrevistas asociadas a una hoja de vida y
     * realizadas el mismo día.
     */
    public List<HvEntrevistas> entrevistasPersona(EntityManager em,BigInteger secuenciaHV);
    
    public String consultarPrimeraEnterevista(EntityManager em,BigInteger secuenciaHV);
    
    
}
