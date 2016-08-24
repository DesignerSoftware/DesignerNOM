/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposReemplazos;
import java.math.BigInteger;
import java.util.List;

public interface AdministrarTiposReemplazosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposReemplazos.
     *
     * @param listaTiposReemplazos Lista TiposReemplazos que se van a
     * modificar.
     */
    public void modificarTiposReemplazos(List<TiposReemplazos> listaTiposReemplazos);

    /**
     * Método encargado de borrar TiposReemplazos.
     *
     * @param listaTiposReemplazos Lista TiposReemplazos que se van a borrar.
     */
    public void borrarTiposReemplazos(List<TiposReemplazos> listaTiposReemplazos);

    /**
     * Método encargado de crear TiposReemplazos.
     *
     * @param listaTiposReemplazos Lista TiposReemplazos que se van a crear.
     */
    public void crearTiposReemplazos(List<TiposReemplazos> listaTiposReemplazos);

    /**
     * Método encargado de recuperar las TiposReemplazos para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposReemplazos.
     */
    public List<TiposReemplazos> consultarTiposReemplazos();

    /**
     * Método encargado de recuperar un TipoReemplazo dada su secuencia.
     *
     * @param secTiposReemplazos Secuencia del TipoReemplazo
     * @return Retorna un TipoReemplazo.
     */
    public TiposReemplazos consultarTipoReemplazo(BigInteger secTiposReemplazos);

    /**
     * Método encargado de contar la cantidad de Encargaturas
     * relacionadas con un TipoReemplazo específica.
     *
     * @param secTiposReemplazos Secuencia del TipoReemplazo.
     * @return Retorna un número indicando la cantidad de Encargaturas
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarEncargaturasTipoReemplazo(BigInteger secTiposReemplazos);

    /**
     * Método encargado de contar la cantidad de ProgramacionesTiempos
     * relacionadas con un TipoReemplazo específica.
     *
     * @param secTiposReemplazos Secuencia del TipoReemplazo.
     * @return Retorna un número indicando la cantidad de ProgramacionesTiempos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarProgramacionesTiemposTipoReemplazo(BigInteger secTiposReemplazos);

    /**
     * Método encargado de contar la cantidad de Reemplazos
     * relacionadas con un TipoReemplazo específica.
     *
     * @param secTiposReemplazos Secuencia del TipoReemplazo.
     * @return Retorna un número indicando la cantidad de Reemplazos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarReemplazosTipoReemplazo(BigInteger secTiposReemplazos);

    /**
     * Método encargado de recuperar las NormasLaborales necesarias para la
     * lista de valores.
     *
     * @return Retorna una lista de NormasLaborales.
     */
    public List<TiposReemplazos> consultarLOVTiposReemplazos();

}
