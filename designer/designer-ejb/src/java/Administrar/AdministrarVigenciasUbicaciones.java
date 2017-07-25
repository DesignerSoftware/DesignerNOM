package Administrar;

import Entidades.Empleados;
import Entidades.UbicacionesGeograficas;
import Entidades.VigenciasUbicaciones;
import InterfaceAdministrar.AdministrarVigenciasUbicacionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaUbicacionesGeograficasInterface;
import InterfacePersistencia.PersistenciaVigenciasUbicacionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarVigenciasUbicaciones implements AdministrarVigenciasUbicacionesInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasUbicaciones.class);

    @EJB
    PersistenciaUbicacionesGeograficasInterface persistenciaUbicacionesGeograficas;
    @EJB
    PersistenciaVigenciasUbicacionesInterface persistenciaVigenciasUbicaciones;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    
    private List<VigenciasUbicaciones> vigenciasUbicaciones;
    private VigenciasUbicaciones vu;
    private Empleados empleado;
    private List<UbicacionesGeograficas> ubicacionesGeograficas;
    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<VigenciasUbicaciones> vigenciasUbicacionesEmpleado(BigInteger secEmpleado) {
        try {
            vigenciasUbicaciones = persistenciaVigenciasUbicaciones.buscarVigenciaUbicacionesEmpleado(em, secEmpleado);
        } catch (Exception e) {
            log.warn("Error en Administrar Vigencias Ubiaciones (vigenciasUbicacionesEmpleado)");
            vigenciasUbicaciones = null;
        }
        return vigenciasUbicaciones;
    }

    @Override
    public void modificarVU(List<VigenciasUbicaciones> listVUModificadas) {
        for (int i = 0; i < listVUModificadas.size(); i++) {
            log.warn("Modificando...");
            vu = listVUModificadas.get(i);
            persistenciaVigenciasUbicaciones.editar(em, vu);
        }
    }

    @Override
    public void borrarVU(VigenciasUbicaciones vigenciasUbicaciones) {
        persistenciaVigenciasUbicaciones.borrar(em, vigenciasUbicaciones);
    }

    @Override
    public void crearVU(VigenciasUbicaciones vigenciasUbicaciones) {
        persistenciaVigenciasUbicaciones.crear(em, vigenciasUbicaciones);
    }

    @Override
    public Empleados buscarEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            empleado = null;
            return empleado;
        }
    }

    @Override
    public List<UbicacionesGeograficas> ubicacionesGeograficas() {
        try {
            ubicacionesGeograficas = persistenciaUbicacionesGeograficas.consultarUbicacionesGeograficas(em);
            return ubicacionesGeograficas;
        } catch (Exception e) {
            return null;
        }
    }
}
