/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.EstructurasFormulas;
import InterfacePersistencia.PersistenciaEstructurasFormulasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre las tablas de la base de datos para obtener la información
 * historica de las formulas.<br>
 * <strong>Entidad Virtual</strong>
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaEstructurasFormulas implements PersistenciaEstructurasFormulasInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public List<EstructurasFormulas> estructurasFormulasParaHistoriaFormula(EntityManager em,BigInteger secuencia) {
        try {
            em.clear();
            String sqlQuery = "select rownum ID, LEVEL NIVEL, vw.posicion POSICION,\n"
                    + "decode(vw.descripcion, null, \n"
                    + "lpad(' ',level*5,' ')|| \n"
                    + "signo, \n"
                    + "lpad(' ',LEVEL*5,' ')||vw.nombre) NOMBRENODO, \n"
                    + "vw.nodo NODO,\n"
                    + "vw.descripcion DESCRIPCION,\n"
                    + "vw.formula FORMULA, \n"
                    + "vw.formulahijo FORMULAHIJO, \n"
                    + "vw.historiaformula HISTORIAFORMULA, \n"
                    + "vw.historiaformula HISTORIAFORMULAHIJO\n"
                    + "from vwarbolesformulas vw\n"
                    + "START WITH vw.historiaformula = ? \n"
                    + "CONNECT BY PRIOR vw.formulahijo = vw.formula\n"
                    + "ORDER BY LEVEL, POSICION";
            Query query = em.createNativeQuery(sqlQuery, "ConsultaEstructurasFormulas");
            query.setParameter(1, secuencia);
            List<EstructurasFormulas> listaEstructurasFormulas = query.getResultList();
            return listaEstructurasFormulas;
        } catch (Exception e) {
            System.out.println("Error PersistenciaEstructurasFormulas.estructurasFormulasParaHistoriaFormula. " + e.toString());
            return null;
        }
    }
}
