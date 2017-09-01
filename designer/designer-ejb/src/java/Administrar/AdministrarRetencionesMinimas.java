/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.RetencionesMinimas;
import Entidades.VigenciasRetencionesMinimas;
import InterfaceAdministrar.AdministrarRetencionesMinimasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRetencionesMinimasInterface;
import InterfacePersistencia.PersistenciaVigenciasRetencionesMinimasInterface;
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
public class AdministrarRetencionesMinimas implements AdministrarRetencionesMinimasInterface {

   private static Logger log = Logger.getLogger(AdministrarRetencionesMinimas.class);

   @EJB
   PersistenciaRetencionesMinimasInterface persistenciaRetencionesMinimas;
   @EJB
   PersistenciaVigenciasRetencionesMinimasInterface persistenciaVigenciasRetencionesMinimas;
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

//VIGENCIAS RETENCIONES
   @Override
   public void borrarVigenciaRetencion(VigenciasRetencionesMinimas vretenciones) {
      try {
         persistenciaVigenciasRetencionesMinimas.borrar(getEm(), vretenciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVigenciaRetencion(VigenciasRetencionesMinimas vretenciones) {
      try {
         persistenciaVigenciasRetencionesMinimas.crear(getEm(), vretenciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarVigenciaRetencion(List<VigenciasRetencionesMinimas> listaVigenciasRetencionesModificar) {
      try {
         for (int i = 0; i < listaVigenciasRetencionesModificar.size(); i++) {
            log.warn("Modificando...");
            persistenciaVigenciasRetencionesMinimas.editar(getEm(), listaVigenciasRetencionesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasRetencionesMinimas> consultarVigenciasRetenciones() {
      try {
         return persistenciaVigenciasRetencionesMinimas.buscarVigenciasRetencionesMinimas(getEm());
      } catch (Exception e) {
         log.warn("Error consultarVigenciasRetenciones: " + e.toString());
         return null;
      }
   }

//RETENCIONES
   @Override
   public void borrarRetencion(RetencionesMinimas retenciones) {
      try {
         persistenciaRetencionesMinimas.borrar(getEm(), retenciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearRetencion(RetencionesMinimas retenciones) {
      try {
         persistenciaRetencionesMinimas.crear(getEm(), retenciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarRetencion(List<RetencionesMinimas> listaRetencionesModificar) {
      try {
         for (int i = 0; i < listaRetencionesModificar.size(); i++) {
            log.warn("Modificando...");
            persistenciaRetencionesMinimas.editar(getEm(), listaRetencionesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<RetencionesMinimas> consultarRetenciones(BigInteger secRetencion) {
      try {
         return persistenciaRetencionesMinimas.buscarRetencionesMinimasVig(getEm(), secRetencion);
      } catch (Exception e) {
         log.warn("Error consultarRetenciones Admi : " + e.toString());
         return null;
      }
   }
}
