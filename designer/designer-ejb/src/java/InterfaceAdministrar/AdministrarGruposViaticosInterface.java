/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.GruposViaticos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarGruposViaticosInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar GruposViaticos.
     *
     * @param listaGruposViativos Lista GruposViaticos que se van a modificar.
     */
    public void modificarGruposViaticos(List<GruposViaticos> listaGruposViativos);

    /**
     * Método encargado de borrar GruposViaticos.
     *
     * @param listaGruposViativos Lista GruposViaticos que se van a borrar.
     */
    public void borrarGruposViaticos(List<GruposViaticos> listaGruposViativos);

    /**
     * Método encargado de crear GruposViaticos.
     *
     * @param listaGruposViativos Lista GruposViaticos que se van a crear.
     */
    public void crearGruposViaticos(List<GruposViaticos> listaGruposViativos);

    /**
     * Método encargado de recuperar un GrupoViatico dada su secuencia.
     *
     * @param secGruposViaticos Secuencia del GrupoViatico.
     * @return Retorna un GruposViaticos cuya secuencia coincida con el valor
     * del parámetro.
     */
    public GruposViaticos consultarGrupoViatico(BigInteger secGruposViaticos);

    /**
     * Metodo encargado de traer todas los GruposViaticos de la base de datos.
     *
     * @return Lista de GruposViaticos.
     */
    public List<GruposViaticos> consultarGruposViaticos();

    /**
     * Método encargado de contar la cantidad de Cargos relacionadas con un
     * GrupoViatico específico.
     *
     * @param secGruposViaticos Secuencia del GruposViaticos.
     * @return Retorna un número indicando la cantidad de Cargos cuya secuencia
     * coincide con el valor del parámetro.
     */
    public BigInteger verificarCargos(BigInteger secGruposViaticos);

    /**
     * Método encargado de contar la cantidad de Plantas relacionadas con un
     * GrupoViatico específico.
     *
     * @param secGruposViaticos Secuencia del GruposViaticos.
     * @return Retorna un número indicando la cantidad de GruposViaticos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarPlantas(BigInteger secGruposViaticos);

    /**
     * Método encargado de contar la cantidad de TablasViaticos relacionadas con
     * un GrupoViatico específico.
     *
     * @param secGruposViaticos Secuencia del GruposViaticos.
     * @return Retorna un número indicando la cantidad de TablasViaticos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarTablasViaticos(BigInteger secGruposViaticos);

    /**
     * Método encargado de contar la cantidad de EersViaticos relacionadas con
     * un GrupoViatico específico.
     *
     * @param secGruposViaticos Secuencia del GruposViaticos.
     * @return Retorna un número indicando la cantidad de EersViaticos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger verificarEersViaticos(BigInteger secGruposViaticos);
}
