/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasDeportes;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasDeportes' 
 * de la base de datos.
 * @author Administrator
 */
public interface PersistenciaVigenciasDeportesInterface {
    /**
     * Método encargado de insertar una VigenciaDeporte en la base de datos.
     * @param vigenciasDeportes VigenciaDeporte que se quiere crear.
     */
    public String crear(EntityManager em, VigenciasDeportes vigenciasDeportes);
    /**
     * Método encargado de modificar una VigenciaDeporte de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasDeportes VigenciaDeporte con los cambios que se van a realizar.
     */
    public String editar(EntityManager em, VigenciasDeportes vigenciasDeportes);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaDeporte que entra por parámetro.
     * @param vigenciasDeportes VigenciaDeporte que se quiere eliminar.
     */
    public String borrar(EntityManager em, VigenciasDeportes vigenciasDeportes);
    /**
     * Método encargado de buscar los últimos deportes practicados por una Persona.
     * @param secuencia Secuencia de la Persona.
     * @return Retorna una lista de las VigenciasDeportes cuya fechaInicial es la ultima fecha registrada para una
     * persona específica.
     */
    public List<VigenciasDeportes> deportePersona(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los deportes practicados por una Persona.
     * @param secuencia Secuencia de la Persona.
     * @return Retorna una lista de VigenciasDeportes asociados a la Persona cuya secuencia coincide con el parámetro.
     */
    public List<VigenciasDeportes> deportesTotalesSecuenciaPersona(EntityManager em, BigInteger secuencia);

    public String consultarPrimerDeporte(EntityManager em,BigInteger secuencia);
}
