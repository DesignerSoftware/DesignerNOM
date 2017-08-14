/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.RiesgosProfesionales;
import InterfaceAdministrar.AdministrarRiesgosProfesionalesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRiesgosProfesionalesInterface;
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
public class AdministrarRiesgosProfesionales implements AdministrarRiesgosProfesionalesInterface {

   private static Logger log = Logger.getLogger(AdministrarRiesgosProfesionales.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaRiesgosProfesionalesInterface persistenciaRiesgos;

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
   public List<RiesgosProfesionales> listRiesgoProfesional() {
      try {
         return persistenciaRiesgos.riesgosProfesionales(getEm());
      } catch (Exception e) {
         log.warn("error en AdministrarRiesgosProfesionales.listRiesgoProfesional " + e.getMessage());
         return null;
      }
   }

   @Override
   public void crearRiesgoProfesional(List<RiesgosProfesionales> listaVD) {
      try {
         for (int i = 0; i < listaVD.size(); i++) {
            persistenciaRiesgos.crear(getEm(), listaVD.get(i));
         }
      } catch (Exception e) {
         log.warn("error en crearRiesgoProfesional administrar :" + e.getMessage());
      }

   }

   @Override
   public void editarRiesgoProfesional(List<RiesgosProfesionales> listaVD) {
      try {
         for (int i = 0; i < listaVD.size(); i++) {
            persistenciaRiesgos.editar(getEm(), listaVD.get(i));
         }
      } catch (Exception e) {
         log.warn("error en editarRiesgoProfesional administrar :" + e.getMessage());
      }
   }

   @Override
   public void borrarRiesgoProfesional(List<RiesgosProfesionales> listaVD) {
      try {
         for (int i = 0; i < listaVD.size(); i++) {
            persistenciaRiesgos.borrar(getEm(), listaVD.get(i));
         }
      } catch (Exception e) {
         log.warn("error en borrarRiesgoProfesional administrar :" + e.getMessage());
      }
   }

}
