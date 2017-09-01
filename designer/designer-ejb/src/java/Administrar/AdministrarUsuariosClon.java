/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Pantallas;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.Usuarios;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosClonInterface;
import InterfacePersistencia.PersistenciaPantallasInterface;
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
 * @author Administrador
 */
@Stateful
public class AdministrarUsuariosClon implements AdministrarUsuariosClonInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuariosClon.class);

   @EJB
   PersistenciaUsuariosInterface persistenciaUsuarios;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaPerfilesInterface persistenciaPerfiles;
   @EJB
   PersistenciaPantallasInterface persistenciaPantallas;
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

   // Metodos
   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public List<Usuarios> consultarUsuarios() {
      try {
         return persistenciaUsuarios.buscarUsuarios(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearUsuariosBD(String alias, String perfil) {
      try {
         persistenciaUsuarios.crearUsuario(getEm(), alias);
         persistenciaUsuarios.crearUsuarioPerfil(getEm(), alias, perfil);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void eliminarUsuariosBD(String alias) {
      try {
         persistenciaUsuarios.borrarUsuario(getEm(), alias);
         persistenciaUsuarios.borrarUsuarioTotal(getEm(), alias);
         log.warn("algo estarÃ¡ haciendo de eliminar");
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void clonarUsuariosBD(BigInteger usuarioOrigen, BigInteger usuarioDestino) {
      try {
         persistenciaUsuarios.clonarUsuario(getEm(), usuarioOrigen, usuarioDestino);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void desbloquearUsuariosBD(String alias) {
      try {
         persistenciaUsuarios.desbloquearUsuario(getEm(), alias);
         log.warn("estÃ¡ haciendo algo de desbloquear");
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void restaurarUsuariosBD(String alias, String fecha) {
      try {
         persistenciaUsuarios.restaurarUsuario(getEm(), alias, fecha);
         log.warn("estÃ¡ haciendo algo de restaurar");
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<Personas> consultarPersonas() {
      try {
         return persistenciaPersonas.consultarPersonas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Perfiles> consultarPerfiles() {
      try {
         return persistenciaPerfiles.consultarPerfiles(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Pantallas> consultarPantallas() {
      try {
         return persistenciaPantallas.buscarPantallas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarUsuarios(List<Usuarios> listaUsuarios) {
      try {
         for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getAlias().equals(null)) {
               listaUsuarios.get(i).setAlias(null);
               persistenciaUsuarios.editar(getEm(), listaUsuarios.get(i));
            } else if (listaUsuarios.get(i).getPersona().getSecuencia() == null) {
               listaUsuarios.get(i).setPersona(null);
            } else if (listaUsuarios.get(i).getPerfil().getSecuencia() == null) {
               listaUsuarios.get(i).setPerfil(null);
            } else if (listaUsuarios.get(i).getPantallainicio().getSecuencia() == null) {
               listaUsuarios.get(i).setPantallainicio(null);
            } else {
               persistenciaUsuarios.editar(getEm(), listaUsuarios.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarUsuarios(List<Usuarios> listaUsuarios) {
      try {
         for (int i = 0; i < listaUsuarios.size(); i++) {
            log.warn("Borrando..Usuarios.");
            if (listaUsuarios.get(i).getAlias().equals(null)) {
               listaUsuarios.get(i).setAlias(null);
               persistenciaUsuarios.borrar(getEm(), listaUsuarios.get(i));
            } else if (listaUsuarios.get(i).getPersona().getSecuencia() == null) {
               listaUsuarios.get(i).setPersona(null);
            } else if (listaUsuarios.get(i).getPerfil().getSecuencia() == null) {
               listaUsuarios.get(i).setPerfil(null);
            } else if (listaUsuarios.get(i).getPantallainicio().getSecuencia() == null) {
               listaUsuarios.get(i).setPantallainicio(null);
            } else {
               persistenciaUsuarios.borrar(getEm(), listaUsuarios.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearUsuarios(List<Usuarios> listaUsuarios) {
      try {
         for (int i = 0; i < listaUsuarios.size(); i++) {
            log.warn("Creando. Usuarios..");
            if (listaUsuarios.get(i).getAlias().equals(null)) {
               listaUsuarios.get(i).setAlias(null);
               persistenciaUsuarios.crear(getEm(), listaUsuarios.get(i));
            } else if (listaUsuarios.get(i).getPersona().getSecuencia() == null) {
               listaUsuarios.get(i).setPersona(null);
            } else if (listaUsuarios.get(i).getPerfil().getSecuencia() == null) {
               listaUsuarios.get(i).setPerfil(null);
            } else if (listaUsuarios.get(i).getPantallainicio().getSecuencia() == null) {
               listaUsuarios.get(i).setPantallainicio(null);
            } else {
               persistenciaUsuarios.crear(getEm(), listaUsuarios.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

}
