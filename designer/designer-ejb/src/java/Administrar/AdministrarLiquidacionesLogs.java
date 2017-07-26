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

    private EntityManager em;

    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void modificarLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs) {
        for (int i = 0; i < listaLiquidacionesLogs.size(); i++) {
            persistenciaLiquidacionesLogs.editar(em, listaLiquidacionesLogs.get(i));
        }
    }

    public void borrarLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs) {
        for (int i = 0; i < listaLiquidacionesLogs.size(); i++) {
            persistenciaLiquidacionesLogs.borrar(em, listaLiquidacionesLogs.get(i));
        }
    }

    public void crearLiquidacionesLogs(List<LiquidacionesLogs> listaLiquidacionesLogs) {
        for (int i = 0; i < listaLiquidacionesLogs.size(); i++) {
            persistenciaLiquidacionesLogs.crear(em, listaLiquidacionesLogs.get(i));
        }
    }

    public List<LiquidacionesLogs> consultarLiquidacionesLogs() {
        return persistenciaLiquidacionesLogs.consultarLiquidacionesLogs(em);
    }

    public List<LiquidacionesLogs> consultarLiquidacionesLogsPorEmpleado(BigInteger secEmpleado) {
        return persistenciaLiquidacionesLogs.consultarLiquidacionesLogsPorEmpleado(em, secEmpleado);
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

    @Override
    public Long getTotalRegistrosLiquidacionesLogsPorEmpleado(BigInteger secEmpleado) {
        return persistenciaLiquidacionesLogs.getTotalRegistrosLiquidacionesLogsPorEmpleado(em, secEmpleado);
    }

    @Override
    public Long getTotalRegistrosLiquidacionesLogsPorOperando(BigInteger secOperando) {
        return persistenciaLiquidacionesLogs.getTotalRegistrosBuscarLiquidacionesLogsPorOperando(em, secOperando);
    }

    @Override
    public Long getTotalRegistrosLiquidacionesLogsPorProceso(BigInteger secProceso) {
        return persistenciaLiquidacionesLogs.getTotalRegistrosBuscarLiquidacionesLogsPorProceso(em, secProceso);
    }
}
