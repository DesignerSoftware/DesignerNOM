/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposFamiliares;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposFamiliaresInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposFamiliares.
     *
     * @param listaTiposFamiliares Lista TiposFamiliares que se van a modificar.
     */
    public void modificarTiposFamiliares(List<TiposFamiliares> listaTiposFamiliares);

    /**
     * Método encargado de borrar TiposFamiliares.
     *
     * @param listaTiposFamiliares Lista TiposFamiliares que se van a borrar.
     */
    public void borrarTiposFamiliares(List<TiposFamiliares> listaTiposFamiliares);

    /**
     * Método encargado de crear TiposFamiliares.
     *
     * @param listaTiposFamiliares Lista TiposFamiliares que se van a crear.
     */
    public void crearTiposFamiliares(List<TiposFamiliares> listaTiposFamiliares);

    /**
     * Método encargado de recuperar las TiposFamiliares para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposFamiliares.
     */
    public List<TiposFamiliares> consultarTiposFamiliares();

    /**
     * Método encargado de recuperar un TipoFamiliar dada su secuencia.
     *
     * @param secTiposFamiliares Secuencia del TipoFamiliar
     * @return Retorna un TiposFamiliares.
     */
    public TiposFamiliares consultarTipoExamen(BigInteger secTiposFamiliares);

    /**
     * Método encargado de contar la cantidad de HvReferencias relacionadas con
     * un TipoFamiliar específica.
     *
     * @param secTiposFamiliares Secuencia del TipoFamiliar.
     * @return Retorna un número indicando la cantidad de HvReferencias cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarHvReferenciasTipoFamiliar(BigInteger secTiposFamiliares);
}
