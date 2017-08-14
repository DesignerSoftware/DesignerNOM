/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaPartesCuerpoInterface;
import Entidades.PartesCuerpo;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'PartesCuerpo' de la
 * base de datos
 *
 * @author John Pineda.
 */
@Stateless
public class PersistenciaPartesCuerpo implements PersistenciaPartesCuerpoInterface {

   private static Logger log = Logger.getLogger(PersistenciaPartesCuerpo.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public void crear(EntityManager em, PartesCuerpo partesCuerpo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(partesCuerpo);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPartesCuerpo.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, PartesCuerpo partesCuerpo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(partesCuerpo);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPartesCuerpo.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, PartesCuerpo partesCuerpo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(partesCuerpo));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPartesCuerpo.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public PartesCuerpo buscarParteCuerpo(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(PartesCuerpo.class, secuencia);
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaPartesCuerpo.buscarParteCuerpo()" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<PartesCuerpo> buscarPartesCuerpo(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT l FROM PartesCuerpo  l ORDER BY l.codigo ASC ");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<PartesCuerpo> listPartesCuerpo = query.getResultList();
         return listPartesCuerpo;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaPartesCuerpo.buscarPartesCuerpo()" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorSoAccidentesMedicos(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM soaccidentesmedicos so WHERE so.parte = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = (BigInteger) query.getSingleResult();
         return retorno;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaPartesCuerpo.contadorSoAccidentesMedicos()" + e.getMessage());
         return retorno;
      }
   }

   @Override
   public BigInteger contadorDetallesExamenes(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM sodetallesexamenes se WHERE se.partecuerpo = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = (BigInteger) query.getSingleResult();
         return retorno;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaPartesCuerpo.contadorDetallesExamenes()" + e.getMessage());
         return retorno;
      }
   }

   @Override
   public BigInteger contadorSoDetallesRevisiones(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM sodetallesrevisiones sr WHERE sr.organo = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = (BigInteger) query.getSingleResult();
         log.warn("PARTESCUERPO CONTADOR SO DETALLES REVISIONES  " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaPartesCuerpo.contadorSoDetallesRevisiones()" + e.getMessage());
         return retorno;
      }
   }
}
