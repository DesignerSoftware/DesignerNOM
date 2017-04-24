package Controlador;

import Entidades.Formulas;
import Entidades.FormulasProcesos;
import Entidades.Operandos;
import Entidades.OperandosLogs;
import Entidades.Procesos;
import Entidades.Tipospagos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarProcesosInterface;
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
public class ControlProceso implements Serializable {

   @EJB
   AdministrarProcesosInterface administrarProcesos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //
   private List<Procesos> listaProcesos;
   private List<Procesos> filtrarListaProcesos;
   private Procesos procesoSeleccionado;
   //
   private List<FormulasProcesos> listaFormulasProcesos;
   private List<FormulasProcesos> filtrarListaFormulasProcesos;
   private FormulasProcesos formulaProcesoSeleccionada;
   //
   private List<OperandosLogs> listaOperandosLogs;
   private List<OperandosLogs> filtrarListaOperandosLogs;
   private OperandosLogs operandoLogSeleccionado;

   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla 
   private Column procesoCodigo, procesoDescripcion, procesoTipoPago, procesoComentario, procesoNumero, procesoContabilizacion, procesoSolucionNodo, procesoAdelanto, procesoSobregiro, procesoAutomatico;
   private Column formulaProcesoFormula, formulaProcesoPeriodicidad;
   private Column operandoOperando, operandoDescripcion;
   //Otros
   private boolean aceptar;
   //modificar
   private List<Procesos> listProcesosModificar;
   private List<FormulasProcesos> listFormulasProcesosModificar;
   private List<OperandosLogs> listOperandosLogsModificar;
   private boolean guardado, guardadoFormulasProcesos, guardadoOperandosLogs;
   //crear 
   private Procesos nuevoProceso;
   private FormulasProcesos nuevoFormulaProceso;
   private OperandosLogs nuevoOperandoLog;
   private List<Procesos> listProcesosCrear;
   private List<FormulasProcesos> listFormulasProcesosCrear;
   private List<OperandosLogs> listOperandosLogsCrear;
   private BigInteger l;
   private int k;
   //borrar 
   private List<Procesos> listProcesosBorrar;
   private List<FormulasProcesos> listFormulasProcesosBorrar;
   private List<OperandosLogs> listOperandosLogsBorrar;
   //editar celda
   private Procesos editarProceso;
   private FormulasProcesos editarFormulaProceso;
   private OperandosLogs editarOperandoLog;
   private int cualCelda, tipoLista, cualCeldaFormulaProceso, tipoListaFormulasProcesos, cualCeldaOperandoLog, tipoListaOperandoLog;
   //duplicar
   private Procesos duplicarProceso;
   private FormulasProcesos duplicarFormulaProceso;
   private OperandosLogs duplicarOperandoLog;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private String nombreXML, nombreTabla;
   private String tipoPago, formula, operando;

   ///////////LOV///////////
   private List<Tipospagos> lovTiposPagos;
   private List<Tipospagos> filtrarLovTiposPagos;
   private Tipospagos tipoPagoLovSeleccionado;

   private List<Formulas> lovFormulas;
   private List<Formulas> filtrarLovFormulas;
   private Formulas formulaLovSeleccionada;

   private List<Operandos> lovOperandos;
   private List<Operandos> filtrarLovOperandos;
   private Operandos operandoLovSeleccionado;

   private boolean permitirIndex, permitirIndexFormulasProcesos, permitirIndexOperandosLogs;
   private int tipoActualizacion;
   private short auxCodigoProceso;
   private String auxDescripcionProceso;
   //
   private boolean cambiosPagina;
   private String altoTablaProcesos, altoTablaForProYOperLogs;
   //
   private Procesos procesoNuevoClonado, procesoBaseClonado;
   private List<Procesos> lovProcesos;
   private List<Procesos> filtrarLovProcesos;
   private Procesos procesoLovSeleccionado;
   //
   private short auxClonadoCodigo;
   //
   private String infoRegistroProceso, infoRegistroFormulaP, infoRegistroOperandoL;
   private String infoRegistroLovFormula, infoRegistroLovTipoPago, infoRegistroLovProceso, infoRegistroLovOperando;
   private String altoTablaProceso;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private int tablaActiva;
   String errorClonado;

   public ControlProceso() {
      //clonado
      procesoNuevoClonado = new Procesos();
      procesoBaseClonado = new Procesos();
      lovProcesos = null;
      //
      paginaAnterior = "nominaf";
      //altos tablas
      altoTablaProcesos = "110";
      altoTablaForProYOperLogs = "95";
      //Permitir index
      permitirIndexFormulasProcesos = true;
      permitirIndex = true;
      permitirIndexOperandosLogs = true;
      //lovs
      formulaProcesoSeleccionada = null;
      procesoLovSeleccionado = null;
      operandoLovSeleccionado = null;
      tipoPagoLovSeleccionado = null;
      formulaLovSeleccionada = null;
      lovTiposPagos = null;
      lovFormulas = null;
      lovOperandos = null;
      //listas de tablas
      listaProcesos = null;
      listaFormulasProcesos = null;
      listaOperandosLogs = null;
      //Otros
      aceptar = true;
      cambiosPagina = true;
      tipoActualizacion = -1;
      k = 0;
      //borrar 
      listProcesosBorrar = new ArrayList<Procesos>();
      listFormulasProcesosBorrar = new ArrayList<FormulasProcesos>();
      listOperandosLogsBorrar = new ArrayList<OperandosLogs>();
      //crear 
      listProcesosCrear = new ArrayList<Procesos>();
      listFormulasProcesosCrear = new ArrayList<FormulasProcesos>();
      listOperandosLogsCrear = new ArrayList<OperandosLogs>();
      //modificar 
      listFormulasProcesosModificar = new ArrayList<FormulasProcesos>();
      listProcesosModificar = new ArrayList<Procesos>();
      listOperandosLogsModificar = new ArrayList<OperandosLogs>();
      //editar
      editarProceso = new Procesos();
      editarFormulaProceso = new FormulasProcesos();
      editarOperandoLog = new OperandosLogs();
      //Cual Celda
      cualCelda = -1;
      cualCeldaFormulaProceso = -1;
      cualCeldaOperandoLog = -1;
      //Tipo Lista
      tipoListaFormulasProcesos = 0;
      tipoLista = 0;
      tipoListaOperandoLog = 0;
      //guardar
      guardado = true;
      guardadoFormulasProcesos = true;
      guardadoOperandosLogs = true;
      //Crear 
      nuevoProceso = new Procesos();
      nuevoProceso.setTipopago(new Tipospagos());
      nuevoFormulaProceso = new FormulasProcesos();
      nuevoFormulaProceso.setFormula(new Formulas());
      nuevoOperandoLog = new OperandosLogs();
      nuevoOperandoLog.setOperando(new Operandos());
      //Duplicar
      duplicarProceso = new Procesos();
      duplicarFormulaProceso = new FormulasProcesos();
      duplicarOperandoLog = new OperandosLogs();
      //Sec Registro
      procesoLovSeleccionado = null;
      formulaProcesoSeleccionada = null;
      //Banderas
      bandera = 0;
      mapParametros.put("paginaAnterior", paginaAnterior);
      tablaActiva = 1;
      errorClonado = "";
   }

