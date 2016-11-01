/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.HVHojasDeVida;
import Entidades.HvReferencias;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'HvReferencias' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaHvReferenciasInterface {

    /**
     * Metodo encargado de buscar las referencias personales de una persona.
     *
     * @param secuenciaHV Secuencia de la hoja de vida de la persona.
     * @return Retorna una lista de HvReferencias con las referencias personales
     * de una persona.
     *
     */
    public List<HvReferencias> referenciasPersonalesPersona(EntityManager em,BigInteger secuenciaHV);

    /**
     * Metodo encargado de buscar las referencias familiares de una persona.
     *
     * @param secuenciaHV Secuencia de la hoja de vida de la persona.
     * @return Retorna una lista de HvReferencias con las referencias familiares
     * de una persona.
     */
    public List<HvReferencias> contarReferenciasFamiliaresPersona(EntityManager em,BigInteger secuenciaHV);

    /**
     * Método encargado de insertar una HvReferencia en la base de datos.
     *
     * @param hvReferencias HvReferencia que se quiere crear.
     */
    public void crear(EntityManager em,HvReferencias hvReferencias);

    /**
     * Método encargado de modificar una HvReferencia de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param hvReferencias HvReferencias con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,HvReferencias hvReferencias);

    /**
     * Método encargado de eliminar de la base de datos una HvReferencia que
     * entra por parámetro.
     *
     * @param hvReferencias HvReferencias que se quiere eliminar.
     */
    public void borrar(EntityManager em,HvReferencias hvReferencias);

    /**
     * Método encargado de buscar el HvReferencias con la secuencia dada por
     * parámetro.
     *
     * @param secuenciaHvReferencias Secuencia de la HvRefencia que se quiere
     * encontrar.
     * @return Retorna la HvReferencia identificado con la secuencia dada por
     * parámetro.
     */
    public HvReferencias buscarHvReferencia(EntityManager em,BigInteger secuenciaHvReferencias);

    /**
     * Método encargado de buscar todas las HvReferencias existentes en la base
     * de datos.
     *
     * @return Retorna una lista de HvReferencias.
     */
    public List<HvReferencias> buscarHvReferencias(EntityManager em);

    /**
     * *
     * Metodo encargado de traer la lista de HvReferencias por empleado donde
     * las referecias en su campo Tipo='PERSONALES'
     *
     * @param secEmpleado Secuencia del empleado
     * @return Lista de Referencias Por empleado
     */
    public List<HvReferencias> consultarHvReferenciasPersonalesPorPersona(EntityManager em,BigInteger secEmpleado);


    /**
     * *
     * Metodo encargado de traer la lista de HvReferencias por empleado donde
     * las referecias en su campo Tipo='FAMILIARES'
     *
     * @param secEmpleado Secuencia del empleado
     * @return Lista de Referencias Por empleado
     */
    public List<HvReferencias> consultarHvReferenciasFamiliarPorPersona(EntityManager em,BigInteger secEmpleado);
    /**
     * Metodo encargado de traer las hojas de vida del empleado relacionadas con
     * HvReferencias
     *
     * @param secEmpleado Secuencia del empleado
     * @return Retorna una lista De HVHojasDeVida
     */
    public List<HVHojasDeVida> consultarHvHojaDeVidaPorPersona(EntityManager em,BigInteger secEmpleado);
    
    public String primeraReferenciaFamiliar(EntityManager em, BigInteger secHV);
    
    public String primeraReferenciaPersonal(EntityManager em, BigInteger secHV);
}
