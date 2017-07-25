/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposBloques;
import InterfacePersistencia.PersistenciaTiposBloquesInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaTiposBloques implements PersistenciaTiposBloquesInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposBloques.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
    */
   @Override
   public void crear(EntityManager em, TiposBloques tiposBloques) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposBloques);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPensionados.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TiposBloques tiposBloques) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposBloques);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPensionados.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TiposBloques tiposBloques) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposBloques);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaPensionados.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TiposBloques> tiposBloques(EntityManager em, BigInteger secuenciaOperando) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT tf FROM TiposBloques tf, Operandos op WHERE tf.operando.secuencia = op.secuencia AND tf.operando.secuencia =:secuenciaOperando AND op.tipo='BLOQUE PL/SQL' ORDER BY tf.fechafinal DESC");
         query.setParameter("secuenciaOperando", secuenciaOperando);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TiposBloques> tiposBloquesResult = new ArrayList<TiposBloques>();
         tiposBloquesResult = query.getResultList();
         return tiposBloquesResult;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaTiposBloques.tiposBloques()" + e.getMessage());
         return null;
      }
   }
}
