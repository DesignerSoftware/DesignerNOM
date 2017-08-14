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
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pestaña
 * 'Personal'.
 *
 * @author Administrator
 */
@Stateful
@Local
public class AdministrarCarpetaPersonal implements AdministrarCarpetaPersonalInterface {

   private static Logger log = Logger.getLogger(AdministrarCarpetaPersonal.class);
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

   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }
   private BigDecimal resultadoActivos;
   private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
   private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------    
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public VWActualesCargos consultarActualCargoEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaVWActualesCargos.buscarCargoEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Date consultarActualesFechas() {
      try {
         return persistenciaVWActualesFechas.actualFechaHasta(getEm());
      } catch (Exception e) {
         log.warn("Error - AdministrarCarpetaPersonal.consultarActualesFechas" + e);
         return null;
      }
   }

   @Override
   public String consultarActualARP(BigInteger secEstructura, BigInteger secCargo, Date fechaHasta) {
      try {
         return persistenciaVigenciasArps.actualARP(getEm(), secEstructura, secCargo, fechaHasta);
      } catch (Exception e) {
         log.warn("Error - AdministrarCarpetaPersonal.consultarActualesFechas" + e);
         return null;
      }
   }

   @Override
   public VWActualesTiposContratos consultarActualTipoContratoEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaActualesTiposContratos.buscarTiposContratosEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesNormasEmpleados consultarActualNormaLaboralEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesNormasEmpleados.buscarNormaLaboral(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesAfiliacionesSalud consultarActualAfiliacionSaludEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesAfiliacionesSalud.buscarAfiliacionSalud(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesAfiliacionesPension consultarActualAfiliacionPensionEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesAfiliacionesPension.buscarAfiliacionPension(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesLocalizaciones consultarActualLocalizacionEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesLocalizaciones.buscarLocalizacion(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesTiposTrabajadores consultarActualTipoTrabajadorEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesTiposTrabajadores.buscarTipoTrabajador(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesTiposTrabajadores consultarActualTipoTrabajadorCodEmpleado(BigInteger codEmpleado) {
      try {
         return persistenciaVWActualesTiposTrabajadores.buscarTipoTrabajadorCodigoEmpl(getEm(), codEmpleado);
      } catch (Exception e) {
         log.warn("Administrar.AdministrarCarpetaPersonal.consultarActualTipoTrabajadorCodEmpleado() ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesContratos consultarActualContratoEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesContratos.buscarContrato(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesJornadas consultarActualJornadaEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesJornadas.buscarJornada(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigDecimal consultarActualSueldoEmpleado(BigInteger secEmpleado) {
      BigDecimal valor = null;
      try {
         VWActualesTiposTrabajadores vwActualesTiposTrabajadores = persistenciaVWActualesTiposTrabajadores.buscarTipoTrabajador(getEm(), secEmpleado);
         String tipo = vwActualesTiposTrabajadores.getTipoTrabajador().getTipo();

         if (tipo.equalsIgnoreCase("ACTIVO")) {
            valor = persistenciaVWActualesSueldos.buscarSueldoActivo(getEm(), secEmpleado);
         } else if (tipo.equalsIgnoreCase("PENSIONADO")) {
            valor = persistenciaVWActualesPensiones.buscarSueldoPensionado(getEm(), secEmpleado);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         valor = null;
      }
      return valor;
   }

   @Override
   public VWActualesReformasLaborales consultarActualReformaLaboralEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesReformasLaborales.buscarReformaLaboral(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesUbicaciones consultarActualUbicacionEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesUbicaciones.buscarUbicacion(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesFormasPagos consultarActualFormaPagoEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesFormasPagos.buscarFormaPago(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesVigenciasViajeros consultarActualTipoViajeroEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVWActualesVigenciasViajeros.buscarTipoViajero(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String consultarActualEstadoVacaciones(BigInteger secEmpleado) {
      try {
         return persistenciaNovedadesSistema.buscarEstadoVacaciones(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigDecimal consultarActualMVR(BigInteger secEmpleado) {
      try {
         return PersistenciaVWActualesMvrs.buscarActualMVR(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String actualIBC(BigInteger secEmpleado, String RETENCIONYSEGSOCXPERSONA) {
      try {
         String ibcEmpleado;
         if (RETENCIONYSEGSOCXPERSONA == null || RETENCIONYSEGSOCXPERSONA.equals("N")) {
            VWActualesIBCS actualIbc = persistenciaVWActualesIBCS.buscarIbcEmpleado(getEm(), secEmpleado);
            if (actualIbc != null) {
               ibcEmpleado = formato.format(actualIbc.getFechaFinal()) + "  " + nf.format(actualIbc.getValor());
            } else {
               return null;
            }
         } else {
            IbcsPersona actualIbc = persistenciaIbcsPersona.buscarIbcPersona(getEm(), secEmpleado);
            if (actualIbc != null) {
               ibcEmpleado = formato.format(actualIbc.getFechafinal()) + "  " + nf.format(actualIbc.getValor());
            } else {
               return null;
            }
         }
         return ibcEmpleado;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String consultarActualSet(BigInteger secEmpleado) {
      try {
         String actualSetEmpleado;
         VWActualesSets actualSet = persistenciaVWActualesSets.buscarSetEmpleado(getEm(), secEmpleado);
         if (actualSet != null) {
            actualSetEmpleado = actualSet.getPorcentaje().toString() + "%   " + nf.format(actualSet.getPromedio());
         } else {
            return null;
         }
         return actualSetEmpleado;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public String consultarActualComprobante(BigInteger secEmpleado) {
      try {
         String actualComprobante;
         CortesProcesos corteProceso = persistenciaCortesProcesos.buscarComprobante(getEm(), secEmpleado);
         if (corteProceso != null && corteProceso.getComprobante() != null) {
            actualComprobante = nf.format(corteProceso.getComprobante().getValor()) + " - " + formato.format(corteProceso.getCorte());
         } else {
            return null;
         }
         return actualComprobante;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<VwTiposEmpleados> consultarEmpleadosTipoTrabajador(String tipo) {
      try {
         return persistenciaVwTiposEmpleados.buscarTiposEmpleadosPorTipo(getEm(), tipo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesTiposTrabajadores consultarEmpleadosTipoTrabajadorPosicion(String tipo, int posicion) {
      try {
         return persistenciaVWActualesTiposTrabajadores.filtrarTipoTrabajadorPosicion(getEm(), tipo, posicion);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public int obtenerTotalRegistrosTipoTrabajador(String tipo) {
      try {
         return persistenciaVWActualesTiposTrabajadores.obtenerTotalRegistrosTipoTrabajador(getEm(), tipo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return 0;
      }
   }

   @Override
   public DetallesEmpresas consultarDetalleEmpresaUsuario() {
      try {
         Short codigoEmpresa = persistenciaEmpresas.codigoEmpresa(getEm());
         return persistenciaDetallesEmpresas.buscarDetalleEmpresa(getEm(), codigoEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Empresas obtenerEmpresa(BigInteger secEmpresa) {
      log.warn(this.getClass().getName() + ".obtenerEmpresa()");
      try {
         return persistenciaEmpresas.buscarEmpresasSecuencia(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn(this.getClass().getName() + " Error en obtenerEmpresa.");
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public List<Empresas> consultarEmpresas() {
      List<Empresas> listaEmpresas = new ArrayList<Empresas>();
      try {
         return persistenciaEmpresas.buscarEmpresas(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getName() + " Error en consultarEmpresas");
         e.printStackTrace();
         return listaEmpresas;
      }
   }

   @Override
   public Usuarios consultarUsuario(String alias) {
      try {
         return persistenciaUsuarios.buscarUsuario(getEm(), alias);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public ParametrosEstructuras consultarParametrosUsuario() {
      try {
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), consultarAliasActualUsuario());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<VigenciasCargos> consultarVigenciasCargosEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasCargos.buscarVigenciasCargosEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<VwTiposEmpleados> consultarRapidaEmpleados() {
      try {
         return persistenciaVwTiposEmpleados.buscarTiposEmpleados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Personas consultarFotoPersona(BigInteger identificacion) {
      try {
         return persistenciaPersonas.buscarFotoPersona(getEm(), identificacion);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void actualizarFotoPersona(BigInteger identificacion) {
      try {
         persistenciaPersonas.actualizarFotoPersona(getEm(), identificacion);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Empleados consultarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void editarVigenciasCargos(List<VigenciasCargos> vigenciasCargos) {
      try {
         for (int i = 0; i < vigenciasCargos.size(); i++) {
            persistenciaVigenciasCargos.editar(getEm(), vigenciasCargos.get(i));
         }
      } catch (Exception e) {
         log.warn("Excepcion Administrar - No Se Guardo Nada ¬¬");
      }
   }

   @Override
   public String consultarAliasActualUsuario() {
      try {
         return persistenciaActualUsuario.actualAliasBD(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void borrarLiquidacionAutomatico() {
      try {
         persistenciaCandados.borrarLiquidacionAutomatico(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarLiquidacionNoAutomatico() {
      try {
         persistenciaCandados.borrarLiquidacionNoAutomatico(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   //METODOS QUE TENGAN QUE VER CON EL BOTON DE LAS FOTOS DE NOMINA F
   @Override
   public BigDecimal borrarActivo(BigInteger secuencia) {
      try {
         resultadoActivos = persistenciaSolucionesNodos.activos(getEm(), secuencia);
         return resultadoActivos;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public boolean borrarEmpleadoActivo(BigInteger secuenciaEmpleado, BigInteger secuenciaPersona) {
      try {
         return persistenciaEmpleado.eliminarEmpleadoNominaF(getEm(), secuenciaEmpleado, secuenciaPersona);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return false;
      }
   }

}
