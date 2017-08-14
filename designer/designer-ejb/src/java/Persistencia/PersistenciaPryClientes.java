/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.PryClientes;
import InterfacePersistencia.PersistenciaPryClientesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'PryClientes' de la
 * base de datos.
 *
 * @author Viktor
 */
@Stateless
public class PersistenciaPryClientes implements PersistenciaPryClientesInterface {

   private static Logger log = Logger.getLogger(PersistenciaPryClientes.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    public void crear(EntityManager em, PryClientes pryClientes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pryClientes);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPryClientes.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, PryClientes pryClientes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pryClientes);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPryClientes.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, PryClientes pryClientes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(pryClientes));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPryClientes.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public PryClientes buscarPryCliente(EntityManager em, BigInteger secuenciaPC) {
        try {
            em.clear();
            return em.find(PryClientes.class, secuenciaPC);
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaPryClientes.buscarPryCliente()" + e.getMessage()); 
            return null;
        }
    }

    @Override
    public List<PryClientes> buscarPryClientes(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT pc FROM PryClientes pc ORDER BY pc.nombre ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<PryClientes> pryclientes = query.getResultList();
            return pryclientes;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaPryClientes.buscarPryClientes()" + e.getMessage());
            return null;
        }
    }

    public BigInteger contadorProyectos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM  proyectos WHERE pry_cliente = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("persistenciapryclientes contadorProyectos Contador " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error  persistenciapryclientes contadorProyectos. " + e);
            return retorno;
        }
    }
}
