/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.DetallesEmpresas;
import InterfacePersistencia.PersistenciaDetallesEmpresasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'DetallesEmpresas' de
 * la base de datos.
 *
 * @author betelgeuse
 * @version 1.1 AndresPineda
 * (Crear-Editar-Borrar-BuscarDetallesEmpresas-BuscarDetalleEmpresaPorSecuencia)
 */
@Stateless
public class PersistenciaDetallesEmpresas implements PersistenciaDetallesEmpresasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     * @param em
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, DetallesEmpresas detallesEmpresas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(detallesEmpresas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesEmpresas.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, DetallesEmpresas detallesEmpresas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(detallesEmpresas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesEmpresas.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, DetallesEmpresas detallesEmpresas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(detallesEmpresas));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaDetallesEmpresas.borrar: " + e);
            }
        }
    }

    @Override
    public DetallesEmpresas buscarDetalleEmpresa(EntityManager em, Short codigoEmpresa) {
        DetallesEmpresas detallesEmpresas;
        try {
            em.clear();
            Query query = em.createQuery("SELECT de FROM DetallesEmpresas de WHERE de.empresa.codigo = :codigoEmpresa");
            query.setParameter("codigoEmpresa", codigoEmpresa);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            detallesEmpresas = (DetallesEmpresas) query.getSingleResult();
            return detallesEmpresas;
        } catch (Exception e) {
            System.out.println("error PersistenciaDetallesEmpresas.buscarDetalleEmpresa. ");
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public DetallesEmpresas buscarDetalleEmpresaPorSecuencia(EntityManager em, BigInteger secEmpresa) {
        try {
            em.clear();
            DetallesEmpresas detallesEmpresas;
            Query query = em.createQuery("SELECT de FROM DetallesEmpresas de WHERE de.empresa.secuencia =:secEmpresa");
            query.setParameter("secEmpresa", secEmpresa);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            detallesEmpresas = (DetallesEmpresas) query.getSingleResult();
            return detallesEmpresas;
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesEmpresas.buscarDetalleEmpresaPorSecuencia. " + e.toString());
            return null;
        }
    }

    @Override
    public List<DetallesEmpresas> buscarDetallesEmpresas(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetallesEmpresas.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("Error PersistenciaDetallesEmpresas.buscarDetallesEmpresas : " + e.toString());
            return null;
        }
    }

}
