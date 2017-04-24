/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Diagnosticoscapitulos;
import Entidades.Diagnosticoscategorias;
import Entidades.Diagnosticossecciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDiagnosticosCategoriasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlDiagnosticosCategorias implements Serializable {

   @EJB
   AdministrarDiagnosticosCategoriasInterface administrarDiagnosticosCategorias;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //DIAGNOSTICOS CATEGORIAS
   private List<Diagnosticoscategorias> listaDiagnosticosCategorias;
   private List<Diagnosticoscategorias> listaFiltrarDiagnosticosCategorias;
   private List<Diagnosticoscategorias> listaDiagnosticosCategoriasCrear;
   private List<Diagnosticoscategorias> listaDiagnosticosCategoriasModificar;
   private List<Diagnosticoscategorias> listaDiagnosticosCategoriasBorrar;
   private Diagnosticoscategorias nuevoDiagnosticoCategoria;
   private Diagnosticoscategorias duplicarDiagnosticoCategoria;
   private Diagnosticoscategorias editarDiagnosticoCategoria;
   private Diagnosticoscategorias diagnosticoCategoriaSeleccionado;
   //DIAGNOSTICOS CAPITULOS
   private List<Diagnosticoscapitulos> listaDiagnosticosCapitulos;
   private List<Diagnosticoscapitulos> listaFiltrarDiagnosticosCapitulos;
   private List<Diagnosticoscapitulos> listaDiagnosticosCapitulosCrear;
   private List<Diagnosticoscapitulos> listaDiagnosticosCapitulosModificar;
   private List<Diagnosticoscapitulos> listaDiagnosticosCapitulosBorrar;
   private Diagnosticoscapitulos nuevoDiagnosticoCapitulo;
   private Diagnosticoscapitulos duplicarDiagnosticoCapitulo;
   private Diagnosticoscapitulos editarDiagnosticoCapitulo;
   private Diagnosticoscapitulos diagnosticoCapituloSeleccionado;
   //DIAGNOSTICOS SECCIONES
   private List<Diagnosticossecciones> listaDiagnosticosSecciones;
   private List<Diagnosticossecciones> listaFiltrarDiagnosticosSecciones;
   private List<Diagnosticossecciones> listaDiagnosticosSeccionesCrear;
   private List<Diagnosticossecciones> listaDiagnosticosSeccionesModificar;
   private List<Diagnosticossecciones> listaDiagnosticosSeccionesBorrar;
   private Diagnosticossecciones nuevoDiagnosticoSeccion;
   private Diagnosticossecciones duplicarDiagnosticoSeccion;
   private Diagnosticossecciones editarDiagnosticoSeccion;
   private Diagnosticossecciones diagnosticoSeccionSeleccionado;
   ///OTROS
   private String altoTabla, infoRegistroCategorias, infoRegistroCapitulos, infoRegistroSecciones, mensajeValidacion, tablaImprimir, nombreArchivo;
   private Boolean activarLov, aceptar, guardado, permitirIndex;
   private int cualCelda, cualCeldaCapitulo, cualCeldaSeccion, tipoLista, cualTabla, tipoActualizacion, k, bandera, registrosBorrados;
   private Column codigo, descripcion, codCapitulo, descCapitulo, codSeccion, descSeccion;
   private BigInteger l;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlDiagnosticosCategorias() {
      listaDiagnosticosCategoriasCrear = new ArrayList<Diagnosticoscategorias>();
      listaDiagnosticosCategoriasBorrar = new ArrayList<Diagnosticoscategorias>();
      listaDiagnosticosCategoriasModificar = new ArrayList<Diagnosticoscategorias>();
      nuevoDiagnosticoCategoria = new Diagnosticoscategorias();
      duplicarDiagnosticoCategoria = new Diagnosticoscategorias();
      editarDiagnosticoCategoria = new Diagnosticoscategorias();

      listaDiagnosticosCapitulosCrear = new ArrayList<Diagnosticoscapitulos>();
      listaDiagnosticosCapitulosBorrar = new ArrayList<Diagnosticoscapitulos>();
      listaDiagnosticosCapitulosModificar = new ArrayList<Diagnosticoscapitulos>();
      nuevoDiagnosticoCapitulo = new Diagnosticoscapitulos();
      duplicarDiagnosticoCapitulo = new Diagnosticoscapitulos();
      editarDiagnosticoCapitulo = new Diagnosticoscapitulos();

      listaDiagnosticosSeccionesCrear = new ArrayList<Diagnosticossecciones>();
      listaDiagnosticosSeccionesBorrar = new ArrayList<Diagnosticossecciones>();
      listaDiagnosticosSeccionesModificar = new ArrayList<Diagnosticossecciones>();
      nuevoDiagnosticoSeccion = new Diagnosticossecciones();
      duplicarDiagnosticoSeccion = new Diagnosticossecciones();
      editarDiagnosticoSeccion = new Diagnosticossecciones();

      permitirIndex = true;
      aceptar = true;
      tipoLista = 0;
      altoTabla = "50";
      activarLov = true;
      cualCelda = -1;
      cualCeldaCapitulo = -1;
      cualCeldaSeccion = -1;
      k = 0;
      guardado = true;
//        cualTabla = -1;
   mapParametros.put ("paginaAnterior", paginaAnterior);

   }


   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listaDiagnosticosCapitulos = null;
      getListaDiagnosticosCapitulos();
      if (listaDiagnosticosCapitulos != null) {
         if (!listaDiagnosticosCapitulos.isEmpty()) {
            diagnosticoCapituloSeleccionado = listaDiagnosticosCapitulos.get(0);
         }
      }

      listaDiagnosticosSecciones = null;
      getListaDiagnosticosSecciones();
//        if (listaDiagnosticosSecciones != null) {
//            if (!listaDiagnosticosSecciones.isEmpty()) {
//                diagnosticoSeccionSeleccionado = listaDiagnosticosSecciones.get(0);
//            }
//        }
      listaDiagnosticosCategorias = null;
      getListaDiagnosticosCategorias();
//        if (listaDiagnosticosCategorias != null) {
//            if (!listaDiagnosticosCategorias.isEmpty()) {
//                diagnosticoCategoriaSeleccionado = listaDiagnosticosCategorias.get(0);
//            }
//        }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listaDiagnosticosCapitulos = null;
      getListaDiagnosticosCapitulos();
      if (listaDiagnosticosCapitulos != null) {
         if (!listaDiagnosticosCapitulos.isEmpty()) {
            diagnosticoCapituloSeleccionado = listaDiagnosticosCapitulos.get(0);
         }
      }

      listaDiagnosticosSecciones = null;
      getListaDiagnosticosSecciones();
//        if (listaDiagnosticosSecciones != null) {
//            if (!listaDiagnosticosSecciones.isEmpty()) {
//                diagnosticoSeccionSeleccionado = listaDiagnosticosSecciones.get(0);
//            }
//        }
      listaDiagnosticosCategorias = null;
      getListaDiagnosticosCategorias();
//        if (listaDiagnosticosCategorias != null) {
//            if (!listaDiagnosticosCategorias.isEmpty()) {
//                diagnosticoCategoriaSeleccionado = listaDiagnosticosCategorias.get(0);
//            }
//        }
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
String pagActual = "diagnosticoscategorias";
         
         
         


         
         
         
         
         
         
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

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarDiagnosticosCategorias.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public String redirigir() {
      return paginaAnterior;
   }

   public void cambiarIndiceCapitulo(Diagnosticoscapitulos capitulo, int celda) {
      if (permitirIndex == true) {
         diagnosticoCapituloSeleccionado = capitulo;
         cualCeldaCapitulo = celda;
         cualTabla = 1;
         tablaImprimir = ":formExportarCapitulos:datosDiagnosticosCapitulosExportar";
         nombreArchivo = "DiagnosticosCapitulosXML";
         listaDiagnosticosSecciones = null;
         listaDiagnosticosSecciones = administrarDiagnosticosCategorias.consultarDiagnosticoSeccion(diagnosticoCapituloSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("form:datosSecciones");
         contarRegistrosSecciones();
         if (cualCeldaCapitulo == 0) {
            diagnosticoCapituloSeleccionado.getCodigo();
         } else if (cualCeldaCapitulo == 1) {
            diagnosticoCapituloSeleccionado.getDescripcion();
         }
      }
   }

   public void cambiarIndiceSeccion(Diagnosticossecciones seccion, int celda) {
      if (permitirIndex == true) {
         diagnosticoSeccionSeleccionado = seccion;
         cualCeldaSeccion = celda;
         cualTabla = 2;
         tablaImprimir = ":formExportarSecciones:datosDiagnosticosSeccionesExportar";
         nombreArchivo = "DiagnosticosSeccionesXML";
         listaDiagnosticosCategorias = null;
         listaDiagnosticosCategorias = administrarDiagnosticosCategorias.consultarDiagnosticoCategoria(diagnosticoSeccionSeleccionado.getSecuencia());
         RequestContext.getCurrentInstance().update("form:datosCategorias");
         contarRegistroCategorias();
         if (cualCeldaSeccion == 0) {
            diagnosticoSeccionSeleccionado.getCodigo();
         } else if (cualCeldaSeccion == 1) {
            diagnosticoSeccionSeleccionado.getDescripcion();
         }
      }
   }

   public void cambiarIndice(Diagnosticoscategorias categoria, int celda) {
      if (permitirIndex == true) {
         diagnosticoCategoriaSeleccionado = categoria;
         cualCelda = celda;
         cualTabla = 3;
         nombreArchivo = "DiagnosticosCategoriasXML";
         tablaImprimir = ":formExportarCategorias:datosDiagnosticosCategoriasExportar";
         if (cualCelda == 0) {
            diagnosticoCategoriaSeleccionado.getCodigo();
         } else if (cualCelda == 1) {
            diagnosticoCategoriaSeleccionado.getDescripcion();
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosCategorias:codigo");
         codigo.setFilterStyle("display:none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosCategorias:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         codCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:codigoCapitulo");
         codCapitulo.setFilterStyle("display: none; visibility: hidden;");
         descCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:descripcionCapitulo");
         descCapitulo.setFilterStyle("display: none; visibility: hidden;");
         codSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:codigoSeccion");
         codSeccion.setFilterStyle("display: none; visibility: hidden;");
         descSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:descripcionSeccion");
         descSeccion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosCategorias");
         RequestContext.getCurrentInstance().update("form:datosCapitulos");
         RequestContext.getCurrentInstance().update("form:datosSecciones");
         bandera = 0;
         listaFiltrarDiagnosticosCategorias = null;
         listaFiltrarDiagnosticosCapitulos = null;
         listaFiltrarDiagnosticosSecciones = null;
         tipoLista = 0;
         altoTabla = "50";
      }

      listaDiagnosticosCapitulosBorrar.clear();
      listaDiagnosticosCapitulosCrear.clear();
      listaDiagnosticosCapitulosModificar.clear();

      listaDiagnosticosSeccionesBorrar.clear();
      listaDiagnosticosSeccionesCrear.clear();
      listaDiagnosticosSeccionesModificar.clear();

      listaDiagnosticosCategoriasBorrar.clear();
      listaDiagnosticosCategoriasCrear.clear();
      listaDiagnosticosCategoriasModificar.clear();

      diagnosticoCapituloSeleccionado = null;
      diagnosticoSeccionSeleccionado = null;
      diagnosticoCategoriaSeleccionado = null;

      listaDiagnosticosCapitulos = null;
      getListaDiagnosticosCapitulos();
      contarRegistrosCapitulos();

      listaDiagnosticosSecciones = null;
      getListaDiagnosticosSecciones();
      contarRegistrosSecciones();

      listaDiagnosticosCategorias = null;
//        if(listaDiagnosticosCategorias == null){
      getListaDiagnosticosCategorias();
//            listaDiagnosticosCategorias = new ArrayList<Diagnosticoscategorias>();
//        }
      contarRegistroCategorias();
      k = 0;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosCategorias");
      RequestContext.getCurrentInstance().update("form:datosCapitulos");
      RequestContext.getCurrentInstance().update("form:datosSecciones");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void salir() {  limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosCategorias:codigo");
         codigo.setFilterStyle("display:none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosCategorias:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         codCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:codigoCapitulo");
         codCapitulo.setFilterStyle("display: none; visibility: hidden;");
         descCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:descripcionCapitulo");
         descCapitulo.setFilterStyle("display: none; visibility: hidden;");
         codSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:codigoSeccion");
         codSeccion.setFilterStyle("display: none; visibility: hidden;");
         descSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:descripcionSeccion");
         descSeccion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosCategorias");
         RequestContext.getCurrentInstance().update("form:datosCapitulos");
         RequestContext.getCurrentInstance().update("form:datosSecciones");
         bandera = 0;
         listaFiltrarDiagnosticosCategorias = null;
         listaFiltrarDiagnosticosCapitulos = null;
         tipoLista = 0;
         altoTabla = "50";
      }

      listaDiagnosticosCategoriasBorrar.clear();;
      listaDiagnosticosCategoriasCrear.clear();
      listaDiagnosticosCategoriasModificar.clear();
      diagnosticoCategoriaSeleccionado = null;

      listaDiagnosticosCapitulosBorrar.clear();
      listaDiagnosticosCapitulosCrear.clear();
      listaDiagnosticosCapitulosModificar.clear();
      diagnosticoCapituloSeleccionado = null;

      listaDiagnosticosSeccionesBorrar.clear();
      listaDiagnosticosSeccionesCrear.clear();
      listaDiagnosticosSeccionesModificar.clear();
      diagnosticoSeccionSeleccionado = null;

      k = 0;
      listaDiagnosticosCategorias = null;
      listaDiagnosticosCapitulos = null;
      listaDiagnosticosSecciones = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosCategorias");
      RequestContext.getCurrentInstance().update("form:datosCapitulos");
      RequestContext.getCurrentInstance().update("form:datosSecciones");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "30";
         codigo = (Column) c.getViewRoot().findComponent("form:datosCategorias:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosCategorias:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         codCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:codigoCapitulo");
         codCapitulo.setFilterStyle("width: 85% !important");
         descCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:descripcionCapitulo");
         descCapitulo.setFilterStyle("width: 85% !important");
         codSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:codigoSeccion");
         codSeccion.setFilterStyle("width: 85% !important");
         descSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:descripcionSeccion");
         descSeccion.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosCategorias");
         RequestContext.getCurrentInstance().update("form:datosCapitulos");
         RequestContext.getCurrentInstance().update("form:datosSecciones");
         bandera = 1;
      } else if (bandera == 1) {
         altoTabla = "50";
         codigo = (Column) c.getViewRoot().findComponent("form:datosCategorias:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosCategorias:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         codCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:codigoCapitulo");
         codCapitulo.setFilterStyle("display: none; visibility: hidden;");
         descCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:descripcionCapitulo");
         descCapitulo.setFilterStyle("display: none; visibility: hidden;");
         codSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:codigoSeccion");
         codSeccion.setFilterStyle("display: none; visibility: hidden;");
         descSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:descripcionSeccion");
         descSeccion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosCapitulos");
         RequestContext.getCurrentInstance().update("form:datosCategorias");
         RequestContext.getCurrentInstance().update("form:datosSecciones");
         bandera = 0;
         listaFiltrarDiagnosticosCategorias = null;
         listaFiltrarDiagnosticosCapitulos = null;
         listaFiltrarDiagnosticosSecciones = null;
         tipoLista = 0;
      }
   }

   public void modificarCategoria(Diagnosticoscategorias categoria, String confirmarCambio, String valorConfirmar) {
      diagnosticoCategoriaSeleccionado = categoria;
      int contador = 0;
      int codvacio = 0;
      boolean coincidencias = false;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (diagnosticoCategoriaSeleccionado.getCodigo() == null || diagnosticoCategoriaSeleccionado.getCodigo().equals(" ")) {
            mensajeValidacion = " NO PUEDEN HABER CAMPOS VACIOS";
            coincidencias = false;
            diagnosticoCategoriaSeleccionado.setCodigo(diagnosticoCategoriaSeleccionado.getCodigo());
         } else if (diagnosticoCategoriaSeleccionado.getDescripcion() == null || diagnosticoCategoriaSeleccionado.getDescripcion().isEmpty()) {
            mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
            diagnosticoCategoriaSeleccionado.setDescripcion(diagnosticoCategoriaSeleccionado.getDescripcion());
            coincidencias = false;
         } else {
            for (int i = 0; i < listaDiagnosticosCategorias.size(); i++) {
               if (diagnosticoCategoriaSeleccionado.getSecuencia() != listaDiagnosticosCategorias.get(i).getSecuencia()) {
                  if (diagnosticoCategoriaSeleccionado.getCodigo().equals(listaDiagnosticosCategorias.get(i).getCodigo())) {
                     contador++;
                  }
               }
            }
            if (contador > 0) {
               mensajeValidacion = "CODIGOS REPETIDOS";
               coincidencias = false;
               diagnosticoCategoriaSeleccionado.setCodigo(diagnosticoCategoriaSeleccionado.getCodigo());
            } else {
               coincidencias = true;
            }

            for (int j = 0; j < listaDiagnosticosCategorias.size(); j++) {
               if (diagnosticoCategoriaSeleccionado.getSecuencia() != listaDiagnosticosCategorias.get(j).getSecuencia()) {
                  if (diagnosticoCategoriaSeleccionado.getDescripcion().equals(listaDiagnosticosCategorias.get(j).getDescripcion())) {
                     contador++;
                  }
               }
            }
            if (contador > 0) {
               mensajeValidacion = "DESCRIPCION REPETIDA";
               coincidencias = false;
               diagnosticoCategoriaSeleccionado.setDescripcion(diagnosticoCategoriaSeleccionado.getDescripcion());
            } else {
               coincidencias = true;
            }

         }

      }

      if (coincidencias == true) {
         if (!listaDiagnosticosCategoriasCrear.contains(diagnosticoCategoriaSeleccionado)) {
            if (!listaDiagnosticosCategoriasModificar.contains(diagnosticoCategoriaSeleccionado)) {
               listaDiagnosticosCategoriasModificar.add(diagnosticoCategoriaSeleccionado);
            }
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionModificar");
         RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
      }

      RequestContext.getCurrentInstance().update("form:datosCategorias");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void modificarCapitulo(Diagnosticoscapitulos capitulo, String confirmarCambio, String valorConfirmar) {
      diagnosticoCapituloSeleccionado = capitulo;
      int contador = 0;
      int codvacio = 0;
      boolean coincidencias = false;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (diagnosticoCapituloSeleccionado.getCodigo() == null || diagnosticoCapituloSeleccionado.getCodigo().equals(" ")) {
            mensajeValidacion = " NO PUEDEN HABER CAMPOS VACIOS";
            coincidencias = false;
            diagnosticoCapituloSeleccionado.setCodigo(diagnosticoCapituloSeleccionado.getCodigo());
         } else if (diagnosticoCapituloSeleccionado.getDescripcion() == null || diagnosticoCapituloSeleccionado.getDescripcion().isEmpty()) {
            mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
            diagnosticoCapituloSeleccionado.setDescripcion(diagnosticoCapituloSeleccionado.getDescripcion());
            coincidencias = false;
         } else {
            for (int i = 0; i < listaDiagnosticosCapitulos.size(); i++) {
               if (diagnosticoCapituloSeleccionado.getSecuencia() != listaDiagnosticosCapitulos.get(i).getSecuencia()) {
                  if (diagnosticoCapituloSeleccionado.getCodigo().equals(listaDiagnosticosCapitulos.get(i).getCodigo())) {
                     contador++;
                  }
               }
            }
            if (contador > 0) {
               mensajeValidacion = "CODIGOS REPETIDOS";
               coincidencias = false;
               diagnosticoCapituloSeleccionado.setCodigo(diagnosticoCapituloSeleccionado.getCodigo());
            } else {
               coincidencias = true;
            }

            for (int j = 0; j < listaDiagnosticosCapitulos.size(); j++) {
               if (diagnosticoCapituloSeleccionado.getSecuencia() != listaDiagnosticosCapitulos.get(j).getSecuencia()) {
                  if (diagnosticoCapituloSeleccionado.getDescripcion().equals(listaDiagnosticosCapitulos.get(j).getDescripcion())) {
                     contador++;
                  }
               }
            }
            if (contador > 0) {
               mensajeValidacion = "DESCRIPCION REPETIDA";
               coincidencias = false;
               diagnosticoCapituloSeleccionado.setDescripcion(diagnosticoCapituloSeleccionado.getDescripcion());
            } else {
               coincidencias = true;
            }

         }

      }

      if (coincidencias == true) {
         if (!listaDiagnosticosCapitulosCrear.contains(diagnosticoCapituloSeleccionado)) {
            if (!listaDiagnosticosCapitulosModificar.contains(diagnosticoCapituloSeleccionado)) {
               listaDiagnosticosCapitulosModificar.add(diagnosticoCapituloSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionModificar");
         RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
      }

      RequestContext.getCurrentInstance().update("form:datosCapitulos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void modificarSeccion(Diagnosticossecciones seccion, String confirmarCambio, String valorConfirmar) {
      diagnosticoSeccionSeleccionado = seccion;
      int contador = 0;
      int codvacio = 0;
      boolean coincidencias = false;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (diagnosticoSeccionSeleccionado.getCodigo() == null || diagnosticoSeccionSeleccionado.getCodigo().equals(" ")) {
            mensajeValidacion = " NO PUEDEN HABER CAMPOS VACIOS";
            coincidencias = false;
            diagnosticoSeccionSeleccionado.setCodigo(diagnosticoSeccionSeleccionado.getCodigo());
         } else if (diagnosticoSeccionSeleccionado.getDescripcion() == null || diagnosticoSeccionSeleccionado.getDescripcion().isEmpty()) {
            mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
            diagnosticoSeccionSeleccionado.setDescripcion(diagnosticoSeccionSeleccionado.getDescripcion());
            coincidencias = false;
         } else {
            for (int i = 0; i < listaDiagnosticosSecciones.size(); i++) {
               if (diagnosticoSeccionSeleccionado.getSecuencia() != listaDiagnosticosSecciones.get(i).getSecuencia()) {
                  if (diagnosticoSeccionSeleccionado.getCodigo().equals(listaDiagnosticosSecciones.get(i).getCodigo())) {
                     contador++;
                  }
               }
            }
            if (contador > 0) {
               mensajeValidacion = "CODIGOS REPETIDOS";
               coincidencias = false;
               diagnosticoSeccionSeleccionado.setCodigo(diagnosticoSeccionSeleccionado.getCodigo());
            } else {
               coincidencias = true;
            }

            for (int j = 0; j < listaDiagnosticosSecciones.size(); j++) {
               if (diagnosticoSeccionSeleccionado.getSecuencia() != listaDiagnosticosSecciones.get(j).getSecuencia()) {
                  if (diagnosticoSeccionSeleccionado.getDescripcion().equals(listaDiagnosticosSecciones.get(j).getDescripcion())) {
                     contador++;
                  }
               }
            }
            if (contador > 0) {
               mensajeValidacion = "DESCRIPCION REPETIDA";
               coincidencias = false;
               diagnosticoSeccionSeleccionado.setDescripcion(diagnosticoSeccionSeleccionado.getDescripcion());
            } else {
               coincidencias = true;
            }

         }

      }

      if (coincidencias == true) {
         if (!listaDiagnosticosSeccionesCrear.contains(diagnosticoSeccionSeleccionado)) {
            if (!listaDiagnosticosSeccionesModificar.contains(diagnosticoSeccionSeleccionado)) {
               listaDiagnosticosSeccionesModificar.add(diagnosticoSeccionSeleccionado);
            }
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionModificar");
         RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
      }

      RequestContext.getCurrentInstance().update("form:datosSecciones");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void borrarCategoria() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (diagnosticoCategoriaSeleccionado != null) {
         if (!listaDiagnosticosCategoriasModificar.isEmpty() && listaDiagnosticosCategoriasModificar.contains(diagnosticoCategoriaSeleccionado)) {
            listaDiagnosticosCategoriasModificar.remove(listaDiagnosticosCategoriasModificar.indexOf(diagnosticoCategoriaSeleccionado));
            listaDiagnosticosCategoriasBorrar.add(diagnosticoCategoriaSeleccionado);
         } else if (!listaDiagnosticosCategoriasCrear.isEmpty() && listaDiagnosticosCategoriasCrear.contains(diagnosticoCategoriaSeleccionado)) {
            listaDiagnosticosCategoriasCrear.remove(listaDiagnosticosCategoriasCrear.indexOf(diagnosticoCategoriaSeleccionado));
         } else {
            listaDiagnosticosCategoriasBorrar.add(diagnosticoCategoriaSeleccionado);
         }
         listaDiagnosticosCategorias.remove(diagnosticoCategoriaSeleccionado);

         if (tipoLista == 1) {
            listaFiltrarDiagnosticosCategorias.remove(diagnosticoCategoriaSeleccionado);
         }
         RequestContext.getCurrentInstance().update("form:datosCategorias");
         contarRegistroCategorias();
         diagnosticoCategoriaSeleccionado = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

      if (diagnosticoCapituloSeleccionado != null) {
         if (!listaDiagnosticosCapitulosModificar.isEmpty() && listaDiagnosticosCapitulosModificar.contains(diagnosticoCapituloSeleccionado)) {
            listaDiagnosticosCapitulosModificar.remove(listaDiagnosticosCapitulosModificar.indexOf(diagnosticoCapituloSeleccionado));
            listaDiagnosticosCapitulosBorrar.add(diagnosticoCapituloSeleccionado);
         } else if (!listaDiagnosticosCapitulosCrear.isEmpty() && listaDiagnosticosCapitulosCrear.contains(diagnosticoCapituloSeleccionado)) {
            listaDiagnosticosCapitulosCrear.remove(listaDiagnosticosCapitulosCrear.indexOf(diagnosticoCapituloSeleccionado));
         } else {
            listaDiagnosticosCapitulosBorrar.add(diagnosticoCapituloSeleccionado);
         }
         listaDiagnosticosCapitulos.remove(diagnosticoCapituloSeleccionado);

         if (tipoLista == 1) {
            listaFiltrarDiagnosticosCapitulos.remove(diagnosticoCapituloSeleccionado);
         }
         RequestContext.getCurrentInstance().update("form:datosCapitulos");
         contarRegistrosCapitulos();
         diagnosticoCapituloSeleccionado = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      if (diagnosticoSeccionSeleccionado != null) {
         if (!listaDiagnosticosSeccionesModificar.isEmpty() && listaDiagnosticosSeccionesModificar.contains(diagnosticoSeccionSeleccionado)) {
            listaDiagnosticosSeccionesModificar.remove(listaDiagnosticosSeccionesModificar.indexOf(diagnosticoSeccionSeleccionado));
            listaDiagnosticosSeccionesBorrar.add(diagnosticoSeccionSeleccionado);
         } else if (!listaDiagnosticosSeccionesCrear.isEmpty() && listaDiagnosticosSeccionesCrear.contains(diagnosticoSeccionSeleccionado)) {
            listaDiagnosticosSeccionesCrear.remove(listaDiagnosticosSeccionesCrear.indexOf(diagnosticoSeccionSeleccionado));
         } else {
            listaDiagnosticosSeccionesBorrar.add(diagnosticoSeccionSeleccionado);
         }
         listaDiagnosticosSecciones.remove(diagnosticoSeccionSeleccionado);

         if (tipoLista == 1) {
            listaFiltrarDiagnosticosSecciones.remove(diagnosticoSeccionSeleccionado);
         }
         RequestContext.getCurrentInstance().update("form:datosSecciones");
         contarRegistrosSecciones();
         diagnosticoSeccionSeleccionado = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void revisarDialogoGuardar() {

      if (!listaDiagnosticosCategoriasBorrar.isEmpty() || !listaDiagnosticosCategoriasCrear.isEmpty() || !listaDiagnosticosCategoriasModificar.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      } else if (!listaDiagnosticosCapitulosBorrar.isEmpty() || !listaDiagnosticosCapitulosCrear.isEmpty() || !listaDiagnosticosCapitulosModificar.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      } else if (!listaDiagnosticosSeccionesBorrar.isEmpty() || !listaDiagnosticosSeccionesCrear.isEmpty() || !listaDiagnosticosSeccionesModificar.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarCategoria() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado == false) {
         if (!listaDiagnosticosCategoriasBorrar.isEmpty()) {
            administrarDiagnosticosCategorias.borrarDiagnosticoCategoria(listaDiagnosticosCategoriasBorrar);
            registrosBorrados = listaDiagnosticosCategoriasBorrar.size();
            RequestContext.getCurrentInstance().update("formularioDialogos:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            listaDiagnosticosCategoriasBorrar.clear();
         }
         if (!listaDiagnosticosCategoriasModificar.isEmpty()) {
            administrarDiagnosticosCategorias.editarDiagnosticoCategoria(listaDiagnosticosCategoriasModificar);
            listaDiagnosticosCategoriasModificar.clear();
         }
         if (!listaDiagnosticosCategoriasCrear.isEmpty()) {
            administrarDiagnosticosCategorias.crearDiagnosticoCategoria(listaDiagnosticosCategoriasCrear);
            listaDiagnosticosCategoriasCrear.clear();
         }
         listaDiagnosticosCategorias = null;
         getListaDiagnosticosCategorias();
         RequestContext.getCurrentInstance().update("form:datosCategorias");
         contarRegistroCategorias();
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      diagnosticoCategoriaSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void guardarCapitulo() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado == false) {
         if (!listaDiagnosticosCapitulosBorrar.isEmpty()) {
            administrarDiagnosticosCategorias.borrarDiagnosticoCapitulo(listaDiagnosticosCapitulosBorrar);
            registrosBorrados = listaDiagnosticosCapitulosBorrar.size();
            RequestContext.getCurrentInstance().update("formularioDialogos:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            listaDiagnosticosCapitulosBorrar.clear();
         }
         if (!listaDiagnosticosCapitulosModificar.isEmpty()) {
            administrarDiagnosticosCategorias.editarDiagnosticoCapitulo(listaDiagnosticosCapitulosModificar);
            listaDiagnosticosCapitulosModificar.clear();
         }
         if (!listaDiagnosticosCapitulosCrear.isEmpty()) {
            administrarDiagnosticosCategorias.crearDiagnosticoCapitulo(listaDiagnosticosCapitulosCrear);
            listaDiagnosticosCapitulosCrear.clear();
         }
         listaDiagnosticosCapitulos = null;
         getListaDiagnosticosCapitulos();
         contarRegistrosCapitulos();
         RequestContext.getCurrentInstance().update("form:datosCapitulos");
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      diagnosticoCapituloSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void guardarSeccion() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado == false) {
         if (!listaDiagnosticosSeccionesBorrar.isEmpty()) {
            administrarDiagnosticosCategorias.borrarDiagnosticoSeccion(listaDiagnosticosSeccionesBorrar);
            registrosBorrados = listaDiagnosticosSeccionesBorrar.size();
            RequestContext.getCurrentInstance().update("formularioDialogos:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            listaDiagnosticosSeccionesBorrar.clear();
         }
         if (!listaDiagnosticosSeccionesModificar.isEmpty()) {
            administrarDiagnosticosCategorias.editarDiagnosticoSeccion(listaDiagnosticosSeccionesModificar);
            listaDiagnosticosSeccionesModificar.clear();
         }
         if (!listaDiagnosticosSeccionesCrear.isEmpty()) {
            administrarDiagnosticosCategorias.crearDiagnosticoSeccion(listaDiagnosticosSeccionesCrear);
            listaDiagnosticosSeccionesCrear.clear();
         }
         listaDiagnosticosSecciones = null;
         getListaDiagnosticosSecciones();
         RequestContext.getCurrentInstance().update("form:datosSecciones");
         contarRegistrosSecciones();
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      diagnosticoSeccionSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      if (cualTabla == 1) {
         if (diagnosticoCapituloSeleccionado != null) {
            editarDiagnosticoCapitulo = diagnosticoCapituloSeleccionado;

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaCapitulo == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editCodigoCapitulo");
               RequestContext.getCurrentInstance().execute("PF('editCodigoCapitulo').show()");
               cualCeldaCapitulo = -1;
            } else if (cualCeldaCapitulo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcionCapitulo");
               RequestContext.getCurrentInstance().execute("PF('editDescripcionCapitulo').show()");
               cualCeldaCapitulo = -1;
            }
         }
      } else if (cualTabla == 2) {
         if (diagnosticoSeccionSeleccionado != null) {
            editarDiagnosticoSeccion = diagnosticoSeccionSeleccionado;

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaSeccion == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editCodigoSeccion");
               RequestContext.getCurrentInstance().execute("PF('editCodigoSeccion').show()");
               cualCeldaSeccion = -1;
            } else if (cualCeldaSeccion == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcionSeccion");
               RequestContext.getCurrentInstance().execute("PF('editDescripcionSeccion').show()");
               cualCeldaSeccion = -1;
            }
         }
      } else if (cualTabla == 3) {
         editarDiagnosticoCategoria = diagnosticoCategoriaSeleccionado;

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
            RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }

   }

   public void agregarNuevoDiagnosticosCategorias() {
      int contador = 0;
      int duplicados = 0;

      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoDiagnosticoCategoria.getCodigo() == null) {
         mensajeValidacion = "*Código \n";
      } else {
         for (int i = 0; i < listaDiagnosticosCategorias.size(); i++) {
            if (listaDiagnosticosCategorias.get(i).getCodigo().equals(nuevoDiagnosticoCategoria.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "No pueden haber códigos repetidos \n";
         } else {
            contador++;
         }
      }

      if (nuevoDiagnosticoCategoria.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else if (nuevoDiagnosticoCategoria.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else {
         contador++;
      }
      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosCategorias:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosCategorias:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCategorias");
            bandera = 0;
            listaFiltrarDiagnosticosCategorias = null;
            tipoLista = 0;
         }

         k++;
         l = BigInteger.valueOf(k);
         nuevoDiagnosticoCategoria.setSecuencia(new BigDecimal(l));
         listaDiagnosticosCategoriasCrear.add(nuevoDiagnosticoCategoria);
         listaDiagnosticosCategorias.add(nuevoDiagnosticoCategoria);
         diagnosticoCategoriaSeleccionado = nuevoDiagnosticoCategoria;
         nuevoDiagnosticoCategoria = new Diagnosticoscategorias();
         RequestContext.getCurrentInstance().update("form:datosCategorias");
         contarRegistroCategorias();

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroDiagnosticoCategorias').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoDiagnosticoCategorias");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoDiagnosticoCategorias').show()");
         contador = 0;
      }
   }

   public void agregarNuevoDiagnosticosCapitulos() {
      int contador = 0;
      int duplicados = 0;

      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoDiagnosticoCapitulo.getCodigo() == null) {
         mensajeValidacion = "*Código \n";
      } else {
         for (int i = 0; i < listaDiagnosticosCapitulos.size(); i++) {
            if (listaDiagnosticosCapitulos.get(i).getCodigo().equals(nuevoDiagnosticoCapitulo.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "No pueden haber códigos repetidos \n";
         } else {
            contador++;
         }
      }

      if (nuevoDiagnosticoCapitulo.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else if (nuevoDiagnosticoCapitulo.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else {
         contador++;
      }
      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:codigoCapitulo");
            codCapitulo.setFilterStyle("display: none; visibility: hidden;");
            descCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:descripcionCapitulo");
            descCapitulo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCapitulos");
            bandera = 0;
            listaFiltrarDiagnosticosCapitulos = null;
            tipoLista = 0;
         }

         k++;
         l = BigInteger.valueOf(k);
         nuevoDiagnosticoCapitulo.setSecuencia(new BigDecimal(l));
         listaDiagnosticosCapitulosCrear.add(nuevoDiagnosticoCapitulo);
         listaDiagnosticosCapitulos.add(nuevoDiagnosticoCapitulo);
         diagnosticoCapituloSeleccionado = nuevoDiagnosticoCapitulo;
         nuevoDiagnosticoCapitulo = new Diagnosticoscapitulos();
         RequestContext.getCurrentInstance().update("form:datosCapitulos");
         contarRegistrosCapitulos();

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroDiagnosticoCapitulos').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoDiagnosticoCapitulos");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoDiagnosticoCapitulos').show()");
         contador = 0;
      }
   }

   public void agregarNuevoDiagnosticosSecciones() {
      int contador = 0;
      int duplicados = 0;

      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoDiagnosticoSeccion.getCodigo() == null) {
         mensajeValidacion = "*Código \n";
      } else {
         for (int i = 0; i < listaDiagnosticosSecciones.size(); i++) {
            if (listaDiagnosticosSecciones.get(i).getCodigo().equals(nuevoDiagnosticoSeccion.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "No pueden haber códigos repetidos \n";
         } else {
            contador++;
         }
      }

      if (nuevoDiagnosticoSeccion.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else if (nuevoDiagnosticoSeccion.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else {
         contador++;
      }
      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:codigoSeccion");
            codSeccion.setFilterStyle("display: none; visibility: hidden;");
            descSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:descripcionSeccion");
            descSeccion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSecciones");
            bandera = 0;
            listaFiltrarDiagnosticosSecciones = null;
            tipoLista = 0;
         }

         k++;
         l = BigInteger.valueOf(k);
         nuevoDiagnosticoSeccion.setSecuencia(new BigDecimal(l));
         listaDiagnosticosSeccionesCrear.add(nuevoDiagnosticoSeccion);
         listaDiagnosticosSecciones.add(nuevoDiagnosticoSeccion);
         diagnosticoSeccionSeleccionado = nuevoDiagnosticoSeccion;
         nuevoDiagnosticoSeccion = new Diagnosticossecciones();
         RequestContext.getCurrentInstance().update("form:datosSecciones");
         contarRegistrosCapitulos();

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroDiagnosticoSeccion').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoDiagnosticoSecciones");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoDiagnosticoSecciones').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoDiagnosticoCategoria() {
      nuevoDiagnosticoCategoria = new Diagnosticoscategorias();
   }

   public void limpiarNuevoDiagnosticoCapitulo() {
      nuevoDiagnosticoCapitulo = new Diagnosticoscapitulos();
   }

   public void limpiarNuevoDiagnosticoSeccion() {
      nuevoDiagnosticoSeccion = new Diagnosticossecciones();
   }

   public void limpiarDuplicarDiagnosticoCategoria() {
      duplicarDiagnosticoCategoria = new Diagnosticoscategorias();
   }

   public void limpiarDuplicarDiagnosticoCapitulo() {
      duplicarDiagnosticoCapitulo = new Diagnosticoscapitulos();
   }

   public void limpiarDuplicarDiagnosticoSeccion() {
      duplicarDiagnosticoSeccion = new Diagnosticossecciones();
   }

   public void limpiarExportar() {
      if (cualTabla == 1) {
         limpiarNuevoDiagnosticoCapitulo();
      } else if (cualTabla == 2) {
         limpiarDuplicarDiagnosticoSeccion();
      } else if (cualTabla == 3) {
         limpiarDuplicarDiagnosticoCategoria();
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void duplicarDiagnosticoCategoria() {
      if (diagnosticoCategoriaSeleccionado != null) {
         duplicarDiagnosticoCategoria = new Diagnosticoscategorias();
         k++;
         l = BigInteger.valueOf(k);

         duplicarDiagnosticoCategoria.setSecuencia(new BigDecimal(l));
         duplicarDiagnosticoCategoria.setCodigo(diagnosticoCategoriaSeleccionado.getCodigo());
         duplicarDiagnosticoCategoria.setDescripcion(diagnosticoCategoriaSeleccionado.getDescripcion());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarC");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroDiagnosticoCategoria').show()");
      } else if (diagnosticoCapituloSeleccionado != null) {
         duplicarDiagnosticoCapitulo = new Diagnosticoscapitulos();
         k++;
         l = BigInteger.valueOf(k);

         duplicarDiagnosticoCapitulo.setSecuencia(new BigDecimal(l));
         duplicarDiagnosticoCapitulo.setCodigo(diagnosticoCapituloSeleccionado.getCodigo());
         duplicarDiagnosticoCapitulo.setDescripcion(diagnosticoCapituloSeleccionado.getDescripcion());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCap");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroDiagnosticoCapitulo').show()");
      } else if (diagnosticoSeccionSeleccionado != null) {
         duplicarDiagnosticoSeccion = new Diagnosticossecciones();
         k++;
         l = BigInteger.valueOf(k);

         duplicarDiagnosticoSeccion.setSecuencia(new BigDecimal(l));
         duplicarDiagnosticoSeccion.setCodigo(diagnosticoSeccionSeleccionado.getCodigo());
         duplicarDiagnosticoSeccion.setDescripcion(diagnosticoSeccionSeleccionado.getDescripcion());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarS");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroDiagnosticoSeccion').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = "";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (duplicarDiagnosticoCategoria.getCodigo() == null) {
         mensajeValidacion = "*Código \n";
      } else {
         for (int i = 0; i < listaDiagnosticosCategorias.size(); i++) {
            if (listaDiagnosticosCategorias.get(i).getCodigo().equals(duplicarDiagnosticoCategoria.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "No pueden haber códigos repetidos \n";
         } else {
            contador++;
         }
      }
      if (duplicarDiagnosticoCategoria.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else if (duplicarDiagnosticoCategoria.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else {
         contador++;
      }

      if (contador == 2) {
         listaDiagnosticosCategorias.add(duplicarDiagnosticoCategoria);
         listaDiagnosticosCategoriasCrear.add(duplicarDiagnosticoCategoria);
         diagnosticoCategoriaSeleccionado = duplicarDiagnosticoCategoria;
         RequestContext.getCurrentInstance().update("form:datosCategorias");
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         contarRegistroCategorias();

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosCategorias:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosCategorias:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCategorias");
            bandera = 0;
            listaFiltrarDiagnosticosCategorias = null;
            tipoLista = 0;
         }
         duplicarDiagnosticoCategoria = new Diagnosticoscategorias();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroDiagnosticoCategoria').hide()");
      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionDuplicarDiagnosticoCategoria");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarDiagnosticoCategoria').show()");
      }

   }

   public void confirmarDuplicarCapitulo() {
      int contador = 0;
      mensajeValidacion = "";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (duplicarDiagnosticoCapitulo.getCodigo() == null) {
         mensajeValidacion = "*Código \n";
      } else {
         for (int i = 0; i < listaDiagnosticosCapitulos.size(); i++) {
            if (listaDiagnosticosCapitulos.get(i).getCodigo().equals(duplicarDiagnosticoCapitulo.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "No pueden haber códigos repetidos \n";
         } else {
            contador++;
         }
      }
      if (duplicarDiagnosticoCapitulo.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else if (duplicarDiagnosticoCapitulo.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else {
         contador++;
      }

      if (contador == 2) {
         listaDiagnosticosCapitulos.add(duplicarDiagnosticoCapitulo);
         listaDiagnosticosCapitulosCrear.add(duplicarDiagnosticoCapitulo);
         diagnosticoCapituloSeleccionado = duplicarDiagnosticoCapitulo;
         RequestContext.getCurrentInstance().update("form:datosCapitulos");
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         contarRegistrosCapitulos();

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:codigoCapitulo");
            codCapitulo.setFilterStyle("display: none; visibility: hidden;");
            descCapitulo = (Column) c.getViewRoot().findComponent("form:datosCapitulos:descripcionCapitulo");
            descCapitulo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCapitulos");
            bandera = 0;
            listaFiltrarDiagnosticosCapitulos = null;
            tipoLista = 0;
         }
         duplicarDiagnosticoCapitulo = new Diagnosticoscapitulos();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroDiagnosticoCapitulo').hide()");
      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionDuplicarDiagnosticoCategoria");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarDiagnosticoCategoria').show()");
      }
   }

   public void confirmarDuplicarSeccion() {
      int contador = 0;
      mensajeValidacion = "";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (duplicarDiagnosticoSeccion.getCodigo() == null) {
         mensajeValidacion = "*Código \n";
      } else {
         for (int i = 0; i < listaDiagnosticosSecciones.size(); i++) {
            if (listaDiagnosticosSecciones.get(i).getCodigo().equals(duplicarDiagnosticoSeccion.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "No pueden haber códigos repetidos \n";
         } else {
            contador++;
         }
      }
      if (duplicarDiagnosticoSeccion.getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else if (duplicarDiagnosticoSeccion.getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "*Descripción \n";
      } else {
         contador++;
      }

      if (contador == 2) {
         listaDiagnosticosSecciones.add(duplicarDiagnosticoSeccion);
         listaDiagnosticosSeccionesCrear.add(duplicarDiagnosticoSeccion);
         diagnosticoSeccionSeleccionado = duplicarDiagnosticoSeccion;
         RequestContext.getCurrentInstance().update("form:datosSecciones");
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         contarRegistrosSecciones();

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:codigoSeccion");
            codSeccion.setFilterStyle("display: none; visibility: hidden;");
            descSeccion = (Column) c.getViewRoot().findComponent("form:datosSecciones:descripcionSeccion");
            descSeccion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSecciones");
            bandera = 0;
            listaFiltrarDiagnosticosSecciones = null;
            tipoLista = 0;
         }
         duplicarDiagnosticoSeccion = new Diagnosticossecciones();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroDiagnosticoSeccion').hide()");
      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionDuplicarDiagnosticoCategoria");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarDiagnosticoCategoria').show()");
      }
   }

   public void exportPDF() throws IOException {
      if (cualTabla == 1) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCapitulos:datosDiagnosticosCapitulosExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarPDF();
         exporter.export(context, tabla, "DIAGNOSTICOSCAPITULOS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (cualTabla == 2) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarSecciones:datosDiagnosticosSeccionesExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarPDF();
         exporter.export(context, tabla, "DIAGNOSTICOSSECCIONES", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (cualTabla == 3) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCategorias:datosDiagnosticosCategoriasExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarPDF();
         exporter.export(context, tabla, "DIAGNOSTICOSCATEGORIAS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }

   }

   public void exportXLS() throws IOException {
      if (cualTabla == 1) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCapitulos:datosDiagnosticosCapitulosExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "DIAGNOSTICOSCAPITULOS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (cualTabla == 2) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarSecciones:datosDiagnosticosSeccionesExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "DIAGNOSTICOSSECCIONES", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (cualTabla == 3) {
         DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCategorias:datosDiagnosticosCategoriasExportar");
         FacesContext context = FacesContext.getCurrentInstance();
         Exporter exporter = new ExportarXLS();
         exporter.export(context, tabla, "DIAGNOSTICOSCATEGORIAS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (diagnosticoCategoriaSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(diagnosticoCategoriaSeleccionado.getSecuencia().toBigInteger(), "DIAGNOSTICOSCATEGORIAS");
         System.out.println("resultado: " + resultado);
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
      } else if (administrarRastros.verificarHistoricosTabla("DIAGNOSTICOSCATEGORIAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void guardarSalir() {
      guardarCapitulo();
      guardarCategoria();
      guardarSeccion();
      salir();
   }

   public void cancelarSalir() {
      cancelarModificacion();
      salir();
   }

   public void guardarTodo() {
      guardarCapitulo();
      guardarCategoria();
      guardarSeccion();
   }

   public void contarRegistroCategorias() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCategoria");
   }

   public void contarRegistrosCapitulos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroCapitulo");
   }

   public void contarRegistrosSecciones() {
      RequestContext.getCurrentInstance().update("form:infoRegistroSeccion");
   }

   public void mostrarDialogoInsertarCapitulo() {
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevoRegistroDiagnosticoCapitulos");
      RequestContext.getCurrentInstance().execute("PF('formularioDialogos:nuevoRegistroDiagnosticoCapitulos').show()");
   }

   public void mostrarDialogoInsertarCategoria() {
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevoRegistroDiagnosticoCategoria");
      RequestContext.getCurrentInstance().execute("PF('formularioDialogos:nuevoRegistroDiagnosticoCategoria').show()");
   }

   public void mostrarDialogoInsertarSeccion() {
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevoRegistroDiagnosticoSeccion");
      RequestContext.getCurrentInstance().execute("PF('formularioDialogos:nuevoRegistroDiagnosticoSeccion').show()");
   }

   public void mostrarDialogoElegirTabla() {
      RequestContext.getCurrentInstance().update("formularioDialogos:seleccionarTablaNewReg");
      RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarTablaNewReg').show()");
   }

   //////////////SETS Y GETS//////////////////////////////
   public List<Diagnosticoscategorias> getListaDiagnosticosCategorias() {
      if (listaDiagnosticosCategorias == null || listaDiagnosticosCategorias.isEmpty()) {
         if (diagnosticoSeccionSeleccionado != null) {
            listaDiagnosticosCategorias = administrarDiagnosticosCategorias.consultarDiagnosticoCategoria(diagnosticoSeccionSeleccionado.getSecuencia());
         } else {
            listaDiagnosticosCategorias = new ArrayList<Diagnosticoscategorias>();
         }
      }
      return listaDiagnosticosCategorias;
   }

   public void setListaDiagnosticosCategorias(List<Diagnosticoscategorias> listaDiagnosticosCategorias) {
      this.listaDiagnosticosCategorias = listaDiagnosticosCategorias;
   }

   public List<Diagnosticoscategorias> getListaFiltrarDiagnosticosCategorias() {
      return listaFiltrarDiagnosticosCategorias;
   }

   public void setListaFiltrarDiagnosticosCategorias(List<Diagnosticoscategorias> listaFiltrarDiagnosticosCategorias) {
      this.listaFiltrarDiagnosticosCategorias = listaFiltrarDiagnosticosCategorias;
   }

   public Diagnosticoscategorias getNuevoDiagnosticoCategoria() {
      return nuevoDiagnosticoCategoria;
   }

   public void setNuevoDiagnosticoCategoria(Diagnosticoscategorias nuevoDiagnosticoCategoria) {
      this.nuevoDiagnosticoCategoria = nuevoDiagnosticoCategoria;
   }

   public Diagnosticoscategorias getDuplicarDiagnosticoCategoria() {
      return duplicarDiagnosticoCategoria;
   }

   public void setDuplicarDiagnosticoCategoria(Diagnosticoscategorias duplicarDiagnosticoCategoria) {
      this.duplicarDiagnosticoCategoria = duplicarDiagnosticoCategoria;
   }

   public Diagnosticoscategorias getDiagnosticoCategoriaSeleccionado() {
      return diagnosticoCategoriaSeleccionado;
   }

   public void setDiagnosticoCategoriaSeleccionado(Diagnosticoscategorias diagnosticoCategoriaSeleccionado) {
      this.diagnosticoCategoriaSeleccionado = diagnosticoCategoriaSeleccionado;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistroCategorias() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCategorias");
      infoRegistroCategorias = String.valueOf(tabla.getRowCount());
      return infoRegistroCategorias;
   }

   public void setInfoRegistroCategorias(String infoRegistroCategorias) {
      this.infoRegistroCategorias = infoRegistroCategorias;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public Boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(Boolean activarLov) {
      this.activarLov = activarLov;
   }

   public Boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(Boolean aceptar) {
      this.aceptar = aceptar;
   }

   public Boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(Boolean guardado) {
      this.guardado = guardado;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

   public List<Diagnosticoscapitulos> getListaDiagnosticosCapitulos() {
      if (listaDiagnosticosCapitulos == null) {
         listaDiagnosticosCapitulos = administrarDiagnosticosCategorias.consultarDiagnosticoCapitulo();
      }
      return listaDiagnosticosCapitulos;
   }

   public void setListaDiagnosticosCapitulos(List<Diagnosticoscapitulos> listaDiagnosticosCapitulos) {
      this.listaDiagnosticosCapitulos = listaDiagnosticosCapitulos;
   }

   public List<Diagnosticoscapitulos> getListaFiltrarDiagnosticosCapitulos() {
      return listaFiltrarDiagnosticosCapitulos;
   }

   public void setListaFiltrarDiagnosticosCapitulos(List<Diagnosticoscapitulos> listaFiltrarDiagnosticosCapitulos) {
      this.listaFiltrarDiagnosticosCapitulos = listaFiltrarDiagnosticosCapitulos;
   }

   public Diagnosticoscapitulos getNuevoDiagnosticoCapitulo() {
      return nuevoDiagnosticoCapitulo;
   }

   public void setNuevoDiagnosticoCapitulo(Diagnosticoscapitulos nuevoDiagnosticoCapitulo) {
      this.nuevoDiagnosticoCapitulo = nuevoDiagnosticoCapitulo;
   }

   public Diagnosticoscapitulos getDuplicarDiagnosticoCapitulo() {
      return duplicarDiagnosticoCapitulo;
   }

   public void setDuplicarDiagnosticoCapitulo(Diagnosticoscapitulos duplicarDiagnosticoCapitulo) {
      this.duplicarDiagnosticoCapitulo = duplicarDiagnosticoCapitulo;
   }

   public Diagnosticoscapitulos getDiagnosticoCapituloSeleccionado() {
      return diagnosticoCapituloSeleccionado;
   }

   public void setDiagnosticoCapituloSeleccionado(Diagnosticoscapitulos diagnosticoCapituloSeleccionado) {
      this.diagnosticoCapituloSeleccionado = diagnosticoCapituloSeleccionado;
   }

   public String getInfoRegistroCapitulos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCapitulos");
      infoRegistroCapitulos = String.valueOf(tabla.getRowCount());
      return infoRegistroCapitulos;
   }

   public void setInfoRegistroCapitulos(String infoRegistroCapitulos) {
      this.infoRegistroCapitulos = infoRegistroCapitulos;
   }

   public String getInfoRegistroSecciones() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSecciones");
      infoRegistroSecciones = String.valueOf(tabla.getRowCount());
      return infoRegistroSecciones;
   }

   public void setInfoRegistroSecciones(String infoRegistroSecciones) {
      this.infoRegistroSecciones = infoRegistroSecciones;
   }

   public List<Diagnosticossecciones> getListaDiagnosticosSecciones() {
      if (listaDiagnosticosSecciones == null || listaDiagnosticosSecciones.isEmpty()) {
         if (diagnosticoCapituloSeleccionado != null) {
            listaDiagnosticosSecciones = administrarDiagnosticosCategorias.consultarDiagnosticoSeccion(diagnosticoCapituloSeleccionado.getSecuencia());
         } else {
            listaDiagnosticosSecciones = new ArrayList<Diagnosticossecciones>();
         }
      }
      return listaDiagnosticosSecciones;

   }

   public void setListaDiagnosticosSecciones(List<Diagnosticossecciones> listaDiagnosticosSecciones) {
      this.listaDiagnosticosSecciones = listaDiagnosticosSecciones;
   }

   public List<Diagnosticossecciones> getListaFiltrarDiagnosticosSecciones() {
      return listaFiltrarDiagnosticosSecciones;
   }

   public void setListaFiltrarDiagnosticosSecciones(List<Diagnosticossecciones> listaFiltrarDiagnosticosSecciones) {
      this.listaFiltrarDiagnosticosSecciones = listaFiltrarDiagnosticosSecciones;
   }

   public Diagnosticossecciones getNuevoDiagnosticoSeccion() {
      return nuevoDiagnosticoSeccion;
   }

   public void setNuevoDiagnosticoSeccion(Diagnosticossecciones nuevoDiagnosticoSeccion) {
      this.nuevoDiagnosticoSeccion = nuevoDiagnosticoSeccion;
   }

   public Diagnosticossecciones getDuplicarDiagnosticoSeccion() {
      return duplicarDiagnosticoSeccion;
   }

   public void setDuplicarDiagnosticoSeccion(Diagnosticossecciones duplicarDiagnosticoSeccion) {
      this.duplicarDiagnosticoSeccion = duplicarDiagnosticoSeccion;
   }

   public Diagnosticossecciones getDiagnosticoSeccionSeleccionado() {
      return diagnosticoSeccionSeleccionado;
   }

   public void setDiagnosticoSeccionSeleccionado(Diagnosticossecciones diagnosticoSeccionSeleccionado) {
      this.diagnosticoSeccionSeleccionado = diagnosticoSeccionSeleccionado;
   }

   public Diagnosticoscategorias getEditarDiagnosticoCategoria() {
      return editarDiagnosticoCategoria;
   }

   public void setEditarDiagnosticoCategoria(Diagnosticoscategorias editarDiagnosticoCategoria) {
      this.editarDiagnosticoCategoria = editarDiagnosticoCategoria;
   }

   public Diagnosticoscapitulos getEditarDiagnosticoCapitulo() {
      return editarDiagnosticoCapitulo;
   }

   public void setEditarDiagnosticoCapitulo(Diagnosticoscapitulos editarDiagnosticoCapitulo) {
      this.editarDiagnosticoCapitulo = editarDiagnosticoCapitulo;
   }

   public Diagnosticossecciones getEditarDiagnosticoSeccion() {
      return editarDiagnosticoSeccion;
   }

   public void setEditarDiagnosticoSeccion(Diagnosticossecciones editarDiagnosticoSeccion) {
      this.editarDiagnosticoSeccion = editarDiagnosticoSeccion;
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

}
