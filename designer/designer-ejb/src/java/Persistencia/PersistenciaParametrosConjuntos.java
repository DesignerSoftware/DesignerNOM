/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ParametrosConjuntos;
import InterfacePersistencia.PersistenciaParametrosConjuntosInterface;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaParametrosConjuntos implements PersistenciaParametrosConjuntosInterface {

   private static Logger log = Logger.getLogger(PersistenciaParametrosConjuntos.class);

    @Override
    public void crearParametros(EntityManager em, ParametrosConjuntos parametrosConjuntos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(parametrosConjuntos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosConjuntos.crearParametros :  ", e);
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
            log.error("Error PersistenciaParametrosConjuntos.editarParametros :  ", e);
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
            log.error("Error PersistenciaParametrosConjuntos.borrarParametros :  ", e);
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
            ParametrosConjuntos pc = (ParametrosConjuntos) query.getSingleResult();
            log.warn("PersistenciaParametrosConjuntos.consultarParametros pc : " + pc);
            return pc;
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosConjuntos.consultarParametros :  ", e);
            return null;
        }
    }
}
