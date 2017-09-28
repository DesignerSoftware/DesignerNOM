package Persistencia;

import Entidades.ParametrosTiempos;
import InterfacePersistencia.PersistenciaParametrosTiemposInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaParametrosTiempos implements PersistenciaParametrosTiemposInterface {

   private static Logger log = Logger.getLogger(PersistenciaParametrosTiempos.class);

    @Override
    public void crear(EntityManager em, ParametrosTiempos parametrosTiempos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(parametrosTiempos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosTiempos.crear :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, ParametrosTiempos parametrosTiempos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(parametrosTiempos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosTiempos.editar :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, ParametrosTiempos parametrosTiempos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(parametrosTiempos));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosTiempos.borrar :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<ParametrosTiempos> buscarParametrosTiempos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM ParametrosTiempos t");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ParametrosTiempos> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosTiempos.buscarParametrosTiempos :  ", e);
            return null;
        }
    }

    @Override
    public ParametrosTiempos buscarParametrosTiemposPorUsuarioBD(EntityManager em, String aliasBD) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT t FROM ParametrosTiempos t WHERE t.usuariobd=:aliasBD");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("aliasBD", aliasBD);
            ParametrosTiempos parametro = (ParametrosTiempos) query.getSingleResult();
            return parametro;
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosTiempos.buscarParametrosTiemposPorUsuarioBD :  ", e);
            return null;
        }
    }

    @Override
    public void ejecutarPKG_INSERTARCUADRILLA(EntityManager em, BigInteger cuadrilla, Date fechaDesde, Date fechaHasta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            String sql = "call PROGRAMACIONESTIEMPOS_PKG.INSERTARCUADRILLA(?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, cuadrilla);
            query.setParameter(2, fechaDesde);
            query.setParameter(3, fechaHasta);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosTiempos.EjecutarPKG_INSERTARCUADRILLA :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void ejecutarPKG_SIMULARTURNOSEMPLEADOS(EntityManager em, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            String sql = "call PROGRAMACIONESTIEMPOS_PKG.SIMULARTURNOSEMPLEADOS(?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaDesde);
            query.setParameter(2, fechaHasta);
            query.setParameter(3, emplDesde);
            query.setParameter(4, emplHasta);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosTiempos.EjecutarPKG_SIMULARTURNOSEMPLEADOS :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void ejecutarPKG_LIQUIDAR(EntityManager em, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta, String formulaLiquidacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            String sql = "";
            if (formulaLiquidacion.equals("EH")) {
                sql = "call TURNOSEMPLEADOS_PKG.LIQUIDAREH(?,?,?,?)";
            } else if (formulaLiquidacion.equals("EC")) {
                sql = "call TURNOSEMPLEADOS_PKG.LIQUIDAREC(?,?,?,?)";
            } else if (formulaLiquidacion.equals("CO")) {
                sql = "call TURNOSEMPLEADOS_PKG.LIQUIDARCO(?,?,?,?)";
            } else if (formulaLiquidacion.equals("PI")) {
                sql = "call TURNOSEMPLEADOS_PKG.LIQUIDARPI(?,?,?,?)";
            }
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaDesde);
            query.setParameter(2, fechaHasta);
            query.setParameter(3, emplDesde);
            query.setParameter(4, emplHasta);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosTiempos.EjecutarPKG_LIQUIDAR :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void ejecutarPKG_EliminarProgramacion(EntityManager em, Date fechaDesde, Date fechaHasta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            String sql = "call programacionestiempos_PKG.EliminarProgramacion(?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaDesde);
            query.setParameter(2, fechaHasta);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosTiempos.EjecutarPKG_EliminarProgramacion :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void ejecutarPKG_ELIMINARSIMULACION(EntityManager em, BigInteger cuadrilla, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            String sql = "call  PROGRAMACIONESTIEMPOS_PKG.ELIMINARSIMULACION(?,?,?,?,?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, cuadrilla);
            query.setParameter(2, fechaDesde);
            query.setParameter(3, fechaHasta);
            query.setParameter(4, emplDesde);
            query.setParameter(5, emplHasta);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaParametrosTiempos.EjecutarPKG_ELIMINARSIMULACION :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
