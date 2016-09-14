/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Cargos;
import Entidades.FirmasReportes;
import Entidades.Empresas;
import Entidades.Personas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFirmasReportesInterface;
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
public class ControlFirmasReportes implements Serializable {

    @EJB
    AdministrarFirmasReportesInterface administrarFirmasReportes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<FirmasReportes> listFirmasReportes;
    private List<FirmasReportes> filtrarFirmasReportes;
    private List<FirmasReportes> crearFirmasReportes;
    private List<FirmasReportes> modificarFirmasReportes;
    private List<FirmasReportes> borrarFirmasReportes;
    private FirmasReportes nuevoFirmasReportes;
    private FirmasReportes duplicarFirmasReportes;
    private FirmasReportes editarFirmasReportes;
    private FirmasReportes firmaReporteSeleccionada;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion, pais, subTituloFirma, personafir, cargo;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private Integer backupCodigo;
    private String backupDescripcion;
    private String backupPais;
    private String backupSubtituloFirma;
    //-------------------------------
    private String backupEmpresas;
    private List<Empresas> listaEmpresas;
    private List<Empresas> filtradoEmpresas;
    private Empresas empresaSeleccionado;
    private String nuevoYduplicarCompletarEmpresa;
    //---------------------------------
    private String backupPersona;
    private List<Personas> listaPersonas;
    private List<Personas> filtradoPersonas;
    private Personas personaSeleccionado;
    private String nuevoYduplicarCompletarPersona;
    //--------------------------------------
    private String backupCargo;
    private List<Cargos> listaCargos;
    private List<Cargos> filtradoCargos;
    private Cargos cargoSeleccionado;
    private String nuevoYduplicarCompletarCargo;

    private String infoRegistro;
    private String infoLOVCargo;
    private String infoLOVPersona;
    private String infoLOVEmpresa;

