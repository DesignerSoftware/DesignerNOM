/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposAccidentes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposAccidentesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposAccidentes.
     *
     * @param listaTiposAccidentes Lista TiposAccidentes que se van a modificar.
     */
    public void modificarTiposAccidentes(List<TiposAccidentes> listaTiposAccidentes);

    /**
     * Método encargado de borrar TiposAccidentes.
     *
     * @param listaTiposAccidentes Lista TiposAccidentes que se van a borrar.
     */
    public void borrarTiposAccidentes(List<TiposAccidentes> listaTiposAccidentes);

    /**
     * Método encargado de crear TiposAccidentes.
     *
     * @param listaTiposAccidentes Lista TiposAccidentes que se van a crear.
     */
    public void crearTiposAccidentes(List<TiposAccidentes> listaTiposAccidentes);

    /**
     * Método encargado de recuperar las TiposAccidentes para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposAccidentes.
     */
    public List<TiposAccidentes> consultarTiposAccidentes();

    /**
     * Método encargado de recuperar una TiposAccidentes dada su secuencia.
     *
     * @param secTiposAccidentes Secuencia del TipoAccidente
     * @return Retorna un TiposAccidentes.
     */
    public TiposAccidentes consultarTiposAccidentes(BigInteger secTiposAccidentes);

    /**
     * Método encargado de contar la cantidad de SoAccidentesMedicos
     * relacionadas con un TipoAccidente específico.
     *
     * @param secTiposAccidentes Secuencia del TipoAccidente.
     * @return Retorna un número indicando la cantidad de SoAccidentesDomesticos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarSoAccidentesMedicosTipoAccidente(BigInteger secTiposAccidentes);

    /**
     * Método encargado de contar la cantidad de SoAccidentesMedicos
     * relacionadas con un TipoAccidente específico.
     *
     * @param secTiposAccidentes Secuencia del TipoAccidente.
     * @return Retorna un número indicando la cantidad de Accidentes cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarAccidentesTipoAccidente(BigInteger secTiposAccidentes);
}
