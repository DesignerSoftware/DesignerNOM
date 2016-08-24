/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Enfermedades;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Enfermedades' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaEnfermedadesInterface {

    /**
     * Método encargado de insertar una Enfermedad en la base de datos.
     *
     * @param enfermedades Enfermedad que se quiere crear.
     */
    public void crear(EntityManager em,Enfermedades enfermedades);

    /**
     * Método encargado de modificar una Enfermedad de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param enfermedades Enfermedad con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Enfermedades enfermedades);

    /**
     * Método encargado de eliminar de la base de datos la Enfermedad que entra
     * por parámetro.
     *
     * @param enfermedades Enfermedad
     */
    public void borrar(EntityManager em,Enfermedades enfermedades);

    /**
     * Método encargado de buscar la Enfermedad con la secEnfermedades dada por
     * parámetro.
     *
     * @param secEnfermedades secEnfermedades de la Enfermedad que se quiere
     * encontrar.
     * @return Retorna la Enfermedad identificada con la secEnfermedades dada
     * por parámetro.
     */
    public Enfermedades buscarEnfermedad(EntityManager em,BigInteger secEnfermedades);

    /**
     * Método encargado de buscar todas las Enfermedades existentes en la base
     * de datos.
     *
     * @return Retorna una lista de Enfermedades.
     */
    public List<Enfermedades> buscarEnfermedades(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre una Enfermedad
     * específica y algún Proyecto. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secEnfermedades secEnfermedades de la Enfermedad.
     * @return Retorna el número de Ausentimos relacionados con la Enfermedad
     * cuya secEnfermedades coincide con el parámetro.
     */
    public BigInteger contadorAusentimos(EntityManager em,BigInteger secEnfermedades);

    /**
     * Método encargado de revisar si existe una relacion entre una Enfermedad
     * específica y algún DetallesLicencias. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secEnfermedades secEnfermedades de la Enfermedad.
     * @return Retorna el número de DetallesLicencias relacionados con la
     * Enfermedad cuya secEnfermedades coincide con el parámetro.
     */
    public BigInteger contadorDetallesLicencias(EntityManager em,BigInteger secEnfermedades);

    /**
     * Método encargado de revisar si existe una relacion entre una Enfermedad
     * específica y algúna EnfermedadesPadecidas. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secEnfermedades secEnfermedades de la Enfermedad.
     * @return Retorna el número de EnfermedadesPadecidas relacionados con la
     * Enfermedad cuya secEnfermedades coincide con el parámetro.
     */
    public BigInteger contadorEnfermedadesPadecidas(EntityManager em,BigInteger secEnfermedades);

    /**
     * Método encargado de revisar si existe una relacion entre una Enfermedad
     * específica y algún SoAusentismos. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secEnfermedades secEnfermedades de la Enfermedad.
     * @return Retorna el número de SoAusentismos relacionados con la Enfermedad
     * cuya secEnfermedades coincide con el parámetro.
     */
    public BigInteger contadorSoausentismos(EntityManager em,BigInteger secEnfermedades);

    /**
     * Método encargado de revisar si existe una relacion entre una Enfermedad
     * específica y algún SorevisionessSistemas. Adémas de la revisión, cuenta
     * cuantas relaciones existen.
     *
     * @param secEnfermedades secEnfermedades de la Enfermedad.
     * @return Retorna el número de SorevisionessSistemas relacionados con la
     * Enfermedad cuya secEnfermedades coincide con el parámetro.
     */
    public BigInteger contadorSorevisionessSistemas(EntityManager em,BigInteger secEnfermedades);
}
