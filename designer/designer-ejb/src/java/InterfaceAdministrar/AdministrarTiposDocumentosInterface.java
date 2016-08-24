/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposDocumentos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarTiposDocumentosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar TiposDocumentos.
     *
     * @param listaTiposDocumentos Lista TiposDocumentos que se van a modificar.
     */
    public void modificarTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos);

    /**
     * Método encargado de borrar TiposDocumentos.
     *
     * @param listaTiposDocumentos Lista TiposDocumentos que se van a borrar.
     */
    public void borrarTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos);

    /**
     * Método encargado de crear TiposDocumentos.
     *
     * @param listaTiposDocumentos Lista TiposDocumentos que se van a crear.
     */
    public void crearTiposDocumentos(List<TiposDocumentos> listaTiposDocumentos);

    /**
     * Método encargado de recuperar las TiposDocumentos para un tabla de la
     * pantalla.
     *
     * @return Retorna un lista de TiposDocumentos.
     */
    public List<TiposDocumentos> consultarTiposDocumentos();

    /**
     * Método encargado de recuperar un TipoDocumento dada su secuencia.
     *
     * @param secTiposDocumentos Secuencia del TipoDocumento
     * @return Retorna un TipoDocumento.
     */
    public TiposDocumentos consultarTipoDocumento(BigInteger secTiposDocumentos);

    /**
     * Método encargado de contar la cantidad de Retirados relacionadas con un
     * TipoDocumento específico.
     *
     * @param secTiposDocumentos Secuencia del TipoDocumento.
     * @return Retorna un número indicando la cantidad de Retirados cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarCodeudoresTipoDocumento(BigInteger secTiposDocumentos);

    /**
     * Método encargado de contar la cantidad de Personas relacionadas con un
     * TipoDocumento específico.
     *
     * @param secTiposDocumentos Secuencia del TipoDocumento.
     * @return Retorna un número indicando la cantidad de Personas cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarPersonasTipoDocumento(BigInteger secTiposDocumentos);
}
