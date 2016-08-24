/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Enfermedades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarEnfermedadesInterface {

    /**
     * Método encargado de modificar Enfermedades.
     *
     * @param listEnfermedades Lista de las Enfermedades que se van a modificar.
     */
    public void modificarEnfermedades(List<Enfermedades> listEnfermedades);

    /**
     * Método encargado de borrar Enfermedades.
     *
     * @param listEnfermedades Lista de las Enfermedades que se van a borrar.
     */
    public void borrarEnfermedades(List<Enfermedades> listEnfermedades);

    /**
     * Método encargado de crear Enfermedades.
     *
     * @param listEnfermedades Lista de las Enfermedades que se van a crear.
     */
    public void crearEnfermedades(List<Enfermedades> listEnfermedades);

    /**
     * Método encargado de recuperar una Enfermedad dada su secuencia.
     *
     * @param secEnfermedades Secuencia de la Enfermedades.
     * @return Retorna una Enfermedad cuya secuencia coincida con el valor del
     * parámetro.
     */
    public Enfermedades consultarEnfermedad(BigInteger secEnfermedades);

    /**
     * Método encargado de recuperar todos las Enfermedades.
     *
     * @return Retorna una lista de Enfermedades.
     */
    public List<Enfermedades> consultarEnfermedades();

    /**
     * Método encargado de validar si existe una relación entre un Enfermedad
     * específica y algún AccidenteMedico. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secEnfermedades Secuencia de una Enfermedad.
     * @return Retorna el número de proyectos relacionados con una Enfermedad
     * cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarAusentimos(BigInteger secEnfermedades);

    /**
     * Método encargado de validar si existe una relación entre una Enfermedad
     * específica y algún DetallesLicencias. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secEnfermedades Secuencia de una Enfermedad.
     * @return Retorna el número de DetallesLicencias relacionados con una
     * Enfermedad cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarDetallesLicencias(BigInteger secEnfermedades);

    /**
     * Método encargado de validar si existe una relación entre una Enfermedad
     * específica y algún EnfermedadesPadecidas. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secEnfermedades Secuencia de una Enfermedad.
     * @return Retorna el número de EnfermedadesPadecidas relacionados con una
     * Enfermedad cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarEnfermedadesPadecidas(BigInteger secEnfermedades);

    /**
     * Método encargado de validar si existe una relación entre un Enfermedad
     * específica y algún SoAusentismos. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secEnfermedades Secuencia de una Enfermedad.
     * @return Retorna el número de SoAusentismos relacionados con una
     * Enfermedad cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarSoAusentismos(BigInteger secEnfermedades);

    /**
     * Método encargado de validar si existe una relación entre un Enfermedad
     * específica y algún SoRevisionesSistemas. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secEnfermedades Secuencia de una Enfermedad.
     * @return Retorna el número de SoRevisionesSistemas relacionados con una
     * Enfermedad cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarSoRevisionesSistemas(BigInteger secEnfermedades);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
