/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.SoCondicionesAmbientalesP;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSoCondicionesAmbientalesPInterface;
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
public class ControlSoCondicionesAmbientalesP implements Serializable {

    @EJB
    AdministrarSoCondicionesAmbientalesPInterface administrarSoCondicionesAmbientalesP;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP;
    private List<SoCondicionesAmbientalesP> filtrarSoCondicionesAmbientalesP;
    private List<SoCondicionesAmbientalesP> crearSoCondicionesAmbientalesP;
    private List<SoCondicionesAmbientalesP> modificarSoCondicionesAmbientalesP;
    private List<SoCondicionesAmbientalesP> borrarSoCondicionesAmbientalesP;
    private SoCondicionesAmbientalesP nuevaSoCondicionAmbientalP;
    private SoCondicionesAmbientalesP duplicarSoCondicionAmbientalP;
    private SoCondicionesAmbientalesP editarSoCondicionAmbientalP;
    private SoCondicionesAmbientalesP soCondicionAmbientalPSeleccionado;
    private BigInteger verificarBorradoAccidentes;
    private int tamano;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;

    private String backUpCodigo;
    private String backUpDescripcion;

    public ControlSoCondicionesAmbientalesP() {
        listSoCondicionesAmbientalesP = null;
        crearSoCondicionesAmbientalesP = new ArrayList<SoCondicionesAmbientalesP>();
        modificarSoCondicionesAmbientalesP = new ArrayList<SoCondicionesAmbientalesP>();
        borrarSoCondicionesAmbientalesP = new ArrayList<SoCondicionesAmbientalesP>();
        permitirIndex = true;
        editarSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
        nuevaSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
        duplicarSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSoCondicionesAmbientalesP.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n EVENTO FILTRAR \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarSoCondicionesAmbientalesP.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR EVENTO FILTRAR ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listSoCondicionesAmbientalesP.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarSoCondicionesAmbientalesP.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listSoCondicionesAmbientalesP.get(index).getDescripcion();
                } else {
                    backUpDescripcion = filtrarSoCondicionesAmbientalesP.get(index).getDescripcion();
                }
            }

            secRegistro = listSoCondicionesAmbientalesP.get(index).getSecuencia();

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE CONTROLSOCONDICIONESAMBIENTALESP  AsignarIndex \n");
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
            System.out.println("ERROR CONTROLSOCONDICIONESAMBIENTALESP asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }
    private String infoRegistro;

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoCondicionesAmbientalesP = null;
            tipoLista = 0;
        }

        borrarSoCondicionesAmbientalesP.clear();
        crearSoCondicionesAmbientalesP.clear();
        modificarSoCondicionesAmbientalesP.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSoCondicionesAmbientalesP = null;
        guardado = true;
        permitirIndex = true;
        getListSoCondicionesAmbientalesP();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoCondicionesAmbientalesP == null || listSoCondicionesAmbientalesP.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoCondicionesAmbientalesP.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoCondicionesAmbientalesP = null;
            tipoLista = 0;
        }

        borrarSoCondicionesAmbientalesP.clear();
        crearSoCondicionesAmbientalesP.clear();
        modificarSoCondicionesAmbientalesP.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSoCondicionesAmbientalesP = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoCondicionesAmbientalesP = null;
            tipoLista = 0;
        }
    }

    public void modificandoSoCondicionAmbientalP(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("MODIFICAR  SO CONDICIONES AMBIENTALES P");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Short a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("MODIFICANDO CLASE ACCIDENTE CONFIRMAR CAMBIO = N");
            if (tipoLista == 0) {
                if (!crearSoCondicionesAmbientalesP.contains(listSoCondicionesAmbientalesP.get(indice))) {
                    if (listSoCondicionesAmbientalesP.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                    } else if (listSoCondicionesAmbientalesP.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listSoCondicionesAmbientalesP.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSoCondicionesAmbientalesP.size(); j++) {
                            if (j != indice) {
                                if (listSoCondicionesAmbientalesP.get(indice).getCodigo().equals(listSoCondicionesAmbientalesP.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSoCondicionesAmbientalesP.get(indice).getDescripcion().isEmpty()) {
                        listSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (listSoCondicionesAmbientalesP.get(indice).getDescripcion().equals(" ")) {
                        listSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarSoCondicionesAmbientalesP.isEmpty()) {
                            modificarSoCondicionesAmbientalesP.add(listSoCondicionesAmbientalesP.get(indice));
                        } else if (!modificarSoCondicionesAmbientalesP.contains(listSoCondicionesAmbientalesP.get(indice))) {
                            modificarSoCondicionesAmbientalesP.add(listSoCondicionesAmbientalesP.get(indice));
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
                    if (listSoCondicionesAmbientalesP.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                    } else if (listSoCondicionesAmbientalesP.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listSoCondicionesAmbientalesP.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSoCondicionesAmbientalesP.size(); j++) {
                            if (j != indice) {
                                if (listSoCondicionesAmbientalesP.get(indice).getCodigo().equals(listSoCondicionesAmbientalesP.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSoCondicionesAmbientalesP.get(indice).getDescripcion().isEmpty()) {
                        listSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (listSoCondicionesAmbientalesP.get(indice).getDescripcion().equals(" ")) {
                        listSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
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

                if (!crearSoCondicionesAmbientalesP.contains(filtrarSoCondicionesAmbientalesP.get(indice))) {
                    if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                    }
                    if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSoCondicionesAmbientalesP.size(); j++) {
                            System.err.println("indice lista  indice : " + listSoCondicionesAmbientalesP.get(j).getCodigo());
                            if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(listSoCondicionesAmbientalesP.get(j).getCodigo())) {
                                contador++;
                            }
                        }

                        for (int j = 0; j < filtrarSoCondicionesAmbientalesP.size(); j++) {
                            System.err.println("indice filtrar indice : " + filtrarSoCondicionesAmbientalesP.get(j).getCodigo());
                            if (j == indice) {
                                if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(filtrarSoCondicionesAmbientalesP.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarSoCondicionesAmbientalesP.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        banderita = false;
                    }
                    if (filtrarSoCondicionesAmbientalesP.get(indice).getDescripcion().equals(" ")) {
                        filtrarSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarSoCondicionesAmbientalesP.isEmpty()) {
                            modificarSoCondicionesAmbientalesP.add(filtrarSoCondicionesAmbientalesP.get(indice));
                        } else if (!modificarSoCondicionesAmbientalesP.contains(filtrarSoCondicionesAmbientalesP.get(indice))) {
                            modificarSoCondicionesAmbientalesP.add(filtrarSoCondicionesAmbientalesP.get(indice));
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
                    if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                    }
                    if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSoCondicionesAmbientalesP.size(); j++) {
                            System.err.println("indice lista  indice : " + listSoCondicionesAmbientalesP.get(j).getCodigo());
                            if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(listSoCondicionesAmbientalesP.get(j).getCodigo())) {
                                contador++;
                            }
                        }

                        for (int j = 0; j < filtrarSoCondicionesAmbientalesP.size(); j++) {
                            System.err.println("indice filtrar indice : " + filtrarSoCondicionesAmbientalesP.get(j).getCodigo());
                            if (j == indice) {
                                if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(filtrarSoCondicionesAmbientalesP.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarSoCondicionesAmbientalesP.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        banderita = false;
                    }
                    if (filtrarSoCondicionesAmbientalesP.get(indice).getDescripcion().equals(" ")) {
                        filtrarSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
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
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoSoCondicionesAmbientalesP() {

        RequestContext context = RequestContext.getCurrentInstance();

        if (index >= 0) {

            if (tipoLista == 0) {
                System.out.println("borrandoSoCondicionesAmbientalesP");
                if (!modificarSoCondicionesAmbientalesP.isEmpty() && modificarSoCondicionesAmbientalesP.contains(listSoCondicionesAmbientalesP.get(index))) {
                    int modIndex = modificarSoCondicionesAmbientalesP.indexOf(listSoCondicionesAmbientalesP.get(index));
                    modificarSoCondicionesAmbientalesP.remove(modIndex);
                    borrarSoCondicionesAmbientalesP.add(listSoCondicionesAmbientalesP.get(index));
                } else if (!crearSoCondicionesAmbientalesP.isEmpty() && crearSoCondicionesAmbientalesP.contains(listSoCondicionesAmbientalesP.get(index))) {
                    int crearIndex = crearSoCondicionesAmbientalesP.indexOf(listSoCondicionesAmbientalesP.get(index));
                    crearSoCondicionesAmbientalesP.remove(crearIndex);
                } else {
                    borrarSoCondicionesAmbientalesP.add(listSoCondicionesAmbientalesP.get(index));
                }
                listSoCondicionesAmbientalesP.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoSoCondicionesAmbientalesP");
                if (!modificarSoCondicionesAmbientalesP.isEmpty() && modificarSoCondicionesAmbientalesP.contains(filtrarSoCondicionesAmbientalesP.get(index))) {
                    int modIndex = modificarSoCondicionesAmbientalesP.indexOf(filtrarSoCondicionesAmbientalesP.get(index));
                    modificarSoCondicionesAmbientalesP.remove(modIndex);
                    borrarSoCondicionesAmbientalesP.add(filtrarSoCondicionesAmbientalesP.get(index));
                } else if (!crearSoCondicionesAmbientalesP.isEmpty() && crearSoCondicionesAmbientalesP.contains(filtrarSoCondicionesAmbientalesP.get(index))) {
                    int crearIndex = crearSoCondicionesAmbientalesP.indexOf(filtrarSoCondicionesAmbientalesP.get(index));
                    crearSoCondicionesAmbientalesP.remove(crearIndex);
                } else {
                    borrarSoCondicionesAmbientalesP.add(filtrarSoCondicionesAmbientalesP.get(index));
                }
                int VCIndex = listSoCondicionesAmbientalesP.indexOf(filtrarSoCondicionesAmbientalesP.get(index));
                listSoCondicionesAmbientalesP.remove(VCIndex);
                filtrarSoCondicionesAmbientalesP.remove(index);

            }
            infoRegistro = "Cantidad de registros: " + listSoCondicionesAmbientalesP.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("verificarBorrado");
        try {
            if (tipoLista == 0) {
                verificarBorradoAccidentes = administrarSoCondicionesAmbientalesP.verificarSoAccidentesMedicos(listSoCondicionesAmbientalesP.get(index).getSecuencia());
            } else {
                verificarBorradoAccidentes = administrarSoCondicionesAmbientalesP.verificarSoAccidentesMedicos(filtrarSoCondicionesAmbientalesP.get(index).getSecuencia());
            }
            if (verificarBorradoAccidentes.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoSoCondicionesAmbientalesP();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
                verificarBorradoAccidentes = new BigInteger("-1");
            }
        } catch (Exception e) {
            System.err.println("ERROR CLASES ACCIDENTES verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarSoCondicionesAmbientalesP.isEmpty() || !crearSoCondicionesAmbientalesP.isEmpty() || !modificarSoCondicionesAmbientalesP.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardandoSoCondicionesAmbientalesP() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("REALIZANDO SO CONDICIONES AMBIENTALES P");
            if (!borrarSoCondicionesAmbientalesP.isEmpty()) {
                administrarSoCondicionesAmbientalesP.borrarSoCondicionesAmbientalesP(borrarSoCondicionesAmbientalesP);
                //mostrarBorrados
                registrosBorrados = borrarSoCondicionesAmbientalesP.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarSoCondicionesAmbientalesP.clear();
            }
            if (!crearSoCondicionesAmbientalesP.isEmpty()) {
                administrarSoCondicionesAmbientalesP.crearSoCondicionesAmbientalesP(crearSoCondicionesAmbientalesP);
                crearSoCondicionesAmbientalesP.clear();
            }
            if (!modificarSoCondicionesAmbientalesP.isEmpty()) {
                administrarSoCondicionesAmbientalesP.modificarSoCondicionesAmbientalesP(modificarSoCondicionesAmbientalesP);
                modificarSoCondicionesAmbientalesP.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listSoCondicionesAmbientalesP = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            k = 0;
            if (guardado == false) {
                guardado = true;
            }
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarSoCondicionAmbientalP = listSoCondicionesAmbientalesP.get(index);
            }
            if (tipoLista == 1) {
                editarSoCondicionAmbientalP = filtrarSoCondicionesAmbientalesP.get(index);
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
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevaSoCondicionAmbiental() {
        System.out.println("agregarNuevaSoCondicionAmbiental");
        int contador = 0;
        int duplicados = 0;

        Short a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaSoCondicionAmbientalP.getCodigo() == null) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevaSoCondicionAmbientalP.getCodigo());

            for (int x = 0; x < listSoCondicionesAmbientalesP.size(); x++) {
                if (listSoCondicionesAmbientalesP.get(x).getCodigo().equals(nuevaSoCondicionAmbientalP.getCodigo())) {
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
        if (nuevaSoCondicionAmbientalP.getDescripcion() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
                bandera = 0;
                filtrarSoCondicionesAmbientalesP = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevaSoCondicionAmbientalP.setSecuencia(l);

            crearSoCondicionesAmbientalesP.add(nuevaSoCondicionAmbientalP);

            listSoCondicionesAmbientalesP.add(nuevaSoCondicionAmbientalP);
            nuevaSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            infoRegistro = "Cantidad de registros: " + listSoCondicionesAmbientalesP.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroSoCondicionesAmbientalesPeligrosaP').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoSoCondicionesAmbientalesP() {
        System.out.println("limpiarSoCondicionesAmbientalesP");
        nuevaSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoSoCondicionAmbientalP() {
        System.out.println("duplicandoSoCondicionAmbientalP");
        if (index >= 0) {
            duplicarSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarSoCondicionAmbientalP.setSecuencia(l);
                duplicarSoCondicionAmbientalP.setCodigo(listSoCondicionesAmbientalesP.get(index).getCodigo());
                duplicarSoCondicionAmbientalP.setDescripcion(listSoCondicionesAmbientalesP.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarSoCondicionAmbientalP.setSecuencia(l);
                duplicarSoCondicionAmbientalP.setCodigo(filtrarSoCondicionesAmbientalesP.get(index).getCodigo());
                duplicarSoCondicionAmbientalP.setDescripcion(filtrarSoCondicionesAmbientalesP.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRCAP");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroSoCondicionesAmbientalesP').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("CONFIRMAR DUPLICAR SO CONDICIONES AMBIENTALES P");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarSoCondicionAmbientalP.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarSoCondicionAmbientalP.getDescripcion());

        if (duplicarSoCondicionAmbientalP.getCodigo() == null || duplicarSoCondicionAmbientalP.getCodigo().equals(" ") || duplicarSoCondicionAmbientalP.getCodigo().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            for (int x = 0; x < listSoCondicionesAmbientalesP.size(); x++) {
                if (listSoCondicionesAmbientalesP.get(x).getCodigo().equals(duplicarSoCondicionAmbientalP.getCodigo())) {
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
        if (duplicarSoCondicionAmbientalP.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarSoCondicionAmbientalP.getSecuencia() + "  " + duplicarSoCondicionAmbientalP.getCodigo());
            if (crearSoCondicionesAmbientalesP.contains(duplicarSoCondicionAmbientalP)) {
                System.out.println("Ya lo contengo.");
            }
            listSoCondicionesAmbientalesP.add(duplicarSoCondicionAmbientalP);
            crearSoCondicionesAmbientalesP.add(duplicarSoCondicionAmbientalP);
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            infoRegistro = "Cantidad de registros: " + listSoCondicionesAmbientalesP.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
                bandera = 0;
                filtrarSoCondicionesAmbientalesP = null;
                tipoLista = 0;
            }
            duplicarSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroSoCondicionesAmbientalesP').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarSoCondicionesAmbientalesP() {
        duplicarSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoCondicionesAmbientalesPExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "CONDICIONESAMBIENTALES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoCondicionesAmbientalesPExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "CONDICIONESAMBIENTALES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listSoCondicionesAmbientalesP.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "SOCONDICIONESAMBIENTALESP"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("SOCONDICIONESAMBIENTALESP")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<SoCondicionesAmbientalesP> getListSoCondicionesAmbientalesP() {
        if (listSoCondicionesAmbientalesP == null) {
            listSoCondicionesAmbientalesP = administrarSoCondicionesAmbientalesP.consultarSoCondicionesAmbientalesP();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoCondicionesAmbientalesP == null || listSoCondicionesAmbientalesP.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoCondicionesAmbientalesP.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listSoCondicionesAmbientalesP;
    }

    public void setListSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP) {
        this.listSoCondicionesAmbientalesP = listSoCondicionesAmbientalesP;
    }

    public List<SoCondicionesAmbientalesP> getFiltrarSoCondicionesAmbientalesP() {
        return filtrarSoCondicionesAmbientalesP;
    }

    public void setFiltrarSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> filtrarSoCondicionesAmbientalesP) {
        this.filtrarSoCondicionesAmbientalesP = filtrarSoCondicionesAmbientalesP;
    }

    public SoCondicionesAmbientalesP getNuevaSoCondicionAmbientalP() {
        return nuevaSoCondicionAmbientalP;
    }

    public void setNuevaSoCondicionAmbientalP(SoCondicionesAmbientalesP nuevaSoCondicionAmbientalP) {
        this.nuevaSoCondicionAmbientalP = nuevaSoCondicionAmbientalP;
    }

    public SoCondicionesAmbientalesP getDuplicarSoCondicionAmbientalP() {
        return duplicarSoCondicionAmbientalP;
    }

    public void setDuplicarSoCondicionAmbientalP(SoCondicionesAmbientalesP duplicarSoCondicionAmbientalP) {
        this.duplicarSoCondicionAmbientalP = duplicarSoCondicionAmbientalP;
    }

    public SoCondicionesAmbientalesP getEditarSoCondicionAmbientalP() {
        return editarSoCondicionAmbientalP;
    }

    public void setEditarSoCondicionAmbientalP(SoCondicionesAmbientalesP editarSoCondicionAmbientalP) {
        this.editarSoCondicionAmbientalP = editarSoCondicionAmbientalP;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
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

    public SoCondicionesAmbientalesP getSoCondicionAmbientalPSeleccionado() {
        return soCondicionAmbientalPSeleccionado;
    }

    public void setSoCondicionAmbientalPSeleccionado(SoCondicionesAmbientalesP soCondicionAmbientalPSeleccionado) {
        this.soCondicionAmbientalPSeleccionado = soCondicionAmbientalPSeleccionado;
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

}
