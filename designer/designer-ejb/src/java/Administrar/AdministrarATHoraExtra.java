package Administrar;

import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.MotivosTurnos;
import Entidades.TurnosEmpleados;
import Entidades.VWEstadosExtras;
import InterfaceAdministrar.AdministrarATHoraExtraInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaMotivosTurnosInterface;
import InterfacePersistencia.PersistenciaTurnosEmpleadosInterface;
import InterfacePersistencia.PersistenciaVWEstadosExtrasInterface;
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
public class AdministrarATHoraExtra implements AdministrarATHoraExtraInterface {

   private static Logger log = Logger.getLogger(AdministrarATHoraExtra.class);

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaTurnosEmpleadosInterface persistenciaTurnosEmpleados;
    @EJB
    PersistenciaMotivosTurnosInterface persistenciaMotivosTurnos;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    PersistenciaVWEstadosExtrasInterface persistenciaVWEstadosExtras;
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
    public List<Empleados> buscarEmpleados() {
        try {
            List<Empleados> lista = persistenciaEmpleado.buscarEmpleadosATHoraExtra(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error buscarEmpleados Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TurnosEmpleados> buscarTurnosEmpleadosPorEmpleado(BigInteger secuencia) {
        try {
            List<TurnosEmpleados> lista = persistenciaTurnosEmpleados.buscarTurnosEmpleadosPorEmpleado(em, secuencia);
            return lista;
        } catch (Exception e) {
            log.warn("Error buscarTurnosEmpleadosPorEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearTurnosEmpleados(List<TurnosEmpleados> listaTR) {
        try {
            for (int i = 0; i < listaTR.size(); i++) {
                if(listaTR.get(i).getMotivoturno().getSecuencia() == null){
                    listaTR.get(i).setMotivoturno(null);
                }
                if(listaTR.get(i).getEstructuraaprueba().getSecuencia() == null){
                    listaTR.get(i).setEstructuraaprueba(null);
                }
                persistenciaTurnosEmpleados.crear(em, listaTR.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearTurnosEmpleados Admi : " + e.toString());
        }
    }

    @Override
    public void editarTurnosEmpleados(List<TurnosEmpleados> listaTR) {
        try {
            for (int i = 0; i < listaTR.size(); i++) {
                if(listaTR.get(i).getMotivoturno().getSecuencia() == null){
                    listaTR.get(i).setMotivoturno(null);
                }
                if(listaTR.get(i).getEstructuraaprueba().getSecuencia() == null){
                    listaTR.get(i).setEstructuraaprueba(null);
                }
                persistenciaTurnosEmpleados.editar(em, listaTR.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarTurnosEmpleados Admi : " + e.toString());
        }
    }

    @Override
    public void borrarTurnosEmpleados(List<TurnosEmpleados> listaTR) {
        try {
            for (int i = 0; i < listaTR.size(); i++) {
                if(listaTR.get(i).getMotivoturno().getSecuencia() == null){
                    listaTR.get(i).setMotivoturno(null);
                }
                if(listaTR.get(i).getEstructuraaprueba().getSecuencia() == null){
                    listaTR.get(i).setEstructuraaprueba(null);
                }
                persistenciaTurnosEmpleados.borrar(em, listaTR.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarTurnosEmpleados Admi : " + e.toString());
        }
    }

    @Override
    public List<VWEstadosExtras> buscarDetallesHorasExtrasPorTurnoEmpleado(BigInteger secuencia) {
        try {
            List<VWEstadosExtras> lista = persistenciaVWEstadosExtras.buscarVWEstadosExtras(em, secuencia);
            return lista;
        } catch (Exception e) {
            log.warn("Error buscarDetallesHorasExtrasPorTurnoEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<MotivosTurnos> lovMotivosTurnos() {
        try {
            List<MotivosTurnos> lista = persistenciaMotivosTurnos.consultarMotivosTurnos(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error lovMotivosTurnos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Estructuras> lovEstructuras() {
        try {
            List<Estructuras> lista = persistenciaEstructuras.consultarEstructurasTurnoEmpleado(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error lovEstructuras Admi : " + e.toString());
            return null;
        }
    }

}
