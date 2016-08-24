/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaPartesCuerpoInterface;
import Entidades.PartesCuerpo;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'PartesCuerpo' de la
 * base de datos
 *
 * @author John Pineda.
 */
@Stateless
public class PersistenciaPartesCuerpo implements PersistenciaPartesCuerpoInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, PartesCuerpo partesCuerpo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(partesCuerpo);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPartesCuerpo.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PartesCuerpo partesCuerpo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(partesCuerpo);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPartesCuerpo.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PartesCuerpo partesCuerpo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(partesCuerpo));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPartesCuerpo.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public PartesCuerpo buscarParteCuerpo(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(PartesCuerpo.class, secuencia);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<PartesCuerpo> buscarPartesCuerpo(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT l FROM PartesCuerpo  l ORDER BY l.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<PartesCuerpo> listPartesCuerpo = query.getResultList();
            return listPartesCuerpo;
        } catch (Exception e) {
            System.err.println("ERROR BUSCAR ELEMENTOS CAUSAS ACCIDENTES  " + e);
            return null;
        }
    }

    @Override
    public BigInteger contadorSoAccidentesMedicos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM soaccidentesmedicos so WHERE so.parte = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) query.getSingleResult();
            System.err.println("PARTESCUERPO CONTADORSOACCIDENTESMEDICOS  " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("PARTESCUERPO  ERROR EN EL CONTADORSOACCIDENTESMEDICOS " + e);
            return retorno;
        }
    }

    @Override
    public BigInteger contadorDetallesExamenes(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM sodetallesexamenes se WHERE se.partecuerpo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) query.getSingleResult();
            System.err.println("PARTESCUERPO CONTADOR DETALLES EXAMENES  " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("PARTESCUERPO  ERROR EN EL DETALLES EXAMENES " + e);
            return retorno;
        }
    }

    @Override
    public BigInteger contadorSoDetallesRevisiones(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM sodetallesrevisiones sr WHERE sr.organo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) query.getSingleResult();
            System.err.println("PARTESCUERPO CONTADOR SO DETALLES REVISIONES  " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("PARTESCUERPO  ERROR EN EL CONTADORSODETALLESREVISIONES " + e);
            return retorno;
        }
    }
}
