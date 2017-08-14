/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ObjetosJsf;
import Entidades.Perfiles;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaObjetosJsfInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;
import InterfaceAdministrar.AdministrarPermisosObjetosJsfInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarPermisosObjetosJsf implements AdministrarPermisosObjetosJsfInterface {

   private static Logger log = Logger.getLogger(AdministrarPermisosObjetosJsf.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaObjetosJsfInterface persistenciaObjetosJsf;
   @EJB
   PersistenciaPerfilesInterface persistenciaPerfiles;
   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<ObjetosJsf> consultarEnable(String nomPantalla) {
      try {
         String perfil = consultarPerfilUsuario();
         return persistenciaObjetosJsf.consultarEnableObjetoJsf(getEm(), perfil, nomPantalla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   private String consultarPerfilUsuario() {
      try {
         Perfiles perfil = persistenciaPerfiles.consultarPerfilPorUsuario(em);
         return perfil.getDescripcion();
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
