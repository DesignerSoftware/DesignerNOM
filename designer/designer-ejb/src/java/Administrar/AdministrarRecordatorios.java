/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Recordatorios;
import InterfaceAdministrar.AdministrarRecordatoriosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRecordatoriosInterface;
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
public class AdministrarRecordatorios implements AdministrarRecordatoriosInterface {

   private static Logger log = Logger.getLogger(AdministrarRecordatorios.class);

   @EJB
   PersistenciaRecordatoriosInterface persistenciaRecordatorios;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
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

   public List<Recordatorios> recordatorios() {
      try {
         return persistenciaRecordatorios.proverbiosRecordatorios(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Recordatorios> mensajesRecordatorios() {
      try {
         return persistenciaRecordatorios.mensajesRecordatorios(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public void borrar(Recordatorios proverbios) {
      try {
         persistenciaRecordatorios.borrar(getEm(), proverbios);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crear(Recordatorios proverbios) {
      try {
         persistenciaRecordatorios.crear(getEm(), proverbios);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void modificar(List<Recordatorios> listaProverbiosModificar) {
      try {
         for (int i = 0; i < listaProverbiosModificar.size(); i++) {
            if (listaProverbiosModificar.get(i).getMensaje() == null) {
               listaProverbiosModificar.get(i).setMensaje(null);
            }
            persistenciaRecordatorios.editar(getEm(), listaProverbiosModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarMU(Recordatorios mensajeUsuario) {
      try {
         persistenciaRecordatorios.borrar(getEm(), mensajeUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearMU(Recordatorios mensajeUsuario) {
      try {
         persistenciaRecordatorios.crear(getEm(), mensajeUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarMU(List<Recordatorios> listaMensajesUsuariosModificar) {
      try {
         for (int i = 0; i < listaMensajesUsuariosModificar.size(); i++) {
            if (listaMensajesUsuariosModificar.get(i).getMensaje() == null) {
               listaMensajesUsuariosModificar.get(i).setMensaje(null);
            }
            if (listaMensajesUsuariosModificar.get(i).getAno() == null) {
               listaMensajesUsuariosModificar.get(i).setAno(null);
            }
            if (listaMensajesUsuariosModificar.get(i).getMes() == null) {
               listaMensajesUsuariosModificar.get(i).setMes(null);
            }
            if (listaMensajesUsuariosModificar.get(i).getDia() == null) {
               listaMensajesUsuariosModificar.get(i).setDia(null);
            }
            persistenciaRecordatorios.editar(getEm(), listaMensajesUsuariosModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }
}
