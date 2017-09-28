/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PreguntasKioskos;
import InterfacePersistencia.PersistenciaPreguntasKioscosInterface;
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
public class PersistenciaPreguntasKioscos implements PersistenciaPreguntasKioscosInterface {

   private static Logger log = Logger.getLogger(PersistenciaPreguntasKioscos.class);

    @Override
    public void crear(EntityManager em, PreguntasKioskos pregunta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pregunta);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPreguntasKioscos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PreguntasKioskos pregunta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pregunta);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPreguntasKioscos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PreguntasKioskos pregunta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(pregunta));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPreguntasKioscos.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<PreguntasKioskos> consultarPreguntasKioskos(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM PREGUNTASKIOSKOS  ORDER BY CODIGO ASC";
            Query query = em.createNativeQuery(sql, PreguntasKioskos.class);
            List< PreguntasKioskos> lista = query.getResultList();
            return lista;

        } catch (Exception e) {
            log.error("Error consultarPreguntasKioskos PersistenciaPreguntasKioscos :  ", e);
            return null;
        }
    }

    @Override
    public PreguntasKioskos consultarPreguntaKiosko(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createNativeQuery("SELECT * FROM PREGUNTASKIOSKOS WHERE SECUENCIA = ?");
            query.setParameter(1, secuencia);
            PreguntasKioskos preguntaK = (PreguntasKioskos) query.getSingleResult();
            return preguntaK;
        } catch (Exception e) {
            log.error("Error PersistenciaPreguntasKioscos consultarPreguntaKiosko :  ", e);
            PreguntasKioskos preguntaK = null;
            return preguntaK;
        }
    }

    @Override
    public BigInteger contarPreguntasKioskos(EntityManager em, BigInteger secuencia) {
       BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM CONEXIONESKIOSKOS WHERE PREGUNTA1 = ? OR PREGUNTA2 = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            query.setParameter(2, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("Contador PreguntasKioskos  persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaPreguntasKioscos.contarPreguntasKioskos():  ", e);
            return retorno;
        }
    }

}
