package Controlador;

import Entidades.DetallesFormulas;
import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import Entidades.SolucionesNodos;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarComprobantesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class ControlComprobantes implements Serializable {

    @EJB
    AdministrarComprobantesInterface administrarComprobantes;

    private List<Parametros> listaParametros;
    private Parametros parametroActual;
    private ParametrosEstructuras parametroEstructura;
    private List<Parametros> listaParametrosLOV;
    private List<Parametros> filtradoListaParametrosLOV;
    private Parametros parametroSeleccionado;
    //SOLUCIONES NODOS EMPLEADO
    private List<SolucionesNodos> listaSolucionesNodosEmpleado;
    private List<SolucionesNodos> filtradolistaSolucionesNodosEmpleado;
    private SolucionesNodos solucionNodoSeleccionada;
    private SolucionesNodos editarSolucionNodo;
    //SOLUCIONES NODOS EMPLEADOR
    private List<SolucionesNodos> listaSolucionesNodosEmpleador;
    private List<SolucionesNodos> filtradolistaSolucionesNodosEmpleador;
    private SolucionesNodos solucionNodoEmpleadorSeleccionada;
    //REGISTRO ACTUAL
    private int registroActual, tablaActual, index;
    //OTROS
    private boolean aceptar, mostrarTodos;
    private Locale locale = new Locale("es", "CO");
    private NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
    //SUBTOTALES
    private BigDecimal subtotalPago, subtotalDescuento, subtotalPasivo, subtotalGasto, neto;
    private String pago, descuento, pasivo, gasto, netoTotal;
    //FILTRADO
    private Column codigoSNE, descipcionSNE, unidadSNE, pagoSNE, descuentoSNE, terceroSNE, fechaHastaSNE, debitoSNE, centroCostoDSNE, creditoSNE, centroCostoCSNE, saldoSNE, fechaDesdeSNE, fechaPagoSNE, FechaModificacioSNE;
    private Column codigoSNER, descipcionSNER, unidadSNER, pasivoSNER, gastoSNER, terceroSNER, fechaHastaSNER, debitoSNER, centroCostoDSNER, creditoSNER, centroCostoCSNER, saldoSNER, fechaDesdeSNER, fechaPagoSNER, FechaModificacioSNER;
    private int bandera;
    private String altoScrollSolucionesNodosEmpleado, altoScrollSolucionesNodosEmpleador;
    //PARCIALES 
    private String parcialesSolucionNodos;
    //DETALLES FORMULAS
    private List<DetallesFormulas> listaDetallesFormulas;
    //FORMATO FECHAS
    private SimpleDateFormat formatoFecha;
    private boolean estadoBtnArriba, estadoBtnAbajo;
    //
    private String infoRegistroEmpleado, infoRegistroComprobante, infoRegistroComprobanteEmpleador, infoRegistroComprobanteEmpleado;
    private Parametros editarParametros;
    private int tipoLista, cualCelda;

    public ControlComprobantes() {
        registroActual = 0;
        aceptar = true;
        mostrarTodos = true;
        subtotalPago = new BigDecimal(0);
        subtotalDescuento = new BigDecimal(0);
        subtotalPasivo = new BigDecimal(0);
        subtotalGasto = new BigDecimal(0);
        bandera = 0;
        altoScrollSolucionesNodosEmpleado = "95";
        altoScrollSolucionesNodosEmpleador = "95";
        listaDetallesFormulas = null;
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        estadoBtnArriba = false;
        estadoBtnAbajo = false;
        parametroActual = null;
        pasivo = "0";
        gasto = "0";
        pago = "0";
        descuento = "0";
        netoTotal = "0";
        listaParametrosLOV = new ArrayList<Parametros>();
        editarParametros = new Parametros();
        tipoLista = 0;
        solucionNodoSeleccionada = null;
        solucionNodoEmpleadorSeleccionada = null;
        editarSolucionNodo = new SolucionesNodos();
        registroActual = 0;
        listaSolucionesNodosEmpleado = null;
        listaSolucionesNodosEmpleador = null;
        listaParametros = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarComprobantes.obtenerConexion(ses.getId());

        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void refrescar() {
        solucionNodoSeleccionada = null;
        solucionNodoEmpleadorSeleccionada = null;
        listaParametros = null;
        listaSolucionesNodosEmpleado = null;
        listaSolucionesNodosEmpleador = null;
        getListaSolucionesNodosEmpleado();
        getListaSolucionesNodosEmpleador();
        getListaParametros();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosSolucionesNodosEmpleado");
        context.update("form:datosSolucionesNodosEmpleadoR");
    }

    public void anteriorEmpleado() {
        if (registroActual > 0) {
            registroActual--;
            parametroActual = listaParametros.get(registroActual);
            listaSolucionesNodosEmpleado = null;
            listaSolucionesNodosEmpleador = null;
            getListaSolucionesNodosEmpleado();
            getListaSolucionesNodosEmpleador();
            if (registroActual == 0) {
                estadoBtnArriba = true;
            }
            if (registroActual < (listaParametros.size() - 1)) {
                estadoBtnAbajo = false;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:panelInf");
            context.update("form:datosSolucionesNodosEmpleado");
            context.update("form:datosSolucionesNodosEmpleador");
            context.update("form:btnArriba");
            context.update("form:btnAbajo");
        }
    }

    public void siguienteEmpleado() {
        if (registroActual < (listaParametros.size() - 1)) {
            registroActual++;
            parametroActual = listaParametros.get(registroActual);
            listaSolucionesNodosEmpleado = null;
            listaSolucionesNodosEmpleador = null;
            getListaSolucionesNodosEmpleado();
            getListaSolucionesNodosEmpleador();
            if (registroActual > 0) {
                estadoBtnArriba = false;
            }
            if (registroActual == (listaParametros.size() - 1)) {
                estadoBtnAbajo = true;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:panelInf");
            context.update("form:datosSolucionesNodosEmpleado");
            context.update("form:datosSolucionesNodosEmpleador");
            context.update("form:btnArriba");
            context.update("form:btnAbajo");
        }
    }

    public void seleccionarEmpleado() {
        listaParametros.clear();
        listaParametros.add(parametroSeleccionado);
        parametroActual = parametroSeleccionado;
        registroActual = 0;
        filtradoListaParametrosLOV = null;
        parametroSeleccionado = null;
        aceptar = true;
        mostrarTodos = false;
        listaSolucionesNodosEmpleado = null;
        listaSolucionesNodosEmpleador = null;
        getListaSolucionesNodosEmpleado();
        getListaSolucionesNodosEmpleador();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:panelInf");
        context.update("form:datosSolucionesNodosEmpleado");
        context.update("form:datosSolucionesNodosEmpleador");

        context.update("formularioDialogos:buscarEmpleadoDialogo");
        context.update("formularioDialogos:lovEmpleados");
        context.update("formularioDialogos:aceptarP");
        context.reset("formularioDialogos:lovEmpleados:globalFilter");
        context.execute("lovEmpleados.clearFilters()");
        context.execute("buscarEmpleadoDialogo.hide()");
    }

    public void cancelarSeleccionEmpleado() {
        filtradoListaParametrosLOV = null;
        parametroSeleccionado = null;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpleados:globalFilter");
        context.execute("lovEmpleados.clearFilters()");
        context.execute("buscarEmpleadoDialogo.hide()");
    }

    public void mostarTodosEmpleados() {
        registroActual = 0;
        listaParametros = null;
        parametroActual = null;
        listaParametrosLOV = null;
        getParametroActual();
        listaSolucionesNodosEmpleado = null;
        listaSolucionesNodosEmpleador = null;
        getListaSolucionesNodosEmpleado();
        getListaSolucionesNodosEmpleador();
        modificarInfoRegistroComprobanteEmpleado(listaSolucionesNodosEmpleado.size());
        modificarInfoRegistroComprobanteEmpleador(listaSolucionesNodosEmpleador.size());
        mostrarTodos = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:panelInf");
        context.update("form:datosSolucionesNodosEmpleado");
        context.update("form:datosSolucionesNodosEmpleador");
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        if (bandera == 0) {
            //SOLUCIONES NODOS EMPLEADO
            codigoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:codigoSNE");
            codigoSNE.setFilterStyle("width: 85%");
            descipcionSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:descipcionSNE");
            descipcionSNE.setFilterStyle("width: 85%");
            unidadSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:unidadSNE");
            unidadSNE.setFilterStyle("width: 85%");
            pagoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:pagoSNE");
            pagoSNE.setFilterStyle("width: 85%");
            descuentoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:descuentoSNE");
            descuentoSNE.setFilterStyle("width: 85%");
            terceroSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:terceroSNE");
            terceroSNE.setFilterStyle("width: 85%");
            fechaHastaSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaHastaSNE");
            fechaHastaSNE.setFilterStyle("width: 85%");
            debitoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:debitoSNE");
            debitoSNE.setFilterStyle("width: 85%");
            centroCostoDSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:centroCostoDSNE");
            centroCostoDSNE.setFilterStyle("width: 85%");
            creditoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:creditoSNE");
            creditoSNE.setFilterStyle("width: 85%");
            centroCostoCSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:centroCostoCSNE");
            centroCostoCSNE.setFilterStyle("width: 85%");
            saldoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:saldoSNE");
            saldoSNE.setFilterStyle("width: 85%");
            fechaDesdeSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaDesdeSNE");
            fechaDesdeSNE.setFilterStyle("width: 85%");
            fechaPagoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaPagoSNE");
            fechaPagoSNE.setFilterStyle("width: 85%");
            FechaModificacioSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:FechaModificacioSNE");
            FechaModificacioSNE.setFilterStyle("width: 85%");

            //SOLUCIONES NODOS EMPLEADOR
            codigoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:codigoSNER");
            codigoSNER.setFilterStyle("width: 85%");
            descipcionSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:descipcionSNER");
            descipcionSNER.setFilterStyle("width: 85%");
            unidadSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:unidadSNER");
            unidadSNER.setFilterStyle("width: 85%");
            pasivoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:pasivoSNER");
            pasivoSNER.setFilterStyle("width: 85%");
            gastoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:gastoSNER");
            gastoSNER.setFilterStyle("width: 85%");
            terceroSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:terceroSNER");
            terceroSNER.setFilterStyle("width: 85%");
            fechaHastaSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaHastaSNER");
            fechaHastaSNER.setFilterStyle("width: 85%");
            debitoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:debitoSNER");
            debitoSNER.setFilterStyle("width: 85%");
            centroCostoDSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:centroCostoDSNER");
            centroCostoDSNER.setFilterStyle("width: 85%");
            creditoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:creditoSNER");
            creditoSNER.setFilterStyle("width: 85%");
            centroCostoCSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:centroCostoCSNER");
            centroCostoCSNER.setFilterStyle("width: 85%");
            saldoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:saldoSNER");
            saldoSNER.setFilterStyle("width: 85%");
            fechaDesdeSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaDesdeSNER");
            fechaDesdeSNER.setFilterStyle("width: 85%");
            fechaPagoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaPagoSNER");
            fechaPagoSNER.setFilterStyle("width: 85%");
            FechaModificacioSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:FechaModificacioSNER");
            FechaModificacioSNER.setFilterStyle("width: 85%");

            altoScrollSolucionesNodosEmpleado = "75";
            altoScrollSolucionesNodosEmpleador = "75";
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosSolucionesNodosEmpleado");
            context.update("form:datosSolucionesNodosEmpleador");
            bandera = 1;

        } else if (bandera == 1) {
            //SOLUCIONES NODOS EMPLEADO
            codigoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:codigoSNE");
            codigoSNE.setFilterStyle("display: none; visibility: hidden;");
            descipcionSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:descipcionSNE");
            descipcionSNE.setFilterStyle("display: none; visibility: hidden;");
            unidadSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:unidadSNE");
            unidadSNE.setFilterStyle("display: none; visibility: hidden;");
            pagoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:pagoSNE");
            pagoSNE.setFilterStyle("display: none; visibility: hidden;");
            descuentoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:descuentoSNE");
            descuentoSNE.setFilterStyle("display: none; visibility: hidden;");
            terceroSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:terceroSNE");
            terceroSNE.setFilterStyle("display: none; visibility: hidden;");
            fechaHastaSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaHastaSNE");
            fechaHastaSNE.setFilterStyle("display: none; visibility: hidden;");
            debitoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:debitoSNE");
            debitoSNE.setFilterStyle("display: none; visibility: hidden;");
            centroCostoDSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:centroCostoDSNE");
            centroCostoDSNE.setFilterStyle("display: none; visibility: hidden;");
            creditoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:creditoSNE");
            creditoSNE.setFilterStyle("display: none; visibility: hidden;");
            centroCostoCSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:centroCostoCSNE");
            centroCostoCSNE.setFilterStyle("display: none; visibility: hidden;");
            saldoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:saldoSNE");
            saldoSNE.setFilterStyle("display: none; visibility: hidden;");
            fechaDesdeSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaDesdeSNE");
            fechaDesdeSNE.setFilterStyle("display: none; visibility: hidden;");
            fechaPagoSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:fechaPagoSNE");
            fechaPagoSNE.setFilterStyle("display: none; visibility: hidden;");
            FechaModificacioSNE = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleado:FechaModificacioSNE");
            FechaModificacioSNE.setFilterStyle("display: none; visibility: hidden;");

            //SOLUCIONES NODOS EMPLEADOR
            codigoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:codigoSNER");
            codigoSNER.setFilterStyle("display: none; visibility: hidden;");
            descipcionSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:descipcionSNER");
            descipcionSNER.setFilterStyle("display: none; visibility: hidden;");
            unidadSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:unidadSNER");
            unidadSNER.setFilterStyle("display: none; visibility: hidden;");
            pasivoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:pasivoSNER");
            pasivoSNER.setFilterStyle("display: none; visibility: hidden;");
            gastoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:gastoSNER");
            gastoSNER.setFilterStyle("display: none; visibility: hidden;");
            terceroSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:terceroSNER");
            terceroSNER.setFilterStyle("display: none; visibility: hidden;");
            fechaHastaSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaHastaSNER");
            fechaHastaSNER.setFilterStyle("display: none; visibility: hidden;");
            debitoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:debitoSNER");
            debitoSNER.setFilterStyle("display: none; visibility: hidden;");
            centroCostoDSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:centroCostoDSNER");
            centroCostoDSNER.setFilterStyle("display: none; visibility: hidden;");
            creditoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:creditoSNER");
            creditoSNER.setFilterStyle("display: none; visibility: hidden;");
            centroCostoCSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:centroCostoCSNER");
            centroCostoCSNER.setFilterStyle("display: none; visibility: hidden;");
            saldoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:saldoSNER");
            saldoSNER.setFilterStyle("display: none; visibility: hidden;");
            fechaDesdeSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaDesdeSNER");
            fechaDesdeSNER.setFilterStyle("display: none; visibility: hidden;");
            fechaPagoSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:fechaPagoSNER");
            fechaPagoSNER.setFilterStyle("display: none; visibility: hidden;");
            FechaModificacioSNER = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionesNodosEmpleador:FechaModificacioSNER");
            FechaModificacioSNER.setFilterStyle("display: none; visibility: hidden;");

            altoScrollSolucionesNodosEmpleado = "95";
            altoScrollSolucionesNodosEmpleador = "95";
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosSolucionesNodosEmpleado");
            context.update("form:datosSolucionesNodosEmpleador");
            bandera = 0;
            filtradolistaSolucionesNodosEmpleado = null;
            filtradolistaSolucionesNodosEmpleador = null;
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla;
        Exporter exporter = new ExportarPDFTablasAnchas();
        FacesContext context = FacesContext.getCurrentInstance();
        tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSolucionesNodosEmpleadoExportar");
        exporter.export(context, tabla, "SolucionesNodosEmpleadoPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSolucionesNodosEmpleadorExportar");
        exporter.export(context, tabla, "SolucionesNodosEmpleadorPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla;
        Exporter exporter = new ExportarXLS();
        FacesContext context = FacesContext.getCurrentInstance();
        tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSolucionesNodosEmpleadoExportar");
        exporter.export(context, tabla, "SolucionesNodosEmpleadoXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //PARCIALES CONCEPTO
    public void parcialSolucionNodo(SolucionesNodos solucionNodo, int celda) {
        solucionNodoSeleccionada = solucionNodo;
        cualCelda = celda;
        solucionNodoSeleccionada.getSecuencia();
        if (cualCelda == 0) {
            parcialesSolucionNodos = solucionNodoSeleccionada.getParciales();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:parcialesConcepto");
        context.execute("parcialesConcepto.show();");
    }

    public void parcialSolucionNodoEmpleador(SolucionesNodos solucionNodoEmpleador, int celda) {
        solucionNodoEmpleadorSeleccionada = solucionNodoEmpleador;
        cualCelda = celda;
        solucionNodoEmpleadorSeleccionada.getSecuencia();
        if (cualCelda == 0) {
            parcialesSolucionNodos = solucionNodoEmpleadorSeleccionada.getParciales();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:parcialesConcepto");
        context.execute("parcialesConcepto.show();");
    }

    public void cambiarIndiceEmpleador(SolucionesNodos solucionNodoEmpleador, int celda) {
        solucionNodoEmpleadorSeleccionada = solucionNodoEmpleador;
        cualCelda = celda;
        solucionNodoEmpleadorSeleccionada.getSecuencia();
        if (cualCelda == 1) {
            solucionNodoEmpleadorSeleccionada.getConcepto().getCodigo();
        } else if (cualCelda == 2) {
            solucionNodoEmpleadorSeleccionada.getConcepto().getDescripcion();
        } else if (cualCelda == 3) {
            solucionNodoEmpleadorSeleccionada.getUnidades();
        } else if (cualCelda == 4) {
            solucionNodoEmpleadorSeleccionada.getPasivo();
        } else if (cualCelda == 5) {
            solucionNodoEmpleadorSeleccionada.getGasto();
        } else if (cualCelda == 6) {
            solucionNodoEmpleadorSeleccionada.getNit().getNombre();
        } else if (cualCelda == 7) {
            solucionNodoEmpleadorSeleccionada.getFechahasta();
        } else if (cualCelda == 8) {
            solucionNodoEmpleadorSeleccionada.getCuentad().getCodigo();
        } else if (cualCelda == 9) {
            solucionNodoEmpleadorSeleccionada.getCentrocostod().getNombre();
        } else if (cualCelda == 10) {
            solucionNodoEmpleadorSeleccionada.getCuentac().getCodigo();
        } else if (cualCelda == 11) {
            solucionNodoEmpleadorSeleccionada.getCentrocostoc().getNombre();
        } else if (cualCelda == 12) {
            solucionNodoEmpleadorSeleccionada.getSaldo();
        } else if (cualCelda == 13) {
            solucionNodoEmpleadorSeleccionada.getFechadesde();
        } else if (cualCelda == 14) {
            solucionNodoEmpleadorSeleccionada.getFechapago();
        } else if (cualCelda == 15) {
            solucionNodoEmpleadorSeleccionada.getUltimamodificacion();
        }
    }

    public void cambiarIndice(SolucionesNodos solucionNodo, int celda) {
        solucionNodoSeleccionada = solucionNodo;
        cualCelda = celda;
        solucionNodoSeleccionada.getSecuencia();
        if (cualCelda == 1) {
            solucionNodoSeleccionada.getConcepto().getCodigo();
        } else if (cualCelda == 2) {
            solucionNodoSeleccionada.getConcepto().getDescripcion();
        } else if (cualCelda == 3) {
            solucionNodoSeleccionada.getUnidades();
        } else if (cualCelda == 4) {
            solucionNodoSeleccionada.getPago();
        } else if (cualCelda == 5) {
            solucionNodoSeleccionada.getDescuento();
        } else if (cualCelda == 6) {
            solucionNodoSeleccionada.getNit().getNombre();
        } else if (cualCelda == 7) {
            solucionNodoSeleccionada.getFechahasta();
        } else if (cualCelda == 8) {
            solucionNodoSeleccionada.getCuentad().getCodigo();
        } else if (cualCelda == 9) {
            solucionNodoSeleccionada.getCentrocostod().getNombre();
        } else if (cualCelda == 10) {
            solucionNodoSeleccionada.getCuentac().getCodigo();
        } else if (cualCelda == 11) {
            solucionNodoSeleccionada.getCentrocostoc().getNombre();
        } else if (cualCelda == 12) {
            solucionNodoSeleccionada.getSaldo();
        } else if (cualCelda == 13) {
            solucionNodoSeleccionada.getFechadesde();
        } else if (cualCelda == 14) {
            solucionNodoSeleccionada.getFechapago();
        } else if (cualCelda == 15) {
            solucionNodoSeleccionada.getUltimamodificacion();
        }
    }

    public void verDetallesFormula() {
        listaDetallesFormulas = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:detallesFormulas");
        context.execute("detallesFormulas.show();");
    }

    public void salir() {
        parametroActual = null;
        listaSolucionesNodosEmpleado = null;
        listaSolucionesNodosEmpleador = null;
        parametroEstructura = null;
        registroActual = 0;
        estadoBtnArriba = false;
        estadoBtnAbajo = false;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void modificarInfoRegistroEmpleado(int valor) {
        infoRegistroEmpleado = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleado");
    }

    public void modificarInfoRegistroComprobanteEmpleador(int valor) {
        infoRegistroComprobanteEmpleador = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroComprobanteEmpleador");
    }

    public void modificarInfoRegistroComprobanteEmpleado(int valor) {
        infoRegistroComprobanteEmpleado = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroComprobanteEmpleado");
    }

    public void eventoFiltrarEmpleado() {
        modificarInfoRegistroEmpleado(filtradoListaParametrosLOV.size());
    }

    public void eventoFiltrarComprobanteEmpleado() {
        modificarInfoRegistroComprobanteEmpleado(filtradolistaSolucionesNodosEmpleado.size());
    }

    public void eventoFiltrarComprobanteEmpleador() {
        modificarInfoRegistroComprobanteEmpleador(filtradolistaSolucionesNodosEmpleador.size());
    }

    public void editarCelda() {
        if (tablaActual == 0) {
            if (solucionNodoSeleccionada != null) {
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 1) {
                    context.update("formularioDialogos:editarCodEmpleado");
                    context.execute("editarCodEmpleado.show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    context.update("formularioDialogos:editarDescConcepto");
                    context.execute("editarDescConcepto.show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    context.update("formularioDialogos:editarUnd");
                    context.execute("editarUnd.show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    context.update("formularioDialogos:editarPago");
                    context.execute("editarPago.show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    context.update("formularioDialogos:editarDescuento");
                    context.execute("editarDescuento.show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    context.update("formularioDialogos:editarTercero");
                    context.execute("editarTercero.show()");
                    cualCelda = -1;
                } else if (cualCelda == 7) {
                    context.update("formularioDialogos:editarFechaHasta");
                    context.execute("editarFechaHasta.show()");
                    cualCelda = -1;
                } else if (cualCelda == 8) {
                    context.update("formularioDialogos:editarDebito");
                    context.execute("editarDebito.show()");
                    cualCelda = -1;
                } else if (cualCelda == 9) {
                    context.update("formularioDialogos:editarCCDebito");
                    context.execute("editarCCDebito.show()");
                    cualCelda = -1;
                } else if (cualCelda == 10) {
                    context.update("formularioDialogos:editarCredito");
                    context.execute("editarCredito.show()");
                    cualCelda = -1;
                } else if (cualCelda == 11) {
                    context.update("formularioDialogos:editarCCCredito");
                    context.execute("editarCCCredito.show()");
                    cualCelda = -1;
                } else if (cualCelda == 12) {
                    context.update("formularioDialogos:editarSaldo");
                    context.execute("editarSaldo.show()");
                    cualCelda = -1;
                } else if (cualCelda == 13) {
                    context.update("formularioDialogos:editarFechaDesde");
                    context.execute("editarFechaDesde.show()");
                    cualCelda = -1;
                } else if (cualCelda == 14) {
                    context.update("formularioDialogos:editarFechaPago");
                    context.execute("editarFechaPago.show()");
                    cualCelda = -1;
                } else if (cualCelda == 15) {
                    context.update("formularioDialogos:editarFechaModificacion");
                    context.execute("formularioDialogos:editarFechaModificacion.show()");
                    cualCelda = -1;
                } // } 
            } else {
                RequestContext.getCurrentInstance().execute("formularioDialogos:seleccionarRegistro.show()");
            }
        } else if (tablaActual == 1) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 1) {
                context.update("formularioDialogos:editarCodEmpleadoEmpleador");
                context.execute("editarCodEmpleadoEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editarDescConceptoEmpleador");
                context.execute("editarDescConceptoEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:editarUndEmpleador");
                context.execute("editarUndEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                context.update("formularioDialogos:editarPasivo");
                context.execute("editarPasivo.show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                context.update("formularioDialogos:editarGasto");
                context.execute("editarGasto.show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                context.update("formularioDialogos:editarTerceroEmpleador");
                context.execute("editarTerceroEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                context.update("formularioDialogos:editarFechaHastaEmpleador");
                context.execute("editarFechaHastaEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                context.update("formularioDialogos:editarDebitoEmpleador");
                context.execute("editarDebitoEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 9) {
                context.update("formularioDialogos:editarCCDebitoEmpleador");
                context.execute("editarCCDebitoEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 10) {
                context.update("formularioDialogos:editarCreditoEmpleador");
                context.execute("editarCreditoEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 11) {
                context.update("formularioDialogos:editarCCCreditoEmpleador");
                context.execute("editarCCCreditoEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 12) {
                context.update("formularioDialogos:editarSaldoEmpleador");
                context.execute("editarSaldoEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 13) {
                context.update("formularioDialogos:editarFechaDesdeEmpleador");
                context.execute("editarFechaDesdeEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 14) {
                context.update("formularioDialogos:editarFechaPagoEmpleador");
                context.execute("editarFechaPagoEmpleador.show()");
                cualCelda = -1;
            } else if (cualCelda == 15) {
                context.update("formularioDialogos:editarFechaModificacionEmpleador");
                context.execute("formularioDialogos:editarFechaModificacionEmpleador.show()");
                cualCelda = -1;
            } // } 
        } else {
            RequestContext.getCurrentInstance().execute("formularioDialogos:seleccionarRegistro.show()");
        }
    }

    public void posicionOtro() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String celda = map.get("celda"); // name attribute of node 
        String registro = map.get("registro"); // type attribute of node 
        int indice = Integer.parseInt(registro);
        int columna = Integer.parseInt(celda);
        solucionNodoSeleccionada = listaSolucionesNodosEmpleado.get(indice);
        cambiarIndice(solucionNodoSeleccionada, columna);
    }

    public void posicionOtroEmpleador() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> mapEm = context.getExternalContext().getRequestParameterMap();
        String celda = mapEm.get("celda"); // name attribute of node 
        String registro = mapEm.get("registro"); // type attribute of node 
        int indice = Integer.parseInt(registro);
        int columna = Integer.parseInt(celda);
        solucionNodoEmpleadorSeleccionada = listaSolucionesNodosEmpleador.get(indice);
        cambiarIndice(solucionNodoEmpleadorSeleccionada, columna);

    }

    //GETTER AND SETTER
    public List<Parametros> getListaParametros() {
        listaParametros = administrarComprobantes.consultarParametrosComprobantesActualUsuario();
        if (listaParametros == null || listaParametros.isEmpty()) {
            estadoBtnArriba = true;
            estadoBtnAbajo = true;
        } else {
            estadoBtnArriba = false;
            estadoBtnAbajo = false;
        }
        return listaParametros;
    }

    public void setListaParametros(List<Parametros> listaParametros) {
        this.listaParametros = listaParametros;
    }

    public Parametros getParametroActual() {
        if (parametroActual == null) {
            getListaParametros();
            if (listaParametros != null && !listaParametros.isEmpty()) {
                parametroActual = listaParametros.get(registroActual);
            }
        }
        return parametroActual;
    }

    public void setParametroActual(Parametros parametroActual) {
        this.parametroActual = parametroActual;
    }

    public ParametrosEstructuras getParametroEstructura() {
        if (parametroEstructura == null) {
            parametroEstructura = administrarComprobantes.consultarParametroEstructuraActualUsuario();
        }
        return parametroEstructura;
    }

    public void setParametroEstructura(ParametrosEstructuras parametroEstructura) {
        this.parametroEstructura = parametroEstructura;
    }

    public List<Parametros> getListaParametrosLOV() {
        if (listaParametrosLOV == null || listaParametrosLOV.isEmpty()) {
            listaParametrosLOV = administrarComprobantes.consultarParametrosComprobantesActualUsuario();
            if (listaParametrosLOV != null) {
                modificarInfoRegistroEmpleado(listaParametrosLOV.size());
            } else {
                modificarInfoRegistroEmpleado(0);
            }
        }

        return listaParametrosLOV;
    }

    public void setListaParametrosLOV(List<Parametros> listaParametrosLOV) {
        this.listaParametrosLOV = listaParametrosLOV;
    }

    public Parametros getParametroSeleccionado() {
        return parametroSeleccionado;
    }

    public void setParametroSeleccionado(Parametros parametroSeleccionado) {
        this.parametroSeleccionado = parametroSeleccionado;
    }

    public List<Parametros> getFiltradoListaParametrosLOV() {
        return filtradoListaParametrosLOV;
    }

    public void setFiltradoListaParametrosLOV(List<Parametros> filtradoListaParametrosLOV) {
        this.filtradoListaParametrosLOV = filtradoListaParametrosLOV;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public boolean isMostrarTodos() {
        return mostrarTodos;
    }

    public List<SolucionesNodos> getListaSolucionesNodosEmpleado() {
        if (parametroActual != null) {
            listaSolucionesNodosEmpleado = administrarComprobantes.consultarSolucionesNodosEmpleado(parametroActual.getEmpleado().getSecuencia());
            if (listaSolucionesNodosEmpleado != null) {
                subtotalPago = new BigDecimal(0);
                subtotalDescuento = new BigDecimal(0);
                for (int i = 0; i < listaSolucionesNodosEmpleado.size(); i++) {
                    if (listaSolucionesNodosEmpleado.get(i).getTipo().equals("PAGO")) {
                        subtotalPago = subtotalPago.add(listaSolucionesNodosEmpleado.get(i).getValor());
                    } else {
                        subtotalDescuento = subtotalDescuento.add(listaSolucionesNodosEmpleado.get(i).getValor());
                    }
                }
                neto = subtotalPago.subtract(subtotalDescuento);
                pago = nf.format(subtotalPago);
                descuento = nf.format(subtotalDescuento);
                netoTotal = nf.format(neto);
            }
            modificarInfoRegistroComprobanteEmpleado(listaSolucionesNodosEmpleado.size());
        } else {
            modificarInfoRegistroComprobanteEmpleado(0);
        }
        return listaSolucionesNodosEmpleado;
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

    public List<SolucionesNodos> getListaSolucionesNodosEmpleador() {
        if (parametroActual != null) {
            if (parametroActual.getEmpleado().getSecuencia() != null) {
                listaSolucionesNodosEmpleador = administrarComprobantes.consultarSolucionesNodosEmpleador(parametroActual.getEmpleado().getSecuencia());
                if (listaSolucionesNodosEmpleador != null) {
                    subtotalPasivo = new BigDecimal(0);
                    subtotalGasto = new BigDecimal(0);
                    for (int i = 0; i < listaSolucionesNodosEmpleador.size(); i++) {
                        if (listaSolucionesNodosEmpleador.get(i).getTipo().equals("PASIVO")) {
                            subtotalPasivo = subtotalPasivo.add(listaSolucionesNodosEmpleador.get(i).getValor());
                        } else if (listaSolucionesNodosEmpleador.get(i).getTipo().equals("GASTO")) {
                            subtotalGasto = subtotalGasto.add(listaSolucionesNodosEmpleador.get(i).getValor());
                        }
                    }
                    pasivo = nf.format(subtotalPasivo);
                    gasto = nf.format(subtotalGasto);
                }
            }
            modificarInfoRegistroComprobanteEmpleador(listaSolucionesNodosEmpleador.size());
        } else {
            modificarInfoRegistroComprobanteEmpleador(0);
        }

        return listaSolucionesNodosEmpleador;
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

    public String getPago() {
        return pago;
    }

    public String getDescuento() {
        return descuento;
    }

    public String getPasivo() {
        return pasivo;
    }

    public String getGasto() {
        return gasto;
    }

    public String getNetoTotal() {
        return netoTotal;
    }

    public String getAltoScrollSolucionesNodosEmpleado() {
        return altoScrollSolucionesNodosEmpleado;
    }

    public String getAltoScrollSolucionesNodosEmpleador() {
        return altoScrollSolucionesNodosEmpleador;
    }

    public String getParcialesSolucionNodos() {
        return parcialesSolucionNodos;
    }

    public List<DetallesFormulas> getListaDetallesFormulas() {
        if (listaDetallesFormulas == null) {
            BigInteger secEmpleado = null, secProceso = null, secHistoriaFormula, secFormula = null;
            String fechaDesde = null, fechaHasta = null;
            if (tablaActual == 0) {
                if (listaSolucionesNodosEmpleado != null && !listaSolucionesNodosEmpleado.isEmpty()) {
                    secFormula = listaSolucionesNodosEmpleado.get(index).getFormula().getSecuencia();//
                    fechaDesde = formatoFecha.format(listaSolucionesNodosEmpleado.get(index).getFechadesde()); //
                    fechaHasta = formatoFecha.format(listaSolucionesNodosEmpleado.get(index).getFechahasta()); //
                    secEmpleado = listaSolucionesNodosEmpleado.get(index).getEmpleado().getSecuencia(); //
                    secProceso = listaSolucionesNodosEmpleado.get(index).getProceso().getSecuencia();//
                }
            } else if (tablaActual == 1) {
                if (listaSolucionesNodosEmpleador != null && !listaSolucionesNodosEmpleador.isEmpty()) {
                    secFormula = listaSolucionesNodosEmpleador.get(index).getFormula().getSecuencia();  //
                    fechaDesde = formatoFecha.format(listaSolucionesNodosEmpleador.get(index).getFechadesde()); // 
                    fechaHasta = formatoFecha.format(listaSolucionesNodosEmpleador.get(index).getFechahasta()); //
                    secEmpleado = listaSolucionesNodosEmpleador.get(index).getEmpleado().getSecuencia(); //
                    secProceso = listaSolucionesNodosEmpleador.get(index).getProceso().getSecuencia(); //
                }
            }
            if (secFormula != null && fechaDesde != null) {
                secHistoriaFormula = administrarComprobantes.consultarHistoriaFormulaFormula(secFormula, fechaDesde);
                listaDetallesFormulas = administrarComprobantes.consultarDetallesFormulasEmpleado(secEmpleado, fechaDesde, fechaHasta, secProceso, secHistoriaFormula);
            }
        }
        return listaDetallesFormulas;
    }

    public void setListaDetallesFormulas(List<DetallesFormulas> listaDetallesFormulas) {
        this.listaDetallesFormulas = listaDetallesFormulas;
    }

    public boolean isEstadoBtnArriba() {
        return estadoBtnArriba;
    }

    public boolean isEstadoBtnAbajo() {
        return estadoBtnAbajo;
    }

    public String getInfoRegistroEmpleado() {
        return infoRegistroEmpleado;
    }

    public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
        this.infoRegistroEmpleado = infoRegistroEmpleado;
    }

    public String getInfoRegistroComprobante() {
        if (listaParametros == null || listaParametros.isEmpty()) {
            infoRegistroComprobante = "Reg. " + "0" + " de " + listaParametros.size();
        } else {
            infoRegistroComprobante = "Reg. " + (registroActual+1) + " de " + listaParametros.size();
        }
        return infoRegistroComprobante;
    }

    public void setInfoRegistroComprobante(String infoRegistroComprobante) {
        this.infoRegistroComprobante = infoRegistroComprobante;
    }

    public Parametros getEditarParametros() {
        return editarParametros;
    }

    public void setEditarParametros(Parametros editarParametros) {
        this.editarParametros = editarParametros;
    }

    public SolucionesNodos getSolucionNodoSeleccionada() {
        return solucionNodoSeleccionada;
    }

    public void setSolucionNodoSeleccionada(SolucionesNodos solucionNodoSeleccionada) {
        this.solucionNodoSeleccionada = solucionNodoSeleccionada;
    }

    public SolucionesNodos getEditarSolucionNodo() {
        return editarSolucionNodo;
    }

    public void setEditarSolucionNodo(SolucionesNodos editarSolucionNodo) {
        this.editarSolucionNodo = editarSolucionNodo;
    }

    public SolucionesNodos getSolucionNodoEmpleadorSeleccionada() {
        return solucionNodoEmpleadorSeleccionada;
    }

    public void setSolucionNodoEmpleadorSeleccionada(SolucionesNodos solucionNodoEmpleadorSeleccionada) {
        this.solucionNodoEmpleadorSeleccionada = solucionNodoEmpleadorSeleccionada;
    }

    public String getInfoRegistroComprobanteEmpleador() {
        return infoRegistroComprobanteEmpleador;
    }

    public void setInfoRegistroComprobanteEmpleador(String infoRegistroComprobanteEmpleador) {
        this.infoRegistroComprobanteEmpleador = infoRegistroComprobanteEmpleador;
    }

    public String getInfoRegistroComprobanteEmpleado() {
        return infoRegistroComprobanteEmpleado;
    }

    public void setInfoRegistroComprobanteEmpleado(String infoRegistroComprobanteEmpleado) {
        this.infoRegistroComprobanteEmpleado = infoRegistroComprobanteEmpleado;
    }

}
