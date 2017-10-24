/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ClasesPensiones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarClasesPensionesInterface {

    /**
     * Método encargado de modificar ClasesPensiones.
     *
     * @param listaClasesPensiones Lista ClasesPensiones que se van a modificar.
     */
    public String modificarClasesPensiones(ClasesPensiones claseP);

    /**
     * Método encargado de borrar ClasesPensiones.
     *
     * @param listaClasesPensiones Lista ClasesPensiones que se van a borrar.
     */
    public String borrarClasesPensiones(ClasesPensiones claseP);

    /**
     * Método encargado de crear ClasesPensiones.
     *
     * @param listaClasesPensiones Lista ClasesPensiones que se van a crear.
     */
    public String crearClasesPensiones(ClasesPensiones claseP);

    /**
     * Método encargado de recuperar las ClasesPensiones para un tabla de la
     * pantalla.
     *
     * @return Retorna un lista de ClasesPensiones.
     */
    public List<ClasesPensiones> consultarClasesPensiones();

    /**
     * Método encargado de recuperar un ClasePension dada su secuencia.
     *
     * @param secClasesPensiones Secuencia del ClasePension
     * @return Retorna un ClasePension.
     */
    public ClasesPensiones consultarClasePension(BigInteger secClasesPensiones);

    /**
     * Método encargado de contar la cantidad de Retirados relacionadas con un
     * ClasePension específico.
     *
     * @param secClasesPensiones Secuencia del ClasePension.
     * @return Retorna un número indicando la cantidad de Retirados cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarRetiradosClasePension(BigInteger secClasesPensiones);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
