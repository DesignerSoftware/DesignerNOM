package Administrar;

import ClasesAyuda.SessionEntityManager;
import InterfaceAdministrar.AdministrarSesionesInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Felipe Triviño
 */
@Singleton
public class AdministrarSesiones implements AdministrarSesionesInterface, Serializable {

   private static Logger log = Logger.getLogger(AdministrarSesiones.class);

   private List<SessionEntityManager> sessionesActivas;

   public AdministrarSesiones() {
      try {
         sessionesActivas = new ArrayList<SessionEntityManager>();
      } catch (Exception e) {
         log.fatal("Administrar.AdministrarSesiones.<init>() ERROR: " + e);
      }
   }

   @Override
   public void adicionarSesion(SessionEntityManager session) {
      try {
         log.warn("Se adiciono la sesion: " + session.getIdSession());
         log.warn("El entityManagerFactory recibido: " + session.getEmf().toString());
         log.warn("El entityManager recibido: " + session.getEm().toString());
         sessionesActivas.add(session);
      } catch (Exception e) {
         log.fatal("Administrar.adicionarSesion() ERROR: " + e);
      }
   }

   @Override
   public void consultarSessionesActivas() {
      try {
         if (!sessionesActivas.isEmpty()) {
            for (int i = 0; i < sessionesActivas.size(); i++) {
               log.warn("Id Sesion: " + sessionesActivas.get(i).getIdSession() + " - Entity Manager " + sessionesActivas.get(i).getEm().toString());
            }
            log.warn("TOTAL SESIONES: " + sessionesActivas.size());
         }
      } catch (Exception e) {
         log.fatal("Administrar.consultarSessionesActivas() ERROR: " + e);
      }
   }

   @Override
   public EntityManager obtenerConexionSesion(String idSesion) {
      try {
         if (!sessionesActivas.isEmpty()) {
            for (SessionEntityManager sem : sessionesActivas) {
               if (sem.getIdSession().equals(idSesion)) {
                  log.warn(this.getClass().getName() + ".obtenerConexionSesion() Encontró la sesión: " + idSesion);
                  return sem.getEm();
               }
            }
         }
      } catch (Exception e) {
         log.fatal("Administrar.obtenerConexionSesion() ERROR: " + e);
      }
      return null;
   }

   @Override
   public void borrarSesion(String idSesion) {
      try {
         if (!sessionesActivas.isEmpty()) {
            for (int i = 0; i < sessionesActivas.size(); i++) {
               if (sessionesActivas.get(i).getIdSession().equals(idSesion)) {
                  sessionesActivas.remove(sessionesActivas.get(i));
                  i--;
               }
            }
         } else {
            log.fatal("No se encontraron sesiones activas.");
         }
      } catch (Exception e) {
         log.fatal("Administrar.borrarSesion() ERROR: " + e);
      }
   }

}
