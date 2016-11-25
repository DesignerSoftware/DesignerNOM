/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Actividades;
import Entidades.Empleados;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import InterfaceAdministrar.AdministrarNReporteBienestarInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActividadesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author AndresPineda
 */
@Stateless
public class AdministrarNReporteBienestar implements AdministrarNReporteBienestarInterface {

    @EJB
    PersistenciaInforeportesInterface persistenciaInforeportes;
    @EJB
    PersistenciaParametrosReportesInterface persistenciaParametrosReportes;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaActividadesInterface persistenciaActividades;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    List<Inforeportes> listInforeportes;
    ParametrosReportes parametroReporte;
    String usuarioActual;
    List<Actividades> listActividades;
    List<Empleados> listEmpleados;
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
            listInforeportes = persistenciaInforeportes.buscarInforeportesUsuarioBienestar(em);
            return listInforeportes;
        } catch (Exception e) {
            System.out.println("Error listInforeportesUsuario " + e.toString());
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
    public List<Actividades> listActividades() {
        try {
            listActividades = persistenciaActividades.buscarActividades(em);
            return listActividades;
        } catch (Exception e) {
            System.out.println("Error en listActividades Administrar: " + e.toString());
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
}
