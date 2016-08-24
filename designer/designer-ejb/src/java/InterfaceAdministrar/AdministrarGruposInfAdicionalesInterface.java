/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.GruposInfAdicionales;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarGruposInfAdicionalesInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar GruposInfAdicionales.
     *
     * @param listaGruposInfAdicionales Lista GruposInfAdicionales que se van a
     * modificar.
     */
    public void modificarGruposInfAdicionales(List<GruposInfAdicionales> listaGruposInfAdicionales);

    /**
     * Método encargado de borrar GruposInfAdicionales.
     *
     * @param listaGruposInfAdicionales Lista GruposInfAdicionales que se van a
     * borrar.
     */
    public void borrarGruposInfAdicionales(List<GruposInfAdicionales> listaGruposInfAdicionales);

    /**
     * Método encargado de crear GruposInfAdicionales.
     *
     * @param listaGruposInfAdicionales Lista GruposInfAdicionales que se van a
     * crear.
     */
    public void crearGruposInfAdicionales(List<GruposInfAdicionales> listaGruposInfAdicionales);

    /**
     * Método encargado de recuperar un GrupoInfAdicional dada su secuencia.
     *
     * @param secGruposInfAdicionales Secuencia del GrupoInfAdicional.
     * @return Retorna un GrupoInfAdicional cuya secuencia coincida con el valor del
     * parámetro.
     */
    public GruposInfAdicionales consultarGrupoInfAdicional(BigInteger secGruposInfAdicionales);

    /**
     * Metodo encargado de traer todas los GruposInfAdicionales de la base de
     * datos.
     *
     * @return Lista de GruposInfAdicionales.
     */
    public List<GruposInfAdicionales> consultarGruposInfAdicionales();

    /**
     * Método encargado de contar la cantidad de InformesAdicionales
     * relacionadas con un GrupoInfAdicional específico.
     *
     * @param secGruposInfAdicionales Secuencia del GrupoInfAdicional.
     * @return Retorna un número indicando la cantidad de InformesAdicionales
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarInformacionesAdicionales(BigInteger secGruposInfAdicionales);
}
