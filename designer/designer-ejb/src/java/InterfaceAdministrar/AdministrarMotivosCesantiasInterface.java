/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MotivosCesantias;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarMotivosCesantiasInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar MotivosCesancias.
     *
     * @param listaMotivosCesancias Lista MotivosCesancias que se van a
     * modificar.
     */
    public void modificarMotivosCesantias(List<MotivosCesantias> listaMotivosCesancias);

    /**
     * Método encargado de borrar MotivosCesancias.
     *
     * @param listaMotivosCesancias Lista MotivosCesancias que se van a borrar.
     */
    public void borrarMotivosCesantias(List<MotivosCesantias> listaMotivosCesancias);

    /**
     * Método encargado de crear MotivosCesancias.
     *
     * @param listaMotivosCesancias Lista MotivosCesancias que se van a crear.
     */
    public void crearMotivosCesantias(List<MotivosCesantias> listaMotivosCesancias);

    /**
     * Método encargado de recuperar las MotivosCesancias para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de MotivosCesancias.
     */
    public List<MotivosCesantias> consultarMotivosCesantias();

    /**
     * Método encargado de recuperar una MotivoCesantia dada su secuencia.
     *
     * @param secMotivosCesantias Secuencia del MotivoCesantia
     * @return Retorna una MotivosCesancias.
     */
    public MotivosCesantias consultarMotivoCesantia(BigInteger secMotivosCesantias);

    /**
     * Método encargado de consultar si existe una relacion entre una
     * MotivoCesantia específica y algúna NovedadesSistemas. Adémas de la
     * revisión, establece cuantas relaciones existen.
     *
     * @param secMotivosCesantias Secuencia del MotivoCesantia.
     * @return Retorna el número de NovedadesSistemas relacionados con la
     * MotivoCambioCargo cuya secuencia coincide con el parámetro.
     */
    public BigInteger contarNovedadesSistemasMotivoCesantia(BigInteger secMotivosCesantias);
}
