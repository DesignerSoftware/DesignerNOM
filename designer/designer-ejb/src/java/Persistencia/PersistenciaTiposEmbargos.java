/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposEmbargos;
import InterfacePersistencia.PersistenciaTiposEmbargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposEmbargos implements PersistenciaTiposEmbargosInterface {

    @Override
    public void crear(EntityManager em, TiposEmbargos tiposEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEmbargos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEmbargos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposEmbargos tiposEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEmbargos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEmbargos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposEmbargos tiposEmbargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposEmbargos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEmbargos.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public TiposEmbargos buscarTipoEmbargo(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(TiposEmbargos.class, secuencia);
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposEmbargos.buscarTipoEmbargo()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposEmbargos> buscarTiposEmbargos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM TiposEmbargos m ORDER BY m.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposEmbargos> listaMotivosPrestamos = query.getResultList();
            return listaMotivosPrestamos;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposEmbargos.buscarTiposEmbargos()" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorEerPrestamos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM eersprestamos ee WHERE ee.tipoembargo = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposEmbargos.contadorEerPrestamos()" + e.getMessage());
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    @Override
    public BigInteger contadorFormasDtos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM formasdtos fdts WHERE fdts.tipoembargo = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposEmbargos.contadorFormasDtos()" + e.getMessage());
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
