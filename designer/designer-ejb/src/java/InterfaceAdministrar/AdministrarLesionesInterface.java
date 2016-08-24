/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Lesiones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarLesionesInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar Lesiones.
     *
     * @param listaLesiones Lista Lesiones que se van a modificar.
     */
    public void modificarLesiones(List<Lesiones> listaLesiones);

    /**
     * Método encargado de borrar Lesiones.
     *
     * @param listaLesiones Lista Lesiones que se van a borrar.
     */
    public void borrarLesiones(List<Lesiones> listaLesiones);

    /**
     * Método encargado de crear Lesiones.
     *
     * @param listaLesiones Lista Lesiones que se van a crear.
     */
    public void crearLesiones(List<Lesiones> listaLesiones);

    /**
     * Método encargado de recuperar un Lesion dada su secuencia.
     *
     * @param secLesiones Secuencia de la Lesion.
     * @return Retorna una Lesion cuya secuencia coincida con el valor del
     * parámetro.
     */
    public Lesiones consultarLesion(BigInteger secLesiones);

    /**
     * Metodo encargado de traer todas las Lesiones de la base de datos.
     *
     * @return Lista de Lesiones.
     */
    public List<Lesiones> consultarLesiones();

    /**
     * Método encargado de contar la cantidad de DetallesLicensias relacionadas
     * con una Lesion específico.
     *
     * @param secLesiones Secuencia de la Lesion.
     * @return Retorna un número indicando la cantidad de DetallesLicensias cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarDetallesLicensiasLesion(BigInteger secLesiones);

    /**
     * Método encargado de contar la cantidad de SoAccidentesMedicos
     * relacionadas con una Lesion específico.
     *
     * @param secLesiones Secuencia de la Lesion.
     * @return Retorna un número indicando la cantidad de SoAccidentesDomesticos
     * cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarSoAccidentesDomesticosLesion(BigInteger secLesiones);
}
