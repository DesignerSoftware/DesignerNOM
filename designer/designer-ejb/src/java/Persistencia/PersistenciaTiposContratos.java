/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposContratos;
import InterfacePersistencia.PersistenciaTiposContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposContratos implements PersistenciaTiposContratosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposContratos.class);

   @Override
   public void crear(EntityManager em, TiposContratos tiposContratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(tiposContratos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposContratos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TiposContratos tiposContratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposContratos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposContratos.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TiposContratos tiposContratos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tiposContratos));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposContratos.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }

   }

   @Override
   public TiposContratos buscarTipoContratoSecuencia(EntityManager em, BigInteger secuencia) {

      try {
         em.clear();
         Query query = em.createQuery("SELECT e FROM TiposContratos e WHERE e.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         TiposContratos tipoC = (TiposContratos) query.getSingleResult();
         return tipoC;
      } catch (Exception e) {
         log.error("PersistenciaTiposContratos.buscarTipoContratoSecuencia():  ", e);
         return null;
      }
   }

   @Override
   public List<TiposContratos> tiposContratos(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT tc FROM TiposContratos tc ORDER BY tc.codigo");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TiposContratos> tiposContratos = query.getResultList();
         return tiposContratos;
      } catch (Exception e) {
         log.error("PersistenciaTiposContratos.tiposContratos():  ", e);
         return null;
      }
   }

   @Override
   public void clonarTipoContrato(BigInteger secuenciaClonado, String nuevoNombre, Short nuevoCodigo) {
      try {
         log.error("No esta clonando nada");
      } catch (Exception e) {
         log.error("Error en clonarTipoContrato :  ", e);
      }
   }
}
