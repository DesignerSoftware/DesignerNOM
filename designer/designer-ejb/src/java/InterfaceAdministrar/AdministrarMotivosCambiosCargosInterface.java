/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.MotivosCambiosCargos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrator
 */
@Local
public interface AdministrarMotivosCambiosCargosInterface {

    
    /**
     * Método encargado de obtener la conexion con la que se ingreso.
     *
     * @param idSesion Identificador de la sesión para encontrar la conexión.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de modificar MotivosCambiosCargos.
     *
     * @param listaMotivosCambiosCargos Lista MotivosCambiosCargos que se van a
     * modificar.
     */
    public void modificarMotivosCambiosCargos(List<MotivosCambiosCargos> listaMotivosCambiosCargos);

    /**
     * Método encargado de borrar MotivosCambiosCargos.
     *
     * @param listaMotivosCambiosCargos Lista MotivosCambiosCargos que se van a
     * borrar.
     */
    public void borrarMotivosCambiosCargos(List<MotivosCambiosCargos> listaMotivosCambiosCargos);

    /**
     * Método encargado de crear MotivosCambiosCargos.
     *
     * @param listaMotivosCambiosCargos Lista MotivosCambiosCargos que se van a
     * crear.
     */
    public void crearMotivosCambiosCargos(List<MotivosCambiosCargos> listaMotivosCambiosCargos);

    /**
     * Método encargado de recuperar las MotivosCambiosCargos para una tabla de
     * la pantalla.
     *
     * @return Retorna una lista de MotivosCambiosCargos.
     */
    public List<MotivosCambiosCargos> consultarMotivosCambiosCargos();

    /**
     * Método encargado de recuperar una MotivoCambioCargo dada su secuencia.
     *
     * @param secMotivosCambiosCargos Secuencia de la Moneda
     * @return Retorna una MotivosCambiosCargos.
     */
    public MotivosCambiosCargos consultarMotivoCambioCargo(BigInteger secMotivosCambiosCargos);

    /**
     * Método encargado de consultar si existe una relacion entre una Moneda
     * específica y algún Proyecto. Adémas de la revisión, establece cuantas
     * relaciones existen.
     *
     * @param secMovitosCambiosCargos Secuencia del MotivoCambioCargo.
     * @return Retorna el número de VigenciasCargos relacionados con la
     * MotivoCambioCargo cuya secuencia coincide con el parámetro.
     */
    public BigInteger contarVigenciasCargosMotivoCambioCargo(BigInteger secMovitosCambiosCargos);
}
