/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposUnidades;
import Entidades.Unidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUnidadesInterface;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlUnidad implements Serializable {

   private static Logger log = Logger.getLogger(ControlUnidad.class);

   @EJB
   AdministrarUnidadesInterface administrarUnidades;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Unidades> listaUnidades;
   private List<Unidades> filtradoListaUnidades;
   private Unidades unidadSeleccionada;
   //LISTA DE VALORES DE TIPOS UNIDADES FALTA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   private List<TiposUnidades> lovTiposUnidades;
   private List<TiposUnidades> lovFiltradoTiposUnidades;
   private TiposUnidades tiposUnidadSeleccionado;
   //Otros
   private boolean aceptar;
   private int tipoActualizacion;
   private boolean permitirIndex;
   private int tipoLista;
   private int cualCelda;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Modificar Unidades
   private List<Unidades> listaUnidadesModificar;
   private boolean guardado, guardarOk;
   //Crear Unidades
   public Unidades nuevaUnidad;
   private List<Unidades> listaUnidadesCrear;
   private BigInteger l;
   private int k;
   private String mensajeValidacion;
   //Borrar Unidad
   private List<Unidades> listaUnidadesBorrar;
   //AUTOCOMPLETAR
   private String tipoUnidad;
   //editar celda
   private Unidades editarUnidad;
   private boolean cambioEditor, aceptarEditar, activarLov;
   //DUPLICAR
   private Unidades duplicarUnidad;
   //RASTRO
   public String altoTabla;
   public String infoRegistroTiposUnidades;
   //
   //Tabla a Imprimir
   private String tablaImprimir, nombreArchivo;
   private Column unidadesCodigos, unidadesNombres, unidadesTipos;
   public String infoRegistro;
   ///////////////////////////////////////////////
   //////////////////PRUEBAS UNITARIAS COMPONENTES
   ///////////////////////////////////////////////
   public String buscarNombre;
   public boolean buscador;
   private BigInteger secuenciaPruebaConceptoEmpresa;
   private BigInteger secuenciaEmpleado;
   public String paginaAnterior;
   private Map<String, Object> mapParametros;

   public ControlUnidad() {
      permitirIndex = true;
      aceptar = true;
      tipoLista = 0;
      listaUnidadesBorrar = new ArrayList<Unidades>();
      listaUnidadesCrear = new ArrayList<Unidades>();
      listaUnidadesModificar = new ArrayList<Unidades>();
      lovTiposUnidades = new ArrayList<TiposUnidades>();
      //editar
      editarUnidad = new Unidades();
      cambioEditor = false;
      aceptarEditar = true;
      cualCelda = -1;
      tipoLista = 0;
      nuevaUnidad = new Unidades();
      nuevaUnidad.setTipounidad(new TiposUnidades());
      unidadSeleccionada = null;
      k = 0;
      altoTabla = "270";
      guardado = true;
      buscador = false;
      tablaImprimir = ":formExportar:datosUnidadesExportar";
      nombreArchivo = "UnidadesXML";
      secuenciaEmpleado = null;
      secuenciaPruebaConceptoEmpresa = null;
      activarLov = true;
      paginaAnterior = "nominaf";
      mapParametros = new LinkedHashMap<String, Object>();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovTiposUnidades = null;
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
         administrarUnidades.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listaUnidades = null;
      getListaUnidades();
      if (listaUnidades != null) {
         if (!listaUnidades.isEmpty()) {
            unidadSeleccionada = listaUnidades.get(0);
         }
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listaUnidades = null;
      getListaUnidades();
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "unidad";
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

   public void activarAceptar() {
      aceptar = false;
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
         unidadesCodigos.setFilterStyle("width: 85% !important;");
         unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
         unidadesNombres.setFilterStyle("width: 85% !important;");
         unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
         unidadesTipos.setFilterStyle("width: 85% !important;");
         altoTabla = "250";
         RequestContext.getCurrentInstance().update("form:datosUnidades");
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
         unidadesCodigos.setFilterStyle("display: none; visibility: hidden;");
         unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
         unidadesNombres.setFilterStyle("display: none; visibility: hidden;");
         unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
         unidadesTipos.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosUnidades");
         altoTabla = "270";
         bandera = 0;
         filtradoListaUnidades = null;
         tipoLista = 0;

      }
   }

   public void cambiarIndice(Unidades unidad, int celda) {
      if (permitirIndex == true) {
         unidadSeleccionada = unidad;
         cualCelda = celda;
         tablaImprimir = ":formExportar:datosUnidadesExportar";
         nombreArchivo = "UnidadesXML";
         unidadSeleccionada.getSecuencia();
         if (cualCelda == 0) {
            deshabilitarBotonLov();
            unidadSeleccionada.getCodigo();
         } else if (cualCelda == 1) {
            unidadSeleccionada.getNombre();
            deshabilitarBotonLov();
         } else if (cualCelda == 2) {
            habilitarBotonLov();
            tipoUnidad = unidadSeleccionada.getTipounidad().getNombre();
         }
      }
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (unidadSeleccionada != null) {
         editarUnidad = unidadSeleccionada;

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigos");
            RequestContext.getCurrentInstance().execute("PF('editarCodigos').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombres");
            RequestContext.getCurrentInstance().execute("PF('editarNombres').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            habilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipos");
            RequestContext.getCurrentInstance().execute("PF('editarTipos').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void modificarUnidades(Unidades unidad) {
      unidadSeleccionada = unidad;
      if (!listaUnidadesCrear.contains(unidadSeleccionada)) {
         if (listaUnidadesModificar.isEmpty()) {
            listaUnidadesModificar.add(unidadSeleccionada);
         } else if (!listaUnidadesModificar.contains(unidadSeleccionada)) {
            listaUnidadesModificar.add(unidadSeleccionada);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosUnidades");
   }

   public void asignarIndex(Unidades unidad, int dlg, int LND) {
      tipoActualizacion = LND;
      unidadSeleccionada = unidad;
      if (dlg == 0) {
         contarRegistroTUnidades();
         RequestContext.getCurrentInstance().update("formularioDialogos:tiposUnidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').show()");
      }
   }

   public void actualizarTiposUnidades() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         unidadSeleccionada.setTipounidad(tiposUnidadSeleccionado);
         if (!listaUnidadesCrear.contains(unidadSeleccionada)) {
            if (listaUnidadesModificar.isEmpty()) {
               listaUnidadesModificar.add(unidadSeleccionada);
            } else if (!listaUnidadesModificar.contains(unidadSeleccionada)) {
               listaUnidadesModificar.add(unidadSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosUnidades");
      } else if (tipoActualizacion == 1) {
         nuevaUnidad.setTipounidad(tiposUnidadSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUnidad");
      } else if (tipoActualizacion == 2) {
         duplicarUnidad.setTipounidad(tiposUnidadSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUnidad");
      }
      lovFiltradoTiposUnidades = null;
      tiposUnidadSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:tiposUnidadesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposUnidades");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTU");
      context.reset("formularioDialogos:LOVTiposUnidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTiposUnidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').hide()");
   }

   public void cancelarCambioTiposUnidades() {
      RequestContext context = RequestContext.getCurrentInstance();
      lovFiltradoTiposUnidades = null;
      tiposUnidadSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("formularioDialogos:tiposUnidadesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposUnidades");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTU");
      context.reset("formularioDialogos:LOVTiposUnidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTiposUnidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').hide()");
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (unidadSeleccionada != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 2) {
            contarRegistroTUnidades();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposUnidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposUnidadesDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUnidadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "UnidadesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUnidadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "UnidadesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void limpiarNuevaUnidad() {
      nuevaUnidad = new Unidades();
      nuevaUnidad.setTipounidad(new TiposUnidades());
      nuevaUnidad.getTipounidad().setNombre(" ");
   }

   public void limpiarDuplicarUnidad() {
      duplicarUnidad = new Unidades();
      duplicarUnidad.setTipounidad(new TiposUnidades());
      duplicarUnidad.getTipounidad().setNombre(" ");
   }

   //VERIFICAR RASTRO
   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (unidadSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(unidadSeleccionada.getSecuencia(), "UNIDADES");
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
      } else if (administrarRastros.verificarHistoricosTabla("UNIDADES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         log.info("Desactivar");
         unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
         unidadesCodigos.setFilterStyle("display: none; visibility: hidden;");
         unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
         unidadesNombres.setFilterStyle("display: none; visibility: hidden;");
         unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
         unidadesTipos.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosUnidades");
         altoTabla = "270";
         bandera = 0;
         filtradoListaUnidades = null;
         tipoLista = 0;
      }
      listaUnidadesBorrar.clear();
      listaUnidadesCrear.clear();
      listaUnidadesModificar.clear();
      unidadSeleccionada = null;
      k = 0;
      listaUnidades = null;
      getListaUnidades();
      contarRegistros();
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosUnidades");

   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         log.info("Desactivar");
         unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
         unidadesCodigos.setFilterStyle("display: none; visibility: hidden;");
         unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
         unidadesNombres.setFilterStyle("display: none; visibility: hidden;");
         unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
         unidadesTipos.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosUnidades");
         altoTabla = "270";
         bandera = 0;
         filtradoListaUnidades = null;
         tipoLista = 0;
         log.info("TipoLista= " + tipoLista);
      }
      listaUnidadesBorrar.clear();
      listaUnidadesCrear.clear();
      listaUnidadesModificar.clear();
      unidadSeleccionada = null;
      k = 0;
      listaUnidades = null;
      getListaUnidades();
      contarRegistros();
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosUnidades");
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      navegar("atras");
   }

   public void borrarUnidades() {

      if (unidadSeleccionada != null) {
         if (tipoLista == 0) {
            if (!listaUnidadesModificar.isEmpty() && listaUnidadesModificar.contains(unidadSeleccionada)) {
               int modIndex = listaUnidadesModificar.indexOf(unidadSeleccionada);
               listaUnidadesModificar.remove(modIndex);
               listaUnidadesBorrar.add(unidadSeleccionada);
            } else if (!listaUnidadesCrear.isEmpty() && listaUnidadesCrear.contains(unidadSeleccionada)) {
               int crearIndex = listaUnidadesCrear.indexOf(unidadSeleccionada);
               listaUnidadesCrear.remove(crearIndex);
            } else {
               listaUnidadesBorrar.add(unidadSeleccionada);
            }
            listaUnidades.remove(unidadSeleccionada);
         }

         if (tipoLista == 1) {
            filtradoListaUnidades.remove(unidadSeleccionada);
         }

         unidadSeleccionada = null;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosUnidades");

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //CREAR UNIDAD
   public void agregarNuevaUnidad() {
      int pasaA = 0;
      int pasa = 0;
      mensajeValidacion = " ";

      if (nuevaUnidad.getNombre() == null || nuevaUnidad.getNombre().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }

      if (nuevaUnidad.getTipounidad().getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }

      for (int i = 0; i < listaUnidades.size(); i++) {
         if (nuevaUnidad.getCodigo() != null) {
            if (nuevaUnidad.getCodigo().equals(listaUnidades.get(i).getCodigo())) {
               RequestContext.getCurrentInstance().update("formularioDialogos:existe");
               RequestContext.getCurrentInstance().execute("PF('existe').show()");
               pasaA++;
            }
         }
      }

      if (pasa == 0 && pasaA == 0) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            log.info("Desactivar");
            unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
            unidadesCodigos.setFilterStyle("display: none; visibility: hidden;");
            unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
            unidadesNombres.setFilterStyle("display: none; visibility: hidden;");
            unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
            unidadesTipos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUnidades");
            altoTabla = "270";
            bandera = 0;
            filtradoListaUnidades = null;
            log.info("TipoLista= " + tipoLista);
            tipoLista = 0;
         }
         //AGREGAR REGISTRO A LA LISTA CIUDADES.
         k++;
         l = BigInteger.valueOf(k);
         nuevaUnidad.setSecuencia(l);
         listaUnidadesCrear.add(nuevaUnidad);
         listaUnidades.add(nuevaUnidad);
         unidadSeleccionada = nuevaUnidad;
         contarRegistros();
         //  nuevaCiudad.setNombre(Departamento);
         nuevaUnidad = new Unidades();
         nuevaUnidad.setTipounidad(new TiposUnidades());
         RequestContext.getCurrentInstance().update("form:datosUnidades");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUnidad').hide()");
      } else if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUnidad");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaUnidad').show()");
      }

   }

   //DUPLICAR CIUDAD
   public void duplicarU() {
      if (unidadSeleccionada != null) {
         duplicarUnidad = new Unidades();

         if (tipoLista == 0) {
            duplicarUnidad.setCodigo(unidadSeleccionada.getCodigo());
            duplicarUnidad.setNombre(unidadSeleccionada.getNombre());
            duplicarUnidad.setTipounidad(unidadSeleccionada.getTipounidad());
         }
         if (tipoLista == 1) {
            duplicarUnidad.setCodigo(unidadSeleccionada.getCodigo());
            duplicarUnidad.setNombre(unidadSeleccionada.getNombre());
            duplicarUnidad.setTipounidad(unidadSeleccionada.getTipounidad());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUnidad");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUnidad').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }

   }

   public void confirmarDuplicar() {
      int pasaA = 0;
      int pasa = 0;
      k++;
      l = BigInteger.valueOf(k);
      duplicarUnidad.setSecuencia(l);

      if (duplicarUnidad.getNombre() == null || duplicarUnidad.getNombre().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }

      if (duplicarUnidad.getTipounidad().getNombre() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }

      for (int i = 0; i < listaUnidades.size(); i++) {
         if (duplicarUnidad.getCodigo() != null) {
            if (duplicarUnidad.getCodigo().equals(listaUnidades.get(i).getCodigo())) {
               RequestContext.getCurrentInstance().update("formularioDialogos:existe");
               RequestContext.getCurrentInstance().execute("PF('existe').show()");
               pasaA++;
            }
         }
      }

      if (pasa == 0 && pasaA == 0) {
         unidadSeleccionada = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            log.info("Desactivar");
            unidadesCodigos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesCodigos");
            unidadesCodigos.setFilterStyle("display: none; visibility: hidden;");
            unidadesNombres = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesNombres");
            unidadesNombres.setFilterStyle("display: none; visibility: hidden;");
            unidadesTipos = (Column) c.getViewRoot().findComponent("form:datosUnidades:unidadesTipos");
            unidadesTipos.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUnidades");
            altoTabla = "270";
            bandera = 0;
            filtradoListaUnidades = null;
            log.info("TipoLista= " + tipoLista);
            tipoLista = 0;
         }
         listaUnidades.add(duplicarUnidad);
         listaUnidadesCrear.add(duplicarUnidad);
         unidadSeleccionada = duplicarUnidad;
         RequestContext.getCurrentInstance().update("form:datosUnidades");
         duplicarUnidad = new Unidades();
         contarRegistroTUnidades();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroUnidad");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUnidad').hide()");

      } else if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUnidad");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaUnidad').show()");
      }

   }

   public void activarBuscador() {
      buscador = false;
      RequestContext.getCurrentInstance().update("formularioDialogos:busquedaNombre");
      RequestContext.getCurrentInstance().execute("PF('busquedaNombre').show()");
   }

   public void guardarYSalir() {
      guardarCambiosUnidad();
      salir();
   }

   //GUARDAR
   public void guardarCambiosUnidad() {
      try {
         if (guardado == false) {
            log.info("Realizando Operaciones Unidades");
            if (!listaUnidadesBorrar.isEmpty()) {
               administrarUnidades.borrarUnidades(listaUnidadesBorrar);
               log.info("Entra");
               listaUnidadesBorrar.clear();
            }
            if (!listaUnidadesCrear.isEmpty()) {
               administrarUnidades.crearUnidades(listaUnidadesCrear);
               listaUnidadesCrear.clear();
            }
            if (!listaUnidadesModificar.isEmpty()) {
               administrarUnidades.modificarUnidades(listaUnidadesModificar);
               listaUnidadesModificar.clear();
            }
            log.info("Se guardaron los datos con exito");
            listaUnidades = null;
            getListaUnidades();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosUnidades");
            guardado = true;
            permitirIndex = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            unidadSeleccionada = null;
         }
      } catch (Exception e) {
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //EVENTO FILTRAR
   public void eventofiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistroTUnidades() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTiposUnidades");
   }

   //Getter & Setters
   public List<Unidades> getListaUnidades() {
      if (listaUnidades == null) {
         listaUnidades = administrarUnidades.consultarUnidades();
      }
      return listaUnidades;
   }

   public void setListaUnidades(List<Unidades> listaUnidades) {
      this.listaUnidades = listaUnidades;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public List<Unidades> getFiltradoListaUnidades() {
      return filtradoListaUnidades;
   }

   public void setFiltradoListaUnidades(List<Unidades> filtradoListaUnidades) {
      this.filtradoListaUnidades = filtradoListaUnidades;
   }

   public Unidades getUnidadSeleccionada() {
      return unidadSeleccionada;
   }

   public void setUnidadSeleccionada(Unidades unidadSeleccionada) {
      this.unidadSeleccionada = unidadSeleccionada;
   }

   public List<TiposUnidades> getLovTiposUnidades() {
      lovTiposUnidades = administrarUnidades.consultarTiposUnidades();
      return lovTiposUnidades;
   }

   public void setLovTiposUnidades(List<TiposUnidades> lovTiposUnidades) {
      this.lovTiposUnidades = lovTiposUnidades;
   }

   public List<TiposUnidades> getLovFiltradoTiposUnidades() {
      return lovFiltradoTiposUnidades;
   }

   public void setLovFiltradoTiposUnidades(List<TiposUnidades> lovFiltradoTiposUnidades) {
      this.lovFiltradoTiposUnidades = lovFiltradoTiposUnidades;
   }

   public TiposUnidades getTiposUnidadSeleccionado() {
      return tiposUnidadSeleccionado;
   }

   public void setTiposUnidadSeleccionado(TiposUnidades tiposUnidadSeleccionado) {
      this.tiposUnidadSeleccionado = tiposUnidadSeleccionado;
   }

   public String getInfoRegistroTiposUnidades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVTiposUnidades");
      infoRegistroTiposUnidades = String.valueOf(tabla.getRowCount());
      return infoRegistroTiposUnidades;
   }

   public void setInfoRegistroTiposUnidades(String infoRegistroTiposUnidades) {
      this.infoRegistroTiposUnidades = infoRegistroTiposUnidades;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public Unidades getEditarUnidad() {
      return editarUnidad;
   }

   public void setEditarUnidad(Unidades editarUnidad) {
      this.editarUnidad = editarUnidad;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUnidades");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getBuscarNombre() {
      return buscarNombre;
   }

   public void setBuscarNombre(String buscarNombre) {
      this.buscarNombre = buscarNombre;
   }

   public boolean isBuscador() {
      return buscador;
   }

   public void setBuscador(boolean buscador) {
      this.buscador = buscador;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public Unidades getNuevaUnidad() {
      return nuevaUnidad;
   }

   public void setNuevaUnidad(Unidades nuevaUnidad) {
      this.nuevaUnidad = nuevaUnidad;
   }

   public Unidades getDuplicarUnidad() {
      return duplicarUnidad;
   }

   public void setDuplicarUnidad(Unidades duplicarUnidad) {
      this.duplicarUnidad = duplicarUnidad;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public BigInteger getSecuenciaPruebaConceptoEmpresa() {
      return secuenciaPruebaConceptoEmpresa;
   }

   public void setSecuenciaPruebaConceptoEmpresa(BigInteger secuenciaPruebaConceptoEmpresa) {
      this.secuenciaPruebaConceptoEmpresa = secuenciaPruebaConceptoEmpresa;
   }

   public BigInteger getSecuenciaEmpleado() {
      return secuenciaEmpleado;
   }

   public void setSecuenciaEmpleado(BigInteger secuenciaEmpleado) {
      this.secuenciaEmpleado = secuenciaEmpleado;
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

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
