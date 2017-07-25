/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Proyectos;
import Entidades.PryRoles;
import Entidades.VigenciasProyectos;
import InterfaceAdministrar.AdministrarVigenciasProyectosInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaProyectosInterface;
import InterfacePersistencia.PersistenciaPryRolesInterface;
import InterfacePersistencia.PersistenciaVigenciasProyectosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarVigenciasProyectos implements AdministrarVigenciasProyectosInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasProyectos.class);

    @EJB
    PersistenciaVigenciasProyectosInterface persistenciaVigenciasProyectos;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaProyectosInterface persistenciaProyectos;
    @EJB
    PersistenciaPryRolesInterface persistenciaPryRoles;
    @EJB
    PersistenciaCargosInterface persistenciaCargos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    
    private VigenciasProyectos vP;
    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public List<VigenciasProyectos> vigenciasProyectosEmpleado(BigInteger secEmpleado) {
        try {
            return persistenciaVigenciasProyectos.vigenciasProyectosEmpleado(em, secEmpleado);
        } catch (Exception e) {
            log.error("Error AdministrarVigenciasProyectos.vigenciasProyectosPersona " + e);
            return null;
        }
    }

    public Empleados encontrarEmpleado(BigInteger secEmpleado) {
        return persistenciaEmpleados.buscarEmpleado(em, secEmpleado);
    }

    //Listas de Valores Educacion, Profesion, Instituciones, Adiestramiento
    @Override
    public List<Proyectos> lovProyectos() {
        return persistenciaProyectos.proyectos(em);
    }

    @Override
    public List<PryRoles> lovPryRoles() {
        return persistenciaPryRoles.pryroles(em);
    }

    @Override
    public List<Cargos> lovCargos() {
        return persistenciaCargos.consultarCargos(em);
    }
       
    @Override
    public void modificarVigenciaProyecto(List<VigenciasProyectos> listaVigenciasProyectosModificar) {
        for (int i = 0; i < listaVigenciasProyectosModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasProyectosModificar.get(i).getProyecto().getSecuencia() == null) {
                listaVigenciasProyectosModificar.get(i).setProyecto(null);
                vP = listaVigenciasProyectosModificar.get(i);
            } else {
                vP = listaVigenciasProyectosModificar.get(i);
            }
            if (listaVigenciasProyectosModificar.get(i).getPryRol().getSecuencia() == null) {
                listaVigenciasProyectosModificar.get(i).setPryRol(null);
                vP = listaVigenciasProyectosModificar.get(i);
            } else {
                vP = listaVigenciasProyectosModificar.get(i);
            }
            if (listaVigenciasProyectosModificar.get(i).getPryCargoproyecto().getSecuencia() == null) {
                listaVigenciasProyectosModificar.get(i).setPryCargoproyecto(null);
                vP = listaVigenciasProyectosModificar.get(i);
            } else {
                vP = listaVigenciasProyectosModificar.get(i);
            }
                       
            
            persistenciaVigenciasProyectos.editar(em, vP);
        }
    }

    @Override
    public void borrarVigenciaProyecto(VigenciasProyectos vigenciasProyectos) {
        persistenciaVigenciasProyectos.borrar(em, vigenciasProyectos);
    }


    @Override
    public void crearVigenciaProyecto(VigenciasProyectos vigenciasProyectos) {
        persistenciaVigenciasProyectos.crear(em, vigenciasProyectos);
    }
    
    @Override
    public Proyectos buscarProyectoPorNombreVigencia(String nombre){
        try{
            Proyectos proyecto = persistenciaProyectos.buscarProyectoNombre(em, nombre);
            return proyecto;
        } catch(Exception e){
            return null;
        }
    }
}
