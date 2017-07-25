package Administrar;

import Entidades.Empleados;
import Entidades.JornadasLaborales;
import Entidades.TiposDescansos;
import Entidades.VigenciasCompensaciones;
import Entidades.VigenciasJornadas;
import InterfaceAdministrar.AdministrarVigenciasJornadasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaJornadasLaboralesInterface;
import InterfacePersistencia.PersistenciaTiposDescansosInterface;
import InterfacePersistencia.PersistenciaVigenciasCompensacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasJornadasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarVigenciasJornadas implements AdministrarVigenciasJornadasInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasJornadas.class);

    @EJB
    PersistenciaVigenciasJornadasInterface persistenciaVigenciasJornadas;
    @EJB
    PersistenciaJornadasLaboralesInterface persistenciaJornadasLaborales;
    @EJB
    PersistenciaTiposDescansosInterface persistenciaTiposDescansos;
    @EJB
    PersistenciaVigenciasCompensacionesInterface persistenciaVigenciasCompensaciones;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    // VigenciasJornadas
    List<VigenciasJornadas> listVigenciasJornadas;
    VigenciasJornadas vigenciaJornada;
    // VigenciasCompensaciones
    List<VigenciasCompensaciones> listVigenciasCompensaciones;
    VigenciasCompensaciones vigenciaCompensacion;
    //JornadasLaborales
    List<JornadasLaborales> listJornadasLaborales;
    //TiposDescansos
    List<TiposDescansos> listTiposDescansos;
    //Empleados
    Empleados empleado;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<VigenciasJornadas> VigenciasJornadasEmpleado(BigInteger secEmpleado) {
        try {
            listVigenciasJornadas = persistenciaVigenciasJornadas.buscarVigenciasJornadasEmpleado(em, secEmpleado);
        } catch (Exception e) {
            log.warn("Error en Administrar Vigencias Jornadas (VigenciasJornadasEmpleado) : " + e.toString());
            listVigenciasJornadas = null;
        }
        return listVigenciasJornadas;
    }

    @Override
    public void modificarVJ(List<VigenciasJornadas> listVJModificadas) {
        try {
            for (int i = 0; i < listVJModificadas.size(); i++) {
                if (listVJModificadas.get(i).getJornadatrabajo().getSecuencia() == null) {
                    listVJModificadas.get(i).setJornadatrabajo(null);
                }
                if (listVJModificadas.get(i).getTipodescanso().getSecuencia() == null) {
                    listVJModificadas.get(i).setTipodescanso(null);
                }
                vigenciaJornada = listVJModificadas.get(i);
                persistenciaVigenciasJornadas.editar(em, vigenciaJornada);
            }
        } catch (Exception e) {
            log.warn("Error modificarVJ AdmiVigJor : " + e.toString());
        }
    }

    @Override
    public void borrarVJ(VigenciasJornadas vigenciasJornadas) {
        try {
            persistenciaVigenciasJornadas.borrar(em, vigenciasJornadas);
        } catch (Exception e) {
            log.warn("Error borrarVJ AdmiVigJor : " + e.toString());
        }
    }

    @Override
    public void crearVJ(VigenciasJornadas vigenciasJornadas) {
        try {
            persistenciaVigenciasJornadas.crear(em, vigenciasJornadas);
        } catch (Exception e) {
            log.warn("Error crearVJ AdmiVigJor : " + e.toString());
        }
    }

    @Override
    public List<VigenciasCompensaciones> VigenciasCompensacionesSecVigenciaTipoComp(String tipoC, BigInteger secVigencia) {
        try {
            listVigenciasCompensaciones = persistenciaVigenciasCompensaciones.buscarVigenciasCompensacionesVigenciayCompensacion(em, tipoC, secVigencia);
        } catch (Exception e) {
            log.warn("Error en Administrar Vigencias Jornadas (VigenciasCompensacionesSecVigenciaTipoComp) : " + e.toString());
            listVigenciasCompensaciones = null;
        }
        return listVigenciasCompensaciones;
    }

    @Override
    public List<VigenciasCompensaciones> VigenciasCompensacionesSecVigencia(BigInteger secVigencia) {
        try {
            listVigenciasCompensaciones = persistenciaVigenciasCompensaciones.buscarVigenciasCompensacionesVigenciaSecuencia(em, secVigencia);
        } catch (Exception e) {
            log.warn("Error en Administrar Vigencias Jornadas (VigenciasCompensacionesSecVigenciaTipoComp) : " + e.toString());
            listVigenciasCompensaciones = null;
        }
        return listVigenciasCompensaciones;
    }

    @Override
    public void modificarVC(List<VigenciasCompensaciones> listVCModificadas) {
        try {
            for (int i = 0; i < listVCModificadas.size(); i++) {
                vigenciaCompensacion = listVCModificadas.get(i);
                persistenciaVigenciasCompensaciones.editar(em, vigenciaCompensacion);
            }
        } catch (Exception e) {
            log.warn("Error modificarVC AdmiVigJor : " + e.toString());
        }
    }

    @Override
    public void borrarVC(VigenciasCompensaciones vigenciasCompensaciones) {
        try {
            persistenciaVigenciasCompensaciones.borrar(em, vigenciasCompensaciones);
        } catch (Exception e) {
            log.warn("Error borrarVC AdmiVigJor : " + e.toString());
        }
    }

    @Override
    public void crearVC(VigenciasCompensaciones vigenciasCompensaciones) {
        try {
            persistenciaVigenciasCompensaciones.crear(em, vigenciasCompensaciones);
        } catch (Exception e) {
            log.warn("Error crearVC AdmiVigJor : " + e.toString());
        }
    }

    @Override
    public List<TiposDescansos> tiposDescansos() {
        try {
            listTiposDescansos = persistenciaTiposDescansos.consultarTiposDescansos(em);
            return listTiposDescansos;
        } catch (Exception e) {
            log.warn("Error tiposDescansos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<JornadasLaborales> jornadasLaborales() {
        try {
            listJornadasLaborales = persistenciaJornadasLaborales.buscarJornadasLaborales(em);
            return listJornadasLaborales;
        } catch (Exception e) {
            log.warn("Error jornadasLaborales Admi : " + e.toString());
            return null;
        }
    }

    @Remove
    @Override
    public void salir() {
        listVigenciasJornadas = null;
        listVigenciasCompensaciones = null;
    }

    @Override
    public Empleados buscarEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            log.warn("Empleado : " + empleado.getPersona().getNombre());
            return empleado;
        } catch (Exception e) {
            log.warn("Error buscarEmpleado Adm : " + e.toString());
            empleado = null;
            return empleado;
        }
    }
}
