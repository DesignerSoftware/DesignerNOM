/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.NovedadesOperandos;
import InterfacePersistencia.PersistenciaNovedadesOperandosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class PersistenciaNovedadesOperandos implements PersistenciaNovedadesOperandosInterface {

   private static Logger log = Logger.getLogger(PersistenciaNovedadesOperandos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, NovedadesOperandos novedadesOperandos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedadesOperandos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNovedadesOperandos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, NovedadesOperandos novedadesOperandos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedadesOperandos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNovedadesOperandos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, NovedadesOperandos novedadesOperandos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(novedadesOperandos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNovedadesOperandos.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<NovedadesOperandos> novedadesOperandos(EntityManager em, BigInteger secuenciaOperando) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT no FROM NovedadesOperandos no WHERE no.operando.secuencia =:secuenciaOperando");
            query.setParameter("secuenciaOperando", secuenciaOperando);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<NovedadesOperandos> novedadesOperandos = query.getResultList();
            List<NovedadesOperandos> novedadesOperandosResult = new ArrayList<NovedadesOperandos>(novedadesOperandos);
            return novedadesOperandosResult;
        } catch (Exception e) {
            log.error("Error: (novedadesOperandos)" + e.getMessage());
            return null;
        }
    }

}
