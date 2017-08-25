/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Cargos;
import Entidades.Empleados;
import Entidades.Encargaturas;
import Entidades.Estructuras;
import Entidades.MotivosReemplazos;
import Entidades.Personas;
import Entidades.TiposReemplazos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesReemplazosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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

@ManagedBean
@SessionScoped
public class ControlNovedadesReemplazos implements Serializable {

   private static Logger log = Logger.getLogger(ControlNovedadesReemplazos.class);

   @EJB
   AdministrarNovedadesReemplazosInterface administrarNovedadesReemplazos; // Encargaturas
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //SECUENCIA DE LA PERSONA
   private BigInteger secuenciaEmpleado;
   private Empleados empleado;
   //LISTA ENCARGATURAS
   private List<Encargaturas> listaEncargaturas;
   private List<Encargaturas> filtradosListaEncargaturas;
   private Encargaturas encargaturaSeleccionada;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   private boolean permitirIndex;
   //Consultas sobre detalles
   private Empleados empleadoParametro;
   //editar celda
   private Encargaturas editarEncargaturas;
   private boolean cambioEditor, aceptarEditar;
   private int cualCelda, tipoLista;
   //L.O.V TIPOSREEMPLAZOS
   private List<TiposReemplazos> lovTiposReemplazos;
   private List<TiposReemplazos> lovTiposReemplazosFiltrar;
   private TiposReemplazos seleccionTiposReemplazos;
   //L.O.V MOTIVOSREEMPLAZOS
   private List<MotivosReemplazos> lovMotivosReemplazos;
   private List<MotivosReemplazos> lovMotivosReemplazosFiltrar;
   private MotivosReemplazos seleccionMotivosReemplazos;
   //L.O.V ESTRUCTURAS
   private List<Estructuras> lovEstructuras;
   private List<Estructuras> lovEstructurasFiltrar;
   private Estructuras seleccionEstructuras;
   //L.O.V CARGOS
   private List<Cargos> lovCargos;
   private List<Cargos> lovCargosFiltrar;
   private Cargos seleccionCargos;
   //BUSCAR EMPLEADOS
   private List<Empleados> lovBuscarEmpleados;
   private List<Empleados> lovBuscarEmpleadosFiltrar;
   private Empleados seleccionBuscarEmpleados;
   //L.O.V EMPLEADOS
   private List<Empleados> lovEmpleados;
   private List<Empleados> lovEmpleadosFiltrar;
   private Empleados seleccionEmpleados;
   //Seleccion Mostrar Todos
   private Empleados seleccionMostrar;
   //RASTROS
   //LISTA QUE NO ES LISTA - 1 SOLO ELEMENTO
   private List<Empleados> listaEmpleadosNovedad;
   private List<Empleados> filtradosListaEmpleadosNovedad;
   //Crear Encargaturas
   private List<Encargaturas> listaEncargaturasCrear;
   public Encargaturas nuevaEncargatura;
   private int k;
   private BigInteger l;
   private String mensajeValidacion;
   //Modificar Encargaturas
   private List<Encargaturas> listaEncargaturasModificar;
   private boolean guardado, guardarOk;
   //Borrar Encargaturas
   private List<Encargaturas> listaEncargaturasBorrar;
   //Cual Tabla
   private int CualTabla;
   //Boolean deshabilitar botones en caso de que no haya un parametro
   private boolean botones, activarLov;
   private String tipoPantalla;
   //Duplicar
   private Encargaturas duplicarEncargatura;
   //AUTOCOMPLETAR
   private String Reemplazado, TipoReemplazo, MotivoReemplazo, Cargo, Estructura;
   //Columnas Tabla Encargaturas
   private Column nREmpleadoReemplazado, nRTiposReemplazos, nRFechasPagos, nRFechasIniciales, nRFechasFinales, nRCargos, nRMotivosReemplazos, nREstructuras;
   private String altoTabla;
   private String infoRegistro, infoRegistroTipoReemplazo, infoRegistroMotivosReemplazos, infoRegistroEstructuras, infoRegistroCargos, infoRegistroEmpleados, infoRegistroEmpleadosNovedad, infoRegistroBuscarEmpleados;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlNovedadesReemplazos() {
      permitirIndex = true;
      aceptar = true;
      empleadoParametro = new Empleados();
      listaEncargaturas = null;
      lovTiposReemplazos = null;
      lovMotivosReemplazos = null;
      lovEstructuras = null;
      lovEmpleados = null;
      lovBuscarEmpleados = null;
      listaEncargaturasBorrar = new ArrayList<Encargaturas>();
      listaEncargaturasCrear = new ArrayList<Encargaturas>();
      listaEncargaturasModificar = new ArrayList<Encargaturas>();
      listaEmpleadosNovedad = null;
      encargaturaSeleccionada = null;
      guardado = true;
      tipoLista = 0;
      //Crear VC
      nuevaEncargatura = new Encargaturas();
      nuevaEncargatura.setEmpleado(new Empleados());
      nuevaEncargatura.setReemplazado(new Empleados());
      nuevaEncargatura.setTiporeemplazo(new TiposReemplazos());
      nuevaEncargatura.setCargo(new Cargos());
      nuevaEncargatura.setMotivoreemplazo(new MotivosReemplazos());
      nuevaEncargatura.setEstructura(new Estructuras());
      altoTabla = "155";
      activarLov = true;
      lovCargos = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      botones = false;
      tipoPantalla = "TODOS";
      lovEmpleados = null;
      cargarLovEmpleados();
      listaEmpleadosNovedad = lovEmpleados;
      if (listaEmpleadosNovedad != null) {
         if (!listaEmpleadosNovedad.isEmpty()) {
            seleccionMostrar = listaEmpleadosNovedad.get(0);
            listaEncargaturas = null;
            getListaEncargaturas();
         }
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      botones = false;
      tipoPantalla = "TODOS";
      lovEmpleados = null;
      cargarLovEmpleados();
      listaEmpleadosNovedad = lovEmpleados;
      if (listaEmpleadosNovedad != null) {
         if (!listaEmpleadosNovedad.isEmpty()) {
            seleccionMostrar = listaEmpleadosNovedad.get(0);
            listaEncargaturas = null;
            getListaEncargaturas();
         }
      }
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "novedadesreemplazos";
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
      lovBuscarEmpleados = null;
      lovCargos = null;
      lovEmpleados = null;
      lovEstructuras = null;
      lovMotivosReemplazos = null;
      lovTiposReemplazos = null;
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
         administrarNovedadesReemplazos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger secEmpleado, String pagina) { // se usa para ingresar desde empleadoindividual
      paginaAnterior = pagina;
      tipoPantalla = "UNO";
      botones = true;
      listaEmpleadosNovedad = null;
      secuenciaEmpleado = secEmpleado;
      empleado = null;
      getEmpleado();
      getListaEmpleadosNovedad();
      if (listaEmpleadosNovedad != null) {
         if (!listaEmpleadosNovedad.isEmpty()) {
            seleccionMostrar = listaEmpleadosNovedad.get(0);
            listaEncargaturas = null;
            getListaEncargaturas();
         }
      }

      aceptar = true;
   }

   public String volveratras() {
      return paginaAnterior;
   }

   //AUTOCOMPLETAR
   public void modificarEncargaturas(Encargaturas encargatura) {
      encargaturaSeleccionada = encargatura;
      if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
         if (listaEncargaturasModificar.isEmpty()) {
            listaEncargaturasModificar.add(encargaturaSeleccionada);
         } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
            listaEncargaturasModificar.add(encargaturaSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
   }

   //Ubicacion Celda Arriba 
   public void cambiarEmpleado() {
      secuenciaEmpleado = seleccionMostrar.getSecuencia();
      listaEncargaturas = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
      contarRegistros();
   }

   public void cambiarIndice(Encargaturas encargatura, int celda) {
      if (permitirIndex == true) {

         encargaturaSeleccionada = encargatura;
         cualCelda = celda;
         encargaturaSeleccionada.getSecuencia();
         if (cualCelda == 0) {
            habilitarBotonLov();
            Reemplazado = encargaturaSeleccionada.getReemplazado().getNombreCompleto();
         } else if (cualCelda == 1) {
            habilitarBotonLov();
            TipoReemplazo = encargaturaSeleccionada.getTiporeemplazo().getNombre();
         } else if (cualCelda == 2) {
            deshabilitarBotonLov();
            encargaturaSeleccionada.getFechapago();
         } else if (cualCelda == 3) {
            deshabilitarBotonLov();
            encargaturaSeleccionada.getFechainicial();
         } else if (cualCelda == 4) {
            deshabilitarBotonLov();
            encargaturaSeleccionada.getFechafinal();
         } else if (cualCelda == 5) {
            habilitarBotonLov();
            Cargo = encargaturaSeleccionada.getCargo().getNombre();
         } else if (cualCelda == 6) {
            habilitarBotonLov();
            MotivoReemplazo = encargaturaSeleccionada.getMotivoreemplazo().getNombre();
         } else if (cualCelda == 7) {
            habilitarBotonLov();
            Estructura = encargaturaSeleccionada.getEstructura().getNombre();
         }
      }
   }

   public void asignarIndex(Encargaturas encargatura, int dlg, int LND) {
      encargaturaSeleccionada = encargatura;
      RequestContext context = RequestContext.getCurrentInstance();
      tipoActualizacion = LND;
      if (dlg == 0) {
         cargarLovBuscarEmpleados();
         contarRegistrosBuscarEmpleados();
         RequestContext.getCurrentInstance().update("formularioDialogos:buscarEmpleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('buscarEmpleadosDialogo').show()");
      } else if (dlg == 1) {
         contarRegistrosTiposReemplazos();
         RequestContext.getCurrentInstance().update("formularioDialogos:tiposReemplazosDialogo");
         RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').show()");
      } else if (dlg == 2) {
         contarRegistrosMotivosReemplazos();
         RequestContext.getCurrentInstance().update("formularioDialogos:motivosReemplazosDialogo");
         RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').show()");
      } else if (dlg == 3) {
         cargarLovCargos();
         contarRegistrosCargos();
         RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
         RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
      } else if (dlg == 4) {
         cargarLovEstructuras();
         contarRegistrosEstructuras();
         RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
         RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
      } else if (dlg == 5) {
         cargarLovEmpleados();
         contarRegistrosEmpleados();
         RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
         RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
      }
   }

   public void mostrarTodos() {

      RequestContext context = RequestContext.getCurrentInstance();
      if (!listaEmpleadosNovedad.isEmpty()) {
         listaEmpleadosNovedad.clear();
      }
      if (listaEmpleadosNovedad != null) {
         for (int i = 0; i < lovBuscarEmpleados.size(); i++) {
            listaEmpleadosNovedad.add(lovBuscarEmpleados.get(i));
         }
      }
      if (!listaEmpleadosNovedad.isEmpty()) {
         seleccionMostrar = listaEmpleadosNovedad.get(0);
         listaEncargaturas = null;
         getListaEncargaturas();
      }
      log.info("seleccion mostrar en mostrar Todos() : " + seleccionMostrar.getSecuencia());
      listaEncargaturas = null;
      lovEmpleados = null;
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
      seleccionBuscarEmpleados = null;
      filtradosListaEmpleadosNovedad = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;

   }

   public void actualizarBuscarEmpleado() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listaEmpleadosNovedad.isEmpty()) {
         listaEmpleadosNovedad.clear();
         listaEmpleadosNovedad.add(seleccionBuscarEmpleados);
         seleccionMostrar = listaEmpleadosNovedad.get(0);
         listaEncargaturas = null;
         getListaEncargaturas();
         if (listaEncargaturas != null) {
            if (!listaEncargaturas.isEmpty()) {
               encargaturaSeleccionada = listaEncargaturas.get(0);
            }
         }
      }
      log.info("seleccion mostrar en actualizarBuscarEmpleado() : " + seleccionMostrar.getSecuencia());
      RequestContext.getCurrentInstance().update("form:datosEmpleados");
      RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
      listaEncargaturas = null;
      lovEmpleados = null;
      lovBuscarEmpleadosFiltrar = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      seleccionBuscarEmpleados = null;
      RequestContext.getCurrentInstance().update("formularioDialogos:buscarEmpleadosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVBuscarEmpleados");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarBE");
      context.reset("formularioDialogos:LOVBuscarEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVBuscarEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('buscarEmpleadosDialogo').hide()");

   }

