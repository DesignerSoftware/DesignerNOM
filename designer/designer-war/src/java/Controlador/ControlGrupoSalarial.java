/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.GruposSalariales;
import Entidades.VigenciasGruposSalariales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGrupoSalarialInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlGrupoSalarial implements Serializable {

   private static Logger log = Logger.getLogger(ControlGrupoSalarial.class);

   @EJB
   AdministrarGrupoSalarialInterface administrarGrupoSalarial;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<GruposSalariales> listGruposSalariales;
   private List<GruposSalariales> filtrarListGruposSalariales;
   private GruposSalariales grupoSalarialTablaSeleccionada;
   ///////
   private List<VigenciasGruposSalariales> listVigenciasGruposSalariales;
   private List<VigenciasGruposSalariales> filtrarListVigenciasGruposSalariales;
   private VigenciasGruposSalariales vigenciaTablaSeleccionada;
   //Activo/Desactivo Crtl + F11
   private int bandera, banderaVGS;
   //Columnas Tabla 
   private Column gsCodigo, gsDescripcion, gsSalario, vgsFechaVigencia, vgsValor;
   //Otros
   private boolean aceptar;
   //modificar
   private List<GruposSalariales> listGrupoSalarialModificar;
   private List<VigenciasGruposSalariales> listVigenciaGrupoSalarialModificar;
   private boolean guardado, guardadoVGS;
   private boolean cambiosPagina;
   //crear 
   private GruposSalariales nuevoGrupoSalarial;
   private VigenciasGruposSalariales nuevoVigenciaGrupoSalarial;
   private List<GruposSalariales> listGrupoSalarialCrear;
   private List<VigenciasGruposSalariales> listVigenciaGrupoSalarialCrear;
   private BigInteger l;
   private int k;
   //borrar 
   private List<GruposSalariales> listGrupoSalarialBorrar;
   private List<VigenciasGruposSalariales> listVigenciaGrupoSalarialBorrar;
   //editar celda
   private GruposSalariales editarGrupoSalarial;
   private VigenciasGruposSalariales editarVigenciaGrupoSalarial;
   private int cualCelda, tipoLista, cualCeldaVigencia, tipoListaVigencia;
   //duplicar
   private GruposSalariales duplicarGrupoSalarial;
   private VigenciasGruposSalariales duplicarVigenciaGrupoSalarial;
   private BigInteger backUpSecRegistro, backUpSecRegistroVigencia;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private String nombreXML, nombreTabla;
   private BigInteger valorAux;
   private Date fechaVigencia;
   //
   private String mensajeValidacion, mensajeValidacion2;
   //
   private String altoTablaGrupo, altoTablaVigencia;
   //
   private String auxDescripcionGrupo;
   private int cualTabla;
   private String infoRegistroGrupo, infoRegistroVigencia;
   private boolean activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlGrupoSalarial() {
      cambiosPagina = true;
      altoTablaGrupo = "140";
      altoTablaVigencia = "100";
      backUpSecRegistro = null;
      listGruposSalariales = null;
      //Otros
      aceptar = true;
      //borrar aficiones
      listGrupoSalarialBorrar = new ArrayList<GruposSalariales>();
      listVigenciaGrupoSalarialModificar = new ArrayList<VigenciasGruposSalariales>();
      listVigenciaGrupoSalarialBorrar = new ArrayList<VigenciasGruposSalariales>();
      //crear aficiones
      listGrupoSalarialCrear = new ArrayList<GruposSalariales>();
      listVigenciaGrupoSalarialCrear = new ArrayList<VigenciasGruposSalariales>();
      k = 0;
      //modificar aficiones
      listGrupoSalarialModificar = new ArrayList<GruposSalariales>();
      //editar
      editarGrupoSalarial = new GruposSalariales();
      editarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
      editarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
      cualCelda = -1;
      cualCeldaVigencia = -1;
      tipoListaVigencia = 0;
      tipoLista = 0;
      //guardar
      guardado = true;
      guardadoVGS = true;
      //Crear VC
      nuevoGrupoSalarial = new GruposSalariales();
      nuevoVigenciaGrupoSalarial = new VigenciasGruposSalariales();
      duplicarGrupoSalarial = new GruposSalariales();
      duplicarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
      grupoSalarialTablaSeleccionada = null;
      vigenciaTablaSeleccionada = null;
      backUpSecRegistroVigencia = null;
      nombreTabla = ":formExportarG:datosGruposSalarialesExportar";
      nombreXML = "GruposSalarialesXML";
      activarLov = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String page) {
      paginaAnterior = page;
      listGruposSalariales = null;
      getListGruposSalariales();
      if (listGruposSalariales != null) {
         grupoSalarialTablaSeleccionada = listGruposSalariales.get(0);
      }
      listVigenciasGruposSalariales = null;
      getListVigenciasGruposSalariales();
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listGruposSalariales = null;
      getListGruposSalariales();
      if (listGruposSalariales != null) {
         grupoSalarialTablaSeleccionada = listGruposSalariales.get(0);
      }

      listVigenciasGruposSalariales = null;
      getListVigenciasGruposSalariales();
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "gruposalarial";
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
         administrarGrupoSalarial.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public String redirigir() {
      return paginaAnterior;
   }

   public void modificarGrupoSalarial(GruposSalariales grupoSalarial) {
      grupoSalarialTablaSeleccionada = grupoSalarial;
      boolean existeDescripcion = true;
      if (grupoSalarialTablaSeleccionada.getDescripcion() == null) {
         existeDescripcion = false;
      } else if (grupoSalarialTablaSeleccionada.getDescripcion().isEmpty()) {
         existeDescripcion = false;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (existeDescripcion == true) {
         if (tipoLista == 0) {
            if (!listGrupoSalarialCrear.contains(grupoSalarialTablaSeleccionada)) {
               if (listGrupoSalarialModificar.isEmpty()) {
                  listGrupoSalarialModificar.add(grupoSalarialTablaSeleccionada);
               } else if (!listGrupoSalarialModificar.contains(grupoSalarialTablaSeleccionada)) {
                  listGrupoSalarialModificar.add(grupoSalarialTablaSeleccionada);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         grupoSalarialTablaSeleccionada.setDescripcion(auxDescripcionGrupo);
         RequestContext.getCurrentInstance().execute("PF('errorRegDescripcion').show()");
      }
      RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");

   }

   public void modificarVigenciaGrupoSalarial(VigenciasGruposSalariales vigenciaGrupoS) {
      boolean validarDatosNull = true;
      if (vigenciaTablaSeleccionada.getFechavigencia() == null || vigenciaTablaSeleccionada.getValor() == null) {
         validarDatosNull = false;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (validarDatosNull == true) {
         vigenciaTablaSeleccionada.setFechavigencia(vigenciaTablaSeleccionada.getFechavigencia());
         vigenciaTablaSeleccionada.setValor(vigenciaTablaSeleccionada.getValor());
         if (!listVigenciaGrupoSalarialCrear.contains(vigenciaTablaSeleccionada)) {
            if (listVigenciaGrupoSalarialModificar.isEmpty()) {
               listVigenciaGrupoSalarialModificar.add(vigenciaTablaSeleccionada);
            } else if (!listVigenciaGrupoSalarialModificar.contains(vigenciaTablaSeleccionada)) {
               listVigenciaGrupoSalarialModificar.add(vigenciaTablaSeleccionada);
            }
            if (guardadoVGS == true) {
               guardadoVGS = false;
            }
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         vigenciaTablaSeleccionada.setFechavigencia(fechaVigencia);
         vigenciaTablaSeleccionada.setValor(valorAux);
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
   }

   public void modificarFechas(VigenciasGruposSalariales vigenciaGS, int c) {
      VigenciasGruposSalariales auxiliar = null;
      auxiliar = vigenciaTablaSeleccionada;
      if (auxiliar.getFechavigencia() != null) {
         cambiarIndiceVigencia(auxiliar, c);
         modificarVigenciaGrupoSalarial(auxiliar);
      } else {
         vigenciaTablaSeleccionada.setFechavigencia(fechaVigencia);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void cambiarIndice(GruposSalariales grupoS, int celda) {
      cualTabla = 1;
      grupoSalarialTablaSeleccionada = grupoS;
      grupoSalarialTablaSeleccionada.getSecuencia();
      auxDescripcionGrupo = grupoSalarialTablaSeleccionada.getDescripcion();
      cualCelda = celda;
      if (cualCelda == 0) {
         grupoSalarialTablaSeleccionada.getCodigo();
      } else if (cualCelda == 2) {
         grupoSalarialTablaSeleccionada.getDescripcion();
      } else if (cualCelda == 3) {
         grupoSalarialTablaSeleccionada.getSalario();
      }
      listVigenciasGruposSalariales = null;
      listVigenciasGruposSalariales = administrarGrupoSalarial.lisVigenciasGruposSalarialesSecuencia(grupoSalarialTablaSeleccionada.getSecuencia());
      contarRegistrosVigencia();
      RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
   }

   public void cambiarIndiceVigencia(VigenciasGruposSalariales vigenciaGS, int celda) {
      cualTabla = 2;
      vigenciaTablaSeleccionada = vigenciaGS;
      cualCeldaVigencia = celda;
      vigenciaTablaSeleccionada.getSecuencia();
      fechaVigencia = vigenciaTablaSeleccionada.getFechavigencia();
      valorAux = vigenciaTablaSeleccionada.getValor();
      if (banderaVGS == 1) {
         vgsValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
         vgsValor.setFilterStyle("display: none; visibility: hidden;");
         vgsFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
         vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
         banderaVGS = 0;
         filtrarListVigenciasGruposSalariales = null;
         tipoListaVigencia = 0;
      }
   }

   public void guardarYSalir() {
      guardarGeneral();
      salir();
   }

   public void guardarGeneral() {
      if (cambiosPagina == false) {
         guardarCambiosGrupoSalarial();
         guardarCambiosVigenciaGrupoSalarial();
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void guardarCambiosGrupoSalarial() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listGrupoSalarialBorrar.isEmpty()) {
               for (int i = 0; i < listGrupoSalarialBorrar.size(); i++) {
                  administrarGrupoSalarial.borrarGruposSalariales(listGrupoSalarialBorrar);
               }
               listGrupoSalarialBorrar.clear();
            }
            if (!listGrupoSalarialCrear.isEmpty()) {
               for (int i = 0; i < listGrupoSalarialCrear.size(); i++) {
                  administrarGrupoSalarial.crearGruposSalariales(listGrupoSalarialCrear);
               }
               listGrupoSalarialCrear.clear();
            }
            if (!listGrupoSalarialModificar.isEmpty()) {
               administrarGrupoSalarial.editarGruposSalariales(listGrupoSalarialModificar);
               listGrupoSalarialModificar.clear();
            }
            listGruposSalariales = null;
            RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
            guardado = true;
            k = 0;
            grupoSalarialTablaSeleccionada = null;
            contarRegistrosGrupo();
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Grupo Salarial con Éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         log.warn("Error guardarCambiosGrupoSalarial : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Grupo Salarial, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosVigenciaGrupoSalarial() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardadoVGS == false) {
            if (!listVigenciaGrupoSalarialBorrar.isEmpty()) {
               for (int i = 0; i < listVigenciaGrupoSalarialBorrar.size(); i++) {
                  administrarGrupoSalarial.borrarVigenciasGruposSalariales(listVigenciaGrupoSalarialBorrar);
               }
               listVigenciaGrupoSalarialBorrar.clear();
            }
            if (!listVigenciaGrupoSalarialCrear.isEmpty()) {
               for (int i = 0; i < listVigenciaGrupoSalarialCrear.size(); i++) {
                  administrarGrupoSalarial.crearVigenciasGruposSalariales(listVigenciaGrupoSalarialCrear);
               }
               listVigenciaGrupoSalarialCrear.clear();
            }
            if (!listVigenciaGrupoSalarialModificar.isEmpty()) {
               administrarGrupoSalarial.editarVigenciasGruposSalariales(listVigenciaGrupoSalarialModificar);
               listVigenciaGrupoSalarialModificar.clear();
            }
            listVigenciasGruposSalariales = null;
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            guardadoVGS = true;
            k = 0;
            vigenciaTablaSeleccionada = null;
            contarRegistrosVigencia();
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos de Vigencia Grupo Salarial con Éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         log.warn("Error guardarCambiosGrupoSalarial : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Vigencia Grupo Salarial, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificacionGeneral() {
      RequestContext context = RequestContext.getCurrentInstance();
      cancelarModificacionGrupoSalarial();
      cancelarModificacionVigenciaGrupoSalarial();
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void cancelarModificacionGrupoSalarial() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaGrupo = "140";
         gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
         gsCodigo.setFilterStyle("display: none; visibility: hidden;");
         gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
         gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
         gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
         gsSalario.setFilterStyle("display: none; visibility: hidden;");
         ////
         RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
         bandera = 0;
         filtrarListGruposSalariales = null;
         tipoLista = 0;
      }
      listGrupoSalarialBorrar.clear();
      listGrupoSalarialCrear.clear();
      listGrupoSalarialModificar.clear();
      grupoSalarialTablaSeleccionada = null;
      k = 0;
      listGruposSalariales = null;
      guardado = true;
      contarRegistrosGrupo();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
   }

   public void cancelarModificacionVigenciaGrupoSalarial() {
      if (banderaVGS == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaVigencia = "100";
         vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
         vgsValor.setFilterStyle("display: none; visibility: hidden;");
         vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
         vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
         banderaVGS = 0;
         filtrarListVigenciasGruposSalariales = null;
         tipoListaVigencia = 0;
      }
      listVigenciaGrupoSalarialBorrar.clear();
      listVigenciaGrupoSalarialCrear.clear();
      listVigenciaGrupoSalarialModificar.clear();
      vigenciaTablaSeleccionada = null;
      k = 0;
      listVigenciasGruposSalariales = null;
      guardadoVGS = true;
      contarRegistrosVigencia();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
   }

   public void editarCelda() {
      if (cualTabla == 1) {
         if (grupoSalarialTablaSeleccionada != null) {
            editarGrupoSalarial = grupoSalarialTablaSeleccionada;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoD");
               RequestContext.getCurrentInstance().execute("PF('editarCodigoD').show()");
               cualCelda = -1;
            } else if (cualCelda == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionD");
               RequestContext.getCurrentInstance().execute("PF('editarDescripcionD').show()");
               cualCelda = -1;
            } else if (cualCelda == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarSalarioD");
               RequestContext.getCurrentInstance().execute("PF('editarSalarioD').show()");
               cualCelda = -1;
            }
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }

      } else if (cualTabla == 2) {
         if (vigenciaTablaSeleccionada != null) {
            if (tipoListaVigencia == 0) {
               editarVigenciaGrupoSalarial = vigenciaTablaSeleccionada;
            }
            if (tipoListaVigencia == 1) {
               editarVigenciaGrupoSalarial = vigenciaTablaSeleccionada;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaVigencia == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVigenciaD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaVigenciaD').show()");
               cualCeldaVigencia = -1;
            } else if (cualCeldaVigencia == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarValorD");
               RequestContext.getCurrentInstance().execute("PF('editarValorD').show()");
               cualCeldaVigencia = -1;
            }
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      }
   }

   public void dialogoNuevoRegistro() {
      RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
      RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()");
   }

   //CREAR 
   public void agregarNuevoGrupoSalarial() {
      int contador = 0;
      int duplicados = 0;
      mensajeValidacion = " ";

      if (nuevoGrupoSalarial.getCodigo() == null) {
         mensajeValidacion = "El campo Código es obligatorio";
      } else {
         for (int i = 0; i < listGruposSalariales.size(); i++) {
            if (listGruposSalariales.get(i).getCodigo() == nuevoGrupoSalarial.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "Ya existe un registro con el código ingresado";
         } else {
            contador++;
         }
      }

      if (nuevoGrupoSalarial.getDescripcion() == (null) || nuevoGrupoSalarial.getDescripcion().isEmpty()) {
         mensajeValidacion = "El campo Descripción es obligatorio";
      } else {
         contador++;
      }
      log.info("contador : " + contador);
      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGrupo = "140";
            gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
            gsCodigo.setFilterStyle("display: none; visibility: hidden;");
            gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
            gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
            gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
            gsSalario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
            bandera = 0;
            filtrarListGruposSalariales = null;
            tipoLista = 0;
         }
         //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
         k++;
         l = BigInteger.valueOf(k);
         nuevoGrupoSalarial.setSecuencia(l);
         listGrupoSalarialCrear.add(nuevoGrupoSalarial);
         listGruposSalariales.add(nuevoGrupoSalarial);
         grupoSalarialTablaSeleccionada = nuevoGrupoSalarial;
         contarRegistrosGrupo();
         nuevoGrupoSalarial = new GruposSalariales();
         RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroGrupoSalarial').hide()");

      } else {
         RequestContext.getCurrentInstance().update("form:errorRegDescripcion");
         RequestContext.getCurrentInstance().execute("PF('errorRegDescripcion').show()");
         contador = 0;
      }

   }

   public void agregarNuevoVigenciaGrupoSalarial() {
      int contador = 0;
      int duplicados = 0;
      mensajeValidacion2 = " ";

      if (nuevoVigenciaGrupoSalarial.getFechavigencia() == null) {
         mensajeValidacion2 = "El campo Fecha es obligatorio";
      } else {
         for (int i = 0; i < listVigenciasGruposSalariales.size(); i++) {
            if (listVigenciasGruposSalariales.get(i).getFechavigencia() == nuevoVigenciaGrupoSalarial.getFechavigencia()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion2 = "Ya existe una vigencia con la fecha ingresada";
         } else {
            contador++;
         }
      }

      if (nuevoVigenciaGrupoSalarial.getValor() == (null)) {
         mensajeValidacion2 = "El campo Valor es obligatorio";
      } else {
         contador++;
      }
      log.info("contador : " + contador);
      if (contador == 2) {
         if (banderaVGS == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaVigencia = "100";
            vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
            vgsValor.setFilterStyle("display: none; visibility: hidden;");
            vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
            vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            banderaVGS = 0;
            filtrarListVigenciasGruposSalariales = null;
            tipoListaVigencia = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoVigenciaGrupoSalarial.setSecuencia(l);
         nuevoVigenciaGrupoSalarial.setGruposalarial(grupoSalarialTablaSeleccionada);
         listVigenciaGrupoSalarialCrear.add(nuevoVigenciaGrupoSalarial);
         if (listVigenciasGruposSalariales == null) {
            listVigenciasGruposSalariales = new ArrayList<VigenciasGruposSalariales>();
         }
         listVigenciasGruposSalariales.add(nuevoVigenciaGrupoSalarial);
         contarRegistrosVigencia();
         nuevoVigenciaGrupoSalarial = new VigenciasGruposSalariales();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciaGrupoSalarial').hide()");
         if (guardadoVGS == true) {
            guardadoVGS = false;
         }
         vigenciaTablaSeleccionada = null;
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().update("form:errorRegNew");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
         contador = 0;
      }
   }

   public void limpiarNuevaGrupoSalarial() {
      nuevoGrupoSalarial = new GruposSalariales();
   }

   public void limpiarNuevaVigenciaGrupoSalarial() {
      nuevoVigenciaGrupoSalarial = new VigenciasGruposSalariales();
   }

   public void verificarRegistroDuplicar() {
      if (cualTabla == 1) {
         duplicarGrupoSalarialM();
      }
      if (cualTabla == 2) {
         duplicarVigenciaGrupoSalarialM();
      }
   }

   public void duplicarGrupoSalarialM() {
      if (grupoSalarialTablaSeleccionada != null) {
         duplicarGrupoSalarial = new GruposSalariales();

         duplicarGrupoSalarial.setCodigo(grupoSalarialTablaSeleccionada.getCodigo());
         duplicarGrupoSalarial.setDescripcion(grupoSalarialTablaSeleccionada.getDescripcion());
         duplicarGrupoSalarial.setEscalafonsalarial(grupoSalarialTablaSeleccionada.getEscalafonsalarial());
         duplicarGrupoSalarial.setSalario(grupoSalarialTablaSeleccionada.getSalario());
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroGrupoSalarial");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroGrupoSalarial').show()");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarVigenciaGrupoSalarialM() {

      if (vigenciaTablaSeleccionada != null) {
         duplicarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
         duplicarVigenciaGrupoSalarial.setFechavigencia(vigenciaTablaSeleccionada.getFechavigencia());
         duplicarVigenciaGrupoSalarial.setGruposalarial(vigenciaTablaSeleccionada.getGruposalarial());
         duplicarVigenciaGrupoSalarial.setValor(vigenciaTablaSeleccionada.getValor());
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroVigenciaGrupoSalarial");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaGrupoSalarial').show()");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }

   }

   public void confirmarDuplicarGrupoSalarial() {
      int contador = 0;
      int duplicados = 0;
      mensajeValidacion = " ";

      if (duplicarGrupoSalarial.getCodigo() == null) {
         mensajeValidacion = "El campo Código es obligatorio";
      } else {
         for (int i = 0; i < listGruposSalariales.size(); i++) {
            if (listGruposSalariales.get(i).getCodigo() == duplicarGrupoSalarial.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "Ya existe un registro con el código ingresado";
         } else {
            contador++;
         }
      }

      if (duplicarGrupoSalarial.getDescripcion() == (null) || duplicarGrupoSalarial.getDescripcion().isEmpty()) {
         mensajeValidacion = "El campo Descripción es obligatorio";
      } else {
         contador++;
      }

      log.info("contador : " + contador);
      if (contador == 2) {
         k++;
         l = BigInteger.valueOf(k);
         duplicarGrupoSalarial.setSecuencia(l);
         listGruposSalariales.add(duplicarGrupoSalarial);
         listGrupoSalarialCrear.add(duplicarGrupoSalarial);
         RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
         grupoSalarialTablaSeleccionada = duplicarGrupoSalarial;
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGrupo = "140";
            gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
            gsCodigo.setFilterStyle("display: none; visibility: hidden;");
            gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
            gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
            gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
            gsSalario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
            bandera = 0;
            filtrarListGruposSalariales = null;
            tipoLista = 0;
         }
         contarRegistrosGrupo();
         duplicarGrupoSalarial = new GruposSalariales();
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroGrupoSalarial').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:errorRegDescripcion");
         RequestContext.getCurrentInstance().execute("PF('errorRegDescripcion').show()");
         contador = 0;
      }
   }

   public void confirmarDuplicarVigenciaGrupoSalarial() {
      int contador = 0;
      int duplicados = 0;
      mensajeValidacion2 = " ";

      if (duplicarVigenciaGrupoSalarial.getFechavigencia() == null) {
         mensajeValidacion2 = "El campo Fecha es obligatorio";
      } else {
         for (int i = 0; i < listVigenciasGruposSalariales.size(); i++) {
            if (listVigenciasGruposSalariales.get(i).getFechavigencia() == duplicarVigenciaGrupoSalarial.getFechavigencia()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion2 = "Ya existe una vigencia con la fecha ingresada";
         } else {
            contador++;
         }
      }

      if (duplicarVigenciaGrupoSalarial.getValor() == (null)) {
         mensajeValidacion2 = "El campo Valor es obligatorio";
      } else {
         contador++;
      }
      log.info("contador : " + contador);
      if (contador == 2) {
         duplicarVigenciaGrupoSalarial.setGruposalarial(grupoSalarialTablaSeleccionada);
         if (listVigenciasGruposSalariales == null) {
            listVigenciasGruposSalariales = new ArrayList<VigenciasGruposSalariales>();
         }
         k++;
         l = BigInteger.valueOf(k);
         duplicarVigenciaGrupoSalarial.setSecuencia(l);
         listVigenciasGruposSalariales.add(duplicarVigenciaGrupoSalarial);
         listVigenciaGrupoSalarialCrear.add(duplicarVigenciaGrupoSalarial);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciaGrupoSalarial').hide()");
         vigenciaTablaSeleccionada = duplicarVigenciaGrupoSalarial;
         if (guardadoVGS == true) {
            guardadoVGS = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (banderaVGS == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaVigencia = "100";
            vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
            vgsValor.setFilterStyle("display: none; visibility: hidden;");
            vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
            vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            bandera = 0;
            filtrarListGruposSalariales = null;
            tipoLista = 0;
         }
         contarRegistrosVigencia();
         duplicarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
      } else {
         RequestContext.getCurrentInstance().update("form:errorRegNew");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
         contador = 0;
      }
   }

   public void limpiarDuplicarGrupoSalarial() {
      duplicarGrupoSalarial = new GruposSalariales();
   }

   public void limpiarDuplicarVigenciaGrupoSalarial() {
      duplicarVigenciaGrupoSalarial = new VigenciasGruposSalariales();
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   public void verificarRegistroBorrar() {
      if (cualTabla == 1) {
         if (listVigenciasGruposSalariales.isEmpty()) {
            borrarGrupoSalarial();
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
         }
      } else if (cualTabla == 2) {
         borrarVigenciaGrupoSalarial();
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void borrarGrupoSalarial() {
      if (grupoSalarialTablaSeleccionada != null) {
         if (!listGrupoSalarialModificar.isEmpty() && listGrupoSalarialModificar.contains(grupoSalarialTablaSeleccionada)) {
            int modIndex = listGrupoSalarialModificar.indexOf(grupoSalarialTablaSeleccionada);
            listGrupoSalarialModificar.remove(modIndex);
            listGrupoSalarialBorrar.add(grupoSalarialTablaSeleccionada);
         } else if (!listGrupoSalarialCrear.isEmpty() && listGrupoSalarialCrear.contains(grupoSalarialTablaSeleccionada)) {
            int crearIndex = listGrupoSalarialCrear.indexOf(grupoSalarialTablaSeleccionada);
            listGrupoSalarialCrear.remove(crearIndex);
         } else {
            listGrupoSalarialBorrar.add(grupoSalarialTablaSeleccionada);
         }
         listGruposSalariales.remove(grupoSalarialTablaSeleccionada);
         if (tipoLista == 1) {
            filtrarListGruposSalariales.remove(grupoSalarialTablaSeleccionada);
         }
         contarRegistrosGrupo();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
         grupoSalarialTablaSeleccionada = null;

         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void borrarVigenciaGrupoSalarial() {
      if (vigenciaTablaSeleccionada != null) {
         if (!listVigenciaGrupoSalarialModificar.isEmpty() && listVigenciaGrupoSalarialModificar.contains(vigenciaTablaSeleccionada)) {
            int modIndex = listVigenciaGrupoSalarialModificar.indexOf(vigenciaTablaSeleccionada);
            listVigenciaGrupoSalarialModificar.remove(modIndex);
            listVigenciaGrupoSalarialBorrar.add(vigenciaTablaSeleccionada);
         } else if (!listVigenciaGrupoSalarialCrear.isEmpty() && listVigenciaGrupoSalarialCrear.contains(vigenciaTablaSeleccionada)) {
            int crearIndex = listVigenciaGrupoSalarialCrear.indexOf(vigenciaTablaSeleccionada);
            listVigenciaGrupoSalarialCrear.remove(crearIndex);
         } else {
            listVigenciaGrupoSalarialBorrar.add(vigenciaTablaSeleccionada);
         }
         listVigenciasGruposSalariales.remove(vigenciaTablaSeleccionada);
         if (tipoListaVigencia == 1) {
            filtrarListVigenciasGruposSalariales.remove(vigenciaTablaSeleccionada);
         }
         contarRegistrosVigencia();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
         vigenciaTablaSeleccionada = null;

         if (guardadoVGS == true) {
            guardadoVGS = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (grupoSalarialTablaSeleccionada != null) {
         if (bandera == 0) {
            altoTablaGrupo = "120";
            gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
            gsCodigo.setFilterStyle("width: 85% !important");
            gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
            gsDescripcion.setFilterStyle("width: 85% !important");
            gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
            gsSalario.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
            bandera = 1;
         } else if (bandera == 1) {
            altoTablaGrupo = "140";
            gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
            gsCodigo.setFilterStyle("display: none; visibility: hidden;");
            gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
            gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
            gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
            gsSalario.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
            bandera = 0;
            filtrarListGruposSalariales = null;
            tipoLista = 0;
         }
      }
      if (vigenciaTablaSeleccionada != null) {
         if (banderaVGS == 0) {
            altoTablaVigencia = "80";
            vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
            vgsValor.setFilterStyle("width: 180px");
            vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
            vgsFechaVigencia.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            banderaVGS = 1;
         } else if (banderaVGS == 1) {
            altoTablaVigencia = "100";
            vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
            vgsValor.setFilterStyle("display: none; visibility: hidden;");
            vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
            vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
            banderaVGS = 0;
            filtrarListVigenciasGruposSalariales = null;
            tipoListaVigencia = 0;
         }
      }
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaGrupo = "140";
         gsCodigo = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsCodigo");
         gsCodigo.setFilterStyle("display: none; visibility: hidden;");
         gsDescripcion = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsDescripcion");
         gsDescripcion.setFilterStyle("display: none; visibility: hidden;");
         gsSalario = (Column) c.getViewRoot().findComponent("form:datosGrupoSalarial:gsSalario");
         gsSalario.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGrupoSalarial");
         bandera = 0;
         filtrarListGruposSalariales = null;
         tipoLista = 0;
      }
      if (banderaVGS == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaVigencia = "100";
         vgsValor = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsValor");
         vgsValor.setFilterStyle("display: none; visibility: hidden;");
         vgsFechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial:vgsFechaVigencia");
         vgsFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosVigenciaGrupoSalarial");
         banderaVGS = 0;
         filtrarListVigenciasGruposSalariales = null;
         tipoListaVigencia = 0;
      }
      listGrupoSalarialBorrar.clear();
      listGrupoSalarialCrear.clear();
      listGrupoSalarialModificar.clear();
      listVigenciaGrupoSalarialBorrar.clear();
      listVigenciaGrupoSalarialCrear.clear();
      listVigenciaGrupoSalarialModificar.clear();
      vigenciaTablaSeleccionada = null;
      grupoSalarialTablaSeleccionada = null;
      k = 0;
      listGruposSalariales = null;
      listVigenciasGruposSalariales = null;
      guardado = true;
      guardadoVGS = true;
      cambiosPagina = true;
      contarRegistrosGrupo();
      contarRegistrosVigencia();
      navegar("atras");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void validarExportPDF() throws IOException {
      if (cualTabla == 1) {
         exportPDF_GS();
      }
      if (cualTabla == 2) {
         exportPDF_VGS();
      }
   }

   public void exportPDF_GS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarG:datosGruposSalarialesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "GruposSalarialesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_VGS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVG:datosVigenciaGruposSalarialesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "VigenciasGruposSalarialesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void validarExportXLS() throws IOException {
      if (cualTabla == 1) {
         exportXLS_GS();
      }
      if (cualTabla == 2) {
         exportXLS_VGS();
      }
   }

   public void exportXLS_GS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarG:datosGruposSalarialesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "GruposSalarialesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_VGS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVG:datosVigenciaGruposSalarialesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "VigenciasGruposSalarialesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }
   //EVENTO FILTRAR

   public void eventoFiltrarGrupo() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistrosGrupo();
   }

   public void eventoFiltrarVigencia() {
      if (tipoListaVigencia == 0) {
         tipoListaVigencia = 1;
      }
      contarRegistrosVigencia();
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {
      if (cualTabla == 1) {
         verificarRastroGrupoSalarial();
      } else if (cualTabla == 2) {
         verificarRastroVigenciaGrupoSalarial();
      }
   }

   public void verificarRastroGrupoSalarial() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (grupoSalarialTablaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(grupoSalarialTablaSeleccionada.getSecuencia(), "GRUPOSSALARIALES");
         backUpSecRegistro = grupoSalarialTablaSeleccionada.getSecuencia();
         backUp = grupoSalarialTablaSeleccionada.getSecuencia();
         grupoSalarialTablaSeleccionada = null;
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "GruposSalariales";
            msnConfirmarRastro = "La tabla GRUPOSSALARIALES tiene rastros para el registro seleccionado, ¿Desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("GRUPOSSALARIALES")) {
         nombreTablaRastro = "GruposSalariales";
         msnConfirmarRastroHistorico = "La tabla GRUPOSSALARIALES tiene rastros históricos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroVigenciaGrupoSalarial() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaTablaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(vigenciaTablaSeleccionada.getSecuencia(), "VIGENCIASGRUPOSSALARIALES");
         backUpSecRegistroVigencia = vigenciaTablaSeleccionada.getSecuencia();
         backUp = vigenciaTablaSeleccionada.getSecuencia();
         vigenciaTablaSeleccionada = null;
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "VigenciasGruposSalariales";
            msnConfirmarRastro = "La tabla VIGENCIASGRUPOSSALARIALES tiene rastros para el registro seleccionado, ¿Desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASGRUPOSSALARIALES")) {
         nombreTablaRastro = "VigenciasGruposSalariales";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASGRUPOSSALARIALES tiene rastros históricos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistrosGrupo() {
      RequestContext.getCurrentInstance().update("form:infoRegistroGrupo");
   }

   public void contarRegistrosVigencia() {
      RequestContext.getCurrentInstance().update("form:infoRegistroVigencia");
   }

   //GETTERS AND SETTERS
   public List<GruposSalariales> getListGruposSalariales() {
      try {
         if (listGruposSalariales == null) {
            listGruposSalariales = administrarGrupoSalarial.listGruposSalariales();
         }
         return listGruposSalariales;
      } catch (Exception e) {
         log.warn("Error...!! getListGruposSalariales : " + e.toString());
         return null;
      }
   }

   public void setListGruposSalariales(List<GruposSalariales> ListGruposSalariales) {
      this.listGruposSalariales = ListGruposSalariales;
   }

   public List<GruposSalariales> getFiltrarListGruposSalariales() {
      return filtrarListGruposSalariales;
   }

   public void setFiltrarListGruposSalariales(List<GruposSalariales> ListGruposSalariales) {
      this.filtrarListGruposSalariales = ListGruposSalariales;
   }

   public GruposSalariales getNuevoGrupoSalarial() {
      return nuevoGrupoSalarial;
   }

   public void setNuevoGrupoSalarial(GruposSalariales GrupoSalarial) {
      this.nuevoGrupoSalarial = GrupoSalarial;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public GruposSalariales getEditarGrupoSalarial() {
      return editarGrupoSalarial;
   }

   public void setEditarGrupoSalarial(GruposSalariales GrupoSalarial) {
      this.editarGrupoSalarial = GrupoSalarial;
   }

   public GruposSalariales getDuplicarGrupoSalarial() {
      return duplicarGrupoSalarial;
   }

   public void setDuplicarGrupoSalarial(GruposSalariales duplicarGrupoSalarial) {
      this.duplicarGrupoSalarial = duplicarGrupoSalarial;
   }

   public BigInteger getBackUpSecRegistro() {
      return backUpSecRegistro;
   }

   public void setBackUpSecRegistro(BigInteger BackUpSecRegistro) {
      this.backUpSecRegistro = BackUpSecRegistro;
   }

   public List<VigenciasGruposSalariales> getListVigenciasGruposSalariales() {
      if (listVigenciasGruposSalariales == null) {
         if (grupoSalarialTablaSeleccionada != null) {
            listVigenciasGruposSalariales = administrarGrupoSalarial.lisVigenciasGruposSalarialesSecuencia(grupoSalarialTablaSeleccionada.getSecuencia());
         }
      }
      return listVigenciasGruposSalariales;
   }

   public void setListVigenciasGruposSalariales(List<VigenciasGruposSalariales> listVigenciasGruposSalariales) {
      this.listVigenciasGruposSalariales = listVigenciasGruposSalariales;
   }

   public List<VigenciasGruposSalariales> getFiltrarListVigenciasGruposSalariales() {
      return filtrarListVigenciasGruposSalariales;
   }

   public void setFiltrarListVigenciasGruposSalariales(List<VigenciasGruposSalariales> filtrarListVigenciasGruposSalariales) {
      this.filtrarListVigenciasGruposSalariales = filtrarListVigenciasGruposSalariales;
   }

   public List<GruposSalariales> getListGrupoSalarialModificar() {
      return listGrupoSalarialModificar;
   }

   public void setListGrupoSalarialModificar(List<GruposSalariales> listGrupoSalarialModificar) {
      this.listGrupoSalarialModificar = listGrupoSalarialModificar;
   }

   public List<GruposSalariales> getListGrupoSalarialCrear() {
      return listGrupoSalarialCrear;
   }

   public void setListGrupoSalarialCrear(List<GruposSalariales> listGrupoSalarialCrear) {
      this.listGrupoSalarialCrear = listGrupoSalarialCrear;
   }

   public List<GruposSalariales> getListGrupoSalarialBorrar() {
      return listGrupoSalarialBorrar;
   }

   public void setListGrupoSalarialBorrar(List<GruposSalariales> listGrupoSalarialBorrar) {
      this.listGrupoSalarialBorrar = listGrupoSalarialBorrar;
   }

   public List<VigenciasGruposSalariales> getListVigenciaGrupoSalarialModificar() {
      return listVigenciaGrupoSalarialModificar;
   }

   public void setListVigenciaGrupoSalarialModificar(List<VigenciasGruposSalariales> listVigenciaGrupoSalariaModificar) {
      this.listVigenciaGrupoSalarialModificar = listVigenciaGrupoSalariaModificar;
   }

   public VigenciasGruposSalariales getNuevoVigenciaGrupoSalarial() {
      return nuevoVigenciaGrupoSalarial;
   }

   public void setNuevoVigenciaGrupoSalarial(VigenciasGruposSalariales nuevoVigenciaGrupoSalarial) {
      this.nuevoVigenciaGrupoSalarial = nuevoVigenciaGrupoSalarial;
   }

   public List<VigenciasGruposSalariales> getListVigenciaGrupoSalarialCrear() {
      return listVigenciaGrupoSalarialCrear;
   }

   public void setListVigenciaGrupoSalarialCrear(List<VigenciasGruposSalariales> listVigenciaGrupoSalarialCrear) {
      this.listVigenciaGrupoSalarialCrear = listVigenciaGrupoSalarialCrear;
   }

   public List<VigenciasGruposSalariales> getListVigenciaGrupoSalarialBorrar() {
      return listVigenciaGrupoSalarialBorrar;
   }

   public void setListVigenciaGrupoSalarialBorrar(List<VigenciasGruposSalariales> listVigenciaGrupoSalarialBorrar) {
      this.listVigenciaGrupoSalarialBorrar = listVigenciaGrupoSalarialBorrar;
   }

   public VigenciasGruposSalariales getEditarVigenciaGrupoSalarial() {
      return editarVigenciaGrupoSalarial;
   }

   public void setEditarVigenciaGrupoSalarial(VigenciasGruposSalariales editarVigenciaGrupoSalarial) {
      this.editarVigenciaGrupoSalarial = editarVigenciaGrupoSalarial;
   }

   public VigenciasGruposSalariales getDuplicarVigenciaGrupoSalarial() {
      return duplicarVigenciaGrupoSalarial;
   }

   public void setDuplicarVigenciaGrupoSalarial(VigenciasGruposSalariales duplicarVigenciaGrupoSalarial) {
      this.duplicarVigenciaGrupoSalarial = duplicarVigenciaGrupoSalarial;
   }

   public BigInteger getBackUpSecRegistroVigencia() {
      return backUpSecRegistroVigencia;
   }

   public void setBackUpSecRegistroVigencia(BigInteger backUpSecRegistroVigencia) {
      this.backUpSecRegistroVigencia = backUpSecRegistroVigencia;
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
      if (grupoSalarialTablaSeleccionada != null) {
         nombreTabla = ":formExportarG:datosGruposSalarialesExportar";
         nombreXML = "GruposSalarialesXML";
      }
      if (vigenciaTablaSeleccionada != null) {
         nombreTabla = ":formExportarVG:datosVigenciaGruposSalarialesExportar";
         nombreXML = "VigenciasGruposSalarialesXML";
      }
      return nombreTabla;
   }

   public void setNombreTabla(String nombreTabla) {
      this.nombreTabla = nombreTabla;
   }

   public GruposSalariales getGrupoSalarialTablaSeleccionada() {
      return grupoSalarialTablaSeleccionada;
   }

   public void setGrupoSalarialTablaSeleccionada(GruposSalariales grupoSalarialTablaSeleccionada) {
      this.grupoSalarialTablaSeleccionada = grupoSalarialTablaSeleccionada;
   }

   public VigenciasGruposSalariales getVigenciaTablaSeleccionada() {
      return vigenciaTablaSeleccionada;
   }

   public void setVigenciaTablaSeleccionada(VigenciasGruposSalariales vigenciaTablaSeleccionada) {
      this.vigenciaTablaSeleccionada = vigenciaTablaSeleccionada;
   }

   public String getAltoTablaGrupo() {
      return altoTablaGrupo;
   }

   public void setAltoTablaGrupo(String algoTablaGrupo) {
      this.altoTablaGrupo = algoTablaGrupo;
   }

   public String getAltoTablaVigencia() {
      return altoTablaVigencia;
   }

   public void setAltoTablaVigencia(String altoTablaVigencia) {
      this.altoTablaVigencia = altoTablaVigencia;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public String getInfoRegistroGrupo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosGrupoSalarial");
      infoRegistroGrupo = String.valueOf(tabla.getRowCount());
      return infoRegistroGrupo;
   }

   public void setInfoRegistroGrupo(String infoRegistroGrupo) {
      this.infoRegistroGrupo = infoRegistroGrupo;
   }

   public String getInfoRegistroVigencia() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciaGrupoSalarial");
      infoRegistroVigencia = String.valueOf(tabla.getRowCount());
      return infoRegistroVigencia;
   }

   public void setInfoRegistroVigencia(String infoRegistroVigencia) {
      this.infoRegistroVigencia = infoRegistroVigencia;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public String getMensajeValidacion2() {
      return mensajeValidacion2;
   }

   public void setMensajeValidacion2(String mensajeValidacion2) {
      this.mensajeValidacion2 = mensajeValidacion2;
   }

}
