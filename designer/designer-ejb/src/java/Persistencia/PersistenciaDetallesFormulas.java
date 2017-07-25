/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.DetallesFormulas;
import InterfacePersistencia.PersistenciaDetallesFormulasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;
/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la entidad 'DetallesFormulas',
 * la cual no es un mapeo de la base de datos sino una Entidad para albergar un resultado.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaDetallesFormulas implements PersistenciaDetallesFormulasInterface {

   private static Logger log = Logger.getLogger(PersistenciaDetallesFormulas.class);

    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public List<DetallesFormulas> detallesFormula(EntityManager em,BigInteger secEmpleado, String fechaDesde, String fechaHasta, BigInteger secProceso, BigInteger secHistoriaFormula) {
        try {
            em.clear();
            String sqlQuery = "select ROWNUM ID, LEVEL NIVEL, \n"
                    + "        POSICION,\n"
                    + "        decode(descripcion, null,\n"
                    + "          lpad(' ',level*5,' ')||signo,\n"
                    + "          lpad(' ',LEVEL*5,' ')||nombre\n"
                    + "          ) NOMBRENODO, \n"
                    + "        NODO, \n"
                    + "        operandos_pkg.ObtenerCodigo(operando) OPERANDO,\n"
                    + "        DESCRIPCION DESCRIPCION,\n"
                    + "        FORMULA FORMULA, \n"
                    + "        formulahijo FORMULAHIJO, \n"
                    + "        HISTORIAFORMULA, \n"
                    + "        HISTORIAFORMULAHIJO,\n"
                    + "        LIQUIDACIONESLOGS_PKG.ConsultarValor(?, operandos_pkg.ObtenerCodigo(operando), to_date(?,'dd/mm/yyyy'), to_date(?,'dd/mm/yyyy'), operandos_pkg.ObtenerCodigo(?))\n"
                    + "          VALOR\n"
                    + "      from vwarbolesformulas\n"
                    + "      START WITH historiaFORMULA= ?\n"
                    + "      CONNECT BY PRIOR formulahijo = FORMULA\n"
                    + "      ORDER BY LEVEL, POSICION";
            Query query = em.createNativeQuery(sqlQuery, "DetallesFormulasComprobantes");
            query.setParameter(1, secEmpleado);
            query.setParameter(2, fechaDesde);
            query.setParameter(3, fechaHasta);
            query.setParameter(4, secProceso);
            query.setParameter(5, secHistoriaFormula);
            List<DetallesFormulas> listaDetallesFormula = query.getResultList();
            return listaDetallesFormula;
        } catch (Exception e) {
            log.error("Error PersistenciaDetallesFormulas.liquidacionesCerradas. " + e);
            return null;
        }
    }
}
