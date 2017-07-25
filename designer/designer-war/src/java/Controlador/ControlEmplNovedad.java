/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Novedades;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplNovedadInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.ArrayList;
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
public class ControlEmplNovedad implements Serializable {

   private static Logger log = Logger.getLogger(ControlEmplNovedad.class);

   @EJB
   AdministrarEmplNovedadInterface administrarEmplNovedad;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //Vigencias Cargos
   private List<Novedades> listNovedadesEmpleado;
   private List<Novedades> filtrarListNovedadesEmpleado;
   private List<Novedades> listNovedadesEmpleadoModificar;
   private List<Novedades> listNovedadesEmpleadoBorrar;
   private Novedades novedadSeleccionada;
   private Empleados empleado;
   //lovs
   private List<Conceptos> lovConceptos;
   private List<Conceptos> lovConceptosFiltrar;
   private Conceptos conceptoSeleccionado;

   private List<Periodicidades> lovPeriodicidades;
   private List<Periodicidades> lovPeriodicidadesFiltrar;
   private Periodicidades periodicidadSeleccionada;

   private List<Terceros> lovTerceros;
   private List<Terceros> lovTercerosFiltrar;
   private Terceros terceroSeleccionado;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla VC
   private Column novedadCodigoConcepto, novedadDescripcionConcepto, novedadFechaInicial, novedadFechaFinal, novedadValor, novedadSaldo, novedadCodigoPeriodicidad,
           novedadDescripcionPeriodicidad, novedadTercero, novedadObservacion, novedadFechaReporte;
   //Otros
   private boolean aceptar, activarLov;
   private boolean guardado;
   //editar celda
   private Novedades editarNovedad;
   private int cualCelda, tipoLista;
   private boolean cambioEditor, aceptarEditar;
   private String altoTabla;
   private String infoRegistro, infoRegistroConceptos, infoRegistroPeriodicidades, infoRegistroTerceros;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmplNovedad() {
      listNovedadesEmpleado = null;
      listNovedadesEmpleadoModificar = new ArrayList<Novedades>();
      listNovedadesEmpleadoBorrar = new ArrayList<Novedades>();
      aceptar = true;
      editarNovedad = new Novedades();
      cambioEditor = false;
      aceptarEditar = true;
      cualCelda = -1;
      tipoLista = 0;
      lovConceptos = null;
      lovPeriodicidades = null;
      lovTerceros = null;
      guardado = true;
      activarLov = true;
      altoTabla = "310";
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
      String pagActual = "emplnovedad";
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

   public void limpiarListasValor() {
      lovConceptos = null;
      lovPeriodicidades = null;
      lovTerceros = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarEmplNovedad.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger empl) {
      listNovedadesEmpleado = null;
      empleado = administrarEmplNovedad.actualEmpleado(empl);
      getListNovedadesEmpleado();
      if (listNovedadesEmpleado != null) {
         if (!listNovedadesEmpleado.isEmpty()) {
            novedadSeleccionada = listNovedadesEmpleado.get(0);
         }
      }
   }

   public void refrescar() {
      cerrarFiltrado();
      listNovedadesEmpleado = null;
      novedadSeleccionada = null;
      guardado = true;
      getListNovedadesEmpleado();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
   }

   public void sencillo() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      novedadSeleccionada = listNovedadesEmpleado.get(indice);
      cambiarIndice(novedadSeleccionada, columna);
   }

   public void modificarNovedades(Novedades novedad) {
      if (listNovedadesEmpleadoModificar.isEmpty()) {
         listNovedadesEmpleadoModificar.add(novedadSeleccionada);
      } else if (!listNovedadesEmpleadoModificar.contains(novedadSeleccionada)) {
         listNovedadesEmpleadoModificar.add(novedadSeleccionada);
      }
      guardado = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
   }

