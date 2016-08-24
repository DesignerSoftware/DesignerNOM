/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.ActualUsuario;
import Entidades.Empleados;
import Entidades.ReformasLaborales;
import Entidades.VigenciasReformasLaborales;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author AndresPineda
 */
public interface AdministrarVigenciasReformasLaboralesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Metodo que obtiene la lista de VigenciasReformasLaborales de un Empleado
     *
     * @param secEmpleado Secuencia del Empleado
     * @return Lista de VigenciasReformasLaborales de un Empleado
     */
    public List<VigenciasReformasLaborales> vigenciasReformasLaboralesEmpleado(BigInteger secEmpleado);

    /**
     * Metodo que modificas las VigenciasReformasLaborales
     *
     * @param listVRLModificadas Lista de VigenciasReformasLaborales para ser
     * modificadas
     */
    public void modificarVRL(List<VigenciasReformasLaborales> listVRLModificadas);

    /**
     * Metodo que borra las VigenciasReformasLaborales
     *
     * @param vigenciasReformasLaborales Lista de VigenciasReformasLaborales
     * para ser borradas
     */
    public void borrarVRL(VigenciasReformasLaborales vigenciasReformasLaborales);

    /**
     * Metodo que crea las VigenciasReformasLaborales
     *
     * @param vigenciasReformasLaborales Lista de VigenciasReformasLaborales
     * para ser creadas
     */
    public void crearVRL(VigenciasReformasLaborales vigenciasReformasLaborales);

    /**
     * Metodo que busca un Empleado por la secuencia
     *
     * @param secuencia Secuenai del Empleado
     * @return empl Empleado que cumple la secuencia
     */
    public Empleados buscarEmpleado(BigInteger secuencia);

    /**
     * Metodo que obtiene todas las ReformasLaborales
     *
     * @return listRF Lista de Reformas Laborales
     */
    public List<ReformasLaborales> reformasLaborales();

    /**
     * Metodo que cierra la sesion
     */
    public void salir();

    public ActualUsuario obtenerActualUsuario();

}
