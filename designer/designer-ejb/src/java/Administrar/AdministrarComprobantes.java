/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.DetallesFormulas;
import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import Entidades.SolucionesNodos;
import InterfaceAdministrar.AdministrarComprobantesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaDetallesFormulasInterface;
import InterfacePersistencia.PersistenciaHistoriasformulasInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaParametrosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Comprobantes'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarComprobantes implements AdministrarComprobantesInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    

    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaSolucionesNodos'.
     */
    @EJB
    PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaParametros'.
     */
    @EJB
    PersistenciaParametrosInterface persistenciaParametros;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaActualUsuario'.
     */
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaParametrosEstructuras'.
     */
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaDetallesFormulas'.
     */
    @EJB
    PersistenciaDetallesFormulasInterface persistenciaDetallesFormulas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaHistoriasformulas'.
     */
    @EJB
    PersistenciaHistoriasformulasInterface persistenciaHistoriasformulas;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Parametros> consultarParametrosComprobantesActualUsuario() {
        String usuarioBD;
        usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
        System.out.println("administrarcomprobantes consultarParametrosComprobantesActualUsuario()  actualUsuario: " + usuarioBD );
        return persistenciaParametros.parametrosComprobantes(em,usuarioBD);
    }

    @Override
    public ParametrosEstructuras consultarParametroEstructuraActualUsuario() {
        String usuarioBD;
        usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
        return persistenciaParametrosEstructuras.buscarParametro(em,usuarioBD);
    }

    @Override
    public List<SolucionesNodos> consultarSolucionesNodosEmpleado(BigInteger secEmpleado) {
        return persistenciaSolucionesNodos.solucionNodoEmpleado(em,secEmpleado);
    }

    @Override
    public List<SolucionesNodos> consultarSolucionesNodosEmpleador(BigInteger secEmpleado) {
        return persistenciaSolucionesNodos.solucionNodoEmpleador(em,secEmpleado);
    }

    @Override
    public List<DetallesFormulas> consultarDetallesFormulasEmpleado(BigInteger secEmpleado, String fechaDesde, String fechaHasta, BigInteger secProceso, BigInteger secHistoriaFormula) {
        return persistenciaDetallesFormulas.detallesFormula(em,secEmpleado, fechaDesde, fechaHasta, secProceso, secHistoriaFormula);
    }

    @Override
    public BigInteger consultarHistoriaFormulaFormula(BigInteger secFormula, String fechaDesde) {
        return persistenciaHistoriasformulas.obtenerSecuenciaHistoriaFormula(em,secFormula, fechaDesde);
    }
}
