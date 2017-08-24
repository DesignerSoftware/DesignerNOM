/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Modulos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Modulos' 
 * de la base de datos.
 * @author -Felipphe- Felipe Triviño
 */
public interface PersistenciaModulosInterface {
    /**
     * Método encargado de insertar un Modulo en la base de datos.
     * @param modulos Modulo que se quiere crear.
     */
    public void crear(EntityManager em, Modulos modulos);
    /**
     * Método encargado de modificar un Modulo de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param modulos Modulo
     */
    public void editar(EntityManager em, Modulos modulos);
    /**
     * Método encargado de eliminar de la base de datos el Modulo que entra por parámetro.
     * @param modulos Modulo que se quiere eliminar.
     */
    public void borrar(EntityManager em, Modulos modulos);
    /**
     * Método encargado de buscar el Modulo con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Modulo que se quiere encontrar.
     * @return Retorna el Modulo identificado con la secuencia dada por parámetro.
     */
    public Modulos buscarModulos(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los Modulos existentes en la base de datos.
     * @return Retorna una lista de Modulos.
     */
    public List<Modulos> buscarModulos(EntityManager em);
    
    public Modulos buscarModulosPorSecuencia(EntityManager em, BigInteger secModulo);
    
    public List<Modulos> listaModulos(EntityManager em);

}
