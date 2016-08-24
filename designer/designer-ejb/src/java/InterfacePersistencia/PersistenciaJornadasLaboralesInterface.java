/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.JornadasLaborales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'JornadasLaborales' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaJornadasLaboralesInterface {
    
    /**
     * Método encargado de insertar una JornadaLaboral en la base de datos.
     * @param jornadasLaborales JornadaLaboral que se quiere crear.
     */
    public void crear(EntityManager em, JornadasLaborales jornadasLaborales);
    /**
     * Método encargado de modificar una JornadaLaboral de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param jornadasLaborales JornadaLaboral con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, JornadasLaborales jornadasLaborales);
    /**
     * Método encargado de eliminar de la base de datos la JornadaLaboral que entra por parámetro.
     * @param jornadasLaborales JornadaLaboral que se quiere eliminar.
     */
    public void borrar(EntityManager em, JornadasLaborales jornadasLaborales);
    /**
     * Método encargado de buscar todas las JornadasLaborales existentes en la base de datos.
     * @return Retorna una lista de JornadasLaborales.
     */
    public List<JornadasLaborales> buscarJornadasLaborales(EntityManager em);
    /**
     * Método encargado de buscar la JornadaLaboral con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la JornadaLaboral que se quiere encontrar.
     * @return Retorna la JornadaLaboral identificada con la secuencia dada por parámetro.
     */
    public JornadasLaborales buscarJornadaLaboralSecuencia(EntityManager em, BigInteger secuencia);
    
}
