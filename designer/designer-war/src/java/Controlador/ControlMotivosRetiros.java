/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosRetiros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosRetirosInterface;
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
public class ControlMotivosRetiros implements Serializable {

    @EJB
    AdministrarMotivosRetirosInterface administrarMotivosRetiros;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosRetiros> listMotivosRetiros;
    private List<MotivosRetiros> filtrarMotivosRetiros;
    private List<MotivosRetiros> crearMotivosRetiros;
    private List<MotivosRetiros> modificarMotivosRetiros;
    private List<MotivosRetiros> borrarMotivosRetiros;
    private MotivosRetiros nuevoMotivosRetiros;
    private MotivosRetiros duplicarMotivosRetiros;
    private MotivosRetiros editarMotivosRetiros;
    private MotivosRetiros motivoRetiroSelecciodo;
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

    private String backUpDescripcion;
    private Integer backUpCodigo;

    public ControlMotivosRetiros() {
        listMotivosRetiros = null;
        crearMotivosRetiros = new ArrayList<MotivosRetiros>();
        modificarMotivosRetiros = new ArrayList<MotivosRetiros>();
        borrarMotivosRetiros = new ArrayList<MotivosRetiros>();
        permitirIndex = true;
        editarMotivosRetiros = new MotivosRetiros();
        nuevoMotivosRetiros = new MotivosRetiros();
        duplicarMotivosRetiros = new MotivosRetiros();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosRetiros.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlMotivosRetiros.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarMotivosRetiros.size();
            context.update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlMotivosRetiros eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listMotivosRetiros.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listMotivosRetiros.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listMotivosRetiros.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarMotivosRetiros.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarMotivosRetiros.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarMotivosRetiros.get(index).getSecuencia();
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlMotivosRetiros.asignarIndex \n");
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
            System.out.println("ERROR ControlMotivosRetiros.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            bandera = 0;
            filtrarMotivosRetiros = null;
            tipoLista = 0;
        }

        borrarMotivosRetiros.clear();
        crearMotivosRetiros.clear();
        modificarMotivosRetiros.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosRetiros = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosRetiros();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosRetiros == null || listMotivosRetiros.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosRetiros.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosMotivosRetiros");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            bandera = 0;
            filtrarMotivosRetiros = null;
            tipoLista = 0;
        }

