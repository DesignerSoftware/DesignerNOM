/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MotivosPrestamos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarMotivosPrestamosInterface {
	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar MotivosPrestamos.
     *
     * @param listaMotivosPrestamos Lista MotivosPrestamos que se van a modificar.
     */
    public void modificarMotivosPrestamos(List<MotivosPrestamos> listaMotivosPrestamos);

    /**
     * Método encargado de borrar MotivosPrestamos.
     *
     * @param listaMotivosPrestamos Lista MotivosPrestamos que se van a borrar.
     */
    public void borrarMotivosPrestamos(List<MotivosPrestamos> listaMotivosPrestamos);

    /**
     * Método encargado de crear MotivosPrestamos.
     *
     * @param listaMotivosPrestamos Lista MotivosPrestamos que se van a crear.
     */
    public void crearMotivosPrestamos(List<MotivosPrestamos> listaMotivosPrestamos);

    /**
     * Método encargado de recuperar las MotivosPrestamos para una tabla de la
     * pantalla.
     *
     * @return Retorna una lista de MotivosPrestamos.
     */
    public List<MotivosPrestamos> mostrarMotivosPrestamos();

    /**
     * Método encargado de recuperar una MotivosPrestamos dada su secuencia.
     *
     * @param secMotivosPrestamos Secuencia del MotivosPrestamos
     * @return Retorna una MotivosPrestamos.
     */
    public MotivosPrestamos mostrarMotivoPrestamo(BigInteger secMotivosPrestamos);

    /**
     * Método encargado de consultar si existe una relacion entre una
     * MotivosPrestamos específica y algún EersPrestamos. Adémas de la revisión,
     * establece cuantas relaciones existen.
     *
     * @param secMotivosPrestamos Secuencia del MotivosPrestamos.
     * @return Retorna el número de EersPrestamos relacionados con la
     * MotivoCambioCargo cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarEersPrestamosMotivoPrestamo(BigInteger secMotivosPrestamos);
}
