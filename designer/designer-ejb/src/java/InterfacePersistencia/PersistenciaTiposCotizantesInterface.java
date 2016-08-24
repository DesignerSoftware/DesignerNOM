/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposCotizantes;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TiposCotizantes' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaTiposCotizantesInterface {
    /**
     * Método encargado de buscar todos los TiposCotizantes existentes en la base de datos, ordenados por código.
     * @return Retorna una lista de TiposCotizantes ordenados por código.
     */
    public List<TiposCotizantes> lovTiposCotizantes(EntityManager em);
    /**
     * Método encargado de insertar un TipoCotizante en la base de datos.
     *
     * @param tiposCotizantes TiposCotizantes que se quiere crear.
     */
    public void crear(EntityManager em, TiposCotizantes tiposCotizantes);
     /**
     * Método encargado de modificar un TiposCotizantes de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposCotizantes TipoCotizante con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposCotizantes tiposCotizantes);

    /**
     * Método encargado de eliminar de la base de datos el TipoCotizante que entra
     * por parámetro.
     *
     * @param tiposCotizantes tipoCotizante que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposCotizantes tiposCotizantes);

        
}
