package Controlador;

import Entidades.Conceptos;
import Entidades.ConceptosRedondeos;
import Entidades.TiposRedondeos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarConceptosRedondeosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
 * @author Victor Algarin
 */
@ManagedBean
@SessionScoped
public class ControlConceptoRedondeo implements Serializable {

   @EJB
   AdministrarConceptosRedondeosInterface administrarConceptosRedondeos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //Lista ConceptosRedondeos
   private List<ConceptosRedondeos> listaConceptosRedondeos;
   private List<ConceptosRedondeos> filtradoListaConceptosRedondeos;
   private ConceptosRedondeos conceptoRedondeoSeleccionado;
   //Columnas Tabla VC
   private Column conceptoRedondeoConcepto, conceptoRedondeoTipoRedondeo;
   //OTROS
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   private boolean permitirIndex;
   private boolean aceptar;
   //AUTOCOMPLETAR
   private String Concepto, TipoRedondeo;
   private String altoScrollConceptosRedondeos;
   //modificar
   private List<ConceptosRedondeos> listaConceptosRedondeosModificar;
   private boolean guardado;
   //crear VC
   public ConceptosRedondeos nuevoConceptoRedondeo;
   private List<ConceptosRedondeos> listaConceptosRedondeosCrear;
   private BigInteger l;
   private int k;
   private String mensajeValidacion;
   //borrar VC
   private List<ConceptosRedondeos> listaConceptosRedondeosBorrar;
   //editar celda
   private ConceptosRedondeos editarConceptosRedondeos;
   private int cualCelda, tipoLista;
   private boolean cambioEditor, aceptarEditar;
   //duplicar
   private ConceptosRedondeos duplicarConceptoRedondeo;
   //RASTROS
   private boolean cambiosPagina;
   //L.O.V Conceptos Redondeos
   private List<ConceptosRedondeos> lovConceptosRedondeos;
   private List<ConceptosRedondeos> filtradoLovConceptosRedondeos;
   private ConceptosRedondeos conceptoRedondeoLovSeleccionado;
   //L.O.V Conceptos
   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtradoLovConceptos;
   public Conceptos conceptoLovSeleccionado;
   //L.O.V Tipo
   private List<TiposRedondeos> lovTiposRedondeos;
   private List<TiposRedondeos> filtradoLovTiposRedondeos;
   private TiposRedondeos tipoRedondeoLovSeleccionado;
   //Conteo de registros
   private String infoRegistro, infoRegistroLov, infoRegistroLovConceptos, infoRegistroLovTipos;

   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   /**
    * Constructor de ControlConceptoRedondeo
    */
   public ControlConceptoRedondeo() {
      altoScrollConceptosRedondeos = "315";
      permitirIndex = true;
      listaConceptosRedondeos = null;
      //Otros
      aceptar = true;
      //borrar aficiones
      listaConceptosRedondeosBorrar = new ArrayList<ConceptosRedondeos>();
      //crear aficiones
      listaConceptosRedondeosCrear = new ArrayList<ConceptosRedondeos>();
      k = 0;
      //modificar aficiones
      listaConceptosRedondeosModificar = new ArrayList<ConceptosRedondeos>();
      //editar
      editarConceptosRedondeos = new ConceptosRedondeos();
      cambioEditor = false;
      aceptarEditar = true;
      cualCelda = -1;
      tipoLista = 0;
      //guardar
      guardado = true;
      //Crear VC
      nuevoConceptoRedondeo = new ConceptosRedondeos();
      duplicarConceptoRedondeo = new ConceptosRedondeos();
      conceptoRedondeoSeleccionado = null;
      cambiosPagina = true;
      paginaAnterior = "nominaf";
      //Conteo de registros
      infoRegistro = "";
      infoRegistroLov = "";
      infoRegistroLovConceptos = "";
      infoRegistroLovTipos = "";
      mapParametros.put("paginaAnterior", paginaAnterior);
   }
   
