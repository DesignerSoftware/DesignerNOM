/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Grupostiposentidades;
import Entidades.TiposEntidades;
import Entidades.VigenciasAfiliaciones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposEntidadesInterface {
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
 /**
     * Método encargado de modificar TiposEntidades.
     *
     * @param listaTiposEntidades Lista TiposEntidades que se van a modificar.
     */
    public void modificarTipoEntidad(List<TiposEntidades> listaTiposEntidades);
/**
     * Método encargado de borrar TiposEntidades.
     *
     * @param listaTiposEntidades Lista TiposEntidades que se van a borrar.
     */
    public void borrarTipoEntidad(List<TiposEntidades> listaTiposEntidades);

    /**
     * Método encargado de crear TiposEntidades.
     *
     * @param listaTiposEntidades Lista TiposEntidades que se van a crear.
     */
    public void crearTipoEntidad(List<TiposEntidades> listaTiposEntidades);

/**
     * Método encargado de recuperar las TiposEntidades para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposEntidades.
     */
    public List<TiposEntidades> consultarTiposEntidades();

    /**
     * Método encargado de recuperar un TipoEntidad dada su secuencia.
     *
     * @param secTiposEntidades Secuencia del TipoEntidad
     * @return Retorna un TiposEntidades.
     */
    public TiposEntidades consultarTipoEntidad(BigInteger secTiposEntidades);

/**
     * Método encargado de contar la cantidad de VigenciasAfiliaciones relacionadas con
     * un TipoEntidad específica.
     *
     * @param secTiposEntidades Secuencia del TipoEntidad.
     * @return Retorna un número indicando la cantidad de VigenciasAfiliaciones cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarVigenciasAfiliacionesTipoEntidad(BigInteger secTiposEntidades);
/**
     * Método encargado de contar la cantidad de FormulasContratosEntidades relacionadas con
     * un TipoEntidad específica.
     *
     * @param secTiposEntidades Secuencia del TipoEntidad.
     * @return Retorna un número indicando la cantidad de FormulasContratosEntidades cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarFormulasContratosEntidadesTipoEntidad(BigInteger secTiposEntidades);
        /**
     * Método encargado de recuperar las Grupostiposentidades necesarias para la
     * lista de valores.
     *
     * @return Retorna una lista de GruposTiposEntidades.
     */
    public List<Grupostiposentidades> consultarLOVGruposTiposEntidades();
}