    public ControlFirmasReportes() {
        listFirmasReportes = null;
        crearFirmasReportes = new ArrayList<FirmasReportes>();
        modificarFirmasReportes = new ArrayList<FirmasReportes>();
        borrarFirmasReportes = new ArrayList<FirmasReportes>();
        permitirIndex = true;
        editarFirmasReportes = new FirmasReportes();
        nuevoFirmasReportes = new FirmasReportes();
        nuevoFirmasReportes.setEmpresa(new Empresas());
        nuevoFirmasReportes.setPersonaFirma(new Personas());
        nuevoFirmasReportes.setCargo(new Cargos());
        duplicarFirmasReportes = new FirmasReportes();
        duplicarFirmasReportes.setEmpresa(new Empresas());
        duplicarFirmasReportes.setPersonaFirma(new Personas());
        duplicarFirmasReportes.setCargo(new Cargos());
        listaEmpresas = null;
        filtradoEmpresas = null;
        listaPersonas = null;
        filtradoPersonas = null;
        listaCargos = null;
        filtradoCargos = null;
        guardado = true;
        tamano = 270;
        aceptar = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarFirmasReportes.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlFirmasReportes.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarFirmasReportes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlFirmasReportes eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listFirmasReportes.get(index).getSecuencia();
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backupCodigo = listFirmasReportes.get(index).getCodigo();
                }
                if (cualCelda == 1) {
                    backupDescripcion = listFirmasReportes.get(index).getDescripcion();
                }
                if (cualCelda == 2) {
                    backupEmpresas = listFirmasReportes.get(index).getEmpresa().getNombre();
                    System.out.println("EMPRESA : " + backupEmpresas);
                }
                if (cualCelda == 3) {
                    backupSubtituloFirma = listFirmasReportes.get(index).getSubtitulofirma();
                }
                if (cualCelda == 4) {
                    backupPersona = listFirmasReportes.get(index).getPersonaFirma().getNombre();
                    System.out.println("PERSONA : " + backupPersona);

                }
                if (cualCelda == 5) {
                    backupCargo = listFirmasReportes.get(index).getCargo().getNombre();
                    System.out.println("CARGO : " + backupCargo);

                }
            } else if (tipoLista == 1) {
                if (cualCelda == 0) {
                    backupCodigo = filtrarFirmasReportes.get(index).getCodigo();
                }
                if (cualCelda == 1) {
                    backupDescripcion = filtrarFirmasReportes.get(index).getDescripcion();
                }
                if (cualCelda == 2) {
                    backupEmpresas = filtrarFirmasReportes.get(index).getEmpresa().getNombre();
                    System.out.println("EMPRESA : " + backupEmpresas);
                }
                if (cualCelda == 3) {
                    backupSubtituloFirma = filtrarFirmasReportes.get(index).getSubtitulofirma();
                }
                if (cualCelda == 4) {
                    backupPersona = filtrarFirmasReportes.get(index).getPersonaFirma().getNombre();
                    System.out.println("PERSONA : " + backupPersona);
                }
                if (cualCelda == 5) {
                    backupCargo = listFirmasReportes.get(index).getCargo().getNombre();
                    System.out.println("CARGO : " + backupCargo);

                }
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("\n ENTRE A ControlFirmasReportes.asignarIndex \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dig == 2) {
                RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
                PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                dig = -1;
            }
            if (dig == 4) {
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                dig = -1;
            }
            if (dig == 5) {
                RequestContext.getCurrentInstance().update("form:cargosDialogo");
                PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                dig = -1;
            }
        } catch (Exception e) {
            System.out.println("ERROR ControlFirmasReportes.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        if (index >= 0) {
            if (cualCelda == 2) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
                PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
            }
            if (cualCelda == 4) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }
            if (cualCelda == 5) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:cargosDialogo");
                PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
            }
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
            subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            bandera = 0;
            filtrarFirmasReportes = null;
            tipoLista = 0;
        }

        borrarFirmasReportes.clear();
        crearFirmasReportes.clear();
        modificarFirmasReportes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listFirmasReportes = null;
        guardado = true;
        permitirIndex = true;
        getListFirmasReportes();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listFirmasReportes == null || listFirmasReportes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listFirmasReportes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
            subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            bandera = 0;
            filtrarFirmasReportes = null;
            tipoLista = 0;
        }

        borrarFirmasReportes.clear();
        crearFirmasReportes.clear();
        modificarFirmasReportes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listFirmasReportes = null;
        guardado = true;
        permitirIndex = true;
        getListFirmasReportes();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listFirmasReportes == null || listFirmasReportes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listFirmasReportes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
            pais.setFilterStyle("width: 85% !important");
            subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
            subTituloFirma.setFilterStyle("width: 85% !important");
            personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
            personafir.setFilterStyle("width: 85% !important");
            cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
            cargo.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
            pais.setFilterStyle("display: none; visibility: hidden;");
            subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
            subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            bandera = 0;
            filtrarFirmasReportes = null;
            tipoLista = 0;
        }
    }

    public void actualizarPais() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("pais seleccionado : " + empresaSeleccionado.getNombre());
        System.out.println("tipo Actualizacion : " + tipoActualizacion);
        System.out.println("tipo Lista : " + tipoLista);

        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listFirmasReportes.get(index).setEmpresa(empresaSeleccionado);

                if (!crearFirmasReportes.contains(listFirmasReportes.get(index))) {
                    if (modificarFirmasReportes.isEmpty()) {
                        modificarFirmasReportes.add(listFirmasReportes.get(index));
                    } else if (!modificarFirmasReportes.contains(listFirmasReportes.get(index))) {
                        modificarFirmasReportes.add(listFirmasReportes.get(index));
                    }
                }
            } else {
                filtrarFirmasReportes.get(index).setEmpresa(empresaSeleccionado);

                if (!crearFirmasReportes.contains(filtrarFirmasReportes.get(index))) {
                    if (modificarFirmasReportes.isEmpty()) {
                        modificarFirmasReportes.add(filtrarFirmasReportes.get(index));
                    } else if (!modificarFirmasReportes.contains(filtrarFirmasReportes.get(index))) {
                        modificarFirmasReportes.add(filtrarFirmasReportes.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            System.out.println("ACTUALIZAR PAIS PAIS SELECCIONADO : " + empresaSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            System.out.println("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + empresaSeleccionado.getNombre());
            nuevoFirmasReportes.setEmpresa(empresaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPais");
        } else if (tipoActualizacion == 2) {
            System.out.println("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + empresaSeleccionado.getNombre());
            duplicarFirmasReportes.setEmpresa(empresaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPais");
        }
        filtradoEmpresas = null;
        empresaSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("form:lovTiposFamiliares:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTiposFamiliares').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovTiposFamiliares");
        //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
    }

    public void actualizarPersonas() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("pais seleccionado : " + personaSeleccionado.getNombre());
        System.out.println("tipo Actualizacion : " + tipoActualizacion);
        System.out.println("tipo Lista : " + tipoLista);

        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listFirmasReportes.get(index).setPersonaFirma(personaSeleccionado);

                if (!crearFirmasReportes.contains(listFirmasReportes.get(index))) {
                    if (modificarFirmasReportes.isEmpty()) {
                        modificarFirmasReportes.add(listFirmasReportes.get(index));
                    } else if (!modificarFirmasReportes.contains(listFirmasReportes.get(index))) {
                        modificarFirmasReportes.add(listFirmasReportes.get(index));
                    }
                }
            } else {
                filtrarFirmasReportes.get(index).setPersonaFirma(personaSeleccionado);

                if (!crearFirmasReportes.contains(filtrarFirmasReportes.get(index))) {
                    if (modificarFirmasReportes.isEmpty()) {
                        modificarFirmasReportes.add(filtrarFirmasReportes.get(index));
                    } else if (!modificarFirmasReportes.contains(filtrarFirmasReportes.get(index))) {
                        modificarFirmasReportes.add(filtrarFirmasReportes.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            System.out.println("ACTUALIZAR PAIS PAIS SELECCIONADO : " + personaSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            System.out.println("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + personaSeleccionado.getNombre());
            nuevoFirmasReportes.setPersonaFirma(personaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
        } else if (tipoActualizacion == 2) {
            System.out.println("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + personaSeleccionado.getNombre());
            duplicarFirmasReportes.setPersonaFirma(personaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
        }
        filtradoPersonas = null;
        personaSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("form:lovPersonas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovPersonas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovPersonas");
        //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
    }

    public void actualizarCargos() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("pais seleccionado : " + cargoSeleccionado.getNombre());
        System.out.println("tipo Actualizacion : " + tipoActualizacion);
        System.out.println("tipo Lista : " + tipoLista);

        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listFirmasReportes.get(index).setCargo(cargoSeleccionado);

                if (!crearFirmasReportes.contains(listFirmasReportes.get(index))) {
                    if (modificarFirmasReportes.isEmpty()) {
                        modificarFirmasReportes.add(listFirmasReportes.get(index));
                    } else if (!modificarFirmasReportes.contains(listFirmasReportes.get(index))) {
                        modificarFirmasReportes.add(listFirmasReportes.get(index));
                    }
                }
            } else {
                filtrarFirmasReportes.get(index).setCargo(cargoSeleccionado);

                if (!crearFirmasReportes.contains(filtrarFirmasReportes.get(index))) {
                    if (modificarFirmasReportes.isEmpty()) {
                        modificarFirmasReportes.add(filtrarFirmasReportes.get(index));
                    } else if (!modificarFirmasReportes.contains(filtrarFirmasReportes.get(index))) {
                        modificarFirmasReportes.add(filtrarFirmasReportes.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            System.out.println("ACTUALIZAR PAIS CARGO SELECCIONADO : " + cargoSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            System.out.println("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + cargoSeleccionado.getNombre());
            nuevoFirmasReportes.setCargo(cargoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
        } else if (tipoActualizacion == 2) {
            System.out.println("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + cargoSeleccionado.getNombre());
            duplicarFirmasReportes.setCargo(cargoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
        }
        filtradoPersonas = null;
        personaSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("form:lovCargos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCargos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('cargosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovCargos");
        //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
    }

    public void cancelarCambioPais() {
        filtradoEmpresas = null;
        empresaSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTiposFamiliares:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTiposFamiliares').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').hide()");
    }

    public void cancelarCambioPersonas() {
        filtradoPersonas = null;
        personaSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovPersonas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovPersonas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').hide()");
    }

    public void cancelarCambioCargos() {
        filtradoCargos = null;
        cargoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCargos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCargos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('cargosDialogo').hide()");
    }

    public void modificarFirmasReportes(int indice, String confirmarCambio, String valorConfirmar) {
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
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearFirmasReportes.contains(listFirmasReportes.get(indice))) {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);
                    System.out.println("backupSubtituloFirma : " + backupSubtituloFirma);
                    System.out.println("backupPais : " + backupPais);

                    if (listFirmasReportes.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listFirmasReportes.get(indice).setCodigo(backupCodigo);

                    } else {
                        for (int j = 0; j < listFirmasReportes.size(); j++) {
                            if (j != indice) {
                                if (listFirmasReportes.get(indice).getCodigo() == listFirmasReportes.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listFirmasReportes.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listFirmasReportes.get(indice).getDescripcion().isEmpty() || listFirmasReportes.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listFirmasReportes.get(indice).setDescripcion(backupDescripcion);
                    } else if (listFirmasReportes.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listFirmasReportes.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }
                    if (listFirmasReportes.get(indice).getSubtitulofirma().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita2 = false;
                        listFirmasReportes.get(indice).setSubtitulofirma(backupSubtituloFirma);
                    } else if (listFirmasReportes.get(indice).getSubtitulofirma().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita2 = false;
                        listFirmasReportes.get(indice).setSubtitulofirma(backupSubtituloFirma);

                    } else {
                        banderita2 = true;
                    }

                    if (banderita == true && banderita1 == true && banderita2 == true) {
                        if (modificarFirmasReportes.isEmpty()) {
                            modificarFirmasReportes.add(listFirmasReportes.get(indice));
                        } else if (!modificarFirmasReportes.contains(listFirmasReportes.get(indice))) {
                            modificarFirmasReportes.add(listFirmasReportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                    RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listFirmasReportes.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listFirmasReportes.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listFirmasReportes.size(); j++) {
                            if (j != indice) {
                                if (listFirmasReportes.get(indice).getCodigo() == listFirmasReportes.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listFirmasReportes.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listFirmasReportes.get(indice).getDescripcion().isEmpty() || listFirmasReportes.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listFirmasReportes.get(indice).setDescripcion(backupDescripcion);
                    } else if (listFirmasReportes.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listFirmasReportes.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }
                    if (listFirmasReportes.get(indice).getSubtitulofirma().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita2 = false;
                        listFirmasReportes.get(indice).setSubtitulofirma(backupSubtituloFirma);
                    } else if (listFirmasReportes.get(indice).getSubtitulofirma().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita2 = false;
                        listFirmasReportes.get(indice).setSubtitulofirma(backupSubtituloFirma);

                    } else {
                        banderita2 = true;
                    }
                    if (banderita == true && banderita1 == true && banderita2 == true) {
                        if (guardado == true) {
                            guardado = false;
                        }
                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                    RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            } else {

                if (!crearFirmasReportes.contains(filtrarFirmasReportes.get(indice))) {
                    if (filtrarFirmasReportes.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarFirmasReportes.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarFirmasReportes.size(); j++) {
                            if (j != indice) {
                                if (filtrarFirmasReportes.get(indice).getCodigo() == listFirmasReportes.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listFirmasReportes.size(); j++) {
                            if (j != indice) {
                                if (filtrarFirmasReportes.get(indice).getCodigo() == listFirmasReportes.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarFirmasReportes.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarFirmasReportes.get(indice).getDescripcion().isEmpty() || filtrarFirmasReportes.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarFirmasReportes.get(indice).setDescripcion(backupDescripcion);
                    }
                    if (filtrarFirmasReportes.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarFirmasReportes.get(indice).setDescripcion(backupDescripcion);
                    } else {
                        banderita1 = true;
                    }
                    if (filtrarFirmasReportes.get(indice).getSubtitulofirma().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita2 = false;
                        filtrarFirmasReportes.get(indice).setSubtitulofirma(backupSubtituloFirma);
                    } else if (filtrarFirmasReportes.get(indice).getSubtitulofirma().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita2 = false;
                        filtrarFirmasReportes.get(indice).setSubtitulofirma(backupSubtituloFirma);

                    } else {
                        banderita2 = true;
                    }
                    if (banderita == true && banderita1 == true && banderita2 == true) {

                        if (modificarFirmasReportes.isEmpty()) {
                            modificarFirmasReportes.add(filtrarFirmasReportes.get(indice));
                        } else if (!modificarFirmasReportes.contains(filtrarFirmasReportes.get(indice))) {
                            modificarFirmasReportes.add(filtrarFirmasReportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarFirmasReportes.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarFirmasReportes.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarFirmasReportes.size(); j++) {
                            if (j != indice) {
                                if (filtrarFirmasReportes.get(indice).getCodigo() == listFirmasReportes.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listFirmasReportes.size(); j++) {
                            if (j != indice) {
                                if (filtrarFirmasReportes.get(indice).getCodigo() == listFirmasReportes.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarFirmasReportes.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarFirmasReportes.get(indice).getDescripcion().isEmpty() || filtrarFirmasReportes.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarFirmasReportes.get(indice).setDescripcion(backupDescripcion);
                    } else if (filtrarFirmasReportes.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarFirmasReportes.get(indice).setDescripcion(backupDescripcion);
                    } else {
                        banderita1 = true;
                    }
                    if (filtrarFirmasReportes.get(indice).getSubtitulofirma().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita2 = false;
                        filtrarFirmasReportes.get(indice).setSubtitulofirma(backupSubtituloFirma);
                    } else if (filtrarFirmasReportes.get(indice).getSubtitulofirma().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita2 = false;
                        filtrarFirmasReportes.get(indice).setSubtitulofirma(backupSubtituloFirma);

                    } else {
                        banderita2 = true;
                    }
                    if (banderita == true && banderita1 == true && banderita2 == true) {
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (confirmarCambio.equalsIgnoreCase("PAISES")) {
            System.out.println("MODIFICANDO NORMA LABORAL : " + listFirmasReportes.get(indice).getEmpresa().getNombre());
            if (!listFirmasReportes.get(indice).getEmpresa().getNombre().equals("")) {
                if (tipoLista == 0) {
                    listFirmasReportes.get(indice).getEmpresa().setNombre(backupEmpresas);
                } else {
                    listFirmasReportes.get(indice).getEmpresa().setNombre(backupEmpresas);
                }

                for (int i = 0; i < listaEmpresas.size(); i++) {
                    if (listaEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listFirmasReportes.get(indice).setEmpresa(listaEmpresas.get(indiceUnicoElemento));
                    } else {
                        filtrarFirmasReportes.get(indice).setEmpresa(listaEmpresas.get(indiceUnicoElemento));
                    }
                    listaEmpresas.clear();
                    listaEmpresas = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
                    PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupEmpresas != null) {
                    if (tipoLista == 0) {
                        listFirmasReportes.get(index).getEmpresa().setNombre(backupEmpresas);
                    } else {
                        filtrarFirmasReportes.get(index).getEmpresa().setNombre(backupEmpresas);
                    }
                }
                tipoActualizacion = 0;
                System.out.println("PAIS ANTES DE MOSTRAR DIALOGO : " + backupEmpresas);
                RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
                PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearFirmasReportes.contains(listFirmasReportes.get(indice))) {

                        if (modificarFirmasReportes.isEmpty()) {
                            modificarFirmasReportes.add(listFirmasReportes.get(indice));
                        } else if (!modificarFirmasReportes.contains(listFirmasReportes.get(indice))) {
                            modificarFirmasReportes.add(listFirmasReportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearFirmasReportes.contains(filtrarFirmasReportes.get(indice))) {

                        if (modificarFirmasReportes.isEmpty()) {
                            modificarFirmasReportes.add(filtrarFirmasReportes.get(indice));
                        } else if (!modificarFirmasReportes.contains(filtrarFirmasReportes.get(indice))) {
                            modificarFirmasReportes.add(filtrarFirmasReportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        } else if (confirmarCambio.equalsIgnoreCase("PERSONAS")) {
            System.out.println("MODIFICANDO NORMA LABORAL : " + listFirmasReportes.get(indice).getPersonaFirma().getNombre());
            if (!listFirmasReportes.get(indice).getPersonaFirma().getNombre().equals("")) {
                if (tipoLista == 0) {
                    listFirmasReportes.get(indice).getPersonaFirma().setNombre(backupPersona);
                } else {
                    listFirmasReportes.get(indice).getPersonaFirma().setNombre(backupPersona);
                }

                for (int i = 0; i < listaPersonas.size(); i++) {
                    if (listaPersonas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listFirmasReportes.get(indice).setPersonaFirma(listaPersonas.get(indiceUnicoElemento));
                    } else {
                        filtrarFirmasReportes.get(indice).setPersonaFirma(listaPersonas.get(indiceUnicoElemento));
                    }
                    listaEmpresas.clear();
                    listaEmpresas = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupPersona != null) {
                    if (tipoLista == 0) {
                        listFirmasReportes.get(index).getPersonaFirma().setNombre(backupPersona);
                    } else {
                        filtrarFirmasReportes.get(index).getPersonaFirma().setNombre(backupPersona);
                    }
                }
                tipoActualizacion = 0;
                System.out.println("PAIS ANTES DE MOSTRAR DIALOGO PERSONA : " + backupPersona);
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearFirmasReportes.contains(listFirmasReportes.get(indice))) {

                        if (modificarFirmasReportes.isEmpty()) {
                            modificarFirmasReportes.add(listFirmasReportes.get(indice));
                        } else if (!modificarFirmasReportes.contains(listFirmasReportes.get(indice))) {
                            modificarFirmasReportes.add(listFirmasReportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearFirmasReportes.contains(filtrarFirmasReportes.get(indice))) {

                        if (modificarFirmasReportes.isEmpty()) {
                            modificarFirmasReportes.add(filtrarFirmasReportes.get(indice));
                        } else if (!modificarFirmasReportes.contains(filtrarFirmasReportes.get(indice))) {
                            modificarFirmasReportes.add(filtrarFirmasReportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        } else if (confirmarCambio.equalsIgnoreCase("CARGOS")) {
            System.out.println("MODIFICANDO CARGO: " + listFirmasReportes.get(indice).getCargo().getNombre());
            if (!listFirmasReportes.get(indice).getPersonaFirma().getNombre().equals("")) {
                if (tipoLista == 0) {
                    listFirmasReportes.get(indice).getCargo().setNombre(backupCargo);
                } else {
                    listFirmasReportes.get(indice).getCargo().setNombre(backupCargo);
                }

                for (int i = 0; i < listaCargos.size(); i++) {
                    if (listaCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listFirmasReportes.get(indice).setCargo(listaCargos.get(indiceUnicoElemento));
                    } else {
                        filtrarFirmasReportes.get(indice).setCargo(listaCargos.get(indiceUnicoElemento));
                    }
                    listaEmpresas.clear();
                    listaEmpresas = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:cargosDialogo");
                    PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupCargo != null) {
                    if (tipoLista == 0) {
                        listFirmasReportes.get(index).getCargo().setNombre(backupCargo);
                    } else {
                        filtrarFirmasReportes.get(index).getCargo().setNombre(backupCargo);
                    }
                }
                tipoActualizacion = 0;
                System.out.println("PAIS ANTES DE MOSTRAR DIALOGO CARGOS : " + backupCargo);
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearFirmasReportes.contains(listFirmasReportes.get(indice))) {

                        if (modificarFirmasReportes.isEmpty()) {
                            modificarFirmasReportes.add(listFirmasReportes.get(indice));
                        } else if (!modificarFirmasReportes.contains(listFirmasReportes.get(indice))) {
                            modificarFirmasReportes.add(listFirmasReportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearFirmasReportes.contains(filtrarFirmasReportes.get(indice))) {

                        if (modificarFirmasReportes.isEmpty()) {
                            modificarFirmasReportes.add(filtrarFirmasReportes.get(indice));
                        } else if (!modificarFirmasReportes.contains(filtrarFirmasReportes.get(indice))) {
                            modificarFirmasReportes.add(filtrarFirmasReportes.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        }

    }

    public void borrandoFirmasReportes() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoFirmasReportes");
                if (!modificarFirmasReportes.isEmpty() && modificarFirmasReportes.contains(listFirmasReportes.get(index))) {
                    int modIndex = modificarFirmasReportes.indexOf(listFirmasReportes.get(index));
                    modificarFirmasReportes.remove(modIndex);
                    borrarFirmasReportes.add(listFirmasReportes.get(index));
                } else if (!crearFirmasReportes.isEmpty() && crearFirmasReportes.contains(listFirmasReportes.get(index))) {
                    int crearIndex = crearFirmasReportes.indexOf(listFirmasReportes.get(index));
                    crearFirmasReportes.remove(crearIndex);
                } else {
                    borrarFirmasReportes.add(listFirmasReportes.get(index));
                }
                listFirmasReportes.remove(index);
                infoRegistro = "Cantidad de registros: " + listFirmasReportes.size();
            }
            if (tipoLista == 1) {
                System.out.println("borrandoFirmasReportes ");
                if (!modificarFirmasReportes.isEmpty() && modificarFirmasReportes.contains(filtrarFirmasReportes.get(index))) {
                    int modIndex = modificarFirmasReportes.indexOf(filtrarFirmasReportes.get(index));
                    modificarFirmasReportes.remove(modIndex);
                    borrarFirmasReportes.add(filtrarFirmasReportes.get(index));
                } else if (!crearFirmasReportes.isEmpty() && crearFirmasReportes.contains(filtrarFirmasReportes.get(index))) {
                    int crearIndex = crearFirmasReportes.indexOf(filtrarFirmasReportes.get(index));
                    crearFirmasReportes.remove(crearIndex);
                } else {
                    borrarFirmasReportes.add(filtrarFirmasReportes.get(index));
                }
                int VCIndex = listFirmasReportes.indexOf(filtrarFirmasReportes.get(index));
                listFirmasReportes.remove(VCIndex);
                filtrarFirmasReportes.remove(index);
                infoRegistro = "Cantidad de registros: " + filtrarFirmasReportes.size();

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");

            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
        System.out.println("1...");
        if (valorCambio.equals("EMPRESAS")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarEmpresa = nuevoFirmasReportes.getEmpresa().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarEmpresa = duplicarFirmasReportes.getEmpresa().getNombre();
            }
            System.out.println("EMPRESA : " + nuevoYduplicarCompletarEmpresa);
        } else if (valorCambio.equals("PERSONA")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarPersona = nuevoFirmasReportes.getPersonaFirma().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarPersona = duplicarFirmasReportes.getPersonaFirma().getNombre();
            }

            System.out.println("PERSONA : " + nuevoYduplicarCompletarPersona);
        } else if (valorCambio.equals("CARGO")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarCargo = nuevoFirmasReportes.getCargo().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarCargo = duplicarFirmasReportes.getCargo().getNombre();
            }
            System.out.println("CARGO : " + nuevoYduplicarCompletarCargo);
        }

    }

    public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("EMPRESAS")) {
            System.out.println(" nueva Ciudad    Entro al if 'Centro costo'");
            System.out.println("NOMBRE CENTRO COSTO: " + nuevoFirmasReportes.getEmpresa().getNombre());

            if (!nuevoFirmasReportes.getEmpresa().getNombre().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCiudadCompletar: " + nuevoYduplicarCompletarEmpresa);
                nuevoFirmasReportes.getEmpresa().setNombre(nuevoYduplicarCompletarEmpresa);
                getListaEmpresas();
                for (int i = 0; i < listaEmpresas.size(); i++) {
                    if (listaEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    nuevoFirmasReportes.setEmpresa(listaEmpresas.get(indiceUnicoElemento));
                    listaEmpresas = null;
                    getListaEmpresas();
                    System.err.println("NORMA LABORAL GUARDADA " + nuevoFirmasReportes.getEmpresa().getNombre());
                } else {
                    RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
                    PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoFirmasReportes.getEmpresa().setNombre(nuevoYduplicarCompletarEmpresa);
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoFirmasReportes.setEmpresa(new Empresas());
                nuevoFirmasReportes.getEmpresa().setNombre(" ");
                System.out.println("NUEVA NORMA LABORAL" + nuevoFirmasReportes.getEmpresa().getNombre());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPais");
        } else if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
            System.out.println(" nueva Ciudad    Entro al if 'Centro costo'");
            System.out.println("NUEVO PERSONA :-------> " + nuevoFirmasReportes.getPersonaFirma().getNombre());

            if (!nuevoFirmasReportes.getPersonaFirma().getNombre().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarPersona);
                nuevoFirmasReportes.getPersonaFirma().setNombre(nuevoYduplicarCompletarPersona);
                getListaEmpresas();
                for (int i = 0; i < listaPersonas.size(); i++) {
                    if (listaPersonas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    nuevoFirmasReportes.setPersonaFirma(listaPersonas.get(indiceUnicoElemento));
                    listaEmpresas = null;
                    getListaEmpresas();
                    System.err.println("PERSONA GUARDADA :-----> " + nuevoFirmasReportes.getPersonaFirma().getNombre());
                } else {
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoFirmasReportes.getPersonaFirma().setNombre(nuevoYduplicarCompletarPersona);
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoFirmasReportes.setPersonaFirma(new Personas());
                nuevoFirmasReportes.getPersonaFirma().setNombre(" ");
                System.out.println("NUEVA NORMA LABORAL" + nuevoFirmasReportes.getPersonaFirma().getNombre());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            System.out.println(" nueva Ciudad    Entro al if 'Centro costo'");
            System.out.println("NUEVO PERSONA :-------> " + nuevoFirmasReportes.getCargo().getNombre());

            if (!nuevoFirmasReportes.getCargo().getNombre().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarCargo);
                nuevoFirmasReportes.getCargo().setNombre(nuevoYduplicarCompletarCargo);
                getListaEmpresas();
                for (int i = 0; i < listaCargos.size(); i++) {
                    if (listaCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    nuevoFirmasReportes.setCargo(listaCargos.get(indiceUnicoElemento));
                    listaEmpresas = null;
                    getListaEmpresas();
                    System.err.println("CARGO GUARDADA :-----> " + nuevoFirmasReportes.getCargo().getNombre());
                } else {
                    RequestContext.getCurrentInstance().update("form:cargosDialogo");
                    PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoFirmasReportes.getCargo().setNombre(nuevoYduplicarCompletarCargo);
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoFirmasReportes.setCargo(new Cargos());
                nuevoFirmasReportes.getCargo().setNombre(" ");
                System.out.println("NUEVO CARGO " + nuevoFirmasReportes.getCargo().getNombre());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
        }

    }

    public void asignarVariableEmpresas(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
        PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
    }

    public void asignarVariablePersonas(int tipoNuevo) {
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

    public void asignarVariableCargos(int tipoNuevo) {
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

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        System.out.println("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("EMPRESAS")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarEmpresa);

            if (!duplicarFirmasReportes.getEmpresa().getNombre().equals("")) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarEmpresa);
                duplicarFirmasReportes.getEmpresa().setNombre(nuevoYduplicarCompletarEmpresa);
                for (int i = 0; i < listaEmpresas.size(); i++) {
                    if (listaEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarFirmasReportes.setEmpresa(listaEmpresas.get(indiceUnicoElemento));
                    listaEmpresas = null;
                    getListaEmpresas();
                } else {
                    RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
                    PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    //duplicarFirmasReportes.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarFirmasReportes.setEmpresa(new Empresas());
                    duplicarFirmasReportes.getEmpresa().setNombre(" ");

                    System.out.println("DUPLICAR Valor NORMA LABORAL : " + duplicarFirmasReportes.getEmpresa().getNombre());
                    System.out.println("nuevoYduplicarCompletarPais : " + nuevoYduplicarCompletarEmpresa);
                    if (tipoLista == 0) {
                        listFirmasReportes.get(index).getEmpresa().setNombre(nuevoYduplicarCompletarEmpresa);
                        System.err.println("tipo lista" + tipoLista);
                        System.err.println("Secuencia Parentesco " + listFirmasReportes.get(index).getEmpresa().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarFirmasReportes.get(index).getEmpresa().setNombre(nuevoYduplicarCompletarEmpresa);
                    }

                }

            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPais");
        } else if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarPersona);

            if (!duplicarFirmasReportes.getPersonaFirma().getNombre().equals("")) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarPersona);
                duplicarFirmasReportes.getPersonaFirma().setNombre(nuevoYduplicarCompletarPersona);
                for (int i = 0; i < listaPersonas.size(); i++) {
                    if (listaPersonas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarFirmasReportes.setPersonaFirma(listaPersonas.get(indiceUnicoElemento));
                    listaPersonas = null;
                    getListaEmpresas();
                } else {
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    //duplicarFirmasReportes.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarFirmasReportes.setPersonaFirma(new Personas());
                    duplicarFirmasReportes.getPersonaFirma().setNombre(" ");

                    System.out.println("DUPLICAR PERSONA  : " + duplicarFirmasReportes.getPersonaFirma().getNombre());
                    System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarPersona);
                    if (tipoLista == 0) {
                        listFirmasReportes.get(index).getPersonaFirma().setNombre(nuevoYduplicarCompletarPersona);
                        System.err.println("tipo lista" + tipoLista);
                        System.err.println("Secuencia Parentesco " + listFirmasReportes.get(index).getPersonaFirma().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarFirmasReportes.get(index).getPersonaFirma().setNombre(nuevoYduplicarCompletarPersona);
                    }

                }

            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarCargo);

            if (!duplicarFirmasReportes.getCargo().getNombre().equals("")) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarCargo);
                duplicarFirmasReportes.getCargo().setNombre(nuevoYduplicarCompletarCargo);
                for (int i = 0; i < listaCargos.size(); i++) {
                    if (listaCargos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarFirmasReportes.setCargo(listaCargos.get(indiceUnicoElemento));
                    listaCargos = null;
                    getListaCargos();
                } else {
                    RequestContext.getCurrentInstance().update("form:cargosDialogo");
                    PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    //duplicarFirmasReportes.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarFirmasReportes.setCargo(new Cargos());
                    duplicarFirmasReportes.getCargo().setNombre(" ");

                    System.out.println("DUPLICAR CARGO  : " + duplicarFirmasReportes.getCargo().getNombre());
                    System.out.println("nuevoYduplicarCompletarCARGO : " + nuevoYduplicarCompletarCargo);
                    if (tipoLista == 0) {
                        listFirmasReportes.get(index).getCargo().setNombre(nuevoYduplicarCompletarCargo);
                        System.err.println("tipo lista" + tipoLista);
                        System.err.println("Secuencia Parentesco " + listFirmasReportes.get(index).getCargo().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarFirmasReportes.get(index).getCargo().setNombre(nuevoYduplicarCompletarCargo);
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
     BigInteger contarCiudadesDepartamento;
     BigInteger contarSoAccidentesMedicosDepartamento;

     try {
     System.err.println("Control Secuencia de ControlFirmasReportes ");
     if (tipoLista == 0) {
     contarBienProgramacionesDepartamento = administrarFirmasReportes.contarBienProgramacionesDepartamento(listFirmasReportes.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarFirmasReportes.contarCapModulosDepartamento(listFirmasReportes.get(index).getSecuencia());
     contarCiudadesDepartamento = administrarFirmasReportes.contarCiudadesDepartamento(listFirmasReportes.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarFirmasReportes.contarSoAccidentesMedicosDepartamento(listFirmasReportes.get(index).getSecuencia());
     } else {
     contarBienProgramacionesDepartamento = administrarFirmasReportes.contarBienProgramacionesDepartamento(filtrarFirmasReportes.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarFirmasReportes.contarCapModulosDepartamento(filtrarFirmasReportes.get(index).getSecuencia());
     contarCiudadesDepartamento = administrarFirmasReportes.contarCiudadesDepartamento(filtrarFirmasReportes.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarFirmasReportes.contarSoAccidentesMedicosDepartamento(filtrarFirmasReportes.get(index).getSecuencia());
     }
     if (contarBienProgramacionesDepartamento.equals(new BigInteger("0"))
     && contarCapModulosDepartamento.equals(new BigInteger("0"))
     && contarCiudadesDepartamento.equals(new BigInteger("0"))
     && contarSoAccidentesMedicosDepartamento.equals(new BigInteger("0"))) {
     System.out.println("Borrado==0");
     borrandoFirmasReportes();
     } else {
     System.out.println("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     RequestContext.getCurrentInstance().update("form:validacionBorrar");
     PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
     index = -1;
     contarBienProgramacionesDepartamento = new BigInteger("-1");
     contarCapModulosDepartamento = new BigInteger("-1");
     contarCiudadesDepartamento = new BigInteger("-1");
     contarSoAccidentesMedicosDepartamento = new BigInteger("-1");

     }
     } catch (Exception e) {
     System.err.println("ERROR ControlFirmasReportes verificarBorrado ERROR " + e);
     }
     }
     */
    public void revisarDialogoGuardar() {

        if (!borrarFirmasReportes.isEmpty() || !crearFirmasReportes.isEmpty() || !modificarFirmasReportes.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarFirmasReportes() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarFirmasReportes");
            if (!borrarFirmasReportes.isEmpty()) {
                administrarFirmasReportes.borrarFirmasReportes(borrarFirmasReportes);
                //mostrarBorrados
                registrosBorrados = borrarFirmasReportes.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarFirmasReportes.clear();
            }
            if (!modificarFirmasReportes.isEmpty()) {
                administrarFirmasReportes.modificarFirmasReportes(modificarFirmasReportes);
                modificarFirmasReportes.clear();
            }
            if (!crearFirmasReportes.isEmpty()) {
                administrarFirmasReportes.crearFirmasReportes(crearFirmasReportes);
                crearFirmasReportes.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listFirmasReportes = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarFirmasReportes = listFirmasReportes.get(index);
            }
            if (tipoLista == 1) {
                editarFirmasReportes = filtrarFirmasReportes.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                PrimefacesContextUI.ejecutar("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                PrimefacesContextUI.ejecutar("PF('editDescripcion').show()");
                cualCelda = -1;

            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
                PrimefacesContextUI.ejecutar("PF('editPais').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editSubtituloFirma");
                PrimefacesContextUI.ejecutar("PF('editSubtituloFirma').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPersonas");
                PrimefacesContextUI.ejecutar("PF('editPersonas').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCargos");
                PrimefacesContextUI.ejecutar("PF('editCargos').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoFirmasReportes() {
        System.out.println("agregarNuevoFirmasReportes");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoFirmasReportes.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoFirmasReportes.getCodigo());

            for (int x = 0; x < listFirmasReportes.size(); x++) {
                if (listFirmasReportes.get(x).getCodigo() == nuevoFirmasReportes.getCodigo()) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoFirmasReportes.getDescripcion() == null || nuevoFirmasReportes.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }
        if (nuevoFirmasReportes.getEmpresa().getNombre() == null || nuevoFirmasReportes.getEmpresa().getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Empresa \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }
        if (nuevoFirmasReportes.getPersonaFirma().getNombre() == null || nuevoFirmasReportes.getPersonaFirma().getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Persona \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (nuevoFirmasReportes.getCargo().getNombre() == null || nuevoFirmasReportes.getCargo().getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Cargo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }
        if (nuevoFirmasReportes.getSubtitulofirma() == null || nuevoFirmasReportes.getSubtitulofirma().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Subitulo Firma \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 6) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
                pais.setFilterStyle("display: none; visibility: hidden;");
                subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
                subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
                personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
                personafir.setFilterStyle("display: none; visibility: hidden;");
                cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
                cargo.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarFirmasReportes = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoFirmasReportes.setSecuencia(l);

            crearFirmasReportes.add(nuevoFirmasReportes);

            listFirmasReportes.add(nuevoFirmasReportes);
            nuevoFirmasReportes = new FirmasReportes();
            nuevoFirmasReportes.setEmpresa(new Empresas());
            nuevoFirmasReportes.setPersonaFirma(new Personas());
            nuevoFirmasReportes.setCargo(new Cargos());
            infoRegistro = "Cantidad de registros: " + listFirmasReportes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroFirmasReportes').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoFirmasReportes() {
        System.out.println("limpiarNuevoFirmasReportes");
        nuevoFirmasReportes = new FirmasReportes();
        nuevoFirmasReportes.setEmpresa(new Empresas());
        nuevoFirmasReportes.setPersonaFirma(new Personas());
        nuevoFirmasReportes.setCargo(new Cargos());
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoFirmasReportes() {
        System.out.println("duplicandoFirmasReportes");
        if (index >= 0) {
            duplicarFirmasReportes = new FirmasReportes();
            duplicarFirmasReportes.setEmpresa(new Empresas());
            duplicarFirmasReportes.setPersonaFirma(new Personas());
            duplicarFirmasReportes.setCargo(new Cargos());
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarFirmasReportes.setSecuencia(l);
                duplicarFirmasReportes.setCodigo(listFirmasReportes.get(index).getCodigo());
                duplicarFirmasReportes.setDescripcion(listFirmasReportes.get(index).getDescripcion());
                duplicarFirmasReportes.setEmpresa(listFirmasReportes.get(index).getEmpresa());
                duplicarFirmasReportes.setSubtitulofirma(listFirmasReportes.get(index).getSubtitulofirma());
                duplicarFirmasReportes.setPersonaFirma(listFirmasReportes.get(index).getPersonaFirma());
                duplicarFirmasReportes.setCargo(listFirmasReportes.get(index).getCargo());
            }
            if (tipoLista == 1) {
                duplicarFirmasReportes.setSecuencia(l);
                duplicarFirmasReportes.setCodigo(filtrarFirmasReportes.get(index).getCodigo());
                duplicarFirmasReportes.setDescripcion(filtrarFirmasReportes.get(index).getDescripcion());
                duplicarFirmasReportes.setEmpresa(filtrarFirmasReportes.get(index).getEmpresa());
                duplicarFirmasReportes.setSubtitulofirma(filtrarFirmasReportes.get(index).getSubtitulofirma());
                duplicarFirmasReportes.setPersonaFirma(filtrarFirmasReportes.get(index).getPersonaFirma());
                duplicarFirmasReportes.setCargo(filtrarFirmasReportes.get(index).getCargo());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroFirmasReportes').show()");
            index = -1;
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarFirmasReportes.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarFirmasReportes.getDescripcion());

        if (duplicarFirmasReportes.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listFirmasReportes.size(); x++) {
                if (listFirmasReportes.get(x).getCodigo() == duplicarFirmasReportes.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarFirmasReportes.getDescripcion() == null || duplicarFirmasReportes.getDescripcion().equals(" ")) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarFirmasReportes.getEmpresa().getNombre() == null || duplicarFirmasReportes.getEmpresa().getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Empresa \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarFirmasReportes.getPersonaFirma().getNombre() == null || duplicarFirmasReportes.getPersonaFirma().getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Persona \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarFirmasReportes.getCargo().getNombre() == null || duplicarFirmasReportes.getEmpresa().getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Cargo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarFirmasReportes.getSubtitulofirma() == null || duplicarFirmasReportes.getSubtitulofirma().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Subtitulo Firma \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 6) {

            System.out.println("Datos Duplicando: " + duplicarFirmasReportes.getSecuencia() + "  " + duplicarFirmasReportes.getCodigo());
            if (crearFirmasReportes.contains(duplicarFirmasReportes)) {
                System.out.println("Ya lo contengo.");
            }
            listFirmasReportes.add(duplicarFirmasReportes);
            crearFirmasReportes.add(duplicarFirmasReportes);
            infoRegistro = "Cantidad de registros: " + listFirmasReportes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
            index = -1;
            System.out.println("--------------DUPLICAR------------------------");
            System.out.println("CODIGO : " + duplicarFirmasReportes.getCodigo());
            System.out.println("NOMBRE: " + duplicarFirmasReportes.getDescripcion());
            System.out.println("EMPRESA: " + duplicarFirmasReportes.getEmpresa().getNombre());
            System.out.println("SUBTITULO : " + duplicarFirmasReportes.getSubtitulofirma());
            System.out.println("PERSONA : " + duplicarFirmasReportes.getPersonaFirma().getNombre());
            System.out.println("CARGO : " + duplicarFirmasReportes.getCargo().getNombre());
            System.out.println("--------------DUPLICAR------------------------");

            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                pais = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:pais");
                pais.setFilterStyle("display: none; visibility: hidden;");
                subTituloFirma = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:subTituloFirma");
                subTituloFirma.setFilterStyle("display: none; visibility: hidden;");
                personafir = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:personafir");
                personafir.setFilterStyle("display: none; visibility: hidden;");
                cargo = (Column) c.getViewRoot().findComponent("form:datosFirmasReportes:cargo");
                cargo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosFirmasReportes");
                bandera = 0;
                filtrarFirmasReportes = null;
                tipoLista = 0;
            }
            duplicarFirmasReportes = new FirmasReportes();
            duplicarFirmasReportes.setEmpresa(new Empresas());
            duplicarFirmasReportes.setCargo(new Cargos());
            duplicarFirmasReportes.setPersonaFirma(new Personas());

            PrimefacesContextUI.ejecutar("PF('duplicarRegistroFirmasReportes').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarFirmasReportes() {
        duplicarFirmasReportes = new FirmasReportes();
        duplicarFirmasReportes.setEmpresa(new Empresas());
        duplicarFirmasReportes.setPersonaFirma(new Personas());
        duplicarFirmasReportes.setCargo(new Cargos());
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFirmasReportesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "FIRMASREPORTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFirmasReportesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "FIRMASREPORTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listFirmasReportes.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "FIRMASREPORTES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("FIRMASREPORTES")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<FirmasReportes> getListFirmasReportes() {
        if (listFirmasReportes == null) {
            listFirmasReportes = administrarFirmasReportes.consultarFirmasReportes();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listFirmasReportes == null || listFirmasReportes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listFirmasReportes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listFirmasReportes;
    }

    public void setListFirmasReportes(List<FirmasReportes> listFirmasReportes) {
        this.listFirmasReportes = listFirmasReportes;
    }

    public List<FirmasReportes> getFiltrarFirmasReportes() {
        return filtrarFirmasReportes;
    }

    public void setFiltrarFirmasReportes(List<FirmasReportes> filtrarFirmasReportes) {
        this.filtrarFirmasReportes = filtrarFirmasReportes;
    }

    public FirmasReportes getNuevoFirmasReportes() {
        return nuevoFirmasReportes;
    }

    public void setNuevoFirmasReportes(FirmasReportes nuevoFirmasReportes) {
        this.nuevoFirmasReportes = nuevoFirmasReportes;
    }

    public FirmasReportes getDuplicarFirmasReportes() {
        return duplicarFirmasReportes;
    }

    public void setDuplicarFirmasReportes(FirmasReportes duplicarFirmasReportes) {
        this.duplicarFirmasReportes = duplicarFirmasReportes;
    }

    public FirmasReportes getEditarFirmasReportes() {
        return editarFirmasReportes;
    }

    public void setEditarFirmasReportes(FirmasReportes editarFirmasReportes) {
        this.editarFirmasReportes = editarFirmasReportes;
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

    public List<Empresas> getListaEmpresas() {
        if (listaEmpresas == null) {
            listaEmpresas = administrarFirmasReportes.consultarLOVEmpresas();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listaEmpresas == null || listaEmpresas.isEmpty()) {
            infoLOVEmpresa = "Cantidad de registros: 0 ";
        } else {
            infoLOVEmpresa = "Cantidad de registros: " + listaEmpresas.size();
        }
        RequestContext.getCurrentInstance().update("form:infoLOVEmpresa");
        return listaEmpresas;
    }

    public void setListaEmpresas(List<Empresas> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public List<Empresas> getFiltradoEmpresas() {
        return filtradoEmpresas;
    }

    public void setFiltradoEmpresas(List<Empresas> filtradoEmpresas) {
        this.filtradoEmpresas = filtradoEmpresas;
    }

    public Empresas getEmpresaSeleccionado() {
        return empresaSeleccionado;
    }

    public void setEmpresaSeleccionado(Empresas empresaSeleccionado) {
        this.empresaSeleccionado = empresaSeleccionado;
    }

    public List<Personas> getListaPersonas() {
        if (listaPersonas == null) {
            listaPersonas = administrarFirmasReportes.consultarLOVPersonas();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listaPersonas == null || listaPersonas.isEmpty()) {
            infoLOVPersona = "Cantidad de registros: 0 ";
        } else {
            infoLOVPersona = "Cantidad de registros: " + listaPersonas.size();
        }
        RequestContext.getCurrentInstance().update("form:infoLOVPersona");
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public List<Personas> getFiltradoPersonas() {
        return filtradoPersonas;
    }

    public void setFiltradoPersonas(List<Personas> filtradoPersonas) {
        this.filtradoPersonas = filtradoPersonas;
    }

    public Personas getPersonaSeleccionado() {
        return personaSeleccionado;
    }

    public void setPersonaSeleccionado(Personas personaSeleccionado) {
        this.personaSeleccionado = personaSeleccionado;
    }

    public List<Cargos> getListaCargos() {
        if (listaCargos == null) {
            listaCargos = administrarFirmasReportes.consultarLOVCargos();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listaCargos == null || listaCargos.isEmpty()) {
            infoLOVCargo = "Cantidad de registros: 0 ";
        } else {
            infoLOVCargo = "Cantidad de registros: " + listaCargos.size();
        }
        RequestContext.getCurrentInstance().update("form:infoLOVCargo");
        return listaCargos;
    }

    public void setListaCargos(List<Cargos> listaCargos) {
        this.listaCargos = listaCargos;
    }

    public List<Cargos> getFiltradoCargos() {
        return filtradoCargos;
    }

    public void setFiltradoCargos(List<Cargos> filtradoCargos) {
        this.filtradoCargos = filtradoCargos;
    }

    public Cargos getCargoSeleccionado() {
        return cargoSeleccionado;
    }

    public void setCargoSeleccionado(Cargos cargoSeleccionado) {
        this.cargoSeleccionado = cargoSeleccionado;
    }

    public FirmasReportes getFirmaReporteSeleccionada() {
        return firmaReporteSeleccionada;
    }

    public void setFirmaReporteSeleccionada(FirmasReportes firmaReporteSeleccionada) {
        this.firmaReporteSeleccionada = firmaReporteSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoLOVCargo() {
        return infoLOVCargo;
    }

    public void setInfoLOVCargo(String infoLOVCargo) {
        this.infoLOVCargo = infoLOVCargo;
    }

    public String getInfoLOVPersona() {
        return infoLOVPersona;
    }

    public void setInfoLOVPersona(String infoLOVPersona) {
        this.infoLOVPersona = infoLOVPersona;
    }

    public String getInfoLOVEmpresa() {
        return infoLOVEmpresa;
    }

    public void setInfoLOVEmpresa(String infoLOVEmpresa) {
        this.infoLOVEmpresa = infoLOVEmpresa;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

}
