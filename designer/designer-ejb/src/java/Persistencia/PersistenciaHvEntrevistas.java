/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.HVHojasDeVida;
import Entidades.HvEntrevistas;
import InterfacePersistencia.PersistenciaHvEntrevistasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'HvEntrevistas' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaHvEntrevistas implements PersistenciaHvEntrevistasInterface {

   private static Logger log = Logger.getLogger(PersistenciaHvEntrevistas.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   public void crear(EntityManager em, HvEntrevistas hvEntrevistas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(hvEntrevistas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaHvEntrevistas.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public void editar(EntityManager em, HvEntrevistas hvEntrevistas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(hvEntrevistas);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaHvEntrevistas.editar: " + e);
      }
   }

   public void borrar(EntityManager em, HvEntrevistas hvEntrevistas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(hvEntrevistas));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaHvEntrevistas.borrar: " + e);
      }
   }

   public HvEntrevistas buscarHvEntrevista(EntityManager em, BigInteger secuenciaHvEntrevista) {
      try {
         em.clear();
         return em.find(HvEntrevistas.class, secuenciaHvEntrevista);
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaHvEntrevistas.buscarHvEntrevista() e: " + e);
         return null;
      }
   }

   public List<HvEntrevistas> buscarHvEntrevistas(EntityManager em) {
      em.clear();
      Query query = em.createQuery("SELECT te FROM HvEntrevistas te ORDER BY te.fecha ASC ");
      query.setHint("javax.persistence.cache.storeMode", "REFRESH");
      List<HvEntrevistas> listHvEntrevistas = query.getResultList();
      return listHvEntrevistas;

   }

   public List<HvEntrevistas> buscarHvEntrevistasPorEmpleado(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT he FROM HVHojasDeVida hv , HvEntrevistas he , Empleados e WHERE hv.secuencia = he.hojadevida.secuencia AND e.persona = hv.persona.secuencia AND e.secuencia = :secuenciaEmpl ORDER BY he.fecha ");
         query.setParameter("secuenciaEmpl", secEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<HvEntrevistas> listHvEntrevistas = query.getResultList();
         return listHvEntrevistas;
      } catch (Exception e) {
         log.error("Error en PERSISTENCIAHVENTREVISTAS ERROR " + e);
         return null;
      }
   }

   public List<HVHojasDeVida> buscarHvHojaDeVidaPorPersona(EntityManager em, BigInteger secPersona) {
      try {
         em.clear();
         String sql = "SELECT * FROM HVHOJASDEVIDA hv , PERSONAS p WHERE p.secuencia= hv.persona AND p.secuencia = ?";
         Query query = em.createNativeQuery(sql, HVHojasDeVida.class);
         query.setParameter(1, secPersona);
         List<HVHojasDeVida> hvHojasDeVIda = query.getResultList();
         return hvHojasDeVIda;
      } catch (Exception e) {
         log.error("Error en Persistencia HvEntrevistas buscarHvHojaDeVidaPorEmpleado " + e);
         return null;
      }
   }

   @Override
   public List<HvEntrevistas> entrevistasPersona(EntityManager em, BigInteger secuenciaHV) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(hve) FROM HvEntrevistas hve WHERE hve.hojadevida.secuencia = :secuenciaHV");
         query.setParameter("secuenciaHV", secuenciaHV);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         if (resultado > 0) {
            Query queryFinal = em.createQuery("SELECT hve FROM HvEntrevistas hve WHERE hve.hojadevida.secuencia = :secuenciaHV and hve.fecha = (SELECT MAX(hven.fecha) FROM HvEntrevistas hven WHERE hven.hojadevida.secuencia = :secuenciaHV)");
            queryFinal.setParameter("secuenciaHV", secuenciaHV);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<HvEntrevistas> listaHvEntrevistas = queryFinal.getResultList();
            return listaHvEntrevistas;
         }
         return null;
      } catch (Exception e) {
         log.error("Error PersistenciaHvEntrevistas.entrevistasPersona" + e);
         return null;
      }
   }

   @Override
   public String consultarPrimeraEnterevista(EntityManager em, BigInteger secuenciaHV) {
      String entrevista;
      try {
         em.clear();
         String sql = "SELECT E.NOMBRE||' '||TO_CHAR(E.FECHA,'DD-MM-YYYY')\n"
                 + "   FROM  HVENTREVISTAS E\n"
                 + "   WHERE E.HOJADEVIDA = ?\n"
                 + "   AND FECHA= (SELECT MAX(EI.FECHA) FROM HVENTREVISTAS EI WHERE EI.HOJADEVIDA = E.HOJADEVIDA)";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuenciaHV);
         entrevista = (String) query.getSingleResult();
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("Persistencia.PersistenciaHvEntrevistas.consultarPrimeraEnterevista() e: " + e);
         } else {
            log.error("Persistencia.PersistenciaHvEntrevistas.consultarPrimeraEnterevista() e: " + e);
         }
         entrevista = "";
      }
      return entrevista;
   }
}
