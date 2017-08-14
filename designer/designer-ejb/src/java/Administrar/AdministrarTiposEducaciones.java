/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposEducaciones;
import InterfaceAdministrar.AdministrarTiposEducacionesInterface;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
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
public class AdministrarTiposEducaciones implements AdministrarTiposEducacionesInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposEducaciones.class);

   @EJB
   PersistenciaTiposEducacionesInterface persistenciaTiposEducaciones;
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
   public List<TiposEducaciones> TiposEducaciones() {
      try {
         return persistenciaTiposEducaciones.tiposEducaciones(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposEducaciones> lovTiposEducaciones() {
      try {
         return persistenciaTiposEducaciones.tiposEducaciones(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crear(List<TiposEducaciones> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaTiposEducaciones.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en AdministrarTiposEducaciones.crear : " + e.toString());
      }
   }

   @Override
   public void editar(List<TiposEducaciones> listaEditar) {
      try {
         for (int i = 0; i < listaEditar.size(); i++) {
            persistenciaTiposEducaciones.editar(getEm(), listaEditar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en AdministrarTiposEducaciones.editar : " + e.toString());
      }
   }

   @Override
   public void borrar(List<TiposEducaciones> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaTiposEducaciones.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en AdministrarTiposEducaciones.borrar : " + e.toString());
      }
   }
}
