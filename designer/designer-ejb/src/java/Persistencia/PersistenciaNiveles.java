/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Niveles;
import InterfacePersistencia.PersistenciaNivelesInterface;
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
public class PersistenciaNiveles implements PersistenciaNivelesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    public void crear(EntityManager em, Niveles niveles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(niveles);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaNiveles.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, Niveles niveles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(niveles);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaNiveles.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, Niveles niveles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(niveles));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaNiveles.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public Niveles consultarNivel(EntityManager em, BigInteger secNiveles) {
        try {
            em.clear();
            return em.find(Niveles.class, secNiveles);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Niveles> consultarNiveles(EntityManager em) {
        em.clear();
        Query query = em.createQuery("SELECT te FROM Niveles te ORDER BY te.codigo ASC ");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<Niveles> listMotivosDemandas = query.getResultList();
        return listMotivosDemandas;

    }

    public BigInteger contarEvalConvocatoriasNivel(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM evalconvocatorias WHERE nivel = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("Contador PersistenciaNiveles contarEvalConvocatoriasNivel  " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaNiveles  contarEvalConvocatoriasNivel ERROR. " + e);
            return retorno;
        }
    }

    public BigInteger contarPlantasNivel(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM plantas WHERE nivel = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("Contador PersistenciaNiveles contarPlantasNivel  " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaNiveles  contarPlantasNivel ERROR. " + e);
            return retorno;
        }
    }

    public BigInteger contarPlantasPersonalesNivel(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM plantaspersonales WHERE nivel = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("Contador PersistenciaNiveles contarPlantasPersonalesNivel  " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaNiveles  contarPlantasPersonalesNivel ERROR. " + e);
            return retorno;
        }
    }
}
