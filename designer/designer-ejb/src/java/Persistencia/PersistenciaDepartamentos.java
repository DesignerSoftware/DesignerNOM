/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Departamentos;
import InterfacePersistencia.PersistenciaDepartamentosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Departamentos' de la
 * base de datos
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaDepartamentos implements PersistenciaDepartamentosInterface {

   private static Logger log = Logger.getLogger(PersistenciaDepartamentos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    public void crear(EntityManager em, Departamentos departamentos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(departamentos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaDepartamentos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, Departamentos departamentos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(departamentos);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaDepartamentos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, Departamentos departamentos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(departamentos));
            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.error("Error PersistenciaDepartamentos.borrar: " + e);
        }
    }

    public Departamentos consultarDepartamento(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(Departamentos.class, secuencia);
        } catch (Exception e) {
            log.error("Error buscarDeporte PersistenciaDepartamentos : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Departamentos> consultarDepartamentos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT d FROM Departamentos d ORDER BY d.nombre");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Departamentos> departamentos = query.getResultList();
            return departamentos;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaDepartamentos.consultarDepartamentos()" + e.getMessage());
            return null;
        }
    }

    public BigInteger contarSoAccidentesMedicosDepartamento(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "select COUNT(*)from soaccidentesmedicos WHERE departamento = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PersistenciaDepartamentos contarSoAccidentesMedicosDepartamento  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    public BigInteger contarCiudadesDepartamento(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM ciudades WHERE departamento = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PersistenciaDepartamentos contarCiudadesDepartamento  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    public BigInteger contarCapModulosDepartamento(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM capmodulos WHERE departamento = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PersistenciaDepartamentos contarCapModulosDepartamento  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }

    }

    public BigInteger contarBienProgramacionesDepartamento(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM bienprogramaciones WHERE departamento = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PersistenciaDepartamentos contarBienProgramacionesDepartamento  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }

    @Override
    public List<Departamentos> consultarDepartamentosPorPais(EntityManager em, BigInteger secPais) {
        try {
            em.clear();
            String sql = "SELECT * FROM DEPARTAMENTOS WHERE PAIS = ? ORDER BY NOMBRE";
            Query query = em.createNativeQuery(sql, Departamentos.class);
            query.setParameter(1, secPais);
            List<Departamentos> departamentos = query.getResultList();
            return departamentos;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaDepartamentos.consultarDepartamentosPorPais()" + e.getMessage());
            return null;
        }
    }
}
