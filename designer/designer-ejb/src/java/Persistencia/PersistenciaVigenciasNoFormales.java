/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasNoFormales;
import InterfacePersistencia.PersistenciaVigenciasNoFormalesInterface;
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
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasNoFormales'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasNoFormales implements PersistenciaVigenciasNoFormalesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasNoFormales.class);

    @Override
    public void crear(EntityManager em, VigenciasNoFormales vigenciasNoFormales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasNoFormales);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasNoFormales.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasNoFormales vigenciasNoFormales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasNoFormales);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasNoFormales.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasNoFormales vigenciasNoFormales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasNoFormales));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasNoFormales.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasNoFormales> buscarVigenciasNoFormales(EntityManager em) {
        try{
        em.clear();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(VigenciasNoFormales.class));
        return em.createQuery(cq).getResultList();
        }catch(Exception e){
            log.error("Persistencia.PersistenciaVigenciasNoFormales.buscarVigenciasNoFormales()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VigenciasNoFormales> vigenciasNoFormalesPersona(EntityManager em, BigInteger secuenciaPersona) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vNF FROM VigenciasNoFormales vNF WHERE vNF.persona.secuencia = :secuenciaPersona ORDER BY vNF.fechavigencia DESC");
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasNoFormales> listaVigenciasNoFormales = query.getResultList();
            return listaVigenciasNoFormales;
        } catch (Exception e) {
            log.error("Error PersistenciaTelefonos.telefonoPersona" + e.getMessage());
            return null;
        }
    }
}
