/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.CentrosCostos;
import Entidades.Cuentas;
import Entidades.Proyecciones;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.Terceros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarProyeccionesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlProyecciones implements Serializable {

    @EJB
    AdministrarProyeccionesInterface administrarProyecciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
//EMPRESA
    private List<Empleados> listaEmpleados;
    private List<Empleados> filtradoListaEmpleados;

    private Empleados empleadoSeleccionado;
    private int banderaModificacionEmpresa;
    private int indiceEmpresaMostrada;
//LISTA CENTRO COSTO
    private List<Proyecciones> listProyecciones;
    private List<Proyecciones> filtrarProyecciones;
    private List<Proyecciones> borrarProyecciones;
    private Proyecciones editarProyeccion;
    private Proyecciones nuevaProyeccion;

    private Column descripcionConcepto,
            nombreEmpleado,
            fechaDesde,
            fechaHasta,
            valor,
            formula,
            centroCosto,
            codigoCuentaC,
            descripcionCuentaC,
            codigoCuentaD,
            descripcionCuentaD,
            nit,
            nitNombre;

    private Proyecciones ProyeccionesSeleccionada;

    private int tamano;

    public ControlProyecciones() {
        borrarProyecciones = new ArrayList<Proyecciones>();
        permitirIndex = true;
        listaEmpleados = null;
        empleadoSeleccionado = new Empleados();
        indiceEmpresaMostrada = 0;
        listProyecciones = null;
        editarProyeccion = new Proyecciones();
        nuevaProyeccion = new Proyecciones();
        aceptar = true;
        filtradoListaEmpleados = null;
        guardado = true;
        tamano = 270;
        buscarCentrocosto = false;
        mostrartodos = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarProyecciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    private String paginaAnterior;

    public void recibirAtras(String atras) {
        paginaAnterior = atras;
        System.out.println("ControProyecciones pagina anterior : " + paginaAnterior);
    }

    public String redireccionarAtras() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A CONTROLBETAPROYECCIONES.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarProyecciones.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

        } catch (Exception e) {
            System.out.println("ERROR CONTROLBETAPROYECCIONES eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    private String backUpDescripcionConcepto;
    private String backUpNombreEmpleado;
    private Date backUpFechaDesde;
    private Date backUpFechaHasta;
    private BigDecimal backUpValor;
    private String backUpFormula;
    private String backUpCentroCosto;
    private String backUpCodigoCuentaC;
    private String backUpCuentaC;
    private String backUpCuentaD;
    private String backUpCodigoCuentaD;
    private Long backUpNit;
    private String backUpNitNombre;

    public void cambiarIndice(int indice, int celda) {
        System.err.println("BETA CENTRO COSTO TIPO LISTA = " + tipoLista);
        System.err.println("PERMITIR INDEX = " + permitirIndex);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            System.err.println("CAMBIAR INDICE CUALCELDA = " + cualCelda);
            secRegistro = listProyecciones.get(index).getSecuencia();
            System.err.println("Sec Registro = " + secRegistro);
            if (tipoLista == 0) {
                backUpDescripcionConcepto = listProyecciones.get(index).getConcepto().getDescripcion();
                backUpNombreEmpleado = listProyecciones.get(index).getEmpleado().getPersona().getNombreCompleto();
                backUpFechaDesde = listProyecciones.get(index).getFechaDesde();
                backUpFechaHasta = listProyecciones.get(index).getFechaHasta();
                backUpValor = listProyecciones.get(index).getValor();
                backUpFormula = listProyecciones.get(index).getFormula().getNombrelargo();
                backUpCentroCosto = listProyecciones.get(index).getCentroCosto().getNombre();
                backUpCodigoCuentaC = listProyecciones.get(index).getCuentaC().getCodigo();
                backUpCuentaC = listProyecciones.get(index).getCuentaC().getDescripcion();
                backUpCodigoCuentaD = listProyecciones.get(index).getCuentaD().getCodigo();
                backUpCuentaD = listProyecciones.get(index).getCuentaD().getDescripcion();
                backUpNit = listProyecciones.get(index).getNit().getCodigo();
                backUpNitNombre = listProyecciones.get(index).getNit().getNombre();

            } else {
                backUpDescripcionConcepto = filtrarProyecciones.get(index).getConcepto().getDescripcion();
                backUpNombreEmpleado = filtrarProyecciones.get(index).getEmpleado().getPersona().getNombreCompleto();
                backUpFechaDesde = filtrarProyecciones.get(index).getFechaDesde();
                backUpFechaHasta = filtrarProyecciones.get(index).getFechaHasta();
                backUpValor = filtrarProyecciones.get(index).getValor();
                backUpFormula = filtrarProyecciones.get(index).getFormula().getNombrelargo();
                backUpCentroCosto = filtrarProyecciones.get(index).getCentroCosto().getNombre();
                backUpCodigoCuentaC = filtrarProyecciones.get(index).getCuentaC().getCodigo();
                backUpCuentaC = filtrarProyecciones.get(index).getCuentaC().getDescripcion();
                backUpCodigoCuentaD = filtrarProyecciones.get(index).getCuentaD().getCodigo();
                backUpCuentaD = filtrarProyecciones.get(index).getCuentaD().getDescripcion();
                backUpNit = filtrarProyecciones.get(index).getNit().getCodigo();
                backUpNitNombre = filtrarProyecciones.get(index).getNit().getNombre();

            }

            System.out.println("Indice: " + index + " Celda: " + cualCelda);
        }
    }

    public void modificandoProyecciones(int indice, String confirmarCambio, String valorConfirmar) {

        System.err.println("ENTRE A MODIFICANDOPROYECCIONES INDEX : " + indice);
        index = indice;
        Short a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR CENTROCOSTO, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                listProyecciones.get(index).getConcepto().setDescripcion(backUpDescripcionConcepto);
                listProyecciones.get(index).getEmpleado().getPersona().setNombreCompleto(backUpNombreEmpleado);
                listProyecciones.get(index).getEmpleado().getPersona().setNombreCompleto(backUpNombreEmpleado);
                listProyecciones.get(index).setValor(backUpValor);
                listProyecciones.get(index).getFormula().setNombrelargo(backUpFormula);
                listProyecciones.get(index).getCentroCosto().setNombre(backUpCentroCosto);
                listProyecciones.get(index).getCuentaC().setCodigo(backUpCodigoCuentaC);
                listProyecciones.get(index).getCuentaC().setDescripcion(backUpCuentaC);
                listProyecciones.get(index).getCuentaD().setCodigo(backUpCodigoCuentaD);
                listProyecciones.get(index).getCuentaD().setDescripcion(backUpCuentaD);
                listProyecciones.get(index).getNit().setCodigo(backUpNit);
                listProyecciones.get(index).getNit().setNombre(backUpNitNombre);
                index = -1;
                secRegistro = null;

            } else {
                filtrarProyecciones.get(index).getConcepto().setDescripcion(backUpDescripcionConcepto);
                filtrarProyecciones.get(index).getEmpleado().getPersona().setNombreCompleto(backUpNombreEmpleado);
                filtrarProyecciones.get(index).getEmpleado().getPersona().setNombreCompleto(backUpNombreEmpleado);
                filtrarProyecciones.get(index).setValor(backUpValor);
                filtrarProyecciones.get(index).getFormula().setNombrelargo(backUpFormula);
                filtrarProyecciones.get(index).getCentroCosto().setNombre(backUpCentroCosto);
                filtrarProyecciones.get(index).getCuentaC().setCodigo(backUpCodigoCuentaC);
                filtrarProyecciones.get(index).getCuentaC().setDescripcion(backUpCuentaC);
                filtrarProyecciones.get(index).getCuentaD().setCodigo(backUpCodigoCuentaD);
                filtrarProyecciones.get(index).getCuentaD().setDescripcion(backUpCuentaD);
                filtrarProyecciones.get(index).getNit().setCodigo(backUpNit);
                filtrarProyecciones.get(index).getNit().setNombre(backUpNitNombre);
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosProyecciones");
        }
        RequestContext.getCurrentInstance().update("form:datosProyecciones");

    }

    public void mostrarInfo(int indice, int celda) {
        int contador = 0;
        int fechas = 0;
        mensajeValidacion = " ";
        index = indice;
        cualCelda = celda;
        System.out.println("Entre a mostrarInfo");
        RequestContext context = RequestContext.getCurrentInstance();
        if (permitirIndex == true) {
            secRegistro = listProyecciones.get(index).getSecuencia();
            listProyecciones.get(indice).setFechaDesde(backUpFechaDesde);
            listProyecciones.get(indice).setFechaHasta(backUpFechaHasta);
            index = -1;
            secRegistro = null;
        } else {

            secRegistro = filtrarProyecciones.get(index).getSecuencia();
            filtrarProyecciones.get(indice).setFechaDesde(backUpFechaDesde);
            filtrarProyecciones.get(indice).setFechaHasta(backUpFechaHasta);
            contador++;

            index = -1;
            secRegistro = null;
        }
        RequestContext.getCurrentInstance().update("form:datosProyecciones");

    }

    public void cancelarModificacion() {
        //try {
        System.out.println("entre a CONTROLBETAPROYECCIONES.cancelarModificacion");
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {

            descripcionConcepto = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionConcepto");
            descripcionConcepto.setFilterStyle("display: none; visibility: hidden;");
            //1
            nombreEmpleado = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nombreEmpleado");
            nombreEmpleado.setFilterStyle("display: none; visibility: hidden;");
            //2
            fechaDesde = (Column) c.getViewRoot().findComponent("form:datosProyecciones:fechaDesde");
            fechaDesde.setFilterStyle("display: none; visibility: hidden;");
            //3 
            fechaHasta = (Column) c.getViewRoot().findComponent("form:datosProyecciones:fechaHasta");
            fechaHasta.setFilterStyle("display: none; visibility: hidden;");
            //4
            valor = (Column) c.getViewRoot().findComponent("form:datosProyecciones:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            //5 
            formula = (Column) c.getViewRoot().findComponent("form:datosProyecciones:formula");
            formula.setFilterStyle("display: none; visibility: hidden;");
            //6
            centroCosto = (Column) c.getViewRoot().findComponent("form:datosProyecciones:centroCosto");
            centroCosto.setFilterStyle("display: none; visibility: hidden;");
            //7 
            codigoCuentaC = (Column) c.getViewRoot().findComponent("form:datosProyecciones:codigoCuentaC");
            codigoCuentaC.setFilterStyle("display: none; visibility: hidden;");
            descripcionCuentaC = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionCuentaC");
            descripcionCuentaC.setFilterStyle("display: none; visibility: hidden;");
            codigoCuentaD = (Column) c.getViewRoot().findComponent("form:datosProyecciones:codigoCuentaD");
            codigoCuentaD.setFilterStyle("display: none; visibility: hidden;");
            descripcionCuentaD = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionCuentaD");
            descripcionCuentaD.setFilterStyle("display: none; visibility: hidden;");
            nit = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nit");
            nit.setFilterStyle("display: none; visibility: hidden;");
            nitNombre = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nitNombre");
            nitNombre.setFilterStyle("display: none; visibility: hidden;");
            tamano = 270;
            bandera = 0;
            filtrarProyecciones = null;
            tipoLista = 0;
        }
        if (borrarProyecciones != null) {
            borrarProyecciones.clear();
        }
        index = -1;
        k = 0;
        listProyecciones = null;
        guardado = true;
        permitirIndex = true;
        buscarCentrocosto = false;
        mostrartodos = true;
        RequestContext context = RequestContext.getCurrentInstance();
        empleadoSeleccionado = new Empleados();
        empleadoSeleccionado.setSecuencia(null);
        getListProyecciones();
        if (listProyecciones == null || listProyecciones.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listProyecciones.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosProyecciones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        //} catch (Exception E) {
        //  System.out.println("ERROR CONTROLBETAPROYECCIONES.ModificarModificacion ERROR====================" + E.getMessage());
        //}
    }

    public void salir() {
        try {
            System.out.println("entre a CONTROLBETAPROYECCIONES.Salir");
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {

                descripcionConcepto = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionConcepto");
                descripcionConcepto.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombreEmpleado = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nombreEmpleado");
                nombreEmpleado.setFilterStyle("display: none; visibility: hidden;");
                //2
                fechaDesde = (Column) c.getViewRoot().findComponent("form:datosProyecciones:fechaDesde");
                fechaDesde.setFilterStyle("display: none; visibility: hidden;");
                //3 
                fechaHasta = (Column) c.getViewRoot().findComponent("form:datosProyecciones:fechaHasta");
                fechaHasta.setFilterStyle("display: none; visibility: hidden;");
                //4
                valor = (Column) c.getViewRoot().findComponent("form:datosProyecciones:valor");
                valor.setFilterStyle("display: none; visibility: hidden;");
                //5 
                formula = (Column) c.getViewRoot().findComponent("form:datosProyecciones:formula");
                formula.setFilterStyle("display: none; visibility: hidden;");
                //6
                centroCosto = (Column) c.getViewRoot().findComponent("form:datosProyecciones:centroCosto");
                centroCosto.setFilterStyle("display: none; visibility: hidden;");
                //7 
                codigoCuentaC = (Column) c.getViewRoot().findComponent("form:datosProyecciones:codigoCuentaC");
                codigoCuentaC.setFilterStyle("display: none; visibility: hidden;");
                descripcionCuentaC = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionCuentaC");
                descripcionCuentaC.setFilterStyle("display: none; visibility: hidden;");
                codigoCuentaD = (Column) c.getViewRoot().findComponent("form:datosProyecciones:codigoCuentaD");
                codigoCuentaD.setFilterStyle("display: none; visibility: hidden;");
                descripcionCuentaD = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionCuentaD");
                descripcionCuentaD.setFilterStyle("display: none; visibility: hidden;");
                nit = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nit");
                nit.setFilterStyle("display: none; visibility: hidden;");
                nitNombre = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nitNombre");
                nitNombre.setFilterStyle("display: none; visibility: hidden;");
                tamano = 270;
                bandera = 0;
                filtrarProyecciones = null;
                tipoLista = 0;
            }

            borrarProyecciones.clear();
            index = -1;
            k = 0;
            listProyecciones = null;
            guardado = true;
            permitirIndex = true;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosProyecciones");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception E) {
            System.out.println("ERROR CONTROLBETAPROYECCIONES.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A CONTROLBETAPROYECCIONES.asignarIndex \n");
            index = indice;
            RequestContext context = RequestContext.getCurrentInstance();

            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dig == 2) {
                RequestContext.getCurrentInstance().update("form:tiposProyeccionesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposProyeccionesDialogo').show()");
                dig = -1;
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLBETAPROYECCIONES.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    private boolean buscarCentrocosto;
    private boolean mostrartodos;

    public void asignarVariableTiposCC(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tiposProyeccionesDialogo");
        RequestContext.getCurrentInstance().execute("PF('tiposProyeccionesDialogo').show()");
    }

    public void limpiarNuevoProyecciones() {
        System.out.println("\n ENTRE A CONTROLBETAPROYECCIONES.limpiarNuevoProyecciones \n");
        try {
            nuevaProyeccion = new Proyecciones();
            nuevaProyeccion.setEmpleado(new Empleados());
            nuevaProyeccion.setFormula(new Formulas());
            nuevaProyeccion.setEmpleado(new Empleados());
            nuevaProyeccion.setCuentaC(new Cuentas());
            nuevaProyeccion.setCuentaD(new Cuentas());
            nuevaProyeccion.setNit(new Terceros());
            index = -1;
        } catch (Exception e) {
            System.out.println("Error CONTROLBETAPROYECCIONES.LimpiarNuevoProyecciones ERROR=============================" + e.getMessage());
        }
    }

    public void mostrarDialogoListaEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        index = -1;
        RequestContext.getCurrentInstance().execute("PF('buscarProyeccionesDialogo').show()");
    }

    public void borrandoProyecciones() {
        try {
            banderaModificacionEmpresa = 1;
            if (index >= 0) {
                if (tipoLista == 0) {
                    borrarProyecciones.add(listProyecciones.get(index));
                    listProyecciones.remove(index);
                }
                if (tipoLista == 1) {
                    borrarProyecciones.add(filtrarProyecciones.get(index));
                    int VCIndex = listProyecciones.indexOf(filtrarProyecciones.get(index));
                    listProyecciones.remove(VCIndex);
                    filtrarProyecciones.remove(index);
                }

                RequestContext context = RequestContext.getCurrentInstance();
                index = -1;
                System.err.println("verificar Borrado " + guardado);
                if (guardado == true) {
                    guardado = false;
                }
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosProyecciones");
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLBETAPROYECCIONES.BorrarProyecciones ERROR=====================" + e.getMessage());
        }
    }

    public void guardarCambiosProyecciones() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Localizacion");
            if (!borrarProyecciones.isEmpty()) {
                administrarProyecciones.borrarProyecciones(borrarProyecciones);
                //mostrarBorrados
                registrosBorrados = borrarProyecciones.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarProyecciones.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            k = 0;
            guardado = true;
            index = -1;
            listProyecciones = null;
            permitirIndex = true;
            buscarCentrocosto = false;
            mostrartodos = true;
            empleadoSeleccionado = new Empleados();
            empleadoSeleccionado.setSecuencia(null);
            getListProyecciones();
            if (listProyecciones == null || listProyecciones.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listProyecciones.size();
            }
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            banderaModificacionEmpresa = 0;
            RequestContext.getCurrentInstance().update("form:datosProyecciones");
        }
    }

    public void activarCtrlF11() {
        System.out.println("\n ENTRE A CONTROLBETAPROYECCIONES.activarCtrlF11 \n");
        try {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 0) {
                tamano = 250;
                System.out.println("Activar");

                descripcionConcepto = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionConcepto");
                descripcionConcepto.setFilterStyle("width: 85% !important;");
                //1
                nombreEmpleado = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nombreEmpleado");
                nombreEmpleado.setFilterStyle("width: 85% !important;");
                //2
                fechaDesde = (Column) c.getViewRoot().findComponent("form:datosProyecciones:fechaDesde");
                fechaDesde.setFilterStyle("width: 85% !important;");
                //3 
                fechaHasta = (Column) c.getViewRoot().findComponent("form:datosProyecciones:fechaHasta");
                fechaHasta.setFilterStyle("width: 85% !important;");
                //4
                valor = (Column) c.getViewRoot().findComponent("form:datosProyecciones:valor");
                valor.setFilterStyle("width: 85% !important;");
                //5 
                formula = (Column) c.getViewRoot().findComponent("form:datosProyecciones:formula");
                formula.setFilterStyle("width: 85% !important;");
                //6
                centroCosto = (Column) c.getViewRoot().findComponent("form:datosProyecciones:centroCosto");
                centroCosto.setFilterStyle("width: 85% !important;");
                //7 
                codigoCuentaC = (Column) c.getViewRoot().findComponent("form:datosProyecciones:codigoCuentaC");
                codigoCuentaC.setFilterStyle("width: 85% !important;");

                descripcionCuentaC = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionCuentaC");
                descripcionCuentaC.setFilterStyle("width: 85% !important;");

                codigoCuentaD = (Column) c.getViewRoot().findComponent("form:datosProyecciones:codigoCuentaD");
                codigoCuentaD.setFilterStyle("width: 85% !important;");

                descripcionCuentaD = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionCuentaD");
                descripcionCuentaD.setFilterStyle("width: 85% !important;");

                nit = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nit");
                nit.setFilterStyle("width: 85% !important;");

                nitNombre = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nitNombre");
                nitNombre.setFilterStyle("width: 85% !important;");

                RequestContext.getCurrentInstance().update("form:datosProyecciones");
                bandera = 1;
            } else if (bandera == 1) {
                System.out.println("Desactivar");

                descripcionConcepto = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionConcepto");
                descripcionConcepto.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombreEmpleado = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nombreEmpleado");
                nombreEmpleado.setFilterStyle("display: none; visibility: hidden;");
                //2
                fechaDesde = (Column) c.getViewRoot().findComponent("form:datosProyecciones:fechaDesde");
                fechaDesde.setFilterStyle("display: none; visibility: hidden;");
                //3 
                fechaHasta = (Column) c.getViewRoot().findComponent("form:datosProyecciones:fechaHasta");
                fechaHasta.setFilterStyle("display: none; visibility: hidden;");
                //4
                valor = (Column) c.getViewRoot().findComponent("form:datosProyecciones:valor");
                valor.setFilterStyle("display: none; visibility: hidden;");
                //5 
                formula = (Column) c.getViewRoot().findComponent("form:datosProyecciones:formula");
                formula.setFilterStyle("display: none; visibility: hidden;");
                //6
                centroCosto = (Column) c.getViewRoot().findComponent("form:datosProyecciones:centroCosto");
                centroCosto.setFilterStyle("display: none; visibility: hidden;");
                //7 
                codigoCuentaC = (Column) c.getViewRoot().findComponent("form:datosProyecciones:codigoCuentaC");
                codigoCuentaC.setFilterStyle("display: none; visibility: hidden;");
                descripcionCuentaC = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionCuentaC");
                descripcionCuentaC.setFilterStyle("display: none; visibility: hidden;");
                codigoCuentaD = (Column) c.getViewRoot().findComponent("form:datosProyecciones:codigoCuentaD");
                codigoCuentaD.setFilterStyle("display: none; visibility: hidden;");
                descripcionCuentaD = (Column) c.getViewRoot().findComponent("form:datosProyecciones:descripcionCuentaD");
                descripcionCuentaD.setFilterStyle("display: none; visibility: hidden;");
                nit = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nit");
                nit.setFilterStyle("display: none; visibility: hidden;");
                nitNombre = (Column) c.getViewRoot().findComponent("form:datosProyecciones:nitNombre");
                nitNombre.setFilterStyle("display: none; visibility: hidden;");
                tamano = 270;
                RequestContext.getCurrentInstance().update("form:datosProyecciones");
                bandera = 0;
                filtrarProyecciones = null;
                tipoLista = 0;
            }
        } catch (Exception e) {

            System.out.println("ERROR CONTROLBETAPROYECCIONES.activarCtrlF11 ERROR====================" + e.getMessage());
        }
    }

    public void editarCelda() {
        try {
            System.out.println("\n ENTRE A editarCelda INDEX  " + index);
            if (index >= 0) {
                System.out.println("\n ENTRE AeditarCelda TIPOLISTA " + tipoLista);
                if (tipoLista == 0) {
                    editarProyeccion = listProyecciones.get(index);
                }
                if (tipoLista == 1) {
                    editarProyeccion = filtrarProyecciones.get(index);
                }
                RequestContext context = RequestContext.getCurrentInstance();
                System.out.println("CONTROLBETAPROYECCIONES: Entro a editar... valor celda: " + cualCelda);
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionConceptoD");
                    RequestContext.getCurrentInstance().execute("PF('editarDescripcionConceptoD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreEmpleadoD");
                    RequestContext.getCurrentInstance().execute("PF('editarNombreEmpleadoD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaDesdeD");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaDesdeD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHastaD");
                    RequestContext.getCurrentInstance().execute("PF('editarFechaHastaD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarValorD");
                    RequestContext.getCurrentInstance().execute("PF('editarValorD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaD");
                    RequestContext.getCurrentInstance().execute("PF('editarFormulaD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCentroCostoD");
                    RequestContext.getCurrentInstance().execute("PF('editarCentroCostoD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 7) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoCuentaCD");
                    RequestContext.getCurrentInstance().execute("PF('editarCodigoCuentaCD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 8) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionCuentaCD");
                    RequestContext.getCurrentInstance().execute("PF('editarDescripcionCuentaCD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 9) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoCuentaDD");
                    RequestContext.getCurrentInstance().execute("PF('editarCodigoCuentaDD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 10) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionCuentaDD");
                    RequestContext.getCurrentInstance().execute("PF('editarDescripcionCuentaDD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 11) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNitCodigoD");
                    RequestContext.getCurrentInstance().execute("PF('editarNitCodigoD').show()");
                    cualCelda = -1;
                } else if (cualCelda == 12) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNitNombreD");
                    RequestContext.getCurrentInstance().execute("PF('editarNitNombreD').show()");
                    cualCelda = -1;
                }
            }
            index = -1;
        } catch (Exception E) {
            System.out.println("ERROR CONTROLBETAPROYECCIONES.editarCelDa ERROR=====================" + E.getMessage());
        }
    }

    public void listaValoresBoton() {

        try {
            if (index >= 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 2) {
                    System.out.println("\n ListaValoresBoton \n");
                    RequestContext.getCurrentInstance().update("formularioDialogos:tiposProyeccionesDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tiposProyeccionesDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        } catch (Exception e) {
            System.out.println("\n ERROR CONTROLBETAPROYECCIONES.listaValoresBoton ERROR====================" + e.getMessage());

        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosProyeccionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Proyecciones", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    /**
     *
     * @throws IOException
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosProyeccionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Proyecciones", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listProyecciones.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "PROYECCIONES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("PROYECCIONES")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    public void lovEmpleados() {
        index = -1;
        secRegistro = null;
        cualCelda = -1;
        RequestContext.getCurrentInstance().execute("PF('EmpleadosDialogo').show()");
    }

    public void cambiarEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("Cambiar empresa  GUARDADO = " + guardado);
        System.err.println("Cambiar empresa  GUARDADO = " + empleadoSeleccionado.getPersona().getNombreCompleto());
        if (guardado == true) {
            RequestContext.getCurrentInstance().update("form:nombreEmpresa");
            RequestContext.getCurrentInstance().update("form:nitEmpresa");
            filtradoListaEmpleados = null;
            listProyecciones = null;
            aceptar = true;
            mostrartodos = false;
            buscarCentrocosto = true;
            context.reset("formularioDialogos:lovEmpleados:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('lovEmpleados').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('EmpleadosDialogo').hide()");
            //RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpleados");
            //RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
            RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
            RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
            banderaModificacionEmpresa = 0;
            RequestContext.getCurrentInstance().update("form:datosProyecciones");
            RequestContext.getCurrentInstance().update("formularioDialogos:lovProyecciones");

        } else {
            banderaModificacionEmpresa = 0;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cancelarCambioEmpleado() {
        filtradoListaEmpleados = null;
        banderaModificacionEmpresa = 0;
        index = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpleadosDialogo').hide()");
    }
//-----------------------------------------------------------------------------**

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }
    private String infoRegistroEmpleados;

    public List<Empleados> getListaEmpleados() {
        try {
            if (listaEmpleados == null) {
                listaEmpleados = administrarProyecciones.consultarLOVEmpleados();
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listProyecciones == null || listaEmpleados.isEmpty()) {
                infoRegistroEmpleados = "Cantidad de registros: 0 ";
            } else {
                infoRegistroEmpleados = "Cantidad de registros: " + listaEmpleados.size();
            }
            RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
            return listaEmpleados;
        } catch (Exception e) {
            System.out.println("ERRO LISTA EMPRESAS " + e);
            return null;
        }
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public List<Empleados> getFiltradoListaEmpleados() {
        return filtradoListaEmpleados;
    }

    public void setFiltradoListaEmpleados(List<Empleados> filtradoListaEmpleados) {
        this.filtradoListaEmpleados = filtradoListaEmpleados;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    private String infoRegistro;

    public List<Proyecciones> getListProyecciones() {
        try {
            if (listProyecciones == null) {
                listProyecciones = administrarProyecciones.consultarProyeccionesEmpleado(empleadoSeleccionado.getSecuencia());
            } else {
                System.out.println(".-.");
            }
            for (int z = 0; z < listProyecciones.size(); z++) {
                if (listProyecciones.get(z).getCentroCosto() == null) {
                    listProyecciones.get(z).setCentroCosto(new CentrosCostos());
                }
                if (listProyecciones.get(z).getEmpleado() == null) {
                    listProyecciones.get(z).setEmpleado(new Empleados());
                }
                if (listProyecciones.get(z).getFormula() == null) {
                    listProyecciones.get(z).setFormula(new Formulas());
                }
                if (listProyecciones.get(z).getCuentaC() == null) {
                    listProyecciones.get(z).setCuentaC(new Cuentas());
                }
                if (listProyecciones.get(z).getCuentaD() == null) {
                    listProyecciones.get(z).setCuentaD(new Cuentas());
                }
                if (listProyecciones.get(z).getNit() == null) {
                    listProyecciones.get(z).setNit(new Terceros());
                }
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listProyecciones == null || listProyecciones.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listProyecciones.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            return listProyecciones;
        } catch (Exception e) {
            System.out.println(" BETA  BETA ControlCentrosCosto: Error al recibir los Proyecciones de la empresa seleccionada /n" + e.getMessage());
            return null;
        }
    }

    public void setListProyecciones(List<Proyecciones> listProyecciones) {
        this.listProyecciones = listProyecciones;
    }

    public List<Proyecciones> getFiltrarProyecciones() {
        return filtrarProyecciones;
    }

    public void setFiltrarProyecciones(List<Proyecciones> filtrarProyecciones) {
        this.filtrarProyecciones = filtrarProyecciones;
    }

    private String infoRegistroTiposEmpleados;

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public Proyecciones getProyeccionesSeleccionada() {
        return ProyeccionesSeleccionada;
    }

    public void setProyeccionesSeleccionada(Proyecciones ProyeccionesSeleccionada) {
        this.ProyeccionesSeleccionada = ProyeccionesSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroEmpleados() {
        return infoRegistroEmpleados;
    }

    public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
        this.infoRegistroEmpleados = infoRegistroEmpleados;
    }

    public String getInfoRegistroTiposEmpleados() {
        return infoRegistroTiposEmpleados;
    }

    public void setInfoRegistroTiposEmpleados(String infoRegistroTiposEmpleados) {
        this.infoRegistroTiposEmpleados = infoRegistroTiposEmpleados;
    }

    public boolean isBuscarCentrocosto() {
        return buscarCentrocosto;
    }

    public void setBuscarCentrocosto(boolean buscarCentrocosto) {
        this.buscarCentrocosto = buscarCentrocosto;
    }

    public boolean isMostrartodos() {
        return mostrartodos;
    }

    public void setMostrartodos(boolean mostrartodos) {
        this.mostrartodos = mostrartodos;
    }

    public Proyecciones getEditarProyeccion() {
        return editarProyeccion;
    }

    public void setEditarProyeccion(Proyecciones editarProyeccion) {
        this.editarProyeccion = editarProyeccion;
    }

}
