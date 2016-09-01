/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.*;
import InterfaceAdministrar.AdministrarCarpetaPersonalInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pestaña
 * 'Personal'.
 *
 * @author Administrator
 */
@Stateful
public class AdministrarCarpetaPersonal implements AdministrarCarpetaPersonalInterface {
    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesCargos'.
     */
    @EJB
    PersistenciaVWActualesCargosInterface persistenciaVWActualesCargos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesFechas'.
     */
    @EJB
    PersistenciaVWActualesFechasInterface persistenciaVWActualesFechas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'PersistenciaVigenciasArps'.
     */
    @EJB
    PersistenciaVigenciasArpsInterface persistenciaVigenciasArps;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaActualesTiposContratos'.
     */
    @EJB
    PersistenciaVWActualesTiposContratosInterface persistenciaActualesTiposContratos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesNormasEmpleados'.
     */
    @EJB
    PersistenciaVWActualesNormasEmpleadosInterface persistenciaVWActualesNormasEmpleados;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesAfiliacionesSalud'.
     */
    @EJB
    PersistenciaVWActualesAfiliacionesSaludInterface persistenciaVWActualesAfiliacionesSalud;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesAfiliacionesPension'.
     */
    @EJB
    PersistenciaVWActualesAfiliacionesPensionInterface persistenciaVWActualesAfiliacionesPension;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesLocalizaciones'.
     */
    @EJB
    PersistenciaVWActualesLocalizacionesInterface persistenciaVWActualesLocalizaciones;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVigenciasTiposTrabajadores'.
     */
    @EJB
    PersistenciaVigenciasTiposTrabajadoresInterface persistenciaVigenciasTiposTrabajadores;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesTiposTrabajadores'.
     */
    @EJB
    PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesContratos'.
     */
    @EJB
    PersistenciaVWActualesContratosInterface persistenciaVWActualesContratos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesJornadas'.
     */
    @EJB
    PersistenciaVWActualesJornadasInterface persistenciaVWActualesJornadas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesSueldos'.
     */
    @EJB
    PersistenciaVWActualesSueldosInterface persistenciaVWActualesSueldos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesPensiones'.
     */
    @EJB
    PersistenciaVWActualesPensionesInterface persistenciaVWActualesPensiones;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesReformasLaborales'.
     */
    @EJB
    PersistenciaVWActualesReformasLaboralesInterface persistenciaVWActualesReformasLaborales;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesUbicaciones'.
     */
    @EJB
    PersistenciaVWActualesUbicacionesInterface persistenciaVWActualesUbicaciones;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesFormasPagos'.
     */
    @EJB
    PersistenciaVWActualesFormasPagosInterface persistenciaVWActualesFormasPagos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesVigenciasViajeros'.
     */
    @EJB
    PersistenciaVWActualesVigenciasViajerosInterface persistenciaVWActualesVigenciasViajeros;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaNovedadesSistema'.
     */
    @EJB
    PersistenciaNovedadesSistemaInterface persistenciaNovedadesSistema;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'PersistenciaVWActualesMvrs'.
     */
    @EJB
    PersistenciaVWActualesMvrsInterface PersistenciaVWActualesMvrs;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaDetallesEmpresas'.
     */
    @EJB
    PersistenciaDetallesEmpresasInterface persistenciaDetallesEmpresas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaUsuarios'.
     */
    @EJB
    PersistenciaUsuariosInterface persistenciaUsuarios;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaParametrosEstructuras'.
     */
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVigenciasCargos'.
     */
    @EJB
    PersistenciaVigenciasCargosInterface persistenciaVigenciasCargos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaPersonas'.
     */
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEmpleado'.
     */
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaActualUsuario'.
     */
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEmpresas'.
     */
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesIBCS'.
     */
    @EJB
    PersistenciaVWActualesIBCSInterface persistenciaVWActualesIBCS;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaIbcsPersona'.
     */
    @EJB
    PersistenciaIbcsPersonaInterface persistenciaIbcsPersona;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaVWActualesSets'.
     */
    @EJB
    PersistenciaVWActualesSetsInterface persistenciaVWActualesSets;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaComprobantes'.
     */
    @EJB
    PersistenciaCortesProcesosInterface persistenciaCortesProcesos;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    PersistenciaCandadosInterface persistenciaCandados;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
    @EJB
    PersistenciaVwTiposEmpleadosInterface persistenciaVwTiposEmpleados;

