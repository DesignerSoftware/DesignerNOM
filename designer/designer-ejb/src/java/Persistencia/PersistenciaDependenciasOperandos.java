/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.DependenciasOperandos;
import InterfacePersistencia.PersistenciaDependenciasOperandosInterface;
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
public class PersistenciaDependenciasOperandos implements PersistenciaDependenciasOperandosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, DependenciasOperandos dependenciasOperandos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(dependenciasOperandos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasCargos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, DependenciasOperandos dependenciasOperandos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(dependenciasOperandos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasCargos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, DependenciasOperandos dependenciasOperandos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(dependenciasOperandos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaVigenciasCargos.borrar: " + e);
            }
        }
    }

    @Override
    public List<DependenciasOperandos> dependenciasOperandos(EntityManager em, BigInteger secuenciaOperando) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tf FROM DependenciasOperandos tf, Operandos op WHERE tf.operando.secuencia = :secuenciaOperando ORDER BY tf.codigo DESC");
            query.setParameter("secuenciaOperando", secuenciaOperando);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<DependenciasOperandos> dependenciasOperandosResult = new ArrayList<DependenciasOperandos>();
            dependenciasOperandosResult = query.getResultList();
            return dependenciasOperandosResult;
        } catch (Exception e) {
            System.out.println("Error: (dependenciasOperandos)" + e);
            return null;
        }
    }

    @Override
    public String nombreOperandos(EntityManager em, int codigo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT op.nombre FROM Operandos op WHERE op.codigo = :codigo");
            query.setParameter("codigo", codigo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            String nombre;
            nombre = (String) query.getSingleResult();
            return nombre;
        } catch (Exception e) {
            System.out.println("Error: (nombreOperandos)" + e);
            return null;
        }
    }
}
