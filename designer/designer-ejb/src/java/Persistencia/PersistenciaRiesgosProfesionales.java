/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.RiesgosProfesionales;
import InterfacePersistencia.PersistenciaRiesgosProfesionalesInterface;
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
public class PersistenciaRiesgosProfesionales implements PersistenciaRiesgosProfesionalesInterface {

    @Override
    public void crear(EntityManager em, RiesgosProfesionales riesgop) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(riesgop);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRiesgosProfesionales.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, RiesgosProfesionales riesgop) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(riesgop);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRiesgosProfesionales.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, RiesgosProfesionales riesgop) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(riesgop));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRiesgosProfesionales.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<RiesgosProfesionales> riesgosProfesionales(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM RIESGOSPROFESIONALES ORDER BY FECHAVIGENCIA";
            Query queryFinal = em.createNativeQuery(sql, RiesgosProfesionales.class);
            List<RiesgosProfesionales> listaRiesgos = queryFinal.getResultList();
            return listaRiesgos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaRiesgosProfesionales riesgosProfesionales : " + e.getMessage());
            return null;
        }
    }

}
