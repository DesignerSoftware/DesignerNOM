/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Empleados;
import Entidades.TiposTallas;
import Entidades.VigenciasTallas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarVigenciasTallasInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
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
public class ControlVigenciasTallas implements Serializable {

    @EJB
    AdministrarVigenciasTallasInterface administrarVigenciasTallas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<VigenciasTallas> listVigenciasTallas;
    private List<VigenciasTallas> filtrarVigenciasTallas;
    private List<VigenciasTallas> crearVigenciasTallas;
    private List<VigenciasTallas> modificarVigenciasTallas;
    private List<VigenciasTallas> borrarVigenciasTallas;
    private VigenciasTallas nuevoVigenciaTalla;
    private VigenciasTallas duplicarVigenciaTalla;
    private VigenciasTallas editarVigenciaTalla;
    private VigenciasTallas vigenciaTallaSeleccionada;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column idTipoTalla, fecha, idTalla, idObservaciones;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger secuenciaEmpleado;
//Empleado
    private Empleados empleadoSeleccionada;

    //autocompletar
    private String tipoTalla;
    private List<TiposTallas> listaTiposTallas;
    private List<TiposTallas> filtradoTiposTallas;
    private TiposTallas tipoTallaSeleccionado;
    private String nuevoParentesco;
    private String infoRegistro;
    private String infoRegistroTiposFamiliares;
    private int tamano;
    private Date backUpFecha;

