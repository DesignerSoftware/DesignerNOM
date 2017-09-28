/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MotivosContratos;
import InterfacePersistencia.PersistenciaMotivosContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosContratos' de
 * la base de datos. (Para verificar que esta asociado a una
 * VigenciaTipoContrato, se realiza la operacion sobre la tabla
 * VigenciasTiposContratos)
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaMotivosContratos implements PersistenciaMotivosContratosInterface {

   private static Logger log = Logger.getLogger(PersistenciaMotivosContratos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, MotivosContratos motivosContratos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosContratos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosContratos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, MotivosContratos motivosContratos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosContratos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosContratos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, MotivosContratos motivosContratos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivosContratos));
            tx.commit();

        } catch (Exception e) {
            log.error("Error PersistenciaMotivosContratos.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public MotivosContratos buscarMotivoContrato(EntityManager em, BigInteger secuenciaMotivosContratos) {
        try {
            em.clear();
            return em.find(MotivosContratos.class, secuenciaMotivosContratos);
        } catch (Exception e) {
            log.error("ERROR PersistenciaMotivosContratos buscarMotivosContratos ERROR  ", e);
            return null;
        }
    }

    @Override
    public List<MotivosContratos> motivosContratos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM MotivosContratos m ORDER BY m.codigo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<MotivosContratos> motivosContratos = query.getResultList();
            return motivosContratos;
        } catch (Exception e) {
            log.error("PersistenciaMotivosContratos.motivosContratos():  ", e);
            return null;
        }
    }

    @Override
    public List<MotivosContratos> buscarMotivosContratos(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MotivosContratos.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            log.error(" ERROR EN PersistenciaMotivosContratos buscarMotivosCambiosCargos ERROR ", e);
            return null;
        }
    }

    @Override
    public BigInteger verificarBorradoVigenciasTiposContratos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            Query query = em.createQuery("SELECT count(v) FROM VigenciasTiposContratos v WHERE v.motivocontrato.secuencia =:secTipoCentroCosto ");
            query.setParameter("secTipoCentroCosto", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            retorno = new BigInteger(query.getSingleResult().toString());
        } catch (Exception e) {
            log.error("ERROR EN PersistenciaMotivosMotivosContratos verificarBorrado ERROR :  ", e);
        } finally {
            return retorno;
        }
    }
}
