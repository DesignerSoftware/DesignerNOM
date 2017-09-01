/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.Conceptos;
import Entidades.Cuentas;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import Entidades.GruposConceptos;
import Entidades.Procesos;
import Entidades.ReformasLaborales;
import Entidades.TiposCentrosCostos;
import Entidades.TiposContratos;
import Entidades.TiposTrabajadores;
import Entidades.VigenciasConceptosRL;
import Entidades.VigenciasConceptosTC;
import Entidades.VigenciasConceptosTT;
import Entidades.VigenciasCuentas;
import Entidades.VigenciasGruposConceptos;
import InterfaceAdministrar.AdministrarDetalleConceptoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaCuentasInterface;
import InterfacePersistencia.PersistenciaFormulasConceptosInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaGruposConceptosInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaReformasLaboralesInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTiposCentrosCostosInterface;
import InterfacePersistencia.PersistenciaTiposContratosInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasConceptosRLInterface;
import InterfacePersistencia.PersistenciaVigenciasConceptosTCInterface;
import InterfacePersistencia.PersistenciaVigenciasConceptosTTInterface;
import InterfacePersistencia.PersistenciaVigenciasCuentasInterface;
import InterfacePersistencia.PersistenciaVigenciasGruposConceptosInterface;
import java.math.BigInteger;
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
 * 'DetalleConcepto'.
 *
 * @author Andres Pineda.
 */
@Stateful
public class AdministrarDetalleConcepto implements AdministrarDetalleConceptoInterface {

