package Administrar;

import ClasesAyuda.SessionEntityManager;
import InterfaceAdministrar.AdministrarSesionesInterface;
import Persistencia.SesionEntityManagerFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Singleton;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Felipe Triviño
 */
@Singleton
public class AdministrarSesiones implements AdministrarSesionesInterface, Serializable {

   private static Logger log = Logger.getLogger(AdministrarSesiones.class);

   private List<SessionEntityManager> sessionesActivas;

   Map<String, String> datosConexiones = new HashMap<String, String>();

   public AdministrarSesiones() {
      try {
         sessionesActivas = new ArrayList<SessionEntityManager>();
      } catch (Exception e) {
         log.fatal("AdministrarSesiones.<init>() ERROR: " + e);
      }
   }

   @Override
   public void adicionarSesion(SessionEntityManager session) {
      try {
         log.warn("Se adiciono la sesion: " + session.getIdSession());
         log.warn("El entityManagerFactory recibido: " + session.getEmf().toString());
//         log.warn("El entityManager recibido: " + session.getEm().toString());
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
               log.warn("Id Sesion: " + sessionesActivas.get(i).getIdSession() + " - Entity Manager Factory" + sessionesActivas.get(i).getEmf().toString());
            }
            log.warn("TOTAL SESIONES: " + sessionesActivas.size());
         }
      } catch (Exception e) {
         log.fatal("Administrar.consultarSessionesActivas() ERROR: " + e);
      }
   }

   @Override
   public EntityManagerFactory obtenerConexionSesionEMF(String idSesion) {
      try {
         log.warn("AdministrarSesiones.obtenerConexionSesion() sessionesActivas: " + sessionesActivas);
         if (!sessionesActivas.isEmpty()) {
            for (SessionEntityManager sem : sessionesActivas) {
               if (sem.getIdSession().equals(idSesion)) {
                  log.warn("AdministrarSesiones.obtenerConexionSesion() Encontró la sesión: " + idSesion);
                  if (sem.getEmf() == null || !sem.getEmf().isOpen()) {
                     sem = null;
                     SesionEntityManagerFactory semf = new SesionEntityManagerFactory();
                     log.fatal("XX -- RECONECTANDO CON LA UNIDAD DE PERSISTENCIA -- XX");
                     if (semf.crearFactoryUsuario(datosConexiones.get("U_" + idSesion), datosConexiones.get("C_" + idSesion), datosConexiones.get("B_" + idSesion))) {
                        sessionesActivas.remove(sem);
                        sessionesActivas.add(new SessionEntityManager(idSesion, semf.getEmf()));
                        return semf.getEmf();
                     }
                  }
                  return sem.getEmf();
               }
            }
         }
      } catch (Exception e) {
         log.fatal("Administrar.obtenerConexionSesion() ERROR: " + e);
      }
      return null;
   }

   @Override
   public SessionEntityManager obtenerSesionEMF(String idSesion) {
      try {
         if (!sessionesActivas.isEmpty()) {
            for (SessionEntityManager sem : sessionesActivas) {
               if (sem.getIdSession().equals(idSesion)) {
                  log.warn("AdministrarSesiones.obtenerSesionEMF() Encontró la sesión: " + idSesion);
                  return sem;
               }
            }
         }
      } catch (Exception e) {
         log.fatal("Administrar.obtenerSesionEMF() ERROR: " + e);
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
            log.fatal("AdministrarSesiones.borrarSesion() No se encontraron sesiones activas.");
         }
      } catch (Exception e) {
         log.fatal("Administrar.borrarSesion() ERROR: " + e);
      }
   }

   @Override
   public void guardarDatosConexion(String baseDatos, String usuario, String contrasena, String idSesion) {
      try {
         datosConexiones.put("U_" + idSesion, usuario);
         datosConexiones.put("B_" + idSesion, baseDatos);
         datosConexiones.put("C_" + idSesion, contrasena);
         log.warn("AdministrarSesiones.guardarDatosConexion() usuario: " + usuario + ", baseDatos: " + baseDatos + " y contraseña: " + contrasena);
      } catch (Exception e) {
         log.fatal("Error AdministrarSesiones.guardarDatosConexion(): " + e);
      }
   }

   @Override
   public boolean reconectarUsuario(SessionEntityManager sem) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
}
