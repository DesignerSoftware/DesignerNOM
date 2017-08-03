package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.InterconTotal;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import InterfaceAdministrar.AdministrarInterfaseContableTotalInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaInterconTotalInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
import excepciones.ExcepcionBD;
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
public class AdministrarInterfaseContableTotal implements AdministrarInterfaseContableTotalInterface {

    private static Logger log = Logger.getLogger(AdministrarInterfaseContableTotal.class);

    @EJB
    PersistenciaParametrosContablesInterface persistenciaParametrosContables;
    @EJB
    PersistenciaInterconTotalInterface persistenciaInterconTotal;
    @EJB
    PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
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
    @EJB
    PersistenciaInforeportesInterface persistenciaReportes;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<ParametrosContables> obtenerParametrosContablesUsuarioBD(String usuarioBD) {
        try {
            List<ParametrosContables> listaParametros = persistenciaParametrosContables.buscarParametrosContablesUsuarioBD(em, usuarioBD);
            int n = 0;
            if (listaParametros == null) {
                n++;
            } else if (listaParametros.isEmpty()) {
                n++;
            }

            if (n > 0) {
                Empresas empresaAux = persistenciaEmpresas.consultarPrimeraEmpresaSinPaquete(em);
                ParametrosContables parametro = new ParametrosContables();
                parametro.setSecuencia(BigInteger.ONE);
                parametro.setEmpresaCodigo(empresaAux.getCodigo());
                parametro.setEmpresaRegistro(empresaAux);
                parametro.setUsuario(usuarioBD);
                parametro.setFechafinalcontabilizacion(new Date());
                parametro.setFechainicialcontabilizacion(new Date());
                parametro.setArchivo("NOMINA");
                listaParametros.add(parametro);
            }

            if (listaParametros != null) {
                if (!listaParametros.isEmpty()) {
                    for (int i = 0; i < listaParametros.size(); i++) {
                        Empresas empresa = null;
                        if (listaParametros.get(i).getEmpresaRegistro() == null) {
                            empresa = persistenciaEmpresas.consultarEmpresaPorCodigo(em, listaParametros.get(i).getEmpresaCodigo());
                        }
                        if (empresa != null) {
                            listaParametros.get(i).setEmpresaRegistro(empresa);
                        }
                        if (listaParametros.get(i).getProceso() == null) {
                            listaParametros.get(i).setProceso(new Procesos());
                        }
                    }
                }
            }
            return listaParametros;
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
                parametro.setProceso(new Procesos());
            }
            persistenciaParametrosContables.crear(em, parametro);
        } catch (Exception e) {
            log.warn("Error modificarParametroContable Admi : " + e.toString());

        }
    }

    @Override
    public List<SolucionesNodos> obtenerSolucionesNodosParametroContable(Date fechaInicial, Date fechaFinal) {
        try {
            List<SolucionesNodos> lista = persistenciaSolucionesNodos.buscarSolucionesNodosParaParametroContable(em, fechaInicial, fechaFinal);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerSolucionesNodosParametroContable Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<InterconTotal> obtenerInterconTotalParametroContable(Date fechaInicial, Date fechaFinal) {
        try {
            List<InterconTotal> lista = persistenciaInterconTotal.buscarInterconTotalParaParametroContable(em, fechaInicial, fechaFinal);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerInterconTotalParametroContable Admi : " + e.toString());
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
    public Date obtenerFechaMaxInterconTotal() {
        try {
            Date fecha = persistenciaInterconTotal.obtenerFechaContabilizacionMaxInterconTotal(em);
            return fecha;
        } catch (Exception e) {
            log.warn("Error obtenerFechaMaxInterconTotal Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public ParametrosEstructuras parametrosLiquidacion() {
        String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
        return persistenciaParametrosEstructuras.buscarParametro(em, usuarioBD);
    }

    @Override
    public void actualizarFlagInterconTotal(Date fechaInicial, Date fechaFinal, Short empresa) {
        try {
            persistenciaInterconTotal.actualizarFlagInterconTotal(em, fechaInicial, fechaFinal, empresa);
        } catch (Exception e) {
            log.warn("Error actualizarFlagInterconTotal Admi : " + e.toString());
        }
    }

    @Override
    public void actualizarFlagInterconTotalProcesoDeshacer(Date fechaInicial, Date fechaFinal, BigInteger proceso) {
        try {
            persistenciaInterconTotal.actualizarFlagInterconTotalProcesoDeshacer(em, fechaInicial, fechaFinal, proceso);
        } catch (Exception e) {
            log.warn("Error actualizarFlagInterconTotalProcesoDeshacer Admi : " + e.toString());
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
    public void ejcutarPKGUbicarnuevointercon_total(BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso) {
        try {
            persistenciaInterconTotal.ejecutarPKGUbicarnuevointercon_total(em, secuencia, fechaInicial, fechaFinal, proceso);
        } catch (Exception e) {
            log.warn("Error ejcutarPKGUbicarnuevointercon_total Admi : " + e.toString());
        }
    }

    @Override
    public void eliminarInterconTotal(Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
        try {
            persistenciaInterconTotal.eliminarInterconTotal(em, fechaInicial, fechaFinal, empresa, proceso);
        } catch (Exception e) {
            log.warn("Error eliminarInterconTotal Admi : " + e.toString());
        }
    }

    @Override
    public int contarProcesosContabilizadosInterconTotal(Date fechaInicial, Date fechaFinal) {
        try {
            int contador = persistenciaInterconTotal.contarProcesosContabilizadosInterconTotal(em, fechaInicial, fechaFinal);
            return contador;
        } catch (Exception e) {
            log.warn("Error contarProcesosContabilizadosInterconTotal Admi : " + e.toString());
            return -1;
        }
    }

    @Override
    public void cerrarProcesoContabilizacion(Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
        try {
            persistenciaInterconTotal.cerrarProcesoContabilizacion(em, fechaInicial, fechaFinal, empresa, proceso);
        } catch (Exception e) {
            log.warn("Error cerrarProcesoContabilizacion Admi : " + e.toString());
        }
    }

    @Override
    public Integer obtenerContadorFlagGeneradoFechasTotal(Date fechaIni, Date fechaFin) {
        try {
            Integer contador = persistenciaContabilizaciones.obtenerContadorFlagGeneradoFechasTotal(em, fechaIni, fechaFin);
            return contador;
        } catch (Exception e) {
            log.warn("Error obtenerContadorFlagGeneradoFechasTotal Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void ejecutarPKGRecontabilizacion(Date fechaIni, Date fechaFin) throws ExcepcionBD {
        try {
            persistenciaInterconTotal.ejecutarPKGRecontabilizacion(em, fechaIni, fechaFin);
        } catch (ExcepcionBD ebd) {
            throw ebd;
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
    public String ejecutarPKGCrearArchivoPlano(int tipoArchivo, Date fechaIni, Date fechaFin, BigInteger proceso, String nombreArchivo) {
        return persistenciaInterconTotal.ejecutarPKGCrearArchivoPlano(em, tipoArchivo, fechaIni, fechaFin, proceso, nombreArchivo);
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

    @Override
    public void borrarRegistroGenerado(List<SolucionesNodos> listBorrar) {
        for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaSolucionesNodos.borrar(em, listBorrar.get(i));
        }
    }

    @Override
    public void borrarRegistroIntercon(List<InterconTotal> listBorrar) {
        for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaInterconTotal.borrar(em, listBorrar.get(i));
        }
    }
}
