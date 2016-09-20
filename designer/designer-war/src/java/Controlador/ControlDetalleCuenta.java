/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.Cuentas;
import Entidades.VigenciasCuentas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarDetalleCuentaInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
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

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlDetalleCuenta implements Serializable {

    @EJB
    AdministrarDetalleCuentaInterface administrarDetalleCuenta;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Cuentas Credito
    private List<VigenciasCuentas> listCuentasCredito;
    private List<VigenciasCuentas> filtrarListCuentasCredito;
    private VigenciasCuentas cuentaCreditoTablaSeleccionada;
    //Vigencias Cuentas Debito
    private List<VigenciasCuentas> listCuentasDebito;
    private List<VigenciasCuentas> filtrarListCuentasDebito;
    private VigenciasCuentas cuentaDebitoTablaSeleccionada;
    //Cuentas
    private Cuentas cuentaActual;
    //Activo/Desactivo Crtl + F11
    private int banderaCredito;
    //Activo/Desactivo VP Crtl + F11
    private int banderaDebito;
    //Columnas Tabla VL
    private Column creditoFechaInicial, creditoFechaFinal, creditoCodigo, creditoDescripcion, creditoDescripcionCC, creditoCC, creditoTipo;
    //Columnas Tabla VP
    private Column debitoFechaInicial, debitoFechaFinal, debitoCodigo, debitoDescripcion, debitoDescripcionCC, debitoCC, debitoTipo;
    private boolean aceptar;
    private int indexCredito;
    private boolean guardado, guardarOk;
    private VigenciasCuentas editarCredito;
    private VigenciasCuentas editarDebito;
    private int cualCeldaCredito, tipoListaCredito;
    private boolean cambioEditor, aceptarEditar;
    private int indexDebito;
    private int cualCeldaDebito, tipoListaDebito;
    private int indexAuxCredito, indexAuxDebito;
    private String nombreXML;
    private String nombreTabla;
    private BigInteger secRegistroCredito;
    private BigInteger backUpSecRegistroCredito;
    private BigInteger secRegistroDebito;
    private BigInteger backUpSecRegistroDebito;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    //
    private String altoTablaCredito, altoTablaDebito;

    public ControlDetalleCuenta() {
        altoTablaDebito = "105";
        altoTablaCredito = "120";
        nombreTablaRastro = "";
        backUp = null;
        listCuentasDebito = null;
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        secRegistroCredito = null;
        backUpSecRegistroCredito = null;
        secRegistroDebito = null;
        backUpSecRegistroDebito = null;
        listCuentasCredito = null;
        cuentaActual = new Cuentas();
        //Otros
        aceptar = true;
        editarCredito = new VigenciasCuentas();
        editarDebito = new VigenciasCuentas();
        cambioEditor = false;
        aceptarEditar = true;
        cualCeldaCredito = -1;
        tipoListaCredito = 0;
        tipoListaDebito = 0;
        //guardar
        guardado = true;
        indexCredito = -1;
        banderaCredito = 0;
        banderaDebito = 0;
        indexDebito = -1;
        cualCeldaDebito = -1;
        nombreTabla = ":formExportarCredito:datosCreditoExportar";
        nombreXML = "CuentrasCreditoXML";
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarDetalleCuenta.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirCuenta(BigInteger cuenta) {
        listCuentasCredito = null;
        listCuentasDebito = null;
        cuentaActual = administrarDetalleCuenta.mostrarCuenta(cuenta);
    }

    public void posicionCredito() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceCredito(indice, columna);
    }

    public void posicionDebito() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceDebito(indice, columna);
    }

    public void cambiarIndiceCredito(int indice, int celda) {
        cualCeldaCredito = celda;
        indexCredito = indice;
        indexAuxCredito = indice;
        indexAuxDebito = -1;
        secRegistroCredito = listCuentasCredito.get(indexCredito).getSecuencia();
    }

    /**
     * Metodo que obtiene la posicion dentro de la tabla VigenciasProrrateos
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndiceDebito(int indice, int celda) {
        indexDebito = indice;
        cualCeldaDebito = celda;
        indexAuxDebito = indice;
        indexAuxCredito = -1;
        secRegistroDebito = listCuentasDebito.get(indexDebito).getSecuencia();

    }

    //MOSTRAR DATOS CELDA
    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {
        if (indexAuxCredito >= 0) {
            if (tipoListaCredito == 0) {
                editarCredito = listCuentasCredito.get(indexCredito);
            }
            if (tipoListaCredito == 1) {
                editarCredito = filtrarListCuentasCredito.get(indexCredito);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaCredito == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialCreditoD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicialCreditoD').show()");
                cualCeldaCredito = -1;
            } else if (cualCeldaCredito == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalCreditoD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaFinalCreditoD').show()");
                cualCeldaCredito = -1;
            } else if (cualCeldaCredito == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoCreditoD");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoCreditoD').show()");
                cualCeldaCredito = -1;
            } else if (cualCeldaCredito == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionCreditoD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionCreditoD').show()");
                cualCeldaCredito = -1;
            } else if (cualCeldaCredito == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionCCCreditoD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionCCCreditoD').show()");
                cualCeldaCredito = -1;
            } else if (cualCeldaCredito == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCCCreditoD");
                RequestContext.getCurrentInstance().execute("PF('editarCCCreditoD').show()");
                cualCeldaCredito = -1;
            } else if (cualCeldaCredito == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoCreditoD");
                RequestContext.getCurrentInstance().execute("PF('editarTipoCreditoD').show()");
                cualCeldaCredito = -1;
            }
        }
        if (indexAuxDebito >= 0) {
            if (tipoListaDebito == 0) {
                editarDebito = listCuentasDebito.get(indexDebito);
            }
            if (tipoListaDebito == 1) {
                editarDebito = filtrarListCuentasDebito.get(indexDebito);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaDebito == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicialDebitoD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicialDebitoD').show()");
                cualCeldaDebito = -1;
            } else if (cualCeldaDebito == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaFinalDebitoD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaFinalDebitoD').show()");
                cualCeldaDebito = -1;
            } else if (cualCeldaDebito == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoDebitoD");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoDebitoD').show()");
                cualCeldaDebito = -1;
            } else if (cualCeldaDebito == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionDebitoD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionDebitoD').show()");
                cualCeldaDebito = -1;
            } else if (cualCeldaDebito == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionCCDebitoD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionCCDebitoD').show()");
                cualCeldaDebito = -1;
            } else if (cualCeldaDebito == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCCDebitoD");
                RequestContext.getCurrentInstance().execute("PF('editarCCDebitoD').show()");
                cualCeldaDebito = -1;
            } else if (cualCeldaDebito == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoDebitoD");
                RequestContext.getCurrentInstance().execute("PF('editarTipoDebitoD').show()");
                cualCeldaDebito = -1;
            }
        }
        indexCredito = -1;
        secRegistroCredito = null;
        indexDebito = -1;
        secRegistroDebito = null;
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    /**
     * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        // if (indexMvrs >= 0) {
        if (indexCredito >= 0) {
            filtradoCredito();
        }
        // }
        //  if (indexOtrosCertificados >= 0) {
        if (indexDebito >= 0) {
            filtradoDebito();
        }
        //  }
    }

    /**
     * Metodo que acciona el filtrado de la tabla vigencia localizacion
     */
    public void filtradoCredito() {
        if (banderaCredito == 0) {
            altoTablaCredito = "100";
            creditoFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoFechaInicial");
            creditoFechaInicial.setFilterStyle("width: 85% !important");
            creditoFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoFechaFinal");
            creditoFechaFinal.setFilterStyle("width: 85% !important");
            creditoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoCodigo");
            creditoCodigo.setFilterStyle("width: 85% !important");
            creditoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoDescripcion");
            creditoDescripcion.setFilterStyle("width: 85% !important");
            creditoDescripcionCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoDescripcionCC");
            creditoDescripcionCC.setFilterStyle("width: 85% !important");
            creditoCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoCC");
            creditoCC.setFilterStyle("width: 85% !important");
            creditoTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoTipo");
            creditoTipo.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosCuentaCredito");
            banderaCredito = 1;
        } else if (banderaCredito == 1) {
            altoTablaCredito = "120";
            creditoFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoFechaInicial");
            creditoFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            creditoFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoFechaFinal");
            creditoFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            creditoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoCodigo");
            creditoCodigo.setFilterStyle("display: none; visibility: hidden;");
            creditoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoDescripcion");
            creditoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            creditoDescripcionCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoDescripcionCC");
            creditoDescripcionCC.setFilterStyle("display: none; visibility: hidden;");
            creditoCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoCC");
            creditoCC.setFilterStyle("display: none; visibility: hidden;");
            creditoTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoTipo");
            creditoTipo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCuentaCredito");
            banderaCredito = 0;
            filtrarListCuentasCredito = null;
            tipoListaCredito = 0;
        }

    }

    /**
     * Metodo que acciona el filtrado de la tabla vigencia prorrateo
     */
    public void filtradoDebito() {
        if (banderaDebito == 0) {

            altoTablaDebito = "83";
            debitoFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoFechaInicial");
            debitoFechaInicial.setFilterStyle("width: 85% !important;");
            debitoFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoFechaFinal");
            debitoFechaFinal.setFilterStyle("width: 85% !important;");
            debitoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoCodigo");
            debitoCodigo.setFilterStyle("width: 85% !important;");
            debitoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoDescripcion");
            debitoDescripcion.setFilterStyle("width: 85% !important;");
            debitoDescripcionCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoDescripcionCC");
            debitoDescripcionCC.setFilterStyle("width: 85% !important;");
            debitoCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoCC");
            debitoCC.setFilterStyle("width: 85% !important;");
            debitoTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoTipo");
            debitoTipo.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosCuentaDebito");
            banderaDebito = 1;
        } else if (banderaDebito == 1) {
            altoTablaDebito = "105";
            debitoFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoFechaInicial");
            debitoFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            debitoFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoFechaFinal");
            debitoFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            debitoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoCodigo");
            debitoCodigo.setFilterStyle("display: none; visibility: hidden;");
            debitoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoDescripcion");
            debitoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            debitoDescripcionCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoDescripcionCC");
            debitoDescripcionCC.setFilterStyle("display: none; visibility: hidden;");
            debitoCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoCC");
            debitoCC.setFilterStyle("display: none; visibility: hidden;");
            debitoTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoTipo");
            debitoTipo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCuentaDebito");
            banderaDebito = 0;
            filtrarListCuentasDebito = null;
            tipoListaDebito = 0;
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        if (banderaCredito == 1) {
            altoTablaCredito = "120";
            creditoFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoFechaInicial");
            creditoFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            creditoFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoFechaFinal");
            creditoFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            creditoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoCodigo");
            creditoCodigo.setFilterStyle("display: none; visibility: hidden;");
            creditoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoDescripcion");
            creditoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            creditoDescripcionCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoDescripcionCC");
            creditoDescripcionCC.setFilterStyle("display: none; visibility: hidden;");
            creditoCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoCC");
            creditoCC.setFilterStyle("display: none; visibility: hidden;");
            creditoTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaCredito:creditoTipo");
            creditoTipo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCuentaCredito");
            banderaCredito = 0;
            filtrarListCuentasCredito = null;
            tipoListaCredito = 0;
        }
        if (banderaDebito == 1) {
            altoTablaDebito = "105";
            debitoFechaInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoFechaInicial");
            debitoFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            debitoFechaFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoFechaFinal");
            debitoFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            debitoCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoCodigo");
            debitoCodigo.setFilterStyle("display: none; visibility: hidden;");
            debitoDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoDescripcion");
            debitoDescripcion.setFilterStyle("display: none; visibility: hidden;");
            debitoDescripcionCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoDescripcionCC");
            debitoDescripcionCC.setFilterStyle("display: none; visibility: hidden;");
            debitoCC = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoCC");
            debitoCC.setFilterStyle("display: none; visibility: hidden;");
            debitoTipo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosCuentaDebito:debitoTipo");
            debitoTipo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCuentaDebito");
            banderaDebito = 0;
            filtrarListCuentasDebito = null;
            tipoListaDebito = 0;
        }
        indexCredito = -1;
        secRegistroCredito = null;
        indexDebito = -1;
        secRegistroDebito = null;
        listCuentasCredito = null;
        listCuentasDebito = null;
        guardado = true;
        cuentaActual = null;
    }

    /**
     * Metodo que activa el boton aceptar de la pagina y los dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    /**
     * Selecciona la tabla a exportar XML con respecto al index activo
     *
     * @return Nombre del dialogo a exportar en XML
     */
    public String exportXML() {
        if (indexAuxCredito >= 0) {
            nombreTabla = ":formExportarCredito:datosCreditoExportar";
            nombreXML = "CuentasCreditoXML";
        }
        if (indexAuxDebito >= 0) {
            nombreTabla = ":formExportarDebito:datosDebitoExportar";
            nombreXML = "CuentasDebitoXML";
        }
        return nombreTabla;
    }

    /**
     * Valida la tabla a exportar en PDF con respecto al index activo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        if (indexAuxCredito >= 0) {
            exportPDF_C();
        }
        if (indexAuxDebito >= 0) {
            exportPDF_D();
        }
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Localizacion
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDF_C() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCredito:datosCreditoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "CuentasCreditoPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexCredito = -1;
        secRegistroCredito = null;
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Prorrateo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDF_D() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDebito:datosDebitoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "CuentasDebitoPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexDebito = -1;
        secRegistroDebito = null;
    }

    /**
     * Verifica que tabla exportar XLS con respecto al index activo
     *
     * @throws IOException
     */
    public void verificarExportXLS() throws IOException {
        if (indexAuxCredito >= 0) {
            exportXLS_C();
        }
        if (indexAuxDebito >= 0) {
            exportXLS_D();
        }
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Sueldos
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS_C() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCredito:datosCreditoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "CuentasCreditoXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexCredito = -1;
        secRegistroCredito = null;
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Afiliaciones
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS_D() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDebito:datosDebitoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "CuentasDebitoXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        indexDebito = -1;
        secRegistroDebito = null;
    }

    //EVENTO FILTRAR
    /**
     * Evento que cambia la lista real a la filtrada
     */
    public void eventoFiltrar() {
        if (indexAuxCredito >= 0) {
            if (tipoListaCredito == 0) {
                tipoListaCredito = 1;
            }
        }
        if (indexAuxDebito >= 0) {
            if (tipoListaDebito == 0) {
                tipoListaDebito = 1;
            }
        }
    }

    //METODO RASTROS PARA LAS TABLAS EN EMPLVIGENCIASUELDOS
    public void verificarRastroTabla() {
        if (listCuentasDebito == null || listCuentasCredito == null) {
            //Dialogo para seleccionar el rato de la tabla deseada
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
        }

        if ((listCuentasDebito != null) && (listCuentasCredito != null)) {
            if (indexCredito >= 0) {
                verificarRastroCredito();
                indexCredito = -1;
            }
            if (indexDebito >= 0) {
                //Metodo Rastro Vigencias Afiliaciones
                verificarRastroDebito();
                indexDebito = -1;
            }
        }
    }
    //Verificar Rastro Vigencia Sueldos

    public void verificarRastroCredito() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listCuentasCredito != null) {
            if (secRegistroCredito != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroCredito, "VIGENCIASCUENTAS");
                backUpSecRegistroCredito = secRegistroCredito;
                backUp = secRegistroCredito;
                secRegistroCredito = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "VigenciasCuentas";
                    msnConfirmarRastro = "La tabla VIGENCIASCUENTAS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
        } else {
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASCUENTAS")) {
                nombreTablaRastro = "VigenciasCuentas";
                msnConfirmarRastroHistorico = "La tabla VIGENCIASCUENTAS tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        indexCredito = -1;
    }

    //Verificar Rastro Vigencia Sueldos
    public void verificarRastroDebito() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listCuentasDebito != null) {
            if (secRegistroDebito != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroDebito, "VIGENCIASCUENTAS");
                backUpSecRegistroDebito = secRegistroDebito;
                backUp = backUpSecRegistroDebito;
                secRegistroDebito = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "VigenciasCuentas";
                    msnConfirmarRastro = "La tabla VIGENCIASCUENTAS tiene rastros para el registro seleccionado, ¿desea continuar?";
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
        } else {
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASCUENTAS")) {
                nombreTablaRastro = "VigenciasCuentas";
                msnConfirmarRastroHistorico = "La tabla VIGENCIASCUENTAS tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        indexDebito = -1;
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }

    public void obtenerCuenta(BigInteger secuencia) {
        cuentaActual = administrarDetalleCuenta.mostrarCuenta(secuencia);
        indexCredito = -1;
        secRegistroCredito = null;
        indexDebito = -1;
        secRegistroDebito = null;
        listCuentasCredito = null;
        listCuentasDebito = null;
        guardado = true;
    }
    
    public List<VigenciasCuentas> getListCuentasCredito() {
        try {
            if (listCuentasCredito == null) {
                if (cuentaActual.getSecuencia() != null) {
                    listCuentasCredito = administrarDetalleCuenta.consultarListaVigenciasCuentasCredito(cuentaActual.getSecuencia());
                }
            }
            return listCuentasCredito;
        } catch (Exception e) {
            System.out.println("Error getListCuentasCredito : " + e.toString());
            return null;
        }
    }

    public void setListCuentasCredito(List<VigenciasCuentas> list) {
        this.listCuentasCredito = list;
    }

    public List<VigenciasCuentas> getFiltrarListCuentasCredito() {
        return filtrarListCuentasCredito;
    }

    public void setFiltrarListCuentasCredito(List<VigenciasCuentas> filtrar) {
        this.filtrarListCuentasCredito = filtrar;
    }

    public List<VigenciasCuentas> getListCuentasDebito() {
        try {
            if (listCuentasDebito == null) {
                if (cuentaActual.getSecuencia() != null) {
                    listCuentasDebito = administrarDetalleCuenta.consultarListaVigenciasCuentasDebito(cuentaActual.getSecuencia());
                }
            }
            return listCuentasDebito;
        } catch (Exception e) {
            System.out.println("Error getListCuentasDebito : " + e.toString());
            return null;
        }
    }

    public void setListCuentasDebito(List<VigenciasCuentas> listCuentasDebito) {
        this.listCuentasDebito = listCuentasDebito;
    }

    public List<VigenciasCuentas> getFiltrarListCuentasDebito() {
        return filtrarListCuentasDebito;
    }

    public void setFiltrarListCuentasDebito(List<VigenciasCuentas> listCuentasDebito) {
        this.filtrarListCuentasDebito = listCuentasDebito;
    }

    public Cuentas getCuentaActual() {
        return cuentaActual;
    }

    public void setCuentaActual(Cuentas cuenta) {
        this.cuentaActual = cuenta;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public VigenciasCuentas getEditarCredito() {
        return editarCredito;
    }

    public void setEditarCredito(VigenciasCuentas editarC) {
        this.editarCredito = editarC;
    }

    public VigenciasCuentas getEditarDebito() {
        return editarDebito;
    }

    public void setEditarDebito(VigenciasCuentas editarD) {
        this.editarDebito = editarD;
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

    public BigInteger getBackUpSecRegistroCredito() {
        return backUpSecRegistroCredito;
    }

    public void setBackUpSecRegistroCredito(BigInteger backUpSecRegistroCredito) {
        this.backUpSecRegistroCredito = backUpSecRegistroCredito;
    }

    public VigenciasCuentas getCuentaCreditoTablaSeleccionada() {
        getListCuentasCredito();
        if (listCuentasCredito != null) {
            int tam = listCuentasCredito.size();
            if (tam > 0) {
                cuentaCreditoTablaSeleccionada = listCuentasCredito.get(0);
            }
        }
        return cuentaCreditoTablaSeleccionada;
    }

    public void setCuentaCreditoTablaSeleccionada(VigenciasCuentas cuentaCreditoTablaSeleccionada) {
        this.cuentaCreditoTablaSeleccionada = cuentaCreditoTablaSeleccionada;
    }

    public VigenciasCuentas getCuentaDebitoTablaSeleccionada() {
        getListCuentasDebito();
        if (listCuentasDebito != null) {
            int tam = listCuentasDebito.size();
            if (tam > 0) {
                cuentaDebitoTablaSeleccionada = listCuentasDebito.get(0);
            }
        }
        return cuentaDebitoTablaSeleccionada;
    }

    public void setCuentaDebitoTablaSeleccionada(VigenciasCuentas cuentaDebitoTablaSeleccionada) {
        this.cuentaDebitoTablaSeleccionada = cuentaDebitoTablaSeleccionada;
    }

    public String getAltoTablaCredito() {
        return altoTablaCredito;
    }

    public void setAltoTablaCredito(String altoTablaCredito) {
        this.altoTablaCredito = altoTablaCredito;
    }

    public String getAltoTablaDebito() {
        return altoTablaDebito;
    }

    public void setAltoTablaDebito(String altoTablaDebito) {
        this.altoTablaDebito = altoTablaDebito;
    }

}
