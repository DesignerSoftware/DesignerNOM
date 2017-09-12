/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Perfiles;
import Entidades.PermisosObjetosDB;
import Entidades.PermisosPantallas;
import InterfaceAdministrar.AdministrarPerfilesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import InterfacePersistencia.PersistenciaPermisosObjetosDBInterface;
import InterfacePersistencia.PersistenciaPermisosPantallasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarPerfiles implements AdministrarPerfilesInterface {

   private static Logger log = Logger.getLogger(AdministrarPerfiles.class);

   @EJB
   PersistenciaPerfilesInterface persistenciaPerfiles;
   @EJB
   PersistenciaPermisosPantallasInterface persistenciaPermisosPantallas;
   @EJB
   PersistenciaPermisosObjetosDBInterface persistenciaPermisosDB;
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public void modificarPerfiles(List<Perfiles> listaPerfiles) {
      try {
         for (int i = 0; i < listaPerfiles.size(); i++) {
            persistenciaPerfiles.editar(getEm(), listaPerfiles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarPerfiles() ERROR: " + e);
      }
   }

   @Override
   public void borrarPerfiles(List<Perfiles> listaPerfiles) {
      try {
         for (int i = 0; i < listaPerfiles.size(); i++) {
            persistenciaPerfiles.borrar(getEm(), listaPerfiles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarPerfiles() ERROR: " + e);
      }
   }

   @Override
   public void crearPerfiles(List<Perfiles> listaPerfiles) {
      try {
         for (int i = 0; i < listaPerfiles.size(); i++) {
            persistenciaPerfiles.crear(getEm(), listaPerfiles.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearPerfiles() ERROR: " + e);
      }
   }

   @Override
   public List<Perfiles> consultarPerfiles() {
      try {
         return persistenciaPerfiles.consultarPerfilesAdmon(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarPerfiles() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<PermisosPantallas> consultarPermisosPantallas(BigInteger secPerfil) {
      try {
         return persistenciaPermisosPantallas.consultarPermisosPorPerfil(getEm(), secPerfil);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarPermisosPantallas() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearPermisoPantalla(List<PermisosPantallas> permisop) {
      try {
         for (int i = 0; i < permisop.size(); i++) {
            persistenciaPermisosPantallas.crear(getEm(), permisop.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearPermisoPantalla() ERROR: " + e);
      }
   }

   @Override
   public void editarPermisoPantalla(List<PermisosPantallas> permisop) {
      try {
         for (int i = 0; i < permisop.size(); i++) {
            persistenciaPermisosPantallas.editar(getEm(), permisop.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".editarPermisoPantalla() ERROR: " + e);
      }
   }

   @Override
   public void borrarPermisoPantalla(List<PermisosPantallas> permisop) {
      try {
         for (int i = 0; i < permisop.size(); i++) {
            persistenciaPermisosPantallas.borrar(getEm(), permisop.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarPermisoPantalla() ERROR: " + e);
      }
   }

   @Override
   public List<PermisosObjetosDB> consultarPermisosObjetos(BigInteger secPerfil) {
      try {
         return persistenciaPermisosDB.consultarPermisosPorPerfil(getEm(), secPerfil);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarPermisosObjetos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearPermisoObjeto(List<PermisosObjetosDB> permisosOb) {
      try {
         for (int i = 0; i < permisosOb.size(); i++) {
            persistenciaPermisosDB.crear(getEm(), permisosOb.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearPermisoObjeto() ERROR: " + e);
      }
   }

   @Override
   public void editarPermisoObjeto(List<PermisosObjetosDB> permisosOb) {
      try {
         for (int i = 0; i < permisosOb.size(); i++) {
            persistenciaPermisosDB.editar(getEm(), permisosOb.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".editarPermisoObjeto() ERROR: " + e);
      }
   }

   @Override
   public void borrarPermisoObjeto(List<PermisosObjetosDB> permisosOb) {
      try {
         for (int i = 0; i < permisosOb.size(); i++) {
            persistenciaPermisosDB.borrar(getEm(), permisosOb.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarPermisoObjeto() ERROR: " + e);
      }
   }

   @Override
   public void ejcutarPKGRecrearPerfil(String descripcion, String pwd) {
      try {
         persistenciaPerfiles.ejecutarPKGRecrearPerfil(getEm(), descripcion, pwd);
      } catch (Exception e) {
         log.warn("Error ejcutarPKGUbicarnuevointercon_total admi: " + e.getMessage());
      }
   }

   @Override
   public void ejcutarPKGEliminarPerfil(String descripcion) {
      try {
         persistenciaPerfiles.ejecutarPKGEliminarPerfil(getEm(), descripcion);
      } catch (Exception e) {
         log.warn("Error ejcutarPKGUbicarnuevointercon_total admi: " + e.getMessage());
      }
   }

   @Override
   public Perfiles consultarPerfilUsuario() {
      try {
         return persistenciaPerfiles.consultarPerfilPorUsuario(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarPerfilUsuario() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void clonarPantallas(String nomPerfil) {
      try {
         persistenciaPerfiles.clonarPantallas(getEm(), nomPerfil);
      } catch (Exception e) {
         log.warn("Error en administrar.clonarPantallas() : " + e.getMessage());
      }
   }

   @Override
   public void clonarPermisosObjetos(String nomPerfil) {
      try {
         persistenciaPerfiles.clonarPermisosObjetos(getEm(), nomPerfil);
      } catch (Exception e) {
         log.warn("Error en administrar.clonarPermisosObjetos() : " + e.getMessage());
      }
   }

}
