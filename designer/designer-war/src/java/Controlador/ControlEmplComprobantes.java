package Controlador;

import Entidades.CentrosCostos;
import Entidades.Comprobantes;
import Entidades.CortesProcesos;
import Entidades.Cuentas;
import Entidades.DetallesFormulas;
import Entidades.Empleados;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.Terceros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplComprobantesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
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

@ManagedBean
@SessionScoped
public class ControlEmplComprobantes implements Serializable {

   @EJB
   AdministrarEmplComprobantesInterface administrarComprobantes;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private Empleados empleado;
   private BigInteger secuenciaEmpleado;
   //TABLA 1
   private List<Comprobantes> listaComprobantes;
   private List<Comprobantes> filtradolistaComprobantes;
   private Comprobantes comprobanteSeleccionado;
   //TABLA 2
   private List<CortesProcesos> listaCortesProcesos;
   private List<CortesProcesos> filtradolistaCortesProcesos;
   private CortesProcesos cortesProcesosSeleccionado;
   //TABLA 3
   private List<SolucionesNodos> listaSolucionesNodosEmpleado;
   private List<SolucionesNodos> filtradolistaSolucionesNodosEmpleado;
   private SolucionesNodos empleadoTablaSeleccionado;
   //TABLA 4
   private List<SolucionesNodos> listaSolucionesNodosEmpleador;
   private List<SolucionesNodos> filtradolistaSolucionesNodosEmpleador;
   private SolucionesNodos empleadorTablaSeleccionado;
   //Conteo Registros
   private String infoRegistroComp, infoRegistroCP, infoRegistroTEmpleado, infoRegistroTEmpleador;
   //LOV PROCESOS
   private List<Procesos> lovProcesos;
   private Procesos procesoSelecionado;
   private List<Procesos> filtradoProcesos;
   //LOV TERCEROS
   private List<Terceros> lovTerceros;
   private Terceros TerceroSelecionado;
   private List<Terceros> filtradolistaTerceros;
   //LOV CUENTAS
   private List<Cuentas> lovCuentas;
   private List<Cuentas> filtrarLovCuentas;
   private Cuentas cuentaSeleccionada;
   //LOV CENTROSCOSTOS
   private List<CentrosCostos> lovCentrosCostos;
   private List<CentrosCostos> filtrarLovCentrosCostos;
   private CentrosCostos centroCostoSeleccionado;
   //
   private int tipoActualizacion;
   private boolean permitirIndex;
   //Activo/Desactivo Crtl + F11
   private int banderaComprobantes;
   private int banderaCortesProcesos;
   //Columnas Tablas
   DataTable tablaComprobantes, tablaCortesProcesos;
   private Column numeroComprobanteC, fechaC, fechaEntregaC;
   private Column fechaCorteCP, procesoCP;
   private String altoScrollComprobante;
   private String altoScrollSolucionesNodos;
   //Tablas soluciones nodos
   private Column c_codigo, c_descripcion, c_fDesde, c_fHasta, c_unidad, c_pago, c_descuento,
           c_tercero, c_cuentaD, c_cuentaC, c_CCDebito, c_CCCredito, c_saldo, c_fechaPago;
   private Column cr_codigo, cr_descripcion, cr_fDesde, cr_fHasta, cr_unidad, cr_pasivo, cr_gasto,
           cr_tercero, cr_cuentaD, cr_cuentaC, cr_CCDebito, cr_CCCredito, cr_saldo, cr_fechaPago;
   //Otros
   private boolean aceptar;
   private int index, tablaActual;
   private String tablaExportar, nombreArchivoExportar;
   //modificar
   private List<Comprobantes> listaComprobantesModificar;
   private List<CortesProcesos> listaCortesProcesosModificar;
   private List<SolucionesNodos> listaSolucionesNodosEmpleadoModificar;
   private List<SolucionesNodos> listaSolucionesNodosEmpleadorModificar;
   private boolean guardado;
   private boolean modificacionesComprobantes;
   private boolean modificacionesCortesProcesos;
   private boolean modificacionesSolucionesNodosEmpleado;
   private boolean modificacionesSolucionesNodosEmpleador;
   //crear
   private Comprobantes nuevoComprobante;
   private CortesProcesos nuevoCorteProceso;
   private List<Comprobantes> listaComprobantesCrear;
   private List<CortesProcesos> listaCortesProcesosCrear;
   private BigInteger l;
   private int k;
   private String mensajeValidacion;
   //borrar VC
   private List<Comprobantes> listaComprobantesBorrar;
   private List<CortesProcesos> listaCortesProcesosBorrar;
   //editar celda
   private Comprobantes editarComprobante;
   private CortesProcesos editarCorteProceso;
   private boolean cambioEditor, aceptarEditar;
   //duplicar
   private Comprobantes duplicarComprobante;
   private CortesProcesos duplicarCorteProceso;
   //AUTOCOMPLETAR
   private String proceso, tercero, codigoCredito, codigoDebito, centroCostoDebito, centroCostoCredito;
   //RASTRO*/
   private BigInteger secRegistro;
   private String nombreTabla;
   //INFORMATIVOS
   private int cualCelda, tipoListaComprobantes, tipoListaCortesProcesos, tipoListaSNEmpleado, tipoListaSNEmpleador, tipoTabla;
   //SUBTOTALES Y NETO
   private BigDecimal subtotalPago, subtotalDescuento, subtotalPasivo, subtotalGasto, neto;
   //PARCIALES 
   private String parcialesSolucionNodos;
   //DETALLES FORMULAS
   private List<DetallesFormulas> listaDetallesFormulas;
   //FORMATO FECHAS
   private SimpleDateFormat formatoFecha;
   //
   private String infoRegistroProceso, infoRegistroTercero, infoRegistroCuentaD, infoRegistroCuentaC, infoRegistroCentroCostoC, infoRegistroCentroCostoD;
   //
   private Date auxFechaEntregadoComprobante;

   private DataTable tabla1, tabla2;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlEmplComprobantes() {
      permitirIndex = true;
      lovProcesos = new ArrayList<Procesos>();
      lovTerceros = new ArrayList<Terceros>();
      procesoSelecionado = new Procesos();
      comprobanteSeleccionado = null;
      //Otros
      aceptar = true;
      //borrar
      listaComprobantesBorrar = new ArrayList<Comprobantes>();
      listaCortesProcesosBorrar = new ArrayList<CortesProcesos>();
      //crear
      listaComprobantesCrear = new ArrayList<Comprobantes>();
      listaCortesProcesosCrear = new ArrayList<CortesProcesos>();
      k = 0;
      //modificar
      listaComprobantesModificar = new ArrayList<Comprobantes>();
      listaCortesProcesosModificar = new ArrayList<CortesProcesos>();
      listaSolucionesNodosEmpleadoModificar = new ArrayList<SolucionesNodos>();
      listaSolucionesNodosEmpleadorModificar = new ArrayList<SolucionesNodos>();
      modificacionesCortesProcesos = false;
      modificacionesComprobantes = false;
      //editar
      editarComprobante = new Comprobantes();
      editarCorteProceso = new CortesProcesos();
      cambioEditor = false;
      aceptarEditar = true;
      cualCelda = -1;
      tipoListaComprobantes = 0;
      tipoListaCortesProcesos = 0;
      tipoListaSNEmpleado = 0;
      tipoListaSNEmpleador = 0;
      tipoTabla = 0;
      //guardar
      guardado = true;
      //Crear
      nuevoComprobante = new Comprobantes();
      nuevoCorteProceso = new CortesProcesos();
      nuevoCorteProceso.setProceso(new Procesos());
      duplicarComprobante = new Comprobantes();
      secRegistro = null;
      tablaExportar = ":formExportar:datosComprobantesExportar";
      altoScrollComprobante = "69";
      banderaComprobantes = 0;
      banderaCortesProcesos = 0;
      nombreTabla = "Comprobantes";
      subtotalPago = new BigDecimal(0);
      subtotalDescuento = new BigDecimal(0);
      subtotalPasivo = new BigDecimal(0);
      subtotalGasto = new BigDecimal(0);
      listaDetallesFormulas = null;
      formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
      infoRegistroComp = "0";
      infoRegistroCP = "0";
      infoRegistroTEmpleado = "0";
      infoRegistroTEmpleador = "0";
      infoRegistroProceso = "0";
      infoRegistroTercero = "0";
      infoRegistroCuentaC = "0";
      infoRegistroCuentaD = "0";
      infoRegistroCentroCostoD = "0";
      infoRegistroCentroCostoC = "0";
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
      } else {
         String pagActual = "emplcomprobante";
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

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarComprobantes.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger sec) {
      subtotalPago = new BigDecimal(0);
      subtotalDescuento = new BigDecimal(0);
      subtotalPasivo = new BigDecimal(0);
      subtotalGasto = new BigDecimal(0);
      secuenciaEmpleado = sec;
      empleado = administrarComprobantes.consultarEmpleado(secuenciaEmpleado);
      if (empleado != null) {
         listaComprobantes = administrarComprobantes.consultarComprobantesEmpleado(empleado.getSecuencia());
         if (listaComprobantes != null) {
            if (listaComprobantes.size() > 0) {
               for (int i = 0; i < listaComprobantes.size(); i++) {
                  if (listaComprobantes.get(i).getFecha() == null) {
                     listaComprobantes.get(i).setReadOnlyFecha(false);
                  } else {
                     listaComprobantes.get(i).setReadOnlyFecha(true);
                  }
                  if (listaComprobantes.get(i).getFechaentregado() == null) {
                     listaComprobantes.get(i).setReadOnlyFechaEntregado(false);
                  } else {
                     listaComprobantes.get(i).setReadOnlyFechaEntregado(true);
                  }
                  if (listaComprobantes.get(i).getNumero() == null) {
                     listaComprobantes.get(i).setReadOnlyNumero(false);
                  } else {
                     listaComprobantes.get(i).setReadOnlyNumero(true);
                  }
               }

               comprobanteSeleccionado = listaComprobantes.get(0);
               listaCortesProcesos = administrarComprobantes.consultarCortesProcesosComprobante(comprobanteSeleccionado.getSecuencia());
               if (!listaCortesProcesos.isEmpty()) {
                  cortesProcesosSeleccionado = listaCortesProcesos.get(0);
                  listaSolucionesNodosEmpleado = administrarComprobantes.consultarSolucionesNodosEmpleado(cortesProcesosSeleccionado.getSecuencia(), empleado.getSecuencia());
                  listaSolucionesNodosEmpleador = administrarComprobantes.consultarSolucionesNodosEmpleador(cortesProcesosSeleccionado.getSecuencia(), empleado.getSecuencia());
                  if (listaSolucionesNodosEmpleado != null) {
                     for (int i = 0; i < listaSolucionesNodosEmpleado.size(); i++) {
                        if (listaSolucionesNodosEmpleado.get(i).getTipo().equals("PAGO")) {
                           subtotalPago = subtotalPago.add(listaSolucionesNodosEmpleado.get(i).getValor());
                        } else {
                           subtotalDescuento = subtotalDescuento.add(listaSolucionesNodosEmpleado.get(i).getValor());
                        }
                     }
                  }
                  if (listaSolucionesNodosEmpleador != null) {
                     for (int i = 0; i < listaSolucionesNodosEmpleador.size(); i++) {
                        if (listaSolucionesNodosEmpleador.get(i).getTipo().equals("PASIVO")) {
                           subtotalPasivo = subtotalPasivo.add(listaSolucionesNodosEmpleador.get(i).getValor());
                        } else if (listaSolucionesNodosEmpleador.get(i).getTipo().equals("GASTO")) {
                           subtotalGasto = subtotalGasto.add(listaSolucionesNodosEmpleador.get(i).getValor());
                        }
                     }
                  }
                  neto = subtotalPago.subtract(subtotalDescuento);
               }
            }
         }
      }
      if (tipoListaComprobantes == 1) {
         infoRegistroComp = String.valueOf(filtradolistaComprobantes.size());
      } else if (listaComprobantes != null) {
         infoRegistroComp = String.valueOf(listaComprobantes.size());
      } else {
         infoRegistroComp = String.valueOf(0);
      }
   }

   public void posicionComprobante() {
      FacesContext context = FacesContext.getCurrentInstance();
      Map<String, String> map = context.getExternalContext().getRequestParameterMap();
      String name = map.get("n"); // name attribute of node
      String type = map.get("t"); // type attribute of node
      int indice = Integer.parseInt(type);
      int columna = Integer.parseInt(name);
      if (tipoListaComprobantes == 0) {
         comprobanteSeleccionado = listaComprobantes.get(indice);
      } else {
         comprobanteSeleccionado = filtradolistaComprobantes.get(indice);
      }
      cambiarIndiceComprobantes(comprobanteSeleccionado, columna);
   }

