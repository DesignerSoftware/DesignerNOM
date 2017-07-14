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
import Persistencia.PersistenciaProcesos;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'DetalleConcepto'.
 *
 * @author Andres Pineda.
 */
@Stateful
public class AdministrarDetalleConcepto implements AdministrarDetalleConceptoInterface {

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

   private EntityManager em;

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public List<VigenciasCuentas> consultarListaVigenciasCuentasConcepto(BigInteger secConcepto) {
      try {
         List<VigenciasCuentas> lista = persistenciaVigenciasCuentas.buscarVigenciasCuentasPorConcepto(em, secConcepto);
         return lista;
      } catch (Exception e) {
         System.out.println("Error listVigenciasCuentasConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasCuentas(List<VigenciasCuentas> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasCuentas.crear(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearVigenciasCuentas Admi : " + e.toString());
      }
   }

   @Override
   public void modificarVigenciasCuentas(List<VigenciasCuentas> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasCuentas.editar(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearVigenciasCuentas Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasCuentas(List<VigenciasCuentas> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasCuentas.borrar(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearVigenciasCuentas Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposCentrosCostos> consultarLOVTiposCentrosCostos() {
      try {
         List<TiposCentrosCostos> tipos = persistenciaTiposCentrosCostos.buscarTiposCentrosCostos(em);
         return tipos;
      } catch (Exception e) {
         System.out.println("Error listTiposCentrosCostos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Cuentas> consultarLOVCuentas() {
      try {
         List<Cuentas> cuentas = persistenciaCuentas.buscarCuentas(em);
         return cuentas;
      } catch (Exception e) {
         System.out.println("Error listCuentas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<CentrosCostos> consultarLOVCentrosCostos() {
      try {
         List<CentrosCostos> centros = persistenciaCentrosCostos.buscarCentrosCostos(em);
         return centros;
      } catch (Exception e) {
         System.out.println("Error listCentrosCostos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasGruposConceptos> consultarListaVigenciasGruposConceptosConcepto(BigInteger secConcepto) {
      try {
         List<VigenciasGruposConceptos> lista = persistenciaVigenciasGruposConceptos.listVigenciasGruposConceptosPorConcepto(em, secConcepto);
         return lista;
      } catch (Exception e) {
         System.out.println("Error listVigenciasGruposConceptosConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVGC) {
      try {
         for (int i = 0; i < listaVGC.size(); i++) {
            persistenciaVigenciasGruposConceptos.crear(em, listaVGC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearVigenciasGruposConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void modificarVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVGC) {
      try {
         for (int i = 0; i < listaVGC.size(); i++) {
            persistenciaVigenciasGruposConceptos.editar(em, listaVGC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error editarVigenciasGruposConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVGC) {
      try {
         for (int i = 0; i < listaVGC.size(); i++) {
            persistenciaVigenciasGruposConceptos.borrar(em, listaVGC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error borrarVigenciasGruposConceptos Admi : " + e.toString());
      }
   }

   @Override
   public List<GruposConceptos> consultarLOVGruposConceptos() {
      try {
         List<GruposConceptos> grupos = persistenciaGruposConceptos.buscarGruposConceptos(em);
         return grupos;
      } catch (Exception e) {
         System.out.println("Error listGruposConceptos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasConceptosTT> consultarListaVigenciasConceptosTTConcepto(BigInteger secConcepto) {
      try {
         List<VigenciasConceptosTT> lista = persistenciaVigenciasConceptosTT.listVigenciasConceptosTTPorConcepto(em, secConcepto);
         return lista;
      } catch (Exception e) {
         System.out.println("Error listVigenciasConceptosTTConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasConceptosTT(List<VigenciasConceptosTT> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTT.crear(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearVigenciasConceptosTT Admi : " + e.toString());
      }
   }

   @Override
   public void modificarVigenciasConceptosTT(List<VigenciasConceptosTT> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTT.editar(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error editarVigenciasConceptosTT Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasConceptosTT(List<VigenciasConceptosTT> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTT.borrar(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error borrarVigenciasConceptosTT Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposTrabajadores> consultarLOVTiposTrabajadores() {
      try {
         List<TiposTrabajadores> tipos = persistenciaTiposTrabajadores.buscarTiposTrabajadores(em);
         return tipos;
      } catch (Exception e) {
         System.out.println("Error listTiposTrabajadores Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasConceptosTC> consultarListaVigenciasConceptosTCConcepto(BigInteger secConcepto) {
      try {
         List<VigenciasConceptosTC> lista = persistenciaVigenciasConceptosTC.listVigenciasConceptosTCPorConcepto(em, secConcepto);
         return lista;
      } catch (Exception e) {
         System.out.println("Error listVigenciasConceptosTCConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasConceptosTC(List<VigenciasConceptosTC> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTC.crear(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public void modificarVigenciasConceptosTC(List<VigenciasConceptosTC> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTC.editar(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error editarVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasConceptosTC(List<VigenciasConceptosTC> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosTC.borrar(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error borrarVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposContratos> consultarLOVTiposContratos() {
      try {
         List<TiposContratos> tipos = persistenciaTiposContratos.tiposContratos(em);
         return tipos;
      } catch (Exception e) {
         System.out.println("Error listTiposContratos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<VigenciasConceptosRL> consultarListaVigenciasConceptosRLCConcepto(BigInteger secConcepto) {
      try {
         List<VigenciasConceptosRL> lista = persistenciaVigenciasConceptosRL.listVigenciasConceptosRLPorConcepto(em, secConcepto);
         return lista;
      } catch (Exception e) {
         System.out.println("Error listVigenciasConceptosRLCConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasConceptosRL(List<VigenciasConceptosRL> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosRL.crear(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public void modificarVigenciasConceptosRL(List<VigenciasConceptosRL> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosRL.editar(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error editarVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasConceptosRL(List<VigenciasConceptosRL> listaVC) {
      try {
         for (int i = 0; i < listaVC.size(); i++) {
            persistenciaVigenciasConceptosRL.borrar(em, listaVC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error borrarVigenciasConceptosTC Admi : " + e.toString());
      }
   }

   @Override
   public List<ReformasLaborales> consultarLOVReformasLaborales() {
      try {
         List<ReformasLaborales> reformas = persistenciaReformasLaborales.buscarReformasLaborales(em);
         return reformas;
      } catch (Exception e) {
         System.out.println("Error listReformasLaborales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<FormulasConceptos> consultarListaFormulasConceptosConcepto(BigInteger secConcepto) {
      try {
         List<FormulasConceptos> lista = persistenciaFormulasConceptos.formulasConceptosXSecConcepto(em, secConcepto);
         return lista;
      } catch (Exception e) {
         System.out.println("Error listFormulasConceptosConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearFormulasConceptos(List<FormulasConceptos> listaFC) {
      try {
         for (int i = 0; i < listaFC.size(); i++) {
            persistenciaFormulasConceptos.crear(em, listaFC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void modificarFormulasConceptos(List<FormulasConceptos> listaFC) {
      try {
         for (int i = 0; i < listaFC.size(); i++) {
            persistenciaFormulasConceptos.editar(em, listaFC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error editarFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarFormulasConceptos(List<FormulasConceptos> listaFC) {
      try {
         for (int i = 0; i < listaFC.size(); i++) {
            persistenciaFormulasConceptos.borrar(em, listaFC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error borrarFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public List<Formulas> consultarLOVFormulas() {
      try {
         List<Formulas> formulas = persistenciaFormulas.buscarFormulas(em);
         return formulas;
      } catch (Exception e) {
         System.out.println("Error listFormulas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<FormulasConceptos> consultarLOVFormulasConceptos() {
      try {
         List<FormulasConceptos> formulas = persistenciaFormulasConceptos.buscarFormulasConceptos(em);
         return formulas;
      } catch (Exception e) {
         System.out.println("Error listFormulasConceptos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Procesos> consultarLOVProcesos() {
      try {
         System.out.println("Si entro en consultarLOVProcesos()");
         return persistenciaProcesos.lovProcesos(em);
      } catch (Exception e) {
         System.out.println("Error listFormulasConceptos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Long contarFormulasConceptosConcepto(BigInteger secConcepto) {
      try {
         Long retorno = PersistenciaFormulasConceptos.comportamientoConceptoAutomaticoSecuenciaConcepto(em, secConcepto);
         return retorno;
      } catch (Exception e) {
         System.out.println("Error comportamientoAutomaticoConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Long contarFormulasNovedadesConcepto(BigInteger secConcepto) {
      try {
         Long retorno = PersistenciaFormulasConceptos.comportamientoConceptoSemiAutomaticoSecuenciaConcepto(em, secConcepto);
         return retorno;
      } catch (Exception e) {
         System.out.println("Error comportamientoSemiAutomaticoConcepto Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Conceptos consultarConceptoActual(BigInteger secConcepto) {
      try {
         Conceptos actual = persistenciaConceptos.conceptosPorSecuencia(em, secConcepto);
         return actual;
      } catch (Exception e) {
         System.out.println("Error conceptoActual Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public boolean eliminarConceptoTotal(BigInteger secConcepto) {
      try {
         return persistenciaConceptos.eliminarConcepto(em, secConcepto);
      } catch (Exception e) {
         System.out.println("Error eliminarConcepto Admi : " + e.toString());
         return false;
      }
   }

   @Override
   public boolean verificarSolucionesNodosConcepto(BigInteger secConcepto) {
      try {
         boolean retorno = persistenciaSolucionesNodos.solucionesNodosParaConcepto(em, secConcepto);
         return retorno;
      } catch (Exception e) {
         System.out.println("Error verificarSolucionesNodosParaConcepto Admi : " + e.toString());
         return false;
      }
   }

   public int contarVigCuentasPorTipoccConceptoYCuentac(BigInteger tipoCC, BigInteger cuentaC, BigInteger concepto, Date fechaIni) {
      try {
         return persistenciaCuentas.contarVigCuentasPorTipoccConceptoYCuentac(em, tipoCC, cuentaC, concepto, fechaIni);
      } catch (Exception e) {
         System.out.println("AdministrarDetalleConcepto.contarVigCuentasPorTipoccConceptoYCuentac() ERROR : " + e);
         return 0;
      }
   }

   public int contarVigCuentasPorTipoccConceptoYCuentad(BigInteger tipoCC, BigInteger cuentaD, BigInteger concepto, Date fechaIni) {
      try {
         return persistenciaCuentas.contarVigCuentasPorTipoccConceptoYCuentad(em, tipoCC, cuentaD, concepto, fechaIni);
      } catch (Exception e) {
         System.out.println("AdministrarDetalleConcepto.contarVigCuentasPorTipoccConceptoYCuentad() ERROR : " + e);
         return 0;
      }
   }

   public CentrosCostos centroCostoLocalizacionTrabajador(BigInteger secEmpresa) {
      try {
         return persistenciaCuentas.centroCostoLocalizacionTrabajador(em, secEmpresa);
      } catch (Exception e) {
         System.out.println("AdministrarDetalleConcepto.centroCostoLocalizacionTrabajador() ERROR : " + e);
         return null;
      }
   }

   public CentrosCostos centroCostoContabilidad(BigInteger secEmpresa) {
      try {
         return persistenciaCuentas.centroCostoContabilidad(em, secEmpresa);
      } catch (Exception e) {
         System.out.println("AdministrarDetalleConcepto.centroCostoContabilidad() ERROR : " + e);
         return null;
      }
   }

   public List<Cuentas> cuenta2505(BigInteger secEmpresa) {
      try {
         return persistenciaCuentas.cuenta2505(em, secEmpresa);
      } catch (Exception e) {
         System.out.println("AdministrarDetalleConcepto.cuenta2505() ERROR : " + e);
         return null;
      }
   }
}
