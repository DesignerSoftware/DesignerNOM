/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Monedas;
import InterfacePersistencia.PersistenciaMonedasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Monedas' de la base
 * de datos.
 *
 * @author Viktor
 */
@Stateless

public class PersistenciaMonedas implements PersistenciaMonedasInterface {

   private static Logger log = Logger.getLogger(PersistenciaMonedas.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Monedas monedas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(monedas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaMonedas.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Monedas monedas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(monedas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaMonedas.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Monedas monedas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(monedas));
            tx.commit();

        } catch (Exception e) {
            log.error("Error PersistenciaMonedas.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public BigInteger contadorProyectos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*) FROM proyectos WHERE tipomoneda = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error(" PersistenciaMonedas contadorIdiomasPersonas Error : " + e.getMessage());
            return retorno;
        }
    }

    @Override
    public Monedas consultarMoneda(EntityManager em, BigInteger secuenciaTI) {
        try {
            em.clear();
            return em.find(Monedas.class, secuenciaTI);
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaMonedas.consultarMoneda()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Monedas> consultarMonedas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM Monedas m ORDER BY m.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Monedas> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaMonedas.consultarMonedas()" + e.getMessage());
            return null;
        }
    }
}
