/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.LugaresOcurrencias;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarLugaresOcurrenciasInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar LugaresOcurrencias.
     *
     * @param listaLugaresOcurrencias Lista LugaresOcurrencias que se van a
     * modificar.
     */
    public void modificarLugarOcurrencia(List<LugaresOcurrencias> listaLugaresOcurrencias);

    /**
     * Método encargado de borrar LugaresOcurrencias.
     *
     * @param listaLugaresOcurrencias Lista LugaresOcurrencias que se van a
     * borrar.
     */
    public void borrarLugarOcurrencia(List<LugaresOcurrencias> listaLugaresOcurrencias);

    /**
     * Método encargado de crear LugaresOcurrencias.
     *
     * @param listaLugaresOcurrencias Lista LugaresOcurrencias que se van a
     * crear.
     */
    public void crearLugarOcurrencia(List<LugaresOcurrencias> listaLugaresOcurrencias);

    /**
     * Método encargado de recuperar las LugaresOcurrencias para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de LugaresOcurrencias.
     */
    public List<LugaresOcurrencias> consultarLugaresOcurrencias();

    /**
     * Método encargado de recuperar una LugaresOcurrencias dada su secuencia.
     *
     * @param secLugaresOcurrencias Secuencia del LugaresOcurrencias
     * @return Retorna una LugaresOcurrencias.
     */
    public LugaresOcurrencias consultarLugarOcurrencia(BigInteger secLugaresOcurrencias);

    /**
     * Método encargado de consultar si existe una relacion entre una
     * LugaresOcurrencias específica y algún SoAccidentes. Adémas de la
     * revisión, establece cuantas relaciones existen.
     *
     * @param secLugaresOcurrencias Secuencia de la LugaresOcurrencias.
     * @return Retorna el número de SoAccidentes relacionados con la
     * MotivoCambioCargo cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarSoAccidentesLugarOcurrencia(BigInteger secLugaresOcurrencias);
}
