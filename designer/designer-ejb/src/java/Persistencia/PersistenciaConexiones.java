/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Conexiones;
import InterfacePersistencia.PersistenciaConexionesInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar las operaciones para recuperar información de la
 * conexión que esta guardada en la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaConexiones implements PersistenciaConexionesInterface {

   private static Logger log = Logger.getLogger(PersistenciaConexiones.class);

   @Override
   public void crear_Modificar(Conexiones conexion, EntityManager em) {
      em.clear();
      EntityTransaction tx = em.getTransaction();
      try {
         tx.begin();
         em.merge(conexion);
         tx.commit();
      } catch (Exception e) {
         log.error("Error PersistenciaConexiones.crear_modificar:  ", e);
         if (tx.isActive()) {
            tx.rollback();
         }
      }
   }

   @Override
   public BigInteger buscarSID(EntityManager em) {
      try {
         String sqlQuery = "select sys_context('USERENV','SID') FROM DUAL";
         Query query = em.createNativeQuery(sqlQuery);
         String strSID = (String) query.getSingleResult();
         BigInteger SID = new BigInteger(strSID);
         return SID;
      } catch (Exception e) {
         log.error("Error PersistenciaConexiones.buscarSID:  ", e);
         return null;
      }
   }

   @Override
   public void verificarSID(EntityManager em, Conexiones conexion) {
      try {
         BigInteger SID = buscarSID(em);
         Query query = em.createQuery("SELECT COUNT(c) FROM Conexiones c WHERE c.sid = :SID");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         query.setParameter("SID", SID);
         Long conteo = (Long) query.getSingleResult();
         conexion.setSid(SID);
         if (conteo == 0) {
            crear_Modificar(conexion, em);
         } else {
            query = em.createQuery("SELECT c.secuencia FROM Conexiones c WHERE c.sid = :SID");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("SID", SID);
            BigInteger secuenciaConexion = (BigInteger) query.getSingleResult();
            conexion.setSecuencia(secuenciaConexion);
            crear_Modificar(conexion, em);
         }
      } catch (Exception e) {
         log.error("Error PersistenciaConexiones.verificarSID:  ", e);
      }
   }
}
