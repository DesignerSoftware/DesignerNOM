package Controlador;

import Entidades.Formulas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.io.Serializable;
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

@ManagedBean
@SessionScoped
public class ControlFormula implements Serializable {

   @EJB
   AdministrarFormulaInterface administrarFormula;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //Parametros que llegan
   private List<Formulas> listaFormulas;
   private List<Formulas> filtradoListaFormulas;
   private List<Formulas> lovFormulas;
   private List<Formulas> filtradoListaFormulasLOV;
   private Formulas formulaSeleccionadaLOV;
   private Formulas formulaSeleccionada;
   private boolean verSeleccionFormula;
   private boolean verMostrarTodos;
   private boolean mostrarTodos;
//   private boolean permitirIndex;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla VC
   private Column columnaNombreLargo, columnaNombreCorto, columnaEstado, columnaNota, columnaTipo; //, columnaPeriodicidad;
   //Otros
   private boolean aceptar;
   private String altoTablaReg, altoTablaH;
   private boolean disabledPropiedadesFormula;
   //modificar
   private List<Formulas> listaFormulasModificar;
   private boolean guardado;
   //crear VC
   public Formulas nuevaFormula;
   private List<Formulas> listaFormulasCrear;
   private BigInteger l;
   private int k;
   private String mensajeValidacion;
   //borrar VC
   private List<Formulas> listaFormulasBorrar;
   //editar celda
   private Formulas editarFormula;
   private int cualCelda, tipoLista;
   //duplicar
   private Formulas duplicarFormula;
   //RASTRO
   //CLONAR Formula
   private Formulas formulaOriginal;
   private Formulas formulaClon;
   private int cambioFormula;
   private boolean activoDetalleFormula, activoBuscarTodos;
   //0 - Detalle Concepto / 1 - Nomina
   private int llamadoPrevioPagina;
   //
   private String infoRegistro, infoRegistroFormula;
   //
   private DataTable tabla;
   private boolean unaVez;
//   private int regSolucion;
   private String nombreLargoMientras;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlFormula() {
      llamadoPrevioPagina = 1;
      activoBuscarTodos = false;
      activoDetalleFormula = true;
      listaFormulas = null;
      filtradoListaFormulas = null;
      //Otros
      aceptar = true;
      listaFormulasBorrar = new ArrayList<Formulas>();
      listaFormulasCrear = new ArrayList<Formulas>();
      k = 0;
      listaFormulasModificar = new ArrayList<Formulas>();
      editarFormula = new Formulas();
      mostrarTodos = true;
      cualCelda = -1;
      guardado = true;
      //Crear 
      nuevaFormula = new Formulas();
      duplicarFormula = new Formulas();
      //CLON
      formulaClon = new Formulas();
      formulaOriginal = new Formulas();
//      permitirIndex = true;
//      altoTablaReg = "204";

      formulaSeleccionada = null;
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      tipoLista = 0;
      mapParametros.put("paginaAnterior", paginaAnterior);
      disabledPropiedadesFormula = true;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarFormula.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void limpiarListasValor() {
      lovFormulas = null;
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listaFormulas = null;
      getListaFormulas();
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      String cargarFormula = (String) mapParametros.get("cargarFormula");
      if (cargarFormula != null) {
         if (cargarFormula.equals("SI")) {
            BigInteger sec = (BigInteger) mapParametros.get("secFormula");
            obtenerFormulaSecuencia(sec);
         } else if (cargarFormula.equals("NO")) {
            formulaSeleccionada = (Formulas) mapParametros.get("formula");
            listaFormulas.clear();
            listaFormulas.add(formulaSeleccionada);
            llamadoPrevioPagina = 0;
            mostrarTodos = false;
         } else {
            listaFormulas = null;
         }
      }
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "formula";
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
      } else {
         if (pag.equals("historiaformula")) {
            Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            mapParaEnviar.put("paginaAnterior", pagActual);
            mapParaEnviar.put("formula", formulaSeleccionada);
            ControlHistoriaFormula controlHistoriaFormula = (ControlHistoriaFormula) fc.getApplication().evaluateExpressionGet(fc, "#{controlHistoriaFormula}", ControlHistoriaFormula.class);
            controlHistoriaFormula.recibirParametros(mapParaEnviar);
            pag = "historiaformula";
         }
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

   public void verDetalle(Formulas formula) {
      System.out.println("Controlador.ControlFormula.verDetalle()");
      unaVez = true;
      nombreLargoMientras = "0";
      formulaSeleccionada = formula;
      navegar("historiaformula");
   }

   public boolean activarSelec() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      return false;
   }

   //OBTENER FORMULA POR SECUENCIA
   public void obtenerFormulaSecuencia(BigInteger secuencia) {
      if (secuencia != null) {
         Formulas actual = administrarFormula.buscarFormulaSecuencia(secuencia);
         listaFormulas = new ArrayList<Formulas>();
         listaFormulas.add(actual);
         activoBuscarTodos = true;
      } else {
         getListaFormulas();
         activoBuscarTodos = false;
      }
      llamadoPrevioPagina = 0;
      mostrarTodos = false;
   }

   //SELECCIONAR NATURALEZA
   public void seleccionarItem(Formulas formula) {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
//      regSolucion = -1;
      nombreLargoMientras = "0";
      formulaSeleccionada = formula;
      if (!listaFormulasCrear.contains(formulaSeleccionada)) {
         if (listaFormulasModificar.isEmpty()) {
            listaFormulasModificar.add(formulaSeleccionada);
         } else if (!listaFormulasModificar.contains(formulaSeleccionada)) {
            listaFormulasModificar.add(formulaSeleccionada);
         }
      }
      if (guardado) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:novedadFormula");
      RequestContext.getCurrentInstance().update("form:procesoFormula");
      RequestContext.getCurrentInstance().update("form:conceptoFormula");
      RequestContext.getCurrentInstance().update("form:legislacionFormula");
      //RequestContext.getCurrentInstance().update("form:datosFormulas"); 
//      RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      if (formulaSeleccionada != null) {
         editarFormula = formulaSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editorNombreLargo");
            RequestContext.getCurrentInstance().execute("PF('editorNombreLargo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editorNombreCorto");
            RequestContext.getCurrentInstance().execute("PF('editorNombreCorto').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editorNota");
            RequestContext.getCurrentInstance().execute("PF('editorNota').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
      activoDetalleFormula = true;
   }

   public void modificarFormula(Formulas formula) {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      formulaSeleccionada = formula;
      if (!listaFormulasCrear.contains(formulaSeleccionada)) {
         if (listaFormulasModificar.isEmpty()) {
            listaFormulasModificar.add(formulaSeleccionada);
         } else if (!listaFormulasModificar.contains(formulaSeleccionada)) {
            listaFormulasModificar.add(formulaSeleccionada);
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      if (formulaSeleccionada.isPeriodicidadFormula()) {
         formulaSeleccionada.setPeriodicidadindependiente("S");
      } else if (formulaSeleccionada.isPeriodicidadFormula() == false) {
         formulaSeleccionada.setPeriodicidadindependiente("N");
      }
      RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
      RequestContext.getCurrentInstance().update("form:datosFormulas");
   }

   //Ubicacion Celda.
   public void cambiarIndice(Formulas formula, int celda) {
      formulaSeleccionada = formula;
      cualCelda = celda;
      if (unaVez) {
         activoDetalleFormula = false;
         unaVez = false;
      }
      RequestContext.getCurrentInstance().update("form:novedadFormula");
      RequestContext.getCurrentInstance().update("form:procesoFormula");
      RequestContext.getCurrentInstance().update("form:conceptoFormula");
      RequestContext.getCurrentInstance().update("form:legislacionFormula");
   }

   public void lovFomula(int quien) {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      if (quien == 0) {
         if (guardado) {
            activoDetalleFormula = true;
            lovFormulas = null;
            getLovFormulas();
            contarRegistrosFormulasLOV();
            RequestContext.getCurrentInstance().update("formularioDialogos:FormulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').show()");
            verSeleccionFormula = false;
            cambioFormula = 0;
         } else {
            verSeleccionFormula = true;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
         }
      } else {
         lovFormulas = null;
         getLovFormulas();
         contarRegistrosFormulasLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:FormulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').show()");
         cambioFormula = 1;
      }
      cualCelda = -1;
   }

   public void mostrarTodasFormulas() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      if (guardado) {
         if (bandera == 1) {
            cargarTablaDefault();
         }
         mostrarTodos = true;
         verMostrarTodos = false;
         activoDetalleFormula = true;

         listaFormulas.clear();
         for (int i = 0; i < lovFormulas.size(); i++) {
            listaFormulas.add(lovFormulas.get(i));
         }
         formulaSeleccionada = null;
         RequestContext.getCurrentInstance().update("form:mostrarTodos");
         RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
         RequestContext.getCurrentInstance().update("form:datosFormulas");
         contarRegistros();
      } else {
         verMostrarTodos = true;
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
      cualCelda = -1;
   }

   public void seleccionFormula() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      RequestContext context = RequestContext.getCurrentInstance();
      if (cambioFormula == 0) {
         if (bandera == 1) {
            cargarTablaDefault();
         }
         listaFormulas.clear();
         listaFormulas.add(formulaSeleccionadaLOV);
         formulaSeleccionada = formulaSeleccionadaLOV;
         activoDetalleFormula = true;
         mostrarTodos = false;
         RequestContext.getCurrentInstance().update("form:mostrarTodos");
         RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
         RequestContext.getCurrentInstance().update("form:datosFormulas");
         contarRegistros();
      } else {
         formulaOriginal = formulaSeleccionadaLOV;
         RequestContext.getCurrentInstance().update("form:descripcionClon");
      }
      filtradoListaFormulasLOV = null;
      formulaSeleccionadaLOV = null;
      aceptar = true;

      context.reset("formularioDialogos:lovFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').hide()");
   }

   public void cancelarSeleccionFormula() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      filtradoListaFormulasLOV = null;
      formulaSeleccionadaLOV = null;
      aceptar = true;
      formulaOriginal.setNombresFormula(null);
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:lovFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulasDialogo').hide()");
   }

   public void borrarFormula() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      if (formulaSeleccionada != null) {

         if (!listaFormulasModificar.isEmpty() && listaFormulasModificar.contains(formulaSeleccionada)) {
            int modIndex = listaFormulasModificar.indexOf(formulaSeleccionada);
            listaFormulasModificar.remove(modIndex);
            listaFormulasBorrar.add(formulaSeleccionada);
         } else if (!listaFormulasCrear.isEmpty() && listaFormulasCrear.contains(formulaSeleccionada)) {
            int crearIndex = listaFormulasCrear.indexOf(formulaSeleccionada);
            listaFormulasCrear.remove(crearIndex);
         } else {
            listaFormulasBorrar.add(formulaSeleccionada);
         }
         listaFormulas.remove(formulaSeleccionada);
         if (tipoLista == 1) {
            filtradoListaFormulas.remove(formulaSeleccionada);
         }

         activoDetalleFormula = true;
         contarRegistros();
         RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
         RequestContext.getCurrentInstance().update("form:datosFormulas");

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void guardarSalir() {
      guardarCambios();
      salir();
   }

   public void cancelarSalir() {
      cancelarModificaciones();
      salir();
   }

   //GUARDAR
   public void guardarCambios() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listaFormulasBorrar.isEmpty()) {
               for (int i = 0; i < listaFormulasBorrar.size(); i++) {
                  if (listaFormulasBorrar.get(i).isPeriodicidadFormula()) {
                     listaFormulasBorrar.get(i).setPeriodicidadindependiente("S");
                  } else if (listaFormulasBorrar.get(i).isPeriodicidadFormula() == false) {
                     listaFormulasBorrar.get(i).setPeriodicidadindependiente("N");
                  }
                  administrarFormula.borrar(listaFormulasBorrar.get(i));
               }
               listaFormulasBorrar.clear();
            }
            if (!listaFormulasCrear.isEmpty()) {
               for (int i = 0; i < listaFormulasCrear.size(); i++) {
                  if (listaFormulasCrear.get(i).isPeriodicidadFormula()) {
                     listaFormulasCrear.get(i).setPeriodicidadindependiente("S");
                  } else if (listaFormulasCrear.get(i).isPeriodicidadFormula() == false) {
                     listaFormulasCrear.get(i).setPeriodicidadindependiente("N");
                  }
                  administrarFormula.crear(listaFormulasCrear.get(i));
               }
               listaFormulasCrear.clear();
            }
            if (!listaFormulasModificar.isEmpty()) {
               administrarFormula.modificar(listaFormulasModificar);
               listaFormulasModificar.clear();
            }
            listaFormulas = null;
            getListaFormulas();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            RequestContext.getCurrentInstance().update("form:novedadFormula");
            RequestContext.getCurrentInstance().update("form:procesoFormula");
            RequestContext.getCurrentInstance().update("form:conceptoFormula");
            RequestContext.getCurrentInstance().update("form:legislacionFormula");

            RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
            RequestContext.getCurrentInstance().update("form:datosFormulas");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//            permitirIndex = true;
            k = 0;
            activoDetalleFormula = true;

            if (verSeleccionFormula) {
               lovFomula(0);
            }
            if (verMostrarTodos) {
               mostrarTodasFormulas();
            }
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Se presento un error en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   public void activarCtrlF11() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      RequestContext context = RequestContext.getCurrentInstance();
      FacesContext c = FacesContext.getCurrentInstance();
      formulaSeleccionada = null;
      if (bandera == 0) {
//         altoTablaReg = "184";
         columnaNombreLargo = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaNombreLargo");
         columnaNombreLargo.setFilterStyle("width: 85% !important;");
         columnaNombreCorto = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaNombreCorto");
         columnaNombreCorto.setFilterStyle("width: 85% !important;");
         columnaEstado = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaEstado");
         columnaEstado.setFilterStyle("width: 85% !important;");
         columnaNota = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaNota");
         columnaNota.setFilterStyle("width: 85% !important;");
         columnaTipo = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaTipo");
         columnaTipo.setFilterStyle("width: 85% !important;");
//            columnaPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaPeriodicidad");
//            columnaPeriodicidad.setFilterStyle("width: 94%;");
//         RequestContext.getCurrentInstance().update("form:datosFormulas"); RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
         bandera = 1;

      } else if (bandera == 1) {
         cargarTablaDefault();
      }
      activoDetalleFormula = true;
      RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
      RequestContext.getCurrentInstance().update("form:datosFormulas");

   }

   public void dialogoIngresoNuevoRegistro() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      activoDetalleFormula = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('NuevaFormulaDialogo').show()");

   }

