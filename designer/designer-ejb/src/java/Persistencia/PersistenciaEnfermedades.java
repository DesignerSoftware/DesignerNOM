/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Enfermedades;
import InterfacePersistencia.PersistenciaEnfermedadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless Clase encargada de realizar operaciones sobre la tabla
 * 'Enfermedades' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaEnfermedades implements PersistenciaEnfermedadesInterface {

   private static Logger log = Logger.getLogger(PersistenciaEnfermedades.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, Enfermedades enfermedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(enfermedades);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaEnfermedades.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Enfermedades enfermedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(enfermedades);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaEnfermedades.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Enfermedades enfermedades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(enfermedades));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                log.error("Error PersistenciaEnfermedades.borrar: " + e);
            }
        }
    }

    @Override
    public Enfermedades buscarEnfermedad(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(Enfermedades.class, secuencia);
        } catch (Exception e) {
            log.error("Error en la persistenciaEnfermedadesERROR : " + e);
            return null;
        }
    }

    @Override
    public List<Enfermedades> buscarEnfermedades(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM Enfermedades e ORDER BY e.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Enfermedades> enfermedades = query.getResultList();
            return enfermedades;
        } catch (Exception e) {
            log.error("Error en PersistenciaEnfermedadesProfesionales Por Empleados ERROR" + e);
            return null;
        }
    }

    public BigInteger contadorAusentimos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM ausentismos WHERE enfermedad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.error("PERSISTENCIAENFERMEDADES contadorAusentimos = " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAENFERMEDADES contadorAusentimos  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    public BigInteger contadorDetallesLicencias(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM detalleslicencias WHERE enfermedad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.error("PERSISTENCIAENFERMEDADES contadorDetallesLicencias = " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAENFERMEDADES contadorDetallesLicencias  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    public BigInteger contadorEnfermedadesPadecidas(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM enfermedadespadecidas WHERE enfermedad= ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.error("PERSISTENCIAENFERMEDADES contadorEnfermedadesPadecidas = " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAENFERMEDADES contadorEnfermedadesPadecidas  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    public BigInteger contadorSoausentismos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM soausentismos e  WHERE enfermedad = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.error("PERSISTENCIAENFERMEDADES contadorSoausentismos = " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAENFERMEDADES contadorSoausentismos  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    public BigInteger contadorSorevisionessSistemas(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM sorevisionessistemas e WHERE enfermedadactual = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.error("PERSISTENCIAENFERMEDADES contadorSorevisionessSistemas = " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAENFERMEDADES contadorSorevisionessSistemas  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
