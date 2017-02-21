/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.FirmasReportes;
import InterfacePersistencia.PersistenciaFirmasReportesInterface;
import java.math.BigInteger;
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
public class PersistenciaFirmasReportes implements PersistenciaFirmasReportesInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   public void crear(EntityManager em, FirmasReportes tiposCursos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposCursos);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaFirmasReportes.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
         e.printStackTrace();
      }
   }

   public void editar(EntityManager em, FirmasReportes tiposCursos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposCursos);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaFirmasReportes.editar: " + e);
      }
   }

   public void borrar(EntityManager em, FirmasReportes tiposCursos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tiposCursos));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaFirmasReportes.borrar: " + e);
      }
   }

   public List<FirmasReportes> consultarFirmasReportes(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT g FROM FirmasReportes g ORDER BY g.codigo ASC ");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List< FirmasReportes> listMotivosDemandas = query.getResultList();
         return listMotivosDemandas;

      } catch (Exception e) {
         System.out.println("Error consultarFirmasReportes PersistenciaFirmasReportes : " + e.toString());
         return null;
      }
   }

   public FirmasReportes consultarFirmaReporte(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT te FROM FirmasReportes te WHERE te.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         FirmasReportes tiposCursos = (FirmasReportes) query.getSingleResult();
         return tiposCursos;
      } catch (Exception e) {
         System.out.println("Error PersistenciaFirmasReportes consultarTipoCurso : " + e.toString());
         FirmasReportes tiposEntidades = null;
         return tiposEntidades;
      }
   }
}
