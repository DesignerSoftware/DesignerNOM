/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasCompensaciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasCompensaciones' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaVigenciasCompensacionesInterface {
    /**
     * Método encargado de insertar una VigenciaCompensacion en la base de datos.
     * @param vigenciasCompensaciones VigenciaCompensacion que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasCompensaciones vigenciasCompensaciones);
    /**
     * Método encargado de modificar una VigenciaCompensacion de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasCompensaciones VigenciaCompensacion con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasCompensaciones vigenciasCompensaciones);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaCompensacion que entra por parámetro.
     * @param vigenciasCompensaciones VigenciaCompensacion que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasCompensaciones vigenciasCompensaciones);
    /**
     * Método encargado de buscar todas las VigenciasCompensaciones existentes en la base de datos.
     * @return Retorna una lista de VigenciasCompensaciones.
     */
    public List<VigenciasCompensaciones> buscarVigenciasCompensaciones(EntityManager em );
    /**
     * Método encargado de buscar la VigenciaCompensacion con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaCompensacion que se quiere encontrar.
     * @return Retorna la VigenciaCompensacion identificada con la secuencia dada por parámetro.
     */
    public VigenciasCompensaciones buscarVigenciaCompensacionSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciasCompensaciones de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna una lista de VigenciasCompensaciones asociadas al Empleado cuya secuencia coincide
     * con la dada por parámetro y ordenadas de forma descendente por fechaInicial.
     */
    public List<VigenciasCompensaciones> buscarVigenciasCompensacionesEmpleado(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciasCompensaciones asociadas a una VigenciaJornada específica.
     * @param secuencia Secuencia de la VigenciaJornada.
     * @return Retorna una lista de VigenciasCompensaciones ordenadas de forma descendente por fechaInicial y 
     * asociadas a la VigenciaJornada cuya secuencia coincide con la dada por parámetro.
     */
    public List<VigenciasCompensaciones> buscarVigenciasCompensacionesVigenciaSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las VigenciasCompensaciones con un tipoCompensacion definido.
     * @param tipoC String que representa el tipo de compensación. Este atributo solo puede ser 'DINERO' o 'DESCANSO'.
     * @return Retorna una lista de VigenciasCompensaciones ordenadas de forma descendente por fechaInicial y
     * cuyo atributo tipoCompensacion coincide con el parámetro.
     */
    public List<VigenciasCompensaciones> buscarVigenciasCompensacionesTipoCompensacion (EntityManager em, String tipoC);
    /**
     * Método encargado de buscar las VigenciasCompensaciones con un tipoCompensacion definido y una VigenciaJornada específica.
     * @param tipoC String que representa el tipo de compensación. Este atributo solo puede ser 'DINERO' o 'DESCANSO'.
     * @param secuencia Secuencia de la VigenciaJornada.
     * @return Retorna una lista de VigenciasCompensaciones ordenadas de forma descendente por fechaInicial,
     * asociadas a la VigenciaJornada cuya secuencia coincide con la dada por el parámetro "secuencia" y 
     * cuyo atributo tipoCompensacion coincide con el parámetro "tipoC".
     */
    public List<VigenciasCompensaciones> buscarVigenciasCompensacionesVigenciayCompensacion(EntityManager em, String tipoC,BigInteger secuencia);
}
