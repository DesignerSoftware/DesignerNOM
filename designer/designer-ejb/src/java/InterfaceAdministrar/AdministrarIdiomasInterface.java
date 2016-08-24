/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Idiomas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarIdiomasInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar Idiomas.
     *
     * @param listaIdiomas Lista Idiomas que se van a modificar.
     */
    public void modificarIdiomas(List<Idiomas> listaIdiomas);

    /**
     * Método encargado de borrar Idiomas.
     *
     * @param listaIdiomas Lista Idiomas que se van a borrar.
     */
    public void borrarIdiomas(List<Idiomas> listaIdiomas);

    /**
     * Método encargado de crear Idiomas.
     *
     * @param listaIdiomas Lista Idiomas que se van a crear.
     */
    public void crearIdiomas(List<Idiomas> listaIdiomas);

    /**
     * Método encargado de recuperar un Idioma dada su secuencia.
     *
     * @param secIdiomas Secuencia del Idioma.
     * @return Retorna un Idioma cuya secuencia coincida con el valor del
     * parámetro.
     */
    public Idiomas consultarIdioma(BigInteger secIdiomas);

    /**
     * Metodo encargado de traer todas los Idiomas de la base de datos.
     *
     * @return Lista de Idiomas.
     */
    public List<Idiomas> mostrarIdiomas();

    /**
     * Método encargado de contar la cantidad de IdiomasPersonas relacionadas
     * con un Idioma específico.
     *
     * @param secIdiomas Secuencia del Idiomas.
     * @return Retorna un número indicando la cantidad de IdiomasPersonas cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarBorradoIdiomasPersonas(BigInteger secIdiomas);
}
