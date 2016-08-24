/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.NormasLaborales;
import Entidades.VigenciasNormasEmpleados;
import InterfaceAdministrar.AdministrarVigenciaNormaLaboralInterface;
import InterfacePersistencia.PersistenciaNormasLaboralesInterface;
import InterfacePersistencia.PersistenciaVigenciasNormasEmpleadosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author John Pineda
 */
@Stateful
public class AdministrarVigenciaNormaLaboral implements AdministrarVigenciaNormaLaboralInterface {

    /**
     * CREACION DE LOS EJB
     */
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaNormasLaboralesInterface persistenciaNormasLaborales;
    @EJB
    PersistenciaVigenciasNormasEmpleadosInterface persistenciaVigenciasNormasEmpleados;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;

    /**
     * Creacion de metodos
     */
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<VigenciasNormasEmpleados> consultarVigenciasNormasEmpleadosPorEmpleado(BigInteger secEmpleado) {
        List<VigenciasNormasEmpleados> vigenciasNormasEmpleados; //esta lista es la que se mostrara en la tabla de vigencias

        try {
            vigenciasNormasEmpleados = persistenciaVigenciasNormasEmpleados.buscarVigenciasNormasEmpleadosEmpl(em, secEmpleado);
        } catch (Exception e) {
            System.out.println("Error en ADMINISTRARVIGENCIANORMALABORAL (vigenciasUbicacionesEmpleado)");
            vigenciasNormasEmpleados = null;
        }
        return vigenciasNormasEmpleados;
    }

    @Override
    public void modificarVigenciaNormaLaboral(List<VigenciasNormasEmpleados> listaVigenciasNormasEmpleados) {
        for (int i = 0; i < listaVigenciasNormasEmpleados.size(); i++) {
            persistenciaVigenciasNormasEmpleados.editar(em, listaVigenciasNormasEmpleados.get(i));
        }
    }

    @Override
    public void borrarVigenciaNormaLaboral(List<VigenciasNormasEmpleados> listaVigenciasNormasEmpleados) {
        for (int i = 0; i < listaVigenciasNormasEmpleados.size(); i++) {
            persistenciaVigenciasNormasEmpleados.borrar(em, listaVigenciasNormasEmpleados.get(i));
        }
    }

    @Override
    public void crearVigenciaNormaLaboral(List<VigenciasNormasEmpleados> listaVigenciasNormasEmpleados) {
        for (int i = 0; i < listaVigenciasNormasEmpleados.size(); i++) {
            persistenciaVigenciasNormasEmpleados.crear(em, listaVigenciasNormasEmpleados.get(i));
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
    public List<NormasLaborales> lovNormasLaborales() {
        List<NormasLaborales> normasLaborales;
        try {
            normasLaborales = persistenciaNormasLaborales.consultarNormasLaborales(em);
            return normasLaborales;
        } catch (Exception e) {
            System.err.println("ERROR EN AdministrarVigencianormaLaboral en NormasLabolares ERROR " + e);
            return null;
        }
    }
}
