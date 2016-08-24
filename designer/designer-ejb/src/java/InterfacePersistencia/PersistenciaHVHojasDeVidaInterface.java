/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.HVHojasDeVida;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'HVHojasDeVida' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaHVHojasDeVidaInterface {
    /**
     * Método encargado de buscar la HVHojasDeVida de una persona.
     * @param secuenciaPersona Secuencia de la persona de la cual se quiere la HVHojasDeVida.
     * @return Retorna la HVHojasDeVida de la persona.
     */
    public HVHojasDeVida hvHojaDeVidaPersona(EntityManager em,BigInteger secuenciaPersona);
    /**
     * Método encargado de modificar una HVHojasDeVida de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param hVHojasDeVida HVHojasDeVida con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,HVHojasDeVida hVHojasDeVida);
}
