/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.PartesCuerpo;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPartesCuerpoInterface;
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
public class ControlPartesCuerpo implements Serializable {

    @EJB
    AdministrarPartesCuerpoInterface administrarPartesCuerpo;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<PartesCuerpo> listPartesCuerpo;
    private List<PartesCuerpo> filtrarPartesCuerpo;
    private List<PartesCuerpo> crearPartesCuerpo;
    private List<PartesCuerpo> modificarPartesCuerpo;
    private List<PartesCuerpo> borrarPartesCuerpo;
    private PartesCuerpo nuevoPartesCuerpo;
    private PartesCuerpo duplicarPartesCuerpo;
    private PartesCuerpo editarPartesCuerpo;
    private PartesCuerpo parteCuerpoSeleccionado;
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

    public ControlPartesCuerpo() {
        listPartesCuerpo = null;
        crearPartesCuerpo = new ArrayList<PartesCuerpo>();
        modificarPartesCuerpo = new ArrayList<PartesCuerpo>();
        borrarPartesCuerpo = new ArrayList<PartesCuerpo>();
        permitirIndex = true;
        editarPartesCuerpo = new PartesCuerpo();
        nuevoPartesCuerpo = new PartesCuerpo();
        duplicarPartesCuerpo = new PartesCuerpo();
        guardado = true;
        tamano = 270;
        System.out.println("controlPartesCuerpo Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlPartesCuerpo PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPartesCuerpo.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlPartesCuerpo.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarPartesCuerpo.size();
            context.update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlPartesCuerpo eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listPartesCuerpo.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listPartesCuerpo.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listPartesCuerpo.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarPartesCuerpo.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarPartesCuerpo.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarPartesCuerpo.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlPartesCuerpo.asignarIndex \n");
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
            System.out.println("ERROR ControlPartesCuerpo.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            bandera = 0;
            filtrarPartesCuerpo = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarPartesCuerpo.clear();
        crearPartesCuerpo.clear();
        modificarPartesCuerpo.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listPartesCuerpo = null;
        guardado = true;
        permitirIndex = true;
        getListPartesCuerpo();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listPartesCuerpo == null || listPartesCuerpo.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listPartesCuerpo.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosPartesCuerpo");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            bandera = 0;
            filtrarPartesCuerpo = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarPartesCuerpo.clear();
        crearPartesCuerpo.clear();
        modificarPartesCuerpo.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listPartesCuerpo = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosPartesCuerpo");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            bandera = 0;
            filtrarPartesCuerpo = null;
            tipoLista = 0;
        }
    }

    public void modificarPartesCuerpo(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearPartesCuerpo.contains(listPartesCuerpo.get(indice))) {
                    if (listPartesCuerpo.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listPartesCuerpo.size(); j++) {
                            if (j != indice) {
                                if (listPartesCuerpo.get(indice).getCodigo().equals(listPartesCuerpo.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listPartesCuerpo.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listPartesCuerpo.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarPartesCuerpo.isEmpty()) {
                            modificarPartesCuerpo.add(listPartesCuerpo.get(indice));
                        } else if (!modificarPartesCuerpo.contains(listPartesCuerpo.get(indice))) {
                            modificarPartesCuerpo.add(listPartesCuerpo.get(indice));
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
                    if (listPartesCuerpo.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listPartesCuerpo.size(); j++) {
                            if (j != indice) {
                                if (listPartesCuerpo.get(indice).getCodigo().equals(listPartesCuerpo.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listPartesCuerpo.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listPartesCuerpo.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
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

                if (!crearPartesCuerpo.contains(filtrarPartesCuerpo.get(indice))) {
                    if (filtrarPartesCuerpo.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < filtrarPartesCuerpo.size(); j++) {
                            if (j != indice) {
                                if (filtrarPartesCuerpo.get(indice).getCodigo().equals(filtrarPartesCuerpo.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarPartesCuerpo.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarPartesCuerpo.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarPartesCuerpo.isEmpty()) {
                            modificarPartesCuerpo.add(filtrarPartesCuerpo.get(indice));
                        } else if (!modificarPartesCuerpo.contains(filtrarPartesCuerpo.get(indice))) {
                            modificarPartesCuerpo.add(filtrarPartesCuerpo.get(indice));
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
                    if (filtrarPartesCuerpo.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < filtrarPartesCuerpo.size(); j++) {
                            if (j != indice) {
                                if (filtrarPartesCuerpo.get(indice).getCodigo().equals(filtrarPartesCuerpo.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarPartesCuerpo.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarPartesCuerpo.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
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
            context.update("form:datosPartesCuerpo");
            context.update("form:ACEPTAR");
        }

    }

    public void borrandoPartesCuerpo() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoPartesCuerpo");
                if (!modificarPartesCuerpo.isEmpty() && modificarPartesCuerpo.contains(listPartesCuerpo.get(index))) {
                    int modIndex = modificarPartesCuerpo.indexOf(listPartesCuerpo.get(index));
                    modificarPartesCuerpo.remove(modIndex);
                    borrarPartesCuerpo.add(listPartesCuerpo.get(index));
                } else if (!crearPartesCuerpo.isEmpty() && crearPartesCuerpo.contains(listPartesCuerpo.get(index))) {
                    int crearIndex = crearPartesCuerpo.indexOf(listPartesCuerpo.get(index));
                    crearPartesCuerpo.remove(crearIndex);
                } else {
                    borrarPartesCuerpo.add(listPartesCuerpo.get(index));
                }
                listPartesCuerpo.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoPartesCuerpo ");
                if (!modificarPartesCuerpo.isEmpty() && modificarPartesCuerpo.contains(filtrarPartesCuerpo.get(index))) {
                    int modIndex = modificarPartesCuerpo.indexOf(filtrarPartesCuerpo.get(index));
                    modificarPartesCuerpo.remove(modIndex);
                    borrarPartesCuerpo.add(filtrarPartesCuerpo.get(index));
                } else if (!crearPartesCuerpo.isEmpty() && crearPartesCuerpo.contains(filtrarPartesCuerpo.get(index))) {
                    int crearIndex = crearPartesCuerpo.indexOf(filtrarPartesCuerpo.get(index));
                    crearPartesCuerpo.remove(crearIndex);
                } else {
                    borrarPartesCuerpo.add(filtrarPartesCuerpo.get(index));
                }
                int VCIndex = listPartesCuerpo.indexOf(filtrarPartesCuerpo.get(index));
                listPartesCuerpo.remove(VCIndex);
                filtrarPartesCuerpo.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosPartesCuerpo");
            infoRegistro = "Cantidad de registros: " + listPartesCuerpo.size();
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
        BigInteger contarDetallesExamenesParteCuerpo;
        BigInteger contarSoAccidentesMedicosParteCuerpo;
        BigInteger contarSoDetallesRevisionesParteCuerpo;

        try {
            System.err.println("Control Secuencia de ControlPartesCuerpo ");
            if (tipoLista == 0) {
                contarDetallesExamenesParteCuerpo = administrarPartesCuerpo.contarDetallesExamenesParteCuerpo(listPartesCuerpo.get(index).getSecuencia());
                contarSoAccidentesMedicosParteCuerpo = administrarPartesCuerpo.contarSoAccidentesMedicosParteCuerpo(listPartesCuerpo.get(index).getSecuencia());
                contarSoDetallesRevisionesParteCuerpo = administrarPartesCuerpo.contarSoDetallesRevisionesParteCuerpo(listPartesCuerpo.get(index).getSecuencia());
            } else {
                contarDetallesExamenesParteCuerpo = administrarPartesCuerpo.contarDetallesExamenesParteCuerpo(filtrarPartesCuerpo.get(index).getSecuencia());
                contarSoAccidentesMedicosParteCuerpo = administrarPartesCuerpo.contarSoAccidentesMedicosParteCuerpo(filtrarPartesCuerpo.get(index).getSecuencia());
                contarSoDetallesRevisionesParteCuerpo = administrarPartesCuerpo.contarSoDetallesRevisionesParteCuerpo(filtrarPartesCuerpo.get(index).getSecuencia());
            }
            if (contarDetallesExamenesParteCuerpo.equals(new BigInteger("0"))
                    && contarSoAccidentesMedicosParteCuerpo.equals(new BigInteger("0"))
                    && contarSoDetallesRevisionesParteCuerpo.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoPartesCuerpo();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                index = -1;
                contarDetallesExamenesParteCuerpo = new BigInteger("-1");
                contarSoAccidentesMedicosParteCuerpo = new BigInteger("-1");
                contarSoDetallesRevisionesParteCuerpo = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlPartesCuerpo verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarPartesCuerpo.isEmpty() || !crearPartesCuerpo.isEmpty() || !modificarPartesCuerpo.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void guardarPartesCuerpo() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarPartesCuerpo");
            if (!borrarPartesCuerpo.isEmpty()) {
                administrarPartesCuerpo.borrarPartesCuerpo(borrarPartesCuerpo);
                //mostrarBorrados
                registrosBorrados = borrarPartesCuerpo.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarPartesCuerpo.clear();
            }
            if (!modificarPartesCuerpo.isEmpty()) {
                administrarPartesCuerpo.modificarPartesCuerpo(modificarPartesCuerpo);
                modificarPartesCuerpo.clear();
            }
            if (!crearPartesCuerpo.isEmpty()) {
                administrarPartesCuerpo.crearPartesCuerpo(crearPartesCuerpo);
                crearPartesCuerpo.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listPartesCuerpo = null;
            context.update("form:datosPartesCuerpo");
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
                editarPartesCuerpo = listPartesCuerpo.get(index);
            }
            if (tipoLista == 1) {
                editarPartesCuerpo = filtrarPartesCuerpo.get(index);
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

    public void agregarNuevoPartesCuerpo() {
        System.out.println("agregarNuevoPartesCuerpo");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoPartesCuerpo.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoPartesCuerpo.getCodigo());

            for (int x = 0; x < listPartesCuerpo.size(); x++) {
                if (listPartesCuerpo.get(x).getCodigo().equals(nuevoPartesCuerpo.getCodigo())) {
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
        if (nuevoPartesCuerpo.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoPartesCuerpo.getDescripcion().isEmpty()) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
                bandera = 0;
                filtrarPartesCuerpo = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoPartesCuerpo.setSecuencia(l);

            crearPartesCuerpo.add(nuevoPartesCuerpo);

            listPartesCuerpo.add(nuevoPartesCuerpo);
            nuevoPartesCuerpo = new PartesCuerpo();
            context.update("form:datosPartesCuerpo");
            infoRegistro = "Cantidad de registros: " + listPartesCuerpo.size();
            context.update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroPartesCuerpo.hide()");
            index = -1;
            secRegistro = null;

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoPartesCuerpo() {
        System.out.println("limpiarNuevoPartesCuerpo");
        nuevoPartesCuerpo = new PartesCuerpo();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoPartesCuerpo() {
        System.out.println("duplicandoPartesCuerpo");
        if (index >= 0) {
            duplicarPartesCuerpo = new PartesCuerpo();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarPartesCuerpo.setSecuencia(l);
                duplicarPartesCuerpo.setCodigo(listPartesCuerpo.get(index).getCodigo());
                duplicarPartesCuerpo.setDescripcion(listPartesCuerpo.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarPartesCuerpo.setSecuencia(l);
                duplicarPartesCuerpo.setCodigo(filtrarPartesCuerpo.get(index).getCodigo());
                duplicarPartesCuerpo.setDescripcion(filtrarPartesCuerpo.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarTE");
            context.execute("duplicarRegistroPartesCuerpo.show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarPartesCuerpo.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarPartesCuerpo.getDescripcion());

        if (duplicarPartesCuerpo.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listPartesCuerpo.size(); x++) {
                if (listPartesCuerpo.get(x).getCodigo().equals(duplicarPartesCuerpo.getCodigo())) {
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
        if (duplicarPartesCuerpo.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarPartesCuerpo.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarPartesCuerpo.getSecuencia() + "  " + duplicarPartesCuerpo.getCodigo());
            if (crearPartesCuerpo.contains(duplicarPartesCuerpo)) {
                System.out.println("Ya lo contengo.");
            }
            listPartesCuerpo.add(duplicarPartesCuerpo);
            crearPartesCuerpo.add(duplicarPartesCuerpo);
            context.update("form:datosPartesCuerpo");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listPartesCuerpo.size();
            context.update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
                bandera = 0;
                filtrarPartesCuerpo = null;
                tipoLista = 0;
            }
            duplicarPartesCuerpo = new PartesCuerpo();
            RequestContext.getCurrentInstance().execute("duplicarRegistroPartesCuerpo.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    public void limpiarDuplicarPartesCuerpo() {
        duplicarPartesCuerpo = new PartesCuerpo();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPartesCuerpoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "PARTESCUERPO", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPartesCuerpoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "PARTESCUERPO", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listPartesCuerpo.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "PARTESCUERPO"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("PARTESCUERPO")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<PartesCuerpo> getListPartesCuerpo() {
        if (listPartesCuerpo == null) {
            System.out.println("ControlPartesCuerpo getListPartesCuerpo");
            listPartesCuerpo = administrarPartesCuerpo.consultarPartesCuerpo();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listPartesCuerpo == null || listPartesCuerpo.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listPartesCuerpo.size();
        }
        return listPartesCuerpo;
    }

    public void setListPartesCuerpo(List<PartesCuerpo> listPartesCuerpo) {
        this.listPartesCuerpo = listPartesCuerpo;
    }

    public List<PartesCuerpo> getFiltrarPartesCuerpo() {
        return filtrarPartesCuerpo;
    }

    public void setFiltrarPartesCuerpo(List<PartesCuerpo> filtrarPartesCuerpo) {
        this.filtrarPartesCuerpo = filtrarPartesCuerpo;
    }

    public PartesCuerpo getNuevoPartesCuerpo() {
        return nuevoPartesCuerpo;
    }

    public void setNuevoPartesCuerpo(PartesCuerpo nuevoPartesCuerpo) {
        this.nuevoPartesCuerpo = nuevoPartesCuerpo;
    }

    public PartesCuerpo getDuplicarPartesCuerpo() {
        return duplicarPartesCuerpo;
    }

    public void setDuplicarPartesCuerpo(PartesCuerpo duplicarPartesCuerpo) {
        this.duplicarPartesCuerpo = duplicarPartesCuerpo;
    }

    public PartesCuerpo getEditarPartesCuerpo() {
        return editarPartesCuerpo;
    }

    public void setEditarPartesCuerpo(PartesCuerpo editarPartesCuerpo) {
        this.editarPartesCuerpo = editarPartesCuerpo;
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

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public PartesCuerpo getParteCuerpoSeleccionado() {
        return parteCuerpoSeleccionado;
    }

    public void setParteCuerpoSeleccionado(PartesCuerpo parteCuerpoSeleccionado) {
        this.parteCuerpoSeleccionado = parteCuerpoSeleccionado;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
