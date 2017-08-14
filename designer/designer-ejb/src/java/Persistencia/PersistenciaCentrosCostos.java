/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.CentrosCostos;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'CentrosCostos' de la
 * base de datos
 *
 * @author Hugo David Sin Gutiérrez
 */
@Stateless
public class PersistenciaCentrosCostos implements PersistenciaCentrosCostosInterface {

   private static Logger log = Logger.getLogger(PersistenciaCentrosCostos.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, CentrosCostos centrosCostos) {
      try {
         if (centrosCostos.getManoobra() == null) {
            log.warn("PERSISTENCIA CENTROSCOSTOS MANO DE OBRA ES NULA ");
         } else if (centrosCostos.getManoobra().isEmpty() || centrosCostos.getManoobra().equals("") || centrosCostos.getManoobra().equals(" ")) {
            centrosCostos.setManoobra(null);
         }

         em.clear();
         EntityTransaction tx = em.getTransaction();
         try {
            tx.begin();
            em.merge(centrosCostos);
            tx.commit();
         } catch (Exception e) {
            log.error("Error PersistenciaVigenciasCargos.crear: " + e);
            if (tx.isActive()) {
               tx.rollback();
            }
         }
      } catch (Exception e) {
         log.error("Error crear PersistenciaCentrosCostos E " + e);
      }
   }

