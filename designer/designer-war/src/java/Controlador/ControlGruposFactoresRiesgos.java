/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.GruposFactoresRiesgos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGruposFactoresRiesgosInterface;
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
public class ControlGruposFactoresRiesgos implements Serializable {

    @EJB
    AdministrarGruposFactoresRiesgosInterface administrarGruposFactoresRiesgos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<GruposFactoresRiesgos> listGruposFactoresRiesgos;
    private List<GruposFactoresRiesgos> filtrarGruposFactoresRiesgos;
    private List<GruposFactoresRiesgos> crearGruposFactoresRiesgos;
    private List<GruposFactoresRiesgos> modificarGruposFactoresRiesgos;
    private List<GruposFactoresRiesgos> borrarGruposFactoresRiesgos;
    private GruposFactoresRiesgos nuevoGruposFactoresRiesgos;
    private GruposFactoresRiesgos duplicarGruposFactoresRiesgos;
    private GruposFactoresRiesgos editarGruposFactoresRiesgos;
    private GruposFactoresRiesgos grupoFactorRiesgoSeleccionado;
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
    //filtrado table
    private int tamano;
    private Integer backupCodigo;
    private String backupDescripcion;
    private String infoRegistro;

    public ControlGruposFactoresRiesgos() {
        listGruposFactoresRiesgos = null;
        crearGruposFactoresRiesgos = new ArrayList<GruposFactoresRiesgos>();
        modificarGruposFactoresRiesgos = new ArrayList<GruposFactoresRiesgos>();
        borrarGruposFactoresRiesgos = new ArrayList<GruposFactoresRiesgos>();
        permitirIndex = true;
        editarGruposFactoresRiesgos = new GruposFactoresRiesgos();
        nuevoGruposFactoresRiesgos = new GruposFactoresRiesgos();
        duplicarGruposFactoresRiesgos = new GruposFactoresRiesgos();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarGruposFactoresRiesgos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlGruposFactoresRiesgos.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarGruposFactoresRiesgos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlGruposFactoresRiesgos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);
        System.err.println("permitirIndex  " + permitirIndex);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listGruposFactoresRiesgos.get(index).getSecuencia();
            if (tipoLista == 0) {
                backupCodigo = listGruposFactoresRiesgos.get(index).getCodigo();
                backupDescripcion = listGruposFactoresRiesgos.get(index).getDescripcion();
            } else if (tipoLista == 1) {
                backupCodigo = filtrarGruposFactoresRiesgos.get(index).getCodigo();
                backupDescripcion = filtrarGruposFactoresRiesgos.get(index).getDescripcion();
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlGruposFactoresRiesgos.asignarIndex \n");
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
            System.out.println("ERROR ControlGruposFactoresRiesgos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
            bandera = 0;
            filtrarGruposFactoresRiesgos = null;
            tipoLista = 0;
        }

        borrarGruposFactoresRiesgos.clear();
        crearGruposFactoresRiesgos.clear();
        modificarGruposFactoresRiesgos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listGruposFactoresRiesgos = null;
        guardado = true;
        permitirIndex = true;
        getListGruposFactoresRiesgos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listGruposFactoresRiesgos == null || listGruposFactoresRiesgos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listGruposFactoresRiesgos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
            bandera = 0;
            filtrarGruposFactoresRiesgos = null;
            tipoLista = 0;
        }

