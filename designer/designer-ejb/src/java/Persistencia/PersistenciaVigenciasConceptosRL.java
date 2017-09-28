/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasConceptosRL;
import InterfacePersistencia.PersistenciaVigenciasConceptosRLInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasConceptosRL'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasConceptosRL implements PersistenciaVigenciasConceptosRLInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasConceptosRL.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public void crear(EntityManager em, VigenciasConceptosRL vigenciasConceptosRL) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciasConceptosRL);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasConceptosRL.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasConceptosRL vigenciasConceptosRL) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasConceptosRL);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasConceptosRL.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasConceptosRL vigenciasConceptosRL) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasConceptosRL));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasConceptosRL.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    @Override
    public boolean verificacionZonaTipoReformasLaborales(EntityManager em, BigInteger secuenciaC, BigInteger secuenciaTS) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(vcRL) FROM VigenciasConceptosRL vcRL WHERE vcRL.concepto.secuencia = :secuenciaConcepto AND vcRL.tiposalario.secuencia = :secuenciaTS");
            query.setParameter("secuenciaConcepto", secuenciaC);
            query.setParameter("secuenciaTS", secuenciaTS);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            log.error("Exepcion PersistenciaVigenciasConceptosRL:  ", e);
            return false;
        }
    }
    
    @Override
    public List<VigenciasConceptosRL> listVigenciasConceptosRLPorConcepto(EntityManager em, BigInteger secuencia){
        try {
            em.clear();
            Query query = em.createQuery("SELECT vcRL FROM VigenciasConceptosRL vcRL WHERE vcRL.concepto.secuencia = :secuenciaConcepto");
            query.setParameter("secuenciaConcepto", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasConceptosRL> resultado = (List<VigenciasConceptosRL>) query.getResultList();
            return resultado;
        } catch (Exception e) {
            log.error("Exepcion PersistenciaVigenciasConceptosRL:  ", e);
            return null;
        }
    }
}
