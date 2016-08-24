/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.ConceptosJuridicos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'ConceptosJuridicos' 
 * de la base de datos.
 * @author Andres Pineda.
 */
public interface PersistenciaConceptosJuridicosInterface {
    /**
     * Método encargado de insertar un ConceptoJuridico en la base de datos.
     * @param conceptosJuridicos ConceptoJuridico que se quiere crear.
     */
    public void crear(EntityManager em,ConceptosJuridicos conceptosJuridicos);
    /**
     * Método encargado de modificar un ConceptoJuridico de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param conceptosJuridicos ConceptoJuridico con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,ConceptosJuridicos conceptosJuridicos);
    /**
     * Método encargado de eliminar de la base de datos el ConceptoJuridico que entra por parámetro.
     * @param conceptosJuridicos ConceptoJuridico que se quiere eliminar.
     */
    public void borrar(EntityManager em,ConceptosJuridicos conceptosJuridicos);
    /**
     * Método encargado de buscar todos los TiposJornadas existentes en la base de datos.
     * @return Retorna una lista de TipoJornadas.
     */
    public List<ConceptosJuridicos> buscarConceptosJuridicos(EntityManager em);
    /**
     * Método encargado de buscar el ConceptoJuridico con la secuencia dada por parámetro.
     * @param secuencia Secuencia del ConceptoJuridico que se quiere encontrar.
     * @return Retorna el ConceptoJuridico identificado con la secuencia dada por parámetro.
     */
    public ConceptosJuridicos buscarConceptosJuridicosSecuencia(EntityManager em,BigInteger secuencia);
    /**
     * Método encargado de buscar los ConceptosJuridicos de una empresa específica.
     * @param secuencia Secuencia de la Empresa.
     * @return Retorna una lista de ConceptosJuridicos los cuales estan asociados a la empresa cuya secuencia coincida
     * con el valor dado por parámetro.
     */
    public List<ConceptosJuridicos> buscarConceptosJuridicosEmpresa(EntityManager em,BigInteger secuencia);
}
