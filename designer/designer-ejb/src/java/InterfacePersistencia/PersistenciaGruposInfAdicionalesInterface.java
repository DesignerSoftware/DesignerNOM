/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.GruposInfAdicionales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'GruposInfAdicionales' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaGruposInfAdicionalesInterface {

    /**
     * Método encargado de insertar un GrupoInfAdicional en la base de datos.
     *
     * @param gruposInfAdicionales GrupoInfAdicional que se quiere crear.
     */
    public void crear(EntityManager em,GruposInfAdicionales gruposInfAdicionales);

    /**
     * Método encargado de modificar un GrupoInfAdicional de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param gruposInfAdicionales GrupoInfAdicional con los cambios que se van
     * a realizar.
     */
    public void editar(EntityManager em,GruposInfAdicionales gruposInfAdicionales);

    /**
     * Método encargado de eliminar de la base de datos el GrupoInfAdicional que
     * entra por parámetro.
     *
     * @param gruposInfAdicionales GrupoInfAdicional que se quiere eliminar.
     */
    public void borrar(EntityManager em,GruposInfAdicionales gruposInfAdicionales);

    /**
     * Método encargado de buscar el GrupoInfAdicional con la
     * secGruposInfAdicionales dada por parámetro.
     *
     * @param secGruposInfAdicionales Secuencia del GrupoInfAdicional que se
     * quiere encontrar.
     * @return Retorna el GrupoInfAdicional identificado con la
     * secGruposInfAdicionales dada por parámetro.
     */
    public GruposInfAdicionales buscarGrupoInfAdicional(EntityManager em,BigInteger secGruposInfAdicionales);

    /**
     * Método encargado de buscar todos los GruposInfAdicionales existentes en
     * la base de datos.
     *
     * @return Retorna una lista de GruposInfAdicionales.
     */
    public List<GruposInfAdicionales> buscarGruposInfAdicionales(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre una
     * GruposInfAdicionales específica y algún InformeAdicional. Adémas de la
     * revisión, cuenta cuantas relaciones existen.
     *
     * @param secGruposInfAdicionales Secuencia del GruposInfAdicionales.
     * @return Retorna el número de InformacionesAdicionales relacionados con la
     * moneda cuya secGruposInfAdicionales coincide con el parámetro.
     */
    public BigInteger contadorInformacionesAdicionales(EntityManager em,BigInteger secGruposInfAdicionales);
}
