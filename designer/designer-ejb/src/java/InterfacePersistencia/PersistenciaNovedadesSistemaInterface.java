/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.NovedadesSistema;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'NovedadesSistema' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaNovedadesSistemaInterface {

   /**
    * Método encargado de insertar una NovedadSistema en la base de datos.
    *
    * @param em
    * @param novedades NovedadSistema que se quiere crear.
    */
   public void crear(EntityManager em, NovedadesSistema novedades);

   /**
    * Método encargado de modificar una NovedadSistema de la base de datos. Este
    * método recibe la información del parámetro para hacer un 'merge' con la
    * información de la base de datos.
    *
    * @param em
    * @param novedades NovedadSistema con los cambios que se van a realizar.
    */
   public void editar(EntityManager em, NovedadesSistema novedades);

   /**
    * Método encargado de eliminar de la base de datos la NovedadSistema que
    * entra por parámetro.
    *
    * @param em
    * @param novedades NovedadSistema que se quiere eliminar.
    */
   public void borrar(EntityManager em, NovedadesSistema novedades);

   /**
    * Método encargado de buscar las NovedadesSistema ordenadas por
    * fechaInicialDisfrute, de tipo 'DEFINITIVA' y de un empleado dado como
    * parámetro.
    *
    * @param em
    * @param secuenciaEmpleado Secuencia del empleado al que se la buscan las
    * NovedadesSistema
    * @return Retorna una lista de NovedadesSistema.
    */
   public List<NovedadesSistema> novedadesEmpleado(EntityManager em, BigInteger secuenciaEmpleado);

   public List<NovedadesSistema> novedadesEmpleadoVacaciones(EntityManager em, BigInteger secuenciaEmpleado);

   /**
    * Método encargado de buscar si ha o no disfrutado vacaciones.
    *
    * @param em Conexion de la sesion.
    * @param secuenciaEmpleado Secuencia del empleado al que se la buscan las
    * NovedadesSistema
    * @return Retorna un String.
    */
   public BigDecimal valorCesantias(EntityManager em, BigInteger secuenciaEmpleado);

   public BigDecimal valorIntCesantias(EntityManager em, BigInteger secuenciaEmpleado);

   public String buscarEstadoVacaciones(EntityManager em, BigInteger secuenciaEmpleado);

   public List<NovedadesSistema> novedadesEmpleadoCesantias(EntityManager em, BigInteger secuenciaEmpleado);

//    public List<NovedadesSistema> todasNovedadesCesantias(EntityManager em, BigInteger secuenciaEmpleado);
//    
//    public List<NovedadesSistema> novedadesCesantiasNoLiquidadas(EntityManager em, BigInteger secuenciaEmpleado);
   public List<NovedadesSistema> novedadsistemaPorEmpleadoYVacacion(EntityManager em, BigInteger secEmpleado, BigInteger secVacacion);

   public BigDecimal consultarValorTotalDetalleVacacion(EntityManager em, BigInteger secNovedadSistema);

   public void adicionaNovedadCambiosMasivos(EntityManager em,
           String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
           BigInteger secTercero, BigInteger secFormula, BigInteger valor,
           BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal,
           BigInteger unidadParteEntera, BigInteger unidadParteFraccion);

   public void undoAdicionaNovedadCambiosMasivos(EntityManager em,
           String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
           BigInteger secTercero, BigInteger secFormula, BigInteger valor,
           BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal);
}
