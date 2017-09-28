/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PlantillasValidaRL;
import InterfacePersistencia.PersistenciaPlantillasValidaRLInterface;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPlantillasValidaRL  implements PersistenciaPlantillasValidaRLInterface {

   private static Logger log = Logger.getLogger(PersistenciaPlantillasValidaRL.class);

    @Override
    public void crear(EntityManager em, PlantillasValidaRL plantillarl) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillarl);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasValidaRL.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PlantillasValidaRL plantillarl) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillarl);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasValidaRL.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PlantillasValidaRL plantillarl) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(plantillarl));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasValidaRL.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
