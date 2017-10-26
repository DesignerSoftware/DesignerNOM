/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasCuentas;
import Entidades.VigenciasCuentasAux;
import InterfacePersistencia.PersistenciaVigenciasCuentasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasCuentas' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasCuentas implements PersistenciaVigenciasCuentasInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasCuentas.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
    */
   @Override
   public void crear(EntityManager em, VigenciasCuentas vigenciasCuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(vigenciasCuentas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCuentas.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasCuentas vigenciasCuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasCuentas);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCuentas.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasCuentas vigenciasCuentas) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasCuentas));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasCuentas.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<VigenciasCuentas> buscarVigenciasCuentas(EntityManager em) {
      em.clear();
      Query query = em.createQuery("SELECT v FROM VigenciasCuentas v");
      query.setHint("javax.persistence.cache.storeMode", "REFRESH");
      List<VigenciasCuentas> vigenciasCuentas = (List<VigenciasCuentas>) query.getResultList();
      if (vigenciasCuentas != null) {
         if (!vigenciasCuentas.isEmpty()) {
            em.clear();
            Query query2 = em.createNativeQuery("select VC.SECUENCIA, TCC.NOMBRE NOMBRETIPOCC, CO.CODIGO CODIGOCONCEPTO, CO.EMPRESA, CO.NATURALEZA,\n"
                    + " CO.DESCRIPCION DESCRIPCIONCONCEPTO, CCD.NOMBRE NOMBRECONSD, CCC.NOMBRE NOMBRECONSC,\n"
                    + " CCC.CODIGO CODCONSOLIDAC, CCD.CODIGO CODCONSOLIDAD, CUC.CODIGO CODCUENTAC,\n"
                    + " CUD.CODIGO CODCUENTAD, CUD.DESCRIPCION NOMBRECUENTAD, CUC.DESCRIPCION NOMBRECUENTAC\n"
                    + " FROM VIGENCIASCUENTAS VC, CONCEPTOS CO , TIPOSCENTROSCOSTOS TCC, CUENTAS CUD, CUENTAS CUC, CENTROSCOSTOS CCD, CENTROSCOSTOS CCC\n"
                    + " WHERE\n"
                    + " VC.CONCEPTO = CO.SECUENCIA\n"
                    + " AND VC.TIPOCC = TCC.SECUENCIA\n"
                    + " AND CUD.SECUENCIA = VC.CUENTAD\n"
                    + " AND CUC.SECUENCIA = VC.CUENTAC\n"
                    + " AND CCC.SECUENCIA = VC.CONSOLIDADORC\n"
                    + " AND CCD.SECUENCIA = VC.CONSOLIDADORD", VigenciasCuentasAux.class);
            List<VigenciasCuentasAux> vigenciasCuentasAux = query2.getResultList();
            log.warn("buscarVigenciasCuentasPorConcepto() Ya consulo Transients");
            if (vigenciasCuentasAux != null) {
               if (!vigenciasCuentasAux.isEmpty()) {
                  log.warn("buscarVigenciasCuentasPorConcepto() vigenciasCuentasAux.size(): " + vigenciasCuentasAux.size());
                  for (VigenciasCuentasAux recAux : vigenciasCuentasAux) {
                     for (VigenciasCuentas recVigCuenta : vigenciasCuentas) {
                        if (recAux.getSecuencia().equals(recVigCuenta.getSecuencia())) {
                           recVigCuenta.llenarTransients(recAux);
                        }
                     }
                  }
               }
            }
         }
      }
      return vigenciasCuentas;
   }

   @Override
   public VigenciasCuentas buscarVigenciaCuentaSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vc FROM VigenciasCuentas vc WHERE vc.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         VigenciasCuentas vigenciaCuenta = (VigenciasCuentas) query.getSingleResult();
         if (vigenciaCuenta != null) {
            if (vigenciaCuenta.getSecuencia() != null) {
               em.clear();
               Query query2 = em.createNativeQuery("select VC.SECUENCIA, TCC.NOMBRE NOMBRETIPOCC, CO.CODIGO CODIGOCONCEPTO, CO.EMPRESA, CO.NATURALEZA,\n"
                       + " CO.DESCRIPCION DESCRIPCIONCONCEPTO, CCD.NOMBRE NOMBRECONSD, CCC.NOMBRE NOMBRECONSC,\n"
                       + " CCC.CODIGO CODCONSOLIDAC, CCD.CODIGO CODCONSOLIDAD, CUC.CODIGO CODCUENTAC,\n"
                       + " CUD.CODIGO CODCUENTAD, CUD.DESCRIPCION NOMBRECUENTAD, CUC.DESCRIPCION NOMBRECUENTAC\n"
                       + " FROM VIGENCIASCUENTAS VC, CONCEPTOS CO , TIPOSCENTROSCOSTOS TCC, CUENTAS CUD, CUENTAS CUC, CENTROSCOSTOS CCD, CENTROSCOSTOS CCC\n"
                       + " WHERE \n"
                       + " VC.SECUENCIA = " + secuencia + "\n"
                       + " AND VC.CONCEPTO = CO.SECUENCIA\n"
                       + " AND VC.TIPOCC = TCC.SECUENCIA\n"
                       + " AND CUD.SECUENCIA = VC.CUENTAD\n"
                       + " AND CUC.SECUENCIA = VC.CUENTAC\n"
                       + " AND CCC.SECUENCIA = VC.CONSOLIDADORC\n"
                       + " AND CCD.SECUENCIA = VC.CONSOLIDADORD", VigenciasCuentasAux.class);
               VigenciasCuentasAux vigenciaCuentaAux = (VigenciasCuentasAux) query2.getSingleResult();
               log.warn("buscarVigenciaCuentaSecuencia() vigenciaCuentaAux: " + vigenciaCuentaAux);
               if (vigenciaCuentaAux != null) {
                  if (vigenciaCuentaAux.getSecuencia() != null && vigenciaCuentaAux.getSecuencia().equals(vigenciaCuenta.getSecuencia())) {
                     vigenciaCuenta.llenarTransients(vigenciaCuentaAux);
                  }
               }
            }
         }
         return vigenciaCuenta;
      } catch (Exception e) {
         log.error("PersistenciaVigenciasCuentas.buscarVigenciaCuentaSecuencia():  ", e);
         return null;
      }
   }

   @Override
   public List<VigenciasCuentas> buscarVigenciasCuentasPorCredito(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vc FROM VigenciasCuentas vc WHERE vc.cuentac =:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasCuentas> vigenciasCuentas = (List<VigenciasCuentas>) query.getResultList();
         if (vigenciasCuentas != null) {
            if (!vigenciasCuentas.isEmpty()) {
               em.clear();
               Query query2 = em.createNativeQuery("select VC.SECUENCIA, TCC.NOMBRE NOMBRETIPOCC, CO.CODIGO CODIGOCONCEPTO, CO.EMPRESA, CO.NATURALEZA,\n"
                       + " CO.DESCRIPCION DESCRIPCIONCONCEPTO, CCD.NOMBRE NOMBRECONSD, CCC.NOMBRE NOMBRECONSC,\n"
                       + " CCC.CODIGO CODCONSOLIDAC, CCD.CODIGO CODCONSOLIDAD, CUC.CODIGO CODCUENTAC,\n"
                       + " CUD.CODIGO CODCUENTAD, CUD.DESCRIPCION NOMBRECUENTAD, CUC.DESCRIPCION NOMBRECUENTAC\n"
                       + " FROM VIGENCIASCUENTAS VC, CONCEPTOS CO , TIPOSCENTROSCOSTOS TCC, CUENTAS CUD, CUENTAS CUC, CENTROSCOSTOS CCD, CENTROSCOSTOS CCC\n"
                       + " WHERE\n"
                       + " VC.CONCEPTO = CO.SECUENCIA\n"
                       + " AND VC.TIPOCC = TCC.SECUENCIA\n"
                       + " AND CUD.SECUENCIA = VC.CUENTAD\n"
                       + " AND CUC.SECUENCIA = VC.CUENTAC\n"
                       + " AND CUC.SECUENCIA = " + secuencia + "\n"
                       + " AND CCC.SECUENCIA = VC.CONSOLIDADORC\n"
                       + " AND CCD.SECUENCIA = VC.CONSOLIDADORD", VigenciasCuentasAux.class);
               List<VigenciasCuentasAux> vigenciasCuentasAux = query2.getResultList();
               log.warn("buscarVigenciasCuentasPorConcepto() Ya consulo Transients");
               if (vigenciasCuentasAux != null) {
                  if (!vigenciasCuentasAux.isEmpty()) {
                     log.warn("buscarVigenciasCuentasPorConcepto() vigenciasCuentasAux.size(): " + vigenciasCuentasAux.size());
                     for (VigenciasCuentasAux recAux : vigenciasCuentasAux) {
                        for (VigenciasCuentas recVigCuenta : vigenciasCuentas) {
                           if (recAux.getSecuencia().equals(recVigCuenta.getSecuencia())) {
                              recVigCuenta.llenarTransients(recAux);
                           }
                        }
                     }
                  }
               }
            }
         }
         return vigenciasCuentas;
      } catch (Exception e) {
         log.error("Error buscarVigenciasCuentasPorCredito Persistencia :  ", e);
         return null;
      }
   }

   @Override
   public List<VigenciasCuentas> buscarVigenciasCuentasPorDebito(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vc FROM VigenciasCuentas vc WHERE vc.cuentad =:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasCuentas> vigenciasCuentas = (List<VigenciasCuentas>) query.getResultList();
         if (vigenciasCuentas != null) {
            if (!vigenciasCuentas.isEmpty()) {
               em.clear();
               Query query2 = em.createNativeQuery("select VC.SECUENCIA, TCC.NOMBRE NOMBRETIPOCC, CO.CODIGO CODIGOCONCEPTO, CO.EMPRESA, CO.NATURALEZA,\n"
                       + " CO.DESCRIPCION DESCRIPCIONCONCEPTO, CCD.NOMBRE NOMBRECONSD, CCC.NOMBRE NOMBRECONSC,\n"
                       + " CCC.CODIGO CODCONSOLIDAC, CCD.CODIGO CODCONSOLIDAD, CUC.CODIGO CODCUENTAC,\n"
                       + " CUD.CODIGO CODCUENTAD, CUD.DESCRIPCION NOMBRECUENTAD, CUC.DESCRIPCION NOMBRECUENTAC\n"
                       + " FROM VIGENCIASCUENTAS VC, CONCEPTOS CO , TIPOSCENTROSCOSTOS TCC, CUENTAS CUD, CUENTAS CUC, CENTROSCOSTOS CCD, CENTROSCOSTOS CCC\n"
                       + " WHERE\n"
                       + " VC.CONCEPTO = CO.SECUENCIA\n"
                       + " AND VC.TIPOCC = TCC.SECUENCIA\n"
                       + " AND CUD.SECUENCIA = VC.CUENTAD\n"
                       + " AND CUC.SECUENCIA = VC.CUENTAC\n"
                       + " AND CUD.SECUENCIA = " + secuencia + "\n"
                       + " AND CCC.SECUENCIA = VC.CONSOLIDADORC\n"
                       + " AND CCD.SECUENCIA = VC.CONSOLIDADORD", VigenciasCuentasAux.class);
               List<VigenciasCuentasAux> vigenciasCuentasAux = query2.getResultList();
               log.warn("buscarVigenciasCuentasPorConcepto() Ya consulo Transients");
               if (vigenciasCuentasAux != null) {
                  if (!vigenciasCuentasAux.isEmpty()) {
                     log.warn("buscarVigenciasCuentasPorConcepto() vigenciasCuentasAux.size(): " + vigenciasCuentasAux.size());
                     for (VigenciasCuentasAux recAux : vigenciasCuentasAux) {
                        for (VigenciasCuentas recVigCuenta : vigenciasCuentas) {
                           if (recAux.getSecuencia().equals(recVigCuenta.getSecuencia())) {
                              recVigCuenta.llenarTransients(recAux);
                           }
                        }
                     }
                  }
               }
            }
         }
         return vigenciasCuentas;
      } catch (Exception e) {
         log.error("Error buscarVigenciasCuentasPorDebito Persistencia :  ", e);
         return null;
      }
   }

   @Override
   public List<VigenciasCuentas> buscarVigenciasCuentasPorConcepto(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vc FROM VigenciasCuentas vc WHERE vc.concepto=:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasCuentas> vigenciasCuentas = (List<VigenciasCuentas>) query.getResultList();
         if (vigenciasCuentas != null) {
            if (!vigenciasCuentas.isEmpty()) {
               em.clear();
               Query query2 = em.createNativeQuery("select VC.SECUENCIA, TCC.NOMBRE NOMBRETIPOCC, CO.CODIGO CODIGOCONCEPTO,"
                       + " CO.EMPRESA, CO.NATURALEZA, CO.DESCRIPCION DESCRIPCIONCONCEPTO, CCD.NOMBRE NOMBRECONSD,"
                       + " CCC.NOMBRE NOMBRECONSC, CCC.CODIGO CODCONSOLIDAC, CCD.CODIGO CODCONSOLIDAD, CUC.CODIGO CODCUENTAC,\n"
                       + " CUD.CODIGO CODCUENTAD, CUD.DESCRIPCION NOMBRECUENTAD, CUC.DESCRIPCION NOMBRECUENTAC\n"
                       + " FROM VIGENCIASCUENTAS VC, CONCEPTOS CO , TIPOSCENTROSCOSTOS TCC, CUENTAS CUD, CUENTAS CUC, CENTROSCOSTOS CCD, CENTROSCOSTOS CCC\n"
                       + " WHERE CO.SECUENCIA = " + secuencia + "\n"
                       + " AND VC.CONCEPTO = CO.SECUENCIA\n"
                       + " AND VC.TIPOCC = TCC.SECUENCIA\n"
                       + " AND CUD.SECUENCIA = VC.CUENTAD\n"
                       + " AND CUC.SECUENCIA = VC.CUENTAC\n"
                       + " AND CCC.SECUENCIA = VC.CONSOLIDADORC\n"
                       + " AND CCD.SECUENCIA = VC.CONSOLIDADORD", VigenciasCuentasAux.class);
               List<VigenciasCuentasAux> vigenciasCuentasAux = query2.getResultList();
               log.warn("buscarVigenciasCuentasPorConcepto() Ya consulo Transients");
               if (vigenciasCuentasAux != null) {
                  if (!vigenciasCuentasAux.isEmpty()) {
                     log.warn("buscarVigenciasCuentasPorConcepto() vigenciasCuentasAux.size(): " + vigenciasCuentasAux.size());
                     for (VigenciasCuentasAux recAux : vigenciasCuentasAux) {
                        for (VigenciasCuentas recVigCuenta : vigenciasCuentas) {
                           if (recAux.getSecuencia().equals(recVigCuenta.getSecuencia())) {
                              recVigCuenta.llenarTransients(recAux);
                           }
                        }
                     }
                  }
               }
            }
         }
         return vigenciasCuentas;
      } catch (Exception e) {
         log.error("Error buscarVigenciasCuentasPorConcepto Persistencia :  ", e);
         return null;
      }
   }
}
