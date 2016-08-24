/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposTelefonos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TiposTelefonos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaTiposTelefonosInterface {
    /**
     * Método encargado de insertar un TipoTelefono en la base de datos.
     * @param tiposTelefonos TipoTelefono que se quiere crear.
     */
    public void crear(EntityManager em, TiposTelefonos tiposTelefonos);
    /**
     * Método encargado de modificar un TipoTelefono de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param tiposTelefonos TipoTelefono con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposTelefonos tiposTelefonos);
    /**
     * Método encargado de eliminar de la base de datos el TipoTelefono que entra por parámetro.
     * @param em
     * @param tiposTelefonos TipoTelefono que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposTelefonos tiposTelefonos);
    /**
     * Método encargado de buscar el TipoTelefono con la secuencia dada por parámetro.
     * @param secuencia Secuencia del TipoTelefono que se quiere encontrar.
     * @return Retorna el TipoTelefono identificado con la secuencia dada por parámetro.
     */
    public TiposTelefonos buscarTipoTelefonos(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los TiposTelefonos existentes en la base de datos, ordenados por código.
     * @return Retorna una lista de TiposTelefonos ordenados por código.
     */
    public List<TiposTelefonos> tiposTelefonos(EntityManager em );
}
