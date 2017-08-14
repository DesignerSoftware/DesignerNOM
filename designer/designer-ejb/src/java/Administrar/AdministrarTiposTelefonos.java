/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposTelefonos;
import InterfaceAdministrar.AdministrarTiposTelefonosInterface;
import InterfacePersistencia.PersistenciaTiposTelefonosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarTiposTelefonos implements AdministrarTiposTelefonosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposTelefonos.class);

   @EJB
   PersistenciaTiposTelefonosInterface persistenciaTiposTelefonos;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private TiposTelefonos tt;
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
   public List<TiposTelefonos> tiposTelefonos() {
      try {
         return persistenciaTiposTelefonos.tiposTelefonos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarTipoTelefono(List<TiposTelefonos> listaTiposTelefonosModificar) {
      try {
         for (int i = 0; i < listaTiposTelefonosModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaTiposTelefonosModificar.get(i).getSecuencia() == null) {
               tt = listaTiposTelefonosModificar.get(i);
            } else {
               tt = listaTiposTelefonosModificar.get(i);
            }
            persistenciaTiposTelefonos.editar(getEm(), tt);
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarTipoTelefono(TiposTelefonos tipoTelefono) {
      try {
         persistenciaTiposTelefonos.borrar(getEm(), tipoTelefono);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearTipoTelefono(TiposTelefonos tipoTelefono) {
      try {
         persistenciaTiposTelefonos.crear(getEm(), tipoTelefono);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }
}
