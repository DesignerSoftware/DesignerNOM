/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Entidades.TiposConstantes;
import InterfacePersistencia.PersistenciaTiposConstantesInterface;
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
public class PersistenciaTiposConstantes implements PersistenciaTiposConstantesInterface{

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/
    
    @Override
    public void crear(EntityManager em, TiposConstantes tiposConstantes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposConstantes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposConstantes.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposConstantes tiposConstantes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposConstantes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposConstantes.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposConstantes tiposConstantes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposConstantes));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposConstantes.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposConstantes> tiposConstantes(EntityManager em, BigInteger secuenciaOperando, String tipo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT DISTINCT tf FROM TiposConstantes tf, Operandos op WHERE tf.operando.secuencia =:secuenciaOperando and op.tipo=:tipo ORDER BY tf.fechafinal DESC");
            query.setParameter("secuenciaOperando", secuenciaOperando);
            query.setParameter("tipo", tipo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposConstantes> tiposConstantes = query.getResultList();
            List<TiposConstantes> tiposConstantesResult = new ArrayList<TiposConstantes>(tiposConstantes);

            System.out.println("tiposConstantes" + tiposConstantesResult);
            return tiposConstantesResult;
        } catch (Exception e) {
            System.out.println("Error: (tiposConstantes)" + e);
            return null;
        }
    }
}