/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Proyectos;
import InterfacePersistencia.PersistenciaProyectosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br> 
 * Clase encargada de realizar operaciones sobre la tabla 'Proyectos'
 * de la base de datos.
 * @author AndresPineda
 */
@Stateless
public class PersistenciaProyectos implements PersistenciaProyectosInterface {

   private static Logger log = Logger.getLogger(PersistenciaProyectos.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, Proyectos proyectos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(proyectos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaProyectos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Proyectos proyectos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(proyectos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaProyectos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Proyectos proyectos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(proyectos));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaProyectos.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }    
    
    @Override
    public Proyectos buscarProyectoSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Proyectos p WHERE p.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Proyectos proyectos = (Proyectos) query.getSingleResult();
            return proyectos;
        } catch (Exception e) {
            log.error("Error buscarProyectoSecuencia PersistenciaProyectos"+ e.getMessage());
            Proyectos proyectos = null;
            return proyectos;
        }
    }
     
   @Override
    public List<Proyectos> proyectos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Proyectos p ORDER BY p.empresa.nombre");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Proyectos> proyectos = query.getResultList();
            return proyectos;
        } catch (Exception e) {
           log.error("Error en PersistenciaProyectos, proyectos() : " + e.getCause().toString());
            return null;
        }
    }
   
    @Override
   public Proyectos buscarProyectoNombre(EntityManager em, String nombreP){
       try{
           em.clear();
           Query query = em.createQuery("SELECT p FROM Proyectos p WHERE p.nombreproyecto =:nombreP");
           query.setParameter("nombreP", nombreP);
           query.setHint("javax.persistence.cache.storeMode", "REFRESH");
           Proyectos pry = (Proyectos) query.getSingleResult();
           return pry;
       }catch(Exception e){
           log.error("Error buscarProyectoNombre PersistenciaProyectos : "+e.toString());
           return null;
       }
   }
}