    public ControlVigenciasTallas() {
        listVigenciasTallas = null;
        crearVigenciasTallas = new ArrayList<VigenciasTallas>();
        modificarVigenciasTallas = new ArrayList<VigenciasTallas>();
        borrarVigenciasTallas = new ArrayList<VigenciasTallas>();
        permitirIndex = true;
        guardado = true;
        editarVigenciaTalla = new VigenciasTallas();
        nuevoVigenciaTalla = new VigenciasTallas();
        nuevoVigenciaTalla.setTipoTalla(new TiposTallas());
        nuevoVigenciaTalla.setEmpleado(new Empleados());
        duplicarVigenciaTalla = new VigenciasTallas();
        duplicarVigenciaTalla.setTipoTalla(new TiposTallas());
        duplicarVigenciaTalla.setEmpleado(new Empleados());
        empleadoSeleccionada = null;
        listaTiposTallas = null;
        filtradoTiposTallas = null;
        tipoLista = 0;
        tamano = 270;
        aceptar = true;
        secuenciaEmpleado = null;

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasTallas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger sec) {
        System.out.println("ENTRE A RECIBIR EMPLEADO EN CONTROLVIGENCIASTALLAS");
        if (sec == null) {
            System.out.println("ERROR EN RECIVIR LA SECUENCIA DEL EMPLEADO EN ControlVigenciasTallas");
            //  sec = BigInteger.valueOf(10661039);
        }
        secuenciaEmpleado = sec;
        listVigenciasTallas = null;
        empleadoSeleccionada = null;
        getEmpleadoSeleccionada();
        getListVigenciasTallas();
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlVigenciasTallas.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarVigenciasTallas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlVigenciasTallas eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void mostrarInfo(int indice, int celda) {
        int contador = 0;
        int fechas = 0;
        mensajeValidacion = " ";
        index = indice;
        cualCelda = celda;
        RequestContext context = RequestContext.getCurrentInstance();
        if (permitirIndex == true) {
            if (tipoLista == 0) {
                secRegistro = listVigenciasTallas.get(index).getSecuencia();
                if (listVigenciasTallas.get(indice).getFechavigencia() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    listVigenciasTallas.get(indice).setFechavigencia(backUpFecha);
                    contador++;
                }
                if (contador == 0) {
                    if (!crearVigenciasTallas.contains(listVigenciasTallas.get(indice))) {
                        if (modificarVigenciasTallas.isEmpty()) {
                            modificarVigenciasTallas.add(listVigenciasTallas.get(indice));
                        } else if (!modificarVigenciasTallas.contains(listVigenciasTallas.get(indice))) {
                            modificarVigenciasTallas.add(listVigenciasTallas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");

                    } else {
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
                    }
                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                }
            } else {

                secRegistro = filtrarVigenciasTallas.get(index).getSecuencia();
                if (filtrarVigenciasTallas.get(indice).getFechavigencia() == null) {
                    filtrarVigenciasTallas.get(indice).setFechavigencia(backUpFecha);
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    contador++;
                }
                if (contador == 0) {
                    if (!crearVigenciasTallas.contains(filtrarVigenciasTallas.get(indice))) {
                        if (modificarVigenciasTallas.isEmpty()) {
                            modificarVigenciasTallas.add(filtrarVigenciasTallas.get(indice));
                        } else if (!modificarVigenciasTallas.contains(filtrarVigenciasTallas.get(indice))) {
                            modificarVigenciasTallas.add(filtrarVigenciasTallas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");

                    } else {
                        if (guardado == true) {
                            guardado = false;
                            RequestContext.getCurrentInstance().update("form:ACEPTAR");
                        }
                        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
                    }
                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");

                }

                RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
                index = -1;
                secRegistro = null;
            }

        }

    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listVigenciasTallas.get(index).getSecuencia();

            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpFecha = listVigenciasTallas.get(index).getFechavigencia();
                } else {
                    backUpFecha = filtrarVigenciasTallas.get(index).getFechavigencia();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    tipoTalla = listVigenciasTallas.get(index).getTipoTalla().getDescripcion();
                } else {
                    tipoTalla = filtrarVigenciasTallas.get(index).getTipoTalla().getDescripcion();
                }
                System.out.println("Cambiar Indice Tipos Tallas: " + tipoTalla);
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlVigenciasTallas.asignarIndex \n");
            RequestContext context = RequestContext.getCurrentInstance();

            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dig == 1) {
                RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
                PrimefacesContextUI.ejecutar("PF('tipostallasDialogo').show()");
                dig = -1;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlVigenciasTallas.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();

            if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
                PrimefacesContextUI.ejecutar("PF('tipostallasDialogo').show()");
                tipoActualizacion = 0;
            }

        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
            idTipoTalla.setFilterStyle("display: none; visibility: hidden;");
            idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
            idTalla.setFilterStyle("display: none; visibility: hidden;");
            idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
            idObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            bandera = 0;
            filtrarVigenciasTallas = null;
            tipoLista = 0;
        }

        borrarVigenciasTallas.clear();
        crearVigenciasTallas.clear();
        modificarVigenciasTallas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listVigenciasTallas = null;
        guardado = true;
        permitirIndex = true;
        getListVigenciasTallas();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listVigenciasTallas == null || listVigenciasTallas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listVigenciasTallas.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
            idTipoTalla.setFilterStyle("display: none; visibility: hidden;");
            idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
            idTalla.setFilterStyle("display: none; visibility: hidden;");
            idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
            idObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            bandera = 0;
            filtrarVigenciasTallas = null;
            tipoLista = 0;
        }

        borrarVigenciasTallas.clear();
        crearVigenciasTallas.clear();
        modificarVigenciasTallas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listVigenciasTallas = null;
        guardado = true;
        permitirIndex = true;
        getListVigenciasTallas();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listVigenciasTallas == null || listVigenciasTallas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listVigenciasTallas.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
            fecha.setFilterStyle("width: 85% !important;");
            idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
            idTipoTalla.setFilterStyle("width: 85% !important;");
            idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
            idTalla.setFilterStyle("width: 85% !important;");
            idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
            idObservaciones.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
            idTipoTalla.setFilterStyle("display: none; visibility: hidden;");
            idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
            idTalla.setFilterStyle("display: none; visibility: hidden;");
            idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
            idObservaciones.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            bandera = 0;
            filtrarVigenciasTallas = null;
            tipoLista = 0;
        }
    }

