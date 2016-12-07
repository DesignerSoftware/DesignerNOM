/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import InterfaceAdministrar.AdministrarNReporteCapacitacionInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarNReporteCapacitacion implements AdministrarNReporteCapacitacionInterface {

    @EJB
    PersistenciaInforeportesInterface persistenciaInforeportes;
    @EJB
    PersistenciaParametrosReportesInterface persistenciaParametrosReportes;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    List<Inforeportes> listInforeportes;
    ParametrosReportes parametroReporte;
    String usuarioActual;
    /////////Lista de valores
    List<Empresas> listEmpresas;
    List<Empleados> listEmpleados;
    List<Estructuras> listEstructuras;

    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
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
    public List<Empleados> listEmpleados() {
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
    public List<Estructuras> listEstructuras() {
        try {
            listEstructuras = persistenciaEstructuras.buscarEstructuras(em);
            return listEstructuras;
        } catch (Exception e) {
            System.out.println("Error listEstructuras : " + e.toString());
            return null;
        }
    }
}
