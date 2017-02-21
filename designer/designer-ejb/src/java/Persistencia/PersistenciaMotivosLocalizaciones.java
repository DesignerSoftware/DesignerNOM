/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MotivosLocalizaciones;
import InterfacePersistencia.PersistenciaMotivosLocalizacionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'MotivosLocalizaciones' de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaMotivosLocalizaciones implements PersistenciaMotivosLocalizacionesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, MotivosLocalizaciones motivosLocalizaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosLocalizaciones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosLocalizaciones.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, MotivosLocalizaciones motivosLocalizaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosLocalizaciones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosLocalizaciones.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, MotivosLocalizaciones motivosLocalizaciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivosLocalizaciones));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosLocalizaciones.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<MotivosLocalizaciones> buscarMotivosLocalizaciones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM MotivosLocalizaciones m");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<MotivosLocalizaciones> motivosL = (List<MotivosLocalizaciones>) query.getResultList();
            return motivosL;
        } catch (Exception e) {
            System.out.println("Error buscarMotivosLocalizaciones PersistenciaMotivosLovalizaciones" + e.getMessage());
            return null;
        }
    }

    @Override
    public MotivosLocalizaciones buscarMotivoLocalizacionSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM MotivosLocalizaciones m WHERE m.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            MotivosLocalizaciones motivoL = (MotivosLocalizaciones) query.getSingleResult();
            return motivoL;
        } catch (Exception e) {
            System.out.println("Error buscarMotivoLocalizacionSecuencia PersistenciaMotivosLocalizaciones" + e.getMessage());
            MotivosLocalizaciones motivoL = null;
            return motivoL;
        }
    }

    public BigInteger contarVigenciasLocalizacionesMotivoLocalizacion(EntityManager em, BigInteger secMotivoLocalizacion) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM vigenciaslocalizaciones WHERE motivo =?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secMotivoLocalizacion);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.out.println("Error PERSISTENCIAMOTIVOSLOCALIZACIONES  contarVigenciasLocalizacionesMotivoLocalizacion. " + e.getMessage());
            return retorno;
        }
    }

}
