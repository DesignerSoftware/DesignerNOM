/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PlantillasValidaNL;
import InterfacePersistencia.PersistenciaPlantillasValidaNLInterface;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPlantillasValidaNL implements PersistenciaPlantillasValidaNLInterface {

    @Override
    public void crear(EntityManager em, PlantillasValidaNL plantillanl) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillanl);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaNL.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PlantillasValidaNL plantillanl) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillanl);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaNL.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PlantillasValidaNL plantillanl) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(plantillanl));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaNL.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