   public void limpiarListasValor() {
      lovFormulas = null;
      lovOperandos = null;
      lovProcesos = null;
      lovTiposPagos = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarProcesos.obtenerConexion(ses.getId());
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
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);

      } else {
         limpiarListasValor();
         */
String pagActual = "proceso";
         
         
         


         
         
         
         
         
         
         if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);
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

   public void inicializarPagina(String paginaLlamado) {
      paginaAnterior = paginaLlamado;
      listaFormulasProcesos = null;
      listaProcesos = null;
      getListaProcesos();
      if (listaProcesos != null) {
         if (listaProcesos.size() >= 1) {
            procesoSeleccionado = listaProcesos.get(0);
            getListaFormulasProcesos();
            getListaOperandosLogs();
         }
      }
   }

   public boolean validarCamposNulosProceso(int n) {
      boolean retorno = true;
      if (n == 0) {
         if (procesoSeleccionado.getCodigo() <= 0 || procesoSeleccionado.getTipopago().getSecuencia() == null) {
            retorno = false;
         }
         if (procesoSeleccionado.getDescripcion() == null) {
            retorno = false;
         } else if (procesoSeleccionado.getDescripcion().isEmpty()) {
            retorno = false;
         }
      }
      if (n == 1) {
         if (nuevoProceso.getCodigo() <= 0 || nuevoProceso.getTipopago().getSecuencia() == null) {
            retorno = false;
         }
         if (nuevoProceso.getDescripcion() == null) {
            retorno = false;
         } else if (nuevoProceso.getDescripcion().isEmpty()) {
            retorno = false;
         }
      }
      if (n == 2) {
         if (duplicarProceso.getCodigo() <= 0 || duplicarProceso.getTipopago().getSecuencia() == null) {
            retorno = false;
         }
         if (duplicarProceso.getDescripcion() == null) {
            retorno = false;
         } else if (duplicarProceso.getDescripcion().isEmpty()) {
            retorno = false;
         }
      }
      return retorno;
   }

   public boolean validarCamposNulosFormulaProceso(int n) {
      boolean retorno = true;
      if (n == 1) {
         if (nuevoFormulaProceso.getFormula().getSecuencia() == null) {
            retorno = false;
         }
      }
      if (n == 2) {
         if (duplicarFormulaProceso.getFormula().getSecuencia() == null) {
            retorno = false;
         }
      }
      return retorno;
   }

   public boolean validarCamposNulosOperandoLog(int n) {
      boolean retorno = true;
      if (n == 1) {
         if (nuevoOperandoLog.getOperando().getSecuencia() == null) {
            retorno = false;
         }
      }
      if (n == 2) {
         if (duplicarOperandoLog.getOperando().getSecuencia() == null) {
            retorno = false;
         }
      }
      return retorno;
   }

   public void procesoModificacionProceso(Procesos proceso) {
      procesoSeleccionado = proceso;
      boolean respuesta = validarCamposNulosProceso(0);
      if (respuesta == true) {
         modificarProceso(procesoSeleccionado);
      } else {
         procesoSeleccionado.setCodigo(auxCodigoProceso);
         procesoSeleccionado.setDescripcion(auxDescripcionProceso);
         RequestContext.getCurrentInstance().update("form:datosProceso");
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullProceso').show()");
      }
   }

   public void modificarProceso(Procesos proceso) {
      procesoSeleccionado = proceso;
      int tamDes = 0;
      tamDes = procesoSeleccionado.getDescripcion().length();
      if (tamDes >= 1 && tamDes <= 30) {
         String textM = procesoSeleccionado.getDescripcion().toUpperCase();
         procesoSeleccionado.setDescripcion(textM);
         String textC = procesoSeleccionado.getComentarios().toUpperCase();
         procesoSeleccionado.setComentarios(textC);
         if (!listProcesosCrear.contains(procesoSeleccionado)) {
            if (listProcesosModificar.isEmpty()) {
               listProcesosModificar.add(procesoSeleccionado);
            } else if (!listProcesosModificar.contains(procesoSeleccionado)) {
               listProcesosModificar.add(procesoSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
            }
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosProceso");
      } else {
         procesoSeleccionado.setDescripcion(auxDescripcionProceso);
         RequestContext.getCurrentInstance().update("form:datosProceso");
         RequestContext.getCurrentInstance().execute("PF('errorDescripcionProceso').show()");
      }
   }

   public void modificarProceso(Procesos proceso, String confirmarCambio, String valorConfirmar) {
      procesoSeleccionado = proceso;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("TIPOPAGO")) {
         procesoSeleccionado.getTipopago().setDescripcion(tipoPago);
         for (int i = 0; i < lovTiposPagos.size(); i++) {
            if (lovTiposPagos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            procesoSeleccionado.setTipopago(lovTiposPagos.get(indiceUnicoElemento));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:TipoPagoDialogo");
            contarRegistrosLovTipoPago();
            RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         String textM = procesoSeleccionado.getDescripcion().toUpperCase();
         procesoSeleccionado.setDescripcion(textM);
         if (!listProcesosCrear.contains(procesoSeleccionado)) {
            if (listProcesosModificar.isEmpty()) {
               listProcesosModificar.add(procesoSeleccionado);
            } else if (!listProcesosModificar.contains(procesoSeleccionado)) {
               listProcesosModificar.add(procesoSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosProceso");
   }

   public void modificarFormulaProceso(FormulasProcesos formulaproceso) {
      formulaProcesoSeleccionada = formulaproceso;
      if (!listFormulasProcesosCrear.contains(formulaProcesoSeleccionada)) {
         if (listFormulasProcesosModificar.isEmpty()) {
            listFormulasProcesosModificar.add(formulaProcesoSeleccionada);
         } else if (!listFormulasProcesosModificar.contains(formulaProcesoSeleccionada)) {
            listFormulasProcesosModificar.add(formulaProcesoSeleccionada);
         }
         if (guardadoFormulasProcesos == true) {
            guardadoFormulasProcesos = false;
         }
      }
      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
   }

   public void modificarFormulaProceso(FormulasProcesos formulaProceso, String confirmarCambio, String valorConfirmar) {
      formulaProcesoSeleccionada = formulaProceso;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         formulaProcesoSeleccionada.getFormula().setNombrelargo(formula);
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            formulaProcesoSeleccionada.setFormula(lovFormulas.get(indiceUnicoElemento));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexFormulasProcesos = false;
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            contarRegistrosLovFormula();
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
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
            if (guardadoFormulasProcesos == true) {
               guardadoFormulasProcesos = false;
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
   }

   public void modificarOperandoLog(OperandosLogs operandoLog) {
      operandoLogSeleccionado = operandoLog;
      if (!listOperandosLogsCrear.contains(operandoLogSeleccionado)) {
         if (listOperandosLogsModificar.isEmpty()) {
            listOperandosLogsModificar.add(operandoLogSeleccionado);
         } else if (!listOperandosLogsModificar.contains(operandoLogSeleccionado)) {
            listOperandosLogsModificar.add(operandoLogSeleccionado);
         }
         if (guardadoOperandosLogs == true) {
            guardadoOperandosLogs = false;
         }
      }
      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosOperando");
   }

   public void modificarOperandoLog(OperandosLogs operandoLog, String confirmarCambio, String valorConfirmar) {
      operandoLogSeleccionado = operandoLog;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
         operandoLogSeleccionado.getOperando().setNombre(operando);
         for (int i = 0; i < lovOperandos.size(); i++) {
            if (lovOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            operandoLogSeleccionado.setOperando(lovOperandos.get(indiceUnicoElemento));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndexOperandosLogs = false;
            RequestContext.getCurrentInstance().update("form:OperandoDialogo");
            contarRegistrosLovOperando();
            RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listOperandosLogsCrear.contains(operandoLogSeleccionado)) {
            if (listOperandosLogsModificar.isEmpty()) {
               listOperandosLogsModificar.add(operandoLogSeleccionado);
            } else if (!listOperandosLogsModificar.contains(operandoLogSeleccionado)) {
               listOperandosLogsModificar.add(operandoLogSeleccionado);
            }
            if (guardadoOperandosLogs == true) {
               guardadoOperandosLogs = false;
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosOperando");
   }

   public void posicionOperando() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      operandoLogSeleccionado = listaOperandosLogs.get(indice);
      cambiarIndiceOperandoLog(operandoLogSeleccionado, columna);
   }

   public void cambiarIndice(Procesos proceso, int celda) {
      tablaActiva = 1;
      procesoSeleccionado = proceso;
      if (guardadoFormulasProcesos == true && guardadoOperandosLogs == true) {
         if (permitirIndex == true) {
            cualCelda = celda;
            formulaProcesoSeleccionada = null;
            operandoLogSeleccionado = null;
            auxCodigoProceso = procesoSeleccionado.getCodigo();
            auxDescripcionProceso = procesoSeleccionado.getDescripcion();
            listaFormulasProcesos = null;
            getListaFormulasProcesos();
            RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
            listaOperandosLogs = null;
            getListaOperandosLogs();
            RequestContext.getCurrentInstance().update("form:datosOperando");
            contarRegistrosFP();
            contarRegistrosOL();
            if (bandera == 1) {
               restaurarFiltroTablas();
            }
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void cambiarIndiceFormulaProceso(FormulasProcesos formulaProceso, int celda) {
      tablaActiva = 2;
      formulaProcesoSeleccionada = formulaProceso;
      if (permitirIndexFormulasProcesos == true) {
         operandoLogSeleccionado = null;
         cualCeldaFormulaProceso = celda;
         formula = formulaProcesoSeleccionada.getFormula().getNombrelargo();
         RequestContext.getCurrentInstance().update("form:datosOperando");
      }
   }

   public void cambiarIndiceOperandoLog(OperandosLogs operandoL, int celda) {
      tablaActiva = 3;
      operandoLogSeleccionado = operandoL;
      if (permitirIndexOperandosLogs == true) {
         formulaProcesoSeleccionada = null;
         cualCeldaOperandoLog = celda;
         operando = operandoLogSeleccionado.getOperando().getNombre();
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
      }
   }

   //GUARDAR
   public void guardarYSalir() {
      guardarGeneral();
      salir();
   }

   public void cancelarYSalir() {
      cancelarModificacionGeneral();
      salir();
   }

   public void guardarGeneral() {
      if (guardado == false || guardadoFormulasProcesos == false || guardadoOperandosLogs == false) {
         if (guardado == false) {
            guardarCambiosProceso();
         }
         if (guardadoFormulasProcesos == false) {
            guardarCambiosFormulaProceso();
         }
         if (guardadoOperandosLogs == false) {
            guardarCambiosOperandoLog();
         }

      }
   }

   public void guardarCambiosProceso() {
      try {
         if (!listProcesosBorrar.isEmpty()) {
            for (int i = 0; i < listProcesosBorrar.size(); i++) {
               administrarProcesos.borrarProcesos(listProcesosBorrar);
            }
            listProcesosBorrar.clear();
         }
         if (!listProcesosCrear.isEmpty()) {
            for (int i = 0; i < listProcesosCrear.size(); i++) {
               administrarProcesos.crearProcesos(listProcesosCrear);
            }
            listProcesosCrear.clear();
         }
         if (!listProcesosModificar.isEmpty()) {
            administrarProcesos.editarProcesos(listProcesosModificar);
            listProcesosModificar.clear();
         }
         listaProcesos = null;
         listaOperandosLogs = null;
         listaFormulasProcesos = null;
         procesoSeleccionado = null;
         procesoLovSeleccionado = null;
         guardado = true;
         k = 0;
         tablaActiva = 0;

         RequestContext.getCurrentInstance().update("form:datosProceso");
         RequestContext.getCurrentInstance().update("form:datosOperando");
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
         contarRegistros();
         contarRegistrosFP();
         contarRegistrosOL();
         FacesMessage msg = new FacesMessage("Información", "Los datos de Procesos se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosProceso : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Procesos, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosFormulaProceso() {
      try {
         if (!listFormulasProcesosBorrar.isEmpty()) {
            administrarProcesos.borrarFormulasProcesos(listFormulasProcesosBorrar);
            listFormulasProcesosBorrar.clear();
         }
         if (!listFormulasProcesosCrear.isEmpty()) {
            administrarProcesos.crearFormulasProcesos(listFormulasProcesosCrear);
            listFormulasProcesosCrear.clear();
         }
         if (!listFormulasProcesosModificar.isEmpty()) {
            administrarProcesos.editarFormulasProcesos(listFormulasProcesosModificar);
            listFormulasProcesosModificar.clear();
         }
         listaFormulasProcesos = null;
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
         contarRegistrosFP();
         guardadoFormulasProcesos = true;
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Formulas se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosFormulaProceso : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Formulas, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosOperandoLog() {
      try {
         if (!listOperandosLogsBorrar.isEmpty()) {
            administrarProcesos.borrarOperandosLogs(listOperandosLogsBorrar);
            listOperandosLogsBorrar.clear();
         }
         if (!listOperandosLogsCrear.isEmpty()) {
            administrarProcesos.crearOperandosLogs(listOperandosLogsCrear);
            listOperandosLogsCrear.clear();
         }
         if (!listOperandosLogsModificar.isEmpty()) {
            administrarProcesos.editarOperandosLogs(listOperandosLogsModificar);
            listOperandosLogsModificar.clear();
         }
         listaOperandosLogs = null;
         RequestContext.getCurrentInstance().update("form:datosOperando");
         contarRegistrosOL();
         guardadoOperandosLogs = true;
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Operandos se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosFormulaProceso : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Operandos, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //CANCELAR MODIFICACIONES
   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacionGeneral() {
      cancelarModificacionProceso();
      cancelarModificacionFormulaProceso();
      cancelarModificacionOperandoLog();
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      procesoBaseClonado = new Procesos();
      procesoNuevoClonado = new Procesos();
      tablaActiva = 0;
      RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
      RequestContext.getCurrentInstance().update("form:CodigoNuevoClonado");
      RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
      RequestContext.getCurrentInstance().update("form:DescripcionNuevoClonado");
   }

   public void cancelarModificacionProceso() {
      if (bandera == 1) {
         restaurarFiltroTablas();
      }
      listProcesosBorrar.clear();
      listProcesosCrear.clear();
      listProcesosModificar.clear();
      k = 0;
      listaProcesos = null;
      listaOperandosLogs = null;
      listaFormulasProcesos = null;
      procesoSeleccionado = null;
      procesoLovSeleccionado = null;

      RequestContext.getCurrentInstance().update("form:datosProceso");
      RequestContext.getCurrentInstance().update("form:datosOperando");
      RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
      contarRegistros();
      contarRegistrosFP();
      contarRegistrosOL();
      guardado = true;
   }

   public void cancelarModificacionFormulaProceso() {
      if (bandera == 1) {
         restaurarFiltroTablas();
      }
      listFormulasProcesosBorrar.clear();
      listFormulasProcesosCrear.clear();
      listFormulasProcesosModificar.clear();
      formulaProcesoSeleccionada = null;
      k = 0;
      listaFormulasProcesos = null;
      getListaFormulasProcesos();
      guardadoFormulasProcesos = true;
      permitirIndexFormulasProcesos = true;
      RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
      contarRegistrosFP();
   }

   public void cancelarModificacionOperandoLog() {
      if (bandera == 1) {
         restaurarFiltroTablas();
      }
      listOperandosLogsBorrar.clear();
      listOperandosLogsCrear.clear();
      listOperandosLogsModificar.clear();
      operandoLogSeleccionado = null;
      k = 0;
      listaOperandosLogs = null;
      getListaOperandosLogs();
      guardadoOperandosLogs = true;
      permitirIndexOperandosLogs = true;
      RequestContext.getCurrentInstance().update("form:datosOperando");
      contarRegistrosOL();
   }

   public void editarCelda() {
      System.out.println("Controlador.ControlProceso.editarCelda() tablaActiva: " + tablaActiva);
      if (tablaActiva == 2) {
         editarFormulaProceso = formulaProcesoSeleccionada;
         if (cualCeldaFormulaProceso == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaFormulaProceso");
            RequestContext.getCurrentInstance().execute("PF('editarFormulaFormulaProceso').show()");
            cualCeldaFormulaProceso = -1;
         }
      } else if (tablaActiva == 3) {
         editarOperandoLog = operandoLogSeleccionado;
         if (cualCeldaOperandoLog == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarOperandoOperandoLogD");
            RequestContext.getCurrentInstance().execute("PF('editarOperandoOperandoLogD').show()");
            cualCeldaOperandoLog = -1;
         } else if (cualCeldaOperandoLog == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionOperandoLogD");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionOperandoLogD').show()");
            cualCeldaOperandoLog = -1;
         }
      } else if (tablaActiva == 1) {
         editarProceso = procesoSeleccionado;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoProcesoD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoProcesoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionProcesoD");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionProcesoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoPagoProcesoD");
            RequestContext.getCurrentInstance().execute("PF('editarTipoPagoProcesoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarComentarioProcesoD");
            RequestContext.getCurrentInstance().execute("PF('editarComentarioProcesoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNumeroProcesoD");
            RequestContext.getCurrentInstance().execute("PF('editarNumeroProcesoD').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void dialogoNuevoRegistro(int n) {
      if (guardado == false || guardadoFormulasProcesos == false || guardadoOperandosLogs == false) {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      } else if (procesoSeleccionado != null) {
         if (n == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroProceso");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroProceso').show()");
         } else if (n == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroFormulaProceso");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormulaProceso').show()");
         } else if (n == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroOperandoLog");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroOperandoLog').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorSeleccionProceso').show()");
      }
   }

   //CREAR 
   public void agregarNuevoProceso() {
      boolean respueta = validarCamposNulosProceso(1);
      if (respueta == true) {
         int tamDes;
         tamDes = nuevoProceso.getDescripcion().length();
         if (tamDes >= 1 && tamDes <= 30) {
            if (bandera == 1) {
               restaurarFiltroTablas();
            }

            k++;
            l = BigInteger.valueOf(k);
            String text = nuevoProceso.getDescripcion().toUpperCase();
            nuevoProceso.setDescripcion(text);
            nuevoProceso.setSecuencia(l);
            listProcesosCrear.add(nuevoProceso);
            listaProcesos.add(nuevoProceso);
            procesoSeleccionado = listaProcesos.get(listaProcesos.indexOf(nuevoProceso));
            nuevoProceso = new Procesos();
            nuevoProceso.setTipopago(new Tipospagos());
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosProceso");
            contarRegistros();
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroProceso').hide()");
            if (guardado == true) {
               guardado = false;
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorDescripcionProceso').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullProceso').show()");
      }
   }

   public void agregarNuevoFormulaProceso() {
      boolean respueta = validarCamposNulosFormulaProceso(1);
      if (respueta == true) {
         if (bandera == 1) {
            restaurarFiltroTablas();
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoFormulaProceso.setSecuencia(l);
         nuevoFormulaProceso.setProceso(procesoSeleccionado);
         if (listaFormulasProcesos.size() == 0) {
            listaFormulasProcesos = new ArrayList<FormulasProcesos>();
         }
         listFormulasProcesosCrear.add(nuevoFormulaProceso);
         listaFormulasProcesos.add(nuevoFormulaProceso);
         formulaProcesoSeleccionada = listaFormulasProcesos.get(listaFormulasProcesos.indexOf(nuevoFormulaProceso));
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
         contarRegistrosFP();
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroFormulaProceso').hide()");
         nuevoFormulaProceso = new FormulasProcesos();
         nuevoFormulaProceso.setFormula(new Formulas());
         if (guardadoFormulasProcesos == true) {
            guardadoFormulasProcesos = false;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullFormulaProceso').show()");
      }
   }

   public void agregarNuevoOperandoLog() {
      if (nuevoOperandoLog.getOperando().getSecuencia() != null) {
         if (bandera == 1) {
            restaurarFiltroTablas();
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoOperandoLog.setSecuencia(l);
         nuevoOperandoLog.setProceso(procesoSeleccionado);
         if (listaOperandosLogs.size() == 0) {
            listaOperandosLogs = new ArrayList<OperandosLogs>();
         }
         listOperandosLogsCrear.add(nuevoOperandoLog);
         listaOperandosLogs.add(nuevoOperandoLog);
         operandoLogSeleccionado = listaOperandosLogs.get(listaOperandosLogs.indexOf(nuevoOperandoLog));
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         context.update("form:ACEPTAR");
         context.update("form:datosOperando");
         contarRegistrosOL();
         context.execute("PF('NuevoRegistroOperandoLog').hide()");
         nuevoOperandoLog = new OperandosLogs();
         nuevoOperandoLog.setOperando(new Operandos());
         if (guardadoOperandosLogs == true) {
            guardadoOperandosLogs = false;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullOperando').show()");
      }
   }

   //LIMPIAR NUEVO REGISTRO
   /**
    */
   public void limpiarNuevaProceso() {
      nuevoProceso = new Procesos();
      nuevoProceso.setTipopago(new Tipospagos());
   }

   public void limpiarNuevaFormulaProceso() {
      nuevoFormulaProceso = new FormulasProcesos();
      nuevoFormulaProceso.setFormula(new Formulas());
   }

   public void limpiarNuevaOperandoLog() {
      nuevoOperandoLog = new OperandosLogs();
      nuevoOperandoLog.setOperando(new Operandos());
   }

   //DUPLICAR VC
   /**
    */
   public void verificarRegistroDuplicar() {
      if (tablaActiva == 1) {
         duplicarProcesoM();
      } else if (tablaActiva == 2) {
         duplicarFormulaProcesoM();
      } else if (tablaActiva == 3) {
         duplicarOperandoLogM();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarProcesoM() {
      duplicarProceso = new Procesos();
      duplicarProceso.setCodigo(procesoSeleccionado.getCodigo());
      duplicarProceso.setDescripcion(procesoSeleccionado.getDescripcion());
      duplicarProceso.setTipopago(procesoSeleccionado.getTipopago());
      duplicarProceso.setContabilizacion(procesoSeleccionado.getContabilizacion());
      duplicarProceso.setEliminarliqadelanto(procesoSeleccionado.getEliminarliqadelanto());
      duplicarProceso.setEliminarliqsolucionnodo(procesoSeleccionado.getEliminarliqsolucionnodo());
      duplicarProceso.setControlsobregiro(procesoSeleccionado.getControlsobregiro());
      duplicarProceso.setAutomatico(procesoSeleccionado.getAutomatico());
      duplicarProceso.setComentarios(procesoSeleccionado.getComentarios());
      duplicarProceso.setNumerocierrerequerido(procesoSeleccionado.getNumerocierrerequerido());
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroProceso");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroProceso').show()");

   }

   public void duplicarFormulaProcesoM() {
      duplicarFormulaProceso = new FormulasProcesos();
      duplicarFormulaProceso.setFormula(formulaProcesoSeleccionada.getFormula());
      duplicarFormulaProceso.setPeriodicidadindependiente(formulaProcesoSeleccionada.getPeriodicidadindependiente());
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroFormulaProceso");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroFormulaProceso').show()");

   }

   public void duplicarOperandoLogM() {
      duplicarOperandoLog = new OperandosLogs();
      duplicarOperandoLog.setDescripcion(operandoLogSeleccionado.getDescripcion());
      duplicarOperandoLog.setProceso(operandoLogSeleccionado.getProceso());
      duplicarOperandoLog.setOperando(operandoLogSeleccionado.getOperando());
      RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroOperandoLog");
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroOperandoLog').show()");

   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
    */
   public void confirmarDuplicarProceso() {
      boolean respueta = validarCamposNulosProceso(2);
      if (respueta == true) {
         if (nuevoProceso.getDescripcion().length() >= 1 && nuevoProceso.getDescripcion().length() <= 30) {
            if (bandera == 1) {
               restaurarFiltroTablas();
            }
            k++;
            l = BigInteger.valueOf(k);
            duplicarProceso.setSecuencia(l);
            if (duplicarProceso.getDescripcion() != null) {
               String text = duplicarProceso.getDescripcion().toUpperCase();
               duplicarProceso.setDescripcion(text);
            }
            listaProcesos.add(duplicarProceso);
            listProcesosCrear.add(duplicarProceso);
            procesoSeleccionado = listaProcesos.get(listaProcesos.indexOf(duplicarProceso));
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:ACEPTAR");
            context.update("form:datosProceso");
            contarRegistros();
            context.execute("PF('DuplicarRegistroProceso').hide()");
            if (guardado == true) {
               guardado = false;
            }
            duplicarProceso = new Procesos();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorDescripcionProceso').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullProceso').show()");
      }
   }

   public void confirmarDuplicarFormulaProceso() {
      boolean respueta = validarCamposNulosFormulaProceso(2);
      if (respueta == true) {
         if (bandera == 1) {
            restaurarFiltroTablas();
         }
         k++;
         l = BigInteger.valueOf(k);
         duplicarFormulaProceso.setSecuencia(l);
         duplicarFormulaProceso.setProceso(procesoSeleccionado);
         listaFormulasProcesos.add(duplicarFormulaProceso);
         listFormulasProcesosCrear.add(duplicarFormulaProceso);
         formulaProcesoSeleccionada = listaFormulasProcesos.get(listaFormulasProcesos.indexOf(duplicarFormulaProceso));
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         context.update("form:ACEPTAR");
         context.update("form:datosFormulaProceso");
         contarRegistrosFP();
         context.execute("PF('DuplicarRegistroFormulaProceso').hide()");
         if (guardadoFormulasProcesos == true) {
            guardadoFormulasProcesos = false;
         }
         duplicarFormulaProceso = new FormulasProcesos();
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullFormulaProceso').show()");
      }
   }

   public void confirmarDuplicarOperandoLog() {
      if (duplicarOperandoLog.getOperando().getSecuencia() != null) {
         if (bandera == 1) {
            restaurarFiltroTablas();
         }
         k++;
         l = BigInteger.valueOf(k);
         duplicarOperandoLog.setSecuencia(l);
         duplicarOperandoLog.setProceso(procesoSeleccionado);
         listaOperandosLogs.add(duplicarOperandoLog);
         listOperandosLogsCrear.add(duplicarOperandoLog);
         operandoLogSeleccionado = listaOperandosLogs.get(listaOperandosLogs.indexOf(duplicarOperandoLog));
         cambiosPagina = false;
         RequestContext context = RequestContext.getCurrentInstance();
         context.update("form:ACEPTAR");
         context.update("form:datosOperando");
         contarRegistrosOL();
         context.execute("PF('DuplicarRegistroOperandoLog').hide()");
         if (guardadoOperandosLogs == true) {
            guardadoOperandosLogs = false;
         }
         duplicarOperandoLog = new OperandosLogs();
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullOperando').show()");
      }
   }

   //LIMPIAR DUPLICAR
   /**
    * Metodo que limpia los datos de un duplicar Set
    */
   public void limpiarDuplicarProceso() {
      duplicarProceso = new Procesos();
      duplicarProceso.setTipopago(new Tipospagos());
   }

   public void limpiarDuplicarFormulaProceso() {
      duplicarFormulaProceso = new FormulasProcesos();
      duplicarFormulaProceso.setFormula(new Formulas());
   }

   public void limpiarDuplicarOperandoLog() {
      duplicarOperandoLog = new OperandosLogs();
      duplicarOperandoLog.setOperando(new Operandos());
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
      if (tablaActiva == 2) {
         borrarFormulaProceso();
      } else if (tablaActiva == 3) {
         borrarOperandoLog();
      } else if (tablaActiva == 1) {
         if (listaFormulasProcesos.isEmpty() && listaOperandosLogs.isEmpty()) {
            borrarProceso();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void borrarProceso() {
      if (!listProcesosModificar.isEmpty() && listProcesosModificar.contains(procesoSeleccionado)) {
         listProcesosModificar.remove(procesoSeleccionado);
         listProcesosBorrar.add(procesoSeleccionado);
      } else if (!listProcesosCrear.isEmpty() && listProcesosCrear.contains(procesoSeleccionado)) {
         listProcesosCrear.remove(procesoSeleccionado);
      } else {
         listProcesosBorrar.add(procesoSeleccionado);
      }
      listaProcesos.remove(procesoSeleccionado);
      if (tipoLista == 1) {
         filtrarListaProcesos.remove(procesoSeleccionado);
      }
      procesoSeleccionado = null;
      listaFormulasProcesos = null;
      listaOperandosLogs = null;
      operandoLogSeleccionado = null;
      formulaProcesoSeleccionada = null;
      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosProceso");
      RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
      RequestContext.getCurrentInstance().update("form:datosOperando");
      contarRegistros();
      contarRegistrosFP();
      contarRegistrosOL();
      if (guardado == true) {
         guardado = false;
      }
   }

   public void borrarFormulaProceso() {
      if (!listFormulasProcesosModificar.isEmpty() && listFormulasProcesosModificar.contains(formulaProcesoSeleccionada)) {
         listFormulasProcesosModificar.remove(formulaProcesoSeleccionada);
         listFormulasProcesosBorrar.add(formulaProcesoSeleccionada);
      } else if (!listFormulasProcesosCrear.isEmpty() && listFormulasProcesosCrear.contains(formulaProcesoSeleccionada)) {
         listFormulasProcesosCrear.remove(formulaProcesoSeleccionada);
      } else {
         listFormulasProcesosBorrar.add(formulaProcesoSeleccionada);
      }
      listaFormulasProcesos.remove(formulaProcesoSeleccionada);
      if (tipoListaFormulasProcesos == 1) {
         filtrarListaFormulasProcesos.remove(formulaProcesoSeleccionada);
      }
      cambiosPagina = false;
      formulaProcesoSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
      contarRegistrosFP();

      if (guardadoFormulasProcesos == true) {
         guardadoFormulasProcesos = false;
      }
   }

   public void borrarOperandoLog() {
      if (!listOperandosLogsModificar.isEmpty() && listOperandosLogsModificar.contains(operandoLogSeleccionado)) {
         listOperandosLogsModificar.remove(operandoLogSeleccionado);
         listOperandosLogsBorrar.add(operandoLogSeleccionado);
      } else if (!listOperandosLogsCrear.isEmpty() && listOperandosLogsCrear.contains(operandoLogSeleccionado)) {
         listOperandosLogsCrear.remove(operandoLogSeleccionado);
      } else {
         listOperandosLogsBorrar.add(operandoLogSeleccionado);
      }
      listaOperandosLogs.remove(operandoLogSeleccionado);
      if (tipoListaOperandoLog == 1) {
         filtrarListaOperandosLogs.remove(operandoLogSeleccionado);
      }
      operandoLogSeleccionado = null;
      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosOperando");
      contarRegistrosOL();
      if (guardadoOperandosLogs == true) {
         guardadoOperandosLogs = false;
      }
   }
   //CTRL + F11 ACTIVAR/DESACTIVAR

   /**
    * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      if (bandera == 0) {
         altoTablaProcesos = "90";
         procesoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoCodigo");
         procesoCodigo.setFilterStyle("width: 80% !important");
         procesoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoDescripcion");
         procesoDescripcion.setFilterStyle("width: 85% !important");
         procesoTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoTipoPago");
         procesoTipoPago.setFilterStyle("width: 85% !important");
         procesoComentario = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoComentario");
         procesoComentario.setFilterStyle("width: 85% !important;");
         procesoNumero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoNumero");
         procesoNumero.setFilterStyle("width: 75% !important");
         procesoContabilizacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoContabilizacion");
         procesoContabilizacion.setFilterStyle("width: 70% !important");
         procesoSolucionNodo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSolucionNodo");
         procesoSolucionNodo.setFilterStyle("width: 70% !important");
         procesoAdelanto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAdelanto");
         procesoAdelanto.setFilterStyle("width: 70% !important");
         procesoSobregiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoSobregiro");
         procesoSobregiro.setFilterStyle("width: 70% !important");
         procesoAutomatico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosProceso:procesoAutomatico");
         procesoAutomatico.setFilterStyle("width: 70% !important");

         altoTablaForProYOperLogs = "75";
         formulaProcesoFormula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoFormula");
         formulaProcesoFormula.setFilterStyle("width: 85% !important;");
         formulaProcesoPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoPeriodicidad");
         formulaProcesoPeriodicidad.setFilterStyle("width: 70% !important;");
         bandera = 1;

         operandoOperando = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoOperando");
         operandoOperando.setFilterStyle("width: 85% !important;");
         operandoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosOperando:operandoDescripcion");
         operandoDescripcion.setFilterStyle("width: 85% !important;");

         RequestContext.getCurrentInstance().update("form:datosProceso");
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
         RequestContext.getCurrentInstance().update("form:datosOperando");
      } else if (bandera == 1) {
         restaurarFiltroTablas();
      }
   }

   public void restaurarFiltroTablas() {
      FacesContext fc = FacesContext.getCurrentInstance();
      altoTablaProcesos = "110";
      procesoCodigo = (Column) fc.getViewRoot().findComponent("form:datosProceso:procesoCodigo");
      procesoCodigo.setFilterStyle("display: none; visibility: hidden;");
      procesoDescripcion = (Column) fc.getViewRoot().findComponent("form:datosProceso:procesoDescripcion");
      procesoDescripcion.setFilterStyle("display: none; visibility: hidden;");
      procesoTipoPago = (Column) fc.getViewRoot().findComponent("form:datosProceso:procesoTipoPago");
      procesoTipoPago.setFilterStyle("display: none; visibility: hidden;");
      procesoComentario = (Column) fc.getViewRoot().findComponent("form:datosProceso:procesoComentario");
      procesoComentario.setFilterStyle("display: none; visibility: hidden;");
      procesoNumero = (Column) fc.getViewRoot().findComponent("form:datosProceso:procesoNumero");
      procesoNumero.setFilterStyle("display: none; visibility: hidden;");
      procesoContabilizacion = (Column) fc.getViewRoot().findComponent("form:datosProceso:procesoContabilizacion");
      procesoContabilizacion.setFilterStyle("display: none; visibility: hidden;");
      procesoSolucionNodo = (Column) fc.getViewRoot().findComponent("form:datosProceso:procesoSolucionNodo");
      procesoSolucionNodo.setFilterStyle("display: none; visibility: hidden;");
      procesoAdelanto = (Column) fc.getViewRoot().findComponent("form:datosProceso:procesoAdelanto");
      procesoAdelanto.setFilterStyle("display: none; visibility: hidden;");
      procesoSobregiro = (Column) fc.getViewRoot().findComponent("form:datosProceso:procesoSobregiro");
      procesoSobregiro.setFilterStyle("display: none; visibility: hidden;");
      procesoAutomatico = (Column) fc.getViewRoot().findComponent("form:datosProceso:procesoAutomatico");
      procesoAutomatico.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosProceso");
      bandera = 0;
      filtrarListaProcesos = null;
      tipoLista = 0;

      altoTablaForProYOperLogs = "95";
      formulaProcesoPeriodicidad = (Column) fc.getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoPeriodicidad");
      formulaProcesoPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
      formulaProcesoFormula = (Column) fc.getViewRoot().findComponent("form:datosFormulaProceso:formulaProcesoFormula");
      formulaProcesoFormula.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
      filtrarListaFormulasProcesos = null;
      tipoListaFormulasProcesos = 0;

      operandoOperando = (Column) fc.getViewRoot().findComponent("form:datosOperando:operandoOperando");
      operandoOperando.setFilterStyle("display: none; visibility: hidden;");
      operandoDescripcion = (Column) fc.getViewRoot().findComponent("form:datosOperando:operandoDescripcion");
      operandoDescripcion.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosOperando");
      filtrarListaOperandosLogs = null;
      tipoListaOperandoLog = 0;
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         restaurarFiltroTablas();
      }
      listProcesosBorrar.clear();
      listProcesosCrear.clear();
      listProcesosModificar.clear();
      listFormulasProcesosBorrar.clear();
      listFormulasProcesosCrear.clear();
      listFormulasProcesosModificar.clear();
      listOperandosLogsBorrar.clear();
      listOperandosLogsCrear.clear();
      listOperandosLogsModificar.clear();
      procesoLovSeleccionado = null;
      formulaProcesoSeleccionada = null;
      operandoLogSeleccionado = null;
      k = 0;
      listaProcesos = null;
      listaFormulasProcesos = null;
      listaOperandosLogs = null;
      guardado = true;
      guardadoFormulasProcesos = true;
      guardadoOperandosLogs = true;
      cambiosPagina = true;
      procesoBaseClonado = new Procesos();
      procesoNuevoClonado = new Procesos();
      limpiarListasValor();
      navegar("atras");
   }

   public void listaValoresBoton() {
      if (tablaActiva == 1) {
         if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("form:TipoPagoDialogo");
            contarRegistrosLovTipoPago();
            RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (tablaActiva == 2) {
         if (cualCeldaFormulaProceso == 0) {
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            contarRegistrosLovFormula();
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (tablaActiva == 3) {
         if (cualCeldaOperandoLog == 0) {
            RequestContext.getCurrentInstance().update("form:OperandoDialogo");
            contarRegistrosLovOperando();
            RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').show()");
            tipoActualizacion = 0;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void asignarIndex(Procesos proceso, int campo) {
      procesoSeleccionado = proceso;
      tipoActualizacion = 0;
      tablaActiva = 1;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("form:TipoPagoDialogo");
         contarRegistrosLovTipoPago();
         RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').show()");
      }
   }

   public void asignarIndexFP(FormulasProcesos formulaP, int campo) {
      formulaProcesoSeleccionada = formulaP;
      tablaActiva = 2;
      tipoActualizacion = 0;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("form:FormulaDialogo");
         contarRegistrosLovFormula();
         RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
      }
   }

   public void asignarIndexOL(OperandosLogs operandoLog, int campo) {
      operandoLogSeleccionado = operandoLog;
      tipoActualizacion = 0;
      tablaActiva = 3;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("form:OperandoDialogo");
         contarRegistrosLovOperando();
         RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').show()");
      }
   }

   public void asignarIndexNuevos(int campo, int tipoAct) {
      tipoActualizacion = tipoAct;
      tablaActiva = 1;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("form:TipoPagoDialogo");
         contarRegistrosLovTipoPago();
         RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').show()");
      }
   }

   public void asignarIndexFPNuevos(int campo, int tipoAct) {
      tablaActiva = 2;
      tipoActualizacion = tipoAct;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("form:FormulaDialogo");
         contarRegistrosLovFormula();
         RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
      }
   }

   public void asignarIndexOLNuevos(int campo, int tipoAct) {
      tipoActualizacion = tipoAct;
      tablaActiva = 3;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("form:OperandoDialogo");
         contarRegistrosLovOperando();
         RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').show()");
      }
   }

   public void valoresBackupAutocompletarGeneral(int tipoNuevo, String Campo) {
      if (Campo.equals("FORMULA")) {
         if (tipoNuevo == 1) {
            formula = nuevoFormulaProceso.getFormula().getNombrelargo();
         } else if (tipoNuevo == 2) {
            formula = duplicarFormulaProceso.getFormula().getNombrelargo();
         }
      }
      if (Campo.equals("TIPOPAGO")) {
         if (tipoNuevo == 1) {
            tipoPago = nuevoProceso.getTipopago().getDescripcion();
         } else if (tipoNuevo == 2) {
            tipoPago = duplicarProceso.getTipopago().getDescripcion();
         }
      }
      if (Campo.equals("OPERANDO")) {
         if (tipoNuevo == 1) {
            operando = nuevoOperandoLog.getOperando().getNombre();
         } else if (tipoNuevo == 2) {
            operando = duplicarOperandoLog.getOperando().getNombre();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoProceso(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("TIPOPAGO")) {
         if (tipoNuevo == 1) {
            nuevoProceso.getTipopago().setDescripcion(tipoPago);
         } else if (tipoNuevo == 2) {
            duplicarProceso.getTipopago().setDescripcion(tipoPago);
         }
         for (int i = 0; i < lovTiposPagos.size(); i++) {
            if (lovTiposPagos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoProceso.setTipopago(lovTiposPagos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProcesoTipoPago");
            } else if (tipoNuevo == 2) {
               duplicarProceso.setTipopago(lovTiposPagos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProcesoTipoPago");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:TipoPagoDialogo");
            contarRegistrosLovTipoPago();
            RequestContext.getCurrentInstance().execute("PF('TipoPagoDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProcesoTipoPago");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProcesoTipoPago");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoFormulaProceso(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         if (tipoNuevo == 1) {
            nuevoFormulaProceso.getFormula().setNombrelargo(tipoPago);
         } else if (tipoNuevo == 2) {
            duplicarFormulaProceso.getFormula().setNombrelargo(tipoPago);
         }
         for (int i = 0; i < lovFormulas.size(); i++) {
            if (lovFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoFormulaProceso.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaProcesoFormula");
            } else if (tipoNuevo == 2) {
               duplicarFormulaProceso.setFormula(lovFormulas.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaProcesoFormula");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            contarRegistrosLovFormula();
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaProcesoFormula");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaProcesoFormula");
            }
         }
      }
   }

   public void autocompletarNuevoyDuplicadoOperandoLog(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
         if (tipoNuevo == 1) {
            nuevoOperandoLog.getOperando().setNombre(operando);
         } else if (tipoNuevo == 2) {
            duplicarOperandoLog.getOperando().setNombre(operando);
         }
         for (int i = 0; i < lovOperandos.size(); i++) {
            if (lovOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoOperandoLog.setOperando(lovOperandos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogOperando");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogDescripcion");
            } else if (tipoNuevo == 2) {
               duplicarOperandoLog.setOperando(lovOperandos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogOperando");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogOperando");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:FormulaDialogo");
            contarRegistrosLovFormula();
            RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogOperando");
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogDescripcion");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogOperando");
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogOperando");
            }
         }
      }
   }

   public void actualizarFormula() {
      if (tipoActualizacion == 0) {
         formulaProcesoSeleccionada.setFormula(formulaLovSeleccionada);
         if (!listFormulasProcesosCrear.contains(formulaProcesoSeleccionada)) {
            if (listFormulasProcesosModificar.isEmpty()) {
               listFormulasProcesosModificar.add(formulaProcesoSeleccionada);
            } else if (!listFormulasProcesosModificar.contains(formulaProcesoSeleccionada)) {
               listFormulasProcesosModificar.add(formulaProcesoSeleccionada);
            }
         }
         if (guardadoFormulasProcesos == true) {
            guardadoFormulasProcesos = false;
         }
         permitirIndexFormulasProcesos = true;
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
      } else if (tipoActualizacion == 1) {
         nuevoFormulaProceso.setFormula(formulaLovSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFormulaProcesoFormula");
      } else if (tipoActualizacion == 2) {
         duplicarFormulaProceso.setFormula(formulaLovSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormulaProcesoFormula");
      }
      filtrarLovFormulas = null;
      formulaLovSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovFormula:globalFilter");
      context.execute("PF('lovFormula').clearFilters()");
      context.update("form:FormulaDialogo");
      context.update("form:lovFormula");
      context.update("form:aceptarF");
      context.execute("PF('FormulaDialogo').hide()");
   }

   public void cancelarCambioFormula() {
      filtrarLovFormulas = null;
      formulaLovSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexFormulasProcesos = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovFormula:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormula').clearFilters()");
      RequestContext.getCurrentInstance().update("form:FormulaDialogo");
      RequestContext.getCurrentInstance().update("form:lovFormula");
      RequestContext.getCurrentInstance().update("form:aceptarF");
      RequestContext.getCurrentInstance().execute("PF('FormulaDialogo').hide()");
   }

   public void actualizarTipoPago() {
      if (tipoActualizacion == 0) {
         procesoSeleccionado.setTipopago(tipoPagoLovSeleccionado);
         if (!listProcesosCrear.contains(procesoSeleccionado)) {
            if (listProcesosModificar.isEmpty()) {
               listProcesosModificar.add(procesoSeleccionado);
            } else if (!listProcesosModificar.contains(procesoSeleccionado)) {
               listProcesosModificar.add(procesoSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosProceso");
      } else if (tipoActualizacion == 1) {
         nuevoProceso.setTipopago(tipoPagoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProcesoTipoPago");
      } else if (tipoActualizacion == 2) {
         duplicarProceso.setTipopago(tipoPagoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProcesoTipoPago");
      }
      filtrarLovTiposPagos = null;
      tipoPagoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTipoPago:globalFilter");
      context.execute("PF('lovTipoPago').clearFilters()");
      context.update("form:TipoPagoDialogo");
      context.update("form:lovTipoPago");
      context.update("form:aceptarTP");
      context.execute("PF('TipoPagoDialogo').hide()");
   }

   public void cancelarCambioTipoPago() {
      RequestContext context = RequestContext.getCurrentInstance();
      filtrarLovTiposPagos = null;
      tipoPagoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      context.reset("form:lovTipoPago:globalFilter");
      context.execute("PF('lovTipoPago').clearFilters()");
      context.update("form:TipoPagoDialogo");
      context.update("form:lovTipoPago");
      context.update("form:aceptarTP");
      context.execute("PF('TipoPagoDialogo').hide()");
   }

   public void actualizarOperando() {
      if (tipoActualizacion == 0) {
         operandoLogSeleccionado.setOperando(operandoLovSeleccionado);
         if (!listOperandosLogsCrear.contains(operandoLogSeleccionado)) {
            if (listOperandosLogsModificar.isEmpty()) {
               listOperandosLogsModificar.add(operandoLogSeleccionado);
            } else if (!listOperandosLogsModificar.contains(operandoLogSeleccionado)) {
               listOperandosLogsModificar.add(operandoLogSeleccionado);
            }
         }
         if (guardadoOperandosLogs == true) {
            guardadoOperandosLogs = false;
         }
         permitirIndexOperandosLogs = true;
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosOperando");
      } else if (tipoActualizacion == 1) {
         nuevoOperandoLog.setOperando(operandoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogOperando");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperandoLogDescripcion");
      } else if (tipoActualizacion == 2) {
         duplicarOperandoLog.setOperando(operandoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogOperando");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperandoLogDescripcion");
      }
      filtrarLovOperandos = null;
      operandoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovOperando:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOperando').clearFilters()");
      RequestContext.getCurrentInstance().update("form:OperandoDialogo");
      RequestContext.getCurrentInstance().update("form:lovOperando");
      RequestContext.getCurrentInstance().update("form:aceptarO");
      RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').hide()");
   }

   public void cancelarCambioOperando() {
      filtrarLovOperandos = null;
      operandoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndexOperandosLogs = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovOperando:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOperando').clearFilters()");
      RequestContext.getCurrentInstance().update("form:OperandoDialogo");
      RequestContext.getCurrentInstance().update("form:lovOperando");
      RequestContext.getCurrentInstance().update("form:aceptarO");
      RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').hide()");
   }

   /**
    * Metodo que activa el boton aceptar de la pantalla y dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   public String exportXML() {
      if (procesoSeleccionado != null) {
         nombreTabla = ":formExportarP:datosProcesoExportar";
         nombreXML = "Procesos_XML";
      }
      if (formulaProcesoSeleccionada != null) {
         nombreTabla = ":formExportarFP:datosFormulaProcesoExportar";
         nombreXML = "FormulasProcesos_XML";
      }
      if (operandoLogSeleccionado != null) {
         nombreTabla = ":formExportarOL:datosOperandoExportar";
         nombreXML = "OperandosLogs_XML";
      }
      return nombreTabla;
   }

   /**
    * Metodo que exporta datos a PDF
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      if (procesoSeleccionado != null) {
         exportPDF_P();
      }
      if (formulaProcesoSeleccionada != null) {
         exportPDF_FP();
      }
      if (operandoLogSeleccionado != null) {
         exportPDF_OL();
      }
   }

   public void exportPDF_P() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarP:datosProcesoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Procesos_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_FP() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarFP:datosFormulaProcesoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "FormulasProcesos_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_OL() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarOL:datosOperandoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "OperandosLogs_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportXLS() throws IOException {
      if (procesoSeleccionado != null) {
         exportXLS_P();
      }
      if (formulaProcesoSeleccionada != null) {
         exportXLS_FP();
      }
      if (operandoLogSeleccionado != null) {
         exportXLS_OL();
      }
   }

   public void exportXLS_P() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarP:datosProcesoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Procesos_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_FP() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarFP:datosFormulaProcesoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "FormulasProcesos_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_OL() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarOL:datosOperandoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "OperandosLogs_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }
   //EVENTO FILTRAR

   /**
    * Evento que cambia la lista reala a la filtrada
    */
   public void eventoFiltrar(int n) {
      if (n == 0) {
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         procesoSeleccionado = null;
         formulaProcesoSeleccionada = null;
         operandoLogSeleccionado = null;
         listaFormulasProcesos = null;
         RequestContext.getCurrentInstance().update("form:datosFormulaProceso");
         listaOperandosLogs = null;
         RequestContext.getCurrentInstance().update("form:datosOperando");
         contarRegistros();
         contarRegistrosFP();
         contarRegistrosOL();
      }
      if (n == 1) {
         if (tipoListaFormulasProcesos == 0) {
            tipoListaFormulasProcesos = 1;
         }
         formulaProcesoSeleccionada = null;
         contarRegistrosFP();
      }
      if (n == 2) {
         if (tipoListaOperandoLog == 0) {
            tipoListaOperandoLog = 1;
         }
         operandoLogSeleccionado = null;
         contarRegistrosOL();
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistroP");
   }

   public void contarRegistrosFP() {
      RequestContext.getCurrentInstance().update("form:infoRegistroFormulasP");
   }

   public void contarRegistrosOL() {
      RequestContext.getCurrentInstance().update("form:infoRegistroOperandoL");
   }

   public void contarRegistrosLovFormula() {
      RequestContext.getCurrentInstance().update("form:infoRegistroFormula");
   }

   public void contarRegistrosLovOperando() {
      RequestContext.getCurrentInstance().update("form:infoRegistroOperando");
   }

   public void contarRegistrosLovProceso() {
      RequestContext.getCurrentInstance().update("form:infoRegistroLovProceso");
   }

   public void contarRegistrosLovTipoPago() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTipoPago");
   }
   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

   public void verificarRastro() {
      if (tablaActiva == 0) {
         RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
      } else {
         if (tablaActiva == 1) {
            verificarRastroProceso();
         }
         if (tablaActiva == 2) {
            verificarRastroFormulaProceso();
         }
         if (tablaActiva == 3) {
            verificarRastroOperandoLog();
         }
      }
   }

   public void verificarRastroProceso() {
      if (procesoSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(procesoSeleccionado.getSecuencia(), "PROCESOS");
         backUp = procesoSeleccionado.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "Procesos";
            msnConfirmarRastro = "La tabla PROCESOS tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      }
   }

   public void verificarRastroProcesoH() {
      if (administrarRastros.verificarHistoricosTabla("PROCESOS")) {
         nombreTablaRastro = "Procesos";
         msnConfirmarRastroHistorico = "La tabla PROCESOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroFormulaProceso() {
      if (formulaProcesoSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(formulaProcesoSeleccionada.getSecuencia(), "FORMULASPROCESOS");
         backUp = formulaProcesoSeleccionada.getSecuencia();
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
      }
   }

   public void verificarRastroFormulaProcesoH() {
      if (administrarRastros.verificarHistoricosTabla("FORMULASPROCESOS")) {
         nombreTablaRastro = "FormulasProcesos";
         msnConfirmarRastroHistorico = "La tabla FORMULASPROCESOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroOperandoLog() {
      if (operandoLogSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(operandoLogSeleccionado.getSecuencia(), "OPERANDOSLOGS");
         backUp = operandoLogSeleccionado.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "OperandosLogs";
            msnConfirmarRastro = "La tabla OPERANDOSLOGS tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      }
   }

   public void verificarRastroOperandoLogH() {
      if (administrarRastros.verificarHistoricosTabla("OPERANDOSLOGS")) {
         nombreTablaRastro = "OperandosLogs";
         msnConfirmarRastroHistorico = "La tabla OPERANDOSLOGS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void posicionProcesoClonado() {
      if (guardado == true) {
         auxClonadoCodigo = procesoBaseClonado.getCodigo();
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void autoCompletarSeleccionarProcesoClonado(String valorConfirmar, int tipoAutoCompletar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (tipoAutoCompletar == 0) {
         short num = Short.parseShort(valorConfirmar);
         for (int i = 0; i < lovProcesos.size(); i++) {
            if (lovProcesos.get(i).getCodigo() == num) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            procesoBaseClonado = lovProcesos.get(indiceUnicoElemento);
         } else {
            procesoBaseClonado.setCodigo(auxClonadoCodigo);
            procesoBaseClonado.setDescripcion(auxDescripcionProceso);
            RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
            RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
            RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
            contarRegistrosLovProceso();
            RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
         }
      }
      if (tipoAutoCompletar == 1) {
         for (int i = 0; i < lovProcesos.size(); i++) {
            if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            procesoBaseClonado = lovProcesos.get(indiceUnicoElemento);
         } else {
            procesoBaseClonado.setCodigo(auxClonadoCodigo);
            procesoBaseClonado.setDescripcion(auxDescripcionProceso);
            RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
            RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
            RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
            contarRegistrosLovProceso();
            RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
         }
      }
   }

   public void dispararDialogoClonarProceso() {
      RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
      contarRegistrosLovProceso();
      RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
   }

   public void seleccionarProcesoClonado() {
      procesoBaseClonado = procesoLovSeleccionado;
      RequestContext.getCurrentInstance().update("form:CodigoBaseClonado");
      RequestContext.getCurrentInstance().update("form:DescripcionBaseClonado");
      procesoLovSeleccionado = new Procesos();
      filtrarLovProcesos = null;

      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovProceso:globalFilter");
      context.update("form:ProcesoDialogo");
      context.update("form:lovProceso");
      context.update("form:aceptarP");
      context.execute("PF('ProcesoDialogo').hide()");
      context.execute("PF('lovProceso').clearFilters()");
   }

   public void cancelarProcesoClonado() {
      RequestContext context = RequestContext.getCurrentInstance();
      procesoLovSeleccionado = new Procesos();
      filtrarLovProcesos = null;
      context.reset("form:lovProceso:globalFilter");
      context.execute("PF('ProcesoDialogo').hide()");
      context.execute("PF('lovProceso').clearFilters()");
   }

   public boolean validarNuevoProcesoClon() {
      boolean retorno = true;
      int conteo = 0;
      for (int i = 0; i < lovProcesos.size(); i++) {
         if (lovProcesos.get(i).getCodigo() == procesoNuevoClonado.getCodigo()) {
            conteo++;
         }
      }
      if (conteo > 0) {
         retorno = false;
      }
      return retorno;
   }

   public void clonarProceso() {
      if (!procesoNuevoClonado.getDescripcion().isEmpty() && procesoNuevoClonado.getCodigo() >= 1 && procesoBaseClonado.getCodigo() >= 1) {
         if (validarNuevoProcesoClon() == true) {
            errorClonado = administrarProcesos.clonarProceso(procesoNuevoClonado.getDescripcion(), procesoNuevoClonado.getCodigo(), procesoBaseClonado.getCodigo());
            if (errorClonado.equals("BIEN")) {
               FacesMessage msg = new FacesMessage("Información", "El registro fue clonado con Éxito.");
               FacesContext.getCurrentInstance().addMessage(null, msg);
               RequestContext.getCurrentInstance().update("form:growl");
            } else {
               RequestContext.getCurrentInstance().update("form:errorClonado");
               RequestContext.getCurrentInstance().execute("PF('errorClonado').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorCodigoClon').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosClonado').show()");
      }
   }

//GETTERS AND SETTERS
   public List<Procesos> getListaProcesos() {
      if (listaProcesos == null) {
         listaProcesos = administrarProcesos.listaProcesos();
      }
      return listaProcesos;
   }

   public void setListaProcesos(List<Procesos> setListaProcesos) {
      this.listaProcesos = setListaProcesos;
   }

   public List<Procesos> getFiltrarListaProcesos() {
      return filtrarListaProcesos;
   }

   public void setFiltrarListaProcesos(List<Procesos> setFiltrarListaProcesos) {
      this.filtrarListaProcesos = setFiltrarListaProcesos;
   }

   public Procesos getNuevoProceso() {
      return nuevoProceso;
   }

   public void setNuevoProceso(Procesos setNuevoProceso) {
      this.nuevoProceso = setNuevoProceso;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public Procesos getEditarProceso() {
      return editarProceso;
   }

   public void setEditarProceso(Procesos setEditarProceso) {
      this.editarProceso = setEditarProceso;
   }

   public Procesos getDuplicarProceso() {
      return duplicarProceso;
   }

   public void setDuplicarProceso(Procesos setDuplicarProceso) {
      this.duplicarProceso = setDuplicarProceso;
   }

   public List<FormulasProcesos> getListaFormulasProcesos() {
      if (listaFormulasProcesos == null) {
         if (procesoSeleccionado != null) {
            listaFormulasProcesos = administrarProcesos.listaFormulasProcesosParaProcesoSecuencia(procesoSeleccionado.getSecuencia());
         }
      }
      return listaFormulasProcesos;
   }

   public void setListaFormulasProcesos(List<FormulasProcesos> setListaFormulasProcesos) {
      this.listaFormulasProcesos = setListaFormulasProcesos;
   }

   public List<FormulasProcesos> getFiltrarListaFormulasProcesos() {
      return filtrarListaFormulasProcesos;
   }

   public void setFiltrarListaFormulasProcesos(List<FormulasProcesos> setFiltrarListaFormulasProcesos) {
      this.filtrarListaFormulasProcesos = setFiltrarListaFormulasProcesos;
   }

   public List<Procesos> getListProcesosModificar() {
      return listProcesosModificar;
   }

   public void setProcesosModificar(List<Procesos> setProcesosModificar) {
      this.listProcesosModificar = setProcesosModificar;
   }

   public List<Procesos> getListProcesosCrear() {
      return listProcesosCrear;
   }

   public void setListProcesosCrear(List<Procesos> setListProcesosCrear) {
      this.listProcesosCrear = setListProcesosCrear;
   }

   public List<Procesos> getListProcesosBorrar() {
      return listProcesosBorrar;
   }

   public void setListProcesosBorrar(List<Procesos> setListProcesosBorrar) {
      this.listProcesosBorrar = setListProcesosBorrar;
   }

   public List<FormulasProcesos> getListFormulasProcesosModificar() {
      return listFormulasProcesosModificar;
   }

   public void setListFormulasProcesosModificar(List<FormulasProcesos> setListFormulasProcesosModificar) {
      this.listFormulasProcesosModificar = setListFormulasProcesosModificar;
   }

   public FormulasProcesos getNuevoFormulaProceso() {
      return nuevoFormulaProceso;
   }

   public void setNuevoFormulaProceso(FormulasProcesos setNuevoFormulaProceso) {
      this.nuevoFormulaProceso = setNuevoFormulaProceso;
   }

   public List<FormulasProcesos> getListFormulasProcesosCrear() {
      return listFormulasProcesosCrear;
   }

   public void setListFormulasProcesosCrear(List<FormulasProcesos> setListFormulasProcesosCrear) {
      this.listFormulasProcesosCrear = setListFormulasProcesosCrear;
   }

   public List<FormulasProcesos> getLisFormulasProcesosBorrar() {
      return listFormulasProcesosBorrar;
   }

   public void setListFormulasProcesosBorrar(List<FormulasProcesos> setListFormulasProcesosBorrar) {
      this.listFormulasProcesosBorrar = setListFormulasProcesosBorrar;
   }

   public FormulasProcesos getEditarFormulaProceso() {
      return editarFormulaProceso;
   }

   public void setEditarFormulaProceso(FormulasProcesos setEditarFormulaProceso) {
      this.editarFormulaProceso = setEditarFormulaProceso;
   }

   public FormulasProcesos getDuplicarFormulaProceso() {
      return duplicarFormulaProceso;
   }

   public void setDuplicarFormulaProceso(FormulasProcesos setDuplicarFormulaProceso) {
      this.duplicarFormulaProceso = setDuplicarFormulaProceso;
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

   public BigInteger getBackUp() {
      return backUp;
   }

   public void setBackUp(BigInteger backUp) {
      this.backUp = backUp;
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

   public void setNombreTabla(String setNombreTabla) {
      this.nombreTabla = setNombreTabla;
   }

   public List<Tipospagos> getLovTiposPagos() {
      if (lovTiposPagos == null) {
         lovTiposPagos = administrarProcesos.lovTiposPagos();
      }
      return lovTiposPagos;
   }

   public void setLovTiposPagos(List<Tipospagos> setLovTiposPagos) {
      this.lovTiposPagos = setLovTiposPagos;
   }

   public List<Tipospagos> getFiltrarLovTiposPagos() {
      return filtrarLovTiposPagos;
   }

   public void setFiltrarLovTiposPagos(List<Tipospagos> setFiltrarLovTiposPagos) {
      this.filtrarLovTiposPagos = setFiltrarLovTiposPagos;
   }

   public Tipospagos getTipoPagoLovSeleccionado() {
      return tipoPagoLovSeleccionado;
   }

   public void setTipoPagoLovSeleccionado(Tipospagos setTipoPagoSeleccionado) {
      this.tipoPagoLovSeleccionado = setTipoPagoSeleccionado;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public String getAltoTablaProcesos() {
      return altoTablaProcesos;
   }

   public void setAltoTablaProcesos(String setAltoTablaProcesos) {
      this.altoTablaProcesos = setAltoTablaProcesos;
   }

   public String getAltoTablaForProYOperLogs() {
      return altoTablaForProYOperLogs;
   }

   public void setAltoTablaForProYOperLogs(String setAltoTablaFormulasProcesos) {
      this.altoTablaForProYOperLogs = setAltoTablaFormulasProcesos;
   }

   public List<Formulas> getLovFormulas() {
      if (lovFormulas == null) {
         lovFormulas = administrarProcesos.lovFormulas();
      }
      return lovFormulas;
   }

   public void setLovFormulas(List<Formulas> setLovFormulas) {
      this.lovFormulas = setLovFormulas;
   }

   public List<Formulas> getFiltrarLovFormulas() {
      return filtrarLovFormulas;
   }

   public void setFiltrarLovFormulas(List<Formulas> setFiltrarLovFormulas) {
      this.filtrarLovFormulas = setFiltrarLovFormulas;
   }

   public Formulas getFormulaLovSeleccionada() {
      return formulaLovSeleccionada;
   }

   public void setFormulaLovSeleccionada(Formulas setFormulaSeleccionado) {
      this.formulaLovSeleccionada = setFormulaSeleccionado;
   }

   public List<OperandosLogs> getListaOperandosLogs() {
      if (listaOperandosLogs == null) {
         if (procesoSeleccionado != null) {
            listaOperandosLogs = administrarProcesos.listaOperandosLogsParaProcesoSecuencia(procesoSeleccionado.getSecuencia());
         }
      }
      return listaOperandosLogs;
   }

   public void setListaOperandosLogs(List<OperandosLogs> setListaOperandosLogs) {
      this.listaOperandosLogs = setListaOperandosLogs;
   }

   public List<OperandosLogs> getFiltrarListaOperandosLogs() {
      return filtrarListaOperandosLogs;
   }

   public void setFiltrarListaOperandosLogs(List<OperandosLogs> setFiltrarListaOperandosLogs) {
      this.filtrarListaOperandosLogs = setFiltrarListaOperandosLogs;
   }

   public List<OperandosLogs> getListOperandosLogsModificar() {
      return listOperandosLogsModificar;
   }

   public void setListOperandosLogsModificar(List<OperandosLogs> setListOperandosLogsModificar) {
      this.listOperandosLogsModificar = setListOperandosLogsModificar;
   }

   public OperandosLogs getNuevoOperandoLog() {
      return nuevoOperandoLog;
   }

   public void setNuevoOperandoLog(OperandosLogs setNuevoOperandoLog) {
      this.nuevoOperandoLog = setNuevoOperandoLog;
   }

   public List<OperandosLogs> getListOperandosLogsCrear() {
      return listOperandosLogsCrear;
   }

   public void setListOperandosLogsCrear(List<OperandosLogs> setListOperandosLogsCrear) {
      this.listOperandosLogsCrear = setListOperandosLogsCrear;
   }

   public List<OperandosLogs> getListOperandosLogsBorrar() {
      return listOperandosLogsBorrar;
   }

   public void setListOperandosLogsBorrar(List<OperandosLogs> setListOperandosLogsBorrar) {
      this.listOperandosLogsBorrar = setListOperandosLogsBorrar;
   }

   public OperandosLogs getEditarOperandoLog() {
      return editarOperandoLog;
   }

   public void setEditarOperandoLog(OperandosLogs setEditarOperandoLog) {
      this.editarOperandoLog = setEditarOperandoLog;
   }

   public OperandosLogs getDuplicarOperandoLog() {
      return duplicarOperandoLog;
   }

   public void setDuplicarOperandoLog(OperandosLogs setDuplicarOperandoLog) {
      this.duplicarOperandoLog = setDuplicarOperandoLog;
   }

   public List<Operandos> getLovOperandos() {
      if (lovOperandos == null) {
         lovOperandos = administrarProcesos.lovOperandos();
      }
      return lovOperandos;
   }

   public void setLovOperandos(List<Operandos> setLovOperandos) {
      this.lovOperandos = setLovOperandos;
   }

   public List<Operandos> getFiltrarLovOperandos() {
      return filtrarLovOperandos;
   }

   public void setFiltrarLovOperandos(List<Operandos> setFiltrarLovOperandos) {
      this.filtrarLovOperandos = setFiltrarLovOperandos;
   }

   public Operandos getOperandoLovSeleccionado() {
      return operandoLovSeleccionado;
   }

   public void setOperandoLovSeleccionado(Operandos setOperandoSeleccionado) {
      this.operandoLovSeleccionado = setOperandoSeleccionado;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public Procesos getProcesoNuevoClonado() {
      return procesoNuevoClonado;
   }

   public void setProcesoNuevoClonado(Procesos procesoNuevoClonado) {
      this.procesoNuevoClonado = procesoNuevoClonado;
   }

   public Procesos getProcesoBaseClonado() {
      return procesoBaseClonado;
   }

   public void setProcesoBaseClonado(Procesos procesoBaseClonado) {
      this.procesoBaseClonado = procesoBaseClonado;
   }

   public List<Procesos> getLovProcesos() {
      if (lovProcesos == null) {
         getListaProcesos();
         if (listaProcesos != null) {
            if (!listaProcesos.isEmpty()) {
               lovProcesos = new ArrayList<Procesos>();
               for (int i = 0; i < listaProcesos.size(); i++) {
                  lovProcesos.add(listaProcesos.get(i));
               }
            }
         } else {
            lovProcesos = administrarProcesos.listaProcesos();
         }
      }
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> lovProcesos) {
      this.lovProcesos = lovProcesos;
   }

   public List<Procesos> getFiltrarLovProcesos() {
      return filtrarLovProcesos;
   }

   public void setFiltrarLovProcesos(List<Procesos> filtrarLovProcesos) {
      this.filtrarLovProcesos = filtrarLovProcesos;
   }

   public Procesos getProcesoLovSeleccionado() {
      return procesoLovSeleccionado;
   }

   public void setProcesoLovSeleccionado(Procesos procesoLovSeleccionado) {
      this.procesoLovSeleccionado = procesoLovSeleccionado;
   }

   public Procesos getProcesoSeleccionado() {
      return procesoSeleccionado;
   }

   public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
      this.procesoSeleccionado = procesoSeleccionado;
   }

   public FormulasProcesos getFormulaProcesoSeleccionada() {
      return formulaProcesoSeleccionada;
   }

   public void setFormulaProcesoSeleccionada(FormulasProcesos formulaProcesoSeleccionada) {
      this.formulaProcesoSeleccionada = formulaProcesoSeleccionada;
   }

   public OperandosLogs getOperandoLogSeleccionado() {
      return operandoLogSeleccionado;
   }

   public void setOperandoLogSeleccionado(OperandosLogs operandoLogSeleccionado) {
      this.operandoLogSeleccionado = operandoLogSeleccionado;
   }

   public String getInfoRegistroLovFormula() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovFormula");
      infoRegistroLovFormula = String.valueOf(tabla.getRowCount());
      return infoRegistroLovFormula;
   }

   public void setInfoRegistroLovFormula(String infoRegistroLovFormula) {
      this.infoRegistroLovFormula = infoRegistroLovFormula;
   }

   public String getInfoRegistroLovTipoPago() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTipoPago");
      infoRegistroLovTipoPago = String.valueOf(tabla.getRowCount());
      return infoRegistroLovTipoPago;
   }

   public void setInfoRegistroLovTipoPago(String infoRegistroLovTipoPago) {
      this.infoRegistroLovTipoPago = infoRegistroLovTipoPago;
   }

   public String getInfoRegistroLovProceso() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovProceso");
      infoRegistroLovProceso = String.valueOf(tabla.getRowCount());
      return infoRegistroLovProceso;
   }

   public void setInfoRegistroLovProceso(String infoRegistroLovProceso) {
      this.infoRegistroLovProceso = infoRegistroLovProceso;
   }

   public String getInfoRegistroLovOperando() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperando");
      infoRegistroLovOperando = String.valueOf(tabla.getRowCount());
      return infoRegistroLovOperando;
   }

   public void setInfoRegistroLovOperando(String infoRegistroLovOperando) {
      this.infoRegistroLovOperando = infoRegistroLovOperando;
   }

   public String getInfoRegistroProceso() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosProceso");
      infoRegistroProceso = String.valueOf(tabla.getRowCount());
      return infoRegistroProceso;
   }

   public void setInfoRegistroProceso(String infoRegistroProceso) {
      this.infoRegistroProceso = infoRegistroProceso;
   }

   public String getInfoRegistroFormulaP() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFormulaProceso");
      infoRegistroFormulaP = String.valueOf(tabla.getRowCount());
      return infoRegistroFormulaP;
   }

   public void setInfoRegistroFormulaP(String infoRegistroFormulaP) {
      this.infoRegistroFormulaP = infoRegistroFormulaP;
   }

   public String getInfoRegistroOperandoL() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosOperando");
      infoRegistroOperandoL = String.valueOf(tabla.getRowCount());
      return infoRegistroOperandoL;
   }

   public void setInfoRegistroOperandoL(String infoRegistroOperandoL) {
      this.infoRegistroOperandoL = infoRegistroOperandoL;
   }

   public String getAltoTablaProceso() {
      return altoTablaProceso;
   }

   public void setAltoTablaProceso(String altoTablaProceso) {
      this.altoTablaProceso = altoTablaProceso;
   }

   public String getErrorClonado() {
      return errorClonado;
   }

   public void setErrorClonado(String errorClonado) {
      this.errorClonado = errorClonado;
   }
}
