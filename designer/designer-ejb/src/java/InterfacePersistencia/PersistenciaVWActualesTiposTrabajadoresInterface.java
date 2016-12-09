/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Empleados;
import Entidades.VWActualesTiposTrabajadores;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * vista 'VWActualesTiposTrabajadores' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaVWActualesTiposTrabajadoresInterface {

   /**
    * Método encargado de buscar el TipoTrabajador actual de un empleado, para
    * esto se realiza la consulta sobre la vista VWActualesAfiliacionesPension.
    * El término actual está dado por la fechaHasta de las liquidaciones.
    *
    * @param secuencia Secuencia del empleado.
    * @return Retorna una VWActualesTiposTrabajadores con la información del
    * TipoTrabajador actual de un empleado.
    */
   public VWActualesTiposTrabajadores buscarTipoTrabajador(EntityManager em, BigInteger secuencia);

   public VWActualesTiposTrabajadores buscarTipoTrabajadorCodigoEmpl(EntityManager em, BigInteger codigo);

   /**
    * Método encargado de buscar los VWActualesTiposTrabajadores filtrados por
    * TipoTrabajador.
    *
    * @param tipo Secuencia del TipoTrabajador.
    * @return Retorna una lista de VWActualesTiposTrabajadores asociados con el
    * TipoTrabajador específicado.
    */
   public List<VWActualesTiposTrabajadores> FiltrarTipoTrabajador(EntityManager em, String tipo);

   /**
    * Método encargado de buscar los TiposTrabajadores actuales, para esto se
    * realiza la consulta sobre la vista VWActualesAfiliacionesPension. El
    * término actual está dado por la fechaHasta de las liquidaciones.
    *
    * @return Retorna una lista con todos los VWActualesTiposTrabajadores de la
    * vista.
    */
   public List<VWActualesTiposTrabajadores> busquedaRapidaTrabajadores(EntityManager em);

   /**
    * Método encargado de verificar si un empleado está 'ACTIVO' en el momento
    * de la fechaHasta de la liquidación.
    *
    * @param empleado Secuencia del empleado.
    * @return Retorna True si el empleado cuya secuencia coincide con la del
    * parametro, es de tipo 'ACTIVO'. False de lo contrario.
    */
   public boolean verificarTipoTrabajador(EntityManager em, Empleados empleado);

   /**
    * Método encargado de buscar los VWActualesTiposTrabajadores cuyo
    * TipoTrabajador es de tipo 'ACTIVO', 'PENSIONADO' o 'RETIRADO'.
    *
    * @return Retorna una lista de VWActualesTiposTrabajadores.
    */
   public List<VWActualesTiposTrabajadores> tipoTrabajadorEmpleado(EntityManager em);

   public VWActualesTiposTrabajadores filtrarTipoTrabajadorPosicion(EntityManager em, String p_tipo, int posicion);

   public int obtenerTotalRegistrosTipoTrabajador(EntityManager em, String p_tipo);

   public String consultarTipoTrabajador(EntityManager em, BigInteger secEmpleado);

   public Date consultarFechaVigencia(EntityManager em, BigInteger secEmpleado);
}
