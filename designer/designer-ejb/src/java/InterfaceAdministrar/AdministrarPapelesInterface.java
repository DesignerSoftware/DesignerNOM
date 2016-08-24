/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empresas;
import Entidades.Papeles;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarPapelesInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Metodo encargado de recuperar todas las empresas.
     *
     * @return Lista de Empresas
     */
    public List<Empresas> consultarEmpresas();

    /**
     * Método encargado de modificar Papeles.
     *
     * @param listaPapeles Lista Papeles que se van a modificar.
     */
    public void modificarPapeles(List<Papeles> listaPapeles);

    /**
     * Método encargado de borrar Papeles.
     *
     * @param listaPapeles Lista Papeles que se van a borrar.
     */
    public void borrarPapeles(List<Papeles> listaPapeles);

    /**
     * Método encargado de crear Papeles.
     *
     * @param listaPapeles Lista Papeles que se van a crear.
     */
    public void crearPapeles(List<Papeles> listaPapeles);

    /**
     * Metodo Encargado de traer los Papeles de una Empresa Especifica
     *
     * @param secEmpresa Secuencia de la Empresa.
     * @return Lista de Papeles.
     */
    public List<Papeles> consultarPapelesPorEmpresa(BigInteger secEmpresa);

    /**
     * Método encargado de contar la cantidad de VigenciasCargos relacionados
     * con un Papel específico.
     *
     * @param secPapeles Secuencia del Papel.
     * @return Retorna un número indicando la cantidad de VigenciasCargos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarVigenciasCargosPapel(BigInteger secPapeles);
}
