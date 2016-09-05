/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.LugaresOcurrencias;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarLugaresOcurrenciasInterface;
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
public class ControlLugaresOcurrencias implements Serializable {

    @EJB
    AdministrarLugaresOcurrenciasInterface administrarLugaresOcurrencias;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<LugaresOcurrencias> listLugaresOcurrencias;
    private List<LugaresOcurrencias> filtrarLugaresOcurrencias;
    private List<LugaresOcurrencias> crearLugaresOcurrencias;
    private List<LugaresOcurrencias> modificarLugaresOcurrencias;
    private List<LugaresOcurrencias> borrarLugaresOcurrencias;
    private LugaresOcurrencias nuevoLugaresOcurrencias;
    private LugaresOcurrencias duplicarLugaresOcurrencias;
    private LugaresOcurrencias editarLugaresOcurrencias;
    private LugaresOcurrencias lugarOcurrenciaSeleccionada;
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
    private String infoRegistro;
    private Integer backUpCodigo;

    public ControlLugaresOcurrencias() {
        listLugaresOcurrencias = null;
        crearLugaresOcurrencias = new ArrayList<LugaresOcurrencias>();
        modificarLugaresOcurrencias = new ArrayList<LugaresOcurrencias>();
        borrarLugaresOcurrencias = new ArrayList<LugaresOcurrencias>();
        permitirIndex = true;
        editarLugaresOcurrencias = new LugaresOcurrencias();
        nuevoLugaresOcurrencias = new LugaresOcurrencias();
        duplicarLugaresOcurrencias = new LugaresOcurrencias();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarLugaresOcurrencias.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlLugaresOcurrencias.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarLugaresOcurrencias.size();
            context.update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlLugaresOcurrencias eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listLugaresOcurrencias.get(index).getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listLugaresOcurrencias.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarLugaresOcurrencias.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listLugaresOcurrencias.get(index).getDescripcion();
                } else {
                    backUpDescripcion = filtrarLugaresOcurrencias.get(index).getDescripcion();
                }
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlLugaresOcurrencias.asignarIndex \n");
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
            System.out.println("ERROR ControlLugaresOcurrencias.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            bandera = 0;
            filtrarLugaresOcurrencias = null;
            tipoLista = 0;
        }

