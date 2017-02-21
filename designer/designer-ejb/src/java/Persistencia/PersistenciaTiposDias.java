/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposDias;
import InterfacePersistencia.PersistenciaTiposDiasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposDias' de la base
 * de datos.
 *
 * @author AndresPineda.
 */
@Stateless
public class PersistenciaTiposDias implements PersistenciaTiposDiasInterface {

    @Override
    public void crear(EntityManager em, TiposDias tiposDias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposDias);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposDias.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposDias tiposDias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposDias);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposDias.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposDias tiposDias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposDias));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposDias.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public TiposDias buscarTipoDia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(TiposDias.class, secuencia);
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposDias buscarTipoDia : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposDias> buscarTiposDias(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT td FROM TiposDias td ORDER BY td.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposDias> tiposDias = query.getResultList();
            return tiposDias;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposDias buscarTiposDias : " + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorDiasLaborales(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM diaslaborables WHERE tipodia = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposDias.contadorDiasLaborales() + e.getMessage()");
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    @Override
    public BigInteger contadorExtrasRecargos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM extrasrecargos WHERE tipodia = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposDias.contadorExtrasRecargos()" + e.getMessage());
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
