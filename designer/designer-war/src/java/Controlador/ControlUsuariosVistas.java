package Controlador;

import Entidades.ObjetosDB;
import Entidades.UsuariosVistas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarUsuariosVistasInterface;
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
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlUsuariosVistas implements Serializable {

   @EJB
   AdministrarUsuariosVistasInterface administrarUsuarioVistas;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<UsuariosVistas> listaUsuariosVistas;
   private List<UsuariosVistas> filtrarUsuariosVistas;
   private String mensajeValidacion;
   private List<UsuariosVistas> listaUsuariosVistasCrear;
   private List<UsuariosVistas> listaUsuariosVistasModificar;
   private List<UsuariosVistas> listaUsuariosVistasBorrar;
   private List<ObjetosDB> lovObjetosDB;
   private List<ObjetosDB> lovFiltradoObjetosDB;
   private ObjetosDB objetosDBSeleccionado;
   private UsuariosVistas nuevaUsuariosVistas;
   private UsuariosVistas duplicarUsuariosVistas;
   private UsuariosVistas eliminarUsuariosVistas;
   private UsuariosVistas editarUsuariosVistas;
   private UsuariosVistas usuariosVistasSeleccionado;
   public String altoTabla;
   public String infoRegistroObjetosDB;
   private boolean permitirIndex;
   private String tablaImprimir, nombreArchivo;
   private Column usuariovistaDescripcion, usuariovistaNombreVista, usuariovistaObjetoDB, usuariovistaAlias, baseestructura,
           usuariovistaBaseEstructura, usuariovistaEstructuraJOIN, usuariovistaCondicion, usuariovistaHINTPrincipal;
   public String infoRegistro;
   public boolean buscador;
   //otros
   private int cualCelda, tipoLista, indiceAux, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   public String paginaAnterior;
   private Map<String, Object> mapParametros;
   private boolean activarLov;

   public ControlUsuariosVistas() {
      permitirIndex = true;
      aceptar = true;
      tipoLista = 0;
      listaUsuariosVistas = null;
      listaUsuariosVistasCrear = new ArrayList<UsuariosVistas>();
      listaUsuariosVistasModificar = new ArrayList<UsuariosVistas>();
      listaUsuariosVistasBorrar = new ArrayList<UsuariosVistas>();
      lovObjetosDB = null;
      cualCelda = -1;
      tipoLista = 0;
      nuevaUsuariosVistas = new UsuariosVistas();
      nuevaUsuariosVistas.setObjetodb(new ObjetosDB());
      duplicarUsuariosVistas = new UsuariosVistas();
      eliminarUsuariosVistas = new UsuariosVistas();
      duplicarUsuariosVistas.setObjetodb(new ObjetosDB());
      usuariosVistasSeleccionado = null;
      k = 0;
      altoTabla = "330";
      guardado = true;
      buscador = false;
      tablaImprimir = ":formExportar:datosUsuariosVistasExportar";
      nombreArchivo = "UsuariosVistasXML";
      paginaAnterior = "nominaf";
      mapParametros = new LinkedHashMap<String, Object>();
      mapParametros.put("paginaAnterior", paginaAnterior);
      activarLov = true;
   }

   public void limpiarListasValor() {
      lovObjetosDB = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarUsuarioVistas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listaUsuariosVistas = null;
      getListaUsuariosVistas();
      if (listaUsuariosVistas != null) {
         if (!listaUsuariosVistas.isEmpty()) {
            usuariosVistasSeleccionado = listaUsuariosVistas.get(0);
         }
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "usuariovista";
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

   public String redirigir() {
      return paginaAnterior;
   }

   public void activarAceptar() {
      aceptar = false;
   }

   //UBICACION CELDA
   public void cambiarIndice(UsuariosVistas usuariov, int celda) {
      if (permitirIndex == true) {
         usuariosVistasSeleccionado = usuariov;
         cualCelda = celda;
         tablaImprimir = ":formExportar:datosUsuariosVistasExportar";
         nombreArchivo = "UsuariosVistasXML";
         usuariosVistasSeleccionado.getSecuencia();

         if (cualCelda == 0) {
            usuariosVistasSeleccionado.getDescripcion();
            deshabilitarBotonLov();
         } else if (cualCelda == 1) {
            usuariosVistasSeleccionado.getNombrevista();
            deshabilitarBotonLov();
         } else if (cualCelda == 2) {
            usuariosVistasSeleccionado.getObjetodb().getNombre();
            habilitarBotonLov();
         } else if (cualCelda == 3) {
            usuariosVistasSeleccionado.getAlias();
            deshabilitarBotonLov();
         } else if (cualCelda == 5) {
            usuariosVistasSeleccionado.getEstructurajoin();
            deshabilitarBotonLov();
         } else if (cualCelda == 6) {
            usuariosVistasSeleccionado.getCondicion();
            deshabilitarBotonLov();
         } else if (cualCelda == 7) {
            usuariosVistasSeleccionado.getHintprincipal();
            deshabilitarBotonLov();
         }
      }
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (usuariosVistasSeleccionado != null) {
         editarUsuariosVistas = usuariosVistasSeleccionado;
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcion').show()");
            cualCelda = -1;
         }
         if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreVista");
            RequestContext.getCurrentInstance().execute("PF('editarNombreVista').show()");
            cualCelda = -1;
         }
         if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObjetoDB");
            RequestContext.getCurrentInstance().execute("PF('editarObjetoDB').show()");
            cualCelda = -1;
         }
         if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarAlias");
            RequestContext.getCurrentInstance().execute("PF('editarAlias').show()");
            cualCelda = -1;
         }
         if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEstructuraJOIN");
            RequestContext.getCurrentInstance().execute("PF('editarEstructuraJOIN').show()");
            cualCelda = -1;
         }
         if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCondicion");
            RequestContext.getCurrentInstance().execute("PF('editarCondicion').show()");
            cualCelda = -1;
         }
         if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarHINTPrincipal");
            RequestContext.getCurrentInstance().execute("PF('editarHINTPrincipal').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //MOSTRAR L.O.V OBJETOSDB
   public void actualizarObjetosDB() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         usuariosVistasSeleccionado.setObjetodb(objetosDBSeleccionado);
         if (!listaUsuariosVistasCrear.contains(usuariosVistasSeleccionado)) {
            if (listaUsuariosVistasModificar.isEmpty()) {
               listaUsuariosVistasModificar.add(usuariosVistasSeleccionado);
            } else if (!listaUsuariosVistasModificar.contains(usuariosVistasSeleccionado)) {
               listaUsuariosVistasModificar.add(usuariosVistasSeleccionado);
            }
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
      } else if (tipoActualizacion == 1) {
         nuevaUsuariosVistas.setObjetodb(objetosDBSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUsuarioVista");
      } else if (tipoActualizacion == 2) {
         duplicarUsuariosVistas.setObjetodb(objetosDBSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuarioVista");
      }
      lovFiltradoObjetosDB = null;
      objetosDBSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;

      RequestContext.getCurrentInstance().update("formularioDialogos:objetosDBDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVObjetosDB");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarOB");
      context.reset("formularioDialogos:LOVObjetosDB:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVObjetosDB').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('objetosDBDialogo').hide()");
   }

   public void asignarIndexObjeto(UsuariosVistas usuariov, int dlg, int LND) {
      usuariosVistasSeleccionado = usuariov;
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = LND;
      if (dlg == 0) {
         lovObjetosDB = null;
         getLovObjetosDB();
         contarRegistrosLOV();
         RequestContext.getCurrentInstance().update("formularioDialogos:objetosDBDialogo");
         RequestContext.getCurrentInstance().execute("PF('objetosDBDialogo').show()");
      }
   }

   public void cancelarCambioObjetoDB() {
      lovFiltradoObjetosDB = null;
      objetosDBSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:objetosDBDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVObjetosDB");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarOB");
      context.reset("formularioDialogos:LOVObjetosDB:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVObjetosDB').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('objetosDBDialogo').hide()");
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (usuariosVistasSeleccionado != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 2) {
            lovObjetosDB = null;
            getLovObjetosDB();
            contarRegistrosLOV();
            RequestContext.getCurrentInstance().update("formularioDialogos:objetosDBDialogo");
            RequestContext.getCurrentInstance().execute("PF('objetosDBDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void seleccionarBaseEstructura(String estadoBaseEstructura, UsuariosVistas usuariov) {
      usuariosVistasSeleccionado = usuariov;
      if (estadoBaseEstructura != null) {
         if (estadoBaseEstructura.equals("SI")) {
            usuariosVistasSeleccionado.setBaseestructura("S");
         } else if (estadoBaseEstructura.equals("NO")) {
            usuariosVistasSeleccionado.setBaseestructura("N");
         }
      } else {
         usuariosVistasSeleccionado.setBaseestructura(null);
      }
      if (!listaUsuariosVistasCrear.contains(usuariosVistasSeleccionado)) {
         if (listaUsuariosVistasModificar.isEmpty()) {
            listaUsuariosVistasModificar.add(usuariosVistasSeleccionado);
         } else if (!listaUsuariosVistasModificar.contains(usuariosVistasSeleccionado)) {
            listaUsuariosVistasModificar.add(usuariosVistasSeleccionado);
         }
      }
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
      System.out.println("Subtipo: " + usuariosVistasSeleccionado.getBaseestructura());
   }

   public void seleccionarNuevoBaseEstructura(String estadoBaseEstructura, int tipoNuevo) {

      if (tipoNuevo == 1) {
         if (estadoBaseEstructura != null) {
            if (estadoBaseEstructura.equals("SI")) {
               nuevaUsuariosVistas.setBaseestructura("S");
            } else if (estadoBaseEstructura.equals("NO")) {
               nuevaUsuariosVistas.setBaseestructura("N");
            }
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoBaseEstructura");

      } else if (tipoNuevo == 2) {
         if (estadoBaseEstructura != null) {
            if (estadoBaseEstructura.equals("SI")) {
               duplicarUsuariosVistas.setBaseestructura("S");
            } else if (estadoBaseEstructura.equals("NO")) {
               duplicarUsuariosVistas.setBaseestructura("N");
            }
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoBaseEstructura");
      }

   }

   //ACTIVAR F11
   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         System.out.println("Activar");
         usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
         usuariovistaDescripcion.setFilterStyle("width: 85% !important");
         usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
         usuariovistaNombreVista.setFilterStyle("width: 85% !important");
         usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
         usuariovistaObjetoDB.setFilterStyle("width: 85% !important");
         usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
         usuariovistaAlias.setFilterStyle("width: 85% !important");
         baseestructura = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:baseestructura");
         baseestructura.setFilterStyle("width: 85% !important");
         usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
         usuariovistaEstructuraJOIN.setFilterStyle("width: 85% !important;");
         usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
         usuariovistaCondicion.setFilterStyle("width: 85% !important;");
         usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
         usuariovistaHINTPrincipal.setFilterStyle("width: 85% !important;");
         altoTabla = "310";
         RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
         bandera = 1;
         tipoLista = 1;
         System.out.println("TipoLista= " + tipoLista);
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
         usuariovistaDescripcion.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
         usuariovistaNombreVista.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
         usuariovistaObjetoDB.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
         usuariovistaAlias.setFilterStyle("display: none; visibility: hidden;");
         baseestructura = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:baseestructura");
         baseestructura.setFilterStyle("display: none; visibility: hidden");
         usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
         usuariovistaEstructuraJOIN.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
         usuariovistaCondicion.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
         usuariovistaHINTPrincipal.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
         altoTabla = "330";
         bandera = 0;
         filtrarUsuariosVistas = null;
         tipoLista = 0;
      }
   }

   public void eventofiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void modificarUsuariosVistas(UsuariosVistas usuarioVista) {
      usuariosVistasSeleccionado = usuarioVista;
      if (!listaUsuariosVistasCrear.contains(usuariosVistasSeleccionado)) {
         if (listaUsuariosVistasModificar.isEmpty()) {
            listaUsuariosVistasModificar.add(usuariosVistasSeleccionado);
         } else if (!listaUsuariosVistasModificar.contains(usuariosVistasSeleccionado)) {
            listaUsuariosVistasModificar.add(usuariosVistasSeleccionado);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosVistasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "UsuariosVistasPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUsuariosVistasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "UsuariosVistasXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void limpiarNuevaUsuarioVista() {
      nuevaUsuariosVistas = new UsuariosVistas();
      nuevaUsuariosVistas.setObjetodb(new ObjetosDB());
   }

   public void limpiarDuplicarUsuarioVista() {
      duplicarUsuariosVistas = new UsuariosVistas();
      duplicarUsuariosVistas.setObjetodb(new ObjetosDB());
   }

   public void agregarNuevaUsuarioVista() {
      RequestContext context = RequestContext.getCurrentInstance();
      int pasa = 0;
      mensajeValidacion = " ";
      if (nuevaUsuariosVistas.getAlias() == null || nuevaUsuariosVistas.getAlias().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }
      if (nuevaUsuariosVistas.getObjetodb().getNombre() == null || nuevaUsuariosVistas.getObjetodb().getNombre().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }
      if (pasa == 0) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
            usuariovistaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
            usuariovistaNombreVista.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
            usuariovistaObjetoDB.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
            usuariovistaAlias.setFilterStyle("display: none; visibility: hidden;");
            baseestructura = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:baseestructura");
            baseestructura.setFilterStyle("display: none; visibility: hidden");
            usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
            usuariovistaEstructuraJOIN.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
            usuariovistaCondicion.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
            usuariovistaHINTPrincipal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            altoTabla = "330";
            bandera = 0;
            filtrarUsuariosVistas = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevaUsuariosVistas.setSecuencia(l);
         listaUsuariosVistasCrear.add(nuevaUsuariosVistas);
         listaUsuariosVistas.add(nuevaUsuariosVistas);
         usuariosVistasSeleccionado = nuevaUsuariosVistas;
         contarRegistros();
         limpiarNuevaUsuarioVista();
         RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroUsuarioVista");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroUsuarioVista').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuarioVista");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuarioVista').show()");
      }
   }

   public void borrarUsuarioVista() {
      if (usuariosVistasSeleccionado != null) {
         if (!listaUsuariosVistasModificar.isEmpty() && listaUsuariosVistasModificar.contains(usuariosVistasSeleccionado)) {
            int modIndex = listaUsuariosVistasModificar.indexOf(usuariosVistasSeleccionado);
            listaUsuariosVistasModificar.remove(modIndex);
            listaUsuariosVistasBorrar.add(usuariosVistasSeleccionado);
         } else if (!listaUsuariosVistasCrear.isEmpty() && listaUsuariosVistasCrear.contains(usuariosVistasSeleccionado)) {
            int crearIndex = listaUsuariosVistasCrear.indexOf(usuariosVistasSeleccionado);
            listaUsuariosVistasCrear.remove(crearIndex);
         } else {
            listaUsuariosVistasBorrar.add(usuariosVistasSeleccionado);
         }
         listaUsuariosVistas.remove(usuariosVistasSeleccionado);
         if (tipoLista == 1) {
            filtrarUsuariosVistas.remove(usuariosVistasSeleccionado);
         }
         usuariosVistasSeleccionado = null;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarUsuarioVista() {
      if (usuariosVistasSeleccionado != null) {
         duplicarUsuariosVistas = new UsuariosVistas();
         duplicarUsuariosVistas.setDescripcion(usuariosVistasSeleccionado.getDescripcion());
         duplicarUsuariosVistas.setNombrevista(usuariosVistasSeleccionado.getNombrevista());
         duplicarUsuariosVistas.setObjetodb(usuariosVistasSeleccionado.getObjetodb());
         duplicarUsuariosVistas.setAlias(usuariosVistasSeleccionado.getAlias());
         duplicarUsuariosVistas.setBaseestructura(usuariosVistasSeleccionado.getBaseestructura());
         duplicarUsuariosVistas.setEstructurajoin(usuariosVistasSeleccionado.getEstructurajoin());
         duplicarUsuariosVistas.setCondicion(usuariosVistasSeleccionado.getEstructurajoin());
         duplicarUsuariosVistas.setHintprincipal(usuariosVistasSeleccionado.getHintprincipal());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuarioVista");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuarioVistas').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int pasa = 0;

      if (duplicarUsuariosVistas.getAlias() == null || duplicarUsuariosVistas.getAlias().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }
      if (duplicarUsuariosVistas.getObjetodb().getNombre() == null || duplicarUsuariosVistas.getObjetodb().getNombre().equals("")) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
         pasa++;
      }
      if (pasa == 0) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
            usuariovistaDescripcion.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
            usuariovistaNombreVista.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
            usuariovistaObjetoDB.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
            usuariovistaAlias.setFilterStyle("display: none; visibility: hidden;");
            baseestructura = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:baseestructura");
            baseestructura.setFilterStyle("display: none; visibility: hidden");
            usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
            usuariovistaEstructuraJOIN.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
            usuariovistaCondicion.setFilterStyle("display: none; visibility: hidden;");
            usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
            usuariovistaHINTPrincipal.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            altoTabla = "330";
            bandera = 0;
            filtrarUsuariosVistas = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         duplicarUsuariosVistas.setSecuencia(l);
         listaUsuariosVistas.add(duplicarUsuariosVistas);
         listaUsuariosVistasCrear.add(duplicarUsuariosVistas);
         usuariosVistasSeleccionado = duplicarUsuariosVistas;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
         duplicarUsuariosVistas = new UsuariosVistas();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUsuarioVista");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroUsuarioVistas').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaUsuarioVista");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaUsuarioVista').show()");
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
         usuariovistaDescripcion.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
         usuariovistaNombreVista.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
         usuariovistaObjetoDB.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
         usuariovistaAlias.setFilterStyle("display: none; visibility: hidden;");
         baseestructura = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:baseestructura");
         baseestructura.setFilterStyle("display: none; visibility: hidden");
         usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
         usuariovistaEstructuraJOIN.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
         usuariovistaCondicion.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
         usuariovistaHINTPrincipal.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
         altoTabla = "330";
         bandera = 0;
         filtrarUsuariosVistas = null;
         tipoLista = 0;
      }
      listaUsuariosVistasBorrar.clear();
      listaUsuariosVistasCrear.clear();
      listaUsuariosVistasModificar.clear();
      usuariosVistasSeleccionado = null;
      k = 0;
      listaUsuariosVistas = null;
      getListaUsuariosVistas();
      contarRegistros();
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         usuariovistaDescripcion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:descripcion");
         usuariovistaDescripcion.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaNombreVista = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:nombrevista");
         usuariovistaNombreVista.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaObjetoDB = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:objetodb");
         usuariovistaObjetoDB.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaAlias = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:alias");
         usuariovistaAlias.setFilterStyle("display: none; visibility: hidden;");
         baseestructura = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:baseestructura");
         baseestructura.setFilterStyle("display: none; visibility: hidden");
         usuariovistaEstructuraJOIN = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:estructurajoin");
         usuariovistaEstructuraJOIN.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaCondicion = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:condicion");
         usuariovistaCondicion.setFilterStyle("display: none; visibility: hidden;");
         usuariovistaHINTPrincipal = (Column) c.getViewRoot().findComponent("form:datosUsuariosVistas:hintprincipal");
         usuariovistaHINTPrincipal.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
         altoTabla = "330";
         bandera = 0;
         filtrarUsuariosVistas = null;
         tipoLista = 0;
      }
      listaUsuariosVistasBorrar.clear();
      listaUsuariosVistasCrear.clear();
      listaUsuariosVistasModificar.clear();
      usuariosVistasSeleccionado = null;
      k = 0;
      listaUsuariosVistas = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      navegar("atras");
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (usuariosVistasSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(usuariosVistasSeleccionado.getSecuencia(), "USUARIOSVISTAS");
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
      } else if (administrarRastros.verificarHistoricosTabla("USUARIOSVISTAS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void guardarYSalir() {
      guardarCambiosUsuarioVista();
      salir();
   }

   public void guardarCambiosUsuarioVista() {
      try {
         if (guardado == false) {
            if (!listaUsuariosVistasBorrar.isEmpty()) {
               administrarUsuarioVistas.borrarUsuariosVistas(listaUsuariosVistasBorrar);
               listaUsuariosVistasBorrar.clear();
            }
            if (!listaUsuariosVistasCrear.isEmpty()) {
               administrarUsuarioVistas.crearUsuariosVistas(listaUsuariosVistasCrear);
               listaUsuariosVistasCrear.clear();
            }
            if (!listaUsuariosVistasModificar.isEmpty()) {
               administrarUsuarioVistas.modificarUsuariosVistas(listaUsuariosVistasModificar);
               listaUsuariosVistasModificar.clear();
            }
            listaUsuariosVistas = null;
            getListaUsuariosVistas();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosUsuariosVistas");
            guardado = true;
            permitirIndex = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            usuariosVistasSeleccionado = null;
         }
      } catch (Exception e) {
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void crearUsuarioVista() {
      try {
         if (usuariosVistasSeleccionado != null) {
            administrarUsuarioVistas.crearUsuarioVistaDB(usuariosVistasSeleccionado.getObjetodb().getSecuencia());
            RequestContext.getCurrentInstance().execute("PF('crearVistaUsuario').show()");
         }
      } catch (Exception e) {
         System.out.println("error ControlUsuariosEstructuras.crearVistaUsuario() : " + e.getMessage());
         RequestContext.getCurrentInstance().execute("PF('errorCrearVistaUsuario').show()");
      }
   }

   public void contarRegistrosLOV() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroObjetosDB");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //GETTER AND SETTER
   public List<UsuariosVistas> getListaUsuariosVistasCrear() {
      return listaUsuariosVistasCrear;
   }

   public void setListaUsuariosVistasCrear(List<UsuariosVistas> listaUsuariosVistasCrear) {
      this.listaUsuariosVistasCrear = listaUsuariosVistasCrear;
   }

   public List<UsuariosVistas> getListaUsuariosVistasModificar() {
      return listaUsuariosVistasModificar;
   }

   public void setListaUsuariosVistasModificar(List<UsuariosVistas> listaUsuariosVistasModificar) {
      this.listaUsuariosVistasModificar = listaUsuariosVistasModificar;
   }

   public List<UsuariosVistas> getListaUsuariosVistasBorrar() {
      return listaUsuariosVistasBorrar;
   }

   public void setListaUsuariosVistasBorrar(List<UsuariosVistas> listaUsuariosVistasBorrar) {
      this.listaUsuariosVistasBorrar = listaUsuariosVistasBorrar;
   }

   public List<UsuariosVistas> getListaUsuariosVistas() {
      if (listaUsuariosVistas == null) {
         listaUsuariosVistas = administrarUsuarioVistas.consultarUsuariosVistas();
      }
      return listaUsuariosVistas;
   }

   public void setListaUsuariosVistas(List<UsuariosVistas> listaUsuariosVistas) {
      this.listaUsuariosVistas = listaUsuariosVistas;
   }

   public List<UsuariosVistas> getFiltrarUsuariosVistas() {
      return filtrarUsuariosVistas;
   }

   public void setFiltrarUsuariosVistas(List<UsuariosVistas> filtrarUsuariosVistas) {
      this.filtrarUsuariosVistas = filtrarUsuariosVistas;
   }

   public ObjetosDB getObjetosDBSeleccionado() {
      return objetosDBSeleccionado;
   }

   public void setObjetosDBSeleccionado(ObjetosDB objetosDBSeleccionado) {
      this.objetosDBSeleccionado = objetosDBSeleccionado;
   }

   public List<ObjetosDB> getLovObjetosDB() {
      if (lovObjetosDB == null) {
         lovObjetosDB = administrarUsuarioVistas.consultarObjetosDB();
      }
      return lovObjetosDB;
   }

   public String getInfoRegistroObjetosDB() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVObjetosDB");
      infoRegistroObjetosDB = String.valueOf(tabla.getRowCount());
      return infoRegistroObjetosDB;
   }

   public void setInfoRegistroObjetosDB(String infoRegistroObjetosDB) {
      this.infoRegistroObjetosDB = infoRegistroObjetosDB;
   }

   public void setLovObjetosDB(List<ObjetosDB> lovObjetosDB) {
      this.lovObjetosDB = lovObjetosDB;
   }

   public List<ObjetosDB> getLovFiltradoObjetosDB() {
      return lovFiltradoObjetosDB;
   }

   public void setLovFiltradoObjetosDB(List<ObjetosDB> lovFiltradoObjetosDB) {
      this.lovFiltradoObjetosDB = lovFiltradoObjetosDB;
   }

   public UsuariosVistas getNuevaUsuariosVistas() {
      return nuevaUsuariosVistas;
   }

   public void setNuevaUsuariosVistas(UsuariosVistas nuevaUsuariosVistas) {
      this.nuevaUsuariosVistas = nuevaUsuariosVistas;
   }

   public UsuariosVistas getDuplicarUsuariosVistas() {
      return duplicarUsuariosVistas;
   }

   public void setDuplicarUsuariosVistas(UsuariosVistas duplicarUsuariosVistas) {
      this.duplicarUsuariosVistas = duplicarUsuariosVistas;
   }

   public UsuariosVistas getEditarUsuariosVistas() {
      return editarUsuariosVistas;
   }

   public void setEditarUsuariosVistas(UsuariosVistas editarUsuariosVistas) {
      this.editarUsuariosVistas = editarUsuariosVistas;
   }

   public UsuariosVistas getUsuariosVistasSeleccionado() {
      return usuariosVistasSeleccionado;
   }

   public void setUsuariosVistasSeleccionado(UsuariosVistas usuariosVistasSeleccionado) {
      this.usuariosVistasSeleccionado = usuariosVistasSeleccionado;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
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

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosUsuariosVistas");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public UsuariosVistas getEliminarUsuariosVistas() {
      return eliminarUsuariosVistas;
   }

   public void setEliminarUsuariosVistas(UsuariosVistas eliminarUsuariosVistas) {
      this.eliminarUsuariosVistas = eliminarUsuariosVistas;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
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
