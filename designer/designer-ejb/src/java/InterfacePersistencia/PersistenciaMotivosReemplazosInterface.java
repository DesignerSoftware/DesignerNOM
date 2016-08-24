/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.MotivosReemplazos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'MotivosReemplazos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaMotivosReemplazosInterface {

    public void crear(EntityManager em, MotivosReemplazos motivoReemplazo);

    public void editar(EntityManager em, MotivosReemplazos motivoReemplazo);

    public void borrar(EntityManager em, MotivosReemplazos motivoReemplazo);

    public List<MotivosReemplazos> motivosReemplazos(EntityManager em);

    public BigInteger contarEncargaturasMotivoReemplazo(EntityManager em, BigInteger secuencia);
}
