/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaGruposTiposEntidadesInterface;
import Entidades.Grupostiposentidades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'GruposTiposEntidades'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaGruposTiposEntidades implements PersistenciaGruposTiposEntidadesInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    */
   /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     EntityManager em;*/
   @Override
   public void crear(EntityManager em, Grupostiposentidades gruposTiposEntidades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(gruposTiposEntidades);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaGruposTiposEntidades.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void editar(EntityManager em, Grupostiposentidades gruposTiposEntidades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(gruposTiposEntidades);
         tx.commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaGruposTiposEntidades.editar: " + e);
      }
   }

   @Override
   public void borrar(EntityManager em, Grupostiposentidades gruposTiposEntidades) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(gruposTiposEntidades));
         tx.commit();
         em.getTransaction().commit();
      } catch (Exception e) {
         if (tx.isActive()) {
            tx.rollback();
         }
         System.out.println("Error PersistenciaGruposTiposEntidades.borrar: " + e);
      }
   }

   @Override
   public Grupostiposentidades consultarGrupoTipoEntidad(EntityManager em, BigInteger secuencia) {
      try {
         em.clear();
         return em.find(Grupostiposentidades.class, secuencia);
      } catch (Exception e) {
         System.out.println("\n ERROR EN PersistenciaGruposTiposEntidades buscarGrupoTipoEntidad ERROR " + e);

         return null;
      }
   }

   @Override
   public List<Grupostiposentidades> consultarGruposTiposEntidades(EntityManager em) {
      try {
         em.clear();
         String sql = "SELECT * FROM GRUPOSTIPOSENTIDADES ORDER BY CODIGO";
         Query query = em.createNativeQuery(sql, Grupostiposentidades.class);
         List<Grupostiposentidades> todosGrupostiposentidades = query.getResultList();
         return todosGrupostiposentidades;
      } catch (Exception e) {
         System.err.println("Error: PersistenciaGrupostiposentidades consultarGrupostiposentidades ERROR " + e);
         return null;
      }
   }

   public BigInteger contarTiposEntidadesGrupoTipoEntidad(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM tiposentidades WHERE grupo =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         System.out.println("Contador PersistenciaGrupostiposentidades contarTiposEntidadesGrupoTipoEntidad Retorno " + retorno);
         return retorno;
      } catch (Exception e) {
         System.err.println("Error PersistenciaGrupostiposentidades contarTiposEntidadesGrupoTipoEntidad ERROR : " + e);
         return retorno;
      }
   }

   public BigInteger contarTSgruposTiposEntidadesTipoEntidad(EntityManager em, BigInteger secuencia) {
      BigInteger retorno = new BigInteger("-1");
      try {
         em.clear();
         String sqlQuery = "SELECT COUNT(*) FROM tsgrupostiposentidades WHERE grupotipoentidad =?";
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, secuencia);
         retorno = new BigInteger(query.getSingleResult().toString());
         System.out.println("Contador PersistenciaGrupostiposentidades contarTSgruposTiposEntidadesTipoEntidad Retorno " + retorno);
         return retorno;
      } catch (Exception e) {
         System.err.println("Error PersistenciaGrupostiposentidades contarTSgruposTiposEntidadesTipoEntidad ERROR : " + e);
         return retorno;
      }
   }

}
