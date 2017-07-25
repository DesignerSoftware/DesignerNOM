package Administrar;

import Entidades.Empleados;
import Entidades.VWVacaPendientesEmpleados;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVWVacaPendientesEmpleadosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaVWVacaPendientesEmpleadosInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateless
public class AdministrarVWVacaPendientesEmpleados implements AdministrarVWVacaPendientesEmpleadosInterface {

   private static Logger log = Logger.getLogger(AdministrarVWVacaPendientesEmpleados.class);

    @EJB
    PersistenciaVWVacaPendientesEmpleadosInterface persistenciaVWVacaPendientesEmpleados;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
    @EJB
    PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    Empleados empleado;
    List<VWVacaPendientesEmpleados> vacaciones;
    BigDecimal unidades;
    private EntityManager em;
    //

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crearVacaPendiente(VWVacaPendientesEmpleados vaca) {
        try {
            persistenciaVWVacaPendientesEmpleados.crear(em, vaca);
        } catch (Exception e) {
            log.warn("Error en crearVacaPeniente Admi : " + e.toString());
        }
    }

    @Override
    public void editarVacaPendiente(VWVacaPendientesEmpleados vaca) {
        try {
            persistenciaVWVacaPendientesEmpleados.editar(em, vaca);
        } catch (Exception e) {
            log.warn("Error en editarVacaPendiente Admi : " + e.toString());
        }
    }

    @Override
    public void borrarVacaPendiente(VWVacaPendientesEmpleados vaca) {
        try {
            persistenciaVWVacaPendientesEmpleados.borrar(em, vaca);
        } catch (Exception e) {
            log.warn("Error en borrarVacaPendiente Admi : " + e.toString());
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> vacaPendientesPendientes(Empleados empl,Date fechaingreso) {
        try {
            vacaciones = persistenciaVWVacaPendientesEmpleados.vacaEmpleadoPendientes(em, empl.getSecuencia(),fechaingreso);
            return vacaciones;
        } catch (Exception e) {
            log.warn("Error en vacaPendientesMayorCero Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> vacaPendientesDisfrutadas(Empleados empl,Date fechaingreso) {
        try {
            vacaciones = persistenciaVWVacaPendientesEmpleados.vacaEmpleadoDisfrutadas(em, empl.getSecuencia(),fechaingreso);
            return vacaciones;
        } catch (Exception e) {
            log.warn("Error en vacaPendientesIgualCero Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Empleados obtenerEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleado.buscarEmpleado(em, secuencia);
            return empleado;
        } catch (Exception e) {
            log.warn("Error en obtener empleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public BigDecimal diasProvisionadosEmpleado(Empleados empl,Date fechaContratacion) {
        try {
            unidades = persistenciaSolucionesNodos.diasProvisionados(em, empl.getSecuencia(),fechaContratacion);
            return unidades;
        } catch (Exception e) {
            log.warn("Error en diasProvisionadosEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Date obtenerFechaFinalContratacionEmpleado(BigInteger secEmpleado) {
        try {
            Date ultimaFecha = persistenciaVigenciasTiposContratos.fechaFinalContratacionVacaciones(em, secEmpleado);
            return ultimaFecha;
        } catch (Exception e) {
            log.warn("Error obtenerFechaFinalContratacionEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> vacaPendientesPendientesAnterioresContratos(Empleados empl) {
       try {
            vacaciones = persistenciaVWVacaPendientesEmpleados.vacaEmpleadoPendientesAnterioresContratos(em, empl.getSecuencia());
            return vacaciones;
        } catch (Exception e) {
            log.warn("Error en vacaPendientesIgualCero Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> vacaPendientesDisfrutadasAnterioresContratos(Empleados empl) {
       try {
            vacaciones = persistenciaVWVacaPendientesEmpleados.vacaEmpleadoDisfrutadasAnterioresContratos(em, empl.getSecuencia());
            return vacaciones;
        } catch (Exception e) {
            log.warn("Error en vacaPendientesIgualCero Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Date obtenerFechaMaxContrato(BigInteger secEmpleado) {
       try {
            Date ultimaFecha = persistenciaVigenciasTiposContratos.fechaMaxContrato(em, secEmpleado);
            return ultimaFecha;
        } catch (Exception e) {
            log.warn("Error obtenerFechaFinalContratacionEmpleado Admi : " + e.toString());
            return null;
        }
    }
}
