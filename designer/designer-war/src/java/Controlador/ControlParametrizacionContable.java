/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import Entidades.CentrosCostos;
import Entidades.ConceptosAux2;
import Entidades.Cuentas;
import Entidades.Procesos;
import Entidades.TiposCentrosCostos;
import Entidades.VigenciasCuentas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDetalleConceptoInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import org.apache.log4j.Logger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@ManagedBean
@javax.faces.bean.SessionScoped
public class ControlParametrizacionContable implements Serializable {

   private static Logger log = Logger.getLogger(ControlParametrizacionContable.class);

   @EJB
   AdministrarDetalleConceptoInterface administrarDetalleConcepto;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //////////////Conceptos////////////////
   private ConceptosAux2 conceptoSeleccionado;
   private List<ConceptosAux2> filtrarlistaConceptos;
   private List<ConceptosAux2> listaConceptos;
   ///////////VigenciasCuentas////////////
   private List<VigenciasCuentas> listaVigenciasCuentas;
   private List<VigenciasCuentas> filtrarListVigenciasCuentasConcepto;
   private VigenciasCuentas vigenciaCuentaSeleccionada, vigCuentaCrearSeleccionada, vigCuentaEditar;
   private String altoTabla;//, altoTablaConceptos;
   //////////////
   private List<VigenciasCuentas> listVigCParaCrear;
   private List<VigenciasCuentas> listVigenciasCuentasModificar;
   private List<VigenciasCuentas> listVigenciasCuentasCrear;
   private List<VigenciasCuentas> listVigenciasCuentasBorrar;
   private int cualCelda, tipoListaVigenciaCuenta, tipoLista;

   ////////////Listas Valores VigenciasCuentas/////////////
   private List<TiposCentrosCostos> lovTiposCentrosCostos;
   private List<TiposCentrosCostos> filtrarListTiposCentrosCostos;
   private TiposCentrosCostos tipoCentroCostoSeleccionadoLOV;

   private List<Cuentas> lovCuentas;
   private List<Cuentas> lovCuentas2505;
   private List<Cuentas> filtrarListCuentas;
   private Cuentas cuentaSeleccionadaLOV;

   private List<CentrosCostos> lovCentrosCostos;
   private List<CentrosCostos> filtrarListCentrosCostos;
   private CentrosCostos centroCostoSeleccionadoLOV;

   private List<Procesos> lovProcesos;
   private List<Procesos> filtrarlovProcesos;
   private Procesos procesoSeleccionadoLOV;

   //////////////Otros////////////////
   private boolean aceptar;
   private boolean guardado;
   private BigInteger l;
   private int k;
   private String paginaAnterior = "nominaf";
   private int tipoActualizacion;

   private String comportamientoConcepto, mensajeError = "";
   private String infoRegistroVCuenta, infoRegistroConceptos, infoRegistroLovTipoCentroCosto, infoRegistroLovCuentaDebito, infoRegistroLovCuentas, infoRegistroLovCuentaCredito, infoRegistroLovCentroCostoDebito, infoRegistroLovCentroCostoCredito, infoRegistroLovProcesos;
   private ListasRecurrentes listasRecurrentes;
   private int bandera;

   private String auxVC_TipoCC, auxVC_Debito, auxVC_DescDeb, auxVC_Credito, auxVC_DescCre, auxVC_ConsDeb, auxVC_ConsCre, auxVC_Proceso;
   private Date auxVC_FechaIni, auxVC_FechaFin, fechaParametro;
   private Column checkconcepto, codigoconcepto, descripcionconcepto, naturalezaconcepto, activoconcepto, conjuntoconcepto;
   private Column vigenciaCuentaFechaInicial, vigenciaCuentaFechaFinal, vigenciaCuentaTipoCC, vigenciaCuentaDebitoCod, vigenciaCuentaDebitoDes,
           vigenciaCuentaCCConsolidadorD, vigenciaCuentaCreditoCod, vigenciaCuentaCreditoDes, vigenciaCuentaCCProceso, vigenciaCuentaCCConsolidadorC;
   private String nombreXML, nombreExportar, nombreTabla;

   /**
    * Creates a new instance of ControlParametrizacionContable
    */
   public ControlParametrizacionContable() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
      altoTabla = "140";
      //
      conceptoSeleccionado = null;
      listaVigenciasCuentas = null;
      comportamientoConcepto = "";
      lovTiposCentrosCostos = null;
      lovCuentas = null;
      lovCentrosCostos = null;
      lovProcesos = null;

      tipoCentroCostoSeleccionadoLOV = null;
      cuentaSeleccionadaLOV = null;
      centroCostoSeleccionadoLOV = null;

      aceptar = true;
      k = 0;
      tipoActualizacion = -1;

      listVigenciasCuentasBorrar = new ArrayList<VigenciasCuentas>();
      listVigenciasCuentasCrear = new ArrayList<VigenciasCuentas>();
      listVigCParaCrear = new ArrayList<VigenciasCuentas>();
      listVigenciasCuentasModificar = new ArrayList<VigenciasCuentas>();

      cualCelda = -1;
      tipoListaVigenciaCuenta = 0;
      tipoLista = 0;
      guardado = true;
      vigenciaCuentaSeleccionada = null;
      fechaParametro = new Date(1, 1, 0);

      infoRegistroLovTipoCentroCosto = "0";
      infoRegistroLovCuentaDebito = "0";
      infoRegistroLovCuentaCredito = "0";
      infoRegistroLovCentroCostoDebito = "0";
      infoRegistroLovCentroCostoCredito = "0";

