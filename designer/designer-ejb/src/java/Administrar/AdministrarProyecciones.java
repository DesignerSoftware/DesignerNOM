/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Proyecciones;
import InterfaceAdministrar.AdministrarProyeccionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaProyeccionesInterface;
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
public class AdministrarProyecciones implements AdministrarProyeccionesInterface {

   private static Logger log = Logger.getLogger(AdministrarProyecciones.class);

    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaProyeccionesInterface persistenciaProyecciones;
    private EntityManager em;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void borrarProyecciones(List<Proyecciones> lista) {
        log.warn("AdministrarProyecciones borrarProyecciones");
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCentroCosto().getSecuencia() == null) {
                lista.get(i).setCentroCosto(null);
            }
            if (lista.get(i).getEmpleado().getSecuencia() == null) {
                lista.get(i).setEmpleado(null);
            }
            if (lista.get(i).getFormula().getSecuencia() == null) {
                lista.get(i).setFormula(null);
            }
            if (lista.get(i).getCuentaC().getSecuencia() == null) {
                lista.get(i).setCuentaC(null);
            }
            if (lista.get(i).getCuentaD().getSecuencia() == null) {
                lista.get(i).setCuentaD(null);
            }
            if (lista.get(i).getNit().getSecuencia() == null) {
                lista.get(i).setNit(null);
            }
            persistenciaProyecciones.borrar(em, lista.get(i));
        }
    }

    public List<Proyecciones> consultarProyeccionesEmpleado(BigInteger secEmpleado) {
        List<Proyecciones> lista;
        lista = persistenciaProyecciones.consultarProyecciones(em, secEmpleado);
        return lista;
    }

    public List<Empleados> consultarLOVEmpleados() {
        List<Empleados> lista;
        lista = persistenciaEmpleado.consultarEmpleadosParaProyecciones(em);
        return lista;
    }
}
