/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasGruposConceptos;
import InterfacePersistencia.PersistenciaVigenciasGruposConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasGruposConceptos'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasGruposConceptos implements PersistenciaVigenciasGruposConceptosInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public void crear(EntityManager em, VigenciasGruposConceptos vigenciasGruposConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciasGruposConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasGruposConceptos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasGruposConceptos vigenciasGruposConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasGruposConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasGruposConceptos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasGruposConceptos vigenciasGruposConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasGruposConceptos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasGruposConceptos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public boolean verificacionGrupoUnoConcepto(EntityManager em, BigInteger secuenciaConcepto) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(vgc) FROM VigenciasGruposConceptos vgc WHERE vgc.concepto.secuencia = :secuenciaConcepto AND vgc.gruposConceptos.codigo = 1");
            query.setParameter("secuenciaConcepto", secuenciaConcepto);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            System.out.println("Exepcion PersistenciaVigenciasConceptosRL: " + e);
            return false;
        }
    }

    @Override
    public List<VigenciasGruposConceptos> listVigenciasGruposConceptosPorConcepto(EntityManager em, BigInteger secuenciaC) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vgc FROM VigenciasGruposConceptos vgc WHERE vgc.concepto.secuencia = :secuenciaConcepto");
            query.setParameter("secuenciaConcepto", secuenciaC);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasGruposConceptos> resultado = (List<VigenciasGruposConceptos>) query.getResultList();
            return resultado;
        } catch (Exception e) {
            System.out.println("Exepcion listVigenciasGruposConceptosPorConcepto PersistenciaVigenciasGruposConceptos: " + e.toString());
            return null;
        }
    }
    
    @Override
    public List<VigenciasGruposConceptos> listVigenciasGruposConceptosPorGrupoConcepto(EntityManager em, BigInteger secuenciaG) {
        try {
            em.clear();
            String sqlQuery = ("SELECT *\n"
                    + "FROM VIGENCIASGRUPOSCONCEPTOS V\n"
                    + "WHERE V.GRUPOCONCEPTO = ? AND EXISTS (SELECT 1 FROM CONCEPTOS C WHERE C.SECUENCIA = V.CONCEPTO)");
            Query query = em.createNativeQuery(sqlQuery,VigenciasGruposConceptos.class);
            query.setParameter(1, secuenciaG);
            List<VigenciasGruposConceptos> resultado = query.getResultList();
            return resultado;
        } catch (Exception e) {
            System.out.println("Error: (prorrogaMostrar)" + e);
            return null;
        }
    }
}
