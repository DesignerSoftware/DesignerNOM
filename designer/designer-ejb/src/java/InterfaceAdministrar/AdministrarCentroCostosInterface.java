/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.CentrosCostos;
import Entidades.Empresas;
import Entidades.TiposCentrosCostos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarCentroCostosInterface {

    /**
     * Metodo encargado de recuperar todas las empresas.
     *
     * @return Lista de Empresas
     */
    public List<Empresas> buscarEmpresas();
    //************************************CENTROS COSTROS**************************************

    /**
     * Metodo encargado de Modificar CentrosCostos.
     *
     * @param listaCentrosCostos Lista CentrosCostos que se van a modificar.
     */
    public void modificarCentroCostos(List<CentrosCostos> listaCentrosCostos);

    /**
     * Metodo encargado de borrar CentrosCostos.
     *
     * @param listaCentrosCostos Lista CentrosCostos que se van a borrar.
     */
    public void borrarCentroCostos(List<CentrosCostos> listaCentrosCostos);

    /**
     * Método encargado de crear CentrosCostos.
     *
     * @param listaCentrosCostos Lista CentrosCostos que se van a crear.
     */
    public void crearCentroCostos(List<CentrosCostos> listaCentrosCostos);

    /**
     * Metodo Encargado de traer los CentrosCentros de una Empresa Especifica y
     * el campo COMODIN es igual a 'N'.
     *
     * @param secEmpresa Secuencia de la Empresa.
     * @return Lista de CentrosCostos.
     */
    public List<CentrosCostos> consultarCentrosCostosPorEmpresa(BigInteger secEmpresa);

    /**
     * Método encargado de recuperar los TiposCentrosCostos necesarios para la
     * lista de valores.
     *
     * @return Retorna una lista de TiposCentrosCostos.
     */
    public List<TiposCentrosCostos> lovTiposCentrosCostos();

    /**
     * Método encargado de contar la cantidad de Comprobantes Contables
     * relacionadas con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de Comprobantes Contables
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorComprobantesContables(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de DetallesCCConsolidador
     * relacionadas con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de DetallesCCConsolidador
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorDetallesCCConsolidador(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de Detalles Contables relacionadas
     * con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de Detalles Contables
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorDetalleContable(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de Empresas relacionadas con un
     * CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de Empresas cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorEmpresas(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de Estructuras relacionadas con un
     * CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de Estructuras cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorEstructuras(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de InterConCondor relacionados con
     * un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de InterConCondor cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorInterConCondor(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de InterConDynamics relacionados
     * con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de InterConDynamics cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorInterConDynamics(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de InterConGeneral relacionados
     * con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de InterConGeneral cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorInterConGeneral(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de InterConHelisa relacionados con
     * un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de InterConHelisa cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorInterConHelisa(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de InterConSapbo relacionados con
     * un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de InterConSapbo cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorInterConSapbo(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de InterConSiigo relacionados con
     * un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de InterConSiigo cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorInterConSiigo(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de InterConTotal relacionados con
     * un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de InterConTotal cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorInterConTotal(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de NovedadesD relacionados con un
     * CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de NovedadesD cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorNovedadesD(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de NovedadesC relacionados con un
     * CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de NovedadesC cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorNovedadesC(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de ProcesosProductivos
     * relacionados con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de ProcesosProductivos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorProcesosProductivos(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de Proyecciones relacionados con
     * un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de Proyecciones cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorProyecciones(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de SolucionesNodosC relacionados
     * con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de SolucionesNodosC cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorSolucionesNodosC(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de SolucionesNodosD relacionados
     * con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de SolucionesNodosD cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorSolucionesNodosD(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de SoPanoramas relacionados con un
     * CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de SoPanoramas cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorSoPanoramas(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de Terceros relacionados con un
     * CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de Terceros cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorTerceros(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de UnidadesRegistradas
     * relacionados con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de UnidadesRegistradas
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorUnidadesRegistradas(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de VigenciasCuentasC relacionados
     * con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de VigenciasCuentasC cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorVigenciasCuentasC(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de VigenciasCuentasD relacionados
     * con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de VigenciasCuentasD cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorVigenciasCuentasD(BigInteger secCentroCosto);

    /**
     * Método encargado de contar la cantidad de VigenciasProrrateos
     * relacionados con un CentroCosto específico.
     *
     * @param secCentroCosto Secuencia del centro costo.
     * @return Retorna un número indicando la cantidad de VigenciasProrrateos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contadorVigenciasProrrateos(BigInteger secCentroCosto);
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

}
