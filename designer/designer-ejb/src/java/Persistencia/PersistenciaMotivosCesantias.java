/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaMotivosCesantiasInterface;
import Entidades.MotivosCesantias;
import java.math.BigInteger;
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
public class PersistenciaMotivosCesantias implements PersistenciaMotivosCesantiasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     * @param em
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, MotivosCesantias motivosCesantias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosCesantias);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosCesantias.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, MotivosCesantias motivosCesantias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosCesantias);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosCesantias.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, MotivosCesantias motivosCesantias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivosCesantias));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaMotivosCesantias.borrar: " + e);
            }
        }
    }

    @Override
    public MotivosCesantias buscarMotivoCensantia(EntityManager em, BigInteger secuenciaME) {
        try {
            em.clear();
            return em.find(MotivosCesantias.class, secuenciaME);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<MotivosCesantias> buscarMotivosCesantias(EntityManager em) {
        em.clear();
        Query query = em.createQuery("SELECT m FROM MotivosCesantias m ORDER BY m.codigo ASC");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<MotivosCesantias> listaMotivosEmbargos = query.getResultList();
        return listaMotivosEmbargos;
    }

    @Override
    public BigInteger contadorNovedadesSistema(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM novedadessistema WHERE motivocesantia =  ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("PERSISTENCIAMOTIVOSCENSANTIAS CONTADORNOVEDADESSISTEMA = " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("ERROR PERSISTENCIAMOTIVOSCENSANTIAS CONTADORNOVEDADESSISTEMA  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

}
