/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MotivosEmbargos;
import InterfacePersistencia.PersistenciaMotivosEmbargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosEmbargos' de
 * la base de datos.
 *
 * @author John Pineda
 */
@Stateless
public class PersistenciaMotivosEmbargos implements PersistenciaMotivosEmbargosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, MotivosEmbargos motivosEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosEmbargos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosEmbargos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, MotivosEmbargos motivosEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosEmbargos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosEmbargos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, MotivosEmbargos motivosEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivosEmbargos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosEmbargos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public MotivosEmbargos buscarMotivoEmbargo(EntityManager em, BigInteger secuenciaME) {
        try {
            em.clear();
            return em.find(MotivosEmbargos.class, secuenciaME);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<MotivosEmbargos> buscarMotivosEmbargos(EntityManager em) {
        em.clear();
        Query query = em.createQuery("SELECT m FROM MotivosEmbargos m ORDER BY m.codigo ASC");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<MotivosEmbargos> listaMotivosEmbargos = query.getResultList();
        return listaMotivosEmbargos;
    }

    @Override
    public BigInteger contadorEersPrestamos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM eersprestamos eer WHERE eer.motivoembargo = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("PERSISTENCIAMOTIVOSEMBARGOS CONTADOREERSPRESTAMOS = " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("ERROR PERSISTENCIAMOTIVOSEMBARGOS CONTADOREERSPRESTAMOS  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    @Override
    public BigInteger contadorEmbargos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM  embargos emb WHERE emb.motivo = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("PERSISTENCIAMOTIVOSEMBARGOS CONTADOREMBARGOS = " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("ERROR PERSISTENCIAMOTIVOSEMBARGOS CONTADOREMBARGOS  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
