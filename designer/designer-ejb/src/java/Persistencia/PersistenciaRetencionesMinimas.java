/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.RetencionesMinimas;
import InterfacePersistencia.PersistenciaRetencionesMinimasInterface;
import java.math.BigInteger;
import java.util.Date;
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
public class PersistenciaRetencionesMinimas implements PersistenciaRetencionesMinimasInterface {

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
            System.out.println("Error PersistenciaRetencionesMinimas.crear: " + e);
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
            System.out.println("Error PersistenciaRetencionesMinimas.editar: " + e);
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
            System.out.println("Error PersistenciaRetencionesMinimas.borrar: " + e);
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
            for (int i = 0; i < resultado.size(); i++) {
                System.out.println("resultado : " + resultado.get(i).getSecuencia());
            }

            return resultado;

        } catch (Exception e) {
            System.out.println("Error: ( RetencionesMinimas)" + e.toString());
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
            System.out.println("Error en Persistencia Retenciones Minimas " + e);
            return null;
        }
    }
}
