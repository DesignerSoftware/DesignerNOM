/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.DetallesExtrasRecargos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'DetalleExtraRecargos' 
 * de la base de datos.
 * @author Andres Pineda.
 */
public interface PersistenciaDetallesExtrasRecargosInterface {
    /**
     * Método encargado de insertar un DetalleExtraRecargo en la base de datos.
     * @param detallesExtrasRecargos DetalleExtraRecargo que se quiere crear.
     */
    public void crear(EntityManager em,DetallesExtrasRecargos detallesExtrasRecargos);
    /**
     * Método encargado de modificar un DetalleExtraRecargo de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param detallesExtrasRecargos DetalleExtraRecargo con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,DetallesExtrasRecargos detallesExtrasRecargos);
    /**
     * Método encargado de eliminar de la base de datos el DetalleExtraRecargo que entra por parámetro.
     * @param detallesExtrasRecargos DetalleExtraRecargo que se quiere eliminar.
     */
    public void borrar(EntityManager em,DetallesExtrasRecargos detallesExtrasRecargos);
    /**
     * Método encargado de buscar el DetalleExtraRecargo con la secuencia dada por parámetro.
     * @param secuencia Secuencia del DetalleExtraRecargo que se quiere encontrar.
     * @return Retorna el DetalleExtraRecargo identificado con la secuencia dada por parámetro.
     */
    public DetallesExtrasRecargos buscarDetalleExtraRecargo(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar todos los DetalleExtraRecargos existentes en la base de datos, ordenados descendentemente por código.
     * @return Retorna una lista de DetalleExtraRecargos.
     */
    public List<DetallesExtrasRecargos> buscaDetallesExtrasRecargos(EntityManager em);
    /**
     * Método encargado de buscar los DetallesExtrasRecargos asociados a un ExtraRecargo específico.
     * @param secuencia Secuencia del ExtraRecargo.
     * @return Retorna una lista de DetallesExtrasRecargos cuyo ExtraRecargo tienen como secuencia el valor dado por
     * parámetro.
     */
    public List<DetallesExtrasRecargos> buscaDetallesExtrasRecargosPorSecuenciaExtraRecargo(EntityManager em,BigInteger secuencia);    
}
