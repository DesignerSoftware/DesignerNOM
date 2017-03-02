/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Departamentos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Departamentos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaDepartamentosInterface {

    public void crear(EntityManager em, Departamentos departamentos);

    public void editar(EntityManager em, Departamentos departamentos);

    public void borrar(EntityManager em, Departamentos departamentos);

    public Departamentos consultarDepartamento(EntityManager em, BigInteger secuencia);

    public List<Departamentos> consultarDepartamentos(EntityManager em);

    public BigInteger contarSoAccidentesMedicosDepartamento(EntityManager em, BigInteger secuencia);

    public BigInteger contarCiudadesDepartamento(EntityManager em, BigInteger secuencia);

    public BigInteger contarCapModulosDepartamento(EntityManager em, BigInteger secuencia);

    public BigInteger contarBienProgramacionesDepartamento(EntityManager em, BigInteger secuencia);

    public List<Departamentos> consultarDepartamentosPorPais(EntityManager em, BigInteger secPais);
}
