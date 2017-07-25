/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.GruposConceptos;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministrarNReportesSeguridadInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaGruposConceptosInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
import InterfacePersistencia.PersistenciaSucursalesPilaInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import java.math.BigInteger;
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
public class AdministrarNReportesSeguridad implements AdministrarNReportesSeguridadInterface {

   private static Logger log = Logger.getLogger(AdministrarNReportesSeguridad.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
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
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaSucursalesPilaInterface persistenciaSucursales;

    private EntityManager em;
    List<Inforeportes> listInforeportes;
    ParametrosReportes parametroReporte;
    String usuarioActual;
    List<Empresas> listEmpresas;
    List<GruposConceptos> listGruposConceptos;
    List<Empleados> listEmpleados;
    List<Estructuras> listEstructuras;
    List<TiposTrabajadores> listTiposTrabajadores;
    List<Terceros> listTerceros;
    List<SucursalesPila> listSucursalesPila;

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
            log.warn("Error parametrosDeReporte Administrar" + e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> listInforeportesUsuario() {
        try {
            listInforeportes = persistenciaInforeportes.buscarInforeportesUsuarioSeguridadSocial(em);
            return listInforeportes;
        } catch (Exception e) {
            log.warn("Error listInforeportesUsuario " + e);
            return null;
        }
    }

    @Override
    public List<Empresas> listEmpresas() {
        try {
            listEmpresas = persistenciaEmpresas.consultarEmpresas(em);
            return listEmpresas;
        } catch (Exception e) {
            log.warn("Error listEmpresas Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<GruposConceptos> listGrupos() {
        try {
            listGruposConceptos = persistenciaGruposConceptos.buscarGruposConceptos(em);
            return listGruposConceptos;
        } catch (Exception e) {
            log.warn("Error listGruposConcetos : " + e.toString());
            return null;
        }
    }

    @Override
    public void modificarParametrosReportes(ParametrosReportes parametroInforme) {
        try {
            persistenciaParametrosReportes.editar(em, parametroInforme);
        } catch (Exception e) {
            log.warn("Error modificarParametrosReportes : " + e.toString());
        }
    }

    @Override
    public List<Terceros> listTerceros() {
        listTerceros = persistenciaTerceros.todosTerceros(em);
        return listTerceros;
    }

    @Override
    public List<TiposTrabajadores> listTiposTrabajadores() {
        listTiposTrabajadores = persistenciaTiposTrabajadores.buscarTiposTrabajadores(em);
        return listTiposTrabajadores;
    }

    @Override
    public List<Estructuras> listEstructuras() {
        try {
            listEstructuras = persistenciaEstructuras.buscarEstructuras(em);
            return listEstructuras;
        } catch (Exception e) {
            log.warn("Error listEstructuras : " + e.toString());
            return null;
        }
    }

    @Override
    public List<SucursalesPila> listSucursalesPorEmpresa(BigInteger secuenciaEmpresa) {
        listSucursalesPila = persistenciaSucursales.consultarSucursalesPilaPorEmpresa(em, secuenciaEmpresa);
        return listSucursalesPila;
    }

    @Override
    public List<SucursalesPila> listSucursales() {
        listSucursalesPila = persistenciaSucursales.consultarSucursalesPila(em);
        return listSucursalesPila;
    }

    @Override
    public List<Empleados> listEmpleados() {
        log.warn(this.getClass().getName() + ".listEmpleados()");
        log.warn(this.getClass().getName() + ".listEmpleados() entity manager" + em);
        try {
            listEmpleados = persistenciaEmpleado.buscarEmpleados(em);
            log.warn(this.getClass().getName() + ".listEmpleados() fin.");
            return listEmpleados;
        } catch (Exception e) {
            log.warn(this.getClass().getName() + " error " + e.toString());
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
            log.warn("Error guardarCambiosInfoReportes Admi : " + e.toString());
        }
    }

}
