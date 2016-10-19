/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Cargos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Cargos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaCargosInterface {

    /**
     * Método encargado de insertar un Cargo en la base de datos.
     *
     * @param cargos Cargo que se quiere crear.
     */
    public void crear(EntityManager em,Cargos cargos);

    /**
     * Método encargado de modificar un Cargo de la base de datos. Este método
     * recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param cargos Cargo con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Cargos cargos);

    /**
     * Método encargado de eliminar de la base de datos el Cargo que entra por
     * parámetro.
     *
     * @param cargos Cargo que se quiere eliminar.
     */
    public void borrar(EntityManager em,Cargos cargos);

    /**
     * Método encargado de buscar el Cargo con la secuencia dada por parámetro.
     *
     * @param secuencia Identificador único del Cargo que se quiere encontrar.
     * @return Retorna el Cargo identificado con la secuencia dada por
     * parámetro.
     */
    public Cargos buscarCargoSecuencia(EntityManager em,BigInteger secuencia);

    /**
     * Método encargado de buscar todos los Cargos existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Cargos.
     */
    public List<Cargos> consultarCargos(EntityManager em);

    /**
     * Método encargado de traer todos los cargos de la base de datos,
     * calculando el salario de cada cargo
     *
     * @return Retorna una lista de Cargos. Cada objeto cargos tiene su salario
     * incluido.
     */
    public List<Cargos> cargosSalario(EntityManager em);

    /**
     * Método encargado de traer todos los cargos relacionados con una empresada
     * dad por parametro
     *
     * @param secEmpresa Secuencia Empresa
     * @return Retorna una lista de Cargos
     */
    public List<Cargos> buscarCargosPorSecuenciaEmpresa(EntityManager em,BigInteger secEmpresa);
    
    public List<Cargos> lovCargos(EntityManager em);

   // public List<Cargos> consultarCargosXEmpresa(EntityManager em, BigInteger secEmpresa);
}
