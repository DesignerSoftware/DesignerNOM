/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Operandos;
import Entidades.TiposConstantes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposConstantesInterface;
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
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlTipoConstante")
@SessionScoped
public class ControlTipoConstante implements Serializable {

   private static Logger log = Logger.getLogger(ControlTipoConstante.class);

   @EJB
   AdministrarTiposConstantesInterface administrarTiposConstantes;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //Parametros que llegan
   private Operandos operando;
   //LISTA INFOREPORTES
   private List<TiposConstantes> listaTiposConstantes;
   private List<TiposConstantes> filtradosListaTiposConstantes;
   private TiposConstantes tipoConstanteSeleccionada;
   //L.O.V INFOREPORTES
   private List<TiposConstantes> lovlistaTiposConstantes;
   private List<TiposConstantes> filtradosLovTiposConstantes;
   private TiposConstantes tipoConstanteLovSeleccionado;
   //editar celda
   private TiposConstantes editarTiposConstantes;
   private boolean aceptarEditar;
   private int cualCelda, tipoLista;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   //RASTROS
   private boolean guardado;
   //Crear Novedades
   private List<TiposConstantes> listaTiposConstantesCrear;
   private TiposConstantes nuevoTipoConstante;
   private TiposConstantes duplicarTipoConstante;
   private int k;
   private BigInteger l;
   private String mensajeValidacion;
   //Modificar Novedades
   private List<TiposConstantes> listaTiposConstantesModificar;
   //Borrar Novedades
   private List<TiposConstantes> listaTiposConstantesBorrar;
   //AUTOCOMPLETAR
   private String Formula;
   //Columnas Tabla Ciudades
   private Column tiposConstantesTipos, tiposConstantesIniciales, tiposConstantesFinales, tiposConstantesReales, tiposConstantesDates, tiposConstantesCadenas;
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
   private String infoRegistro;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private DataTable tabla;

