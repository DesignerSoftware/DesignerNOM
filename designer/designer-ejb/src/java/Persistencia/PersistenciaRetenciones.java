/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Retenciones;
import InterfacePersistencia.PersistenciaRetencionesInterface;
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
public class PersistenciaRetenciones implements PersistenciaRetencionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaRetenciones.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Retenciones retenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(retenciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaRetenciones.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Retenciones retenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(retenciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaRetenciones.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Retenciones retenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(retenciones));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaRetenciones.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Retenciones> buscarRetenciones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Retenciones r");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Retenciones> setsLista = (List<Retenciones>) query.getResultList();
            return setsLista;
        } catch (Exception e) {
            log.error("PersistenciaRetenciones.buscarRetenciones():  ", e);
            return null;
        }
    }

    @Override
    public List<Retenciones> buscarRetencionesVig(EntityManager em, BigInteger secRetencion) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Retenciones r WHERE r.vigencia.secuencia = :secRetencion");
            query.setParameter("secRetencion", secRetencion);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Retenciones> retenciones = query.getResultList();
            return retenciones;
        } catch (Exception e) {
            log.error("PersistenciaRetenciones.buscarRetencionesVig():  ", e);
            return null;
        }
    }
}
