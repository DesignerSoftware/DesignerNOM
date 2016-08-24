/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Personas;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import InterfacePersistencia.PersistenciaPersonasInterface;
import java.math.BigInteger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Personas' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaPersonas implements PersistenciaPersonasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Personas personas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(personas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPersonas.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Personas personas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(personas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPersonas.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Personas personas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(personas));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPersonas.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Personas buscarPersona(EntityManager em, BigInteger secuencia) {
        em.clear();
        return em.find(Personas.class, secuencia);
    }

    @Override
    public List<Personas> consultarPersonas(EntityManager em) {
        em.clear();
        String sql = "SELECT * FROM Personas";
        Query query = em.createNativeQuery(sql, Personas.class);
        List<Personas> lista = query.getResultList();
        return lista;
    }

    @Override
    public void actualizarFotoPersona(EntityManager em, BigInteger identificacion) {
        try {
            em.clear();
            em.getTransaction().begin();
            Query query = em.createNativeQuery("update Personas p set p.pathfoto='S' where p.numerodocumento = ?");
            query.setParameter(1, identificacion);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No se pudo agregar estado de fotografia: " + e);
        }
    }

    @Override
    public Personas buscarFotoPersona(EntityManager em, BigInteger identificacion) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p from Personas p where p.numerodocumento = :identificacion");
            query.setParameter("identificacion", identificacion);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Personas persona = (Personas) query.getSingleResult();
            return persona;
        } catch (Exception e) {
            Personas personas = null;
            return personas;
        }
    }

    @Override
    public Personas buscarPersonaSecuencia(EntityManager em, BigInteger secuencia) {
        Personas persona;
        try {
            em.clear();
            String sql = "SELECT * FROM Personas  WHERE secuencia = ?";
            Query query = em.createNativeQuery(sql, Personas.class);
            query.setParameter(1, secuencia);
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            persona = (Personas) query.getSingleResult();
            return persona;
        } catch (Exception e) {
            persona = null;
            System.out.println("Error buscarPersonaSecuencia PersistenciaPersonas");
        }
        return persona;
    }

    @Override
    public Personas buscarPersonaPorNumeroDocumento(EntityManager em, BigInteger numeroDocumento) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Personas p WHERE p.numerodocumento=:numeroDocumento AND p.digitoverificaciondocumento is null");
            query.setParameter("numeroDocumento", numeroDocumento);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Personas persona = (Personas) query.getSingleResult();
            return persona;
        } catch (Exception e) {
            System.out.println("Error buscarPersonaPorNumeroDocumento PersistenciaPersonas : " + e.toString());
            return null;
        }
    }

    @Override
    public Personas obtenerUltimaPersonaAlmacenada(EntityManager em, BigInteger documento) {
        try {
            em.clear();
            System.out.println("documento : " + documento);
            Query query = em.createQuery("SELECT p FROM Personas p WHERE p.numerodocumento=:documento");
            query.setParameter("documento", documento);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Personas persona = (Personas) query.getSingleResult();
            return persona;
        } catch (Exception e) {
            System.out.println("Error obtenerUltimaPersonaAlmacenada PersistenciaPersonas : " + e.toString());
            return null;
        }
    }
}
