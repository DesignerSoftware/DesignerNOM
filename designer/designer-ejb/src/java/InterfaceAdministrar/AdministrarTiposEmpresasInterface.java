/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposEmpresas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposEmpresasInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposEmpresas.
     *
     * @param listaTiposEmpresas Lista TiposEmpresas que se van a modificar.
     */
    public void modificarTiposEmpresas(List<TiposEmpresas> listaTiposEmpresas);

    /**
     * Método encargado de borrar TiposEmpresas.
     *
     * @param listaTiposEmpresas Lista TiposEmpresas que se van a borrar.
     */
    public void borrarTiposEmpresas(List<TiposEmpresas> listaTiposEmpresas);

    /**
     * Método encargado de crear TiposEmpresas.
     *
     * @param listaTiposEmpresas Lista TiposEmpresas que se van a crear.
     */
    public void crearTiposEmpresas(List<TiposEmpresas> listaTiposEmpresas);

    /**
     * Método encargado de recuperar las TiposEmpresas para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposEmpresas.
     */
    public List<TiposEmpresas> consultarTiposEmpresas();

    /**
     * Método encargado de recuperar un TipoEmpresa dada su secuencia.
     *
     * @param secTipoEmpresa Secuencia del TipoEmpresa
     * @return Retorna un TiposEmpresas.
     */
    public TiposEmpresas consultarTipoEmpresa(BigInteger secTipoEmpresa);

    /**
     * Método encargado de contar la cantidad de SueldosMercados relacionadas con
     * un TipoEmpresa específica.
     *
     * @param secTiposEmpresas Secuencia del TipoEmpresa.
     * @return Retorna un número indicando la cantidad de SueldosMercados cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarSueldosMercadosTipoEmpresa(BigInteger secTiposEmpresas);
}
