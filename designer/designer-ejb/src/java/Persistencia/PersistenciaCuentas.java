/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.CentrosCostos;
import Entidades.Cuentas;
import InterfacePersistencia.PersistenciaCuentasInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'Cuentas' de la base
 * de datos
 *
 * @author betelgeuse
 */
@Stateful
public class PersistenciaCuentas implements PersistenciaCuentasInterface {

   private static Logger log = Logger.getLogger(PersistenciaCuentas.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, Cuentas cuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(cuentas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaCuentas.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Cuentas cuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(cuentas);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaCuentas.editar: " + e);
      }
   }

   @Override
   public void borrar(EntityManager em, Cuentas cuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(cuentas));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaCuentas.borrar: " + e);
      }
   }

   @Override
   public List<Cuentas> buscarCuentas(EntityManager em) {
      try {
         em.clear();
         List<Cuentas> cuentas = (List<Cuentas>) em.createQuery("SELECT c FROM Cuentas c").getResultList();
         return cuentas;
      } catch (Exception e) {
         log.error("Error buscarCuentas PersistenciaCuentas : " + e.toString());
         return null;
      }
   }

   @Override
   public Cuentas buscarCuentasSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Cuentas c WHERE c.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Cuentas cuentas = (Cuentas) query.getSingleResult();
         return cuentas;
      } catch (Exception e) {
         log.error("Error buscarCuentasSecuencia PersistenciaCuentas : " + e.toString());
         Cuentas cuentas = null;
         return cuentas;
      }
   }

   @Override
   public List<Cuentas> buscarCuentasSecuenciaEmpresa(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM Cuentas c WHERE c.empresa.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Cuentas> cuentas = (List<Cuentas>) query.getResultList();
         return cuentas;
      } catch (Exception e) {
         log.error("Error buscarCuentasSecuenciaEmpresa PersistenciaCuentas : " + e.toString());
         List<Cuentas> cuentas = null;
         return cuentas;
      }
   }

   public int contarVigCuentasPorTipoccConceptoYCuentac(EntityManager em, BigInteger tipoCC, BigInteger cuentaC, BigInteger concepto, Date fechaIni) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT COUNT(*) FROM VIGENCIASCUENTAS V WHERE FECHAINICIAL = ? \n"
                 + " AND TIPOCC = ? AND CUENTAC = ? AND CONCEPTO = ?");
         query.setParameter(1, fechaIni);
         query.setParameter(2, tipoCC);
         query.setParameter(3, cuentaC);
         query.setParameter(4, concepto);
         BigDecimal variable = (BigDecimal) query.getSingleResult();
         if (variable != null) {
            return variable.intValue();
         } else {
            return 0;
         }
      } catch (Exception e) {
         log.error("Error contarVigCuentasPorTipoccConceptoYCuentac PersistenciaCuentas : " + e.toString());
         return 0;
      }
   }

   public int contarVigCuentasPorTipoccConceptoYCuentad(EntityManager em, BigInteger tipoCC, BigInteger cuentaD, BigInteger concepto, Date fechaIni) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT COUNT(*) FROM VIGENCIASCUENTAS V WHERE FECHAINICIAL = ? \n"
                 + " AND TIPOCC = ? AND CUENTAD = ? AND CONCEPTO = ?");
         query.setParameter(1, fechaIni);
         query.setParameter(2, tipoCC);
         query.setParameter(3, cuentaD);
         query.setParameter(4, concepto);
         BigDecimal variable = (BigDecimal) query.getSingleResult();
         if (variable != null) {
            return variable.intValue();
         } else {
            return 0;
         }
      } catch (Exception e) {
         log.error("Error contarVigCuentasPorTipoccConceptoYCuentad PersistenciaCuentas : " + e.toString());
         return 0;
      }
   }

   public CentrosCostos centroCostoLocalizacionTrabajador(EntityManager em, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT CC.* FROM centroscostos CC\n"
                 + " WHERE cc.empresa = ? AND cc.nombre LIKE '%LOCALIZACION%TRABAJADOR%'", CentrosCostos.class);
         query.setParameter(1, secEmpresa);
         CentrosCostos cc = (CentrosCostos) query.getSingleResult();
         return cc;
      } catch (Exception e) {
         log.error("Error centroCostoLocalizacionTrabajador PersistenciaCuentas : " + e.toString());
         return null;
      }
   }

   public CentrosCostos centroCostoContabilidad(EntityManager em, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT CC.* FROM centroscostos CC\n"
                 + " WHERE cc.empresa = ? AND cc.nombre LIKE '%CONTABILIDAD%'", CentrosCostos.class);
         query.setParameter(1, secEmpresa);
         CentrosCostos cc = (CentrosCostos) query.getSingleResult();
         return cc;
      } catch (Exception e) {
         log.error("Error centroCostoContabilidad PersistenciaCuentas : " + e.toString());
         return null;
      }
   }

   public List<Cuentas> cuenta2505(EntityManager em, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT CC.* FROM cuentas CC\n"
                 + " WHERE cc.empresa = ? AND cc.codigo LIKE '2505%'", Cuentas.class);
         query.setParameter(1, secEmpresa);
         List<Cuentas> cc = query.getResultList();
         return cc;
      } catch (Exception e) {
         log.error("Error cuenta2505 PersistenciaCuentas : " + e.toString());
         return null;
      }
   }
}
