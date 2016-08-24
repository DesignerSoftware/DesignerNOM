/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MetodosPagos;
import InterfacePersistencia.PersistenciaMetodosPagosInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'MetodosPagos' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaMetodosPagos implements PersistenciaMetodosPagosInterface {

    @Override
    public void crear(EntityManager em, MetodosPagos metodosPagos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(metodosPagos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMetodosPagos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, MetodosPagos metodosPagos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(metodosPagos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMetodosPagos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, MetodosPagos metodosPagos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(metodosPagos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaMetodosPagos.borrar: " + e);
            }
        }
    }

    @Override
    public MetodosPagos buscarMetodosPagosEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(MetodosPagos.class, secuencia);
        } catch (Exception e) {
            System.out.println("Error en la persistencia vigencias formas pagos ERROR : " + e);
            return null;
        }
    }

    public List<MetodosPagos> buscarMetodosPagos(EntityManager em) {
        em.clear();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(MetodosPagos.class));
        return em.createQuery(cq).getResultList();
    }

    public BigInteger contadorvigenciasformaspagos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM vigenciasformaspagos WHERE metodopago = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.err.println(" PersistenciaMetodosPagos contadorvigenciasformaspagos Contador " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println(" PersistenciaMetodosPagos contadorvigenciasformaspagos Error " + e);
            return retorno;
        }
    }
}
