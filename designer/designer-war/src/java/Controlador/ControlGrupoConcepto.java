/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Conceptos;
import Entidades.GruposConceptos;
import Entidades.VigenciasGruposConceptos;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGruposConceptosInterface;
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
import ControlNavegacion.ListasRecurrentes;
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
public class ControlGrupoConcepto implements Serializable {

   private static Logger log = Logger.getLogger(ControlGrupoConcepto.class);

   @EJB
   AdministrarGruposConceptosInterface administrarGruposConceptos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //Lista Grupos Conceptos(Arriba)
   private List<GruposConceptos> listaGruposConceptos;
   private List<GruposConceptos> filtradoListaGruposConceptos;
   private GruposConceptos grupoConceptoSeleccionado;
   //Lista Vigencias Grupos Conceptos
   private List<VigenciasGruposConceptos> listaVigenciasGruposConceptos;
   private List<VigenciasGruposConceptos> filtradoListaVigenciasGruposConceptos;
   private VigenciasGruposConceptos vigenciaGrupoCSeleccionado;
   //LOVLista Conceptos(Abajo)
   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtrarLovConceptos;
   private Conceptos conceptoLovSeleccionado;
   //LOV GRUPOS CONCEPTOS
   private List<GruposConceptos> lovGruposConceptos;
   private List<GruposConceptos> filtrarlovGruposConceptos;
   private GruposConceptos gruposSeleccionado;

   private String infoRegistro, infoRegistroVigencias, infoRegistroLOVConceptos, infoRegistroLOVGruposC;
   //REGISTRO ACTUAL
   private int tablaActual;
   //OTROS
   private boolean aceptar, mostrarTodos;
   private String tamano;
   //Crear Vigencias VigenciasGruposConceptos (Arriba)
   private List<GruposConceptos> listaGruposConceptosCrear;
   public GruposConceptos nuevoGruposConceptos;
   public GruposConceptos duplicarGruposConceptos;
   private int k;
   private BigInteger l;
   private String mensajeValidacion;
   //Modificar Vigencias VigenciasGruposConceptos
   private List<GruposConceptos> listaGruposConceptosModificar;
   //Borrar Vigencias VigenciasGruposConceptos
   private List<GruposConceptos> listaGruposConceptosBorrar;
   //Crear VigenciasGruposConceptos (Abajo)
   private List<VigenciasGruposConceptos> listaVigenciasGruposConceptosCrear;
   public VigenciasGruposConceptos duplicarVigenciaGruposConceptos;
   //Modificar VigenciasGruposConceptos
   private List<VigenciasGruposConceptos> listaVigenciasGruposConceptosModificar;
   //Borrar VigenciasGruposConceptos
   private List<VigenciasGruposConceptos> listaVigenciasGruposConceptosBorrar;
   public VigenciasGruposConceptos nuevoVigenciasGruposConceptos;
   //OTROS
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   private boolean permitirIndex;
   //editar celda
   private GruposConceptos editarGruposConceptos;
   private VigenciasGruposConceptos editarVigenciasGruposConceptos;
   private int cualCelda, tipoLista;
   private int cualCeldaD;
   private int tipoListaD;
   //Autocompletar
   private String codigo, empresa;
   //RASTROS
   private boolean guardado;
   private boolean cambiosPagina;
   //FILTRADO
   private Column colCodigo, colDescripcion, colFundamental;
   private Column colCodigo2, colDescripcion2, colNaturaleza, colInicial, colFinal, colEmpresa;
   //Tabla a Imprimir
   private String tablaImprimir, nombreArchivo;
   //Sec Abajo Duplicar
   private int m;
   private BigInteger n;
   //SECUENCIA DE VIGENCIA
   private Date fechaParametro;
   private Date fechaInicial;
   private Date fechaFinal;
   private Integer cualTabla;

   private String paginaAnterior;
   private Map<String, Object> mapParametros;
   private boolean activarLov;
   private ListasRecurrentes listasRecurrentes;

