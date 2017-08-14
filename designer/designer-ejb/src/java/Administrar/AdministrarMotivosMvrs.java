/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosMvrsInterface;
import Entidades.Motivosmvrs;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosMvrsInterface;
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
public class AdministrarMotivosMvrs implements AdministrarMotivosMvrsInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosMvrs.class);

   @EJB
   PersistenciaMotivosMvrsInterface persistenciaMotivosMvrs;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

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
   public void modificarMotivosMvrs(List<Motivosmvrs> listaMotivosMvrs) {
      try {
         for (int i = 0; i < listaMotivosMvrs.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosMvrs.editar(getEm(), listaMotivosMvrs.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarMotivosMvrs(List<Motivosmvrs> listaMotivosMvrs) {
      try {
         for (int i = 0; i < listaMotivosMvrs.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosMvrs.borrar(getEm(), listaMotivosMvrs.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearMotivosMvrs(List<Motivosmvrs> listaMotivosMvrs) {
      try {
         for (int i = 0; i < listaMotivosMvrs.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMotivosMvrs.crear(getEm(), listaMotivosMvrs.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Motivosmvrs> consultarMotivosMvrs() {
      try {
         return persistenciaMotivosMvrs.buscarMotivosMvrs(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Motivosmvrs consultarMotivosMvrs(BigInteger secMotivosMvrs) {
      try {
         return persistenciaMotivosMvrs.buscarMotivosMvrs(getEm(), secMotivosMvrs);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
