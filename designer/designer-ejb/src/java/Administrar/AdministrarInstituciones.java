/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Instituciones;
import InterfaceAdministrar.AdministrarInstitucionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarInstituciones implements AdministrarInstitucionesInterface {

   private static Logger log = Logger.getLogger(AdministrarInstituciones.class);

   @EJB
   PersistenciaInstitucionesInterface persistenciaInstituciones;

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
   public List<Instituciones> Instituciones() {
      List<Instituciones> listaInstituciones;
      listaInstituciones = persistenciaInstituciones.instituciones(getEm());
      return listaInstituciones;
   }

   @Override
   public List<Instituciones> lovInstituciones() {
      return persistenciaInstituciones.instituciones(getEm());
   }

   @Override
   public void crear(List<Instituciones> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaInstituciones.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en AdministrarInstituciones.crear : " + e.toString());
      }
   }

   @Override
   public void editar(List<Instituciones> listaEditar) {
      try {
         for (int i = 0; i < listaEditar.size(); i++) {
            persistenciaInstituciones.editar(getEm(), listaEditar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en AdministrarInstituciones.editar : " + e.toString());
      }
   }

   @Override
   public void borrar(List<Instituciones> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaInstituciones.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error en AdministrarInstituciones.borrar : " + e.toString());
      }
   }
}
