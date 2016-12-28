/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Bancos;
import InterfacePersistencia.PersistenciaBancosInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Bancos' de la base de
 * datos
 *
 * @author Andrés Pineda
 */
@Stateless
public class PersistenciaBancos implements PersistenciaBancosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, Bancos bancos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(bancos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaBancos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Bancos bancos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(bancos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaBancos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Bancos bancos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(bancos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaBancos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Bancos> buscarBancos(EntityManager em) {
        try {
            em.clear();
            String sql= "SELECT * FROM BANCOS ORDER BY CODIGO";
            Query query = em.createNativeQuery(sql, Bancos.class);
            List<Bancos> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error buscarBancos persistencia bancos : " + e.toString());
            return null;
        }
    }
}
