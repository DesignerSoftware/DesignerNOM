/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.FormulasAseguradas;
import Entidades.Formulas;
import Entidades.Periodicidades;
import Entidades.Procesos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulasAseguradasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlFormulasAseguradas implements Serializable {

   @EJB
   AdministrarFormulasAseguradasInterface administrarFormulasAseguradas;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<FormulasAseguradas> listFormulasAseguradas;
   private List<FormulasAseguradas> filtrarFormulasAseguradas;
   private List<FormulasAseguradas> crearFormulasAseguradas;
   private List<FormulasAseguradas> modificarFormulasAseguradas;
   private List<FormulasAseguradas> borrarFormulasAseguradas;
   private FormulasAseguradas nuevoFormulaAsegurada;
   private FormulasAseguradas duplicarFormulaAsegurada;
   private FormulasAseguradas editarFormulasAseguradas;
   private FormulasAseguradas formulaAseguradaLovSeleccionada;
   private FormulasAseguradas formulaAseguradaSeleccionada;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column proceso, personafir, periodicidad;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private String infoRegistro;
   private String infoRegistroFormulas;
   private String infoRegistroProcesos;
   private String infoRegistroPeriocididades;
   private String infoRegistroFormulasAseguradas;
   //---------------------------------
   private String backupFormula;
   private List<Formulas> lovFormulas;
   private List<Formulas> filtradoLovFormulas;
   private Formulas formulaLovSeleccionado;
   private String nuevoYduplicarCompletarFormula;
   //--------------------------------------
   private String backupProceso;
   private List<Procesos> lovProcesos;
   private List<Procesos> filtradoLovProcesos;
   private Procesos procesoLovSeleccionado;
   private String nuevoYduplicarCompletarProcesos;
   //--------------------------------------
   //--------------------------------------
   private String backupPeriodicidad;
   private List<Periodicidades> lovPeriodicidades;
   private List<Periodicidades> filtradoLovPeriodicidades;
   private Periodicidades periodicidadLovSeleccionado;
   private String nuevoYduplicarCompletarPeriodicidad;
   //--------------------------------------
   private String nuevoYduplicarCompletarCargo;

   //---------------------------------
   private List<FormulasAseguradas> lovFormulasAseguradas;
   private List<FormulasAseguradas> filterlovFormulasAseguradas;

   private boolean buscarFormulas;
   private boolean mostrarTodos;
   private boolean cambioFormulasAseguradas = false;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlFormulasAseguradas() {
      lovFormulasAseguradas = null;
      listFormulasAseguradas = null;
      crearFormulasAseguradas = new ArrayList<FormulasAseguradas>();
      modificarFormulasAseguradas = new ArrayList<FormulasAseguradas>();
      borrarFormulasAseguradas = new ArrayList<FormulasAseguradas>();
      permitirIndex = true;
      editarFormulasAseguradas = new FormulasAseguradas();
      nuevoFormulaAsegurada = new FormulasAseguradas();
      nuevoFormulaAsegurada.setFormula(new Formulas());
      nuevoFormulaAsegurada.setProceso(new Procesos());
      nuevoFormulaAsegurada.setPeriodicidad(new Periodicidades());
      duplicarFormulaAsegurada = new FormulasAseguradas();
      duplicarFormulaAsegurada.setFormula(new Formulas());
      duplicarFormulaAsegurada.setProceso(new Procesos());
      duplicarFormulaAsegurada.setPeriodicidad(new Periodicidades());
      lovFormulas = null;
      filtradoLovFormulas = null;
      lovProcesos = null;
      filtradoLovProcesos = null;
      lovPeriodicidades = null;
      filtradoLovPeriodicidades = null;
      guardado = true;
//      tamano = 270;
      formulaAseguradaLovSeleccionada = new FormulasAseguradas();
      mostrarTodos = true;
      buscarFormulas = false;
      aceptar = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
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

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
         String pagActual = "formulasaseguradas";
         //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
         //mapParametros.put("paginaAnterior", pagActual);
         //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
         controlListaNavegacion.adicionarPagina(pagActual);
      }
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarFormulasAseguradas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      formulaAseguradaSeleccionada = null;
      contarRegistros();
   }

   public void cambiarIndice(FormulasAseguradas formulaA, int celda) {
      formulaAseguradaSeleccionada = formulaA;
      if (permitirIndex == true) {
         cualCelda = celda;
         if (cualCelda == 0) {
            backupFormula = formulaAseguradaSeleccionada.getFormula().getNombrelargo();
         }
         if (cualCelda == 1) {
            backupProceso = formulaAseguradaSeleccionada.getFormula().getNombrelargo();
            System.out.println("CONCEPTO : " + backupProceso);
         }
         if (cualCelda == 2) {
            backupPeriodicidad = formulaAseguradaSeleccionada.getPeriodicidad().getNombre();
            System.out.println("VALOR VOLUNTARIO : " + backupPeriodicidad);
         }

      }
   }

   public void asignarIndex(FormulasAseguradas formulaA, int LND, int dig) {
      formulaAseguradaSeleccionada = formulaA;
      try {
         System.out.println("\n ENTRE A ControlFormulasAseguradas.asignarIndex \n");
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dig == 0) {
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
            dig = -1;
         }
         if (dig == 1) {
            RequestContext.getCurrentInstance().update("form:procesosDialogo");
            RequestContext.getCurrentInstance().execute("PF('procesosDialogo').show()");
            dig = -1;
         }
         if (dig == 2) {
            RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
            dig = -1;
         }
      } catch (Exception e) {
         System.out.println("ERROR ControlFormulasAseguradas.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (formulaAseguradaSeleccionada != null) {
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }
         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("form:procesosDialogo");
            RequestContext.getCurrentInstance().execute("PF('procesosDialogo').show()");
         }
         if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
         }
      }
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         restaurarTabla();
      }

      borrarFormulasAseguradas.clear();
      crearFormulasAseguradas.clear();
      modificarFormulasAseguradas.clear();
      formulaAseguradaSeleccionada = null;
      k = 0;
      listFormulasAseguradas = null;
      guardado = true;
      permitirIndex = true;
      mostrarTodos = true;
      buscarFormulas = false;
      RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
      contarRegistros();
   }

   public void salir() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         restaurarTabla();
      }

      borrarFormulasAseguradas.clear();
      crearFormulasAseguradas.clear();
      modificarFormulasAseguradas.clear();
      formulaAseguradaSeleccionada = null;
      k = 0;
      listFormulasAseguradas = null;
      guardado = true;
      permitirIndex = true;
      mostrarTodos = true;
      buscarFormulas = false;
      RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
      contarRegistros();
   }

   public void cancelarModificacionCambio() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         restaurarTabla();
      }

      borrarFormulasAseguradas.clear();
      crearFormulasAseguradas.clear();
      modificarFormulasAseguradas.clear();
      formulaAseguradaSeleccionada = null;
      k = 0;
      listFormulasAseguradas = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void guardarFormulasCambios() {
      if (guardado == false) {
         System.out.println("Realizando guardarConceptosSoportes");
         if (!borrarFormulasAseguradas.isEmpty()) {
            administrarFormulasAseguradas.borrarFormulasAseguradas(borrarFormulasAseguradas);
            registrosBorrados = borrarFormulasAseguradas.size();
            borrarFormulasAseguradas.clear();
         }
         if (!modificarFormulasAseguradas.isEmpty()) {
            administrarFormulasAseguradas.modificarFormulasAseguradas(modificarFormulasAseguradas);
            modificarFormulasAseguradas.clear();
         }
         if (!crearFormulasAseguradas.isEmpty()) {
            administrarFormulasAseguradas.crearFormulasAseguradas(crearFormulasAseguradas);
            crearFormulasAseguradas.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listFormulasAseguradas = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         k = 0;
         guardado = true;
         llamadoDialogoBuscarFormulas();
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         personafir = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:personafir");
         personafir.setFilterStyle("width: 85% !important");
         proceso = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:proceso");
         proceso.setFilterStyle("width: 85% !important");
         periodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:periodicidad");
         periodicidad.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void restaurarTabla() {
      FacesContext c = FacesContext.getCurrentInstance();
      personafir = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:personafir");
      personafir.setFilterStyle("display: none; visibility: hidden;");
      proceso = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:proceso");
      proceso.setFilterStyle("display: none; visibility: hidden;");
      periodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:periodicidad");
      periodicidad.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
      bandera = 0;
      filtrarFormulasAseguradas = null;
      tipoLista = 0;
   }

   public void actualizarFormulas() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("formula seleccionado : " + formulaLovSeleccionado.getNombrelargo());
      System.out.println("tipo Actualizacion : " + tipoActualizacion);
      System.out.println("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         formulaAseguradaSeleccionada.setFormula(formulaLovSeleccionado);

         if (!crearFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
            if (modificarFormulasAseguradas.isEmpty()) {
               modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
            } else if (!modificarFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
               modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         System.out.println("ACTUALIZAR FORMULA : " + formulaLovSeleccionado.getNombrelargo());
         RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         System.out.println("ACTUALIZAR FORMULA NUEVO DEPARTAMENTO: " + formulaLovSeleccionado.getNombrelargo());
         nuevoFormulaAsegurada.setFormula(formulaLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      } else if (tipoActualizacion == 2) {
         System.out.println("ACTUALIZAR FORMULA DUPLICAR DEPARTAMENO: " + formulaLovSeleccionado.getNombrelargo());
         duplicarFormulaAsegurada.setFormula(formulaLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }
      filtradoLovFormulas = null;
      formulaLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      cambioFormulasAseguradas = true;
      context.reset("form:lovFormulas:globalFilter");
      context.update("form:lovFormulas");
      context.update("form:personasDialogo");
      context.update("form:aceptarForm");
      RequestContext.getCurrentInstance().execute("PF('lovFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
   }

   public void cancelarCambioFormulas() {
      filtradoLovFormulas = null;
      formulaLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
   }

   public void actualizarProcesos() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("formula seleccionado : " + procesoLovSeleccionado.getDescripcion());
      System.out.println("tipo Actualizacion : " + tipoActualizacion);
      System.out.println("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         formulaAseguradaSeleccionada.setProceso(procesoLovSeleccionado);

         if (!crearFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
            if (modificarFormulasAseguradas.isEmpty()) {
               modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
            } else if (!modificarFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
               modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         System.out.println("ACTUALIZAR FORMULA : " + procesoLovSeleccionado.getDescripcion());
         RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         System.out.println("ACTUALIZAR FORMULA NUEVO DEPARTAMENTO: " + procesoLovSeleccionado.getDescripcion());
         nuevoFormulaAsegurada.setProceso(procesoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProceso");
      } else if (tipoActualizacion == 2) {
         System.out.println("ACTUALIZAR FORMULA DUPLICAR DEPARTAMENO: " + procesoLovSeleccionado.getDescripcion());
         duplicarFormulaAsegurada.setProceso(procesoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProceso");
      }
      filtradoLovProcesos = null;
      procesoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      cambioFormulasAseguradas = true;
      context.reset("form:lovProcesos:globalFilter");
      context.update("form:lovProcesos");
      context.update("form:procesosDialogo");
      context.update("form:aceptarProce");
      RequestContext.getCurrentInstance().execute("PF('lovProcesos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('procesosDialogo').hide()");
   }

   public void cancelarCambioProcesos() {
      filtradoLovProcesos = null;
      procesoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovProcesos:globalFilter");
      context.update("form:lovProcesos");
      context.update("form:procesosDialogo");
      context.update("form:aceptarProce");
      RequestContext.getCurrentInstance().execute("PF('lovProcesos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('procesosDialogo').hide()");
   }

   public void actualizarPeriocidades() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("formula seleccionado : " + periodicidadLovSeleccionado.getNombre());
      System.out.println("tipo Actualizacion : " + tipoActualizacion);
      System.out.println("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         formulaAseguradaSeleccionada.setPeriodicidad(periodicidadLovSeleccionado);

         if (!crearFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
            if (modificarFormulasAseguradas.isEmpty()) {
               modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
            } else if (!modificarFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
               modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         System.out.println("ACTUALIZAR FORMULA : " + periodicidadLovSeleccionado.getNombre());
         RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         System.out.println("ACTUALIZAR FORMULA NUEVO DEPARTAMENTO: " + periodicidadLovSeleccionado.getNombre());
         nuevoFormulaAsegurada.setPeriodicidad(periodicidadLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPeriodicidad");
      } else if (tipoActualizacion == 2) {
         System.out.println("ACTUALIZAR FORMULA DUPLICAR DEPARTAMENO: " + periodicidadLovSeleccionado.getNombre());
         duplicarFormulaAsegurada.setPeriodicidad(periodicidadLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidad");
      }
      filtradoLovProcesos = null;
      procesoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      cambioFormulasAseguradas = true;
      context.reset("form:lovPeriodicidades:globalFilter");
      context.update("form:periodicidadesDialogo");
      context.update("form:lovPeriodicidades");
      context.update("form:aceptarPerio");
      RequestContext.getCurrentInstance().execute("PF('lovPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
   }

   public void cancelarCambioPeriodicidades() {
      filtradoLovPeriodicidades = null;
      periodicidadLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovPeriodicidades:globalFilter");
      context.update("form:periodicidadesDialogo");
      context.update("form:lovPeriodicidades");
      context.update("form:aceptarPerio");
      RequestContext.getCurrentInstance().execute("PF('lovPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
   }

   public void modificarFormulasAseguradas(FormulasAseguradas formulaA, String confirmarCambio, String valorConfirmar) {
      formulaAseguradaSeleccionada = formulaA;
      int coincidencias = 0;
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;
      boolean banderita2 = false;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         System.out.println("MODIFICANDO NORMA LABORAL : " + formulaAseguradaSeleccionada.getFormula().getNombrelargo());
         if (!formulaAseguradaSeleccionada.getFormula().getNombrelargo().equals("")) {
            formulaAseguradaSeleccionada.getFormula().setNombrelargo(backupFormula);

            for (int i = 0; i < lovFormulas.size(); i++) {
               if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               formulaAseguradaSeleccionada.setFormula(lovFormulas.get(indiceUnicoElemento));
               lovFormulas.clear();
               lovFormulas = null;
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupFormula != null) {
               formulaAseguradaSeleccionada.getFormula().setNombrelargo(backupFormula);
            }
            tipoActualizacion = 0;
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }
         if (coincidencias == 1) {
            if (!crearFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {

               if (modificarFormulasAseguradas.isEmpty()) {
                  modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
               } else if (!modificarFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
                  modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
         }
         RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
         System.out.println("MODIFICANDO NORMA LABORAL : " + formulaAseguradaSeleccionada.getProceso().getDescripcion());
         if (!formulaAseguradaSeleccionada.getProceso().getDescripcion().equals("")) {
            formulaAseguradaSeleccionada.getProceso().setDescripcion(backupProceso);

            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               formulaAseguradaSeleccionada.setProceso(lovProcesos.get(indiceUnicoElemento));
               lovProcesos.clear();
               lovProcesos = null;
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:procesosDialogo");
               RequestContext.getCurrentInstance().execute("PF('procesosDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupProceso != null) {
               formulaAseguradaSeleccionada.getProceso().setDescripcion(backupProceso);
            }
            tipoActualizacion = 0;
            System.out.println("PAIS ANTES DE MOSTRAR DIALOGO PERSONA : " + backupProceso);
            RequestContext.getCurrentInstance().update("form:procesosDialogo");
            RequestContext.getCurrentInstance().execute("PF('procesosDialogo').show()");
         }

         if (coincidencias == 1) {
            if (!crearFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
               if (modificarFormulasAseguradas.isEmpty()) {
                  modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
               } else if (!modificarFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
                  modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
         }
         RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
         System.out.println("MODIFICANDO NORMA LABORAL : " + formulaAseguradaSeleccionada.getProceso().getDescripcion());
         if (!formulaAseguradaSeleccionada.getPeriodicidad().getNombre().equals("")) {
            formulaAseguradaSeleccionada.getPeriodicidad().setNombre(backupPeriodicidad);

            for (int i = 0; i < lovPeriodicidades.size(); i++) {
               if (lovPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               formulaAseguradaSeleccionada.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               lovPeriodicidades.clear();
               lovPeriodicidades = null;
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            System.out.println("BackUpPeriodicidad : " + backupPeriodicidad);

            formulaAseguradaSeleccionada.getPeriodicidad().setNombre(backupPeriodicidad);
            formulaAseguradaSeleccionada.setPeriodicidad(new Periodicidades());
            coincidencias = 1;
            tipoActualizacion = 0;
         }

         if (coincidencias == 1) {
            if (!crearFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {

               if (modificarFormulasAseguradas.isEmpty()) {
                  modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
               } else if (!modificarFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
                  modificarFormulasAseguradas.add(formulaAseguradaSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
         }
         RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void borrandoFormulasAseguradas() {
      if (formulaAseguradaSeleccionada != null) {
         if (!modificarFormulasAseguradas.isEmpty() && modificarFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
            modificarFormulasAseguradas.remove(formulaAseguradaSeleccionada);
            borrarFormulasAseguradas.add(formulaAseguradaSeleccionada);
         } else if (!crearFormulasAseguradas.isEmpty() && crearFormulasAseguradas.contains(formulaAseguradaSeleccionada)) {
            crearFormulasAseguradas.remove(formulaAseguradaSeleccionada);
         } else {
            borrarFormulasAseguradas.add(formulaAseguradaSeleccionada);
         }
         listFormulasAseguradas.remove(formulaAseguradaSeleccionada);
         if (tipoLista == 1) {
            filtrarFormulasAseguradas.remove(formulaAseguradaSeleccionada);
         }

         formulaAseguradaSeleccionada = null;
         RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
         contarRegistros();

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         cambioFormulasAseguradas = true;
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
      System.out.println("1...");
      if (valorCambio.equals("FORMULA")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarFormula = nuevoFormulaAsegurada.getFormula().getNombrelargo();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarFormula = duplicarFormulaAsegurada.getFormula().getNombrelargo();
         }

         System.out.println("CONCEPTO : " + nuevoYduplicarCompletarFormula);
      }
      if (valorCambio.equals("PROCESO")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarProcesos = nuevoFormulaAsegurada.getProceso().getDescripcion();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarProcesos = duplicarFormulaAsegurada.getProceso().getDescripcion();
         }

         System.out.println("PROCESO : " + nuevoYduplicarCompletarProcesos);
      }
      if (valorCambio.equals("PERIODICIDAD")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarPeriodicidad = nuevoFormulaAsegurada.getPeriodicidad().getNombre();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarPeriodicidad = duplicarFormulaAsegurada.getPeriodicidad().getNombre();
         }

         System.out.println("PERIODICIDAD : " + nuevoYduplicarCompletarPeriodicidad);
      }

   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         System.out.println(" nueva Operando    Entro al if 'Centro costo'");
         System.out.println("NUEVO PERSONA :-------> " + nuevoFormulaAsegurada.getFormula().getNombrelargo());

         if (!nuevoFormulaAsegurada.getFormula().getNombrelargo().equals("")) {
            System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("valorConfirmar: " + valorConfirmar);
            System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarFormula);
            nuevoFormulaAsegurada.getFormula().setNombrelargo(nuevoYduplicarCompletarFormula);
            for (int i = 0; i < lovFormulas.size(); i++) {
               if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoFormulaAsegurada.setFormula(lovFormulas.get(indiceUnicoElemento));
               lovFormulas = null;
               System.err.println("PERSONA GUARDADA :-----> " + nuevoFormulaAsegurada.getFormula().getNombrelargo());
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoFormulaAsegurada.getFormula().setNombrelargo(nuevoYduplicarCompletarFormula);
            System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoFormulaAsegurada.setFormula(new Formulas());
            nuevoFormulaAsegurada.getFormula().setNombrelargo(" ");
            System.out.println("NUEVA NORMA LABORAL" + nuevoFormulaAsegurada.getFormula().getNombrelargo());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");

      } else if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
         System.out.println("NUEVO PROCESO :-------> " + nuevoFormulaAsegurada.getProceso().getDescripcion());

         if (!nuevoFormulaAsegurada.getProceso().getDescripcion().equals("")) {
            System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("valorConfirmar: " + valorConfirmar);
            System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarProcesos);
            nuevoFormulaAsegurada.getProceso().setDescripcion(nuevoYduplicarCompletarProcesos);
            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoFormulaAsegurada.setProceso(lovProcesos.get(indiceUnicoElemento));
               lovProcesos = null;
               System.err.println("PROCESO GUARDADO :-----> " + nuevoFormulaAsegurada.getProceso().getDescripcion());
            } else {
               RequestContext.getCurrentInstance().update("form:procesosDialogo");
               RequestContext.getCurrentInstance().execute("PF('procesosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoFormulaAsegurada.getProceso().setDescripcion(nuevoYduplicarCompletarProcesos);
            System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoFormulaAsegurada.setProceso(new Procesos());
            nuevoFormulaAsegurada.getProceso().setDescripcion(" ");
            System.out.println("PROCESOSOS : : : : :  " + nuevoFormulaAsegurada.getProceso().getDescripcion());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProceso");

      } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
         System.out.println("NUEVO PERIODICIDAD :-------> " + nuevoFormulaAsegurada.getPeriodicidad().getNombre());

         if (!nuevoFormulaAsegurada.getPeriodicidad().getNombre().equals("")) {
            System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("valorConfirmar: " + valorConfirmar);
            System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarPeriodicidad);
            nuevoFormulaAsegurada.getProceso().setDescripcion(nuevoYduplicarCompletarPeriodicidad);
            for (int i = 0; i < lovPeriodicidades.size(); i++) {
               if (lovPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoFormulaAsegurada.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               lovPeriodicidades = null;
               System.err.println("PROCESO GUARDADO :-----> " + nuevoFormulaAsegurada.getPeriodicidad().getNombre());
            } else {
               RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoFormulaAsegurada.setProceso(new Procesos());
            nuevoFormulaAsegurada.getProceso().setDescripcion(" ");
            System.out.println("PERIODICIDAD : : : : :  " + nuevoFormulaAsegurada.getProceso().getDescripcion());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPeriodicidad");

      }
   }

   public void asignarVariableFormulas(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:personasDialogo");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
   }

   public void asignarVariableProcesos(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:procesosDialogo");
      RequestContext.getCurrentInstance().execute("PF('procesosDialogo').show()");
   }

   public void asignarVariablePeriodicidades(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      System.out.println("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
         System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarFormula);

         if (!duplicarFormulaAsegurada.getFormula().getNombrelargo().equals("")) {
            System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
            System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarFormula);
            duplicarFormulaAsegurada.getFormula().setNombrelargo(nuevoYduplicarCompletarFormula);
            for (int i = 0; i < lovFormulas.size(); i++) {
               if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarFormulaAsegurada.setFormula(lovFormulas.get(indiceUnicoElemento));
               lovFormulas = null;
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            duplicarFormulaAsegurada.setFormula(new Formulas());
            duplicarFormulaAsegurada.getFormula().setNombrelargo(" ");

            System.out.println("DUPLICAR PERSONA  : " + duplicarFormulaAsegurada.getFormula().getNombrelargo());
            System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarFormula);
            formulaAseguradaSeleccionada.getFormula().setNombrelargo(nuevoYduplicarCompletarFormula);
            System.err.println("tipo lista" + tipoLista);
            System.err.println("Secuencia Parentesco " + formulaAseguradaSeleccionada.getFormula().getSecuencia());

         }

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      } else if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
         System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
         System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarProcesos);

         if (!duplicarFormulaAsegurada.getProceso().getDescripcion().equals("")) {
            System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
            System.out.println("DUPLICAR nuevoYduplicarCompletarProcesos: " + nuevoYduplicarCompletarProcesos);
            duplicarFormulaAsegurada.getProceso().setDescripcion(nuevoYduplicarCompletarProcesos);
            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarFormulaAsegurada.setProceso(lovProcesos.get(indiceUnicoElemento));
               lovFormulas = null;
            } else {
               RequestContext.getCurrentInstance().update("form:procesosDialogo");
               RequestContext.getCurrentInstance().execute("PF('procesosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            duplicarFormulaAsegurada.setProceso(new Procesos());
            duplicarFormulaAsegurada.getProceso().setDescripcion(" ");

            System.out.println("DUPLICAR PROCESO  : " + duplicarFormulaAsegurada.getProceso().getDescripcion());
            System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarProcesos);
            formulaAseguradaSeleccionada.getProceso().setDescripcion(nuevoYduplicarCompletarProcesos);
            //System.err.println("tipo lista" + tipoLista);
            //System.err.println("Secuencia Parentesco " + formulaAseguradaSeleccionada.getFormula().getSecuencia());
         }

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProceso");
      } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
         System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
         System.out.println("DUPLICAR PERIODICIDAD : " + nuevoYduplicarCompletarPeriodicidad);

         if (!duplicarFormulaAsegurada.getPeriodicidad().getNombre().equals("")) {
            System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
            System.out.println("DUPLICAR nuevoYduplicarCompletarProcesos: " + nuevoYduplicarCompletarPeriodicidad);
            duplicarFormulaAsegurada.getPeriodicidad().setNombre(nuevoYduplicarCompletarPeriodicidad);
            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarFormulaAsegurada.setPeriodicidad(lovPeriodicidades.get(indiceUnicoElemento));
               lovFormulas = null;
            } else {
               RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
               RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            duplicarFormulaAsegurada.setPeriodicidad(new Periodicidades());
            duplicarFormulaAsegurada.getPeriodicidad().setNombre(" ");

            System.out.println("DUPLICAR PERIODICIDAD  : " + duplicarFormulaAsegurada.getPeriodicidad().getNombre());
            System.out.println("nuevoYduplicarCompletarPERIODICIDAD : " + nuevoYduplicarCompletarPeriodicidad);
            formulaAseguradaSeleccionada.getPeriodicidad().setNombre(nuevoYduplicarCompletarPeriodicidad);
            //System.err.println("tipo lista" + tipoLista);
            //System.err.println("Secuencia Parentesco " + formulaAseguradaSeleccionada.getFormula().getSecuencia());
         }

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidad");
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarFormulasAseguradas.isEmpty() || !crearFormulasAseguradas.isEmpty() || !modificarFormulasAseguradas.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarFormulasAseguradas() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         System.out.println("Realizando guardarFormulasAseguradas");
         if (!borrarFormulasAseguradas.isEmpty()) {
            administrarFormulasAseguradas.borrarFormulasAseguradas(borrarFormulasAseguradas);
            registrosBorrados = borrarFormulasAseguradas.size();
            borrarFormulasAseguradas.clear();
         }
         if (!modificarFormulasAseguradas.isEmpty()) {
            administrarFormulasAseguradas.modificarFormulasAseguradas(modificarFormulasAseguradas);
            modificarFormulasAseguradas.clear();
         }
         if (!crearFormulasAseguradas.isEmpty()) {
            administrarFormulasAseguradas.crearFormulasAseguradas(crearFormulasAseguradas);
            crearFormulasAseguradas.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listFormulasAseguradas = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
         k = 0;
         guardado = true;

      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (formulaAseguradaSeleccionada != null) {
         editarFormulasAseguradas = formulaAseguradaSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
            RequestContext.getCurrentInstance().execute("PF('editPais').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFormulas");
            RequestContext.getCurrentInstance().execute("PF('editFormulas').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editOperandos");
            RequestContext.getCurrentInstance().execute("PF('editOperandos').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public boolean validarNuevoRepetido(FormulasAseguradas formulaA) {
      boolean continuar = true;
      for (int i = 0; i < listFormulasAseguradas.size(); i++) {
         if (listFormulasAseguradas.get(i).getFormula().getSecuencia().equals(formulaA.getFormula().getSecuencia())
                 && listFormulasAseguradas.get(i).getProceso().getSecuencia().equals(formulaA.getProceso().getSecuencia())) {
            continuar = false;
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaRepetidos').show()");
            break;
         }
      }
      return continuar;
   }

   public void agregarNuevoFormulasAseguradas() {
      int contador = 0;
      mensajeValidacion = " ";
      if (nuevoFormulaAsegurada.getFormula().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + " *Formula \n";
      } else {
         contador++;
      }
      if (nuevoFormulaAsegurada.getProceso().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + " *Proceso \n";
      } else {
         contador++;
      }
      System.out.println("nuevoFormulaAsegurada.getFormula() : " + nuevoFormulaAsegurada.getFormula());
      System.out.println("nuevoFormulaAsegurada.getProceso() : " + nuevoFormulaAsegurada.getProceso());
      System.out.println("agregarNuevoFormulasAseguradas() contador : " + contador);

      if (contador == 2) {
         if (validarNuevoRepetido(nuevoFormulaAsegurada)) {
            if (bandera == 1) {
               restaurarTabla();
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoFormulaAsegurada.setSecuencia(l);
            crearFormulasAseguradas.add(nuevoFormulaAsegurada);
            listFormulasAseguradas.add(nuevoFormulaAsegurada);
            formulaAseguradaSeleccionada = listFormulasAseguradas.get(listFormulasAseguradas.indexOf(nuevoFormulaAsegurada));

            nuevoFormulaAsegurada = new FormulasAseguradas();
            nuevoFormulaAsegurada.setFormula(new Formulas());
            nuevoFormulaAsegurada.setProceso(new Procesos());
            nuevoFormulaAsegurada.setPeriodicidad(new Periodicidades());
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            contarRegistros();
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFormulasAseguradas').hide()");
            cambioFormulasAseguradas = true;
         }
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaFormulaAse");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaFormulaAse').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoFormulasAseguradas() {
      System.out.println("limpiarNuevoFormulasAseguradas");
      nuevoFormulaAsegurada = new FormulasAseguradas();
      nuevoFormulaAsegurada.setFormula(new Formulas());
      nuevoFormulaAsegurada.setProceso(new Procesos());
      nuevoFormulaAsegurada.setPeriodicidad(new Periodicidades());
   }

   //------------------------------------------------------------------------------
   public void cargarNuevoFormulasAseguradas() {
      duplicarFormulaAsegurada = new FormulasAseguradas();
      duplicarFormulaAsegurada.setFormula(new Formulas());
      duplicarFormulaAsegurada.setPeriodicidad(new Periodicidades());
      duplicarFormulaAsegurada.setProceso(new Procesos());
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFormulasAseguradas').show()");
   }

   public void duplicandoFormulasAseguradas() {
      if (formulaAseguradaSeleccionada != null) {
         duplicarFormulaAsegurada = new FormulasAseguradas();
         duplicarFormulaAsegurada.setFormula(new Formulas());
         k++;
         l = BigInteger.valueOf(k);
         duplicarFormulaAsegurada.setSecuencia(l);
         duplicarFormulaAsegurada.setFormula(formulaAseguradaSeleccionada.getFormula());
         duplicarFormulaAsegurada.setProceso(formulaAseguradaSeleccionada.getProceso());
         duplicarFormulaAsegurada.setPeriodicidad(formulaAseguradaSeleccionada.getPeriodicidad());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroFormulasAseguradas').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      if (duplicarFormulaAsegurada.getFormula().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + " *Formula \n";
      } else {
         contador++;
      }
      if (duplicarFormulaAsegurada.getProceso().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + " *Proceso \n";
      } else {
         contador++;
      }
      System.out.println("confirmarDuplicar() contador : " + contador);
      System.out.println("duplicarFormulaAsegurada.getProceso() : " + duplicarFormulaAsegurada.getProceso());
      System.out.println("duplicarFormulaAsegurada.getFormula() : " + duplicarFormulaAsegurada.getFormula());
      if (contador == 2) {
         if (validarNuevoRepetido(duplicarFormulaAsegurada)) {
//            if (crearFormulasAseguradas.contains(duplicarFormulaAsegurada)) {
//               System.out.println("Ya lo contengo.");
//            }
            listFormulasAseguradas.add(duplicarFormulaAsegurada);
            crearFormulasAseguradas.add(duplicarFormulaAsegurada);
            formulaAseguradaSeleccionada = listFormulasAseguradas.get(listFormulasAseguradas.indexOf(duplicarFormulaAsegurada));
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            System.out.println("--------------DUPLICAR------------------------");
            System.out.println("PERSONA : " + duplicarFormulaAsegurada.getFormula().getNombrelargo());
            System.out.println("--------------DUPLICAR------------------------");

            if (guardado == true) {
               guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
               restaurarTabla();
            }
            contarRegistros();
            duplicarFormulaAsegurada = new FormulasAseguradas();
            duplicarFormulaAsegurada.setFormula(new Formulas());
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroFormulasAseguradas').hide()");
         }
      } else {
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarFormulasAseguradas() {
      duplicarFormulaAsegurada = new FormulasAseguradas();
      duplicarFormulaAsegurada.setFormula(new Formulas());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFormulasAseguradasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "FORMULASASEGURADAS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFormulasAseguradasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "FORMULASASEGURADAS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      System.out.println("lol");
      if (formulaAseguradaSeleccionada != null) {
         System.out.println("lol 2");
         int resultado = administrarRastros.obtenerTabla(formulaAseguradaSeleccionada.getSecuencia(), "FORMULASASEGURADAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
         System.out.println("resultado: " + resultado);
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
//         } else {
//            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//         }
      } else if (administrarRastros.verificarHistoricosTabla("FORMULASASEGURADAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void llamadoDialogoBuscarFormulas() {
      try {
         if (guardado == false) {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarFormulas').show()");
         } else {
            lovFormulasAseguradas = null;
            getLovFormulasAseguradas();
            RequestContext.getCurrentInstance().update("formularioDialogos:lovFormulasAs");
            RequestContext.getCurrentInstance().execute("PF('formulasAseguradasDialogo').show()");
         }
      } catch (Exception e) {
         System.err.println("ERROR LLAMADO DIALOGO BUSCAR CENTROS COSTOS " + e);
      }
   }

   public void seleccionFormulaAsegurada() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         System.err.println("guardado = " + guardado);
         if (guardado == true) {
            System.err.println("1 = " + 1);
            listFormulasAseguradas.clear();
//            listFormulasAseguradas = new ArrayList<FormulasAseguradas>();
            System.err.println("SELECCION FORMULA ASEGURADA " + formulaAseguradaLovSeleccionada.getFormula().getNombrelargo());
            listFormulasAseguradas.add(formulaAseguradaLovSeleccionada);
            System.err.println("listFormulasAseguradas tamaño " + listFormulasAseguradas.size());
            System.err.println("listFormulasAseguradas nombre " + listFormulasAseguradas.get(0).getFormula().getNombrelargo());
            filterlovFormulasAseguradas = null;
            aceptar = true;
            context.update("form:datosFormulasAseguradas");
            mostrarTodos = false;
            buscarFormulas = true;
            RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
            RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
            contarRegistros();
            formulaAseguradaLovSeleccionada = null;
            context.reset("formularioDialogos:lovFormulasAs:globalFilter");
            context.update("formularioDialogos:aceptarNCC");
            context.update("formularioDialogos:formulasAseguradasDialogo");
            context.update("formularioDialogos:lovFormulasAs");
            context.execute("PF('lovFormulasAs').clearFilters()");
            context.execute("PF('formulasAseguradasDialogo').hide()");
         } else {
            RequestContext.getCurrentInstance().update("form:confirmarGuardarFormulas");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarFormulas').show()");
         }
      } catch (Exception e) {
         System.out.println("ERROR CONTROL FORMULAS ASEGURADAS.seleccionaVigencia ERROR====" + e.getMessage());
      }
   }

   public void cancelarSeleccionConceptoSoporte() {
      RequestContext context = RequestContext.getCurrentInstance();
      filterlovFormulasAseguradas = null;
      aceptar = true;
      tipoActualizacion = -1;
      context.reset("formularioDialogos:lovFormulasAs:globalFilter");
      context.update("formularioDialogos:aceptarNCC");
      context.update("formularioDialogos:formulasAseguradasDialogo");
      context.update("formularioDialogos:lovFormulasAs");
      context.execute("PF('lovFormulasAs').clearFilters()");
      context.execute("PF('formulasAseguradasDialogo').hide()");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLovPeriodicidades() {
      RequestContext.getCurrentInstance().update("form:infoRegistroPeriocididades");
   }

   public void contarRegistrosLovFormulas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroProcesos");
   }

   public void contarRegistrosLovProcesos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroProcesos");
   }

   public void contarRegistrosLovFormulasAs() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroFormulasAseguradas");
   }

   public FormulasAseguradas getFormulaAseguradaLovSeleccionada() {
      return formulaAseguradaLovSeleccionada;
   }

   public void setFormulaAseguradaLovSeleccionada(FormulasAseguradas formulaAseguradaLovSeleccionada) {
      this.formulaAseguradaLovSeleccionada = formulaAseguradaLovSeleccionada;
   }

   public List<FormulasAseguradas> getLovFormulasAseguradas() {
      if (lovFormulasAseguradas == null) {
         lovFormulasAseguradas = administrarFormulasAseguradas.consultarFormulasAseguradas();
      }
      return lovFormulasAseguradas;
   }

   public void setLovFormulasAseguradas(List<FormulasAseguradas> lovFormulasAseguradas) {
      this.lovFormulasAseguradas = lovFormulasAseguradas;
   }

   public List<FormulasAseguradas> getFilterlovFormulasAseguradas() {
      return filterlovFormulasAseguradas;
   }

   public void setFilterlovFormulasAseguradas(List<FormulasAseguradas> filterlovFormulasAseguradas) {
      this.filterlovFormulasAseguradas = filterlovFormulasAseguradas;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<FormulasAseguradas> getListFormulasAseguradas() {
      if (listFormulasAseguradas == null) {
         listFormulasAseguradas = administrarFormulasAseguradas.consultarFormulasAseguradas();
         for (int i = 0; i < listFormulasAseguradas.size(); i++) {
            if (listFormulasAseguradas.get(i).getPeriodicidad() == null) {
               listFormulasAseguradas.get(i).setPeriodicidad(new Periodicidades());
            }
         }
      }
      return listFormulasAseguradas;
   }

   public void setListFormulasAseguradas(List<FormulasAseguradas> listFormulasAseguradas) {
      this.listFormulasAseguradas = listFormulasAseguradas;
   }

   public List<FormulasAseguradas> getFiltrarFormulasAseguradas() {
      return filtrarFormulasAseguradas;
   }

   public void setFiltrarFormulasAseguradas(List<FormulasAseguradas> filtrarFormulasAseguradas) {
      this.filtrarFormulasAseguradas = filtrarFormulasAseguradas;
   }

   public FormulasAseguradas getNuevoFormulaAsegurada() {
      return nuevoFormulaAsegurada;
   }

   public void setNuevoFormulaAsegurada(FormulasAseguradas nuevoFormulaAsegurada) {
      this.nuevoFormulaAsegurada = nuevoFormulaAsegurada;
   }

   public FormulasAseguradas getDuplicarFormulaAsegurada() {
      return duplicarFormulaAsegurada;
   }

   public void setDuplicarFormulaAsegurada(FormulasAseguradas duplicarFormulaAsegurada) {
      this.duplicarFormulaAsegurada = duplicarFormulaAsegurada;
   }

   public FormulasAseguradas getEditarFormulasAseguradas() {
      return editarFormulasAseguradas;
   }

   public void setEditarFormulasAseguradas(FormulasAseguradas editarFormulasAseguradas) {
      this.editarFormulasAseguradas = editarFormulasAseguradas;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public int getTamano() {
      proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulasAseguradas:proceso");
      if (proceso.getFilterStyle().startsWith("width: 85%")) {
         tamano = 295;
      } else {
         bandera = 0;
         tamano = 315;
      }
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public List<Formulas> getLovFormulas() {
      if (lovFormulas == null) {
         lovFormulas = administrarFormulasAseguradas.consultarLOVFormulas();
      }
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> lovFormulas) {
      this.lovFormulas = lovFormulas;
   }

   public List<Formulas> getFiltradoLovFormulas() {
      return filtradoLovFormulas;
   }

   public void setFiltradoLovFormulas(List<Formulas> filtradoLovFormulas) {
      this.filtradoLovFormulas = filtradoLovFormulas;
   }

   public Formulas getFormulaLovSeleccionado() {
      return formulaLovSeleccionado;
   }

   public void setFormulaLovSeleccionado(Formulas formulaLovSeleccionado) {
      this.formulaLovSeleccionado = formulaLovSeleccionado;
   }

   public List<Procesos> getLovProcesos() {
      if (lovProcesos == null) {
         lovProcesos = administrarFormulasAseguradas.consultarLOVProcesos();
      }
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> lovProcesos) {
      this.lovProcesos = lovProcesos;
   }

   public Procesos getProcesoLovSeleccionado() {
      return procesoLovSeleccionado;
   }

   public void setProcesoLovSeleccionado(Procesos procesoLovSeleccionado) {
      this.procesoLovSeleccionado = procesoLovSeleccionado;
   }

   public List<Procesos> getFiltradoLovProcesos() {
      return filtradoLovProcesos;
   }

   public void setFiltradoLovProcesos(List<Procesos> filtradoLovProcesos) {
      this.filtradoLovProcesos = filtradoLovProcesos;
   }

   public List<Periodicidades> getLovPeriodicidades() {
      if (lovPeriodicidades == null) {
         lovPeriodicidades = administrarFormulasAseguradas.consultarLOVPPeriodicidades();
      }
      return lovPeriodicidades;
   }

   public void setLovPeriodicidades(List<Periodicidades> lovPeriodicidades) {
      this.lovPeriodicidades = lovPeriodicidades;
   }

   public List<Periodicidades> getFiltradoLovPeriodicidades() {
      return filtradoLovPeriodicidades;
   }

   public void setFiltradoLovPeriodicidades(List<Periodicidades> filtradoLovPeriodicidades) {
      this.filtradoLovPeriodicidades = filtradoLovPeriodicidades;
   }

   public Periodicidades getPeriodicidadLovSeleccionado() {
      return periodicidadLovSeleccionado;
   }

   public void setPeriodicidadLovSeleccionado(Periodicidades periodicidadLovSeleccionado) {
      this.periodicidadLovSeleccionado = periodicidadLovSeleccionado;
   }

   public FormulasAseguradas getFormulaAseguradaSeleccionada() {
      return formulaAseguradaSeleccionada;
   }

   public void setFormulaAseguradaSeleccionada(FormulasAseguradas formulaAseguradaSeleccionada) {
      this.formulaAseguradaSeleccionada = formulaAseguradaSeleccionada;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFormulasAseguradas");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroFormulas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovFormulas");
      infoRegistroFormulas = String.valueOf(tabla.getRowCount());
      return infoRegistroFormulas;
   }

   public void setInfoRegistroFormulas(String infoRegistroFormulas) {
      this.infoRegistroFormulas = infoRegistroFormulas;
   }

   public String getInfoRegistroProcesos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovProcesos");
      infoRegistroProcesos = String.valueOf(tabla.getRowCount());
      return infoRegistroProcesos;
   }

   public void setInfoRegistroProcesos(String infoRegistroProcesos) {
      this.infoRegistroProcesos = infoRegistroProcesos;
   }

   public String getInfoRegistroPeriocididades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovPeriodicidades");
      infoRegistroPeriocididades = String.valueOf(tabla.getRowCount());
      return infoRegistroPeriocididades;
   }

   public void setInfoRegistroPeriocididades(String infoRegistroPeriocididades) {
      this.infoRegistroPeriocididades = infoRegistroPeriocididades;
   }

   public String getInfoRegistroFormulasAseguradas() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovFormulasAs");
      infoRegistroFormulasAseguradas = String.valueOf(tabla.getRowCount());
      return infoRegistroFormulasAseguradas;
   }

   public void setInfoRegistroFormulasAseguradas(String infoRegistroFormulasAseguradas) {
      this.infoRegistroFormulasAseguradas = infoRegistroFormulasAseguradas;
   }

   public boolean isBuscarFormulas() {
      return buscarFormulas;
   }

   public void setBuscarFormulas(boolean buscarFormulas) {
      this.buscarFormulas = buscarFormulas;
   }

   public boolean isMostrarTodos() {
      return mostrarTodos;
   }

   public void setMostrarTodos(boolean mostrarTodos) {
      this.mostrarTodos = mostrarTodos;
   }
}
