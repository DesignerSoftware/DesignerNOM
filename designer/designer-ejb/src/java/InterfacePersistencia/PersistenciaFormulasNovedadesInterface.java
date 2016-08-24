/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.FormulasNovedades;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'FormulasNovedades' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaFormulasNovedadesInterface {

    /**
     * Método encargado de verfificar si una fórmula existe en la tabla
     * FormulasNovedades de la base de datos.
     *
     * @param secFormula Secuencia de la fórmula a verificar.
     * @return Retorna true si la formula existe en la tabla FormulasNovedades
     * de la base de datos.
     */
    public boolean verificarExistenciaFormulasNovedades(EntityManager em,BigInteger secFormula);

    public void crear(EntityManager em,FormulasNovedades formulasNovedades);

    public void editar(EntityManager em,FormulasNovedades formulasNovedades);

    public void borrar(EntityManager em,FormulasNovedades formulasNovedades);

    public List<FormulasNovedades> formulasNovedadesParaFormulaSecuencia(EntityManager em,BigInteger secuencia);
}
