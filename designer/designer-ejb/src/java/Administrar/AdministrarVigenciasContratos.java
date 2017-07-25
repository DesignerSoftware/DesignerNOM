
package Administrar;


import Entidades.Contratos;
import Entidades.Empleados;
import Entidades.TiposContratos;
import Entidades.VigenciasContratos;
import InterfaceAdministrar.AdministrarVigenciasContratosInterface;
import InterfacePersistencia.PersistenciaContratosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaTiposContratosInterface;
import InterfacePersistencia.PersistenciaVigenciasContratosInterface;
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

public class AdministrarVigenciasContratos implements AdministrarVigenciasContratosInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasContratos.class);

    @EJB
    PersistenciaContratosInterface persistenciaContratos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaTiposContratosInterface persistenciaTiposContratos;
    @EJB
    PersistenciaVigenciasContratosInterface persistenciaVigenciasContratos;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    
    List<VigenciasContratos> vigenciasContratos;
    VigenciasContratos vigenciaContrato;
    Empleados empleado;
    List<Contratos> contratos;
    List<TiposContratos> tiposContratos;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public List<VigenciasContratos> VigenciasContratosEmpleado(BigInteger secEmpleado) {
        try {
            vigenciasContratos = persistenciaVigenciasContratos.buscarVigenciaContratoEmpleado(em, secEmpleado);
        } catch (Exception e) {
            log.warn("Error en Administrar Vigencias Contratos (VigenciasContratosEmpleado)");
            vigenciasContratos = null;
        }
        return vigenciasContratos;
    }

    @Override
    public void modificarVC(List<VigenciasContratos> listVCModificadas) {
        for (int i = 0; i < listVCModificadas.size(); i++) {
            log.warn("Modificando...");
            vigenciaContrato = listVCModificadas.get(i);
            persistenciaVigenciasContratos.editar(em, vigenciaContrato);
        }
    }

    @Override
    public void borrarVC(VigenciasContratos vigenciasContratos) {
        persistenciaVigenciasContratos.borrar(em, vigenciasContratos);
    }

    @Override
    public void crearVC(VigenciasContratos vigenciasContratos) {
        persistenciaVigenciasContratos.crear(em, vigenciasContratos);
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
    public List<Contratos> contratos() {
        try {
            contratos = persistenciaContratos.buscarContratos(em);
            return contratos;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<TiposContratos> tiposContratos() {
        try {
            tiposContratos = persistenciaTiposContratos.tiposContratos(em);
            return tiposContratos;
        } catch (Exception e) {
            return null;
        }
    }

    @Remove
    @Override
    public void salir() {
        vigenciaContrato = null;
    }

}
