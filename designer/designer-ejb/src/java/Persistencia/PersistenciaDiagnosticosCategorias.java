/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Diagnosticoscapitulos;
import Entidades.Diagnosticoscategorias;
import Entidades.Diagnosticossecciones;
import InterfacePersistencia.PersistenciaDiagnosticosCategoriasInterface;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'DiagnosticosCategorias' de la base de datos.
 *
 * @author Viktor
 */
@Stateless
public class PersistenciaDiagnosticosCategorias implements PersistenciaDiagnosticosCategoriasInterface {

   private static Logger log = Logger.getLogger(PersistenciaDiagnosticosCategorias.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    *
    * @param em
    * @param diagnosticosCategorias
    */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
   @Override
   public void crear(EntityManager em, Diagnosticoscategorias diagnosticosCategorias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(diagnosticosCategorias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDiagnosticosCategorias.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Diagnosticoscategorias diagnosticosCategorias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(diagnosticosCategorias);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDiagnosticosCategorias.editar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Diagnosticoscategorias diagnosticosCategorias) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(diagnosticosCategorias));
         tx.commit();

      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         log.error("Error PersistenciaDiagnosticosCategorias.borrar: " + e);
      }
   }

   @Override
   public List<Diagnosticoscategorias> buscarDiagnosticos(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM DIAGNOSTICOSCATEGORIAS";
         Query query = em.createNativeQuery(sql, Diagnosticoscategorias.class);
         List<Diagnosticoscategorias> diagnosticosCategorias = query.getResultList();
         return diagnosticosCategorias;
      } catch (Exception e) {
         log.error("Error en PersistenciaDiagnosticosCategorias buscarDiagnosticos ERROR : " + e.getMessage().toString());
         return null;
      }
   }

   @Override
   public List<Diagnosticoscategorias> buscarCategorias(EntityManager em, BigDecimal secSeccion) {
      try {
         em.clear();
         String sql = "SELECT * FROM DIAGNOSTICOSCATEGORIAS WHERE SECCION=?";
         Query query = em.createNativeQuery(sql, Diagnosticoscategorias.class);
         query.setParameter(1, secSeccion);
         List<Diagnosticoscategorias> diagnosticosCategorias = query.getResultList();
         return diagnosticosCategorias;
      } catch (Exception e) {
         log.error("Error en PersistenciaDiagnosticosCategorias buscarDiagnosticos ERROR : " + e.getMessage().toString());
         return null;
      }
   }

   @Override
   public List<Diagnosticoscapitulos> buscarCapitulo(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM DIAGNOSTICOSCAPITULOS";
         Query query = em.createNativeQuery(sql, Diagnosticoscapitulos.class);
         List<Diagnosticoscapitulos> diagnosticosCapitulos = query.getResultList();
         return diagnosticosCapitulos;
      } catch (Exception e) {
         log.error("Error en PersistenciaDiagnosticosCategorias buscarCapitulo ERROR : " + e.getMessage().toString());
         return null;
      }
   }

   @Override
   public void crearCapitulo(EntityManager em, Diagnosticoscapitulos diagnosticosCapitulo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(diagnosticosCapitulo);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDiagnosticosCategorias.crearCapitulo: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editarCapitulo(EntityManager em, Diagnosticoscapitulos diagnosticosCapitulo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(diagnosticosCapitulo);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDiagnosticosCategorias.editarCapitulo: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrarCapitulo(EntityManager em, Diagnosticoscapitulos diagnosticosCapitulo) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(diagnosticosCapitulo));
         tx.commit();

      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            log.error("Error PersistenciaDiagnosticosCategorias.borrarCapitulo: " + e);
         }
      }
   }

   @Override
   public List<Diagnosticossecciones> buscarSeccion(EntityManager em, BigDecimal secCapitulo) {
      try {
         em.clear();
         String sql = "SELECT * FROM DIAGNOSTICOSSECCIONES WHERE CAPITULO = ? ORDER BY CODIGO ASC";
         Query query = em.createNativeQuery(sql, Diagnosticossecciones.class);
         query.setParameter(1, secCapitulo);
         List<Diagnosticossecciones> diagnosticosSecciones = query.getResultList();
         return diagnosticosSecciones;
      } catch (Exception e) {
         log.error("Error en PersistenciaDiagnosticosCategorias buscarSección ERROR : " + e.getMessage().toString());
         return null;
      }
   }

   @Override
   public void crearSeccion(EntityManager em, Diagnosticossecciones diagnosticosSeccion) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(diagnosticosSeccion);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDiagnosticosCategorias.crearSección: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editarSeccion(EntityManager em, Diagnosticossecciones diagnosticosSeccion) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(diagnosticosSeccion);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaDiagnosticosCategorias.editarSección: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrarSeccion(EntityManager em, Diagnosticossecciones diagnosticosSeccion) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(diagnosticosSeccion));
         tx.commit();

      } catch (Exception e) {
         try {
            if (tx.isActive()) {
               tx.rollback();
            }
         } catch (Exception ex) {
            log.error("Error PersistenciaDiagnosticosCategorias.borrarSección: " + e);
         }
      }
   }
}
