/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaTiposCertificadosInterface;
import Entidades.TiposCertificados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;

@Stateless
public class PersistenciaTiposCertificados implements PersistenciaTiposCertificadosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposCertificados.class);

   @Override
   public void crear(EntityManager em, TiposCertificados motivosMvrs) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(motivosMvrs);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposCertificados.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TiposCertificados motivosMvrs) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(motivosMvrs);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposCertificados.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TiposCertificados motivosMvrs) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(motivosMvrs));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposCertificados.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public TiposCertificados buscarTipoCertificado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(TiposCertificados.class, secuencia);
      } catch (Exception e) {
         log.error("PersistenciaTiposCertificados.buscarTipoCertificado():  ", e);
         return null;
      }
   }

   @Override
   public List<TiposCertificados> buscarTiposCertificados(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(TiposCertificados.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("PersistenciaTiposCertificados.buscarTiposCertificados():  ", e);
         return null;
      }
   }
}
