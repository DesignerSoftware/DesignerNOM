/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaPaisesInterface;
import Entidades.Paises;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPaises implements PersistenciaPaisesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Paises tiposAusentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposAusentismos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPaises.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Paises tiposAusentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposAusentismos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPaises.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Paises tiposAusentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposAusentismos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPaises.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Paises> consultarPaises(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ta FROM Paises ta ORDER BY ta.codigo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Paises> todosPaises = query.getResultList();
            return todosPaises;
        } catch (Exception e) {
            System.err.println("Error: PersistenciaPaises consultarPaises ERROR " + e);
            return null;
        }
    }

    public Paises consultarPais(EntityManager em, BigInteger secClaseCategoria) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT cc FROM Paises cc WHERE cc.secuencia=:secuencia");
            query.setParameter("secuencia", secClaseCategoria);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Paises clasesCategorias = (Paises) query.getSingleResult();
            return clasesCategorias;
        } catch (Exception e) {
            return null;
        }
    }

    public BigInteger contarDepartamentosPais(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM departamentos WHERE pais = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("Contador PersistenciaPaises contarDepartamentosPais Retorno " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaPaises contarDepartamentosPais ERROR : " + e);
            return retorno;
        }
    }

    public BigInteger contarFestivosPais(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM festivos WHERE pais = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("Contador PersistenciaPaises contarFestivosPais Retorno " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaPaises contarFestivosPais ERROR : " + e);
            return retorno;
        }
    }

}
