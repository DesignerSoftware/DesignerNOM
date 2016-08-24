/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.MotivosCambiosSueldos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'MotivosCambiosSueldos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaMotivosCambiosSueldosInterface {
    
    /**
     * Método encargado de insertar un MotivoCambioSueldo en la base de datos.
     * @param motivosCambiosSueldos MotivoCambioSueldo que se quiere crear.
     */
    public void crear(EntityManager em, MotivosCambiosSueldos motivosCambiosSueldos);
    /**
     * Método encargado de modificar un MotivoCambioSueldo de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param motivosCambiosSueldos MotivoCambioSueldo con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, MotivosCambiosSueldos motivosCambiosSueldos);
    /**
     * Método encargado de eliminar de la base de datos el MotivoCambioSueldo que entra por parámetro.
     * @param motivosCambiosSueldos MotivoCambioSueldo que se quiere eliminar.
     */
    public void borrar(EntityManager em, MotivosCambiosSueldos motivosCambiosSueldos);
    /**
     * Método encargado de buscar todos los MotivosCambiosSueldos existentes en la base de datos.
     * @return Retorna una lista de MotivosCambiosSueldos.
     */
    public List<MotivosCambiosSueldos> buscarMotivosCambiosSueldos(EntityManager em);
    /**
     * Método encargado de buscar el MotivoCambioSueldo con la secMotivosCambiosSueldos dada por parámetro.
     * @param secMotivosCambiosSueldos Secuencia del MotivoCambioSueldo que se quiere encontrar.
     * @return Retorna el MotivoCambioSueldo identificado con la secMotivosCambiosSueldos dada por parámetro.
     */
    public MotivosCambiosSueldos buscarMotivoCambioSueldoSecuencia(EntityManager em, BigInteger secMotivosCambiosSueldos);
    /**
     * Método encargado de verificar si hay al menos una VigenciaSueldo asociada a un MotivoCambioSueldo.
     * @param secMotivosCambiosSueldos Secuencia del MotivoCambioSueldo
     * @return Retorna un valor mayor a cero si existe alguna VigenciaSueldo asociada a un MotivoCambioSueldo.
     */
    public BigInteger verificarBorradoVigenciasSueldos(EntityManager em, BigInteger secMotivosCambiosSueldos);
}
