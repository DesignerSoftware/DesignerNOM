/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.EstructurasFormulas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Inteface encargada de determinar las operaciones necesarias para obtener la información
 * historica de las formulas.<br>
 * <strong>Entidad Virtual</strong>
 * @author Andres Pineda.
 */
public interface PersistenciaEstructurasFormulasInterface {

    /**
     * Método encargado de buscar la informacion para mostrar de una HistoriaFormula y generar las EstructurasFormulas.
     * @param secuencia Secuencia de la HistoriaFormula a la cual se desea obtener sus EstructurasFormulas
     * @return Lista de EstructurasFormulas referenciados para la HistoriaFormula
     */
    public List<EstructurasFormulas> estructurasFormulasParaHistoriaFormula(EntityManager em,BigInteger secuencia);

}
