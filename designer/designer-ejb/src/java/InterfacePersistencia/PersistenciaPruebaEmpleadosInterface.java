/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.PruebaEmpleados;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan para agrupar la información de un empleado. 
 * de la base de datos.
 * @author Viktor
 */
public interface PersistenciaPruebaEmpleadosInterface {
    /**
     * Método encargado de mostrar la información referente a un empleado que se encuentra almacenada
     * en varias tablas de la base de datos.
     * @param em
     * @param secEmpleado Secuencia del empleado del que se quiere la información.
     * @return Retorna una PruebaEmpleado con la informacion del empleado.
     */
    public PruebaEmpleados empleadosAsignacion(EntityManager em, BigInteger secEmpleado);
    public List<PruebaEmpleados> empleadosNovedadesEmpl(EntityManager em);
    public List<PruebaEmpleados> empleadosNovedadesEmple(EntityManager em);
}
