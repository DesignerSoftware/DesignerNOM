/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.MotivosRetiros;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'MotivosRetiros' de la base de datos.
 *
 * @author AndresPineda
 */
public interface PersistenciaMotivosRetirosInterface {

    /**
     * Método encargado de insertar un MotivoRetiro en la base de datos.
     *
     * @param motivosRetiros MotivoRetiro que se quiere crear.
     */
    public void crear(EntityManager em, MotivosRetiros motivosRetiros);

    /**
     * Método encargado de modificar un MotivoRetiro de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param motivosRetiros MotivoRetiro con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, MotivosRetiros motivosRetiros);

    /**
     * Método encargado de eliminar de la base de datos el MotivoRetiro que
     * entra por parámetro.
     *
     * @param motivosRetiros MotivoRetiro que se quiere eliminar.
     */
    public void borrar(EntityManager em, MotivosRetiros motivosRetiros);

    /**
     * Método encargado de buscar todos los MotivosRetiros existentes en la base
     * de datos.
     *
     * @return Retorna una lista de MotivosRetiros.
     */
    public List<MotivosRetiros> consultarMotivosRetiros(EntityManager em);

    /**
     * Método encargado de buscar el MotivoRetiro con la secMotivosRetiros dada
     * por parámetro.
     *
     * @param secMotivosRetiros Secuencia del MotivoRetiro que se quiere
     * encontrar.
     * @return Retorna el MotivoRetiro identificado con la secMotivosRetiros
     * dada por parámetro.
     */
    public MotivosRetiros consultarMotivoRetiro(EntityManager em, BigInteger secMotivosRetiros);

    /**
     * Metodo encargado de contar cuantas HVExperianciasLaborales estan
     * relacionadas con la secuencia de MotivoRetiro recivida
     *
     * @param secMotivosRetiros Secuencia del MotivoRetiro
     * @return Cuantos HVExperienciasLaborales tienen la secuencia
     * secMotivosRetiros recivida
     */
    public BigInteger contarHVExperienciasLaboralesMotivoRetiro(EntityManager em, BigInteger secMotivosRetiros);

    /**
     * Metodo encargado de contar cuantas NoveradesSistema estan relacionadas
     * con la secuencia de MotivoRetiro recivida
     *
     * @param secMotivosRetiros Secuencia del MotivoRetiro
     * @return Cuantos NoveradesSistema tienen la secuencia secMotivosRetiros
     * recivida
     */
    public BigInteger contarNovedadesSistemasMotivoRetiro(EntityManager em, BigInteger secMotivosRetiros);

    /**
     * Metodo encargado de contar cuantas RetiMotivosRetiros estan relacionadas
     * con la secuencia de MotivoRetiro recivida
     *
     * @param secMotivosRetiros Secuencia del MotivoRetiro
     * @return Cuantos RetiMotivosRetiros tienen la secuencia secMotivosRetiros
     * recivida
     */
    public BigInteger contarRetiMotivosRetirosMotivoRetiro(EntityManager em, BigInteger secMotivosRetiros);

    /**
     * Metodo encargado de contar cuantas Retirados estan relacionadas con la
     * secuencia de MotivoRetiro recivida
     *
     * @param secMotivosRetiros Secuencia del MotivoRetiro
     * @return Cuantos Retirados tienen la secuencia secMotivosRetiros recivida
     */
    public BigInteger contarRetiradosMotivoRetiro(EntityManager em, BigInteger secMotivosRetiros);
}
