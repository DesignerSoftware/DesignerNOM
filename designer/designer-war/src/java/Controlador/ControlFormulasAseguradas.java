/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.FormulasAseguradas;
import Entidades.Formulas;
import Entidades.Periodicidades;
import Entidades.Procesos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulasAseguradasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class ControlFormulasAseguradas implements Serializable {

    @EJB
    AdministrarFormulasAseguradasInterface administrarFormulasAseguradas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<FormulasAseguradas> listFormulasAseguradas;
    private List<FormulasAseguradas> filtrarFormulasAseguradas;
    private List<FormulasAseguradas> crearFormulasAseguradas;
    private List<FormulasAseguradas> modificarFormulasAseguradas;
    private List<FormulasAseguradas> borrarFormulasAseguradas;
    private FormulasAseguradas nuevoFormulasAseguradas;
    private FormulasAseguradas duplicarFormulasAseguradas;
    private FormulasAseguradas editarFormulasAseguradas;
    private FormulasAseguradas formulaAseguradaSeleccionada;
    private FormulasAseguradas formulaAseguradaSeleccion;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column proceso, personafir, periodicidad;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private String infoRegistro;
    private String infoRegistroFormulas;
    private String infoRegistroProcesos;
    private String infoRegistroPeriocididades;
    private String infoRegistroFormulasAseguradas;
    //---------------------------------
    private String backupFormula;
    private List<Formulas> listaFormulas;
    private List<Formulas> filtradoFormulas;
    private Formulas formulaSeleccionado;
    private String nuevoYduplicarCompletarFormula;
    //--------------------------------------
    private String backupProceso;
    private List<Procesos> listaProcesos;
    private List<Procesos> filtradoProcesos;
    private Procesos procesoSeleccionado;
    private String nuevoYduplicarCompletarProcesos;
    //--------------------------------------
    //--------------------------------------
    private String backupPeriodicidad;
    private List<Periodicidades> listaPeriodicidades;
    private List<Periodicidades> filtradoPeriodicidades;
    private Periodicidades periodicidadSeleccionado;
    private String nuevoYduplicarCompletarPeriodicidad;
    //--------------------------------------
    private String nuevoYduplicarCompletarCargo;

    //---------------------------------
    private List<FormulasAseguradas> listFormulasAseguradasBoton;
    private List<FormulasAseguradas> filterFormulasAseguradasBoton;

    private boolean buscarFormulas;
    private boolean mostrarTodos;

    public ControlFormulasAseguradas() {
        listFormulasAseguradasBoton = null;
        listFormulasAseguradas = null;
        crearFormulasAseguradas = new ArrayList<FormulasAseguradas>();
        modificarFormulasAseguradas = new ArrayList<FormulasAseguradas>();
        borrarFormulasAseguradas = new ArrayList<FormulasAseguradas>();
        permitirIndex = true;
        editarFormulasAseguradas = new FormulasAseguradas();
        nuevoFormulasAseguradas = new FormulasAseguradas();
        nuevoFormulasAseguradas.setFormula(new Formulas());
        nuevoFormulasAseguradas.setProceso(new Procesos());
        nuevoFormulasAseguradas.setPeriodicidad(new Periodicidades());
        duplicarFormulasAseguradas = new FormulasAseguradas();
        duplicarFormulasAseguradas.setFormula(new Formulas());
        duplicarFormulasAseguradas.setProceso(new Procesos());
        duplicarFormulasAseguradas.setPeriodicidad(new Periodicidades());
        listaFormulas = null;
        filtradoFormulas = null;
        listaProcesos = null;
        filtradoProcesos = null;
        listaPeriodicidades = null;
        filtradoPeriodicidades = null;
        guardado = true;
        tamano = 270;
        formulaAseguradaSeleccionada = new FormulasAseguradas();
        mostrarTodos = true;
        buscarFormulas = false;
        aceptar = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarFormulasAseguradas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlFormulasAseguradas.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarFormulasAseguradas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlFormulasAseguradas eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listFormulasAseguradas.get(index).getSecuencia();
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    //formulacodigo
                    backupFormula = listFormulasAseguradas.get(index).getFormula().getNombrelargo();
                }
                if (cualCelda == 1) {
                    backupProceso = listFormulasAseguradas.get(index).getFormula().getNombrelargo();
                    System.out.println("CONCEPTO : " + backupProceso);

                }
                if (cualCelda == 2) {
                    backupPeriodicidad = listFormulasAseguradas.get(index).getPeriodicidad().getNombre();
                    System.out.println("VALOR VOLUNTARIO : " + backupPeriodicidad);

                }

            } else if (tipoLista == 1) {
                if (cualCelda == 0) {
                    backupFormula = filtrarFormulasAseguradas.get(index).getFormula().getNombrelargo();
                }
                /*if (cualCelda == 1) {
                 backupConcepto = filtrarFormulasAseguradas.get(index).getFormula().getNombrelargo();
                 System.out.println("CONCEPTO : " + backupConcepto);
                 }
                 if (cualCelda == 2) {
                 backupValorVoluntario = filtrarFormulasAseguradas.get(index).getValorunitario();
                 System.out.println("VALOR VOLUNTARIO : " + backupConcepto);

                 }*/
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("\n ENTRE A ControlFormulasAseguradas.asignarIndex \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dig == 0) {
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                dig = -1;
            }
            if (dig == 1) {
                RequestContext.getCurrentInstance().update("form:procesosDialogo");
                PrimefacesContextUI.ejecutar("PF('procesosDialogo').show()");
                dig = -1;
            }
            if (dig == 2) {
                RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
                dig = -1;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlFormulasAseguradas.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        if (index >= 0) {

            if (cualCelda == 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }
            if (cualCelda == 1) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:procesosDialogo");
                PrimefacesContextUI.ejecutar("PF('procesosDialogo').show()");
            }
            if (cualCelda == 2) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
            }
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            personafir = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            periodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:periodicidad");
            periodicidad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            bandera = 0;
            filtrarFormulasAseguradas = null;
            tipoLista = 0;
        }

        borrarFormulasAseguradas.clear();
        crearFormulasAseguradas.clear();
        modificarFormulasAseguradas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listFormulasAseguradas = null;
        guardado = true;
        permitirIndex = true;
        mostrarTodos = true;
        buscarFormulas = false;
        getListFormulasAseguradas();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listFormulasAseguradas == null || listFormulasAseguradas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listFormulasAseguradas.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            personafir = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            periodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:periodicidad");
            periodicidad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            bandera = 0;
            filtrarFormulasAseguradas = null;
            tipoLista = 0;
        }

        borrarFormulasAseguradas.clear();
        crearFormulasAseguradas.clear();
        modificarFormulasAseguradas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listFormulasAseguradas = null;
        guardado = true;
        permitirIndex = true;
        mostrarTodos = true;
        buscarFormulas = false;
        getListFormulasAseguradas();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listFormulasAseguradas == null || listFormulasAseguradas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listFormulasAseguradas.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
    }

    public void cancelarModificacionCambio() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            personafir = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            periodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:periodicidad");
            periodicidad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            bandera = 0;
            filtrarFormulasAseguradas = null;
            tipoLista = 0;
        }

        borrarFormulasAseguradas.clear();
        crearFormulasAseguradas.clear();
        modificarFormulasAseguradas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listFormulasAseguradas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void guardarFormulasCambios() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarConceptosSoportes");
            if (!borrarFormulasAseguradas.isEmpty()) {
                administrarFormulasAseguradas.borrarFormulasAseguradas(borrarFormulasAseguradas);
                //mostrarBorrados
                registrosBorrados = borrarFormulasAseguradas.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarFormulasAseguradas.clear();
            }
            if (!modificarFormulasAseguradas.isEmpty()) {
                administrarFormulasAseguradas.modificarFormulasAseguradas(modificarFormulasAseguradas);
                modificarFormulasAseguradas.clear();
            }
            if (!crearFormulasAseguradas.isEmpty()) {
                administrarFormulasAseguradas.crearFormulasAseguradas(crearFormulasAseguradas);
                crearFormulasAseguradas.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listFormulasAseguradas = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            k = 0;
            guardado = true;
            llamadoDialogoBuscarFormulas();
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            personafir = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:personafir");
            personafir.setFilterStyle("width: 85% !important");
            proceso = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:proceso");
            proceso.setFilterStyle("width: 85% !important");
            periodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:periodicidad");
            periodicidad.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            personafir = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            periodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:periodicidad");
            periodicidad.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            bandera = 0;
            filtrarFormulasAseguradas = null;
            tipoLista = 0;
        }
    }

    public void actualizarFormulas() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("formula seleccionado : " + formulaSeleccionado.getNombrelargo());
        System.out.println("tipo Actualizacion : " + tipoActualizacion);
        System.out.println("tipo Lista : " + tipoLista);

        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listFormulasAseguradas.get(index).setFormula(formulaSeleccionado);

                if (!crearFormulasAseguradas.contains(listFormulasAseguradas.get(index))) {
                    if (modificarFormulasAseguradas.isEmpty()) {
                        modificarFormulasAseguradas.add(listFormulasAseguradas.get(index));
                    } else if (!modificarFormulasAseguradas.contains(listFormulasAseguradas.get(index))) {
                        modificarFormulasAseguradas.add(listFormulasAseguradas.get(index));
                    }
                }
            } else {
                filtrarFormulasAseguradas.get(index).setFormula(formulaSeleccionado);

                if (!crearFormulasAseguradas.contains(filtrarFormulasAseguradas.get(index))) {
                    if (modificarFormulasAseguradas.isEmpty()) {
                        modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(index));
                    } else if (!modificarFormulasAseguradas.contains(filtrarFormulasAseguradas.get(index))) {
                        modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            System.out.println("ACTUALIZAR FORMULA : " + formulaSeleccionado.getNombrelargo());
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            System.out.println("ACTUALIZAR FORMULA NUEVO DEPARTAMENTO: " + formulaSeleccionado.getNombrelargo());
            nuevoFormulasAseguradas.setFormula(formulaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");
        } else if (tipoActualizacion == 2) {
            System.out.println("ACTUALIZAR FORMULA DUPLICAR DEPARTAMENO: " + formulaSeleccionado.getNombrelargo());
            duplicarFormulasAseguradas.setFormula(formulaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
        }
        filtradoFormulas = null;
        formulaSeleccionado = null;
        aceptar = true;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cambioFormulasAseguradas = true;
        context.reset("form:lovFormulas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovFormulas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovFormulas");
    }

    public void cancelarCambioFormulas() {
        filtradoFormulas = null;
        formulaSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovFormulas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovFormulas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').hide()");
    }

    public void actualizarProcesos() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("formula seleccionado : " + procesoSeleccionado.getDescripcion());
        System.out.println("tipo Actualizacion : " + tipoActualizacion);
        System.out.println("tipo Lista : " + tipoLista);

        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listFormulasAseguradas.get(index).setProceso(procesoSeleccionado);

                if (!crearFormulasAseguradas.contains(listFormulasAseguradas.get(index))) {
                    if (modificarFormulasAseguradas.isEmpty()) {
                        modificarFormulasAseguradas.add(listFormulasAseguradas.get(index));
                    } else if (!modificarFormulasAseguradas.contains(listFormulasAseguradas.get(index))) {
                        modificarFormulasAseguradas.add(listFormulasAseguradas.get(index));
                    }
                }
            } else {
                filtrarFormulasAseguradas.get(index).setProceso(procesoSeleccionado);

                if (!crearFormulasAseguradas.contains(filtrarFormulasAseguradas.get(index))) {
                    if (modificarFormulasAseguradas.isEmpty()) {
                        modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(index));
                    } else if (!modificarFormulasAseguradas.contains(filtrarFormulasAseguradas.get(index))) {
                        modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            System.out.println("ACTUALIZAR FORMULA : " + procesoSeleccionado.getDescripcion());
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            System.out.println("ACTUALIZAR FORMULA NUEVO DEPARTAMENTO: " + procesoSeleccionado.getDescripcion());
            nuevoFormulasAseguradas.setProceso(procesoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProceso");
        } else if (tipoActualizacion == 2) {
            System.out.println("ACTUALIZAR FORMULA DUPLICAR DEPARTAMENO: " + procesoSeleccionado.getDescripcion());
            duplicarFormulasAseguradas.setProceso(procesoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProceso");
        }
        filtradoProcesos = null;
        procesoSeleccionado = null;
        aceptar = true;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cambioFormulasAseguradas = true;
        context.reset("form:lovProcesos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovProcesos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('procesosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovProcesos");
    }

    public void cancelarCambioProcesos() {
        filtradoProcesos = null;
        procesoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovProcesos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovProcesos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('procesosDialogo').hide()");
    }

    public void actualizarPeriocidades() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("formula seleccionado : " + periodicidadSeleccionado.getNombre());
        System.out.println("tipo Actualizacion : " + tipoActualizacion);
        System.out.println("tipo Lista : " + tipoLista);

        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listFormulasAseguradas.get(index).setPeriodicidad(periodicidadSeleccionado);

                if (!crearFormulasAseguradas.contains(listFormulasAseguradas.get(index))) {
                    if (modificarFormulasAseguradas.isEmpty()) {
                        modificarFormulasAseguradas.add(listFormulasAseguradas.get(index));
                    } else if (!modificarFormulasAseguradas.contains(listFormulasAseguradas.get(index))) {
                        modificarFormulasAseguradas.add(listFormulasAseguradas.get(index));
                    }
                }
            } else {
                filtrarFormulasAseguradas.get(index).setPeriodicidad(periodicidadSeleccionado);

                if (!crearFormulasAseguradas.contains(filtrarFormulasAseguradas.get(index))) {
                    if (modificarFormulasAseguradas.isEmpty()) {
                        modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(index));
                    } else if (!modificarFormulasAseguradas.contains(filtrarFormulasAseguradas.get(index))) {
                        modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            System.out.println("ACTUALIZAR FORMULA : " + periodicidadSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            System.out.println("ACTUALIZAR FORMULA NUEVO DEPARTAMENTO: " + periodicidadSeleccionado.getNombre());
            nuevoFormulasAseguradas.setPeriodicidad(periodicidadSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPeriodicidad");
        } else if (tipoActualizacion == 2) {
            System.out.println("ACTUALIZAR FORMULA DUPLICAR DEPARTAMENO: " + periodicidadSeleccionado.getNombre());
            duplicarFormulasAseguradas.setPeriodicidad(periodicidadSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidad");
        }
        filtradoProcesos = null;
        procesoSeleccionado = null;
        aceptar = true;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cambioFormulasAseguradas = true;
        context.reset("form:lovPeriodicidades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovPeriodicidades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovPeriodicidades");
    }

    public void cancelarCambioPeriodicidades() {
        filtradoPeriodicidades = null;
        periodicidadSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovPeriodicidades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovPeriodicidades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').hide()");
    }

    public void modificarFormulasAseguradas(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;
        int coincidencias = 0;
        int contador = 0;
        boolean banderita = false;
        boolean banderita1 = false;
        boolean banderita2 = false;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            System.out.println("MODIFICANDO NORMA LABORAL : " + listFormulasAseguradas.get(indice).getFormula().getNombrelargo());
            if (!listFormulasAseguradas.get(indice).getFormula().getNombrelargo().equals("")) {
                if (tipoLista == 0) {
                    listFormulasAseguradas.get(indice).getFormula().setNombrelargo(backupFormula);
                } else {
                    listFormulasAseguradas.get(indice).getFormula().setNombrelargo(backupFormula);
                }

                for (int i = 0; i < listaFormulas.size(); i++) {
                    if (listaFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listFormulasAseguradas.get(indice).setFormula(listaFormulas.get(indiceUnicoElemento));
                    } else {
                        filtrarFormulasAseguradas.get(indice).setFormula(listaFormulas.get(indiceUnicoElemento));
                    }
                    listaFormulas.clear();
                    listaFormulas = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupFormula != null) {
                    if (tipoLista == 0) {
                        listFormulasAseguradas.get(index).getFormula().setNombrelargo(backupFormula);
                    } else {
                        filtrarFormulasAseguradas.get(index).getFormula().setNombrelargo(backupFormula);
                    }
                }
                tipoActualizacion = 0;
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearFormulasAseguradas.contains(listFormulasAseguradas.get(indice))) {

                        if (modificarFormulasAseguradas.isEmpty()) {
                            modificarFormulasAseguradas.add(listFormulasAseguradas.get(indice));
                        } else if (!modificarFormulasAseguradas.contains(listFormulasAseguradas.get(indice))) {
                            modificarFormulasAseguradas.add(listFormulasAseguradas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearFormulasAseguradas.contains(filtrarFormulasAseguradas.get(indice))) {

                        if (modificarFormulasAseguradas.isEmpty()) {
                            modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(indice));
                        } else if (!modificarFormulasAseguradas.contains(filtrarFormulasAseguradas.get(indice))) {
                            modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        } else if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
            System.out.println("MODIFICANDO NORMA LABORAL : " + listFormulasAseguradas.get(indice).getProceso().getDescripcion());
            if (!listFormulasAseguradas.get(indice).getProceso().getDescripcion().equals("")) {
                if (tipoLista == 0) {
                    listFormulasAseguradas.get(indice).getProceso().setDescripcion(backupProceso);
                } else {
                    listFormulasAseguradas.get(indice).getProceso().setDescripcion(backupProceso);
                }

                for (int i = 0; i < listaProcesos.size(); i++) {
                    if (listaProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listFormulasAseguradas.get(indice).setProceso(listaProcesos.get(indiceUnicoElemento));
                    } else {
                        filtrarFormulasAseguradas.get(indice).setProceso(listaProcesos.get(indiceUnicoElemento));
                    }
                    listaProcesos.clear();
                    listaProcesos = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:procesosDialogo");
                    PrimefacesContextUI.ejecutar("PF('procesosDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupProceso != null) {
                    if (tipoLista == 0) {
                        listFormulasAseguradas.get(index).getProceso().setDescripcion(backupProceso);
                    } else {
                        filtrarFormulasAseguradas.get(index).getProceso().setDescripcion(backupProceso);
                    }
                }
                tipoActualizacion = 0;
                System.out.println("PAIS ANTES DE MOSTRAR DIALOGO PERSONA : " + backupProceso);
                RequestContext.getCurrentInstance().update("form:procesosDialogo");
                PrimefacesContextUI.ejecutar("PF('procesosDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearFormulasAseguradas.contains(listFormulasAseguradas.get(indice))) {

                        if (modificarFormulasAseguradas.isEmpty()) {
                            modificarFormulasAseguradas.add(listFormulasAseguradas.get(indice));
                        } else if (!modificarFormulasAseguradas.contains(listFormulasAseguradas.get(indice))) {
                            modificarFormulasAseguradas.add(listFormulasAseguradas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearFormulasAseguradas.contains(filtrarFormulasAseguradas.get(indice))) {

                        if (modificarFormulasAseguradas.isEmpty()) {
                            modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(indice));
                        } else if (!modificarFormulasAseguradas.contains(filtrarFormulasAseguradas.get(indice))) {
                            modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
            System.out.println("MODIFICANDO NORMA LABORAL : " + listFormulasAseguradas.get(indice).getProceso().getDescripcion());
            if (!listFormulasAseguradas.get(indice).getPeriodicidad().getNombre().equals("")) {
                if (tipoLista == 0) {
                    listFormulasAseguradas.get(indice).getPeriodicidad().setNombre(backupPeriodicidad);
                } else {
                    listFormulasAseguradas.get(indice).getPeriodicidad().setNombre(backupPeriodicidad);
                }

                for (int i = 0; i < listaPeriodicidades.size(); i++) {
                    if (listaPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listFormulasAseguradas.get(indice).setPeriodicidad(listaPeriodicidades.get(indiceUnicoElemento));
                    } else {
                        filtrarFormulasAseguradas.get(indice).setPeriodicidad(listaPeriodicidades.get(indiceUnicoElemento));
                    }
                    listaPeriodicidades.clear();
                    listaPeriodicidades = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                    PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                System.out.println("BackUpPeriodicidad : " + backupPeriodicidad);

                if (tipoLista == 0) {
                    listFormulasAseguradas.get(indice).getPeriodicidad().setNombre(backupPeriodicidad);
                    listFormulasAseguradas.get(indice).setPeriodicidad(new Periodicidades());
                    System.out.println("listFormulasAseguradas.get(" + indice + ").getPeriodicidad() : = " + listFormulasAseguradas.get(indice).getPeriodicidad());
                } else {
                    filtrarFormulasAseguradas.get(indice).getPeriodicidad().setNombre(backupPeriodicidad);
                    filtrarFormulasAseguradas.get(indice).setPeriodicidad(new Periodicidades());
                }
                coincidencias = 1;

                tipoActualizacion = 0;

            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearFormulasAseguradas.contains(listFormulasAseguradas.get(indice))) {

                        if (modificarFormulasAseguradas.isEmpty()) {
                            modificarFormulasAseguradas.add(listFormulasAseguradas.get(indice));
                        } else if (!modificarFormulasAseguradas.contains(listFormulasAseguradas.get(indice))) {
                            modificarFormulasAseguradas.add(listFormulasAseguradas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearFormulasAseguradas.contains(filtrarFormulasAseguradas.get(indice))) {

                        if (modificarFormulasAseguradas.isEmpty()) {
                            modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(indice));
                        } else if (!modificarFormulasAseguradas.contains(filtrarFormulasAseguradas.get(indice))) {
                            modificarFormulasAseguradas.add(filtrarFormulasAseguradas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        }

    }

    public void borrandoFormulasAseguradas() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoFormulasAseguradas");
                if (!modificarFormulasAseguradas.isEmpty() && modificarFormulasAseguradas.contains(listFormulasAseguradas.get(index))) {
                    int modIndex = modificarFormulasAseguradas.indexOf(listFormulasAseguradas.get(index));
                    modificarFormulasAseguradas.remove(modIndex);
                    borrarFormulasAseguradas.add(listFormulasAseguradas.get(index));
                } else if (!crearFormulasAseguradas.isEmpty() && crearFormulasAseguradas.contains(listFormulasAseguradas.get(index))) {
                    int crearIndex = crearFormulasAseguradas.indexOf(listFormulasAseguradas.get(index));
                    crearFormulasAseguradas.remove(crearIndex);
                } else {
                    borrarFormulasAseguradas.add(listFormulasAseguradas.get(index));
                }
                listFormulasAseguradas.remove(index);
                infoRegistro = "Cantidad de registros: " + listFormulasAseguradas.size();
            }
            if (tipoLista == 1) {
                System.out.println("borrandoFormulasAseguradas ");
                if (!modificarFormulasAseguradas.isEmpty() && modificarFormulasAseguradas.contains(filtrarFormulasAseguradas.get(index))) {
                    int modIndex = modificarFormulasAseguradas.indexOf(filtrarFormulasAseguradas.get(index));
                    modificarFormulasAseguradas.remove(modIndex);
                    borrarFormulasAseguradas.add(filtrarFormulasAseguradas.get(index));
                } else if (!crearFormulasAseguradas.isEmpty() && crearFormulasAseguradas.contains(filtrarFormulasAseguradas.get(index))) {
                    int crearIndex = crearFormulasAseguradas.indexOf(filtrarFormulasAseguradas.get(index));
                    crearFormulasAseguradas.remove(crearIndex);
                } else {
                    borrarFormulasAseguradas.add(filtrarFormulasAseguradas.get(index));
                }
                int VCIndex = listFormulasAseguradas.indexOf(filtrarFormulasAseguradas.get(index));
                listFormulasAseguradas.remove(VCIndex);
                filtrarFormulasAseguradas.remove(index);
                infoRegistro = "Cantidad de registros: " + filtrarFormulasAseguradas.size();

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            cambioFormulasAseguradas = true;
        }

    }

    public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
        System.out.println("1...");
        if (valorCambio.equals("FORMULA")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarFormula = nuevoFormulasAseguradas.getFormula().getNombrelargo();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarFormula = duplicarFormulasAseguradas.getFormula().getNombrelargo();
            }

            System.out.println("CONCEPTO : " + nuevoYduplicarCompletarFormula);
        }
        if (valorCambio.equals("PROCESO")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarProcesos = nuevoFormulasAseguradas.getProceso().getDescripcion();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarProcesos = duplicarFormulasAseguradas.getProceso().getDescripcion();
            }

            System.out.println("PROCESO : " + nuevoYduplicarCompletarProcesos);
        }
        if (valorCambio.equals("PERIODICIDAD")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarPeriodicidad = nuevoFormulasAseguradas.getPeriodicidad().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarPeriodicidad = duplicarFormulasAseguradas.getPeriodicidad().getNombre();
            }

            System.out.println("PERIODICIDAD : " + nuevoYduplicarCompletarPeriodicidad);
        }

    }

    public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            System.out.println(" nueva Operando    Entro al if 'Centro costo'");
            System.out.println("NUEVO PERSONA :-------> " + nuevoFormulasAseguradas.getFormula().getNombrelargo());

            if (!nuevoFormulasAseguradas.getFormula().getNombrelargo().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarFormula);
                nuevoFormulasAseguradas.getFormula().setNombrelargo(nuevoYduplicarCompletarFormula);
                for (int i = 0; i < listaFormulas.size(); i++) {
                    if (listaFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    nuevoFormulasAseguradas.setFormula(listaFormulas.get(indiceUnicoElemento));
                    listaFormulas = null;
                    System.err.println("PERSONA GUARDADA :-----> " + nuevoFormulasAseguradas.getFormula().getNombrelargo());
                } else {
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoFormulasAseguradas.getFormula().setNombrelargo(nuevoYduplicarCompletarFormula);
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoFormulasAseguradas.setFormula(new Formulas());
                nuevoFormulasAseguradas.getFormula().setNombrelargo(" ");
                System.out.println("NUEVA NORMA LABORAL" + nuevoFormulasAseguradas.getFormula().getNombrelargo());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");

        } else if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
            System.out.println("NUEVO PROCESO :-------> " + nuevoFormulasAseguradas.getProceso().getDescripcion());

            if (!nuevoFormulasAseguradas.getProceso().getDescripcion().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarProcesos);
                nuevoFormulasAseguradas.getProceso().setDescripcion(nuevoYduplicarCompletarProcesos);
                for (int i = 0; i < listaProcesos.size(); i++) {
                    if (listaProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    nuevoFormulasAseguradas.setProceso(listaProcesos.get(indiceUnicoElemento));
                    listaProcesos = null;
                    System.err.println("PROCESO GUARDADO :-----> " + nuevoFormulasAseguradas.getProceso().getDescripcion());
                } else {
                    RequestContext.getCurrentInstance().update("form:procesosDialogo");
                    PrimefacesContextUI.ejecutar("PF('procesosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoFormulasAseguradas.getProceso().setDescripcion(nuevoYduplicarCompletarProcesos);
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoFormulasAseguradas.setProceso(new Procesos());
                nuevoFormulasAseguradas.getProceso().setDescripcion(" ");
                System.out.println("PROCESOSOS : : : : :  " + nuevoFormulasAseguradas.getProceso().getDescripcion());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoProceso");

        } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
            System.out.println("NUEVO PERIODICIDAD :-------> " + nuevoFormulasAseguradas.getPeriodicidad().getNombre());

            if (!nuevoFormulasAseguradas.getPeriodicidad().getNombre().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarPeriodicidad);
                nuevoFormulasAseguradas.getProceso().setDescripcion(nuevoYduplicarCompletarPeriodicidad);
                for (int i = 0; i < listaPeriodicidades.size(); i++) {
                    if (listaPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    nuevoFormulasAseguradas.setPeriodicidad(listaPeriodicidades.get(indiceUnicoElemento));
                    listaPeriodicidades = null;
                    System.err.println("PROCESO GUARDADO :-----> " + nuevoFormulasAseguradas.getPeriodicidad().getNombre());
                } else {
                    RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                    PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoFormulasAseguradas.setProceso(new Procesos());
                nuevoFormulasAseguradas.getProceso().setDescripcion(" ");
                System.out.println("PERIODICIDAD : : : : :  " + nuevoFormulasAseguradas.getProceso().getDescripcion());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPeriodicidad");

        }
    }

    public void asignarVariableFormulas(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:personasDialogo");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
    }

    public void asignarVariableProcesos(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:procesosDialogo");
        PrimefacesContextUI.ejecutar("PF('procesosDialogo').show()");
    }

    public void asignarVariablePeriodicidades(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
        PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
    }

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        System.out.println("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarFormula);

            if (!duplicarFormulasAseguradas.getFormula().getNombrelargo().equals("")) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarFormula);
                duplicarFormulasAseguradas.getFormula().setNombrelargo(nuevoYduplicarCompletarFormula);
                for (int i = 0; i < listaFormulas.size(); i++) {
                    if (listaFormulas.get(i).getNombrelargo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarFormulasAseguradas.setFormula(listaFormulas.get(indiceUnicoElemento));
                    listaFormulas = null;
                } else {
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    //duplicarFormulasAseguradas.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarFormulasAseguradas.setFormula(new Formulas());
                    duplicarFormulasAseguradas.getFormula().setNombrelargo(" ");

                    System.out.println("DUPLICAR PERSONA  : " + duplicarFormulasAseguradas.getFormula().getNombrelargo());
                    System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarFormula);
                    if (tipoLista == 0) {
                        listFormulasAseguradas.get(index).getFormula().setNombrelargo(nuevoYduplicarCompletarFormula);
                        System.err.println("tipo lista" + tipoLista);
                        System.err.println("Secuencia Parentesco " + listFormulasAseguradas.get(index).getFormula().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarFormulasAseguradas.get(index).getFormula().setNombrelargo(nuevoYduplicarCompletarFormula);
                    }

                }

            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
        } else if (confirmarCambio.equalsIgnoreCase("PROCESO")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarProcesos);

            if (!duplicarFormulasAseguradas.getProceso().getDescripcion().equals("")) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoYduplicarCompletarProcesos: " + nuevoYduplicarCompletarProcesos);
                duplicarFormulasAseguradas.getProceso().setDescripcion(nuevoYduplicarCompletarProcesos);
                for (int i = 0; i < listaProcesos.size(); i++) {
                    if (listaProcesos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarFormulasAseguradas.setProceso(listaProcesos.get(indiceUnicoElemento));
                    listaFormulas = null;
                } else {
                    RequestContext.getCurrentInstance().update("form:procesosDialogo");
                    PrimefacesContextUI.ejecutar("PF('procesosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarFormulasAseguradas.setProceso(new Procesos());
                    duplicarFormulasAseguradas.getProceso().setDescripcion(" ");

                    System.out.println("DUPLICAR PROCESO  : " + duplicarFormulasAseguradas.getProceso().getDescripcion());
                    System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarProcesos);
                    if (tipoLista == 0) {
                        listFormulasAseguradas.get(index).getProceso().setDescripcion(nuevoYduplicarCompletarProcesos);
                        //System.err.println("tipo lista" + tipoLista);
                        //System.err.println("Secuencia Parentesco " + listFormulasAseguradas.get(index).getFormula().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarFormulasAseguradas.get(index).getProceso().setDescripcion(nuevoYduplicarCompletarFormula);
                    }

                }

            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarProceso");
        } else if (confirmarCambio.equalsIgnoreCase("PERIODICIDAD")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR PERIODICIDAD : " + nuevoYduplicarCompletarPeriodicidad);

            if (!duplicarFormulasAseguradas.getPeriodicidad().getNombre().equals("")) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoYduplicarCompletarProcesos: " + nuevoYduplicarCompletarPeriodicidad);
                duplicarFormulasAseguradas.getPeriodicidad().setNombre(nuevoYduplicarCompletarPeriodicidad);
                for (int i = 0; i < listaProcesos.size(); i++) {
                    if (listaPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarFormulasAseguradas.setPeriodicidad(listaPeriodicidades.get(indiceUnicoElemento));
                    listaFormulas = null;
                } else {
                    RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                    PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarFormulasAseguradas.setPeriodicidad(new Periodicidades());
                    duplicarFormulasAseguradas.getPeriodicidad().setNombre(" ");

                    System.out.println("DUPLICAR PERIODICIDAD  : " + duplicarFormulasAseguradas.getPeriodicidad().getNombre());
                    System.out.println("nuevoYduplicarCompletarPERIODICIDAD : " + nuevoYduplicarCompletarPeriodicidad);
                    if (tipoLista == 0) {
                        listFormulasAseguradas.get(index).getPeriodicidad().setNombre(nuevoYduplicarCompletarPeriodicidad);
                        //System.err.println("tipo lista" + tipoLista);
                        //System.err.println("Secuencia Parentesco " + listFormulasAseguradas.get(index).getFormula().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarFormulasAseguradas.get(index).getProceso().setDescripcion(nuevoYduplicarCompletarFormula);
                    }

                }

            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPeriodicidad");
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarFormulasAseguradas.isEmpty() || !crearFormulasAseguradas.isEmpty() || !modificarFormulasAseguradas.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarFormulasAseguradas() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarFormulasAseguradas");
            if (!borrarFormulasAseguradas.isEmpty()) {
                administrarFormulasAseguradas.borrarFormulasAseguradas(borrarFormulasAseguradas);
                //mostrarBorrados
                registrosBorrados = borrarFormulasAseguradas.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarFormulasAseguradas.clear();
            }
            if (!modificarFormulasAseguradas.isEmpty()) {
                administrarFormulasAseguradas.modificarFormulasAseguradas(modificarFormulasAseguradas);
                modificarFormulasAseguradas.clear();
            }
            if (!crearFormulasAseguradas.isEmpty()) {
                administrarFormulasAseguradas.crearFormulasAseguradas(crearFormulasAseguradas);
                crearFormulasAseguradas.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listFormulasAseguradas = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            k = 0;
            guardado = true;

        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarFormulasAseguradas = listFormulasAseguradas.get(index);
            }
            if (tipoLista == 1) {
                editarFormulasAseguradas = filtrarFormulasAseguradas.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
                PrimefacesContextUI.ejecutar("PF('editPais').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFormulas");
                PrimefacesContextUI.ejecutar("PF('editFormulas').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editOperandos");
                PrimefacesContextUI.ejecutar("PF('editOperandos').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoFormulasAseguradas() {
        System.out.println("agregarNuevoFormulasAseguradas");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoFormulasAseguradas.getFormula().getNombrelargo() == null || nuevoFormulasAseguradas.getFormula().getNombrelargo().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Formula \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;//3

        }
        if (nuevoFormulasAseguradas.getProceso().getDescripcion() == null || nuevoFormulasAseguradas.getProceso().getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Proceso \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;//3

        }

        System.out.println("contador " + contador);
        System.out.println("Periodicidad " + nuevoFormulasAseguradas.getPeriodicidad().getNombre());

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                proceso = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:proceso");
                proceso.setFilterStyle("display: none; visibility: hidden;");
                personafir = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:personafir");
                personafir.setFilterStyle("display: none; visibility: hidden;");
                periodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:periodicidad");
                periodicidad.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarFormulasAseguradas = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoFormulasAseguradas.setSecuencia(l);
            crearFormulasAseguradas.add(nuevoFormulasAseguradas);

            listFormulasAseguradas.add(nuevoFormulasAseguradas);
            nuevoFormulasAseguradas = new FormulasAseguradas();
            nuevoFormulasAseguradas.setFormula(new Formulas());
            nuevoFormulasAseguradas.setProceso(new Procesos());
            nuevoFormulasAseguradas.setPeriodicidad(new Periodicidades());
            infoRegistro = "Cantidad de registros: " + listFormulasAseguradas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroFormulasAseguradas').hide()");
            index = -1;
            secRegistro = null;
            cambioFormulasAseguradas = true;

        } else {

            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoFormulasAseguradas() {
        System.out.println("limpiarNuevoFormulasAseguradas");
        nuevoFormulasAseguradas = new FormulasAseguradas();
        nuevoFormulasAseguradas.setFormula(new Formulas());
        nuevoFormulasAseguradas.setProceso(new Procesos());
        nuevoFormulasAseguradas.setPeriodicidad(new Periodicidades());
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void cargarNuevoFormulasAseguradas() {
        System.out.println("cargarNuevoFormulasAseguradas");

        duplicarFormulasAseguradas = new FormulasAseguradas();
        duplicarFormulasAseguradas.setFormula(new Formulas());
        duplicarFormulasAseguradas.setPeriodicidad(new Periodicidades());
        duplicarFormulasAseguradas.setProceso(new Procesos());
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.ejecutar("PF('nuevoRegistroFormulasAseguradas').show()");

    }

    public void duplicandoFormulasAseguradas() {
        System.out.println("duplicandoFormulasAseguradas");
        if (index >= 0) {
            duplicarFormulasAseguradas = new FormulasAseguradas();
            duplicarFormulasAseguradas.setFormula(new Formulas());
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarFormulasAseguradas.setSecuencia(l);
                duplicarFormulasAseguradas.setFormula(listFormulasAseguradas.get(index).getFormula());
                duplicarFormulasAseguradas.setProceso(listFormulasAseguradas.get(index).getProceso());
                duplicarFormulasAseguradas.setPeriodicidad(listFormulasAseguradas.get(index).getPeriodicidad());
            }
            if (tipoLista == 1) {
                duplicarFormulasAseguradas.setSecuencia(l);
                duplicarFormulasAseguradas.setFormula(filtrarFormulasAseguradas.get(index).getFormula());
                duplicarFormulasAseguradas.setProceso(filtrarFormulasAseguradas.get(index).getProceso());
                duplicarFormulasAseguradas.setPeriodicidad(filtrarFormulasAseguradas.get(index).getPeriodicidad());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroFormulasAseguradas').show()");
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;

        if (duplicarFormulasAseguradas.getFormula().getNombrelargo() == null || duplicarFormulasAseguradas.getFormula().getNombrelargo().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Formula \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarFormulasAseguradas.getProceso().getDescripcion() == null || duplicarFormulasAseguradas.getProceso().getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Proceso \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {
            if (crearFormulasAseguradas.contains(duplicarFormulasAseguradas)) {
                System.out.println("Ya lo contengo.");
            }
            listFormulasAseguradas.add(duplicarFormulasAseguradas);
            crearFormulasAseguradas.add(duplicarFormulasAseguradas);
            RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
            index = -1;
            System.out.println("--------------DUPLICAR------------------------");
            System.out.println("PERSONA : " + duplicarFormulasAseguradas.getFormula().getNombrelargo());
            System.out.println("--------------DUPLICAR------------------------");

            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                proceso = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:proceso");
                proceso.setFilterStyle("display: none; visibility: hidden;");
                personafir = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:personafir");
                personafir.setFilterStyle("display: none; visibility: hidden;");
                periodicidad = (Column) c.getViewRoot().findComponent("form:datosFormulasAseguradas:periodicidad");
                periodicidad.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
                bandera = 0;
                filtrarFormulasAseguradas = null;
                tipoLista = 0;
            }

            infoRegistro = "Cantidad de registros: " + listFormulasAseguradas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            duplicarFormulasAseguradas = new FormulasAseguradas();
            duplicarFormulasAseguradas.setFormula(new Formulas());

            PrimefacesContextUI.ejecutar("PF('duplicarRegistroFormulasAseguradas').hide()");
            index = -1;
        } else {

            contador = 0;

            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarFormulasAseguradas() {
        duplicarFormulasAseguradas = new FormulasAseguradas();
        duplicarFormulasAseguradas.setFormula(new Formulas());
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFormulasAseguradasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "FORMULASASEGURADAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFormulasAseguradasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "FORMULASASEGURADAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listFormulasAseguradas.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "FORMULASASEGURADAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                System.out.println("resultado: " + resultado);
                if (resultado == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                } else if (resultado == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("FORMULASASEGURADAS")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }
    private boolean cambioFormulasAseguradas = false;

    public void llamadoDialogoBuscarFormulas() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                // banderaSeleccionCentrosCostosPorEmpresa = true;
                PrimefacesContextUI.ejecutar("PF('confirmarGuardarFormulas').show()");

            } else {
                listFormulasAseguradasBoton = null;
                getListFormulasAseguradasBoton();
                index = -1;
                RequestContext.getCurrentInstance().update("formularioDialogos:lovCentrosCostos");
                PrimefacesContextUI.ejecutar("PF('buscarCentrosCostosDialogo').show()");

            }
        } catch (Exception e) {
            System.err.println("ERROR LLAMADO DIALOGO BUSCAR CENTROS COSTOS " + e);
        }

    }

    public void seleccionFormulaAsegurada() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            System.err.println("guardado = " + guardado);
            if (guardado == true) {
                System.err.println("1 = " + 1);
                listFormulasAseguradas.clear();
                listFormulasAseguradas = new ArrayList<FormulasAseguradas>();
                System.err.println("SELECCION FORMULA ASEGURADA " + formulaAseguradaSeleccionada.getFormula().getNombrelargo());
                listFormulasAseguradas.add(formulaAseguradaSeleccionada);
                System.err.println("listFormulasAseguradas tamaño " + listFormulasAseguradas.size());
                System.err.println("listFormulasAseguradas nombre " + listFormulasAseguradas.get(0).getFormula().getNombrelargo());
                formulaAseguradaSeleccionada = null;
                filterFormulasAseguradasBoton = null;
                aceptar = true;
                RequestContext.getCurrentInstance().update("form:datosFormulasAseguradas");
                PrimefacesContextUI.ejecutar("PF('buscarCentrosCostosDialogo').hide()");
                context.reset("formularioDialogos:lovCentrosCostos:globalFilter");
                mostrarTodos = false;
                buscarFormulas = true;
                RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
                RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
                if (listFormulasAseguradas == null || listFormulasAseguradas.isEmpty()) {
                    infoRegistro = "Cantidad de registros: 0 ";
                } else {
                    infoRegistro = "Cantidad de registros: " + listFormulasAseguradas.size();
                }
                RequestContext.getCurrentInstance().update("form:infoRegistro");

            } else {
                RequestContext.getCurrentInstance().update("form:confirmarGuardarFormulas");
                PrimefacesContextUI.ejecutar("PF('confirmarGuardarFormulas').show()");
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROL FORMULAS ASEGURADAS.seleccionaVigencia ERROR====" + e.getMessage());
        }
    }

    public void cancelarSeleccionConceptoSoporte() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            formulaAseguradaSeleccionada = null;
            filterFormulasAseguradasBoton = null;
            aceptar = true;
            index = -1;
            tipoActualizacion = -1;
            RequestContext.getCurrentInstance().update("form:aceptarNCC");

        } catch (Exception e) {
            System.out.println("ERROR CONTROLBETACENTROSCOSTOS.cancelarSeleccionVigencia ERROR====" + e.getMessage());
        }
    }

    public FormulasAseguradas getFormulaAseguradaSeleccionada() {
        return formulaAseguradaSeleccionada;
    }

    public void setFormulaAseguradaSeleccionada(FormulasAseguradas formulaAseguradaSeleccionada) {
        this.formulaAseguradaSeleccionada = formulaAseguradaSeleccionada;
    }

    public List<FormulasAseguradas> getListFormulasAseguradasBoton() {
        if (listFormulasAseguradasBoton == null) {
            listFormulasAseguradasBoton = administrarFormulasAseguradas.consultarFormulasAseguradas();
        }

        RequestContext context = RequestContext.getCurrentInstance();

        if (listFormulasAseguradasBoton == null || listFormulasAseguradasBoton.isEmpty()) {
            infoRegistroFormulasAseguradas = "Cantidad de registros: 0 ";
        } else {
            infoRegistroFormulasAseguradas = "Cantidad de registros: " + listFormulasAseguradasBoton.size();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroFormulasAseguradas");
        return listFormulasAseguradasBoton;
    }

    public void setListFormulasAseguradasBoton(List<FormulasAseguradas> listFormulasAseguradasBoton) {
        this.listFormulasAseguradasBoton = listFormulasAseguradasBoton;
    }

    public List<FormulasAseguradas> getFilterFormulasAseguradasBoton() {
        return filterFormulasAseguradasBoton;
    }

    public void setFilterFormulasAseguradasBoton(List<FormulasAseguradas> filterFormulasAseguradasBoton) {
        this.filterFormulasAseguradasBoton = filterFormulasAseguradasBoton;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<FormulasAseguradas> getListFormulasAseguradas() {
        if (listFormulasAseguradas == null) {
            listFormulasAseguradas = administrarFormulasAseguradas.consultarFormulasAseguradas();
            for (int i = 0; i < listFormulasAseguradas.size(); i++) {
                if (listFormulasAseguradas.get(i).getPeriodicidad() == null) {
                    listFormulasAseguradas.get(i).setPeriodicidad(new Periodicidades());
                }
            }
            RequestContext context = RequestContext.getCurrentInstance();

            if (listFormulasAseguradas == null || listFormulasAseguradas.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listFormulasAseguradas.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        }

        return listFormulasAseguradas;
    }

    public void setListFormulasAseguradas(List<FormulasAseguradas> listFormulasAseguradas) {
        this.listFormulasAseguradas = listFormulasAseguradas;
    }

    public List<FormulasAseguradas> getFiltrarFormulasAseguradas() {
        return filtrarFormulasAseguradas;
    }

    public void setFiltrarFormulasAseguradas(List<FormulasAseguradas> filtrarFormulasAseguradas) {
        this.filtrarFormulasAseguradas = filtrarFormulasAseguradas;
    }

    public FormulasAseguradas getNuevoFormulasAseguradas() {
        return nuevoFormulasAseguradas;
    }

    public void setNuevoFormulasAseguradas(FormulasAseguradas nuevoFormulasAseguradas) {
        this.nuevoFormulasAseguradas = nuevoFormulasAseguradas;
    }

    public FormulasAseguradas getDuplicarFormulasAseguradas() {
        return duplicarFormulasAseguradas;
    }

    public void setDuplicarFormulasAseguradas(FormulasAseguradas duplicarFormulasAseguradas) {
        this.duplicarFormulasAseguradas = duplicarFormulasAseguradas;
    }

    public FormulasAseguradas getEditarFormulasAseguradas() {
        return editarFormulasAseguradas;
    }

    public void setEditarFormulasAseguradas(FormulasAseguradas editarFormulasAseguradas) {
        this.editarFormulasAseguradas = editarFormulasAseguradas;
    }

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

    public List<Formulas> getListaFormulas() {
        if (listaFormulas == null) {
            listaFormulas = administrarFormulasAseguradas.consultarLOVFormulas();
        }

        RequestContext context = RequestContext.getCurrentInstance();

        if (listaFormulas == null || listaFormulas.isEmpty()) {
            infoRegistroFormulas = "Cantidad de registros: 0 ";
        } else {
            infoRegistroFormulas = "Cantidad de registros: " + listaFormulas.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroProcesos");
        return listaFormulas;
    }

    public void setListaFormulas(List<Formulas> listaFormulas) {
        this.listaFormulas = listaFormulas;
    }

    public List<Formulas> getFiltradoFormulas() {
        return filtradoFormulas;
    }

    public void setFiltradoFormulas(List<Formulas> filtradoFormulas) {
        this.filtradoFormulas = filtradoFormulas;
    }

    public Formulas getFormulaSeleccionado() {
        return formulaSeleccionado;
    }

    public void setFormulaSeleccionado(Formulas formulaSeleccionado) {
        this.formulaSeleccionado = formulaSeleccionado;
    }

    public List<Procesos> getListaProcesos() {
        if (listaProcesos == null) {
            listaProcesos = administrarFormulasAseguradas.consultarLOVProcesos();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listaProcesos == null || listaProcesos.isEmpty()) {
            infoRegistroProcesos = "Cantidad de registros: 0 ";
        } else {
            infoRegistroProcesos = "Cantidad de registros: " + listaProcesos.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroProcesos");
        return listaProcesos;

    }

    public void setListaProcesos(List<Procesos> listaProcesos) {
        this.listaProcesos = listaProcesos;
    }

    public Procesos getProcesoSeleccionado() {
        return procesoSeleccionado;
    }

    public void setProcesoSeleccionado(Procesos procesoSeleccionado) {
        this.procesoSeleccionado = procesoSeleccionado;
    }

    public List<Procesos> getFiltradoProcesos() {
        return filtradoProcesos;
    }

    public void setFiltradoProcesos(List<Procesos> filtradoProcesos) {
        this.filtradoProcesos = filtradoProcesos;
    }

    public List<Periodicidades> getListaPeriodicidades() {
        if (listaPeriodicidades == null) {
            listaPeriodicidades = administrarFormulasAseguradas.consultarLOVPPeriodicidades();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listaPeriodicidades == null || listaPeriodicidades.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistroPeriocididades = "Cantidad de registros: " + listaPeriodicidades.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroPeriocididades");
        return listaPeriodicidades;
    }

    public void setListaPeriodicidades(List<Periodicidades> listaPeriodicidades) {
        this.listaPeriodicidades = listaPeriodicidades;
    }

    public List<Periodicidades> getFiltradoPeriodicidades() {
        return filtradoPeriodicidades;
    }

    public void setFiltradoPeriodicidades(List<Periodicidades> filtradoPeriodicidades) {
        this.filtradoPeriodicidades = filtradoPeriodicidades;
    }

    public Periodicidades getPeriodicidadSeleccionado() {
        return periodicidadSeleccionado;
    }

    public void setPeriodicidadSeleccionado(Periodicidades periodicidadSeleccionado) {
        this.periodicidadSeleccionado = periodicidadSeleccionado;
    }

    public FormulasAseguradas getFormulaAseguradaSeleccion() {
        return formulaAseguradaSeleccion;
    }

    public void setFormulaAseguradaSeleccion(FormulasAseguradas formulaAseguradaSeleccion) {
        this.formulaAseguradaSeleccion = formulaAseguradaSeleccion;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroFormulas() {
        return infoRegistroFormulas;
    }

    public void setInfoRegistroFormulas(String infoRegistroFormulas) {
        this.infoRegistroFormulas = infoRegistroFormulas;
    }

    public String getInfoRegistroProcesos() {
        return infoRegistroProcesos;
    }

    public void setInfoRegistroProcesos(String infoRegistroProcesos) {
        this.infoRegistroProcesos = infoRegistroProcesos;
    }

    public String getInfoRegistroPeriocididades() {
        return infoRegistroPeriocididades;
    }

    public void setInfoRegistroPeriocididades(String infoRegistroPeriocididades) {
        this.infoRegistroPeriocididades = infoRegistroPeriocididades;
    }

    public String getInfoRegistroFormulasAseguradas() {
        return infoRegistroFormulasAseguradas;
    }

    public void setInfoRegistroFormulasAseguradas(String infoRegistroFormulasAseguradas) {
        this.infoRegistroFormulasAseguradas = infoRegistroFormulasAseguradas;
    }

    public boolean isBuscarFormulas() {
        return buscarFormulas;
    }

    public void setBuscarFormulas(boolean buscarFormulas) {
        this.buscarFormulas = buscarFormulas;
    }

    public boolean isMostrarTodos() {
        return mostrarTodos;
    }

    public void setMostrarTodos(boolean mostrarTodos) {
        this.mostrarTodos = mostrarTodos;
    }

}
