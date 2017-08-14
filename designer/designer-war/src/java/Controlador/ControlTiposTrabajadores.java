/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposCotizantes;
import Entidades.TiposTrabajadores;
import Entidades.VigenciasDiasTT;
import Entidades.VigenciasTiposTrabajadores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposCotizantesInterface;
import InterfaceAdministrar.AdministrarTiposTrabajadoresInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;
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
import org.primefaces.component.export.Exporter;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

/**
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlTiposTrabajadores implements Serializable {

   private static Logger log = Logger.getLogger(ControlTiposTrabajadores.class);

   @EJB
   AdministrarTiposCotizantesInterface administrarTiposCotizantes;
   @EJB
   AdministrarTiposTrabajadoresInterface administrarTiposTrabajadores;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //Lista Tipos Trabajadores
   private List<TiposTrabajadores> listaTiposTrabajadores;
   private List<TiposTrabajadores> filtrarTiposTrabajadores;
   private TiposTrabajadores tipoTrabajadorSeleccionado;
   //Vigencias Dias TT
   private List<VigenciasDiasTT> listaVigenciasDiasTT;
   private List<VigenciasDiasTT> filtrarVigenciasDiasTT;
   private VigenciasDiasTT vigenciaDiaSeleccionado;
   //Listas valores
   private List<TiposCotizantes> lovTiposCotizantes;
   private List<TiposCotizantes> filtrarLovTiposCotizantes;
   private TiposCotizantes tipoCotizanteLovSeleccionado;
   private List<TiposTrabajadores> lovTiposTrabajadores;
   private List<TiposTrabajadores> filtrarLovTiposTrabajadores;
   private TiposTrabajadores tipoTrabajadorLovSeleccionado;
   //Columnas Tabla Tipos Trabajadores
   private Column tTCodigo, tTNombre, ttFactorD, ttTipoCot, ttDiasVacNoOrd, ttDiasVac, tTModalidad, tTTipo,
           tTNivel, tTBase, tTPor, tTMPS, tTPatronPS, tTPatronPP, tTPatronPR, tTCesCC, VDTdias, VDTFecha;

   //Variables de control
   private int tipoActualizacion;
   private int bandera;
   private boolean aceptar;
   private boolean guardado;
   private int cualCelda, tipoLista, tipoListaVD, cualCeldaVD;
   public String infoRegistroTT, infoRegistroTC, infoRegistroVD, infoRegistroLovTT;
   private String altoTabla, altoTablaVD;
   //modificar
   private List<TiposTrabajadores> listTTModificar;
   //crear
   private TiposTrabajadores nuevoTipoT;
   private List<TiposTrabajadores> listTTCrear;
   private BigInteger nuevaSecuencia;
   private int intNuevaSec;
   //borrar
   private List<TiposTrabajadores> listTTBorrar;
   //editar celda
   private TiposTrabajadores editarTT;
   //duplicar
   private TiposTrabajadores duplicarTT;
   //Vigencias Dias
   private List<VigenciasDiasTT> listVDModificar;
   private List<VigenciasDiasTT> listVDCrear;
   private List<VigenciasDiasTT> listVDBorrar;
   private VigenciasDiasTT nuevaVD;
   private VigenciasDiasTT duplicarVD;
   private VigenciasDiasTT editarVD;
   //Backs
   private TiposCotizantes tipoCotizanteBack;
   private String nombreTTBack;
   private Short codigoBack;
   private Date fechaVDBack;
   //Activar boton lista Valor
   private boolean activarLOV;
   //Para Recordar Seleccion
   private DataTable tablaTT, tablaVD;
//    private Map<BigInteger, String> mapaModalidades;
   private boolean mostrartodos;
   //RASTROS
   private String nombreTablaRastro, msnConfirmarRastro, msnConfirmarRastroHistorico;
   //
   private String permitirCambioBotonLov;
   private int tablaActiva;
   //CLONAR
   private String nombreNuevoClonado;
   private Short codigoNuevoClonado;
   private TiposTrabajadores tipoTrabajadorAClonar;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private String altoTablaReg;

   /**
    * Creates a new instance of ControlTiposTrabajadores
    */
   public ControlTiposTrabajadores() {
      //Lista Tipos Trabajadores
      listaTiposTrabajadores = null;
      tipoTrabajadorSeleccionado = null;
      listaVigenciasDiasTT = null;
      //Lista Tipos Cotizantes
      lovTiposCotizantes = null;
      tipoCotizanteLovSeleccionado = null;
      //Variables de control
      tipoActualizacion = -1;
      aceptar = true;
      guardado = true;
      cualCelda = -1;
      tipoLista = 0;
      infoRegistroTT = "";
      infoRegistroTC = "";
      altoTabla = "187";
      altoTablaVD = "45";
      //modificar
      listTTModificar = new ArrayList<TiposTrabajadores>();
      //crear
      nuevoTipoT = new TiposTrabajadores();
      listTTCrear = new ArrayList<TiposTrabajadores>();
      intNuevaSec = 0;
      //borrar
      listTTBorrar = new ArrayList<TiposTrabajadores>();
      //editar celda
      editarTT = new TiposTrabajadores();
      //duplicar
      duplicarTT = new TiposTrabajadores();
      paginaAnterior = "nominaf";
      activarLOV = true;
      tipoCotizanteBack = new TiposCotizantes();
      fechaVDBack = new Date();
//      mapaModalidades = new LinkedHashMap<BigInteger, String>();
      mostrartodos = true;
      lovTiposTrabajadores = null;

      listVDModificar = new ArrayList<VigenciasDiasTT>();
      listVDCrear = new ArrayList<VigenciasDiasTT>();
      listVDBorrar = new ArrayList<VigenciasDiasTT>();
      nuevaVD = new VigenciasDiasTT();
      duplicarVD = new VigenciasDiasTT();
      editarVD = new VigenciasDiasTT();
      permitirCambioBotonLov = "SIapagarCelda";
      tablaActiva = 0;
      tipoTrabajadorAClonar = new TiposTrabajadores();
      nombreNuevoClonado = null;
      codigoNuevoClonado = null;
      nombreTTBack = "";
      codigoBack = new Short("0");
      mapParametros.put("paginaAnterior", paginaAnterior);
      guardado = true;
      tipoLista = 0;
      tipoListaVD = 0;
   }

   public void limpiarListasValor() {
      lovTiposCotizantes = null;
      lovTiposTrabajadores = null;
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
         administrarTiposTrabajadores.obtenerConexion(ses.getId());
         administrarTiposCotizantes.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
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
      String pagActual = "tipotrabajador";
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

   public void recibirPaginaAnterior(String pagina) {
      paginaAnterior = pagina;
      listaTiposTrabajadores = null;
      listaVigenciasDiasTT = null;
      getListaTiposTrabajadores();
      if (listaTiposTrabajadores != null) {
         if (!listaTiposTrabajadores.isEmpty()) {
            tipoTrabajadorSeleccionado = listaTiposTrabajadores.get(0);
            listaVigenciasDiasTT = administrarTiposTrabajadores.consultarDiasPorTipoT(tipoTrabajadorSeleccionado.getSecuencia());
         }
      }
   }

   public String retornarPagina() {
      return paginaAnterior;
   }

   public void cambiarIndice(TiposTrabajadores tipoTrabajador, int celda) {
      tipoTrabajadorSeleccionado = tipoTrabajador;
      vigenciaDiaSeleccionado = null;
      tipoActualizacion = 0;
      cualCelda = celda;
      listaVigenciasDiasTT = administrarTiposTrabajadores.consultarDiasPorTipoT(tipoTrabajadorSeleccionado.getSecuencia());
      if (cualCelda == 3) {
         permitirCambioBotonLov = "NOapagarCelda";
         tipoCotizanteBack = tipoTrabajadorSeleccionado.getTipocotizante();
         activarBotonLOV();
      } else {
         permitirCambioBotonLov = "SoloHacerNull";
         anularBotonLOV();
      }
      if (cualCelda == 0) {
         codigoBack = tipoTrabajadorSeleccionado.getCodigo();
      } else if (cualCelda == 1) {
         nombreTTBack = tipoTrabajadorSeleccionado.getNombre();
      }
      contarRegistrosVD();
      vigenciaDiaSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
      tablaActiva = 1;
   }

   public void cambiarIndiceDefault() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (permitirCambioBotonLov.equals("SoloHacerNull")) {
         anularBotonLOV();
      } else if (permitirCambioBotonLov.equals("SIapagarCelda")) {
         anularBotonLOV();
         cualCelda = -1;
      } else if (permitirCambioBotonLov.equals("NOapagarCelda")) {
         activarBotonLOV();
      }
      listaVigenciasDiasTT = administrarTiposTrabajadores.consultarDiasPorTipoT(tipoTrabajadorSeleccionado.getSecuencia());
      contarRegistrosVD();
      permitirCambioBotonLov = "SIapagarCelda";
      tablaActiva = 1;
      vigenciaDiaSeleccionado = null;
      log.info("cambiarIndiceDefault() tablaActiva: " + tablaActiva);
      RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
   }

   public void cambiarIndiceVD(VigenciasDiasTT vigenciaDia, int celda) {
      log.info("cambiarIndiceVD celda: " + celda);
      vigenciaDiaSeleccionado = vigenciaDia;
      tipoActualizacion = 0;
      cualCeldaVD = celda;

      if (cualCeldaVD == 1) {
         fechaVDBack = vigenciaDiaSeleccionado.getFechaVigencia();
      }
      anularBotonLOV();
      tablaActiva = 2;
   }

   public void cambiarIndiceVDDefault() {
      log.info("cambiarIndiceDefault cualCeldaVD : " + cualCeldaVD);
      cualCeldaVD = 0;
      log.info("cambiarIndiceDefault cualCeldaVD : " + cualCeldaVD);
      tipoActualizacion = 0;
      anularBotonLOV();
      tablaActiva = 2;
   }

   public void asignarIndex(TiposTrabajadores tiposTrabajador, int column) {
      tipoTrabajadorSeleccionado = tiposTrabajador;
      tipoActualizacion = 0;
      cualCelda = column;
      if (cualCelda == 3) {
         activarBotonLOV();
         contarRegistrosLovTCotiz();
         RequestContext.getCurrentInstance().update("form:dialogTiposCot");
         RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').show()");
      } else {
         anularBotonLOV();
      }
      tablaActiva = 1;
   }

   public void lovTipoCotizanteNueyDup(int tipoAct) {
      tipoActualizacion = tipoAct;
      activarBotonLOV();
      contarRegistrosLovTCotiz();
      RequestContext.getCurrentInstance().update("form:dialogTiposCot");
      RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').show()");
   }

   public void listaValoresBoton() {
      if (cualCelda == 3) {
         contarRegistrosLovTCotiz();
         tipoActualizacion = 0;
         RequestContext.getCurrentInstance().update("form:dialogTiposCot");
         RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').show()");
      }
   }

   public void modificarTT(TiposTrabajadores tipoTrabajador, String column, String valor) {
      tipoTrabajadorSeleccionado = tipoTrabajador;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (column.equalsIgnoreCase("TC")) {
         tipoTrabajadorSeleccionado.setTipocotizante(tipoCotizanteBack);

         for (int i = 0; i < lovTiposCotizantes.size(); i++) {
            if (lovTiposCotizantes.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            tipoTrabajadorSeleccionado.setTipocotizante(lovTiposCotizantes.get(indiceUnicoElemento));
         } else {
            RequestContext.getCurrentInstance().update("form:dialogTiposCot");
            RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').show()");
            tipoActualizacion = 0;
         }
         if (coincidencias == 1) {
            modificarTT(tipoTrabajador);
         }
      }
      if (column.equalsIgnoreCase("COD")) {
         log.info("modificarTT COD valor : " + valor);
         tipoTrabajadorSeleccionado.setCodigo(codigoBack);
         Short cod = new Short(valor);
         for (int i = 0; i < listaTiposTrabajadores.size(); i++) {
            if (listaTiposTrabajadores.get(i).getCodigo() == cod) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias > 0) {
            RequestContext.getCurrentInstance().update("form:errorClonadoRepetido");
            RequestContext.getCurrentInstance().execute("PF('errorClonadoRepetido').show()");
            tipoActualizacion = 0;
         } else {
            tipoTrabajadorSeleccionado.setCodigo(cod);
            modificarTT(tipoTrabajador);
         }
      }
      if (column.equalsIgnoreCase("NOM")) {
         log.info("modificarTT NOM valor : " + valor);
         tipoTrabajadorSeleccionado.setNombre(nombreTTBack);
//            llenarLOVTT();
         for (int i = 0; i < listaTiposTrabajadores.size(); i++) {
            if (listaTiposTrabajadores.get(i).getNombre().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias > 0) {
            RequestContext.getCurrentInstance().update("form:errorClonadoRepetido");
            RequestContext.getCurrentInstance().execute("PF('errorClonadoRepetido').show()");
         } else {
            tipoTrabajadorSeleccionado.setNombre(valor);
//                if (coincidencias == 1) {
            modificarTT(tipoTrabajador);
//                }
         }
         tipoActualizacion = 0;
      }
      activarBotonLOV();
      RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
   }

   public void modificarTT(TiposTrabajadores tipoTrabajador) {
      log.info("Entro en el evento Change()");
      tipoTrabajadorSeleccionado = tipoTrabajador;
      if (!listTTCrear.contains(tipoTrabajadorSeleccionado)) {
         if (listTTModificar.isEmpty()) {
            listTTModificar.add(tipoTrabajadorSeleccionado);
         } else if (!listTTModificar.contains(tipoTrabajadorSeleccionado)) {
            listTTModificar.add(tipoTrabajadorSeleccionado);
         }
      }
      if (guardado) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void modificarFechaVD(VigenciasDiasTT vigenciaDia) {
      vigenciaDiaSeleccionado = vigenciaDia;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      vigenciaDiaSeleccionado.setFechaVigencia(fechaVDBack);

      for (int i = 0; i < listaVigenciasDiasTT.size(); i++) {
         if (listaVigenciasDiasTT.get(i).getFechaVigencia() == vigenciaDia.getFechaVigencia()) {
            indiceUnicoElemento = i;
            coincidencias++;
         }
      }
      if (coincidencias == 1) {
         RequestContext.getCurrentInstance().update("form:dialogErrorFechas");
         RequestContext.getCurrentInstance().execute("PF('dialogErrorFechas').show()");
         tipoActualizacion = 0;
      } else {
         modificarVD(vigenciaDia);
      }
      activarBotonLOV();
      RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
   }

   public void modificarVD(VigenciasDiasTT vigenciaDia) {
      vigenciaDiaSeleccionado = vigenciaDia;
      if (!listVDCrear.contains(vigenciaDiaSeleccionado)) {
         if (listVDModificar.isEmpty()) {
            listVDModificar.add(vigenciaDiaSeleccionado);
         } else if (!listVDModificar.contains(vigenciaDiaSeleccionado)) {
            listVDModificar.add(vigenciaDiaSeleccionado);
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void autocompletarNuevoyDuplicado(String column, String valor, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (column.equalsIgnoreCase("TC")) {
         if (tipoNuevo == 1) {
            nuevoTipoT.setTipocotizante(tipoCotizanteBack);
         } else if (tipoNuevo == 2) {
            duplicarTT.setTipocotizante(tipoCotizanteBack);
         }
         for (int i = 0; i < lovTiposCotizantes.size(); i++) {
            if (lovTiposCotizantes.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoTipoT.setTipocotizante(lovTiposCotizantes.get(indiceUnicoElemento));
               //Tok/ RequestContext.getCurrentInstance().update("formularioDialogos:Campo en dialogo nuevo");
            } else if (tipoNuevo == 2) {
               duplicarTT.setTipocotizante(lovTiposCotizantes.get(indiceUnicoElemento));
               //Tok/ RequestContext.getCurrentInstance().update("formularioDialogos:Campo en dialogo duplicar");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:dialogTiposCot");
            RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               //Tok/ RequestContext.getCurrentInstance().update("formularioDialogos:Campo en dialogo nuevo");
            } else if (tipoNuevo == 2) {
               //Tok/ RequestContext.getCurrentInstance().update("formularioDialogos:Campo en dialogo duplicar");
            }
         }
      }
   }

   //LOVS
   /**
    * Metodo que actualiza la tipo cotizacion seleccionada
    */
   public void actualizarTipoCotizacion() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {// Si se trabaja sobre la tabla y no sobre un dialogo
         tipoTrabajadorSeleccionado.setTipocotizante(tipoCotizanteLovSeleccionado);
         if (!listTTCrear.contains(tipoTrabajadorSeleccionado)) {
            if (listTTModificar.isEmpty()) {
               listTTModificar.add(tipoTrabajadorSeleccionado);
            } else if (!listTTModificar.contains(tipoTrabajadorSeleccionado)) {
               listTTModificar.add(tipoTrabajadorSeleccionado);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
      } else if (tipoActualizacion == 1) {// Para crear registro
         nuevoTipoT.setTipocotizante(tipoCotizanteLovSeleccionado);
         //Tok/ RequestContext.getCurrentInstance().update("formularioDialogos:Campo en dialogo nuevo");
      } else if (tipoActualizacion == 2) {// Para duplicar registro
         duplicarTT.setTipocotizante(tipoCotizanteLovSeleccionado);
         //Tok/ RequestContext.getCurrentInstance().update("formularioDialogos:Campo en dialogo duplicar");
      }
      filtrarLovTiposCotizantes = null;
      aceptar = true;
      tipoActualizacion = -1;
      context.reset("form:lovTiposCot:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposCot').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').hide()");
      RequestContext.getCurrentInstance().update("form:dialogTiposCot");
      RequestContext.getCurrentInstance().update("form:lovTiposCot");
      RequestContext.getCurrentInstance().update("form:aceptarTC");
   }

   /**
    * Metodo que cancela los cambios sobre reforma laboral
    */
   public void cancelarCambioReformaLaboral() {
      filtrarLovTiposCotizantes = null;
      tipoCotizanteLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();

      context.reset("form:lovTiposCot:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposCot').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').hide()");
      RequestContext.getCurrentInstance().update("form:dialogTiposCot");
      RequestContext.getCurrentInstance().update("form:lovTiposCot");
      RequestContext.getCurrentInstance().update("form:aceptarTC");
   }

   /**
    * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "167";
         altoTablaVD = "25";
         tTCodigo = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTCodigo");
         tTCodigo.setFilterStyle("width: 75% !important");
         tTNombre = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTNombre");
         tTNombre.setFilterStyle("width: 85% !important");
         ttFactorD = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttFactorD");
         ttFactorD.setFilterStyle("width: 75% !important");
         ttTipoCot = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttTipoCot");
         ttTipoCot.setFilterStyle("width: 85% !important");
         ttDiasVacNoOrd = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttDiasVacNoOrd");
         ttDiasVacNoOrd.setFilterStyle("width: 75% !important");
         ttDiasVac = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttDiasVac");
         ttDiasVac.setFilterStyle("width: 75% !important");
         tTModalidad = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTModalidad");
         tTModalidad.setFilterStyle("width: 85% !important");
         tTTipo = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTTipo");
         tTTipo.setFilterStyle("width: 85% !important");
         tTNivel = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTNivel");
         tTNivel.setFilterStyle("width: 75% !important");
         tTBase = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTBase");
         tTBase.setFilterStyle("width: 85% !important");
         tTPor = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPor");
         tTPor.setFilterStyle("width: 75% !important");
         tTMPS = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTMPS");
         tTMPS.setFilterStyle("width: 75% !important");
         tTPatronPS = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPatronPS");
         tTPatronPS.setFilterStyle("width: 75% !important");
         tTPatronPP = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPatronPP");
         tTPatronPP.setFilterStyle("width: 75% !important");
         tTPatronPR = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPatronPR");
         tTPatronPR.setFilterStyle("width: 75% !important");
         tTCesCC = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTCesCC");
         tTCesCC.setFilterStyle("width: 75% !important");
         //Vigencias Dias TT
         VDTdias = (Column) c.getViewRoot().findComponent("form:datosVigenciasDTT:VDTdias");
         VDTdias.setFilterStyle("width: 80% !important");
         VDTFecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasDTT:VDTFecha");
         VDTFecha.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
         RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         altoTabla = "187";
         altoTablaVD = "45";
         tTCodigo = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTCodigo");
         tTCodigo.setFilterStyle("display: none; visibility: hidden;");
         tTNombre = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTNombre");
         tTNombre.setFilterStyle("display: none; visibility: hidden;");
         ttFactorD = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttFactorD");
         ttFactorD.setFilterStyle("display: none; visibility: hidden;");
         ttTipoCot = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttTipoCot");
         ttTipoCot.setFilterStyle("display: none; visibility: hidden;");
         ttDiasVacNoOrd = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttDiasVacNoOrd");
         ttDiasVacNoOrd.setFilterStyle("display: none; visibility: hidden;");
         ttDiasVac = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttDiasVac");
         ttDiasVac.setFilterStyle("display: none; visibility: hidden;");
         tTModalidad = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTModalidad");
         tTModalidad.setFilterStyle("display: none; visibility: hidden;");
         tTTipo = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTTipo");
         tTTipo.setFilterStyle("display: none; visibility: hidden;");
         tTNivel = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTNivel");
         tTNivel.setFilterStyle("display: none; visibility: hidden;");
         tTBase = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTBase");
         tTBase.setFilterStyle("display: none; visibility: hidden;");
         tTPor = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPor");
         tTPor.setFilterStyle("display: none; visibility: hidden;");
         tTMPS = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTMPS");
         tTMPS.setFilterStyle("display: none; visibility: hidden;");
         tTPatronPS = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPatronPS");
         tTPatronPS.setFilterStyle("display: none; visibility: hidden;");
         tTPatronPP = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPatronPP");
         tTPatronPP.setFilterStyle("display: none; visibility: hidden;");
         tTPatronPR = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPatronPR");
         tTPatronPR.setFilterStyle("display: none; visibility: hidden;");
         tTCesCC = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTCesCC");
         tTCesCC.setFilterStyle("display: none; visibility: hidden;");
         //Vigencias Dias TT
         VDTdias = (Column) c.getViewRoot().findComponent("form:datosVigenciasDTT:VDTdias");
         VDTdias.setFilterStyle("display: none; visibility: hidden;");
         VDTFecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasDTT:VDTFecha");
         VDTFecha.setFilterStyle("display: none; visibility: hidden;");
         tipoLista = 0;
         bandera = 0;
         filtrarTiposTrabajadores = null;
         RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
         RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
      }
   }

   public void restaurarTablaTT() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTabla = "187";
      altoTablaVD = "45";
      tTCodigo = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTCodigo");
      tTCodigo.setFilterStyle("display: none; visibility: hidden;");
      tTNombre = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTNombre");
      tTNombre.setFilterStyle("display: none; visibility: hidden;");
      ttFactorD = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttFactorD");
      ttFactorD.setFilterStyle("display: none; visibility: hidden;");
      ttTipoCot = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttTipoCot");
      ttTipoCot.setFilterStyle("display: none; visibility: hidden;");
      ttDiasVacNoOrd = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttDiasVacNoOrd");
      ttDiasVacNoOrd.setFilterStyle("display: none; visibility: hidden;");
      ttDiasVac = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:ttDiasVac");
      ttDiasVac.setFilterStyle("display: none; visibility: hidden;");
      tTModalidad = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTModalidad");
      tTModalidad.setFilterStyle("display: none; visibility: hidden;");
      tTTipo = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTTipo");
      tTTipo.setFilterStyle("display: none; visibility: hidden;");
      tTNivel = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTNivel");
      tTNivel.setFilterStyle("display: none; visibility: hidden;");
      tTBase = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTBase");
      tTBase.setFilterStyle("display: none; visibility: hidden;");
      tTPor = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPor");
      tTPor.setFilterStyle("display: none; visibility: hidden;");
      tTMPS = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTMPS");
      tTMPS.setFilterStyle("display: none; visibility: hidden;");
      tTPatronPS = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPatronPS");
      tTPatronPS.setFilterStyle("display: none; visibility: hidden;");
      tTPatronPP = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPatronPP");
      tTPatronPP.setFilterStyle("display: none; visibility: hidden;");
      tTPatronPR = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTPatronPR");
      tTPatronPR.setFilterStyle("display: none; visibility: hidden;");
      tTCesCC = (Column) c.getViewRoot().findComponent("form:datosTTrabajadores:tTCesCC");
      tTCesCC.setFilterStyle("display: none; visibility: hidden;");
      //Vigencias Dias TT
      VDTdias = (Column) c.getViewRoot().findComponent("form:datosVigenciasDTT:VDTdias");
      VDTdias.setFilterStyle("display: none; visibility: hidden;");
      VDTFecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasDTT:VDTFecha");
      VDTFecha.setFilterStyle("display: none; visibility: hidden;");
      tipoLista = 0;
      bandera = 0;
      filtrarTiposTrabajadores = null;
      RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
      RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
   }

   //CREAR TT
   /**
    * Metodo que se encarga de agregar un nuevo TT
    */
   public void agregarNuevaTT() {
      RequestContext context = RequestContext.getCurrentInstance();
      int cont = 0;
      Short cod = new Short("0");
      if ((nuevoTipoT.getCodigo() != cod) || (nuevoTipoT.getNombre() != null)) {

         for (int j = 0; j < listaTiposTrabajadores.size(); j++) {
            if (nuevoTipoT.getCodigo() == listaTiposTrabajadores.get(j).getCodigo()) {
               cont++;
            }
         }
         for (int j = 0; j < listaTiposTrabajadores.size(); j++) {
            if (nuevoTipoT.getNombre().equals(listaTiposTrabajadores.get(j).getNombre())) {
               cont++;
            }
         }
         if (cont > 0) {
            RequestContext.getCurrentInstance().update("form:validacionNuevo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
         } else {
            if (bandera == 1) {
               restaurarTablaTT();
            }
            intNuevaSec++;
            nuevaSecuencia = BigInteger.valueOf(intNuevaSec);
            nuevoTipoT.setSecuencia(nuevaSecuencia);
            listTTCrear.add(nuevoTipoT);
            listaTiposTrabajadores.add(nuevoTipoT);
            perderSeleccionTT();
            tipoTrabajadorSeleccionado = listaTiposTrabajadores.get(listaTiposTrabajadores.indexOf(nuevoTipoT));

            anularBotonLOV();
            contarRegistrosTT();
            listaVigenciasDiasTT = null;
            nuevoTipoT = new TiposTrabajadores();
            nuevoTipoT.setTipocotizante(new TiposCotizantes());

            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroTT').hide()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorNewRegNulos').show()");
      }
   }
//LIMPIAR NUEVO REGISTRO

   public void limpiarNuevaTT() {
      nuevoTipoT = new TiposTrabajadores();
      nuevoTipoT.setTipocotizante(new TiposCotizantes());
   }
   //DUPLICAR TT

   /**
    * Metodo que duplica una vigencia especifica dado por la posicion de la fila
    */
   public void duplicarTT() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoTrabajadorSeleccionado == null && vigenciaDiaSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (tablaActiva == 2 && vigenciaDiaSeleccionado != null) {
         duplicarVD = new VigenciasDiasTT();
         intNuevaSec++;
         nuevaSecuencia = BigInteger.valueOf(intNuevaSec);

         duplicarVD.setCodigo(vigenciaDiaSeleccionado.getCodigo());
         duplicarVD.setDias(vigenciaDiaSeleccionado.getDias());
         duplicarVD.setFechaVigencia(vigenciaDiaSeleccionado.getFechaVigencia());
         duplicarVD.setTipoTrabajador(vigenciaDiaSeleccionado.getTipoTrabajador());
         duplicarVD.setSecuencia(nuevaSecuencia);
         log.info("duplicarTT abrir duplicarRegistroVD");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroVD");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroVD').show()");

      } else if (tablaActiva == 1 && tipoTrabajadorSeleccionado != null) {
         duplicarTT = new TiposTrabajadores();
         intNuevaSec++;
         nuevaSecuencia = BigInteger.valueOf(intNuevaSec);

         duplicarTT.setBaseendeudamiento(tipoTrabajadorSeleccionado.getBaseendeudamiento());
         duplicarTT.setCesantiasectorconstruccion(tipoTrabajadorSeleccionado.getCesantiasectorconstruccion());
         duplicarTT.setCodigo(tipoTrabajadorSeleccionado.getCodigo());
         duplicarTT.setDerechodiasvacaciones(tipoTrabajadorSeleccionado.getDerechodiasvacaciones());
         duplicarTT.setDiasvacacionesnoordinarios(tipoTrabajadorSeleccionado.getDiasvacacionesnoordinarios());
         duplicarTT.setFactordesalarizacion(tipoTrabajadorSeleccionado.getFactordesalarizacion());
         duplicarTT.setModalidad(tipoTrabajadorSeleccionado.getModalidad());
         duplicarTT.setModalidadpensionsectorsalud(tipoTrabajadorSeleccionado.getModalidadpensionsectorsalud());
         duplicarTT.setNivelendeudamiento(tipoTrabajadorSeleccionado.getNivelendeudamiento());
         duplicarTT.setNombre(tipoTrabajadorSeleccionado.getNombre());
         duplicarTT.setPatronpagapension(tipoTrabajadorSeleccionado.getPatronpagapension());
         duplicarTT.setPatronpagaretencion(tipoTrabajadorSeleccionado.getPatronpagaretencion());
         duplicarTT.setPatronpagasalud(tipoTrabajadorSeleccionado.getPatronpagasalud());
         duplicarTT.setPorcentajesml(tipoTrabajadorSeleccionado.getPorcentajesml());
         duplicarTT.setPromediabasicoacumulados(tipoTrabajadorSeleccionado.getPromediabasicoacumulados());
         duplicarTT.setTipo(tipoTrabajadorSeleccionado.getTipo());
         duplicarTT.setTipocotizante(tipoTrabajadorSeleccionado.getTipocotizante());
         duplicarTT.setSemestreespecial(tipoTrabajadorSeleccionado.getSemestreespecial());
         duplicarTT.setSecuencia(nuevaSecuencia);
         log.info("duplicarTT abrir duplicarRegistroTT");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroTT");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTT').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * VigenciasReformasLaborales
    */
   public void confirmarDuplicarTT() {
      RequestContext context = RequestContext.getCurrentInstance();
      int cont = 0;
      Short cod = new Short("0");
      if ((duplicarTT.getCodigo() != cod) && duplicarTT.getNombre() != null) {

         for (int j = 0; j < listaTiposTrabajadores.size(); j++) {
            if (duplicarTT.getCodigo() == listaTiposTrabajadores.get(j).getCodigo()) {
               cont++;
            }
         }
         for (int j = 0; j < listaTiposTrabajadores.size(); j++) {
            if (duplicarTT.getNombre().equals(listaTiposTrabajadores.get(j).getNombre())) {
               cont++;
            }
         }
         if (cont > 0) {
            RequestContext.getCurrentInstance().update("form:validacionNuevo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
         } else {
            intNuevaSec++;
            nuevaSecuencia = BigInteger.valueOf(intNuevaSec);
            duplicarTT.setSecuencia(nuevaSecuencia);
            listaTiposTrabajadores.add(duplicarTT);
            listTTCrear.add(duplicarTT);
            perderSeleccionTT();
            tipoTrabajadorSeleccionado = listaTiposTrabajadores.get(listaTiposTrabajadores.lastIndexOf(duplicarTT));
            contarRegistrosTT();
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            duplicarTT = new TiposTrabajadores();
            if (bandera == 1) {
               restaurarTablaTT();
            }
            anularBotonLOV();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTT').hide()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorNewRegNulos').show()");
      }
   }
//LIMPIAR DUPLICAR

   /**
    * Metodo que limpia los datos de un duplicar Vigencia
    */
   public void limpiarduplicarTT() {
      duplicarTT = new TiposTrabajadores();
      duplicarTT.setTipocotizante(new TiposCotizantes());
   }

   //BORRAR TT
   /**
    * Metodo que borra las vigencias seleccionadas
    */
   public void borrar() {
      if (tipoTrabajadorSeleccionado == null && vigenciaDiaSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (tablaActiva == 2 && vigenciaDiaSeleccionado != null) {
            if (!listVDModificar.isEmpty() && listVDModificar.contains(vigenciaDiaSeleccionado)) {
               listVDModificar.remove(vigenciaDiaSeleccionado);
               listVDBorrar.add(vigenciaDiaSeleccionado);
            } else if (!listVDCrear.isEmpty() && listVDCrear.contains(vigenciaDiaSeleccionado)) {
               listVDCrear.remove(vigenciaDiaSeleccionado);
            } else {
               listVDBorrar.add(vigenciaDiaSeleccionado);
            }
            listaVigenciasDiasTT.remove(vigenciaDiaSeleccionado);
            if (tipoListaVD == 1) {
               filtrarVigenciasDiasTT.remove(vigenciaDiaSeleccionado);
            }
         } else if (tablaActiva == 1 && tipoTrabajadorSeleccionado != null) {
            if (administrarTiposTrabajadores.hayRegistrosSecundarios(tipoTrabajadorSeleccionado.getSecuencia())) {
               RequestContext.getCurrentInstance().execute("PF('validacionRS').show()");
            } else {
               if (!listTTModificar.isEmpty() && listTTModificar.contains(tipoTrabajadorSeleccionado)) {
                  listTTModificar.remove(tipoTrabajadorSeleccionado);
                  listTTBorrar.add(tipoTrabajadorSeleccionado);
               } else if (!listTTCrear.isEmpty() && listTTCrear.contains(tipoTrabajadorSeleccionado)) {
                  listTTCrear.remove(tipoTrabajadorSeleccionado);
               } else {
                  listTTBorrar.add(tipoTrabajadorSeleccionado);
               }
               listaTiposTrabajadores.remove(tipoTrabajadorSeleccionado);
               if (tipoLista == 1) {
                  filtrarTiposTrabajadores.remove(tipoTrabajadorSeleccionado);
               }
               contarRegistrosTT();
            }
         }
         perderSeleccionTT();
         RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      anularBotonLOV();
   }
   //CREAR VD

   /**
    * Metodo que se encarga de agregar un nuevo VD
    */
   public void agregarNuevaVD() {
      RequestContext context = RequestContext.getCurrentInstance();
      int cont = 0;
      if ((nuevaVD.getDias() != null) && (nuevaVD.getFechaVigencia() != null)) {

         for (int j = 0; j < listaVigenciasDiasTT.size(); j++) {
            if (nuevaVD.getFechaVigencia() == listaVigenciasDiasTT.get(j).getFechaVigencia()) {
               cont++;
            }
         }
         if (cont > 0) {
            RequestContext.getCurrentInstance().update("form:validacionFechas");
            RequestContext.getCurrentInstance().execute("PF('validacionFechas').show()");
         } else {
            if (bandera == 1) {
               restaurarTablaTT();
            }
            intNuevaSec++;
            nuevaSecuencia = BigInteger.valueOf(intNuevaSec);
            nuevaVD.setSecuencia(nuevaSecuencia);
            listVDCrear.add(nuevaVD);
            listaVigenciasDiasTT.add(nuevaVD);
            vigenciaDiaSeleccionado = listaVigenciasDiasTT.get(listaVigenciasDiasTT.indexOf(nuevaVD));
            anularBotonLOV();
            contarRegistrosVD();
            nuevaVD = new VigenciasDiasTT();

            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVD').hide()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorNewRegNulosVD').show()");
      }
   }
//LIMPIAR NUEVO REGISTRO

   public void limpiarNuevaVD() {
      nuevaVD = new VigenciasDiasTT();
   }
   //DUPLICAR VD

   /**
    * Metodo que duplica una vigencia especifica dado por la posicion de la fila
    */
   public void duplicarVDias() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaDiaSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         duplicarTT = new TiposTrabajadores();
         intNuevaSec++;
         nuevaSecuencia = BigInteger.valueOf(intNuevaSec);

         duplicarVD.setCodigo(vigenciaDiaSeleccionado.getCodigo());
         duplicarVD.setFechaVigencia(vigenciaDiaSeleccionado.getFechaVigencia());
         duplicarVD.setDias(vigenciaDiaSeleccionado.getDias());
         duplicarVD.setSecuencia(nuevaSecuencia);
         duplicarVD.setTipoTrabajador(vigenciaDiaSeleccionado.getTipoTrabajador());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroVD");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroVD').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    */
   public void confirmarDuplicarVD() {
      RequestContext context = RequestContext.getCurrentInstance();
      int cont = 0;
      if ((duplicarVD.getDias() != null) || (duplicarVD.getFechaVigencia() != null)) {

         for (int j = 0; j < listaVigenciasDiasTT.size(); j++) {
            if (duplicarVD.getFechaVigencia() == listaVigenciasDiasTT.get(j).getFechaVigencia()) {
               cont++;
            }
         }
         if (cont > 0) {
            RequestContext.getCurrentInstance().update("form:validacionFechas");
            RequestContext.getCurrentInstance().execute("PF('validacionFechas').show()");
         } else {
            intNuevaSec++;
            nuevaSecuencia = BigInteger.valueOf(intNuevaSec);
            duplicarVD.setSecuencia(nuevaSecuencia);
            listaVigenciasDiasTT.add(duplicarVD);
            listVDCrear.add(duplicarVD);
            vigenciaDiaSeleccionado = listaVigenciasDiasTT.get(listaVigenciasDiasTT.indexOf(duplicarVD));
            contarRegistrosVD();
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            duplicarVD = new VigenciasDiasTT();
            if (bandera == 1) {
               restaurarTablaTT();
            }
            anularBotonLOV();
            RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroVD').hide()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorNewRegNulosVD').show()");
      }
   }
//LIMPIAR DUPLICAR

   /**
    * Metodo que limpia los datos de un duplicar Vigencia
    */
   public void limpiarduplicarVD() {
      duplicarVD = new VigenciasDiasTT();
   }

   //BORRAR VD
   /**
    * Metodo que borra las vigencias seleccionadas
    */
   public void borrarVD() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (vigenciaDiaSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (!listVDModificar.isEmpty() && listVDModificar.contains(vigenciaDiaSeleccionado)) {
            listVDModificar.remove(vigenciaDiaSeleccionado);
            listVDBorrar.add(vigenciaDiaSeleccionado);
         } else if (!listVDCrear.isEmpty() && listVDCrear.contains(vigenciaDiaSeleccionado)) {
            listVDCrear.remove(vigenciaDiaSeleccionado);
         } else {
            listVDBorrar.add(vigenciaDiaSeleccionado);
         }
         listaVigenciasDiasTT.remove(vigenciaDiaSeleccionado);
         if (tipoLista == 1) {
            filtrarVigenciasDiasTT.remove(vigenciaDiaSeleccionado);
         }
         contarRegistrosTT();
         perderSeleccionTT();
         RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");

         activarBotonLOV();
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   //Seleccion en Listas de Valores
   public void actualizarTipoCotizante() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         if (tipoActualizacion == 0) {
            tipoTrabajadorSeleccionado.setTipocotizante(tipoCotizanteLovSeleccionado);
            if (!listTTCrear.contains(tipoTrabajadorSeleccionado)) {
               if (listTTModificar.isEmpty()) {
                  listTTModificar.add(tipoTrabajadorSeleccionado);
               } else if (!listTTModificar.contains(tipoTrabajadorSeleccionado)) {
                  listTTModificar.add(tipoTrabajadorSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
            RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
            RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').hide()");
         } else if (tipoActualizacion == 1) {
            nuevoTipoT.setTipocotizante(tipoCotizanteLovSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevatipoCotNTT");
            RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').hide()");
         } else if (tipoActualizacion == 2) {
            duplicarTT.setTipocotizante(tipoCotizanteLovSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicartipoCotNTT");
            RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').hide()");
         }
         filtrarLovTiposCotizantes = null;
         tipoCotizanteLovSeleccionado = null;
         aceptar = true;
         tipoActualizacion = -1;

         context.reset("form:lovTiposCot:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovTiposCot').clearFilters()");
         RequestContext.getCurrentInstance().update("form:dialogTiposCot");
         RequestContext.getCurrentInstance().update("form:lovTiposCot");
         RequestContext.getCurrentInstance().update("form:aceptarTC");
         RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').hide()");

      } catch (Exception e) {
         log.warn("Error BETA .actualizarCentroCosto ERROR============" + e.getMessage());
      }
      activarBotonLOV();
   }

   public void cancelarSeleccionTipoCot() {
      try {
         filtrarLovTiposCotizantes = null;
         tipoCotizanteLovSeleccionado = null;
         aceptar = true;
         tipoActualizacion = -1;

         RequestContext context = RequestContext.getCurrentInstance();
         context.reset("form:lovTiposCot:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovTiposCot').clearFilters()");
         RequestContext.getCurrentInstance().update("form:dialogTiposCot");
         RequestContext.getCurrentInstance().update("form:lovTiposCot");
         RequestContext.getCurrentInstance().update("form:aceptarTC");
         RequestContext.getCurrentInstance().execute("PF('dialogTiposCot').hide()");
      } catch (Exception e) {
         log.warn("Error cancelarSeleccionTipoCot :" + e.getMessage());
      }
   }

   public void llamadoDialogoTT(int tipoAct) {
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = tipoAct;
      tipoTrabajadorLovSeleccionado = null;
      try {
         if (guardado == false) {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
         } else {
            llenarLOVTT();
            RequestContext.getCurrentInstance().update("form:lovTiposTra");
            RequestContext.getCurrentInstance().execute("PF('dialogTiposTra').show()");
            contarRegistrosLovTT();
         }
      } catch (Exception e) {
         log.error("ERROR LLAMADO DIALOGO BUSCAR TT " + e);
      }
   }

   public void llenarLOVTT() {
      if (lovTiposTrabajadores == null) {
         lovTiposTrabajadores = new ArrayList<TiposTrabajadores>();
         if (listaTiposTrabajadores != null) {
            for (int i = 0; i < listaTiposTrabajadores.size(); i++) {
               lovTiposTrabajadores.add(listaTiposTrabajadores.get(i));
            }
         } else {
            lovTiposTrabajadores = administrarTiposTrabajadores.buscarTiposTrabajadoresTT();
         }
      }
   }

   public void seleccionarTipoTrabajador() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         if (tipoActualizacion == 0) {
            listaTiposTrabajadores.clear();
            listaTiposTrabajadores.add(tipoTrabajadorLovSeleccionado);
            tipoTrabajadorSeleccionado = tipoTrabajadorLovSeleccionado;
            listaVigenciasDiasTT = administrarTiposTrabajadores.consultarDiasPorTipoT(tipoTrabajadorSeleccionado.getSecuencia());
            filtrarLovTiposTrabajadores = null;
            aceptar = true;
            mostrartodos = false;
            contarRegistrosTT();
            contarRegistrosVD();
            activarBotonLOV();

            RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
            RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
            RequestContext.getCurrentInstance().update("form:mostrarTodos");
         } else if (tipoActualizacion == 3) {
            tipoTrabajadorAClonar = new TiposTrabajadores();
            tipoTrabajadorAClonar.setCodigo(tipoTrabajadorLovSeleccionado.getCodigo());
            tipoTrabajadorAClonar.setNombre(tipoTrabajadorLovSeleccionado.getNombre());
            tipoTrabajadorAClonar.setSecuencia(tipoTrabajadorLovSeleccionado.getSecuencia());

            RequestContext.getCurrentInstance().update("form:codigoTipoTClonarBase");
            RequestContext.getCurrentInstance().update("form:nombreTipoTClonarBase");
         }
         context.reset("form:lovTiposTra:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovTiposTra').clearFilters()");
         RequestContext.getCurrentInstance().update("form:dialogTiposTra");
         RequestContext.getCurrentInstance().update("form:lovTiposTra");
         RequestContext.getCurrentInstance().update("form:aceptarTTra");
         RequestContext.getCurrentInstance().execute("PF('dialogTiposTra').hide()");

      } catch (Exception e) {
         log.warn("Error seleccionarTipoTrabajador : " + e.getMessage());
      }
   }

   public void autocompletarClonado(String column) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (column.equalsIgnoreCase("COD")) {
         for (int i = 0; i < listaTiposTrabajadores.size(); i++) {
            if (listaTiposTrabajadores.get(i).getCodigo() == tipoTrabajadorAClonar.getCodigo()) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            tipoTrabajadorAClonar = new TiposTrabajadores();
            tipoTrabajadorAClonar.setCodigo(listaTiposTrabajadores.get(indiceUnicoElemento).getCodigo());
            tipoTrabajadorAClonar.setNombre(listaTiposTrabajadores.get(indiceUnicoElemento).getNombre());
            tipoTrabajadorAClonar.setSecuencia(listaTiposTrabajadores.get(indiceUnicoElemento).getSecuencia());
            RequestContext.getCurrentInstance().update("form:codigoTipoTClonarBase");
            RequestContext.getCurrentInstance().update("form:nombreTipoTClonarBase");
         }
      } else if (column.equalsIgnoreCase("Nom")) {
         for (int i = 0; i < listaTiposTrabajadores.size(); i++) {
            if (listaTiposTrabajadores.get(i).getNombre().startsWith(tipoTrabajadorAClonar.getNombre().toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            tipoTrabajadorAClonar = new TiposTrabajadores();
            tipoTrabajadorAClonar.setCodigo(listaTiposTrabajadores.get(indiceUnicoElemento).getCodigo());
            tipoTrabajadorAClonar.setNombre(listaTiposTrabajadores.get(indiceUnicoElemento).getNombre());
            tipoTrabajadorAClonar.setSecuencia(listaTiposTrabajadores.get(indiceUnicoElemento).getSecuencia());
            RequestContext.getCurrentInstance().update("form:codigoTipoTClonarBase");
            RequestContext.getCurrentInstance().update("form:nombreTipoTClonarBase");
         }
      }
      if (coincidencias != 1) {
         RequestContext.getCurrentInstance().update("form:lovTiposTra");
         RequestContext.getCurrentInstance().update("form:dialogTiposTra");
         RequestContext.getCurrentInstance().execute("PF('dialogTiposTra').show()");
      }
   }

   public void clonarTT() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("Entro en Clonar()");
      if (nombreNuevoClonado != null && codigoNuevoClonado > 0 && tipoTrabajadorAClonar.getCodigo() > 0) {
         int error = 0;
         for (int i = 0; i < listaTiposTrabajadores.size(); i++) {
            if (listaTiposTrabajadores.get(i).getNombre().equals(nombreNuevoClonado)) {
               error++;
            }
            if (listaTiposTrabajadores.get(i).getCodigo() == codigoNuevoClonado) {
               error++;
            }
         }
         if (error > 0) {
            RequestContext.getCurrentInstance().update("form:errorClonadoRepetido");
            RequestContext.getCurrentInstance().execute("PF('errorClonadoRepetido').show()");
         } else {
            String retornoClonado = administrarTiposTrabajadores.clonarTT(nombreNuevoClonado, codigoNuevoClonado, tipoTrabajadorAClonar.getCodigo());
            if (retornoClonado.equals("BIEN")) {
               listaTiposTrabajadores = null;
               getListaTiposTrabajadores();
               boolean banderita = false;
               if (listaTiposTrabajadores != null) {
                  for (int i = 0; i < listaTiposTrabajadores.size(); i++) {
                     if (listaTiposTrabajadores.get(i).getNombre().equals(nombreNuevoClonado)) {
                        banderita = true;
                     }
                  }
                  if (banderita) {
                     FacesMessage msg = new FacesMessage("Información", "Tipo trabajador clonado correctamente");
                     FacesContext.getCurrentInstance().addMessage(null, msg);
                     RequestContext.getCurrentInstance().update("form:growl");
                  }
               }
               RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
               RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
               contarRegistrosTT();
               contarRegistrosVD();
            } else {
               FacesMessage msg = new FacesMessage("Error", "ERROR clonando Tipo trabajador : " + retornoClonado);
               FacesContext.getCurrentInstance().addMessage(null, msg);
               RequestContext.getCurrentInstance().update("form:growl");
            }
         }
      } else {
         RequestContext.getCurrentInstance().update("form:errorClonadoNulos");
         RequestContext.getCurrentInstance().execute("PF('errorClonadoNulos').show()");
      }
   }

   public void cancelarCambioTipoTrabajador() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         filtrarLovTiposTrabajadores = null;
         aceptar = true;
         tipoActualizacion = -1;
         context.reset("form:lovTiposTra:globalFilter");
         RequestContext.getCurrentInstance().execute("PF('lovTiposTra').clearFilters()");
         RequestContext.getCurrentInstance().update("form:dialogTiposTra");
         RequestContext.getCurrentInstance().update("form:lovTiposTra");
         RequestContext.getCurrentInstance().update("form:aceptarTTra");
         RequestContext.getCurrentInstance().execute("PF('dialogTiposTra').hide()");
      } catch (Exception e) {
         log.warn("Error cancelarCambioTipoTrabajador : " + e.getMessage());
      }
   }

   public void mostrarTodos() {
      RequestContext context = RequestContext.getCurrentInstance();
      listaTiposTrabajadores.clear();
      if (lovTiposTrabajadores != null) {
         for (int i = 0; i < lovTiposTrabajadores.size(); i++) {
            listaTiposTrabajadores.add(lovTiposTrabajadores.get(i));
         }
      } else {
         listaTiposTrabajadores = administrarTiposTrabajadores.buscarTiposTrabajadoresTT();
      }
      tipoTrabajadorSeleccionado = null;
      listaVigenciasDiasTT = null;
      aceptar = true;
      contarRegistrosTT();
      contarRegistrosVD();
      anularBotonLOV();
      mostrartodos = true;
      RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
      RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
      RequestContext.getCurrentInstance().update("form:mostrarTodos");
   }

   public void guardarYSalir() {
      guardadoGeneral();
      salir();
   }

   //GUARDAR
   /**
    * Metodo que guarda los cambios efectuados en la pagina
    */
   public void guardadoGeneral() {
      if (guardado == false) {
         if (!listTTBorrar.isEmpty()) {
            for (int i = 0; i < listTTBorrar.size(); i++) {
               administrarTiposTrabajadores.borrarTT(listTTBorrar.get(i));
            }
            listTTBorrar.clear();
         }
         if (!listTTCrear.isEmpty()) {
            for (int i = 0; i < listTTCrear.size(); i++) {
               administrarTiposTrabajadores.crearTT(listTTCrear.get(i));
            }
            listTTCrear.clear();
         }
         if (!listTTModificar.isEmpty()) {
            for (int i = 0; i < listTTModificar.size(); i++) {
               administrarTiposTrabajadores.editarTT(listTTModificar.get(i));
            }
            listTTModificar.clear();
         }

         if (!listVDBorrar.isEmpty()) {
            for (int i = 0; i < listVDBorrar.size(); i++) {
               administrarTiposTrabajadores.borrarVD(listVDBorrar.get(i));
            }
            listVDBorrar.clear();
         }
         if (!listVDCrear.isEmpty()) {
            for (int i = 0; i < listVDCrear.size(); i++) {
               administrarTiposTrabajadores.crearVD(listVDCrear.get(i));
            }
            listVDCrear.clear();
         }
         if (!listVDModificar.isEmpty()) {
            for (int i = 0; i < listVDModificar.size(); i++) {
               administrarTiposTrabajadores.editarVD(listVDModificar.get(i));
            }
            listVDModificar.clear();
         }

         listaTiposTrabajadores = null;
         getListaTiposTrabajadores();
         perderSeleccionTT();
         contarRegistrosTT();
         contarRegistrosVD();
         activarBotonLOV();

         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         intNuevaSec = 0;
      }
   }

   //CANCELAR MODIFICACIONES
   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificaciones() {
      if (bandera == 1) {
         restaurarTablaTT();
      }
      tablaActiva = 0;
      listTTBorrar.clear();
      listTTCrear.clear();
      listTTModificar.clear();
      listaTiposTrabajadores = null;
      getListaTiposTrabajadores();
      lovTiposTrabajadores = null;
      contarRegistrosTT();
      perderSeleccionTT();
      intNuevaSec = 0;
      guardado = true;
      mostrartodos = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }
   //SALIR

   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         restaurarTablaTT();
      }
      lovTiposTrabajadores = null;
      tablaActiva = 0;

      activarBotonLOV();
      listTTBorrar.clear();
      listTTCrear.clear();
      listTTModificar.clear();
      intNuevaSec = 0;
      listaTiposTrabajadores = null;
      perderSeleccionTT();
      guardado = true;
      navegar("atras");
   }

   //MOSTRAR DATOS CELDA
   /**
    * Metodo que muestra los dialogos de editar con respecto a la lista real o
    * la lista filtrada y a la columna
    */
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoTrabajadorSeleccionado == null && vigenciaDiaSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else {
         if (tablaActiva == 1 && tipoTrabajadorSeleccionado != null) {
            editarTT = tipoTrabajadorSeleccionado;

            if (cualCelda == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoTT");
               RequestContext.getCurrentInstance().execute("PF('editarCodigoTT').show()");
               cualCelda = -1;
            } else if (cualCelda == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripTT");
               RequestContext.getCurrentInstance().execute("PF('editarDescripTT').show()");
               cualCelda = -1;
            } else if (cualCelda == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFactorDesTT");
               RequestContext.getCurrentInstance().execute("PF('editarFactorDesTT').show()");
               cualCelda = -1;
            } else if (cualCelda == 3) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoCotTT");
               RequestContext.getCurrentInstance().execute("PF('editarTipoCotTT').show()");
               cualCelda = -1;
            } else if (cualCelda == 4) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasVacNOTT");
               RequestContext.getCurrentInstance().execute("PF('editarDiasVacNOTT').show()");
               cualCelda = -1;
            } else if (cualCelda == 5) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasVacTT");
               RequestContext.getCurrentInstance().execute("PF('editarDiasVacTT').show()");
               cualCelda = -1;
            } else if (cualCelda == 8) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarNivelEndTT");
               RequestContext.getCurrentInstance().execute("PF('editarNivelEndTT').show()");
               cualCelda = -1;
            } else if (cualCelda == 10) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarPorcentTT");
               RequestContext.getCurrentInstance().execute("PF('editarPorcentTT').show()");
               cualCelda = -1;
            }
         }
         if (tablaActiva == 2 && vigenciaDiaSeleccionado != null) {
            editarVD = vigenciaDiaSeleccionado;

            if (cualCeldaVD == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasVD");
               RequestContext.getCurrentInstance().execute("PF('editarDiasVD').show()");
               cualCeldaVD = -1;
            } else if (cualCeldaVD == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVD");
               RequestContext.getCurrentInstance().execute("PF('editarFechaVD').show()");
               cualCeldaVD = -1;
            }
         }
      }
   }

   public void verificarNuevoVigenciasDias() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoTrabajadorSeleccionado != null) {
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVD");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVD");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVD').show()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoVigenciasDias");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoVigenciasDias').show()");
      }
   }

   //METODO RASTROS PARA LAS TABLAS
   public void verificarRastro() {
      if (tipoTrabajadorSeleccionado == null && vigenciaDiaSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablasH').show()");
      } else if (tablaActiva == 2 && vigenciaDiaSeleccionado != null) {
         verificarRastroVigenciasDias();
      } else if (tablaActiva == 1 && tipoTrabajadorSeleccionado != null) {
         verificarRastroTipoTrabajador();
      }
   }

   public void verificarRastroTipoTrabajador() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(tipoTrabajadorSeleccionado.getSecuencia(), "TIPOSTRABAJADORES");
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "TiposTrabajadores";
         msnConfirmarRastro = "La tabla TIPOSTRABAJADORES tiene rastros para el registro seleccionado, ¿desea continuar?";
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

   public void verificarRastroTipoTrabajadorH() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("TIPOSTRABAJADORES")) {
         nombreTablaRastro = "TiposTrabajadores";
         msnConfirmarRastroHistorico = "La tabla TIPOSTRABAJADORES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroVigenciasDias() {
      RequestContext context = RequestContext.getCurrentInstance();
      int resultado = administrarRastros.obtenerTabla(vigenciaDiaSeleccionado.getSecuencia(), "VIGENCIASDIASTT");
      if (resultado == 1) {
         RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
      } else if (resultado == 2) {
         nombreTablaRastro = "VigenciasDiasTT";
         msnConfirmarRastro = "La tabla VIGENCIASDIASTT tiene rastros para el registro seleccionado, ¿desea continuar?";
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

   public void verificarRastroVigenciasDiasH() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (administrarRastros.verificarHistoricosTabla("VIGENCIASDIASTT")) {
         nombreTablaRastro = "VigenciasDiasTT";
         msnConfirmarRastroHistorico = "La tabla VIGENCIASDIASTT tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   /**
    * Metodo que activa el boton aceptar de la pagina y los dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }

   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //Perder seleccion TT
   public void perderSeleccionTT() {
      tipoTrabajadorSeleccionado = null;
      anularBotonLOV();
      listaVigenciasDiasTT = null;
      vigenciaDiaSeleccionado = null;
      contarRegistrosVD();
      RequestContext.getCurrentInstance().update("form:datosTTrabajadores");
      RequestContext.getCurrentInstance().update("form:datosVigenciasDTT");
   }

   // Eventos filtrar //
   public void eventoFiltrarTT() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
//        perderSeleccionTT();
      anularBotonLOV();
      contarRegistrosTT();
   }

   public void eventoFiltrarVD() {
      if (tipoListaVD == 0) {
         tipoListaVD = 1;
      }
//        vigenciaDiaSeleccionado = null;
      anularBotonLOV();
      contarRegistrosVD();
   }

   public void contarRegistrosTT() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosVD() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroDias");
   }

   public void contarRegistrosLovTT() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroTTra");
   }

   public void contarRegistrosLovTCotiz() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroTCot");
   }

   public void recordarSeleccionTT() {
      if (tipoTrabajadorSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaTT = (DataTable) c.getViewRoot().findComponent("form:datosTTrabajadores");
         tablaTT.setSelection(tipoTrabajadorSeleccionado);
      } else {
         RequestContext.getCurrentInstance().execute("PF('datosTTrabajadores').unselectAllRows()");
      }
   }

   public void recordarSeleccionVD() {
      if (vigenciaDiaSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaVD = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasDTT");
         tablaVD.setSelection(vigenciaDiaSeleccionado);
         log.info("vigenciaDiaSeleccionado: " + vigenciaDiaSeleccionado);
      }
   }

   public void exportPDF() throws IOException {
      FacesContext context = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) context.getViewRoot().findComponent("formExportar:datosTTrabajadoresExportar");
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TiposTrabajadoresPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      FacesContext context = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) context.getViewRoot().findComponent("formExportar:datosTTrabajadoresExportar");
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TiposTrabajadoresXLS", false, false, "UTF-8", null, null);
   }

   //GET'S y SET'S
   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getAltoTablaVD() {
      return altoTablaVD;
   }

   public void setAltoTablaVD(String altoTablaVD) {
      this.altoTablaVD = altoTablaVD;
   }

   public TiposTrabajadores getDuplicarTT() {
      return duplicarTT;
   }

   public void setDuplicarTT(TiposTrabajadores duplicarTT) {
      this.duplicarTT = duplicarTT;
   }

   public TiposTrabajadores getEditarTT() {
      return editarTT;
   }

   public void setEditarTT(TiposTrabajadores editarTT) {
      this.editarTT = editarTT;
   }

   public List<TiposCotizantes> getFiltrarLovTiposCotizantes() {
      return filtrarLovTiposCotizantes;
   }

   public void setFiltrarLovTiposCotizantes(List<TiposCotizantes> filtrarLovTiposCotizantes) {
      this.filtrarLovTiposCotizantes = filtrarLovTiposCotizantes;
   }

   public List<TiposTrabajadores> getFiltrarTiposTrabajadores() {
      return filtrarTiposTrabajadores;
   }

   public void setFiltrarTiposTrabajadores(List<TiposTrabajadores> filtrarTiposTrabajadores) {
      this.filtrarTiposTrabajadores = filtrarTiposTrabajadores;
   }

   public List<TiposTrabajadores> getListaTiposTrabajadores() {
      if (listaTiposTrabajadores == null) {
         listaTiposTrabajadores = administrarTiposTrabajadores.buscarTiposTrabajadoresTT();
         if (listaTiposTrabajadores != null) {
            if (!listaTiposTrabajadores.isEmpty()) {
               for (TiposTrabajadores recTipoT : listaTiposTrabajadores) {
                  if (recTipoT.getTipocotizante() == null) {
                     recTipoT.setTipocotizante(new TiposCotizantes());
                  }
               }
            }
         }
      }
      return listaTiposTrabajadores;
   }

   public void setListaTiposTrabajadores(List<TiposTrabajadores> listaTiposTrabajadores) {
      this.listaTiposTrabajadores = listaTiposTrabajadores;
   }

   public List<TiposCotizantes> getLovTiposCotizantes() {
      if (lovTiposCotizantes == null) {
         lovTiposCotizantes = administrarTiposCotizantes.tiposCotizantes();
      }
      return lovTiposCotizantes;
   }

   public void setLovTiposCotizantes(List<TiposCotizantes> lovTiposCotizantes) {
      this.lovTiposCotizantes = lovTiposCotizantes;
   }

   public TiposCotizantes getTipoCotizanteLovSeleccionado() {
      return tipoCotizanteLovSeleccionado;
   }

   public void setTipoCotizanteLovSeleccionado(TiposCotizantes tipoCotizanteLovSeleccionado) {
      this.tipoCotizanteLovSeleccionado = tipoCotizanteLovSeleccionado;
   }

   public TiposTrabajadores getTipoTrabajadorSeleccionado() {
      return tipoTrabajadorSeleccionado;
   }

   public void setTipoTrabajadorSeleccionado(TiposTrabajadores tipoTrabajadorSeleccionado) {
      this.tipoTrabajadorSeleccionado = tipoTrabajadorSeleccionado;
   }

   public List<VigenciasDiasTT> getListaVigenciasDiasTT() {
      return listaVigenciasDiasTT;
   }

   public void setListaVigenciasDiasTT(List<VigenciasDiasTT> listaVigenciasDiasTT) {
      this.listaVigenciasDiasTT = listaVigenciasDiasTT;
   }

   public VigenciasDiasTT getVigenciaDiaSeleccionado() {
      return vigenciaDiaSeleccionado;
   }

   public void setVigenciaDiaSeleccionado(VigenciasDiasTT vigenciaDiaSeleccionado) {
      this.vigenciaDiaSeleccionado = vigenciaDiaSeleccionado;
   }

   public List<VigenciasDiasTT> getFiltrarVigenciasDiasTT() {
      return filtrarVigenciasDiasTT;
   }

   public void setFiltrarVigenciasDiasTT(List<VigenciasDiasTT> filtrarVigenciasDiasTT) {
      this.filtrarVigenciasDiasTT = filtrarVigenciasDiasTT;
   }

   public String getInfoRegistroVD() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasDTT");
      infoRegistroVD = String.valueOf(tabla.getRowCount());
      return infoRegistroVD;
   }

   public String getInfoRegistroLovTT() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposTra");
      infoRegistroLovTT = String.valueOf(tabla.getRowCount());
      return infoRegistroLovTT;
   }

   public String getInfoRegistroTC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposCot");
      infoRegistroTC = String.valueOf(tabla.getRowCount());
      return infoRegistroTC;
   }

   public String getInfoRegistroTT() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTTrabajadores");
      infoRegistroTT = String.valueOf(tabla.getRowCount());
      return infoRegistroTT;
   }

   public void setInfoRegistroVD(String infoRegistroVD) {
      this.infoRegistroVD = infoRegistroVD;
   }

   public void setInfoRegistroTC(String infoRegistroTC) {
      this.infoRegistroTC = infoRegistroTC;
   }

   public void setInfoRegistroTT(String infoRegistroTT) {
      this.infoRegistroTT = infoRegistroTT;
   }

   public void setInfoRegistroLovTT(String infoRegistroLovTT) {
      this.infoRegistroLovTT = infoRegistroLovTT;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

   public List<TiposTrabajadores> getLovTiposTrabajadores() {
      return lovTiposTrabajadores;
   }

   public void setLovTiposTrabajadores(List<TiposTrabajadores> lovTiposTrabajadores) {
      this.lovTiposTrabajadores = lovTiposTrabajadores;
   }

   public TiposTrabajadores getNuevoTipoT() {
      return nuevoTipoT;
   }

   public List<TiposTrabajadores> getFiltrarLovTiposTrabajadores() {
      return filtrarLovTiposTrabajadores;
   }

   public void setFiltrarLovTiposTrabajadores(List<TiposTrabajadores> filtrarLovTiposTrabajadores) {
      this.filtrarLovTiposTrabajadores = filtrarLovTiposTrabajadores;
   }

   public TiposTrabajadores getTipoTrabajadorLovSeleccionado() {
      return tipoTrabajadorLovSeleccionado;
   }

   public void setTipoTrabajadorLovSeleccionado(TiposTrabajadores tipoTrabajadorLovSeleccionado) {
      this.tipoTrabajadorLovSeleccionado = tipoTrabajadorLovSeleccionado;
   }

   public void setNuevoTipoT(TiposTrabajadores nuevoTipoT) {
      this.nuevoTipoT = nuevoTipoT;
   }

   public boolean isMostrartodos() {
      return mostrartodos;
   }

   public void setMostrartodos(boolean mostrartodos) {
      this.mostrartodos = mostrartodos;
   }

   public String getMsnConfirmarRastro() {
      return msnConfirmarRastro;
   }

   public void setMsnConfirmarRastro(String msnConfirmarRastro) {
      this.msnConfirmarRastro = msnConfirmarRastro;
   }

   public String getNombreTablaRastro() {
      return nombreTablaRastro;
   }

   public void setNombreTablaRastro(String nombreTablaRastro) {
      this.nombreTablaRastro = nombreTablaRastro;
   }

   public String getMsnConfirmarRastroHistorico() {
      return msnConfirmarRastroHistorico;
   }

   public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
      this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
   }

   public VigenciasDiasTT getDuplicarVD() {
      return duplicarVD;
   }

   public void setDuplicarVD(VigenciasDiasTT duplicarVD) {
      this.duplicarVD = duplicarVD;
   }

   public VigenciasDiasTT getEditarVD() {
      return editarVD;
   }

   public void setEditarVD(VigenciasDiasTT editarVD) {
      this.editarVD = editarVD;
   }

   public VigenciasDiasTT getNuevaVD() {
      return nuevaVD;
   }

   public void setNuevaVD(VigenciasDiasTT nuevaVD) {
      this.nuevaVD = nuevaVD;
   }

   public Short getCodigoNuevoClonado() {
      return codigoNuevoClonado;
   }

   public void setCodigoNuevoClonado(Short codigoNuevoClonado) {
      this.codigoNuevoClonado = codigoNuevoClonado;
   }

   public String getNombreNuevoClonado() {
      return nombreNuevoClonado;
   }

   public void setNombreNuevoClonado(String nombreNuevoClonado) {
      this.nombreNuevoClonado = nombreNuevoClonado;
   }

   public TiposTrabajadores getTipoTrabajadorAClonar() {
      return tipoTrabajadorAClonar;
   }

   public void setTipoTrabajadorAClonar(TiposTrabajadores tipoTrabajadorAClonar) {
      this.tipoTrabajadorAClonar = tipoTrabajadorAClonar;
   }

   public String getAltoTablaReg() {
      if (altoTabla == "167") {
         altoTablaReg = "" + 7;
      } else {
         bandera = 0;
         altoTablaReg = "" + 8;
      }
      return altoTablaReg;
   }

   public void setAltoTablaReg(String altoTablaReg) {
      this.altoTablaReg = altoTablaReg;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

}