    private EntityManager em;
    private Long resultadoActivos;
    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public VWActualesCargos consultarActualCargoEmpleado(BigInteger secuenciaEmpleado) {
        try {
            VWActualesCargos vwActualesCargos = persistenciaVWActualesCargos.buscarCargoEmpleado(em, secuenciaEmpleado);
            return vwActualesCargos;
        } catch (Exception e) {
            System.out.println("ConsultarCargo.");
            System.out.println("Exception" + e);
            return null;
        }
    }

    @Override
    public Date consultarActualesFechas() {
        try {
            Date actualFechaHasta = persistenciaVWActualesFechas.actualFechaHasta(em);
            return actualFechaHasta;
        } catch (Exception e) {
            System.out.println("Error - AdministrarCarpetaPersonal.consultarActualesFechas" + e);
            return null;
        }
    }

    @Override
    public String consultarActualARP(BigInteger secEstructura, BigInteger secCargo, Date fechaHasta) {
        try {
            String actualARP = persistenciaVigenciasArps.actualARP(em, secEstructura, secCargo, fechaHasta);
            return actualARP;
        } catch (Exception e) {
            System.out.println("Error - AdministrarCarpetaPersonal.consultarActualesFechas" + e);
            return null;
        }
    }

    @Override
    public VWActualesTiposContratos consultarActualTipoContratoEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesTiposContratos vwActualesTiposContratos = persistenciaActualesTiposContratos.buscarTiposContratosEmpleado(em, secEmpleado);
            return vwActualesTiposContratos;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesNormasEmpleados consultarActualNormaLaboralEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesNormasEmpleados vwActualesNormasEmpleados = persistenciaVWActualesNormasEmpleados.buscarNormaLaboral(em, secEmpleado);
            return vwActualesNormasEmpleados;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesAfiliacionesSalud consultarActualAfiliacionSaludEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesAfiliacionesSalud vwActualesAfiliacionesSalud = persistenciaVWActualesAfiliacionesSalud.buscarAfiliacionSalud(em, secEmpleado);
            return vwActualesAfiliacionesSalud;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesAfiliacionesPension consultarActualAfiliacionPensionEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesAfiliacionesPension vwActualesAfiliacionesPension = persistenciaVWActualesAfiliacionesPension.buscarAfiliacionPension(em, secEmpleado);
            return vwActualesAfiliacionesPension;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesLocalizaciones consultarActualLocalizacionEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesLocalizaciones vwActualesLocalizaciones = persistenciaVWActualesLocalizaciones.buscarLocalizacion(em, secEmpleado);
            return vwActualesLocalizaciones;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesTiposTrabajadores consultarActualTipoTrabajadorEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesTiposTrabajadores vwActualesTiposTrabajadores = persistenciaVWActualesTiposTrabajadores.buscarTipoTrabajador(em, secEmpleado);
            return vwActualesTiposTrabajadores;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesContratos consultarActualContratoEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesContratos vwActualesContratos = persistenciaVWActualesContratos.buscarContrato(em, secEmpleado);
            return vwActualesContratos;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesJornadas consultarActualJornadaEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesJornadas vwActualesJornadas = persistenciaVWActualesJornadas.buscarJornada(em, secEmpleado);
            return vwActualesJornadas;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BigDecimal consultarActualSueldoEmpleado(BigInteger secEmpleado) {
        BigDecimal valor = null;
        try {
            VWActualesTiposTrabajadores vwActualesTiposTrabajadores = persistenciaVWActualesTiposTrabajadores.buscarTipoTrabajador(em, secEmpleado);
            String tipo = vwActualesTiposTrabajadores.getTipoTrabajador().getTipo();

            if (tipo.equalsIgnoreCase("ACTIVO")) {
                valor = persistenciaVWActualesSueldos.buscarSueldoActivo(em, secEmpleado);
            } else if (tipo.equalsIgnoreCase("PENSIONADO")) {
                valor = persistenciaVWActualesPensiones.buscarSueldoPensionado(em, secEmpleado);
            }
        } catch (Exception e) {
            valor = null;
        }
        return valor;
    }

