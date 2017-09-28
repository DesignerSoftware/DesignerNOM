/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.VWContabilidadDetallada;
import Entidades.VWContabilidadResumida1;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVWContabilidadResumida1Interface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
@Named(value = "controlInterfaseContableHistorico")
@SessionScoped
public class ControlInterfaseContableHistorico implements Serializable {

   private static Logger log = Logger.getLogger(ControlInterfaseContableHistorico.class);

   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarVWContabilidadResumida1Interface administrarContabilidadResumida;

   //
   private ActualUsuario actualUsuarioBD;
   private ParametrosContables parametroContableActual;
   private ParametrosContables nuevoParametroContable;
   private List<ParametrosContables> listaParametrosContables;
   private Date auxParametroFechaInicial, auxParametroFechaFinal;
   private boolean permitirIndexParametro;
   private boolean cambiosParametro;
   private List<ParametrosContables> listParametrosContablesBorrar;
   //
   private List<VWContabilidadResumida1> listaContabilidadResumida;
   private List<VWContabilidadResumida1> listaContabilidadResumidaFiltrar;
   private VWContabilidadResumida1 contabilidadResumidaSeleccionada;
   private VWContabilidadResumida1 editarContabilidadResumida;
   //
   private List<VWContabilidadDetallada> listaContabilidadDetallada;
   private List<VWContabilidadDetallada> listaContabilidadDetalladaFiltrar;
   private VWContabilidadDetallada contabilidadDetalladaSeleccionada;
   private VWContabilidadDetallada editarContabilidadDetallada;
   //
   private List<Empresas> lovEmpresas;
   private List<Empresas> filtrarLovEmpresas;
   private Empresas empresaSeleccionada;
   private String infoRegistroEmpresa;
   //
   private List<Procesos> lovProcesos;
   private List<Procesos> filtrarLovProcesos;
   private Procesos procesoSeleccionado;
   private String infoRegistroProceso;
   //
   private boolean guardado;
   private Date fechaDeParametro;
   private boolean aceptar;
   private boolean activarLov;
   private int tipoActualizacion, tipoListaGenerada, tipoListaIntercon;
   private String infoRegistroGenerados;
   private String infoRegistroContabilizados;
   private int cualCeldaIntercon, cualCeldaGenerado, banderaIntercon, banderaGenerado;
   //
   private Column genFechaCon, genCuentaContable, genvalorDebito, genvalorCredito, genConcepto, genProceso;
   private Column interProceso, interEmpleado, interTercero, interCuenta, interValorCredito, interValorDebito, interCentroCosto, interConcepto;
   private String altoTablaGenerada;
   private String altoTablaIntercon;
   private String fechaIniRecon, fechaFinRecon;
   private int cualTabla;
   private int cualCeldaParametroContable;
   private String msnFechasActualizar;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   private BigDecimal totalCGenerado, totalDGenerado, totalDInter, totalCInter;

