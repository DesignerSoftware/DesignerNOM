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
    private String altoScrollComprobante, altoScrollCortesProcesos;
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
    private String infoRegistroProceso, infoRegistroTercero, infoRegistroCuenta, infoRegistroCentroCosto;
    //
    private Date auxFechaEntregadoComprobante;

    private DataTable tabla1, tabla2;

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
        altoScrollComprobante = "57";
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
        infoRegistroCuenta = "0";
        infoRegistroCentroCosto = "0";
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
                    cargarListasConComprobante();
                }
            }
        }
        contarRegistrosComp();
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

    public void cambiarIndiceComprobantes(Comprobantes comprobante, int celda) {
        comprobanteSeleccionado = comprobante;
        RequestContext context = RequestContext.getCurrentInstance();
        if (modificacionesSolucionesNodosEmpleado == false && modificacionesSolucionesNodosEmpleador == false) {
            if (modificacionesCortesProcesos == false) {
                if (permitirIndex == true) {
                    subtotalPago = new BigDecimal(0);
                    subtotalDescuento = new BigDecimal(0);
                    subtotalPasivo = new BigDecimal(0);
                    subtotalGasto = new BigDecimal(0);
                    cualCelda = celda;
                    tipoTabla = 0;
                    //Para los rastros
                    nombreTabla = "Comprobantes";
                    secRegistro = comprobanteSeleccionado.getSecuencia();//
                    auxFechaEntregadoComprobante = comprobanteSeleccionado.getFechaentregado();

                    cargarListasConComprobante();

                    if (banderaCortesProcesos == 1) {
                        FacesContext c = FacesContext.getCurrentInstance();

                        fechaCorteCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:fechaCorteCP");
                        fechaCorteCP.setFilterStyle("display: none; visibility: hidden;");
                        procesoCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:procesoCP");
                        procesoCP.setFilterStyle("display: none; visibility: hidden;");
                        altoScrollComprobante = "57";
                        RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
                        banderaCortesProcesos = 0;
                        filtradolistaCortesProcesos = null;
                        tipoListaCortesProcesos = 0;
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
                context.execute("confirmarGuardar.show();");
            }
        } else {
            context.execute("confirmarGuardar.show();");
        }
    }

    public void cambiarIndiceCortesProcesos(CortesProcesos corteProceso, int celda) {
        cortesProcesosSeleccionado = corteProceso;
        RequestContext context = RequestContext.getCurrentInstance();
        if (modificacionesSolucionesNodosEmpleado == false && modificacionesSolucionesNodosEmpleador == false) {
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
                FacesContext c = FacesContext.getCurrentInstance();

                numeroComprobanteC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:numeroComprobanteC");
                numeroComprobanteC.setFilterStyle("display: none; visibility: hidden;");
                fechaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaC");
                fechaC.setFilterStyle("display: none; visibility: hidden;");
                fechaEntregaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaEntregaC");
                fechaEntregaC.setFilterStyle("display: none; visibility: hidden;");
                altoScrollComprobante = "57";
                context.update("form:datosComprobantes");
                banderaComprobantes = 0;
                filtradolistaComprobantes = null;
                tipoListaComprobantes = 0;
            }
            tablaExportar = ":formExportar:datosCortesProcesosExportar";
            nombreArchivoExportar = "CortesProcesosXML";
            context.update("form:exportarXML");
            context.update("form:subTotalPago");
            context.update("form:subTotalDescuento");
            context.update("form:subTotalPasivo");
            context.update("form:subTotalGasto");
            context.update("form:neto");
        } else {
            context.execute("confirmarGuardar.show();");
        }
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
                tercero = empleadoTablaSeleccionado.getNit().getNombre();
            }
            if (cualCelda == 8) {
                codigoDebito = empleadoTablaSeleccionado.getCuentad().getCodigo();
            }
            if (cualCelda == 9) {
                centroCostoDebito = empleadoTablaSeleccionado.getCentrocostod().getNombre();
            }
            if (cualCelda == 10) {
                codigoCredito = empleadoTablaSeleccionado.getCuentac().getCodigo();
            }
            if (cualCelda == 11) {
                centroCostoCredito = empleadoTablaSeleccionado.getCentrocostoc().getNombre();
            }
        }
        /* if (banderaComprobantes == 1) {
         numeroComprobanteC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:numeroComprobanteC");
         numeroComprobanteC.setFilterStyle("display: none; visibility: hidden;");
         fechaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaC");
         fechaC.setFilterStyle("display: none; visibility: hidden;");
         fechaEntregaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaEntregaC");
         fechaEntregaC.setFilterStyle("display: none; visibility: hidden;");
         altoScrollComprobante = "61";
         RequestContext.getCurrentInstance().update("form:datosComprobantes");
         banderaComprobantes = 0;
         filtradolistaComprobantes = null;
         tipoListaComprobantes = 0;
         }*/
        //tablaExportar = ":formExportar:datosCortesProcesosExportar";
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
                tercero = empleadorTablaSeleccionado.getNit().getNombre();
            }
            if (cualCelda == 8) {
                codigoDebito = empleadorTablaSeleccionado.getCuentad().getCodigo();
            }
            if (cualCelda == 9) {
                centroCostoDebito = empleadorTablaSeleccionado.getCentrocostod().getNombre();
            }
            if (cualCelda == 10) {
                codigoCredito = empleadorTablaSeleccionado.getCuentac().getCodigo();
            }
            if (cualCelda == 11) {
                centroCostoCredito = empleadorTablaSeleccionado.getCentrocostoc().getNombre();
            }
        }
        /* if (banderaComprobantes == 1) {
         numeroComprobanteC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:numeroComprobanteC");
         numeroComprobanteC.setFilterStyle("display: none; visibility: hidden;");
         fechaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaC");
         fechaC.setFilterStyle("display: none; visibility: hidden;");
         fechaEntregaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaEntregaC");
         fechaEntregaC.setFilterStyle("display: none; visibility: hidden;");
         altoScrollComprobante = "61";
         RequestContext.getCurrentInstance().update("form:datosComprobantes");
         banderaComprobantes = 0;
         filtradolistaComprobantes = null;
         tipoListaComprobantes = 0;
         }*/
        //tablaExportar = ":formExportar:datosCortesProcesosExportar";
        nombreArchivoExportar = "SolucionesNodosEmpleadorXML";
    }

    public void modificarComprobantesFechaEntregado(Comprobantes comprobante, int celda) {
        comprobanteSeleccionado = comprobante;
        System.out.println("modificarComprobantesFechaEntregado()");
        if (comprobanteSeleccionado.getFechaentregado() == null) {
            System.out.println("comprobanteSeleccionado.getFechaentregado() == null");
            comprobanteSeleccionado.setFechaentregado(auxFechaEntregadoComprobante);
            RequestContext.getCurrentInstance().execute("errorFechaEntregaNull.show()");
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

        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        secRegistro = null;
        tipoTabla = -1;
        modificacionesComprobantes = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosComprobantes");
    }

    public void modificarCortesProcesos(CortesProcesos corteProceso, String confirmarCambio, String valorConfirmar) {
        cortesProcesosSeleccionado = corteProceso;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
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
            context.update("form:datosCortesProcesos");
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
                context.update("formularioDialogos:ProcesosDialogo");
                contarRegistrosLovProc(0);
                context.execute("ProcesosDialogo.show()");
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

        context.update("form:datosCortesProcesos");
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
                    context.update("formularioDialogos:nuevoProceso");
                } else if (tipoNuevo == 2) {
                    duplicarCorteProceso.setProceso(lovProcesos.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicadoProceso");
                }
//                lovProcesos.clear();
//                getListaProcesos();
            } else {
                context.update("formularioDialogos:ProcesosDialogo");
                contarRegistrosLovProc(0);
                context.execute("ProcesosDialogo.show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoProceso");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicadoProceso");
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
                empleadoTablaSeleccionado.getNit().setNombre(tercero);
            } else if (tipoTabla == 3) {
                empleadorTablaSeleccionado = sn;
                empleadorTablaSeleccionado.getNit().setNombre(tercero);
            }

            for (int i = 0; i < lovTerceros.size(); i++) {
                if (lovTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }

            if (coincidencias == 1) {
                if (tipoTabla == 2) {
                    empleadoTablaSeleccionado.setNit(lovTerceros.get(indiceUnicoElemento));
                } else if (tipoTabla == 3) {
                    empleadorTablaSeleccionado.setNit(lovTerceros.get(indiceUnicoElemento));
                }
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:TercerosDialogo");
                contarRegistrosLovTercero(0);
                context.execute("TercerosDialogo.show()");
            }
        }
        if (tipoDato.equals("CREDITO")) {
            if (tipoTabla == 2) {
                empleadoTablaSeleccionado = sn;
                empleadoTablaSeleccionado.getCuentac().setCodigo(codigoCredito);
            } else if (tipoTabla == 3) {
                empleadorTablaSeleccionado = sn;
                empleadorTablaSeleccionado.getCuentac().setCodigo(codigoCredito);
            }

            for (int i = 0; i < lovCuentas.size(); i++) {
                if (lovCuentas.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoTabla == 2) {
                    empleadoTablaSeleccionado.setCuentac(lovCuentas.get(indiceUnicoElemento));
                } else if (tipoTabla == 3) {
                    empleadorTablaSeleccionado.setCuentac(lovCuentas.get(indiceUnicoElemento));
                }
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:CuentaCreditoDialogo");
                contarRegistrosLovCuentas(0);
                context.execute("CuentaCreditoDialogo.show()");
            }
        }
        if (tipoDato.equals("DEBITO")) {
            if (tipoTabla == 2) {
                empleadoTablaSeleccionado = sn;
                empleadoTablaSeleccionado.getCuentad().setCodigo(codigoDebito);
            } else if (tipoTabla == 3) {
                empleadorTablaSeleccionado = sn;
                empleadorTablaSeleccionado.getCuentad().setCodigo(codigoDebito);
            }

            for (int i = 0; i < lovCuentas.size(); i++) {
                if (lovCuentas.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoTabla == 2) {
                    empleadoTablaSeleccionado.setCuentad(lovCuentas.get(indiceUnicoElemento));
                } else if (tipoTabla == 3) {
                    empleadorTablaSeleccionado.setCuentad(lovCuentas.get(indiceUnicoElemento));
                }
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:CuentaDebitoDialogo");
                contarRegistrosLovCuentas(0);
                context.execute("CuentaDebitoDialogo.show()");
            }
        }
        if (tipoDato.equals("CENTROCOSTODEBITO")) {
            if (tipoTabla == 2) {
                empleadoTablaSeleccionado = sn;
                empleadoTablaSeleccionado.getCentrocostod().setNombre(centroCostoDebito);
            } else if (tipoTabla == 3) {
                empleadorTablaSeleccionado = sn;
                empleadorTablaSeleccionado.getCentrocostod().setNombre(centroCostoDebito);
            }

            for (int i = 0; i < lovCentrosCostos.size(); i++) {
                if (lovCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoTabla == 2) {
                    empleadoTablaSeleccionado.setCentrocostod(lovCentrosCostos.get(indiceUnicoElemento));
                } else if (tipoTabla == 3) {
                    empleadorTablaSeleccionado.setCentrocostod(lovCentrosCostos.get(indiceUnicoElemento));
                }
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:CentroCostoDebitoDialogo");
                contarRegistrosLovCC(0);
                context.execute("CentroCostoDebitoDialogo.show()");
            }
        }
        if (tipoDato.equals("CENTROCOSTOCREDITO")) {
            if (tipoTabla == 2) {
                empleadoTablaSeleccionado = sn;
                empleadoTablaSeleccionado.getCentrocostoc().setNombre(centroCostoCredito);
            } else if (tipoTabla == 3) {
                empleadorTablaSeleccionado = sn;
                empleadorTablaSeleccionado.getCentrocostoc().setNombre(centroCostoCredito);
            }

            for (int i = 0; i < lovCentrosCostos.size(); i++) {
                if (lovCentrosCostos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoTabla == 2) {
                    empleadoTablaSeleccionado.setCentrocostoc(lovCentrosCostos.get(indiceUnicoElemento));
                } else if (tipoTabla == 3) {
                    empleadorTablaSeleccionado.setCentrocostoc(lovCentrosCostos.get(indiceUnicoElemento));
                }
            } else {
                permitirIndex = false;
                context.update("formularioDialogos:CentroCostoCreditoDialogo");
                contarRegistrosLovCC(0);
                context.execute("CentroCostoCreditoDialogo.show()");
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
                context.update("form:tablaEmpleado");
            } else if (tipoTabla == 3) {
                if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
                    listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
                } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
                    listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
                }
                modificacionesSolucionesNodosEmpleador = true;
                context.update("form:tablaEmpleador");
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
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = 0;
        context.update("formularioDialogos:ProcesosDialogo");
        contarRegistrosLovProc(0);
        context.execute("ProcesosDialogo.show()");
    }

    public void llamarLOVProcesosNuevo_Duplicado(int tipoN) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoN == 1) {
            tipoActualizacion = 1;
        } else {
            tipoActualizacion = 2;
        }
        context.update("formularioDialogos:ProcesosDialogo");
        contarRegistrosLovProc(0);
        context.execute("ProcesosDialogo.show()");
    }

    //LOV TERCEROS
    public void llamarLOVTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:TercerosDialogo");
        contarRegistrosLovTercero(0);
        context.execute("TercerosDialogo.show()");
    }

    public void llamarLOVCuentaDebito() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:CuentaDebitoDialogo");
        contarRegistrosLovCuentas(0);
        context.execute("CuentaDebitoDialogo.show()");
    }

    public void llamarLOVCuentaCredito() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:CuentaCreditoDialogo");
        contarRegistrosLovCuentas(0);
        context.execute("CuentaCreditoDialogo.show()");
    }

    public void llamarLOVCentroCostoDebito() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:CentroCostoDebitoDialogo");
        contarRegistrosLovCC(0);
        context.execute("CentroCostoDebitoDialogo.show()");
    }

    public void llamarLOVCentroCostoCredito() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:CentroCostoCreditoDialogo");
        contarRegistrosLovCC(0);
        context.execute("CentroCostoCreditoDialogo.show()");
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
            context.update("form:datosCortesProcesos");
            modificacionesCortesProcesos = true;
            permitirIndex = true;
        } else if (tipoActualizacion == 1) {
            nuevoCorteProceso.setProceso(procesoSelecionado);
            context.update("formularioDialogos:nuevoCorteProceso");
        } else if (tipoActualizacion == 2) {
            duplicarCorteProceso.setProceso(procesoSelecionado);
            context.update("formularioDialogos:duplicadoCorteProceso");
        }
        filtradoProcesos = null;
        procesoSelecionado = null;
        aceptar = true;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        tipoTabla = -1;
        context.update("formularioDialogos:ProcesosDialogo");
        context.update("formularioDialogos:lovProcesos");
        context.update("formularioDialogos:aceptarP");
        context.reset("formularioDialogos:lovProcesos:globalFilter");
        context.execute("lovProcesos.clearFilters()");
        context.execute("ProcesosDialogo.hide()");
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
        context.update("formularioDialogos:ProcesosDialogo");
        context.update("formularioDialogos:lovProcesos");
        context.update("formularioDialogos:aceptarP");
        context.reset("formularioDialogos:lovProcesos:globalFilter");
        context.execute("lovProcesos.clearFilters()");
        context.execute("ProcesosDialogo.hide()");
    }

    //ACTUALIZAR TERCERO
    public void actualizarTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoTabla == 2) {
            empleadoTablaSeleccionado.setNit(TerceroSelecionado);
            if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
                listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
                listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.update("form:tablaEmpleado");
            modificacionesSolucionesNodosEmpleado = true;
            permitirIndex = true;
        } else if (tipoTabla == 3) {
            empleadorTablaSeleccionado.setNit(TerceroSelecionado);
            if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
                listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
                listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.update("form:tablaEmpleador");
            modificacionesSolucionesNodosEmpleador = true;
            permitirIndex = true;
        }

        filtradolistaTerceros = null;
        TerceroSelecionado = null;
        aceptar = true;
        secRegistro = null;
        cualCelda = -1;
        tipoTabla = -1;
        context.update("formularioDialogos:TercerosDialogo");
        context.update("formularioDialogos:lovTerceros");
        context.update("formularioDialogos:aceptarT");
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        context.execute("lovTerceros.clearFilters()");
        context.execute("TercerosDialogo.hide()");
    }

    public void cancelarTercero() {
        filtradolistaTerceros = null;
        TerceroSelecionado = null;
        aceptar = true;
        secRegistro = null;
        permitirIndex = true;
        tipoTabla = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:TercerosDialogo");
        context.update("formularioDialogos:lovTerceros");
        context.update("formularioDialogos:aceptarT");
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        context.execute("lovTerceros.clearFilters()");
        context.execute("TercerosDialogo.hide()");
    }

    public void actualizarCuentaDebito() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoTabla == 2) {
            empleadoTablaSeleccionado.setCuentad(cuentaSeleccionada);
            if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
                listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
                listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.update("form:tablaEmpleado");
            modificacionesSolucionesNodosEmpleado = true;
            permitirIndex = true;
        } else if (tipoTabla == 3) {
            empleadorTablaSeleccionado.setCuentad(cuentaSeleccionada);
            if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
                listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
                listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.update("form:tablaEmpleador");
            modificacionesSolucionesNodosEmpleador = true;
            permitirIndex = true;
        }

        filtrarLovCuentas = null;
        cuentaSeleccionada = new Cuentas();
        aceptar = true;
        secRegistro = null;
        cualCelda = -1;
        tipoTabla = -1;
        context.update("formularioDialogos:CuentaDebitoDialogo");
        context.update("formularioDialogos:lovCuentaDebito");
        context.update("formularioDialogos:aceptarCD");
        context.reset("formularioDialogos:lovCuentaDebito:globalFilter");
        context.execute("lovCuentaDebito.clearFilters()");
        context.execute("CuentaDebitoDialogo.hide()");
    }

    public void cancelarCuentaDebito() {
        filtrarLovCuentas = null;
        cuentaSeleccionada = new Cuentas();
        aceptar = true;
        secRegistro = null;
        permitirIndex = true;
        tipoTabla = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:CuentaDebitoDialogo");
        context.update("formularioDialogos:lovCuentaDebito");
        context.update("formularioDialogos:aceptarCD");
        context.reset("formularioDialogos:lovCuentaDebito:globalFilter");
        context.execute("lovCuentaDebito.clearFilters()");
        context.execute("CuentaDebitoDialogo.hide()");
    }

    public void actualizarCuentaCredito() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoTabla == 2) {
            empleadoTablaSeleccionado.setCuentac(cuentaSeleccionada);
            if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
                listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
                listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.update("form:tablaEmpleado");
            modificacionesSolucionesNodosEmpleado = true;
            permitirIndex = true;
        } else if (tipoTabla == 3) {
            empleadorTablaSeleccionado.setCuentac(cuentaSeleccionada);
            if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
                listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
                listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.update("form:tablaEmpleador");
            modificacionesSolucionesNodosEmpleador = true;
            permitirIndex = true;
        }

        filtrarLovCuentas = null;
        cuentaSeleccionada = new Cuentas();
        aceptar = true;
        secRegistro = null;
        cualCelda = -1;
        tipoTabla = -1;
        context.update("formularioDialogos:CuentaCreditoDialogo");
        context.update("formularioDialogos:lovCuentaCredito");
        context.update("formularioDialogos:aceptarCC");
        context.reset("formularioDialogos:lovCuentaCredito:globalFilter");
        context.execute("lovCuentaCredito.clearFilters()");
        context.execute("CuentaCreditoDialogo.hide()");
    }

    public void cancelarCuentaCredito() {
        filtrarLovCuentas = null;
        cuentaSeleccionada = new Cuentas();
        aceptar = true;
        secRegistro = null;
        permitirIndex = true;
        tipoTabla = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:CuentaCreditoDialogo");
        context.update("formularioDialogos:lovCuentaCredito");
        context.update("formularioDialogos:aceptarCC");
        context.reset("formularioDialogos:lovCuentaCredito:globalFilter");
        context.execute("lovCuentaCredito.clearFilters()");
        context.execute("CuentaCreditoDialogo.hide()");
    }

    public void actualizarCentroCostoCredito() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoTabla == 2) {
            empleadoTablaSeleccionado.setCentrocostoc(centroCostoSeleccionado);
            if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
                listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
                listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.update("form:tablaEmpleado");
            modificacionesSolucionesNodosEmpleado = true;
            permitirIndex = true;
        } else if (tipoTabla == 3) {
            empleadorTablaSeleccionado.setCentrocostoc(centroCostoSeleccionado);
            if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
                listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
                listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.update("form:tablaEmpleador");
            modificacionesSolucionesNodosEmpleador = true;
            permitirIndex = true;
        }

        filtrarLovCentrosCostos = null;
        centroCostoSeleccionado = null;
        aceptar = true;
        secRegistro = null;
        cualCelda = -1;
        tipoTabla = -1;
        context.update("formularioDialogos:CentroCostoCreditoDialogo");
        context.update("formularioDialogos:lovCentroCostoCredito");
        context.update("formularioDialogos:aceptarCCC");
        context.reset("formularioDialogos:lovCentroCostoCredito:globalFilter");
        context.execute("lovCentroCostoCredito.clearFilters()");
        context.execute("CentroCostoCreditoDialogo.hide()");
    }

    public void cancelarCentroCostoCredito() {
        filtrarLovCentrosCostos = null;
        centroCostoSeleccionado = null;
        aceptar = true;
        secRegistro = null;
        permitirIndex = true;
        tipoTabla = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:CentroCostoCreditoDialogo");
        context.update("formularioDialogos:lovCentroCostoCredito");
        context.update("formularioDialogos:aceptarCCC");
        context.reset("formularioDialogos:lovCentroCostoCredito:globalFilter");
        context.execute("lovCentroCostoCredito.clearFilters()");
        context.execute("CentroCostoCreditoDialogo.hide()");
    }

    public void actualizarCentroCostoDebito() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoTabla == 2) {
            empleadoTablaSeleccionado.setCentrocostod(centroCostoSeleccionado);
            if (listaSolucionesNodosEmpleadoModificar.isEmpty()) {
                listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadoModificar.contains(empleadoTablaSeleccionado)) {
                listaSolucionesNodosEmpleadoModificar.add(empleadoTablaSeleccionado);
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.update("form:tablaEmpleado");
            modificacionesSolucionesNodosEmpleado = true;
            permitirIndex = true;
        } else if (tipoTabla == 3) {
            empleadorTablaSeleccionado.setCentrocostod(centroCostoSeleccionado);
            if (listaSolucionesNodosEmpleadorModificar.isEmpty()) {
                listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            } else if (!listaSolucionesNodosEmpleadorModificar.contains(empleadorTablaSeleccionado)) {
                listaSolucionesNodosEmpleadorModificar.add(empleadorTablaSeleccionado);
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.update("form:tablaEmpleador");
            modificacionesSolucionesNodosEmpleador = true;
            permitirIndex = true;
        }

        aceptar = true;
        secRegistro = null;
        cualCelda = -1;
        tipoTabla = -1;
        context.update("formularioDialogos:CentroCostoDebitoDialogo");
        context.update("formularioDialogos:lovCentroCostoDebito");
        context.update("formularioDialogos:aceptarCCD");
        context.reset("formularioDialogos:lovCentroCostoDebito:globalFilter");
        context.execute("lovCentroCostoDebito.clearFilters()");
        context.execute("CentroCostoDebitoDialogo.hide()");
    }

    public void cancelarCentroCostoDebito() {
        filtrarLovCentrosCostos = null;
        centroCostoSeleccionado = null;
        aceptar = true;
        secRegistro = null;
        permitirIndex = true;
        tipoTabla = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:CentroCostoDebitoDialogo");
        context.update("formularioDialogos:lovCentroCostoDebito");
        context.update("formularioDialogos:aceptarCCD");
        context.reset("formularioDialogos:lovCentroCostoDebito:globalFilter");
        context.execute("lovCentroCostoDebito.clearFilters()");
        context.execute("CentroCostoDebitoDialogo.hide()");
    }

    //BORRAR VC
    public void borrar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoTabla == 0) {
            if (comprobanteSeleccionado != null) {
                if (administrarComprobantes.consultarCortesProcesosComprobante(comprobanteSeleccionado.getSecuencia()).isEmpty()) {
                    if (!listaComprobantesModificar.isEmpty() && listaComprobantesModificar.contains(comprobanteSeleccionado)) {
                        int modIndex = listaComprobantesModificar.indexOf(comprobanteSeleccionado);
                        listaComprobantesModificar.remove(modIndex);
                        listaComprobantesBorrar.add(comprobanteSeleccionado);
                    } else if (!listaComprobantesCrear.isEmpty() && listaComprobantesCrear.contains(comprobanteSeleccionado)) {
                        int crearIndex = listaComprobantesCrear.indexOf(comprobanteSeleccionado);
                        listaComprobantesCrear.remove(crearIndex);
                    } else {
                        listaComprobantesBorrar.add(comprobanteSeleccionado);
                    }
                    listaComprobantes.remove(comprobanteSeleccionado);
                    modificacionesComprobantes = true;
                } else if (!listaComprobantesCrear.isEmpty() && listaComprobantesCrear.contains(comprobanteSeleccionado)) {
                    int crearIndex = listaComprobantesCrear.indexOf(comprobanteSeleccionado);
                    listaComprobantesCrear.remove(crearIndex);
                    listaComprobantes.remove(comprobanteSeleccionado);
                    modificacionesComprobantes = true;
                } else {
                    context.execute("errorBorrarComprobante.show();");
                }
                if (tipoListaComprobantes == 1) {
                    filtradolistaComprobantes.remove(comprobanteSeleccionado);
                }
                context.update("form:datosComprobantes");
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
                    context.execute("errorBorrarCortesProcesos.show();");
                }
                if (tipoListaCortesProcesos == 1) {
                    filtradolistaCortesProcesos.remove(cortesProcesosSeleccionado);
                }
                context.update("form:datosCortesProcesos");
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
                    context.execute("NuevoRegistroPagina.show()");
                }
            } else if (tipoTabla == 0) {
                BigInteger sugerenciaNumero = administrarComprobantes.consultarMaximoNumeroComprobante().add(new BigInteger("1"));
                nuevoComprobante.setNumero(sugerenciaNumero);
                context.update("formularioDialogos:nuevoComprobante");
                context.execute("NuevoRegistroComprobantes.show()");
            } else if (tipoTabla == 1) {
                context.update("formularioDialogos:nuevoCorteProceso");
                context.execute("NuevoRegistroCortesProcesos.show()");
            }
        } else {
            BigInteger sugerenciaNumero = administrarComprobantes.consultarMaximoNumeroComprobante().add(new BigInteger("1"));
            nuevoComprobante.setNumero(sugerenciaNumero);
            context.update("formularioDialogos:nuevoComprobante");
            context.execute("NuevoRegistroComprobantes.show()");
        }
    }

    public void agregarNuevoComprobante() {
        int pasa = 0;
        mensajeValidacion = "";
        RequestContext context = RequestContext.getCurrentInstance();
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
                FacesContext c = FacesContext.getCurrentInstance();

                numeroComprobanteC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:numeroComprobanteC");
                numeroComprobanteC.setFilterStyle("display: none; visibility: hidden;");
                fechaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaC");
                fechaC.setFilterStyle("display: none; visibility: hidden;");
                fechaEntregaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaEntregaC");
                fechaEntregaC.setFilterStyle("display: none; visibility: hidden;");
                altoScrollComprobante = "57";
                RequestContext.getCurrentInstance().update("form:datosComprobantes");
                banderaComprobantes = 0;
                filtradolistaComprobantes = null;
                tipoListaComprobantes = 0;
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
            context.update("form:datosComprobantes");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.execute("NuevoRegistroComprobantes.hide()");
            modificacionesComprobantes = true;
            tipoTabla = -1;
            secRegistro = null;
        } else {
            context.update("formularioDialogos:validacioNuevoComprobante");
            context.execute("validacioNuevoComprobante.show()");
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
        RequestContext context = RequestContext.getCurrentInstance();
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
                FacesContext c = FacesContext.getCurrentInstance();

                fechaCorteCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:fechaCorteCP");
                fechaCorteCP.setFilterStyle("display: none; visibility: hidden;");
                procesoCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:procesoCP");
                procesoCP.setFilterStyle("display: none; visibility: hidden;");
                altoScrollComprobante = "57";
                RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
                banderaCortesProcesos = 0;
                filtradolistaCortesProcesos = null;
                tipoListaCortesProcesos = 0;
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
            context.update("form:datosCortesProcesos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            context.execute("NuevoRegistroCortesProcesos.hide()");
            modificacionesCortesProcesos = true;
            tipoTabla = -1;
            secRegistro = null;
        } else {
            context.update("formularioDialogos:validacioNuevoCorteProceso");
            context.execute("validacioNuevoCorteProceso.show()");
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

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDialogos:duplicadoComprobante");
                context.execute("DuplicarRegistroComprobantes.show()");
                secRegistro = null;
            }
        } else if (tipoTabla == 1) {
            if (cortesProcesosSeleccionado != null) {
                duplicarCorteProceso = new CortesProcesos();
                duplicarCorteProceso.setCorte(cortesProcesosSeleccionado.getCorte());
                duplicarCorteProceso.setProceso(cortesProcesosSeleccionado.getProceso());
                duplicarCorteProceso.setEmpleado(cortesProcesosSeleccionado.getEmpleado());
                duplicarCorteProceso.setComprobante(comprobanteSeleccionado);

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDialogos:duplicadoCorteProceso");
                context.execute("DuplicarCortesProcesos.show()");
                secRegistro = null;
            }
        }
    }

    public void confirmarDuplicarComprobantes() {
        int pasa = 0;
        mensajeValidacion = "";
        RequestContext context = RequestContext.getCurrentInstance();
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
            context.update("form:datosComprobantes");
            context.execute("DuplicarRegistroComprobantes.hide()");
            tipoTabla = -1;
            modificacionesComprobantes = true;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (banderaComprobantes == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                numeroComprobanteC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:numeroComprobanteC");
                numeroComprobanteC.setFilterStyle("display: none; visibility: hidden;");
                fechaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaC");
                fechaC.setFilterStyle("display: none; visibility: hidden;");
                fechaEntregaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaEntregaC");
                fechaEntregaC.setFilterStyle("display: none; visibility: hidden;");
                altoScrollComprobante = "57";
                RequestContext.getCurrentInstance().update("form:datosComprobantes");
                banderaComprobantes = 0;
                filtradolistaComprobantes = null;
                tipoListaComprobantes = 0;
            }
        } else {
            context.update("formularioDialogos:validacioNuevoComprobante");
            context.execute("validacioNuevoComprobante.show()");
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
        RequestContext context = RequestContext.getCurrentInstance();
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
            context.update("form:datosCortesProcesos");
            context.execute("DuplicarCortesProcesos.hide()");
            tipoTabla = -1;
            modificacionesCortesProcesos = true;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (banderaCortesProcesos == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                fechaCorteCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:fechaCorteCP");
                fechaCorteCP.setFilterStyle("display: none; visibility: hidden;");
                procesoCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:procesoCP");
                procesoCP.setFilterStyle("display: none; visibility: hidden;");
                altoScrollComprobante = "57";
                RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
                banderaCortesProcesos = 0;
                filtradolistaCortesProcesos = null;
                tipoListaCortesProcesos = 0;
            }
        } else {
            context.update("formularioDialogos:validacioNuevoCorteProceso");
            context.execute("validacioNuevoCorteProceso.show()");
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
        if (tipoTabla == 0) {
            if (banderaComprobantes == 0) {
                numeroComprobanteC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:numeroComprobanteC");
                numeroComprobanteC.setFilterStyle("width: 40px;");
                fechaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaC");
                fechaC.setFilterStyle("width: 60px;");
                fechaEntregaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaEntregaC");
                fechaEntregaC.setFilterStyle("width: 60px;");
                altoScrollComprobante = "34";
                RequestContext.getCurrentInstance().update("form:datosComprobantes");
                banderaComprobantes = 1;

            } else if (banderaComprobantes == 1) {
                numeroComprobanteC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:numeroComprobanteC");
                numeroComprobanteC.setFilterStyle("display: none; visibility: hidden;");
                fechaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaC");
                fechaC.setFilterStyle("display: none; visibility: hidden;");
                fechaEntregaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaEntregaC");
                fechaEntregaC.setFilterStyle("display: none; visibility: hidden;");
                altoScrollComprobante = "57";
                RequestContext.getCurrentInstance().update("form:datosComprobantes");
                banderaComprobantes = 0;
                filtradolistaComprobantes = null;
                tipoListaComprobantes = 0;
            }
        } else if (tipoTabla == 1) {
            if (banderaCortesProcesos == 0) {
                fechaCorteCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:fechaCorteCP");
                fechaCorteCP.setFilterStyle("width: 80px;");
                procesoCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:procesoCP");
                procesoCP.setFilterStyle("width: 150px;");
                altoScrollCortesProcesos = "37";
                RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
                banderaCortesProcesos = 1;

            } else if (banderaCortesProcesos == 1) {
                fechaCorteCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:fechaCorteCP");
                fechaCorteCP.setFilterStyle("display: none; visibility: hidden;");
                procesoCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:procesoCP");
                procesoCP.setFilterStyle("display: none; visibility: hidden;");
                altoScrollComprobante = "57";
                RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
                banderaCortesProcesos = 0;
                filtradolistaCortesProcesos = null;
                tipoListaCortesProcesos = 0;
            }
        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (tipoTabla == 0) {
            if (comprobanteSeleccionado != null) {
                editarComprobante = comprobanteSeleccionado;
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 0) {
                    context.update("formularioDialogos:editarNumeroComprobante");
                    context.execute("editarNumeroComprobante.show()");
                } else if (cualCelda == 1) {
                    context.update("formularioDialogos:editarFechaComprobante");
                    context.execute("editarFechaComprobante.show()");
                } else if (cualCelda == 2) {
                    context.update("formularioDialogos:editarFechaEntrega");
                    context.execute("editarFechaEntrega.show()");
                }
            }
        } else if (tipoTabla == 1) {
            editarCorteProceso = cortesProcesosSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                context.update("formularioDialogos:editarFechaCorte");
                context.execute("editarFechaCorte.show()");
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editarProceso");
                context.execute("editarProceso.show()");
            }
        }
        secRegistro = null;
    }

    //LISTA DE VALORES BOTON
    public void listaValoresBoton() {
        if (tipoTabla == 1) {
            if (cortesProcesosSeleccionado != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 1) {
                    context.update("formularioDialogos:ProcesosDialogo");
                    context.execute("ProcesosDialogo.show()");
                    tipoActualizacion = 0;
                }
            }
        }
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        int resultado = -1;
        if (nombreTabla != null) {
            if (nombreTabla.equals("Comprobantes")) {
                resultado = administrarRastros.obtenerTabla(secRegistro, nombreTabla.toUpperCase());
            } else if (nombreTabla.equals("CortesProcesos")) {
                resultado = administrarRastros.obtenerTabla(secRegistro, nombreTabla.toUpperCase());
            }
            if (resultado == 1) {
                context.update("formDialogos:errorObjetosDB");
                context.execute("errorObjetosDB.show()");
            } else if (resultado == 2) {
                context.update("formDialogos:confirmarRastro");
                context.execute("confirmarRastro.show()");
            } else if (resultado == 3) {
                context.update("formDialogos:errorRegistroRastro");
                context.execute("errorRegistroRastro.show()");
            } else if (resultado == 4) {
                context.update("formDialogos:errorTablaConRastro");
                context.execute("errorTablaConRastro.show()");
            } else if (resultado == 5) {
                context.update("formDialogos:errorTablaSinRastro");
                context.execute("errorTablaSinRastro.show()");
            }
        } else {
            context.execute("seleccionarRegistro.show()");
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
            FacesContext c = FacesContext.getCurrentInstance();

            fechaCorteCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:fechaCorteCP");
            fechaCorteCP.setFilterStyle("display: none; visibility: hidden;");
            procesoCP = (Column) c.getViewRoot().findComponent("form:datosCortesProcesos:procesoCP");
            procesoCP.setFilterStyle("display: none; visibility: hidden;");
            altoScrollComprobante = "57";
            RequestContext.getCurrentInstance().update("form:datosCortesProcesos");
            banderaCortesProcesos = 0;
            filtradolistaCortesProcesos = null;
            tipoListaCortesProcesos = 0;
        }
        if (banderaComprobantes == 1) {
            FacesContext c = FacesContext.getCurrentInstance();

            numeroComprobanteC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:numeroComprobanteC");
            numeroComprobanteC.setFilterStyle("display: none; visibility: hidden;");
            fechaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaC");
            fechaC.setFilterStyle("display: none; visibility: hidden;");
            fechaEntregaC = (Column) c.getViewRoot().findComponent("form:datosComprobantes:fechaEntregaC");
            fechaEntregaC.setFilterStyle("display: none; visibility: hidden;");
            altoScrollComprobante = "57";
            RequestContext.getCurrentInstance().update("form:datosComprobantes");
            banderaComprobantes = 0;
            filtradolistaComprobantes = null;
            tipoListaComprobantes = 0;
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
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:ACEPTAR");
        context.update("form:datosComprobantes");
        context.update("form:datosCortesProcesos");
        context.update("form:tablaEmpleado");
        context.update("form:tablaEmpleador");
    }

    public void guardarCambiosComprobantes() {
        RequestContext context = RequestContext.getCurrentInstance();
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
            context.update("form:datosComprobantes");
            guardado = true;
            modificacionesComprobantes = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Los datos de Comprobante se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosComprobantes Controlador: " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Comprobante, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
    }

    public void eliminarComprobantegeneral() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("Entro en eliminarComprobantegeneral()");
        try {
            System.out.println("cortesProcesosSeleccionado : " + cortesProcesosSeleccionado);
            if(cortesProcesosSeleccionado != null){
            administrarComprobantes.eliminarCPconUndoCierre(cortesProcesosSeleccionado.getSecuencia(), secuenciaEmpleado, cortesProcesosSeleccionado.getCorte());
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
            context.update("form:datosComprobantes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "El Comprobante se elimino con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            } else{
                context.execute("seleccionarRegistro.show()");
            }
        } catch (Exception e) {
            System.out.println("Error eliminarComprobantegeneral Controlador: " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error al eliminar el Comprobante.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
    }

    public void guardarCambiosCorteProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
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
            listaCortesProcesos = null;
            context.update("form:datosCortesProcesos");
            guardado = true;
            modificacionesCortesProcesos = false;
            context.update("form:aceptar");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Los datos de Corte Proceso se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosCorteProceso Controlador: " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Corte Proceso, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
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
            context.update("form:tablaEmpleado");
            guardado = true;
            modificacionesSolucionesNodosEmpleado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Los datos de Empleado se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosEmpleado Controlador: " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Empleado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
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
            context.update("form:tablaEmpleador");
            guardado = true;
            modificacionesSolucionesNodosEmpleador = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Los datos de Empleador se guardaron con Éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosEmpleador Controlador: " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Empleador, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
    }

    //GUARDAR
    public void guardarCambios() {
        System.out.println("guardarCambios() guardado : " + guardado);
        System.out.println("guardarCambios() modificacionesComprobantes : " + modificacionesComprobantes);
        System.out.println("guardarCambios() modificacionesCortesProcesos : " + modificacionesCortesProcesos);
        System.out.println("guardarCambios() modificacionesSolucionesNodosEmpleado : " + modificacionesSolucionesNodosEmpleado);
        System.out.println("guardarCambios() modificacionesSolucionesNodosEmpleador : " + modificacionesComprobantes);
        RequestContext context = RequestContext.getCurrentInstance();
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
            context.update("form:datosComprobantes");
            context.update("form:datosCortesProcesos");
            context.update("form:tablaEmpleado");
            context.update("form:tablaEmpleador");
            context.update("form:exportarXML");
            secRegistro = null;
        }
    }
