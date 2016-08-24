/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.PryClientes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarPryClientesInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar PryClientes.
     *
     * @param listaPryClientes Lista PryClientes que se van a modificar.
     */
    public void modificarPryClientes(List<PryClientes> listaPryClientes);

    /**
     * Método encargado de borrar PryClientes.
     *
     * @param listaPryClientes Lista PryClientes que se van a borrar.
     */
    public void borrarPryClientes(List<PryClientes> listaPryClientes);

    /**
     * Método encargado de crear PryClientes.
     *
     * @param listaPryClientes Lista PryClientes que se van a crear.
     */
    public void crearPryClientes(List<PryClientes> listaPryClientes);

    /**
     * Método encargado de recuperar las PryClientes para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de PryClientes.
     */
    public List<PryClientes> consultarPryClientes();

    /**
     * Método encargado de recuperar una PryClientes dada su secuencia.
     *
     * @param secPryClientes Secuencia del PryClientes
     * @return Retorna una PryClientes.
     */
    public PryClientes consultarPryCliente(BigInteger secPryClientes);

    /**
     * Método encargado de contar la cantidad de Proyectos relacionadas con una
     * PryCliente específico.
     *
     * @param secPryClientes Secuencia del PryCliente.
     * @return Retorna un número indicando la cantidad de Proyectos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarProyectosPryCliente(BigInteger secPryClientes);
}