   public ControlTipoConstante() {
      cambiosPagina = true;
      nuevoTipoConstante = new TiposConstantes();
      nuevoTipoConstante.setFechainicial(new Date(20, 0, 1));
      nuevoTipoConstante.setFechafinal(new Date(9999 - 1900, 11, 31));
      aceptar = true;
      tipoConstanteSeleccionada = null;
      guardado = true;
      tipoLista = 0;
      listaTiposConstantesBorrar = new ArrayList<TiposConstantes>();
      listaTiposConstantesCrear = new ArrayList<TiposConstantes>();
      listaTiposConstantesModificar = new ArrayList<TiposConstantes>();
      altoTabla = "286";
      duplicarTipoConstante = new TiposConstantes();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovlistaTiposConstantes = null;
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
         administrarTiposConstantes.obtenerConexion(ses.getId());
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
      operando = (Operandos) mapParametros.get("operandoActual");
      listaTiposConstantes = null;
      getListaTiposConstantes();
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
         
      } else {
       */
      String pagActual = "tipoconstante";

      //           controlRastro.recibirDatosTabla(tipoConctanteSeleccionada.getSecuencia(), "Conceptos", pagActual);
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

   //UBICACION CELDA
   public void cambiarIndice(TiposConstantes tipoC, int celda) {
      tipoConstanteSeleccionada = tipoC;
      cualCelda = celda;
   }

   //BORRAR CIUDADES
   public void borrarTipoConstante() {
      if (tipoConstanteSeleccionada != null) {
         if (!listaTiposConstantesModificar.isEmpty() && listaTiposConstantesModificar.contains(tipoConstanteSeleccionada)) {
            listaTiposConstantesModificar.remove(tipoConstanteSeleccionada);
            listaTiposConstantesBorrar.add(tipoConstanteSeleccionada);
         } else if (!listaTiposConstantesCrear.isEmpty() && listaTiposConstantesCrear.contains(tipoConstanteSeleccionada)) {
            listaTiposConstantesCrear.remove(tipoConstanteSeleccionada);
         } else {
            listaTiposConstantesBorrar.add(tipoConstanteSeleccionada);
         }
         listaTiposConstantes.remove(tipoConstanteSeleccionada);
         if (tipoLista == 1) {
            filtradosListaTiposConstantes.remove(tipoConstanteSeleccionada);
         }
         RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
         contarRegistros();
         tipoConstanteSeleccionada = null;
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
      if (bandera == 0) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTabla = "266";
         tiposConstantesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesTipos");
         tiposConstantesTipos.setFilterStyle("width: 85% !important;");
         tiposConstantesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesIniciales");
         tiposConstantesIniciales.setFilterStyle("width: 85% !important;");
         tiposConstantesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesFinales");
         tiposConstantesFinales.setFilterStyle("width: 85% !important;");
         tiposConstantesReales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesReales");
         tiposConstantesReales.setFilterStyle("width: 85% !important;");
         tiposConstantesDates = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesDates");
         tiposConstantesDates.setFilterStyle("width: 85% !important;");
         tiposConstantesCadenas = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesCadenas");
         tiposConstantesCadenas.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
         contarRegistros();
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void restaurarTabla() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTabla = "286";
      log.info("Desactivar");
      log.info("TipoLista= " + tipoLista);
      tiposConstantesTipos = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesTipos");
      tiposConstantesTipos.setFilterStyle("display: none; visibility: hidden;");
      tiposConstantesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesIniciales");
      tiposConstantesIniciales.setFilterStyle("display: none; visibility: hidden;");
      tiposConstantesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesFinales");
      tiposConstantesFinales.setFilterStyle("display: none; visibility: hidden;");
      tiposConstantesReales = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesReales");
      tiposConstantesReales.setFilterStyle("display: none; visibility: hidden;");
      tiposConstantesDates = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesDates");
      tiposConstantesDates.setFilterStyle("display: none; visibility: hidden;");
      tiposConstantesCadenas = (Column) c.getViewRoot().findComponent("form:datosTiposConstantes:tiposConstantesCadenas");
      tiposConstantesCadenas.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
      contarRegistros();
      bandera = 0;
      filtradosListaTiposConstantes = null;
      tipoLista = 0;
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         restaurarTabla();
      }
      listaTiposConstantesBorrar.clear();
      listaTiposConstantesCrear.clear();
      listaTiposConstantesModificar.clear();
      tipoConstanteSeleccionada = null;
      k = 0;
      listaTiposConstantes = null;
      guardado = true;
      navegar("atras");
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (tipoConstanteSeleccionada != null) {
         editarTiposConstantes = tipoConstanteSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipos");
            RequestContext.getCurrentInstance().execute("PF('editarTipos').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasIniciales");
            RequestContext.getCurrentInstance().execute("PF('editarFechasIniciales').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasFinales");
            RequestContext.getCurrentInstance().execute("PF('editarFechasFinales').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValoresReales");
            RequestContext.getCurrentInstance().execute("PF('editarValoresReales').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValoresDates");
            RequestContext.getCurrentInstance().execute("PF('editarValoresDates').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValoresCadenas");
            RequestContext.getCurrentInstance().execute("PF('editarValoresCadenas').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void cancelarYSalir() {
//      cancelarModificacion();
      salir();
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTabla();
      }
      listaTiposConstantesBorrar.clear();
      listaTiposConstantesCrear.clear();
      listaTiposConstantesModificar.clear();
      listaTiposConstantes = null;
      tipoConstanteSeleccionada = null;
      k = 0;
      guardado = true;
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
      contarRegistros();
   }

   //DUPLICAR Operando
   public void duplicarTC() {
      if (tipoConstanteSeleccionada != null) {
         duplicarTipoConstante = new TiposConstantes();
         k++;
         l = BigInteger.valueOf(k);
         duplicarTipoConstante.setSecuencia(l);
         duplicarTipoConstante.setFechainicial(tipoConstanteSeleccionada.getFechainicial());
         duplicarTipoConstante.setFechafinal(tipoConstanteSeleccionada.getFechafinal());
         duplicarTipoConstante.setOperando(tipoConstanteSeleccionada.getOperando());
         duplicarTipoConstante.setTipo(tipoConstanteSeleccionada.getTipo());
         duplicarTipoConstante.setValorreal(tipoConstanteSeleccionada.getValorreal());
         duplicarTipoConstante.setValordate(tipoConstanteSeleccionada.getValordate());
         duplicarTipoConstante.setValorstring(tipoConstanteSeleccionada.getValorstring());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoConstante");
         RequestContext.getCurrentInstance().execute("PF('DuplicarTipoConstante').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //RASTROS 
   public void verificarRastro() {
      if (tipoConstanteSeleccionada != null) {
         int result = administrarRastros.obtenerTabla(tipoConstanteSeleccionada.getSecuencia(), "TIPOSCONSTANTES");
         log.info("resultado: " + result);
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

   public void guardarYSalir() {
      guardarCambiosTiposConstantes();
      salir();
   }

   //GUARDAR
   public void guardarCambiosTiposConstantes() {
      if (guardado == false) {
         if (!listaTiposConstantesBorrar.isEmpty()) {
            for (int i = 0; i < listaTiposConstantesBorrar.size(); i++) {
               administrarTiposConstantes.borrarTiposConstantes(listaTiposConstantesBorrar.get(i));
            }
            listaTiposConstantesBorrar.clear();
         }
         if (!listaTiposConstantesCrear.isEmpty()) {
            for (int i = 0; i < listaTiposConstantesCrear.size(); i++) {
               administrarTiposConstantes.crearTiposConstantes(listaTiposConstantesCrear.get(i));
            }
            listaTiposConstantesCrear.clear();
         }
         if (!listaTiposConstantesModificar.isEmpty()) {
            for (int i = 0; i < listaTiposConstantesModificar.size(); i++) {
               administrarTiposConstantes.modificarTiposConstantes(listaTiposConstantesModificar.get(i));
            }
            listaTiposConstantesModificar.clear();
         }
         listaTiposConstantes = null;
         cambiosPagina = true;
         tipoConstanteSeleccionada = null;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
         contarRegistros();
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void seleccionarTipo(String estadoTipo, int indice, int celda) {
      if (estadoTipo.equals("NUMERICO")) {
         tipoConstanteSeleccionada.setTipo("N");
         numericoB = false;
         cadenaB = true;
         fechaB = true;
         tipoConstanteSeleccionada.setValordate(null);
         tipoConstanteSeleccionada.setValorstring(null);
      } else if (estadoTipo.equals("FECHA")) {
         tipoConstanteSeleccionada.setTipo("F");
         numericoB = true;
         cadenaB = true;
         fechaB = false;
         tipoConstanteSeleccionada.setValorreal(null);
         tipoConstanteSeleccionada.setValorstring(null);

      } else if (estadoTipo.equals("CADENA")) {
         tipoConstanteSeleccionada.setTipo("C");
         numericoB = true;
         cadenaB = true;
         fechaB = false;
         tipoConstanteSeleccionada.setValordate(null);
         tipoConstanteSeleccionada.setValorreal(null);
      }

      if (!listaTiposConstantesCrear.contains(tipoConstanteSeleccionada)) {
         if (listaTiposConstantesModificar.isEmpty()) {
            listaTiposConstantesModificar.add(tipoConstanteSeleccionada);
         } else if (!listaTiposConstantesModificar.contains(tipoConstanteSeleccionada)) {
            listaTiposConstantesModificar.add(tipoConstanteSeleccionada);
         }
      }
      if (guardado == true) {
         guardado = false;
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
      contarRegistros();
   }

   public void seleccionarTipoNuevoTipoConstante(String estadoTipo, int tipoNuevo) {
      if (tipoNuevo == 1) {
         if (estadoTipo.equals("NUMERICO")) {
            nuevoTipoConstante.setTipo("N");
            numericoBN = false;
            cadenaBN = true;
            fechaBN = true;
            nuevoTipoConstante.setValordate(null);
            nuevoTipoConstante.setValorstring(null);
         } else if (estadoTipo.equals("FECHA")) {
            nuevoTipoConstante.setTipo("F");
            numericoBN = true;
            cadenaBN = true;
            fechaBN = false;
            nuevoTipoConstante.setValorreal(null);
            nuevoTipoConstante.setValorstring(null);
         } else if (estadoTipo.equals("CADENA")) {
            nuevoTipoConstante.setTipo("C");
            numericoBN = true;
            cadenaBN = true;
            fechaBN = false;
            nuevoTipoConstante.setValordate(null);
            nuevoTipoConstante.setValorreal(null);
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoValorReal");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoValorDate");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoValorCadena");
      } else {
         if (estadoTipo.equals("NUMERICO")) {
            duplicarTipoConstante.setTipo("N");
            numericoBD = false;
            cadenaBD = true;
            fechaBD = true;
            duplicarTipoConstante.setValordate(null);
            duplicarTipoConstante.setValorstring(null);
         } else if (estadoTipo.equals("FECHA")) {
            duplicarTipoConstante.setTipo("F");
            numericoBD = true;
            cadenaBD = true;
            fechaBD = false;
            duplicarTipoConstante.setValorreal(null);
            duplicarTipoConstante.setValorstring(null);
         } else if (estadoTipo.equals("CADENA")) {
            duplicarTipoConstante.setTipo("C");
            numericoBD = true;
            cadenaBD = true;
            fechaBD = false;
            duplicarTipoConstante.setValordate(null);
            duplicarTipoConstante.setValorreal(null);
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarValorReal");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarValorDate");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarValorCadena");
      }
   }

   public void revisarTipo(TiposConstantes tipoC) {
      tipoConstanteSeleccionada = tipoC;
      if (tipoConstanteSeleccionada.getTipo().equals("N")) {
         numericoB = false;
         fechaB = true;
         cadenaB = true;
      } else if (tipoConstanteSeleccionada.getTipo().equals("F")) {
         numericoB = true;
         fechaB = false;
         cadenaB = true;
      } else if (tipoConstanteSeleccionada.getTipo().equals("C")) {
         numericoB = true;
         fechaB = true;
         cadenaB = false;
      }
   }

   public void revisarTipoN() {
      if (nuevoTipoConstante.getTipo().equals("N")) {
         numericoBN = false;
         fechaBN = true;
         cadenaBN = true;
      } else if (nuevoTipoConstante.getTipo().equals("F")) {
         numericoBN = true;
         fechaBN = false;
         cadenaBN = true;
      } else if (nuevoTipoConstante.getTipo().equals("C")) {
         numericoBN = true;
         fechaBN = true;
         cadenaBN = false;
      }
   }

   public void revisarTipoD() {
      if (duplicarTipoConstante.getTipo().equals("N")) {
         numericoBD = false;
         fechaBD = true;
         cadenaBD = true;
      } else if (duplicarTipoConstante.getTipo().equals("F")) {
         numericoBD = true;
         fechaBD = false;
         cadenaBD = true;
      } else if (duplicarTipoConstante.getTipo().equals("C")) {
         numericoBD = true;
         fechaBD = true;
         cadenaBD = false;
      }
   }

//AUTOCOMPLETAR
   public void modificarTiposConstantes(TiposConstantes tipoC, String confirmarCambio, String valorConfirmar) {
      tipoConstanteSeleccionada = tipoC;

      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaTiposConstantesCrear.contains(tipoConstanteSeleccionada)) {

            if (listaTiposConstantesModificar.isEmpty()) {
               listaTiposConstantesModificar.add(tipoConstanteSeleccionada);
            } else if (!listaTiposConstantesModificar.contains(tipoConstanteSeleccionada)) {
               listaTiposConstantesModificar.add(tipoConstanteSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
         RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
         contarRegistros();
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      tipoConstanteSeleccionada = null;
      contarRegistros();
   }

   public void agregarNuevoTipoConstante() {
      int pasa = 0;
      mensajeValidacion = new String();

      if (nuevoTipoConstante.getFechainicial() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (nuevoTipoConstante.getFechafinal() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Final\n";
         pasa++;
      }
      if (pasa == 0) {
         if (nuevoTipoConstante.getFechafinal().after(nuevoTipoConstante.getFechainicial())) {
            if (validarNuevoTraslapes(nuevoTipoConstante)) {

               if (bandera == 1) {
                  restaurarTabla();
               }
               //AGREGAR REGISTRO A LA LISTA NOVEDADES .
               k++;
               l = BigInteger.valueOf(k);
               nuevoTipoConstante.setSecuencia(l);
               log.info("Operando: " + operando);
               nuevoTipoConstante.setOperando(operando);

               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
               listaTiposConstantesCrear.add(nuevoTipoConstante);
               listaTiposConstantes.add(nuevoTipoConstante);
               tipoConstanteSeleccionada = listaTiposConstantes.get(listaTiposConstantes.indexOf(nuevoTipoConstante));
               nuevoTipoConstante = new TiposConstantes();
               nuevoTipoConstante.setFechainicial(new Date(20, 0, 1));
               nuevoTipoConstante.setFechafinal(new Date(9999 - 1900, 11, 31));
               RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
               contarRegistros();
               if (guardado == true) {
                  guardado = false;
                  cambiosPagina = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().execute("PF('NuevoTipoConstante').hide()");

            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechasTraslapos').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoConstante");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoConstante').show()");
      }
   }

   public void confirmarDuplicar() {
      int pasa = 0;
      mensajeValidacion = new String();
      if (duplicarTipoConstante.getFechainicial() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (duplicarTipoConstante.getFechafinal() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Final\n";
         pasa++;
      }
      if (pasa == 0) {
         if (duplicarTipoConstante.getFechafinal().after(duplicarTipoConstante.getFechainicial())) {
            if (validarNuevoTraslapes(duplicarTipoConstante)) {

               if (bandera == 1) {
                  restaurarTabla();
               }
               cambiosPagina = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
               //Falta Ponerle el Operando al cual se agregará
               duplicarTipoConstante.setOperando(operando);
               listaTiposConstantes.add(duplicarTipoConstante);
               listaTiposConstantesCrear.add(duplicarTipoConstante);
               tipoConstanteSeleccionada = listaTiposConstantes.get(listaTiposConstantes.indexOf(duplicarTipoConstante));

               if (guardado == true) {
                  guardado = false;
                  cambiosPagina = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
               RequestContext.getCurrentInstance().update("form:datosTiposConstantes");
               duplicarTipoConstante = new TiposConstantes();
               RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarTipoConstante");
               RequestContext.getCurrentInstance().execute("PF('DuplicarTipoConstante').hide()");

            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechasTraslapos').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoConstante");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoConstante').show()");
      }
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

   public boolean validarNuevoTraslapes(TiposConstantes tipoContante) {
      boolean continuar = true;
      for (int i = 0; i < listaTiposConstantes.size(); i++) {
         if (hayTraslaposFechas(listaTiposConstantes.get(i).getFechainicial(), listaTiposConstantes.get(i).getFechafinal(),
                 tipoContante.getFechainicial(), tipoContante.getFechafinal())) {
            continuar = false;
         }
      }
      return continuar;
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposConstantesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TiposConstantesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposConstantesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TiposConstantesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //LIMPIAR NUEVO REGISTRO CIUDAD
   public void limpiarNuevoTiposConstantes() {
      nuevoTipoConstante = new TiposConstantes();
      nuevoTipoConstante.setFechainicial(new Date(20, 0, 1));
      nuevoTipoConstante.setFechafinal(new Date(9999 - 1900, 11, 31));
   }

   //LIMPIAR Duplicar REGISTRO CIUDAD
   public void limpiarDuplicarTiposConstantes() {
      duplicarTipoConstante = new TiposConstantes();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void recordarSeleccion() {
      if (tipoConstanteSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposConstantes");
         tabla.setSelection(tipoConstanteSeleccionada);
      }
   }

//Getter & Setter
   public List<TiposConstantes> getListaTiposConstantes() {
      if (listaTiposConstantes == null && operando != null) {
         listaTiposConstantes = administrarTiposConstantes.buscarTiposConstantes(operando.getSecuencia());
      }
      return listaTiposConstantes;
   }

   public void setListaTiposConstantes(List<TiposConstantes> listaTiposConstantes) {
      this.listaTiposConstantes = listaTiposConstantes;
   }

   public List<TiposConstantes> getFiltradosListaTiposConstantes() {
      return filtradosListaTiposConstantes;
   }

   public void setFiltradosListaTiposConstantes(List<TiposConstantes> filtradosListaTiposConstantes) {
      this.filtradosListaTiposConstantes = filtradosListaTiposConstantes;
   }

   public TiposConstantes getEditarTiposConstantes() {
      return editarTiposConstantes;
   }

   public void setEditarTiposConstantes(TiposConstantes editarTiposConstantes) {
      this.editarTiposConstantes = editarTiposConstantes;
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

   public TiposConstantes getNuevoTipoConstante() {
      return nuevoTipoConstante;
   }

   public void setNuevoTipoConstante(TiposConstantes nuevoTipoConstante) {
      this.nuevoTipoConstante = nuevoTipoConstante;
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

   public TiposConstantes getDuplicarTipoConstante() {
      return duplicarTipoConstante;
   }

   public void setDuplicarTipoConstante(TiposConstantes duplicarTipoConstante) {
      this.duplicarTipoConstante = duplicarTipoConstante;
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

   public TiposConstantes getTipoConstanteSeleccionada() {
      return tipoConstanteSeleccionada;
   }

   public void setTipoConstanteSeleccionada(TiposConstantes tipoConstanteSeleccionada) {
      this.tipoConstanteSeleccionada = tipoConstanteSeleccionada;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposConstantes");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }
}
