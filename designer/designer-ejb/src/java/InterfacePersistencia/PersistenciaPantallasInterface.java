/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Pantallas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Pantallas' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaPantallasInterface {
    /**
     * Método encargado de buscar una pantalla asociada a una tabla.
     * @param secuenciaTab Secuencia de la tabla.
     * @return Retorna una Pantalla.
     */
    public Pantallas buscarPantalla(EntityManager em, BigInteger secuenciaTab);
    public List<Pantallas> buscarPantallas(EntityManager em);
    public String buscarIntContable(EntityManager em,BigInteger secEmpresa);

}
