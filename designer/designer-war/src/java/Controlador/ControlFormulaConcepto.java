package Controlador;

import Entidades.Conceptos;
import Entidades.Empresas;
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
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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

   public ControlFormulaConcepto() {
      altoTabla = "310";
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
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarFormulaConcepto.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirFormula(BigInteger secuencia) {
      formulaActual = administrarFormulaConcepto.formulaActual(secuencia);
      listFormulasConceptos = null;
      getListFormulasConceptos();
      contarRegistros();
      anularBotonLOV();
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
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:datosFormulaConcepto");
      cambiosFormulasConceptos = true;
   }

   public void modificarFormulaConcepto(FormulasConceptos fc, String confirmarCambio, String valorConfirmar) {
      formulaConceptoSeleccionada = fc;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
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
            formulaConceptoSeleccionada.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
            formulaConceptoSeleccionada.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
         } else {
            permitirIndex = false;
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         formulaConceptoSeleccionada.setNombreConcepto(concepto);
         formulaConceptoSeleccionada.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
         formulaConceptoSeleccionada.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
         formulaConceptoSeleccionada.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
         formulaConceptoSeleccionada.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
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
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
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
            contarRegistrosLovFCOrden(0);
            context.update("form:OrdenDialogo");
            context.execute("OrdenDialogo.show()");
            tipoActualizacion = 0;
         }

      }
      if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
         formulaConceptoSeleccionada.setNombreEmpresa(empresa);
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getEmpresa().getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaConceptoSeleccionada.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
            formulaConceptoSeleccionada.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
            formulaConceptoSeleccionada.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
            formulaConceptoSeleccionada.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
            formulaConceptoSeleccionada.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
         } else {
            permitirIndex = false;
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("NIT")) {
         formulaConceptoSeleccionada.setNitEmpresa(Long.parseLong(nitEmpresa));
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getEmpresa().getStrNit().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaConceptoSeleccionada.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
            formulaConceptoSeleccionada.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
            formulaConceptoSeleccionada.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
            formulaConceptoSeleccionada.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
            formulaConceptoSeleccionada.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
         } else {
            permitirIndex = false;
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
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
      context.update("form:datosFormulaConcepto");
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
            System.out.println("Asignando codigoConcepto : null como ''''");
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
      RequestContext context = RequestContext.getCurrentInstance();
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
               nuevaFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
               nuevaFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
               context.update("formularioDialogos:nuevaCodigo");
               context.update("formularioDialogos:nuevaConcepto");
               context.update("formularioDialogos:nuevaEmpresa");
               context.update("formularioDialogos:nuevaNIT");
            } else if (tipoNuevo == 2) {
               duplicarFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               duplicarFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               duplicarFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               duplicarFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
               duplicarFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
               context.update("formularioDialogos:duplicarCodigo");
               context.update("formularioDialogos:duplicarConcepto");
               context.update("formularioDialogos:duplicarEmpresa");
               context.update("formularioDialogos:duplicarNIT");
            }
         } else {
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               context.update("formularioDialogos:nuevaCodigo");
            } else if (tipoNuevo == 2) {
               context.update("formularioDialogos:duplicarCodigo");
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
               nuevaFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
               nuevaFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
               context.update("formularioDialogos:nuevaCodigo");
               context.update("formularioDialogos:nuevaConcepto");
               context.update("formularioDialogos:nuevaEmpresa");
               context.update("formularioDialogos:nuevaNIT");
            } else if (tipoNuevo == 2) {
               duplicarFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               duplicarFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               duplicarFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               duplicarFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
               duplicarFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
               context.update("formularioDialogos:duplicarCodigo");
               context.update("formularioDialogos:duplicarConcepto");
               context.update("formularioDialogos:duplicarEmpresa");
               context.update("formularioDialogos:duplicarNIT");
            }
         } else {
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               context.update("formularioDialogos:nuevaConcepto");
            } else if (tipoNuevo == 2) {
               context.update("formularioDialogos:duplicarConcepto");
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
               context.update("formularioDialogos:nuevaOrden");
            } else if (tipoNuevo == 2) {
               duplicarFormulaConcepto.setStrOrden(lovFormulasConceptosOrden.get(indiceUnicoElemento).getStrOrden());
               context.update("formularioDialogos:duplicarOrden");
            }
         } else {
            contarRegistrosLovFCOrden(0);
            context.update("form:OrdenDialogo");
            context.execute("OrdenDialogo.show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               context.update("formularioDialogos:nuevaOrden");
            } else if (tipoNuevo == 2) {
               context.update("formularioDialogos:duplicarOrden");
            }
         }
      } else if (campo.equalsIgnoreCase("EMPRESA")) {
         if (tipoNuevo == 1) {
            nuevaFormulaConcepto.setNombreEmpresa(empresa);
         } else if (tipoNuevo == 2) {
            duplicarFormulaConcepto.setNombreEmpresa(empresa);
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getEmpresa().getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               nuevaFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               nuevaFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               nuevaFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
               nuevaFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
               context.update("formularioDialogos:nuevaCodigo");
               context.update("formularioDialogos:nuevaConcepto");
               context.update("formularioDialogos:nuevaEmpresa");
               context.update("formularioDialogos:nuevaNIT");
            } else if (tipoNuevo == 2) {
               duplicarFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               duplicarFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               duplicarFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               duplicarFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
               duplicarFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
               context.update("formularioDialogos:duplicarCodigo");
               context.update("formularioDialogos:duplicarConcepto");
               context.update("formularioDialogos:duplicarEmpresa");
               context.update("formularioDialogos:duplicarNIT");
            }
         } else {
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               context.update("formularioDialogos:nuevaEmpresa");
            } else if (tipoNuevo == 2) {
               context.update("formularioDialogos:duplicarEmpresa");
            }
         }
      } else if (campo.equalsIgnoreCase("NIT")) {
         if (tipoNuevo == 1) {
            nuevaFormulaConcepto.setNitEmpresa(Long.parseLong(nitEmpresa));
         } else if (tipoNuevo == 2) {
            duplicarFormulaConcepto.setNitEmpresa(Long.parseLong(nitEmpresa));
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getEmpresa().getStrNit().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               nuevaFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               nuevaFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               nuevaFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
               nuevaFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
               context.update("formularioDialogos:nuevaCodigo");
               context.update("formularioDialogos:nuevaConcepto");
               context.update("formularioDialogos:nuevaEmpresa");
               context.update("formularioDialogos:nuevaNIT");
            } else if (tipoNuevo == 2) {
               duplicarFormulaConcepto.setConcepto(lovConceptos.get(indiceUnicoElemento).getSecuencia());
               duplicarFormulaConcepto.setNombreConcepto(lovConceptos.get(indiceUnicoElemento).getDescripcion());
               duplicarFormulaConcepto.setCodigoConcepto(lovConceptos.get(indiceUnicoElemento).getCodigo());
               duplicarFormulaConcepto.setNombreEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNombre());
               duplicarFormulaConcepto.setNitEmpresa(lovConceptos.get(indiceUnicoElemento).getEmpresa().getNit());
               context.update("formularioDialogos:duplicarCodigo");
               context.update("formularioDialogos:duplicarConcepto");
               context.update("formularioDialogos:duplicarEmpresa");
               context.update("formularioDialogos:duplicarNIT");
            }
         } else {
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               context.update("formularioDialogos:nuevaNIT");
            } else if (tipoNuevo == 2) {
               context.update("formularioDialogos:duplicarNIT");
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
         ///////// Captura Objetos Para Campos NotNull ///////////
         fechaIni = formulaConceptoSeleccionada.getFechainicial();
         fechaFin = formulaConceptoSeleccionada.getFechafinal();
         concepto = formulaConceptoSeleccionada.getNombreConcepto();
         orden = formulaConceptoSeleccionada.getStrOrden();
         empresa = formulaConceptoSeleccionada.getNombreEmpresa();
         nitEmpresa = "" + formulaConceptoSeleccionada.getNitEmpresa();
         if (formulaConceptoSeleccionada.getCodigoConcepto() != null){
            codigoConcepto = formulaConceptoSeleccionada.getCodigoConcepto().toString();
         }
      }
   }

   public boolean validarFechasRegistroFormulas(int tipoAct) {
      boolean retorno = false;
      if (tipoAct == 0) {
         if (formulaConceptoSeleccionada.getFechainicial().after(fechaParametro) && (formulaConceptoSeleccionada.getFechainicial().before(formulaConceptoSeleccionada.getFechafinal()))) {
            retorno = true;
         }
      }
      if (tipoAct == 1) {
         if (nuevaFormulaConcepto.getFechainicial().after(fechaParametro) && (nuevaFormulaConcepto.getFechainicial().before(nuevaFormulaConcepto.getFechafinal()))) {
            retorno = true;
         }
      }
      if (tipoAct == 2) {
         if (duplicarFormulaConcepto.getFechainicial().after(fechaParametro) && (duplicarFormulaConcepto.getFechainicial().before(duplicarFormulaConcepto.getFechafinal()))) {
            retorno = true;
         }
      }
      return retorno;
   }

   public void modificacionesFechaFormula(FormulasConceptos fc, int c) {
      formulaConceptoSeleccionada = fc;
      if ((formulaConceptoSeleccionada.getFechainicial() != null) && (formulaConceptoSeleccionada.getFechafinal() != null)) {
         boolean validacion = validarFechasRegistroFormulas(0);
         if (validacion == true) {
            cambiarIndiceFormulaConcepto(fc, c);
            modificarFormulaConcepto(fc);
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosFormulaConcepto");
         } else {
            System.out.println("Error de fechas de ingreso");
            RequestContext context = RequestContext.getCurrentInstance();
            formulaConceptoSeleccionada.setFechainicial(fechaIni);
            formulaConceptoSeleccionada.setFechafinal(fechaFin);
            context.update("form:datosFormulaConcepto");
            context.execute("errorFechasFC.show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         formulaConceptoSeleccionada.setFechainicial(fechaIni);
         formulaConceptoSeleccionada.setFechafinal(fechaFin);
         context.update("form:datosFormulaConcepto");
         context.execute("errorRegNuevo.show()");
      }
   }

   //GUARDAR
   /**
    */
   public void guardadoGeneral() {
      System.out.println("Entro en guardadoGeneral()");
      if (cambiosFormulasConceptos == true) {
         RequestContext context = RequestContext.getCurrentInstance();
         try {
            System.out.println("listFormulasConceptosBorrar.size() : " + listFormulasConceptosBorrar.size());
            System.out.println("listFormulasConceptosCrear.size() : " + listFormulasConceptosCrear.size());
            System.out.println("listFormulasConceptosModificar.size() : " + listFormulasConceptosModificar.size());
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
            context.update("form:datosFormulaConcepto");
            FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            k = 0;
            cambiosFormulasConceptos = false;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } catch (Exception e) {
            System.out.println("Error guardarCambiosFormula  : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
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
      RequestContext context = RequestContext.getCurrentInstance();
      getListFormulasConceptos();
      contarRegistros();
      anularBotonLOV();
      context.update("form:datosFormulaConcepto");
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (formulaConceptoSeleccionada != null) {
         if (cualCelda == 2) {
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 3) {
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 4) {
            contarRegistrosLovFCOrden(0);
            context.update("form:OrdenDialogo");
            context.execute("OrdenDialogo.show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 5) {
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 6) {
            contarRegistrosLovConceptos(0);
            context.update("form:ConceptoDialogo");
            context.execute("ConceptoDialogo.show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndex(FormulasConceptos fc, int dlg, int LND) {
      formulaConceptoSeleccionada = fc;
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = LND;
      activarBotonLOV();
      if (dlg == 0) {
         contarRegistrosLovConceptos(0);
         context.update("form:ConceptoDialogo");
         context.execute("ConceptoDialogo.show()");
      } else if (dlg == 1) {
         contarRegistrosLovFCOrden(0);
         context.update("form:OrdenDialogo");
         context.execute("OrdenDialogo.show()");
      }
   }

   public void asignarIndex(int dlg, int LND) {
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = LND;
      anularBotonLOV();
      if (dlg == 0) {
         contarRegistrosLovConceptos(0);
         context.update("form:ConceptoDialogo");
         context.execute("ConceptoDialogo.show()");
      } else if (dlg == 1) {
         contarRegistrosLovFCOrden(0);
         context.update("form:OrdenDialogo");
         context.execute("OrdenDialogo.show()");
      }
   }

   public void editarCelda() {
      if (formulaConceptoSeleccionada != null) {
         editarFormulaConcepto = formulaConceptoSeleccionada;
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            context.update("formularioDialogos:editarFechaInicialFD");
            context.execute("editarFechaInicialFD.show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            context.update("formularioDialogos:editarFechaFinalFD");
            context.execute("editarFechaFinalFD.show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            context.update("formularioDialogos:editarCodigoFD");
            context.execute("editarCodigoFD.show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            context.update("formularioDialogos:editarConceptoFD");
            context.execute("editarConceptoFD.show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            context.update("formularioDialogos:editarOrdenFD");
            context.execute("editarOrdenFD.show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            context.update("formularioDialogos:editarEmpresaFD");
            context.execute("editarEmpresaFD.show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            context.update("formularioDialogos:editarNitFD");
            context.execute("editarNitFD.show()");
            cualCelda = -1;
         }
      }
   }

   public void ingresoNuevoRegistro() {
      validarIngresoNuevaFormula();
   }

   public void validarIngresoNuevaFormula() {
      RequestContext context = RequestContext.getCurrentInstance();
      limpiarNuevoFormula();
      context.update("form:nuevaF");
      context.update("formularioDialogos:NuevoRegistroFormula");
      context.execute("NuevoRegistroFormula.show()");
   }

   public void validarDuplicadoRegistro() {
      if (formulaConceptoSeleccionada != null) {
         duplicarFormulaM();
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         context.execute("seleccionarRegistro.show()");
      }
   }

   public void validarBorradoRegistro() {
      if (formulaConceptoSeleccionada != null) {
         borrarFormula();
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         context.execute("seleccionarRegistro.show()");
      }
   }

   public boolean validarNuevosDatosFormula(int tipoAct) {
      boolean retorno = false;
      if (tipoAct == 1) {
         if ((nuevaFormulaConcepto.getConcepto() != null)
                 && (nuevaFormulaConcepto.getFechainicial() != null)
                 && (nuevaFormulaConcepto.getFechafinal() != null)
                 && (!nuevaFormulaConcepto.getStrOrden().isEmpty())) {
            return true;
         }
      }
      if (tipoAct == 2) {
         if ((duplicarFormulaConcepto.getConcepto() != null)
                 && (duplicarFormulaConcepto.getFechainicial() != null)
                 && (duplicarFormulaConcepto.getFechafinal() != null)
                 && (!duplicarFormulaConcepto.getStrOrden().isEmpty())) {
            return true;
         }
      }
      return retorno;
   }

   public void agregarNuevoFormula() {
      boolean resp = validarNuevosDatosFormula(1);
      if (resp == true) {
         boolean validacion = validarFechasRegistroFormulas(1);
         if (validacion == true) {
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
            contarRegistros();
            ////------////
            nuevaFormulaConcepto = new FormulasConceptos();
            ////-----////
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("NuevoRegistroFormula.hide()");
            context.update("form:datosFormulaConcepto");
            context.update("formularioDialogos:NuevoRegistroFormula");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosFormulasConceptos = true;
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorFechasFC.show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         context.execute("errorRegNuevo.show()");
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

         RequestContext context = RequestContext.getCurrentInstance();
         context.update("formularioDialogos:DuplicarRegistroFormula");
         context.execute("DuplicarRegistroFormula.show()");
      }
   }

   public void confirmarDuplicarFormula() {
      boolean resp = validarNuevosDatosFormula(2);
      if (resp == true) {
         boolean validacion = validarFechasRegistroFormulas(2);
         if (validacion == true) {
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
            contarRegistros();

            duplicarFormulaConcepto = new FormulasConceptos();
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosFormulaConcepto");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.execute("DuplicarRegistroFormula.hide()");
            cambiosFormulasConceptos = true;
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("errorFechasFC.show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         context.execute("errorRegNuevo.show()");
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
         RequestContext context = RequestContext.getCurrentInstance();
         contarRegistros();
         context.update("form:datosFormulaConcepto");
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
         altoTabla = "290";
         formulaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaInicial");
         formulaFechaInicial.setFilterStyle("width: 85%;");
         formulaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaFechaFinal");
         formulaFechaFinal.setFilterStyle("width: 85%;");
         formulaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaCodigo");
         formulaCodigo.setFilterStyle("width: 85%");
         formulaDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaDescripcion");
         formulaDescripcion.setFilterStyle("width: 85%");
         formulaOrden = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaOrden");
         formulaOrden.setFilterStyle("width: 85%");
         formulaEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaEmpresa");
         formulaEmpresa.setFilterStyle("width: 85%");
         formulaNIT = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaConcepto:formulaNIT");
         formulaNIT.setFilterStyle("width: 85%");

         RequestContext.getCurrentInstance().update("form:datosFormulaConcepto");
         bandera = 1;
      } else {
         restablecerTabla();
      }
   }

   public void restablecerTabla() {
      if (bandera == 1) {
         altoTabla = "310";
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
      restablecerTabla();
      listFormulasConceptosBorrar.clear();
      listFormulasConceptosCrear.clear();
      listFormulasConceptosModificar.clear();
      formulaConceptoSeleccionada = null;
      formulaActual = null;
      k = 0;
      listFormulasConceptos = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      cambiosFormulasConceptos = false;
      nuevaFormulaConcepto = new FormulasConceptos();
      duplicarFormulaConcepto = new FormulasConceptos();
      lovConceptos = null;
      lovFormulasConceptosOrden = null;
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
         context.update("form:datosFormulaConcepto");
      } else if (tipoActualizacion == 1) {
         nuevaFormulaConcepto.setStrOrden(formulaCOrdenLovSeleccionado.getStrOrden());
         context.update("formularioDialogos:nuevaOrden");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaConcepto.setStrOrden(formulaCOrdenLovSeleccionado.getStrOrden());
         context.update("formularioDialogos:duplicarOrden");
      }
      filtrarLovFormulasConceptosOrden = null;
      formulaCOrdenLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;

      context.update("form:OrdenDialogo");
      context.update("form:lovOrden");
      context.update("form:aceptarO");
      context.reset("form:lovOrden:globalFilter");
      context.execute("lovOrden.clearFilters()");
      context.execute("OrdenDialogo.hide()");
   }

   public void cancelarCambioOrden() {
      filtrarLovFormulasConceptosOrden = null;
      formulaCOrdenLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:OrdenDialogo");
      context.update("form:lovOrden");
      context.update("form:aceptarO");
      context.reset("form:lovOrden:globalFilter");
      context.execute("lovOrden.clearFilters()");
      context.execute("OrdenDialogo.hide()");
   }

   public void actualizarConcepto() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         formulaConceptoSeleccionada.setConcepto(conceptoLovSeleccionada.getSecuencia());
         formulaConceptoSeleccionada.setNombreConcepto(conceptoLovSeleccionada.getDescripcion());
         formulaConceptoSeleccionada.setCodigoConcepto(conceptoLovSeleccionada.getCodigo());
         formulaConceptoSeleccionada.setNombreEmpresa(conceptoLovSeleccionada.getEmpresa().getNombre());
         formulaConceptoSeleccionada.setNitEmpresa(conceptoLovSeleccionada.getEmpresa().getNit());

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
         context.update("form:datosFormulaConcepto");
      } else if (tipoActualizacion == 1) {
         nuevaFormulaConcepto.setConcepto(conceptoLovSeleccionada.getSecuencia());
         nuevaFormulaConcepto.setNombreConcepto(conceptoLovSeleccionada.getDescripcion());
         nuevaFormulaConcepto.setCodigoConcepto(conceptoLovSeleccionada.getCodigo());
         nuevaFormulaConcepto.setNombreEmpresa(conceptoLovSeleccionada.getEmpresa().getNombre());
         nuevaFormulaConcepto.setNitEmpresa(conceptoLovSeleccionada.getEmpresa().getNit());
         context.update("formularioDialogos:nuevaCodigo");
         context.update("formularioDialogos:nuevaConcepto");
         context.update("formularioDialogos:nuevaEmpresa");
         context.update("formularioDialogos:nuevaNIT");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaConcepto.setConcepto(conceptoLovSeleccionada.getSecuencia());
         duplicarFormulaConcepto.setNombreConcepto(conceptoLovSeleccionada.getDescripcion());
         duplicarFormulaConcepto.setCodigoConcepto(conceptoLovSeleccionada.getCodigo());
         duplicarFormulaConcepto.setNombreEmpresa(conceptoLovSeleccionada.getEmpresa().getNombre());
         duplicarFormulaConcepto.setNitEmpresa(conceptoLovSeleccionada.getEmpresa().getNit());
         context.update("formularioDialogos:duplicarCodigo");
         context.update("formularioDialogos:duplicarConcepto");
         context.update("formularioDialogos:duplicarEmpresa");
         context.update("formularioDialogos:duplicarNIT");
      }
      filtrarLovConceptos = null;
      conceptoLovSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;

      context.update("form:ConceptoDialogo");
      context.update("form:lovConcepto");
      context.update("form:aceptarC");
      context.reset("form:lovConcepto:globalFilter");
      context.execute("lovConcepto.clearFilters()");
      context.execute("ConceptoDialogo.hide()");
   }

   public void cancelarCambioConcepto() {
      filtrarLovConceptos = null;
      conceptoLovSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:ConceptoDialogo");
      context.update("form:lovConcepto");
      context.update("form:aceptarC");
      context.reset("form:lovConcepto:globalFilter");
      context.execute("lovConcepto.clearFilters()");
      context.execute("ConceptoDialogo.hide()");
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
      RequestContext context = RequestContext.getCurrentInstance();
      if (formulaConceptoSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(formulaConceptoSeleccionada.getSecuencia(), "FORMULASCONCEPTOS");
         if (resultado == 1) {
            context.execute("errorObjetosDB.show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "FormulasConceptos";
            msnConfirmarRastro = "La tabla FORMULASCONCEPTOS tiene rastros para el registro seleccionado, ¿desea continuar?";
            context.update("form:msnConfirmarRastro");
            context.execute("confirmarRastro.show()");
         } else if (resultado == 3) {
            context.execute("errorRegistroRastro.show()");
         } else if (resultado == 4) {
            context.execute("errorTablaConRastro.show()");
         } else if (resultado == 5) {
            context.execute("errorTablaSinRastro.show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("FORMULASCONCEPTOS")) {
         nombreTablaRastro = "FormulasConceptos";
         msnConfirmarRastroHistorico = "La tabla FORMULASCONCEPTOS tiene rastros historicos, ¿Desea continuar?";
         context.update("form:confirmarRastroHistorico");
         context.execute("confirmarRastroHistorico.show()");
      } else {
         context.execute("errorRastroHistorico.show()");
      }
      anularBotonLOV();
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   public void contarRegistros() {
      if (tipoLista == 1) {
         infoRegistro = String.valueOf(filtrarListFormulasConceptos.size());
      } else if (listFormulasConceptos != null) {
         infoRegistro = String.valueOf(listFormulasConceptos.size());
      } else {
         infoRegistro = String.valueOf(0);
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLovFCOrden(int tipoLov) {
      if (tipoLov == 1) {
         infoRegistroLovFCOrden = String.valueOf(filtrarLovFormulasConceptosOrden.size());
      } else if (lovConceptos != null) {
         infoRegistroLovFCOrden = String.valueOf(lovFormulasConceptosOrden.size());
      } else {
         infoRegistroLovFCOrden = String.valueOf(0);
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroOrden");
   }

   public void contarRegistrosLovConceptos(int tipoLov) {
      if (tipoLov == 1) {
         infoRegistroLovConcepto = String.valueOf(filtrarLovConceptos.size());
      } else if (lovConceptos != null) {
         infoRegistroLovConcepto = String.valueOf(lovConceptos.size());
      } else {
         infoRegistroLovConcepto = String.valueOf(0);
      }
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
      try {
         if (listFormulasConceptos == null) {
            if (formulaActual.getSecuencia() != null) {
               System.out.println("Entro a consultar formulasConceptosParaFormula");
               listFormulasConceptos = administrarFormulaConcepto.formulasConceptosParaFormula(formulaActual.getSecuencia());
               System.out.println("Salio de consultar formulasConceptosParaFormula");
            }
         }
         return listFormulasConceptos;
      } catch (Exception e) {
         System.out.println("Error getListFormulasConceptosFormula : " + e.toString());
         return null;
      }
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

   public List<FormulasConceptos> getListFormulasConceptosModificar() {
      return listFormulasConceptosModificar;
   }

   public void setListFormulasConceptosModificar(List<FormulasConceptos> setListFormulasConceptosModificar) {
      this.listFormulasConceptosModificar = setListFormulasConceptosModificar;
   }

   public FormulasConceptos getNuevaFormulaConcepto() {
      return nuevaFormulaConcepto;
   }

   public void setNuevaFormulaConcepto(FormulasConceptos setNuevaFormulaConcepto) {
      this.nuevaFormulaConcepto = setNuevaFormulaConcepto;
   }

   public List<FormulasConceptos> getListFormulasConceptosCrear() {
      return listFormulasConceptosCrear;
   }

   public void setListFormulasConceptosCrear(List<FormulasConceptos> setListFormulasConceptosCrear) {
      this.listFormulasConceptosCrear = setListFormulasConceptosCrear;
   }

   public List<FormulasConceptos> getListFormulasConceptosBorrar() {
      return listFormulasConceptosBorrar;
   }

   public void setListFormulasConceptosBorrar(List<FormulasConceptos> setListFormulasConceptosBorrar) {
      this.listFormulasConceptosBorrar = setListFormulasConceptosBorrar;
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
         System.out.println("a consultar listFormulasConceptos");
         lovFormulasConceptosOrden = administrarFormulaConcepto.listFormulasConceptos();
         System.out.println("Ya consulto listFormulasConceptos");
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
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistroLovConcepto() {
      return infoRegistroLovConcepto;
   }

   public void setInfoRegistroLovConcepto(String infoRegistroLovConcepto) {
      this.infoRegistroLovConcepto = infoRegistroLovConcepto;
   }

   public String getInfoRegistroLovFCOrden() {
      return infoRegistroLovFCOrden;
   }

   public void setInfoRegistroLovFCOrden(String infoRegistroLovFCOrden) {
      this.infoRegistroLovFCOrden = infoRegistroLovFCOrden;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

}
