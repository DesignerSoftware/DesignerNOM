/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Operandos;
import InterfacePersistencia.PersistenciaOperandosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Operandos' de la base
 * de datos.
 *
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaOperandos implements PersistenciaOperandosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public List<Operandos> buscarOperandos(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Operandos.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarOperandos PersistenciaOperandos : " + e.toString());
            return null;
        }
    }

    public List<Operandos> operandoPorConceptoSoporte(EntityManager em, BigInteger secConceptoSoporte) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT o FROM Operandos o, ConceptosSoportes cs WHERE cs.operando.secuencia = o.secuencia AND cs.concepto.secuencia = :secConceptoSoporte ");
            query.setParameter("secConceptoSoporte", secConceptoSoporte);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Operandos> operandos = query.getResultList();
            return operandos;
        } catch (Exception e) {
            System.out.println("Error Persistencia PersistenciaOperandos operandoPorConceptoSoporte : " + e.toString());
            return null;
        }
    }
    
    @Override
    public void crear(EntityManager em, Operandos operandos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(operandos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("EL Operandos no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public void editar(EntityManager em, Operandos operandos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(operandos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("El Operandos no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Operandos operandos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(operandos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("El Operandos no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public String valores(EntityManager em, BigInteger secuenciaOperando) {
        try {
            em.clear();
            String valor;
            Query query = em.createNativeQuery("SELECT DECODE(tc.tipo,'C',tc.valorstring,'N',to_char(tc.valorreal),to_char(tc.valordate,'DD/MM/YYYY')) FROM TIPOSCONSTANTES tc WHERE tc.operando=? AND tc.fechainicial=(select max(tci.fechainicial) from tiposconstantes tci WHERE tci.operando= ?)");
            query.setParameter(1, secuenciaOperando);
            query.setParameter(2, secuenciaOperando);
            valor = (String) query.getSingleResult();
            return valor;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Operandos operandosPorSecuencia(EntityManager em, BigInteger secOperando) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT o FROM Operandos o WHERE o.secuencia=:secOperando");
            query.setParameter("secOperando", secOperando);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Operandos operandos = (Operandos) query.getSingleResult();
            return operandos;
        } catch (Exception e) {
            System.out.println("Error Persistencia operandosPorSecuencia : " + e.toString());
            return null;
        }
    }
}