   public ControlGrupoConcepto() {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      listasRecurrentes = controlListaNavegacion.getListasRecurrentes();
      permitirIndex = true;
      cualTabla = 0;
      bandera = 0;
      mostrarTodos = true;
      listaGruposConceptosBorrar = new ArrayList<GruposConceptos>();
      listaGruposConceptosCrear = new ArrayList<GruposConceptos>();
      listaGruposConceptosModificar = new ArrayList<GruposConceptos>();
      listaVigenciasGruposConceptosBorrar = new ArrayList<VigenciasGruposConceptos>();
      listaVigenciasGruposConceptosCrear = new ArrayList<VigenciasGruposConceptos>();
      listaVigenciasGruposConceptosModificar = new ArrayList<VigenciasGruposConceptos>();
      tablaImprimir = ":formExportar:datosGruposConceptosExportar";
      nombreArchivo = "GruposConceptosXML";
      //Crear GruposConceptos
      nuevoGruposConceptos = new GruposConceptos();
      nuevoGruposConceptos.setFundamental("N");
      duplicarGruposConceptos = new GruposConceptos();
      //Crear VigenciasGruposConceptos
      nuevoVigenciasGruposConceptos = new VigenciasGruposConceptos();
      nuevoVigenciasGruposConceptos.setConcepto(new Conceptos());
      duplicarVigenciaGruposConceptos = new VigenciasGruposConceptos();
      m = 0;
      cambiosPagina = true;
      activarLov = true;
      paginaAnterior = "nominaf";
      mapParametros = new LinkedHashMap<String, Object>();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovConceptos = null;
      lovGruposConceptos = null;
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
         administrarGruposConceptos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void seleccionarTipoNuevoFundamental(String estadoTipo, int tipoNuevo) {
      if (tipoNuevo == 1) {
         if (estadoTipo.equals("SI ES PERSONALIZABLE")) {
            nuevoGruposConceptos.setFundamental("S");
         } else if (estadoTipo.equals("NO ES PERSONALIZABLE")) {
            nuevoGruposConceptos.setFundamental("N");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoFundamental");
      } else {
         if (estadoTipo.equals("SI ES PERSONALIZABLE")) {
            duplicarGruposConceptos.setFundamental("S");
         } else if (estadoTipo.equals("NO ES PERSONALIZABLE")) {
            duplicarGruposConceptos.setFundamental("N");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFundamental");
      }
   }

   public void actualizarConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         vigenciaGrupoCSeleccionado.setConcepto(conceptoLovSeleccionado);
         if (!listaVigenciasGruposConceptosCrear.contains(vigenciaGrupoCSeleccionado)) {
            if (listaVigenciasGruposConceptosModificar.isEmpty()) {
               listaVigenciasGruposConceptosModificar.add(vigenciaGrupoCSeleccionado);
            } else if (!listaVigenciasGruposConceptosModificar.contains(vigenciaGrupoCSeleccionado)) {
               listaVigenciasGruposConceptosModificar.add(vigenciaGrupoCSeleccionado);
            }
         }

         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
      } else if (tipoActualizacion == 1) {
         nuevoVigenciasGruposConceptos.setConcepto(conceptoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigoV");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoDescripcionV");
      } else if (tipoActualizacion == 2) {
         duplicarVigenciaGruposConceptos.setConcepto(conceptoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoV");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionV");
      }
      filtrarLovConceptos = null;
      conceptoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
      //RequestContext.getCurrentInstance().update("formularioDialogos:LOVConceptos");
   }

   public void cancelarCambioConceptos() {
      filtrarLovConceptos = null;
      conceptoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   //CREAR Grupo Concepto
   public void agregarNuevoGrupoConcepto() {
      int pasa = 0;
      int pasar = 0;

      mensajeValidacion = new String();

      if (nuevoGruposConceptos.getCodigo() == null) {
         mensajeValidacion = mensajeValidacion + " * Codigo\n";
         pasa++;
      }
      if (nuevoGruposConceptos.getDescripcion().equals(" ")) {
         mensajeValidacion = mensajeValidacion + " * Descripcion\n";
         pasa++;
      }

      for (int i = 0; i < listaGruposConceptos.size(); i++) {
         if (nuevoGruposConceptos.getCodigo() == listaGruposConceptos.get(i).getCodigo()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:codigos");
            RequestContext.getCurrentInstance().execute("PF('codigos').show()");
            pasar++;
         }
      }

      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevo");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
      }

      if (pasa == 0 && pasar == 0) {
         if (bandera == 1) {
            restaurarTablas();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         k++;
         l = BigInteger.valueOf(k);
         nuevoGruposConceptos.setSecuencia(l);
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         listaGruposConceptosCrear.add(nuevoGruposConceptos);
         listaGruposConceptos.add(nuevoGruposConceptos);
         grupoConceptoSeleccionado = listaGruposConceptos.get(listaGruposConceptos.indexOf(nuevoGruposConceptos));
         RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
         contarRegistros();
         deshabilitarBotonLov();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroGruposConceptos').hide()");
         limpiarNuevoGruposConceptos();
      }
   }

   public boolean validarCamposNuevaVigencia(VigenciasGruposConceptos vigencia) {
      int pasa1 = 0;
      int pasa2 = 0;
      int pasa3 = 0;
      Date fechaCero = new Date(1, 1, 1);
      mensajeValidacion = new String();
      log.info("validarCamposNuevaVigencia fechaCero : " + fechaCero);
      if (vigencia.getConcepto().getSecuencia() == null) {
         mensajeValidacion = mensajeValidacion + " * Codigo\n";
         pasa1++;
      }
      if (vigencia.getFechainicial() == null || vigencia.getFechafinal() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial \n * Fecha Final \n";
         pasa1++;
      } else if (vigencia.getFechainicial().after(vigencia.getFechafinal())
              || vigencia.getFechainicial().equals(vigencia.getFechafinal())
              || vigencia.getFechainicial().before(fechaCero)) {
         log.info("vigencia.getFechainicial().after(vigencia.getFechafinal()) : " + vigencia.getFechainicial().after(vigencia.getFechafinal()));
         log.info("vigencia.getFechainicial().equals(vigencia.getFechafinal()) : " + vigencia.getFechainicial().equals(vigencia.getFechafinal()));
         log.info("vigencia.getFechainicial().before(fechaCero) : " + vigencia.getFechainicial().before(fechaCero));
         pasa2++;
      } else if (listaVigenciasGruposConceptos != null) {
         if (!listaVigenciasGruposConceptos.isEmpty()) {
            for (int i = 0; i < listaVigenciasGruposConceptos.size(); i++) {
               if (listaVigenciasGruposConceptos.get(i).getConcepto().getSecuencia().equals(vigencia.getConcepto().getSecuencia())) {
                  if ((listaVigenciasGruposConceptos.get(i).getFechafinal().after(vigencia.getFechafinal())
                          && listaVigenciasGruposConceptos.get(i).getFechainicial().before(vigencia.getFechafinal()))
                          || (listaVigenciasGruposConceptos.get(i).getFechainicial().before(vigencia.getFechainicial())
                          && listaVigenciasGruposConceptos.get(i).getFechafinal().after(vigencia.getFechainicial()))
                          || listaVigenciasGruposConceptos.get(i).getFechafinal().equals(vigencia.getFechafinal())
                          || listaVigenciasGruposConceptos.get(i).getFechainicial().equals(vigencia.getFechainicial())
                          || listaVigenciasGruposConceptos.get(i).getFechainicial().equals(vigencia.getFechafinal())
                          || listaVigenciasGruposConceptos.get(i).getFechafinal().equals(vigencia.getFechainicial())) {
                     pasa3++;
                  }
               }
            }
         }
      }
      if (pasa1 != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevo");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
      } else if (pasa2 != 0) {
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoFechas').show()");
      } else if (pasa3 != 0) {
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoRepetidos').show()");
      }
      if (pasa1 == 0 && pasa2 == 0 && pasa3 == 0) {
         return true;
      } else {
         return false;
      }
   }

   //CREAR Grupo Concepto
   public void agregarNuevoVigenciaGrupoConcepto() {
      if (validarCamposNuevaVigencia(nuevoVigenciasGruposConceptos)) {
         if (bandera == 1) {
            restaurarTablas();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         k++;
         l = BigInteger.valueOf(k);
         nuevoVigenciasGruposConceptos.setSecuencia(l);
         nuevoVigenciasGruposConceptos.setGrupoconcepto(grupoConceptoSeleccionado);
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         listaVigenciasGruposConceptos.add(nuevoVigenciasGruposConceptos);
         listaVigenciasGruposConceptosCrear.add(nuevoVigenciasGruposConceptos);
         vigenciaGrupoCSeleccionado = listaVigenciasGruposConceptos.get(listaVigenciasGruposConceptos.indexOf(nuevoVigenciasGruposConceptos));
         RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
         contarRegistrosVigencias();
         deshabilitarBotonLov();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasGruposConceptos').hide()");
         limpiarNuevoGruposConceptos();
      }
   }

   public void seleccionarFundamental(String estadoFundamental, GruposConceptos grupoC, int celda) {
      cualCelda = celda;
      grupoConceptoSeleccionado = grupoC;
      if (estadoFundamental.equals("SI ES PERSONALIZABLE")) {
         grupoConceptoSeleccionado.setFundamental("S");
      } else if (estadoFundamental.equals("NO ES PERSONALIZABLE")) {
         grupoConceptoSeleccionado.setFundamental("N");
      }

      if (!listaGruposConceptosCrear.contains(grupoConceptoSeleccionado)) {
         if (listaGruposConceptosModificar.isEmpty()) {
            listaGruposConceptosModificar.add(grupoConceptoSeleccionado);
         } else if (!listaGruposConceptosModificar.contains(grupoConceptoSeleccionado)) {
            listaGruposConceptosModificar.add(grupoConceptoSeleccionado);
         }
      }

      if (guardado == true) {
         guardado = false;
      }
      deshabilitarBotonLov();
      RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
      contarRegistros();
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      grupoConceptoSeleccionado = null;
      listaVigenciasGruposConceptos = null;
      RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
      contarRegistros();
      contarRegistrosVigencias();
      deshabilitarBotonLov();
   }

   //EVENTO FILTRAR
   public void eventoFiltrarD() {
      if (tipoListaD == 0) {
         tipoListaD = 1;
      }
      vigenciaGrupoCSeleccionado = null;
      contarRegistrosVigencias();
      deshabilitarBotonLov();
   }

   //Ubicacion Celda Arriba 
   public void cambiarVigencia() {
      if (listaGruposConceptosCrear.isEmpty() && listaGruposConceptosBorrar.isEmpty() && listaGruposConceptosModificar.isEmpty()) {
         listaVigenciasGruposConceptos = null;
         getListaVigenciasGruposConceptos();
         RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
         contarRegistrosVigencias();
         deshabilitarBotonLov();
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
         RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
      }
   }

   public void limpiarListas() {
      listaGruposConceptosCrear.clear();
      listaGruposConceptosBorrar.clear();
      listaGruposConceptosModificar.clear();
      RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
      contarRegistros();
   }

   //BORRAR 
   public void borrar() {

      if (grupoConceptoSeleccionado != null && cualTabla == 0) {
         if (listaVigenciasGruposConceptos.isEmpty()) {
            if (!listaGruposConceptosModificar.isEmpty() && listaGruposConceptosModificar.contains(grupoConceptoSeleccionado)) {
               listaGruposConceptosModificar.remove(grupoConceptoSeleccionado);
               listaGruposConceptosBorrar.add(grupoConceptoSeleccionado);
            } else if (!listaGruposConceptosCrear.isEmpty() && listaGruposConceptosCrear.contains(grupoConceptoSeleccionado)) {
               listaGruposConceptosCrear.remove(grupoConceptoSeleccionado);
            } else {
               listaGruposConceptosBorrar.add(grupoConceptoSeleccionado);
            }
            listaGruposConceptos.remove(grupoConceptoSeleccionado);

            if (tipoLista == 1) {
               filtradoListaGruposConceptos.remove(grupoConceptoSeleccionado);
            }
            RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
            contarRegistros();
            deshabilitarBotonLov();
            cambiosPagina = false;
            grupoConceptoSeleccionado = null;

            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            log.info("No se puede borrar porque tiene registros en la tabla de abajo");
            RequestContext.getCurrentInstance().update("formularioDialogos:registro");
            RequestContext.getCurrentInstance().execute("PF('registro').show()");
            deshabilitarBotonLov();
         }
      } else if (vigenciaGrupoCSeleccionado != null && cualTabla == 1) {

         if (!listaVigenciasGruposConceptosModificar.isEmpty() && listaVigenciasGruposConceptosModificar.contains(vigenciaGrupoCSeleccionado)) {
            listaVigenciasGruposConceptosModificar.remove(vigenciaGrupoCSeleccionado);
            listaVigenciasGruposConceptosBorrar.add(vigenciaGrupoCSeleccionado);
         } else if (!listaVigenciasGruposConceptosCrear.isEmpty() && listaVigenciasGruposConceptosCrear.contains(vigenciaGrupoCSeleccionado)) {
            listaVigenciasGruposConceptosCrear.remove(vigenciaGrupoCSeleccionado);
         } else {
            listaVigenciasGruposConceptosBorrar.add(vigenciaGrupoCSeleccionado);
         }
         listaVigenciasGruposConceptos.remove(vigenciaGrupoCSeleccionado);

         if (tipoListaD == 1) {
            filtradoListaVigenciasGruposConceptos.remove(vigenciaGrupoCSeleccionado);
         }

         RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
         contarRegistrosVigencias();
         deshabilitarBotonLov();
         vigenciaGrupoCSeleccionado = null;
         cambiosPagina = false;

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         deshabilitarBotonLov();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void guardarYSalir() {
      guardarTodo();
      salir();
   }

   public void guardarTodo() {
      if (guardado == false) {
         log.info("Realizando Operaciones Grupo Concepto");
         if (!listaGruposConceptosBorrar.isEmpty()) {
            for (int i = 0; i < listaGruposConceptosBorrar.size(); i++) {
               log.info("Borrando...");
               if (listaGruposConceptosBorrar.get(i).getFundamental() == null) {
                  listaGruposConceptosBorrar.get(i).setFundamental(null);
               }

               administrarGruposConceptos.borrarGruposConceptos(listaGruposConceptosBorrar.get(i));
               log.info("Entra");
               listaGruposConceptosBorrar.clear();
            }
         }
         if (!listaGruposConceptosCrear.isEmpty()) {
            for (int i = 0; i < listaGruposConceptosCrear.size(); i++) {
               log.info("Creando...");
               log.info(listaGruposConceptosCrear.size());
               if (listaGruposConceptosCrear.get(i).getFundamental() == null) {
                  listaGruposConceptosCrear.get(i).setFundamental(null);
               }
               administrarGruposConceptos.crearGruposConceptos(listaGruposConceptosCrear.get(i));
            }

            log.info("LimpiaLista");
            listaGruposConceptosCrear.clear();
         }
         if (!listaGruposConceptosModificar.isEmpty()) {
            administrarGruposConceptos.modificarGruposConceptos(listaGruposConceptosModificar);
            listaGruposConceptosModificar.clear();
         }

         log.info("Se guardaron los datos con exito");
         listaGruposConceptos = null;
         RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
         contarRegistros();
         guardado = true;
         permitirIndex = true;
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      log.info("Valor k: " + k);

      if (guardado == false) {
         log.info("Realizando Operaciones Vigencias");
         if (!listaVigenciasGruposConceptosBorrar.isEmpty()) {
            for (int i = 0; i < listaVigenciasGruposConceptosBorrar.size(); i++) {
               log.info("Borrando...");
               administrarGruposConceptos.borrarVigenciaGruposConceptos(listaVigenciasGruposConceptosBorrar.get(i));
            }
            log.info("Entra");
            listaVigenciasGruposConceptosBorrar.clear();
         }
      }
      if (!listaVigenciasGruposConceptosCrear.isEmpty()) {
         for (int i = 0; i < listaVigenciasGruposConceptosCrear.size(); i++) {
            log.info("Creando...");
            log.info(listaVigenciasGruposConceptosCrear.size());

            administrarGruposConceptos.crearVigenciaGruposConceptos(listaVigenciasGruposConceptosCrear.get(i));

         }
         listaVigenciasGruposConceptosCrear.clear();
      }
      if (!listaVigenciasGruposConceptosModificar.isEmpty()) {
         administrarGruposConceptos.modificarVigenciaGruposConceptos(listaVigenciasGruposConceptosModificar);

         listaVigenciasGruposConceptosModificar.clear();
      }
      grupoConceptoSeleccionado = null;
      log.info("Se guardaron los datos con exito");
      listaVigenciasGruposConceptos = null;
      FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
      FacesContext.getCurrentInstance().addMessage(null, msg);
      RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
      RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
      contarRegistrosVigencias();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:growl");
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      cambiosPagina = true;
      listasRecurrentes.getLovGruposConceptos().clear();
      deshabilitarBotonLov();
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   public void activarCtrlF11() {
      log.info("cualTabla= " + cualTabla);
      if (bandera == 0) {
         log.info("Activa 1");
         //Tabla Vigencias VigenciasGruposConceptos
         colCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:colCodigo");
         colCodigo.setFilterStyle("width: 80% !important");
         colDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:colDescripcion");
         colDescripcion.setFilterStyle("width: 85% !important");
         colFundamental = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:colFundamental");
         colFundamental.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
         contarRegistros();
         bandera = 1;

         log.info("Activa 2");
         colCodigo2 = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colCodigo2");
         colCodigo2.setFilterStyle("width: 80% !important;");
         colDescripcion2 = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colDescripcion2");
         colDescripcion2.setFilterStyle("width: 85% !important;");
         colNaturaleza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colNaturaleza");
         colNaturaleza.setFilterStyle("width: 85% !important;");
         colInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colInicial");
         colInicial.setFilterStyle("width: 75% !important;");
         colFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colFinal");
         colFinal.setFilterStyle("width: 75% !important;");
         colEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colEmpresa");
         colEmpresa.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
         contarRegistrosVigencias();
         bandera = 1;
         tipoListaD = 1;
      } else {
         restaurarTablas();
      }
      deshabilitarBotonLov();
   }

   public void restaurarTablas() {
      colCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:colCodigo");
      colCodigo.setFilterStyle("display: none; visibility: hidden;");
      colDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:colDescripcion");
      colDescripcion.setFilterStyle("display: none; visibility: hidden;");
      colFundamental = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:colFundamental");
      colFundamental.setFilterStyle("display: none; visibility: hidden;");
      bandera = 0;
      filtradoListaGruposConceptos = null;

      colCodigo2 = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colCodigo2");
      colCodigo2.setFilterStyle("display: none; visibility: hidden;");
      colDescripcion2 = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colDescripcion2");
      colDescripcion2.setFilterStyle("display: none; visibility: hidden;");
      colNaturaleza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colNaturaleza");
      colNaturaleza.setFilterStyle("display: none; visibility: hidden;");
      colInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colInicial");
      colInicial.setFilterStyle("display: none; visibility: hidden;");
      colFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colFinal");
      colFinal.setFilterStyle("display: none; visibility: hidden;");
      colEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:colEmpresa");
      colEmpresa.setFilterStyle("display: none; visibility: hidden;");
      bandera = 0;
      tipoListaD = 0;
      filtradoListaVigenciasGruposConceptos = null;

      RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
      RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
      contarRegistros();
      contarRegistrosVigencias();
      deshabilitarBotonLov();
   }

   public void salir() {
      if (bandera == 1) {
         restaurarTablas();
      }
      listaGruposConceptosBorrar.clear();
      listaGruposConceptosCrear.clear();
      listaGruposConceptosModificar.clear();
      grupoConceptoSeleccionado = null;

      listaGruposConceptos = null;

      listaVigenciasGruposConceptosBorrar.clear();
      listaVigenciasGruposConceptosCrear.clear();
      listaVigenciasGruposConceptosModificar.clear();
      vigenciaGrupoCSeleccionado = null;

      listaVigenciasGruposConceptos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
      RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
      contarRegistros();
      contarRegistros();
      deshabilitarBotonLov();
      navegar("atras");
   }

   //CANCELAR MODIFICACIONES
   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTablas();
      }
      listaGruposConceptosBorrar.clear();
      listaGruposConceptosCrear.clear();
      listaGruposConceptosModificar.clear();
      grupoConceptoSeleccionado = null;

      listaGruposConceptos = null;

      listaVigenciasGruposConceptosBorrar.clear();
      listaVigenciasGruposConceptosCrear.clear();
      listaVigenciasGruposConceptosModificar.clear();
      vigenciaGrupoCSeleccionado = null;

      listaVigenciasGruposConceptos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
      RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
      contarRegistros();
      contarRegistros();
      deshabilitarBotonLov();
   }

//CREAR Vigencia
//   public void agregarNuevaVigencia() {
//      int pasa = 0;
//      int pasar = 0;
//      mensajeValidacion = new String();
//
//      if (nuevoVigenciasGruposConceptos.getConcepto().getCodigo() == null) {
//         mensajeValidacion = mensajeValidacion + " * Codigo \n";
//         pasa++;
//      }
//
//      if (nuevoVigenciasGruposConceptos.getFechainicial() == null) {
//         mensajeValidacion = mensajeValidacion + " * Fecha Inicial";
//      }
//
//      if (nuevoVigenciasGruposConceptos.getFechafinal() == null) {
//         mensajeValidacion = mensajeValidacion + " * Fecha Final";
//      }
//
//      if (pasa != 0) {
//         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevo");
//         RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
//      }
//
//      if (pasa == 0 && pasar == 0) {
//         if (bandera == 1) {
//            restaurarTablas();
//         }
//         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
//         k++;
//         l = BigInteger.valueOf(k);
//         nuevoVigenciasGruposConceptos.setSecuencia(l);
//         log.info("grupoConceptoSeleccionado" + grupoConceptoSeleccionado.getCodigo());
//         nuevoVigenciasGruposConceptos.setGrupoconcepto(grupoConceptoSeleccionado);
//         cambiosPagina = false;
//         RequestContext.getCurrentInstance().update("form:ACEPTAR");
//         listaVigenciasGruposConceptosCrear.add(nuevoVigenciasGruposConceptos);
//         listaVigenciasGruposConceptos.add(nuevoVigenciasGruposConceptos);
//         vigenciaGrupoCSeleccionado = listaVigenciasGruposConceptos.get(listaVigenciasGruposConceptos.indexOf(nuevoVigenciasGruposConceptos));
//
//         RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
//         contarRegistrosVigencias();
//         deshabilitarBotonLov();
//         if (guardado == true) {
//            guardado = false;
//            RequestContext.getCurrentInstance().update("form:ACEPTAR");
//         }
//         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasGruposConceptos').hide()");
//         limpiarNuevoVigenciaGruposConceptos();
//      }
//   }
   public void confirmarDuplicar() {
      int pasa = 0;
      if (pasa == 0) {
         listaGruposConceptos.add(duplicarGruposConceptos);
         listaGruposConceptosCrear.add(duplicarGruposConceptos);
         grupoConceptoSeleccionado = listaGruposConceptos.get(listaGruposConceptos.indexOf(duplicarGruposConceptos));
         RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
         contarRegistros();
         deshabilitarBotonLov();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            restaurarTablas();
         }
      }
      duplicarGruposConceptos = new GruposConceptos();
   }

   public void confirmarDuplicarD() {
      if (validarCamposNuevaVigencia(duplicarVigenciaGruposConceptos)) {
         listaVigenciasGruposConceptos.add(duplicarVigenciaGruposConceptos);
         listaVigenciasGruposConceptosCrear.add(duplicarVigenciaGruposConceptos);
         vigenciaGrupoCSeleccionado = listaVigenciasGruposConceptos.get(listaVigenciasGruposConceptos.indexOf(duplicarVigenciaGruposConceptos));
         RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
         contarRegistrosVigencias();
         deshabilitarBotonLov();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            restaurarTablas();
         }
         duplicarVigenciaGruposConceptos = new VigenciasGruposConceptos();
      }
   }

   //DUPLICAR Grupos/Vigencias
   public void duplicarE() {
      if (grupoConceptoSeleccionado != null && cualTabla == 0) {
         duplicarGruposConceptos = new GruposConceptos();
         k++;
         l = BigInteger.valueOf(k);
         duplicarGruposConceptos.setSecuencia(l);
         duplicarGruposConceptos.setCodigo(grupoConceptoSeleccionado.getCodigo());
         duplicarGruposConceptos.setDescripcion(grupoConceptoSeleccionado.getDescripcion());
         duplicarGruposConceptos.setFundamental(grupoConceptoSeleccionado.getFundamental());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGrupoConcepto");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroGruposConceptos').show()");

      } else if (vigenciaGrupoCSeleccionado != null && cualTabla == 1) {
         duplicarVigenciaGruposConceptos = new VigenciasGruposConceptos();
         m++;
         n = BigInteger.valueOf(m);
         duplicarVigenciaGruposConceptos.setSecuencia(n);
         duplicarVigenciaGruposConceptos.setGrupoconcepto(vigenciaGrupoCSeleccionado.getGrupoconcepto());
         duplicarVigenciaGruposConceptos.setConcepto(vigenciaGrupoCSeleccionado.getConcepto());
         duplicarVigenciaGruposConceptos.setFechainicial(vigenciaGrupoCSeleccionado.getFechainicial());
         duplicarVigenciaGruposConceptos.setFechafinal(vigenciaGrupoCSeleccionado.getFechafinal());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciasGruposConceptos");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciasGruposConceptos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (grupoConceptoSeleccionado != null && cualTabla == 0) {
         editarGruposConceptos = grupoConceptoSeleccionado;
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoGC");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoGC').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionGC");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionGC').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFundamental");
            RequestContext.getCurrentInstance().execute("PF('editarFundamentalGC').show()");
            cualCelda = -1;
         }
      } else if (vigenciaGrupoCSeleccionado != null && cualTabla == 1) {
         editarVigenciasGruposConceptos = vigenciaGrupoCSeleccionado;
         log.info("Entro a editar... valor celda: " + cualCeldaD);
         log.info("Cual Tabla: " + cualTabla);
         if (cualCeldaD == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoV");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoV').show()");
            cualCeldaD = -1;
         } else if (cualCeldaD == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionV");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionV').show()");
            cualCeldaD = -1;
         } else if (cualCeldaD == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNaturalezaV");
            RequestContext.getCurrentInstance().execute("PF('editarNaturalezaV').show()");
            cualCeldaD = -1;
         } else if (cualCeldaD == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialV");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialV').show()");
            cualCeldaD = -1;
         } else if (cualCeldaD == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalV");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalV').show()");
            cualCeldaD = -1;
         } else if (cualCeldaD == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaV");
            RequestContext.getCurrentInstance().execute("PF('editarEmpresaV').show()");
            cualCeldaD = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (grupoConceptoSeleccionado != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0 || cualCelda == 5) {
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo) {
      if (tipoNuevo == 1) {
         tipoActualizacion = 1;
         codigo = nuevoVigenciasGruposConceptos.getConcepto().getCodigoSTR();
      } else if (tipoNuevo == 2) {
         codigo = duplicarVigenciaGruposConceptos.getConcepto().getCodigoSTR();
         tipoActualizacion = 2;
      }
   }

   public void autocomplConceptoNuevoyDuplicado(String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      tipoActualizacion = tipoNuevo;
      if (codigo != null) {
         if (tipoNuevo == 1) {
            nuevoVigenciasGruposConceptos.getConcepto().setCodigoSTR(codigo);
         } else if (tipoNuevo == 2) {
            duplicarVigenciaGruposConceptos.getConcepto().setCodigoSTR(codigo);
         }
      }
      for (int i = 0; i < lovConceptos.size(); i++) {
         if (lovConceptos.get(i).getCodigoSTR().startsWith(valorConfirmar.toUpperCase())) {
            indiceUnicoElemento = i;
            coincidencias++;
         }
      }
      if (coincidencias == 1) {
         if (tipoNuevo == 1) {
            nuevoVigenciasGruposConceptos.setConcepto(lovConceptos.get(indiceUnicoElemento));
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaGrupoConcepto");
         } else if (tipoNuevo == 2) {
            duplicarVigenciaGruposConceptos.setConcepto(lovConceptos.get(indiceUnicoElemento));
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaGrupoConcepto");
         }
      } else {
         RequestContext.getCurrentInstance().update("form:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
         if (tipoNuevo == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaGrupoConcepto");
         } else if (tipoNuevo == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaGrupoConcepto");
         }
      }
   }

   public void mostrarTodos() {
      if (listasRecurrentes.getLovGruposConceptos().isEmpty()) {
         listaGruposConceptos = administrarGruposConceptos.buscarGruposConceptos();
         if (listaGruposConceptos != null) {
            listasRecurrentes.setLovGruposConceptos(listaGruposConceptos);
         }
      } else {
         listaGruposConceptos = new ArrayList<GruposConceptos>(listasRecurrentes.getLovGruposConceptos());
      }
      if (!listaGruposConceptos.isEmpty()) {
         grupoConceptoSeleccionado = listaGruposConceptos.get(0);
      }
      listaVigenciasGruposConceptos = null;
      getListaVigenciasGruposConceptos();
      RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
      RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
      contarRegistrosVigencias();
      contarRegistros();
      deshabilitarBotonLov();
      filtradoListaGruposConceptos = null;
      aceptar = true;
      grupoConceptoSeleccionado = null;
      vigenciaGrupoCSeleccionado = null;
      tipoActualizacion = -1;
      cualCelda = -1;
   }
//
//   public void chiste() {
//      if (!listaGruposConceptos.isEmpty() && listaVigenciasGruposConceptos.isEmpty()) {
//         RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
//         RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
//      }
//      int tamaño = listaGruposConceptos.size();
//
//      if (tamaño == 0) {
//         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroGruposConceptos");
//         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroGruposConceptos').show()");
//      }
//
//      if (listaVigenciasGruposConceptos.isEmpty() && !listaGruposConceptos.isEmpty()) {
//         RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
//         RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
//      } else if (cualTabla == 0) {
//         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroGruposConceptos");
//         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroGruposConceptos').show()");
//      } else if (cualTabla == 1) {
//         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasGruposConceptos");
//         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasGruposConceptos').show()");
//      }
//   }
//
//   public void dialogoGruposConceptos() {
//      cualTabla = 0;
//      RequestContext.getCurrentInstance().update("form:NuevoRegistroGruposConceptos");
//      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroGruposConceptos').show()");
//   }
//
//   public void dialogoVigenciasGruposConceptos() {
//      cualTabla = 1;
//      RequestContext.getCurrentInstance().update("form:NuevoRegistroVigenciasGruposConceptos");
//      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasGruposConceptos').show()");
//   }

   //Fechas
   public void modificarVigencias(VigenciasGruposConceptos vigencia) {
      vigenciaGrupoCSeleccionado = vigencia;
      if (!listaVigenciasGruposConceptosCrear.contains(vigenciaGrupoCSeleccionado)) {
         if (listaVigenciasGruposConceptosModificar.isEmpty()) {
            listaVigenciasGruposConceptosModificar.add(vigenciaGrupoCSeleccionado);
         } else if (!listaVigenciasGruposConceptosModificar.contains(vigenciaGrupoCSeleccionado)) {
            listaVigenciasGruposConceptosModificar.add(vigenciaGrupoCSeleccionado);
         }
         if (guardado == true) {
            guardado = false;
         }
      }
   }

   public boolean validarFechasRegistro(int i) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      log.error("fechaparametro : " + fechaParametro);
      boolean retorno = true;
      if (i == 0) {
         VigenciasGruposConceptos auxiliar = null;
         auxiliar = vigenciaGrupoCSeleccionado;
         if (auxiliar.getFechafinal() != null) {
            if (auxiliar.getFechainicial().after(fechaParametro) && auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (auxiliar.getFechafinal() == null) {
            if (auxiliar.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (i == 1) {
         if (nuevoVigenciasGruposConceptos.getFechafinal() != null) {
            if (nuevoVigenciasGruposConceptos.getFechainicial().after(fechaParametro) && nuevoVigenciasGruposConceptos.getFechainicial().before(nuevoVigenciasGruposConceptos.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (nuevoVigenciasGruposConceptos.getFechafinal() == null) {
            if (nuevoVigenciasGruposConceptos.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (i == 2) {
         if (duplicarVigenciaGruposConceptos.getFechafinal() != null) {
            if (duplicarVigenciaGruposConceptos.getFechainicial().after(fechaParametro) && duplicarVigenciaGruposConceptos.getFechainicial().before(duplicarVigenciaGruposConceptos.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (duplicarVigenciaGruposConceptos.getFechafinal() == null) {
            if (duplicarVigenciaGruposConceptos.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      return retorno;
   }

   public void modificarFechas(VigenciasGruposConceptos vigencia, int c) {
      vigenciaGrupoCSeleccionado = vigencia;
      if (vigenciaGrupoCSeleccionado.getFechainicial() != null) {
         boolean retorno = false;
         retorno = validarFechasRegistro(0);
         if (retorno == true) {
            cambiarIndiceD(vigenciaGrupoCSeleccionado, c);
            modificarVigencias(vigenciaGrupoCSeleccionado);
         } else {
            vigenciaGrupoCSeleccionado.setFechafinal(fechaFinal);
            vigenciaGrupoCSeleccionado.setFechainicial(fechaInicial);
            RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         vigenciaGrupoCSeleccionado.setFechainicial(fechaInicial);
         RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void actualizarGruposConceptos() {
      if (!listaGruposConceptos.isEmpty()) {
         listaGruposConceptos.clear();
         listaGruposConceptos.add(gruposSeleccionado);
         grupoConceptoSeleccionado = listaGruposConceptos.get(0);
      } else {
         listaGruposConceptos.add(gruposSeleccionado);
      }
      listaVigenciasGruposConceptos = null;
      getListaVigenciasGruposConceptos();
      RequestContext.getCurrentInstance().reset("formularioDialogos:LOVGrupos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVGrupos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('gruposConceptosDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
      RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
      contarRegistros();
      contarRegistrosVigencias();
      filtradoListaGruposConceptos = null;
      gruposSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   public void cancelarCambioGruposConceptos() {
      filtrarlovGruposConceptos = null;
      gruposSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVGrupos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVGrupos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('gruposConceptosDialogo').hide()");
   }

   //AUTOCOMPLETAR Vigencias
   public void modificarGruposConceptos(GruposConceptos grupoC, String confirmarCambio, String valorConfirmar) {
      grupoConceptoSeleccionado = grupoC;

      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaGruposConceptosCrear.contains(grupoConceptoSeleccionado)) {

            if (listaGruposConceptosModificar.isEmpty()) {
               listaGruposConceptosModificar.add(grupoConceptoSeleccionado);
            } else if (!listaGruposConceptosModificar.contains(grupoConceptoSeleccionado)) {
               listaGruposConceptosModificar.add(grupoConceptoSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosGruposConceptos");
      }
   }

   //AUTOCOMPLETAR VigenciasGruposConceptos
   public void modificarVigenciasGruposConceptos(VigenciasGruposConceptos vigencia, String confirmarCambio, String valorConfirmar) {
      vigenciaGrupoCSeleccionado = vigencia;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;

      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaVigenciasGruposConceptosCrear.contains(vigenciaGrupoCSeleccionado)) {

            if (listaVigenciasGruposConceptosModificar.isEmpty()) {
               listaVigenciasGruposConceptosModificar.add(vigenciaGrupoCSeleccionado);
            } else if (!listaVigenciasGruposConceptosModificar.contains(vigenciaGrupoCSeleccionado)) {
               listaVigenciasGruposConceptosModificar.add(vigenciaGrupoCSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) { //Va la lista de valores de Conceptos con el query re ficti
         vigenciaGrupoCSeleccionado.getConcepto().setCodigoSTR(codigo);

         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getCodigoSTR().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            vigenciaGrupoCSeleccionado.setConcepto(lovConceptos.get(indiceUnicoElemento));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = 0;
         }
      } else if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
         vigenciaGrupoCSeleccionado.getConcepto().setNombreEmpresa(empresa);
      }
      if (coincidencias == 1) {
         if (!listaVigenciasGruposConceptosCrear.contains(vigenciaGrupoCSeleccionado)) {
            if (listaVigenciasGruposConceptosModificar.isEmpty()) {
               listaVigenciasGruposConceptosModificar.add(vigenciaGrupoCSeleccionado);
            } else if (!listaVigenciasGruposConceptosModificar.contains(vigenciaGrupoCSeleccionado)) {
               listaVigenciasGruposConceptosModificar.add(vigenciaGrupoCSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
            }
         }
      }
      RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
   }

   public void gruposConceptosDialogo() {
      RequestContext.getCurrentInstance().update("formularioDialogos:gruposConceptosDialogo");
      RequestContext.getCurrentInstance().execute("PF('gruposConceptosDialogo').show()");
   }

   public void asignarIndex(VigenciasGruposConceptos vigencia, int dlg, int LND) {
      vigenciaGrupoCSeleccionado = vigencia;
      tipoActualizacion = LND;
      if (dlg == 0) {
         habilitarBotonLov();
         RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else {
         deshabilitarBotonLov();
      }
   }

   public void asignarIndex(int dlg, int LND) {
      tipoActualizacion = LND;
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      }
   }

   //UBICACION CELDA
   public void cambiarIndice(GruposConceptos grupoc, int celda) {
      grupoConceptoSeleccionado = grupoc;
      if (permitirIndex == true) {
         cualCelda = celda;
         cualTabla = 0;
         tablaImprimir = ":formExportar:datosGruposConceptosExportar";
         nombreArchivo = "GruposConceptosXML";
         log.info("CualTabla = " + cualTabla);
         grupoConceptoSeleccionado = grupoConceptoSeleccionado;
         listaVigenciasGruposConceptos = null;
         getListaVigenciasGruposConceptos();
         RequestContext.getCurrentInstance().update("form:datosVigenciasGruposConceptos");
         contarRegistrosVigencias();
      }
      deshabilitarBotonLov();
   }

   //UBICACION CELDA
   public void cambiarIndiceD(VigenciasGruposConceptos vigencia, int celda) {
      vigenciaGrupoCSeleccionado = vigencia;
      if (permitirIndex == true) {
         cualCeldaD = celda;
         cualTabla = 1;
         fechaInicial = vigenciaGrupoCSeleccionado.getFechainicial();
         fechaFinal = vigenciaGrupoCSeleccionado.getFechafinal();
         tablaImprimir = ":formExportar:datosVigenciasGruposConceptosExportar";
         nombreArchivo = "VigenciasGruposConceptosXML";
         log.info("CualTabla = " + cualTabla);
         codigo = vigenciaGrupoCSeleccionado.getConcepto().getCodigoSTR();
         empresa = vigenciaGrupoCSeleccionado.getConcepto().getNombreEmpresa();
         if (cualCeldaD == 0 || cualCeldaD == 1) {
            habilitarBotonLov();
         } else {
            deshabilitarBotonLov();
         }
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      if (cualTabla == 0) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposConceptosExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarPDF();
         exporter.export(context, tabla, "GruposConceptosPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasGruposConceptosExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarPDFTablasAnchas();
         exporter.export(context, tabla, "VigenciasGruposConceptosPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      }
   }

   public void exportXLS() throws IOException {
      if (cualTabla == 0) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposConceptosExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "GruposConceptosXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasGruposConceptosExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "VigenciasGruposConceptosXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      }
   }

   //LIMPIAR NUEVO REGISTRO
   public void limpiarNuevoGruposConceptos() {
      nuevoGruposConceptos = new GruposConceptos();
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroGruposConceptos");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasGruposConceptos");
   }

   //LIMPIAR NUEVO DETALLE EMBARGO
   public void limpiarNuevoVigenciaGruposConceptos() {
      nuevoVigenciasGruposConceptos = new VigenciasGruposConceptos();
      nuevoVigenciasGruposConceptos.setConcepto(new Conceptos());
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevoVigenciaGrupoConcepto");
      RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasGruposConceptos");
   }

   //LIMPIAR DUPLICAR
   public void limpiarduplicarGruposConceptos() {
      duplicarGruposConceptos = new GruposConceptos();
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarGrupoConcepto");
   }
   //LIMPIAR DUPLICAR NO FORMAL

   public void limpiarduplicarVigenciaGruposConceptos() {
      duplicarVigenciaGruposConceptos = new VigenciasGruposConceptos();
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaGrupoConcepto");
   }

   public void verificarRastro() {
      if (cualTabla == 0) {
         log.info("lol");
//         if (!listaGruposConceptos.isEmpty()) {
         if (grupoConceptoSeleccionado != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(grupoConceptoSeleccionado.getSecuencia(), "GRUPOSCONCEPTOS");
            log.info("resultado: " + resultado);
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
//            } else {
//               RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//            }
         } else if (administrarRastros.verificarHistoricosTabla("GRUPOSCONCEPTOS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
         }
      } else {
         log.info("D");
//         if (!listaVigenciasGruposConceptos.isEmpty()) {
         if (vigenciaGrupoCSeleccionado != null) {
            log.info("NF2");
            int resultadoNF = administrarRastros.obtenerTabla(vigenciaGrupoCSeleccionado.getSecuencia(), "VIGENCIASGRUPOSCONCEPTOS");
            log.info("resultado: " + resultadoNF);
            if (resultadoNF == 1) {
               RequestContext.getCurrentInstance().execute("PF('errorObjetosDBNF').show()");
            } else if (resultadoNF == 2) {
               RequestContext.getCurrentInstance().execute("PF('confirmarRastroNF').show()");
            } else if (resultadoNF == 3) {
               RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroNF').show()");
            } else if (resultadoNF == 4) {
               RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroNF').show()");
            } else if (resultadoNF == 5) {
               RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroNF').show()");
            }
//            } else {
//               RequestContext.getCurrentInstance().execute("PF('seleccionarRegistroNF').show()");
//            }
         } else if (administrarRastros.verificarHistoricosTabla("VIGENCIASGRUPOSCONCEPTOS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoNF').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoNF').show()");
         }
      }
   }

   public void validarNuevoGrupo() {
      limpiarNuevoGruposConceptos();
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroGruposConceptos').show()");
   }

   public void validarNuevaVigencia() {
      if (grupoConceptoSeleccionado != null) {
         nuevoVigenciasGruposConceptos = new VigenciasGruposConceptos();
         nuevoVigenciasGruposConceptos.setConcepto(new Conceptos());
         nuevoVigenciasGruposConceptos.setGrupoconcepto(grupoConceptoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasGruposConceptos");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasGruposConceptos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistroGrupo').show()");
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
      getListaGruposConceptos();
      if (listaGruposConceptos != null) {
         if (!listaGruposConceptos.isEmpty()) {
            grupoConceptoSeleccionado = listaGruposConceptos.get(0);
         }
      }
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "grupoconcepto";
//         Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
//         mapParametros.put("paginaAnterior", pagActual);
//         mas Parametros
      if (pag.equals("rastrotablaGC")) {
         ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         controlRastro.recibirDatosTabla(grupoConceptoSeleccionado.getSecuencia(), "GruposConceptos", pagActual);
         pag = "rastrotabla";
      } else if (pag.equals("rastrotablaHGC")) {
         ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         controlRastro.historicosTabla("GruposConceptos", pagActual);
         pag = "rastrotabla";
      } else if (pag.equals("rastrotablaVGC")) {
         ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         controlRastro.recibirDatosTabla(vigenciaGrupoCSeleccionado.getSecuencia(), "VigenciasGruposConceptos", pagActual);
         pag = "rastrotabla";
      } else if (pag.equals("rastrotablaHVGC")) {
         ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         controlRastro.historicosTabla("VigenciasGruposConceptos", pagActual);
         pag = "rastrotabla";
      }
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
         //           controlRastro.recibirDatosTabla(conceptoLovSeleccionado.getSecuencia(), "Conceptos", pagActual);
         //      } else if (pag.equals("rastrotablaH")) {
         //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
         //     controlRastro.historicosTabla("Conceptos", pagActual);
         //   pag = "rastrotabla";
         //}
      }
      log.info("ControlGrupoConcepto.navegar() paginaAnterior:" + paginaAnterior + ", pag: " + pag);
      limpiarListasValor();
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      //inicializarCosas(); Inicializar cosas de ser necesario
      getListaGruposConceptos();
      if (listaGruposConceptos != null) {
         if (!listaGruposConceptos.isEmpty()) {
            grupoConceptoSeleccionado = listaGruposConceptos.get(0);
         }
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosVigencias() {
      RequestContext.getCurrentInstance().update("form:infoRegistroVigencias");
   }

   public void contarRegistrosLOVGrupos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLOVGruposC");
   }

   public void contarRegistrosLOVConceptos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLOVConceptos");
   }

   //Getter & Setter
   public List<GruposConceptos> getListaGruposConceptos() {
      if (listaGruposConceptos == null) {
         if (listasRecurrentes.getLovGruposConceptos().isEmpty()) {
            listaGruposConceptos = administrarGruposConceptos.buscarGruposConceptos();
            if (listaGruposConceptos != null) {
               listasRecurrentes.setLovGruposConceptos(listaGruposConceptos);
            }
         } else {
            listaGruposConceptos = new ArrayList<GruposConceptos>(listasRecurrentes.getLovGruposConceptos());
         }
      }
      return listaGruposConceptos;
   }

   public void setListaGruposConceptos(List<GruposConceptos> listaGruposConceptos) {
      this.listaGruposConceptos = listaGruposConceptos;
   }

   public List<GruposConceptos> getFiltradoListaGruposConceptos() {
      return filtradoListaGruposConceptos;
   }

   public void setFiltradoListaGruposConceptos(List<GruposConceptos> filtradoListaGruposConceptos) {
      this.filtradoListaGruposConceptos = filtradoListaGruposConceptos;
   }

   public GruposConceptos getGrupoConceptoSeleccionado() {
      return grupoConceptoSeleccionado;
   }

   public void setGrupoConceptoSeleccionado(GruposConceptos grupoConceptoSeleccionado) {
      this.grupoConceptoSeleccionado = grupoConceptoSeleccionado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public List<VigenciasGruposConceptos> getListaVigenciasGruposConceptos() {
      if (listaVigenciasGruposConceptos == null && grupoConceptoSeleccionado != null) {
         listaVigenciasGruposConceptos = administrarGruposConceptos.buscarVigenciasGruposConceptos(grupoConceptoSeleccionado.getSecuencia());
      }
      return listaVigenciasGruposConceptos;
   }

   public void setListaVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVigenciasGruposConceptos) {
      this.listaVigenciasGruposConceptos = listaVigenciasGruposConceptos;
   }

   public List<VigenciasGruposConceptos> getFiltradoListaVigenciasGruposConceptos() {
      return filtradoListaVigenciasGruposConceptos;
   }

   public void setFiltradoListaVigenciasGruposConceptos(List<VigenciasGruposConceptos> filtradoListaVigenciasGruposConceptos) {
      this.filtradoListaVigenciasGruposConceptos = filtradoListaVigenciasGruposConceptos;
   }

   public VigenciasGruposConceptos getVigenciaGrupoCSeleccionado() {
      return vigenciaGrupoCSeleccionado;
   }

   public void setVigenciaGrupoCSeleccionado(VigenciasGruposConceptos vigenciaGrupoCSeleccionado) {
      this.vigenciaGrupoCSeleccionado = vigenciaGrupoCSeleccionado;
   }

   public GruposConceptos getEditarGruposConceptos() {
      return editarGruposConceptos;
   }

   public void setEditarGruposConceptos(GruposConceptos editarGruposConceptos) {
      this.editarGruposConceptos = editarGruposConceptos;
   }

   public VigenciasGruposConceptos getEditarVigenciasGruposConceptos() {
      return editarVigenciasGruposConceptos;
   }

   public void setEditarVigenciasGruposConceptos(VigenciasGruposConceptos editarVigenciasGruposConceptos) {
      this.editarVigenciasGruposConceptos = editarVigenciasGruposConceptos;
   }

   public String getTablaImprimir() {
      return tablaImprimir;
   }

   public void setTablaImprimir(String tablaImprimir) {
      this.tablaImprimir = tablaImprimir;
   }

   public String getNombreArchivo() {
      return nombreArchivo;
   }

   public void setNombreArchivo(String nombreArchivo) {
      this.nombreArchivo = nombreArchivo;
   }

   public GruposConceptos getNuevoGruposConceptos() {
      return nuevoGruposConceptos;
   }

   public void setNuevoGruposConceptos(GruposConceptos nuevoGruposConceptos) {
      this.nuevoGruposConceptos = nuevoGruposConceptos;
   }

   public VigenciasGruposConceptos getNuevoVigenciaGruposConceptos() {
      return nuevoVigenciasGruposConceptos;
   }

   public void setNuevoVigenciaGruposConceptos(VigenciasGruposConceptos nuevoVigenciaGruposConceptos) {
      this.nuevoVigenciasGruposConceptos = nuevoVigenciaGruposConceptos;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public GruposConceptos getDuplicarGruposConceptos() {
      return duplicarGruposConceptos;
   }

   public void setDuplicarGruposConceptos(GruposConceptos duplicarGruposConceptos) {
      this.duplicarGruposConceptos = duplicarGruposConceptos;
   }

   public VigenciasGruposConceptos getDuplicarVigenciaGruposConceptos() {
      return duplicarVigenciaGruposConceptos;
   }

   public void setDuplicarVigenciaGruposConceptos(VigenciasGruposConceptos duplicarVigenciaGruposConceptos) {
      this.duplicarVigenciaGruposConceptos = duplicarVigenciaGruposConceptos;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public List<Conceptos> getLovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = administrarGruposConceptos.lovConceptos();
      }

      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptos) {
      this.lovConceptos = lovConceptos;
   }

   public List<Conceptos> getFiltrarLovConceptos() {
      return filtrarLovConceptos;
   }

   public void setFiltrarLovConceptos(List<Conceptos> filtrarLovConceptos) {
      this.filtrarLovConceptos = filtrarLovConceptos;
   }

   public Conceptos getConceptoLovSeleccionado() {
      return conceptoLovSeleccionado;
   }

   public void setConceptoLovSeleccionado(Conceptos conceptoLovSeleccionado) {
      this.conceptoLovSeleccionado = conceptoLovSeleccionado;
   }

   public List<GruposConceptos> getLovGruposConceptos() {
      if (lovGruposConceptos == null) {
         if (listasRecurrentes.getLovGruposConceptos().isEmpty()) {
            lovGruposConceptos = administrarGruposConceptos.buscarGruposConceptos();
            if (lovGruposConceptos != null) {
               listasRecurrentes.setLovGruposConceptos(lovGruposConceptos);
            }
         } else {
            lovGruposConceptos = new ArrayList<GruposConceptos>(listasRecurrentes.getLovGruposConceptos());
         }
      }
      return lovGruposConceptos;
   }

   public void setLovGruposConceptos(List<GruposConceptos> lovGruposConceptos) {
      this.lovGruposConceptos = lovGruposConceptos;
   }

   public List<GruposConceptos> getFiltrarlovGruposConceptos() {
      return filtrarlovGruposConceptos;
   }

   public void setFiltrarlovGruposConceptos(List<GruposConceptos> filtrarlovGruposConceptos) {
      this.filtrarlovGruposConceptos = filtrarlovGruposConceptos;
   }

   public GruposConceptos getGruposSeleccionado() {
      return gruposSeleccionado;
   }

   public void setGruposSeleccionado(GruposConceptos gruposSeleccionado) {
      this.gruposSeleccionado = gruposSeleccionado;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosGruposConceptos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroVigencias() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasGruposConceptos");
      infoRegistroVigencias = String.valueOf(tabla.getRowCount());
      return infoRegistroVigencias;
   }

   public void setInfoRegistroVigencias(String infoRegistroVigencias) {
      this.infoRegistroVigencias = infoRegistroVigencias;
   }

   public String getInfoRegistroLOVConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVConceptos");
      infoRegistroLOVConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroLOVConceptos;
   }

   public void setInfoRegistroLOVConceptos(String infoRegistroLOVConceptos) {
      this.infoRegistroLOVConceptos = infoRegistroLOVConceptos;
   }

   public String getInfoRegistroLOVGruposC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVGrupos");
      infoRegistroLOVGruposC = String.valueOf(tabla.getRowCount());
      return infoRegistroLOVGruposC;
   }

   public void setInfoRegistroLOVGruposC(String infoRegistroLOVGruposC) {
      this.infoRegistroLOVGruposC = infoRegistroLOVGruposC;
   }

   public String getTamano() {
      try {
         colCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:colCodigo");
         String estilo = colCodigo.getFilterStyle();
         log.info("getTamano() estilo : " + estilo);
         if (estilo.startsWith("width: 85%")) {
            tamano = "" + 113;
         } else {
            tamano = "" + 133;
         }
      } catch (Exception e) {
         log.warn("ERROR ControlgrupoConcepto.getTamano:  ", e);
      }
      return tamano;
   }

   public void setTamano(String tamano) {
      this.tamano = tamano;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
