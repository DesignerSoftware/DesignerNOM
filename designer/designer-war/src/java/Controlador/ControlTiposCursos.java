/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposCursos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposCursosInterface;
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
import utilidadesUI.PrimefacesContextUI;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlTiposCursos implements Serializable {

    @EJB
    AdministrarTiposCursosInterface administrarTiposCursos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposCursos> listTiposCursos;
    private List<TiposCursos> filtrarTiposCursos;
    private List<TiposCursos> crearTiposCursos;
    private List<TiposCursos> modificarTiposCursos;
    private List<TiposCursos> borrarTiposCursos;
    private TiposCursos nuevoTiposCursos;
    private TiposCursos duplicarTiposCursos;
    private TiposCursos editarTiposCursos;
    private TiposCursos clasesPensionesSeleccionado;
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

    public ControlTiposCursos() {
        listTiposCursos = null;
        crearTiposCursos = new ArrayList<TiposCursos>();
        modificarTiposCursos = new ArrayList<TiposCursos>();
        borrarTiposCursos = new ArrayList<TiposCursos>();
        permitirIndex = true;
        editarTiposCursos = new TiposCursos();
        nuevoTiposCursos = new TiposCursos();
        duplicarTiposCursos = new TiposCursos();
        guardado = true;
        tamano = 270;
        System.out.println("controlTiposCursos Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposCursos PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposCursos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlTiposCursos.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }  RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposCursos.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposCursos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listTiposCursos.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listTiposCursos.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listTiposCursos.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarTiposCursos.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarTiposCursos.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarTiposCursos.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposCursos.asignarIndex \n");
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
            System.out.println("ERROR ControlTiposCursos.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosTiposCursos");
            bandera = 0;
            filtrarTiposCursos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposCursos.clear();
        crearTiposCursos.clear();
        modificarTiposCursos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposCursos = null;
        guardado = true;
        permitirIndex = true;
        getListTiposCursos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposCursos == null || listTiposCursos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposCursos.size();
        }
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:datosTiposCursos");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosTiposCursos");
            bandera = 0;
            filtrarTiposCursos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposCursos.clear();
        crearTiposCursos.clear();
        modificarTiposCursos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposCursos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosTiposCursos");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
            codigo.setFilterStyle("width: 85%;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
            descripcion.setFilterStyle("width: 85%;");
            PrimefacesContextUI.actualizar("form:datosTiposCursos");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosTiposCursos");
            bandera = 0;
            filtrarTiposCursos = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposCursos(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearTiposCursos.contains(listTiposCursos.get(indice))) {
                    if (listTiposCursos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCursos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposCursos.size(); j++) {
                            if (j != indice) {
                                if (listTiposCursos.get(indice).getCodigo().equals(listTiposCursos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposCursos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposCursos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCursos.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposCursos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCursos.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposCursos.isEmpty()) {
                            modificarTiposCursos.add(listTiposCursos.get(indice));
                        } else if (!modificarTiposCursos.contains(listTiposCursos.get(indice))) {
                            modificarTiposCursos.add(listTiposCursos.get(indice));
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
                    if (listTiposCursos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCursos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposCursos.size(); j++) {
                            if (j != indice) {
                                if (listTiposCursos.get(indice).getCodigo().equals(listTiposCursos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposCursos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposCursos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCursos.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposCursos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposCursos.get(indice).setDescripcion(backUpDescripcion);
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

                if (!crearTiposCursos.contains(filtrarTiposCursos.get(indice))) {
                    if (filtrarTiposCursos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposCursos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < filtrarTiposCursos.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposCursos.get(indice).getCodigo().equals(filtrarTiposCursos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposCursos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposCursos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposCursos.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposCursos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposCursos.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposCursos.isEmpty()) {
                            modificarTiposCursos.add(filtrarTiposCursos.get(indice));
                        } else if (!modificarTiposCursos.contains(filtrarTiposCursos.get(indice))) {
                            modificarTiposCursos.add(filtrarTiposCursos.get(indice));
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
                    if (filtrarTiposCursos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposCursos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < listTiposCursos.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposCursos.get(indice).getCodigo().equals(filtrarTiposCursos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposCursos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposCursos.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposCursos.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarTiposCursos.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposCursos.get(indice).setDescripcion(backUpDescripcion);
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
            PrimefacesContextUI.actualizar("form:datosTiposCursos");
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }

    }

    public void borrandoTiposCursos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposCursos");
                if (!modificarTiposCursos.isEmpty() && modificarTiposCursos.contains(listTiposCursos.get(index))) {
                    int modIndex = modificarTiposCursos.indexOf(listTiposCursos.get(index));
                    modificarTiposCursos.remove(modIndex);
                    borrarTiposCursos.add(listTiposCursos.get(index));
                } else if (!crearTiposCursos.isEmpty() && crearTiposCursos.contains(listTiposCursos.get(index))) {
                    int crearIndex = crearTiposCursos.indexOf(listTiposCursos.get(index));
                    crearTiposCursos.remove(crearIndex);
                } else {
                    borrarTiposCursos.add(listTiposCursos.get(index));
                }
                listTiposCursos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposCursos ");
                if (!modificarTiposCursos.isEmpty() && modificarTiposCursos.contains(filtrarTiposCursos.get(index))) {
                    int modIndex = modificarTiposCursos.indexOf(filtrarTiposCursos.get(index));
                    modificarTiposCursos.remove(modIndex);
                    borrarTiposCursos.add(filtrarTiposCursos.get(index));
                } else if (!crearTiposCursos.isEmpty() && crearTiposCursos.contains(filtrarTiposCursos.get(index))) {
                    int crearIndex = crearTiposCursos.indexOf(filtrarTiposCursos.get(index));
                    crearTiposCursos.remove(crearIndex);
                } else {
                    borrarTiposCursos.add(filtrarTiposCursos.get(index));
                }
                int VCIndex = listTiposCursos.indexOf(filtrarTiposCursos.get(index));
                listTiposCursos.remove(VCIndex);
                filtrarTiposCursos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosTiposCursos");
            infoRegistro = "Cantidad de registros: " + listTiposCursos.size();
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
        System.out.println("Estoy en verificarBorrado");
        BigInteger contarCursosTipoCurso;

        try {
            System.err.println("Control Secuencia de ControlTiposCursos ");
            if (tipoLista == 0) {
                contarCursosTipoCurso = administrarTiposCursos.contarCursosTipoCurso(listTiposCursos.get(index).getSecuencia());
            } else {
                contarCursosTipoCurso = administrarTiposCursos.contarCursosTipoCurso(filtrarTiposCursos.get(index).getSecuencia());
            }
            if (contarCursosTipoCurso.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposCursos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
                contarCursosTipoCurso = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposCursos verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposCursos.isEmpty() || !crearTiposCursos.isEmpty() || !modificarTiposCursos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposCursos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposCursos");
            if (!borrarTiposCursos.isEmpty()) {
                administrarTiposCursos.borrarTiposCursos(borrarTiposCursos);
                //mostrarBorrados
                registrosBorrados = borrarTiposCursos.size();
                PrimefacesContextUI.actualizar("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarTiposCursos.clear();
            }
            if (!modificarTiposCursos.isEmpty()) {
                administrarTiposCursos.modificarTiposCursos(modificarTiposCursos);
                modificarTiposCursos.clear();
            }
            if (!crearTiposCursos.isEmpty()) {
                administrarTiposCursos.crearTiposCursos(crearTiposCursos);
                crearTiposCursos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposCursos = null;
            PrimefacesContextUI.actualizar("form:datosTiposCursos");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
        }
        index = -1;
        PrimefacesContextUI.actualizar("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTiposCursos = listTiposCursos.get(index);
            }
            if (tipoLista == 1) {
                editarTiposCursos = filtrarTiposCursos.get(index);
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

    public void agregarNuevoTiposCursos() {
        System.out.println("agregarNuevoTiposCursos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposCursos.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTiposCursos.getCodigo());

            for (int x = 0; x < listTiposCursos.size(); x++) {
                if (listTiposCursos.get(x).getCodigo().equals(nuevoTiposCursos.getCodigo())) {
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
        if (nuevoTiposCursos.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoTiposCursos.getDescripcion().isEmpty()) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosTiposCursos");
                bandera = 0;
                filtrarTiposCursos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposCursos.setSecuencia(l);

            crearTiposCursos.add(nuevoTiposCursos);

            listTiposCursos.add(nuevoTiposCursos);
            nuevoTiposCursos = new TiposCursos();
            PrimefacesContextUI.actualizar("form:datosTiposCursos");
            infoRegistro = "Cantidad de registros: " + listTiposCursos.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroTiposCursos').hide()");
            index = -1;
            secRegistro = null;

        } else {
            PrimefacesContextUI.actualizar("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposCursos() {
        System.out.println("limpiarNuevoTiposCursos");
        nuevoTiposCursos = new TiposCursos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposCursos() {
        System.out.println("duplicandoTiposCursos");
        if (index >= 0) {
            duplicarTiposCursos = new TiposCursos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposCursos.setSecuencia(l);
                duplicarTiposCursos.setCodigo(listTiposCursos.get(index).getCodigo());
                duplicarTiposCursos.setDescripcion(listTiposCursos.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarTiposCursos.setSecuencia(l);
                duplicarTiposCursos.setCodigo(filtrarTiposCursos.get(index).getCodigo());
                duplicarTiposCursos.setDescripcion(filtrarTiposCursos.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroTiposCursos').show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposCursos.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposCursos.getDescripcion());

        if (duplicarTiposCursos.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposCursos.size(); x++) {
                if (listTiposCursos.get(x).getCodigo().equals(duplicarTiposCursos.getCodigo())) {
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
        if (duplicarTiposCursos.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarTiposCursos.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTiposCursos.getSecuencia() + "  " + duplicarTiposCursos.getCodigo());
            if (crearTiposCursos.contains(duplicarTiposCursos)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposCursos.add(duplicarTiposCursos);
            crearTiposCursos.add(duplicarTiposCursos);
            PrimefacesContextUI.actualizar("form:datosTiposCursos");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listTiposCursos.size();
            PrimefacesContextUI.actualizar("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCursos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosTiposCursos");
                bandera = 0;
                filtrarTiposCursos = null;
                tipoLista = 0;
            }
            duplicarTiposCursos = new TiposCursos();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroTiposCursos').hide()");

        } else {
            contador = 0;
            PrimefacesContextUI.actualizar("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposCursos() {
        duplicarTiposCursos = new TiposCursos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCursosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSCURSOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCursosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSCURSOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposCursos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSCURSOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("TIPOSCURSOS")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposCursos> getListTiposCursos() {
        if (listTiposCursos == null) {
            System.out.println("ControlTiposCursos getListTiposCursos");
            listTiposCursos = administrarTiposCursos.consultarTiposCursos();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposCursos == null || listTiposCursos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposCursos.size();
        }
        return listTiposCursos;
    }

    public void setListTiposCursos(List<TiposCursos> listTiposCursos) {
        this.listTiposCursos = listTiposCursos;
    }

    public List<TiposCursos> getFiltrarTiposCursos() {
        return filtrarTiposCursos;
    }

    public void setFiltrarTiposCursos(List<TiposCursos> filtrarTiposCursos) {
        this.filtrarTiposCursos = filtrarTiposCursos;
    }

    public TiposCursos getNuevoTiposCursos() {
        return nuevoTiposCursos;
    }

    public void setNuevoTiposCursos(TiposCursos nuevoTiposCursos) {
        this.nuevoTiposCursos = nuevoTiposCursos;
    }

    public TiposCursos getDuplicarTiposCursos() {
        return duplicarTiposCursos;
    }

    public void setDuplicarTiposCursos(TiposCursos duplicarTiposCursos) {
        this.duplicarTiposCursos = duplicarTiposCursos;
    }

    public TiposCursos getEditarTiposCursos() {
        return editarTiposCursos;
    }

    public void setEditarTiposCursos(TiposCursos editarTiposCursos) {
        this.editarTiposCursos = editarTiposCursos;
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

    public TiposCursos getTiposCursosSeleccionado() {
        return clasesPensionesSeleccionado;
    }

    public void setTiposCursosSeleccionado(TiposCursos clasesPensionesSeleccionado) {
        this.clasesPensionesSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
