/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasCompensaciones;
import InterfacePersistencia.PersistenciaVigenciasCompensacionesInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasCompensaciones'
 * de la base de datos.
 * @author AndresPineda
 */
@Stateless
public class PersistenciaVigenciasCompensaciones implements PersistenciaVigenciasCompensacionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasCompensaciones.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public void crear(EntityManager em, VigenciasCompensaciones vigenciasCompensaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasCompensaciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasCompensaciones.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasCompensaciones vigenciasCompensaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasCompensaciones);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasCompensaciones.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasCompensaciones vigenciasCompensaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasCompensaciones));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasCompensaciones.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasCompensaciones> buscarVigenciasCompensaciones(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VigenciasCompensaciones.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            log.error("Error buscarVigenciasCompensaciones PersistenciaVigenciasCompensaciones" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VigenciasCompensaciones> buscarVigenciasCompensacionesEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vc FROM VigenciasCompensaciones vc WHERE vc.vigenciajornada.empleado.secuencia = :secuenciaEmpl ORDER BY vc.fechainicial DESC");
            query.setParameter("secuenciaEmpl", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasCompensaciones> vigenciasCompensaciones = query.getResultList();
            return vigenciasCompensaciones;
        } catch (Exception e) {
            log.error("Error en buscarVigenciasCompensacionesEmpleado PersistenciaVigenciasCompensaciones " + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasCompensaciones buscarVigenciaCompensacionSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VigenciasCompensaciones v WHERE v.secuencia = :secuencia").setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasCompensaciones vigenciasCompensaciones = (VigenciasCompensaciones) query.getSingleResult();
            return vigenciasCompensaciones;
        } catch (Exception e) {
            log.error("Error buscarVigenciaCompensacionSecuencia PersistenciaVigenciasCompensaciones" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VigenciasCompensaciones> buscarVigenciasCompensacionesVigenciaSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vc FROM VigenciasCompensaciones vc WHERE vc.vigenciajornada.secuencia =:secVigencia ORDER BY vc.fechainicial DESC");
            query.setParameter("secVigencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasCompensaciones> vigenciasCompensaciones = query.getResultList();
            return vigenciasCompensaciones;
        } catch (Exception e) {
            log.error("Error buscarVigenciasCompensacionesVigenciaSecuencia PersistenciaVigenciasCompensaciones" + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<VigenciasCompensaciones> buscarVigenciasCompensacionesTipoCompensacion (EntityManager em, String tipoC){
        try{
            em.clear();
           Query query = em.createQuery("SELECT vc FROM VigenciasCompensaciones vc WHERE vc.tipocompensacion =:tipoCompensacion ORDER BY  vc.fechainicial DESC");
           query.setParameter("tipoCompensacion", tipoC);
           query.setHint("javax.persistence.cache.storeMode", "REFRESH");
           List<VigenciasCompensaciones> vigenciasCompensaciones = query.getResultList();
           return vigenciasCompensaciones;
        }catch(Exception e){
            log.error("Error buscarVigenciasCompensacionesTipoCompensacion PersistenciaVigenciasCompensaciones" + e.getMessage());
            return null;
        }
    }
    
    @Override
     public List<VigenciasCompensaciones> buscarVigenciasCompensacionesVigenciayCompensacion(EntityManager em, String tipoC,BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vc FROM VigenciasCompensaciones vc WHERE vc.tipocompensacion =:tipoCompensacion AND vc.vigenciajornada.secuencia =:secVigencia ORDER BY  vc.fechainicial DESC");
            query.setParameter("tipoCompensacion", tipoC);
            query.setParameter("tipoCompensacion", tipoC);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasCompensaciones> vigenciasCompensaciones = query.getResultList();
            return vigenciasCompensaciones;
        } catch (Exception e) {
            log.error("Error buscarVigenciasCompensacionesVigenciayCompensacion PersistenciaVigenciasCompensaciones" + e.getMessage());
            return null;
        }
    }
}
