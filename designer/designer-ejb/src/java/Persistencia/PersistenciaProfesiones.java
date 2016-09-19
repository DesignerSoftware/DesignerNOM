/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Profesiones;
import InterfacePersistencia.PersistenciaProfesionesInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
/**
 * Clase Stateless. <br> 
 * Clase encargada de realizar operaciones sobre la tabla 'Profesiones'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaProfesiones implements PersistenciaProfesionesInterface{
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
            System.out.println("Error PersistenciaProfesiones.crear: " + e);
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
                System.out.println("Error PersistenciaProfesiones.borrar: " + e);
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
            System.out.println("Error PersistenciaProfesiones.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
