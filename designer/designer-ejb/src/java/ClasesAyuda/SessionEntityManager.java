/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClasesAyuda;

import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrador
 */
public class SessionEntityManager {

   private String idSession;
   private EntityManagerFactory emf;
//   private EntityManager em;

   public SessionEntityManager(String idSession, EntityManagerFactory emf) {
      this.idSession = idSession;
      this.emf = emf;
   }

   public String getIdSession() {
      return idSession;
   }

   public void setIdSession(String idSession) {
      this.idSession = idSession;
   }

   public EntityManagerFactory getEmf() {
      return emf;
   }

   public void setEmf(EntityManagerFactory emf) {
      this.emf = emf;
   }
//
//   public EntityManager getEm() {
//      em = emf.createEntityManager();
//      return em;
//   }
//
//   public void setEm(EntityManager em) {
//      this.em = em;
//   }
}
