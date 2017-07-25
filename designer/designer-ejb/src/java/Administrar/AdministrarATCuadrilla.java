package Administrar;

import Entidades.Cuadrillas;
import Entidades.DetallesTurnosRotativos;
import Entidades.Empleados;
import Entidades.Turnosrotativos;
import InterfaceAdministrar.AdministrarATCuadrillaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCuadrillasInterface;
import InterfacePersistencia.PersistenciaDetallesTurnosRotativosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaTurnosRotativosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarATCuadrilla implements AdministrarATCuadrillaInterface {

   private static Logger log = Logger.getLogger(AdministrarATCuadrilla.class);

    @EJB
    PersistenciaCuadrillasInterface persistenciaCuadrillas;
    @EJB
    PersistenciaTurnosRotativosInterface persistenciaTurnosRotativos;
    @EJB
    PersistenciaDetallesTurnosRotativosInterface persistenciaDetallesTurnosRotativos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;

    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Cuadrillas> obtenerCuadrillas() {
        try {
            List<Cuadrillas> lista = persistenciaCuadrillas.buscarCuadrillas(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerCuadrillas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearCuadrillas(List<Cuadrillas> listaC) {
        try {
            for (int i = 0; i < listaC.size(); i++) {
                persistenciaCuadrillas.crear(em, listaC.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearCuadrillas Admi : " + e.toString());
        }
    }

    @Override
    public void editarCuadrillas(List<Cuadrillas> listaC) {
        try {
            for (int i = 0; i < listaC.size(); i++) {
                persistenciaCuadrillas.editar(em, listaC.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearCuadrillas Admi : " + e.toString());
        }
    }

    @Override
    public void borrarCuadrillas(List<Cuadrillas> listaC) {
        try {
            for (int i = 0; i < listaC.size(); i++) {
                persistenciaCuadrillas.borrar(em, listaC.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearCuadrillas Admi : " + e.toString());
        }
    }

    @Override
    public List<Turnosrotativos> obtenerTurnosRotativos(BigInteger secuencia) {
        try {
            List<Turnosrotativos> lista = persistenciaTurnosRotativos.buscarTurnosRotativosPorCuadrilla(em, secuencia);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerTurnosRotativos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearTurnosRotativos(List<Turnosrotativos> listaTR) {
        try {
            for (int i = 0; i < listaTR.size(); i++) {
                persistenciaTurnosRotativos.crear(em, listaTR.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearTurnosRotativos Admi : " + e.toString());
        }
    }

    @Override
    public void editarTurnosRotativos(List<Turnosrotativos> listaTR) {
        try {
            for (int i = 0; i < listaTR.size(); i++) {
                persistenciaTurnosRotativos.editar(em, listaTR.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarTurnosRotativos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarTurnosRotativos(List<Turnosrotativos> listaTR) {
        try {
            for (int i = 0; i < listaTR.size(); i++) {
                persistenciaTurnosRotativos.borrar(em, listaTR.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarTurnosRotativos Admi : " + e.toString());
        }
    }

    @Override
    public List<DetallesTurnosRotativos> obtenerDetallesTurnosRotativos(BigInteger secuencia) {
        try {
            List<DetallesTurnosRotativos> lista = persistenciaDetallesTurnosRotativos.buscarDetallesTurnosRotativosPorTurnoRotativo(em, secuencia);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerDetallesTurnosRotativos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearDetallesTurnosRotativos(List<DetallesTurnosRotativos> listaDTR) {
        try {
            log.warn("Here");
            for (int i = 0; i < listaDTR.size(); i++) {
                log.warn("Here Again For : " + i);
                persistenciaDetallesTurnosRotativos.crear(em, listaDTR.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearDetallesTurnosRotativos Admi : " + e.toString());
        }
    }

    @Override
    public void editarDetallesTurnosRotativos(List<DetallesTurnosRotativos> listaDTR) {
        try {
            for (int i = 0; i < listaDTR.size(); i++) {
                persistenciaDetallesTurnosRotativos.editar(em, listaDTR.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarDetallesTurnosRotativos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarDetallesTurnosRotativos(List<DetallesTurnosRotativos> listaDTR) {
        try {
            for (int i = 0; i < listaDTR.size(); i++) {
                persistenciaDetallesTurnosRotativos.borrar(em, listaDTR.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarDetallesTurnosRotativos Admi : " + e.toString());
        }
    }

    @Override
    public List<Empleados> lovEmpleados() {
        try {
            List<Empleados> lista = persistenciaEmpleados.buscarEmpleados(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error lovEmpleados Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void borrarProgramacionCompleta() {
        try {
            persistenciaCuadrillas.borrarProgramacionCompleta(em);
        } catch (Exception e) {
            log.warn("Error borrarProgramacionCompleta Admi : " + e.toString());
        }
    }

    //@Override
    public List<Empleados> consultarEmpleadosProcesoBuscarEmpleadosCuadrillas() {
        try {
            List<Empleados> lista = persistenciaEmpleados.consultarEmpleadosCuadrillas(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error consultarEmpleadosProcesoBuscarEmpleadosCuadrillas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Cuadrillas> obtenerInfoCuadrillasPorEmpleado(BigInteger secuencia) {
        try {
            List<Cuadrillas> lista = persistenciaCuadrillas.buscarCuadrillasParaEmpleado(em, secuencia);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerInfoCuadrillasPorEmpleado Admi : " + e.toString());
            return null;
        }
    }

}
