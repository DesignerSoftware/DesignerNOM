/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Nodos;
import InterfacePersistencia.PersistenciaNodosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Nodos' de la base de
 * datos.
 *
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaNodos implements PersistenciaNodosInterface {

   private static Logger log = Logger.getLogger(PersistenciaNodos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Nodos nodos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(nodos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNodos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Nodos nodos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(nodos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNodos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Nodos nodos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(nodos));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaNodos.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Nodos buscarNodoSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT n FROM Nodos n WHERE n.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Nodos nodos = (Nodos) query.getSingleResult();
            return nodos;
        } catch (Exception e) {
            log.error("Error buscarNodoSecuencia PersistenciaNodos : " + e.toString());
            Nodos nodos = null;
            return nodos;
        }
    }

    @Override
    public List<Nodos> listNodos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT n FROM Nodos n");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Nodos> nodos = (List<Nodos>) query.getResultList();
            return nodos;
        } catch (Exception e) {
            log.error("Error listNodos PersistenciaNodos : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Nodos> buscarNodosPorSecuenciaHistoriaFormula(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT n FROM Nodos n WHERE n.historiaformula.secuencia=:secuenciaHF ORDER BY n.posicion ASC");
            query.setParameter("secuenciaHF", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Nodos> nodos = query.getResultList();
            return nodos;
        } catch (Exception e) {
            log.error("Error buscarNodosPorSecuenciaHistoriaFormula PersistenciaNodos : " + e.toString());
            List<Nodos> nodos = null;
            return nodos;
        }
    }
}
