/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Formulas;
import Entidades.Operandos;
import Entidades.TiposFormulas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposFormulasInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
public class ControlTipoFormula implements Serializable {

   @EJB
   AdministrarTiposFormulasInterface administrarTiposFormulas;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //Parametros que llegan
   private Operandos operando;
   //LISTA INFOREPORTES
   private List<TiposFormulas> listaTiposFormulas;
   private List<TiposFormulas> filtradosListaTiposFormulas;
   private TiposFormulas tipoFormulaSeleccionada;
   //L.O.V INFOREPORTES
   private List<TiposFormulas> lovlistaTiposFormulas;
   private List<TiposFormulas> lovfiltradoslistaTiposFormulas;
   private TiposFormulas tipoFormulaLovSeleccionada;
   //editar celda
   private TiposFormulas editarTiposFormulas;
   private boolean aceptarEditar;
   private int cualCelda, tipoLista;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   //RASTROS
   private boolean guardado;
   //Crear Novedades
   private List<TiposFormulas> listaTiposFormulasCrear;
   public TiposFormulas nuevoTipoFormula;
   public TiposFormulas duplicarTipoFormula;
   private int k;
   private BigInteger l;
   private String mensajeValidacion;
   //Modificar Novedades
   private List<TiposFormulas> listaTiposFormulasModificar;
   //Borrar Novedades
   private List<TiposFormulas> listaTiposFormulasBorrar;
   //AUTOCOMPLETAR
   private String Formula;
   //Columnas Tabla Ciudades
   private Column tiposFormulasIniciales, tiposFormulasFinales, tiposFormulasObjetos;
   //ALTO SCROLL TABLA
   private String altoTabla;
   private boolean cambiosPagina;
   //L.O.V FORMULAS
   private List<Formulas> lovListaFormulas;
   private List<Formulas> lovFiltradosListaFormulas;
   private Formulas seleccionFormulas;
   //Enviar a Formulas
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   public String infoRegistro;

