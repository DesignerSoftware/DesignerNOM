/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.RetencionesMinimas;
import InterfacePersistencia.PersistenciaRetencionesMinimasInterface;
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
public class PersistenciaRetencionesMinimas implements PersistenciaRetencionesMinimasInterface {

   private static Logger log = Logger.getLogger(PersistenciaRetencionesMinimas.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, RetencionesMinimas retenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(retenciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaRetencionesMinimas.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, RetencionesMinimas retenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(retenciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaRetencionesMinimas.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, RetencionesMinimas retenciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(retenciones));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaRetencionesMinimas.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    //Trae las relaciones en base al ausentismo seleccionado
    @Override
    public List<RetencionesMinimas> retenciones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT u FROM RetencionesMinimas u ORDER BY u.retencion ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<RetencionesMinimas> resultado = query.getResultList();
            return resultado;

        } catch (Exception e) {
            log.error("Error: ( RetencionesMinimas) ", e);
            return null;
        }
    }

    @Override
    public List<RetencionesMinimas> buscarRetencionesMinimasVig(EntityManager em, BigInteger secRetencion) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM RetencionesMinimas r WHERE r.vigenciaretencionminima.secuencia = :secRetencion");
            query.setParameter("secRetencion", secRetencion);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<RetencionesMinimas> retenciones = query.getResultList();
            return retenciones;
        } catch (Exception e) {
            log.error("Error en Persistencia Retenciones Minimas  ", e);
            return null;
        }
    }
}
