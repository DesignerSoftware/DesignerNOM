/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosReemplazos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosReemplazosInterface;
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
public class ControlMotivosReemplazos implements Serializable {

    @EJB
    AdministrarMotivosReemplazosInterface administrarMotivosReemplazos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<MotivosReemplazos> listMotivosReemplazos;
    private List<MotivosReemplazos> filtrarMotivosReemplazos;
    private List<MotivosReemplazos> crearMotivosReemplazos;
    private List<MotivosReemplazos> modificarMotivosReemplazos;
    private List<MotivosReemplazos> borrarMotivosReemplazos;
    private MotivosReemplazos nuevoMotivosReemplazos;
    private MotivosReemplazos duplicarMotivosReemplazos;
    private MotivosReemplazos editarMotivosReemplazos;
    private MotivosReemplazos motivosReemplazosSeleccionado;
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
    private String paginaAnterior;

    public ControlMotivosReemplazos() {
        listMotivosReemplazos = null;
        crearMotivosReemplazos = new ArrayList<MotivosReemplazos>();
        modificarMotivosReemplazos = new ArrayList<MotivosReemplazos>();
        borrarMotivosReemplazos = new ArrayList<MotivosReemplazos>();
        permitirIndex = true;
        editarMotivosReemplazos = new MotivosReemplazos();
        nuevoMotivosReemplazos = new MotivosReemplazos();
        duplicarMotivosReemplazos = new MotivosReemplazos();
        guardado = true;
        tamano = 270;
        System.out.println("controlMotivosReemplazos Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlMotivosReemplazos PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosReemplazos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaAnterior(String pagina) {
        paginaAnterior = pagina;
        System.out.println("ControlMotivosReemplazos recibirPaginaAnterior paginaAnterior : " + paginaAnterior);
    }

    public String redirigirPaginaAnterior() {
        System.out.println("ControlMotivosReemplazos redirigirPaginaAnterior paginaAnterior : " + paginaAnterior);
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlMotivosReemplazos.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarMotivosReemplazos.size();
            context.update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlMotivosReemplazos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listMotivosReemplazos.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listMotivosReemplazos.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listMotivosReemplazos.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarMotivosReemplazos.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarMotivosReemplazos.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarMotivosReemplazos.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlMotivosReemplazos.asignarIndex \n");
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
            System.out.println("ERROR ControlMotivosReemplazos.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            bandera = 0;
            filtrarMotivosReemplazos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivosReemplazos.clear();
        crearMotivosReemplazos.clear();
        modificarMotivosReemplazos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosReemplazos = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosReemplazos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosReemplazos == null || listMotivosReemplazos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosReemplazos.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosMotivosReemplazos");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            bandera = 0;
            filtrarMotivosReemplazos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivosReemplazos.clear();
        crearMotivosReemplazos.clear();
        modificarMotivosReemplazos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosReemplazos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosMotivosReemplazos");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            bandera = 0;
            filtrarMotivosReemplazos = null;
            tipoLista = 0;
        }
    }

    public void modificarMotivosReemplazos(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearMotivosReemplazos.contains(listMotivosReemplazos.get(indice))) {
                    if (listMotivosReemplazos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosReemplazos.size(); j++) {
                            if (j != indice) {
                                if (listMotivosReemplazos.get(indice).getCodigo() == listMotivosReemplazos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosReemplazos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listMotivosReemplazos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarMotivosReemplazos.isEmpty()) {
                            modificarMotivosReemplazos.add(listMotivosReemplazos.get(indice));
                        } else if (!modificarMotivosReemplazos.contains(listMotivosReemplazos.get(indice))) {
                            modificarMotivosReemplazos.add(listMotivosReemplazos.get(indice));
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
                    if (listMotivosReemplazos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosReemplazos.size(); j++) {
                            if (j != indice) {
                                if (listMotivosReemplazos.get(indice).getCodigo() == listMotivosReemplazos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosReemplazos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listMotivosReemplazos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
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

                if (!crearMotivosReemplazos.contains(filtrarMotivosReemplazos.get(indice))) {
                    if (filtrarMotivosReemplazos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < listMotivosReemplazos.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosReemplazos.get(indice).getCodigo() == listMotivosReemplazos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarMotivosReemplazos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (filtrarMotivosReemplazos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarMotivosReemplazos.isEmpty()) {
                            modificarMotivosReemplazos.add(filtrarMotivosReemplazos.get(indice));
                        } else if (!modificarMotivosReemplazos.contains(filtrarMotivosReemplazos.get(indice))) {
                            modificarMotivosReemplazos.add(filtrarMotivosReemplazos.get(indice));
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
                    if (filtrarMotivosReemplazos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < listMotivosReemplazos.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosReemplazos.get(indice).getCodigo() == listMotivosReemplazos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarMotivosReemplazos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (filtrarMotivosReemplazos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
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
            context.update("form:datosMotivosReemplazos");
            context.update("form:ACEPTAR");
        }

    }

    public void borrandoMotivosReemplazos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoMotivosReemplazos");
                if (!modificarMotivosReemplazos.isEmpty() && modificarMotivosReemplazos.contains(listMotivosReemplazos.get(index))) {
                    int modIndex = modificarMotivosReemplazos.indexOf(listMotivosReemplazos.get(index));
                    modificarMotivosReemplazos.remove(modIndex);
                    borrarMotivosReemplazos.add(listMotivosReemplazos.get(index));
                } else if (!crearMotivosReemplazos.isEmpty() && crearMotivosReemplazos.contains(listMotivosReemplazos.get(index))) {
                    int crearIndex = crearMotivosReemplazos.indexOf(listMotivosReemplazos.get(index));
                    crearMotivosReemplazos.remove(crearIndex);
                } else {
                    borrarMotivosReemplazos.add(listMotivosReemplazos.get(index));
                }
                listMotivosReemplazos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoMotivosReemplazos ");
                if (!modificarMotivosReemplazos.isEmpty() && modificarMotivosReemplazos.contains(filtrarMotivosReemplazos.get(index))) {
                    int modIndex = modificarMotivosReemplazos.indexOf(filtrarMotivosReemplazos.get(index));
                    modificarMotivosReemplazos.remove(modIndex);
                    borrarMotivosReemplazos.add(filtrarMotivosReemplazos.get(index));
                } else if (!crearMotivosReemplazos.isEmpty() && crearMotivosReemplazos.contains(filtrarMotivosReemplazos.get(index))) {
                    int crearIndex = crearMotivosReemplazos.indexOf(filtrarMotivosReemplazos.get(index));
                    crearMotivosReemplazos.remove(crearIndex);
                } else {
                    borrarMotivosReemplazos.add(filtrarMotivosReemplazos.get(index));
                }
                int VCIndex = listMotivosReemplazos.indexOf(filtrarMotivosReemplazos.get(index));
                listMotivosReemplazos.remove(VCIndex);
                filtrarMotivosReemplazos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosMotivosReemplazos");
            infoRegistro = "Cantidad de registros: " + listMotivosReemplazos.size();
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
        BigInteger contarEncargaturasMotivoReemplazo;

        try {
            System.err.println("Control Secuencia de ControlMotivosReemplazos ");
            if (tipoLista == 0) {
                contarEncargaturasMotivoReemplazo = administrarMotivosReemplazos.contarEncargaturasMotivoReemplazo(listMotivosReemplazos.get(index).getSecuencia());
            } else {
                contarEncargaturasMotivoReemplazo = administrarMotivosReemplazos.contarEncargaturasMotivoReemplazo(filtrarMotivosReemplazos.get(index).getSecuencia());
            }
            if (contarEncargaturasMotivoReemplazo.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoMotivosReemplazos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                index = -1;
                contarEncargaturasMotivoReemplazo = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlMotivosReemplazos verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarMotivosReemplazos.isEmpty() || !crearMotivosReemplazos.isEmpty() || !modificarMotivosReemplazos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void guardarMotivosReemplazos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarMotivosReemplazos");
            if (!borrarMotivosReemplazos.isEmpty()) {
                administrarMotivosReemplazos.borrarMotivosReemplazos(borrarMotivosReemplazos);
                //mostrarBorrados
                registrosBorrados = borrarMotivosReemplazos.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarMotivosReemplazos.clear();
            }
            if (!modificarMotivosReemplazos.isEmpty()) {
                administrarMotivosReemplazos.modificarMotivosReemplazos(modificarMotivosReemplazos);
                modificarMotivosReemplazos.clear();
            }
            if (!crearMotivosReemplazos.isEmpty()) {
                administrarMotivosReemplazos.crearMotivosReemplazos(crearMotivosReemplazos);
                crearMotivosReemplazos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMotivosReemplazos = null;
            context.update("form:datosMotivosReemplazos");
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
                editarMotivosReemplazos = listMotivosReemplazos.get(index);
            }
            if (tipoLista == 1) {
                editarMotivosReemplazos = filtrarMotivosReemplazos.get(index);
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

    public void agregarNuevoMotivosReemplazos() {
        System.out.println("agregarNuevoMotivosReemplazos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivosReemplazos.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivosReemplazos.getCodigo());

            for (int x = 0; x < listMotivosReemplazos.size(); x++) {
                if (listMotivosReemplazos.get(x).getCodigo() == nuevoMotivosReemplazos.getCodigo()) {
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
        if (nuevoMotivosReemplazos.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Motivo Reemplazo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoMotivosReemplazos.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Motivo Reemplazo \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
                bandera = 0;
                filtrarMotivosReemplazos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivosReemplazos.setSecuencia(l);

            crearMotivosReemplazos.add(nuevoMotivosReemplazos);

            listMotivosReemplazos.add(nuevoMotivosReemplazos);
            nuevoMotivosReemplazos = new MotivosReemplazos();
            context.update("form:datosMotivosReemplazos");
            infoRegistro = "Cantidad de registros: " + listMotivosReemplazos.size();
            context.update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroMotivosReemplazos.hide()");
            index = -1;
            secRegistro = null;

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivosReemplazos() {
        System.out.println("limpiarNuevoMotivosReemplazos");
        nuevoMotivosReemplazos = new MotivosReemplazos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoMotivosReemplazos() {
        System.out.println("duplicandoMotivosReemplazos");
        if (index >= 0) {
            duplicarMotivosReemplazos = new MotivosReemplazos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivosReemplazos.setSecuencia(l);
                duplicarMotivosReemplazos.setCodigo(listMotivosReemplazos.get(index).getCodigo());
                duplicarMotivosReemplazos.setNombre(listMotivosReemplazos.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarMotivosReemplazos.setSecuencia(l);
                duplicarMotivosReemplazos.setCodigo(filtrarMotivosReemplazos.get(index).getCodigo());
                duplicarMotivosReemplazos.setNombre(filtrarMotivosReemplazos.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarTE");
            context.execute("duplicarRegistroMotivosReemplazos.show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarMotivosReemplazos.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarMotivosReemplazos.getNombre());

        if (duplicarMotivosReemplazos.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosReemplazos.size(); x++) {
                if (listMotivosReemplazos.get(x).getCodigo() == duplicarMotivosReemplazos.getCodigo()) {
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
        if (duplicarMotivosReemplazos.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Motivo Reemplazo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarMotivosReemplazos.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Motivo Reemplazo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarMotivosReemplazos.getSecuencia() + "  " + duplicarMotivosReemplazos.getCodigo());
            if (crearMotivosReemplazos.contains(duplicarMotivosReemplazos)) {
                System.out.println("Ya lo contengo.");
            }
            listMotivosReemplazos.add(duplicarMotivosReemplazos);
            crearMotivosReemplazos.add(duplicarMotivosReemplazos);
            context.update("form:datosMotivosReemplazos");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listMotivosReemplazos.size();
            context.update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
                bandera = 0;
                filtrarMotivosReemplazos = null;
                tipoLista = 0;
            }
            duplicarMotivosReemplazos = new MotivosReemplazos();
            RequestContext.getCurrentInstance().execute("duplicarRegistroMotivosReemplazos.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    public void limpiarDuplicarMotivosReemplazos() {
        duplicarMotivosReemplazos = new MotivosReemplazos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosReemplazosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MOTIVOSREEMPLEAZOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosReemplazosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MOTIVOSREEMPLEAZOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listMotivosReemplazos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSREEMPLEAZOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("MOTIVOSREEMPLEAZOS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<MotivosReemplazos> getListMotivosReemplazos() {
        if (listMotivosReemplazos == null) {
            System.out.println("ControlMotivosReemplazos getListMotivosReemplazos");
            listMotivosReemplazos = administrarMotivosReemplazos.MotivosReemplazos();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosReemplazos == null || listMotivosReemplazos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosReemplazos.size();
        }
        return listMotivosReemplazos;
    }

    public void setListMotivosReemplazos(List<MotivosReemplazos> listMotivosReemplazos) {
        this.listMotivosReemplazos = listMotivosReemplazos;
    }

    public List<MotivosReemplazos> getFiltrarMotivosReemplazos() {
        return filtrarMotivosReemplazos;
    }

    public void setFiltrarMotivosReemplazos(List<MotivosReemplazos> filtrarMotivosReemplazos) {
        this.filtrarMotivosReemplazos = filtrarMotivosReemplazos;
    }

    public MotivosReemplazos getNuevoMotivosReemplazos() {
        return nuevoMotivosReemplazos;
    }

    public void setNuevoMotivosReemplazos(MotivosReemplazos nuevoMotivosReemplazos) {
        this.nuevoMotivosReemplazos = nuevoMotivosReemplazos;
    }

    public MotivosReemplazos getDuplicarMotivosReemplazos() {
        return duplicarMotivosReemplazos;
    }

    public void setDuplicarMotivosReemplazos(MotivosReemplazos duplicarMotivosReemplazos) {
        this.duplicarMotivosReemplazos = duplicarMotivosReemplazos;
    }

    public MotivosReemplazos getEditarMotivosReemplazos() {
        return editarMotivosReemplazos;
    }

    public void setEditarMotivosReemplazos(MotivosReemplazos editarMotivosReemplazos) {
        this.editarMotivosReemplazos = editarMotivosReemplazos;
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

    public MotivosReemplazos getMotivosReemplazosSeleccionado() {
        return motivosReemplazosSeleccionado;
    }

    public void setMotivosReemplazosSeleccionado(MotivosReemplazos clasesPensionesSeleccionado) {
        this.motivosReemplazosSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
