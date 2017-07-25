/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.GruposTiposCC;
import Entidades.TiposCentrosCostos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposCentrosCostosInterface;
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
public class ControlTiposCentrosCostos implements Serializable {

   private static Logger log = Logger.getLogger(ControlTiposCentrosCostos.class);

   @EJB
   AdministrarTiposCentrosCostosInterface administrarTiposCentrosCostos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<TiposCentrosCostos> listTiposCentrosCostos;
   private List<TiposCentrosCostos> filtrarTiposCentrosCostos;
   private List<TiposCentrosCostos> crearTiposCentrosCostos;
   private List<TiposCentrosCostos> modificarTiposCentrosCostos;
   private List<TiposCentrosCostos> borrarTiposCentrosCostos;
   private TiposCentrosCostos nuevoTipoCentroCosto;
   private TiposCentrosCostos duplicarTipoCentroCosto;
   private TiposCentrosCostos editarTipoCentroCosto;
   private TiposCentrosCostos tipoCentroCostoSeleccionado;
   //lov gruposTiposCC
   private GruposTiposCC grupoTipoCCSeleccionada;
   private List<GruposTiposCC> filtradoGruposTiposCC;
   private List<GruposTiposCC> lovGruposTiposCC;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   String grupoTipoCCAutoCompletar;
   String nuevoGrupoTipoCCAutoCompletar;
   //RASTRO
   private Column codigo, nombre, grupoTipoCC;
   //borrado
   private BigInteger borradoCC;
   private BigInteger borradoVC;
   private BigInteger borradoRP;
   private int registrosBorrados;
   private String mensajeValidacion;
   private String infoRegistro;
   private String infoRegistroTiposCentrosCostos;
   private int tamano;
   private boolean activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   /**
    * Creates a new instance of ControlTiposCentrosCostos
    */
   public ControlTiposCentrosCostos() {
      listTiposCentrosCostos = null;
      crearTiposCentrosCostos = new ArrayList<TiposCentrosCostos>();
      modificarTiposCentrosCostos = new ArrayList<TiposCentrosCostos>();
      borrarTiposCentrosCostos = new ArrayList<TiposCentrosCostos>();
      lovGruposTiposCC = null;
      permitirIndex = true;
      editarTipoCentroCosto = new TiposCentrosCostos();
      nuevoTipoCentroCosto = new TiposCentrosCostos();
      nuevoTipoCentroCosto.setGrupotipocc(new GruposTiposCC());
      duplicarTipoCentroCosto = new TiposCentrosCostos();
      duplicarTipoCentroCosto.setGrupotipocc(new GruposTiposCC());
      guardado = true;
      aceptar = true;
      tamano = 330;
      activarLov = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovGruposTiposCC = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTiposCentrosCostos.obtenerConexion(ses.getId());
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
       */
      String pagActual = "tipocentrocosto";

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

   public void recibirPaginaAnterior(String pagina) {
      paginaAnterior = pagina;
   }

   public String redirigirPaginaAnterior() {
      return paginaAnterior;
   }

   public void cambiarIndice(TiposCentrosCostos tipocc, int celda) {
      tipoCentroCostoSeleccionado = tipocc;
      cualCelda = celda;
      tipoCentroCostoSeleccionado.getSecuencia();
      deshabilitarBotonLov();

      if (cualCelda == 0) {
         tipoCentroCostoSeleccionado.getCodigo();
         deshabilitarBotonLov();
      }
      if (cualCelda == 1) {
         deshabilitarBotonLov();
         tipoCentroCostoSeleccionado.getNombre();
      }
      if (cualCelda == 2) {
         habilitarBotonLov();
         tipoCentroCostoSeleccionado.getGrupotipocc().getDescripcion();
      }
   }

   public void asignarIndex(TiposCentrosCostos tipocc, int LND, int dig) {
      tipoCentroCostoSeleccionado = tipocc;
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = LND;

      if (dig == 2) {
         contarRegistrosGrupo();
         RequestContext.getCurrentInstance().update("form:gruposTiposCentrosCostosDialogo");
         RequestContext.getCurrentInstance().execute("PF('gruposTiposCentrosCostosDialogo').show()");
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (tipoCentroCostoSeleccionado != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 2) {
            contarRegistrosGrupo();
            RequestContext.getCurrentInstance().update("form:gruposTiposCentrosCostosDialogo");
            RequestContext.getCurrentInstance().execute("PF('gruposTiposCentrosCostosDialogo').show()");
            tipoActualizacion = 0;
         }

      }
   }

   public void actualizarGrupoTipoCC() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         tipoCentroCostoSeleccionado.setGrupotipocc(grupoTipoCCSeleccionada);

         if (!crearTiposCentrosCostos.contains(tipoCentroCostoSeleccionado)) {
            if (modificarTiposCentrosCostos.isEmpty()) {
               modificarTiposCentrosCostos.add(tipoCentroCostoSeleccionado);
            } else if (!modificarTiposCentrosCostos.contains(tipoCentroCostoSeleccionado)) {
               modificarTiposCentrosCostos.add(tipoCentroCostoSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         nuevoTipoCentroCosto.setGrupotipocc(grupoTipoCCSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoCentroCosto");
      } else if (tipoActualizacion == 2) {
         duplicarTipoCentroCosto.setGrupotipocc(grupoTipoCCSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTiposCentrosCostos");
      }
      filtradoGruposTiposCC = null;
      grupoTipoCCSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext.getCurrentInstance().update("form:gruposTiposCentrosCostosDialogo");
      RequestContext.getCurrentInstance().update("form:lovGruposTiposCC");
      RequestContext.getCurrentInstance().update("form:aceptarGTCC");
      context.reset("form:lovGruposTiposCC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGruposTiposCC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('gruposTiposCentrosCostosDialogo').hide()");
   }

   public void cancelarCambioGrupoTipoCC() {
      filtradoGruposTiposCC = null;
      grupoTipoCCSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:gruposTiposCentrosCostosDialogo");
      RequestContext.getCurrentInstance().update("form:lovGruposTiposCC");
      RequestContext.getCurrentInstance().update("form:aceptarGTCC");
      context.reset("form:lovGruposTiposCC:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovGruposTiposCC').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('gruposTiposCentrosCostosDialogo').hide()");
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         codigo = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         grupoTipoCC = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:grupoTipoCC");
         grupoTipoCC.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
         bandera = 0;
         filtrarTiposCentrosCostos = null;
         tipoLista = 0;
      }
      borrarTiposCentrosCostos.clear();
      crearTiposCentrosCostos.clear();
      modificarTiposCentrosCostos.clear();
      tipoCentroCostoSeleccionado = null;
      k = 0;
      listTiposCentrosCostos = null;
      guardado = true;
      permitirIndex = true;
      getListTiposCentrosCostos();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         codigo = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         grupoTipoCC = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:grupoTipoCC");
         grupoTipoCC.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
         bandera = 0;
         filtrarTiposCentrosCostos = null;
         tipoLista = 0;
      }
      borrarTiposCentrosCostos.clear();
      crearTiposCentrosCostos.clear();
      modificarTiposCentrosCostos.clear();
      tipoCentroCostoSeleccionado = null;
      k = 0;
      listTiposCentrosCostos = null;
      guardado = true;
      permitirIndex = true;
      getListTiposCentrosCostos();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void modificarTipoCentroCosto(TiposCentrosCostos tipocc) {
      tipoCentroCostoSeleccionado = tipocc;
      if (modificarTiposCentrosCostos.isEmpty()) {
         modificarTiposCentrosCostos.add(tipoCentroCostoSeleccionado);
      } else if (!modificarTiposCentrosCostos.contains(tipoCentroCostoSeleccionado)) {
         modificarTiposCentrosCostos.add(tipoCentroCostoSeleccionado);
      }
      guardado = false;

      RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 310;
         codigo = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:nombre");
         nombre.setFilterStyle("width: 85% !important;");
         grupoTipoCC = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:grupoTipoCC");
         grupoTipoCC.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         tamano = 330;
         log.info("Desactivar");
         codigo = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         grupoTipoCC = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:grupoTipoCC");
         grupoTipoCC.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
         bandera = 0;
         filtrarTiposCentrosCostos = null;
         tipoLista = 0;
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("GRUPOSTIPOSCC")) {
         if (tipoNuevo == 1) {
            nuevoTipoCentroCosto.getGrupotipocc().getDescripcion();
         } else if (tipoNuevo == 2) {
            duplicarTipoCentroCosto.getGrupotipocc().getDescripcion();
         }

      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("GRUPOSTIPOSCC")) {
         if (tipoNuevo == 1) {
            nuevoTipoCentroCosto.getGrupotipocc().setDescripcion(nuevoGrupoTipoCCAutoCompletar);
         } else if (tipoNuevo == 2) {
            duplicarTipoCentroCosto.getGrupotipocc().setDescripcion(nuevoGrupoTipoCCAutoCompletar);
         }
         for (int i = 0; i < lovGruposTiposCC.size(); i++) {
            if (lovGruposTiposCC.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoTipoCentroCosto.setGrupotipocc(lovGruposTiposCC.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoGrupoTipoCC");
            } else if (tipoNuevo == 2) {
               duplicarTipoCentroCosto.setGrupotipocc(lovGruposTiposCC.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGrupoTipoCentroCosto");
            }
            lovGruposTiposCC.clear();
            lovGruposTiposCC = null;
            getLovGruposTiposCC();
         } else {
            RequestContext.getCurrentInstance().update("form:gruposTiposCentrosCostosDialogo");
            RequestContext.getCurrentInstance().execute("PF('gruposTiposCentrosCostosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoGrupoTipoCC");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGrupoTipoCentroCosto");
            }
         }
      }
   }

   public void agregarNuevoTipoCentroCosto() {
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoTipoCentroCosto.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listTiposCentrosCostos.size(); x++) {
            if (listTiposCentrosCostos.get(x).getCodigo() == nuevoTipoCentroCosto.getCodigo()) {
               duplicados++;
            }
         }

         if (duplicados > 0) {
            mensajeValidacion = " Existe un registro con el código ingresado. Por favor ingrese un código válido \n";
         } else {
            log.info("bandera");
            contador++;
         }
      }
      if (nuevoTipoCentroCosto.getNombre() == (null)) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

      } else {
         contador++;

      }
      if (nuevoTipoCentroCosto.getGrupotipocc().getSecuencia() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

      } else {
         contador++;
         duplicados = 0;

         if (contador == 3) {
            if (bandera == 1) {
               FacesContext c = FacesContext.getCurrentInstance();
               //CERRAR FILTRADO
               log.info("Desactivar");
               codigo = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:codigo");
               codigo.setFilterStyle("display: none; visibility: hidden;");
               nombre = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:nombre");
               nombre.setFilterStyle("display: none; visibility: hidden;");
               grupoTipoCC = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:grupoTipoCC");
               grupoTipoCC.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
               bandera = 0;
               filtrarTiposCentrosCostos = null;
               tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoCentroCosto.setSecuencia(l);
            crearTiposCentrosCostos.add(nuevoTipoCentroCosto);
            listTiposCentrosCostos.add(nuevoTipoCentroCosto);
            tipoCentroCostoSeleccionado = nuevoTipoCentroCosto;
            nuevoTipoCentroCosto = new TiposCentrosCostos();
            nuevoTipoCentroCosto.setGrupotipocc(new GruposTiposCC());
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTipoCentroCosto').hide()");
         } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
         }
      }
   }

