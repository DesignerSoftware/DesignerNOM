/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosReemplazos;
import InterfaceAdministrar.AdministrarMotivosReemplazosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosReemplazosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarMotivosReemplazos implements AdministrarMotivosReemplazosInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosReemplazos.class);

   @EJB
   PersistenciaMotivosReemplazosInterface persistenciaMotivosReemplazos;

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
   public List<MotivosReemplazos> MotivosReemplazos() {
      try {
         return persistenciaMotivosReemplazos.motivosReemplazos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosReemplazos> lovTiposReemplazos() {
      try {
         return persistenciaMotivosReemplazos.motivosReemplazos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public void modificarMotivosReemplazos(List<MotivosReemplazos> listaMotivosReemplazos) {
      try {
         for (int i = 0; i < listaMotivosReemplazos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosReemplazos.editar(getEm(), listaMotivosReemplazos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarMotivosReemplazos(List<MotivosReemplazos> listaMotivosReemplazos) {
      try {
         for (int i = 0; i < listaMotivosReemplazos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosReemplazos.borrar(getEm(), listaMotivosReemplazos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearMotivosReemplazos(List<MotivosReemplazos> listaMotivosReemplazos) {
      try {
         for (int i = 0; i < listaMotivosReemplazos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMotivosReemplazos.crear(getEm(), listaMotivosReemplazos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public BigInteger contarEncargaturasMotivoReemplazo(BigInteger secMotivoReemplazo) {
      try {
         return persistenciaMotivosReemplazos.contarEncargaturasMotivoReemplazo(getEm(), secMotivoReemplazo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
