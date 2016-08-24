/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Comprobantes;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Comprobantes' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaComprobantesInterface {

    /**
     * Método encargado de insertar un Comprobante en la base de datos.
     *
     * @param comprobante Comprobante que se quiere crear.
     */
    public void crear(EntityManager em, Comprobantes comprobante);

    /**
     * Método encargado de modificar un Comprobante de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param comprobante Comprobante con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Comprobantes comprobante);

    /**
     * Método encargado de eliminar de la base de datos el Comprobante que entra
     * por parámetro.
     *
     * @param comprobante Comprobante que se quiere eliminar.
     */
    public void borrar(EntityManager em, Comprobantes comprobante);

    /**
     * Método encargado de buscar el Comprobante con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Identificador único del Comprobante que se quiere
     * encontrar.
     * @return Retorna el Comprobante identificado con la secuencia dada por
     * parámetro.
     */
    public Comprobantes buscarComprobanteSecuencia(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de buscar todos los Comprobantes existentes en la base
     * de datos.
     *
     * @return Retorna una lista de Comprobantes.
     */
    public List<Comprobantes> buscarComprobantes(EntityManager em);

    /**
     * Método encargado de buscar todos los Comprobantes de un empleado
     * especifico.
     *
     * @param secuenciaEmpleado Secuencia del empleado al que se le quieren
     * buscar los comprobantes.
     * @return Retorna una lista de Comprobantes de un empleado.
     */
    public List<Comprobantes> comprobantesEmpleado(EntityManager em, BigInteger secuenciaEmpleado);

    /**
     * Método encargado de buscar el mayor número registrado en el atributo
     * 'numero' de la tabla Comprobantes de la base de datos.
     *
     * @return Retorna el número mas grande del atributo número en la tabla
     * Comprobantes.
     */
    public BigInteger numeroMaximoComprobante(EntityManager em);

    public BigDecimal buscarValorNumeroMaximo(EntityManager em);

    public Comprobantes buscarComprobanteParaPrimerRegistroEmpleado(EntityManager em, BigInteger secEmpleado);
}
