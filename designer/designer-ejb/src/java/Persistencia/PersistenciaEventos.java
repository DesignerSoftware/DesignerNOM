/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Eventos;
import InterfacePersistencia.PersistenciaEventosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Eventos' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaEventos implements PersistenciaEventosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, Eventos eventos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(eventos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEventos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Eventos eventos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(eventos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEventos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Eventos eventos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(eventos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaEventos.borrar: " + e);
            }
        }
    }

    @Override
    public Eventos buscarEvento(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(Eventos.class, secuencia);
        } catch (Exception e) {
            System.out.println("Error en la PersistenciaEventos : " + e);
            return null;
        }
    }

    @Override
    public List<Eventos> buscarEventos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM Eventos e ORDER BY e.codigo DESC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Eventos> eventos = query.getResultList();
            return eventos;
        } catch (Exception e) {
            System.out.println("Error en PersistenciaEventos Por buscarEventos ERROR" + e);
            return null;
        }
    }

    public BigInteger contadorVigenciasEventos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM vigenciaseventos WHERE evento = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.out.println("Contador PersitenciaEventos contadorVigenciasEventos persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersitenciaEventos contadorVigenciasEventos. " + e);
            return retorno;
        }
    }

}
