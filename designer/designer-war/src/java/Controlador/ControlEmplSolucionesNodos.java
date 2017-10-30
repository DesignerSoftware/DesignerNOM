/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import ControlNavegacion.ListasRecurrentes;
import Entidades.CentrosCostos;
import Entidades.Conceptos;
import Entidades.Cuentas;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.Periodicidades;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.Terceros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplSolucionesNodosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasTiposTrabajadoresInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
@Named(value = "controlEmplSolucionesNodos")
@SessionScoped
public class ControlEmplSolucionesNodos implements Serializable {

    private static Logger log = Logger.getLogger(ControlEmplSolucionesNodos.class);

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarEmplSolucionesNodosInterface administrarEmplSolucionesNodos;

    //Lista empleados Soluciones Nodos
    private List<Empleados> listaEmpleados;
    private List<Empleados> listaEmpleadosFiltrar;
    private Empleados empleadoSeleccionado;
    //Listas Soluciones Nodos
    private List<SolucionesNodos> listaSN;
    private List<SolucionesNodos> listaSNFiltrar;
    private List<SolucionesNodos> listaSNCrear;
    private List<SolucionesNodos> listaSNModificar;
    private List<SolucionesNodos> listaSNBorrar;
    private SolucionesNodos nuevoSn;
    private SolucionesNodos duplicarSn;
    private SolucionesNodos editarSn;
    private SolucionesNodos snSeleccionado;
    //Lov Empleados
    private List<Empleados> lovEmpleados;
    private List<Empleados> lovEmpleadosFiltrar;
    private Empleados empleadoLovSeleccionado;
    //Lov Concepto
    private List<Conceptos> lovConceptos;
    private List<Conceptos> lovConceptosFiltrar;
    private Conceptos conceptoSeleccionado;
    //Lov Periodicidades
    private List<Periodicidades> lovPeriodicidades;
    private List<Periodicidades> lovPeriodicidadesFiltrar;
    private Periodicidades periodicidadSeleccionado;
    //Lov Terceros
    private List<Terceros> lovTerceros;
    private List<Terceros> lovTercerosFiltrar;
    private Terceros terceroSeleccionado;
    //Lov Cuentas
    private List<Cuentas> lovCuentas;
    private List<Cuentas> lovCuentassFiltrar;
    private Cuentas cuentaSeleccionada;
    //Lov CentrosCostos
    private List<CentrosCostos> lovCentrosCostos;
    private List<CentrosCostos> lovCentrosCostosFiltrar;
    private CentrosCostos centroCostoSeleccionado;
    //Lov Procesos
    private List<Procesos> lovProcesos;
    private List<Procesos> lovProcesosFiltrar;
    private Procesos procesoSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoListaNov;
    private String mensajeValidacion;
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean guardado;
    //Columnas tabla empleados
    private Column nEFechasIngreso, nEEmpleadosNombres;
    //Columnas tabla SolucionesNodos
    private Column concepto, descripcion, fechaI, fechaF, valor, saldo, unidades, estado, pago, nit,
            nombretecero, ccdebito, cccredito, cuentad, cuentac, tipo, proceso;
    private String altoTablaEmp, altoTablaRegEmp, altoTablaAux;// altoTablaEmp, altoTablaRegEmp;
    private String infoRegistroEmpleados, infoRegistroSoluciones, infoRegistroEmpleadosLOV, infoRegistroConcepto, infoRegistroPeriodicidad,
            infoRegistroTercero, infoRegistroCentroCostoD, infoRegistroCuenta, infoRegistroCuentaD, infoRegistroCentroCosto, infoRegistroProceso;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private ListasRecurrentes listasRecurrentes;
    private String msgError;
    private boolean activarMTodos, activarLOV;
    private int k;
    private BigInteger l;

    public ControlEmplSolucionesNodos() {

        listaEmpleados = null;
        listaSNBorrar = new ArrayList<SolucionesNodos>();
        listaSNCrear = new ArrayList<SolucionesNodos>();
        listaSNModificar = new ArrayList<SolucionesNodos>();
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        tipoListaNov = 0;
        lovEmpleados = null;
        nuevoSn = new SolucionesNodos();
        nuevoSn.setEstado("CERRADO");
        nuevoSn.setTipo("PAGO");
        duplicarSn = new SolucionesNodos();
        altoTablaEmp = "125";
        altoTablaAux = "130";
        altoTablaRegEmp = "6";
        paginaAnterior = "nominaf";
        activarMTodos = true;
        periodicidadSeleccionado = null;
        activarLOV = true;
        mapParametros.put("paginaAnterior", paginaAnterior);

    }

    @PreDestroy
    public void destruyendose() {
        log.info(this.getClass().getName() + ".destruyendose() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEmplSolucionesNodos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listaEmpleados = null;
            getListaEmpleados();
            if (listaEmpleados != null) {
                if (!listaEmpleados.isEmpty()) {
                    empleadoSeleccionado = listaEmpleados.get(0);
                }
            }
            listaSN = null;
            getListaSN();
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
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
        String pagActual = "emplsolucionnodo";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
        }
        limpiarListasValor();
    }

    public void limpiarListasValor() {
        lovEmpleados = null;
        lovCentrosCostos = null;
        lovConceptos = null;
        lovCuentas = null;
        lovEmpleados = null;
        lovPeriodicidades = null;
        lovProcesos = null;
        lovTerceros = null;
    }

