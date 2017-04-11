package Controlador;

import Entidades.Formulas;
import Entidades.FormulasProcesos;
import Entidades.Procesos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaProcesoInterface;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlFormulaProceso implements Serializable {

   @EJB
   AdministrarFormulaProcesoInterface administrarFormulaProceso;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<FormulasProcesos> listFormulasProcesos;
   private List<FormulasProcesos> filtrarListFormulasProcesos;
   private Formulas formulaActual;
   private FormulasProcesos formulaProcesoSeleccionada;
   ///////
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla VC
   private Column formulaProceso, formulaPeriodicidad;
   //Otros
   private boolean aceptar;
   //modificar
   private List<FormulasProcesos> listFormulasProcesosModificar;
   private boolean guardado;
   //crear 
   private FormulasProcesos nuevoFormulaProceso;
   private List<FormulasProcesos> listFormulasProcesosCrear;
   private BigInteger l;
   private int k;
   //borrar 
   private List<FormulasProcesos> listFormulasProcesosBorrar;
   //editar celda
   private FormulasProcesos editarFormulaProceso;
   private int cualCelda, tipoLista;
   //duplicar
   private FormulasProcesos duplicarFormulaProceso;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private String nombreTablaRastro;
   private String nombreXML, nombreTabla;
   private String proceso;

   //////////////////////
   private List<Procesos> lovProcesos;
   private List<Procesos> filtrarLovProcesos;
   private Procesos procesoSeleccionado;

   private boolean permitirIndex;
   private int tipoActualizacion;
   //
   private String altoTabla;
   private String infoRegistro;
   private String infoRegistroProceso;
   private String paginaEntrante;
   //
   private boolean activarLOV;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlFormulaProceso() {
      altoTabla = "180";
      permitirIndex = true;
      tipoActualizacion = -1;
      lovProcesos = null;
      procesoSeleccionado = new Procesos();
      listFormulasProcesos = null;
      //Otros
      aceptar = true;
      //borrar aficiones
      listFormulasProcesosBorrar = new ArrayList<FormulasProcesos>();
      //crear aficiones
      listFormulasProcesosCrear = new ArrayList<FormulasProcesos>();
      k = 0;
      //modificar aficiones
      listFormulasProcesosModificar = new ArrayList<FormulasProcesos>();
      //editar
      editarFormulaProceso = new FormulasProcesos();
      cualCelda = -1;
      tipoLista = 0;
      //guardar
      guardado = true;
      //Crear VC
      nuevoFormulaProceso = new FormulasProcesos();
      nuevoFormulaProceso.setProceso(new Procesos());
      duplicarFormulaProceso = new FormulasProcesos();
      formulaActual = new Formulas();
      //
      activarLOV = true;
      paginaEntrante = "";
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
         System.out.println("navegar('Atras') : " + pag);
      } else {
         String pagActual = "formulaproceso";
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
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
      }
      limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarFormulaProceso.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirFormula(Formulas formula, String pagina) {
      paginaEntrante = pagina;
//      formulaActual = administrarFormulaProceso.formulaActual(secuencia);
      formulaActual = formula;
      getListFormulasProcesos();
   }

   public String retornarPagina() {
      return paginaEntrante;
   }

   public void modificarFormulaProceso(FormulasProcesos formulaP, int celda) {
      formulaProcesoSeleccionada = formulaP;
      if (celda == 1) {
         anularBotonLOV();
      }
      if (!listFormulasProcesosCrear.contains(formulaProcesoSeleccionada)) {
         if (listFormulasProcesosModificar.isEmpty()) {
            listFormulasProcesosModificar.add(formulaProcesoSeleccionada);
         } else if (!listFormulasProcesosModificar.contains(formulaProcesoSeleccionada)) {
            listFormulasProcesosModificar.add(formulaProcesoSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
   }

   public void modificarFormulaProceso(FormulasProcesos formulaProceso, String confirmarCambio, String valorConfirmar) {
      formulaProcesoSeleccionada = formulaProceso;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
         formulaProcesoSeleccionada.getProceso().setDescripcion(proceso);
         for (int i = 0; i < lovProcesos.size(); i++) {
            if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaProcesoSeleccionada.setProceso(lovProcesos.get(indiceUnicoElemento));
            lovProcesos.clear();
            getLovProcesos();
         } else {
            permitirIndex = false;
            contarRegistrosLov(0);
            RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listFormulasProcesosCrear.contains(formulaProcesoSeleccionada)) {
            if (listFormulasProcesosModificar.isEmpty()) {
               listFormulasProcesosModificar.add(formulaProcesoSeleccionada);
            } else if (!listFormulasProcesosModificar.contains(formulaProcesoSeleccionada)) {
               listFormulasProcesosModificar.add(formulaProcesoSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
   }

   public void cambiarIndice(FormulasProcesos formulaProceso, int celda) {
      formulaProcesoSeleccionada = formulaProceso;
      if (permitirIndex == true) {
         cualCelda = celda;
         if (celda == 0) {
            activarBotonLOV();
         }
         proceso = formulaProcesoSeleccionada.getProceso().getDescripcion();
      }
   }

   //GUARDAR
   public void guardarGeneral() {
      try {
         System.out.println("listFormulasProcesosModificar : " + listFormulasProcesosModificar);
         if (guardado == false) {
            if (!listFormulasProcesosBorrar.isEmpty()) {
               for (int i = 0; i < listFormulasProcesosBorrar.size(); i++) {
                  administrarFormulaProceso.borrarFormulasProcesos(listFormulasProcesosBorrar);
               }
               listFormulasProcesosBorrar.clear();
            }
            if (!listFormulasProcesosCrear.isEmpty()) {
               for (int i = 0; i < listFormulasProcesosCrear.size(); i++) {
                  administrarFormulaProceso.crearFormulasProcesos(listFormulasProcesosCrear);
               }
               listFormulasProcesosCrear.clear();
            }
            if (!listFormulasProcesosModificar.isEmpty()) {
               for (int i = 0; i < listFormulasProcesosModificar.size(); i++) {
                  System.out.println("listFormulasProcesosModificar.get(i).getProceso().getDescripcion() : " + listFormulasProcesosModificar.get(i).getProceso().getDescripcion());
                  System.out.println("listFormulasProcesosModificar.get(i).getPeriodicidadindependiente() : " + listFormulasProcesosModificar.get(i).getPeriodicidadindependiente());
                  System.out.println("listFormulasProcesosModificar.get(i).isCheckPeriodicidad() : " + listFormulasProcesosModificar.get(i).isCheckPeriodicidad());
               }
               administrarFormulaProceso.editarFormulasProcesos(listFormulasProcesosModificar);
               listFormulasProcesosModificar.clear();
            }
            listFormulasProcesos = null;
            getListFormulasProcesos();
            RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
            contarRegistros();
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificacionGeneral() {
      if (guardado == false) {
         cancelarModificacionFormulaProceso();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
      }
      formulaProcesoSeleccionada = null;
   }

   public void cancelarModificacionFormulaProceso() {
      restaurarTabla();
      listFormulasProcesosBorrar.clear();
      listFormulasProcesosCrear.clear();
      listFormulasProcesosModificar.clear();
      k = 0;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      permitirIndex = true;
      listFormulasProcesos = null;
      getListFormulasProcesos();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
   }

   public void editarCelda() {
      if (formulaProcesoSeleccionada != null) {
         editarFormulaProceso = formulaProcesoSeleccionada;
         if (cualCelda == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaProcesoD");
            RequestContext.getCurrentInstance().execute("PF('editarFormulaProcesoD').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void dialogoNuevoRegistro() {
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroProceso");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroProceso').show()");
   }

   public void restaurarTabla() {
      if (bandera == 1) {
         altoTabla = "180";
         formulaProceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProceso");
         formulaProceso.setFilterStyle("display: none; visibility: hidden;");
         formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaPeriodicidad");
         formulaPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
         bandera = 0;
         filtrarListFormulasProcesos = null;
         tipoLista = 0;
      }
   }

   public boolean validarNuevoNoRepetido(FormulasProcesos formulaP) {
      boolean error = false;
      if (formulaP.getProceso().getSecuencia() != null) {
         for (int i = 0; i < listFormulasProcesos.size(); i++) {
            if (listFormulasProcesos.get(i).getProceso().getSecuencia().equals(formulaP.getProceso().getSecuencia())) {
               error = true;
               RequestContext.getCurrentInstance().execute("PF('errorRegistroRep').show()");
               break;
            }
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroFP').show()");
      }
      return error;
   }

   //CREAR 
   public void agregarNuevoFormulaProceso() {
      if (!validarNuevoNoRepetido(nuevoFormulaProceso)) {
         restaurarTabla();
         k++;
         l = BigInteger.valueOf(k);
         nuevoFormulaProceso.setSecuencia(l);
         nuevoFormulaProceso.setFormula(formulaActual);
         listFormulasProcesosCrear.add(nuevoFormulaProceso);
         listFormulasProcesos.add(nuevoFormulaProceso);
         formulaProcesoSeleccionada = listFormulasProcesos.get(listFormulasProcesos.indexOf(nuevoFormulaProceso));
         nuevoFormulaProceso = new FormulasProcesos();
         nuevoFormulaProceso.setProceso(new Procesos());
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroProceso').hide()");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         contarRegistros();
      }
   }

   //LIMPIAR NUEVO REGISTRO
   /**
    */
   public void limpiarNuevaFormulaProceso() {
      nuevoFormulaProceso = new FormulasProcesos();
      nuevoFormulaProceso.setProceso(new Procesos());
   }

//DUPLICAR VC
   /**
    */
//   public void verificarRegistroDuplicar() {
//      if (formulaProcesoSeleccionada != null) {
//         duplicarFormulaProcesoM();
//      } else {
//         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//      }
//   }
////
//   public void duplicarFormulaProcesoM() {
//      if (formulaProcesoSeleccionada != null) {
//         duplicarFormulaProceso = new FormulasProcesos();
//         k++;
//         l = BigInteger.valueOf(k);
//         duplicarFormulaProceso.setFormula(formulaProcesoSeleccionada.getFormula());
//         duplicarFormulaProceso.setProceso(formulaProcesoSeleccionada.getProceso());
//         duplicarFormulaProceso.setPeriodicidadindependiente(formulaProcesoSeleccionada.getPeriodicidadindependiente());
//         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroFormulaProceso");
//         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormulaProceso').show()");
//      }
//   }
//
//   /**
//    * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
//    */
//   public void confirmarDuplicarFormulaProceso() {
//      if (!validarNuevoNoRepetido(duplicarFormulaProceso)) {
//         k++;
//         l = BigInteger.valueOf(k);
//         duplicarFormulaProceso.setSecuencia(l);
//         listFormulasProcesos.add(duplicarFormulaProceso);
//         listFormulasProcesosCrear.add(duplicarFormulaProceso);
//         formulaProcesoSeleccionada = listFormulasProcesos.get(listFormulasProcesos.indexOf(duplicarFormulaProceso));
//         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
//         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormulaProceso').hide()");
//         if (guardado == true) {
//            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//         }
//         restaurarTabla();
//         duplicarFormulaProceso = new FormulasProcesos();
//         contarRegistros();
//      }
//   }
   //LIMPIAR DUPLICAR
   /**
    * Metodo que limpia los datos de un duplicar Set
    */
//   public void limpiarDuplicarFormulaProceso() {
//      duplicarFormulaProceso = new FormulasProcesos();
//      duplicarFormulaProceso.setProceso(new Procesos());
//   }
   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   //BORRAR VC
   /**
    */
   public void verificarRegistroBorrar() {
      if (formulaProcesoSeleccionada != null) {
         borrarFormulaProceso();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void borrarFormulaProceso() {
      if (formulaProcesoSeleccionada != null) {
         if (!listFormulasProcesosModificar.isEmpty() && listFormulasProcesosModificar.contains(formulaProcesoSeleccionada)) {
            listFormulasProcesosModificar.remove(formulaProcesoSeleccionada);
            listFormulasProcesosBorrar.add(formulaProcesoSeleccionada);
         } else if (!listFormulasProcesosCrear.isEmpty() && listFormulasProcesosCrear.contains(formulaProcesoSeleccionada)) {
            listFormulasProcesosCrear.remove(formulaProcesoSeleccionada);
         } else {
            listFormulasProcesosBorrar.add(formulaProcesoSeleccionada);
         }
         listFormulasProcesos.remove(formulaProcesoSeleccionada);
         if (tipoLista == 1) {
            filtrarListFormulasProcesos.remove(formulaProcesoSeleccionada);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         getListFormulasProcesos();
         formulaProcesoSeleccionada = null;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   /**
    * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      if (bandera == 0) {
         altoTabla = "160";
         formulaProceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProceso");
         formulaProceso.setFilterStyle("width: 85% !important");
         formulaPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaPeriodicidad");
         formulaPeriodicidad.setFilterStyle("width: 66% !important");
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
         bandera = 1;
      } else {
         restaurarTabla();
      }
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {  limpiarListasValor();
      restaurarTabla();
      listFormulasProcesosBorrar.clear();
      listFormulasProcesosCrear.clear();
      listFormulasProcesosModificar.clear();
      formulaProcesoSeleccionada = null;
      k = 0;
      listFormulasProcesos = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      formulaActual = null;
      lovProcesos = null;
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (formulaProcesoSeleccionada != null) {
         if (cualCelda == 0) {
            contarRegistrosLov(0);
            RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndex(FormulasProcesos formulaProceso, int dlg, int LND) {
      RequestContext context = RequestContext.getCurrentInstance();
      formulaProcesoSeleccionada = formulaProceso;
      tipoActualizacion = LND;

      if (dlg == 0) {
         activarBotonLOV();
         contarRegistrosLov(0);
         RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("PROCESO")) {
         if (tipoNuevo == 1) {
            proceso = nuevoFormulaProceso.getProceso().getDescripcion();
         } else if (tipoNuevo == 2) {
            proceso = duplicarFormulaProceso.getProceso().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoFormulaProceso(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
         if (tipoNuevo == 1) {
            nuevoFormulaProceso.getProceso().setDescripcion(proceso);
         } else if (tipoNuevo == 2) {
            duplicarFormulaProceso.getProceso().setDescripcion(proceso);
         }
         for (int i = 0; i < lovProcesos.size(); i++) {
            if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoFormulaProceso.setProceso(lovProcesos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaProceso");
            } else if (tipoNuevo == 2) {
               duplicarFormulaProceso.setProceso(lovProcesos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaProceso");
            }
            lovProcesos.clear();
            getLovProcesos();
         } else {
            contarRegistrosLov(0);
            RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaProceso");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaProceso");
            }
         }
      }
   }

   public void actualizarProceso() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         formulaProcesoSeleccionada.setProceso(procesoSeleccionado);
         if (!listFormulasProcesosCrear.contains(formulaProcesoSeleccionada)) {
            if (listFormulasProcesosModificar.isEmpty()) {
               listFormulasProcesosModificar.add(formulaProcesoSeleccionada);
            } else if (!listFormulasProcesosModificar.contains(formulaProcesoSeleccionada)) {
               listFormulasProcesosModificar.add(formulaProcesoSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
      } else if (tipoActualizacion == 1) {
         nuevoFormulaProceso.setProceso(procesoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaProceso");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaProceso.setProceso(procesoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaProceso");
      }
      filtrarLovProcesos = null;
      procesoSeleccionado = new Procesos();
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
      RequestContext.getCurrentInstance().update("form:lovProceso");
      RequestContext.getCurrentInstance().update("form:aceptarF");
      context.reset("form:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').hide()");
   }

   public void cancelarCambioProceso() {
      filtrarLovProcesos = null;
      procesoSeleccionado = new Procesos();
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
      RequestContext.getCurrentInstance().update("form:lovProceso");
      RequestContext.getCurrentInstance().update("form:aceptarF");
      context.reset("form:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').hide()");
   }

   /**
    * Metodo que activa el boton aceptar de la pantalla y dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   public String exportXML() {
      nombreTabla = ":formExportarFormula:datosFormulaProcesoExportar";
      nombreXML = "FormulaProceso_XML";
      return nombreTabla;
   }

   /**
    * Metodo que exporta datos a PDF
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      exportPDF_NF();
   }

   public void exportPDF_NF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarFormula:datosFormulaProcesoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "FormulaProceso_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportXLS() throws IOException {
      exportXLS_NF();
   }

   public void exportXLS_NF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarFormula:datosFormulaProcesoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "FormulaProceso_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //EVENTO FILTRAR
   /**
    * Evento que cambia la lista reala a la filtrada
    */
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      formulaProcesoSeleccionada = null;
      contarRegistros();
      anularBotonLOV();
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastroFormulaProceso() {
//      RequestContext context = RequestContext.getCurrentInstance();
      if (formulaProcesoSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(formulaProcesoSeleccionada.getSecuencia(), "FORMULASPROCESOS");
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "FormulasProcesos";
            msnConfirmarRastro = "La tabla FORMULASPROCESOS tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("FORMULASPROCESOS")) {
         nombreTablaRastro = "FormulasProcesos";
         msnConfirmarRastroHistorico = "La tabla FORMULASPROCESOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLov(int tipoLov) {
      RequestContext.getCurrentInstance().update("form:infoRegistroProceso");
   }

   //GETTERS AND SETTERS
   public List<FormulasProcesos> getListFormulasProcesos() {
      if (listFormulasProcesos == null) {
         if (formulaActual != null) {
            listFormulasProcesos = administrarFormulaProceso.listFormulasProcesosParaFormula(formulaActual.getSecuencia());
         }
      }
      return listFormulasProcesos;
   }

   public void setListFormulasProceso(List<FormulasProcesos> setListFormulasProceso) {
      this.listFormulasProcesos = setListFormulasProceso;
   }

   public List<FormulasProcesos> getFiltrarListFormulasProcesos() {
      return filtrarListFormulasProcesos;
   }

   public void setFiltrarListFormulasProcesos(List<FormulasProcesos> setFiltrarListFormulasProcesos) {
      this.filtrarListFormulasProcesos = setFiltrarListFormulasProcesos;
   }

   public FormulasProcesos getNuevoFormulaProceso() {
      return nuevoFormulaProceso;
   }

   public void setNuevoFormulaProceso(FormulasProcesos setNuevoFormulaProceso) {
      this.nuevoFormulaProceso = setNuevoFormulaProceso;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public FormulasProcesos getEditarFormulaProceso() {
      return editarFormulaProceso;
   }

   public void setEditarFormulaProceso(FormulasProcesos setEditarFormulaProceso) {
      this.editarFormulaProceso = setEditarFormulaProceso;
   }

   public FormulasProcesos getDuplicarFormulasProcesos() {
      return duplicarFormulaProceso;
   }

   public void setDuplicarFormulasProcesos(FormulasProcesos setDuplicarFormulasProcesos) {
      this.duplicarFormulaProceso = setDuplicarFormulasProcesos;
   }

   public List<FormulasProcesos> getListFormulasProcesosModificar() {
      return listFormulasProcesosModificar;
   }

   public void setListFormulasProcesosModificar(List<FormulasProcesos> setListFormulasProcesosModificar) {
      this.listFormulasProcesosModificar = setListFormulasProcesosModificar;
   }

   public List<FormulasProcesos> getListFormulasProcesosCrear() {
      return listFormulasProcesosCrear;
   }

   public void setListFormulasProcesosCrear(List<FormulasProcesos> setListFormulasProcesosCrear) {
      this.listFormulasProcesosCrear = setListFormulasProcesosCrear;
   }

   public List<FormulasProcesos> getListFormulasProcesosBorrar() {
      return listFormulasProcesosBorrar;
   }

   public void setListFormulasProcesosBorrar(List<FormulasProcesos> setListFormulasProcesosBorrar) {
      this.listFormulasProcesosBorrar = setListFormulasProcesosBorrar;
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

   public List<Procesos> getLovProcesos() {
      if (lovProcesos == null) {
         lovProcesos = administrarFormulaProceso.listProcesos();
      }
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> setLovProcesos) {
      this.lovProcesos = setLovProcesos;
   }

   public List<Procesos> getFiltrarLovProcesos() {
      return filtrarLovProcesos;
   }

   public void setFiltrarLovProcesos(List<Procesos> setFiltrarLovProcesos) {
      this.filtrarLovProcesos = setFiltrarLovProcesos;
   }

   public Procesos getProcesoSeleccionada() {
      return procesoSeleccionado;
   }

   public void setProcesoSeleccionada(Procesos setProcesoSeleccionada) {
      this.procesoSeleccionado = setProcesoSeleccionada;
   }

   public Formulas getFormulaActual() {
      return formulaActual;
   }

   public void setFormulaActual(Formulas formulaActual) {
      this.formulaActual = formulaActual;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public Procesos getProcesoSeleccionado() {
      return procesoSeleccionado;
   }

   public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
      this.procesoSeleccionado = procesoSeleccionado;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFormulaProceso");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroProceso() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovProceso");
      infoRegistroProceso = String.valueOf(tabla.getRowCount());
      return infoRegistroProceso;
   }

   public void setInfoRegistroProceso(String infoRegistroProceso) {
      this.infoRegistroProceso = infoRegistroProceso;
   }

   public FormulasProcesos getFormulaProcesoSeleccionada() {
      return formulaProcesoSeleccionada;
   }

   public void setFormulaProcesoSeleccionada(FormulasProcesos formulaProcesoSeleccionada) {
      this.formulaProcesoSeleccionada = formulaProcesoSeleccionada;
   }

   public FormulasProcesos getDuplicarFormulaProceso() {
      return duplicarFormulaProceso;
   }

   public void setDuplicarFormulaProceso(FormulasProcesos duplicarFormulaProceso) {
      this.duplicarFormulaProceso = duplicarFormulaProceso;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

}
