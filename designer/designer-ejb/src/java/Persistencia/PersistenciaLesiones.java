/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaLesionesInterface;
import Entidades.Lesiones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Lesiones' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaLesiones implements PersistenciaLesionesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, Lesiones lesiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(lesiones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaLesiones.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Lesiones lesiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(lesiones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaLesiones.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Lesiones lesiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(lesiones));
            tx.commit();

        } catch (Exception e) {
            System.out.println("Error PersistenciaLesiones.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Lesiones buscarLesion(EntityManager em, BigInteger secuenciaL) {
        try {
            em.clear();
            return em.find(Lesiones.class, secuenciaL);
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaLesiones.buscarLesion()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Lesiones> buscarLesiones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT l FROM Lesiones l ORDER BY l.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Lesiones> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaLesiones.buscarLesiones()" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorDetallesLicensias(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM  detalleslicencias dl WHERE dl.lesion= ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaLesiones.contadorDetallesLicensias()"  + e.getMessage());
            return retorno;
        }
    }

    @Override
    public BigInteger contadorSoAccidentesDomesticos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM soaccidentesmedicos sm WHERE sm.lesion = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaLesiones.contadorSoAccidentesDomesticos()" + e.getMessage());
            return retorno;
        }
    }
}
