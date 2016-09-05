/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Periodicidades;
import Entidades.Unidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPeriodicidadesInterface;
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
public class ControlPeriodicidades implements Serializable {

    @EJB
    AdministrarPeriodicidadesInterface administrarPeriodicidades;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;

//LISTA CENTRO COSTO
    private List<Periodicidades> listPeriodicidades;
    private List<Periodicidades> listPeriodicidadesBoton;
    private List<Periodicidades> filtrarPeriodicidades;
    private List<Periodicidades> crearPeriodicidades;
    private List<Periodicidades> modificarPeriodicidades;
    private List<Periodicidades> borrarPeriodicidades;
    private Periodicidades nuevaPeriodicidad;
    private Periodicidades duplicarPeriodicidad;
    private Periodicidades editarUnidad;
    private Periodicidades periocidadSeleccionadaTabla;
    private Column codigoCC, nombreUnidad,
            tipoUnidad, codigoUnidad, codigoUnidadbase,
            unidadBase;

    //AUTOCOMPLETAR
    private String unidadCodigo, unidadNombre, unidadBaseCodigo, unidadBaseDescripcion;
    private List<Unidades> listaUnidades;
    private List<Unidades> filtradoUnidades;
    private Unidades unidadSeleccionada;
    private List<Periodicidades> filterPericiodidades;
    private String nuevaUnidad;

    private Periodicidades PericiodidadesSeleccionado;
    private boolean banderaSeleccionPericiodidades;
    private boolean mostrarTodos;
    private int tamano;
    private String paginaAnterior;
    private Integer backUpCodigo;
    private String backUpDescripcion;

