/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.DetallesPeriodicidades;
import Entidades.Periodicidades;
import InterfacePersistencia.PersistenciaDetallesPeriodicidadesInterface;
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
public class PersistenciaDetallesPeriodicidades implements PersistenciaDetallesPeriodicidadesInterface {

    @Override
    public void crear(EntityManager em, DetallesPeriodicidades detallep) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(detallep);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesPeriodicidades.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, DetallesPeriodicidades detallep) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(detallep);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesPeriodicidades.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, DetallesPeriodicidades detallep) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(detallep));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesPeriodicidades.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<DetallesPeriodicidades> buscarDetallesPeriodicidad(EntityManager em, BigInteger secuenciaPeriodicidad) {
        try{
        em.clear();
        String sql = "SELECT * FROM DETALLESPERIODICIDADES WHERE PERIODICIDAD = ?";
        Query query = em.createNativeQuery(sql, DetallesPeriodicidades.class);
        query.setParameter(1, secuenciaPeriodicidad);
        List<DetallesPeriodicidades> lista = query.getResultList();
        return lista;
        }catch(Exception e){
            System.out.println("error persistenciadetallesperiodicidades.buscarDetallesPeriodicidad:" + e.toString());
            return null;
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Periodicidades buscarPeriodicidadPorSecuencia(EntityManager em, BigInteger secuenciaPeriodicidad) {
        try{
        em.clear();
        String sql = "SELECT * FROM PERIODICIDADES WHERE SECUENCIA = ?";
        Query query = em.createNativeQuery(sql,Periodicidades.class);
        query.setParameter(1, secuenciaPeriodicidad);
        Periodicidades periodicidad = (Periodicidades) query.getSingleResult();
        return periodicidad;
        }catch(Exception e){
            System.out.println("error persistenciadetallesperiodicidades.buscarPeriodicidadPorSecuencia:" + e.toString());
            return null;
        }
    }
}
