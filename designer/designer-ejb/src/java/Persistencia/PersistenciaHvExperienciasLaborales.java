/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.HvExperienciasLaborales;
import InterfacePersistencia.PersistenciaHvExperienciasLaboralesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'HvExperienciasLaborales' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaHvExperienciasLaborales implements PersistenciaHvExperienciasLaboralesInterface {

   private static Logger log = Logger.getLogger(PersistenciaHvExperienciasLaborales.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, HvExperienciasLaborales experienciasLaborales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(experienciasLaborales);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaHistoriasformulas.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, HvExperienciasLaborales experienciasLaborales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(experienciasLaborales);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaHistoriasformulas.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, HvExperienciasLaborales experienciasLaborales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(experienciasLaborales));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaHistoriasformulas.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public HvExperienciasLaborales buscarHvExperienciaLaboral(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(HvExperienciasLaborales.class, secuencia);
      } catch (Exception e) {
         log.error("Error en la PersistenciaHvExperienciasLaborales ERROR :  ", e);
         return null;
      }
   }

   @Override
   public List<HvExperienciasLaborales> experienciaLaboralPersona(EntityManager em, BigInteger secuenciaHV) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(hve) FROM HvExperienciasLaborales hve WHERE hve.hojadevida.secuencia = :secuenciaHV");
         query.setParameter("secuenciaHV", secuenciaHV);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         if (resultado > 0) {
            Query queryFinal = em.createQuery("SELECT hve FROM HvExperienciasLaborales hve WHERE hve.hojadevida.secuencia = :secuenciaHV and hve.fechadesde = (SELECT MAX(hvex.fechadesde) FROM HvExperienciasLaborales hvex WHERE hvex.hojadevida.secuencia = :secuenciaHV)");
            queryFinal.setParameter("secuenciaHV", secuenciaHV);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<HvExperienciasLaborales> listaExperienciasLaborales = queryFinal.getResultList();
            return listaExperienciasLaborales;
         }
         return null;
      } catch (Exception e) {
         log.error("Error PersistenciaHvExperienciasLaborales.experienciaLaboralPersona ", e);
         return null;
      }
   }

   @Override
   public List<HvExperienciasLaborales> experienciasLaboralesSecuenciaEmpleado(EntityManager em, BigInteger secuenciaHv) {
      try {
         em.clear();
         Query queryFinal = em.createQuery("SELECT hve FROM HvExperienciasLaborales hve WHERE hve.hojadevida.secuencia = :secuenciaHV");
         queryFinal.setParameter("secuenciaHV", secuenciaHv);
         queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<HvExperienciasLaborales> listaExperienciasLaborales = queryFinal.getResultList();
         return listaExperienciasLaborales;
      } catch (Exception e) {
         log.error("Error experienciasLaboralesSecuenciaEmpleado PersistenciaHvExperienciasLaborales :  ", e);
         return null;
      }
   }

   @Override
   public String primeraExpLaboral(EntityManager em, BigInteger secuenciaHv) {
      String experiencia;
      try {
         em.clear();
         String sql = "SELECT SUBSTR(E.empresa||' '||TO_CHAR(E.fechadesde,'DD-MM-YYYY'),1,30)\n"
                 + "     FROM  HVEXPERIENCIASLABORALES E \n"
                 + "     WHERE E.HOJADEVIDA = ? \n"
                 + "     AND   E.fechadesde = (SELECT MAX(EI.FECHADESDE) FROM HVEXPERIENCIASLABORALES EI WHERE EI.hojadevida = E.HOJADEVIDA) AND ROWNUM = 1";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuenciaHv);
         experiencia = (String) query.getSingleResult();
         if (experiencia == null) {
            experiencia = "";
         }
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("PersistenciaHvExperienciasLaborales.primeraExpLaboral() e: " + e);
         } else {
            log.error("PersistenciaHvExperienciasLaborales.primeraExpLaboral() e:  ", e);
         }
         experiencia = "";
      }
      return experiencia;
   }
}
