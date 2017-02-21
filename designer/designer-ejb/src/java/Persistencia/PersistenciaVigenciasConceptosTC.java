/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasConceptosTC;
import InterfacePersistencia.PersistenciaVigenciasConceptosTCInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasConceptosTC'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasConceptosTC implements PersistenciaVigenciasConceptosTCInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public void crear(EntityManager em, VigenciasConceptosTC vigenciasConceptosTC) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciasConceptosTC);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasConceptosTC.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasConceptosTC vigenciasConceptosTC) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasConceptosTC);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasConceptosTC.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasConceptosTC vigenciasConceptosTC) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasConceptosTC));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasConceptosTC.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public boolean verificacionZonaTipoContrato(EntityManager em, BigInteger secuenciaC, BigInteger secuenciaTC) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(vcTC) FROM VigenciasConceptosTC vcTC WHERE vcTC.concepto.secuencia = :secuenciaConcepto AND vcTC.tipocontrato.secuencia = :secuenciaTC");
            query.setParameter("secuenciaConcepto", secuenciaC);
            query.setParameter("secuenciaTC", secuenciaTC);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            System.out.println("Exepcion PersistenciaVigenciasConceptosTC: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<VigenciasConceptosTC> listVigenciasConceptosTCPorConcepto(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vcTC FROM VigenciasConceptosTC vcTC WHERE vcTC.concepto.secuencia = :secuenciaConcepto");
            query.setParameter("secuenciaConcepto", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasConceptosTC> resultado = (List<VigenciasConceptosTC>) query.getResultList();
            return resultado;
        } catch (Exception e) {
            System.out.println("Exepcion listVigenciasConceptosTCPorConcepto PersistenciaVigenciasConceptosTC : " + e.getMessage());
            return null;
        }
    }
}
