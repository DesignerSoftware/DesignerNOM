/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarUsuariosEstructurasInterface;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Usuarios;
import Entidades.UsuariosEstructuras;
import Entidades.UsuariosVistas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaUsuariosEstructurasInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import InterfacePersistencia.PersistenciaUsuariosVistasInterface;
import java.math.BigDecimal;
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
public class AdministrarUsuariosEstructuras implements AdministrarUsuariosEstructurasInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuariosEstructuras.class);

   @EJB
   PersistenciaUsuariosInterface persistenciaUsuarios;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaUsuariosEstructurasInterface persistenciaUsuariosEstructuras;
   @EJB
   PersistenciaUsuariosVistasInterface persistenciaUsuariosVistas;

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
   public List<UsuariosEstructuras> consultarUsuariosEstructuras(BigInteger secUsuario) {
      try {
         return persistenciaUsuariosEstructuras.consultarUsuariosEstructuras(getEm(), secUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearUsuarioEstructura(List<UsuariosEstructuras> listCrear) {
      try {
         for (int i = 0; i < listCrear.size(); i++) {
            persistenciaUsuariosEstructuras.crear(getEm(), listCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarUsuarioEstructura(List<UsuariosEstructuras> listModificar) {
      try {
         for (int i = 0; i < listModificar.size(); i++) {
            persistenciaUsuariosEstructuras.editar(getEm(), listModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarUsuarioEstructura(List<UsuariosEstructuras> listBorrar) {
      try {
         for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaUsuariosEstructuras.borrar(getEm(), listBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Usuarios> lovUsuarios() {
      try {
         return persistenciaUsuarios.buscarUsuarios(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Estructuras> lovEstructuras() {
      try {
         return persistenciaEstructuras.buscarEstructuras(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Empresas> lovEmpresas() {
      try {
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<UsuariosVistas> listaUsuariosVistas() {
      try {
         return persistenciaUsuariosVistas.buscarUsuariosVistas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearUsuarioVista(List<UsuariosVistas> listCrear) {
      try {
         for (int i = 0; i < listCrear.size(); i++) {
            persistenciaUsuariosVistas.crear(getEm(), listCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarUsuarioVista(List<UsuariosVistas> listModificar) {
      try {
         for (int i = 0; i < listModificar.size(); i++) {
            persistenciaUsuariosVistas.editar(getEm(), listModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarUsuarioVista(List<UsuariosVistas> listBorrar) {
      try {
         for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaUsuariosVistas.borrar(getEm(), listBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public BigDecimal contarUsuariosEstructuras(BigInteger secUsuario) {
      try {
         return persistenciaUsuariosEstructuras.contarUsuariosEstructuras(getEm(), secUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearVistaUsuarioEstructura(BigInteger secUsuarioEstructura, BigInteger secUsuario) {
      try {
         persistenciaUsuariosEstructuras.crearVistaUsuarioEstructura(getEm(), secUsuarioEstructura, secUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

}
