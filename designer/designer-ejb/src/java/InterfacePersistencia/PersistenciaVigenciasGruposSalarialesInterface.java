/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.VigenciasGruposSalariales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'VigenciasGruposSalariales' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaVigenciasGruposSalarialesInterface {
    /**
     * Método encargado de insertar una VigenciaGrupoSalarial en la base de datos.
     * @param vigenciasGruposSalariales VigenciaGrupoSalarial que se quiere crear.
     */
    public void crear(EntityManager em, VigenciasGruposSalariales vigenciasGruposSalariales);
    /**
     * Método encargado de modificar una VigenciaGrupoSalarial de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param vigenciasGruposSalariales VigenciaGrupoSalarial con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, VigenciasGruposSalariales vigenciasGruposSalariales);
    /**
     * Método encargado de eliminar de la base de datos la VigenciaGrupoSalarial que entra por parámetro.
     * @param vigenciasGruposSalariales VigenciaGrupoSalarial que se quiere eliminar.
     */
    public void borrar(EntityManager em, VigenciasGruposSalariales vigenciasGruposSalariales);    
    /**
     * Método encargado de buscar la VigenciaGrupoSalarial con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la VigenciaGrupoSalarial que se quiere encontrar.
     * @return Retorna la VigenciaGrupoSalarial identificada con la secuencia dada por parámetro.
     */
    public VigenciasGruposSalariales buscarVigenciaGrupoSalarialSecuencia(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todas las VigenciasGruposSalariales existentes en la base de datos.
     * @return Retorna una lista de VigenciasGruposSalariales.
     */
    public List<VigenciasGruposSalariales> buscarVigenciasGruposSalariales(EntityManager em );
    /**
     * Método encargado de buscar todas las VigenciasGruposSalariales asociadas a un GrupoSalarial específico.
     * @param secuencia Secuencia del GrupoSalarial.
     * @return Retorna una lista de VigenciasGruposSalariales las cuales están asociadas al grupoSalarias
     * cuya secuencia coincide con la secuencia dada por parámetro.
     */
    public List<VigenciasGruposSalariales> buscarVigenciaGrupoSalarialSecuenciaGrupoSal(EntityManager em, BigInteger secuencia);
}
