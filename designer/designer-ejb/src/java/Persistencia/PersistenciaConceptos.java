/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Conceptos;
import InterfacePersistencia.PersistenciaConceptosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 * Clase Stateless Clase encargada de realizar operaciones sobre la tabla
 * 'Conceptos' de la base de datos
 *
 * @author Betelgeuse
 */
@Stateless
public class PersistenciaConceptos implements PersistenciaConceptosInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    *
    * @param em
    */
   /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager em;
    */
   @Override
   public void crear(EntityManager em, Conceptos concepto) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(concepto);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaConceptos.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Conceptos concepto) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(concepto);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaConceptos.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Conceptos concepto) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(concepto));
         tx.commit();

      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            System.out.println("Error PersistenciaConceptos.borrar: " + e);
         }
      }
   }

   @Override
   public List<Conceptos> buscarConceptos(EntityManager em) {
      em.clear();
      try {
         String sql = "SELECT * FROM CONCEPTOS ORDER BY DESCRIPCION"; 
         Query query = em.createNativeQuery(sql,Conceptos.class);
         List<Conceptos> listaConceptos = query.getResultList();
          return listaConceptos;
      } catch (Exception e) {
         System.out.println("Error en buscarConceptos() : " + e.getCause());
         return null;
      }
   }

   @Override
   public boolean verificarCodigoConcepto(EntityManager em, BigInteger codigoConcepto) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(c) FROM Conceptos c WHERE c.codigo = :codigo");
         query.setParameter("codigo", codigoConcepto);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         return resultado > 0;
      } catch (Exception e) {
         System.out.println("Exepcion: " + e);
         return false;
      }
   }

   @Override
   public Conceptos validarCodigoConcepto(EntityManager em, BigInteger codigoConcepto, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.codigo = :codigo AND c.empresa.secuencia = :secEmpresa");
         query.setParameter("codigo", codigoConcepto);
         query.setParameter("secEmpresa", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Conceptos concepto = (Conceptos) query.getSingleResult();
         return concepto;
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<Conceptos> conceptosPorEmpresa(EntityManager em, BigInteger secEmpresa) {
      try {
         em.clear();
         // Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.empresa.secuencia  = NVL(:secEmpresa, c.empresa.secuencia) ORDER BY c.codigo ASC");
         Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.empresa.secuencia  = :secEmpresa ORDER BY c.codigo ASC");

         query.setParameter("secEmpresa", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Conceptos> listaConceptos = query.getResultList();
         return listaConceptos;
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<Conceptos> conceptosEmpresaActivos_Inactivos(EntityManager em, BigInteger secEmpresa, String estado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.empresa.secuencia = :secEmpresa AND c.activo = :estado ORDER BY c.codigo ASC");
         query.setParameter("secEmpresa", secEmpresa);
         query.setParameter("estado", estado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Conceptos> listaConceptos = query.getResultList();
         return listaConceptos;
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<Conceptos> conceptosEmpresaSinPasivos(EntityManager em, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.empresa.secuencia = :secEmpresa AND c.naturaleza <> 'L' ORDER BY c.codigo ASC");
         query.setParameter("secEmpresa", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Conceptos> listaConceptos = query.getResultList();
         return listaConceptos;
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public void clonarConcepto(EntityManager em, BigInteger secConceptoOrigen, BigInteger codigoConceptoNuevo, String descripcionConceptoNuevo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         StoredProcedureQuery query = em.createStoredProcedureQuery("CONCEPTOS_PKG.CLONARCONCEPTO");
         query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
         query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);

         query.setParameter(1, secConceptoOrigen);
         query.setParameter(2, codigoConceptoNuevo);
         query.setParameter(3, descripcionConceptoNuevo);
         query.execute();
         tx.commit();
         System.out.println("clonarConcepto() Ya hiso commit");

//         String sqlQuery = "call CONCEPTOS_PKG.CLONARCONCEPTO(?, ?, ?)";
//         Query query = em.createNativeQuery(sqlQuery);
//         query.setParameter(1, secConceptoOrigen);
//         query.setParameter(2, codigoConceptoNuevo);
//         query.setParameter(3, descripcionConceptoNuevo);
//         query.executeUpdate();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error en clonarConcepto: " + e);
      }
   }

   @Override
   public Conceptos conceptosPorSecuencia(EntityManager em, BigInteger secConcepto) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Conceptos c WHERE c.secuencia=:secConcepto");
         query.setParameter("secConcepto", secConcepto);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Conceptos conceptos = (Conceptos) query.getSingleResult();
         return conceptos;
      } catch (Exception e) {
         System.out.println("Error Persistencia conceptosPorSecuencia : " + e.toString());
         return null;
      }
   }

   @Override
   public boolean eliminarConcepto(EntityManager em, BigInteger secuenciaConcepto) {
      try {
         em.clear();
         String sqlQuery = "call conceptos_pkg.Eliminarconcepto(:secuencia)";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter("secuencia", secuenciaConcepto);
         query.executeUpdate();
         return true;
      } catch (Exception e) {
         System.out.println("Error eliminarConcepto PersistenciaConceptos : " + e.toString());
         return false;
      }
   }

   @Override
   public String conceptoParaFormulaContrato(EntityManager em, BigInteger secuencia, Date fechaFin) {
      try {
         em.clear();
         String sqlQuery = "select substr(c.codigo||'-'||c.descripcion,1,200)\n"
                 + "from formulasconceptos fc, conceptos c\n"
                 + "where fc.formula = ? \n"
                 + "and fc.concepto = c.secuencia\n"
                 + "and exists (select 'x' from empresas e where e.secuencia = c.empresa)\n"
                 + "and fechainicial = (select max(fechainicial) from formulasconceptos fci\n"
                 + "where fci.formula = fc.formula\n"
                 + "and fci.concepto = fc.concepto\n"
                 + "and fci.fechainicial <= nvl(?,sysdate))";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         query.setParameter(2, fechaFin);
         List<String> listaConceptos = query.getResultList();
         String conceptoString = " ";
         if (!listaConceptos.isEmpty()) {
            int tam = listaConceptos.size();
            if (tam == 1) {
               conceptoString = listaConceptos.get(0);
            }
            if (tam >= 2) {
               conceptoString = "... " + listaConceptos.get(0);
            }
         }
         return conceptoString;
      } catch (Exception e) {
         System.out.println("Error PersistenciaConceptos.conceptoParaFormulaContrato. " + e.toString());
         return null;
      }
   }

   @Override
   public List<Conceptos> conceptoEmpresa(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "SELECT  C.*,\n"
                 + "decode(c.naturaleza,'P','PAGO','D','DESCUENTO','L','PASIVO','G','GASTO','N','NETO') NATURALEZA\n"
                 + "FROM CONCEPTOS C, EMPRESAS S\n"
                 + "WHERE C.EMPRESA = S.SECUENCIA\n"
                 + "ORDER BY C.DESCRIPCION";
         Query query = em.createNativeQuery(sqlQuery, Conceptos.class);
         List<Conceptos> listaConceptos = query.getResultList();
         return listaConceptos;
      } catch (Exception e) {
         System.out.println("Error PersistenciaConceptos.conceptoEmpresa " + e.toString());
         return null;
      }
   }

   @Override
   public List<Conceptos> novedadConceptos(EntityManager em) {
      try {
         em.clear();
         String sqlQuery = "SELECT  V.* FROM Conceptos V WHERE EXISTS (select 'x' from empresas e where v.empresa=e.secuencia) AND NVL(V.ACTIVO,'S')='S' ORDER BY V.CODIGO";
         Query query = em.createNativeQuery(sqlQuery, Conceptos.class);
         List<Conceptos> listaConceptos = query.getResultList();
         return listaConceptos;
      } catch (Exception e) {
         System.out.println("Error PersistenciaConceptos.novedadConceptos " + e.toString());
         return null;
      }
   }
}
