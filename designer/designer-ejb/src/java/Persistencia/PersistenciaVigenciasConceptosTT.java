/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasConceptosTT;
import InterfacePersistencia.PersistenciaVigenciasConceptosTTInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasConceptosTT'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasConceptosTT implements PersistenciaVigenciasConceptosTTInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasConceptosTT.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override 
    public void crear(EntityManager em, VigenciasConceptosTT conceptosTT) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(conceptosTT);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasConceptosTT.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override 
    public void editar(EntityManager em, VigenciasConceptosTT conceptosTT) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(conceptosTT);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasConceptosTT.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override 
    public void borrar(EntityManager em, VigenciasConceptosTT conceptosTT) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(conceptosTT));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasConceptosTT.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public boolean verificacionZonaTipoTrabajador(EntityManager em, BigInteger secuenciaConcepto, BigInteger secuenciaTT) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vcTT FROM VigenciasConceptosTT vcTT WHERE vcTT.concepto.secuencia = :secuenciaConcepto AND vcTT.tipotrabajador.secuencia = :secuenciaTT");
            query.setParameter("secuenciaConcepto", secuenciaConcepto);
            query.setParameter("secuenciaTT", secuenciaTT);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            log.error("Exepcion PersistenciaVigenciasConceptosTT:  ", e);
            return false;
        }
    }
    
    @Override
    public List<VigenciasConceptosTT> listVigenciasConceptosTTPorConcepto(EntityManager em, BigInteger secuenciaC){
        try {
            em.clear();
            Query query = em.createQuery("SELECT vcTT FROM VigenciasConceptosTT vcTT WHERE vcTT.concepto.secuencia = :secuenciaConcepto");
            query.setParameter("secuenciaConcepto", secuenciaC);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasConceptosTT> resultado = (List<VigenciasConceptosTT>) query.getResultList();
            return resultado;
        } catch (Exception e) {
            log.error("Exepcion listVigenciasConceptosTTPorConcepto PersistenciaVigenciasConceptosTT:  ", e);
            return null;
        }
    }
}
