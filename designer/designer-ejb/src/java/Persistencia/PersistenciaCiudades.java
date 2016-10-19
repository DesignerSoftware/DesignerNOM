/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Ciudades;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Ciudades' de la base
 * de datos
 *
 * @author Betelgeuse
 */
@Stateless
public class PersistenciaCiudades implements PersistenciaCiudadesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, Ciudades ciudades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ciudades);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaCiudades.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Ciudades ciudades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ciudades);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaCiudades.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Ciudades ciudades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(ciudades));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaCiudades.borrar: " + e);
            }
        }
    }

    @Override
    public List<Ciudades> consultarCiudades(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Ciudades c ORDER BY c.nombre");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Ciudades> ciudades = query.getResultList();
            return ciudades;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Ciudades> lovCiudades(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT  SECUENCIA,  NOMBRE FROM  CIUDADES ORDER BY NOMBRE";
            Query query = em.createNativeQuery(sql, Ciudades.class);
            List<Ciudades> ciudades = query.getResultList();
            return ciudades;
        } catch (Exception e) {
            return null;
        }
    }

}
