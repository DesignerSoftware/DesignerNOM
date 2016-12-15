/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.CambiosMasivos;
import Entidades.ParametrosCambiosMasivos;
import InterfacePersistencia.PersistenciaCambiosMasivosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
@LocalBean
public class PersistenciaCambiosMasivos implements PersistenciaCambiosMasivosInterface {
//
//   @Override
//   public void crearCambiosMasivos(EntityManager em, CambiosMasivos cambioMasivo) {
//      em.clear();
//      EntityTransaction tx = em.getTransaction();
//      try {
//         tx.begin();
//         em.persist(cambioMasivo);
//         tx.commit();
//      } catch (Exception e) {
//         System.out.println("Error PersistenciaCambiosMasivos.crear: " + e);
//         if (tx.isActive()) {
//            tx.rollback();
//         }
//      }
//   }
//
//   @Override
//   public void editarCambiosMasivos(EntityManager em, CambiosMasivos cambioMasivo) {
//      em.clear();
//      EntityTransaction tx = em.getTransaction();
//      try {
//         tx.begin();
//         em.merge(cambioMasivo);
//         tx.commit();
//      } catch (Exception e) {
//         System.out.println("Error PersistenciaCambiosMasivos.editar: " + e);
//         if (tx.isActive()) {
//            tx.rollback();
//         }
//      }
//   }
//
//   @Override
//   public void borrarCambiosMasivos(EntityManager em, CambiosMasivos cambioMasivo) {
//      em.clear();
//      EntityTransaction tx = em.getTransaction();
//      try {
//         tx.begin();
//         em.remove(em.merge(cambioMasivo));
//         tx.commit();
//      } catch (Exception e) {
//         if (tx.isActive()) {
//            tx.rollback();
//         }
//         System.out.println("Error PersistenciaCambiosMasivos.borrar: " + e);
//      }
//   }
//
//   @Override
//   public CambiosMasivos buscarCambioMasivoSecuencia(EntityManager em, BigInteger secuencia) {
//      try {
//         em.clear();
//         return em.find(CambiosMasivos.class, secuencia);
//      } catch (Exception e) {
//         System.out.println("Error PersistenciaCambiosMasivos.buscarCambioMasivoSecuencia(): " + e);
//         return null;
//      }
//   }
//
//   @Override
//   public List<CambiosMasivos> consultarCambiosMasivos(EntityManager em) {
//      try {
//         em.clear();
//         Query query = em.createNativeQuery("SELECT CM.* FROM CAMBIOSMASIVOS CM \n"
//                 + "WHERE EXISTS(SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA = CM.EMPLEADO)", CambiosMasivos.class);
//         List<CambiosMasivos> lista = query.getResultList();
//         return lista;
//      } catch (Exception e) {
//         System.out.println("Error PersistenciaCambiosMasivos.consultarCambiosMasivos: " + e);
//         return null;
//      }
//   }
//
//   public ParametrosCambiosMasivos consultarParametroCambiosMasivos(EntityManager em, String usuario) {
//      try {
//         em.clear();
//         Query query = em.createNativeQuery("SELECT * FROM PARAMETROSCAMBIOSMASIVOS WHERE usuariobd = '" + usuario + "'", ParametrosCambiosMasivos.class);
//         ParametrosCambiosMasivos parametro = (ParametrosCambiosMasivos) query.getSingleResult();
//         return parametro;
//      } catch (Exception e) {
//         System.out.println("Error PersistenciaCambiosMasivos.consultarParametroCambiosMasivos: " + e);
//         return null;
//      }
//   }
}
