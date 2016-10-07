/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ParametrosReportes;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'ParametrosReportes'
 * de la base de datos.
 * @author AndresPineda
 */
@Stateless
public class PersistenciaParametrosReportes implements PersistenciaParametrosReportesInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, ParametrosReportes parametrosInformes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(parametrosInformes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosReportes.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, ParametrosReportes parametrosInformes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(parametrosInformes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosReportes.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, ParametrosReportes parametrosInformes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(parametrosInformes));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosReportes.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public ParametrosReportes buscarParametroInforme(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(ParametrosReportes.class, secuencia);
        } catch (Exception e) {
            System.out.println("Error buscarParametroInforme Persistencia");
            return null;
        }
    }

    @Override
    public List<ParametrosReportes> buscarParametrosReportes(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ParametrosReportes.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarParametrosReportes");
            return null;
        }
    }

    @Override
    public ParametrosReportes buscarParametroInformeUsuario(EntityManager em, String alias) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT pi FROM ParametrosReportes pi WHERE pi.usuario =:Alias");
            query.setParameter("Alias", alias);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            ParametrosReportes parametrosInformes = (ParametrosReportes) query.getSingleResult();
            return parametrosInformes;
        } catch (Exception e) {
            System.out.println("Error en buscarParametroInformeUsuario " + e);
            return null;
        }
    }   
}
