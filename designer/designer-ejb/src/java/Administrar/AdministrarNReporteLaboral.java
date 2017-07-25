/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import InterfaceAdministrar.AdministrarNReporteLaboralInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarNReporteLaboral implements AdministrarNReporteLaboralInterface {

   private static Logger log = Logger.getLogger(AdministrarNReporteLaboral.class);

    @EJB
    PersistenciaInforeportesInterface persistenciaInforeportes;
    @EJB
    PersistenciaParametrosReportesInterface persistenciaParametrosReportes;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaCargosInterface persistenciaCargos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    List<Inforeportes> listInforeportes;
    ParametrosReportes parametroReporte;
    String usuarioActual;
    List<Cargos> listCargos;
    List<Empresas> listEmpresas;
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
            log.warn("Error parametrosDeReporte Administrar" + e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> listInforeportesUsuario() {
        try {
            listInforeportes = persistenciaInforeportes.buscarInforeportesUsuarioLaboral(em);
            return listInforeportes;
        } catch (Exception e) {
            log.warn("Error listInforeportesUsuario " + e);
            return null;
        }
    }
    
    @Override
    public void modificarParametrosReportes(ParametrosReportes parametroInforme){
        log.warn("Administrar.AdministrarNReporteLaboral.modificarParametrosReportes()");
        try{
            log.warn("Ingrese al try");
            persistenciaParametrosReportes.editar(em, parametroInforme);
        }catch(Exception e){
            log.warn("Error modificarParametrosReportes : "+e.toString());
        }
    }
    
    @Override
    public List<Cargos> listCargos(){
        try{
            listCargos = persistenciaCargos.consultarCargos(em);
            return listCargos;
        }catch(Exception e){
            log.warn("Error en listCargos Administrar: "+e.toString());
            return null;
        }
    }
    
    @Override
    public List<Empleados> listEmpleados(){
        try{
            listEmpleados = persistenciaEmpleado.buscarEmpleados(em);
            return listEmpleados;
        }catch(Exception e){
            log.warn("Error listEmpleados : "+e.toString());
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
    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR) {
         log.warn("Administrar.AdministrarNReporteLaboral.guardarCambiosInfoReportes()");
        try {
            log.warn("Ingrese al try... listaIR: " + listaIR);
            for (int i = 0; i < listaIR.size(); i++) {
                log.warn("listaIR.get(i): " + listaIR.get(i));
                persistenciaInforeportes.editar(em, listaIR.get(i));
//                log.warn("listaIR.get(i): " + listaIR.get(i));                
            }
        } catch (Exception e) {
            log.warn("Error guardarCambiosInfoReportes Admi : " + e.toString());
        }
    }
}
