/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ParametrosInformes;
import InterfacePersistencia.PersistenciaParametrosInformesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'ParametrosInformes'
 * de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaParametrosInformes implements PersistenciaParametrosInformesInterface {

   private static Logger log = Logger.getLogger(PersistenciaParametrosInformes.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

   @Override
   public void crear(EntityManager em, ParametrosInformes parametrosInformes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(parametrosInformes);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaParametrosInformes.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, ParametrosInformes parametrosInformes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(parametrosInformes);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaParametrosInformes.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, ParametrosInformes parametrosInformes) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(parametrosInformes));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaParametrosInformes.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public ParametrosInformes buscarParametroInforme(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(ParametrosInformes.class, secuencia);
      } catch (Exception e) {
         log.error("Error buscarParametroInforme Persistencia" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<ParametrosInformes> buscarParametrosInformes(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(ParametrosInformes.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Error buscarParametrosInformes" + e.getMessage());
         return null;
      }
   }

   @Override
   public ParametrosInformes buscarParametroInformeUsuario(EntityManager em, String alias) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT pi FROM ParametrosInformes pi WHERE pi.usuario =:Alias");
         query.setParameter("Alias", alias);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         ParametrosInformes parametrosInformes = (ParametrosInformes) query.getSingleResult();
         return parametrosInformes;
      } catch (Exception e) {
         log.error("Error en buscarParametroInformeUsuario " + e.getMessage());
         return null;
      }
   }
}
