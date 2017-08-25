/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasEventos;
import InterfacePersistencia.PersistenciaVigenciasEventosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasEventos' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasEventos implements PersistenciaVigenciasEventosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasEventos.class);

   @Override
   public void crear(EntityManager em, VigenciasEventos vigenciasEventos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(vigenciasEventos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasEventos.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasEventos vigenciasEventos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasEventos);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasEventos.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasEventos vigenciasEventos) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasEventos));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasEventos.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<VigenciasEventos> eventosEmpleado(EntityManager em, BigInteger secuenciaEmpl) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT COUNT(ve) FROM VigenciasEventos ve WHERE ve.empleado.secuencia = :secuenciaEmpl");
         query.setParameter("secuenciaEmpl", secuenciaEmpl);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         Long resultado = (Long) query.getSingleResult();
         if (resultado > 0) {
            Query queryFinal = em.createQuery("SELECT ve FROM VigenciasEventos ve WHERE ve.empleado.secuencia = :secuenciaEmpl and ve.fechainicial = (SELECT MAX(vev.fechainicial) FROM VigenciasEventos vev WHERE vev.empleado.secuencia = :secuenciaEmpl)");
            queryFinal.setParameter("secuenciaEmpl", secuenciaEmpl);
            List<VigenciasEventos> listaVigenciasEventos = queryFinal.getResultList();
            return listaVigenciasEventos;
         }
         return null;
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasEventos.eventosPersona" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<VigenciasEventos> vigenciasEventosSecuenciaEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ve FROM VigenciasEventos ve WHERE ve.empleado.secuencia = :secuenciaEmpl");
         query.setParameter("secuenciaEmpl", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasEventos> resultado = (List<VigenciasEventos>) query.getResultList();
         return resultado;
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasEventos.vigenciasEventosSecuenciaEmpleado" + e.getMessage());
         return null;
      }
   }

   @Override
   public String primerEvento(EntityManager em, BigInteger secPersona) {
      String evento;
      try {
         em.clear();
         String sql = "SELECT SUBSTR(B.DESCRIPCION ||' '|| TO_CHAR(FECHAINICIAL,'DD-MM-YYYY'),1,30)\n"
                 + "    FROM vigenciaseventos A, EVENTOS B\n"
                 + "    WHERE A.empleado = (select secuencia from empleados where persona=?) \n"
                 + "    AND A.evento = B.secuencia\n"
                 + "    AND A.FECHAINICIAL = (SELECT MAX(V.FECHAINICIAL) FROM vigenciaseventos  V WHERE V.EMPLEADO = A.EMPLEADO) AND ROWNUM = 1";
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secPersona);
         evento = (String) query.getSingleResult();
         if (evento == null) {
            evento = "";
         }
         return evento;
      } catch (Exception e) {
         if (e.getMessage().contains("did not retrieve any entities")) {
            log.trace("Persistencia.PersistenciaVigenciasEventos.primerEvento()" + e.getMessage());
         } else {
            log.error("Persistencia.PersistenciaVigenciasEventos.primerEvento()" + e.getMessage());
         }
         return "";
      }
   }
}
