/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmpresasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
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
public class ControlPeriodosActivos implements Serializable {

    @EJB
    AdministrarEmpresasInterface administrarEmpresas;
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
    private List<Empresas> listaEmpresas;
    private List<Empresas> filtradoListaEmpresas;

    private Empresas empresaSeleccionada;
    private int banderaModificacionEmpresa;
    private int indiceEmpresaMostrada;
//LISTA CENTRO COSTO
    private List<Empresas> listEmpresa;
    private List<Empresas> listEmpresaPorBoton;
    private List<Empresas> filtrarEmpresas;
    private List<Empresas> modificarEmpresas;
    private Empresas editarEmpresas;
    private Empresas mostrarEmpresas;
    private Empresas nuevoEmpresas;

    private Column fechaInicial, fechaFinal;

    private Empresas backUpEmpresaActual;

    private boolean banderaSeleccionEmpresasPorEmpresa;

    private int tamano;

    public ControlPeriodosActivos() {
        permitirIndex = true;
        listaEmpresas = null;
        empresaSeleccionada = null;
        indiceEmpresaMostrada = 0;
        listEmpresa = null;
        listEmpresaPorBoton = null;
        modificarEmpresas = new ArrayList<Empresas>();
        editarEmpresas = new Empresas();
        aceptar = true;
        filtradoListaEmpresas = null;
        guardado = true;
        banderaSeleccionEmpresasPorEmpresa = false;
        tamano = 260;
        buscarCentrocosto = false;
        mostrartodos = true;
        empresaSeleccionada = null;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEmpresas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
    private BigInteger secuenciaEmpresa;

    public void recibirEmpresa(BigInteger secEmpresa) {
        empresaSeleccionada = null;
        if (secEmpresa.equals(new BigInteger("0"))) {
            secuenciaEmpresa = null;
        } else {
            secuenciaEmpresa = secEmpresa;
        }
        System.out.println("ControlPeriodosActivos recibirEmpresa secuenciaEmpresa : " + secuenciaEmpresa);
        getEmpresaSeleccionada();
        getListEmpresa();
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A CONTROLBETACENTROSCOSTOS.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLBETACENTROSCOSTOS eventoFiltrar ERROR===" + e.getMessage());
        }
    }
    private Date backUpFechaInicial;
    private Date backUpFechaFinal;

