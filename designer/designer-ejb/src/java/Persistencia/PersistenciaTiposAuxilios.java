/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaTiposAuxiliosInterface;
import Entidades.TiposAuxilios;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposAuxilios' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposAuxilios implements PersistenciaTiposAuxiliosInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposAuxilios.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
    */
   @Override
   public void crear(EntityManager em, TiposAuxilios tiposAuxilios) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposAuxilios);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposAuxilios.crear: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TiposAuxilios tiposAuxilios) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tiposAuxilios);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposAuxilios.editar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TiposAuxilios tiposAuxilios) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tiposAuxilios));
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaTiposAuxilios.borrar: " + e.getMessage());
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public TiposAuxilios buscarTipoAuxilio(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(TiposAuxilios.class, secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<TiposAuxilios> buscarTiposAuxilios(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT m FROM TiposAuxilios m ORDER BY m.codigo ASC");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TiposAuxilios> listaMotivosEmbargos = query.getResultList();
         return listaMotivosEmbargos;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaTiposAuxilios.buscarTiposAuxilios()" + e.getMessage());
         return null;
      }
   }

   @Override
   public BigInteger contadorTablasAuxilios(EntityManager em, BigInteger secuencia) {
      BigInteger retorno;
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*)FROM tablasauxilios ta WHERE ta.tipoauxilio = ? ";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         return retorno;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaTiposAuxilios.contadorTablasAuxilios()" + e.getMessage());
         retorno = new BigInteger("-1");
         return retorno;
      }
   }
}
