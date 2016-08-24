/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Periodicidades;
import Entidades.Unidades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarPeriodicidadesInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar Periodicidades.
     *
     * @param listaPeriodicidades Lista Periodicidades que se van a modificar.
     */
    public void modificarPeriodicidades(List<Periodicidades> listaPeriodicidades);

    /**
     * Método encargado de borrar Periodicidades.
     *
     * @param listaPeriodicidades Lista Periodicidades que se van a borrar.
     */
    public void borrarPeriodicidades(List<Periodicidades> listaPeriodicidades);

    /**
     * Método encargado de crear Periodicidades.
     *
     * @param listaPeriodicidades Lista Periodicidades que se van a crear.
     */
    public void crearPeriodicidades(List<Periodicidades> listaPeriodicidades);

    /**
     * Método encargado de recuperar las Periodicidades para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de Periodicidades.
     */
    public List<Periodicidades> consultarPeriodicidades();

    /**
     * Método encargado de recuperar un Periodicidad dada su secuencia.
     *
     * @param secPeriodicidades Secuencia del Periodicidad
     * @return Retorna un Periodicidades.
     */
    public Periodicidades consultarPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de contar la cantidad de CPCompromisos relacionadas con
     * un Periodicidad específica.
     *
     * @param secPeriodicidades Secuencia del Periodicidad.
     * @return Retorna un número indicando la cantidad de CPCompromisos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarCPCompromisosPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de contar la cantidad de DetallesPeriodicidades
     * relacionadas con un Periodicidad específica.
     *
     * @param secPeriodicidades Secuencia del Periodicidad.
     * @return Retorna un número indicando la cantidad de DetallesPeriodicidades
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarDetallesPeriodicidadesPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de contar la cantidad de EersPrestamosDtos relacionadas
     * con un Periodicidad específica.
     *
     * @param secPeriodicidades Secuencia del Periodicidad.
     * @return Retorna un número indicando la cantidad de EersPrestamosDtos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarEersPrestamosDtosPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de contar la cantidad de Empresas relacionadas con un
     * Periodicidad específica.
     *
     * @param secPeriodicidades Secuencia del Periodicidad.
     * @return Retorna un número indicando la cantidad de Empresas cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarEmpresasPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de contar la cantidad de FormulasAseguradas relacionadas
     * con un Periodicidad específica.
     *
     * @param secPeriodicidades Secuencia del Periodicidad.
     * @return Retorna un número indicando la cantidad de FormulasAseguradas
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarFormulasAseguradasPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de contar la cantidad de FormulasContratos relacionadas
     * con un Periodicidad específica.
     *
     * @param secPeriodicidades Secuencia del Periodicidad.
     * @return Retorna un número indicando la cantidad de FormulasContratos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarFormulasContratosPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de contar la cantidad de GruposProvisiones relacionadas
     * con un Periodicidad específica.
     *
     * @param secPeriodicidades Secuencia del Periodicidad.
     * @return Retorna un número indicando la cantidad de GruposProvisiones cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarGruposProvisionesPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de contar la cantidad de Novedad relacionadas con un
     * Periodicidad específica.
     *
     * @param secPeriodicidades Secuencia del Periodicidad.
     * @return Retorna un número indicando la cantidad de Novedad cuya secuencia
     * coincide con el valor del parámetro.
     */
    public BigInteger contarNovedadesPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de contar la cantidad de ParametrosCambiosMasivos
     * relacionadas con un Periodicidad específica.
     *
     * @param secPeriodicidades Secuencia del Periodicidad.
     * @return Retorna un número indicando la cantidad de
     * ParametrosCambiosMasivos cuya secuencia coincide con el valor del
     * parámetro.
     */
    public BigInteger contarParametrosCambiosMasivosPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de contar la cantidad de VigenciasFormasPagos
     * relacionadas con un Periodicidad específica.
     *
     * @param secPeriodicidades Secuencia del Periodicidad.
     * @return Retorna un número indicando la cantidad de VigenciasFormasPagos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarVigenciasFormasPagosPeriodicidad(BigInteger secPeriodicidades);

    /**
     * Método encargado de recuperar las Unidades necesarias para la lista de
     * valores.
     *
     * @return Retorna una lista de Unidades.
     */
    public List<Unidades> consultarLOVUnidades();
}
