/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Operandos;
import Entidades.TiposBloques;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposBloquesInterface;
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
public class ControlTipoBloque implements Serializable {

   @EJB
   AdministrarTiposBloquesInterface administrarTiposBloques;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //Parametros que llegan
   private Operandos operando;
   //LISTA INFOREPORTES
   private List<TiposBloques> listaTiposBloques;
   private List<TiposBloques> filtradosListaTiposBloques;
   private TiposBloques tipoBloqueSeleccionado;
   //L.O.V INFOREPORTES
   private List<TiposBloques> lovTiposBloques;
   private List<TiposBloques> lovfiltradosTiposBloques;
   private TiposBloques tipoBloqueLovSeleccionada;
   //editar celda
   private TiposBloques editarTiposBloques;
   private boolean aceptarEditar;
   private int cualCelda, tipoLista;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   //RASTROS
   private boolean guardado;
   //Crear Novedades
   private List<TiposBloques> listaTiposBloquesCrear;
   public TiposBloques nuevoTipoBloque;
   public TiposBloques duplicarTipoBloque;
   private int k;
   private BigInteger l;
   private String mensajeValidacion;
   //Modificar Novedades
   private List<TiposBloques> listaTiposBloquesModificar;
   //Borrar Novedades
   private List<TiposBloques> listaTiposBloquesBorrar;
   //AUTOCOMPLETAR
   private String Formula;
   //Columnas Tabla Ciudades
   private Column tiposBloquesIniciales, tiposBloquesFinales, tiposBloquesTipos, tiposBloquesSQL;
   //ALTO SCROLL TABLA
   private String altoTabla;
   private boolean cambiosPagina;
   //Booleanos para activar casillas en la Tabla
   private boolean numericoB;
   private boolean fechaB;
   private boolean cadenaB;
   //Booleanos para activar casillas en nuevo registro
   private boolean numericoBN;
   private boolean fechaBN;
   private boolean cadenaBN;
   //Booleanos para activar casillas en duplicar registro
   private boolean numericoBD;
   private boolean fechaBD;
   private boolean cadenaBD;
   //String que guarda el editar
   private String editorE;
   public BigInteger secuenciaOperando;
   public String tipoOperando;
   public Operandos operandoRegistro;
   private String paginaAnterior = "nominaf";
   public String infoRegistro;
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlTipoBloque() {
      listaTiposBloques = null;
      cambiosPagina = true;
      nuevoTipoBloque = new TiposBloques();
      nuevoTipoBloque.setFechainicial(new Date());
      aceptar = true;
      tipoBloqueSeleccionado = null;
      guardado = true;
      tipoLista = 0;
      listaTiposBloquesBorrar = new ArrayList<TiposBloques>();
      listaTiposBloquesCrear = new ArrayList<TiposBloques>();
      listaTiposBloquesModificar = new ArrayList<TiposBloques>();
      altoTabla = "270";
      duplicarTipoBloque = new TiposBloques();
      nuevoTipoBloque.setFechainicial(new Date());
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarRastros.obtenerConexion(ses.getId());
         administrarTiposBloques.obtenerConexion(ses.getId());
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
      listaTiposBloques = null;
      getListaTiposBloques();
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
         String pagActual = "tipobloque";
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
      limpiarListasValor();
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

   public void guardarVariables() {
      if (listaTiposBloquesBorrar.isEmpty() && listaTiposBloquesCrear.isEmpty() && listaTiposBloquesModificar.isEmpty()) {
         RequestContext.getCurrentInstance().execute("PF('dirigirDependencia()");
      } else {
         guardado = false;
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void cambiarEditor() {
      System.out.println("Bloque Tipos Bloques : " + editarTiposBloques.getBloqueplsql());
      System.out.println("Editar Tipo Bloque" + editarTiposBloques.getSecuencia());
      for (int i = 0; i < listaTiposBloques.size(); i++) {
         if (editarTiposBloques.getSecuencia().equals(listaTiposBloques.get(i).getSecuencia())) {
            System.out.println("ENTRO");
            listaTiposBloques.get(i).setBloqueplsql(editarTiposBloques.getBloqueplsql());
            listaTiposBloquesModificar.add(listaTiposBloques.get(i));
         }
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTiposBloques");
      contarRegistros();
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (tipoBloqueSeleccionado != null) {
         editarTiposBloques = tipoBloqueSeleccionado;

         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasIniciales");
            RequestContext.getCurrentInstance().execute("PF('editarFechasIniciales').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasFinales");
            RequestContext.getCurrentInstance().execute("PF('editarFechasFinales').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSQL");
            RequestContext.getCurrentInstance().execute("PF('editarSQL').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

//UBICACION CELDA
   public void cambiarIndice(TiposBloques tipoB, int celda) {
      tipoBloqueSeleccionado = tipoB;
      cualCelda = celda;
      editorE = tipoBloqueSeleccionado.getBloqueplsql();
   }

//BORRAR CIUDADES
   public void borrarTipoBloque() {

      if (tipoBloqueSeleccionado != null) {
         if (!listaTiposBloquesModificar.isEmpty() && listaTiposBloquesModificar.contains(tipoBloqueSeleccionado)) {
            listaTiposBloquesModificar.remove(tipoBloqueSeleccionado);
            listaTiposBloquesBorrar.add(tipoBloqueSeleccionado);
         } else if (!listaTiposBloquesCrear.isEmpty() && listaTiposBloquesCrear.contains(tipoBloqueSeleccionado)) {
            listaTiposBloquesCrear.remove(tipoBloqueSeleccionado);
         } else {
            listaTiposBloquesBorrar.add(tipoBloqueSeleccionado);
         }
         listaTiposBloques.remove(tipoBloqueSeleccionado);
         if (tipoLista == 1) {
            filtradosListaTiposBloques.remove(tipoBloqueSeleccionado);
         }
         RequestContext.getCurrentInstance().update("form:datosTiposBloques");
         contarRegistros();
         tipoBloqueSeleccionado = null;
         if (guardado == true) {
            guardado = false;
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "250";
         tiposBloquesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesIniciales");
         tiposBloquesIniciales.setFilterStyle("width: 85% !important;");
         tiposBloquesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesFinales");
         tiposBloquesFinales.setFilterStyle("width: 85% !important;");
         tiposBloquesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesTipos");
         tiposBloquesTipos.setFilterStyle("width: 85% !important;");
         tiposBloquesSQL = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesSQL");
         tiposBloquesSQL.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposBloques");
         contarRegistros();
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void restaurarTabla() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTabla = "270";
      System.out.println("Desactivar");
      System.out.println("TipoLista= " + tipoLista);
      tiposBloquesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesIniciales");
      tiposBloquesIniciales.setFilterStyle("display: none; visibility: hidden;");
      tiposBloquesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesFinales");
      tiposBloquesFinales.setFilterStyle("display: none; visibility: hidden;");
      tiposBloquesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesTipos");
      tiposBloquesTipos.setFilterStyle("display: none; visibility: hidden;");
      tiposBloquesSQL = (Column) c.getViewRoot().findComponent("form:datosTiposBloques:tiposBloquesSQL");
      tiposBloquesSQL.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosTiposBloques");
      contarRegistros();
      bandera = 0;
      filtradosListaTiposBloques = null;
      tipoLista = 0;
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         restaurarTabla();
      }
      listaTiposBloquesBorrar.clear();
      listaTiposBloquesCrear.clear();
      listaTiposBloquesModificar.clear();
      tipoBloqueSeleccionado = null;
      k = 0;
      listaTiposBloques = null;
      guardado = true;
      navegar("atras");
   }

   public void cancelarYSalir() {
      cancelarModificacion();
      salir();
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTabla();
      }
      listaTiposBloquesBorrar.clear();
      listaTiposBloquesCrear.clear();
      listaTiposBloquesModificar.clear();
      tipoBloqueSeleccionado = null;
      k = 0;
      listaTiposBloques = null;
      guardado = true;
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosTiposBloques");
      contarRegistros();
   }

   //DUPLICAR Operando
   public void duplicarTC() {
      if (tipoBloqueSeleccionado != null) {
         duplicarTipoBloque = new TiposBloques();
         k++;
         l = BigInteger.valueOf(k);
         duplicarTipoBloque.setSecuencia(l);
         duplicarTipoBloque.setFechainicial(tipoBloqueSeleccionado.getFechainicial());
         duplicarTipoBloque.setFechafinal(tipoBloqueSeleccionado.getFechafinal());
         duplicarTipoBloque.setOperando(tipoBloqueSeleccionado.getOperando());
         duplicarTipoBloque.setTipo(tipoBloqueSeleccionado.getTipo());
         duplicarTipoBloque.setBloqueplsql(tipoBloqueSeleccionado.getBloqueplsql());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoBloque");
         RequestContext.getCurrentInstance().execute("PF('DuplicarTipoBloque').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //RASTROS 
   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoBloqueSeleccionado != null) {
         int result = administrarRastros.obtenerTabla(tipoBloqueSeleccionado.getSecuencia(), "TIPOSCONSTANTES");
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSCONSTANTES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }
   //GUARDAR

   public void guardarYSalir() {
      guardarCambiosTiposBloques();
      salir();
   }

   public void guardarCambiosTiposBloques() {
      if (guardado == false) {
         System.out.println("Realizando Operaciones Novedades");
         if (!listaTiposBloquesBorrar.isEmpty()) {
            for (int i = 0; i < listaTiposBloquesBorrar.size(); i++) {
               System.out.println("Borrando..." + listaTiposBloquesBorrar.size());
               administrarTiposBloques.borrarTiposBloques(listaTiposBloquesBorrar.get(i));
            }
            System.out.println("Entra");
            listaTiposBloquesBorrar.clear();
         }
         if (!listaTiposBloquesCrear.isEmpty()) {
            for (int i = 0; i < listaTiposBloquesCrear.size(); i++) {
               System.out.println("Creando...");
               administrarTiposBloques.crearTiposBloques(listaTiposBloquesCrear.get(i));
            }
            System.out.println("LimpiaLista");
            listaTiposBloquesCrear.clear();
         }
         if (!listaTiposBloquesModificar.isEmpty()) {
            for (int i = 0; i < listaTiposBloquesModificar.size(); i++) {
               administrarTiposBloques.modificarTiposBloques(listaTiposBloquesModificar.get(i));
            }
            listaTiposBloquesModificar.clear();
         }
         listaTiposBloques = null;
         cambiosPagina = true;
         tipoBloqueSeleccionado = null;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTiposBloques");
         contarRegistros();
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void seleccionarTipo(String estadoTipo, int indice, int celda) {
      if (estadoTipo.equals("NUMERICO")) {
         tipoBloqueSeleccionado.setTipo("NUMBER");
      } else if (estadoTipo.equals("CARACTER")) {
         tipoBloqueSeleccionado.setTipo("VARCHAR");
      } else if (estadoTipo.equals("FECHA")) {
         tipoBloqueSeleccionado.setTipo("DATE");
      }

      if (!listaTiposBloquesCrear.contains(tipoBloqueSeleccionado)) {
         if (listaTiposBloquesModificar.isEmpty()) {
            listaTiposBloquesModificar.add(tipoBloqueSeleccionado);
         } else if (!listaTiposBloquesModificar.contains(tipoBloqueSeleccionado)) {
            listaTiposBloquesModificar.add(tipoBloqueSeleccionado);
         }
      }
      if (guardado == true) {
         guardado = false;
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosTiposBloques");
      contarRegistros();
   }

   public void seleccionarTipoNuevoTipoBloque(String estadoTipo, int tipoNuevo) {
      if (tipoNuevo == 1) {
         if (estadoTipo.equals("NUMERICO")) {
            nuevoTipoBloque.setTipo("NUMBER");
         } else if (estadoTipo.equals("CARACTER")) {
            nuevoTipoBloque.setTipo("VARCHAR");
         } else if (estadoTipo.equals("FECHA")) {
            nuevoTipoBloque.setTipo("DATE");
         }
      } else if (estadoTipo.equals("NUMERICO")) {
         duplicarTipoBloque.setTipo("NUMBER");
      } else if (estadoTipo.equals("CARACTER")) {
         duplicarTipoBloque.setTipo("VARCHAR");
      } else if (estadoTipo.equals("FECHA")) {
         duplicarTipoBloque.setTipo("DATE");
      }

   }

//AUTOCOMPLETAR
   public void modificarTiposBloques(TiposBloques tipoB, String column, String valor) {
      tipoBloqueSeleccionado = tipoB;
      if (column.equalsIgnoreCase("N")) {
         if (!listaTiposBloquesCrear.contains(tipoBloqueSeleccionado)) {
            if (listaTiposBloquesModificar.isEmpty()) {
               listaTiposBloquesModificar.add(tipoBloqueSeleccionado);
            } else if (!listaTiposBloquesModificar.contains(tipoBloqueSeleccionado)) {
               listaTiposBloquesModificar.add(tipoBloqueSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosTiposBloques");
         contarRegistros();
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      tipoBloqueSeleccionado = null;
      contarRegistros();
   }

   public void agregarNuevoTipoBloque() {
      int pasa = 0;
      int pasa2 = 0;
      mensajeValidacion = new String();
      if (nuevoTipoBloque.getFechainicial() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (nuevoTipoBloque.getFechafinal() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Final\n";
         pasa++;
      }
      if (nuevoTipoBloque.getFechainicial() != null && nuevoTipoBloque.getFechafinal() != null) {
         if (nuevoTipoBloque.getFechafinal().before(nuevoTipoBloque.getFechainicial())) {
            RequestContext.getCurrentInstance().update("formularioDialogos:errorFechas");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
            pasa2++;
         }
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoBloque");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoBloque').show()");
      }
      if (pasa == 0 && pasa2 == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         k++;
         l = BigInteger.valueOf(k);
         nuevoTipoBloque.setSecuencia(l);
         System.out.println("Operando: " + operando);
         nuevoTipoBloque.setOperando(operando);

         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         listaTiposBloquesCrear.add(nuevoTipoBloque);
         listaTiposBloques.add(nuevoTipoBloque);
         tipoBloqueSeleccionado = listaTiposBloques.get(listaTiposBloques.indexOf(nuevoTipoBloque));
         nuevoTipoBloque = new TiposBloques();
         RequestContext.getCurrentInstance().update("form:datosTiposBloques");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoTipoBloque').hide()");
      }
   }

   public void confirmarDuplicar() {
      int pasa = 0;
      mensajeValidacion = new String();
      if (duplicarTipoBloque.getFechainicial() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoBloque");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoBloque').show()");
      }
      if (pasa == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         duplicarTipoBloque.setOperando(operando);
         listaTiposBloques.add(duplicarTipoBloque);
         listaTiposBloquesCrear.add(duplicarTipoBloque);
         tipoBloqueSeleccionado = listaTiposBloques.get(listaTiposBloques.indexOf(duplicarTipoBloque));

         if (guardado == true) {
            guardado = false;
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosTiposBloques");
         duplicarTipoBloque = new TiposBloques();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarTipoBloque");
         RequestContext.getCurrentInstance().execute("PF('DuplicarTipoBloque').hide()");
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposBloquesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TiposBloquesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposBloquesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TiposBloquesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //LIMPIAR NUEVO REGISTRO CIUDAD
   public void limpiarNuevoTiposBloques() {
      nuevoTipoBloque = new TiposBloques();
   }

   //LIMPIAR Duplicar REGISTRO CIUDAD
   public void limpiarDuplicarTiposBloques() {
      duplicarTipoBloque = new TiposBloques();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   //Getter & Setter
   public List<TiposBloques> getListaTiposBloques() {
      if (listaTiposBloques == null && operando != null) {
         listaTiposBloques = administrarTiposBloques.buscarTiposBloques(operando.getSecuencia());
      }
      return listaTiposBloques;
   }

   public void setListaTiposBloques(List<TiposBloques> listaTiposBloques) {
      this.listaTiposBloques = listaTiposBloques;
   }

   public List<TiposBloques> getFiltradosListaTiposBloques() {
      return filtradosListaTiposBloques;
   }

   public void setFiltradosListaTiposBloques(List<TiposBloques> filtradosListaTiposBloques) {
      this.filtradosListaTiposBloques = filtradosListaTiposBloques;
   }

   public TiposBloques getEditarTiposBloques() {
      return editarTiposBloques;
   }

   public void setEditarTiposBloques(TiposBloques editarTiposBloques) {
      this.editarTiposBloques = editarTiposBloques;
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

   public TiposBloques getNuevoTipoBloque() {
      return nuevoTipoBloque;
   }

   public void setNuevoTipoBloque(TiposBloques nuevoTipoBloque) {
      this.nuevoTipoBloque = nuevoTipoBloque;
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

   public TiposBloques getDuplicarTipoBloque() {
      return duplicarTipoBloque;
   }

   public void setDuplicarTipoBloque(TiposBloques duplicarTipoBloque) {
      this.duplicarTipoBloque = duplicarTipoBloque;
   }

   public boolean isNumericoB() {
      return numericoB;
   }

   public void setNumericoB(boolean numericoB) {
      this.numericoB = numericoB;
   }

   public boolean isFechaB() {
      return fechaB;
   }

   public void setFechaB(boolean fechaB) {
      this.fechaB = fechaB;
   }

   public boolean isCadenaB() {
      return cadenaB;
   }

   public void setCadenaB(boolean cadenaB) {
      this.cadenaB = cadenaB;
   }

   public boolean isNumericoBN() {
      return numericoBN;
   }

   public void setNumericoBN(boolean numericoBN) {
      this.numericoBN = numericoBN;
   }

   public boolean isFechaBN() {
      return fechaBN;
   }

   public void setFechaBN(boolean fechaBN) {
      this.fechaBN = fechaBN;
   }

   public boolean isCadenaBN() {
      return cadenaBN;
   }

   public void setCadenaBN(boolean cadenaBN) {
      this.cadenaBN = cadenaBN;
   }

   public boolean isNumericoBD() {
      return numericoBD;
   }

   public void setNumericoBD(boolean numericoBD) {
      this.numericoBD = numericoBD;
   }

   public boolean isFechaBD() {
      return fechaBD;
   }

   public void setFechaBD(boolean fechaBD) {
      this.fechaBD = fechaBD;
   }

   public boolean isCadenaBD() {
      return cadenaBD;
   }

   public void setCadenaBD(boolean cadenaBD) {
      this.cadenaBD = cadenaBD;
   }

   public String getEditorE() {
      return editorE;
   }

   public void setEditorE(String editorE) {
      this.editorE = editorE;
   }

   public BigInteger getSecuenciaOperando() {
      return secuenciaOperando;
   }

   public void setSecuenciaOperando(BigInteger secuenciaOperando) {
      this.secuenciaOperando = secuenciaOperando;
   }

   public Operandos getOperandoSeleccionado() {
      return operandoRegistro;
   }

   public void setOperandoSeleccionado(Operandos operandoRegistro) {
      this.operandoRegistro = operandoRegistro;
   }

   public TiposBloques getTipoBloqueSeleccionado() {
      return tipoBloqueSeleccionado;
   }

   public void setTipoBloqueSeleccionado(TiposBloques tipoBloqueSeleccionado) {
      this.tipoBloqueSeleccionado = tipoBloqueSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposBloques");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }
}
