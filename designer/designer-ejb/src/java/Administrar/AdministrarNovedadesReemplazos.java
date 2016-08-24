/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.Estructuras;
import Entidades.MotivosReemplazos;
import Entidades.TiposReemplazos;
import InterfaceAdministrar.AdministrarNovedadesReemplazosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEncargaturasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaMotivosReemplazosInterface;
import InterfacePersistencia.PersistenciaTiposReemplazosInterface;
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
public class AdministrarNovedadesReemplazos implements AdministrarNovedadesReemplazosInterface {

    @EJB
    PersistenciaEncargaturasInterface persistenciaEncargaturas;
    @EJB
    PersistenciaMotivosReemplazosInterface persistenciaMotivosReemplazos;
    @EJB
    PersistenciaTiposReemplazosInterface persistenciaTiposReemplazos;
    @EJB
    PersistenciaCargosInterface persistenciaCargos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
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
    
    //Trae las encargaturas del empleado cuya secuencia se envía como parametro//
    @Override
    public List<Encargaturas> encargaturasEmpleado(BigInteger secEmpleado) {
        try {
            return persistenciaEncargaturas.encargaturasEmpleado(em, secEmpleado);
        } catch (Exception e) {
            System.err.println("Error AdministrarNovedadesReemplazos.encargaturasEmpleado" + e);
            return null;
        }
    }

    @Override
    public Empleados encontrarEmpleado(BigInteger secEmpleado) {
        return persistenciaEmpleados.buscarEmpleado(em, secEmpleado);
    }

    //Listas de Tipos Reemplazos, Motivos Reemplazos, Estructuras, Cargos
    @Override
    public List<Empleados> lovEmpleados() {
        return persistenciaEmpleados.buscarEmpleados(em);
    }

    @Override
    public List<TiposReemplazos> lovTiposReemplazos() {
        return persistenciaTiposReemplazos.buscarTiposReemplazos(em);
    }

    @Override
    public List<MotivosReemplazos> lovMotivosReemplazos() {
        return persistenciaMotivosReemplazos.motivosReemplazos(em);
    }

    @Override
    public List<Estructuras> lovEstructuras() {
        return persistenciaEstructuras.estructuras(em);
    }

    public List<Cargos> lovCargos() {
        return persistenciaCargos.cargosSalario(em);
    }

    /*Toca Arreglarlo con el Native Query
     @Override
     public List<Cargos> lovCargos() {
     return persistenciaCargos.cargos();
     }*/
    @Override
    public void modificarEncargatura(List<Encargaturas> listaEncargaturasModificar) {
        for (int i = 0; i < listaEncargaturasModificar.size(); i++) {
            System.out.println("Modificando...");
            if (listaEncargaturasModificar.get(i).getCargo().getSecuencia() == null) {
                listaEncargaturasModificar.get(i).setCargo(null);
            }
            if (listaEncargaturasModificar.get(i).getMotivoreemplazo().getSecuencia() == null) {
                listaEncargaturasModificar.get(i).setMotivoreemplazo(null);
            }
            if (listaEncargaturasModificar.get(i).getReemplazado().getSecuencia() == null) {
                listaEncargaturasModificar.get(i).setReemplazado(null);
            }
            if (listaEncargaturasModificar.get(i).getEstructura().getSecuencia() == null) {
                listaEncargaturasModificar.get(i).setEstructura(null);
            }
            persistenciaEncargaturas.editar(em, listaEncargaturasModificar.get(i));
        }
    }

    @Override
    public void borrarEncargaturas(Encargaturas encargaturas) {
        persistenciaEncargaturas.borrar(em, encargaturas);
    }

    @Override
    public void crearEncargaturas(Encargaturas encargaturas) {
        persistenciaEncargaturas.crear(em, encargaturas);
    }
}
