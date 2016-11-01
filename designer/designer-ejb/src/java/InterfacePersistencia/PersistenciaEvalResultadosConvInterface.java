/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.EvalResultadosConv;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'EvalResultadosConv' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaEvalResultadosConvInterface {

    /**
     * Método encargado de recuperar las ultimas EvalResultadosConv
     * (evaluaciones) realizadas a un empleado.
     *
     * @param em
     * @param evalresconvocatoria
     * @param secuenciaEmpleado Secuencia del empleado del que se quiere obtener
     * la información
     * @return Retorna una lista de EvalResultadosConv con las ultimas
     * evaluaciones realizadas al empleado.
     */

    public void crear(EntityManager em, EvalResultadosConv evalresconvocatoria);

    public void editar(EntityManager em, EvalResultadosConv evalresconvocatoria);

    public void borrar(EntityManager em, EvalResultadosConv evalresconvocatoria);

    public List<EvalResultadosConv> consultarEvalResultadosConvocatorias(EntityManager em,BigInteger secuenciaEmpleado);

    public List<EvalResultadosConv> pruebasAplicadasPersona(EntityManager em, BigInteger secuenciaEmpleado);
    
    public String primerPruebaAplicada(EntityManager em,BigInteger secuenciaEmpleado);
    
}
