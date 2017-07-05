/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PlantillasValidaTS;
import InterfacePersistencia.PersistenciaPlantillasValidaTSInterface;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPlantillasValidaTS implements PersistenciaPlantillasValidaTSInterface{

    @Override
    public void crear(EntityManager em, PlantillasValidaTS plantillats) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillats);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaTS.crear: " + e.getMessage());
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
            System.out.println("Error PersistenciaPlantillasValidaTS.editar: " + e.getMessage());
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
            System.out.println("Error PersistenciaPlantillasValidaTS.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
