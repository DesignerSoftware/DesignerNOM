/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.MotivosContratos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'MotivosContratos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaMotivosContratosInterface {
    /**
     * Método encargado de insertar un MotivoContrato en la base de datos.
     * @param motivosContratos MotivoContrato que se quiere crear.
     */
    public void crear(EntityManager em, MotivosContratos motivosContratos);
    /**
     * Método encargado de modificar un MotivoContrato de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param motivosContratos MotivoContrato con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, MotivosContratos motivosContratos);
    /**
     * Método encargado de eliminar de la base de datos el MotivoContrato que entra por parámetro.
     * @param motivosContratos MotivoContrato que se quiere eliminar.
     */
    public void borrar(EntityManager em, MotivosContratos motivosContratos);
    /**
     * Método encargado de buscar el MotivoContrato con la secMotivosContratos dada por parámetro.
     * @param secMotivosContratos Secuencia del MotivoContrato que se quiere encontrar.
     * @return Retorna el MotivoContrato identificado con la secMotivosContratos dada por parámetro.
     */
    public MotivosContratos buscarMotivoContrato(EntityManager em, BigInteger secMotivosContratos);
    /**
     * Método encargado de buscar todos los MotivosContratos existentes en la base de datos.
     * @return Retorna una lista de MotivosContratos.
     */
    public List<MotivosContratos> buscarMotivosContratos(EntityManager em);
    /**
     * Método encargado de verificar si hay al menos una VigenciaTipoContrato asociada a un MotivoContrato.
     * @param secMotivosContratos Secuencia del MotivoContrato
     * @return Retorna un valor mayor a cero si existe alguna VigenciaTipoContrato asociada a un MotivoContrato.
     */
    public BigInteger verificarBorradoVigenciasTiposContratos(EntityManager em, BigInteger secMotivosContratos);
    /**
     * Método encargado de buscar todos los MotivosContratos existentes en la base de datos, ordenados por código.
     * @return Retorna una lista de MotivosContratos ordenados por código.
     */
    public List<MotivosContratos> motivosContratos(EntityManager em);
}
