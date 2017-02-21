/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaEnfoquesInterface;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import Entidades.Enfoques;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaEnfoques implements PersistenciaEnfoquesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    public void crear(EntityManager em, Enfoques enfoques) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(enfoques);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEnfoques.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, Enfoques enfoques) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(enfoques);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEnfoques.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, Enfoques enfoques) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(enfoques));
            tx.commit();

        } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                System.out.println("Error PersistenciaEnfoques.borrar: " + e);
        }
    }

    public Enfoques buscarEnfoque(EntityManager em, BigInteger secuenciaEnfoques) {
        try {
            em.clear();
            return em.find(Enfoques.class, secuenciaEnfoques);
        } catch (Exception e) {
            System.err.println("ERROR PersistenciaEnfoques buscarEnfoque ERROR " + e);
            return null;
        }
    }

    public List<Enfoques> buscarEnfoques(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Enfoques.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("\n ERROR EN PersistenciaEnfoques buscarEnfoques ERROR" + e);
            return null;
        }
    }

    public BigInteger contadorTiposDetalles(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM tiposdetalles td , enfoques eee WHERE eee.secuencia=td.enfoque AND eee.secuencia  = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("PERSISTENCIAENFOQUES contadorTiposDetalles = " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("ERROR PERSISTENCIAENFOQUES contadorTiposDetalles  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
