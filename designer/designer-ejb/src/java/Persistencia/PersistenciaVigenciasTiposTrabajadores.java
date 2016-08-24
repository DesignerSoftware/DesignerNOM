/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasTiposTrabajadores;
import InterfacePersistencia.PersistenciaVigenciasTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'VigenciasTiposTrabajadores' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasTiposTrabajadores implements PersistenciaVigenciasTiposTrabajadoresInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, VigenciasTiposTrabajadores vigenciasTiposTrabajadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciasTiposTrabajadores);
            tx.commit();
        } catch (Exception e) {
            System.out.println("PersistenciaVigenciasTiposTrabajadores La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasTiposTrabajadores vigenciasTiposTrabajadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasTiposTrabajadores);
            tx.commit();
        } catch (Exception e) {
            System.out.println("La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
           
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasTiposTrabajadores vigenciasTiposTrabajadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasTiposTrabajadores));
            tx.commit();
        } catch (Exception e) {
            System.out.println("La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public List<VigenciasTiposTrabajadores> buscarVigenciasTiposTrabajadores(EntityManager em) {
        em.clear();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(VigenciasTiposTrabajadores.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<VigenciasTiposTrabajadores> buscarVigenciasTiposTrabajadoresEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            String sql = "SELECT * FROM VigenciasTiposTrabajadores  WHERE empleado = ? ORDER BY fechavigencia DESC";
            /*
             Query query = em.createQuery("SELECT vtt FROM VigenciasTiposTrabajadores vtt WHERE vtt.empleado.secuencia = :secuenciaEmpl ORDER BY vtt.fechavigencia DESC");
             query.setParameter("secuenciaEmpl", secEmpleado);
             query.setHint("javax.persistence.cache.storeMode", "REFRESH");*/
            Query query = em.createNativeQuery(sql, VigenciasTiposTrabajadores.class);
            query.setParameter(1, secEmpleado);
            List<VigenciasTiposTrabajadores> vigenciasTiposTrabajadores = query.getResultList();
            return vigenciasTiposTrabajadores;
        } catch (Exception e) {
            System.out.println("Error en Persistencia Vigencias TiposTrabajadores " + e);
            return null;
        }
    }

    @Override
    public VigenciasTiposTrabajadores buscarVigenciasTiposTrabajadoresSecuencia(EntityManager em, BigInteger secVTT) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VigenciasTiposTrabajadores v WHERE v.secuencia = : secuencia").setParameter("secuencia", secVTT);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasTiposTrabajadores vigenciasTiposTrabajadores = (VigenciasTiposTrabajadores) query.getSingleResult();
            return vigenciasTiposTrabajadores;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<VigenciasTiposTrabajadores> buscarEmpleados(EntityManager em) {
        em.clear();
        Query query = em.createQuery("SELECT vtt FROM VigenciasTiposTrabajadores vtt "
                + "WHERE vtt.fechavigencia = (SELECT MAX(vttt.fechavigencia) "
                + "FROM VigenciasTiposTrabajadores vttt)");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return query.getResultList();
    }

    @Override
    public VigenciasTiposTrabajadores buscarVigenciaTipoTrabajadorRestriccionUN(EntityManager em, BigInteger empleado, Date fechaVigencia, BigInteger tipoTrabajador) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vtt FROM VigenciasTiposTrabajadores vtt WHERE vtt.empleado.secuencia = :empleado AND vtt.fechavigencia=:fechaVigencia AND vtt.tipotrabajador.secuencia=:tipoTrabajador");
            query.setParameter("empleado", empleado);
            query.setParameter("fechaVigencia", fechaVigencia);
            query.setParameter("tipoTrabajador", tipoTrabajador);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasTiposTrabajadores obj = (VigenciasTiposTrabajadores) query.getSingleResult();
            return obj;
        } catch (Exception e) {
            System.out.println("Error en Persistencia Vigencias TiposTrabajadores buscarVigenciaTipoTrabajadorRestriccionUNB " + e);
            return null;
        }
    }
}