   public ControlTipoFormula() {
      cambiosPagina = true;
      nuevoTipoFormula = new TiposFormulas();
      nuevoTipoFormula.setFechainicial(new Date());
      lovListaFormulas = null;
      aceptar = true;
      tipoFormulaSeleccionada = null;
      guardado = true;
      tipoLista = 0;
      listaTiposFormulasBorrar = new ArrayList<TiposFormulas>();
      listaTiposFormulasCrear = new ArrayList<TiposFormulas>();
      listaTiposFormulasModificar = new ArrayList<TiposFormulas>();
      altoTabla = "270";
      duplicarTipoFormula = new TiposFormulas();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTiposFormulas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      operando = (Operandos) mapParametros.get("operandoActual");
      listaTiposFormulas = null;
      getListaTiposFormulas();
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
         String pagActual = "tipoformula";
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
      limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

   //UBICACION CELDA
   public void cambiarIndice(TiposFormulas tipoF, int celda) {
      tipoFormulaSeleccionada = tipoF;
      cualCelda = celda;
   }

   //AUTOCOMPLETAR
   public void modificarTiposFormulas(TiposFormulas tipoF, String confirmarCambio, String valorConfirmar) {
      tipoFormulaSeleccionada = tipoF;;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;

      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaTiposFormulasCrear.contains(tipoFormulaSeleccionada)) {

            if (listaTiposFormulasModificar.isEmpty()) {
               listaTiposFormulasModificar.add(tipoFormulaSeleccionada);
            } else if (!listaTiposFormulasModificar.contains(tipoFormulaSeleccionada)) {
               listaTiposFormulasModificar.add(tipoFormulaSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
         contarRegistros();
      } else if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         tipoFormulaSeleccionada.getFormula().setNombrecorto(Formula);

         for (int i = 0; i < listaTiposFormulas.size(); i++) {
            if (listaTiposFormulas.get(i).getFormula().getNombrecorto().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            tipoFormulaSeleccionada.setFormula(lovListaFormulas.get(indiceUnicoElemento));
            lovListaFormulas.clear();
            getLovListaFormulas();
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndex(TiposFormulas tipoF, int campo, int tipoAct) {
      tipoFormulaSeleccionada = tipoF;
      tipoActualizacion = tipoAct;
      if (tipoAct > 0) {
         tipoFormulaSeleccionada = null;
      }
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
      }
   }

   public void asignarIndex(int campo, int tipoAct) {
      tipoActualizacion = tipoAct;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:formulasDialogo");
         RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
      }
   }

   public void guardarVariables(BigInteger secuencia) {
      if (tipoFormulaSeleccionada == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
      if (listaTiposFormulasCrear.isEmpty() && listaTiposFormulasBorrar.isEmpty() && listaTiposFormulasModificar.isEmpty()) {
         if (tipoFormulaSeleccionada != null) {
            RequestContext.getCurrentInstance().execute("PF('dirigirFormula()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (tipoFormulaSeleccionada != null) {
         if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("form:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = 0;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("FORMULA")) {
         if (tipoNuevo == 1) {
            Formula = nuevoTipoFormula.getFormula().getNombrecorto();
         } else if (tipoNuevo == 2) {
            Formula = duplicarTipoFormula.getFormula().getNombrecorto();
         }
      }

   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         if (tipoNuevo == 1) {
            nuevoTipoFormula.getFormula().setNombrecorto(Formula);
         } else if (tipoNuevo == 2) {
            duplicarTipoFormula.getFormula().setNombrecorto(Formula);
         }
         for (int i = 0; i < lovListaFormulas.size(); i++) {
            if (lovListaFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoTipoFormula.setFormula(lovListaFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstado");
            } else if (tipoNuevo == 2) {
               duplicarTipoFormula.setFormula(lovListaFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstado");
            }
            lovListaFormulas.clear();
            getLovListaFormulas();
         } else {
            RequestContext.getCurrentInstance().update("form:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormula");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoEstado");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormula");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstado");
            }
         }
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      tipoFormulaSeleccionada = null;
      contarRegistros();
   }

   public void confirmarDuplicar() {
      int pasa = 0;
      mensajeValidacion = new String();
      if (duplicarTipoFormula.getFechainicial() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoFormula");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoFormula').show()");
      }
      if (pasa == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         //Falta Ponerle el Operando al cual se agregará
         duplicarTipoFormula.setOperando(operando);
         listaTiposFormulas.add(duplicarTipoFormula);
         listaTiposFormulasCrear.add(duplicarTipoFormula);
         tipoFormulaSeleccionada = listaTiposFormulas.get(listaTiposFormulas.indexOf(duplicarTipoFormula));
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
         contarRegistros();
         duplicarTipoFormula = new TiposFormulas();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarTipoFormula");
         RequestContext.getCurrentInstance().execute("PF('DuplicarTipoFormula').hide()");
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "250";
         tiposFormulasIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasIniciales");
         tiposFormulasIniciales.setFilterStyle("width: 85% !important;");
         tiposFormulasFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasFinales");
         tiposFormulasFinales.setFilterStyle("width: 85% !important;");
         tiposFormulasObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasObjetos");
         tiposFormulasObjetos.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
         contarRegistros();
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void cancelarYSalir() {
      cancelarModificacion();
      salir();
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTabla();
      }
      listaTiposFormulasBorrar.clear();
      listaTiposFormulasCrear.clear();
      listaTiposFormulasModificar.clear();
      tipoFormulaSeleccionada = null;
      k = 0;
      listaTiposFormulas = null;
      guardado = true;
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
      contarRegistros();
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (tipoFormulaSeleccionada != null) {
         editarTiposFormulas = tipoFormulaSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasIniciales");
            RequestContext.getCurrentInstance().execute("PF('editarFechasIniciales').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasFinales");
            RequestContext.getCurrentInstance().execute("PF('editarFechasFinales').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulas");
            RequestContext.getCurrentInstance().execute("PF('editarFormulas').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEstados");
            RequestContext.getCurrentInstance().execute("PF('editarEstados').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposFormulasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TiposFormulasPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposFormulasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TiposFormulasXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //LIMPIAR NUEVO REGISTRO CIUDAD
   public void limpiarNuevoTiposFormulas() {
      nuevoTipoFormula = new TiposFormulas();
   }

   public void limpiarduplicarTiposFormulas() {
      duplicarTipoFormula = new TiposFormulas();
   }

   //DUPLICAR Operando
   public void duplicarTF() {
      if (tipoFormulaSeleccionada != null) {
         duplicarTipoFormula = new TiposFormulas();
         k++;
         l = BigInteger.valueOf(k);
         duplicarTipoFormula.setSecuencia(l);
         duplicarTipoFormula.setFormula(tipoFormulaSeleccionada.getFormula());
         duplicarTipoFormula.setFechainicial(tipoFormulaSeleccionada.getFechainicial());
         duplicarTipoFormula.setFechafinal(tipoFormulaSeleccionada.getFechafinal());
         duplicarTipoFormula.setOperando(tipoFormulaSeleccionada.getOperando());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoFormula");
         RequestContext.getCurrentInstance().execute("PF('DuplicarTipoFormula').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void guardarYSalir() {
      guardarCambiosTiposFormulas();
      salir();
   }

   //GUARDAR
   public void guardarCambiosTiposFormulas() {
      if (guardado == false) {
         if (!listaTiposFormulasBorrar.isEmpty()) {
            for (int i = 0; i < listaTiposFormulasBorrar.size(); i++) {
               administrarTiposFormulas.borrarTiposFormulas(listaTiposFormulasBorrar.get(i));
            }
            listaTiposFormulasBorrar.clear();
         }

         if (!listaTiposFormulasCrear.isEmpty()) {
            for (int i = 0; i < listaTiposFormulasCrear.size(); i++) {
               administrarTiposFormulas.crearTiposFormulas(listaTiposFormulasCrear.get(i));
            }
            listaTiposFormulasCrear.clear();
         }
         if (!listaTiposFormulasModificar.isEmpty()) {
            for (int i = 0; i < listaTiposFormulasModificar.size(); i++) {
               administrarTiposFormulas.modificarTiposFormulas(listaTiposFormulasCrear.get(i));
            }
            listaTiposFormulasModificar.clear();
         }
         listaTiposFormulas = null;

         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
         contarRegistros();
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

//RASTROS 
   public void verificarRastro() {
      if (tipoFormulaSeleccionada != null) {
         int result = administrarRastros.obtenerTabla(tipoFormulaSeleccionada.getSecuencia(), "TIPOSFUNCIONES");
         System.out.println("resultado: " + result);
         if (result == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (result == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (result == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (result == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (result == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
//         } else {
//            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//         }
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSFUNCIONES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void actualizarFormulas() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         tipoFormulaSeleccionada.setFormula(seleccionFormulas);
         if (!listaTiposFormulasCrear.contains(tipoFormulaSeleccionada)) {
            if (listaTiposFormulasModificar.isEmpty()) {
               listaTiposFormulasModificar.add(tipoFormulaSeleccionada);
            } else if (!listaTiposFormulasModificar.contains(tipoFormulaSeleccionada)) {
               listaTiposFormulasModificar.add(tipoFormulaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
         contarRegistros();
      } else if (tipoActualizacion == 1) {
         nuevoTipoFormula.setFormula(seleccionFormulas);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoFormula");
      } else if (tipoActualizacion == 2) {
         duplicarTipoFormula.setFormula(seleccionFormulas);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoFormula");
      }
      filtradosListaTiposFormulas = null;
      seleccionFormulas = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
      //RequestContext.getCurrentInstance().update("formularioDialogos:LOVFormulas");
   }

   public void cancelarCambioFormulas() {
      lovFiltradosListaFormulas = null;
      seleccionFormulas = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('formulasDialogo').hide()");
   }

   public void agregarNuevoTipoFormula() {
      int pasa = 0;
      int pasa2 = 0;
      mensajeValidacion = new String();

      if (nuevoTipoFormula.getFechainicial() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (nuevoTipoFormula.getFechafinal() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Final\n";
         pasa++;
      }
      if (nuevoTipoFormula.getFormula().getNombrecorto() == null) {
         mensajeValidacion = mensajeValidacion + " * Nombre Corto\n";
         pasa++;
      }

      if (nuevoTipoFormula.getFechainicial() != null && nuevoTipoFormula.getFechafinal() != null) {
         if (nuevoTipoFormula.getFechafinal().before(nuevoTipoFormula.getFechainicial())) {
            RequestContext.getCurrentInstance().update("formularioDialogos:errorFechas");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            pasa2++;
         }
      }

      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoFormula");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoFormula').show()");
      }

      if (pasa == 0 && pasa2 == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         k++;
         l = BigInteger.valueOf(k);
         nuevoTipoFormula.setSecuencia(l);
         System.out.println("Operando: " + operando);
         nuevoTipoFormula.setOperando(operando);

         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         listaTiposFormulasCrear.add(nuevoTipoFormula);
         listaTiposFormulas.add(nuevoTipoFormula);
         tipoFormulaSeleccionada = listaTiposFormulas.get(listaTiposFormulas.indexOf(nuevoTipoFormula));
         nuevoTipoFormula = new TiposFormulas();
         RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoTipoFormula').hide()");
      }
   }

   public void restaurarTabla() {
      //CERRAR FILTRADO
      FacesContext c = FacesContext.getCurrentInstance();
      altoTabla = "270";
      System.out.println("Desactivar");
      System.out.println("TipoLista= " + tipoLista);
      tiposFormulasIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasIniciales");
      tiposFormulasIniciales.setFilterStyle("display: none; visibility: hidden;");
      tiposFormulasFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasFinales");
      tiposFormulasFinales.setFilterStyle("display: none; visibility: hidden;");
      tiposFormulasObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFormulas:tiposFormulasObjetos");
      tiposFormulasObjetos.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
      contarRegistros();
      bandera = 0;
      filtradosListaTiposFormulas = null;
      tipoLista = 0;
   }

   //BORRAR CIUDADES
   public void borrarTipoFormula() {

      if (tipoFormulaSeleccionada != null) {
         if (!listaTiposFormulasModificar.isEmpty() && listaTiposFormulasModificar.contains(tipoFormulaSeleccionada)) {
            listaTiposFormulasModificar.remove(tipoFormulaSeleccionada);
            listaTiposFormulasBorrar.add(tipoFormulaSeleccionada);
         } else if (!listaTiposFormulasCrear.isEmpty() && listaTiposFormulasCrear.contains(tipoFormulaSeleccionada)) {
            listaTiposFormulasCrear.remove(tipoFormulaSeleccionada);
         } else {
            listaTiposFormulasBorrar.add(tipoFormulaSeleccionada);
         }
         listaTiposFormulas.remove(tipoFormulaSeleccionada);
         if (tipoLista == 1) {
            filtradosListaTiposFormulas.remove(tipoFormulaSeleccionada);
         }
         RequestContext.getCurrentInstance().update("form:datosTiposFormulas");
         contarRegistros();
         tipoFormulaSeleccionada = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void salir() {  limpiarListasValor();
      if (bandera == 1) {
         restaurarTabla();
      }
      listaTiposFormulasBorrar.clear();
      listaTiposFormulasCrear.clear();
      listaTiposFormulasModificar.clear();
      tipoFormulaSeleccionada = null;
      k = 0;
      listaTiposFormulas = null;
      guardado = true;
      navegar("atras");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   //Getter & Setter
   public List<TiposFormulas> getListaTiposFormulas() {
      if (listaTiposFormulas == null && operando != null) {
         listaTiposFormulas = administrarTiposFormulas.buscarTiposFormulas(operando.getSecuencia());
      }
      return listaTiposFormulas;
   }

   public void setListaTiposFormulas(List<TiposFormulas> listaTiposFormulas) {
      this.listaTiposFormulas = listaTiposFormulas;
   }

   public List<TiposFormulas> getFiltradosListaTiposFormulas() {
      return filtradosListaTiposFormulas;
   }

   public void setFiltradosListaTiposFormulas(List<TiposFormulas> filtradosListaTiposFormulas) {
      this.filtradosListaTiposFormulas = filtradosListaTiposFormulas;
   }

   public TiposFormulas getEditarTiposFormulas() {
      return editarTiposFormulas;
   }

   public void setEditarTiposFormulas(TiposFormulas editarTiposFormulas) {
      this.editarTiposFormulas = editarTiposFormulas;
   }

   public boolean isAceptarEditar() {
      return aceptarEditar;
   }

   public void setAceptarEditar(boolean aceptarEditar) {
      this.aceptarEditar = aceptarEditar;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public TiposFormulas getNuevoTipoFormula() {
      return nuevoTipoFormula;
   }

   public void setNuevoTipoFormula(TiposFormulas nuevoTipoFormula) {
      this.nuevoTipoFormula = nuevoTipoFormula;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public Operandos getOperando() {
      return operando;
   }

   public void setOperando(Operandos operando) {
      this.operando = operando;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public TiposFormulas getDuplicarTipoFormula() {
      return duplicarTipoFormula;
   }

   public void setDuplicarTipoFormula(TiposFormulas duplicarTipoFormula) {
      this.duplicarTipoFormula = duplicarTipoFormula;
   }

   public List<Formulas> getLovListaFormulas() {
      if (lovListaFormulas == null) {
         lovListaFormulas = administrarTiposFormulas.lovFormulas();
      }
      return lovListaFormulas;
   }

   public void setLovListaFormulas(List<Formulas> lovListaFormulas) {
      this.lovListaFormulas = lovListaFormulas;
   }

   public List<Formulas> getLovFiltradosListaFormulas() {
      return lovFiltradosListaFormulas;
   }

   public void setLovFiltradosListaFormulas(List<Formulas> lovFiltradosListaFormulas) {
      this.lovFiltradosListaFormulas = lovFiltradosListaFormulas;
   }

   public Formulas getSeleccionFormulas() {
      return seleccionFormulas;
   }

   public void setSeleccionFormulas(Formulas seleccionFormulas) {
      this.seleccionFormulas = seleccionFormulas;
   }

   public TiposFormulas getTipoFormulaSeleccionada() {
      return tipoFormulaSeleccionada;
   }

   public void setTipoFormulaSeleccionada(TiposFormulas tipoFormulaSeleccionada) {
      this.tipoFormulaSeleccionada = tipoFormulaSeleccionada;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposFormulas");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
