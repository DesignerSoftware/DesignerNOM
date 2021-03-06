/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasDomiciliarias;
import InterfacePersistencia.PersistenciaVigenciasDomiciliariasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'VigenciasDomiciliarias' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasDomiciliarias implements PersistenciaVigenciasDomiciliariasInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasDomiciliarias.class);

   @Override
   public List<VigenciasDomiciliarias> visitasDomiciliariasPersona(EntityManager em, BigInteger secuenciaPersona) {
      try {
         em.clear();
         String sql = "SELECT * FROM VIGENCIASDOMICILIARIAS WHERE PERSONA = ?";
         Query query = em.createNativeQuery(sql, VigenciasDomiciliarias.class);
         query.setParameter(1, secuenciaPersona);
         List<VigenciasDomiciliarias> listVisitas = query.getResultList();
         return listVisitas;
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasDomiciliarias.visitasDomiciliariasPersona ", e);
         return null;
      }
   }

   @Override
   public void crear(EntityManager em, VigenciasDomiciliarias visita) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(visita);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasDomiciliarias.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasDomiciliarias visita) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(visita));
         tx.commit();

      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            log.error("Error PersistenciaVigenciasDomiciliarias.borrar:  ", e);
         }
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasDomiciliarias visita) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(visita);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasDomiciliarias.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public VigenciasDomiciliarias actualVisitaDomiciliariaPersona(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         String sql = "SELECT * FROM VIGENCIASDOMICILIARIAS WHERE FECHA= (SELECT MAX(vdo.fecha) FROM VigenciasDomiciliarias vdo WHERE vdo.persona = ?) and persona = ?";
         Query query = em.createNativeQuery(sql, VigenciasDomiciliarias.class);
         query.setParameter(1, secuencia);
         query.setParameter(2, secuencia);
         VigenciasDomiciliarias vigenciaActual = (VigenciasDomiciliarias) query.getSingleResult();
         return vigenciaActual;

      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasDomiciliarias.actualVisitaDomiciliariaPersona ", e);
         return null;
      }
   }

   @Override
   public String primeraVigenciaDomiciliaria(EntityManager em, BigInteger secuencia) {
      String visita;
      try {
         em.clear();
         String sql = "SELECT DECODE(AUX.NOMBRE,NULL,'','VISITADO EL'||' '||TO_CHAR(AUX.FECHA,'DD-MM-YYYY'))\n"
                 + "	 FROM (SELECT V.persona NOMBRE,MAX(V.fecha)FECHA\n"
                 + "	 FROM PERSONAS P,VIGENCIASDOMICILIARIAS V\n"
                 + "	 WHERE  P.SECUENCIA =V.persona(+) AND P.secuencia= ? AND ROWNUM = 1 \n"
                 + "	 GROUP BY V.persona) AUX";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuencia);
         visita = (String) query.getSingleResult();
         if (visita == null) {
            visita = "";
         }
         return visita;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("PersistenciaVigenciasDomiciliarias.primeraVigenciaDomiciliaria(): " + e);
         } else {
            log.error("PersistenciaVigenciasDomiciliarias.primeraVigenciaDomiciliaria():  ", e);
         }
         return "";
      }
   }
}
