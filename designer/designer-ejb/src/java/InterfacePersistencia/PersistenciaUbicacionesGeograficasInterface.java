/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.UbicacionesGeograficas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'UbicacionesGeograficas' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaUbicacionesGeograficasInterface {

    /**
     * Método encargado de insertar un UbicacionesGeograficas en la base de
     * datos.
     *
     * @param ubicacionGeografica UbicacionesGeograficas que se quiere crear.
     */
    public void crear(EntityManager em, UbicacionesGeograficas ubicacionGeografica);

    /**
     * Método encargado de modificar un UbicacionesGeograficas de la base de
     * datos. Este método recibe la información del parámetro para hacer un
     * 'merge' con la información de la base de datos.
     *
     * @param ubicacionGeografica UbicacionesGeograficas con los cambios que se
     * van a realizar.
     */
    public void editar(EntityManager em, UbicacionesGeograficas ubicacionGeografica);

    /**
     * Método encargado de eliminar de la base de datos el
     * UbicacionesGeograficas que entra por parámetro.
     *
     * @param ubicacionGeografica UbicacionesGeograficas que se quiere eliminar.
     */
    public void borrar(EntityManager em, UbicacionesGeograficas ubicacionGeografica);

    /**
     * Método encargado de buscar todas las UbicacionesGeograficas existentes en
     * la base de datos.
     *
     * @return Retorna una lista de UbicacionesGeograficas.
     */
    public List<UbicacionesGeograficas> consultarUbicacionesGeograficas(EntityManager em );

    /**
     * Método encargado de buscar el UbicacionesGeografica con la
     * secUbicacionesGeografica dada por parámetro.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeografica que
     * se quiere encontrar.
     * @return Retorna el UbicacionesGeografica identificado con la
     * secUbicacionesGeografica dada por parámetro.
     */
    public UbicacionesGeograficas consultarUbicacionGeografica(EntityManager em, BigInteger secUbicacionesGeograficas);

    /**
     * Método encargado de buscar los UbicacionesGeografica de una empresa
     * específica.
     *
     * @param secEmpresa Identificador único de la empresa a la cual pertenecen
     * los ubicacionGeografica.
     * @return Retorna una lista de CentrosCostos que pertenecen a la empresa
     * con secUbicacionesGeografica igual a la pasada por parametro.
     */
    public List<UbicacionesGeograficas> consultarUbicacionesGeograficasPorEmpresa(EntityManager em, BigInteger secEmpresa);

    /**
     * Método encargado de revisar si existe una relación entre un
     * UbicacionesGeograficas específica y algún AfiliacionesEntidades. además
     * de la revisión, cuenta cuantas relaciónes existen.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeograficas.
     * @return Retorna el número de AfiliacionesEntidades relacionados con el
     * UbicacionesGeograficas cuya secUbicacionesGeograficas coincide con el
     * parámetro.
     */
    public BigInteger contarAfiliacionesEntidadesUbicacionGeografica(EntityManager em, BigInteger secUbicacionesGeograficas);

    /**
     * Método encargado de revisar si existe una relación entre un
     * UbicacionesGeograficas específica y algún Inspecciones. además de la
     * revisión, cuenta cuantas relaciónes existen.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeograficas.
     * @return Retorna el número de Inspecciones relacionados con el
     * UbicacionesGeograficas cuya secUbicacionesGeograficas coincide con el
     * parámetro.
     */
    public BigInteger contarInspeccionesUbicacionGeografica(EntityManager em, BigInteger secUbicacionesGeograficas);

    /**
     * Método encargado de revisar si existe una relación entre un
     * UbicacionesGeograficas específica y algún ParametrosInformes. además de
     * la revisión, cuenta cuantas relaciónes existen.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeograficas.
     * @return Retorna el número de ParametrosInformes relacionados con el
     * UbicacionesGeograficas cuya secUbicacionesGeograficas coincide con el
     * parámetro.
     */
    public BigInteger contarParametrosInformesUbicacionGeografica(EntityManager em, BigInteger secUbicacionesGeograficas);

    /**
     * Método encargado de revisar si existe una relación entre un
     * UbicacionesGeograficas específica y algún Revisiones. además de la
     * revisión, cuenta cuantas relaciónes existen.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeograficas.
     * @return Retorna el número de Revisiones relacionados con el
     * UbicacionesGeograficas cuya secUbicacionesGeograficas coincide con el
     * parámetro.
     */
    public BigInteger contarRevisionesUbicacionGeografica(EntityManager em, BigInteger secUbicacionesGeograficas);

    /**
     * Método encargado de revisar si existe una relación entre un
     * UbicacionesGeograficas específica y algún VigenciasUbicaciones. además de
     * la revisión, cuenta cuantas relaciónes existen.
     *
     * @param secUbicacionesGeograficas Secuencia del UbicacionesGeograficas.
     * @return Retorna el número de VigenciasUbicaciones relacionados con el
     * UbicacionesGeograficas cuya secUbicacionesGeograficas coincide con el
     * parámetro.
     */
    public BigInteger contarVigenciasUbicacionesGeografica(EntityManager em, BigInteger secUbicacionesGeograficas);
    
    public int existeCiudadporSecuencia(EntityManager em, BigInteger secuenciaCiudad);
}
