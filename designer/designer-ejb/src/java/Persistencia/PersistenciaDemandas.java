/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Demandas;
import InterfacePersistencia.PersistenciaDemandasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Demandas'
 * de la base de datos
 * @author betelgeuse
 */
@Stateless
public class PersistenciaDemandas implements PersistenciaDemandasInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em,Demandas demandas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(demandas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDemandas.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em,Demandas demandas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(demandas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDemandas.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em,Demandas demandas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(demandas));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaDemandas.borrar: " + e);
            }
        }
    }

    @Override
    public List<Demandas> demandasPersona(EntityManager em,BigInteger secuenciaEmpl) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(d) FROM Demandas d WHERE d.empleado.secuencia = :secuenciaEmpl");
            query.setParameter("secuenciaEmpl", secuenciaEmpl);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query queryFinal = em.createQuery("SELECT d FROM Demandas d WHERE d.empleado.secuencia = :secuenciaEmpl");
                queryFinal.setParameter("secuenciaEmpl", secuenciaEmpl);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<Demandas> listaDemandas = queryFinal.getResultList();
                return listaDemandas;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error PersistenciaDemandas.demandasPersona" + e);
            return null;
        }
    }
}