   @Override
   public void editar(EntityManager em, CentrosCostos centrosCostos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(centrosCostos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaCentrosCostos.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, CentrosCostos centrosCostos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(centrosCostos));
         tx.commit();

      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            log.error("Error PersistenciaCentrosCostos.borrar: " + e);
         }
      }
   }

   @Override
   public List<CentrosCostos> buscarCentrosCostos(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cc FROM CentrosCostos cc");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<CentrosCostos> centrosCostos = query.getResultList();
         return centrosCostos;
      } catch (Exception e) {
         log.error("Error buscarCentrosCostos PersistenciaCentrosCostos " + e);
         return null;
      }
   }

   @Override
   public CentrosCostos buscarCentroCostoSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cc FROM CentrosCostos cc WHERE cc.secuencia =:secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         CentrosCostos centrosCostos = (CentrosCostos) query.getSingleResult();
         return centrosCostos;
      } catch (Exception e) {
         log.error("Error buscarCentroCostoSecuencia PersistenciaCentrosCostos " + e);
         CentrosCostos centrosCostos = null;
         return centrosCostos;
      }
   }

   @Override
   public List<CentrosCostos> buscarCentrosCostosEmpr(EntityManager em, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cce FROM CentrosCostos cce WHERE cce.empresa.secuencia = :secuenciaEmpr AND cce.comodin='N' ORDER BY cce.codigo ASC");
         query.setParameter("secuenciaEmpr", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<CentrosCostos> centrosCostos = query.getResultList();
         return centrosCostos;
      } catch (Exception e) {
         log.error("Error en Persistencia PersistenciaCentrosCostos BuscarCentrosCostosEmpr " + e);
         return null;
      }
   }

   @Override
   public List<CentrosCostos> buscarCentrosCostosCM(EntityManager em) {
      try {
         em.clear();
         Query query = em.createNativeQuery("select c.* \n"
                 + "from estructuras e,centroscostos c,organigramas o,empresas em \n"
                 + "where e.centrocosto=c.secuencia and e.organigrama=o.secuencia and o.empresa=em.secuencia", CentrosCostos.class);
         List<CentrosCostos> centrosCostos = query.getResultList();
         return centrosCostos;
      } catch (Exception e) {
         log.error("Error en Persistencia PersistenciaCentrosCostos buscarCentrosCostosCM " + e);
         return null;
      }
   }

   @Override
   public BigInteger contadorComprobantesContables(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, comprobantescontables ccs WHERE cc.secuencia = ccs.centrocostoconsolidador AND cc.secuencia = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         log.warn("PersistenciaCENTROSCOSTOS contadorComprobantesContables  " + query.getSingleResult());
         return new BigInteger(query.getSingleResult().toString());

      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorHvReferencias. " + e);
         return null;
      }
   }

   @Override
   public BigInteger contadorDetallesCCConsolidador(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, detallescc dcc WHERE cc.secuencia = dcc.ccconsolidador AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorDetallesCCConsolidador. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorDetallesCCDetalle(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, detallescc dcc WHERE cc.secuencia = dcc.ccdetalle AND cc.secuencia=?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorDetallesCCDetalle. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorEmpresas(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, empresas e WHERE cc.secuencia = e.centrocosto AND cc.secuencia=?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorEmpresas. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorEstructuras(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, estructuras e WHERE cc.secuencia = e.centrocosto AND cc.secuencia=?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorEstructuras. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorInterconCondor(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, intercon_condor ic WHERE cc.secuencia = ic.centrocosto AND cc.secuencia=?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorInterconCondor. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorInterconDynamics(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, intercon_dynamics id WHERE cc.secuencia = id.centrocosto AND cc.secuencia=?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorInterconDynamics. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorInterconGeneral(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, intercon_general ig WHERE cc.secuencia = ig.centrocosto AND cc.secuencia=?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorInterconGeneral. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorInterconHelisa(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, intercon_helisa ih WHERE cc.secuencia = ih.centrocosto AND cc.secuencia=?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorInterconHelisa. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorInterconSapbo(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, intercon_sapbo isp WHERE cc.secuencia = isp.centrocosto AND cc.secuencia=?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorInterconSapbo. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorInterconSiigo(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, intercon_siigo isi WHERE cc.secuencia = isi.centrocosto AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorInterconSiigo. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorInterconTotal(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, intercon_total it WHERE cc.secuencia = it.centrocosto AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorInterconTotal. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorNovedadesC(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, novedades n WHERE cc.secuencia = n.centrocostoc AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorNovedadesC. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorNovedadesD(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, novedades n WHERE cc.secuencia = n.centrocostod AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorNovedadesD. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorProcesosProductivos(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, procesosproductivos pp WHERE cc.secuencia = pp.centrocosto AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorProcesosProductivos. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorProyecciones(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, proyecciones p WHERE cc.secuencia = p.centrocosto AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorProyecciones. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorSolucionesNodosC(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, solucionesnodos sn WHERE cc.secuencia = sn.centrocostoc AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorSolucionesNodosC. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorSolucionesNodosD(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, solucionesnodos sn WHERE cc.secuencia = sn.centrocostod AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorSolucionesNodosD. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorSoPanoramas(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, sopanoramas sp WHERE cc.secuencia = sp.centrocosto AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorSoPanoramas. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorTerceros(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, terceros t WHERE cc.secuencia = t.centrocosto AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorTerceros. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorUnidadesRegistradas(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, unidadesregistradas ur WHERE cc.secuencia = ur.centrocosto AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorUnidadesRegistradas. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorVigenciasCuentasC(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, vigenciascuentas vc WHERE cc.secuencia = vc.consolidadorc AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorVigenciasCuentasC. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorVigenciasCuentasD(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, vigenciascuentas vc WHERE cc.secuencia = vc.consolidadord AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorVigenciasCuentasD. " + e);
         return retorno;
      }
   }

   @Override
   public BigInteger contadorVigenciasProrrateos(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM centroscostos cc, vigenciasprorrateos vp WHERE cc.secuencia = vp.centrocosto AND cc.secuencia =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Error PERSISTENCIACENTROSCOSTOS  contadorVigenciasProrrateos. " + e);
         return retorno;
      }
   }

   @Override
   public List<CentrosCostos> buscarCentroCostoPorSecuenciaEmpresa(EntityManager em, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT c FROM CentrosCostos c WHERE EXISTS (SELECT e FROM Empresas e WHERE e.secuencia=c.empresa.secuencia AND e.secuencia=:secEmpresa) AND c.obsoleto!='S' ORDER BY c.codigo ASC");
         query.setParameter("secEmpresa", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<CentrosCostos> centrosCostos = query.getResultList();
         return centrosCostos;
      } catch (Exception e) {
         log.error("Error buscarCentroCostoSecuencia PersistenciaCentrosCostos : " + e.toString());
         List<CentrosCostos> centrosCostos = null;
         return centrosCostos;
      }
   }
}
