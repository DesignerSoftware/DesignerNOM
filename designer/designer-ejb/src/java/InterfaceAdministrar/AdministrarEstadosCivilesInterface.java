/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.EstadosCiviles;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarEstadosCivilesInterface {

    /**
     * Método encargado de modificar EstadosCiviles.
     *
     * @param listaEstadosCiviles Lista de las EstadosCiviles que se van a
     * modificar.
     */
    public void modificarEstadosCiviles(List<EstadosCiviles> listaEstadosCiviles);

    /**
     * Método encargado de borrar EstadosCiviles.
     *
     * @param listaEstadosCiviles Lista de las EstadosCiviles que se van a
     * borrar.
     */
    public void borrarEstadosCiviles(List<EstadosCiviles> listaEstadosCiviles);

    /**
     * Método encargado de crear EstadosCiviles.
     *
     * @param listaEstadosCiviles Lista de las EstadosCiviles que se van a
     * crear.
     */
    public void crearEstadosCiviles(List<EstadosCiviles> listaEstadosCiviles);

    /**
     * Método encargado de recuperar todos los Estados Civiles.
     *
     * @return Retorna una lista de Estados Civiles.
     */
    public List<EstadosCiviles> consultarEstadosCiviles();

    /**
     * Método encargado de validar si existe una relación entre un EstadoCivil
     * específica y algúna VigenciaEstadoCivil. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secEstadosCiviles Secuencia de un EstadoCivil.
     * @return Retorna el número de VigenciasEstadosCiviles relacionados con una
     * EstadoCivil cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarVigenciasEstadosCiviles(BigInteger secEstadosCiviles);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