        borrarGruposFactoresRiesgos.clear();
        crearGruposFactoresRiesgos.clear();
        modificarGruposFactoresRiesgos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listGruposFactoresRiesgos = null;
        guardado = true;
        permitirIndex = true;
        getListGruposFactoresRiesgos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listGruposFactoresRiesgos == null || listGruposFactoresRiesgos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listGruposFactoresRiesgos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:codigo");
            codigo.setFilterStyle("width: 85%");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:descripcion");
            descripcion.setFilterStyle("width: 85%");
            RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
            bandera = 0;
            filtrarGruposFactoresRiesgos = null;
            tipoLista = 0;
        }
    }

    public void modificarGruposFactoresRiesgos(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearGruposFactoresRiesgos.contains(listGruposFactoresRiesgos.get(indice))) {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listGruposFactoresRiesgos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listGruposFactoresRiesgos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listGruposFactoresRiesgos.size(); j++) {
                            if (j != indice) {
                                if (listGruposFactoresRiesgos.get(indice).getCodigo() == listGruposFactoresRiesgos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listGruposFactoresRiesgos.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listGruposFactoresRiesgos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listGruposFactoresRiesgos.get(indice).setDescripcion(backupDescripcion);
                    } else if (listGruposFactoresRiesgos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listGruposFactoresRiesgos.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarGruposFactoresRiesgos.isEmpty()) {
                            modificarGruposFactoresRiesgos.add(listGruposFactoresRiesgos.get(indice));
                        } else if (!modificarGruposFactoresRiesgos.contains(listGruposFactoresRiesgos.get(indice))) {
                            modificarGruposFactoresRiesgos.add(listGruposFactoresRiesgos.get(indice));
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
                    RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listGruposFactoresRiesgos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listGruposFactoresRiesgos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listGruposFactoresRiesgos.size(); j++) {
                            if (j != indice) {
                                if (listGruposFactoresRiesgos.get(indice).getCodigo() == listGruposFactoresRiesgos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listGruposFactoresRiesgos.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listGruposFactoresRiesgos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listGruposFactoresRiesgos.get(indice).setDescripcion(backupDescripcion);
                    } else if (listGruposFactoresRiesgos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listGruposFactoresRiesgos.get(indice).setDescripcion(backupDescripcion);

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
                    RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            } else {

                if (!crearGruposFactoresRiesgos.contains(filtrarGruposFactoresRiesgos.get(indice))) {
                    if (filtrarGruposFactoresRiesgos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarGruposFactoresRiesgos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarGruposFactoresRiesgos.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposFactoresRiesgos.get(indice).getCodigo() == listGruposFactoresRiesgos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listGruposFactoresRiesgos.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposFactoresRiesgos.get(indice).getCodigo() == listGruposFactoresRiesgos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarGruposFactoresRiesgos.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarGruposFactoresRiesgos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarGruposFactoresRiesgos.get(indice).setDescripcion(backupDescripcion);
                    }
                    if (filtrarGruposFactoresRiesgos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarGruposFactoresRiesgos.get(indice).setDescripcion(backupDescripcion);
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarGruposFactoresRiesgos.isEmpty()) {
                            modificarGruposFactoresRiesgos.add(filtrarGruposFactoresRiesgos.get(indice));
                        } else if (!modificarGruposFactoresRiesgos.contains(filtrarGruposFactoresRiesgos.get(indice))) {
                            modificarGruposFactoresRiesgos.add(filtrarGruposFactoresRiesgos.get(indice));
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
                    if (filtrarGruposFactoresRiesgos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarGruposFactoresRiesgos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarGruposFactoresRiesgos.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposFactoresRiesgos.get(indice).getCodigo() == listGruposFactoresRiesgos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listGruposFactoresRiesgos.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposFactoresRiesgos.get(indice).getCodigo() == listGruposFactoresRiesgos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarGruposFactoresRiesgos.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarGruposFactoresRiesgos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarGruposFactoresRiesgos.get(indice).setDescripcion(backupDescripcion);
                    }
                    if (filtrarGruposFactoresRiesgos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarGruposFactoresRiesgos.get(indice).setDescripcion(backupDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoGruposFactoresRiesgos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoGruposFactoresRiesgos");
                if (!modificarGruposFactoresRiesgos.isEmpty() && modificarGruposFactoresRiesgos.contains(listGruposFactoresRiesgos.get(index))) {
                    int modIndex = modificarGruposFactoresRiesgos.indexOf(listGruposFactoresRiesgos.get(index));
                    modificarGruposFactoresRiesgos.remove(modIndex);
                    borrarGruposFactoresRiesgos.add(listGruposFactoresRiesgos.get(index));
                } else if (!crearGruposFactoresRiesgos.isEmpty() && crearGruposFactoresRiesgos.contains(listGruposFactoresRiesgos.get(index))) {
                    int crearIndex = crearGruposFactoresRiesgos.indexOf(listGruposFactoresRiesgos.get(index));
                    crearGruposFactoresRiesgos.remove(crearIndex);
                } else {
                    borrarGruposFactoresRiesgos.add(listGruposFactoresRiesgos.get(index));
                }
                listGruposFactoresRiesgos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoGruposFactoresRiesgos ");
                if (!modificarGruposFactoresRiesgos.isEmpty() && modificarGruposFactoresRiesgos.contains(filtrarGruposFactoresRiesgos.get(index))) {
                    int modIndex = modificarGruposFactoresRiesgos.indexOf(filtrarGruposFactoresRiesgos.get(index));
                    modificarGruposFactoresRiesgos.remove(modIndex);
                    borrarGruposFactoresRiesgos.add(filtrarGruposFactoresRiesgos.get(index));
                } else if (!crearGruposFactoresRiesgos.isEmpty() && crearGruposFactoresRiesgos.contains(filtrarGruposFactoresRiesgos.get(index))) {
                    int crearIndex = crearGruposFactoresRiesgos.indexOf(filtrarGruposFactoresRiesgos.get(index));
                    crearGruposFactoresRiesgos.remove(crearIndex);
                } else {
                    borrarGruposFactoresRiesgos.add(filtrarGruposFactoresRiesgos.get(index));
                }
                int VCIndex = listGruposFactoresRiesgos.indexOf(filtrarGruposFactoresRiesgos.get(index));
                listGruposFactoresRiesgos.remove(VCIndex);
                filtrarGruposFactoresRiesgos.remove(index);

            }
            infoRegistro = "Cantidad de registros: " + listGruposFactoresRiesgos.size();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        BigInteger contarFactoresRiesgoGrupoFactorRiesgo;
        BigInteger contarSoProActividadesGrupoFactorRiesgo;
        BigInteger contarSoIndicadoresGrupoFactorRiesgo;

        try {
            System.err.println("Control Secuencia de ControlGruposFactoresRiesgos ");
            if (tipoLista == 0) {
                contarFactoresRiesgoGrupoFactorRiesgo = administrarGruposFactoresRiesgos.contarFactoresRiesgoGrupoFactorRiesgo(listGruposFactoresRiesgos.get(index).getSecuencia());
                contarSoIndicadoresGrupoFactorRiesgo = administrarGruposFactoresRiesgos.contarSoIndicadoresGrupoFactorRiesgo(listGruposFactoresRiesgos.get(index).getSecuencia());
                contarSoProActividadesGrupoFactorRiesgo = administrarGruposFactoresRiesgos.contarSoProActividadesGrupoFactorRiesgo(listGruposFactoresRiesgos.get(index).getSecuencia());
            } else {
                contarFactoresRiesgoGrupoFactorRiesgo = administrarGruposFactoresRiesgos.contarFactoresRiesgoGrupoFactorRiesgo(filtrarGruposFactoresRiesgos.get(index).getSecuencia());
                contarSoIndicadoresGrupoFactorRiesgo = administrarGruposFactoresRiesgos.contarSoIndicadoresGrupoFactorRiesgo(filtrarGruposFactoresRiesgos.get(index).getSecuencia());
                contarSoProActividadesGrupoFactorRiesgo = administrarGruposFactoresRiesgos.contarSoProActividadesGrupoFactorRiesgo(filtrarGruposFactoresRiesgos.get(index).getSecuencia());
            }
            if (contarFactoresRiesgoGrupoFactorRiesgo.equals(new BigInteger("0")) && contarSoIndicadoresGrupoFactorRiesgo.equals(new BigInteger("0")) && contarSoProActividadesGrupoFactorRiesgo.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoGruposFactoresRiesgos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
                contarFactoresRiesgoGrupoFactorRiesgo = new BigInteger("-1");
                contarSoIndicadoresGrupoFactorRiesgo = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlGruposFactoresRiesgos verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarGruposFactoresRiesgos.isEmpty() || !crearGruposFactoresRiesgos.isEmpty() || !modificarGruposFactoresRiesgos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarGruposFactoresRiesgos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarGruposFactoresRiesgos");
            if (!borrarGruposFactoresRiesgos.isEmpty()) {
                administrarGruposFactoresRiesgos.borrarGruposFactoresRiesgos(borrarGruposFactoresRiesgos);
                //mostrarBorrados
                registrosBorrados = borrarGruposFactoresRiesgos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarGruposFactoresRiesgos.clear();
            }
            if (!modificarGruposFactoresRiesgos.isEmpty()) {
                administrarGruposFactoresRiesgos.modificarGruposFactoresRiesgos(modificarGruposFactoresRiesgos);
                modificarGruposFactoresRiesgos.clear();
            }
            if (!crearGruposFactoresRiesgos.isEmpty()) {
                administrarGruposFactoresRiesgos.crearGruposFactoresRiesgos(crearGruposFactoresRiesgos);
                crearGruposFactoresRiesgos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listGruposFactoresRiesgos = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarGruposFactoresRiesgos = listGruposFactoresRiesgos.get(index);
            }
            if (tipoLista == 1) {
                editarGruposFactoresRiesgos = filtrarGruposFactoresRiesgos.get(index);
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

    public void agregarNuevoGruposFactoresRiesgos() {
        System.out.println("agregarNuevoGruposFactoresRiesgos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoGruposFactoresRiesgos.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoGruposFactoresRiesgos.getCodigo());

            for (int x = 0; x < listGruposFactoresRiesgos.size(); x++) {
                if (listGruposFactoresRiesgos.get(x).getCodigo() == nuevoGruposFactoresRiesgos.getCodigo()) {
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
        if (nuevoGruposFactoresRiesgos.getDescripcion() == null || nuevoGruposFactoresRiesgos.getDescripcion().isEmpty()) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
                bandera = 0;
                filtrarGruposFactoresRiesgos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoGruposFactoresRiesgos.setSecuencia(l);

            crearGruposFactoresRiesgos.add(nuevoGruposFactoresRiesgos);

            listGruposFactoresRiesgos.add(nuevoGruposFactoresRiesgos);
            infoRegistro = "Cantidad de registros: " + listGruposFactoresRiesgos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            nuevoGruposFactoresRiesgos = new GruposFactoresRiesgos();
            RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroGruposFactoresRiesgos').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoGruposFactoresRiesgos() {
        System.out.println("limpiarNuevoGruposFactoresRiesgos");
        nuevoGruposFactoresRiesgos = new GruposFactoresRiesgos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoGruposFactoresRiesgos() {
        System.out.println("duplicandoGruposFactoresRiesgos");
        if (index >= 0) {
            duplicarGruposFactoresRiesgos = new GruposFactoresRiesgos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarGruposFactoresRiesgos.setSecuencia(l);
                duplicarGruposFactoresRiesgos.setCodigo(listGruposFactoresRiesgos.get(index).getCodigo());
                duplicarGruposFactoresRiesgos.setDescripcion(listGruposFactoresRiesgos.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarGruposFactoresRiesgos.setSecuencia(l);
                duplicarGruposFactoresRiesgos.setCodigo(filtrarGruposFactoresRiesgos.get(index).getCodigo());
                duplicarGruposFactoresRiesgos.setDescripcion(filtrarGruposFactoresRiesgos.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroGruposFactoresRiesgos').show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarGruposFactoresRiesgos.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarGruposFactoresRiesgos.getDescripcion());

        if (duplicarGruposFactoresRiesgos.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listGruposFactoresRiesgos.size(); x++) {
                if (listGruposFactoresRiesgos.get(x).getCodigo() == duplicarGruposFactoresRiesgos.getCodigo()) {
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
        if (duplicarGruposFactoresRiesgos.getDescripcion() == null || duplicarGruposFactoresRiesgos.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarGruposFactoresRiesgos.getSecuencia() + "  " + duplicarGruposFactoresRiesgos.getCodigo());
            if (crearGruposFactoresRiesgos.contains(duplicarGruposFactoresRiesgos)) {
                System.out.println("Ya lo contengo.");
            }
            listGruposFactoresRiesgos.add(duplicarGruposFactoresRiesgos);
            crearGruposFactoresRiesgos.add(duplicarGruposFactoresRiesgos);
            RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
            index = -1;
            infoRegistro = "Cantidad de registros: " + listGruposFactoresRiesgos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposFactoresRiesgos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGruposFactoresRiesgos");
                bandera = 0;
                filtrarGruposFactoresRiesgos = null;
                tipoLista = 0;
            }
            duplicarGruposFactoresRiesgos = new GruposFactoresRiesgos();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroGruposFactoresRiesgos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarGruposFactoresRiesgos() {
        duplicarGruposFactoresRiesgos = new GruposFactoresRiesgos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposFactoresRiesgosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "GRUPOSFACTORESRIESGOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposFactoresRiesgosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "GRUPOSFACTORESRIESGOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listGruposFactoresRiesgos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "GRUPOSFACTORESRIESGOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("GRUPOSFACTORESRIESGOS")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<GruposFactoresRiesgos> getListGruposFactoresRiesgos() {
        if (listGruposFactoresRiesgos == null) {
            listGruposFactoresRiesgos = administrarGruposFactoresRiesgos.consultarGruposFactoresRiesgos();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listGruposFactoresRiesgos == null || listGruposFactoresRiesgos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listGruposFactoresRiesgos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listGruposFactoresRiesgos;
    }

    public void setListGruposFactoresRiesgos(List<GruposFactoresRiesgos> listGruposFactoresRiesgos) {
        this.listGruposFactoresRiesgos = listGruposFactoresRiesgos;
    }

    public List<GruposFactoresRiesgos> getFiltrarGruposFactoresRiesgos() {
        return filtrarGruposFactoresRiesgos;
    }

    public void setFiltrarGruposFactoresRiesgos(List<GruposFactoresRiesgos> filtrarGruposFactoresRiesgos) {
        this.filtrarGruposFactoresRiesgos = filtrarGruposFactoresRiesgos;
    }

    public GruposFactoresRiesgos getNuevoGruposFactoresRiesgos() {
        return nuevoGruposFactoresRiesgos;
    }

    public void setNuevoGruposFactoresRiesgos(GruposFactoresRiesgos nuevoGruposFactoresRiesgos) {
        this.nuevoGruposFactoresRiesgos = nuevoGruposFactoresRiesgos;
    }

    public GruposFactoresRiesgos getDuplicarGruposFactoresRiesgos() {
        return duplicarGruposFactoresRiesgos;
    }

    public void setDuplicarGruposFactoresRiesgos(GruposFactoresRiesgos duplicarGruposFactoresRiesgos) {
        this.duplicarGruposFactoresRiesgos = duplicarGruposFactoresRiesgos;
    }

    public GruposFactoresRiesgos getEditarGruposFactoresRiesgos() {
        return editarGruposFactoresRiesgos;
    }

    public void setEditarGruposFactoresRiesgos(GruposFactoresRiesgos editarGruposFactoresRiesgos) {
        this.editarGruposFactoresRiesgos = editarGruposFactoresRiesgos;
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

    public GruposFactoresRiesgos getGrupoFactorRiesgoSeleccionado() {
        return grupoFactorRiesgoSeleccionado;
    }

    public void setGrupoFactorRiesgoSeleccionado(GruposFactoresRiesgos grupoFactorRiesgoSeleccionado) {
        this.grupoFactorRiesgoSeleccionado = grupoFactorRiesgoSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
