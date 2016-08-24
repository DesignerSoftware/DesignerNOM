/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Recordatorios;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Recordatorios' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaRecordatoriosInterface {
    /**
     * Método encargado de buscar el primer recordatorio de tipo 'PROVERBIO' de una tabla de Recordatorios
     * organizada aleatoriamente.
     * @param entity EntityManager encargado de la comunicación con la base de datos.
     * @return Retorna un Recordatorio de tipo 'PROVERBIO'.
     */
    public Recordatorios recordatorioRandom(EntityManager entity);
    /**
     * Método encargado de buscar los mensajes de los recordatorios de tipo 'RECORDATORIO', con fecha igual al día actual,
     * pertenecientes al usuario que está usando el aplicativo o no están asociados a ningún usuario.
     * (EntityManager em, día actual hace referencia al díe en la que el usuario ingresa al aplicativo)
     * @param entity EntityManager encargado de la comunicación con la base de datos.
     * @return Retorna una lista de Strings con los mensajes de los recordatorios de tipo 'RECORDATORIO' que cumplen las condiciones.
     */
    public List<String> recordatoriosInicio(EntityManager entity);
    /**
     * Método encargado de buscar los mensajes de los recordatorios de tipo 'CONSULTA', con fecha igual al día actual,
     * pertenecientes al usuario que está usando el aplicativo o no están asociados a ningún usuario.
     * (EntityManager em, día actual hace referencia al díe en la que el usuario ingresa al aplicativo)
     * @param entity EntityManager encargado de la comunicación con la base de datos.
     * @return Retorna una lista de recordatorios de tipo 'CONSULTA' que cumplen las condiciones.
     */
    public List<Recordatorios> consultasInicio(EntityManager entity);
    
    public List<Recordatorios> proverbiosRecordatorios(EntityManager em);
    
    public List<Recordatorios> mensajesRecordatorios(EntityManager em);
    public void crear(EntityManager em, Recordatorios recordatorios);
    public void borrar(EntityManager em, Recordatorios recordatorios);
    public void editar(EntityManager em, Recordatorios recordatorios);
    public Recordatorios consultaRecordatorios(EntityManager em, BigInteger secuencia) throws Exception;
    public List<String> ejecutarConsultaRecordatorio(EntityManager em, BigInteger secuencia) throws Exception;
}
