/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.CentrosCostos;
import Entidades.Cuentas;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Cuentas' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaCuentasInterface {

   /**
    * Método encargado de insertar una Cuenta en la base de datos.
    *
    * @param cuentas Cuenta que se quiere crear.
    */
   public void crear(EntityManager em, Cuentas cuentas);

   /**
    * Método encargado de modificar una Cuenta de la base de datos. Este método
    * recibe la información del parámetro para hacer un 'merge' con la
    * información de la base de datos.
    *
    * @param cuentas Cuenta con los cambios que se van a realizar.
    */
   public void editar(EntityManager em, Cuentas cuentas);

   /**
    * Método encargado de eliminar de la base de datos la cuenta que entra por
    * parámetro.
    *
    * @param cuentas Cuenta que se quiere eliminar.
    */
   public void borrar(EntityManager em, Cuentas cuentas);

   /**
    * Método encargado de buscar todas las cuentas existentes en la base de
    * datos.
    *
    * @return Retorna una lista de Cuentas.
    */
   public List<Cuentas> buscarCuentas(EntityManager em);

   /**
    * Método encargado de buscar la Cuenta con la secuencia dada por parámetro.
    *
    * @param secuencia Secuencia de la Cuenta que se quiere encontrar.
    * @return Retorna la Cuenta identificada con la secuencia dada por
    * parámetro.
    */
   public Cuentas buscarCuentasSecuencia(EntityManager em, BigInteger secuencia);

   /**
    * Método encargado de buscar las Cuentas de una empresa especifica.
    *
    * @param secuencia Secuencia de la empresa a quien pertenecen las cuentas.
    * @return Retorna una lista de Cuentas, las cuales pertenecen a una empresa
    */
   public List<Cuentas> buscarCuentasSecuenciaEmpresa(EntityManager em, BigInteger secuencia);

   public int contarVigCuentasPorTipoccConceptoYCuentac(EntityManager em, BigInteger tipoCC, BigInteger cuentaC, BigInteger concepto, Date fechaIni);

   public int contarVigCuentasPorTipoccConceptoYCuentad(EntityManager em, BigInteger tipoCC, BigInteger cuentaC, BigInteger concepto, Date fechaIni);

   public CentrosCostos centroCostoLocalizacionTrabajador(EntityManager em, BigInteger secEmpresa);

   public CentrosCostos centroCostoContabilidad(EntityManager em, BigInteger secEmpresa);

   public List<Cuentas> cuenta2505(EntityManager em, BigInteger secEmpresa);
}
