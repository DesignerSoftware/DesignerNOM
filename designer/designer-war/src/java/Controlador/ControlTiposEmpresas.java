/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposEmpresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposEmpresasInterface;
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
public class ControlTiposEmpresas implements Serializable {

    @EJB
    AdministrarTiposEmpresasInterface administrarTiposEmpresas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposEmpresas> listTiposEmpresas;
    private List<TiposEmpresas> filtrarTiposEmpresas;
    private List<TiposEmpresas> crearTiposEmpresas;
    private List<TiposEmpresas> modificarTiposEmpresas;
    private List<TiposEmpresas> borrarTiposEmpresas;
    private TiposEmpresas nuevoTiposEmpresas;
    private TiposEmpresas duplicarTiposEmpresas;
    private TiposEmpresas editarTiposEmpresas;
    private TiposEmpresas tiposEmpresasSeleccionado;
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

    public ControlTiposEmpresas() {
        listTiposEmpresas = null;
        crearTiposEmpresas = new ArrayList<TiposEmpresas>();
        modificarTiposEmpresas = new ArrayList<TiposEmpresas>();
        borrarTiposEmpresas = new ArrayList<TiposEmpresas>();
        permitirIndex = true;
        editarTiposEmpresas = new TiposEmpresas();
        nuevoTiposEmpresas = new TiposEmpresas();
        duplicarTiposEmpresas = new TiposEmpresas();
        guardado = true;
        tamano = 270;
        System.out.println("controlTiposEmpresas Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposEmpresas PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposEmpresas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlTiposEmpresas.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }  RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposEmpresas.size();
            context.update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposEmpresas eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listTiposEmpresas.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listTiposEmpresas.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listTiposEmpresas.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarTiposEmpresas.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarTiposEmpresas.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarTiposEmpresas.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposEmpresas.asignarIndex \n");
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
            System.out.println("ERROR ControlTiposEmpresas.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
            bandera = 0;
            filtrarTiposEmpresas = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposEmpresas.clear();
        crearTiposEmpresas.clear();
        modificarTiposEmpresas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposEmpresas = null;
        guardado = true;
        permitirIndex = true;
        getListTiposEmpresas();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposEmpresas == null || listTiposEmpresas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposEmpresas.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosTiposEmpresas");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
            bandera = 0;
            filtrarTiposEmpresas = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposEmpresas.clear();
        crearTiposEmpresas.clear();
        modificarTiposEmpresas.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposEmpresas = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosTiposEmpresas");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
            bandera = 0;
            filtrarTiposEmpresas = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposEmpresas(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearTiposEmpresas.contains(listTiposEmpresas.get(indice))) {
                    if (listTiposEmpresas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposEmpresas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposEmpresas.size(); j++) {
                            if (j != indice) {
                                if (listTiposEmpresas.get(indice).getCodigo() == listTiposEmpresas.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposEmpresas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposEmpresas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposEmpresas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposEmpresas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposEmpresas.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposEmpresas.isEmpty()) {
                            modificarTiposEmpresas.add(listTiposEmpresas.get(indice));
                        } else if (!modificarTiposEmpresas.contains(listTiposEmpresas.get(indice))) {
                            modificarTiposEmpresas.add(listTiposEmpresas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listTiposEmpresas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposEmpresas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposEmpresas.size(); j++) {
                            if (j != indice) {
                                if (listTiposEmpresas.get(indice).getCodigo() == listTiposEmpresas.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposEmpresas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposEmpresas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposEmpresas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposEmpresas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposEmpresas.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearTiposEmpresas.contains(filtrarTiposEmpresas.get(indice))) {
                    if (filtrarTiposEmpresas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposEmpresas.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < listTiposEmpresas.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposEmpresas.get(indice).getCodigo() == listTiposEmpresas.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposEmpresas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposEmpresas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposEmpresas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposEmpresas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposEmpresas.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposEmpresas.isEmpty()) {
                            modificarTiposEmpresas.add(filtrarTiposEmpresas.get(indice));
                        } else if (!modificarTiposEmpresas.contains(filtrarTiposEmpresas.get(indice))) {
                            modificarTiposEmpresas.add(filtrarTiposEmpresas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarTiposEmpresas.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposEmpresas.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < listTiposEmpresas.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposEmpresas.get(indice).getCodigo() == listTiposEmpresas.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposEmpresas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposEmpresas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposEmpresas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposEmpresas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposEmpresas.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            context.update("form:datosTiposEmpresas");
            context.update("form:ACEPTAR");
        }

    }

    public void borrandoTiposEmpresas() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposEmpresas");
                if (!modificarTiposEmpresas.isEmpty() && modificarTiposEmpresas.contains(listTiposEmpresas.get(index))) {
                    int modIndex = modificarTiposEmpresas.indexOf(listTiposEmpresas.get(index));
                    modificarTiposEmpresas.remove(modIndex);
                    borrarTiposEmpresas.add(listTiposEmpresas.get(index));
                } else if (!crearTiposEmpresas.isEmpty() && crearTiposEmpresas.contains(listTiposEmpresas.get(index))) {
                    int crearIndex = crearTiposEmpresas.indexOf(listTiposEmpresas.get(index));
                    crearTiposEmpresas.remove(crearIndex);
                } else {
                    borrarTiposEmpresas.add(listTiposEmpresas.get(index));
                }
                listTiposEmpresas.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposEmpresas ");
                if (!modificarTiposEmpresas.isEmpty() && modificarTiposEmpresas.contains(filtrarTiposEmpresas.get(index))) {
                    int modIndex = modificarTiposEmpresas.indexOf(filtrarTiposEmpresas.get(index));
                    modificarTiposEmpresas.remove(modIndex);
                    borrarTiposEmpresas.add(filtrarTiposEmpresas.get(index));
                } else if (!crearTiposEmpresas.isEmpty() && crearTiposEmpresas.contains(filtrarTiposEmpresas.get(index))) {
                    int crearIndex = crearTiposEmpresas.indexOf(filtrarTiposEmpresas.get(index));
                    crearTiposEmpresas.remove(crearIndex);
                } else {
                    borrarTiposEmpresas.add(filtrarTiposEmpresas.get(index));
                }
                int VCIndex = listTiposEmpresas.indexOf(filtrarTiposEmpresas.get(index));
                listTiposEmpresas.remove(VCIndex);
                filtrarTiposEmpresas.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosTiposEmpresas");
            infoRegistro = "Cantidad de registros: " + listTiposEmpresas.size();
            context.update("form:informacionRegistro");

            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        BigInteger contarSueldosMercadosTipoEmpresa;

        try {
            System.err.println("Control Secuencia de ControlTiposEmpresas ");
            if (tipoLista == 0) {
                contarSueldosMercadosTipoEmpresa = administrarTiposEmpresas.contarSueldosMercadosTipoEmpresa(listTiposEmpresas.get(index).getSecuencia());
            } else {
                contarSueldosMercadosTipoEmpresa = administrarTiposEmpresas.contarSueldosMercadosTipoEmpresa(filtrarTiposEmpresas.get(index).getSecuencia());
            }
            if (contarSueldosMercadosTipoEmpresa.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposEmpresas();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                index = -1;
                contarSueldosMercadosTipoEmpresa = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposEmpresas verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposEmpresas.isEmpty() || !crearTiposEmpresas.isEmpty() || !modificarTiposEmpresas.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void guardarTiposEmpresas() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposEmpresas");
            if (!borrarTiposEmpresas.isEmpty()) {
                administrarTiposEmpresas.borrarTiposEmpresas(borrarTiposEmpresas);
                //mostrarBorrados
                registrosBorrados = borrarTiposEmpresas.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarTiposEmpresas.clear();
            }
            if (!modificarTiposEmpresas.isEmpty()) {
                administrarTiposEmpresas.modificarTiposEmpresas(modificarTiposEmpresas);
                modificarTiposEmpresas.clear();
            }
            if (!crearTiposEmpresas.isEmpty()) {
                administrarTiposEmpresas.crearTiposEmpresas(crearTiposEmpresas);
                crearTiposEmpresas.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposEmpresas = null;
            context.update("form:datosTiposEmpresas");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTiposEmpresas = listTiposEmpresas.get(index);
            }
            if (tipoLista == 1) {
                editarTiposEmpresas = filtrarTiposEmpresas.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                context.update("formularioDialogos:editCodigo");
                context.execute("editCodigo.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editDescripcion");
                context.execute("editDescripcion.show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoTiposEmpresas() {
        System.out.println("agregarNuevoTiposEmpresas");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposEmpresas.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTiposEmpresas.getCodigo());

            for (int x = 0; x < listTiposEmpresas.size(); x++) {
                if (listTiposEmpresas.get(x).getCodigo() == nuevoTiposEmpresas.getCodigo()) {
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
        if (nuevoTiposEmpresas.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoTiposEmpresas.getDescripcion().isEmpty()) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
                bandera = 0;
                filtrarTiposEmpresas = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposEmpresas.setSecuencia(l);

            crearTiposEmpresas.add(nuevoTiposEmpresas);

            listTiposEmpresas.add(nuevoTiposEmpresas);
            nuevoTiposEmpresas = new TiposEmpresas();
            context.update("form:datosTiposEmpresas");
            infoRegistro = "Cantidad de registros: " + listTiposEmpresas.size();
            context.update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroTiposEmpresas.hide()");
            index = -1;
            secRegistro = null;

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposEmpresas() {
        System.out.println("limpiarNuevoTiposEmpresas");
        nuevoTiposEmpresas = new TiposEmpresas();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposEmpresas() {
        System.out.println("duplicandoTiposEmpresas");
        if (index >= 0) {
            duplicarTiposEmpresas = new TiposEmpresas();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposEmpresas.setSecuencia(l);
                duplicarTiposEmpresas.setCodigo(listTiposEmpresas.get(index).getCodigo());
                duplicarTiposEmpresas.setDescripcion(listTiposEmpresas.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarTiposEmpresas.setSecuencia(l);
                duplicarTiposEmpresas.setCodigo(filtrarTiposEmpresas.get(index).getCodigo());
                duplicarTiposEmpresas.setDescripcion(filtrarTiposEmpresas.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarTE");
            context.execute("duplicarRegistroTiposEmpresas.show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposEmpresas.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposEmpresas.getDescripcion());

        if (duplicarTiposEmpresas.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposEmpresas.size(); x++) {
                if (listTiposEmpresas.get(x).getCodigo() == duplicarTiposEmpresas.getCodigo()) {
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
        if (duplicarTiposEmpresas.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarTiposEmpresas.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTiposEmpresas.getSecuencia() + "  " + duplicarTiposEmpresas.getCodigo());
            if (crearTiposEmpresas.contains(duplicarTiposEmpresas)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposEmpresas.add(duplicarTiposEmpresas);
            crearTiposEmpresas.add(duplicarTiposEmpresas);
            context.update("form:datosTiposEmpresas");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listTiposEmpresas.size();
            context.update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposEmpresas:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposEmpresas");
                bandera = 0;
                filtrarTiposEmpresas = null;
                tipoLista = 0;
            }
            duplicarTiposEmpresas = new TiposEmpresas();
            RequestContext.getCurrentInstance().execute("duplicarRegistroTiposEmpresas.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    public void limpiarDuplicarTiposEmpresas() {
        duplicarTiposEmpresas = new TiposEmpresas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposEmpresasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSEMPRESAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposEmpresasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSEMPRESAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposEmpresas.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSEMPRESAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                System.out.println("resultado: " + resultado);
                if (resultado == 1) {
                    context.execute("errorObjetosDB.show()");
                } else if (resultado == 2) {
                    context.execute("confirmarRastro.show()");
                } else if (resultado == 3) {
                    context.execute("errorRegistroRastro.show()");
                } else if (resultado == 4) {
                    context.execute("errorTablaConRastro.show()");
                } else if (resultado == 5) {
                    context.execute("errorTablaSinRastro.show()");
                }
            } else {
                context.execute("seleccionarRegistro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TIPOSEMPRESAS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposEmpresas> getListTiposEmpresas() {
        if (listTiposEmpresas == null) {
            System.out.println("ControlTiposEmpresas getListTiposEmpresas");
            listTiposEmpresas = administrarTiposEmpresas.consultarTiposEmpresas();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposEmpresas == null || listTiposEmpresas.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposEmpresas.size();
        }
        return listTiposEmpresas;
    }

    public void setListTiposEmpresas(List<TiposEmpresas> listTiposEmpresas) {
        this.listTiposEmpresas = listTiposEmpresas;
    }

    public List<TiposEmpresas> getFiltrarTiposEmpresas() {
        return filtrarTiposEmpresas;
    }

    public void setFiltrarTiposEmpresas(List<TiposEmpresas> filtrarTiposEmpresas) {
        this.filtrarTiposEmpresas = filtrarTiposEmpresas;
    }

    public TiposEmpresas getNuevoTiposEmpresas() {
        return nuevoTiposEmpresas;
    }

    public void setNuevoTiposEmpresas(TiposEmpresas nuevoTiposEmpresas) {
        this.nuevoTiposEmpresas = nuevoTiposEmpresas;
    }

    public TiposEmpresas getDuplicarTiposEmpresas() {
        return duplicarTiposEmpresas;
    }

    public void setDuplicarTiposEmpresas(TiposEmpresas duplicarTiposEmpresas) {
        this.duplicarTiposEmpresas = duplicarTiposEmpresas;
    }

    public TiposEmpresas getEditarTiposEmpresas() {
        return editarTiposEmpresas;
    }

    public void setEditarTiposEmpresas(TiposEmpresas editarTiposEmpresas) {
        this.editarTiposEmpresas = editarTiposEmpresas;
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

    public TiposEmpresas getTiposEmpresasSeleccionado() {
        return tiposEmpresasSeleccionado;
    }

    public void setTiposEmpresasSeleccionado(TiposEmpresas tiposEmpresasSeleccionado) {
        this.tiposEmpresasSeleccionado = tiposEmpresasSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