    @Override
    public VWActualesReformasLaborales consultarActualReformaLaboralEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesReformasLaborales vwActualesReformasLaborales = persistenciaVWActualesReformasLaborales.buscarReformaLaboral(em, secEmpleado);
            return vwActualesReformasLaborales;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesUbicaciones consultarActualUbicacionEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesUbicaciones vWActualesUbicaciones = persistenciaVWActualesUbicaciones.buscarUbicacion(em, secEmpleado);
            return vWActualesUbicaciones;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesFormasPagos consultarActualFormaPagoEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesFormasPagos vwActualesFormasPagos = persistenciaVWActualesFormasPagos.buscarFormaPago(em, secEmpleado);
            return vwActualesFormasPagos;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesVigenciasViajeros consultarActualTipoViajeroEmpleado(BigInteger secEmpleado) {
        try {
            VWActualesVigenciasViajeros vwActualesVigenciasViajeros = persistenciaVWActualesVigenciasViajeros.buscarTipoViajero(em, secEmpleado);
            return vwActualesVigenciasViajeros;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String consultarActualEstadoVacaciones(BigInteger secEmpleado) {
        try {
            String estadoVacaciones = persistenciaNovedadesSistema.buscarEstadoVacaciones(em, secEmpleado);
            return estadoVacaciones;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BigDecimal consultarActualMVR(BigInteger secEmpleado) {
        try {
            BigDecimal actualMVR = PersistenciaVWActualesMvrs.buscarActualMVR(em, secEmpleado);
            return actualMVR;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String actualIBC(BigInteger secEmpleado, String RETENCIONYSEGSOCXPERSONA) {
        try {
            String ibcEmpleado;
            if (RETENCIONYSEGSOCXPERSONA == null || RETENCIONYSEGSOCXPERSONA.equals("N")) {
                VWActualesIBCS actualIbc = persistenciaVWActualesIBCS.buscarIbcEmpleado(em, secEmpleado);
                if (actualIbc != null) {
                    ibcEmpleado = formato.format(actualIbc.getFechaFinal()) + "  " + nf.format(actualIbc.getValor());
                } else {
                    return null;
                }
            } else {
                IbcsPersona actualIbc = persistenciaIbcsPersona.buscarIbcPersona(em, secEmpleado);
                if (actualIbc != null) {
                    ibcEmpleado = formato.format(actualIbc.getFechafinal()) + "  " + nf.format(actualIbc.getValor());
                } else {
                    return null;
                }
            }
            return ibcEmpleado;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String consultarActualSet(BigInteger secEmpleado) {
        try {
            String actualSetEmpleado;
            VWActualesSets actualSet = persistenciaVWActualesSets.buscarSetEmpleado(em, secEmpleado);
            if (actualSet != null) {
                actualSetEmpleado = actualSet.getPorcentaje().toString() + "%   " + nf.format(actualSet.getPromedio());
            } else {
                return null;
            }
            return actualSetEmpleado;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String consultarActualComprobante(BigInteger secEmpleado) {
        try {
            String actualComprobante;
            CortesProcesos corteProceso = persistenciaCortesProcesos.buscarComprobante(em, secEmpleado);
            if (corteProceso != null && corteProceso.getComprobante() != null) {
                actualComprobante = nf.format(corteProceso.getComprobante().getValor()) + " - " + formato.format(corteProceso.getCorte());
            } else {
                return null;
            }
            return actualComprobante;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<VwTiposEmpleados> consultarEmpleadosTipoTrabajador(String tipo) {
        try {
            List<VwTiposEmpleados> tipoEmpleadoLista = persistenciaVwTiposEmpleados.buscarTiposEmpleadosPorTipo(em, tipo);
            return tipoEmpleadoLista;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VWActualesTiposTrabajadores consultarEmpleadosTipoTrabajadorPosicion(String tipo, int posicion) {
        try {
            VWActualesTiposTrabajadores tipoEmpleado = persistenciaVWActualesTiposTrabajadores.filtrarTipoTrabajadorPosicion(em, tipo, posicion);
            return tipoEmpleado;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int obtenerTotalRegistrosTipoTrabajador(String tipo) {
        try {
            int totalRegistros = persistenciaVWActualesTiposTrabajadores.obtenerTotalRegistrosTipoTrabajador(em, tipo);
            return totalRegistros;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public DetallesEmpresas consultarDetalleEmpresaUsuario() {
        try {
            Short codigoEmpresa = persistenciaEmpresas.codigoEmpresa(em);
            DetallesEmpresas detallesEmpresas = persistenciaDetallesEmpresas.buscarDetalleEmpresa(em, codigoEmpresa);
            return detallesEmpresas;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public Empresas obtenerEmpresa(BigInteger secEmpresa){
        System.out.println(this.getClass().getName()+".obtenerEmpresa()");
        Empresas empresa = null;
        try{
        empresa = persistenciaEmpresas.buscarEmpresasSecuencia(em, secEmpresa);
        return empresa;
        } catch(Exception e){
            System.out.println(this.getClass().getName()+" Error en obtenerEmpresa.");
            e.printStackTrace();
            return empresa;
        }
    }

    @Override
    public List<Empresas> consultarEmpresas(){
        List<Empresas> listaEmpresas = new ArrayList<Empresas>();
        try{
        listaEmpresas = persistenciaEmpresas.buscarEmpresas(em);
        return listaEmpresas;
        } catch(Exception e){
            System.out.println(this.getClass().getName()+" Error en consultarEmpresas");
            e.printStackTrace();
            return listaEmpresas;
        }
    }

    @Override
    public Usuarios consultarUsuario(String alias) {
        try {
            Usuarios usuarios = persistenciaUsuarios.buscarUsuario(em, alias);
            return usuarios;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ParametrosEstructuras consultarParametrosUsuario() {
        try {
            ParametrosEstructuras parametrosEstructuras = persistenciaParametrosEstructuras.buscarParametro(em, consultarAliasActualUsuario());
            return parametrosEstructuras;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<VigenciasCargos> consultarVigenciasCargosEmpleado(BigInteger secEmpleado) {
        try {
            List<VigenciasCargos> vigenciasCargos = persistenciaVigenciasCargos.buscarVigenciasCargosEmpleado(em, secEmpleado);
            return vigenciasCargos;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<VwTiposEmpleados> consultarRapidaEmpleados() {
        try {
            List<VwTiposEmpleados> busquedaRapidaEmpleado = persistenciaVwTiposEmpleados.buscarTiposEmpleados(em);
            return busquedaRapidaEmpleado;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Personas consultarFotoPersona(BigInteger identificacion) {
        try {
            Personas persona = persistenciaPersonas.buscarFotoPersona(em, identificacion);
            return persona;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void actualizarFotoPersona(BigInteger identificacion) {
        try {
            persistenciaPersonas.actualizarFotoPersona(em, identificacion);
        } catch (Exception e) {
            System.out.println("No se puede actalizar el estado de la Foto.");
        }
    }

    @Override
    public Empleados consultarEmpleado(BigInteger secuencia) {
        try {
            Empleados empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            Empleados empleado = null;
            return empleado;
        }
    }

    @Override
    public void editarVigenciasCargos(List<VigenciasCargos> vigenciasCargos) {
        try {
            for (int i = 0; i < vigenciasCargos.size(); i++) {
                persistenciaVigenciasCargos.editar(em, vigenciasCargos.get(i));
            }
        } catch (Exception e) {
            System.out.println("Excepcion Administrar - No Se Guardo Nada ¬¬");
        }
    }

    @Override
    public String consultarAliasActualUsuario() {
        return persistenciaActualUsuario.actualAliasBD(em);
    }

    @Override
    public void borrarLiquidacionAutomatico() {
        persistenciaCandados.borrarLiquidacionAutomatico(em);
    }

    @Override
    public void borrarLiquidacionNoAutomatico() {
        persistenciaCandados.borrarLiquidacionNoAutomatico(em);
    }

    //METODOS QUE TENGAN QUE VER CON EL BOTON DE LAS FOTOS DE NOMINA F
    @Override
    public Long borrarActivo(BigInteger secuencia) {
        resultadoActivos = persistenciaSolucionesNodos.activos(em, secuencia);
        return resultadoActivos;
    }

    @Override
    public void borrarEmpleadoActivo(BigInteger secuenciaEmpleado, BigInteger secuenciaPersona) {
        persistenciaEmpleado.eliminarEmpleadoNominaF(em, secuenciaEmpleado, secuenciaPersona);

    }

}
