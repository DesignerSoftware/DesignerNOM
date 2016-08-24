/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.EstadosAfiliaciones;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import Entidades.TiposEntidades;
import Entidades.VigenciasAfiliaciones;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarVigenciasAfiliaciones3Interface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public void crearVigenciaAfiliacion(VigenciasAfiliaciones vigencia);

    public void editarVigenciaAfiliacion(VigenciasAfiliaciones vigencia);

    public void borrarVigenciaAfiliacion(VigenciasAfiliaciones vigencia);

    public List<VigenciasAfiliaciones> listVigenciasAfiliacionesEmpleado(BigInteger secuencia);

    public List<Terceros> listTerceros();

    public List<EstadosAfiliaciones> listEstadosAfiliaciones();

    public List<TiposEntidades> listTiposEntidades();
    
    public List<TercerosSucursales> listTercerosSucursales();
    
    public Empleados obtenerEmpleado (BigInteger secuencia);
    
    public Long validacionTercerosSurcursalesNuevaVigencia(BigInteger secuencia,Date fechaInicial,BigDecimal secuenciaTE, BigInteger secuenciaTer);
    
    public VigenciasAfiliaciones vigenciaAfiliacionSecuencia(BigInteger secuencia);
    
    public Date fechaContratacion(Empleados empl);
   
}
