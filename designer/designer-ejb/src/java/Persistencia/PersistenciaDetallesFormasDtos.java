/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.DetallesFormasDtos;
import InterfacePersistencia.PersistenciaDetallesFormasDtosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaDetallesFormasDtos implements PersistenciaDetallesFormasDtosInterface {

   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
   @Override
   public void crear(EntityManager em, DetallesFormasDtos detallesFormasDtos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(detallesFormasDtos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaDetallesFormasDtos.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, DetallesFormasDtos detallesFormasDtos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(detallesFormasDtos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaDetallesFormasDtos.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, DetallesFormasDtos detallesFormasDtos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(detallesFormasDtos));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaDetallesFormasDtos.borrar: " + e);
      }
   }

   @Override
   public List<DetallesFormasDtos> detallesFormasDescuentos(EntityManager em, BigInteger formaDto) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT dfd FROM DetallesFormasDtos dfd WHERE dfd.formadto.secuencia = :formaDto ORDER BY 2");
         query.setParameter("formaDto", formaDto);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<DetallesFormasDtos> detallesFormasDtos = query.getResultList();
         List<DetallesFormasDtos> detallesFormasDtosResult = new ArrayList<DetallesFormasDtos>(detallesFormasDtos);
         return detallesFormasDtosResult;
      } catch (Exception e) {
         System.out.println("Error: (eersPrestamos)" + e);
         return null;
      }
   }
}
