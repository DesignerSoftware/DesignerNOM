package Administrar;

import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.TiposTrabajadores;
import Entidades.Usuarios;
import InterfaceAdministrar.AdministrarParametrosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaParametrosEstadosInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaParametrosInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarParametros implements AdministrarParametrosInterface {

    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    @EJB
    PersistenciaParametrosInterface persistenciaParametros;
    @EJB
    PersistenciaParametrosEstadosInterface persistenciaParametrosEstados;
    @EJB
    PersistenciaUsuariosInterface persistenciaUsuarios;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
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
    
    public Usuarios usuarioActual() {
        String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
        return persistenciaUsuarios.buscarUsuario(em, usuarioBD);
    }

    @Override
    public ParametrosEstructuras parametrosLiquidacion() {
        String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
        return persistenciaParametrosEstructuras.buscarParametro(em, usuarioBD);
    }

    @Override
    public List<Estructuras> lovEstructuras() {
        return persistenciaEstructuras.estructuras(em);
    }

    public List<TiposTrabajadores> lovTiposTrabajadores() {
        return persistenciaTiposTrabajadores.buscarTiposTrabajadores(em);
    }

    @Override
    public List<Procesos> lovProcesos() {
        return persistenciaProcesos.procesosParametros(em);
    }

    @Override
    public List<Parametros> empleadosParametros() {
        String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
        return persistenciaParametros.empleadosParametros(em, usuarioBD);
    }

    @Override
    public String estadoParametro(BigInteger secuenciaParametro) {
        return persistenciaParametrosEstados.parametrosComprobantes(em, secuenciaParametro);
    }

    public void crearParametroEstructura(ParametrosEstructuras parametroEstructura) {
        persistenciaParametrosEstructuras.editar(em, parametroEstructura);
    }

    public void eliminarParametros(List<Parametros> listaParametros) {
        for (int i = 0; i < listaParametros.size(); i++) {
            persistenciaParametros.borrar(em, listaParametros.get(i));
        }
    }
    
    public void crearParametros(List<Parametros> listaParametros) {
        for (int i = 0; i < listaParametros.size(); i++) {
            persistenciaParametros.crear(em, listaParametros.get(i));
        }
    }

    @Override
    public void adicionarEmpleados(BigInteger secParametroEstructura) {
        persistenciaParametrosEstructuras.adicionarEmpleados(em, secParametroEstructura);
    }

    public void borrarParametros(BigInteger secParametroEstructura) {
        persistenciaParametros.borrarParametros(em, secParametroEstructura);
    }

    public Integer empleadosParametrizados(BigInteger secProceso) {
        return persistenciaParametrosEstructuras.empleadosParametrizados(em, secProceso);
    }

    public Integer diferenciaDias(String fechaInicial, String fechaFinal) {
        return persistenciaParametrosEstructuras.diasDiferenciaFechas(em, fechaInicial, fechaFinal);
    }
    
    @Override
    public List<Empleados> empleadosLov() {
        return persistenciaEmpleado.lovEmpleadosParametros(em);
    }
}
