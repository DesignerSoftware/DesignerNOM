/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvEntrevistas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarHvEntrevistasInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar HvEntrevistas.
     *
     * @param listaHvEntrevistas Lista HvEntrevistas que se van a modificar.
     */
    public void modificarHvEntrevistas(List<HvEntrevistas> listaHvEntrevistas);

    /**
     * Método encargado de borrar HvEntrevistas.
     *
     * @param listaHvEntrevistas Lista HvEntrevistas que se van a borrar.
     */
    public void borrarHvEntrevistas(List<HvEntrevistas> listaHvEntrevistas);

    /**
     * Método encargado de crear HvEntrevistas.
     *
     * @param listaHvEntrevistas Lista HvEntrevistas que se van a crear.
     */
    public void crearHvEntrevistas(List<HvEntrevistas> listaHvEntrevistas);

    /**
     * Metodo Encargado de traer las HvEntrevistas de un Empleado Especifico.
     *
     * @param secEmpleado Secuencia del Empleado.
     * @return Lista de HvEntrevistas.
     */
    public List<HvEntrevistas> consultarHvEntrevistasPorEmpleado(BigInteger secEmpleado);

    /**
     * Método encargado de recuperar un HvEntrevista dada su secuencia.
     *
     * @param secHvEntrevista Secuencia del HvEntrevista.
     * @return Retorna un HvEntrevistas cuya secuencia coincida con el valor del
     * parámetro.
     */
    public HvEntrevistas consultarHvEntrevista(BigInteger secHvEntrevista);

    /**
     * *
     * Metodo encargado de buscar un Empleado especifico
     *
     * @param secEmpleado Secuencia del Empleado
     * @return Empleado.
     */
    public Empleados consultarEmpleado(BigInteger secEmpleado);

    /**
     * Metodo encargado de traer las HVHojasDeVida de un empleado especifico
     *
     * @param secuencia
     * @return lista HVHojasDeVida
     */
    public List<HVHojasDeVida> buscarHVHojasDeVida(BigInteger secuencia);
}
