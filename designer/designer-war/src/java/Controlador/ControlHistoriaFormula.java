package Controlador;

import Entidades.EstructurasFormulas;
import Entidades.Formulas;
import Entidades.Historiasformulas;
import Entidades.Nodos;
import Entidades.Operadores;
import Entidades.Operandos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarHistoriaFormulaInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
import org.primefaces.component.scrollpanel.ScrollPanel;
import org.primefaces.context.RequestContext;

/**
 *
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlHistoriaFormula implements Serializable {

   @EJB
   AdministrarHistoriaFormulaInterface administrarHistoriaFormula;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //////////////Formulas//////////////////
   private Formulas formulaActual;
   ///////////Formulascontratos////////////
   private List<Historiasformulas> listHistoriasFormulas;
   private Historiasformulas historiaFormulaSeleccionada;
   private List<Historiasformulas> filtrarListHistoriasFormulas;
   ///////////Formulascontratos/////////////
   private int banderaHistoriasFormulas;
//   private int indexHistoriasFormulas, indexAuxHistoriasFormulas;
   private List<Historiasformulas> listHistoriasFormulasModificar;
   private Historiasformulas nuevaHistoriaFormula;
   private List<Historiasformulas> listHistoriasFormulasCrear;
   private List<Historiasformulas> listHistoriasFormulasBorrar;
   private Historiasformulas editarHistoriaFormula;
   private int cualCeldaHistoriasFormulas, tipoLista;
   private Historiasformulas duplicarHistoriaFormula;
   private boolean cambiosHistoriaFormula;
   private BigInteger backUpSecRegistroHistoriaFormula;
   private String observacion;
   private Date fechaIni, fechaFin;
   private Column historiaFechaInicial, historiaFechaFinal, historiaNota;
   //////////////Otros////////////////Otros////////////////////
   private boolean aceptar;
   private boolean guardado;
   private BigInteger l;
   private int k;
   private String nombreXML, nombreExportar;
   private String nombreTabla;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private int tipoActualizacion;
   private Date fechaParametro;
   /////////////////////
   private List<Nodos> listNodosHistoriaFormula;
   private String formulaCompleta;
   private String nodo1, nodo2, nodo3, nodo4, nodo5, nodo6, nodo7, nodo8, nodo9, nodo10, nodo11, nodo12, nodo13, nodo14, nodo15, nodo16;
   private boolean nodo1RO, nodo2RO, nodo3RO, nodo4RO, nodo5RO, nodo6RO, nodo7RO, nodo8RO, nodo9RO, nodo10RO, nodo11RO, nodo12RO, nodo13RO, nodo14RO, nodo15RO, nodo16RO;
   private String nodo1_1, nodo2_1, nodo3_1, nodo4_1, nodo5_1, nodo6_1, nodo7_1, nodo8_1, nodo9_1, nodo10_1, nodo11_1, nodo12_1, nodo13_1, nodo14_1, nodo15_1, nodo16_1;
   private boolean nodo1_1RO, nodo2_1RO, nodo3_1RO, nodo4_1RO, nodo5_1RO, nodo6_1RO, nodo7_1RO, nodo8_1RO, nodo9_1RO, nodo10_1RO, nodo11_1RO, nodo12_1RO, nodo13_1RO, nodo14_1RO, nodo15_1RO, nodo16_1RO;
   //////Operadores//////////
   private Operadores operadorSeleccionado;
   private List<Operadores> listOperadores;
   private List<Operadores> filtrarListOperadores;
   //////Operandos//////////
   private Operandos operandoSeleccionado;
   private List<Operandos> listOperandos;
   private List<Operandos> filtrarListOperandos;
   /////////////////////
   private int indexNodoSeleecionado;
   private String auxNodoSeleccionado;
   private int actualizacionNodo;
   private List<Nodos> listNodosCrear;
   private List<Nodos> listNodosBorrar;
   private List<Nodos> listNodosModificar;
   private BigInteger secuenciaRegistroNodos, backUpSecuenciaRegistroNodo;
   private boolean cambiosNodos;
   private List<Nodos> listNodosParaExportar;
   private Nodos editarNodo;
   //////////EstructurasFormulas ///////////////
   private List<EstructurasFormulas> listEstructurasFormulas;
   private List<EstructurasFormulas> filtrarListEstructurasFormulas;
   private String auxEF_Formula, auxEF_Descripcion;
   private BigInteger auxEF_IdFormula, auxEF_IdHijo;
   private int indexEstructuraFormula, celdaEstructuraFormula;
   private EstructurasFormulas seleccionEstructuraF;
   private EstructurasFormulas editarEstructura;
   private boolean visibilidadBtnP, visibilidadBtnS;
   private ScrollPanel panelNodosPrincipal, panelNodosSecundario;

   private DataTable tabla;
   private String infoRegistro, infoRegistroOperador, infoRegistroOperando;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlHistoriaFormula() {
      visibilidadBtnP = false;
      visibilidadBtnS = false;
      editarEstructura = new EstructurasFormulas();
      seleccionEstructuraF = new EstructurasFormulas();
      indexEstructuraFormula = -1;
      celdaEstructuraFormula = -1;
      listEstructurasFormulas = null;
      editarNodo = new Nodos();
      listNodosCrear = new ArrayList<Nodos>();
      listNodosBorrar = new ArrayList<Nodos>();
      listNodosModificar = new ArrayList<Nodos>();
      actualizacionNodo = -1;
      listOperadores = null;
      listOperandos = null;
      operandoSeleccionado = new Operandos();
      operadorSeleccionado = new Operadores();
      nodo1 = "";
      nodo2 = "";
      nodo3 = "";
      nodo4 = "";
      nodo5 = "";
      nodo6 = "";
      nodo7 = "";
      nodo8 = "";
      nodo9 = "";
      nodo10 = "";
      nodo11 = "";
      nodo12 = "";
      nodo13 = "";
      nodo14 = "";
      nodo15 = "";
      nodo16 = "";
      nodo1RO = true;
      nodo2RO = true;
      nodo3RO = true;
      nodo4RO = true;
      nodo5RO = true;
      nodo6RO = true;
      nodo7RO = true;
      nodo8RO = true;
      nodo9RO = true;
      nodo10RO = true;
      nodo11RO = true;
      nodo12RO = true;
      nodo13RO = true;
      nodo14RO = true;
      nodo15RO = true;
      nodo16RO = true;
      nodo1_1 = "";
      nodo2_1 = "";
      nodo3_1 = "";
      nodo4_1 = "";
      nodo5_1 = "";
      nodo6_1 = "";
      nodo7_1 = "";
      nodo8_1 = "";
      nodo9_1 = "";
      nodo10_1 = "";
      nodo11_1 = "";
      nodo12_1 = "";
      nodo13_1 = "";
      nodo14_1 = "";
      nodo15_1 = "";
      nodo16_1 = "";
      nodo1_1RO = true;
      nodo2_1RO = true;
      nodo3_1RO = true;
      nodo4_1RO = true;
      nodo5_1RO = true;
      nodo6_1RO = true;
      nodo7_1RO = true;
      nodo8_1RO = true;
      nodo9_1RO = true;
      nodo10_1RO = true;
      nodo11_1RO = true;
      nodo12_1RO = true;
      nodo13_1RO = true;
      nodo14_1RO = true;
      nodo15_1RO = true;
      nodo16_1RO = true;
      formulaCompleta = "";
      listNodosHistoriaFormula = null;
      formulaActual = new Formulas();
      listHistoriasFormulas = null;
      fechaParametro = new Date(1, 1, 0);

      nombreExportar = "";
      nombreTablaRastro = "";
      backUp = null;
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";

      historiaFormulaSeleccionada = null;
      backUpSecRegistroHistoriaFormula = null;
      aceptar = true;
      k = 0;

      listHistoriasFormulasBorrar = new ArrayList<Historiasformulas>();
      listHistoriasFormulasCrear = new ArrayList<Historiasformulas>();
      listHistoriasFormulasModificar = new ArrayList<Historiasformulas>();
      editarHistoriaFormula = new Historiasformulas();
      cualCeldaHistoriasFormulas = -1;
      tipoLista = 0;
      guardado = true;
      nuevaHistoriaFormula = new Historiasformulas();
      banderaHistoriasFormulas = 0;
      nombreTabla = ":formExportarHistoria:datosHistoriaFormulasExportar";
      nombreXML = "HistoriaFormula_XML";
      duplicarHistoriaFormula = new Historiasformulas();
      cambiosHistoriaFormula = false;
      cambiosNodos = false;

      paginaAnterior = "nominaf";
      infoRegistro = "";
      infoRegistroOperador = "";
      infoRegistroOperando = "";
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
         String pagActual = "historiaformula";
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

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarHistoriaFormula.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public boolean validarSolapamientoFechas(int i) {
      boolean retorno = false;
      if (i == 0) {
         if (!listHistoriasFormulas.isEmpty()) {
            int conteo = 0;
            for (int j = 1; j < listHistoriasFormulas.size(); j++) {
               if ((listHistoriasFormulas.get(j - 1).getFechafinal().after(listHistoriasFormulas.get(j).getFechainicial()))
                       || (listHistoriasFormulas.get(j - 1).getFechainicial().after(listHistoriasFormulas.get(j).getFechainicial()))) {
                  conteo++;
               }
            }
            if (conteo == 0) {
               retorno = true;
            }
         }
      }
      if (i == 1) {
         if (!listHistoriasFormulas.isEmpty()) {
            int conteo = 0;
            int tam = listHistoriasFormulas.size();
            if (tam >= 2) {
               for (int j = 1; j < listHistoriasFormulas.size(); j++) {
                  if ((listHistoriasFormulas.get(j - 1).getFechafinal().after(nuevaHistoriaFormula.getFechainicial()))
                          || (listHistoriasFormulas.get(j - 1).getFechainicial().after(nuevaHistoriaFormula.getFechainicial()))) {
                     conteo++;
                  }
               }
            }
            if (tam == 1) {
               if ((listHistoriasFormulas.get(0).getFechafinal().after(nuevaHistoriaFormula.getFechainicial()))
                       || (listHistoriasFormulas.get(0).getFechainicial().after(nuevaHistoriaFormula.getFechainicial()))) {
                  conteo++;
               }
            }
            if (conteo == 0) {
               retorno = true;
            }
         } else {
            retorno = true;
         }
      }
      if (i == 2) {
         if (!listHistoriasFormulas.isEmpty()) {
            int conteo = 0;
            int tam = listHistoriasFormulas.size();
            if (tam >= 2) {
               for (int j = 1; j < listHistoriasFormulas.size(); j++) {
                  if ((listHistoriasFormulas.get(j - 1).getFechafinal().after(duplicarHistoriaFormula.getFechainicial()))
                          || (listHistoriasFormulas.get(j - 1).getFechainicial().after(duplicarHistoriaFormula.getFechainicial()))) {
                     conteo++;
                  }
               }
            }
            if (tam == 1) {
               if ((listHistoriasFormulas.get(0).getFechafinal().after(duplicarHistoriaFormula.getFechainicial()))
                       || (listHistoriasFormulas.get(0).getFechainicial().after(duplicarHistoriaFormula.getFechainicial()))) {
                  conteo++;
               }
            }
            if (conteo == 0) {
               retorno = true;
            }
         } else {
            retorno = true;
         }
      }
      return retorno;
   }

   public void recibirFormulaYPagina(Formulas formula, String pagina) {
//      formulaActual = administrarHistoriaFormula.actualFormula(secuencia);
      System.out.println("Controlador.ControlHistoriaFormula.recibirFormulaYPagina()");
      System.out.println("formula" + formula);
      System.out.println("formula.getNombrecorto()" + formula.getNombrecorto());
      paginaAnterior = pagina;
      formulaActual = formula;
      listHistoriasFormulas = null;
      listEstructurasFormulas = null;
      getListHistoriasFormulas();
      if (listHistoriasFormulas != null) {
         if (!listHistoriasFormulas.isEmpty()) {
            historiaFormulaSeleccionada = listHistoriasFormulas.get(0);
         }
      }
      getListNodosHistoriaFormula();
      cargarDatosParaNodos2();
      listNodosParaExportar = null;
      getListEstructurasFormulas();

      FacesContext fc = FacesContext.getCurrentInstance();
      fc.getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "historiaFormula");
   }

   public String retornarPagina() {
      return paginaAnterior;
   }

   public void modificarHistoriaFormula(Historiasformulas historiaformula) {
      historiaFormulaSeleccionada = historiaformula;
      boolean retorno = validarSolapamientoFechas(0);
      if (retorno == true) {
         int aux = 0;
         String nota = historiaFormulaSeleccionada.getObservaciones();
         if (!nota.isEmpty()) {
            aux = nota.length();
         }
         if (aux >= 0 && aux <= 200) {
            if (!listHistoriasFormulasCrear.contains(historiaFormulaSeleccionada)) {
               if (listHistoriasFormulasModificar.isEmpty()) {
                  listHistoriasFormulasModificar.add(historiaFormulaSeleccionada);
               } else if (!listHistoriasFormulasModificar.contains(historiaFormulaSeleccionada)) {
                  listHistoriasFormulasModificar.add(historiaFormulaSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
            RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
            cambiosHistoriaFormula = true;
         } else {
            historiaFormulaSeleccionada.setObservaciones(observacion);
            RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
            RequestContext.getCurrentInstance().execute("PF('errorNotaHF').show()");
         }
      }
   }

   public void cambiarIndiceHistoriaFormula(Historiasformulas historiaFormula, int celda) {
      if (cambiosNodos == false) {
         cualCeldaHistoriasFormulas = celda;
         historiaFormulaSeleccionada = historiaFormula;
         fechaIni = historiaFormulaSeleccionada.getFechainicial();
         fechaFin = historiaFormulaSeleccionada.getFechafinal();
         observacion = historiaFormulaSeleccionada.getObservaciones();
         listNodosParaExportar = null;
         listEstructurasFormulas = null;
         listNodosHistoriaFormula = null;
         cargarDatosParaNodos();
         indexNodoSeleecionado = -1;
         indexEstructuraFormula = -1;
         System.out.println("Controlador.ControlHistoriaFormula.cambiarIndiceHistoriaFormula()");
         System.out.println("historiaFormulaSeleccionada : " + historiaFormulaSeleccionada);
         getListEstructurasFormulas();
         if (listEstructurasFormulas != null) {
            System.out.println("listEstructurasFormulas.size() : " + listEstructurasFormulas.size());
         } else {
            System.out.println("listEstructurasFormulas : " + listEstructurasFormulas);
         }
         RequestContext.getCurrentInstance().update("form:datosEstructuraFormula");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public boolean validarFechasRegistroHistoriaFormula(int i) {
      boolean retorno = false;
      if (i == 0) {
         Historiasformulas auxiliar = listHistoriasFormulas.get(i);
         if (auxiliar.getFechainicial().after(fechaParametro) && (auxiliar.getFechainicial().before(auxiliar.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 1) {
         if (nuevaHistoriaFormula.getFechainicial().after(fechaParametro) && (nuevaHistoriaFormula.getFechainicial().before(nuevaHistoriaFormula.getFechafinal()))) {
            retorno = true;
         }
      }
      if (i == 2) {
         if (duplicarHistoriaFormula.getFechainicial().after(fechaParametro) && (duplicarHistoriaFormula.getFechainicial().before(duplicarHistoriaFormula.getFechafinal()))) {
            retorno = true;
         }
      }
      return retorno;
   }

   public void modificacionesFechaHistoriaFormula(Historiasformulas historiaformula, int c) {
      historiaFormulaSeleccionada = historiaformula;
      if ((historiaFormulaSeleccionada.getFechainicial() != null) && (historiaFormulaSeleccionada.getFechafinal() != null)) {
         boolean validacion = validarFechasRegistroHistoriaFormula(0);
         if (validacion == true) {
            if (validarSolapamientoFechas(0) == true) {
               cambiarIndiceHistoriaFormula(historiaFormulaSeleccionada, c);
               modificarHistoriaFormula(historiaFormulaSeleccionada);
               RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
            } else {
               historiaFormulaSeleccionada.setFechafinal(fechaFin);
               historiaFormulaSeleccionada.setFechainicial(fechaIni);
               RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
               RequestContext.getCurrentInstance().execute("PF('errorSolapamientoFechas').show()");
            }
         } else {
            historiaFormulaSeleccionada.setFechainicial(fechaIni);
            historiaFormulaSeleccionada.setFechafinal(fechaFin);
            RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
            RequestContext.getCurrentInstance().execute("PF('errorFechasHF').show()");
         }
      } else {
         historiaFormulaSeleccionada.setFechainicial(fechaIni);
         historiaFormulaSeleccionada.setFechafinal(fechaFin);
         RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   //GUARDAR
   public void guardarYSalir() {
      guardadoGeneral();
      salir();
   }

   public void guardadoGeneral() {
      if (cambiosHistoriaFormula == true) {
         guardarCambiosHistoriaFormula();
      }
      if (cambiosNodos == true) {
         guardarCambiosNodos();
      }
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void guardarCambiosHistoriaFormula() {
      System.out.println("Controlador.ControlHistoriaFormula.guardarCambiosHistoriaFormula()");
      FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:growl");
      if (!listHistoriasFormulasBorrar.isEmpty()) {
         System.out.println("listHistoriasFormulasBorrar : " + listHistoriasFormulasBorrar);
         administrarHistoriaFormula.borrarHistoriasFormulas(listHistoriasFormulasBorrar);
         listHistoriasFormulasBorrar.clear();
      }
      if (!listHistoriasFormulasCrear.isEmpty()) {
         administrarHistoriaFormula.crearHistoriasFormulas(listHistoriasFormulasCrear);
         listHistoriasFormulasCrear.clear();
      }
      if (!listHistoriasFormulasModificar.isEmpty()) {
         administrarHistoriaFormula.editarHistoriasFormulas(listHistoriasFormulasModificar);
         listHistoriasFormulasModificar.clear();
      }
      listHistoriasFormulas = null;
      k = 0;
      cambiosHistoriaFormula = false;

      RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
      RequestContext.getCurrentInstance().update("form:growl");

   }

   public void guardarCambiosNodos() {
      FacesMessage msg = new FacesMessage("Información", "Los datos se guardaron con Éxito.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:growl");
      if (!listNodosBorrar.isEmpty()) {
         administrarHistoriaFormula.borrarNodos(listNodosBorrar);
         listNodosBorrar.clear();
      }
      if (!listNodosCrear.isEmpty()) {
         administrarHistoriaFormula.crearNodos(listNodosCrear);
         listNodosCrear.clear();
      }
      if (!listNodosModificar.isEmpty()) {

         administrarHistoriaFormula.editarNodos(listNodosModificar);
         listNodosModificar.clear();
      }
      listNodosHistoriaFormula = null;
      listNodosParaExportar = null;
      cargarDatosParaNodos();
      RequestContext.getCurrentInstance().update("form:growl");
      k = 0;
//      indexNodoSeleecionado = -1;
//      secuenciaRegistroNodos = null;
      cambiosNodos = false;
   }

   //CANCELAR MODIFICACIONES
   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacion() {
      cancelarModificacionesHistoriaFormula();
      cancelarModificacionNodos();
   }

   public void cancelarModificacionesHistoriaFormula() {
      if (banderaHistoriasFormulas == 1) {
         historiaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaInicial");
         historiaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         historiaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaFinal");
         historiaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         historiaNota = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaNota");
         historiaNota.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
         banderaHistoriasFormulas = 0;
         filtrarListHistoriasFormulas = null;
         tipoLista = 0;
      }
      listHistoriasFormulasBorrar.clear();
      listHistoriasFormulasCrear.clear();
      listHistoriasFormulasModificar.clear();
      historiaFormulaSeleccionada = null;
      k = 0;
      listHistoriasFormulas = null;
      guardado = true;
      cambiosHistoriaFormula = false;
      getListHistoriasFormulas();
      RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
   }

   public void cancelarModificacionNodos() {
      listNodosBorrar.clear();
      listNodosCrear.clear();
      listNodosModificar.clear();
      indexNodoSeleecionado = -1;
      secuenciaRegistroNodos = null;
      k = 0;
      listNodosHistoriaFormula = null;
      guardado = true;
      cambiosNodos = false;
      listNodosParaExportar = null;
//      getListNodosHistoriaFormula();
      cambiarVisibilidadPaneles(1);
      cargarDatosParaNodos();
      listEstructurasFormulas = null;
      RequestContext.getCurrentInstance().update("form:datosEstructuraFormula");
//      getListEstructurasFormulas();
   }

   public void darSeleccion() {
      System.out.println("Controlador.ControlHistoriaFormula.darSeleccion() historiaFormulaSeleccionada : " + historiaFormulaSeleccionada);
      if (historiaFormulaSeleccionada != null) {
         if (listHistoriasFormulas.contains(historiaFormulaSeleccionada)) {
            FacesContext c = FacesContext.getCurrentInstance();
            tabla = (DataTable) c.getViewRoot().findComponent("form:datosHistoriaFormula");
            tabla.setSelection(historiaFormulaSeleccionada);
            int n = listHistoriasFormulas.indexOf(historiaFormulaSeleccionada);
            RequestContext.getCurrentInstance().execute("PF('datosHistoriaFormula').unselectAllRows();PF('datosHistoriaFormula').selectRow(" + n + ");");
//            FacesContext c = FacesContext.getCurrentInstance();
//            tabla = (DataTable) c.getViewRoot().findComponent("form:datosHistoriaFormula");
//            tabla.setSelection(historiaFormulaSeleccionada);
//            if (listHistoriasFormulas.indexOf(historiaFormulaSeleccionada) == 0) {
//               RequestContext.getCurrentInstance().execute("PF('datosHistoriaFormula').unselectAllRows();PF('datosHistoriaFormula').selectRow(0);");
//            }
         }
      }
   }

   public void editarCelda() {
      if (historiaFormulaSeleccionada != null) {
         editarHistoriaFormula = historiaFormulaSeleccionada;
         if (cualCeldaHistoriasFormulas == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialHFD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialHFD').show()");
            cualCeldaHistoriasFormulas = -1;
         } else if (cualCeldaHistoriasFormulas == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalHFD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalHFD').show()");
            cualCeldaHistoriasFormulas = -1;
         } else if (cualCeldaHistoriasFormulas == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacionHFD");
            RequestContext.getCurrentInstance().execute("PF('editarObservacionHFD').show()");
            cualCeldaHistoriasFormulas = -1;
         }
      }
      if (indexNodoSeleecionado >= 0) {
         editarNodo = listNodosHistoriaFormula.get(indexNodoSeleecionado);
         if (editarNodo.getOperador().getSecuencia() != null) {
            String aux = editarNodo.getOperador().getSigno();
            editarNodo.setDescripcionNodo(aux);
            RequestContext.getCurrentInstance().update("formularioDialogos:editarOperadorNodoD");
            RequestContext.getCurrentInstance().execute("PF('editarOperadorNodoD').show()");
         }
         if (editarNodo.getOperando().getSecuencia() != null) {
            String aux = editarNodo.getOperando().getNombre();
            editarNodo.setDescripcionNodo(aux);
            RequestContext.getCurrentInstance().update("formularioDialogos:editarOperandoNodoD");
            RequestContext.getCurrentInstance().execute("PF('editarOperandoNodoD').show()");
         }
//         indexNodoSeleecionado = -1;
//         secuenciaRegistroNodos = null;
      }
      if (indexEstructuraFormula >= 0) {
         editarEstructura = listEstructurasFormulas.get(indexEstructuraFormula);
         if (celdaEstructuraFormula == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaEFD");
            RequestContext.getCurrentInstance().execute("PF('editarFormulaEFD').show()");
            celdaEstructuraFormula = -1;
         } else if (celdaEstructuraFormula == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionEFD");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionEFD').show()");
            celdaEstructuraFormula = -1;
         } else if (celdaEstructuraFormula == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarIdFormulaEFD");
            RequestContext.getCurrentInstance().execute("PF('editarIdFormulaEFD').show()");
            celdaEstructuraFormula = -1;
         } else if (celdaEstructuraFormula == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarIdHijoEFD");
            RequestContext.getCurrentInstance().execute("PF('editarIdHijoEFD').show()");
            celdaEstructuraFormula = -1;
         }
//         indexEstructuraFormula = -1;
      }
   }

   public void ingresoNuevoRegistro() {
      if (listHistoriasFormulas != null) {
         if (listHistoriasFormulas.size() == 0) {
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroHistoria').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPagina').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroHistoria').show()");
      }
   }

   public void validarIngresoNuevaHistoriaFormula() {
      limpiarNuevoHistoriaFormula();
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaHF");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroHistoria");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroHistoria').show()");
   }

   public void validarDuplicadoRegistro() {
      if (historiaFormulaSeleccionada != null) {
         duplicarHistoriaFormulaM();
      } else if (indexNodoSeleecionado >= 0) {
         RequestContext.getCurrentInstance().execute("PF('errorDuplicarNodo').show()");
      } else if (indexEstructuraFormula >= 0) {
         RequestContext.getCurrentInstance().execute("PF('errorDuplicarNodo').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void validarBorradoRegistro() {
      if (historiaFormulaSeleccionada != null && indexNodoSeleecionado < 0 && indexEstructuraFormula < 0) {
         if (listNodosHistoriaFormula.isEmpty()) {
            borrarHistoriaFormula();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorBorradoHistoriaF').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('verificarBorrarRegistros').show()");
      }
   }

   public void validarBorradoHistoriaF() {
      if (historiaFormulaSeleccionada != null) {
         if (listNodosHistoriaFormula == null) {
            borrarHistoriaFormula();
         } else if (listNodosHistoriaFormula.isEmpty()) {
            borrarHistoriaFormula();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorBorradoHistoriaF').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void validarBorradoNodo() {
      if (indexNodoSeleecionado >= 0) {
         borrarNodo();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public boolean validarNuevosDatosHistoriaFormula(int i) {
      boolean retorno = false;
      if (i == 1) {
         if ((nuevaHistoriaFormula.getFechafinal() != null)
                 && (nuevaHistoriaFormula.getFechainicial() != null)) {
            retorno = true;
         }
      }
      if (i == 2) {
         if ((duplicarHistoriaFormula.getFechafinal() != null)
                 && (duplicarHistoriaFormula.getFechainicial() != null)) {
            retorno = true;
         }
      }
      return retorno;
   }

   public void agregarNuevoHistoriaFormula() {
      boolean resp = validarNuevosDatosHistoriaFormula(1);
      if (resp == true) {
         boolean validacion = validarFechasRegistroHistoriaFormula(1);
         if (validacion == true) {
            int tam = 0;
            String aux = nuevaHistoriaFormula.getObservaciones();
            if (!aux.isEmpty()) {
               tam = aux.length();
            }
            if (tam >= 0 && tam <= 200) {
               if (validarSolapamientoFechas(1) == true) {
                  if (banderaHistoriasFormulas == 1) {
                     historiaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaInicial");
                     historiaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                     historiaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaFinal");
                     historiaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                     historiaNota = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaNota");
                     historiaNota.setFilterStyle("display: none; visibility: hidden;");
                     RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
                     banderaHistoriasFormulas = 0;
                     filtrarListHistoriasFormulas = null;
                     tipoLista = 0;
                  }
                  k++;

                  BigInteger var = BigInteger.valueOf(k);
                  nuevaHistoriaFormula.setSecuencia(var);
                  nuevaHistoriaFormula.setFormula(formulaActual);
                  listHistoriasFormulasCrear.add(nuevaHistoriaFormula);
                  listHistoriasFormulas.add(nuevaHistoriaFormula);
                  historiaFormulaSeleccionada = listHistoriasFormulas.get(listHistoriasFormulas.indexOf(nuevaHistoriaFormula));
                  ////------////
                  nuevaHistoriaFormula = new Historiasformulas();
                  ////-----////
                  RequestContext.getCurrentInstance().execute("PF('NuevoRegistroHistoria').hide()");
                  RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
                  contarRegistros();
                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  cambiosHistoriaFormula = true;
               } else {
                  RequestContext.getCurrentInstance().execute("PF('errorSolapamientoFechas').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorNotaHF').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasHF').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarNuevoHistoriaFormula() {
      nuevaHistoriaFormula = new Historiasformulas();
   }

   public void duplicarHistoriaFormulaM() {
      if (historiaFormulaSeleccionada != null) {
         duplicarHistoriaFormula = new Historiasformulas();
         duplicarHistoriaFormula.setObservaciones(historiaFormulaSeleccionada.getObservaciones());
         duplicarHistoriaFormula.setFechafinal(historiaFormulaSeleccionada.getFechafinal());
         duplicarHistoriaFormula.setFechainicial(historiaFormulaSeleccionada.getFechainicial());

         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroHistoria");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroHistoria').show()");

      }
   }

   public void confirmarDuplicarHistoriaFormula() {
      boolean resp = validarNuevosDatosHistoriaFormula(2);
      if (resp == true) {
         boolean validacion = validarFechasRegistroHistoriaFormula(2);
         if (validacion == true) {
            int tam = 0;
            String aux = duplicarHistoriaFormula.getObservaciones();
            if (!aux.isEmpty()) {
               tam = aux.length();
            }
            if (tam >= 0 && tam <= 200) {
               if (validarSolapamientoFechas(2) == true) {
                  if (banderaHistoriasFormulas == 1) {
                     historiaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaInicial");
                     historiaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                     historiaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaFinal");
                     historiaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                     historiaNota = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaNota");
                     historiaNota.setFilterStyle("display: none; visibility: hidden;");
                     banderaHistoriasFormulas = 0;
                     filtrarListHistoriasFormulas = null;
                     tipoLista = 0;
                  }
                  k++;
                  BigInteger var = BigInteger.valueOf(k);

                  duplicarHistoriaFormula.setSecuencia(var);
                  duplicarHistoriaFormula.setFormula(formulaActual);
                  listHistoriasFormulasCrear.add(duplicarHistoriaFormula);
                  listHistoriasFormulas.add(duplicarHistoriaFormula);
                  historiaFormulaSeleccionada = listHistoriasFormulas.get(listHistoriasFormulas.indexOf(duplicarHistoriaFormula));
                  duplicarHistoriaFormula = new Historiasformulas();

                  RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
                  contarRegistros();
                  if (guardado == true) {
                     guardado = false;
                     RequestContext.getCurrentInstance().update("form:ACEPTAR");
                  }
                  RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroHistoria').hide()");
                  cambiosHistoriaFormula = true;
               } else {
                  RequestContext.getCurrentInstance().execute("PF('errorSolapamientoFechas').show()");
               }
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorNotaHF').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorFechasHF').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRegNuevo').show()");
      }
   }

   public void limpiarDuplicarHistoriaFormula() {
      duplicarHistoriaFormula = new Historiasformulas();
   }

   ///////////////////////////////////////////////////////////////
   public void borrarHistoriaFormula() {
      if (historiaFormulaSeleccionada != null) {
         if (!listHistoriasFormulasModificar.isEmpty() && listHistoriasFormulasModificar.contains(historiaFormulaSeleccionada)) {
            listHistoriasFormulasModificar.remove(historiaFormulaSeleccionada);
            listHistoriasFormulasBorrar.add(historiaFormulaSeleccionada);
         } else if (!listHistoriasFormulasCrear.isEmpty() && listHistoriasFormulasCrear.contains(historiaFormulaSeleccionada)) {
            listHistoriasFormulasCrear.remove(historiaFormulaSeleccionada);
         } else {
            listHistoriasFormulasBorrar.add(historiaFormulaSeleccionada);
         }
         listHistoriasFormulas.remove(historiaFormulaSeleccionada);
         if (tipoLista == 1) {
            filtrarListHistoriasFormulas.remove(historiaFormulaSeleccionada);
         }
         historiaFormulaSeleccionada = null;
         RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
         contarRegistros();
         cambiosHistoriaFormula = true;
         if (guardado == true) {
            guardado = false;
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
      if (banderaHistoriasFormulas == 0) {
         historiaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaInicial");
         historiaFechaInicial.setFilterStyle("width: 85% !important;");
         historiaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaFinal");
         historiaFechaFinal.setFilterStyle("width: 85% !important;");
         historiaNota = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaNota");
         historiaNota.setFilterStyle("width: 85% !important");

         RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
         banderaHistoriasFormulas = 1;
      } else if (banderaHistoriasFormulas == 1) {
         historiaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaInicial");
         historiaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         historiaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaFinal");
         historiaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         historiaNota = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaNota");
         historiaNota.setFilterStyle("display: none; visibility: hidden;");

         RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
         banderaHistoriasFormulas = 0;
         filtrarListHistoriasFormulas = null;
         tipoLista = 0;
      }
   }
   //SALIR

   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      if (banderaHistoriasFormulas == 1) {
         historiaFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaInicial");
         historiaFechaInicial.setFilterStyle("display: none; visibility: hidden;");
         historiaFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaFechaFinal");
         historiaFechaFinal.setFilterStyle("display: none; visibility: hidden;");
         historiaNota = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosHistoriaFormula:historiaNota");
         historiaNota.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHistoriaFormula");
         banderaHistoriasFormulas = 0;
         filtrarListHistoriasFormulas = null;
         tipoLista = 0;
      }
      listHistoriasFormulasBorrar.clear();
      listHistoriasFormulasCrear.clear();
      listHistoriasFormulasModificar.clear();
      historiaFormulaSeleccionada = null;
      formulaActual = null;
      k = 0;
      listHistoriasFormulas = null;
      guardado = true;
      cambiosHistoriaFormula = false;
      nuevaHistoriaFormula = new Historiasformulas();
      duplicarHistoriaFormula = new Historiasformulas();
      listNodosHistoriaFormula = null;
      listNodosCrear.clear();
      listNodosBorrar.clear();
      listNodosModificar.clear();
      formulaCompleta = null;
      indexNodoSeleecionado = -1;
      secuenciaRegistroNodos = null;
      aceptar = true;
      listNodosParaExportar = null;
      listEstructurasFormulas = null;
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void dispararDialogoGuardarCambios() {
      if (cambiosHistoriaFormula == true || cambiosNodos == true) {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show();");
      }
   }

   /**
    *
    * @return Nombre del dialogo a exportar en XML
    */
   public String exportXML() {
      if (historiaFormulaSeleccionada != null) {
         nombreTabla = ":formExportarHistoria:datosHistoriaFormulasExportar";
         nombreXML = "HistoriaFormula_XML";
      }
      if (indexNodoSeleecionado >= 0) {
         nombreTabla = ":formExportarNodos:datosNodosExportar";
         nombreXML = "GenerarFormula_XML";
      }
      if (indexEstructuraFormula >= 0) {
         nombreTabla = ":formExportarEstructura:datosEstructuraExportar";
         nombreXML = "EstructuraFormula_XML";
      }
      return nombreTabla;
   }

   /**
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      if (historiaFormulaSeleccionada != null) {
         nombreTabla = ":formExportarHistoria:datosHistoriaFormulasExportar";
         nombreExportar = "HistoriaFormula_PDF";
         exportPDF_Tabla();
      }
      if (indexNodoSeleecionado >= 0) {
         nombreTabla = ":formExportarNodos:datosNodosExportar";
         nombreExportar = "GenerarFormula_PDF";
         exportPDF_Tabla();
      }
      if (indexEstructuraFormula >= 0) {
         nombreTabla = ":formExportarEstructura:datosEstructuraExportar";
         nombreExportar = "EstructuraFormula_PDF";
         exportPDF_Tabla();
      }
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
      if (historiaFormulaSeleccionada != null) {
         nombreTabla = ":formExportarHistoria:datosHistoriaFormulasExportar";
         nombreExportar = "HistoriaFormula_XLS";
         exportXLS_Tabla();
      }
      if (indexNodoSeleecionado >= 0) {
         nombreTabla = ":formExportarNodos:datosNodosExportar";
         nombreExportar = "GenerarFormula_XLS";
         exportXLS_Tabla();
      }
      if (indexEstructuraFormula >= 0) {
         nombreTabla = ":formExportarEstructura:datosEstructuraExportar";
         nombreExportar = "EstructuraFormula_PDF";
         exportXLS_Tabla();
      }
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
      historiaFormulaSeleccionada = null;
      contarRegistros();
   }

   public void verificarRastroTabla() {
      if (indexNodoSeleecionado >= 0) {
         verificarRastroNodos();
      } else if (indexEstructuraFormula >= 0) {
         RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
      } else if (historiaFormulaSeleccionada != null) {
         verificarRastroHistoriaFormula();
      } else {
         RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
      }
   }

   public void verificarRastroHistoriaFormula() {
      if (historiaFormulaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(historiaFormulaSeleccionada.getSecuencia(), "HISTORIASFORMULAS");
         backUpSecRegistroHistoriaFormula = historiaFormulaSeleccionada.getSecuencia();
         backUp = historiaFormulaSeleccionada.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "Historiasformulas";
            msnConfirmarRastro = "La tabla HISTORIASFORMULAS tiene rastros para el registro seleccionado, ¿desea continuar?";
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

   public void verificarRastroHistoriaFormulaHist() {
      if (administrarRastros.verificarHistoricosTabla("HISTORIASFORMULAS")) {
         nombreTablaRastro = "Historiasformulas";
         msnConfirmarRastroHistorico = "La tabla HISTORIASFORMULAS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroNodos() {
      if (secuenciaRegistroNodos != null) {
         int resultado = administrarRastros.obtenerTabla(secuenciaRegistroNodos, "NODOS");
         backUpSecuenciaRegistroNodo = secuenciaRegistroNodos;
         backUp = secuenciaRegistroNodos;
         secuenciaRegistroNodos = null;
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "Nodos";
            msnConfirmarRastro = "La tabla NODOS tiene rastros para el registro seleccionado, ¿desea continuar?";
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

   public void verificarRastroNodosHist() {
      if (administrarRastros.verificarHistoricosTabla("NODOS")) {
         nombreTablaRastro = "Nodos";
         msnConfirmarRastroHistorico = "La tabla NODOS tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   public void limpiarIformacionNodo() {
      nodo1 = " ";
      nodo1RO = true;
      nodo2 = " ";
      nodo2RO = true;
      nodo3 = " ";
      nodo3RO = true;
      nodo4 = " ";
      nodo4RO = true;
      nodo5 = " ";
      nodo5RO = true;
      nodo6 = " ";
      nodo6RO = true;
      nodo7 = " ";
      nodo7RO = true;
      nodo8 = " ";
      nodo8RO = true;
      nodo9 = " ";
      nodo9RO = true;
      nodo10 = " ";
      nodo10RO = true;
      nodo11 = " ";
      nodo11RO = true;
      nodo12 = " ";
      nodo12RO = true;
      nodo13 = " ";
      nodo13RO = true;
      nodo14 = " ";
      nodo14RO = true;
      nodo15 = " ";
      nodo15RO = true;
      nodo16 = " ";
      nodo16RO = true;

      nodo1_1 = " ";
      nodo1_1RO = true;
      nodo2_1 = " ";
      nodo2_1RO = true;
      nodo3_1 = " ";
      nodo3_1RO = true;
      nodo4_1 = " ";
      nodo4_1RO = true;
      nodo5_1 = " ";
      nodo5_1RO = true;
      nodo6_1 = " ";
      nodo6_1RO = true;
      nodo7_1 = " ";
      nodo7_1RO = true;
      nodo8_1 = " ";
      nodo8_1RO = true;
      nodo9_1 = " ";
      nodo9_1RO = true;
      nodo10_1 = " ";
      nodo10_1RO = true;
      nodo11_1 = " ";
      nodo11_1RO = true;
      nodo12_1 = " ";
      nodo12_1RO = true;
      nodo13_1 = " ";
      nodo13_1RO = true;
      nodo14_1 = " ";
      nodo14_1RO = true;
      nodo15_1 = " ";
      nodo15_1RO = true;
      nodo16_1 = " ";
      nodo16_1RO = true;
   }

   public void posicionNodo(int i) {
      if (cambiosHistoriaFormula == false) {
         indexNodoSeleecionado = i;
         secuenciaRegistroNodos = listNodosHistoriaFormula.get(indexNodoSeleecionado).getSecuencia();
//         historiaFormulaSeleccionada = null;
         indexEstructuraFormula = -1;
         if (listNodosHistoriaFormula.get(indexNodoSeleecionado).getOperador().getSecuencia() != null) {
            auxNodoSeleccionado = listNodosHistoriaFormula.get(indexNodoSeleecionado).getOperador().getSigno();
         }
         if (listNodosHistoriaFormula.get(indexNodoSeleecionado).getOperando().getSecuencia() != null) {
            auxNodoSeleccionado = listNodosHistoriaFormula.get(indexNodoSeleecionado).getOperando().getNombre();
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void cargarDatosParaNodos() {
      getListNodosHistoriaFormula();
      limpiarIformacionNodo();
      if (listNodosHistoriaFormula != null) {
         if (!listNodosHistoriaFormula.isEmpty()) {

            int aux = 0;
            while (aux < listNodosHistoriaFormula.size()) {
               if (aux == 0) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo1");
               }
               if (aux == 1) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo2 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo2 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo2RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo2");
               }
               if (aux == 2) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo3 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo3 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo3RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo3");
               }
               if (aux == 3) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo4 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo4 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo4RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo4");
               }
               if (aux == 4) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo5 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo5 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo5RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo5");
               }
               if (aux == 5) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo6 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo6 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo6RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo6");
               }
               if (aux == 6) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo7 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo7 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo7RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo7");
               }
               if (aux == 7) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo8 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo8 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo8RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo8");
               }
               if (aux == 8) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo9 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo9 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo9RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo9");
               }
               if (aux == 9) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo10 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo10 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo10RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo10");
               }
               if (aux == 10) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo11 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo11 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo11RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo11");
               }
               if (aux == 11) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo12 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo12 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo12RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo12");
               }
               if (aux == 12) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo13 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo13 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo13RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo13");
               }
               if (aux == 13) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo14 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo14 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo14RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo14");
               }
               if (aux == 14) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo15 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo15 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo15RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo15");
               }
               if (aux == 15) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo16 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo16 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo16RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo16");
               }
               //pagina secundaria//
               if (aux == 16) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo1_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo1_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo1_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo1_1");
               }
               if (aux == 17) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo2_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo2_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo2_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo2_1");
               }
               if (aux == 18) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo3_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo3_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo3_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo3_1");
               }
               if (aux == 19) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo4_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo4_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo4_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo4_1");
               }
               if (aux == 20) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo5_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo5_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo5_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo5_1");
               }
               if (aux == 21) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo6_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo6_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo6_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo6_1");
               }
               if (aux == 22) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo7_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo7_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo7_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo7_1");
               }
               if (aux == 23) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo8_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo8_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo8_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo8_1");
               }
               if (aux == 24) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo9_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo9_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo9_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo9_1");
               }
               if (aux == 25) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo10_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo10_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo10_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo10_1");
               }
               if (aux == 26) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo11_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo11_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo11_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo11_1");
               }
               if (aux == 27) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo12_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo12_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo12_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo12_1");
               }
               if (aux == 28) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo13_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo13_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo13_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo13_1");
               }
               if (aux == 29) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo14_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo14_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo14_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo14_1");
               }
               if (aux == 30) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo15_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo15_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo15_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo15_1");
               }
               if (aux == 31) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo16_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo16_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo16_1RO = false;
                  RequestContext.getCurrentInstance().update("form:nodo16_1");
               }
               aux++;
            }
         }
      }
      getFormulaCompleta();
      RequestContext.getCurrentInstance().update("form:formulaComplete");
      RequestContext.getCurrentInstance().update("form:panelNodosPrincipal");
      RequestContext.getCurrentInstance().update("form:panelNodosSecundario");
   }

   public void cargarDatosParaNodos2() {
      limpiarIformacionNodo();
      if (listNodosHistoriaFormula != null) {
         if (!listNodosHistoriaFormula.isEmpty()) {

            int aux = 0;
            while (aux < listNodosHistoriaFormula.size()) {
               if (aux == 0) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo1RO = false;
               }
               if (aux == 1) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo2 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo2 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo2RO = false;
               }
               if (aux == 2) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo3 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo3 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo3RO = false;
               }
               if (aux == 3) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo4 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo4 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo4RO = false;
               }
               if (aux == 4) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo5 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo5 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo5RO = false;
               }
               if (aux == 5) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo6 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo6 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo6RO = false;
               }
               if (aux == 6) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo7 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo7 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo7RO = false;
               }
               if (aux == 7) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo8 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo8 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo8RO = false;
               }
               if (aux == 8) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo9 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo9 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo9RO = false;
               }
               if (aux == 9) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo10 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo10 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo10RO = false;
               }
               if (aux == 10) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo11 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo11 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo11RO = false;
               }
               if (aux == 11) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo12 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo12 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo12RO = false;
               }
               if (aux == 12) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo13 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo13 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo13RO = false;
               }
               if (aux == 13) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo14 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo14 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo14RO = false;
               }
               if (aux == 14) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo15 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo15 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo15RO = false;
               }
               if (aux == 15) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo16 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo16 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo16RO = false;
               }
               if (aux == 16) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo1_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo1_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo1_1RO = false;
               }
               if (aux == 17) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo2_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo2_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo2_1RO = false;
               }
               if (aux == 18) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo3_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo3_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo3_1RO = false;
               }
               if (aux == 19) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo4_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo4_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo4_1RO = false;
               }
               if (aux == 20) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo5_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo5_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo5_1RO = false;
               }
               if (aux == 21) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo6_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo6_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo6_1RO = false;
               }
               if (aux == 22) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo7_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo7_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo7_1RO = false;
               }
               if (aux == 23) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo8_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo8_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo8_1RO = false;
               }
               if (aux == 24) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo9_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo9_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo9_1RO = false;
               }
               if (aux == 25) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo10_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo10_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo10_1RO = false;
               }
               if (aux == 26) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo11_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo11_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo11_1RO = false;
               }
               if (aux == 27) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo12_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo12_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo12_1RO = false;
               }
               if (aux == 28) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo13_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo13_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo13_1RO = false;
               }
               if (aux == 29) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo14_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo14_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo14_1RO = false;
               }
               if (aux == 30) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo15_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo15_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo15_1RO = false;
               }
               if (aux == 31) {
                  if (listNodosHistoriaFormula.get(aux).getOperador().getSecuencia() != null) {
                     nodo16_1 = listNodosHistoriaFormula.get(aux).getOperador().getSigno();
                  } else if (listNodosHistoriaFormula.get(aux).getOperando().getSecuencia() != null) {
                     nodo16_1 = listNodosHistoriaFormula.get(aux).getOperando().getNombre();
                  }
                  nodo16_1RO = false;
               }
               aux++;
            }
         }
      }
      getFormulaCompleta();
   }

   public void dispararDialogoNodoNuevo() {
      if (cambiosHistoriaFormula == false) {
         if (historiaFormulaSeleccionada != null) {
            actualizacionNodo = 1;
            RequestContext.getCurrentInstance().execute("PF('SeleccionRegGenerarFormula').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void dispararDialogoNodo() {
      if (indexNodoSeleecionado >= 0) {
         actualizacionNodo = 0;
         RequestContext.getCurrentInstance().execute("PF('SeleccionRegGenerarFormula').show()");
      }
   }

   public void dialogoNuevoOperando() {
      RequestContext.getCurrentInstance().update("formularioDialogos:OperandoDialogo");
      RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').show()");
   }

   public void dialogoNuevoOperandor() {
      RequestContext.getCurrentInstance().update("formularioDialogos:OperadorDialogo");
      RequestContext.getCurrentInstance().execute("PF('OperadorDialogo').show()");
   }

   public void actualizarOperando() {
      if (actualizacionNodo == 0) {
         modificacionesCambiosNodos(indexNodoSeleecionado, 0);
         listNodosParaExportar = null;
         cargarDatosParaNodos();
      }
      if (actualizacionNodo == 1) {
         k++;
         BigInteger var = BigInteger.valueOf(k);
         Nodos nuevo = new Nodos(var);
         nuevo.setOperador(new Operadores());
         nuevo.setOperando(new Operandos());
         nuevo.setHistoriaformula(historiaFormulaSeleccionada);
         short posicion = (short) (listNodosHistoriaFormula.size() + 1);
         nuevo.setPosicion(posicion);
         nuevo.setOperando(operandoSeleccionado);
         short aux = (short) (listNodosHistoriaFormula.size() + 1);
         nuevo.setPosicion(aux);
         listNodosHistoriaFormula.add(nuevo);
         listNodosCrear.add(nuevo);
         listNodosParaExportar = null;
         cargarDatosParaNodos();
         cambiosNodos = true;
         int tam = listNodosHistoriaFormula.size();
         if (tam > 16) {
            panelNodosSecundario = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosSecundario");
            panelNodosSecundario.setStyle("position: absolute; left: 3px; top: 15px; width: 835px; height: 83px; border: none; visibility: visible");

            panelNodosPrincipal = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosPrincipal");
            panelNodosPrincipal.setStyle("position: absolute; left: 3px; top: 15px; width:825px; height:83px;border: none;visibility: hidden");

            visibilidadBtnS = true;
            visibilidadBtnP = false;

            RequestContext.getCurrentInstance().update("form:panelNodosSecundario");
            RequestContext.getCurrentInstance().update("form:panelNodosPrincipal");
         }
         if (tam <= 16) {
            panelNodosSecundario = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosSecundario");
            panelNodosSecundario.setStyle("position: absolute; left: 3px; top: 15px; width: 835px; height: 83px; border: none; visibility: hidden");

            panelNodosPrincipal = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosPrincipal");
            panelNodosPrincipal.setStyle("position: absolute; left: 3px; top: 15px; width:825px; height:83px;border: none;visibility: visible");

            visibilidadBtnS = true;
            visibilidadBtnP = true;

            RequestContext.getCurrentInstance().update("form:panelNodosSecundario");
            RequestContext.getCurrentInstance().update("form:panelNodosPrincipal");
         }
      }

      filtrarListOperandos = null;
      operandoSeleccionado = new Operandos();
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      //RequestContext.getCurrentInstance().update("formularioDialogos:OperandoDialogo");
      context.reset("formularioDialogos:lovOperando:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOperando').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').hide()");
//      indexNodoSeleecionado = -1;
   }

   public void cancelarCambioOperando() {
      filtrarListOperandos = null;
      operandoSeleccionado = new Operandos();
      RequestContext context = RequestContext.getCurrentInstance();
      aceptar = true;
//      indexNodoSeleecionado = -1;
      context.reset("formularioDialogos:lovOperando:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOperando').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').hide()");
   }

   public void actualizarOperador() {
      if (actualizacionNodo == 0) {
         modificacionesCambiosNodos(indexNodoSeleecionado, 1);
         listNodosParaExportar = null;
         cargarDatosParaNodos();
      }
      if (actualizacionNodo == 1) {
         k++;
         BigInteger var = BigInteger.valueOf(k);
         Nodos nuevo = new Nodos(var);
         nuevo.setOperador(new Operadores());
         nuevo.setOperando(new Operandos());
         short aux = (short) (listNodosHistoriaFormula.size() + 1);
         nuevo.setPosicion(aux);
         nuevo.setHistoriaformula(historiaFormulaSeleccionada);

         nuevo.setOperador(operadorSeleccionado);
         listNodosHistoriaFormula.add(nuevo);
         listNodosCrear.add(nuevo);
         listNodosParaExportar = null;
         cambiosNodos = true;
         cargarDatosParaNodos();
         if (listNodosHistoriaFormula.size() > 16) {
            panelNodosSecundario = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosSecundario");
            panelNodosSecundario.setStyle("position: absolute; left: 3px; top: 15px; width:825px; height:83px; border: none; visibility: visible;");

            panelNodosPrincipal = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosPrincipal");
            panelNodosPrincipal.setStyle("position: absolute; left: 3px; top: 15px; width:825px; height:83px; border: none; visibility: hidden;");

            visibilidadBtnS = true;
            visibilidadBtnP = false;

            RequestContext.getCurrentInstance().update("form:panelNodosSecundario");
            RequestContext.getCurrentInstance().update("form:panelNodosPrincipal");
         }
         if (listNodosHistoriaFormula.size() <= 16) {
            panelNodosSecundario = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosSecundario");
            panelNodosSecundario.setStyle("position: absolute; left: 3px; top: 15px; width: 825px; height: 83px; border: none; visibility: hidden;");

            panelNodosPrincipal = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosPrincipal");
            panelNodosPrincipal.setStyle("position: absolute; left: 3px; top: 15px; width: 825px; height: 83px; border: none; visibility: visible;");

            visibilidadBtnS = true;
            visibilidadBtnP = true;

            RequestContext.getCurrentInstance().update("form:panelNodosSecundario");
            RequestContext.getCurrentInstance().update("form:panelNodosPrincipal");
         }
      }

      filtrarListOperadores = null;
      operadorSeleccionado = new Operadores();
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:lovOperador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOperador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('OperadorDialogo').hide()");
//      indexNodoSeleecionado = -1;
   }

   public void cancelarCambioOperador() {
      filtrarListOperadores = null;
      operadorSeleccionado = new Operadores();
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:lovOperador:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOperador').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('OperadorDialogo').hide()");
      aceptar = true;
//      indexNodoSeleecionado = -1;
   }

   public void modificacionesCambiosNodos(int indice, int tipoCambio) {
      if (tipoCambio == 0) {
         listNodosHistoriaFormula.get(indice).setOperando(operandoSeleccionado);
      } else if (tipoCambio == 1) {
         listNodosHistoriaFormula.get(indice).setOperador(operadorSeleccionado);
      }
      if (!listNodosCrear.contains(listNodosHistoriaFormula.get(indice))) {
         if (listNodosModificar.isEmpty()) {
            listNodosModificar.add(listNodosHistoriaFormula.get(indice));
         } else if (!listNodosModificar.contains(listNodosHistoriaFormula.get(indice))) {
            listNodosModificar.add(listNodosHistoriaFormula.get(indice));
         }
         if (guardado == true) {
            guardado = false;
         }
      }
      cambiosNodos = true;
//      indexNodoSeleecionado = -1;
//      secuenciaRegistroNodos = null;
      listNodosParaExportar = null;
   }

   public void modificacionesAutoCompletarNodos(int indice, String valorConfirmar) {
      indexNodoSeleecionado = indice;
      int tipoCambio = -1;
      if (listNodosHistoriaFormula.get(indexNodoSeleecionado).getOperando().getSecuencia() != null) {
         tipoCambio = 0;
      } else if (listNodosHistoriaFormula.get(indexNodoSeleecionado).getOperador().getSecuencia() != null) {
         tipoCambio = 1;
      }
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (tipoCambio == 0) {
         listNodosHistoriaFormula.get(indexNodoSeleecionado).getOperando().setDescripcion(auxNodoSeleccionado);
         for (int i = 0; i < listOperandos.size(); i++) {
            if (listOperandos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            listNodosHistoriaFormula.get(indexNodoSeleecionado).setOperando(listOperandos.get(indiceUnicoElemento));
//            listOperandos.clear();
//            getListOperandos();
         } else {
            RequestContext.getCurrentInstance().update("form:OperandoDialogo");
            RequestContext.getCurrentInstance().execute("PF('OperandoDialogo').show()");
            actualizacionNodo = 0;
         }
      }
      if (tipoCambio == 1) {
         listNodosHistoriaFormula.get(indexNodoSeleecionado).getOperador().setSigno(auxNodoSeleccionado);
         for (int i = 0; i < listOperadores.size(); i++) {
            if (listOperadores.get(i).getSigno().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            listNodosHistoriaFormula.get(indexNodoSeleecionado).setOperador(listOperadores.get(indiceUnicoElemento));
//            listOperadores.clear();
//            getListOperadores();
         } else {
            RequestContext.getCurrentInstance().update("form:OperadorDialogo");
            RequestContext.getCurrentInstance().execute("PF('OperadorDialogo').show()");
            actualizacionNodo = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listNodosCrear.contains(listNodosHistoriaFormula.get(indexNodoSeleecionado))) {
            if (listNodosModificar.isEmpty()) {
               listNodosModificar.add(listNodosHistoriaFormula.get(indexNodoSeleecionado));
            } else if (!listNodosModificar.contains(listNodosHistoriaFormula.get(indexNodoSeleecionado))) {
               listNodosModificar.add(listNodosHistoriaFormula.get(indexNodoSeleecionado));
            }
            if (guardado == true) {
               guardado = false;
            }
         }
         cambiosNodos = true;
//         indexNodoSeleecionado = -1;
         secuenciaRegistroNodos = null;
      }
      listNodosParaExportar = null;
      cargarDatosParaNodos();
   }

   public void borrarNodo() {
      if (indexNodoSeleecionado >= 0) {
         if (!listNodosModificar.isEmpty() && listNodosModificar.contains(listNodosHistoriaFormula.get(indexNodoSeleecionado))) {
            listNodosModificar.remove(listNodosHistoriaFormula.get(indexNodoSeleecionado));
            listNodosBorrar.add(listNodosHistoriaFormula.get(indexNodoSeleecionado));
         } else if (!listNodosCrear.isEmpty() && listNodosCrear.contains(listNodosHistoriaFormula.get(indexNodoSeleecionado))) {
            listNodosCrear.remove(listNodosHistoriaFormula.get(indexNodoSeleecionado));
         } else {
            listNodosBorrar.add(listNodosHistoriaFormula.get(indexNodoSeleecionado));
         }
         listNodosHistoriaFormula.remove(indexNodoSeleecionado);
         indexNodoSeleecionado = -1;
         secuenciaRegistroNodos = null;
         cambiosNodos = true;
         if (guardado == true) {
            guardado = false;
         }
         listNodosParaExportar = null;
         cargarDatosParaNodos();
         int tam = listNodosHistoriaFormula.size();
         if (tam > 16) {
            panelNodosSecundario = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosSecundario");
            panelNodosSecundario.setStyle("position: absolute; left: 3px; top: 15px; width: 835px; height: 83px; border: none; visibility: visible");

            panelNodosPrincipal = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosPrincipal");
            panelNodosPrincipal.setStyle("position: absolute; left: 3px; top: 15px; width:825px; height:83px;border: none;visibility: hidden");

            visibilidadBtnS = true;
            visibilidadBtnP = false;

            RequestContext.getCurrentInstance().update("form:panelNodosSecundario");
            RequestContext.getCurrentInstance().update("form:panelNodosPrincipal");
         }
         if (tam <= 16) {
            panelNodosSecundario = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosSecundario");
            panelNodosSecundario.setStyle("position: absolute; left: 3px; top: 15px; width: 835px; height: 83px; border: none; visibility: hidden");

            panelNodosPrincipal = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosPrincipal");
            panelNodosPrincipal.setStyle("position: absolute; left: 3px; top: 15px; width:825px; height:83px;border: none;visibility: visible");

            visibilidadBtnS = true;
            visibilidadBtnP = true;

            RequestContext.getCurrentInstance().update("form:panelNodosSecundario");
            RequestContext.getCurrentInstance().update("form:panelNodosPrincipal");
         }
      }
   }

   public void cambiarVisibilidadPaneles(int i) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (i == 1) {
         panelNodosSecundario = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosSecundario");
         panelNodosSecundario.setStyle("position: absolute; left: 3px; top: 15px; width: 835px; height: 83px; border: none; visibility: hidden");

         panelNodosPrincipal = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosPrincipal");
         panelNodosPrincipal.setStyle("position: absolute; left: 3px; top: 15px; width:825px; height:83px;border: none;visibility: visible");

         visibilidadBtnS = false;
         visibilidadBtnP = true;
      }
      if (i == 2) {
         panelNodosSecundario = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosSecundario");
         panelNodosSecundario.setStyle("position: absolute; left: 3px; top: 15px; width: 835px; height: 83px; border: none; visibility: visible");

         panelNodosPrincipal = (ScrollPanel) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:panelNodosPrincipal");
         panelNodosPrincipal.setStyle("position: absolute; left: 3px; top: 15px; width:825px; height:83px;border: none;visibility: hidden");

         visibilidadBtnS = true;
         visibilidadBtnP = false;
      }
      RequestContext.getCurrentInstance().update("form:panelNodosSecundario");
      RequestContext.getCurrentInstance().update("form:panelNodosPrincipal");
   }

   public void cambiarIndiceEstructuraFormula(int indice, int celda) {
      if (cambiosHistoriaFormula == false && cambiosNodos == false) {
         indexEstructuraFormula = indice;
         celdaEstructuraFormula = celda;
         auxEF_Descripcion = listEstructurasFormulas.get(indexEstructuraFormula).getDescripcion();
         auxEF_Formula = listEstructurasFormulas.get(indexEstructuraFormula).getNombreNodo();
         auxEF_IdFormula = listEstructurasFormulas.get(indexEstructuraFormula).getFormula();
         auxEF_IdHijo = listEstructurasFormulas.get(indexEstructuraFormula).getFormulaHijo();
//         indexNodoSeleecionado = -1;
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void modificaEstructuraFormula(int indice) {
      listEstructurasFormulas.get(indice).setDescripcion(auxEF_Descripcion);
      listEstructurasFormulas.get(indice).setNombreNodo(auxEF_Formula);
      listEstructurasFormulas.get(indice).setFormula(auxEF_IdFormula);
      listEstructurasFormulas.get(indice).setFormulaHijo(auxEF_IdHijo);
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosEstructuraFormula");
      RequestContext.getCurrentInstance().execute("PF('errorModificacionEF').show()");
   }

   public void obtenerNivelAutomatico() {
      int nivel = listEstructurasFormulas.get(indexEstructuraFormula).getNivel();
      int aux = indexEstructuraFormula + 1;
      boolean encontroNivel = false;
      for (int i = aux; i < listEstructurasFormulas.size(); i++) {
         if (listEstructurasFormulas.get(i).getNivel() == nivel) {
            encontroNivel = true;
            indexEstructuraFormula = i;
            break;
         }
      }
      if (encontroNivel == false) {
         setSeleccionEstructuraF(listEstructurasFormulas.get(indexEstructuraFormula));
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosEstructuraFormula");

      }
      if (encontroNivel == true) {
         setSeleccionEstructuraF(listEstructurasFormulas.get(indexEstructuraFormula));
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosEstructuraFormula");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosOpdo() {
      RequestContext.getCurrentInstance().update("formularioDialogos:informacionRegistroOpdo");
   }

   public void contarRegistrosOpor() {
      RequestContext.getCurrentInstance().update("formularioDialogos:informacionRegistroOpor");
   }

   //GET - SET 
   public List<Historiasformulas> getListHistoriasFormulas() {
      try {
         if (listHistoriasFormulas == null) {
            listHistoriasFormulas = administrarHistoriaFormula.listHistoriasFormulasParaFormula(formulaActual.getSecuencia());
         }
         if (!listHistoriasFormulas.isEmpty()) {
            for (int i = 0; i < listHistoriasFormulas.size(); i++) {
               String aux = listHistoriasFormulas.get(i).getObservaciones().toUpperCase();
               listHistoriasFormulas.get(i).setObservaciones(aux);
            }
         }
         return listHistoriasFormulas;
      } catch (Exception e) {
         System.out.println("Error getListHistoriasFormulas " + e.toString());
         return null;
      }
   }

   public void setListHistoriasFormulas(List<Historiasformulas> t) {
      this.listHistoriasFormulas = t;
   }

   public List<Historiasformulas> getFiltrarListHistoriasFormulas() {
      return filtrarListHistoriasFormulas;
   }

   public void setFiltrarListHistoriasFormulas(List<Historiasformulas> filtrarListHistoriasFormulas) {
      this.filtrarListHistoriasFormulas = filtrarListHistoriasFormulas;
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

   public BigInteger getBackUpSecRegistroHistoriaFormula() {
      return backUpSecRegistroHistoriaFormula;
   }

   public void setBackUpSecRegistroHistoriaFormula(BigInteger b) {
      this.backUpSecRegistroHistoriaFormula = b;
   }

   public List<Historiasformulas> getListHistoriasFormulasModificar() {
      return listHistoriasFormulasModificar;
   }

   public void setListHistoriasFormulasModificar(List<Historiasformulas> setListHistoriasFormulasModificar) {
      this.listHistoriasFormulasModificar = setListHistoriasFormulasModificar;
   }

   public Historiasformulas getNuevaHistoriaFormula() {
      return nuevaHistoriaFormula;
   }

   public void setNuevaHistoriaFormula(Historiasformulas setNuevaHistoriaFormula) {
      this.nuevaHistoriaFormula = setNuevaHistoriaFormula;
   }

   public List<Historiasformulas> getListHistoriasFormulasCrear() {
      return listHistoriasFormulasCrear;
   }

   public void setListHistoriasFormulasCrear(List<Historiasformulas> setListHistoriasFormulasCrear) {
      this.listHistoriasFormulasCrear = setListHistoriasFormulasCrear;
   }

   public List<Historiasformulas> getListHistoriasFormulasBorrar() {
      return listHistoriasFormulasBorrar;
   }

   public void setListHistoriasFormulasBorrar(List<Historiasformulas> setListHistoriasFormulasBorrar) {
      this.listHistoriasFormulasBorrar = setListHistoriasFormulasBorrar;
   }

   public Historiasformulas getEditarHistoriaFormula() {
      return editarHistoriaFormula;
   }

   public void setEditarHistoriaFormula(Historiasformulas setEditarHistoriaFormula) {
      this.editarHistoriaFormula = setEditarHistoriaFormula;
   }

   public Historiasformulas getDuplicarHistoriaFormula() {
      return duplicarHistoriaFormula;
   }

   public void setDuplicarHistoriaFormula(Historiasformulas setDuplicarHistoriaFormula) {
      this.duplicarHistoriaFormula = setDuplicarHistoriaFormula;
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

   public Formulas getFormulaActual() {
      return formulaActual;
   }

   public void setFormulaActual(Formulas setFormulaActual) {
      this.formulaActual = setFormulaActual;
   }

   public List<Nodos> getListNodosHistoriaFormula() {
      if (listNodosHistoriaFormula == null && historiaFormulaSeleccionada != null) {
         listNodosHistoriaFormula = administrarHistoriaFormula.listNodosDeHistoriaFormula(historiaFormulaSeleccionada.getSecuencia());
      }
      if (listNodosHistoriaFormula != null) {
         if (!listNodosHistoriaFormula.isEmpty()) {
            System.out.println("Controlador.ControlHistoriaFormula.getListNodosHistoriaFormula() listNodosHistoriaFormula.size() : " + listNodosHistoriaFormula.size());
            if (listNodosHistoriaFormula.size() <= 16) {
               visibilidadBtnS = true;
               visibilidadBtnP = true;
            }
            if (listNodosHistoriaFormula.size() > 16) {
               visibilidadBtnS = false;
               visibilidadBtnP = true;
            }
            for (int i = 0; i < listNodosHistoriaFormula.size(); i++) {
               if (listNodosHistoriaFormula.get(i).getOperador() == null) {
                  listNodosHistoriaFormula.get(i).setOperador(new Operadores());
               }
               if (listNodosHistoriaFormula.get(i).getOperando() == null) {
                  listNodosHistoriaFormula.get(i).setOperando(new Operandos());
               }
            }
         }
      }
      return listNodosHistoriaFormula;
   }

   public void setListNodosHistoriaFormula(List<Nodos> listNodosHistoriaFormula) {
      this.listNodosHistoriaFormula = listNodosHistoriaFormula;
   }

   public String getFormulaCompleta() {
      formulaCompleta = "";
      if (listNodosHistoriaFormula != null) {
         if (!listNodosHistoriaFormula.isEmpty()) {
            for (int i = 0; i < listNodosHistoriaFormula.size(); i++) {
               if (listNodosHistoriaFormula.get(i).getOperador().getSecuencia() != null) {
                  formulaCompleta = formulaCompleta + listNodosHistoriaFormula.get(i).getOperador().getSigno() + " ";
               } else if (listNodosHistoriaFormula.get(i).getOperando().getSecuencia() != null) {
                  formulaCompleta = formulaCompleta + listNodosHistoriaFormula.get(i).getOperando().getNombre() + " ";
               }
            }
         }
      }
      return formulaCompleta;
   }

   public void setFormulaCompleta(String formulaCompleta) {
      this.formulaCompleta = formulaCompleta;
   }

   public String getNodo1() {
      return nodo1;
   }

   public void setNodo1(String nodo1) {
      this.nodo1 = nodo1;
   }

   public String getNodo2() {
      return nodo2;
   }

   public void setNodo2(String nodo2) {
      this.nodo2 = nodo2;
   }

   public String getNodo3() {
      return nodo3;
   }

   public void setNodo3(String nodo3) {
      this.nodo3 = nodo3;
   }

   public String getNodo4() {
      return nodo4;
   }

   public void setNodo4(String nodo4) {
      this.nodo4 = nodo4;
   }

   public String getNodo5() {
      return nodo5;
   }

   public void setNodo5(String nodo5) {
      this.nodo5 = nodo5;
   }

   public String getNodo6() {
      return nodo6;
   }

   public void setNodo6(String nodo6) {
      this.nodo6 = nodo6;
   }

   public String getNodo7() {
      return nodo7;
   }

   public void setNodo7(String nodo7) {
      this.nodo7 = nodo7;
   }

   public String getNodo8() {
      return nodo8;
   }

   public void setNodo8(String nodo8) {
      this.nodo8 = nodo8;
   }

   public String getNodo9() {
      return nodo9;
   }

   public void setNodo9(String nodo9) {
      this.nodo9 = nodo9;
   }

   public String getNodo10() {
      return nodo10;
   }

   public void setNodo10(String nodo10) {
      this.nodo10 = nodo10;
   }

   public String getNodo11() {
      return nodo11;
   }

   public void setNodo11(String nodo11) {
      this.nodo11 = nodo11;
   }

   public String getNodo12() {
      return nodo12;
   }

   public void setNodo12(String nodo12) {
      this.nodo12 = nodo12;
   }

   public String getNodo13() {
      return nodo13;
   }

   public void setNodo13(String nodo13) {
      this.nodo13 = nodo13;
   }

   public String getNodo14() {
      return nodo14;
   }

   public void setNodo14(String nodo14) {
      this.nodo14 = nodo14;
   }

   public String getNodo15() {
      return nodo15;
   }

   public void setNodo15(String nodo15) {
      this.nodo15 = nodo15;
   }

   public String getNodo16() {
      return nodo16;
   }

   public void setNodo16(String nodo16) {
      this.nodo16 = nodo16;
   }

   public boolean isNodo1RO() {
      return nodo1RO;
   }

   public void setNodo1RO(boolean nodo1RO) {
      this.nodo1RO = nodo1RO;
   }

   public boolean isNodo2RO() {
      return nodo2RO;
   }

   public void setNodo2RO(boolean nodo2RO) {
      this.nodo2RO = nodo2RO;
   }

   public boolean isNodo3RO() {
      return nodo3RO;
   }

   public void setNodo3RO(boolean nodo3RO) {
      this.nodo3RO = nodo3RO;
   }

   public boolean isNodo4RO() {
      return nodo4RO;
   }

   public void setNodo4RO(boolean nodo4RO) {
      this.nodo4RO = nodo4RO;
   }

   public boolean isNodo5RO() {
      return nodo5RO;
   }

   public void setNodo5RO(boolean nodo5RO) {
      this.nodo5RO = nodo5RO;
   }

   public boolean isNodo6RO() {
      return nodo6RO;
   }

   public void setNodo6RO(boolean nodo6RO) {
      this.nodo6RO = nodo6RO;
   }

   public boolean isNodo7RO() {
      return nodo7RO;
   }

   public void setNodo7RO(boolean nodo7RO) {
      this.nodo7RO = nodo7RO;
   }

   public boolean isNodo8RO() {
      return nodo8RO;
   }

   public void setNodo8RO(boolean nodo8RO) {
      this.nodo8RO = nodo8RO;
   }

   public boolean isNodo9RO() {
      return nodo9RO;
   }

   public void setNodo9RO(boolean nodo9RO) {
      this.nodo9RO = nodo9RO;
   }

   public boolean isNodo10RO() {
      return nodo10RO;
   }

   public void setNodo10RO(boolean nodo10RO) {
      this.nodo10RO = nodo10RO;
   }

   public boolean isNodo11RO() {
      return nodo11RO;
   }

   public void setNodo11RO(boolean nodo11RO) {
      this.nodo11RO = nodo11RO;
   }

   public boolean isNodo12RO() {
      return nodo12RO;
   }

   public void setNodo12RO(boolean nodo12RO) {
      this.nodo12RO = nodo12RO;
   }

   public boolean isNodo13RO() {
      return nodo13RO;
   }

   public void setNodo13RO(boolean nodo13RO) {
      this.nodo13RO = nodo13RO;
   }

   public boolean isNodo14RO() {
      return nodo14RO;
   }

   public void setNodo14RO(boolean nodo14RO) {
      this.nodo14RO = nodo14RO;
   }

   public boolean isNodo15RO() {
      return nodo15RO;
   }

   public void setNodo15RO(boolean nodo15RO) {
      this.nodo15RO = nodo15RO;
   }

   public boolean isNodo16RO() {
      return nodo16RO;
   }

   public void setNodo16RO(boolean nodo16RO) {
      this.nodo16RO = nodo16RO;
   }

   public String getNodo1_1() {
      return nodo1_1;
   }

   public void setNodo1_1(String nodo1_1) {
      this.nodo1_1 = nodo1_1;
   }

   public String getNodo2_1() {
      return nodo2_1;
   }

   public void setNodo2_1(String nodo2_1) {
      this.nodo2_1 = nodo2_1;
   }

   public String getNodo3_1() {
      return nodo3_1;
   }

   public void setNodo3_1(String nodo3_1) {
      this.nodo3_1 = nodo3_1;
   }

   public String getNodo4_1() {
      return nodo4_1;
   }

   public void setNodo4_1(String nodo4_1) {
      this.nodo4_1 = nodo4_1;
   }

   public String getNodo5_1() {
      return nodo5_1;
   }

   public void setNodo5_1(String nodo5_1) {
      this.nodo5_1 = nodo5_1;
   }

   public String getNodo6_1() {
      return nodo6_1;
   }

   public void setNodo6_1(String nodo6_1) {
      this.nodo6_1 = nodo6_1;
   }

   public String getNodo7_1() {
      return nodo7_1;
   }

   public void setNodo7_1(String nodo7_1) {
      this.nodo7_1 = nodo7_1;
   }

   public String getNodo8_1() {
      return nodo8_1;
   }

   public void setNodo8_1(String nodo8_1) {
      this.nodo8_1 = nodo8_1;
   }

   public String getNodo9_1() {
      return nodo9_1;
   }

   public void setNodo9_1(String nodo9_1) {
      this.nodo9_1 = nodo9_1;
   }

   public String getNodo10_1() {
      return nodo10_1;
   }

   public void setNodo10_1(String nodo10_1) {
      this.nodo10_1 = nodo10_1;
   }

   public String getNodo11_1() {
      return nodo11_1;
   }

   public void setNodo11_1(String nodo11_1) {
      this.nodo11_1 = nodo11_1;
   }

   public String getNodo12_1() {
      return nodo12_1;
   }

   public void setNodo12_1(String nodo12_1) {
      this.nodo12_1 = nodo12_1;
   }

   public String getNodo13_1() {
      return nodo13_1;
   }

   public void setNodo13_1(String nodo13_1) {
      this.nodo13_1 = nodo13_1;
   }

   public String getNodo14_1() {
      return nodo14_1;
   }

   public void setNodo14_1(String nodo14_1) {
      this.nodo14_1 = nodo14_1;
   }

   public String getNodo15_1() {
      return nodo15_1;
   }

   public void setNodo15_1(String nodo15_1) {
      this.nodo15_1 = nodo15_1;
   }

   public String getNodo16_1() {
      return nodo16_1;
   }

   public void setNodo16_1(String nodo16_1) {
      this.nodo16_1 = nodo16_1;
   }

   public boolean isNodo1_1RO() {
      return nodo1_1RO;
   }

   public void setNodo1_1RO(boolean nodo1_1RO) {
      this.nodo1_1RO = nodo1_1RO;
   }

   public boolean isNodo2_1RO() {
      return nodo2_1RO;
   }

   public void setNodo2_1RO(boolean nodo2_1RO) {
      this.nodo2_1RO = nodo2_1RO;
   }

   public boolean isNodo3_1RO() {
      return nodo3_1RO;
   }

   public void setNodo3_1RO(boolean nodo3_1RO) {
      this.nodo3_1RO = nodo3_1RO;
   }

   public boolean isNodo4_1RO() {
      return nodo4_1RO;
   }

   public void setNodo4_1RO(boolean nodo4_1RO) {
      this.nodo4_1RO = nodo4_1RO;
   }

   public boolean isNodo5_1RO() {
      return nodo5_1RO;
   }

   public void setNodo5_1RO(boolean nodo5_1RO) {
      this.nodo5_1RO = nodo5_1RO;
   }

   public boolean isNodo6_1RO() {
      return nodo6_1RO;
   }

   public void setNodo6_1RO(boolean nodo6_1RO) {
      this.nodo6_1RO = nodo6_1RO;
   }

   public boolean isNodo7_1RO() {
      return nodo7_1RO;
   }

   public void setNodo7_1RO(boolean nodo7_1RO) {
      this.nodo7_1RO = nodo7_1RO;
   }

   public boolean isNodo8_1RO() {
      return nodo8_1RO;
   }

   public void setNodo8_1RO(boolean nodo8_1RO) {
      this.nodo8_1RO = nodo8_1RO;
   }

   public boolean isNodo9_1RO() {
      return nodo9_1RO;
   }

   public void setNodo9_1RO(boolean nodo9_1RO) {
      this.nodo9_1RO = nodo9_1RO;
   }

   public boolean isNodo10_1RO() {
      return nodo10_1RO;
   }

   public void setNodo10_1RO(boolean nodo10_1RO) {
      this.nodo10_1RO = nodo10_1RO;
   }

   public boolean isNodo11_1RO() {
      return nodo11_1RO;
   }

   public void setNodo11_1RO(boolean nodo11_1RO) {
      this.nodo11_1RO = nodo11_1RO;
   }

   public boolean isNodo12_1RO() {
      return nodo12_1RO;
   }

   public void setNodo12_1RO(boolean nodo12_1RO) {
      this.nodo12_1RO = nodo12_1RO;
   }

   public boolean isNodo13_1RO() {
      return nodo13_1RO;
   }

   public void setNodo13_1RO(boolean nodo13_1RO) {
      this.nodo13_1RO = nodo13_1RO;
   }

   public boolean isNodo14_1RO() {
      return nodo14_1RO;
   }

   public void setNodo14_1RO(boolean nodo14_1RO) {
      this.nodo14_1RO = nodo14_1RO;
   }

   public boolean isNodo15_1RO() {
      return nodo15_1RO;
   }

   public void setNodo15_1RO(boolean nodo15_1RO) {
      this.nodo15_1RO = nodo15_1RO;
   }

   public boolean isNodo16_1RO() {
      return nodo16_1RO;
   }

   public void setNodo16_1RO(boolean nodo16_1RO) {
      this.nodo16_1RO = nodo16_1RO;
   }

   public Operandos getOperandoSeleccionado() {
      return operandoSeleccionado;
   }

   public void setOperandoSeleccionado(Operandos operandoSeleccionado) {
      this.operandoSeleccionado = operandoSeleccionado;
   }

   public Operadores getOperadorSeleccionado() {
      return operadorSeleccionado;
   }

   public void setOperadorSeleccionado(Operadores operadorSeleccionado) {
      this.operadorSeleccionado = operadorSeleccionado;
   }

   public List<Operadores> getListOperadores() {
      if (listOperadores == null) {
         listOperadores = administrarHistoriaFormula.listOperadores();
      }
      return listOperadores;
   }

   public void setListOperadores(List<Operadores> listOperadores) {
      this.listOperadores = listOperadores;
   }

   public List<Operandos> getListOperandos() {
      if (listOperandos == null) {
         listOperandos = administrarHistoriaFormula.listOperandos();
      }
      return listOperandos;
   }

   public void setListOperandos(List<Operandos> listOperandos) {
      this.listOperandos = listOperandos;
   }

   public List<Operadores> getFiltrarListOperadores() {
      return filtrarListOperadores;
   }

   public void setFiltrarListOperadores(List<Operadores> filtrarListOperadores) {
      this.filtrarListOperadores = filtrarListOperadores;
   }

   public List<Operandos> getFiltrarListOperandos() {
      return filtrarListOperandos;
   }

   public void setFiltrarListOperandos(List<Operandos> filtrarListOperandos) {
      this.filtrarListOperandos = filtrarListOperandos;
   }

   public List<Nodos> getListNodosCrear() {
      return listNodosCrear;
   }

   public void setListNodosCrear(List<Nodos> listNodosCrear) {
      this.listNodosCrear = listNodosCrear;
   }

   public List<Nodos> getListNodosBorrar() {
      return listNodosBorrar;
   }

   public void setListNodosBorrar(List<Nodos> listNodosBorrar) {
      this.listNodosBorrar = listNodosBorrar;
   }

   public List<Nodos> getListNodosModificar() {
      return listNodosModificar;
   }

   public void setListNodosModificar(List<Nodos> listNodosModificar) {
      this.listNodosModificar = listNodosModificar;
   }

   public List<Nodos> getListNodosParaExportar() {
      if (listNodosParaExportar == null) {
         listNodosParaExportar = new ArrayList<Nodos>();
         if (listNodosHistoriaFormula != null) {
            if (!listNodosHistoriaFormula.isEmpty()) {
               for (int i = 0; i < listNodosHistoriaFormula.size(); i++) {
                  if (listNodosHistoriaFormula.get(i).getOperador().getSecuencia() != null) {
                     String aux = listNodosHistoriaFormula.get(i).getOperador().getSigno();
                     listNodosHistoriaFormula.get(i).setDescripcionNodo(aux);
                     listNodosParaExportar.add(listNodosHistoriaFormula.get(i));
                  }
                  if (listNodosHistoriaFormula.get(i).getOperando().getSecuencia() != null) {
                     String aux = listNodosHistoriaFormula.get(i).getOperando().getNombre();
                     listNodosHistoriaFormula.get(i).setDescripcionNodo(aux);
                     listNodosParaExportar.add(listNodosHistoriaFormula.get(i));
                  }
               }
            }
         }
      }
      return listNodosParaExportar;
   }

   public void setListNodosParaExportar(List<Nodos> listNodosParaExportar) {
      this.listNodosParaExportar = listNodosParaExportar;
   }

   public Nodos getEditarNodo() {
      return editarNodo;
   }

   public void setEditarNodo(Nodos editarNodo) {
      this.editarNodo = editarNodo;
   }

   public BigInteger getSecuenciaRegistroNodos() {
      return secuenciaRegistroNodos;
   }

   public void setSecuenciaRegistroNodos(BigInteger secuenciaRegistroNodos) {
      this.secuenciaRegistroNodos = secuenciaRegistroNodos;
   }

   public BigInteger getBackUpSecuenciaRegistroNodo() {
      return backUpSecuenciaRegistroNodo;
   }

   public void setBackUpSecuenciaRegistroNodo(BigInteger backUpSecuenciaRegistroNodo) {
      this.backUpSecuenciaRegistroNodo = backUpSecuenciaRegistroNodo;
   }

   public List<EstructurasFormulas> getListEstructurasFormulas() {
      if (listEstructurasFormulas == null && historiaFormulaSeleccionada != null) {
         listEstructurasFormulas = administrarHistoriaFormula.listEstructurasFormulasParaHistoriaFormula(historiaFormulaSeleccionada.getSecuencia());
      }
      if (listEstructurasFormulas != null) {
         if (!listEstructurasFormulas.isEmpty()) {
            for (int i = 0; i < listEstructurasFormulas.size(); i++) {
               if (listEstructurasFormulas.get(i).getDescripcion() != null) {
                  String aux = listEstructurasFormulas.get(i).getDescripcion().toUpperCase();
                  listEstructurasFormulas.get(i).setDescripcion(aux);
               }
            }
         }
      }
      return listEstructurasFormulas;
   }

   public void setListEstructurasFormulas(List<EstructurasFormulas> listEstructurasFormulas) {
      this.listEstructurasFormulas = listEstructurasFormulas;
   }

   public List<EstructurasFormulas> getFiltrarListEstructurasFormulas() {
      return filtrarListEstructurasFormulas;
   }

   public void setFiltrarListEstructurasFormulas(List<EstructurasFormulas> filtrarListEstructurasFormulas) {
      this.filtrarListEstructurasFormulas = filtrarListEstructurasFormulas;
   }

   public EstructurasFormulas getSeleccionEstructuraF() {
      return seleccionEstructuraF;
   }

   public void setSeleccionEstructuraF(EstructurasFormulas seleccionEstructuraF) {
      this.seleccionEstructuraF = seleccionEstructuraF;
   }

   public EstructurasFormulas getEditarEstructura() {
      return editarEstructura;
   }

   public void setEditarEstructura(EstructurasFormulas editarEstructura) {
      this.editarEstructura = editarEstructura;
   }

   public boolean isVisibilidadBtnP() {
      return visibilidadBtnP;
   }

   public void setVisibilidadBtnP(boolean visibilidadBtnP) {
      this.visibilidadBtnP = visibilidadBtnP;
   }

   public boolean isVisibilidadBtnS() {
      return visibilidadBtnS;
   }

   public void setVisibilidadBtnS(boolean visibilidadBtnS) {
      this.visibilidadBtnS = visibilidadBtnS;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosHistoriaFormula");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroOperador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovOperador");
      infoRegistroOperador = String.valueOf(tabla.getRowCount());
      return infoRegistroOperador;
   }

   public void setInfoRegistroOperador(String infoRegistroOperador) {
      this.infoRegistroOperador = infoRegistroOperador;
   }

   public String getInfoRegistroOperando() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovOperando");
      infoRegistroOperando = String.valueOf(tabla.getRowCount());
      return infoRegistroOperando;
   }

   public void setInfoRegistroOperando(String infoRegistroOperando) {
      this.infoRegistroOperando = infoRegistroOperando;
   }

   public Historiasformulas getHistoriaFormulaSeleccionada() {
      return historiaFormulaSeleccionada;
   }

   public void setHistoriaFormulaSeleccionada(Historiasformulas historiaFormulaSeleccionada) {
      this.historiaFormulaSeleccionada = historiaFormulaSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

}
