/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Retenciones;
import Entidades.VigenciasRetenciones;
import InterfaceAdministrar.AdministrarRetencionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRetencionesInterface;
import InterfacePersistencia.PersistenciaVigenciasRetencionesInterface;
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
public class AdministrarRetenciones implements AdministrarRetencionesInterface {

   private static Logger log = Logger.getLogger(AdministrarRetenciones.class);

   @EJB
   PersistenciaRetencionesInterface persistenciaRetenciones;
   @EJB
   PersistenciaVigenciasRetencionesInterface persistenciaVigenciasRetenciones;
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
   public void borrarVigenciaRetencion(VigenciasRetenciones vretenciones) {
      try {
         persistenciaVigenciasRetenciones.borrar(getEm(), vretenciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVigenciaRetencion(VigenciasRetenciones vretenciones) {
      try {
         persistenciaVigenciasRetenciones.crear(getEm(), vretenciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarVigenciaRetencion(List<VigenciasRetenciones> listaVigenciasRetencionesModificar) {
      try {
         for (int i = 0; i < listaVigenciasRetencionesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasRetencionesModificar.get(i).getUvt() == null) {
               listaVigenciasRetencionesModificar.get(i).setUvt(null);
            }
            persistenciaVigenciasRetenciones.editar(getEm(), listaVigenciasRetencionesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasRetenciones> consultarVigenciasRetenciones() {
      try {
         return persistenciaVigenciasRetenciones.buscarVigenciasRetenciones(getEm());
      } catch (Exception e) {
         log.warn("Error consultarVigenciasRetenciones: " + e.toString());
         return null;
      }
   }

//RETENCIONES
   @Override
   public void borrarRetencion(Retenciones retenciones) {
      try {
         persistenciaRetenciones.borrar(getEm(), retenciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearRetencion(Retenciones retenciones) {
      try {
         persistenciaRetenciones.crear(getEm(), retenciones);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarRetencion(List<Retenciones> listaRetencionesModificar) {
      try {
         for (int i = 0; i < listaRetencionesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaRetencionesModificar.get(i).getAdicionauvt() == null) {
               listaRetencionesModificar.get(i).setAdicionauvt(null);
            }
            persistenciaRetenciones.editar(getEm(), listaRetencionesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Retenciones> consultarRetenciones(BigInteger secRetencion) {
      try {
         return persistenciaRetenciones.buscarRetencionesVig(getEm(), secRetencion);
      } catch (Exception e) {
         log.warn("Error conceptoActual Admi : " + e.toString());
         return null;
      }
   }

}
