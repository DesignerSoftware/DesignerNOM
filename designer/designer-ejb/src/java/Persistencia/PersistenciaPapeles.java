/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Papeles;
import InterfacePersistencia.PersistenciaPapelesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPapeles implements PersistenciaPapelesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    public void crear(EntityManager em, Papeles papel) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(papel);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPapeles.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, Papeles papel) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(papel);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPapeles.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, Papeles papel) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(papel));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPapeles.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public List<Papeles> consultarPapeles(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Papeles p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Papeles> listaPapeles = (List<Papeles>) query.getResultList();
            return listaPapeles;
        } catch (Exception e) {
            System.err.println("ERROR PERSISTENCIAPAPELES AL CONSULTARPAPELES ERROR : " + e);
            return null;
        }
    }

    public Papeles consultarPapel(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT papelillo FROM Papeles papelillo WHERE papelillo.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Papeles papel = (Papeles) query.getSingleResult();
            return papel;
        } catch (Exception e) {
            System.err.println("ERROR PERSISTENCIAPAPELES AL CONSULTARPAPEL");
            Papeles papel = null;
            return papel;
        }
    }

    public List<Papeles> consultarPapelesEmpresa(EntityManager em, BigInteger secEmpresa) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT papelillo FROM Papeles papelillo WHERE papelillo.empresa.secuencia = :secuenciaEmpr ORDER BY papelillo.codigo ASC");
            query.setParameter("secuenciaEmpr", secEmpresa);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Papeles> centrosCostos = query.getResultList();
            return centrosCostos;
        } catch (Exception e) {
            System.err.println("ERROR PERSISTENCIAPAPELES AL CONSULTARPAPELESEMPRESA ERROR " + e);
            return null;
        }
    }

    public BigInteger contarVigenciasCargosPapel(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM vigenciascargos WHERE papel = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            System.out.println("PERSISTENCIAPAPELES CONTARVIGENCIASCARGOSPAPEL CONTARDOR" + query.getSingleResult());
            return new BigInteger(query.getSingleResult().toString());

        } catch (Exception e) {
            System.err.println("ERROR PERSISTENCIAPAPELES CONTARVIGENCIASCARGOSPAPEL ERROR : " + e);
            return null;
        }
    }

}
