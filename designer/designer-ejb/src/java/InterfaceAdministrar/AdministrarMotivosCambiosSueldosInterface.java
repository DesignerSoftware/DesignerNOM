/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MotivosCambiosSueldos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarMotivosCambiosSueldosInterface {

    	/**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de modificar MotivosCambiosSueldos.
     *
     * @param listaMotivosCambiosSueldos Lista MotivosCambiosSueldos que se van
     * a modificar.
     */
    public void modificarMotivosCambiosSueldos(List<MotivosCambiosSueldos> listaMotivosCambiosSueldos);

    /**
     * Método encargado de borrar MotivosCambiosSueldos.
     *
     * @param listaMotivosCambiosSueldos Lista MotivosCambiosSueldos que se van
     * a borrar.
     */
    public void borrarMotivosCambiosSueldos(List<MotivosCambiosSueldos> listaMotivosCambiosSueldos);

    /**
     * Método encargado de crear MotivosCambiosSueldos.
     *
     * @param listaMotivosCambiosSueldos Lista MotivosCambiosSueldos que se van
     * a crear.
     */
    public void crearMotivosCambiosSueldos(List<MotivosCambiosSueldos> listaMotivosCambiosSueldos);

    /**
     * Método encargado de recuperar las MotivosCambiosSueldos para una tabla de
     * la pantalla.
     *
     * @return Retorna una lista de MotivosCambiosSueldos.
     */
    public List<MotivosCambiosSueldos> consultarMotivosCambiosSueldos();

    /**
     * Método encargado de recuperar una MotivoCambioSueldo dada su secuencia.
     *
     * @param secMotivosCambiosSueldos Secuencia del MotivoCambioSueldo
     * @return Retorna una MotivosCambiosSueldos.
     */
    public MotivosCambiosSueldos consultarMotivoCambioCargo(BigInteger secMotivosCambiosSueldos);

    /**
     * Método encargado de consultar si existe una relacion entre una
     * MotivoCambioSueldos específica y algún VigenciasSueldos. Adémas de la
     * revisión, establece cuantas relaciones existen.
     *
     * @param secMotivosCambiosSueldos Secuencia del MotivoCambioSueldos.
     * @return Retorna el número de VigenciasSueldos relacionados con la
     * MotivoCambioCargo cuya secuencia coincide con el parámetro.
     */
    public BigInteger contarVigenciasSueldosMotivoCambioSueldo(BigInteger secMotivosCambiosSueldos);
}
