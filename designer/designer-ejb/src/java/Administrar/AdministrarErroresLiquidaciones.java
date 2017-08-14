/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarErroresLiquidacionesInterface;
import Entidades.ErroresLiquidacion;
import Entidades.VigenciasLocalizaciones;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaErroresLiquidacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasLocalizacionesInterface;
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
public class AdministrarErroresLiquidaciones implements AdministrarErroresLiquidacionesInterface {

   private static Logger log = Logger.getLogger(AdministrarErroresLiquidaciones.class);

   @EJB
   PersistenciaErroresLiquidacionesInterface persistenciaErroresLiquidacionesInterface;
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaVigenciasLocalizacionesInterface persistenciaVigenciasLocalicaciones;
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

   public List<ErroresLiquidacion> consultarErroresLiquidacion() {
      try {
         log.warn("AdministrarErroresLiquidaciones.consultarErroresLiquidacion()");
         return persistenciaErroresLiquidacionesInterface.consultarErroresLiquidacion(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<ErroresLiquidacion> consultarErroresLiquidacionEmpleado(BigInteger secEmpleado) {
      try {
         List<ErroresLiquidacion> listaLiquidacionesLog = persistenciaErroresLiquidacionesInterface.consultarErroresLiquidacionPorEmpleado(getEm(), secEmpleado);
         List<VigenciasLocalizaciones> vigenciaSeleccionada;
         if (listaLiquidacionesLog != null || !listaLiquidacionesLog.isEmpty()) {
            for (int i = 0; i < listaLiquidacionesLog.size(); i++) {
               vigenciaSeleccionada = persistenciaVigenciasLocalicaciones.buscarVigenciasLocalizacionesEmpleado(getEm(), listaLiquidacionesLog.get(i).getEmpleado().getSecuencia());
               listaLiquidacionesLog.get(i).setVigenciaLocalizacion(vigenciaSeleccionada.get(0));
            }
         }
         return listaLiquidacionesLog;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public void borrarErroresLiquidaciones(List<ErroresLiquidacion> listaErroresLiquidacion) {
      try {
         log.warn("ADMINISTRARLIQUDACIONES listaErroresLiquidacion : " + listaErroresLiquidacion.size());
         for (int i = 0; i < listaErroresLiquidacion.size(); i++) {
            persistenciaErroresLiquidacionesInterface.borrar(getEm(), listaErroresLiquidacion.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public int borrarTodosErroresLiquidacion() {
      try {
         return persistenciaErroresLiquidacionesInterface.BorrarTotosErroresLiquidaciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return 0;
      }
   }
}
