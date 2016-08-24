package Persistencia;

import Entidades.ParametrosAutoliq;
import InterfacePersistencia.PersistenciaParametrosAutoliqInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaParametrosAutoliq implements PersistenciaParametrosAutoliqInterface{

    @Override
    public void crear(EntityManager em, ParametrosAutoliq autoliq) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(autoliq);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosAutoliq.crear : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, ParametrosAutoliq autoliq) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(autoliq);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosAutoliq.editar : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, ParametrosAutoliq autoliq) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(autoliq));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosAutoliq.borrar : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<ParametrosAutoliq> consultarParametrosAutoliq(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM ParametrosAutoliq p ORDER BY p.ano ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ParametrosAutoliq> listaParametros = query.getResultList();
            return listaParametros;
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosAutoliq.consultarParametrosAutoliq : " + e.toString());
            return null;
        }
    }
    
    @Override
    public List<ParametrosAutoliq> consultarParametrosAutoliqPorEmpresas(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM parametrosautoliq p WHERE EXISTS (SELECT 'x' FROM empresas e WHERE e.secuencia = p.empresa) ORDER BY p.ano DESC";
            Query query = em.createNativeQuery(sql, ParametrosAutoliq.class);
            List<ParametrosAutoliq> listaParametros = query.getResultList();
            return listaParametros;
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosAutoliq.consultarParametrosAutoliqPorEmpresas : " + e.toString());
            return null;
        }
    }
}
