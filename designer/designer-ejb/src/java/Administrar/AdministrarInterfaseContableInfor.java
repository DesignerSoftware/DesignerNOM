/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.InterconInfor;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import InterfaceAdministrar.AdministrarInterfaseContableInforInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaInterconInforInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
import InterfacePersistencia.PersistenciaVWMensajeSAPBOV8Interface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarInterfaseContableInfor implements AdministrarInterfaseContableInforInterface {

   private static Logger log = Logger.getLogger(AdministrarInterfaseContableInfor.class);

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
    PersistenciaInterconInforInterface persistenciaInterconInfor;
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaContabilizacionesInterface persistenciaContabilizaciones;
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
    @EJB
    PersistenciaVWActualesFechasInterface persistenciaVWActualesFechas;
    @EJB
    PersistenciaVWMensajeSAPBOV8Interface persistenciaVWMensajesAPBOV8;
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
    public List<InterconInfor> obtenerInterconInforParametroContable(Date fechaInicial, Date fechaFinal) {
       try {
            List<InterconInfor> lista = persistenciaInterconInfor.buscarInterconInforParametroContable(em, fechaInicial, fechaFinal);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerInterconInforParametroContable Admi : " + e.toString());
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
    public Date obtenerMaxFechaInterconInfor() {
       try {
            Date fecha = persistenciaInterconInfor.obtenerFechaMaxInterconInfor(em);
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
    public void actualizarFlagProcesoAnularInterfaseContableInfor(Date fechaIni, Date fechaFin) {
        try {
            persistenciaInterconInfor.actualizarFlagProcesoAnularInterfaseContableInfor(em, fechaIni, fechaFin);
        } catch (Exception e) {
            log.warn("Error actualizarFlagProcesoAnularInterfaseContableSAPBOV8 Admi : " + e.toString());
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
    public void ejeuctarPKGUbicarnuevointercon_Infor(BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaInterconInfor.ejeuctarPKGUbicarnuevointercon_Infor(em, secuencia, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            log.warn("Error ejeuctarPKGUbicarnuevointercon_SAPBOV8 Admi : " + e.toString());
        }
    }

    @Override
    public void cambiarFlaginterconContableInfor(Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaContabilizaciones.actualizarFlahInterconContableSAPBOV8(em, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            log.warn("Error cambiarFlagInterconContableSAPBOV8 Admi : " + e.toString());
        }
    }

    @Override
    public void ejecutarDeleteInterconSAP(Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaInterconInfor.ejecutarDeleteInterconInfor(em, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            log.warn("Error ejecutarDeleteInterconSAP Admi : " + e.toString());
        }
    }

    @Override
    public void cerrarProcesoLiquidacion(Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaInterconInfor.cerrarProcesoLiquidacion(em, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            log.warn("Error cerrarProcesoLiquidacion Admi : " + e.toString());
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
            persistenciaInterconInfor.ejecutarPKGRecontabilizacion(em, fechaIni, fechaFin);
        } catch (Exception e) {
            log.warn("Error obtenerContadorFlagGeneradoFechasSAP Admi : " + e.toString());
        }
    }

    @Override
    public int contarProcesosContabilizadosInterconInfor(Date fechaInicial, Date fechaFinal) {
        try {
            int contador = persistenciaInterconInfor.contarProcesosContabilizadosInterconInfor(em, fechaInicial, fechaFinal);
            return contador;
        } catch (Exception e) {
            log.warn("Error contarProcesosContabilizadosInterconTotal Admi : " + e.toString());
            return -1;
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
    public void ejecutarPKGCrearArchivoPlano(Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
        try {
            persistenciaInterconInfor.ejecutarPKGCrearArchivoPlanoInfor(em, fechaIni, fechaFin, proceso, descripcionProceso, nombreArchivo);
        } catch (Exception e) {
            log.warn("Error ejecutarPKGCrearArchivoPlano Admi : " + e.toString());
        }
    }

    @Override
    public String obtenerEnvioInterfaseContabilidadEmpresa(short codigoEmpresa) {
        try {
            String envio = persistenciaEmpresas.obtenerEnvioInterfaseContabilidadEmpresa(em, codigoEmpresa);
            return envio;
        } catch (Exception e) {
            log.warn("Error obtenerEnvioInterfaseContabilidadEmpresa Admi : " + e.toString());
            return null;
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

    @Override
    public void cerrarProcesoContabilizacion(Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
        try {
            persistenciaInterconInfor.cerrarProcesoContabilizacion(em, fechaInicial, fechaFinal, empresa, proceso);
        } catch (Exception e) {
            log.warn("Error cerrarProcesoContabilizacion Admi : " + e.toString());
        }
    }

    @Override
    public void actualizarFlagInterconInforProcesoDeshacer(Date fechaInicial, Date fechaFinal, BigInteger proceso) {
        try {
            persistenciaInterconInfor.actualizarFlagInterconInforProcesoDeshacer(em, fechaInicial, fechaFinal, proceso);
        } catch (Exception e) {
            log.warn("Error actualizarFlagInterconTotalProcesoDeshacer Admi : " + e.toString());
        }
    }

    @Override
    public void eliminarInterconInfor(Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
        try {
            persistenciaInterconInfor.eliminarInterconInfor(em, fechaInicial, fechaFinal, empresa, proceso);
        } catch (Exception e) {
            log.warn("Error eliminarInterconSapBO Admi : " + e.toString());
        }
    }

    @Override
    public void actualizarFlagInterconInfor(Date fechaInicial, Date fechaFinal, Short empresa) {
         try {
            persistenciaInterconInfor.actualizarFlagInterconInfor(em, fechaInicial, fechaFinal, empresa);
        } catch (Exception e) {
            log.warn("Error actualizarFlagInterconSapBO Admi : " + e.toString());
        }
    }

    @Override
    public void borrarRegistroGenerado(List<SolucionesNodos> listBorrar) {
       for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaSolucionesNodos.borrar(em, listBorrar.get(i));
        }
    }

    @Override
    public void borrarRegistroIntercon(List<InterconInfor> listBorrar) {
        for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaInterconInfor.borrar(em, listBorrar.get(i));
        }
    }

}
