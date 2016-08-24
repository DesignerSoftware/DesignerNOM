/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.InterconDynamics;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaInterconDynamicsInterface {

    public void crear(EntityManager em, InterconDynamics interconDynamics);

    public void editar(EntityManager em, InterconDynamics interconDynamics);

    public void borrar(EntityManager em, InterconDynamics interconDynamics);

    public InterconDynamics buscarInterconDynamicSecuencia(EntityManager em, BigInteger secuencia);

    public List<InterconDynamics> buscarInterconDynamicParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal);

    public int contarProcesosContabilizadosInterconDynamics(EntityManager em, Date fechaInicial, Date fechaFinal);

    public void cerrarProcesoContabilizacion(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal empleadoDesde, BigDecimal empleadoHasta);

    public void ejecutarPKGCrearArchivoPlano(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal empleadoDesde, BigDecimal empleadoHasta);

    public void ejecutarPKGRecontabilizar(EntityManager em, Date fechaInicial, Date fechaFinal);

    public void deleteInterconDynamics(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta);

    public void actualizarFlagContabilizacionDeshacerDynamics(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKGUbicarnuevointercon_DYNAMICS(EntityManager em, BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta);

    public void anularComprobantesCerrados(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso);

    public Date obtenerFechaContabilizacionMaxInterconDynamics(EntityManager em);

    public void ejecutarPKGUbicarnuevointercon_PLIN(EntityManager em, BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta);

    public void anularComprobantesCerrados_PL(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso);

    public void cerrarProcesoContabilizacion_PL(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal empleadoDesde, BigDecimal empleadoHasta);

    public void ejecutarPKGCrearArchivoPlano_PL(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal empleadoDesde, BigDecimal empleadoHasta);

    public void actionProcesarDatosDYNAMICSPL(EntityManager em, short codigoEmpresa);

    public void actionRespuestaDYNAMICSPL(EntityManager em, short codigoEmpresa);

    public void ejecutarPKGUbicarnuevointercon_MAMUT(EntityManager em, BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKGCrearArchivoPlano_MAMUT(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal empleadoDesde, BigDecimal empleadoHasta);

    public void ejecutarPKGUbicarnuevointercon_DYNAMICS_PACIFIC(EntityManager em, BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKGCrearArchivoPlano_PACIFIC(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal empleadoDesde, BigDecimal empleadoHasta);

    public String obtenerCargoDYNAMICSVT(EntityManager em, BigInteger empleado, Date fechaContabilizacion);

    public void ejecutarPKGCrearArchivoPlano_VT(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal empleadoDesde, BigDecimal empleadoHasta);

    public void ejecutarPKGUbicarnuevointercon_PROLUB(EntityManager em, BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKGCrearArchivoPlano_PROLUB(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal empleadoDesde, BigDecimal empleadoHasta);

    public void actualizarFlagContabilizacionDeshacerDynamics_NOT_EXITS(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKGUbicarnuevointercon_YV(EntityManager em, BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKGCrearArchivoPlano_YV(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal empleadoDesde, BigDecimal empleadoHasta);

    public void ejecutarPKGCrearArchivoPlano_CPS(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal empleadoDesde, BigDecimal empleadoHasta);
    
    public void ejecutarPKGCrearArchivoPlano_SX(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal empleadoDesde, BigDecimal empleadoHasta);
}
