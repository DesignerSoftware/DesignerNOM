/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Grupostiposentidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGruposTiposEntidadesInterface;
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
public class ControlGruposTiposEntidades implements Serializable {

    @EJB
    AdministrarGruposTiposEntidadesInterface administrarGruposTiposEntidades;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Grupostiposentidades> listGruposTiposEntidades;
    private List<Grupostiposentidades> filtrarGruposTiposEntidades;
    private List<Grupostiposentidades> crearGruposTiposEntidades;
    private List<Grupostiposentidades> modificarGruposTiposEntidades;
    private List<Grupostiposentidades> borrarGruposTiposEntidades;
    private Grupostiposentidades nuevoGruposTiposEntidades;
    private Grupostiposentidades duplicarGruposTiposEntidades;
    private Grupostiposentidades editarGruposTiposEntidades;
    private Grupostiposentidades grupoFactorRiesgoSeleccionado;
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
    private Integer backupCodigo;
    private String backupDescripcion;
    private String infoRegistro;

    public ControlGruposTiposEntidades() {
        listGruposTiposEntidades = null;
        crearGruposTiposEntidades = new ArrayList<Grupostiposentidades>();
        modificarGruposTiposEntidades = new ArrayList<Grupostiposentidades>();
        borrarGruposTiposEntidades = new ArrayList<Grupostiposentidades>();
        permitirIndex = true;
        editarGruposTiposEntidades = new Grupostiposentidades();
        nuevoGruposTiposEntidades = new Grupostiposentidades();
        duplicarGruposTiposEntidades = new Grupostiposentidades();
        guardado = true;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarGruposTiposEntidades.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
private String paginaAnterior;
public void recibirPagina(String pagina){paginaAnterior = pagina;}
public String redirigirPaginaAnterior(){return paginaAnterior;}

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlGruposTiposEntidades.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarGruposTiposEntidades.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlGruposTiposEntidades eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);
        System.err.println("permitirIndex  " + permitirIndex);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listGruposTiposEntidades.get(index).getSecuencia();
            if (tipoLista == 0) {
                backupCodigo = listGruposTiposEntidades.get(index).getCodigo();
                backupDescripcion = listGruposTiposEntidades.get(index).getNombre();
            } else if (tipoLista == 1) {
                backupCodigo = filtrarGruposTiposEntidades.get(index).getCodigo();
                backupDescripcion = filtrarGruposTiposEntidades.get(index).getNombre();
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlGruposTiposEntidades.asignarIndex \n");
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
            System.out.println("ERROR ControlGruposTiposEntidades.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            bandera = 0;
            filtrarGruposTiposEntidades = null;
            tipoLista = 0;
        }

        borrarGruposTiposEntidades.clear();
        crearGruposTiposEntidades.clear();
        modificarGruposTiposEntidades.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listGruposTiposEntidades = null;
        guardado = true;
        permitirIndex = true;
        getListGruposTiposEntidades();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listGruposTiposEntidades == null || listGruposTiposEntidades.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listGruposTiposEntidades.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            bandera = 0;
            filtrarGruposTiposEntidades = null;
            tipoLista = 0;
        }

