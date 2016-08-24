/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.PryPlataformas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarPryPlataformasInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar PryPlataformas.
     *
     * @param listaPryPlataformas Lista PryPlataformas que se van a modificar.
     */
    public void modificarPryPlataformas(List<PryPlataformas> listaPryPlataformas);

    /**
     * Método encargado de borrar PryPlataformas.
     *
     * @param listaPryPlataformas Lista PryPlataformas que se van a borrar.
     */
    public void borrarPryPlataformas(List<PryPlataformas> listaPryPlataformas);

    /**
     * Método encargado de crear PryPlataformas.
     *
     * @param listaPryPlataformas Lista PryPlataformas que se van a crear.
     */
    public void crearPryPlataformas(List<PryPlataformas> listaPryPlataformas);

    /**
     * Método encargado de recuperar las PryPlataformas para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de PryPlataformas.
     */
    public List<PryPlataformas> mostrarPryPlataformas();

    /**
     * Método encargado de recuperar una PryPlataformas dada su secuencia.
     *
     * @param secPryPlataformas Secuencia del PryPlataformas
     * @return Retorna una PryPlataformas.
     */
    public PryPlataformas mostrarPryPlataformas(BigInteger secPryPlataformas);

  /**
     * Método encargado de contar la cantidad de Proyectos
     * relacionadas con una PryPlataforma específico.
     *
     * @param secPryPlataformas Secuencia de la PryPlataforma.
     * @return Retorna un número indicando la cantidad de Proyectos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarProyectosPryPlataformas(BigInteger secPryPlataformas);
}
