/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.OtrosCertificados;
import InterfacePersistencia.PersistenciaOtrosCertificadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'OtrosCertificados' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaOtrosCertificados implements PersistenciaOtrosCertificadosInterface {

   private static Logger log = Logger.getLogger(PersistenciaOtrosCertificados.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, OtrosCertificados certificados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(certificados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaOtrosCertificados.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, OtrosCertificados certificados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(certificados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaOtrosCertificados.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, OtrosCertificados certificados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(certificados));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaOtrosCertificados.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<OtrosCertificados> buscarOtrosCertificados(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT o FROM OtrosCertificados o");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<OtrosCertificados> certificados = (List<OtrosCertificados>) query.getResultList();
            return certificados;
        } catch (Exception e) {
            log.error("PersistenciaOtrosCertificados.buscarOtrosCertificados():  ", e);
            return null;
        }
    }

    @Override
    public OtrosCertificados buscarOtroCertificadoSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT oc FROM OtrosCertificados oc WHERE oc.secuencia= :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            OtrosCertificados certificados = (OtrosCertificados) query.getResultList();
            return certificados;
        } catch (Exception e) {
            log.error("PersistenciaOtrosCertificados.buscarOtroCertificadoSecuencia():  ", e);
            OtrosCertificados certificados = null;
            return certificados;
        }
    }

    @Override
    public List<OtrosCertificados> buscarOtrosCertificadosEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT oc FROM OtrosCertificados oc WHERE oc.empleado.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<OtrosCertificados> certificados = (List<OtrosCertificados>) query.getResultList();
            return certificados;
        } catch (Exception e) {
            log.error("PersistenciaOtrosCertificados.buscarOtrosCertificadosEmpleado():  ", e);
            List<OtrosCertificados> certificados = null;
            return certificados;
        }
    }
}
