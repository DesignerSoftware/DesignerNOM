/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasCuentas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasCuentas' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasCuentasInterface {
    /**
     * Método encargado de insertar una VigenciaCuenta en la base de datos.
     * @param vigenciasCuentas VigenciaCuenta que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasCuentas vigenciasCuentas);
    /**
     * Método encargado de modificar una VigenciaCuenta de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasCuentas VigenciaCuenta con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasCuentas vigenciasCuentas);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaCuenta que entra por parámetro.
     * @param vigenciasCuentas VigenciaCuenta que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasCuentas vigenciasCuentas);
    /**
     * Método encargado de buscar la VigenciaCuenta con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaCuenta que se quiere encontrar.
     * @return Retorna la VigenciaCuenta identificada con la secuencia dada por parámetro.
     */
    public VigenciasCuentas buscarVigenciaCuentaSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todas las VigenciasCuentas existentes en la base de datos.
     * @return Retorna una lista de VigenciasCuentas.
     */
    public List<VigenciasCuentas> buscarVigenciasCuentas(EntityManager em );
    /**
     * Método encargado de buscar las vigenciasCuentas asociadas a un Concepto específico.
     * @param secuencia Secuencia del Concepto.
     * @return Retorna una lista de vigenciasCuentas cuyo Concepto tiene como secuencia la dada por parámetro.
     */
    public List<VigenciasCuentas> buscarVigenciasCuentasPorConcepto(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las vigenciasCuentas asociadas a una Cuenta de credito (EntityManager em, cuentac) específica.
     * @param secuencia Secuencia de la cuenta de credito (cuentac).
     * @return Retorna una lista de vigenciasCuentas cuya Cuenta de credito (cuentac) tiene como secuencia la dada por parámetro.
     */
    public List<VigenciasCuentas> buscarVigenciasCuentasPorCredito(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar las vigenciasCuentas asociadas a una Cuenta de debito (cuentad) específica.
     * @param secuencia Secuencia de la cuenta de debito (cuentad).
     * @return Retorna una lista de vigenciasCuentas cuya Cuenta de debito (cuentad) tiene como secuencia la dada por parámetro.
     */
    public List<VigenciasCuentas> buscarVigenciasCuentasPorDebito(EntityManager em, BigInteger secuencia);
}
