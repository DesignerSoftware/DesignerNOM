package InterfacePersistencia;

import Entidades.Tablas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Tablas' 
 * de la base de datos.
 * @author -Felipphe-
 */
public interface PersistenciaTablasInterface {
    /**
     * Método encargado de buscar las tablas de tipo 'SISTEMA' y 'CONFIGURACION' que están asociadas a un Modulo
     * específico y existen en la tabla Pantallas.
     * @param secuenciaMod Secuencia del Modulo.
     * @return Retorna una lista de Tablas.
     */
    public List<Tablas> buscarTablas(EntityManager em, BigInteger secuenciaMod);

}
