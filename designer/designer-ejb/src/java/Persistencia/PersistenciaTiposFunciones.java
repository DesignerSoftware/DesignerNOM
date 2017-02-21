/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposFunciones;
import InterfacePersistencia.PersistenciaTiposFuncionesInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposFunciones implements PersistenciaTiposFuncionesInterface {

       
    @Override
    public void crear(EntityManager em, TiposFunciones tiposFunciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposFunciones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposFunciones.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposFunciones tiposFunciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposFunciones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposFunciones.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposFunciones tiposFunciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposFunciones));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposFunciones.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposFunciones> tiposFunciones(EntityManager em, BigInteger secuenciaOperando, String tipo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT DISTINCT tf FROM TiposFunciones tf, Operandos op WHERE tf.operando.secuencia =:secuenciaOperando and op.tipo=:tipo");
            query.setParameter("secuenciaOperando", secuenciaOperando);
            query.setParameter("tipo", tipo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposFunciones> tiposFunciones = query.getResultList();
            List<TiposFunciones> tiposFuncionesResult = new ArrayList<TiposFunciones>(tiposFunciones);
            return tiposFuncionesResult;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposFunciones.tiposFunciones()" + e.getMessage());
            return null;
        }
    }
}