   public void cambiarIndice(Novedades novedad, int celda) {
      novedadSeleccionada = novedad;
      cualCelda = celda;
      novedadSeleccionada.getSecuencia();
      if (cualCelda == 0) {
         habilitarBotonLov();
         novedadSeleccionada.getConcepto().getCodigo();
      } else if (cualCelda == 1) {
         habilitarBotonLov();
         novedadSeleccionada.getConcepto().getDescripcion();
      } else if (cualCelda == 2) {
         deshabilitarBotonLov();
         novedadSeleccionada.getFechainicial();
      } else if (cualCelda == 3) {
         deshabilitarBotonLov();
         novedadSeleccionada.getFechafinal();
      } else if (cualCelda == 4) {
         deshabilitarBotonLov();
         novedadSeleccionada.getValortotal();
      } else if (cualCelda == 5) {
         deshabilitarBotonLov();
         novedadSeleccionada.getSaldo();
      } else if (cualCelda == 6) {
         habilitarBotonLov();
         novedadSeleccionada.getPeriodicidad().getCodigo();
      } else if (cualCelda == 7) {
         habilitarBotonLov();
         novedadSeleccionada.getPeriodicidad().getNombre();
      } else if (cualCelda == 8) {
         habilitarBotonLov();
         novedadSeleccionada.getTercero().getNombre();
      } else if (cualCelda == 9) {
         deshabilitarBotonLov();
         novedadSeleccionada.getObservaciones();
      } else if (cualCelda == 10) {
         deshabilitarBotonLov();
         novedadSeleccionada.getFechareporte();
      }
   }

   public void listaValoresBoton() {

      if (cualCelda == 0) {
         lovConceptos = null;
         cargarLovConceptos();
         contarRegistrosConceptos();
         RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (cualCelda == 1) {
         lovConceptos = null;
         cargarLovConceptos();
         contarRegistrosConceptos();
         RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (cualCelda == 6) {
         lovPeriodicidades = null;
         cargarLovPeriodicidades();
         contarRegistrosPeriodicidades();
         RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (cualCelda == 7) {
         lovPeriodicidades = null;
         cargarLovPeriodicidades();
         contarRegistrosPeriodicidades();
         RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (cualCelda == 8) {
         lovTerceros = null;
         cargarLovTerceros();
         contarRegistrosTerceros();
         RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
      }
   }

   public void editarCelda() {
      if (novedadSeleccionada != null) {
         editarNovedad = novedadSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoConceptoD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoConceptoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionConceptoD");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionConceptoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicial");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicial').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinal");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinal').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValorD");
            RequestContext.getCurrentInstance().execute("PF('editarValorD').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldoD");
            RequestContext.getCurrentInstance().execute("PF('editarSaldoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoPeriodicidadD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoPeriodicidadD').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombrePeriodicidadD");
            RequestContext.getCurrentInstance().execute("PF('editarNombrePeriodicidadD').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroD");
            RequestContext.getCurrentInstance().execute("PF('editarTerceroD').show()");
            cualCelda = -1;
         } else if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacionD");
            RequestContext.getCurrentInstance().execute("PF('editarObservacionD').show()");
            cualCelda = -1;
         } else if (cualCelda == 10) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaIngreso");
            RequestContext.getCurrentInstance().execute("PF('editarFechaIngreso').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         altoTabla = "290";
         novedadCodigoConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadCodigoConcepto");
         novedadCodigoConcepto.setFilterStyle("width: 85% !important");
         novedadDescripcionConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadDescripcionConcepto");
         novedadDescripcionConcepto.setFilterStyle("width: 85% !important");
         novedadValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadValor");
         novedadValor.setFilterStyle("width: 85% !important");
         novedadFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadFechaInicial");
         novedadFechaInicial.setFilterStyle("width: 85% !important");
         novedadFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadFechaFinal");
         novedadFechaFinal.setFilterStyle("width: 85% !important");
         novedadSaldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadSaldo");
         novedadSaldo.setFilterStyle("width: 85% !important");
         novedadCodigoPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadCodigoPeriodicidad");
         novedadCodigoPeriodicidad.setFilterStyle("width: 85% !important");
         novedadDescripcionPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadDescripcionPeriodicidad");
         novedadDescripcionPeriodicidad.setFilterStyle("width: 85% !important");
         novedadTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadTercero");
         novedadTercero.setFilterStyle("width: 85% !important");
         novedadObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadObservacion");
         novedadObservacion.setFilterStyle("width: 85% !important");
         novedadFechaReporte = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadFechaReporte");
         novedadFechaReporte.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
      }
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         cerrarFiltrado();
      }
      listNovedadesEmpleado = null;
      novedadSeleccionada = null;
      guardado = true;
      empleado = null;
      navegar("atras");
   }

