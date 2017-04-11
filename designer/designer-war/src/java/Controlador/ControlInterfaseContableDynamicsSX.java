package Controlador;

import Entidades.ActualUsuario;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.InterconDynamics;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarInterfaseContableDynamicsSXInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
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
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlInterfaseContableDynamicsSX implements Serializable {

   @EJB
   AdministrarInterfaseContableDynamicsSXInterface administrarInterfaseDynamicsSX;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private ActualUsuario actualUsuarioBD;
   //
   private ParametrosContables parametroContableActual;
   private ParametrosContables nuevoParametroContable;
   private List<ParametrosContables> listaParametrosContables;
   private int indexParametroContable;
   private String auxParametroEmpresa, auxParametroProceso;
   private Date auxParametroFechaInicial, auxParametroFechaFinal;
   private String auxParametroEmplDesde, auxParametroEmplHasta;
   private boolean permitirIndexParametro;
   private boolean cambiosParametro;
   private List<ParametrosContables> listParametrosContablesBorrar;
   //
   private List<SolucionesNodos> listaGenerados;
   private List<SolucionesNodos> filtrarListaGenerados;
   private SolucionesNodos generadoTablaSeleccionado;
   private int indexGenerado, cualCeldaGenerado;
   private SolucionesNodos editarGenerado;
   private int tipoListaGenerada, banderaGenerado;
   //
   private List<InterconDynamics> listaInterconDynamics;
   private List<InterconDynamics> filtrarListaInterconDynamics;
   private InterconDynamics interconTablaSeleccionada;
   private int indexIntercon, cualCeldaIntercon;
   private InterconDynamics editarIntercon;
   private int tipoListaIntercon, banderaIntercon;

   private List<Empresas> lovEmpresas;
   private List<Empresas> filtrarLovEmpresas;
   private Empresas empresaSeleccionada;
   private String infoRegistroEmpresa;

   private List<Procesos> lovProcesos;
   private List<Procesos> filtrarLovProcesos;
   private Procesos procesoSeleccionado;
   private String infoRegistroProceso;

   private List<Empleados> lovEmpleados;
   private List<Empleados> filtrarLovEmpleados;
   private Empleados empleadoSeleccionado;
   private String infoRegistroEmpleado;
   //
   private boolean guardado;
   private Date fechaDeParametro;
   private boolean aceptar;
   //
   private boolean estadoBtnArriba, estadoBtnAbajo;
   private int registroActual;
   //
   private boolean activarAgregar, activarOtros;
   private boolean activarEnviar, activarDeshacer;
   //
   private int tipoActualizacion;
   //
   private boolean modificacionParametro;
   //
   private Column genConcepto, genValor, genTercero, genCntDebito, genCntCredito, genEmpleado, genProceso;
   private String altoTablaGenerada;
   private Column interEmpleado, interTercero, interCuenta, interDebito, interCredito, interConcepto, interCentroCosto, interFechaVencimiento, interOriginalDebito, interOriginalCredito, interPreradicacion, interCodAlternativo;
   private String altoTablaIntercon;
   //
   private BigInteger secRegistro, backUpSecRegistro;
   //
   private int tipoPlano;
   //
   private String msnFechasActualizar;
   //
   private int totalCGenerado, totalDGenerado, totalDInter, totalCInter;
   //
   private String msnPaso1;
   //
   private String fechaIniRecon, fechaFinRecon;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlInterfaseContableDynamicsSX() {
      msnPaso1 = "";
      totalCGenerado = 0;
      totalDGenerado = 0;
      totalDInter = 0;
      totalCInter = 0;
      activarEnviar = true;
      activarDeshacer = true;
      msnFechasActualizar = "";
      tipoPlano = 1;
      altoTablaGenerada = "75";
      altoTablaIntercon = "75";
      tipoListaGenerada = 0;
      tipoListaIntercon = 0;
      banderaGenerado = 0;
      banderaIntercon = 0;
      modificacionParametro = false;
      listParametrosContablesBorrar = new ArrayList<ParametrosContables>();
      nuevoParametroContable = new ParametrosContables();
      nuevoParametroContable.setEmpresaRegistro(new Empresas());
      nuevoParametroContable.setProceso(new Procesos());
      activarAgregar = true;
      activarOtros = true;
      listaGenerados = null;
      listaInterconDynamics = null;
      generadoTablaSeleccionado = new SolucionesNodos();
      interconTablaSeleccionada = new InterconDynamics();
      indexGenerado = -1;
      indexIntercon = -1;
      cualCeldaGenerado = -1;
      cualCeldaIntercon = -1;
      editarGenerado = new SolucionesNodos();
      editarIntercon = new InterconDynamics();
      registroActual = 0;
      estadoBtnArriba = true;
      estadoBtnAbajo = true;
      aceptar = true;
      cambiosParametro = false;
      permitirIndexParametro = true;
      indexParametroContable = -1;
      guardado = true;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      actualUsuarioBD = null;
      getActualUsuarioBD();
      listaParametrosContables = null;
      getListaParametrosContables();
      parametroContableActual = null;
      getParametroContableActual();
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      actualUsuarioBD = null;
      getActualUsuarioBD();
      listaParametrosContables = null;
      getListaParametrosContables();
      parametroContableActual = null;
      getParametroContableActual();
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
         String pagActual = "interfasecontabledynamicssx";
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
         administrarInterfaseDynamicsSX.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public String redirigir() {
      return paginaAnterior;
   }

   public void anteriorParametro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado == true) {
         if (registroActual > 0) {
            registroActual--;
            parametroContableActual = listaParametrosContables.get(registroActual);
            listaGenerados = null;
            listaInterconDynamics = null;
            activarEnviar = true;
            activarDeshacer = true;
            RequestContext.getCurrentInstance().update("form:btnEnviar");
            RequestContext.getCurrentInstance().update("form:btnDeshacer");
            RequestContext.getCurrentInstance().update("form:PLANO");
            totalCGenerado = 0;
            totalDGenerado = 0;
            totalDInter = 0;
            totalCInter = 0;
            if (registroActual == 0) {
               estadoBtnArriba = true;
            }
            if (registroActual < (listaParametrosContables.size() - 1)) {
               estadoBtnAbajo = false;
            }

            if (banderaGenerado == 1) {
               FacesContext c = FacesContext.getCurrentInstance();
               altoTablaGenerada = "75";
               genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
               genProceso.setFilterStyle("display: none; visibility: hidden;");
               genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
               genEmpleado.setFilterStyle("display: none; visibility: hidden;");
               genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
               genCntCredito.setFilterStyle("display: none; visibility: hidden;");
               genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
               genCntDebito.setFilterStyle("display: none; visibility: hidden;");
               genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
               genTercero.setFilterStyle("display: none; visibility: hidden;");
               genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
               genValor.setFilterStyle("display: none; visibility: hidden;");
               genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
               genConcepto.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosGenerados");
               banderaGenerado = 0;
               filtrarListaGenerados = null;
               tipoListaGenerada = 0;
            }
            if (banderaIntercon == 1) {
               FacesContext c = FacesContext.getCurrentInstance();
               altoTablaIntercon = "75";
               interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
               interEmpleado.setFilterStyle("display: none; visibility: hidden;");
               interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
               interTercero.setFilterStyle("display: none; visibility: hidden;");
               interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
               interCuenta.setFilterStyle("display: none; visibility: hidden;");
               interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
               interDebito.setFilterStyle("display: none; visibility: hidden;");
               interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
               interCredito.setFilterStyle("display: none; visibility: hidden;");
               interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
               interConcepto.setFilterStyle("display: none; visibility: hidden;");
               interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
               interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
               interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
               interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
               interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
               interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
               interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
               interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
               interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
               interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
               interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
               interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosIntercon");
               banderaIntercon = 0;
               filtrarListaInterconDynamics = null;
               tipoListaIntercon = 0;
            }
            activarEnviar = true;
            activarDeshacer = true;
            RequestContext.getCurrentInstance().update("form:PanelTotal");
            RequestContext.getCurrentInstance().update("form:panelParametro");
            RequestContext.getCurrentInstance().update("form:btnArriba");
            RequestContext.getCurrentInstance().update("form:btnAbajo");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
      }
   }

   public void siguienteParametro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado == true) {
         if (registroActual < (listaParametrosContables.size() - 1)) {
            registroActual++;
            parametroContableActual = listaParametrosContables.get(registroActual);
            listaGenerados = null;
            listaInterconDynamics = null;
            activarEnviar = true;
            activarDeshacer = true;
            RequestContext.getCurrentInstance().update("form:btnEnviar");
            RequestContext.getCurrentInstance().update("form:btnDeshacer");
            RequestContext.getCurrentInstance().update("form:PLANO");
            totalCGenerado = 0;
            totalDGenerado = 0;
            totalDInter = 0;
            totalCInter = 0;
            if (registroActual > 0) {
               estadoBtnArriba = false;
            }
            if (registroActual == (listaParametrosContables.size() - 1)) {
               estadoBtnAbajo = true;
            }

            if (banderaGenerado == 1) {
               FacesContext c = FacesContext.getCurrentInstance();
               altoTablaGenerada = "75";
               genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
               genProceso.setFilterStyle("display: none; visibility: hidden;");
               genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
               genEmpleado.setFilterStyle("display: none; visibility: hidden;");
               genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
               genCntCredito.setFilterStyle("display: none; visibility: hidden;");
               genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
               genCntDebito.setFilterStyle("display: none; visibility: hidden;");
               genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
               genTercero.setFilterStyle("display: none; visibility: hidden;");
               genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
               genValor.setFilterStyle("display: none; visibility: hidden;");
               genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
               genConcepto.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosGenerados");
               banderaGenerado = 0;
               filtrarListaGenerados = null;
               tipoListaGenerada = 0;
            }
            if (banderaIntercon == 1) {
               FacesContext c = FacesContext.getCurrentInstance();
               altoTablaIntercon = "75";
               interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
               interEmpleado.setFilterStyle("display: none; visibility: hidden;");
               interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
               interTercero.setFilterStyle("display: none; visibility: hidden;");
               interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
               interCuenta.setFilterStyle("display: none; visibility: hidden;");
               interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
               interDebito.setFilterStyle("display: none; visibility: hidden;");
               interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
               interCredito.setFilterStyle("display: none; visibility: hidden;");
               interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
               interConcepto.setFilterStyle("display: none; visibility: hidden;");
               interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
               interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
               interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
               interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
               interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
               interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
               interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
               interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
               interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
               interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
               interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
               interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosIntercon");
               banderaIntercon = 0;
               filtrarListaInterconDynamics = null;
               tipoListaIntercon = 0;
            }
            activarEnviar = true;
            activarDeshacer = true;
            RequestContext.getCurrentInstance().update("form:PanelTotal");
            RequestContext.getCurrentInstance().update("form:panelParametro");
            RequestContext.getCurrentInstance().update("form:btnArriba");
            RequestContext.getCurrentInstance().update("form:btnAbajo");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardarSinSalida').show()");
      }
   }

   public void modificarParametroContable() {
      if (guardado == true) {
         guardado = false;
      }
      cambiosParametro = true;
      modificacionParametro = true;
      indexParametroContable = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void modificarParametroContable(String confirmarCambio, String valorConfirmar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (valorConfirmar.equals("EMPRESA")) {
         parametroContableActual.getEmpresaRegistro().setNombre(auxParametroEmpresa);
         for (int i = 0; i < lovEmpresas.size(); i++) {
            if (lovEmpresas.get(i).getNombre().startsWith(confirmarCambio.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            parametroContableActual.setEmpresaRegistro(lovEmpresas.get(indiceUnicoElemento));
            parametroContableActual.setEmpresaCodigo(lovEmpresas.get(indiceUnicoElemento).getCodigo());
            lovEmpresas.clear();
            getLovEmpresas();
            if (guardado == true) {
               guardado = false;
            }
            cambiosParametro = true;
            modificacionParametro = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:parametroEmpresa");
         } else {
            permitirIndexParametro = false;
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
         }
      } else if (valorConfirmar.equals("PROCESO")) {
         if (!confirmarCambio.isEmpty()) {
            parametroContableActual.getProceso().setDescripcion(auxParametroProceso);
            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(confirmarCambio.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroContableActual.setProceso(lovProcesos.get(indiceUnicoElemento));
               lovProcesos.clear();
               getLovProcesos();
               if (guardado == true) {
                  guardado = false;
               }
               cambiosParametro = true;
               modificacionParametro = true;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
               RequestContext.getCurrentInstance().update("form:parametroProceso");
            } else {
               permitirIndexParametro = false;
               RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
            }
         } else {
            parametroContableActual.setProceso(new Procesos());
            if (guardado == true) {
               guardado = false;
            }
            cambiosParametro = true;
            modificacionParametro = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:parametroProceso");
         }
      } else if (valorConfirmar.equals("EMPLDESDE")) {
         if (!confirmarCambio.isEmpty()) {
            parametroContableActual.setStrCodigoEmpleadoDesde(auxParametroEmplDesde);
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(confirmarCambio.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroContableActual.setCodigoempleadodesde(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
               lovEmpleados.clear();
               getLovEmpleados();
               if (guardado == true) {
                  guardado = false;
               }
               cambiosParametro = true;
               modificacionParametro = true;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
               RequestContext.getCurrentInstance().update("form:parametroProceso");
            } else {
               permitirIndexParametro = false;
               RequestContext.getCurrentInstance().update("formEmplDialogo:EmpleadoDesdeDialogo");
               RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
            }
         } else {
            parametroContableActual.setCodigoempleadodesde(null);
            if (guardado == true) {
               guardado = false;
            }
            cambiosParametro = true;
            modificacionParametro = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:parametroEmplDesde");
         }
      } else if (valorConfirmar.equals("EMPLHASTA")) {
         if (!confirmarCambio.isEmpty()) {
            parametroContableActual.setStrCodigoEmpleadoHasta(auxParametroEmplHasta);
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(confirmarCambio.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroContableActual.setCodigoempleadohasta(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
               lovEmpleados.clear();
               getLovEmpleados();
               if (guardado == true) {
                  guardado = false;
               }
               cambiosParametro = true;
               modificacionParametro = true;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
               RequestContext.getCurrentInstance().update("form:parametroProceso");
            } else {
               permitirIndexParametro = false;
               RequestContext.getCurrentInstance().update("formEmplDialogo:EmpleadoHastaDialogo");
               RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
            }
         } else {
            parametroContableActual.setCodigoempleadohasta(null);
            if (guardado == true) {
               guardado = false;
            }
            cambiosParametro = true;
            modificacionParametro = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:parametroEmplHasta");
         }
      }
   }

   public void modificarFechasParametro(int i) {
      RequestContext context = RequestContext.getCurrentInstance();
      if (parametroContableActual.getFechainicialcontabilizacion() != null && parametroContableActual.getFechafinalcontabilizacion() != null) {
         boolean validacion = validarFechaParametro(0);
         if (validacion == true) {
            cambiarIndiceParametro(i);
            parametroContableActual.setFechacontabilizacion(parametroContableActual.getFechafinalcontabilizacion());
            modificarParametroContable();
            RequestContext.getCurrentInstance().update("form:panelParametro");
         } else {
            parametroContableActual.setFechafinalcontabilizacion(auxParametroFechaFinal);
            parametroContableActual.setFechainicialcontabilizacion(auxParametroFechaInicial);
            RequestContext.getCurrentInstance().update("form:panelParametro:parametroFechaFinal");
            RequestContext.getCurrentInstance().update("form:panelParametro:parametroFechaInicial");
            parametroContableActual.setFechacontabilizacion(parametroContableActual.getFechafinalcontabilizacion());
            RequestContext.getCurrentInstance().update("form:panelParametro");
            RequestContext.getCurrentInstance().execute("PF('errorFechasParametro').show()");
         }
      } else {
         parametroContableActual.setFechafinalcontabilizacion(auxParametroFechaFinal);
         parametroContableActual.setFechainicialcontabilizacion(auxParametroFechaInicial);
         RequestContext.getCurrentInstance().update("form:panelParametro:parametroFechaFinal");
         RequestContext.getCurrentInstance().update("form:panelParametro:parametroFechaInicial");
         RequestContext.getCurrentInstance().execute("PF('errorFechasNull').show()");
      }
   }

   public boolean validarFechaParametro(int i) {
      fechaDeParametro = new Date();
      fechaDeParametro.setYear(0);
      fechaDeParametro.setMonth(1);
      fechaDeParametro.setDate(1);
      boolean retorno = true;
      if (i == 0) {
         if (parametroContableActual.getFechainicialcontabilizacion().after(fechaDeParametro) && parametroContableActual.getFechafinalcontabilizacion().after(fechaDeParametro)) {
            if (parametroContableActual.getFechafinalcontabilizacion().after(parametroContableActual.getFechainicialcontabilizacion())) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoParametroContable.getFechainicialcontabilizacion().after(fechaDeParametro) && nuevoParametroContable.getFechafinalcontabilizacion().after(fechaDeParametro)) {
            if (nuevoParametroContable.getFechafinalcontabilizacion().after(nuevoParametroContable.getFechainicialcontabilizacion())) {
               retorno = true;
            } else {
               retorno = false;
            }
         } else {
            retorno = false;
         }
      }
      return retorno;
   }

   public void posicionGenerado() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      indexGenerado = indice;
      cualCeldaGenerado = columna;
      indexParametroContable = -1;
      indexIntercon = -1;
      if (banderaIntercon == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaIntercon = "75";
         interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
         interEmpleado.setFilterStyle("display: none; visibility: hidden;");
         interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
         interTercero.setFilterStyle("display: none; visibility: hidden;");
         interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
         interCuenta.setFilterStyle("display: none; visibility: hidden;");
         interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
         interDebito.setFilterStyle("display: none; visibility: hidden;");
         interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
         interCredito.setFilterStyle("display: none; visibility: hidden;");
         interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
         interConcepto.setFilterStyle("display: none; visibility: hidden;");
         interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
         interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
         interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
         interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
         interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
         interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
         interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
         interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
         interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
         interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
         interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
         interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIntercon");
         banderaIntercon = 0;
         filtrarListaInterconDynamics = null;
         tipoListaIntercon = 0;
      }
   }

   public void posicionIntercon() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      indexIntercon = indice;
      cualCeldaIntercon = columna;
      indexParametroContable = -1;
      indexGenerado = -1;
      if (tipoListaIntercon == 0) {
         secRegistro = listaInterconDynamics.get(indexIntercon).getSecuencia();
      } else {
         secRegistro = filtrarListaInterconDynamics.get(indexIntercon).getSecuencia();
      }
      if (banderaGenerado == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaGenerada = "75";
         genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
         genProceso.setFilterStyle("display: none; visibility: hidden;");
         genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
         genEmpleado.setFilterStyle("display: none; visibility: hidden;");
         genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
         genCntCredito.setFilterStyle("display: none; visibility: hidden;");
         genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
         genCntDebito.setFilterStyle("display: none; visibility: hidden;");
         genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
         genTercero.setFilterStyle("display: none; visibility: hidden;");
         genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
         genValor.setFilterStyle("display: none; visibility: hidden;");
         genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
         genConcepto.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGenerados");
         banderaGenerado = 0;
         filtrarListaGenerados = null;
         tipoListaGenerada = 0;
      }
   }

   public void cambiarIndiceParametro(int indice) {
      System.out.println("Entre al metodo");
      if (permitirIndexParametro == true) {
         indexParametroContable = indice;
         indexGenerado = -1;
         indexIntercon = -1;
         auxParametroEmpresa = parametroContableActual.getEmpresaRegistro().getNombre();
         auxParametroProceso = parametroContableActual.getProceso().getDescripcion();
         auxParametroFechaFinal = parametroContableActual.getFechafinalcontabilizacion();
         auxParametroFechaInicial = parametroContableActual.getFechainicialcontabilizacion();
         auxParametroEmplDesde = parametroContableActual.getStrCodigoEmpleadoDesde();
         auxParametroEmplHasta = parametroContableActual.getStrCodigoEmpleadoHasta();
         if (banderaGenerado == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGenerada = "75";
            genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
            genProceso.setFilterStyle("display: none; visibility: hidden;");
            genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
            genEmpleado.setFilterStyle("display: none; visibility: hidden;");
            genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
            genCntCredito.setFilterStyle("display: none; visibility: hidden;");
            genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
            genCntDebito.setFilterStyle("display: none; visibility: hidden;");
            genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
            genTercero.setFilterStyle("display: none; visibility: hidden;");
            genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
            genValor.setFilterStyle("display: none; visibility: hidden;");
            genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
            genConcepto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            banderaGenerado = 0;
            filtrarListaGenerados = null;
            tipoListaGenerada = 0;
         }
         if (banderaIntercon == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaIntercon = "75";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("display: none; visibility: hidden;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("display: none; visibility: hidden;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("display: none; visibility: hidden;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("display: none; visibility: hidden;");
            interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
            interCredito.setFilterStyle("display: none; visibility: hidden;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("display: none; visibility: hidden;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
            interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
            interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
            interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
            interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
            interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
            interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
            interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
            interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
            interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconDynamics = null;
            tipoListaIntercon = 0;
         }
         System.out.println("indexParametroContable : " + indexParametroContable);
      }
   }

   public String msnPaso1() {
      if (parametroContableActual.getProceso().getSecuencia() != null) {
         msnPaso1 = parametroContableActual.getProceso().getDescripcion().toUpperCase();
      } else {
         msnPaso1 = "TODOS";
      }
      return msnPaso1;
   }

   public void inicioCerrarPeriodoContable() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         int contador = administrarInterfaseDynamicsSX.contarProcesosContabilizadosInterconDynamics(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
         if (contador != -1) {
            if (contador == 0) {
               RequestContext.getCurrentInstance().execute("PF('contadoCeroPerContable').show()");
            } else {
               RequestContext.getCurrentInstance().update("form:paso1CerrarPeriodo");
               RequestContext.getCurrentInstance().execute("PF('paso1CerrarPeriodo').show()");
            }
         }
      } catch (Exception e) {
         System.out.println("Error cerrarPeriodoContable Controlador : " + e.toString());
      }
   }

   public void finCerrarPeriodoContable() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         guardadoGeneral();
         administrarInterfaseDynamicsSX.cerrarProcesoContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
         listaGenerados = null;
         if (listaGenerados == null) {
            listaGenerados = administrarInterfaseDynamicsSX.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (listaGenerados != null) {
               if (listaGenerados.size() > 0) {
                  activarEnviar = false;
               } else {
                  activarDeshacer = true;
               }

            } else {
               activarDeshacer = true;
            }
            getTotalCGenerado();
            getTotalDGenerado();
         }
         listaInterconDynamics = null;
         if (listaInterconDynamics == null) {
            listaInterconDynamics = administrarInterfaseDynamicsSX.obtenerInterconDynamicsParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (listaInterconDynamics != null) {
               if (listaInterconDynamics.size() > 0) {
                  activarDeshacer = false;
               } else {
                  activarDeshacer = true;
               }
            } else {
               activarDeshacer = true;
            }
            getTotalCInter();
            getTotalDInter();
         }

         RequestContext.getCurrentInstance().update("form:btnEnviar");
         RequestContext.getCurrentInstance().update("form:btnDeshacer");
         RequestContext.getCurrentInstance().update("form:PLANO");

         RequestContext.getCurrentInstance().update("form:totalDGenerado");
         RequestContext.getCurrentInstance().update("form:totalCGenerado");
         RequestContext.getCurrentInstance().update("form:totalDInter");
         RequestContext.getCurrentInstance().update("form:totalCInter");
         if (banderaGenerado == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGenerada = "75";
            genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
            genProceso.setFilterStyle("display: none; visibility: hidden;");
            genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
            genEmpleado.setFilterStyle("display: none; visibility: hidden;");
            genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
            genCntCredito.setFilterStyle("display: none; visibility: hidden;");
            genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
            genCntDebito.setFilterStyle("display: none; visibility: hidden;");
            genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
            genTercero.setFilterStyle("display: none; visibility: hidden;");
            genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
            genValor.setFilterStyle("display: none; visibility: hidden;");
            genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
            genConcepto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            banderaGenerado = 0;
            filtrarListaGenerados = null;
            tipoListaGenerada = 0;
         }
         if (banderaIntercon == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaIntercon = "75";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("display: none; visibility: hidden;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("display: none; visibility: hidden;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("display: none; visibility: hidden;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("display: none; visibility: hidden;");
            interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
            interCredito.setFilterStyle("display: none; visibility: hidden;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("display: none; visibility: hidden;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
            interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
            interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
            interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
            interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
            interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
            interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
            interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
            interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
            interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconDynamics = null;
            tipoListaIntercon = 0;
         }
         RequestContext.getCurrentInstance().update("form:datosGenerados");
         RequestContext.getCurrentInstance().update("form:datosIntercon");
      } catch (Exception e) {
         System.out.println("Error finCerrarPeriodoContable Controlador : " + e.toString());
      }
   }

   public void actionBtnGenerarPlano() {
      try {
         guardadoGeneral();
         String descripcionProceso = administrarInterfaseDynamicsSX.obtenerDescripcionProcesoArchivo(parametroContableActual.getProceso().getSecuencia());
         String nombreArchivo = "Interfase_DYNAMICS_" + descripcionProceso;
         administrarInterfaseDynamicsSX.ejecutarPKGCrearArchivoPlano(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), descripcionProceso, nombreArchivo, parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());

      } catch (Exception e) {
         System.out.println("Error actionBtnGenerarPlano Control : " + e.toString());

      } finally {
         actualUsuarioBD = null;
         getActualUsuarioBD();
         listaParametrosContables = null;
         getListaParametrosContables();
         parametroContableActual = null;
         getParametroContableActual();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:PanelTotal");
      }
   }

   public void actionBtnRecontabilizar() {
      Integer contador = administrarInterfaseDynamicsSX.conteoContabilizacionesDynamics(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
      if (contador != null) {
         if (contador != 0) {
            RequestContext.getCurrentInstance().execute("PF('paso1Recon').show()");
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorRecon').show()");
         }
      }
   }

   public void ejecutarPaso3Recon() {
      if (parametroContableActual.getFechafinalcontabilizacion() != null && parametroContableActual.getFechainicialcontabilizacion() != null) {
         DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
         String fechaI = df.format(parametroContableActual.getFechainicialcontabilizacion());
         String fechaF = df.format(parametroContableActual.getFechafinalcontabilizacion());
         fechaFinRecon = fechaF;
         fechaIniRecon = fechaI;
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:paso3Recon");
         RequestContext.getCurrentInstance().execute("PF('paso3Recon').show()");
      }
   }

   public void finalizarProcesoRecontabilizacion() {
      try {
         guardadoGeneral();
         administrarInterfaseDynamicsSX.ejecutarPKGRecontabilizar(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
      } catch (Exception e) {
         System.out.println("Error finalizarProcesoRecontabilizacion Controlador : " + e.toString());
      }

   }

   public void actionBtnDeshacer() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         guardadoGeneral();
         administrarInterfaseDynamicsSX.actualizarFlagContabilizacionDeshacerDynamics(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
         administrarInterfaseDynamicsSX.deleteInterconDynamics(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
         administrarInterfaseDynamicsSX.actualizarFlagContabilizacionDeshacerDynamics_NOT_EXITS(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
         listaGenerados = null;
         if (listaGenerados == null) {
            listaGenerados = administrarInterfaseDynamicsSX.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (listaGenerados != null) {
               if (listaGenerados.size() > 0) {
                  activarEnviar = false;
               } else {
                  activarDeshacer = true;
               }

            } else {
               activarDeshacer = true;
            }
            getTotalCGenerado();
            getTotalDGenerado();
         }
         listaInterconDynamics = null;
         if (listaInterconDynamics == null) {
            listaInterconDynamics = administrarInterfaseDynamicsSX.obtenerInterconDynamicsParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (listaInterconDynamics != null) {
               if (listaInterconDynamics.size() > 0) {
                  activarDeshacer = false;
               } else {
                  activarDeshacer = true;
               }
            } else {
               activarDeshacer = true;
            }
            getTotalCInter();
            getTotalDInter();
         }

         RequestContext.getCurrentInstance().update("form:btnEnviar");
         RequestContext.getCurrentInstance().update("form:btnDeshacer");
         RequestContext.getCurrentInstance().update("form:PLANO");

         RequestContext.getCurrentInstance().update("form:totalDGenerado");
         RequestContext.getCurrentInstance().update("form:totalCGenerado");
         RequestContext.getCurrentInstance().update("form:totalDInter");
         RequestContext.getCurrentInstance().update("form:totalCInter");
         if (banderaGenerado == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaGenerada = "75";
            genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
            genProceso.setFilterStyle("display: none; visibility: hidden;");
            genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
            genEmpleado.setFilterStyle("display: none; visibility: hidden;");
            genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
            genCntCredito.setFilterStyle("display: none; visibility: hidden;");
            genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
            genCntDebito.setFilterStyle("display: none; visibility: hidden;");
            genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
            genTercero.setFilterStyle("display: none; visibility: hidden;");
            genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
            genValor.setFilterStyle("display: none; visibility: hidden;");
            genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
            genConcepto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            banderaGenerado = 0;
            filtrarListaGenerados = null;
            tipoListaGenerada = 0;
         }
         if (banderaIntercon == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            altoTablaIntercon = "75";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("display: none; visibility: hidden;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("display: none; visibility: hidden;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("display: none; visibility: hidden;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("display: none; visibility: hidden;");
            interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
            interCredito.setFilterStyle("display: none; visibility: hidden;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("display: none; visibility: hidden;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
            interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
            interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
            interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
            interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
            interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
            interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
            interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
            interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
            interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconDynamics = null;
            tipoListaIntercon = 0;
         }
         RequestContext.getCurrentInstance().update("form:datosGenerados");
         RequestContext.getCurrentInstance().update("form:datosIntercon");
      } catch (Exception e) {
         System.out.println("Error actionBtnDeshacer Controlador : " + e.toString());
      }
   }

   public void actionBtnEnviar() {
      Date fechaDesde = administrarInterfaseDynamicsSX.buscarFechaDesdeVWActualesFechas();
      Date fechaHasta = administrarInterfaseDynamicsSX.buscarFechaHastaVWActualesFechas();
      RequestContext context = RequestContext.getCurrentInstance();
      if (fechaDesde != null && fechaHasta != null) {
         if ((fechaDesde.before(parametroContableActual.getFechainicialcontabilizacion()) && fechaHasta.after(parametroContableActual.getFechafinalcontabilizacion()))
                 || (fechaDesde.before(parametroContableActual.getFechainicialcontabilizacion()) && fechaHasta.after(parametroContableActual.getFechafinalcontabilizacion()))) {
            RequestContext.getCurrentInstance().execute("PF('errorVWActualesFechas').show()");
         } else if (parametroContableActual.getEmpresaRegistro().getSecuencia() != null
                 && parametroContableActual.getFechafinalcontabilizacion() != null
                 && parametroContableActual.getFechainicialcontabilizacion() != null) {
            guardadoGeneral();
            administrarInterfaseDynamicsSX.ejecutarPKGUbicarnuevointercon_DYNAMICS(parametroContableActual.getSecuencia(), parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia(), parametroContableActual.getCodigoempleadodesde(), parametroContableActual.getCodigoempleadohasta());
            listaGenerados = null;
            if (listaGenerados == null) {
               listaGenerados = administrarInterfaseDynamicsSX.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
               if (listaGenerados != null) {
                  if (listaGenerados.size() > 0) {
                     activarEnviar = false;
                  } else {
                     activarDeshacer = true;
                  }
               } else {
                  activarDeshacer = true;
               }
               getTotalCGenerado();
               getTotalDGenerado();
            }
            listaInterconDynamics = null;
            if (listaInterconDynamics == null) {
               listaInterconDynamics = administrarInterfaseDynamicsSX.obtenerInterconDynamicsParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
               if (listaInterconDynamics != null) {
                  if (listaInterconDynamics.size() > 0) {
                     activarDeshacer = false;
                  } else {
                     activarDeshacer = true;
                  }
               } else {
                  activarDeshacer = true;
               }
               getTotalCInter();
               getTotalDInter();
            }
            RequestContext.getCurrentInstance().update("form:totalDGenerado");
            RequestContext.getCurrentInstance().update("form:totalCGenerado");
            RequestContext.getCurrentInstance().update("form:totalDInter");
            RequestContext.getCurrentInstance().update("form:totalCInter");

            RequestContext.getCurrentInstance().update("form:btnEnviar");
            RequestContext.getCurrentInstance().update("form:btnDeshacer");
            RequestContext.getCurrentInstance().update("form:PLANO");
            if (banderaGenerado == 1) {
               FacesContext c = FacesContext.getCurrentInstance();
               altoTablaGenerada = "75";
               genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
               genProceso.setFilterStyle("display: none; visibility: hidden;");
               genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
               genEmpleado.setFilterStyle("display: none; visibility: hidden;");
               genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
               genCntCredito.setFilterStyle("display: none; visibility: hidden;");
               genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
               genCntDebito.setFilterStyle("display: none; visibility: hidden;");
               genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
               genTercero.setFilterStyle("display: none; visibility: hidden;");
               genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
               genValor.setFilterStyle("display: none; visibility: hidden;");
               genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
               genConcepto.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosGenerados");
               banderaGenerado = 0;
               filtrarListaGenerados = null;
               tipoListaGenerada = 0;
            }
            if (banderaIntercon == 1) {
               FacesContext c = FacesContext.getCurrentInstance();
               altoTablaIntercon = "75";
               interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
               interEmpleado.setFilterStyle("display: none; visibility: hidden;");
               interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
               interTercero.setFilterStyle("display: none; visibility: hidden;");
               interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
               interCuenta.setFilterStyle("display: none; visibility: hidden;");
               interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
               interDebito.setFilterStyle("display: none; visibility: hidden;");
               interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
               interCredito.setFilterStyle("display: none; visibility: hidden;");
               interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
               interConcepto.setFilterStyle("display: none; visibility: hidden;");
               interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
               interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
               interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
               interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
               interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
               interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
               interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
               interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
               interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
               interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
               interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
               interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
               RequestContext.getCurrentInstance().update("form:datosIntercon");
               banderaIntercon = 0;
               filtrarListaInterconDynamics = null;
               tipoListaIntercon = 0;
            }
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
         }
      }
   }

   public void anularComprobantesCerrados() {
      try {
         guardadoGeneral();
         administrarInterfaseDynamicsSX.anularComprobantesCerrados(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
         FacesMessage msg = new FacesMessage("Información", "Se realizo el proceso con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error anularComprobantesCerrados Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el proceso de anulacion");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void validarFechasProcesoActualizar() {
      Date fechaContabilizacion = administrarInterfaseDynamicsSX.obtenerFechaMaxContabilizaciones();
      Date fechaInterconTotal = administrarInterfaseDynamicsSX.obtenerFechaMaxInterconDynamics();
      DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
      RequestContext context = RequestContext.getCurrentInstance();
      if (fechaContabilizacion != null && fechaInterconTotal != null) {
         if (fechaContabilizacion.compareTo(fechaInterconTotal) == 0) {
            String fecha = df.format(fechaContabilizacion);
            msnFechasActualizar = fecha;
            RequestContext.getCurrentInstance().update("form:anteriorContabilizacion");
            RequestContext.getCurrentInstance().execute("PF('anteriorContabilizacion').show()");
         } else {
            RequestContext.getCurrentInstance().update("form:nuncaContabilizo");
            RequestContext.getCurrentInstance().execute("PF('nuncaContabilizo').show()");
         }
      }

   }

   public boolean validarFechasParametros() {
      boolean retorno = false;
      ParametrosEstructuras parametroLiquidacion = administrarInterfaseDynamicsSX.parametrosLiquidacion();
      if ((parametroLiquidacion.getFechadesdecausado().compareTo(parametroContableActual.getFechainicialcontabilizacion()) == 0)
              && (parametroLiquidacion.getFechahastacausado().compareTo(parametroContableActual.getFechafinalcontabilizacion()) == 0)) {
         retorno = true;
      }
      return retorno;
   }

   public void actionBtnActualizar() {
      RequestContext context = RequestContext.getCurrentInstance();
      boolean validar = validarFechasParametros();
      if (validar == true) {
         //guardadoGeneral();

         listaGenerados = null;
         if (listaGenerados == null) {
            listaGenerados = administrarInterfaseDynamicsSX.obtenerSolucionesNodosParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (listaGenerados != null) {
               if (listaGenerados.size() > 0) {
                  activarEnviar = false;
               } else {
                  activarEnviar = true;
               }
            } else {
               activarEnviar = true;
            }
            getTotalCGenerado();
            getTotalDGenerado();
         }
         /*
           
            
          */
         listaInterconDynamics = null;
         if (listaInterconDynamics == null) {
            listaInterconDynamics = administrarInterfaseDynamicsSX.obtenerInterconDynamicsParametroContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion());
            if (listaInterconDynamics != null) {
               if (listaInterconDynamics.size() > 0) {
                  activarDeshacer = false;
               } else {
                  activarDeshacer = true;
               }
            } else {
               activarDeshacer = true;
            }
            getTotalCInter();
            getTotalDInter();
         }

         RequestContext.getCurrentInstance().update("form:PanelTotal");
         int tam1 = 0;
         int tam2 = 0;
         if (listaGenerados != null) {
            tam1 = listaGenerados.size();
         }
         if (listaInterconDynamics != null) {
            tam2 = listaInterconDynamics.size();
         }
         if (tam1 == 0 && tam2 == 0) {
            RequestContext.getCurrentInstance().execute("PF('procesoSinDatos').show()");
         }
         //validarFechasProcesoActualizar();

         System.out.println("I finish");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorFechasParametros').show()");
      }
   }

   public void guardarSalir() {
      guardadoGeneral();
      salir();
   }

   public void cancelarSalir() {
      cancelarModificaciones();
      salir();
   }

   public void guardadoGeneral() {
      if (guardado == false) {
         if (cambiosParametro == true) {
            guardarCambiosParametro();
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void guardarCambiosParametro() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (modificacionParametro == true) {
            administrarInterfaseDynamicsSX.modificarParametroContable(parametroContableActual);
            modificacionParametro = false;
         }
         if (!listParametrosContablesBorrar.isEmpty()) {
            for (int i = 0; i < listParametrosContablesBorrar.size(); i++) {
               administrarInterfaseDynamicsSX.borrarParametroContable(listParametrosContablesBorrar);
            }
            listParametrosContablesBorrar.clear();
         }
         listaParametrosContables = null;
         getListaParametrosContables();
         parametroContableActual = null;
         getParametroContableActual();
         cambiosParametro = false;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:PanelTotal");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosParametro Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Un error ha ocurrido en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificaciones() {
      if (banderaGenerado == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaGenerada = "75";
         genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
         genProceso.setFilterStyle("display: none; visibility: hidden;");
         genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
         genEmpleado.setFilterStyle("display: none; visibility: hidden;");
         genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
         genCntCredito.setFilterStyle("display: none; visibility: hidden;");
         genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
         genCntDebito.setFilterStyle("display: none; visibility: hidden;");
         genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
         genTercero.setFilterStyle("display: none; visibility: hidden;");
         genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
         genValor.setFilterStyle("display: none; visibility: hidden;");
         genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
         genConcepto.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGenerados");
         banderaGenerado = 0;
         filtrarListaGenerados = null;
         tipoListaGenerada = 0;
      }
      if (banderaIntercon == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaIntercon = "75";
         interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
         interEmpleado.setFilterStyle("display: none; visibility: hidden;");
         interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
         interTercero.setFilterStyle("display: none; visibility: hidden;");
         interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
         interCuenta.setFilterStyle("display: none; visibility: hidden;");
         interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
         interDebito.setFilterStyle("display: none; visibility: hidden;");
         interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
         interCredito.setFilterStyle("display: none; visibility: hidden;");
         interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
         interConcepto.setFilterStyle("display: none; visibility: hidden;");
         interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
         interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
         interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
         interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
         interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
         interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
         interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
         interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
         interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
         interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
         interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
         interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIntercon");
         banderaIntercon = 0;
         filtrarListaInterconDynamics = null;
         tipoListaIntercon = 0;
      }
      totalCGenerado = 0;
      totalDGenerado = 0;
      totalDInter = 0;
      totalCInter = 0;
      activarEnviar = true;
      activarDeshacer = true;
      modificacionParametro = false;
      aceptar = true;
      listParametrosContablesBorrar.clear();
      actualUsuarioBD = null;
      getActualUsuarioBD();
      listaParametrosContables = null;
      getListaParametrosContables();
      parametroContableActual = null;
      listaGenerados = null;
      listaInterconDynamics = null;
      getParametroContableActual();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:btnEnviar");
      RequestContext.getCurrentInstance().update("form:btnDeshacer");
      RequestContext.getCurrentInstance().update("form:PLANO");
      RequestContext.getCurrentInstance().update("form:PanelTotal");
      cambiosParametro = false;
      guardado = true;
      indexParametroContable = -1;
      indexGenerado = -1;
      indexIntercon = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (indexParametroContable >= 0) {
         if (indexParametroContable == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaParametro");
            RequestContext.getCurrentInstance().execute("PF('editarEmpresaParametro').show()");
            indexParametroContable = -1;
         } else if (indexParametroContable == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoParametro");
            RequestContext.getCurrentInstance().execute("PF('editarProcesoParametro').show()");
            indexParametroContable = -1;
         } else if (indexParametroContable == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialParametro");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialParametro').show()");
            indexParametroContable = -1;
         } else if (indexParametroContable == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalParametro");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalParametro').show()");
            indexParametroContable = -1;
         } else if (indexParametroContable == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaContaParametro");
            RequestContext.getCurrentInstance().execute("PF('editarFechaContaParametro').show()");
            indexParametroContable = -1;
         } else if (indexParametroContable == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmplDesdeParametro");
            RequestContext.getCurrentInstance().execute("PF('editarEmplDesdeParametro').show()");
            indexParametroContable = -1;
         } else if (indexParametroContable == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmplHastaParametro");
            RequestContext.getCurrentInstance().execute("PF('editarEmplHastaParametro').show()");
            indexParametroContable = -1;
         } else if (indexParametroContable == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarChequeraParametro");
            RequestContext.getCurrentInstance().execute("PF('editarChequeraParametro').show()");
            indexParametroContable = -1;
         } else if (indexParametroContable == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarRespuestaParametro");
            RequestContext.getCurrentInstance().execute("PF('editarRespuestaParametro').show()");
            indexParametroContable = -1;
         }

      }
      if (indexGenerado >= 0) {
         if (tipoListaGenerada == 0) {
            editarGenerado = listaGenerados.get(indexGenerado);
         } else {
            editarGenerado = filtrarListaGenerados.get(indexGenerado);
         }
         if (cualCeldaGenerado == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoGenerado");
            RequestContext.getCurrentInstance().execute("PF('editarProcesoGenerado').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadoGenerado");
            RequestContext.getCurrentInstance().execute("PF('editarEmpleadoGenerado').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCreditoGenerado");
            RequestContext.getCurrentInstance().execute("PF('editarCreditoGenerado').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDebitoGenerado");
            RequestContext.getCurrentInstance().execute("PF('editarDebitoGenerado').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroGenerado");
            RequestContext.getCurrentInstance().execute("PF('editarTerceroGenerado').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValorGenerado");
            RequestContext.getCurrentInstance().execute("PF('editarValorGenerado').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoGenerado");
            RequestContext.getCurrentInstance().execute("PF('editarConceptoGenerado').show()");
            cualCeldaGenerado = -1;
         }
         indexGenerado = -1;
      }
      if (indexIntercon >= 0) {
         if (tipoListaIntercon == 0) {
            editarIntercon = listaInterconDynamics.get(indexIntercon);
         } else {
            editarIntercon = filtrarListaInterconDynamics.get(indexIntercon);
         }
         if (cualCeldaIntercon == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadoIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarEmpleadoIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTerceroIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarTerceroIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCuentaIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarCuentaIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDebitoIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarDebitoIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCreditoIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarCreditoIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarConceptoIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCentroCostoIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarCentroCostoIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVencimientoIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarFechaVencimientoIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValorOriginalDebitoIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarValorOriginalDebitoIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarValorOriginalCreditoIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarValorOriginalCreditoIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 10) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarPreRadicacionIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarPreRadicacionIntercon').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 11) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodAlternativoIntercon");
            RequestContext.getCurrentInstance().execute("PF('editarCodAlternativoIntercon').show()");
            cualCeldaIntercon = -1;
         }
         indexIntercon = -1;
      }
   }

   public void salir() {  limpiarListasValor();
      if (banderaGenerado == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaGenerada = "75";
         genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
         genProceso.setFilterStyle("display: none; visibility: hidden;");
         genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
         genEmpleado.setFilterStyle("display: none; visibility: hidden;");
         genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
         genCntCredito.setFilterStyle("display: none; visibility: hidden;");
         genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
         genCntDebito.setFilterStyle("display: none; visibility: hidden;");
         genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
         genTercero.setFilterStyle("display: none; visibility: hidden;");
         genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
         genValor.setFilterStyle("display: none; visibility: hidden;");
         genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
         genConcepto.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGenerados");
         banderaGenerado = 0;
         filtrarListaGenerados = null;
         tipoListaGenerada = 0;
      }
      if (banderaIntercon == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaIntercon = "75";
         interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
         interEmpleado.setFilterStyle("display: none; visibility: hidden;");
         interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
         interTercero.setFilterStyle("display: none; visibility: hidden;");
         interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
         interCuenta.setFilterStyle("display: none; visibility: hidden;");
         interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
         interDebito.setFilterStyle("display: none; visibility: hidden;");
         interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
         interCredito.setFilterStyle("display: none; visibility: hidden;");
         interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
         interConcepto.setFilterStyle("display: none; visibility: hidden;");
         interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
         interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
         interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
         interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
         interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
         interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
         interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
         interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
         interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
         interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
         interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
         interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIntercon");
         banderaIntercon = 0;
         filtrarListaInterconDynamics = null;
         tipoListaIntercon = 0;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      listParametrosContablesBorrar.clear();
      modificacionParametro = false;
      listaParametrosContables = null;
      getListaParametrosContables();
      parametroContableActual = null;
      listaGenerados = null;
      listaInterconDynamics = null;
      actualUsuarioBD = null;
      cambiosParametro = false;
      guardado = true;
      indexParametroContable = -1;
      indexGenerado = -1;
      indexIntercon = -1;
      activarEnviar = true;
      activarDeshacer = true;
      totalCGenerado = 0;
      totalDGenerado = 0;
      totalDInter = 0;
      totalCInter = 0;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void asignarIndex(Integer indice, int numeroDialogo, int tipoNuevo) {
      tipoActualizacion = tipoNuevo;
      indexParametroContable = indice;
      RequestContext context = RequestContext.getCurrentInstance();
      if (numeroDialogo == 0) {
         RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
      } else if (numeroDialogo == 1) {
         RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
      } else if (numeroDialogo == 2) {
         RequestContext.getCurrentInstance().update("formEmplDialogo:EmpleadoDesdeDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
      } else if (numeroDialogo == 3) {
         RequestContext.getCurrentInstance().update("formEmplDialogo:EmpleadoHastaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
      }
   }

   public void actualizarEmpresa() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         parametroContableActual.setEmpresaRegistro(empresaSeleccionada);
         parametroContableActual.setEmpresaCodigo(empresaSeleccionada.getCodigo());
         indexParametroContable = -1;
         if (guardado == true) {
            guardado = false;
         }
         modificacionParametro = true;
         cambiosParametro = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:parametroEmpresa");
      }
      if (tipoActualizacion == 1) {
         nuevoParametroContable.setEmpresaRegistro(empresaSeleccionada);
         nuevoParametroContable.setEmpresaCodigo(empresaSeleccionada.getCodigo());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresaParametro");
      }
      empresaSeleccionada = new Empresas();
      filtrarLovEmpresas = null;
      aceptar = true;/*
        RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
        RequestContext.getCurrentInstance().update("form:lovEmpresa");
        RequestContext.getCurrentInstance().update("form:aceptarE");*/
      context.reset("form:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void cancelarEmpresa() {
      empresaSeleccionada = new Empresas();
      filtrarLovEmpresas = null;
      indexParametroContable = -1;
      permitirIndexParametro = true;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void actualizarProceso() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         parametroContableActual.setProceso(procesoSeleccionado);
         indexParametroContable = -1;
         if (guardado == true) {
            guardado = false;
         }
         modificacionParametro = true;
         cambiosParametro = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:parametroProceso");
      }
      if (tipoActualizacion == 1) {
         nuevoParametroContable.setProceso(procesoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoParametro");
      }
      procesoSeleccionado = new Procesos();
      filtrarLovProcesos = null;
      aceptar = true;/*
         RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
         RequestContext.getCurrentInstance().update("form:lovProceso");
         RequestContext.getCurrentInstance().update("form:aceptarP");*/

      context.reset("form:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
   }

   public void cancelarProceso() {
      aceptar = true;
      procesoSeleccionado = new Procesos();
      filtrarLovProcesos = null;
      indexParametroContable = -1;
      permitirIndexParametro = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
   }

   public void actualizarEmpleadoDesde() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         parametroContableActual.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
         indexParametroContable = -1;
         if (guardado == true) {
            guardado = false;
         }
         modificacionParametro = true;
         cambiosParametro = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:parametroEmplDesde");
      }
      if (tipoActualizacion == 1) {
         nuevoParametroContable.setCodigoempleadodesde(empleadoSeleccionado.getCodigoempleado());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoEmplDesde");
      }
      empleadoSeleccionado = new Empleados();
      filtrarLovEmpleados = null;
      aceptar = true;/*
        RequestContext.getCurrentInstance().update("formEmplDialogo:EmpleadoDesdeDialogo");
        RequestContext.getCurrentInstance().update("formEmplDialogo:lovEmpleadoDesde");
        RequestContext.getCurrentInstance().update("formEmplDialogo:aceptarED");*/
      context.reset("formEmplDialogo:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
   }

   public void cancelarEmpleadoDesde() {
      aceptar = true;
      empleadoSeleccionado = new Empleados();
      filtrarLovEmpleados = null;
      indexParametroContable = -1;
      permitirIndexParametro = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formEmplDialogo:lovEmpleadoDesde:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoDesde').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').hide()");
   }

   public void actualizarEmpleadoHasta() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         parametroContableActual.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
         indexParametroContable = -1;
         if (guardado == true) {
            guardado = false;
         }
         modificacionParametro = true;
         cambiosParametro = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:parametroEmplHasta");
      }
      if (tipoActualizacion == 1) {
         nuevoParametroContable.setCodigoempleadohasta(empleadoSeleccionado.getCodigoempleado());
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoEmplHasta");
      }
      empleadoSeleccionado = new Empleados();
      filtrarLovEmpleados = null;
      aceptar = true;/*
        RequestContext.getCurrentInstance().update("formEmplDialogo:EmpleadoHastaDialogo");
        RequestContext.getCurrentInstance().update("formEmplDialogo:lovEmpleadoHasta");
        RequestContext.getCurrentInstance().update("formEmplDialogo:aceptarEH");*/
      context.reset("formEmplDialogo:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
   }

   public void cancelarEmpleadoHasta() {
      aceptar = true;
      empleadoSeleccionado = new Empleados();
      filtrarLovEmpleados = null;
      indexParametroContable = -1;
      permitirIndexParametro = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formEmplDialogo:lovEmpleadoHasta:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpleadoHasta').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').hide()");
   }

   public void listaValoresBoton() {
      if (indexParametroContable >= 0) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (indexParametroContable == 0) {
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
         }
         if (indexParametroContable == 2) {
            RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
         }
         if (indexParametroContable == 5) {
            RequestContext.getCurrentInstance().update("formEmplDialogo:EmpleadoDesdeDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
         }
         if (indexParametroContable == 6) {
            RequestContext.getCurrentInstance().update("formEmplDialogo:EmpleadoHastaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
         }
      }
   }

   public void valoresBackupAutocompletar() {
      auxParametroEmpresa = nuevoParametroContable.getEmpresaRegistro().getNombre();
      auxParametroProceso = nuevoParametroContable.getProceso().getDescripcion();
      auxParametroEmplDesde = nuevoParametroContable.getStrCodigoEmpleadoDesde();
      auxParametroEmplHasta = nuevoParametroContable.getStrCodigoEmpleadoHasta();
   }

   public void autocompletarNuevo(String procesoCambio, String confirmarCambio) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (procesoCambio.equals("EMPRESA")) {
         nuevoParametroContable.getEmpresaRegistro().setNombre(auxParametroEmpresa);
         for (int i = 0; i < lovEmpresas.size(); i++) {
            if (lovEmpresas.get(i).getNombre().startsWith(confirmarCambio.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            nuevoParametroContable.setEmpresaRegistro(lovEmpresas.get(indiceUnicoElemento));
            nuevoParametroContable.setEmpresaCodigo(lovEmpresas.get(indiceUnicoElemento).getCodigo());
            lovEmpresas.clear();
            getLovEmpresas();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresaParametro");
         } else {
            nuevoParametroContable.getEmpresaRegistro().setNombre(auxParametroEmpresa);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresaParametro");
            permitirIndexParametro = false;
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
         }
      } else if (procesoCambio.equals("PROCESO")) {
         if (!confirmarCambio.isEmpty()) {
            nuevoParametroContable.getProceso().setDescripcion(auxParametroProceso);
            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(confirmarCambio.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               nuevoParametroContable.setProceso(lovProcesos.get(indiceUnicoElemento));
               lovProcesos.clear();
               getLovProcesos();
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoParametro");
            } else {
               nuevoParametroContable.getProceso().setDescripcion(auxParametroProceso);
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoParametro");
               permitirIndexParametro = false;
               RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
            }
         } else {
            nuevoParametroContable.setProceso(new Procesos());
            lovProcesos.clear();
            getLovProcesos();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoParametro");
         }
      } else if (procesoCambio.equals("EMPLDESDE")) {
         if (!confirmarCambio.isEmpty()) {
            nuevoParametroContable.setStrCodigoEmpleadoDesde(auxParametroEmplDesde);
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(confirmarCambio.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               nuevoParametroContable.setCodigoempleadodesde(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
               lovEmpleados.clear();
               getLovEmpleados();
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoEmplDesde");
            } else {
               nuevoParametroContable.setStrCodigoEmpleadoDesde(auxParametroEmplDesde);
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoEmplDesde");
               permitirIndexParametro = false;
               RequestContext.getCurrentInstance().update("formEmplDialogo:EmpleadoDesdeDialogo");
               RequestContext.getCurrentInstance().execute("PF('EmpleadoDesdeDialogo').show()");
            }
         } else {
            nuevoParametroContable.setCodigoempleadodesde(null);
            lovEmpleados.clear();
            getLovEmpleados();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoEmplDesde");
         }
      } else if (procesoCambio.equals("EMPLHASTA")) {
         if (!confirmarCambio.isEmpty()) {
            nuevoParametroContable.setStrCodigoEmpleadoHasta(auxParametroEmplHasta);
            for (int i = 0; i < lovEmpleados.size(); i++) {
               if (lovEmpleados.get(i).getCodigoempleadoSTR().startsWith(confirmarCambio.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               nuevoParametroContable.setCodigoempleadohasta(lovEmpleados.get(indiceUnicoElemento).getCodigoempleado());
               lovEmpleados.clear();
               getLovEmpleados();
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoEmplHasta");
            } else {
               nuevoParametroContable.setStrCodigoEmpleadoHasta(auxParametroEmplHasta);
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoEmplHasta");
               permitirIndexParametro = false;
               RequestContext.getCurrentInstance().update("formEmplDialogo:EmpleadoHastaDialogo");
               RequestContext.getCurrentInstance().execute("PF('EmpleadoHastaDialogo').show()");
            }
         } else {
            nuevoParametroContable.setCodigoempleadohasta(null);
            lovEmpleados.clear();
            getLovEmpleados();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaProcesoEmplHasta");
         }
      }
   }

   public void borrarParametroContable() {
      if (modificacionParametro == true) {
         modificacionParametro = false;
      }
      listaParametrosContables.remove(parametroContableActual);
      listParametrosContablesBorrar.add(parametroContableActual);
      parametroContableActual = null;
      getParametroContableActual();
      if (listaParametrosContables != null) {
         int tam = listaParametrosContables.size();
         if (tam == 0 || tam == 1) {
            estadoBtnAbajo = true;
            estadoBtnArriba = true;
         }
         if (tam > 1) {
            estadoBtnAbajo = false;
            estadoBtnArriba = true;
         }
      }
      activarEnviar = true;
      activarDeshacer = true;
      totalCGenerado = 0;
      totalDGenerado = 0;
      totalDInter = 0;
      totalCInter = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:PanelTotal");
      RequestContext.getCurrentInstance().update("form:btnEnviar");
      RequestContext.getCurrentInstance().update("form:btnDeshacer");
      RequestContext.getCurrentInstance().update("form:PLANO");
   }

   public void modificarFechaFinalNuevoRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      nuevoParametroContable.setFechacontabilizacion(nuevoParametroContable.getFechafinalcontabilizacion());
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFechaContaParametro");
   }

   public void agregarNuevoParametro() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (nuevoParametroContable.getEmpresaRegistro().getSecuencia() != null && nuevoParametroContable.getFechafinalcontabilizacion() != null && nuevoParametroContable.getFechainicialcontabilizacion() != null) {
            boolean validar = validarFechaParametro(1);
            if (validar == true) {
               RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPC').hide()");
               int k = 1;
               BigInteger var = new BigInteger(String.valueOf(k));
               nuevoParametroContable.setSecuencia(var);
               if (nuevoParametroContable.getProceso().getSecuencia() == null) {
                  nuevoParametroContable.setProceso(new Procesos());
               }
               administrarInterfaseDynamicsSX.crearParametroContable(nuevoParametroContable);
               nuevoParametroContable = new ParametrosContables();
               activarAgregar = true;
               activarOtros = false;
               listaParametrosContables = null;
               getListaParametrosContables();
               parametroContableActual = null;
               getParametroContableActual();
               listaGenerados = null;
               listaInterconDynamics = null;
               activarEnviar = true;
               activarDeshacer = true;
               totalCGenerado = 0;
               totalDGenerado = 0;
               totalDInter = 0;
               totalCInter = 0;
               RequestContext.getCurrentInstance().update("form:btnEnviar");
               RequestContext.getCurrentInstance().update("form:btnDeshacer");
               RequestContext.getCurrentInstance().update("form:PLANO");
               if (banderaGenerado == 1) {
                  FacesContext c = FacesContext.getCurrentInstance();
                  altoTablaGenerada = "75";
                  genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
                  genProceso.setFilterStyle("display: none; visibility: hidden;");
                  genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
                  genEmpleado.setFilterStyle("display: none; visibility: hidden;");
                  genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
                  genCntCredito.setFilterStyle("display: none; visibility: hidden;");
                  genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
                  genCntDebito.setFilterStyle("display: none; visibility: hidden;");
                  genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
                  genTercero.setFilterStyle("display: none; visibility: hidden;");
                  genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
                  genValor.setFilterStyle("display: none; visibility: hidden;");
                  genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
                  genConcepto.setFilterStyle("display: none; visibility: hidden;");
                  RequestContext.getCurrentInstance().update("form:datosGenerados");
                  banderaGenerado = 0;
                  filtrarListaGenerados = null;
                  tipoListaGenerada = 0;
               }
               if (banderaIntercon == 1) {
                  FacesContext c = FacesContext.getCurrentInstance();
                  altoTablaIntercon = "75";
                  interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
                  interEmpleado.setFilterStyle("display: none; visibility: hidden;");
                  interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
                  interTercero.setFilterStyle("display: none; visibility: hidden;");
                  interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
                  interCuenta.setFilterStyle("display: none; visibility: hidden;");
                  interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
                  interDebito.setFilterStyle("display: none; visibility: hidden;");
                  interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
                  interCredito.setFilterStyle("display: none; visibility: hidden;");
                  interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
                  interConcepto.setFilterStyle("display: none; visibility: hidden;");
                  interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
                  interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                  interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
                  interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
                  interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
                  interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
                  interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
                  interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
                  interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
                  interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
                  interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
                  interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
                  RequestContext.getCurrentInstance().update("form:datosIntercon");
                  banderaIntercon = 0;
                  filtrarListaInterconDynamics = null;
                  tipoListaIntercon = 0;
               }
               RequestContext.getCurrentInstance().update("form:PanelTotal");
               FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
               FacesContext.getCurrentInstance().addMessage(null, msg);
               RequestContext.getCurrentInstance().update("form:growl");
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorFechasParametro').show()");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorNewRegNull').show()");
         }
      } catch (Exception e) {
         System.out.println("Error Controlador agregarNuevo : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void limpiarNuevoParametro() {
      nuevoParametroContable = new ParametrosContables();
      nuevoParametroContable.setEmpresaRegistro(new Empresas());
      nuevoParametroContable.setProceso(new Procesos());
   }

   public void validarExportPDF() throws IOException {
      if (indexParametroContable >= 0) {
         exportPDF_PC();
      }
      if (indexGenerado >= 0) {
         exportPDF_G();
      }
      if (indexIntercon >= 0) {
         exportPDF_I();
      }
   }

   public void exportPDF_PC() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosParametroExportar");
      FacesContext context = c;
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ParametrosContables_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      indexGenerado = -1;
   }

   public void exportPDF_G() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosGenerarExportar");
      FacesContext context = c;
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "Generados_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      indexGenerado = -1;
   }

   public void exportPDF_I() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosInterconExportar");
      FacesContext context = c;
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "InterconDynamics_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      indexIntercon = -1;
   }

   public void validarExportXLS() throws IOException {
      if (indexParametroContable >= 0) {
         exportXLS_PC();
      }
      if (indexGenerado >= 0) {
         exportXLS_G();
      }
      if (indexIntercon >= 0) {
         exportXLS_I();
      }
   }

   public void exportXLS_PC() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosParametroExportar");
      FacesContext context = c;
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ParametrosContables_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      indexParametroContable = -1;
   }

   public void exportXLS_G() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosGenerarExportar");
      FacesContext context = c;
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "Generados_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      indexGenerado = -1;
   }

   public void exportXLS_I() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosInterconExportar");
      FacesContext context = c;
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "InterconDynamics_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      indexIntercon = -1;
   }

   public String validarExportXML() {
      String tabla = "";
      if (indexParametroContable >= 0) {
         tabla = ":formExportar:datosParametroExportar";
      }
      if (indexGenerado >= 0) {
         tabla = ":formExportar:datosGenerarExportar";
      }
      if (indexIntercon >= 0) {
         tabla = ":formExportar:datosInterconExportar";
      }
      return tabla;
   }

   public String validarNombreExportXML() {
      String nombre = "";
      if (indexParametroContable >= 0) {
         nombre = "ParametrosContables_XML";
      }
      if (indexGenerado >= 0) {
         nombre = "Generados_XML";
      }
      if (indexIntercon >= 0) {
         nombre = "InterconDynamics_XML";
      }
      return nombre;
   }

   public void eventoFiltrar() {
      if (indexGenerado >= 0) {
         if (tipoListaGenerada == 0) {
            tipoListaGenerada = 1;
         }
      }
      if (indexIntercon >= 0) {
         if (tipoListaIntercon == 0) {
            tipoListaIntercon = 1;
         }
      }
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (indexGenerado >= 0) {
         if (banderaGenerado == 0) {
            altoTablaGenerada = "55";
            genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
            genProceso.setFilterStyle("width: 85% !important;");
            genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
            genEmpleado.setFilterStyle("width: 85% !important;");
            genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
            genCntCredito.setFilterStyle("width: 85% !important;");
            genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
            genCntDebito.setFilterStyle("width: 85% !important;");
            genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
            genTercero.setFilterStyle("width: 85% !important;");
            genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
            genValor.setFilterStyle("width: 85% !important;");
            genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
            genConcepto.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            banderaGenerado = 1;
         } else if (banderaGenerado == 1) {
            altoTablaGenerada = "75";
            genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
            genProceso.setFilterStyle("display: none; visibility: hidden;");
            genEmpleado = (Column) c.getViewRoot().findComponent("form:datosGenerados:genEmpleado");
            genEmpleado.setFilterStyle("display: none; visibility: hidden;");
            genCntCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntCredito");
            genCntCredito.setFilterStyle("display: none; visibility: hidden;");
            genCntDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCntDebito");
            genCntDebito.setFilterStyle("display: none; visibility: hidden;");
            genTercero = (Column) c.getViewRoot().findComponent("form:datosGenerados:genTercero");
            genTercero.setFilterStyle("display: none; visibility: hidden;");
            genValor = (Column) c.getViewRoot().findComponent("form:datosGenerados:genValor");
            genValor.setFilterStyle("display: none; visibility: hidden;");
            genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
            genConcepto.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGenerados");
            banderaGenerado = 0;
            filtrarListaGenerados = null;
            tipoListaGenerada = 0;
         }
      }
      if (indexIntercon >= 0) {
         if (banderaIntercon == 0) {
            altoTablaIntercon = "53";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("width: 85% !important;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("width: 85% !important;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("width: 85% !important;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("width: 85% !important;");
            interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
            interCredito.setFilterStyle("width: 85% !important;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("width: 85% !important;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("width: 85% !important;");
            interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
            interFechaVencimiento.setFilterStyle("width: 85% !important;");
            interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
            interOriginalDebito.setFilterStyle("width: 85% !important;");
            interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
            interOriginalCredito.setFilterStyle("width: 85% !important;");
            interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
            interPreradicacion.setFilterStyle("width: 85% !important;");
            interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
            interCodAlternativo.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 1;
         } else if (banderaIntercon == 1) {
            altoTablaIntercon = "75";
            interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
            interEmpleado.setFilterStyle("display: none; visibility: hidden;");
            interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
            interTercero.setFilterStyle("display: none; visibility: hidden;");
            interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
            interCuenta.setFilterStyle("display: none; visibility: hidden;");
            interDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interDebito");
            interDebito.setFilterStyle("display: none; visibility: hidden;");
            interCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCredito");
            interCredito.setFilterStyle("display: none; visibility: hidden;");
            interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
            interConcepto.setFilterStyle("display: none; visibility: hidden;");
            interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
            interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
            interFechaVencimiento = (Column) c.getViewRoot().findComponent("form:datosIntercon:interFechaVencimiento");
            interFechaVencimiento.setFilterStyle("display: none; visibility: hidden;");
            interOriginalDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalDebito");
            interOriginalDebito.setFilterStyle("display: none; visibility: hidden;");
            interOriginalCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interOriginalCredito");
            interOriginalCredito.setFilterStyle("display: none; visibility: hidden;");
            interPreradicacion = (Column) c.getViewRoot().findComponent("form:datosIntercon:interPreradicacion");
            interPreradicacion.setFilterStyle("display: none; visibility: hidden;");
            interCodAlternativo = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCodAlternativo");
            interCodAlternativo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIntercon");
            banderaIntercon = 0;
            filtrarListaInterconDynamics = null;
            tipoListaIntercon = 0;
         }
      }

   }

   public void validarRastro() {
      if (indexIntercon >= 0) {
         verificarRastro();
      }
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (listaInterconDynamics != null) {
         if (secRegistro != null) {
            int resultado = administrarRastros.obtenerTabla(secRegistro, "INTERCON_DYNAMICS");
            backUpSecRegistro = secRegistro;
            secRegistro = null;
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
      } else if (administrarRastros.verificarHistoricosTabla("INTERCON_DYNAMICS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      indexIntercon = -1;
   }

   public ActualUsuario getActualUsuarioBD() {
      if (actualUsuarioBD == null) {
         actualUsuarioBD = administrarInterfaseDynamicsSX.obtenerActualUsuario();
      }
      return actualUsuarioBD;
   }

   public void setActualUsuarioBD(ActualUsuario actualUsuarioBD) {
      this.actualUsuarioBD = actualUsuarioBD;
   }

   public ParametrosContables getParametroContableActual() {
      if (parametroContableActual == null) {
         getListaParametrosContables();
         if (listaParametrosContables != null) {
            if (listaParametrosContables.size() > 0) {
               parametroContableActual = listaParametrosContables.get(0);
            }
         }
      }
      return parametroContableActual;
   }

   public void setParametroContableActual(ParametrosContables parametroContable) {
      this.parametroContableActual = parametroContable;
   }

   public List<Empresas> getLovEmpresas() {
      lovEmpresas = administrarInterfaseDynamicsSX.lovEmpresas();
      return lovEmpresas;
   }

   public void setLovEmpresas(List<Empresas> lovEmpresas) {
      this.lovEmpresas = lovEmpresas;
   }

   public List<Empresas> getFiltrarLovEmpresas() {
      return filtrarLovEmpresas;
   }

   public void setFiltrarLovEmpresas(List<Empresas> filtrarLovEmpresas) {
      this.filtrarLovEmpresas = filtrarLovEmpresas;
   }

   public Empresas getEmpresaSeleccionada() {
      return empresaSeleccionada;
   }

   public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
      this.empresaSeleccionada = empresaSeleccionada;
   }

   public List<Procesos> getLovProcesos() {
      lovProcesos = administrarInterfaseDynamicsSX.lovProcesos();
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> lovProcesos) {
      this.lovProcesos = lovProcesos;
   }

   public List<Procesos> getFiltrarLovProcesos() {
      return filtrarLovProcesos;
   }

   public void setFiltrarLovProcesos(List<Procesos> filtrarLovProcesos) {
      this.filtrarLovProcesos = filtrarLovProcesos;
   }

   public Procesos getProcesoSeleccionado() {
      return procesoSeleccionado;
   }

   public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
      this.procesoSeleccionado = procesoSeleccionado;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
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

   public String getInfoRegistroEmpresa() {
      getLovEmpresas();
      if (lovEmpresas != null) {
         infoRegistroEmpresa = "Cantidad de registros : " + lovEmpresas.size();
      } else {
         infoRegistroEmpresa = "Cantidad de registros : 0";
      }
      return infoRegistroEmpresa;
   }

   public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
      this.infoRegistroEmpresa = infoRegistroEmpresa;
   }

   public String getInfoRegistroProceso() {
      getLovProcesos();
      if (lovProcesos != null) {
         infoRegistroProceso = "Cantidad de registros : " + lovProcesos.size();
      } else {
         infoRegistroProceso = "Cantidad de registros : 0";
      }
      return infoRegistroProceso;
   }

   public void setInfoRegistroProceso(String infoRegistroProceso) {
      this.infoRegistroProceso = infoRegistroProceso;
   }

   public List<ParametrosContables> getListaParametrosContables() {
      if (listaParametrosContables == null) {
         getActualUsuarioBD();
         if (actualUsuarioBD.getSecuencia() != null) {
            listaParametrosContables = administrarInterfaseDynamicsSX.obtenerParametrosContablesUsuarioBD(actualUsuarioBD.getAlias());
         }
         if (listaParametrosContables != null) {
            int tam = listaParametrosContables.size();
            if (tam > 0) {
               registroActual = 0;
            }
            if (tam == 0 || tam == 1) {
               estadoBtnAbajo = true;
               estadoBtnArriba = true;
            }
            if (tam > 1) {
               estadoBtnAbajo = false;
               estadoBtnArriba = true;
            }
            if (tam == 0) {
               activarAgregar = false;
               activarOtros = true;
            } else {
               activarAgregar = true;
               activarOtros = false;
            }
         }
      }
      return listaParametrosContables;
   }

   public void setListaParametrosContables(List<ParametrosContables> listaParametrosContables) {
      this.listaParametrosContables = listaParametrosContables;
   }

   public boolean isEstadoBtnArriba() {
      return estadoBtnArriba;
   }

   public void setEstadoBtnArriba(boolean estadoBtnArriba) {
      this.estadoBtnArriba = estadoBtnArriba;
   }

   public boolean isEstadoBtnAbajo() {
      return estadoBtnAbajo;
   }

   public void setEstadoBtnAbajo(boolean estadoBtnAbajo) {
      this.estadoBtnAbajo = estadoBtnAbajo;
   }

   public List<SolucionesNodos> getListaGenerados() {
      return listaGenerados;
   }

   public void setListaGenerados(List<SolucionesNodos> listaGenerados) {
      this.listaGenerados = listaGenerados;
   }

   public List<SolucionesNodos> getFiltrarListaGenerados() {
      return filtrarListaGenerados;
   }

   public void setFiltrarListaGenerados(List<SolucionesNodos> filtrarListaGenerados) {
      this.filtrarListaGenerados = filtrarListaGenerados;
   }

   public SolucionesNodos getGeneradoTablaSeleccionado() {
      getListaGenerados();
      if (listaGenerados != null) {
         if (listaGenerados.size() > 0) {
            generadoTablaSeleccionado = listaGenerados.get(0);
         }
      } else {
         generadoTablaSeleccionado = new SolucionesNodos();
      }
      return generadoTablaSeleccionado;
   }

   public void setGeneradoTablaSeleccionado(SolucionesNodos generadoTablaSeleccionado) {
      this.generadoTablaSeleccionado = generadoTablaSeleccionado;
   }

   public SolucionesNodos getEditarGenerado() {
      return editarGenerado;
   }

   public void setEditarGenerado(SolucionesNodos editarGenerado) {
      this.editarGenerado = editarGenerado;
   }

   public boolean isActivarAgregar() {
      return activarAgregar;
   }

   public void setActivarAgregar(boolean activarAgregar) {
      this.activarAgregar = activarAgregar;
   }

   public boolean isActivarOtros() {
      return activarOtros;
   }

   public void setActivarOtros(boolean activarOtros) {
      this.activarOtros = activarOtros;
   }

   public ParametrosContables getNuevoParametroContable() {
      return nuevoParametroContable;
   }

   public void setNuevoParametroContable(ParametrosContables nuevoParametroContable) {
      this.nuevoParametroContable = nuevoParametroContable;
   }

   public String getAltoTablaGenerada() {
      return altoTablaGenerada;
   }

   public void setAltoTablaGenerada(String altoTablaGenerada) {
      this.altoTablaGenerada = altoTablaGenerada;
   }

   public List<ParametrosContables> getListParametrosContablesBorrar() {
      return listParametrosContablesBorrar;
   }

   public void setListParametrosContablesBorrar(List<ParametrosContables> listParametrosContablesBorrar) {
      this.listParametrosContablesBorrar = listParametrosContablesBorrar;
   }

   public List<InterconDynamics> getListaInterconDynamics() {
      return listaInterconDynamics;
   }

   public void setListaInterconDynamics(List<InterconDynamics> setListaInterconDynamics) {
      this.listaInterconDynamics = setListaInterconDynamics;
   }

   public List<InterconDynamics> getFiltrarListaInterconDynamics() {
      return filtrarListaInterconDynamics;
   }

   public void setFiltrarListaInterconDynamics(List<InterconDynamics> setFiltrarListaInterconDynamics) {
      this.filtrarListaInterconDynamics = setFiltrarListaInterconDynamics;
   }

   public InterconDynamics getInterconTablaSeleccionada() {
      getListaInterconDynamics();
      if (listaInterconDynamics != null) {
         if (listaInterconDynamics.size() > 0) {
            interconTablaSeleccionada = listaInterconDynamics.get(0);
         }
      } else {
         interconTablaSeleccionada = new InterconDynamics();
      }
      return interconTablaSeleccionada;
   }

   public void setInterconTablaSeleccionada(InterconDynamics interconTablaSeleccionada) {
      this.interconTablaSeleccionada = interconTablaSeleccionada;
   }

   public InterconDynamics getEditarIntercon() {
      return editarIntercon;
   }

   public void setEditarIntercon(InterconDynamics editarIntercon) {
      this.editarIntercon = editarIntercon;
   }

   public String getAltoTablaIntercon() {
      return altoTablaIntercon;
   }

   public void setAltoTablaIntercon(String altoTablaIntercon) {
      this.altoTablaIntercon = altoTablaIntercon;
   }

   public BigInteger getSecRegistro() {
      return secRegistro;
   }

   public void setSecRegistro(BigInteger secRegistro) {
      this.secRegistro = secRegistro;
   }

   public BigInteger getBackUpSecRegistro() {
      return backUpSecRegistro;
   }

   public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
      this.backUpSecRegistro = backUpSecRegistro;
   }

   public int getTipoPlano() {
      return tipoPlano;
   }

   public void setTipoPlano(int tipoPlano) {
      this.tipoPlano = tipoPlano;
   }

   public String getMsnFechasActualizar() {
      return msnFechasActualizar;
   }

   public void setMsnFechasActualizar(String msnFechasActualizar) {
      this.msnFechasActualizar = msnFechasActualizar;
   }

   public boolean isActivarEnviar() {
      return activarEnviar;
   }

   public void setActivarEnviar(boolean activarEnviar) {
      this.activarEnviar = activarEnviar;
   }

   public boolean isActivarDeshacer() {
      return activarDeshacer;
   }

   public void setActivarDeshacer(boolean activarDeshacer) {
      this.activarDeshacer = activarDeshacer;
   }

   public int getTotalCGenerado() {
      totalCGenerado = 0;
      getListaGenerados();
      if (listaGenerados != null) {
         for (int i = 0; i < listaGenerados.size(); i++) {
            totalCGenerado = totalCGenerado + listaGenerados.get(i).getValor().intValue();
         }
      }
      return totalCGenerado;
   }

   public void setTotalCGenerado(int totalCGenerado) {
      this.totalCGenerado = totalCGenerado;
   }

   public int getTotalDGenerado() {
      totalDGenerado = 0;
      getListaGenerados();
      if (listaGenerados != null) {
         for (int i = 0; i < listaGenerados.size(); i++) {
            totalDGenerado = totalDGenerado + listaGenerados.get(i).getValor().intValue();
         }
      }
      return totalDGenerado;
   }

   public void setTotalDGenerado(int totalDGenerado) {
      this.totalDGenerado = totalDGenerado;
   }

   public int getTotalDInter() {
      totalDInter = 0;
      getListaInterconDynamics();
      if (listaInterconDynamics != null) {
         for (int i = 0; i < listaInterconDynamics.size(); i++) {
            totalDInter = totalDInter + listaInterconDynamics.get(i).getValord().intValue();
         }
      }
      return totalDInter;
   }

   public void setTotalDInter(int totalDInter) {
      this.totalDInter = totalDInter;
   }

   public int getTotalCInter() {
      totalCInter = 0;
      getListaInterconDynamics();
      if (listaInterconDynamics != null) {
         for (int i = 0; i < listaInterconDynamics.size(); i++) {
            totalCInter = totalCInter + listaInterconDynamics.get(i).getValorc().intValue();
         }
      }
      return totalCInter;
   }

   public void setTotalCInter(int totalCInter) {
      this.totalCInter = totalCInter;
   }

   public String getFechaIniRecon() {
      return fechaIniRecon;
   }

   public void setFechaIniRecon(String fechaIniRecon) {
      this.fechaIniRecon = fechaIniRecon;
   }

   public String getFechaFinRecon() {
      return fechaFinRecon;
   }

   public void setFechaFinRecon(String fechaFinRecon) {
      this.fechaFinRecon = fechaFinRecon;
   }

   public List<Empleados> getLovEmpleados() {
      lovEmpleados = administrarInterfaseDynamicsSX.buscarEmpleadosEmpresa();
      return lovEmpleados;
   }

   public void setLovEmpleados(List<Empleados> lovEmpleados) {
      this.lovEmpleados = lovEmpleados;
   }

   public List<Empleados> getFiltrarLovEmpleados() {
      return filtrarLovEmpleados;
   }

   public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleados) {
      this.filtrarLovEmpleados = filtrarLovEmpleados;
   }

   public Empleados getEmpleadoSeleccionado() {
      return empleadoSeleccionado;
   }

   public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
      this.empleadoSeleccionado = empleadoSeleccionado;
   }

   public String getInfoRegistroEmpleado() {
      return infoRegistroEmpleado;
   }

   public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
      this.infoRegistroEmpleado = infoRegistroEmpleado;
   }

}