   public void cancelarCambioBuscarEmpleado() {
      lovBuscarEmpleadosFiltrar = null;
      seleccionBuscarEmpleados = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:buscarEmpleadosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVBuscarEmpleados");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarBE");
      context.reset("formularioDialogos:LOVBuscarEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVBuscarEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('buscarEmpleadosDialogo').hide()");
   }

   public void actualizarEmpleados() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         log.info("Tipo Lista: " + tipoLista);
         encargaturaSeleccionada.setReemplazado(seleccionEmpleados);
         if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
            if (listaEncargaturasModificar.isEmpty()) {
               listaEncargaturasModificar.add(encargaturaSeleccionada);
            } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
               listaEncargaturasModificar.add(encargaturaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         for (int i = 0; i < listaEncargaturas.size(); i++) {
            log.info("En la lista encargaturas está:" + listaEncargaturas.get(i).getReemplazado().getNombreCompleto());
            log.info("Seleccionado: " + seleccionEmpleados.getNombreCompleto());
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaEncargatura.setReemplazado(seleccionEmpleados);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEncargatura");
      } else if (tipoActualizacion == 2) {
         duplicarEncargatura.setReemplazado(seleccionEmpleados);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");
      }
      lovEmpleadosFiltrar = null;
      seleccionEmpleados = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;

      RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
      context.reset("formularioDialogos:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
   }

   public void actualizarTiposReemplazos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         encargaturaSeleccionada.setTiporeemplazo(seleccionTiposReemplazos);
         if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
            if (listaEncargaturasModificar.isEmpty()) {
               listaEncargaturasModificar.add(encargaturaSeleccionada);
            } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
               listaEncargaturasModificar.add(encargaturaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaEncargatura.setTiporeemplazo(seleccionTiposReemplazos);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEncargatura");
      } else if (tipoActualizacion == 2) {
         duplicarEncargatura.setTiporeemplazo(seleccionTiposReemplazos);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");

      }
      lovTiposReemplazosFiltrar = null;
      seleccionTiposReemplazos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;

      RequestContext.getCurrentInstance().update("formularioDialogos:tiposReemplazosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposReemplazos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTR");
      context.reset("formularioDialogos:LOVTiposReemplazos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTiposReemplazos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').hide()");
   }

   public void actualizarCargos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         log.info("Tipo Lista: " + tipoLista);
         encargaturaSeleccionada.setCargo(seleccionCargos);
         if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
            if (listaEncargaturasModificar.isEmpty()) {
               listaEncargaturasModificar.add(encargaturaSeleccionada);
            } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
               listaEncargaturasModificar.add(encargaturaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaEncargatura.setCargo(seleccionCargos);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEncargatura");
      } else if (tipoActualizacion == 2) {
         duplicarEncargatura.setCargo(seleccionCargos);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");
      }
      lovCargosFiltrar = null;
      seleccionCargos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVCargos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
      context.reset("formularioDialogos:LOVCargos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVCargos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
   }

   public void actualizarMotivosReemplazos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         encargaturaSeleccionada.setMotivoreemplazo(seleccionMotivosReemplazos);
         if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
            if (listaEncargaturasModificar.isEmpty()) {
               listaEncargaturasModificar.add(encargaturaSeleccionada);
            } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
               listaEncargaturasModificar.add(encargaturaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaEncargatura.setMotivoreemplazo(seleccionMotivosReemplazos);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEncargatura");
      } else if (tipoActualizacion == 2) {
         duplicarEncargatura.setMotivoreemplazo(seleccionMotivosReemplazos);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");

      }
      lovTiposReemplazosFiltrar = null;
      seleccionTiposReemplazos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:motivosReemplazosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVMotivosReemplazos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarMR");
      context.reset("formularioDialogos:LOVMotivosReemplazos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVMotivosReemplazos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').hide()");
   }

   public void actualizarEstructuras() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         encargaturaSeleccionada.setEstructura(seleccionEstructuras);
         if (!listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
            if (listaEncargaturasModificar.isEmpty()) {
               listaEncargaturasModificar.add(encargaturaSeleccionada);
            } else if (!listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
               listaEncargaturasModificar.add(encargaturaSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
      } else if (tipoActualizacion == 1) {
         nuevaEncargatura.setEstructura(seleccionEstructuras);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEncargatura");
      } else if (tipoActualizacion == 2) {
         duplicarEncargatura.setEstructura(seleccionEstructuras);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");

      }
      lovEstructurasFiltrar = null;
      seleccionEstructuras = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVEstructuras");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarEST");
      context.reset("formularioDialogos:LOVEstructuras:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEstructuras').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').hide()");
   }

   public void cancelarCambioEstructuras() {
      lovEstructurasFiltrar = null;
      seleccionEstructuras = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVEstructuras");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarEST");
      context.reset("formularioDialogos:LOVEstructuras:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEstructuras').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').hide()");
   }

   public void cancelarCambioEmpleados() {
      lovEmpleadosFiltrar = null;
      seleccionEmpleados = null;
      seleccionEmpleados = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
      context.reset("formularioDialogos:LOVEmpleados:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
   }

   public void cancelarCambioCargos() {
      lovCargosFiltrar = null;
      seleccionCargos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVCargos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
      context.reset("formularioDialogos:LOVCargos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVCargos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('cargosDialogo').hide()");
   }

   public void cancelarCambioMotivosReemplazos() {
      lovMotivosReemplazosFiltrar = null;
      seleccionMotivosReemplazos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:motivosReemplazosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVMotivosReemplazos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarMR");
      context.reset("formularioDialogos:LOVMotivosReemplazos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVMotivosReemplazos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').hide()");
   }

   public void cancelarCambioTiposReemplazos() {
      lovTiposReemplazosFiltrar = null;
      seleccionTiposReemplazos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:tiposReemplazosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVTiposReemplazos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTR");
      context.reset("formularioDialogos:LOVTiposReemplazos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVTiposReemplazos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').hide()");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void activarCtrlF11() {
      log.info("TipoLista= " + tipoLista);
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         log.info("Activar");
         log.info("TipoLista= " + tipoLista);
         nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
         nREmpleadoReemplazado.setFilterStyle("width: 85% !important;");
         nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
         nRTiposReemplazos.setFilterStyle("85%");
         nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
         nRFechasPagos.setFilterStyle("width: 85% !important;");
         nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
         nRFechasIniciales.setFilterStyle("width: 85% !important;");
         nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
         nRFechasFinales.setFilterStyle("width: 85% !important;");
         nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
         nRCargos.setFilterStyle("width: 85% !important;");
         nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
         nRMotivosReemplazos.setFilterStyle("width: 85% !important;");
         nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
         nREstructuras.setFilterStyle("width: 85% !important;");
         altoTabla = "135";
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         log.info("TipoLista= " + tipoLista);
         nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
         nREmpleadoReemplazado.setFilterStyle("display: none; visibility: hidden;");
         nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
         nRTiposReemplazos.setFilterStyle("display: none; visibility: hidden;");
         nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
         nRFechasPagos.setFilterStyle("display: none; visibility: hidden;");
         nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
         nRFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
         nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
         nRFechasFinales.setFilterStyle("display: none; visibility: hidden;");
         nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
         nRCargos.setFilterStyle("display: none; visibility: hidden;");
         nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
         nRMotivosReemplazos.setFilterStyle("display: none; visibility: hidden;");
         nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
         nREstructuras.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "155";
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
         bandera = 0;
         filtradosListaEncargaturas = null;
         tipoLista = 0;
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEncargaturasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "EncargaturasPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEncargaturasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "EncargaturasXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //CREAR ENCARGATURAS
   public void agregarNuevaNovedadReemplazo() {
      int pasa = 0;
      int pasa2 = 0;
      mensajeValidacion = new String();
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevaEncargatura.getTiporeemplazo().getNombre() == null || nuevaEncargatura.getTiporeemplazo().getNombre().isEmpty()) {
         log.info("Entro a Tipo Reemplazo");
         mensajeValidacion = mensajeValidacion + " * TipoReemplazo \n";
         pasa++;
      }
      if (nuevaEncargatura.getFechainicial() == null) {
         log.info("Entro a Fecha Inicial");
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (nuevaEncargatura.getFechapago() == null) {
         log.info("Entro a FechaPago");
         mensajeValidacion = mensajeValidacion + " * Fecha Pago\n";
         pasa++;
      }
      if (nuevaEncargatura.getFechafinal() == null) {
         log.info("Entro a FechaFinal");
         mensajeValidacion = mensajeValidacion + " * Fecha Final\n";
         pasa++;
      }

      log.info("Valor Pasa: " + pasa);
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadReemplazo");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadReemplazo').show()");
      }

      if ((nuevaEncargatura.getReemplazado().getNombreCompleto() != null && !nuevaEncargatura.getReemplazado().getNombreCompleto().equals(" "))
              && (nuevaEncargatura.getCargo().getNombre() != null && !nuevaEncargatura.getCargo().getNombre().equals(""))) {
         log.info("Entro a Inconsistencia");
         RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
         RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
         pasa2++;
      }
      if (pasa == 0 && pasa2 == 0) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            log.info("Desactivar");
            log.info("TipoLista= " + tipoLista);
            nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
            nREmpleadoReemplazado.setFilterStyle("display: none; visibility: hidden;");
            nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
            nRTiposReemplazos.setFilterStyle("display: none; visibility: hidden;");
            nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
            nRFechasPagos.setFilterStyle("display: none; visibility: hidden;");
            nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
            nRFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
            nRFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
            nRCargos.setFilterStyle("display: none; visibility: hidden;");
            nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
            nRMotivosReemplazos.setFilterStyle("display: none; visibility: hidden;");
            nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
            nREstructuras.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "155";
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            bandera = 0;
            filtradosListaEncargaturas = null;
            tipoLista = 0;

         }
         //REGISTRO A LA LISTA ENCARGATURA .
         k++;
         l = BigInteger.valueOf(k);
         nuevaEncargatura.setSecuencia(l);
         nuevaEncargatura.setEmpleado(seleccionMostrar); //Envia empleado
         listaEncargaturasCrear.add(nuevaEncargatura);
         listaEncargaturas.add(nuevaEncargatura);
         encargaturaSeleccionada = nuevaEncargatura;
         contarRegistros();
         nuevaEncargatura = new Encargaturas();
         nuevaEncargatura.setEmpleado(new Empleados());
         nuevaEncargatura.setReemplazado(new Empleados());
         nuevaEncargatura.setTiporeemplazo(new TiposReemplazos());
         nuevaEncargatura.setCargo(new Cargos());
         nuevaEncargatura.setMotivoreemplazo(new MotivosReemplazos());
         nuevaEncargatura.setEstructura(new Estructuras());
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevaNovedadReemplazo').hide()");
      } else {

      }
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listaEncargaturas.isEmpty()) {
         if (encargaturaSeleccionada != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(encargaturaSeleccionada.getSecuencia(), "ENCARGATURAS");
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
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("ENCARGATURAS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //LIMPIAR NUEVO REGISTRO ENCARGATURA
   public void limpiarNuevaEncargatura() {
      nuevaEncargatura = new Encargaturas();
      nuevaEncargatura.setReemplazado(new Empleados());
      nuevaEncargatura.setEmpleado(new Empleados());
      nuevaEncargatura.setTiporeemplazo(new TiposReemplazos());
      nuevaEncargatura.setCargo(new Cargos());
      nuevaEncargatura.setMotivoreemplazo(new MotivosReemplazos());
      nuevaEncargatura.setEstructura(new Estructuras());
   }

   //GUARDAR
   public void guardarCambiosEncargaturas() {
      try {
         if (guardado == false) {
            log.info("Realizando Operaciones Encargaturas");
            if (!listaEncargaturasBorrar.isEmpty()) {
               for (int i = 0; i < listaEncargaturasBorrar.size(); i++) {
                  log.info("Borrando...");
                  if (listaEncargaturasBorrar.get(i).getCargo().getSecuencia() == null) {
                     listaEncargaturasBorrar.get(i).setCargo(null);
                  }
                  if (listaEncargaturasBorrar.get(i).getEstructura().getSecuencia() == null) {
                     listaEncargaturasBorrar.get(i).setEstructura(null);
                  }
                  if (listaEncargaturasBorrar.get(i).getMotivoreemplazo().getSecuencia() == null) {
                     listaEncargaturasBorrar.get(i).setMotivoreemplazo(null);
                  }
                  if (listaEncargaturasBorrar.get(i).getReemplazado().getSecuencia() == null) {
                     listaEncargaturasBorrar.get(i).setReemplazado(null);
                  }
//                        
//                        if(listaEncargaturasBorrar.get(i).getReemplazado().getPersona().getNombreCompleto() == null){
//                            listaEncargaturasBorrar.get(i).getReemplazado().setPersona(new Personas());
//                            listaEncargaturasBorrar.get(i).getReemplazado().getPersona().setNombreCompleto("");
//                        }
                  administrarNovedadesReemplazos.borrarEncargaturas(listaEncargaturasBorrar.get(i));
               }
               log.info("Entra");
               listaEncargaturasBorrar.clear();
            }

            if (!listaEncargaturasCrear.isEmpty()) {
               for (int i = 0; i < listaEncargaturasCrear.size(); i++) {
                  log.info("Creando...");

                  if (listaEncargaturasCrear.get(i).getMotivoreemplazo().getSecuencia() == null) {
                     listaEncargaturasCrear.get(i).setMotivoreemplazo(null);
                  }
                  if (listaEncargaturasCrear.get(i).getReemplazado().getSecuencia() == null) {
                     listaEncargaturasCrear.get(i).setReemplazado(null);
                  }
                  if (listaEncargaturasCrear.get(i).getCargo().getSecuencia() == null) {
                     listaEncargaturasCrear.get(i).setCargo(null);
                  }
                  if (listaEncargaturasCrear.get(i).getEstructura().getSecuencia() == null) {
                     listaEncargaturasCrear.get(i).setEstructura(null);
                  }
                  administrarNovedadesReemplazos.crearEncargaturas(listaEncargaturasCrear.get(i));

               }
               log.info("LimpiaLista");
               listaEncargaturasCrear.clear();
            }
            if (!listaEncargaturasModificar.isEmpty()) {
               administrarNovedadesReemplazos.modificarEncargatura(listaEncargaturasModificar);
               listaEncargaturasModificar.clear();
            }
            log.info("Se guardaron los datos con exito");
            listaEncargaturas = null;
            encargaturaSeleccionada = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         log.warn("Error en guardarCambisoEncargaturas : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Error en el guardaro, Intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //BORRAR ENCARGATURAS
   public void borrarEncargaturas() {

      if (encargaturaSeleccionada != null) {
         if (!listaEncargaturasModificar.isEmpty() && listaEncargaturasModificar.contains(encargaturaSeleccionada)) {
            int modIndex = listaEncargaturasModificar.indexOf(encargaturaSeleccionada);
            listaEncargaturasModificar.remove(modIndex);
            listaEncargaturasBorrar.add(encargaturaSeleccionada);
         } else if (!listaEncargaturasCrear.isEmpty() && listaEncargaturasCrear.contains(encargaturaSeleccionada)) {
            int crearIndex = listaEncargaturasCrear.indexOf(encargaturaSeleccionada);
            listaEncargaturasCrear.remove(crearIndex);
         } else {
            listaEncargaturasBorrar.add(encargaturaSeleccionada);
         }
         listaEncargaturas.remove(encargaturaSeleccionada);

         if (tipoLista == 1) {
            filtradosListaEncargaturas.remove(encargaturaSeleccionada);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
         encargaturaSeleccionada = null;
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("REEMPLAZADO")) {
         if (tipoNuevo == 1) {
            Reemplazado = nuevaEncargatura.getReemplazado().getNombreCompleto();
         } else if (tipoNuevo == 2) {
            Reemplazado = duplicarEncargatura.getReemplazado().getNombreCompleto();
         }
      } else if (Campo.equals("TIPOREEMPLAZO")) {
         if (tipoNuevo == 1) {
            TipoReemplazo = nuevaEncargatura.getTiporeemplazo().getNombre();
         } else if (tipoNuevo == 2) {
            TipoReemplazo = duplicarEncargatura.getTiporeemplazo().getNombre();
         }
      } else if (Campo.equals("CARGO")) {
         if (tipoNuevo == 1) {
            Cargo = nuevaEncargatura.getCargo().getNombre();
         } else if (tipoNuevo == 2) {
            Cargo = duplicarEncargatura.getCargo().getNombre();
         }
      } else if (Campo.equals("MOTIVOREEMPLAZO")) {
         if (tipoNuevo == 1) {
            MotivoReemplazo = nuevaEncargatura.getMotivoreemplazo().getNombre();
         } else if (tipoNuevo == 2) {
            MotivoReemplazo = duplicarEncargatura.getMotivoreemplazo().getNombre();
         }
      } else if (Campo.equals("ESTRUCTURA")) {
         if (tipoNuevo == 1) {
            Estructura = nuevaEncargatura.getEstructura().getNombre();
         } else if (tipoNuevo == 2) {
            Estructura = duplicarEncargatura.getEstructura().getNombre();
         }
      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("REEMPLAZADO")) {
         if (tipoNuevo == 1) {
            nuevaEncargatura.getReemplazado().setNombreCompleto(Reemplazado);
         } else if (tipoNuevo == 2) {
            duplicarEncargatura.getReemplazado().setNombreCompleto(Reemplazado);
         }
         for (int i = 0; i < lovEmpleados.size(); i++) {
            if (lovEmpleados.get(i).getNombreCompleto().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaEncargatura.setReemplazado(lovEmpleados.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoReemplazado");
            } else if (tipoNuevo == 2) {
               duplicarEncargatura.setReemplazado(lovEmpleados.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarReemplazado");
            }
            lovEmpleados.clear();
            getLovEmpleados();
         } else {
            RequestContext.getCurrentInstance().update("form:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoReemplazado");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarReemplazado");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("TIPOREEMPLAZO")) {
         if (tipoNuevo == 1) {
            nuevaEncargatura.getTiporeemplazo().setNombre(TipoReemplazo);
         } else if (tipoNuevo == 2) {
            duplicarEncargatura.getTiporeemplazo().setNombre(TipoReemplazo);
         }

         for (int i = 0; i < lovTiposReemplazos.size(); i++) {
            if (lovTiposReemplazos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaEncargatura.setTiporeemplazo(lovTiposReemplazos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoReemplazo");
            } else if (tipoNuevo == 2) {
               duplicarEncargatura.setTiporeemplazo(lovTiposReemplazos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoReemplazo");
            }
            lovTiposReemplazos.clear();
            getLovTiposReemplazos();
         } else {
            RequestContext.getCurrentInstance().update("form:tiposReemplazosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoReemplazo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicartipoReemplazo");
            }
         }
      } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
         if (tipoNuevo == 1) {
            nuevaEncargatura.getCargo().setNombre(Cargo);
         } else if (tipoNuevo == 2) {
            duplicarEncargatura.getCargo().setNombre(Cargo);
         }

         for (int i = 0; i < lovCargos.size(); i++) {
            if (lovCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaEncargatura.setCargo(lovCargos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
            } else if (tipoNuevo == 2) {
               duplicarEncargatura.setCargo(lovCargos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
            }
            lovCargos.clear();
            getLovCargos();
         } else {
            RequestContext.getCurrentInstance().update("form:cargosDialogo");
            RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
            }
         }

      } else if (confirmarCambio.equalsIgnoreCase("MOTIVOREEMPLAZO")) {
         if (tipoNuevo == 1) {
            nuevaEncargatura.getMotivoreemplazo().setNombre(MotivoReemplazo);
         } else if (tipoNuevo == 2) {
            duplicarEncargatura.getMotivoreemplazo().setNombre(MotivoReemplazo);
         }

         for (int i = 0; i < lovMotivosReemplazos.size(); i++) {
            if (lovMotivosReemplazos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaEncargatura.setMotivoreemplazo(lovMotivosReemplazos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoMotivoReemplazo");
            } else if (tipoNuevo == 2) {
               duplicarEncargatura.setMotivoreemplazo(lovMotivosReemplazos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoReemplazo");
            }
            lovMotivosReemplazos.clear();
            getLovMotivosReemplazos();
         } else {
            RequestContext.getCurrentInstance().update("form:motivosReemplazosDialogo");
            RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoMotivoReemplazo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoReemplazo");
            }
         }

      } else if (confirmarCambio.equalsIgnoreCase("ESTRUCTURA")) {
         if (tipoNuevo == 1) {
            nuevaEncargatura.getEstructura().setNombre(Estructura);
         } else if (tipoNuevo == 2) {
            duplicarEncargatura.getEstructura().setNombre(Estructura);
         }

         for (int i = 0; i < lovEstructuras.size(); i++) {
            if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevaEncargatura.setEstructura(lovEstructuras.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstructura");
            } else if (tipoNuevo == 2) {
               duplicarEncargatura.setEstructura(lovEstructuras.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructura");
            }
            lovEstructuras.clear();
            getLovEstructuras();
         } else {
            RequestContext.getCurrentInstance().update("form:estructurasDialogo");
            RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstructura");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructura");
            }
         }

      }
   }

   //CANCELAR MODIFICACIONES
   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();

         nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
         nREmpleadoReemplazado.setFilterStyle("display: none; visibility: hidden;");
         nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
         nRTiposReemplazos.setFilterStyle("display: none; visibility: hidden;");
         nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
         nRFechasPagos.setFilterStyle("display: none; visibility: hidden;");
         nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
         nRFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
         nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
         nRFechasFinales.setFilterStyle("display: none; visibility: hidden;");
         nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
         nRCargos.setFilterStyle("display: none; visibility: hidden;");
         nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
         nRMotivosReemplazos.setFilterStyle("display: none; visibility: hidden;");
         nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
         nREstructuras.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "155";
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
         bandera = 0;
         filtradosListaEncargaturas = null;
         tipoLista = 0;
      }

      listaEncargaturasBorrar.clear();
      listaEncargaturasCrear.clear();
      listaEncargaturasModificar.clear();
      encargaturaSeleccionada = null;
      listaEncargaturas = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
         nREmpleadoReemplazado.setFilterStyle("display: none; visibility: hidden;");
         nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
         nRTiposReemplazos.setFilterStyle("display: none; visibility: hidden;");
         nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
         nRFechasPagos.setFilterStyle("display: none; visibility: hidden;");
         nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
         nRFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
         nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
         nRFechasFinales.setFilterStyle("display: none; visibility: hidden;");
         nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
         nRCargos.setFilterStyle("display: none; visibility: hidden;");
         nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
         nRMotivosReemplazos.setFilterStyle("display: none; visibility: hidden;");
         nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
         nREstructuras.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "155";
         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
         bandera = 0;
         filtradosListaEncargaturas = null;
         tipoLista = 0;
      }
      listaEncargaturasBorrar.clear();
      listaEncargaturasCrear.clear();
      listaEncargaturasModificar.clear();
      encargaturaSeleccionada = null;
      listaEncargaturas = null;
      lovBuscarEmpleados = null;
      lovEmpleados = null;
      guardado = true;
      permitirIndex = true;
      navegar("atras");
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (encargaturaSeleccionada != null) {
         if (tipoLista == 0) {
            editarEncargaturas = encargaturaSeleccionada;
         }
         if (tipoLista == 1) {
            editarEncargaturas = encargaturaSeleccionada;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarReemplazados");
            RequestContext.getCurrentInstance().execute("PF('editarReemplazados').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTiposReemplazos");
            RequestContext.getCurrentInstance().execute("PF('editarTiposReemplazos').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasPagos");
            RequestContext.getCurrentInstance().execute("PF('editarFechasPagos').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasIniciales");
            RequestContext.getCurrentInstance().execute("PF('editarFechasIniciales').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasFinales");
            RequestContext.getCurrentInstance().execute("PF('editarFechasFinales').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarMotivosReemplazos");
            RequestContext.getCurrentInstance().execute("PF('editarMotivosReemplazos').show()");
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEstructuras");
            RequestContext.getCurrentInstance().execute("PF('editarEstructuras').show()");
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCargos");
            RequestContext.getCurrentInstance().execute("PF('editarCargos').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //DUPLICAR ENCARGATURA
   public void duplicarNR() {
      if (encargaturaSeleccionada != null) {
         duplicarEncargatura = new Encargaturas();
         k++;
         l = BigInteger.valueOf(k);

         duplicarEncargatura.setSecuencia(l);
         duplicarEncargatura.setEmpleado(encargaturaSeleccionada.getEmpleado());
         duplicarEncargatura.setReemplazado(encargaturaSeleccionada.getReemplazado());
         duplicarEncargatura.setTiporeemplazo(encargaturaSeleccionada.getTiporeemplazo());
         duplicarEncargatura.setFechapago(encargaturaSeleccionada.getFechapago());
         duplicarEncargatura.setFechainicial(encargaturaSeleccionada.getFechainicial());
         duplicarEncargatura.setFechafinal(encargaturaSeleccionada.getFechafinal());
         duplicarEncargatura.setCargo(encargaturaSeleccionada.getCargo());
         duplicarEncargatura.setMotivoreemplazo(encargaturaSeleccionada.getMotivoreemplazo());
         duplicarEncargatura.setEstructura(encargaturaSeleccionada.getEstructura());

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEncargatura");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroEncargatura').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void cargarLovCargos() {
      if (lovCargos == null) {
         lovCargos = administrarNovedadesReemplazos.lovCargos();
      }
   }

   public void cargarLovEmpleados() {
      if (lovEmpleados == null) {
         lovEmpleados = administrarNovedadesReemplazos.lovEmpleados();
      }
   }

   public void cargarLovBuscarEmpleados() {
      if (lovBuscarEmpleados == null) {
         lovBuscarEmpleados = administrarNovedadesReemplazos.lovEmpleados();
      }
   }

   public void cargarLovEstructuras() {
      if (lovEstructuras == null) {
         lovEstructuras = administrarNovedadesReemplazos.lovEstructuras();
      }
   }

   public void confirmarDuplicar() {

      int pasa2 = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      if ((!duplicarEncargatura.getReemplazado().getNombreCompleto().equals("  ") && !duplicarEncargatura.getReemplazado().getNombreCompleto().equals(" ")) && (!duplicarEncargatura.getCargo().getNombre().equals(" ") && !duplicarEncargatura.getCargo().getNombre().equals(""))) {
         log.info("Entro a Inconsistencia");
         RequestContext.getCurrentInstance().update("formularioDialogos:inconsistencia");
         RequestContext.getCurrentInstance().execute("PF('inconsistencia').show()");
         pasa2++;
      }

      if (pasa2 == 0) {
         listaEncargaturas.add(duplicarEncargatura);
         listaEncargaturasCrear.add(duplicarEncargatura);
         encargaturaSeleccionada = duplicarEncargatura;

         RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            nREmpleadoReemplazado = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREmpleadoReemplazado");
            nREmpleadoReemplazado.setFilterStyle("display: none; visibility: hidden;");
            nRTiposReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRTiposReemplazos");
            nRTiposReemplazos.setFilterStyle("display: none; visibility: hidden;");
            nRFechasPagos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasPagos");
            nRFechasPagos.setFilterStyle("display: none; visibility: hidden;");
            nRFechasIniciales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasIniciales");
            nRFechasIniciales.setFilterStyle("display: none; visibility: hidden;");
            nRFechasFinales = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRFechasFinales");
            nRFechasFinales.setFilterStyle("display: none; visibility: hidden;");
            nRCargos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRCargos");
            nRCargos.setFilterStyle("display: none; visibility: hidden;");
            nRMotivosReemplazos = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nRMotivosReemplazos");
            nRMotivosReemplazos.setFilterStyle("display: none; visibility: hidden;");
            nREstructuras = (Column) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado:nREstructuras");
            nREstructuras.setFilterStyle("display: none; visibility: hidden;");
            altoTabla = "155";
            RequestContext.getCurrentInstance().update("form:datosEncargaturasEmpleado");
            bandera = 0;
            filtradosListaEncargaturas = null;
            tipoLista = 0;
         }
         duplicarEncargatura = new Encargaturas();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroEncargatura");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroEncargatura').hide()");
      }
   }
   //LIMPIAR DUPLICAR

   /**
    * Metodo que limpia los datos de un duplicar Encargaturas
    */
   public void limpiarduplicarEncargaturas() {
      duplicarEncargatura = new Encargaturas();
      duplicarEncargatura.setReemplazado(new Empleados());
      duplicarEncargatura.setTiporeemplazo(new TiposReemplazos());
      duplicarEncargatura.setCargo(new Cargos());
      duplicarEncargatura.setMotivoreemplazo(new MotivosReemplazos());
      duplicarEncargatura.setEstructura(new Estructuras());
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (encargaturaSeleccionada != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            cargarLovEmpleados();
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposReemplazosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposReemplazosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 5) {
            cargarLovCargos();
            RequestContext.getCurrentInstance().update("formularioDialogos:cargosDialogo");
            RequestContext.getCurrentInstance().execute("PF('cargosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:motivosReemplazosDialogo");
            RequestContext.getCurrentInstance().execute("PF('motivosReemplazosDialogo').show()");
            tipoActualizacion = 0;
         } else if (cualCelda == 7) {
            cargarLovEstructuras();
            RequestContext.getCurrentInstance().update("formularioDialogos:estructurasDialogo");
            RequestContext.getCurrentInstance().execute("PF('estructurasDialogo').show()");
         }
      }
   }

   public void mostrarOperacionEnProceso() {
      RequestContext.getCurrentInstance().execute(" PF('operacionEnProceso').show()");
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosTiposReemplazos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTipoReemplazo");
   }

   public void contarRegistrosMotivosReemplazos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMotivoReemplazo");
   }

   public void contarRegistrosBuscarEmpleados() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroBuscarEmpleados");
   }

   public void contarRegistrosEstructuras() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEstructura");
   }

   public void contarRegistrosCargos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCargos");
   }

   public void contarRegistrosEmpleados() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleados");
   }

   public void contarRegistrosEmpleadosNovedad() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleadosNovedad");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //Getter & Setter
   public Empleados getEmpleado() {
      if (tipoPantalla.equals("UNO") && empleado == null) {
         empleado = administrarNovedadesReemplazos.encontrarEmpleado(secuenciaEmpleado);
      } else {
         empleado = new Empleados();
      }
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public List<Encargaturas> getListaEncargaturas() {
      if (listaEncargaturas == null) {
         if (seleccionMostrar != null) {
            listaEncargaturas = administrarNovedadesReemplazos.encargaturasEmpleado(seleccionMostrar.getSecuencia());
            log.info("lista encargaturas en el get : " + listaEncargaturas);
         }
      }
      return listaEncargaturas;
   }

   public void setListaEncargaturas(List<Encargaturas> listaEncargaturas) {
      this.listaEncargaturas = listaEncargaturas;
   }

   public List<Encargaturas> getFiltradosListaEncargaturas() {
      return filtradosListaEncargaturas;
   }

   public void setFiltradosListaEncargaturas(List<Encargaturas> filtradosListaEncargaturas) {
      this.filtradosListaEncargaturas = filtradosListaEncargaturas;
   }

   public List<TiposReemplazos> getLovTiposReemplazos() {
      if (lovTiposReemplazos == null) {
         lovTiposReemplazos = administrarNovedadesReemplazos.lovTiposReemplazos();
      }
      return lovTiposReemplazos;
   }

   public void setLovTiposReemplazos(List<TiposReemplazos> lovTiposReemplazos) {
      this.lovTiposReemplazos = lovTiposReemplazos;
   }

   public List<MotivosReemplazos> getLovMotivosReemplazos() {
      if (lovMotivosReemplazos == null) {
         lovMotivosReemplazos = administrarNovedadesReemplazos.lovMotivosReemplazos();
      }
      return lovMotivosReemplazos;
   }

   public void setLovMotivosReemplazos(List<MotivosReemplazos> lovMotivosReemplazos) {
      this.lovMotivosReemplazos = lovMotivosReemplazos;
   }

   public List<Estructuras> getLovEstructuras() {
      return lovEstructuras;
   }

   public void setLovEstructuras(List<Estructuras> lovEstructuras) {
      this.lovEstructuras = lovEstructuras;
   }

   public List<Empleados> getLovEmpleados() {
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> lovEmpleados) {
      this.lovEmpleados = lovEmpleados;
   }

   public List<Empleados> getListaEmpleadosNovedad() {
      if (listaEmpleadosNovedad == null) {
         listaEmpleadosNovedad = administrarNovedadesReemplazos.buscarEmpleadoReemplazosHV(secuenciaEmpleado);
      }
      return listaEmpleadosNovedad;
   }

   public void setListaEmpleadosNovedad(List<Empleados> listaEmpleadosNovedad) {
      this.listaEmpleadosNovedad = listaEmpleadosNovedad;
   }

   public List<TiposReemplazos> getLovTiposReemplazosFiltrar() {
      return lovTiposReemplazosFiltrar;
   }

   public void setLovTiposReemplazosFiltrar(List<TiposReemplazos> lovTiposReemplazosFiltrar) {
      this.lovTiposReemplazosFiltrar = lovTiposReemplazosFiltrar;
   }

   public List<MotivosReemplazos> getLovMotivosReemplazosFiltrar() {
      return lovMotivosReemplazosFiltrar;
   }

   public void setLovMotivosReemplazosFiltrar(List<MotivosReemplazos> lovMotivosReemplazosFiltrar) {
      this.lovMotivosReemplazosFiltrar = lovMotivosReemplazosFiltrar;
   }

   public List<Estructuras> getLovEstructurasFiltrar() {
      return lovEstructurasFiltrar;
   }

   public void setLovEstructurasFiltrar(List<Estructuras> lovEstructurasFiltrar) {
      this.lovEstructurasFiltrar = lovEstructurasFiltrar;
   }

   public List<Empleados> getLovEmpleadosFiltrar() {
      return lovEmpleadosFiltrar;
   }

   public void setLovEmpleadosFiltrar(List<Empleados> lovEmpleadosFiltrar) {
      this.lovEmpleadosFiltrar = lovEmpleadosFiltrar;
   }

   public List<Empleados> getFiltradosListaEmpleadosNovedad() {
      return filtradosListaEmpleadosNovedad;
   }

   public void setFiltradosListaEmpleadosNovedad(List<Empleados> filtradoListaEmpleadosNovedad) {
      this.filtradosListaEmpleadosNovedad = filtradoListaEmpleadosNovedad;
   }

   public TiposReemplazos getSeleccionTiposReemplazos() {
      return seleccionTiposReemplazos;
   }

   public void setSeleccionTiposReemplazos(TiposReemplazos seleccionTiposReemplazos) {
      this.seleccionTiposReemplazos = seleccionTiposReemplazos;
   }

   public MotivosReemplazos getSeleccionMotivosReemplazos() {
      return seleccionMotivosReemplazos;
   }

   public void setSeleccionMotivosReemplazos(MotivosReemplazos seleccionMotivosReemplazos) {
      this.seleccionMotivosReemplazos = seleccionMotivosReemplazos;
   }

   public Estructuras getSeleccionEstructuras() {
      return seleccionEstructuras;
   }

   public void setSeleccionEstructuras(Estructuras seleccionEstructuras) {
      this.seleccionEstructuras = seleccionEstructuras;
   }

   public Empleados getSeleccionEmpleados() {
      return seleccionEmpleados;
   }

   public void setSeleccionEmpleados(Empleados seleccionEmpleados) {
      this.seleccionEmpleados = seleccionEmpleados;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public Empleados getSeleccionEmpleadosReemplazados() {
      return seleccionEmpleados;
   }

   public void setSeleccionEmpleadosReemplazados(Empleados seleccionEmpleados) {
      this.seleccionEmpleados = seleccionEmpleados;
   }

   public Encargaturas getEditarEncargaturas() {
      return editarEncargaturas;
   }

   public void setEditarEncargaturas(Encargaturas editarEncargaturas) {
      this.editarEncargaturas = editarEncargaturas;
   }

   public List<Cargos> getLovCargos() {
      return lovCargos;
   }

   public void setLovCargos(List<Cargos> lovCargos) {
      this.lovCargos = lovCargos;
   }

   public List<Cargos> getLovCargosFiltrar() {
      return lovCargosFiltrar;
   }

   public void setLovCargosFiltrar(List<Cargos> lovCargosFiltrar) {
      this.lovCargosFiltrar = lovCargosFiltrar;
   }

   public Cargos getSeleccionCargos() {
      return seleccionCargos;
   }

   public void setSeleccionCargos(Cargos seleccionCargos) {
      this.seleccionCargos = seleccionCargos;
   }

   public Encargaturas getNuevaEncargatura() {
      return nuevaEncargatura;
   }

   public void setNuevaEncargatura(Encargaturas nuevaEncargatura) {
      this.nuevaEncargatura = nuevaEncargatura;
   }

   public Encargaturas getDuplicarEncargatura() {
      return duplicarEncargatura;
   }

   public void setDuplicarEncargatura(Encargaturas duplicarEncargatura) {
      this.duplicarEncargatura = duplicarEncargatura;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public Empleados getSeleccionMostrar() {
      return seleccionMostrar;
   }

   public void setSeleccionMostrar(Empleados seleccionMostrar) {
      this.seleccionMostrar = seleccionMostrar;
   }

   public boolean isBotones() {
      return botones;
   }

   public Encargaturas getEncargaturaSeleccionada() {
      return encargaturaSeleccionada;
   }

   public void setEncargaturaSeleccionada(Encargaturas encargaturaSeleccionada) {
      this.encargaturaSeleccionada = encargaturaSeleccionada;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEncargaturasEmpleado");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroTipoReemplazo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVTiposReemplazos");
      infoRegistroTipoReemplazo = String.valueOf(tabla.getRowCount());
      return infoRegistroTipoReemplazo;
   }

   public void setInfoRegistroTipoReemplazo(String infoRegistroTipoReemplazo) {
      this.infoRegistroTipoReemplazo = infoRegistroTipoReemplazo;
   }

   public String getInfoRegistroMotivosReemplazos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVMotivosReemplazos");
      infoRegistroMotivosReemplazos = String.valueOf(tabla.getRowCount());
      return infoRegistroMotivosReemplazos;
   }

   public void setInfoRegistroMotivosReemplazos(String infoRegistroMotivosReemplazos) {
      this.infoRegistroMotivosReemplazos = infoRegistroMotivosReemplazos;
   }

   public String getInfoRegistroEstructuras() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEstructuras");
      infoRegistroEstructuras = String.valueOf(tabla.getRowCount());
      return infoRegistroEstructuras;
   }

   public void setInfoRegistroEstructuras(String infoRegistroEstructuras) {
      this.infoRegistroEstructuras = infoRegistroEstructuras;
   }

   public String getInfoRegistroCargos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVCargos");
      infoRegistroCargos = String.valueOf(tabla.getRowCount());
      return infoRegistroCargos;
   }

   public void setInfoRegistroCargos(String infoRegistroCargos) {
      this.infoRegistroCargos = infoRegistroCargos;
   }

   public String getInfoRegistroEmpleados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEmpleados");
      infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleados;
   }

   public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
      this.infoRegistroEmpleados = infoRegistroEmpleados;
   }

   public String getInfoRegistroEmpleadosNovedad() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
      infoRegistroEmpleadosNovedad = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpleadosNovedad;
   }

   public void setInfoRegistroEmpleadosNovedad(String infoRegistroEmpleadosNovedad) {
      this.infoRegistroEmpleadosNovedad = infoRegistroEmpleadosNovedad;
   }

   public List<Empleados> getLovBuscarEmpleados() {
      return lovBuscarEmpleados;
   }

   public void setLovBuscarEmpleados(List<Empleados> lovBuscarEmpleados) {
      this.lovBuscarEmpleados = lovBuscarEmpleados;
   }

   public List<Empleados> getLovBuscarEmpleadosFiltrar() {
      return lovBuscarEmpleadosFiltrar;
   }

   public void setLovBuscarEmpleadosFiltrar(List<Empleados> lovBuscarEmpleadosFiltrar) {
      this.lovBuscarEmpleadosFiltrar = lovBuscarEmpleadosFiltrar;
   }

   public Empleados getSeleccionBuscarEmpleados() {
      return seleccionBuscarEmpleados;
   }

   public void setSeleccionBuscarEmpleados(Empleados seleccionBuscarEmpleados) {
      this.seleccionBuscarEmpleados = seleccionBuscarEmpleados;
   }

   public String getInfoRegistroBuscarEmpleados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVBuscarEmpleados");
      infoRegistroBuscarEmpleados = String.valueOf(tabla.getRowCount());
      return infoRegistroBuscarEmpleados;
   }

   public void setInfoRegistroBuscarEmpleados(String infoRegistroBuscarEmpleados) {
      this.infoRegistroBuscarEmpleados = infoRegistroBuscarEmpleados;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
