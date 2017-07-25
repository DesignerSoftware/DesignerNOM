/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PlantillasValidaTS;
import InterfacePersistencia.PersistenciaPlantillasValidaTSInterface;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPlantillasValidaTS implements PersistenciaPlantillasValidaTSInterface {

   private static Logger log = Logger.getLogger(PersistenciaPlantillasValidaTS.class);

    @Override
    public void crear(EntityManager em, PlantillasValidaTS plantillats) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillats);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasValidaTS.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PlantillasValidaTS plantillats) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillats);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasValidaTS.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PlantillasValidaTS plantillats) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(plantillats));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPlantillasValidaTS.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
