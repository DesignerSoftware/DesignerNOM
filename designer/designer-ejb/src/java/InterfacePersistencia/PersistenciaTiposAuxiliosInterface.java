/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposAuxilios;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TipoAuxilios' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaTiposAuxiliosInterface {
    /**
     * Método encargado de insertar un TipoAuxilio en la base de datos.
     * @param tiposAuxilios TipoAuxilio que se quiere crear.
     */
    public String crear(EntityManager em, TiposAuxilios tiposAuxilios);
    /**
     * Método encargado de modificar un TipoAuxilio de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param tiposAuxilios TipoAuxilio con los cambios que se van a realizar.
     */
    public String editar(EntityManager em, TiposAuxilios tiposAuxilios);
    /**
     * Método encargado de eliminar de la base de datos el TipoAuxilio que entra por parámetro.
     * @param tiposAuxilios TipoAuxilio con los cambios que se van a realizar.
     */
    public String borrar(EntityManager em, TiposAuxilios tiposAuxilios);
    /**
     * Método encargado de buscar el TipoAuxilio con la secuencia dada por parámetro.
     * @param secuencia Secuencia del TipoAuxilio que se quiere encontrar.
     * @return Retorna el TipoAuxilio identificada con la secuencia dada por parámetro.
     */
    public TiposAuxilios buscarTipoAuxilio(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los TiposAuxilios existentes en la base de datos, ordenadas por código.
     * @return Retorna una lista de TiposAuxilios.
     */
    public List<TiposAuxilios> buscarTiposAuxilios(EntityManager em);
    /**
     * Método encargado de recuperar cuantas TablasAuxilios están asociadas a un TipoAuxilio específico.
     * @param secuencia Secuencia de TipoAuxilio.
     * @return Retorna el número de TablasAuxilios cuyo atributo 'TipoAuxilio' tiene como secuencia el 
     * valor dado por parámetro.
     */
    public BigInteger contadorTablasAuxilios(EntityManager em, BigInteger secuencia);
}
