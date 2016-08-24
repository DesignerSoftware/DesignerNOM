/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MotivosReemplazos;
import InterfacePersistencia.PersistenciaMotivosReemplazosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosReemplazos' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaMotivosReemplazos implements PersistenciaMotivosReemplazosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */

    public void crear(EntityManager em, MotivosReemplazos motivoReemplazo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivoReemplazo);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosReemplazos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, MotivosReemplazos motivoReemplazo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivoReemplazo);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosReemplazos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, MotivosReemplazos motivoReemplazo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivoReemplazo));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaMotivosReemplazos.borrar: " + e);
            }
        }
    }

    @Override
    public List<MotivosReemplazos> motivosReemplazos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT mR FROM MotivosReemplazos mR ORDER BY mR.codigo");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<MotivosReemplazos> motivosReemplazos = query.getResultList();
            return motivosReemplazos;
        } catch (Exception e) {
            return null;
        }
    }

    public BigInteger contarEncargaturasMotivoReemplazo(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM encargaturas sm WHERE sm.motivoreemplazo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            System.out.println("persistenciaMotivosReemplazos contarEncargaturasMotivoReemplazo retorno : " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("persistenciaMotivosReemplazos  contarEncargaturasMotivoReemplazo ERROR : " + e);
            return retorno;
        }
    }
}
