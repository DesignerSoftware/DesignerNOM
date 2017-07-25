/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import ClasesAyuda.ResultadoBorrarTodoNovedades;
import Entidades.ActualUsuario;
import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import Entidades.Generales;
import Entidades.Novedades;
import Entidades.TempNovedades;
import Entidades.VWActualesReformasLaborales;
import Entidades.VWActualesTiposContratos;
import Entidades.VWActualesTiposTrabajadores;
import InterfaceAdministrar.AdministrarCargueArchivosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaFormulasConceptosInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaFormulasNovedadesInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaNovedadesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaSolucionesFormulasInterface;
import InterfacePersistencia.PersistenciaTempNovedadesInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaVWActualesReformasLaboralesInterface;
import InterfacePersistencia.PersistenciaVWActualesTiposContratosInterface;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasConceptosRLInterface;
import InterfacePersistencia.PersistenciaVigenciasConceptosTCInterface;
import InterfacePersistencia.PersistenciaVigenciasConceptosTTInterface;
import InterfacePersistencia.PersistenciaVigenciasGruposConceptosInterface;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'CargueArchivos'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarCargueArchivos implements AdministrarCargueArchivosInterface {

   private static Logger log = Logger.getLogger(AdministrarCargueArchivos.class);
   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    

   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTempNovedades'.
    */
   @EJB
   PersistenciaTempNovedadesInterface persistenciaTempNovedades;
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
    * 'persistenciaConceptos'.
    */
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
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
    * 'persistenciaPeriodicidades'.
    */
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTerceros'.
    */
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
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
    * 'persistenciaVWActualesReformasLaborales'.
    */
   @EJB
   PersistenciaVWActualesReformasLaboralesInterface persistenciaVWActualesReformasLaborales;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaVWActualesTiposContratos'.
    */
   @EJB
   PersistenciaVWActualesTiposContratosInterface persistenciaVWActualesTiposContratos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFormulasConceptos'.
    */
   @EJB
   PersistenciaFormulasConceptosInterface persistenciaFormulasConceptos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFormulasNovedades'.
    */
   @EJB
   PersistenciaFormulasNovedadesInterface persistenciaFormulasNovedades;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaVigenciasConceptosRL'.
    */
   @EJB
   PersistenciaVigenciasConceptosRLInterface persistenciaVigenciasConceptosRL;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaVigenciasConceptosTC'.
    */
   @EJB
   PersistenciaVigenciasConceptosTCInterface persistenciaVigenciasConceptosTC;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaVigenciasConceptosTT'.
    */
   @EJB
   PersistenciaVigenciasConceptosTTInterface persistenciaVigenciasConceptosTT;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFormulas'.
    */
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaVigenciasGruposConceptos'.
    */
   @EJB
   PersistenciaVigenciasGruposConceptosInterface persistenciaVigenciasGruposConceptos;
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
    * 'persistenciaSolucionesFormulas'.
    */
   @EJB
   PersistenciaSolucionesFormulasInterface persistenciaSolucionesFormulas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaNovedades'.
    */
   @EJB
   PersistenciaNovedadesInterface persistenciaNovedades;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   @EJB
   PersistenciaGeneralesInterface persistenciaGenerales;

   private EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }
   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------

   @Override
   public void crearTempNovedades(List<TempNovedades> listaTempNovedades) {
      for (int i = 0; i < listaTempNovedades.size(); i++) {
         persistenciaTempNovedades.crear(em, listaTempNovedades.get(i));
      }
   }

   @Override
   public void modificarTempNovedades(TempNovedades tempNovedades) {
      persistenciaTempNovedades.editar(em, tempNovedades);
   }

   @Override
   public void borrarTempNovedad(TempNovedades tempNovedades) {
      persistenciaTempNovedades.borrar(em, tempNovedades);
   }

   @Override
   public void borrarRegistrosTempNovedades(String usuarioBD) {
      persistenciaTempNovedades.borrarRegistrosTempNovedades(em, usuarioBD);
   }

   @Override
   public ActualUsuario actualUsuario() {
      return persistenciaActualUsuario.actualUsuarioBD(em);
   }

   @Override
   public BigInteger consultarParametrosEmpresa(String usuarioBD) {
      return persistenciaParametrosEstructuras.buscarEmpresaParametros(em, usuarioBD);
   }

   @Override
   public List<TempNovedades> consultarTempNovedades(String usuarioBD) {
      return persistenciaTempNovedades.obtenerTempNovedades(em, usuarioBD);
   }

   @Override
   public boolean verificarEmpleadoEmpresa(BigInteger codEmpleado, BigInteger secEmpresa) {
      return persistenciaEmpleado.verificarCodigoEmpleado_Empresa(em, codEmpleado, secEmpresa);
   }

   @Override
   public boolean verificarConcepto(BigInteger codConcepto) {
      return persistenciaConceptos.verificarCodigoConcepto(em, codConcepto);
   }

   @Override
   public boolean verificarPeriodicidad(BigInteger codPeriodicidad) {
      return persistenciaPeriodicidades.verificarCodigoPeriodicidad(em, codPeriodicidad);
   }

   @Override
   public boolean verificarTercero(BigInteger nitTercero) {
      return persistenciaTerceros.verificarTerceroPorNit(em, nitTercero);
   }

   @Override
   public boolean verificarTipoEmpleadoActivo(BigInteger codEmpleado, BigInteger secEmpresa) {
      Empleados empleado = consultarEmpleadoEmpresa(codEmpleado, secEmpresa);
      return persistenciaVWActualesTiposTrabajadores.verificarTipoTrabajador(em, empleado);
   }

   @Override
   public Empleados consultarEmpleadoEmpresa(BigInteger codEmpleado, BigInteger secEmpresa) {
      Empleados empleado = persistenciaEmpleado.buscarEmpleadoCodigo_Empresa(em, codEmpleado, secEmpresa);
      return empleado;
   }

   @Override
   public VWActualesTiposTrabajadores consultarActualTipoTrabajadorEmpleado(BigInteger secuenciaEmpleado) {
      return persistenciaVWActualesTiposTrabajadores.buscarTipoTrabajador(em, secuenciaEmpleado);
   }

   @Override
   public VWActualesReformasLaborales consultarActualReformaLaboralEmpleado(BigInteger secuenciaEmpleado) {
      return persistenciaVWActualesReformasLaborales.buscarReformaLaboral(em, secuenciaEmpleado);
   }

   @Override
   public VWActualesTiposContratos consultarActualTipoContratoEmpleado(BigInteger secuenciaEmpleado) {
      return persistenciaVWActualesTiposContratos.buscarTiposContratosEmpleado(em, secuenciaEmpleado);
   }

   @Override
   public Conceptos verificarConceptoEmpresa(BigInteger codigoConcepto, BigInteger codigoEmpresa) {
      return persistenciaConceptos.validarCodigoConcepto(em, codigoConcepto, codigoEmpresa);
   }

   @Override
   public String determinarTipoConcepto(BigInteger secConcepto) {
      String tipo = "MANUAL";
      boolean verificar = persistenciaFormulasConceptos.verificarExistenciaConceptoFormulasConcepto(em, secConcepto);
      if (verificar == true) {
         List<FormulasConceptos> formulasConcepto = persistenciaFormulasConceptos.formulasConceptosXSecConcepto(em, secConcepto);
         for (int i = 0; i < formulasConcepto.size(); i++) {
            verificar = persistenciaFormulasNovedades.verificarExistenciaFormulasNovedades(em, formulasConcepto.get(i).getFormula());
            if (verificar == true) {
               tipo = "SEMI-AUTOMATICO";
            } else {
               tipo = "AUTOMATICO";
               break;
            }
         }
         return tipo;
      } else {
         tipo = "MANUAL";
         return tipo;
      }
   }

   @Override
   public boolean verificarZonaT(BigInteger secConcepto, BigInteger secRL, BigInteger secTC, BigInteger secTT) {
      try {
         boolean validarZonaT;
         validarZonaT = persistenciaVigenciasConceptosRL.verificacionZonaTipoReformasLaborales(em, secConcepto, secRL);
         if (validarZonaT == true) {
            validarZonaT = persistenciaVigenciasConceptosTC.verificacionZonaTipoContrato(em, secConcepto, secTC);
            if (validarZonaT == true) {
               validarZonaT = persistenciaVigenciasConceptosTT.verificacionZonaTipoTrabajador(em, secConcepto, secTT);
            }
         }
         return validarZonaT;
      } catch (Exception e) {
         log.warn("Error verificarZonaT: " + e);
         return false;
      }
   }

   @Override
   public List<Formulas> consultarFormulasCargue() {
      return persistenciaFormulas.buscarFormulasCarge(em);
   }

   @Override
   public Formulas consultarFormulaCargueInicial() {
      return persistenciaFormulas.buscarFormulaCargeInicial(em);
   }

   @Override
   public boolean verificarFormulaCargueConcepto(BigInteger secConcepto, BigInteger secFormula) {
      return persistenciaFormulasConceptos.verificarFormulaCargue_Concepto(em, secConcepto, secFormula);
   }

   @Override
   public boolean verificarNecesidadTercero(BigInteger secConcepto) {
      return persistenciaVigenciasGruposConceptos.verificacionGrupoUnoConcepto(em, secConcepto);
   }

   @Override
   public boolean verificarTerceroEmpresa(BigInteger nit, BigInteger secEmpresa) {
      return persistenciaTerceros.verificarTerceroParaEmpresaEmpleado(em, nit, secEmpresa);
   }

   @Override
   public List<String> consultarDocumentosSoporteCargadosUsuario(String usuarioBD) {
      return persistenciaTempNovedades.obtenerDocumentosSoporteCargados(em, usuarioBD);
   }

   @Override
   public void cargarTempNovedades(Date fechaReporte, String nombreCorto, String usarFormula) {
      SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
      String fechaR = formatoFecha.format(fechaReporte);
      persistenciaTempNovedades.cargarTempNovedades(em, fechaR, nombreCorto, usarFormula);
   }

   @Override
   public int reversarNovedades(ActualUsuario usuarioBD, String documentoSoporte) {
      List<Novedades> listNovedades = persistenciaNovedades.novedadesParaReversar(em, usuarioBD.getSecuencia(), documentoSoporte);
      int validarNoLiquidadas = 0;
      for (int i = 0; i < listNovedades.size(); i++) {
         if (persistenciaSolucionesFormulas.validarNovedadesNoLiquidadas(em, listNovedades.get(i).getSecuencia()) > 0) {
            validarNoLiquidadas++;
         }
      }
      listNovedades.clear();
      if (validarNoLiquidadas == 0) {
         persistenciaTempNovedades.reversarTempNovedades(em, usuarioBD.getAlias(), documentoSoporte);
         return persistenciaNovedades.reversarNovedades(em, usuarioBD.getSecuencia(), documentoSoporte);
      } else {
         return 0;
      }
   }

   @Override
   public ResultadoBorrarTodoNovedades BorrarTodo(ActualUsuario usuarioBD, List<String> documentosSoporte) {
      ResultadoBorrarTodoNovedades resultadoProceso = new ResultadoBorrarTodoNovedades();
      int registrosBorrados = 0;
      for (int j = 0; j < documentosSoporte.size(); j++) {
         List<Novedades> listNovedades = persistenciaNovedades.novedadesParaReversar(em, usuarioBD.getSecuencia(), documentosSoporte.get(j));
         int validarNoLiquidadas = 0;
         for (int i = 0; i < listNovedades.size(); i++) {
            if (persistenciaSolucionesFormulas.validarNovedadesNoLiquidadas(em, listNovedades.get(i).getSecuencia()) > 0) {
               validarNoLiquidadas++;
            }
         }
         listNovedades.clear();
         if (validarNoLiquidadas == 0) {
            registrosBorrados = registrosBorrados + persistenciaNovedades.reversarNovedades(em, usuarioBD.getSecuencia(), documentosSoporte.get(j));
            persistenciaTempNovedades.reversarTempNovedades(em, usuarioBD.getAlias(), documentosSoporte.get(j));
         } else {
            if (resultadoProceso.getDocumentosNoBorrados() == null) {
               resultadoProceso.setDocumentosNoBorrados(new ArrayList<String>());
            }
            resultadoProceso.getDocumentosNoBorrados().add(documentosSoporte.get(j));
         }
      }
      resultadoProceso.setRegistrosBorrados(registrosBorrados);
      return resultadoProceso;
   }

   public String consultarRuta() {
      try {
         Generales general = persistenciaGenerales.obtenerRutas(em);
         return general.getUbicareportes();
      } catch (Exception e) {
         log.warn("ERROR Administrar.AdministrarCargueArchivos.consultarRuta() e : " + e);
         return "C:\\DesignerRHN\\Reportes\\ArchivosPlanos\\";
      }
   }
}
