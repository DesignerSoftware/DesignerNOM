/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import java.math.BigInteger;
import javax.persistence.EntityManager;
/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'RastrosTablas' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaRastrosTablasInterface {
    /**
     * Método encargado de verificar si un objeto de la base de datos tiene permitido usar rastros.
     * @param secObjetoTabla Secuencia del objeto de la base de datos.
     * @return Retorna true si el objeto de la base de datos tiene derecho a usar Rastros.
     */
    public boolean verificarRastroTabla(EntityManager em, BigInteger secObjetoTabla);
}
