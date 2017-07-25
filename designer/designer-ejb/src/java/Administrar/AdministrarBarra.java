/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.ConsultasLiquidaciones;
import Entidades.ParametrosEstructuras;
import InterfaceAdministrar.AdministrarBarraInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaCandadosInterface;
import InterfacePersistencia.PersistenciaConsultasLiquidacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaParametrosEstadosInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br> Clase encargada de realizar las operaciones lógicas para
 * la pantalla 'Barra'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarBarra implements AdministrarBarraInterface {

   private static Logger log = Logger.getLogger(AdministrarBarra.class);
    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    

    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaParametrosEstados'.
     */
    @EJB
    PersistenciaParametrosEstadosInterface persistenciaParametrosEstados;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaCandados'.
     */
    @EJB
    PersistenciaCandadosInterface persistenciaCandados;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaActualUsuario'.
     */
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaParametrosEstructuras'.
     */
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaConsultasLiquidaciones'.
     */
    @EJB
    PersistenciaConsultasLiquidacionesInterface persistenciaConsultasLiquidaciones;
    /**
     * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
     * persistencia 'persistenciaEmpresas'.
     */
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    /**
     * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
     * conexión del usuario que está usando el aplicativo.
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
    public Integer contarEmpleadosParaLiquidar() {
        return persistenciaParametrosEstados.empleadosParaLiquidar(em, consultarUsuarioBD());
    }

    @Override
    public Integer contarEmpleadosLiquidados() {
        return persistenciaParametrosEstados.empleadosLiquidados(em, consultarUsuarioBD());
    }

    @Override
    public boolean verificarPermisosLiquidar(String usuarioBD) {
        return persistenciaCandados.permisoLiquidar(em, usuarioBD);
    }

    @Override
    public String consultarUsuarioBD() {
        return persistenciaActualUsuario.actualAliasBD(em);
    }

    @Override
    public void liquidarNomina() {
        persistenciaCandados.liquidar(em);
    }

    @Override
    public String consultarEstadoLiquidacion(String usuarioBD) {
        return persistenciaCandados.estadoLiquidacion(em, usuarioBD);
    }

    @Override
    public ParametrosEstructuras consultarParametrosLiquidacion() {
        return persistenciaParametrosEstructuras.buscarParametro(em, consultarUsuarioBD());
    }

    @Override
    public void inicializarParametrosEstados() {
        persistenciaParametrosEstados.inicializarParametrosEstados(em);
    }

    @Override
    public Integer consultarProgresoLiquidacion(Integer totalEmpleadoALiquidar) {
        return persistenciaCandados.progresoLiquidacion(em, totalEmpleadoALiquidar);
    }

    @Override
    public void cancelarLiquidacion(String usuarioBD) {
        persistenciaCandados.cancelarLiquidacion(em, usuarioBD);
    }

    @Override
    public List<ConsultasLiquidaciones> liquidacionesCerradas(String fechaInicial, String fechaFinal) {
        log.warn("AdministrarBarra liquidacionesCerradas() fechaInicial : " + fechaInicial);
        log.warn("AdministrarBarra liquidacionesCerradas() fechaFinal : " + fechaFinal);
        return persistenciaConsultasLiquidaciones.liquidacionesCerradas(em, fechaInicial, fechaFinal);
    }

    @Override
    public List<ConsultasLiquidaciones> consultarPreNomina() {
        return persistenciaConsultasLiquidaciones.preNomina(em);
    }

    @Override
    public String consultarEstadoConsultaDatos(BigInteger secuenciaEmpresa) {
        return persistenciaEmpresas.estadoConsultaDatos(em, secuenciaEmpresa);
    }
}
