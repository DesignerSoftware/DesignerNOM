/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Ciudades;
import Entidades.Empresas;
import Entidades.SucursalesPila;
import Entidades.UbicacionesGeograficas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarUbicacionesGeograficasInterface {

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
     * Metodo encargado de Modificar UbicacionesGeograficas.
     *
     * @param listaUbicacionesGeograficas Lista UbicacionesGeograficas que se
     * van a modificar.
     */
    public void modificarUbicacionesGeograficas(List<UbicacionesGeograficas> listaUbicacionesGeograficas);

    /**
     * Metodo encargado de borrar UbicacionesGeograficas.
     *
     * @param listaUbicacionesGeograficas Lista UbicacionesGeograficas que se
     * van a borrar.
     */
    public void borrarUbicacionesGeograficas(List<UbicacionesGeograficas> listaUbicacionesGeograficas);

    /**
     * Método encargado de crear UbicacionesGeograficas.
     *
     * @param listaUbicacionesGeograficas Lista UbicacionesGeograficas que se
     * van a crear.
     */
    public void crearUbicacionesGeograficas(List<UbicacionesGeograficas> listaUbicacionesGeograficas);

    /**
     * Metodo Encargado de traer los CentrosCentros de una Empresa Especifica
     *
     * @param secEmpresa Secuencia de la Empresa.
     * @return Lista de UbicacionesGeograficas.
     */
    public List<UbicacionesGeograficas> consultarUbicacionesGeograficasPorEmpresa(BigInteger secEmpresa);

    /**
     * Método encargado de recuperar los Ciudades necesarios para la lista de
     * valores.
     *
     * @return Retorna una lista de Ciudades.
     */
    public List<Ciudades> lovCiudades();

    /**
     * *
     * Método encargado de recuperar los SucursalesPila necesarios para la lista
     * de valores de una empresa especifica.
     *
     *
     * @param secEmpresa Secuencia de la empresa
     * @return Retorna una lista de SucursalesPila.
     */
    public List<SucursalesPila> lovSucursalesPilaPorEmpresa(BigInteger secEmpresa);

    /**
     * Método encargado de contar la cantidad de AfiliacionesEntidades
     * relacionadas con un UbicacionesGeograficas específica.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeograficas.
     * @return Retorna un número indicando la cantidad de UbicacionesGeograficas
     * cuya secUbicacionesGeograficas coincide con el valor del parámetro.
     */
    public BigInteger contarAfiliacionesEntidadesUbicacionGeografica(BigInteger secUbicacionesGeograficas);

    /**
     * Método encargado de contar la cantidad de Inspecciones relacionadas con
     * un UbicacionesGeograficas específica.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeograficas.
     * @return Retorna un número indicando la cantidad de UbicacionesGeograficas
     * cuya secUbicacionesGeograficas coincide con el valor del parámetro.
     */
    public BigInteger contarInspeccionesUbicacionGeografica(BigInteger secUbicacionesGeograficas);

    /**
     * Método encargado de contar la cantidad de ParametrosInformes relacionadas
     * con un UbicacionesGeograficas específica.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeograficas.
     * @return Retorna un número indicando la cantidad de UbicacionesGeograficas
     * cuya secUbicacionesGeograficas coincide con el valor del parámetro.
     */
    public BigInteger contarParametrosInformesUbicacionGeografica(BigInteger secUbicacionesGeograficas);

    /**
     * Método encargado de contar la cantidad de Revisiones relacionadas con un
     * UbicacionesGeograficas específica.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeograficas.
     * @return Retorna un número indicando la cantidad de UbicacionesGeograficas
     * cuya secUbicacionesGeograficas coincide con el valor del parámetro.
     */
    public BigInteger contarRevisionesUbicacionGeografica(BigInteger secUbicacionesGeograficas);

    /**
     * Método encargado de contar la cantidad de VigenciasUbicaciones
     * relacionadas con un UbicacionesGeograficas específica.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeograficas.
     * @return Retorna un número indicando la cantidad de VigenciasUbicaciones
     * cuya secUbicacionesGeograficas coincide con el valor del parámetro.
     */
    public BigInteger contarVigenciasUbicacionesUbicacionGeografica(BigInteger secUbicacionesGeograficas);
}
