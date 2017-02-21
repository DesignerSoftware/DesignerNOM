/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.VigenciasTallas;
import InterfacePersistencia.PersistenciaVigenciasTallasInterface;
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
public class PersistenciaVigenciasTallas implements PersistenciaVigenciasTallasInterface {

    public void crear(EntityManager em, VigenciasTallas vigenciasTallas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasTallas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasTallas.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, VigenciasTallas vigenciasTallas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasTallas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasTallas.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, VigenciasTallas vigenciasTallas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasTallas));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasTallas.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public List<VigenciasTallas> consultarVigenciasTallasPorPersona(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT g FROM VigenciasTallas g WHERE g.empleado.secuencia = :secEmpleado ");
            query.setParameter("secEmpleado", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List< VigenciasTallas> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;

        } catch (Exception e) {
            System.out.println("Error consultarVigenciasTallas consultarVigenciasTallasPorPersona : " + e.getMessage());
            return null;
        }
    }

}