   public void cerrarFiltrado() {
      altoTabla = "310";
      novedadCodigoConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadCodigoConcepto");
      novedadCodigoConcepto.setFilterStyle("display: none; visibility: hidden;");
      novedadDescripcionConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadDescripcionConcepto");
      novedadDescripcionConcepto.setFilterStyle("display: none; visibility: hidden;");
      novedadValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadValor");
      novedadValor.setFilterStyle("display: none; visibility: hidden;");
      novedadFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadFechaInicial");
      novedadFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      novedadFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadFechaFinal");
      novedadFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      novedadSaldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadSaldo");
      novedadSaldo.setFilterStyle("display: none; visibility: hidden;");
      novedadCodigoPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadCodigoPeriodicidad");
      novedadCodigoPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
      novedadDescripcionPeriodicidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadDescripcionPeriodicidad");
      novedadDescripcionPeriodicidad.setFilterStyle("display: none; visibility: hidden;");
      novedadTercero = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadTercero");
      novedadTercero.setFilterStyle("display: none; visibility: hidden;");
      novedadObservacion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadObservacion");
      novedadObservacion.setFilterStyle("display: none; visibility: hidden;");
      novedadFechaReporte = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesEmpleado:novedadFechaReporte");
      novedadFechaReporte.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      bandera = 0;
      filtrarListNovedadesEmpleado = null;
      tipoLista = 0;
   }

