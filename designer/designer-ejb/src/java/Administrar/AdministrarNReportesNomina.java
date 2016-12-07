/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Asociaciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.GruposConceptos;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import Entidades.Terceros;
import Entidades.TiposAsociaciones;
import Entidades.TiposTrabajadores;
import Entidades.UbicacionesGeograficas;
import InterfaceAdministrar.AdministrarNReportesNominaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaAsociacionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaGruposConceptosInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTiposAsociacionesInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaUbicacionesGeograficasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarNReportesNomina implements AdministrarNReportesNominaInterface {

    @EJB
    PersistenciaInforeportesInterface persistenciaInforeportes;
    @EJB
    PersistenciaParametrosReportesInterface persistenciaParametrosReportes;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaGruposConceptosInterface persistenciaGruposConceptos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaUbicacionesGeograficasInterface persistenciaUbicacionesGeograficas;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaTiposAsociacionesInterface persistenciaTiposAsociaciones;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    @EJB
    PersistenciaAsociacionesInterface persistenciaAsociaciones;

    List<Inforeportes> listInforeportes;
    ParametrosReportes parametroReporte;
    String usuarioActual;
    /////////Lista de valores
    List<Empresas> listEmpresas;
    List<GruposConceptos> listGruposConceptos;
    List<Empleados> listEmpleados;
    List<UbicacionesGeograficas> listUbicacionesGeograficas;
    List<TiposAsociaciones> listTiposAsociaciones;
    List<Estructuras> listEstructuras;
    List<TiposTrabajadores> listTiposTrabajadores;
    List<Terceros> listTerceros;
    List<Procesos> listProcesos;
    List<Asociaciones> listAsociaciones;
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
    public ParametrosReportes parametrosDeReporte() {
        try {
            if (usuarioActual == null) {
                usuarioActual = persistenciaActualUsuario.actualAliasBD(em);
            }
            if (parametroReporte == null) {
                parametroReporte = persistenciaParametrosReportes.buscarParametroInformeUsuario(em, usuarioActual);
            }
            return parametroReporte;
        } catch (Exception e) {
            System.out.println("Error parametrosDeReporte Administrar" + e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> listInforeportesUsuario() {
        try {
            listInforeportes = persistenciaInforeportes.buscarInforeportesUsuarioNomina(em);
            return listInforeportes;
        } catch (Exception e) {
            System.out.println("Error listInforeportesUsuario " + e);
            return null;
        }
    }

    @Override
    public void modificarParametrosReportes(ParametrosReportes parametroInforme) {
        try {
            persistenciaParametrosReportes.editar(em, parametroInforme);
        } catch (Exception e) {
            System.out.println("Error modificarParametrosReportes : " + e.toString());
        }
    }

    @Override
    public List<Empresas> listEmpresas() {
        try {
            listEmpresas = persistenciaEmpresas.consultarEmpresas(em);
            return listEmpresas;
        } catch (Exception e) {
            System.out.println("Error listEmpresas Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<GruposConceptos> listGruposConcetos() {
        try {
            listGruposConceptos = persistenciaGruposConceptos.buscarGruposConceptos(em);
            return listGruposConceptos;
        } catch (Exception e) {
            System.out.println("Error listGruposConcetos : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Empleados> listEmpleados() {
        System.out.println(this.getClass().getName() + ".listEmpleados()");
        System.out.println(this.getClass().getName() + ".listEmpleados() entity manager" + em);
        try {
            listEmpleados = persistenciaEmpleado.buscarEmpleados(em);
            System.out.println(this.getClass().getName() + ".listEmpleados() fin.");
            return listEmpleados;
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " error " + e.toString());
            return null;
        }
    }

    @Override
    public List<UbicacionesGeograficas> listUbicacionesGeograficas() {
        try {
            listUbicacionesGeograficas = persistenciaUbicacionesGeograficas.consultarUbicacionesGeograficas(em);
            return listUbicacionesGeograficas;
        } catch (Exception e) {
            System.out.println("Error listUbicacionesGeograficas : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposAsociaciones> listTiposAsociaciones() {
        try {
            listTiposAsociaciones = persistenciaTiposAsociaciones.buscarTiposAsociaciones(em);
            return listTiposAsociaciones;
        } catch (Exception e) {
            System.out.println("Error listTiposAsociaciones : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Estructuras> listEstructuras() {
        try {
            listEstructuras = persistenciaEstructuras.buscarEstructuras(em);
            return listEstructuras;
        } catch (Exception e) {
            System.out.println("Error listEstructuras : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposTrabajadores> listTiposTrabajadores() {
        try {
            listTiposTrabajadores = persistenciaTiposTrabajadores.buscarTiposTrabajadores(em);
            return listTiposTrabajadores;
        } catch (Exception e) {
            System.out.println("Error listTiposTrabajadores : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Terceros> listTerceros(BigInteger secEmpresa) {
        try {
            //listTerceros = persistenciaTerceros.buscarTerceros(em);
            listTerceros = persistenciaTerceros.lovTerceros(em, secEmpresa);
            return listTerceros;
        } catch (Exception e) {
            System.out.println("Error listTerceros : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Procesos> listProcesos() {
        try {
            listProcesos = persistenciaProcesos.buscarProcesos(em);
            return listProcesos;
        } catch (Exception e) {
            System.out.println("Error listProcesos : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Asociaciones> listAsociaciones() {
        try {
            listAsociaciones = persistenciaAsociaciones.buscarAsociaciones(em);
            return listAsociaciones;
        } catch (Exception e) {
            System.out.println("Error listAsociaciones : " + e.toString());
            return null;
        }
    }

    @Override
    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR) {
        try {
            for (int i = 0; i < listaIR.size(); i++) {
                persistenciaInforeportes.editar(em, listaIR.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambiosInfoReportes Admi : " + e.toString());
        }
    }
}