    /*   public void modificandoHvReferencia(int indice, String confirmarCambio, String valorConfirmar) {
     System.err.println("ENTRE A MODIFICAR HV Referencia");
     index = indice;

     int contador = 0;
     boolean banderita = false;
     Short a;
     a = null;
     RequestContext context = RequestContext.getCurrentInstance();
     System.err.println("TIPO LISTA = " + tipoLista);
     if (confirmarCambio.equalsIgnoreCase("N")) {
     System.err.println("ENTRE A MODIFICAR HvReferencia, CONFIRMAR CAMBIO ES N");
     if (tipoLista == 0) {
     if (!crearVigenciasTallasFamiliares.contains(listVigenciasTallas.get(indice))) {

     if (listVigenciasTallas.get(indice).getFechavigencia().isEmpty()) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     } else if (listVigenciasTallas.get(indice).getFechavigencia().equals(" ")) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     } else {
     banderita = true;
     }

     if (banderita == true) {
     if (modificarVigenciasTallasFamiliares.isEmpty()) {
     modificarVigenciasTallasFamiliares.add(listVigenciasTallas.get(indice));
     } else if (!modificarVigenciasTallasFamiliares.contains(listVigenciasTallas.get(indice))) {
     modificarVigenciasTallasFamiliares.add(listVigenciasTallas.get(indice));
     }
     if (guardado == true) {
     guardado = false;
     }

     } else {
     RequestContext.getCurrentInstance().update("form:validacionModificar");
     PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
     cancelarModificacion();
     }
     index = -1;
     secRegistro = null;
     }
     } else {

     if (!crearVigenciasTallasFamiliares.contains(filtrarVigenciasTallas.get(indice))) {
     if (filtrarVigenciasTallas.get(indice).getFechavigencia().isEmpty()) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     }
     if (filtrarVigenciasTallas.get(indice).getFechavigencia().equals(" ")) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     }

     if (banderita == true) {
     if (modificarVigenciasTallasFamiliares.isEmpty()) {
     modificarVigenciasTallasFamiliares.add(filtrarVigenciasTallas.get(indice));
     } else if (!modificarVigenciasTallasFamiliares.contains(filtrarVigenciasTallas.get(indice))) {
     modificarVigenciasTallasFamiliares.add(filtrarVigenciasTallas.get(indice));
     }
     if (guardado == true) {
     guardado = false;
     }

     } else {
     RequestContext.getCurrentInstance().update("form:validacionModificar");
     PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
     cancelarModificacion();
     }
     index = -1;
     secRegistro = null;
     }

     }
     RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
     }

     }
     */
    /**
     *
     * @param indice donde se encuentra posicionado
     * @param confirmarCambio nombre de la columna
     * @param valorConfirmar valor ingresado
     */
    public void modificarVigenciasTallas(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0, pass = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR HvReferencia, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearVigenciasTallas.contains(listVigenciasTallas.get(indice))) {

                    if (listVigenciasTallas.get(indice).getFechavigencia() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listVigenciasTallas.get(indice).setFechavigencia(backUpFecha);
                    } else {
                        pass++;
                    }

                    if (pass == 1) {
                        if (modificarVigenciasTallas.isEmpty()) {
                            modificarVigenciasTallas.add(listVigenciasTallas.get(indice));
                        } else if (!modificarVigenciasTallas.contains(listVigenciasTallas.get(indice))) {
                            modificarVigenciasTallas.add(listVigenciasTallas.get(indice));
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
                    if (listVigenciasTallas.get(indice).getFechavigencia() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listVigenciasTallas.get(indice).setFechavigencia(backUpFecha);
                    } else {
                        pass++;
                    }

                    if (pass == 1) {

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
            } else {

                if (!crearVigenciasTallas.contains(filtrarVigenciasTallas.get(indice))) {
                    if (filtrarVigenciasTallas.get(indice).getFechavigencia() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarVigenciasTallas.get(indice).setFechavigencia(backUpFecha);
                    } else {
                        pass++;
                    }

                    if (pass == 1) {
                        if (modificarVigenciasTallas.isEmpty()) {
                            modificarVigenciasTallas.add(filtrarVigenciasTallas.get(indice));
                        } else if (!modificarVigenciasTallas.contains(filtrarVigenciasTallas.get(indice))) {
                            modificarVigenciasTallas.add(filtrarVigenciasTallas.get(indice));
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
                    if (filtrarVigenciasTallas.get(indice).getFechavigencia() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarVigenciasTallas.get(indice).setFechavigencia(backUpFecha);
                    } else {
                        pass++;
                    }
                    if (pass == 1) {

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
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
        } else if (confirmarCambio.equalsIgnoreCase("TIPOSTALLAS")) {
            if (listVigenciasTallas.get(indice).getTipoTalla().getDescripcion() != null || !listVigenciasTallas.get(indice).getTipoTalla().getDescripcion().isEmpty()) {
                if (tipoLista == 0) {
                    listVigenciasTallas.get(indice).getTipoTalla().setDescripcion(tipoTalla);

                } else {
                    filtrarVigenciasTallas.get(indice).getTipoTalla().setDescripcion(tipoTalla);
                }

                for (int i = 0; i < listaTiposTallas.size(); i++) {
                    if (listaTiposTallas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listVigenciasTallas.get(indice).setTipoTalla(listaTiposTallas.get(indiceUnicoElemento));
                    } else {
                        filtrarVigenciasTallas.get(indice).setTipoTalla(listaTiposTallas.get(indiceUnicoElemento));
                    }
                    listaTiposTallas.clear();
                    listaTiposTallas = null;
                    getListaTiposTallas();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
                    PrimefacesContextUI.ejecutar("PF('tipostallasDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                System.out.println("PUSE UN VACIO");
                listaTiposTallas.clear();
                listaTiposTallas = null;
                getListaTiposTallas();
                RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
                PrimefacesContextUI.ejecutar("PF('tipostallasDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearVigenciasTallas.contains(listVigenciasTallas.get(indice))) {

                        if (modificarVigenciasTallas.isEmpty()) {
                            modificarVigenciasTallas.add(listVigenciasTallas.get(indice));
                        } else if (!modificarVigenciasTallas.contains(listVigenciasTallas.get(indice))) {
                            modificarVigenciasTallas.add(listVigenciasTallas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearVigenciasTallas.contains(filtrarVigenciasTallas.get(indice))) {

                        if (modificarVigenciasTallas.isEmpty()) {
                            modificarVigenciasTallas.add(filtrarVigenciasTallas.get(indice));
                        } else if (!modificarVigenciasTallas.contains(filtrarVigenciasTallas.get(indice))) {
                            modificarVigenciasTallas.add(filtrarVigenciasTallas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {
            }

            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");

        }
        RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void actualizarTipoFamiliar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listVigenciasTallas.get(index).setTipoTalla(tipoTallaSeleccionado);
                if (!crearVigenciasTallas.contains(listVigenciasTallas.get(index))) {
                    if (modificarVigenciasTallas.isEmpty()) {
                        modificarVigenciasTallas.add(listVigenciasTallas.get(index));
                    } else if (!modificarVigenciasTallas.contains(listVigenciasTallas.get(index))) {
                        modificarVigenciasTallas.add(listVigenciasTallas.get(index));
                    }
                }
            } else {
                filtrarVigenciasTallas.get(index).setTipoTalla(tipoTallaSeleccionado);
                if (!crearVigenciasTallas.contains(filtrarVigenciasTallas.get(index))) {
                    if (modificarVigenciasTallas.isEmpty()) {
                        modificarVigenciasTallas.add(filtrarVigenciasTallas.get(index));
                    } else if (!modificarVigenciasTallas.contains(filtrarVigenciasTallas.get(index))) {
                        modificarVigenciasTallas.add(filtrarVigenciasTallas.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
        } else if (tipoActualizacion == 1) {
            nuevoVigenciaTalla.setTipoTalla(tipoTallaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoTalla");
        } else if (tipoActualizacion == 2) {
            System.out.println(tipoTallaSeleccionado.getDescripcion());
            duplicarVigenciaTalla.setTipoTalla(tipoTallaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionTipoTallas");
        }
        filtrarVigenciasTallas = null;
        tipoTallaSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;

        context.reset("form:lovTiposTallas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTiposTallas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tipostallasDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovTiposTallas");
    }

    public void cancelarCambioTiposTallas() {
        filtradoTiposTallas = null;
        tipoTallaSeleccionado = new TiposTallas();
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTiposTallas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTiposTallas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('tipostallasDialogo').hide()");
    }

    public void borrandoVigenciasTallas() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoVigenciasTallas");
                if (!modificarVigenciasTallas.isEmpty() && modificarVigenciasTallas.contains(listVigenciasTallas.get(index))) {
                    int modIndex = modificarVigenciasTallas.indexOf(listVigenciasTallas.get(index));
                    modificarVigenciasTallas.remove(modIndex);
                    borrarVigenciasTallas.add(listVigenciasTallas.get(index));
                } else if (!crearVigenciasTallas.isEmpty() && crearVigenciasTallas.contains(listVigenciasTallas.get(index))) {
                    int crearIndex = crearVigenciasTallas.indexOf(listVigenciasTallas.get(index));
                    crearVigenciasTallas.remove(crearIndex);
                } else {
                    borrarVigenciasTallas.add(listVigenciasTallas.get(index));
                }
                listVigenciasTallas.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoVigenciasTallas ");
                if (!modificarVigenciasTallas.isEmpty() && modificarVigenciasTallas.contains(filtrarVigenciasTallas.get(index))) {
                    int modIndex = modificarVigenciasTallas.indexOf(filtrarVigenciasTallas.get(index));
                    modificarVigenciasTallas.remove(modIndex);
                    borrarVigenciasTallas.add(filtrarVigenciasTallas.get(index));
                } else if (!crearVigenciasTallas.isEmpty() && crearVigenciasTallas.contains(filtrarVigenciasTallas.get(index))) {
                    int crearIndex = crearVigenciasTallas.indexOf(filtrarVigenciasTallas.get(index));
                    crearVigenciasTallas.remove(crearIndex);
                } else {
                    borrarVigenciasTallas.add(filtrarVigenciasTallas.get(index));
                }
                int VCIndex = listVigenciasTallas.indexOf(filtrarVigenciasTallas.get(index));
                listVigenciasTallas.remove(VCIndex);
                filtrarVigenciasTallas.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listVigenciasTallas == null || listVigenciasTallas.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listVigenciasTallas.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    /* public void verificarBorrado() {
     System.out.println("Estoy en verificarBorrado");
     try {
     System.err.println("Control Secuencia de ControlVigenciasTallas ");
     competenciasCargos = administrarHvEntrevistas.verificarBorradoCompetenciasCargos(listHvEntrevistas.get(index).getSecuencia());

     if (competenciasCargos.intValueExact() == 0) {
     System.out.println("Borrado==0");
     borrandoHvEntrevistas();
     } else {
     System.out.println("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     RequestContext.getCurrentInstance().update("form:validacionBorrar");
     PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
     index = -1;

     competenciasCargos = new BigDecimal(-1);

     }
     } catch (Exception e) {
     System.err.println("ERROR ControlVigenciasTallas verificarBorrado ERROR " + e);
     }
     }*/
    public void revisarDialogoGuardar() {

        if (!borrarVigenciasTallas.isEmpty() || !crearVigenciasTallas.isEmpty() || !modificarVigenciasTallas.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarVigenciasTallas() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarVigenciasTallas");
            if (!borrarVigenciasTallas.isEmpty()) {
                for (int i = 0; i < borrarVigenciasTallas.size(); i++) {
                    System.out.println("Borrando...");
                    if (borrarVigenciasTallas.get(i).getTipoTalla().getSecuencia() == null) {
                        borrarVigenciasTallas.get(i).setTipoTalla(null);
                    }
                }
                administrarVigenciasTallas.borrarVigenciasTallas(borrarVigenciasTallas);
                //mostrarBorrados
                registrosBorrados = borrarVigenciasTallas.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarVigenciasTallas.clear();
            }
            if (!crearVigenciasTallas.isEmpty()) {
                for (int i = 0; i < crearVigenciasTallas.size(); i++) {

                    if (crearVigenciasTallas.get(i).getTipoTalla().getSecuencia() == null) {
                        crearVigenciasTallas.get(i).setTipoTalla(null);
                    }
                }
                System.out.println("Creando...");
                administrarVigenciasTallas.crearVigenciasTallas(crearVigenciasTallas);
                crearVigenciasTallas.clear();
            }
            if (!modificarVigenciasTallas.isEmpty()) {
                for (int i = 0; i < modificarVigenciasTallas.size(); i++) {
                    if (modificarVigenciasTallas.get(i).getTipoTalla().getSecuencia() == null) {
                        modificarVigenciasTallas.get(i).setTipoTalla(null);
                    }
                }
                administrarVigenciasTallas.modificarVigenciasTallas(modificarVigenciasTallas);
                modificarVigenciasTallas.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listVigenciasTallas = null;
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

            k = 0;
        }
        index = -1;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarVigenciaTalla = listVigenciasTallas.get(index);
            }
            if (tipoLista == 1) {
                editarVigenciaTalla = filtrarVigenciasTallas.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                PrimefacesContextUI.ejecutar("PF('editarFecha').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTipoTalla");
                PrimefacesContextUI.ejecutar("PF('editTipoTalla').show()");
                cualCelda = -1;

            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTallita");
                PrimefacesContextUI.ejecutar("PF('editTallita').show()");
                cualCelda = -1;

            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editObservacion");
                PrimefacesContextUI.ejecutar("PF('editObservacion').show()");
                cualCelda = -1;

            }

        }
        index = -1;
        secRegistro = null;
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {

        if (Campo.equalsIgnoreCase("TIPOSTALLAS")) {
            if (tipoNuevo == 1) {
                nuevoParentesco = nuevoVigenciaTalla.getTipoTalla().getDescripcion();
            } else if (tipoNuevo == 2) {
                nuevoParentesco = duplicarVigenciaTalla.getTipoTalla().getDescripcion();
            }
        }
        System.err.println("NUEVO PARENTESCO " + nuevoParentesco);
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOSTALLAS")) {
            if (!nuevoVigenciaTalla.getTipoTalla().getDescripcion().equals("")) {
                if (tipoNuevo == 1) {
                    nuevoVigenciaTalla.getTipoTalla().setDescripcion(nuevoParentesco);
                }
                for (int i = 0; i < listaTiposTallas.size(); i++) {
                    if (listaTiposTallas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            } else {

                if (tipoNuevo == 1) {
                    nuevoVigenciaTalla.setTipoTalla(new TiposTallas());
                    coincidencias = 1;
                }
                coincidencias = 1;
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoVigenciaTalla.setTipoTalla(listaTiposTallas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoTalla");
                }
                listaTiposTallas.clear();
                listaTiposTallas = null;
                getListaTiposTallas();
            } else {
                RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
                PrimefacesContextUI.ejecutar("PF('tipostallasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoTalla");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionTipoTallas");
                }
            }
        }
    }

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOSTALLAS")) {
            if (!duplicarVigenciaTalla.getTipoTalla().getDescripcion().equals("")) {
                if (tipoNuevo == 2) {
                    duplicarVigenciaTalla.getTipoTalla().setDescripcion(nuevoParentesco);
                }
                for (int i = 0; i < listaTiposTallas.size(); i++) {
                    if (listaTiposTallas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 2) {
                        duplicarVigenciaTalla.setTipoTalla(listaTiposTallas.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionTipoTallas");

                    }
                    listaTiposTallas.clear();
                    listaTiposTallas = null;
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionTipoTallas");
                    getListaTiposTallas();
                } else {
                    RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
                    PrimefacesContextUI.ejecutar("PF('tipostallasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionTipoTallas");
                    }
                }
            } else {
                if (tipoNuevo == 2) {
                    duplicarVigenciaTalla.setTipoTalla(new TiposTallas());
                    System.out.println("NUEVO PARENTESCO " + nuevoParentesco);
                    if (tipoLista == 0) {
                        if (index >= 0) {
                            listVigenciasTallas.get(index).getTipoTalla().setDescripcion(nuevoParentesco);
                            System.err.println("tipo lista" + tipoLista);
                            System.err.println("Secuencia Parentesco " + listVigenciasTallas.get(index).getTipoTalla().getSecuencia());
                        }
                    } else if (tipoLista == 1) {
                        filtrarVigenciasTallas.get(index).getTipoTalla().setDescripcion(nuevoParentesco);
                    }

                }
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionTipoTallas");

        }
    }

    public void asignarVariableTiposTallasNuevo(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tipostallasDialogo");
        PrimefacesContextUI.ejecutar("PF('tipostallasDialogo').show()");
    }

    public void agregarNuevoTiposTallas() {
        System.out.println("agregarNuevoHvRefencias");
        int contador = 0;
        Short a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoVigenciaTalla.getFechavigencia() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Fecha Vigencia \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("bandera");
            contador++;
        }
        if (nuevoVigenciaTalla.getTipoTalla().getSecuencia() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Tipo Talla \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("bandera");
            contador++;
        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
                fecha.setFilterStyle("display: none; visibility: hidden;");
                idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
                idTipoTalla.setFilterStyle("display: none; visibility: hidden;");
                idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
                idTalla.setFilterStyle("display: none; visibility: hidden;");
                idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
                idObservaciones.setFilterStyle("display: none; visibility: hidden;");

                RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
                bandera = 0;
                filtrarVigenciasTallas = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoVigenciaTalla.setSecuencia(l);

            nuevoVigenciaTalla.setEmpleado(empleadoSeleccionada);
            crearVigenciasTallas.add(nuevoVigenciaTalla);
            listVigenciasTallas.add(nuevoVigenciaTalla);
            nuevoVigenciaTalla = new VigenciasTallas();
            nuevoVigenciaTalla.setTipoTalla(new TiposTallas());
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            infoRegistro = "Cantidad de registros: " + listVigenciasTallas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroVigenciasTallas').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoVigenciasTallas() {
        System.out.println("limpiarNuevoHvEntrevistas");
        nuevoVigenciaTalla = new VigenciasTallas();
        nuevoVigenciaTalla.setTipoTalla(new TiposTallas());
        nuevoVigenciaTalla.setEmpleado(new Empleados());
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoVigenciasTallas() {
        System.out.println("duplicandoVigenciasTallas");
        if (index >= 0) {
            duplicarVigenciaTalla = new VigenciasTallas();
            duplicarVigenciaTalla.setTipoTalla(new TiposTallas());

            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarVigenciaTalla.setSecuencia(l);
                duplicarVigenciaTalla.setFechavigencia(listVigenciasTallas.get(index).getFechavigencia());
                duplicarVigenciaTalla.setTipoTalla(listVigenciasTallas.get(index).getTipoTalla());
                duplicarVigenciaTalla.setTalla(listVigenciasTallas.get(index).getTalla());
                duplicarVigenciaTalla.setObservaciones(listVigenciasTallas.get(index).getObservaciones());
                duplicarVigenciaTalla.setEmpleado(listVigenciasTallas.get(index).getEmpleado());
            }
            if (tipoLista == 1) {
                duplicarVigenciaTalla.setSecuencia(l);
                duplicarVigenciaTalla.setFechavigencia(filtrarVigenciasTallas.get(index).getFechavigencia());
                duplicarVigenciaTalla.setTipoTalla(filtrarVigenciasTallas.get(index).getTipoTalla());
                duplicarVigenciaTalla.setTalla(filtrarVigenciasTallas.get(index).getTalla());
                duplicarVigenciaTalla.setObservaciones(filtrarVigenciasTallas.get(index).getObservaciones());
                duplicarVigenciaTalla.setEmpleado(filtrarVigenciasTallas.get(index).getEmpleado());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPanelVigenciasTallas");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroVigenciasTallas').show()");
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR VIGENCIASTALLAS");
        int contador = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarVigenciaTalla.getFechavigencia() == null) {
            mensajeValidacion = mensajeValidacion + "   *Fecha Vigencia \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarVigenciaTalla.getTipoTalla().getSecuencia() == null) {
            mensajeValidacion = mensajeValidacion + "*Tipo Talla \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (contador == 2) {

            if (crearVigenciasTallas.contains(duplicarVigenciaTalla)) {
                System.out.println("Ya lo contengo.");
            } else {
                crearVigenciasTallas.add(duplicarVigenciaTalla);
            }
            listVigenciasTallas.add(duplicarVigenciaTalla);
            RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listVigenciasTallas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                fecha = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:fecha");
                fecha.setFilterStyle("display: none; visibility: hidden;");
                idTipoTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTipoTalla");
                idTipoTalla.setFilterStyle("display: none; visibility: hidden;");
                idTalla = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idTalla");
                idTalla.setFilterStyle("display: none; visibility: hidden;");
                idObservaciones = (Column) c.getViewRoot().findComponent("form:datosVigenciasTallas:idObservaciones");
                idObservaciones.setFilterStyle("display: none; visibility: hidden;");

                RequestContext.getCurrentInstance().update("form:datosVigenciasTallas");
                bandera = 0;
                filtrarVigenciasTallas = null;
                tipoLista = 0;
            }
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroVigenciasTallas').hide()");
            duplicarVigenciaTalla = new VigenciasTallas();

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarVigenciasTallas() {
        duplicarVigenciaTalla = new VigenciasTallas();
        duplicarVigenciaTalla.setTipoTalla(new TiposTallas());
        duplicarVigenciaTalla.setEmpleado(new Empleados());

    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasTallasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "REFERENCIASFAMILIARES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasTallasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "REFERENCIASFAMILIARES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listVigenciasTallas.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "VIGENCIASTALLAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASTALLAS")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<VigenciasTallas> getListVigenciasTallas() {
        if (listVigenciasTallas == null) {
            listVigenciasTallas = administrarVigenciasTallas.consultarVigenciasTallasPorEmpleado(secuenciaEmpleado);
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listVigenciasTallas == null || listVigenciasTallas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listVigenciasTallas.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listVigenciasTallas;
    }

    public void setListVigenciasTallas(List<VigenciasTallas> listVigenciasTallas) {
        this.listVigenciasTallas = listVigenciasTallas;
    }

    public List<VigenciasTallas> getFiltrarVigenciasTallas() {
        return filtrarVigenciasTallas;
    }

    public void setFiltrarVigenciasTallas(List<VigenciasTallas> filtrarVigenciasTallas) {
        this.filtrarVigenciasTallas = filtrarVigenciasTallas;
    }

    public List<VigenciasTallas> getCrearVigenciasTallas() {
        return crearVigenciasTallas;
    }

    public void setCrearVigenciasTallas(List<VigenciasTallas> crearVigenciasTallas) {
        this.crearVigenciasTallas = crearVigenciasTallas;
    }

    public VigenciasTallas getNuevoVigenciaTalla() {
        return nuevoVigenciaTalla;
    }

    public void setNuevoVigenciaTalla(VigenciasTallas nuevoVigenciaTalla) {
        this.nuevoVigenciaTalla = nuevoVigenciaTalla;
    }

    public VigenciasTallas getDuplicarVigenciaTalla() {
        return duplicarVigenciaTalla;
    }

    public void setDuplicarVigenciaTalla(VigenciasTallas duplicarVigenciaTalla) {
        this.duplicarVigenciaTalla = duplicarVigenciaTalla;
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

    public BigInteger getSecuenciaPersona() {
        return secuenciaEmpleado;
    }

    public void setSecuenciaPersona(BigInteger secuenciaPersona) {
        this.secuenciaEmpleado = secuenciaPersona;
    }

    public List<TiposTallas> getListaTiposTallas() {
        if (listaTiposTallas == null) {
            listaTiposTallas = administrarVigenciasTallas.consultarLOVTiposTallas();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaTiposTallas == null || listaTiposTallas.isEmpty()) {
            infoRegistroTiposFamiliares = "Cantidad de registros: 0 ";
        } else {
            infoRegistroTiposFamiliares = "Cantidad de registros: " + listaTiposTallas.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroTiposFamiliares");
        return listaTiposTallas;
    }

    public void setListaTiposTallas(List<TiposTallas> listaTiposTallas) {
        this.listaTiposTallas = listaTiposTallas;
    }

    public List<TiposTallas> getFiltradoTiposTallas() {
        return filtradoTiposTallas;
    }

    public void setFiltradoTiposTallas(List<TiposTallas> filtradoTiposTallas) {
        this.filtradoTiposTallas = filtradoTiposTallas;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public VigenciasTallas getEditarVigenciaTalla() {
        return editarVigenciaTalla;
    }

    public void setEditarVigenciaTalla(VigenciasTallas editarVigenciaTalla) {
        this.editarVigenciaTalla = editarVigenciaTalla;
    }

    public TiposTallas getTipoFamiliarSeleccionado() {
        return tipoTallaSeleccionado;
    }

    public void setTipoFamiliarSeleccionado(TiposTallas tipoFamiliarSeleccionado) {
        this.tipoTallaSeleccionado = tipoFamiliarSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Empleados getEmpleadoSeleccionada() {
        if (empleadoSeleccionada == null) {
            empleadoSeleccionada = administrarVigenciasTallas.consultarEmpleadoSecuenciaPersona(secuenciaEmpleado);
        }
        return empleadoSeleccionada;
    }

    public void setEmpleadoSeleccionada(Empleados empleadoSeleccionada) {
        this.empleadoSeleccionada = empleadoSeleccionada;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroTiposFamiliares() {
        return infoRegistroTiposFamiliares;
    }

    public void setInfoRegistroTiposFamiliares(String infoRegistroTiposFamiliares) {
        this.infoRegistroTiposFamiliares = infoRegistroTiposFamiliares;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public VigenciasTallas getVigenciaTallaSeleccionada() {
        return vigenciaTallaSeleccionada;
    }

    public void setVigenciaTallaSeleccionada(VigenciasTallas vigenciaTallaSeleccionada) {
        this.vigenciaTallaSeleccionada = vigenciaTallaSeleccionada;
    }

}
