/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.SolucionesFormulas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'SolucionesFormulas' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaSolucionesFormulasInterface {

    /**
     * Método encargado de validar si existe al menos una SolucionFormula para
     * una Novedad específica. En caso de existir la relación significa que la
     * Novedad ya fue liquidada y no puede ser eliminada.
     *
     * @param secNovedad Secuencia de la Novedad
     * @return Retorna 1 si existe la relación y 0 de lo contrario.
     */
    public int validarNovedadesNoLiquidadas(EntityManager em, BigInteger secNovedad);

    /**
     * Metodo encargado de obtener la lista de SolucionesFormulas para un
     * Empleado especifico y para una Novedad especifica.
     *
     * @param secEmpleado Secuencia del Empleado
     * @param secNovedad Secuencia de la Novedad
     * @return Retorna la lista de SolucionesFormulas para el Empleado Y Novedad
     * referenciado.
     */
    public List<SolucionesFormulas> listaSolucionesFormulasParaEmpleadoYNovedad(EntityManager em, BigInteger secEmpleado, BigInteger secNovedad);
    
    public void borrar(EntityManager em, SolucionesFormulas solucionf);
    
}
