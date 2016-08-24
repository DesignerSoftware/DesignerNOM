/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvReferencias;
import Entidades.Personas;
import Entidades.TiposFamiliares;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarHvReferenciasInterface {
    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de modificar HvReferencias.
     *
     * @param listaHvReferencias Lista HvReferencias que se van a modificar.
     */
    public void modificarHvReferencias(List<HvReferencias> listaHvReferencias);

    /**
     * Método encargado de borrar HvReferencias.
     *
     * @param listaHvReferencias Lista HvReferencias que se van a borrar.
     */
    public void borrarHvReferencias(List<HvReferencias> listaHvReferencias);

    /**
     * Método encargado de crear HvReferencias.
     *
     * @param listaHvReferencias Lista HvReferencias que se van a crear.
     */
    public void crearHvReferencias(List<HvReferencias> listaHvReferencias);

    /**
     * Metodo Encargado de traer las HvReferencias de un Empleado Especifico.
     * donde el campo tipo es igual a ='PERSONALES'
     *
     * @param secEmpleado Secuencia del Empleado.
     * @return Lista de HvReferencias.
     */
    public List<HvReferencias> consultarHvReferenciasPersonalesPorPersona(BigInteger secEmpleado);

    /**
     * Método encargado de recuperar un HvEntrevista dada su secuencia.
     *
     * @param secHvReferencias Secuencia del HvEntrevista.
     * @return Retorna un HvReferencias cuya secuencia coincida con el valor del
     * parámetro.
     */
    public HvReferencias consultarHvReferencia(BigInteger secHvReferencias);

    /**
     * Metodo Encargado de traer las HvReferencias de un Empleado Especifico.
     * donde el campo tipo es igual a ='FAMILIARES'
     *
     * @param secEmpleado Secuencia del Empleado.
     * @return Lista de HvReferencias.
     */
    public List<HvReferencias> consultarHvReferenciasFamiliaresPorPersona(BigInteger secEmpleado);

    /**
     * Metodo encargado de traer las HVHojasDeVida de un empleado especifico
     *
     * @param secEmpleado
     * @return lista HVHojasDeVida
     */
    public List<HVHojasDeVida> consultarHvHojasDeVida(BigInteger secEmpleado);

    /**
     * Método encargado de recuperar los TiposFamiliares necesarias para la
     * lista de valores.
     *
     * @return Retorna una lista de TiposFamiliares.
     */
    public List<TiposFamiliares> consultarLOVTiposFamiliares();

    /**
     * *
     * Metodo encargado de buscar un Empleado especifico
     *
     * @param secEmpleado Secuencia del Empleado
     * @return Empleado.
     */
    public Personas consultarPersonas(BigInteger secEmpleado);
    
    public Empleados empleadoActual(BigInteger secuencia);
}
