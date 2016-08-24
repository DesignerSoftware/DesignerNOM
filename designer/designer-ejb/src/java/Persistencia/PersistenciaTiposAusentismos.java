/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Tiposausentismos;
import InterfacePersistencia.PersistenciaTiposAusentismosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposAusentismos' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposAusentismos implements PersistenciaTiposAusentismosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */

    @Override
    public void crear(EntityManager em, Tiposausentismos tiposAusentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposAusentismos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposAusentismos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Tiposausentismos tiposAusentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposAusentismos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposAusentismos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Tiposausentismos tiposAusentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposAusentismos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposAusentismos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Tiposausentismos> consultarTiposAusentismos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ta FROM Tiposausentismos ta ORDER BY ta.codigo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Tiposausentismos> todosTiposAusentismos = query.getResultList();
            return todosTiposAusentismos;
        } catch (Exception e) {
            System.out.println("Error: (todasNovedades)" + e);
            return null;
        }
    }

    public Tiposausentismos consultarTipoAusentismo(EntityManager em, BigInteger secClaseCategoria) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT cc FROM Tiposausentismos cc WHERE cc.secuencia=:secuencia");
            query.setParameter("secuencia", secClaseCategoria);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Tiposausentismos clasesCategorias = (Tiposausentismos) query.getSingleResult();
            return clasesCategorias;
        } catch (Exception e) {
            return null;
        }
    }

    public BigInteger contarClasesAusentimosTipoAusentismo(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM clasesausentismos WHERE tipo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("Contador PersistenciaTiposAusentismos contarClasesAusentimosTipoAusentismo Retorno " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaTiposAusentismos contarClasesAusentimosTipoAusentismo ERROR : " + e);
            return retorno;
        }
    }

    public BigInteger contarSOAusentimosTipoAusentismo(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM soausentismos WHERE tipo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("Contador PersistenciaTiposAusentismos contarSOAusentimosTipoAusentismo Retorno " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaTiposAusentismos contarSOAusentimosTipoAusentismo ERROR : " + e);
            return retorno;
        }
    }

}
