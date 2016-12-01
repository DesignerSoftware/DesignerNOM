/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Parametros;
import InterfacePersistencia.PersistenciaParametrosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Parametros' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaParametros implements PersistenciaParametrosInterface {

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
   @Override
   public void crear(EntityManager em, Parametros parametro) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(parametro);
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaParametros.crear: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public void borrar(EntityManager em, Parametros parametro) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.remove(em.merge(parametro));
         tx.commit();
      } catch (Exception e) {
         System.out.println("Error PersistenciaMotivosCambiosSueldos.borrar: " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public List<Parametros> parametrosComprobantes(EntityManager em, String usuarioBD) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT p FROM Parametros p WHERE EXISTS (SELECT pi FROM ParametrosInstancias pi, UsuariosInstancias ui WHERE pi.instancia.secuencia = ui.instancia.secuencia AND ui.usuario.alias = :usuarioBD AND pi.parametro.secuencia = p.secuencia) ORDER BY p.empleado.codigoempleado");
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         List<Parametros> listaParametros = query.getResultList();
         System.out.println("PersistenciaParametros ListaParametros: " + listaParametros);
         return listaParametros;
      } catch (Exception e) {
         System.out.println("Exepcion en PersistenciaParametros.parametrosComprobantes" + e);
         return null;
      }
   }

   @Override
   public List<Parametros> empleadosParametros(EntityManager em, String usuarioBD) {
      try {
         System.out.println("Persistencia.PersistenciaParametros.empleadosParametros() usuarioBD: " + usuarioBD + ", em: " + em);
         em.clear();
         Query query = em.createQuery("SELECT p FROM Parametros p WHERE p.empleado IS NOT NULL AND p.usuario.alias = :usuarioBD", Parametros.class);
         query.setParameter("usuarioBD", usuarioBD);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         System.out.println("query: " + query);
         List<Parametros> listaParametros = query.getResultList();
         if(listaParametros != null){
            System.out.println("listaParametros.size() : " + listaParametros.size());
         }
         return listaParametros;
      } catch (Exception e) {
         System.out.println("Exepcion en PersistenciaParametros.empleadosParametros" + e);
         return null;
      }
   }

   @Override
   public void borrarParametros(EntityManager em, BigInteger secParametrosEstructuras) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         Query query = em.createQuery("DELETE FROM Parametros p WHERE p.parametroestructura.secuencia = :secParametrosEstructuras");
         query.setParameter("secParametrosEstructuras", secParametrosEstructuras);
         query.executeUpdate();
         tx.commit();
      } catch (Exception e) {
         System.out.println("PersistenciaParametros.borrarParametros. " + e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }
}
