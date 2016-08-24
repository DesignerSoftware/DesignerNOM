/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.NormasLaborales;
import Entidades.VigenciasNormasEmpleados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarVigenciaNormaLaboralInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Metodo Encargado de traer las VigenciasNormasEmpleados de un Empleado
     * Especifico.
     *
     * @param secEmpleado Secuencia del Empleado.
     * @return Lista de VigenciasNormasEmpleados.
     */
    public List<VigenciasNormasEmpleados> consultarVigenciasNormasEmpleadosPorEmpleado(BigInteger secEmpleado);

    /**
     * Metodo encargado de Modificar VigenciasNormasEmpleados.
     *
     * @param listaVigenciasNormasEmpleados Lista VigenciasNormasEmpleados que
     * se van a modificar.
     */
    public void modificarVigenciaNormaLaboral(List<VigenciasNormasEmpleados> listaVigenciasNormasEmpleados);

    /**
     * Metodo encargado de borrar VigenciasNormasEmpleados.
     *
     * @param listaVigenciasNormasEmpleados Lista VigenciasNormasEmpleados que
     * se van a borrar.
     */
    public void borrarVigenciaNormaLaboral(List<VigenciasNormasEmpleados> listaVigenciasNormasEmpleados);

    /**
     * Método encargado de crear VigenciasNormasEmpleados.
     *
     * @param listaVigenciasNormasEmpleados Lista VigenciasNormasEmpleados que
     * se van a crear.
     */
    public void crearVigenciaNormaLaboral(List<VigenciasNormasEmpleados> listaVigenciasNormasEmpleados);

    /**
     * *
     * Metodo encargado de buscar un Empleado especifico
     *
     * @param secEmpleado Secuencia del Empleado
     * @return Empleado .
     */
    public Empleados consultarEmpleado(BigInteger secEmpleado);

    /**
     * Método encargado de recuperar las NormasLaborales necesarias para la
     * lista de valores.
     *
     * @return Retorna una lista de NormasLaborales.
     */
    public List<NormasLaborales> lovNormasLaborales();
}
