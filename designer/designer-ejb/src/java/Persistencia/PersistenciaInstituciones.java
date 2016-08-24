/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Instituciones;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Instituciones' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaInstituciones implements PersistenciaInstitucionesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//   @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, Instituciones instituciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(instituciones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInstituciones.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Instituciones instituciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(instituciones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaInstituciones.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Instituciones instituciones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(instituciones));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaInstituciones.borrar: " + e);
            }
        }
    }

    @Override
    public Instituciones buscarInstitucion(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            BigInteger sec = new BigInteger(secuencia.toString());
            return em.find(Instituciones.class, sec);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Instituciones> instituciones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT i FROM Instituciones i");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Instituciones> listaInstituciones = query.getResultList();
            return listaInstituciones;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTelefonos.tiposTelefonos" + e);
            return null;
        }
    }
}
