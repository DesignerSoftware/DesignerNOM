/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Rastros;
import InterfacePersistencia.PersistenciaRastrosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Rastros' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaRastros implements PersistenciaRastrosInterface {

   private static Logger log = Logger.getLogger(PersistenciaRastros.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public List<Rastros> rastrosTabla(EntityManager em, BigInteger secRegistro, String nombreTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Rastros r WHERE r.tabla = :nombreTabla AND r.secuenciatabla = :secRegistro AND r.manipulacion IN ('I','U') ORDER BY r.fecharastro DESC");
            query.setParameter("nombreTabla", nombreTabla.toUpperCase());
            query.setParameter("secRegistro", secRegistro.toString());
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Rastros> listaRastroTabla = query.getResultList();
            return listaRastroTabla;

        } catch (Exception e) {
            log.error("Persistencia.PersistenciaRastros.rastrosTabla()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Rastros> rastrosTablaHistoricos(EntityManager em, String nombreTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Rastros r WHERE r.tabla = :nombreTabla AND r.manipulacion IN ('I','U') ORDER BY r.fecharastro DESC");
            query.setParameter("nombreTabla", nombreTabla.toUpperCase());
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Rastros> listaRastroTabla = query.getResultList();
            return listaRastroTabla;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaRastros.rastrosTablaHistoricos()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Rastros> rastrosTablaHistoricosEliminados(EntityManager em, String nombreTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Rastros r WHERE r.tabla = :nombreTabla AND r.manipulacion = 'D' ORDER BY r.fecharastro DESC");
            query.setParameter("nombreTabla", nombreTabla.toUpperCase());
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Rastros> listaRastroTabla = query.getResultList();
            return listaRastroTabla;

        } catch (Exception e) {
            log.error("Exepcion en rastrosTablaHistoricos " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Rastros> rastrosTablaHistoricosEliminadosEmpleados(EntityManager em, String nombreTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Rastros r WHERE r.tabla = :nombreTabla AND r.manipulacion = 'D' AND EXISTS (SELECT r FROM RastrosValores rv where rv.nombrecolumna = 'EMPLEADO'AND EXISTS (SELECT t FROM "
                    + nombreTabla + " t WHERE rv.valorprevio = t.empleado.secuencia)) ORDER BY r.fecharastro DESC");
            query.setParameter("nombreTabla", nombreTabla.toUpperCase());
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Rastros> listaRastroTabla = query.getResultList();
            return listaRastroTabla;

        } catch (Exception e) {
            log.error("Exepcion en rastrosTablaHistoricos " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Rastros> rastrosTablaFecha(EntityManager em, Date fechaRegistro, String nombreTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT r FROM Rastros r WHERE r.tabla = :nombreTabla AND r.fecharastro = :fechaRegistro AND r.manipulacion IN ('I','U') ORDER BY r.fecharastro DESC");
            query.setParameter("nombreTabla", nombreTabla.toUpperCase());
            query.setParameter("fechaRegistro", fechaRegistro);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Rastros> listaRastroTabla = query.getResultList();
            return listaRastroTabla;

        } catch (Exception e) {
            log.error("Exepcion en rastrosTablaFecha " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean verificarRastroRegistroTabla(EntityManager em, BigInteger secRegistro, String nombreTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(r) FROM Rastros r WHERE r.tabla = :nombreTabla AND r.manipulacion IN ('I','U') AND r.secuenciatabla = :secRegistro");
            query.setParameter("nombreTabla", nombreTabla.toUpperCase());
            query.setParameter("secRegistro", secRegistro.toString());
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            log.error("Excepción en verificarRastroRegistroTabla " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verificarRastroHistoricoTabla(EntityManager em, String nombreTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(r) FROM Rastros r WHERE r.tabla = :nombreTabla AND r.manipulacion IN ('I','U')");
            query.setParameter("nombreTabla", nombreTabla);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > 0;
        } catch (Exception e) {
            log.error("Exepcion en verificarRastroHistoricoTabla " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verificarEmpleadoTabla(EntityManager em, String nombreTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(r) FROM Rastros r WHERE r.tabla = :nombreTabla AND r.manipulacion = 'D' AND EXISTS (SELECT r FROM RastrosValores rv where rv.nombrecolumna = 'EMPLEADO'AND EXISTS (SELECT t FROM "
                    + nombreTabla + " t WHERE rv.valorprevio = t.empleado.secuencia)) ORDER BY r.fecharastro DESC");
            query.setParameter("nombreTabla", nombreTabla.toUpperCase());
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            return resultado > -1;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaRastros.verificarEmpleadoTabla()" + e.getMessage());
            return false;
        }
    }
}
