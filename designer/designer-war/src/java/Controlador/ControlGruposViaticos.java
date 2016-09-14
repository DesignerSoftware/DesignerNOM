/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.GruposViaticos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGruposViaticosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class ControlGruposViaticos implements Serializable {

    @EJB
    AdministrarGruposViaticosInterface administrarGruposViaticos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<GruposViaticos> listGruposViaticos;
    private List<GruposViaticos> filtrarGruposViaticos;
    private List<GruposViaticos> crearGruposViaticos;
    private List<GruposViaticos> modificarGruposViaticos;
    private List<GruposViaticos> borrarGruposViaticos;
    private GruposViaticos nuevoGruposViaticos;
    private GruposViaticos duplicarGruposViaticos;
    private GruposViaticos editarGruposViaticos;
    private GruposViaticos grupoFactorRiesgoSeleccionado;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion, estado;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private Integer backupCodigo;
    private String backupDescripcion;
    private String infoRegistro;

    public ControlGruposViaticos() {
        listGruposViaticos = null;
        crearGruposViaticos = new ArrayList<GruposViaticos>();
        modificarGruposViaticos = new ArrayList<GruposViaticos>();
        borrarGruposViaticos = new ArrayList<GruposViaticos>();
        permitirIndex = true;
        editarGruposViaticos = new GruposViaticos();
        nuevoGruposViaticos = new GruposViaticos();
        duplicarGruposViaticos = new GruposViaticos();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarGruposViaticos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlGruposViaticos.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarGruposViaticos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlGruposViaticos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    private BigDecimal backUpPorcentaje;

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);
        System.err.println("permitirIndex  " + permitirIndex);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listGruposViaticos.get(index).getSecuencia();
            if (tipoLista == 0) {
                backupCodigo = listGruposViaticos.get(index).getCodigo();
                backupDescripcion = listGruposViaticos.get(index).getDescripcion();
                backUpPorcentaje = listGruposViaticos.get(index).getPorcentajelastday();
            } else if (tipoLista == 1) {
                backupCodigo = filtrarGruposViaticos.get(index).getCodigo();
                backupDescripcion = filtrarGruposViaticos.get(index).getDescripcion();
                backUpPorcentaje = filtrarGruposViaticos.get(index).getPorcentajelastday();
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlGruposViaticos.asignarIndex \n");
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
            System.out.println("ERROR ControlGruposViaticos.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            bandera = 0;
            filtrarGruposViaticos = null;
            tipoLista = 0;
        }

        borrarGruposViaticos.clear();
        crearGruposViaticos.clear();
        modificarGruposViaticos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listGruposViaticos = null;
        guardado = true;
        permitirIndex = true;
        getListGruposViaticos();
        RequestContext context = RequestContext.getCurrentInstance();

        if (listGruposViaticos == null || listGruposViaticos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listGruposViaticos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            bandera = 0;
            filtrarGruposViaticos = null;
            tipoLista = 0;
        }

        borrarGruposViaticos.clear();
        crearGruposViaticos.clear();
        modificarGruposViaticos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listGruposViaticos = null;
        guardado = true;
        permitirIndex = true;
        getListGruposViaticos();
        RequestContext context = RequestContext.getCurrentInstance();

        if (listGruposViaticos == null || listGruposViaticos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listGruposViaticos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
            estado.setFilterStyle("width: 85% !important");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            bandera = 0;
            filtrarGruposViaticos = null;
            tipoLista = 0;
        }
    }

    public void modificarGruposViaticos(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0, pass = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearGruposViaticos.contains(listGruposViaticos.get(indice))) {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listGruposViaticos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listGruposViaticos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listGruposViaticos.size(); j++) {
                            if (j != indice) {
                                if (listGruposViaticos.get(indice).getCodigo() == listGruposViaticos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listGruposViaticos.get(indice).setCodigo(backupCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listGruposViaticos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listGruposViaticos.get(indice).setDescripcion(backupDescripcion);
                    } else if (listGruposViaticos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listGruposViaticos.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        pass++;
                    }
                    if (listGruposViaticos.get(indice).getPorcentajelastday() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listGruposViaticos.get(indice).setPorcentajelastday(backUpPorcentaje);
                    } else if (listGruposViaticos.get(indice).getPorcentajelastday().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listGruposViaticos.get(indice).setPorcentajelastday(backUpPorcentaje);

                    } else {
                        pass++;
                    }

                    if (pass == 3) {
                        if (modificarGruposViaticos.isEmpty()) {
                            modificarGruposViaticos.add(listGruposViaticos.get(indice));
                        } else if (!modificarGruposViaticos.contains(listGruposViaticos.get(indice))) {
                            modificarGruposViaticos.add(listGruposViaticos.get(indice));
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
                    RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listGruposViaticos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listGruposViaticos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listGruposViaticos.size(); j++) {
                            if (j != indice) {
                                if (listGruposViaticos.get(indice).getCodigo() == listGruposViaticos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listGruposViaticos.get(indice).setCodigo(backupCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listGruposViaticos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listGruposViaticos.get(indice).setDescripcion(backupDescripcion);
                    } else if (listGruposViaticos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listGruposViaticos.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        pass++;
                    }
                    if (listGruposViaticos.get(indice).getPorcentajelastday() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listGruposViaticos.get(indice).setPorcentajelastday(backUpPorcentaje);
                    } else {
                        pass++;
                    }
                    if (pass == 3) {
                        if (guardado == true) {
                            guardado = false;
                        }
                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                    RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            } else {

                if (!crearGruposViaticos.contains(filtrarGruposViaticos.get(indice))) {
                    if (filtrarGruposViaticos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarGruposViaticos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarGruposViaticos.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposViaticos.get(indice).getCodigo() == listGruposViaticos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listGruposViaticos.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposViaticos.get(indice).getCodigo() == listGruposViaticos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarGruposViaticos.get(indice).setCodigo(backupCodigo);

                        } else {
                            pass++;
                        }

                    }

                    if (filtrarGruposViaticos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarGruposViaticos.get(indice).setDescripcion(backupDescripcion);
                    } else if (filtrarGruposViaticos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarGruposViaticos.get(indice).setDescripcion(backupDescripcion);
                    } else {
                        pass++;
                    }
                    if (filtrarGruposViaticos.get(indice).getPorcentajelastday() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarGruposViaticos.get(indice).setPorcentajelastday(backUpPorcentaje);
                    } else if (filtrarGruposViaticos.get(indice).getPorcentajelastday().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarGruposViaticos.get(indice).setPorcentajelastday(backUpPorcentaje);

                    } else {
                        pass++;
                    }
                    if (pass == 3) {
                        if (modificarGruposViaticos.isEmpty()) {
                            modificarGruposViaticos.add(filtrarGruposViaticos.get(indice));
                        } else if (!modificarGruposViaticos.contains(filtrarGruposViaticos.get(indice))) {
                            modificarGruposViaticos.add(filtrarGruposViaticos.get(indice));
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
                    if (filtrarGruposViaticos.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarGruposViaticos.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarGruposViaticos.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposViaticos.get(indice).getCodigo() == listGruposViaticos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listGruposViaticos.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposViaticos.get(indice).getCodigo() == listGruposViaticos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarGruposViaticos.get(indice).setCodigo(backupCodigo);

                        } else {
                            pass++;
                        }

                    }

                    if (filtrarGruposViaticos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarGruposViaticos.get(indice).setDescripcion(backupDescripcion);
                    } else if (filtrarGruposViaticos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarGruposViaticos.get(indice).setDescripcion(backupDescripcion);
                    } else {
                        pass++;
                    }
                    if (filtrarGruposViaticos.get(indice).getPorcentajelastday() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarGruposViaticos.get(indice).setPorcentajelastday(backUpPorcentaje);
                    } else if (filtrarGruposViaticos.get(indice).getPorcentajelastday().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarGruposViaticos.get(indice).setPorcentajelastday(backUpPorcentaje);

                    } else {
                        pass++;
                    }
                    if (pass == 3) {
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
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoGruposViaticos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoGruposViaticos");
                if (!modificarGruposViaticos.isEmpty() && modificarGruposViaticos.contains(listGruposViaticos.get(index))) {
                    int modIndex = modificarGruposViaticos.indexOf(listGruposViaticos.get(index));
                    modificarGruposViaticos.remove(modIndex);
                    borrarGruposViaticos.add(listGruposViaticos.get(index));
                } else if (!crearGruposViaticos.isEmpty() && crearGruposViaticos.contains(listGruposViaticos.get(index))) {
                    int crearIndex = crearGruposViaticos.indexOf(listGruposViaticos.get(index));
                    crearGruposViaticos.remove(crearIndex);
                } else {
                    borrarGruposViaticos.add(listGruposViaticos.get(index));
                }
                listGruposViaticos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoGruposViaticos ");
                if (!modificarGruposViaticos.isEmpty() && modificarGruposViaticos.contains(filtrarGruposViaticos.get(index))) {
                    int modIndex = modificarGruposViaticos.indexOf(filtrarGruposViaticos.get(index));
                    modificarGruposViaticos.remove(modIndex);
                    borrarGruposViaticos.add(filtrarGruposViaticos.get(index));
                } else if (!crearGruposViaticos.isEmpty() && crearGruposViaticos.contains(filtrarGruposViaticos.get(index))) {
                    int crearIndex = crearGruposViaticos.indexOf(filtrarGruposViaticos.get(index));
                    crearGruposViaticos.remove(crearIndex);
                } else {
                    borrarGruposViaticos.add(filtrarGruposViaticos.get(index));
                }
                int VCIndex = listGruposViaticos.indexOf(filtrarGruposViaticos.get(index));
                listGruposViaticos.remove(VCIndex);
                filtrarGruposViaticos.remove(index);

            }
            infoRegistro = "Cantidad de registros: " + listGruposViaticos.size();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
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
        BigInteger verificarCargos;
        BigInteger verificarEersViaticos;
        BigInteger verificarPlantas;
        BigInteger verificarTablasViaticos;

        try {
            System.err.println("Control Secuencia de ControlGruposViaticos ");
            if (tipoLista == 0) {
                verificarCargos = administrarGruposViaticos.verificarCargos(listGruposViaticos.get(index).getSecuencia());
                verificarEersViaticos = administrarGruposViaticos.verificarEersViaticos(listGruposViaticos.get(index).getSecuencia());
                verificarPlantas = administrarGruposViaticos.verificarPlantas(listGruposViaticos.get(index).getSecuencia());
                verificarTablasViaticos = administrarGruposViaticos.verificarTablasViaticos(listGruposViaticos.get(index).getSecuencia());
            } else {
                verificarCargos = administrarGruposViaticos.verificarCargos(filtrarGruposViaticos.get(index).getSecuencia());
                verificarEersViaticos = administrarGruposViaticos.verificarEersViaticos(filtrarGruposViaticos.get(index).getSecuencia());
                verificarPlantas = administrarGruposViaticos.verificarPlantas(filtrarGruposViaticos.get(index).getSecuencia());
                verificarTablasViaticos = administrarGruposViaticos.verificarTablasViaticos(filtrarGruposViaticos.get(index).getSecuencia());
            }
            if (verificarCargos.equals(new BigInteger("0"))
                    && verificarEersViaticos.equals(new BigInteger("0"))
                    && verificarPlantas.equals(new BigInteger("0"))
                    && verificarTablasViaticos.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoGruposViaticos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
                verificarCargos = new BigInteger("-1");
                verificarEersViaticos = new BigInteger("-1");
                verificarPlantas = new BigInteger("-1");
                verificarTablasViaticos = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlGruposViaticos verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarGruposViaticos.isEmpty() || !crearGruposViaticos.isEmpty() || !modificarGruposViaticos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarGruposViaticos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarGruposViaticos");
            if (!borrarGruposViaticos.isEmpty()) {
                administrarGruposViaticos.borrarGruposViaticos(borrarGruposViaticos);
                //mostrarBorrados
                registrosBorrados = borrarGruposViaticos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarGruposViaticos.clear();
            }
            if (!modificarGruposViaticos.isEmpty()) {
                administrarGruposViaticos.modificarGruposViaticos(modificarGruposViaticos);
                modificarGruposViaticos.clear();
            }
            if (!crearGruposViaticos.isEmpty()) {
                administrarGruposViaticos.crearGruposViaticos(crearGruposViaticos);
                crearGruposViaticos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listGruposViaticos = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarGruposViaticos = listGruposViaticos.get(index);
            }
            if (tipoLista == 1) {
                editarGruposViaticos = filtrarGruposViaticos.get(index);
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

    public void agregarNuevoGruposViaticos() {
        System.out.println("agregarNuevoGruposViaticos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoGruposViaticos.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoGruposViaticos.getCodigo());

            for (int x = 0; x < listGruposViaticos.size(); x++) {
                if (listGruposViaticos.get(x).getCodigo() == nuevoGruposViaticos.getCodigo()) {
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
        if (nuevoGruposViaticos.getDescripcion() == null || nuevoGruposViaticos.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;
        }
        if (nuevoGruposViaticos.getPorcentajelastday() == null || nuevoGruposViaticos.getPorcentajelastday() == null) {
            mensajeValidacion = mensajeValidacion + " *Porcentaje \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;
        }

        System.out.println("contador " + contador);

        if (contador == 3) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
                estado.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
                bandera = 0;
                filtrarGruposViaticos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoGruposViaticos.setSecuencia(l);

            crearGruposViaticos.add(nuevoGruposViaticos);

            listGruposViaticos.add(nuevoGruposViaticos);
            infoRegistro = "Cantidad de registros: " + listGruposViaticos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            nuevoGruposViaticos = new GruposViaticos();
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroGruposViaticos').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoGruposViaticos() {
        System.out.println("limpiarNuevoGruposViaticos");
        nuevoGruposViaticos = new GruposViaticos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoGruposViaticos() {
        System.out.println("duplicandoGruposViaticos");
        if (index >= 0) {
            duplicarGruposViaticos = new GruposViaticos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarGruposViaticos.setSecuencia(l);
                duplicarGruposViaticos.setCodigo(listGruposViaticos.get(index).getCodigo());
                duplicarGruposViaticos.setDescripcion(listGruposViaticos.get(index).getDescripcion());
                duplicarGruposViaticos.setPorcentajelastday(listGruposViaticos.get(index).getPorcentajelastday());
            }
            if (tipoLista == 1) {
                duplicarGruposViaticos.setSecuencia(l);
                duplicarGruposViaticos.setCodigo(filtrarGruposViaticos.get(index).getCodigo());
                duplicarGruposViaticos.setDescripcion(filtrarGruposViaticos.get(index).getDescripcion());
                duplicarGruposViaticos.setPorcentajelastday(filtrarGruposViaticos.get(index).getPorcentajelastday());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroGruposViaticos').show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarGruposViaticos.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarGruposViaticos.getDescripcion());

        if (duplicarGruposViaticos.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   * Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listGruposViaticos.size(); x++) {
                if (listGruposViaticos.get(x).getCodigo() == duplicarGruposViaticos.getCodigo()) {
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
        if (duplicarGruposViaticos.getDescripcion() == null || duplicarGruposViaticos.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   * una Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarGruposViaticos.getPorcentajelastday() == null || duplicarGruposViaticos.getPorcentajelastday() == null) {
            mensajeValidacion = mensajeValidacion + "   *Porcentaje \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 3) {

            System.out.println("Datos Duplicando: " + duplicarGruposViaticos.getSecuencia() + "  " + duplicarGruposViaticos.getCodigo());
            if (crearGruposViaticos.contains(duplicarGruposViaticos)) {
                System.out.println("Ya lo contengo.");
            }
            listGruposViaticos.add(duplicarGruposViaticos);
            crearGruposViaticos.add(duplicarGruposViaticos);
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            index = -1;
            infoRegistro = "Cantidad de registros: " + listGruposViaticos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
                estado.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
                bandera = 0;
                filtrarGruposViaticos = null;
                tipoLista = 0;
            }
            duplicarGruposViaticos = new GruposViaticos();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroGruposViaticos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarGruposViaticos() {
        duplicarGruposViaticos = new GruposViaticos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposViaticosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "GRUPOSINFADICIONALES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposViaticosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "GRUPOSINFADICIONALES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listGruposViaticos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "GRUPOSINFADICIONALES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("GRUPOSINFADICIONALES")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<GruposViaticos> getListGruposViaticos() {
        if (listGruposViaticos == null) {
            listGruposViaticos = administrarGruposViaticos.consultarGruposViaticos();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listGruposViaticos == null || listGruposViaticos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listGruposViaticos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listGruposViaticos;
    }

    public void setListGruposViaticos(List<GruposViaticos> listGruposViaticos) {
        this.listGruposViaticos = listGruposViaticos;
    }

    public List<GruposViaticos> getFiltrarGruposViaticos() {
        return filtrarGruposViaticos;
    }

    public void setFiltrarGruposViaticos(List<GruposViaticos> filtrarGruposViaticos) {
        this.filtrarGruposViaticos = filtrarGruposViaticos;
    }

    public GruposViaticos getNuevoGruposViaticos() {
        return nuevoGruposViaticos;
    }

    public void setNuevoGruposViaticos(GruposViaticos nuevoGruposViaticos) {
        this.nuevoGruposViaticos = nuevoGruposViaticos;
    }

    public GruposViaticos getDuplicarGruposViaticos() {
        return duplicarGruposViaticos;
    }

    public void setDuplicarGruposViaticos(GruposViaticos duplicarGruposViaticos) {
        this.duplicarGruposViaticos = duplicarGruposViaticos;
    }

    public GruposViaticos getEditarGruposViaticos() {
        return editarGruposViaticos;
    }

    public void setEditarGruposViaticos(GruposViaticos editarGruposViaticos) {
        this.editarGruposViaticos = editarGruposViaticos;
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

    public GruposViaticos getGrupoFactorRiesgoSeleccionado() {
        return grupoFactorRiesgoSeleccionado;
    }

    public void setGrupoFactorRiesgoSeleccionado(GruposViaticos grupoFactorRiesgoSeleccionado) {
        this.grupoFactorRiesgoSeleccionado = grupoFactorRiesgoSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
