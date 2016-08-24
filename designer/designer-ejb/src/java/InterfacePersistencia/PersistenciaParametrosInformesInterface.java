/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.ParametrosInformes;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'ParametrosInformes' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaParametrosInformesInterface {
    /**
     * Método encargado de insertar un ParametroInforme en la base de datos.
     * @param parametrosInformes ParametroInforme que se quiere crear.
     */
    public void crear(EntityManager em, ParametrosInformes parametrosInformes);
    /**
     * Método encargado de modificar un ParametroInforme de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param parametrosInformes ParametroInforme con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, ParametrosInformes parametrosInformes);
    /**
     * Método encargado de eliminar de la base de datos el ParametroInforme que entra por parámetro.
     * @param parametrosInformes ParametroInforme que se quiere eliminar.
     */
    public void borrar(EntityManager em, ParametrosInformes parametrosInformes);
    /**
     * Método encargado de buscar el ParametroInforme con la secuencia dada por parámetro.
     * @param secuencia Secuencia del ParametroInforme que se quiere encontrar.
     * @return Retorna el ParametroInforme identificado con la secuencia dada por parámetro.
     */
    public ParametrosInformes buscarParametroInforme(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los ParametrosInformes existentes en la base de datos.
     * @return Retorna una lista de ParametrosInformes.
     */
    public List<ParametrosInformes> buscarParametrosInformes(EntityManager em);
    /**
     * Método encargado de buscar el ParametroInforme del un usuario.
     * @param alias Alias del usuario del que se desea buscar el ParametroInforme
     * @return Retorna el ParametroInforme asociado al usuario cuyo alias es el dado por parámetro.
     */
    public ParametrosInformes buscarParametroInformeUsuario(EntityManager em, String alias);
    
}
