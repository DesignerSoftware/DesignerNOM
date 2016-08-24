package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empleados;
import Entidades.ReformasLaborales;
import Entidades.VigenciasReformasLaborales;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasReformasLaboralesInterface;
import InterfacePersistencia.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarVigenciasReformasLaborales implements AdministrarVigenciasReformasLaboralesInterface {

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaReformasLaboralesInterface persistenciaReformasLaborales;
    @EJB
    PersistenciaVigenciasReformasLaboralesInterface persistenciaVigenciasReformasLaborales;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    List<VigenciasReformasLaborales> vigenciasRefLab;
    VigenciasReformasLaborales vigenciaRefLab;
    Empleados empleado;
    List<ReformasLaborales> reformasLaborales;
    private EntityManager em;
    //LOGS//
    private ActualUsuario actualUsuario;
    private final static Logger logger = Logger.getLogger("connectionSout");
    private Date fechaDia;
    private final SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

    
    private void configurarLog() {
        //PropertyConfigurator.configure("log4j.properties");
    }

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
        //configurarLog();
    }

    @Override
    public List<VigenciasReformasLaborales> vigenciasReformasLaboralesEmpleado(BigInteger secEmpleado) {
        try {
            vigenciasRefLab = persistenciaVigenciasReformasLaborales.buscarVigenciasReformasLaboralesEmpleado(em, secEmpleado);
        } catch (Exception e) {
            logger.error("Metodo: vigenciasReformasLaboralesEmpleado - AdministrarVigenciasReformasLaborales - Fecha : " + format.format(fechaDia) + " - Usuario : " + actualUsuario.getAlias() + " - Error : " + e.toString());
            vigenciasRefLab = null;
        }
        return vigenciasRefLab;
    }

    @Override
    public void modificarVRL(List<VigenciasReformasLaborales> listVRLModificadas) {
        for (int i = 0; i < listVRLModificadas.size(); i++) {
            vigenciaRefLab = listVRLModificadas.get(i);
            persistenciaVigenciasReformasLaborales.editar(em, vigenciaRefLab);
        }
    }

    @Override
    public void borrarVRL(VigenciasReformasLaborales vigenciasReformasLaborales) {
        persistenciaVigenciasReformasLaborales.borrar(em, vigenciasReformasLaborales);
    }

    @Override
    public void crearVRL(VigenciasReformasLaborales vigenciasReformasLaborales) {
        persistenciaVigenciasReformasLaborales.crear(em, vigenciasReformasLaborales);
    }

    @Override
    public Empleados buscarEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            logger.error("Metodo: buscarEmpleado - AdministrarVigenciasReformasLaborales - Fecha : " + format.format(fechaDia) + " - Usuario : " + actualUsuario.getAlias() + " - Error : " + e.toString());
            empleado = null;
            return empleado;
        }
    }

    @Override
    public List<ReformasLaborales> reformasLaborales() {
        try {
            reformasLaborales = persistenciaReformasLaborales.buscarReformasLaborales(em);
            return reformasLaborales;
        } catch (Exception e) {
            logger.error("Metodo: reformasLaborales - AdministrarVigenciasReformasLaborales - Fecha : " + format.format(fechaDia) + " - Usuario : " + actualUsuario.getAlias() + " - Error : " + e.toString());
            return null;
        }
    }

    @Override
    public ActualUsuario obtenerActualUsuario() {
        try {
            actualUsuario = persistenciaActualUsuario.actualUsuarioBD(em);
            return actualUsuario;
        } catch (Exception e) {
            logger.error("Metodo: obtenerActualUsuario - AdministrarVigenciasReformasLaborales - Fecha : " + format.format(fechaDia) + " - Usuario : " + actualUsuario.getAlias() + " - Error : " + e.toString());
            return null;
        }
    }

    @Remove
    @Override
    public void salir() {
        vigenciaRefLab = null;
    }

}
