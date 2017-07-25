/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Profesiones;
import InterfacePersistencia.PersistenciaProfesionesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
/**
 * Clase Stateless. <br> 
 * Clase encargada de realizar operaciones sobre la tabla 'Profesiones'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaProfesiones implements PersistenciaProfesionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaProfesiones.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     * @param em
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
        public List<Profesiones> profesiones(EntityManager em) {
        try {
            em.clear();
            String sql ="SELECT * FROM PROFESIONES";
            Query query = em.createNativeQuery(sql,Profesiones.class);
            List<Profesiones> profesiones = query.getResultList();
            return profesiones;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaProfesiones.profesiones()" + e.getMessage());
            return null;
        }
    }

        @Override
    public void crear(EntityManager em, Profesiones profesion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(profesion);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaProfesiones.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Profesiones profesion) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(profesion));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                log.error("Error PersistenciaProfesiones.borrar: " + e.getMessage());
            }
        }
    }

    @Override
    public void editar(EntityManager em, Profesiones profesion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(profesion);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaProfesiones.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
