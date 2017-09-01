/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Inforeportes;
import Entidades.Usuarios;
import Entidades.UsuariosInforeportes;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosInfoReportesInterface;
import InterfacePersistencia.PersistenciaUsuariosInfoReportesInterface;
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
public class AdministrarUsuariosInfoReportes implements AdministrarUsuariosInfoReportesInterface {

   private static Logger log = Logger.getLogger(AdministrarUsuariosInfoReportes.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaUsuariosInfoReportesInterface persistenciaUsuariosIR;

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
   public void crear(List<UsuariosInforeportes> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaUsuariosIR.crear(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void editar(List<UsuariosInforeportes> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaUsuariosIR.editar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrar(List<UsuariosInforeportes> lista) {
      try {
         for (int i = 0; i < lista.size(); i++) {
            persistenciaUsuariosIR.borrar(getEm(), lista.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<UsuariosInforeportes> listaUsuariosIR(BigInteger secUsuario) {
      try {
         return persistenciaUsuariosIR.listaUsuariosIR(getEm(), secUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Inforeportes> lovIR() {
      try {
         return persistenciaUsuariosIR.lovIR(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Usuarios> listaUsuarios() {
      try {
         return persistenciaUsuariosIR.listaUsuarios(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Long getTotalRegistros(BigInteger secUsuario) {
      try {
         return persistenciaUsuariosIR.getTotalRegistros(getEm(), secUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<UsuariosInforeportes> getFind(int firstRow, int max, BigInteger secUsuario) {
      try {
         return persistenciaUsuariosIR.getFind(getEm(), firstRow, max, secUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<UsuariosInforeportes> getBuscarIR(int firstRow, int max, BigInteger secUsuarioIR) {
      try {
         return persistenciaUsuariosIR.getBuscarUIR(getEm(), firstRow, max, secUsuarioIR);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Long getTotalRegistrosBuscar(BigInteger secUsuario) {
      try {
         return persistenciaUsuariosIR.getTotalRegistrosBuscar(getEm(), secUsuario);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
