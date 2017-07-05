/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PlantillasValidaLL;
import InterfacePersistencia.PersistenciaPlantillasValidaLLInterface;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPlantillasValidaLL implements PersistenciaPlantillasValidaLLInterface{

    @Override
    public void crear(EntityManager em, PlantillasValidaLL plantillall) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillall);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaLL.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PlantillasValidaLL plantillall) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillall);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaLL.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PlantillasValidaLL plantillall) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(plantillall));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaLL.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
