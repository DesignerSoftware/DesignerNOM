/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposRedondeos;
import InterfacePersistencia.PersistenciaTiposRedondeosInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.criteria.CriteriaQuery;

@Stateless
public class PersistenciaTiposRedondeos implements PersistenciaTiposRedondeosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposRedondeos.class);

   @Override
   public List<TiposRedondeos> buscarTiposRedondeos(EntityManager em) {
      try {
         em.clear();
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(TiposRedondeos.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("Error buscarTiposRedondeos PersistenciaTiposRedondeos :  ", e);
         return null;
      }
   }
}
