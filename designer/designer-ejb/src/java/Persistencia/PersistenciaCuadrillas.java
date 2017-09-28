package Persistencia;

import Entidades.Cuadrillas;
import InterfacePersistencia.PersistenciaCuadrillasInterface;
import java.math.BigInteger;
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
public class PersistenciaCuadrillas implements PersistenciaCuadrillasInterface {

   private static Logger log = Logger.getLogger(PersistenciaCuadrillas.class);

    @Override
    public void crear(EntityManager em, Cuadrillas cuadrillas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cuadrillas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error crear PersistenciaCuadrillas  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Cuadrillas cuadrillas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cuadrillas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error editar PersistenciaCuadrillas  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Cuadrillas cuadrillas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(cuadrillas));
            tx.commit();
        } catch (Exception e) {
            log.error("Error borrar PersistenciaCuadrillas  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Cuadrillas buscarCuadrillaPorSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Cuadrillas c WHERE c.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Cuadrillas cuadrilla = (Cuadrillas) query.getSingleResult();
            return cuadrilla;
        } catch (Exception e) {
            log.error("Error buscarCuadrillaPorSecuencia PersistenciaCuadrillas  ", e);
            return null;
        }
    }

    @Override
    public List<Cuadrillas> buscarCuadrillas(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c FROM Cuadrillas c ORDER BY c.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Cuadrillas> cuadrillas = query.getResultList();
            return cuadrillas;
        } catch (Exception e) {
            log.error("Error buscarCuadrillas PersistenciaCuadrillas  ", e);
            return null;
        }
    }

    @Override
    public void borrarProgramacionCompleta(EntityManager em) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            em.clear();
            String sql = "delete novedadescuadrillas nc\n"
                    + "  where exists (select 'x' from empleados e, detallesturnosrotativos dtr where e.secuencia=dtr.empleado and dtr.secuencia=nc.detalleturnorotativo);\n"
                    + "  delete detallesturnosrotativos dtr\n"
                    + "  where exists (select 'x' from empleados e where e.secuencia=dtr.empleado)";
            Query query = em.createNativeQuery(sql);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaCuadrillas.borrarProgramacionCompleta :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<Cuadrillas> buscarCuadrillasParaEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String sql = "SELECT * \n"
                    + "FROM CUADRILLAS \n"
                    + "WHERE EXISTS (SELECT 1 \n"
                    + "              FROM DETALLESTURNOSROTATIVOS DTR, TURNOSROTATIVOS TR \n"
                    + "              WHERE DTR.TURNOROTATIVO=TR.SECUENCIA \n"
                    + "			  AND TR.CUADRILLA = CUADRILLAS.SECUENCIA\n"
                    + "              AND DTR.EMPLEADO=?)";
            Query query = em.createNativeQuery(sql, Cuadrillas.class);
            query.setParameter(1, secuencia);
            List<Cuadrillas> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersistenciaCuadrillas.buscarCuadrillasParaEmpleado :  ", e);
            return null;
        }
    }
}
