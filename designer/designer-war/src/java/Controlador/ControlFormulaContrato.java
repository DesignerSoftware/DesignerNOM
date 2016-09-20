package Controlador;


import Entidades.Contratos;
import Entidades.Formulas;
import Entidades.Formulascontratos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaContratoInterface;
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

@ManagedBean
@SessionScoped
public class ControlFormulaContrato implements Serializable {

   @EJB
   AdministrarFormulaContratoInterface administrarFormulaContrato;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //////////////Formulas//////////////////
   private Formulas formulaActual;
   ///////////Formulascontratos////////////
   private List<Formulascontratos> listFormulasContratos;
   private List<Formulascontratos> filtrarListFormulasContratos;
   private Formulascontratos formulaTablaSeleccionada;
   ///////////Formulascontratos/////////////
   private int banderaFormulaContrato;
   private List<Formulascontratos> listFormulasContratosModificar;
   private Formulascontratos nuevaFormulaContrato;
   private List<Formulascontratos> listFormulasContratosCrear;
   private List<Formulascontratos> listFormulasContratosBorrar;
   private Formulascontratos editarFormulaContrato;
   private int cualCelda, tipoLista;
   private Formulascontratos duplicarFormulaContrato;
   private boolean cambiosFormulaContrato;
   private String legislacion, periodicidad, tercero;
   private Date fechaIni;
   private Column formulaFechaInicial, formulaFechaFinal, formulaLegislacion, formulaPeriodicidad, formulaTercero;
   private boolean permitirIndexFormulaContrato;
   ////////////Listas Valores Formulascontratos/////////////
   private List<Contratos> lovContratos;
   private List<Contratos> filtrarLovContratos;
   private Contratos contratoLovSeleccionado;
   private List<Terceros> lovTerceros;
   private List<Terceros> filtrarLovTerceros;
   private Terceros terceroLovSeleccionada;
   private List<Periodicidades> lovPeriodicidades;
   private List<Periodicidades> filtrarLovPeriodicidades;
   private Periodicidades periodicidadLovSeleccionado;
   //////////////Otros////////////////Otros////////////////////
   private boolean aceptar;
   private boolean guardado;
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
   private String infoRegistroLovContrato, infoRegistroLovPeriodicidad, infoRegistroLovTercero;
   //
   private boolean activoDetalle, activarLOV;

