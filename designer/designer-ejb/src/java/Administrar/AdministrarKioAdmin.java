/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ConexionesKioskos;
import Entidades.Empleados;
import InterfaceAdministrar.AdministrarKioAdminInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaKioAdminInterface;
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
public class AdministrarKioAdmin implements AdministrarKioAdminInterface {

   private static Logger log = Logger.getLogger(AdministrarKioAdmin.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaKioAdminInterface persistencisKioAdmin;

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
   public List<Empleados> listEmpleadosCK() {
      try {
         return persistencisKioAdmin.consultarEmpleadosCK(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public ConexionesKioskos listCK(BigInteger secEmpleado) {
      try {
         return persistencisKioAdmin.conexionesKioskos(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void editarCK(List<ConexionesKioskos> ck) {
      try {
         for (int i = 0; i < ck.size(); i++) {
            persistencisKioAdmin.modificarck(getEm(), ck.get(i));
         }
      } catch (Exception e) {
         log.warn("error en editarCK : " + e.getMessage());
      }
   }

   @Override
   public void resetUsuario(BigInteger secEmpleado) {
      try {
         persistencisKioAdmin.resetUsuario(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("erro en reset Usuario admin : " + e.getMessage());
      }
   }

   @Override
   public void unlockUsuario(BigInteger secEmpleado) {
      try {
         persistencisKioAdmin.unlockUsuario(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("erro en unlock Usuario admin : " + e.getMessage());
      }
   }

}
