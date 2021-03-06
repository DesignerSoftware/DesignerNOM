/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasNormasEmpleados;
import InterfacePersistencia.PersistenciaVigenciasNormasEmpleadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'VigenciasNormasEmpleados' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasNormasEmpleados implements PersistenciaVigenciasNormasEmpleadosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasNormasEmpleados.class);

   @Override
   public boolean crear(EntityManager em, VigenciasNormasEmpleados vigenciasNormasEmpleados) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(vigenciasNormasEmpleados);
         tx.commit();
         return true;
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasNormasEmpleados.crear:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
         return false;
      }
   }

   @Override
   public void editar(EntityManager em, VigenciasNormasEmpleados vigenciasNormasEmpleados) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(vigenciasNormasEmpleados);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasNormasEmpleados.editar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, VigenciasNormasEmpleados vigenciasNormasEmpleados) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(vigenciasNormasEmpleados));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaVigenciasNormasEmpleados.borrar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }

   }

   @Override
   public VigenciasNormasEmpleados buscarVigenciasNormasEmpleado(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(VigenciasNormasEmpleados.class, secuencia);
      } catch (Exception e) {
          log.error("PersistenciaVigenciasNormasEmpleados.buscarVigenciasNormasEmpleado():  ", e);
         return null;
      }
   }

   @Override
   public List<VigenciasNormasEmpleados> buscarVigenciasNormasEmpleadosEmpl(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT vne FROM VigenciasNormasEmpleados vne WHERE vne.empleado.secuencia = :secuenciaEmpl ORDER BY vne.fechavigencia DESC");
         query.setParameter("secuenciaEmpl", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<VigenciasNormasEmpleados> vigenciasNormasEmpleados = query.getResultList();
         return vigenciasNormasEmpleados;
      } catch (Exception e) {
         log.error("Error en Persistencia Vigencias Normas Empleados  ", e);
         return null;
      }
   }

   @Override
   public List<VigenciasNormasEmpleados> buscarVigenciasNormasEmpleados(EntityManager em) {
      try{
       em.clear();
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      cq.select(cq.from(VigenciasNormasEmpleados.class));
      return em.createQuery(cq).getResultList();
      }catch(Exception e){
          log.error("PersistenciaVigenciasNormasEmpleados.buscarVigenciasNormasEmpleados():  ", e);
          return null;
      }
   }
}