   public void agregarNuevaFormula() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      int pasa = 0;
      mensajeValidacion = "";
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevaFormula.getNombrelargo() == null) {
         mensajeValidacion = " * Nombre Largo\n";
         pasa++;
      }
      if (nuevaFormula.getNombrecorto() == null) {
         mensajeValidacion = mensajeValidacion + " *Nombre Corto\n";
         pasa++;
      }
      if (nuevaFormula.getTipo() == null) {
         mensajeValidacion = mensajeValidacion + " *Tipo\n";
         pasa++;
      }
      if (nuevaFormula.getEstado() == null) {
         mensajeValidacion = mensajeValidacion + " *Estado\n";
         pasa++;
      }
      if (pasa == 0) {
         if (bandera == 1) {
            cargarTablaDefault();
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevaFormula.setSecuencia(l);
         if (nuevaFormula.isPeriodicidadFormula()) {
            nuevaFormula.setPeriodicidadindependiente("S");
         } else if (nuevaFormula.isPeriodicidadFormula() == false) {
            nuevaFormula.setPeriodicidadindependiente("N");
         }
         listaFormulasCrear.add(nuevaFormula);
         listaFormulas.add(nuevaFormula);
         nuevaFormula = new Formulas();
         contarRegistros();
         RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
         RequestContext.getCurrentInstance().update("form:datosFormulas");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevaFormulaDialogo').hide()");
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevaFormulaDialogo");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevaFormula");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevaFormula').show()");
      }
   }
   //LIMPIAR NUEVO REGISTRO

   public void limpiarNuevaFormula() {
      nuevaFormula = new Formulas();
   }

   public void duplicarRegistro() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      RequestContext context = RequestContext.getCurrentInstance();
      if (formulaSeleccionada != null) {
         duplicarFormula = new Formulas();
         duplicarFormula.setNombrelargo(formulaSeleccionada.getNombrelargo());
         duplicarFormula.setNombrecorto(formulaSeleccionada.getNombrecorto());
         duplicarFormula.setEstado(formulaSeleccionada.getEstado());
         duplicarFormula.setObservaciones(formulaSeleccionada.getObservaciones());
         duplicarFormula.setTipo(formulaSeleccionada.getTipo());
         duplicarFormula.setPeriodicidadindependiente(formulaSeleccionada.getPeriodicidadindependiente());

         activoDetalleFormula = true;

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
         RequestContext.getCurrentInstance().execute("PF('DuplicarFormulaDialogo').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      int pasa = 0;
      mensajeValidacion = "";
      RequestContext context = RequestContext.getCurrentInstance();

      if (duplicarFormula.getNombrelargo() == null) {
         mensajeValidacion = " * Nombre Largo\n";
         pasa++;
      }
      if (duplicarFormula.getNombrecorto() == null) {
         mensajeValidacion = mensajeValidacion + " *Nombre Corto\n";
         pasa++;
      }
      if (duplicarFormula.getTipo() == null) {
         mensajeValidacion = mensajeValidacion + " *Tipo\n";
         pasa++;
      }
      if (duplicarFormula.getEstado() == null) {
         mensajeValidacion = mensajeValidacion + " *Estado\n";
         pasa++;
      }
      if (pasa == 0) {
         if (duplicarFormula.isPeriodicidadFormula()) {
            duplicarFormula.setPeriodicidadindependiente("S");
         } else if (duplicarFormula.isPeriodicidadFormula() == false) {
            duplicarFormula.setPeriodicidadindependiente("N");
         }
         k++;
         l = BigInteger.valueOf(k);
         duplicarFormula.setSecuencia(l);
         listaFormulas.add(duplicarFormula);
         listaFormulasCrear.add(duplicarFormula);
         RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
         RequestContext.getCurrentInstance().update("form:datosFormulas");
         RequestContext.getCurrentInstance().execute("PF('DuplicarFormulaDialogo').hide()");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         contarRegistros();
         if (bandera == 1) {
            cargarTablaDefault();
         }
         RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
         RequestContext.getCurrentInstance().update("form:datosFormulas");
         duplicarFormula = new Formulas();
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevaFormula");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevaFormula').show()");
      }
   }
   //LIMPIAR DUPLICAR

   public void limpiarduplicar() {
      duplicarFormula = new Formulas();
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         cargarTablaDefault();
      }
      listaFormulasBorrar.clear();
      listaFormulasCrear.clear();
      listaFormulasModificar.clear();
      formulaSeleccionada = null;
      k = 0;
      listaFormulas = null;
      guardado = true;
      mostrarTodos = true;
      formulaClon = new Formulas();
      formulaOriginal = new Formulas();
      activoDetalleFormula = true;
      if (verSeleccionFormula) {
         lovFomula(0);
      }
      if (verMostrarTodos) {
         mostrarTodasFormulas();
      }
      activoBuscarTodos = false;
      formulaSeleccionada = null;
      navegar("atras");
   }

   public void cancelarModificaciones() {
      RequestContext context = RequestContext.getCurrentInstance();
      unaVez = true;
      tipoLista = 0;
      nombreLargoMientras = "0";
      if (bandera == 1) {
         cargarTablaDefault();
      }
      listaFormulasBorrar.clear();
      listaFormulasCrear.clear();
      listaFormulasModificar.clear();
      formulaSeleccionada = null;
      k = 0;
      listaFormulas = null;
      getListaFormulas();
      disabledPropiedadesFormula = true;
      context.update("form:informacionRegistro");
      guardado = true;
      context.update("form:ACEPTAR");
      mostrarTodos = true;
      formulaClon = new Formulas();
      formulaOriginal = new Formulas();
      activoDetalleFormula = true;
      if (verSeleccionFormula) {
         lovFomula(0);
      }
      if (verMostrarTodos) {
         mostrarTodasFormulas();
      }
      context.update("form:novedadFormula");
      context.update("form:procesoFormula");
      context.update("form:conceptoFormula");
      context.update("form:legislacionFormula");

      context.update("form:mostrarTodos");
      context.execute("PF('datosFormulas').clearFilters()");
      context.update("form:datosFormulas");
      context.update("form:nombreLargoFormulaClon");
      context.update("form:nombreCortoFormulaClon");
      context.update("form:observacionFormulaClon");
      context.update("form:descripcionClon");
      contarRegistros();
   }

   public void cargarTablaDefault() {
      unaVez = true;
      tipoLista = 0;
      nombreLargoMientras = "0";
      FacesContext c = FacesContext.getCurrentInstance();
      columnaNombreLargo = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaNombreLargo");
      columnaNombreLargo.setFilterStyle("display: none; visibility: hidden;");
      columnaNombreCorto = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaNombreCorto");
      columnaNombreCorto.setFilterStyle("display: none; visibility: hidden;");
      columnaEstado = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaEstado");
      columnaEstado.setFilterStyle("display: none; visibility: hidden;");
      columnaNota = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaNota");
      columnaNota.setFilterStyle("display: none; visibility: hidden;");
      columnaTipo = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaTipo");
      columnaTipo.setFilterStyle("display: none; visibility: hidden;");
//        columnaPeriodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulas:columnaPeriodicidad");
//        columnaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
      bandera = 0;
      filtradoListaFormulas = null;
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      activoDetalleFormula = true;

      if (formulaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(formulaSeleccionada.getSecuencia(), "FORMULAS");
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
      } else if (administrarRastros.verificarHistoricosTabla("FORMULAS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      DataTable tablaE = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFormulasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tablaE, "FormulasPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      activoDetalleFormula = true;
      RequestContext contxt = RequestContext.getCurrentInstance();
      contxt.update("form:detalleFormula");
   }

   public void exportXLS() throws IOException {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      DataTable tablaE = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFormulasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tablaE, "FormulasXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      activoDetalleFormula = true;
      RequestContext contxt = RequestContext.getCurrentInstance();
      contxt.update("form:detalleFormula");
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      formulaSeleccionada = null;
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosFormulasLOV() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroFormula");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   //CLONAR
   public void clonarFormula() {
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      if (formulaClon.getNombrelargo() != null && formulaClon.getNombrecorto() != null && formulaOriginal.getSecuencia() != null) {
         administrarFormula.clonarFormula(formulaOriginal.getNombrecorto(), formulaClon.getNombrecorto(), formulaClon.getNombrelargo(), formulaClon.getObservaciones());
         formulaClon = new Formulas();
         formulaOriginal = new Formulas();
         listaFormulas = null;
         getListaFormulas();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         activoDetalleFormula = true;

         RequestContext.getCurrentInstance().execute("PF('datosFormulas').clearFilters()");
         RequestContext.getCurrentInstance().update("form:datosFormulas");
         RequestContext.getCurrentInstance().update("form:nombreLargoFormulaClon");
         RequestContext.getCurrentInstance().update("form:nombreCortoFormulaClon");
         RequestContext.getCurrentInstance().update("form:observacionFormulaClon");
         RequestContext.getCurrentInstance().update("form:descripcionClon");

         RequestContext.getCurrentInstance().update("form:novedadFormula");
         RequestContext.getCurrentInstance().update("form:procesoFormula");
         RequestContext.getCurrentInstance().update("form:conceptoFormula");
         RequestContext.getCurrentInstance().update("form:legislacionFormula");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Formula clonada con exito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoClon");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevoClon').show()");
      }
   }

   //FORMULA OPERANDO
   public void formulaOperando(Formulas formula) {
      formulaSeleccionada = formula;
      unaVez = true;
//      regSolucion = -1;
      nombreLargoMientras = "0";
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('confirmarOperandoFormula').show();");
   }

   public void confirmarFormulaOperando() {
      administrarFormula.operandoFormula(formulaSeleccionada.getSecuencia());
   }

//   public String paginaRetorno() {
//      unaVez = true;
////      regSolucion = -1;
//      nombreLargoMientras = "0";
//      String paginaRetorno = "";
//      if (llamadoPrevioPagina == 1) {
//         paginaRetorno = "nomina";
//      }
//      if (llamadoPrevioPagina == 0) {
//         paginaRetorno = "detalleconcepto";
//      }
//      llamadoPrevioPagina = 1;
//      return paginaRetorno;
//   }
   public void darSeleccion() {
      if (formulaSeleccionada != null) {
         if (listaFormulas.contains(formulaSeleccionada)) {
            if (listaFormulas.indexOf(formulaSeleccionada) > 6) {
               List<Formulas> listaAuxiliar = new ArrayList<Formulas>();
               listaAuxiliar.addAll(listaFormulas);
               listaFormulas.clear();
               listaFormulas.add(formulaSeleccionada);
               listaAuxiliar.remove(formulaSeleccionada);
               listaFormulas.addAll(listaAuxiliar);
               FacesContext c = FacesContext.getCurrentInstance();
               tabla = (DataTable) c.getViewRoot().findComponent("form:datosFormulas");
               tabla.setValue(listaFormulas);
               tabla.setSelection(formulaSeleccionada);
            }
         }
      }
//      if (formulaSeleccionada != null) {
//         if (listaFormulas.contains(formulaSeleccionada)) {
//            if (!listaFormulas.get(0).equals(formulaSeleccionada)) {
//
//               regSolucion = listaFormulas.indexOf(formulaSeleccionada);
//               nombreLargoMientras = listaFormulas.get(regSolucion - 1).getNombrelargo();
//               List<Formulas> listaAux = new ArrayList<Formulas>();
//
//               for (int i = 0; i < listaFormulas.size(); i++) {
//                  if (!listaFormulas.get(i).equals(formulaSeleccionada)) {
//                     listaAux.add(listaFormulas.get(i));
//                  }
//               }
//               listaFormulas.set(0, formulaSeleccionada);
//               for (int n = 0; n < listaAux.size(); n++) {
//                  listaFormulas.set((n + 1), listaAux.get(n));
//               }
//            }
//         }
//         if ((unaVez == false) && (regSolucion != -1) && (!nombreLargoMientras.equals("0"))) {
//            listaFormulas.get(regSolucion).setNombrelargo(nombreLargoMientras);
//         }
//         FacesContext c = FacesContext.getCurrentInstance();
//         tabla = (DataTable) c.getViewRoot().findComponent("form:datosFormulas");
//         tabla.setSelection(formulaSeleccionada);
//      } else {
//         unaVez = true;
//         regSolucion = -1;
//         nombreLargoMientras = "0";
//         formulaSeleccionada = null;
//      }
   }

   //GETTER AND SETTER
   public List<Formulas> getListaFormulas() {
      if (listaFormulas == null) {
         listaFormulas = administrarFormula.formulas();
      }
      return listaFormulas;
   }

   public void setListaFormulas(List<Formulas> listaFormulas) {
      this.listaFormulas = listaFormulas;
   }

   public List<Formulas> getFiltradoListaFormulas() {
      return filtradoListaFormulas;
   }

   public void setFiltradoListaFormulas(List<Formulas> filtradoListaFormulas) {
      this.filtradoListaFormulas = filtradoListaFormulas;
   }

   public List<Formulas> getLovFormulas() {
      if (lovFormulas == null) {
         lovFormulas = administrarFormula.formulas();
      }
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> lovFormulas) {
      this.lovFormulas = lovFormulas;
   }

   public List<Formulas> getFiltradoListaFormulasLOV() {
      return filtradoListaFormulasLOV;
   }

   public void setFiltradoListaFormulasLOV(List<Formulas> filtradoListaFormulasLOV) {
      this.filtradoListaFormulasLOV = filtradoListaFormulasLOV;
   }

   public Formulas getFormulaSeleccionadaLOV() {
      return formulaSeleccionadaLOV;
   }

   public void setFormulaSeleccionadaLOV(Formulas formulaSeleccionadaLov) {
      this.formulaSeleccionadaLOV = formulaSeleccionadaLov;
   }

   public boolean isMostrarTodos() {
      return mostrarTodos;
   }

   public void setMostrarTodos(boolean mostrarTodos) {
      this.mostrarTodos = mostrarTodos;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public Formulas getNuevaFormula() {
      return nuevaFormula;
   }

   public void setNuevaFormula(Formulas nuevaFormula) {
      this.nuevaFormula = nuevaFormula;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public Formulas getEditarFormula() {
      return editarFormula;
   }

   public void setEditarFormula(Formulas editarFormula) {
      this.editarFormula = editarFormula;
   }

   public Formulas getDuplicarFormula() {
      return duplicarFormula;
   }

   public void setDuplicarFormula(Formulas duplicarFormula) {
      this.duplicarFormula = duplicarFormula;
   }

   public Formulas getFormulaOriginal() {
      return formulaOriginal;
   }

   public void setFormulaOriginal(Formulas formulaOriginal) {
      this.formulaOriginal = formulaOriginal;
   }

   public Formulas getFormulaClon() {
      return formulaClon;
   }

   public void setFormulaClon(Formulas formulaClon) {
      this.formulaClon = formulaClon;
   }

   public String getAltoTablaReg() {
      columnaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulas:columnaNombreCorto");
      if (columnaNombreCorto.getFilterStyle().startsWith("width: 85%")) {
         altoTablaReg = "" + 8;
      } else {
         bandera = 0;
         altoTablaReg = "" + 9;
      }
      return altoTablaReg;
   }

   public String getAltoTablaH() {
      columnaNombreCorto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulas:columnaNombreCorto");
      if (columnaNombreCorto.getFilterStyle().startsWith("width: 85%")) {
         altoTablaH = "" + 180;
      } else {
         bandera = 0;
         altoTablaH = "" + 200;
      }
      return altoTablaH;
   }

   public void setAltoTablaH(String altoTablaH) {
      this.altoTablaH = altoTablaH;
   }

   public boolean isDisabledPropiedadesFormula() {
      if (formulaSeleccionada != null) {
         if (formulaSeleccionada.getTipo().equals("FINAL") && formulaSeleccionada.getEstado().equals("ACTIVO")) {
            disabledPropiedadesFormula = false;
         } else {
            disabledPropiedadesFormula = true;
         }
      }
      return disabledPropiedadesFormula;
   }

   public boolean isActivoDetalleFormula() {
      return activoDetalleFormula;
   }

   public void setActivoDetalleFormula(boolean activoDetalleFormula) {
      this.activoDetalleFormula = activoDetalleFormula;
   }

   public boolean isActivoBuscarTodos() {
      return activoBuscarTodos;
   }

   public void setActivoBuscarTodos(boolean activoBuscarTodos) {
      this.activoBuscarTodos = activoBuscarTodos;
   }

   public Formulas getFormulaSeleccionada() {
      return formulaSeleccionada;
   }

   public void setFormulaSeleccionada(Formulas formulaSeleccionada) {
      this.formulaSeleccionada = formulaSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFormulas");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroFormula() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovFormulas");
      infoRegistroFormula = String.valueOf(tabla.getRowCount());
      return infoRegistroFormula;
   }

   public void setInfoRegistroFormula(String infoRegistroFormula) {
      this.infoRegistroFormula = infoRegistroFormula;
   }
}
