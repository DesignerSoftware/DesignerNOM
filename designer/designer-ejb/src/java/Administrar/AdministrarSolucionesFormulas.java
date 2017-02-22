/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Novedades;
import Entidades.SolucionesFormulas;
import InterfaceAdministrar.AdministrarSolucionesFormulasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaNovedadesInterface;
import InterfacePersistencia.PersistenciaSolucionesFormulasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'SolucionFormula'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarSolucionesFormulas implements AdministrarSolucionesFormulasInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaNovedades'.
     */
    @EJB
    PersistenciaNovedadesInterface persistenciaNovedades;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEmpleado'.
     */
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaSolucionesFormulas'.
     */
    @EJB
    PersistenciaSolucionesFormulasInterface persistenciaSolucionesFormulas;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<SolucionesFormulas> listaSolucionesFormulaParaEmpleadoYNovedad(BigInteger secEmpleado, BigInteger secNovedad) {
        try {
            List<SolucionesFormulas> lista = persistenciaSolucionesFormulas.listaSolucionesFormulasParaEmpleadoYNovedad(em, secEmpleado, secNovedad);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaSolucionesFormulaParaEmpleadoYNovedad Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Empleados empleadoActual(BigInteger codEmpleado) {
        try {
            Empleados empl = persistenciaEmpleado.buscarEmpleadoTipo(em, codEmpleado);
            return empl;
        } catch (Exception e) {
            System.out.println("Error empleadoActual Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Novedades novedadActual(BigInteger secNovedad) {
        try {
            Novedades novedad = persistenciaNovedades.buscarNovedad(em, secNovedad);
            return novedad;
        } catch (Exception e) {
            System.out.println("Error novedadActual Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void borrarSolucionesFormulas(List<SolucionesFormulas> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaSolucionesFormulas.borrar(em, listaBorrar.get(i));
        }
    }

}
