/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Tipospagos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposPagosInterface;
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
public class ControlTiposPagos implements Serializable {

    @EJB
    AdministrarTiposPagosInterface administrarTiposPagos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Tipospagos> listTiposPagos;
    private List<Tipospagos> filtrarTiposPagos;
    private List<Tipospagos> crearTiposPagos;
    private List<Tipospagos> modificarTiposPagos;
    private List<Tipospagos> borrarTiposPagos;
    private Tipospagos nuevoTiposPagos;
    private Tipospagos duplicarTiposPagos;
    private Tipospagos editarTiposPagos;
    private Tipospagos tipospagosSeleccionado;
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

    public ControlTiposPagos() {
        listTiposPagos = null;
        crearTiposPagos = new ArrayList<Tipospagos>();
        modificarTiposPagos = new ArrayList<Tipospagos>();
        borrarTiposPagos = new ArrayList<Tipospagos>();
        permitirIndex = true;
        editarTiposPagos = new Tipospagos();
        nuevoTiposPagos = new Tipospagos();
        duplicarTiposPagos = new Tipospagos();
        guardado = true;
        tamano = 270;
        System.out.println("controlTiposPagos Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposPagos PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposPagos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlTiposPagos.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposPagos.size();
            context.update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposPagos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listTiposPagos.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listTiposPagos.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listTiposPagos.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarTiposPagos.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarTiposPagos.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarTiposPagos.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposPagos.asignarIndex \n");
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
            System.out.println("ERROR ControlTiposPagos.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposPagos");
            bandera = 0;
            filtrarTiposPagos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposPagos.clear();
        crearTiposPagos.clear();
        modificarTiposPagos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposPagos = null;
        guardado = true;
        permitirIndex = true;
        getListTiposPagos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposPagos == null || listTiposPagos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposPagos.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosTiposPagos");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposPagos");
            bandera = 0;
            filtrarTiposPagos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposPagos.clear();
        crearTiposPagos.clear();
        modificarTiposPagos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposPagos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosTiposPagos");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosTiposPagos");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposPagos");
            bandera = 0;
            filtrarTiposPagos = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposPagos(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearTiposPagos.contains(listTiposPagos.get(indice))) {
                    if (listTiposPagos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPagos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposPagos.size(); j++) {
                            if (j != indice) {
                                if (listTiposPagos.get(indice).getCodigo().equals(listTiposPagos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposPagos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposPagos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPagos.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposPagos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPagos.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposPagos.isEmpty()) {
                            modificarTiposPagos.add(listTiposPagos.get(indice));
                        } else if (!modificarTiposPagos.contains(listTiposPagos.get(indice))) {
                            modificarTiposPagos.add(listTiposPagos.get(indice));
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
                    if (listTiposPagos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPagos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposPagos.size(); j++) {
                            if (j != indice) {
                                if (listTiposPagos.get(indice).getCodigo().equals(listTiposPagos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposPagos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposPagos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPagos.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposPagos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPagos.get(indice).setDescripcion(backUpDescripcion);
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

                if (!crearTiposPagos.contains(filtrarTiposPagos.get(indice))) {
                    if (filtrarTiposPagos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposPagos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < filtrarTiposPagos.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposPagos.get(indice).getCodigo().equals(filtrarTiposPagos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposPagos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposPagos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposPagos.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposPagos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposPagos.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposPagos.isEmpty()) {
                            modificarTiposPagos.add(filtrarTiposPagos.get(indice));
                        } else if (!modificarTiposPagos.contains(filtrarTiposPagos.get(indice))) {
                            modificarTiposPagos.add(filtrarTiposPagos.get(indice));
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
                    if (filtrarTiposPagos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposPagos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                       for (int j = 0; j < filtrarTiposPagos.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposPagos.get(indice).getCodigo().equals(filtrarTiposPagos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposPagos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposPagos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposPagos.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposPagos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposPagos.get(indice).setDescripcion(backUpDescripcion);
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
            context.update("form:datosTiposPagos");
            context.update("form:ACEPTAR");
        }

    }

    public void borrandoTiposPagos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposPagos");
                if (!modificarTiposPagos.isEmpty() && modificarTiposPagos.contains(listTiposPagos.get(index))) {
                    int modIndex = modificarTiposPagos.indexOf(listTiposPagos.get(index));
                    modificarTiposPagos.remove(modIndex);
                    borrarTiposPagos.add(listTiposPagos.get(index));
                } else if (!crearTiposPagos.isEmpty() && crearTiposPagos.contains(listTiposPagos.get(index))) {
                    int crearIndex = crearTiposPagos.indexOf(listTiposPagos.get(index));
                    crearTiposPagos.remove(crearIndex);
                } else {
                    borrarTiposPagos.add(listTiposPagos.get(index));
                }
                listTiposPagos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposPagos ");
                if (!modificarTiposPagos.isEmpty() && modificarTiposPagos.contains(filtrarTiposPagos.get(index))) {
                    int modIndex = modificarTiposPagos.indexOf(filtrarTiposPagos.get(index));
                    modificarTiposPagos.remove(modIndex);
                    borrarTiposPagos.add(filtrarTiposPagos.get(index));
                } else if (!crearTiposPagos.isEmpty() && crearTiposPagos.contains(filtrarTiposPagos.get(index))) {
                    int crearIndex = crearTiposPagos.indexOf(filtrarTiposPagos.get(index));
                    crearTiposPagos.remove(crearIndex);
                } else {
                    borrarTiposPagos.add(filtrarTiposPagos.get(index));
                }
                int VCIndex = listTiposPagos.indexOf(filtrarTiposPagos.get(index));
                listTiposPagos.remove(VCIndex);
                filtrarTiposPagos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosTiposPagos");
            infoRegistro = "Cantidad de registros: " + listTiposPagos.size();
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
        BigInteger contarRetiradosClasePension;

        try {
            System.err.println("Control Secuencia de ControlTiposPagos ");
            if (tipoLista == 0) {
                contarRetiradosClasePension = administrarTiposPagos.contarProcesosTipoPago(listTiposPagos.get(index).getSecuencia());
            } else {
                contarRetiradosClasePension = administrarTiposPagos.contarProcesosTipoPago(filtrarTiposPagos.get(index).getSecuencia());
            }
            if (contarRetiradosClasePension.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposPagos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                index = -1;
                contarRetiradosClasePension = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposPagos verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposPagos.isEmpty() || !crearTiposPagos.isEmpty() || !modificarTiposPagos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void guardarTiposPagos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposPagos");
            if (!borrarTiposPagos.isEmpty()) {
                administrarTiposPagos.borrarTiposPagos(borrarTiposPagos);
                //mostrarBorrados
                registrosBorrados = borrarTiposPagos.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarTiposPagos.clear();
            }
            if (!modificarTiposPagos.isEmpty()) {
                administrarTiposPagos.modificarTiposPagos(modificarTiposPagos);
                modificarTiposPagos.clear();
            }
            if (!crearTiposPagos.isEmpty()) {
                administrarTiposPagos.crearTiposPagos(crearTiposPagos);
                crearTiposPagos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposPagos = null;
            context.update("form:datosTiposPagos");
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
                editarTiposPagos = listTiposPagos.get(index);
            }
            if (tipoLista == 1) {
                editarTiposPagos = filtrarTiposPagos.get(index);
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

    public void agregarNuevoTiposPagos() {
        System.out.println("agregarNuevoTiposPagos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposPagos.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTiposPagos.getCodigo());

            for (int x = 0; x < listTiposPagos.size(); x++) {
                if (listTiposPagos.get(x).getCodigo().equals(nuevoTiposPagos.getCodigo())) {
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
        if (nuevoTiposPagos.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoTiposPagos.getDescripcion().isEmpty()) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposPagos");
                bandera = 0;
                filtrarTiposPagos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposPagos.setSecuencia(l);

            crearTiposPagos.add(nuevoTiposPagos);

            listTiposPagos.add(nuevoTiposPagos);
            nuevoTiposPagos = new Tipospagos();
            context.update("form:datosTiposPagos");
            infoRegistro = "Cantidad de registros: " + listTiposPagos.size();
            context.update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroTiposPagos.hide()");
            index = -1;
            secRegistro = null;

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposPagos() {
        System.out.println("limpiarNuevoTiposPagos");
        nuevoTiposPagos = new Tipospagos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposPagos() {
        System.out.println("duplicandoTiposPagos");
        if (index >= 0) {
            duplicarTiposPagos = new Tipospagos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposPagos.setSecuencia(l);
                duplicarTiposPagos.setCodigo(listTiposPagos.get(index).getCodigo());
                duplicarTiposPagos.setDescripcion(listTiposPagos.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarTiposPagos.setSecuencia(l);
                duplicarTiposPagos.setCodigo(filtrarTiposPagos.get(index).getCodigo());
                duplicarTiposPagos.setDescripcion(filtrarTiposPagos.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarTE");
            context.execute("duplicarRegistroTiposPagos.show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposPagos.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposPagos.getDescripcion());

        if (duplicarTiposPagos.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposPagos.size(); x++) {
                if (listTiposPagos.get(x).getCodigo().equals(duplicarTiposPagos.getCodigo())) {
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
        if (duplicarTiposPagos.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarTiposPagos.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTiposPagos.getSecuencia() + "  " + duplicarTiposPagos.getCodigo());
            if (crearTiposPagos.contains(duplicarTiposPagos)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposPagos.add(duplicarTiposPagos);
            crearTiposPagos.add(duplicarTiposPagos);
            context.update("form:datosTiposPagos");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listTiposPagos.size();
            context.update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPagos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposPagos");
                bandera = 0;
                filtrarTiposPagos = null;
                tipoLista = 0;
            }
            duplicarTiposPagos = new Tipospagos();
            RequestContext.getCurrentInstance().execute("duplicarRegistroTiposPagos.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    public void limpiarDuplicarTiposPagos() {
        duplicarTiposPagos = new Tipospagos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposPagosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSPAGOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposPagosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSPAGOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposPagos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSPAGOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("TIPOSPAGOS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Tipospagos> getListTiposPagos() {
        if (listTiposPagos == null) {
            System.out.println("ControlTiposPagos getListTiposPagos");
            listTiposPagos = administrarTiposPagos.consultarTiposPagos();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposPagos == null || listTiposPagos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposPagos.size();
        }
        return listTiposPagos;
    }

    public void setListTiposPagos(List<Tipospagos> listTiposPagos) {
        this.listTiposPagos = listTiposPagos;
    }

    public List<Tipospagos> getFiltrarTiposPagos() {
        return filtrarTiposPagos;
    }

    public void setFiltrarTiposPagos(List<Tipospagos> filtrarTiposPagos) {
        this.filtrarTiposPagos = filtrarTiposPagos;
    }

    public Tipospagos getNuevoTiposPagos() {
        return nuevoTiposPagos;
    }

    public void setNuevoTiposPagos(Tipospagos nuevoTiposPagos) {
        this.nuevoTiposPagos = nuevoTiposPagos;
    }

    public Tipospagos getDuplicarTiposPagos() {
        return duplicarTiposPagos;
    }

    public void setDuplicarTiposPagos(Tipospagos duplicarTiposPagos) {
        this.duplicarTiposPagos = duplicarTiposPagos;
    }

    public Tipospagos getEditarTiposPagos() {
        return editarTiposPagos;
    }

    public void setEditarTiposPagos(Tipospagos editarTiposPagos) {
        this.editarTiposPagos = editarTiposPagos;
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

    public Tipospagos getTiposPagosSeleccionado() {
        return tipospagosSeleccionado;
    }

    public void setTiposPagosSeleccionado(Tipospagos clasesPensionesSeleccionado) {
        this.tipospagosSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
