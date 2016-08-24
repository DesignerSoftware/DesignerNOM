/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.GruposTiposCC;
import Entidades.TiposCentrosCostos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposCentrosCostosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposCentrosCostos.
     *
     * @param listaTiposCentrosCostos Lista TiposCentrosCostos que se van a
     * modificar.
     */
    public void modificarTipoCentrosCostos(List<TiposCentrosCostos> listaTiposCentrosCostos);

    /**
     * Método encargado de borrar TiposCentrosCostos.
     *
     * @param listaTiposCentrosCostos Lista TiposCentrosCostos que se van a
     * borrar.
     */
    public void borrarTiposCentrosCostos(List<TiposCentrosCostos> listaTiposCentrosCostos);

    /**
     * Método encargado de crear TiposCentrosCostos.
     *
     * @param listaTiposCentrosCostos Lista TiposCentrosCostos que se van a
     * crear.
     */
    public void crearTiposCentrosCostos(List<TiposCentrosCostos> listaTiposCentrosCostos);

    /**
     * Método encargado de recuperar las TiposCentrosCostos para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposCentrosCostos.
     */
    public List<TiposCentrosCostos> consultarTiposCentrosCostos();

    /**
     * Método encargado de recuperar un TipoCentroCosto dada su secuencia.
     *
     * @param secTiposCentrosCostos Secuencia del TipoCentroCosto
     * @return Retorna un TipoCentroCosto.
     */
    public TiposCentrosCostos consultarTipoCentroCosto(BigInteger secTiposCentrosCostos);

    /**
     * Método encargado de contar la cantidad de CentrosCostos relacionadas con
     * un TipoCentroCosto específica.
     *
     * @param secTiposCentrosCostos Secuencia del TipoCentroCosto.
     * @return Retorna un número indicando la cantidad de CentrosCostos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarCentrosCostosTipoCentroCosto(BigInteger secTiposCentrosCostos);

    /**
     * Método encargado de contar la cantidad de VigenciasCuentas relacionadas
     * con un TipoCentroCosto específica.
     *
     * @param secTiposCentrosCostos Secuencia del TipoCentroCosto.
     * @return Retorna un número indicando la cantidad de VigenciasCuentas cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarVigenciasCuentasTipoCentroCosto(BigInteger secTiposCentrosCostos);

    /**
     * Método encargado de contar la cantidad de RiesgosProfesionales
     * relacionadas con un TipoCentroCosto específica.
     *
     * @param secTiposCentrosCostos Secuencia del TipoCentroCosto.
     * @return Retorna un número indicando la cantidad de RiesgosProfesionales
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarRiesgosProfesionalesTipoCentroCosto(BigInteger secTiposCentrosCostos);

    /**
     * Método encargado de recuperar las GruposTiposCC necesarias para la lista
     * de valores.
     *
     * @return Retorna una lista de GruposTiposCC.
     */
    public List<GruposTiposCC> consultarLOVGruposTiposCentrosCostos();
}
