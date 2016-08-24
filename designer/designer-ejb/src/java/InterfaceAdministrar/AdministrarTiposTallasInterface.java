/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposTallas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposTallasInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposTallas.
     *
     * @param listaTiposTallas Lista TiposTallas que se van a modificar.
     */
    public void modificarTiposTallas(List<TiposTallas> listaTiposTallas);

    /**
     * Método encargado de borrar TiposTallas.
     *
     * @param listaTiposTallas Lista TiposTallas que se van a borrar.
     */
    public void borrarTiposTallas(List<TiposTallas> listaTiposTallas);

    /**
     * Método encargado de crear TiposTallas.
     *
     * @param listaTiposTallas Lista TiposTallas que se van a crear.
     */
    public void crearTiposTallas(List<TiposTallas> listaTiposTallas);

    /**
     * Método encargado de recuperar las TiposTallas para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposTallas.
     */
    public List<TiposTallas> consultarTiposTallas();

    /**
     * Método encargado de recuperar un TipoTalla dada su secuencia.
     *
     * @param secTiposTallas Secuencia del TipoTalla
     * @return Retorna un TipoTalla.
     */
    public TiposTallas consultarTipoTalla(BigInteger secTiposTallas);

    /**
     * Método encargado de contar la cantidad de Elementos relacionadas con un
     * TipoTalla específica.
     *
     * @param secTiposTallas Secuencia del TipoTalla.
     * @return Retorna un número indicando la cantidad de Elementos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarElementosTipoTalla(BigInteger secTiposTallas);

    /**
     * Método encargado de contar la cantidad de VigenciasTallas relacionadas
     * con un TipoTalla específica.
     *
     * @param secTiposTallas Secuencia del TipoTalla.
     * @return Retorna un número indicando la cantidad de VigenciasTallas cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarVigenciasTallasTipoTalla(BigInteger secTiposTallas);
}
