/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposDocumentos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposDocumentosInterface;
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

@ManagedBean
@SessionScoped
public class ControlTiposDocumentos implements Serializable {

    @EJB
    AdministrarTiposDocumentosInterface administrarTiposDocumentos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<TiposDocumentos> listTiposDocumentos;
    private List<TiposDocumentos> filtrarTiposDocumentos;
    private List<TiposDocumentos> crearTiposDocumentos;
    private List<TiposDocumentos> modificarTiposDocumentos;
    private List<TiposDocumentos> borrarTiposDocumentos;
    private TiposDocumentos nuevoTiposDocumentos;
    private TiposDocumentos duplicarTiposDocumentos;
    private TiposDocumentos editarTiposDocumentos;
    private TiposDocumentos tiposDocumentosSeleccionado;
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
    private String infoRecurso;
    private String backUpNombreLargo;

    public ControlTiposDocumentos() {
        listTiposDocumentos = null;
        crearTiposDocumentos = new ArrayList<TiposDocumentos>();
        modificarTiposDocumentos = new ArrayList<TiposDocumentos>();
        borrarTiposDocumentos = new ArrayList<TiposDocumentos>();
        permitirIndex = true;
        editarTiposDocumentos = new TiposDocumentos();
        nuevoTiposDocumentos = new TiposDocumentos();
        duplicarTiposDocumentos = new TiposDocumentos();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposDocumentos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlTiposDocumentos.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }  RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposDocumentos.size();
            context.update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposDocumentos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listTiposDocumentos.get(index).getSecuencia();
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpNombreLargo = listTiposDocumentos.get(index).getNombrelargo();
                } else {
                    backUpNombreLargo = filtrarTiposDocumentos.get(index).getNombrelargo();
                }
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposDocumentos.asignarIndex \n");
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
            System.out.println("ERROR ControlTiposDocumentos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }
    private String infoRegistro;

    public void cancelarModificacion() {FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            bandera = 0;
            filtrarTiposDocumentos = null;
            tipoLista = 0;
        }

        borrarTiposDocumentos.clear();
        crearTiposDocumentos.clear();
        modificarTiposDocumentos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposDocumentos = null;
        guardado = true;
        permitirIndex = true;
        getListTiposDocumentos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposDocumentos == null || listTiposDocumentos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposDocumentos.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosTiposDocumentos");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            bandera = 0;
            filtrarTiposDocumentos = null;
            tipoLista = 0;
        }

        borrarTiposDocumentos.clear();
        crearTiposDocumentos.clear();
        modificarTiposDocumentos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposDocumentos = null;
        guardado = true;
        permitirIndex = true;
        getListTiposDocumentos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposDocumentos == null || listTiposDocumentos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposDocumentos.size();
        }
        context.update("form:informacionRegistro");
        context.update("form:datosTiposDocumentos");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 246;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
            codigo.setFilterStyle("width: 220px");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
            descripcion.setFilterStyle("width: 400px");
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
            bandera = 0;
            filtrarTiposDocumentos = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposDocumentos(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0, pass = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearTiposDocumentos.contains(listTiposDocumentos.get(indice))) {
                    if (listTiposDocumentos.get(indice).getNombrecorto() != null) {
                        if (listTiposDocumentos.get(indice).getNombrecorto().length() > 3) {
                            mensajeValidacion = "EL NOMBRE CORTO MAXIMO DEBE TENER 3 CARACTERES";
                        } else {
                            for (int j = 0; j < listTiposDocumentos.size(); j++) {
                                if (j != indice) {
                                    if (listTiposDocumentos.get(indice).getNombrecorto().equals(listTiposDocumentos.get(j).getNombrecorto())) {
                                        contador++;
                                    }
                                }
                            }
                            if (contador > 0) {
                                mensajeValidacion = "NOMBRES CORTOS REPETIDOS";
                            } else {
                                pass++;
                            }
                        }
                    } else {
                        pass++;
                    }
                    if (listTiposDocumentos.get(indice).getNombrelargo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposDocumentos.get(indice).setNombrelargo(backUpNombreLargo);
                    } else if (listTiposDocumentos.get(indice).getNombrelargo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposDocumentos.get(indice).setNombrelargo(backUpNombreLargo);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (modificarTiposDocumentos.isEmpty()) {
                            modificarTiposDocumentos.add(listTiposDocumentos.get(indice));
                        } else if (!modificarTiposDocumentos.contains(listTiposDocumentos.get(indice))) {
                            modificarTiposDocumentos.add(listTiposDocumentos.get(indice));
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
                    if (listTiposDocumentos.get(indice).getNombrecorto() != null) {
                        if (listTiposDocumentos.get(indice).getNombrecorto().length() > 3) {
                            mensajeValidacion = "EL NOMBRE CORTO MAXIMO DEBE TENER 3 CARACTERES";
                        } else {
                            for (int j = 0; j < listTiposDocumentos.size(); j++) {
                                if (j != indice) {
                                    if (listTiposDocumentos.get(indice).getNombrecorto().equals(listTiposDocumentos.get(j).getNombrecorto())) {
                                        contador++;
                                    }
                                }
                            }
                            if (contador > 0) {
                                mensajeValidacion = "NOMBRES CORTOS REPETIDOS";
                            } else {
                                pass++;
                            }
                        }
                    } else {
                        pass++;
                    }
                    if (listTiposDocumentos.get(indice).getNombrelargo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposDocumentos.get(indice).setNombrelargo(backUpNombreLargo);
                    } else if (listTiposDocumentos.get(indice).getNombrelargo().isEmpty()) {
                        listTiposDocumentos.get(indice).setNombrelargo(backUpNombreLargo);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
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

                if (!crearTiposDocumentos.contains(filtrarTiposDocumentos.get(indice))) {
                    if (filtrarTiposDocumentos.get(indice).getNombrecorto() != null) {
                        if (filtrarTiposDocumentos.get(indice).getNombrecorto().length() > 3) {
                            mensajeValidacion = "EL NOMBRE CORTO MAXIMO DEBE TENER 3 CARACTERES";
                        } else {
                            for (int j = 0; j < filtrarTiposDocumentos.size(); j++) {
                                if (j != indice) {
                                    if (filtrarTiposDocumentos.get(indice).getNombrecorto().equals(filtrarTiposDocumentos.get(j).getNombrecorto())) {
                                        contador++;
                                    }
                                }
                            }
                            if (contador > 0) {
                                mensajeValidacion = "NOMBRES CORTOS REPETIDOS";
                            } else {
                                pass++;
                            }
                        }
                    } else {
                        pass++;
                    }
                    if (filtrarTiposDocumentos.get(indice).getNombrelargo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposDocumentos.get(indice).setNombrelargo(backUpNombreLargo);

                    } else if (filtrarTiposDocumentos.get(indice).getNombrelargo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposDocumentos.get(indice).setNombrelargo(backUpNombreLargo);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (modificarTiposDocumentos.isEmpty()) {
                            modificarTiposDocumentos.add(filtrarTiposDocumentos.get(indice));
                        } else if (!modificarTiposDocumentos.contains(filtrarTiposDocumentos.get(indice))) {
                            modificarTiposDocumentos.add(filtrarTiposDocumentos.get(indice));
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
                    if (filtrarTiposDocumentos.get(indice).getNombrecorto() != null) {
                        if (filtrarTiposDocumentos.get(indice).getNombrecorto().length() > 3) {
                            mensajeValidacion = "EL NOMBRE CORTO MAXIMO DEBE TENER 3 CARACTERES";
                        } else {
                            for (int j = 0; j < filtrarTiposDocumentos.size(); j++) {
                                if (j != indice) {
                                    if (filtrarTiposDocumentos.get(indice).getNombrecorto().equals(filtrarTiposDocumentos.get(j).getNombrecorto())) {
                                        contador++;
                                    }
                                }
                            }
                            if (contador > 0) {
                                mensajeValidacion = "NOMBRES CORTOS REPETIDOS";
                            } else {
                                pass++;
                            }
                        }
                    } else {
                        pass++;
                    }
                    if (filtrarTiposDocumentos.get(indice).getNombrelargo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposDocumentos.get(indice).setNombrelargo(backUpNombreLargo);

                    } else if (filtrarTiposDocumentos.get(indice).getNombrelargo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposDocumentos.get(indice).setNombrelargo(backUpNombreLargo);
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
            context.update("form:datosTiposDocumentos");
            context.update("form:ACEPTAR");
        }

    }

    public void borrandoTiposDocumentos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposDocumentos");
                if (!modificarTiposDocumentos.isEmpty() && modificarTiposDocumentos.contains(listTiposDocumentos.get(index))) {
                    int modIndex = modificarTiposDocumentos.indexOf(listTiposDocumentos.get(index));
                    modificarTiposDocumentos.remove(modIndex);
                    borrarTiposDocumentos.add(listTiposDocumentos.get(index));
                } else if (!crearTiposDocumentos.isEmpty() && crearTiposDocumentos.contains(listTiposDocumentos.get(index))) {
                    int crearIndex = crearTiposDocumentos.indexOf(listTiposDocumentos.get(index));
                    crearTiposDocumentos.remove(crearIndex);
                } else {
                    borrarTiposDocumentos.add(listTiposDocumentos.get(index));
                }
                listTiposDocumentos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposDocumentos ");
                if (!modificarTiposDocumentos.isEmpty() && modificarTiposDocumentos.contains(filtrarTiposDocumentos.get(index))) {
                    int modIndex = modificarTiposDocumentos.indexOf(filtrarTiposDocumentos.get(index));
                    modificarTiposDocumentos.remove(modIndex);
                    borrarTiposDocumentos.add(filtrarTiposDocumentos.get(index));
                } else if (!crearTiposDocumentos.isEmpty() && crearTiposDocumentos.contains(filtrarTiposDocumentos.get(index))) {
                    int crearIndex = crearTiposDocumentos.indexOf(filtrarTiposDocumentos.get(index));
                    crearTiposDocumentos.remove(crearIndex);
                } else {
                    borrarTiposDocumentos.add(filtrarTiposDocumentos.get(index));
                }
                int VCIndex = listTiposDocumentos.indexOf(filtrarTiposDocumentos.get(index));
                listTiposDocumentos.remove(VCIndex);
                filtrarTiposDocumentos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listTiposDocumentos == null || listTiposDocumentos.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listTiposDocumentos.size();
            }
            context.update("form:informacionRegistro");
            context.update("form:datosTiposDocumentos");
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
        BigInteger contarCodeudoresTipoDocumento;
        BigInteger contarPersonasTipoDocumento;

        try {
            System.err.println("Control Secuencia de ControlTiposDocumentos ");
            if (tipoLista == 0) {
                contarCodeudoresTipoDocumento = administrarTiposDocumentos.contarCodeudoresTipoDocumento(listTiposDocumentos.get(index).getSecuencia());
                contarPersonasTipoDocumento = administrarTiposDocumentos.contarPersonasTipoDocumento(listTiposDocumentos.get(index).getSecuencia());
            } else {
                contarCodeudoresTipoDocumento = administrarTiposDocumentos.contarCodeudoresTipoDocumento(filtrarTiposDocumentos.get(index).getSecuencia());
                contarPersonasTipoDocumento = administrarTiposDocumentos.contarPersonasTipoDocumento(filtrarTiposDocumentos.get(index).getSecuencia());
            }
            if (contarCodeudoresTipoDocumento.equals(new BigInteger("0")) && contarPersonasTipoDocumento.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposDocumentos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                index = -1;
                contarCodeudoresTipoDocumento = new BigInteger("-1");
                contarPersonasTipoDocumento = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposDocumentos verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposDocumentos.isEmpty() || !crearTiposDocumentos.isEmpty() || !modificarTiposDocumentos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void guardarTiposDocumentos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposDocumentos");
            if (!borrarTiposDocumentos.isEmpty()) {
                administrarTiposDocumentos.borrarTiposDocumentos(borrarTiposDocumentos);
                //mostrarBorrados
                registrosBorrados = borrarTiposDocumentos.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarTiposDocumentos.clear();
            }
            if (!modificarTiposDocumentos.isEmpty()) {
                administrarTiposDocumentos.modificarTiposDocumentos(modificarTiposDocumentos);
                modificarTiposDocumentos.clear();
            }
            if (!crearTiposDocumentos.isEmpty()) {
                administrarTiposDocumentos.crearTiposDocumentos(crearTiposDocumentos);
                crearTiposDocumentos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposDocumentos = null;
            context.update("form:datosTiposDocumentos");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTiposDocumentos = listTiposDocumentos.get(index);
            }
            if (tipoLista == 1) {
                editarTiposDocumentos = filtrarTiposDocumentos.get(index);
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

    public void agregarNuevoTiposDocumentos() {
        System.out.println("agregarNuevoTiposDocumentos");
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposDocumentos.getNombrecorto() != null) {
            for (int i = 0; i < listTiposDocumentos.size(); i++) {
                if (nuevoTiposDocumentos.getNombrecorto().equals(listTiposDocumentos.get(i).getNombrecorto())) {
                    duplicados++;
                }
            }
            if (duplicados == 0) {
                contador++;
            } else {
                mensajeValidacion = "Nombre Corto repetido";
            }
        } else {
            contador++;
        }

        if (nuevoTiposDocumentos.getNombrelargo() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
                bandera = 0;
                filtrarTiposDocumentos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposDocumentos.setSecuencia(l);

            crearTiposDocumentos.add(nuevoTiposDocumentos);

            listTiposDocumentos.add(nuevoTiposDocumentos);
            nuevoTiposDocumentos = new TiposDocumentos();
            context.update("form:datosTiposDocumentos");

            infoRegistro = "Cantidad de registros: " + listTiposDocumentos.size();
            context.update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroTiposDocumentos.hide()");
            index = -1;
            secRegistro = null;

        } else {
            context.update("form:validacionNuevaCentroCosto");
            context.execute("validacionNuevaCentroCosto.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposDocumentos() {
        System.out.println("limpiarNuevoTiposDocumentos");
        nuevoTiposDocumentos = new TiposDocumentos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposDocumentos() {
        System.out.println("duplicandoTiposDocumentos");
        if (index >= 0) {
            duplicarTiposDocumentos = new TiposDocumentos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposDocumentos.setSecuencia(l);
                duplicarTiposDocumentos.setNombrecorto(listTiposDocumentos.get(index).getNombrecorto());
                duplicarTiposDocumentos.setNombrelargo(listTiposDocumentos.get(index).getNombrelargo());
            }
            if (tipoLista == 1) {
                duplicarTiposDocumentos.setSecuencia(l);
                duplicarTiposDocumentos.setNombrecorto(filtrarTiposDocumentos.get(index).getNombrecorto());
                duplicarTiposDocumentos.setNombrelargo(filtrarTiposDocumentos.get(index).getNombrelargo());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:duplicarTE");
            context.execute("duplicarRegistroTiposDocumentos.show()");
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

        System.err.println("ConfirmarDuplicar Nombre Corto " + duplicarTiposDocumentos.getNombrecorto());
        System.err.println("ConfirmarDuplicar Nombre Largo " + duplicarTiposDocumentos.getNombrelargo());
        if (duplicarTiposDocumentos.getNombrecorto() != null) {
            for (int i = 0; i < listTiposDocumentos.size(); i++) {
                if (duplicarTiposDocumentos.getNombrecorto().equals(listTiposDocumentos.get(i).getNombrecorto())) {
                    duplicados++;
                }
            }
            if (duplicados == 0) {
                contador++;
            } else {
                mensajeValidacion = "Nombre Corto repetido";
            }
        } else {
            contador++;
        }

        if (duplicarTiposDocumentos.getNombrelargo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion  \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarTiposDocumentos.setSecuencia(l);
            if (crearTiposDocumentos.contains(duplicarTiposDocumentos)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposDocumentos.add(duplicarTiposDocumentos);
            crearTiposDocumentos.add(duplicarTiposDocumentos);
            context.update("form:datosTiposDocumentos");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }

            infoRegistro = "Cantidad de registros: " + listTiposDocumentos.size();
            context.update("form:informacionRegistro");
            context.update("form:ACEPTAR");
            if (bandera == 1) {FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposDocumentos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposDocumentos");
                bandera = 0;
                filtrarTiposDocumentos = null;
                tipoLista = 0;
            }
            duplicarTiposDocumentos = new TiposDocumentos();
            RequestContext.getCurrentInstance().execute("duplicarRegistroTiposDocumentos.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    public void limpiarDuplicarTiposDocumentos() {
        duplicarTiposDocumentos = new TiposDocumentos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposDocumentosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSDOCUMENTOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposDocumentosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSDOCUMENTOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposDocumentos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSDOCUMENTOS"); //En ENCARGATURAS lo cambia por el Descripcion de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("TIPOSDOCUMENTOS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposDocumentos> getListTiposDocumentos() {
        if (listTiposDocumentos == null) {
            listTiposDocumentos = administrarTiposDocumentos.consultarTiposDocumentos();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposDocumentos == null || listTiposDocumentos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposDocumentos.size();
        }
        context.update("form:informacionRegistro");
        return listTiposDocumentos;
    }

    public void setListTiposDocumentos(List<TiposDocumentos> listTiposDocumentos) {
        this.listTiposDocumentos = listTiposDocumentos;
    }

    public List<TiposDocumentos> getFiltrarTiposDocumentos() {
        return filtrarTiposDocumentos;
    }

    public void setFiltrarTiposDocumentos(List<TiposDocumentos> filtrarTiposDocumentos) {
        this.filtrarTiposDocumentos = filtrarTiposDocumentos;
    }

    public TiposDocumentos getNuevoTiposDocumentos() {
        return nuevoTiposDocumentos;
    }

    public void setNuevoTiposDocumentos(TiposDocumentos nuevoTiposDocumentos) {
        this.nuevoTiposDocumentos = nuevoTiposDocumentos;
    }

    public TiposDocumentos getDuplicarTiposDocumentos() {
        return duplicarTiposDocumentos;
    }

    public void setDuplicarTiposDocumentos(TiposDocumentos duplicarTiposDocumentos) {
        this.duplicarTiposDocumentos = duplicarTiposDocumentos;
    }

    public TiposDocumentos getEditarTiposDocumentos() {
        return editarTiposDocumentos;
    }

    public void setEditarTiposDocumentos(TiposDocumentos editarTiposDocumentos) {
        this.editarTiposDocumentos = editarTiposDocumentos;
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

    public TiposDocumentos getTiposDocumentosSeleccionado() {
        return tiposDocumentosSeleccionado;
    }

    public void setTiposDocumentosSeleccionado(TiposDocumentos tiposDocumentosSeleccionado) {
        this.tiposDocumentosSeleccionado = tiposDocumentosSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
