/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.VWContabilidadDetallada;
import Entidades.VWContabilidadResumida1;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVWContabilidadResumida1Interface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaInterconSapBOInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaVWContabilidadResumida1Interface;
import Persistencia.PersistenciaInterconSapBO;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarVWContabilidadResumida implements AdministrarVWContabilidadResumida1Interface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaVWContabilidadResumida1Interface persistenciaContabilidadResumida;
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    @EJB
    PersistenciaParametrosContablesInterface persistenciaParametrosContables;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaContabilizacionesInterface persistenciaContabilizaciones;
    @EJB
    PersistenciaInterconSapBOInterface persistenciaInterconSapBO;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<VWContabilidadResumida1> obtenerContabilidadResumida(Date FechaIni, Date FechaFin, BigInteger secProceso) {
        List<VWContabilidadResumida1> lista = persistenciaContabilidadResumida.buscarContabilidadResumidaParametroContable(em, FechaIni, FechaFin, secProceso);
        return lista;
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
            System.out.println("Error obtenerParametroContableUsuarioBD Admi : " + e.toString());
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
    public Date obtenerFechaMaxContabilizaciones() {
        try {
            Date fecha = persistenciaContabilizaciones.obtenerFechaMaximaContabilizaciones(em);
            return fecha;
        } catch (Exception e) {
            System.out.println("Error obtenerFechaMaxContabilizaciones Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Date obtenerFechaMaxInterconSapBO() {
        try {
            Date fecha = persistenciaInterconSapBO.obtenerFechaMaxInterconSAPBO(em);
            return fecha;
        } catch (Exception e) {
            System.out.println("Error obtenerFechaMaxInterconTotal Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public ParametrosEstructuras parametrosLiquidacion() {
        String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
        return persistenciaParametrosEstructuras.buscarParametro(em, usuarioBD);
    }

    @Override
    public List<VWContabilidadDetallada> obtenerContabilidadDetallada(Date FechaIni, Date FechaFin, BigInteger secProceso) {
        List<VWContabilidadDetallada> lista = persistenciaContabilidadResumida.buscarContabilidadDetalladaParametroContable(em, FechaIni, FechaFin, secProceso);
        return lista;
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
    public Integer abrirPeriodoContable(Date FechaIni, Date FechaFin, BigInteger secProceso) {
        try {
            Integer proceso = persistenciaContabilidadResumida.abrirPeriodoContable(em, FechaIni, FechaFin, secProceso);
            return proceso;
        } catch (Exception e) {
            System.out.println("Error en abrirPeriodoContable Admi : " + e.getMessage());
            return null;
        }
    }

    @Override
    public void actualizarPeriodoContable(Date FechaIni, Date FechaFin, BigInteger secProceso) {
        persistenciaContabilidadResumida.actualizarPeriodoContable(em, FechaIni, FechaFin, secProceso);
    }

}
