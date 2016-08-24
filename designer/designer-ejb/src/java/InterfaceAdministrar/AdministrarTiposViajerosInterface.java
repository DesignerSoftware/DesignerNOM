/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Tiposviajeros;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposViajerosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TipoViajeros.
     *
     * @param listaTiposViajeros Lista TipoViajeros que se van a modificar.
     */
    public void modificarTiposViajeros(List<Tiposviajeros> listaTiposViajeros);

    /**
     * Método encargado de borrar TipoViajeros.
     *
     * @param listaTiposViajeros Lista TipoViajeros que se van a borrar.
     */
    public void borrarTiposViajeros(List<Tiposviajeros> listaTiposViajeros);

    /**
     * Método encargado de crear TipoViajeros.
     *
     * @param listaTiposViajeros Lista TipoViajeros que se van a crear.
     */
    public void crearTiposViajeros(List<Tiposviajeros> listaTiposViajeros);

    /**
     * Método encargado de recuperar las TipoViajeros para un tabla de la
     * pantalla.
     *
     * @return Retorna un lista de TipoViajeros.
     */
    public List<Tiposviajeros> consultarTiposViajeros();

    /**
     * Método encargado de recuperar un TipoViajero dada su secuencia.
     *
     * @param secTiposViajeros Secuencia del TipoViajero
     * @return Retorna un TipoViajero.
     */
    public Tiposviajeros consultarTipoViajero(BigInteger secTiposViajeros);

    /**
     * Método encargado de contar la cantidad de Escalafones relacionadas con un
     * TipoViajero específico.
     *
     * @param secTiposViajeros Secuencia del TipoViajero.
     * @return Retorna un número indicando la cantidad de TiposLegalizaciones
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarTiposLegalizaciones(BigInteger secTiposViajeros);

    /**
     * Método encargado de contar la cantidad de Escalafones relacionadas con un
     * TipoViajero específico.
     *
     * @param secTiposViajeros Secuencia del TipoViajero.
     * @return Retorna un número indicando la cantidad de VigenciasViajeros cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarVigenciasViajeros(BigInteger secTiposViajeros);
}
