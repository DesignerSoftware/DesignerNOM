/**
 * Documentación a cargo de Andres Pineda
 */
package InterfacePersistencia;

import Entidades.Escalafones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Escalafones' 
 * de la base de datos.
 * @author Andres Pineda
 */
public interface PersistenciaEscalafonesInterface {

    /**
     * Método encargado de insertar un Escalafon en la base de datos.
     * @param escalafones Escalafon que se quiere crear.
     */
    public void crear(EntityManager em,Escalafones escalafones);
    /**
     * Método encargado de modificar un Escalafon de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param escalafones Escalafones con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Escalafones escalafones);
    /**
     * Método encargado de eliminar de la base de datos el Escalafon que entra por parámetro.
     * @param escalafon Escalafones que se quiere eliminar.
     */
    public void borrar(EntityManager em,Escalafones escalafon);
     /**
     * Método encargado de buscar todos los Escalafones existentes en la base de datos.
     * @return Retorna una lista de Escalafones
     */
    public List<Escalafones> buscarEscalafones(EntityManager em);
    /**
     * Método encargado de buscar un Escalafon con la secEscalafon dada por
     * parámetro.
     *
     * @param secEscalafon secEscalafon del Escalafon que se quiere
     * encontrar.
     * @return Retorna el Escalafon identificado con la secEscalafon dada
     * por parámetro.
     */
    public Escalafones buscarEscalafonSecuencia(EntityManager em,BigInteger secEscalafon);

}
