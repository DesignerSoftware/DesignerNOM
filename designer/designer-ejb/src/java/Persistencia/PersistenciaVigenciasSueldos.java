/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasSueldos;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import InterfacePersistencia.PersistenciaVigenciasSueldosInterface;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

@Stateless
public class PersistenciaVigenciasSueldos implements PersistenciaVigenciasSueldosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasSueldos.class);

    public boolean crear(EntityManager em, VigenciasSueldos vigenciasSueldos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasSueldos);
            tx.commit();
            return true;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasSueldos.crear()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
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
            log.error("Persistencia.PersistenciaVigenciasSueldos.editar()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
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
            log.error("Persistencia.PersistenciaVigenciasSueldos.borrar()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public List<VigenciasSueldos> buscarVigenciasSeldos(EntityManager em) {
        try {
            em.clear();
            javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VigenciasSueldos.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasSueldos.buscarVigenciasSeldos()" + e.getMessage());
            return null;
        }
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
            log.error("Persistencia.PersistenciaVigenciasSueldos.buscarVigenciasSueldosEmpleado()" + e.getMessage());
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
            log.error("Persistencia.PersistenciaVigenciasSueldos.buscarVigenciasSueldosSecuencia()" + e.getMessage());
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
            log.error("Persistencia.PersistenciaVigenciasSueldos.buscarVigenciasSueldosEmpleadoRecientes()" + e.getMessage());
            return null;
        }
    }

    @Override
    public void adicionaSueldoCambiosMasivos(EntityManager em, BigInteger secMotivoCS, BigInteger secTipoSueldo, BigInteger secUnidad, BigInteger valor, Date fechaCambio) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.AdicionaSueldo");
            query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(5, Date.class, ParameterMode.IN);

            query.setParameter(1, secMotivoCS);
            query.setParameter(2, secTipoSueldo);
            query.setParameter(3, secUnidad);
            query.setParameter(4, valor);
            query.setParameter(5, fechaCambio);
            query.execute();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasSueldos.adicionaSueldoCambiosMasivos()" + e.getMessage());
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            tx.commit();
        }
    }

    @Override
    public void undoAdicionaSueldoCambiosMasivos(EntityManager em, BigInteger secMotivoCS, BigInteger secTipoSueldo, BigInteger secUnidad, BigInteger valor, Date fechaCambio) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            StoredProcedureQuery query = em.createStoredProcedureQuery("CAMBIOSMASIVOS_PKG.UndoAdicionaSueldo");
            query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(5, Date.class, ParameterMode.IN);

            query.setParameter(1, secMotivoCS);
            query.setParameter(2, secTipoSueldo);
            query.setParameter(3, secUnidad);
            query.setParameter(4, valor);
            query.setParameter(5, fechaCambio);
            query.execute();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasSueldos.undoAdicionaSueldoCambiosMasivos()" + e.getMessage());
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            tx.commit();
        }
    }
}
