/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Novedades;
import InterfaceAdministrar.AdministrarEmplNovedadInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaNovedadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarEmplNovedad implements AdministrarEmplNovedadInterface {

    @EJB
    PersistenciaNovedadesInterface persistenciaNovedades;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
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
    public List<Novedades> listNovedadesEmpleado(BigInteger secuenciaE) {
        try {
            List<Novedades> listNovedades = persistenciaNovedades.todasNovedadesEmpleado(em, secuenciaE);
            return listNovedades;
        } catch (Exception e) {
            System.out.println("Error listNovedadesEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Empleados actualEmpleado(BigInteger secuencia) {
        try {
            Empleados empl = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empl;
        } catch (Exception e) {
            System.out.println("Error actualEmpleado Admi : " + e.toString());
            return null;
        }
    }
}
