/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ParametrosConjuntos;
import InterfacePersistencia.PersistenciaParametrosConjuntosInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaParametrosConjuntos implements PersistenciaParametrosConjuntosInterface {

    @Override
    public void crearParametros(EntityManager em, ParametrosConjuntos parametrosConjuntos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(parametrosConjuntos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosConjuntos.crearParametros : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editarParametros(EntityManager em, ParametrosConjuntos parametrosConjuntos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(parametrosConjuntos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosConjuntos.editarParametros : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrarParametros(EntityManager em, ParametrosConjuntos parametrosConjuntos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(parametrosConjuntos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosConjuntos.borrarParametros : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public ParametrosConjuntos consultarParametros(EntityManager em) {
        try {
            em.clear();
            Query query = em.createNativeQuery("SELECT * FROM PARAMETROSCONJUNTOS p WHERE p.USUARIOBD = USER", ParametrosConjuntos.class);
//            Query query = em.createQuery("SELECT p FROM ParametrosConjuntos p WHERE p.usuarioBD = USER", ParametrosConjuntos.class);
//            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            ParametrosConjuntos pc = (ParametrosConjuntos) query.getSingleResult();
            System.out.println("PersistenciaParametrosConjuntos.consultarParametros pc : " + pc);
            return pc;
        } catch (Exception e) {
            System.err.println("Error PersistenciaParametrosConjuntos.consultarParametros : " + e.toString());
            return null;
        }
    }
}