        borrarMotivosRetiros.clear();
        crearMotivosRetiros.clear();
        modificarMotivosRetiros.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosRetiros = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosRetiros();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosRetiros == null || listMotivosRetiros.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosRetiros.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosMotivosRetiros");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
            bandera = 0;
            filtrarMotivosRetiros = null;
            tipoLista = 0;
        }
    }

    public void modificarMotivosRetiros(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearMotivosRetiros.contains(listMotivosRetiros.get(indice))) {
                    if (listMotivosRetiros.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosRetiros.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosRetiros.size(); j++) {
                            if (j != indice) {
                                if (listMotivosRetiros.get(indice).getCodigo().equals(listMotivosRetiros.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listMotivosRetiros.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosRetiros.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listMotivosRetiros.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }
                    if (listMotivosRetiros.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosRetiros.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarMotivosRetiros.isEmpty()) {
                            modificarMotivosRetiros.add(listMotivosRetiros.get(indice));
                        } else if (!modificarMotivosRetiros.contains(listMotivosRetiros.get(indice))) {
                            modificarMotivosRetiros.add(listMotivosRetiros.get(indice));
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
                    if (listMotivosRetiros.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosRetiros.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosRetiros.size(); j++) {
                            if (j != indice) {
                                if (listMotivosRetiros.get(indice).getCodigo().equals(listMotivosRetiros.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listMotivosRetiros.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosRetiros.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listMotivosRetiros.get(indice).setNombre(backUpDescripcion);
                        banderita = false;
                    }
                    if (listMotivosRetiros.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosRetiros.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarMotivosRetiros.isEmpty()) {
                            modificarMotivosRetiros.add(listMotivosRetiros.get(indice));
                        } else if (!modificarMotivosRetiros.contains(listMotivosRetiros.get(indice))) {
                            modificarMotivosRetiros.add(listMotivosRetiros.get(indice));
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
                }
            } else {

                if (!crearMotivosRetiros.contains(filtrarMotivosRetiros.get(indice))) {
                    if (filtrarMotivosRetiros.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMotivosRetiros.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < filtrarMotivosRetiros.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosRetiros.get(indice).getCodigo().equals(filtrarMotivosRetiros.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarMotivosRetiros.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarMotivosRetiros.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosRetiros.get(indice).setNombre(backUpDescripcion);
                    }
                    if (filtrarMotivosRetiros.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosRetiros.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarMotivosRetiros.isEmpty()) {
                            modificarMotivosRetiros.add(filtrarMotivosRetiros.get(indice));
                        } else if (!modificarMotivosRetiros.contains(filtrarMotivosRetiros.get(indice))) {
                            modificarMotivosRetiros.add(filtrarMotivosRetiros.get(indice));
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
                    if (filtrarMotivosRetiros.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarMotivosRetiros.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < filtrarMotivosRetiros.size(); j++) {
                            if (j != indice) {
                                if (filtrarMotivosRetiros.get(indice).getCodigo().equals(filtrarMotivosRetiros.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarMotivosRetiros.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (filtrarMotivosRetiros.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosRetiros.get(indice).setNombre(backUpDescripcion);
                    }

                    if (filtrarMotivosRetiros.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarMotivosRetiros.get(indice).setNombre(backUpDescripcion);
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
            context.update("form:datosMotivosRetiros");
            context.update("form:ACEPTAR");
        }

    }

    public void borrandoMotivosRetiros() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoMotivosRetiros");
                if (!modificarMotivosRetiros.isEmpty() && modificarMotivosRetiros.contains(listMotivosRetiros.get(index))) {
                    int modIndex = modificarMotivosRetiros.indexOf(listMotivosRetiros.get(index));
                    modificarMotivosRetiros.remove(modIndex);
                    borrarMotivosRetiros.add(listMotivosRetiros.get(index));
                } else if (!crearMotivosRetiros.isEmpty() && crearMotivosRetiros.contains(listMotivosRetiros.get(index))) {
                    int crearIndex = crearMotivosRetiros.indexOf(listMotivosRetiros.get(index));
                    crearMotivosRetiros.remove(crearIndex);
                } else {
                    borrarMotivosRetiros.add(listMotivosRetiros.get(index));
                }
                listMotivosRetiros.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoMotivosRetiros ");
                if (!modificarMotivosRetiros.isEmpty() && modificarMotivosRetiros.contains(filtrarMotivosRetiros.get(index))) {
                    int modIndex = modificarMotivosRetiros.indexOf(filtrarMotivosRetiros.get(index));
                    modificarMotivosRetiros.remove(modIndex);
                    borrarMotivosRetiros.add(filtrarMotivosRetiros.get(index));
                } else if (!crearMotivosRetiros.isEmpty() && crearMotivosRetiros.contains(filtrarMotivosRetiros.get(index))) {
                    int crearIndex = crearMotivosRetiros.indexOf(filtrarMotivosRetiros.get(index));
                    crearMotivosRetiros.remove(crearIndex);
                } else {
                    borrarMotivosRetiros.add(filtrarMotivosRetiros.get(index));
                }
                int VCIndex = listMotivosRetiros.indexOf(filtrarMotivosRetiros.get(index));
                listMotivosRetiros.remove(VCIndex);
                filtrarMotivosRetiros.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosMotivosRetiros");
            infoRegistro = "Cantidad de registros: " + listMotivosRetiros.size();
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
        BigInteger contarHVExperienciasLaboralesMotivoRetiro;
        BigInteger contarRetiMotivosRetirosMotivoRetiro;
        BigInteger contarNovedadesSistemasMotivoRetiro;
        BigInteger contarRetiradosMotivoRetiro;

        try {
            System.err.println("Control Secuencia de ControlMotivosRetiros ");
            if (tipoLista == 0) {
                contarHVExperienciasLaboralesMotivoRetiro = administrarMotivosRetiros.contarHVExperienciasLaboralesMotivoRetiro(listMotivosRetiros.get(index).getSecuencia());
                contarNovedadesSistemasMotivoRetiro = administrarMotivosRetiros.contarNovedadesSistemasMotivoRetiro(listMotivosRetiros.get(index).getSecuencia());
                contarRetiMotivosRetirosMotivoRetiro = administrarMotivosRetiros.contarRetiMotivosRetirosMotivoRetiro(listMotivosRetiros.get(index).getSecuencia());
                contarRetiradosMotivoRetiro = administrarMotivosRetiros.contarRetiradosMotivoRetiro(listMotivosRetiros.get(index).getSecuencia());
            } else {
                contarHVExperienciasLaboralesMotivoRetiro = administrarMotivosRetiros.contarHVExperienciasLaboralesMotivoRetiro(filtrarMotivosRetiros.get(index).getSecuencia());
                contarNovedadesSistemasMotivoRetiro = administrarMotivosRetiros.contarNovedadesSistemasMotivoRetiro(filtrarMotivosRetiros.get(index).getSecuencia());
                contarRetiMotivosRetirosMotivoRetiro = administrarMotivosRetiros.contarRetiMotivosRetirosMotivoRetiro(filtrarMotivosRetiros.get(index).getSecuencia());
                contarRetiradosMotivoRetiro = administrarMotivosRetiros.contarRetiradosMotivoRetiro(filtrarMotivosRetiros.get(index).getSecuencia());
            }
            if (contarRetiradosMotivoRetiro.equals(new BigInteger("0")) && contarRetiMotivosRetirosMotivoRetiro.equals(new BigInteger("0")) && contarHVExperienciasLaboralesMotivoRetiro.equals(new BigInteger("0")) && contarNovedadesSistemasMotivoRetiro.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoMotivosRetiros();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                index = -1;
                contarHVExperienciasLaboralesMotivoRetiro = new BigInteger("-1");
                contarNovedadesSistemasMotivoRetiro = new BigInteger("-1");
                contarRetiMotivosRetirosMotivoRetiro = new BigInteger("-1");
                contarRetiradosMotivoRetiro = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlMotivosRetiros verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarMotivosRetiros.isEmpty() || !crearMotivosRetiros.isEmpty() || !modificarMotivosRetiros.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void guardarMotivosRetiros() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarMotivosRetiros");
            if (!borrarMotivosRetiros.isEmpty()) {
                administrarMotivosRetiros.borrarMotivosRetiros(borrarMotivosRetiros);
                //mostrarBorrados
                registrosBorrados = borrarMotivosRetiros.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarMotivosRetiros.clear();
            }
            if (!modificarMotivosRetiros.isEmpty()) {
                administrarMotivosRetiros.modificarMotivosRetiros(modificarMotivosRetiros);
                modificarMotivosRetiros.clear();
            }
            if (!crearMotivosRetiros.isEmpty()) {
                administrarMotivosRetiros.crearMotivosRetiros(crearMotivosRetiros);
                crearMotivosRetiros.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMotivosRetiros = null;
            context.update("form:datosMotivosRetiros");
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
                editarMotivosRetiros = listMotivosRetiros.get(index);
            }
            if (tipoLista == 1) {
                editarMotivosRetiros = filtrarMotivosRetiros.get(index);
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

    public void agregarNuevoMotivosRetiros() {
        System.out.println("agregarNuevoMotivosRetiros");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivosRetiros.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivosRetiros.getCodigo());

            for (int x = 0; x < listMotivosRetiros.size(); x++) {
                if (listMotivosRetiros.get(x).getCodigo().equals(nuevoMotivosRetiros.getCodigo())) {
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
        if (nuevoMotivosRetiros.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
                bandera = 0;
                filtrarMotivosRetiros = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivosRetiros.setSecuencia(l);

            crearMotivosRetiros.add(nuevoMotivosRetiros);

            listMotivosRetiros.add(nuevoMotivosRetiros);
            nuevoMotivosRetiros = new MotivosRetiros();
            context.update("form:datosMotivosRetiros");
            infoRegistro = "Cantidad de registros: " + listMotivosRetiros.size();
            context.update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroMotivosRetiros.hide()");
            index = -1;
            secRegistro = null;

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivosRetiros() {
        System.out.println("limpiarNuevoMotivosRetiros");
        nuevoMotivosRetiros = new MotivosRetiros();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoMotivosRetiros() {
        System.out.println("duplicandoMotivosRetiros");
        if (index >= 0) {
            duplicarMotivosRetiros = new MotivosRetiros();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivosRetiros.setSecuencia(l);
                duplicarMotivosRetiros.setCodigo(listMotivosRetiros.get(index).getCodigo());
                duplicarMotivosRetiros.setNombre(listMotivosRetiros.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarMotivosRetiros.setSecuencia(l);
                duplicarMotivosRetiros.setCodigo(filtrarMotivosRetiros.get(index).getCodigo());
                duplicarMotivosRetiros.setNombre(filtrarMotivosRetiros.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarTE");
            context.execute("duplicarRegistroMotivosRetiros.show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarMotivosRetiros.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarMotivosRetiros.getNombre());

        if (duplicarMotivosRetiros.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosRetiros.size(); x++) {
                if (listMotivosRetiros.get(x).getCodigo().equals(duplicarMotivosRetiros.getCodigo())) {
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
        if (duplicarMotivosRetiros.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarMotivosRetiros.getSecuencia() + "  " + duplicarMotivosRetiros.getCodigo());
            if (crearMotivosRetiros.contains(duplicarMotivosRetiros)) {
                System.out.println("Ya lo contengo.");
            }
            listMotivosRetiros.add(duplicarMotivosRetiros);
            crearMotivosRetiros.add(duplicarMotivosRetiros);
            context.update("form:datosMotivosRetiros");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listMotivosRetiros.size();
            context.update("form:informacionRegistro");
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosRetiros:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosRetiros");
                bandera = 0;
                filtrarMotivosRetiros = null;
                tipoLista = 0;
            }
            duplicarMotivosRetiros = new MotivosRetiros();
            RequestContext.getCurrentInstance().execute("duplicarRegistroMotivosRetiros.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    public void limpiarDuplicarMotivosRetiros() {
        duplicarMotivosRetiros = new MotivosRetiros();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosRetirosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MOTIVOSRETIROS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosRetirosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MOTIVOSRETIROS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listMotivosRetiros.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSRETIROS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("MOTIVOSRETIROS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<MotivosRetiros> getListMotivosRetiros() {
        if (listMotivosRetiros == null) {
            listMotivosRetiros = administrarMotivosRetiros.consultarMotivosRetiros();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosRetiros == null || listMotivosRetiros.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosRetiros.size();
        }
        context.update("form:informacionRegistro");
        return listMotivosRetiros;
    }

    public void setListMotivosRetiros(List<MotivosRetiros> listMotivosRetiros) {
        this.listMotivosRetiros = listMotivosRetiros;
    }

    public List<MotivosRetiros> getFiltrarMotivosRetiros() {
        return filtrarMotivosRetiros;
    }

    public void setFiltrarMotivosRetiros(List<MotivosRetiros> filtrarMotivosRetiros) {
        this.filtrarMotivosRetiros = filtrarMotivosRetiros;
    }

    public MotivosRetiros getNuevoMotivosRetiros() {
        return nuevoMotivosRetiros;
    }

    public void setNuevoMotivosRetiros(MotivosRetiros nuevoMotivosRetiros) {
        this.nuevoMotivosRetiros = nuevoMotivosRetiros;
    }

    public MotivosRetiros getDuplicarMotivosRetiros() {
        return duplicarMotivosRetiros;
    }

    public void setDuplicarMotivosRetiros(MotivosRetiros duplicarMotivosRetiros) {
        this.duplicarMotivosRetiros = duplicarMotivosRetiros;
    }

    public MotivosRetiros getEditarMotivosRetiros() {
        return editarMotivosRetiros;
    }

    public void setEditarMotivosRetiros(MotivosRetiros editarMotivosRetiros) {
        this.editarMotivosRetiros = editarMotivosRetiros;
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

    public MotivosRetiros getMotivoRetiroSelecciodo() {
        return motivoRetiroSelecciodo;
    }

    public void setMotivoRetiroSelecciodo(MotivosRetiros motivoRetiroSelecciodo) {
        this.motivoRetiroSelecciodo = motivoRetiroSelecciodo;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
