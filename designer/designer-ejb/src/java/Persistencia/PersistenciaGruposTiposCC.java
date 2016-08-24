/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaGruposTiposCCInterface;
import Entidades.GruposTiposCC;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'GruposTiposCC'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaGruposTiposCC implements PersistenciaGruposTiposCCInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    EntityManager em;*/

    @Override
    public void crear(EntityManager em,GruposTiposCC gruposTiposCC) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(gruposTiposCC);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaGruposTiposCC.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em,GruposTiposCC gruposTiposCC) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(gruposTiposCC);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaGruposTiposCC.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em,GruposTiposCC gruposTiposCC) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(gruposTiposCC));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaGruposTiposCC.borrar: " + e);
            }
        }
    }

    @Override
    public GruposTiposCC buscarGruposTiposCCPorSecuencia(EntityManager em,BigInteger secuencia) {
        try {
            em.clear();
            return em.find(GruposTiposCC.class, secuencia);
        } catch (Exception e) {
            System.err.println("ERROR PersistenciaGruposTiposCC buscarGruposTiposCC ERROR " + e);
            return null;
        }
    }

    @Override
    public List<GruposTiposCC> buscarGruposTiposCC(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GruposTiposCC.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("\n ERROR EN PersistenciaGruposTiposCC buscarGruposTiposCC ERROR" + e);
            return null;
        }
    }

}