   public void guardarCambios() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listNovedadesEmpleadoBorrar.isEmpty()) {
               administrarEmplNovedad.borrarNovedad(listNovedadesEmpleadoBorrar);
               listNovedadesEmpleadoBorrar.clear();
            }
            if (!listNovedadesEmpleadoModificar.isEmpty()) {
               administrarEmplNovedad.editarNovedad(listNovedadesEmpleadoModificar);
               listNovedadesEmpleadoModificar.clear();
            }
            listNovedadesEmpleado = null;
            getListNovedadesEmpleado();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            novedadSeleccionada = null;
         }

         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      } catch (Exception e) {
         log.warn("Error guardarCambios : " + e.getMessage());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarSalir() {
      guardarCambios();
      salir();
   }

   public void cancelarSalir() {
      refrescar();
      salir();
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "NovedadesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesEmpleadoExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "NovedadesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (novedadSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADES");
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
      } else if (administrarRastros.verificarHistoricosTabla("NOVEDADES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void borrarNovedad() {

      if (novedadSeleccionada != null) {
         if (!listNovedadesEmpleadoModificar.isEmpty() && listNovedadesEmpleadoModificar.contains(novedadSeleccionada)) {
            int modIndex = listNovedadesEmpleadoModificar.indexOf(novedadSeleccionada);
            listNovedadesEmpleadoModificar.remove(modIndex);
            listNovedadesEmpleadoBorrar.add(novedadSeleccionada);
         } else {
            listNovedadesEmpleadoBorrar.add(novedadSeleccionada);
         }
         listNovedadesEmpleado.remove(novedadSeleccionada);
         if (tipoLista == 1) {
            filtrarListNovedadesEmpleado.remove(novedadSeleccionada);
         }
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
         novedadSeleccionada = null;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void verDetalle(Novedades novedad) {
      novedadSeleccionada = novedad;
      FacesContext fc = FacesContext.getCurrentInstance();
      fc.getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "detallenovedad");
   }

   public void cargarLovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = administrarEmplNovedad.lovConceptos();
      }
   }

   public void cargarLovPeriodicidades() {
      if (lovPeriodicidades == null) {
         lovPeriodicidades = administrarEmplNovedad.lovPeriodicidades();
      }
   }

   public void cargarLovTerceros() {
      if (lovTerceros == null) {
         lovTerceros = administrarEmplNovedad.lovTerceros();
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

   public void asignarIndex(Novedades novedad, int dlg) {
      novedadSeleccionada = novedad;
      if (dlg == 1) {
         lovConceptos = null;
         cargarLovConceptos();
         contarRegistrosConceptos();
         RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (dlg == 2) {
         lovPeriodicidades = null;
         cargarLovPeriodicidades();
         contarRegistrosPeriodicidades();
         RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').show()");
      } else if (dlg == 3) {
         lovTerceros = null;
         cargarLovTerceros();
         contarRegistrosTerceros();
         RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
      }
   }

   public void actualizarConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      novedadSeleccionada.setConcepto(conceptoSeleccionado);
      if (listNovedadesEmpleadoModificar.isEmpty()) {
         listNovedadesEmpleadoModificar.add(novedadSeleccionada);
      } else if (!listNovedadesEmpleadoModificar.contains(novedadSeleccionada)) {
         listNovedadesEmpleadoModificar.add(novedadSeleccionada);
      }
      guardado = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      lovConceptosFiltrar = null;
      conceptoSeleccionado = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovConceptos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
      context.reset("formularioDialogos:lovConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public void cancelarCambioConceptos() {
      lovConceptosFiltrar = null;
      conceptoSeleccionado = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovConceptos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
      context.reset("formularioDialogos:lovConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public void actualizarPeriodicidades() {
      RequestContext context = RequestContext.getCurrentInstance();
      novedadSeleccionada.setPeriodicidad(periodicidadSeleccionada);
      if (listNovedadesEmpleadoModificar.isEmpty()) {
         listNovedadesEmpleadoModificar.add(novedadSeleccionada);
      } else if (!listNovedadesEmpleadoModificar.contains(novedadSeleccionada)) {
         listNovedadesEmpleadoModificar.add(novedadSeleccionada);
      }
      guardado = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      lovPeriodicidadesFiltrar = null;
      periodicidadSeleccionada = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovPeriodicidades");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
      context.reset("formularioDialogos:lovPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
   }

   public void cancelarCambioPeriodicidades() {
      RequestContext context = RequestContext.getCurrentInstance();
      lovPeriodicidadesFiltrar = null;
      periodicidadSeleccionada = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formularioDialogos:periodicidadesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovPeriodicidades");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
      context.reset("formularioDialogos:lovPeriodicidades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovPeriodicidades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('periodicidadesDialogo').hide()");
   }

   public void actualizarTerceros() {
      RequestContext context = RequestContext.getCurrentInstance();
      novedadSeleccionada.setTercero(terceroSeleccionado);
      if (listNovedadesEmpleadoModificar.isEmpty()) {
         listNovedadesEmpleadoModificar.add(novedadSeleccionada);
      } else if (!listNovedadesEmpleadoModificar.contains(novedadSeleccionada)) {
         listNovedadesEmpleadoModificar.add(novedadSeleccionada);
      }
      guardado = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
      lovTercerosFiltrar = null;
      terceroSeleccionado = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
      context.reset("formularioDialogos:lovTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
   }

   public void cancelarCambioTerceros() {
      RequestContext context = RequestContext.getCurrentInstance();
      lovTercerosFiltrar = null;
      terceroSeleccionado = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
      context.reset("formularioDialogos:lovTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");

   }

   public void contarRegistrosConceptos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroConceptos");
   }

   public void contarRegistrosPeriodicidades() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroPeriodicidades");

   }

   public void contarRegistrosTerceros() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTerceros");

   }

   //GETTERS AND SETTERS
   public List<Novedades> getListNovedadesEmpleado() {
      try {
         if (listNovedadesEmpleado == null) {
            listNovedadesEmpleado = administrarEmplNovedad.listNovedadesEmpleado(empleado.getSecuencia());
         }
         if (!listNovedadesEmpleado.isEmpty()) {
            for (int i = 0; i < listNovedadesEmpleado.size(); i++) {
               if (listNovedadesEmpleado.get(i).getConcepto() == null) {
                  listNovedadesEmpleado.get(i).setConcepto(new Conceptos());
               }
               if (listNovedadesEmpleado.get(i).getPeriodicidad() == null) {
                  listNovedadesEmpleado.get(i).setPeriodicidad(new Periodicidades());
               }
               if (listNovedadesEmpleado.get(i).getTercero() == null) {
                  listNovedadesEmpleado.get(i).setTercero(new Terceros());
               }
            }
         }
         return listNovedadesEmpleado;
      } catch (Exception e) {
         log.warn("Error...!! getListNovedadesEmpleado : " + e.toString());
         return null;
      }
   }

   public void setListNovedadesEmpleado(List<Novedades> listNovedadesEmpleado) {
      this.listNovedadesEmpleado = listNovedadesEmpleado;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public List<Novedades> getFiltrarListNovedadesEmpleado() {
      return filtrarListNovedadesEmpleado;
   }

   public void setFiltrarListNovedadesEmpleado(List<Novedades> filtrarListNovedadesEmpleado) {
      this.filtrarListNovedadesEmpleado = filtrarListNovedadesEmpleado;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public Novedades getEditarNovedad() {
      return editarNovedad;
   }

   public void setEditarNovedad(Novedades editarNov) {
      this.editarNovedad = editarNov;
   }

   public Novedades getNovedadSeleccionada() {
      return novedadSeleccionada;
   }

   public void setNovedadSeleccionada(Novedades novedadSeleccionada) {
      this.novedadSeleccionada = novedadSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesEmpleado");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public List<Conceptos> getLovConceptos() {
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptos) {
      this.lovConceptos = lovConceptos;
   }

   public List<Conceptos> getLovConceptosFiltrar() {
      return lovConceptosFiltrar;
   }

   public void setLovConceptosFiltrar(List<Conceptos> lovConceptosFiltrar) {
      this.lovConceptosFiltrar = lovConceptosFiltrar;
   }

   public Conceptos getConceptoSeleccionado() {
      return conceptoSeleccionado;
   }

   public void setConceptoSeleccionado(Conceptos conceptoSeleccionado) {
      this.conceptoSeleccionado = conceptoSeleccionado;
   }

   public List<Periodicidades> getLovPeriodicidades() {
      return lovPeriodicidades;
   }

   public void setLovPeriodicidades(List<Periodicidades> lovPeriodicidades) {
      this.lovPeriodicidades = lovPeriodicidades;
   }

   public List<Periodicidades> getLovPeriodicidadesFiltrar() {
      return lovPeriodicidadesFiltrar;
   }

   public void setLovPeriodicidadesFiltrar(List<Periodicidades> lovPeriodicidadesFiltrar) {
      this.lovPeriodicidadesFiltrar = lovPeriodicidadesFiltrar;
   }

   public Periodicidades getPeriodicidadSeleccionada() {
      return periodicidadSeleccionada;
   }

   public void setPeriodicidadSeleccionada(Periodicidades periodicidadSeleccionada) {
      this.periodicidadSeleccionada = periodicidadSeleccionada;
   }

   public List<Terceros> getLovTerceros() {
      return lovTerceros;
   }

   public void setLovTerceros(List<Terceros> lovTerceros) {
      this.lovTerceros = lovTerceros;
   }

   public List<Terceros> getLovTercerosFiltrar() {
      return lovTercerosFiltrar;
   }

   public void setLovTercerosFiltrar(List<Terceros> lovTercerosFiltrar) {
      this.lovTercerosFiltrar = lovTercerosFiltrar;
   }

   public Terceros getTerceroSeleccionado() {
      return terceroSeleccionado;
   }

   public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
      this.terceroSeleccionado = terceroSeleccionado;
   }

   public String getInfoRegistroConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovConceptos");
      infoRegistroConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroConceptos;
   }

   public void setInfoRegistroConceptos(String infoRegistroConceptos) {
      this.infoRegistroConceptos = infoRegistroConceptos;
   }

   public String getInfoRegistroPeriodicidades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovPeriodicidades");
      infoRegistroPeriodicidades = String.valueOf(tabla.getRowCount());
      return infoRegistroPeriodicidades;
   }

   public void setInfoRegistroPeriodicidades(String infoRegistroPeriodicidades) {
      this.infoRegistroPeriodicidades = infoRegistroPeriodicidades;
   }

   public String getInfoRegistroTerceros() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTerceros");
      infoRegistroTerceros = String.valueOf(tabla.getRowCount());
      return infoRegistroTerceros;
   }

   public void setInfoRegistroTerceros(String infoRegistroTerceros) {
      this.infoRegistroTerceros = infoRegistroTerceros;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
