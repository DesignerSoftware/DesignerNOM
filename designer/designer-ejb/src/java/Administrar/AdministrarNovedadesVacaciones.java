/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import InterfaceAdministrar.AdministrarNovedadesVacacionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaNovedadesSistemaInterface;
import InterfacePersistencia.PersistenciaVacacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

@Stateful
@Local
public class AdministrarNovedadesVacaciones implements AdministrarNovedadesVacacionesInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesVacaciones.class);

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
    @EJB
    PersistenciaNovedadesSistemaInterface persistenciaNovedadesSistema;
    @EJB
    PersistenciaVacacionesInterface persistenciaVacaciones;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public List<Empleados> empleadosVacaciones() {
        return persistenciaEmpleados.empleadosVacaciones(em);
    }
    
    @Override
    public Date obtenerFechaContratacionEmpleado(BigInteger secEmpleado) {
        try {
            Date ultimaFecha = persistenciaVigenciasTiposContratos.fechaFinalContratacionVacaciones(em, secEmpleado);
            return ultimaFecha;
        } catch (Exception e) {
            log.warn("Error obtenerFechaContratacionEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void adelantarPeriodo(BigInteger secEmpleado) {
        log.warn("Administrar novedades Vacaciones. adelantar Periodo para el empleado : " + secEmpleado);
       persistenciaVacaciones.adelantarPeriodo(em, secEmpleado); 
    }
    
    
    @Override
    public BigDecimal validarJornadaVacaciones(BigInteger secEmpleado, Date fechaInicialDisfrute) {
        log.warn("entró a validarJornadaVacaciones");
        log.warn("empleado a consultar : " + secEmpleado);
        log.warn("fecha inicial : " + fechaInicialDisfrute);
        BigDecimal jornada = persistenciaVacaciones.consultarJornadaVacaciones(em, secEmpleado, fechaInicialDisfrute);
        return jornada;
    }

    @Override
    public boolean validarFestivoVacaciones(Date fechaInicial, BigDecimal tipoJornada) {
        boolean festivo = persistenciaVacaciones.validarFestivoVacaciones(em, fechaInicial, tipoJornada);
        return festivo;
    }

    @Override
    public boolean validarDiaLaboralVacaciones( BigDecimal tipoJornada,String dia) {
        boolean dialaboral = persistenciaVacaciones.validarDiaLaboralVacaciones(em,tipoJornada,dia);
        return dialaboral;
    }

    @Override
    public Date fechaSiguiente(Date fecha, BigInteger numeroDias, BigDecimal jornada) {
       Date fechaSiguiente = persistenciaVacaciones.siguienteDia(em, fecha, numeroDias,jornada);
       return fechaSiguiente;
    }

    @Override
    public BigInteger periodicidadEmpleado(BigInteger secEmpleado) {
       BigInteger secPeriodicidad = persistenciaVacaciones.periodicidadEmpleado(em, secEmpleado);
       return secPeriodicidad;
    }

    @Override
    public Date anteriorFechaLimite(Date fechafinvaca, BigInteger secPeriodicidad) {
       Date anteriorFechaLimite = persistenciaVacaciones.anteriorFechaLimiteCalendario(em, fechafinvaca, secPeriodicidad);
       return anteriorFechaLimite;
    }

    @Override
    public Date despuesFechaLimite(Date fechafinvaca, BigInteger secPeriodicidad) {
        Date despuesFechaLimite = persistenciaVacaciones.despuesFechaLimiteCalendario(em, fechafinvaca, secPeriodicidad);
        return despuesFechaLimite;
    }

    @Override
    public Date fechaUltimoCorte(BigInteger secEmpleado, int codigoProceso) {
        Date fechaUltimoCorte = persistenciaVacaciones.fechaUltimoCorte(em, secEmpleado, codigoProceso);
        return fechaUltimoCorte;
    }
    
    
    
    
    
}
