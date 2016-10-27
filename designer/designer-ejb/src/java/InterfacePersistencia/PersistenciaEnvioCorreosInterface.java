package InterfacePersistencia;

import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.envioCorreos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'EnvioCorreos' de la base de datos.
 */
public interface PersistenciaEnvioCorreosInterface {

    /**
     * Método encargado de buscar todos los envioCorreos existentes en la base de
     * datos.
     *
     * @param em
     * @param secReporte
     * @return Retorna una lista de envioCorreos.
     */
    public List<envioCorreos> consultarEnvios(EntityManager em, BigInteger secReporte);
    
        /**
     * Método encargado de buscar el envioCorreos con la secuencia dada por parámetro.
     * @param em
     * @param secEnvioReporte
     * @return Retorna el envioCorreos con la secuencia dada por parámetro.
     */
    public Inforeportes buscarEnvioCorreoporSecuencia(EntityManager em, BigInteger secEnvioReporte);
    
            /**
     * Método encargado de buscar el envioCorreos con la secuencia dada por parámetro.
     * @param em
     * @param secEnvioReporte
     * @return Retorna el envioCorreos identificada con la secuencia dada por parámetro.
     */
//    public List<Empleados> buscarEmpleados(EntityManager em, BigInteger secEnvioRepEmp);

    /**
     * Método encargado de modificar un envioCorreos de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param em
     * @param enviocorreos envioCorreos con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, envioCorreos enviocorreos);

    /**
     * Método encargado de eliminar de la base de datos el Envio de correo que
     * entra por parámetro.
     *
     * @param em
     * @param enviocorreo
     */
    public void borrar(EntityManager em, envioCorreos enviocorreo);
}