        borrarGruposTiposEntidades.clear();
        crearGruposTiposEntidades.clear();
        modificarGruposTiposEntidades.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listGruposTiposEntidades = null;
        guardado = true;
        permitirIndex = true;
        getListGruposTiposEntidades();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listGruposTiposEntidades == null || listGruposTiposEntidades.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listGruposTiposEntidades.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            bandera = 0;
            filtrarGruposTiposEntidades = null;
            tipoLista = 0;
        }
    }

    public void modificarGruposTiposEntidades(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        boolean banderita1 = false;

        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearGruposTiposEntidades.contains(listGruposTiposEntidades.get(indice))) {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listGruposTiposEntidades.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listGruposTiposEntidades.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listGruposTiposEntidades.size(); j++) {
                            if (j != indice) {
                                if (listGruposTiposEntidades.get(indice).getCodigo() == listGruposTiposEntidades.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listGruposTiposEntidades.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listGruposTiposEntidades.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listGruposTiposEntidades.get(indice).setNombre(backupDescripcion);
                    } else if (listGruposTiposEntidades.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listGruposTiposEntidades.get(indice).setNombre(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarGruposTiposEntidades.isEmpty()) {
                            modificarGruposTiposEntidades.add(listGruposTiposEntidades.get(indice));
                        } else if (!modificarGruposTiposEntidades.contains(listGruposTiposEntidades.get(indice))) {
                            modificarGruposTiposEntidades.add(listGruposTiposEntidades.get(indice));
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
                    RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listGruposTiposEntidades.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listGruposTiposEntidades.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listGruposTiposEntidades.size(); j++) {
                            if (j != indice) {
                                if (listGruposTiposEntidades.get(indice).getCodigo() == listGruposTiposEntidades.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listGruposTiposEntidades.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listGruposTiposEntidades.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listGruposTiposEntidades.get(indice).setNombre(backupDescripcion);
                    } else if (listGruposTiposEntidades.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listGruposTiposEntidades.get(indice).setNombre(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (guardado == true) {
                            guardado = false;
                        }
                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                    RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            } else {

                if (!crearGruposTiposEntidades.contains(filtrarGruposTiposEntidades.get(indice))) {
                    if (filtrarGruposTiposEntidades.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarGruposTiposEntidades.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarGruposTiposEntidades.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposTiposEntidades.get(indice).getCodigo() == listGruposTiposEntidades.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listGruposTiposEntidades.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposTiposEntidades.get(indice).getCodigo() == listGruposTiposEntidades.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarGruposTiposEntidades.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarGruposTiposEntidades.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarGruposTiposEntidades.get(indice).setNombre(backupDescripcion);
                    }
                    if (filtrarGruposTiposEntidades.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarGruposTiposEntidades.get(indice).setNombre(backupDescripcion);
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarGruposTiposEntidades.isEmpty()) {
                            modificarGruposTiposEntidades.add(filtrarGruposTiposEntidades.get(indice));
                        } else if (!modificarGruposTiposEntidades.contains(filtrarGruposTiposEntidades.get(indice))) {
                            modificarGruposTiposEntidades.add(filtrarGruposTiposEntidades.get(indice));
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
                    if (filtrarGruposTiposEntidades.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarGruposTiposEntidades.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarGruposTiposEntidades.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposTiposEntidades.get(indice).getCodigo() == listGruposTiposEntidades.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listGruposTiposEntidades.size(); j++) {
                            if (j != indice) {
                                if (filtrarGruposTiposEntidades.get(indice).getCodigo() == listGruposTiposEntidades.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarGruposTiposEntidades.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarGruposTiposEntidades.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarGruposTiposEntidades.get(indice).setNombre(backupDescripcion);
                    }
                    if (filtrarGruposTiposEntidades.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarGruposTiposEntidades.get(indice).setNombre(backupDescripcion);
                    }

                    if (banderita == true && banderita1 == true) {
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
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoGruposTiposEntidades() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoGruposTiposEntidades");
                if (!modificarGruposTiposEntidades.isEmpty() && modificarGruposTiposEntidades.contains(listGruposTiposEntidades.get(index))) {
                    int modIndex = modificarGruposTiposEntidades.indexOf(listGruposTiposEntidades.get(index));
                    modificarGruposTiposEntidades.remove(modIndex);
                    borrarGruposTiposEntidades.add(listGruposTiposEntidades.get(index));
                } else if (!crearGruposTiposEntidades.isEmpty() && crearGruposTiposEntidades.contains(listGruposTiposEntidades.get(index))) {
                    int crearIndex = crearGruposTiposEntidades.indexOf(listGruposTiposEntidades.get(index));
                    crearGruposTiposEntidades.remove(crearIndex);
                } else {
                    borrarGruposTiposEntidades.add(listGruposTiposEntidades.get(index));
                }
                listGruposTiposEntidades.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoGruposTiposEntidades ");
                if (!modificarGruposTiposEntidades.isEmpty() && modificarGruposTiposEntidades.contains(filtrarGruposTiposEntidades.get(index))) {
                    int modIndex = modificarGruposTiposEntidades.indexOf(filtrarGruposTiposEntidades.get(index));
                    modificarGruposTiposEntidades.remove(modIndex);
                    borrarGruposTiposEntidades.add(filtrarGruposTiposEntidades.get(index));
                } else if (!crearGruposTiposEntidades.isEmpty() && crearGruposTiposEntidades.contains(filtrarGruposTiposEntidades.get(index))) {
                    int crearIndex = crearGruposTiposEntidades.indexOf(filtrarGruposTiposEntidades.get(index));
                    crearGruposTiposEntidades.remove(crearIndex);
                } else {
                    borrarGruposTiposEntidades.add(filtrarGruposTiposEntidades.get(index));
                }
                int VCIndex = listGruposTiposEntidades.indexOf(filtrarGruposTiposEntidades.get(index));
                listGruposTiposEntidades.remove(VCIndex);
                filtrarGruposTiposEntidades.remove(index);

            }
            infoRegistro = "Cantidad de registros: " + listGruposTiposEntidades.size();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
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
        BigInteger contarTSgruposTiposEntidadesTipoEntidad;
        BigInteger contarSoProActividadesGrupoFactorRiesgo;
        BigInteger contarTiposEntidadesGrupoTipoEntidad;

        try {
            System.err.println("Control Secuencia de ControlGruposTiposEntidades ");
            if (tipoLista == 0) {
                contarTSgruposTiposEntidadesTipoEntidad = administrarGruposTiposEntidades.contarTSgruposTiposEntidadesTipoEntidad(listGruposTiposEntidades.get(index).getSecuencia());
                contarTiposEntidadesGrupoTipoEntidad = administrarGruposTiposEntidades.contarTiposEntidadesGrupoTipoEntidad(listGruposTiposEntidades.get(index).getSecuencia());
            } else {
                contarTSgruposTiposEntidadesTipoEntidad = administrarGruposTiposEntidades.contarTSgruposTiposEntidadesTipoEntidad(filtrarGruposTiposEntidades.get(index).getSecuencia());
                contarTiposEntidadesGrupoTipoEntidad = administrarGruposTiposEntidades.contarTiposEntidadesGrupoTipoEntidad(filtrarGruposTiposEntidades.get(index).getSecuencia());
            }
            if (contarTSgruposTiposEntidadesTipoEntidad.equals(new BigInteger("0")) && contarTiposEntidadesGrupoTipoEntidad.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoGruposTiposEntidades();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;
                contarTSgruposTiposEntidadesTipoEntidad = new BigInteger("-1");
                contarTiposEntidadesGrupoTipoEntidad = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlGruposTiposEntidades verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarGruposTiposEntidades.isEmpty() || !crearGruposTiposEntidades.isEmpty() || !modificarGruposTiposEntidades.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarGruposTiposEntidades() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarGruposTiposEntidades");
            if (!borrarGruposTiposEntidades.isEmpty()) {
                administrarGruposTiposEntidades.borrarGruposTiposEntidades(borrarGruposTiposEntidades);
                //mostrarBorrados
                registrosBorrados = borrarGruposTiposEntidades.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarGruposTiposEntidades.clear();
            }
            if (!modificarGruposTiposEntidades.isEmpty()) {
                administrarGruposTiposEntidades.modificarGruposTiposEntidades(modificarGruposTiposEntidades);
                modificarGruposTiposEntidades.clear();
            }
            if (!crearGruposTiposEntidades.isEmpty()) {
                administrarGruposTiposEntidades.crearGruposTiposEntidades(crearGruposTiposEntidades);
                crearGruposTiposEntidades.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listGruposTiposEntidades = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarGruposTiposEntidades = listGruposTiposEntidades.get(index);
            }
            if (tipoLista == 1) {
                editarGruposTiposEntidades = filtrarGruposTiposEntidades.get(index);
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

    public void agregarNuevoGruposTiposEntidades() {
        System.out.println("agregarNuevoGruposTiposEntidades");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoGruposTiposEntidades.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoGruposTiposEntidades.getCodigo());

            for (int x = 0; x < listGruposTiposEntidades.size(); x++) {
                if (listGruposTiposEntidades.get(x).getCodigo() == nuevoGruposTiposEntidades.getCodigo()) {
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
        if (nuevoGruposTiposEntidades.getNombre() == null || nuevoGruposTiposEntidades.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
                bandera = 0;
                filtrarGruposTiposEntidades = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoGruposTiposEntidades.setSecuencia(l);

            crearGruposTiposEntidades.add(nuevoGruposTiposEntidades);

            listGruposTiposEntidades.add(nuevoGruposTiposEntidades);
            infoRegistro = "Cantidad de registros: " + listGruposTiposEntidades.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            nuevoGruposTiposEntidades = new Grupostiposentidades();
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroGruposTiposEntidades').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoGruposTiposEntidades() {
        System.out.println("limpiarNuevoGruposTiposEntidades");
        nuevoGruposTiposEntidades = new Grupostiposentidades();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoGruposTiposEntidades() {
        System.out.println("duplicandoGruposTiposEntidades");
        if (index >= 0) {
            duplicarGruposTiposEntidades = new Grupostiposentidades();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarGruposTiposEntidades.setSecuencia(l);
                duplicarGruposTiposEntidades.setCodigo(listGruposTiposEntidades.get(index).getCodigo());
                duplicarGruposTiposEntidades.setNombre(listGruposTiposEntidades.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarGruposTiposEntidades.setSecuencia(l);
                duplicarGruposTiposEntidades.setCodigo(filtrarGruposTiposEntidades.get(index).getCodigo());
                duplicarGruposTiposEntidades.setNombre(filtrarGruposTiposEntidades.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroGruposTiposEntidades').show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarGruposTiposEntidades.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarGruposTiposEntidades.getNombre());

        if (duplicarGruposTiposEntidades.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listGruposTiposEntidades.size(); x++) {
                if (listGruposTiposEntidades.get(x).getCodigo() == duplicarGruposTiposEntidades.getCodigo()) {
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
        if (duplicarGruposTiposEntidades.getNombre() == null || duplicarGruposTiposEntidades.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarGruposTiposEntidades.getSecuencia() + "  " + duplicarGruposTiposEntidades.getCodigo());
            if (crearGruposTiposEntidades.contains(duplicarGruposTiposEntidades)) {
                System.out.println("Ya lo contengo.");
            }
            listGruposTiposEntidades.add(duplicarGruposTiposEntidades);
            crearGruposTiposEntidades.add(duplicarGruposTiposEntidades);
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            index = -1;
            infoRegistro = "Cantidad de registros: " + listGruposTiposEntidades.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
                bandera = 0;
                filtrarGruposTiposEntidades = null;
                tipoLista = 0;
            }
            duplicarGruposTiposEntidades = new Grupostiposentidades();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroGruposTiposEntidades').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarGruposTiposEntidades() {
        duplicarGruposTiposEntidades = new Grupostiposentidades();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposTiposEntidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "GRUPOSTIPOSENTIDADES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposTiposEntidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "GRUPOSTIPOSENTIDADES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listGruposTiposEntidades.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "GRUPOSTIPOSENTIDADES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("GRUPOSTIPOSENTIDADES")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Grupostiposentidades> getListGruposTiposEntidades() {
        if (listGruposTiposEntidades == null) {
            listGruposTiposEntidades = administrarGruposTiposEntidades.consultarGruposTiposEntidades();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listGruposTiposEntidades == null || listGruposTiposEntidades.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listGruposTiposEntidades.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listGruposTiposEntidades;
    }

    public void setListGruposTiposEntidades(List<Grupostiposentidades> listGruposTiposEntidades) {
        this.listGruposTiposEntidades = listGruposTiposEntidades;
    }

    public List<Grupostiposentidades> getFiltrarGruposTiposEntidades() {
        return filtrarGruposTiposEntidades;
    }

    public void setFiltrarGruposTiposEntidades(List<Grupostiposentidades> filtrarGruposTiposEntidades) {
        this.filtrarGruposTiposEntidades = filtrarGruposTiposEntidades;
    }

    public Grupostiposentidades getNuevoGruposTiposEntidades() {
        return nuevoGruposTiposEntidades;
    }

    public void setNuevoGruposTiposEntidades(Grupostiposentidades nuevoGruposTiposEntidades) {
        this.nuevoGruposTiposEntidades = nuevoGruposTiposEntidades;
    }

    public Grupostiposentidades getDuplicarGruposTiposEntidades() {
        return duplicarGruposTiposEntidades;
    }

    public void setDuplicarGruposTiposEntidades(Grupostiposentidades duplicarGruposTiposEntidades) {
        this.duplicarGruposTiposEntidades = duplicarGruposTiposEntidades;
    }

    public Grupostiposentidades getEditarGruposTiposEntidades() {
        return editarGruposTiposEntidades;
    }

    public void setEditarGruposTiposEntidades(Grupostiposentidades editarGruposTiposEntidades) {
        this.editarGruposTiposEntidades = editarGruposTiposEntidades;
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

    public Grupostiposentidades getGrupoFactorRiesgoSeleccionado() {
        return grupoFactorRiesgoSeleccionado;
    }

    public void setGrupoFactorRiesgoSeleccionado(Grupostiposentidades grupoFactorRiesgoSeleccionado) {
        this.grupoFactorRiesgoSeleccionado = grupoFactorRiesgoSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
