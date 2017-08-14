/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposTiposCC;
import InterfaceAdministrar.AdministrarGruposTiposCCInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposTiposCCInterface;
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
public class AdministrarGruposTiposCC implements AdministrarGruposTiposCCInterface {

   private static Logger log = Logger.getLogger(AdministrarGruposTiposCC.class);

   @EJB
   PersistenciaGruposTiposCCInterface persistenciaGrupos;
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
   public void crearGrupo(List<GruposTiposCC> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaGrupos.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("Error Administrar.AdministrarGruposTiposCC.crearGrupo() : " + e.toString());
      }

   }

   @Override
   public void editarGrupo(List<GruposTiposCC> listaEditar) {
      try {
         for (int i = 0; i < listaEditar.size(); i++) {
            persistenciaGrupos.editar(getEm(), listaEditar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error Administrar.AdministrarGruposTiposCC.editarGrupo() : " + e.toString());
      }
   }

   @Override
   public void borrarGrupo(List<GruposTiposCC> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaGrupos.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("Error Administrar.AdministrarGruposTiposCC.borrarGrupo() : " + e.toString());
      }
   }

   @Override
   public List<GruposTiposCC> consultarGrupos() {
      try {
         return persistenciaGrupos.buscarGruposTiposCC(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
