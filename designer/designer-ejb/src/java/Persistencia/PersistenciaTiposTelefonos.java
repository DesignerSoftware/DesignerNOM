/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposTelefonos;
import InterfacePersistencia.PersistenciaTiposTelefonosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposTelefonos' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateful
public class PersistenciaTiposTelefonos implements PersistenciaTiposTelefonosInterface {

    @Override
    public void crear(EntityManager em, TiposTelefonos tiposTelefonos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(tiposTelefonos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTelefonos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposTelefonos tiposTelefonos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposTelefonos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTelefonos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposTelefonos tiposTelefonos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            System.out.println("em : " + em);
            System.out.println("tiposTelefonos : "+ tiposTelefonos);
            tx.begin();
            em.remove(em.merge(tiposTelefonos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTelefonos.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public TiposTelefonos buscarTipoTelefonos(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            BigInteger sec = new BigInteger(secuencia.toString());
            return em.find(TiposTelefonos.class, sec);
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposTelefonos.buscarTipoTelefonos()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposTelefonos> tiposTelefonos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tt FROM TiposTelefonos tt ORDER BY tt.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposTelefonos> listaTiposTelefonos = query.getResultList();
            return listaTiposTelefonos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTelefonos.tiposTelefonos" + e.getMessage());
            return null;
        }
    }
}