   private static Logger log = Logger.getLogger(AdministrarDetalleConcepto.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaVigenciasCuentas'.
    */
   @EJB
   PersistenciaVigenciasCuentasInterface persistenciaVigenciasCuentas;
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
    * 'persistenciaVigenciasConceptosTT'.
    */
   @EJB
   PersistenciaVigenciasConceptosTTInterface persistenciaVigenciasConceptosTT;
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
    * 'persistenciaVigenciasConceptosRL'.
    */
   @EJB
   PersistenciaVigenciasConceptosRLInterface persistenciaVigenciasConceptosRL;
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
    * 'persistenciaConceptos'.
    */
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposCentrosCostos'.
    */
   @EJB
   PersistenciaTiposCentrosCostosInterface persistenciaTiposCentrosCostos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCuentas'.
    */
   @EJB
   PersistenciaCuentasInterface persistenciaCuentas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCentrosCostos'.
    */
   @EJB
   PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaGruposConceptos'.
    */
   @EJB
   PersistenciaGruposConceptosInterface persistenciaGruposConceptos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposTrabajadores'.
    */
   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposContratos'.
    */
   @EJB
   PersistenciaTiposContratosInterface persistenciaTiposContratos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaReformasLaborales'.
    */
   @EJB
   PersistenciaReformasLaboralesInterface persistenciaReformasLaborales;
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
    * 'PersistenciaFormulasConceptos'.
    */
   @EJB
   PersistenciaFormulasConceptosInterface PersistenciaFormulasConceptos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaSolucionesNodos'.
    */
   @EJB
   PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;

   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasCuentas> consultarListaVigenciasCuentasConcepto(BigInteger secConcepto) {
      try {
         return persistenciaVigenciasCuentas.buscarVigenciasCuentasPorConcepto(getEm(), secConcepto);
      } catch (Exception e) {
         log.warn("Error listVigenciasCuentasConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasCuentas(List<VigenciasCuentas> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasCuentas.crear(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasCuentas Admi : " + e.toString());
      }
   }

   @Override
   public void modificarVigenciasCuentas(List<VigenciasCuentas> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasCuentas.editar(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasCuentas Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasCuentas(List<VigenciasCuentas> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasCuentas.borrar(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasCuentas Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposCentrosCostos> consultarLOVTiposCentrosCostos() {
      try {
         return persistenciaTiposCentrosCostos.buscarTiposCentrosCostos(getEm());
      } catch (Exception e) {
         log.warn("Error listTiposCentrosCostos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Cuentas> consultarLOVCuentas() {
      try {
         return persistenciaCuentas.buscarCuentas(getEm());
      } catch (Exception e) {
         log.warn("Error listCuentas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<CentrosCostos> consultarLOVCentrosCostos() {
      try {
         return persistenciaCentrosCostos.buscarCentrosCostos(getEm());
      } catch (Exception e) {
         log.warn("Error listCentrosCostos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasGruposConceptos> consultarListaVigenciasGruposConceptosConcepto(BigInteger secConcepto) {
      try {
         return persistenciaVigenciasGruposConceptos.listVigenciasGruposConceptosPorConcepto(getEm(), secConcepto);
      } catch (Exception e) {
         log.warn("Error listVigenciasGruposConceptosConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVGC) {
      try {
         for (int i = 0; i < listaVGC.size(); i++) {
            persistenciaVigenciasGruposConceptos.crear(getEm(), listaVGC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasGruposConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void modificarVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVGC) {
      try {
         for (int i = 0; i < listaVGC.size(); i++) {
            persistenciaVigenciasGruposConceptos.editar(getEm(), listaVGC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarVigenciasGruposConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVGC) {
      try {
         for (int i = 0; i < listaVGC.size(); i++) {
            persistenciaVigenciasGruposConceptos.borrar(getEm(), listaVGC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarVigenciasGruposConceptos Admi : " + e.toString());
      }
   }

   @Override
   public List<GruposConceptos> consultarLOVGruposConceptos() {
      try {
         return persistenciaGruposConceptos.buscarGruposConceptos(getEm());
      } catch (Exception e) {
         log.warn("Error listGruposConceptos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasConceptosTT> consultarListaVigenciasConceptosTTConcepto(BigInteger secConcepto) {
      try {
         return persistenciaVigenciasConceptosTT.listVigenciasConceptosTTPorConcepto(getEm(), secConcepto);
      } catch (Exception e) {
         log.warn("Error listVigenciasConceptosTTConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasConceptosTT(List<VigenciasConceptosTT> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTT.crear(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasConceptosTT Admi : " + e.toString());
      }
   }

   @Override
   public void modificarVigenciasConceptosTT(List<VigenciasConceptosTT> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTT.editar(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarVigenciasConceptosTT Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasConceptosTT(List<VigenciasConceptosTT> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTT.borrar(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarVigenciasConceptosTT Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposTrabajadores> consultarLOVTiposTrabajadores() {
      try {
         return persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.warn("Error listTiposTrabajadores Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasConceptosTC> consultarListaVigenciasConceptosTCConcepto(BigInteger secConcepto) {
      try {
         return persistenciaVigenciasConceptosTC.listVigenciasConceptosTCPorConcepto(getEm(), secConcepto);
      } catch (Exception e) {
         log.warn("Error listVigenciasConceptosTCConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasConceptosTC(List<VigenciasConceptosTC> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTC.crear(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public void modificarVigenciasConceptosTC(List<VigenciasConceptosTC> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTC.editar(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasConceptosTC(List<VigenciasConceptosTC> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTC.borrar(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposContratos> consultarLOVTiposContratos() {
      try {
         return persistenciaTiposContratos.tiposContratos(getEm());
      } catch (Exception e) {
         log.warn("Error listTiposContratos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasConceptosRL> consultarListaVigenciasConceptosRLCConcepto(BigInteger secConcepto) {
      try {
         return persistenciaVigenciasConceptosRL.listVigenciasConceptosRLPorConcepto(getEm(), secConcepto);
      } catch (Exception e) {
         log.warn("Error listVigenciasConceptosRLCConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasConceptosRL(List<VigenciasConceptosRL> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosRL.crear(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public void modificarVigenciasConceptosRL(List<VigenciasConceptosRL> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosRL.editar(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasConceptosRL(List<VigenciasConceptosRL> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosRL.borrar(getEm(), listaVC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public List<ReformasLaborales> consultarLOVReformasLaborales() {
      try {
         return persistenciaReformasLaborales.buscarReformasLaborales(getEm());
      } catch (Exception e) {
         log.warn("Error listReformasLaborales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<FormulasConceptos> consultarListaFormulasConceptosConcepto(BigInteger secConcepto) {
      try {
         List<FormulasConceptos> lista = persistenciaFormulasConceptos.formulasConceptosXSecConcepto(getEm(), secConcepto);
         return lista;
      } catch (Exception e) {
         log.warn("Error listFormulasConceptosConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearFormulasConceptos(List<FormulasConceptos> listaFC) {
      try {
         for (int i = 0; i < listaFC.size(); i++) {
            persistenciaFormulasConceptos.crear(getEm(), listaFC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void modificarFormulasConceptos(List<FormulasConceptos> listaFC) {
      try {
         for (int i = 0; i < listaFC.size(); i++) {
            persistenciaFormulasConceptos.editar(getEm(), listaFC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarFormulasConceptos(List<FormulasConceptos> listaFC) {
      try {
         for (int i = 0; i < listaFC.size(); i++) {
            persistenciaFormulasConceptos.borrar(getEm(), listaFC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public List<Formulas> consultarLOVFormulas() {
      try {
         return persistenciaFormulas.buscarFormulas(getEm());
      } catch (Exception e) {
         log.warn("Error listFormulas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<FormulasConceptos> consultarLOVFormulasConceptos() {
      try {
         return persistenciaFormulasConceptos.buscarFormulasConceptos(getEm());
      } catch (Exception e) {
         log.warn("Error listFormulasConceptos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Procesos> consultarLOVProcesos() {
      try {
         log.warn("Si entro en consultarLOVProcesos()");
         return persistenciaProcesos.lovProcesos(getEm());
      } catch (Exception e) {
         log.warn("Error listFormulasConceptos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Long contarFormulasConceptosConcepto(BigInteger secConcepto) {
      try {
         return PersistenciaFormulasConceptos.comportamientoConceptoAutomaticoSecuenciaConcepto(getEm(), secConcepto);
      } catch (Exception e) {
         log.warn("Error comportamientoAutomaticoConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Long contarFormulasNovedadesConcepto(BigInteger secConcepto) {
      try {
         return PersistenciaFormulasConceptos.comportamientoConceptoSemiAutomaticoSecuenciaConcepto(getEm(), secConcepto);
      } catch (Exception e) {
         log.warn("Error comportamientoSemiAutomaticoConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Conceptos consultarConceptoActual(BigInteger secConcepto) {
      try {
         return persistenciaConceptos.conceptosPorSecuencia(getEm(), secConcepto);
      } catch (Exception e) {
         log.warn("Error conceptoActual Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public boolean eliminarConceptoTotal(BigInteger secConcepto) {
      try {
         return persistenciaConceptos.eliminarConcepto(getEm(), secConcepto);
      } catch (Exception e) {
         log.warn("Error eliminarConcepto Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean verificarSolucionesNodosConcepto(BigInteger secConcepto) {
      try {
         boolean retorno = persistenciaSolucionesNodos.solucionesNodosParaConcepto(getEm(), secConcepto);
         return retorno;
      } catch (Exception e) {
         log.warn("Error verificarSolucionesNodosParaConcepto Admi : " + e.toString());
         return false;
      }
   }

   public int contarVigCuentasPorTipoccConceptoYCuentac(BigInteger tipoCC, BigInteger cuentaC, BigInteger concepto, Date fechaIni) {
      try {
         return persistenciaCuentas.contarVigCuentasPorTipoccConceptoYCuentac(getEm(), tipoCC, cuentaC, concepto, fechaIni);
      } catch (Exception e) {
         log.warn("AdministrarDetalleConcepto.contarVigCuentasPorTipoccConceptoYCuentac() ERROR : " + e);
         return 0;
      }
   }

   public int contarVigCuentasPorTipoccConceptoYCuentad(BigInteger tipoCC, BigInteger cuentaD, BigInteger concepto, Date fechaIni) {
      try {
         return persistenciaCuentas.contarVigCuentasPorTipoccConceptoYCuentad(getEm(), tipoCC, cuentaD, concepto, fechaIni);
      } catch (Exception e) {
         log.warn("AdministrarDetalleConcepto.contarVigCuentasPorTipoccConceptoYCuentad() ERROR : " + e);
         return 0;
      }
   }

   public CentrosCostos centroCostoLocalizacionTrabajador(BigInteger secEmpresa) {
      try {
         return persistenciaCuentas.centroCostoLocalizacionTrabajador(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("AdministrarDetalleConcepto.centroCostoLocalizacionTrabajador() ERROR : " + e);
         return null;
      }
   }

   public CentrosCostos centroCostoContabilidad(BigInteger secEmpresa) {
      try {
         return persistenciaCuentas.centroCostoContabilidad(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("AdministrarDetalleConcepto.centroCostoContabilidad() ERROR : " + e);
         return null;
      }
   }

   public List<Cuentas> cuenta2505(BigInteger secEmpresa) {
      try {
         return persistenciaCuentas.cuenta2505(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("AdministrarDetalleConcepto.cuenta2505() ERROR : " + e);
         return null;
      }
   }
}
