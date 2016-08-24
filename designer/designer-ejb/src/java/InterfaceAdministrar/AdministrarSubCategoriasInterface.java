/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.SubCategorias;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarSubCategoriasInterface {
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
/**
     * Método encargado de modificar SubCategorias.
     *
     * @param listaSubCategorias Lista SubCategorias que se van a
     * modificar.
     */
    public void modificarSubCategorias(List<SubCategorias> listaSubCategorias);
/**
     * Método encargado de borrar SubCategorias.
     *
     * @param listaSubCategorias Lista SubCategorias que se van a borrar.
     */
    public void borrarSubCategorias(List<SubCategorias> listaSubCategorias);
/**
     * Método encargado de crear SubCategorias.
     *
     * @param listaSubCategorias Lista SubCategorias que se van a crear.
     */
    public void crearSubCategorias(List<SubCategorias> listaSubCategorias);
/**
     * Método encargado de recuperar las SubCategorias para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de SubCategorias.
     */
    public List<SubCategorias> consultarSubCategorias();
/**
     * Método encargado de recuperar una SubCategoria dada su secuencia.
     *
     * @param secSubCategorias Secuencia de la SubCategoria
     * @return Retorna una SubCategoria.
     */
    public SubCategorias consultarSubCategoria(BigInteger secSubCategorias);
 /**
     * Método encargado de contar la cantidad de Escalafones relacionadas con una
     * SubCategoria específico.
     *
     * @param secSubCategorias Secuencia de la SubCategoria.
     * @return Retorna un número indicando la cantidad de Escalafones cuya secuencia
     * coincide con el valor del parámetro.
     */
    public BigInteger contarEscalafones(BigInteger secSubCategorias);
}