        borrarLugaresOcurrencias.clear();
        crearLugaresOcurrencias.clear();
        modificarLugaresOcurrencias.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listLugaresOcurrencias = null;
        guardado = true;
        permitirIndex = true;
        getListLugaresOcurrencias();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listLugaresOcurrencias == null || listLugaresOcurrencias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosLugaresOcurrencias");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            bandera = 0;
            filtrarLugaresOcurrencias = null;
            tipoLista = 0;
        }

        borrarLugaresOcurrencias.clear();
        crearLugaresOcurrencias.clear();
        modificarLugaresOcurrencias.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listLugaresOcurrencias = null;
        guardado = true;
        permitirIndex = true;
        getListLugaresOcurrencias();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listLugaresOcurrencias == null || listLugaresOcurrencias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosLugaresOcurrencias");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            bandera = 0;
            filtrarLugaresOcurrencias = null;
            tipoLista = 0;
        }
    }

    public void modificarLugaresOcurrencias(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0, pass = 0;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearLugaresOcurrencias.contains(listLugaresOcurrencias.get(indice))) {
                    if (listLugaresOcurrencias.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listLugaresOcurrencias.size(); j++) {
                            if (j != indice) {
                                if (listLugaresOcurrencias.get(indice).getCodigo() == listLugaresOcurrencias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listLugaresOcurrencias.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                    } else if (listLugaresOcurrencias.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);

                    } else {
                        pass++;
                    }
                    if (pass == 2) {
                        if (modificarLugaresOcurrencias.isEmpty()) {
                            modificarLugaresOcurrencias.add(listLugaresOcurrencias.get(indice));
                        } else if (!modificarLugaresOcurrencias.contains(listLugaresOcurrencias.get(indice))) {
                            modificarLugaresOcurrencias.add(listLugaresOcurrencias.get(indice));
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
                    if (listLugaresOcurrencias.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listLugaresOcurrencias.size(); j++) {
                            if (j != indice) {
                                if (listLugaresOcurrencias.get(indice).getCodigo() == listLugaresOcurrencias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listLugaresOcurrencias.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                    } else if (listLugaresOcurrencias.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);

                    } else {
                        pass++;
                    }
                    if (pass == 2) {

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

                if (!crearLugaresOcurrencias.contains(filtrarLugaresOcurrencias.get(indice))) {
                    if (filtrarLugaresOcurrencias.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < filtrarLugaresOcurrencias.size(); j++) {
                            if (j != indice) {
                                if (filtrarLugaresOcurrencias.get(indice).getCodigo() == filtrarLugaresOcurrencias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listLugaresOcurrencias.size(); j++) {
                            if (j != indice) {
                                if (filtrarLugaresOcurrencias.get(indice).getCodigo() == listLugaresOcurrencias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }

                    if (filtrarLugaresOcurrencias.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                    } else if (filtrarLugaresOcurrencias.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (modificarLugaresOcurrencias.isEmpty()) {
                            modificarLugaresOcurrencias.add(filtrarLugaresOcurrencias.get(indice));
                        } else if (!modificarLugaresOcurrencias.contains(filtrarLugaresOcurrencias.get(indice))) {
                            modificarLugaresOcurrencias.add(filtrarLugaresOcurrencias.get(indice));
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

                    if (filtrarLugaresOcurrencias.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < filtrarLugaresOcurrencias.size(); j++) {
                            if (j != indice) {
                                if (filtrarLugaresOcurrencias.get(indice).getCodigo() == filtrarLugaresOcurrencias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listLugaresOcurrencias.size(); j++) {
                            if (j != indice) {
                                if (filtrarLugaresOcurrencias.get(indice).getCodigo() == listLugaresOcurrencias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }

                    if (filtrarLugaresOcurrencias.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                    } else if (filtrarLugaresOcurrencias.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {

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
            context.update("form:datosLugaresOcurrencias");
            context.update("form:ACEPTAR");
        }

    }

    public void borrandoLugaresOcurrencias() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoLugaresOcurrencias");
                if (!modificarLugaresOcurrencias.isEmpty() && modificarLugaresOcurrencias.contains(listLugaresOcurrencias.get(index))) {
                    int modIndex = modificarLugaresOcurrencias.indexOf(listLugaresOcurrencias.get(index));
                    modificarLugaresOcurrencias.remove(modIndex);
                    borrarLugaresOcurrencias.add(listLugaresOcurrencias.get(index));
                } else if (!crearLugaresOcurrencias.isEmpty() && crearLugaresOcurrencias.contains(listLugaresOcurrencias.get(index))) {
                    int crearIndex = crearLugaresOcurrencias.indexOf(listLugaresOcurrencias.get(index));
                    crearLugaresOcurrencias.remove(crearIndex);
                } else {
                    borrarLugaresOcurrencias.add(listLugaresOcurrencias.get(index));
                }
                listLugaresOcurrencias.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoLugaresOcurrencias ");
                if (!modificarLugaresOcurrencias.isEmpty() && modificarLugaresOcurrencias.contains(filtrarLugaresOcurrencias.get(index))) {
                    int modIndex = modificarLugaresOcurrencias.indexOf(filtrarLugaresOcurrencias.get(index));
                    modificarLugaresOcurrencias.remove(modIndex);
                    borrarLugaresOcurrencias.add(filtrarLugaresOcurrencias.get(index));
                } else if (!crearLugaresOcurrencias.isEmpty() && crearLugaresOcurrencias.contains(filtrarLugaresOcurrencias.get(index))) {
                    int crearIndex = crearLugaresOcurrencias.indexOf(filtrarLugaresOcurrencias.get(index));
                    crearLugaresOcurrencias.remove(crearIndex);
                } else {
                    borrarLugaresOcurrencias.add(filtrarLugaresOcurrencias.get(index));
                }
                int VCIndex = listLugaresOcurrencias.indexOf(filtrarLugaresOcurrencias.get(index));
                listLugaresOcurrencias.remove(VCIndex);
                filtrarLugaresOcurrencias.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listLugaresOcurrencias == null || listLugaresOcurrencias.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
            }
            context.update("form:informacionRegistro");
            context.update("form:datosLugaresOcurrencias");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
        }

    }

    public void revisarDialogoGuardar() {

        if (!borrarLugaresOcurrencias.isEmpty() || !crearLugaresOcurrencias.isEmpty() || !modificarLugaresOcurrencias.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void verificarBorrado() {
        System.out.println("verificarBorrado");
        BigInteger soAccidentes;

        try {
            if (tipoLista == 0) {
                soAccidentes = administrarLugaresOcurrencias.verificarSoAccidentesLugarOcurrencia(listLugaresOcurrencias.get(index).getSecuencia());
            } else {
                soAccidentes = administrarLugaresOcurrencias.verificarSoAccidentesLugarOcurrencia(filtrarLugaresOcurrencias.get(index).getSecuencia());
            }
            if (soAccidentes.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoLugaresOcurrencias();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                index = -1;
                soAccidentes = new BigInteger("-1");
            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
        }
    }

    public void guardarLugaresOcurrencias() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarLugaresOcurrencias");
            if (!borrarLugaresOcurrencias.isEmpty()) {
                administrarLugaresOcurrencias.borrarLugarOcurrencia(borrarLugaresOcurrencias);
                //mostrarBorrados
                registrosBorrados = borrarLugaresOcurrencias.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarLugaresOcurrencias.clear();
            }
            if (!modificarLugaresOcurrencias.isEmpty()) {
                administrarLugaresOcurrencias.modificarLugarOcurrencia(modificarLugaresOcurrencias);
                modificarLugaresOcurrencias.clear();
            }
            if (!crearLugaresOcurrencias.isEmpty()) {
                administrarLugaresOcurrencias.crearLugarOcurrencia(crearLugaresOcurrencias);
                crearLugaresOcurrencias.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listLugaresOcurrencias = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            context.update("form:datosLugaresOcurrencias");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarLugaresOcurrencias = listLugaresOcurrencias.get(index);
            }
            if (tipoLista == 1) {
                editarLugaresOcurrencias = filtrarLugaresOcurrencias.get(index);
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

    public void agregarNuevoLugaresOcurrencias() {
        System.out.println("agregarNuevoLugaresOcurrencias");
        int contador = 0;
        int duplicados = 0;
        Integer a;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoLugaresOcurrencias.getCodigo() == a) {
            mensajeValidacion = " *Código \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoLugaresOcurrencias.getCodigo());

            for (int x = 0; x < listLugaresOcurrencias.size(); x++) {
                if (listLugaresOcurrencias.get(x).getCodigo() == nuevoLugaresOcurrencias.getCodigo()) {
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
        if (nuevoLugaresOcurrencias.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
                bandera = 0;
                filtrarLugaresOcurrencias = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoLugaresOcurrencias.setSecuencia(l);

            crearLugaresOcurrencias.add(nuevoLugaresOcurrencias);

            listLugaresOcurrencias.add(nuevoLugaresOcurrencias);
            nuevoLugaresOcurrencias = new LugaresOcurrencias();
            context.update("form:datosLugaresOcurrencias");

            infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
            context.update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroLugaresOcurrencias.hide()");
            index = -1;
            secRegistro = null;

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
        }
    }

    public void limpiarNuevoLugaresOcurrencias() {
        System.out.println("limpiarNuevoLugaresOcurrencias");
        nuevoLugaresOcurrencias = new LugaresOcurrencias();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoLugaresOcurrencias() {
        System.out.println("duplicandoLugaresOcurrencias");
        if (index >= 0) {
            duplicarLugaresOcurrencias = new LugaresOcurrencias();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarLugaresOcurrencias.setSecuencia(l);
                duplicarLugaresOcurrencias.setCodigo(listLugaresOcurrencias.get(index).getCodigo());
                duplicarLugaresOcurrencias.setDescripcion(listLugaresOcurrencias.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarLugaresOcurrencias.setSecuencia(l);
                duplicarLugaresOcurrencias.setCodigo(filtrarLugaresOcurrencias.get(index).getCodigo());
                duplicarLugaresOcurrencias.setDescripcion(filtrarLugaresOcurrencias.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarTE");
            context.execute("duplicarRegistroLugaresOcurrencias.show()");
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
        Integer a;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarLugaresOcurrencias.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarLugaresOcurrencias.getDescripcion());

        if (duplicarLugaresOcurrencias.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listLugaresOcurrencias.size(); x++) {
                if (listLugaresOcurrencias.get(x).getCodigo() == duplicarLugaresOcurrencias.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (duplicarLugaresOcurrencias.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarLugaresOcurrencias.getSecuencia() + "  " + duplicarLugaresOcurrencias.getCodigo());
            if (crearLugaresOcurrencias.contains(duplicarLugaresOcurrencias)) {
                System.out.println("Ya lo contengo.");
            }
            listLugaresOcurrencias.add(duplicarLugaresOcurrencias);
            crearLugaresOcurrencias.add(duplicarLugaresOcurrencias);
            context.update("form:datosLugaresOcurrencias");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
            context.update("form:informacionRegistro");
            context.update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
                bandera = 0;
                filtrarLugaresOcurrencias = null;
                tipoLista = 0;
            }
            duplicarLugaresOcurrencias = new LugaresOcurrencias();
            RequestContext.getCurrentInstance().execute("duplicarRegistroLugaresOcurrencias.hide()");

        } else {
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    public void limpiarDuplicarLugaresOcurrencias() {
        duplicarLugaresOcurrencias = new LugaresOcurrencias();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLugaresOcurrenciasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "LUGARESOCURRENCIAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLugaresOcurrenciasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "LUGARESOCURRENCIAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listLugaresOcurrencias.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "LUGARESOCURRENCIAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("LUGARESOCURRENCIAS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<LugaresOcurrencias> getListLugaresOcurrencias() {
        if (listLugaresOcurrencias == null) {
            listLugaresOcurrencias = administrarLugaresOcurrencias.consultarLugaresOcurrencias();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listLugaresOcurrencias == null || listLugaresOcurrencias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
        }
        context.update("form:informacionRegistro");
        return listLugaresOcurrencias;
    }

    public void setListLugaresOcurrencias(List<LugaresOcurrencias> listLugaresOcurrencias) {
        this.listLugaresOcurrencias = listLugaresOcurrencias;
    }

    public List<LugaresOcurrencias> getFiltrarLugaresOcurrencias() {
        return filtrarLugaresOcurrencias;
    }

    public void setFiltrarLugaresOcurrencias(List<LugaresOcurrencias> filtrarLugaresOcurrencias) {
        this.filtrarLugaresOcurrencias = filtrarLugaresOcurrencias;
    }

    public LugaresOcurrencias getNuevoLugaresOcurrencias() {
        return nuevoLugaresOcurrencias;
    }

    public void setNuevoLugaresOcurrencias(LugaresOcurrencias nuevoLugaresOcurrencias) {
        this.nuevoLugaresOcurrencias = nuevoLugaresOcurrencias;
    }

    public LugaresOcurrencias getDuplicarLugaresOcurrencias() {
        return duplicarLugaresOcurrencias;
    }

    public void setDuplicarLugaresOcurrencias(LugaresOcurrencias duplicarLugaresOcurrencias) {
        this.duplicarLugaresOcurrencias = duplicarLugaresOcurrencias;
    }

    public LugaresOcurrencias getEditarLugaresOcurrencias() {
        return editarLugaresOcurrencias;
    }

    public void setEditarLugaresOcurrencias(LugaresOcurrencias editarLugaresOcurrencias) {
        this.editarLugaresOcurrencias = editarLugaresOcurrencias;
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

    public LugaresOcurrencias getLugarOcurrenciaSeleccionada() {
        return lugarOcurrenciaSeleccionada;
    }

    public void setLugarOcurrenciaSeleccionada(LugaresOcurrencias lugarOcurrenciaSeleccionada) {
        this.lugarOcurrenciaSeleccionada = lugarOcurrenciaSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
