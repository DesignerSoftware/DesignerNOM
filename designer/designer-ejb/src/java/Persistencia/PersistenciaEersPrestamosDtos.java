/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Entidades.EersPrestamosDtos;
import InterfacePersistencia.PersistenciaEersPrestamosDtosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class PersistenciaEersPrestamosDtos implements PersistenciaEersPrestamosDtosInterface {

    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em,EersPrestamosDtos eersPrestamosDtos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(eersPrestamosDtos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEersPrestamosDtos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em,EersPrestamosDtos eersPrestamosDtos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(eersPrestamosDtos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEersPrestamosDtos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em,EersPrestamosDtos eersPrestamosDtos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(eersPrestamosDtos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaEersPrestamosDtos.borrar: " + e);
            }
        }
    }

    @Override
    public List<EersPrestamosDtos> eersPrestamosDtosEmpleado(EntityManager em,BigInteger secuenciaEersPrestamo) {
        try {
            em.clear();
            Query query = em.createQuery("select e from EersPrestamosDtos e where e.eerprestamo.secuencia = :secuenciaEersPrestamo ");
            query.setParameter("secuenciaEersPrestamo", secuenciaEersPrestamo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<EersPrestamosDtos> eersPrestamosDtos = query.getResultList();
            List<EersPrestamosDtos> eersPrestamosDtosResult = new ArrayList<EersPrestamosDtos>(eersPrestamosDtos);
            return eersPrestamosDtosResult;
        } catch (Exception e) {
            System.out.println("Error: (eersPrestamosDtos)" + e);
            return null;
        }
    }
}
