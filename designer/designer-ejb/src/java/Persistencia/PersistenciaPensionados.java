/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Pensionados;
import InterfacePersistencia.PersistenciaPensionadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless. <br> Clase encargada de realizar operaciones sobre la tabla
 * 'MotivosContratos' de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaPensionados implements PersistenciaPensionadosInterface {

   private static Logger log = Logger.getLogger(PersistenciaPensionados.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Pensionados pensionados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pensionados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPensionados.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Pensionados pensionados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pensionados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPensionados.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Pensionados pensionados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(pensionados));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPensionados.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Pensionados buscarPensionado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            BigInteger in = (BigInteger) secuencia;
            return em.find(Pensionados.class, in);
        } catch (Exception e) {
            log.error("PersistenciaPensionados.buscarPensionado():  ", e);
            return null;
        }
    }

    @Override
    public List<Pensionados> buscarPensionados(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM Pensionados";
            Query query = em.createNativeQuery(sql, Pensionados.class);
            List<Pensionados> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("PersistenciaPensionados.buscarPensionados():  ", e);
            return null;
        }
    }

    @Override
    public List<Pensionados> buscarPensionadosEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Pensionados p WHERE p.vigenciatipotrabajador.empleado.secuencia = :secuenciaEmpl ORDER BY p.fechainiciopension DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Pensionados> pensionados = query.getResultList();
            return pensionados;
        } catch (Exception e) {
            log.error("Error en Persistencia Pensionados (buscarPensionadosEmpleado)  ", e);
            return null;
        }
    }

    @Override
    public Pensionados buscarPensionVigenciaSecuencia(EntityManager em, BigInteger secVigencia) {
        try {
            em.clear();
            String sql = "SELECT * FROM Pensionados WHERE vigenciatipotrabajador = ?";
            Query query = em.createNativeQuery(sql, Pensionados.class);
            query.setParameter(1, secVigencia);
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Pensionados pensionVigencia = (Pensionados) query.getSingleResult();
            return pensionVigencia;
        } catch (Exception e) {
            log.error("buscarPensionVigenciaSecuencia Error (PersistenciaPensionados):  ", e);
            return new Pensionados();
        }
    }
}
