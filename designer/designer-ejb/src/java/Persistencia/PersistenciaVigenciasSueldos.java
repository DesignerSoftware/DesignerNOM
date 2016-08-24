/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasSueldos;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import InterfacePersistencia.PersistenciaVigenciasSueldosInterface;
import java.math.BigInteger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasSueldos' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasSueldos implements PersistenciaVigenciasSueldosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    public void crear(EntityManager em, VigenciasSueldos vigenciasSueldos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            if(vigenciasSueldos.getTiposueldo()==null){
                System.out.println("I'm Null");
            }
            tx.begin();
            em.merge(vigenciasSueldos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("PersistenciaVigenciasSueldos La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    public void editar(EntityManager em, VigenciasSueldos vigenciasSueldos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasSueldos);
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

    public void borrar(EntityManager em, VigenciasSueldos vigenciasSueldos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasSueldos));
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

    public List<VigenciasSueldos> buscarVigenciasSeldos(EntityManager em) {
        em.clear();
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(VigenciasSueldos.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<VigenciasSueldos> buscarVigenciasSueldosEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vs FROM VigenciasSueldos vs WHERE vs.empleado.secuencia = :secuenciaEmpl ORDER BY vs.fechavigencia DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasSueldos> vigenciasSueldos = query.getResultList();
            return vigenciasSueldos;
        } catch (Exception e) {
            System.out.println("Error en buscarVigenciasSueldosEmpleado " + e);
            return null;
        }
    }

    @Override
    public VigenciasSueldos buscarVigenciasSueldosSecuencia(EntityManager em, BigInteger secVS) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VigenciasSueldos v WHERE v.secuencia = :secuencia").setParameter("secuencia", secVS);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasSueldos vigenciasSueldos = (VigenciasSueldos) query.getSingleResult();
            return vigenciasSueldos;
        } catch (Exception e) {
            System.out.println("Error en buscarVigenciasSueldosSecuencia " + e);
            return null;
        }
    }

    @Override
    public List<VigenciasSueldos> buscarVigenciasSueldosEmpleadoRecientes(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            String consulta = "SELECT * FROM VIGENCIASSUELDOS v "
                    + "WHERE v.fechavigencia = ("
                    + "    SELECT MAX(i.fechavigencia) "
                    + "    FROM VIGENCIASSUELDOS i, tipossueldos ts"
                    + "    WHERE v.empleado = i.empleado"
                    + "    AND ts.secuencia = v.tiposueldo"
                    + "    AND i.fechavigencia <= (SELECT fechahastacausado FROM VWACTUALESFECHAS)"
                    + "    AND i.fechavigencia >=  (SELECT decode (NVL(TS.BASICO,'N'),'S', "
                    + "                                GREATEST(MAX(vrl.fechavigencia),i.fechavigencia), "
                    + "                                LEAST(MAX(vrl.fechavigencia),i.fechavigencia)) "
                    + "                            FROM VIGENCIASREFORMASLABORALES vrl"
                    + "                            WHERE vrl.empleado = v.empleado"
                    + "                            AND vrl.fechavigencia <= (SELECT fechahastacausado FROM VWACTUALESFECHAS)"
                    + "                            ) "
                    + "                        )"
                    + "AND v.empleado = (SELECT e.secuencia FROM empleados e WHERE e.secuencia = ?)"
                    + "ORDER BY fechavigencia DESC";
            List<VigenciasSueldos> vigenciasSueldos = (List<VigenciasSueldos>) em.createNativeQuery(consulta, VigenciasSueldos.class).setParameter(1, secEmpleado).getResultList();
            return vigenciasSueldos;
        } catch (Exception e) {
            System.out.println("Error en buscarVigenciasSueldosEmpleadoRecientes " + e);
            return null;
        }
    }
}
