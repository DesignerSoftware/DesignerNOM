/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.Cuadrillas;
import Entidades.Empleados;
import Entidades.ParametrosTiempos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrador
 */
public interface AdministrarATParametroTiempoInterface {

    public void obtenerConexion(String idSesion);

    public List<Empleados> lovEmpleados();

    public ParametrosTiempos consultarParametrosTiemposPorUsarioBD(String usuarioBD);

    public ActualUsuario obtenerActualUsuario();

    public List<Cuadrillas> lovCuadrillas();

    public Date obtenerFechaInicialMinTurnosEmpleados();

    public Date obtenerFechaFinalMaxTurnosEmpleados();

    public void ejecutarPKG_INSERTARCUADRILLA(BigInteger cuadrilla, Date fechaDesde, Date fechaHasta);

    public void ejecutarPKG_SIMULARTURNOSEMPLEADOS(Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKG_LIQUIDAR(Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta, String formulaLiquidacion);

    public void ejecutarPKG_EliminarProgramacion(Date fechaDesde, Date fechaHasta);

    public void ejecutarPKG_ELIMINARSIMULACION(BigInteger cuadrilla, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta);

    public int ejecutarPKG_CONTARNOVEDADESLIQ(Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKG_ELIMINARLIQUIDACION(Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta);

    public void modificarParametroTiempo(ParametrosTiempos parametro);

}
