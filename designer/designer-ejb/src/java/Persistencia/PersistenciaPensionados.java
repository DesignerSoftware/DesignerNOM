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
            System.out.println("Error PersistenciaPensionados.crear: " + e.getMessage());
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
            System.out.println("Error PersistenciaPensionados.editar: " + e.getMessage());
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
            System.out.println("Error PersistenciaPensionados.borrar: " + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaPensionados.buscarPensionado()" + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaPensionados.buscarPensionados()" + e.getMessage());
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
            System.out.println("Error en Persistencia Pensionados (buscarPensionadosEmpleado) " + e.toString());
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
            System.out.println("buscarPensionVigenciaSecuencia Error (PersistenciaPensionados): " + e.toString());
            return new Pensionados();
        }
    }
}
