/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PlantillasValidaRL;
import InterfacePersistencia.PersistenciaPlantillasValidaRLInterface;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPlantillasValidaRL  implements PersistenciaPlantillasValidaRLInterface{

    @Override
    public void crear(EntityManager em, PlantillasValidaRL plantillarl) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillarl);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaRL.crear: " + e.getMessage());
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
            System.out.println("Error PersistenciaPlantillasValidaRL.editar: " + e.getMessage());
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
            System.out.println("Error PersistenciaPlantillasValidaRL.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