   public void cargarListasConComprobante() {
      System.out.println("Entro en cargarListasConComprobante()");
      listaCortesProcesos = administrarComprobantes.consultarCortesProcesosComprobante(comprobanteSeleccionado.getSecuencia());
      if (!listaCortesProcesos.isEmpty()) {
         cortesProcesosSeleccionado = listaCortesProcesos.get(0);
         cargarListasSolucionesNodos();
      }
      contarRegistrosCP();
      contarRegistrosTE();
      contarRegistrosTEr();
      RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
   }

   public void cargarListasSolucionesNodos() {
      System.out.println("Entro en cargarListasSolucionesNodos()");
      listaSolucionesNodosEmpleado = administrarComprobantes.consultarSolucionesNodosEmpleado(cortesProcesosSeleccionado.getSecuencia(), empleado.getSecuencia());
      listaSolucionesNodosEmpleador = administrarComprobantes.consultarSolucionesNodosEmpleador(cortesProcesosSeleccionado.getSecuencia(), empleado.getSecuencia());
      if (listaSolucionesNodosEmpleado != null) {
         for (int i = 0; i < listaSolucionesNodosEmpleado.size(); i++) {
            if (listaSolucionesNodosEmpleado.get(i).getTipo().equals("PAGO")) {
               subtotalPago = subtotalPago.add(listaSolucionesNodosEmpleado.get(i).getValor());
            } else {
               subtotalDescuento = subtotalDescuento.add(listaSolucionesNodosEmpleado.get(i).getValor());
            }
         }
      }
      if (listaSolucionesNodosEmpleador != null) {
         for (int i = 0; i < listaSolucionesNodosEmpleador.size(); i++) {
            if (listaSolucionesNodosEmpleador.get(i).getTipo().equals("PASIVO")) {
               subtotalPasivo = subtotalPasivo.add(listaSolucionesNodosEmpleador.get(i).getValor());
            } else if (listaSolucionesNodosEmpleador.get(i).getTipo().equals("GASTO")) {
               subtotalGasto = subtotalGasto.add(listaSolucionesNodosEmpleador.get(i).getValor());
            }
         }
      }
      neto = subtotalPago.subtract(subtotalDescuento);

      RequestContext.getCurrentInstance().update("form:tablaEmpleado");
      RequestContext.getCurrentInstance().update("form:tablaEmpleador");
   }

   public void limpiarListasdeComprobante() {
      listaCortesProcesos.clear();
      listaSolucionesNodosEmpleado.clear();
      listaSolucionesNodosEmpleador.clear();
      contarRegistrosCP();
      contarRegistrosTE();
      contarRegistrosTEr();
      RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
      RequestContext.getCurrentInstance().update("form:tablaEmpleado");
      RequestContext.getCurrentInstance().update("form:tablaEmpleador");
   }

   public void limpiarListasdeCortesProcesos() {
      listaSolucionesNodosEmpleado.clear();
      listaSolucionesNodosEmpleador.clear();
      contarRegistrosTE();
      contarRegistrosTEr();
      RequestContext.getCurrentInstance().update("form:tablaEmpleado");
      RequestContext.getCurrentInstance().update("form:tablaEmpleador");
   }

