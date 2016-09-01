package Persistencia;

import Entidades.AportesEntidades;
import InterfacePersistencia.PersistenciaAportesEntidadesInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaAportesEntidades implements PersistenciaAportesEntidadesInterface {

    @Override
    public void crear(EntityManager em, AportesEntidades aportesEntidades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(aportesEntidades);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidades.crear : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, AportesEntidades aportesEntidades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(aportesEntidades);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidades.editar : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, AportesEntidades aportesEntidades) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(aportesEntidades));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidades.borrar : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<AportesEntidades> consultarAportesEntidades(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT a FROM AportesEntidades a ORDER BY a.ano ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<AportesEntidades> aportesEntidades = query.getResultList();
            return aportesEntidades;
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidades.consultarParametrosAutoliq : " + e.toString());
            return null;
        }
    }

    @Override
    public List<AportesEntidades> consultarAportesEntidadesPorEmpresaMesYAnio(EntityManager em, BigInteger secEmpresa, short mes, short ano) {
        try {
            em.clear();
            String sql = "SELECT * FROM AportesEntidades a WHERE a.empresa =? AND a.ano =? AND a.mes =? AND EXISTS(SELECT 'x' FROM Empleados e WHERE e.secuencia = a.empleado) ORDER BY a.empleado DESC";
            Query query = em.createNativeQuery(sql, AportesEntidades.class);
            query.setParameter(1, secEmpresa);
            query.setParameter(2, ano);
            query.setParameter(3, mes);
            List<AportesEntidades> aportesEntidades = query.getResultList();
            return aportesEntidades;
        } catch (Exception e) {
            System.out.println("Error PersistenciaAportesEntidades.consultarAportesEntidadesPorEmpresaMesYAño : " + e.toString());
            return null;
        }
    }

    @Override
    public void borrarAportesEntidadesProcesoAutomatico(EntityManager em, BigInteger secEmpresa, short mes, short ano) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call APORTESENTIDADES_PKG.ELIMINAR(?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, ano);
            query.setParameter(2, mes);
            query.setParameter(3, secEmpresa);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error en PersistenciaAportesEntidades.borrarAportesEntidadesProcesoAutomatico: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public String ejecutarPKGInsertar(EntityManager em, Date fechaIni, Date fechaFin, BigInteger tipoTrabajador, BigInteger secEmpresa) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call APORTESENTIDADES_PKG.INSERTAR(?, ?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, fechaIni);
            query.setParameter(2, fechaFin);
            query.setParameter(3, tipoTrabajador);
            query.setParameter(4, secEmpresa);
            query.executeUpdate();
            tx.commit();
            return "PROCESO_EXITOSO";
        } catch (Exception e) {
            System.out.println("Error en PersistenciaAportesEntidades.ejecutarPKGInsertar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
            return "ERROR_PERSISTENCIA";
        }
    }

    @Override
    public String ejecutarPKGActualizarNovedades(EntityManager em, BigInteger secEmpresa, short mes, short ano) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call APORTESENTIDADES_PKG.ACTUALIZARNOVEDADES(?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, ano);
            query.setParameter(2, mes);
            query.setParameter(3, secEmpresa);
            query.executeUpdate();
            tx.commit();
            return "PROCESO_EXITOSO";
        } catch (Exception e) {
            System.out.println("Error en PersistenciaAportesEntidades.ejecutarPKGActualizarNovedades: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
            return "ERROR_PERSISTENCIA";
        }
    }

    @Override
    public void ejecutarAcumularDiferencia(EntityManager em, BigInteger secEmpresa, short mes, short ano) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call APORTESENTIDADES_PKG.ACUMULARDIFERENCIA(?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, ano);
            query.setParameter(2, mes);
            query.setParameter(3, secEmpresa);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error en PersistenciaAportesEntidades.ejecutarAcumularDiferencia: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
