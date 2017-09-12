/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.LiquidacionesLogs;
import Entidades.Operandos;
import Entidades.Procesos;
import InterfaceAdministrar.AdministrarLiquidacionesLogsInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaLiquidacionesLogsInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
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
public class AdministrarLiquidacionesLogs implements AdministrarLiquidacionesLogsInterface {

   private static Logger log = Logger.getLogger(AdministrarLiquidacionesLogs.class);

   @EJB
   PersistenciaLiquidacionesLogsInterface persistenciaLiquidacionesLogs;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaOperandosInterface persistenciaOperandos;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
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

   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public void modificarLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs) {
      try {
         for (int i = 0; i < listaLiquidacionesLogs.size(); i++) {
            persistenciaLiquidacionesLogs.editar(getEm(), listaLiquidacionesLogs.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarLiquidacionesLogs() ERROR: " + e);
      }
   }

   public void borrarLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs) {
      try {
         for (int i = 0; i < listaLiquidacionesLogs.size(); i++) {
            persistenciaLiquidacionesLogs.borrar(getEm(), listaLiquidacionesLogs.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarLiquidacionesLogs() ERROR: " + e);
      }
   }

   public void crearLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs) {
      try {
         for (int i = 0; i < listaLiquidacionesLogs.size(); i++) {
            persistenciaLiquidacionesLogs.crear(getEm(), listaLiquidacionesLogs.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearLiquidacionesLogs() ERROR: " + e);
      }
   }

   public List<LiquidacionesLogs> consultarLiquidacionesLogs() {
      try {
         return persistenciaLiquidacionesLogs.consultarLiquidacionesLogs(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLiquidacionesLogs() ERROR: " + e);
         return null;
      }
   }

   public List<LiquidacionesLogs> consultarLiquidacionesLogsPorEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaLiquidacionesLogs.consultarLiquidacionesLogsPorEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLiquidacionesLogsPorEmpleado() ERROR: " + e);
         return null;
      }
   }

   public List<LiquidacionesLogs> consultarLiquidacionesLogsPorOperando(BigInteger secOperando) {
      try {
         return persistenciaLiquidacionesLogs.consultarLiquidacionesLogsPorOperando(getEm(), secOperando);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLiquidacionesLogsPorOperando() ERROR: " + e);
         return null;
      }
   }

   public List<LiquidacionesLogs> consultarLiquidacionesLogsPorProceso(BigInteger secProceso) {
      try {
         return persistenciaLiquidacionesLogs.consultarLiquidacionesLogsPorProceso(getEm(), secProceso);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLiquidacionesLogsPorProceso() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> consultarLOVEmpleados() {
      try {
         return persistenciaEmpleados.consultarEmpleadosLiquidacionesLog(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLOVEmpleados() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Operandos> consultarLOVOperandos() {
      try {
         return persistenciaOperandos.buscarOperandos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLOVOperandos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Procesos> consultarLOVProcesos() {
      try {
         return persistenciaProcesos.lovProcesos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarLOVProcesos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Long getTotalRegistrosLiquidacionesLogsPorEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaLiquidacionesLogs.getTotalRegistrosLiquidacionesLogsPorEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".getTotalRegistrosLiquidacionesLogsPorEmpleado() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Long getTotalRegistrosLiquidacionesLogsPorOperando(BigInteger secOperando) {
      try {
         return persistenciaLiquidacionesLogs.getTotalRegistrosBuscarLiquidacionesLogsPorOperando(getEm(), secOperando);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".getTotalRegistrosLiquidacionesLogsPorOperando() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Long getTotalRegistrosLiquidacionesLogsPorProceso(BigInteger secProceso) {
      try {
         return persistenciaLiquidacionesLogs.getTotalRegistrosBuscarLiquidacionesLogsPorProceso(getEm(), secProceso);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".getTotalRegistrosLiquidacionesLogsPorProceso() ERROR: " + e);
         return null;
      }
   }
}
