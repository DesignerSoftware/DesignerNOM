/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PlantillasValidaTC;
import InterfacePersistencia.PersistenciaPlantillasValidaTCInterface;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Stateless
public class PersistenciaPlantillasValidaTC implements PersistenciaPlantillasValidaTCInterface {

    @Override
    public void crear(EntityManager em, PlantillasValidaTC plantillatc) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillatc);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaTC.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PlantillasValidaTC plantillatc) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(plantillatc);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaTC.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PlantillasValidaTC plantillatc) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(plantillatc));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPlantillasValidaTC.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
