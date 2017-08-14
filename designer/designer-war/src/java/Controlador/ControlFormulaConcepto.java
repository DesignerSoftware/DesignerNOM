package Controlador;

import Entidades.Conceptos;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaConceptoInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlFormulaConcepto implements Serializable {

   private static Logger log = Logger.getLogger(ControlFormulaConcepto.class);

   @EJB
   AdministrarFormulaConceptoInterface administrarFormulaConcepto;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //////////////Formulas//////////////////
   private Formulas formulaActual;
   ///////////FormulasConceptos////////////
   private List<FormulasConceptos> listFormulasConceptos;
   private List<FormulasConceptos> filtrarListFormulasConceptos;
   private FormulasConceptos formulaConceptoSeleccionada;
   ///////////FormulasConceptos/////////////
   private int bandera;
   private List<FormulasConceptos> listFormulasConceptosModificar;
   private FormulasConceptos nuevaFormulaConcepto;
   private List<FormulasConceptos> listFormulasConceptosCrear;
   private List<FormulasConceptos> listFormulasConceptosBorrar;
   private FormulasConceptos editarFormulaConcepto;
   private int cualCelda, tipoLista;
   private FormulasConceptos duplicarFormulaConcepto;
   private boolean cambiosFormulasConceptos;
   private String concepto, empresa, orden, codigoConcepto, nitEmpresa;
   private Date fechaIni, fechaFin;
   private Column formulaFechaInicial, formulaFechaFinal, formulaCodigo, formulaDescripcion, formulaOrden, formulaEmpresa, formulaNIT;
   private boolean permitirIndex;
   ////////////Listas Valores FormulasConceptos/////////////
   private List<FormulasConceptos> lovFormulasConceptosOrden;
   private List<FormulasConceptos> filtrarLovFormulasConceptosOrden;
   private FormulasConceptos formulaCOrdenLovSeleccionado;
   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtrarLovConceptos;
   private Conceptos conceptoLovSeleccionada;
   //////////////Otros////////////////Otros////////////////////
   private boolean aceptar;
   private boolean guardado;
   private boolean activarLOV;
   private BigInteger l;
   private int k;
   private String nombreXML, nombreExportar;
   private String nombreTabla;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private String nombreTablaRastro;
   private int tipoActualizacion;
   private Date fechaParametro;
   //
   private String infoRegistro;
   private String altoTabla;
   private String infoRegistroLovConcepto, infoRegistroLovFCOrden;
   //
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlFormulaConcepto() {
      altoTabla = "300";
      formulaActual = new Formulas();
      listFormulasConceptos = null;
      fechaParametro = new Date(1, 1, 0);
      permitirIndex = true;

      lovFormulasConceptosOrden = null;
      lovConceptos = null;

      formulaCOrdenLovSeleccionado = new FormulasConceptos();
      conceptoLovSeleccionada = new Conceptos();

      nombreExportar = "";
      nombreTablaRastro = "";
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";

      formulaConceptoSeleccionada = null;
      aceptar = true;
      k = 0;

      listFormulasConceptosBorrar = new ArrayList<FormulasConceptos>();
      listFormulasConceptosCrear = new ArrayList<FormulasConceptos>();
      listFormulasConceptosModificar = new ArrayList<FormulasConceptos>();
      editarFormulaConcepto = new FormulasConceptos();
      cualCelda = -1;
      tipoLista = 0;
      guardado = true;
      nuevaFormulaConcepto = new FormulasConceptos();
      bandera = 0;
      nombreTabla = ":formExportarFormula:datosFormulaConceptoExportar";
      nombreXML = "FormulaConcepto_XML";
      duplicarFormulaConcepto = new FormulasConceptos();
      cambiosFormulasConceptos = false;

      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovConceptos = null;
      lovFormulasConceptosOrden = null;
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }

   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarFormulaConcepto.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirFormula(Formulas formula, String pagina) {
      log.info("Controlador.ControlFormulaConcepto.recibirFormula() formula: " + formula + " , y pagina: " + pagina);
      paginaAnterior = pagina;
      formulaActual = formula;
      listFormulasConceptos = null;
      if (formulaActual != null) {
         getListFormulasConceptos();
      }
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "formulaconcepto";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
      } else {
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
         fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
         //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParaEnviar.put("paginaAnterior", pagActual);
         //mas Parametros
         //         if (pag.equals("rastrotabla")) {
         //           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
      }
      limpiarListasValor();
   }

   public String retornarPagina() {
      return paginaAnterior;
   }

   public void modificarFormulaConcepto(FormulasConceptos fc) {
      formulaConceptoSeleccionada = fc;
      if (!listFormulasConceptosCrear.contains(formulaConceptoSeleccionada)) {
         if (listFormulasConceptosModificar.isEmpty()) {
            listFormulasConceptosModificar.add(formulaConceptoSeleccionada);
         } else if (!listFormulasConceptosModificar.contains(formulaConceptoSeleccionada)) {
            listFormulasConceptosModificar.add(formulaConceptoSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      cambiosFormulasConceptos = true;
   }

   public void modificarFormulaConcepto(FormulasConceptos fc, String confirmarCambio, String valorConfirmar) {
      formulaConceptoSeleccionada = fc;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("CODIGO")) {
         formulaConceptoSeleccionada.setCodigoConcepto(new BigInteger(codigoConcepto));
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getCodigoSTR().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaConceptoSeleccionada.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
            formulaConceptoSeleccionada.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
            formulaConceptoSeleccionada.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
            formulaConceptoSeleccionada.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
            formulaConceptoSeleccionada.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
         } else {
            permitirIndex = false;
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         formulaConceptoSeleccionada.setNombreConcepto(concepto);
         formulaConceptoSeleccionada.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
         formulaConceptoSeleccionada.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
         formulaConceptoSeleccionada.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
         formulaConceptoSeleccionada.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaConceptoSeleccionada.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
         } else {
            permitirIndex = false;
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("ORDEN")) {
         formulaConceptoSeleccionada.setStrOrden(orden);
         for (int i = 0; i < lovFormulasConceptosOrden.size(); i++) {
            if (lovFormulasConceptosOrden.get(i).getStrOrden().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaConceptoSeleccionada.setStrOrden(lovFormulasConceptosOrden.get(indiceUnicoElemento).getStrOrden());
         } else {
            permitirIndex = false;
            contarRegistrosLovFCOrden();
            RequestContext.getCurrentInstance().update("form:OrdenDialogo");
            RequestContext.getCurrentInstance().execute("PF('OrdenDialogo').show()");
            tipoActualizacion = 0;
         }

      }
      if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
         formulaConceptoSeleccionada.setNombreEmpresa(empresa);
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getNombreEmpresa().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaConceptoSeleccionada.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
            formulaConceptoSeleccionada.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
            formulaConceptoSeleccionada.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
            formulaConceptoSeleccionada.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
            formulaConceptoSeleccionada.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
         } else {
            permitirIndex = false;
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("NIT")) {
         formulaConceptoSeleccionada.setNitEmpresa(Long.parseLong(nitEmpresa));
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (("" + lovConceptos.get(i).getNitEmpresa()).startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaConceptoSeleccionada.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
            formulaConceptoSeleccionada.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
            formulaConceptoSeleccionada.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
            formulaConceptoSeleccionada.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
            formulaConceptoSeleccionada.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
         } else {
            permitirIndex = false;
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listFormulasConceptosCrear.contains(formulaConceptoSeleccionada)) {
            if (listFormulasConceptosModificar.isEmpty()) {
               listFormulasConceptosModificar.add(formulaConceptoSeleccionada);
            } else if (!listFormulasConceptosModificar.contains(formulaConceptoSeleccionada)) {
               listFormulasConceptosModificar.add(formulaConceptoSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         cambiosFormulasConceptos = true;
      }
      RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
   }

   ///////////////////////////////////////////////////////////////////////////
   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("CODIGO")) {
         try {
            if (tipoNuevo == 1) {
               codigoConcepto = nuevaFormulaConcepto.getCodigoConcepto().toString();
            } else if (tipoNuevo == 2) {
               codigoConcepto = duplicarFormulaConcepto.getCodigoConcepto().toString();
            }
         } catch (Exception e) {
            log.info("Asignando codigoConcepto : null como ''''");
            codigoConcepto = "";
         }
      } else if (Campo.equals("CONCEPTO")) {
         if (tipoNuevo == 1) {
            concepto = nuevaFormulaConcepto.getNombreConcepto();
         } else if (tipoNuevo == 2) {
            concepto = duplicarFormulaConcepto.getNombreConcepto();
         }
      } else if (Campo.equals("ORDEN")) {
         if (tipoNuevo == 1) {
            orden = nuevaFormulaConcepto.getStrOrden();
         } else if (tipoNuevo == 2) {
            orden = duplicarFormulaConcepto.getStrOrden();
         }
      } else if (Campo.equals("EMPRESA")) {
         if (tipoNuevo == 1) {
            empresa = nuevaFormulaConcepto.getNombreEmpresa();
         } else if (tipoNuevo == 2) {
            empresa = duplicarFormulaConcepto.getNombreEmpresa();
         }
      } else if (Campo.equals("NIT")) {
         if (tipoNuevo == 1) {
            nitEmpresa = "" + nuevaFormulaConcepto.getNitEmpresa();
         } else if (tipoNuevo == 2) {
            nitEmpresa = "" + duplicarFormulaConcepto.getNitEmpresa();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoFormulaConcepto(String campo, String valor, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (campo.equalsIgnoreCase("CODIGO")) {
         if (tipoNuevo == 1) {
            nuevaFormulaConcepto.setCodigoConcepto(new BigInteger(codigoConcepto));
         } else if (tipoNuevo == 2) {
            duplicarFormulaConcepto.setCodigoConcepto(new BigInteger(codigoConcepto));
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getCodigoSTR().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               nuevaFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               nuevaFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               nuevaFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
               nuevaFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNIT");
            } else if (tipoNuevo == 2) {
               duplicarFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               duplicarFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               duplicarFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               duplicarFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
               duplicarFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNIT");
            }
         } else {
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
            }
         }
      } else if (campo.equalsIgnoreCase("CONCEPTO")) {
         if (tipoNuevo == 1) {
            nuevaFormulaConcepto.setNombreConcepto(concepto);
         } else if (tipoNuevo == 2) {
            duplicarFormulaConcepto.setNombreConcepto(concepto);
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               nuevaFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               nuevaFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               nuevaFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
               nuevaFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNIT");
            } else if (tipoNuevo == 2) {
               duplicarFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               duplicarFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               duplicarFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               duplicarFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
               duplicarFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNIT");
            }
         } else {
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaConcepto");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
            }
         }
      } else if (campo.equalsIgnoreCase("ORDEN")) {
         if (tipoNuevo == 1) {
            nuevaFormulaConcepto.setStrOrden(orden);
         } else if (tipoNuevo == 2) {
            duplicarFormulaConcepto.setStrOrden(orden);
         }
         for (int i = 0; i < lovFormulasConceptosOrden.size(); i++) {
            if (lovFormulasConceptosOrden.get(i).getStrOrden().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulaConcepto.setStrOrden(lovFormulasConceptosOrden.get(indiceUnicoElemento).getStrOrden());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaOrden");
            } else if (tipoNuevo == 2) {
               duplicarFormulaConcepto.setStrOrden(lovFormulasConceptosOrden.get(indiceUnicoElemento).getStrOrden());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOrden");
            }
         } else {
            contarRegistrosLovFCOrden();
            RequestContext.getCurrentInstance().update("form:OrdenDialogo");
            RequestContext.getCurrentInstance().execute("PF('OrdenDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaOrden");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOrden");
            }
         }
      } else if (campo.equalsIgnoreCase("EMPRESA")) {
         if (tipoNuevo == 1) {
            nuevaFormulaConcepto.setNombreEmpresa(empresa);
         } else if (tipoNuevo == 2) {
            duplicarFormulaConcepto.setNombreEmpresa(empresa);
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getNombreEmpresa().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               nuevaFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               nuevaFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               nuevaFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
               nuevaFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNIT");
            } else if (tipoNuevo == 2) {
               duplicarFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               duplicarFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               duplicarFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               duplicarFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
               duplicarFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNIT");
            }
         } else {
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
            }
         }
      } else if (campo.equalsIgnoreCase("NIT")) {
         if (tipoNuevo == 1) {
            nuevaFormulaConcepto.setNitEmpresa(Long.parseLong(nitEmpresa));
         } else if (tipoNuevo == 2) {
            duplicarFormulaConcepto.setNitEmpresa(Long.parseLong(nitEmpresa));
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (("" + lovConceptos.get(i).getNitEmpresa()).startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               nuevaFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               nuevaFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               nuevaFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
               nuevaFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNIT");
            } else if (tipoNuevo == 2) {
               duplicarFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               duplicarFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               duplicarFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               duplicarFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getNombreEmpresa());
               duplicarFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getNitEmpresa());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNIT");
            }
         } else {
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNIT");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNIT");
            }
         }
      }
   }

   public void cambiarIndiceFormulaConcepto(FormulasConceptos fc, int celda) {
      formulaConceptoSeleccionada = fc;
      if (permitirIndex == true) {
         cualCelda = celda;
         if (cualCelda > 1) {
            activarBotonLOV();
         } else {
            anularBotonLOV();
         }
         fechaIni = formulaConceptoSeleccionada.getFechainicial();
         fechaFin = formulaConceptoSeleccionada.getFechafinal();
         concepto = formulaConceptoSeleccionada.getNombreConcepto();
         orden = formulaConceptoSeleccionada.getStrOrden();
         empresa = formulaConceptoSeleccionada.getNombreEmpresa();
         nitEmpresa = "" + formulaConceptoSeleccionada.getNitEmpresa();
         if (formulaConceptoSeleccionada.getCodigoConcepto() != null) {
            codigoConcepto = formulaConceptoSeleccionada.getCodigoConcepto().toString();
         }
      }
   }

   public void modificacionesFechaFormula(FormulasConceptos fc, int c) {
      formulaConceptoSeleccionada = fc;
      if ((formulaConceptoSeleccionada.getFechainicial() != null) && (formulaConceptoSeleccionada.getFechafinal() != null)) {
         boolean validacion = validarFechasRegistroFormulas(formulaConceptoSeleccionada);
         if (validacion == true) {
            cambiarIndiceFormulaConcepto(fc, c);
            modificarFormulaConcepto(fc);
            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         } else {
            log.warn("Error de fechas de ingreso");
            formulaConceptoSeleccionada.setFechainicial(fechaIni);
            formulaConceptoSeleccionada.setFechafinal(fechaFin);
            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
            RequestContext.getCurrentInstance().execute("PF('errorFechasFC').show()");
         }
      } else {
         formulaConceptoSeleccionada.setFechainicial(fechaIni);
         formulaConceptoSeleccionada.setFechafinal(fechaFin);
         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void guardadoYSalir() {
      guardadoGeneral();
      navegar("atras");
   }

   //GUARDAR
   /**
    */
   public void guardadoGeneral() {
      log.info("Entro en guardadoGeneral()");
      if (cambiosFormulasConceptos == true) {
         try {
            log.info("listFormulasConceptosBorrar.size() : " + listFormulasConceptosBorrar.size());
            log.info("listFormulasConceptosCrear.size() : " + listFormulasConceptosCrear.size());
            log.info("listFormulasConceptosModificar.size() : " + listFormulasConceptosModificar.size());
            if (!listFormulasConceptosBorrar.isEmpty()) {
               administrarFormulaConcepto.borrarFormulasConceptos(listFormulasConceptosBorrar);
               listFormulasConceptosBorrar.clear();
            }
            if (!listFormulasConceptosCrear.isEmpty()) {
               administrarFormulaConcepto.crearFormulasConceptos(listFormulasConceptosCrear);
               listFormulasConceptosCrear.clear();
            }
            if (!listFormulasConceptosModificar.isEmpty()) {
               administrarFormulaConcepto.editarFormulasConceptos(listFormulasConceptosModificar);
               listFormulasConceptosModificar.clear();
            }
            formulaConceptoSeleccionada = null;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            k = 0;
            cambiosFormulasConceptos = false;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } catch (Exception e) {
            log.warn("Error guardarCambiosFormula  : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      }
   }

   //CANCELAR MODIFICACIONES
   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacion() {
      restablecerTabla();
      listFormulasConceptosBorrar.clear();
      listFormulasConceptosCrear.clear();
      listFormulasConceptosModificar.clear();
      formulaConceptoSeleccionada = null;
      k = 0;
      listFormulasConceptos = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      cambiosFormulasConceptos = false;
      permitirIndex = true;
      getListFormulasConceptos();
      contarRegistros();
      anularBotonLOV();
      RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
   }

   public void listaValoresBoton() {
      if (formulaConceptoSeleccionada != null) {
         if (cualCelda == 2) {
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 3) {
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 4) {
            contarRegistrosLovFCOrden();
            RequestContext.getCurrentInstance().update("form:OrdenDialogo");
            RequestContext.getCurrentInstance().execute("PF('OrdenDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 5) {
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 6) {
            contarRegistrosLovConceptos();
            RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndex(FormulasConceptos fc, int dlg, int LND) {
      formulaConceptoSeleccionada = fc;
      tipoActualizacion = LND;
      activarBotonLOV();
      if (dlg == 0) {
         contarRegistrosLovConceptos();
         RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
      } else if (dlg == 1) {
         contarRegistrosLovFCOrden();
         RequestContext.getCurrentInstance().update("form:OrdenDialogo");
         RequestContext.getCurrentInstance().execute("PF('OrdenDialogo').show()");
      }
   }

   public void asignarIndex(int dlg, int LND) {
      tipoActualizacion = LND;
      anularBotonLOV();
      if (dlg == 0) {
         contarRegistrosLovConceptos();
         RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').show()");
      } else if (dlg == 1) {
         contarRegistrosLovFCOrden();
         RequestContext.getCurrentInstance().update("form:OrdenDialogo");
         RequestContext.getCurrentInstance().execute("PF('OrdenDialogo').show()");
      }
   }

   public void editarCelda() {
      if (formulaConceptoSeleccionada != null) {
         editarFormulaConcepto = formulaConceptoSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialFD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialFD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalFD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalFD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoFD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoFD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoFD");
            RequestContext.getCurrentInstance().execute("PF('editarConceptoFD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarOrdenFD");
            RequestContext.getCurrentInstance().execute("PF('editarOrdenFD').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaFD");
            RequestContext.getCurrentInstance().execute("PF('editarEmpresaFD').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNitFD");
            RequestContext.getCurrentInstance().execute("PF('editarNitFD').show()");
            cualCelda = -1;
         }
      }
   }

   public void ingresoNuevoRegistro() {
      validarIngresoNuevaFormula();
   }

   public void validarIngresoNuevaFormula() {
      limpiarNuevoFormula();
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaF");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFormula");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormula').show()");
   }

   public void validarDuplicadoRegistro() {
      if (formulaConceptoSeleccionada != null) {
         duplicarFormulaM();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void validarBorradoRegistro() {
      if (formulaConceptoSeleccionada != null) {
         borrarFormula();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public boolean validarNuevosDatosFormula(FormulasConceptos formulaConcepto) {
      boolean retorno = false;
      if ((formulaConcepto.getConcepto() != null)
              && (formulaConcepto.getFechainicial() != null)
              && (formulaConcepto.getFechafinal() != null)
              && (!formulaConcepto.getStrOrden().isEmpty())) {
         return true;
      }
      return retorno;
   }

   public boolean hayTraslaposFechas(Date fecha1Ini, Date fecha1Fin, Date fecha2Ini, Date fecha2Fin) {
      boolean hayTraslapos;
      if ((fecha1Fin.after(fecha2Fin) && fecha1Ini.before(fecha2Fin))
              || (fecha1Ini.before(fecha2Ini) && fecha1Fin.after(fecha2Ini))
              || fecha1Fin.equals(fecha2Fin)
              || fecha1Ini.equals(fecha2Ini)
              || fecha1Ini.equals(fecha2Fin)
              || fecha1Fin.equals(fecha2Ini)) {
         hayTraslapos = true;
      } else {
         hayTraslapos = false;
      }
      return hayTraslapos;
   }

   public boolean validarNuevoTraslapes(FormulasConceptos formulaCon) {
      boolean continuar = true;
      for (int i = 0; i < listFormulasConceptos.size(); i++) {
         if (listFormulasConceptos.get(i).getConcepto().equals(formulaCon.getConcepto())) {
            if (hayTraslaposFechas(listFormulasConceptos.get(i).getFechainicial(), listFormulasConceptos.get(i).getFechafinal(),
                    formulaCon.getFechainicial(), formulaCon.getFechafinal())) {
               continuar = false;
            }
         }
      }
      return continuar;
   }

   public boolean validarFechasRegistroFormulas(FormulasConceptos formulaCon) {
      boolean retorno = false;
      if (formulaCon.getFechainicial().after(fechaParametro) && (formulaCon.getFechainicial().before(formulaCon.getFechafinal()))) {
         retorno = true;
      }
      return retorno;
   }

   public void agregarNuevoFormula() {
      if (validarNuevosDatosFormula(nuevaFormulaConcepto)) {
         if (validarFechasRegistroFormulas(nuevaFormulaConcepto)) {
            if (validarNuevoTraslapes(nuevaFormulaConcepto)) {
               restablecerTabla();
               k++;
               BigInteger var = BigInteger.valueOf(k);
               nuevaFormulaConcepto.setSecuencia(var);
               nuevaFormulaConcepto.setTipo("C");
               nuevaFormulaConcepto.setFormula(formulaActual.getSecuencia());
               nuevaFormulaConcepto.setNombreFormula(formulaActual.getNombrelargo());
               listFormulasConceptosCrear.add(nuevaFormulaConcepto);
               listFormulasConceptos.add(nuevaFormulaConcepto);
               formulaConceptoSeleccionada = listFormulasConceptos.get(listFormulasConceptos.indexOf(nuevaFormulaConcepto));
               ////------////
               nuevaFormulaConcepto = new FormulasConceptos();
               ////-----////
               RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormula').hide()");
               RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
               RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFormula");
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               contarRegistros();
               cambiosFormulasConceptos = true;
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechastraslapos').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasFC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarNuevoFormula() {
      nuevaFormulaConcepto = new FormulasConceptos();
   }

   public void duplicarFormulaM() {
      if (formulaConceptoSeleccionada != null) {
         duplicarFormulaConcepto = new FormulasConceptos();
         duplicarFormulaConcepto.setConcepto(formulaConceptoSeleccionada.getConcepto());
         duplicarFormulaConcepto.setNombreConcepto(formulaConceptoSeleccionada.getNombreConcepto());
         duplicarFormulaConcepto.setCodigoConcepto(formulaConceptoSeleccionada.getCodigoConcepto());
         duplicarFormulaConcepto.setNombreEmpresa(formulaConceptoSeleccionada.getNombreEmpresa());
         duplicarFormulaConcepto.setNitEmpresa(formulaConceptoSeleccionada.getNitEmpresa());
         duplicarFormulaConcepto.setFechafinal(formulaConceptoSeleccionada.getFechafinal());
         duplicarFormulaConcepto.setFechainicial(formulaConceptoSeleccionada.getFechainicial());
         duplicarFormulaConcepto.setStrOrden(formulaConceptoSeleccionada.getStrOrden());
         duplicarFormulaConcepto.setTipo(formulaConceptoSeleccionada.getTipo());

         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroFormula");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormula').show()");
      }
   }

   public void confirmarDuplicarFormula() {
      boolean resp = validarNuevosDatosFormula(duplicarFormulaConcepto);
      if (resp == true) {
         boolean validacion = validarFechasRegistroFormulas(duplicarFormulaConcepto);
         if (validacion == true) {
            if (validarNuevoTraslapes(duplicarFormulaConcepto)) {
               restablecerTabla();
               k++;
               BigInteger var = BigInteger.valueOf(k);
               duplicarFormulaConcepto.setSecuencia(var);
               duplicarFormulaConcepto.setFormula(formulaActual.getSecuencia());
               duplicarFormulaConcepto.setNombreFormula(formulaActual.getNombrelargo());
               duplicarFormulaConcepto.setTipo("C");
               listFormulasConceptosCrear.add(duplicarFormulaConcepto);
               listFormulasConceptos.add(duplicarFormulaConcepto);
               formulaConceptoSeleccionada = listFormulasConceptos.get(listFormulasConceptos.indexOf(duplicarFormulaConcepto));
               duplicarFormulaConcepto = new FormulasConceptos();
               RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormula').hide()");
               cambiosFormulasConceptos = true;
               contarRegistros();
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechastraslapos').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasFC').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarDuplicarFormula() {
      duplicarFormulaConcepto = new FormulasConceptos();
   }

   ///////////////////////////////////////////////////////////////
   public void borrarFormula() {

      if (formulaConceptoSeleccionada != null) {
         if (!listFormulasConceptosModificar.isEmpty() && listFormulasConceptosModificar.contains(formulaConceptoSeleccionada)) {
            int modIndex = listFormulasConceptosModificar.indexOf(formulaConceptoSeleccionada);
            listFormulasConceptosModificar.remove(modIndex);
            listFormulasConceptosBorrar.add(formulaConceptoSeleccionada);
         } else if (!listFormulasConceptosCrear.isEmpty() && listFormulasConceptosCrear.contains(formulaConceptoSeleccionada)) {
            int crearIndex = listFormulasConceptosCrear.indexOf(formulaConceptoSeleccionada);
            listFormulasConceptosCrear.remove(crearIndex);
         } else {
            listFormulasConceptosBorrar.add(formulaConceptoSeleccionada);
         }
         listFormulasConceptos.remove(formulaConceptoSeleccionada);
         if (tipoLista == 1) {
            filtrarListFormulasConceptos.remove(formulaConceptoSeleccionada);
         }
         formulaConceptoSeleccionada = null;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         cambiosFormulasConceptos = true;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   /**
    * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      filtradoFormula();
   }

   /**
    */
   public void filtradoFormula() {
      if (bandera == 0) {
         altoTabla = "280";
         formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaInicial");
         formulaFechaInicial.setFilterStyle("width: 85% !important;");
         formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaFinal");
         formulaFechaFinal.setFilterStyle("width: 85% !important;");
         formulaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaCodigo");
         formulaCodigo.setFilterStyle("width: 85% !important");
         formulaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaDescripcion");
         formulaDescripcion.setFilterStyle("width: 85% !important");
         formulaOrden = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaOrden");
         formulaOrden.setFilterStyle("width: 85% !important");
         formulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaEmpresa");
         formulaEmpresa.setFilterStyle("width: 85% !important");
         formulaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaNIT");
         formulaNIT.setFilterStyle("width: 85% !important");

         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         bandera = 1;
      } else {
         restablecerTabla();
      }
   }

   public void restablecerTabla() {
      if (bandera == 1) {
         altoTabla = "300";
         formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaInicial");
         formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaFinal");
         formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         formulaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaCodigo");
         formulaCodigo.setFilterStyle("display: none; visibility: hidden;");
         formulaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaDescripcion");
         formulaDescripcion.setFilterStyle("display: none; visibility: hidden;");
         formulaOrden = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaOrden");
         formulaOrden.setFilterStyle("display: none; visibility: hidden;");
         formulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaEmpresa");
         formulaEmpresa.setFilterStyle("display: none; visibility: hidden;");
         formulaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaNIT");
         formulaNIT.setFilterStyle("display: none; visibility: hidden;");
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         bandera = 0;
         filtrarListFormulasConceptos = null;
         tipoLista = 0;
      }
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      restablecerTabla();
      listFormulasConceptosBorrar.clear();
      listFormulasConceptosCrear.clear();
      listFormulasConceptosModificar.clear();
      formulaConceptoSeleccionada = null;
      formulaActual = new Formulas();
      k = 0;
      listFormulasConceptos = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      cambiosFormulasConceptos = false;
      nuevaFormulaConcepto = new FormulasConceptos();
      duplicarFormulaConcepto = new FormulasConceptos();
//      lovConceptos = null;
//      lovFormulasConceptosOrden = null;
   }

   public void actualizarOrden() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         formulaConceptoSeleccionada.setStrOrden(formulaCOrdenLovSeleccionado.getStrOrden());
         if (!listFormulasConceptosCrear.contains(formulaConceptoSeleccionada)) {
            if (listFormulasConceptosModificar.isEmpty()) {
               listFormulasConceptosModificar.add(formulaConceptoSeleccionada);
            } else if (!listFormulasConceptosModificar.contains(formulaConceptoSeleccionada)) {
               listFormulasConceptosModificar.add(formulaConceptoSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         cambiosFormulasConceptos = true;
         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      } else if (tipoActualizacion == 1) {
         nuevaFormulaConcepto.setStrOrden(formulaCOrdenLovSeleccionado.getStrOrden());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaOrden");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaConcepto.setStrOrden(formulaCOrdenLovSeleccionado.getStrOrden());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOrden");
      }
      filtrarLovFormulasConceptosOrden = null;
      formulaCOrdenLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:OrdenDialogo");
      RequestContext.getCurrentInstance().update("form:lovOrden");
      RequestContext.getCurrentInstance().update("form:aceptarO");
      context.reset("form:lovOrden:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOrden').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('OrdenDialogo').hide()");
   }

   public void cancelarCambioOrden() {
      filtrarLovFormulasConceptosOrden = null;
      formulaCOrdenLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:OrdenDialogo");
      RequestContext.getCurrentInstance().update("form:lovOrden");
      RequestContext.getCurrentInstance().update("form:aceptarO");
      context.reset("form:lovOrden:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOrden').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('OrdenDialogo').hide()");
   }

   public void actualizarConcepto() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         formulaConceptoSeleccionada.setConcepto(conceptoLovSeleccionada.getSecuencia());
         formulaConceptoSeleccionada.setNombreConcepto(conceptoLovSeleccionada.getDescripcion());
         formulaConceptoSeleccionada.setCodigoConcepto(conceptoLovSeleccionada.getCodigo());
         formulaConceptoSeleccionada.setNombreEmpresa(conceptoLovSeleccionada.getNombreEmpresa());
         formulaConceptoSeleccionada.setNitEmpresa(conceptoLovSeleccionada.getNitEmpresa());

         if (!listFormulasConceptosCrear.contains(formulaConceptoSeleccionada)) {
            if (listFormulasConceptosModificar.isEmpty()) {
               listFormulasConceptosModificar.add(formulaConceptoSeleccionada);
            } else if (!listFormulasConceptosModificar.contains(formulaConceptoSeleccionada)) {
               listFormulasConceptosModificar.add(formulaConceptoSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         cambiosFormulasConceptos = true;
         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
      } else if (tipoActualizacion == 1) {
         nuevaFormulaConcepto.setConcepto(conceptoLovSeleccionada.getSecuencia());
         nuevaFormulaConcepto.setNombreConcepto(conceptoLovSeleccionada.getDescripcion());
         nuevaFormulaConcepto.setCodigoConcepto(conceptoLovSeleccionada.getCodigo());
         nuevaFormulaConcepto.setNombreEmpresa(conceptoLovSeleccionada.getNombreEmpresa());
         nuevaFormulaConcepto.setNitEmpresa(conceptoLovSeleccionada.getNitEmpresa());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigo");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaConcepto");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNIT");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaConcepto.setConcepto(conceptoLovSeleccionada.getSecuencia());
         duplicarFormulaConcepto.setNombreConcepto(conceptoLovSeleccionada.getDescripcion());
         duplicarFormulaConcepto.setCodigoConcepto(conceptoLovSeleccionada.getCodigo());
         duplicarFormulaConcepto.setNombreEmpresa(conceptoLovSeleccionada.getNombreEmpresa());
         duplicarFormulaConcepto.setNitEmpresa(conceptoLovSeleccionada.getNitEmpresa());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNIT");
      }
      filtrarLovConceptos = null;
      conceptoLovSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
      RequestContext.getCurrentInstance().update("form:lovConcepto");
      RequestContext.getCurrentInstance().update("form:aceptarC");
      context.reset("form:lovConcepto:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConcepto').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').hide()");
   }

   public void cancelarCambioConcepto() {
      filtrarLovConceptos = null;
      conceptoLovSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ConceptoDialogo");
      RequestContext.getCurrentInstance().update("form:lovConcepto");
      RequestContext.getCurrentInstance().update("form:aceptarC");
      context.reset("form:lovConcepto:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConcepto').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ConceptoDialogo').hide()");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   /**
    *
    * @return Nombre del dialogo a exportar en XML
    */
   public String exportXML() {
      nombreTabla = ":formExportarFormula:datosFormulaConceptoExportar";
      nombreXML = "FormulaConcepto_XML";
      return nombreTabla;
   }

   /**
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      nombreTabla = ":formExportarFormula:datosFormulaConceptoExportar";
      nombreExportar = "FormulaConcepto_PDF";
      exportPDF_Tabla();
   }

   /**
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDF_Tabla() throws IOException {
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
      nombreTabla = ":formExportarFormula:datosFormulaConceptoExportar";
      nombreExportar = "FormulaConcepto_XLS";
      exportXLS_Tabla();
   }

   /**
    * Metodo que exporta datos a XLS Vigencia Sueldos
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS_Tabla() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(nombreTabla);
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, nombreExportar, false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista real a la filtrada
    */
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      formulaConceptoSeleccionada = null;
      contarRegistros();
      anularBotonLOV();
   }

   public void verificarRastroTabla() {
      if (formulaConceptoSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(formulaConceptoSeleccionada.getSecuencia(), "FORMULASCONCEPTOS");
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "FormulasConceptos";
            msnConfirmarRastro = "La tabla FORMULASCONCEPTOS tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("FORMULASCONCEPTOS")) {
         nombreTablaRastro = "FormulasConceptos";
         msnConfirmarRastroHistorico = "La tabla FORMULASCONCEPTOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      anularBotonLOV();
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLovFCOrden() {
      RequestContext.getCurrentInstance().update("form:infoRegistroOrden");
   }

   public void contarRegistrosLovConceptos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroConcepto");
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //GET - SET 
   public List<FormulasConceptos> getListFormulasConceptos() {
      if (listFormulasConceptos == null) {
         log.info("Entro a consultar formulasConceptosParaFormula formulaActual : " + formulaActual);
         if (formulaActual.getSecuencia() != null) {
            listFormulasConceptos = administrarFormulaConcepto.formulasConceptosParaFormula(formulaActual.getSecuencia());
            log.info("Salio de consultar formulasConceptosParaFormula");
         }
      }
      return listFormulasConceptos;
   }

   public void setListFormulasConceptos(List<FormulasConceptos> t) {
      this.listFormulasConceptos = t;
   }

   public List<FormulasConceptos> getFiltrarListFormulasConceptos() {
      return filtrarListFormulasConceptos;
   }

   public void setFiltrarListFormulasConceptos(List<FormulasConceptos> t) {
      this.filtrarListFormulasConceptos = t;
   }

   public String getNombreXML() {
      return nombreXML;
   }

   public void setNombreXML(String nombreXML) {
      this.nombreXML = nombreXML;
   }

   public String getNombreTabla() {
      return nombreTabla;
   }

   public void setNombreTabla(String nombreTabla) {
      this.nombreTabla = nombreTabla;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public FormulasConceptos getNuevaFormulaConcepto() {
      return nuevaFormulaConcepto;
   }

   public void setNuevaFormulaConcepto(FormulasConceptos setNuevaFormulaConcepto) {
      this.nuevaFormulaConcepto = setNuevaFormulaConcepto;
   }

   public FormulasConceptos getEditarFormulaConcepto() {
      return editarFormulaConcepto;
   }

   public void setEditarFormulaConcepto(FormulasConceptos setEditarFormulaConcepto) {
      this.editarFormulaConcepto = setEditarFormulaConcepto;
   }

   public FormulasConceptos getDuplicarFormulaConcepto() {
      return duplicarFormulaConcepto;
   }

   public void setDuplicarFormulaConcepto(FormulasConceptos setDuplicarFormulaConcepto) {
      this.duplicarFormulaConcepto = setDuplicarFormulaConcepto;
   }

   public String getMsnConfirmarRastro() {
      return msnConfirmarRastro;
   }

   public void setMsnConfirmarRastro(String msnConfirmarRastro) {
      this.msnConfirmarRastro = msnConfirmarRastro;
   }

   public String getMsnConfirmarRastroHistorico() {
      return msnConfirmarRastroHistorico;
   }

   public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
      this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
   }

   public String getNombreTablaRastro() {
      return nombreTablaRastro;
   }

   public void setNombreTablaRastro(String nombreTablaRastro) {
      this.nombreTablaRastro = nombreTablaRastro;
   }

   public Formulas getFormulaActual() {
      return formulaActual;
   }

   public void setFormulaActual(Formulas setFormulaActual) {
      this.formulaActual = setFormulaActual;
   }

   public List<FormulasConceptos> getLovFormulasConceptosOrden() {
      if (lovFormulasConceptosOrden == null) {
         log.info("a consultar listFormulasConceptos");
         lovFormulasConceptosOrden = administrarFormulaConcepto.listFormulasConceptos();
         log.info("Ya consulto listFormulasConceptos");
      }
      return lovFormulasConceptosOrden;
   }

   public void setLovFormulasConceptosOrden(List<FormulasConceptos> setListFormulasConceptos) {
      this.lovFormulasConceptosOrden = setListFormulasConceptos;
   }

   public List<FormulasConceptos> getFiltrarLovFormulasConceptosOrden() {
      return filtrarLovFormulasConceptosOrden;
   }

   public void setFiltrarLovFormulasConceptosOrden(List<FormulasConceptos> setFiltrarListFormulasConceptos) {
      this.filtrarLovFormulasConceptosOrden = setFiltrarListFormulasConceptos;
   }

   public FormulasConceptos getFormulaCOrdenLovSeleccionado() {
      return formulaCOrdenLovSeleccionado;
   }

   public void setFormulaCOrdenLovSeleccionado(FormulasConceptos setFormulaSeleccionado) {
      this.formulaCOrdenLovSeleccionado = setFormulaSeleccionado;
   }

   public List<Conceptos> getLovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = administrarFormulaConcepto.listConceptos();
      }
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> setListConceptos) {
      this.lovConceptos = setListConceptos;
   }

   public List<Conceptos> getFiltrarLovConceptos() {
      return filtrarLovConceptos;
   }

   public void setFiltrarLovConceptos(List<Conceptos> setFiltrarListConceptos) {
      this.filtrarLovConceptos = setFiltrarListConceptos;
   }

   public Conceptos getConceptoLovSeleccionada() {
      return conceptoLovSeleccionada;
   }

   public void setConceptoLovSeleccionada(Conceptos setConceptoSeleccionada) {
      this.conceptoLovSeleccionada = setConceptoSeleccionada;
   }

   public FormulasConceptos getFormulaConceptoSeleccionada() {
      return formulaConceptoSeleccionada;
   }

   public void setFormulaConceptoSeleccionada(FormulasConceptos formulaConceptoSeleccionada) {
      this.formulaConceptoSeleccionada = formulaConceptoSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFormulaConcepto");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroLovConcepto() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovConcepto");
      infoRegistroLovConcepto = String.valueOf(tabla.getRowCount());
      return infoRegistroLovConcepto;
   }

   public void setInfoRegistroLovConcepto(String infoRegistroLovConcepto) {
      this.infoRegistroLovConcepto = infoRegistroLovConcepto;
   }

   public String getInfoRegistroLovFCOrden() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOrden");
      infoRegistroLovFCOrden = String.valueOf(tabla.getRowCount());
      return infoRegistroLovFCOrden;
   }

   public void setInfoRegistroLovFCOrden(String infoRegistroLovFCOrden) {
      this.infoRegistroLovFCOrden = infoRegistroLovFCOrden;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

}
