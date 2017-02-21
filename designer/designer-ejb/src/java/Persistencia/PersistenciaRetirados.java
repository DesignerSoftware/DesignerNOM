/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Retirados;
import InterfacePersistencia.PersistenciaRetiradosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Retirados' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaRetirados implements PersistenciaRetiradosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, Retirados retirados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(retirados);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRetirados.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Retirados retirados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(retirados);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRetirados.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Retirados retirados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(retirados));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaRetirados.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Retirados> buscarRetirados(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Retirados.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaRetirados.buscarRetirados()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Retirados> buscarRetirosEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Retirados r WHERE r.vigenciatipotrabajador.empleado.secuencia = :secuenciaEmpl ORDER BY r.fecharetiro DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Retirados> retiros = query.getResultList();
            return retiros;
        } catch (Exception e) {
            System.out.println("Error en Persistencia Retirados " + e.getMessage());
            return null;
        }
    }

    @Override
    public Retirados buscarRetiroSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Retirados r WHERE r.secuencia = :secuencia").setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Retirados retiro = (Retirados) query.getSingleResult();
            return retiro;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaRetirados.buscarRetiroSecuencia()" + e.getMessage());
            return null;
        }
    }

    @Override
    public Retirados buscarRetiroVigenciaSecuencia(EntityManager em, BigInteger secVigencia) {
        try {
            em.clear();
            String sql = "SELECT * FROM Retirados WHERE vigenciatipotrabajador = ?";
            Query query = em.createNativeQuery(sql, Retirados.class);
            query.setParameter(1, secVigencia);
            //query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Retirados retiroVigencia = (Retirados) query.getSingleResult();
            return retiroVigencia;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaRetirados.buscarRetiroVigenciaSecuencia()" + e.getMessage());
            return new Retirados();
        }
    }

    @Override
    public void adicionaRetiroCambiosMasivos(EntityManager em, String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.AdicionaRetiro");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, Date.class, ParameterMode.IN);

            query.setParameter(1, indemniza);
            query.setParameter(2, secMotivoDefinitiva);
            query.setParameter(3, secMotivoRetiro);
            query.setParameter(4, fechaCambio);
            query.execute();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaRetirados.adicionaRetiroCambiosMasivos()" + e.getMessage());
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            tx.commit();
        }
    }

    @Override
    public void undoAdicionaRetiroCambiosMasivos(EntityManager em, String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.UndoAdicionaRetiro");
            query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, Date.class, ParameterMode.IN);

            query.setParameter(1, indemniza);
            query.setParameter(2, secMotivoDefinitiva);
            query.setParameter(3, secMotivoRetiro);
            query.setParameter(4, fechaCambio);
            query.execute();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaRetirados.undoAdicionaRetiroCambiosMasivos()" + e.getMessage());
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            tx.commit();
        }
    }

    @Override
    public void adicionaReingresoCambiosMasivos(EntityManager em, Date fechaInicio, Date fechaFin) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.AdicionaReingreso");
            query.registerStoredProcedureParameter(1, Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, Date.class, ParameterMode.IN);

            query.setParameter(1, fechaInicio);
            query.setParameter(2, fechaFin);
            query.execute();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaRetirados.adicionaReingresoCambiosMasivos()" + e.getMessage());
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            tx.commit();
        }
    }
}
