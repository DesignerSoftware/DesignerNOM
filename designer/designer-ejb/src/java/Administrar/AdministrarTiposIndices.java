/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposIndices;
import InterfaceAdministrar.AdministrarTiposIndicesInterface;
import InterfacePersistencia.PersistenciaTiposIndicesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposIndices implements AdministrarTiposIndicesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposIndices.class);

   @EJB
   PersistenciaTiposIndicesInterface persistenciaTiposIndices;
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

   public void modificarTiposIndices(List<TiposIndices> listaTiposIndices) {
      try {
         for (int i = 0; i < listaTiposIndices.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposIndices.editar(getEm(), listaTiposIndices.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarTiposIndices(List<TiposIndices> listaTiposIndices) {
      try {
         for (int i = 0; i < listaTiposIndices.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposIndices.borrar(getEm(), listaTiposIndices.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearTiposIndices(List<TiposIndices> listaTiposIndices) {
      try {
         for (int i = 0; i < listaTiposIndices.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposIndices.crear(getEm(), listaTiposIndices.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<TiposIndices> consultarTiposIndices() {
      try {
         return persistenciaTiposIndices.consultarTiposIndices(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public TiposIndices consultarTipoIndice(BigInteger secTiposIndices) {
      try {
         return persistenciaTiposIndices.consultarTipoIndice(getEm(), secTiposIndices);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarIndicesTipoIndice(BigInteger secTiposIndices) {
      try {
         return persistenciaTiposIndices.contarIndicesTipoIndice(getEm(), secTiposIndices);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposIndices contarIndicesTipoIndice ERROR : " + e);
         return null;
      }
   }

}
