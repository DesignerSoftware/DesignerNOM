/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.SucursalesPila;
import Entidades.Empresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarSucursalesPilaInterface;
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
public class ControlSucursalesPila implements Serializable {

    @EJB
    AdministrarSucursalesPilaInterface administrarSucursalesPila;
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
    private List<SucursalesPila> listSucursalesPilaPorEmpresa;
    private List<SucursalesPila> listSucursalesPilaPorEmpresaBoton;
    private List<SucursalesPila> filtrarSucursalesPila;
    private List<SucursalesPila> crearSucursalesPila;
    private List<SucursalesPila> modificarSucursalesPila;
    private List<SucursalesPila> borrarSucursalesPila;
    private SucursalesPila nuevoCentroCosto;
    private SucursalesPila duplicarCentroCosto;
    private SucursalesPila editarCentroCosto;
    private SucursalesPila sucursalPilaSeleccionada;

    private Column codigoCC, nombreCentroCosto;

    //AUTOCOMPLETAR
    private List<SucursalesPila> filterSucursalesPilaPorEmpresa;
    private Empresas backUpEmpresaActual;

    private SucursalesPila SucursalesPilaPorEmpresaSeleccionado;
    private boolean banderaSeleccionSucursalesPilaPorEmpresa;
    private int tamano;
    private String backupCodigo;
    private String backupDescripcion;
    private String infoRegistro;
    private String infoRegistroBoton;

