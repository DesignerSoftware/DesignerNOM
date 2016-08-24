/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposEmbargos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TiposEmbargos' 
 * de la base de datos.
 * @author betelgeuse
 */
@Local
public interface PersistenciaTiposEmbargosInterface {
    /**
     * Método encargado de insertar un TipoEmbargo en la base de datos.
     * @param tiposEmbargos TipoEmbargo que se quiere crear.
     */
    public void crear(EntityManager em, TiposEmbargos tiposEmbargos);
    /**
     * Método encargado de modificar un TipoEmbargo de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param tiposEmbargos TipoEmbargo con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposEmbargos tiposEmbargos);
    /**
     * Método encargado de eliminar de la base de datos el TipoEmbargo que entra por parámetro.
     * @param tiposEmbargos TipoEmbargo con los cambios que se van a realizar.
     */
    public void borrar(EntityManager em, TiposEmbargos tiposEmbargos);
    /**
     * Método encargado de buscar el TipoEmbargo con la secTiposEmbargos dada por parámetro.
     * @param secTiposEmbargos Secuencia del TipoEmbargo que se quiere encontrar.
     * @return Retorna el TipoEmbargo identificada con la secTiposEmbargos dada por parámetro.
     */
    public TiposEmbargos buscarTipoEmbargo(EntityManager em, BigInteger secTiposEmbargos);
    /**
     * Método encargado de buscar todos los TiposEmbargos existentes en la base de datos, ordenadas por código.
     * @return Retorna una lista de TiposEmbargos.
     */
    public List<TiposEmbargos> buscarTiposEmbargos(EntityManager em );
    /**
     * Método encargado de recuperar cuantos EerPrestamos están asociados a un TipoEmbargo específico.
     * @param secTiposEmbargos Secuencia de TipoEmbargo.
     * @return Retorna el número de EerPrestamos cuyo atributo 'TipoEmbargo' tiene como secTiposEmbargos el 
     * valor dado por parámetro.
     */
    public BigInteger contadorEerPrestamos(EntityManager em, BigInteger secTiposEmbargos);
    /**
     * Método encargado de recuperar cuantos FormasDtos están asociados a un TipoEmbargo específico.
     * @param secTiposEmbargos Secuencia de TipoEmbargo.
     * @return Retorna el número de FormasDtos cuyo atributo 'TipoEmbargo' tiene como secTiposEmbargos el 
     * valor dado por parámetro.
     */
    public BigInteger contadorFormasDtos(EntityManager em, BigInteger secTiposEmbargos);
}
