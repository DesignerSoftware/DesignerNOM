/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.TiposTallas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposTallasInterface;
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
public class ControlTiposTallas implements Serializable {

    @EJB
    AdministrarTiposTallasInterface administrarTiposTallas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposTallas> listTiposTallas;
    private List<TiposTallas> filtrarTiposTallas;
    private List<TiposTallas> crearTiposTallas;
    private List<TiposTallas> modificarTiposTallas;
    private List<TiposTallas> borrarTiposTallas;
    private TiposTallas nuevoTiposTallas;
    private TiposTallas duplicarTiposTallas;
    private TiposTallas editarTiposTallas;
    private TiposTallas tiposTallasSeleccionado;
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
    private Integer backUpCodigo;
    private String backUpDescripcion;

    public ControlTiposTallas() {
        listTiposTallas = null;
        crearTiposTallas = new ArrayList<TiposTallas>();
        modificarTiposTallas = new ArrayList<TiposTallas>();
        borrarTiposTallas = new ArrayList<TiposTallas>();
        permitirIndex = true;
        editarTiposTallas = new TiposTallas();
        nuevoTiposTallas = new TiposTallas();
        duplicarTiposTallas = new TiposTallas();
        guardado = true;
        tamano = 270;
        System.out.println("controlTiposTallas Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposTallas PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposTallas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlTiposTallas.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposTallas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposTallas eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listTiposTallas.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listTiposTallas.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listTiposTallas.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarTiposTallas.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarTiposTallas.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarTiposTallas.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposTallas.asignarIndex \n");
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
            System.out.println("ERROR ControlTiposTallas.asignarIndex ERROR======" + e.getMessage());
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
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            bandera = 0;
            filtrarTiposTallas = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposTallas.clear();
        crearTiposTallas.clear();
        modificarTiposTallas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposTallas = null;
        guardado = true;
        permitirIndex = true;
        getListTiposTallas();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposTallas == null || listTiposTallas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposTallas.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosTiposTallas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            bandera = 0;
            filtrarTiposTallas = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposTallas.clear();
        crearTiposTallas.clear();
        modificarTiposTallas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposTallas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposTallas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            bandera = 0;
            filtrarTiposTallas = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposTallas(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearTiposTallas.contains(listTiposTallas.get(indice))) {
                    if (listTiposTallas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposTallas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposTallas.size(); j++) {
                            if (j != indice) {
                                if (listTiposTallas.get(indice).getCodigo().equals(listTiposTallas.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposTallas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposTallas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposTallas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposTallas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposTallas.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposTallas.isEmpty()) {
                            modificarTiposTallas.add(listTiposTallas.get(indice));
                        } else if (!modificarTiposTallas.contains(listTiposTallas.get(indice))) {
                            modificarTiposTallas.add(listTiposTallas.get(indice));
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
                    if (listTiposTallas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposTallas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposTallas.size(); j++) {
                            if (j != indice) {
                                if (listTiposTallas.get(indice).getCodigo().equals(listTiposTallas.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposTallas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposTallas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposTallas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposTallas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposTallas.get(indice).setDescripcion(backUpDescripcion);
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

                if (!crearTiposTallas.contains(filtrarTiposTallas.get(indice))) {
                    if (filtrarTiposTallas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposTallas.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < filtrarTiposTallas.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposTallas.get(indice).getCodigo().equals(filtrarTiposTallas.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposTallas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposTallas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposTallas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposTallas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposTallas.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposTallas.isEmpty()) {
                            modificarTiposTallas.add(filtrarTiposTallas.get(indice));
                        } else if (!modificarTiposTallas.contains(filtrarTiposTallas.get(indice))) {
                            modificarTiposTallas.add(filtrarTiposTallas.get(indice));
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
                    if (filtrarTiposTallas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposTallas.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < filtrarTiposTallas.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposTallas.get(indice).getCodigo().equals(filtrarTiposTallas.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposTallas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposTallas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposTallas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposTallas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposTallas.get(indice).setDescripcion(backUpDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposTallas() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposTallas");
                if (!modificarTiposTallas.isEmpty() && modificarTiposTallas.contains(listTiposTallas.get(index))) {
                    int modIndex = modificarTiposTallas.indexOf(listTiposTallas.get(index));
                    modificarTiposTallas.remove(modIndex);
                    borrarTiposTallas.add(listTiposTallas.get(index));
                } else if (!crearTiposTallas.isEmpty() && crearTiposTallas.contains(listTiposTallas.get(index))) {
                    int crearIndex = crearTiposTallas.indexOf(listTiposTallas.get(index));
                    crearTiposTallas.remove(crearIndex);
                } else {
                    borrarTiposTallas.add(listTiposTallas.get(index));
                }
                listTiposTallas.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposTallas ");
                if (!modificarTiposTallas.isEmpty() && modificarTiposTallas.contains(filtrarTiposTallas.get(index))) {
                    int modIndex = modificarTiposTallas.indexOf(filtrarTiposTallas.get(index));
                    modificarTiposTallas.remove(modIndex);
                    borrarTiposTallas.add(filtrarTiposTallas.get(index));
                } else if (!crearTiposTallas.isEmpty() && crearTiposTallas.contains(filtrarTiposTallas.get(index))) {
                    int crearIndex = crearTiposTallas.indexOf(filtrarTiposTallas.get(index));
                    crearTiposTallas.remove(crearIndex);
                } else {
                    borrarTiposTallas.add(filtrarTiposTallas.get(index));
                }
                int VCIndex = listTiposTallas.indexOf(filtrarTiposTallas.get(index));
                listTiposTallas.remove(VCIndex);
                filtrarTiposTallas.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            infoRegistro = "Cantidad de registros: " + listTiposTallas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

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
        BigInteger contarElementosTipoTalla;
        BigInteger contarVigenciasTallasTipoTalla;

        try {
            System.err.println("Control Secuencia de ControlTiposTallas ");
            if (tipoLista == 0) {
                contarElementosTipoTalla = administrarTiposTallas.contarElementosTipoTalla(listTiposTallas.get(index).getSecuencia());
                contarVigenciasTallasTipoTalla = administrarTiposTallas.contarVigenciasTallasTipoTalla(listTiposTallas.get(index).getSecuencia());
            } else {
                contarElementosTipoTalla = administrarTiposTallas.contarElementosTipoTalla(filtrarTiposTallas.get(index).getSecuencia());
                contarVigenciasTallasTipoTalla = administrarTiposTallas.contarVigenciasTallasTipoTalla(filtrarTiposTallas.get(index).getSecuencia());
            }
            if (contarElementosTipoTalla.equals(new BigInteger("0"))
                    && contarVigenciasTallasTipoTalla.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposTallas();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
                contarElementosTipoTalla = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposTallas verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposTallas.isEmpty() || !crearTiposTallas.isEmpty() || !modificarTiposTallas.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposTallas() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposTallas");
            if (!borrarTiposTallas.isEmpty()) {
                administrarTiposTallas.borrarTiposTallas(borrarTiposTallas);
                //mostrarBorrados
                registrosBorrados = borrarTiposTallas.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarTiposTallas.clear();
            }
            if (!modificarTiposTallas.isEmpty()) {
                administrarTiposTallas.modificarTiposTallas(modificarTiposTallas);
                modificarTiposTallas.clear();
            }
            if (!crearTiposTallas.isEmpty()) {
                administrarTiposTallas.crearTiposTallas(crearTiposTallas);
                crearTiposTallas.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposTallas = null;
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTiposTallas = listTiposTallas.get(index);
            }
            if (tipoLista == 1) {
                editarTiposTallas = filtrarTiposTallas.get(index);
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

    public void agregarNuevoTiposTallas() {
        System.out.println("agregarNuevoTiposTallas");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposTallas.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTiposTallas.getCodigo());

            for (int x = 0; x < listTiposTallas.size(); x++) {
                if (listTiposTallas.get(x).getCodigo().equals(nuevoTiposTallas.getCodigo())) {
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
        if (nuevoTiposTallas.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoTiposTallas.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposTallas");
                bandera = 0;
                filtrarTiposTallas = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposTallas.setSecuencia(l);

            crearTiposTallas.add(nuevoTiposTallas);

            listTiposTallas.add(nuevoTiposTallas);
            nuevoTiposTallas = new TiposTallas();
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            infoRegistro = "Cantidad de registros: " + listTiposTallas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroTiposTallas').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposTallas() {
        System.out.println("limpiarNuevoTiposTallas");
        nuevoTiposTallas = new TiposTallas();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposTallas() {
        System.out.println("duplicandoTiposTallas");
        if (index >= 0) {
            duplicarTiposTallas = new TiposTallas();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposTallas.setSecuencia(l);
                duplicarTiposTallas.setCodigo(listTiposTallas.get(index).getCodigo());
                duplicarTiposTallas.setDescripcion(listTiposTallas.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarTiposTallas.setSecuencia(l);
                duplicarTiposTallas.setCodigo(filtrarTiposTallas.get(index).getCodigo());
                duplicarTiposTallas.setDescripcion(filtrarTiposTallas.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroTiposTallas').show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposTallas.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposTallas.getDescripcion());

        if (duplicarTiposTallas.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposTallas.size(); x++) {
                if (listTiposTallas.get(x).getCodigo().equals(duplicarTiposTallas.getCodigo())) {
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
        if (duplicarTiposTallas.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarTiposTallas.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTiposTallas.getSecuencia() + "  " + duplicarTiposTallas.getCodigo());
            if (crearTiposTallas.contains(duplicarTiposTallas)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposTallas.add(duplicarTiposTallas);
            crearTiposTallas.add(duplicarTiposTallas);
            RequestContext.getCurrentInstance().update("form:datosTiposTallas");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listTiposTallas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposTallas:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposTallas");
                bandera = 0;
                filtrarTiposTallas = null;
                tipoLista = 0;
            }
            duplicarTiposTallas = new TiposTallas();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroTiposTallas').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposTallas() {
        duplicarTiposTallas = new TiposTallas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposTallasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSTALLAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposTallasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSTALLAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposTallas.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSTALLAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("TIPOSTALLAS")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposTallas> getListTiposTallas() {
        if (listTiposTallas == null) {
            System.out.println("ControlTiposTallas getListTiposTallas");
            listTiposTallas = administrarTiposTallas.consultarTiposTallas();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposTallas == null || listTiposTallas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposTallas.size();
        }
        return listTiposTallas;
    }

    public void setListTiposTallas(List<TiposTallas> listTiposTallas) {
        this.listTiposTallas = listTiposTallas;
    }

    public List<TiposTallas> getFiltrarTiposTallas() {
        return filtrarTiposTallas;
    }

    public void setFiltrarTiposTallas(List<TiposTallas> filtrarTiposTallas) {
        this.filtrarTiposTallas = filtrarTiposTallas;
    }

    public TiposTallas getNuevoTiposTallas() {
        return nuevoTiposTallas;
    }

    public void setNuevoTiposTallas(TiposTallas nuevoTiposTallas) {
        this.nuevoTiposTallas = nuevoTiposTallas;
    }

    public TiposTallas getDuplicarTiposTallas() {
        return duplicarTiposTallas;
    }

    public void setDuplicarTiposTallas(TiposTallas duplicarTiposTallas) {
        this.duplicarTiposTallas = duplicarTiposTallas;
    }

    public TiposTallas getEditarTiposTallas() {
        return editarTiposTallas;
    }

    public void setEditarTiposTallas(TiposTallas editarTiposTallas) {
        this.editarTiposTallas = editarTiposTallas;
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

    public TiposTallas getTiposTallasSeleccionado() {
        return tiposTallasSeleccionado;
    }

    public void setTiposTallasSeleccionado(TiposTallas clasesPensionesSeleccionado) {
        this.tiposTallasSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
