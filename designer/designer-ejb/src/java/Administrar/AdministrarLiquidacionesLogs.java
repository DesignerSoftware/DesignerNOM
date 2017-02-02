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

/**
 *
 * @author user
 */
@Stateful
public class AdministrarLiquidacionesLogs implements AdministrarLiquidacionesLogsInterface {

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

   private EntityManager em;

   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   public void modificarLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs) {
      for (int i = 0; i < listaLiquidacionesLogs.size(); i++) {
         System.out.println("Administrar Modificando...");
         persistenciaLiquidacionesLogs.editar(em, listaLiquidacionesLogs.get(i));
      }
   }

   public void borrarLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs) {
      for (int i = 0; i < listaLiquidacionesLogs.size(); i++) {
         System.out.println("Administrar Borrando...");
         persistenciaLiquidacionesLogs.borrar(em, listaLiquidacionesLogs.get(i));
      }
   }

   public void crearLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs) {
      for (int i = 0; i < listaLiquidacionesLogs.size(); i++) {
         System.out.println("Administrar Creando...");
         persistenciaLiquidacionesLogs.crear(em, listaLiquidacionesLogs.get(i));
      }
   }

   public List<LiquidacionesLogs> consultarLiquidacionesLogs() {
      return persistenciaLiquidacionesLogs.consultarLiquidacionesLogs(em);
   }

   public List<LiquidacionesLogs> consultarLiquidacionesLogsPorEmpleado(BigInteger secEmpleado) {
      List<LiquidacionesLogs> lesiones;
      lesiones = persistenciaLiquidacionesLogs.consultarLiquidacionesLogsPorEmpleado(em, secEmpleado);
      return lesiones;
   }

   public List<LiquidacionesLogs> consultarLiquidacionesLogsPorOperando(BigInteger secOperando) {
      return persistenciaLiquidacionesLogs.consultarLiquidacionesLogsPorOperando(em, secOperando);
   }

   public List<LiquidacionesLogs> consultarLiquidacionesLogsPorProceso(BigInteger secProceso) {
      return persistenciaLiquidacionesLogs.consultarLiquidacionesLogsPorProceso(em, secProceso);
   }

   @Override
   public List<Empleados> consultarLOVEmpleados() {
      List<Empleados> listEmpleados;
      listEmpleados = persistenciaEmpleados.consultarEmpleadosLiquidacionesLog(em);
      return listEmpleados;
   }

   @Override
   public List<Operandos> consultarLOVOperandos() {
      return persistenciaOperandos.buscarOperandos(em);
   }

   @Override
   public List<Procesos> consultarLOVProcesos() {
      return persistenciaProcesos.lovProcesos(em);
   }
}
