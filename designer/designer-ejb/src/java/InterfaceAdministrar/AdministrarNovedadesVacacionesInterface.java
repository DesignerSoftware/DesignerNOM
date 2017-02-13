/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarNovedadesVacacionesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<Empleados> empleadosVacaciones();

    public Date obtenerFechaContratacionEmpleado(BigInteger secEmpleado);

    public void adelantarPeriodo(BigInteger secEmpleado);

    public BigDecimal validarJornadaVacaciones(BigInteger secEmpleado, Date fechaInicialDisfrute);

    public boolean validarFestivoVacaciones(Date fechaInicial, BigDecimal tipoJornada);

    public boolean validarDiaLaboralVacaciones(BigDecimal tipoJornada,String dia);
    
    public Date fechaSiguiente(Date fecha,BigInteger numeroDias, BigDecimal jornada);
    
    public BigInteger periodicidadEmpleado(BigInteger secEmpleado);
    
    public Date anteriorFechaLimite(Date fechafinvaca, BigInteger secPeriodicidad);
    
    public Date despuesFechaLimite(Date fechafinvaca, BigInteger secPeriodicidad);
    
    public Date fechaUltimoCorte(BigInteger secEmpleado,int codigoProceso);
}
