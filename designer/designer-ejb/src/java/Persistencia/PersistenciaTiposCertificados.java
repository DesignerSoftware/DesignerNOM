/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaTiposCertificadosInterface;
import Entidades.TiposCertificados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposCertificados' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposCertificados implements PersistenciaTiposCertificadosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, TiposCertificados motivosMvrs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosMvrs);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCertificados.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposCertificados motivosMvrs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosMvrs);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCertificados.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposCertificados motivosMvrs) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivosMvrs));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCertificados.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public TiposCertificados buscarTipoCertificado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(TiposCertificados.class, secuencia);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<TiposCertificados> buscarTiposCertificados(EntityManager em) {
        em.clear();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(TiposCertificados.class));
        return em.createQuery(cq).getResultList();
    }
}
