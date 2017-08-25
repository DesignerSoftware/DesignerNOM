/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasIndicadores;
import InterfacePersistencia.PersistenciaVigenciasIndicadoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaVigenciasIndicadores implements PersistenciaVigenciasIndicadoresInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasIndicadores.class);

   @Override
   public void crear(EntityManager em, VigenciasIndicadores vigenciasIndicadores) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(vigenciasIndicadores);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasIndicadores.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasIndicadores vigenciasIndicadores) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasIndicadores);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasIndicadores.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasIndicadores vigenciasIndicadores) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasIndicadores));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasIndicadores.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<VigenciasIndicadores> buscarVigenciasIndicadores(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vi FROM VigenciasIndicadores vi ORDER BY vi.secuencia");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasIndicadores> vigenciasIndicadores = query.getResultList();
         return vigenciasIndicadores;
      } catch (Exception e) {
         log.error("Error buscarVigenciasIndicadores PersistenciaVigenciasIndicadores : " + e.getMessage());
         return null;
      }
   }

   @Override
   public VigenciasIndicadores buscarVigenciaIndicadorSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vi FROM VigenciasIndicadores vi WHERE vi.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VigenciasIndicadores vigenciasIndicadores = (VigenciasIndicadores) query.getSingleResult();
         return vigenciasIndicadores;
      } catch (Exception e) {
         log.error("Error buscarVigenciaIndicadorSecuencia PersistenciaVigenciasIndicadores : " + e.getMessage());
         return null;
      }
   }

   @Override
   public List<VigenciasIndicadores> ultimosIndicadoresEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(vi) FROM VigenciasIndicadores vi WHERE vi.empleado.secuencia = :secuenciaEmpl");
         query.setParameter("secuenciaEmpl", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         if (resultado > 0) {
            Query queryFinal = em.createQuery("SELECT vi FROM VigenciasIndicadores vi WHERE vi.empleado.secuencia = :secuenciaEmpl and vi.fechainicial = (SELECT MAX(vin.fechainicial) FROM VigenciasIndicadores vin WHERE vin.empleado.secuencia = :secuenciaEmpl)");
            queryFinal.setParameter("secuenciaEmpl", secuencia);
            List<VigenciasIndicadores> listaVigenciasIndicadores = queryFinal.getResultList();
            return listaVigenciasIndicadores;
         }
         return null;
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasIndicadores.indicadoresPersona" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<VigenciasIndicadores> indicadoresTotalesEmpleadoSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query queryFinal = em.createQuery("SELECT vi FROM VigenciasIndicadores vi WHERE vi.empleado.secuencia = :secuenciaEmpl");
         queryFinal.setParameter("secuenciaEmpl", secuencia);
         queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasIndicadores> listaVigenciasIndicadores = queryFinal.getResultList();
         return listaVigenciasIndicadores;
      } catch (Exception e) {
         log.error("Error indicadoresTotalesEmpleadoSecuencia : " + e.getMessage());
         return null;
      }
   }

   @Override
   public String primeraVigenciaIndicador(EntityManager em, BigInteger secuencia) {
      String indicador;
      try {
         em.clear();
         String sql = "SELECT SUBSTR(B.DESCRIPCION||' '||TO_CHAR(A.FECHAINICIAL,'DD-MM-YYYY'),1,30)\n"
                 + "   FROM VIGENCIASINDICADORES A, INDICADORES B\n"
                 + "   WHERE A.EMPLEADO = (select secuencia from empleados where persona=?) AND\n"
                 + "   A.INDICADOR = B.SECUENCIA \n"
                 + "   AND A.FECHAINICIAL = (SELECT MAX(V.FECHAINICIAL) FROM VIGENCIASINDICADORES V WHERE V.EMPLEADO = A.EMPLEADO) AND ROWNUM = 1";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuencia);
         indicador = (String) query.getSingleResult();
         if (indicador == null) {
            indicador = "";
         }
         return indicador;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("Persistencia.PersistenciaVigenciasIndicadores.primeraVigenciaIndicador()" + e.getMessage());
         } else {
            log.error("Persistencia.PersistenciaVigenciasIndicadores.primeraVigenciaIndicador()" + e.getMessage());
         }
         return "";
      }
   }
}
