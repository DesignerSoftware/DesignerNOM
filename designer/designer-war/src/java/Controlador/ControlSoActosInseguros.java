/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.SoActosInseguros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSoActosInsegurosInterface;
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
import utilidadesUI.PrimefacesContextUI;

/**
 *
 * @author John Pineda
 */
@ManagedBean
@SessionScoped
public class ControlSoActosInseguros implements Serializable {

    @EJB
    AdministrarSoActosInsegurosInterface administrarSoActosInseguros;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<SoActosInseguros> listSoActosInseguros;
    private List<SoActosInseguros> filtrarSoActosInseguros;
    private List<SoActosInseguros> crearSoActosInseguros;
    private List<SoActosInseguros> modificarSoActosInseguros;
    private List<SoActosInseguros> borrarSoActosInseguros;
    private SoActosInseguros nuevaSoActoInseguro;
    private SoActosInseguros duplicarSoActoInseguro;
    private SoActosInseguros editarSoActoInseguro;
    private SoActosInseguros soActoInseguroSeleccionado;
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
    private BigInteger verificarSoAccidentesMedicos;
    private int tamano;

    private String backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro;

    public ControlSoActosInseguros() {
        listSoActosInseguros = null;
        crearSoActosInseguros = new ArrayList<SoActosInseguros>();
        modificarSoActosInseguros = new ArrayList<SoActosInseguros>();
        borrarSoActosInseguros = new ArrayList<SoActosInseguros>();
        permitirIndex = true;
        editarSoActoInseguro = new SoActosInseguros();
        nuevaSoActoInseguro = new SoActosInseguros();
        duplicarSoActoInseguro = new SoActosInseguros();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSoActosInseguros.obtenerConexion(ses.getId());
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
            infoRegistro = "Cantidad de registros: " + filtrarSoActosInseguros.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");
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
                    backUpCodigo = listSoActosInseguros.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarSoActosInseguros.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listSoActosInseguros.get(index).getDescripcion();
                } else {
                    backUpDescripcion = filtrarSoActosInseguros.get(index).getDescripcion();
                }
            }
            secRegistro = listSoActosInseguros.get(index).getSecuencia();

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE CONTROLSOACTOSINSEGUROS  AsignarIndex \n");
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
            System.out.println("ERROR CONTROLSOACTOSINSEGUROS asignarIndex ERROR =" + e);
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoActosInseguros = null;
            tipoLista = 0;
        }

        borrarSoActosInseguros.clear();
        crearSoActosInseguros.clear();
        modificarSoActosInseguros.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSoActosInseguros = null;
        guardado = true;
        permitirIndex = true;
        getListSoActosInseguros();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoActosInseguros == null || listSoActosInseguros.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoActosInseguros.size();
        }
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoActosInseguros = null;
            tipoLista = 0;
        }

        borrarSoActosInseguros.clear();
        crearSoActosInseguros.clear();
        modificarSoActosInseguros.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSoActosInseguros = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoActosInseguros = null;
            tipoLista = 0;
        }
    }

    public void modificandoSoActosInseguros(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("SO ACTOS INSEGUROS");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Short a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("MODIFICANDO SO ACTO INSEGURO CONFIRMAR CAMBIO = N");
            if (tipoLista == 0) {
                if (!crearSoActosInseguros.contains(listSoActosInseguros.get(indice))) {
                    if (listSoActosInseguros.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                    } else if (listSoActosInseguros.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listSoActosInseguros.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSoActosInseguros.size(); j++) {
                            if (j != indice) {
                                if (listSoActosInseguros.get(indice).getCodigo().equals(listSoActosInseguros.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSoActosInseguros.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                        banderita = false;
                    }
                    if (listSoActosInseguros.get(indice).getDescripcion().equals(" ")) {
                        listSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarSoActosInseguros.isEmpty()) {
                            modificarSoActosInseguros.add(listSoActosInseguros.get(indice));
                        } else if (!modificarSoActosInseguros.contains(listSoActosInseguros.get(indice))) {
                            modificarSoActosInseguros.add(listSoActosInseguros.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listSoActosInseguros.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                    } else if (listSoActosInseguros.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listSoActosInseguros.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSoActosInseguros.size(); j++) {
                            if (j != indice) {
                                if (listSoActosInseguros.get(indice).getCodigo().equals(listSoActosInseguros.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSoActosInseguros.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                        banderita = false;
                    }
                    if (listSoActosInseguros.get(indice).getDescripcion().equals(" ")) {
                        listSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearSoActosInseguros.contains(filtrarSoActosInseguros.get(indice))) {
                    if (filtrarSoActosInseguros.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                    }
                    if (filtrarSoActosInseguros.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSoActosInseguros.size(); j++) {
                            if (filtrarSoActosInseguros.get(indice).getCodigo().equals(listSoActosInseguros.get(j).getCodigo())) {
                                contador++;
                            }
                        }

                        for (int j = 0; j < filtrarSoActosInseguros.size(); j++) {
                            if (j != indice) {
                                if (filtrarSoActosInseguros.get(indice).getCodigo().equals(filtrarSoActosInseguros.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarSoActosInseguros.get(indice).getDescripcion().isEmpty()) {
                        filtrarSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (filtrarSoActosInseguros.get(indice).getDescripcion().equals(" ")) {
                        filtrarSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarSoActosInseguros.isEmpty()) {
                            modificarSoActosInseguros.add(filtrarSoActosInseguros.get(indice));
                        } else if (!modificarSoActosInseguros.contains(filtrarSoActosInseguros.get(indice))) {
                            modificarSoActosInseguros.add(filtrarSoActosInseguros.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarSoActosInseguros.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                    }
                    if (filtrarSoActosInseguros.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSoActosInseguros.size(); j++) {
                            if (filtrarSoActosInseguros.get(indice).getCodigo().equals(listSoActosInseguros.get(j).getCodigo())) {
                                contador++;
                            }
                        }

                        for (int j = 0; j < filtrarSoActosInseguros.size(); j++) {
                            if (j != indice) {
                                if (filtrarSoActosInseguros.get(indice).getCodigo().equals(filtrarSoActosInseguros.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarSoActosInseguros.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarSoActosInseguros.get(indice).getDescripcion().isEmpty()) {
                        filtrarSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (filtrarSoActosInseguros.get(indice).getDescripcion().equals(" ")) {
                        filtrarSoActosInseguros.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }

    }

    public void borrandoSoActosInseguros() {

        RequestContext context = RequestContext.getCurrentInstance();

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("borrandoSoActosInseguros");
                if (!modificarSoActosInseguros.isEmpty() && modificarSoActosInseguros.contains(listSoActosInseguros.get(index))) {
                    int modIndex = modificarSoActosInseguros.indexOf(listSoActosInseguros.get(index));
                    modificarSoActosInseguros.remove(modIndex);
                    borrarSoActosInseguros.add(listSoActosInseguros.get(index));
                } else if (!crearSoActosInseguros.isEmpty() && crearSoActosInseguros.contains(listSoActosInseguros.get(index))) {
                    int crearIndex = crearSoActosInseguros.indexOf(listSoActosInseguros.get(index));
                    crearSoActosInseguros.remove(crearIndex);
                } else {
                    borrarSoActosInseguros.add(listSoActosInseguros.get(index));
                }
                listSoActosInseguros.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoSoActosInseguros");
                if (!modificarSoActosInseguros.isEmpty() && modificarSoActosInseguros.contains(filtrarSoActosInseguros.get(index))) {
                    int modIndex = modificarSoActosInseguros.indexOf(filtrarSoActosInseguros.get(index));
                    modificarSoActosInseguros.remove(modIndex);
                    borrarSoActosInseguros.add(filtrarSoActosInseguros.get(index));
                } else if (!crearSoActosInseguros.isEmpty() && crearSoActosInseguros.contains(filtrarSoActosInseguros.get(index))) {
                    int crearIndex = crearSoActosInseguros.indexOf(filtrarSoActosInseguros.get(index));
                    crearSoActosInseguros.remove(crearIndex);
                } else {
                    borrarSoActosInseguros.add(filtrarSoActosInseguros.get(index));
                }
                int VCIndex = listSoActosInseguros.indexOf(filtrarSoActosInseguros.get(index));
                listSoActosInseguros.remove(VCIndex);
                filtrarSoActosInseguros.remove(index);

            }
            PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
            infoRegistro = "Cantidad de registros: " + listSoActosInseguros.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("verificarBorrado");
        try {
            if (tipoLista == 0) {
                verificarSoAccidentesMedicos = administrarSoActosInseguros.verificarSoAccidentesMedicos(listSoActosInseguros.get(index).getSecuencia());
            } else {
                verificarSoAccidentesMedicos = administrarSoActosInseguros.verificarSoAccidentesMedicos(filtrarSoActosInseguros.get(index).getSecuencia());
            }
            if (verificarSoAccidentesMedicos.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoSoActosInseguros();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
                verificarSoAccidentesMedicos = new BigInteger("-1");
            }
        } catch (Exception e) {
            System.err.println("ERROR CLASES ACCIDENTES verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarSoActosInseguros.isEmpty() || !crearSoActosInseguros.isEmpty() || !modificarSoActosInseguros.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardandoSoActosInseguros() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("REALIZANDO SO ACTPS INSEGUROS");
            if (!borrarSoActosInseguros.isEmpty()) {
                administrarSoActosInseguros.borrarSoActosInseguros(borrarSoActosInseguros);
                //mostrarBorrados
                registrosBorrados = borrarSoActosInseguros.size();
                PrimefacesContextUI.actualizar("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarSoActosInseguros.clear();
            }
            if (!crearSoActosInseguros.isEmpty()) {
                administrarSoActosInseguros.crearSoActosInseguros(crearSoActosInseguros);
                crearSoActosInseguros.clear();
            }
            if (!modificarSoActosInseguros.isEmpty()) {
                administrarSoActosInseguros.modificarSoActosInseguros(modificarSoActosInseguros);
                modificarSoActosInseguros.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listSoActosInseguros = null;

            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
            PrimefacesContextUI.actualizar("form:datosActividades");
            PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
            k = 0;
            if (guardado == false) {
                guardado = true;
            }
        }
        index = -1;
        PrimefacesContextUI.actualizar("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarSoActoInseguro = listSoActosInseguros.get(index);
            }
            if (tipoLista == 1) {
                editarSoActoInseguro = filtrarSoActosInseguros.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editCodigo");
                PrimefacesContextUI.ejecutar("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editDescripcion");
                PrimefacesContextUI.ejecutar("PF('editDescripcion').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoSoActoInseguro() {
        System.out.println("agregarNuevoSoActoInseguro");
        int contador = 0;
        int duplicados = 0;

        Short a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaSoActoInseguro.getCodigo() == null) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevaSoActoInseguro.getCodigo());

            for (int x = 0; x < listSoActosInseguros.size(); x++) {
                if (listSoActosInseguros.get(x).getCodigo().equals(nuevaSoActoInseguro.getCodigo())) {
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
        if (nuevaSoActoInseguro.getDescripcion() == (null)) {
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
                PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
                bandera = 0;
                filtrarSoActosInseguros = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevaSoActoInseguro.setSecuencia(l);

            crearSoActosInseguros.add(nuevaSoActoInseguro);

            listSoActosInseguros.add(nuevaSoActoInseguro);
            nuevaSoActoInseguro = new SoActosInseguros();
            infoRegistro = "Cantidad de registros: " + listSoActosInseguros.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroSoCondicionesAmbientalesPeligrosaP').hide()");
            index = -1;
            secRegistro = null;

        } else {
            PrimefacesContextUI.actualizar("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoaSoActoInseguro() {
        System.out.println("limpiarNuevoaSoActoInseguro");
        nuevaSoActoInseguro = new SoActosInseguros();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoSoActoInseguro() {
        System.out.println("duplicandoSoActoInseguro");
        if (index >= 0) {
            duplicarSoActoInseguro = new SoActosInseguros();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarSoActoInseguro.setSecuencia(l);
                duplicarSoActoInseguro.setCodigo(listSoActosInseguros.get(index).getCodigo());
                duplicarSoActoInseguro.setDescripcion(listSoActosInseguros.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarSoActoInseguro.setSecuencia(l);
                duplicarSoActoInseguro.setCodigo(filtrarSoActosInseguros.get(index).getCodigo());
                duplicarSoActoInseguro.setDescripcion(filtrarSoActosInseguros.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarRCAP");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroSoCondicionesAmbientalesP').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("CONFIRMAR DUPLICAR SO ACTOS INSEGUROS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarSoActoInseguro.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarSoActoInseguro.getDescripcion());

        if (duplicarSoActoInseguro.getCodigo() == null || duplicarSoActoInseguro.getCodigo().equals(" ") || duplicarSoActoInseguro.getCodigo().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            for (int x = 0; x < listSoActosInseguros.size(); x++) {
                if (listSoActosInseguros.get(x).getCodigo().equals(duplicarSoActoInseguro.getCodigo())) {
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
        if (duplicarSoActoInseguro.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarSoActoInseguro.getSecuencia() + "  " + duplicarSoActoInseguro.getCodigo());
            if (crearSoActosInseguros.contains(duplicarSoActoInseguro)) {
                System.out.println("Ya lo contengo.");
            }
            listSoActosInseguros.add(duplicarSoActoInseguro);
            crearSoActosInseguros.add(duplicarSoActoInseguro);
            PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listSoActosInseguros.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosSoCondicionesAmbientalesP");
                bandera = 0;
                filtrarSoActosInseguros = null;
                tipoLista = 0;
            }
            duplicarSoActoInseguro = new SoActosInseguros();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroSoCondicionesAmbientalesP').hide()");

        } else {
            contador = 0;
            PrimefacesContextUI.actualizar("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarSoActosInseguros() {
        duplicarSoActoInseguro = new SoActosInseguros();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoCondicionesAmbientalesPExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ACTOSINSEGUROS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoCondicionesAmbientalesPExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ACTOSINSEGUROS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listSoActosInseguros.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "SOACTOSINSEGUROS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("SOACTOSINSEGUROS")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<SoActosInseguros> getListSoActosInseguros() {
        if (listSoActosInseguros == null) {
            listSoActosInseguros = administrarSoActosInseguros.consultarSoActosInseguros();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoActosInseguros == null || listSoActosInseguros.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoActosInseguros.size();
        }
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        return listSoActosInseguros;
    }

    public void setListSoActosInseguros(List<SoActosInseguros> listSoActosInseguros) {
        this.listSoActosInseguros = listSoActosInseguros;
    }

    public List<SoActosInseguros> getFiltrarSoActosInseguros() {
        return filtrarSoActosInseguros;
    }

    public void setFiltrarSoActosInseguros(List<SoActosInseguros> filtrarSoActosInseguros) {
        this.filtrarSoActosInseguros = filtrarSoActosInseguros;
    }

    public List<SoActosInseguros> getModificarSoActosInseguros() {
        return modificarSoActosInseguros;
    }

    public void setModificarSoActosInseguros(List<SoActosInseguros> modificarSoActosInseguros) {
        this.modificarSoActosInseguros = modificarSoActosInseguros;
    }

    public SoActosInseguros getNuevaSoActoInseguro() {
        return nuevaSoActoInseguro;
    }

    public void setNuevaSoActoInseguro(SoActosInseguros nuevaSoActoInseguro) {
        this.nuevaSoActoInseguro = nuevaSoActoInseguro;
    }

    public SoActosInseguros getDuplicarSoActoInseguro() {
        return duplicarSoActoInseguro;
    }

    public void setDuplicarSoActoInseguro(SoActosInseguros duplicarSoActoInseguro) {
        this.duplicarSoActoInseguro = duplicarSoActoInseguro;
    }

    public SoActosInseguros getEditarSoActoInseguro() {
        return editarSoActoInseguro;
    }

    public void setEditarSoActoInseguro(SoActosInseguros editarSoActoInseguro) {
        this.editarSoActoInseguro = editarSoActoInseguro;
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

    public SoActosInseguros getSoActoInseguroSeleccionado() {
        return soActoInseguroSeleccionado;
    }

    public void setSoActoInseguroSeleccionado(SoActosInseguros soActoInseguroSeleccionado) {
        this.soActoInseguroSeleccionado = soActoInseguroSeleccionado;
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