    public ControlSucursalesPila() {
        permitirIndex = true;
        listaEmpresas = null;
        empresaSeleccionada = null;
        indiceEmpresaMostrada = 0;
        listSucursalesPilaPorEmpresa = null;
        listSucursalesPilaPorEmpresaBoton = null;
        crearSucursalesPila = new ArrayList<SucursalesPila>();
        modificarSucursalesPila = new ArrayList<SucursalesPila>();
        borrarSucursalesPila = new ArrayList<SucursalesPila>();
        editarCentroCosto = new SucursalesPila();
        nuevoCentroCosto = new SucursalesPila();
        duplicarCentroCosto = new SucursalesPila();
        aceptar = true;
        filtradoListaEmpresas = null;
        guardado = true;
        banderaSeleccionSucursalesPilaPorEmpresa = false;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSucursalesPila.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A CONTROLSUCURSALESPILA.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarSucursalesPila.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR CONTROLSUCURSALESPILA eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("BETA CENTRO COSTO TIPO LISTA = " + tipoLista);
        System.err.println("PERMITIR INDEX = " + permitirIndex);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            System.err.println("CAMBIAR INDICE CUALCELDA = " + cualCelda);
            secRegistro = listSucursalesPilaPorEmpresa.get(index).getSecuencia();
            System.err.println("Sec Registro = " + secRegistro);
            if (tipoLista == 0) {
                backupCodigo = listSucursalesPilaPorEmpresa.get(index).getCodigo();
                backupDescripcion = listSucursalesPilaPorEmpresa.get(index).getDescripcion();
            } else if (tipoLista == 1) {
                backupCodigo = listSucursalesPilaPorEmpresa.get(index).getCodigo();
                backupDescripcion = listSucursalesPilaPorEmpresa.get(index).getDescripcion();
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void modificandoCentroCosto(int indice, String confirmarCambio, String valorConfirmar) {

        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        boolean banderita1 = false;

        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearSucursalesPila.contains(listSucursalesPilaPorEmpresa.get(indice))) {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listSucursalesPilaPorEmpresa.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSucursalesPilaPorEmpresa.size(); j++) {
                            if (j != indice) {
                                if (listSucursalesPilaPorEmpresa.get(indice).getCodigo().equals(listSucursalesPilaPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listSucursalesPilaPorEmpresa.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSucursalesPilaPorEmpresa.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSucursalesPilaPorEmpresa.get(indice).setDescripcion(backupDescripcion);
                    } else if (listSucursalesPilaPorEmpresa.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSucursalesPilaPorEmpresa.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarSucursalesPila.isEmpty()) {
                            modificarSucursalesPila.add(listSucursalesPilaPorEmpresa.get(indice));
                        } else if (!modificarSucursalesPila.contains(listSucursalesPilaPorEmpresa.get(indice))) {
                            modificarSucursalesPila.add(listSucursalesPilaPorEmpresa.get(indice));
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
                    RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listSucursalesPilaPorEmpresa.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSucursalesPilaPorEmpresa.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listSucursalesPilaPorEmpresa.size(); j++) {
                            if (j != indice) {
                                if (listSucursalesPilaPorEmpresa.get(indice).getCodigo().equals(listSucursalesPilaPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listSucursalesPilaPorEmpresa.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSucursalesPilaPorEmpresa.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSucursalesPilaPorEmpresa.get(indice).setDescripcion(backupDescripcion);
                    } else if (listSucursalesPilaPorEmpresa.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSucursalesPilaPorEmpresa.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (guardado == true) {
                            guardado = false;
                        }
                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                    RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            } else {

                if (!crearSucursalesPila.contains(filtrarSucursalesPila.get(indice))) {
                    if (filtrarSucursalesPila.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarSucursalesPila.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarSucursalesPila.size(); j++) {
                            if (j != indice) {
                                if (filtrarSucursalesPila.get(indice).getCodigo().equals(listSucursalesPilaPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listSucursalesPilaPorEmpresa.size(); j++) {
                            if (j != indice) {
                                if (filtrarSucursalesPila.get(indice).getCodigo().equals(listSucursalesPilaPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarSucursalesPila.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarSucursalesPila.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSucursalesPila.get(indice).setDescripcion(backupDescripcion);
                    }
                    if (filtrarSucursalesPila.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSucursalesPila.get(indice).setDescripcion(backupDescripcion);
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarSucursalesPila.isEmpty()) {
                            modificarSucursalesPila.add(filtrarSucursalesPila.get(indice));
                        } else if (!modificarSucursalesPila.contains(filtrarSucursalesPila.get(indice))) {
                            modificarSucursalesPila.add(filtrarSucursalesPila.get(indice));
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
                    if (filtrarSucursalesPila.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarSucursalesPila.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarSucursalesPila.size(); j++) {
                            if (j != indice) {
                                if (filtrarSucursalesPila.get(indice).getCodigo().equals(listSucursalesPilaPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listSucursalesPilaPorEmpresa.size(); j++) {
                            if (j != indice) {
                                if (filtrarSucursalesPila.get(indice).getCodigo().equals(listSucursalesPilaPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarSucursalesPila.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarSucursalesPila.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSucursalesPila.get(indice).setDescripcion(backupDescripcion);
                    }
                    if (filtrarSucursalesPila.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSucursalesPila.get(indice).setDescripcion(backupDescripcion);
                    }

                    if (banderita == true && banderita1 == true) {
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
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void cancelarModificacion() {
        try {
            System.out.println("entre a CONTROLSUCURSALESPILA.cancelarModificacion");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                //0
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                //2

                bandera = 0;
                filtrarSucursalesPila = null;
                tipoLista = 0;
            }

            borrarSucursalesPila.clear();
            crearSucursalesPila.clear();
            modificarSucursalesPila.clear();
            index = -1;
            k = 0;
            listSucursalesPilaPorEmpresa = null;
            guardado = true;
            aceptar = true;
            permitirIndex = true;
            getListSucursalesPilaPorEmpresa();
            RequestContext context = RequestContext.getCurrentInstance();
            if (listSucursalesPilaPorEmpresa == null || listSucursalesPilaPorEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listSucursalesPilaPorEmpresa.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            banderaModificacionEmpresa = 0;
            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
            }
            RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");

        } catch (Exception E) {
            System.out.println("ERROR CONTROLSUCURSALESPILA.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void salir() {
        try {
            System.out.println("entre a CONTROLSUCURSALESPILA.cancelarModificacion");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                //0
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                //2

                bandera = 0;
                filtrarSucursalesPila = null;
                tipoLista = 0;
            }

            borrarSucursalesPila.clear();
            crearSucursalesPila.clear();
            modificarSucursalesPila.clear();
            index = -1;
            k = 0;
            listSucursalesPilaPorEmpresa = null;
            guardado = true;
            permitirIndex = true;
            getListSucursalesPilaPorEmpresa();
            RequestContext context = RequestContext.getCurrentInstance();
            if (listSucursalesPilaPorEmpresa == null || listSucursalesPilaPorEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listSucursalesPilaPorEmpresa.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            banderaModificacionEmpresa = 0;
            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
            }
            RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception E) {
            System.out.println("ERROR CONTROLSUCURSALESPILA.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A CONTROLSUCURSALESPILA.asignarIndex \n");
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

        } catch (Exception e) {
            System.out.println("ERROR CONTROLSUCURSALESPILA.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void llamadoDialogoBuscarSucursalesPila() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                banderaSeleccionSucursalesPilaPorEmpresa = true;
                PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");

            } else {
                listSucursalesPilaPorEmpresaBoton = null;
                getListSucursalesPilaPorEmpresaBoton();
                index = -1;
                RequestContext.getCurrentInstance().update("formularioDialogos:lovSucursalesPila");
                PrimefacesContextUI.ejecutar("PF('buscarSucursalesPilaDialogo').show()");

            }
        } catch (Exception e) {
            System.err.println("ERROR LLAMADO DIALOGO BUSCAR CENTROS COSTOS " + e);
        }

    }

    public void cancelarSeleccionCentroCostoPorEmpresa() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            SucursalesPilaPorEmpresaSeleccionado = null;
            filterSucursalesPilaPorEmpresa = null;
            aceptar = true;
            index = -1;
            tipoActualizacion = -1;
            RequestContext.getCurrentInstance().update("form:ACEPTARNCC");

        } catch (Exception e) {
            System.out.println("ERROR CONTROLSUCURSALESPILA.cancelarSeleccionVigencia ERROR====" + e.getMessage());
        }
    }

    public void limpiarNuevoSucursalesPila() {
        System.out.println("\n ENTRE A CONTROLSUCURSALESPILA.limpiarNuevoSucursalesPila \n");
        try {
            nuevoCentroCosto = new SucursalesPila();
            index = -1;
        } catch (Exception e) {
            System.out.println("Error CONTROLSUCURSALESPILA.LimpiarNuevoSucursalesPila ERROR=============================" + e.getMessage());
        }
    }

    public void agregarNuevoSucursalesPila() {
        System.out.println("\n ENTRE A CONTROLSUCURSALESPILA.agregarNuevoSucursalesPila \n");
        try {
            int contador = 0;
            mensajeValidacion = " ";
            int duplicados = 0;
            RequestContext context = RequestContext.getCurrentInstance();

            banderaModificacionEmpresa = 1;
            if (nuevoCentroCosto.getCodigo() == null) {
                mensajeValidacion = mensajeValidacion + "   *Codigo \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);

            } else if (nuevoCentroCosto.getCodigo().isEmpty()) {
                mensajeValidacion = mensajeValidacion + "   *Codigo \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);

            } else {
                System.out.println("codigo en Motivo Cambio Cargo: " + nuevoCentroCosto.getCodigo());

                for (int x = 0; x < listSucursalesPilaPorEmpresa.size(); x++) {
                    if (listSucursalesPilaPorEmpresa.get(x).getCodigo().equals(nuevoCentroCosto.getCodigo())) {
                        duplicados++;
                    }
                }
                System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

                if (duplicados > 0) {
                    mensajeValidacion = " *Que NO hayan codigos repetidos \n";
                    System.out.println("Mensaje validacion : " + mensajeValidacion);
                } else {
                    System.out.println("bandera");
                    contador++;
                }
            }
            if (nuevoCentroCosto.getDescripcion() == null) {
                mensajeValidacion = mensajeValidacion + "   *Descripción \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);

            } else if (nuevoCentroCosto.getDescripcion().isEmpty()) {
                mensajeValidacion = mensajeValidacion + "   *Descripción \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);

            } else {
                System.out.println("Bandera : ");
                contador++;
            }
            if (contador == 2) {
                k++;
                l = BigInteger.valueOf(k);
                nuevoCentroCosto.setSecuencia(l);

                nuevoCentroCosto.setEmpresa(empresaSeleccionada);
                if (crearSucursalesPila.contains(nuevoCentroCosto)) {
                    System.out.println("Ya lo contengo.");
                } else {
                    crearSucursalesPila.add(nuevoCentroCosto);

                }
                listSucursalesPilaPorEmpresa.add(nuevoCentroCosto);
                RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
                nuevoCentroCosto = new SucursalesPila();
                // index = -1;

                if (listSucursalesPilaPorEmpresa == null || listSucursalesPilaPorEmpresa.isEmpty()) {
                    infoRegistro = "Cantidad de registros: 0 ";
                } else {
                    infoRegistro = "Cantidad de registros: " + listSucursalesPilaPorEmpresa.size();
                }
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
                secRegistro = null;
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    codigoCC = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoCC");
                    codigoCC.setFilterStyle("display: none; visibility: hidden;");
                    nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreCentroCosto");
                    nombreCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosSucursalesPila");

                    bandera = 0;
                    filtrarSucursalesPila = null;
                    tipoLista = 0;
                }
                mensajeValidacion = " ";
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroSucursalesPila').hide()");

            } else {
                contador = 0;
                RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
                PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLSUCURSALESPILA.agregarNuevoSucursalesPila ERROR===========================" + e.getMessage());
        }
    }

    public void cargarTiposSucursalesPilaNuevoRegistro(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tiposSucursalesPilaDialogo");
            PrimefacesContextUI.ejecutar("PF('tiposSucursalesPilaDialogo').show()");
        } else if (tipoNuevo == 1) {
            tipoActualizacion = 2;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tiposSucursalesPilaDialogo");
            PrimefacesContextUI.ejecutar("PF('tiposSucursalesPilaDialogo').show()");
        }
    }

    public void mostrarDialogoNuevoTiposSucursalesPila() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevoCentroCosto = new SucursalesPila();
        index = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoSucursalesPila");
        PrimefacesContextUI.ejecutar("PF('NuevoRegistroSucursalesPila').show()");
    }

    public void mostrarDialogoListaEmpresas() {
        RequestContext context = RequestContext.getCurrentInstance();
        index = -1;
        PrimefacesContextUI.ejecutar("PF('buscarSucursalesPilaDialogo').show()");
    }

    public void duplicandoSucursalesPila() {
        try {
            banderaModificacionEmpresa = 1;
            System.out.println("\n ENTRE A CONTROLSUCURSALESPILA.duplicarSucursalesPila INDEX===" + index);

            if (index >= 0) {
                System.out.println("\n ENTRE A CONTROLSUCURSALESPILA.duplicarSucursalesPila TIPOLISTA===" + tipoLista);

                duplicarCentroCosto = new SucursalesPila();
                k++;
                l = BigInteger.valueOf(k);
                if (tipoLista == 0) {
                    duplicarCentroCosto.setSecuencia(l);
                    duplicarCentroCosto.setEmpresa(listSucursalesPilaPorEmpresa.get(index).getEmpresa());
                    duplicarCentroCosto.setCodigo(listSucursalesPilaPorEmpresa.get(index).getCodigo());
                    duplicarCentroCosto.setDescripcion(listSucursalesPilaPorEmpresa.get(index).getDescripcion());

                }
                if (tipoLista == 1) {

                    duplicarCentroCosto.setSecuencia(l);
                    duplicarCentroCosto.setEmpresa(filtrarSucursalesPila.get(index).getEmpresa());
                    duplicarCentroCosto.setCodigo(filtrarSucursalesPila.get(index).getCodigo());
                    duplicarCentroCosto.setDescripcion(filtrarSucursalesPila.get(index).getDescripcion());

                }

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSucursalesPila");
                PrimefacesContextUI.ejecutar("PF('DuplicarRegistroSucursalesPila').show()");
                index = -1;
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLSUCURSALESPILA.DuplicarSucursalesPila ERROR===============" + e.getMessage());
        }
    }

    public void limpiarDuplicarSucursalesPila() {
        System.out.println("\n ENTRE A CONTROLSUCURSALESPILA.limpiarDuplicarSucursalesPila \n");
        try {
            duplicarCentroCosto = new SucursalesPila();
        } catch (Exception e) {
            System.out.println("ERROR CONTROLSUCURSALESPILA.limpiarDuplicarSucursalesPila ERROR========" + e.getMessage());
        }

    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLTIPOSSUCURSALESPILA");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;

        if (duplicarCentroCosto.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarCentroCosto.getCodigo().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";

        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + duplicarCentroCosto.getCodigo());

            for (int x = 0; x < listSucursalesPilaPorEmpresa.size(); x++) {
                if (listSucursalesPilaPorEmpresa.get(x).getCodigo().equals(duplicarCentroCosto.getCodigo())) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO hayan codigos repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }

        }
        if (duplicarCentroCosto.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarCentroCosto.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Descripción \n";

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarCentroCosto.setSecuencia(l);

            duplicarCentroCosto.setEmpresa(empresaSeleccionada);

            if (crearSucursalesPila.contains(duplicarCentroCosto)) {
                System.out.println("Ya lo contengo.");
            } else {
                listSucursalesPilaPorEmpresa.add(duplicarCentroCosto);
            }
            crearSucursalesPila.add(duplicarCentroCosto);
            RequestContext.getCurrentInstance().update("form:datosSucursalesPila");

            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (listSucursalesPilaPorEmpresa == null || listSucursalesPilaPorEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listSucursalesPilaPorEmpresa.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
                bandera = 0;
                filtrarSucursalesPila = null;
                tipoLista = 0;
            }
            duplicarCentroCosto = new SucursalesPila();
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroSucursalesPila').hide()");
            mensajeValidacion = " ";
            banderaModificacionEmpresa = 1;

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    private BigInteger contarNovedadesAutoLiquidacionesSucursal_Pila;
    private BigInteger contarNovedadesCorreccionesAutolSucursal_Pila;
    private BigInteger contarOdisCabecerasSucursal_Pila;
    private BigInteger contarOdiscorReaccionesCabSucursal_Pila;
    private BigInteger contarParametrosInformesSucursal_Pila;
    private BigInteger contarUbicacionesGeograficasSucursal_Pila;

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        System.out.println("TIPOLISTA = " + tipoLista);
        BigInteger pruebilla;
        try {
            if (tipoLista == 0) {
                contarNovedadesAutoLiquidacionesSucursal_Pila = administrarSucursalesPila.contarNovedadesAutoLiquidacionesSucursal_Pila(listSucursalesPilaPorEmpresa.get(index).getSecuencia());
                contarNovedadesCorreccionesAutolSucursal_Pila = administrarSucursalesPila.contarNovedadesCorreccionesAutolSucursal_Pila(listSucursalesPilaPorEmpresa.get(index).getSecuencia());;
                contarOdisCabecerasSucursal_Pila = administrarSucursalesPila.contarOdisCabecerasSucursal_Pila(listSucursalesPilaPorEmpresa.get(index).getSecuencia());
                contarOdiscorReaccionesCabSucursal_Pila = administrarSucursalesPila.contarOdiscorReaccionesCabSucursal_Pila(listSucursalesPilaPorEmpresa.get(index).getSecuencia());
                contarParametrosInformesSucursal_Pila = administrarSucursalesPila.contarParametrosInformesSucursal_Pila(listSucursalesPilaPorEmpresa.get(index).getSecuencia());
                contarUbicacionesGeograficasSucursal_Pila = administrarSucursalesPila.contarUbicacionesGeograficasSucursal_Pila(listSucursalesPilaPorEmpresa.get(index).getSecuencia());
            } else {
                contarNovedadesAutoLiquidacionesSucursal_Pila = administrarSucursalesPila.contarNovedadesAutoLiquidacionesSucursal_Pila(filtrarSucursalesPila.get(index).getSecuencia());
                contarNovedadesCorreccionesAutolSucursal_Pila = administrarSucursalesPila.contarNovedadesCorreccionesAutolSucursal_Pila(filtrarSucursalesPila.get(index).getSecuencia());;
                contarOdisCabecerasSucursal_Pila = administrarSucursalesPila.contarOdisCabecerasSucursal_Pila(filtrarSucursalesPila.get(index).getSecuencia());
                contarOdiscorReaccionesCabSucursal_Pila = administrarSucursalesPila.contarOdiscorReaccionesCabSucursal_Pila(filtrarSucursalesPila.get(index).getSecuencia());
                contarParametrosInformesSucursal_Pila = administrarSucursalesPila.contarParametrosInformesSucursal_Pila(filtrarSucursalesPila.get(index).getSecuencia());
                contarUbicacionesGeograficasSucursal_Pila = administrarSucursalesPila.contarUbicacionesGeograficasSucursal_Pila(filtrarSucursalesPila.get(index).getSecuencia());
            }
            if (contarNovedadesAutoLiquidacionesSucursal_Pila.equals(new BigInteger("0"))
                    && contarNovedadesCorreccionesAutolSucursal_Pila.equals(new BigInteger("0"))
                    && contarOdisCabecerasSucursal_Pila.equals(new BigInteger("0"))
                    && contarOdiscorReaccionesCabSucursal_Pila.equals(new BigInteger("0"))
                    && contarParametrosInformesSucursal_Pila.equals(new BigInteger("0"))
                    && contarUbicacionesGeograficasSucursal_Pila.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoCentroCosto();
            } else {

                System.out.println("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
                contarNovedadesAutoLiquidacionesSucursal_Pila = new BigInteger("-1");
                contarNovedadesCorreccionesAutolSucursal_Pila = new BigInteger("-1");
                contarOdisCabecerasSucursal_Pila = new BigInteger("-1");
                contarOdiscorReaccionesCabSucursal_Pila = new BigInteger("-1");
                contarParametrosInformesSucursal_Pila = new BigInteger("-1");
                contarUbicacionesGeograficasSucursal_Pila = new BigInteger("-1");
            }
        } catch (Exception e) {
            System.err.println("ERROR CONTROL BETA CENTROS COSTOS verificarBorrado ERROR " + e);
        }
    }

    public void borrandoCentroCosto() {
        try {
            banderaModificacionEmpresa = 1;
            if (index >= 0) {
                if (tipoLista == 0) {
                    if (!modificarSucursalesPila.isEmpty() && modificarSucursalesPila.contains(listSucursalesPilaPorEmpresa.get(index))) {
                        int modIndex = modificarSucursalesPila.indexOf(listSucursalesPilaPorEmpresa.get(index));
                        modificarSucursalesPila.remove(modIndex);
                        borrarSucursalesPila.add(listSucursalesPilaPorEmpresa.get(index));
                    } else if (!crearSucursalesPila.isEmpty() && crearSucursalesPila.contains(listSucursalesPilaPorEmpresa.get(index))) {
                        int crearIndex = crearSucursalesPila.indexOf(listSucursalesPilaPorEmpresa.get(index));
                        crearSucursalesPila.remove(crearIndex);
                    } else {

                        borrarSucursalesPila.add(listSucursalesPilaPorEmpresa.get(index));
                    }
                    listSucursalesPilaPorEmpresa.remove(index);
                }
                if (tipoLista == 1) {
                    if (!modificarSucursalesPila.isEmpty() && modificarSucursalesPila.contains(filtrarSucursalesPila.get(index))) {
                        System.out.println("\n 6 ENTRE A CONTROLSUCURSALESPILA.borrarCentroCosto tipolista==1 try if if if filtrarSucursalesPila.get(index).getCodigo()" + filtrarSucursalesPila.get(index).getCodigo());

                        int modIndex = modificarSucursalesPila.indexOf(filtrarSucursalesPila.get(index));
                        modificarSucursalesPila.remove(modIndex);
                        borrarSucursalesPila.add(filtrarSucursalesPila.get(index));
                    } else if (!crearSucursalesPila.isEmpty() && crearSucursalesPila.contains(filtrarSucursalesPila.get(index))) {
                        System.out.println("\n 7 ENTRE A CONTROLSUCURSALESPILA.borrarCentroCosto tipolista==1 try if if if filtrarSucursalesPila.get(index).getCodigo()" + filtrarSucursalesPila.get(index).getCodigo());
                        int crearIndex = crearSucursalesPila.indexOf(filtrarSucursalesPila.get(index));
                        crearSucursalesPila.remove(crearIndex);
                    } else {
                        System.out.println("\n 8 ENTRE A CONTROLSUCURSALESPILA.borrarCentroCosto tipolista==1 try if if if filtrarSucursalesPila.get(index).getCodigo()" + filtrarSucursalesPila.get(index).getCodigo());
                        borrarSucursalesPila.add(filtrarSucursalesPila.get(index));
                    }
                    int VCIndex = listSucursalesPilaPorEmpresa.indexOf(filtrarSucursalesPila.get(index));
                    listSucursalesPilaPorEmpresa.remove(VCIndex);
                    filtrarSucursalesPila.remove(index);
                }

                RequestContext context = RequestContext.getCurrentInstance();
                index = -1;
                System.err.println("verificar Borrado " + guardado);
                if (guardado == true) {
                    guardado = false;
                }
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                if (listSucursalesPilaPorEmpresa == null || listSucursalesPilaPorEmpresa.isEmpty()) {
                    infoRegistro = "Cantidad de registros: 0 ";
                } else {
                    infoRegistro = "Cantidad de registros: " + listSucursalesPilaPorEmpresa.size();
                }
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
                RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLSUCURSALESPILA.BorrarCentroCosto ERROR=====================" + e.getMessage());
        }
    }

    public void guardarCambiosCentroCosto() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Localizacion");
            if (!borrarSucursalesPila.isEmpty()) {
                administrarSucursalesPila.borrarSucursalesPila(borrarSucursalesPila);
                //mostrarBorrados
                registrosBorrados = borrarSucursalesPila.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarSucursalesPila.clear();
            }
            if (!crearSucursalesPila.isEmpty()) {
                administrarSucursalesPila.crearSucursalesPila(crearSucursalesPila);
                crearSucursalesPila.clear();
            }
            if (!modificarSucursalesPila.isEmpty()) {
                administrarSucursalesPila.modificarSucursalesPila(modificarSucursalesPila);
                modificarSucursalesPila.clear();
            }
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            System.out.println("Se guardaron los datos con exito");
            listSucursalesPilaPorEmpresa = null;
            RequestContext.getCurrentInstance().update(":form:datosSucursalesPila");
            k = 0;
            guardado = true;
            aceptar = true;
            RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");

            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
                banderaModificacionEmpresa = 1;

            }
            if (banderaSeleccionSucursalesPilaPorEmpresa == true) {
                listSucursalesPilaPorEmpresaBoton = null;
                getListSucursalesPilaPorEmpresaBoton();
                index = -1;
                RequestContext.getCurrentInstance().update("formularioDialogos:lovSucursalesPila");
                PrimefacesContextUI.ejecutar("PF('buscarSucursalesPilaDialogo').show()");
                banderaSeleccionSucursalesPilaPorEmpresa = false;
            }
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        banderaModificacionEmpresa = 0;
    }

    public void cancelarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (banderaModificacionEmpresa == 0) {
            empresaSeleccionada = backUpEmpresaActual;
            RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
            banderaModificacionEmpresa = 1;
        }

    }

    public void activarCtrlF11() {
        System.out.println("\n ENTRE A CONTROLSUCURSALESPILA.activarCtrlF11 \n");

        try {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 0) {
                tamano = 250;
                System.out.println("Activar");
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoCC");
                codigoCC.setFilterStyle("width: 85%;");
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("width: 85%;");
                RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
                bandera = 1;
            } else if (bandera == 1) {
                System.out.println("Desactivar");
                //
                tamano = 270;
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosSucursalesPila:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSucursalesPila");
                bandera = 0;
                filtrarSucursalesPila = null;
                tipoLista = 0;
            }
        } catch (Exception e) {

            System.out.println("ERROR CONTROLSUCURSALESPILA.activarCtrlF11 ERROR====================" + e.getMessage());
        }
    }

    public void editarCelda() {
        try {
            System.out.println("\n ENTRE A editarCelda INDEX  " + index);
            if (index >= 0) {
                System.out.println("\n ENTRE AeditarCelda TIPOLISTA " + tipoLista);
                if (tipoLista == 0) {
                    editarCentroCosto = listSucursalesPilaPorEmpresa.get(index);
                }
                if (tipoLista == 1) {
                    editarCentroCosto = filtrarSucursalesPila.get(index);
                }
                RequestContext context = RequestContext.getCurrentInstance();
                System.out.println("CONTROLSUCURSALESPILA: Entro a editar... valor celda: " + cualCelda);
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCCC");
                    PrimefacesContextUI.ejecutar("PF('editarCCC').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNCC");
                    PrimefacesContextUI.ejecutar("PF('editarNCC').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTCC");
                    PrimefacesContextUI.ejecutar("PF('editarTCC').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarMO");
                    PrimefacesContextUI.ejecutar("PF('editarMO').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCAT");
                    PrimefacesContextUI.ejecutar("PF('editarCAT').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarO");
                    PrimefacesContextUI.ejecutar("PF('editarO').show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCCTT");
                    PrimefacesContextUI.ejecutar("PF('editarCCTT').show()");
                    cualCelda = -1;
                } else if (cualCelda == 7) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarD");
                    PrimefacesContextUI.ejecutar("PF('editarD').show()");
                    cualCelda = -1;
                }
            }
            index = -1;
        } catch (Exception E) {
            System.out.println("ERROR CONTROLSUCURSALESPILA.editarCelDa ERROR=====================" + E.getMessage());
        }
    }

    public void listaValoresBoton() {

        try {
            if (index >= 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 2) {
                    System.out.println("\n ListaValoresBoton \n");
                    RequestContext.getCurrentInstance().update("formularioDialogos:tiposSucursalesPilaDialogo");
                    PrimefacesContextUI.ejecutar("PF('tiposSucursalesPilaDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        } catch (Exception e) {
            System.out.println("\n ERROR CONTROLSUCURSALESPILA.listaValoresBoton ERROR====================" + e.getMessage());

        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSucursalesPilaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SucursalesPila", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    /**
     *
     * @throws IOException
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSucursalesPilaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SucursalesPila", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listSucursalesPilaPorEmpresa.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "SUCURSALESPILA"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("SUCURSALESPILA")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    public void lovEmpresas() {
        index = -1;
        secRegistro = null;
        cualCelda = -1;
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').show()");
    }

    public void cambiarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("Cambiar empresa  GUARDADO = " + guardado);
        System.err.println("Cambiar empresa  GUARDADO = " + empresaSeleccionada.getNombre());
        if (guardado == true) {
            RequestContext.getCurrentInstance().update("form:nombreEmpresa");
            RequestContext.getCurrentInstance().update("form:nitEmpresa");
            getListSucursalesPilaPorEmpresa();
            filtradoListaEmpresas = null;
            listSucursalesPilaPorEmpresa = null;
            aceptar = true;
            context.reset("formularioDialogos:lovEmpresas:globalFilter");
            PrimefacesContextUI.ejecutar("PF('lovEmpresas').clearFilters()");
            PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').hide()");
            //RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
            backUpEmpresaActual = empresaSeleccionada;
            banderaModificacionEmpresa = 0;
            RequestContext.getCurrentInstance().update("form:datosSucursalesPila");

        } else {
            banderaModificacionEmpresa = 0;
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
    }

    public void cancelarCambioEmpresa() {
        filtradoListaEmpresas = null;
        banderaModificacionEmpresa = 0;
        index = -1;
        aceptar = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpresas').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpresasDialogo').hide()");

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
                listaEmpresas = administrarSucursalesPila.buscarEmpresas();
                if (!listaEmpresas.isEmpty()) {
                    empresaSeleccionada = listaEmpresas.get(0);
                    backUpEmpresaActual = empresaSeleccionada;
                }
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listaEmpresas == null || listaEmpresas.isEmpty()) {
                infoRegistroEmpresas = "Cantidad de registros: 0 ";
            } else {
                infoRegistroEmpresas = "Cantidad de registros: " + listaEmpresas.size();
            }
            RequestContext.getCurrentInstance().update("form:infoRegistroEmpresas");
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
        try {
            if (empresaSeleccionada == null) {
                getListaEmpresas();
                return empresaSeleccionada;
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLSUCURSALESPILA.getEmpresaSeleccionada ERROR " + e);
        } finally {
            return empresaSeleccionada;
        }
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public List<SucursalesPila> getListSucursalesPilaPorEmpresa() {
        try {
            if (empresaSeleccionada == null) {
                getEmpresaSeleccionada();
                if (listSucursalesPilaPorEmpresa == null) {
                    listSucursalesPilaPorEmpresa = administrarSucursalesPila.consultarSucursalPila(empresaSeleccionada.getSecuencia());
                } else {
                    System.out.println(".-.");
                }
            } else if (listSucursalesPilaPorEmpresa == null) {
                listSucursalesPilaPorEmpresa = administrarSucursalesPila.consultarSucursalPila(empresaSeleccionada.getSecuencia());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listSucursalesPilaPorEmpresa == null || listSucursalesPilaPorEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listSucursalesPilaPorEmpresa.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            return listSucursalesPilaPorEmpresa;
        } catch (Exception e) {
            System.out.println(" BETA  BETA ControlCentrosCosto: Error al recibir los SucursalesPila de la empresa seleccionada /n" + e.getMessage());
            return null;
        }
    }

    public List<SucursalesPila> getListSucursalesPilaPorEmpresaBoton() {
        try {
            if (listSucursalesPilaPorEmpresaBoton == null) {
                listSucursalesPilaPorEmpresaBoton = administrarSucursalesPila.consultarSucursalPila(empresaSeleccionada.getSecuencia());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listSucursalesPilaPorEmpresaBoton == null || listSucursalesPilaPorEmpresaBoton.isEmpty()) {
                infoRegistroBoton = "Cantidad de registros: 0 ";
            } else {
                infoRegistroBoton = "Cantidad de registros: " + listSucursalesPilaPorEmpresaBoton.size();
            }
            RequestContext.getCurrentInstance().update("form:infoRegistroBoton");
            return listSucursalesPilaPorEmpresaBoton;

        } catch (Exception e) {
            System.out.println("ControlCentrosCosto: Error al recibir los SucursalesPila de la empresa seleccionada /n" + e.getMessage());
            return null;
        }
    }

    public void setListSucursalesPilaPorEmpresaBoton(List<SucursalesPila> listSucursalesPilaPorEmpresaBoton) {
        this.listSucursalesPilaPorEmpresaBoton = listSucursalesPilaPorEmpresaBoton;
    }

    public void setListSucursalesPilaPorEmpresa(List<SucursalesPila> listSucursalesPilaPorEmpresa) {
        this.listSucursalesPilaPorEmpresa = listSucursalesPilaPorEmpresa;
    }

    public List<SucursalesPila> getFiltrarSucursalesPila() {
        return filtrarSucursalesPila;
    }

    public void setFiltrarSucursalesPila(List<SucursalesPila> filtrarSucursalesPila) {
        this.filtrarSucursalesPila = filtrarSucursalesPila;
    }

    public SucursalesPila getNuevoCentroCosto() {
        if (nuevoCentroCosto == null) {
            nuevoCentroCosto = new SucursalesPila();
        }
        return nuevoCentroCosto;
    }

    public void setNuevoCentroCosto(SucursalesPila nuevoCentroCosto) {
        this.nuevoCentroCosto = nuevoCentroCosto;
    }

    public SucursalesPila getDuplicarCentroCosto() {
        return duplicarCentroCosto;
    }

    public void setDuplicarCentroCosto(SucursalesPila duplicarCentroCosto) {
        this.duplicarCentroCosto = duplicarCentroCosto;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<SucursalesPila> getFilterSucursalesPilaPorEmpresa() {
        return filterSucursalesPilaPorEmpresa;
    }

    public void setFilterSucursalesPilaPorEmpresa(List<SucursalesPila> filterSucursalesPilaPorEmpresa) {
        this.filterSucursalesPilaPorEmpresa = filterSucursalesPilaPorEmpresa;
    }

    public SucursalesPila getSucursalesPilaPorEmpresaSeleccionado() {
        return SucursalesPilaPorEmpresaSeleccionado;
    }

    public void setSucursalesPilaPorEmpresaSeleccionado(SucursalesPila SucursalesPilaPorEmpresaSeleccionado) {
        this.SucursalesPilaPorEmpresaSeleccionado = SucursalesPilaPorEmpresaSeleccionado;
    }

    public SucursalesPila getEditarCentroCosto() {
        return editarCentroCosto;
    }

    public void setEditarCentroCosto(SucursalesPila editarCentroCosto) {
        this.editarCentroCosto = editarCentroCosto;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public SucursalesPila getSucursalPilaSeleccionada() {
        return sucursalPilaSeleccionada;
    }

    public void setSucursalPilaSeleccionada(SucursalesPila sucursalPilaSeleccionada) {
        this.sucursalPilaSeleccionada = sucursalPilaSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroBoton() {
        return infoRegistroBoton;
    }

    public void setInfoRegistroBoton(String infoRegistroBoton) {
        this.infoRegistroBoton = infoRegistroBoton;
    }

    public String getInfoRegistroEmpresas() {
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

}
