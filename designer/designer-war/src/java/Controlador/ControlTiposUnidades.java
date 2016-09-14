/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.TiposUnidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposUnidadesInterface;
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
public class ControlTiposUnidades implements Serializable {

    @EJB
    AdministrarTiposUnidadesInterface administrarTiposUnidades;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposUnidades> listTiposUnidades;
    private List<TiposUnidades> filtrarTiposUnidades;
    private List<TiposUnidades> crearTiposUnidades;
    private List<TiposUnidades> modificarTiposUnidades;
    private List<TiposUnidades> borrarTiposUnidades;
    private TiposUnidades nuevoTiposUnidades;
    private TiposUnidades duplicarTiposUnidades;
    private TiposUnidades editarTiposUnidades;
    private TiposUnidades tiposUnidadesSeleccionado;
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
    public String paginaAnterior;

    public ControlTiposUnidades() {
        listTiposUnidades = null;
        crearTiposUnidades = new ArrayList<TiposUnidades>();
        modificarTiposUnidades = new ArrayList<TiposUnidades>();
        borrarTiposUnidades = new ArrayList<TiposUnidades>();
        permitirIndex = true;
        editarTiposUnidades = new TiposUnidades();
        nuevoTiposUnidades = new TiposUnidades();
        duplicarTiposUnidades = new TiposUnidades();
        guardado = true;
        tamano = 270;
        System.out.println("controlTiposUnidades Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposUnidades PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposUnidades.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listTiposUnidades= null;
        getListTiposUnidades();
        
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlTiposUnidades.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposUnidades.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposUnidades eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listTiposUnidades.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listTiposUnidades.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listTiposUnidades.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarTiposUnidades.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarTiposUnidades.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarTiposUnidades.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposUnidades.asignarIndex \n");
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
            System.out.println("ERROR ControlTiposUnidades.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
            bandera = 0;
            filtrarTiposUnidades = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposUnidades.clear();
        crearTiposUnidades.clear();
        modificarTiposUnidades.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposUnidades = null;
        guardado = true;
        permitirIndex = true;
        getListTiposUnidades();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposUnidades == null || listTiposUnidades.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposUnidades.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
            bandera = 0;
            filtrarTiposUnidades = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposUnidades.clear();
        crearTiposUnidades.clear();
        modificarTiposUnidades.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposUnidades = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
            bandera = 0;
            filtrarTiposUnidades = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposUnidades(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearTiposUnidades.contains(listTiposUnidades.get(indice))) {
                    if (listTiposUnidades.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposUnidades.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposUnidades.size(); j++) {
                            if (j != indice) {
                                if (listTiposUnidades.get(indice).getCodigo().equals(listTiposUnidades.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposUnidades.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposUnidades.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposUnidades.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listTiposUnidades.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposUnidades.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposUnidades.isEmpty()) {
                            modificarTiposUnidades.add(listTiposUnidades.get(indice));
                        } else if (!modificarTiposUnidades.contains(listTiposUnidades.get(indice))) {
                            modificarTiposUnidades.add(listTiposUnidades.get(indice));
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
                    if (listTiposUnidades.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposUnidades.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposUnidades.size(); j++) {
                            if (j != indice) {
                                if (listTiposUnidades.get(indice).getCodigo().equals(listTiposUnidades.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposUnidades.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposUnidades.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposUnidades.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listTiposUnidades.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposUnidades.get(indice).setNombre(backUpDescripcion);
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

                if (!crearTiposUnidades.contains(filtrarTiposUnidades.get(indice))) {
                    if (filtrarTiposUnidades.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposUnidades.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {

                        for (int j = 0; j < filtrarTiposUnidades.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposUnidades.get(indice).getCodigo().equals(filtrarTiposUnidades.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposUnidades.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposUnidades.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposUnidades.get(indice).setNombre(backUpDescripcion);
                    }
                    if (filtrarTiposUnidades.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposUnidades.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposUnidades.isEmpty()) {
                            modificarTiposUnidades.add(filtrarTiposUnidades.get(indice));
                        } else if (!modificarTiposUnidades.contains(filtrarTiposUnidades.get(indice))) {
                            modificarTiposUnidades.add(filtrarTiposUnidades.get(indice));
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
                    if (filtrarTiposUnidades.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarTiposUnidades.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < filtrarTiposUnidades.size(); j++) {
                            if (j != indice) {
                                if (filtrarTiposUnidades.get(indice).getCodigo().equals(filtrarTiposUnidades.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarTiposUnidades.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarTiposUnidades.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposUnidades.get(indice).setNombre(backUpDescripcion);
                    }
                    if (filtrarTiposUnidades.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarTiposUnidades.get(indice).setNombre(backUpDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposUnidades() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposUnidades");
                if (!modificarTiposUnidades.isEmpty() && modificarTiposUnidades.contains(listTiposUnidades.get(index))) {
                    int modIndex = modificarTiposUnidades.indexOf(listTiposUnidades.get(index));
                    modificarTiposUnidades.remove(modIndex);
                    borrarTiposUnidades.add(listTiposUnidades.get(index));
                } else if (!crearTiposUnidades.isEmpty() && crearTiposUnidades.contains(listTiposUnidades.get(index))) {
                    int crearIndex = crearTiposUnidades.indexOf(listTiposUnidades.get(index));
                    crearTiposUnidades.remove(crearIndex);
                } else {
                    borrarTiposUnidades.add(listTiposUnidades.get(index));
                }
                listTiposUnidades.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposUnidades ");
                if (!modificarTiposUnidades.isEmpty() && modificarTiposUnidades.contains(filtrarTiposUnidades.get(index))) {
                    int modIndex = modificarTiposUnidades.indexOf(filtrarTiposUnidades.get(index));
                    modificarTiposUnidades.remove(modIndex);
                    borrarTiposUnidades.add(filtrarTiposUnidades.get(index));
                } else if (!crearTiposUnidades.isEmpty() && crearTiposUnidades.contains(filtrarTiposUnidades.get(index))) {
                    int crearIndex = crearTiposUnidades.indexOf(filtrarTiposUnidades.get(index));
                    crearTiposUnidades.remove(crearIndex);
                } else {
                    borrarTiposUnidades.add(filtrarTiposUnidades.get(index));
                }
                int VCIndex = listTiposUnidades.indexOf(filtrarTiposUnidades.get(index));
                listTiposUnidades.remove(VCIndex);
                filtrarTiposUnidades.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
            infoRegistro = "Cantidad de registros: " + listTiposUnidades.size();
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
        BigInteger contarUnidadesTipoUnidad;

        try {
            System.err.println("Control Secuencia de ControlTiposUnidades ");
            if (tipoLista == 0) {
                contarUnidadesTipoUnidad = administrarTiposUnidades.contarUnidadesTipoUnidad(listTiposUnidades.get(index).getSecuencia());
            } else {
                contarUnidadesTipoUnidad = administrarTiposUnidades.contarUnidadesTipoUnidad(filtrarTiposUnidades.get(index).getSecuencia());
            }
            if (contarUnidadesTipoUnidad.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposUnidades();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
                contarUnidadesTipoUnidad = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposUnidades verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposUnidades.isEmpty() || !crearTiposUnidades.isEmpty() || !modificarTiposUnidades.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposUnidades() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposUnidades");
            if (!borrarTiposUnidades.isEmpty()) {
                administrarTiposUnidades.borrarTiposUnidades(borrarTiposUnidades);
                //mostrarBorrados
                registrosBorrados = borrarTiposUnidades.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarTiposUnidades.clear();
            }
            if (!modificarTiposUnidades.isEmpty()) {
                administrarTiposUnidades.modificarTiposUnidades(modificarTiposUnidades);
                modificarTiposUnidades.clear();
            }
            if (!crearTiposUnidades.isEmpty()) {
                administrarTiposUnidades.crearTiposUnidades(crearTiposUnidades);
                crearTiposUnidades.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposUnidades = null;
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
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
                editarTiposUnidades = listTiposUnidades.get(index);
            }
            if (tipoLista == 1) {
                editarTiposUnidades = filtrarTiposUnidades.get(index);
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

    public void agregarNuevoTiposUnidades() {
        System.out.println("agregarNuevoTiposUnidades");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposUnidades.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTiposUnidades.getCodigo());

            for (int x = 0; x < listTiposUnidades.size(); x++) {
                if (listTiposUnidades.get(x).getCodigo().equals(nuevoTiposUnidades.getCodigo())) {
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
        if (nuevoTiposUnidades.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoTiposUnidades.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
                bandera = 0;
                filtrarTiposUnidades = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposUnidades.setSecuencia(l);

            crearTiposUnidades.add(nuevoTiposUnidades);

            listTiposUnidades.add(nuevoTiposUnidades);
            nuevoTiposUnidades = new TiposUnidades();
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
            infoRegistro = "Cantidad de registros: " + listTiposUnidades.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroTiposUnidades').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposUnidades() {
        System.out.println("limpiarNuevoTiposUnidades");
        nuevoTiposUnidades = new TiposUnidades();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposUnidades() {
        System.out.println("duplicandoTiposUnidades");
        if (index >= 0) {
            duplicarTiposUnidades = new TiposUnidades();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposUnidades.setSecuencia(l);
                duplicarTiposUnidades.setCodigo(listTiposUnidades.get(index).getCodigo());
                duplicarTiposUnidades.setNombre(listTiposUnidades.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarTiposUnidades.setSecuencia(l);
                duplicarTiposUnidades.setCodigo(filtrarTiposUnidades.get(index).getCodigo());
                duplicarTiposUnidades.setNombre(filtrarTiposUnidades.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroTiposUnidades').show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposUnidades.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposUnidades.getNombre());

        if (duplicarTiposUnidades.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposUnidades.size(); x++) {
                if (listTiposUnidades.get(x).getCodigo().equals(duplicarTiposUnidades.getCodigo())) {
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
        if (duplicarTiposUnidades.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarTiposUnidades.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTiposUnidades.getSecuencia() + "  " + duplicarTiposUnidades.getCodigo());
            if (crearTiposUnidades.contains(duplicarTiposUnidades)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposUnidades.add(duplicarTiposUnidades);
            crearTiposUnidades.add(duplicarTiposUnidades);
            RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listTiposUnidades.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposUnidades:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposUnidades");
                bandera = 0;
                filtrarTiposUnidades = null;
                tipoLista = 0;
            }
            duplicarTiposUnidades = new TiposUnidades();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroTiposUnidades').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposUnidades() {
        duplicarTiposUnidades = new TiposUnidades();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposUnidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSUNIDADES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposUnidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSUNIDADES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposUnidades.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSUNIDADES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("TIPOSUNIDADES")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposUnidades> getListTiposUnidades() {
        if (listTiposUnidades == null) {
            System.out.println("ControlTiposUnidades getListTiposUnidades");
            listTiposUnidades = administrarTiposUnidades.consultarTiposUnidades();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposUnidades == null || listTiposUnidades.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposUnidades.size();
        }
        return listTiposUnidades;
    }

    public void setListTiposUnidades(List<TiposUnidades> listTiposUnidades) {
        this.listTiposUnidades = listTiposUnidades;
    }

    public List<TiposUnidades> getFiltrarTiposUnidades() {
        return filtrarTiposUnidades;
    }

    public void setFiltrarTiposUnidades(List<TiposUnidades> filtrarTiposUnidades) {
        this.filtrarTiposUnidades = filtrarTiposUnidades;
    }

    public TiposUnidades getNuevoTiposUnidades() {
        return nuevoTiposUnidades;
    }

    public void setNuevoTiposUnidades(TiposUnidades nuevoTiposUnidades) {
        this.nuevoTiposUnidades = nuevoTiposUnidades;
    }

    public TiposUnidades getDuplicarTiposUnidades() {
        return duplicarTiposUnidades;
    }

    public void setDuplicarTiposUnidades(TiposUnidades duplicarTiposUnidades) {
        this.duplicarTiposUnidades = duplicarTiposUnidades;
    }

    public TiposUnidades getEditarTiposUnidades() {
        return editarTiposUnidades;
    }

    public void setEditarTiposUnidades(TiposUnidades editarTiposUnidades) {
        this.editarTiposUnidades = editarTiposUnidades;
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

    public TiposUnidades getTiposUnidadesSeleccionado() {
        return tiposUnidadesSeleccionado;
    }

    public void setTiposUnidadesSeleccionado(TiposUnidades clasesPensionesSeleccionado) {
        this.tiposUnidadesSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
