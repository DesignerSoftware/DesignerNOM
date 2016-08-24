/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Soausentismos;
import InterfacePersistencia.PersistenciaSoausentismosInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'SoAusentismos' de la
 * base de datos.
 *
 * @author John Pineda.
 */
@Stateless
public class PersistenciaSoausentismos implements PersistenciaSoausentismosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Soausentismos soausentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(soausentismos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoausentismos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Soausentismos soausentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(soausentismos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoausentismos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Soausentismos soausentismos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(soausentismos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSoausentismos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Soausentismos> ausentismosEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT soa FROM Soausentismos soa WHERE soa.empleado.secuencia= :secuenciaEmpleado");
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Soausentismos> todosAusentismos = query.getResultList();
            return todosAusentismos;
        } catch (Exception e) {
            System.out.println("Error: (todasNovedades)" + e);
            return null;
        }
    }

    @Override
    public List<Soausentismos> prorrogas(EntityManager em, BigInteger secuenciaEmpleado, BigInteger secuenciaCausa, BigInteger secuenciaAusentismo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT soa FROM Soausentismos soa WHERE soa.empleado.secuencia= :secuenciaEmpleado AND soa.causa.secuencia= :secuenciaCausa AND soa.secuencia= :secuenciaAusentismo");
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            query.setParameter("secuenciaCausa", secuenciaCausa);
            query.setParameter("secuenciaAusentismo", secuenciaAusentismo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Soausentismos> prorrogas = query.getResultList();
            return prorrogas;
        } catch (Exception e) {
            System.out.println("Error: (prorrogas)" + e);
            return null;
        }
    }

    @Override
    public String prorrogaMostrar(EntityManager em, BigInteger secuenciaProrroga) {
        try {
            em.clear();
            String sqlQuery = ("SELECT nvl(A.NUMEROCERTIFICADO,'Falta # Certificado')||':'||A.fecha||'->'||A.fechafinaus\n"
                    + "FROM SOAUSENTISMOS A\n"
                    + "WHERE A.SECUENCIA = ?");
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuenciaProrroga);
            String resultado = (String) query.getSingleResult();
            return resultado;
        } catch (Exception e) {
            System.out.println("Error: (prorrogaMostrar)" + e);
            return null;
        }
    }
}
