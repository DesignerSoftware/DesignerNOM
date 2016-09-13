/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Operandos;
import Entidades.ConceptosSoportes;
import Entidades.Conceptos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarConceptosSoportesInterface;
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
public class ControlConceptosSoportes implements Serializable {

    @EJB
    AdministrarConceptosSoportesInterface administrarConceptosSoportes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<ConceptosSoportes> listConceptosSoportes;
    private List<ConceptosSoportes> filtrarConceptosSoportes;
    private List<ConceptosSoportes> crearConceptosSoportes;
    private List<ConceptosSoportes> modificarConceptosSoportes;
    private List<ConceptosSoportes> borrarConceptosSoportes;
    private ConceptosSoportes nuevoConceptosSoportes;
    private ConceptosSoportes duplicarConceptosSoportes;
    private ConceptosSoportes editarConceptosSoportes;
    private ConceptosSoportes conceptoSoporteSeleccionado;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, personafir, cargo;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private BigInteger backupCodigo;
    private String backupPais;
    //---------------------------------
    private String backupOperando;
    private List<Conceptos> listaConceptos;
    private List<Conceptos> filtradoConceptos;
    private Conceptos conceptoSeleccionado;
    private String nuevoYduplicarCompletarPersona;
    //--------------------------------------
    private String backupConcepto;
    private List<Operandos> listaOperandos;
    private List<Operandos> filtradoOperandos;
    private Operandos operandoSeleccionado;
    private String nuevoYduplicarCompletarCargo;

    //---------------------------------
    private List<ConceptosSoportes> listConceptosSoportesBoton;
    private List<ConceptosSoportes> filterConceptosSoportesBoton;
    private ConceptosSoportes conceptoSoporteMostrado;
    
    private String paginaAnterior;

    private BigInteger secConceptoSeleccionado;
    
    private String infoRecursoConceptoLista; 

