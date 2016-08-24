/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.PartesCuerpo;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarPartesCuerpoInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar PartesCuerpo.
     *
     * @param listaPartesCuerpos Lista PartesCuerpo que se van a modificar.
     */
    public void modificarPartesCuerpo(List<PartesCuerpo> listaPartesCuerpos);

    /**
     * Método encargado de borrar PartesCuerpo.
     *
     * @param listaPartesCuerpos Lista PartesCuerpo que se van a borrar.
     */
    public void borrarPartesCuerpo(List<PartesCuerpo> listaPartesCuerpos);

    /**
     * Método encargado de crear PartesCuerpo.
     *
     * @param listaPartesCuerpos Lista PartesCuerpo que se van a crear.
     */
    public void crearPartesCuerpo(List<PartesCuerpo> listaPartesCuerpos);

    /**
     * Método encargado de recuperar las PartesCuerpo para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de PartesCuerpo.
     */
    public List<PartesCuerpo> consultarPartesCuerpo();

    /**
     * Método encargado de recuperar una PartesCuerpo dada su secuencia.
     *
     * @param secPartesCuerpo Secuencia de la ParteCuerpo
     * @return Retorna una PartesCuerpo.
     */
    public PartesCuerpo consultarParteCuerpo(BigInteger secPartesCuerpo);

    /**
     * Método encargado de contar la cantidad de SoAccidentesMedicos
     * relacionadas con una ParteCuerpo específica.
     *
     * @param secPartesCuerpo Secuencia de la ParteCuerpo.
     * @return Retorna un número indicando la cantidad de SoAccidentesMedicos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarSoAccidentesMedicosParteCuerpo(BigInteger secPartesCuerpo);

    /**
     * Método encargado de contar la cantidad de DetallesExamenes relacionadas
     * con una ParteCuerpo específica.
     *
     * @param secPartesCuerpo Secuencia de la ParteCuerpo.
     * @return Retorna un número indicando la cantidad de DetallesExamenes cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarDetallesExamenesParteCuerpo(BigInteger secPartesCuerpo);

    /**
     * Método encargado de contar la cantidad de SoDetallesRevisiones
     * relacionadas con una ParteCuerpo específica.
     *
     * @param secPartesCuerpo Secuencia de la ParteCuerpo.
     * @return Retorna un número indicando la cantidad de SoDetallesRevisiones
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarSoDetallesRevisionesParteCuerpo(BigInteger secPartesCuerpo);
}