   public void recibirPaginaEntrante(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
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
         String pagActual = "conceptoredondeo";
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
         administrarConceptosRedondeos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (conceptoRedondeoSeleccionado != null) {
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposRedondeosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposRedondeosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   //AUTOCOMPLETAR
   public void modificarConceptosRedondeos(ConceptosRedondeos conceptoRed, String confirmarCambio, String valorConfirmar) {
      conceptoRedondeoSeleccionado = conceptoRed;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      System.out.println("Entro a Modificar ConceptosRedondeos");

      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaConceptosRedondeosCrear.contains(conceptoRedondeoSeleccionado)) {

            if (listaConceptosRedondeosModificar.isEmpty()) {
               listaConceptosRedondeosModificar.add(conceptoRedondeoSeleccionado);
            } else if (!listaConceptosRedondeosModificar.contains(conceptoRedondeoSeleccionado)) {
               listaConceptosRedondeosModificar.add(conceptoRedondeoSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
      } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         conceptoRedondeoSeleccionado.getConcepto().setDescripcion(Concepto);
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            conceptoRedondeoSeleccionado.setConcepto(lovConceptos.get(indiceUnicoElemento));
            lovConceptos.clear();
            getLovConceptos();
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = 0;
         }
      }

   }

   //Ubicacion Celda.
   /**
    * Metodo que obtiene la posicion dentro de la tabla ConceptosRedondeos
    *
    * @param indice Fila de la tabla
    * @param celda Columna de la tabla
    */
   //UBICACION CELDA
   public void cambiarIndice(ConceptosRedondeos conceptoRed, int celda) {
      conceptoRedondeoSeleccionado = conceptoRed;
      if (permitirIndex == true) {
         cualCelda = celda;

         if (cualCelda == 0) {
            Concepto = conceptoRedondeoSeleccionado.getConcepto().getDescripcion();
         } else if (cualCelda == 1) {
            TipoRedondeo = conceptoRedondeoSeleccionado.getTiporedondeo().getDescripcion();
         }

      }
   }

   public void actualizarConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         conceptoRedondeoSeleccionado.setConcepto(conceptoLovSeleccionado);
         if (!listaConceptosRedondeosCrear.contains(conceptoRedondeoSeleccionado)) {
            if (listaConceptosRedondeosModificar.isEmpty()) {
               listaConceptosRedondeosModificar.add(conceptoRedondeoSeleccionado);
            } else if (!listaConceptosRedondeosModificar.contains(conceptoRedondeoSeleccionado)) {
               listaConceptosRedondeosModificar.add(conceptoRedondeoSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
      } else if (tipoActualizacion == 1) {
         System.out.println("Entro al Nuevo, como esperaba");
         nuevoConceptoRedondeo.setConcepto(conceptoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConcepto");
      } else if (tipoActualizacion == 2) {
         duplicarConceptoRedondeo.setConcepto(conceptoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");

      }
      filtradoLovConceptos = null;
      conceptoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVConceptos");
      RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
   }

   public void cancelarCambiosConceptos() {
      filtradoLovConceptos = null;
      conceptoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVConceptos");
      RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
   }

   public void actualizarTiposRedondeos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         conceptoRedondeoSeleccionado.setTiporedondeo(tipoRedondeoLovSeleccionado);
         if (!listaConceptosRedondeosCrear.contains(conceptoRedondeoSeleccionado)) {
            if (listaConceptosRedondeosModificar.isEmpty()) {
               listaConceptosRedondeosModificar.add(conceptoRedondeoSeleccionado);
            } else if (!listaConceptosRedondeosModificar.contains(conceptoRedondeoSeleccionado)) {
               listaConceptosRedondeosModificar.add(conceptoRedondeoSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
      } else if (tipoActualizacion == 1) {
         nuevoConceptoRedondeo.setTiporedondeo(tipoRedondeoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
      } else if (tipoActualizacion == 2) {
         duplicarConceptoRedondeo.setTiporedondeo(tipoRedondeoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");

      }
      filtradoLovTiposRedondeos = null;
      tipoRedondeoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVTiposRedondeos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTiposRedondeos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tiposRedondeosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposRedondeos");
      RequestContext.getCurrentInstance().update("formularioDialogos:tiposRedondeosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
      RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
   }

   public void cancelarCambiosTiposRedondeos() {
      filtradoLovTiposRedondeos = null;
      tipoRedondeoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext.getCurrentInstance().reset("formularioDialogos:LOVTiposRedondeos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTiposRedondeos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tiposRedondeosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposRedondeos");
      RequestContext.getCurrentInstance().update("formularioDialogos:tiposRedondeosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
      RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
   }

   public void actualizarConceptosRedondeos() {
      System.out.println("conceptosRedondeosSeleccionado : " + conceptoRedondeoLovSeleccionado.getConcepto().getDescripcion());

      if (!listaConceptosRedondeos.isEmpty()) {
         listaConceptosRedondeos.clear();
      }
      listaConceptosRedondeos.add(conceptoRedondeoLovSeleccionado);

      RequestContext.getCurrentInstance().reset("formularioDialogos:LOVConceptosRedondeos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptosRedondeos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosRedondeosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVConceptosRedondeos");
      RequestContext.getCurrentInstance().update("formularioDialogos:conceptosRedondeosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCR");
      RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
      contarRegistros();
      filtradoListaConceptosRedondeos = null;
      conceptoRedondeoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   public void cancelarCambiosConceptosRedondeos() {
      filtradoListaConceptosRedondeos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVConceptosRedondeos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVConceptosRedondeos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosRedondeosDialogo').hide()");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVConceptosRedondeos");
      RequestContext.getCurrentInstance().update("formularioDialogos:conceptosRedondeosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCR");
      RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
   }

   public void mostrarTodos() {
      if (!listaConceptosRedondeos.isEmpty()) {
         listaConceptosRedondeos.clear();
      }
      listaConceptosRedondeos = administrarConceptosRedondeos.consultarConceptosRedondeos();
      RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
      contarRegistros();
      filtradoListaConceptosRedondeos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
   }

   public void asignarIndex(ConceptosRedondeos conceptoRed, int campo, int tipoAct) {
      conceptoRedondeoSeleccionado = conceptoRed;
      tipoActualizacion = tipoAct;
      if (campo == 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (campo == 1) {
         RequestContext.getCurrentInstance().update("formularioDialogos:tiposRedondeosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tiposRedondeosDialogo').show()");
      } else if (campo == 2) {
         RequestContext.getCurrentInstance().update("formularioDialogos:conceptosRedondeosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosRedondeosDialogo').show()");
      }
   }

   public void asignarIndex(int campo, int tipoAct) {
      tipoActualizacion = tipoAct;

      if (campo == 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      } else if (campo == 1) {
         RequestContext.getCurrentInstance().update("formularioDialogos:tiposRedondeosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tiposRedondeosDialogo').show()");
      } else if (campo == 2) {
         RequestContext.getCurrentInstance().update("formularioDialogos:conceptosRedondeosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosRedondeosDialogo').show()");
      }
   }

   //CANCELAR MODIFICACIONES
   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacion() {
      //CANCELAR MODIFICACIONES
      if (bandera == 1) {
         restaurarTabla();
      }
      listaConceptosRedondeosBorrar.clear();
      listaConceptosRedondeosCrear.clear();
      listaConceptosRedondeosModificar.clear();
      cambiosPagina = true;
      conceptoRedondeoSeleccionado = null;
      k = 0;
      listaConceptosRedondeos = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void cancelarCambioConceptosRedondeos() {
      filtradoLovConceptos = null;
      conceptoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
   }

   //MOSTRAR DATOS CELDA
   /**
    * Metodo que muestra los dialogos de editar con respecto a la lista real o
    * la lista filtrada y a la columna
    */
   public void editarCelda() {
      if (conceptoRedondeoSeleccionado != null) {
         editarConceptosRedondeos = conceptoRedondeoSeleccionado;

         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConcepto");
            RequestContext.getCurrentInstance().execute("PF('editarConcepto').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoRedondeo");
            RequestContext.getCurrentInstance().execute("PF('editarTipoRedondeo').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //CREAR CONCEPTOS REDONDEO
   /**
    * Metodo que se encarga de agregar un nuevo concepto redondeo
    */
   public void agregarNuevoConceptoRedondeo() {

      mensajeValidacion = new String();
      int pasa = 0;

      if (nuevoConceptoRedondeo.getConcepto().getDescripcion().equals(" ")) {
         mensajeValidacion = mensajeValidacion + " * Concepto\n";
         pasa++;
      }
      if (nuevoConceptoRedondeo.getTiporedondeo().getDescripcion().equals(" ")) {
         mensajeValidacion = mensajeValidacion + " * Tipo Redondeo\n";
         pasa++;
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoConceptoRedondeo");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoConceptoRedondeo').show()");
      }
      if (bandera == 1) {
         restaurarTabla();
      }
      //AGREGAR REGISTRO A LA LISTA NOVEDADES.
      k++;
      l = BigInteger.valueOf(k);
      nuevoConceptoRedondeo.setSecuencia(l);

      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      listaConceptosRedondeosCrear.add(nuevoConceptoRedondeo);
      listaConceptosRedondeos.add(nuevoConceptoRedondeo);
      conceptoRedondeoSeleccionado = listaConceptosRedondeos.get(listaConceptosRedondeos.indexOf(nuevoConceptoRedondeo));
      RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
      nuevoConceptoRedondeo = new ConceptosRedondeos();
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroConceptosRedondeos').hide()");
      contarRegistros();
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("CONCEPTO")) {
         if (tipoNuevo == 1) {
            Concepto = nuevoConceptoRedondeo.getConcepto().getDescripcion();
         } else if (tipoNuevo == 2) {
            Concepto = duplicarConceptoRedondeo.getConcepto().getDescripcion();
         }
      } else if (Campo.equals("TIPO")) {
         if (tipoNuevo == 1) {
            TipoRedondeo = nuevoConceptoRedondeo.getTiporedondeo().getDescripcion();
         } else if (tipoNuevo == 2) {
            TipoRedondeo = duplicarConceptoRedondeo.getTiporedondeo().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
         if (tipoNuevo == 1) {
            nuevoConceptoRedondeo.getConcepto().setDescripcion(Concepto);
         } else if (tipoNuevo == 2) {
            duplicarConceptoRedondeo.getConcepto().setDescripcion(Concepto);
         }
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoConceptoRedondeo.setConcepto(lovConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConcepto");
            } else if (tipoNuevo == 2) {
               duplicarConceptoRedondeo.setConcepto(lovConceptos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
            }
            lovConceptos.clear();
            getLovConceptos();
         } else {
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoConcepto");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarConcepto");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("TIPO")) {
         if (tipoNuevo == 1) {
            nuevoConceptoRedondeo.getTiporedondeo().setDescripcion(TipoRedondeo);
         } else if (tipoNuevo == 2) {
            duplicarConceptoRedondeo.getTiporedondeo().setDescripcion(TipoRedondeo);
         }

         for (int i = 0; i < lovTiposRedondeos.size(); i++) {
            if (lovTiposRedondeos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoConceptoRedondeo.setTiporedondeo(lovTiposRedondeos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");
            } else if (tipoNuevo == 2) {
               duplicarConceptoRedondeo.setTiporedondeo(lovTiposRedondeos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");
            }
            lovTiposRedondeos.clear();
            getLovTiposRedondeos();
         } else {
            RequestContext.getCurrentInstance().update("form:tiposRedondeosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposRedondeosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipo");

            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipo");

            }
         }

      }
   }
   //LIMPIAR NUEVO REGISTRO

   /**
    * Metodo que limpia las casillas del nuevo Concepto Redondeo
    */
   public void limpiarNuevaConceptosRedondeos() {
      nuevoConceptoRedondeo = new ConceptosRedondeos();
   }

   //DUPLICAR VC
   /**
    * Metodo que duplica un ConceptoRedondeo especifico dado por la posicion de
    * la fila
    */
   public void duplicarConceptosRedondeos() {
      if (conceptoRedondeoSeleccionado != null) {
         duplicarConceptoRedondeo = new ConceptosRedondeos();
         k++;
         l = BigInteger.valueOf(k);

         duplicarConceptoRedondeo.setSecuencia(l);
         duplicarConceptoRedondeo.setConcepto(conceptoRedondeoSeleccionado.getConcepto());
         duplicarConceptoRedondeo.setTiporedondeo(conceptoRedondeoSeleccionado.getTiporedondeo());
         System.out.println("Concepto Duplicar : " + conceptoRedondeoSeleccionado.getConcepto());

         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroConceptosRedondeos");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroConceptosRedondeos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla
    * ConceptosRedondeos
    */
   public void confirmarDuplicar() {
      if (bandera == 1) {
         restaurarTabla();
      }
      //AGREGAR REGISTRO A LA LISTA NOVEDADES .
      k++;
      l = BigInteger.valueOf(k);
      duplicarConceptoRedondeo.setSecuencia(l);

      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      listaConceptosRedondeosCrear.add(duplicarConceptoRedondeo);
      listaConceptosRedondeos.add(duplicarConceptoRedondeo);
      conceptoRedondeoSeleccionado = listaConceptosRedondeos.get(listaConceptosRedondeos.indexOf(duplicarConceptoRedondeo));
      RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
      duplicarConceptoRedondeo = new ConceptosRedondeos();
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      contarRegistros();
      RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroConceptosRedondeos').hide()");
      duplicarConceptoRedondeo = new ConceptosRedondeos();
   }

   //LIMPIAR DUPLICAR
   /**
    * Metodo que limpia los datos de un duplicar ConceptoRedondeo
    */
   public void limpiarDuplicarConceptosRedondeos() {
      duplicarConceptoRedondeo = new ConceptosRedondeos();
   }

   //BORRAR VC
   /**
    * Metodo que borra los ConceptosRedondeos seleccionados
    */
   public void borrarConceptosRedondeos() {

      if (conceptoRedondeoSeleccionado != null) {
         if (!listaConceptosRedondeosModificar.isEmpty() && listaConceptosRedondeosModificar.contains(conceptoRedondeoSeleccionado)) {
            listaConceptosRedondeosModificar.remove(conceptoRedondeoSeleccionado);
            listaConceptosRedondeosBorrar.add(conceptoRedondeoSeleccionado);
         } else if (!listaConceptosRedondeosCrear.isEmpty() && listaConceptosRedondeosCrear.contains(conceptoRedondeoSeleccionado)) {
            listaConceptosRedondeosCrear.remove(conceptoRedondeoSeleccionado);
         } else {
            listaConceptosRedondeosBorrar.add(conceptoRedondeoSeleccionado);
         }
         listaConceptosRedondeos.remove(conceptoRedondeoSeleccionado);
         if (tipoLista == 1) {
            filtradoListaConceptosRedondeos.remove(conceptoRedondeoSeleccionado);
         }

         RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
         contarRegistros();
         conceptoRedondeoSeleccionado = null;

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void restaurarTabla() {
      altoScrollConceptosRedondeos = "315";
      conceptoRedondeoConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoConcepto");
      conceptoRedondeoConcepto.setFilterStyle("display: none; visibility: hidden;");
      conceptoRedondeoTipoRedondeo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoTipoRedondeo");
      conceptoRedondeoTipoRedondeo.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
      bandera = 0;
      filtradoListaConceptosRedondeos = null;
      tipoLista = 0;
   }
   //CTRL + F11 ACTIVAR/DESACTIVAR

   /**
    * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      if (bandera == 0) {
         altoScrollConceptosRedondeos = "295";
         conceptoRedondeoConcepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoConcepto");
         conceptoRedondeoConcepto.setFilterStyle("width: 85% !important");
         conceptoRedondeoTipoRedondeo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosConceptosRedondeos:conceptoRedondeoTipoRedondeo");
         conceptoRedondeoTipoRedondeo.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
         bandera = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      if (bandera == 1) {
         restaurarTabla();
      }
      listaConceptosRedondeosBorrar.clear();
      listaConceptosRedondeosCrear.clear();
      listaConceptosRedondeosModificar.clear();
      conceptoRedondeoSeleccionado = null;
      k = 0;
      listaConceptosRedondeos = null;
      guardado = true;
   }

   /**
    * Metodo que activa el boton aceptar de la pantalla y dialogos
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
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosRedondeosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ConceptosRedondeosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosRedondeosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ConceptosRedondeosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }
   //EVENTO FILTRAR

   /**
    * Evento que cambia la lista reala a la filtrada
    */
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      conceptoRedondeoSeleccionado = null;
      contarRegistros();
   }
   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

   public void verificarRastro() {
      System.out.println("lol");
      if (conceptoRedondeoSeleccionado != null) {
         System.out.println("lol 2");
         int resultado = administrarRastros.obtenerTabla(conceptoRedondeoSeleccionado.getSecuencia(), "CONCEPTOSREDONDEOS");
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
//         } else {
//            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//         }
      } else if (administrarRastros.verificarHistoricosTabla("CONCEPTOSREDONDEOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //GUARDAR
   public void guardarCambiosConceptosRedondeos() {
      if (guardado == false) {
         System.out.println("Realizando Operaciones ConceptosRedondeos");

         if (!listaConceptosRedondeosBorrar.isEmpty()) {
            for (int i = 0; i < listaConceptosRedondeosBorrar.size(); i++) {
               System.out.println("Borrando..." + listaConceptosRedondeosBorrar.size());
               administrarConceptosRedondeos.borrarConceptosRedondeos(listaConceptosRedondeosBorrar.get(i));
            }
            System.out.println("Entra");
            listaConceptosRedondeosBorrar.clear();
         }

         if (!listaConceptosRedondeosCrear.isEmpty()) {
            for (int i = 0; i < listaConceptosRedondeosCrear.size(); i++) {
               System.out.println("Creando...");
               administrarConceptosRedondeos.crearConceptosRedondeos(listaConceptosRedondeosCrear.get(i));
            }
            System.out.println("LimpiaLista");
            listaConceptosRedondeosCrear.clear();
         }
         if (!listaConceptosRedondeosModificar.isEmpty()) {
            administrarConceptosRedondeos.modificarConceptosRedondeos(listaConceptosRedondeosModificar);
            listaConceptosRedondeosModificar.clear();
         }

         System.out.println("Se guardaron los datos con exito");
         listaConceptosRedondeos = null;

         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosConceptosRedondeos");
         contarRegistros();
         guardado = true;
         lovConceptosRedondeos = null;
         RequestContext.getCurrentInstance().update("formularioDialogos:LOVConceptosRedondeos");
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         //  k = 0;
      }
   }

   public String volverPaginaAnterior() {
      return paginaAnterior;
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLov() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLov");
   }

   public void contarRegistrosLovConceptos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovConceptos");
   }

   public void contarRegistrosLovTipos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroLovTipos");
   }
   //GETTERS AND SETTERS

   /**
    * Metodo que obtiene la lista de ConceptosRedondeos de un Empleado, en caso
    * de que sea null hace el llamado al metodo de obtener ConceptosRedondeos
    * del empleado, en caso contrario no genera operaciones
    *
    * @return listS Lista de ConceptosRedondeos de una Persona
    */
   public List<ConceptosRedondeos> getListaConceptosRedondeos() {
      if (listaConceptosRedondeos == null) {
         listaConceptosRedondeos = administrarConceptosRedondeos.consultarConceptosRedondeos();
      }
      return listaConceptosRedondeos;
   }

   public void setListaConceptosRedondeos(List<ConceptosRedondeos> listaConceptosRedondeos) {
      this.listaConceptosRedondeos = listaConceptosRedondeos;
   }

   public List<ConceptosRedondeos> getLovConceptosRedondeos() {
      if (lovConceptosRedondeos == null) {
         lovConceptosRedondeos = administrarConceptosRedondeos.consultarConceptosRedondeos();
      }
      return lovConceptosRedondeos;
   }

   public void setLovConceptosRedondeos(List<ConceptosRedondeos> lovConceptosRedondeos) {
      this.lovConceptosRedondeos = lovConceptosRedondeos;
   }

   public List<ConceptosRedondeos> getFiltradoLovConceptosRedondeos() {
      return filtradoLovConceptosRedondeos;
   }

   public void setFiltradoLovConceptosRedondeos(List<ConceptosRedondeos> filtradoLovConceptosRedondeos) {
      this.filtradoLovConceptosRedondeos = filtradoLovConceptosRedondeos;
   }

   public ConceptosRedondeos getConceptoRedondeoLovSeleccionado() {
      return conceptoRedondeoLovSeleccionado;
   }

   public void setConceptoRedondeoLovSeleccionado(ConceptosRedondeos conceptoRedondeoLovSeleccionado) {
      this.conceptoRedondeoLovSeleccionado = conceptoRedondeoLovSeleccionado;
   }

   public List<ConceptosRedondeos> getFiltradoListaConceptosRedondeos() {
      return filtradoListaConceptosRedondeos;
   }

   public void setFiltrarConceptosRedondeos(List<ConceptosRedondeos> filtradoListaConceptosRedondeos) {
      this.filtradoListaConceptosRedondeos = filtradoListaConceptosRedondeos;
   }

   public ConceptosRedondeos getNuevoConceptoRedondeo() {
      return nuevoConceptoRedondeo;
   }

   public void setNuevoConceptoRedondeo(ConceptosRedondeos nuevoConceptoRedondeo) {
      this.nuevoConceptoRedondeo = nuevoConceptoRedondeo;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public ConceptosRedondeos getEditarConceptosRedondeos() {
      return editarConceptosRedondeos;
   }

   public void setEditarConceptosRedondeos(ConceptosRedondeos editarConceptoRedondeo) {
      this.editarConceptosRedondeos = editarConceptoRedondeo;
   }

   public ConceptosRedondeos getDuplicarConceptoRedondeo() {
      return duplicarConceptoRedondeo;
   }

   public void setDuplicarConceptoRedondeo(ConceptosRedondeos duplicarConceptoRedondeo) {
      this.duplicarConceptoRedondeo = duplicarConceptoRedondeo;
   }

   public List<Conceptos> getLovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = administrarConceptosRedondeos.lovConceptos();
      }
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptos) {
      this.lovConceptos = lovConceptos;
   }

   public List<Conceptos> getFiltradoLovConceptos() {
      return filtradoLovConceptos;
   }

   public void setFiltradoLovConceptos(List<Conceptos> filtradoLovConceptos) {
      this.filtradoLovConceptos = filtradoLovConceptos;
   }

   public Conceptos getConceptoLovSeleccionado() {
      return conceptoLovSeleccionado;
   }

   public void setConceptoLovSeleccionado(Conceptos conceptoLovSeleccionado) {
      this.conceptoLovSeleccionado = conceptoLovSeleccionado;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public String getAltoScrollConceptosRedondeos() {
      return altoScrollConceptosRedondeos;
   }

   public void setAltoScrollConceptosRedondeos(String altoScrollConceptosRedondeos) {
      this.altoScrollConceptosRedondeos = altoScrollConceptosRedondeos;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public List<TiposRedondeos> getLovTiposRedondeos() {
      if (lovTiposRedondeos == null) {
         lovTiposRedondeos = administrarConceptosRedondeos.lovTiposRedondeos();
      }
      return lovTiposRedondeos;
   }

   public void setLovTiposRedondeos(List<TiposRedondeos> lovTiposRedondeos) {
      this.lovTiposRedondeos = lovTiposRedondeos;
   }

   public List<TiposRedondeos> getFiltradoLovTiposRedondeos() {
      return filtradoLovTiposRedondeos;
   }

   public void setFiltradoLovTiposRedondeos(List<TiposRedondeos> filtradoLovTiposRedondeos) {
      this.filtradoLovTiposRedondeos = filtradoLovTiposRedondeos;
   }

   public TiposRedondeos getTipoRedondeoLovSeleccionado() {
      return tipoRedondeoLovSeleccionado;
   }

   public void setTipoRedondeoLovSeleccionado(TiposRedondeos tipoRedondeoLovSeleccionado) {
      this.tipoRedondeoLovSeleccionado = tipoRedondeoLovSeleccionado;
   }

   public ConceptosRedondeos getConceptoRedondeoSeleccionado() {
      return conceptoRedondeoSeleccionado;
   }

   public void setConceptoRedondeoSeleccionado(ConceptosRedondeos conceptoRedondeoSeleccionado) {
      this.conceptoRedondeoSeleccionado = conceptoRedondeoSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosConceptosRedondeos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroLov() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVConceptosRedondeos");
      infoRegistroLov = String.valueOf(tabla.getRowCount());
      return infoRegistroLov;
   }

   public void setInfoRegistroLov(String infoRegistroLov) {
      this.infoRegistroLov = infoRegistroLov;
   }

   public String getInfoRegistroLovConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVConceptos");
      infoRegistroLovConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroLovConceptos;
   }

   public void setInfoRegistroLovConceptos(String infoRegistroLovConceptos) {
      this.infoRegistroLovConceptos = infoRegistroLovConceptos;
   }

   public String getInfoRegistroLovTipos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVTiposRedondeos");
      infoRegistroLovTipos = String.valueOf(tabla.getRowCount());
      return infoRegistroLovTipos;
   }

   public void setInfoRegistroLovTipos(String infoRegistroLovTipos) {
      this.infoRegistroLovTipos = infoRegistroLovTipos;
   }

}
