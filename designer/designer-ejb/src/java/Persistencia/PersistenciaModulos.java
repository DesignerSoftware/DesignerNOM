/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Modulos;
import InterfacePersistencia.PersistenciaModulosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Modulos' de la base
 * de datos.
 *
 * @author -Felipphe- Felipe Triviño
 */
@Stateless
public class PersistenciaModulos implements PersistenciaModulosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Modulos modulos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(modulos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaModulos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Modulos modulos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(modulos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaModulos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Modulos modulos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(modulos));
            tx.commit();

        } catch (Exception e) {
            System.out.println("Error PersistenciaModulos.borrar: " + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public Modulos buscarModulos(EntityManager em, BigInteger secuencia) {
        try{
        em.clear();
        return em.find(Modulos.class, secuencia);
        }catch(Exception e){
            System.out.println("Persistencia.PersistenciaModulos.buscarModulos()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Modulos> buscarModulos(EntityManager em) {
        try {
            em.clear();
            System.out.println("buscarModulos 1");
            Query query = em.createQuery("SELECT m FROM Modulos m");
            System.out.println("buscarModulos 2");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            System.out.println("buscarModulos 3");
            List<Modulos> lista = query.getResultList();
            System.out.println("buscarModulos 4");
            return lista;
        } catch (Exception e) {
            System.out.println("Error buscarModulos PersistenciaModulos : " + e.toString());
            return null;
        }
    }
    
    @Override
    public Modulos buscarModulosPorSecuencia(EntityManager em, BigInteger secModulo) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM Modulos m where m.secuencia = :secModulo");
            query.setParameter("secModulo", secModulo);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Modulos modu = (Modulos) query.getSingleResult();
            return modu;
        } catch (Exception e) {
            System.out.println("Error buscarModulos PersistenciaModulos : " + e.toString());
            return null;
        }
    }

}
