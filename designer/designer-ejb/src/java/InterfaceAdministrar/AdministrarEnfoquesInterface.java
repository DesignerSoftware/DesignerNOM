/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Enfoques;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarEnfoquesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar Enfoques.
     *
     * @param listaEnfoques Lista de las Enfoques que se van a modificar.
     */
    public void modificarEnfoques(List<Enfoques> listaEnfoques);

    /**
     * Método encargado de borrar Enfoques.
     *
     * @param listaEnfoques Lista de las Enfoques que se van a borrar.
     */
    public void borrarEnfoques(List<Enfoques> listaEnfoques);

    /**
     * Método encargado de crear Enfoques.
     *
     * @param listaEnfoques Lista de las Enfoques que se van a crear.
     */
    public void crearEnfoques(List<Enfoques> listaEnfoques);

    /**
     * Método encargado de recuperar un Enfoque dada su secuencia.
     *
     * @param secEnfoques Secuencia de los Enfoques.
     * @return Retorna una Enfermedad cuya secuencia coincida con el valor del
     * parámetro.
     */
    public Enfoques consultarEnfoque(BigInteger secEnfoques);

    /**
     * Método encargado de recuperar todos los Enfoques.
     *
     * @return Retorna una lista de Enfoques.
     */
    public List<Enfoques> consultarEnfoques();

    /**
     * Método encargado de validar si existe una relación entre un Enfoque
     * específica y algún TiposDetalles. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secEnfoques Secuencia de una Enfoque.
     * @return Retorna el número de TiposDetalles relacionados con una Enfoque
     * cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarTiposDetalles(BigInteger secEnfoques);
}
