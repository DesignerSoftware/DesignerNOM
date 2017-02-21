/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaElementosCausasAccidentesInterface;
import Entidades.ElementosCausasAccidentes;
import java.math.BigInteger;
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
public class PersistenciaElementosCausasAccidentes implements PersistenciaElementosCausasAccidentesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    public void crear(EntityManager em, ElementosCausasAccidentes elementosCausasAccidentes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(elementosCausasAccidentes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaElementosCausasAccidentes.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, ElementosCausasAccidentes elementosCausasAccidentes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(elementosCausasAccidentes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaElementosCausasAccidentes.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, ElementosCausasAccidentes elementosCausasAccidentes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(elementosCausasAccidentes));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaElementosCausasAccidentes.borrar: " + e);
            }
        }
    }

    public ElementosCausasAccidentes buscarElementoCausaAccidente(EntityManager em, BigInteger secuenciaECA) {
        try {
            em.clear();
            return em.find(ElementosCausasAccidentes.class, secuenciaECA);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ElementosCausasAccidentes> buscarElementosCausasAccidentes(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT l FROM ElementosCausasAccidentes  l ORDER BY l.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ElementosCausasAccidentes> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;
        } catch (Exception e) {
            System.err.println("ERROR PERSISCENTIAELEMENTOSCAUSASACCIDENTES BUSCARELEMENTOSCAUSASACCIDENTES  ERROR" + e);
            return null;
        }

    }

    public BigInteger contadorSoAccidentesMedicos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM soaccidentesmedicos WHERE agentelesion = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("PERSISCENTIAELEMENTOSCAUSASACCIDENTES contadorSoAccidentesMedicos = " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("ERROR PERSISCENTIAELEMENTOSCAUSASACCIDENTES verificarBorradoVigenciasDeportes  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    public BigInteger contadorSoAccidentes(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM soaccidentes   WHERE causa = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("PERSISCENTIAELEMENTOSCAUSASACCIDENTES contadorSoAccidentes = " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("ERROR PERSISCENTIAELEMENTOSCAUSASACCIDENTES contadorSoAccidentes  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    public BigInteger contadorSoIndicadoresFr(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM soindicadoresfr   WHERE fuente =  ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("PERSISCENTIAELEMENTOSCAUSASACCIDENTES contadorSoIndicadoresFr = " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("ERROR PERSISCENTIAELEMENTOSCAUSASACCIDENTES contadorSoIndicadoresFr  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