    public void cambiarIndice(SolucionesNodos solucion, int celda) {
        snSeleccionado = solucion;
        cualCelda = celda;
        snSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            snSeleccionado.getCodigoconcepto();
            habilitarBtnLov();
        } else if (cualCelda == 1) {
            snSeleccionado.getNombreconcepto();
            habilitarBtnLov();
        } else if (cualCelda == 2) {
            snSeleccionado.getFechadesde();
            deshabilitarBtnLov();
        } else if (cualCelda == 3) {
            snSeleccionado.getFechahasta();
            deshabilitarBtnLov();
        } else if (cualCelda == 4) {
            snSeleccionado.getValor();
            deshabilitarBtnLov();
        } else if (cualCelda == 5) {
            snSeleccionado.getSaldo();
            deshabilitarBtnLov();
        } else if (cualCelda == 6) {
            snSeleccionado.getUnidades();
            deshabilitarBtnLov();
        } else if (cualCelda == 7) {
            snSeleccionado.getEstado();
            deshabilitarBtnLov();
        } else if (cualCelda == 8) {
            snSeleccionado.getPago();
            deshabilitarBtnLov();
        } else if (cualCelda == 9) {
            snSeleccionado.getNittercero();
            habilitarBtnLov();
        } else if (cualCelda == 10) {
            snSeleccionado.getNombretercero();
            habilitarBtnLov();
        } else if (cualCelda == 11) {
            snSeleccionado.getCentrocostod();
            habilitarBtnLov();
        } else if (cualCelda == 12) {
            snSeleccionado.getCentrocostoc();
            habilitarBtnLov();
        } else if (cualCelda == 13) {
            snSeleccionado.getCuentad();
            habilitarBtnLov();
        } else if (cualCelda == 14) {
            snSeleccionado.getCuentac();
            habilitarBtnLov();
        } else if (cualCelda == 15) {
            snSeleccionado.getTipo();
            deshabilitarBtnLov();
        } else if (cualCelda == 16) {
            snSeleccionado.getProceso();
            habilitarBtnLov();
        }
    }

    public void habilitarBtnLov() {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBtnLov() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    //Ubicacion Celda Arriba 
    public void cambiarEmpleado() {
        listaSN = null;
        if (listaSNCrear.isEmpty() && listaSNBorrar.isEmpty() && listaSNModificar.isEmpty()) {
            snSeleccionado = null;
            getListaSN();
            if (tipoListaNov == 1) {
                RequestContext.getCurrentInstance().execute("PF('datosSolucionesNodos').clearFilters()");
            }
            contarRegistrosSoluciones();
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        }
    }

    public void abrirLista(int listaV) {
        if (listaV == 0) {
            lovEmpleados = null;
            cargarLovEmpleados();
            contarRegistroEmpleadosLOV();
            RequestContext.getCurrentInstance().update("formLovEmpleado:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        }
    }

    public void agregarNuevaSN() {
        int pasa = 0;
//        Empleados emp = new Empleados();
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoSn.getFechadesde() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoSn.getFechahasta() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoSn.getConcepto() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (nuevoSn.getValor() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (nuevoSn.getFechapago() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoSn.getCentrocostoc() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoSn.getCentrocostod() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoSn.getCuentac() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (nuevoSn.getCuentad() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (pasa == 0) {
            if (bandera == 1) {
                cargarTablaDefault();
            }
            BigDecimal tipoTrabajador = administrarEmplSolucionesNodos.buscarTipoTrabjadorXEmpl(empleadoSeleccionado.getSecuencia());
            k++;
            l = BigInteger.valueOf(k);
            nuevoSn.setSecuencia(l);
            nuevoSn.setEmpleado(empleadoSeleccionado.getSecuencia());
            nuevoSn.setTipotrabajador(tipoTrabajador.toBigInteger());
            listaSNCrear.add(nuevoSn);
            listaSN.add(0, nuevoSn);
            snSeleccionado = nuevoSn;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSn");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSolucion').hide()");
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
            nuevoSn = new SolucionesNodos();
        } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoRegistro");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoRegistro').show()");
        }

    }

    public void duplicarSN() {
        if (snSeleccionado != null) {
            duplicarSn = new SolucionesNodos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarSn.setSecuencia(l);
            duplicarSn.setEmpleado(snSeleccionado.getEmpleado());
            nuevoSn.setTipotrabajador(snSeleccionado.getTipotrabajador());
            duplicarSn.setConcepto(snSeleccionado.getConcepto());
            duplicarSn.setNombreconcepto(snSeleccionado.getNombreconcepto());
            duplicarSn.setFechadesde(snSeleccionado.getFechadesde());
            duplicarSn.setFechahasta(snSeleccionado.getFechahasta());
            duplicarSn.setValor(snSeleccionado.getValor());
            duplicarSn.setSaldo(snSeleccionado.getSaldo());
            duplicarSn.setUnidades(snSeleccionado.getUnidades());
            duplicarSn.setEstado(snSeleccionado.getEstado());
            duplicarSn.setPago(snSeleccionado.getPago());
            duplicarSn.setNittercero(snSeleccionado.getNittercero());
            duplicarSn.setNombretercero(snSeleccionado.getNombretercero());
            duplicarSn.setCentrocostod(snSeleccionado.getCentrocostod());
            duplicarSn.setCentrocostoc(snSeleccionado.getCentrocostoc());
            duplicarSn.setCuentac(snSeleccionado.getCuentac());
            duplicarSn.setCuentad(snSeleccionado.getCuentad());
            duplicarSn.setTipo(snSeleccionado.getTipo());
            duplicarSn.setProceso(snSeleccionado.getProceso());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSn");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSolucion').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        if (duplicarSn.getFechadesde() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarSn.getFechahasta() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (duplicarSn.getValor() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (duplicarSn.getFechapago() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (duplicarSn.getConcepto() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarSn.getCentrocostoc() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarSn.getCentrocostod() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarSn.getCuentac() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }
        if (duplicarSn.getCuentad() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            pasa++;
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoRegistro");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoRegistro').show()");
        }
        if (pasa == 0) {
            listaSN.add(0, duplicarSn);
            listaSNCrear.add(duplicarSn);
            snSeleccionado = duplicarSn;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                cargarTablaDefault();
            }
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
            duplicarSn = new SolucionesNodos();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroSolucion");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSolucion').hide()");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SolucionesNodosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SolucionesNodosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTablaEmp = "105";
            altoTablaAux = "110";
            altoTablaRegEmp = "5";
            concepto = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:concepto");
            concepto.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            fechaI = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:fechaI");
            fechaI.setFilterStyle("width: 85% !important");
            fechaF = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:fechaF");
            fechaF.setFilterStyle("width: 85% !important");
            valor = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:valor");
            valor.setFilterStyle("width: 85% !important");
            saldo = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:saldo");
            saldo.setFilterStyle("width: 85% !important");
            unidades = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:unidades");
            unidades.setFilterStyle("width: 85% !important");
            estado = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:estado");
            estado.setFilterStyle("width: 85% !important");
            pago = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:pago");
            pago.setFilterStyle("width: 85% !important");
            nit = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:nit");
            nit.setFilterStyle("width: 85% !important");
            nombretecero = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:nombretecero");
            nombretecero.setFilterStyle("width: 85% !important");
            ccdebito = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:ccdebito");
            ccdebito.setFilterStyle("width: 85% !important");
            cccredito = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:cccredito");
            cccredito.setFilterStyle("width: 85% !important");
            cuentad = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:cuentad");
            cuentad.setFilterStyle("width: 85% !important");
            cuentac = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:cuentac");
            cuentac.setFilterStyle("width: 85% !important");
            tipo = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:tipo");
            tipo.setFilterStyle("width: 85% !important");
            proceso = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:proceso");
            proceso.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
            bandera = 1;
            nEEmpleadosNombres = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosNombres");
            nEEmpleadosNombres.setFilterStyle("width: 85% !important");
            nEFechasIngreso = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEFechasIngreso");
            nEFechasIngreso.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosEmpleados");
        } else if (bandera == 1) {
            cargarTablaDefault();
        }
        contarRegistroEmpleados();
        contarRegistrosSoluciones();
    }

    public void cargarTablaDefault() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTablaEmp = "125";
        altoTablaAux = "130";
        altoTablaRegEmp = "6";
        concepto = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:concepto");
        concepto.setFilterStyle("display: none; visibility: hidden;");
        descripcion = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:descripcion");
        descripcion.setFilterStyle("display: none; visibility: hidden;");
        fechaI = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:fechaI");
        fechaI.setFilterStyle("display: none; visibility: hidden;");
        fechaF = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:fechaF");
        fechaF.setFilterStyle("display: none; visibility: hidden;");
        valor = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:valor");
        valor.setFilterStyle("display: none; visibility: hidden;");
        saldo = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:saldo");
        saldo.setFilterStyle("display: none; visibility: hidden;");
        unidades = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:unidades");
        unidades.setFilterStyle("display: none; visibility: hidden;");
        estado = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:estado");
        estado.setFilterStyle("display: none; visibility: hidden;");
        pago = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:pago");
        pago.setFilterStyle("display: none; visibility: hidden;");
        nit = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:nit");
        nit.setFilterStyle("display: none; visibility: hidden;");
        nombretecero = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:nombretecero");
        nombretecero.setFilterStyle("display: none; visibility: hidden;");
        ccdebito = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:ccdebito");
        ccdebito.setFilterStyle("display: none; visibility: hidden;");
        cccredito = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:cccredito");
        cccredito.setFilterStyle("display: none; visibility: hidden;");
        cuentad = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:cuentad");
        cuentad.setFilterStyle("display: none; visibility: hidden;");
        cuentac = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:cuentac");
        cuentac.setFilterStyle("display: none; visibility: hidden;");
        tipo = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:tipo");
        tipo.setFilterStyle("display: none; visibility: hidden;");
        proceso = (Column) c.getViewRoot().findComponent("form:datosSolucionesNodos:proceso");
        proceso.setFilterStyle("display: none; visibility: hidden;");
        bandera = 0;
        nEEmpleadosNombres = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEEmpleadosNombres");
        nEEmpleadosNombres.setFilterStyle("display: none; visibility: hidden;");
        nEFechasIngreso = (Column) c.getViewRoot().findComponent("form:datosEmpleados:nEFechasIngreso");
        nEFechasIngreso.setFilterStyle("display: none; visibility: hidden;");
        listaSNFiltrar = null;
        listaEmpleadosFiltrar = null;
        RequestContext.getCurrentInstance().execute("PF('datosSolucionesNodos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('datosEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        tipoLista = 0;
        tipoListaNov = 0;
    }

    public void mostrarTodos() {
        if (!listaEmpleados.isEmpty()) {
            listaEmpleados.clear();
        }
        if (lovEmpleados != null) {
            for (int i = 0; i < lovEmpleados.size(); i++) {
                listaEmpleados.add(lovEmpleados.get(i));
            }
        }
        empleadoSeleccionado = listaEmpleados.get(0);
        listaSN = administrarEmplSolucionesNodos.solucionesNodosEmpl(empleadoSeleccionado.getSecuencia());
        listaEmpleadosFiltrar = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        activarMTodos = true;
        contarRegistroEmpleados();
        contarRegistrosSoluciones();
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    public void seleccionarComboBox(SolucionesNodos sn, String estado) {
        snSeleccionado = sn;
        if (estado.equals("PAGO")) {
            snSeleccionado.setTipo("PAGO");
        } else if (estado.equals("DESCUENTO")) {
            snSeleccionado.setTipo("DESCUENTO");
        } else if (estado.equals("PASIVO")) {
            snSeleccionado.setTipo("PASIVO");
        } else if (estado.equals("GASTO")) {
            snSeleccionado.setTipo("GASTO");
        } else if (estado.equals("NETO")) {
            snSeleccionado.setTipo("(\"NETO");
        }
        modificarSn(snSeleccionado);
    }

    public void actualizarEmpleadosNovedad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaEmpleados.isEmpty()) {
            listaEmpleados.clear();
            listaEmpleados.add(empleadoLovSeleccionado);
            empleadoSeleccionado = listaEmpleados.get(0);
        }
        listaSN.clear();
        listaSN = administrarEmplSolucionesNodos.solucionesNodosEmpl(empleadoSeleccionado.getSecuencia());
        aceptar = true;
        listaEmpleadosFiltrar = null;
        if (listaSN != null) {
            if (!listaSN.isEmpty()) {
                snSeleccionado = listaSN.get(0);
            }
        }
        tipoActualizacion = -1;
        cualCelda = -1;
        activarMTodos = false;
        empleadoLovSeleccionado = null;
        contarRegistroEmpleados();
        contarRegistrosSoluciones();
        context.reset("formLovEmpleado:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovEmpleado:empleadosDialogo");
        RequestContext.getCurrentInstance().update("formLovEmpleado:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formLovEmpleado:aceptarE");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    //BORRAR NOVEDADES
    public void borrarNovedades() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (snSeleccionado != null) {
            if (!listaSNModificar.isEmpty() && listaSNModificar.contains(snSeleccionado)) {
                int modIndex = listaSNModificar.indexOf(snSeleccionado);
                listaSNModificar.remove(modIndex);
                listaSNBorrar.add(snSeleccionado);
            } else if (!listaSNCrear.isEmpty() && listaSNCrear.contains(snSeleccionado)) {
                int crearIndex = listaSNCrear.indexOf(snSeleccionado);
                listaSNCrear.remove(crearIndex);
            } else {
                listaSNBorrar.add(snSeleccionado);
            }
            listaSN.remove(snSeleccionado);

            if (tipoListaNov == 1) {
                listaSNFiltrar.remove(snSeleccionado);
            }
            contarRegistrosSoluciones();
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
            snSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void modificarSn(SolucionesNodos sn) {
        snSeleccionado = sn;
        if (!listaSNCrear.contains(snSeleccionado)) {
            if (listaSNModificar.isEmpty()) {
                listaSNModificar.add(snSeleccionado);
            } else if (!listaSNModificar.contains(snSeleccionado)) {
                listaSNModificar.add(snSeleccionado);
            }
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");

    }

    //GUARDAR
    public void guardarCambiosNovedades() {
        msgError = "";
        if (guardado == false) {
            if (!listaSNBorrar.isEmpty()) {
                for (int i = 0; i < listaSNBorrar.size(); i++) {
                    msgError = administrarEmplSolucionesNodos.borrar(listaSNBorrar.get(i));
                }
                listaSNBorrar.clear();
            }

            if (!listaSNCrear.isEmpty()) {
                for (int i = 0; i < listaSNCrear.size(); i++) {
                    msgError = administrarEmplSolucionesNodos.crear(listaSNCrear.get(i));
                }
                listaSNCrear.clear();
            }
            if (!listaSNModificar.isEmpty()) {
                for (int i = 0; i < listaSNModificar.size(); i++) {
                    msgError = administrarEmplSolucionesNodos.crear(listaSNModificar.get(i));
                }
                listaSNModificar.clear();
            }

            if (msgError.equals("EXITO")) {

                listaSN = null;
                getListaSN();
                contarRegistrosSoluciones();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
                guardado = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                //  k = 0;
            } else {
                RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardadoBD");
                RequestContext.getCurrentInstance().execute("PF('errorGuardadoBD').show()");
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarCambioEmpleados() {
        lovEmpleadosFiltrar = null;
        empleadoLovSeleccionado = null;
        aceptar = true;
        snSeleccionado = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovEmpleado:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovEmpleado:empleadosDialogo");
        RequestContext.getCurrentInstance().update("formLovEmpleado:LOVEmpleados");
        RequestContext.getCurrentInstance().update("formLovEmpleado:aceptarE");
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (snSeleccionado != null) {
            int result = administrarRastros.obtenerTabla(snSeleccionado.getSecuencia(), "SOLUCIONESNODOS");
            if (result == 1) {
                RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (result == 2) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (result == 3) {
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (result == 4) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (result == 5) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("SOLUCIONESNODOS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            cargarTablaDefault();
        }
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        tipoListaNov = 0;
        limpiarNuevaSN();
        limpiarduplicarSN();
        altoTablaEmp = "125";
        altoTablaAux = "130";
        altoTablaRegEmp = "6";
        listaSNBorrar.clear();
        listaSNCrear.clear();
        listaSNModificar.clear();
        listaSN = null;
        lovEmpleados = null;
        activarMTodos = true;
        empleadoSeleccionado = null;
        snSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            cargarTablaDefault();
        }
        listaEmpleados = null;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        tipoListaNov = 0;
        lovEmpleados = null;
        limpiarNuevaSN();
        limpiarduplicarSN();
        altoTablaEmp = "125";
        altoTablaAux = "130";
        altoTablaRegEmp = "6";
        listaSNBorrar.clear();
        listaSNCrear.clear();
        listaSNModificar.clear();
        snSeleccionado = null;
        listaSN = null;
        activarMTodos = true;
        empleadoSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        navegar("atras");
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoListaNov == 0) {
            tipoListaNov = 1;
        }
        contarRegistrosSoluciones();
    }

    public void eventoFiltrarEmpleados() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        empleadoSeleccionado = null;
        snSeleccionado = null;
        listaSN = null;
        listaSNFiltrar = null;
        if (tipoListaNov == 1) {
            RequestContext.getCurrentInstance().execute("PF('datosSolucionesNodos').clearFilters()");
        }
        RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        contarRegistrosSoluciones();
        contarRegistroEmpleados();
    }

    public void contarRegistroEmpleados() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
    }

    public void contarRegistrosSoluciones() {
        RequestContext.getCurrentInstance().update("form:infoRegistroNovedades");
    }

    public void contarRegistroEmpleadosLOV() {
        RequestContext.getCurrentInstance().update("formLovEmpleado:infoRegistroEmpleadosLOV");
    }

    public void contarRegistroConcepto() {
        RequestContext.getCurrentInstance().update("formLovConcepto:infoRegistroConceptosLOV");
    }

    public void contarRegistroTercero() {
        RequestContext.getCurrentInstance().update("formLovTercero:infoRegistroTercerosLOV");
    }

    public void contarRegistroCuenta() {
        RequestContext.getCurrentInstance().update("formLovCuentaC:infoRegistroCuentaC");
    }

    public void contarRegistroCuentaD() {
        RequestContext.getCurrentInstance().update("formLovCuentaD:infoRegistroCuentaD");
    }

    public void contarRegistroCentroCosto() {
        RequestContext.getCurrentInstance().update("formLovCC:infoRegistroCCCLOV");
    }

    public void contarRegistroCentroCostoD() {
        RequestContext.getCurrentInstance().update("formLovCCD:infoRegistroCCDLOV");
    }

    public void contarRegistroProceso() {
        RequestContext.getCurrentInstance().update("formProceso:infoRegistroProceso");
    }

    public void posicionOtro() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, String> map = context.getExternalContext().getRequestParameterMap();
            String celda = map.get("celda"); // name attribute of node 
            String registro = map.get("registro"); // type attribute of node 
            int indice = Integer.parseInt(registro);
            int columna = Integer.parseInt(celda);
            snSeleccionado = listaSN.get(indice);
            cambiarIndice(snSeleccionado, columna);
        } catch (ArrayIndexOutOfBoundsException aex) {
            snSeleccionado = null;
        }
    }

    public void actualizarConcepto() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (!listaSNCrear.contains(snSeleccionado)) {
                snSeleccionado.setConcepto(conceptoSeleccionado.getSecuencia());
                snSeleccionado.setCodigoconcepto(conceptoSeleccionado.getCodigo());
                snSeleccionado.setNombreconcepto(conceptoSeleccionado.getDescripcion());
                if (listaSNModificar.isEmpty()) {
                    listaSNModificar.add(snSeleccionado);
                } else if (!listaSNModificar.contains(snSeleccionado)) {
                    listaSNModificar.add(snSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        }
        if (tipoActualizacion == 1) {
            nuevoSn.setConcepto(conceptoSeleccionado.getSecuencia());
            nuevoSn.setCodigoconcepto(conceptoSeleccionado.getCodigo());
            nuevoSn.setNombreconcepto(conceptoSeleccionado.getDescripcion());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSn");
        } else if (tipoActualizacion == 2) {
            duplicarSn.setConcepto(conceptoSeleccionado.getSecuencia());
            duplicarSn.setCodigoconcepto(conceptoSeleccionado.getCodigo());
            duplicarSn.setNombreconcepto(conceptoSeleccionado.getDescripcion());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSn");
        }
        lovConceptosFiltrar = null;
        conceptoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovConcepto:LOVConceptos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovConcepto:LOVConceptos");
        RequestContext.getCurrentInstance().update("formLovConcepto:conceptosDialogo");
        RequestContext.getCurrentInstance().update("formLovConcepto:aceptarP");
    }

    public void cancelarCambioConcepto() {
        lovConceptosFiltrar = null;
        conceptoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovConcepto:LOVConceptos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVConceptos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovConcepto:LOVConceptos");
        RequestContext.getCurrentInstance().update("formLovConcepto:conceptosDialogo");
        RequestContext.getCurrentInstance().update("formLovConcepto:aceptarP");

    }

    public void actualizarTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (!listaSNCrear.contains(snSeleccionado)) {
                snSeleccionado.setNittercero(terceroSeleccionado.getSecuencia());
                snSeleccionado.setNombretercero(terceroSeleccionado.getNombre());
                if (listaSNModificar.isEmpty()) {
                    listaSNModificar.add(snSeleccionado);
                } else if (!listaSNModificar.contains(snSeleccionado)) {
                    listaSNModificar.add(snSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        }
        if (tipoActualizacion == 1) {
            nuevoSn.setNittercero(terceroSeleccionado.getSecuencia());
            nuevoSn.setNombretercero(terceroSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSn");
        } else if (tipoActualizacion == 2) {
            duplicarSn.setNittercero(terceroSeleccionado.getSecuencia());
            duplicarSn.setNombretercero(terceroSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSn");
        }
        lovTercerosFiltrar = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovTercero:LOVTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovTercero:LOVTerceros");
        RequestContext.getCurrentInstance().update("formLovTercero:tercerosDialogo");
        RequestContext.getCurrentInstance().update("formLovTercero:aceptarT");
    }

    public void cancelarCambioTercero() {
        lovTercerosFiltrar = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovTercero:LOVTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVTerceros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovTercero:LOVTerceros");
        RequestContext.getCurrentInstance().update("formLovTercero:tercerosDialogo");
        RequestContext.getCurrentInstance().update("formLovTercero:aceptarT");
    }

    public void actualizarCCCredito() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (!listaSNCrear.contains(snSeleccionado)) {
                snSeleccionado.setCentrocostoc(centroCostoSeleccionado.getSecuencia());
                snSeleccionado.setNombrecentrocostoc(centroCostoSeleccionado.getNombre());
                if (listaSNModificar.isEmpty()) {
                    listaSNModificar.add(snSeleccionado);
                } else if (!listaSNModificar.contains(snSeleccionado)) {
                    listaSNModificar.add(snSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        }
        if (tipoActualizacion == 1) {
            nuevoSn.setCentrocostoc(centroCostoSeleccionado.getSecuencia());
            nuevoSn.setNombrecentrocostoc(centroCostoSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSn");
        } else if (tipoActualizacion == 2) {
            duplicarSn.setCentrocostoc(centroCostoSeleccionado.getSecuencia());
            duplicarSn.setNombrecentrocostoc(centroCostoSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSn");
        }
        lovCentrosCostosFiltrar = null;
        centroCostoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovCC:LOVCC:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCC').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ccDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovCC:LOVCC");
        RequestContext.getCurrentInstance().update("formLovCC:ccDialogo");
        RequestContext.getCurrentInstance().update("formLovCC:aceptarCC");
    }

    public void cancelarCambioCCCredito() {
        lovCentrosCostosFiltrar = null;
        centroCostoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovCC:LOVCC:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCC').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ccDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovCC:LOVCC");
        RequestContext.getCurrentInstance().update("formLovCC:ccDialogo");
        RequestContext.getCurrentInstance().update("formLovCC:aceptarCC");
    }

    public void actualizarCCDebito() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (!listaSNCrear.contains(snSeleccionado)) {
                snSeleccionado.setCentrocostod(centroCostoSeleccionado.getSecuencia());
                snSeleccionado.setNombrecentrocostod(centroCostoSeleccionado.getNombre());
                if (listaSNModificar.isEmpty()) {
                    listaSNModificar.add(snSeleccionado);
                } else if (!listaSNModificar.contains(snSeleccionado)) {
                    listaSNModificar.add(snSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        }
        if (tipoActualizacion == 1) {
            nuevoSn.setCentrocostod(centroCostoSeleccionado.getSecuencia());
            nuevoSn.setNombrecentrocostod(centroCostoSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSn");
        } else if (tipoActualizacion == 2) {
            duplicarSn.setCentrocostod(centroCostoSeleccionado.getSecuencia());
            duplicarSn.setNombrecentrocostod(centroCostoSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSn");
        }
        lovCentrosCostosFiltrar = null;
        centroCostoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovCCD:LOVCCD:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCCD').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ccdDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovCCD:LOVCCD");
        RequestContext.getCurrentInstance().update("formLovCCD:ccdDialogo");
        RequestContext.getCurrentInstance().update("formLovCCD:aceptarCCD");
    }

    public void cancelarCambioCCDebito() {
        lovCentrosCostosFiltrar = null;
        centroCostoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovCCD:LOVCCD:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCCD').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ccdDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovCCD:LOVCCD");
        RequestContext.getCurrentInstance().update("formLovCCD:ccdDialogo");
        RequestContext.getCurrentInstance().update("formLovCCD:aceptarCCD");
    }

    public void actualizarCuentaD() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (!listaSNCrear.contains(snSeleccionado)) {
                snSeleccionado.setCuentad(cuentaSeleccionada.getSecuencia());
                snSeleccionado.setCodigocuentad(cuentaSeleccionada.getCodigo());
                if (listaSNModificar.isEmpty()) {
                    listaSNModificar.add(snSeleccionado);
                } else if (!listaSNModificar.contains(snSeleccionado)) {
                    listaSNModificar.add(snSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        }
        if (tipoActualizacion == 1) {
            nuevoSn.setCuentad(cuentaSeleccionada.getSecuencia());
            nuevoSn.setCodigocuentad(cuentaSeleccionada.getCodigo());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSn");
        } else if (tipoActualizacion == 2) {
            duplicarSn.setCuentad(cuentaSeleccionada.getSecuencia());
            duplicarSn.setCodigocuentad(cuentaSeleccionada.getCodigo());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSn");
        }
        lovCuentassFiltrar = null;
        cuentaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovCuentaD:LOVCuentaD:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCuentaD').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cuentadDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovCuentaD:LOVCuentaD");
        RequestContext.getCurrentInstance().update("formLovCuentaD:cuentadDialogo");
        RequestContext.getCurrentInstance().update("formLovCuentaD:aceptarCuentaD");
    }

    public void cancelarCambioCuentaD() {
        lovCuentassFiltrar = null;
        cuentaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovCuentaD:LOVCuentaD:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCuentaD').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cuentadDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovCuentaD:LOVCuentaD");
        RequestContext.getCurrentInstance().update("formLovCuentaD:cuentadDialogo");
        RequestContext.getCurrentInstance().update("formLovCuentaD:aceptarCuentaD");
    }

    public void actualizarCuentaC() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (!listaSNCrear.contains(snSeleccionado)) {
                snSeleccionado.setCuentac(cuentaSeleccionada.getSecuencia());
                snSeleccionado.setCodigocuentac(cuentaSeleccionada.getCodigo());
                if (listaSNModificar.isEmpty()) {
                    listaSNModificar.add(snSeleccionado);
                } else if (!listaSNModificar.contains(snSeleccionado)) {
                    listaSNModificar.add(snSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        }
        if (tipoActualizacion == 1) {
            nuevoSn.setCuentac(cuentaSeleccionada.getSecuencia());
            nuevoSn.setCodigocuentac(cuentaSeleccionada.getCodigo());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSn");
        } else if (tipoActualizacion == 2) {
            duplicarSn.setCuentac(cuentaSeleccionada.getSecuencia());
            duplicarSn.setCodigocuentac(cuentaSeleccionada.getCodigo());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSn");
        }
        lovCuentassFiltrar = null;
        cuentaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formLovCuentaC:LOVCuentaC:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCuentaC').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cuentacDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovCuentaC:LOVCuentaC");
        RequestContext.getCurrentInstance().update("formLovCuentaC:cuentacDialogo");
        RequestContext.getCurrentInstance().update("formLovCuentaC:aceptarCuentaC");
    }

    public void cancelarCambioCuentaC() {
        lovCuentassFiltrar = null;
        cuentaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formLovCuentaC:LOVCuentaC:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVCuentaC').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('cuentacDialogo').hide()");
        RequestContext.getCurrentInstance().update("formLovCuentaC:LOVCuentaC");
        RequestContext.getCurrentInstance().update("formLovCuentaC:cuentacDialogo");
        RequestContext.getCurrentInstance().update("formLovCuentaC:aceptarCuentaC");
    }

    public void actualizarProceso() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (!listaSNCrear.contains(snSeleccionado)) {
                snSeleccionado.setProceso(procesoSeleccionado.getSecuencia());
                if (listaSNModificar.isEmpty()) {
                    listaSNModificar.add(snSeleccionado);
                } else if (!listaSNModificar.contains(snSeleccionado)) {
                    listaSNModificar.add(snSeleccionado);
                }
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosSolucionesNodos");
        }
        if (tipoActualizacion == 1) {
            nuevoSn.setProceso(procesoSeleccionado.getSecuencia());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSn");
        } else if (tipoActualizacion == 2) {
            duplicarSn.setProceso(procesoSeleccionado.getSecuencia());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSn");
        }
        lovProcesos = null;
        procesoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formProceso:LOVProcesos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVProcesos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('procesoDialogo').hide()");
        RequestContext.getCurrentInstance().update("formProceso:LOVProcesos");
        RequestContext.getCurrentInstance().update("formProceso:procesoDialogo");
        RequestContext.getCurrentInstance().update("formProceso:aceptarProceso");
    }

    public void cancelarCambioProceso() {
        lovProcesos = null;
        procesoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formProceso:LOVProcesos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVProcesos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('procesoDialogo').hide()");
        RequestContext.getCurrentInstance().update("formProceso:LOVProcesos");
        RequestContext.getCurrentInstance().update("formProceso:procesoDialogo");
        RequestContext.getCurrentInstance().update("formProceso:aceptarProceso");
    }

    public void cargarLovEmpleados() {
        if (lovEmpleados == null) {
            lovEmpleados = administrarEmplSolucionesNodos.emplSolucionesNodos();
        }
    }

    public void cargarLovConceptos() {
        if (lovConceptos == null) {
            lovConceptos = administrarEmplSolucionesNodos.buscarConceptos();
        }
    }

    public void cargarLovProcesos() {
        if (lovProcesos == null) {
            lovProcesos = administrarEmplSolucionesNodos.buscarProcesos();
        }
    }

    public void cargarLovTerceros() {
        if (lovTerceros == null) {
            lovTerceros = administrarEmplSolucionesNodos.buscarTerceros();
        }
    }

    public void cargarLovCC() {
        if (lovCentrosCostos == null) {
            lovCentrosCostos = administrarEmplSolucionesNodos.buscarCentrosCostos();
        }
    }

    public void cargarLovCCD() {
        if (lovCentrosCostos == null) {
            lovCentrosCostos = administrarEmplSolucionesNodos.buscarCentrosCostos();
        }
    }

    public void cargarLovCuentaC() {
        if (lovCuentas == null) {
            lovCuentas = administrarEmplSolucionesNodos.buscarCuentas();
        }
    }

    public void cargarLovCuentaD() {
        if (lovCuentas == null) {
            lovCuentas = administrarEmplSolucionesNodos.buscarCuentas();
        }
    }

    public void asignarIndex(int dlg, int lnd) {
        tipoActualizacion = lnd;
        if (dlg == 0) {
            lovConceptos = null;
            cargarLovConceptos();
            RequestContext.getCurrentInstance().update("formLovConcepto:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
        } else if (dlg == 1) {
            lovTerceros = null;
            cargarLovTerceros();
            RequestContext.getCurrentInstance().update("formLovTercero:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
        } else if (dlg == 2) {
            lovCentrosCostos = null;
            cargarLovCC();
            RequestContext.getCurrentInstance().update("formLovCC:ccDialogo");
            RequestContext.getCurrentInstance().execute("PF('ccDialogo').show()");
        } else if (dlg == 3) {
            lovCentrosCostos = null;
            cargarLovCCD();
            RequestContext.getCurrentInstance().update("formLovCCD:ccdDialogo");
            RequestContext.getCurrentInstance().execute("PF('ccdDialogo').show()");
        } else if (dlg == 4) {
            lovCuentas = null;
            cargarLovCuentaD();
            RequestContext.getCurrentInstance().update("formLovCuentaD:cuentadDialogo");
            RequestContext.getCurrentInstance().execute("PF('cuentadDialogo').show()");
        } else if (dlg == 5) {
            lovCuentas = null;
            cargarLovCuentaD();
            RequestContext.getCurrentInstance().update("formLovCuentaC:cuentacDialogo");
            RequestContext.getCurrentInstance().execute("PF('cuentacDialogo').show()");
        } else if (dlg == 6) {
            lovProcesos = null;
            cargarLovProcesos();
            RequestContext.getCurrentInstance().update("formProceso:procesoDialogo");
            RequestContext.getCurrentInstance().execute("PF('procesoDialogo').show()");
        }
    }

    public void editarCelda() {
        if (snSeleccionado != null) {
            editarSn = snSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editConcepto");
                RequestContext.getCurrentInstance().execute("PF('editConcepto').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombreConcepto");
                RequestContext.getCurrentInstance().execute("PF('editNombreConcepto').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaInicial");
                RequestContext.getCurrentInstance().execute("PF('editFechaInicial').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaFinal");
                RequestContext.getCurrentInstance().execute("PF('editFechaFinal').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editValor");
                RequestContext.getCurrentInstance().execute("PF('editValor').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editSaldo");
                RequestContext.getCurrentInstance().execute("PF('editSaldo').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editUnidades");
                RequestContext.getCurrentInstance().execute("PF('editUnidades').show()");
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editEstado");
                RequestContext.getCurrentInstance().execute("PF('editEstado').show()");
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaPago");
                RequestContext.getCurrentInstance().execute("PF('editFechaPago').show()");
            } else if (cualCelda == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNit");
                RequestContext.getCurrentInstance().execute("PF('editNit').show()");
            } else if (cualCelda == 10) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTercero");
                RequestContext.getCurrentInstance().execute("PF('editTercero').show()");
            } else if (cualCelda == 11) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCCDebito");
                RequestContext.getCurrentInstance().execute("PF('editCCDebito').show()");
            } else if (cualCelda == 12) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCCCredito");
                RequestContext.getCurrentInstance().execute("PF('editCCCredito').show()");
            } else if (cualCelda == 13) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCuentaD");
                RequestContext.getCurrentInstance().execute("PF('editCuentaD').show()");
            } else if (cualCelda == 14) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCuentaC");
                RequestContext.getCurrentInstance().execute("PF('editCuentaC').show()");
            } else if (cualCelda == 16) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editProceso");
                RequestContext.getCurrentInstance().execute("PF('editProceso').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void limpiarNuevaSN() {
        nuevoSn = new SolucionesNodos();
    }

    public void limpiarduplicarSN() {
        duplicarSn = new SolucionesNodos();
    }

    /////////////////SETS Y GETS//////////////////
    public List<Empleados> getListaEmpleados() {
        if (listaEmpleados == null) {
            listaEmpleados = administrarEmplSolucionesNodos.emplSolucionesNodos();
        }
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public List<Empleados> getListaEmpleadosFiltrar() {
        return listaEmpleadosFiltrar;
    }

    public void setListaEmpleadosFiltrar(List<Empleados> listaEmpleadosFiltrar) {
        this.listaEmpleadosFiltrar = listaEmpleadosFiltrar;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public List<SolucionesNodos> getListaSN() {
        if (listaSN == null) {
            if (empleadoSeleccionado != null) {
                listaSN = administrarEmplSolucionesNodos.solucionesNodosEmpl(empleadoSeleccionado.getSecuencia());
            }
        }
        return listaSN;
    }

    public void setListaSN(List<SolucionesNodos> listaSN) {
        this.listaSN = listaSN;
    }

    public List<SolucionesNodos> getListaSNFiltrar() {
        return listaSNFiltrar;
    }

    public void setListaSNFiltrar(List<SolucionesNodos> listaSNFiltrar) {
        this.listaSNFiltrar = listaSNFiltrar;
    }

    public SolucionesNodos getNuevoSn() {
        return nuevoSn;
    }

    public void setNuevoSn(SolucionesNodos nuevoSn) {
        this.nuevoSn = nuevoSn;
    }

    public SolucionesNodos getDuplicarSn() {
        return duplicarSn;
    }

    public void setDuplicarSn(SolucionesNodos duplicarSn) {
        this.duplicarSn = duplicarSn;
    }

    public SolucionesNodos getEditarSn() {
        return editarSn;
    }

    public void setEditarSn(SolucionesNodos editarSn) {
        this.editarSn = editarSn;
    }

    public SolucionesNodos getSnSeleccionado() {
        return snSeleccionado;
    }

    public void setSnSeleccionado(SolucionesNodos snSeleccionado) {
        this.snSeleccionado = snSeleccionado;
    }

    public List<Empleados> getLovEmpleados() {
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empleados> getLovEmpleadosFiltrar() {
        return lovEmpleadosFiltrar;
    }

    public void setLovEmpleadosFiltrar(List<Empleados> lovEmpleadosFiltrar) {
        this.lovEmpleadosFiltrar = lovEmpleadosFiltrar;
    }

    public Empleados getEmpleadoLovSeleccionado() {
        return empleadoLovSeleccionado;
    }

    public void setEmpleadoLovSeleccionado(Empleados empleadoLovSeleccionado) {
        this.empleadoLovSeleccionado = empleadoLovSeleccionado;
    }

    public List<Conceptos> getLovConceptos() {
        return lovConceptos;
    }

    public void setLovConceptos(List<Conceptos> lovConceptos) {
        this.lovConceptos = lovConceptos;
    }

    public List<Conceptos> getLovConceptosFiltrar() {
        return lovConceptosFiltrar;
    }

    public void setLovConceptosFiltrar(List<Conceptos> lovConceptosFiltrar) {
        this.lovConceptosFiltrar = lovConceptosFiltrar;
    }

    public Conceptos getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(Conceptos conceptoSeleccionado) {
        this.conceptoSeleccionado = conceptoSeleccionado;
    }

    public List<Periodicidades> getLovPeriodicidades() {
        return lovPeriodicidades;
    }

    public void setLovPeriodicidades(List<Periodicidades> lovPeriodicidades) {
        this.lovPeriodicidades = lovPeriodicidades;
    }

    public List<Periodicidades> getLovPeriodicidadesFiltrar() {
        return lovPeriodicidadesFiltrar;
    }

    public void setLovPeriodicidadesFiltrar(List<Periodicidades> lovPeriodicidadesFiltrar) {
        this.lovPeriodicidadesFiltrar = lovPeriodicidadesFiltrar;
    }

    public Periodicidades getPeriodicidadSeleccionado() {
        return periodicidadSeleccionado;
    }

    public void setPeriodicidadSeleccionado(Periodicidades periodicidadSeleccionado) {
        this.periodicidadSeleccionado = periodicidadSeleccionado;
    }

    public List<Terceros> getLovTerceros() {
        return lovTerceros;
    }

    public void setLovTerceros(List<Terceros> lovTerceros) {
        this.lovTerceros = lovTerceros;
    }

    public List<Terceros> getLovTercerosFiltrar() {
        return lovTercerosFiltrar;
    }

    public void setLovTercerosFiltrar(List<Terceros> lovTercerosFiltrar) {
        this.lovTercerosFiltrar = lovTercerosFiltrar;
    }

    public Terceros getTerceroSeleccionado() {
        return terceroSeleccionado;
    }

    public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
        this.terceroSeleccionado = terceroSeleccionado;
    }

    public List<Cuentas> getLovCuentas() {
        return lovCuentas;
    }

    public void setLovCuentas(List<Cuentas> lovCuentas) {
        this.lovCuentas = lovCuentas;
    }

    public List<Cuentas> getLovCuentassFiltrar() {
        return lovCuentassFiltrar;
    }

    public void setLovCuentassFiltrar(List<Cuentas> lovCuentassFiltrar) {
        this.lovCuentassFiltrar = lovCuentassFiltrar;
    }

    public Cuentas getCuentaSeleccionada() {
        return cuentaSeleccionada;
    }

    public void setCuentaSeleccionada(Cuentas cuentaSeleccionada) {
        this.cuentaSeleccionada = cuentaSeleccionada;
    }

    public List<CentrosCostos> getLovCentrosCostos() {
        return lovCentrosCostos;
    }

    public void setLovCentrosCostos(List<CentrosCostos> lovCentrosCostos) {
        this.lovCentrosCostos = lovCentrosCostos;
    }

    public List<CentrosCostos> getLovCentrosCostosFiltrar() {
        return lovCentrosCostosFiltrar;
    }

    public void setLovCentrosCostosFiltrar(List<CentrosCostos> lovCentrosCostosFiltrar) {
        this.lovCentrosCostosFiltrar = lovCentrosCostosFiltrar;
    }

    public CentrosCostos getCentroCostoSeleccionado() {
        return centroCostoSeleccionado;
    }

    public void setCentroCostoSeleccionado(CentrosCostos centroCostoSeleccionado) {
        this.centroCostoSeleccionado = centroCostoSeleccionado;
    }

    public List<Procesos> getLovProcesos() {
        return lovProcesos;
    }

    public void setLovProcesos(List<Procesos> lovProcesos) {
        this.lovProcesos = lovProcesos;
    }

    public List<Procesos> getLovProcesosFiltrar() {
        return lovProcesosFiltrar;
    }

    public void setLovProcesosFiltrar(List<Procesos> lovProcesosFiltrar) {
        this.lovProcesosFiltrar = lovProcesosFiltrar;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTablaEmp() {
        return altoTablaEmp;
    }

    public void setAltoTablaEmp(String altoTablaEmp) {
        this.altoTablaEmp = altoTablaEmp;
    }

    public String getAltoTablaRegEmp() {
        return altoTablaRegEmp;
    }

    public void setAltoTablaRegEmp(String altoTablaRegEmp) {
        this.altoTablaRegEmp = altoTablaRegEmp;
    }

    public String getAltoTablaAux() {
        return altoTablaAux;
    }

    public void setAltoTablaAux(String altoTablaAux) {
        this.altoTablaAux = altoTablaAux;
    }

    public String getInfoRegistroEmpleados() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
        infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleados;
    }

    public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
        this.infoRegistroEmpleados = infoRegistroEmpleados;
    }

    public String getInfoRegistroSoluciones() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosSolucionesNodos");
        infoRegistroSoluciones = String.valueOf(tabla.getRowCount());
        return infoRegistroSoluciones;
    }

    public void setInfoRegistroSoluciones(String infoRegistroSoluciones) {
        this.infoRegistroSoluciones = infoRegistroSoluciones;
    }

    public String getInfoRegistroEmpleadosLOV() {
        return infoRegistroEmpleadosLOV;
    }

    public void setInfoRegistroEmpleadosLOV(String infoRegistroEmpleadosLOV) {
        this.infoRegistroEmpleadosLOV = infoRegistroEmpleadosLOV;
    }

    public String getInfoRegistroConcepto() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovConcepto:LOVConceptos");
        if (lovConceptosFiltrar != null) {
            if (lovConceptosFiltrar.size() == 1) {
                conceptoSeleccionado = lovConceptosFiltrar.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows();PF('LOVConceptos').selectRow(0);");
            } else {
                conceptoSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('LOVConceptos').unselectAllRows();");
            }
        } else {
            conceptoSeleccionado = null;
            aceptar = true;
        }
        infoRegistroConcepto = String.valueOf(tabla.getRowCount());
        return infoRegistroConcepto;
    }

    public void setInfoRegistroConcepto(String infoRegistroConcepto) {
        this.infoRegistroConcepto = infoRegistroConcepto;
    }

    public String getInfoRegistroPeriodicidad() {
        return infoRegistroPeriodicidad;
    }

    public void setInfoRegistroPeriodicidad(String infoRegistroPeriodicidad) {
        this.infoRegistroPeriodicidad = infoRegistroPeriodicidad;
    }

    public String getInfoRegistroTercero() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovTercero:LOVTerceros");
        if (lovTercerosFiltrar != null) {
            if (lovTercerosFiltrar.size() == 1) {
                terceroSeleccionado = lovTercerosFiltrar.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();PF('LOVTerceros').selectRow(0);");
            } else {
                terceroSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('LOVTerceros').unselectAllRows();");
            }
        } else {
            conceptoSeleccionado = null;
            aceptar = true;
        }
        infoRegistroTercero = String.valueOf(tabla.getRowCount());
        return infoRegistroTercero;
    }

    public void setInfoRegistroTercero(String infoRegistroTercero) {
        this.infoRegistroTercero = infoRegistroTercero;
    }

    public String getInfoRegistroCentroCostoD() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovCCD:LOVCCD");
        if (lovCentrosCostosFiltrar != null) {
            if (lovCentrosCostosFiltrar.size() == 1) {
                centroCostoSeleccionado = lovCentrosCostosFiltrar.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVCCD').unselectAllRows();PF('LOVCCD').selectRow(0);");
            } else {
                centroCostoSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('LOVCCD').unselectAllRows();");
            }
        } else {
            centroCostoSeleccionado = null;
            aceptar = true;
        }
        infoRegistroCentroCostoD = String.valueOf(tabla.getRowCount());
        return infoRegistroCentroCostoD;
    }

    public void setInfoRegistroCentroCostoD(String infoRegistroCentroCostoD) {
        this.infoRegistroCentroCostoD = infoRegistroCentroCostoD;
    }

    public String getInfoRegistroCuenta() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovCuentaC:LOVCuentaC");
        if (lovCuentassFiltrar != null) {
            if (lovCuentassFiltrar.size() == 1) {
                cuentaSeleccionada = lovCuentassFiltrar.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVCuentaC').unselectAllRows();PF('LOVCuentaC').selectRow(0);");
            } else {
                cuentaSeleccionada = null;
                RequestContext.getCurrentInstance().execute("PF('LOVCuentaC').unselectAllRows();");
            }
        } else {
            cuentaSeleccionada = null;
            aceptar = true;
        }
        infoRegistroCuenta = String.valueOf(tabla.getRowCount());
        return infoRegistroCuenta;
    }

    public void setInfoRegistroCuenta(String infoRegistroCuenta) {
        this.infoRegistroCuenta = infoRegistroCuenta;
    }

    public String getInfoRegistroCuentaD() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovCuentaD:LOVCuentaD");
        if (lovCuentassFiltrar != null) {
            if (lovCuentassFiltrar.size() == 1) {
                cuentaSeleccionada = lovCuentassFiltrar.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVCuentaD').unselectAllRows();PF('LOVCuentaD').selectRow(0);");
            } else {
                cuentaSeleccionada = null;
                RequestContext.getCurrentInstance().execute("PF('LOVCuentaD').unselectAllRows();");
            }
        } else {
            cuentaSeleccionada = null;
            aceptar = true;
        }
        infoRegistroCuentaD = String.valueOf(tabla.getRowCount());
        return infoRegistroCuentaD;
    }

    public void setInfoRegistroCuentaD(String infoRegistroCuentaD) {
        this.infoRegistroCuentaD = infoRegistroCuentaD;
    }

    public String getInfoRegistroCentroCosto() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formLovCC:LOVCC");
        if (lovCentrosCostosFiltrar != null) {
            if (lovCentrosCostosFiltrar.size() == 1) {
                centroCostoSeleccionado = lovCentrosCostosFiltrar.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVCC').unselectAllRows();PF('LOVCC').selectRow(0);");
            } else {
                centroCostoSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('LOVCC').unselectAllRows();");
            }
        } else {
            centroCostoSeleccionado = null;
            aceptar = true;
        }
        infoRegistroCentroCosto = String.valueOf(tabla.getRowCount());
        return infoRegistroCentroCosto;
    }

    public void setInfoRegistroCentroCosto(String infoRegistroCentroCosto) {
        this.infoRegistroCentroCosto = infoRegistroCentroCosto;
    }

    public String getInfoRegistroProceso() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formProceso:LOVProcesos");
        if (lovProcesosFiltrar != null) {
            if (lovProcesosFiltrar.size() == 1) {
                procesoSeleccionado = lovProcesosFiltrar.get(0);
                aceptar = false;
                RequestContext.getCurrentInstance().execute("PF('LOVProcesos').unselectAllRows();PF('LOVProcesos').selectRow(0);");
            } else {
                procesoSeleccionado = null;
                RequestContext.getCurrentInstance().execute("PF('LOVProcesos').unselectAllRows();");
            }
        } else {
            procesoSeleccionado = null;
            aceptar = true;
        }
        infoRegistroProceso = String.valueOf(tabla.getRowCount());
        return infoRegistroProceso;
    }

    public void setInfoRegistroProceso(String infoRegistroProceso) {
        this.infoRegistroProceso = infoRegistroProceso;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public boolean isActivarMTodos() {
        return activarMTodos;
    }

    public void setActivarMTodos(boolean activarMTodos) {
        this.activarMTodos = activarMTodos;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

    public Procesos getProcesoSeleccionado() {
        return procesoSeleccionado;
    }

    public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
        this.procesoSeleccionado = procesoSeleccionado;
    }

}
