/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosLocalizaciones;
import InterfaceAdministrar.AdministrarMotivosLocalizacionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosLocalizacionesInterface;
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
public class AdministrarMotivosLocalizaciones implements AdministrarMotivosLocalizacionesInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosLocalizaciones.class);

   @EJB
   PersistenciaMotivosLocalizacionesInterface PersistenciaMotivosLocalizaciones;
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
   public void modificarMotivosLocalizaciones(List<MotivosLocalizaciones> listaMotivosLocalizaciones) {
      try {
         for (int i = 0; i < listaMotivosLocalizaciones.size(); i++) {
            log.warn("Administrar Modificando...");
            PersistenciaMotivosLocalizaciones.editar(getEm(), listaMotivosLocalizaciones.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarMotivosLocalizaciones(List<MotivosLocalizaciones> listaMotivosLocalizaciones) {
      try {
         for (int i = 0; i < listaMotivosLocalizaciones.size(); i++) {
            log.warn("Administrar Borrando...");
            PersistenciaMotivosLocalizaciones.borrar(getEm(), listaMotivosLocalizaciones.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearMotivosLocalizaciones(List<MotivosLocalizaciones> listaMotivosLocalizaciones) {
      try {
         for (int i = 0; i < listaMotivosLocalizaciones.size(); i++) {
            log.warn("Administrar Creando...");
            PersistenciaMotivosLocalizaciones.crear(getEm(), listaMotivosLocalizaciones.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<MotivosLocalizaciones> mostrarMotivosCambiosCargos() {
      try {
         return PersistenciaMotivosLocalizaciones.buscarMotivosLocalizaciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public MotivosLocalizaciones mostrarMotivoCambioCargo(BigInteger secMotivosCambiosCargos) {
      try {
         return PersistenciaMotivosLocalizaciones.buscarMotivoLocalizacionSecuencia(getEm(), secMotivosCambiosCargos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarVigenciasLocalizacionesMotivoLocalizacion(BigInteger secMotivoLocalizacion) {
      try {
         return PersistenciaMotivosLocalizaciones.contarVigenciasLocalizacionesMotivoLocalizacion(getEm(), secMotivoLocalizacion);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
