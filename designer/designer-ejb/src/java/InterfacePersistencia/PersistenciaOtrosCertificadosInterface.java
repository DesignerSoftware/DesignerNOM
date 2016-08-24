/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.OtrosCertificados;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'OtrosCertificados' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaOtrosCertificadosInterface {
    /**
     * Método encargado de insertar un OtroCertificado en la base de datos.
     * @param certificados OtroCertificado que se quiere crear.
     */
    public void crear(EntityManager em, OtrosCertificados certificados);
    /**
     * Método encargado de modificar un OtroCertificado de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param certificados OtroCertificado con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, OtrosCertificados certificados);
    /**
     * Método encargado de eliminar de la base de datos el OtroCertificado que entra por parámetro.
     * @param certificados OtroCertificado que se quiere eliminar.
     */
    public void borrar(EntityManager em, OtrosCertificados certificados);
    /**
     * Método encargado de buscar todos los OtrosCertificados existentes en la base de datos.
     * @return Retorna una lista de OtrosCertificados.
     */
    public List<OtrosCertificados> buscarOtrosCertificados(EntityManager em);
    /**
     * Método encargado de buscar el OtroCertificado con la secuencia dada por parámetro.
     * @param secuencia Secuencia del OtroCertificado que se quiere encontrar.
     * @return Retorna el OtroCertificado identificado con la secuencia dada por parámetro.
     */
    public OtrosCertificados buscarOtroCertificadoSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar los OtrosCertificados de un Empleado específico.
     * @param secuencia Secuencia del Empleado.
     * @return Retorna los OtrosCertificados de un empleado
     */
    public List<OtrosCertificados> buscarOtrosCertificadosEmpleado(EntityManager em, BigInteger secuencia);
}
