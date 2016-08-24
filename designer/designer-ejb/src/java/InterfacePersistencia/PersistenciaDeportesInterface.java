/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Deportes;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Deportes' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaDeportesInterface {

    /**
     * Método encargado de insertar un Deporte en la base de datos.
     *
     * @param deportes Deporte que se quiere crear.
     */
    public void crear(EntityManager em,Deportes deportes);

    /**
     * Método encargado de modificar un Deporte de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param deportes Deporte con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Deportes deportes);

    /**
     * Método encargado de eliminar de la base de datos el Deporte que entra por
     * parámetro.
     *
     * @param deportes Deporte que se quiere eliminar.
     */
    public void borrar(EntityManager em,Deportes deportes);

    /**
     * Método encargado de buscar el Deporte con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Deporte que se quiere encontrar.
     * @return Retorna el Deporte cuya secuencia coincide con el parámetro dado.
     */
    public Deportes buscarDeporte(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar todos los Deportes existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Deportes.
     */
    public List<Deportes> buscarDeportes(EntityManager em);

    /**
     * 
     * @param secuencia
     * @return
     */
    public BigInteger contadorParametrosInformes(EntityManager em,BigInteger secuencia);

    /**
     * 
     * @param secuencia
     * @return
     */
    public BigInteger contadorDeportesPersonas(EntityManager em,BigInteger secuencia);

    /**
     * 
     * @param secuencia
     * @return
     */
    public BigInteger verificarBorradoVigenciasDeportes(EntityManager em,BigInteger secuencia);

}
