/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.SoCondicionesAmbientalesP;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'SoCondicionesAmbientalesP' 
 * de la base de datos.
 * @author John Pineda.
 */

public interface PersistenciaSoCondicionesAmbientalesPInterface {
    /**
     * Método encargado de insertar una SoCondicionAmbientalP en la base de datos.
     * @param soCondicionesAmbientalesP SoCondicionAmbientalP que se quiere crear.
     */
    public void crear(EntityManager em, SoCondicionesAmbientalesP soCondicionesAmbientalesP);
    /**
     * Método encargado de modificar una SoCondicionAmbientalP de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param soCondicionesAmbientalesP SoCondicionAmbientalP con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, SoCondicionesAmbientalesP soCondicionesAmbientalesP) ;
    /**
     * Método encargado de eliminar de la base de datos la SoCondicionAmbientalP que entra por parámetro.
     * @param soCondicionesAmbientalesP SoCondicionAmbientalP con los cambios que se van a realizar.
     */
    public void borrar(EntityManager em, SoCondicionesAmbientalesP soCondicionesAmbientalesP);
    /**
     * Método encargado de buscar la SoCondicionAmbientalP con la secuencia dada por parámetro.
     * @param secuencia Secuencia de la SoCondicionAmbientalP que se quiere encontrar.
     * @return Retorna la SoCondicionAmbientalP identificada con la secuencia dada por parámetro.
     */
    public SoCondicionesAmbientalesP buscarSoCondicionAmbientalP(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todas las SoCondicionesAmbientalesP existentes en la base de datos, ordenadas por código.
     * @return Retorna una lista de SoCondicionesAmbientalesP.
     */
    public List<SoCondicionesAmbientalesP> buscarSoCondicionesAmbientalesP(EntityManager em);
    /**
     * Método encargado de recuperar cuantos SoAccidentesMedicos están asociados a una SoCondicionAmbientalP específica.
     * @param secuencia Secuencia de la SoCondicionAmbientalP.
     * @return Retorna el número de SoAccidentesMedicos cuyo atributo 'CondicionAmbientalP' tiene como secuencia el 
     * valor dado por parámetro.
     */
    public BigInteger contadorSoAccidentesMedicos(EntityManager em, BigInteger secuencia);
}