      nombreTabla = ":formExportarVigenciasCuentas:datosVigenciaCuentasExportar";
      nombreXML = "ConceptosXML";
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "parametrizacioncontable";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
      } else if (pag.equals(pagActual)) {
         controlListaNavegacion.guardarNavegacion("nominaf", pagActual);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pagActual);
      } else {
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
      }
      limpiarListasValor();
   }

   public void limpiarListasValor() {
      log.warn("Controlador.ControlParametrizacionContable.limpiarListasValor()");
      lovCentrosCostos = null;
      lovCuentas = null;
      lovProcesos = null;
      lovTiposCentrosCostos = null;
   }

   public void limpiarListas() {
      listVigenciasCuentasBorrar.clear();
      listVigenciasCuentasCrear.clear();
      listVigenciasCuentasModificar.clear();
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }

   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      log.info("Entro en inicializarAdministrador()");
      FacesContext x = FacesContext.getCurrentInstance();
      HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
      administrarDetalleConcepto.obtenerConexion(ses.getId());
      try {
         getListaConceptos();
         if (listaConceptos != null) {
            if (!listaConceptos.isEmpty()) {
               conceptoSeleccionado = listaConceptos.get(0);
               for (int i = 0; i < 7; i++) {
                  k++;
                  listVigCParaCrear.add(new VigenciasCuentas((new BigInteger("" + k)), conceptoSeleccionado));
               }
            }
         }
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void modificarVigenciaCuenta(VigenciasCuentas cuenta, String columnCambio, String valor) {
      cargarLOVsVCuentas();
      int coincidencias = 0;
      tipoActualizacion = 0;
      int indiceUnicoElemento = 0;
      vigenciaCuentaSeleccionada = cuenta;

      if (columnCambio.equalsIgnoreCase("TIPOCC")) {
         vigenciaCuentaSeleccionada.setNombreTipocc(auxVC_TipoCC);

         for (int i = 0; i < lovTiposCentrosCostos.size(); i++) {
            if (lovTiposCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setTipocc(lovTiposCentrosCostos.get(indiceUnicoElemento));
            autocompletarTipoCC(vigenciaCuentaSeleccionada);
         } else {
            contarRegistrosLovTipoCentroCosto();
            RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
            RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').show()");
         }
      }
      if (columnCambio.equalsIgnoreCase("CODDEBITO")) {
         vigenciaCuentaSeleccionada.setCodCuentad(auxVC_Debito);

         for (int i = 0; i < lovCuentas.size(); i++) {
            if (lovCuentas.get(i).getCodigo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setCuentad(lovCuentas.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCuentaDebito();
            RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
         }
      }
      if (columnCambio.equalsIgnoreCase("DESDEBITO")) {
         vigenciaCuentaSeleccionada.setDescripcionCuentad(auxVC_DescDeb);

         for (int i = 0; i < lovCuentas.size(); i++) {
            if (lovCuentas.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setCuentad(lovCuentas.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCuentaDebito();
            RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
         }
      }
      if (columnCambio.equalsIgnoreCase("CONSOLIDADORDEBITO")) {
         vigenciaCuentaSeleccionada.setNombreConsolidadord(auxVC_ConsDeb);

         for (int i = 0; i < lovCentrosCostos.size(); i++) {
            if (lovCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setConsolidadord(lovCentrosCostos.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCentroCostoDebito();
            RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
            RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
         }
      }
      if (columnCambio.equalsIgnoreCase("CODCREDITO")) {
         vigenciaCuentaSeleccionada.setCodCuentac(auxVC_Credito);

         for (int i = 0; i < lovCuentas.size(); i++) {
            if (lovCuentas.get(i).getCodigo().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setCuentac(lovCuentas.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCuentaCredito();
            RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
            RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
         }
      }
      if (columnCambio.equalsIgnoreCase("DESCREDITO")) {
         vigenciaCuentaSeleccionada.setDescripcionCuentac(auxVC_DescCre);

         for (int i = 0; i < lovCuentas.size(); i++) {
            if (lovCuentas.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setCuentac(lovCuentas.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCuentaCredito();
            RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
         }
      }
      if (columnCambio.equalsIgnoreCase("PROCESO")) {
         if (valor.equals("") || valor.isEmpty()) {
            vigenciaCuentaSeleccionada.setNombreProceso("");
            vigenciaCuentaSeleccionada.setProceso(null);
            coincidencias = 1;
         } else {
            vigenciaCuentaSeleccionada.setNombreProceso(auxVC_Proceso);

            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigenciaCuentaSeleccionada.setProceso(lovProcesos.get(indiceUnicoElemento).getSecuencia());
               vigenciaCuentaSeleccionada.setNombreProceso(lovProcesos.get(indiceUnicoElemento).getDescripcion());
            } else {
               contarRegistrosLovCuentaCredito();
               RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
            }
         }
      }
      if (columnCambio.equalsIgnoreCase("CONSOLIDADORCREDITO")) {
         vigenciaCuentaSeleccionada.setNombreConsolidadorc(auxVC_ConsCre);

         for (int i = 0; i < lovCentrosCostos.size(); i++) {
            if (lovCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaCuentaSeleccionada.setConsolidadorc(lovCentrosCostos.get(indiceUnicoElemento));
         } else {
            contarRegistrosLovCentroCostoCredito();
            RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
            RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
         }
      }
      if (coincidencias == 1) {
         modificarVigenciaCuenta();
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
   }

   public void modificarVigenciaCuenta() {
      if (validarActualizarVigenciaCuenta()) {
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
         }
      }
   }

   public boolean autocompletarTipoCC(VigenciasCuentas vigCuenta) {
      int error = 0;
      CentrosCostos ccLocalizacionTrabajador = null;
      CentrosCostos ccContabilidad = null;
      List<Cuentas> cuenta2505 = null;
      log.info("ControlParametrizacionContable.autocompletarTipoCC() conceptoSeleccionado : " + conceptoSeleccionado);
      log.info("ControlParametrizacionContable.autocompletarTipoCC() vigCuenta : " + vigCuenta);
      log.info("ControlParametrizacionContable.autocompletarTipoCC() vigCuenta.getConcepto() : " + vigCuenta.getConcepto());
      if (vigCuenta.getConcepto() == null) {
         vigCuenta.setConcepto(conceptoSeleccionado);
      }
      if (vigCuenta.getConcepto() != null) {
         log.info("ControlParametrizacionContable.autocompletarTipoCC() 1");
         int conteoVCuentas = administrarDetalleConcepto.contarVigCuentasPorTipoccConceptoYCuentac(vigCuenta.getTipocc(), vigCuenta.getCuentac(), vigCuenta.getConcepto(), vigCuenta.getFechainicial());
         if (conteoVCuentas > 1) {
            mensajeError = "El Tipo de Centro Costo que Ingresó ya está asignado a otra VigenciaCuenta";
            log.info("El Tipo de Centro Costo que Ingresó ya está asignado a otra VigenciaCuenta 1");
            error++;
         } else {
            conteoVCuentas = administrarDetalleConcepto.contarVigCuentasPorTipoccConceptoYCuentad(vigCuenta.getTipocc(), vigCuenta.getCuentad(), vigCuenta.getConcepto(), vigCuenta.getFechainicial());
            if (conteoVCuentas > 1) {
               mensajeError = "El Tipo de Centro Costo que Ingresó ya está asignado a otro VigenciaCuenta";
               log.info("El Tipo de Centro Costo que Ingresó ya está asignado a otra VigenciaCuenta 2");
               error++;
            }
         }
         log.info("ControlParametrizacionContable.autocompletarTipoCC() error: " + error);
         if (error == 0) {
            ccLocalizacionTrabajador = administrarDetalleConcepto.centroCostoLocalizacionTrabajador(vigCuenta.getEmpresa());
            ccContabilidad = administrarDetalleConcepto.centroCostoContabilidad(vigCuenta.getEmpresa());
            cuenta2505 = administrarDetalleConcepto.cuenta2505(vigCuenta.getEmpresa());
            log.info("ccLocalizacionTrabajador: " + ccLocalizacionTrabajador);
            log.info("ccContabilidad: " + ccContabilidad);
            log.info("cuenta2505: " + cuenta2505);
            if (cuenta2505 != null) {
               if (cuenta2505.size() != 1) {
                  lovCuentas2505.clear();
                  for (Cuentas recCuenta : lovCuentas) {
                     if (recCuenta.getCodigo().startsWith("2505")) {
                        lovCuentas2505.add(recCuenta);
                     }
                  }
                  error++;
                  mensajeError = "cuentas con [2505...] retornan mas de un registro o ninguno.";
                  log.info(mensajeError);
                  RequestContext.getCurrentInstance().update("formlovCuentas:CuentasDialogo");
                  RequestContext.getCurrentInstance().update("formlovCuentas:lovCuentas");
                  RequestContext.getCurrentInstance().execute("PF('CuentasDialogo').show()");
                  contarRegistrosCuentas();
               }
            } else {
               mensajeError = "Se genero un conflicto consultando cuentas con [2505...]";
               log.info(mensajeError);
               error++;
            }
            log.warn("Error: " + error);
            if (error == 0) {
               log.info("vigCuenta.getConcepto().getNaturaleza(): " + vigCuenta.getNaturaleza());
               log.info("vigCuenta.getNombreTipocc(): " + vigCuenta.getNombreTipocc());
               log.info("vigCuenta.getConsolidadord(): " + vigCuenta.getConsolidadord());
               log.info("vigCuenta.getConsolidadorc(): " + vigCuenta.getConsolidadorc());
               log.info("vigCuenta.getTipocc(): " + vigCuenta.getTipocc());
               if (vigCuenta.getTipocc() != null) {
                  log.info("vigCuenta.getNombreTipocc(): " + vigCuenta.getNombreTipocc());
               }
               if (vigCuenta.getNaturaleza().equals("P")) {
                  log.info("vigCuenta.getNombreTipocc().contains(\"APLICA\")  :: ->" + (vigCuenta.getNombreTipocc().contains("APLICA")));
                  if (!vigCuenta.getNombreTipocc().contains("APLICA")) {
                     if (vigCuenta.getConsolidadord() != null) {
                        if (vigCuenta.getNombreConsolidadord() != null) {
                           log.info("vigCuenta.getNombreConsolidadord(): " + vigCuenta.getNombreConsolidadord());
                           mensajeError = "Atención: Se va a cambiar el centro de costo consolidador débito por la localizacion del trabajador. Revise por favor esta configuración.";
                           log.info(mensajeError);
                           error++;
                        }
                     }
                     vigCuenta.setConsolidadord(ccLocalizacionTrabajador);
                     vigCuenta.setConsolidadorc(ccContabilidad);
                  }
                  if (vigCuenta.getCuentac() == null) {
                     vigCuenta.setCuentac(cuenta2505.get(0));
                  } else if (vigCuenta.getCuentac() == null) {
                     vigCuenta.setCuentac(cuenta2505.get(0));
                  }
               }
               if (vigCuenta.getNaturaleza().equals("D")) {
                  if (!vigCuenta.getNombreTipocc().contains("APLICA")) {
                     if (vigCuenta.getConsolidadorc() != null) {
                        if (vigCuenta.getNombreConsolidadorc() != null) {
                           log.info("vigCuenta.getNombreConsolidadord(): " + vigCuenta.getNombreConsolidadord());
                           mensajeError = "Atención: Se va a cambiar el centro de costo consolidador crédito por la localizacion del trabajador. Revise por favor esta configuración.";
                           log.info(mensajeError);
                           error++;
                        }
                     }
                     vigCuenta.setConsolidadord(ccLocalizacionTrabajador);
                     vigCuenta.setConsolidadorc(ccContabilidad);

                  } else if (tipoActualizacion != 0) {
                     vigCuenta.setConsolidadorc(ccContabilidad);
                     vigCuenta.setConsolidadord(ccContabilidad);
                  }
                  if (vigCuenta.getCuentad() == null) {
                     vigCuenta.setCuentad(cuenta2505.get(0));
                  } else if (vigCuenta.getCuentad() == null) {
                     vigCuenta.setCuentad(cuenta2505.get(0));
                  }
               }
               if (vigCuenta.getNaturaleza().equals("N")) {
                  if (!vigCuenta.getNombreTipocc().contains("APLICA")) {
                     if (vigCuenta.getConsolidadorc() != null) {
                        if (vigCuenta.getNombreConsolidadorc() != null) {
                           log.info("vigCuenta.getNombreConsolidadord(): " + vigCuenta.getNombreConsolidadord());
                           mensajeError = "Atención: Se va a cambiar el centro de costo consolidador crédito por la localizacion del trabajador. Revise por favor esta configuración.";
                           log.info(mensajeError);
                           error++;
                        }
                     }
                     vigCuenta.setConsolidadorc(ccContabilidad);
                     vigCuenta.setConsolidadord(ccContabilidad);
                  }
                  if (vigCuenta.getCuentad() == null) {
                     vigCuenta.setCuentad(cuenta2505.get(0));
                     vigCuenta.setCuentac(cuenta2505.get(0));
                  } else if (vigCuenta.getCuentad() == null) {
                     vigCuenta.setCuentad(cuenta2505.get(0));
                     vigCuenta.setCuentac(cuenta2505.get(0));
                  }
               }
               log.warn("Error: " + error);
               log.info("vigCuenta.getConsolidadord(): " + vigCuenta.getConsolidadord());
               log.info("vigCuenta.getConsolidadorc(): " + vigCuenta.getConsolidadorc());
               log.info("vigCuenta.getTipocc(): " + vigCuenta.getTipocc());
               if (error > 0) {
                  RequestContext.getCurrentInstance().update("form:alertaTCC");
                  RequestContext.getCurrentInstance().execute("PF('alertaTCC').show()");
               }
               return true;
            } else {
               RequestContext.getCurrentInstance().update("form:errorTCC");
               RequestContext.getCurrentInstance().execute("PF('errorTCC').show()");
               return false;
            }
         } else {
            RequestContext.getCurrentInstance().update("form:errorTCC");
            RequestContext.getCurrentInstance().execute("PF('errorTCC').show()");
            return false;
         }
      } else {
         log.info("ControlParametrizacionContable.autocompletarTipoCC() vigCuenta.getConcepto() : " + vigCuenta.getConcepto());
         return false;
      }
   }

   public void autocompletarNuevoyDuplicadoVigenciaCuenta(VigenciasCuentas vigencia, String campo, String valor) {
      vigCuentaCrearSeleccionada = vigencia;
      cargarLOVsVCuentas();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      switch (campo) {
         case "TIPOCC":
            vigCuentaCrearSeleccionada.setNombreTipocc(auxVC_TipoCC);
            for (int i = 0; i < lovTiposCentrosCostos.size(); i++) {
               if (lovTiposCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setTipocc(lovTiposCentrosCostos.get(indiceUnicoElemento));
               autocompletarTipoCC(vigCuentaCrearSeleccionada);
            } else {
               contarRegistrosLovTipoCentroCosto();
               RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
               RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').show()");
            }
            break;
         case "CODDEBITO":
            vigCuentaCrearSeleccionada.setCodCuentad(auxVC_Debito);
            for (int i = 0; i < lovCuentas.size(); i++) {
               if (lovCuentas.get(i).getCodigo().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setCuentad(lovCuentas.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCuentaDebito();
               RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
               RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta:::nuevaDebitoVC");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta:::nuevaDesDebitoVC");
               }
            }
            break;
         case "DESDEBITO":
            vigCuentaCrearSeleccionada.setDescripcionCuentad(auxVC_DescDeb);
            for (int i = 0; i < lovCuentas.size(); i++) {
               if (lovCuentas.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setCuentad(lovCuentas.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCuentaDebito();
               RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
               RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDebitoVC");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta:::nuevaDesDebitoVC");
               }
            }
            break;
         case "CONSOLIDADORDEBITO":
            vigCuentaCrearSeleccionada.setNombreConsolidadord(auxVC_ConsDeb);
            for (int i = 0; i < lovCentrosCostos.size(); i++) {
               if (lovCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setConsolidadord(lovCentrosCostos.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCentroCostoDebito();
               RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
               RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaConsoliDebVC");
               }
            }
            break;
         case "CODCREDITO":
            vigCuentaCrearSeleccionada.setCodCuentac(auxVC_Credito);
            for (int i = 0; i < lovCuentas.size(); i++) {
               if (lovCuentas.get(i).getCodigo().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setCuentac(lovCuentas.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCuentaCredito();
               RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
               RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaCreditoVC");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDesCreditoVC");
               }
            }
            break;
         case "DESCREDITO":
            vigCuentaCrearSeleccionada.setDescripcionCuentac(auxVC_DescCre);
            for (int i = 0; i < lovCuentas.size(); i++) {
               if (lovCuentas.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setCuentac(lovCuentas.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCuentaCredito();
               RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
               RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaCreditoVC");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDesCreditoVC");
               }
            }
            break;
         case "CONSOLIDADORCREDITO":
            vigCuentaCrearSeleccionada.setNombreConsolidadorc(auxVC_ConsCre);
            for (int i = 0; i < lovCentrosCostos.size(); i++) {
               if (lovCentrosCostos.get(i).getNombre().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setConsolidadorc(lovCentrosCostos.get(indiceUnicoElemento));
            } else {
               contarRegistrosLovCentroCostoCredito();
               RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
               RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaConsoliCreVC");
               }
            }
            break;
         case "PROCESO":
            log.info("autocompletarNuevoyDuplicadoVigenciaCuenta campo = 'PROCESO'");
            vigCuentaCrearSeleccionada.setNombreProceso(auxVC_Proceso);
            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               vigCuentaCrearSeleccionada.setProceso(lovProcesos.get(indiceUnicoElemento).getSecuencia());
               vigCuentaCrearSeleccionada.setNombreProceso(lovProcesos.get(indiceUnicoElemento).getDescripcion());
            } else {
               contarRegistrosLovCentroCostoCredito();
               RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
               if (tipoActualizacion != 0) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//                  RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevoProceso");
               }
            }
            break;
         default:
            break;
      }
   }

   //////////Validaciones de los campos de las tablas de la pagina///////////////
   public boolean validarActualizarVigenciaCuenta() {
      boolean retorno = false;
      if ((vigenciaCuentaSeleccionada.getFechafinal() != null) && (vigenciaCuentaSeleccionada.getFechainicial() != null)
              && (vigenciaCuentaSeleccionada.getConsolidadorc() != null)
              && (vigenciaCuentaSeleccionada.getConsolidadord() != null)
              && (vigenciaCuentaSeleccionada.getCuentac() != null)
              && (vigenciaCuentaSeleccionada.getCuentad() != null)
              && (vigenciaCuentaSeleccionada.getTipocc() != null)) {
         retorno = true;
      }
      return retorno;
   }

   ///////////////////////////////////////////////////////////////////////////
   public void valoresBackupAutocompletarVCuenta(VigenciasCuentas vigencia, String campo) {
      vigCuentaCrearSeleccionada = vigencia;
      switch (campo) {
         case "TIPOCC":
            auxVC_TipoCC = vigCuentaCrearSeleccionada.getNombreTipocc();
            break;
         case "CODDEBITO":
            auxVC_Debito = vigCuentaCrearSeleccionada.getCodCuentad();
            break;
         case "DESDEBITO":
            auxVC_DescDeb = vigCuentaCrearSeleccionada.getDescripcionCuentad();
            break;
         case "CONSOLIDADORDEBITO":
            auxVC_ConsDeb = vigCuentaCrearSeleccionada.getNombreConsolidadord();
            break;
         case "CODCREDITO":
            auxVC_Credito = vigCuentaCrearSeleccionada.getCodCuentac();
            break;
         case "DESCREDITO":
            auxVC_DescCre = vigCuentaCrearSeleccionada.getDescripcionCuentac();
            break;
         case "CONSOLIDADORCREDITO":
            auxVC_ConsCre = vigCuentaCrearSeleccionada.getNombreConsolidadorc();
            break;
         case "PROCESO":
            log.info("valoresBackupAutocompletarGeneral campo = 'PROCESO'");
            auxVC_Proceso = vigCuentaCrearSeleccionada.getNombreProceso();
            break;
         default:
            break;
      }
   }

   ////////////Listas Valores VigenciasCuentas/////////////
   public void cargarLOVsVCuentas() {
      log.info("Entro en cargarLOVs()");
      if (lovTiposCentrosCostos == null) {
         lovTiposCentrosCostos = administrarDetalleConcepto.consultarLOVTiposCentrosCostos();
         log.info("cargo lovTiposCentrosCostos");
      }
      if (lovCuentas == null) {
         if (listasRecurrentes.getLovCuentas().isEmpty()) {
            lovCuentas = administrarDetalleConcepto.consultarLOVCuentas();
            if (lovCuentas != null) {
               listasRecurrentes.setLovCuentas(lovCuentas);
            }
         } else {
            lovCuentas = new ArrayList<Cuentas>(listasRecurrentes.getLovCuentas());
         }
         log.info("cargo lovCuentas");
      }
      if (lovCentrosCostos == null) {
         if (listasRecurrentes.getLovCentrosCostos().isEmpty()) {
            lovCentrosCostos = administrarDetalleConcepto.consultarLOVCentrosCostos();
            if (lovCentrosCostos != null) {
               listasRecurrentes.setLovCentrosCostos(lovCentrosCostos);
            }
         } else {
            lovCentrosCostos = new ArrayList<CentrosCostos>(listasRecurrentes.getLovCentrosCostos());
         }
         log.info("cargo lovCentrosCostos");
      }
      if (lovProcesos == null) {
         if (listasRecurrentes.getLovProcesos().isEmpty()) {
            lovProcesos = administrarDetalleConcepto.consultarLOVProcesos();
            if (lovProcesos != null) {
               listasRecurrentes.setLovProcesos(lovProcesos);
            }
         } else {
            lovProcesos = new ArrayList<Procesos>(listasRecurrentes.getLovProcesos());
         }
         log.info("cargo lovProcesos");
      }
   }

   public void limpiarListasModificaciones() {
      listVigenciasCuentasCrear.clear();
      listVigenciasCuentasBorrar.clear();
      listVigenciasCuentasModificar.clear();
   }

   public void cambiarIndiceConceptos() {
      vigenciaCuentaSeleccionada = null;
      //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
      if (!listVigenciasCuentasModificar.isEmpty() || !listVigenciasCuentasCrear.isEmpty() || !listVigenciasCuentasBorrar.isEmpty()) {
         RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
      }
      for (VigenciasCuentas nuevaVigenciaCuenta : listVigCParaCrear) {
         nuevaVigenciaCuenta.setConcepto(conceptoSeleccionado);
      }
      listaVigenciasCuentas = null;
      if (tipoListaVigenciaCuenta == 1) {
         RequestContext.getCurrentInstance().execute("PF('datosVigenciaCuenta').clearFilters()");
      }
      getListaVigenciasCuentas();
      RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      contarRegistrosCuentas();
   }

   public void cambiarIndiceVigenciaCuenta(VigenciasCuentas vcuenta, int celda) {
      cualCelda = celda;
      vigenciaCuentaSeleccionada = vcuenta;
      tipoActualizacion = 0;
//      vigCuentaEditar = vigenciaCuentaSeleccionada;
      ///////// Captura Objetos Para campos NotNull ///////////
      auxVC_FechaIni = vigenciaCuentaSeleccionada.getFechainicial();
      auxVC_FechaFin = vigenciaCuentaSeleccionada.getFechafinal();
      auxVC_TipoCC = vigenciaCuentaSeleccionada.getNombreTipocc();
      auxVC_DescDeb = vigenciaCuentaSeleccionada.getDescripcionCuentad();
      auxVC_Debito = vigenciaCuentaSeleccionada.getCodCuentad();
      auxVC_ConsDeb = vigenciaCuentaSeleccionada.getNombreConsolidadord();
      auxVC_DescCre = vigenciaCuentaSeleccionada.getDescripcionCuentac();
      auxVC_Credito = vigenciaCuentaSeleccionada.getCodCuentac();
      auxVC_ConsCre = vigenciaCuentaSeleccionada.getNombreConsolidadorc();
      auxVC_Proceso = vigenciaCuentaSeleccionada.getNombreProceso();
   }

   public void modificacionesFechaVigenciaCuenta(VigenciasCuentas cuenta, int c) {
      VigenciasCuentas auxiliar = null;
      auxiliar = cuenta;
      vigenciaCuentaSeleccionada = cuenta;
      if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
         boolean validacion = validarFechasRegistroVigCuenta();
         if (validacion == true) {
            cambiarIndiceVigenciaCuenta(cuenta, c);
            modificarVigenciaCuenta();
            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
         } else {
            vigenciaCuentaSeleccionada.setFechainicial(auxVC_FechaIni);
            vigenciaCuentaSeleccionada.setFechafinal(auxVC_FechaFin);
            RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
            RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
//                vigenciaCuentaSeleccionada = null;
         }
      } else {
         vigenciaCuentaSeleccionada.setFechainicial(auxVC_FechaIni);
         vigenciaCuentaSeleccionada.setFechafinal(auxVC_FechaFin);
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
//            vigenciaCuentaSeleccionada = null;
      }
   }

   public boolean validarFechasNuevoVigCuenta() {
      log.info("ControlParametrizacionContable.validarFechasNuevoVigCuenta()");
      boolean retorno = true;
      for (VigenciasCuentas nuevaVigenciaCuenta : listVigCParaCrear) {
         if (nuevaVigenciaCuenta.getAcrear()
                 && (!nuevaVigenciaCuenta.getFechainicial().after(fechaParametro)
                 || (!nuevaVigenciaCuenta.getFechainicial().before(nuevaVigenciaCuenta.getFechafinal())))) {
            retorno = false;
            break;
         }
      }
      log.info("validarFechasNuevoVigCuenta() Va a retornar: " + retorno);
      return retorno;
   }

   public boolean validarFechasRegistroVigCuenta() {
      boolean retorno = false;
      VigenciasCuentas auxiliar = vigenciaCuentaSeleccionada;
      if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
         retorno = true;
      }
      return retorno;
   }

   public void guardarYSalir() {
      guardarCambiosVC();
      salir();
   }

   public void guardarCambiosVC() {
      log.warn("guardarCambiosVC");
      log.warn("listVigenciasCuentasBorrar : " + listVigenciasCuentasBorrar);
      log.warn("listVigenciasCuentasCrear : " + listVigenciasCuentasCrear);
      log.warn("listVigenciasCuentasModificar : " + listVigenciasCuentasModificar);
      List<BigInteger> listaSecuenciasConceptos = new ArrayList<BigInteger>();
      try {
         if (!listVigenciasCuentasBorrar.isEmpty()) {
            administrarDetalleConcepto.borrarVigenciasCuentas(listVigenciasCuentasBorrar);
            for (VigenciasCuentas recVC : listVigenciasCuentasBorrar) {
               if (!listaSecuenciasConceptos.contains(recVC.getConcepto())) {
                  listaSecuenciasConceptos.add(recVC.getConcepto());
               }
            }
            listVigenciasCuentasBorrar.clear();
         }
         if (!listVigenciasCuentasCrear.isEmpty()) {
            administrarDetalleConcepto.crearVigenciasCuentas(listVigenciasCuentasCrear);
            for (VigenciasCuentas recVC : listVigenciasCuentasCrear) {
               if (!listaSecuenciasConceptos.contains(recVC.getConcepto())) {
                  listaSecuenciasConceptos.add(recVC.getConcepto());
               }
            }
            listVigenciasCuentasCrear.clear();
         }
         if (!listVigenciasCuentasModificar.isEmpty()) {
            administrarDetalleConcepto.modificarVigenciasCuentas(listVigenciasCuentasModificar);
            for (VigenciasCuentas recVC : listVigenciasCuentasModificar) {
               if (!listaSecuenciasConceptos.contains(recVC.getConcepto())) {
                  listaSecuenciasConceptos.add(recVC.getConcepto());
               }
            }
            listVigenciasCuentasModificar.clear();
         }
//         listaConceptos = null;
         k = 0;
         vigenciaCuentaSeleccionada = null;
//         cambiosVigenciaCuenta = false;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Vigencias Cuentas se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         log.info("ControlParametrizacionContable.guardarCambiosVC() listaSecuencias: " + listaSecuenciasConceptos);
         if (!listaSecuenciasConceptos.isEmpty()) {
            for (BigInteger recBI : listaSecuenciasConceptos) {
               int cantidad = administrarDetalleConcepto.contarVigenciasXConcepto(recBI);
               log.info("ControlParametrizacionContable.guardarCambiosVC() ya conto cantidad: " + cantidad);
               for (ConceptosAux2 recVC : listaConceptos) {
                  if (recVC.getSecuencia().equals(recBI)) {
                     if (cantidad > 0) {
                        recVC.setParametrizado("SI");
                        recVC.setVigenciascuentas(cantidad);
                     } else {
                        recVC.setParametrizado("NO");
                        recVC.setVigenciascuentas(cantidad);
                     }
                  }
               }
            }
         }
         RequestContext.getCurrentInstance().update("form:datosConceptos");
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
         RequestContext.getCurrentInstance().update("form:growl");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         log.warn("Error guardarCambiosVigenciaCuenta : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Cuentas y Tipos CC, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void recargarVigenciaCuentaDefault() {
      altoTabla = "140";
      FacesContext c = FacesContext.getCurrentInstance();
      checkconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:checkconcepto");
      checkconcepto.setFilterStyle("display: none; visibility: hidden;");
      codigoconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:codigoconcepto");
      codigoconcepto.setFilterStyle("display: none; visibility: hidden;");
      descripcionconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:descripcionconcepto");
      descripcionconcepto.setFilterStyle("display: none; visibility: hidden;");
      naturalezaconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:naturalezaconcepto");
      naturalezaconcepto.setFilterStyle("display: none; visibility: hidden;");
      activoconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:activoconcepto");
      activoconcepto.setFilterStyle("display: none; visibility: hidden;");
      conjuntoconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:conjuntoconcepto");
      conjuntoconcepto.setFilterStyle("display: none; visibility: hidden;");

      vigenciaCuentaFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaFechaInicial");
      vigenciaCuentaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaFechaFinal");
      vigenciaCuentaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaTipoCC = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaTipoCC");
      vigenciaCuentaTipoCC.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaDebitoCod = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaDebitoCod");
      vigenciaCuentaDebitoCod.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaDebitoDes = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaDebitoDes");
      vigenciaCuentaDebitoDes.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaCCConsolidadorD = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCConsolidadorD");
      vigenciaCuentaCCConsolidadorD.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaCreditoCod = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCreditoCod");
      vigenciaCuentaCreditoCod.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaCreditoDes = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCreditoDes");
      vigenciaCuentaCreditoDes.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaCCConsolidadorC = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCConsolidadorC");
      vigenciaCuentaCCConsolidadorC.setFilterStyle("display: none; visibility: hidden;");
      vigenciaCuentaCCProceso = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCProceso");
      vigenciaCuentaCCProceso.setFilterStyle("display: none; visibility: hidden;");

      bandera = 0;
      filtrarListVigenciasCuentasConcepto = null;
      tipoListaVigenciaCuenta = 0;
      tipoLista = 0;
   }

   public void validarIngresoNuevaVigenciaCuenta() {
      if (conceptoSeleccionado != null) {
         tipoActualizacion = 1;
         limpiarNuevoVigenciaCuenta();
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaCuenta");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaCuenta').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistroC').show()");
      }
   }

   public void limpiarNuevoVigenciaCuenta() {
      log.warn("limpiarNuevoVigenciaCuenta 1");
      for (VigenciasCuentas recVig : listVigCParaCrear) {
         if (tipoActualizacion == 1) {
            if ((recVig.getAcrear())) {
               k++;
               recVig.clear();
               recVig.setSecuencia(new BigInteger("" + k));
               recVig.setConcepto(conceptoSeleccionado);
            }
         } else {
            log.warn("limpiarNuevoVigenciaCuenta != 1");
            k++;
            recVig.clear();
            recVig.setSecuencia(new BigInteger("" + k));
            recVig.setConcepto(conceptoSeleccionado);
         }
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
      log.warn("limpiarNuevoVigenciaCuenta 2");
//      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
//      log.warn("limpiarNuevoVigenciaCuenta 3");
//      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaCuenta");
//      log.warn("limpiarNuevoVigenciaCuenta 4");
   }

   public void modificarACrear(VigenciasCuentas vc, boolean valor) {
      log.info("ControlParametrizacionContable.modificarACrear() valor: " + valor);
      vc.setAcrear(valor);
   }

   public void modificarACrear(VigenciasCuentas vc) {
      log.info("ControlParametrizacionContable.modificarACrear()");
      if (vc.getAcrear()) {
         vc.setAcrear(false);
      } else {
         vc.setAcrear(true);
      }
   }

   public boolean validarNuevosDatosVigenciaCuenta() {
      log.info("ControlParametrizacionContable.validarNuevosDatosVigenciaCuenta()");
      boolean retorno = true;
      for (VigenciasCuentas nuevaVigenciaCuenta : listVigCParaCrear) {
         if (nuevaVigenciaCuenta.getAcrear() && (nuevaVigenciaCuenta.getFechafinal() == null
                 || nuevaVigenciaCuenta.getFechainicial() == null
                 || nuevaVigenciaCuenta.getConsolidadorc() == null
                 || nuevaVigenciaCuenta.getConsolidadord() == null
                 || nuevaVigenciaCuenta.getCuentac() == null
                 || nuevaVigenciaCuenta.getCuentad() == null
                 || nuevaVigenciaCuenta.getTipocc() == null)) {
            retorno = false;
            break;
         }
      }
      log.info("validarNuevosDatosVigenciaCuenta() Va a retornar: " + retorno);
      return retorno;
   }

   public boolean validarHayNuevos() {
      int cont = 0;
      for (VigenciasCuentas nuevaVigenciaCuenta : listVigCParaCrear) {
         if (nuevaVigenciaCuenta.getAcrear()) {
            cont++;
            break;
         }
      }
      return (cont > 0);
   }

   public void actualizarTipoCentroCosto() {
      log.info("actualizarTipoCentroCosto() tipoActualizacion : " + tipoActualizacion);
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setTipocc(tipoCentroCostoSeleccionadoLOV);
         if (autocompletarTipoCC(vigenciaCuentaSeleccionada)) {
            if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
               if (listVigenciasCuentasModificar.isEmpty()) {
                  listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
               } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
                  listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
               }
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setTipocc(tipoCentroCostoSeleccionadoLOV);
         autocompletarTipoCC(vigCuentaCrearSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaTipoCCVC");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVC");
      }
      tipoCentroCostoSeleccionadoLOV = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
      RequestContext.getCurrentInstance().update("formlovTiposCC:lovTiposCC");
      RequestContext.getCurrentInstance().update("formlovTiposCC:aceptarTCC");
      context.reset("formlovTiposCC:lovTiposCC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposCC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').hide()");
   }

   public void actualizarCuentaDebito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setCuentad(cuentaSeleccionadaLOV);
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setCuentad(cuentaSeleccionadaLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDebitoVC");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDesDebitoVC");
      }
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formlovDebito:lovDebito");
      RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
      RequestContext.getCurrentInstance().update("formlovDebito:aceptarCD");
      context.reset("formlovDebito:lovDebito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovDebito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').hide()");
   }

   public void actualizarCuentas2505() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setCuentad(cuentaSeleccionadaLOV);
         vigenciaCuentaSeleccionada.setCuentac(cuentaSeleccionadaLOV);
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setCuentad(cuentaSeleccionadaLOV);
         vigCuentaCrearSeleccionada.setCuentac(cuentaSeleccionadaLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDebitoVC");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDesDebitoVC");
      }
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formlovCuentas:lovCuentas");
      RequestContext.getCurrentInstance().update("formlovCuentas:CuentasDialogo");
      RequestContext.getCurrentInstance().update("formlovCuentas:aceptarCuentas");
      context.reset("formlovCuentas:lovCuentas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCuentas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CuentasDialogo').hide()");
   }

   public void cancelarCambioCuentaDebito() {
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovDebito:lovDebito");
      RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
      RequestContext.getCurrentInstance().update("formlovDebito:aceptarCD");
      context.reset("formlovDebito:lovDebito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovDebito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').hide()");
   }

   public void cancelarCambioCuentas() {
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("formlovCuentas:lovCuentas");
      context.update("formlovCuentas:CuentasDialogo");
      context.update("formlovCuentas:aceptarCuentas");
      context.reset("formlovCuentas:lovCuentas:globalFilter");
      context.execute("PF('lovCuentas').clearFilters()");
      context.execute("PF('CuentasDialogo').hide()");
   }

   public void actualizarCuentaCredito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setCuentac(cuentaSeleccionadaLOV);
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setCuentac(cuentaSeleccionadaLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaCreditoVC");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaDesCreditoVC");
      }
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formlovCredito:lovCredito");
      RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
      RequestContext.getCurrentInstance().update("formlovCredito:aceptarCC");
      context.reset("formlovCredito:lovCredito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCredito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').hide()");
   }

   public void cancelarCambioCuentaCredito() {
      filtrarListCuentas = null;
      cuentaSeleccionadaLOV = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovCredito:lovCredito");
      RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
      RequestContext.getCurrentInstance().update("formlovCredito:aceptarCC");
      context.reset("formlovCredito:lovCredito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCredito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').hide()");
   }

   public void actualizarCentroCostoDebito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setConsolidadord(centroCostoSeleccionadoLOV);
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setConsolidadord(centroCostoSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaConsoliDebVC");
      }
      filtrarListCentrosCostos = null;
      centroCostoSeleccionadoLOV = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:lovCentroCostoD");
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:aceptarCCD");
      context.reset("formlovCentroCostoD:lovCentroCostoD:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoD').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').hide()");
   }

   public void cancelarCambioCentroCostoDebito() {
      filtrarListCentrosCostos = null;
      centroCostoSeleccionadoLOV = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:lovCentroCostoD");
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:aceptarCCD");
      context.reset("formlovCentroCostoD:lovCentroCostoD:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoD').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').hide()");
   }

   public void actualizarCentroCostoCredito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setConsolidadorc(centroCostoSeleccionadoLOV);
         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setConsolidadorc(centroCostoSeleccionadoLOV);
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevaConsoliCreVC");
      }
      filtrarListCentrosCostos = null;
      centroCostoSeleccionadoLOV = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:lovCentroCostoC");
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:aceptarCCC");
      context.reset("formlovCentroCostoC:lovCentroCostoC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').hide()");
   }

   public void cancelarCambioCentroCostoCredito() {
      filtrarListCentrosCostos = null;
      centroCostoSeleccionadoLOV = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:lovCentroCostoC");
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:aceptarCCC");
      context.reset("formlovCentroCostoC:lovCentroCostoC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').hide()");
   }

   public void actualizarProceso() {
      if (tipoActualizacion == 0) {
         vigenciaCuentaSeleccionada.setProceso(procesoSeleccionadoLOV.getSecuencia());
         vigenciaCuentaSeleccionada.setNombreProceso(procesoSeleccionadoLOV.getDescripcion());

         if (!listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            if (listVigenciasCuentasModificar.isEmpty()) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            } else if (!listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
               listVigenciasCuentasModificar.add(vigenciaCuentaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else if (tipoActualizacion != 0) {
         vigCuentaCrearSeleccionada.setProceso(procesoSeleccionadoLOV.getSecuencia());
         vigCuentaCrearSeleccionada.setNombreProceso(procesoSeleccionadoLOV.getDescripcion());
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
//         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta::nuevoProceso");
      }
      cancelarCambioProceso();
   }

   public void cancelarCambioProceso() {
      filtrarlovProcesos = null;
      procesoSeleccionadoLOV = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovProceso:lovProceso");
      RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
      RequestContext.getCurrentInstance().update("formlovProceso:aceptarPro");
      context.reset("formlovProceso:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').hide()");
   }

   public void eventoFiltrarConceptos() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      conceptoSeleccionado = null;
      contarRegistrosConceptos();
   }

   public void eventoFiltrarCuenta() {
      if (tipoListaVigenciaCuenta == 0) {
         tipoListaVigenciaCuenta = 1;
      }
      vigenciaCuentaSeleccionada = null;
      contarRegistrosCuentas();
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarCambioTipoCentroCosto() {
      tipoCentroCostoSeleccionadoLOV = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formlovTiposCC:lovTiposCC");
      RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
      RequestContext.getCurrentInstance().update("formlovTiposCC:aceptarTCC");
      context.reset("formlovTiposCC:lovTiposCC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposCC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').hide()");
   }

   public void salir() {
      log.warn("Controlador.ControlParametrizacionContable.salir()");
      limpiarListasValor();
      recargarVigenciaCuentaDefault();
      listVigenciasCuentasBorrar.clear();
      listVigenciasCuentasCrear.clear();
      listVigenciasCuentasModificar.clear();
      vigenciaCuentaSeleccionada = null;
      k = 0;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

      lovCentrosCostos = null;
      lovCuentas = null;
      lovTiposCentrosCostos = null;
      lovProcesos = null;
      navegar("atras");
   }

   public void borrarVigenciaCuenta() {
      if (vigenciaCuentaSeleccionada != null) {
         if (!listVigenciasCuentasModificar.isEmpty() && listVigenciasCuentasModificar.contains(vigenciaCuentaSeleccionada)) {
            listVigenciasCuentasModificar.remove(vigenciaCuentaSeleccionada);
            listVigenciasCuentasBorrar.add(vigenciaCuentaSeleccionada);
         } else if (!listVigenciasCuentasCrear.isEmpty() && listVigenciasCuentasCrear.contains(vigenciaCuentaSeleccionada)) {
            listVigenciasCuentasCrear.remove(vigenciaCuentaSeleccionada);
         } else {
            listVigenciasCuentasBorrar.add(vigenciaCuentaSeleccionada);
         }
         listaVigenciasCuentas.remove(vigenciaCuentaSeleccionada);
         if (tipoListaVigenciaCuenta == 1) {
            filtrarListVigenciasCuentasConcepto.remove(vigenciaCuentaSeleccionada);
         }
         contarRegistrosCuentas();
         vigenciaCuentaSeleccionada = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      } else { 
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoVigenciaCuenta() {
      log.info("ControlParametrizacionContable.agregarNuevoVigenciaCuenta()");
      if (validarHayNuevos()) {
         if (validarNuevosDatosVigenciaCuenta()) {
            if (validarFechasNuevoVigCuenta()) {
               if (bandera == 1) {
                  recargarVigenciaCuentaDefault();
               }
               int cont = 0;
               for (VigenciasCuentas nuevaVigenciaCuenta : listVigCParaCrear) {
                  if (nuevaVigenciaCuenta.getAcrear()) {
                     cont++;
//                     nuevaVigenciaCuenta.setConcepto(conceptoSeleccionado);
                     listVigenciasCuentasCrear.add(nuevaVigenciaCuenta);
                     listaVigenciasCuentas.add(nuevaVigenciaCuenta);
                     vigenciaCuentaSeleccionada = listaVigenciasCuentas.get(listaVigenciasCuentas.indexOf(nuevaVigenciaCuenta));
                  }
               }
               log.info("agregarNuevoVigenciaCuenta() cont: " + cont);
               ////------////
               //limpiarNuevoVigenciaCuenta();
               ////-----////
               RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaCuenta').hide()");
               RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
               RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaCuenta");
               contarRegistrosCuentas();
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               limpiarNuevoVigenciaCuenta();
               tipoActualizacion = 0;
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechasVC').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarVigenciaCuentaM() {
      tipoActualizacion = 2;
      log.info("duplicarVigenciaCuentaM() vigenciaCuentaSeleccionada: " + vigenciaCuentaSeleccionada);
      if (vigenciaCuentaSeleccionada != null) {
         for (VigenciasCuentas dupVigCu : listVigCParaCrear) {
            dupVigCu.clonar(vigenciaCuentaSeleccionada);
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciaCuenta");
         RequestContext.getCurrentInstance().update("formularioDialogos:datosNuevaVigCuenta");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaCuenta').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void asignarIndex(int campo) {
      cargarLOVsVCuentas();
      if (campo == 0) {
         contarRegistrosLovTipoCentroCosto();
         RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').show()");
      } else if (campo == 1) {
         contarRegistrosLovCuentaDebito();
         RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
         RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
      } else if (campo == 2) {
         contarRegistrosLovCuentaDebito();
         RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
         RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
      } else if (campo == 3) {
         contarRegistrosLovCentroCostoDebito();
         RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
         RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
      } else if (campo == 4) {
         contarRegistrosLovCuentaCredito();
         RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
         RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
      } else if (campo == 5) {
         contarRegistrosLovCuentaCredito();
         RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
         RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
      } else if (campo == 6) {
         contarRegistrosLovCentroCostoCredito();
         RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
         RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').show()");
      } else if (campo == 9) {
         log.info("asignarIndex (campo == 9)");
         contarRegistrosLovProcesos();
         RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
         RequestContext.getCurrentInstance().update("formlovProceso:lovProceso");
         RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
      }
   }

   public void asignarIndex(int campo, int tipoAct) {
      tipoActualizacion = tipoAct;
      cargarLOVsVCuentas();
      if (campo == 0) {
         contarRegistrosLovTipoCentroCosto();
         RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').show()");
      } else if (campo == 1) {
         contarRegistrosLovCuentaDebito();
         RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
         RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
      } else if (campo == 2) {
         contarRegistrosLovCuentaDebito();
         RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
         RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
      } else if (campo == 3) {
         contarRegistrosLovCentroCostoDebito();
         RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
         RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
      } else if (campo == 4) {
         contarRegistrosLovCuentaCredito();
         RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
         RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
      } else if (campo == 5) {
         contarRegistrosLovCuentaCredito();
         RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
         RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
      } else if (campo == 6) {
         contarRegistrosLovCentroCostoCredito();
         RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
         RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').show()");
      } else if (campo == 9) {
         log.info("asignarIndex (campo == 9)");
         contarRegistrosLovProcesos();
         RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
         RequestContext.getCurrentInstance().update("formlovProceso:lovProceso");
         RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         recargarVigenciaCuentaDefault();
      }
      listVigenciasCuentasBorrar.clear();
      listVigenciasCuentasCrear.clear();
      listVigenciasCuentasModificar.clear();
      vigCuentaCrearSeleccionada = null;
      vigenciaCuentaSeleccionada = null;
      conceptoSeleccionado = null;
      k = 0;
      listaVigenciasCuentas = null;
      guardado = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:datosConceptos");
      context.update("form:datosVigenciaCuenta");
   }

   public void verificarRastroVigenciaCuenta() {
      if (vigenciaCuentaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(vigenciaCuentaSeleccionada.getSecuencia(), "VIGENCIASCUENTAS");
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASCUENTAS")) {
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void editarCelda() {
      if (vigenciaCuentaSeleccionada != null) {
         vigCuentaEditar = vigenciaCuentaSeleccionada;
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            context.update("formularioDialogos:editarFechaInicialVCD");
            context.execute("PF('editarFechaInicialVCD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            context.update("formularioDialogos:editarFechaFinalVCD");
            context.execute("PF('editarFechaFinalVCD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            context.update("formularioDialogos:editarTipoCCVCD");
            context.execute("PF('editarTipoCCVCD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            context.update("formularioDialogos:editaCodDebitoVCD");
            context.execute("PF('editaCodDebitoVCD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            context.update("formularioDialogos:editaDesDebitoVCD");
            context.execute("PF('editaDesDebitoVCD').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            context.update("formularioDialogos:editaCCConsolidadorDVCD");
            context.execute("PF('editaCCConsolidadorDVCD').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            context.update("formularioDialogos:editaCodCreditoVCD");
            context.execute("PF('editaCodCreditoVCD').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            context.update("formularioDialogos:editaDesCreditoVCD");
            context.execute("PF('editaDesCreditoVCD').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            context.update("formularioDialogos:editaCCConsolidadorCVCD");
            context.execute("PF('editaCCConsolidadorCVCD').show()");
            cualCelda = -1;
         }
         vigenciaCuentaSeleccionada = null;
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void listaValoresBoton() {
      cargarLOVsVCuentas();
      if (cualCelda == 2) {
         contarRegistrosLovTipoCentroCosto();
         RequestContext.getCurrentInstance().update("formlovTiposCC:TipoCCDialogo");
         RequestContext.getCurrentInstance().execute("PF('TipoCCDialogo').show()");
         tipoActualizacion = 0;
      }
      if (cualCelda == 3) {
         contarRegistrosLovCuentaDebito();
         RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
         RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
         tipoActualizacion = 0;
      }
      if (cualCelda == 4) {
         contarRegistrosLovCuentaDebito();
         RequestContext.getCurrentInstance().update("formlovDebito:DebitoDialogo");
         RequestContext.getCurrentInstance().execute("PF('DebitoDialogo').show()");
         tipoActualizacion = 0;
      }
      if (cualCelda == 5) {
         contarRegistrosLovCentroCostoDebito();
         RequestContext.getCurrentInstance().update("formlovCentroCostoD:CentroCostoDDialogo");
         RequestContext.getCurrentInstance().execute("PF('CentroCostoDDialogo').show()");
         tipoActualizacion = 0;
      }
      if (cualCelda == 6) {
         contarRegistrosLovCuentaCredito();
         RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
         RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
         tipoActualizacion = 0;
      }
      if (cualCelda == 7) {
         contarRegistrosLovCuentaCredito();
         RequestContext.getCurrentInstance().update("formlovCredito:CreditoDialogo");
         RequestContext.getCurrentInstance().execute("PF('CreditoDialogo').show()");
         tipoActualizacion = 0;
      }
      if (cualCelda == 8) {
         contarRegistrosLovCentroCostoCredito();
         RequestContext.getCurrentInstance().update("formlovCentroCostoC:CentroCostoCDialogo");
         RequestContext.getCurrentInstance().execute("PF('CentroCostoCDialogo').show()");
         tipoActualizacion = 0;
      }
      if (cualCelda == 9) {
         contarRegistrosLovProcesos();
         RequestContext.getCurrentInstance().update("formlovProceso:ProcesosDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
         tipoActualizacion = 0;
      }
   }

   public void filtrar() {
      log.info("bandera : " + bandera);
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "120";
         checkconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:checkconcepto");
         checkconcepto.setFilterStyle("width: 75% !important;");
         codigoconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:codigoconcepto");
         codigoconcepto.setFilterStyle("width: 85% !important;");
         descripcionconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:descripcionconcepto");
         descripcionconcepto.setFilterStyle("width: 85% !important;");
         naturalezaconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:naturalezaconcepto");
         naturalezaconcepto.setFilterStyle("width: 85% !important;");
         activoconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:activoconcepto");
         activoconcepto.setFilterStyle("width: 85% !important;");
         conjuntoconcepto = (Column) c.getViewRoot().findComponent("form:datosConceptos:conjuntoconcepto");
         conjuntoconcepto.setFilterStyle("width: 75% !important;");

         vigenciaCuentaFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaFechaInicial");
         vigenciaCuentaFechaInicial.setFilterStyle("width: 85% !important;");
         vigenciaCuentaFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaFechaFinal");
         vigenciaCuentaFechaFinal.setFilterStyle("width: 85% !important;");
         vigenciaCuentaTipoCC = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaTipoCC");
         vigenciaCuentaTipoCC.setFilterStyle("width: 85% !important;");
         vigenciaCuentaDebitoCod = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaDebitoCod");
         vigenciaCuentaDebitoCod.setFilterStyle("width: 85% !important;");
         vigenciaCuentaDebitoDes = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaDebitoDes");
         vigenciaCuentaDebitoDes.setFilterStyle("width: 85% !important;");
         vigenciaCuentaCCConsolidadorD = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCConsolidadorD");
         vigenciaCuentaCCConsolidadorD.setFilterStyle("width: 85% !important;");
         vigenciaCuentaCreditoCod = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCreditoCod");
         vigenciaCuentaCreditoCod.setFilterStyle("width: 85% !important;");
         vigenciaCuentaCreditoDes = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCreditoDes");
         vigenciaCuentaCreditoDes.setFilterStyle("width: 85% !important;");
         vigenciaCuentaCCConsolidadorC = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCConsolidadorC");
         vigenciaCuentaCCConsolidadorC.setFilterStyle("width: 85% !important;");
         vigenciaCuentaCCProceso = (Column) c.getViewRoot().findComponent("form:datosVigenciaCuenta:vigenciaCuentaCCProceso");
         vigenciaCuentaCCProceso.setFilterStyle("width: 85% !important;");
         bandera = 1;
      } else if (bandera == 1) {
         recargarVigenciaCuentaDefault();
      }
      RequestContext.getCurrentInstance().execute("PF('datosVigenciaCuenta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('datosConceptos').clearFilters()");
      RequestContext.getCurrentInstance().update("form:datosVigenciaCuenta");
      RequestContext.getCurrentInstance().update("form:datosConceptos");
   }

   public String exportXML() {
      log.info("exportXML");
      if (vigenciaCuentaSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasCuentas:datosVigenciaCuentasExportar";
         nombreXML = "VigenciasCuentas_XML";
      } else if (conceptoSeleccionado != null) {
         nombreTabla = ":formExportarDatosConceptos:datosConceptosExportar";
         nombreXML = "ConceptosP_XML";
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
      return nombreTabla;
   }

   /**
    * Valida la tabla a exportar en PDF con respecto al index activo
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      log.info("validarExportPDF");
      if (vigenciaCuentaSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasCuentas:datosVigenciaCuentasExportar";
         nombreExportar = "VigenciasCuentas_PDF";
         exportPDF_Tabla();
      } else if (conceptoSeleccionado != null) {
         nombreTabla = ":formExportarDatosConceptos:datosConceptosExportar";
         nombreXML = "ConceptosP_XML";
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   /**
    * Metodo que exporta datos a PDF Vigencia Localizacion
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDF_Tabla() throws IOException {
      log.info("exportPDF_Tabla");
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(nombreTabla);
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, nombreExportar, false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Verifica que tabla exportar XLS con respecto al index activo
    *
    * @throws IOException
    */
   public void verificarExportXLS() throws IOException {
      log.info("verificarExportXLS()");
      if (vigenciaCuentaSeleccionada != null) {
         nombreTabla = ":formExportarVigenciasCuentas:datosVigenciaCuentasExportar";
         nombreExportar = "VigenciasCuentas_XLS";
         exportXLS_Tabla();
      } else if (conceptoSeleccionado != null) {
         nombreTabla = ":formExportarDatosConceptos:datosConceptosExportar";
         nombreXML = "ConceptosP_XML";
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Sueldos
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS_Tabla() throws IOException {
      log.info("exportXLS_Tabla");
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(nombreTabla);
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, nombreExportar, false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //Conteo de registros: 
   public void contarRegistrosConceptos() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosCuentas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroVCuenta");
   }

   //Conteo de Registros Listas de Valor:
   public void contarRegistrosLovTipoCentroCosto() {
      RequestContext.getCurrentInstance().update("formlovTiposCC:infoRegistroLovTipoCentroCosto");
   }

   public void contarRegistrosLovCuentaDebito() {
      RequestContext.getCurrentInstance().update("formlovDebito:infoRegistroLovCuentaDebito");
   }

   public void contarRegistrosLovCuentas() {
      RequestContext.getCurrentInstance().update("formlovCuentas:infoRegistroLovCuentas");
   }

   public void contarRegistrosLovCuentaCredito() {
      RequestContext.getCurrentInstance().update("formlovCredito:infoRegistroLovCuentaCredito");
   }

   public void contarRegistrosLovProcesos() {
      RequestContext.getCurrentInstance().update("formlovProceso:infoRegistroLovProcesos");
   }

   public void contarRegistrosLovCentroCostoDebito() {
      RequestContext.getCurrentInstance().update("formlovCentroCostoD:infoRegistroLovCentroCostoDebito");
   }

   public void contarRegistrosLovCentroCostoCredito() {
      RequestContext.getCurrentInstance().update("formlovCentroCostoC:infoRegistroLovCentroCostoCredito");
   }

   //GET - SET 
   public VigenciasCuentas getVigenciaCuentaSeleccionada() {
      return vigenciaCuentaSeleccionada;
   }

   public void setVigenciaCuentaSeleccionada(VigenciasCuentas vigenciaCuentaTablaSeleccionada) {
      this.vigenciaCuentaSeleccionada = vigenciaCuentaTablaSeleccionada;
   }

   public CentrosCostos getCentroCostoSeleccionadoLOV() {
      return centroCostoSeleccionadoLOV;
   }

   public void setCentroCostoSeleccionadoLOV(CentrosCostos centroCostoSeleccionado) {
      this.centroCostoSeleccionadoLOV = centroCostoSeleccionado;
   }

   public TiposCentrosCostos getTipoCentroCostoSeleccionadoLOV() {
      return tipoCentroCostoSeleccionadoLOV;
   }

   public void setTipoCentroCostoSeleccionadoLOV(TiposCentrosCostos tipoCentroCostoSeleccionado) {
      this.tipoCentroCostoSeleccionadoLOV = tipoCentroCostoSeleccionado;
   }

   public Cuentas getCuentaSeleccionadaLOV() {
      return cuentaSeleccionadaLOV;
   }

   public void setCuentaSeleccionadaLOV(Cuentas cuentaSeleccionada) {
      this.cuentaSeleccionadaLOV = cuentaSeleccionada;
   }

   public void cargarNombresProcesos() {
      if (listaVigenciasCuentas != null) {
         if (!listaVigenciasCuentas.isEmpty()) {
            if (lovProcesos == null) {
               lovProcesos = administrarDetalleConcepto.consultarLOVProcesos();
            }
            if (lovProcesos != null) {
               if (!lovProcesos.isEmpty()) {
                  for (int i = 0; i < listaVigenciasCuentas.size(); i++) {
                     if (listaVigenciasCuentas.get(i).getProceso() != null) {
                        for (int j = 0; j < lovProcesos.size(); j++) {
                           if (listaVigenciasCuentas.get(i).getProceso().equals(lovProcesos.get(j).getSecuencia())) {
                              listaVigenciasCuentas.get(i).setNombreProceso(lovProcesos.get(j).getDescripcion());
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public List<VigenciasCuentas> getListaVigenciasCuentas() {
      try {
         if (listaVigenciasCuentas == null) {
            if (conceptoSeleccionado != null) {
               if (conceptoSeleccionado.getSecuencia() != null) {
                  listaVigenciasCuentas = administrarDetalleConcepto.consultarListaVigenciasCuentasConcepto(conceptoSeleccionado.getSecuencia());
                  cargarNombresProcesos();
               }
            }
         }
         return listaVigenciasCuentas;
      } catch (Exception e) {
         log.warn("Error getListaVigenciasCuentas " + e.toString());
         return null;
      }
   }

   public void setListaVigenciasCuentas(List<VigenciasCuentas> t) {
      this.listaVigenciasCuentas = t;
   }

   public List<VigenciasCuentas> getFiltrarListVigenciasCuentasConcepto() {
      return filtrarListVigenciasCuentasConcepto;
   }

   public void setFiltrarListVigenciasCuentasConcepto(List<VigenciasCuentas> t) {
      this.filtrarListVigenciasCuentasConcepto = t;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public List<VigenciasCuentas> getListVigenciasCuentasModificar() {
      return listVigenciasCuentasModificar;
   }

   public void setListVigenciasCuentasModificar(List<VigenciasCuentas> setListVigenciasCuentasModificar) {
      this.listVigenciasCuentasModificar = setListVigenciasCuentasModificar;
   }

   public List<VigenciasCuentas> getListConceptosJuridicosCrear() {
      return listVigenciasCuentasCrear;
   }

   public void setListConceptosJuridicosCrear(List<VigenciasCuentas> setListConceptosJuridicosCrear) {
      this.listVigenciasCuentasCrear = setListConceptosJuridicosCrear;
   }

   public List<VigenciasCuentas> getListVigenciasCuentasBorrar() {
      return listVigenciasCuentasBorrar;
   }

   public void setListVigenciasCuentasBorrar(List<VigenciasCuentas> setListVigenciasCuentasBorrar) {
      this.listVigenciasCuentasBorrar = setListVigenciasCuentasBorrar;
   }

   public List<VigenciasCuentas> getListVigenciasCuentasCrear() {
      return listVigenciasCuentasCrear;
   }

   public void setListVigenciasCuentasCrear(List<VigenciasCuentas> listVigenciasCuentasCrear) {
      this.listVigenciasCuentasCrear = listVigenciasCuentasCrear;
   }

   public List<TiposCentrosCostos> getFiltrarListTiposCentrosCostos() {
      return filtrarListTiposCentrosCostos;
   }

   public void setFiltrarListTiposCentrosCostos(List<TiposCentrosCostos> filtrarListTiposCentrosCostos) {
      this.filtrarListTiposCentrosCostos = filtrarListTiposCentrosCostos;
   }

   public List<Cuentas> getFiltrarListCuentas() {
      return filtrarListCuentas;
   }

   public void setFiltrarListCuentas(List<Cuentas> filtrarListCuentas) {
      this.filtrarListCuentas = filtrarListCuentas;
   }

   public List<CentrosCostos> getFiltrarListCentrosCostos() {
      return filtrarListCentrosCostos;
   }

   public void setFiltrarListCentrosCostos(List<CentrosCostos> filtrarListCentrosCostos) {
      this.filtrarListCentrosCostos = filtrarListCentrosCostos;
   }

   public String getComportamientoConcepto() {
      return comportamientoConcepto;
   }

   public void setComportamientoConcepto(String comportamientoConcepto) {
      this.comportamientoConcepto = comportamientoConcepto;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public List<TiposCentrosCostos> getLovTiposCentrosCostos() {
      return lovTiposCentrosCostos;
   }

   public void setLovTiposCentrosCostos(List<TiposCentrosCostos> lovTiposCentrosCostos) {
      this.lovTiposCentrosCostos = lovTiposCentrosCostos;
   }

   public List<Cuentas> getLovCuentas() {
      return lovCuentas;
   }

   public void setLovCuentas(List<Cuentas> lovCuentas) {
      this.lovCuentas = lovCuentas;
   }

   public List<Cuentas> getLovCuentas2505() {
      if (lovCuentas2505 == null) {
         lovCuentas2505 = new ArrayList<Cuentas>();
      }
      return lovCuentas2505;
   }

   public void setLovCuentas2505(List<Cuentas> lovCuentas2505) {
      this.lovCuentas2505 = lovCuentas2505;
   }

   public List<CentrosCostos> getLovCentrosCostos() {
      return lovCentrosCostos;
   }

   public void setLovCentrosCostos(List<CentrosCostos> lovCentrosCostos) {
      this.lovCentrosCostos = lovCentrosCostos;
   }

   public List<Procesos> getLovProcesos() {
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> lovProcesos) {
      this.lovProcesos = lovProcesos;
   }

   public List<Procesos> getFiltrarlovProcesos() {
      return filtrarlovProcesos;
   }

   public void setFiltrarlovProcesos(List<Procesos> filtrarlovProcesos) {
      this.filtrarlovProcesos = filtrarlovProcesos;
   }

   public Procesos getProcesoSeleccionadoLOV() {
      return procesoSeleccionadoLOV;
   }

   public void setProcesoSeleccionadoLOV(Procesos procesoSeleccionadoLOV) {
      this.procesoSeleccionadoLOV = procesoSeleccionadoLOV;
   }

   public void setInfoRegistroLovTipoCentroCosto(String infoRegistroLovTipoCentroCosto) {
      this.infoRegistroLovTipoCentroCosto = infoRegistroLovTipoCentroCosto;
   }

   public void setInfoRegistroLovCuentaDebito(String infoRegistroLovCuentaDebito) {
      this.infoRegistroLovCuentaDebito = infoRegistroLovCuentaDebito;
   }

   public void setInfoRegistroLovCuentaCredito(String infoRegistroLovCuentaCredito) {
      this.infoRegistroLovCuentaCredito = infoRegistroLovCuentaCredito;
   }

   public void setInfoRegistroLovCentroCostoDebito(String infoRegistroLovCentroCostoDebito) {
      this.infoRegistroLovCentroCostoDebito = infoRegistroLovCentroCostoDebito;
   }

   public void setInfoRegistroLovCentroCostoCredito(String infoRegistroLovCentroCostoCredito) {
      this.infoRegistroLovCentroCostoCredito = infoRegistroLovCentroCostoCredito;
   }

   public void setInfoRegistroLovProcesos(String infoRegistroLovProcesos) {
      this.infoRegistroLovProcesos = infoRegistroLovProcesos;
   }

   public void setInfoRegistroVCuenta(String infoRegistroVCuenta) {
      this.infoRegistroVCuenta = infoRegistroVCuenta;
   }

   public void setInfoRegistroConceptos(String infoRegistroConceptos) {
      this.infoRegistroConceptos = infoRegistroConceptos;
   }

   public String getInfoRegistroVCuenta() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaCuenta");
      infoRegistroVCuenta = String.valueOf(tabla.getRowCount());
      return infoRegistroVCuenta;
   }

   public String getInfoRegistroConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosConceptos");
      infoRegistroConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptos;
   }

   public String getInfoRegistroLovTipoCentroCosto() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovTiposCC:lovTiposCC");
      infoRegistroLovTipoCentroCosto = String.valueOf(tabla.getRowCount());
      return infoRegistroLovTipoCentroCosto;
   }

   public String getInfoRegistroLovCuentaDebito() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovDebito:lovDebito");
      infoRegistroLovCuentaDebito = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCuentaDebito;
   }

   public String getInfoRegistroLovCuentas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovCuentas:lovCuentas");
      infoRegistroLovCuentas = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCuentas;
   }

   public String getInfoRegistroLovCuentaCredito() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovCredito:lovCredito");
      infoRegistroLovCuentaCredito = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCuentaCredito;
   }

   public String getInfoRegistroLovCentroCostoDebito() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovCentroCostoD:lovCentroCostoD");
      infoRegistroLovCentroCostoDebito = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCentroCostoDebito;
   }

   public String getInfoRegistroLovCentroCostoCredito() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovCentroCostoC:lovCentroCostoC");
      infoRegistroLovCentroCostoCredito = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCentroCostoCredito;
   }

   public String getInfoRegistroLovProcesos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formlovProceso:lovProceso");
      infoRegistroLovProcesos = String.valueOf(tabla.getRowCount());
      return infoRegistroLovProcesos;
   }

   public String getMensajeError() {
      return mensajeError;
   }

   public List<VigenciasCuentas> getListVigCParaCrear() {
      return listVigCParaCrear;
   }

   public void setListVigCParaCrear(List<VigenciasCuentas> listVigCParaCrear) {
      this.listVigCParaCrear = listVigCParaCrear;
   }

   public VigenciasCuentas getVigCuentaCrearSeleccionada() {
      return vigCuentaCrearSeleccionada;
   }

   public void setVigCuentaCrearSeleccionada(VigenciasCuentas vigCuentaCrearSeleccionada) {
      this.vigCuentaCrearSeleccionada = vigCuentaCrearSeleccionada;
   }

   public ConceptosAux2 getConceptoSeleccionado() {
      return conceptoSeleccionado;
   }

   public void setConceptoSeleccionado(ConceptosAux2 conceptoSeleccionado) {
      this.conceptoSeleccionado = conceptoSeleccionado;
   }

   public List<ConceptosAux2> getListaConceptos() {
      if (listaConceptos == null) {
         listaConceptos = administrarDetalleConcepto.consultarConceptosAux2();
      }
      return listaConceptos;
   }

   public void setListaConceptos(List<ConceptosAux2> listaConceptos) {
      this.listaConceptos = listaConceptos;
   }

   public List<ConceptosAux2> getFiltrarlistaConceptos() {
      return filtrarlistaConceptos;
   }

   public void setFiltrarlistaConceptos(List<ConceptosAux2> filtrarlistaConceptos) {
      this.filtrarlistaConceptos = filtrarlistaConceptos;
   }

   public String getNombreXML() {
      return nombreXML;
   }

   public void setNombreXML(String nombreXML) {
      this.nombreXML = nombreXML;
   }

   public VigenciasCuentas getVigCuentaEditar() {
      return vigCuentaEditar;
   }

   public void setVigCuentaEditar(VigenciasCuentas vigCuentaEditar) {
      this.vigCuentaEditar = vigCuentaEditar;
   }

}
