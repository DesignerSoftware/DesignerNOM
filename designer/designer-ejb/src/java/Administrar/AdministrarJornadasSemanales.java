/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Jornadas;
import Entidades.JornadasLaborales;
import Entidades.JornadasSemanales;
import InterfaceAdministrar.AdministrarJornadasSemanalesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaJornadasInterface;
import InterfacePersistencia.PersistenciaJornadasLaboralesInterface;
import InterfacePersistencia.PersistenciaJornadasSemanalesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarJornadasSemanales implements AdministrarJornadasSemanalesInterface {

   private static Logger log = Logger.getLogger(AdministrarJornadasSemanales.class);

   @EJB
   PersistenciaJornadasSemanalesInterface persistenciaJornadasSemanales;
   @EJB
   PersistenciaJornadasInterface persistenciaJornadasInterface;
   @EJB
   PersistenciaJornadasLaboralesInterface persistenciaJornadasLaboralesInterface;
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

   public List<JornadasSemanales> consultarJornadasSemanales() {
      try {
         return persistenciaJornadasSemanales.buscarJornadasSemanales(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<JornadasLaborales> consultarJornadasLaborales() {
      try {
         return persistenciaJornadasLaboralesInterface.buscarJornadasLaborales(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Jornadas> consultarJornadas() {
      try {
         return persistenciaJornadasInterface.consultarJornadas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales) {
      try {
         JornadasSemanales c;
         for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            log.warn("Modificando...");
            c = listaJornadasSemanales.get(i);
            persistenciaJornadasSemanales.editar(getEm(), c);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales) {
      try {
         for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            log.warn("Borrando...");
            persistenciaJornadasSemanales.borrar(getEm(), listaJornadasSemanales.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales) {
      try {
         for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            log.warn("Creando...");
            persistenciaJornadasSemanales.crear(getEm(), listaJornadasSemanales.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

}
