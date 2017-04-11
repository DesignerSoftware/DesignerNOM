package Controlador;

import Entidades.Empleados;
import Entidades.IbcsAutoliquidaciones;
import Entidades.Procesos;
import Entidades.TiposEntidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarIbcAutoliquidInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class ControlIbcAutoliquid implements Serializable {

   @EJB
   AdministrarIbcAutoliquidInterface administrarIBCAuto;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //
   private List<IbcsAutoliquidaciones> listIbcsAutoliquidaciones;
   private List<IbcsAutoliquidaciones> filtrarListIbcsAutoliquidaciones;
   private IbcsAutoliquidaciones ibcsTablaSeleccionada;
   //
   private List<TiposEntidades> listTiposEntidades;
   private List<TiposEntidades> filtrarListTiposEntidades;
   private TiposEntidades tipoEntidadActual;
   private int registroActual;
   //
   private List<Procesos> listProcesos;
   private List<Procesos> filtrarListProcesos;
   private Procesos procesoSeleccionado;
   //
   private int tipoActualizacion;
   private int bandera, banderaTE;
   //Columnas Tabla VL
   private Column ibcFechaInicial, ibcFechaFinal, ibcEstado, ibcProceso, ibcValor, ibcFechaPago;
   private Column codigoTipoEntidad, nombreTipoEntidad;
   //Otros
   private boolean aceptar;
   private List<IbcsAutoliquidaciones> listIbcsAutoliquidacionesModificar;
   private boolean guardado;
   public IbcsAutoliquidaciones nuevoIBC;
   private List<IbcsAutoliquidaciones> listIbcsAutoliquidacionesCrear;
   private BigInteger l;
   private int k;
   private List<IbcsAutoliquidaciones> listIbcsAutoliquidacionesBorrar;
   private IbcsAutoliquidaciones editarIBC;
   private int cualCelda, tipoLista, tipoListaTE;
   private IbcsAutoliquidaciones duplicarIBC;
   private boolean permitirIndex;
   //Variables Autompletar
   //private String motivoCambioSueldo, tiposEntidades, tiposSueldos, terceros;
   //Indices VigenciaProrrateo / VigenciaProrrateoProyecto
   private String nombreXML;
   private String nombreTabla;
   private boolean cambiosIBC;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private String proceso;
   private Empleados empleado;
   private Date fechaParametro;
   private Date fechaIni, fechaFin;
   private BigDecimal auxValor;
   private String altoTabla;
   private String altoTablaTipoE;
   //
   private String infoRegistroProceso;
   private String infoRegistroTE;
   private String infoRegistroIBC;
   //
   private DataTable tablaC;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlIbcAutoliquid() {
      altoTabla = "170";
      altoTablaTipoE = "95";
      empleado = new Empleados();
      registroActual = 0;
      listProcesos = null;
      tipoEntidadActual = new TiposEntidades();
      listTiposEntidades = null;
      nombreTablaRastro = "";
      backUp = null;
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      tipoEntidadActual = null;
      listIbcsAutoliquidaciones = null;
      //Otros
      aceptar = true;
      //borrar aficiones
      listIbcsAutoliquidacionesBorrar = new ArrayList<IbcsAutoliquidaciones>();
      //crear aficiones
      listIbcsAutoliquidacionesCrear = new ArrayList<IbcsAutoliquidaciones>();
      k = 0;
      //modificar aficiones
      listIbcsAutoliquidacionesModificar = new ArrayList<IbcsAutoliquidaciones>();
      //editar
      editarIBC = new IbcsAutoliquidaciones();
      cualCelda = -1;
      tipoLista = 0;
      tipoListaTE = 0;
      //guardar
      guardado = true;
      //Crear VC
      nuevoIBC = new IbcsAutoliquidaciones();
      nuevoIBC.setProceso(new Procesos());
      ibcsTablaSeleccionada = null;
      bandera = 0;
      banderaTE = 0;
      permitirIndex = true;

      nombreTabla = ":formExportarIBCS:datosIBCSExportar";
      nombreXML = "IBCSAutoliquidacionXML";

      duplicarIBC = new IbcsAutoliquidaciones();
      cambiosIBC = false;
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
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
         System.out.println("navegar('Atras') : " + pag);
      } else {
         String pagActual = "ibcautoliquid";
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
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
      }
      limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarIBCAuto.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void obtenerTipoEntidad(BigInteger empl) {
      try {
         listIbcsAutoliquidaciones = null;
         listTiposEntidades = null;
         empleado = administrarIBCAuto.empleadoActual(empl);
         tipoEntidadActual = getTipoEntidadActual();
         getListIbcsAutoliquidaciones();

      } catch (Exception e) {
         System.out.println("Error obtenerTipoEntidad : " + e.toString());
      }
   }

   public void obtenerDetallesIBCS() {
      if (bandera == 1) {
         cerrarFiltrado();
      }
      ibcsTablaSeleccionada = null;
      if (cambiosIBC == true) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      } else {
         int posicion = listTiposEntidades.indexOf(tipoEntidadActual);
//-            secRegistroTipoEntidad = listTiposEntidades.get(posicion).getSecuencia();
         listIbcsAutoliquidaciones = null;
         getListIbcsAutoliquidaciones();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosIBCS");
      }
   }

   public void modificarIBCS(IbcsAutoliquidaciones ibc) {
      ibcsTablaSeleccionada.setValor(auxValor);
      if (!listIbcsAutoliquidacionesCrear.contains(ibcsTablaSeleccionada)) {
         if (listIbcsAutoliquidacionesModificar.isEmpty()) {
            listIbcsAutoliquidacionesModificar.add(ibcsTablaSeleccionada);
         } else if (!listIbcsAutoliquidacionesModificar.contains(ibcsTablaSeleccionada)) {
            listIbcsAutoliquidacionesModificar.add(ibcsTablaSeleccionada);
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      cambiosIBC = true;
   }

   public boolean validarFechasRegistro(int i) {
      fechaParametro = new Date();
      fechaParametro.setYear(0);
      fechaParametro.setMonth(1);
      fechaParametro.setDate(1);
      boolean retorno = true;
      if (i == 0) {
         IbcsAutoliquidaciones auxiliar = null;
         auxiliar = ibcsTablaSeleccionada;

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
         if (nuevoIBC.getFechafinal() != null) {
            if (nuevoIBC.getFechainicial().after(fechaParametro) && nuevoIBC.getFechainicial().before(nuevoIBC.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (nuevoIBC.getFechafinal() == null) {
            if (nuevoIBC.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      if (i == 2) {
         if (duplicarIBC.getFechafinal() != null) {
            if (duplicarIBC.getFechainicial().after(fechaParametro) && duplicarIBC.getFechainicial().before(duplicarIBC.getFechafinal())) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
         if (duplicarIBC.getFechafinal() == null) {
            if (duplicarIBC.getFechainicial().after(fechaParametro)) {
               retorno = true;
            } else {
               retorno = false;
            }
         }
      }
      return retorno;
   }

   public void modificarFechas(IbcsAutoliquidaciones ibc, int c) {
      IbcsAutoliquidaciones auxiliar = null;

      if (ibcsTablaSeleccionada.getFechainicial() != null) {
         boolean retorno = false;
         ibcsTablaSeleccionada = ibc;
         retorno = validarFechasRegistro(0);
         if (retorno == true) {
            cambiarIndice(ibcsTablaSeleccionada, c);
            modificarIBCS(ibcsTablaSeleccionada);
         } else {
            ibcsTablaSeleccionada.setFechafinal(fechaFin);
            ibcsTablaSeleccionada.setFechainicial(fechaIni);

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosIBCS");
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         ibcsTablaSeleccionada.setFechainicial(fechaIni);

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosIBCS");
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void modificarIBCSAutocompletar(IbcsAutoliquidaciones ibc, String confirmarCambio, String valorConfirmar) {
      ibcsTablaSeleccionada = ibc;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
         if (!valorConfirmar.isEmpty()) {
            ibcsTablaSeleccionada.getProceso().setDescripcion(proceso);

            for (int i = 0; i < listProcesos.size(); i++) {
               if (listProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               ibcsTablaSeleccionada.setProceso(listProcesos.get(indiceUnicoElemento));

               listProcesos.clear();
               getListProcesos();
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            coincidencias = 1;
            listProcesos.clear();
            getListProcesos();
            ibcsTablaSeleccionada.setProceso(new Procesos());

         }
      }
      if (coincidencias == 1) {
         if (!listIbcsAutoliquidacionesCrear.contains(ibcsTablaSeleccionada)) {
            if (listIbcsAutoliquidacionesModificar.isEmpty()) {
               listIbcsAutoliquidacionesModificar.add(ibcsTablaSeleccionada);
            } else if (!listIbcsAutoliquidacionesModificar.contains(ibcsTablaSeleccionada)) {
               listIbcsAutoliquidacionesModificar.add(ibcsTablaSeleccionada);
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }

         cambiosIBC = true;
      }
      RequestContext.getCurrentInstance().update("form:datosIBCS");
   }

   public void cambiarIndice(IbcsAutoliquidaciones ibc, int celda) {
      if (permitirIndex == true) {
         cualCelda = celda;
         ibcsTablaSeleccionada = ibc;
         fechaFin = ibcsTablaSeleccionada.getFechafinal();
         fechaIni = ibcsTablaSeleccionada.getFechainicial();
         auxValor = ibcsTablaSeleccionada.getValor();
         if (cualCelda == 3) {
            proceso = ibcsTablaSeleccionada.getProceso().getDescripcion();
         }

      }
   }

   public void guardadoGeneral() {
      if (cambiosIBC == true) {
         guardarCambiosIBCS();
      }
   }

   public void guardarCambiosIBCS() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listIbcsAutoliquidacionesBorrar.isEmpty()) {
               administrarIBCAuto.borrarIbcsAutoliquidaciones(listIbcsAutoliquidacionesBorrar);
               listIbcsAutoliquidacionesBorrar.clear();
            }
            if (!listIbcsAutoliquidacionesCrear.isEmpty()) {
               administrarIBCAuto.crearIbcsAutoliquidaciones(listIbcsAutoliquidacionesCrear);
               listIbcsAutoliquidacionesCrear.clear();
            }
            if (!listIbcsAutoliquidacionesModificar.isEmpty()) {
               administrarIBCAuto.modificarIbcsAutoliquidaciones(listIbcsAutoliquidacionesModificar);
               listIbcsAutoliquidacionesModificar.clear();
            }
            listIbcsAutoliquidaciones = null;
            RequestContext.getCurrentInstance().update("form:datosIBCS");
            cambiosIBC = false;
            k = 0;
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         System.out.println("Error guardarCambiosIBCS : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Se ha presentado un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }
   //CANCELAR MODIFICACIONES

   public void cancelarModificacion() {
      if (bandera == 1) {
         cerrarFiltrado();
      }
      if (banderaTE == 1) {
         cerrarFiltradoTE();
      }
      listIbcsAutoliquidacionesBorrar.clear();
      listIbcsAutoliquidacionesCrear.clear();
      listIbcsAutoliquidacionesModificar.clear();
      tipoEntidadActual = null;
      ibcsTablaSeleccionada = null;
      k = 0;
      listIbcsAutoliquidaciones = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      cambiosIBC = false;
      getListIbcsAutoliquidaciones();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosIBCS");
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (ibcsTablaSeleccionada != null) {
         editarIBC = ibcsTablaSeleccionada;

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEstadoD");
            RequestContext.getCurrentInstance().execute("PF('editarEstadoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoD");
            RequestContext.getCurrentInstance().execute("PF('editarProcesoD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValorD");
            RequestContext.getCurrentInstance().execute("PF('editarValorD').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaPagoD");
            RequestContext.getCurrentInstance().execute("PF('editarFechaPagoD').show()");
            cualCelda = -1;
         }
      }

   }

   public void validarIngresoNuevoRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:NuevoRegistroIBCS");
      RequestContext.getCurrentInstance().execute("PF('NuevoRegistroIBCS').show()");

   }

   public void validarDuplicadoRegistro() {
      if (ibcsTablaSeleccionada != null) {
         duplicarIBCS();
      }
   }

   public void validarBorradoRegistro() {
      if (ibcsTablaSeleccionada != null) {
         borrarIBCS();
      }
   }

   public void agregarNuevoIBCS() {
      if (nuevoIBC.getFechainicial() != null && nuevoIBC.getValor() != null) {
         if (validarFechasRegistro(1) == true) {
            if (bandera == 1) {
               cerrarFiltrado();
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
            k++;
            BigInteger var = BigInteger.valueOf(k);
            nuevoIBC.setSecuencia(var);
            nuevoIBC.setTipoentidad(tipoEntidadActual);
            listIbcsAutoliquidacionesCrear.add(nuevoIBC);
            listIbcsAutoliquidaciones.add(nuevoIBC);
            ////------////
            nuevoIBC = new IbcsAutoliquidaciones();
            nuevoIBC.setProceso(new Procesos());

            ////-----////
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosIBCS");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroIBCS').hide()");
            cambiosIBC = true;
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void limpiarNuevoIBCS() {
      nuevoIBC = new IbcsAutoliquidaciones();
      nuevoIBC.setProceso(new Procesos());

   }

   public void duplicarIBCS() {
      if (ibcsTablaSeleccionada != null) {
         duplicarIBC = new IbcsAutoliquidaciones();

         duplicarIBC.setEmpleado(ibcsTablaSeleccionada.getEmpleado());
         duplicarIBC.setEstado(ibcsTablaSeleccionada.getEstado());
         duplicarIBC.setFechafinal(ibcsTablaSeleccionada.getFechafinal());
         duplicarIBC.setFechainicial(ibcsTablaSeleccionada.getFechainicial());
         duplicarIBC.setFechapago(ibcsTablaSeleccionada.getFechapago());
         duplicarIBC.setFechasistema(ibcsTablaSeleccionada.getFechasistema());
         duplicarIBC.setProceso(ibcsTablaSeleccionada.getProceso());
         duplicarIBC.setValor(ibcsTablaSeleccionada.getValor());

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroIBCS");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroIBCS').show()");
      }
   }

   public void confirmarDuplicar() {
      if (duplicarIBC.getFechainicial() != null && duplicarIBC.getValor() != null) {
         if (validarFechasRegistro(2) == true) {
            if (bandera == 1) {
               cerrarFiltrado();
            }
            k++;
            BigInteger var = BigInteger.valueOf(k);
            duplicarIBC.setSecuencia(var);
            duplicarIBC.setTipoentidad(tipoEntidadActual);
            listIbcsAutoliquidacionesCrear.add(duplicarIBC);
            listIbcsAutoliquidaciones.add(duplicarIBC);
            duplicarIBC = new IbcsAutoliquidaciones();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosIBCS");
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroIBCS').hide()");
            cambiosIBC = true;
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
      }
   }

   public void cerrarFiltrado() {
      altoTabla = "170";
      ibcFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcFechaInicial");
      ibcFechaInicial.setFilterStyle("display: none; visibility: hidden;");
      ibcFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcFechaFinal");
      ibcFechaFinal.setFilterStyle("display: none; visibility: hidden;");
      ibcEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcEstado");
      ibcEstado.setFilterStyle("display: none; visibility: hidden;");
      ibcProceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcProceso");
      ibcProceso.setFilterStyle("display: none; visibility: hidden;");
      ibcValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcValor");
      ibcValor.setFilterStyle("display: none; visibility: hidden;");
      ibcFechaPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcFechaPago");
      ibcFechaPago.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosIBCS");
      bandera = 0;
      filtrarListIbcsAutoliquidaciones = null;
      tipoLista = 0;
   }

   public void cerrarFiltradoTE() {
      altoTablaTipoE = "95";
      codigoTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoEntidad:codigoTipoEntidad");
      codigoTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
      nombreTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoEntidad:nombreTipoEntidad");
      nombreTipoEntidad.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosTipoEntidad");
      banderaTE = 0;
      filtrarListTiposEntidades = null;
      tipoListaTE = 0;
   }

   public void limpiarDuplicarIBCS() {
      duplicarIBC = new IbcsAutoliquidaciones();
      duplicarIBC.setProceso(new Procesos());
   }

   public void borrarIBCS() {
      if (ibcsTablaSeleccionada != null) {
         if (!listIbcsAutoliquidacionesModificar.isEmpty() && listIbcsAutoliquidacionesModificar.contains(ibcsTablaSeleccionada)) {
            int modIndex = listIbcsAutoliquidacionesModificar.indexOf(ibcsTablaSeleccionada);
            listIbcsAutoliquidacionesModificar.remove(modIndex);
            listIbcsAutoliquidacionesBorrar.add(ibcsTablaSeleccionada);
         } else if (!listIbcsAutoliquidacionesCrear.isEmpty() && listIbcsAutoliquidacionesCrear.contains(ibcsTablaSeleccionada)) {
            int crearIndex = listIbcsAutoliquidacionesCrear.indexOf(ibcsTablaSeleccionada);
            listIbcsAutoliquidacionesCrear.remove(crearIndex);
         } else {
            listIbcsAutoliquidacionesBorrar.add(ibcsTablaSeleccionada);
         }
         listIbcsAutoliquidaciones.remove(ibcsTablaSeleccionada);
         if (tipoLista == 1) {
            filtrarListIbcsAutoliquidaciones.remove(ibcsTablaSeleccionada);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosIBCS");
         ibcsTablaSeleccionada = null;
         cambiosIBC = true;
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void activarCtrlF11() {
      if (tipoEntidadActual != null) {
         filtradoTipoEntidad();
      }
      if (ibcsTablaSeleccionada != null) {
         filtradoIBCS();
      }
   }

   public void filtradoIBCS() {
      if (bandera == 0) {
         altoTabla = "150";
         ibcFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcFechaInicial");
         ibcFechaInicial.setFilterStyle("width: 85% !important");
         ibcFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcFechaFinal");
         ibcFechaFinal.setFilterStyle("width: 85% !important");
         ibcEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcEstado");
         ibcEstado.setFilterStyle("width: 85% !important");
         ibcProceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcProceso");
         ibcProceso.setFilterStyle("width: 85% !important");
         ibcValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcValor");
         ibcValor.setFilterStyle("width: 85% !important");
         ibcFechaPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosIBCS:ibcFechaPago");
         ibcFechaPago.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosIBCS");
         bandera = 1;
      } else if (bandera == 1) {
         cerrarFiltrado();
      }
   }

   public void filtradoTipoEntidad() {
      if (banderaTE == 0) {
         altoTablaTipoE = "75";
         codigoTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoEntidad:codigoTipoEntidad");
         codigoTipoEntidad.setFilterStyle("width: 85% !important");
         nombreTipoEntidad = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTipoEntidad:nombreTipoEntidad");
         nombreTipoEntidad.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosTipoEntidad");
         banderaTE = 1;
      } else if (banderaTE == 1) {
         cerrarFiltradoTE();
      }
   }

   public void salir() {  limpiarListasValor();
      if (banderaTE == 1) {
         cerrarFiltradoTE();
      }
      if (bandera == 1) {
         cerrarFiltrado();
      }

      listIbcsAutoliquidacionesBorrar.clear();
      listIbcsAutoliquidacionesCrear.clear();
      listIbcsAutoliquidacionesModificar.clear();
      ibcsTablaSeleccionada = null;
      tipoEntidadActual = null;
      registroActual = 0;
      k = 0;
      listIbcsAutoliquidaciones = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void asignarIndex(IbcsAutoliquidaciones ibc, int dlg, int LND) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (LND == 0) {
         ibcsTablaSeleccionada = ibc;
         tipoActualizacion = 0;
      } else if (LND == 1) {
         tipoActualizacion = 1;
      } else if (LND == 2) {
         tipoActualizacion = 2;
      }
      if (dlg == 0) {
         RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
      }
   }

   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (ibcsTablaSeleccionada != null) {
         if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("PROCESO")) {
         if (tipoNuevo == 1) {
            proceso = nuevoIBC.getProceso().getDescripcion();
         } else if (tipoNuevo == 2) {
            proceso = duplicarIBC.getProceso().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicadoIBCS(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
         if (!valorConfirmar.isEmpty()) {
            if (tipoNuevo == 1) {
               nuevoIBC.getProceso().setDescripcion(proceso);
            } else if (tipoNuevo == 2) {
               duplicarIBC.getProceso().setDescripcion(proceso);
            }
            for (int i = 0; i < listProcesos.size(); i++) {
               if (listProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 1) {
                  nuevoIBC.setProceso(listProcesos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoIBCS");
               } else if (tipoNuevo == 2) {
                  duplicarIBC.setProceso(listProcesos.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicaProcesoIBCS");
               }
               listProcesos.clear();
               getListProcesos();
            } else {
               RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 1) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoIBCS");
               } else if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicaProcesoIBCS");
               }
            }
         } else {
            listProcesos.clear();
            getListProcesos();
            if (tipoNuevo == 1) {
               nuevoIBC.setProceso(new Procesos());
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoIBCS");
            } else if (tipoNuevo == 2) {
               duplicarIBC.setProceso(new Procesos());
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicaProcesoIBCS");
            }
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void actualizarProceso() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         ibcsTablaSeleccionada.setProceso(procesoSeleccionado);
         if (!listIbcsAutoliquidacionesCrear.contains(ibcsTablaSeleccionada)) {
            if (listIbcsAutoliquidacionesModificar.isEmpty()) {
               listIbcsAutoliquidacionesModificar.add(ibcsTablaSeleccionada);
            } else if (!listIbcsAutoliquidacionesModificar.contains(ibcsTablaSeleccionada)) {
               listIbcsAutoliquidacionesModificar.add(ibcsTablaSeleccionada);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         cambiosIBC = true;
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosIBCS");
      } else if (tipoActualizacion == 1) {
         nuevoIBC.setProceso(procesoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoIBCS");
      } else if (tipoActualizacion == 2) {
         duplicarIBC.setProceso(procesoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicaProcesoIBCS");
      }
      filtrarListProcesos = null;
      procesoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;/*
         * RequestContext.getCurrentInstance().update("form:ProcesosDialogo");
         * RequestContext.getCurrentInstance().update("form:lovProcesos"); RequestContext.getCurrentInstance().update("form:aceptarP");
       */
      context.reset("form:lovProcesos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProcesos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').hide()");
   }

   public void cancelarCambioProceso() {
      filtrarListProcesos = null;
      procesoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovProcesos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProcesos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').hide()");
   }

   public void posicionTipoEntidad() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      tipoEntidadActual = listTiposEntidades.get(indice);

      RequestContext context2 = RequestContext.getCurrentInstance();
      context2.update("form:datosTipoEntidad");
      obtenerDetallesIBCS();
   }

   public String exportXML() {
      if (ibcsTablaSeleccionada != null) {
         nombreTabla = ":formExportarIBCS:datosIBCSExportar";
         nombreXML = "IBCSAutoliquidacion_XML";
      }
      if (tipoEntidadActual != null) {
         nombreTabla = ":formExportarTipoEntidad:datosTipoEntidadExportar";
         nombreXML = "TiposEntidades_XML";
      }
      return nombreTabla;
   }

   public void validarExportPDF() throws IOException {
      if (ibcsTablaSeleccionada != null) {
         exportPDF_IBC();
      }
      if (tipoEntidadActual != null) {
         exportPDF_TE();
      }
   }

   public void exportPDF_IBC() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarIBCS:datosIBCSExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "IBCSAutoliquidacion_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportPDF_TE() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTipoEntidad:datosTipoEntidadExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TiposEntidades_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarExportXLS() throws IOException {
      if (ibcsTablaSeleccionada != null) {
         exportXLS_IBC();
      }
      if (tipoEntidadActual != null) {
         exportXLS_TE();
      }
   }

   public void exportXLS_IBC() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarIBCS:datosIBCSExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "IBCSAutoliquidacion_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_TE() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarTipoEntidad:datosTipoEntidadExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TiposEntidades_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //METODO RASTROS PARA LAS TABLAS EN EMPLVIGENCIASUELDOS
   public void verificarRastroTabla() {
      if (tipoEntidadActual != null) {
         verificarRastroTipoEntidad();
      }
      if (ibcsTablaSeleccionada != null) {
         verificarRastroIBCS();
      }
   }

   public void verificarRastroIBCS() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listIbcsAutoliquidaciones != null) {
         if (ibcsTablaSeleccionada.getSecuencia() != null) {
            int resultado = administrarRastros.obtenerTabla(ibcsTablaSeleccionada.getSecuencia(), "IBCSAUTOLIQUIDACIONES");
            backUp = ibcsTablaSeleccionada.getSecuencia();
            if (resultado == 1) {
               RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
               nombreTablaRastro = "IbcsAutoliquidaciones";
               msnConfirmarRastro = "La tabla IBCSAUTOLIQUIDACIONES tiene rastros para el registro seleccionado, ¿desea continuar?";
               RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
      } else if (administrarRastros.verificarHistoricosTabla("IBCSAUTOLIQUIDACIONES")) {
         nombreTablaRastro = "IbcsAutoliquidaciones";
         msnConfirmarRastroHistorico = "La tabla IBCSAUTOLIQUIDACIONES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroTipoEntidad() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listTiposEntidades != null) {
         if (tipoEntidadActual != null) {
            int resultado = administrarRastros.obtenerTabla(tipoEntidadActual.getSecuencia(), "TIPOSENTIDADES");
            backUp = tipoEntidadActual.getSecuencia();
            if (resultado == 1) {
               RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (resultado == 2) {
               nombreTablaRastro = "TiposEntidades";
               msnConfirmarRastro = "La tabla TIPOSENTIDADES tiene rastros para el registro seleccionado, ¿desea continuar?";
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSENTIDADES")) {
         nombreTablaRastro = "TiposEntidades";
         msnConfirmarRastroHistorico = "La tabla TIPOSENTIDADES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   public void eventoFiltrarTE() {
      if (tipoEntidadActual != null) {
         if (tipoListaTE == 0) {
            tipoListaTE = 1;
         }
      }
   }

   public void eventoFiltrarIBC() {
      if (ibcsTablaSeleccionada != null) {
         if (tipoLista == 0) {
            tipoLista = 1;
         }
      }
   }

   public void eventoFiltrarProceso() {
      modificarInfoRegistroProcesos(filtrarListProcesos.size());
      RequestContext.getCurrentInstance().update("form:infoRegistroProceso");
   }

   private void modificarInfoRegistroTE(int valor) {
      infoRegistroTE = String.valueOf(valor);
   }

   private void modificarInfoRegistroIBC(int valor) {
      infoRegistroIBC = String.valueOf(valor);
   }

   private void modificarInfoRegistroProcesos(int valor) {
      infoRegistroProceso = String.valueOf(valor);
   }

   public void contarRegistrosTE() {
      if (listTiposEntidades != null) {
         if (listTiposEntidades.size() > 0) {
            modificarInfoRegistroTE(listTiposEntidades.size());
            RequestContext.getCurrentInstance().update("form:infoRegistroTE");
         } else {
            modificarInfoRegistroTE(0);
            RequestContext.getCurrentInstance().update("form:infoRegistroTE");
         }
      } else {
         modificarInfoRegistroTE(0);
         RequestContext.getCurrentInstance().update("form:infoRegistroTE");
      }
   }

   public void contarRegistrosIBC() {
      if (listIbcsAutoliquidaciones != null) {
         if (listIbcsAutoliquidaciones.size() > 0) {
            modificarInfoRegistroIBC(listIbcsAutoliquidaciones.size());
            RequestContext.getCurrentInstance().update("form:infoRegistroIBC");
         } else {
            modificarInfoRegistroIBC(0);
            RequestContext.getCurrentInstance().update("form:infoRegistroIBC");
         }
      } else {
         modificarInfoRegistroIBC(0);
         RequestContext.getCurrentInstance().update("form:infoRegistroIBC");
      }
   }

   public void contarRegistrosProceso() {
      if (listProcesos != null) {
         if (listProcesos.size() > 0) {
            modificarInfoRegistroProcesos(listProcesos.size());
         } else {
            modificarInfoRegistroProcesos(0);
         }
      } else {
         modificarInfoRegistroProcesos(0);
      }
   }

   public void recordarSeleccion() {
      if (tipoEntidadActual != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosTipoEntidad");
         tablaC.setSelection(tipoEntidadActual);
      } else {
         tipoEntidadActual = null;
      }
      //System.out.println("vigenciaSeleccionada: " + vigenciaSeleccionada);
   }

   ////////////////////////////////////******************************************************************************************************************************************/
   public List<IbcsAutoliquidaciones> getListIbcsAutoliquidaciones() {
      try {
         if (listIbcsAutoliquidaciones == null) {
            if (tipoEntidadActual.getSecuencia() != null && empleado.getSecuencia() != null) {
               listIbcsAutoliquidaciones = administrarIBCAuto.listIBCSAutoliquidaciones(tipoEntidadActual.getSecuencia(), empleado.getSecuencia());
            }
         }
         return listIbcsAutoliquidaciones;
      } catch (Exception e) {
         System.out.println("Error getListIbcsAutoliquidaciones : " + e.toString());
         return null;
      }

   }

   public void setListIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> listIbcsAutoliquidaciones) {
      this.listIbcsAutoliquidaciones = listIbcsAutoliquidaciones;
   }

   public List<IbcsAutoliquidaciones> getFiltrarListIbcsAutoliquidaciones() {
      return filtrarListIbcsAutoliquidaciones;
   }

   public void setFiltrarListIbcsAutoliquidaciones(List<IbcsAutoliquidaciones> filtrarListIbcsAutoliquidaciones) {
      this.filtrarListIbcsAutoliquidaciones = filtrarListIbcsAutoliquidaciones;
   }

   public List<TiposEntidades> getListTiposEntidades() {
      try {
         if (listTiposEntidades == null) {
            listTiposEntidades = administrarIBCAuto.listTiposEntidadesIBCS();
         }
         return listTiposEntidades;
      } catch (Exception e) {
         System.out.println("Error getListTiposEntidades : " + e.toString());
         return null;
      }

   }

   public void setListTiposEntidades(List<TiposEntidades> listTiposEntidades) {
      this.listTiposEntidades = listTiposEntidades;
   }

   public TiposEntidades getTipoEntidadActual() {
//        
//        try {
//            getListTiposEntidades();
//            int tam = listTiposEntidades.size();
//            if (tam >= 1) {
//                tipoEntidadActual = listTiposEntidades.get(registroActual);
//            }
      return tipoEntidadActual;
//        } catch (Exception e) {
//            System.out.println("Error getTipoEntidadActual : " + e.toString());
//            return null;
//        }
   }

   public void setTipoEntidadActual(TiposEntidades tipoEntidadActual) {
      this.tipoEntidadActual = tipoEntidadActual;
   }

   public List<Procesos> getListProcesos() {
      if (listProcesos == null) {
         listProcesos = administrarIBCAuto.listProcesos();
      }
      return listProcesos;
   }

   public void setListProcesos(List<Procesos> listProcesos) {
      this.listProcesos = listProcesos;
   }

   public List<Procesos> getFiltrarListProcesos() {
      return filtrarListProcesos;
   }

   public void setFiltrarListProcesos(List<Procesos> filtrarListProcesos) {
      this.filtrarListProcesos = filtrarListProcesos;
   }

   public Procesos getProcesoSeleccionado() {
      return procesoSeleccionado;
   }

   public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
      this.procesoSeleccionado = procesoSeleccionado;
   }

   public List<IbcsAutoliquidaciones> getListIbcsAutoliquidacionesModificar() {
      return listIbcsAutoliquidacionesModificar;
   }

   public void setListIbcsAutoliquidacionesModificar(List<IbcsAutoliquidaciones> listIbcsAutoliquidacionesModificar) {
      this.listIbcsAutoliquidacionesModificar = listIbcsAutoliquidacionesModificar;
   }

   public IbcsAutoliquidaciones getNuevoIBC() {
      return nuevoIBC;
   }

   public void setNuevoIBC(IbcsAutoliquidaciones nuevoIBC) {
      this.nuevoIBC = nuevoIBC;
   }

   public List<IbcsAutoliquidaciones> getListIbcsAutoliquidacionesCrear() {
      return listIbcsAutoliquidacionesCrear;
   }

   public void setListIbcsAutoliquidacionesCrear(List<IbcsAutoliquidaciones> listIbcsAutoliquidacionesCrear) {
      this.listIbcsAutoliquidacionesCrear = listIbcsAutoliquidacionesCrear;
   }

   public List<IbcsAutoliquidaciones> getListIbcsAutoliquidacionesBorrar() {
      return listIbcsAutoliquidacionesBorrar;
   }

   public void setListIbcsAutoliquidacionesBorrar(List<IbcsAutoliquidaciones> listIbcsAutoliquidacionesBorrar) {
      this.listIbcsAutoliquidacionesBorrar = listIbcsAutoliquidacionesBorrar;
   }

   public IbcsAutoliquidaciones getEditarIBC() {
      return editarIBC;
   }

   public void setEditarIBC(IbcsAutoliquidaciones editarIBC) {
      this.editarIBC = editarIBC;
   }

   public IbcsAutoliquidaciones getDuplicarIBC() {
      return duplicarIBC;
   }

   public void setDuplicarIBC(IbcsAutoliquidaciones duplicarIBC) {
      this.duplicarIBC = duplicarIBC;
   }

   public String getNombreXML() {
      return nombreXML;
   }

   public void setNombreXML(String nombreXML) {
      this.nombreXML = nombreXML;
   }

   public String getNombreTabla() {
      return nombreTabla;
   }

   public void setNombreTabla(String nombreTabla) {
      this.nombreTabla = nombreTabla;
   }

   public String getMsnConfirmarRastro() {
      return msnConfirmarRastro;
   }

   public void setMsnConfirmarRastro(String msnConfirmarRastro) {
      this.msnConfirmarRastro = msnConfirmarRastro;
   }

   public String getMsnConfirmarRastroHistorico() {
      return msnConfirmarRastroHistorico;
   }

   public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
      this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
   }

   public BigInteger getBackUp() {
      return backUp;
   }

   public void setBackUp(BigInteger backUp) {
      this.backUp = backUp;
   }

   public String getNombreTablaRastro() {
      return nombreTablaRastro;
   }

   public void setNombreTablaRastro(String nombreTablaRastro) {
      this.nombreTablaRastro = nombreTablaRastro;
   }

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

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public List<TiposEntidades> getFiltrarListTiposEntidades() {
      return filtrarListTiposEntidades;
   }

   public void setFiltrarListTiposEntidades(List<TiposEntidades> filtrarListTiposEntidades) {
      this.filtrarListTiposEntidades = filtrarListTiposEntidades;
   }

   public String getAltoTablaTipoE() {
      return altoTablaTipoE;
   }

   public void setAltoTablaTipoE(String altoTablaTipoE) {
      this.altoTablaTipoE = altoTablaTipoE;
   }

   public IbcsAutoliquidaciones getIbcsTablaSeleccionada() {
      return ibcsTablaSeleccionada;
   }

   public void setIbcsTablaSeleccionada(IbcsAutoliquidaciones ibcsTablaSeleccionada) {
      this.ibcsTablaSeleccionada = ibcsTablaSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistroProceso() {
      return infoRegistroProceso;
   }

   public String getInfoRegistroIBC() {
      return infoRegistroIBC;
   }

   public String getInfoRegistroTE() {
      return infoRegistroTE;
   }
}
