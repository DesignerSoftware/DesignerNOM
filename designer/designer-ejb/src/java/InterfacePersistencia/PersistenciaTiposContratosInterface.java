/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposContratos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TiposContratos' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaTiposContratosInterface {
    /**
     * Método encargado de insertar un TipoContrato en la base de datos.
     * @param tiposContratos TipoContrato que se quiere crear.
     */
    public void crear(EntityManager em, TiposContratos tiposContratos);
    /**
     * Método encargado de modificar un TipoContrato de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param tiposContratos TipoContrato con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, TiposContratos tiposContratos);
    /**
     * Método encargado de eliminar de la base de datos el TipoContrato que entra por parámetro.
     * @param tiposContratos TipoContrato que se quiere eliminar.
     */
    public void borrar(EntityManager em, TiposContratos tiposContratos);
    /**
     * Método encargado de buscar el TipoContrato con la secuencia dada por parámetro.
     * @param secuencia Secuencia del TipoContrato que se quiere encontrar.
     * @return Retorna el TipoContrato identificado con la secuencia dada por parámetro.
     */
    public TiposContratos buscarTipoContratoSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los TiposContratos existentes en la base de datos, ordenados por código.
     * @return Retorna una lista de TiposContratos ordenados por código.
     */
    public List<TiposContratos> tiposContratos(EntityManager em);
    
    public void clonarTipoContrato(BigInteger secuenciaClonado, String nuevoNombre, Short nuevoCodigo);
            
}