   public ControlFormulaContrato() {
      activoDetalle = true;
      altoTabla = "305";
      formulaActual = new Formulas();
      listFormulasContratos = null;
      fechaParametro = new Date(1, 1, 0);
      permitirIndexFormulaContrato = true;

      lovContratos = null;
      lovTerceros = null;
      lovPeriodicidades = null;

      contratoLovSeleccionado = new Contratos();
      terceroLovSeleccionada = new Terceros();
      periodicidadLovSeleccionado = new Periodicidades();

      nombreExportar = "";
      nombreTablaRastro = "";
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";

      aceptar = true;
      k = 0;

      listFormulasContratosBorrar = new ArrayList<Formulascontratos>();
      listFormulasContratosCrear = new ArrayList<Formulascontratos>();
      listFormulasContratosModificar = new ArrayList<Formulascontratos>();
      editarFormulaContrato = new Formulascontratos();
      cualCelda = -1;
      tipoLista = 0;
      guardado = true;
      nuevaFormulaContrato = new Formulascontratos();
      nuevaFormulaContrato.setPeriodicidad(new Periodicidades());
      nuevaFormulaContrato.setTercero(new Terceros());
      nuevaFormulaContrato.setContrato(new Contratos());
      formulaTablaSeleccionada = null;
      banderaFormulaContrato = 0;
      nombreTabla = ":formExportarFormula:datosFormulaContratosExportar";
      nombreXML = "FormulaContrato_XML";
      duplicarFormulaContrato = new Formulascontratos();
      cambiosFormulaContrato = false;
      activarLOV = true;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarFormulaContrato.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirFormula(BigInteger secuencia) {
      formulaActual = administrarFormulaContrato.actualFormula(secuencia);
      listFormulasContratos = null;
      getListFormulasContratos();
      contarRegistros();
      anularBotonLOV();
   }

   public void modificarFormulaContrato(Formulascontratos fc) {
      formulaTablaSeleccionada = fc;
      if (!listFormulasContratosCrear.contains(formulaTablaSeleccionada)) {
         if (listFormulasContratosModificar.isEmpty()) {
            listFormulasContratosModificar.add(formulaTablaSeleccionada);
         } else if (!listFormulasContratosModificar.contains(formulaTablaSeleccionada)) {
            listFormulasContratosModificar.add(formulaTablaSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
      cambiosFormulaContrato = true;
   }

   public void modificarFormulaContrato(Formulascontratos fc, String confirmarCambio, String valorConfirmar) {
      formulaTablaSeleccionada = fc;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("LEGISLACION")) {
         formulaTablaSeleccionada.getContrato().setDescripcion(legislacion);
         for (int i = 0; i < lovContratos.size(); i++) {
            if (lovContratos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaTablaSeleccionada.setContrato(lovContratos.get(indiceUnicoElemento));
            lovContratos.clear();
            getLovContratos();
         } else {
            contarRegistrosLovContratos(0);
            permitirIndexFormulaContrato = false;
            RequestContext.getCurrentInstance().update("form:ContratoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ContratoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
         formulaTablaSeleccionada.getPeriodicidad().setNombre(periodicidad);
         for (int i = 0; i < lovPeriodicidades.size(); i++) {
            if (lovPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaTablaSeleccionada.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
            lovPeriodicidades.clear();
            getLovPeriodicidades();
         } else {
            contarRegistrosLovPeriod(0);
            permitirIndexFormulaContrato = false;
            RequestContext.getCurrentInstance().update("form:PeriodicidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
         if (!valorConfirmar.isEmpty()) {
            formulaTablaSeleccionada.getTercero().setNombre(tercero);
            for (int i = 0; i < lovTerceros.size(); i++) {
               if (lovTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               formulaTablaSeleccionada.setTercero(lovTerceros.get(indiceUnicoElemento));
               lovTerceros.clear();
               getLovTerceros();
            } else {
               contarRegistrosLovTerceros(0);
               permitirIndexFormulaContrato = false;
               RequestContext.getCurrentInstance().update("form:TerceroDialogo");
               RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            lovTerceros.clear();
            getLovTerceros();
            formulaTablaSeleccionada.setTercero(new Terceros());
            coincidencias = 1;
         }
      }
      if (coincidencias == 1) {
         if (!listFormulasContratosCrear.contains(formulaTablaSeleccionada)) {
            if (listFormulasContratosModificar.isEmpty()) {
               listFormulasContratosModificar.add(formulaTablaSeleccionada);
            } else if (!listFormulasContratosModificar.contains(formulaTablaSeleccionada)) {
               listFormulasContratosModificar.add(formulaTablaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         activoDetalle = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
         cambiosFormulaContrato = true;
      }
      RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
   }

   ///////////////////////////////////////////////////////////////////////////
   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("LEGISLACION")) {
         if (tipoNuevo == 1) {
            legislacion = nuevaFormulaContrato.getContrato().getDescripcion();
         } else if (tipoNuevo == 2) {
            legislacion = duplicarFormulaContrato.getContrato().getDescripcion();
         }
      } else if (Campo.equals("PERIODICIDAD")) {
         if (tipoNuevo == 1) {
            periodicidad = nuevaFormulaContrato.getPeriodicidad().getNombre();
         } else if (tipoNuevo == 2) {
            periodicidad = duplicarFormulaContrato.getPeriodicidad().getNombre();
         }
      } else if (Campo.equals("TERCERO")) {
         if (tipoNuevo == 1) {
            tercero = nuevaFormulaContrato.getTercero().getNombre();
         } else if (tipoNuevo == 2) {
            tercero = duplicarFormulaContrato.getTercero().getNombre();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoFormulaContrato(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("LEGISLACION")) {
         if (tipoNuevo == 1) {
            nuevaFormulaContrato.getContrato().setDescripcion(legislacion);
         } else if (tipoNuevo == 2) {
            duplicarFormulaContrato.getContrato().setDescripcion(legislacion);
         }
         for (int i = 0; i < lovContratos.size(); i++) {
            if (lovContratos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulaContrato.setContrato(lovContratos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaLegislacion");
            } else if (tipoNuevo == 2) {
               duplicarFormulaContrato.setContrato(lovContratos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarLegislacion");
            }
            lovContratos.clear();
            getLovContratos();
         } else {
            contarRegistrosLovContratos(0);
            RequestContext.getCurrentInstance().update("form:ContratoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ContratoDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaLegislacion");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarLegislacion");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
         if (tipoNuevo == 1) {
            nuevaFormulaContrato.getPeriodicidad().setNombre(periodicidad);
         } else if (tipoNuevo == 2) {
            duplicarFormulaContrato.getPeriodicidad().setNombre(periodicidad);
         }
         for (int i = 0; i < lovPeriodicidades.size(); i++) {
            if (lovPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaFormulaContrato.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidad");
            } else if (tipoNuevo == 2) {
               duplicarFormulaContrato.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidad");
            }
            lovPeriodicidades.clear();
            getLovPeriodicidades();
         } else {
            contarRegistrosLovPeriod(0);
            RequestContext.getCurrentInstance().update("form:PeriodicidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidad");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidad");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevaFormulaContrato.getTercero().setNombre(tercero);
            } else if (tipoNuevo == 2) {
               duplicarFormulaContrato.getTercero().setNombre(tercero);
            }
            for (int i = 0; i < lovTerceros.size(); i++) {
               if (lovTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevaFormulaContrato.setTercero(lovTerceros.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTercero");
               } else if (tipoNuevo == 2) {
                  duplicarFormulaContrato.setTercero(lovTerceros.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
               }
               lovTerceros.clear();
               getLovTerceros();
            } else {
               contarRegistrosLovTerceros(0);
               RequestContext.getCurrentInstance().update("form:TerceroDialogo");
               RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTercero");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
               }
            }
         } else {
            lovTerceros.clear();
            getLovTerceros();
            if (tipoNuevo == 1) {
               nuevaFormulaContrato.setTercero(new Terceros());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTercero");
            } else if (tipoNuevo == 2) {
               duplicarFormulaContrato.setTercero(new Terceros());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
            }
         }
      }
   }

   public void cambiarIndiceFormulaContrato(Formulascontratos fc, int celda) {
      if (permitirIndexFormulaContrato == true) {
         cualCelda = celda;
         if (cualCelda >= 2 && cualCelda <= 4) {
            activarBotonLOV();
         } else {
            anularBotonLOV();
         }
         formulaTablaSeleccionada = fc;
         activoDetalle = false;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
         ///////// Captura Objetos Para Campos NotNull ///////////
         fechaIni = formulaTablaSeleccionada.getFechainicial();
         legislacion = formulaTablaSeleccionada.getContrato().getDescripcion();
         tercero = formulaTablaSeleccionada.getTercero().getNombre();
         periodicidad = formulaTablaSeleccionada.getPeriodicidad().getNombre();
      }
   }

   public boolean validarFechasRegistroFormulas(int i) {
      boolean retorno = false;
      if (i == 0) {
         Formulascontratos auxiliar = formulaTablaSeleccionada;
         if (auxiliar.getFechainicial() != null && auxiliar.getFechafinal() != null) {
            if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
               retorno = true;
            }
         }
         if (auxiliar.getFechainicial() != null && auxiliar.getFechafinal() == null) {
            if (auxiliar.getFechainicial().after(fechaParametro)) {
               retorno = true;
            }
         }
      }
      if (i == 1) {
         if (nuevaFormulaContrato.getFechainicial() != null && nuevaFormulaContrato.getFechafinal() != null) {
            if (nuevaFormulaContrato.getFechainicial().after(fechaParametro) && (nuevaFormulaContrato.getFechainicial().before(nuevaFormulaContrato.getFechafinal()))) {
               retorno = true;
            }
         }
         if (nuevaFormulaContrato.getFechainicial() != null && nuevaFormulaContrato.getFechafinal() == null) {
            if (nuevaFormulaContrato.getFechainicial().after(fechaParametro)) {
               retorno = true;
            }
         }
      }
      if (i == 2) {

         if (duplicarFormulaContrato.getFechainicial() != null && duplicarFormulaContrato.getFechafinal() != null) {
            if (duplicarFormulaContrato.getFechainicial().after(fechaParametro) && (duplicarFormulaContrato.getFechainicial().before(duplicarFormulaContrato.getFechafinal()))) {
               retorno = true;
            }
         }
         if (duplicarFormulaContrato.getFechainicial() != null && duplicarFormulaContrato.getFechafinal() == null) {
            if (duplicarFormulaContrato.getFechainicial().after(fechaParametro)) {
               retorno = true;
            }
         }
      }
      return retorno;
   }

   public void modificacionesFechaFormula(Formulascontratos fc, int c) {
      formulaTablaSeleccionada = fc;
      if ((formulaTablaSeleccionada.getFechainicial() != null)) {
         boolean validacion = validarFechasRegistroFormulas(0);
         if (validacion == true) {
            cambiarIndiceFormulaContrato(formulaTablaSeleccionada, c);
            modificarFormulaContrato(formulaTablaSeleccionada);
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         } else {
            System.out.println("Error de fechas de ingreso");
            RequestContext context = RequestContext.getCurrentInstance();
            formulaTablaSeleccionada.setFechainicial(fechaIni);
            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
            RequestContext.getCurrentInstance().execute("PF('errorFechasFC').show()");
            activoDetalle = true;
            RequestContext.getCurrentInstance().update("form:detalleFormula");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         formulaTablaSeleccionada.setFechainicial(fechaIni);
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
         activoDetalle = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
      }
   }

   //GUARDAR
   public void guardadoGeneral() {
      if (cambiosFormulaContrato == true) {
         guardarCambiosFormula();
      }
   }

   public void guardarCambiosFormula() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (!listFormulasContratosBorrar.isEmpty()) {
            administrarFormulaContrato.borrarFormulasContratos(listFormulasContratosBorrar);
            listFormulasContratosBorrar.clear();
         }
         if (!listFormulasContratosCrear.isEmpty()) {
            administrarFormulaContrato.crearFormulasContratos(listFormulasContratosCrear);
            listFormulasContratosCrear.clear();
         }
         if (!listFormulasContratosModificar.isEmpty()) {
            administrarFormulaContrato.editarFormulasContratos(listFormulasContratosModificar);
            listFormulasContratosModificar.clear();
         }
         listFormulasContratos = null;
         getListFormulasContratos();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         k = 0;
         activoDetalle = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
         formulaTablaSeleccionada = null;
         cambiosFormulaContrato = false;
         FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void restaurarTabla() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaFormulaContrato == 1) {
         altoTabla = "305";
         formulaFechaInicial = (Column) c.getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
         formulaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         formulaFechaFinal = (Column) c.getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
         formulaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         formulaLegislacion = (Column) c.getViewRoot().findComponent("form:datosFormulaContrato:formulaLegislacion");
         formulaLegislacion.setFilterStyle("display: none; visibility: hidden;");
         formulaPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
         formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
         formulaTercero = (Column) c.getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
         formulaTercero.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         banderaFormulaContrato = 0;
         filtrarListFormulasContratos = null;
         tipoLista = 0;
      }
   }

   //CANCELAR MODIFICACIONES
   public void cancelarModificacion() {
      restaurarTabla();
      anularBotonLOV();
      listFormulasContratosBorrar.clear();
      listFormulasContratosCrear.clear();
      listFormulasContratosModificar.clear();
      formulaTablaSeleccionada = null;
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      k = 0;
      listFormulasContratos = null;
      getListFormulasContratos();
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      cambiosFormulaContrato = false;
      permitirIndexFormulaContrato = true;
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (formulaTablaSeleccionada != null) {
         if (cualCelda == 2) {
            contarRegistrosLovContratos(0);
            RequestContext.getCurrentInstance().update("form:ContratoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ContratoDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 3) {
            contarRegistrosLovPeriod(0);
            RequestContext.getCurrentInstance().update("form:PeriodicidadDialogo");
            RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').show()");
            tipoActualizacion = 0;
         }
         if (cualCelda == 4) {
            contarRegistrosLovTerceros(0);
            RequestContext.getCurrentInstance().update("form:TerceroDialogo");
            RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndex(Formulascontratos fc, int dlg, int LND) {
      RequestContext context = RequestContext.getCurrentInstance();
      formulaTablaSeleccionada = fc;
      tipoActualizacion = LND;
      activarBotonLOV();
      if (dlg == 0) {
         contarRegistrosLovContratos(0);
         RequestContext.getCurrentInstance().update("form:ContratoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ContratoDialogo').show()");
      } else if (dlg == 1) {
         contarRegistrosLovPeriod(0);
         RequestContext.getCurrentInstance().update("form:PeriodicidadDialogo");
         RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').show()");
      } else if (dlg == 2) {
         contarRegistrosLovTerceros(0);
         RequestContext.getCurrentInstance().update("form:TerceroDialogo");
         RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
      }
   }

   public void asignarIndex(int dlg, int LND) {
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = LND;
      activarBotonLOV();
      if (dlg == 0) {
         contarRegistrosLovContratos(0);
         RequestContext.getCurrentInstance().update("form:ContratoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ContratoDialogo').show()");
      } else if (dlg == 1) {
         contarRegistrosLovPeriod(0);
         RequestContext.getCurrentInstance().update("form:PeriodicidadDialogo");
         RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').show()");
      } else if (dlg == 2) {
         contarRegistrosLovTerceros(0);
         RequestContext.getCurrentInstance().update("form:TerceroDialogo");
         RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
      }
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (formulaTablaSeleccionada != null) {
         editarFormulaContrato = formulaTablaSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialFD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialFD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalFD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalFD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarContratoFD");
            RequestContext.getCurrentInstance().execute("PF('editarContratoFD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPerdiodicidadFD");
            RequestContext.getCurrentInstance().execute("PF('editarPerdiodicidadFD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroFD");
            RequestContext.getCurrentInstance().execute("PF('editarTerceroFD').show()");
            cualCelda = -1;
         }
         activoDetalle = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void ingresoNuevoRegistro() {
      validarIngresoNuevaFormula();
   }

   public void validarIngresoNuevaFormula() {
      RequestContext context = RequestContext.getCurrentInstance();
      limpiarNuevoFormula();
      RequestContext.getCurrentInstance().update("form:nuevaF");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFormula");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormula').show()");
   }

   public void validarDuplicadoRegistro() {
      if (formulaTablaSeleccionada != null) {
         duplicarFormulaM();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void validarBorradoRegistro() {
      if (formulaTablaSeleccionada != null) {
         borrarFormula();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public boolean validarNuevosDatosFormula(int i) {
      boolean retorno = false;
      if (i == 1) {
         if ((nuevaFormulaContrato.getContrato().getSecuencia() != null)
                 && (nuevaFormulaContrato.getPeriodicidad().getSecuencia() != null)
                 && (nuevaFormulaContrato.getFechainicial() != null)) {
            return true;
         }
      }
      if (i == 2) {
         if ((duplicarFormulaContrato.getContrato().getSecuencia() != null)
                 && (duplicarFormulaContrato.getPeriodicidad().getSecuencia() != null)
                 && (duplicarFormulaContrato.getFechainicial() != null)) {
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
            restaurarTabla();
            k++;

            BigInteger var = BigInteger.valueOf(k);
            nuevaFormulaContrato.setSecuencia(var);
            nuevaFormulaContrato.setFormula(formulaActual);
            listFormulasContratosCrear.add(nuevaFormulaContrato);
            listFormulasContratos.add(nuevaFormulaContrato);
            formulaTablaSeleccionada = listFormulasContratos.get(listFormulasContratos.indexOf(nuevaFormulaContrato));
            nuevaFormulaContrato = new Formulascontratos();
            nuevaFormulaContrato.setPeriodicidad(new Periodicidades());
            nuevaFormulaContrato.setTercero(new Terceros());
            nuevaFormulaContrato.setContrato(new Contratos());
            RequestContext context = RequestContext.getCurrentInstance();
            contarRegistros();
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormula').hide()");
            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFormula");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosFormulaContrato = true;
            activoDetalle = true;
            RequestContext.getCurrentInstance().update("form:detalleFormula");
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasFC').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarNuevoFormula() {
      nuevaFormulaContrato = new Formulascontratos();
      nuevaFormulaContrato.setPeriodicidad(new Periodicidades());
      nuevaFormulaContrato.setTercero(new Terceros());
      nuevaFormulaContrato.setContrato(new Contratos());
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
   }

   public void duplicarFormulaM() {
      if (formulaTablaSeleccionada != null) {
         duplicarFormulaContrato = new Formulascontratos();
         duplicarFormulaContrato.setContrato(formulaTablaSeleccionada.getContrato());
         duplicarFormulaContrato.setFechafinal(formulaTablaSeleccionada.getFechafinal());
         duplicarFormulaContrato.setFechainicial(formulaTablaSeleccionada.getFechainicial());
         duplicarFormulaContrato.setPeriodicidad(formulaTablaSeleccionada.getPeriodicidad());
         duplicarFormulaContrato.setTercero(formulaTablaSeleccionada.getTercero());
         if (duplicarFormulaContrato.getTercero() == null) {
            duplicarFormulaContrato.setTercero(new Terceros());
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroFormula");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormula').show()");
      }
   }

   public void confirmarDuplicarFormula() {
      boolean resp = validarNuevosDatosFormula(2);
      if (resp == true) {
         boolean validacion = validarFechasRegistroFormulas(2);
         if (validacion == true) {
            RequestContext context = RequestContext.getCurrentInstance();
            restaurarTabla();
            k++;
            BigInteger var = BigInteger.valueOf(k);

            duplicarFormulaContrato.setSecuencia(var);
            duplicarFormulaContrato.setFormula(formulaActual);
            listFormulasContratosCrear.add(duplicarFormulaContrato);
            listFormulasContratos.add(duplicarFormulaContrato);
            formulaTablaSeleccionada = listFormulasContratos.get(listFormulasContratos.indexOf(duplicarFormulaContrato));
            duplicarFormulaContrato = new Formulascontratos();

            duplicarFormulaContrato = new Formulascontratos();
            duplicarFormulaContrato.setPeriodicidad(new Periodicidades());
            duplicarFormulaContrato.setTercero(new Terceros());
            duplicarFormulaContrato.setContrato(new Contratos());
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormula').hide()");
            cambiosFormulaContrato = true;
            activoDetalle = true;
            RequestContext.getCurrentInstance().update("form:detalleFormula");

         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechasFC').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarDuplicarFormula() {
      duplicarFormulaContrato = new Formulascontratos();
      duplicarFormulaContrato.setPeriodicidad(new Periodicidades());
      duplicarFormulaContrato.setTercero(new Terceros());
      duplicarFormulaContrato.setContrato(new Contratos());
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
   }

   ///////////////////////////////////////////////////////////////
   public void borrarFormula() {
      if (formulaTablaSeleccionada != null) {
         if (!listFormulasContratosModificar.isEmpty() && listFormulasContratosModificar.contains(formulaTablaSeleccionada)) {
            int modIndex = listFormulasContratosModificar.indexOf(formulaTablaSeleccionada);
            listFormulasContratosModificar.remove(modIndex);
            listFormulasContratosBorrar.add(formulaTablaSeleccionada);
         } else if (!listFormulasContratosCrear.isEmpty() && listFormulasContratosCrear.contains(formulaTablaSeleccionada)) {
            int crearIndex = listFormulasContratosCrear.indexOf(formulaTablaSeleccionada);
            listFormulasContratosCrear.remove(crearIndex);
         } else {
            listFormulasContratosBorrar.add(formulaTablaSeleccionada);
         }
         listFormulasContratos.remove(formulaTablaSeleccionada);
         if (tipoLista == 1) {
            filtrarListFormulasContratos.remove(formulaTablaSeleccionada);
         }
         anularBotonLOV();
         RequestContext context = RequestContext.getCurrentInstance();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         activoDetalle = true;
         RequestContext.getCurrentInstance().update("form:detalleFormula");
         formulaTablaSeleccionada = null;
         cambiosFormulaContrato = true;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      anularBotonLOV();
      if (banderaFormulaContrato == 0) {
         altoTabla = "285";
         formulaFechaInicial = (Column) c.getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaInicial");
         formulaFechaInicial.setFilterStyle("width: 85% !important;");
         formulaFechaFinal = (Column) c.getViewRoot().findComponent("form:datosFormulaContrato:formulaFechaFinal");
         formulaFechaFinal.setFilterStyle("width: 85% !important;");
         formulaLegislacion = (Column) c.getViewRoot().findComponent("form:datosFormulaContrato:formulaLegislacion");
         formulaLegislacion.setFilterStyle("width: 85% !important");
         formulaPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulaContrato:formulaPeriodicidad");
         formulaPeriodicidad.setFilterStyle("width: 85% !important");
         formulaTercero = (Column) c.getViewRoot().findComponent("form:datosFormulaContrato:formulaTercero");
         formulaTercero.setFilterStyle("width: 85% !important");

         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
         banderaFormulaContrato = 1;
      } else {
         restaurarTabla();
      }
   }

   public void salir() {
      restaurarTabla();
      listFormulasContratosBorrar.clear();
      listFormulasContratosCrear.clear();
      listFormulasContratosModificar.clear();
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      formulaTablaSeleccionada = null;
      formulaActual = null;
      k = 0;
      listFormulasContratos = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      cambiosFormulaContrato = false;
      nuevaFormulaContrato = new Formulascontratos();
      duplicarFormulaContrato = new Formulascontratos();
      lovPeriodicidades = null;
      lovTerceros = null;
      lovContratos = null;
   }

   public void actualizarContrato() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         formulaTablaSeleccionada.setContrato(contratoLovSeleccionado);
         if (!listFormulasContratosCrear.contains(formulaTablaSeleccionada)) {
            if (listFormulasContratosModificar.isEmpty()) {
               listFormulasContratosModificar.add(formulaTablaSeleccionada);
            } else if (!listFormulasContratosModificar.contains(formulaTablaSeleccionada)) {
               listFormulasContratosModificar.add(formulaTablaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexFormulaContrato = true;
         cambiosFormulaContrato = true;
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
      } else if (tipoActualizacion == 1) {
         nuevaFormulaContrato.setContrato(contratoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaLegislacion");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaContrato.setContrato(contratoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarLegislacion");
      }
      filtrarLovContratos = null;
      contratoLovSeleccionado = null;
      aceptar = true;
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:ContratoDialogo");
      RequestContext.getCurrentInstance().update("form:lovContrato");
      RequestContext.getCurrentInstance().update("form:aceptarC");
      context.reset("form:lovContrato:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovContrato').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ContratoDialogo').hide()");
   }

   public void cancelarCambioContrato() {
      filtrarLovContratos = null;
      contratoLovSeleccionado = null;
      aceptar = true;
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      tipoActualizacion = -1;
      permitirIndexFormulaContrato = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ContratoDialogo");
      RequestContext.getCurrentInstance().update("form:lovContrato");
      RequestContext.getCurrentInstance().update("form:aceptarC");
      context.reset("form:lovContrato:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovContrato').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ContratoDialogo').hide()");
   }

   public void actualizarTercero() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         formulaTablaSeleccionada.setTercero(terceroLovSeleccionada);
         if (!listFormulasContratosCrear.contains(formulaTablaSeleccionada)) {
            if (listFormulasContratosModificar.isEmpty()) {
               listFormulasContratosModificar.add(formulaTablaSeleccionada);
            } else if (!listFormulasContratosModificar.contains(formulaTablaSeleccionada)) {
               listFormulasContratosModificar.add(formulaTablaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexFormulaContrato = true;
         cambiosFormulaContrato = true;
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
      } else if (tipoActualizacion == 1) {
         nuevaFormulaContrato.setTercero(terceroLovSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTercero");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaContrato.setTercero(terceroLovSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
      }
      filtrarLovTerceros = null;
      terceroLovSeleccionada = null;
      aceptar = true;
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:TerceroDialogo");
      RequestContext.getCurrentInstance().update("form:lovTercero");
      RequestContext.getCurrentInstance().update("form:aceptarT");
      context.reset("form:lovTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
   }

   public void cancelarCambioTercero() {
      filtrarLovTerceros = null;
      terceroLovSeleccionada = null;
      aceptar = true;
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      tipoActualizacion = -1;
      permitirIndexFormulaContrato = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:TerceroDialogo");
      RequestContext.getCurrentInstance().update("form:lovTercero");
      RequestContext.getCurrentInstance().update("form:aceptarT");
      context.reset("form:lovTercero:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTercero').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').hide()");
   }

   public void actualizarPeriodicidad() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         formulaTablaSeleccionada.setPeriodicidad(periodicidadLovSeleccionado);
         if (!listFormulasContratosCrear.contains(formulaTablaSeleccionada)) {
            if (listFormulasContratosModificar.isEmpty()) {
               listFormulasContratosModificar.add(formulaTablaSeleccionada);
            } else if (!listFormulasContratosModificar.contains(formulaTablaSeleccionada)) {
               listFormulasContratosModificar.add(formulaTablaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndexFormulaContrato = true;
         cambiosFormulaContrato = true;
         RequestContext.getCurrentInstance().update("form:datosFormulaContrato");
      } else if (tipoActualizacion == 1) {
         nuevaFormulaContrato.setPeriodicidad(periodicidadLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaPeriodicidad");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaContrato.setPeriodicidad(periodicidadLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidad");
      }
      filtrarLovPeriodicidades = null;
      periodicidadLovSeleccionado = null;
      aceptar = true;
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:PeriodicidadDialogo");
      RequestContext.getCurrentInstance().update("form:lovPeriodicidad");
      RequestContext.getCurrentInstance().update("form:aceptarP");
      context.reset("form:lovPeriodicidad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPeriodicidad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').hide()");
   }

   public void cancelarCambioPeriodicidad() {
      filtrarLovPeriodicidades = null;
      periodicidadLovSeleccionado = null;
      aceptar = true;
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      tipoActualizacion = -1;
      permitirIndexFormulaContrato = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:PeriodicidadDialogo");
      RequestContext.getCurrentInstance().update("form:lovPeriodicidad");
      RequestContext.getCurrentInstance().update("form:aceptarP");
      context.reset("form:lovPeriodicidad:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPeriodicidad').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('PeriodicidadDialogo').hide()");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public String exportXML() {
      nombreTabla = ":formExportarFormula:datosFormulaContratosExportar";
      nombreXML = "FormulaContrato_XML";
      return nombreTabla;
   }

   /**
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      nombreTabla = ":formExportarFormula:datosFormulaContratosExportar";
      nombreExportar = "FormulaContrato_PDF";
      exportPDF_Tabla();
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
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
      nombreTabla = ":formExportarFormula:datosFormulaContratosExportar";
      nombreExportar = "FormulaContrato_XLS";
      exportXLS_Tabla();
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
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

   //Contar Registros :
   public void contarRegistros() {
      if (tipoLista == 1) {
         infoRegistro = String.valueOf(filtrarListFormulasContratos.size());
      } else if (listFormulasContratos != null) {
         infoRegistro = String.valueOf(listFormulasContratos.size());
      } else {
         infoRegistro = String.valueOf(0);
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLovContratos(int tipoLov) {
      if (tipoLov == 1) {
         infoRegistroLovContrato = String.valueOf(filtrarLovContratos.size());
      } else if (lovContratos != null) {
         infoRegistroLovContrato = String.valueOf(lovContratos.size());
      } else {
         infoRegistroLovContrato = String.valueOf(0);
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroContrato");
   }

   public void contarRegistrosLovPeriod(int tipoLov) {
      if (tipoLov == 1) {
         infoRegistroLovPeriodicidad = String.valueOf(filtrarLovPeriodicidades.size());
      } else if (lovPeriodicidades != null) {
         infoRegistroLovPeriodicidad = String.valueOf(lovPeriodicidades.size());
      } else {
         infoRegistroLovPeriodicidad = String.valueOf(0);
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroPeriodicidad");
   }

   public void contarRegistrosLovTerceros(int tipoLov) {
      if (tipoLov == 1) {
         infoRegistroLovTercero = String.valueOf(filtrarLovTerceros.size());
      } else if (lovTerceros != null) {
         infoRegistroLovTercero = String.valueOf(lovTerceros.size());
      } else {
         infoRegistroLovTercero = String.valueOf(0);
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroTercero");
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista real a la filtrada
    */
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
      anularBotonLOV();
      formulaTablaSeleccionada = null;
   }

   public void verificarRastroTabla() {
      verificarRastroFormulaContrato();
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
   }

   public void verificarRastroFormulaContrato() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (formulaTablaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(formulaTablaSeleccionada.getSecuencia(), "FORMULASCONTRATOS");
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "Formulascontratos";
            msnConfirmarRastro = "La tabla FORMULASCONTRATOS tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("FORMULASCONTRATOS")) {
         nombreTablaRastro = "Formulascontratos";
         msnConfirmarRastroHistorico = "La tabla FORMULASCONTRATOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      activoDetalle = true;
      RequestContext.getCurrentInstance().update("form:detalleFormula");
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   //GET - SET 
   public List<Formulascontratos> getListFormulasContratos() {
      try {
         if (listFormulasContratos == null) {
            if (formulaActual.getSecuencia() != null) {
               listFormulasContratos = administrarFormulaContrato.listFormulasContratosParaFormula(formulaActual.getSecuencia());
               if (listFormulasContratos != null) {
                  for (int i = 0; i < listFormulasContratos.size(); i++) {
                     if (listFormulasContratos.get(i).getTercero() == null) {
                        listFormulasContratos.get(i).setTercero(new Terceros());
                     }
                  }
               }
            }
         }
         return listFormulasContratos;
      } catch (Exception e) {
         System.out.println("Error getListFormulasContratos " + e.toString());
         return null;
      }
   }

   public void setListFormulasContratos(List<Formulascontratos> t) {
      this.listFormulasContratos = t;
   }

   public List<Formulascontratos> getFiltrarListFormulasContratos() {
      return filtrarListFormulasContratos;
   }

   public void setFiltrarListFormulasContratos(List<Formulascontratos> t) {
      this.filtrarListFormulasContratos = t;
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

   public List<Formulascontratos> getListFormulasContratosModificar() {
      return listFormulasContratosModificar;
   }

   public void setListFormulasContratosModificar(List<Formulascontratos> setListFormulasContratosModificar) {
      this.listFormulasContratosModificar = setListFormulasContratosModificar;
   }

   public Formulascontratos getNuevaFormulaContrato() {
      return nuevaFormulaContrato;
   }

   public void setNuevaFormulaContrato(Formulascontratos setNuevaFormulaContrato) {
      this.nuevaFormulaContrato = setNuevaFormulaContrato;
   }

   public List<Formulascontratos> getListFormulasContratosCrear() {
      return listFormulasContratosCrear;
   }

   public void setListFormulasContratosCrear(List<Formulascontratos> setListFormulasContratosCrear) {
      this.listFormulasContratosCrear = setListFormulasContratosCrear;
   }

   public List<Formulascontratos> getListFormulasContratosBorrar() {
      return listFormulasContratosBorrar;
   }

   public void setListFormulasContratosBorrar(List<Formulascontratos> setListFormulasContratosBorrar) {
      this.listFormulasContratosBorrar = setListFormulasContratosBorrar;
   }

   public Formulascontratos getEditarFormulaContrato() {
      return editarFormulaContrato;
   }

   public void setEditarFormulaContrato(Formulascontratos setEditarFormulaContrato) {
      this.editarFormulaContrato = setEditarFormulaContrato;
   }

   public Formulascontratos getDuplicarFormulaContrato() {
      return duplicarFormulaContrato;
   }

   public void setDuplicarFormulaContrato(Formulascontratos setDuplicarFormulaContrato) {
      this.duplicarFormulaContrato = setDuplicarFormulaContrato;
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

   public List<Contratos> getLovContratos() {
      if (lovContratos == null) {
         lovContratos = administrarFormulaContrato.listContratos();
      }
      return lovContratos;
   }

   public void setLovContratos(List<Contratos> setListContratos) {
      this.lovContratos = setListContratos;
   }

   public List<Contratos> getFiltrarLovContratos() {
      return filtrarLovContratos;
   }

   public void setFiltrarLovContratos(List<Contratos> setFiltrarListContratos) {
      this.filtrarLovContratos = setFiltrarListContratos;
   }

   public Contratos getContratoLovSeleccionado() {
      return contratoLovSeleccionado;
   }

   public void setContratoLovSeleccionado(Contratos setContratoSeleccionado) {
      this.contratoLovSeleccionado = setContratoSeleccionado;
   }

   public List<Terceros> getLovTerceros() {
      if (lovTerceros == null) {
         lovTerceros = administrarFormulaContrato.listTerceros();
      }
      return lovTerceros;
   }

   public void setLovTerceros(List<Terceros> setListTerceros) {
      this.lovTerceros = setListTerceros;
   }

   public List<Terceros> getFiltrarLovTerceros() {
      return filtrarLovTerceros;
   }

   public void setFiltrarLovTerceros(List<Terceros> setFiltrarListTerceros) {
      this.filtrarLovTerceros = setFiltrarListTerceros;
   }

   public Terceros getTerceroLovSeleccionada() {
      return terceroLovSeleccionada;
   }

   public void setTerceroLovSeleccionada(Terceros setTerceroSeleccionada) {
      this.terceroLovSeleccionada = setTerceroSeleccionada;
   }

   public List<Periodicidades> getLovPeriodicidades() {
      if (lovPeriodicidades == null) {
         lovPeriodicidades = administrarFormulaContrato.listPeriodicidades();
      }
      return lovPeriodicidades;
   }

   public void setLovPeriodicidades(List<Periodicidades> setListPeriodicidades) {
      this.lovPeriodicidades = setListPeriodicidades;
   }

   public List<Periodicidades> getFiltrarLovPeriodicidades() {
      return filtrarLovPeriodicidades;
   }

   public void setFiltrarLovPeriodicidades(List<Periodicidades> setFiltrarListPeriodicidades) {
      this.filtrarLovPeriodicidades = setFiltrarListPeriodicidades;
   }

   public Periodicidades getPeriodicidadSeleccionada() {
      return periodicidadLovSeleccionado;
   }

   public void setPeriodicidadSeleccionada(Periodicidades setPeriodicidadSeleccionada) {
      this.periodicidadLovSeleccionado = setPeriodicidadSeleccionada;
   }

   public Formulascontratos getFormulaTablaSeleccionada() {
      return formulaTablaSeleccionada;
   }

   public void setFormulaTablaSeleccionada(Formulascontratos formulaTablaSeleccionada) {
      this.formulaTablaSeleccionada = formulaTablaSeleccionada;
   }

   public Periodicidades getPeriodicidadLovSeleccionado() {
      return periodicidadLovSeleccionado;
   }

   public void setPeriodicidadLovSeleccionado(Periodicidades periodicidadSeleccionado) {
      this.periodicidadLovSeleccionado = periodicidadSeleccionado;
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

   public boolean isActivoDetalle() {
      return activoDetalle;
   }

   public void setActivoDetalle(boolean activoDetalle) {
      this.activoDetalle = activoDetalle;
   }

   public String getInfoRegistroLovContrato() {
      return infoRegistroLovContrato;
   }

   public void setInfoRegistroLovContrato(String infoRegistroLovContrato) {
      this.infoRegistroLovContrato = infoRegistroLovContrato;
   }

   public String getInfoRegistroLovPeriodicidad() {
      return infoRegistroLovPeriodicidad;
   }

   public void setInfoRegistroLovPeriodicidad(String infoRegistroLovPeriodicidad) {
      this.infoRegistroLovPeriodicidad = infoRegistroLovPeriodicidad;
   }

   public String getInfoRegistroLovTercero() {
      return infoRegistroLovTercero;
   }

   public void setInfoRegistroLovTercero(String infoRegistroLovTercero) {
      this.infoRegistroLovTercero = infoRegistroLovTercero;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

}
