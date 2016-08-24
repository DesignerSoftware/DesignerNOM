/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Ciudades;
import Entidades.Juzgados;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarJuzgadosInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de recuperar las Ciudades necesarias para la lista de
     * valores.
     *
     * @return Retorna una lista de Ciudades.
     */
    public List<Ciudades> consultarLOVCiudades();

    /**
     * Método encargado de modificar Juzgados.
     *
     * @param listaJuzgados Lista Juzgados que se van a modificar.
     */
    public void modificarJuzgados(List<Juzgados> listaJuzgados);

    /**
     * Método encargado de borrar Juzgados.
     *
     * @param listaJuzgados Lista Juzgados que se van a borrar.
     */
    public void borrarJuzgados(List<Juzgados> listaJuzgados);

    /**
     * Método encargado de crear Juzgados.
     *
     * @param listaJuzgados Lista Juzgados que se van a crear.
     */
    public void crearJuzgados(List<Juzgados> listaJuzgados);

    /**
     * Metodo Encargado de traer las Juzgados de una Ciudad Especifica.
     *
     * @param secCiudad Secuencia de las Ciuades.
     * @return Lista de Juzgados.
     */
    public List<Juzgados> consultarJuzgadosPorCiudad(BigInteger secCiudad);

    /**
     * Método encargado de recuperar los Juzgados necesarias para la lista de
     * valores.
     *
     * @return Retorna una lista de Juzgados.
     */
    public List<Juzgados> LOVJuzgadosPorCiudadGeneral();

    /**
     * Método encargado de contar la cantidad de EerPrestamos relacionadas con
     * un Juzgado específico.
     *
     * @param secJuzgados Secuencia del Juzgados.
     * @return Retorna un número indicando la cantidad de EerPrestamos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarEerPrestamos(BigInteger secJuzgados);

    /**
     * Metodo encargado de revisar si la cadena ingresada es un numero o una
     * letra
     *
     * @param cadena
     * @return Retorna true si es numero o false si es una letra
     */
    public boolean isNumeric(String cadena);
}
