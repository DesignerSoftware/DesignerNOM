/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaCandadosInterface;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br> Clase encargada de realizar operaciones sobre la tabla
 * 'Candados' de la base de datos
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaCandados implements PersistenciaCandadosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager em;
     */
    @Override
    public boolean permisoLiquidar(EntityManager em, String usuarioBD) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(c) FROM Candados c WHERE c.usuario.alias = :usuarioBD");
            query.setParameter("usuarioBD", usuarioBD);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            System.out.println("Exepcion: permisoLiquidar " + e);
            return false;
        }
    }

    @Override
    public void liquidar(EntityManager em) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        int i = -100;
        try {
            tx.begin();
            String sqlQuery = "call PRCUTL_FORMSLIQUIDAR()";
            Query query = em.createNativeQuery(sqlQuery);
            i = query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaCandados.liquidar. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public String estadoLiquidacion(EntityManager em, String usuarioBD) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT c.estado FROM Candados c WHERE c.usuario.alias = :usuarioBD");
            query.setParameter("usuarioBD", usuarioBD);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            String estadoLiquidacion = (String) query.getSingleResult();
            return estadoLiquidacion;
        } catch (Exception e) {
            System.out.println("Exepcion: estadoLiquidacion " + e);
            return null;
        }
    }

    @Override
    public Integer progresoLiquidacion(EntityManager em, Integer totalEmpleadosALiquidar) {
        try {
            em.clear();
            String sqlQuery = "select liquidar_pkg.conteoliquidados(?) from dual";
//            String sqlQuery = "call liquidar_pkg.conteoLiquidados(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, totalEmpleadosALiquidar);
            BigDecimal progreso = (BigDecimal) query.getSingleResult();
            Integer porcentajeProgreso = progreso.intValueExact();
            return porcentajeProgreso;
        } catch (Exception e) {
            System.out.println("Error progresoLiquidacion. " + e);
            return null;
        }
    }

    @Override
    public void cancelarLiquidacion(EntityManager em, String usuarioBD) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery("UPDATE Candados c SET c.estado='CANCELAR' WHERE c.usuario.alias = :usuarioBD");
            query.setParameter("usuarioBD", usuarioBD);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Exepcion: PersistenciaCandados.cancelarLiquidacion " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void cerrarLiquidacionAutomatico(EntityManager em) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call UTL_FORMS.CERRARLIQUIDACION()";
            Query query = em.createNativeQuery(sqlQuery);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaCandados.cerrarLiquidacionAutomatico. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void cerrarLiquidacionNoAutomatico(EntityManager em) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call UTL_FORMS.CERRARLIQPAGOPORFUERA()";
            Query query = em.createNativeQuery(sqlQuery);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaCandados.cerrarLiquidacionNoAutomatico. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrarLiquidacionAutomatico(EntityManager em) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call UTL_FORMS.ELIMINARLIQUIDACION()";
            Query query = em.createNativeQuery(sqlQuery);
            int resultado = query.executeUpdate();
            System.out.println("resultado del borrado: "+resultado);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error cerrarLiquidacion. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrarLiquidacionNoAutomatico(EntityManager em) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call UTL_FORMS.ELIMINARLIQPAGOPORFUERA()";
            Query query = em.createNativeQuery(sqlQuery);
            int resultado = query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error cerrarLiquidacion. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
