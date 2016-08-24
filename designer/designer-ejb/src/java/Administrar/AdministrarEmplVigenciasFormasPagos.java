/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.FormasPagos;
import Entidades.MetodosPagos;
import Entidades.Periodicidades;
import Entidades.Sucursales;
import Entidades.VigenciasFormasPagos;
import InterfaceAdministrar.AdministrarEmplVigenciasFormasPagosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaFormasPagosInterface;
import InterfacePersistencia.PersistenciaMetodosPagosInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaSucursalesInterface;
import InterfacePersistencia.PersistenciaVigenciasFormasPagosInterface;
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
public class AdministrarEmplVigenciasFormasPagos implements AdministrarEmplVigenciasFormasPagosInterface {

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaSucursalesInterface persistenciaSucursales;
    @EJB
    PersistenciaVigenciasFormasPagosInterface persistenciaVigenciasFormasPagos;
    @EJB
    PersistenciaFormasPagosInterface persistenciaFormasPagos;
    @EJB
    PersistenciaMetodosPagosInterface persistenciaMetodosPagos;
    @EJB
    PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    Empleados empleado;
    VigenciasFormasPagos vigenciaFormaPagoPorEmpleado;
    List<VigenciasFormasPagos> listVigenciasFormasPagosPorEmpleado;
    List<Sucursales> listSucursales;
    List<FormasPagos> listFormasPagos;
    List<MetodosPagos> listMetodosPagos;
    List<Periodicidades> listPeriodicidades;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<VigenciasFormasPagos> consultarVigenciasFormasPagosPorEmpleado(BigInteger secEmpleado) {
        try {
            listVigenciasFormasPagosPorEmpleado = persistenciaVigenciasFormasPagos.buscarVigenciasFormasPagosPorEmpleado(em,secEmpleado);
        } catch (Exception e) {
            System.out.println("Error en Administrar Vigencias Formas Pagos");
            listVigenciasFormasPagosPorEmpleado = null;
        }
        return listVigenciasFormasPagosPorEmpleado;
    }

    @Override
    public void modificarVigenciasFormasPagos(List<VigenciasFormasPagos> listaVigenciasFormasPagos) {
        System.out.println("Administrar Modificando... Tamaño :" + listaVigenciasFormasPagos.size());
        for (int i = 0; i < listaVigenciasFormasPagos.size(); i++) {
            System.out.println("AdministrarEmplVigenciasFormasPagos FECHA INICIAL : " + listaVigenciasFormasPagos.get(i).getFechavigencia());
            System.out.println("AdministrarEmplVigenciasFormasPagos CUENTA : " + listaVigenciasFormasPagos.get(i).getCuenta());
            System.out.println("AdministrarEmplVigenciasFormasPagos FECHA CUENTA : " + listaVigenciasFormasPagos.get(i).getFechacuenta());
            System.out.println("AdministrarEmplVigenciasFormasPagos SUCURSAL : " + listaVigenciasFormasPagos.get(i).getSucursal().getNombre());
            System.out.println("AdministrarEmplVigenciasFormasPagos FORMA PAGO : " + listaVigenciasFormasPagos.get(i).getFormapago().getNombre());
            System.out.println("AdministrarEmplVigenciasFormasPagos TIPO CUENTA : " + listaVigenciasFormasPagos.get(i).getTipocuenta());
            System.out.println("AdministrarEmplVigenciasFormasPagos METODO PAGO : " + listaVigenciasFormasPagos.get(i).getMetodopago().getDescripcion());

            if (listaVigenciasFormasPagos.get(i).getSucursal().getSecuencia() == null) {
                System.out.println("ADMINISTRAR EMPLVIGENCIASFORMASPAGOS ES NULO...");
                listaVigenciasFormasPagos.get(i).setSucursal(null);
            }
            persistenciaVigenciasFormasPagos.editar(em,listaVigenciasFormasPagos.get(i));
        }
    }

    @Override
    public void borrarVigenciasFormasPagos(List<VigenciasFormasPagos> listaVigenciasFormasPagos) {
        for (int i = 0; i < listaVigenciasFormasPagos.size(); i++) {
            System.out.println("Administrar Modificando...");
            if (listaVigenciasFormasPagos.get(i).getSucursal().getSecuencia() == null) {
                System.out.println("ADMINISTRAR EMPLVIGENCIASFORMASPAGOS ES NULO...");
                listaVigenciasFormasPagos.get(i).setSucursal(null);
            }
            persistenciaVigenciasFormasPagos.borrar(em,listaVigenciasFormasPagos.get(i));
        }
    }

    @Override
    public void crearVigencasFormasPagos(VigenciasFormasPagos vigenciasFormasPagos) {
        persistenciaVigenciasFormasPagos.crear(em,vigenciasFormasPagos);
    }

    @Override
    public Empleados consultarEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleados.buscarEmpleadoSecuencia(em,secuencia);
            return empleado;
        } catch (Exception e) {
            System.out.println("AdministrarVigenciasFormasPagos buscarEmpleado error" + e);
            empleado = null;
            return empleado;
        }
    }

    @Override
    public List<Sucursales> consultarLOVSucursales() {
        try {
            listSucursales = persistenciaSucursales.consultarSucursales(em);
            return listSucursales;
        } catch (Exception e) {
            System.err.println("AdministrarVigencasFormasPagos Error en la busqueda de sucursales " + e);
            return null;
        }
    }

    @Override
    public List<FormasPagos> consultarLOVFormasPagos() {
        try {
            listFormasPagos = persistenciaFormasPagos.buscarFormasPagos(em);
            return listFormasPagos;
        } catch (Exception e) {
            System.err.println("AdministrarVigencasFormasPagos Error en la busqueda de Formas pagos " + e);
            return null;
        }
    }

    @Override
    public List<MetodosPagos> consultarLOVMetodosPagos() {
        try {
            listMetodosPagos = persistenciaMetodosPagos.buscarMetodosPagos(em);
            return listMetodosPagos;
        } catch (Exception e) {
            System.err.println("AdministrarVigencasFormasPagos Error en la busqueda de Metodos Pagos " + e);
            return null;
        }
    }

    @Override
    public List<Periodicidades> consultarLOVPerdiocidades() {
        try {
            listPeriodicidades = persistenciaPeriodicidades.consultarPeriodicidades(em);
            return listPeriodicidades;
        } catch (Exception e) {
            System.err.println("AdministrarVigencasFormasPagos Error en la busqueda de Periodicidades " + e);
            return null;
        }
    }
}
