/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposConclusiones;
import InterfaceAdministrar.AdministrarTiposConclusionesInterface;
import InterfacePersistencia.PersistenciaTiposConclusionesInterface;
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
public class AdministrarTiposConclusiones implements AdministrarTiposConclusionesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposConclusiones.class);

   @EJB
   PersistenciaTiposConclusionesInterface persistenciaTiposConclusiones;
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

   @Override
   public void modificarTiposConclusiones(List<TiposConclusiones> listaTiposConclusiones) {
      try {
         for (int i = 0; i < listaTiposConclusiones.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposConclusiones.editar(getEm(), listaTiposConclusiones.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTiposConclusiones() ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposConclusiones(List<TiposConclusiones> listaTiposConclusiones) {
      try {
         for (int i = 0; i < listaTiposConclusiones.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposConclusiones.borrar(getEm(), listaTiposConclusiones.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTiposConclusiones() ERROR: " + e);
      }
   }

   @Override
   public void crearTiposConclusiones(List<TiposConclusiones> listaTiposConclusiones) {
      try {
         for (int i = 0; i < listaTiposConclusiones.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposConclusiones.crear(getEm(), listaTiposConclusiones.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTiposConclusiones() ERROR: " + e);
      }
   }

   @Override
   public List<TiposConclusiones> consultarTiposConclusiones() {
      try {
         return persistenciaTiposConclusiones.consultarTiposConclusiones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTiposConclusiones() ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposConclusiones consultarTipoConclusion(BigInteger secTiposConclusiones) {
      try {
         return persistenciaTiposConclusiones.consultarTipoConclusion(getEm(), secTiposConclusiones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTipoConclusion() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarProcesosTipoConclusion(BigInteger secTiposConclusiones) {
      try {
         return persistenciaTiposConclusiones.contarChequeosMedicosTipoConclusion(getEm(), secTiposConclusiones);
      } catch (Exception e) {
         log.error("ERROR AdministrarTiposConclusiones contarChequeosMedicosTipoConclusion ERROR : " + e);
         return null;
      }
   }
}
