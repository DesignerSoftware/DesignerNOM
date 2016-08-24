/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TiposConclusiones;
import InterfacePersistencia.PersistenciaTiposConclusionesInterface;
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
public class PersistenciaTiposConclusiones implements PersistenciaTiposConclusionesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    public void crear(EntityManager em, TiposConclusiones tiposConclusiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposConclusiones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposConclusiones.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, TiposConclusiones tiposConclusiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposConclusiones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposConclusiones.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, TiposConclusiones tiposConclusiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposConclusiones));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposConclusiones.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public List<TiposConclusiones> consultarTiposConclusiones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposConclusiones t ORDER BY t.codigo  ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposConclusiones> tiposConclusiones = query.getResultList();
            return tiposConclusiones;
        } catch (Exception e) {
            System.out.println("Error buscarTiposConclusiones ERROR" + e);
            return null;
        }
    }

    public TiposConclusiones consultarTipoConclusion(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM TiposConclusiones t WHERE t.secuencia =:secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposConclusiones tiposConclusiones = (TiposConclusiones) query.getSingleResult();
            return tiposConclusiones;
        } catch (Exception e) {
            System.out.println("Error buscarTipoConclusionSecuencia");
            TiposConclusiones tiposConclusiones = null;
            return tiposConclusiones;
        }
    }

    public BigInteger contarChequeosMedicosTipoConclusion(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM chequeosmedicos WHERE tipochequeo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("Contador PersistenciaTiposConclusiones contarChequeosMedicosTipoConclusion Retorno " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaTiposConclusiones contarChequeosMedicosTipoConclusion ERROR : " + e);
            return retorno;
        }
    }
}