    public void cambiarIndice(int indice, int celda) {
        System.err.println("BETA CENTRO COSTO TIPO LISTA = " + tipoLista);
        System.err.println("PERMITIR INDEX = " + permitirIndex);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            System.err.println("CAMBIAR INDICE CUALCELDA = " + cualCelda);
            secRegistro = listEmpresa.get(index).getSecuencia();
            System.err.println("Sec Registro = " + secRegistro);
            if (cualCelda == 0) {
                backUpFechaInicial = listEmpresa.get(index).getControlinicioperiodoactivo();
            }
            if (cualCelda == 0) {
                backUpFechaFinal = listEmpresa.get(index).getControlfinperiodoactivo();
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void mostrarInfo(int indice, int celda, String cambio) {
        Calendar myCalendar = Calendar.getInstance();
        int contador = 0;
        int fechas = 0;
        int mes = 0, anito = 0, mesHoy = 0, anitoHoy = 0;

        System.out.println("ControlVigenciasPlantas mostrar info indice : " + indice + "  permitirInxes : " + permitirIndex);
        if (permitirIndex == true) {
            RequestContext context = RequestContext.getCurrentInstance();

            mensajeValidacion = " ";
            index = indice;
            cualCelda = celda;
            System.out.println("ControlVigenciasPlantas mostrarInfo INDICE : " + index + " cualCelda : " + cualCelda);
            if (tipoLista == 0) {
                secRegistro = listEmpresa.get(indice).getSecuencia();
                if (cambio.equalsIgnoreCase("INICIO")) {
                    System.err.println("MODIFICAR FECHA \n Indice" + indice + "Fecha " + listEmpresa.get(indice).getControlinicioperiodoactivo());
                    if (listEmpresa.get(indice).getControlinicioperiodoactivo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        //listEmpresa.get(indice).setControlinicioperiodoactivo(backUpFecha);
                    } else {
                        listEmpresa.get(indice).getControlfinperiodoactivo();
                        myCalendar.setTime(listEmpresa.get(indice).getControlinicioperiodoactivo());
                        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
                        Date firstDay = myCalendar.getTime();
                        System.err.println("Primer Dia : " + firstDay);
                        listEmpresa.get(indice).setControlinicioperiodoactivo(firstDay);
                        myCalendar.add(Calendar.MONTH, 1);
                        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
                        myCalendar.add(Calendar.DATE, -1);
                        Date lastDay = myCalendar.getTime();
                        System.err.println("Ultimo dia : " + lastDay);
                        listEmpresa.get(indice).setControlfinperiodoactivo(lastDay);
                        contador++;
                    }

                } else {
                    System.err.println("FINAL");
                    if (listEmpresa.get(indice).getControlfinperiodoactivo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        //listEmpresa.get(indice).setControlinicioperiodoactivo(backUpFecha);
                    } else {
                        myCalendar.setTime(listEmpresa.get(indice).getControlfinperiodoactivo());
                        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
                        Date firstDay = myCalendar.getTime();
                        System.err.println("Primer Dia : " + firstDay);
                        listEmpresa.get(indice).setControlinicioperiodoactivo(firstDay);
                        myCalendar.add(Calendar.MONTH, 1);
                        myCalendar.set(Calendar.DAY_OF_MONTH, 1);
                        myCalendar.set(Calendar.DATE, -1);
                        Date lastDay = myCalendar.getTime();
                        System.err.println("Ultimo dia : " + lastDay);
                        listEmpresa.get(indice).setControlfinperiodoactivo(lastDay);
                        contador++;
                    }

                }
                if (contador == 1) {
                    Date hoy = new Date();
                    mesHoy = hoy.getMonth();
                    anitoHoy = hoy.getYear();
                    mes = listEmpresa.get(indice).getControlfinperiodoactivo().getMonth();
                    anito = listEmpresa.get(indice).getControlfinperiodoactivo().getYear();
                    System.err.println("mesHoy : " + mesHoy);
                    System.err.println("anitoHoy : " + anitoHoy);
                    System.err.println("mes: " + mes);
                    System.err.println("anito: " + anito);
                    if ((anito - anitoHoy) != 0 || (mes - mesHoy) != 0) {
                        context.execute("modificacionFechas1.show()");
                    }
                    //listEmpresa.get(indice).getCentrocosto().setCodigo(empresaSeleccionada.);
                    if (modificarEmpresas.isEmpty()) {
                        modificarEmpresas.add(listEmpresa.get(indice));
                    } else if (!modificarEmpresas.contains(listEmpresa.get(indice))) {
                        modificarEmpresas.add(listEmpresa.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                    context.update("form:datosEmpresas");
                } else {
                    context.update("form:validacionModificar");
                    context.execute("validacionModificar.show()");
                }
            } else {
                secRegistro = filtrarEmpresas.get(indice).getSecuencia();
                System.err.println("MODIFICAR FECHA \n Indice" + indice + "Fecha " + filtrarEmpresas.get(indice).getControlinicioperiodoactivo());
                if (filtrarEmpresas.get(indice).getControlinicioperiodoactivo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    contador++;
                    //listEmpresa.get(indice).setControlinicioperiodoactivo(backUpFecha);

                } else {
                    for (int j = 0; j < filtrarEmpresas.size(); j++) {
                        if (j != indice) {
                            if (filtrarEmpresas.get(indice).getControlinicioperiodoactivo().equals(filtrarEmpresas.get(j).getControlinicioperiodoactivo())) {
                                fechas++;
                            }
                        }
                    }

                }
                if (fechas > 0) {
                    mensajeValidacion = "FECHAS REPETIDAS";
                    contador++;
                    //listEmpresa.get(indice).setControlinicioperiodoactivo(backUpFecha);

                }
                if (contador == 0) {
                    if (modificarEmpresas.isEmpty()) {
                        modificarEmpresas.add(filtrarEmpresas.get(indice));
                    } else if (!modificarEmpresas.contains(filtrarEmpresas.get(indice))) {
                        modificarEmpresas.add(filtrarEmpresas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                    context.update("form:datosEmpresas");

                } else {
                    context.update("form:validacionModificar");
                    context.execute("validacionModificar.show()");
                }
            }

            index = -1;
            secRegistro = null;
            context.update("form:datosEmpresas");
        }

        System.out.println(
                "Indice: " + index + " Celda: " + cualCelda);

    }

    public void cancelarModificacion() {
        try {
            System.out.println("entre a CONTROLBETACENTROSCOSTOS.cancelarModificacion");
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                //0
                fechaInicial = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaInicial");
                fechaInicial.setFilterStyle("display: none; visibility: hidden;");
                //1
                fechaFinal = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaFinal");
                fechaFinal.setFilterStyle("display: none; visibility: hidden;");

                tamano = 260;
                bandera = 0;
                filtrarEmpresas = null;
                tipoLista = 0;
            }

            modificarEmpresas.clear();
            index = -1;
            k = 0;
            listEmpresa = null;
            guardado = true;
            permitirIndex = true;
            buscarCentrocosto = false;
            mostrartodos = true;
            RequestContext context = RequestContext.getCurrentInstance();
            banderaModificacionEmpresa = 0;
            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
            }
            getListEmpresa();
            if (listEmpresa == null || listEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listEmpresa.size();
            }
            context.update("form:informacionRegistro");
            context.update("form:datosEmpresas");
            context.update("form:ACEPTAR");
            context.update("form:BUSCARCENTROCOSTO");
            context.update("form:MOSTRARTODOS");
            context.update("formularioDialogos:aceptarE");

        } catch (Exception E) {
            System.out.println("ERROR CONTROLBETACENTROSCOSTOS.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void salir() {
        try {
            System.out.println("entre a CONTROLBETACENTROSCOSTOS.Salir");
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                fechaInicial = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaInicial");
                fechaInicial.setFilterStyle("display: none; visibility: hidden;");
                fechaFinal = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaFinal");
                fechaFinal.setFilterStyle("display: none; visibility: hidden;");

                tamano = 260;
                bandera = 0;
                filtrarEmpresas = null;
                tipoLista = 0;
            }

            modificarEmpresas.clear();
            index = -1;
            k = 0;
            listEmpresa = null;
            guardado = true;
            permitirIndex = true;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosEmpresas");
            context.update("form:ACEPTAR");
            context.update("formularioDialogos:aceptarE");

        } catch (Exception E) {
            System.out.println("ERROR CONTROLBETACENTROSCOSTOS.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A CONTROLBETACENTROSCOSTOS.asignarIndex \n");
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
                context.update("form:tiposEmpresasDialogo");
                context.execute("tiposEmpresasDialogo.show()");
                dig = -1;
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLBETACENTROSCOSTOS.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void llamadoDialogoBuscarEmpresas() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                banderaSeleccionEmpresasPorEmpresa = true;
                context.execute("confirmarGuardar.show()");

            } else {
                listEmpresaPorBoton = null;
                getListEmpresasPorEmpresaBoton();
                index = -1;
                context.update("formularioDialogos:lovEmpresas");
                context.execute("buscarEmpresasDialogo.show()");

            }
        } catch (Exception e) {
            System.err.println("ERROR LLAMADO DIALOGO BUSCAR CENTROS COSTOS " + e);
        }

    }
    private boolean buscarCentrocosto;
    private boolean mostrartodos;

    public void guardarCambiosCentroCosto() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {

            if (!modificarEmpresas.isEmpty()) {
                administrarEmpresas.editarEmpresas(modificarEmpresas);
                modificarEmpresas.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listEmpresa = null;
            context.update("form:datosTipoCentroCosto");
            k = 0;
            guardado = true;

            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
                banderaModificacionEmpresa = 1;

            }
            if (banderaSeleccionEmpresasPorEmpresa == true) {
                listEmpresaPorBoton = null;
                getListEmpresasPorEmpresaBoton();
                index = -1;
                context.update("formularioDialogos:lovEmpresas");
                context.execute("buscarEmpresasDialogo.show()");
                banderaSeleccionEmpresasPorEmpresa = false;
            }
        }
        index = -1;
        aceptar = true;
        FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.update("form:growl");
        context.update("formularioDialogos:aceptarE");

        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        banderaModificacionEmpresa = 0;
    }

    public void cancelarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (banderaModificacionEmpresa == 0) {
            empresaSeleccionada = backUpEmpresaActual;
            context.update("formularioDialogos:lovEmpresas");
            banderaModificacionEmpresa = 1;
        }
    }

    public void limpiarNuevoPeriodosActivos() {

    }

    public void activarCtrlF11() {
        System.out.println("\n ENTRE A CONTROLBETACENTROSCOSTOS.activarCtrlF11 \n");

        try {

            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 0) {
                tamano = 236;
                System.out.println("Activar");
                fechaInicial = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaInicial");
                fechaInicial.setFilterStyle("width: 40px");
                fechaFinal = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaFinal");
                fechaFinal.setFilterStyle("width: 105px");

                RequestContext.getCurrentInstance().update("form:datosEmpresas");
                bandera = 1;
            } else if (bandera == 1) {
                System.out.println("Desactivar");
                //0
                fechaInicial = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaInicial");
                fechaInicial.setFilterStyle("display: none; visibility: hidden;");
                fechaFinal = (Column) c.getViewRoot().findComponent("form:datosEmpresas:fechaFinal");
                fechaFinal.setFilterStyle("display: none; visibility: hidden;");

                tamano = 260;
                RequestContext.getCurrentInstance().update("form:datosEmpresas");
                bandera = 0;
                filtrarEmpresas = null;
                tipoLista = 0;
            }
        } catch (Exception e) {

            System.out.println("ERROR CONTROLBETACENTROSCOSTOS.activarCtrlF11 ERROR====================" + e.getMessage());
        }
    }

    public void editarCelda() {
        try {
            System.out.println("\n ENTRE A editarCelda INDEX  " + index);
            if (index >= 0) {
                System.out.println("\n ENTRE AeditarCelda TIPOLISTA " + tipoLista);
                if (tipoLista == 0) {
                    editarEmpresas = listEmpresa.get(index);
                }
                if (tipoLista == 1) {
                    editarEmpresas = filtrarEmpresas.get(index);
                }
                RequestContext context = RequestContext.getCurrentInstance();
                System.out.println("CONTROLBETACENTROSCOSTOS: Entro a editar... valor celda: " + cualCelda);
                if (cualCelda == 0) {
                    context.update("formularioDialogos:editarFechaInicial");
                    context.execute("editarFechaInicial.show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    context.update("formularioDialogos:editarFechaFinal");
                    context.execute("editarFechaFinal.show()");
                    cualCelda = -1;
                }
            }
            index = -1;
        } catch (Exception E) {
            System.out.println("ERROR CONTROLBETACENTROSCOSTOS.editarCelDa ERROR=====================" + E.getMessage());
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "CentroCostos", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    /**
     *
     * @throws IOException
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEmpresasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "CentroCostos", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listEmpresa.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "CENTROSCOSTOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                System.out.println("resultado: " + resultado);
                if (resultado == 1) {
                    context.execute("errorObjetosDB.show()");
                } else if (resultado == 2) {
                    context.execute("confirmarRastro.show()");
                } else if (resultado == 3) {
                    context.execute("errorRegistroRastro.show()");
                } else if (resultado == 4) {
                    context.execute("errorTablaConRastro.show()");
                } else if (resultado == 5) {
                    context.execute("errorTablaSinRastro.show()");
                }
            } else {
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("CENTROSCOSTOS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    public void lovEmpresas() {
        index = -1;
        secRegistro = null;
        cualCelda = -1;
        RequestContext.getCurrentInstance().execute("EmpresasDialogo.show()");
    }

    public void cambiarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("Cambiar empresa  GUARDADO = " + guardado);
        System.err.println("Cambiar empresa  GUARDADO = " + empresaSeleccionada.getNombre());
        if (guardado == true) {
            context.update("form:nombreEmpresa");
            context.update("form:nitEmpresa");
            getListEmpresa();
            getListEmpresasPorEmpresaBoton();
            filtradoListaEmpresas = null;
            listEmpresa = null;
            aceptar = true;
            context.reset("formularioDialogos:lovEmpresas:globalFilter");
            context.execute("lovEmpresas.clearFilters()");
            context.execute("EmpresasDialogo.hide()");
            //context.update(":lovEmpresas");
            backUpEmpresaActual = empresaSeleccionada;
            banderaModificacionEmpresa = 0;
            context.update("form:datosEmpresas");

        } else {
            banderaModificacionEmpresa = 0;
            context.execute("confirmarGuardar.show()");
        }
    }

    public void cancelarCambioEmpresa() {
        filtradoListaEmpresas = null;
        banderaModificacionEmpresa = 0;
        index = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        context.execute("lovEmpresas.clearFilters()");
        context.execute("EmpresasDialogo.hide()");
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
    private String infoRegistroEmpresas;

    public List<Empresas> getListaEmpresas() {
        try {
            if (listaEmpresas == null) {
                listaEmpresas = administrarEmpresas.listaEmpresas();
                if (!listaEmpresas.isEmpty()) {
                    empresaSeleccionada = listaEmpresas.get(0);
                    backUpEmpresaActual = empresaSeleccionada;
                }
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listEmpresa == null || listaEmpresas.isEmpty()) {
                infoRegistroEmpresas = "Cantidad de registros: 0 ";
            } else {
                infoRegistroEmpresas = "Cantidad de registros: " + listaEmpresas.size();
            }
            context.update("form:infoRegistroEmpresas");
            return listaEmpresas;
        } catch (Exception e) {
            System.out.println("ERRO LISTA EMPRESAS " + e);
            return null;
        }
    }

    public void setListaEmpresas(List<Empresas> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public List<Empresas> getFiltradoListaEmpresas() {
        return filtradoListaEmpresas;
    }

    public void setFiltradoListaEmpresas(List<Empresas> filtradoListaEmpresas) {
        this.filtradoListaEmpresas = filtradoListaEmpresas;
    }

    public Empresas getEmpresaSeleccionada() {
        // try {
        if (secuenciaEmpresa == null) {
            if (empresaSeleccionada == null) {
                empresaSeleccionada = administrarEmpresas.consultarEmpresaSecuencia(secuenciaEmpresa);
            }
        } else {
            if (empresaSeleccionada == null) {
                getListEmpresa();
            }
        }
        System.out.println("EMPRESA SELECCIONADA : " + empresaSeleccionada.getNombre());
        return empresaSeleccionada;
        /*} catch (Exception e) {
         System.out.println("ERROR CONTROLBETACENTROSCOSTOS.getEmpresaSeleccionada ERROR " + e);
         } finally {
         return empresaSeleccionada;
         }*/
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }
    private String infoRegistro;

    public List<Empresas> getListEmpresasPorEmpresaBoton() {
        try {
            if (empresaSeleccionada == null) {
                getEmpresaSeleccionada();
                if (listEmpresaPorBoton == null) {
                    listEmpresaPorBoton = administrarEmpresas.listasEmpresasPorSecuenciaEmpresa(secuenciaEmpresa);
                }
            }
            return listEmpresaPorBoton;
        } catch (Exception e) {
            System.out.println("ControlCentrosCosto: Error al recibir los Empresas de la empresa seleccionada /n" + e.getMessage());
            return null;
        }
    }

    public List<Empresas> getListEmpresa() {
        // try {
        if (secuenciaEmpresa == null) {
            if (listEmpresa == null) {
                listEmpresa = administrarEmpresas.listasEmpresasPorSecuenciaEmpresa(secuenciaEmpresa);
                System.out.println("ControlPeriodosActivos Secuencia = null getListEmpresa listEmpresas : " + listEmpresa.size());
                empresaSeleccionada = listEmpresa.get(0);
            } else {
                System.out.println(".-.");
            }
        } else {
            if (listEmpresa == null) {
                listEmpresa = administrarEmpresas.listasEmpresasPorSecuenciaEmpresa(secuenciaEmpresa);
                System.out.println("ControlPeriodosActivos Seecuencia !=null getListEmpresa() Tamaño lista Empresa :" + listEmpresa.size());
                empresaSeleccionada = listEmpresa.get(0);
                System.out.println("ControlPeriodosActivos Seecuencia !=null getListEmpresa() Empresa Seleccionada  :" + empresaSeleccionada.getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            if (listEmpresa == null || listEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listEmpresa.size();
            }
            context.update("form:informacionRegistro");

        }
        return listEmpresa;
        // } catch (Exception e) {
        //     System.out.println(" BETA  BETA ControlCentrosCosto: Error al recibir los Empresas de la empresa seleccionada /n" + e.getMessage());
        //     return null;
        // }
    }

    public void setListEmpresa(List<Empresas> listEmpresa) {
        this.listEmpresa = listEmpresa;
    }

    public void setListEmpresasPorEmpresa(List<Empresas> listEmpresa) {
        this.listEmpresa = listEmpresa;
    }

    public List<Empresas> getFiltrarEmpresas() {
        return filtrarEmpresas;
    }

    public void setFiltrarEmpresas(List<Empresas> filtrarEmpresas) {
        this.filtrarEmpresas = filtrarEmpresas;
    }

    public Empresas getNuevoEmpresas() {
        return nuevoEmpresas;
    }

    public void setNuevoEmpresas(Empresas nuevoEmpresas) {
        this.nuevoEmpresas = nuevoEmpresas;
    }

    public Empresas getEditarEmpresas() {
        return editarEmpresas;
    }

    public void setEditarEmpresas(Empresas editarEmpresas) {
        this.editarEmpresas = editarEmpresas;
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

    public String getInfoRegistroEmpresas() {
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
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

    public Empresas getMostrarEmpresas() {
        return mostrarEmpresas;
    }

    public void setMostrarEmpresas(Empresas mostrarEmpresas) {
        this.mostrarEmpresas = mostrarEmpresas;
    }

}
