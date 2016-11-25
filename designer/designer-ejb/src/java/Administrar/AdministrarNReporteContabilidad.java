/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import InterfaceAdministrar.AdministrarNReporteContabilidadInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author AndresPineda
 */
@Stateless
public class AdministrarNReporteContabilidad implements AdministrarNReporteContabilidadInterface {

    @EJB
    PersistenciaInforeportesInterface persistenciaInforeportes;
    @EJB
    PersistenciaParametrosReportesInterface persistenciaParametrosReportes;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    List<Inforeportes> listInforeportes;
    ParametrosReportes parametroReporte;
    String usuarioActual;
    List<Procesos> listProcesos;
    List<Empleados> listEmpleados;
    List<Empresas> listEmpresas;
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
            usuarioActual = persistenciaActualUsuario.actualAliasBD(em);
            parametroReporte = persistenciaParametrosReportes.buscarParametroInformeUsuario(em, usuarioActual);
            return parametroReporte;
        } catch (Exception e) {
            System.out.println("Error parametrosDeReporte Administrar" + e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> listInforeportesUsuario() {
        try {
            listInforeportes = persistenciaInforeportes.buscarInforeportesUsuarioContabilidad(em);
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
    public List<Procesos> listProcesos() {
        try {
            listProcesos = persistenciaProcesos.buscarProcesos(em);
            return listProcesos;
        } catch (Exception e) {
            System.out.println("Error en listProcesos Administrar: " + e.toString());
            return null;
        }
    }

    @Override
    public List<Empleados> listEmpleados() {
        try {
            listEmpleados = persistenciaEmpleado.buscarEmpleados(em);
            return listEmpleados;
        } catch (Exception e) {
            System.out.println("Error listEmpleados : " + e.toString());
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

    @Override
    public List<Empresas> listEmpresas() {
        try {
            listEmpresas = persistenciaEmpresas.buscarEmpresas(em);
            return listEmpresas;
        } catch (Exception e) {
            System.out.println("Error en listProcesos Administrar: " + e.toString());
            return null;
        }
    }
}
