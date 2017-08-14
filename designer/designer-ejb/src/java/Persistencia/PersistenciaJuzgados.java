/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaJuzgadosInterface;
import Entidades.Juzgados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Juzgados' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaJuzgados implements PersistenciaJuzgadosInterface {

   private static Logger log = Logger.getLogger(PersistenciaJuzgados.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     * //
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, Juzgados juzgados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(juzgados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaJuzgados.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Juzgados juzgados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(juzgados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaJuzgados.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Juzgados juzgados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(juzgados));
            tx.commit();

        } catch (Exception e) {
            log.error("Error PersistenciaJuzgados.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Juzgados buscarJuzgado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(Juzgados.class, secuencia);
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaJuzgados.buscarJuzgado()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Juzgados> buscarJuzgados(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM Juzgados m ORDER BY m.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Juzgados> listaMotivosPrestamos = query.getResultList();
            return listaMotivosPrestamos;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaJuzgados.buscarJuzgados()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Juzgados> buscarJuzgadosPorCiudad(EntityManager em, BigInteger secCiudad) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT cce FROM Juzgados cce WHERE cce.ciudad.secuencia = :secuenciaJuzgado ORDER BY cce.codigo ASC");
            query.setParameter("secuenciaJuzgado", secCiudad);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Juzgados> listaJuzgadosPorCiudad = query.getResultList();
            return listaJuzgadosPorCiudad;
        } catch (Exception e) {
            log.error("Error en Persistencia PersistenciaCentrosCostos BuscarCentrosCostosEmpr " + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorEerPrestamos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            log.warn("Persistencia secuencia borrado " + secuencia);
            String sqlQuery = " SELECT COUNT(*)FROM eersprestamos ers , juzgados juz WHERE ers.juzgado = juz.secuencia AND juz.secuencia = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("PERSISTENCIAJUZGADOS CONTADOREERPRESTAMOS = " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAJUZGADOS CONTADOREERPRESTAMOS ERROR = " + e.getMessage());
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

}
