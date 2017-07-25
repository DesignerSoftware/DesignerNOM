/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Empleados;
import Entidades.VWActualesTiposTrabajadores;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la vista
 * 'VWActualesTiposTrabajadores' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWActualesTiposTrabajadores implements PersistenciaVWActualesTiposTrabajadoresInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesTiposTrabajadores.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
    */
   @Override
   public VWActualesTiposTrabajadores buscarTipoTrabajador(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt WHERE vwatt.empleado.secuencia = :empleado");
         query.setParameter("empleado", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesTiposTrabajadores vwActualesTiposTrabajadores = (VWActualesTiposTrabajadores) query.getSingleResult();
         return vwActualesTiposTrabajadores;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaVWActualesTiposTrabajadores.buscarTipoTrabajador()" + e.getMessage());
         return null;
      }
   }

   @Override
   public VWActualesTiposTrabajadores buscarTipoTrabajadorCodigoEmpl(EntityManager em, BigInteger codigo) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt WHERE vwatt.empleado.codigoempleado = :empleado");
         query.setParameter("empleado", codigo);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesTiposTrabajadores vwActualesTiposTrabajadores = (VWActualesTiposTrabajadores) query.getSingleResult();
         return vwActualesTiposTrabajadores;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaVWActualesTiposTrabajadores.buscarTipoTrabajadorCodigoEmpl() ERROR : " + e);
         return null;
      }
   }

   @Override
   public List<VWActualesTiposTrabajadores> FiltrarTipoTrabajador(EntityManager em, String p_tipo) {
      try {
         em.clear();
         if (!p_tipo.isEmpty()) {
            List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadoresLista = (List<VWActualesTiposTrabajadores>) em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt WHERE vwatt.tipoTrabajador.tipo = :tipotrabajador")
                    .setParameter("tipotrabajador", p_tipo)
                    .getResultList();
            return vwActualesTiposTrabajadoresLista;
         } else {
            log.error("Error en PersistenciaVWActualesTiposTrabajadores.FiltrarTipoTrabajador. "
                    + "No recibió el parametro");
            List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadores = null;
            return vwActualesTiposTrabajadores;
         }
      } catch (Exception e) {
         List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadores = null;
         return vwActualesTiposTrabajadores;
      }
   }

   @Override
   public VWActualesTiposTrabajadores filtrarTipoTrabajadorPosicion(EntityManager em, String p_tipo, int posicion) {
      try {
         em.clear();
         if (!p_tipo.isEmpty()) {
            Query query = em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt, Empleados e"
                    + " WHERE vwatt.tipoTrabajador.tipo = :tipotrabajador AND vwatt.empleado.secuencia = e.secuencia");
            query.setParameter("tipotrabajador", p_tipo);
            query.setFirstResult(posicion);
            query.setMaxResults(1);
            VWActualesTiposTrabajadores vwActualesTiposTrabajadores = (VWActualesTiposTrabajadores) query.getSingleResult();
            return vwActualesTiposTrabajadores;
         } else {
            log.error("Error en PersistenciaVWActualesTiposTrabajadores.filtrarTipoTrabajadorPosicion. " + "No recibió el parametro");
            return null;
         }
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public int obtenerTotalRegistrosTipoTrabajador(EntityManager em, String p_tipo) {
      try {
         em.clear();
         if (!p_tipo.isEmpty()) {
            Query query = em.createQuery("SELECT COUNT(vwatt) FROM VWActualesTiposTrabajadores vwatt, Empleados e"
                    + " WHERE vwatt.tipoTrabajador.tipo = :tipotrabajador AND vwatt.empleado.secuencia = e.secuencia");
            query.setParameter("tipotrabajador", p_tipo);
            Long totalRegistros = (Long) query.getSingleResult();
//            log.error("Valor total Registros: " + totalRegistros);
//            log.error("Tipo: " + p_tipo);
            return totalRegistros.intValue();
         } else {
            log.error("Error en PersistenciaVWActualesTiposTrabajadores.obtenerTotalRegistrosTipoTrabajador. " + "No recibió el parametro");
            return 0;
         }
      } catch (Exception e) {
         log.error("PersistenciaVWActualesTiposTrabajadores.obtenerTotalRegistrosTipoTrabajador() ERROR: " + e);
         e.printStackTrace();
         return 0;
      }
   }

   @Override
   public List<VWActualesTiposTrabajadores> busquedaRapidaTrabajadores(EntityManager em) {
      try {
         em.clear();
         List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadoresLista = (List<VWActualesTiposTrabajadores>) em.createQuery("SELECT vwatt FROM VWActualesTiposTrabajadores vwatt")
                 .getResultList();
         return vwActualesTiposTrabajadoresLista;
      } catch (Exception e) {
         log.error("PersistenciaVWActualesTiposTrabajadores.busquedaRapidaTrabajadores() ERROR: " + e);
         List<VWActualesTiposTrabajadores> vwActualesTiposTrabajadores = null;
         return vwActualesTiposTrabajadores;
      }
   }

   //VALIDACION ARCHIVO PLANO
   @Override
   public boolean verificarTipoTrabajador(EntityManager em, Empleados empleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw.tipoTrabajador.tipo FROM VWActualesTiposTrabajadores vw WHERE vw.empleado.secuencia= :secuencia");
         query.setParameter("secuencia", empleado.getSecuencia());
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         String tipoEmpleado = (String) query.getSingleResult();
         return tipoEmpleado.equalsIgnoreCase("ACTIVO");
      } catch (Exception e) {
         log.error("Exepcion en PersistenciaVWActualesTiposTrabajadores.verificarTipoTrabajador");
         return false;
      }
   }

   @Override
   public String consultarTipoTrabajador(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw.tipoTrabajador.tipo FROM VWActualesTiposTrabajadores vw WHERE vw.empleado.secuencia= :secuencia");
         query.setParameter("secuencia", secEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         String tipoEmpleado = (String) query.getSingleResult();
         return tipoEmpleado;
      } catch (Exception e) {
         log.error("Exepcion en PersistenciaVWActualesTiposTrabajadores.consultarTipoTrabajador");
         return "";
      }
   }

   @Override
   public Date consultarFechaVigencia(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesTiposTrabajadores vw WHERE vw.empleado.secuencia= :secuencia");
         query.setParameter("secuencia", secEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VWActualesTiposTrabajadores objfecha = (VWActualesTiposTrabajadores) query.getSingleResult();
         log.error("PersistenciaVWActualesTiposTrabajadores.consultarFechaVigencia objfecha : " + objfecha);
         Date fecha = objfecha.getFechaVigencia();
         log.error("PersistenciaVWActualesTiposTrabajadores.consultarFechaVigencia fecha : " + fecha);
         return fecha;
      } catch (Exception e) {
         log.error("ERROR: PersistenciaVWActualesTiposTrabajadores.consultarFechaVigencia : " + e.getCause());
         return null;
      }
   }

   @Override
   public List<VWActualesTiposTrabajadores> tipoTrabajadorEmpleado(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vw FROM VWActualesTiposTrabajadores vw where vw.tipoTrabajador.tipo IN ('ACTIVO','PENSIONADO','RETIRADO')");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VWActualesTiposTrabajadores> tipoEmpleado = query.getResultList();
         log.error("Tiene: " + tipoEmpleado.size() + " registros");
         return tipoEmpleado;
      } catch (Exception e) {
         log.error("Exepcion en PersistenciaVWActualesTiposTrabajadores.tipoTrabajadorEmpleado" + e);
         return null;
      }
   }
}