   public void limpiarNuevoTipoCentroCosto() {
      nuevoTipoCentroCosto = new TiposCentrosCostos();
      nuevoTipoCentroCosto.setGrupotipocc(new GruposTiposCC());

   }

   public void guardarCambiosTiposCentroCosto() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         if (!borrarTiposCentrosCostos.isEmpty()) {
            administrarTiposCentrosCostos.borrarTiposCentrosCostos(borrarTiposCentrosCostos);
            registrosBorrados = borrarTiposCentrosCostos.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarTiposCentrosCostos.clear();
         }
         if (!crearTiposCentrosCostos.isEmpty()) {
            administrarTiposCentrosCostos.crearTiposCentrosCostos(crearTiposCentrosCostos);

            crearTiposCentrosCostos.clear();
         }
         if (!modificarTiposCentrosCostos.isEmpty()) {
            for (int i = 0; i < modificarTiposCentrosCostos.size(); i++) {
               if (modificarTiposCentrosCostos.get(i).getGrupotipocc().getSecuencia() == null) {
                  modificarTiposCentrosCostos.get(i).setGrupotipocc(null);
               }
            }
            administrarTiposCentrosCostos.modificarTipoCentrosCostos(modificarTiposCentrosCostos);
            modificarTiposCentrosCostos.clear();
         }
         listTiposCentrosCostos = null;
         RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         k = 0;
         guardado = true;
      }
      tipoCentroCostoSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void duplicarTiposCentrosCostos() {
      if (tipoCentroCostoSeleccionado != null) {
         duplicarTipoCentroCosto = new TiposCentrosCostos();
         if (tipoLista == 0) {
            duplicarTipoCentroCosto.setCodigo(tipoCentroCostoSeleccionado.getCodigo());
            duplicarTipoCentroCosto.setGrupotipocc(tipoCentroCostoSeleccionado.getGrupotipocc());
            duplicarTipoCentroCosto.setNombre(tipoCentroCostoSeleccionado.getNombre());
         }
         if (tipoLista == 1) {
            duplicarTipoCentroCosto.setCodigo(tipoCentroCostoSeleccionado.getCodigo());
            duplicarTipoCentroCosto.setGrupotipocc(tipoCentroCostoSeleccionado.getGrupotipocc());
            duplicarTipoCentroCosto.setNombre(tipoCentroCostoSeleccionado.getNombre());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTiposCentrosCostos");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCentrosCostos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;

      if (duplicarTipoCentroCosto.getCodigo() == a) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         for (int x = 0; x < listTiposCentrosCostos.size(); x++) {
            if (listTiposCentrosCostos.get(x).getCodigo() == duplicarTipoCentroCosto.getCodigo()) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido \n";
         } else {
            log.info("bandera");
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarTipoCentroCosto.getNombre() == null || duplicarTipoCentroCosto.getNombre().isEmpty()) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

      } else {
         log.info("Bandera : ");
         contador++;
      }
      if (duplicarTipoCentroCosto.getGrupotipocc().getDescripcion() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         contador++;
         log.info("Bandera : ");
      }

      if (contador == 3) {
         k++;
         l = BigInteger.valueOf(k);
         duplicarTipoCentroCosto.setSecuencia(l);
         log.info("Datos Duplicando: " + duplicarTipoCentroCosto.getSecuencia() + "  " + duplicarTipoCentroCosto.getCodigo());
         if (crearTiposCentrosCostos.contains(duplicarTipoCentroCosto)) {
            log.info("Ya lo contengo.");
         }
         listTiposCentrosCostos.add(duplicarTipoCentroCosto);
         crearTiposCentrosCostos.add(duplicarTipoCentroCosto);
         RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
         tipoCentroCostoSeleccionado = duplicarTipoCentroCosto;
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            grupoTipoCC = (Column) c.getViewRoot().findComponent("form:datosTipoCentroCosto:grupoTipoCC");
            grupoTipoCC.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
            bandera = 0;
            filtrarTiposCentrosCostos = null;
            tipoLista = 0;
         }
         duplicarTipoCentroCosto = new TiposCentrosCostos();
         duplicarTipoCentroCosto.setGrupotipocc(new GruposTiposCC());
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCentrosCostos').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarduplicarTiposCentrosCostos() {
      duplicarTipoCentroCosto = new TiposCentrosCostos();
      duplicarTipoCentroCosto.setGrupotipocc(new GruposTiposCC());
   }

   public void editarCelda() {
      if (tipoCentroCostoSeleccionado != null) {
         editarTipoCentroCosto = tipoCentroCostoSeleccionado;

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
            RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
            RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editGrupoTipoCC");
            RequestContext.getCurrentInstance().execute("PF('editGrupoTipoCC').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoCentroCostoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TiposCentrosCostosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoCentroCostoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TiposCentrosCostosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarBorrado() {
      try {
         if (tipoLista == 0) {
            borradoCC = administrarTiposCentrosCostos.contarCentrosCostosTipoCentroCosto(tipoCentroCostoSeleccionado.getSecuencia());
            borradoVC = administrarTiposCentrosCostos.contarVigenciasCuentasTipoCentroCosto(tipoCentroCostoSeleccionado.getSecuencia());
            borradoRP = administrarTiposCentrosCostos.contarRiesgosProfesionalesTipoCentroCosto(tipoCentroCostoSeleccionado.getSecuencia());
         } else {
            borradoCC = administrarTiposCentrosCostos.contarCentrosCostosTipoCentroCosto(tipoCentroCostoSeleccionado.getSecuencia());
            borradoVC = administrarTiposCentrosCostos.contarVigenciasCuentasTipoCentroCosto(tipoCentroCostoSeleccionado.getSecuencia());
            borradoRP = administrarTiposCentrosCostos.contarRiesgosProfesionalesTipoCentroCosto(tipoCentroCostoSeleccionado.getSecuencia());
         }
         if (borradoCC.equals(new BigInteger("0")) && borradoVC.equals(new BigInteger("0")) && borradoRP.equals(new BigInteger("0"))) {
            borrarTiposCentrosCostos();
         } else {

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            tipoCentroCostoSeleccionado = null;
            borradoCC = new BigInteger("-1");
            borradoVC = new BigInteger("-1");
            borradoRP = new BigInteger("-1");
         }

      } catch (Exception e) {
         log.error("ERROR ControlTiposEntidades verificarBorrado ERROR " + e);
      }
   }

   public void borrarTiposCentrosCostos() {

      if (tipoCentroCostoSeleccionado != null) {

         log.info("Entro a borrarTiposCentrosCostos");
         if (!modificarTiposCentrosCostos.isEmpty() && modificarTiposCentrosCostos.contains(tipoCentroCostoSeleccionado)) {
            int modIndex = modificarTiposCentrosCostos.indexOf(tipoCentroCostoSeleccionado);
            modificarTiposCentrosCostos.remove(modIndex);
            borrarTiposCentrosCostos.add(tipoCentroCostoSeleccionado);
         } else if (!crearTiposCentrosCostos.isEmpty() && crearTiposCentrosCostos.contains(tipoCentroCostoSeleccionado)) {
            int crearIndex = crearTiposCentrosCostos.indexOf(tipoCentroCostoSeleccionado);
            crearTiposCentrosCostos.remove(crearIndex);
         } else {
            borrarTiposCentrosCostos.add(tipoCentroCostoSeleccionado);
         }
         listTiposCentrosCostos.remove(tipoCentroCostoSeleccionado);
         if (tipoLista == 1) {
            filtrarTiposCentrosCostos.remove(tipoCentroCostoSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosTipoCentroCosto");
         tipoCentroCostoSeleccionado = null;

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }

   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoCentroCostoSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(tipoCentroCostoSeleccionado.getSecuencia(), "TIPOSCENTROSCOSTOS");
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSCENTROSCOSTOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void eventoFiltrar() {
      try {
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         contarRegistros();
      } catch (Exception e) {
         log.warn("Error ControlTiposCentrosCostos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosGrupo() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTiposCentrosCostos");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

//------------------------------------------------------------------------------
   public List<TiposCentrosCostos> getListTiposCentrosCostos() {
      if (listTiposCentrosCostos == null) {
         listTiposCentrosCostos = administrarTiposCentrosCostos.consultarTiposCentrosCostos();
      }
      return listTiposCentrosCostos;
   }

   public void setListTiposCentrosCostos(List<TiposCentrosCostos> listTiposCentrosCostos) {
      this.listTiposCentrosCostos = listTiposCentrosCostos;
   }

   public List<TiposCentrosCostos> getFiltrarTiposCentrosCostos() {
      return filtrarTiposCentrosCostos;
   }

   public void setFiltrarTiposCentrosCostos(List<TiposCentrosCostos> filtrarTiposCentrosCostos) {
      this.filtrarTiposCentrosCostos = filtrarTiposCentrosCostos;
   }

   public GruposTiposCC getGrupoTipoCCSeleccionada() {
      return grupoTipoCCSeleccionada;
   }

   public void setGrupoTipoCCSeleccionada(GruposTiposCC grupoTipoCCSeleccionada) {
      this.grupoTipoCCSeleccionada = grupoTipoCCSeleccionada;
   }

   public List<GruposTiposCC> getLovGruposTiposCC() {
      if (lovGruposTiposCC == null) {
         lovGruposTiposCC = administrarTiposCentrosCostos.consultarLOVGruposTiposCentrosCostos();
      }
      return lovGruposTiposCC;
   }

   public void setLovGruposTiposCC(List<GruposTiposCC> listaGruposTiposCCD) {
      this.lovGruposTiposCC = listaGruposTiposCCD;
   }

   public List<GruposTiposCC> getFiltradoGruposTiposCC() {
      return filtradoGruposTiposCC;
   }

   public void setFiltradoGruposTiposCC(List<GruposTiposCC> filtradoGruposTiposCC) {
      this.filtradoGruposTiposCC = filtradoGruposTiposCC;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public TiposCentrosCostos getNuevoTipoCentroCosto() {
      return nuevoTipoCentroCosto;
   }

   public void setNuevoTipoCentroCosto(TiposCentrosCostos nuevoTipoCentroCosto) {
      this.nuevoTipoCentroCosto = nuevoTipoCentroCosto;
   }

   public TiposCentrosCostos getDuplicarTipoCentroCosto() {
      return duplicarTipoCentroCosto;
   }

   public void setDuplicarTipoCentroCosto(TiposCentrosCostos duplicarTipoCentroCosto) {
      this.duplicarTipoCentroCosto = duplicarTipoCentroCosto;
   }

   public TiposCentrosCostos getEditarTipoCentroCosto() {
      return editarTipoCentroCosto;
   }

   public void setEditarTipoCentroCosto(TiposCentrosCostos editarTipoCentroCosto) {
      this.editarTipoCentroCosto = editarTipoCentroCosto;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public TiposCentrosCostos getTipoCentroCostoSeleccionado() {
      return tipoCentroCostoSeleccionado;
   }

   public void setTipoCentroCostoSeleccionado(TiposCentrosCostos tipoCentroCostoSeleccionado) {
      this.tipoCentroCostoSeleccionado = tipoCentroCostoSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTipoCentroCosto");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroTiposCentrosCostos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovGruposTiposCC");
      infoRegistroTiposCentrosCostos = String.valueOf(tabla.getRowCount());
      return infoRegistroTiposCentrosCostos;
   }

   public void setInfoRegistroTiposCentrosCostos(String infoRegistroTiposCentrosCostos) {
      this.infoRegistroTiposCentrosCostos = infoRegistroTiposCentrosCostos;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }
}
