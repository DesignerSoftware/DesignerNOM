/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ObjetosDB;
import Entidades.UsuariosVistas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosVistasInterface;
import InterfacePersistencia.PersistenciaObjetosDBInterface;
import InterfacePersistencia.PersistenciaUsuariosVistasInterface;
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
public class AdministrarUsuariosVistas implements AdministrarUsuariosVistasInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuariosVistas.class);

   @EJB
   PersistenciaUsuariosVistasInterface persistenciaUsuariosVistas;
   @EJB
   PersistenciaObjetosDBInterface persistenciaObjetoDB;
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
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public List<UsuariosVistas> consultarUsuariosVistas() {
      try {
         return persistenciaUsuariosVistas.buscarUsuariosVistas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<ObjetosDB> consultarObjetosDB() {
      try {
         return persistenciaObjetoDB.consultarObjetoDB(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarUsuariosVistas(List<UsuariosVistas> listaUsuariosVistas) {
      try {
         for (int i = 0; i < listaUsuariosVistas.size(); i++) {
            if (listaUsuariosVistas.get(i).getObjetodb().getSecuencia() == null) {
               listaUsuariosVistas.get(i).setObjetodb(null);
            } else if (listaUsuariosVistas.get(i).getAlias() == null) {
               listaUsuariosVistas.get(i).setAlias(null);
            } else {
               persistenciaUsuariosVistas.editar(getEm(), listaUsuariosVistas.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarUsuariosVistas(List<UsuariosVistas> listaUsuariosVistas) {
      try {
         for (int i = 0; i < listaUsuariosVistas.size(); i++) {
            if (listaUsuariosVistas.get(i).getObjetodb().getSecuencia() == null) {
               listaUsuariosVistas.get(i).setObjetodb(null);
            } else if (listaUsuariosVistas.get(i).getAlias() == null) {
               listaUsuariosVistas.get(i).setAlias(null);
            } else {
               persistenciaUsuariosVistas.borrar(getEm(), listaUsuariosVistas.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearUsuariosVistas(List<UsuariosVistas> listaUsuariosVistas) {
      try {
         for (int i = 0; i < listaUsuariosVistas.size(); i++) {
            if (listaUsuariosVistas.get(i).getObjetodb().getSecuencia() == null) {
               listaUsuariosVistas.get(i).setObjetodb(null);
            } else if (listaUsuariosVistas.get(i).getAlias() == null) {
               listaUsuariosVistas.get(i).setAlias(null);
            } else {
               persistenciaUsuariosVistas.crear(getEm(), listaUsuariosVistas.get(i));
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Integer crearUsuarioVistaDB(BigInteger objeto) {
      try {
         return persistenciaUsuariosVistas.crearUsuarioVista(getEm(), objeto);
      } catch (Exception e) {
         log.warn("Error crearUsuarioVistaDB Admi : " + e.toString());
         return null;
      }
   }

}
