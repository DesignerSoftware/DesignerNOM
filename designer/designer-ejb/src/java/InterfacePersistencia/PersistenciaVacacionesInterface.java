/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.Vacaciones;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaVacacionesInterface {
    
    public List<Vacaciones> periodoVacaciones(EntityManager em, BigInteger secuenciaEmpleado);
    public void adelantarPeriodo(EntityManager em,BigInteger secEmpleado);
    public int diasPendientes(EntityManager em, BigInteger secEmpleado,Date fechainicialcausado);
    public BigDecimal consultarJornadaVacaciones(EntityManager em,BigInteger secEmpleado, Date fechainicialDisfrute);
    public boolean validarFestivoVacaciones (EntityManager em,Date FechaInicialDisfrute, BigDecimal Jornada);
    public boolean validarDiaLaboralVacaciones (EntityManager em,BigDecimal tipoJornada,String dia);
    public Date siguienteDia(EntityManager em,Date fecha, BigInteger numeroDias,BigDecimal jornada);
    public BigInteger periodicidadEmpleado (EntityManager em, BigInteger secEmpleado);
    public Date anteriorFechaLimiteCalendario(EntityManager em,Date fechafinvaca,BigInteger secPeriodicidad);
    public Date despuesFechaLimiteCalendario(EntityManager em,Date fechafinvaca,BigInteger secPeriodicidad);
    public Date fechaUltimoCorte(EntityManager em, BigInteger secEmpleado,int codigoProceso);
    public void adicionaVacacionCambiosMasivos(EntityManager em, BigInteger ndias, Date fechaCambio, Date fechaPago);
    public void undoAdicionaVacacionCambiosMasivos(EntityManager em, BigInteger ndias, Date fechaCambio, Date fechaPago);
}
