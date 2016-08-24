/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.MotivosLocalizaciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'MotivosLocalizaciones' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaMotivosLocalizacionesInterface {

    /**
     * Método encargado de insertar un MotivoLocalizacion en la base de datos.
     *
     * @param motivosLocalizaciones MotivoLocalizacion que se quiere crear.
     */
    public void crear(EntityManager em, MotivosLocalizaciones motivosLocalizaciones);

    /**
     * Método encargado de modificar un MotivoLocalizacion de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con
     * la información de la base de datos.
     *
     * @param motivosLocalizaciones MotivoLocalizacion con los cambios que se
     * van a realizar.
     */
    public void editar(EntityManager em, MotivosLocalizaciones motivosLocalizaciones);

    /**
     * Método encargado de eliminar de la base de datos el MotivoLocalizacion
     * que entra por parámetro.
     *
     * @param motivosLocalizaciones MotivoLocalizacion que se quiere eliminar.
     */
    public void borrar(EntityManager em, MotivosLocalizaciones motivosLocalizaciones);

    /**
     * Método encargado de buscar todos los MotivosLocalizaciones existentes en
     * la base de datos.
     *
     * @return Retorna una lista de MotivosLocalizaciones.
     */
    public List<MotivosLocalizaciones> buscarMotivosLocalizaciones(EntityManager em);

    /**
     * Método encargado de buscar el MotivoLocalizacion con la
     * secMotivosLocalizaciones dada por parámetro.
     *
     * @param secMotivosLocalizaciones Secuencia del MotivoLocalizacion que se
     * quiere encontrar.
     * @return Retorna el MotivoLocalizacion identificado con la
     * secMotivosLocalizaciones dada por parámetro.
     */
    public MotivosLocalizaciones buscarMotivoLocalizacionSecuencia(EntityManager em, BigInteger secMotivosLocalizaciones);

    public BigInteger contarVigenciasLocalizacionesMotivoLocalizacion(EntityManager em, BigInteger secMotivoLocalizacion);
}
