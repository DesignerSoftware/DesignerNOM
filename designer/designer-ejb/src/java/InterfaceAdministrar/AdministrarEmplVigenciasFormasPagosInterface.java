/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.FormasPagos;
import Entidades.MetodosPagos;
import Entidades.Periodicidades;
import Entidades.Sucursales;
import Entidades.VigenciasFormasPagos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarEmplVigenciasFormasPagosInterface {

    /**
     * Metodo Encargado de traer las VigenciasFormasPagos de un Empleado
     * Especifico.
     *
     * @param secEmpleado Secuencia del Empleado.
     * @return Lista de VigenciasFormasPagos.
     */
    public List<VigenciasFormasPagos> consultarVigenciasFormasPagosPorEmpleado(BigInteger secEmpleado);

    /**
     * Metodo encargado de Modificar VigenciasFormasPagos.
     *
     * @param listVigenciasFormasPagos Lista VigenciasFormasPagos que se van a
     * modificar.
     */
    public void modificarVigenciasFormasPagos(List<VigenciasFormasPagos> listVigenciasFormasPagos);

    /**
     * Metodo encargado de borrar VigenciasFormasPagos.
     *
     * @param listVigenciasFormasPagos Lista VigenciasFormasPagos que se van a
     * borrar.
     */
    public void borrarVigenciasFormasPagos(List<VigenciasFormasPagos> listVigenciasFormasPagos);

    /**
     * Metodo encargado de crear VigenciasFormasPagos.
     *
     * @param vigenciasFormasPagos vigenciasFormasPagos que se va a crear.
     */
    public void crearVigencasFormasPagos(VigenciasFormasPagos vigenciasFormasPagos);

    /**
     * *
     * Metodo encargado de buscar un Empleado especifico
     *
     * @param secEmpleado Secuencia del Empleado
     * @return Empleado .
     */
    public Empleados consultarEmpleado(BigInteger secEmpleado);

    /**
     * Método encargado de recuperar las Sucursales necesarias para la lista de
     * valores.
     *
     * @return Retorna una lista de Sucursales.
     */
    public List<Sucursales> consultarLOVSucursales();

    /**
     * Método encargado de recuperar las FormasPagos necesarias para la lista de
     * valores.
     *
     * @return Retorna una lista de FormasPagos.
     */
    public List<FormasPagos> consultarLOVFormasPagos();

    /**
     * Método encargado de recuperar los MetodosPagos necesarias para la lista
     * de valores.
     *
     * @return Retorna una lista de MetodosPagos.
     */
    public List<MetodosPagos> consultarLOVMetodosPagos();

    /**
     * Método encargado de recuperar las Periodicidades necesarias para la lista
     * de valores.
     *
     * @return Retorna una lista de Periodicidades.
     */
    public List<Periodicidades> consultarLOVPerdiocidades();

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    public BigDecimal consultarPeriodicidadPorEmpl(BigInteger secEmpleado);
            
    
}
