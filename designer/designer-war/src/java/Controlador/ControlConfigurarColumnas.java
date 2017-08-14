package Controlador;

import Entidades.ColumnasEscenarios;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarConfigurarColumnasInterface;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlConfigurarColumnas implements Serializable {

   private static Logger log = Logger.getLogger(ControlConfigurarColumnas.class);

   @EJB
   AdministrarConfigurarColumnasInterface administrarConfigurarColumnas;

   //Columnas Escenarios
   private List<ColumnasEscenarios> listaColumnasEscenarios;
   private List<ColumnasEscenarios> filtrarListaColumnasEscenarios;
   private List<ColumnasEscenarios> listaRespaldoColumnasEscenarios;
   private ColumnasEscenarios columnaSeleccionada;
   //LOV
   private List<ColumnasEscenarios> lovColumnasEscenarios;
   private ColumnasEscenarios columnaEscenarioSeleccionada;
   private List<ColumnasEscenarios> filtrarLovColumnasEscenarios;
   //
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas 
   private Column vrlFecha, vrlNombre;
   //Otros
   private boolean aceptar;
   private int index;
   private int tipoActualizacion;
   //modificar
   private boolean guardado;
   //editar celda
   private ColumnasEscenarios editarColumna;
   private int cualCelda, tipoLista;
   //
   private String nombreColumna;
   private boolean permitirIndex;
   //
   private String altoTabla;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlConfigurarColumnas() {
      altoTabla = "275";
      tipoActualizacion = 0;
      listaColumnasEscenarios = new ArrayList<ColumnasEscenarios>();
      listaRespaldoColumnasEscenarios = new ArrayList<ColumnasEscenarios>();
      lovColumnasEscenarios = new ArrayList<ColumnasEscenarios>();
      //Otros
      aceptar = true;
      //editar
      editarColumna = new ColumnasEscenarios();
      cualCelda = -1;
      tipoLista = 0;
      //guardar
      guardado = true;
      permitirIndex = true;
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
      String pagActual = "configurarcolumnas";
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
      lovColumnasEscenarios = null;
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
         administrarConfigurarColumnas.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirListaColumnasEscenarios(List<ColumnasEscenarios> listaBusquedaAvanzada) {
      if (listaBusquedaAvanzada != null) {
         log.info("listaColumnasEscenarios : " + listaColumnasEscenarios.size());
         listaColumnasEscenarios = listaBusquedaAvanzada;
         listaRespaldoColumnasEscenarios = listaBusquedaAvanzada;
         eliminarColumnasCargadas();
      } else {
         log.info("listaColumnasEscenarios : 0");
         listaColumnasEscenarios = new ArrayList<ColumnasEscenarios>();
      }
   }

   public void modificarColumnaEscenario(int indice, String confirmarCambio, String valorConfirmar) {
      index = indice;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("NOMBRECOLUMNA")) {
         if (tipoLista == 0) {
            listaColumnasEscenarios.get(indice).setNombrecolumna(nombreColumna);
         } else {
            filtrarListaColumnasEscenarios.get(indice).setNombrecolumna(nombreColumna);
         }
         for (int i = 0; i < lovColumnasEscenarios.size(); i++) {
            if (lovColumnasEscenarios.get(i).getNombrecolumna().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoLista == 0) {
               listaColumnasEscenarios.set(indice, lovColumnasEscenarios.get(indiceUnicoElemento));
            } else {
               filtrarListaColumnasEscenarios.set(indice, lovColumnasEscenarios.get(indiceUnicoElemento));
            }
            lovColumnasEscenarios.clear();
            getLovColumnasEscenarios();
            eliminarColumnasCargadas();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("form:ColumnaEscenarioDialogo");
            RequestContext.getCurrentInstance().execute("PF('ColumnaEscenarioDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      RequestContext.getCurrentInstance().update("form:datosConfigurarColumna");
   }

   public void posicionColumnaEscenario() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      cambiarIndice(indice, columna);
   }

   public void cambiarIndice(int indice, int celda) {
      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         if (tipoLista == 0) {
            nombreColumna = listaColumnasEscenarios.get(index).getNombrecolumna();
         }
         if (tipoLista == 1) {
            nombreColumna = filtrarListaColumnasEscenarios.get(index).getNombrecolumna();
         }
      }
   }

   public void eliminarColumnasCargadas() {
      lovColumnasEscenarios.clear();
      getLovColumnasEscenarios();
      if (tipoLista == 0) {
         for (int i = 0; i < listaColumnasEscenarios.size(); i++) {
            int indice = lovColumnasEscenarios.indexOf(listaColumnasEscenarios.get(i));
            lovColumnasEscenarios.remove(indice);
         }
      }
      if (tipoLista == 1) {
         for (int i = 0; i < filtrarListaColumnasEscenarios.size(); i++) {
            int indice = lovColumnasEscenarios.indexOf(filtrarListaColumnasEscenarios.get(i));
            lovColumnasEscenarios.remove(indice);
         }
      }
   }

   //CANCELAR MODIFICACIONES
   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacion() {
      if (bandera == 1) {
         altoTabla = "275";
         //CERRAR FILTRADO
         vrlFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConfigurarColumna:vrlFecha");
         vrlFecha.setFilterStyle("display: none; visibility: hidden;");
         vrlNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConfigurarColumna:vrlNombre");
         vrlNombre.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosConfigurarColumna");
         bandera = 0;
         filtrarListaColumnasEscenarios = null;
         tipoLista = 0;
      }
      tipoActualizacion = -1;
      index = -1;
      if (listaRespaldoColumnasEscenarios == null) {
         listaRespaldoColumnasEscenarios = new ArrayList<ColumnasEscenarios>();
      }
      listaColumnasEscenarios = listaRespaldoColumnasEscenarios;
      guardado = true;
      lovColumnasEscenarios.clear();
      getLovColumnasEscenarios();
      getColumnaSeleccionada();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosConfigurarColumna");
   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarColumna = listaColumnasEscenarios.get(index);
         }
         if (tipoLista == 1) {
            editarColumna = filtrarListaColumnasEscenarios.get(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCampoColumna");
            RequestContext.getCurrentInstance().execute("PF('editarCampoColumna').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionColumna");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionColumna').show()");
            cualCelda = -1;
         }
      }
      index = -1;
   }

   //BORRAR VC
   /**
    * Metodo que borra las vigencias seleccionadas
    */
   public void borrarColumnaEscenario() {
      if (index >= 0) {
         listaColumnasEscenarios.remove(index);
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosConfigurarColumna");
         index = -1;
         eliminarColumnasCargadas();
      }
   }
   //CTRL + F11 ACTIVAR/DESACTIVAR

   /**
    * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      if (bandera == 0) {
         altoTabla = "255";
         vrlFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConfigurarColumna:vrlFecha");
         vrlFecha.setFilterStyle("width: 85% !important");
         vrlNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConfigurarColumna:vrlNombre");
         vrlNombre.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosConfigurarColumna");
         bandera = 1;
      } else if (bandera == 1) {
         altoTabla = "275";
         vrlFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConfigurarColumna:vrlFecha");
         vrlFecha.setFilterStyle("display: none; visibility: hidden;");
         vrlNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConfigurarColumna:vrlNombre");
         vrlNombre.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosConfigurarColumna");
         bandera = 0;
         filtrarListaColumnasEscenarios = null;
         tipoLista = 0;
      }
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         altoTabla = "275";
         vrlFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConfigurarColumna:vrlFecha");
         vrlFecha.setFilterStyle("display: none; visibility: hidden;");
         vrlNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConfigurarColumna:vrlNombre");
         vrlNombre.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosConfigurarColumna");
         bandera = 0;
         filtrarListaColumnasEscenarios = null;
         tipoLista = 0;
      }
      tipoActualizacion = -1;
      index = -1;
      listaRespaldoColumnasEscenarios = null;
      guardado = true;
   }

   public void dispararDialogoNuevaColumna() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaColumnasEscenarios.size() < 10) {
         tipoActualizacion = 1;
         RequestContext.getCurrentInstance().update("form:ColumnaEscenarioDialogo");
         RequestContext.getCurrentInstance().execute("PF('ColumnaEscenarioDialogo').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorNewColumna').show()");
      }
   }

   public void dispararDialogoActualizarColumna() {
      tipoActualizacion = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ColumnaEscenarioDialogo");
      RequestContext.getCurrentInstance().execute("PF('ColumnaEscenarioDialogo').show()");
   }

   public void agregarColumnaEscenario() {
      if (bandera == 1) {
         altoTabla = "275";
         //CERRAR FILTRADO
         vrlFecha = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConfigurarColumna:vrlFecha");
         vrlFecha.setFilterStyle("display: none; visibility: hidden;");
         vrlNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConfigurarColumna:vrlNombre");
         vrlNombre.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosConfigurarColumna");
         bandera = 0;
         filtrarListaColumnasEscenarios = null;
         tipoLista = 0;
      }
      if (tipoActualizacion == 0) {
         listaColumnasEscenarios.set(index, columnaEscenarioSeleccionada);
      }
      if (tipoActualizacion == 1) {
         listaColumnasEscenarios.add(columnaEscenarioSeleccionada);
      }
      if (guardado == true) {
         guardado = false;
         //RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      permitirIndex = true;
      filtrarLovColumnasEscenarios = null;
      columnaEscenarioSeleccionada = null;
      aceptar = true;
      index = -1;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosConfigurarColumna");
      /*
         RequestContext.getCurrentInstance().update("form:ColumnaEscenarioDialogo");
         RequestContext.getCurrentInstance().update("form:lovColumnaEscenario");
         RequestContext.getCurrentInstance().update("form:aceptarCE");*/
      context.reset("form:lovColumnaEscenario:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovColumnaEscenario').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ColumnaEscenarioDialogo').hide()");
      //eliminarColumnasCargadas();
   }

   /**
    * Metodo que cancela los cambios sobre reforma laboral
    */
   public void cancelarCambioColumnaEscenario() {
      filtrarLovColumnasEscenarios = null;
      columnaEscenarioSeleccionada = null;
      aceptar = true;
      index = -1;
      permitirIndex = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovColumnaEscenario:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovColumnaEscenario').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ColumnaEscenarioDialogo').hide()");
   }

   //LISTA DE VALORES DINAMICA
   /**
    * Metodo que activa la lista de valores de la tabla con respecto a las
    * reformas laborales
    */
   public void listaValoresBoton() {
      if (index >= 0) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:ColumnaEscenarioDialogo");
         RequestContext.getCurrentInstance().execute("PF('ColumnaEscenarioDialogo').show()");
         tipoActualizacion = 0;
      }
   }

   /**
    * Metodo que activa el boton aceptar de la pagina y los dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   /**
    * Metodo que exporta datos a PDF
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConfigurarColumnaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ColumnasEscenarios_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConfigurarColumnaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ColumnasEscenarios_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
   }
   //EVENTO FILTRAR

   /**
    * Evento que cambia la lista reala a la filtrada
    */
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
   }

   //GETTERS AND SETTERS
   public List<ColumnasEscenarios> getListaColumnasEscenarios() {
      return listaColumnasEscenarios;
   }

   public void setListaColumnasEscenarios(List<ColumnasEscenarios> setListaColumnasEscenarios) {
      this.listaColumnasEscenarios = setListaColumnasEscenarios;
   }

   public List<ColumnasEscenarios> getFiltrarListaColumnasEscenarios() {
      return filtrarListaColumnasEscenarios;
   }

   public void setFiltrarListaColumnasEscenarios(List<ColumnasEscenarios> setFiltrarListaColumnasEscenarios) {
      this.filtrarListaColumnasEscenarios = setFiltrarListaColumnasEscenarios;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public List<ColumnasEscenarios> getLovColumnasEscenarios() {
      lovColumnasEscenarios = administrarConfigurarColumnas.listaColumnasEscenarios();
      return lovColumnasEscenarios;
   }

   public void setLovColumnasEscenarios(List<ColumnasEscenarios> setLovColumnasEscenarios) {
      this.lovColumnasEscenarios = setLovColumnasEscenarios;
   }

   public List<ColumnasEscenarios> getFiltrarLovColumnasEscenarios() {
      return filtrarLovColumnasEscenarios;
   }

   public void setFiltrarLovColumnasEscenarios(List<ColumnasEscenarios> setFiltrarLovColumnasEscenarios) {
      this.filtrarLovColumnasEscenarios = setFiltrarLovColumnasEscenarios;
   }

   public ColumnasEscenarios getEditarColumna() {
      return editarColumna;
   }

   public void setEditarColumna(ColumnasEscenarios setEditarColumna) {
      this.editarColumna = setEditarColumna;
   }

   public ColumnasEscenarios getColumnaEscenarioSeleccionada() {
      return columnaEscenarioSeleccionada;
   }

   public void setColumnaEscenarioSeleccionada(ColumnasEscenarios setColumnaEscenarioSeleccionada) {
      this.columnaEscenarioSeleccionada = setColumnaEscenarioSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public ColumnasEscenarios getColumnaSeleccionada() {
      getListaColumnasEscenarios();
      if (listaColumnasEscenarios != null) {
         int tam = listaColumnasEscenarios.size();
         if (tam > 0) {
            columnaSeleccionada = listaColumnasEscenarios.get(0);
         }
      }
      return columnaSeleccionada;
   }

   public void setColumnaSeleccionada(ColumnasEscenarios columnaSeleccionada) {
      this.columnaSeleccionada = columnaSeleccionada;
   }

}