//
//    public void seleccionTablaEmpleadoIz() {
//        empleadoTablaSeleccionado = listaSolucionesNodosEmpleado.;
//        FacesContext c = FacesContext.getCurrentInstance();
//        RequestContext.getCurrentInstance().execute("$('.tabla2ScrollInteligente .ui-datatable-scrollable-body').animate({scrollTop : 0}, 500);");
//        tabla1 = (DataTable) c.getViewRoot().findComponent("form:tablaEmpleado");
//        tabla1.setSelection(empleadoTablaSeleccionado);
//        RequestContext.getCurrentInstance().update("form:tablaEmpleado");
//        RequestContext.getCurrentInstance().execute("if ($('.tabla2ScrollInteligente .ui-datatable .ui-state-highlight').offset().top-(275.25) < 0.5) {"
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
//        RequestContext.getCurrentInstance().execute("if ($('.tablaScrollInteligente .ui-datatable .ui-state-highlight').offset().top-(275.25) < 0.5) {"
//                + "$('.tablaScrollInteligente .ui-datatable-scrollable-body').animate({scrollTop : ($('.tablaScrollInteligente .ui-datatable .ui-state-highlight').offset().top-(275))+60}, 1000);} "
//                + "else if ($('.tablaScrollInteligente .ui-datatable .ui-state-highlight').offset().top-(276.25) > 70) {"
//                + "$('.tablaScrollInteligente .ui-datatable-scrollable-body').animate({scrollTop : ($('.tablaScrollInteligente .ui-datatable .ui-state-highlight').offset().top-22)}, 1000);}");
//    }

    public void organizarTablasEmpleado() {
        RequestContext.getCurrentInstance().update("form:tablaEmpleado");
    }

    public void organizarTablasEmpleador() {
        RequestContext.getCurrentInstance().update("form:tablaEmpleador");
    }

    //EVENTO FILTRAR
    public void eventoFiltrarComponentes() {
        if (tipoListaComprobantes == 0) {
            tipoListaComprobantes = 1;
        }
    }

    public void eventoFiltrarCortesProcesos() {
        if (tipoListaCortesProcesos == 0) {
            tipoListaCortesProcesos = 1;
        }
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
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:parcialesConcepto");
        context.execute("parcialesConcepto.show();");
    }

    public void verDetallesFormula() {
        listaDetallesFormulas = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:detallesFormulas");
        context.execute("detallesFormulas.show();");
    }

    public void contarRegistrosComp() {
        if (tipoListaComprobantes == 1) {
            infoRegistroComp = String.valueOf(filtradolistaComprobantes.size());
        } else if (listaComprobantes != null) {
            infoRegistroComp = String.valueOf(listaComprobantes.size());
        } else {
            infoRegistroComp = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistroCompr");
    }

    public void contarRegistrosCP() {
        if (tipoListaCortesProcesos == 1) {
            infoRegistroCP = String.valueOf(filtradolistaCortesProcesos.size());
        } else if (listaCortesProcesos != null) {
            infoRegistroCP = String.valueOf(listaCortesProcesos.size());
        } else {
            infoRegistroCP = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistroProc");
    }

    public void contarRegistrosTE() {
        if (tipoListaSNEmpleado == 1) {
            infoRegistroTEmpleado = String.valueOf(filtradolistaSolucionesNodosEmpleado.size());
        } else if (listaSolucionesNodosEmpleado != null) {
            infoRegistroTEmpleado = String.valueOf(listaSolucionesNodosEmpleado.size());
        } else {
            infoRegistroTEmpleado = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistroE");
    }

    public void contarRegistrosTEr() {
        if (tipoListaSNEmpleador == 1) {
            infoRegistroTEmpleador = String.valueOf(filtradolistaSolucionesNodosEmpleador.size());
        } else if (listaSolucionesNodosEmpleador != null) {
            infoRegistroTEmpleador = String.valueOf(listaSolucionesNodosEmpleador.size());
        } else {
            infoRegistroTEmpleador = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistroEr");
    }

    //Conteo Listas de Valor :
    public void contarRegistrosLovProc(int tipoListaLov) {
        if (tipoListaLov == 1) {
            infoRegistroProceso = String.valueOf(filtradoProcesos.size());
        } else if (lovProcesos != null) {
            infoRegistroProceso = String.valueOf(lovProcesos.size());
        } else {
            infoRegistroProceso = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroProceso");
    }

    public void contarRegistrosLovTercero(int tipoListaLov) {
        if (tipoListaLov == 1) {
            infoRegistroTercero = String.valueOf(filtradolistaTerceros.size());
        } else if (lovTerceros != null) {
            infoRegistroTercero = String.valueOf(lovTerceros.size());
        } else {
            infoRegistroTercero = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTercero");
    }

    public void contarRegistrosLovCuentas(int tipoListaLov) {
        if (tipoListaLov == 1) {
            infoRegistroCuenta = String.valueOf(filtrarLovCuentas.size());
        } else if (lovCuentas != null) {
            infoRegistroCuenta = String.valueOf(lovCuentas.size());
        } else {
            infoRegistroCuenta = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCuentaDebito");
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCuentaCredito");
    }

    public void contarRegistrosLovCC(int tipoListaLov) {
        if (tipoListaLov == 1) {
            infoRegistroCentroCosto = String.valueOf(filtrarLovCentrosCostos.size());
        } else if (lovCentrosCostos != null) {
            infoRegistroCentroCosto = String.valueOf(lovCentrosCostos.size());
        } else {
            infoRegistroCentroCosto = String.valueOf(0);
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCentroCostoDebito");
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCentroCostoCredito");
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

    public String getAltoScrollCortesProcesos() {
        altoScrollCortesProcesos = altoScrollComprobante;
        return altoScrollCortesProcesos;
    }

    public List<Terceros> getLovTerceros() {
        if (empleado.getEmpresa().getSecuencia() != null && lovTerceros.isEmpty()) {
            lovTerceros = administrarComprobantes.consultarLOVTerceros(empleado.getEmpresa().getSecuencia());
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
                    secFormula = listaSolucionesNodosEmpleado.get(index).getFormula().getSecuencia();
                    fechaDesde = formatoFecha.format(listaSolucionesNodosEmpleado.get(index).getFechadesde());
                    fechaHasta = formatoFecha.format(listaSolucionesNodosEmpleado.get(index).getFechahasta());
                    secEmpleado = listaSolucionesNodosEmpleado.get(index).getEmpleado().getSecuencia();
                    secProceso = listaSolucionesNodosEmpleado.get(index).getProceso().getSecuencia();
                }
            } else if (tablaActual == 1) {
                if (listaSolucionesNodosEmpleador != null && !listaSolucionesNodosEmpleador.isEmpty()) {
                    secFormula = listaSolucionesNodosEmpleador.get(index).getFormula().getSecuencia();
                    fechaDesde = formatoFecha.format(listaSolucionesNodosEmpleador.get(index).getFechadesde());
                    fechaHasta = formatoFecha.format(listaSolucionesNodosEmpleador.get(index).getFechahasta());
                    secEmpleado = listaSolucionesNodosEmpleador.get(index).getEmpleado().getSecuencia();
                    secProceso = listaSolucionesNodosEmpleador.get(index).getProceso().getSecuencia();
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

    public String getInfoRegistroProceso() {
        return infoRegistroProceso;
    }

    public void setInfoRegistroProceso(String infoRegistroProceso) {
        this.infoRegistroProceso = infoRegistroProceso;
    }

    public String getInfoRegistroTercero() {
        return infoRegistroTercero;
    }

    public void setInfoRegistroTercero(String infoRegistroTercero) {
        this.infoRegistroTercero = infoRegistroTercero;
    }

    public String getInfoRegistroCuenta() {
        return infoRegistroCuenta;
    }

    public void setInfoRegistroCuenta(String infoRegistroCuentaDebito) {
        this.infoRegistroCuenta = infoRegistroCuentaDebito;
    }

    public String getInfoRegistroCentroCosto() {
        return infoRegistroCentroCosto;
    }

    public void setInfoRegistroCentroCosto(String infoRegistroCentroCostoDebito) {
        this.infoRegistroCentroCosto = infoRegistroCentroCostoDebito;
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

    public String getInfoRegistroComp() {
        return infoRegistroComp;
    }

    public void setInfoRegistroComp(String infoRegistroComp) {
        this.infoRegistroComp = infoRegistroComp;
    }

    public String getInfoRegistroCP() {
        return infoRegistroCP;
    }

    public void setInfoRegistroCP(String infoRegistroCP) {
        this.infoRegistroCP = infoRegistroCP;
    }

    public String getInfoRegistroTEmpleado() {
        return infoRegistroTEmpleado;
    }

    public void setInfoRegistroTEmpleado(String infoRegistroTEmpleado) {
        this.infoRegistroTEmpleado = infoRegistroTEmpleado;
    }

    public String getInfoRegistroTEmpleador() {
        return infoRegistroTEmpleador;
    }

    public void setInfoRegistroTEmpleador(String infoRegistroTEmpleador) {
        this.infoRegistroTEmpleador = infoRegistroTEmpleador;
    }

}
