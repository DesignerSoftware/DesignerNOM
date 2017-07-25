/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ExtrasRecargos;
import InterfacePersistencia.PersistenciaExtrasRecargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'ExtrasRecargos' de la
 * base de datos.
 *
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaExtrasRecargos implements PersistenciaExtrasRecargosInterface {

   private static Logger log = Logger.getLogger(PersistenciaExtrasRecargos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, ExtrasRecargos extrasRecargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(extrasRecargos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaExtrasRecargos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, ExtrasRecargos extrasRecargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(extrasRecargos);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.error("Error PersistenciaExtrasRecargos.editar: " + e);
        }
    }

    @Override
    public void borrar(EntityManager em, ExtrasRecargos extrasRecargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(extrasRecargos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                log.error("Error PersistenciaExtrasRecargos.borrar: " + e);
            }
        }
    }

    @Override
    public ExtrasRecargos buscarExtraRecargo(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(ExtrasRecargos.class, secuencia);
        } catch (Exception e) {
            log.error("Error PersistenciaExtrasRecargos buscarExtraRecargo : " + e.toString());
            return null;
        }
    }

    @Override
    public List<ExtrasRecargos> buscarExtrasRecargos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT er FROM ExtrasRecargos er ORDER BY er.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ExtrasRecargos> extrasRecargos = query.getResultList();
            return extrasRecargos;
        } catch (Exception e) {
            log.error("Error PersistenciaExtrasRecargos buscarExtrasRecargos : " + e.toString());
            return null;
        }
    }
}
