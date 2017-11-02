/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposExamenes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'TiposExamenes' 
 * de la base de datos.
 * @author John Pineda
 */
public interface PersistenciaTiposExamenesInterface {
    /**
     * Método encargado de insertar un TipoExamen en la base de datos.
     * @param tiposExamenes TipoExamen que se quiere crear.
     */
    public String crear(EntityManager em, TiposExamenes tiposExamenes);
    /**
     * Método encargado de modificar un TipoExamen de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param tiposExamenes TipoExamen con los cambios que se van a realizar.
     */
    public String editar(EntityManager em, TiposExamenes tiposExamenes);
    /**
     * Método encargado de eliminar de la base de datos el TipoExamen que entra por parámetro.
     * @param tiposExamenes TipoExamen que se quiere eliminar.
     */
    public String borrar(EntityManager em, TiposExamenes tiposExamenes);
    /**
     * Método encargado de buscar el TipoExamen con la secTipoExamen dada por parámetro.
     * @param secTipoExamen Secuencia del TipoExamen que se quiere encontrar.
     * @return Retorna el TipoExamen identificado con la secTipoExamen dada por parámetro.
     */
    public TiposExamenes buscarTipoExamen(EntityManager em, BigInteger secTipoExamen);
    /**
     * Método encargado de buscar todos los TiposExamenes existentes en la base de datos, ordenados por código.
     * @return Retorna una lista de TiposExamenes.
     */
    public List<TiposExamenes> buscarTiposExamenes(EntityManager em );
    /**
     * Método encargado de revisar si existe una relacion entre un TipoExamen definido y algún TipoExamenCargo.
     * Ademas de la revision, cuenta cuantas relaciones existen.
     * @param secTipoExamen Secuencia del TipoExamen.
     * @return Retorna el número de relaciones entre el TipoExamen cuya secTipoExamen coincida con la dada por parámetro y 
     * la tabla TiposExamenesCargos
     */
    public BigInteger contadorTiposExamenesCargos(EntityManager em, BigInteger secTipoExamen);
    /**
     * Método encargado de revisar si existe una relacion entre un TipoExamen definido y alguna VigenciasExamenesMedicos.
     * Ademas de la revision, cuenta cuantas relaciones existen.
     * @param secTipoExamen Secuencia del TipoExamen.
     * @return Retorna el número de relaciones entre el TipoExamen cuya secTipoExamen coincida con la dada por parámetro y 
     * la tabla VigenciasExamenesMedicos
     */
    public BigInteger contadorVigenciasExamenesMedicos(EntityManager em, BigInteger secTipoExamen);
}
