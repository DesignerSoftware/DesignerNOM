/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.InterconSapBO;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import Entidades.VWMensajeSAPBOV8;
import InterfaceAdministrar.AdministrarInterfaseContableSapBOV8Interface;
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
import InterfacePersistencia.PersistenciaVWMensajeSAPBOV8Interface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarInterfaseContableSapBOV8 implements AdministrarInterfaseContableSapBOV8Interface{

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
            System.out.println("Error obtenerParametrosContablesUsuarioBD Admi : " + e.toString());
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
            System.out.println("Error modificarParametroContable Admi : " + e.toString());

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
            System.out.println("Error borrarParametroContable Admi : " + e.toString());
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
            System.out.println("Error modificarParametroContable Admi : " + e.toString());

        }
    }

    @Override
    public List<SolucionesNodos> obtenerSolucionesNodosParametroContable(Date fechaInicial, Date fechaFinal) {
        try {
            List<SolucionesNodos> lista = persistenciaSolucionesNodos.buscarSolucionesNodosParaParametroContable_SAP(em, fechaInicial, fechaFinal);
            return lista;
        } catch (Exception e) {
            System.out.println("Error obtenerSolucionesNodosParametroContable Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<InterconSapBO> obtenerInterconSapBOParametroContable(Date fechaInicial, Date fechaFinal) {
        try {
            List<InterconSapBO> lista = persistenciaInterconSap.buscarInterconSAPBOParametroContable(em, fechaInicial, fechaFinal);
            return lista;
        } catch (Exception e) {
            System.out.println("Error obtenerInterconSapBOParametroContable Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Procesos> lovProcesos() {
        try {
            List<Procesos> lista = persistenciaProcesos.buscarProcesos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovProcesos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Empresas> lovEmpresas() {
        try {
            List<Empresas> lista = persistenciaEmpresas.buscarEmpresas(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovEmpresas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public ActualUsuario obtenerActualUsuario() {
        try {
            ActualUsuario user = persistenciaActualUsuario.actualUsuarioBD(em);
            return user;
        } catch (Exception e) {
            System.out.println("Error obtenerActualUsuario Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Date obtenerMaxFechaContabilizaciones() {
        try {
            Date fecha = persistenciaContabilizaciones.obtenerFechaMaximaContabilizacionesSAPBOV8(em);
            return fecha;
        } catch (Exception e) {
            System.out.println("Error obtenerMaxFechaContabilizaciones Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Date obtenerMaxFechaIntercoSapBO() {
        try {
            Date fecha = persistenciaInterconSap.obtenerFechaMaxInterconSAPBO(em);
            return fecha;
        } catch (Exception e) {
            System.out.println("Error obtenerMaxFechaIntercoSapBO Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public ParametrosEstructuras parametrosLiquidacion() {
        String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
        return persistenciaParametrosEstructuras.buscarParametro(em, usuarioBD);
    }

    @Override
    public void actualizarFlagProcesoAnularInterfaseContableSAPBOV8(Date fechaIni, Date fechaFin) {
        try {
            persistenciaInterconSap.actualizarFlagProcesoAnularInterfaseContableSAPBOV8(em, fechaIni, fechaFin);
        } catch (Exception e) {
            System.out.println("Error actualizarFlagProcesoAnularInterfaseContableSAPBOV8 Admi : " + e.toString());
        }
    }

    @Override
    public Date buscarFechaHastaVWActualesFechas() {
        try {
            Date objeto = persistenciaVWActualesFechas.actualFechaHasta(em);
            return objeto;
        } catch (Exception e) {
            System.out.println("Error buscarFechaHastaVWActualesFechas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Date buscarFechaDesdeVWActualesFechas() {
        try {
            Date objeto = persistenciaVWActualesFechas.actualFechaDesde(em);
            return objeto;
        } catch (Exception e) {
            System.out.println("Error buscarFechaDesdeVWActualesFechas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void ejeuctarPKGUbicarnuevointercon_SAPBOV8(BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaInterconSap.ejeuctarPKGUbicarnuevointercon_SAPBOV8(em, secuencia, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            System.out.println("Error ejeuctarPKGUbicarnuevointercon_SAPBOV8 Admi : " + e.toString());
        }
    }

    @Override
    public void cambiarFlagInterconContableSAPBOV8(Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaContabilizaciones.actualizarFlahInterconContableSAPBOV8(em, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            System.out.println("Error cambiarFlagInterconContableSAPBOV8 Admi : " + e.toString());
        }
    }

    @Override
    public void ejecutarDeleteInterconSAP(Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaInterconSap.ejecutarDeleteInterconSAPBOV8(em, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            System.out.println("Error ejecutarDeleteInterconSAP Admi : " + e.toString());
        }
    }

    @Override
    public void cerrarProcesoLiquidacion(Date fechaIni, Date fechaFin, BigInteger proceso) {
        try {
            persistenciaInterconSap.cerrarProcesoLiquidacion(em, fechaIni, fechaFin, proceso);
        } catch (Exception e) {
            System.out.println("Error cerrarProcesoLiquidacion Admi : " + e.toString());
        }
    }

    @Override
    public Integer obtenerContadorFlagGeneradoFechasSAP(Date fechaIni, Date fechaFin) {
        try {
            Integer contador = persistenciaContabilizaciones.obtenerContadorFlagGeneradoFechasSAP(em, fechaIni, fechaFin);
            return contador;
        } catch (Exception e) {
            System.out.println("Error obtenerContadorFlagGeneradoFechasSAP Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void ejecutarPKGRecontabilizacion(Date fechaIni, Date fechaFin) {
        try {
            persistenciaInterconSap.ejecutarPKGRecontabilizacion(em, fechaIni, fechaFin);
        } catch (Exception e) {
            System.out.println("Error obtenerContadorFlagGeneradoFechasSAP Admi : " + e.toString());
        }
    }

    @Override
    public List<VWMensajeSAPBOV8> obtenerErroresSAPBOV8() {
        try {
            List<VWMensajeSAPBOV8> lista = persistenciaVWMensajesAPBOV8.buscarListaErroresSAPBOV8(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error obtenerErroresSAPBOV8 Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public int contarProcesosContabilizadosInterconSAPBO(Date fechaInicial, Date fechaFinal) {
        try {
            int contador = persistenciaInterconSap.contarProcesosContabilizadosInterconSAPBO(em, fechaInicial, fechaFinal);
            return contador;
        } catch (Exception e) {
            System.out.println("Error contarProcesosContabilizadosInterconTotal Admi : " + e.toString());
            return -1;
        }
    }

    @Override
    public String obtenerDescripcionProcesoArchivo(BigInteger proceso) {
        try {
            String valor = persistenciaProcesos.obtenerDescripcionProcesoPorSecuencia(em, proceso);
            return valor;
        } catch (Exception e) {
            System.out.println("Error obtenerDescripcionProcesoArchivo Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public String obtenerPathServidorWeb() {
        try {
            String path = persistenciaGenerales.obtenerPathServidorWeb(em);
            return path;
        } catch (Exception e) {
            System.out.println("Error obtenerPathServidorWeb Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public String obtenerPathProceso() {
        try {
            String path = persistenciaGenerales.obtenerPathProceso(em);
            return path;
        } catch (Exception e) {
            System.out.println("Error obtenerPathProceso Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void ejecutarPKGCrearArchivoPlano(Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
        try {
            persistenciaInterconSap.ejecutarPKGCrearArchivoPlanoSAPV8(em, fechaIni, fechaFin, proceso, descripcionProceso, nombreArchivo);
        } catch (Exception e) {
            System.out.println("Error ejecutarPKGCrearArchivoPlano Admi : " + e.toString());
        }
    }

    public String obtenerEnvioInterfaseContabilidadEmpresa(short codigoEmpresa) {
        try {
            String envio = persistenciaEmpresas.obtenerEnvioInterfaseContabilidadEmpresa(em, codigoEmpresa);
            return envio;
        } catch (Exception e) {
            System.out.println("Error obtenerEnvioInterfaseContabilidadEmpresa Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public UsuariosInterfases obtenerUsuarioInterfaseContabilizacion() {
        try {
            UsuariosInterfases usuario = persistenciaUsuariosInterfases.obtenerUsuarioInterfaseContabilidad(em);
            return usuario;
        } catch (Exception e) {
            System.out.println("Error obtenerUsuarioInterfaseContabilizacion Admi : " + e.toString());
            return null;
        }
    }
}
