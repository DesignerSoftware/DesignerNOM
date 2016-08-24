/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MetodosPagos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarMetodosPagosInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
  /**
     * Método encargado de modificar MetodosPagos.
     *
     * @param listaMetodosPagos Lista MetodosPagos que se van a modificar.
     */
    public void modificarMetodosPagos(List<MetodosPagos> listaMetodosPagos);
 /**
     * Método encargado de borrar MetodosPagos.
     *
     * @param listaMetodosPagos Lista MetodosPagos que se van a borrar.
     */
    public void borrarMetodosPagos(List<MetodosPagos> listaMetodosPagos);
/**
     * Método encargado de crear MetodosPagos.
     *
     * @param listaMetodosPagos Lista MetodosPagos que se van a crear.
     */
    public void crearMetodosPagos(List<MetodosPagos> listaMetodosPagos);

    /**
     * Método encargado de recuperar un MetodoPago dada su secuencia.
     *
     * @param secMetodosPagos Secuencia del MetodoPago.
     * @return Retorna una Lesion cuya secuencia coincida con el valor del
     * parámetro.
     */
    public MetodosPagos consultarMetodoPago(BigInteger secMetodosPagos);

    /**
     * Metodo encargado de traer todas las MetodosPagos de la base de datos.
     *
     * @return Lista de MetodosPagos.
     */
    public List<MetodosPagos> consultarMetodosPagos();

    /**
     * Método encargado de contar la cantidad de VigenciasFormasPagos
     * relacionadas con un MetodoPago específico.
     *
     * @param secMetodoPago Secuencia del MetodoPago.
     * @return Retorna un número indicando la cantidad de VigenciasFormasPagos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarMetodosPagosVigenciasFormasPagos(BigInteger secMetodoPago);
}
