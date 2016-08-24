/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ClasesCategorias;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarClasesCategoriasInterface {

    /**
     * Método encargado de modificar ClasesCategorias.
     *
     * @param listaClasesCategorias Lista ClasesCategorias que se van a
     * modificar.
     */
    public void modificarClasesCategorias(List<ClasesCategorias> listaClasesCategorias);

    /**
     * Método encargado de borrar ClasesCategorias.
     *
     * @param listaClasesCategorias Lista ClasesCategorias que se van a borrar.
     */
    public void borrarClasesCategorias(List<ClasesCategorias> listaClasesCategorias);

    /**
     * Método encargado de crear ClasesCategorias.
     *
     * @param listaClasesCategorias Lista ClasesCategorias que se van a crear.
     */
    public void crearClasesCategorias(List<ClasesCategorias> listaClasesCategorias);

    /**
     * Método encargado de recuperar las ClasesCategorias para un tabla de la
     * pantalla.
     *
     * @return Retorna un lista de ClasesCategorias.
     */
    public List<ClasesCategorias> consultarClasesCategorias();

    /**
     * Método encargado de recuperar un ClaseCategoria dada su secuencia.
     *
     * @param secClasesCategorias Secuencia del ClaseCategoria
     * @return Retorna un ClaseCategoria.
     */
    public ClasesCategorias consultarClaseCategoria(BigInteger secClasesCategorias);

    /**
     * Método encargado de contar la cantidad de Categoria relacionadas con un
     * ClaseCategoria específico.
     *
     * @param secClasesCategorias Secuencia del ClaseCategoria.
     * @return Retorna un número indicando la cantidad de Categoria cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarCategoriaClaseCategoria(BigInteger secClasesCategorias);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
