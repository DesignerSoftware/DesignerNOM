/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Monedas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author John Steven Pineda
 */
@Local
public interface AdministrarMonedasInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar Monedas.
     *
     * @param listMonedasModificadas Lista de las Monedas que se van a
     * modificar.
     */
    public void modificarMonedas(List<Monedas> listMonedasModificadas);

    /**
     * Método encargado de borrar Monedas.
     *
     * @param monedas Una Moneda que se van a Borrar.
     */
    public void borrarMonedas(Monedas monedas);

    /**
     * Método encargado de crear Monedas.
     *
     * @param monedas Una Moneda que se van a crear.
     */
    public void crearMonedas(Monedas monedas);

    /**
     * Método encargado de recuperar las Monedas para una tabla de la pantalla.
     *
     * @return Retorna una lista de Contratos.
     */
    public List<Monedas> consultarMonedas();

    /**
     * Método encargado de recuperar una Moneda dada su secuencia.
     *
     * @param secuencia Secuencia de la Moneda
     * @return Retorna una Moneda.
     */
    public Monedas consultarMoneda(BigInteger secuencia);

    /**
     * Método encargado de consultar si existe una relacion entre una Moneda
     * específica y algún Proyecto. Adémas de la revisión, establece cuantas
     * relaciones existen.
     *
     * @param secuencia Secuencia de la Moneda.
     * @return Retorna el número de proyectos relacionados con la moneda cuya
     * secuencia coincide con el parámetro.
     */
    public BigInteger verificarMonedasProyecto(BigInteger secuencia);
}
