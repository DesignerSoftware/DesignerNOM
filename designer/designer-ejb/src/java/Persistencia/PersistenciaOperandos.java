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
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
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

   private static Logger log = Logger.getLogger(PersistenciaOperandos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public List<Operandos> buscarOperandos(EntityManager em) {
      try {
         em.clear();
//         log.warn("PersistenciaOperandos.buscarOperandos() 1");
//         Query q = em.createNativeQuery("SELECT * FROM OPERANDOS", Operandos.class);
//         log.warn("PersistenciaOperandos.buscarOperandos() 2");
//         List<Operandos> lista = q.getResultList();
//         if (lista != null) {
//            log.warn("PersistenciaOperandos.buscarOperandos() lista.size() : " + lista.size());
//         }
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         log.warn("PersistenciaOperandos.buscarOperandos() 1");
         cq.select(cq.from(Operandos.class));
         log.warn("PersistenciaOperandos.buscarOperandos() 2");
         List<Operandos> lista = em.createQuery(cq).getResultList();
         return lista;
      } catch (Exception e) {
         log.error("Error buscarOperandos PersistenciaOperandos :  ", e);
         return null;
      }
   }

   public List<Operandos> operandoPorConceptoSoporte(EntityManager em, BigInteger secConcepto) {
      try {
         log.warn("Esta en la persistencia, operandoPorConceptoSoporte() secConcepto : " + secConcepto);
         em.clear();
         Query query = em.createNativeQuery("SELECT ALL OPERANDOS.* \n"
                 + "FROM OPERANDOS \n"
                 + "where secuencia in (\n"
                 + "select operando \n"
                 + "  from vwarbolesformulas\n"
                 + "  START WITH historiaFORMULA = (select hf.secuencia from historiasformulas hf, formulasconceptos fc\n"
                 + "  where hf.fechainicial = (select max(hfi.fechainicial)\n"
                 + "  from historiasformulas hfi\n"
                 + "  where fc.concepto = ? \n"
                 + "  and hf.formula = hfi.formula)\n"
                 + "  and  fc.formula = hf.formula)\n"
                 + "  CONNECT BY PRIOR formulahijo = FORMULA)", Operandos.class);
         query.setParameter(1, secConcepto);
         List<Operandos> operandos = query.getResultList();
         return operandos;
      } catch (Exception e) {
         log.error("Error Persistencia PersistenciaOperandos operandoPorConceptoSoporte :  ", e);
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
         log.error("PersistenciaOperandos.crear():  ", e);
         if (tx.isActive()) {
            tx.rollback();
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
         log.error("PersistenciaOperandos.editar():  ", e);
         if (tx.isActive()) {
            tx.rollback();
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
         log.error("PersistenciaOperandos.borrar():  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public String valores(EntityManager em, BigInteger secuenciaOperando) {
      try {
         em.clear();
         String valor;
         Query query = em.createNativeQuery("SELECT DECODE(tc.tipo,'C',tc.valorstring,'N',to_char(tc.valorreal),to_char(tc.valordate,'DD/MM/YYYY')) FROM TIPOSCONSTANTES tc WHERE tc.operando=? AND tc.fechainicial=(select max(tci.fechainicial) from tiposconstantes tci WHERE tci.operando= tc.operando)");
         query.setParameter(1, secuenciaOperando);
         valor = (String) query.getSingleResult();
         return valor;
      } catch (Exception e) {
//          log.error("PersistenciaOperandos.valores():  ", e);
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
         log.error("Error Persistencia operandosPorSecuencia :  ", e);
         return null;
      }
   }

   public String clonarOperando(EntityManager em, short codigoOrigen, String nombreDest, String descripcionDest) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         log.warn("codigoOrigen : " + codigoOrigen);
         log.warn("nombreDest : " + nombreDest);
         log.warn("descripcionDest : " + descripcionDest);
         StoredProcedureQuery query = em.createStoredProcedureQuery("OPERANDOS_PKG.ClonarOperando");
         query.registerStoredProcedureParameter(1, short.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);

         query.setParameter(1, codigoOrigen);
         query.setParameter(2, nombreDest);
         query.setParameter(3, descripcionDest);
         query.execute();
         return "BIEN";
      } catch (Exception e) {
         log.error("ERROR: " + this.getClass().getName() + ".clonarOperando()");
         e.printStackTrace();
         if (tx.isActive()) {
            tx.rollback();
         }
         return "ERROR EJECUTANDO LA TRANSACCION DESDE EL SISTEMA";
      } finally {
         tx.commit();
      }
   }
}
