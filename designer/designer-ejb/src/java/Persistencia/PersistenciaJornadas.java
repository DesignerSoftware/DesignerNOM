/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Jornadas;
import InterfacePersistencia.PersistenciaJornadasInterface;
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
public class PersistenciaJornadas implements PersistenciaJornadasInterface {

   private static Logger log = Logger.getLogger(PersistenciaJornadas.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    EntityManager em;
    @Override
    public void crear(EntityManager em, Jornadas jornadas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(jornadas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaJornadas.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Jornadas jornadas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(jornadas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaJornadas.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Jornadas jornadas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(jornadas));
            tx.commit();

        } catch (Exception e) {
            log.error("Error PersistenciaJornadas.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Jornadas consultarJornada(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(Jornadas.class, secuencia);
        } catch (Exception e) {
            log.error("\n ERROR EN PersistenciaJornadas buscarJornada ERROR  ", e);
            return null;
        }
    }

    @Override
    public List<Jornadas> consultarJornadas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ta FROM Jornadas ta ORDER BY ta.codigo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Jornadas> todosJornadas = query.getResultList();
            return todosJornadas;
        } catch (Exception e) {
            log.error("Error: PersistenciaJornadas consultarJornadas ERROR  ", e);
            return null;
        }
    }

    @Override
    public BigInteger contarTarifasEscalafonesJornada(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT (*) FROM tarifasescalafones WHERE jornada =?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaJornadas contarTiposEntidadesJornada ERROR :  ", e);
            return retorno;
        }
    }

    @Override
    public BigInteger contarJornadasLaboralesJornada(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT (*) FROM jornadaslaborales WHERE jornada =?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaJornadas contarTSjornadasTipoEntidad ERROR :  ", e);
            return retorno;
        }
    }

}
