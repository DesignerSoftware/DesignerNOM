/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.Empleados;
import Entidades.LiquidacionesLogs;
import Entidades.Operandos;
import Entidades.Procesos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarLiquidacionesLogsInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
public class ControlLiquidacionesLogs implements Serializable {

    @EJB
    AdministrarLiquidacionesLogsInterface administrarLiquidacionesLogs;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<LiquidacionesLogs> listLiquidacionesLogs;
    private List<LiquidacionesLogs> filtrarLiquidacionesLogs;
    private List<LiquidacionesLogs> crearLiquidacionesLogs;
    private List<LiquidacionesLogs> modificarLiquidacionesLogs;
    private List<LiquidacionesLogs> borrarLiquidacionesLogs;
    private LiquidacionesLogs nuevoLiquidacionesLogs;
    private LiquidacionesLogs duplicarLiquidacionesLogs;
    private LiquidacionesLogs editarLiquidacionesLogs;
    private LiquidacionesLogs liquidacionesLogsSeleccionado;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column fechaInicial, fechaFinal, empleado, operando, proceso, valor;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;

    private List<Empleados> listaEmpleados;
    private List<Empleados> filtrarEmpleados;
    private Empleados empleadoSeleccionado;

    public ControlLiquidacionesLogs() {
        listLiquidacionesLogs = null;
        crearLiquidacionesLogs = new ArrayList<LiquidacionesLogs>();
        modificarLiquidacionesLogs = new ArrayList<LiquidacionesLogs>();
        borrarLiquidacionesLogs = new ArrayList<LiquidacionesLogs>();
        permitirIndex = true;
        editarLiquidacionesLogs = new LiquidacionesLogs();
        nuevoLiquidacionesLogs = new LiquidacionesLogs();
        duplicarLiquidacionesLogs = new LiquidacionesLogs();
        guardado = true;
        aceptar = true;
        tamano = 270;
        empleadoSeleccionado = null;
        System.out.println("controlLiquidacionesLogs Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlLiquidacionesLogs PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarLiquidacionesLogs.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlLiquidacionesLogs.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarLiquidacionesLogs.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlLiquidacionesLogs eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void mostrarInfo(int indice, int celda) {

        mensajeValidacion = " ";
        index = indice;
        cualCelda = celda;
        RequestContext context = RequestContext.getCurrentInstance();
        if (permitirIndex == true) {
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    secRegistro = listLiquidacionesLogs.get(index).getSecuencia();

                    if (listLiquidacionesLogs.get(indice).getFechadesde() == null) {
                        listLiquidacionesLogs.get(indice).setFechadesde(backUpFechaDesde);
                    } else if (!listLiquidacionesLogs.get(indice).getFechadesde().equals(backUpFechaDesde) && backUpFechaDesde != null) {
                        listLiquidacionesLogs.get(indice).setFechadesde(backUpFechaDesde);
                    }
                    index = -1;
                    secRegistro = null;

                } else {
                    if (filtrarLiquidacionesLogs.get(indice).getFechadesde() == null) {
                        filtrarLiquidacionesLogs.get(indice).setFechadesde(backUpFechaDesde);
                    } else if (!filtrarLiquidacionesLogs.get(indice).getFechadesde().equals(backUpFechaDesde) && backUpFechaDesde != null) {
                        filtrarLiquidacionesLogs.get(indice).setFechadesde(backUpFechaDesde);
                    }
                    index = -1;
                    secRegistro = null;
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    secRegistro = listLiquidacionesLogs.get(index).getSecuencia();

                    if (listLiquidacionesLogs.get(indice).getFechahasta() == null) {
                        listLiquidacionesLogs.get(indice).setFechahasta(backUpFechaHasta);
                    } else if (!listLiquidacionesLogs.get(indice).getFechahasta().equals(backUpFechaHasta) && backUpFechaHasta != null) {
                        listLiquidacionesLogs.get(indice).setFechahasta(backUpFechaHasta);
                    }
                    index = -1;
                    secRegistro = null;

                } else {
                    if (filtrarLiquidacionesLogs.get(indice).getFechahasta() == null) {
                        filtrarLiquidacionesLogs.get(indice).setFechahasta(backUpFechaHasta);
                    } else if (!filtrarLiquidacionesLogs.get(indice).getFechahasta().equals(backUpFechaHasta) && backUpFechaHasta != null) {
                        filtrarLiquidacionesLogs.get(indice).setFechahasta(backUpFechaHasta);
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");

        }

    }
    private Date backUpFechaDesde;
    private Date backUpFechaHasta;
    private String backUpEmpleado;
    private String backUpOperando;
    private String backUpProceso;
    private String backUpValor;

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listLiquidacionesLogs.get(index).getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpFechaDesde = listLiquidacionesLogs.get(index).getFechadesde();
                } else {
                    backUpFechaDesde = filtrarLiquidacionesLogs.get(index).getFechadesde();
                }
                System.out.println("backUpFechaDesde : " + backUpFechaDesde);
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpFechaHasta = listLiquidacionesLogs.get(index).getFechahasta();
                } else {
                    backUpFechaHasta = filtrarLiquidacionesLogs.get(index).getFechahasta();
                }
                System.out.println("backUpFechaHasta : " + backUpFechaHasta);
            }
            if (cualCelda == 2) {
                if (tipoLista == 0) {
                    backUpEmpleado = listLiquidacionesLogs.get(index).getEmpleado().getPersona().getNombreCompleto();
                } else {
                    backUpEmpleado = filtrarLiquidacionesLogs.get(index).getEmpleado().getPersona().getNombreCompleto();
                }
                System.out.println("backUpEmpleado : " + backUpEmpleado);
            }
            if (cualCelda == 3) {
                if (tipoLista == 0) {
                    backUpOperando = listLiquidacionesLogs.get(index).getOperando().getDescripcion();
                } else {
                    backUpOperando = filtrarLiquidacionesLogs.get(index).getOperando().getDescripcion();
                }
                System.out.println("backUpOperando : " + backUpOperando);
            }
            if (cualCelda == 4) {
                if (tipoLista == 0) {
                    backUpProceso = listLiquidacionesLogs.get(index).getProceso().getDescripcion();
                } else {
                    backUpProceso = filtrarLiquidacionesLogs.get(index).getProceso().getDescripcion();
                }
                System.out.println("backUpProceso : " + backUpProceso);
            }
            if (cualCelda == 5) {
                if (tipoLista == 0) {
                    backUpValor = listLiquidacionesLogs.get(index).getValor();
                } else {
                    backUpValor = filtrarLiquidacionesLogs.get(index).getValor();
                }
                System.out.println("backUpProceso : " + backUpValor);
            }
            System.out.println("Indice: " + index + " Celda: " + cualCelda);
        }
    }

    public void modificarLiquidacionesLogSinGuardar(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0, pass = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar");
            if (tipoLista == 0) {
                if (!crearLiquidacionesLogs.contains(listLiquidacionesLogs.get(indice))) {

                    if (listLiquidacionesLogs.get(indice).getEmpleado().getPersona().getNombreCompleto() == null) {
                        System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpEmpleado : " + backUpEmpleado);
                        listLiquidacionesLogs.get(indice).getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
                    } else if (!listLiquidacionesLogs.get(indice).getEmpleado().getPersona().getNombreCompleto().equals(backUpEmpleado) && backUpEmpleado != null) {
                        listLiquidacionesLogs.get(indice).getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
                        System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpEmpleado : " + backUpEmpleado);
                    }
                    if (listLiquidacionesLogs.get(indice).getOperando().getDescripcion() == null) {
                        listLiquidacionesLogs.get(indice).getOperando().setDescripcion(backUpOperando);
                        System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpOperando : " + backUpOperando);
                    } else if (!listLiquidacionesLogs.get(indice).getOperando().getDescripcion().equals(backUpOperando) && backUpOperando != null) {
                        listLiquidacionesLogs.get(indice).getOperando().setDescripcion(backUpOperando);
                        System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpOperando : " + backUpOperando);
                    }
                    if (listLiquidacionesLogs.get(indice).getProceso().getDescripcion() == null) {
                        listLiquidacionesLogs.get(indice).getProceso().setDescripcion(backUpProceso);
                        System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpProceso : " + backUpProceso);
                    } else if (!listLiquidacionesLogs.get(indice).getProceso().getDescripcion().equals(backUpProceso) && backUpProceso != null) {
                        listLiquidacionesLogs.get(indice).getProceso().setDescripcion(backUpProceso);
                        System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpProceso : " + backUpProceso);
                    }
                    if (listLiquidacionesLogs.get(indice).getValor() == null) {
                        listLiquidacionesLogs.get(indice).setValor(backUpValor);
                        System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpValor : " + backUpValor);
                    } else if (!listLiquidacionesLogs.get(indice).getValor().equals(backUpValor) && backUpValor != null) {
                        listLiquidacionesLogs.get(indice).setValor(backUpValor);
                        System.err.println("CONTROLLIQUIDACIONESLOGS modificarLiquidacionesLogSinGuardar backUpValor : " + backUpValor);
                    }

                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearLiquidacionesLogs.contains(filtrarLiquidacionesLogs.get(indice))) {

                    if (filtrarLiquidacionesLogs.get(indice).getEmpleado().getPersona().getNombreCompleto() == null) {
                        filtrarLiquidacionesLogs.get(indice).getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
                    } else if (!filtrarLiquidacionesLogs.get(indice).getEmpleado().getPersona().getNombreCompleto().equals(backUpEmpleado) && backUpEmpleado != null) {
                        filtrarLiquidacionesLogs.get(indice).getEmpleado().getPersona().setNombreCompleto(backUpEmpleado);
                    }
                    if (filtrarLiquidacionesLogs.get(indice).getOperando().getDescripcion() == null) {
                        filtrarLiquidacionesLogs.get(indice).getOperando().setDescripcion(backUpOperando);
                    } else if (!filtrarLiquidacionesLogs.get(indice).getOperando().getDescripcion().equals(backUpOperando) && backUpOperando != null) {
                        filtrarLiquidacionesLogs.get(indice).getOperando().setDescripcion(backUpOperando);
                    }
                    if (filtrarLiquidacionesLogs.get(indice).getProceso().getDescripcion() == null) {
                        filtrarLiquidacionesLogs.get(indice).getProceso().setDescripcion(backUpProceso);
                    } else if (!filtrarLiquidacionesLogs.get(indice).getProceso().getDescripcion().equals(backUpProceso) && backUpProceso != null) {
                        filtrarLiquidacionesLogs.get(indice).getProceso().setDescripcion(backUpProceso);
                    }
                    if (filtrarLiquidacionesLogs.get(indice).getValor() == null) {
                        filtrarLiquidacionesLogs.get(indice).setValor(backUpValor);
                    } else if (!filtrarLiquidacionesLogs.get(indice).getValor().equals(backUpValor) && backUpValor != null) {
                        filtrarLiquidacionesLogs.get(indice).setValor(backUpValor);
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
        }
        RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlLiquidacionesLogs.asignarIndex \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlLiquidacionesLogs.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    private String infoRegistro;

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaInicial");
            fechaInicial.setFilterStyle("display: none; visibility: hidden;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaFinal");
            fechaFinal.setFilterStyle("display: none; visibility: hidden;");
            empleado = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            operando = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:operando");
            operando.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
            bandera = 0;
            filtrarLiquidacionesLogs = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarLiquidacionesLogs.clear();
        crearLiquidacionesLogs.clear();
        modificarLiquidacionesLogs.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listLiquidacionesLogs = null;
        guardado = true;
        permitirIndex = true;
        getListLiquidacionesLogs();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listLiquidacionesLogs == null || listLiquidacionesLogs.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLiquidacionesLogs.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaInicial");
            fechaInicial.setFilterStyle("display: none; visibility: hidden;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaFinal");
            fechaFinal.setFilterStyle("display: none; visibility: hidden;");
            empleado = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            operando = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:operando");
            operando.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");

            RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
            bandera = 0;
            filtrarLiquidacionesLogs = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarLiquidacionesLogs.clear();
        crearLiquidacionesLogs.clear();
        modificarLiquidacionesLogs.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listLiquidacionesLogs = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaInicial");
            fechaInicial.setFilterStyle("width: 85% !important;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaFinal");
            fechaFinal.setFilterStyle("width: 85% !important;");
            empleado = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:empleado");
            empleado.setFilterStyle("width: 85% !important;");
            operando = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:operando");
            operando.setFilterStyle("width: 85% !important;");
            proceso = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:proceso");
            proceso.setFilterStyle("width: 85% !important;");
            valor = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:valor");
            valor.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");

            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaInicial");
            fechaInicial.setFilterStyle("display: none; visibility: hidden;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaFinal");
            fechaFinal.setFilterStyle("display: none; visibility: hidden;");
            empleado = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            operando = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:operando");
            operando.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");

            RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
            bandera = 0;
            filtrarLiquidacionesLogs = null;
            tipoLista = 0;
        }
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarLiquidacionesLogs = listLiquidacionesLogs.get(index);
            }
            if (tipoLista == 1) {
                editarLiquidacionesLogs = filtrarLiquidacionesLogs.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaInicial");
                RequestContext.getCurrentInstance().execute("PF('editarFechaInicial').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHasta");
                RequestContext.getCurrentInstance().execute("PF('editarFechaHasta').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleado");
                RequestContext.getCurrentInstance().execute("PF('editarEmpleado').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarOperandoE");
                RequestContext.getCurrentInstance().execute("PF('editarOperandoE').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoE");
                RequestContext.getCurrentInstance().execute("PF('editarProcesoE').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorE");
                RequestContext.getCurrentInstance().execute("PF('editarValorE').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void limpiarNuevoLiquidacionesLogs() {
        nuevoLiquidacionesLogs = new LiquidacionesLogs();
        nuevoLiquidacionesLogs.setEmpleado(new Empleados());
        nuevoLiquidacionesLogs.setProceso(new Procesos());
        nuevoLiquidacionesLogs.setOperando(new Operandos());
    }

    public void revisarDialogoGuardar() {

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:confirmarGuardar");
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
    }

    public void borrarLiquidacionesLogsEmpleado() {
    }

    public void seleccionarEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();

        System.out.println("Empleado Seleccionado : " + empleadoSeleccionado.getPersona().getNombreCompleto());
        if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            fechaInicial = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaInicial");
            fechaInicial.setFilterStyle("display: none; visibility: hidden;");
            fechaFinal = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:fechaFinal");
            fechaFinal.setFilterStyle("display: none; visibility: hidden;");
            empleado = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:empleado");
            empleado.setFilterStyle("display: none; visibility: hidden;");
            operando = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:operando");
            operando.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) c.getViewRoot().findComponent("form:datosLiquidacionesLogs:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");

            RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
            bandera = 0;
            filtrarLiquidacionesLogs = null;
            tipoLista = 0;
        }
        listLiquidacionesLogs = null;
        getListLiquidacionesLogs();
        aceptar = true;
        RequestContext.getCurrentInstance().update("form:datosLiquidacionesLogs");
        RequestContext.getCurrentInstance().update("form:nombreEmpleado");
        context.reset("form:lovTipoCentrosCostos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoCentrosCostos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').hide()");
    }

    public void cancelarCambioEmpleado() {
        filtrarEmpleados = null;
        empleadoSeleccionado = null;
        aceptar = true;
        index = -1;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoCentrosCostos:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoCentrosCostos').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').hide()");

    }

    public void llamarDialogoEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
        RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesLogsExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "LIQUIDACIONESLOGS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLiquidacionesLogsExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "LIQUIDACIONESLOGS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listLiquidacionesLogs.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "LIQUIDACIONESLOGS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("LIQUIDACIONESLOGS")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<LiquidacionesLogs> getListLiquidacionesLogs() {
        if (empleadoSeleccionado == null) {
            getListaEmpleados();
            if (listLiquidacionesLogs == null) {
                System.out.println("ControlLiquidacionesLogs getListLiquidacionesLogs");
                listLiquidacionesLogs = administrarLiquidacionesLogs.consultarLiquidacionesLogsPorEmpleado(empleadoSeleccionado.getSecuencia());
            }
        } else {
            listLiquidacionesLogs = administrarLiquidacionesLogs.consultarLiquidacionesLogsPorEmpleado(empleadoSeleccionado.getSecuencia());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listLiquidacionesLogs == null || listLiquidacionesLogs.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLiquidacionesLogs.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");

        return listLiquidacionesLogs;
    }

    public void setListLiquidacionesLogs(List<LiquidacionesLogs> listLiquidacionesLogs) {
        this.listLiquidacionesLogs = listLiquidacionesLogs;
    }

    public List<LiquidacionesLogs> getFiltrarLiquidacionesLogs() {
        return filtrarLiquidacionesLogs;
    }

    public void setFiltrarLiquidacionesLogs(List<LiquidacionesLogs> filtrarLiquidacionesLogs) {
        this.filtrarLiquidacionesLogs = filtrarLiquidacionesLogs;
    }

    public LiquidacionesLogs getNuevoLiquidacionesLogs() {
        return nuevoLiquidacionesLogs;
    }

    public void setNuevoLiquidacionesLogs(LiquidacionesLogs nuevoLiquidacionesLogs) {
        this.nuevoLiquidacionesLogs = nuevoLiquidacionesLogs;
    }

    public LiquidacionesLogs getEditarLiquidacionesLogs() {
        return editarLiquidacionesLogs;
    }

    public void setEditarLiquidacionesLogs(LiquidacionesLogs editarLiquidacionesLogs) {
        this.editarLiquidacionesLogs = editarLiquidacionesLogs;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
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

    public LiquidacionesLogs getLiquidacionesLogsSeleccionado() {
        return liquidacionesLogsSeleccionado;
    }

    public void setLiquidacionesLogsSeleccionado(LiquidacionesLogs clasesPensionesSeleccionado) {
        this.liquidacionesLogsSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }
    private String infoRegistroEmpleados;

    public List<Empleados> getListaEmpleados() {
        if (listaEmpleados == null) {
            listaEmpleados = administrarLiquidacionesLogs.consultarLOVEmpleados();
            empleadoSeleccionado = listaEmpleados.get(0);
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaEmpleados == null || listaEmpleados.isEmpty()) {
            infoRegistroEmpleados = "Cantidad de registros: 0 ";
        } else {
            infoRegistroEmpleados = "Cantidad de registros: " + listaEmpleados.size();
        }
        return listaEmpleados;
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public List<Empleados> getFiltrarEmpleados() {
        return filtrarEmpleados;
    }

    public void setFiltrarEmpleados(List<Empleados> filtrarEmpleados) {
        this.filtrarEmpleados = filtrarEmpleados;
    }

    public Empleados getEmpleadoSeleccionado() {
        if (empleadoSeleccionado == null) {
            getListaEmpleados();
        }
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public String getInfoRegistroEmpleados() {
        return infoRegistroEmpleados;
    }

    public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
        this.infoRegistroEmpleados = infoRegistroEmpleados;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

}
