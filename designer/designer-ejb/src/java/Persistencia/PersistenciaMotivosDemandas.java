/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MotivosDemandas;
import InterfacePersistencia.PersistenciaMotivosDemandasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosDemandas' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaMotivosDemandas implements PersistenciaMotivosDemandasInterface {

   private static Logger log = Logger.getLogger(PersistenciaMotivosDemandas.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public void crear(EntityManager em, MotivosDemandas motivosDemandas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(motivosDemandas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaMotivosDemandas.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, MotivosDemandas motivosDemandas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(motivosDemandas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaMotivosDemandas.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, MotivosDemandas motivosDemandas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(motivosDemandas));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaMotivosDemandas.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   public MotivosDemandas buscarMotivoDemanda(EntityManager em, BigInteger secuenciaE) {
      try {
         em.clear();
         return em.find(MotivosDemandas.class, secuenciaE);
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaMotivosDemandas.buscarMotivoDemanda()" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<MotivosDemandas> buscarMotivosDemandas(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT g FROM MotivosDemandas g ORDER BY g.codigo ASC ");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<MotivosDemandas> motivosDemandas = query.getResultList();
         return motivosDemandas;
      } catch (Exception e) {
         log.error("Error PersistenciaMotivosDemandas.buscarMotivosDemandas" + e.getMessage());
         return null;
      }
   }

   public BigInteger contadorDemandas(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM demandas WHERE motivo = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         log.warn("PersistenciaMotivosDemana Contador " + retorno);
         return retorno;
      } catch (Exception e) {
         log.error("Error  PersistenciaMotivosDemanas contadorDemanas error " + e.getMessage());
         return retorno;
      }
   }
}
