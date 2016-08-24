/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.IbcsAutoliquidaciones;
import Entidades.Procesos;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarIbcAutoliquidInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaIbcsAutoliquidacionesInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
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
public class AdministrarIbcAutoliquid implements AdministrarIbcAutoliquidInterface {

    @EJB
    PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
    @EJB
    PersistenciaIbcsAutoliquidacionesInterface persistenciaIbcsAutoliquidaciones;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
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
    public List<TiposEntidades> listTiposEntidadesIBCS() {
        try {
            List<TiposEntidades> listTiposEntidades;
            listTiposEntidades = persistenciaTiposEntidades.buscarTiposEntidadesIBCS(em);
            return listTiposEntidades;
        } catch (Exception e) {
            System.out.println("Error listTiposEntidadesIBCS Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<IbcsAutoliquidaciones> listIBCSAutoliquidaciones(BigInteger secuenciaTE, BigInteger secuenciaEmpl) {
        try {
            List<IbcsAutoliquidaciones> IbcsAutoliquidaciones;
            IbcsAutoliquidaciones = persistenciaIbcsAutoliquidaciones.buscarIbcsAutoliquidacionesTipoEntidadEmpleado(em, secuenciaTE, secuenciaEmpl);
            return IbcsAutoliquidaciones;
        } catch (Exception e) {
            System.out.println("Error listIBCSAutoliquidaciones Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> crearIbcsAutoliquidaciones) {
        try {
            for (int i = 0; i < crearIbcsAutoliquidaciones.size(); i++) {
                if (crearIbcsAutoliquidaciones.get(i).getProceso().getSecuencia() == null) {
                    crearIbcsAutoliquidaciones.get(i).setProceso(null);
                }
                persistenciaIbcsAutoliquidaciones.crear(em, crearIbcsAutoliquidaciones.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en crearCargo Admi : " + e.toString());
        }
    }

    @Override
    public void modificarIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> modificarIbcsAutoliquidaciones) {
        try {
            for (int i = 0; i < modificarIbcsAutoliquidaciones.size(); i++) {
                if (modificarIbcsAutoliquidaciones.get(i).getProceso().getSecuencia() == null) {
                    modificarIbcsAutoliquidaciones.get(i).setProceso(null);
                }
                persistenciaIbcsAutoliquidaciones.editar(em, modificarIbcsAutoliquidaciones.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en modificarCargo Admi : " + e.toString());
        }
    }

    @Override
    public void borrarIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> borrarIbcsAutoliquidaciones) {
        try {
            for (int i = 0; i < borrarIbcsAutoliquidaciones.size(); i++) {
                if (borrarIbcsAutoliquidaciones.get(i).getProceso().getSecuencia() == null) {
                    borrarIbcsAutoliquidaciones.get(i).setProceso(null);
                }
                persistenciaIbcsAutoliquidaciones.borrar(em, borrarIbcsAutoliquidaciones.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en borrarCargo Admi : " + e.toString());
        }
    }

    @Override
    public List<Procesos> listProcesos() {
        try {
            List<Procesos> listProcesos;
            listProcesos = persistenciaProcesos.buscarProcesos(em);
            return listProcesos;
        } catch (Exception e) {
            System.out.println("Error listProcesos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Empleados empleadoActual(BigInteger secuencia) {
        try {
            Empleados empl = persistenciaEmpleado.buscarEmpleado(em, secuencia);
            return empl;
        } catch (Exception e) {
            System.out.println("Error empleadoActual Admi : " + e.toString());
            return null;
        }
    }
}
