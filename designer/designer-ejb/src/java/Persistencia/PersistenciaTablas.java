/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Tablas;
import InterfacePersistencia.PersistenciaTablasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Stateless
public class PersistenciaTablas implements PersistenciaTablasInterface {

   public List<Tablas> consultarTablas(EntityManager em) {
      try {
         em.clear();
         Query query = em.createNativeQuery("SELECT * FROM Tablas t where EXISTS (SELECT p.secuencia FROM Pantallas p where t.secuencia = p.tabla) ORDER BY t.nombre", Tablas.class);
         List<Tablas> lista = query.getResultList();
         return lista;
      } catch (Exception e) {
         System.out.println("ERROR: PersistenciaTablas.consultarTablas() Error: " + e);
         return null;
      }
   }

   @Override
   public List<Tablas> buscarTablas(EntityManager em, BigInteger secuenciaMod) {
      //System.out.println("PersistenciaTablas.buscarTablas() secuenciaMod : " + secuenciaMod);
      try {
         em.clear();
         Query query = em.createQuery("select t from Tablas t where t.modulo.secuencia = :secuenciaMod "
                 + " and EXISTS (SELECT p FROM Pantallas p where t = p.tabla)"
                 + " and t.tipo in ('SISTEMA','CONFIGURACION') "
                 + "order by t.nombre");
         query.setParameter("secuenciaMod", secuenciaMod);
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         //System.out.println("PersistenciaTablas.buscarTablas() query : " + query);
         List<Tablas> tablas = (List<Tablas>) query.getResultList();
         return tablas;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaTablas.buscarTablas(): " + e);
         return null;
      }
   }
}
