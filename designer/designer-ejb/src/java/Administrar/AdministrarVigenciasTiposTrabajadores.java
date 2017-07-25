package Administrar;

import Entidades.ClasesPensiones;
import Entidades.Empleados;
import Entidades.MotivosRetiros;
import Entidades.Pensionados;
import Entidades.Personas;
import Entidades.Retirados;
import Entidades.TiposPensionados;
import Entidades.TiposTrabajadores;
import Entidades.VigenciasTiposTrabajadores;
import InterfaceAdministrar.AdministrarVigenciasTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaClasesPensionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import InterfacePersistencia.PersistenciaPensionadosInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaRetiradosInterface;
import InterfacePersistencia.PersistenciaTiposPensionadosInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposTrabajadoresInterface;
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
public class AdministrarVigenciasTiposTrabajadores implements AdministrarVigenciasTiposTrabajadoresInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasTiposTrabajadores.class);

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
    @EJB
    PersistenciaVigenciasTiposTrabajadoresInterface persistenciaVigenciasTiposTrabajadores;
    @EJB
    PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
    @EJB
    PersistenciaRetiradosInterface persistenciaRetirados;
    @EJB
    PersistenciaPensionadosInterface persistenciaPensionados;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaTiposPensionadosInterface persistenciaTiposPensionados;
    @EJB
    PersistenciaClasesPensionesInterface persistenciaClasesPensiones;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
    List<VigenciasTiposTrabajadores> vigenciasTiposTrabajadores;
    VigenciasTiposTrabajadores vigenciaTipoTrabajador;
    Empleados empleado;
    List<TiposTrabajadores> tiposTrabajadores;
    TiposTrabajadores tipoTrabajadorCodigo;

    List<MotivosRetiros> motivosRetiros;
    MotivosRetiros motivoRetiroCodigo;
    List<Retirados> retiradosEmpleado;
    Retirados retiradoVigencia;

    List<Pensionados> listaPensionados;
    List<Pensionados> listaPensionesEmpleado;
    Pensionados pensionVigencia;

    List<Personas> listaPersonas;
    Personas persona;

    List<TiposPensionados> tiposPensionados;
    TiposPensionados tipoPension;

    List<ClasesPensiones> clasesPensiones;
    ClasesPensiones clasePension;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<VigenciasTiposTrabajadores> vigenciasTiposTrabajadoresEmpleado(BigInteger secEmpleado) {
        try {
            vigenciasTiposTrabajadores = persistenciaVigenciasTiposTrabajadores.buscarVigenciasTiposTrabajadoresEmpleado(em, secEmpleado);
        } catch (Exception e) {
            log.warn("Error en Administrar Vigencias Tipos Trabajadores (vigenciasTiposTrabajadoresEmpleado) : " + e.toString());
            vigenciasTiposTrabajadores = null;
        }
        return vigenciasTiposTrabajadores;
    }

    @Override
    public void modificarVTT(List<VigenciasTiposTrabajadores> listVTTModificadas) {
        for (int i = 0; i < listVTTModificadas.size(); i++) {
            log.warn("Modificando...");
            vigenciaTipoTrabajador = listVTTModificadas.get(i);
            persistenciaVigenciasTiposTrabajadores.editar(em, vigenciaTipoTrabajador);
        }
    }

    @Override
    public void borrarVTT(VigenciasTiposTrabajadores vigenciasTiposTrabajadores) {
        persistenciaVigenciasTiposTrabajadores.borrar(em, vigenciasTiposTrabajadores);
    }

    @Override
    public void crearVTT(VigenciasTiposTrabajadores vigenciasTiposTrabajadores) {
        try {
            persistenciaVigenciasTiposTrabajadores.crear(em, vigenciasTiposTrabajadores);
        } catch (Exception e) {
            log.warn("Error crearVTT AdministrarVIgenciasTipoTrabajador : " + e.toString());
        }
    }

    @Override
    public Empleados buscarEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleados.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            empleado = null;
            log.warn("Error buscarEmpleado Admi : " + e.toString());
            return empleado;
        }
    }

    @Override
    public List<TiposTrabajadores> tiposTrabajadores() {
        try {
            tiposTrabajadores = persistenciaTiposTrabajadores.buscarTiposTrabajadores(em);
            return tiposTrabajadores;
        } catch (Exception e) {
            log.warn("Error tiposTrabajadores Admi : " + e.toString());
            return null;
        }
    }

    public TiposTrabajadores tipoTrabajadorCodigo(short codTipoTrabajador) {
        try {
            tipoTrabajadorCodigo = persistenciaTiposTrabajadores.buscarTipoTrabajadorCodigoTiposhort(em, codTipoTrabajador);
            return tipoTrabajadorCodigo;
        } catch (Exception e) {
            log.warn("Error tipoTrabajadorCodigo Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearRetirado(Retirados retirado) {
        try {
            persistenciaRetirados.crear(em, retirado);
        } catch (Exception e) {
            log.warn("Error crearRetirado Admi : " + e.toString());
        }
    }

    @Override
    public void editarRetirado(Retirados retirado) {
        try {
            persistenciaRetirados.editar(em, retirado);
        } catch (Exception e) {
            log.warn("Error editarRetirado !!");
        }
    }

    @Override
    public void borrarRetirado(Retirados retirado) {
        try {
            persistenciaRetirados.borrar(em, retirado);
        } catch (Exception e) {
            log.warn("Error borrarRetirado Admi : " + e.toString());
        }
    }

    @Override
    public List<Retirados> retiradosEmpleado(BigInteger secEmpleado) {
        try {
            retiradosEmpleado = persistenciaRetirados.buscarRetirosEmpleado(em, secEmpleado);
            return retiradosEmpleado;
        } catch (Exception e) {
            log.warn("Error retiradosEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Retirados retiroPorSecuenciaVigencia(BigInteger secVigencia) {
        try {
            retiradoVigencia = persistenciaRetirados.buscarRetiroVigenciaSecuencia(em, secVigencia);
            return retiradoVigencia;
        } catch (Exception e) {
            log.warn("Error retiroPorSecuenciaVigencia Admi : " + e.toString());
            return new Retirados();
        }
    }

    @Override
    public List<MotivosRetiros> motivosRetiros() {
        try {
            motivosRetiros = persistenciaMotivosRetiros.consultarMotivosRetiros(em);
            return motivosRetiros;
        } catch (Exception e) {
            log.warn("Error motivosRetiros Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public MotivosRetiros motivoRetiroCodigo(BigInteger codMotivoRetiro) {
        try {
            motivoRetiroCodigo = persistenciaMotivosRetiros.consultarMotivoRetiro(em, codMotivoRetiro);
            return motivoRetiroCodigo;
        } catch (Exception e) {
            log.warn("Error motivoRetiroCodigo Admi : " + e.toString());
            return null;
        }
    }

/////
    @Override
    public List<TiposPensionados> tiposPensionados() {
        try {
            tiposPensionados = persistenciaTiposPensionados.consultarTiposPensionados(em);
            return tiposPensionados;
        } catch (Exception e) {
            log.warn("Error tiposPensionados Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<ClasesPensiones> clasesPensiones() {
        try {
            clasesPensiones = persistenciaClasesPensiones.consultarClasesPensiones(em);
            return clasesPensiones;
        } catch (Exception e) {
            log.warn("Error clasesPensiones Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public ClasesPensiones clasePensionCodigo(BigInteger codClasePension) {
        try {
            clasePension = persistenciaClasesPensiones.consultarClasePension(em, codClasePension);
            return clasePension;
        } catch (Exception e) {
            log.warn("Error AdministrarVigenciaTipoTrabajador clasePensionCodigo : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Personas> listaPersonas() {
        try {
            listaPersonas = persistenciaPersonas.consultarPersonas(em);
            return listaPersonas;
        } catch (Exception e) {
            log.warn("Error listaPersonas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Personas personaSecuencia(BigInteger secPersona) {
        try {
            persona = persistenciaPersonas.buscarPersonaSecuencia(em, secPersona);
            return persona;
        } catch (Exception e) {
            log.warn("Error personaSecuencia Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearPensionado(Pensionados pension) {
        try {
            persistenciaPensionados.crear(em, pension);
        } catch (Exception e) {
            log.warn("Error crearPensionado AdministrarVigenciaTiposTrabajadores : " + e.toString());
        }
    }

    @Override
    public void editarPensionado(Pensionados pension) {
        try {
            persistenciaPensionados.editar(em, pension);
        } catch (Exception e) {
            log.warn("Error editarPensionado Admi : " + e.toString());
        }
    }

    @Override
    public void borrarPensionado(Pensionados pension) {
        try {
            persistenciaPensionados.borrar(em, pension);
        } catch (Exception e) {
            log.warn("Error borrarPensionado Admi : " + e.toString());
        }
    }

    @Override
    public List<Pensionados> pensionadoEmpleado(BigInteger secEmpleado) {
        try {
            listaPensionesEmpleado = persistenciaPensionados.buscarPensionadosEmpleado(em, secEmpleado);
            return listaPensionesEmpleado;
        } catch (Exception e) {
            log.warn("Error pensionadoEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Pensionados> listaPensionados() {
        try {
            listaPensionados = persistenciaPensionados.buscarPensionados(em);
            return listaPensionados;
        } catch (Exception e) {
            log.warn("Error listaPensionados Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Pensionados pensionPorSecuenciaVigencia(BigInteger secVigencia) {
        try {
            pensionVigencia = persistenciaPensionados.buscarPensionVigenciaSecuencia(em, secVigencia);
            return pensionVigencia;
        } catch (Exception e) {
            log.warn("Error pensionPorSecuenciaVigencia Admi : " + e.toString());
            return new Pensionados();
        }
    }

    @Remove
    @Override
    public void salir() {
        vigenciaTipoTrabajador = null;
    }
}