    public ControlPeriodicidades() {
        permitirIndex = true;
        listPeriodicidades = null;
        listPeriodicidadesBoton = null;
        crearPeriodicidades = new ArrayList<Periodicidades>();
        modificarPeriodicidades = new ArrayList<Periodicidades>();
        borrarPeriodicidades = new ArrayList<Periodicidades>();
        editarUnidad = new Periodicidades();
        nuevaPeriodicidad = new Periodicidades();
        nuevaPeriodicidad.setUnidad(new Unidades());
        nuevaPeriodicidad.setUnidadbase(new Unidades());
        duplicarPeriodicidad = new Periodicidades();
        listaUnidades = null;
        aceptar = true;
        guardado = true;
        banderaSeleccionPericiodidades = false;
        tamano = 270;
        mostrarTodos = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPeriodicidades.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A CONTROLPERIODICIDADES eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarPeriodicidades.size();
            context.update("form:informacionRegistro");

        } catch (Exception e) {
            System.out.println("ERROR CONTROLPERIODICIDADES eventoFiltrar ERROR===" + e);
        }
    }

    public void refrescar() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            tamano = 270;
            codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
            codigoCC.setFilterStyle("display: none; visibility: hidden;");
            nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
            nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
            tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
            tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
            codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
            codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
            codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
            codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
            unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
            unidadBase.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
            bandera = 0;
            filtrarPeriodicidades = null;
            tipoLista = 0;
        }
        crearPeriodicidades.clear();
        borrarPeriodicidades.clear();
        modificarPeriodicidades.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listPeriodicidades = null;
        guardado = true;
        permitirIndex = true;
        mostrarTodos = true;

        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:datosPeriodicidades");
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("BETA CENTRO COSTO TIPO LISTA = " + tipoLista);
        System.err.println("PERMITIR INDEX = " + permitirIndex);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            System.err.println("CAMBIAR INDICE CUALCELDA = " + cualCelda);
            secRegistro = listPeriodicidades.get(index).getSecuencia();
            System.err.println("Sec Registro = " + secRegistro);
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listPeriodicidades.get(index).getCodigo();
                    System.err.println("backUpCodigo = " + backUpCodigo);
                } else {
                    backUpCodigo = filtrarPeriodicidades.get(index).getCodigo();
                    System.err.println("backUpCodigo = " + backUpCodigo);
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listPeriodicidades.get(index).getNombre();
                    System.err.println("backUpDescripcion = " + backUpDescripcion);
                } else {
                    backUpDescripcion = filtrarPeriodicidades.get(index).getNombre();
                    System.err.println("backUpDescripcion = " + backUpDescripcion);
                }
            }
            if (cualCelda == 2) {
                if (tipoLista == 0) {
                    unidadCodigo = listPeriodicidades.get(index).getUnidad().getCodigo();
                    System.err.println("grupoTipoUnidadAutocompletar = " + unidadCodigo);
                } else {
                    unidadCodigo = filtrarPeriodicidades.get(index).getUnidad().getCodigo();
                }
            }
            if (cualCelda == 3) {
                if (tipoLista == 0) {
                    unidadNombre = listPeriodicidades.get(index).getUnidad().getNombre();
                    System.err.println("grupoTipoUnidadAutocompletar = " + unidadNombre);
                } else {
                    unidadNombre = filtrarPeriodicidades.get(index).getUnidad().getNombre();
                }
            }
            if (cualCelda == 4) {
                if (tipoLista == 0) {
                    unidadBaseCodigo = listPeriodicidades.get(index).getUnidadbase().getCodigo();
                    System.err.println("grupoTipoUnidadAutocompletar = " + unidadBaseCodigo);
                } else {
                    unidadBaseCodigo = filtrarPeriodicidades.get(index).getUnidadbase().getCodigo();
                }
            }
            if (cualCelda == 5) {
                if (tipoLista == 0) {
                    unidadBaseDescripcion = listPeriodicidades.get(index).getUnidadbase().getNombre();
                    System.err.println("grupoTipoUnidadAutocompletar = " + unidadBaseDescripcion);
                } else {
                    unidadBaseDescripcion = filtrarPeriodicidades.get(index).getUnidadbase().getNombre();
                }
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void modificandoUnidad(int indice, String confirmarCambio, String valorConfirmar) {

        System.err.println("ENTRE A MODIFICAR CENTROCOSTO");
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        boolean banderita = false;
        boolean banderita1 = false;
        int contador = 0;
        Short a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR CENTROCOSTO, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearPeriodicidades.contains(listPeriodicidades.get(indice))) {
                    if (listPeriodicidades.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPeriodicidades.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listPeriodicidades.size(); j++) {
                            if (j != indice) {
                                if (listPeriodicidades.get(indice).getCodigo().equals(listPeriodicidades.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            listPeriodicidades.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (listPeriodicidades.get(indice).getNombre().isEmpty()) {
                        listPeriodicidades.get(indice).setNombre(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                    } else if (listPeriodicidades.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listPeriodicidades.get(indice).setNombre(backUpDescripcion);
                        banderita1 = false;
                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarPeriodicidades.isEmpty()) {
                            modificarPeriodicidades.add(listPeriodicidades.get(indice));
                        } else if (!modificarPeriodicidades.contains(listPeriodicidades.get(indice))) {
                            modificarPeriodicidades.add(listPeriodicidades.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;

                        }
                        context.update("form:ACEPTAR");

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listPeriodicidades.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPeriodicidades.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listPeriodicidades.size(); j++) {
                            if (j != indice) {
                                if (listPeriodicidades.get(indice).getCodigo().equals(listPeriodicidades.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            listPeriodicidades.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (listPeriodicidades.get(indice).getNombre().isEmpty()) {
                        listPeriodicidades.get(indice).setNombre(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                    } else if (listPeriodicidades.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listPeriodicidades.get(indice).setNombre(backUpDescripcion);
                        banderita1 = false;
                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {

                        if (guardado == true) {
                            guardado = false;

                        }
                        context.update("form:ACEPTAR");

                    } else {
                        context.update("form:validacionModificar");
                        context.execute("validacionModificar.show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearPeriodicidades.contains(filtrarPeriodicidades.get(indice))) {
                    if (filtrarPeriodicidades.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarPeriodicidades.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listPeriodicidades.size(); j++) {
                            if (j != indice) {
                                if (filtrarPeriodicidades.get(indice).getCodigo().equals(listPeriodicidades.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarPeriodicidades.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }
                    }

                    if (filtrarPeriodicidades.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarPeriodicidades.get(indice).setNombre(backUpDescripcion);
                        banderita1 = false;
                    }
                    if (filtrarPeriodicidades.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarPeriodicidades.get(indice).setNombre(backUpDescripcion);
                        banderita1 = false;
                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarPeriodicidades.isEmpty()) {
                            modificarPeriodicidades.add(filtrarPeriodicidades.get(indice));
                        } else if (!modificarPeriodicidades.contains(filtrarPeriodicidades.get(indice))) {
                            modificarPeriodicidades.add(filtrarPeriodicidades.get(indice));
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
                    if (filtrarPeriodicidades.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarPeriodicidades.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listPeriodicidades.size(); j++) {
                            if (j != indice) {
                                if (filtrarPeriodicidades.get(indice).getCodigo().equals(listPeriodicidades.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            filtrarPeriodicidades.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }
                    }

                    if (filtrarPeriodicidades.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarPeriodicidades.get(indice).setNombre(backUpDescripcion);
                        banderita1 = false;
                    }
                    if (filtrarPeriodicidades.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarPeriodicidades.get(indice).setNombre(backUpDescripcion);
                        banderita1 = false;
                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {

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
            context.update("form:datosPeriodicidades");
        } else if (confirmarCambio.equalsIgnoreCase("UNIDADES")) {
            System.err.println("ENTRE A MODIFICAR, CONFIRMAR CAMBIO ES UNIDADES");
            System.err.println("UNIDAD NOMBRE =  " + unidadNombre);
            System.err.println("UNIDAD CODIGO =  " + unidadCodigo);
            System.err.println("LISTA PERIODICIDADES INDICE NOMBRE UNIDAD " + listPeriodicidades.get(indice).getUnidad().getNombre());
            System.err.println("LISTA PERIODICIDADES INDICE CODIGO UNIDAD " + listPeriodicidades.get(indice).getUnidad().getCodigo());
            if (cualCelda == 2) {
                if (tipoLista == 0) {
                    System.err.println("COMPLETAR CODIGO UNIDAD " + unidadCodigo);
                    listPeriodicidades.get(indice).getUnidad().setCodigo(unidadCodigo);
                } else {
                    filtrarPeriodicidades.get(indice).getUnidad().setCodigo(unidadCodigo);
                }
                getListaUnidades();
                System.out.println("VALOR A CONFIRMAR :  " + valorConfirmar.toUpperCase());
                for (int i = 0; i < listaUnidades.size(); i++) {
                    if (listaUnidades.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listPeriodicidades.get(indice).setUnidad(listaUnidades.get(indiceUnicoElemento));
                        if (modificarPeriodicidades.isEmpty()) {
                            modificarPeriodicidades.add(listPeriodicidades.get(indice));
                        } else if (!modificarPeriodicidades.contains(listPeriodicidades.get(indice))) {
                            modificarPeriodicidades.add(listPeriodicidades.get(indice));
                        }
                    } else {
                        filtrarPeriodicidades.get(indice).setUnidad(listaUnidades.get(indiceUnicoElemento));
                        if (modificarPeriodicidades.isEmpty()) {
                            modificarPeriodicidades.add(filtrarPeriodicidades.get(indice));
                        } else if (!modificarPeriodicidades.contains(filtrarPeriodicidades.get(indice))) {
                            modificarPeriodicidades.add(filtrarPeriodicidades.get(indice));
                        }
                    }
                    listaUnidades.clear();
                    listaUnidades = null;
                    getListaUnidades();
                    guardado = false;
                    context.update("form:ACEPTAR");

                } else {
                    permitirIndex = false;
                    context.update("form:tiposPeriodicidadesDialogo");
                    context.execute("tiposPeriodicidadesDialogo.show()");
                    tipoActualizacion = 0;
                }
            }
            if (cualCelda == 3) {
                if (tipoLista == 0) {
                    System.err.println("COMPLETAR   UNIDAD NOMBRE " + unidadNombre);
                    listPeriodicidades.get(indice).getUnidad().setNombre(unidadNombre);
                } else {

                    filtrarPeriodicidades.get(indice).getUnidad().setNombre(unidadNombre);
                }
                getListaUnidades();
                for (int i = 0; i < listaUnidades.size(); i++) {
                    if (listaUnidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listPeriodicidades.get(indice).setUnidad(listaUnidades.get(indiceUnicoElemento));
                        if (modificarPeriodicidades.isEmpty()) {
                            modificarPeriodicidades.add(listPeriodicidades.get(indice));
                        } else if (!modificarPeriodicidades.contains(listPeriodicidades.get(indice))) {
                            modificarPeriodicidades.add(listPeriodicidades.get(indice));
                        }
                    } else {
                        filtrarPeriodicidades.get(indice).setUnidad(listaUnidades.get(indiceUnicoElemento));
                        if (modificarPeriodicidades.isEmpty()) {
                            modificarPeriodicidades.add(filtrarPeriodicidades.get(indice));
                        } else if (!modificarPeriodicidades.contains(filtrarPeriodicidades.get(indice))) {
                            modificarPeriodicidades.add(filtrarPeriodicidades.get(indice));
                        }
                    }
                    listaUnidades.clear();
                    listaUnidades = null;
                    getListaUnidades();
                    guardado = false;
                    context.update("form:ACEPTAR");

                } else {
                    permitirIndex = false;
                    context.update("form:tiposPeriodicidadesDialogo");
                    context.execute("tiposPeriodicidadesDialogo.show()");
                    tipoActualizacion = 0;
                }
            }

        } else if (confirmarCambio.equalsIgnoreCase("UNIDADESBASE")) {
            System.err.println("ENTRE A MODIFICAR, CONFIRMAR CAMBIO ES UNIDADES");
            System.err.println("UNIDAD BASE CODIGO =  " + unidadBaseCodigo);
            System.err.println("UNIDAD BASE DESCRIPCION =  " + unidadBaseDescripcion);
            System.err.println("LISTA PERIODICIDADES INDICE BASEUNIDAD DESCRIPCION " + listPeriodicidades.get(indice).getUnidadbase().getNombre());
            System.err.println("LISTA PERIODICIDADES INDICE BASEUNIDAD CODIGO " + listPeriodicidades.get(indice).getUnidadbase().getCodigo());
            if (cualCelda == 4) {
                if (tipoLista == 0) {
                    System.err.println("COMPLETAR BASE CODIGO UNIDAD " + unidadBaseCodigo);
                    listPeriodicidades.get(indice).getUnidadbase().setCodigo(unidadBaseCodigo);
                } else {
                    filtrarPeriodicidades.get(indice).getUnidadbase().setCodigo(unidadBaseCodigo);
                }
                getListaUnidades();
                System.out.println("VALOR A CONFIRMAR :  " + valorConfirmar.toUpperCase());
                for (int i = 0; i < listaUnidades.size(); i++) {
                    if (listaUnidades.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listPeriodicidades.get(indice).setUnidadbase(listaUnidades.get(indiceUnicoElemento));
                        if (modificarPeriodicidades.isEmpty()) {
                            modificarPeriodicidades.add(listPeriodicidades.get(indice));
                        } else if (!modificarPeriodicidades.contains(listPeriodicidades.get(indice))) {
                            modificarPeriodicidades.add(listPeriodicidades.get(indice));
                        }
                    } else {
                        filtrarPeriodicidades.get(indice).setUnidadbase(listaUnidades.get(indiceUnicoElemento));
                        if (modificarPeriodicidades.isEmpty()) {
                            modificarPeriodicidades.add(filtrarPeriodicidades.get(indice));
                        } else if (!modificarPeriodicidades.contains(filtrarPeriodicidades.get(indice))) {
                            modificarPeriodicidades.add(filtrarPeriodicidades.get(indice));
                        }
                    }
                    listaUnidades.clear();
                    listaUnidades = null;
                    getListaUnidades();
                    guardado = false;
                    context.update("form:ACEPTAR");

                } else {
                    permitirIndex = false;
                    context.update("form:tiposPeriodicidadesDialogo");
                    context.execute("tiposPeriodicidadesDialogo.show()");
                    tipoActualizacion = 0;
                }
            }
            if (cualCelda == 5) {
                if (tipoLista == 0) {
                    System.err.println("COMPLETAR BASE DESCRIPCION " + unidadBaseDescripcion);
                    listPeriodicidades.get(indice).getUnidadbase().setNombre(unidadBaseDescripcion);
                } else {

                    filtrarPeriodicidades.get(indice).getUnidadbase().setNombre(unidadBaseDescripcion);
                }
                getListaUnidades();
                for (int i = 0; i < listaUnidades.size(); i++) {
                    if (listaUnidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listPeriodicidades.get(indice).setUnidadbase(listaUnidades.get(indiceUnicoElemento));
                        if (modificarPeriodicidades.isEmpty()) {
                            modificarPeriodicidades.add(listPeriodicidades.get(indice));
                        } else if (!modificarPeriodicidades.contains(listPeriodicidades.get(indice))) {
                            modificarPeriodicidades.add(listPeriodicidades.get(indice));
                        }
                    } else {
                        filtrarPeriodicidades.get(indice).setUnidadbase(listaUnidades.get(indiceUnicoElemento));
                        if (modificarPeriodicidades.isEmpty()) {
                            modificarPeriodicidades.add(filtrarPeriodicidades.get(indice));
                        } else if (!modificarPeriodicidades.contains(filtrarPeriodicidades.get(indice))) {
                            modificarPeriodicidades.add(filtrarPeriodicidades.get(indice));
                        }
                    }
                    listaUnidades.clear();
                    listaUnidades = null;
                    getListaUnidades();
                    guardado = false;
                    context.update("form:ACEPTAR");

                } else {
                    permitirIndex = false;
                    context.update("form:tiposPeriodicidadesDialogo");
                    context.execute("tiposPeriodicidadesDialogo.show()");
                    tipoActualizacion = 0;
                }
            }

        }
        context.update("form:datosPeriodicidades");

    }

    private String infoRegistro;

    public void cancelarModificacion() {
        try {
            System.out.println("entre a CONTROLPERIODICIDADES.cancelarModificacion");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                //0
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
                nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
                //2
                tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
                tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
                //3 
                codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
                codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
                //4
                codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
                codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
                //5 
                unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
                unidadBase.setFilterStyle("display: none; visibility: hidden;");

                bandera = 0;
                filtrarPeriodicidades = null;
                tipoLista = 0;
            }

            borrarPeriodicidades.clear();
            crearPeriodicidades.clear();
            modificarPeriodicidades.clear();
            getListPeriodicidades();
            RequestContext context = RequestContext.getCurrentInstance();
            if (listPeriodicidades == null || listPeriodicidades.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listPeriodicidades.size();
            }
            context.update("form:informacionRegistro");
            index = -1;
            k = 0;
            listPeriodicidades = null;
            guardado = true;
            permitirIndex = true;
            mostrarTodos = false;
            context.update("form:MOSTRARTODOS");

            context.update("form:datosPeriodicidades");
            context.update("form:ACEPTAR");
        } catch (Exception E) {
            System.out.println("ERROR CONTROLPERIODICIDADES.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void salir() {
        try {
            System.out.println("entre a CONTROLPERIODICIDADES.cancelarModificacion");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                //0
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
                nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
                //2
                tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
                tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
                //3 
                codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
                codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
                //4
                codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
                codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
                //5 
                unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
                unidadBase.setFilterStyle("display: none; visibility: hidden;");

                bandera = 0;
                filtrarPeriodicidades = null;
                tipoLista = 0;
            }

            borrarPeriodicidades.clear();
            crearPeriodicidades.clear();
            modificarPeriodicidades.clear();
            getListPeriodicidades();
            RequestContext context = RequestContext.getCurrentInstance();
            if (listPeriodicidades == null || listPeriodicidades.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listPeriodicidades.size();
            }
            context.update("form:informacionRegistro");
            index = -1;
            k = 0;
            listPeriodicidades = null;
            guardado = true;
            permitirIndex = true;
            mostrarTodos = false;
            context.update("form:MOSTRARTODOS");

            context.update("form:datosPeriodicidades");
            context.update("form:ACEPTAR");
        } catch (Exception E) {
            System.out.println("ERROR CONTROLPERIODICIDADES.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A CONTROLPERIODICIDADES.asignarIndex \n");
            index = indice;
            RequestContext context = RequestContext.getCurrentInstance();

            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dig == 2) {
                context.update("form:tiposPeriodicidadesDialogo");
                context.execute("tiposPeriodicidadesDialogo.show()");
                dig = -1;
            }
            if (dig == 3) {
                context.update("form:tiposPeriodicidadesDialogo");
                context.execute("tiposPeriodicidadesDialogo.show()");
                dig = -1;
            }
            if (dig == 4) {
                context.update("form:unidadesBaseDialogo");
                context.execute("unidadesBaseDialogo.show()");
                dig = -1;
            }
            if (dig == 5) {
                context.update("form:unidadesBaseDialogo");
                context.execute("unidadesBaseDialogo.show()");
                dig = -1;
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLPERIODICIDADES.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarUnidad() {
        System.out.println("\n ENTRE A CONTROLPERIODICIDADES.actualizarUnidad \n");
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("\n ENTRE A CONTROLPERIODICIDADES.actualizarUnidad TIPOACTUALIZACION====" + tipoActualizacion);
            if (tipoActualizacion == 0) {
                listPeriodicidades.get(index).setUnidad(unidadSeleccionada);
                if (!crearPeriodicidades.contains(listPeriodicidades.get(index))) {
                    if (modificarPeriodicidades.isEmpty()) {
                        modificarPeriodicidades.add(listPeriodicidades.get(index));
                    } else if (!modificarPeriodicidades.contains(listPeriodicidades.get(index))) {
                        modificarPeriodicidades.add(listPeriodicidades.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                context.update("form:datosPeriodicidades");
            } else if (tipoActualizacion == 1) {
                System.out.println("ENTRO A ACTUALIZAR EL NOMBRE DE UNIDAD ");
                nuevaPeriodicidad.setUnidad(unidadSeleccionada);
                System.out.println("UNIDAD SELECCIONADA  : " + nuevaPeriodicidad.getUnidad().getNombre());
                context.update("formularioDialogos:nuevaTipoUnidads");
                context.update("formularioDialogos:nuevaTipoUnidadsCodigo");
                context.update("form:datosPeriodicidades");
            } else if (tipoActualizacion == 2) {
                duplicarPeriodicidad.setUnidad(unidadSeleccionada);
                context.update("formularioDialogos:duplicarCodigoUnidades");
                context.update("formularioDialogos:duplicarTipoUnidads");
                context.update("form:datosPeriodicidades");
            }
            filtradoUnidades = null;
            unidadSeleccionada = null;
            aceptar = true;
            index = -1;
            tipoActualizacion = -1;
            permitirIndex = true;
            context.reset("form:lovTipoPeriodicidades:globalFilter");
            context.execute("lovTipoPeriodicidades.clearFilters()");
            context.execute("tiposPeriodicidadesDialogo.hide()");
        } catch (Exception e) {
            System.out.println("ERROR BETA .actualizarUnidad ERROR============" + e.getMessage());
        }
    }

    public void actualizarUnidadBase() {
        System.out.println("\n ENTRE A CONTROLPERIODICIDADES.actualizarUnidad \n");
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("\n ENTRE A CONTROLPERIODICIDADES.actualizarUnidad TIPOACTUALIZACION====" + tipoActualizacion);
            if (tipoActualizacion == 0) {
                listPeriodicidades.get(index).setUnidadbase(unidadSeleccionada);
                if (!crearPeriodicidades.contains(listPeriodicidades.get(index))) {
                    if (modificarPeriodicidades.isEmpty()) {
                        modificarPeriodicidades.add(listPeriodicidades.get(index));
                    } else if (!modificarPeriodicidades.contains(listPeriodicidades.get(index))) {
                        modificarPeriodicidades.add(listPeriodicidades.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }

                }
                context.update("form:datosPeriodicidades");
            } else if (tipoActualizacion == 1) {
                // context.reset("formularioDialogos:nuevaTipoPeriodicidades");
                System.out.println("Entro actualizar centro costo nuevo rgistro");
                nuevaPeriodicidad.setUnidadbase(unidadSeleccionada);
                System.out.println("Nuevo Unidad Base Seleccionado: " + nuevaPeriodicidad.getUnidadbase().getNombre());
                context.update("formularioDialogos:nuevaCodigoBase");
                context.update("formularioDialogos:nuevaNombreBase");
            } else if (tipoActualizacion == 2) {
                duplicarPeriodicidad.setUnidadbase(unidadSeleccionada);
                context.update("formularioDialogos:duplicarCodigoUnidadesBase");
                context.update("formularioDialogos:duplicarDescripcionUnidadesBase");
            }
            filtradoUnidades = null;
            unidadSeleccionada = null;
            aceptar = true;
            index = -1;
            tipoActualizacion = -1;
            permitirIndex = true;
            context.reset("form:lovUnidadesBase:globalFilter");
            context.execute("lovUnidadesBase.clearFilters()");
            context.execute("unidadesBaseDialogo.hide()");
        } catch (Exception e) {
            System.out.println("ERROR BETA .actualizarUnidad ERROR============" + e.getMessage());
        }
    }

    public void cancelarCambioTiposUnidad() {
        try {
            filtradoUnidades = null;
            unidadSeleccionada = null;
            aceptar = true;
            index = -1;
            tipoActualizacion = -1;
            permitirIndex = true;
            RequestContext context = RequestContext.getCurrentInstance();
            context.reset("form:lovTipoPeriodicidades:globalFilter");
            context.execute("lovTipoPeriodicidades.clearFilters()");
            context.execute("tiposPeriodicidadesDialogo.hide()");
        } catch (Exception e) {
            System.out.println("ERROR CONTROLPERIODICIDADES.cancelarCambioUnidad ERROR=====" + e.getMessage());
        }
    }

    public void cancelarCambioUnidadBase() {
        filtradoUnidades = null;
        unidadSeleccionada = null;
        aceptar = true;
        index = -1;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovUnidadesBase:globalFilter");
        context.execute("lovUnidadesBase.clearFilters()");
        context.execute("unidadesBaseDialogo.hide()");
    }

    public void llamadoDialogoBuscarPeriodicidades() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                banderaSeleccionPericiodidades = true;
                context.execute("confirmarGuardar.show()");

            } else {
                listPeriodicidadesBoton = null;
                getListPericiodidadesBoton();
                index = -1;
                context.update("formularioDialogos:lovPeriodicidades");
                context.update("formularioDialogos:buscarPeriodicidadesDialogo");
                context.execute("buscarPeriodicidadesDialogo.show()");

            }
        } catch (Exception e) {
            System.err.println("ERROR LLAMADO DIALOGO BUSCAR CENTROS COSTOS " + e);
        }

    }

    public void seleccionPeriodicidades() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();

            if (guardado == true) {
                listPeriodicidades.clear();
                System.err.println("seleccionPericiodidades " + PericiodidadesSeleccionado.getNombre());
                listPeriodicidades.add(PericiodidadesSeleccionado);
                System.err.println("listPeriodicidades tamaño " + listPeriodicidades.size());
                System.err.println("listPeriodicidades nombre " + listPeriodicidades.get(0).getNombre());
                PericiodidadesSeleccionado = null;
                filterPericiodidades = null;
                aceptar = true;
                context.update("form:datosPeriodicidades");
                context.reset("formularioDialogos:lovPeriodicidades:globalFilter");
                context.execute("lovPeriodicidades.clearFilters()");
                context.execute("buscarPeriodicidadesDialogo.hide()");
                mostrarTodos = false;
                if (listPeriodicidades == null || listPeriodicidades.isEmpty()) {
                    infoRegistro = "Cantidad de registros: 0 ";
                } else {
                    infoRegistro = "Cantidad de registros: " + listPeriodicidades.size();
                }
                context.update("form:informacionRegistro");
                context.update("form:MOSTRARTODOS");
            } /*else {
             System.err.println("listPeriodicidades tamaño " + listPeriodicidades.size());
             System.err.println("listPeriodicidades nombre " + listPeriodicidades.get(0).getNombre());
             banderaSeleccionPericiodidades = true;
             context.execute("confirmarGuardar.show()");
             PericiodidadesSeleccionado = null;
             listPeriodicidades.clear();
             System.err.println("seleccionPericiodidades " + PericiodidadesSeleccionado.getNombre());
             listPeriodicidades.add(PericiodidadesSeleccionado);
             filterPericiodidades = null;
             aceptar = true;
             banderaModificacionEmpresa = 0;
             context.execute("buscarPeriodicidadesDialogo.hide()");
             context.reset("formularioDialogos:lovPeriodicidades:globalFilter");
             }*/


        } catch (Exception e) {
            System.out.println("ERROR CONTROLPERIODICIDADES.seleccionaVigencia ERROR====" + e.getMessage());
        }
    }

    public void cancelarSeleccionUnidadPorEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        PericiodidadesSeleccionado = null;
        filterPericiodidades = null;
        aceptar = true;
        index = -1;
        tipoActualizacion = -1;
        context.update("form:aceptarNCC");
        context.reset("formularioDialogos:lovPeriodicidades:globalFilter");
        context.execute("lovPeriodicidades.clearFilters()");
        context.execute("buscarPeriodicidadesDialogo.hide()");
    }
    
    private String nuevoUnidadCodigo;
    private String nuevoUnidadBaseCodigo;
    private String nuevoUnidadBaseNombre;

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        System.out.println("1...");
        if (Campo.equals("UNIDADNOMBRE")) {
            if (tipoNuevo == 1) {
                nuevaUnidad = nuevaPeriodicidad.getUnidad().getNombre();
            } else if (tipoNuevo == 2) {
                nuevaUnidad = duplicarPeriodicidad.getUnidad().getNombre();
            }

        } else if (Campo.equals("UNIDADCODIGO")) {
            if (tipoNuevo == 1) {
                nuevoUnidadCodigo = nuevaPeriodicidad.getUnidad().getCodigo();
            } else if (tipoNuevo == 2) {
                nuevoUnidadCodigo = duplicarPeriodicidad.getUnidad().getCodigo();
            }

        } else if (Campo.equals("UNIDADBASECODIGO")) {
            if (tipoNuevo == 1) {
                nuevoUnidadBaseCodigo = nuevaPeriodicidad.getUnidadbase().getCodigo();
            } else if (tipoNuevo == 2) {
                nuevoUnidadBaseCodigo = duplicarPeriodicidad.getUnidadbase().getCodigo();
            }

        } else if (Campo.equals("UNIDADBASENOMBRE")) {
            if (tipoNuevo == 1) {
                nuevoUnidadBaseNombre = nuevaPeriodicidad.getUnidadbase().getCodigo();
            } else if (tipoNuevo == 2) {
                nuevoUnidadBaseNombre = duplicarPeriodicidad.getUnidadbase().getCodigo();
            }

        }
    }

    public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("UNIDADNOMBRE")) {

            System.out.println(" NUEVA PERIODICIDAD ");
            System.out.println("NOMBRE UNIDAD : " + nuevaPeriodicidad.getUnidad().getNombre());
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETARNUEVO VALORCONFIRMAR : " + valorConfirmar);
            System.out.println("NOMBRE NUEVA UNIDAD : " + nuevaUnidad);
            nuevaPeriodicidad.getUnidad().setNombre(nuevaUnidad);
            getListaUnidades();
            for (int i = 0; i < listaUnidades.size(); i++) {
                if (listaUnidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETARNUEVO COINCIDENCIAS : " + coincidencias);
            if (coincidencias == 1) {
                nuevaPeriodicidad.setUnidad(listaUnidades.get(indiceUnicoElemento));
                listaUnidades = null;
                getListaUnidades();
            } else {
                context.update("form:tiposPeriodicidadesDialogo");
                context.execute("tiposPeriodicidadesDialogo.show()");
                tipoActualizacion = tipoNuevo;
            }

            context.update("formularioDialogos:nuevaTipoUnidads");
        }
        if (confirmarCambio.equalsIgnoreCase("UNIDADCODIGO")) {

            System.out.println(" NUEVA PERIODICIDAD ");
            System.out.println("CODIGO UNIDAD : " + nuevaPeriodicidad.getUnidad().getCodigo());
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETARNUEVO CODIGO VALORCONFIRMAR : " + valorConfirmar);
            System.out.println("NOMBRE NUEVA UNIDAD : " + nuevoUnidadCodigo);
            nuevaPeriodicidad.getUnidad().setNombre(nuevoUnidadCodigo);
            getListaUnidades();
            for (int i = 0; i < listaUnidades.size(); i++) {
                if (listaUnidades.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETARNUEVO CODIGO COINCIDENCIAS : " + coincidencias);
            if (coincidencias == 1) {
                nuevaPeriodicidad.setUnidad(listaUnidades.get(indiceUnicoElemento));
                listaUnidades = null;
                getListaUnidades();
            } else {
                context.update("form:tiposPeriodicidadesDialogo");
                context.execute("tiposPeriodicidadesDialogo.show()");
                tipoActualizacion = tipoNuevo;
            }

            context.update("formularioDialogos:nuevaTipoUnidadsCodigo");
        }
        if (confirmarCambio.equalsIgnoreCase("UNIDADBASECODIGO")) {

            System.out.println(" NUEVA PERIODICIDAD ");
            System.out.println("CODIGO BASE UNIDAD : " + nuevaPeriodicidad.getUnidadbase().getCodigo());
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETARNUEVO CODIGO VALORCONFIRMAR : " + valorConfirmar);
            System.out.println("NOMBRE NUEVA UNIDAD : " + nuevoUnidadBaseCodigo);
            nuevaPeriodicidad.getUnidadbase().setCodigo(nuevoUnidadBaseCodigo);
            getListaUnidades();
            for (int i = 0; i < listaUnidades.size(); i++) {
                if (listaUnidades.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETARNUEVO BASE CODIGO COINCIDENCIAS : " + coincidencias);
            if (coincidencias == 1) {
                nuevaPeriodicidad.setUnidadbase(listaUnidades.get(indiceUnicoElemento));
                listaUnidades = null;
                getListaUnidades();
            } else {
                context.update("form:unidadesBaseDialogo");
                context.execute("unidadesBaseDialogo.show()");
                tipoActualizacion = tipoNuevo;
            }

            context.update("formularioDialogos:nuevaCodigoBase");
        }
        if (confirmarCambio.equalsIgnoreCase("UNIDADBASENOMBRE")) {

            System.out.println(" NUEVA PERIODICIDAD ");
            System.out.println("NOMBRE BASE UNIDAD : " + nuevaPeriodicidad.getUnidadbase().getNombre());
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETARNUEVO NOMBRE VALORCONFIRMAR : " + valorConfirmar);
            System.out.println("NOMBRE NUEVA UNIDAD : " + nuevoUnidadBaseNombre);
            nuevaPeriodicidad.getUnidadbase().setNombre(nuevoUnidadBaseNombre);
            getListaUnidades();
            for (int i = 0; i < listaUnidades.size(); i++) {
                if (listaUnidades.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETARNUEVO BASE NOMBRE COINCIDENCIAS : " + coincidencias);
            if (coincidencias == 1) {
                nuevaPeriodicidad.setUnidadbase(listaUnidades.get(indiceUnicoElemento));
                listaUnidades = null;
                getListaUnidades();
            } else {
                context.update("form:unidadesBaseDialogo");
                context.execute("unidadesBaseDialogo.show()");
                tipoActualizacion = tipoNuevo;
            }

            context.update("formularioDialogos:nuevaNombreBase");
        }
    }

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        System.out.println("entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("UNIDADCODIGO")) {

            System.out.println("CONTROLPERIODICIDADES DUPLICAR CODIGO UNIDAD: " + duplicarPeriodicidad.getUnidad().getCodigo());
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETLARDUPLICADO VALORCONFIRMAR: " + valorConfirmar);
            System.out.println("CODIGO DUPLICAR: " + nuevoUnidadCodigo);
            duplicarPeriodicidad.getUnidad().setCodigo(nuevoUnidadCodigo);
            for (int i = 0; i < listaUnidades.size(); i++) {
                if (listaUnidades.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
                duplicarPeriodicidad.setUnidad(listaUnidades.get(indiceUnicoElemento));
                listaUnidades = null;
                getListaUnidades();
            } else {
                context.update("form:tiposPeriodicidadesDialogo");
                context.execute("tiposPeriodicidadesDialogo.show()");
                tipoActualizacion = tipoNuevo;
            }

            context.update("formularioDialogos:duplicarTipoPeriodicidades");
        }
        if (confirmarCambio.equalsIgnoreCase("UNIDADNOMBRE")) {

            System.out.println("CONTROLPERIODICIDADES DUPLICAR NOMBRE UNIDAD: " + duplicarPeriodicidad.getUnidad().getNombre());
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETLARDUPLICADO VALORCONFIRMAR: " + valorConfirmar);
            System.out.println("NOMBRE DUPLICAR: " + nuevaUnidad);
            duplicarPeriodicidad.getUnidad().setNombre(nuevaUnidad);
            for (int i = 0; i < listaUnidades.size(); i++) {
                if (listaUnidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
                duplicarPeriodicidad.setUnidad(listaUnidades.get(indiceUnicoElemento));
                listaUnidades = null;
                getListaUnidades();
            } else {
                context.update("form:tiposPeriodicidadesDialogo");
                context.execute("tiposPeriodicidadesDialogo.show()");
                tipoActualizacion = tipoNuevo;
            }

            context.update("formularioDialogos:duplicarTipoPeriodicidades");
        }
        if (confirmarCambio.equalsIgnoreCase("UNIDADBASECODIGO")) {

            System.out.println("CONTROLPERIODICIDADES DUPLICAR CODIGO  UNIDADBASE : " + duplicarPeriodicidad.getUnidadbase().getCodigo());
            System.out.println("CONTROLPERIODICIDADES AUTOCOMPLETLARDUPLICADO VALORCONFIRMAR: " + valorConfirmar);
            System.out.println("NOMBRE BASE DUPLICAR: " + nuevaUnidad);
            duplicarPeriodicidad.getUnidadbase().setCodigo(nuevoUnidadBaseCodigo);
            for (int i = 0; i < listaUnidades.size(); i++) {
                if (listaUnidades.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
                duplicarPeriodicidad.setUnidadbase(listaUnidades.get(indiceUnicoElemento));
                listaUnidades = null;
                getListaUnidades();
            } else {
                context.update("form:unidadesBaseDialogo");
                context.execute("unidadesBaseDialogo.show()");
                tipoActualizacion = tipoNuevo;
            }

            context.update("formularioDialogos:duplicarTipoPeriodicidades");
        }
    }

    public void asignarVariableUnidad(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:tiposPeriodicidadesDialogo");
        context.execute("tiposPeriodicidadesDialogo.show()");
    }

    public void asignarVariableUnidadBase(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:unidadesBaseDialogo");
        context.execute("unidadesBaseDialogo.show()");
    }

    public void limpiarNuevoPeriodicidades() {
        System.out.println("\n ENTRE A CONTROLPERIODICIDADES.limpiarNuevoPeriodicidades \n");
        try {
            nuevaPeriodicidad = new Periodicidades();
            nuevaPeriodicidad.setUnidad(new Unidades());
            nuevaPeriodicidad.setUnidadbase(new Unidades());
            index = -1;
        } catch (Exception e) {
            System.out.println("Error CONTROLPERIODICIDADES.LimpiarNuevoPeriodicidades ERROR=============================" + e.getMessage());
        }
    }

    public void agregarNuevoPeriodicidades() {
        System.out.println("\n ENTRE A CONTROLPERIODICIDADES.agregarNuevoPeriodicidades \n");
        try {
            int contador = 0;
            mensajeValidacion = " ";
            int duplicados = 0;
            RequestContext context = RequestContext.getCurrentInstance();

            if (nuevaPeriodicidad.getCodigo() == null) {
                mensajeValidacion = mensajeValidacion + "   *Codigo \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("codigo en Motivo Cambio Cargo: " + nuevaPeriodicidad.getCodigo());

                for (int x = 0; x < listPeriodicidades.size(); x++) {
                    if (listPeriodicidades.get(x).getCodigo().equals(nuevaPeriodicidad.getCodigo())) {
                        duplicados++;
                    }
                }
                System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

                if (duplicados > 0) {
                    mensajeValidacion = " *Que NO hayan codigos repetidos \n";
                    System.out.println("Mensaje validacion : " + mensajeValidacion);
                } else {
                    System.out.println("bandera");
                    contador++;
                }
            }
            if (nuevaPeriodicidad.getNombre() == null) {
                mensajeValidacion = mensajeValidacion + "   *Nombre \n";

            } else {
                System.out.println("Bandera : ");
                contador++;
            }
            if (nuevaPeriodicidad.getUnidad().getCodigo() == null && nuevaPeriodicidad.getUnidad().getCodigo() == null) {
                mensajeValidacion = mensajeValidacion + "   *Unidad\n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);

            } else {
                System.out.println("Bandera : ");
                contador++;
            }
            if (nuevaPeriodicidad.getUnidad().getCodigo() == null && nuevaPeriodicidad.getUnidad().getCodigo() == null) {
                mensajeValidacion = mensajeValidacion + "   *Unidad Base\n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);

            } else {
                System.out.println("Bandera : ");
                contador++;
            }
            System.out.println("CONTADOR AGREGAR : " + contador);
            if (contador == 4) {
                k++;
                l = BigInteger.valueOf(k);
                nuevaPeriodicidad.setSecuencia(l);
                if (crearPeriodicidades.contains(nuevaPeriodicidad)) {
                    System.out.println("Ya lo contengo.");
                } else {
                    crearPeriodicidades.add(nuevaPeriodicidad);

                }
                listPeriodicidades.add(nuevaPeriodicidad);
                context.update("form:datosPeriodicidades");
                infoRegistro = "Cantidad de registros: " + listPeriodicidades.size();
                context.update("form:informacionRegistro");
                nuevaPeriodicidad = new Periodicidades();
                // index = -1;
                secRegistro = null;
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                if (bandera == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
                    codigoCC.setFilterStyle("display: none; visibility: hidden;");
                    nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
                    nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
                    tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
                    tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
                    codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
                    codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
                    codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
                    codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
                    unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
                    unidadBase.setFilterStyle("display: none; visibility: hidden;");
                    RequestContext.getCurrentInstance().update("form:datosPeriodicidades");

                    bandera = 0;
                    filtrarPeriodicidades = null;
                    tipoLista = 0;
                }
                mensajeValidacion = " ";
                RequestContext.getCurrentInstance().execute("nuevoRegistroPeriodicidades.hide()");

            } else {
                contador = 0;
                context.update("form:validacionDuplicarVigencia");
                context.execute("validacionDuplicarVigencia.show()");
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLPERIODICIDADES.agregarNuevoPeriodicidades ERROR===========================" + e.getMessage());
        }
    }

    public void cargarUnidadesNuevoRegistro(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:tiposPeriodicidadesDialogo");
            context.execute("tiposPeriodicidadesDialogo.show()");
        } else if (tipoNuevo == 1) {
            tipoActualizacion = 2;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:tiposPeriodicidadesDialogo");
            context.execute("tiposPeriodicidadesDialogo.show()");
        }
    }

    public void cargarUnidadesBaseNuevoRegistro(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:unidadesBaseDialogo");
            context.execute("unidadesBaseDialogo.show()");
        } else if (tipoNuevo == 1) {
            tipoActualizacion = 2;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:unidadesBaseDialogo");
            context.execute("unidadesBaseDialogo.show()");
        }
    }

    public void mostrarDialogoPeriodicidades() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaPeriodicidad = new Periodicidades();
        nuevaPeriodicidad.setUnidad(new Unidades());
        nuevaPeriodicidad.setUnidadbase(new Unidades());
        index = -1;
        context.update("formularioDialogos:nuevoRegistroPeriodicidades");
        context.execute("nuevoRegistroPeriodicidades.show()");
    }

    public void mostrarDialogoListaEmpresas() {
        RequestContext context = RequestContext.getCurrentInstance();
        index = -1;
        context.execute("buscarPeriodicidadesDialogo.show()");
    }

    public void duplicandoPeriodicidades() {
        try {
            System.out.println("\n ENTRE A CONTROLPERIODICIDADES.duplicarPeriodicidades INDEX===" + index);

            if (index >= 0) {
                System.out.println("\n ENTRE A CONTROLPERIODICIDADES.duplicarPeriodicidades TIPOLISTA===" + tipoLista);

                duplicarPeriodicidad = new Periodicidades();
                duplicarPeriodicidad.setUnidad(new Unidades());
                duplicarPeriodicidad.setUnidadbase(new Unidades());
                k++;
                l = BigInteger.valueOf(k);
                if (tipoLista == 0) {
                    duplicarPeriodicidad.setSecuencia(l);
                    duplicarPeriodicidad.setCodigo(listPeriodicidades.get(index).getCodigo());
                    duplicarPeriodicidad.setNombre(listPeriodicidades.get(index).getNombre());
                    duplicarPeriodicidad.getUnidad().setCodigo(listPeriodicidades.get(index).getUnidad().getCodigo());
                    duplicarPeriodicidad.getUnidad().setNombre(listPeriodicidades.get(index).getUnidad().getNombre());
                    duplicarPeriodicidad.getUnidadbase().setCodigo(listPeriodicidades.get(index).getUnidadbase().getCodigo());
                    duplicarPeriodicidad.getUnidadbase().setNombre(listPeriodicidades.get(index).getUnidadbase().getNombre());

                }
                if (tipoLista == 1) {

                    duplicarPeriodicidad.setSecuencia(l);
                    duplicarPeriodicidad.setCodigo(filtrarPeriodicidades.get(index).getCodigo());
                    duplicarPeriodicidad.setNombre(filtrarPeriodicidades.get(index).getNombre());
                    duplicarPeriodicidad.getUnidad().setCodigo(filtrarPeriodicidades.get(index).getUnidad().getCodigo());
                    duplicarPeriodicidad.getUnidad().setNombre(filtrarPeriodicidades.get(index).getUnidad().getNombre());
                    duplicarPeriodicidad.getUnidadbase().setCodigo(filtrarPeriodicidades.get(index).getUnidadbase().getCodigo());
                    duplicarPeriodicidad.getUnidadbase().setNombre(filtrarPeriodicidades.get(index).getUnidadbase().getNombre());

                }

                RequestContext context = RequestContext.getCurrentInstance();
                context.update("formularioDialogos:duplicarUnidads");
                context.execute("DuplicarRegistroPeriodicidades.show()");
                index = -1;
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLPERIODICIDADES DUPLICANDOPERIODICIDADESERROR" + e);
        }
    }

    public void limpiarDuplicarPeriodicidades() {
        System.out.println("\n ENTRE A CONTROLPERIODICIDADES LIMPIARDUPLICARPERIODICIDADES \n");
        try {
            duplicarPeriodicidad = new Periodicidades();
            duplicarPeriodicidad.setUnidad(new Unidades());
            duplicarPeriodicidad.setUnidadbase(new Unidades());
        } catch (Exception e) {
            System.out.println("ERROR  CONTROLPERIODICIDADES LIMPIARDUPLICARPERIODICIDADES ERROR : " + e);
        }

    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLPERIODICIDADES");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;

        if (duplicarPeriodicidad.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + duplicarPeriodicidad.getCodigo());

            for (int x = 0; x < listPeriodicidades.size(); x++) {
                if (listPeriodicidades.get(x).getCodigo().equals(duplicarPeriodicidad.getCodigo())) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO hayan codigos repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }

        }
        if (duplicarPeriodicidad.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarPeriodicidad.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   * Nombre \n";

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarPeriodicidad.getUnidad().getCodigo() == null && duplicarPeriodicidad.getUnidad().getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Unidad \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarPeriodicidad.getUnidadbase().getCodigo() == null && duplicarPeriodicidad.getUnidadbase().getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Unidad Base \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (contador == 4) {
            if (crearPeriodicidades.contains(duplicarPeriodicidad)) {
                System.out.println("Ya lo contengo.");
            } else {
                listPeriodicidades.add(duplicarPeriodicidad);
            }
            crearPeriodicidades.add(duplicarPeriodicidad);
            context.update("form:datosPeriodicidades");
            infoRegistro = "Cantidad de registros: " + listPeriodicidades.size();
            context.update("form:informacionRegistro");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                //1
                nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
                nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
                //2
                tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
                tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
                //3 COMBO BOX
                codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
                codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
                //4
                codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
                codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
                //5 COMBO BOX
                unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
                unidadBase.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
                //6
                RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
                bandera = 0;
                filtrarPeriodicidades = null;
                tipoLista = 0;
            }
            duplicarPeriodicidad = new Periodicidades();
            duplicarPeriodicidad.setUnidad(new Unidades());
            RequestContext.getCurrentInstance().execute("DuplicarRegistroPeriodicidades.hide()");
            mensajeValidacion = " ";

        } else {
            contador = 0;
            context.update("form:validacionDuplicarVigencia");
            context.execute("validacionDuplicarVigencia.show()");
        }
    }

    private BigInteger contarCPCompromisosPeriodicidad;
    private BigInteger contarDetallesPeriodicidadesPeriodicidad;
    private BigInteger contarEersPrestamosDtosPeriodicidad;
    private BigInteger contarEmpresasPeriodicidad;
    private BigInteger contarFormulasAseguradasPeriodicidad;
    private BigInteger contarFormulasContratosPeriodicidad;
    private BigInteger contarGruposProvisionesPeriodicidad;
    private BigInteger contarNovedadPeriodicidad;
    private BigInteger contadorInterconHelisa;
    private BigInteger contadorInterconSapbo;

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        System.out.println("TIPOLISTA = " + tipoLista);
        BigInteger pruebilla;
        try {
            if (tipoLista == 0) {
                //  System.err.println("Control Secuencia de ControlTiposEmpresas secuencia centro costo" + listPeriodicidades.get(index).getSecuencia());
                contarCPCompromisosPeriodicidad = administrarPeriodicidades.contarCPCompromisosPeriodicidad(listPeriodicidades.get(index).getSecuencia());
                //  System.out.println("ControlBetaCC contadorComprobantesContables: " + contadorComprobantesContables);
                contarDetallesPeriodicidadesPeriodicidad = administrarPeriodicidades.contarDetallesPeriodicidadesPeriodicidad(listPeriodicidades.get(index).getSecuencia());;
                // System.out.println("SE TOTEA ControlBetaCC contadorDetallesCCConsolidador: " + contadorDetallesCCConsolidador);
                contarEersPrestamosDtosPeriodicidad = administrarPeriodicidades.contarEersPrestamosDtosPeriodicidad(listPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC contadorEmpresas: " + contadorEmpresas);
                contarEmpresasPeriodicidad = administrarPeriodicidades.contarEmpresasPeriodicidad(listPeriodicidades.get(index).getSecuencia());
                //  System.out.println("ControlBetaCC contadorEstructuras " + contadorEstructuras);
                contarFormulasAseguradasPeriodicidad = administrarPeriodicidades.contarFormulasAseguradasPeriodicidad(listPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC: contadorDetallesCCDetalle" + contadorDetallesCCDetalle);
                contarFormulasContratosPeriodicidad = administrarPeriodicidades.contarFormulasContratosPeriodicidad(listPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC: contadorInterconCondor" + contadorInterconCondor);
                contarGruposProvisionesPeriodicidad = administrarPeriodicidades.contarGruposProvisionesPeriodicidad(listPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC:contadorInterconDynamics " + contadorInterconDynamics);
                contarNovedadPeriodicidad = administrarPeriodicidades.contarNovedadesPeriodicidad(listPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC: contadorInterconGeneral" + contadorInterconGeneral);
                contadorInterconHelisa = administrarPeriodicidades.contarParametrosCambiosMasivosPeriodicidad(listPeriodicidades.get(index).getSecuencia());
                //            System.out.println("ControlBetaCC: contadorInterconHelisa" + contadorInterconHelisa);
                contadorInterconSapbo = administrarPeriodicidades.contarVigenciasFormasPagosPeriodicidad(listPeriodicidades.get(index).getSecuencia());
                //            System.out.println("ControlBetaCC: contadorInterconSapbo" + contadorInterconSapbo);
            } else {
                //               System.err.println(" FILTRAR  FILTRAR  FILTRAR  FILTRAR Control Secuencia de ControlTiposEmpresas secuencia centro costo" + listPeriodicidades.get(index).getSecuencia());
                contarCPCompromisosPeriodicidad = administrarPeriodicidades.contarCPCompromisosPeriodicidad(filtrarPeriodicidades.get(index).getSecuencia());
                //  System.out.println("ControlBetaCC contadorComprobantesContables: " + contadorComprobantesContables);
                contarDetallesPeriodicidadesPeriodicidad = administrarPeriodicidades.contarDetallesPeriodicidadesPeriodicidad(filtrarPeriodicidades.get(index).getSecuencia());;
                // System.out.println("SE TOTEA ControlBetaCC contadorDetallesCCConsolidador: " + contadorDetallesCCConsolidador);
                contarEersPrestamosDtosPeriodicidad = administrarPeriodicidades.contarEersPrestamosDtosPeriodicidad(filtrarPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC contadorEmpresas: " + contadorEmpresas);
                contarEmpresasPeriodicidad = administrarPeriodicidades.contarEmpresasPeriodicidad(filtrarPeriodicidades.get(index).getSecuencia());
                //  System.out.println("ControlBetaCC contadorEstructuras " + contadorEstructuras);
                contarFormulasAseguradasPeriodicidad = administrarPeriodicidades.contarFormulasAseguradasPeriodicidad(filtrarPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC: contadorDetallesCCDetalle" + contadorDetallesCCDetalle);
                contarFormulasContratosPeriodicidad = administrarPeriodicidades.contarFormulasContratosPeriodicidad(filtrarPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC: contadorInterconCondor" + contadorInterconCondor);
                contarGruposProvisionesPeriodicidad = administrarPeriodicidades.contarGruposProvisionesPeriodicidad(filtrarPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC:contadorInterconDynamics " + contadorInterconDynamics);
                contarNovedadPeriodicidad = administrarPeriodicidades.contarNovedadesPeriodicidad(filtrarPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC: contadorInterconGeneral" + contadorInterconGeneral);
                contadorInterconHelisa = administrarPeriodicidades.contarParametrosCambiosMasivosPeriodicidad(filtrarPeriodicidades.get(index).getSecuencia());
                //            System.out.println("ControlBetaCC: contadorInterconHelisa" + contadorInterconHelisa);
                contadorInterconSapbo = administrarPeriodicidades.contarVigenciasFormasPagosPeriodicidad(filtrarPeriodicidades.get(index).getSecuencia());
                // System.out.println("ControlBetaCC: contadorInterconSapbo" + contadorInterconSapbo);
                //       System.out.println("ControlBetaCC: contadorVigenciasProrrateos" + contadorVigenciasProrrateos);
                //pruebilla = administrarPeriodicidades.sumaTotal(filtrarPeriodicidades.get(index).getSecuencia());
                //System.err.println("pruebilla :::::::::::::::::::::::::::::::: " + pruebilla);
            }
            if (contarDetallesPeriodicidadesPeriodicidad.equals(new BigInteger("0"))
                    && contarEersPrestamosDtosPeriodicidad.equals(new BigInteger("0"))
                    && contarEmpresasPeriodicidad.equals(new BigInteger("0"))
                    && contarFormulasAseguradasPeriodicidad.equals(new BigInteger("0"))
                    && contarFormulasContratosPeriodicidad.equals(new BigInteger("0"))
                    && contarGruposProvisionesPeriodicidad.equals(new BigInteger("0"))
                    && contarNovedadPeriodicidad.equals(new BigInteger("0"))
                    && contadorInterconHelisa.equals(new BigInteger("0"))
                    && contadorInterconSapbo.equals(new BigInteger("0"))
                    && contarCPCompromisosPeriodicidad.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoUnidad();
            } else {

                System.out.println("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                index = -1;
                contarCPCompromisosPeriodicidad = new BigInteger("-1");
                contarDetallesPeriodicidadesPeriodicidad = new BigInteger("-1");
                contarEersPrestamosDtosPeriodicidad = new BigInteger("-1");
                contarEmpresasPeriodicidad = new BigInteger("-1");
                contarFormulasAseguradasPeriodicidad = new BigInteger("-1");
                contarFormulasContratosPeriodicidad = new BigInteger("-1");
                contarGruposProvisionesPeriodicidad = new BigInteger("-1");
                contarNovedadPeriodicidad = new BigInteger("-1");
                contadorInterconHelisa = new BigInteger("-1");
                contadorInterconSapbo = new BigInteger("-1");
            }
        } catch (Exception e) {
            System.err.println("ERROR CONTROL BETA CENTROS COSTOS verificarBorrado ERROR " + e);
        }
    }

    public void borrandoUnidad() {
        try {
            if (index >= 0) {
                if (tipoLista == 0) {
                    if (!modificarPeriodicidades.isEmpty() && modificarPeriodicidades.contains(listPeriodicidades.get(index))) {
                        int modIndex = modificarPeriodicidades.indexOf(listPeriodicidades.get(index));
                        modificarPeriodicidades.remove(modIndex);
                        borrarPeriodicidades.add(listPeriodicidades.get(index));
                    } else if (!crearPeriodicidades.isEmpty() && crearPeriodicidades.contains(listPeriodicidades.get(index))) {
                        int crearIndex = crearPeriodicidades.indexOf(listPeriodicidades.get(index));
                        crearPeriodicidades.remove(crearIndex);
                    } else {

                        borrarPeriodicidades.add(listPeriodicidades.get(index));
                    }
                    listPeriodicidades.remove(index);
                }
                if (tipoLista == 1) {
                    if (!modificarPeriodicidades.isEmpty() && modificarPeriodicidades.contains(filtrarPeriodicidades.get(index))) {
                        System.out.println("\n 6 ENTRE A CONTROLPERIODICIDADES.borrarUnidad tipolista==1 try if if if filtrarPeriodicidades.get(index).getCodigo()" + filtrarPeriodicidades.get(index).getCodigo());

                        int modIndex = modificarPeriodicidades.indexOf(filtrarPeriodicidades.get(index));
                        modificarPeriodicidades.remove(modIndex);
                        borrarPeriodicidades.add(filtrarPeriodicidades.get(index));
                    } else if (!crearPeriodicidades.isEmpty() && crearPeriodicidades.contains(filtrarPeriodicidades.get(index))) {
                        System.out.println("\n 7 ENTRE A CONTROLPERIODICIDADES.borrarUnidad tipolista==1 try if if if filtrarPeriodicidades.get(index).getCodigo()" + filtrarPeriodicidades.get(index).getCodigo());
                        int crearIndex = crearPeriodicidades.indexOf(filtrarPeriodicidades.get(index));
                        crearPeriodicidades.remove(crearIndex);
                    } else {
                        System.out.println("\n 8 ENTRE A CONTROLPERIODICIDADES.borrarUnidad tipolista==1 try if if if filtrarPeriodicidades.get(index).getCodigo()" + filtrarPeriodicidades.get(index).getCodigo());
                        borrarPeriodicidades.add(filtrarPeriodicidades.get(index));
                    }
                    int VCIndex = listPeriodicidades.indexOf(filtrarPeriodicidades.get(index));
                    listPeriodicidades.remove(VCIndex);
                    filtrarPeriodicidades.remove(index);
                }

                RequestContext context = RequestContext.getCurrentInstance();
                index = -1;
                System.err.println("verificar Borrado " + guardado);
                if (guardado == true) {
                    guardado = false;
                }
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                context.update("form:datosPeriodicidades");
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLPERIODICIDADES BORRARPERIODICIDAD ERROR " + e);
        }
    }

    public void guardarCambiosPeriodicidad() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias Localizacion");
            if (!borrarPeriodicidades.isEmpty()) {
                administrarPeriodicidades.borrarPeriodicidades(borrarPeriodicidades);
                //mostrarBorrados
                registrosBorrados = borrarPeriodicidades.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarPeriodicidades.clear();
            }
            if (!modificarPeriodicidades.isEmpty()) {
                administrarPeriodicidades.modificarPeriodicidades(modificarPeriodicidades);
                modificarPeriodicidades.clear();
            }
            if (!crearPeriodicidades.isEmpty()) {
                administrarPeriodicidades.crearPeriodicidades(crearPeriodicidades);
                crearPeriodicidades.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listPeriodicidades = null;
            context.update("form:datosTipoUnidad");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            context.update("form:growl");
            k = 0;
            guardado = true;

            if (banderaSeleccionPericiodidades == true) {
                listPeriodicidadesBoton = null;
                getListPericiodidadesBoton();
                index = -1;
                context.update("formularioDialogos:lovPeriodicidades");
                context.execute("buscarPeriodicidadesDialogo.show()");
                banderaSeleccionPericiodidades = false;
            }
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        System.out.println("\n ENTRE A CONTROLPERIODICIDADES ACTIVARCTRLF11 \n");
        FacesContext c = FacesContext.getCurrentInstance();
        try {

            if (bandera == 0) {
                tamano = 250;
                System.out.println("Activar");
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
                codigoCC.setFilterStyle("width: width: 85%");
                nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
                nombreUnidad.setFilterStyle("width: width: 85%");
                tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
                tipoUnidad.setFilterStyle("width: 85%;");
                codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
                codigoUnidad.setFilterStyle("width: 85%;");
                codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
                codigoUnidadbase.setFilterStyle("width: 85%;");
                unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
                unidadBase.setFilterStyle("width: 85%;");
                RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
                bandera = 1;
            } else if (bandera == 1) {
                System.out.println("Desactivar");
                //0
                tamano = 270;
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombreUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:nombreUnidad");
                nombreUnidad.setFilterStyle("display: none; visibility: hidden;");
                tipoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:tipoUnidad");
                tipoUnidad.setFilterStyle("display: none; visibility: hidden;");
                codigoUnidad = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidad");
                codigoUnidad.setFilterStyle("display: none; visibility: hidden;");
                codigoUnidadbase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:codigoUnidadbase");
                codigoUnidadbase.setFilterStyle("display: none; visibility: hidden;");
                unidadBase = (Column) c.getViewRoot().findComponent("form:datosPeriodicidades:unidadBase");
                unidadBase.setFilterStyle("display: none; visibility: hidden;");

                RequestContext.getCurrentInstance().update("form:datosPeriodicidades");
                bandera = 0;
                filtrarPeriodicidades = null;
                tipoLista = 0;
            }
        } catch (Exception e) {

            System.out.println("ERROR CONTROLPERIODICIDADES ACTICARCTRLF11 ERROR : " + e);
        }
    }

    public void editarCelda() {
        try {
            System.out.println("\n ENTRE A editarCelda INDEX  " + index);
            editarUnidad = new Periodicidades();
            editarUnidad.setUnidad(new Unidades());
            editarUnidad.setUnidadbase(new Unidades());
            if (index >= 0) {
                System.out.println("\n ENTRE AeditarCelda TIPOLISTA " + tipoLista);
                if (tipoLista == 0) {
                    editarUnidad = listPeriodicidades.get(index);
                }
                if (tipoLista == 1) {
                    editarUnidad = filtrarPeriodicidades.get(index);
                }
                RequestContext context = RequestContext.getCurrentInstance();
                System.out.println("CONTROLPERIODICIDADES : Entro a editar... valor celda: " + cualCelda);
                if (cualCelda == 0) {
                    context.update("formularioDialogos:editarCCC");
                    context.execute("editarCCC.show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    context.update("formularioDialogos:editarNCC");
                    context.execute("editarNCC.show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    context.update("formularioDialogos:editarTCC");
                    context.execute("editarTCC.show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    context.update("formularioDialogos:editarMO");
                    context.execute("editarMO.show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    context.update("formularioDialogos:editarCAT");
                    context.execute("editarCAT.show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    context.update("formularioDialogos:editarO");
                    context.execute("editarO.show()");
                    cualCelda = -1;
                }
            }
            index = -1;
        } catch (Exception E) {
            System.out.println("ERROR CONTROLPERIODICIDADES EDITARCELDA ERROR " + E);
        }
    }

    public void listaValoresBoton() {

        try {
            if (index >= 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                System.out.println("\n ListaValoresBoton \n");
                if (cualCelda == 2) {
                    context.update("formularioDialogos:tiposPeriodicidadesDialogo");
                    context.execute("tiposPeriodicidadesDialogo.show()");
                    tipoActualizacion = 0;
                }
                if (cualCelda == 3) {
                    context.update("formularioDialogos:tiposPeriodicidadesDialogo");
                    context.execute("tiposPeriodicidadesDialogo.show()");
                    tipoActualizacion = 0;
                }
                if (cualCelda == 4) {
                    context.update("formularioDialogos:unidadesBaseDialogo");
                    context.execute("unidadesBaseDialogo.show()");
                    tipoActualizacion = 0;
                }
                if (cualCelda == 5) {
                    context.update("formularioDialogos:unidadesBaseDialogo");
                    context.execute("unidadesBaseDialogo.show()");
                    tipoActualizacion = 0;
                }
            }
        } catch (Exception e) {
            System.out.println("\n ERROR CONTROLPERIODICIDADES LISTAVALORESBOTON ERROR " + e);

        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPeriodicidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Periodicidades", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    /**
     *
     * @throws IOException
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPeriodicidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Periodicidades", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listPeriodicidades.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "PERIODICIDADES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("PERIODICIDADES")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }

        }
        index = -1;
    }

//-----------------------------------------------------------------------------**
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

    public List<Periodicidades> getListPeriodicidades() {
        if (listPeriodicidades == null) {
            listPeriodicidades = administrarPeriodicidades.consultarPeriodicidades();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listPeriodicidades == null || listPeriodicidades.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listPeriodicidades.size();
        }
        context.update("form:informacionRegistro");
        return listPeriodicidades;
    }

    public void setListPeriodicidades(List<Periodicidades> listPeriodicidades) {
        this.listPeriodicidades = listPeriodicidades;
    }
    private String infoRegistroPeriodicidadesBoton;

    public List<Periodicidades> getListPericiodidadesBoton() {
        try {
            if (listPeriodicidadesBoton == null) {
                listPeriodicidadesBoton = administrarPeriodicidades.consultarPeriodicidades();
                //listPeriodicidadesBoton = listPeriodicidades;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listPeriodicidadesBoton == null || listPeriodicidadesBoton.isEmpty()) {
                infoRegistroPeriodicidadesBoton = "Cantidad de registros: 0 ";
            } else {
                infoRegistroPeriodicidadesBoton = "Cantidad de registros: " + listPeriodicidadesBoton.size();
            }
            context.update("form:infoRegistroPeriodicidadesBoton");
            return listPeriodicidadesBoton;
        } catch (Exception e) {
            System.out.println("CONTROL PERIODICIDADES : Error al recibir los Periodicidades de la empresa seleccionada /n" + e.getMessage());
            return null;
        }
    }

    public void setListPericiodidadesBoton(List<Periodicidades> listPeriodicidadesBoton) {
        this.listPeriodicidadesBoton = listPeriodicidadesBoton;
    }

    public void setListPericiodidades(List<Periodicidades> listPeriodicidades) {
        this.listPeriodicidades = listPeriodicidades;
    }

    public List<Periodicidades> getFiltrarPeriodicidades() {
        return filtrarPeriodicidades;
    }

    public void setFiltrarPeriodicidades(List<Periodicidades> filtrarPeriodicidades) {
        this.filtrarPeriodicidades = filtrarPeriodicidades;
    }

    public Periodicidades getNuevaPeriodicidad() {
        if (nuevaPeriodicidad == null) {
            nuevaPeriodicidad = new Periodicidades();
        }
        return nuevaPeriodicidad;
    }

    public void setNuevaPeriodicidad(Periodicidades nuevaPeriodicidad) {
        this.nuevaPeriodicidad = nuevaPeriodicidad;
    }

    public Periodicidades getDuplicarPeriodicidad() {
        return duplicarPeriodicidad;
    }

    public void setDuplicarPeriodicidad(Periodicidades duplicarPeriodicidad) {
        this.duplicarPeriodicidad = duplicarPeriodicidad;
    }
    private String infoRegistroUnidades;
    private String infoRegistroUnidades1;

    public List<Unidades> getListaUnidades() {
        if (listaUnidades == null) {
            listaUnidades = administrarPeriodicidades.consultarLOVUnidades();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaUnidades == null || listaUnidades.isEmpty()) {
            infoRegistroUnidades = "Cantidad de registros: 0 ";
            infoRegistroUnidades1 = "Cantidad de registros: 0 ";
        } else {
            infoRegistroUnidades = "Cantidad de registros: " + listaUnidades.size();
            infoRegistroUnidades1 = "Cantidad de registros: " + listaUnidades.size();
        }
        context.update("form:infoRegistroUnidades");
        context.update("form:infoRegistroUnidades1");
        return listaUnidades;
    }

    public void setListaUnidades(List<Unidades> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public List<Unidades> getFiltradoUnidades() {
        return filtradoUnidades;
    }

    public void setFiltradoUnidades(List<Unidades> filtradoUnidades) {
        this.filtradoUnidades = filtradoUnidades;
    }

    public Unidades getUnidadSeleccionada() {
        return unidadSeleccionada;
    }

    public void setUnidadSeleccionada(Unidades unidadSeleccionada) {
        this.unidadSeleccionada = unidadSeleccionada;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<Periodicidades> getFilterPericiodidades() {
        return filterPericiodidades;
    }

    public void setFilterPericiodidades(List<Periodicidades> filterPericiodidades) {
        this.filterPericiodidades = filterPericiodidades;
    }

    public Periodicidades getPericiodidadesSeleccionado() {
        return PericiodidadesSeleccionado;
    }

    public void setPericiodidadesSeleccionado(Periodicidades PericiodidadesSeleccionado) {
        this.PericiodidadesSeleccionado = PericiodidadesSeleccionado;
    }

    public Periodicidades getEditarUnidad() {
        return editarUnidad;
    }

    public void setEditarUnidad(Periodicidades editarUnidad) {
        this.editarUnidad = editarUnidad;
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

    public Periodicidades getPeriocidadSeleccionadaTabla() {
        return periocidadSeleccionadaTabla;
    }

    public void setPeriocidadSeleccionadaTabla(Periodicidades periocidadSeleccionadaTabla) {
        this.periocidadSeleccionadaTabla = periocidadSeleccionadaTabla;
    }

    public boolean isMostrarTodos() {
        return mostrarTodos;
    }

    public void setMostrarTodos(boolean mostrarTodos) {
        this.mostrarTodos = mostrarTodos;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroPeriodicidadesBoton() {
        return infoRegistroPeriodicidadesBoton;
    }

    public void setInfoRegistroPeriodicidadesBoton(String infoRegistroPeriodicidadesBoton) {
        this.infoRegistroPeriodicidadesBoton = infoRegistroPeriodicidadesBoton;
    }

    public String getInfoRegistroUnidades() {
        return infoRegistroUnidades;
    }

    public void setInfoRegistroUnidades(String infoRegistroUnidades) {
        this.infoRegistroUnidades = infoRegistroUnidades;
    }

    public String getInfoRegistroUnidades1() {
        return infoRegistroUnidades1;
    }

    public void setInfoRegistroUnidades1(String infoRegistroUnidades1) {
        this.infoRegistroUnidades1 = infoRegistroUnidades1;
    }

}
