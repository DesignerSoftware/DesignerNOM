/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposPensionados;
import InterfacePersistencia.PersistenciaTiposPensionadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposPensionados implements PersistenciaTiposPensionadosInterface {

   @Override
    public void crear(EntityManager em, TiposPensionados tiposPensionados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposPensionados);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposPensionados.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposPensionados tiposPensionados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposPensionados);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposPensionados.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposPensionados tiposPensionados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposPensionados));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposPensionados.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposPensionados> consultarTiposPensionados(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM  TiposPensionados";
            Query query = em.createNativeQuery(sql, TiposPensionados.class);
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposPensionados> listaTiposPensionados = query.getResultList();
            return listaTiposPensionados;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposPensionados.consultarTiposPensionados()" + e.getMessage());
            return null;
        }
    }

    @Override
    public TiposPensionados consultarTipoPensionado(EntityManager em, BigInteger secuencia) {

        try {
            em.clear();
            Query query = em.createQuery("SELECT tp FROM TiposPensionados tp WHERE tp.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposPensionados tipoP = (TiposPensionados) query.getSingleResult();
            return tipoP;
        } catch (Exception e) {
            System.out.println("Error buscarTipoPensionSecuencia PersistenciaTiposPensionados " + e.getMessage());
            TiposPensionados tipoP = null;
            return tipoP;
        }
    }

    @Override
    public BigInteger contarPensionadosTipoPension(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM pensionados WHERE tipopensionado=?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosRetiros   contarRetiradosClasePension. " + e.getMessage());
            return retorno;
        }
    }
}
