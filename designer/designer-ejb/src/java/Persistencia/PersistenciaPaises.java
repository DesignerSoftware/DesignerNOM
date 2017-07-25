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
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPaises implements PersistenciaPaisesInterface {

   private static Logger log = Logger.getLogger(PersistenciaPaises.class);

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
            log.error("Error PersistenciaPaises.crear: " + e.getMessage());
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
            log.error("Error PersistenciaPaises.editar: " + e.getMessage());
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
            log.error("Error PersistenciaPaises.borrar: " + e.getMessage());
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
            log.error("Error: PersistenciaPaises consultarPaises ERROR " + e.getMessage());
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
            log.error("Persistencia.PersistenciaPaises.consultarPais()" + e.getMessage());
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
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaPaises contarDepartamentosPais ERROR : " + e.getMessage());
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
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaPaises contarFestivosPais ERROR : " + e.getMessage());
            return retorno;
        }
    }

}
