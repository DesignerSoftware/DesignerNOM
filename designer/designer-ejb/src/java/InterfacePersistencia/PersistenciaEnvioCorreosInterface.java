package InterfacePersistencia;

import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.EnvioCorreos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'EnvioCorreos' de la base de datos.
 */
public interface PersistenciaEnvioCorreosInterface {

    /**
     * Método encargado de buscar todos los EnvioCorreos existentes en la base de
     * datos.
     *
     * @param em
     * @param secReporte
     * @return Retorna una lista de EnvioCorreos.
     */
    public List<EnvioCorreos> consultarEnvios(EntityManager em, BigInteger secReporte);
    
        /**
     * Método encargado de buscar el EnvioCorreos con la secuencia dada por parámetro.
     * @param em
     * @param secEnvioReporte
     * @return Retorna el EnvioCorreos con la secuencia dada por parámetro.
     */
    public Inforeportes buscarEnvioCorreoporSecuencia(EntityManager em, BigInteger secEnvioReporte);
    
            /**
     * Método encargado de buscar el EnvioCorreos con la secuencia dada por parámetro.
     * @param em
     * @param secEnvioReporte
     * @return Retorna el EnvioCorreos identificada con la secuencia dada por parámetro.
     */
//    public List<Empleados> buscarEmpleados(EntityManager em, BigInteger secEnvioRepEmp);

    /**
     * Método encargado de modificar un EnvioCorreos de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param em
     * @param enviocorreos EnvioCorreos con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, EnvioCorreos enviocorreos);

    /**
     * Método encargado de eliminar de la base de datos el Envio de correo que
     * entra por parámetro.
     *
     * @param em
     * @param enviocorreo
     */
    public void borrar(EntityManager em, EnvioCorreos enviocorreo);
    
    /**
     *
     * @param em
     * @param emplDesde
     * @param emplHasta
     * @return
     */
    public List<Empleados> CorreoCodEmpleados(EntityManager em, BigDecimal emplDesde, BigDecimal emplHasta);
    
}
