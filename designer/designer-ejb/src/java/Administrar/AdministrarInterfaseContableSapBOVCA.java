package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.InterconSapBO;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.Terceros;
import Entidades.UsuariosInterfases;
import InterfaceAdministrar.AdministrarInterfaseContableSapBOVCAInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaInterconSapBOInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
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
public class AdministrarInterfaseContableSapBOVCA implements AdministrarInterfaseContableSapBOVCAInterface {

   private static Logger log = Logger.getLogger(AdministrarInterfaseContableSapBOVCA.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaParametrosContablesInterface persistenciaParametrosContables;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
    @EJB
    PersistenciaInterconSapBOInterface persistenciaInterconSap;
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaContabilizacionesInterface persistenciaContabilizaciones;
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
    @EJB
    PersistenciaVWActualesFechasInterface persistenciaVWActualesFechas;
    @EJB
    PersistenciaUsuariosInterfasesInterface persistenciaUsuariosInterfases;
    @EJB
    PersistenciaGeneralesInterface persistenciaGenerales;

    private EntityManager em;

    @Override
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
            log.warn("Error obtenerParametrosContablesUsuarioBD Admi : " + e.toString());
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
            List<SolucionesNodos> lista = persistenciaSolucionesNodos.buscarSolucionesNodosParaParametroContable_SAP(em, fechaInicial, fechaFinal);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerSolucionesNodosParametroContable Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<InterconSapBO> obtenerInterconSapBOVCAParametroContable(Date fechaInicial, Date fechaFinal) {
        try {
            List<InterconSapBO> lista = persistenciaInterconSap.buscarInterconSAPBOParametroContable(em, fechaInicial, fechaFinal);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerInterconSapBOVCAParametroContable Admi : " + e.toString());
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
    public Date obtenerMaxFechaContabilizaciones() {
        try {
            Date fecha = persistenciaContabilizaciones.obtenerFechaMaximaContabilizacionesSAPBOV8(em);
            return fecha;
        } catch (Exception e) {
            log.warn("Error obtenerMaxFechaContabilizaciones Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Date obtenerMaxFechaIntercoSapBO() {
        try {
            Date fecha = persistenciaInterconSap.obtenerFechaMaxInterconSAPBO(em);
            return fecha;
        } catch (Exception e) {
            log.warn("Error obtenerMaxFechaIntercoSapBO Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public ParametrosEstructuras parametrosLiquidacion() {
        String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
        return persistenciaParametrosEstructuras.buscarParametro(em, usuarioBD);
    }

    @Override
    public void actualizarFlagProcesoAnularInterfaseContableSAPBOVCA(Date fechaIni, Date fechaFin) {
        try {
            persistenciaInterconSap.actualizarFlagProcesoAnularInterfaseContableSAPBOV8(em, fechaIni, fechaFin);
        } catch (Exception e) {
            log.warn("Error actualizarFlagProcesoAnularInterfaseContableSAPBOVCA Admi : " + e.toString());
        }
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

    @Override
    public void cerrarProcesoLiquidacion(Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaInterconSap.cerrarProcesoLiquidacion(em, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            log.warn("Error cerrarProcesoLiquidacion Admi : " + e.toString());
        }
    }

    @Override
    public void cambiarFlagInterconContableSAPBOVCA(Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaContabilizaciones.actualizarFlahInterconContableSAPBOV8(em, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            log.warn("Error cambiarFlagInterconContableSAPBOVCA Admi : " + e.toString());
        }
    }

    @Override
    public void ejecutarDeleteInterconSAP(Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaInterconSap.ejecutarDeleteInterconSAPBOV8(em, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            log.warn("Error ejecutarDeleteInterconSAP Admi : " + e.toString());
        }
    }

    @Override
    public void ejeuctarPKGUbicarnuevointercon_SAPBO_VCA(BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaInterconSap.ejeuctarPKGUbicarnuevointercon_SAPBO_VCA(em, secuencia, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            log.warn("Error ejeuctarPKGUbicarnuevointercon_SAPBO_VCA Admi : " + e.toString());
        }
    }

    @Override
    public int contarProcesosContabilizadosInterconSAPBO(Date fechaInicial, Date fechaFinal) {
        try {
            int contador = persistenciaInterconSap.contarProcesosContabilizadosInterconSAPBO(em, fechaInicial, fechaFinal);
            return contador;
        } catch (Exception e) {
            log.warn("Error contarProcesosContabilizadosInterconSAPBO Admi : " + e.toString());
            return -1;
        }
    }

    @Override
    public Integer obtenerContadorFlagGeneradoFechasSAP(Date fechaIni, Date fechaFin) {
        try {
            Integer contador = persistenciaContabilizaciones.obtenerContadorFlagGeneradoFechasSAP(em, fechaIni, fechaFin);
            return contador;
        } catch (Exception e) {
            log.warn("Error obtenerContadorFlagGeneradoFechasSAP Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void ejecutarPKGRecontabilizacion(Date fechaIni, Date fechaFin) {
        try {
            persistenciaInterconSap.ejecutarPKGRecontabilizacion(em, fechaIni, fechaFin);
        } catch (Exception e) {
            log.warn("Error ejecutarPKGRecontabilizacion Admi : " + e.toString());
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
    public void ejecutarPKGCrearArchivoPlano(Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
        try {
            persistenciaInterconSap.ejecutarPKGCrearArchivoPlanoSAPVCA(em, fechaIni, fechaFin, proceso, descripcionProceso, nombreArchivo);
        } catch (Exception e) {
            log.warn("Error ejecutarPKGCrearArchivoPlano Admi : " + e.toString());
        }
    }

    @Override
    public UsuariosInterfases obtenerUsuarioInterfaseContabilizacion() {
        try {

            UsuariosInterfases usuario = persistenciaUsuariosInterfases.obtenerUsuarioInterfaseContabilidad(em);
            return usuario;
        } catch (Exception e) {
            log.warn("Error obtenerUsuarioInterfaseContabilizacion Admi : " + e.toString());
            return null;
        }
    }
}
