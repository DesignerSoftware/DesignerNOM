/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Enfermedades;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Enfermedades' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaEnfermedadesInterface {

    public String crear(EntityManager em,Enfermedades enfermedades);

    public String editar(EntityManager em,Enfermedades enfermedades);

    public String borrar(EntityManager em,Enfermedades enfermedades);

    public Enfermedades buscarEnfermedad(EntityManager em,BigInteger secEnfermedades);

    public List<Enfermedades> buscarEnfermedades(EntityManager em);

    public BigInteger contadorAusentimos(EntityManager em,BigInteger secEnfermedades);

    public BigInteger contadorDetallesLicencias(EntityManager em,BigInteger secEnfermedades);

    public BigInteger contadorEnfermedadesPadecidas(EntityManager em,BigInteger secEnfermedades);

    public BigInteger contadorSoausentismos(EntityManager em,BigInteger secEnfermedades);

    public BigInteger contadorSorevisionessSistemas(EntityManager em,BigInteger secEnfermedades);
}
