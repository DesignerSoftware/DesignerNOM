/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TercerosSucursales;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TercerosSucursales'
 * de la base de datos.
 *
 * @author AndresPineda
 */
@Stateless
public class PersistenciaTercerosSucursales implements PersistenciaTercerosSucursalesInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    *
    * @param em
    */
   /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
    */
   @Override
   public void crear(EntityManager em, TercerosSucursales tercerosSucursales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.persist(tercerosSucursales);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaTercerosSucursales.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, TercerosSucursales tercerosSucursales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(tercerosSucursales);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaTercerosSucursales.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, TercerosSucursales tercerosSucursales) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(tercerosSucursales));
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaTercerosSucursales.borrar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<TercerosSucursales> buscarTercerosSucursales(EntityManager em) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT TS.* FROM TERCEROSSUCURSALES TS, TERCEROS T, EMPRESAS E"
                 + " WHERE TS.TERCERO = T.SECUENCIA AND T.EMPRESA = E.SECUENCIA", TercerosSucursales.class);
         List<TercerosSucursales> tercerosSucursales = query.getResultList();
         return tercerosSucursales;
      } catch (Exception e) {
         System.out.println("Error buscarTercerosSucursales");
         return null;
      }
   }

   @Override
   public TercerosSucursales buscarTercerosSucursalesSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ts FROM TercerosSucursales ts WHERE ts.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         TercerosSucursales tercerosSucursales = (TercerosSucursales) query.getSingleResult();
         return tercerosSucursales;
      } catch (Exception e) {
         System.out.println("Error buscarTercerosSucursalesSecuencia");
         TercerosSucursales tercerosSucursales = null;
         return tercerosSucursales;
      }
   }

   @Override
   public List<TercerosSucursales> buscarTercerosSucursalesPorTerceroSecuencia(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ts FROM TercerosSucursales ts WHERE ts.tercero.secuencia = :secuenciaT");
         query.setParameter("secuenciaT", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TercerosSucursales> listTercerosS = query.getResultList();
         return listTercerosS;
      } catch (Exception e) {
         System.out.println("Error buscarTercerosSucursalesPorTerceroSecuencia PersistenciaTerceroSurcusal : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TercerosSucursales> buscarTercerosSucursalesPorEmpresa(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT ts FROM TercerosSucursales ts WHERE ts.tercero.empresa.secuencia = :secuencia");
         query.setParameter("secuencia", secuencia);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<TercerosSucursales> listTercerosS = query.getResultList();
         return listTercerosS;
      } catch (Exception e) {
         System.out.println("Error buscarTercerosSucursalesPorEmpresa PersistenciaTerceroSurcusal : " + e.toString());
         return null;
      }
   }
}
