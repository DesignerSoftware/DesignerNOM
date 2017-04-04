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
//         System.out.println("PersistenciaOperandos.buscarOperandos() 1");
//         Query q = em.createNativeQuery("SELECT * FROM OPERANDOS", Operandos.class);
//         System.out.println("PersistenciaOperandos.buscarOperandos() 2");
//         List<Operandos> lista = q.getResultList();
//         if (lista != null) {
//            System.out.println("PersistenciaOperandos.buscarOperandos() lista.size() : " + lista.size());
//         }
         CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         System.out.println("PersistenciaOperandos.buscarOperandos() 1");
         cq.select(cq.from(Operandos.class));
         System.out.println("PersistenciaOperandos.buscarOperandos() 2");
         List<Operandos> lista = em.createQuery(cq).getResultList();
         return lista;
      } catch (Exception e) {
         System.out.println("Error buscarOperandos PersistenciaOperandos : " + e.toString());
         return null;
      }
   }

   public List<Operandos> operandoPorConceptoSoporte(EntityManager em, BigInteger secConcepto) {
      try {
         System.out.println("Esta en la persistencia, operandoPorConceptoSoporte() secConcepto : " + secConcepto);
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
         System.out.println("Persistencia.PersistenciaOperandos.crear()" + e.getMessage());
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
         System.out.println("Persistencia.PersistenciaOperandos.editar()" + e.getMessage());
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
         System.out.println("Persistencia.PersistenciaOperandos.borrar()" + e.getMessage());
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
//         query.setParameter(2, secuenciaOperando);
         valor = (String) query.getSingleResult();
         return valor;
      } catch (Exception e) {
//          System.out.println("Persistencia.PersistenciaOperandos.valores()" + e.getMessage());
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
