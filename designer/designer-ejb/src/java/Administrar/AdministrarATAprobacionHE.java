package Administrar;

import Entidades.ActualUsuario;
import Entidades.EersCabeceras;
import Entidades.EersDetalles;
import Entidades.EersFlujos;
import Entidades.Empleados;
import Entidades.Estructuras;
import InterfaceAdministrar.AdministrarATAprobacionHEInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEersCabecerasInterface;
import InterfacePersistencia.PersistenciaEersDetallesInterface;
import InterfacePersistencia.PersistenciaEersFlujosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
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
public class AdministrarATAprobacionHE implements AdministrarATAprobacionHEInterface {

   private static Logger log = Logger.getLogger(AdministrarATAprobacionHE.class);

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    PersistenciaEersCabecerasInterface persistenciaEersCabeceras;
    @EJB
    PersistenciaEersDetallesInterface persistenciaEersDetalles;
    @EJB
    PersistenciaEersFlujosInterface persistenciaEersFlujos;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
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
    public List<EersCabeceras> obtenerTotalesEersCabeceras() {
        try {
            List<EersCabeceras> lista = persistenciaEersCabeceras.buscarEersCabecerasTotales(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerTotalesEersCabeceras Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<EersCabeceras> obtenerEersCabecerasPorEmpleado(BigInteger secuencia) {
        try {
            List<EersCabeceras> lista = persistenciaEersCabeceras.buscarEersCabecerasTotalesPorEmpleado(em, secuencia);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerEersCabecerasPorEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearEersCabeceras(List<EersCabeceras> listaEC) {
        try {
            for (int i = 0; i < listaEC.size(); i++) {
                persistenciaEersCabeceras.crear(em, listaEC.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearEersCabeceras Admi : " + e.toString());
        }
    }

    @Override
    public void editarEersCabeceras(List<EersCabeceras> listaEC) {
        try {
            for (int i = 0; i < listaEC.size(); i++) {
                persistenciaEersCabeceras.editar(em, listaEC.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarEersCabeceras Admi : " + e.toString());
        }
    }

    @Override
    public void borrarEersCabeceras(List<EersCabeceras> listaEC) {
        try {
            for (int i = 0; i < listaEC.size(); i++) {
                persistenciaEersCabeceras.borrar(em, listaEC.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarEersCabeceras Admi : " + e.toString());
        }
    }

    @Override
    public List<EersDetalles> obtenerDetallesEersCabecera(BigInteger secuencia) {
        try {
            List<EersDetalles> lista = persistenciaEersDetalles.buscarEersDetallesPorEersCabecera(em, secuencia);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerDetallesEersCabecera Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<EersFlujos> obtenerFlujosEersCabecera(BigInteger secuencia) {
        try {
            List<EersFlujos> lista = persistenciaEersFlujos.buscarEersFlujosPorEersCabecera(em, secuencia);
            return lista;
        } catch (Exception e) {
            log.warn("Error obtenerFlujosEersCabecera Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Estructuras> lovEstructuras(BigInteger secuenciaEstado) {
        try {
            List<Estructuras> lista = persistenciaEstructuras.consultarEstructurasEersCabeceras(em, secuenciaEstado);
            return lista;
        } catch (Exception e) {
            log.warn("Error lovEstructuras Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Empleados> lovEmpleados() {
        try {
            List<Empleados> lista = persistenciaEmpleado.consultarEmpleadosParaAprobarHorasExtras(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error lovEmpleados Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public ActualUsuario obtenerActualUsuarioSistema() {
        try {
            ActualUsuario usuario = persistenciaActualUsuario.actualUsuarioBD(em);
            return usuario;
        } catch (Exception e) {
            log.warn("Error obtenerActualUsuarioSistema Admi : " + e.toString());
            return null;
        }
    }

}
