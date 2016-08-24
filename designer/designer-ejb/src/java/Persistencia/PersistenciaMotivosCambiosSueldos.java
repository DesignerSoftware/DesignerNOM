/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MotivosCambiosSueldos;
import InterfacePersistencia.PersistenciaMotivosCambiosSueldosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'MotivosCambiosSueldos' de la base de datos. (Para verificar que esta
 * asociado a una VigenciaSueldo, se realiza la operacion sobre la tabla
 * VigenciasSueldos)
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaMotivosCambiosSueldos implements PersistenciaMotivosCambiosSueldosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public void crear(EntityManager em, MotivosCambiosSueldos motivosCambiosSueldos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosCambiosSueldos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosCambiosSueldos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, MotivosCambiosSueldos motivosCambiosSueldos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivosCambiosSueldos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaMotivosCambiosSueldos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, MotivosCambiosSueldos motivosCambiosSueldos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivosCambiosSueldos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaMotivosCambiosSueldos.borrar: " + e);
            }
        }
    }

    @Override
    public List<MotivosCambiosSueldos> buscarMotivosCambiosSueldos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM MotivosCambiosSueldos m");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<MotivosCambiosSueldos> motivosCambiosSueldos = (List<MotivosCambiosSueldos>) query.getResultList();
            return motivosCambiosSueldos;
        } catch (Exception e) {
            System.out.println("Error buscarMotivosCambiosSueldos PersistenciaMotivoCambioSueldo : " + e.toString());
            return null;
        }
    }

    @Override
    public MotivosCambiosSueldos buscarMotivoCambioSueldoSecuencia(EntityManager em, BigInteger secuencia) {

        try {
            em.clear();
            Query query = em.createQuery("SELECT mcs FROM MotivosCambiosSueldos mcs WHERE mcs.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            MotivosCambiosSueldos motivosCambiosSueldos = (MotivosCambiosSueldos) query.getSingleResult();
            return motivosCambiosSueldos;
        } catch (Exception e) {
            System.out.println("Error buscarMotivoCambioSueldoSecuencia");
            MotivosCambiosSueldos motivosCambiosSueldos = null;
            return motivosCambiosSueldos;
        }
    }

    @Override
    public BigInteger verificarBorradoVigenciasSueldos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            Query query = em.createQuery("SELECT count(vs) FROM VigenciasSueldos vs WHERE vs.motivocambiosueldo.secuencia =:secMotivosCambiosSueldos ");
            query.setParameter("secMotivosCambiosSueldos", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            retorno = new BigInteger(query.getSingleResult().toString());
            System.err.println("PersistenciaMotivosCambiosSueldos retorno ==" + retorno.intValue());
        } catch (Exception e) {
            System.err.println("ERROR EN PersistenciaMotivosCambiosSueldos verificarBorradoVigenciasSueldos ERROR :" + e);
        } finally {
            return retorno;
        }
    }
}
