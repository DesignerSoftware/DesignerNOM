/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.HistoricosUsuarios;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.Usuarios;
import InterfaceAdministrar.AdministrarHistoricosUsuariosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaHistoricosUsuariosInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
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
public class AdministrarHistoricosUsuarios implements AdministrarHistoricosUsuariosInterface {

   private static Logger log = Logger.getLogger(AdministrarHistoricosUsuarios.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaHistoricosUsuariosInterface persistenciaHistoricosUsuarios;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaUsuariosInterface persistenciaUsuarios;
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
   public List<HistoricosUsuarios> consultarHistoricosUsuarios(BigInteger secUsuario) {
      try {
         List<HistoricosUsuarios> listaHistoricos = persistenciaHistoricosUsuarios.buscarHistoricosUsuarios(getEm(), secUsuario);
         return listaHistoricos;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearHistoricosUsuarios(List<HistoricosUsuarios> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            if (listaCrear.get(i).getPerfil().getSecuencia() == null) {
               listaCrear.get(i).setPerfil(new Perfiles());
            }
            if (listaCrear.get(i).getUsuario().getSecuencia() == null) {
               listaCrear.get(i).setUsuario(new Usuarios());
            }
            if (listaCrear.get(i).getPersona().getSecuencia() == null) {
               listaCrear.get(i).setPersona(new Personas());
            }
            persistenciaHistoricosUsuarios.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarHistoricosUsuarios(List<HistoricosUsuarios> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            if (listaModificar.get(i).getPerfil().getSecuencia() == null) {
               listaModificar.get(i).setPerfil(new Perfiles());
            }
            if (listaModificar.get(i).getUsuario().getSecuencia() == null) {
               listaModificar.get(i).setUsuario(new Usuarios());
            }
            if (listaModificar.get(i).getPersona().getSecuencia() == null) {
               listaModificar.get(i).setPersona(new Personas());
            }
            persistenciaHistoricosUsuarios.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarHistoricosUsuarios(List<HistoricosUsuarios> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            if (listaBorrar.get(i).getPerfil().getSecuencia() == null) {
               listaBorrar.get(i).setPerfil(new Perfiles());
            }
            if (listaBorrar.get(i).getUsuario().getSecuencia() == null) {
               listaBorrar.get(i).setUsuario(new Usuarios());
            }
            if (listaBorrar.get(i).getPersona().getSecuencia() == null) {
               listaBorrar.get(i).setPersona(new Personas());
            }
            persistenciaHistoricosUsuarios.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Personas> lovPersonas() {
      try {
         return persistenciaPersonas.consultarPersonas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Perfiles> lovPerfiles() {
      try {
         return persistenciaPerfiles.consultarPerfiles(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Usuarios> lovUsuarios(BigInteger secUsuario) {
      try {
         return persistenciaUsuarios.buscarUsuariosXSecuencia(getEm(), secUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
