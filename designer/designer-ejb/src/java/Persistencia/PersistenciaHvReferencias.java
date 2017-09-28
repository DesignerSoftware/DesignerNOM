/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.HVHojasDeVida;
import Entidades.HvReferencias;
import InterfacePersistencia.PersistenciaHvReferenciasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'HvReferencias' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaHvReferencias implements PersistenciaHvReferenciasInterface {

   private static Logger log = Logger.getLogger(PersistenciaHvReferencias.class);

   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    *
    * @param em
    */
   @Override
   public void crear(EntityManager em, HvReferencias hvReferencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         log.error("pass");
         tx.begin();
         em.merge(hvReferencias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaHvReferencias.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, HvReferencias hvReferencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(hvReferencias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaHvReferencias.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, HvReferencias hvReferencias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(hvReferencias));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaHvReferencias.borrar:  ", e);
      }
   }

   @Override
   public List<HvReferencias> buscarHvReferencias(EntityManager em) {
      em.clear();
      Query query = em.createQuery("SELECT te FROM HvReferencias te ");
      query.setHint("javax.persistence.cache.storeMode", "REFRESH");
      List<HvReferencias> listHvReferencias = query.getResultList();
      return listHvReferencias;
   }

   @Override
   public List<HvReferencias> consultarHvReferenciasPersonalesPorPersona(EntityManager em, BigInteger secPersona) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT hr FROM HvReferencias hr , Personas e WHERE e.secuencia = hr.hojadevida.persona.secuencia AND e.secuencia = :secuenciaEmpl AND hr.tipo='PERSONALES' ORDER BY hr.nombrepersona ");
         query.setParameter("secuenciaEmpl", secPersona);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<HvReferencias> listHvReferencias = query.getResultList();
         if (listHvReferencias != null) {
            log.warn("PersistenciaHvReferencias Tamaño listHvReferencias : " + listHvReferencias.size());
         }
         return listHvReferencias;
      } catch (Exception e) {
         log.error("Error en Persistencia Vigencias Normas Empleados  ", e);
         return null;
      }
   }

   @Override
   public List<HvReferencias> referenciasPersonalesPersona(EntityManager em, BigInteger secuenciaHV) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(hvr) FROM HvReferencias hvr WHERE hvr.hojadevida.secuencia = :secuenciaHV");
         query.setParameter("secuenciaHV", secuenciaHV);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         if (resultado > 0) {
            Query queryFinal = em.createQuery("SELECT hvr FROM HvReferencias hvr WHERE hvr.hojadevida.secuencia = :secuenciaHV and hvr.tipo = 'PERSONALES'");
            queryFinal.setParameter("secuenciaHV", secuenciaHV);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<HvReferencias> listaReferenciasPersonales = queryFinal.getResultList();
            return listaReferenciasPersonales;
         }
         return null;
      } catch (Exception e) {
         log.error("Error PersistenciasHvReferencias.referenciasPersonalesPersona ", e);
         return null;
      }
   }

   @Override
   public List<HVHojasDeVida> consultarHvHojaDeVidaPorPersona(EntityManager em, BigInteger secPersona) {
      try {
         em.clear();
         String sql = "SELECT * FROM HVHOJASDEVIDA hv , PERSONAS p WHERE p.secuencia= hv.persona AND p.secuencia = ?";
         log.warn("PersistenciaHvReferencias secuencia empleado hoja de vida " + secPersona);
         Query query = em.createNativeQuery(sql, HVHojasDeVida.class);
         query.setParameter(1, secPersona);
         List<HVHojasDeVida> hvHojasDeVIda = query.getResultList();
         return hvHojasDeVIda;
      } catch (Exception e) {
         log.error("Error en Persistencia HVREFERENCIAS buscarHvHojaDeVidaPorPersona  ", e);
         return null;
      }
   }

   @Override
   public List<HvReferencias> consultarHvReferenciasFamiliarPorPersona(EntityManager em, BigInteger secEmpleado) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT hr FROM  HvReferencias hr , Personas e WHERE e.secuencia  = hr.hojadevida.persona.secuencia AND e.secuencia = :secuenciaEmpl AND hr.tipo='FAMILIARES' ORDER BY hr.nombrepersona ");
         query.setParameter("secuenciaEmpl", secEmpleado);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<HvReferencias> listHvReferencias = query.getResultList();
         return listHvReferencias;
      } catch (Exception e) {
         log.error("Error en Persistencia HvRefencias 1   ", e);
         return null;
      }
   }

   @Override
   public HvReferencias buscarHvReferencia(EntityManager em, BigInteger secHvReferencias) {
      try {
         em.clear();
         return em.find(HvReferencias.class, secHvReferencias);
      } catch (Exception e) {
         log.error("PersistenciaHvReferencias.buscarHvReferencia() e:  ", e);
         return null;
      }
   }

   @Override
   public List<HvReferencias> contarReferenciasFamiliaresPersona(EntityManager em, BigInteger secuenciaHV) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(hvr) FROM HvReferencias hvr WHERE hvr.hojadevida.secuencia = :secuenciaHV");
         query.setParameter("secuenciaHV", secuenciaHV);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         if (resultado > 0) {
            Query queryFinal = em.createQuery("SELECT hvr FROM HvReferencias hvr WHERE hvr.hojadevida.secuencia = :secuenciaHV and hvr.tipo = 'FAMILIARES'");
            queryFinal.setParameter("secuenciaHV", secuenciaHV);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<HvReferencias> listaReferenciasPersonales = queryFinal.getResultList();
            return listaReferenciasPersonales;
         }
         return null;
      } catch (Exception e) {
         log.error("Error PersistenciasHvReferencias.referenciasPersonalesPersona ", e);
         return null;
      }
   }

   @Override
   public String primeraReferenciaFamiliar(EntityManager em, BigInteger secHV) {
      String referenciaF;
      try {
         em.clear();
         String sql = "SELECT R.NOMBREPERSONA \n"
                 + "   FROM  HVREFERENCIAS R \n"
                 + "   WHERE R.HOJADEVIDA = ? AND \n"
                 + "   R.TIPO = 'FAMILIARES' \n"
                 + "   AND ROWNUM = 1";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secHV);
         referenciaF = (String) query.getSingleResult();
         if (referenciaF == null) {
            referenciaF = "";
         }
         return referenciaF;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("PersistenciaHvReferencias.primeraReferenciaFamiliar() e: " + e);
         } else {
            log.error("PersistenciaHvReferencias.primeraReferenciaFamiliar() e:  ", e);
         }
         return "";
      }
   }

   @Override
   public String primeraReferenciaPersonal(EntityManager em, BigInteger secHV) {
      String referenciaP;
      try {
         em.clear();
         String sql = "SELECT R.NOMBREPERSONA \n"
                 + "   FROM  HVREFERENCIAS R \n"
                 + "   WHERE R.HOJADEVIDA = ? AND \n"
                 + "   R.TIPO = 'PERSONALES' \n"
                 + "   AND ROWNUM = 1";

         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secHV);
         referenciaP = (String) query.getSingleResult();
         if (referenciaP == null) {
            referenciaP = "";
         }
         return referenciaP;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("PersistenciaHvReferencias.primeraReferenciaPersonal() e:  ", e);
         } else {
            log.error("PersistenciaHvReferencias.primeraReferenciaPersonal() e:  ", e);
         }
         return "";
      }
   }

}
