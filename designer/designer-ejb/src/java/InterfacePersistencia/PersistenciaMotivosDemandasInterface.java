/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.MotivosDemandas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'MotivosDemandas' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaMotivosDemandasInterface {

    /**
     * Método encargado de insertar un MotivoDemanda en la base de datos.
     *
     * @param motivosDemandas MotivoDemanda que se quiere crear.
     */
    public void crear(EntityManager em, MotivosDemandas motivosDemandas);

    /**
     * Método encargado de modificar un MotivoDemanda de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param motivosDemandas MotivoDemanda con los cambios que se van a
     * realizar.
     */
    public void editar(EntityManager em, MotivosDemandas motivosDemandas);

    /**
     * Método encargado de eliminar de la base de datos el MotivoDemanda que
     * entra por parámetro.
     *
     * @param motivosDemandas MotivoDemanda que se quiere eliminar.
     */
    public void borrar(EntityManager em, MotivosDemandas motivosDemandas);

    /**
     * Método encargado de buscar todos los MotivosDemandas existentes en la
     * base de datos, ordenados por secMotivosDemandas.
     *
     * @return Retorna una lista de MotivosDemandas ordenados por
     * secMotivosDemandas.
     */
    public List<MotivosDemandas> buscarMotivosDemandas(EntityManager em);

    /**
     * Método encargado de revisar si existe una relacion entre un MotivoDemanda
     * específica y algúna Demanda. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secMotivosDemandas Secuencia del Motivo Demanda.
     * @return Retorna el número de Demandas relacionados con el Motivo Demanda
     * cuya secMotivosDemandas coincide con el parámetro.
     */
    public BigInteger contadorDemandas(EntityManager em, BigInteger secMotivosDemandas);

    /**
     * Método encargado de buscar el MotivoDemanda con la secMotivosDemandas
     * dada por parámetro.
     *
     * @param secMotivosDemandas Secuencia del MotivoDemandas que se quiere
     * encontrar.
     * @return Retorna el MotivoDemanda identificado con la secMotivosDemandas
     * dada por parámetro.
     */
    public MotivosDemandas buscarMotivoDemanda(EntityManager em, BigInteger secMotivosDemandas);
}