   public void cambiarIndiceComprobantes(Comprobantes comprobante, int celda) {
      comprobanteSeleccionado = comprobante;
      RequestContext context = RequestContext.getCurrentInstance();
      if (modificacionesSolucionesNodosEmpleado == false && modificacionesSolucionesNodosEmpleador == false) {
         if (modificacionesCortesProcesos == false) {
            if (permitirIndex == true) {
               System.out.println("Esta en cambiarIndiceComprobantes()");
               subtotalPago = new BigDecimal(0);
               subtotalDescuento = new BigDecimal(0);
               subtotalPasivo = new BigDecimal(0);
               subtotalGasto = new BigDecimal(0);
               cualCelda = celda;
               tipoTabla = 0;
               //Para los rastros
               nombreTabla = "Comprobantes";
               secRegistro = comprobanteSeleccionado.getSecuencia();
               auxFechaEntregadoComprobante = comprobanteSeleccionado.getFechaentregado();
               cargarListasConComprobante();

               if (banderaCortesProcesos == 1) {
                  restaurarTablaCortesProcesos();
                  restaurarTablaComprobantes();
                  restaurarTablaEmpleado();
                  restaurarTablaEmpleador();
               }
               tablaExportar = ":formExportar:datosComprobantesExportar";
               nombreArchivoExportar = "ComprobantesXML";

               context.update("form:exportarXML");
               context.update("form:subTotalPago");
               context.update("form:subTotalDescuento");
               context.update("form:subTotalPasivo");
               context.update("form:subTotalGasto");
               context.update("form:neto");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show();");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show();");
      }
   }

   public void cambiarIndiceCortesProcesos(CortesProcesos corteProceso, int celda) {
      cortesProcesosSeleccionado = corteProceso;
      if (modificacionesSolucionesNodosEmpleado == false && modificacionesSolucionesNodosEmpleador == false) {
         System.out.println("Esta en cambiarIndiceCortesProcesos()");
         if (permitirIndex == true) {
            subtotalPago = new BigDecimal(0);
            subtotalDescuento = new BigDecimal(0);
            subtotalPasivo = new BigDecimal(0);
            subtotalGasto = new BigDecimal(0);
            cualCelda = celda;
            tipoTabla = 1;
            nombreTabla = "CortesProcesos";
            secRegistro = cortesProcesosSeleccionado.getSecuencia();
            if (cualCelda == 1) {
               proceso = cortesProcesosSeleccionado.getProceso().getDescripcion();
            }
            cargarListasSolucionesNodos();
         }

         if (banderaComprobantes == 1) {
            restaurarTablaComprobantes();
         }
         tablaExportar = ":formExportar:datosCortesProcesosExportar";
         nombreArchivoExportar = "CortesProcesosXML";
         RequestContext.getCurrentInstance().update("form:exportarXML");
         RequestContext.getCurrentInstance().update("form:subTotalPago");
         RequestContext.getCurrentInstance().update("form:subTotalDescuento");
         RequestContext.getCurrentInstance().update("form:subTotalPasivo");
         RequestContext.getCurrentInstance().update("form:subTotalGasto");
         RequestContext.getCurrentInstance().update("form:neto");
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show();");
      }
   }

   public void restaurarTablaComprobantes() {
      FacesContext c = FacesContext.getCurrentInstance();
      numeroComprobanteC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:numeroComprobanteC");
      numeroComprobanteC.setFilterStyle("display: none; visibility: hidden;");
      fechaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaC");
      fechaC.setFilterStyle("display: none; visibility: hidden;");
      fechaEntregaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaEntregaC");
      fechaEntregaC.setFilterStyle("display: none; visibility: hidden;");
      altoScrollComprobante = "69";
      RequestContext.getCurrentInstance().update("form:datosComprobantes");
      banderaComprobantes = 0;
      tipoListaComprobantes = 0;
      filtradolistaComprobantes = null;
   }

   public void restaurarTablaCortesProcesos() {
      FacesContext c = FacesContext.getCurrentInstance();
      fechaCorteCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:fechaCorteCP");
      fechaCorteCP.setFilterStyle("display: none; visibility: hidden;");
      procesoCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:procesoCP");
      procesoCP.setFilterStyle("display: none; visibility: hidden;");
      altoScrollComprobante = "69";
      RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
      banderaCortesProcesos = 0;
      tipoListaCortesProcesos = 0;
      filtradolistaCortesProcesos = null;
   }

   public void restaurarTablaEmpleado() {
      FacesContext c = FacesContext.getCurrentInstance();
      c_codigo = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:codigoSNE2");
      c_codigo.setFilterStyle("display: none; visibility: hidden;");
      c_descripcion = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:descripcionSNE2");
      c_descripcion.setFilterStyle("display: none; visibility: hidden;");
      c_fDesde = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:fechaDesdeSNE");
      c_fDesde.setFilterStyle("display: none; visibility: hidden;");
      c_fHasta = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:fechaHastaSNE");
      c_fHasta.setFilterStyle("display: none; visibility: hidden;");
      c_unidad = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:unidadSNE");
      c_unidad.setFilterStyle("display: none; visibility: hidden;");
      c_pago = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:pasivoSNE");
      c_pago.setFilterStyle("display: none; visibility: hidden;");
      c_descuento = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:gastoSNE");
      c_descuento.setFilterStyle("display: none; visibility: hidden;");
      c_tercero = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:terceroSNE");
      c_tercero.setFilterStyle("display: none; visibility: hidden;");
      c_cuentaD = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:debitoSNE");
      c_cuentaD.setFilterStyle("display: none; visibility: hidden;");
      c_cuentaC = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:creditoSNE");
      c_cuentaC.setFilterStyle("display: none; visibility: hidden;");
      c_CCDebito = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:centroCostoDSNE");
      c_CCDebito.setFilterStyle("display: none; visibility: hidden;");
      c_CCCredito = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:centroCostoCSNE");
      c_CCCredito.setFilterStyle("display: none; visibility: hidden;");
      c_saldo = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:saldoSNE");
      c_saldo.setFilterStyle("display: none; visibility: hidden;");
      c_fechaPago = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:pagoSNE");
      c_fechaPago.setFilterStyle("display: none; visibility: hidden;");
      banderaCortesProcesos = 0;
      tipoListaSNEmpleado = 0;
      RequestContext.getCurrentInstance().update("form:tablaEmpleado");
   }

   public void restaurarTablaEmpleador() {
      FacesContext c = FacesContext.getCurrentInstance();
      cr_codigo = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:codigoSNEM");
      cr_codigo.setFilterStyle("display: none; visibility: hidden;");
      cr_descripcion = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:descripcionSNEM");
      cr_descripcion.setFilterStyle("display: none; visibility: hidden;");
      cr_fDesde = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:fechaDesdeSNE");
      cr_fDesde.setFilterStyle("display: none; visibility: hidden;");
      cr_fHasta = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:fechaHastaSNEM");
      cr_fHasta.setFilterStyle("display: none; visibility: hidden;");
      cr_unidad = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:unidadSNEM");
      cr_unidad.setFilterStyle("display: none; visibility: hidden;");
      cr_pasivo = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:pasivoSNEM");
      cr_pasivo.setFilterStyle("display: none; visibility: hidden;");
      cr_gasto = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:gastoSNEM");
      cr_gasto.setFilterStyle("display: none; visibility: hidden;");
      cr_tercero = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:terceroSNEM");
      cr_tercero.setFilterStyle("display: none; visibility: hidden;");
      cr_cuentaD = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:debitoSNEM");
      cr_cuentaD.setFilterStyle("display: none; visibility: hidden;");
      cr_cuentaC = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:creditoSNEM");
      cr_cuentaC.setFilterStyle("display: none; visibility: hidden;");
      cr_CCDebito = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:centroCostoDSNEM");
      cr_CCDebito.setFilterStyle("display: none; visibility: hidden;");
      cr_CCCredito = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:centroCostoCSNEM");
      cr_CCCredito.setFilterStyle("display: none; visibility: hidden;");
      cr_saldo = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:saldoSNEM");
      cr_saldo.setFilterStyle("display: none; visibility: hidden;");
      cr_fechaPago = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:pagoSNEM");
      cr_fechaPago.setFilterStyle("display: none; visibility: hidden;");
      banderaCortesProcesos = 0;
      tipoListaSNEmpleador = 0;
      RequestContext.getCurrentInstance().update("form:tablaEmpleador");
   }

   public void cambiarIndiceSolucionesNodosEmpleado(SolucionesNodos snEmpleado, int celda) {
      empleadoTablaSeleccionado = snEmpleado;
      if (permitirIndex == true) {
         cualCelda = celda;
         tipoTabla = 2;
         nombreTabla = "SolucionesNodos";
         tablaExportar = ":formExportar:solucionesNodoEmpleadoExportar";
         secRegistro = empleadoTablaSeleccionado.getSecuencia();
         if (cualCelda == 7) {
            tercero = empleadoTablaSeleccionado.getNombretercero();
         }
         if (cualCelda == 8) {
            codigoDebito = empleadoTablaSeleccionado.getCodigocuentad();
         }
         if (cualCelda == 9) {
            centroCostoDebito = empleadoTablaSeleccionado.getNombrecentrocostod();
         }
         if (cualCelda == 10) {
            codigoCredito = empleadoTablaSeleccionado.getCodigocuentac();
         }
         if (cualCelda == 11) {
            centroCostoCredito = empleadoTablaSeleccionado.getNombrecentrocostoc();
         }
      }
      nombreArchivoExportar = "SolucionesNodosEmpleadoXML";
   }

   public void cambiarIndiceSolucionesNodosEmpleador(SolucionesNodos snEmpleador, int celda) {
      empleadorTablaSeleccionado = snEmpleador;
      if (permitirIndex == true) {
         cualCelda = celda;
         tipoTabla = 3;
         nombreTabla = "SolucionesNodos";
         tablaExportar = ":formExportar:solucionesNodoEmpleadorExportar";
         secRegistro = empleadorTablaSeleccionado.getSecuencia();
         if (cualCelda == 7) {
            tercero = empleadorTablaSeleccionado.getNombretercero();
         }
         if (cualCelda == 8) {
            codigoDebito = empleadorTablaSeleccionado.getCodigocuentad();
         }
         if (cualCelda == 9) {
            centroCostoDebito = empleadorTablaSeleccionado.getNombrecentrocostod();
         }
         if (cualCelda == 10) {
            codigoCredito = empleadorTablaSeleccionado.getCodigocuentac();
         }
         if (cualCelda == 11) {
            centroCostoCredito = empleadorTablaSeleccionado.getNombrecentrocostoc();
         }
      }
      nombreArchivoExportar = "SolucionesNodosEmpleadorXML";
   }

   public void modificarComprobantesFechaEntregado(Comprobantes comprobante, int celda) {
      comprobanteSeleccionado = comprobante;
      System.out.println("modificarComprobantesFechaEntregado()");
      if (comprobanteSeleccionado.getFechaentregado() == null) {
         System.out.println("comprobanteSeleccionado.getFechaentregado() == null");
         comprobanteSeleccionado.setFechaentregado(auxFechaEntregadoComprobante);
         RequestContext.getCurrentInstance().execute("PF('errorFechaEntregaNull').show()");
      }
      modificarComprobantes(comprobante);
      RequestContext.getCurrentInstance().update("form:datosComprobantes");
   }

   public void modificarComprobantes(Comprobantes comprobante) {
      System.out.println("modificarComprobantes()");
      comprobanteSeleccionado = comprobante;
      if (!listaComprobantesCrear.contains(comprobanteSeleccionado)) {
         if (listaComprobantesModificar.isEmpty()) {
            listaComprobantesModificar.add(comprobanteSeleccionado);
         } else if (!listaComprobantesModificar.contains(comprobanteSeleccionado)) {
            listaComprobantesModificar.add(comprobanteSeleccionado);
         }
      }
      cargarListasConComprobante();
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      secRegistro = null;
      tipoTabla = -1;
      modificacionesComprobantes = true;
      RequestContext.getCurrentInstance().update("form:datosComprobantes");
   }

   public void modificarCortesProcesos(CortesProcesos corteProceso, String confirmarCambio, String valorConfirmar) {
      cortesProcesosSeleccionado = corteProceso;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equals("N")) {
         if (!listaCortesProcesosCrear.contains(cortesProcesosSeleccionado)) {
            if (listaCortesProcesosModificar.isEmpty()) {
               listaCortesProcesosModificar.add(cortesProcesosSeleccionado);
            } else if (!listaCortesProcesosModificar.contains(cortesProcesosSeleccionado)) {
               listaCortesProcesosModificar.add(cortesProcesosSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         secRegistro = null;
         tipoTabla = -1;
         modificacionesCortesProcesos = true;
         RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
      } else if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
         cortesProcesosSeleccionado.getProceso().setDescripcion(proceso);
         for (int i = 0; i < lovProcesos.size(); i++) {
            if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            cortesProcesosSeleccionado.setProceso(lovProcesos.get(indiceUnicoElemento));
//                lovProcesos.clear();
//                getListaProcesos();
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
            contarRegistrosLovProc();
            RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
      if (coincidencias == 1) {
         if (!listaCortesProcesosCrear.contains(cortesProcesosSeleccionado)) {
            if (listaCortesProcesosModificar.isEmpty()) {
               listaCortesProcesosModificar.add(cortesProcesosSeleccionado);
            } else if (!listaCortesProcesosModificar.contains(cortesProcesosSeleccionado)) {
               listaCortesProcesosModificar.add(cortesProcesosSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

         }
         modificacionesCortesProcesos = true;
         secRegistro = null;
      }

      RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
   }

   //AUTOCOMPLETAR NUEVO Y DUPLICADO
   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("PROCESO")) {
         if (tipoNuevo == 1) {
            proceso = nuevoCorteProceso.getProceso().getDescripcion();
         } else if (tipoNuevo == 2) {
            proceso = duplicarCorteProceso.getProceso().getDescripcion();
         }
      }
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
         if (tipoNuevo == 1) {
            nuevoCorteProceso.getProceso().setDescripcion(proceso);
         } else if (tipoNuevo == 2) {
            duplicarCorteProceso.getProceso().setDescripcion(proceso);
         }
         for (int i = 0; i < lovProcesos.size(); i++) {
            if (lovProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoCorteProceso.setProceso(lovProcesos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProceso");
            } else if (tipoNuevo == 2) {
               duplicarCorteProceso.setProceso(lovProcesos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoProceso");
            }
//                lovProcesos.clear();
//                getListaProcesos();
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
            contarRegistrosLovProc();
            RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProceso");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoProceso");
            }
         }
      }
   }

   public void modificarSolucionesNodos(String tipoDato, SolucionesNodos sn, String valorConfirmar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoDato.equals("TERCERO")) {
         if (tipoTabla == 2) {
            empleadoTablaSeleccionado = sn;
            empleadoTablaSeleccionado.setNombretercero(tercero);
         } else if (tipoTabla == 3) {
            empleadorTablaSeleccionado = sn;
            empleadorTablaSeleccionado.setNombretercero(tercero);
         }

         for (int i = 0; i < lovTerceros.size(); i++) {
            if (lovTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }

         if (coincidencias == 1) {
            if (tipoTabla == 2) {
               empleadoTablaSeleccionado.setNittercero(lovTerceros.get(indiceUnicoElemento).getSecuencia());
               empleadoTablaSeleccionado.setNombretercero(lovTerceros.get(indiceUnicoElemento).getNombre());
            } else if (tipoTabla == 3) {
               empleadorTablaSeleccionado.setNittercero(lovTerceros.get(indiceUnicoElemento).getSecuencia());
               empleadorTablaSeleccionado.setNombretercero(lovTerceros.get(indiceUnicoElemento).getNombre());
            }
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
            contarRegistrosLovTercero();
            RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').show()");
         }
      }
      if (tipoDato.equals("CREDITO")) {
         if (tipoTabla == 2) {
            empleadoTablaSeleccionado = sn;
            empleadoTablaSeleccionado.setCodigocuentac(codigoCredito);
         } else if (tipoTabla == 3) {
            empleadorTablaSeleccionado = sn;
            empleadorTablaSeleccionado.setCodigocuentac(codigoCredito);
         }

         for (int i = 0; i < lovCuentas.size(); i++) {
            if (lovCuentas.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoTabla == 2) {
               empleadoTablaSeleccionado.setCuentac(lovCuentas.get(indiceUnicoElemento).getSecuencia());
               empleadoTablaSeleccionado.setCodigocuentac(lovCuentas.get(indiceUnicoElemento).getCodigo());
            } else if (tipoTabla == 3) {
               empleadorTablaSeleccionado.setCuentac(lovCuentas.get(indiceUnicoElemento).getSecuencia());
               empleadorTablaSeleccionado.setCodigocuentac(lovCuentas.get(indiceUnicoElemento).getCodigo());
            }
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:CuentaCreditoDialogo");
            contarRegistrosLovCuentasC();
            RequestContext.getCurrentInstance().execute("PF('CuentaCreditoDialogo').show()");
         }
      }
      if (tipoDato.equals("DEBITO")) {
         if (tipoTabla == 2) {
            empleadoTablaSeleccionado = sn;
            empleadoTablaSeleccionado.setCodigocuentad(codigoDebito);
         } else if (tipoTabla == 3) {
            empleadorTablaSeleccionado = sn;
            empleadorTablaSeleccionado.setCodigocuentad(codigoDebito);
         }

         for (int i = 0; i < lovCuentas.size(); i++) {
            if (lovCuentas.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoTabla == 2) {
               empleadoTablaSeleccionado.setCuentad(lovCuentas.get(indiceUnicoElemento).getSecuencia());
               empleadoTablaSeleccionado.setCodigocuentad(lovCuentas.get(indiceUnicoElemento).getCodigo());
            } else if (tipoTabla == 3) {
               empleadorTablaSeleccionado.setCuentad(lovCuentas.get(indiceUnicoElemento).getSecuencia());
               empleadorTablaSeleccionado.setCodigocuentad(lovCuentas.get(indiceUnicoElemento).getCodigo());
            }
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:CuentaDebitoDialogo");
            contarRegistrosLovCuentasD();
            RequestContext.getCurrentInstance().execute("PF('CuentaDebitoDialogo').show()");
         }
      }
      if (tipoDato.equals("CENTROCOSTODEBITO")) {
         if (tipoTabla == 2) {
            empleadoTablaSeleccionado = sn;
            empleadoTablaSeleccionado.setNombrecentrocostod(centroCostoDebito);
         } else if (tipoTabla == 3) {
            empleadorTablaSeleccionado = sn;
            empleadorTablaSeleccionado.setNombrecentrocostod(centroCostoDebito);
         }

         for (int i = 0; i < lovCentrosCostos.size(); i++) {
            if (lovCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoTabla == 2) {
               empleadoTablaSeleccionado.setCentrocostod(lovCentrosCostos.get(indiceUnicoElemento).getSecuencia());
               empleadoTablaSeleccionado.setNombrecentrocostod(lovCentrosCostos.get(indiceUnicoElemento).getNombre());
            } else if (tipoTabla == 3) {
               empleadorTablaSeleccionado.setCentrocostod(lovCentrosCostos.get(indiceUnicoElemento).getSecuencia());
               empleadorTablaSeleccionado.setNombrecentrocostod(lovCentrosCostos.get(indiceUnicoElemento).getNombre());
            }
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:CentroCostoDebitoDialogo");
            contarRegistrosLovCCD();
            RequestContext.getCurrentInstance().execute("PF('CentroCostoDebitoDialogo').show()");
         }
      }
      if (tipoDato.equals("CENTROCOSTOCREDITO")) {
         if (tipoTabla == 2) {
            empleadoTablaSeleccionado = sn;
            empleadoTablaSeleccionado.setNombrecentrocostoc(centroCostoCredito);
         } else if (tipoTabla == 3) {
            empleadorTablaSeleccionado = sn;
            empleadorTablaSeleccionado.setNombrecentrocostoc(centroCostoCredito);
         }

         for (int i = 0; i < lovCentrosCostos.size(); i++) {
            if (lovCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoTabla == 2) {
               empleadoTablaSeleccionado.setCentrocostoc(lovCentrosCostos.get(indiceUnicoElemento).getSecuencia());
               empleadoTablaSeleccionado.setNombrecentrocostoc(lovCentrosCostos.get(indiceUnicoElemento).getNombre());
            } else if (tipoTabla == 3) {
               empleadorTablaSeleccionado.setCentrocostoc(lovCentrosCostos.get(indiceUnicoElemento).getSecuencia());
               empleadorTablaSeleccionado.setNombrecentrocostoc(lovCentrosCostos.get(indiceUnicoElemento).getNombre());
            }
         } else {
            permitirIndex = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:CentroCostoCreditoDialogo");
            contarRegistrosLovCCC();
            RequestContext.getCurrentInstance().execute("PF('CentroCostoCreditoDialogo').show()");
         }
      }
      if (coincidencias == 1) {
         if (tipoTabla == 2) {
            if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
               listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
               listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            }
            modificacionesSolucionesNodosEmpleado = true;
            RequestContext.getCurrentInstance().update("form:tablaEmpleado");
         } else if (tipoTabla == 3) {
            if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
               listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
               listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            }
            modificacionesSolucionesNodosEmpleador = true;
            RequestContext.getCurrentInstance().update("form:tablaEmpleador");
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         secRegistro = null;
      }
   }
   //LOV PROCESOS

   public void llamarLOVProcesos(CortesProcesos corteProceso) {
      cortesProcesosSeleccionado = corteProceso;
      tipoActualizacion = 0;
      RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
      contarRegistrosLovProc();
      RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
   }

   public void llamarLOVProcesosNuevo_Duplicado(int tipoN) {
      if (tipoN == 1) {
         tipoActualizacion = 1;
      } else {
         tipoActualizacion = 2;
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
      contarRegistrosLovProc();
      RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
   }

   //LOVs Soluciones Nodos
   public void llamarLovsSolucionesNodos(SolucionesNodos sn, int tipoTab, int campo) {
      tipoTabla = tipoTab;
      if (tipoTab == 2) {
         empleadoTablaSeleccionado = sn;
      } else if (tipoTab == 3) {
         empleadorTablaSeleccionado = sn;
      }
      if (campo == 7) {
         llamarLOVTerceros();
      } else if (campo == 8) {
         llamarLOVCuentaDebito();
      } else if (campo == 9) {
         llamarLOVCentroCostoDebito();
      } else if (campo == 10) {
         llamarLOVCuentaCredito();
      } else if (campo == 11) {
         llamarLOVCentroCostoCredito();
      }
   }

   public void llamarLOVTerceros() {
      RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
      contarRegistrosLovTercero();
      RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').show()");
   }

   public void llamarLOVCuentaDebito() {
      RequestContext.getCurrentInstance().update("formularioDialogos:CuentaDebitoDialogo");
      contarRegistrosLovCuentasD();
      RequestContext.getCurrentInstance().execute("PF('CuentaDebitoDialogo').show()");
   }

   public void llamarLOVCuentaCredito() {
      RequestContext.getCurrentInstance().update("formularioDialogos:CuentaCreditoDialogo");
      contarRegistrosLovCuentasC();
      RequestContext.getCurrentInstance().execute("PF('CuentaCreditoDialogo').show()");
   }

   public void llamarLOVCentroCostoDebito() {
      RequestContext.getCurrentInstance().update("formularioDialogos:CentroCostoDebitoDialogo");
      contarRegistrosLovCCD();
      RequestContext.getCurrentInstance().execute("PF('CentroCostoDebitoDialogo').show()");
   }

   public void llamarLOVCentroCostoCredito() {
      RequestContext.getCurrentInstance().update("formularioDialogos:CentroCostoCreditoDialogo");
      contarRegistrosLovCCC();
      RequestContext.getCurrentInstance().execute("PF('CentroCostoCreditoDialogo').show()");
   }

   public void activarAceptar() {
      aceptar = false;
   }

   //CAMBIAR PROCESO
   public void actualizarProceso() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         cortesProcesosSeleccionado.setProceso(procesoSelecionado);
         if (!listaCortesProcesosCrear.contains(cortesProcesosSeleccionado)) {
            if (listaCortesProcesosModificar.isEmpty()) {
               listaCortesProcesosModificar.add(cortesProcesosSeleccionado);
            } else if (!listaCortesProcesosModificar.contains(cortesProcesosSeleccionado)) {
               listaCortesProcesosModificar.add(cortesProcesosSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
         modificacionesCortesProcesos = true;
         permitirIndex = true;
      } else if (tipoActualizacion == 1) {
         nuevoCorteProceso.setProceso(procesoSelecionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCorteProceso");
      } else if (tipoActualizacion == 2) {
         duplicarCorteProceso.setProceso(procesoSelecionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoCorteProceso");
      }
      filtradoProcesos = null;
      procesoSelecionado = null;
      aceptar = true;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      tipoTabla = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovProcesos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
      context.reset("formularioDialogos:lovProcesos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProcesos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').hide()");
   }

   public void cancelarProceso() {
      filtradoProcesos = null;
      procesoSelecionado = null;
      aceptar = true;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      tipoTabla = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovProcesos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
      context.reset("formularioDialogos:lovProcesos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovProcesos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').hide()");
   }

   //ACTUALIZAR TERCERO
   public void actualizarTercero() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoTabla == 2) {
         empleadoTablaSeleccionado.setNittercero(TerceroSelecionado.getSecuencia());
         empleadoTablaSeleccionado.setNombretercero(TerceroSelecionado.getNombre());
         if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
            listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
         } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
            listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tablaEmpleado");
         modificacionesSolucionesNodosEmpleado = true;
         permitirIndex = true;
      } else if (tipoTabla == 3) {
         empleadorTablaSeleccionado.setNittercero(TerceroSelecionado.getSecuencia());
         empleadorTablaSeleccionado.setNombretercero(TerceroSelecionado.getNombre());
         if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
            listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
         } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
            listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tablaEmpleador");
         modificacionesSolucionesNodosEmpleador = true;
         permitirIndex = true;
      }

      filtradolistaTerceros = null;
      TerceroSelecionado = null;
      aceptar = true;
      secRegistro = null;
      cualCelda = -1;
      tipoTabla = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
      context.reset("formularioDialogos:lovTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').hide()");
   }

   public void cancelarTercero() {
      filtradolistaTerceros = null;
      TerceroSelecionado = null;
      aceptar = true;
      secRegistro = null;
      permitirIndex = true;
      tipoTabla = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:TercerosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
      context.reset("formularioDialogos:lovTerceros:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('TercerosDialogo').hide()");
   }

   public void actualizarCuentaDebito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoTabla == 2) {
         empleadoTablaSeleccionado.setCuentad(cuentaSeleccionada.getSecuencia());
         empleadoTablaSeleccionado.setCodigocuentad(cuentaSeleccionada.getCodigo());
         if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
            listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
         } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
            listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tablaEmpleado");
         modificacionesSolucionesNodosEmpleado = true;
         permitirIndex = true;
      } else if (tipoTabla == 3) {
         empleadorTablaSeleccionado.setCuentad(cuentaSeleccionada.getSecuencia());
         empleadorTablaSeleccionado.setCodigocuentad(cuentaSeleccionada.getCodigo());
         if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
            listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
         } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
            listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tablaEmpleador");
         modificacionesSolucionesNodosEmpleador = true;
         permitirIndex = true;
      }

      filtrarLovCuentas = null;
      cuentaSeleccionada = new Cuentas();
      aceptar = true;
      secRegistro = null;
      cualCelda = -1;
      tipoTabla = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:CuentaDebitoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovCuentaDebito");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCD");
      context.reset("formularioDialogos:lovCuentaDebito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCuentaDebito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CuentaDebitoDialogo').hide()");
   }

   public void cancelarCuentaDebito() {
      filtrarLovCuentas = null;
      cuentaSeleccionada = new Cuentas();
      aceptar = true;
      secRegistro = null;
      permitirIndex = true;
      tipoTabla = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:CuentaDebitoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovCuentaDebito");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCD");
      context.reset("formularioDialogos:lovCuentaDebito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCuentaDebito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CuentaDebitoDialogo').hide()");
   }

   public void actualizarCuentaCredito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoTabla == 2) {
         empleadoTablaSeleccionado.setCuentac(cuentaSeleccionada.getSecuencia());
         empleadoTablaSeleccionado.setCodigocuentac(cuentaSeleccionada.getCodigo());
         if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
            listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
         } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
            listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tablaEmpleado");
         modificacionesSolucionesNodosEmpleado = true;
         permitirIndex = true;
      } else if (tipoTabla == 3) {
         empleadorTablaSeleccionado.setCuentac(cuentaSeleccionada.getSecuencia());
         empleadorTablaSeleccionado.setCodigocuentac(cuentaSeleccionada.getCodigo());
         if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
            listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
         } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
            listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tablaEmpleador");
         modificacionesSolucionesNodosEmpleador = true;
         permitirIndex = true;
      }
      filtrarLovCuentas = null;
      cuentaSeleccionada = new Cuentas();
      aceptar = true;
      secRegistro = null;
      cualCelda = -1;
      tipoTabla = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:CuentaCreditoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovCuentaCredito");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCC");
      context.reset("formularioDialogos:lovCuentaCredito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCuentaCredito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CuentaCreditoDialogo').hide()");
   }

   public void cancelarCuentaCredito() {
      filtrarLovCuentas = null;
      cuentaSeleccionada = new Cuentas();
      aceptar = true;
      secRegistro = null;
      permitirIndex = true;
      tipoTabla = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:CuentaCreditoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovCuentaCredito");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCC");
      context.reset("formularioDialogos:lovCuentaCredito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCuentaCredito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CuentaCreditoDialogo').hide()");
   }

   public void actualizarCentroCostoCredito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoTabla == 2) {
         empleadoTablaSeleccionado.setCentrocostoc(centroCostoSeleccionado.getSecuencia());
         empleadoTablaSeleccionado.setNombrecentrocostoc(centroCostoSeleccionado.getNombre());
         if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
            listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
         } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
            listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tablaEmpleado");
         modificacionesSolucionesNodosEmpleado = true;
         permitirIndex = true;
      } else if (tipoTabla == 3) {
         empleadorTablaSeleccionado.setCentrocostoc(centroCostoSeleccionado.getSecuencia());
         empleadorTablaSeleccionado.setNombrecentrocostoc(centroCostoSeleccionado.getNombre());
         if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
            listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
         } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
            listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tablaEmpleador");
         modificacionesSolucionesNodosEmpleador = true;
         permitirIndex = true;
      }
      filtrarLovCentrosCostos = null;
      centroCostoSeleccionado = null;
      aceptar = true;
      secRegistro = null;
      cualCelda = -1;
      tipoTabla = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:CentroCostoCreditoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovCentroCostoCredito");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCCC");
      context.reset("formularioDialogos:lovCentroCostoCredito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoCredito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoCreditoDialogo').hide()");
   }

   public void cancelarCentroCostoCredito() {
      filtrarLovCentrosCostos = null;
      centroCostoSeleccionado = null;
      aceptar = true;
      secRegistro = null;
      permitirIndex = true;
      tipoTabla = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:CentroCostoCreditoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovCentroCostoCredito");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCCC");
      context.reset("formularioDialogos:lovCentroCostoCredito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoCredito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoCreditoDialogo').hide()");
   }

   public void actualizarCentroCostoDebito() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoTabla == 2) {
         empleadoTablaSeleccionado.setCentrocostod(centroCostoSeleccionado.getSecuencia());
         empleadoTablaSeleccionado.setNombrecentrocostod(centroCostoSeleccionado.getNombre());
         if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
            listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
         } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
            listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tablaEmpleado");
         modificacionesSolucionesNodosEmpleado = true;
         permitirIndex = true;
      } else if (tipoTabla == 3) {
         empleadorTablaSeleccionado.setCentrocostod(centroCostoSeleccionado.getSecuencia());
         empleadorTablaSeleccionado.setNombrecentrocostod(centroCostoSeleccionado.getNombre());
         if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
            listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
         } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
            listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
         }

         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:tablaEmpleador");
         modificacionesSolucionesNodosEmpleador = true;
         permitirIndex = true;
      }

      aceptar = true;
      secRegistro = null;
      cualCelda = -1;
      tipoTabla = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:CentroCostoDebitoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovCentroCostoDebito");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCCD");
      context.reset("formularioDialogos:lovCentroCostoDebito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoDebito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoDebitoDialogo').hide()");
   }

   public void cancelarCentroCostoDebito() {
      filtrarLovCentrosCostos = null;
      centroCostoSeleccionado = null;
      aceptar = true;
      secRegistro = null;
      permitirIndex = true;
      tipoTabla = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:CentroCostoDebitoDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovCentroCostoDebito");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarCCD");
      context.reset("formularioDialogos:lovCentroCostoDebito:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovCentroCostoDebito').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('CentroCostoDebitoDialogo').hide()");
   }

   //BORRAR VC
   public void borrar() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoTabla == 0) {
         if (comprobanteSeleccionado != null) {
            if (administrarComprobantes.consultarCortesProcesosComprobante(comprobanteSeleccionado.getSecuencia()).isEmpty()) {
               if (!listaComprobantesModificar.isEmpty() && listaComprobantesModificar.contains(comprobanteSeleccionado)) {
                  listaComprobantesModificar.remove(comprobanteSeleccionado);
                  listaComprobantesBorrar.add(comprobanteSeleccionado);
               } else if (!listaComprobantesCrear.isEmpty() && listaComprobantesCrear.contains(comprobanteSeleccionado)) {
                  listaComprobantesCrear.remove(comprobanteSeleccionado);
               } else {
                  listaComprobantesBorrar.add(comprobanteSeleccionado);
               }
               listaComprobantes.remove(comprobanteSeleccionado);
               modificacionesComprobantes = true;
            } else if (!listaComprobantesCrear.isEmpty() && listaComprobantesCrear.contains(comprobanteSeleccionado)) {
               listaComprobantesCrear.remove(comprobanteSeleccionado);
               listaComprobantes.remove(comprobanteSeleccionado);
               modificacionesComprobantes = true;
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorBorrarComprobante').show();");
            }
            if (tipoListaComprobantes == 1) {
               filtradolistaComprobantes.remove(comprobanteSeleccionado);
            }
            RequestContext.getCurrentInstance().update("form:datosComprobantes");
            comprobanteSeleccionado = null;
         }
      } else if (tipoTabla == 1) {
         if (cortesProcesosSeleccionado != null) {
            if (administrarComprobantes.consultarSolucionesNodosEmpleado(cortesProcesosSeleccionado.getSecuencia(), secuenciaEmpleado).isEmpty() && administrarComprobantes.consultarSolucionesNodosEmpleador(cortesProcesosSeleccionado.getSecuencia(), secuenciaEmpleado).isEmpty()) {
               if (!listaCortesProcesosModificar.isEmpty() && listaCortesProcesosModificar.contains(cortesProcesosSeleccionado)) {
                  listaCortesProcesosModificar.remove(cortesProcesosSeleccionado);
                  listaCortesProcesosBorrar.add(cortesProcesosSeleccionado);
               } else {
                  listaCortesProcesosBorrar.add(cortesProcesosSeleccionado);
               }
               listaCortesProcesos.remove(cortesProcesosSeleccionado);
               modificacionesCortesProcesos = true;
            } else if (!listaCortesProcesosCrear.isEmpty() && listaCortesProcesosCrear.contains(cortesProcesosSeleccionado)) {
               listaCortesProcesosCrear.remove(cortesProcesosSeleccionado);
               listaCortesProcesos.remove(cortesProcesosSeleccionado);
               modificacionesCortesProcesos = true;
            } else {
               RequestContext.getCurrentInstance().execute("PF('errorBorrarCortesProcesos').show();");
            }
            if (tipoListaCortesProcesos == 1) {
               filtradolistaCortesProcesos.remove(cortesProcesosSeleccionado);
            }
            RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
            cortesProcesosSeleccionado = null;
         }
      }
      secRegistro = null;
      tipoTabla = -1;
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }
//CREAR COMPROBANTE Y CORTE PROCESO

   public void nuevoRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (!listaComprobantes.isEmpty()) {
         if (listaCortesProcesos.isEmpty()) {
            if (comprobanteSeleccionado != null) {
               RequestContext.getCurrentInstance().execute("PF('NuevoRegistroPagina').show()");
            }
         } else if (tipoTabla == 0) {
            BigInteger sugerenciaNumero = administrarComprobantes.consultarMaximoNumeroComprobante().add(new BigInteger("1"));
            nuevoComprobante.setNumero(sugerenciaNumero);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoComprobante");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroComprobantes').show()");
         } else if (tipoTabla == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCorteProceso");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCortesProcesos').show()");
         }
      } else {
         BigInteger sugerenciaNumero = administrarComprobantes.consultarMaximoNumeroComprobante().add(new BigInteger("1"));
         nuevoComprobante.setNumero(sugerenciaNumero);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoComprobante");
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroComprobantes').show()");
      }
   }

   public void agregarNuevoComprobante() {
      int pasa = 0;
      mensajeValidacion = "";
      if (nuevoComprobante.getNumero() == null) {
         mensajeValidacion = " * Numero \n";
         pasa++;
      }
      if (nuevoComprobante.getFecha() == null) {
         mensajeValidacion = " * Fecha Comprobante \n";
         pasa++;
      }
      if (nuevoComprobante.isCheckEntregado() == true) {
         if (nuevoComprobante.getFechaentregado() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Entrega \n";
            pasa++;
         }
      }
      if (pasa == 0) {
         if (banderaComprobantes == 1) {
            restaurarTablaComprobantes();
         }
         //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
         k++;
         l = BigInteger.valueOf(k);
         nuevoComprobante.setSecuencia(l);
         nuevoComprobante.setEmpleado(empleado);
         nuevoComprobante.setValor(new BigDecimal(0));
         listaComprobantesCrear.add(nuevoComprobante);
         listaComprobantes.add(nuevoComprobante);
         comprobanteSeleccionado = listaComprobantes.get(listaComprobantes.indexOf(nuevoComprobante));
         nuevoComprobante = new Comprobantes();
         RequestContext.getCurrentInstance().update("form:datosComprobantes");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroComprobantes').hide()");
         modificacionesComprobantes = true;
         tipoTabla = -1;
         secRegistro = null;
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoComprobante");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevoComprobante').show()");
      }
      contarRegistrosComp();
   }

   //LIMPIAR NUEVO REGISTRO COMPROBANTES
   public void limpiarNuevoComprobante() {
      nuevoComprobante = new Comprobantes();
      tipoTabla = -1;
      secRegistro = null;
   }

   public void agregarNuevoCorteProceso() {
      int pasa = 0;
      mensajeValidacion = "";
      if (nuevoCorteProceso.getCorte() == null) {
         mensajeValidacion = " * Fecha de corte \n";
         pasa++;
      }
      if (nuevoCorteProceso.getProceso().getSecuencia() == null) {
         mensajeValidacion = " * Proceso \n";
         pasa++;
      }
      if (pasa == 0) {
         if (banderaCortesProcesos == 1) {
            restaurarTablaCortesProcesos();
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoCorteProceso.setSecuencia(l);
         nuevoCorteProceso.setEmpleado(empleado);
         nuevoCorteProceso.setComprobante(comprobanteSeleccionado);
         listaCortesProcesosCrear.add(nuevoCorteProceso);
         listaCortesProcesos.add(nuevoCorteProceso);
         cortesProcesosSeleccionado = listaCortesProcesos.get(listaCortesProcesos.indexOf(nuevoCorteProceso));
         nuevoCorteProceso = new CortesProcesos();
         nuevoCorteProceso.setProceso(new Procesos());
         RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCortesProcesos').hide()");
         modificacionesCortesProcesos = true;
         tipoTabla = -1;
         secRegistro = null;
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoCorteProceso");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevoCorteProceso').show()");
      }
   }

   //LIMPIAR NUEVO REGISTRO CORTE PROCESO
   public void limpiarNuevoCorteProceso() {
      nuevoCorteProceso = new CortesProcesos();
      nuevoCorteProceso.setProceso(new Procesos());
      tipoTabla = -1;
      secRegistro = null;
   }

   //DUPLICADOS
   public void duplicarC() {
      if (tipoTabla == 0) {
         if (comprobanteSeleccionado != null) {
            duplicarComprobante = new Comprobantes();
            duplicarComprobante.setNumero(comprobanteSeleccionado.getNumero());
            duplicarComprobante.setFecha(comprobanteSeleccionado.getFecha());
            duplicarComprobante.setFechaentregado(comprobanteSeleccionado.getFechaentregado());
            duplicarComprobante.setEmpleado(comprobanteSeleccionado.getEmpleado());
            duplicarComprobante.setValor(new BigDecimal(0));

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoComprobante");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroComprobantes').show()");
            secRegistro = null;
         }
      } else if (tipoTabla == 1) {
         if (cortesProcesosSeleccionado != null) {
            duplicarCorteProceso = new CortesProcesos();
            duplicarCorteProceso.setCorte(cortesProcesosSeleccionado.getCorte());
            duplicarCorteProceso.setProceso(cortesProcesosSeleccionado.getProceso());
            duplicarCorteProceso.setEmpleado(cortesProcesosSeleccionado.getEmpleado());
            duplicarCorteProceso.setComprobante(comprobanteSeleccionado);

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicadoCorteProceso");
            RequestContext.getCurrentInstance().execute("PF('DuplicarCortesProcesos').show()");
            secRegistro = null;
         }
      }
   }

   public void confirmarDuplicarComprobantes() {
      int pasa = 0;
      mensajeValidacion = "";
      if (duplicarComprobante.getNumero() == null) {
         mensajeValidacion = " * Numero \n";
         pasa++;
      }
      if (duplicarComprobante.getFecha() == null) {
         mensajeValidacion = " * Fecha Comprobante \n";
         pasa++;
      }
      if (duplicarComprobante.isCheckEntregado() == true) {
         if (duplicarComprobante.getFechaentregado() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Entrega \n";
            pasa++;
         }
      }
      if (pasa == 0) {
         k++;
         l = BigInteger.valueOf(k);
         duplicarComprobante.setSecuencia(l);
         listaComprobantes.add(duplicarComprobante);
         listaComprobantesCrear.add(duplicarComprobante);
         comprobanteSeleccionado = listaComprobantes.get(listaComprobantes.indexOf(duplicarComprobante));
         RequestContext.getCurrentInstance().update("form:datosComprobantes");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroComprobantes').hide()");
         tipoTabla = -1;
         modificacionesComprobantes = true;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (banderaComprobantes == 1) {
            restaurarTablaComprobantes();
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoComprobante");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevoComprobante').show()");
      }
   }

   //LIMPIAR DUPLICADO REGISTRO COMPROBANTES
   public void limpiarDuplicadoComprobante() {
      duplicarComprobante = new Comprobantes();
      secRegistro = null;
      tipoTabla = -1;
   }

   public void confirmarDuplicarCortesProcesos() {
      int pasa = 0;
      mensajeValidacion = "";
      if (duplicarCorteProceso.getCorte() == null) {
         mensajeValidacion = " * Fecha Corte \n";
         pasa++;
      }
      if (duplicarCorteProceso.getProceso().getSecuencia() == null) {
         mensajeValidacion = " * Proceso \n";
         pasa++;
      }
      if (pasa == 0) {
         k++;
         l = BigInteger.valueOf(k);
         duplicarCorteProceso.setSecuencia(l);
         listaCortesProcesos.add(duplicarCorteProceso);
         listaCortesProcesosCrear.add(duplicarCorteProceso);
         cortesProcesosSeleccionado = listaCortesProcesos.get(listaCortesProcesos.indexOf(duplicarCorteProceso));
         RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
         RequestContext.getCurrentInstance().execute("PF('DuplicarCortesProcesos').hide()");
         tipoTabla = -1;
         modificacionesCortesProcesos = true;
         secRegistro = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         if (banderaCortesProcesos == 1) {
            restaurarTablaCortesProcesos();
         }
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoCorteProceso");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevoCorteProceso').show()");
      }
   }

   //LIMPIAR DUPLICADO REGISTRO CORTES PROCESOS
   public void limpiarDuplicadoCortesProcesos() {
      duplicarCorteProceso = new CortesProcesos();
      tipoTabla = -1;
      secRegistro = null;
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (banderaComprobantes == 0) {
         numeroComprobanteC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:numeroComprobanteC");
         numeroComprobanteC.setFilterStyle("width: 85% !important;");
         fechaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaC");
         fechaC.setFilterStyle("width: 85% !important;");
         fechaEntregaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaEntregaC");
         fechaEntregaC.setFilterStyle("width: 85% !important;");
         altoScrollComprobante = "52";
         RequestContext.getCurrentInstance().update("form:datosComprobantes");
         banderaComprobantes = 1;
      } else if (banderaComprobantes == 1) {
         restaurarTablaComprobantes();
      }
      if (banderaCortesProcesos == 0) {
         fechaCorteCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:fechaCorteCP");
         fechaCorteCP.setFilterStyle("width: 85% !important;");
         procesoCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:procesoCP");
         procesoCP.setFilterStyle("width: 85% !important;");
         altoScrollComprobante = "52";

         c_codigo = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:codigoSNE2");
         c_codigo.setFilterStyle("width: 85% !important;");
         c_descripcion = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:descripcionSNE2");
         c_descripcion.setFilterStyle("width: 85% !important;");
         c_fDesde = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:fechaDesdeSNE");
         c_fDesde.setFilterStyle("width: 85% !important;");
         c_fHasta = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:fechaHastaSNE");
         c_fHasta.setFilterStyle("width: 85% !important;");
         c_unidad = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:unidadSNE");
         c_unidad.setFilterStyle("width: 85% !important;");
         c_pago = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:pasivoSNE");
         c_pago.setFilterStyle("width: 85% !important;");
         c_descuento = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:gastoSNE");
         c_descuento.setFilterStyle("width: 85% !important;");
         c_tercero = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:terceroSNE");
         c_tercero.setFilterStyle("width: 85% !important;");
         c_cuentaD = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:debitoSNE");
         c_cuentaD.setFilterStyle("width: 85% !important;");
         c_cuentaC = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:creditoSNE");
         c_cuentaC.setFilterStyle("width: 85% !important;");
         c_CCDebito = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:centroCostoDSNE");
         c_CCDebito.setFilterStyle("width: 85% !important;");
         c_CCCredito = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:centroCostoCSNE");
         c_CCCredito.setFilterStyle("width: 85% !important;");
         c_saldo = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:saldoSNE");
         c_saldo.setFilterStyle("width: 85% !important;");
         c_fechaPago = (Column) c.getViewRoot().findComponent("form:tablaEmpleado:pagoSNE");
         c_fechaPago.setFilterStyle("width: 85% !important;");

         cr_codigo = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:codigoSNEM");
         cr_codigo.setFilterStyle("width: 85% !important;");
         cr_descripcion = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:descripcionSNEM");
         cr_descripcion.setFilterStyle("width: 85% !important;");
         cr_fDesde = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:fechaDesdeSNE");
         cr_fDesde.setFilterStyle("width: 85% !important;");
         cr_fHasta = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:fechaHastaSNEM");
         cr_fHasta.setFilterStyle("width: 85% !important;");
         cr_unidad = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:unidadSNEM");
         cr_unidad.setFilterStyle("width: 85% !important;");
         cr_pasivo = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:pasivoSNEM");
         cr_pasivo.setFilterStyle("width: 85% !important;");
         cr_gasto = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:gastoSNEM");
         cr_gasto.setFilterStyle("width: 85% !important;");
         cr_tercero = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:terceroSNEM");
         cr_tercero.setFilterStyle("width: 85% !important;");
         cr_cuentaD = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:debitoSNEM");
         cr_cuentaD.setFilterStyle("width: 85% !important;");
         cr_cuentaC = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:creditoSNEM");
         cr_cuentaC.setFilterStyle("width: 85% !important;");
         cr_CCDebito = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:centroCostoDSNEM");
         cr_CCDebito.setFilterStyle("width: 85% !important;");
         cr_CCCredito = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:centroCostoCSNEM");
         cr_CCCredito.setFilterStyle("width: 85% !important;");
         cr_saldo = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:saldoSNEM");
         cr_saldo.setFilterStyle("width: 85% !important;");
         cr_fechaPago = (Column) c.getViewRoot().findComponent("form:tablaEmpleador:pagoSNEM");
         cr_fechaPago.setFilterStyle("width: 85% !important;");
         banderaCortesProcesos = 1;

         RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
         RequestContext.getCurrentInstance().update("form:tablaEmpleador");
         RequestContext.getCurrentInstance().update("form:tablaEmpleado");

      } else if (banderaCortesProcesos == 1) {
         restaurarTablaCortesProcesos();
         restaurarTablaEmpleado();
         restaurarTablaEmpleador();
      }
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (tipoTabla == 0) {
         if (comprobanteSeleccionado != null) {
            editarComprobante = comprobanteSeleccionado;
            if (cualCelda == 0) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarNumeroComprobante");
               RequestContext.getCurrentInstance().execute("PF('editarNumeroComprobante').show()");
            } else if (cualCelda == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaComprobante");
               RequestContext.getCurrentInstance().execute("PF('editarFechaComprobante').show()");
            } else if (cualCelda == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaEntrega");
               RequestContext.getCurrentInstance().execute("PF('editarFechaEntrega').show()");
            }
         }
      } else if (tipoTabla == 1) {
         editarCorteProceso = cortesProcesosSeleccionado;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaCorte");
            RequestContext.getCurrentInstance().execute("PF('editarFechaCorte').show()");
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarProceso");
            RequestContext.getCurrentInstance().execute("PF('editarProceso').show()");
         }
      }
      secRegistro = null;
   }

   //LISTA DE VALORES BOTON
   public void listaValoresBoton() {
      if (tipoTabla == 1) {
         if (cortesProcesosSeleccionado != null) {
            if (cualCelda == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:ProcesosDialogo");
               RequestContext.getCurrentInstance().execute("PF('ProcesosDialogo').show()");
               tipoActualizacion = 0;
            }
         }
      }
   }
   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

   public void verificarRastro() {
      int resultado = -1;
      if (nombreTabla != null) {
         if (nombreTabla.equals("Comprobantes")) {
            resultado = administrarRastros.obtenerTabla(secRegistro, nombreTabla.toUpperCase());
         } else if (nombreTabla.equals("CortesProcesos")) {
            resultado = administrarRastros.obtenerTabla(secRegistro, nombreTabla.toUpperCase());
         }
         if (resultado == 1) {
            RequestContext.getCurrentInstance().update("formDialogos:errorObjetosDB");
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            RequestContext.getCurrentInstance().update("formDialogos:confirmarRastro");
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (resultado == 3) {
            RequestContext.getCurrentInstance().update("formDialogos:errorRegistroRastro");
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (resultado == 4) {
            RequestContext.getCurrentInstance().update("formDialogos:errorTablaConRastro");
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (resultado == 5) {
            RequestContext.getCurrentInstance().update("formDialogos:errorTablaSinRastro");
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }
   //EXPORTAR

   public void exportPDF() throws IOException {
      DataTable tabla;
      Exporter exporter = new ExportarPDF();
      if (tipoTabla == 0) {
         FacesContext context = FacesContext.getCurrentInstance();
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosComprobantesExportar");
         exporter.export(context, tabla, "ComprobantesPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (tipoTabla == 1) {
         FacesContext context = FacesContext.getCurrentInstance();
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCortesProcesosExportar");
         exporter.export(context, tabla, "CortesProcesosPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (tipoTabla == 2) {
         FacesContext context = FacesContext.getCurrentInstance();
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:solucionesNodoEmpleadoExportar");
         exporter.export(context, tabla, "SolucionesNodosEmpleadoPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (tipoTabla == 3) {
         FacesContext context = FacesContext.getCurrentInstance();
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:solucionesNodoEmpleadorExportar");
         exporter.export(context, tabla, "SolucionesNodosEmpleadorPDF", false, false, "UTF-8", null, null);
         context.responseComplete();
      }
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla;
      Exporter exporter = new ExportarXLS();
      if (tipoTabla == 0) {
         FacesContext context = FacesContext.getCurrentInstance();
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosComprobantesExportar");
         exporter.export(context, tabla, "ComprobantesXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (tipoTabla == 1) {
         FacesContext context = FacesContext.getCurrentInstance();
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCortesProcesosExportar");
         exporter.export(context, tabla, "CortesProcesosXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (tipoTabla == 2) {
         FacesContext context = FacesContext.getCurrentInstance();
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:solucionesNodoEmpleadoExportar");
         exporter.export(context, tabla, "SolucionesNodosEmpleadoXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      } else if (tipoTabla == 3) {
         FacesContext context = FacesContext.getCurrentInstance();
         tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:solucionesNodoEmpleadorExportar");
         exporter.export(context, tabla, "SolucionesNodosEmpleadorXLS", false, false, "UTF-8", null, null);
         context.responseComplete();
      }
   }

   //SALIR Y REFRESCAR
   public void salir() {
      if (banderaCortesProcesos == 1) {
         restaurarTablaCortesProcesos();
      }
      if (banderaComprobantes == 1) {
         restaurarTablaComprobantes();
      }
      listaComprobantesBorrar.clear();
      listaCortesProcesosBorrar.clear();
      listaComprobantesCrear.clear();
      listaCortesProcesosCrear.clear();
      listaComprobantesModificar.clear();
      listaCortesProcesosModificar.clear();
      listaSolucionesNodosEmpleadoModificar.clear();
      listaSolucionesNodosEmpleadorModificar.clear();
      modificacionesComprobantes = false;
      modificacionesCortesProcesos = false;
      modificacionesSolucionesNodosEmpleado = false;
      modificacionesSolucionesNodosEmpleador = false;
      tipoTabla = 0;
      cualCelda = -1;
      tablaExportar = ":formExportar:datosComprobantesExportar";
      nombreTabla = "Comprobantes";
      comprobanteSeleccionado = null;
      cortesProcesosSeleccionado = null;
      empleadoTablaSeleccionado = null;
      empleadorTablaSeleccionado = null;
      secRegistro = null;
      k = 0;
      listaComprobantes = null;
      listaCortesProcesos = null;
      listaSolucionesNodosEmpleado = null;
      listaSolucionesNodosEmpleador = null;
      guardado = true;
      permitirIndex = true;
      recibirEmpleado(secuenciaEmpleado);
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosComprobantes");
      RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
      RequestContext.getCurrentInstance().update("form:tablaEmpleado");
      RequestContext.getCurrentInstance().update("form:tablaEmpleador");
   }

   public void guardarCambiosComprobantes() {
      try {
         if (!listaComprobantesBorrar.isEmpty()) {
            for (int i = 0; i < listaComprobantesBorrar.size(); i++) {
               administrarComprobantes.borrarComprobantes(listaComprobantesBorrar.get(i));
            }
            listaComprobantesBorrar.clear();
         }
         if (!listaComprobantesCrear.isEmpty()) {
            for (int i = 0; i < listaComprobantesCrear.size(); i++) {
               administrarComprobantes.crearComprobante(listaComprobantesCrear.get(i));
            }
            listaComprobantesCrear.clear();
         }
         if (!listaComprobantesModificar.isEmpty()) {
            administrarComprobantes.modificarComprobantes(listaComprobantesModificar);
            listaComprobantesModificar.clear();
         }
         listaComprobantes = null;
         RequestContext.getCurrentInstance().update("form:datosComprobantes");
         guardado = true;
         modificacionesComprobantes = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Comprobante se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosComprobantes Controlador: " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Comprobante, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void eliminarComprobantegeneral() {
      System.out.println("Entro en eliminarComprobantegeneral()");
      System.out.println("cortesProcesosSeleccionado : " + cortesProcesosSeleccionado);
      if (cortesProcesosSeleccionado != null) {
         boolean pasa = administrarComprobantes.eliminarCPconUndoCierre(cortesProcesosSeleccionado.getProceso().getSecuencia(), secuenciaEmpleado, cortesProcesosSeleccionado.getCorte());
         if (pasa) {
            listaComprobantes.clear();
            listaComprobantes = administrarComprobantes.consultarComprobantesEmpleado(empleado.getSecuencia());
            if (listaComprobantes != null) {
               if (!listaComprobantes.isEmpty()) {
                  comprobanteSeleccionado = listaComprobantes.get(0);
                  cargarListasConComprobante();
               }
            }
            guardado = true;
            modificacionesComprobantes = false;
            RequestContext.getCurrentInstance().update("form:datosComprobantes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "El Comprobante se elimino con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistrosComp();
         } else {
//            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error al eliminar el Comprobante.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().execute("PF('errorEliminando').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void guardarCambiosCorteProceso() {
      try {
         if (!listaCortesProcesosBorrar.isEmpty()) {
            for (int i = 0; i < listaCortesProcesosBorrar.size(); i++) {
               if (listaCortesProcesosBorrar.get(i).getProceso().getSecuencia() == null) {
                  listaCortesProcesosBorrar.get(i).setProceso(null);
                  administrarComprobantes.borrarCortesProcesos(listaCortesProcesosBorrar.get(i));
               } else {
                  administrarComprobantes.borrarCortesProcesos(listaCortesProcesosBorrar.get(i));
               }
            }
            listaCortesProcesosBorrar.clear();
         }
         if (!listaCortesProcesosCrear.isEmpty()) {
            for (int i = 0; i < listaCortesProcesosCrear.size(); i++) {
               if (listaCortesProcesosCrear.get(i).getProceso().getSecuencia() == null) {
                  listaCortesProcesosCrear.get(i).setProceso(null);
                  administrarComprobantes.crearCorteProceso(listaCortesProcesosCrear.get(i));
               } else {
                  administrarComprobantes.crearCorteProceso(listaCortesProcesosCrear.get(i));
               }
            }
            listaCortesProcesosCrear.clear();
         }
         if (!listaCortesProcesosModificar.isEmpty()) {
            administrarComprobantes.modificarCortesProcesos(listaCortesProcesosModificar);
            listaCortesProcesosModificar.clear();
         }
         listaCortesProcesos.clear();
         RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
         guardado = true;
         modificacionesCortesProcesos = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Corte Proceso se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosCorteProceso Controlador: " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Corte Proceso, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosEmpleado() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (!listaSolucionesNodosEmpleadoModificar.isEmpty()) {
            administrarComprobantes.modificarSolucionesNodosEmpleado(listaSolucionesNodosEmpleadoModificar);
            listaSolucionesNodosEmpleadoModificar.clear();
         }
         listaSolucionesNodosEmpleado = null;
         RequestContext.getCurrentInstance().update("form:tablaEmpleado");
         guardado = true;
         modificacionesSolucionesNodosEmpleado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Los datos de Empleado se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosEmpleado Controlador: " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Empleado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosEmpleador() {
      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (!listaSolucionesNodosEmpleadorModificar.isEmpty()) {
            administrarComprobantes.modificarSolucionesNodosEmpleado(listaSolucionesNodosEmpleadorModificar);
            listaSolucionesNodosEmpleadorModificar.clear();
         }
         listaSolucionesNodosEmpleador = null;
         RequestContext.getCurrentInstance().update("form:tablaEmpleador");
         guardado = true;
         modificacionesSolucionesNodosEmpleador = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Los datos de Empleador se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosEmpleador Controlador: " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Empleador, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   //GUARDAR
   public void guardarCambios() {
      System.out.println("guardarCambios() guardado : " + guardado);
      System.out.println("guardarCambios() modificacionesComprobantes : " + modificacionesComprobantes);
      System.out.println("guardarCambios() modificacionesCortesProcesos : " + modificacionesCortesProcesos);
      System.out.println("guardarCambios() modificacionesSolucionesNodosEmpleado : " + modificacionesSolucionesNodosEmpleado);
      System.out.println("guardarCambios() modificacionesSolucionesNodosEmpleador : " + modificacionesComprobantes);
      if (guardado == false) {
         if (modificacionesComprobantes == true) {
            guardarCambiosComprobantes();
         }
         if (modificacionesCortesProcesos == true) {
            guardarCambiosCorteProceso();
         }
         if (modificacionesSolucionesNodosEmpleado == true) {
            guardarCambiosEmpleado();
         }
         if (modificacionesSolucionesNodosEmpleador == true) {
            guardarCambiosEmpleador();
         }
         recibirEmpleado(secuenciaEmpleado);
         tablaExportar = ":formExportar:datosComprobantesExportar";
         RequestContext.getCurrentInstance().update("form:datosComprobantes");
         RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
         RequestContext.getCurrentInstance().update("form:tablaEmpleado");
         RequestContext.getCurrentInstance().update("form:tablaEmpleador");
         RequestContext.getCurrentInstance().update("form:exportarXML");
         secRegistro = null;
      }
   }
//
//    public void seleccionTablaEmpleadoIz() {
//        empleadoTablaSeleccionado = listaSolucionesNodosEmpleado.;
//        FacesContext c = FacesContext.getCurrentInstance();
//        RequestContext.getCurrentInstance().execute("PF('$('.tabla2ScrollInteligente .ui-datatable-scrollable-body').animate({scrollTop : 0}, 500);");
//        tabla1 = (DataTable) c.getViewRoot().findComponent("form:tablaEmpleado");
//        tabla1.setSelection(empleadoTablaSeleccionado);
//        RequestContext.getCurrentInstance().update("form:tablaEmpleado");
//        RequestContext.getCurrentInstance().execute("PF('if ($('.tabla2ScrollInteligente .ui-datatable .ui-state-highlight').offset().top-(275.25) < 0.5) {"
//                + "$('.tabla2ScrollInteligente .ui-datatable-scrollable-body').animate({scrollTop : ($('.tabla2ScrollInteligente .ui-datatable .ui-state-highlight').offset().top-(275))+60}, 1000);} "
//                + "else if ($('.tabla2ScrollInteligente .ui-datatable .ui-state-highlight').offset().top-(276.25) > 70) {"
//                + "$('.tabla2ScrollInteligente .ui-datatable-scrollable-body').animate({scrollTop : ($('.tabla2ScrollInteligente .ui-datatable .ui-state-highlight').offset().top-22)}, 1000);}");
//    }
//
//    public void seleccionTablaEmpleadoDer() {
//        FacesContext c = FacesContext.getCurrentInstance();
//        tabla2 = (DataTable) c.getViewRoot().findComponent("form:tablaEmpleado");
//        tabla2.setSelection(empleadoTablaSeleccionado);
//        RequestContext.getCurrentInstance().update("form:tablaEmpleado");
//        RequestContext.getCurrentInstance().execute("PF('if ($('.tablaScrollInteligente .ui-datatable .ui-state-highlight').offset().top-(275.25) < 0.5) {"
//                + "$('.tablaScrollInteligente .ui-datatable-scrollable-body').animate({scrollTop : ($('.tablaScrollInteligente .ui-datatable .ui-state-highlight').offset().top-(275))+60}, 1000);} "
//                + "else if ($('.tablaScrollInteligente .ui-datatable .ui-state-highlight').offset().top-(276.25) > 70) {"
//                + "$('.tablaScrollInteligente .ui-datatable-scrollable-body').animate({scrollTop : ($('.tablaScrollInteligente .ui-datatable .ui-state-highlight').offset().top-22)}, 1000);}");
//    }
//
//    public void organizarTablasEmpleado() {
//        RequestContext.getCurrentInstance().update("form:tablaEmpleado");
//    }
//
//    public void organizarTablasEmpleador() {
//        RequestContext.getCurrentInstance().update("form:tablaEmpleador");
//    }

   //EVENTO FILTRAR
   public void eventoFiltrarComponentes() {
      if (tipoListaComprobantes == 0) {
         tipoListaComprobantes = 1;
      }
      comprobanteSeleccionado = null;
      limpiarListasdeComprobante();
      contarRegistrosComp();
   }

   public void eventoFiltrarCortesProcesos() {
      if (tipoListaCortesProcesos == 0) {
         tipoListaCortesProcesos = 1;
      }
      cortesProcesosSeleccionado = null;
      limpiarListasdeCortesProcesos();
      contarRegistrosCP();
   }

   public void eventoFiltrarSNEmpleado() {
      if (tipoListaSNEmpleado == 0) {
         tipoListaSNEmpleado = 1;
      }
      empleadoTablaSeleccionado = null;
      contarRegistrosTE();
   }

   public void eventoFiltrarSNEmpleador() {
      if (tipoListaSNEmpleador == 0) {
         tipoListaSNEmpleador = 1;
      }
      empleadorTablaSeleccionado = null;
      contarRegistrosTEr();
   }

   //PARCIALES CONCEPTO
   public void parcialSolucionNodo(int indice, int tabla) {
      index = indice;
      tablaActual = tabla;
      if (tabla == 0) {
         empleadoTablaSeleccionado = listaSolucionesNodosEmpleado.get(index);
         parcialesSolucionNodos = empleadoTablaSeleccionado.getParciales();
      } else if (tabla == 1) {
         empleadorTablaSeleccionado = listaSolucionesNodosEmpleador.get(index);
         parcialesSolucionNodos = empleadorTablaSeleccionado.getParciales();
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:parcialesConcepto");
      RequestContext.getCurrentInstance().execute("PF('parcialesConcepto').show();");
   }

   public void verDetallesFormula() {
      listaDetallesFormulas = null;
      RequestContext.getCurrentInstance().update("formularioDialogos:detallesFormulas");
      RequestContext.getCurrentInstance().execute("PF('detallesFormulas').show();");
   }

   public void contarRegistrosComp() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroCompr");
   }

   public void contarRegistrosCP() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroProc");
   }

   public void contarRegistrosTE() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroE");
   }

   public void contarRegistrosTEr() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroEr");
   }

   //Conteo Listas de Valor :
   public void contarRegistrosLovProc() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroProceso");
   }

   public void contarRegistrosLovTercero() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTercero");
   }

   public void contarRegistrosLovCuentasD() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCuentaDebito");
   }

   public void contarRegistrosLovCuentasC() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCuentaCredito");
   }

   public void contarRegistrosLovCCC() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCentroCostoCredito");
   }

   public void contarRegistrosLovCCD() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCentroCostoDebito");
   }

//    public void contarRegistrosLovCredito(int tipoListaLov) {
//        if (tipoListaLov == 1) {
//            infoRegistroCuentaCredito = String.valueOf(filtrarLovCuentas.size());
//        } else if (lovCuentas != null) {
//            infoRegistroCuentaCredito = String.valueOf(lovCuentas.size());
//        } else {
//            infoRegistroCuentaCredito = String.valueOf(0);
//        }
//        RequestContext.getCurrentInstance().update("form:informacionRegistroEr");
//    }
//
//    public void contarRegistrosLovCCCredito(int tipoListaLov) {
//        if (tipoListaLov == 1) {
//            infoRegistroCentroCostoCredito = String.valueOf(filtrarLovCentrosCostos.size());
//        } else if (lovCentrosCostos != null) {
//            infoRegistroCentroCostoCredito = String.valueOf(lovCentrosCostos.size());
//        } else {
//            infoRegistroCentroCostoCredito = String.valueOf(0);
//        }
//        RequestContext.getCurrentInstance().update("form:informacionRegistroEr");
//    }
//GETTER AND SETTER
   public Empleados getEmpleado() {
      return empleado;
   }

   public List<Comprobantes> getListaComprobantes() {
      return listaComprobantes;
   }

   public List<CortesProcesos> getListaCortesProcesos() {
      return listaCortesProcesos;
   }

   public List<SolucionesNodos> getListaSolucionesNodosEmpleado() {
      return listaSolucionesNodosEmpleado;
   }

   public List<SolucionesNodos> getListaSolucionesNodosEmpleador() {
      return listaSolucionesNodosEmpleador;
   }

   public void setListaComprobantes(List<Comprobantes> listaComprobantes) {
      this.listaComprobantes = listaComprobantes;
   }

   public List<Comprobantes> getFiltradolistaComprobantes() {
      return filtradolistaComprobantes;
   }

   public void setFiltradolistaComprobantes(List<Comprobantes> filtradolistaComprobantes) {
      this.filtradolistaComprobantes = filtradolistaComprobantes;
   }

   public void setListaCortesProcesos(List<CortesProcesos> listaCortesProcesos) {
      this.listaCortesProcesos = listaCortesProcesos;
   }

   public List<CortesProcesos> getFiltradolistaCortesProcesos() {
      return filtradolistaCortesProcesos;
   }

   public void setFiltradolistaCortesProcesos(List<CortesProcesos> filtradolistaCortesProcesos) {
      this.filtradolistaCortesProcesos = filtradolistaCortesProcesos;
   }

   public void setListaSolucionesNodosEmpleado(List<SolucionesNodos> listaSolucionesNodosEmpleado) {
      this.listaSolucionesNodosEmpleado = listaSolucionesNodosEmpleado;
   }

   public List<SolucionesNodos> getFiltradolistaSolucionesNodosEmpleado() {
      return filtradolistaSolucionesNodosEmpleado;
   }

   public void setFiltradolistaSolucionesNodosEmpleado(List<SolucionesNodos> filtradolistaSolucionesNodosEmpleado) {
      this.filtradolistaSolucionesNodosEmpleado = filtradolistaSolucionesNodosEmpleado;
   }

   public void setListaSolucionesNodosEmpleador(List<SolucionesNodos> listaSolucionesNodosEmpleador) {
      this.listaSolucionesNodosEmpleador = listaSolucionesNodosEmpleador;
   }

   public List<SolucionesNodos> getFiltradolistaSolucionesNodosEmpleador() {
      return filtradolistaSolucionesNodosEmpleador;
   }

   public void setFiltradolistaSolucionesNodosEmpleador(List<SolucionesNodos> filtradolistaSolucionesNodosEmpleador) {
      this.filtradolistaSolucionesNodosEmpleador = filtradolistaSolucionesNodosEmpleador;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public Comprobantes getNuevoComprobante() {
      return nuevoComprobante;
   }

   public void setNuevoComprobante(Comprobantes nuevoComprobante) {
      this.nuevoComprobante = nuevoComprobante;
   }

   public CortesProcesos getNuevoCorteProceso() {
      return nuevoCorteProceso;
   }

   public void setNuevoCorteProceso(CortesProcesos nuevoCorteProceso) {
      this.nuevoCorteProceso = nuevoCorteProceso;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public BigInteger getSecRegistro() {
      return secRegistro;
   }

   public Comprobantes getDuplicarComprobante() {
      return duplicarComprobante;
   }

   public void setDuplicarComprobante(Comprobantes duplicarComprobante) {
      this.duplicarComprobante = duplicarComprobante;
   }

   public String getTablaExportar() {
      return tablaExportar;
   }

   public void setTablaExportar(String tablaExportar) {
      this.tablaExportar = tablaExportar;
   }

   public String getNombreArchivoExportar() {
      return nombreArchivoExportar;
   }

   public void setNombreArchivoExportar(String nombreArchivoExportar) {
      this.nombreArchivoExportar = nombreArchivoExportar;
   }

   public String getAltoScrollComprobante() {
      return altoScrollComprobante;
   }

   public void setAltoScrollComprobante(String altoScrollComprobante) {
      this.altoScrollComprobante = altoScrollComprobante;
   }

   public Comprobantes getEditarComprobante() {
      return editarComprobante;
   }

   public void setEditarComprobante(Comprobantes editarComprobante) {
      this.editarComprobante = editarComprobante;
   }

   public String getNombreTabla() {
      return nombreTabla;
   }

   public List<Procesos> getLovProcesos() {
      lovProcesos = administrarComprobantes.consultarLOVProcesos();
      return lovProcesos;
   }

   public void setLovProcesos(List<Procesos> listaProcesos) {
      this.lovProcesos = listaProcesos;
   }

   public Procesos getProcesoSelecionado() {
      return procesoSelecionado;
   }

   public void setProcesoSelecionado(Procesos procesoSelecionado) {
      this.procesoSelecionado = procesoSelecionado;
   }

   public List<Procesos> getFiltradoProcesos() {
      return filtradoProcesos;
   }

   public void setFiltradoProcesos(List<Procesos> filtradoProcesos) {
      this.filtradoProcesos = filtradoProcesos;
   }

   public CortesProcesos getEditarCorteProceso() {
      return editarCorteProceso;
   }

   public void setEditarCorteProceso(CortesProcesos editarCorteProceso) {
      this.editarCorteProceso = editarCorteProceso;
   }

   public CortesProcesos getDuplicarCorteProceso() {
      return duplicarCorteProceso;
   }

   public void setDuplicarCorteProceso(CortesProcesos duplicarCorteProceso) {
      this.duplicarCorteProceso = duplicarCorteProceso;
   }

   public Comprobantes getComprobanteSeleccionado() {
      return comprobanteSeleccionado;
   }

   public void setComprobanteSeleccionado(Comprobantes comprobanteSeleccionado) {
      this.comprobanteSeleccionado = comprobanteSeleccionado;
   }

   public List<Terceros> getLovTerceros() {
      if (empleado.getEmpresa() != null && lovTerceros.isEmpty()) {
         if (empleado.getEmpresa().getSecuencia() != null) {
            lovTerceros = administrarComprobantes.consultarLOVTerceros(empleado.getEmpresa().getSecuencia());
         }
      }
      return lovTerceros;
   }

   public void setLovTerceros(List<Terceros> listaTerceros) {
      this.lovTerceros = listaTerceros;
   }

   public Terceros getTerceroSelecionado() {
      return TerceroSelecionado;
   }

   public void setTerceroSelecionado(Terceros TerceroSelecionado) {
      this.TerceroSelecionado = TerceroSelecionado;
   }

   public List<Terceros> getFiltradolistaTerceros() {
      return filtradolistaTerceros;
   }

   public void setFiltradolistaTerceros(List<Terceros> filtradolistaTerceros) {
      this.filtradolistaTerceros = filtradolistaTerceros;
   }

   public BigDecimal getSubtotalPago() {
      return subtotalPago;
   }

   public BigDecimal getSubtotalDescuento() {
      return subtotalDescuento;
   }

   public BigDecimal getSubtotalPasivo() {
      return subtotalPasivo;
   }

   public BigDecimal getSubtotalGasto() {
      return subtotalGasto;
   }

   public BigDecimal getNeto() {
      return neto;
   }

   public List<DetallesFormulas> getListaDetallesFormulas() {
      if (listaDetallesFormulas == null) {
         BigInteger secEmpleado = null, secProceso = null, secHistoriaFormula, secFormula = null;
         String fechaDesde = null, fechaHasta = null;
         if (tablaActual == 0) {
            if (listaSolucionesNodosEmpleado != null && !listaSolucionesNodosEmpleado.isEmpty()) {
               secFormula = listaSolucionesNodosEmpleado.get(index).getFormula();
               fechaDesde = formatoFecha.format(listaSolucionesNodosEmpleado.get(index).getFechadesde());
               fechaHasta = formatoFecha.format(listaSolucionesNodosEmpleado.get(index).getFechahasta());
               secEmpleado = listaSolucionesNodosEmpleado.get(index).getEmpleado();
               secProceso = listaSolucionesNodosEmpleado.get(index).getProceso();
            }
         } else if (tablaActual == 1) {
            if (listaSolucionesNodosEmpleador != null && !listaSolucionesNodosEmpleador.isEmpty()) {
               secFormula = listaSolucionesNodosEmpleador.get(index).getFormula();
               fechaDesde = formatoFecha.format(listaSolucionesNodosEmpleador.get(index).getFechadesde());
               fechaHasta = formatoFecha.format(listaSolucionesNodosEmpleador.get(index).getFechahasta());
               secEmpleado = listaSolucionesNodosEmpleador.get(index).getEmpleado();
               secProceso = listaSolucionesNodosEmpleador.get(index).getProceso();
            }
         }
         if (secFormula != null && fechaDesde != null) {
            secHistoriaFormula = administrarComprobantes.consultarHistoriaFormula(secFormula, fechaDesde);
            listaDetallesFormulas = administrarComprobantes.consultarDetallesFormula(secEmpleado, fechaDesde, fechaHasta, secProceso, secHistoriaFormula);
         }
      }
      return listaDetallesFormulas;
   }

   public void setListaDetallesFormulas(List<DetallesFormulas> listaDetallesFormulas) {
      this.listaDetallesFormulas = listaDetallesFormulas;
   }

   public String getParcialesSolucionNodos() {
      return parcialesSolucionNodos;
   }

   public CortesProcesos getCortesProcesosSeleccionado() {
      return cortesProcesosSeleccionado;
   }

   public void setCortesProcesosSeleccionado(CortesProcesos cortesProcesosSeleccionado) {
      this.cortesProcesosSeleccionado = cortesProcesosSeleccionado;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public List<Cuentas> getLovCuentas() {
      lovCuentas = administrarComprobantes.lovCuentas();
      return lovCuentas;
   }

   public void setLovCuentas(List<Cuentas> lovCuentas) {
      this.lovCuentas = lovCuentas;
   }

   public List<Cuentas> getFiltrarLovCuentas() {
      return filtrarLovCuentas;
   }

   public void setFiltrarLovCuentas(List<Cuentas> filtrarLovCuentas) {
      this.filtrarLovCuentas = filtrarLovCuentas;
   }

   public Cuentas getCuentaSeleccionada() {
      return cuentaSeleccionada;
   }

   public void setCuentaSeleccionada(Cuentas cuentaSeleccionada) {
      this.cuentaSeleccionada = cuentaSeleccionada;
   }

   public List<CentrosCostos> getLovCentrosCostos() {
      lovCentrosCostos = administrarComprobantes.lovCentrosCostos();
      return lovCentrosCostos;
   }

   public void setLovCentrosCostos(List<CentrosCostos> lovCentrosCostos) {
      this.lovCentrosCostos = lovCentrosCostos;
   }

   public List<CentrosCostos> getFiltrarLovCentrosCostos() {
      return filtrarLovCentrosCostos;
   }

   public void setFiltrarLovCentrosCostos(List<CentrosCostos> filtrarLovCentrosCostos) {
      this.filtrarLovCentrosCostos = filtrarLovCentrosCostos;
   }

   public CentrosCostos getCentroCostoSeleccionado() {
      return centroCostoSeleccionado;
   }

   public void setCentroCostoSeleccionado(CentrosCostos centroCostoSeleccionado) {
      this.centroCostoSeleccionado = centroCostoSeleccionado;
   }

   public void setInfoRegistroProceso(String infoRegistroProceso) {
      this.infoRegistroProceso = infoRegistroProceso;
   }

   public void setInfoRegistroTercero(String infoRegistroTercero) {
      this.infoRegistroTercero = infoRegistroTercero;
   }

   public SolucionesNodos getEmpleadoTablaSeleccionado() {
      return empleadoTablaSeleccionado;
   }

   public void setEmpleadoTablaSeleccionado(SolucionesNodos empleadoTablaSeleccionado) {
      this.empleadoTablaSeleccionado = empleadoTablaSeleccionado;
   }

   public SolucionesNodos getEmpleadorTablaSeleccionado() {
      return empleadorTablaSeleccionado;
   }

   public void setEmpleadorTablaSeleccionado(SolucionesNodos empleadorTablaSeleccionado) {
      this.empleadorTablaSeleccionado = empleadorTablaSeleccionado;
   }

   public void setInfoRegistroComp(String infoRegistroComp) {
      this.infoRegistroComp = infoRegistroComp;
   }

   public void setInfoRegistroCP(String infoRegistroCP) {
      this.infoRegistroCP = infoRegistroCP;
   }

   public void setInfoRegistroTEmpleado(String infoRegistroTEmpleado) {
      this.infoRegistroTEmpleado = infoRegistroTEmpleado;
   }

   public void setInfoRegistroTEmpleador(String infoRegistroTEmpleador) {
      this.infoRegistroTEmpleador = infoRegistroTEmpleador;
   }

   public void setInfoRegistroCuentaD(String infoRegistroCuentaD) {
      this.infoRegistroCuentaD = infoRegistroCuentaD;
   }

   public void setInfoRegistroCuentaC(String infoRegistroCuentaC) {
      this.infoRegistroCuentaC = infoRegistroCuentaC;
   }

   public void setInfoRegistroCentroCostoC(String infoRegistroCentroCostoC) {
      this.infoRegistroCentroCostoC = infoRegistroCentroCostoC;
   }

   public void setInfoRegistroCentroCostoD(String infoRegistroCentroCostoD) {
      this.infoRegistroCentroCostoD = infoRegistroCentroCostoD;
   }

   public String getInfoRegistroComp() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosComprobantes");
      infoRegistroComp = String.valueOf(tabla.getRowCount());
      return infoRegistroComp;
   }

   public String getInfoRegistroCP() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCortesProcesos");
      infoRegistroCP = String.valueOf(tabla.getRowCount());
      return infoRegistroCP;
   }

   public String getInfoRegistroTEmpleado() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:tablaEmpleado");
      infoRegistroTEmpleado = String.valueOf(tabla.getRowCount());
      return infoRegistroTEmpleado;
   }

   public String getInfoRegistroTEmpleador() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:tablaEmpleador");
      infoRegistroTEmpleador = String.valueOf(tabla.getRowCount());
      return infoRegistroTEmpleador;
   }

   public String getInfoRegistroProceso() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovProcesos");
      infoRegistroProceso = String.valueOf(tabla.getRowCount());
      return infoRegistroProceso;
   }

   public String getInfoRegistroTercero() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTerceros");
      infoRegistroTercero = String.valueOf(tabla.getRowCount());
      return infoRegistroTercero;
   }

   public String getInfoRegistroCuentaD() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCuentaDebito");
      infoRegistroCuentaD = String.valueOf(tabla.getRowCount());
      return infoRegistroCuentaD;
   }

   public String getInfoRegistroCuentaC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCuentaCredito");
      infoRegistroCuentaC = String.valueOf(tabla.getRowCount());
      return infoRegistroCuentaC;
   }

   public String getInfoRegistroCentroCostoC() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCentroCostoCredito");
      infoRegistroCentroCostoC = String.valueOf(tabla.getRowCount());
      return infoRegistroCentroCostoC;
   }

   public String getInfoRegistroCentroCostoD() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovCentroCostoDebito");
      infoRegistroCentroCostoD = String.valueOf(tabla.getRowCount());
      return infoRegistroCentroCostoD;
   }

   public String getAltoScrollSolucionesNodos() {
      if (banderaCortesProcesos == 1) {
         this.altoScrollSolucionesNodos = "58";
      } else {
         this.altoScrollSolucionesNodos = "75";
      }
      return altoScrollSolucionesNodos;
   }

   public void setAltoScrollSolucionesNodos(String altoScrollSolucionesNodos) {
      this.altoScrollSolucionesNodos = altoScrollSolucionesNodos;
   }

}
