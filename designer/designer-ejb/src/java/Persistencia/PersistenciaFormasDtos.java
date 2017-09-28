/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.FormasDtos;
import InterfacePersistencia.PersistenciaFormasDtosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaFormasDtos implements PersistenciaFormasDtosInterface {

   private static Logger log = Logger.getLogger(PersistenciaFormasDtos.class);

   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   @Override
   public void crear(EntityManager em, FormasDtos formasDtos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(formasDtos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaFormasDtos.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, FormasDtos formasDtos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(formasDtos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaFormasDtos.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, FormasDtos formasDtos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(formasDtos));
         tx.commit();

      } catch (Exception e) {
         log.error("Error PersistenciaFormasDtos.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<FormasDtos> formasDescuentos(EntityManager em, BigInteger tipoEmbargo) {
      try {
         em.clear();
         String sqlQuery = "SELECT SECUENCIA, DESCRIPCION FROM FORMASDTOS \n"
                 + "WHERE TIPO = 'EMBARGO'\n"
                 + "and nvl(tipoembargo, ?)= ?\n"
                 + "ORDER BY 2";
         Query query = em.createNativeQuery(sqlQuery, FormasDtos.class);
         query.setParameter(1, tipoEmbargo);
         query.setParameter(2, tipoEmbargo);
         List<FormasDtos> formasDtos = query.getResultList();
         List<FormasDtos> formasDtosResult = new ArrayList<FormasDtos>(formasDtos);
         return formasDtosResult;
      } catch (Exception e) {
         log.error("Error: (FormasDtos) ", e);
         return null;
      }
   }
}
