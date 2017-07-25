package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.InterconDynamics;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import InterfaceAdministrar.AdministrarInterfaseContableDynamicsVTInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaInterconDynamicsInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarInterfaseContableDynamicsVT implements AdministrarInterfaseContableDynamicsVTInterface {

   private static Logger log = Logger.getLogger(AdministrarInterfaseContableDynamicsVT.class);

    @EJB
    PersistenciaParametrosContablesInterface persistenciaParametrosContables;
    @EJB
    PersistenciaInterconDynamicsInterface persistenciaInterconDynamics;
    @EJB
    PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
    @EJB
    PersistenciaVWActualesFechasInterface persistenciaVWActualesFechas;
    @EJB
    PersistenciaGeneralesInterface persistenciaGenerales;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaContabilizacionesInterface persistenciaContabilizaciones;
    @EJB
    PersistenciaUsuariosInterfasesInterface persistenciaUsuariosInterfases;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    //@Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<ParametrosContables> obtenerParametrosContablesUsuarioBD(String usuarioBD) {
        try {
            List<ParametrosContables> parametro = persistenciaParametrosContables.buscarParametrosContablesUsuarioBD(em, usuarioBD);
            if (parametro != null) {
                for (int i = 0; i < parametro.size(); i++) {
                    Empresas empresa = persistenciaEmpresas.consultarEmpresaPorCodigo(em, parametro.get(i).getEmpresaCodigo());
                    if (empresa != null) {
                        parametro.get(i).setEmpresaRegistro(empresa);
                    }
                    if (parametro.get(i).getProceso() == null) {
                        parametro.get(i).setProceso(new Procesos());
                    }
                }
            }
            return parametro;
        } catch (Exception e) {
            log.warn("Error obtenerParametroContableUsuarioBD Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void modificarParametroContable(ParametrosContables parametro) {
        try {
            if (parametro.getProceso().getSecuencia() == null) {
                parametro.setProceso(null);
            }
            persistenciaParametrosContables.editar(em, parametro);
        } catch (Exception e) {
            log.warn("Error modificarParametroContable Admi : " + e.toString());

        }
    }

    @Override
    public void borrarParametroContable(List<ParametrosContables> listPC) {
        try {
            for (int i = 0; i < listPC.size(); i++) {
                if (listPC.get(i).getProceso().getSecuencia() == null) {
                    listPC.get(i).setProceso(null);
                }
                persistenciaParametrosContables.borrar(em, listPC.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarParametroContable Admi : " + e.toString());
        }
    }

    @Override
    public void crearParametroContable(ParametrosContables parametro) {
        try {
            if (parametro.getProceso().getSecuencia() == null) {
                parametro.setProceso(null);
            }
            persistenciaParametrosContables.crear(em, parametro);
        } catch (Exception e) {
            log.warn("Error modificarParametroContable Admi : " + e.toString());

        }
    }

    @Override
    public List<SolucionesNodos> obtenerSolucionesNodosParametroContable(Date fechaInicial, Date fechaFinal) {
        try {
            List<SolucionesNodos> lista = persistenciaSolucionesNodos.buscarSolucionesNodosParaParametroContable_Dynamics(em, fechaInicial, fechaFinal);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerSolucionesNodosParametroContable Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<InterconDynamics> obtenerInterconDynamicsParametroContable(Date fechaInicial, Date fechaFinal) {
        try {
            List<InterconDynamics> lista = persistenciaInterconDynamics.buscarInterconDynamicParametroContable(em, fechaInicial, fechaFinal);
            if (lista != null) {
                for (int i = 0; i < lista.size(); i++) {
                    String cargo = persistenciaInterconDynamics.obtenerCargoDYNAMICSVT(em, lista.get(i).getEmpleado().getSecuencia(), lista.get(i).getFechacontabilizacion());
                    if (cargo != null) {
                        lista.get(i).setCargo(cargo);
                    } else {
                        lista.get(i).setCargo("");
                    }
                }
            }
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerInterconDynamicsParametroContable Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Procesos> lovProcesos() {
        try {
            List<Procesos> lista = persistenciaProcesos.buscarProcesos(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error lovProcesos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Empresas> lovEmpresas() {
        try {
            List<Empresas> lista = persistenciaEmpresas.buscarEmpresas(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error lovEmpresas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public ActualUsuario obtenerActualUsuario() {
        try {
            ActualUsuario user = persistenciaActualUsuario.actualUsuarioBD(em);
            return user;
        } catch (Exception e) {
            log.warn("Error obtenerActualUsuario Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public ParametrosEstructuras parametrosLiquidacion() {
        String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
        return persistenciaParametrosEstructuras.buscarParametro(em, usuarioBD);
    }

    @Override
    public Date buscarFechaHastaVWActualesFechas() {
        try {
            Date objeto = persistenciaVWActualesFechas.actualFechaHasta(em);
            return objeto;
        } catch (Exception e) {
            log.warn("Error buscarFechaHastaVWActualesFechas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Date buscarFechaDesdeVWActualesFechas() {
        try {
            Date objeto = persistenciaVWActualesFechas.actualFechaDesde(em);
            return objeto;
        } catch (Exception e) {
            log.warn("Error buscarFechaDesdeVWActualesFechas Admi : " + e.toString());
            return null;
        }
    }

    //@Override
    public String obtenerDescripcionProcesoArchivo(BigInteger proceso) {
        try {
            String valor = persistenciaProcesos.obtenerDescripcionProcesoPorSecuencia(em, proceso);
            return valor;
        } catch (Exception e) {
            log.warn("Error obtenerDescripcionProcesoArchivo Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public String obtenerPathServidorWeb() {
        try {
            String path = persistenciaGenerales.obtenerPathServidorWeb(em);
            return path;
        } catch (Exception e) {
            log.warn("Error obtenerPathServidorWeb Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public String obtenerPathProceso() {
        try {
            String path = persistenciaGenerales.obtenerPathProceso(em);
            return path;
        } catch (Exception e) {
            log.warn("Error obtenerPathProceso Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public int contarProcesosContabilizadosInterconDynamics(Date fechaInicial, Date fechaFinal) {
        try {
            int contador = persistenciaInterconDynamics.contarProcesosContabilizadosInterconDynamics(em, fechaInicial, fechaFinal);
            return contador;
        } catch (Exception e) {
            log.warn("Error contarProcesosContabilizadosInterconDynamics Admi : " + e.toString());
            return -1;
        }
    }

    @Override
    public void cerrarProcesoContable(Date fechaInicial, Date fechaFinal, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta) {
        try {
            persistenciaInterconDynamics.cerrarProcesoContabilizacion(em, fechaInicial, fechaFinal, proceso, emplDesde, emplHasta);
        } catch (Exception e) {
            log.warn("Error cerrarProcesoContable Admi : " + e.toString());
        }
    }

    @Override
    public List<Empleados> buscarEmpleadosEmpresa() {
        try {
            List<Empleados> lista = persistenciaEmpleados.buscarEmpleados(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error buscarEmpleadosEmpresa Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void ejecutarPKGCrearArchivoPlano(Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo, BigDecimal emplDesde, BigDecimal emplHasta) {
        try {
            persistenciaInterconDynamics.ejecutarPKGCrearArchivoPlano_VT(em, fechaIni, fechaFin, proceso, descripcionProceso, nombreArchivo, emplDesde, emplHasta);
        } catch (Exception e) {
            log.warn("Error ejecutarPKGCrearArchivoPlano Admi : " + e.toString());
        }
    }

    @Override
    public Integer conteoContabilizacionesDynamics(Date fechaIni, Date fechaFin) {
        try {
            Integer conteo = persistenciaContabilizaciones.obtenerContadorFlagGeneradoFechasDynamics(em, fechaIni, fechaFin);
            return conteo;
        } catch (Exception e) {
            log.warn("Error conteoContabilizacionesDynamics Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void ejecutarPKGRecontabilizar(Date fechaIni, Date fechaFin) {
        try {
            persistenciaInterconDynamics.ejecutarPKGRecontabilizar(em, fechaIni, fechaFin);
        } catch (Exception e) {
            log.warn("Error ejecutarPKGRecontabilizar Admi : " + e.toString());
        }
    }

    @Override
    public void actualizarFlagContabilizacionDeshacerDynamics(Date fechaIni, Date fechaFin, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta) {
        try {
            persistenciaInterconDynamics.actualizarFlagContabilizacionDeshacerDynamics(em, fechaIni, fechaFin, proceso, emplDesde, emplHasta);
        } catch (Exception e) {
            log.warn("Error actualizarFlagContabilizacionDeshacerDynamics Admi : " + e.toString());
        }
    }

    @Override
    public void deleteInterconDynamics(Date fechaIni, Date fechaFin, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta) {
        try {
            persistenciaInterconDynamics.deleteInterconDynamics(em, fechaIni, fechaFin, proceso, emplDesde, emplHasta);
        } catch (Exception e) {
            log.warn("Error deleteInterconDynamics Admi : " + e.toString());
        }
    }
    
    @Override
    public void actualizarFlagContabilizacionDeshacerDynamics_NOT_EXITS(Date fechaIni, Date fechaFin, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta) {
        try {
            persistenciaInterconDynamics.actualizarFlagContabilizacionDeshacerDynamics_NOT_EXITS(em, fechaIni, fechaFin, proceso, emplDesde, emplHasta);
        } catch (Exception e) {
            log.warn("Error actualizarFlagContabilizacionDeshacerDynamics Admi : " + e.toString());
        }
    }

    @Override
    public void ejecutarPKGUbicarnuevointercon_DYNAMICS(BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso, BigDecimal emplDesde, BigDecimal emplHasta) {
        try {
            persistenciaInterconDynamics.ejecutarPKGUbicarnuevointercon_DYNAMICS(em, secuencia, fechaIni, fechaFin, proceso, emplDesde, emplHasta);
        } catch (Exception e) {
            log.warn("Error ejecutarPKGUbicarnuevointercon_DYNAMICS Admi : " + e.toString());
        }
    }

    @Override
    public void anularComprobantesCerrados(Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaInterconDynamics.anularComprobantesCerrados(em, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            log.warn("Error anularComprobantesCerrados Admi : " + e.toString());

        }
    }

    @Override
    public Date obtenerFechaMaxContabilizaciones() {
        try {
            Date fecha = persistenciaContabilizaciones.obtenerFechaMaximaContabilizaciones(em);
            return fecha;
        } catch (Exception e) {
            log.warn("Error obtenerFechaMaxContabilizaciones Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Date obtenerFechaMaxInterconDynamics() {
        try {
            Date fecha = persistenciaInterconDynamics.obtenerFechaContabilizacionMaxInterconDynamics(em);
            return fecha;
        } catch (Exception e) {
            log.warn("Error obtenerFechaMaxInterconDynamics Admi : " + e.toString());
            return null;
        }
    }
}
