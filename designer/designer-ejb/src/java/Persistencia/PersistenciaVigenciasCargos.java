/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Empleados;
import Entidades.Papeles;
import Entidades.VigenciasCargos;
import InterfacePersistencia.PersistenciaVigenciasCargosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasCargos' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Local
@Stateless
public class PersistenciaVigenciasCargos implements PersistenciaVigenciasCargosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager emr;*/
    //private UserTransaction utx;
    @Override
    public void crear(EntityManager em, VigenciasCargos vigenciasCargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            System.out.println("vigenciasCargos Persona Nombre: " + vigenciasCargos.getEmpleado().getPersona().getNombre());
            System.out.println("vigenciasCargos Persona Secuencia: " + vigenciasCargos.getEmpleado().getPersona().getSecuencia());
            System.out.println("vigenciasCargos Empleado Secuencia: " + vigenciasCargos.getEmpleado().getSecuencia());
            tx.begin();
            System.out.println("TX Begin");
            em.persist(vigenciasCargos);
            System.out.println("Persist");
            tx.commit();
            System.out.println("commitea");
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasCargos.crear: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasCargos vigenciasCargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasCargos);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error PersistenciaVigenciasCargos.editar: " + e);
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasCargos vigenciasCargos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasCargos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaVigenciasCargos.borrar: " + e);
            }
        }
    }

    @Override
    public VigenciasCargos buscarVigenciaCargo(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(VigenciasCargos.class, secuencia);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VigenciasCargos buscarVigenciaCargoXEmpleado(EntityManager em, BigInteger secuenciaEmpl, BigInteger secEmpresa) {
        try {
            System.out.println("buscarVigenciaCargoXEmpleado() secuenciaEmpl : " + secuenciaEmpl);
            System.out.println("buscarVigenciaCargoXEmpleado() secEmpresa : " + secEmpresa);
            em.clear();
//            Query query = em.createNativeQuery("SELECT VC.* FROM EMPLEADOS E, VIGENCIASCARGOS VC, EMPRESAS EM WHERE VC.EMPLEADO = E.SECUENCIA AND E.EMPRESA = EM.SECUENCIA AND E.SECUENCIA = ?  AND EM.SECUENCIA = ?");
            Query query = em.createQuery("SELECT v FROM Empleados e, VigenciasCargos v, Empresas em WHERE v.empleado.secuencia = e.secuencia AND e.empresa.secuencia = em.secuencia AND e.secuencia =:secuenciaEmpl  AND em.secuencia =:secEmpresa");
            query.setParameter("secuenciaEmpl", secuenciaEmpl);
            query.setParameter("secEmpresa", secEmpresa);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasCargos vigCargo = (VigenciasCargos) query.getSingleResult();
            System.out.println("buscarVigenciaCargoXEmpleado() secCargo : " + vigCargo);

            return vigCargo;
        } catch (Exception e) {
            System.err.println(this.getClass().getName() + "buscarVigenciaCargoXEmpleado catch() ERROR : " + e);
            return null;
        }
    }

    @Override
    public List<VigenciasCargos> buscarVigenciasCargos(EntityManager em) {
        em.clear();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(VigenciasCargos.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<VigenciasCargos> buscarVigenciasCargosEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            //em.flush();
            //em.getTransaction().begin();
            Query query2 = em.createQuery("SELECT v FROM VigenciasCargos v where v.empleado.secuencia = :secuencia order by v.fechavigencia desc");
            query2.setParameter("secuencia", secEmpleado);
            query2.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasCargos> vigenciasCargos = (List<VigenciasCargos>) query2.getResultList();
            //em.getTransaction().commit();
            return vigenciasCargos;
        } catch (Exception e) {
            List<VigenciasCargos> vigenciasCargos = null;
            return vigenciasCargos;
        }
    }
}
