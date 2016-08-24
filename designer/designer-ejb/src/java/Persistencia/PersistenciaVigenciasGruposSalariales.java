/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasGruposSalariales;
import InterfacePersistencia.PersistenciaVigenciasGruposSalarialesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasGruposSalariales'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasGruposSalariales implements PersistenciaVigenciasGruposSalarialesInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public void crear(EntityManager em, VigenciasGruposSalariales vigenciasGruposSalariales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasGruposSalariales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasGruposSalariales.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasGruposSalariales vigenciasGruposSalariales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasGruposSalariales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasGruposSalariales.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasGruposSalariales vigenciasGruposSalariales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasGruposSalariales));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasGruposSalariales.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasGruposSalariales> buscarVigenciasGruposSalariales(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VigenciasGruposSalariales v");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasGruposSalariales> vigenciasGruposSalariales = (List<VigenciasGruposSalariales>) query.getResultList();
            return vigenciasGruposSalariales;
        } catch (Exception e) {
            System.out.println("Error buscarVigenciasGruposSalariales PersistenciaVigenciasGruposSalariales : " + e.toString());
            return null;
        }
    }

    @Override
    public VigenciasGruposSalariales buscarVigenciaGrupoSalarialSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vgs FROM VigenciasGruposSalariales vgs WHERE vgs.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasGruposSalariales vigenciasGruposSalariales = (VigenciasGruposSalariales) query.getSingleResult();
            return vigenciasGruposSalariales;
        } catch (Exception e) {
            System.out.println("Error buscarVigenciaGrupoSalarialSecuencia PersistenciaVigenciasGruposSalariales : " + e.toString());
            VigenciasGruposSalariales vigenciasGruposSalariales = null;
            return vigenciasGruposSalariales;
        }
    }
    
    @Override
    public List<VigenciasGruposSalariales> buscarVigenciaGrupoSalarialSecuenciaGrupoSal(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vgs FROM VigenciasGruposSalariales vgs WHERE vgs.gruposalarial.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasGruposSalariales> vigenciasGruposSalariales = (List<VigenciasGruposSalariales>) query.getResultList();
            return vigenciasGruposSalariales;
        } catch (Exception e) {
            System.out.println("Error buscarVigenciaGrupoSalarialSecuencia PersistenciaVigenciasGruposSalariales : " + e.toString());
            List<VigenciasGruposSalariales> vigenciasGruposSalariales = null;
            return vigenciasGruposSalariales;
        }
    }
}