    public ControlConceptosSoportes() {
        paginaAnterior = "";
        banderaConceptoEscogido = true;
        listConceptosSoportesBoton = null;
        listConceptosSoportes = null;
        crearConceptosSoportes = new ArrayList<ConceptosSoportes>();
        modificarConceptosSoportes = new ArrayList<ConceptosSoportes>();
        borrarConceptosSoportes = new ArrayList<ConceptosSoportes>();
        permitirIndex = true;
        editarConceptosSoportes = new ConceptosSoportes();
        nuevoConceptosSoportes = new ConceptosSoportes();
        nuevoConceptosSoportes.setConcepto(new Conceptos());
        nuevoConceptosSoportes.setOperando(new Operandos());
        duplicarConceptosSoportes = new ConceptosSoportes();
        duplicarConceptosSoportes.setConcepto(new Conceptos());
        duplicarConceptosSoportes.setOperando(new Operandos());
        listaConceptos = null;
        filtradoConceptos = null;
        listaOperandos = null;
        filtradoOperandos = null;
        guardado = true;
        tamano = 270;
        mostrarTodos = true;
        buscarConceptoSoporte = false;
        aceptar = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarConceptosSoportes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlConceptosSoportes.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarConceptosSoportes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlConceptosSoportes eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listConceptosSoportes.get(index).getSecuencia();
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    //conceptocodigo
                    backupCodigo = listConceptosSoportes.get(index).getConcepto().getCodigo();
                    backupConcepto = listConceptosSoportes.get(index).getConcepto().getDescripcion();
                }
                if (cualCelda == 1) {
                    backupCodigo = listConceptosSoportes.get(index).getConcepto().getCodigo();
                    backupConcepto = listConceptosSoportes.get(index).getConcepto().getDescripcion();
                    System.out.println("CONCEPTO : " + backupConcepto);

                }
                if (cualCelda == 2) {
                    backupOperando = listConceptosSoportes.get(index).getOperando().getNombre();
                    System.out.println("OPERANDO : " + backupOperando);

                }

            } else if (tipoLista == 1) {
                if (cualCelda == 0) {
                    backupCodigo = filtrarConceptosSoportes.get(index).getConcepto().getCodigo();
                    backupConcepto = filtrarConceptosSoportes.get(index).getConcepto().getDescripcion();
                }
                if (cualCelda == 1) {
                    backupCodigo = filtrarConceptosSoportes.get(index).getConcepto().getCodigo();
                    backupConcepto = filtrarConceptosSoportes.get(index).getConcepto().getDescripcion();
                    System.out.println("CONCEPTO : " + backupConcepto);

                }
                if (cualCelda == 2) {
                    backupOperando = filtrarConceptosSoportes.get(index).getOperando().getNombre();
                    System.out.println("OPERANDO : " + backupOperando);

                }
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("\n ENTRE A ControlConceptosSoportes.asignarIndex \n");
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
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                dig = -1;
            }
            if (dig == 2) {
                if (tipoLista == 0) {
                    secConceptoSeleccionado = listConceptosSoportes.get(indice).getConcepto().getSecuencia();
                } else {
                    secConceptoSeleccionado = filtrarConceptosSoportes.get(indice).getConcepto().getSecuencia();
                }
                listaOperandos = null;
                getListaOperandos();
                RequestContext.getCurrentInstance().update("form:cargosDialogo");
                PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                dig = -1;
            }
        } catch (Exception e) {
            System.out.println("ERROR ControlConceptosSoportes.asignarIndex ERROR======" + e.getMessage());
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
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }
            if (cualCelda == 2) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:cargosDialogo");
                PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
            }
        }
    }
    private String infoRegistro;

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            bandera = 0;
            filtrarConceptosSoportes = null;
            tipoLista = 0;
        }

        borrarConceptosSoportes.clear();
        crearConceptosSoportes.clear();
        modificarConceptosSoportes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listConceptosSoportes = null;
        guardado = true;
        permitirIndex = true;
        getListConceptosSoportes();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listConceptosSoportes == null || listConceptosSoportes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listConceptosSoportes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        mostrarTodos = true;
        buscarConceptoSoporte = false;
        RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
        RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
        RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            bandera = 0;
            filtrarConceptosSoportes = null;
            tipoLista = 0;
        }

        borrarConceptosSoportes.clear();
        crearConceptosSoportes.clear();
        modificarConceptosSoportes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listConceptosSoportes = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void cancelarModificacionCambio() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            bandera = 0;
            filtrarConceptosSoportes = null;
            tipoLista = 0;
        }

        borrarConceptosSoportes.clear();
        crearConceptosSoportes.clear();
        modificarConceptosSoportes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listConceptosSoportes = null;
        guardado = true;
        permitirIndex = true;
        seleccionConceptoSoporte();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            tamano = 250;
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:codigo");
            codigo.setFilterStyle("width: 85%");
            personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:personafir");
            personafir.setFilterStyle("width: 85%");
            cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:cargo");
            cargo.setFilterStyle("width: 85%");
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            FacesContext c = FacesContext.getCurrentInstance();
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            bandera = 0;
            filtrarConceptosSoportes = null;
            tipoLista = 0;
        }
    }

    public void actualizarConceptos() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("concepto seleccionado : " + conceptoSeleccionado.getDescripcion());
        System.out.println("tipo Actualizacion : " + tipoActualizacion);
        System.out.println("tipo Lista : " + tipoLista);

        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listConceptosSoportes.get(index).setConcepto(conceptoSeleccionado);

                if (!crearConceptosSoportes.contains(listConceptosSoportes.get(index))) {
                    if (modificarConceptosSoportes.isEmpty()) {
                        modificarConceptosSoportes.add(listConceptosSoportes.get(index));
                    } else if (!modificarConceptosSoportes.contains(listConceptosSoportes.get(index))) {
                        modificarConceptosSoportes.add(listConceptosSoportes.get(index));
                    }
                }
            } else {
                filtrarConceptosSoportes.get(index).setConcepto(conceptoSeleccionado);

                if (!crearConceptosSoportes.contains(filtrarConceptosSoportes.get(index))) {
                    if (modificarConceptosSoportes.isEmpty()) {
                        modificarConceptosSoportes.add(filtrarConceptosSoportes.get(index));
                    } else if (!modificarConceptosSoportes.contains(filtrarConceptosSoportes.get(index))) {
                        modificarConceptosSoportes.add(filtrarConceptosSoportes.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            System.out.println("ACTUALIZAR CONCEPTO SELECCIONADO : " + conceptoSeleccionado.getDescripcion());
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            System.out.println("ACTUALIZAR CONCEPTO NUEVO DEPARTAMENTO: " + conceptoSeleccionado.getDescripcion());
            nuevoConceptosSoportes.setConcepto(conceptoSeleccionado);
            secConceptoSeleccionado = nuevoConceptosSoportes.getConcepto().getSecuencia();
            System.out.println("secuencia concepto seleccionado : " + secConceptoSeleccionado);
            listaOperandos = null;
            getListaOperandos();
            banderaConceptoEscogido = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
            RequestContext.getCurrentInstance().update("formularioDialogos:btnnuevoCargo");
            getListaOperandos();
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");
        } else if (tipoActualizacion == 2) {
            System.out.println("ACTUALIZAR CONCEPTO DUPLICAR DEPARTAMENO: " + conceptoSeleccionado.getDescripcion());
            duplicarConceptosSoportes.setConcepto(conceptoSeleccionado);
            secConceptoSeleccionado = nuevoConceptosSoportes.getConcepto().getSecuencia();
            listaOperandos = null;
            getListaOperandos();

            banderaConceptoEscogido = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
            RequestContext.getCurrentInstance().update("formularioDialogos:btnduplicarCargo");
            getListaOperandos();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
        }
        filtradoConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cambioConceptosSoportes = true;
        context.reset("form:lovConceptos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovConceptos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovConceptos");
    }

    public void actualizarOperandos() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("ciudad seleccionado : " + operandoSeleccionado.getNombre());
        System.out.println("tipo Actualizacion : " + tipoActualizacion);
        System.out.println("tipo Lista : " + tipoLista);

        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listConceptosSoportes.get(index).setOperando(operandoSeleccionado);

                if (!crearConceptosSoportes.contains(listConceptosSoportes.get(index))) {
                    if (modificarConceptosSoportes.isEmpty()) {
                        modificarConceptosSoportes.add(listConceptosSoportes.get(index));
                    } else if (!modificarConceptosSoportes.contains(listConceptosSoportes.get(index))) {
                        modificarConceptosSoportes.add(listConceptosSoportes.get(index));
                    }
                }
            } else {
                filtrarConceptosSoportes.get(index).setOperando(operandoSeleccionado);

                if (!crearConceptosSoportes.contains(filtrarConceptosSoportes.get(index))) {
                    if (modificarConceptosSoportes.isEmpty()) {
                        modificarConceptosSoportes.add(filtrarConceptosSoportes.get(index));
                    } else if (!modificarConceptosSoportes.contains(filtrarConceptosSoportes.get(index))) {
                        modificarConceptosSoportes.add(filtrarConceptosSoportes.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            System.out.println("ACTUALIZAR OPERANDOS CARGO SELECCIONADO : " + operandoSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            System.out.println("ACTUALIZAR OPERANDOS NUEVO DEPARTAMENTO: " + operandoSeleccionado.getNombre());
            nuevoConceptosSoportes.setOperando(operandoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
        } else if (tipoActualizacion == 2) {
            System.out.println("ACTUALIZAR OPERANDOS DUPLICAR DEPARTAMENO: " + operandoSeleccionado.getNombre());
            duplicarConceptosSoportes.setOperando(operandoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
        }
        filtradoConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        cambioConceptosSoportes = true;
        context.reset("form:lovOperandos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovOperandos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('cargosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovOperandos");
        //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
    }

    public void cancelarCambioConceptos() {
        if (index >= 0) {
            listConceptosSoportes.get(index).getConcepto().setDescripcion(backupConcepto);
            listConceptosSoportes.get(index).getConcepto().setCodigo(backupCodigo);
        }
        filtradoConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovConceptos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovConceptos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').hide()");
    }

    public void cancelarCambioOperandos() {
        filtradoOperandos = null;
        operandoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovOperandos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovOperandos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('cargosDialogo').hide()");
    }

    public void modificarConceptosSoportes(int indice, String confirmarCambio, String valorConfirmar) {
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

        if (confirmarCambio.equalsIgnoreCase("PERSONASCODIGO")) {
            System.out.println("MODIFICANDO CODIGO CONCEPTO: " + listConceptosSoportes.get(indice).getConcepto().getDescripcion());
            if (!listConceptosSoportes.get(indice).getConcepto().getDescripcion().equals("")) {
                if (tipoLista == 0) {
                    listConceptosSoportes.get(indice).getConcepto().setCodigo(backupCodigo);
                    listConceptosSoportes.get(indice).getConcepto().setDescripcion(backupConcepto);
                } else {
                    filtrarConceptosSoportes.get(indice).getConcepto().setCodigo(backupCodigo);
                    filtrarConceptosSoportes.get(indice).getConcepto().setDescripcion(backupConcepto);
                }

                for (int i = 0; i < listaConceptos.size(); i++) {
                    if (listaConceptos.get(i).getCodigo().equals(new BigInteger(valorConfirmar))) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(indice).setConcepto(listaConceptos.get(indiceUnicoElemento));
                    } else {
                        filtrarConceptosSoportes.get(indice).setConcepto(listaConceptos.get(indiceUnicoElemento));
                    }
                    listaConceptos.clear();
                    listaConceptos = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupConcepto != null) {
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(indice).getConcepto().setCodigo(backupCodigo);
                        listConceptosSoportes.get(indice).getConcepto().setDescripcion(backupConcepto);
                    } else {
                        filtrarConceptosSoportes.get(indice).getConcepto().setCodigo(backupCodigo);
                        filtrarConceptosSoportes.get(indice).getConcepto().setDescripcion(backupConcepto);
                    }
                }
                tipoActualizacion = 0;
                System.out.println("CODIGO DIALOGO CONCEPTO : " + backupCodigo);
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearConceptosSoportes.contains(listConceptosSoportes.get(indice))) {

                        if (modificarConceptosSoportes.isEmpty()) {
                            modificarConceptosSoportes.add(listConceptosSoportes.get(indice));
                        } else if (!modificarConceptosSoportes.contains(listConceptosSoportes.get(indice))) {
                            modificarConceptosSoportes.add(listConceptosSoportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearConceptosSoportes.contains(filtrarConceptosSoportes.get(indice))) {

                        if (modificarConceptosSoportes.isEmpty()) {
                            modificarConceptosSoportes.add(filtrarConceptosSoportes.get(indice));
                        } else if (!modificarConceptosSoportes.contains(filtrarConceptosSoportes.get(indice))) {
                            modificarConceptosSoportes.add(filtrarConceptosSoportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            cambioConceptosSoportes = true;

        } else if (confirmarCambio.equalsIgnoreCase("PERSONAS")) {
            System.out.println("MODIFICANDO NORMA LABORAL : " + listConceptosSoportes.get(indice).getConcepto().getDescripcion());
            if (!listConceptosSoportes.get(indice).getConcepto().getDescripcion().equals("")) {
                if (tipoLista == 0) {
                    listConceptosSoportes.get(indice).getConcepto().setCodigo(backupCodigo);
                    listConceptosSoportes.get(indice).getConcepto().setDescripcion(backupConcepto);
                } else {
                    filtrarConceptosSoportes.get(indice).getConcepto().setCodigo(backupCodigo);
                    filtrarConceptosSoportes.get(indice).getConcepto().setDescripcion(backupConcepto);
                }

                for (int i = 0; i < listaConceptos.size(); i++) {
                    if (listaConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(indice).setConcepto(listaConceptos.get(indiceUnicoElemento));
                    } else {
                        filtrarConceptosSoportes.get(indice).setConcepto(listaConceptos.get(indiceUnicoElemento));
                    }
                    listaConceptos.clear();
                    listaConceptos = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupConcepto != null) {
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(indice).getConcepto().setCodigo(backupCodigo);
                        listConceptosSoportes.get(indice).getConcepto().setDescripcion(backupConcepto);
                    } else {
                        filtrarConceptosSoportes.get(indice).getConcepto().setCodigo(backupCodigo);
                        filtrarConceptosSoportes.get(indice).getConcepto().setDescripcion(backupConcepto);
                    }
                }
                tipoActualizacion = 0;
                System.out.println("BACKUPCONCEPTO CUANDO ES NULO : " + backupConcepto);
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearConceptosSoportes.contains(listConceptosSoportes.get(indice))) {

                        if (modificarConceptosSoportes.isEmpty()) {
                            modificarConceptosSoportes.add(listConceptosSoportes.get(indice));
                        } else if (!modificarConceptosSoportes.contains(listConceptosSoportes.get(indice))) {
                            modificarConceptosSoportes.add(listConceptosSoportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearConceptosSoportes.contains(filtrarConceptosSoportes.get(indice))) {

                        if (modificarConceptosSoportes.isEmpty()) {
                            modificarConceptosSoportes.add(filtrarConceptosSoportes.get(indice));
                        } else if (!modificarConceptosSoportes.contains(filtrarConceptosSoportes.get(indice))) {
                            modificarConceptosSoportes.add(filtrarConceptosSoportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        } else if (confirmarCambio.equalsIgnoreCase("CARGOS")) {
            System.out.println("MODIFICANDO CARGO: " + listConceptosSoportes.get(indice).getOperando().getNombre());
            if (!listConceptosSoportes.get(indice).getOperando().getNombre().equals("")) {
                if (tipoLista == 0) {
                    listConceptosSoportes.get(indice).getOperando().setNombre(backupOperando);
                } else {
                    listConceptosSoportes.get(indice).getOperando().setNombre(backupOperando);
                }

                for (int i = 0; i < listaOperandos.size(); i++) {
                    if (listaOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(indice).setOperando(listaOperandos.get(indiceUnicoElemento));
                    } else {
                        filtrarConceptosSoportes.get(indice).setOperando(listaOperandos.get(indiceUnicoElemento));
                    }
                    listaOperandos.clear();
                    listaOperandos = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:cargosDialogo");
                    PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupOperando != null) {
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(index).getOperando().setNombre(backupOperando);
                    } else {
                        filtrarConceptosSoportes.get(index).getOperando().setNombre(backupOperando);
                    }
                }
                tipoActualizacion = 0;
                System.out.println("PAIS ANTES DE MOSTRAR DIALOGO CARGOS : " + backupOperando);
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearConceptosSoportes.contains(listConceptosSoportes.get(indice))) {

                        if (modificarConceptosSoportes.isEmpty()) {
                            modificarConceptosSoportes.add(listConceptosSoportes.get(indice));
                        } else if (!modificarConceptosSoportes.contains(listConceptosSoportes.get(indice))) {
                            modificarConceptosSoportes.add(listConceptosSoportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearConceptosSoportes.contains(filtrarConceptosSoportes.get(indice))) {

                        if (modificarConceptosSoportes.isEmpty()) {
                            modificarConceptosSoportes.add(filtrarConceptosSoportes.get(indice));
                        } else if (!modificarConceptosSoportes.contains(filtrarConceptosSoportes.get(indice))) {
                            modificarConceptosSoportes.add(filtrarConceptosSoportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            cambioConceptosSoportes = true;
        }

    }

    public void modificarConceptosSoportesCodigo(int indice, String confirmarCambio, BigInteger valorConfirmar) {
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

        if (confirmarCambio.equalsIgnoreCase("PERSONASCODIGO")) {
            System.out.println("MODIFICANDO CODIGO CONCEPTO: " + listConceptosSoportes.get(indice).getConcepto().getCodigo());
            if (!listConceptosSoportes.get(indice).getConcepto().getDescripcion().equals("")) {
                if (tipoLista == 0) {
                    listConceptosSoportes.get(indice).getConcepto().setCodigo(backupCodigo);
                    listConceptosSoportes.get(indice).getConcepto().setDescripcion(backupConcepto);
                } else {
                    filtrarConceptosSoportes.get(indice).getConcepto().setCodigo(backupCodigo);
                    filtrarConceptosSoportes.get(indice).getConcepto().setDescripcion(backupConcepto);
                }

                for (int i = 0; i < listaConceptos.size(); i++) {
                    if (listaConceptos.get(i).getCodigo().equals(valorConfirmar)) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(indice).setConcepto(listaConceptos.get(indiceUnicoElemento));
                    } else {
                        filtrarConceptosSoportes.get(indice).setConcepto(listaConceptos.get(indiceUnicoElemento));
                    }
                    listaConceptos.clear();
                    listaConceptos = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupCodigo != null) {
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(index).getConcepto().setCodigo(backupCodigo);
                        listConceptosSoportes.get(index).getConcepto().setDescripcion(backupConcepto);
                    } else {
                        filtrarConceptosSoportes.get(index).getConcepto().setCodigo(backupCodigo);
                        filtrarConceptosSoportes.get(index).getConcepto().setDescripcion(backupConcepto);
                    }
                }
                tipoActualizacion = 0;
                System.out.println("CODIGO DIALOGO CONCEPTO : " + backupCodigo);
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearConceptosSoportes.contains(listConceptosSoportes.get(indice))) {

                        if (modificarConceptosSoportes.isEmpty()) {
                            modificarConceptosSoportes.add(listConceptosSoportes.get(indice));
                        } else if (!modificarConceptosSoportes.contains(listConceptosSoportes.get(indice))) {
                            modificarConceptosSoportes.add(listConceptosSoportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearConceptosSoportes.contains(filtrarConceptosSoportes.get(indice))) {

                        if (modificarConceptosSoportes.isEmpty()) {
                            modificarConceptosSoportes.add(filtrarConceptosSoportes.get(indice));
                        } else if (!modificarConceptosSoportes.contains(filtrarConceptosSoportes.get(indice))) {
                            modificarConceptosSoportes.add(filtrarConceptosSoportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }
            cambioConceptosSoportes = true;
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        }

    }

    public void borrandoConceptosSoportes() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoConceptosSoportes");
                if (!modificarConceptosSoportes.isEmpty() && modificarConceptosSoportes.contains(listConceptosSoportes.get(index))) {
                    int modIndex = modificarConceptosSoportes.indexOf(listConceptosSoportes.get(index));
                    modificarConceptosSoportes.remove(modIndex);
                    borrarConceptosSoportes.add(listConceptosSoportes.get(index));
                } else if (!crearConceptosSoportes.isEmpty() && crearConceptosSoportes.contains(listConceptosSoportes.get(index))) {
                    int crearIndex = crearConceptosSoportes.indexOf(listConceptosSoportes.get(index));
                    crearConceptosSoportes.remove(crearIndex);
                } else {
                    borrarConceptosSoportes.add(listConceptosSoportes.get(index));
                }
                listConceptosSoportes.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoConceptosSoportes ");
                if (!modificarConceptosSoportes.isEmpty() && modificarConceptosSoportes.contains(filtrarConceptosSoportes.get(index))) {
                    int modIndex = modificarConceptosSoportes.indexOf(filtrarConceptosSoportes.get(index));
                    modificarConceptosSoportes.remove(modIndex);
                    borrarConceptosSoportes.add(filtrarConceptosSoportes.get(index));
                } else if (!crearConceptosSoportes.isEmpty() && crearConceptosSoportes.contains(filtrarConceptosSoportes.get(index))) {
                    int crearIndex = crearConceptosSoportes.indexOf(filtrarConceptosSoportes.get(index));
                    crearConceptosSoportes.remove(crearIndex);
                } else {
                    borrarConceptosSoportes.add(filtrarConceptosSoportes.get(index));
                }
                int VCIndex = listConceptosSoportes.indexOf(filtrarConceptosSoportes.get(index));
                listConceptosSoportes.remove(VCIndex);
                filtrarConceptosSoportes.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            infoRegistro = "Cantidad de registros: " + listConceptosSoportes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            cambioConceptosSoportes = true;
        }

    }
    private BigInteger nuevoYduplicarCompletarCodigoConcepto;

    public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
        System.out.println("1...");
        if (valorCambio.equals("CODIGOCONCEPTO")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarCodigoConcepto = nuevoConceptosSoportes.getConcepto().getCodigo();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarCodigoConcepto = duplicarConceptosSoportes.getConcepto().getCodigo();
            }
            System.out.println("CARGO : " + nuevoYduplicarCompletarCodigoConcepto);
        } else if (valorCambio.equals("PERSONA")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarPersona = nuevoConceptosSoportes.getConcepto().getDescripcion();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarPersona = duplicarConceptosSoportes.getConcepto().getDescripcion();
            }

            System.out.println("PERSONA : " + nuevoYduplicarCompletarPersona);
        } else if (valorCambio.equals("CARGO")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarCargo = nuevoConceptosSoportes.getOperando().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarCargo = duplicarConceptosSoportes.getOperando().getNombre();
            }
            System.out.println("CARGO : " + nuevoYduplicarCompletarCargo);
        }

    }

    public void autocompletarNuevoCodigoBigInteger(String confirmarCambio, BigInteger valorConfirmar, int tipoNuevo) {

        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (confirmarCambio.equalsIgnoreCase("CODIGOCONCEPTO")) {
            System.out.println(" nueva Operando    Entro al if 'Centro costo'");
            System.out.println("NUEVO PERSONA :-------> " + nuevoConceptosSoportes.getConcepto().getCodigo());

            if (!nuevoConceptosSoportes.getConcepto().getDescripcion().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarCodigoConcepto);
                nuevoConceptosSoportes.getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
                getListaConceptos();
                for (int i = 0; i < listaConceptos.size(); i++) {
                    if (listaConceptos.get(i).getCodigo().equals(valorConfirmar)) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    nuevoConceptosSoportes.setConcepto(listaConceptos.get(indiceUnicoElemento));
                    listaConceptos = null;
                    secConceptoSeleccionado = nuevoConceptosSoportes.getConcepto().getSecuencia();
                    listaOperandos = null;
                    banderaConceptoEscogido = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:btnnuevoCargo");
                    getListaOperandos();

                    System.err.println("PERSONA GUARDADA :-----> " + nuevoConceptosSoportes.getConcepto().getDescripcion());
                } else {
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoConceptosSoportes.getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoConceptosSoportes.setConcepto(new Conceptos());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");
        }

    }

    private boolean banderaConceptoEscogido = true;

    public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
            System.out.println(" nueva Operando    Entro al if 'Centro costo'");
            System.out.println("NUEVO PERSONA :-------> " + nuevoConceptosSoportes.getConcepto().getDescripcion());

            if (!nuevoConceptosSoportes.getConcepto().getDescripcion().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarPersona);
                nuevoConceptosSoportes.getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
                for (int i = 0; i < listaConceptos.size(); i++) {
                    if (listaConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    System.err.println("CONCEPTO OPERANDO GUARDADA :-----> " + nuevoConceptosSoportes.getConcepto().getDescripcion());
                    nuevoConceptosSoportes.setConcepto(listaConceptos.get(indiceUnicoElemento));
                    listaConceptos = null;
                    secConceptoSeleccionado = nuevoConceptosSoportes.getConcepto().getSecuencia();
                    listaOperandos = null;
                    banderaConceptoEscogido = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:btnnuevoCargo");
                    getListaOperandos();
                } else {
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoConceptosSoportes.getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoConceptosSoportes.setConcepto(new Conceptos());
                nuevoConceptosSoportes.getConcepto().setDescripcion(" ");
                System.out.println("NUEVA NORMA LABORAL" + nuevoConceptosSoportes.getConcepto().getDescripcion());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");

        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            System.out.println(" nueva Operando    Entro al if 'Centro costo'");
            System.out.println("NUEVO PERSONA :-------> " + nuevoConceptosSoportes.getOperando().getNombre());

            if (!nuevoConceptosSoportes.getOperando().getNombre().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarCargo);
                nuevoConceptosSoportes.getOperando().setNombre(nuevoYduplicarCompletarCargo);
                for (int i = 0; i < listaOperandos.size(); i++) {
                    if (listaOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    nuevoConceptosSoportes.setOperando(listaOperandos.get(indiceUnicoElemento));
                    listaOperandos = null;
                    System.err.println("CARGO GUARDADA :-----> " + nuevoConceptosSoportes.getOperando().getNombre());
                } else {
                    RequestContext.getCurrentInstance().update("form:cargosDialogo");
                    PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoConceptosSoportes.getOperando().setNombre(nuevoYduplicarCompletarCargo);
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoConceptosSoportes.setOperando(new Operandos());
                nuevoConceptosSoportes.getOperando().setNombre(" ");
                System.out.println("NUEVO CARGO " + nuevoConceptosSoportes.getOperando().getNombre());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
        }

    }

    public void asignarVariableConceptos(int tipoNuevo) {
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

    public void asignarVariableOperandos(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:cargosDialogo");
        PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
    }

    public void autocompletarDuplicadoCodigoBigInteger(String confirmarCambio, BigInteger valorConfirmar, int tipoNuevo) {
        System.out.println("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CODIGOCONCEPTO")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarCodigoConcepto);

            if (duplicarConceptosSoportes.getConcepto().getCodigo() != null) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoYduplicarCompletarCodigoConcepto: " + nuevoYduplicarCompletarCodigoConcepto);
                duplicarConceptosSoportes.getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
                for (int i = 0; i < listaConceptos.size(); i++) {
                    if (listaConceptos.get(i).getCodigo().equals(valorConfirmar)) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarConceptosSoportes.setConcepto(listaConceptos.get(indiceUnicoElemento));
                    listaConceptos = null;
                    secConceptoSeleccionado = duplicarConceptosSoportes.getConcepto().getSecuencia();
                    listaOperandos = null;
                    banderaConceptoEscogido = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:btnduplicarCargo");
                } else {
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    //duplicarConceptosSoportes.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarConceptosSoportes.setConcepto(new Conceptos());

                    System.out.println("DUPLICAR PERSONA  : " + duplicarConceptosSoportes.getConcepto().getCodigo());
                    System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarCodigoConcepto);
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(index).getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
                        System.err.println("tipo lista" + tipoLista);
                        System.err.println("Secuencia Parentesco " + listConceptosSoportes.get(index).getConcepto().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarConceptosSoportes.get(index).getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
                    }

                }

            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
        }
    }

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        System.out.println("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarPersona);

            if (!duplicarConceptosSoportes.getConcepto().getDescripcion().equals("")) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarPersona);
                duplicarConceptosSoportes.getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
                for (int i = 0; i < listaConceptos.size(); i++) {
                    if (listaConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarConceptosSoportes.setConcepto(listaConceptos.get(indiceUnicoElemento));
                    listaConceptos = null;
                    secConceptoSeleccionado = duplicarConceptosSoportes.getConcepto().getSecuencia();
                    listaOperandos = null;
                    banderaConceptoEscogido = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
                    RequestContext.getCurrentInstance().update("formularioDialogos:btnduplicarCargo");
                } else {
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    //duplicarConceptosSoportes.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarConceptosSoportes.setConcepto(new Conceptos());
                    duplicarConceptosSoportes.getConcepto().setDescripcion(" ");

                    System.out.println("DUPLICAR PERSONA  : " + duplicarConceptosSoportes.getConcepto().getDescripcion());
                    System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarPersona);
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(index).getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
                        System.err.println("tipo lista" + tipoLista);
                        System.err.println("Secuencia Parentesco " + listConceptosSoportes.get(index).getConcepto().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarConceptosSoportes.get(index).getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
                    }

                }

            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarCargo);

            if (!duplicarConceptosSoportes.getOperando().getNombre().equals("")) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarCargo);
                duplicarConceptosSoportes.getOperando().setNombre(nuevoYduplicarCompletarCargo);
                for (int i = 0; i < listaOperandos.size(); i++) {
                    if (listaOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarConceptosSoportes.setOperando(listaOperandos.get(indiceUnicoElemento));
                    listaOperandos = null;
                    getListaOperandos();
                } else {
                    RequestContext.getCurrentInstance().update("form:cargosDialogo");
                    PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    //duplicarConceptosSoportes.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarConceptosSoportes.setOperando(new Operandos());
                    duplicarConceptosSoportes.getOperando().setNombre(" ");

                    System.out.println("DUPLICAR CARGO  : " + duplicarConceptosSoportes.getOperando().getNombre());
                    System.out.println("nuevoYduplicarCompletarCARGO : " + nuevoYduplicarCompletarCargo);
                    if (tipoLista == 0) {
                        listConceptosSoportes.get(index).getOperando().setNombre(nuevoYduplicarCompletarCargo);
                        System.err.println("tipo lista" + tipoLista);
                        System.err.println("Secuencia Parentesco " + listConceptosSoportes.get(index).getOperando().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarConceptosSoportes.get(index).getOperando().setNombre(nuevoYduplicarCompletarCargo);
                    }

                }

            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
        }
    }

    /*public void verificarBorrado() {
     System.out.println("Estoy en verificarBorrado");
     BigInteger contarBienProgramacionesDepartamento;
     BigInteger contarCapModulosDepartamento;
     BigInteger contarOperandosDepartamento;
     BigInteger contarSoAccidentesMedicosDepartamento;

     try {
     System.err.println("Control Secuencia de ControlConceptosSoportes ");
     if (tipoLista == 0) {
     contarBienProgramacionesDepartamento = administrarConceptosSoportes.contarBienProgramacionesDepartamento(listConceptosSoportes.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarConceptosSoportes.contarCapModulosDepartamento(listConceptosSoportes.get(index).getSecuencia());
     contarOperandosDepartamento = administrarConceptosSoportes.contarOperandosDepartamento(listConceptosSoportes.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarConceptosSoportes.contarSoAccidentesMedicosDepartamento(listConceptosSoportes.get(index).getSecuencia());
     } else {
     contarBienProgramacionesDepartamento = administrarConceptosSoportes.contarBienProgramacionesDepartamento(filtrarConceptosSoportes.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarConceptosSoportes.contarCapModulosDepartamento(filtrarConceptosSoportes.get(index).getSecuencia());
     contarOperandosDepartamento = administrarConceptosSoportes.contarOperandosDepartamento(filtrarConceptosSoportes.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarConceptosSoportes.contarSoAccidentesMedicosDepartamento(filtrarConceptosSoportes.get(index).getSecuencia());
     }
     if (contarBienProgramacionesDepartamento.equals(new BigInteger("0"))
     && contarCapModulosDepartamento.equals(new BigInteger("0"))
     && contarOperandosDepartamento.equals(new BigInteger("0"))
     && contarSoAccidentesMedicosDepartamento.equals(new BigInteger("0"))) {
     System.out.println("Borrado==0");
     borrandoConceptosSoportes();
     } else {
     System.out.println("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     RequestContext.getCurrentInstance().update("form:validacionBorrar");
     PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
     index = -1;
     contarBienProgramacionesDepartamento = new BigInteger("-1");
     contarCapModulosDepartamento = new BigInteger("-1");
     contarOperandosDepartamento = new BigInteger("-1");
     contarSoAccidentesMedicosDepartamento = new BigInteger("-1");

     }
     } catch (Exception e) {
     System.err.println("ERROR ControlConceptosSoportes verificarBorrado ERROR " + e);
     }
     }
     */
    public void revisarDialogoGuardar() {

        if (!borrarConceptosSoportes.isEmpty() || !crearConceptosSoportes.isEmpty() || !modificarConceptosSoportes.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarConceptosSoportes() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarConceptosSoportes");
            if (!borrarConceptosSoportes.isEmpty()) {
                administrarConceptosSoportes.borrarConceptosSoportes(borrarConceptosSoportes);
                //mostrarBorrados
                registrosBorrados = borrarConceptosSoportes.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarConceptosSoportes.clear();
            }
            if (!modificarConceptosSoportes.isEmpty()) {
                administrarConceptosSoportes.modificarConceptosSoportes(modificarConceptosSoportes);
                modificarConceptosSoportes.clear();
            }
            if (!crearConceptosSoportes.isEmpty()) {
                administrarConceptosSoportes.crearConceptosSoportes(crearConceptosSoportes);
                crearConceptosSoportes.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listConceptosSoportes = null;
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void guardarConceptosSoportesCambios() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarConceptosSoportes");
            if (!borrarConceptosSoportes.isEmpty()) {
                administrarConceptosSoportes.borrarConceptosSoportes(borrarConceptosSoportes);
                //mostrarBorrados
                registrosBorrados = borrarConceptosSoportes.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarConceptosSoportes.clear();
            }
            if (!modificarConceptosSoportes.isEmpty()) {
                administrarConceptosSoportes.modificarConceptosSoportes(modificarConceptosSoportes);
                modificarConceptosSoportes.clear();
            }
            if (!crearConceptosSoportes.isEmpty()) {
                administrarConceptosSoportes.crearConceptosSoportes(crearConceptosSoportes);
                crearConceptosSoportes.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listConceptosSoportes = null;
            k = 0;
            guardado = true;
            //seleccionConceptoSoporte();
            RequestContext.getCurrentInstance().update("formularioDialogos:lovCentrosCostos");
            PrimefacesContextUI.ejecutar("PF('buscarCentrosCostosDialogo').show()");
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarConceptosSoportes = listConceptosSoportes.get(index);
            }
            if (tipoLista == 1) {
                editarConceptosSoportes = filtrarConceptosSoportes.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
                PrimefacesContextUI.ejecutar("PF('editPais').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editConceptos");
                PrimefacesContextUI.ejecutar("PF('editConceptos').show()");
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

    public void agregarNuevoConceptosSoportes() {
        System.out.println("agregarNuevoConceptosSoportes");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoConceptosSoportes.getConcepto().getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Debe Tener un Concepto \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoConceptosSoportes.getConcepto().getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Debe Tener un Concepto \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;//3

        }

    
        if (nuevoConceptosSoportes.getOperando().getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Debe Tener una Operando \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoConceptosSoportes.getOperando().getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Debe Tener una Operando \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;//4

        }

        System.out.println("contador " + contador);

        BigInteger contarConceptosOperandos = administrarConceptosSoportes.contarConceptosOperandos(nuevoConceptosSoportes.getConcepto().getSecuencia(), nuevoConceptosSoportes.getOperando().getSecuencia());
        System.out.println("CONTROLCONCEPTOSSOPORTES AGREGARNUEVOCONCEPTOSOPORTE contarConceptosPerandos : " + contarConceptosOperandos);
        if (contador == 2 && contarConceptosOperandos.equals(new BigInteger("0"))) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:personafir");
                personafir.setFilterStyle("display: none; visibility: hidden;");
                cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:cargo");
                cargo.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarConceptosSoportes = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoConceptosSoportes.setSecuencia(l);
            nuevoConceptosSoportes.setTipo("UNIDAD");
            crearConceptosSoportes.add(nuevoConceptosSoportes);

            listConceptosSoportes.add(nuevoConceptosSoportes);
            nuevoConceptosSoportes = new ConceptosSoportes();
            nuevoConceptosSoportes.setOperando(new Operandos());
            nuevoConceptosSoportes.setConcepto(new Conceptos());
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            infoRegistro = "Cantidad de registros: " + listConceptosSoportes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroConceptosSoportes').hide()");
            index = -1;
            secRegistro = null;
            cambioConceptosSoportes = true;

        } else {
            if (contarConceptosOperandos.intValue() > 0) {
                mensajeValidacion = "El OPERANDO y el CONCEPTO elegidos ya fueron insertados";
            }
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoConceptosSoportes() {
        System.out.println("limpiarNuevoConceptosSoportes");
        nuevoConceptosSoportes = new ConceptosSoportes();
        nuevoConceptosSoportes.setConcepto(new Conceptos());
        nuevoConceptosSoportes.setOperando(new Operandos());
        banderaConceptoEscogido = true;
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
        RequestContext.getCurrentInstance().update("formularioDialogos:btnnuevoCargo");
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoempresa");

        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void cargarNuevoConceptosSoportes() {
        System.out.println("cargarNuevoConceptosSoportes");

        duplicarConceptosSoportes = new ConceptosSoportes();
        duplicarConceptosSoportes.setConcepto(new Conceptos());
        duplicarConceptosSoportes.setOperando(new Operandos());
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.ejecutar("PF('nuevoRegistroConceptosSoportes').show()");

    }

    public void duplicandoConceptosSoportes() {
        System.out.println("duplicandoConceptosSoportes");
        if (index >= 0) {
            duplicarConceptosSoportes = new ConceptosSoportes();
            duplicarConceptosSoportes.setConcepto(new Conceptos());
            duplicarConceptosSoportes.setOperando(new Operandos());
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarConceptosSoportes.setSecuencia(l);
                duplicarConceptosSoportes.setConcepto(listConceptosSoportes.get(index).getConcepto());
                duplicarConceptosSoportes.setOperando(listConceptosSoportes.get(index).getOperando());
                duplicarConceptosSoportes.setTipo(listConceptosSoportes.get(index).getTipo());
            }
            if (tipoLista == 1) {
                duplicarConceptosSoportes.setSecuencia(l);
                duplicarConceptosSoportes.setConcepto(filtrarConceptosSoportes.get(index).getConcepto());
                duplicarConceptosSoportes.setOperando(filtrarConceptosSoportes.get(index).getOperando());
                duplicarConceptosSoportes.setTipo(filtrarConceptosSoportes.get(index).getTipo());

            }
            banderaConceptoEscogido = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroConceptosSoportes').show()");
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

        if (duplicarConceptosSoportes.getConcepto().getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Debe Tener un Concepto \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarConceptosSoportes.getConcepto().getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Debe Tener un Concepto \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;//3

        }

        if (duplicarConceptosSoportes.getOperando().getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Debe Tener una Operando \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarConceptosSoportes.getOperando().getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Debe Tener una Operando \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;//4

        }

        BigInteger contarConceptosOperandos = administrarConceptosSoportes.contarConceptosOperandos(duplicarConceptosSoportes.getConcepto().getSecuencia(), duplicarConceptosSoportes.getOperando().getSecuencia());
        if (contador == 2 && contarConceptosOperandos.equals(new BigInteger("0"))) {

            if (crearConceptosSoportes.contains(duplicarConceptosSoportes)) {
                System.out.println("Ya lo contengo.");
            }
            listConceptosSoportes.add(duplicarConceptosSoportes);
            crearConceptosSoportes.add(duplicarConceptosSoportes);
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            index = -1;
            System.out.println("--------------DUPLICAR------------------------");
            System.out.println("PERSONA : " + duplicarConceptosSoportes.getConcepto().getDescripcion());
            System.out.println("CARGO : " + duplicarConceptosSoportes.getOperando().getNombre());
            System.out.println("--------------DUPLICAR------------------------");

            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listConceptosSoportes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:personafir");
                personafir.setFilterStyle("display: none; visibility: hidden;");
                cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:cargo");
                cargo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
                bandera = 0;
                filtrarConceptosSoportes = null;
                tipoLista = 0;
            }
            duplicarConceptosSoportes = new ConceptosSoportes();
            duplicarConceptosSoportes.setOperando(new Operandos());
            duplicarConceptosSoportes.setConcepto(new Conceptos());

            RequestContext.getCurrentInstance().execute("duplicarRegistroConceptosSoportes').hide()");
            index = -1;
        } else {
            contador = 0;
            if (contarConceptosOperandos.intValue() > 0) {
                mensajeValidacion = "El OPERANDO y el CONCEPTO elegidos ya fueron insertados";
            }
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarConceptosSoportes() {
        duplicarConceptosSoportes = new ConceptosSoportes();
        duplicarConceptosSoportes.setConcepto(new Conceptos());
        duplicarConceptosSoportes.setOperando(new Operandos());
        banderaConceptoEscogido = true;
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
        RequestContext.getCurrentInstance().update("formularioDialogos:btnduplicarCargo");
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosSoportesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "CONCEPTOSSOPORTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosSoportesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "CONCEPTOSSOPORTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listConceptosSoportes.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "CONCEPTOSSOPORTES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("CONCEPTOSSOPORTES")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }
    private boolean cambioConceptosSoportes = false;

    public void llamadoDialogoBuscarConceptos() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                // banderaSeleccionCentrosCostosPorEmpresa = true;
                PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");

            } else {
                listConceptosSoportesBoton = null;
                getListConceptosSoportesBoton();
                index = -1;
                RequestContext.getCurrentInstance().update("formularioDialogos:lovCentrosCostos");
                PrimefacesContextUI.ejecutar("PF('buscarCentrosCostosDialogo').show()");

            }
        } catch (Exception e) {
            System.err.println("ERROR LLAMADO DIALOGO BUSCAR CENTROS COSTOS " + e);
        }

    }

    private boolean mostrarTodos;
    private boolean buscarConceptoSoporte;

    public void seleccionConceptoSoporte() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();

            if (guardado == true) {
                listConceptosSoportes.clear();
                System.err.println("seleccionCentrosCostosPorEmpresa " + conceptoSoporteSeleccionado.getConcepto().getDescripcion());
                listConceptosSoportes.add(conceptoSoporteSeleccionado);
                System.err.println("listCentrosCostosPorEmpresa tamaño " + listConceptosSoportes.size());
                System.err.println("listCentrosCostosPorEmpresa nombre " + listConceptosSoportes.get(0).getConcepto().getDescripcion());
                conceptoSoporteSeleccionado = null;
                filterConceptosSoportesBoton = null;
                aceptar = true;
                RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
                PrimefacesContextUI.ejecutar("PF('buscarCentrosCostosDialogo').hide()");
                context.reset("formularioDialogos:lovCentrosCostos:globalFilter");
                mostrarTodos = false;
                buscarConceptoSoporte = true;
                RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
                RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
                if (listConceptosSoportes == null || listConceptosSoportes.isEmpty()) {
                    infoRegistro = "Cantidad de registros: 0 ";
                } else {
                    infoRegistro = "Cantidad de registros: " + listConceptosSoportes.size();
                }
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
            } else {
                RequestContext.getCurrentInstance().update("form:confirmarGuardarConceptos");
                PrimefacesContextUI.ejecutar("PF('confirmarGuardarConceptos').show()");
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLCONCEPTOSSOPORTES.seleccionaVigencia ERROR====" + e.getMessage());
        }
    }

    public void cancelarSeleccionConceptoSoporte() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            conceptoSoporteSeleccionado = null;
            filterConceptosSoportesBoton = null;
            aceptar = true;
            index = -1;
            tipoActualizacion = -1;
            RequestContext.getCurrentInstance().update("formularioDialogos:aceptarNCC");

        } catch (Exception e) {
            System.out.println("ERROR CONTROLBETACENTROSCOSTOS.cancelarSeleccionVigencia ERROR====" + e.getMessage());
        }
    }
    
    public void recibirPagina(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }
    
    public String volverPaginaAnterior() {
        return paginaAnterior;
    }
    
    public List<ConceptosSoportes> getListConceptosSoportesBoton() {
        if (listConceptosSoportesBoton == null) {
            listConceptosSoportesBoton = administrarConceptosSoportes.consultarConceptosSoportes();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listConceptosSoportesBoton == null || listConceptosSoportesBoton.isEmpty()) {
            infoRecursoConceptoLista = "Cantidad de registros: 0 ";
        } else {
            infoRecursoConceptoLista = "Cantidad de registros: " + listConceptosSoportesBoton.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRecursoConceptoLista");
        return listConceptosSoportesBoton;
    }

    public void setListConceptosSoportesBoton(List<ConceptosSoportes> listConceptosSoportesBoton) {
        this.listConceptosSoportesBoton = listConceptosSoportesBoton;
    }

    public List<ConceptosSoportes> getFilterConceptosSoportesBoton() {
        return filterConceptosSoportesBoton;
    }

    public void setFilterConceptosSoportesBoton(List<ConceptosSoportes> filterConceptosSoportesBoton) {
        this.filterConceptosSoportesBoton = filterConceptosSoportesBoton;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public ConceptosSoportes getConceptoSoporteSeleccionado() {
        return conceptoSoporteSeleccionado;
    }

    public void setConceptoSoporteSeleccionado(ConceptosSoportes conceptoSoporteSeleccionado) {
        this.conceptoSoporteSeleccionado = conceptoSoporteSeleccionado;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<ConceptosSoportes> getListConceptosSoportes() {
        System.out.println("CONTROLCONCEPTOSSOPORTES getListConceptosSoportes ");
        if (listConceptosSoportes == null) {
            listConceptosSoportes = administrarConceptosSoportes.consultarConceptosSoportes();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listConceptosSoportes == null || listConceptosSoportes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listConceptosSoportes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        System.out.println("CONTROLCONCEPTOSSOPORTES getListConceptosSoportes ANTES RETURN : TAMAÑO " + listConceptosSoportes.size());
        return listConceptosSoportes;
    }

    public void setListConceptosSoportes(List<ConceptosSoportes> listConceptosSoportes) {
        this.listConceptosSoportes = listConceptosSoportes;
    }

    public List<ConceptosSoportes> getFiltrarConceptosSoportes() {
        return filtrarConceptosSoportes;
    }

    public void setFiltrarConceptosSoportes(List<ConceptosSoportes> filtrarConceptosSoportes) {
        this.filtrarConceptosSoportes = filtrarConceptosSoportes;
    }

    public ConceptosSoportes getNuevoConceptosSoportes() {
        return nuevoConceptosSoportes;
    }

    public void setNuevoConceptosSoportes(ConceptosSoportes nuevoConceptosSoportes) {
        this.nuevoConceptosSoportes = nuevoConceptosSoportes;
    }

    public ConceptosSoportes getDuplicarConceptosSoportes() {
        return duplicarConceptosSoportes;
    }

    public void setDuplicarConceptosSoportes(ConceptosSoportes duplicarConceptosSoportes) {
        this.duplicarConceptosSoportes = duplicarConceptosSoportes;
    }

    public ConceptosSoportes getEditarConceptosSoportes() {
        return editarConceptosSoportes;
    }

    public void setEditarConceptosSoportes(ConceptosSoportes editarConceptosSoportes) {
        this.editarConceptosSoportes = editarConceptosSoportes;
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
    private String infoRegistroConceptos;

    public List<Conceptos> getListaConceptos() {
        if (listaConceptos == null) {
            listaConceptos = administrarConceptosSoportes.consultarLOVConceptos();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaConceptos == null || listaConceptos.isEmpty()) {
            infoRegistroConceptos = "Cantidad de registros: 0 ";
        } else {
            infoRegistroConceptos = "Cantidad de registros: " + listaConceptos.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroConceptos");
        return listaConceptos;
    }

    public void setListaConceptos(List<Conceptos> listaConceptos) {
        this.listaConceptos = listaConceptos;
    }

    public List<Conceptos> getFiltradoConceptos() {
        return filtradoConceptos;
    }

    public void setFiltradoConceptos(List<Conceptos> filtradoConceptos) {
        this.filtradoConceptos = filtradoConceptos;
    }

    public Conceptos getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(Conceptos conceptoSeleccionado) {
        this.conceptoSeleccionado = conceptoSeleccionado;
    }

    private String infoRegistroOperandos;

    public List<Operandos> getListaOperandos() {
        if (listaOperandos == null) {
            listaOperandos = administrarConceptosSoportes.consultarLOVOperandosPorConcepto(secConceptoSeleccionado);
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaOperandos == null || listaOperandos.isEmpty()) {
            infoRegistroOperandos = "Cantidad de registros: 0 ";
        } else {
            infoRegistroOperandos = "Cantidad de registros: " + listaOperandos.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroOperandos");
        return listaOperandos;
    }

    public void setListaOperandos(List<Operandos> listaOperandos) {
        this.listaOperandos = listaOperandos;
    }

    public List<Operandos> getFiltradoOperandos() {
        return filtradoOperandos;
    }

    public void setFiltradoOperandos(List<Operandos> filtradoOperandos) {
        this.filtradoOperandos = filtradoOperandos;
    }

    public Operandos getOperandoSeleccionado() {
        return operandoSeleccionado;
    }

    public void setOperandoSeleccionado(Operandos operandoSeleccionado) {
        this.operandoSeleccionado = operandoSeleccionado;
    }

    public boolean isBanderaConceptoEscogido() {
        return banderaConceptoEscogido;
    }

    public void setBanderaConceptoEscogido(boolean banderaConceptoEscogido) {
        this.banderaConceptoEscogido = banderaConceptoEscogido;
    }

    public ConceptosSoportes getConceptoSoporteMostrado() {
        return conceptoSoporteMostrado;
    }

    public void setConceptoSoporteMostrado(ConceptosSoportes conceptoSoporteMostrado) {
        this.conceptoSoporteMostrado = conceptoSoporteMostrado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isBuscarConceptoSoporte() {
        return buscarConceptoSoporte;
    }

    public void setBuscarConceptoSoporte(boolean buscarConceptoSoporte) {
        this.buscarConceptoSoporte = buscarConceptoSoporte;
    }

    public boolean isMostrarTodos() {
        return mostrarTodos;
    }

    public void setMostrarTodos(boolean mostrarTodos) {
        this.mostrarTodos = mostrarTodos;
    }

    public String getInfoRegistroOperandos() {
        return infoRegistroOperandos;
    }

    public void setInfoRegistroOperandos(String infoRegistroOperandos) {
        this.infoRegistroOperandos = infoRegistroOperandos;
    }

    public String getInfoRegistroConceptos() {
        return infoRegistroConceptos;
    }

    public void setInfoRegistroConceptos(String infoRegistroConceptos) {
        this.infoRegistroConceptos = infoRegistroConceptos;
    }

    public String getInfoRecursoConceptoLista() {
        return infoRecursoConceptoLista;
    }

    public void setInfoRecursoConceptoLista(String infoRecursoConceptoLista) {
        this.infoRecursoConceptoLista = infoRecursoConceptoLista;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }
    

}
