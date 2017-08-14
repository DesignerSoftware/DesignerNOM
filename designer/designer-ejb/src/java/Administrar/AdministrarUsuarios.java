/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Pantallas;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.TiposDocumentos;
import Entidades.Usuarios;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaPantallasInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
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
public class AdministrarUsuarios implements AdministrarUsuariosInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuarios.class);

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
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   @EJB
   PersistenciaTiposDocumentosInterface persistenciaTipoDocumento;

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

   // Metodos
   @Override
   public void obtenerConexion(String idSesion) {
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
   public String crearUsuariosBD(String alias) {
      try {
         return persistenciaUsuarios.crearUsuario(getEm(), alias);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String CrearUsuarioPerfilBD(String alias, String perfil) {
      try {
         return persistenciaUsuarios.crearUsuarioPerfil(getEm(), alias, perfil);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String eliminarUsuariosBD(String alias) {
      try {
         return persistenciaUsuarios.borrarUsuario(getEm(), alias);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String eliminarUsuarioTotalBD(String alias) {
      try {
         return persistenciaUsuarios.borrarUsuarioTotal(getEm(), alias);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String clonarUsuariosBD(BigInteger usuarioOrigen, BigInteger usuarioDestino) {
      try {
         return persistenciaUsuarios.clonarUsuario(getEm(), usuarioOrigen, usuarioDestino);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String desbloquearUsuariosBD(String alias) {
      try {
         return persistenciaUsuarios.desbloquearUsuario(getEm(), alias);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String restaurarUsuariosBD(String alias, String fecha) {
      try {
         return persistenciaUsuarios.restaurarUsuario(getEm(), alias, fecha);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
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

   @Override
   public List<Ciudades> lovCiudades() {
      try {
         return persistenciaCiudades.lovCiudades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposDocumentos> consultarTiposDocumentos() {
      try {
         return persistenciaTipoDocumento.consultarTiposDocumentos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearPersona(Personas persona) {
      try {
         persistenciaPersonas.crear(getEm(), persona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

}
