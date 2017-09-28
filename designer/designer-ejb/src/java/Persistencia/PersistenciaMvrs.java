/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Mvrs;
import InterfacePersistencia.PersistenciaMvrsInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Mvrs' de la base de
 * datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaMvrs implements PersistenciaMvrsInterface {

   private static Logger log = Logger.getLogger(PersistenciaMvrs.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Mvrs mvrs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(mvrs);
            tx.commit();
        } catch (Exception e) {
            log.error("PersistenciaMvrs.crear():  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Mvrs mvrs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(mvrs);
            tx.commit();
        } catch (Exception e) {
            log.error("PersistenciaMvrs.editar():  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Mvrs mvrs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(mvrs));
            tx.commit();
        } catch (Exception e) {
            log.error("PersistenciaMvrs.borrar():  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Mvrs> buscarMvrs(EntityManager em) {
        try{
        em.clear();
        List<Mvrs> mvrs = (List<Mvrs>) em.createQuery("SELECT m FROM Mvrs m").getResultList();
        return mvrs;
        }catch(Exception e){
            log.error("PersistenciaMvrs.buscarMvrs():  ", e);
            return null;
        }
    }

    @Override
    public Mvrs buscarMvrSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT mvrs FROM Mvrs mvrs WHERE mvrs.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Mvrs mvrs = (Mvrs) query.getSingleResult();
            return mvrs;
        } catch (Exception e) {
            log.error("PersistenciaMvrs.buscarMvrSecuencia():  ", e);
            Mvrs mvrs = null;
            return mvrs;
        }
    }

    @Override
    public List<Mvrs> buscarMvrsEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT mvrs FROM Mvrs mvrs WHERE mvrs.empleado.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Mvrs> mvrs = (List<Mvrs>) query.getResultList();
            return mvrs;
        } catch (Exception e) {
            log.error("PersistenciaMvrs.buscarMvrsEmpleado():  ", e);
            List<Mvrs> mvrs = null;
            return mvrs;
        }
    }
}
