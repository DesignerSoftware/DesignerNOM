/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposChequeos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposChequeosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposChequeos.
     *
     * @param listaTiposChequeos Lista TiposChequeos que se van a modificar.
     */
    public void modificarTiposChequeos(List<TiposChequeos> listaTiposChequeos);

    /**
     * Método encargado de borrar TiposChequeos.
     *
     * @param listaTiposChequeos Lista TiposChequeos que se van a borrar.
     */
    public void borrarTiposChequeos(List<TiposChequeos> listaTiposChequeos);

    /**
     * Método encargado de crear TiposChequeos.
     *
     * @param listaTiposChequeos Lista TiposChequeos que se van a crear.
     */
    public void crearTiposChequeos(List<TiposChequeos> listaTiposChequeos);

    /**
     * Método encargado de recuperar las TiposChequeos para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de TiposChequeos.
     */
    public List<TiposChequeos> consultarTiposChequeos();

    /**
     * Método encargado de recuperar una TiposChequeos dada su secuencia.
     *
     * @param secTiposChequeos Secuencia del TipoChequeo
     * @return Retorna un TiposChequeos.
     */
    public TiposChequeos consultarTipoChequeo(BigInteger secTiposChequeos);

    /**
     * Método encargado de contar la cantidad de ChequeosMedicos relacionadas
     * con un TipoChequeo específico.
     *
     * @param secTiposChequeos Secuencia del TipoChequeo.
     * @return Retorna un número indicando la cantidad de ChequeosMedicos cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarChequeosMedicosTipoChequeo(BigInteger secTiposChequeos);

    /**
     * Método encargado de contar la cantidad de TiposExamenesCargos
     * relacionadas con un TipoChequeo específico.
     *
     * @param secTiposChequeos Secuencia del TipoChequeo.
     * @return Retorna un número indicando la cantidad de TiposExamenesCargos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarTiposExamenesCargosTipoChequeo(BigInteger secTiposChequeos);
}
