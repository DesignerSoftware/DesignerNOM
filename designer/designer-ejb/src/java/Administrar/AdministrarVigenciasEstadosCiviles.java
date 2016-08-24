/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.VigenciasEstadosCiviles;
import Entidades.EstadosCiviles;
import Entidades.Empleados;
import InterfaceAdministrar.AdministrarVigenciasEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaVigenciasEstadosCivilesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarVigenciasEstadosCiviles implements AdministrarVigenciasEstadosCivilesInterface {

    /**
     * CREACION DE LOS EJB
     */
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaEstadosCivilesInterface persistenciaEstadosCiviles;
    @EJB
    PersistenciaVigenciasEstadosCivilesInterface persistenciaVigenciasEstadosCiviles;
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

    /**
     * Creacion de metodos
     */
    public List<VigenciasEstadosCiviles> consultarVigenciasEstadosCivilesPorEmpleado(BigInteger secEmpleado) {
        List<VigenciasEstadosCiviles> vigenciasEstadosCiviles; //esta lista es la que se mostrara en la tabla de vigencias

        try {
            vigenciasEstadosCiviles = persistenciaVigenciasEstadosCiviles.consultarVigenciasEstadosCivilesPorPersona(em, secEmpleado);
        } catch (Exception e) {
            System.out.println("Error en ADMINISTRARVIGENCIANORMALABORAL (vigenciasUbicacionesEmpleado)");
            vigenciasEstadosCiviles = null;
        }
        return vigenciasEstadosCiviles;
    }

    public List<VigenciasEstadosCiviles> consultarVigenciasEstadosCivilesPorEmpleado() {
        List<VigenciasEstadosCiviles> vigenciasEstadosCiviles; //esta lista es la que se mostrara en la tabla de vigencias

        try {
            vigenciasEstadosCiviles = persistenciaVigenciasEstadosCiviles.consultarVigenciasEstadosCiviles(em);
        } catch (Exception e) {
            System.out.println("Error en ADMINISTRARVIGENCIANORMALABORAL (vigenciasUbicacionesEmpleado)");
            vigenciasEstadosCiviles = null;
        }
        return vigenciasEstadosCiviles;
    }

    @Override
    public void modificarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
        for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            System.out.println("Modificando...");
            persistenciaVigenciasEstadosCiviles.editar(em, listaVigenciasEstadosCiviles.get(i));
        }
    }

    @Override
    public void borrarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
        for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            System.out.println("borrar...");
            persistenciaVigenciasEstadosCiviles.borrar(em, listaVigenciasEstadosCiviles.get(i));
        }
    }

    @Override
    public void crearVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
        for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            System.out.println("crear...");
            persistenciaVigenciasEstadosCiviles.crear(em, listaVigenciasEstadosCiviles.get(i));
        }
    }

    @Override
    public Empleados consultarEmpleado(BigInteger secuencia) {
        Empleados empleado;
        try {
            empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            empleado = null;
            return empleado;
        }
    }

    @Override
    public List<EstadosCiviles> lovEstadosCiviles() {
        List<EstadosCiviles> normasLaborales;

        try {
            normasLaborales = persistenciaEstadosCiviles.consultarEstadosCiviles(em);
            return normasLaborales;
        } catch (Exception e) {
            System.err.println("ERROR EN AdministrarVigencianormaLaboral en NormasLabolares ERROR " + e);
            return null;
        }
    }
}
