/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasFormales;
import InterfacePersistencia.PersistenciaVigenciasFormalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasFormales' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasFormales implements PersistenciaVigenciasFormalesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasFormales.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    *
    * @param em
    * @param vigenciasFormales
    */
   /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
    */
   @Override
   public void crear(EntityManager em, VigenciasFormales vigenciasFormales) {
      log.warn(this.getClass().getName() + ".crear()");
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasFormales);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasFormales.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasFormales vigenciasFormales) {
      log.warn(this.getClass().getName() + ".editar()");
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasFormales);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasFormales.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasFormales vigenciasFormales) {
      log.warn(this.getClass().getName() + ".borrar()");
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasFormales));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasFormales.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<VigenciasFormales> buscarVigenciasFormales(EntityManager em) {
      log.warn(this.getClass().getName() + ".buscarVigenciasFormales()");
      try {
         em.clear();
         javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
         cq.select(cq.from(VigenciasFormales.class));
         return em.createQuery(cq).getResultList();
      } catch (Exception e) {
         log.error("error en buscarVigenciasFormales");
         return null;
      }
   }

   private Long contarEducacionPersona(EntityManager em, BigInteger secuencia) {
      log.warn(this.getClass().getName() + ".contarEducacionPersona()");
      Long resultado = null;
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(vf) FROM VigenciasFormales vf WHERE vf.persona.secuencia = :secuenciaPersona");
         query.setParameter("secuenciaPersona", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         resultado = (Long) query.getSingleResult();
         return resultado;
      } catch (Exception e) {
         log.error("PersistenciaVigenciasFormales.contarEducacionPersona():  ", e);
         e.printStackTrace();
         return resultado;
      }
   }

   @Override
   public List<VigenciasFormales> educacionPersona(EntityManager em, BigInteger secuencia) {
      Long resultado = this.contarEducacionPersona(em, secuencia);
      if (resultado > 0) {
         try {
            Query queryFinal = em.createQuery("SELECT vf FROM VigenciasFormales vf WHERE vf.persona.secuencia = :secuenciaPersona and vf.fechavigencia = (SELECT MAX(vfo.fechavigencia) FROM VigenciasFormales vfo WHERE vfo.persona.secuencia = :secuenciaPersona)");
            queryFinal.setParameter("secuenciaPersona", secuencia);
            List<VigenciasFormales> listaVigenciasFormales = queryFinal.getResultList();
            return listaVigenciasFormales;
         } catch (Exception e) {
            log.error("Error PersistenciaVigenciasFormales.educacionPersona ", e);
            return null;
         }
      } else {
         return null;
      }
   }

   @Override
   public List<VigenciasFormales> vigenciasFormalesPersona(EntityManager em, BigInteger secuencia) {
      log.warn(this.getClass().getName() + ".vigenciasFormalesPersona()");
      try {
         em.clear();
         Query query = em.createQuery("SELECT vF FROM VigenciasFormales vF WHERE vF.persona.secuencia = :secuenciaPersona ORDER BY vF.fechavigencia DESC");
         query.setParameter("secuenciaPersona", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasFormales> listaVigenciasFormales = query.getResultList();
         return listaVigenciasFormales;
      } catch (Exception e) {
         log.error("Error PersistenciaTelefonos.telefonoPersona ", e);
         return null;
      }
   }

   @Override
   public String primeraVigenciaFormal(EntityManager em, BigInteger secuencia) {
      String educacion;
      try {
         em.clear();
         String sql = "SELECT SUBSTR(TE.NOMBRE||' '||TO_CHAR(V.FECHAVIGENCIA,'DD-MM-YYYY'),1,30)\n"
                 + "   FROM vigenciasformales V,TIPOSEDUCACIONES TE\n"
                 + "   WHERE V.persona = ? \n"
                 + "   AND   V.tipoeducacion = TE.secuencia\n"
                 + "   AND V.FECHAVIGENCIA=(SELECT MAX(A.FECHAVIGENCIA) FROM vigenciasformales A WHERE A.PERSONA = V.PERSONA) AND ROWNUM = 1 \n"
                 + "   and rownum=1";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secuencia);
         educacion = (String) query.getSingleResult();
         if (educacion == null) {
            educacion = "";
         }
         return educacion;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("PersistenciaVigenciasFormales.primeraVigenciaFormal(): " + e);
         } else {
            log.error("PersistenciaVigenciasFormales.primeraVigenciaFormal():  ", e);
         }
         return "";
      }
   }
}
