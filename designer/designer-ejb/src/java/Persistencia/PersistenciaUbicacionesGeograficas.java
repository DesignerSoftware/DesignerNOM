/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.UbicacionesGeograficas;
import InterfacePersistencia.PersistenciaUbicacionesGeograficasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PersistenciaUbicacionesGeograficas implements PersistenciaUbicacionesGeograficasInterface {

   @Override
   public void crear(EntityManager em, UbicacionesGeograficas ubicacionGeografica) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         if (ubicacionGeografica.getZona() != null) {
            if (ubicacionGeografica.getZona().isEmpty() || ubicacionGeografica.getZona().equals("") || ubicacionGeografica.getZona().equals(" ")) {
               ubicacionGeografica.setZona(null);
            }
         }
         tx.begin();
         em.merge(ubicacionGeografica);
         tx.commit();
      } catch (Exception e) {
         System.err.println("Error crear PersistenciaUbicacionesGeograficas ERROR " + e.getMessage());
      }
   }

   @Override
   public void editar(EntityManager em, UbicacionesGeograficas ubicacionGeografica) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(ubicacionGeografica);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaUbicacionesGeograficas.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, UbicacionesGeograficas ubicacionGeografica) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(ubicacionGeografica));
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaUbicacionesGeograficas.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<UbicacionesGeograficas> consultarUbicacionesGeograficas(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT u FROM UbicacionesGeograficas u, Empresas e WHERE u.empresa.secuencia = e.secuencia ORDER BY u.codigo ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<UbicacionesGeograficas> ubicacionesGeograficas = query.getResultList();
         return ubicacionesGeograficas;
      } catch (Exception e) {
         System.out.println("Error en Persistencia Ubicaciones Geograficas " + e.getMessage());
         return null;
      }
   }

   @Override
   public UbicacionesGeograficas consultarUbicacionGeografica(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cc FROM UbicacionesGeograficas cc WHERE cc.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         UbicacionesGeograficas ubicacionGeografica = (UbicacionesGeograficas) query.getSingleResult();
         return ubicacionGeografica;
      } catch (Exception e) {
          System.out.println("Persistencia.PersistenciaUbicacionesGeograficas.consultarUbicacionGeografica()" + e.getMessage());
          return null;
      }
   }

   @Override
   public List<UbicacionesGeograficas> consultarUbicacionesGeograficasPorEmpresa(EntityManager em, BigInteger secEmpresa) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT cce FROM UbicacionesGeograficas cce WHERE cce.empresa.secuencia = :secuenciaEmpr ORDER BY cce.codigo ASC");
         query.setParameter("secuenciaEmpr", secEmpresa);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<UbicacionesGeograficas> ubicacionGeografica = query.getResultList();
         return ubicacionGeografica;
      } catch (Exception e) {
         System.out.println("Error en Persistencia PersistenciaUbicacionesGeograficas consultarUbicacionesGeograficasPorEmpresa ERROR : " + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contarAfiliacionesEntidadesUbicacionGeografica(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM afiliacionesentidades WHERE ubicaciongeografica = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         System.out.println("ERROR PERSISTENCIAUBICACIONESGEOGRAFICAS CONTARAFILIACIONESENTIDADESUBICACIONGEOGRAFICA ERROR : " + e.getMessage());
         return retorno;
      }
   }

   @Override
   public BigInteger contarInspeccionesUbicacionGeografica(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM inspecciones WHERE ubicaciongeografica = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         System.out.println("ERROR PERSISTENCIAUBICACIONESGEOGRAFICAS CONTARINSPECCIONESUBICACIONGEOGRAFICA ERROR : " + e.getMessage());
         return retorno;
      }
   }

   @Override
   public BigInteger contarParametrosInformesUbicacionGeografica(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM parametrosinformes WHERE ubicaciongeografica = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         System.out.println("ERROR PERSISTENCIAUBICACIONESGEOGRAFICAS CONTARPARAMETROSINFORMESUBICACIONGEOGRAFICA ERROR : " + e.getMessage());
         return retorno;
      }
   }

   @Override
   public BigInteger contarRevisionesUbicacionGeografica(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM revisiones WHERE ubicaciongeografica = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         System.out.println("ERROR PERSISTENCIAUBICACIONESGEOGRAFICAS CONTARREVICIONESUBICACIONGEOGRAFICA ERROR : " + e.getMessage());
         return retorno;
      }
   }

   @Override
   public BigInteger contarVigenciasUbicacionesGeografica(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM vigenciasubicaciones WHERE ubicacion = ?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         System.out.println("ERROR PERSISTENCIAUBICACIONESGEOGRAFICAS CONTARVIGENCIASUBICACIONESUBICACIONGEOGRAFICA ERROR : " + e.getMessage());
         return retorno;
      }
   }

   @Override
   public int existeCiudadporSecuencia(EntityManager em, BigInteger secuenciaCiudad) {
      int retorno = 0;
      try {
         em.clear();
         String sqlQuery = "SELECT SUM(CONTEO) FROM \n"
                 + "(SELECT COUNT(*) CONTEO FROM UBICACIONESGEOGRAFICAS WHERE CIUDAD = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM DETALLESEMPRESAS WHERE CIUDAD = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM DIRECCIONES WHERE CIUDAD = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM EMPRESASBANCOS WHERE CIUDAD = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM JUZGADOS WHERE CIUDAD = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM PARAMETROSREPORTES WHERE CIUDADNOM = " + secuenciaCiudad + " OR CIUDAD = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM PERSONAS WHERE CIUDADNACIMIENTO = " + secuenciaCiudad + " OR CIUDADDOCUMENTO = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM SUCURSALES WHERE CIUDAD = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM TELEFONOS WHERE CIUDAD = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM TERCEROS WHERE CIUDAD = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM TERCEROSSUCURSALES WHERE CIUDAD = " + secuenciaCiudad + " \n"
                 + "UNION \n"
                 + "SELECT COUNT(*) CONTEO FROM VIGENCIASTIPOSCONTRATOS WHERE CIUDAD = " + secuenciaCiudad + ")";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuenciaCiudad);
         retorno = Integer.parseInt(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         System.err.println("ERROR PERSISTENCIAUBICACIONESGEOGRAFICAS existeCiudadporSecuencia : " + e.getMessage());
         return retorno;
      }
   }
}