   public ControlInterfaseContableHistorico() {
      altoTablaGenerada = "65";
      altoTablaIntercon = "65";
      tipoListaGenerada = 0;
      tipoListaIntercon = 0;
      listParametrosContablesBorrar = new ArrayList<ParametrosContables>();
      nuevoParametroContable = new ParametrosContables();
      nuevoParametroContable.setEmpresaRegistro(new Empresas());
      nuevoParametroContable.setProceso(new Procesos());
      listaContabilidadResumida = null;
      contabilidadResumidaSeleccionada = null;
//        listaInterconTotalBorrar = new ArrayList<InterconTotal>();
//        contabilidadDetalladaSeleccionada = new InterconTotal();
//        generadoTablaSeleccionado = null;
//        contabilidadDetalladaSeleccionada = null;
      lovEmpresas = null;
      lovProcesos = null;
      cualCeldaGenerado = -1;
      cualCeldaIntercon = -1;
      aceptar = true;
      cambiosParametro = false;
      permitirIndexParametro = true;
      parametroContableActual = null;
      guardado = true;
      activarLov = true;
      cualTabla = -1;
      banderaGenerado = 0;
      banderaIntercon = 0;
      mapParametros.put("paginaAnterior", paginaAnterior);
      totalCGenerado = BigDecimal.ZERO;
      totalDGenerado = BigDecimal.ZERO;
      totalDInter = BigDecimal.ZERO;
      totalCInter = BigDecimal.ZERO;
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listaParametrosContables = null;
      getListaParametrosContables();
      parametroContableActual = null;
      getParametroContableActual();
      listaContabilidadDetallada = null;
      getListaContabilidadDetallada();
      listaContabilidadResumida = null;
      getListaContabilidadResumida();
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

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "interfasecontablehistorico";
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
      lovEmpresas = null;
      lovProcesos = null;
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
         administrarContabilidadResumida.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct ControlInterfaseContableHistorico:  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public String redirigir() {
      return paginaAnterior;
   }

   public void modificarParametroContable() {
      guardado = false;
      cambiosParametro = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void modificarParametroContable(String confirmarCambio, String valorConfirmar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (valorConfirmar.equals("EMPRESA")) {
         parametroContableActual.getEmpresaRegistro().setNombre(parametroContableActual.getEmpresaRegistro().getNombre());
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
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:parametroEmpresa");
         } else {
            permitirIndexParametro = false;
            lovEmpresas = null;
            cargarLovEmpresas();
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
         }
      } else if (valorConfirmar.equals("PROCESO")) {
         if (!confirmarCambio.isEmpty()) {
            parametroContableActual.getProceso().setDescripcion(parametroContableActual.getProceso().getDescripcion());
            for (int i = 0; i < lovProcesos.size(); i++) {
               if (lovProcesos.get(i).getDescripcion().startsWith(confirmarCambio.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               parametroContableActual.setProceso(lovProcesos.get(indiceUnicoElemento));
               lovProcesos.clear();
               cargarLovProcesos();
               if (guardado == true) {
                  guardado = false;
               }
               cambiosParametro = true;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
               RequestContext.getCurrentInstance().update("form:parametroProceso");
            } else {
               permitirIndexParametro = false;
               lovProcesos = null;
               cargarLovProcesos();
               RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
            }
         } else {
            parametroContableActual.setProceso(null);
            guardado = false;
            cambiosParametro = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:parametroProceso");
         }
      }
   }

   public void modificarFechasParametro(ParametrosContables parametroc, int c) {
      parametroContableActual = parametroc;
      cambiarIndiceParametro(parametroContableActual, c);
      modificarParametroContable();
   }

   public boolean validarFechaParametro(int i) {
      fechaDeParametro = new Date();
      fechaDeParametro.setYear(0);
      fechaDeParametro.setMonth(1);
      fechaDeParametro.setDate(1);
      boolean retorno = true;
      if (i == 0) {
         if (parametroContableActual.getFechainicialcontabilizacion().after(fechaDeParametro) && parametroContableActual.getFechainicialcontabilizacion().before(parametroContableActual.getFechafinalcontabilizacion())) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoParametroContable.getFechainicialcontabilizacion().after(fechaDeParametro) && nuevoParametroContable.getFechainicialcontabilizacion().before(nuevoParametroContable.getFechafinalcontabilizacion())) {
            retorno = true;
         } else {
            retorno = false;
         }
      }
      log.info("retorno : " + retorno);
      return retorno;
   }

   public void posicionGenerado() {
      FacesContext context = FacesContext.getCurrentInstance();
      cualTabla = 2;
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      contabilidadResumidaSeleccionada = listaContabilidadResumida.get(indice);
      cualCeldaGenerado = columna;
      parametroContableActual = null;
      contabilidadDetalladaSeleccionada = null;
   }

   public void posicionIntercon() {
      FacesContext context = FacesContext.getCurrentInstance();
      cualTabla = 3;
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      contabilidadDetalladaSeleccionada = listaContabilidadDetallada.get(indice);
      cualCeldaIntercon = columna;
      parametroContableActual = null;
      contabilidadResumidaSeleccionada = null;
      contabilidadDetalladaSeleccionada.getSecuencia();
   }

   public void cambiarIndiceParametro(ParametrosContables parametroC, int celda) {
      cualCeldaParametroContable = celda;
      cualTabla = 1;
      if (permitirIndexParametro == true) {
         parametroContableActual = parametroC;
         if (cualCeldaParametroContable == 0) {
            parametroContableActual.getEmpresaRegistro().getNombre();
         } else if (cualCeldaParametroContable == 1) {
            parametroContableActual.getProceso().getDescripcion();
         } else if (cualCeldaParametroContable == 2) {
            auxParametroFechaFinal = parametroContableActual.getFechafinalcontabilizacion();
         } else if (cualCeldaParametroContable == 3) {
            auxParametroFechaInicial = parametroContableActual.getFechainicialcontabilizacion();
         }
      }
   }

   public void validarFechasProcesoActualizar() {
      Date fechaContabilizacion = administrarContabilidadResumida.obtenerFechaMaxContabilizaciones();
      Date fechaInterconTotal = administrarContabilidadResumida.obtenerFechaMaxInterconSapBO();
      DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
      RequestContext context = RequestContext.getCurrentInstance();
      if (fechaContabilizacion != null && fechaInterconTotal != null) {
         log.info("fecha contabilizacion en proceso actualizar" + fechaContabilizacion);
         log.info("fecha InterconTotal en proceso actualizar" + fechaInterconTotal);
         if (fechaContabilizacion.equals(fechaInterconTotal) == true) {
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
      ParametrosEstructuras parametroLiquidacion = administrarContabilidadResumida.parametrosLiquidacion();
      if ((parametroLiquidacion.getFechadesdecausado().compareTo(parametroContableActual.getFechainicialcontabilizacion()) == 0)
              && (parametroLiquidacion.getFechahastacausado().compareTo(parametroContableActual.getFechafinalcontabilizacion()) == 0)) {
         retorno = true;
      }
      return retorno;
   }

   public void actionBtnActualizar() {
      RequestContext context = RequestContext.getCurrentInstance();
      listaContabilidadResumida = null;
      if (listaContabilidadResumida == null) {
         log.info("fecha inicial contabilzacion : " + parametroContableActual.getFechainicialcontabilizacion());
         log.info("fecha final contabilzacion : " + parametroContableActual.getFechafinalcontabilizacion());
         log.info("empresa parametro : " + parametroContableActual.getEmpresaRegistro().getNombre());

         listaContabilidadResumida = administrarContabilidadResumida.obtenerContabilidadResumida(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
         getTotalCGenerado();
         getTotalDGenerado();
         RequestContext.getCurrentInstance().update("form:totalDGenerado");
         RequestContext.getCurrentInstance().update("form:totalCGenerado");
         contarRegistrosGenerados();
      }

      listaContabilidadDetallada = null;
      if (listaContabilidadDetallada == null) {
         listaContabilidadDetallada = administrarContabilidadResumida.obtenerContabilidadDetallada(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
         getTotalCInter();
         getTotalDInter();
         RequestContext.getCurrentInstance().update("form:totalDInter");
         RequestContext.getCurrentInstance().update("form:totalCInter");
         contarRegistroContabilizados();
      }
      validarFechasProcesoActualizar();
      RequestContext.getCurrentInstance().update("form:datosGenerados");
      RequestContext.getCurrentInstance().update("form:datosIntercon");
      int tam1 = 0;
      int tam2 = 0;
      if (listaContabilidadResumida != null) {
         tam1 = listaContabilidadResumida.size();
      }
      if (parametroContableActual.getProceso().getSecuencia() != null) {
         tam2 = listaContabilidadDetallada.size();
      }
      if (tam1 == 0 && tam2 == 0) {
         RequestContext.getCurrentInstance().execute("PF('procesoSinDatos').show()");
      }
      log.info("I finish");
      log.info("terminó proceso actualizar");
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
         if (parametroContableActual.getArchivo() == null) {
            parametroContableActual.setArchivo("NOMINA");
         }
         if (parametroContableActual.getProceso().getSecuencia() == null) {
            log.info("entra al if 2");
            parametroContableActual.setProceso(new Procesos());
         }
         administrarContabilidadResumida.modificarParametroContable(parametroContableActual);
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
         log.warn("Error guardarCambiosParametro Controlador : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Un error ha ocurrido en el guardado, intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void cancelarModificaciones() {
      if (banderaGenerado == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaGenerada = "65";
         genFechaCon = (Column) c.getViewRoot().findComponent("form:datosGenerados:genFechaCon");
         genFechaCon.setFilterStyle("display: none; visibility: hidden;");
         genCuentaContable = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCuentaContable");
         genCuentaContable.setFilterStyle("display: none; visibility: hidden;");
         genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
         genConcepto.setFilterStyle("display: none; visibility: hidden;");
         genvalorDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genvalorDebito");
         genvalorDebito.setFilterStyle("display: none; visibility: hidden;");
         genvalorCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genvalorCredito");
         genvalorCredito.setFilterStyle("display: none; visibility: hidden;");
         genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
         genProceso.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGenerados");
         banderaGenerado = 0;
         listaContabilidadResumidaFiltrar = null;
         tipoListaGenerada = 0;
      }
      if (banderaIntercon == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaIntercon = "65";
         interProceso = (Column) c.getViewRoot().findComponent("form:datosIntercon:interProceso");
         interProceso.setFilterStyle("display: none; visibility: hidden;");
         interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
         interEmpleado.setFilterStyle("display: none; visibility: hidden;");
         interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
         interTercero.setFilterStyle("display: none; visibility: hidden;");
         interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
         interCuenta.setFilterStyle("display: none; visibility: hidden;");
         interValorDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interValorDebito");
         interValorDebito.setFilterStyle("display: none; visibility: hidden;");
         interValorCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interValorCredito");
         interValorCredito.setFilterStyle("display: none; visibility: hidden;");
         interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
         interConcepto.setFilterStyle("display: none; visibility: hidden;");
         interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
         interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIntercon");
         banderaIntercon = 0;
         listaContabilidadDetalladaFiltrar = null;
         tipoListaIntercon = 0;
      }
      totalCGenerado = BigDecimal.ZERO;
      totalDGenerado = BigDecimal.ZERO;
      totalDInter = BigDecimal.ZERO;
      totalCInter = BigDecimal.ZERO;
      aceptar = true;
      listParametrosContablesBorrar.clear();
      actualUsuarioBD = null;
      getActualUsuarioBD();
      listaParametrosContables = null;
      getListaParametrosContables();
      parametroContableActual = null;
      listaContabilidadResumida = null;
      listaContabilidadDetallada = null;
      getParametroContableActual();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:PanelTotal");
      cambiosParametro = false;
      guardado = true;
      parametroContableActual = null;
      contabilidadResumidaSeleccionada = null;
      contabilidadDetalladaSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (parametroContableActual != null) {
         if (cualCeldaParametroContable == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresaParametro");
            RequestContext.getCurrentInstance().execute("PF('editarEmpresaParametro').show()");
            parametroContableActual = null;
         } else if (cualCeldaParametroContable == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoParametro");
            RequestContext.getCurrentInstance().execute("PF('editarProcesoParametro').show()");
            parametroContableActual = null;
            parametroContableActual = null;
         } else if (cualCeldaParametroContable == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialParametro");
            RequestContext.getCurrentInstance().execute("PF('editarFechaInicialParametro').show()");
            parametroContableActual = null;
         } else if (cualCeldaParametroContable == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalParametro");
            RequestContext.getCurrentInstance().execute("PF('editarFechaFinalParametro').show()");
            parametroContableActual = null;
         }
      } else if (contabilidadResumidaSeleccionada != null) {
         editarContabilidadResumida = contabilidadResumidaSeleccionada;
         if (cualCeldaGenerado == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarGenFechaCon");
            RequestContext.getCurrentInstance().execute("PF('editarGenFechaCon').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarGenCuentaContable");
            RequestContext.getCurrentInstance().execute("PF('editarGenCuentaContable').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarGenConcepto");
            RequestContext.getCurrentInstance().execute("PF('editarGenConcepto').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarGenvalorDebito");
            RequestContext.getCurrentInstance().execute("PF('editarGenvalorDebito').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarGenvalorCredito");
            RequestContext.getCurrentInstance().execute("PF('editarGenvalorCredito').show()");
            cualCeldaGenerado = -1;
         } else if (cualCeldaGenerado == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarGenProceso");
            RequestContext.getCurrentInstance().execute("PF('editarGenProceso').show()");
            cualCeldaGenerado = -1;
         }
      } else if (contabilidadDetalladaSeleccionada != null) {
         editarContabilidadDetallada = contabilidadDetalladaSeleccionada;
         if (cualCeldaIntercon == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarInterProceso");
            RequestContext.getCurrentInstance().execute("PF('editarInterProceso').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarInterEmpleado");
            RequestContext.getCurrentInstance().execute("PF('editarInterEmpleado').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarInterTercero");
            RequestContext.getCurrentInstance().execute("PF('editarInterTercero').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarInterCuenta");
            RequestContext.getCurrentInstance().execute("PF('editarInterCuenta').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarInterValorDebito");
            RequestContext.getCurrentInstance().execute("PF('editarInterValorDebito').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarInterValorCredito");
            RequestContext.getCurrentInstance().execute("PF('editarInterValorCredito').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarInterConcepto");
            RequestContext.getCurrentInstance().execute("PF('editarInterConcepto').show()");
            cualCeldaIntercon = -1;
         } else if (cualCeldaIntercon == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarInterCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('editarInterCentroCosto').show()");
            cualCeldaIntercon = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void salir() {
      limpiarListasValor();
      if (banderaGenerado == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaGenerada = "65";
         genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
         genProceso.setFilterStyle("display: none; visibility: hidden;");
         genFechaCon = (Column) c.getViewRoot().findComponent("form:datosGenerados:genFechaCon");
         genFechaCon.setFilterStyle("display: none; visibility: hidden;");
         genCuentaContable = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCuentaContable");
         genCuentaContable.setFilterStyle("display: none; visibility: hidden;");
         genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
         genConcepto.setFilterStyle("display: none; visibility: hidden;");
         genvalorDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genvalorDebito");
         genvalorDebito.setFilterStyle("display: none; visibility: hidden;");
         genvalorCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genvalorCredito");
         genvalorCredito.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGenerados");
         banderaGenerado = 0;
         listaContabilidadResumidaFiltrar = null;
         tipoListaGenerada = 0;
      }
      if (banderaIntercon == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         altoTablaIntercon = "65";
         interProceso = (Column) c.getViewRoot().findComponent("form:datosIntercon:interProceso");
         interProceso.setFilterStyle("display: none; visibility: hidden;");
         interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
         interEmpleado.setFilterStyle("display: none; visibility: hidden;");
         interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
         interTercero.setFilterStyle("display: none; visibility: hidden;");
         interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
         interCuenta.setFilterStyle("display: none; visibility: hidden;");
         interValorDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interValorDebito");
         interValorDebito.setFilterStyle("display: none; visibility: hidden;");
         interValorCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interValorCredito");
         interValorCredito.setFilterStyle("display: none; visibility: hidden;");
         interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
         interConcepto.setFilterStyle("display: none; visibility: hidden;");
         interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
         interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIntercon");
         banderaIntercon = 0;
         listaContabilidadDetalladaFiltrar = null;
         tipoListaIntercon = 0;
      }
      totalCGenerado = BigDecimal.ZERO;
      totalDGenerado = BigDecimal.ZERO;
      totalDInter = BigDecimal.ZERO;
      totalCInter = BigDecimal.ZERO;
      RequestContext context = RequestContext.getCurrentInstance();
      listParametrosContablesBorrar.clear();
      listaParametrosContables = null;
      getListaParametrosContables();
      parametroContableActual = null;
      listaContabilidadResumida = null;
      listaContabilidadDetallada = null;
      actualUsuarioBD = null;
      cambiosParametro = false;
      guardado = true;
      parametroContableActual = null;
      contabilidadResumidaSeleccionada = null;
      contabilidadDetalladaSeleccionada = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void asignarIndex(ParametrosContables paramc, int numeroDialogo, int tipoNuevo) {
      tipoActualizacion = tipoNuevo;
      parametroContableActual = paramc;
      RequestContext context = RequestContext.getCurrentInstance();
      if (numeroDialogo == 0) {
         lovEmpresas = null;
         cargarLovEmpresas();
         RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
         RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
      } else if (numeroDialogo == 1) {
         lovProcesos = null;
         cargarLovProcesos();
         RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
         RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
      }
   }

   public void actualizarEmpresa() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         parametroContableActual.setEmpresaRegistro(empresaSeleccionada);
         parametroContableActual.setEmpresaCodigo(empresaSeleccionada.getCodigo());
         parametroContableActual = null;
         if (guardado == true) {
            guardado = false;
         }
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
      aceptar = true;

      RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
      RequestContext.getCurrentInstance().update("form:lovEmpresa");
      RequestContext.getCurrentInstance().update("form:aceptarE");
      context.reset("form:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");

   }

   public void cancelarEmpresa() {
      empresaSeleccionada = new Empresas();
      filtrarLovEmpresas = null;
      parametroContableActual = null;
      permitirIndexParametro = true;
      aceptar = true;
      tipoActualizacion = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
      RequestContext.getCurrentInstance().update("form:lovEmpresa");
      RequestContext.getCurrentInstance().update("form:aceptarE");
      context.reset("form:lovEmpresa:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovEmpresa').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').hide()");
   }

   public void actualizarProceso() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         parametroContableActual.setProceso(procesoSeleccionado);
//            parametroContableActual.setArchivo(procesoSeleccionado.getDescripcion());
         if (guardado == true) {
            guardado = false;
         }
         cambiosParametro = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:parametroProceso");
      }
      procesoSeleccionado = null;
      filtrarLovProcesos = null;
      aceptar = true;
      RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
      RequestContext.getCurrentInstance().update("form:lovProceso");
      RequestContext.getCurrentInstance().update("form:aceptarP");
      context.reset("form:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
   }

   public void cancelarProceso() {
      aceptar = true;
      procesoSeleccionado = new Procesos();
      filtrarLovProcesos = null;
      parametroContableActual = null;
      permitirIndexParametro = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
      RequestContext.getCurrentInstance().update("form:lovProceso");
      RequestContext.getCurrentInstance().update("form:aceptarP");
      context.reset("form:lovProceso:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProceso').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').hide()");
   }

   public void listaValoresBoton() {
      if (parametroContableActual != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCeldaParametroContable == 0) {
            lovEmpresas = null;
            cargarLovEmpresas();
            RequestContext.getCurrentInstance().update("form:EmpresaDialogo");
            RequestContext.getCurrentInstance().execute("PF('EmpresaDialogo').show()");
         }
         if (cualCeldaParametroContable == 2) {
            lovProcesos = null;
            cargarLovProcesos();
            RequestContext.getCurrentInstance().update("form:ProcesoDialogo");
            RequestContext.getCurrentInstance().execute("PF('ProcesoDialogo').show()");
         }
      }
   }

   public void limpiarNuevoParametro() {
      nuevoParametroContable = new ParametrosContables();
      nuevoParametroContable.setEmpresaRegistro(new Empresas());
      nuevoParametroContable.setProceso(new Procesos());
   }

   public void validarExportPDF() throws IOException {
      if (cualTabla == 1) {
         exportPDF_PC();
      }
      if (cualTabla == 2) {
         exportPDF_G();
      }
      if (cualTabla == 3) {
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
   }

   public void exportPDF_G() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosGenerarExportar");
      FacesContext context = c;
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ContabilidadResumida_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      contabilidadResumidaSeleccionada = null;
   }

   public void exportPDF_I() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosInterconExportar");
      FacesContext context = c;
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ContabilidadDetallada_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      contabilidadDetalladaSeleccionada = null;
   }

   public void validarExportXLS() throws IOException {
      if (cualTabla == 1) {
         exportXLS_PC();
      }
      if (cualTabla == 2) {
         exportXLS_G();
      }
      if (cualTabla == 3) {
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
   }

   public void exportXLS_G() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosGenerarExportar");
      FacesContext context = c;
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ContabilidadResumida_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      contabilidadResumidaSeleccionada = null;
   }

   public void exportXLS_I() throws IOException {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosInterconExportar");
      FacesContext context = c;
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "IContabilidadDetallada_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
      contabilidadDetalladaSeleccionada = null;
   }

   public String validarExportXML() {
      String tabla = "";
      if (parametroContableActual != null) {
         tabla = ":formExportar:datosParametroExportar";
      }
      if (contabilidadResumidaSeleccionada != null) {
         tabla = ":formExportar:datosGenerarExportar";
      }
      if (contabilidadDetalladaSeleccionada != null) {
         tabla = ":formExportar:datosInterconExportar";
      }
      return tabla;
   }

   public String validarNombreExportXML() {
      String nombre = "";
      if (parametroContableActual != null) {
         nombre = "ParametrosContables_XML";
      }
      if (contabilidadResumidaSeleccionada != null) {
         nombre = "ContabilidadResumida_XML";
      }
      if (contabilidadDetalladaSeleccionada != null) {
         nombre = "ontabilidadDetallada_XML";
      }
      return nombre;
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaGenerado == 0 && banderaIntercon == 0) {
         altoTablaGenerada = "45";
         genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
         genProceso.setFilterStyle("width: 85% !important;");
         genFechaCon = (Column) c.getViewRoot().findComponent("form:datosGenerados:genFechaCon");
         genFechaCon.setFilterStyle("width: 85% !important;");
         genCuentaContable = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCuentaContable");
         genCuentaContable.setFilterStyle("width: 85% !important;");
         genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
         genConcepto.setFilterStyle("width: 85% !important;");
         genvalorDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genvalorDebito");
         genvalorDebito.setFilterStyle("width: 85% !important;");
         genvalorCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genvalorCredito");
         genvalorCredito.setFilterStyle("width: 85% !important;");

         altoTablaIntercon = "45";
         interProceso = (Column) c.getViewRoot().findComponent("form:datosIntercon:interProceso");
         interProceso.setFilterStyle("width: 85% !important;");
         interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
         interEmpleado.setFilterStyle("width: 85% !important;");
         interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
         interTercero.setFilterStyle("width: 85% !important;");
         interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
         interCuenta.setFilterStyle("width: 85% !important;");
         interValorDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interValorDebito");
         interValorDebito.setFilterStyle("width: 85% !important;");
         interValorCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interValorCredito");
         interValorCredito.setFilterStyle("width: 85% !important;");
         interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
         interConcepto.setFilterStyle("width: 85% !important;");
         interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
         interCentroCosto.setFilterStyle("width: 85% !important;");
         banderaGenerado = 1;
         banderaIntercon = 1;
         RequestContext.getCurrentInstance().update("form:datosIntercon");
         RequestContext.getCurrentInstance().update("form:datosGenerados");
      } else if (banderaGenerado == 1 && banderaIntercon == 1) {
         altoTablaGenerada = "65";
         altoTablaIntercon = "65";
         genProceso = (Column) c.getViewRoot().findComponent("form:datosGenerados:genProceso");
         genProceso.setFilterStyle("display: none; visibility: hidden;");
         genFechaCon = (Column) c.getViewRoot().findComponent("form:datosGenerados:genFechaCon");
         genFechaCon.setFilterStyle("display: none; visibility: hidden;");
         genCuentaContable = (Column) c.getViewRoot().findComponent("form:datosGenerados:genCuentaContable");
         genCuentaContable.setFilterStyle("display: none; visibility: hidden;");
         genConcepto = (Column) c.getViewRoot().findComponent("form:datosGenerados:genConcepto");
         genConcepto.setFilterStyle("display: none; visibility: hidden;");
         genvalorDebito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genvalorDebito");
         genvalorDebito.setFilterStyle("display: none; visibility: hidden;");
         genvalorCredito = (Column) c.getViewRoot().findComponent("form:datosGenerados:genvalorCredito");
         genvalorCredito.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosGenerados");

         interProceso = (Column) c.getViewRoot().findComponent("form:datosIntercon:interProceso");
         interProceso.setFilterStyle("display: none; visibility: hidden;");
         interEmpleado = (Column) c.getViewRoot().findComponent("form:datosIntercon:interEmpleado");
         interEmpleado.setFilterStyle("display: none; visibility: hidden;");
         interTercero = (Column) c.getViewRoot().findComponent("form:datosIntercon:interTercero");
         interTercero.setFilterStyle("display: none; visibility: hidden;");
         interCuenta = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCuenta");
         interCuenta.setFilterStyle("display: none; visibility: hidden;");
         interValorDebito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interValorDebito");
         interValorDebito.setFilterStyle("display: none; visibility: hidden;");
         interValorCredito = (Column) c.getViewRoot().findComponent("form:datosIntercon:interValorCredito");
         interValorCredito.setFilterStyle("display: none; visibility: hidden;");
         interConcepto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interConcepto");
         interConcepto.setFilterStyle("display: none; visibility: hidden;");
         interCentroCosto = (Column) c.getViewRoot().findComponent("form:datosIntercon:interCentroCosto");
         interCentroCosto.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIntercon");
         banderaIntercon = 0;
         listaContabilidadResumidaFiltrar = null;
         tipoListaIntercon = 0;
         banderaGenerado = 0;
         listaContabilidadDetalladaFiltrar = null;
         tipoListaGenerada = 0;
      }
   }

   public void validarRastro() {
      if (cualTabla == 1) {

      } else if (cualTabla == 2) {

      } else if (cualTabla == 3) {
         verificarRastro();
      }
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (contabilidadResumidaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(contabilidadResumidaSeleccionada.getSecuencia(), "VWCONTABILIDADDETALLADA");
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
      } else if (administrarRastros.verificarHistoricosTabla("VWCONTABILIDADDETALLADA")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroParametro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (contabilidadResumidaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(contabilidadResumidaSeleccionada.getSecuencia(), "PARAMETROSCONTABLES");
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDBParametro').show()");
         } else if (resultado == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroParametro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroParametro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroParametro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("PARAMETROSCONTABLES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoParametro').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroResumida() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (contabilidadResumidaSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(contabilidadResumidaSeleccionada.getSecuencia(), "VWCONTABILIDADRESUMIDA1");
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDBResumida').show()");
         } else if (resultado == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroResumida').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroResumida').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroResumida').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("VWCONTABILIDADRESUMIDA1")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoResumida').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void contarRegistroEmpresas() {
      RequestContext.getCurrentInstance().update("form:infoRegistroEmpresa");
   }

   public void contarRegistroProcesos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroProceso");
   }

   public void contarRegistroContabilizados() {
      RequestContext.getCurrentInstance().update("form:infoRegistroContabilizados");
   }

   public void contarRegistrosGenerados() {
      RequestContext.getCurrentInstance().update("form:infoRegistroGenerados");
   }

   public void eventoFiltrarGenerados() {
      contarRegistrosGenerados();
   }

   public void eventoFiltrarContabilizados() {
      contarRegistroContabilizados();
   }

   public void cargarLovEmpresas() {
      if (lovEmpresas == null) {
         lovEmpresas = administrarContabilidadResumida.lovEmpresas();
      }
   }

   public void cargarLovProcesos() {
      if (lovProcesos == null) {
         lovProcesos = administrarContabilidadResumida.lovProcesos();
      }
   }

   public void actionBtnRecontabilizar() {
      Integer contador = administrarContabilidadResumida.abrirPeriodoContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
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

   public void finalizarProcesoAbrirPeriodo() {
      try {
         guardadoGeneral();
         administrarContabilidadResumida.actualizarPeriodoContable(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
         RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
         RequestContext.getCurrentInstance().execute("PF('RecontabilizacionExitosa').show()");
      } catch (Exception e) {
         log.warn("Error finalizarProcesoRecontabilizacion Controlador : " + e.toString());
         RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
         RequestContext.getCurrentInstance().execute("PF('RecontabilizacionExitosa').hide()");
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", e.getMessage()));
         RequestContext.getCurrentInstance().update("form:growl");
      }

//        catch (ExcepcionBD ebd) {
//            RequestContext.getCurrentInstance().execute("PF('operacionEnProceso').hide()");
//            RequestContext.getCurrentInstance().execute("PF('RecontabilizacionExitosa').hide()");
//            log.info("controlInterfaseTotal. finalizarProcesoRecontabilizacion : " + ebd.getMessage());
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", ebd.getMessage()));
//            RequestContext.getCurrentInstance().update("form:growl");
//        }
   }
/////////////////////SETS Y GETS/////////////////////////////

   public ActualUsuario getActualUsuarioBD() {
      if (actualUsuarioBD == null) {
         actualUsuarioBD = administrarContabilidadResumida.obtenerActualUsuario();
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
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovEmpresa");
      infoRegistroEmpresa = String.valueOf(tabla.getRowCount());
      return infoRegistroEmpresa;
   }

   public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
      this.infoRegistroEmpresa = infoRegistroEmpresa;
   }

   public String getInfoRegistroProceso() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovProceso");
      infoRegistroProceso = String.valueOf(tabla.getRowCount());
      return infoRegistroProceso;
   }

   public void setInfoRegistroProceso(String infoRegistroProceso) {
      this.infoRegistroProceso = infoRegistroProceso;
   }

   public List<ParametrosContables> getListaParametrosContables() {
      if (listaParametrosContables == null) {
         getActualUsuarioBD();
         if (actualUsuarioBD.getSecuencia() != null) {
            listaParametrosContables = administrarContabilidadResumida.obtenerParametrosContablesUsuarioBD(actualUsuarioBD.getAlias());
         }
      }
      return listaParametrosContables;
   }

   public void setListaParametrosContables(List<ParametrosContables> listaParametrosContables) {
      this.listaParametrosContables = listaParametrosContables;
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

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public String getInfoRegistroGenerados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosGenerados");
      infoRegistroGenerados = String.valueOf(tabla.getRowCount());
      return infoRegistroGenerados;
   }

   public void setInfoRegistroGenerados(String infoRegistroGenerados) {
      this.infoRegistroGenerados = infoRegistroGenerados;
   }

   public String getInfoRegistroContabilizados() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosIntercon");
      infoRegistroContabilizados = String.valueOf(tabla.getRowCount());
      return infoRegistroContabilizados;
   }

   public void setInfoRegistroContabilizados(String infoRegistroContabilizados) {
      this.infoRegistroContabilizados = infoRegistroContabilizados;
   }

   public String getMsnFechasActualizar() {
      return msnFechasActualizar;
   }

   public void setMsnFechasActualizar(String msnFechasActualizar) {
      this.msnFechasActualizar = msnFechasActualizar;
   }

   public List<VWContabilidadResumida1> getListaContabilidadResumida() {
//        if (listaContabilidadResumida == null) {
//            listaContabilidadResumida = administrarContabilidadResumida.obtenerContabilidadResumida(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
//        }
      return listaContabilidadResumida;
   }

   public void setListaContabilidadResumida(List<VWContabilidadResumida1> listaContabilidadResumida) {
      this.listaContabilidadResumida = listaContabilidadResumida;
   }

   public List<VWContabilidadResumida1> getListaContabilidadResumidaFiltrar() {
      return listaContabilidadResumidaFiltrar;
   }

   public void setListaContabilidadResumidaFiltrar(List<VWContabilidadResumida1> listaContabilidadResumidaFiltrar) {
      this.listaContabilidadResumidaFiltrar = listaContabilidadResumidaFiltrar;
   }

   public VWContabilidadResumida1 getEditarContabilidadResumida() {
      return editarContabilidadResumida;
   }

   public void setEditarContabilidadResumida(VWContabilidadResumida1 editarContabilidadResumida) {
      this.editarContabilidadResumida = editarContabilidadResumida;
   }

   public List<VWContabilidadDetallada> getListaContabilidadDetallada() {
//        if (listaContabilidadDetallada == null) {
//            listaContabilidadDetallada = administrarContabilidadResumida.obtenerContabilidadDetallada(parametroContableActual.getFechainicialcontabilizacion(), parametroContableActual.getFechafinalcontabilizacion(), parametroContableActual.getProceso().getSecuencia());
//        }
      return listaContabilidadDetallada;
   }

   public void setListaContabilidadDetallada(List<VWContabilidadDetallada> listaContabilidadDetallada) {
      this.listaContabilidadDetallada = listaContabilidadDetallada;
   }

   public List<VWContabilidadDetallada> getListaContabilidadDetalladaFiltrar() {
      return listaContabilidadDetalladaFiltrar;
   }

   public void setListaContabilidadDetalladaFiltrar(List<VWContabilidadDetallada> listaContabilidadDetalladaFiltrar) {
      this.listaContabilidadDetalladaFiltrar = listaContabilidadDetalladaFiltrar;
   }

   public VWContabilidadDetallada getEditarContabilidadDetallada() {
      return editarContabilidadDetallada;
   }

   public void setEditarContabilidadDetallada(VWContabilidadDetallada editarContabilidadDetallada) {
      this.editarContabilidadDetallada = editarContabilidadDetallada;
   }

   public VWContabilidadResumida1 getContabilidadResumidaSeleccionada() {
      return contabilidadResumidaSeleccionada;
   }

   public void setContabilidadResumidaSeleccionada(VWContabilidadResumida1 contabilidadResumidaSeleccionada) {
      this.contabilidadResumidaSeleccionada = contabilidadResumidaSeleccionada;
   }

   public VWContabilidadDetallada getContabilidadDetalladaSeleccionada() {
      return contabilidadDetalladaSeleccionada;
   }

   public void setContabilidadDetalladaSeleccionada(VWContabilidadDetallada contabilidadDetalladaSeleccionada) {
      this.contabilidadDetalladaSeleccionada = contabilidadDetalladaSeleccionada;
   }

   public String getAltoTablaIntercon() {
      return altoTablaIntercon;
   }

   public void setAltoTablaIntercon(String altoTablaIntercon) {
      this.altoTablaIntercon = altoTablaIntercon;
   }

   public BigDecimal getTotalCGenerado() {
      totalCGenerado = BigDecimal.ZERO;
      if (listaContabilidadResumida != null) {
         for (int i = 0; i < listaContabilidadResumida.size(); i++) {
            totalCGenerado = totalCGenerado.add(listaContabilidadResumida.get(i).getValorc());
         }
      }
      log.info("totalCGenerado : " + totalCGenerado);
      return totalCGenerado;
   }

   public void setTotalCGenerado(BigDecimal totalCGenerado) {
      this.totalCGenerado = totalCGenerado;
   }

   public BigDecimal getTotalDGenerado() {
      totalDGenerado = BigDecimal.ZERO;
      if (listaContabilidadResumida != null) {
         for (int i = 0; i < listaContabilidadResumida.size(); i++) {
            totalDGenerado = totalDGenerado.add(listaContabilidadResumida.get(i).getValord());
         }
      }
      log.info("totalDGenerado : " + totalDGenerado);
      return totalDGenerado;
   }

   public void setTotalDGenerado(BigDecimal totalDGenerado) {
      this.totalDGenerado = totalDGenerado;
   }

   public BigDecimal getTotalDInter() {
      totalDInter = BigDecimal.ZERO;
      if (listaContabilidadDetallada != null) {
         for (int i = 0; i < listaContabilidadDetallada.size(); i++) {
            totalDInter = totalDInter.add(listaContabilidadDetallada.get(i).getValord());
         }
      }
      log.info("totalDInter : " + totalDInter);
      return totalDInter;
   }

   public void setTotalDInter(BigDecimal totalDInter) {
      this.totalDInter = totalDInter;
   }

   public BigDecimal getTotalCInter() {
      totalCInter = BigDecimal.ZERO;
      if (listaContabilidadDetallada != null) {
         for (int i = 0; i < listaContabilidadDetallada.size(); i++) {
            totalCInter = totalCInter.add(listaContabilidadDetallada.get(i).getValorc());
         }
      }
      log.info("totalCInter : " + totalCInter);
      return totalCInter;
   }

   public void setTotalCInter(BigDecimal totalCInter) {
      this.totalCInter = totalCInter;
   }

}
