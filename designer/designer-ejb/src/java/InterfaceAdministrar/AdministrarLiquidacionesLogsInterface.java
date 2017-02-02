/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.LiquidacionesLogs;
import Entidades.Operandos;
import Entidades.Procesos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarLiquidacionesLogsInterface {

   public void obtenerConexion(String idSesion);

   public void modificarLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs);

   public void borrarLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs);

   public void crearLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs);

   public List<LiquidacionesLogs> consultarLiquidacionesLogs();

   public List<LiquidacionesLogs> consultarLiquidacionesLogsPorEmpleado(BigInteger secEmpleado);

   public List<LiquidacionesLogs> consultarLiquidacionesLogsPorOperando(BigInteger secOperando);
   
   public List<LiquidacionesLogs> consultarLiquidacionesLogsPorProceso(BigInteger secProceso);

   public List<Empleados> consultarLOVEmpleados();
   
   public List<Operandos> consultarLOVOperandos();
   
   public List<Procesos> consultarLOVProcesos();
}
