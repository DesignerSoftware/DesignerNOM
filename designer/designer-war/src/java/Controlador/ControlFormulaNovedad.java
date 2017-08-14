package Controlador;

import Entidades.Formulas;
import Entidades.FormulasNovedades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulaNovedadInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class ControlFormulaNovedad implements Serializable {

   private static Logger log = Logger.getLogger(ControlFormulaNovedad.class);

   @EJB
   AdministrarFormulaNovedadInterface administrarFormulaNovedad;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<FormulasNovedades> listFormulasNovedades;
   private List<FormulasNovedades> filtrarListFormulasNovedades;
   private FormulasNovedades formulaNovedadSeleccionada;
   private Formulas formulaActual;
   ///////
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla VC
   private Column formulaCorto, formulaNombre, formulaSugerida, formulaCargue, formulaUsa, formulaGarantiza;
   //Otros
   private boolean aceptar;
   //modificar
   private List<FormulasNovedades> listFormulasNovedadesModificar;
   private boolean guardado;
   //crear 
   private FormulasNovedades nuevoFormulaNovedad;
   private List<FormulasNovedades> listFormulasNovedadesCrear;
   private BigInteger l;
   private int k;
   //borrar 
   private List<FormulasNovedades> listFormulasNovedadesBorrar;
   //editar celda
   private FormulasNovedades editarFormulaNovedad;
   private int cualCelda, tipoLista;
   //duplicar
   private FormulasNovedades duplicarFormulaNovedad;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private String nombreTablaRastro;
   private String nombreXML, nombreTabla;
   private String formula, infoRegistro;

   //////////////////////
//   private List<Formulas> lovFormulas;
//   private List<Formulas> filtrarLovFormulas;
//   private Formulas formulaSeleccionada;
   private boolean permitirIndex;
   private int tipoActualizacion;
   private String auxFormulaDescripcion;
   //
   private String altoTabla;
   private boolean nuevoYBOrrado;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlFormulaNovedad() {
      altoTabla = "270";
      auxFormulaDescripcion = "";
      permitirIndex = true;
      tipoActualizacion = -1;
      listFormulasNovedades = new ArrayList<FormulasNovedades>();
      //Otros
      aceptar = true;
      //borrar aficiones
      listFormulasNovedadesBorrar = new ArrayList<FormulasNovedades>();
      //crear aficiones
      listFormulasNovedadesCrear = new ArrayList<FormulasNovedades>();
      k = 0;
      //modificar aficiones
      listFormulasNovedadesModificar = new ArrayList<FormulasNovedades>();
      //editar
      editarFormulaNovedad = new FormulasNovedades();
      cualCelda = -1;
      tipoLista = 0;
      //guardar
      guardado = true;
      //Crear VC
      nuevoFormulaNovedad = new FormulasNovedades();
      nuevoFormulaNovedad.setFormula(null);
      duplicarFormulaNovedad = new FormulasNovedades();
      formulaNovedadSeleccionada = null;
      formulaActual = new Formulas();
      paginaAnterior = "nominaf";
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
      String pagActual = "formulanovedad";
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

   public void limpiarListasValor() {

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
         administrarFormulaNovedad.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirFormula(Formulas formula, String pagina) {
      paginaAnterior = pagina;
      formulaActual = formula;
      getListFormulasNovedades();
   }

   public String volverPaginaAnterior() {
      return paginaAnterior;
   }

   public void modificacionesCamposFormula(int indice) {
      formulaNovedadSeleccionada.getFormula().setNombrelargo(auxFormulaDescripcion);
      modificarFormulaNovedad(formulaNovedadSeleccionada);
   }

   public void modificarFormulaNovedad(FormulasNovedades formulaN) {
      formulaNovedadSeleccionada = formulaN;
      if (!listFormulasNovedadesCrear.contains(formulaNovedadSeleccionada)) {
         if (listFormulasNovedadesModificar.isEmpty()) {
            listFormulasNovedadesModificar.add(formulaNovedadSeleccionada);
         } else if (!listFormulasNovedadesModificar.contains(formulaNovedadSeleccionada)) {
            listFormulasNovedadesModificar.add(formulaNovedadSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosFormulaNovedad");

   }
//
//   public void modificarFormulaNovedad(int indice, String confirmarCambio, String valorConfirmar) {
//      index = indice;
//      int coincidencias = 0;
//      int indiceUnicoElemento = 0;
//      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
//         if (tipoLista == 0) {
//            formulaNovedadSeleccionada.getFormula().setNombrecorto(formula);
//         } else {
//            filtrarListFormulasNovedades.get(indice).getFormula().setNombrecorto(formula);
//         }
//         for (int i = 0; i < lovFormulas.size(); i++) {
//            if (lovFormulas.get(i).getNombrecorto().startsWith(valorConfirmar.toUpperCase())) {
//               indiceUnicoElemento = i;
//               coincidencias++;
//            }
//         }
//         if (coincidencias == 1) {
//            if (tipoLista == 0) {
//               formulaNovedadSeleccionada.setFormula(lovFormulas.get(indiceUnicoElemento));
//            } else {
//               filtrarListFormulasNovedades.get(indice).setFormula(lovFormulas.get(indiceUnicoElemento));
//            }
//            lovFormulas.clear();
//            getLovFormulas();
//         } else {
//            permitirIndex = false;
//            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
//            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
//            tipoActualizacion = 0;
//         }
//      }
//      if (coincidencias == 1) {
//         if (tipoLista == 0) {
//            if (!listFormulasNovedadesCrear.contains(formulaNovedadSeleccionada)) {
//               if (listFormulasNovedadesModificar.isEmpty()) {
//                  listFormulasNovedadesModificar.add(formulaNovedadSeleccionada);
//               } else if (!listFormulasNovedadesModificar.contains(formulaNovedadSeleccionada)) {
//                  listFormulasNovedadesModificar.add(formulaNovedadSeleccionada);
//               }
//               if (guardado == true) {
//                  guardado = false;
//                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
//               }
//            }
//         }
//         if (tipoLista == 1) {
//            if (!listFormulasNovedadesCrear.contains(filtrarListFormulasNovedades.get(indice))) {
//               if (listFormulasNovedadesModificar.isEmpty()) {
//                  listFormulasNovedadesModificar.add(filtrarListFormulasNovedades.get(indice));
//               } else if (!listFormulasNovedadesModificar.contains(filtrarListFormulasNovedades.get(indice))) {
//                  listFormulasNovedadesModificar.add(filtrarListFormulasNovedades.get(indice));
//               }
//               if (guardado == true) {
//                  guardado = false;
//                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
//               }
//            }
//         }
//         guardado = true;
//      }
//      RequestContext.getCurrentInstance().update("form:datosFormulaNovedad");
//   }

   public void posicionFormula() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      cambiarIndice(listFormulasNovedades.get(indice), columna);
   }

   public void cambiarIndice(FormulasNovedades formulaN, int celda) {
      formulaNovedadSeleccionada = formulaN;
      if (permitirIndex == true) {
         cualCelda = celda;
         auxFormulaDescripcion = formulaNovedadSeleccionada.getFormula().getNombrelargo();
         formula = formulaNovedadSeleccionada.getFormula().getNombrecorto();
      }
   }

   //GUARDAR
   public void guardarGeneral() {
      guardarCambiosFormulaNovedad();
   }

   public void guardarCambiosFormulaNovedad() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listFormulasNovedadesBorrar.isEmpty()) {
               for (int i = 0; i < listFormulasNovedadesBorrar.size(); i++) {
                  administrarFormulaNovedad.borrarFormulasNovedades(listFormulasNovedadesBorrar);
               }
               listFormulasNovedadesBorrar.clear();
            }
            if (!listFormulasNovedadesCrear.isEmpty()) {
               for (int i = 0; i < listFormulasNovedadesCrear.size(); i++) {
                  administrarFormulaNovedad.crearFormulasNovedades(listFormulasNovedadesCrear);
               }
               listFormulasNovedadesCrear.clear();
            }
            if (!listFormulasNovedadesModificar.isEmpty()) {
               administrarFormulaNovedad.editarFormulasNovedades(listFormulasNovedadesModificar);
               listFormulasNovedadesModificar.clear();
            }
            listFormulasNovedades = new ArrayList<FormulasNovedades>();
            getListFormulasNovedades();
            RequestContext.getCurrentInstance().update("form:datosFormulaNovedad");
            contarRegistros();
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } catch (Exception e) {
         log.warn("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }

   }

   public void cancelarModificacionGeneral() {
      cancelarModificacionFormulaNovedad();
      RequestContext.getCurrentInstance().update("form:datosFormulaNovedad");
   }

   public void cancelarModificacionFormulaNovedad() {
      if (bandera == 1) {
         restablecerTabla();
      }
      listFormulasNovedadesBorrar.clear();
      listFormulasNovedadesCrear.clear();
      listFormulasNovedadesModificar.clear();
      formulaNovedadSeleccionada = null;
      k = 0;
      listFormulasNovedades = new ArrayList<FormulasNovedades>();
      guardado = true;
      permitirIndex = true;
      getListFormulasNovedades();
      RequestContext.getCurrentInstance().update("form:datosFormulaNovedad");
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      if (formulaNovedadSeleccionada != null) {
         editarFormulaNovedad = formulaNovedadSeleccionada;

         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaCortoD");
            RequestContext.getCurrentInstance().execute("PF('editarFormulaCortoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaNombreD");
            RequestContext.getCurrentInstance().execute("PF('editarFormulaNombreD').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void dialogoNuevoRegistro() {
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFormula");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormula').show()");
   }

   //CREAR 
   public void agregarNuevoFormulaNovedad() {
      if (nuevoFormulaNovedad.getFormula().getSecuencia() != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            restablecerTabla();
         }

         k++;
         l = BigInteger.valueOf(k);
         nuevoFormulaNovedad.setSecuencia(l);
         listFormulasNovedadesCrear.add(nuevoFormulaNovedad);
         listFormulasNovedades.add(nuevoFormulaNovedad);
         formulaNovedadSeleccionada = listFormulasNovedades.get(listFormulasNovedades.indexOf(nuevoFormulaNovedad));

         nuevoFormulaNovedad = new FormulasNovedades();
         nuevoFormulaNovedad.setFormula(formulaActual);

         RequestContext.getCurrentInstance().update("form:datosFormulaNovedad");
         contarRegistros();
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormula').hide()");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroFN').show()");
      }
   }

   //LIMPIAR NUEVO REGISTRO
   /**
    */
   public void limpiarNuevaFormulaNovedad() {
      nuevoFormulaNovedad = new FormulasNovedades();
      nuevoFormulaNovedad.setFormula(formulaActual);
   }

//DUPLICAR VC
   /**
    */
   public void verificarRegistroDuplicar() {
      if (formulaNovedadSeleccionada != null) {
         duplicarFormulaNovedadM();
      }
   }

   public void duplicarFormulaNovedadM() {
      duplicarFormulaNovedad = new FormulasNovedades();
      k++;
      l = BigInteger.valueOf(k);

      duplicarFormulaNovedad.setFormula(formulaNovedadSeleccionada.getFormula());
      duplicarFormulaNovedad.setGarantizada(formulaNovedadSeleccionada.getGarantizada());
      duplicarFormulaNovedad.setUsaordenformulaconcepto(formulaNovedadSeleccionada.getUsaordenformulaconcepto());
      duplicarFormulaNovedad.setSugerida(formulaNovedadSeleccionada.getSugerida());
      duplicarFormulaNovedad.setCargue(formulaNovedadSeleccionada.getCargue());

      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroFormulaNovedad");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormulaNovedad').show()");
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
    */
   public void confirmarDuplicarFormulaNovedad() {
      if (duplicarFormulaNovedad.getFormula().getSecuencia() != null) {
         k++;
         l = BigInteger.valueOf(k);
         duplicarFormulaNovedad.setSecuencia(l);
         listFormulasNovedades.add(duplicarFormulaNovedad);
         listFormulasNovedadesCrear.add(duplicarFormulaNovedad);
         formulaNovedadSeleccionada = listFormulasNovedades.get(listFormulasNovedades.indexOf(duplicarFormulaNovedad));

         RequestContext.getCurrentInstance().update("form:datosFormulaNovedad");
         contarRegistros();
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormulaNovedad').hide()");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            restablecerTabla();
         }
         duplicarFormulaNovedad = new FormulasNovedades();
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegistroFN').show()");
      }
   }

   //LIMPIAR DUPLICAR
   /**
    * Metodo que limpia los datos de un duplicar Set
    */
   public void limpiarDuplicarFormulaNovedad() {
      duplicarFormulaNovedad = new FormulasNovedades();
      duplicarFormulaNovedad.setFormula(new Formulas());
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   //BORRAR VC
   /**
    */
   public void verificarRegistroBorrar() {
      if (formulaNovedadSeleccionada != null) {
         borrarFormulaNovedad();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void borrarFormulaNovedad() {
      if (formulaNovedadSeleccionada != null) {
         if (!listFormulasNovedadesModificar.isEmpty() && listFormulasNovedadesModificar.contains(formulaNovedadSeleccionada)) {
            listFormulasNovedadesModificar.remove(formulaNovedadSeleccionada);
            listFormulasNovedadesBorrar.add(formulaNovedadSeleccionada);
         } else if (!listFormulasNovedadesCrear.isEmpty() && listFormulasNovedadesCrear.contains(formulaNovedadSeleccionada)) {
            listFormulasNovedadesCrear.remove(formulaNovedadSeleccionada);
         } else {
            listFormulasNovedadesBorrar.add(formulaNovedadSeleccionada);
         }
         listFormulasNovedades.remove(formulaNovedadSeleccionada);
         if (tipoLista == 1) {
            filtrarListFormulasNovedades.remove(formulaNovedadSeleccionada);
         }
         formulaNovedadSeleccionada = null;
         RequestContext.getCurrentInstance().update("form:datosFormulaNovedad");
         contarRegistros();
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
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "250";
         formulaCorto = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaCorto");
         formulaCorto.setFilterStyle("width: 85% !important");
         formulaNombre = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaNombre");
         formulaNombre.setFilterStyle("width: 85% !important");
         formulaUsa = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaUsa");
         formulaUsa.setFilterStyle("width: 66% !important");
         formulaGarantiza = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaGarantiza");
         formulaGarantiza.setFilterStyle("width: 66% !important");
         formulaSugerida = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaSugerida");
         formulaSugerida.setFilterStyle("width: 66% !important");
         formulaCargue = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaCargue");
         formulaCargue.setFilterStyle("width: 66% !important");
         RequestContext.getCurrentInstance().update("form:datosFormulaNovedad");
         bandera = 1;
      } else if (bandera == 1) {
         restablecerTabla();
      }
   }

   public void restablecerTabla() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTabla = "270";
      formulaCorto = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaCorto");
      formulaCorto.setFilterStyle("display: none; visibility: hidden;");
      formulaNombre = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaNombre");
      formulaNombre.setFilterStyle("display: none; visibility: hidden;");
      formulaUsa = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaUsa");
      formulaUsa.setFilterStyle("display: none; visibility: hidden;");
      formulaGarantiza = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaGarantiza");
      formulaGarantiza.setFilterStyle("display: none; visibility: hidden;");
      formulaSugerida = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaSugerida");
      formulaSugerida.setFilterStyle("display: none; visibility: hidden;");
      formulaCargue = (Column) c.getViewRoot().findComponent("form:datosFormulaNovedad:formulaCargue");
      formulaCargue.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosFormulaNovedad");
      bandera = 0;
      filtrarListFormulasNovedades = null;
      tipoLista = 0;
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         restablecerTabla();
      }
      listFormulasNovedadesBorrar.clear();
      listFormulasNovedadesCrear.clear();
      listFormulasNovedadesModificar.clear();
      formulaNovedadSeleccionada = null;
      k = 0;
      listFormulasNovedades = new ArrayList<FormulasNovedades>();
      guardado = true;
      formulaActual = null;
//      lovFormulas = null;
      navegar("atras");
   }

   public void listaValoresBoton() {
      if (formulaNovedadSeleccionada != null) {
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndex(FormulasNovedades fomulaN, int dlg, int LND) {
      formulaNovedadSeleccionada = fomulaN;
      tipoActualizacion = LND;
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:FormulaDialogo");
         RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
      }

   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("FORMULA")) {
         if (tipoNuevo == 1) {
            formula = nuevoFormulaNovedad.getFormula().getNombrecorto();
         } else if (tipoNuevo == 2) {
            formula = duplicarFormulaNovedad.getFormula().getNombrecorto();
         }
      }
   }
//
//   public void autocompletarNuevoyDuplicadoFormulaNovedad(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
//      int coincidencias = 0;
//      int indiceUnicoElemento = 0;
//      RequestContext context = RequestContext.getCurrentInstance();
//      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
//         if (tipoNuevo == 1) {
//            nuevoFormulaNovedad.getFormula().setNombrecorto(formula);
//         } else if (tipoNuevo == 2) {
//            duplicarFormulaNovedad.getFormula().setNombrecorto(formula);
//         }
//         for (int i = 0; i < lovFormulas.size(); i++) {
//            if (lovFormulas.get(i).getNombrecorto().startsWith(valorConfirmar.toUpperCase())) {
//               indiceUnicoElemento = i;
//               coincidencias++;
//            }
//         }
//         if (coincidencias == 1) {
//            if (tipoNuevo == 1) {
//               nuevoFormulaNovedad.setFormula(lovFormulas.get(indiceUnicoElemento));
//               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaCorto");
//               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaDescripcion");
//            } else if (tipoNuevo == 2) {
//               duplicarFormulaNovedad.setFormula(lovFormulas.get(indiceUnicoElemento));
//               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaCorto");
//               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaDescripcion");
//            }
//            lovFormulas.clear();
//            getLovFormulas();
//         } else {
//            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
//            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
//            tipoActualizacion = tipoNuevo;
//            if (tipoNuevo == 1) {
//               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaCorto");
//               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaDescripcion");
//            } else if (tipoNuevo == 2) {
//               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaCorto");
//               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaDescripcion");
//            }
//         }
//      }
//   }

//   public void actualizarFormula() {
//      RequestContext context = RequestContext.getCurrentInstance();
//      if (tipoActualizacion == 0) {
//            formulaNovedadSeleccionada.setFormula(formulaSeleccionada);
//            if (!listFormulasNovedadesCrear.contains(formulaNovedadSeleccionada)) {
//               if (listFormulasNovedadesModificar.isEmpty()) {
//                  listFormulasNovedadesModificar.add(formulaNovedadSeleccionada);
//               } else if (!listFormulasNovedadesModificar.contains(formulaNovedadSeleccionada)) {
//                  listFormulasNovedadesModificar.add(formulaNovedadSeleccionada);
//               }
//            }
//        
//         if (guardado == true) {
//            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//         }
//         permitirIndex = true;
//         RequestContext.getCurrentInstance().update("form:formulaCorto");
//         RequestContext.getCurrentInstance().update("form:formulaNombre");
//      } else if (tipoActualizacion == 1) {
//         nuevoFormulaNovedad.setFormula(formulaSeleccionada);
//         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaCorto");
//         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaDescripcion");
//      } else if (tipoActualizacion == 2) {
//         duplicarFormulaNovedad.setFormula(formulaSeleccionada);
//         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaCorto");
//         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaDescripcion");
//      }
////      filtrarLovFormulas = null;
////      formulaSeleccionada = null;
//      aceptar = true;
//      formulaNovedadSeleccionada = null;
//      formulaNovedadSeleccionada = null;
//      tipoActualizacion = -1;
//      /*
//         RequestContext.getCurrentInstance().update("form:FormulaDialogo");
//         RequestContext.getCurrentInstance().update("form:lovFormula");
//         RequestContext.getCurrentInstance().update("form:aceptarF");*/
//      context.reset("form:lovFormula:globalFilter");
//      RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
//      RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').hide()");
//   }
   public void cancelarCambioFormula() {
//      filtrarLovFormulas = null;
//      formulaSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovFormula:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').hide()");
   }

   /**
    * Metodo que activa el boton aceptar de la pantalla y dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   public String exportXML() {
      nombreTabla = ":formExportarFormula:datosFormulaNovedadExportar";
      nombreXML = "FormulaNovedad_XML";
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
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarFormula:datosFormulaNovedadExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "FormulaNovedad_PDF", false, false, "UTF-8", null, null);
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
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarFormula:datosFormulaNovedadExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "FormulaNovedad_XLS", false, false, "UTF-8", null, null);
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
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }
   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

   public void verificarRastro() {
      verificarRastroFormulaNovedad();
   }

   public void verificarRastroFormulaNovedad() {
      if (formulaNovedadSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(formulaNovedadSeleccionada.getSecuencia(), "FORMULASNOVEDADES");
         formulaNovedadSeleccionada = null;
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "FormulasNovedades";
            msnConfirmarRastro = "La tabla FORMULASNOVEDADES tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
      } else if (administrarRastros.verificarHistoricosTabla("FORMULASNOVEDADES")) {
         nombreTablaRastro = "FormulasNovedades";
         msnConfirmarRastroHistorico = "La tabla FORMULASNOVEDADES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //GETTERS AND SETTERS
   public List<FormulasNovedades> getListFormulasNovedades() {
      try {
         if (listFormulasNovedades.isEmpty()) {
            if (formulaActual.getSecuencia() != null) {
               listFormulasNovedades = administrarFormulaNovedad.listFormulasNovedadesParaFormula(formulaActual.getSecuencia());
            } else {
               listFormulasNovedades = administrarFormulaNovedad.listFormulasNovedadesParaFormula(null);
            }
            if (listFormulasNovedades == null) {
               listFormulasNovedades = new ArrayList<FormulasNovedades>();
            }
         }
         return listFormulasNovedades;
      } catch (Exception e) {
         log.warn("Error...!! getListFormulasNovedades " + e.toString());
         return null;
      }
   }

   public void setListFormulasNovedades(List<FormulasNovedades> setListFormulasNovedades) {
      this.listFormulasNovedades = setListFormulasNovedades;
   }

   public List<FormulasNovedades> getFiltrarListFormulasNovedades() {
      return filtrarListFormulasNovedades;
   }

   public void setFiltrarListFormulasNovedades(List<FormulasNovedades> setFiltrarListFormulasNovedades) {
      this.filtrarListFormulasNovedades = setFiltrarListFormulasNovedades;
   }

   public FormulasNovedades getNuevoFormulaNovedad() {
      if (nuevoFormulaNovedad.getFormula() == null) {
         nuevoFormulaNovedad.setFormula(formulaActual);
      }
      return nuevoFormulaNovedad;
   }

   public void setNuevoFormulaNovedad(FormulasNovedades setNuevoFormulaNovedad) {
      this.nuevoFormulaNovedad = setNuevoFormulaNovedad;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public FormulasNovedades getEditarFormulaNovedad() {
      return editarFormulaNovedad;
   }

   public void setEditarFormulaNovedad(FormulasNovedades setEditarFormulaNovedad) {
      this.editarFormulaNovedad = setEditarFormulaNovedad;
   }

   public FormulasNovedades getDuplicarFormulaNovedad() {
      return duplicarFormulaNovedad;
   }

   public void setDuplicarFormulaNovedad(FormulasNovedades setDuplicarFormulaNovedad) {
      this.duplicarFormulaNovedad = setDuplicarFormulaNovedad;
   }

   public List<FormulasNovedades> getListFormulasNovedadesModificar() {
      return listFormulasNovedadesModificar;
   }

   public void setListFormulasNovedadesModificar(List<FormulasNovedades> setListFormulasNovedadesModificar) {
      this.listFormulasNovedadesModificar = setListFormulasNovedadesModificar;
   }

   public List<FormulasNovedades> getListFormulasNovedadesCrear() {
      return listFormulasNovedadesCrear;
   }

   public void setListFormulasNovedadesCrear(List<FormulasNovedades> setListFormulasNovedadesCrear) {
      this.listFormulasNovedadesCrear = setListFormulasNovedadesCrear;
   }

   public List<FormulasNovedades> getListFormulasNovedadesBorrar() {
      return listFormulasNovedadesBorrar;
   }

   public void setListFormulasNovedadesBorrar(List<FormulasNovedades> setListFormulasNovedadesBorrar) {
      this.listFormulasNovedadesBorrar = setListFormulasNovedadesBorrar;
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
//
//   public List<Formulas> getLovFormulas() {
//      lovFormulas = administrarFormulaNovedad.listFormulas(formulaActual.getSecuencia());
//
//      return lovFormulas;
//   }
//
//   public void setLovFormulas(List<Formulas> setLovFormulas) {
//      this.lovFormulas = setLovFormulas;
//   }
//
//   public List<Formulas> getFiltrarLovFormulas() {
//      return filtrarLovFormulas;
//   }
//
//   public void setFiltrarLovFormulas(List<Formulas> setFiltrarLovFormulas) {
//      this.filtrarLovFormulas = setFiltrarLovFormulas;
//   }
//
//   public Formulas getFormulaSeleccionada() {
//      return formulaSeleccionada;
//   }
//
//   public void setFormulaSeleccionada(Formulas tipoDiaSeleccionado) {
//      this.formulaSeleccionada = tipoDiaSeleccionado;
//   }

   public Formulas getFormulaActual() {
      return formulaActual;
   }

   public void setFormulaActual(Formulas formulaActual) {
      this.formulaActual = formulaActual;
   }

   public FormulasNovedades getFormulaNovedadSeleccionada() {
      return formulaNovedadSeleccionada;
   }

   public void setFormulaNovedadSeleccionada(FormulasNovedades formulaNovedadSeleccionada) {
      this.formulaNovedadSeleccionada = formulaNovedadSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFormulaNovedad");
      infoRegistro = String.valueOf(tabla.getRowCount());
      if (tabla.getRowCount() == 1) {
         nuevoYBOrrado = true;
         RequestContext.getCurrentInstance().update("form:insertar");
      } else {
         nuevoYBOrrado = false;
         RequestContext.getCurrentInstance().update("form:insertar");
      }
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }
//
//   public String getInfoRegistroFormula() {
//      getLovFormulas();
//      if (lovFormulas != null) {
//         infoRegistroFormula = "Cantidad de registros : " + lovFormulas.size();
//      } else {
//         infoRegistroFormula = "Cantidad de registros : 0";
//      }
//      return infoRegistroFormula;
//   }
//
//   public void setInfoRegistroFormula(String infoRegistroFormula) {
//      this.infoRegistroFormula = infoRegistroFormula;
//   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public AdministrarFormulaNovedadInterface getAdministrarFormulaNovedad() {
      return administrarFormulaNovedad;
   }

   public void setAdministrarFormulaNovedad(AdministrarFormulaNovedadInterface administrarFormulaNovedad) {
      this.administrarFormulaNovedad = administrarFormulaNovedad;
   }

   public boolean isNuevoYBOrrado() {
//      log.info("isNuevoYBOrrado() : " + nuevoYBOrrado);
//      log.info("listFormulasNovedades : " + listFormulasNovedades);
      if (listFormulasNovedades != null) {
         if (listFormulasNovedades.isEmpty()) {
            nuevoYBOrrado = false;
         } else {
            nuevoYBOrrado = true;
         }
      } else {
         nuevoYBOrrado = false;
      }
      return nuevoYBOrrado;
   }

   public void setNuevoYBOrrado(boolean nuevoYBOrrado) {
      this.nuevoYBOrrado = nuevoYBOrrado;
   }

}
