package Administrar;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.MotivosContratos;
import Entidades.TiposContratos;
import Entidades.VigenciasTiposContratos;
import InterfaceAdministrar.AdministrarVigenciasTiposContratosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosContratosInterface;
import InterfacePersistencia.PersistenciaTiposContratosInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarVigenciasTiposContratos implements AdministrarVigenciasTiposContratosInterface {

    @EJB
    PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    @EJB
    PersistenciaMotivosContratosInterface persistenciaMotivosContratos;
    @EJB
    PersistenciaTiposContratosInterface persistenciaTiposContratos;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    
    private List<VigenciasTiposContratos> vigenciasTiposContratos;
    private VigenciasTiposContratos vtc;
    private Empleados empleado;
    private List<Ciudades> ciudades;
    private List<MotivosContratos> motivosContratos;
    private List<TiposContratos> tiposContratos;
    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public List<VigenciasTiposContratos> vigenciasTiposContratosEmpleado(BigInteger secEmpleado) {
        try {
            vigenciasTiposContratos = persistenciaVigenciasTiposContratos.buscarVigenciaTipoContratoEmpleado(em, secEmpleado);
        } catch (Exception e) {
            System.out.println("Error :/");
            vigenciasTiposContratos = null;
        }
        return vigenciasTiposContratos;
    }

    @Override
    public void modificarVTC(List<VigenciasTiposContratos> listVTCModificadas) {
        for (int i = 0; i < listVTCModificadas.size(); i++) {
            System.out.println("Modificando...");
            if (listVTCModificadas.get(i).getCiudad().getSecuencia() == null) {
                listVTCModificadas.get(i).setCiudad(null);
                vtc = listVTCModificadas.get(i);
            } else {
                vtc = listVTCModificadas.get(i);
            }
            persistenciaVigenciasTiposContratos.editar(em, vtc);
        }
    }

    @Override
    public void borrarVTC(VigenciasTiposContratos vigenciasTipoContrato) {
        persistenciaVigenciasTiposContratos.borrar(em, vigenciasTipoContrato);
    }

    @Override
    public void crearVTC(VigenciasTiposContratos vigenciasTipoContrato) {
        persistenciaVigenciasTiposContratos.crear(em, vigenciasTipoContrato);
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
    public List<Ciudades> ciudades() {
        try {
            ciudades = persistenciaCiudades.consultarCiudades(em);
            return ciudades;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<MotivosContratos> motivosContratos() {
        try {
            motivosContratos = persistenciaMotivosContratos.motivosContratos(em);
            return motivosContratos;
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
}
