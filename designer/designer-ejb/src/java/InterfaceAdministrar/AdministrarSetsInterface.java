/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.Sets;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author AndresPineda
 */
public interface AdministrarSetsInterface {
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Metodo que obtiene los Sets de un Empleado especifico
     * @param secEmpleado Secuencia Empleado
     * @return listSE Lista de Sets de un Empleado
     */
    public List<Sets> SetsEmpleado(BigInteger secEmpleado);
    /**
     * Metodo que modifica los Sets en la tabla
     * @param listSetsModificadas Lista de Sets para ser modificados
     */
    public void modificarSets(List<Sets> listSetsModificadas);
    /**
     * Metodo borrador de los Sets
     * @param sets Objeto set a borrar
     */
    public void borrarSets(Sets sets);
    /**
     * Metodo creador de los Sets
     * @param sets Objeto set a crear
     */
    public void crearSets(Sets sets);
    /**
     * Busca un empleado por la secuencia
     * @param secuencia Secuencia del Empleado
     * @return empl Empleado que tiene la secuencia dada
     */
    public Empleados buscarEmpleado(BigInteger secuencia);
    /**
     * Cierra la sesion
     */
    public void salir();
    
}
