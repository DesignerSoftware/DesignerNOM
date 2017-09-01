/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.UsuariosTiposSueldos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosTiposSueldosInterface;
import InterfacePersistencia.PersistenciaUsuariosTiposSueldosInterface;
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
public class AdministrarUsuariosTiposSueldos implements AdministrarUsuariosTiposSueldosInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuariosTiposSueldos.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaUsuariosTiposSueldosInterface persistenciaUsuariosTiposSueldos;

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
   public List<UsuariosTiposSueldos> consultarUsuariosTS() {
      try {
         return persistenciaUsuariosTiposSueldos.buscarUsuariosTiposSueldos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarUsuariosTS(List<UsuariosTiposSueldos> listaUsuarios) {
      try {
         for (int i = 0; i < listaUsuarios.size(); i++) {
            persistenciaUsuariosTiposSueldos.editar(getEm(), listaUsuarios.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarUsuariosTS(List<UsuariosTiposSueldos> listaUsuarios) {
      try {
         for (int i = 0; i < listaUsuarios.size(); i++) {
            persistenciaUsuariosTiposSueldos.borrar(getEm(), listaUsuarios.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearUsuariosTS(List<UsuariosTiposSueldos> listaUsuarios) {
      try {
         for (int i = 0; i < listaUsuarios.size(); i++) {
            persistenciaUsuariosTiposSueldos.crear(getEm(), listaUsuarios.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

}
