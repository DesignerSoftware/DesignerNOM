/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ActualUsuario;
import Entidades.CambiosMasivos;
import Entidades.ParametrosCambiosMasivos;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless. <br> Clase encargada de realizar operaciones sobre la tabla
 * 'ActualUsuario' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaActualUsuario implements PersistenciaActualUsuarioInterface {

   private static Logger log = Logger.getLogger(PersistenciaActualUsuario.class);

   //private final static Logger logger = Logger.getLogger("connectionSout");
   //private Date fechaDia;
   //private final SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos
    *
    * @param em
    */
   /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager em;
    */
   @Override
   public ActualUsuario actualUsuarioBD(EntityManager em) {
      try {
         em.clear();
         Query query = em.createQuery("SELECT au FROM ActualUsuario au");
         query.setHint("javax.persistence.cache.storeMode", "REFRESH");
         ActualUsuario actualUsuario;
         actualUsuario = (ActualUsuario) query.getSingleResult();
         return actualUsuario;
      } catch (Exception e) {
         log.fatal("Persistencia.PersistenciaActualUsuario.actualUsuarioBD() e: " + e);
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public String actualAliasBD(EntityManager em) {
      log.warn(this.getClass().getName() + ".actualAliasBD()");
      try {
         em.clear();
         String sqlQuery = "SELECT au.ALIAS FROM VWActualUsuario au";
         Query query = em.createNativeQuery(sqlQuery);
         String alias;
         alias = (String) query.getSingleResult();
         log.warn("alias: " + alias);
         return alias;
      } catch (Exception e) {
         log.fatal("persistenciaActualUsuario .actualAliasBD  :    Sali con errores");
         return null;
      }
   }

   public String actualAliasBD_EM(EntityManager emg) {
      try {
         emg.clear();
         String sqlQuery = "SELECT au.ALIAS FROM VWActualUsuario au";
         Query query = emg.createNativeQuery(sqlQuery);
         String alias;
         alias = (String) query.getSingleResult();
         return alias;
      } catch (Exception e) {
         log.fatal("Persistencia.PersistenciaActualUsuario.actualAliasBD_EM() e: " + e);
         return null;
      }
   }

   @Override
   public List<CambiosMasivos> consultarCambiosMasivos(EntityManager em) {
//      try {
      log.warn("Persistencia.PersistenciaActualUsuario.consultarCambiosMasivos()");
//         String q = "SELECT CM.* FROM CAMBIOSMASIVOS CM \n"
//                 + "WHERE EXISTS(SELECT 'X' FROM EMPLEADOS E WHERE E.SECUENCIA = CM.EMPLEADO)";
//         log.error("q : " + q);
//         Query query = em.createNativeQuery(q, CambiosMasivos.class);
//         List<CambiosMasivos> lista = query.getResultList();
//         return lista;
//      } catch (Exception e) {
//         log.error("Error PersistenciaCambiosMasivos.consultarCambiosMasivos: " + e);
      return null;
//      }
   }

   @Override
   public ParametrosCambiosMasivos consultarParametroCambiosMasivos(EntityManager em, String usuario) {
      //         em.clear();
//      try {
      log.warn("Persistencia.PersistenciaActualUsuario.consultarParametroCambiosMasivos()");
//         String q = "SELECT * FROM PARAMETROSCAMBIOSMASIVOS WHERE usuariobd = '" + usuario + "'";
//         Query query = em.createNativeQuery(q);
//         log.error("q : " + q);
//         ParametrosCambiosMasivos parametro = (ParametrosCambiosMasivos) query.getSingleResult();
//         return parametro;
//      } catch (Exception e) {
//         log.error("Error PersistenciaCambiosMasivos.consultarParametroCambiosMasivos: " + e);
      return null;
//      }
   }
}
