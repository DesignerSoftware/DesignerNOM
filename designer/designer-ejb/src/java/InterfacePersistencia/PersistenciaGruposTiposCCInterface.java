/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.GruposTiposCC;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'GruposTiposCC' 
 * de la base de datos.
 * @author betelgeuse
 */
@Local
public interface PersistenciaGruposTiposCCInterface {
    /**
     * Método encargado de insertar un GrupoTipoCC en la base de datos.
     * @param gruposTiposCC GrupoTipoCC que se quiere crear.
     */
    public void crear(EntityManager em,GruposTiposCC gruposTiposCC);
    /**
     * Método encargado de modificar un GrupoTipoCC de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param gruposTiposCC GrupoTipoCC con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,GruposTiposCC gruposTiposCC);
    /**
     * Método encargado de eliminar de la base de datos el GrupoTipoCC que entra por parámetro.
     * @param gruposTiposCC GrupoTipoCC que se quiere eliminar.
     */
    public void borrar(EntityManager em,GruposTiposCC gruposTiposCC);
    /**
     * Método encargado de buscar el GrupoTipoCC con la secuencia dada por parámetro.
     * @param secuencia Secuencia del GrupoTipoCC que se quiere encontrar.
     * @return Retorna el GrupoTipoCC identificado con la secuencia dada por parámetro.
     */
    public GruposTiposCC buscarGruposTiposCCPorSecuencia(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar todos los GruposTiposCC existentes en la base de datos.
     * @return Retorna una lista de GruposTiposCC.
     */
    public List<GruposTiposCC> buscarGruposTiposCC(EntityManager em);
}
