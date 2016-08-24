/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Telefonos;
import InterfacePersistencia.PersistenciaTelefonosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Telefonos' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTelefonos implements PersistenciaTelefonosInterface {

    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     */
    @Override
    public void crear(EntityManager em, Telefonos telefonos) {
        System.out.println(this.getClass().getName() + ".crear()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(telefonos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("error en crear");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Telefonos telefonos) {
            System.out.println(this.getClass().getName() + ".editar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(telefonos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("error en editar");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("transaccion cerrada");
        }
    }

    @Override
    public void borrar(EntityManager em, Telefonos telefonos) {
            System.out.println(this.getClass().getName() + ".borrar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(telefonos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("error en borrar()");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("se cerro la transaccion");
        }
    }

    @Override
    public Telefonos buscarTelefono(EntityManager em, BigInteger secuencia) {
            System.out.println(this.getClass().getName() + ".buscarTelefono()");
        try {
            em.clear();
            return em.find(Telefonos.class, secuencia);
        } catch (Exception e) {
            System.out.println("error en buscarTelefono");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Telefonos> buscarTelefonos(EntityManager em) {
            System.out.println(this.getClass().getName()+".buscarTelefonos()");
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Telefonos.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("error en buscarTelefonos");
            return null;
        }
    }

    @Override
    public List<Telefonos> telefonosPersona(EntityManager em, BigInteger secuenciaPersona) {
            System.out.println(this.getClass().getName()+"telefonosPersona()");
        try {
            em.clear();
            String consulta ="SELECT t "
                    + "FROM Telefonos t "
                    + "WHERE t.persona.secuencia = :secuenciaPersona "
                    + "ORDER BY t.fechavigencia DESC";
            Query query = em.createQuery(consulta);
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Telefonos> listaTelefonos = query.getResultList();
            return listaTelefonos;
        } catch (Exception e) {
            System.out.println("error en telefonosPersona");
            e.printStackTrace();
            return null;
        }
    }
}
