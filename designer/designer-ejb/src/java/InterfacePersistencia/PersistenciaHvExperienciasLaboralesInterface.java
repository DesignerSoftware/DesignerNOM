/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.HvExperienciasLaborales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'HvExperienciasLaborales' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaHvExperienciasLaboralesInterface {
    /**
     * Método encargado de insertar una HvExperienciaLaboral en la base de datos.
     * @param experienciasLaborales HvExperienciaLaboral que se quiere crear.
     */
    public void crear(EntityManager em,HvExperienciasLaborales experienciasLaborales);
    /**
     * Método encargado de modificar una HvExperienciaLaboral de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param experienciasLaborales HvExperienciaLaboral con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,HvExperienciasLaborales experienciasLaborales);
    /**
     * Método encargado de eliminar de la base de datos la HvExperienciaLaboral que entra por parámetro.
     * @param experienciasLaborales HvExperienciaLaboral que se quiere eliminar.
     */
    public void borrar(EntityManager em,HvExperienciasLaborales experienciasLaborales);
    /**
     * Método encargado de buscar la HvExperienciaLaboral con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la HvExperienciaLaboral que se quiere encontrar.
     * @return Retorna la HvExperienciaLaboral identificada con la secuencia dada por parámetro.
     */
    public HvExperienciasLaborales buscarHvExperienciaLaboral(EntityManager em,BigInteger secuencia);
    /**
     * Metodo encargado de buscar las experiencias laborales(HvExperienciasLaborales) actuales de una persona.
     * @param secuenciaHV Secuencia de la hoja de vida de la persona.
     * @return Retorna una lista con las últimas experiencias laborales de una persona.
     */
    public List<HvExperienciasLaborales> experienciaLaboralPersona(EntityManager em,BigInteger secuenciaHV);
    /**
     * Metodo encargado de buscar todas las experiencias laborales(HvExperienciasLaborales) de una persona.
     * @param secuenciaHv Secuencia de la hoja de vida de la persona.
     * @return Retorna una lista con las experiencias laborales de una persona.
     */
    public List<HvExperienciasLaborales> experienciasLaboralesSecuenciaEmpleado(EntityManager em,BigInteger secuenciaHv);
}
