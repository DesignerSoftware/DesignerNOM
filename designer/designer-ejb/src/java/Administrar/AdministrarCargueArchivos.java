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
import javax.persistence.EntityManagerFactory;
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

   private EntityManagerFactory emf;
   private EntityManager em;
   private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) {
            if (this.em != null) {
               if (this.em.isOpen()) {
                  this.em.close();
               }
            }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) {
      idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }
   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------

   @Override
   public void crearTempNovedades(List<TempNovedades> listaTempNovedades) {
      try {
         for (int i = 0; i < listaTempNovedades.size(); i++) {
            persistenciaTempNovedades.crear(getEm(), listaTempNovedades.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTempNovedades() ERROR: " + e);
      }
   }

   @Override
   public void modificarTempNovedades(TempNovedades tempNovedades) {
      try {
         persistenciaTempNovedades.editar(getEm(), tempNovedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTempNovedades() ERROR: " + e);
      }
   }

   @Override
   public void borrarTempNovedad(TempNovedades tempNovedades) {
      try {
         persistenciaTempNovedades.borrar(getEm(), tempNovedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTempNovedad() ERROR: " + e);
      }
   }

   @Override
   public void borrarRegistrosTempNovedades(String usuarioBD) {
      try {
         persistenciaTempNovedades.borrarRegistrosTempNovedades(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarRegistrosTempNovedades() ERROR: " + e);
      }
   }

   @Override
   public ActualUsuario actualUsuario() {
      try {
         return persistenciaActualUsuario.actualUsuarioBD(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".actualUsuario() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger consultarParametrosEmpresa(String usuarioBD) {
      try {
         return persistenciaParametrosEstructuras.buscarEmpresaParametros(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarParametrosEmpresa() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TempNovedades> consultarTempNovedades(String usuarioBD) {
      try {
         return persistenciaTempNovedades.obtenerTempNovedades(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTempNovedades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public boolean verificarEmpleadoEmpresa(BigInteger codEmpleado, BigInteger secEmpresa) {
      try {
         return persistenciaEmpleado.verificarCodigoEmpleado_Empresa(getEm(), codEmpleado, secEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".verificarEmpleadoEmpresa() ERROR: " + e);
         return false;
      }
   }

   @Override
   public boolean verificarConcepto(BigInteger codConcepto) {
      try {
         return persistenciaConceptos.verificarCodigoConcepto(getEm(), codConcepto);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".verificarConcepto() ERROR: " + e);
         return false;
      }
   }

   @Override
   public boolean verificarPeriodicidad(BigInteger codPeriodicidad) {
      try {
         return persistenciaPeriodicidades.verificarCodigoPeriodicidad(getEm(), codPeriodicidad);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".verificarPeriodicidad() ERROR: " + e);
         return false;
      }
   }

   @Override
   public boolean verificarTercero(BigInteger nitTercero) {
      try {
         return persistenciaTerceros.verificarTerceroPorNit(getEm(), nitTercero);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".verificarTercero() ERROR: " + e);
         return false;
      }
   }

   @Override
   public boolean verificarTipoEmpleadoActivo(BigInteger codEmpleado, BigInteger secEmpresa) {
      try {
         Empleados empleado = consultarEmpleadoEmpresa(codEmpleado, secEmpresa);
         return persistenciaVWActualesTiposTrabajadores.verificarTipoTrabajador(getEm(), empleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".verificarTipoEmpleadoActivo() ERROR: " + e);
         return false;
      }
   }

   @Override
   public Empleados consultarEmpleadoEmpresa(BigInteger codEmpleado, BigInteger secEmpresa) {
      try {
         return persistenciaEmpleado.buscarEmpleadoCodigo_Empresa(getEm(), codEmpleado, secEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarEmpleadoEmpresa() ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesTiposTrabajadores consultarActualTipoTrabajadorEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaVWActualesTiposTrabajadores.buscarTipoTrabajador(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarActualTipoTrabajadorEmpleado() ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesReformasLaborales consultarActualReformaLaboralEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaVWActualesReformasLaborales.buscarReformaLaboral(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarActualReformaLaboralEmpleado() ERROR: " + e);
         return null;
      }
   }

   @Override
   public VWActualesTiposContratos consultarActualTipoContratoEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaVWActualesTiposContratos.buscarTiposContratosEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarActualTipoContratoEmpleado() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Conceptos verificarConceptoEmpresa(BigInteger codigoConcepto, BigInteger codigoEmpresa) {
      try {
         return persistenciaConceptos.validarCodigoConcepto(getEm(), codigoConcepto, codigoEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".verificarConceptoEmpresa() ERROR: " + e);
         return null;
      }
   }

   @Override
   public String determinarTipoConcepto(BigInteger secConcepto) {
      try {
         String tipo = "MANUAL";
         boolean verificar = persistenciaFormulasConceptos.verificarExistenciaConceptoFormulasConcepto(getEm(), secConcepto);
         if (verificar == true) {
            List<FormulasConceptos> formulasConcepto = persistenciaFormulasConceptos.formulasConceptosXSecConcepto(getEm(), secConcepto);
            for (int i = 0; i < formulasConcepto.size(); i++) {
               verificar = persistenciaFormulasNovedades.verificarExistenciaFormulasNovedades(getEm(), formulasConcepto.get(i).getFormula());
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
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".determinarTipoConcepto() ERROR: " + e);
         return null;
      }
   }

   @Override
   public boolean verificarZonaT(BigInteger secConcepto, BigInteger secRL, BigInteger secTC, BigInteger secTT) {
      try {
         boolean validarZonaT = persistenciaVigenciasConceptosRL.verificacionZonaTipoReformasLaborales(getEm(), secConcepto, secRL);
         System.out.println("AdministrarCargueArchivos.verificarZonaT() 1");
         if (validarZonaT) {
            System.out.println("AdministrarCargueArchivos.verificarZonaT() 2");
            validarZonaT = persistenciaVigenciasConceptosTC.verificacionZonaTipoContrato(getEm(), secConcepto, secTC);
            if (validarZonaT) {
               System.out.println("AdministrarCargueArchivos.verificarZonaT() 3");
               validarZonaT = persistenciaVigenciasConceptosTT.verificacionZonaTipoTrabajador(getEm(), secConcepto, secTT);
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
      try {
         return persistenciaFormulas.buscarFormulasCarge(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarFormulasCargue() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Formulas consultarFormulaCargueInicial() {
      try {
         return persistenciaFormulas.buscarFormulaCargeInicial(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarFormulaCargueInicial() ERROR: " + e);
         return null;
      }
   }

   @Override
   public boolean verificarFormulaCargueConcepto(BigInteger secConcepto, BigInteger secFormula) {
      try {
         return persistenciaFormulasConceptos.verificarFormulaCargue_Concepto(getEm(), secConcepto, secFormula);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".verificarFormulaCargueConcepto() ERROR: " + e);
         return false;
      }
   }

   @Override
   public boolean verificarNecesidadTercero(BigInteger secConcepto) {
      try {
         return persistenciaVigenciasGruposConceptos.verificacionGrupoUnoConcepto(getEm(), secConcepto);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".verificarNecesidadTercero() ERROR: " + e);
         return false;
      }
   }

   @Override
   public boolean verificarTerceroEmpresa(BigInteger nit, BigInteger secEmpresa) {
      try {
         return persistenciaTerceros.verificarTerceroParaEmpresaEmpleado(getEm(), nit, secEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".verificarTerceroEmpresa() ERROR: " + e);
         return false;
      }
   }

   @Override
   public List<String> consultarDocumentosSoporteCargadosUsuario(String usuarioBD) {
      try {
         return persistenciaTempNovedades.obtenerDocumentosSoporteCargados(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarDocumentosSoporteCargadosUsuario() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void cargarTempNovedades(Date fechaReporte, String nombreCorto, String usarFormula) {
      try {
         SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
         String fechaR = formatoFecha.format(fechaReporte);
         System.out.println("AdministrarCargueArchivos.cargarTempNovedades() fechaR: " + fechaR + ", nombreCorto: " + nombreCorto + " y usarFormula: " + usarFormula);
         persistenciaTempNovedades.cargarTempNovedades(getEm(), fechaR, nombreCorto, usarFormula);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".cargarTempNovedades() ERROR: " + e);
      }
   }

   @Override
   public int reversarNovedades(ActualUsuario usuarioBD, String documentoSoporte) {
      try {
         log.warn("AdministrarCargueArchivos.reversarNovedades() documentoSoporte: " + documentoSoporte + ", usuarioBD: " + usuarioBD);
         List<Novedades> listNovedades = persistenciaNovedades.novedadesParaReversar(getEm(), usuarioBD.getSecuencia(), documentoSoporte);
         if (listNovedades != null) {
            log.warn("reversarNovedades() listNovedades.size(): " + listNovedades.size());
         } else {
            log.warn("reversarNovedades() listNovedades.size(): null");
         }
         int validarNoLiquidadas = 0;
         for (int i = 0; i < listNovedades.size(); i++) {
            if (persistenciaSolucionesFormulas.validarNovedadesNoLiquidadas(getEm(), listNovedades.get(i).getSecuencia()) > 0) {
               validarNoLiquidadas++;
            }
         }
         listNovedades.clear();
//         if (validarNoLiquidadas == 0) {
         persistenciaTempNovedades.reversarTempNovedades(getEm(), usuarioBD.getAlias(), documentoSoporte);
         return persistenciaNovedades.reversarNovedades(getEm(), usuarioBD.getSecuencia(), documentoSoporte);
//         } else {
//            return 0;
//         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".reversarNovedades() ERROR: " + e);
         return 0;
      }
   }

   @Override
   public ResultadoBorrarTodoNovedades BorrarTodo(ActualUsuario usuarioBD, List<String> documentosSoporte) {
      try {
         ResultadoBorrarTodoNovedades resultadoProceso = new ResultadoBorrarTodoNovedades();
         int registrosBorrados = 0;
         for (int j = 0; j < documentosSoporte.size(); j++) {
            List<Novedades> listNovedades = persistenciaNovedades.novedadesParaReversar(getEm(), usuarioBD.getSecuencia(), documentosSoporte.get(j));
            int validarNoLiquidadas = 0;
            for (int i = 0; i < listNovedades.size(); i++) {
               if (persistenciaSolucionesFormulas.validarNovedadesNoLiquidadas(getEm(), listNovedades.get(i).getSecuencia()) > 0) {
                  validarNoLiquidadas++;
               }
            }
            listNovedades.clear();
            if (validarNoLiquidadas == 0) {
               registrosBorrados = registrosBorrados + persistenciaNovedades.reversarNovedades(getEm(), usuarioBD.getSecuencia(), documentosSoporte.get(j));
               persistenciaTempNovedades.reversarTempNovedades(getEm(), usuarioBD.getAlias(), documentosSoporte.get(j));
            } else {
               if (resultadoProceso.getDocumentosNoBorrados() == null) {
                  resultadoProceso.setDocumentosNoBorrados(new ArrayList<String>());
               }
               resultadoProceso.getDocumentosNoBorrados().add(documentosSoporte.get(j));
            }
         }
         resultadoProceso.setRegistrosBorrados(registrosBorrados);
         return resultadoProceso;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".BorrarTodo() ERROR: " + e);
         return null;
      }
   }

   public String consultarRuta() {
      try {
         Generales general = persistenciaGenerales.obtenerRutas(getEm());
         return general.getUbicareportes();
      } catch (Exception e) {
         log.warn("ERROR Administrar.AdministrarCargueArchivos.consultarRuta() e : " + e);
         return "C:\\DesignerRHN\\Reportes\\ArchivosPlanos\\";
      }
   }
}
