/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MotivosRetiros;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosRetiros' de la
 * base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaMotivosRetiros implements PersistenciaMotivosRetirosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, MotivosRetiros motivosRetiros) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosRetiros);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosRetiros.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, MotivosRetiros motivosRetiros) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosRetiros);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosRetiros.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, MotivosRetiros motivosRetiros) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivosRetiros));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosRetiros.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<MotivosRetiros> consultarMotivosRetiros(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM MotivosRetiros ORDER BY codigo ASC";
            Query query = em.createNativeQuery(sql, MotivosRetiros.class);
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<MotivosRetiros> listTiposViajeros = query.getResultList();
            return listTiposViajeros;
        } catch (Exception e) {
            System.err.println("ERROR PersistenciaTiposViajeros ConsultarTiposViajeros ERROR :" + e);
            return null;
        }

    }

    @Override
    public MotivosRetiros consultarMotivoRetiro(EntityManager em, BigInteger secuencia) {

        try {
            em.clear();
            String sql = "SELECT * FROM MotivosRetiros WHERE secuencia = ?";
            Query query = em.createNativeQuery(sql, MotivosRetiros.class);
            query.setParameter(1, secuencia);
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            MotivosRetiros motivoR = (MotivosRetiros) query.getSingleResult();
            return motivoR;
        } catch (Exception e) {
            MotivosRetiros motivoR = null;
            return motivoR;
        }

    }

    public BigInteger contarHVExperienciasLaboralesMotivoRetiro(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM hvexperienciaslaborales WHERE motivoretiro = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.err.println("Contador PersistenciaMotivosRetiros  contarHVExperienciasLaboralesMotivoRetiro  " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosRetiros   contarHVExperienciasLaboralesMotivoRetiro. " + e);
            return retorno;
        }
    }

    public BigInteger contarNovedadesSistemasMotivoRetiro(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM novedadessistema WHERE motivoretiro = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.err.println("Contador PersistenciaMotivosRetiros  contarNovedadesSistemasMotivoRetiro  " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosRetiros   contarNovedadesSistemasMotivoRetiro. " + e);
            return retorno;
        }
    }

    public BigInteger contarRetiMotivosRetirosMotivoRetiro(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM parametroscambiosmasivos WHERE retimotivoretiro = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.err.println("Contador PersistenciaMotivosRetiros  contarRetiMotivosRetirosMotivoRetiro  " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosRetiros   contarRetiMotivosRetirosMotivoRetiro. " + e);
            return retorno;
        }
    }

    public BigInteger contarRetiradosMotivoRetiro(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM retirados WHERE motivoretiro = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.err.println("Contador PersistenciaMotivosRetiros  contarRetiRetiradosMotivoRetiro  " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosRetiros   contarRetiRetiradosMotivoRetiro. " + e);
            return retorno;
        }
    }

}
